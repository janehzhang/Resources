/**
  * 文件名：ServWarnDayDAO.java
  * 版本信息：Version 1.0
  * 日期：2013-6-26
  * Copyright tydic.com.cn Corporation 2013 
  * 版权所有
  */
package tydic.portalCommon.procedure.ts.servWarn.day;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DBOutParameter;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.yhd.constant.ConstantStoreProc;


	/**
 * 类描述：
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-6-26 下午03:45:00 
 */
public class ServWarnDayDAO extends MetaBaseDAO {
	/**
	  * 方法描述：
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-6-17 上午09:27:39
	  */
	public String getNewDate() {
		String sql = "select to_date(max(t.DAY_ID), 'yyyy-MM-dd') date_no from CS_SERV_WARN_DAY  t ";
		return  getDataAccess().queryForString(sql);
	}
	
    
	public List<Map<String, Object>> getProdTypeList(Map<String, Object> param) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct t.PROD_TYPE_ID,t.PROD_TYPE_NAME FROM CS_SERV_WARN_DAY t ORDER BY abs(t.PROD_TYPE_ID)");
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
	  * 方法描述：曲线图数据
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-5-29 下午02:06:07
	  */
	public Map<String, Object> getChartData(String dateTime,Map<String, Object> param) {
		String zoneCode = MapUtils.getString(param, "zoneCode", null);
		String prodType = MapUtils.getString(param, "prodType", null);
		StringBuffer sql = new StringBuffer();
		sql.append("select rownum TOP10排名, z.*");
		sql.append(" from (select sum(NUM1) 当日投诉量, sum(NUM3) 昨日投诉量");
		sql.append(" from CS_SERV_WARN_DAY a");
		sql.append(" WHERE DAY_ID = '"+dateTime+"'");
		sql.append(" AND REGION_ID = '"+zoneCode+"'");
		List<Object> params = new ArrayList<Object>();
		if (prodType != null && !"".equals(prodType)) {
			sql.append(" AND PROD_TYPE_ID=?");
			params.add(Convert.toString(prodType));
		}
		sql.append(" group by CMPL_BUSINESS_TYPE1_NAME,");
		sql.append(" CMPL_BUSINESS_TYPE1_ID,");
		sql.append(" CMPL_BUSINESS_TYPE2_NAME,");
		sql.append(" CMPL_BUSINESS_TYPE2_ID,");
		sql.append(" CMPL_BUSINESS_TYPE3_NAME,");
		sql.append(" CMPL_BUSINESS_TYPE3_ID,");
		sql.append(" DAY_ID");
		sql.append(" order by sum(NUM1) desc) z");
		sql.append(" WHERE rownum = 1");
		
		return getDataAccess().queryForMap(sql.toString(), params.toArray());
	}


	/**
	  * 方法描述：柱状图数据
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-5-29 下午03:12:12
	  */
	public List<Map<String, Object>> getDrillTableData(Map<String, Object> param) {
    String dateTime =    MapUtils.getString(param, "dayTime", null);
	String zoneCode = MapUtils.getString(param, "zoneCode", null);
	String prodType = MapUtils.getString(param, "prodType", null);
	
	StringBuffer sql = new StringBuffer();
	sql.append("select rownum TOP10排名, z.*");
	sql.append(" from (select sum(NUM1) 当日投诉量, sum(NUM3) 昨日投诉量,");
	sql.append(" a.region_id, b.zone_name,b.dim_level,b.order_id");
	sql.append(" from CS_SERV_WARN_DAY a ");
	sql.append(" LEFT JOIN META_DIM_ZONE b");
	sql.append(" ON a.region_id = b.ZONE_CODE");
	sql.append(" WHERE a.DAY_ID = '"+dateTime+"'");
	sql.append(" AND b.zone_par_code= '"+zoneCode+"'");
	
	List<Object> params = new ArrayList<Object>();
	if (prodType != null && !"".equals(prodType)) {
		sql.append(" AND a.PROD_TYPE_ID=?");
		params.add(Convert.toString(prodType));
	}
	sql.append(" group by a.region_id, b.zone_name,b.dim_level,b.order_id");
	sql.append(" ORDER BY b.dim_level, b.ORDER_ID) z");
    return getDataAccess().queryForList(sql.toString(),params.toArray());
 }
	
	
	
	/**
	  * 方法描述：21个地市柱状图
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-5-30 上午10:48:48
	 */
	public List<Map<String, Object>> get21DrillTableData(Map<String, Object> param) {
    String dateTime =    MapUtils.getString(param, "dayTime", null);
   	String prodType =    MapUtils.getString(param, "prodType",    null);
   	
   	
	StringBuffer sql = new StringBuffer();
	sql.append("select rownum TOP10排名, z.*");
	sql.append(" from (select sum(NUM1) 当日投诉量, sum(NUM3) 昨日投诉量,");
	sql.append(" a.region_id, b.zone_name,b.dim_level,b.order_id");
	sql.append(" from CS_SERV_WARN_DAY a ");
	sql.append(" LEFT JOIN META_DIM_ZONE b");
	sql.append(" ON a.region_id = b.ZONE_CODE");
	sql.append(" WHERE a.DAY_ID = '"+dateTime+"'");
	sql.append(" AND b.dim_level='3'");
	
	List<Object> params = new ArrayList<Object>();
	if (prodType != null && !"".equals(prodType)) {
		sql.append(" AND a.PROD_TYPE_ID=?");
		params.add(Convert.toString(prodType));
	}
	sql.append(" group by a.region_id, b.zone_name,b.dim_level,b.order_id");
	sql.append(" ORDER BY b.dim_level, b.ORDER_ID) z");
      
      return getDataAccess().queryForList(sql.toString(),params.toArray());
  }

}
