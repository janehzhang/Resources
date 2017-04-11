/**
  * 文件名：MaintionMonitorAction.java
  * 版本信息：Version 1.0
  * 日期：2013-5-28
  * Copyright tydic.com.cn Corporation 2013 
  * 版权所有
  */
package tydic.portalCommon.procedure.zd.mon;

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
 * @version: 2013-5-28 下午03:59:47 
 */
public class MaintionMonitorMonDAO  extends MetaBaseDAO {

		public List<Map<String, Object>> queryMonList(Map<String, Object> param) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT distinct t.MONTH_ID FROM CS_MAINTAIN_MONITOR_MON t ORDER BY t.MONTH_ID desc");
		    return getDataAccess().queryForList(sql.toString());
		}
		
		
		public List<Map<String, Object>> getProdTypeList(Map<String, Object> param) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT distinct t.PROD_TYPE_ID,t.PROD_TYPE_NAME FROM CS_MAINTAIN_MONITOR_MON t ORDER BY to_number(abs(t.PROD_TYPE_ID))");
		    return getDataAccess().queryForList(sql.toString());
		}
		
		public List<Map<String, Object>> getIndexList(Map<String, Object> param) {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT distinct t.ind_id,t.ind_name FROM CS_MAINTAIN_MONITOR_MON t ORDER BY to_number(t.IND_ID)");
		   return getDataAccess().queryForList(sql.toString());
		}
		//by jiong
		public List<Map<String, Object>> getIndexList() {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT distinct t.ind_id,t.ind_name FROM CS_MAINTAIN_MONITOR_MON t ORDER BY to_number(t.IND_ID)");
		   return getDataAccess().queryForList(sql.toString());
		}
		/**
		  * 方法描述：表格数据
		  * @param: 
		  * @return: 
		  * @version: 1.0
		  * @author: yanhaidong
		  * @version: 2013-5-28 下午05:14:49
		  */
		public Map<String, Object> getTableData(Map<String, Object> map) {
			Map<String, Object> mapList =new  HashMap<String, Object>();
			String storeName = ConstantStoreProc.RPT_ZW_MONTH;
			Object[] params = {
					MapUtils.getString(map, "dateTime",    null).replaceAll("-", ""),
					MapUtils.getString(map, "zoneCode",   null),
					MapUtils.getString(map, "indexId",    null),
					MapUtils.getString(map, "prodType",    null),
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
		  * 方法描述：
		  * @param: 
		  * @return: 
		  * @version: 1.0
		  * @author: yanhaidong
		  * @version: 2013-5-30 下午04:23:25
		  */
		public Map<String, Object> getChartData(String dateTime, Map<String, Object> param) {
			String zoneCode = MapUtils.getString(param, "zoneCode", null);
			String prodType = MapUtils.getString(param, "prodType", null);
			String indexId =  MapUtils.getString(param, "indexId", null);
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT SUM(DECODE(IND_ID" +
					",1,DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2))" +
					",2,DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2))" +
					",3,DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2))" +
					",9,DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2))" +
					",SUM(NUM1))) 当月值 FROM CS_MAINTAIN_MONITOR_MON t" +
					" WHERE t.MONTH_ID  = '"+dateTime+"' AND t.REGION_ID = '"+zoneCode+"' ");
			List<Object> params = new ArrayList<Object>();
			if (prodType != null && !"".equals(prodType)) {
				sql.append(" AND PROD_TYPE_ID=?");
				params.add(Convert.toString(prodType));
			}
			if (indexId != null && !"".equals(indexId)) {
				sql.append(" AND IND_ID=?");
				params.add(Convert.toString(indexId));
			}
			   sql.append("GROUP BY IND_ID");
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
			String dateTime =    MapUtils.getString(param, "dateTime", null).replaceAll("-", "");
	    	String zoneCode =    MapUtils.getString(param, "zoneCode",    null);
	    	String indexId =     MapUtils.getString(param, "indexId",     null);
	    	String prodType =    MapUtils.getString(param, "prodType",    null);
	    	StringBuffer sql = new StringBuffer("SELECT SUM(当月值) 当月值 ,region_id, zone_name FROM (SELECT DECODE(IND_ID," +
	    			"1,DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2))," +
	    			"2,DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2))," +
	    			"3,DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2))," +
	    			"9,DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2))," +
	    			"SUM(NUM1)) 当月值 ,t.region_id, d.zone_name,d.dim_level,d.order_id" +
	    			" FROM CS_MAINTAIN_MONITOR_MON t LEFT JOIN META_DIM_ZONE d ON t.region_id = d.ZONE_CODE " +
	    			" WHERE t.MONTH_ID = '"+dateTime+"' and d.zone_par_code = '"+zoneCode+"'"); 
	    	       
	    	        List<Object> params=new ArrayList<Object>();
	                if(indexId != null && !"".equals(indexId)){
	                    sql.append(" AND t.IND_ID=?");
	                    params.add(Convert.toString(indexId));
	                }
	                if(prodType != null && !"".equals(prodType)){
	                    sql.append(" AND t.PROD_TYPE_ID=?");
	                    params.add(Convert.toString(prodType));
	                }
	                sql.append(" GROUP BY IND_ID,t.region_id, d.zone_name,d.dim_level,d.order_id) temp");
	                sql.append(" GROUP BY region_id,zone_name,dim_level,ORDER_ID");
	                sql.append(" ORDER BY dim_level,ORDER_ID");
	
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
			String dateTime =    MapUtils.getString(param, "dateTime", null).replaceAll("-", "");
	    	String zoneCode =    MapUtils.getString(param, "zoneCode",    null);
	    	String indexId =     MapUtils.getString(param, "indexId",     null);
	    	String prodType =    MapUtils.getString(param, "prodType",    null);
	    	StringBuffer sql = new StringBuffer("SELECT SUM(当月值) 当月值 ,region_id, zone_name FROM (SELECT DECODE(IND_ID," +
	    			"1,DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2))," +
	    			"2,DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2))," +
	    			"3,DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2))," +
	    			"9,DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2))," +
	    			"SUM(NUM1)) 当月值 ,t.region_id, d.zone_name,d.dim_level,d.order_id" +
	    			" FROM CS_MAINTAIN_MONITOR_MON t LEFT JOIN META_DIM_ZONE d ON t.region_id = d.ZONE_CODE " +
	    			" WHERE t.MONTH_ID = '"+dateTime+"' and d.dim_level='3'"); 
	    	       
	    	        List<Object> params=new ArrayList<Object>();
	                if(indexId != null && !"".equals(indexId)){
	                    sql.append(" AND t.IND_ID=?");
	                    params.add(Convert.toString(indexId));
	                }
	                if(prodType != null && !"".equals(prodType)){
	                    sql.append(" AND t.PROD_TYPE_ID=?");
	                    params.add(Convert.toString(prodType));
	                }
	                sql.append(" GROUP BY IND_ID,t.region_id, d.zone_name,d.dim_level,d.order_id) temp");
	                sql.append(" GROUP BY region_id,zone_name,dim_level,ORDER_ID");
	                sql.append(" ORDER BY dim_level,ORDER_ID");
	       return getDataAccess().queryForList(sql.toString(),params.toArray());
		}
		
		public String getNewMonth() {
			String sql = "select to_date(max(t.MONTH_ID),'yyyy-MM')   month_no  from CS_MAINTAIN_MONITOR_MON t where  1=1";
			return getDataAccess().queryForString(sql);
		} 	 
}
