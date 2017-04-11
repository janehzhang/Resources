package tydic.reports.complain.monitorDay;

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
import tydic.meta.common.yhd.utils.Pager;
import tydic.reports.CommonUtils.CommonUtils;

public class CmplIndexDAO extends MetaBaseDAO {
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
	
	//投诉指标日监测报表_指标项列表
	public List<Map<String, Object>> getIndListDay(String dateTime) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct IND_ID, IND_NAME from CS_CMPL_MONITOR_DAY_ZXD where day_id='"+dateTime+"'" +
				" order by to_number(case when ind_id='3' then '0' else ind_id end) ");
		return getDataAccess().queryForList(buffer.toString());
	}
	//投诉指标月监测报表_最大月
	public String getMaxMonth1(String tabStr) {
		String sql = " select GET_MAX_PARTITION_DATE('"+tabStr+"',6) from dual";
		return getDataAccess().queryForString(sql);
	}
	//投诉指标月监测报表_最大月
	public String getMaxMonth(String tabStr) {
		String sql = " select max(month_id) from "+tabStr;
		return getDataAccess().queryForString(sql);
	}
	//报表_最大月
	public String getMaxMonthView(String tabStr) {
		String sql = "select max(t.DATE_ID)MONTH_ID from DIM_ALL_DATE_ID t where TABLE_ID='"+tabStr+"'";
		return getDataAccess().queryForString(sql);
	}
	//投诉指标日监测报表_最大日
	public String getMaxDate1(String tabStr) {
		String sql = " select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		return getDataAccess().queryForString(sql);
	}
	//投诉指标日监测报表_最大日
	public String getMaxDate(String tabStr) {
		String sql = " select max(day_id) from "+tabStr;
		return getDataAccess().queryForString(sql);
	}
	//投诉指标月监测报表_指标项列表
	public List<Map<String, Object>> getIndListMon(String dateTime) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct IND_ID, IND_NAME from CS_CMPL_MONITOR_MON_ZXD where month_id='"+dateTime+"'" +
				" order by to_number(case when ind_id = '3' then '1' when ind_id = '1' then '2'  when ind_id = '2' then '3' when ind_id = '1' then '2' else ind_id end) ");
		return getDataAccess().queryForList(buffer.toString());
	}
	//投诉总体情况汇总月报表_指标项列表
	public List<Map<String, Object>> getIndListMon_Zone() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct IND_ID, IND_NAME from CS_CMPL_SUMMARY_MON_D where month_id='201306' " +
				"and ind_id not in('3','19')" +
				" order by to_number(CASE WHEN IND_ID = '18' THEN '1' WHEN IND_ID = '4' THEN '5' WHEN IND_ID = '22' THEN '6' " +
				"WHEN IND_ID = '21' THEN '7' WHEN IND_ID = '20' THEN '7.1' WHEN IND_ID = '15' THEN '8' WHEN IND_ID = '6' THEN '9' " +
				"WHEN IND_ID = '7' THEN '10' WHEN IND_ID = '16' THEN '11' WHEN IND_ID = '8' THEN '12' WHEN IND_ID = '9' THEN '13' " +
				"WHEN IND_ID = '10' THEN '14' WHEN IND_ID = '11' THEN '15' WHEN IND_ID = '12' THEN '16' WHEN IND_ID = '17' THEN '17' " +
				"WHEN IND_ID = '1' THEN '18' WHEN IND_ID = '19' THEN '3' WHEN IND_ID = '3' THEN '4' ELSE IND_ID END) ");
		return getDataAccess().queryForList(buffer.toString());
	}
	//投诉指标月监测报表_月份列表分区表
	public List<Map<String, Object>> getMonList1(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct substr(partition_name, 2) month_id from user_Segments " +
				"where segment_name = '"+tabStr+"' and length(substr(partition_name, 2)) = 6 order by month_id desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//投诉指标月监测报表_月份列表
	public List<Map<String, Object>> getMonList(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct  month_id from "+tabStr+" order by month_id desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//报表_月份列表
	public List<Map<String, Object>> getMonListView(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct DATE_ID as MONTH_ID from DIM_ALL_DATE_ID where TABLE_ID='"+tabStr+"'" +
				" order by DATE_ID desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//投诉现象分析月报表_投诉现象类型列表
	public List<Map<String, Object>> getBusinessList(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct CMPL_BUSINESS_TYPE1_ID,CMPL_BUSINESS_TYPE1_NAME from "+tabStr+" " +
				" where CMPL_BUSINESS_TYPE1_NAME is not null and CMPL_BUSINESS_TYPE1_ID<>'-999' order by to_number(CMPL_BUSINESS_TYPE1_ID)");
		return getDataAccess().queryForList(buffer.toString());
	}
	//投诉现象分析月报表_投诉渠道列表
	public List<Map<String, Object>> getChannelList(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct CHANNEL_TYPE_ID,CHANNEL_TYPE_NAME from "+tabStr+" " +
				" where CHANNEL_TYPE_NAME is not null and CHANNEL_TYPE_ID is not null and CHANNEL_TYPE_ID<>'-999' and CHANNEL_TYPE_NAME<> '-' order by to_number(CHANNEL_TYPE_ID) ");
		return getDataAccess().queryForList(buffer.toString());
	}
	//投诉原因分析月报表_投诉原因列表
	public List<Map<String, Object>> getReasonList(String tabStr,String date) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct CMPL_REASON1_ID,CMPL_REASON1_NAME from "+tabStr+" " +
				" where CMPL_REASON1_NAME is not null and month_id='"+date+"' order by to_number(CMPL_REASON1_ID)");
		return getDataAccess().queryForList(buffer.toString());
	}
	//投诉现象分析月报表_业务类型列表
	public List<Map<String, Object>> getProdList(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct prod_type_id,prod_type_name from "+tabStr+" " +
				" where prod_type_name is not null order by to_number(prod_type_id)");
		return getDataAccess().queryForList(buffer.toString());
	}
	//指标解释列表
	public List<Map<String, Object>> getIndexExplain(Map<String, Object> queryData){
		String rptId=MapUtils.getString(queryData, "rptId", null);
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct INDEX_ID,INDEX_NAME,INDEX_EXPLAIN from CS_CALIBER_EXPLAIN where" +
				" (SELECT ID_INCLUDE FROM CS_REPORT_CALIBER WHERE RPT_ID='"+rptId+"') like '%'''||INDEX_ID||'''%' " +
						" order by to_number(INDEX_ID)");
		return getDataAccess().queryForList(buffer.toString());
	}
	//投诉指标日监测报表_指标解释列表
	public List<Map<String, Object>> getCmplIndexExpDay(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct INDEX_CD,INDEX_NAME,INDEX_EXPLAIN from META_PORTAL_INDEX_EXPLAIN where tab_id='1' and index_cd not in ('004', '007')" +
				" order by case when index_cd = '008' then '001' when index_cd = '005' then '002'  when index_cd = '002' then '003' when index_cd = '001' then '004'" +
				" when index_cd = '003' then '005' else index_cd end ");
		return getDataAccess().queryForList(buffer.toString());
	}
	//投诉指标月监测报表_指标解释列表
	public List<Map<String, Object>> getCmplIndexExpMon(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct INDEX_CD,INDEX_NAME,INDEX_EXPLAIN from META_PORTAL_INDEX_EXPLAIN where tab_id<>'1'" +
				" and index_cd not in ('106', '107', '108', '109', '110', '112','113','114','115','116','118')" +
				" order by case when index_cd='117' then '101' " +
				" when index_cd='111' then '104' when index_cd='104' then '105'" +
				" when index_cd='105' then '106' when index_cd='119' then '107' when index_cd='120' then '108'" +
				" when index_cd='121' then '109' when index_cd='122' then '110' when index_cd='123' then '111'" +
				" when index_cd='101' then '112' else index_cd end ");
		return getDataAccess().queryForList(buffer.toString());
	}
	   //投诉类指标监测报表存储过程
	public Map<String, Object> getCmplIndex_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String rptIndex=MapUtils.getString(queryData, "rptIndex", null);
		String storeName="";
		if("cmplIndexDay".equals(rptIndex)){//投诉日监测
		    storeName = ConstantStoreProc.RPT_CMPL_MONIT_DAY;
		}if("cmplIndexMon".equals(rptIndex)){//投诉月监测
			storeName = ConstantStoreProc.RPT_CMPL_MONIT_MON;
		}
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "prodTypeCode", null),
				MapUtils.getString(queryData, "ind", null),
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
	 * 存储过程 投诉情况报表_日统计量
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getCmplDayData(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= ConstantStoreProc.RPT_LOCAL_CMPL_DAY;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),					
				MapUtils.getString(queryData, "prodTypeCode", null),
				MapUtils.getString(queryData, "cmplBusiTypeCode", null),
				MapUtils.getString(queryData, "indexType", null),
				MapUtils.getString(queryData, "dataType", null),
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
	 * 存储过程 投诉现象分析报表_日统计量
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getCmplPheDayData(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= ConstantStoreProc.RPT_LOCAL_CMPLPHE_DAY;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),					
				MapUtils.getString(queryData, "prodTypeCode", null),
				MapUtils.getString(queryData, "cmplBusiTypeCode", null),
				MapUtils.getString(queryData, "indexType", null),
				//MapUtils.getString(queryData, "dataType", null),
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
	 * 存储过程 投诉原因分析报表_日统计量
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getCmplReasonDayData(Map<String, Object> queryData){
		Map<String, Object> mapList =new  HashMap<String, Object>();
		
		String storeName= ConstantStoreProc.RPT_LOCAL_CMPLREASON_DAY;//获取存储过程
		
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),					
				MapUtils.getString(queryData, "reasonId", null),
				MapUtils.getString(queryData, "channelType", null),
				MapUtils.getString(queryData, "indexType", null),
				//MapUtils.getString(queryData, "dataType", null),
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
	
	
	   //投诉情况报表_存储过程
	public Map<String, Object> getCmplSum_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.P_COMMON_RPT_4J;
		Object[] params = {
				MapUtils.getString(queryData, "reportId", null),
				MapUtils.getString(queryData, "parameters", null),
				MapUtils.getString(queryData, "values", null),
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
	public Map<String, Object> getCmplSum_Pg_Dg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.P_COMMON_RPT_4J;
		Object[] params = {
				MapUtils.getString(queryData, "reportId", null),
				MapUtils.getString(queryData, "parameters", null),
				MapUtils.getString(queryData, "values", null),
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
   //投诉指标日监测报表_表格数据
	public List<Map<String, Object>> getCmplIndex_Day(Map<String, Object> queryData) {
	      String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String prodType ="".equals(Convert.toString(queryData.get("prodType")))?"-1":Convert.toString(queryData.get("prodType"));
	      String sql = "select " +
	      		" ORDER_ID,par_region_id, par_region_name,REGION_ID,region_name,IND_NAME,IND_ID,sum(NUM) current_num,sum(TOTAL_NUM) TOTAL_NUM,sum(TOTAL_NUM_LAST) TOTAL_NUM_LAST," +
	      		" DECODE(sum(TOTAL_NUM_LAST),0,0,round((sum(TOTAL_NUM)/sum(TOTAL_NUM_LAST)-1)*100,2))HB_NUM,DECODE(sum(TOTAL_NUM_LY_M),0,0,round((sum(TOTAL_NUM)/sum(TOTAL_NUM_LY_M)-1)*100,2))TB_NUM," +
	      		"round(sum(TOTAL_NUM)/to_number(substr(day_id,7,2)),2)AVG_NUM" +
	            " from CS_CMPL_MONITOR_DAY a, (select ORDER_ID,zone_code from META_DIM_ZONE start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," +
	            "(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c" +
	            " where a.REGION_ID = b.ZONE_CODE and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.DAY_ID='"+dateTime+"'";
          sql+=" group by (ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.IND_NAME,a.IND_ID,a.DAY_ID) ";
          sql+=" order by a.PAR_REGION_ID,ORDER_ID,a.REGION_ID,to_number(a.ind_id)";
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	   //投诉指标月监测报表_表格数据
	public List<Map<String, Object>> getCmplIndex_Mon(Map<String, Object> queryData) {
	      String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String prodType ="".equals(Convert.toString(queryData.get("prodType")))?"-1":Convert.toString(queryData.get("prodType"));
	      StringBuffer sql = new StringBuffer("select PAR_REGION_NAME,PAR_REGION_ID,REGION_NAME,region_id,IND_NAME,IND_ID," +
	      		"case when IND_ID IN ( 5, 6, 7, 10, 11) then to_char(current_value * 100, 'FM990.00') || '%'" +
	      		" when ind_id = 9 then to_char(round(current_value))" +
	      		" when IND_ID IN (3, 4, 12) then to_char(current_value * 1000, 'FM990.00') || '‰'" +
	      		" when ind_id in (1, 2, 8) then to_char(current_value * 1000000, 'FM990.00') end current_value," +
	      		"case when IND_ID IN (5, 6, 7, 10, 11) then to_char(lastmon_value * 100, 'FM990.00') || '%'" +
	      		" when ind_id = 9 then to_char(round(lastmon_value))" +
	      		" when IND_ID IN (3, 4, 12) then to_char(lastmon_value * 1000, 'FM990.00') || '‰' " +
	      		" when ind_id in (1, 2, 8) then to_char(lastmon_value * 1000000, 'FM990.00') end lastmon_value," +
	      		"case when IND_ID IN ( 5, 6, 7, 10, 11) then to_char(avg_value * 100, 'FM990.00') || '%'" +
	      		" when ind_id = 9 then to_char(round(avg_value))" +
	      		" when IND_ID IN (3, 4, 12) then  to_char(avg_value * 1000, 'FM990.00') || '‰' " +
	      		" when ind_id in (1, 2, 8) then to_char(avg_value * 1000000, 'FM990.00') end avg_value," +
	      		"to_char(round(decode(lastyear_value,0,0,current_value / lastyear_value - 1) * 100,2),'FM9999990.00') || '%' TB," +
	      		"to_char(round(decode(lastmon_value,0,0,current_value / lastmon_value - 1) * 100,2),'FM9999990.00') || '%' HB, " +
	      		"month_id from (select a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.IND_NAME,a.IND_ID," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then  decode(max(num2), 0, 0, sum(num1) / max(num2))" +
	      		" when ind_id in (9) then sum(num1)" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(num2), 0, 0, sum(num1) / sum(num2)) end current_value," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(num2_last),0,0,sum(num1_last) / max(num2_last))" +
	      		" when ind_id in (9) then sum(num1_last)" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(num2_last),0,0,sum(num1_last) / sum(num2_last)) end lastmon_value," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(num2_ly_m),0,0,sum(num1_ly_m) / max(num2_ly_m))" +
	      		" when ind_id in (9) then sum(num1_ly_m)" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(num2_ly_m),0,0,sum(num1_ly_m) / sum(num2_ly_m)) end lastyear_value," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(total_num2),0,0,sum(total_num1) / max(total_num2))" +
	      		" when ind_id in (9) then sum(total_num1) / to_number(substr(month_id, 5, 2))" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(total_num2),0,0,sum(total_num1) / sum(total_num2)) end avg_value," +
	      		"a.month_id,dim_level,ORDER_ID,to_number(substr(month_id, 5, 2)) from CS_CMPL_MONITOR_MON_ZXD a," +
	      		"(select ORDER_ID, zone_code, dim_level from META_DIM_ZONE start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code) b," +
	      		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE = '"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c" +
	      		" where a.REGION_ID = b.ZONE_CODE and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.month_id = '"+dateTime+"'" +
	      		" group by (ORDER_ID, a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME, a.region_id, a.IND_NAME, a.IND_ID,a.month_id, dim_level)) t" +
	      		" order by dim_level, ORDER_ID, REGION_ID, to_number(ind_id)");
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	//投诉指标日监测报表取上月数据/折线图
	public  Map<String,Object> getChartData_CmplIndexDay(String dateTime, Map<String,Object> map) {
	    String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
	   // String prodType ="".equals(Convert.toString(map.get("prodTypeCode")))?"-1":Convert.toString(map.get("prodTypeCode"));
	    String ind="".equals(Convert.toString(map.get("ind")))?"1":Convert.toString(map.get("ind"));//默认显示工信部申诉量
		String sql = "select sum(NUM)current_num,sum(TOTAL_NUM) TOTAL_NUM" 
			  + " from CS_CMPL_MONITOR_DAY_ZXD a " + //,(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='" +
				//prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c 
					"where  a.region_id='"+ zoneCode+"'"; //a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and
         sql+=" and a.DAY_ID='"+dateTime+"' and a.ind_id='"+ind+"' ";
	  return getDataAccess().queryForMap(sql.toString());
  }  
	//投诉指标月监测报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData_CmplIndexMon(String dateTime, Map<String,Object> map) {
	    String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
	    String ind="".equals(Convert.toString(map.get("ind")))?"103":Convert.toString(map.get("ind"));//默认显示越级申诉率
		StringBuffer sql = new StringBuffer(
				"select " +
				"case when IND_ID IN ( 5, 6, 7, 10, 11) then to_char(current_value * 100, 'FM99999990.00')" +
	      		" when ind_id = 9 then to_char(round(current_value))" +
	      		" when IND_ID IN (3, 4, 12) then to_char(current_value * 1000, 'FM99999990.00')" +
	      		" when ind_id in (1, 2, 8) then to_char(current_value * 1000000, 'FM99999990.00') end current_value," +
	      		/*"case when IND_ID IN (5, 6, 7, 10, 11) then to_char(lastmon_value * 100, 'FM990.00')" +
	      		" when ind_id = 9 then to_char(round(lastmon_value))" +
	      		" when IND_ID IN (3, 4, 12) then to_char(lastmon_value * 1000, 'FM990.00') " +
	      		" when ind_id in (1, 2, 8) then to_char(lastmon_value * 1000000, 'FM990.00') end lastmon_value," +
	      		"case when IND_ID IN ( 5, 6, 7, 10, 11) then to_char(avg_value * 100, 'FM990.00')" +
	      		" when ind_id = 9 then to_char(round(avg_value))" +
	      		" when IND_ID IN (3, 4, 12) then  to_char(avg_value * 1000, 'FM990.00')" +
	      		" when ind_id in (1, 2, 8) then to_char(avg_value * 1000000, 'FM990.00') end avg_value," +
	      		"to_char(round(decode(lastyear_value,0,0,current_value / lastyear_value - 1) * 100,2),'FM990.00') TB," +
	      		"to_char(round(decode(lastmon_value,0,0,current_value / lastmon_value - 1) * 100,2),'FM990.00') HB, " +*/
	      		"month_id from (select ind_id," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then  decode(max(num2), 0, 0, sum(num1) / max(num2))" +
	      		" when ind_id in (9) then sum(num1)" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(num2), 0, 0, sum(num1) / sum(num2)) end current_value," +
	      		/*"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(num2_last),0,0,sum(num1_last) / max(num2_last))" +
	      		" when ind_id in (9) then sum(num1_last)" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(num2_last),0,0,sum(num1_last) / sum(num2_last)) end lastmon_value," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(num2_ly_m),0,0,sum(num1_ly_m) / max(num2_ly_m))" +
	      		" when ind_id in (9) then sum(num1_ly_m)" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(num2_ly_m),0,0,sum(num1_ly_m) / sum(num2_ly_m)) end lastyear_value," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(total_num2),0,0,sum(total_num1) / max(total_num2))" +
	      		" when ind_id in (9) then sum(total_num1) / to_number(substr(month_id, 5, 2))" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(total_num2),0,0,sum(total_num1) / sum(total_num2)) end avg_value," +*/
	      		"a.month_id,to_number(substr(month_id, 5, 2)) from CS_CMPL_MONITOR_MON_ZXD a "
			  + "where a.region_id='"+ zoneCode+"' and a.month_id = '"+dateTime+"' and a.ind_id = '"+ind+"'");
		sql.append(" group by (a.ind_id,a.month_id)) t ");
       // sql.append(" order by REGION_ID, to_number(ind_id)");
	  return getDataAccess().queryForMap(sql.toString());
  }  
	//本地全业务抱怨率月报表按时间取数（折线图柱状图点数据）
	public  Map<String,Object> getChartData_AllBusiMon(String dateTime, Map<String,Object> map) {
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
	      String prodTypeCode ="".equals(Convert.toString(map.get("prodTypeCode")))?"-1":Convert.toString(map.get("prodTypeCode"));
	      String cmplBusiTypeCode ="".equals(Convert.toString(map.get("cmplBusiTypeCode")))?"1":Convert.toString(map.get("cmplBusiTypeCode"));
	      StringBuffer sql = new StringBuffer("select decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM)/max(a.NUM1)*1000,2))cmpl"
	              +" from cs_cmpl_complain_mon a," +
	              		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeCode+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
	              				"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeCode+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
	              						"where a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.region_id='"+ zoneCode+"' and a.month_id = '"+dateTime+"'");
	  return getDataAccess().queryForMap(sql.toString());
}  
	//投诉指标日监测报表柱状图
	public List<Map<String, Object>> getBarCmplIndex_Day(Map<String, Object> queryData){
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	    String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	    String prodType ="".equals(Convert.toString(queryData.get("prodType")))?"-1":Convert.toString(queryData.get("prodType"));
	    String ind="".equals(Convert.toString(queryData.get("ind")))?"1":Convert.toString(queryData.get("ind"));//默认显示工信部申诉量
		String sql="select z.* from (select b.ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
				"NVL(sum(NUM),0) current_num" +
				",sum(TOTAL_NUM) TOTAL_NUM,sum(TOTAL_NUM_LAST) TOTAL_NUM_LAST," +
				" DECODE(sum(TOTAL_NUM_LAST),0,0,round((sum(TOTAL_NUM)/sum(TOTAL_NUM_LAST)-1)*100,2))HB_NUM,DECODE(sum(TOTAL_NUM_LY_M),0,0,round((sum(TOTAL_NUM)/sum(TOTAL_NUM_LY_M)-1)*100,2))TB_NUM," +
	      		"round(sum(TOTAL_NUM)/to_number(substr(day_id,7,2)),2)AVG_NUM"
	             +" from CS_CMPL_MONITOR_DAY a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=2 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b" +
	             		",(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c" +
					" where a.REGION_ID = b.ZONE_CODE and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.DAY_ID='"+dateTime+"' and a.IND_ID='"+ind+"'";      
	     sql+=" group by b.ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.day_id) z ";
	     sql+=" order by z.PAR_REGION_ID, z.ORDER_ID,z.REGION_ID ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//投诉指标月监测报表柱状图
	public List<Map<String, Object>> getBarCmplIndex_Mon(Map<String, Object> queryData){
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	    String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	    String prodType ="".equals(Convert.toString(queryData.get("prodType")))?"-1":Convert.toString(queryData.get("prodType"));
	    String ind="".equals(Convert.toString(queryData.get("ind")))?"103":Convert.toString(queryData.get("ind"));//默认显示工信部申诉率
		StringBuffer sql = new StringBuffer("select PAR_REGION_NAME,PAR_REGION_ID,REGION_NAME,region_id,IND_NAME,IND_ID," +
				"case when IND_ID IN ( 5, 6, 7, 10, 11) then to_char(current_value * 100, 'FM990.00')" +
	      		" when ind_id = 9 then to_char(round(current_value))" +
	      		" when IND_ID IN (3, 4, 12) then to_char(current_value * 1000, 'FM990.00')" +
	      		" when ind_id in (1, 2, 8) then to_char(current_value * 1000000, 'FM990.00') end current_value," +
	      		"case when IND_ID IN (5, 6, 7, 10, 11) then to_char(lastmon_value * 100, 'FM990.00')" +
	      		" when ind_id = 9 then to_char(round(lastmon_value))" +
	      		" when IND_ID IN (3, 4, 12) then to_char(lastmon_value * 1000, 'FM990.00') " +
	      		" when ind_id in (1, 2, 8) then to_char(lastmon_value * 1000000, 'FM990.00') end lastmon_value," +
	      		"case when IND_ID IN ( 5, 6, 7, 10, 11) then to_char(avg_value * 100, 'FM990.00')" +
	      		" when ind_id = 9 then to_char(round(avg_value))" +
	      		" when IND_ID IN (3, 4, 12) then  to_char(avg_value * 1000, 'FM990.00')" +
	      		" when ind_id in (1, 2, 8) then to_char(avg_value * 1000000, 'FM990.00') end avg_value," +
	      		"to_char(round(decode(lastyear_value,0,0,current_value / lastyear_value - 1) * 100,2),'FM990.00') TB," +
	      		"to_char(round(decode(lastmon_value,0,0,current_value / lastmon_value - 1) * 100,2),'FM990.00') HB, " +
	      		"month_id from (select a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.IND_NAME,a.IND_ID," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then  decode(max(num2), 0, 0, sum(num1) / max(num2))" +
	      		" when ind_id in (9) then sum(num1)" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(num2), 0, 0, sum(num1) / sum(num2)) end current_value," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(num2_last),0,0,sum(num1_last) / max(num2_last))" +
	      		" when ind_id in (9) then sum(num1_last)" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(num2_last),0,0,sum(num1_last) / sum(num2_last)) end lastmon_value," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(num2_ly_m),0,0,sum(num1_ly_m) / max(num2_ly_m))" +
	      		" when ind_id in (9) then sum(num1_ly_m)" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(num2_ly_m),0,0,sum(num1_ly_m) / sum(num2_ly_m)) end lastyear_value," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(total_num2),0,0,sum(total_num1) / max(total_num2))" +
	      		" when ind_id in (9) then sum(total_num1) / to_number(substr(month_id, 5, 2))" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(total_num2),0,0,sum(total_num1) / sum(total_num2)) end avg_value," +
	      		"a.month_id,dim_level,ORDER_ID,to_number(substr(month_id, 5, 2)) from CS_CMPL_MONITOR_MON_ZXD a,(select dim_level,ORDER_ID,zone_code from META_DIM_ZONE where  dim_level=2 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b,"
	             +"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c " 
		         + "where a.REGION_ID = b.ZONE_CODE and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.MONTH_ID='"+dateTime+"' and a.IND_ID='"+ind+"'");    
		sql.append(" group by (ORDER_ID, a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME, a.region_id, a.IND_NAME, a.IND_ID,a.month_id, dim_level)) t ");
        sql.append(" order by dim_level, ORDER_ID, REGION_ID ");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//投诉类指标日监测报表柱状图21地市
	public List<Map<String, Object>> get21BarCmplIndex_Day(Map<String, Object> queryData){
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	    String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	    String prodType ="".equals(Convert.toString(queryData.get("prodType")))?"-1":Convert.toString(queryData.get("prodType"));
	    String ind="".equals(Convert.toString(queryData.get("ind")))?"1":Convert.toString(queryData.get("ind"));//默认显示工信部申诉量
	    String sql="select z.* from (select b.ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
		"sum(NUM) current_num" +
		",sum(TOTAL_NUM) TOTAL_NUM,sum(TOTAL_NUM_LAST) TOTAL_NUM_LAST," +
		" DECODE(sum(TOTAL_NUM_LAST),0,0,round((sum(TOTAL_NUM)/sum(TOTAL_NUM_LAST)-1)*100,2))HB_NUM,DECODE(sum(TOTAL_NUM_LY_M),0,0,round((sum(TOTAL_NUM)/sum(TOTAL_NUM_LY_M)-1)*100,2))TB_NUM," +
  		"round(sum(TOTAL_NUM)/to_number(substr(day_id,7,2)),2)AVG_NUM"
         +" from CS_CMPL_MONITOR_DAY a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=3 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b" +
         		",(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c " +
					" where a.REGION_ID = b.ZONE_CODE and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.DAY_ID='"+dateTime+"' and a.IND_ID='"+ind+"'";
		 sql+=" group by b.ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.day_id) z ";
		 sql+=" order by z.PAR_REGION_ID, z.ORDER_ID,z.REGION_ID ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//投诉类指标月监测报表柱状图21地市
	public List<Map<String, Object>> get21BarCmplIndex_Mon(Map<String, Object> queryData){
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	    String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	    String prodType ="".equals(Convert.toString(queryData.get("prodType")))?"-1":Convert.toString(queryData.get("prodType"));
	    String ind="".equals(Convert.toString(queryData.get("ind")))?"102":Convert.toString(queryData.get("ind"));//默认显示工信部申诉率
	    StringBuffer sql = new StringBuffer("select PAR_REGION_NAME,PAR_REGION_ID,REGION_NAME,region_id,IND_NAME,IND_ID," +
	    		"case when IND_ID IN ( 5, 6, 7, 10, 11) then to_char(current_value * 100, 'FM990.00')" +
	      		" when ind_id = 9 then to_char(round(current_value))" +
	      		" when IND_ID IN (3, 4, 12) then to_char(current_value * 1000, 'FM990.00')" +
	      		" when ind_id in (1, 2, 8) then to_char(current_value * 1000000, 'FM990.00') end current_value," +
	      		"case when IND_ID IN (5, 6, 7, 10, 11) then to_char(lastmon_value * 100, 'FM990.00')" +
	      		" when ind_id = 9 then to_char(round(lastmon_value))" +
	      		" when IND_ID IN (3, 4, 12) then to_char(lastmon_value * 1000, 'FM990.00') " +
	      		" when ind_id in (1, 2, 8) then to_char(lastmon_value * 1000000, 'FM990.00') end lastmon_value," +
	      		"case when IND_ID IN ( 5, 6, 7, 10, 11) then to_char(avg_value * 100, 'FM990.00')" +
	      		" when ind_id = 9 then to_char(round(avg_value))" +
	      		" when IND_ID IN (3, 4, 12) then  to_char(avg_value * 1000, 'FM990.00')" +
	      		" when ind_id in (1, 2, 8) then to_char(avg_value * 1000000, 'FM990.00') end avg_value," +
	      		"to_char(round(decode(lastyear_value,0,0,current_value / lastyear_value - 1) * 100,2),'FM990.00') TB," +
	      		"to_char(round(decode(lastmon_value,0,0,current_value / lastmon_value - 1) * 100,2),'FM990.00') HB, " +
	      		"month_id from (select a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.IND_NAME,a.IND_ID," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then  decode(max(num2), 0, 0, sum(num1) / max(num2))" +
	      		" when ind_id in (9) then sum(num1)" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(num2), 0, 0, sum(num1) / sum(num2)) end current_value," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(num2_last),0,0,sum(num1_last) / max(num2_last))" +
	      		" when ind_id in (9) then sum(num1_last)" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(num2_last),0,0,sum(num1_last) / sum(num2_last)) end lastmon_value," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(num2_ly_m),0,0,sum(num1_ly_m) / max(num2_ly_m))" +
	      		" when ind_id in (9) then sum(num1_ly_m)" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(num2_ly_m),0,0,sum(num1_ly_m) / sum(num2_ly_m)) end lastyear_value," +
	      		"case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(total_num2),0,0,sum(total_num1) / max(total_num2))" +
	      		" when ind_id in (9) then sum(total_num1) / to_number(substr(month_id, 5, 2))" +
	      		" when IND_ID in (5, 6, 11) then decode(sum(total_num2),0,0,sum(total_num1) / sum(total_num2)) end avg_value," +
	      		"a.month_id,dim_level,ORDER_ID,to_number(substr(month_id, 5, 2)) from CS_CMPL_MONITOR_MON_ZXD a,(select dim_level,ORDER_ID,zone_code from META_DIM_ZONE where  dim_level=3 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b,"
	             +"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c " 
		         + "where a.REGION_ID = b.ZONE_CODE and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.MONTH_ID='"+dateTime+"' and a.IND_ID='"+ind+"'");    
	    sql.append(" group by (ORDER_ID, a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME, a.region_id, a.IND_NAME, a.IND_ID,a.month_id, dim_level)) t ");
        sql.append(" order by dim_level, ORDER_ID, REGION_ID ");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//投诉指标柱状图五大区循环
	public List<Map<String, Object>> getBarCmplIndex_5Zone(String zoneCode){
		String sql="select ZONE_CODE,ZONE_NAME from META_DIM_ZONE where level = 2 " +
		" start with ZONE_CODE ='"+zoneCode+"' connect by prior ZONE_CODE = zone_par_code order by order_id,dim_level";         
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//投诉指标日监测柱状图下钻区域
	public List<Map<String, Object>> getBarCmplIndex_Zone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String ind="".equals(Convert.toString(queryData.get("ind")))?"3":Convert.toString(queryData.get("ind"));//默认显示本地全业务抱怨量
		 String sql="select rownum,region_id,region_name,current_num,TOTAL_NUM from (select region_id,region_name, sum(NUM)current_num,sum(TOTAL_NUM) TOTAL_NUM from CS_CMPL_MONITOR_DAY_ZXD a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+)  and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+1 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and day_id='"+dateTime+"' and ind_id='"+ind+"' group by order_id,region_id,region_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//投诉指标日监测柱状图下级地市循环_网格数据
	public List<Map<String, Object>> getBarCmplIndex_DownZone(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String ind="".equals(Convert.toString(queryData.get("ind")))?"3":Convert.toString(queryData.get("ind"));//默认显示本地全业务抱怨量
		 String sql="select rownum,region_id,region_name,current_num,TOTAL_NUM from (select region_id,region_name, sum(NUM)current_num,sum(TOTAL_NUM)TOTAL_NUM from CS_CMPL_MONITOR_DAY_ZXD a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+)  and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+2 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and day_id='"+dateTime+"' and ind_id='"+ind+"' group by order_id,region_id,region_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
         List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//投诉指标月监测柱状图下钻区域
	public List<Map<String, Object>> getBarCmplIndexMon_Zone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String ind="".equals(Convert.toString(queryData.get("ind")))?"102":Convert.toString(queryData.get("ind"));//默认显示本地全业务抱怨率
		 String sql="select rownum,region_id,region_name,case when IND_ID IN (5, 6, 7, 10, 11) then to_char(current_value * 100, 'FM990.00')" +
		 		" when ind_id = 9 then to_char(round(current_value)) " +
		 		" when IND_ID IN (3, 4, 12) then  to_char(current_value * 1000, 'FM990.00')" +
		 		" when ind_id in (1, 2, 8) then  to_char(current_value * 1000000, 'FM990.00')" +
		 		" end current_value,month_id from (select ind_id,region_id,region_name," +
				" case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(num2), 0, 0, sum(num1) / max(num2))" +
		   		" when ind_id in (9) then sum(num1)" +
		   		"  when IND_ID in (5, 6, 11) then  decode(sum(num2), 0, 0, sum(num1) / sum(num2))" +
		   		"  end current_value,a.month_id,to_number(substr(month_id, 5, 2))" +
		 		" from CS_CMPL_MONITOR_MON_ZXD a, meta_dim_zone_custgrp b"+
				" where a.REGION_ID = b.ZONE_CODE(+) and b.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' and b.DIM_LEVEL=(select dim_level+1 from meta_dim_zone_custgrp " +
						"where ZONE_CODE ='"+zoneCode+"') and a.month_id='"+dateTime+"' and a.ind_id='"+ind+"' group by order_id,region_id,region_name,dim_level,a.ind_id,a.month_id " +
						"order by b.order_id, region_id,b.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//本地全业务抱怨率月报表默认柱状图
	public List<Map<String, Object>> getBarAllBusiMon_Zone(Map<String, Object> queryData){
		 String changeCode =Convert.toString(queryData.get("changeCode"));
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String prodTypeCode ="".equals(Convert.toString(queryData.get("prodTypeCode")))?"-1":Convert.toString(queryData.get("prodTypeCode"));
		 String cmplBusiTypeCode ="".equals(Convert.toString(queryData.get("cmplBusiTypeCode")))?"1":Convert.toString(queryData.get("cmplBusiTypeCode"));
		 String sql="";
		 if("0".equals(changeCode)){
			 sql="select rownum,region_id,region_name,cmpl from (select region_id,region_name," +
		 		"decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM)/max(a.NUM1)*1000,2)) cmpl" +
		 		" from cs_cmpl_complain_mon partition(p"+dateTime+") a, meta_dim_zone_custgrp b,"+
		 		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeCode+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
		 		"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeCode+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
				" where a.REGION_ID = b.ZONE_CODE(+) and a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and " +
				"b.zone_par_code=(select zone_par_code from meta_dim_zone_custgrp " +
						"where ZONE_CODE ='"+zoneCode+"')  group by order_id,region_id,region_name " +
						"order by b.order_id )where rownum<22 and region_name <> '其他' ";
		 }else{
			 sql="select rownum,region_id,region_name,cmpl from (select region_id,region_name," +
			 		"decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM)/max(a.NUM1)*1000,2)) cmpl" +
			 		" from cs_cmpl_complain_mon partition(p"+dateTime+")a, meta_dim_zone_custgrp b,"+
			 		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeCode+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
			 		"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeCode+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
					" where a.REGION_ID = b.ZONE_CODE(+) and a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and " +
					"b.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' and b.DIM_LEVEL=(select dim_level+'"+changeCode+"' from meta_dim_zone_custgrp " +
							"where ZONE_CODE ='"+zoneCode+"') and a.month_id='"+dateTime+"'  group by order_id,region_id,region_name " +
							"order by b.order_id )where rownum<22 and region_name <> '其他' ";
		 }
		 List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
		 public List<Map<String, Object>> getBarAllBusiMon_Zone_Dg(Map<String, Object> queryData){
			 String changeCode =Convert.toString(queryData.get("changeCode"));
			 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
			 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
			 String prodTypeCode ="".equals(Convert.toString(queryData.get("prodTypeCode")))?"-1":Convert.toString(queryData.get("prodTypeCode"));
			 String cmplBusiTypeCode ="".equals(Convert.toString(queryData.get("cmplBusiTypeCode")))?"1":Convert.toString(queryData.get("cmplBusiTypeCode"));
			 String sql="";
			 if(zoneCode.length()==3 && !zoneCode.equals("0000")){
				 changeCode="2";
			 }
			 if("0".equals(changeCode)){
				 sql="select rownum,region_id,region_name,cmpl from (select region_id,region_name," +
			 		"decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM)/max(a.NUM1)*1000,2)) cmpl" +
			 		" from cs_cmpl_complain_mon a, meta_dim_zone_custgrp b,"+
			 		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeCode+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
			 		"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeCode+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
					" where a.REGION_ID = b.ZONE_CODE(+) and a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and " +
					"b.zone_par_code=(select zone_par_code from meta_dim_zone_custgrp " +
							"where ZONE_CODE ='"+zoneCode+"') and a.month_id='"+dateTime+"'  group by order_id,region_id,region_name,dim_level,a.month_id " +
							"order by b.order_id, region_id,b.dim_level)where rownum<22 and region_name <> '其他' ";
			 }else{
				 sql="select rownum,region_id,region_name,cmpl from (select region_id,region_name," +
				 		"decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM)/max(a.NUM1)*1000,2)) cmpl" +
				 		" from cs_cmpl_complain_mon a, meta_dim_zone_custgrp b,"+
				 		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeCode+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
				 		"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeCode+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
						" where a.REGION_ID = b.ZONE_CODE(+) and a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and " +
						"b.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' and b.DIM_LEVEL=(select dim_level+'"+changeCode+"' from meta_dim_zone_custgrp " +
								"where ZONE_CODE ='"+zoneCode+"') and a.month_id='"+dateTime+"'  group by order_id,region_id,region_name,dim_level,a.month_id " +
								"order by b.order_id, region_id,b.dim_level)where rownum<22 and region_name <> '其他' ";
			 }
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//本地全业务抱怨率折线图取开始日期到结束日期之间的数据
	public List<Map<String, Object>> getLinePointAllBusiMon(String startTime,String endTime,Map<String, Object> queryData){
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String prodTypeCode ="".equals(Convert.toString(queryData.get("prodTypeCode")))?"-1":Convert.toString(queryData.get("prodTypeCode"));
		 String cmplBusiTypeCode ="".equals(Convert.toString(queryData.get("cmplBusiTypeCode")))?"1":Convert.toString(queryData.get("cmplBusiTypeCode"));
		 StringBuffer sql=new StringBuffer("select month_id,decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM)/max(a.NUM1)*1000,2)) cmpl from cs_cmpl_complain_mon a, " +
				 "(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeCode+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
			 		"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeCode+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
					" where a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and " +
				"a.region_id = '"+zoneCode+"' and a.month_id <= '"+endTime+"'  and a.month_id >= '"+startTime+"' group by month_id order by month_id");  
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//投诉指标月监测柱状图下级地市循环_网格数据
	public List<Map<String, Object>> getBarCmplIndexMon_DownZone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String ind="".equals(Convert.toString(queryData.get("ind")))?"102":Convert.toString(queryData.get("ind"));//默认显示本地全业务抱怨率
		 String sql="select rownum,region_id,region_name,case when IND_ID IN (5, 6, 7, 10, 11) then to_char(current_value * 100, 'FM990.00')" +
		 		" when ind_id = 9 then to_char(round(current_value)) " +
		 		" when IND_ID IN (3, 4, 12) then  to_char(current_value * 1000, 'FM990.00')" +
		 		" when ind_id in (1, 2, 8) then  to_char(current_value * 1000000, 'FM990.00')" +
		 		" end current_value,month_id from (select ind_id,region_id,region_name," +
				" case when IND_ID IN (1, 2, 3, 4, 7, 8, 10, 12) then decode(max(num2), 0, 0, sum(num1) / max(num2))" +
		   		" when ind_id in (9) then sum(num1)" +
		   		"  when IND_ID in (5, 6, 11) then  decode(sum(num2), 0, 0, sum(num1) / sum(num2))" +
		   		"  end current_value,a.month_id,to_number(substr(month_id, 5, 2))" +
		 		" from CS_CMPL_MONITOR_MON_ZXD a, meta_dim_zone_custgrp b"+
				" where a.REGION_ID = b.ZONE_CODE(+) and b.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' and b.DIM_LEVEL=(select dim_level+2 from meta_dim_zone_custgrp " +
						"where ZONE_CODE ='"+zoneCode+"') and a.month_id='"+dateTime+"' and a.ind_id='"+ind+"' group by order_id,region_id,region_name,dim_level,a.ind_id,a.month_id " +
						"order by b.order_id, region_id,b.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//本地全业务抱怨率月报表_网格数据
	public List<Map<String, Object>> getBarAllBusiMon_DownZone(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String prodTypeCode ="".equals(Convert.toString(queryData.get("prodTypeCode")))?"-1":Convert.toString(queryData.get("prodTypeCode"));
		 String cmplBusiTypeCode ="".equals(Convert.toString(queryData.get("cmplBusiTypeCode")))?"1":Convert.toString(queryData.get("cmplBusiTypeCode"));
		 String sql="select rownum,region_id,region_name,COMPLAIN_NUM,cmpl from (select region_id,region_name,sum(a.COMPLAIN_NUM)COMPLAIN_NUM," +
		 		"max(a.NUM1)NUM1,decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM)/max(a.NUM1)*1000,2)) cmpl" +
		 		" from cs_cmpl_complain_mon a, meta_dim_zone_custgrp b,"+
		 		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeCode+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
		 		"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeCode+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
				" where a.REGION_ID = b.ZONE_CODE(+) and a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and " +
				"b.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' and b.DIM_LEVEL=(select dim_level+2 from meta_dim_zone_custgrp " +
						"where ZONE_CODE ='"+zoneCode+"') and a.month_id='"+dateTime+"'  group by order_id,region_id,region_name,dim_level,a.month_id " +
						"order by b.order_id, region_id,b.dim_level)where rownum<22 ";
         List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//投诉指标柱状图21地市循环
	public List<Map<String, Object>> getBarCmplIndex_21Zone(){
		String sql="select ZONE_CODE,ZONE_NAME from META_DIM_ZONE where level = '3'" +
		" start with ZONE_CODE ='0000' connect by prior ZONE_CODE = zone_par_code order by order_id,dim_level";      
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//投诉日监测折线图取开始日期到结束日期之间的数据
	public List<Map<String, Object>> getLinePoint(String startTime,String endTime,Map<String, Object> queryData){
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		// String prodType ="".equals(Convert.toString(queryData.get("prodTypeCode")))?"-1":Convert.toString(queryData.get("prodTypeCode"));
		 String ind="".equals(Convert.toString(queryData.get("ind")))?"3":Convert.toString(queryData.get("ind"));//默认显示本地全业务抱怨量
		 StringBuffer sql=new StringBuffer("select sum(NUM) current_num,day_id from CS_CMPL_MONITOR_DAY_ZXD a " +
				//"(select CMPL_PROD_TYPE_CODE  from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE = '"+prodType+"' " +
				//"connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE) c where a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE " +
				"where a.region_id = '"+zoneCode+"' and a.DAY_ID <= '"+endTime+"'  and a.DAY_ID >= '"+startTime+"' and a.ind_id = '"+ind+"' group by day_id order by day_id");  
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//越级投诉现象第一级_投诉现象为空
	public List<Map<String, Object>> getBarCmplSkip_1Problem(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime"));
		String channelType =Convert.toString(queryData.get("channelType"));
		String prodType =Convert.toString(queryData.get("prodType"));
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		StringBuffer sql=new StringBuffer("select distinct CMPL_BUSINESS_TYPE1_ID,CMPL_BUSINESS_TYPE1_NAME from CS_CMPL_SKIP_PROBLEM_MON a," +
	            "(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c " +
				"where a.PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and MONTH_ID='"+dateTime+"' and REGION_ID='"+zoneCode+"'") ;
		if(channelType!=null&&!"".equals(channelType)){
			sql.append("and CHANNEL_TYPE_ID='"+channelType+"' ");
		}
		sql.append("order by to_number(CMPL_BUSINESS_TYPE1_ID)");      
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//越级投诉现象第二级
	public List<Map<String, Object>> getBarCmplSkip_2Problem(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime"));
		String channelType =Convert.toString(queryData.get("channelType"));
		String cmplBusinessType =Convert.toString(queryData.get("cmplBusinessType"));
		String prodType =Convert.toString(queryData.get("prodType"));
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		StringBuffer sql=new StringBuffer("select distinct CMPL_BUSINESS_TYPE2_ID,CMPL_BUSINESS_TYPE2_NAME from CS_CMPL_SKIP_PROBLEM_MON a," +
				 "(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c " +
					"where a.PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and CMPL_BUSINESS_TYPE1_ID='"+cmplBusinessType+"' and MONTH_ID='"+dateTime+"' and REGION_ID='"+zoneCode+"'") ;
		if(channelType!=null&&!"".equals(channelType)){
			sql.append("and CHANNEL_TYPE_ID='"+channelType+"' ");
		}
		sql.append("order by to_number(CMPL_BUSINESS_TYPE2_ID) desc");       
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//本地投诉现象第一级_投诉现象为空
	public List<Map<String, Object>> getBarNativeCmplSkip_1Problem(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime"));
		String prodType =Convert.toString(queryData.get("prodType"));
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		StringBuffer sql=new StringBuffer("select distinct CMPL_BUSINESS_TYPE1_ID,CMPL_BUSINESS_TYPE1_NAME from CS_CMPL_PROBLEM_MON a," +
				 "(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c " +
					"where a.PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and MONTH_ID='"+dateTime+"' and REGION_ID='"+zoneCode+"'") ;
		sql.append("order by to_number(CMPL_BUSINESS_TYPE1_ID)");      
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//本地投诉现象第二级
	public List<Map<String, Object>> getBarNativeCmplSkip_2Problem(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime"));
		String cmplBusinessType =Convert.toString(queryData.get("cmplBusinessType"));
		String prodType =Convert.toString(queryData.get("prodType"));
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		StringBuffer sql=new StringBuffer("select distinct CMPL_BUSINESS_TYPE2_ID,CMPL_BUSINESS_TYPE2_NAME from CS_CMPL_PROBLEM_MON a," +
				 "(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c " +
					"where a.PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and CMPL_BUSINESS_TYPE1_ID='"+cmplBusinessType+"' and MONTH_ID='"+dateTime+"' and REGION_ID='"+zoneCode+"'") ;
		sql.append("order by to_number(CMPL_BUSINESS_TYPE2_ID) desc");       
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//越级、本地投诉原因第一级_投诉原因为空
	public List<Map<String, Object>> getBarCmplReason_1Problem(Map<String, Object> queryData,String tabStr){
		String dateTime =Convert.toString(queryData.get("dateTime"));
		String channelType =Convert.toString(queryData.get("channelType"));
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		StringBuffer sql=new StringBuffer("select rownum ,CMPL_REASON1_ID, CMPL_REASON1_NAME from (select distinct CMPL_REASON1_ID,CMPL_REASON1_NAME," +
				"sum(NUM1) from "+tabStr+" " +
				"where MONTH_ID='"+dateTime+"' ") ;
		sql.append("and REGION_ID='"+zoneCode+"' ");
		if(channelType!=null&&!"".equals(channelType)){
			sql.append("and CHANNEL_TYPE_ID='"+channelType+"' ");
		}
		sql.append("group by CMPL_REASON1_ID,CMPL_REASON1_NAME order by sum(NUM1)desc,to_number(CMPL_REASON1_ID)) where rownum <= 20");      
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//越级、本地投诉原因第二级
	public List<Map<String, Object>> getBarCmplReason_2Problem(Map<String, Object> queryData,String tabStr){
		String dateTime =Convert.toString(queryData.get("dateTime"));
		String channelType =Convert.toString(queryData.get("channelType"));
		String reasonId =Convert.toString(queryData.get("reasonId"));
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		StringBuffer sql=new StringBuffer("select rownum ,CMPL_REASON2_ID, CMPL_REASON2_NAME from (select distinct CMPL_REASON2_ID,CMPL_REASON2_NAME,sum(NUM1) from "+tabStr+" " +
				"where CMPL_REASON1_ID='"+reasonId+"' and MONTH_ID='"+dateTime+"' ") ;
		sql.append("and REGION_ID='"+zoneCode+"' ");
		if(channelType!=null&&!"".equals(channelType)){
			sql.append("and CHANNEL_TYPE_ID='"+channelType+"' ");
		}
		sql.append("group by CMPL_REASON2_ID,CMPL_REASON2_NAME order by sum(NUM1)desc,to_number(CMPL_REASON2_ID)desc)where rownum <= 20");       
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//投诉总体情况汇总分析月报表
	public Map<String, Object> getChartData_CmplSumMon(String dateTime,Map<String, Object> queryData){
	    String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		StringBuffer sql = new StringBuffer("select " +
				"round(SUM(NUM1400) ,2) NUM1,DECODE(SUM(num1401),0,0,round(SUM(NUM1400)/SUM(num1401)*1000000,2))NUM2," +
				"DECODE(SUM(NUM301),0,0,round(SUM(NUM300)/SUM(NUM301)*100,2))NUM3," +
				"round(SUM(NUM1700),2)NUM4,DECODE(SUM(NUM1701),0,0,round(SUM(NUM1700)/SUM(NUM1701)*1000,2))NUM5," +
				"round(SUM(NUM1500),2)NUM6,DECODE(SUM(num1501),0,0,round(SUM(NUM1500)/SUM(num1501)*1000,2))NUM7," +
				"DECODE(SUM(num501),0,0,round(SUM(NUM500)/SUM(num501)*100,2))NUM8," +
				"DECODE(SUM(num601),0,0,round(SUM(NUM600)/SUM(num601)*100,2))NUM9," +
				"DECODE(SUM(num1601),0,0,round(SUM(NUM1600)/SUM(num1601)*100,2))NUM10," +
				"DECODE(SUM(num701),0,0,round(SUM(NUM700)/SUM(num701)*100,2))NUM11," +
				"DECODE(SUM(num801),0,0,round(SUM(NUM800)/SUM(num801)*1000,2))NUM12," +
				"DECODE(SUM(num901),0,0,round(SUM(NUM900)/SUM(num901)*1000,2))NUM13," +
				"DECODE(SUM(num1001),0,0,round(SUM(NUM1000)/SUM(num1001)*1000,2))NUM14," +
				"DECODE(SUM(num1201),0,0,round(SUM(NUM1200)/SUM(num1201)*100,2))NUM15," +
				"round(SUM(NUM200),2)NUM16," +
				"DECODE(SUM(num201),0,0,round(SUM(num200)/SUM(num201)*1000,2))NUM17 " +
	      		"from  CS_CMPL_SUMMARY_MON_S " +
	      		"where REGION_ID='"+zoneCode+"' "
		         + "and MONTH_ID='"+dateTime+"'");    
          Map<String,Object> list=getDataAccess().queryForMap(sql.toString());
	      return list;
	}
	//某区域投诉总体情况汇总分析月报表
	public Map<String, Object> getChartData_CmplSumZoneMon(String dateTime,Map<String, Object> queryData){
	    String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	    String ind ="".equals(Convert.toString(queryData.get("ind")))?"":Convert.toString(queryData.get("ind"));
		StringBuffer sql = new StringBuffer("select " +
				"case when max(ind_id) in ('1', '9', '10', '11', '15', '21') then DECODE(SUM(NUM2), 0, 0, ROUND(SUM(NUM1) / SUM(NUM2) * 1000, 2)) " +
				"when max(ind_id) in ('2', '3') then DECODE(SUM(NUM2), 0, 0, ROUND(SUM(NUM1) / SUM(NUM2) * 1000000, 2)) " +
				"when max(ind_id) in ('17', '18', '19', '20', '22') then SUM(NUM1) " +
				"else DECODE(SUM(NUM2), 0, 0, ROUND(SUM(NUM1) / SUM(NUM2) * 100, 2)) end CURRENT_VALUE " +
	      		"from  CS_CMPL_SUMMARY_MON_D " +
	      		"where REGION_ID='"+zoneCode+"' "
		         + "and MONTH_ID='"+dateTime+"' and ind_id='"+ind+"'");    
          Map<String,Object> list=getDataAccess().queryForMap(sql.toString());
	      return list;
	}
	//年度离网用户投诉情况分析月报表_折线图
	public Map<String, Object> getChartData_CmplOffMonLine(int i,String dateTime,Map<String, Object> queryData){
	    String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		StringBuffer sql = new StringBuffer("select " +
				"sum(a.offline_"+i+"_MONTH) OFFLINE_VALUE " +
	      		"from  cs_cust_cmpl_off_line_s a " +
	      		"where REGION_ID='"+zoneCode+"' "
		         + "and MONTH_ID between substr('"+dateTime+"',1,4)|| '01' and '"+dateTime+"' ");  
          Map<String,Object> list=getDataAccess().queryForMap(sql.toString());
	      return list;
	}
	//越级投诉现象月报表(投诉现象为空)
	public Map<String, Object> getChartData_CmplSkipMon(String dateTime,Map<String, Object> queryData){
	    String zoneCode =Convert.toString(queryData.get("zoneCode"));
	    String channelType =Convert.toString(queryData.get("channelType"));
	    String prodType =Convert.toString(queryData.get("prodType"));
	    String cmplBusinessType =Convert.toString(queryData.get("cmplBusinessType"));
		StringBuffer sql = new StringBuffer("select " +
				"SUM(NUM1)NUM1,DECODE(max(num2),0,0,round(sum(num1)/max(num2)*1000000,2))NUM3 " +
	      		"from  CS_CMPL_SKIP_PROBLEM_MON a, " +
	      		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c " +
				"where a.PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and REGION_ID='"+zoneCode+"' "
		         + "and MONTH_ID='"+dateTime+"' "); 
		if(cmplBusinessType!=null&&!"".equals(cmplBusinessType)){
			sql.append("and CMPL_BUSINESS_TYPE1_ID='"+cmplBusinessType+"' ");
		}
         if(channelType!=null&&!"".equals(channelType)){
			sql.append(" and CHANNEL_TYPE_ID='"+channelType+"' ");
		}
		sql.append(" group by month_id");
          Map<String,Object> list=getDataAccess().queryForMap(sql.toString());
	      return list;
	}
	//越级投诉现象月报表（投诉现象不为空）_柱状图
	public Map<String, Object> getChartData_CmplSkipMon1(String dateTime,Map<String, Object> queryData){
	    String zoneCode =Convert.toString(queryData.get("zoneCode"));
	    String cmplBusinessType =Convert.toString(queryData.get("cmplBusinessType"));
	    String channelType =Convert.toString(queryData.get("channelType"));
	    String prodType =Convert.toString(queryData.get("prodType"));
		StringBuffer sql = new StringBuffer("select " +
				"SUM(NUM1)NUM1,DECODE(max(num2),0,0,round(sum(num1)/max(num2)*1000000,2))NUM3 " +
	      		"from  CS_CMPL_SKIP_PROBLEM_MON a, " +
	      		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c " +
				"where a.PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and REGION_ID='"+zoneCode+"' "
		         + "and MONTH_ID='"+dateTime+"' "); 
			sql.append("and CMPL_BUSINESS_TYPE2_ID='"+cmplBusinessType+"' ");
		if(channelType!=null&&!"".equals(channelType)){
			sql.append("and CHANNEL_TYPE_ID='"+channelType+"' ");
		}
		sql.append("group by month_id ");
          Map<String,Object> list=getDataAccess().queryForMap(sql.toString());
	      return list;
	}
	//越级投诉现象月报表（投诉现象不为空）_折线图
	public Map<String, Object> getChartData_CmplSkipMon2(String dateTime,Map<String, Object> queryData){
	    String zoneCode =Convert.toString(queryData.get("zoneCode"));
	    String cmplBusinessType =Convert.toString(queryData.get("cmplBusinessType"));
	    String channelType =Convert.toString(queryData.get("channelType"));
	    String prodType =Convert.toString(queryData.get("prodType"));
		StringBuffer sql = new StringBuffer("select " +
				"SUM(NUM1)NUM1,DECODE(max(num2),0,0,round(sum(num1)/max(num2)*1000000,2))NUM3 " +
	      		"from  CS_CMPL_SKIP_PROBLEM_MON a, " +
	      		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c " +
				"where a.PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and REGION_ID='"+zoneCode+"' "
		         + "and MONTH_ID='"+dateTime+"' "); 
			sql.append("and CMPL_BUSINESS_TYPE1_ID='"+cmplBusinessType+"' ");
		if(channelType!=null&&!"".equals(channelType)){
			sql.append("and CHANNEL_TYPE_ID='"+channelType+"' ");
		}
		sql.append("group by month_id ");
          Map<String,Object> list=getDataAccess().queryForMap(sql.toString());
	      return list;
	}
	//本地投诉现象月报表(投诉现象为空)
	public Map<String, Object> getChartData_CmplMon(String dateTime,Map<String, Object> queryData){
	    String zoneCode =Convert.toString(queryData.get("zoneCode"));
	    String prodType =Convert.toString(queryData.get("prodType"));
	    String cmplBusinessType =Convert.toString(queryData.get("cmplBusinessType"));
		StringBuffer sql = new StringBuffer("select " +
				"SUM(NUM1)NUM1,DECODE(max(NUM2),0,0,ROUND(SUM(NUM1)/max(NUM2)*1000,2))NUM3 " +
	      		"from  CS_CMPL_PROBLEM_MON a," +
	      		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c " +
				"where a.PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and REGION_ID='"+zoneCode+"' "
		         + "and MONTH_ID='"+dateTime+"' "); 
		if(cmplBusinessType!=null&&!"".equals(cmplBusinessType)){
			sql.append("and CMPL_BUSINESS_TYPE1_ID='"+cmplBusinessType+"' ");
		}
		sql.append(" group by month_id");
          Map<String,Object> list=getDataAccess().queryForMap(sql.toString());
	      return list;
	}
	//本地投诉现象月报表（投诉现象不为空）_柱状图
	public Map<String, Object> getChartData_CmplMon1(String dateTime,Map<String, Object> queryData){
	    String zoneCode =Convert.toString(queryData.get("zoneCode"));
	    String cmplBusinessType =Convert.toString(queryData.get("cmplBusinessType"));
	    String prodType =Convert.toString(queryData.get("prodType"));
		StringBuffer sql = new StringBuffer("select " +
				"SUM(NUM1)NUM1,DECODE(max(NUM2),0,0,ROUND(SUM(NUM1)/max(NUM2)*1000,2))NUM3 " +
	      		"from  CS_CMPL_PROBLEM_MON a," +
	      		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c " +
				"where a.PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and REGION_ID='"+zoneCode+"' "
		         + "and MONTH_ID='"+dateTime+"' "); 
			sql.append("and CMPL_BUSINESS_TYPE2_ID='"+cmplBusinessType+"' ");
		sql.append("group by month_id ");
          Map<String,Object> list=getDataAccess().queryForMap(sql.toString());
	      return list;
	}
	//本地投诉现象月报表（投诉现象不为空）_折线图
	public Map<String, Object> getChartData_CmplMon2(String dateTime,Map<String, Object> queryData){
	    String zoneCode =Convert.toString(queryData.get("zoneCode"));
	    String cmplBusinessType =Convert.toString(queryData.get("cmplBusinessType"));
	    String prodType =Convert.toString(queryData.get("prodType"));
		StringBuffer sql = new StringBuffer("select " +
				"SUM(NUM1)NUM1,DECODE(max(NUM2),0,0,ROUND(SUM(NUM1)/max(NUM2)*1000,2))NUM3 " +
	      		"from  CS_CMPL_PROBLEM_MON a," +
	      		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodType+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c " +
				"where a.PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and REGION_ID='"+zoneCode+"' "
		         + "and MONTH_ID='"+dateTime+"' "); 
			sql.append("and CMPL_BUSINESS_TYPE1_ID='"+cmplBusinessType+"' ");
		sql.append("group by month_id ");
          Map<String,Object> list=getDataAccess().queryForMap(sql.toString());
	      return list;
	}
	//越级、本地投诉原因月报表(投诉原因为空)
	public Map<String, Object> getChartData_CmplReasonMon(String dateTime,Map<String, Object> queryData){
	    String zoneCode =Convert.toString(queryData.get("zoneCode"));
	    String channelType =Convert.toString(queryData.get("channelType"));
	    String reasonId =Convert.toString(queryData.get("reasonId"));
	    String tabStr=Convert.toString(queryData.get("tabStr"));
		StringBuffer sql = new StringBuffer("select " +
				"SUM(NUM1)NUM1,DECODE(max(NUM2),0,0,ROUND(SUM(NUM1)/max(NUM2)*1000,2))NUM3 " +
	      		"from  "+tabStr+"  " +
	      		"where MONTH_ID='"+dateTime+"' "); 
		sql.append("and REGION_ID='"+zoneCode+"' ");
		if(reasonId!=null&&!"".equals(reasonId)){
			sql.append("and CMPL_REASON1_ID='"+reasonId+"' ");
		}
		if(channelType!=null&&!"".equals(channelType)){
			sql.append(" and CHANNEL_TYPE_ID='"+channelType+"' ");
		}
		sql.append(" group by month_id");
          Map<String,Object> list=getDataAccess().queryForMap(sql.toString());
	      return list;
	}
	//越级、本地投诉原因月报表（投诉原因不为空）_柱状图
	public Map<String, Object> getChartData_CmplReasonMon1(String dateTime,Map<String, Object> queryData){
	    String zoneCode =Convert.toString(queryData.get("zoneCode"));
	    String reasonId =Convert.toString(queryData.get("reasonId"));
	    String channelType =Convert.toString(queryData.get("channelType"));
	    String tabStr=Convert.toString(queryData.get("tabStr"));
		StringBuffer sql = new StringBuffer("select " +
				"SUM(NUM1)NUM1,DECODE(max(NUM2),0,0,ROUND(SUM(NUM1)/max(NUM2)*1000,2))NUM3 " +
	      		"from  "+tabStr+"  " +
	      		"where MONTH_ID='"+dateTime+"' "); 
		    sql.append("and REGION_ID='"+zoneCode+"' ");
			sql.append("and CMPL_REASON2_ID='"+reasonId+"' ");
		if(channelType!=null&&!"".equals(channelType)){
			sql.append("and CHANNEL_TYPE_ID='"+channelType+"' ");
		}
		sql.append("group by month_id ");
          Map<String,Object> list=getDataAccess().queryForMap(sql.toString());
	      return list;
	}
	//越级、本地投诉原因月报表（投诉原因不为空）_折线图
	public Map<String, Object> getChartData_CmplReasonMon2(String dateTime,Map<String, Object> queryData){
	    String zoneCode =Convert.toString(queryData.get("zoneCode"));
	    String reasonId =Convert.toString(queryData.get("reasonId"));
	    String channelType =Convert.toString(queryData.get("channelType"));
	    String tabStr=Convert.toString(queryData.get("tabStr"));
		StringBuffer sql = new StringBuffer("select " +
				"SUM(NUM1)NUM1,DECODE(max(NUM2),0,0,ROUND(SUM(NUM1)/max(NUM2)*1000,2))NUM3 " +
	      		"from  "+tabStr+"  " +
	      		"where  MONTH_ID='"+dateTime+"' "); 
			sql.append("and CMPL_REASON1_ID='"+reasonId+"' ");
			sql.append("and REGION_ID='"+zoneCode+"' ");
		if(channelType!=null&&!"".equals(channelType)){
			sql.append("and CHANNEL_TYPE_ID='"+channelType+"' ");
		}
		sql.append("group by month_id ");
          Map<String,Object> list=getDataAccess().queryForMap(sql.toString());
	      return list;
	}
}
