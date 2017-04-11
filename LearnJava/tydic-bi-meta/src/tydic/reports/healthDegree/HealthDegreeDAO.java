package tydic.reports.healthDegree;

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

public class HealthDegreeDAO extends MetaBaseDAO {
    //健康度模型_评分架构
	public Map<String, Object> getScoringArchitecture_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_CS_HEALTHY_IDENTIFY;
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
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
	 //健康度模型_总体情况
	public Map<String, Object> getOverallPerformance_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_CS_HEALTHY_MAIN;
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
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
	 //健康度模型_业务项
	public Map<String, Object> getBusinessItem_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_CS_HEALTHY_AREA;
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
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
	 //健康度模型_业务指标
	public Map<String, Object> getBusinessIndex_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_CS_HEALTHY_ITEM;
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "indexId", null),
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
	//最大月
	public String getMaxMonth(String tableStr) {
		String sql = "select rownum,MONTH_ID from (select distinct MONTH_ID from "+tableStr +
		          " where MONTH_ID is not null order by MONTH_ID desc) where rownum=1";
		return getDataAccess().queryForString(sql);
	}
	//月列表
	public List<Map<String, Object>> getMonList(String tableStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct MONTH_ID from "+tableStr+" where MONTH_ID is not null" +
				" order by MONTH_ID desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//总体情况图形数据
	public  List<Map<String, Object>>getChartData_OverallPerformance(String dateTime){
		 StringBuffer sql = new StringBuffer("select a.REGION_ID,a.REGION_NAME,sum(a.ind_score)ind_score,b.order_id from META_USER.CS_HEALTHY_MAIN a," +
		 		"META_DIM_ZONE b where a.region_id=b.zone_code and a.month_id='"+dateTime+"' and dim_level='3' group by a.region_id,a.REGION_NAME,b.order_id order by b.order_id");
		 return getDataAccess().queryForList(sql.toString());
	}
	//业务项列表数据
	public  List<Map<String, Object>>getBusinessItemList(){
		 StringBuffer sql = new StringBuffer("select IND_ID,IND_NAME from cs_healthy_identify where ind_par_id='1' order by ind_par_id");
		 return getDataAccess().queryForList(sql.toString());
	}
	//通过大区获取地市列表
	public  List<Map<String, Object>>getZoneList(String zoneCode){
		 StringBuffer sql = new StringBuffer("select zone_code,zone_name from meta_dim_zone where zone_par_code='"+zoneCode+"' order by order_id");
		 return getDataAccess().queryForList(sql.toString());
	}
	//雷达图数据
	public  Map<String,Object>getBusinessItemRadarMap(String zoneCode,String indId,String dateTime){
		 StringBuffer sql = new StringBuffer("select sum(ind_score)ind_score from META_USER.CS_HEALTHY_MAIN where region_id='"+zoneCode+"' and month_id='"+dateTime+"' and ind_id like '"+indId+"%'");
		 return getDataAccess().queryForMap(sql.toString());
	}
	//雷达图数据平均分
	public  Map<String,Object>getBusinessItemAvgRadarMap(String zoneCode,String dateTime){
		 StringBuffer sql = new StringBuffer("select avg(sum(case when m.ind_id like '1001%' then ind_score end  )) 即时回访满意率," +
		 		"avg(sum(case when m.ind_id like '1002%' then ind_score end  )) 投诉率," +
		 		"avg(sum(case when m.ind_id like '1003%' then ind_score end  )) 客户维系," +
		 		"avg(sum(case when m.ind_id like '1004%' then ind_score end  )) 宽带专项服务提升," +
		 		"avg(sum(case when m.ind_id like '1005%' then ind_score end  )) 划小责任提升 " +
		 		"from META_USER.CS_HEALTHY_MAIN m ,meta_dim_zone z where m.month_id='"+dateTime+"' " +
		 		"and m.REGION_TREECODE like '%"+zoneCode+"%' and m.region_id=z.zone_code and z.dim_level in(3) group by z.zone_code,z.zone_name, " +
		 		"z.order_id order by order_id");
		 return getDataAccess().queryForMap(sql.toString());
	}
	
}

