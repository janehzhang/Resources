package tydic.reports.codeMonitor;
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
 * @modify  qx
 * @modifyDate  2013-12-12
 */
public class CodeMonitorDAO extends MetaBaseDAO {
	//获取最大月份
	public String getMaxMonth(String tabStr){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select Max(MONTH_ID) from "+tabStr);
		return getDataAccess().queryForString(buffer.toString());
	}
	//获取月份列表
	public List<Map<String, Object>>getMonsList(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct MONTH_ID from "+tabStr+" order by MONTH_ID desc ");
		return getDataAccess().queryForList(buffer.toString());
	}
	//获取最大周期
	public String getMaxWeek(String tabStr){
		String sql = "select WEEK_ID from (select distinct WEEK_ID from "+tabStr+
        " where WEEK_ID is not null order by WEEK_ID desc) where rownum=1";
        return getDataAccess().queryForString(sql);
	}
	//获取周列表
	public List<Map<String, Object>>getWeeksList(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct WEEK_ID as DATE_NO from "+tabStr+
				" order by WEEK_ID desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//获取周列表除第一周以外
	public List<Map<String, Object>>getWeeksListAtt(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct WEEK_ID as DATE_NO from "+tabStr+
				" where WEEK_ID not in (select WEEK_ID from (select distinct WEEK_ID from "+tabStr+
				" order by WEEK_ID desc)where rownum = 1) order by WEEK_ID desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	public Map<String, Object> getCustCodeMonitor_pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_CUST_PWD_ACT;	
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "isRealName", null),
				MapUtils.getString(queryData, "custType", null),
				MapUtils.getString(queryData, "custLevel", null),
				MapUtils.getString(queryData, "prodId", null),
				MapUtils.getString(queryData, "erId", null),
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
	public Map<String, Object> getCustCodeExceptionMonitor_pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_CUST_PWD_MONITORING;	
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "isRealName", null),
				MapUtils.getString(queryData, "custType", null),
				MapUtils.getString(queryData, "custLevel", null),
				MapUtils.getString(queryData, "prodId", null),
				MapUtils.getString(queryData, "erId", null),
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
	public Map<String, Object> getCustCodeActiveMonitor_pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_CUST_PWD_ACT_LEVEL;	
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "isRealName", null),
				MapUtils.getString(queryData, "custType", null),
				MapUtils.getString(queryData, "custLevel", null),
				MapUtils.getString(queryData, "prodId", null),
				MapUtils.getString(queryData, "erId", null),
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
	public Map<String, Object> getCustCodePreferMonitor_pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_CUST_PWD_ACT_LIKE;	
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "isRealName", null),
				MapUtils.getString(queryData, "custType", null),
				MapUtils.getString(queryData, "custLevel", null),
				MapUtils.getString(queryData, "prodId", null),
				MapUtils.getString(queryData, "erId", null),
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
	/***
	 * 查询 客户密码激活总体报表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getCustCodeMonitor_sum(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_CUST_PWD_ACT_SUM;	
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "pwdType", null),
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
}
