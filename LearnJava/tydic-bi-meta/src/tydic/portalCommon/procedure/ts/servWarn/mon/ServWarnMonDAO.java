/**
  * 文件名：ServWarnMonDAO.java
  * 版本信息：Version 1.0
  * 日期：2013-7-24
  * Copyright tydic.com.cn Corporation 2013 
  * 版权所有
  */
package tydic.portalCommon.procedure.ts.servWarn.mon;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DBOutParameter;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.yhd.constant.ConstantStoreProc;


	/**
 * 类描述：
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-7-24 下午02:44:36 
 */
public class ServWarnMonDAO extends MetaBaseDAO{

	public String getNewMonth() {
		String sql = "select to_date(max(t.MONTH_ID),'yyyy-MM')   month_no  from CS_CMPL_PROBLEM_MON t ";
		return getDataAccess().queryForString(sql);
	} 	
	
	public List<Map<String, Object>> queryMonList(Map<String, Object> param) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct t.MONTH_ID FROM CS_CMPL_PROBLEM_MON t ORDER BY t.MONTH_ID desc");
	    return getDataAccess().queryForList(sql.toString());
	}
	
	public List<Map<String, Object>> getProdTypeList(Map<String, Object> param) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct t.PROD_TYPE_ID,t.PROD_TYPE_NAME FROM CS_CMPL_PROBLEM_MON  t ORDER BY to_number(abs(t.PROD_TYPE_ID))");
	    return getDataAccess().queryForList(sql.toString());
	}

	
	/**
	  * 方法描述：
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-5-28 下午05:14:49
	  */
	public Map<String, Object> getTableData(Map<String, Object> map) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.P_COMMON_RPT_4J;
		Object[] params = {
				MapUtils.getString(map, "reportId", null),
				MapUtils.getString(map, "parameters", null),
				MapUtils.getString(map, "values", null),
				"$",
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}

	
	/**
	  * 方法描述：曲线图
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-7-24 下午04:15:59
	  */
	public Map<String, Object> getChartData(String dateTime, Map<String, Object> param) {
		String zoneCode = MapUtils.getString(param, "zoneCode", null);
		String prodType = MapUtils.getString(param, "prodType", null);
		StringBuffer sql = new StringBuffer();
		sql.append("select rownum TOP10排名,z.* from (");
		sql.append(" select ");
		sql.append(" sum(NUM1) 投诉量");
		sql.append(" ,sum(NUM3) 上月投诉量");
		sql.append(" ,trim(to_char(decode(sum(NUM3),0,0,round((sum(NUM1)/sum(NUM3)-1)*100,2)),'FM99990.00')||'%') 投诉量环比");
		sql.append(" ,trim(to_char(DECODE(sum(NUM2),0,0,round(sum(NUM1)/sum(NUM2)*1000,2)),'FM99990.00')||'‰') 投诉率");
		sql.append(" ,trim(to_char(DECODE(sum(NUM4),0,0,round(sum(NUM3)/sum(NUM4)*1000,2)),'FM99990.00')||'‰') 上月投诉率");
		sql.append(" ,trim(to_char(DECODE(DECODE(SUM(NUM4),0,0,SUM(NUM3)/SUM(NUM4)),0,0,DECODE(sum(NUM2),0,0,");
		sql.append(" ROUND(((SUM(NUM1)/SUM(NUM2))/(SUM(NUM3)/SUM(NUM4))-1)*100,2))),'FM99990.00')||'%') 投诉率环比");
		sql.append(" from CS_CMPL_PROBLEM_MON a  where month_id = '"+dateTime+"' ");
		sql.append(" and REGION_ID = '"+zoneCode+"'");
		sql.append(" and exists");
		sql.append(" (select 1");
		sql.append(" from tbas_dm.d_v_cmpl_prod_condition b");
		sql.append(" where a.prod_type_id = b.CMPL_PROD_TYPE_CODE");
		sql.append(" start with CMPL_PROD_TYPE_CODE = nvl('"+prodType+"','-1')");
		sql.append(" connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)");
		sql.append(" group by CMPL_BUSINESS_TYPE1_NAME,CMPL_BUSINESS_TYPE1_ID,CMPL_BUSINESS_TYPE2_NAME,");
		sql.append(" CMPL_BUSINESS_TYPE2_ID,CMPL_BUSINESS_TYPE3_NAME,CMPL_BUSINESS_TYPE3_ID");
		sql.append(" order by decode(sum(NUM2),0,0,round(sum(NUM1)/sum(NUM2)*1000,2)) desc) z");
		sql.append(" WHERE rownum=1");
	    return getDataAccess().queryForMap(sql.toString());
	}

	

	
	/**
	  * 方法描述：柱状图
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-7-24 下午04:40:07
	  */
	public List<Map<String, Object>> getDrillTableData(Map<String, Object> param) {
		String dateTime =    MapUtils.getString(param, "dateTime", null);
    	String zoneCode =    MapUtils.getString(param, "zoneCode",    null);
    	String prodType =    MapUtils.getString(param, "prodType",    null);
    	StringBuffer sql = new StringBuffer();
		sql.append("select rownum TOP10排名,z.* from ( select  sum(NUM1) 投诉量 ");
		sql.append(" ,sum(NUM3) 上月投诉量 ");
		sql.append(" ,trim(to_char(decode(sum(NUM3),0,0,round((sum(NUM1)/sum(NUM3)-1)*100,2)),'FM99990.00')||'%') 投诉量环比 ");
		sql.append(" ,trim(to_char(DECODE(sum(NUM2),0,0,round(sum(NUM1)/sum(NUM2)*1000,2)),'FM99990.00')||'‰') 投诉率 ");
		sql.append(" ,trim(to_char(DECODE(sum(NUM4),0,0,round(sum(NUM3)/sum(NUM4)*1000,2)),'FM99990.00')||'‰') 上月投诉率"); 
		sql.append(" ,trim(to_char(DECODE(DECODE(SUM(NUM4),0,0,SUM(NUM3)/SUM(NUM4)),0,0,DECODE(sum(NUM2),0,0, ROUND(((SUM(NUM1)/SUM(NUM2))/(SUM(NUM3)/SUM(NUM4))-1)*100,2))),'FM99990.00')||'%') 投诉率环比");
		sql.append(" ,a.region_id, b.zone_name,b.dim_level,b.order_id");
		sql.append(" from CS_CMPL_PROBLEM_MON a LEFT JOIN META_DIM_ZONE b"); 
		sql.append(" ON a.region_id = b.ZONE_CODE");
		sql.append(" where month_id = '"+dateTime+"'  and zone_par_code = '"+zoneCode+"' ");
		  
		sql.append(" and ");
		sql.append(" exists (select 1 from tbas_dm.d_v_cmpl_prod_condition b where a.prod_type_id = b.CMPL_PROD_TYPE_CODE start with CMPL_PROD_TYPE_CODE = nvl('"+prodType+"','-1')");
		sql.append(" connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)"); 
		sql.append(" group by  a.region_id, b.zone_name,b.dim_level,b.order_id");
		sql.append(" order by  b.dim_level,b.ORDER_ID) z");

       return getDataAccess().queryForList(sql.toString());
       
	}	
	
	/**
	  * 方法描述：21个地市
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-7-24 下午04:40:00
	  */
	public List<Map<String, Object>> get21DrillTableData(Map<String, Object> param) {
		String dateTime =    MapUtils.getString(param, "dateTime", null);
    	//String zoneCode =    MapUtils.getString(param, "zoneCode",    null);
    	String prodType =    MapUtils.getString(param, "prodType",    null);
    	StringBuffer sql = new StringBuffer();
		sql.append("select rownum TOP10排名,z.* from ( select  sum(NUM1) 投诉量 ");
		sql.append(" ,sum(NUM3) 上月投诉量 ");
		sql.append(" ,trim(to_char(decode(sum(NUM3),0,0,round((sum(NUM1)/sum(NUM3)-1)*100,2)),'FM99990.00')||'%') 投诉量环比 ");
		sql.append(" ,trim(to_char(DECODE(sum(NUM2),0,0,round(sum(NUM1)/sum(NUM2)*1000,2)),'FM99990.00')||'‰') 投诉率 ");
		sql.append(" ,trim(to_char(DECODE(sum(NUM4),0,0,round(sum(NUM3)/sum(NUM4)*1000,2)),'FM99990.00')||'‰') 上月投诉率"); 
		sql.append(" ,trim(to_char(DECODE(DECODE(SUM(NUM4),0,0,SUM(NUM3)/SUM(NUM4)),0,0,DECODE(sum(NUM2),0,0, ROUND(((SUM(NUM1)/SUM(NUM2))/(SUM(NUM3)/SUM(NUM4))-1)*100,2))),'FM99990.00')||'%') 投诉率环比");
		sql.append(" ,a.region_id, b.zone_name,b.dim_level,b.order_id");
		sql.append(" from CS_CMPL_PROBLEM_MON a LEFT JOIN META_DIM_ZONE b"); 
		sql.append(" ON a.region_id = b.ZONE_CODE");
		sql.append(" where month_id = '"+dateTime+"'  and b.dim_level='3' ");
		  
		sql.append(" and ");
		sql.append(" exists (select 1 from tbas_dm.d_v_cmpl_prod_condition b where a.prod_type_id = b.CMPL_PROD_TYPE_CODE start with CMPL_PROD_TYPE_CODE = nvl('"+prodType+"','-1')");
		sql.append(" connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)"); 
		sql.append(" group by  a.region_id, b.zone_name,b.dim_level,b.order_id");
		sql.append(" order by  b.dim_level,b.ORDER_ID) z");

       return getDataAccess().queryForList(sql.toString());
	}

}
