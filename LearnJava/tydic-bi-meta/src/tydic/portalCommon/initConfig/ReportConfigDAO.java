
/**   
 * @文件名: ReportConfigDAO.java
 * @包 tydic.portalCommon.initConfig
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-5-15 上午11:25:18
 *  
 */
  
package tydic.portalCommon.initConfig;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.StringUtils;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.DateUtil;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.SqlUtils;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：ReportConfigDAO   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-5-15 上午11:25:18   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class ReportConfigDAO extends MetaBaseDAO
{
    
	/**
	 * @Title: getReportTabMes 
	 * @Description: 获取报表下的指标信息
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getUserIndicators(Map<String,Object> data,String userId,String zoneId){
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT A.TAB_ID,A.INDEX_CD,A.INDEX_NAME,decode(B.INDEX_CD,null,0,1) as bj FROM ");
		buffer.append("(SELECT TAB_ID,INDEX_CD,INDEX_NAME,COUNT(*) FROM META_PORTAL_INDEX_DATA T WHERE TAB_ID=? AND LOCAL_CODE=? GROUP BY TAB_ID,INDEX_CD,INDEX_NAME) A ");
		buffer.append("LEFT JOIN (SELECT TAB_ID,INDEX_CD,USER_ID FROM  META_PORTAL_USER_INDEX WHERE  TAB_ID=? AND USER_ID=?) B  ");
		if("1".equals(data.get("reportname").toString())){
			buffer.append("ON  A.INDEX_CD = B.INDEX_CD order by decode(index_cd,'005',2,'002',3,'001',5,'003',6,'006',7,'007',8,'008',4,'004',9)");
		}
		else{
			buffer.append("ON  A.INDEX_CD = B.INDEX_CD order by decode(index_cd,'102',1,'103',2,'117',3,'111',4,'104',5,'105',6,'101',7,'106',8,'107',9,'108',10,'112',11,'110',12,'109',13,'113',14,'116',15)");
		}
		
		//buffer.append("SELECT D.TAB_ID,D.INDEX_CD,D.INDEX_NAME FROM META_PORTAL_INDEX_DATA D WHERE"); 
		//buffer.append(" D.DATE_NO = (SELECT MAX(D.DATE_NO) DATE_NO FROM META_PORTAL_INDEX_DATA D)");
		//buffer.append(" AND D.TAB_ID = ? AND D.LOCAL_CODE = ?");
		//buffer.append(" GROUP BY D.TAB_ID,D.INDEX_CD,D.INDEX_NAME,D.ORDER_ID");
		//buffer.append(" ORDER BY D.ORDER_ID");
		return getDataAccess().queryForList(buffer.toString(),new Object[]{data.get("reportname"),zoneId,data.get("reportname"),userId});
	}
	/**
	 * @Title: getIndexMes 
	 * @Description: 获取灌输数据表的指标编码和名称
	 * @param tabId
	 * @param localCode
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getIndexMes(String tabId,String localCode){
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT D.TAB_ID,D.INDEX_CD,D.INDEX_NAME FROM META_PORTAL_INDEX_DATA D WHERE"); 
		buffer.append(" D.DATE_NO = (SELECT MAX(D.DATE_NO) DATE_NO FROM META_PORTAL_INDEX_DATA D)");
		buffer.append(" AND D.TAB_ID = ? AND D.LOCAL_CODE = ?");
		buffer.append(" GROUP BY D.TAB_ID,D.INDEX_CD,D.INDEX_NAME,D.ORDER_ID");
		buffer.append(" ORDER BY D.ORDER_ID");
		return getDataAccess().queryForList(buffer.toString(),new Object[]{tabId,localCode});
	}
	/**
	 * @Title: getReportTabMes 
	 * @Description: 获取该报表下的所有指标信息
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getIndexInfo(String tabId){
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT  D.INDEX_CD,D.INDEX_NAME FROM META_PORTAL_INDEX_EXPLAIN D  ");
		buffer.append("WHERE D.TAB_ID=?  ");
		
	  	 if(tabId.equals("1")){
	  		    buffer.append(" order by decode(D.index_cd,'006',9,'005',3,'002',4,'001',5,'003',6,'004',7,'007',8,'008',2)");
		  	 }else{
		  		buffer.append(" order by decode(D.index_cd,'102',2,'103',3,'117',1,'111',4,'104',5,'105',6,'101',7,'106',8,'107',9,'108',10,'112',11,'110',12,'109',13,'113',14)");
		}
		return getDataAccess().queryForList(buffer.toString(),tabId);
	}
	
	/**
	 * @Title: getReportTabMes 
	 * @Description: 获取该报表下的所有的列信息
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getColumnsInfo(String tabId){
		String sql = "SELECT COL_ID,COL_CHN_NAME FROM META_PORTAL_COLUMNS WHERE TAB_ID=?";
		return getDataAccess().queryForList(sql,tabId);
	}
	/**
	 * @Title: ReportConfigDAO 
	 * @Description: 保存用户指标配置
	 * @return boolean
	 * @throws
	 */
    public int[] saveUserConfigure(final List<Map<String,Object>> datas) {
        String sql = "INSERT INTO META_PORTAL_USER_INDEX(TAB_ID,INDEX_CD,USER_ID) VALUES(?,?,?)";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
        public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
        Map<String,Object> data=datas.get(i);
        preparedStatement.setObject(1,data.get("tabId")); 	//实体表表名
        preparedStatement.setObject(2,data.get("indexCd")); //表类id
        preparedStatement.setObject(3,data.get("userId"));//表空间
    }
        public int batchSize(){
        	return datas.size();
        }
        });
    }
    
    /**
     * 方法：删除当前用户已配置的所有指标
     * @param data
     * @return  map
    */
    public int deleteIndexData(Map<String,Object> data)
    {
    	String sql = "delete from meta_portal_user_index where tab_id=? and user_id=?"; 
    	
    	return getDataAccess().execUpdate(sql,data.get("tabId").toString(),data.get("userId").toString());
    }
	
	/**
     * 方法：根据
     * @param type 数据源类型名称
     * @param valid 数据源状态
     * @return
     */
    public List<Map<String,Object>> queryReportName(){
    	String sql = "SELECT TAB_ID,TAB_NAME,ROLLDOWN_LAYER,DEFAULT_GRID,MENU_ID,RPT_TYPE FROM META_PORTAL_TAB ORDER BY ORDER_ID";
    	return getDataAccess().queryForList(sql);
    }
    
    /**
     * 方法：执行数据导入
     * @param data
     * @return boolean
     */
    public int[] importData(final List<Map<String,Object>> datas)
    {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("INSERT INTO META_PORTAL_DATA_AUDIT(AUDIT_ID,SCOPE_ID,AUDIT_TIME,AUDIT_USER,AUDIT_CONCLUDE,DATA_DATE,DATA_AREA)");
    	buffer.append("VALUES(SEQ_META_PORTAL_DATA_AUDIT.NEXTVAL,?,to_date(?,'yyyy-MM-dd hh24:mi:ss'),?,?,?,?)");
    	return getDataAccess().execUpdateBatch(buffer.toString(), new IParamsSetter(){
         public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
        	  Map<String,Object> dataRow=datas.get(i);
        	 preparedStatement.setObject(1,dataRow.get("SCOPE_ID"));
        	 preparedStatement.setObject(2,dataRow.get("time"));
        	 preparedStatement.setObject(3,dataRow.get("userId"));
        	 preparedStatement.setObject(4,dataRow.get("Exhibitiontype"));
        	 preparedStatement.setObject(5,dataRow.get("DATA_DATE" + i));
             preparedStatement.setObject(6,dataRow.get("LOCAL_CODE" + i));
         }
         public int batchSize(){
             return datas.size();
         }
     });
    }
    
    /**
     * 方法：根据页面中传入的报表名称， 
     * 查询对应的Scope_id外键及RPT_TYPE字段
     * @param data
     * @return  map
     */
    public List<Map<String,Object>> getScope_id(Map<String,Object> data)
    {
    	String sql = "";
    	if("".equals(data.get("reportname").toString()))
    		
    	{
    		 sql = "SELECT A.TAB_ID,A.SCOPE_ID,B.RPT_TYPE FROM META_PORTAL_SCOPE A,META_PORTAL_TAB B  WHERE  A.TAB_ID=B.TAB_ID";
    		 return getDataAccess().queryForList(sql);
    	}
    	else
    	{
    	     sql = "SELECT A.TAB_ID,A.SCOPE_ID,B.RPT_TYPE FROM META_PORTAL_SCOPE A,"
    		+"META_PORTAL_TAB B  WHERE  A.TAB_ID = (SELECT TAB_ID FROM META_PORTAL_TAB WHERE TAB_ID=?) AND A.TAB_ID=B.TAB_ID";
    	     return getDataAccess().queryForList(sql,data.get("reportname").toString());
    	}
    }
    
    /**
     * 方法：删除日期范围内已经存在的数据
     * @param data
     * @return  map
    */
    public int deleteData(Map<String,Object> data)
    {
    	String sql = "";
    	String rpt_type = data.get("RPT_TYPE").toString();
    	if(rpt_type.equals("1"))
    	{
    		sql = "delete  from meta_portal_data_audit t where DATA_DATE>=TO_CHAR(TO_DATE(?,"+
    		"'YYYY-MM-DD'),'YYYYMMDD') AND DATA_DATE <= TO_CHAR(TO_DATE(?,'YYYY-MM-DD'),'YYYYMMDD') and scope_id=?";
    	}
    	else if(rpt_type.equals("2"))
    	{
    		sql = "delete  from meta_portal_data_audit t where DATA_DATE>=TO_CHAR(TO_DATE(?,"+
    		"'YYYY-MM-DD'),'YYYYMM') AND DATA_DATE <= TO_CHAR(TO_DATE(?,'YYYY-MM-DD'),'YYYYMM') and scope_id=?";
    	}
    	else
    	{
    		return -1;
    	}
    	return getDataAccess().execUpdate(sql,DateUtil.getDateforStamp(data.get("startDate").toString()),
    	    			DateUtil.getDateforStamp(data.get("endDate").toString()),data.get("SCOPE_ID").toString());
    }
    
    /**
     * 方法：获取要导入的数据 
     * @param data
     * @return  List<Map<String,Object>>
     */
    public List<Map<String,Object>> getInputData(Map<String,Object> data)
    {
    	String sql = "";
    	String rpt_type = data.get("RPT_TYPE").toString();
    	if(rpt_type.equals("1"))
    	{
    		sql = "SELECT DATE_NO DATA_DATE,LOCAL_CODE FROM META_PORTAL_INDEX_DATA WHERE DATE_NO >=  TO_CHAR(TO_DATE(?,"+
    		"'YYYY-MM-DD'),'YYYYMMDD') AND DATE_NO <= TO_CHAR(TO_DATE(?,'YYYY-MM-DD'),'YYYYMMDD') AND TAB_ID=?"; 
    	}
    	else if(rpt_type.equals("2"))
    	{
    		sql = "SELECT MONTH_NO DATA_DATE,LOCAL_CODE FROM META_PORTAL_INDEX_DATA WHERE MONTH_NO >= TO_CHAR(TO_DATE(?,"
    			+"'YYYY-MM-DD'),'YYYYMM') AND MONTH_NO <= TO_CHAR(TO_DATE(?,'YYYY-MM-DD'),'YYYYMM') AND TAB_ID=?"; 
    	}
    	else
    	{
    		return null;
    	}
    	return getDataAccess().queryForList(sql,DateUtil.getDateforStamp(data.get("startDate").toString()),
    			DateUtil.getDateforStamp(data.get("endDate").toString()),data.get("TAB_ID").toString());
    }

	/**
	 * @Title: getPortalWarning 
	 * @Description: 获取报表预警所有信息
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getPortalWarning(Map<String,Object> data){
        String sql="SELECT TAB_ID,INDEX_CD,COLUMN_ID,WARING_TYPE,WARING_VALUE,WARING_VALUE2,TAB_NAME,INDEX_NAME,COLUMN_NAME " +
        		" FROM META_PORTAL_WARNING A WHERE A.TAB_ID = ?";	       
		return getDataAccess().queryForList(sql,data.get("repName").toString());
	}
	
	/**
	 * @Title: getReportTabMes 
	 * @Description: 获取报表tab所有信息
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getReportTabMes(Map<String,Object> data){
		String sql = "SELECT T.* FROM META_PORTAL_TAB T ORDER BY T.ORDER_ID";
		if(data != null && StringUtils.isNotEmpty(""+data.get("repName")) && !"null".equals(""+data.get("repName")))
			sql = "SELECT T.* FROM META_PORTAL_TAB T WHERE T.TAB_NAME LIKE '%"+data.get("repName")+"%' ORDER BY T.ORDER_ID";
		return getDataAccess().queryForList(sql);
	}
	
	public boolean saveReportTab(Map<String,Object> data){
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO META_PORTAL_TAB(TAB_ID,TAB_NAME,TAB_DESC,ORDER_ID,ROLLDOWN_LAYER,DEFAULT_GRID,MENU_ID,RPT_TYPE)");
		buffer.append(" VALUES((SELECT NVL(MAX(TAB_ID),0)+1 TAB_ID FROM META_PORTAL_TAB),?,?,");
		buffer.append(" (SELECT NVL(MAX(ORDER_ID),0)+1 TAB_ID FROM META_PORTAL_TAB),?, ?,?,?)");
		Object[] p = new Object[6];
		p[0] = data.get("tabName");
		p[1] = data.get("tabDesc");
//		p[2] = data.get("orderId");
		p[2] = data.get("rolldownLayer");
		p[3] = data.get("defaultGrid");
		p[4] = null;
		p[5] = data.get("rptType");
		return getDataAccess().execNoQuerySql(buffer.toString(), p);
	}
	/**
	 * @Title: getReportTabById 
	 * @Description: 根据ID获取报表tab信息
	 * @param id
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getReportTabById(String id){
		String sql = "SELECT T.* FROM META_PORTAL_TAB T WHERE T.TAB_ID=?";
		return getDataAccess().queryForMap(sql, id);
	}
	/**
	 * @Title: updateReportTabById 
	 * @Description: 根据id更新信息
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean updateReportTabById(Map<String,Object> data){
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE META_PORTAL_TAB SET TAB_NAME=?,TAB_DESC=?,ORDER_ID=?,ROLLDOWN_LAYER=?,DEFAULT_GRID=?,RPT_TYPE=?");
		buffer.append(" WHERE TAB_ID=?");
		Object[] p = new Object[7];
		p[0] = data.get("tabName");
		p[1] = data.get("tabDesc");
		p[2] = data.get("orderId");
		p[3] = data.get("rolldownLayer");
		p[4] = data.get("defaultGrid");
		p[5] = data.get("rptType");
		p[6] = data.get("tabId");
		return getDataAccess().execNoQuerySql(buffer.toString(), p);
	}
	/**
	 * @Title: delReportTabById 
	 * @Description: 根据tabId删除所有信息（1、配置信息；2、灌输的数据信息）
	 * @param id
	 * @return boolean   
	 * @throws
	 */
	public boolean delReportTabById(String id){
		//删除首页重点指标解释表
		String sql = "DELETE FROM META_PORTAL_INDEX_EXPLAIN WHERE TAB_ID=?";
		if(getDataAccess().execNoQuerySql(sql, id)){
			//删除首页指标列配置表
			sql = "DELETE FROM META_PORTAL_COLUMNS WHERE TAB_ID=?";
			if(getDataAccess().execNoQuerySql(sql, id)){
				//删除指标趋势图配置表
				sql = "DELETE FROM META_PORTAL_TREND_CHART WHERE TAB_ID=?";
				if(getDataAccess().execNoQuerySql(sql, id)){
					//删除首页指标数据表
					sql = "DELETE FROM META_PORTAL_INDEX_DATA WHERE TAB_ID=?";
					if(getDataAccess().execNoQuerySql(sql, id)){
						//查找数据范围ID
						sql = "SELECT SCOPE_ID FROM META_PORTAL_SCOPE WHERE TAB_ID=?";
						Map<String,Object> m = getDataAccess().queryForMap(sql, id);
						if(m != null && m.size() > 0){
							//删除数据审核表
							sql = "DELETE FROM META_PORTAL_DATA_AUDIT WHERE SCOPE_ID=?";
							if(getDataAccess().execNoQuerySql(sql, m.get("SCOPE_ID"))){
								//删除数据审核表
								sql = "DELETE FROM META_PORTAL_SCOPE WHERE TAB_ID=?";
								if(getDataAccess().execNoQuerySql(sql, id)){
									//删除首页重点指标分类(标签)表
									sql = "DELETE FROM META_PORTAL_TAB WHERE TAB_ID=?";
									return getDataAccess().execNoQuerySql(sql, id);
								}else
									return false;
							}else
								return false;
						}else{
							//删除首页重点指标分类(标签)表
							sql = "DELETE FROM META_PORTAL_TAB WHERE TAB_ID=?";
							return getDataAccess().execNoQuerySql(sql, id);
						}
					}else
						return false;
				}else
					return false;
			}else
				return false;
		}else
			return false;
	}
	/**
	 * @Title: getRepColMesByTabId 
	 * @Description: 根据tabId获取列信息
	 * @param tabId
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getRepColMesByTabId(String tabId){
		String sql = "SELECT C.* FROM META_PORTAL_COLUMNS C WHERE C.TAB_ID = ? ORDER BY C.SHOW_ORDER_ID";
		return getDataAccess().queryForList(sql, new Object[]{tabId});
	}
	/**
	 * @Title: getRepChartByTabId 
	 * @Description:
	 * @param tabId
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getRepChartByTabId(String tabId){
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT A.*,T.COL_CHN_NAME FROM ("); 
		buffer.append(" SELECT CH.*,I.RULR_ID, I.RULE_NAME FROM");
		buffer.append(" (SELECT CH.* FROM META_PORTAL_TREND_CHART CH WHERE CH.TAB_ID = ?) CH");
		buffer.append(" LEFT JOIN META_PORTAL_TIME_INTERVAL I ON I.RULR_ID = CH.TIME_INTERVAL_ID) A");
		buffer.append(" LEFT JOIN META_PORTAL_COLUMNS T ON A.COLUMN_ID = T.COL_ID");
		return getDataAccess().queryForList(buffer.toString(), new Object[]{tabId});
	}
	/**
	 * @Title: getRepExpByTabId 
	 * @Description: 获取指标解释信息由tabId（包括：RULR_ID，RULE_NAME，用于对INTERVAL_VALUE的中文转换）
	 * @param tabId
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getRepExpByTabId(String tabId){
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		String sql = "SELECT CH.* FROM META_PORTAL_INDEX_EXPLAIN CH WHERE CH.TAB_ID=?";
		List<Map<String,Object>> list = getDataAccess().queryForList(sql, new Object[]{tabId});
		if(list != null && list.size() > 0){
			//获取时间段的中文名
			for(Map<String,Object> m : list){
				Map<String,Object> map = m;
				String s = m.get("TIME_INTERVAL_ID")+"";
				if(StringUtils.isNotEmpty(s) && !"null".equals(s)){
					sql = "SELECT I.RULR_ID,I.RULE_NAME,I.RULE_TYPE FROM META_PORTAL_TIME_INTERVAL I WHERE I.RULR_ID IN ("+s+")";
					List<Map<String,Object>> rMap = getDataAccess().queryForList(sql);
					if(rMap != null && rMap.size() > 0){
						String rulrIds = "";
						String ruleNames = "";
						int typeId = 0;
						for(Map<String,Object> mm : rMap){
							rulrIds += mm.get("RULR_ID")+",";
							ruleNames += mm.get("RULE_NAME")+",";
							typeId = Integer.valueOf(mm.get("RULE_TYPE").toString());
						}
						rulrIds = delLastChart(rulrIds);
						ruleNames = delLastChart(ruleNames);
						map.put("RULE_TYPE", typeId);
						map.put("RULR_ID", rulrIds);
						map.put("RULE_NAME", ruleNames);
					}else{
						map.put("RULE_TYPE", null);
						map.put("RULR_ID", "");
						map.put("RULE_NAME","");
					}
				}else{
					map.put("RULE_TYPE", null);
					map.put("RULR_ID", "");
					map.put("RULE_NAME","");
				}
				res.add(map);
			}
		}
		return res;	
	}
	private String delLastChart(String s){
		if(StringUtils.isEmpty(s) || "null".equals(s))
			return "";
		if(",".equals(s.substring(s.length()-1,s.length())))
			return s.substring(0,s.length()-1);
		else
			return s;
	}
	/**
	 * @Title: getColMesByColId 
	 * @Description: 根据colId获取列信息
	 * @param colId
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getColMesByColId(String colId){
		String sql = "SELECT C.* FROM META_PORTAL_COLUMNS C WHERE C.COL_ID = ?";
		return getDataAccess().queryForMap(sql, new Object[]{colId});
	}
	/**
	 * @Title: delColMesByColId 
	 * @Description: 根据colId删除列信息
	 * @param colId
	 * @return boolean   
	 * @throws
	 */
	public boolean delColMesByColId(String colId){
		String sql = "DELETE FROM META_PORTAL_COLUMNS WHERE COL_ID = ?";
		return getDataAccess().execNoQuerySql(sql,new Object[]{colId});
	}
	/**
	 * @Title: saveColMes 
	 * @Description:保存列信息 
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean saveColMes(Map<String,Object> data){
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO META_PORTAL_COLUMNS(COL_ID,COL_EN_NAME,COL_CHN_NAME,SHOW_ORDER_ID,TAB_ID,COLUMN_COMPANY,COLUMN_COMPANY_POS)");
		buffer.append(" VALUES((SELECT NVL(MAX(C.COL_ID),0)+1 FROM META_PORTAL_COLUMNS C),?,?,");
		buffer.append(" (SELECT NVL(MAX(C.SHOW_ORDER_ID),0)+1 FROM META_PORTAL_COLUMNS C WHERE C.TAB_ID="+data.get("tabId")+"),?,?,?)");
		Object[] p = new Object[5];
		p[0] = data.get("colEnName");
		p[1] = data.get("colChnName");
//		p[2] = data.get("showOrderId");
		p[2] = data.get("tabId");
		p[3] = data.get("columnCompany");
		p[4] = data.get("columnCompanyPos");
		return getDataAccess().execNoQuerySql(buffer.toString(), p);
	}
	/**
	 * @Title: updateColMes 
	 * @Description: 根据colId更新列信息
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean updateColMes(Map<String,Object> data){
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE META_PORTAL_COLUMNS SET COL_EN_NAME=?,COL_CHN_NAME=?,SHOW_ORDER_ID=?,COLUMN_COMPANY=?,COLUMN_COMPANY_POS=?");
		buffer.append(" WHERE COL_ID=?");
		Object[] p = new Object[6];
		p[0] = data.get("colEnName");
		p[1] = data.get("colChnName");
		p[2] = data.get("showOrderId");
		p[3] = data.get("columnCompany");
		p[4] = data.get("columnCompanyPos");
		p[5] = data.get("colId");
		return getDataAccess().execNoQuerySql(buffer.toString(), p);
	}
	/**
	 * @Title: getInterval 
	 * @Description:获取时间区间 
	 * @param state 1:时间段标识；0：不是时间段
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getInterval(String state,int typeId){
		String sql = "SELECT DISTINCT I.RULR_ID,I.RULE_NAME FROM META_PORTAL_TIME_INTERVAL I WHERE I.INTERVAL_STATE=? AND I.RULE_TYPE=?";
		return getDataAccess().queryForList(sql,state,typeId);
	}
	/**
	 * @Title: saveChart 
	 * @Description: 新增图形配置信息
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean saveChart(Map<String,Object> data){
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO META_PORTAL_TREND_CHART(TAB_ID,INDEX_CD,BROKEN_LINE_NAME,BROKEN_LINE_DESC,");
		buffer.append(" BROKEN_LINE_COLOR,TIME_INTERVAL_ID,COLUMN_ID,IS_SHOW_COL)");
		buffer.append(" VALUES(?,?,?,?,?,?,?,?)");
		Object[] p = new Object[8];
		p[0] = data.get("tabId");
		p[1] = data.get("indexCd");
		p[2] = data.get("brokenLineName");
		p[3] = data.get("brokenLineDesc");
		p[4] = data.get("brokenLineColor");
		p[5] = data.get("timeIntervalId");
		p[6] = data.get("columnId");
		p[7] = data.get("isShowCol");
		return getDataAccess().execNoQuerySql(buffer.toString(), p);
	}
	/**
	 * @Title: updateChart 
	 * @Description: 更新图形配置信息
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean updateChart(Map<String,Object> data){
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE META_PORTAL_TREND_CHART SET BROKEN_LINE_NAME=?,BROKEN_LINE_DESC=?,");
		buffer.append(" BROKEN_LINE_COLOR=?,COLUMN_ID=?,IS_SHOW_COL=?");
		buffer.append(" WHERE TAB_ID=? AND INDEX_CD=? AND TIME_INTERVAL_ID=?");
		Object[] p = new Object[8];
		p[0] = data.get("brokenLineName");
		p[1] = data.get("brokenLineDesc");
		p[2] = data.get("brokenLineColor");
		p[3] = data.get("columnId");
		p[4] = data.get("isShowCol");
		p[5] = data.get("tabId");
		p[6] = data.get("indexCd");
		p[7] = data.get("timeIntervalId");
		return getDataAccess().execNoQuerySql(buffer.toString(), p);
	}
	/**
	 * @Title: delChart 
	 * @Description: 删除图形配置信息
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean delChart(Map<String,Object> data){
		String sql = "DELETE FROM META_PORTAL_TREND_CHART WHERE TAB_ID=? AND INDEX_CD=? AND TIME_INTERVAL_ID=?";
		Object[] p = new Object[3];
		p[0] = data.get("tabId");
		p[1] = data.get("indexCd");
		p[2] = data.get("timeIntervalId");
		return getDataAccess().execNoQuerySql(sql, p);
	}
	/**
	 * @Title: saveExp 
	 * @Description: 新增指标解释
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean saveExp(Map<String,Object> data){
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO META_PORTAL_INDEX_EXPLAIN(INDEX_CD,TAB_ID,INDEX_NAME,INDEX_EXPLAIN,");
		buffer.append(" TIME_INTERVAL_ID) VALUES(?,?,?,?,?)");
		Object[] p = new Object[5];
		p[0] = data.get("indexCd");
		p[1] = data.get("tabId");
		p[2] = data.get("indexName");
		p[3] = data.get("indexExpLain");
		p[4] = data.get("timeIntervalId");
		return getDataAccess().execNoQuerySql(buffer.toString(), p);
	}
	/**
	 * @Title: updateExp 
	 * @Description: 修改指标解释
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean updateExp(Map<String,Object> data){
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE META_PORTAL_INDEX_EXPLAIN SET INDEX_NAME=?,INDEX_EXPLAIN=?,TIME_INTERVAL_ID=?");
		buffer.append(" WHERE INDEX_CD=? AND TAB_ID=?");
		Object[] p = new Object[5];
		p[0] = data.get("indexName");
		p[1] = data.get("indexExpLain");
		p[2] = data.get("timeIntervalId");
		p[3] = data.get("indexCd");
		p[4] = data.get("tabId");
		return getDataAccess().execNoQuerySql(buffer.toString(), p);
	}
	/**
	 * @Title: delExp 
	 * @Description: 删除指标解释
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean delExp(Map<String,Object> data){
		String sql = "DELETE FROM META_PORTAL_INDEX_EXPLAIN WHERE INDEX_CD=? AND TAB_ID=?";
		Object[] p = new Object[2];
		p[0] = data.get("indexCd");
		p[1] = data.get("tabId");
		return getDataAccess().execNoQuerySql(sql, p);
	}
	/**
	 * @Title: batchUpdateRepTabOrderId 
	 * @Description: 批量更新tab的排序ID
	 * @param data    
	 * @return void   
	 * @throws
	 */
	public void batchUpdateRepTabOrderId(Object[][] data){
		String sql = "UPDATE META_PORTAL_TAB SET ORDER_ID=? WHERE TAB_ID=?";
		getDataAccess().execUpdateBatch(sql, data);
	}
	/**
	 * @Title: batchUpdateColOrderId 
	 * @Description: 批量更新列的排序ID
	 * @param data    
	 * @return void   
	 * @throws
	 */
	public void batchUpdateColOrderId(Object[][] data){
		String sql = "UPDATE META_PORTAL_COLUMNS SET SHOW_ORDER_ID=? WHERE COL_ID=?";
		getDataAccess().execUpdateBatch(sql, data);
	}
	
	/**
	 * 查询列信息中的相关关联列
	 * @param tableId 报表的id
	 * */
	public List<Map<String,Object>> queryColInfoData(int tableId){
		String sql = "SELECT T.COL_ID,T.COL_CHN_NAME FROM META_PORTAL_COLUMNS T WHERE T.TAB_ID=?";
		return this.getDataAccess().queryForList(sql, tableId);
	}

	/**
	 * 判断当前指标编码是否已经存在
	 * @param code 新增的code
	 * @param tableId 报表id
	 * */
	public boolean isExistCode(String code,int tableId){
		boolean flag = false;
		String sql ="SELECT I.INDEX_CD FROM META_PORTAL_INDEX_EXPLAIN I WHERE I.INDEX_CD=? AND I.TAB_ID=?";
		List<Map<String,Object>> list = this.getDataAccess().queryForList(sql, code,tableId);
		if(list==null||list.size()>0){
			flag = true;
		}
		return flag;
	}

	
	/**
	 * 保存一条预警信息
	 * @param boolean
	 * */
	public boolean saveWarningInfo(Map<String,Object> data){
		String sql = "insert into meta_portal_warning(TAB_ID,INDEX_CD,COLUMN_ID,WARING_TYPE,WARING_VALUE,WARING_VALUE2,TAB_NAME,INDEX_NAME,COLUMN_NAME) VALUES(?,?,?,?,?,?,?,?,?)";
		Object[] p = new Object[9];
		p[0] = data.get("tabId");
		p[1] = data.get("indexCd");
		p[2] = data.get("columnId");
		p[3] = data.get("waringType");
		p[4] = data.get("waringValue");
		p[5] = data.get("waringValue2");
		p[6] = data.get("tabName");
		p[7] = data.get("indexName");
		p[8] = data.get("columnName");
		return getDataAccess().execNoQuerySql(sql, p);
	}
	/**
	 * @Title: getReportTabById 
	 * @Description: 根据tabid及indexCd查询出一条预警信息
	 * @param id
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getWarningId(String tabId,String indexCd, String columnId){
		String sql = "SELECT TAB_ID,INDEX_CD,COLUMN_ID,WARING_TYPE,WARING_VALUE,WARING_VALUE2 FROM META_PORTAL_WARNING WHERE TAB_ID=? AND INDEX_CD=? AND COLUMN_ID=?";
		return getDataAccess().queryForMap(sql, Integer.parseInt(tabId),indexCd,columnId);
	}
	
	/**
	 * @Title: getReportTabById 
	 * @Description: 根据tabid及indexCd删除一条预警信息
	 * @param id
	 * @return Map<String,Object>   
	 * @throws
	 */
	public boolean deleteWarningId(String tabId,String indexCd,String columnId){
		String sql = "delete from  META_PORTAL_WARNING  WHERE TAB_ID = ? AND INDEX_CD =? AND COLUMN_ID=?";
		return getDataAccess().execNoQuerySql(sql,Integer.parseInt(tabId),indexCd,columnId);
	}
	/**
	 * @Title: updateReportTabById 
	 * @Description: 根据tabid及indexCd修改一条预警信息
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean updateWarning(Map<String,Object> data){
		String sql = "UPDATE META_PORTAL_WARNING SET  WARING_TYPE=?,WARING_VALUE=?,WARING_VALUE2=? WHERE TAB_ID=? AND INDEX_CD=? AND COLUMN_ID=?";
		Object[] p = new Object[6];
		p[0] = data.get("waringType");
		p[1] = data.get("waringValue");
		p[2] = data.get("waringValue2");
		p[3] = data.get("tabId");
		p[4] = data.get("indexCd");
		p[5] = data.get("columnId");
		return getDataAccess().execNoQuerySql(sql, p);
	}
	/**
	 * @Title: getReportTabMes 
	 * @Description: 用户所在区域编码
	 * @return String
	 * @throws
	 */
	public String getZoneCode(String zoneId){
		String sql = "select b.zone_code from meta_mag_user a,meta_dim_zone b where a.zone_id=b.zone_id and a.user_id=?";
		return getDataAccess().queryForString(sql,zoneId);
	}
	/**
	 * @Title: getIndexCols 
	 * @Description: 获取灌输数据表的value列集合
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getIndexCols(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT DISTINCT T.COLUMN_NAME FROM ALL_COL_COMMENTS T WHERE T.COLUMN_NAME LIKE 'VALUE%'");
		buffer.append(" AND T.TABLE_NAME = 'META_PORTAL_INDEX_DATA' ORDER BY TO_NUMBER(SUBSTR(T.COLUMN_NAME,6))");
		return getDataAccess().queryForList(buffer.toString());
	}
	public List<Map<String,Object>> getIndexColsInfo(String tabId,String code,String colId,String color){
		String sql = "SELECT DISTINCT T.INDEX_CD FROM META_PORTAL_TREND_CHART T WHERE T.INDEX_CD=? AND T.TAB_ID=? AND T.COLUMN_ID=? AND BROKEN_LINE_COLOR=?";
		return this.getDataAccess().queryForList(sql,code,tabId,colId,color);
		
	}
   
   public List<Map<String, Object>> getUserIndexsByTabId(String tabId,String userId, String zoneId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT A.TAB_ID,A.INDEX_CD,A.INDEX_NAME,decode(B.INDEX_CD,null,0,1) as bj FROM ");
		buffer.append("(SELECT TAB_ID,INDEX_CD,INDEX_NAME,COUNT(*) FROM META_PORTAL_INDEX_DATA T WHERE TAB_ID=? AND LOCAL_CODE=? GROUP BY TAB_ID,INDEX_CD,INDEX_NAME) A ");
		buffer.append("LEFT JOIN (SELECT TAB_ID,INDEX_CD,USER_ID FROM  META_PORTAL_USER_INDEX WHERE  TAB_ID=? AND USER_ID=?) B  ");
		buffer.append("ON  A.INDEX_CD = B.INDEX_CD order by INDEX_CD");
	    return getDataAccess().queryForList(buffer.toString(),new Object[]{tabId,zoneId,tabId,userId});
	}

		/**
		  * 方法描述：
		  * @param: 
		  * @return: 
		  * @version: 1.0
		  * @author: yanhaidong
		  * @version: 2013-6-27 下午05:40:08
		  */
		public String getIndexName(String tabId, String indexId) {
			  StringBuffer buffer = new StringBuffer();
			  buffer.append("SELECT  D.INDEX_NAME FROM META_PORTAL_INDEX_EXPLAIN D WHERE TAB_ID=? AND INDEX_CD=?");
			  return getDataAccess().queryForString(buffer.toString(), tabId,indexId);
		}
		
		/**
		 *  普通报表的预警
		 *  add yanhd start
		 */
		public List<Map<String, Object>> getReportWarnList(String menuName) {
			  StringBuffer buffer = new StringBuffer();
	          buffer.append("SELECT T.* FROM META_CONFIG_WARNING T left join META_MAG_MENU m on t.menu_id = m.menu_id  WHERE m.menu_name =?");
			  return getDataAccess().queryForList(buffer.toString(), menuName);
		} 
		
		
		public List<Map<String, Object>> getReportWarnPage(Map<String,Object> queryData) {
			  Object repName = queryData.get("repName");
			  StringBuffer buffer = new StringBuffer();
			  buffer.append("SELECT T.* FROM META_CONFIG_WARNING T WHERE 1=1 ");
		      if(repName != null && !"".equals(repName)){
		        	buffer.append(" AND  T.Menu_Name LIKE  "+SqlUtils.allLikeParam(Convert.toString(repName).trim()));
		        }
		      buffer.append(" ORDER by T.CREATEDATE DESC");
			  return getDataAccess().queryForList(buffer.toString());
		} 
	
	  public boolean saveReportWarning(Map<String, Object> data) {
			String sql = "insert into META_CONFIG_WARNING(REPORT_ID,MENU_NAME,MENU_ID,INDEX_NAME,COLUMN_NAME,WARING_TYPE,WARING_VALUE,WARING_VALUE2,CREATEDATE) " +
					" VALUES (?,?,?, ?, ?, ?, ?, ?, sysdate)";
			Object[] p = new Object[8];
			p[0] = queryForNextVal("SEQ_RPT_COMMENT_ID");
			p[1] = data.get("reportName");
			p[2] = data.get("menuInfoId");
			p[3] = data.get("indexName");
			p[4] = data.get("columnName");
			p[5] = data.get("waringType");
			p[6] = data.get("waringValue");
			p[7] = data.get("waringValue2");
			return getDataAccess().execNoQuerySql(sql, p);
		}
	  
  public boolean deleteReportWarning(String reportId) {
		String sql = "delete from  META_CONFIG_WARNING  WHERE REPORT_ID = ?";
		return getDataAccess().execNoQuerySql(sql, reportId);
	}
  public Map<String, Object> getReportWarningId(String reportId) {
	  String sql = "SELECT MENU_NAME,MENU_ID,INDEX_NAME,COLUMN_NAME,WARING_TYPE,WARING_VALUE,WARING_VALUE2 FROM META_CONFIG_WARNING " +
	  		" WHERE  REPORT_ID = ? ";
		return getDataAccess().queryForMap(sql,  reportId); 
  }
  
  

  public boolean updateReportWarning(Map<String, Object> data) {
		String sql = " UPDATE META_CONFIG_WARNING SET MENU_NAME=?,MENU_ID=?,INDEX_NAME=?,COLUMN_NAME=?,WARING_TYPE=?,WARING_VALUE=?,WARING_VALUE2=?, CREATEDATE=sysdate WHERE REPORT_ID=?";
		Object[] p = new Object[8];
		p[0] = data.get("reportName");
		p[1] = data.get("menuInfoId");
		p[2] = data.get("indexName");
		p[3] = data.get("columnName");
		p[4] = data.get("waringType");
		p[5] = data.get("waringValue");
		p[6] = data.get("waringValue2");
		p[7] = data.get("reportId");
		return getDataAccess().execNoQuerySql(sql, p);
  }
		/**
		 *  add yanhd end
		 */





}
