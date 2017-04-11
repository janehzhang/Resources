package tydic.meta.module.report.issue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.report.ReportConstant;
import tydic.meta.sys.code.CodeManager;
import tydic.meta.web.session.SessionManager;

/**
 * 审核数据DAO
 * @author 李国民
 * Date：2012-03-07
 */
public class AuditDataDAO extends MetaBaseDAO{

	private String provinceFlag = "0000";		//全省地域code
	private String isZone = "2";		//地域维度标识
	private String isTime = "1";		//时间维度标识
	private String isD = "11"; 			//按天审核标识
	private String isM = "22";			//按月审核标识
	private int mappingMaxCount = 50000;	//缓存的最大数量
	private static Map<String,String> codeMapping = new HashMap<String,String> ();	//转换维度值map
	
	/**
	 * 查询所有审核数据
	 * @param data 查询过滤条件
	 * @param page 分页
	 * @return
	 */
	public List<Map<String,Object>> getAuditData(Map<String,Object> data,Page page){ 
		Map<String,Object> user = SessionManager.getCurrentUser();
		int zoneId = MapUtils.getIntValue(user, "zoneId");
		String zoneIdSql = "SELECT T.ZONE_CODE FROM META_DIM_ZONE T WHERE T.ZONE_ID=?";
		Map<String,Object> zoneCodeMap = getDataAccess().queryForMap(zoneIdSql, zoneId);
		String zoneCode = zoneCodeMap.get("ZONE_CODE").toString();		//地域编码
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sqlBuffer = new StringBuffer("SELECT T.ISSUE_ID,T.TABLE_ALIAS,M.AUDIT_TYPE AS DATA_PERIOD," +
				" N.DATA_DATE,N.AUDIT_CONCLUDE,M.MIN_DATE,M.MAX_DATE,T.TABLE_ID,M.AUDIT_PROP " +
				" FROM META_RPT_MODEL_ISSUE_CONFIG T " +
				" LEFT JOIN META_RPT_DATA_AUDIT_CFG M ON T.ISSUE_ID = M.ISSUE_ID" +
				" LEFT JOIN (SELECT * FROM (SELECT L.*," +
				" row_number() over(partition by L.AUDIT_CFG_ID ORDER BY L.AUDIT_TIME DESC) RN " +
				" FROM META_RPT_DATA_AUDIT_LOG L) WHERE RN=1) N ON M.AUDIT_CFG_ID = N.AUDIT_CFG_ID" +
				" LEFT JOIN META_TABLES B ON T.TABLE_ID=B.TABLE_ID " +
				" WHERE 1=1 AND B.TABLE_STATE=1 AND T.AUDIT_TYPE=1 " +
				" AND T.ISSUE_STATE=1 AND M.AUDIT_PROP=? " +
				" AND to_date(M.MIN_DATE,'yyyy-mm-dd hh24:mi:ss') < sysdate");
		
		if(zoneCode.equals(provinceFlag)){	//如果是全省审核，添加审核全省数据条件
			params.add(1); 
		}else{		//否则为分公司审核
			params.add(2); 
		}
		//如果数据源不为空或者值不为-1，则加查询条件
		if(data.get("dataSourceId")!=null&&!data.get("dataSourceId").toString().equals("-1")){
			sqlBuffer.append(" AND B.DATA_SOURCE_ID=?");
			params.add(data.get("dataSourceId"));
		}		
		//如果所属用户不为空，则加查询条件
		if(data.get("tableOwner")!=null&&!data.get("tableOwner").toString().equals("")){
			sqlBuffer.append(" AND B.TABLE_OWNER=?");
			params.add(data.get("tableOwner"));
		}		
		//如果层次分类不为空或者值不为-1，则加查询条件
		if(data.get("tableTypeId")!=null&&!data.get("tableTypeId").toString().equals("-1")){
			sqlBuffer.append(" AND B.TABLE_TYPE_ID=?");
			params.add(data.get("tableTypeId"));
		}	
		//如果业务类型不为空，则加查询条件
		if(data.get("tableGroupId")!=null&&!data.get("tableGroupId").toString().trim().equals("")){
			sqlBuffer.append(" AND B.TABLE_GROUP_ID=?");
			params.add(data.get("tableGroupId"));
		}	
		//如果关键字不为空，则加查询条件
		if(data.get("keyword")!=null&&!data.get("keyword").toString().trim().equals("")){
			sqlBuffer.append(" AND T.TABLE_ALIAS LIKE ? ESCAPE '/' ");
            params.add( SqlUtils.allLikeBindParam(data.get("keyword").toString().trim()));
		}
		sqlBuffer.append(" ORDER BY T.ISSUE_ID DESC");
        String sql = sqlBuffer.toString();
        if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
        List<Map<String,Object>> rs = getDataAccess().queryForList(sql,params.toArray());
        //加入码表信息
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("SHOW_DATA_PERIOD", CodeManager.getName(ReportConstant.REPORT_DATA_CYCLE,MapUtils.getString(map,"DATA_PERIOD")));
                map.put("AUDIT_CONCLUDE", CodeManager.getName(ReportConstant.REPORT_IS_SHOW,MapUtils.getString(map,"AUDIT_CONCLUDE")));
                map.put("SHOW_MAX_DATE", CodeManager.getName(ReportConstant.REPORT_PRT_AGREED,MapUtils.getString(map,"MAX_DATE")));
            }
        }
		return rs;
	}	

	/**
	 * 查询某一模型下的审核日志
	 * @param data 模型id，数据日期
	 * @param page 分页
	 * @return
	 */
	public List<Map<String,Object>> getViewData(Date dataDate,String issueId,String dataPeriod,Page page){ 
		List<Object> params = new ArrayList<Object>();
		params.add(issueId);
		StringBuffer sqlBuffer = new StringBuffer("SELECT T.AUDIT_LOG_ID,T.DATA_DATE," +
				" to_char(T.AUDIT_TIME,'yyyy-mm-dd hh24:mi:ss') as AUDIT_TIME," +
				" T.AUDIT_CONCLUDE,T.SHOW_OPINION,M.MAX_DATE,M.ISSUE_ID" +
				" FROM (SELECT * FROM (SELECT L.*,row_number() " +
					" over(partition by L.DATA_DATE ORDER BY L.AUDIT_TIME DESC) RN" +
					" FROM META_RPT_DATA_AUDIT_LOG L)  WHERE RN = 1) T" +
				" LEFT JOIN META_RPT_DATA_AUDIT_CFG M ON T.AUDIT_CFG_ID = M.AUDIT_CFG_ID" +
				" WHERE M.ISSUE_ID=?");
		if(dataDate!=null){
			sqlBuffer.append(" AND T.DATA_DATE=?");
			if(dataPeriod.equals("11")){
				params.add(DateUtil.format(dataDate,"yyyyMMdd"));
			}else{
				params.add(DateUtil.format(dataDate,"yyyyMM"));
			}
		}
		sqlBuffer.append(" ORDER BY T.DATA_DATE DESC");
        String sql = sqlBuffer.toString();
        if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
        List<Map<String,Object>> rs = getDataAccess().queryForList(sql,params.toArray());
        //加入码表信息
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("AUDIT_CONCLUDE", CodeManager.getName(ReportConstant.REPORT_IS_SHOW,MapUtils.getString(map,"AUDIT_CONCLUDE")));
                map.put("SHOW_MAX_DATE", CodeManager.getName(ReportConstant.REPORT_PRT_AGREED,MapUtils.getString(map,"MAX_DATE")));
            }
        }
		return rs;
	}

    /**
	 * 根据模型Id，得到模型基本信息
     * @param issueId 模型id
     * @return
     */
	public Map<String,Object> getIssueInfo(String issueId){
		String sql = "SELECT D.DATA_SOURCE_NAME,C.TABLE_OWNER,C.TABLE_NAME,A.AUDIT_TYPE AS DATA_PERIOD," +
				" A.MAX_DATE,T.TABLE_ALIAS,T.ISSUE_NOTE,C.TABLE_ID,C.DATA_SOURCE_ID,A.AUDIT_CFG_ID" +
				" FROM META_RPT_MODEL_ISSUE_CONFIG T" +
				" LEFT JOIN META_RPT_DATA_AUDIT_CFG A ON A.ISSUE_ID = T.ISSUE_ID" +
				" LEFT JOIN META_TABLES C ON C.TABLE_ID = T.TABLE_ID" +
				" LEFT JOIN META_DATA_SOURCE D ON D.DATA_SOURCE_ID = C.DATA_SOURCE_ID" +
				" WHERE C.TABLE_STATE = 1 AND T.ISSUE_ID = ?";

        Map<String,Object> rs = getDataAccess().queryForMap(sql,issueId);
        //加入码表信息
        if(rs!=null&&rs.size()>0){
        	rs.put("SHOW_DATA_PERIOD", CodeManager.getName(ReportConstant.REPORT_DATA_CYCLE,MapUtils.getString(rs,"DATA_PERIOD")));
        	rs.put("MAX_DATE", CodeManager.getName(ReportConstant.REPORT_PRT_AGREED,MapUtils.getString(rs,"MAX_DATE")));
        }
		return rs;
	}
	
	/**
	 * 根据模型Id，数据日期，获取审核的信息
	 * @param issueId 模型id
	 * @param dataDate 数据日期
	 * @param dataArea 地域
	 * @return
	 */
	public Map<String,Object> getAuditInfo(String issueId, String dataDate, String dataArea){
		String sql = "SELECT T.AUDIT_LOG_ID,T.AUDIT_CONCLUDE,T.AUDIT_OPINION,T.SHOW_OPINION,T.AUDIT_MARK" +
				" FROM META_RPT_DATA_AUDIT_LOG T" +
				" LEFT JOIN META_RPT_DATA_AUDIT_CFG A ON A.AUDIT_CFG_ID = T.AUDIT_CFG_ID" +
				" WHERE A.ISSUE_ID = ? AND T.DATA_DATE=? AND T.DATA_AREA = ?";

        Map<String,Object> rs = getDataAccess().queryForMap(sql,new Object[]{issueId,dataDate,dataArea});
        //加入码表信息
        if(rs!=null&&rs.size()>0){
        	rs.put("IS_AUDIT_CONCLUDE", CodeManager.getName(ReportConstant.REPORT_IS_SHOW,MapUtils.getString(rs,"AUDIT_CONCLUDE")));
        }
		return rs;
	}

    /**
     * 根据带地域宏变量的tableName，表类tableId，查询对应宏变量的所有实体表
     * @param tableName 带地域宏变量的表类名
     * @param tableId  表类id
     * @return
     */
	public List<Map<String,Object>> getTableInstList(String tableName,String tableId){
		String sql = "SELECT T.TABLE_NAME FROM META_TABLE_INST T" +
				" LEFT JOIN META_TABLES M ON T.TABLE_ID = M.TABLE_ID" +
				" WHERE M.TABLE_STATE = 1 AND T.TABLE_ID = ? AND T.TABLE_NAME like ?";
		return getDataAccess().queryForList(sql,new Object[]{tableId,tableName});
	}
	
    /**
     * 根据实体表名tableName，表类tableId，查询实体表是否存在
     * @param tableName 表类名
     * @param tableId  表类id
     * @return
     */
	public Map<String,Object> getTableInst(String tableName,String tableId){
		String sql = "SELECT M.TABLE_ID,M.TABLE_VERSION,M.DATA_SOURCE_ID,T.TABLE_NAME,T.TABLE_OWNER" +
				" FROM META_TABLE_INST T" +
				" LEFT JOIN META_TABLES M ON T.TABLE_ID = M.TABLE_ID" +
				" WHERE M.TABLE_STATE = 1 AND T.TABLE_ID = ? AND T.TABLE_NAME = ?";
		return getDataAccess().queryForMap(sql,new Object[]{tableId,tableName});
	}
	
	/**
	 * 获取已发布的所有字段信息
	 * @param issueId 模型id
	 * @return
	 */
	public List<Map<String,Object>> getTableInstColName(String issueId){
		String sql = "SELECT T.COL_ALIAS,T.COL_NAME,T.COL_BUS_TYPE " +
				" FROM META_RPT_MODEL_ISSUE_COLS T " +
				" WHERE T.ISSUE_ID=?";
		return getDataAccess().queryForList(sql,issueId);
	}
	
	/**
	 * 获取实体表数据
	 * @param data  查询条件，
	 *  	{   issueId:模型id，localCode:地域
	 *  		tableName:表名，dataSourceId:数据源
	 *  	}
	 * @param page
	 * @return
	 */
	public List<Map<String,Object>> getTableInstData(Map<String,Object> data,Page page){
		String issueId = data.get("issueId").toString();			//模型id	
		String localCode = data.get("localCode").toString();		//地域
		String tableOwner = data.get("tableOwner").toString();		//表名
		String tableName = data.get("tableName").toString();		//表名
		String dataSourceId = data.get("dataSourceId").toString();	//数据源
		String dataDate = data.get("dataDate").toString();			//时间
		String dataPeriod = MapUtils.getString(data, "dataPeriod");	//审核模式
		int dataLevel = 0;
		if(dataPeriod.equals(isD)){	//天
			dataLevel = 3;
		}else if(dataPeriod.equals(isM)){	//月
			dataLevel = 2;
		}
		
		//得到需要展示的所有字段
		List<Map<String,Object>> colList = getTableInstColName(issueId);
		String fields = "";
		for(int i=0;i<colList.size();i++) {
			if(i==0) {
				fields += "T."+colList.get(i).get("COL_NAME");
			}else {
				fields += ",T."+colList.get(i).get("COL_NAME");
			}
		}
		
		//得到需要展示的维度字段
        List<Map<String,Object>> dimColList = getTableInstDimCol(issueId);
        //对维度字段进行遍历，添加查询条件
		String whereStr = "";	//查询条件
		String dateStr = "";	//日期查询条件
        for(int i=0;i<dimColList.size();i++){
        	Map<String, Object> map = dimColList.get(i);
        	String dimColName = map.get("COL_NAME").toString();
        	String dimTypeId = map.get("DIM_TYPE_ID").toString();
        	String searchColName = map.get("SEARCH_COL_NAME").toString();
        	String dimCodes = map.get("DIM_CODES")!=null?map.get("DIM_CODES").toString():"";
        	String dimLevels = map.get("DIM_LEVELS").toString();
        	if(!dimCodes.equals("")){	//如果存在过滤值，添加过滤
        		dimCodes = "'"+dimCodes.replaceAll(",", "','")+"'";
        		if(whereStr.equals("")){	//如果为一个条件，加where
        			whereStr += " WHERE T."+dimColName+" NOT IN ("+dimCodes+")";
        		}else{
            		whereStr += " AND T."+dimColName+" NOT IN ("+dimCodes+")";
        		}
        	}
        	if(map.get("DIM_TYPE")!=null&&!map.get("DIM_TYPE").toString().equals("")){
        		//如果dimType不为空，该维度字段为时间或者地域
            	if(map.get("DIM_TYPE").toString().equals(isZone)){	//地域维度
            		if(!localCode.equals(provinceFlag)){	//当不等于全省时，添加查询条件
                		if(dimLevels.indexOf('2')!=-1){
	            			String tmpStr = "SELECT T.ZONE_CODE FROM META.META_DIM_ZONE T" +
		        				" START WITH T."+searchColName+"="+localCode+
		        					" AND T.DIM_TYPE_ID="+dimTypeId+
		        				" CONNECT BY zone_par_id= PRIOR zone_id";
	                		if(whereStr.equals("")){	//如果为一个条件，加where
	                			whereStr += " WHERE T."+dimColName+" IN ("+tmpStr+")";
	                		}else{
	                			whereStr += " AND T."+dimColName+" IN ("+tmpStr+")";
	                		}
                		}
            		}
            	}else if(map.get("DIM_TYPE").toString().equals(isTime)){	//时间维度
            		if(dimLevels.indexOf(dataLevel+"")!=-1){
                		if(dateStr.equals("")){	//如果为一个条件，加where
                			dateStr += " T."+map.get("COL_NAME").toString()+" like '"+dataDate+"%'";
                		}else{
                			dateStr += " OR T."+map.get("COL_NAME").toString()+" like '"+dataDate+"%'";
                		}
            		}
            	}
        	}
        }
        if(!dateStr.equals("")){
        	//如果存在日期过滤条件，把日期查询条件添加到过滤信息中
            if(whereStr.equals("")){	
    			whereStr += " WHERE ("+dateStr+")";
    		}else{
    			whereStr += " AND ("+dateStr+")";
    		}
        }
        
		String sql = StringUtils.join(new String[]{
				"SELECT ",
				fields,
				" FROM ",
				tableOwner,
				".",
				tableName,
				" T ",
				whereStr
		});
		String countSql = StringUtils.join(new String[]{
				"SELECT COUNT(*) FROM ",
				tableOwner,
				".",
				tableName,
				" T ",
				whereStr
		});
        if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql,countSql,page);
        }
        System.out.println(sql);
        List<Map<String,Object>> rs = getDataAccess(dataSourceId).queryForList(sql);

        if(rs!=null&&rs.size()>0){	//当返回值不为空时，经行值转换
	        //把维度值进行转换
	        for (Map<String, Object> dimColMap : dimColList) {
	        	String dimColName = dimColMap.get("COL_NAME").toString();
	        	String dimTableId = dimColMap.get("DIM_TABLE_ID").toString();
	        	String dimTypeId = dimColMap.get("DIM_TYPE_ID").toString();
	        	String searchColName = dimColMap.get("SEARCH_COL_NAME").toString();
				for (Map<String, Object> rsMap : rs) {
					String codeName = getNameByCode(dimTableId,dimTypeId,
							rsMap.get(dimColName).toString(),searchColName);
					rsMap.put(dimColName, codeName);
				}
			}
	        //把标识值进行转换
	        List<Map<String,Object>> codesList = getCodesColList(issueId);
	        for (Map<String, Object> codeMap : codesList) {
	        	String colName = MapUtils.getString(codeMap, "COL_NAME");
	        	String dimCodes = MapUtils.getString(codeMap, "DIM_CODES");
	        	if(dimCodes!=null){
	        		String codes [] = dimCodes.split(",");
		        	Map<String, Object> map = new HashMap<String, Object>();
	        		for(int m=0; m<codes.length; m++){
		        		String vals [] = codes[m].split(";");
		        		for(int n=0; n<vals.length; n++){
		        			String val [] = vals[n].split(":");
		            		map.put(val[0], val[1]);
		        		}
		        	}
		        	for (Map<String, Object> rsMap : rs) {
		        		String codeName = MapUtils.getString(map, MapUtils.getString(rsMap, colName));
		        		if(codeName==null||codeName.equals("")){
		        			codeName = MapUtils.getString(rsMap, colName);
		        		}
		        		rsMap.put(colName, codeName);
					}
	        	}
	        }
        }
		return rs;
	}

	/**
	 * 通过维度表id，归并类型id,得到对应的维度名称
	 * @param dimTableId 维度表id
	 * @param dimTypeId 归并类型id
	 * @param value 维度对应值
	 * @param searchColName 维度对应值对应的列名
	 * @return 维度对应的名称
	 */
	public String getNameByCode(String dimTableId, String dimTypeId, String value, String searchColName){
		String keyName = searchColName+","+value+","+dimTypeId;		//缓存关联维度值信息
		String name = "";
		if(codeMapping.get(keyName)!=null&&!codeMapping.get(keyName).toString().equals("")){
			name = MapUtils.getString(codeMapping, keyName);
		}else{
			if(!dimTableId.equals("")&&!dimTypeId.equals("")&&!value.equals("")){
				String sql = "SELECT I.TABLE_OWNER, T.DATA_SOURCE_ID," +
						" T.TABLE_NAME, T.TABLE_DIM_PREFIX" +
						" FROM META_DIM_TABLES T" +
						" LEFT JOIN META_TABLE_INST I ON T.DIM_TABLE_ID = I.TABLE_ID" +
						" WHERE I.STATE = 1 AND T.DIM_TABLE_ID = ?";
		        Map<String,Object> dimTableMap = getDataAccess().queryForMap(sql,dimTableId);
		        if(dimTableMap!=null&&dimTableMap.size()>0){
		        	String dimTableName =  MapUtils.getString(dimTableMap, "TABLE_NAME");			//维度表名
		        	String dimTablePrefix = MapUtils.getString(dimTableMap, "TABLE_DIM_PREFIX");	//维度表前缀
		        	String tableOwner = MapUtils.getString(dimTableMap, "TABLE_OWNER");		//用户
		        	String dataSoruceId = MapUtils.getString(dimTableMap, "DATA_SOURCE_ID");	//数据源id
		        	
		        	sql = "SELECT T."+dimTablePrefix+"_NAME AS NAME FROM "+tableOwner+"."+dimTableName+" T " +
		        			" WHERE T."+searchColName+"=? AND T.DIM_TYPE_ID=?";
			        Map<String,Object> codeNameMap = getDataAccess(dataSoruceId).queryForMap(sql,value,dimTypeId);
			        if(codeNameMap!=null&&codeNameMap.size()>0){
			        	name = codeNameMap.get("NAME").toString();
			        	if(codeMapping.size()>mappingMaxCount){
			        		codeMapping.clear();
			        	}
			        	codeMapping.put(keyName, name);
			        }
		        }
			}
		}
		return name.equals("")?value:name;
	}
	
	/**
	 * 通过维度表id，归并类型id,得到对应维度code的名称
	 * @param dimTableId 维度表id
	 * @param dimTypeId 归并类型id
	 * @param code 维度值code
	 * @return 维度code对应的名称
	 */
	public String getNameByCode(String dimTableId, String dimTypeId, String code){
		String name = "";
		if(!dimTableId.equals("")&&!dimTypeId.equals("")&&!code.equals("")){
			String sql = "SELECT I.TABLE_OWNER, T.DATA_SOURCE_ID," +
					" T.TABLE_NAME, T.TABLE_DIM_PREFIX" +
					" FROM META_DIM_TABLES T" +
					" LEFT JOIN META_TABLE_INST I ON T.DIM_TABLE_ID = I.TABLE_ID" +
					" WHERE I.STATE = 1 AND T.DIM_TABLE_ID = ?";
	        Map<String,Object> dimTableMap = getDataAccess().queryForMap(sql,dimTableId);
	        if(dimTableMap!=null&&dimTableMap.size()>0){
	        	String dimTableName =  MapUtils.getString(dimTableMap, "TABLE_NAME");			//维度表名
	        	String dimTablePrefix = MapUtils.getString(dimTableMap, "TABLE_DIM_PREFIX");	//维度表前缀
	        	String tableOwner = MapUtils.getString(dimTableMap, "TABLE_OWNER");		//用户
	        	String dataSoruceId = MapUtils.getString(dimTableMap, "DATA_SOURCE_ID");	//数据源id
	        	
	        	sql = "SELECT T."+dimTablePrefix+"_NAME FROM "+tableOwner+"."+dimTableName+" T " +
	        			" WHERE T."+dimTablePrefix+"_CODE=? AND T.DIM_TYPE_ID=?";
		        Map<String,Object> codeNameMap = getDataAccess(dataSoruceId).queryForMap(sql,code,dimTypeId);
		        if(codeNameMap!=null&&codeNameMap.size()>0){
		        	name = codeNameMap.get(dimTablePrefix+"_NAME").toString();
		        }
	        }
		}
		return name;
	}
	
	/**
	 * 获取已发布的维度字段信息
	 * @param issueId 模型id
	 * @return
	 */
	public List<Map<String,Object>> getTableInstDimCol(String issueId){
		String sql = "SELECT C.COL_NAME,C.DIM_TABLE_ID,C.DIM_TYPE_ID," +
				" (SELECT N.COL_NAME FROM META_TABLE_COLS N " +
					" WHERE N.COL_STATE=1 AND N.COL_ID=C.DIM_COL_ID) AS SEARCH_COL_NAME," +
				" T.DIM_TYPE,T.DIM_CODES,T.DIM_LEVELS" +
				" FROM META_RPT_MODEL_ISSUE_COLS T" +
				" LEFT JOIN META_TABLE_COLS C ON T.COL_ID = C.COL_ID" +
				" WHERE C.COL_STATE=1 AND C.COL_BUS_TYPE=0 AND T.ISSUE_ID = ? ";
		return getDataAccess().queryForList(sql,issueId);
	}

	/**
	 * 得到已发布的标识字段信息
	 * @param issueId
	 * @return
	 */
	public List<Map<String,Object>> getCodesColList(String issueId){
		String sql = "SELECT T.COL_NAME,T.DIM_CODES" +
				" FROM META_RPT_MODEL_ISSUE_COLS T" +
				" WHERE t.COL_BUS_TYPE = 2 AND T.ISSUE_ID = ? ";
		return getDataAccess().queryForList(sql,issueId);
	}

	/**
	 * 新增审核记录
	 * @param data
	 * @return
	 */
	public boolean insertAudit(Map<String,Object> data){
		Map<String,Object> user = SessionManager.getCurrentUser();
		String auditCfgId = data.get("auditCfgId").toString();
		String dataDate = data.get("dataDate").toString();
		String auditConclude = data.get("auditConclude").toString();
		String showOpinion = data.get("showOpinion").toString();
		String auditOpinion = data.get("auditOpinion").toString();
		auditOpinion = auditOpinion.replaceAll("\r\n", "\r\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
			"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		String localCode = data.get("localCode").toString();
		String auditMark = "审&nbsp;核&nbsp;人："+user.get("userNamecn")+"\r\n" +
				"审核时间："+DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")+"\r\n" +
				"审核结果："+CodeManager.getName(ReportConstant.REPORT_IS_SHOW,auditConclude)+"\r\n" +
				"审核意见："+auditOpinion;
		
		String sql = "INSERT INTO META_RPT_DATA_AUDIT_LOG(AUDIT_LOG_ID,USER_ID,USER_DEPT_ID,USER_STATION_ID,USER_ZONE_ID," +
				"AUDIT_TIME,DATA_DATE,AUDIT_CONCLUDE,AUDIT_OPINION,SHOW_OPINION,AUDIT_CFG_ID,AUDIT_MARK,DATA_AREA) VALUES" +
				"(?,?,?,?,?,sysdate,?,?,?,?,?,?,?)";
		return getDataAccess().execNoQuerySql(sql, new Object[]{queryForNextVal("SEQ_RPT_DATA_AUDIT_LOG_ID"),
				user.get("userId"),user.get("deptId"),user.get("stationId"),user.get("zoneId"),
				dataDate,auditConclude,auditOpinion,showOpinion,auditCfgId,auditMark,localCode});
	}
	
	/**
	 * 修改审核记录
	 * @param data
	 * @return
	 */
	public boolean updateAudit(Map<String,Object> data){
		Map<String,Object> user = SessionManager.getCurrentUser();
    	String auditLogId = data.get("auditLogId").toString();
		String auditConclude = data.get("auditConclude").toString();
		String showOpinion = data.get("showOpinion").toString();
		String auditOpinion = data.get("auditOpinion").toString();
		auditOpinion = auditOpinion.replaceAll("\r\n", "\r\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
				"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		String auditMark = "\r\n----------------------------------------------\r\n" +
				"审&nbsp;核&nbsp;人："+user.get("userNamecn")+"\r\n" +
				"审核时间："+DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss")+"\r\n" +
				"审核结果："+CodeManager.getName(ReportConstant.REPORT_IS_SHOW,auditConclude)+"\r\n" +
				"审核意见："+auditOpinion;
		
		String sql = "UPDATE META_RPT_DATA_AUDIT_LOG T SET T.AUDIT_TIME = sysdate,T.USER_ID=?,T.USER_DEPT_ID=?," +
				" T.USER_STATION_ID=?,T.USER_ZONE_ID=?,T.AUDIT_CONCLUDE = ?," +
				" T.AUDIT_OPINION = ?,T.SHOW_OPINION = ?,T.AUDIT_MARK = T.AUDIT_MARK||?" +
				" WHERE T.AUDIT_LOG_ID=?";
		return getDataAccess().execNoQuerySql(sql, 
				new Object[]{user.get("userId"),user.get("deptId"),user.get("stationId"),user.get("zoneId"),
				auditConclude,auditOpinion,showOpinion,auditMark,auditLogId});
	}
	
	/**
	 * 判断该数据是否审核过,如果是审核过的，返回审核记录表id
	 * @param data 传入参数
	 * @return
	 */
	public Map<String,Object> isExistAudit(Map<String,Object> data){
		String sql = "SELECT T.AUDIT_LOG_ID FROM META_RPT_DATA_AUDIT_LOG T" +
				" WHERE T.AUDIT_CFG_ID=? AND T.DATA_DATE=? AND T.DATA_AREA = ?";
		return getDataAccess().queryForMap(sql, new Object[]{data.get("auditCfgId"),
				data.get("dataDate"),data.get("localCode")});
	}
	
	/**
	 * 根据审核模式查询本地网分公司数据
	 * @return
	 */
	public List<Map<String,Object>> getZoneList(int auditProp){
		List<Object> params = new ArrayList<Object>();
		String sql = "SELECT T.ZONE_CODE,T.ZONE_NAME FROM META_DIM_ZONE T " +
				" WHERE T.DIM_LEVEL=2 AND DIM_TYPE_ID=4";
		if(auditProp == 2){
			Map<String,Object> user = SessionManager.getCurrentUser();
			sql += " AND T.ZONE_ID=?";
			params.add(user.get("zoneId"));
		}
		return getDataAccess().queryForList(sql,params.toArray());
	}
	
	/**
	 * 判断code是否存在于地域表中
	 * @param code 地域code
	 * @return
	 */
	public boolean isExistByZoneCode(String code){
		String sql = "SELECT T.ZONE_ID FROM META_DIM_ZONE T WHERE " +
				" T.DIM_LEVEL=2 AND T.ZONE_CODE=?";
		Map<String,Object> map = getDataAccess().queryForMap(sql,code);
		return map!=null&&map.size()>0;
	}

}
