package tydic.meta.module.report.issue.Table;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.report.ReportConstant;
import tydic.meta.module.tbl.TblConstant;
import tydic.meta.sys.code.CodeManager;
import tydic.meta.web.session.SessionManager;

/**
 * 数据模型模块
 * @author 谭万昌
 * @date 2012-2-21
 * 
 * @modifier 李国民
 * @modifierDate：2012-04-05
 */
public class IssueDataModelDAO extends MetaBaseDAO{

	private String noTyepId = "'2','3'";		//维度表、维度接口表类型id
	/**
	 * 发布模型时，查询选择表类
	 * @modifier 李国民
	 * 
	 * @param data 查询条件，其数据格式
	 * 		 {keyword：模糊查询表名，dataSourceId：数据源id
	 * 		  tableOwner：所属用户，tableTypeId：层次分类
	 * 		  tableGroupId：业务类型
	 * 		 }
	 * @param page 分页
	 * @return 表类信息
	 */
	public List<Map<String, Object>> queryTableType(Map<String, Object> data,Page page) {

    	List<Object> param = new ArrayList<Object>();
    	StringBuffer bufferSql = new StringBuffer("SELECT T.TABLE_ID,T.TABLE_OWNER,T.TABLE_NAME,T.TABLE_TYPE_ID," +
    			" B.TABLE_GROUP_NAME,C.DATA_SOURCE_NAME,T.TABLE_BUS_COMMENT" +
    			" FROM META_TABLES T " +
    			" LEFT JOIN(SELECT TABLE_GROUP_ID,TABLE_TYPE_ID,TABLE_GROUP_NAME FROM META_TABLE_GROUP)" +
    				" B ON T.TABLE_GROUP_ID = B.TABLE_GROUP_ID AND T.TABLE_TYPE_ID=B.TABLE_TYPE_ID " +
    			" LEFT JOIN(SELECT DATA_SOURCE_NAME,DATA_SOURCE_ID FROM META_DATA_SOURCE) " +
    				" C ON T.DATA_SOURCE_ID=C.DATA_SOURCE_ID " +
    			" WHERE T.TABLE_STATE=1 AND T.TABLE_TYPE_ID NOT IN("+noTyepId+")" +
    			" AND T.TABLE_ID NOT IN (SELECT DIM_TABLE_ID FROM META_DIM_TABLES)");
    	if(data.get("keyword") != null && !data.get("keyword").equals("")) {
    		bufferSql.append(" AND T.TABLE_NAME LIKE ? ESCAPE '/' ");
    		param.add("%"+data.get("keyword").toString().toUpperCase()+"%");
    	}
    	if(data.get("dataSourceId") != null && !data.get("dataSourceId").equals("")) {
    		bufferSql.append(" AND C.DATA_SOURCE_ID=? ");
    		param.add(data.get("dataSourceId"));
    	}
    	if(data.get("tableOwner") != null && !data.get("tableOwner").equals("") && !data.get("tableOwner").equals("-1")) {
    		bufferSql.append(" AND LOWER(T.TABLE_OWNER)=? ");
    		param.add(data.get("tableOwner").toString().toLowerCase());
    	}
    	if(data.get("tableTypeId") != null && !data.get("tableTypeId").equals("")) {
    		bufferSql.append(" AND T.TABLE_TYPE_ID=? ");
    		param.add(data.get("tableTypeId"));
    	}
    	if(data.get("tableGroupId") != null && !data.get("tableGroupId").equals("")) {
    		bufferSql.append(" AND T.TABLE_GROUP_ID=? ");
    		param.add(data.get("tableGroupId"));
    	}
    	bufferSql.append(" ORDER BY T.TABLE_ID DESC");

    	String sql = bufferSql.toString();
		if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }

        List<Map<String,Object>> rs = getDataAccess().queryForList(sql,param.toArray());
        //加入码表信息
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("TABLE_TYPE_NAME", CodeManager.getName(TblConstant.META_SYS_CODE_TABLE_TYPE,MapUtils.getString(map,"TABLE_TYPE_ID")));
            }
        }
		return rs;
	}
	
	/**
	 * 对传入的数模模型字段信息进行处理，把对应维度的层级关联上
	 * @author 李国民
	 * 
	 * @param list 模型字段信息
	 * @return 关联维度层级的信息
	 */
	private List<Map<String, Object>> processingList(List<Map<String, Object>> list){
		for(int i=0;i<list.size();i++) {
			if(list.get(i).get("DIM_TABLE_ID") == null){
				continue;
			} 
			String dimTableId = list.get(i).get("DIM_TABLE_ID").toString();
			String dimTypeId = list.get(i).get("DIM_TYPE_ID").toString();
			String oldLevel = list.get(i).get("OLD_LEVEL").toString();
			String sql1 = "SELECT T.DIM_LEVEL, T.DIM_LEVEL_NAME FROM META_DIM_LEVEL T" +
					" WHERE T.DIM_TABLE_ID = ? AND T.DIM_TYPE_ID = ? AND T.DIM_LEVEL <= ?" +
					" ORDER BY T.DIM_LEVEL";
			List<Map<String, Object>> dimList = getDataAccess().queryForList(sql1,dimTableId,dimTypeId,oldLevel);
			String dimLevels = "";
			String dimLevelIds = "";
			for(int j=0;j<dimList.size();j++) {
				String dimLevel = dimList.get(j).get("DIM_LEVEL").toString();
				String dimLevelName = dimList.get(j).get("DIM_LEVEL_NAME").toString();
				if(j == dimList.size()-1) {
					dimLevels += dimLevelName;
					dimLevelIds += dimLevel;
				}else {
					dimLevels += dimLevelName+",";
					dimLevelIds += dimLevel+",";
				}
				
			}
			list.get(i).put("DIM_LEVELS", dimLevels);
			list.get(i).put("DIM_LEVEL_IDS", dimLevelIds);

			//查询code对应name
			if(list.get(i).get("DIM_CODES")!=null&&!list.get(i).get("DIM_CODES").toString().equals("")){
				String dimCodes = list.get(i).get("DIM_CODES").toString();
				String dimNames = queryNamesByCodes(dimTableId,dimTypeId,dimCodes);
				list.get(i).put("DIM_NAMES", dimNames);
			}
		}
		return list;
	}
	
	/**
	 * 查询表类字段信息
	 * @modifier 李国民
	 * 
	 * @param tableId	表类id
	 * @return	list
	 */
	public List<Map<String, Object>> queryTableTypeColInfo(String tableId) {
		String sql = "SELECT T.COL_ID,T.COL_NAME,T.COL_NAME_CN AS COL_ALIAS," +
				" T.COL_BUS_TYPE,T.DIM_TABLE_ID,T.DIM_TYPE_ID,T.TABLE_ID," +
				" T.DIM_LEVEL AS OLD_LEVEL" +
				" FROM META_TABLE_COLS T" +
				" LEFT JOIN META_TABLES B ON T.TABLE_ID = B.TABLE_ID " +
						" AND T.TABLE_VERSION = B.TABLE_VERSION" +
				" WHERE B.TABLE_STATE = 1 AND T.TABLE_ID = ?" +
				" ORDER BY T.COL_ORDER ASC";
		List<Map<String, Object>> list = getDataAccess().queryForList(sql,tableId);
		return processingList(list);
	}
	
	/**
	 * 查询已发布数据模型的字段信息
	 * @modifier 李国民
	 * 
	 * @param issueId 模型id
	 * @return 已经发布的模型字段信息
	 */
	public List<Map<String, Object>> queryPublishColInfo(String issueId) {
    	String sql = "SELECT T.COLUMN_ID,T.COL_ID,T.COL_NAME,T.COL_ALIAS,T.COL_BUS_TYPE," +
    			" T.DIM_TABLE_ID,T.DIM_TYPE_ID,T.DIM_LEVELS AS ALL_DIM_LEVELS,T.DIM_CODES," +
    			" T.COL_TYPE_ID,B.COL_TYPE_NAME,T.AMOUNT_FLAG," +
    			" C.DIM_LEVEL AS OLD_LEVEL" +
    			" FROM META_RPT_MODEL_ISSUE_COLS T" +
    			" LEFT JOIN (SELECT * FROM META_TABLE_COLS M WHERE M.COL_STATE=1) C ON T.COL_ID = C.COL_ID" +
    			" LEFT JOIN META_RPT_MODEL_COL_BUS_TYPE B ON B.COL_TYPE_ID = T.COL_TYPE_ID" +
    			" WHERE T.ISSUE_ID = ?" +
    			" ORDER BY C.COL_ORDER ASC";
		List<Map<String, Object>> list =  getDataAccess().queryForList(sql,issueId);
		return processingList(list);
	}
	
	/**
	 * 查询修改发布数据模型的字段信息
	 * @modifier 李国民
	 * 
	 * @param tableId	表类id
	 * @param issueId	模型id
	 * @return	list
	 */
	public List<Map<String, Object>> queryModifyPublishColInfo(String tableId, String issueId) {
		String sql = "SELECT C.COLUMN_ID,T.COL_ID,T.COL_NAME," +
				" DECODE(C.COL_ALIAS, '', T.COL_NAME_CN, C.COL_ALIAS) AS COL_ALIAS," +
				" DECODE(C.COL_BUS_TYPE, '', T.COL_BUS_TYPE, C.COL_BUS_TYPE) AS COL_BUS_TYPE," +
				" T.DIM_TABLE_ID,T.DIM_TYPE_ID,C.DIM_LEVELS AS ALL_DIM_LEVELS," +
				" C.DIM_CODES,C.COL_TYPE_ID,B.COL_TYPE_NAME,C.AMOUNT_FLAG," +
				" T.DIM_LEVEL AS OLD_LEVEL," +
		        "(SELECT COUNT(*) FROM META_RPT_TAB_QUERY_CFG H WHERE H.COLUMN_ID = C.COLUMN_ID " +
		        	" AND H.REPORT_ID NOT IN (SELECT F.REPORT_ID FROM META_RPT_TAB_REPORT_CFG " +
		        	" F WHERE F.ISSUE_ID=? AND F.REPORT_STATE=0))+ "+
		        "(SELECT COUNT(*) FROM META_RPT_TAB_FILTER_CFG H WHERE H.COLUMN_ID = C.COLUMN_ID " +
		        	" AND H.REPORT_ID NOT IN (SELECT F.REPORT_ID FROM META_RPT_TAB_REPORT_CFG " +
		        	" F WHERE F.ISSUE_ID=? AND F.REPORT_STATE=0))+ "+
		        "(SELECT COUNT(*) FROM META_RPT_TAB_OUTPUT_CFG H WHERE H.COLUMN_ID = C.COLUMN_ID " +
		        	" AND H.REPORT_ID NOT IN (SELECT F.REPORT_ID FROM META_RPT_TAB_REPORT_CFG " +
		        	" F WHERE F.ISSUE_ID=? AND F.REPORT_STATE=0)) "+
		        " AS IS_USED "+
				" FROM META_TABLE_COLS T" +
				" LEFT JOIN (SELECT * FROM META_RPT_MODEL_ISSUE_COLS M WHERE M.ISSUE_ID = ?) " +
					" C ON T.COL_ID = C.COL_ID" +
				" LEFT JOIN META_RPT_MODEL_COL_BUS_TYPE B ON B.COL_TYPE_ID = C.COL_TYPE_ID" +
				" WHERE T.COL_STATE = 1 AND T.TABLE_ID = ?" +
				" ORDER BY T.COL_ORDER ASC"; 
		List<Map<String, Object>> list =  getDataAccess().queryForList(sql,issueId,issueId,issueId,issueId,tableId);
		return processingList(list);
	}
	
	/**
	 * 通过维度表codes得到对应的names
	 * @modifier 李国民
	 * 
	 * @param dimTableId 维度表id
	 * @param dimTypeId 归并类型id
	 * @param dimCodes 维度code（格式：值1,值2,值3...)
	 * @return
	 */
	public String queryNamesByCodes(String dimTableId, String dimTypeId, String dimCodes){
		dimCodes = "'"+dimCodes.replaceAll(",", "','")+"'";
		String dimSql = "SELECT I.TABLE_OWNER, T.DATA_SOURCE_ID," +
				" T.TABLE_NAME, T.TABLE_DIM_PREFIX" +
				" FROM META_DIM_TABLES T" +
				" LEFT JOIN META_TABLE_INST I ON T.DIM_TABLE_ID = I.TABLE_ID" +
				" WHERE I.STATE = 1 AND T.DIM_TABLE_ID = ?";
		Map<String, Object> dimMap = getDataAccess().queryForMap(dimSql,dimTableId);
    	String tableName =  MapUtils.getString(dimMap, "TABLE_NAME");
    	String talbeDimPrefix = MapUtils.getString(dimMap, "TABLE_DIM_PREFIX");
    	String tableOwner = MapUtils.getString(dimMap, "TABLE_OWNER");
    	String dataSoruceId = MapUtils.getString(dimMap, "DATA_SOURCE_ID");
		String codeNameSql = "SELECT T."+talbeDimPrefix+"_NAME AS SERVICE_NAME"+
						" FROM  "+tableOwner+"."+tableName+" T "+
						" WHERE T.STATE = 1 AND T.DIM_TYPE_ID=? " +
						" AND T."+talbeDimPrefix+"_CODE in ("+dimCodes+")";
		List<Map<String, Object>> codeNameList = getDataAccess(dataSoruceId).queryForList(codeNameSql, dimTypeId);
		String names = "";
		for(int j=0;j<codeNameList.size();j++){
			Map<String, Object> map = codeNameList.get(j);
			if(j==0){
				names += map.get("SERVICE_NAME").toString();
			}else{
				names += ","+map.get("SERVICE_NAME").toString();
			}
		}
		return names;
	}
	
	/**
	 * 得到维度信息
	 * @modifier 李国民
	 * 
     * @param dimTableId 维度表id
     * @param dimTypeId 维度归并类型id
     * @param dimLevel 维度层级
	 * @return
	 */
	public List<Map<String, Object>> queryDimCodes(String dimTableId,String dimTypeId,String dimLevel) {
		String sql = "SELECT I.TABLE_OWNER, T.DATA_SOURCE_ID," +
				" T.TABLE_NAME, T.TABLE_DIM_PREFIX" +
				" FROM META_DIM_TABLES T" +
				" LEFT JOIN META_TABLE_INST I ON T.DIM_TABLE_ID = I.TABLE_ID" +
				" WHERE I.STATE = 1 AND T.DIM_TABLE_ID = ?";
    	Map<String, Object> dimMap = getDataAccess().queryForMap(sql,dimTableId);
    	String tableName =  MapUtils.getString(dimMap, "TABLE_NAME");
    	String talbeDimPrefix = MapUtils.getString(dimMap, "TABLE_DIM_PREFIX");
    	String tableOwner = MapUtils.getString(dimMap, "TABLE_OWNER");
    	String dataSoruceId = MapUtils.getString(dimMap, "DATA_SOURCE_ID");
    	String levels [] = dimLevel.split(",");
    	String startLevel = levels[0];
    	String maxLevel = levels[levels.length-1];
		String dimSql = "SELECT T."+talbeDimPrefix+"_ID AS ID, '0' AS PAR_ID," +
						" T."+talbeDimPrefix+"_NAME AS NAME,T."+talbeDimPrefix+"_CODE AS CODE," +
						" CASE WHEN T.DIM_LEVEL=? THEN 0 ELSE DECODE(NVL(B.CNT,0),0,0,1) END CHILDREN"+
						" FROM  "+tableOwner+"."+tableName+" T " +
						" LEFT JOIN (SELECT Z."+talbeDimPrefix+"_PAR_ID,COUNT(*) CNT " +
							" FROM "+tableOwner+"."+tableName+" Z WHERE Z.DIM_TYPE_ID=? AND Z.STATE = 1" +
							" GROUP BY "+talbeDimPrefix+"_PAR_ID) B " +
							" ON T."+talbeDimPrefix+"_ID=B."+talbeDimPrefix+"_PAR_ID "+
						" WHERE T.STATE = 1 AND T.DIM_TYPE_ID=? AND T.DIM_LEVEL=?" +
						" ORDER BY T.ORDER_ID";
		List<Map<String, Object>> rs = getDataAccess(dataSoruceId).queryForList(dimSql,maxLevel,dimTypeId,dimTypeId,startLevel);
		return rs!=null&&rs.size()>0?rs:null;
	}
	
	/**
	 * 发布数据模型(主表--模型信息表)
	 * @modifier 李国民
	 * 
	 * @param data 模型数据，其数据格式如下：
	 * 		 { tableId：表类id，tableAlias:模型别名，auditType：审核模式
	 * 		   tableKeyword：模型关键字，dataRemark：模型说明
	 * 		   effectTime：生效时间，isListing：模型是否为清单表
	 * 		   subscribeType：发送方式
	 * 		 }
	 * @return 返回发布后的数据模型id
	 */
	public int insetIssueConfig(Map<?,?> data) throws Exception {
		//数据模型id
		int issueId = (int) queryForNextVal("SEQ_RPT_MODEL_ISSUE_CONFIG_ID");
		String sql="INSERT INTO META_RPT_MODEL_ISSUE_CONFIG(ISSUE_ID,TABLE_ID,TABLE_ALIAS,AUDIT_TYPE," +
				" TABLE_KEYWORD,ISSUE_STATE,ISSUE_NOTE,START_TIME,IS_LISTING,SUBSCRIBE_TYPE) "+
			         "VALUES(?,?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?)";
        getDataAccess().execUpdate(sql, new Object[]{issueId,data.get("tableId"),data.get("tableAlias"),
        		data.get("auditType"),data.get("tableKeyword"),1,data.get("dataRemark"),
        		data.get("effectTime"),data.get("isListing"),data.get("subscribeType")});
        
        return issueId;
	}
	
	/**
	 * 发布数据模型(从表--数据审核范围表)
	 * @modifier 李国民
	 * 
	 * @param data 模型数据，其数据格式如下：
	 * 		 { dataCycle：数据审核模式（按月还是按天）
	 * 		   auditProp：审核人地域
	 * 		   appRule：查询数据最大日期(应用约定)
	 * 		 }
	 * @param issueId 模型id
	 */
	public void insertDateAuditCfg(Map<?,?> data,int issueId) throws Exception {
		String sql= "INSERT INTO META_RPT_DATA_AUDIT_CFG" +
				"(AUDIT_CFG_ID,AUDIT_TYPE,ISSUE_ID,AUDIT_PROP,MIN_DATE,MAX_DATE,EFFECT_STATE)" +
				" VALUES(?,?,?,?,?,?,?)";
        getDataAccess().execUpdate(sql, new Object[]{queryForNextVal("SEQ_RPT_DATA_AUDIT_CFG_ID"),
        		data.get("dataCycle"),issueId,data.get("auditProp"),data.get("effectTime"),
        		data.get("appRule"),1});
	}

	/**
	 * 发布数据模型字段
	 * @modifier 李国民
	 * 	
	 * @param datas	字段列表
	 * 		{ colId：表类字段id，colAlias模型字段别名
	 * 		  colBusType：字段类型，dimLevels：维度层次
	 * 		  dimCodes：维度值，selectedFlag：是否选中
	 * 		  colTypeId：字段分类id，amountFlag：是否合计
	 * 		  dimType：维度类型，
	 * 		  dimTableId：维度表表类id，dimTypeId：归并类型id
	 * 		  colName：表字段列名
	 * 		}
	 * @param issueId	模型id
	 * @return 
	 */
	public int[] insertPublishDataModelColInfo(final List<Map<String,Object>> datas,final int issueId) {
		String sql="INSERT INTO META_RPT_MODEL_ISSUE_COLS(COLUMN_ID,COL_ID,COL_ALIAS,COL_BUS_TYPE," +
				" DIM_LEVELS,DIM_CODES,SELECTED_FLAG,COL_TYPE_ID,AMOUNT_FLAG,ISSUE_ID,DIM_TYPE," +
				" DIM_TABLE_ID,DIM_TYPE_ID,COL_NAME) "+
		        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                Map<String,Object> data=datas.get(i);
                preparedStatement.setObject(1,queryForNextVal("SEQ_RPT_MODEL_ISSUE_COL_ID")); 	//自动生成id
                preparedStatement.setObject(2,data.get("colId")); 				//表类字段id
                preparedStatement.setObject(3,data.get("colAlias")); 			//模型发布字段别名
                preparedStatement.setObject(4,data.get("colBusType"));			//字段类型（0维度，1指标，2标识）
                preparedStatement.setObject(5,data.get("dimLevels"));   		//维度层次
                preparedStatement.setObject(6,data.get("dimCodes")); 			//维度值
                preparedStatement.setObject(7,data.get("selectedFlag"));		//是否选中
                preparedStatement.setObject(8,data.get("colTypeId")); 			//字段分类id
                preparedStatement.setObject(9,data.get("amountFlag")); 			//是否合计
                preparedStatement.setObject(10,issueId); 						//模型id
                preparedStatement.setObject(11,data.get("dimType")); 			//维度类型(1日期，2地域，0其他)
                preparedStatement.setObject(12,data.get("dimTableId")); 		//维度表表类id
                preparedStatement.setObject(13,data.get("dimTypeId")); 			//归并类型id
                preparedStatement.setObject(14,data.get("colName")); 			//表字段列名
            }
            public int batchSize(){
                return datas.size();
            }
        });
	}
	  
	/**
	 * 发布数据模型日志记录
	 * @modifier 李国民
	 * 
	 * @param map
	 * @param issueId	模型id
	 * @param issueOperate 操作类型（11新增、12修改、21下线）
	 */
	public void insertPublishDataModelLog(Map<?, ?> data,int issueId,int issueOperate) throws Exception {
		Map<String,Object> user = SessionManager.getCurrentUser();
		String sql="INSERT INTO META_RPT_MODEL_ISSUE_LOG(LOG_ID,USER_ID,ISSUE_ID,ISSUE_TIME,USER_ZONE_ID," +
					" USER_DEPT_ID,USER_STATION_ID,ISSUE_OPERATE) "+
			        " VALUES(?,?,?,sysdate,?,?,?,?)";
        getDataAccess().execUpdate(sql, new Object[]{queryForNextVal("SEQ_RPT_MODEL_ISSUE_LOG_ID"),
        		user.get("userId"),issueId,user.get("zoneId"),user.get("deptId"),
        		user.get("stationId"),issueOperate});
	}
	
	/**
	 * 查询已发布的模型信息
	 * @param data 查询条件
	 * @param page 分页
	 * @return
	 */
    public List<Map<String,Object>> queryDataModel(Map<?,?> data,Page page){
    	List<Object> param = new ArrayList<Object>();
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(" SELECT A.*,B.MAX_DATE,B.AUDIT_PROP,B.AUDIT_TYPE AS DATA_CYCLE FROM (SELECT A.*,B.DATA_SOURCE_NAME,");
    	buffer.append(" B.DATA_SOURCE_USER FROM (");
    	buffer.append(" SELECT A.DATA_SOURCE_ID,A.TABLE_ID,A.TABLE_NAME,A.TABLE_NAME_CN,A.TABLE_OWNER,");
    	buffer.append(" A.TABLE_TYPE_ID,A.TABLE_GROUP_ID,B.TABLE_ALIAS,B.AUDIT_TYPE,B.ISSUE_STATE,");
    	buffer.append(" B.ISSUE_ID,B.ISSUE_NOTE,B.IS_LISTING,B.TABLE_KEYWORD,B.SUBSCRIBE_TYPE,");
    	buffer.append(" to_char(B.START_TIME,'yyyy-mm-dd hh24:mi:ss') AS START_TIME");
    	buffer.append(" FROM META_TABLES A,META_RPT_MODEL_ISSUE_CONFIG B");
    	buffer.append(" WHERE A.TABLE_ID=B.TABLE_ID");
    	if(data.get("keyword") != null && !"".equals(""+data.get("keyword")) && !"null".equals(""+data.get("keyword"))){
    		buffer.append(" AND (A.TABLE_NAME LIKE ? ESCAPE '/' OR A.TABLE_NAME_CN LIKE ? ESCAPE '/' OR B.TABLE_ALIAS LIKE ? ESCAPE '/' OR B.ISSUE_NOTE LIKE ?)");
    		String keyWord = (data.get("keyword")+"").toUpperCase();
    		param.add(SqlUtils.allLikeBindParam(keyWord));
    		param.add(SqlUtils.allLikeBindParam(keyWord));
    		param.add(SqlUtils.allLikeBindParam(keyWord));
    		param.add(SqlUtils.allLikeBindParam(keyWord));
    	}
    	buffer.append(" AND A.TABLE_STATE=1");
    	if(data.get("dataSourceId") != null && !data.get("dataSourceId").equals("")&& !data.get("dataSourceId").equals("-1")) {
    		buffer.append(" AND A.DATA_SOURCE_ID=?");
    		param.add(data.get("dataSourceId").toString());
    	}
    	if(data.get("levelTypeId") != null &&  !data.get("levelTypeId").equals("")) {
    		buffer.append(" AND A.TABLE_TYPE_ID=?");
    		param.add(data.get("levelTypeId").toString());
    	}
    	if(data.get("businessTypeId") != null && !data.get("businessTypeId").equals("")) {
    		buffer.append(" AND A.TABLE_GROUP_ID=?");
    		param.add(data.get("businessTypeId").toString());
    	}
    	buffer.append(" ) A,META_DATA_SOURCE B");
    	buffer.append(" WHERE A.DATA_SOURCE_ID=B.DATA_SOURCE_ID ");
    	if(data.get("userName") != null &&  !data.get("userName").equals("")&& !data.get("userName").equals("-1")) {
    		buffer.append(" AND LOWER(A.TABLE_OWNER)=?");
    		param.add((data.get("userName").toString().toLowerCase()));
    	}
    	buffer.append(" ) A,META_RPT_DATA_AUDIT_CFG B");
    	buffer.append(" WHERE A.ISSUE_ID=B.ISSUE_ID AND A.ISSUE_STATE=1");
    	buffer.append(" ORDER BY A.ISSUE_ID DESC");
    	String sql = buffer.toString();
        if(page != null){
            sql = SqlUtils.wrapPagingSql(sql.toString(), page);
        }
        List<Map<String,Object>> list = getDataAccess().queryForList(sql,param.toArray());
        if(list != null && list.size() > 0){
        	for(Map<String,Object> m : list){
        		m.put("SHOW_DATA_CYCLE", CodeManager.getName(ReportConstant.REPORT_DATA_CYCLE,MapUtils.getString(m,"DATA_CYCLE")));
        	}
        }
    	return list;
    }
    
    
	/**
	 * 查询已发布的模型信息
	 * @param data 查询条件
	 * @param page 分页
	 * @return
	 */
    public List<Map<String,Object>> queryDataModelByIssueId(String issueId){
    	List<Object> param = new ArrayList<Object>();
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(" SELECT A.*,B.MAX_DATE,B.AUDIT_PROP,B.AUDIT_TYPE AS DATA_CYCLE FROM (SELECT A.*,B.DATA_SOURCE_NAME,");
    	buffer.append(" B.DATA_SOURCE_USER FROM (");
    	buffer.append(" SELECT A.DATA_SOURCE_ID,A.TABLE_ID,A.TABLE_NAME,A.TABLE_NAME_CN,A.TABLE_OWNER,");
    	buffer.append(" A.TABLE_TYPE_ID,A.TABLE_GROUP_ID,B.TABLE_ALIAS,B.AUDIT_TYPE,B.ISSUE_STATE,");
    	buffer.append(" B.ISSUE_ID,B.ISSUE_NOTE,B.IS_LISTING,B.TABLE_KEYWORD,");
    	buffer.append(" to_char(B.START_TIME,'yyyy-mm-dd hh24:mi:ss') AS START_TIME");
    	buffer.append(" FROM META_TABLES A,META_RPT_MODEL_ISSUE_CONFIG B");
    	buffer.append(" WHERE A.TABLE_ID=B.TABLE_ID");

    	buffer.append(" AND A.TABLE_STATE=1");

    	buffer.append(" ) A,META_DATA_SOURCE B");
    	buffer.append(" WHERE A.DATA_SOURCE_ID=B.DATA_SOURCE_ID ");
    	buffer.append(" ) A,META_RPT_DATA_AUDIT_CFG B");
    	buffer.append(" WHERE A.ISSUE_ID=B.ISSUE_ID AND A.ISSUE_STATE=1 AND A.ISSUE_ID=?");
    	param.add(issueId.toString());
    	buffer.append(" ORDER BY A.ISSUE_ID DESC");
    	String sql = buffer.toString();
    	
        List<Map<String,Object>> list = getDataAccess().queryForList(sql,param.toArray());
        if(list != null && list.size() > 0){
        	for(Map<String,Object> m : list){
        		m.put("SHOW_DATA_CYCLE", CodeManager.getName(ReportConstant.REPORT_DATA_CYCLE,MapUtils.getString(m,"DATA_CYCLE")));
        	}
        }
    	return list;
    }
    
	/**
	 * 修改数据模型(主表--模型信息表)
	 * @modifier 李国民
	 * 
	 * @param data 模型数据，其数据格式如下：
	 * 		 { tableAlias:模型别名，auditType：审核模式
	 * 		   tableKeyword：模型关键字，dataRemark：模型说明
	 * 		   effectTime：生效时间，isListing：模型是否为清单表
	 * 		   issueId:  模型id,subscribeType：发送方式
	 * 		 }
	 */  
	public void updateIssueConfig(Map<String, Object> data) throws Exception {
		String sql = "UPDATE META_RPT_MODEL_ISSUE_CONFIG SET " +
				" TABLE_ALIAS=?,AUDIT_TYPE=?,TABLE_KEYWORD=?," +
				" ISSUE_NOTE=?,IS_LISTING=?,SUBSCRIBE_TYPE=?" +
				" WHERE ISSUE_ID=?";
		getDataAccess().execUpdate(sql, new Object[]{data.get("tableAlias"),
				data.get("auditType"),data.get("tableKeyword"),data.get("dataRemark"),
				data.get("isListing"),data.get("subscribeType"),data.get("issueId")});
	}
	
	/**
	 * 修改数据模型(从表--数据审核范围表)
	 * @modifier 李国民
	 * 
	 * @param data 模型数据，其数据格式如下：
	 * 		 { dataCycle：数据审核模式（按月还是按天）
	 * 		   auditProp：审核人地域
	 * 		   appRule：查询数据最大日期(应用约定)
	 * 		   issueId 模型id
	 * 		 }
	 */
	public void updateDateAuditCfg(Map<?,?> data) throws Exception {
		String sql1= "UPDATE META_RPT_DATA_AUDIT_CFG " +
		"SET MAX_DATE=?,AUDIT_TYPE=?,AUDIT_PROP=? " +
		" WHERE ISSUE_ID=?";
		getDataAccess().execUpdate(sql1, new Object[]{data.get("appRule"),
        		data.get("dataCycle"),data.get("auditProp"),data.get("issueId")});
	}

	/**
	 * 批量修改模型字段信息
	 * @modifier 李国民
	 * 
	 * @param datas	字段列表
	 * 		{ colId：表类字段id，colAlias模型字段别名
	 * 		  colBusType：字段类型，dimLevels：维度层次
	 * 		  dimCodes：维度值，selectedFlag：是否选中
	 * 		  colTypeId：字段分类id，amountFlag：是否合计
	 * 		  dimType：维度类型，
	 * 		  dimTableId：维度表表类id，dimTypeId：归并类型id
	 * 		  colName：表字段列名
	 * 		}
	 * @return
	 */
	public int[] updatePublishDataModelColInfo(final List<Map<String,Object>> datas) {
		String sql = "UPDATE META_RPT_MODEL_ISSUE_COLS SET COL_ALIAS=?," +
				" DIM_LEVELS=?,DIM_CODES=?,COL_TYPE_ID=?,AMOUNT_FLAG=?," +
				" COL_BUS_TYPE=? WHERE COLUMN_ID=?";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                Map<String,Object> data=datas.get(i);
                preparedStatement.setObject(1,data.get("colAlias")); 			//模型发布字段别名
                preparedStatement.setObject(2,data.get("dimLevels"));   		//维度层次
                preparedStatement.setObject(3,data.get("dimCodes")); 			//维度值
                preparedStatement.setObject(4,data.get("colTypeId")); 			//字段分类id
                preparedStatement.setObject(5,data.get("amountFlag")); 			//是否合计
                preparedStatement.setObject(6,data.get("colBusType")); 			//字段类型
                preparedStatement.setObject(7,data.get("columnId")); 			//已发布模型列id
            }
            public int batchSize(){
                return datas.size();
            }
        });
	}
	public int[] delPublishDataModelColInfo(final List<Map<String,Object>> datas) {
		String sql = "DELETE FROM META_RPT_MODEL_ISSUE_COLS WHERE COLUMN_ID = ?";
		return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
			public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
				Map<String,Object> data=datas.get(i);
				preparedStatement.setObject(1,data.get("columnId")); 			//模型发布字段别名
			}
			public int batchSize(){
				return datas.size();
			}
		});
	}
	
	/**
	 * @Title: validateName 
	 * @Description: 验证名称重复
	 * @param tableAlias
	 * @return boolean true:重复 false:不重复   
	 * @throws
	 */
	public boolean validateName(String tableAlias){
		boolean res = false;
		String sql = "SELECT COUNT(1) COUNTS FROM META_RPT_MODEL_ISSUE_CONFIG T WHERE T.TABLE_ALIAS=?";
		Map<String,Object> rs = getDataAccess().queryForMap(sql,new Object[]{tableAlias});
		if(rs != null && rs.size() > 0){
			if(Integer.valueOf(""+rs.get("COUNTS")) > 0)
				res = true;
		}
		return res;
	}
}