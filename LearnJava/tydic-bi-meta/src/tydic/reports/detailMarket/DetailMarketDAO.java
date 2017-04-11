package tydic.reports.detailMarket;

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

import jxl.write.DateTime;

import oracle.jdbc.OracleTypes;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DBOutParameter;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.yhd.constant.ConstantStoreProc;
import tydic.portalCommon.DateUtil;

public class DetailMarketDAO extends MetaBaseDAO {
	//六大细分市场日报最大日期
	public String getMaxDate(String tabStr) {
		String sql = "select max(t.V_DATE)day_id from "+tabStr+" t where DATE_TYPE='0' ";
		return getDataAccess().queryForString(sql);
	}
	//六大细分市场月报最大月
	public String getMaxMonth(String tabStr) {
		String sql = "select max(t.V_DATE)month_id from "+tabStr+" t where DATE_TYPE='2' ";
		return getDataAccess().queryForString(sql);
	}
	//六大细分市场月列表
	public List<Map<String, Object>> getMonthList(String tabStr) {
		String sql = "select distinct t.V_DATE month_id from "+tabStr+" t where DATE_TYPE='2' order by month_id desc ";
		return getDataAccess().queryForList(sql);
	}
	//六大细分市场产品类别列表
	public List<Map<String, Object>> getProdList() {
		String sql = "select distinct CMPL_PROD_TYPE_NAME,CMPL_PROD_TYPE_CODE  from META_DM.D_CMPL_PROD_TYPE " +
				"where DIM_LEVEL=1 order by CMPL_PROD_TYPE_CODE,cmpl_prod_type_name ";
		return getDataAccess().queryForList(sql);
	}
	//六大细分市场服务类别列表
	public List<Map<String, Object>> getServList() {
		String sql = "select distinct CMPL_BUSI_TYPE_NAME,CMPL_BUSI_TYPE_CODE  from meta_dm.D_CMPL_BUSINESS_TYPE " +
				"where DIM_LEVEL=1 order by CMPL_BUSI_TYPE_CODE,cmpl_busi_type_name ";
		return getDataAccess().queryForList(sql);
	}
	//细分市场存储过程
	public Map<String, Object> queryDetailMarket_pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String rptType =Convert.toString(queryData.get("rptType"));
		String storeName = "";
		if("1".equals(rptType)){
			storeName=ConstantStoreProc.RPT_CS_COMPLAIN_MAIN1;
		}if("2".equals(rptType)){
			storeName=ConstantStoreProc.RPT_CS_COMPLAIN_MAIN2;
		}if("3".equals(rptType)){
			storeName=ConstantStoreProc.RPT_CS_COMPLAIN_MAIN3;
		}if("4".equals(rptType)){
			storeName=ConstantStoreProc.RPT_CS_COMPLAIN_MAIN4;
		}if("5".equals(rptType)){
			storeName=ConstantStoreProc.RPT_CS_COMPLAIN_MAIN5;
		}if("6".equals(rptType)){
			storeName=ConstantStoreProc.RPT_CS_COMPLAIN_MAIN6;
		}if("7".equals(rptType)){
			storeName=ConstantStoreProc.RPT_CS_COMPLAIN_MAIN7;
		}if("8".equals(rptType)){
			storeName=ConstantStoreProc.RPT_CS_COMPLAIN_MAIN8;
		}
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
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
	//细分市场报表饼图列表--产品类别小计
	public List<Map<String, Object>> getDetailMarket1_PieChartList1(Map<String, Object> queryData) throws ParseException{
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String startDate = Convert.toString(queryData.get("startDate")).replaceAll("-", "");
		String dateTime="";
		String inYear=startDate.substring(0,4);
		String inDay=startDate.substring(4,6);
		String inYearMon=startDate.substring(0,6);
		String lastYearMon=DateUtil.getLastMon(inYearMon);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date d1 = sdf.parse("2014-08");
		Date d2 = sdf.parse(inYear+"-"+inDay);
		if(d1.getTime()-d2.getTime()<0){ 
			dateTime=lastYearMon;
		}else{
			dateTime="201408";
		}
		String dateType = Convert.toString(queryData.get("dateType"));
		StringBuffer sql = new StringBuffer("select b.cmpl_prod_type_name cmpl_prod_type_name,b.cmpl_prod_type_code cmpl_prod_type_code," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then SUM_NUM end )), '0') MARKET1_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then SUM_NUM end )), '0') MARKET2_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then SUM_NUM end )), '0') MARKET3_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then SUM_NUM end )), '0') MARKET4_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then SUM_NUM end )), '0') MARKET5_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then SUM_NUM end )), '0') MARKET6_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl in('1','2','3','4','5','6') then SUM_NUM end )), '0') MARKET_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then CMPL_NUM end )), '0') MARKET1_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then CMPL_NUM end )), '0') MARKET2_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then CMPL_NUM end )), '0') MARKET3_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then CMPL_NUM end )), '0') MARKET4_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then CMPL_NUM end )), '0') MARKET5_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then CMPL_NUM end )), '0') MARKET6_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl in('1','2','3','4','5','6') then CMPL_NUM end )), '0') MARKET_CMPL " +
				"from CS_COMPLAIN_MAIN a,meta_dm.D_CMPL_PROD_TYPE b,meta_dm.dm_divide_market_six s " +
				"where a.CMPL_PROD_TYPE_ID like b.cmpl_prod_type_code || '%' and b.dim_level=1 " +
				"and a.divide_market_6=s.divide_market_6(+) " +
				"and a.REGION_ID='"+zoneCode+"'  and a.v_date='"+startDate+"' and DATE_TYPE='"+dateType+"'" +
				"group by b.cmpl_prod_type_code,b.cmpl_prod_type_name order by b.cmpl_prod_type_code,b.cmpl_prod_type_name ");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//细分市场报表饼图列表--服务类别合计
	public List<Map<String, Object>> getDetailMarket1_PieChartList2(Map<String, Object> queryData) throws ParseException{
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String startDate = Convert.toString(queryData.get("startDate")).replaceAll("-", "");
		String dateTime="";
		String inYear=startDate.substring(0,4);
		String inDay=startDate.substring(4,6);
		String inYearMon=startDate.substring(0,6);
		String lastYearMon=DateUtil.getLastMon(inYearMon);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date d1 = sdf.parse("2014-08");
		Date d2 = sdf.parse(inYear+"-"+inDay);
		if(d1.getTime()-d2.getTime()<0){ 
			dateTime=lastYearMon;
		}else{
			dateTime="201408";
		}
		String dateType = Convert.toString(queryData.get("dateType"));
		StringBuffer sql = new StringBuffer("select b.cmpl_busi_type_name cmpl_busi_type_name,b.cmpl_busi_type_code cmpl_busi_type_code," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then SUM_NUM end )), '0') MARKET1_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then SUM_NUM end )), '0') MARKET2_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then SUM_NUM end )), '0') MARKET3_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then SUM_NUM end )), '0') MARKET4_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then SUM_NUM end )), '0') MARKET5_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then SUM_NUM end )), '0') MARKET6_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl in('1','2','3','4','5','6') then SUM_NUM end )), '0') MARKET_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then CMPL_NUM end )), '0') MARKET1_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then CMPL_NUM end )), '0') MARKET2_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then CMPL_NUM end )), '0') MARKET3_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then CMPL_NUM end )), '0') MARKET4_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then CMPL_NUM end )), '0') MARKET5_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then CMPL_NUM end )), '0') MARKET6_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl in('1','2','3','4','5','6') then CMPL_NUM end )), '0') MARKET_CMPL " +
				"from CS_COMPLAIN_MAIN a, meta_dm.d_cmpl_business_type b,meta_dm.dm_divide_market_six s " +
				"where a.CMPL_BUSINESS_TYPE_ID like b.cmpl_busi_type_code||'%' and b.dim_level=1 " +
				"and a.divide_market_6=s.divide_market_6(+) " +
				"and a.REGION_ID='"+zoneCode+"'  and a.v_date='"+startDate+"' and DATE_TYPE='"+dateType+"'" +
				"group by cmpl_busi_type_code,cmpl_busi_type_name order by cmpl_busi_type_code,cmpl_busi_type_name ");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//细分市场名称
	public String getDetailsMarketName(String detailsId) {
		String sql = "select t.divide_market_6_dl_name from meta_dm.dm_divide_market_six t where t.divide_market_6_dl='"+detailsId+"'";
		return getDataAccess().queryForString(sql);
	}
	 public  Map<String,Object> getDetailMarket2_PieData(String marketId,Map<String,Object> map) throws ParseException {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
	     String startDate = Convert.toString(map.get("startDate")).replaceAll("-", "");
	     String dateType = Convert.toString(map.get("dateType"));
	     String dateTime="";
			String inYear=startDate.substring(0,4);
			String inDay=startDate.substring(4,6);
			String inYearMon=startDate.substring(0,6);
			String lastYearMon=DateUtil.getLastMon(inYearMon);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date d1 = sdf.parse("2014-08");
			Date d2 = sdf.parse(inYear+"-"+inDay);
			if(d1.getTime()-d2.getTime()<0){ 
				dateTime=lastYearMon;
			}else{
				dateTime="201408";
			}
		 StringBuffer sql = new StringBuffer("select to_char(MARKET"+marketId+"_SUM) BYL," +
		 		        "to_char(MARKET"+marketId+"_CMPL)tsl from (" +
		 				"select region_id,NVL(TO_CHAR(sum(case when s.divide_market_6_dl='"+marketId+"' then SUM_NUM end )), '0') MARKET"+marketId+"_SUM," +
		 				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='"+marketId+"' then CMPL_NUM end )), '0') MARKET"+marketId+"_CMPL " +
		 				"from CS_COMPLAIN_MAIN a,meta_dm.dm_divide_market_six s " +
		 				"where a.divide_market_6=s.divide_market_6(+) " +
		 				"and a.REGION_ID='"+zoneCode+"'  and a.v_date='"+startDate+"' and DATE_TYPE='"+dateType+"' group by region_id) a " +
		 				"left join meta_user.cs_cust_jfyhs_mon m on a.region_id=m.region_id and m.month_id=substr('"+dateTime+"',0,6)");
	  return getDataAccess().queryForMap(sql.toString());
 }
	 public  Map<String,Object> getDetailMarket2_LineData(String inTime,Map<String,Object> map) throws ParseException {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
	     String startDate = Convert.toString(map.get("startDate")).replaceAll("-", "");
	     String dateType = Convert.toString(map.get("dateType"));
	     String dateTime="";
			String inYear=startDate.substring(0,4);
			String inDay=startDate.substring(4,6);
			String inYearMon=startDate.substring(0,6);
			String lastYearMon=DateUtil.getLastMon(inYearMon);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date d1 = sdf.parse("2014-08");
			Date d2 = sdf.parse(inYear+"-"+inDay);
			if(d1.getTime()-d2.getTime()<0){ 
				dateTime=lastYearMon;
			}else{
				dateTime="201408";
			}
		 StringBuffer sql = new StringBuffer("select to_char(decode(USER_MARKET1,'0','0',round(MARKET1_SUM/USER_MARKET1*1000,2)),'FM99990.00') SHOOL_MARKET_SUM," +
				"to_char(decode(USER_MARKET2,'0','0',round(MARKET2_SUM/USER_MARKET2*1000,2)),'FM99990.00') COUNTRY_MARKET_SUM," +
				"to_char(decode(USER_MARKET3,'0','0',round(MARKET3_SUM/USER_MARKET3*1000,2)),'FM99990.00') BIG_CUSTOMER_SUM," +
				"to_char(decode(USER_MARKET4,'0','0',round(MARKET4_SUM/USER_MARKET4*1000,2)),'FM99990.00') BUSINESS_CUSTOMER_SUM," +
				"to_char(decode(USER_MARKET5,'0','0',round(MARKET5_SUM/USER_MARKET5*1000,2)),'FM99990.00') CITY_FAMILY_SUM," +
				"to_char(decode(USER_MARKET6,'0','0',round(MARKET6_SUM/USER_MARKET6*1000,2)),'FM99990.00') FLOW_MARKET_SUM," +
				"to_char(decode(USER_MARKET1,'0','0',round(MARKET1_CMPL/USER_MARKET1*1000,2)),'FM99990.00') SHOOL_MARKET_CMPL," +
				"to_char(decode(USER_MARKET2,'0','0',round(MARKET1_CMPL/USER_MARKET2*1000,2)),'FM99990.00') COUNTRY_MARKET_CMPL," +
				"to_char(decode(USER_MARKET3,'0','0',round(MARKET2_CMPL/USER_MARKET3*1000,2)),'FM99990.00') BIG_CUSTOMER_CMPL," +
				"to_char(decode(USER_MARKET4,'0','0',round(MARKET3_CMPL/USER_MARKET4*1000,2)),'FM99990.00') BUSINESS_CUSTOMER_CMPL," +
				"to_char(decode(USER_MARKET5,'0','0',round(MARKET4_CMPL/USER_MARKET5*1000,2)),'FM99990.00') CITY_FAMILY_CMPL," +
				"to_char(decode(USER_MARKET6,'0','0',round(MARKET5_CMPL/USER_MARKET6*1000,2)),'FM99990.00') FLOW_MARKET_CMPL " +
				"from (select region_id," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then SUM_NUM end )), '0') MARKET1_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then SUM_NUM end )), '0') MARKET2_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then SUM_NUM end )), '0') MARKET3_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then SUM_NUM end )), '0') MARKET4_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then SUM_NUM end )), '0') MARKET5_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then SUM_NUM end )), '0') MARKET6_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then CMPL_NUM end )), '0') MARKET1_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then CMPL_NUM end )), '0') MARKET2_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then CMPL_NUM end )), '0') MARKET3_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then CMPL_NUM end )), '0') MARKET4_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then CMPL_NUM end )), '0') MARKET5_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then CMPL_NUM end )), '0') MARKET6_CMPL " +
				"from CS_COMPLAIN_MAIN a,meta_dm.dm_divide_market_six s " +
				"where a.divide_market_6=s.divide_market_6(+) " +
				"and a.REGION_ID='"+zoneCode+"'  and a.v_date='"+inTime+"' and DATE_TYPE='"+dateType+"'" +
				"group by region_id) a  " +
				"left join meta_user.cs_cust_jfyhs_mon m on a.region_id=m.region_id and m.month_id=substr('"+dateTime+"',0,6)");
	  return getDataAccess().queryForMap(sql.toString());
 }
	 public  List<Map<String, Object>>getDetailMarket2_barChart(Map<String,Object> map) throws ParseException {
	     String startDate = Convert.toString(map.get("startDate")).replaceAll("-", "");
	     String dateType = Convert.toString(map.get("dateType"));
	     String dateTime="";
			String inYear=startDate.substring(0,4);
			String inDay=startDate.substring(4,6);
			String inYearMon=startDate.substring(0,6);
			String lastYearMon=DateUtil.getLastMon(inYearMon);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date d1 = sdf.parse("2014-08");
			Date d2 = sdf.parse(inYear+"-"+inDay);
			if(d1.getTime()-d2.getTime()<0){ 
				dateTime=lastYearMon;
			}else{
				dateTime="201408";
			}
		 StringBuffer sql = new StringBuffer("select a.REGION_ID,a.REGION_NAME,to_char(decode(USER_MARKET1,'0','0',round(MARKET1_SUM/USER_MARKET1*1000,2)),'FM99990.00') SHOOL_MARKET_SUM," +
				"to_char(decode(USER_MARKET2,'0','0',round(MARKET2_SUM/USER_MARKET2*1000,2)),'FM99990.00') COUNTRY_MARKET_SUM," +
				"to_char(decode(USER_MARKET3,'0','0',round(MARKET3_SUM/USER_MARKET3*1000,2)),'FM99990.00') BIG_CUSTOMER_SUM," +
				"to_char(decode(USER_MARKET4,'0','0',round(MARKET4_SUM/USER_MARKET4*1000,2)),'FM99990.00') BUSINESS_CUSTOMER_SUM," +
				"to_char(decode(USER_MARKET5,'0','0',round(MARKET5_SUM/USER_MARKET5*1000,2)),'FM99990.00') CITY_FAMILY_SUM," +
				"to_char(decode(USER_MARKET6,'0','0',round(MARKET6_SUM/USER_MARKET6*1000,2)),'FM99990.00') FLOW_MARKET_SUM," +
				"to_char(decode(USER_MARKET1,'0','0',round(MARKET1_CMPL/USER_MARKET1*1000,2)),'FM99990.00') SHOOL_MARKET_CMPL," +
				"to_char(decode(USER_MARKET2,'0','0',round(MARKET1_CMPL/USER_MARKET2*1000,2)),'FM99990.00') COUNTRY_MARKET_CMPL," +
				"to_char(decode(USER_MARKET3,'0','0',round(MARKET2_CMPL/USER_MARKET3*1000,2)),'FM99990.00') BIG_CUSTOMER_CMPL," +
				"to_char(decode(USER_MARKET4,'0','0',round(MARKET3_CMPL/USER_MARKET4*1000,2)),'FM99990.00') BUSINESS_CUSTOMER_CMPL," +
				"to_char(decode(USER_MARKET5,'0','0',round(MARKET4_CMPL/USER_MARKET5*1000,2)),'FM99990.00') CITY_FAMILY_CMPL," +
				"to_char(decode(USER_MARKET6,'0','0',round(MARKET5_CMPL/USER_MARKET6*1000,2)),'FM99990.00') FLOW_MARKET_CMPL " +
				"from (select region_id,region_name,order_id," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then SUM_NUM end )), '0') MARKET1_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then SUM_NUM end )), '0') MARKET2_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then SUM_NUM end )), '0') MARKET3_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then SUM_NUM end )), '0') MARKET4_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then SUM_NUM end )), '0') MARKET5_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then SUM_NUM end )), '0') MARKET6_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then CMPL_NUM end )), '0') MARKET1_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then CMPL_NUM end )), '0') MARKET2_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then CMPL_NUM end )), '0') MARKET3_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then CMPL_NUM end )), '0') MARKET4_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then CMPL_NUM end )), '0') MARKET5_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then CMPL_NUM end )), '0') MARKET6_CMPL " +
				"from CS_COMPLAIN_MAIN a,(select zone_code,order_id from meta_dim_zone where dim_level=3)b,meta_dm.dm_divide_market_six s " +
				"where a.divide_market_6=s.divide_market_6(+) and a.region_id=b.zone_code " +
				"and a.v_date='"+startDate+"' and DATE_TYPE='"+dateType+"'" +
				"group by REGION_ID,REGION_NAME,order_id order by order_id) a  " +
				"left join meta_user.cs_cust_jfyhs_mon m on a.region_id=m.region_id and m.month_id=substr('"+dateTime+"',0,6)");
	  return getDataAccess().queryForList(sql.toString());
 }
  public  List<Map<String, Object>>getDetailMarket3_barChart1(Map<String,Object> map) throws ParseException {
	     String startDate = Convert.toString(map.get("startDate")).replaceAll("-", "");
	     String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
	     String dateType = Convert.toString(map.get("dateType"));
	     String dateTime="";
			String inYear=startDate.substring(0,4);
			String inDay=startDate.substring(4,6);
			String inYearMon=startDate.substring(0,6);
			String lastYearMon=DateUtil.getLastMon(inYearMon);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date d1 = sdf.parse("2014-08");
			Date d2 = sdf.parse(inYear+"-"+inDay);
			if(d1.getTime()-d2.getTime()<0){ 
				dateTime=lastYearMon;
			}else{
				dateTime="201408";
			}
		 StringBuffer sql = new StringBuffer("select a.cmpl_prod_type_name,a.cmpl_prod_type_code,to_char(decode(USER_MARKET1,'0','0',round(MARKET1_SUM/USER_MARKET1*1000,2)),'FM99990.00') SHOOL_MARKET_SUM," +
				"to_char(decode(USER_MARKET2,'0','0',round(MARKET2_SUM/USER_MARKET2*1000,2)),'FM99990.00') COUNTRY_MARKET_SUM," +
				"to_char(decode(USER_MARKET3,'0','0',round(MARKET3_SUM/USER_MARKET3*1000,2)),'FM99990.00') BIG_CUSTOMER_SUM," +
				"to_char(decode(USER_MARKET4,'0','0',round(MARKET4_SUM/USER_MARKET4*1000,2)),'FM99990.00') BUSINESS_CUSTOMER_SUM," +
				"to_char(decode(USER_MARKET5,'0','0',round(MARKET5_SUM/USER_MARKET5*1000,2)),'FM99990.00') CITY_FAMILY_SUM," +
				"to_char(decode(USER_MARKET6,'0','0',round(MARKET6_SUM/USER_MARKET6*1000,2)),'FM99990.00') FLOW_MARKET_SUM," +
				"to_char(decode(USER_MARKET1,'0','0',round(MARKET1_CMPL/USER_MARKET1*1000,2)),'FM99990.00') SHOOL_MARKET_CMPL," +
				"to_char(decode(USER_MARKET2,'0','0',round(MARKET1_CMPL/USER_MARKET2*1000,2)),'FM99990.00') COUNTRY_MARKET_CMPL," +
				"to_char(decode(USER_MARKET3,'0','0',round(MARKET2_CMPL/USER_MARKET3*1000,2)),'FM99990.00') BIG_CUSTOMER_CMPL," +
				"to_char(decode(USER_MARKET4,'0','0',round(MARKET3_CMPL/USER_MARKET4*1000,2)),'FM99990.00') BUSINESS_CUSTOMER_CMPL," +
				"to_char(decode(USER_MARKET5,'0','0',round(MARKET4_CMPL/USER_MARKET5*1000,2)),'FM99990.00') CITY_FAMILY_CMPL," +
				"to_char(decode(USER_MARKET6,'0','0',round(MARKET5_CMPL/USER_MARKET6*1000,2)),'FM99990.00') FLOW_MARKET_CMPL " +
				"from (select b.cmpl_prod_type_name cmpl_prod_type_name,b.cmpl_prod_type_code cmpl_prod_type_code,region_id," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then SUM_NUM end )), '0') MARKET1_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then SUM_NUM end )), '0') MARKET2_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then SUM_NUM end )), '0') MARKET3_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then SUM_NUM end )), '0') MARKET4_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then SUM_NUM end )), '0') MARKET5_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then SUM_NUM end )), '0') MARKET6_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then CMPL_NUM end )), '0') MARKET1_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then CMPL_NUM end )), '0') MARKET2_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then CMPL_NUM end )), '0') MARKET3_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then CMPL_NUM end )), '0') MARKET4_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then CMPL_NUM end )), '0') MARKET5_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then CMPL_NUM end )), '0') MARKET6_CMPL " +
				"from CS_COMPLAIN_MAIN a,meta_dm.D_CMPL_PROD_TYPE b,meta_dm.dm_divide_market_six s " +
				"where a.divide_market_6=s.divide_market_6(+) and a.CMPL_PROD_TYPE_ID like b.cmpl_prod_type_code||'%' " +
				"and b.dim_level='1' and a.v_date='"+startDate+"' and DATE_TYPE='"+dateType+"' and region_id='"+zoneCode+"'" +
				"group by cmpl_prod_type_name,cmpl_prod_type_code,region_id order by cmpl_prod_type_code) a  " +
				"left join meta_user.cs_cust_jfyhs_mon m on a.region_id=m.region_id and m.month_id=substr('"+dateTime+"',0,6)");
	  return getDataAccess().queryForList(sql.toString());
 }
  public  List<Map<String, Object>>getDetailMarket3_barChart2(Map<String,Object> map) throws ParseException {
	     String startDate = Convert.toString(map.get("startDate")).replaceAll("-", "");
	     String dateType = Convert.toString(map.get("dateType"));
	     String cmplProdTypeId = Convert.toString(map.get("cmplProdTypeId"));
	     String dateTime="";
			String inYear=startDate.substring(0,4);
			String inDay=startDate.substring(4,6);
			String inYearMon=startDate.substring(0,6);
			String lastYearMon=DateUtil.getLastMon(inYearMon);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date d1 = sdf.parse("2014-08");
			Date d2 = sdf.parse(inYear+"-"+inDay);
			if(d1.getTime()-d2.getTime()<0){ 
				dateTime=lastYearMon;
			}else{
				dateTime="201408";
			}
		 StringBuffer sql = new StringBuffer("select a.REGION_ID,a.REGION_NAME,to_char(decode(USER_MARKET1,'0','0',round(MARKET1_SUM/USER_MARKET1*1000,2)),'FM99990.00') SHOOL_MARKET_SUM," +
				"to_char(decode(USER_MARKET2,'0','0',round(MARKET2_SUM/USER_MARKET2*1000,2)),'FM99990.00') COUNTRY_MARKET_SUM," +
				"to_char(decode(USER_MARKET3,'0','0',round(MARKET3_SUM/USER_MARKET3*1000,2)),'FM99990.00') BIG_CUSTOMER_SUM," +
				"to_char(decode(USER_MARKET4,'0','0',round(MARKET4_SUM/USER_MARKET4*1000,2)),'FM99990.00') BUSINESS_CUSTOMER_SUM," +
				"to_char(decode(USER_MARKET5,'0','0',round(MARKET5_SUM/USER_MARKET5*1000,2)),'FM99990.00') CITY_FAMILY_SUM," +
				"to_char(decode(USER_MARKET6,'0','0',round(MARKET6_SUM/USER_MARKET6*1000,2)),'FM99990.00') FLOW_MARKET_SUM," +
				"to_char(decode(USER_MARKET1,'0','0',round(MARKET1_CMPL/USER_MARKET1*1000,2)),'FM99990.00') SHOOL_MARKET_CMPL," +
				"to_char(decode(USER_MARKET2,'0','0',round(MARKET1_CMPL/USER_MARKET2*1000,2)),'FM99990.00') COUNTRY_MARKET_CMPL," +
				"to_char(decode(USER_MARKET3,'0','0',round(MARKET2_CMPL/USER_MARKET3*1000,2)),'FM99990.00') BIG_CUSTOMER_CMPL," +
				"to_char(decode(USER_MARKET4,'0','0',round(MARKET3_CMPL/USER_MARKET4*1000,2)),'FM99990.00') BUSINESS_CUSTOMER_CMPL," +
				"to_char(decode(USER_MARKET5,'0','0',round(MARKET4_CMPL/USER_MARKET5*1000,2)),'FM99990.00') CITY_FAMILY_CMPL," +
				"to_char(decode(USER_MARKET6,'0','0',round(MARKET5_CMPL/USER_MARKET6*1000,2)),'FM99990.00') FLOW_MARKET_CMPL " +
				"from (select region_id,region_name,order_id," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then SUM_NUM end )), '0') MARKET1_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then SUM_NUM end )), '0') MARKET2_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then SUM_NUM end )), '0') MARKET3_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then SUM_NUM end )), '0') MARKET4_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then SUM_NUM end )), '0') MARKET5_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then SUM_NUM end )), '0') MARKET6_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then CMPL_NUM end )), '0') MARKET1_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then CMPL_NUM end )), '0') MARKET2_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then CMPL_NUM end )), '0') MARKET3_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then CMPL_NUM end )), '0') MARKET4_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then CMPL_NUM end )), '0') MARKET5_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then CMPL_NUM end )), '0') MARKET6_CMPL " +
				"from CS_COMPLAIN_MAIN a,(select zone_code,order_id from meta_dim_zone where dim_level=3)b,meta_dm.dm_divide_market_six s " +
				"where a.divide_market_6=s.divide_market_6(+) and a.region_id=b.zone_code and a.CMPL_PROD_TYPE_ID like '"+cmplProdTypeId+"%' " +
				"and a.v_date='"+startDate+"' and DATE_TYPE='"+dateType+"' " +
				"group by REGION_ID,REGION_NAME,order_id order by order_id) a  " +
				"left join meta_user.cs_cust_jfyhs_mon m on a.region_id=m.region_id and m.month_id=substr('"+dateTime+"',0,6)");
	  return getDataAccess().queryForList(sql.toString());
}
  public  List<Map<String, Object>>getDetailMarket4_barChart1(Map<String,Object> map) throws ParseException {
	     String startDate = Convert.toString(map.get("startDate")).replaceAll("-", "");
	     String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
	     String dateType = Convert.toString(map.get("dateType"));
	     String dateTime="";
			String inYear=startDate.substring(0,4);
			String inDay=startDate.substring(4,6);
			String inYearMon=startDate.substring(0,6);
			String lastYearMon=DateUtil.getLastMon(inYearMon);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date d1 = sdf.parse("2014-08");
			Date d2 = sdf.parse(inYear+"-"+inDay);
			if(d1.getTime()-d2.getTime()<0){ 
				dateTime=lastYearMon;
			}else{
				dateTime="201408";
			}
		 StringBuffer sql = new StringBuffer("select a.cmpl_busi_type_name,a.cmpl_busi_type_code,to_char(decode(USER_MARKET1,'0','0',round(MARKET1_SUM/USER_MARKET1*1000,2)),'FM99990.00') SHOOL_MARKET_SUM," +
				"to_char(decode(USER_MARKET2,'0','0',round(MARKET2_SUM/USER_MARKET2*1000,2)),'FM99990.00') COUNTRY_MARKET_SUM," +
				"to_char(decode(USER_MARKET3,'0','0',round(MARKET3_SUM/USER_MARKET3*1000,2)),'FM99990.00') BIG_CUSTOMER_SUM," +
				"to_char(decode(USER_MARKET4,'0','0',round(MARKET4_SUM/USER_MARKET4*1000,2)),'FM99990.00') BUSINESS_CUSTOMER_SUM," +
				"to_char(decode(USER_MARKET5,'0','0',round(MARKET5_SUM/USER_MARKET5*1000,2)),'FM99990.00') CITY_FAMILY_SUM," +
				"to_char(decode(USER_MARKET6,'0','0',round(MARKET6_SUM/USER_MARKET6*1000,2)),'FM99990.00') FLOW_MARKET_SUM," +
				"to_char(decode(USER_MARKET1,'0','0',round(MARKET1_CMPL/USER_MARKET1*1000,2)),'FM99990.00') SHOOL_MARKET_CMPL," +
				"to_char(decode(USER_MARKET2,'0','0',round(MARKET1_CMPL/USER_MARKET2*1000,2)),'FM99990.00') COUNTRY_MARKET_CMPL," +
				"to_char(decode(USER_MARKET3,'0','0',round(MARKET2_CMPL/USER_MARKET3*1000,2)),'FM99990.00') BIG_CUSTOMER_CMPL," +
				"to_char(decode(USER_MARKET4,'0','0',round(MARKET3_CMPL/USER_MARKET4*1000,2)),'FM99990.00') BUSINESS_CUSTOMER_CMPL," +
				"to_char(decode(USER_MARKET5,'0','0',round(MARKET4_CMPL/USER_MARKET5*1000,2)),'FM99990.00') CITY_FAMILY_CMPL," +
				"to_char(decode(USER_MARKET6,'0','0',round(MARKET5_CMPL/USER_MARKET6*1000,2)),'FM99990.00') FLOW_MARKET_CMPL " +
				"from (select b.cmpl_busi_type_name cmpl_busi_type_name,b.cmpl_busi_type_code cmpl_busi_type_code,region_id," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then SUM_NUM end )), '0') MARKET1_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then SUM_NUM end )), '0') MARKET2_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then SUM_NUM end )), '0') MARKET3_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then SUM_NUM end )), '0') MARKET4_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then SUM_NUM end )), '0') MARKET5_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then SUM_NUM end )), '0') MARKET6_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then CMPL_NUM end )), '0') MARKET1_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then CMPL_NUM end )), '0') MARKET2_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then CMPL_NUM end )), '0') MARKET3_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then CMPL_NUM end )), '0') MARKET4_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then CMPL_NUM end )), '0') MARKET5_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then CMPL_NUM end )), '0') MARKET6_CMPL " +
				"from CS_COMPLAIN_MAIN a,meta_dm.d_cmpl_business_type b,meta_dm.dm_divide_market_six s " +
				"where a.divide_market_6=s.divide_market_6(+) and a.CMPL_BUSINESS_TYPE_ID like b.cmpl_busi_type_code||'%' " +
				"and b.dim_level='1' and a.v_date='"+startDate+"' and DATE_TYPE='"+dateType+"' and region_id='"+zoneCode+"'" +
				"group by cmpl_busi_type_name,cmpl_busi_type_code,region_id order by cmpl_busi_type_code) a  " +
				"left join meta_user.cs_cust_jfyhs_mon m on a.region_id=m.region_id and m.month_id=substr('"+dateTime+"',0,6)");
	  return getDataAccess().queryForList(sql.toString());
}
public  List<Map<String, Object>>getDetailMarket4_barChart2(Map<String,Object> map) throws ParseException {
	     String startDate = Convert.toString(map.get("startDate")).replaceAll("-", "");
	     String dateType = Convert.toString(map.get("dateType"));
	     String servTypeId = Convert.toString(map.get("servTypeId"));
	     String dateTime="";
			String inYear=startDate.substring(0,4);
			String inDay=startDate.substring(4,6);
			String inYearMon=startDate.substring(0,6);
			String lastYearMon=DateUtil.getLastMon(inYearMon);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			Date d1 = sdf.parse("2014-08");
			Date d2 = sdf.parse(inYear+"-"+inDay);
			if(d1.getTime()-d2.getTime()<0){ 
				dateTime=lastYearMon;
			}else{
				dateTime="201408";
			}
		 StringBuffer sql = new StringBuffer("select a.REGION_ID,a.REGION_NAME,to_char(decode(USER_MARKET1,'0','0',round(MARKET1_SUM/USER_MARKET1*1000,2)),'FM99990.00') SHOOL_MARKET_SUM," +
				"to_char(decode(USER_MARKET2,'0','0',round(MARKET2_SUM/USER_MARKET2*1000,2)),'FM99990.00') COUNTRY_MARKET_SUM," +
				"to_char(decode(USER_MARKET3,'0','0',round(MARKET3_SUM/USER_MARKET3*1000,2)),'FM99990.00') BIG_CUSTOMER_SUM," +
				"to_char(decode(USER_MARKET4,'0','0',round(MARKET4_SUM/USER_MARKET4*1000,2)),'FM99990.00') BUSINESS_CUSTOMER_SUM," +
				"to_char(decode(USER_MARKET5,'0','0',round(MARKET5_SUM/USER_MARKET5*1000,2)),'FM99990.00') CITY_FAMILY_SUM," +
				"to_char(decode(USER_MARKET6,'0','0',round(MARKET6_SUM/USER_MARKET6*1000,2)),'FM99990.00') FLOW_MARKET_SUM," +
				"to_char(decode(USER_MARKET1,'0','0',round(MARKET1_CMPL/USER_MARKET1*1000,2)),'FM99990.00') SHOOL_MARKET_CMPL," +
				"to_char(decode(USER_MARKET2,'0','0',round(MARKET1_CMPL/USER_MARKET2*1000,2)),'FM99990.00') COUNTRY_MARKET_CMPL," +
				"to_char(decode(USER_MARKET3,'0','0',round(MARKET2_CMPL/USER_MARKET3*1000,2)),'FM99990.00') BIG_CUSTOMER_CMPL," +
				"to_char(decode(USER_MARKET4,'0','0',round(MARKET3_CMPL/USER_MARKET4*1000,2)),'FM99990.00') BUSINESS_CUSTOMER_CMPL," +
				"to_char(decode(USER_MARKET5,'0','0',round(MARKET4_CMPL/USER_MARKET5*1000,2)),'FM99990.00') CITY_FAMILY_CMPL," +
				"to_char(decode(USER_MARKET6,'0','0',round(MARKET5_CMPL/USER_MARKET6*1000,2)),'FM99990.00') FLOW_MARKET_CMPL " +
				"from (select region_id,region_name,order_id," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then SUM_NUM end )), '0') MARKET1_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then SUM_NUM end )), '0') MARKET2_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then SUM_NUM end )), '0') MARKET3_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then SUM_NUM end )), '0') MARKET4_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then SUM_NUM end )), '0') MARKET5_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then SUM_NUM end )), '0') MARKET6_SUM," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='1' then CMPL_NUM end )), '0') MARKET1_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='2' then CMPL_NUM end )), '0') MARKET2_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='3' then CMPL_NUM end )), '0') MARKET3_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='4' then CMPL_NUM end )), '0') MARKET4_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='5' then CMPL_NUM end )), '0') MARKET5_CMPL," +
				"NVL(TO_CHAR(sum(case when s.divide_market_6_dl='6' then CMPL_NUM end )), '0') MARKET6_CMPL " +
				"from CS_COMPLAIN_MAIN a,(select zone_code,order_id from meta_dim_zone where dim_level=3)b,meta_dm.dm_divide_market_six s " +
				"where a.divide_market_6=s.divide_market_6(+) and a.region_id=b.zone_code and a.CMPL_BUSINESS_TYPE_ID like '"+servTypeId+"%' " +
				"and a.v_date='"+startDate+"' and DATE_TYPE='"+dateType+"' " +
				"group by REGION_ID,REGION_NAME,order_id order by order_id) a  " +
				"left join meta_user.cs_cust_jfyhs_mon m on a.region_id=m.region_id and m.month_id=substr('"+dateTime+"',0,6)");
	  return getDataAccess().queryForList(sql.toString());
}
}
