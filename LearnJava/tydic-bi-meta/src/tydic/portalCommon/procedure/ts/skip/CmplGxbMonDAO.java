/**
  * 文件名：CmplGxbMonDAO.java
  * 版本信息：Version 1.0
  * 日期：2013-8-22
  * Copyright tydic.com.cn Corporation 2013 
  * 版权所有
  */
package tydic.portalCommon.procedure.ts.skip;

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
 * @version: 2013-8-22 下午03:50:37 
 */
public class CmplGxbMonDAO extends MetaBaseDAO{

	public String getNewMonth() {
		String sql = "select to_date(max(t.MONTH_ID),'yyyy-MM')   month_no  from cs_span_cmpl t";
		return getDataAccess().queryForString(sql);
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
		String storeName = ConstantStoreProc.RPT_CMPL_GXB_MON;
		Object[] params = {
				MapUtils.getString(map, "dateTime", null),
				MapUtils.getString(map, "zoneCode", null),
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
		   StringBuffer sql = new StringBuffer();
		/**sql.append("select sum(num3+num9) 工信部申诉量" +
				   " FROM cs_span_cmpl  a where a.month_id = '"+ dateTime +"' " +
				    " and a.region_treecode like  getTreeCode('"+zoneCode+"') || '%' ");**/
		  
		    String lastTime=getLastMon(dateTime);
		    sql.append("select a.yj_num 工信部申诉量,");
			sql.append(" a.user_num 上月固网移动用户数,");
			sql.append(" to_char(DECODE(a.user_num,0,0,round((a.yj_num / a.user_num) * 1000000, 2)),'FM99990.00') 工信部申诉率");
			sql.append(" from (select a.REGION_TREECODE,");
			sql.append(" yj_num,");
			sql.append(" (b.user_gh + b.user_xlt + b.user_cdma) user_num");
			sql.append(" from (select t.treecode REGION_TREECODE, sum(num3 + num9) yj_num");
			sql.append(" from cs_span_cmpl a, meta_dim_zone t");
			sql.append(" where a.month_id = '"+dateTime+"'");
			sql.append(" and a.region_treecode like getTreeCode('"+zoneCode+"') || '%'");
			sql.append(" and t.dim_level <= 3");
			sql.append(" group by (t.treecode)) a");
			sql.append(" left join cs_cust_jfyhs_mon b");
			sql.append(" on a.REGION_TREECODE = b.REGION_TREECODE(+)");
			sql.append(" and b.month_id = '"+lastTime+"') a");
			sql.append(" where a.region_treecode = getTreeCode('"+zoneCode+"')");
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
		List<Map<String, Object>>  listMap= new ArrayList<Map<String, Object>>();
		String dateTime =    MapUtils.getString(param, "dateTime", null);
    	String zoneCode =    MapUtils.getString(param, "zoneCode",    null);
    	StringBuffer sql = new StringBuffer();
    	sql.append("select b.zone_code,b.zone_name from META_DIM_ZONE b where " +
    			"b.zone_par_code = '"+zoneCode+"' order by b.dim_level, b.ORDER_ID ");
        listMap=getDataAccess().queryForList(sql.toString());
      for(Map<String, Object> map : listMap){
    	String code=map.get("ZONE_CODE").toString();
    	StringBuffer sb = new StringBuffer();
    	/**sb.append("select sum(num3+num9) 工信部申诉量 " +
				   " FROM cs_span_cmpl a where a.month_id = '"+ dateTime +"' " +
				    " and a.region_treecode like  getTreeCode('"+code+"') || '%' ");**/
	    String lastTime=getLastMon(dateTime);
	    sb.append("select a.yj_num 工信部申诉量,");
		sb.append(" a.user_num 上月固网移动用户数,");
		sb.append(" to_char(DECODE(a.user_num,0,0,round((a.yj_num / a.user_num) * 1000000, 2)),'FM99990.00') 工信部申诉率");
		sb.append(" from (select a.REGION_TREECODE,");
		sb.append(" yj_num,");
		sb.append(" (b.user_gh + b.user_xlt + b.user_cdma) user_num");
		sb.append(" from (select t.treecode REGION_TREECODE, sum(num3 + num9) yj_num");
		sb.append(" from cs_span_cmpl a, meta_dim_zone t");
		sb.append(" where a.month_id = '"+dateTime+"'");
		sb.append(" and a.region_treecode like getTreeCode('"+code+"') || '%'");
		sb.append(" and t.dim_level <= 3");
		sb.append(" group by (t.treecode)) a");
		sb.append(" left join cs_cust_jfyhs_mon b");
		sb.append(" on a.REGION_TREECODE = b.REGION_TREECODE(+)");
		sb.append(" and b.month_id = '"+lastTime+"') a");
		sb.append(" where a.region_treecode = getTreeCode('"+code+"')");
		map.putAll(getDataAccess().queryForMap(sb.toString()));
       }
       return listMap;
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
		List<Map<String, Object>>  listMap= new ArrayList<Map<String, Object>>();
		String dateTime =    MapUtils.getString(param, "dateTime", null);
    	StringBuffer sql = new StringBuffer();
    	sql.append("select b.zone_code,b.zone_name from META_DIM_ZONE b where " +
    			" b.dim_level='3' order by b.dim_level, b.ORDER_ID ");
        listMap=getDataAccess().queryForList(sql.toString());
      for(Map<String, Object> map : listMap){
    	String code=map.get("ZONE_CODE").toString();
    	StringBuffer sb = new StringBuffer();
		/**sb.append("select sum(num3+num9) 工信部申诉量 " +
				   " FROM cs_span_cmpl a where a.month_id = '"+ dateTime +"' " +
				    " and a.region_treecode like  getTreeCode('"+code+"') || '%' ");**/
    	String lastTime=getLastMon(dateTime);
	    sb.append("select a.yj_num 工信部申诉量,");
		sb.append(" a.user_num 上月固网移动用户数,");
		sb.append(" to_char(DECODE(a.user_num,0,0,round((a.yj_num / a.user_num) * 1000000, 2)),'FM99990.00') 工信部申诉率");
		sb.append(" from (select a.REGION_TREECODE,");
		sb.append(" yj_num,");
		sb.append(" (b.user_gh + b.user_xlt + b.user_cdma) user_num");
		sb.append(" from (select t.treecode REGION_TREECODE, sum(num3 + num9) yj_num");
		sb.append(" from cs_span_cmpl a, meta_dim_zone t");
		sb.append(" where a.month_id = '"+dateTime+"'");
		sb.append(" and a.region_treecode like getTreeCode('"+code+"') || '%'");
		sb.append(" and t.dim_level <= 3");
		sb.append(" group by (t.treecode)) a");
		sb.append(" left join cs_cust_jfyhs_mon b");
		sb.append(" on a.REGION_TREECODE = b.REGION_TREECODE(+)");
		sb.append(" and b.month_id = '"+lastTime+"') a");
		sb.append(" where a.region_treecode = getTreeCode('"+code+"')");   	
		map.putAll(getDataAccess().queryForMap(sb.toString()));
       }
       return listMap;
	}	
	
    public static String getLastMon(String currentMon) {
			String tempStr = currentMon.substring(4, currentMon.length());
			Integer retValue = 0;
			if ("01".equals(tempStr)) {
				retValue = Integer.parseInt(currentMon) - 89;
			} else {
				retValue = Integer.parseInt(currentMon) - 1;
			}
			return String.valueOf(retValue);
		}
}
