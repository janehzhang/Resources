package tydic.portalCommon;


import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.MetaBaseDAO;


/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:首页报表数据展现DAO
 * @date 2012-03-30
 */
public class MetaPortalIndexDataDAO extends MetaBaseDAO{
	/**
	 * @Title: isHaveDataByTabIdAndUserId 
	 * @Description: 判断首页指标用户配置表是否配置了信息
	 * @param tabId 
	 * @param userId
	 * @return boolean  配置：true；否则：false 
	 * @throws
	 */
	public boolean isHaveDataByTabIdAndUserId(String tabId,String userId){
		String sql = "SELECT COUNT(ROWID) COUNTS FROM META_PORTAL_USER_INDEX U WHERE U.TAB_ID = ? AND U.USER_ID =?";
		Object[][] obj = getDataAccess().queryForArray(sql, false, new Object[]{tabId,userId});
		if(Integer.valueOf(""+obj[0][0]) > 0)
			return true;
		else
			return false;
	}
    /**
     * 获取每一个标签对应的同一层次某一区域的所有指标汇总数据
     * @param tabId  标签页ID
     * @param dataDate 数据日期
     * @param localCode  用户所属区域编码
     * @param columns 要查询的列数组
     * @return
     */
    public Object[][] getData(long tabId,int rptType,String dataDate,
                              String localCode,String[] columns,boolean isConfig, String userId){
        String  sql="SELECT " ;
        if (columns != null && columns.length != 0) {
            for (int i = 0; i < columns.length; i++) {
                sql+= StringUtils.isEmpty(columns[i])?("'' tempColumn"+i):columns[i];
                sql+=",";
            }
        }
        if(isConfig){
        	 sql+="D.LOCAL_CODE,D.AREA_NAME,D.INDEX_CD,D.INDEX_NAME INDEXNAME,D.TYPE_ID,D.TYPE_NAME FROM META_PORTAL_INDEX_DATA D,META_PORTAL_USER_INDEX I";
             sql += " WHERE D.TAB_ID = I.TAB_ID AND D.INDEX_CD = I.INDEX_CD AND I.USER_ID="+userId+" AND ";
        }else{
        	 sql+="D.LOCAL_CODE,D.AREA_NAME,D.INDEX_CD,D.INDEX_NAME INDEXNAME,D.TYPE_ID,D.TYPE_NAME FROM META_PORTAL_INDEX_DATA D";
             sql += " WHERE";
        }
        if(rptType==1){
        	sql +=" D.DATE_NO=? AND D.TAB_ID=? AND D.LOCAL_CODE=? ";
        }else{
        	dataDate = dataDate.substring(0,6);
        	sql +=" D.MONTH_NO=? AND D.TAB_ID=? AND D.LOCAL_CODE=? ";
        }
        
       if(tabId==1){
            sql  +="order by d.TYPE_ID, decode(d.index_cd,'006',9,'005',2,'002',3,'001',5,'003',6,'004',7,'007',8,'008',4)";
          }else{
            sql  +="order by d.TYPE_ID, decode(d.index_cd,'102',1,'103',2,'117',3,'111',4,'104',5,'105',6,'101',7,'106',8,'107',9,'108',10,'112',11,'110',12,'109',13,'113',14)";  	
         }
            
        return getDataAccess().queryForArray(sql,false,dataDate,tabId,localCode);

    }
    
   /**yanhd  start**/
	public String getNewDate() {
		String sql = "select to_date(max(t.date_no),'yyyy-MM-dd')  date_no  from  META_PORTAL_INDEX_DATA t where t.tab_id = '1'";
		return getDataAccess().queryForString(sql);
	}
    public String getNewMonth() {
		String sql = "select to_date(max(t.month_no),'yyyy-MM')   month_no  from META_PORTAL_INDEX_DATA t where t.tab_id = '2'";
		return getDataAccess().queryForString(sql);
	}
	
    public List<Map<String,Object>> getViewTabs(Map<String, Object> map){
    	String tabId =    MapUtils.getString(map,   "tabId", null);
        String dataDate = MapUtils.getString(map,   "dataDate", null).replaceAll("-", "");
        String zoneCode = MapUtils.getString(map,   "zoneCode", null);
    	StringBuffer sql = new StringBuffer("select distinct t.type_id,t.type_name from  META_PORTAL_INDEX_DATA t  " +
    	 		"where t.tab_id='"+tabId+"' ");
         if(tabId.equals("1")){
      	   sql.append(" and t.date_no = '"+dataDate+"'");
         }else{
      	   sql.append(" and t.month_no= '"+dataDate+"'");
         }
           sql.append(" and t.local_code='"+zoneCode+"'");
           sql.append(" order by t.type_id");
    	 return getDataAccess().queryForList(sql.toString());
    }   
    
    
   
   public List<Map<String,Object>> getTableData(Map<String,Object>  map){
    	String tabId =    MapUtils.getString(map, "tabId", null);
        String dataDate = MapUtils.getString(map, "dataDate", null).replaceAll("-", "");
        String zoneCode =   MapUtils.getString(map, "zoneCode", null);
        String typeId=    MapUtils.getString(map, "typeId", null);
        
        StringBuffer sql = new StringBuffer("select t.TAB_ID,d.one_menu,d.Url,D.TABID,D.REPORTNAME,t.MONTH_NO,t.DATE_NO,t.LOCAL_CODE,t.AREA_NAME,t.INDEX_CD," +
        		"t.INDEX_NAME,round(t.VALUE2,2) VALUE2,round(t.VALUE3,2) VALUE3,round(t.VALUE4,2) VALUE4," +
        		"round(t.VALUE5,2) VALUE5,round(t.VALUE6,2) VALUE6,round(t.VALUE7,2)" +
        		" VALUE7,round(t.VALUE8,2) VALUE8,round(t.VALUE9,2) VALUE9,round(t.VALUE10,2) VALUE10,t.ORDER_ID" +
        		" from META_PORTAL_INDEX_DATA t left join META_PORTAL_INDEX_EXPLAIN  d on  t.INDEX_CD = d.index_cd" +
        		" where t.tab_id = '"+tabId+"' and t.TYPE_NAME='"+typeId+"'"); 
           sql.append(" and t.local_code='"+zoneCode+"'");
       if(tabId.equals("1")){
    	   sql.append(" and t.date_no = '"+dataDate+"'");
           sql.append(" order by t.TYPE_ID, decode(t.index_cd,'006',9,'005',3,'002',4,'001',5,'003',6,'004',7,'007',8,'008',2)");
       }else{
    	   sql.append(" and t.month_no= '"+dataDate+"'");
    	   sql.append(" order by t.TYPE_ID, decode(t.index_cd,'102',2,'103',3,'117',1,'111',4,'104',5,'105',6,'101',7,'106',8,'107',9,'108',10,'112',11,'110',12,'109',13,'113',14)");
    	    	    
       }
       return getDataAccess().queryForList(sql.toString());
   }
    
  public  Map<String,Object> getChartData(String i, Map<String,Object> map) {
	  	String tabId =    MapUtils.getString(map, "tabId", null);
	    String zoneCode =   MapUtils.getString(map, "zoneCode", null);
	    String indexCd =  MapUtils.getString(map, "indexCd", null);
	  
	   StringBuffer sql = new StringBuffer("select round(t.VALUE2,2) VALUE2,round(t.VALUE3,2) VALUE3,round(t.VALUE4,2) VALUE4,round(t.VALUE5,2) VALUE5,round(t.VALUE6,2) VALUE6," +
	  		"round(t.VALUE7,2) VALUE7,round(t.VALUE8,2) VALUE8,round(t.VALUE9,2) VALUE9," +
	  		"round(t.VALUE10,2) VALUE10 from META_PORTAL_INDEX_DATA t " +
      		" where t.tab_id = '"+tabId+"' "); 
      
     if(tabId.equals("1")){
  	   sql.append(" and t.date_no = '"+i+"'");
  	   sql.append(" and t.local_code='"+zoneCode+"'");
     }else{
  	   sql.append(" and t.month_no= '"+i+"'");
  	   sql.append(" and t.local_code='"+zoneCode+"'");
     }
       sql.append(" and t.index_cd='"+indexCd+"'");
       
	  return getDataAccess().queryForMap(sql.toString());
  }  
  public List<Map<String, Object>> getIndexExp(Map<String, Object> map) {
	  	String tabId = MapUtils.getString(map, "tabId", null);
	  	StringBuffer sql = new StringBuffer("select t.index_name,t.index_explain" +
	  			" from META_PORTAL_INDEX_EXPLAIN t where t.tab_id = '"+tabId+"' "); 
	  
	  	 if(tabId.equals("1")){
	  	    sql.append(" order by decode(t.index_cd,'006',9,'005',3,'002',4,'001',5,'003',6,'004',7,'007',8,'008',2)");
	  	 }else{
	  		sql.append(" order by decode(t.index_cd,'102',2,'103',3,'117',1,'111',4,'104',5,'105',6,'101',7,'106',8,'107',9,'108',10,'112',11,'110',12,'109',13,'113',14)");
	  	 }
	  	return getDataAccess().queryForList(sql.toString());
   }
    
	
  /**
	  * 钻取数据
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2013-3-6 下午04:21:08
	  */
	public List<Map<String, Object>> getDrillTableData(Map<String, Object> map) {
    	String tabId =    MapUtils.getString(map, "tabId", null);
        String dataDate = MapUtils.getString(map, "dataDate", null).replaceAll("-", "");
        String zoneCode = MapUtils.getString(map, "zoneCode", null);
        String indexCd =  MapUtils.getString(map, "indexCd", null);
        
        StringBuffer sql = new StringBuffer("select t.TAB_ID,t.MONTH_NO,t.DATE_NO,t.LOCAL_CODE,d.zone_id,d.zone_code,t.AREA_NAME,t.INDEX_CD," +
        		"t.INDEX_NAME,round(t.VALUE2,2) VALUE2,round(t.VALUE3,2) VALUE3,round(t.VALUE4,2) VALUE4," +
        		"round(t.VALUE5,2) VALUE5,round(t.VALUE6,2) VALUE6,round(t.VALUE7,2) VALUE7," +
        		"round(t.VALUE8,2) VALUE8,round(t.VALUE9,2) VALUE9,round(t.VALUE10,2) VALUE10,t.ORDER_ID,t.TYPE_NAME," +
        		"t.TYPE_ID from META_PORTAL_INDEX_DATA t " +
        		"LEFT JOIN META_DIM_ZONE d ON t.local_code = d.ZONE_CODE " +
        		"where t.tab_id = '"+tabId+"' "); 
        
       if(tabId.equals("1")){
    	   sql.append(" and t.date_no  = '"+dataDate+"'");
       }else{
    	   sql.append(" and t.month_no = '"+dataDate+"'");
       }
       sql.append(" and d.zone_par_code='"+zoneCode+"'");
	   sql.append(" and t.INDEX_CD='"+indexCd+"'");
       sql.append(" ORDER BY d.dim_level, d.ORDER_ID");
       return getDataAccess().queryForList(sql.toString());
	} 
	
	
	/**
	 *  21个地市的数据
	  * 方法描述：
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-4-24 下午04:52:59
	 */
	public List<Map<String, Object>> get21DrillTableData(Map<String, Object> map) {
    	String tabId =    MapUtils.getString(map, "tabId", null);
        String dataDate = MapUtils.getString(map, "dataDate", null).replaceAll("-", "");
        //String zoneCode = MapUtils.getString(map, "zoneCode", null);
        String indexCd =  MapUtils.getString(map, "indexCd", null);
        
        StringBuffer sql = new StringBuffer("select t.TAB_ID,t.MONTH_NO,t.DATE_NO,t.LOCAL_CODE,d.zone_id,d.zone_code,t.AREA_NAME,t.INDEX_CD," +
        		"t.INDEX_NAME,round(t.VALUE2,2) VALUE2,round(t.VALUE3,2) VALUE3,round(t.VALUE4,2) VALUE4," +
        		"round(t.VALUE5,2) VALUE5,round(t.VALUE6,2) VALUE6,round(t.VALUE7,2) VALUE7," +
        		"round(t.VALUE8,2) VALUE8,round(t.VALUE9,2) VALUE9,round(t.VALUE10,2) VALUE10,t.ORDER_ID,t.TYPE_NAME," +
        		"t.TYPE_ID from META_PORTAL_INDEX_DATA t " +
        		"LEFT JOIN META_DIM_ZONE d ON t.local_code = d.ZONE_CODE " +
        		"where t.tab_id = '"+tabId+"' "); 
        
       if(tabId.equals("1")){
    	   sql.append(" and t.date_no  = '"+dataDate+"'");
       }else{
    	   sql.append(" and t.month_no = '"+dataDate+"'");
       }
       sql.append(" and d.dim_level='3'");
	   sql.append(" and t.INDEX_CD='"+indexCd+"'");
       sql.append(" ORDER BY d.dim_level, d.ORDER_ID");
       return getDataAccess().queryForList(sql.toString());
	}  	
    
	// 加载图形
	public List<Map<String, Object>> loadSetChart(Map<String, Object> map) {
		String tabId = MapUtils.getString(map, "tabId", null);
		StringBuffer sql = new StringBuffer(
				" select ID,TAB_ID,TAB_NAME,INDEX_CD,INDEX_NAME,SHOW_ID,SHOW_NAME,CHART_NAME,CURRENT_COLOR,LAST_COLOR,SHAPE_ID " +
				"from META_PORTAL_SET_CHART t where t.tab_id='"+tabId+"'  order by id");
		return getDataAccess().queryForList(sql.toString());
	}
	
  /**yanhd end**/    
    
    public Object[][] getChildData(long tabId,int rptType,String dataDate,
                              String localCode,String indexCd,String[] columns){
         String  sql="SELECT A.LOCAL_CODE, " +
                 "?,A.INDEX_CD,A.INDEX_NAME,C.ZONE_NAME, " ;
        if (columns != null && columns.length != 0) {
            for (int i = 0; i < columns.length; i++) {
                sql+= StringUtils.isEmpty(columns[i])?"":columns[i]+",";
            }
        }
        sql+="A.TAB_ID FROM META_PORTAL_INDEX_DATA A LEFT JOIN " +
                "META_DIM_ZONE C ON A.LOCAL_CODE = C.ZONE_CODE " +
                " WHERE C.ZONE_PAR_ID IN " +
                "(SELECT B.ZONE_ID FROM META_DIM_ZONE B WHERE B.ZONE_CODE=? ) AND ";
        if(rptType==1){
        	sql +=" A.DATE_NO=? AND A.TAB_ID=? AND A.INDEX_CD=? ORDER BY c.dim_level, c.ORDER_ID ,A.ORDER_ID ASC ";
        }else{
        	dataDate = dataDate.substring(0,6);
        	sql +=" A.MONTH_NO=? AND A.TAB_ID=? AND A.INDEX_CD=? ORDER BY c.dim_level, c.ORDER_ID ,A.ORDER_ID ASC ";
        }
        return getDataAccess().queryForArray(sql,false,localCode,localCode,dataDate,tabId,indexCd);
    }
    
    public int getTabRptType(long tabId){
    	String sql = "select t.rpt_type from meta_portal_tab t where t.tab_id=?";
    	return getDataAccess().queryForInt(sql, tabId);
    }
    
    
	public static String getLastMon(String currentMon){
		String tempStr=currentMon.substring(4, currentMon.length());
		Integer retValue=0;
		if("01".equals(tempStr)){
			retValue=Integer.parseInt(currentMon)-89;
		}else{
			retValue=Integer.parseInt(currentMon)-1;
		}
		return String.valueOf(retValue);
	}
	



}
