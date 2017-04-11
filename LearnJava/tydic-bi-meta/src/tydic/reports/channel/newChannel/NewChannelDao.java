package tydic.reports.channel.newChannel;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DBOutParameter;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.yhd.constant.ConstantStoreProc;
import tydic.meta.common.yhd.utils.Pager;
import tydic.portalCommon.DateUtil;
import tydic.reports.CommonUtils.CommonUtils;
/***
 * 渠道偏好视图 数据库DAO类
 * @author 我爱家乡
 *
 */
public class NewChannelDao extends MetaBaseDAO{
	/***
	 * 获取新渠道归类树
	 * @param beginId
	 * @param endId
	 * @return
	 */
	public List<Map<String, Object>> queryChannelNewServById(String beginId, String endId) {
		   String sql = "SELECT A.SERV_ID,A.SERV_NAME,A.SERV_PAR_ID,A.SERV_DESC,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
				+ " FROM META_DM.DM_CHANNEL_SERV_NEW A"
				+ " LEFT JOIN (SELECT SERV_PAR_ID, COUNT(1) CNT"
				+ " FROM META_DM.DM_CHANNEL_SERV_NEW"
				+ " GROUP BY SERV_PAR_ID) C"
				+ " ON A.SERV_ID = C.SERV_PAR_ID" 
				+ " WHERE A.SERV_ID =?";
				Object[] proParams ={endId};
				return getDataAccess().queryForList(sql, proParams);
		}
	//获取查询条件_类型
	public List<Map<String, Object>> getSelectType() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select CHANNEL_TYPE_CODE as CHANNEL_TYPE_ID,CHANNEL_TYPE_NAME,ORDER_ID from meta_dm.d_v_channel_type_c where DIM_LEVEL='2' order by to_number(CHANNEL_TYPE_PAR_CODE),to_number(ORDER_ID)");
		return getDataAccess().queryForList(buffer.toString());
	}
	/***
	 * 根据渠道服务的父ID 查询子数据
	 * @param parentCode
	 * @return
	 */
	public List<Map<String, Object>> querySubChannelNewServId(String parentCode) {
	      String select = "SELECT A.SERV_ID,A.SERV_NAME,A.SERV_PAR_ID,A.SERV_DESC,A.STATE,A.DIM_LEVEL,A.ORDER_ID,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
					+ " FROM META_DM.DM_CHANNEL_SERV_NEW A"
					+ " LEFT JOIN (SELECT SERV_PAR_ID, COUNT(1) CNT"
					+ " FROM META_DM.DM_CHANNEL_SERV_NEW"
					+ " GROUP BY SERV_PAR_ID) C"
					+ " ON A.SERV_ID = C.SERV_PAR_ID"
					+ " WHERE A.SERV_PAR_ID = ? "//and (serv_id like '10%' or serv_id like '30%')
					+ " ORDER BY dim_level, ORDER_ID ASC";
			Object[] proParams = {parentCode};
			return getDataAccess().queryForList(select, proParams);
		}
	
	/**
	 * 获取周
	 * @return
	 */
	public List<Map<String, Object>> getMaxWeek(String tableName) {
		String sql = "select   WEEK_NAME, MONTH_NO from (select distinct  WEEK_ID as WEEK_NAME  ,MONTH_ID AS MONTH_NO "+
          " from " + tableName + "   where week_id is not null) order by MONTH_NO desc, WEEK_NAME desc ";
		return  getDataAccess().queryForList(sql.toString());
	}
	
	/**
	 * 获取最新的天数据
	 * @param tabStr
	 * @param actType
	 * @return
	 */
	public String getNewDay(String tabStr) {
		 
		String sql = "select GET_MAX_PARTITION_DATE('"+tabStr+"',8) day_id from dual ";
		return getDataAccess().queryForString(sql);
	}
	
	//获取查询条件_月份
	public List<Map<String, Object>> getSelectMon(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct MONTH_ID as month_id from "+tabStr+"  where month_id is not null  order by MONTH_ID desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	//获取最大月份
	public String getNewMonth(String tabStr) {
		String sql = "select GET_MAX_PARTITION_DATE('"+tabStr+"',6) month_no from dual";
		return getDataAccess().queryForString(sql);
	}
	
	public List<Map<String, Object>> getKeyMonth(String tabStr,String reportType){
	  String sqlStr="select  t.ind_time as MONTH_ID from META_USER."+tabStr+" t  where t.ind_id like  '"+reportType+"%' group by t.ind_time order by t.ind_time desc";
		
	  return getDataAccess().queryForList(sqlStr);
	}
	
	public List<Map<String, Object>> getMonthList(String indId){
		  String sqlStr="select distinct ind_time MONTH_ID from CS_KF_INDEX_EXPRESSION where ind_id like '"+indId+"%' order by ind_time desc";
		  return getDataAccess().queryForList(sqlStr);
	 }
	public List<Map<String, Object>> getMonList(String tabStr){
		  String sqlStr="select distinct MONTH_ID from "+tabStr+"  order by MONTH_ID desc";
		  return getDataAccess().queryForList(sqlStr);
	 }
	
	public List<Map<String, Object>> getMonthListAllType(String tabStr,String monStr){
		  String sqlStr="select distinct("+monStr+") from "+tabStr+"  order by "+monStr+" desc";
		  return getDataAccess().queryForList(sqlStr);
	 }
	public String getMaxMonthAllType(String tabStr,String monStr){
		  String sqlStr="select MAX("+monStr+") from "+tabStr;
		  return getDataAccess().queryForString(sqlStr);
	 }
	/***
	 * 获取集团的月份
	 * @param indId
	 * @return
	 */
	public List<Map<String, Object>> getGroupMonthList(String date_type,String tableName){
		  String sqlStr="select distinct V_DATE MONTH_ID from "+tableName+"  where DATE_TYPE ='"+date_type+"' order by V_DATE desc";
		  return getDataAccess().queryForList(sqlStr);
	 }
	public List<Map<String, Object>> getGroupMonthList_NEW(String dateName,String tableName){
		  String sqlStr="select distinct "+dateName+" MONTH_ID from "+tableName+" order by "+dateName+" desc";
		  return getDataAccess().queryForList(sqlStr);
	 }
	
	//获取最大月份
	public String getKeyMonth2(String tabStr,String reportType) {
		String sql = "select max(t.ind_time) month_no from META_USER."+tabStr+" t where  t.ind_id like  '"+reportType+"%' ";
		return getDataAccess().queryForString(sql);
	}
	//获取最大月份
	public String getMaxDate(String indId) {
		String sql = "select max(ind_time)month_no from CS_KF_INDEX_EXPRESSION where ind_id like '"+indId+"%'";
		return getDataAccess().queryForString(sql);
	}
	public List<Map<String, Object>> getMonList(String tabStr,String tempNum){
		  String sqlStr="select distinct substr(partition_name, 2) MONTH_ID from user_Segments " +
					"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = "+tempNum+" order by MONTH_ID desc ";
		  return getDataAccess().queryForList(sqlStr);
	}
	public String getMaxMonth(String tableType,String tempNum) {
		String sql="select GET_MAX_PARTITION_DATE('"+tableType+"',"+tempNum+") from dual";
		return getDataAccess().queryForString(sql);
	}
	//渠道分类报表获取最大周
	public String getMaxWeek2(String tableType) {
		 
		String sql = "select GET_MAX_PARTITION_DATE('"+tableType+"',17) from dual";
		return getDataAccess().queryForString(sql);
	}
	public String getMaxMonth(String tableType) {
		String sql = "select  max(month_id) month_id from "+tableType;
		return getDataAccess().queryForString(sql);
	}
	//查询最大日期 
	public String getMaxDateByWeek(String tableType){
		 
		StringBuffer buffer = new StringBuffer();
		buffer.append("select max(v_date) from "+tableType+" where date_type='1'");
		return getDataAccess().queryForString(buffer.toString());
	}
	/***
	 * 获取周
	 * @param tableType
	 * @return
	 * @throws ParseException
	 */
	public List<Map<String, Object>> getWeekList2(String tableType) throws ParseException {
		 
	   StringBuffer buffer = new StringBuffer();
 
		buffer.append("select  replace(substr(t.partition_name,2),'_','~') DATE_NO from SYS.USER_SEGMENTS t where t.segment_name='"+tableType+"' and length(substr(t.partition_name,2))=17");

		return getDataAccess().queryForList(buffer.toString());	
	}
	
	//获取查询条件_月份
	public List<Map<String, Object>> getSelectMon2(String tableType) {
		 
	   StringBuffer buffer = new StringBuffer();
	   buffer.append("select   substr(t.partition_name,2) MONTH_ID from SYS.USER_SEGMENTS t where t.segment_name='"+tableType+"' and length(substr(t.partition_name,2))=6");
		return getDataAccess().queryForList(buffer.toString());
	}
 
	
	
	/***
	 * 存储过程 执行查询 渠道偏好视图
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelAll_pg(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.RPT_CS_CHANNEL_PREF_VIEW;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "queryType", null),
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
	 * 存储过程 区域渠道服务一级报表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelServerFisrt_pg(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.RPT_CS_CHANNEL_ONE_PRO;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
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
		// System.out.println(mapList.size());
		return mapList;
		
	}
	
	/***
	 * 存储过程 执行查询 渠道偏好视图用户级
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelViewUser_pg(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.RPT_CS_CHANNEL_PREF_VIEW_USER;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "queryType", null),
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
	 * 存储过程 执行系统监控最新时间更新
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> systemApply(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.PRO_SYSTEM_APPLY;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "tab", null),
				MapUtils.getString(queryData, "tab2", null),
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
	 * 存储过程 执行系统接口监控最新时间更新
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> systemInterface(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.PRO_SYSTEM_INTERFACE;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "tab5", null),
				MapUtils.getString(queryData, "tab6", null),
				MapUtils.getString(queryData, "tab7", null),
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
	 * 存储过程 偏好应用-服务偏好
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelServicePreferencr(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.RPT_CS_CHANNEL_SERV_SUM;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "terminal", null),
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
	 * 存储过程 偏好应用-渠道偏好 整体
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelSum(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.RPT_CS_CHANNEL_SUM;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "service", null),
				MapUtils.getString(queryData, "terminal", null),
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
	 * 存储过程 偏好应用-服务偏好 六大细分市场&在网时长&用户价值
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelServiceSix(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.PTR_CS_CHANNEL_SEVER_SIX;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "terminal", null),
				MapUtils.getString(queryData, "state", null),
				MapUtils.getString(queryData, "touchpoint", null),
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
	 * 存储过程 偏好应用-服务偏好 六大细分市场&在网时长&用户价值
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelSix(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.RPT_CS_CHANNEL_SIX;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "service", null),
				MapUtils.getString(queryData, "terminal", null),
				MapUtils.getString(queryData, "state", null),
				MapUtils.getString(queryData, "touchpoint", null),
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
	 * 存储过程 偏好应用  产品类型
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelServiceTERMINAL(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.PTR_CS_CHANNEL_SEVER_TERMINAL;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "terminal", null),
				MapUtils.getString(queryData, "state", null),
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
	 * 存储过程      偏好应用-渠道偏好      产品类型&客户等级
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelTERMINAL(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.PTR_CS_CHANNEL_TERMINAL;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "service", null),
				MapUtils.getString(queryData, "terminal", null),
				MapUtils.getString(queryData, "state", null),
				MapUtils.getString(queryData, "touchpoint", null),
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
	 * 存储过程 偏好应用  客户等级
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelServiceCUSTLEVEL(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.PTR_CS_CHANNEL_SEVER_CUSTLEVEL;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "terminal", null),
				MapUtils.getString(queryData, "state", null),
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
	 * 存储过程 偏好应用  付费方式
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelServicePAYMENT(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.PTR_CS_CHANNEL_SEVER_PAYMENT;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "terminal", null),
				MapUtils.getString(queryData, "state", null),
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
	 * 存储过程 偏好应用  付费方式
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> channelPAYMENT(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.PTR_CS_CHANNEL_PAYMENT;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "service", null),
				MapUtils.getString(queryData, "terminal", null),
				MapUtils.getString(queryData, "state", null),
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
	 * 区域渠道服务二级报表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelServerSecond_pg(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.RPT_CS_CHANNEL_SECOND_PRO;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
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
		// System.out.println(mapList.size());
		return mapList;
		
	}	

	/***
	 * 区域渠道服务三级报表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelServerThrid_pg(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.RPT_CS_CHANNEL_THRID_PRO;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
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
		// System.out.println(mapList.size());
		return mapList;
		
	}	
	
	/***
	 * 区域渠道服务三级报表 办理
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelServerThridTransaction_pg(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= CommonUtils.RPT_CS_CHANNEL_THRID_TR_PRO;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
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
		// System.out.println(mapList.size());
		return mapList;
		
	}
	
	
	/***
	 *   渠道服务偏好视图
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryChannelCustView_pg(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName="";
		if(queryData.get("preferType").equals("1")){ //表示客户等级
			storeName= CommonUtils.RPT_CS_CHANNEL_CUST_VIEW;//获取存储过程
		}
		if(queryData.get("preferType").equals("2")){ //战略群偏好视图
			storeName= CommonUtils.RPT_CS_CHANNEL_CUST_GROUP_VIEW;//获取存储过程
		}
		if(queryData.get("preferType").equals("3")){ //客户品牌好视图
			storeName= CommonUtils.RPT_CS_CHANNEL_CUST_BRAND_VIEW;//获取存储过程
		}		
		if(queryData.get("preferType").equals("4")){ //用户价值渠道服务偏好
			storeName= CommonUtils.RPT_CS_CHANNEL_CUST_ARPU_VIEW;//获取存储过程
		}			
		if(queryData.get("preferType").equals("5")){ // 在网时长渠道服务偏好
			storeName= CommonUtils.RPT_CS_CHANNEL_CUST_TIME_VIEW;//获取存储过程
		}			
		
		if(queryData.get("preferType").equals("6")){ // 付费方式渠道服务偏好
			storeName= CommonUtils.RPT_CS_CHANNEL_CUST_PAY_VIEW;//获取存储过程
		}			 
		if(queryData.get("preferType").equals("7")){ // 产品类型渠道服务偏好
			storeName= CommonUtils.RPT_CS_CHANNEL_CUST_TERM_VIEW;//获取存储过程
		}
		if(queryData.get("preferType").equals("8")){
			storeName= CommonUtils.RPT_CS_CHANNEL_MARKET_VIEW;//获取存储过程
		}
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
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
		// System.out.println(mapList.size());
		return mapList;
		
	}
	
	
	
	/***
	 *   关键指标
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> queryKeyAll_pg(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName="";
		
		if(queryData.get("reportType").equals("1001")){
			storeName="RPT_CS_KEY_REPORT_CUST";
		}
		if(queryData.get("reportType").equals("1002")){
			storeName="RPT_CS_KEY_REPORT_CUST_SERVER1";
		}		
		if(queryData.get("reportType").equals("1003")){
			storeName="RPT_CS_KEY_REPORT_LW_VIEW";
		}
		if(queryData.get("reportType").equals("1004")){
			storeName="RPT_CS_KEY_REPORT_KD_VIEW";
		}		
		if(queryData.get("reportType").equals("1005")){
			storeName="RPT_CS_KEY_REPORT_CUST_BY_VIEW";
		}
		if(queryData.get("reportType").equals("1006")){
			storeName="RPT_CS_KEY_REPORT_WH_XMT_VIEW";
		}
		Object[] params = {
				MapUtils.getString(queryData, "month", null).replaceAll("-", ""),
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
		// System.out.println(mapList.size());
		return mapList;
		
	}
	
	public Map<String, Object> queryKeyAll_pg1(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName="";
		
		if(queryData.get("reportType").equals("1001")){
			storeName="RPT_CS_KEY_REPORT_CUST";
		}
		if(queryData.get("reportType").equals("1002")){
			storeName="RPT_CS_KEY_REPORT_CUST_SERVER1";
		}		
		if(queryData.get("reportType").equals("3")){
			storeName="RPT_CS_KEY_REPORT_CUST_MANAGE";
		}
		if(queryData.get("reportType").equals("4")){
			storeName="RPT_CS_KEY_KD_CUST_MANAGE";
		}		
		if(queryData.get("reportType").equals("5")){
			storeName="RPT_CS_KEY_ZGD_CUST_MANAGE";
		}
		if(queryData.get("reportType").equals("6")){
			storeName="RPT_CS_KEY_WH_CUST_MANAGE";
		}
		Object[] params = {
				MapUtils.getString(queryData, "month", null).replaceAll("-", ""),
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
		// System.out.println(mapList.size());
		return mapList;
		
	}
	
	/****
	 * 获取首次加载的日期
	 * @param vDateType
	 * @param tabStr
	 * @return
	 */
	public String getMaxDate_ChannelServ(String vDateType,String tabStr){
		
		StringBuffer buffer = new StringBuffer();
		if(vDateType.equals("0")){//天
			buffer.append("select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual");
		}
		if(vDateType.equals("1")){//周
			buffer.append("select GET_MAX_PARTITION_DATE('"+tabStr+"',17) from dual");
		}
		if(vDateType.equals("2")){//月
			buffer.append("select GET_MAX_PARTITION_DATE('"+tabStr+"',6) from dual");
		}		
		return getDataAccess().queryForString(buffer.toString());
	}	
	
	public String getMaxDate_ChannelServ_new(String dateName,String tableName) {
		String sql = "select max("+dateName+") as DATE_NO from "+tableName+" order by "+dateName+" desc";
		return getDataAccess().queryForString(sql);
	}
	
	/***
	 * 查询渠道服务新类型 树
	 * @param beginId
	 * @param endId
	 * @return
	 */
    public List<Map<String,Object>> queryChannelTypePathPart(String beginId, String endId){	
  	   String sql = "SELECT A.CHANNEL_TYPE_CODE,A.CHANNEL_TYPE_PAR_CODE,"
  	        +" A.CHANNEL_TYPE_NAME,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
  	        +" FROM META_DM.DM_CHANNEL_TYPE   A  "
  	        +" LEFT JOIN (SELECT CHANNEL_TYPE_PAR_CODE, COUNT(1) CNT from META_DM.DM_CHANNEL_TYPE A "
  	         +" GROUP BY CHANNEL_TYPE_PAR_CODE) C"
  	        +" ON A.CHANNEL_TYPE_CODE = C.CHANNEL_TYPE_PAR_CODE"
  	        + " WHERE A.CHANNEL_TYPE_CODE = ? ";
  	
 	    Object[] proParams ={endId};
 	   return getDataAccess().queryForList(sql, proParams);
     }
    /**
     * 动态加载渠道服务的树
     * @param parentCode
     * @return
     */
    public List<Map<String,Object>> querySubChannelTypePart_new(String parentCode){
        String select = "SELECT A.CHANNEL_TYPE_CODE,A.CHANNEL_TYPE_PAR_CODE,"
        +" A.CHANNEL_TYPE_NAME,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
        +" FROM META_DM.DM_CHANNEL_TYPE A"
        +" LEFT JOIN (SELECT CHANNEL_TYPE_PAR_CODE, COUNT(1) CNT from META_DM.DM_CHANNEL_TYPE   "  
        +" GROUP BY CHANNEL_TYPE_PAR_CODE) C"
        +" ON A.CHANNEL_TYPE_CODE = C.CHANNEL_TYPE_PAR_CODE"
        +" WHERE A.STATE=1 and A.CHANNEL_TYPE_PAR_CODE = ? ORDER BY to_number(A.ORDER_ID)";
        	
	    Object[] proParams = {parentCode};
        return getDataAccess().queryForList(select, proParams);
    }
    
    public List<Map<String,Object>> querySubChannelTypePart_new2(String parentCode){
        String select = "SELECT distinct A.CHANNEL_TYPE_CODE_NEW CHANNEL_TYPE_CODE,A.CHANNEL_TYPE_PAR_CODE,"
        +" A.CHANNEL_TYPE_NAME_NEW CHANNEL_TYPE_NAME,A.DIM_LEVEL,A.ORDER_ID_NEW ORDER_ID,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
        +" FROM META_DM.DM_CHANNEL_TYPE A"
        +" LEFT JOIN (SELECT CHANNEL_TYPE_PAR_CODE, COUNT(1) CNT from META_DM.DM_CHANNEL_TYPE   "  
        +" GROUP BY CHANNEL_TYPE_PAR_CODE) C"
        +" ON A.CHANNEL_TYPE_CODE_NEW = C.CHANNEL_TYPE_PAR_CODE"
        +" WHERE A.STATE=1 and A.CHANNEL_TYPE_PAR_CODE = ? ORDER BY to_number(A.ORDER_ID_NEW)";
        	
	    Object[] proParams = {parentCode};
        return getDataAccess().queryForList(select, proParams);
    }
	
  //渠道分类_日、月、周存储过程(一级)
	public Map<String, Object> getChannelSerNewFirst_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CS_CHANNEL_SER_NEW_FIRST;
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
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
	//渠道偏好
	public Map<String, Object> getChannelPreferIndex_Pg(Map<String, Object> queryData) {
		String rptId=MapUtils.getString(queryData, "rptId", null);
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName="";
		if(rptId=="1"||"1".equals(rptId)){
			storeName=ConstantStoreProc.RPT_CHANNEL_PREFER_10000;
		}else if(rptId=="2"||"2".equals(rptId)){
			storeName=ConstantStoreProc.RPT_CHANNEL_PREFER_ZYT;
		}else if(rptId=="3"||"3".equals(rptId)){
			storeName=ConstantStoreProc.RPT_CHANNEL_PREFER_DT;
		}
	    
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
	  //渠道分类_日、月、周存储过程(一，二，三级)
	public Map<String, Object> getChannelSer_Pg(Map<String, Object> queryData) {
		String storeName="";
		String headerLevel=MapUtils.getString(queryData, "headerLevel", null);
		CallableStatement statement=null;
		int len = 0;
		String sql = null;
		//Object[] params=
		if(headerLevel.equals("1")){
			storeName=ConstantStoreProc.RPT_CS_CHANNEL_SER_FIRST_NEW;	
		}else if(headerLevel.equals("2")){
			storeName=ConstantStoreProc.RPT_CS_CHANNEL_SER_SECOND_NEW;//RPT_CS_CHANNEL_SER_SECOND_NEW	
		}else{
			storeName=ConstantStoreProc.RPT_CS_CHANNEL_SER_THRID_NEW;
		}
		
		Map<String, Object> mapList =new  HashMap<String, Object>();
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		
		Object[] params2 = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "thirdType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		
		
		
		ResultSet rs = null;
		try {
			if("2".equals(headerLevel)||"1".equals(headerLevel)){
				len = params.length;
				sql = convertStore(storeName, len);
				statement = getDataAccess().execQueryCall(sql,params);
			}
			else{
				len = params2.length;
				sql = convertStore(storeName, len);
			statement = getDataAccess().execQueryCall(sql,params2);
			}
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

	
	//渠道接接触数
	public Map<String, Object> getChannelPreferSummary_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CHANNEL_PREFER_SUMMARY;
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
	//渠道接接触数
	public Map<String, Object> getChannelPreferChoose_Pg(Map<String, Object> queryData,Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CHANNEL_PREFER_CHOOSE;
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelId", null),
				page.getStartRow(),
				page.getEndRow(),
				new DBOutParameter(OracleTypes.INTEGER),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			mapList.put("allPageCount", statement.getInt(len-1));
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

	  //渠道分类_日、月、周存储过程(一，二，三级)
	public Map<String, Object> getChannelSerChart_Pg(Map<String, Object> queryData) {
		String storeName="";
		String headerLevel=MapUtils.getString(queryData, "headerLevel", null);
		int len = 0;
		String sql = null;
		CallableStatement statement=null;
		if(headerLevel.equals("1")){
			storeName=ConstantStoreProc.RPT_CS_CHANNEL_SER_FIRST_SUM;
		}else if(headerLevel.equals("2")){
			storeName=ConstantStoreProc.RPT_CS_CHANNEL_SER_SECOND_SUM;//RPT_CS_CHANNEL_SER_SECOND_NEW
		}else{
			storeName=ConstantStoreProc.RPT_CS_CHANNEL_SER_THRID_SUM;
		}
		
		Map<String, Object> mapList =new  HashMap<String, Object>();
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		
		Object[] params2 = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "thirdType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		
		

		ResultSet rs = null;
		try {
			if("2".equals(headerLevel)||"1".equals(headerLevel)){
				len = params.length;
				sql = convertStore(storeName, len);
				statement = getDataAccess().execQueryCall(sql,params);
			}
			else{
				len = params2.length;
				sql = convertStore(storeName, len);
				statement = getDataAccess().execQueryCall(sql,params2);
			}
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
	
	  //按区域统计渠道分类_日、月、周存储过程(一，二，三级)
	public Map<String, Object> getAreaServerSum_Pg(Map<String, Object> queryData) {
		String storeName="";
		String headerLevel=MapUtils.getString(queryData, "headerLevel", null);
		int len = 0;
		String sql = null;
		CallableStatement statement=null;
		if(headerLevel.equals("1")){
			storeName=ConstantStoreProc.RPT_CS_CHANNEL_ONE_PRO_NEW;
		}else if(headerLevel.equals("2")){
			storeName=ConstantStoreProc.RPT_CS_CHANNEL_SECOND_PRO_NEW;//RPT_CS_CHANNEL_SER_SECOND_NEW
		}else{
			storeName=ConstantStoreProc.RPT_CS_CHANNEL_THRID_PRO_NEW;
		}
		
		Map<String, Object> mapList =new  HashMap<String, Object>();
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		
		Object[] params2 = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "thirdType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		
		

		ResultSet rs = null;
		try {
			if("2".equals(headerLevel)||"1".equals(headerLevel)){
				len = params.length;
				sql = convertStore(storeName, len);
				statement = getDataAccess().execQueryCall(sql,params);
			}
			else{
				len = params2.length;
				sql = convertStore(storeName, len);
				statement = getDataAccess().execQueryCall(sql,params2);
			}
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
	 * 新渠道服务 二级报表
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getChannelSerNewSecond_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CS_CHANNEL_SER_NEW_SECOND;
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
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
	 *  新渠道服务三级查询 、办理 等报表展现
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getChannelSerNewThird_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CS_CHANNEL_SER_NEW_THRID;
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "thirdType", null),
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
	 * 集团模板数据 存储过程的调用
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getGroupChannelReport_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.PRT_CS_GROUP_CHANNEL_VIEW;
		Object[] params = {
				MapUtils.getString(queryData, "month", null),
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
	 * 查询 集团新口径-集团上报监测月报
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getGroupChannelSc_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.PRT_CS_GROUP_REPORT_MONTH;
		Object[] params = {
				MapUtils.getString(queryData, "month", null),
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
	 * 查询 集团新口径-集团上报清单
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getGroupChannelList_Pg(Map<String, Object> queryData, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.PRT_CS_GROUP_REPORT_LIST_MONTH;
		Object[] params = {
				MapUtils.getString(queryData, "month", null),
			    page.getStartRow(),
				page.getEndRow(),
				new DBOutParameter(OracleTypes.INTEGER),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			mapList.put("allPageCount", statement.getInt(len-1));
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
	
	/****
	 *  渠道服务 新 清单存储过程的展现
	 * @param map
	 * @param page
	 * @return
	 */
	public Map<String, Object> getChannelNewIndexTableData(Map<String, Object> map, Pager page) {
		String storeName="";
		String secondId=MapUtils.getString(map, "secondId", null);
		
		if("1".equals(secondId)){
			storeName=ConstantStoreProc.RPT_CHANNEL_NEW_INDEX_DETAIL2;	
		}else{
			storeName=ConstantStoreProc.RPT_CHANNEL_NEW_INDEX_DETAIL;
		}
		Map<String, Object> mapList =new  HashMap<String, Object>();
		Object[] params = {
				MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
				MapUtils.getString(map, "zoneCode",  null),
				MapUtils.getString(map, "channelId", null),
				MapUtils.getString(map, "servId",  null),
			    page.getStartRow(),
				page.getEndRow(),
				new DBOutParameter(OracleTypes.INTEGER),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			mapList.put("allPageCount", statement.getInt(len-1));
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
	 public  List<Map<String, Object>> getChannelSerLineData(String startDate,String endDate, Map<String,Object> map) {
		 String channelTypeCode = Convert.toString(map.get("channelTypeCode"));
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String dateType=Convert.toString(map.get("dateType"));
		 StringBuffer sql = new StringBuffer("select * from (select * from meta_dim_datetype where date_id >= '"+startDate+"' and date_id <= '"+endDate+"' " +
		    " and date_type = '"+dateType+"') f left join(" +
		    "select V_DATE,sum(cnt_num)SERVICE_NUM,sum(case when channel_serv_id like '60%' then cnt_num end)PAY_NUM," +
      		"sum(case when channel_serv_id like '10%' then cnt_num end)QUERY_NUM,sum(case when channel_serv_id like '30%' then cnt_num end)DEAL_NUM," +
      		"sum(case when channel_serv_id like '20%' then cnt_num end)CONSULT_NUM,sum(case when channel_serv_id like '40%' then cnt_num end)COMPLAIN_NUM," +
      		"sum(case when channel_serv_id like '50%' then cnt_num end)FAULT_NUM,sum(case when channel_serv_id like '99%' then cnt_num end)OTHER_NUM" 
      		+" from CS_CHANNEL_VIEW_LOC_NEW a,(SELECT ORDER_ID, ZONE_CODE FROM META_DIM_ZONE D WHERE D.TREECODE LIKE GETTREECODE('"+zoneCode+"')||'%' ) B,"
            +"(select CHANNEL_TYPE_CODE_NEW from meta_dm.dm_channel_type start with CHANNEL_TYPE_CODE_NEW ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE_NEW = CHANNEL_TYPE_PAR_CODE)c ");
	     sql.append("where a.CHANNEL_ID=CHANNEL_TYPE_CODE_NEW and date_type='"+dateType+"' and A.REGION_ID = B.ZONE_CODE  "+"and V_DATE>='"+startDate+"'" +"and V_DATE<='"+endDate+"'");
	     sql.append(" group by V_DATE)g on f.date_id=g.v_date order by date_id ");
		 return getDataAccess().queryForList(sql.toString());
	 }	
	 //按渠道id获取时间列上的数据
	 public  List<Map<String, Object>> getChannelSerListData(String startDate,String endDate,String channelId,Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String dateType=Convert.toString(map.get("dateType"));
		 StringBuffer sql = new StringBuffer("select * from (select * from meta_dim_datetype_new where date_id >= '"+startDate+"' and date_id <= '"+endDate+"' " +
		    " and date_type = '"+dateType+"') f left join(" +
		    "select V_DATE,sum(cnt_num)SERVICE_NUM,sum(case when channel_serv_id like '60%' then cnt_num end)PAY_NUM," +
      		"sum(case when channel_serv_id like '10%' then cnt_num end)QUERY_NUM,sum(case when channel_serv_id like '30%' then cnt_num end)DEAL_NUM," +
      		"sum(case when channel_serv_id like '20%' then cnt_num end)CONSULT_NUM,sum(case when channel_serv_id like '40%' then cnt_num end)COMPLAIN_NUM," +
      		"sum(case when channel_serv_id like '50%' then cnt_num end)FAULT_NUM,sum(case when channel_serv_id like '99%' then cnt_num end)OTHER_NUM " +
      		/*"sum(case when channel_serv_id like '1001%' then cnt_num end)QUERY_NUM21,sum(case when channel_serv_id like '1002%' then cnt_num end)QUERY_NUM22," +
      		"sum(case when channel_serv_id like '1003%' then cnt_num end)QUERY_NUM23,sum(case when channel_serv_id like '1004%' then cnt_num end)QUERY_NUM24," +
      		"sum(case when channel_serv_id like '1005%' then cnt_num end)QUERY_NUM25,sum(case when channel_serv_id like '2011%' then cnt_num end)PAY_NUM211," +
      		"sum(case when channel_serv_id like '2012%' then cnt_num end)PAY_NUM212,sum(case when channel_serv_id like '3001%' then cnt_num end)DEAL_NUM21," +
      		"sum(case when channel_serv_id like '3002%' then cnt_num end)DEAL_NUM22,sum(case when channel_serv_id like '3003%' then cnt_num end)DEAL_NUM23," +
      		"sum(case when channel_serv_id like '3004%' then cnt_num end)DEAL_NUM24,sum(case when channel_serv_id like '3005%' then cnt_num end)DEAL_NUM25," +
      		"sum(case when channel_serv_id like '3006%' then cnt_num end)DEAL_NUM26,sum(case when channel_serv_id like '3007%' then cnt_num end)DEAL_NUM27," +
      		"sum(case when channel_serv_id like '3008%' then cnt_num end)DEAL_NUM28,sum(case when channel_serv_id like '3009%' then cnt_num end)DEAL_NUM29," +
      		"sum(case when channel_serv_id like '4001%' then cnt_num end)CONSULT_NUM21,sum(case when channel_serv_id like '4002%' then cnt_num end)CONSULT_NUM22," +
      		"sum(case when channel_serv_id like '4003%' then cnt_num end)CONSULT_NUM23,sum(case when channel_serv_id like '4004%' then cnt_num end)CONSULT_NUM24," +
      		"sum(case when channel_serv_id like '4005%' then cnt_num end)CONSULT_NUM25,sum(case when channel_serv_id like '4006%' then cnt_num end)CONSULT_NUM26," +
      		"sum(case when channel_serv_id like '4007%' then cnt_num end)CONSULT_NUM27,sum(case when channel_serv_id like '4008%' then cnt_num end)CONSULT_NUM28," +
      		"sum(case when channel_serv_id like '5001%' then cnt_num end)FAULT_NUM21,sum(case when channel_serv_id like '5002%' then cnt_num end)FAULT_NUM22," +
      		"sum(case when channel_serv_id like '5003%' then cnt_num end)FAULT_NUM23,sum(case when channel_serv_id like '5004%' then cnt_num end)FAULT_NUM24," +
      		"sum(case when channel_serv_id like '10010001%' then cnt_num end)QUERY_NUM211,sum(case when channel_serv_id like '10010002%' then cnt_num end)QUERY_NUM212," +
      		"sum(case when channel_serv_id like '10010003%' then cnt_num end)QUERY_NUM213,sum(case when channel_serv_id like '10010004%' then cnt_num end)QUERY_NUM214," +
      		"sum(case when channel_serv_id like '10010005%' then cnt_num end)QUERY_NUM215,sum(case when channel_serv_id like '10010006%' then cnt_num end)QUERY_NUM216," +
      		"sum(case when channel_serv_id like '10020001%' then cnt_num end)QUERY_NUM221,sum(case when channel_serv_id like '10020002%' then cnt_num end)QUERY_NUM222," +
      		"sum(case when channel_serv_id like '10020003%' then cnt_num end)QUERY_NUM223,sum(case when channel_serv_id like '10030001%' then cnt_num end)QUERY_NUM231," +
      		"sum(case when channel_serv_id like '10030002%' then cnt_num end)QUERY_NUM232,sum(case when channel_serv_id like '10030003%' then cnt_num end)QUERY_NUM233," +
      		"sum(case when channel_serv_id like '10030004%' then cnt_num end)QUERY_NUM234,sum(case when channel_serv_id like '10030005%' then cnt_num end)QUERY_NUM235," +
      		"sum(case when channel_serv_id like '10030006%' then cnt_num end)QUERY_NUM236,sum(case when channel_serv_id like '10030007%' then cnt_num end)QUERY_NUM237," +
      		"sum(case when channel_serv_id like '10030008%' then cnt_num end)QUERY_NUM238,sum(case when channel_serv_id like '10040001%' then cnt_num end)QUERY_NUM241," +
      		"sum(case when channel_serv_id like '10040002%' then cnt_num end)QUERY_NUM242,sum(case when channel_serv_id like '10040003%' then cnt_num end)QUERY_NUM243," +
      		"sum(case when channel_serv_id like '10040004%' then cnt_num end)QUERY_NUM244,sum(case when channel_serv_id like '10040005%' then cnt_num end)QUERY_NUM245," +
      		"sum(case when channel_serv_id like '20110001%' then cnt_num end)PAY_NUM2111,sum(case when channel_serv_id like '20110002%' then cnt_num end)PAY_NUM2112," +
      		"sum(case when channel_serv_id like '20110003%' then cnt_num end)PAY_NUM2113,sum(case when channel_serv_id like '20110004%' then cnt_num end)PAY_NUM2114," +
      		"sum(case when channel_serv_id like '20110005%' then cnt_num end)PAY_NUM2115,sum(case when channel_serv_id like '20110006%' then cnt_num end)PAY_NUM2116," +
      		"sum(case when channel_serv_id like '20120001%' then cnt_num end)PAY_NUM2121,sum(case when channel_serv_id like '20120002%' then cnt_num end)PAY_NUM2122," +
      		"sum(case when channel_serv_id like '20120003%' then cnt_num end)PAY_NUM2123,sum(case when channel_serv_id like '20120004%' then cnt_num end)PAY_NUM2124," +
      		"sum(case when channel_serv_id like '20120005%' then cnt_num end)PAY_NUM2125,sum(case when channel_serv_id like '30010001%' then cnt_num end)DEAL_NUM211," +
      		"sum(case when channel_serv_id like '30010002%' then cnt_num end)DEAL_NUM212,sum(case when channel_serv_id like '30020001%' then cnt_num end)DEAL_NUM221," +
      		"sum(case when channel_serv_id like '30020002%' then cnt_num end)DEAL_NUM222,sum(case when channel_serv_id like '30020003%' then cnt_num end)DEAL_NUM223," +
      		"sum(case when channel_serv_id like '30020004%' then cnt_num end)DEAL_NUM224,sum(case when channel_serv_id like '30030001%' then cnt_num end)DEAL_NUM231," +
      		"sum(case when channel_serv_id like '30030002%' then cnt_num end)DEAL_NUM232,sum(case when channel_serv_id like '30030003%' then cnt_num end)DEAL_NUM233," +
      		"sum(case when channel_serv_id like '30030004%' then cnt_num end)DEAL_NUM234,sum(case when channel_serv_id like '30030005%' then cnt_num end)DEAL_NUM235," +
      		"sum(case when channel_serv_id like '30040001%' then cnt_num end)DEAL_NUM241,sum(case when channel_serv_id like '30040002%' then cnt_num end)DEAL_NUM242," +
      		"sum(case when channel_serv_id like '30050001%' then cnt_num end)DEAL_NUM251,sum(case when channel_serv_id like '30050002%' then cnt_num end)DEAL_NUM252," +
      		"sum(case when channel_serv_id like '30050003%' then cnt_num end)DEAL_NUM253,sum(case when channel_serv_id like '30050004%' then cnt_num end)DEAL_NUM254," +
      		"sum(case when channel_serv_id like '30050005%' then cnt_num end)DEAL_NUM255,sum(case when channel_serv_id like '30050006%' then cnt_num end)DEAL_NUM256," +
      		"sum(case when channel_serv_id like '30050007%' then cnt_num end)DEAL_NUM257,sum(case when channel_serv_id like '30060001%' then cnt_num end)DEAL_NUM261," +
      		"sum(case when channel_serv_id like '30060002%' then cnt_num end)DEAL_NUM262,sum(case when channel_serv_id like '30060003%' then cnt_num end)DEAL_NUM263," +
      		"sum(case when channel_serv_id like '30070001%' then cnt_num end)DEAL_NUM271,sum(case when channel_serv_id like '30070002%' then cnt_num end)DEAL_NUM272," +
      		"sum(case when channel_serv_id like '30070003%' then cnt_num end)DEAL_NUM273,sum(case when channel_serv_id like '30080001%' then cnt_num end)DEAL_NUM281," +
      		"sum(case when channel_serv_id like '30080002%' then cnt_num end)DEAL_NUM282,sum(case when channel_serv_id like '30080003%' then cnt_num end)DEAL_NUM283," +
      		"sum(case when channel_serv_id like '30080004%' then cnt_num end)DEAL_NUM284,sum(case when channel_serv_id like '30080005%' then cnt_num end)DEAL_NUM285," +
      		"sum(case when channel_serv_id like '30080006%' then cnt_num end)DEAL_NUM286,sum(case when channel_serv_id like '30080007%' then cnt_num end)DEAL_NUM287," +
      		"sum(case when channel_serv_id like '30080008%' then cnt_num end)DEAL_NUM288,sum(case when channel_serv_id like '30080009%' then cnt_num end)DEAL_NUM289," +
      		"sum(case when channel_serv_id like '30080010%' then cnt_num end)DEAL_NUM2810,sum(case when channel_serv_id like '30080011%' then cnt_num end)DEAL_NUM2811," +
      		"sum(case when channel_serv_id like '30080012%' then cnt_num end)DEAL_NUM2812,sum(case when channel_serv_id like '40020001%' then cnt_num end)CONSULT_NUM221," +
      		"sum(case when channel_serv_id like '40020002%' then cnt_num end)CONSULT_NUM222,sum(case when channel_serv_id like '40020003%' then cnt_num end)CONSULT_NUM223," +
      		"sum(case when channel_serv_id like '40020004%' then cnt_num end)CONSULT_NUM224,sum(case when channel_serv_id like '40020005%' then cnt_num end)CONSULT_NUM225," +
      		"sum(case when channel_serv_id like '40020006%' then cnt_num end)CONSULT_NUM226,sum(case when channel_serv_id like '40030001%' then cnt_num end)CONSULT_NUM231," +
      		"sum(case when channel_serv_id like '40030002%' then cnt_num end)CONSULT_NUM232,sum(case when channel_serv_id like '40030003%' then cnt_num end)CONSULT_NUM233," +
      		"sum(case when channel_serv_id like '40040001%' then cnt_num end)CONSULT_NUM241,sum(case when channel_serv_id like '40040002%' then cnt_num end)CONSULT_NUM242," +
      		"sum(case when channel_serv_id like '40040003%' then cnt_num end)CONSULT_NUM243,sum(case when channel_serv_id like '40040004%' then cnt_num end)CONSULT_NUM244," +
      		"sum(case when channel_serv_id like '40040005%' then cnt_num end)CONSULT_NUM245,sum(case when channel_serv_id like '40040006%' then cnt_num end)CONSULT_NUM246," +
      		"sum(case when channel_serv_id like '40050001%' then cnt_num end)CONSULT_NUM251,sum(case when channel_serv_id like '40050002%' then cnt_num end)CONSULT_NUM252," +
      		"sum(case when channel_serv_id like '40060001%' then cnt_num end)CONSULT_NUM261,sum(case when channel_serv_id like '40060002%' then cnt_num end)CONSULT_NUM262," +
      		"sum(case when channel_serv_id like '40060003%' then cnt_num end)CONSULT_NUM263,sum(case when channel_serv_id like '40070001%' then cnt_num end)CONSULT_NUM271," +
      		"sum(case when channel_serv_id like '40070002%' then cnt_num end)CONSULT_NUM272,sum(case when channel_serv_id like '40070003%' then cnt_num end)CONSULT_NUM273 "*/
      		" from CS_CHANNEL_VIEW_NEW_LOC a");
	     sql.append(" where a.CHANNEL_ID='"+channelId+"' and date_type='"+dateType+"' and a.region_TREECODE LIKE GETTREECODE('"+zoneCode+"') || '%'"+"and V_DATE>='"+startDate+"'" +"and V_DATE<='"+endDate+"'");
	     sql.append(" group by V_DATE)g on f.date_id=g.v_date order by date_id ");
		 return getDataAccess().queryForList(sql.toString());
	 }
	 
	 public  List<Map<String, Object>> getChannelSerBarData(String startDate,String endDate, Map<String,Object> map) {
		 String channelTypeCode = Convert.toString(map.get("channelTypeCode"));
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String dateType=Convert.toString(map.get("dateType"));
		 StringBuffer sql = new StringBuffer("select CHANNEL_TYPE_CODE_NEW,channel_type_name_new,sum(cnt_num)SERVICE_NUM,sum(case when channel_serv_id like '60%' then cnt_num end)PAY_NUM," +
      		"sum(case when channel_serv_id like '10%' then cnt_num end)QUERY_NUM,sum(case when channel_serv_id like '30%' then cnt_num end)DEAL_NUM," +
      		"sum(case when channel_serv_id like '20%' then cnt_num end)CONSULT_NUM,sum(case when channel_serv_id like '40%' then cnt_num end)COMPLAIN_NUM," +
      		"sum(case when channel_serv_id like '50%' then cnt_num end)FAULT_NUM,sum(case when channel_serv_id like '99%' then cnt_num end)OTHER_NUM" 
      		+" from CS_CHANNEL_VIEW_LOC_NEW a,(select * from meta_dim_zone_branch start with ZONE_CODE = '"+zoneCode+"' connect by prior ZONE_CODE = zone_par_code )b,"
            +"(select CHANNEL_TYPE_CODE_NEW,channel_type_name_new,order_id_new from meta_dm.dm_channel_type start with CHANNEL_TYPE_CODE_NEW ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE_NEW = CHANNEL_TYPE_PAR_CODE)c ");
	     sql.append("where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_ID=CHANNEL_TYPE_CODE_NEW and date_type='"+dateType+"' "+"and V_DATE='"+startDate+"'");
	     sql.append(" group by CHANNEL_TYPE_CODE_NEW,channel_type_name_new, order_id_new order by  to_number(c.order_id_new)");
		 return getDataAccess().queryForList(sql.toString());
	 }
	 public String getCurWeekDate(String dateTime){
		 String sql = "select GET_BELONG_PER_WEEK('"+dateTime+"') from dual";
			return getDataAccess().queryForString(sql);
	 }
	 public String getLastWeekDate(String dateTime){
		 String sql = "select GET_BELONG_PER_MONTH_WEEK('"+dateTime+"') from dual";
			return getDataAccess().queryForString(sql);
	 }
	 
	 /***
		 * 存储过程 移动后付费用户信用管理月报表
		 * @param queryData
		 * @return
		 */
		public Map<String, Object> getCreditManageMonthData(Map<String, Object> queryData){
			Map<String, Object> mapList =new  HashMap<String, Object>();
			
			String storeName= CommonUtils.MKF_YDXYGL_MONTH_PRO;//获取存储过程
			
			Object[] params = {
					//MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "zoneCode", null),
					MapUtils.getString(queryData, "dateType", null),
					//MapUtils.getString(queryData, "channelTypeCode", null),
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
		 * 存储过程 移动后付费用户信用投诉
		 * @param queryData
		 * @return
		 */
		public Map<String, Object> getCreditComplaintData(Map<String, Object> queryData, Pager page){
			Map<String, Object> mapList =new  HashMap<String, Object>();
			
			String storeName= CommonUtils.MKF_CreditComplaint_PRO;//获取存储过程
			
			Object[] params = {
					MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "zoneCode", null),					
					MapUtils.getString(queryData, "dateType", null),
					page.getStartRow(),
					page.getEndRow(),
					new DBOutParameter(OracleTypes.INTEGER),
					new DBOutParameter(OracleTypes.CURSOR)};
			int len = params.length;
			String sql = convertStore(storeName, len);
			ResultSet rs = null;
			try {
				CallableStatement statement = getDataAccess().execQueryCall(sql,params);
				mapList.put("allPageCount", statement.getInt(len-1));
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
		 * 存储过程 移动后付费用户信用管理宽表
		 * @param queryData
		 * @return
		 */
		public Map<String, Object> getCreditManageKBData(Map<String, Object> queryData, Pager page){
			Map<String, Object> mapList =new  HashMap<String, Object>();
			
			String storeName= CommonUtils.MKF_CreditManageKB_PRO;//获取存储过程
			
			Object[] params = {
					//MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "zoneCode", null),					
					MapUtils.getString(queryData, "dateType", null),
					page.getStartRow(),
					page.getEndRow(),
					new DBOutParameter(OracleTypes.INTEGER),
					new DBOutParameter(OracleTypes.CURSOR)};
			int len = params.length;
			String sql = convertStore(storeName, len);
			ResultSet rs = null;
			try {
				CallableStatement statement = getDataAccess().execQueryCall(sql,params);
				mapList.put("allPageCount", statement.getInt(len-1));
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
		 * 存储过程 移动后付费用户临时授信月报表
		 * @param queryData
		 * @return
		 */
		public Map<String, Object> getTemporaryCreditMonthData(Map<String, Object> queryData){
			Map<String, Object> mapList =new  HashMap<String, Object>();
			
			String storeName= CommonUtils.MKF_YDXYGL_LSSX_PRO;//获取存储过程
			
			Object[] params = {
					//MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "zoneCode", null),
					MapUtils.getString(queryData, "dateType", null),
					//MapUtils.getString(queryData, "channelTypeCode", null),
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
		 * 存储过程 移动后付费用户节假日无限信用效果监控报表
		 * @param queryData
		 * @return
		 */
		public Map<String, Object> getHolidayCreditResultData(Map<String, Object> queryData){
			Map<String, Object> mapList =new  HashMap<String, Object>();
			
			String storeName= CommonUtils.MKF_YDXYGL_HOLIDAYCREDIT_PRO;//获取存储过程
			
			Object[] params = {
					//MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "zoneCode", null),
					MapUtils.getString(queryData, "dateType", null),
					//MapUtils.getString(queryData, "channelTypeCode", null),
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
		 * 存储过程 移动后付费用户临时授信管理宽表
		 * @param queryData
		 * @return
		 */
		public Map<String, Object> getLSSXMonitorKBData(Map<String, Object> queryData, Pager page){
			Map<String, Object> mapList =new  HashMap<String, Object>();
			
			String storeName= CommonUtils.MKF_LSSX_MONITORKB_PRO;//获取存储过程
			
			Object[] params = {
					//MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
					MapUtils.getString(queryData, "zoneCode", null),					
					MapUtils.getString(queryData, "dateType", null),
					page.getStartRow(),
					page.getEndRow(),
					new DBOutParameter(OracleTypes.INTEGER),
					new DBOutParameter(OracleTypes.CURSOR)};
			int len = params.length;
			String sql = convertStore(storeName, len);
			ResultSet rs = null;
			try {
				CallableStatement statement = getDataAccess().execQueryCall(sql,params);
				mapList.put("allPageCount", statement.getInt(len-1));
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
		
		public long CreditManageKBDataCount(String month){
			  String recordSql="select count(sum_month) from META_USER.XX_MKF_YDXYGL_KB where sum_month='"+month+"'";
			  long recordCnt=getDataAccess().queryForLong(recordSql, null);			  
			  return recordCnt;
		}		
}
