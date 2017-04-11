package tydic.reports.channel.allBusiness;

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

public class AllBusinessDAO extends MetaBaseDAO {
	//获取查询条件_月份
	public List<Map<String, Object>> getSelectMon() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct month_id as month_id from CS_CHANNEL_GLOBAL_MONTH  order by month_id desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	//全渠道总体情况分析周报_月份列表
	/*public List<Map<String, Object>> getSelectMon_Day() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select MONTH_NO from (select distinct MONTH_NO as MONTH_NO from CS_CHANNEL_GLOBAL_DAY where month_no is not null order by MONTH_NO desc)");
		return getDataAccess().queryForList(buffer.toString());
	}*/
	//全渠道总体情况分析周报_周列表
	public List<Map<String, Object>> getSelectWeek_Day() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select WEEK_NO, WEEK_NAME,MONTH_NO from (select distinct WEEK_NO as WEEK_NO,WEEK_NAME as WEEK_NAME,MONTH_NO from CS_CHANNEL_GLOBAL_DAY where WEEK_NAME is not null)" +
				" order by MONTH_NO desc,WEEK_NO desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//全渠道总体情况分析周报_最大月份
	/*public String getMaxMonth() {
		String sql = "select max(t.MONTH_NO)MONTH_NO from CS_CHANNEL_GLOBAL_DAY t ";
		return getDataAccess().queryForString(sql);
	}*/
	//全渠道总体情况分析周报_最大周
	public String getMaxWeek() {
		String sql = "select rownum,WEEK_NO ,WEEK_NAME from (select distinct WEEK_NO ,WEEK_NAME from CS_CHANNEL_GLOBAL_DAY " +
		          "where week_name is not null  order by WEEK_NO desc) where rownum=1";
		return getDataAccess().queryForString(sql);
	}
	/*public List<Map<String, Object>> getSelectMon_Week() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct month_id as month_id from META_USER.CS_CHANNEL_GLOBAL_DAY  order by month_id desc)");
		return getDataAccess().queryForList(buffer.toString());
	}*/
	public String getNewMonth() {
		String sql = "select to_date(max(t.month_id),'yyyy-MM') month_no from CS_CHANNEL_GLOBAL_MONTH t ";
		return getDataAccess().queryForString(sql);
	}
	/*public String getNewMonth_Week() {
		String sql = "select to_date(max(t.month_id),'yyyy-MM') month_no from META_USER.CS_CHANNEL_GLOBAL_DAY t ";
		return getDataAccess().queryForString(sql);
	}*/
	public String getNewDay(String tabStr) {
		String sql = "select max(t.day_id)day_id from "+tabStr+" t ";
		return getDataAccess().queryForString(sql);
	}
	public List<Map<String, Object>> getSelectProdType(String tabStr){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct prod_type_id,prod_type_name from "+tabStr+"  order by prod_type_id desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	public List<Map<String, Object>> getSelectIndType(String tabStr){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct IND_ID,IND_NAME from "+tabStr+"  order by to_number(IND_ID)");
		return getDataAccess().queryForList(buffer.toString());
	}
	public String getParameters(String rptId) {
		String sql = "select PARAMETERS from t_rpt_config_4j where rpt_id="+rptId;
		return getDataAccess().queryForString(sql);
	}
   //全渠道总体分析月报表表格数据
	public List<Map<String, Object>> getChannelGlobal_Mon(Map<String, Object> queryData) {
	      String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
          StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
  	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_ID,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2) = '3' AND  count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME2 END CHANNEL_TYPE_NAME,nvl(c.CHANNEL_TYPE_code2, '99') CHANNEL_TYPE_ID," +
  	      		"NVL(to_char(sum(SERVICE_NUM)),'-') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'-') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'-') PAY_NUM,NVL(to_char(sum(QUERY_NUM)),'-') QUERY_NUM,NVL(to_char(sum(CONSULT_NUM)),'-') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM)),'-') DEAL_NUM,NVL(to_char(sum(FEEDBACK_NUM)),'-') FEEDBACK_NUM," +
  	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'-') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'-') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'-') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'-') FAULT_NUM," +
  	      		"NVL(to_char(sum(OTHER_NUM)),'-') OTHER_NUM,decode(sum(distinct TOTAL_NUM),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM)*100,2)) PERC_NUM,decode(sum(distinct TOTAL_NUM2),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2)*100,2)) PERC_NUM2,a.MONTH_ID MONTH_ID "
  	              +" from CS_CHANNEL_GLOBAL_MONTH a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+2)b," +
  	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2 from meta_dm.d_v_channel_type e where e.STATE = '1' and e.VIEW_STATE = '"+Convert.toString(queryData.get("changType"))+"' start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
  	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
  	      if(dateTime!=null&&!("".equals(dateTime))){
      		  sql.append(" and a.month_id='"+dateTime+"'");
           }
            sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_ID,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_ID,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
            sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
          
          
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	//全渠道总体分析月报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData(String dateTime, Map<String,Object> map) {
		  String time = Convert.toString(dateTime).replace("-", "");
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  String channelTypeCode ="".equals(Convert.toString(map.get("channelTypeCode")))?"1":Convert.toString(map.get("channelTypeCode"));
	     		  StringBuffer sql = new StringBuffer("select z.* from (select b.ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,(sum(SERVICE_NUM)-sum(QUERY_NUM)-sum(CONSULT_NUM))SERVICE_NUM2,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM) QUERY_NUM,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM) DEAL_NUM,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM,sum(BUSI_NUM) BUSI_NUM,sum(YI_NUM) YI_NUM," +
	      		"sum(ASIST_NUM) ASIST_NUM,sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM)*100 PERC_NUM,sum(PERC_NUM2)*100 PERC_NUM2,a.MONTH_ID MONTH_ID "
	              +" from CS_CHANNEL_GLOBAL_MONTH a,meta_dim_zone b," +
	              "(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              	"where a.region_id=b.zone_code and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE and b.zone_code='"+zoneCode+"'");
		if(time!=null&&!("".equals(time))){
	  		  sql.append(" and a.month_id='"+time+"'");
	     }
        sql.append(" group by b.ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id) z ");
		sql.append(" order by z.PAR_REGION_ID, z.ORDER_ID,z.REGION_ID");
	  return getDataAccess().queryForMap(sql.toString());
  }  
	//全渠道总体分析月报表柱状图
	public List<Map<String, Object>> getBarChannelGlobal_Mon(Map<String, Object> queryData){
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,(sum(SERVICE_NUM)-sum(QUERY_NUM)-sum(CONSULT_NUM))SERVICE_NUM2,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM) QUERY_NUM,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM) DEAL_NUM,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM,sum(BUSI_NUM) BUSI_NUM,sum(YI_NUM) YI_NUM," +
	      		"sum(ASIST_NUM) ASIST_NUM,sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM)*100 PERC_NUM,sum(PERC_NUM2)*100 PERC_NUM2,a.MONTH_ID MONTH_ID "
	             +" from CS_CHANNEL_GLOBAL_MONTH a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=2 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.month_id='"+dateTime+"'");
	     }
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id) z ");
	   sql.append(" order by z.PAR_REGION_ID, ORDER_ID,z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析月报表柱状图21地市
	public List<Map<String, Object>> get21BarChannelGlobal_Mon(Map<String, Object> queryData){
		String zoneCode ="0000";
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,(sum(SERVICE_NUM)-sum(QUERY_NUM)-sum(CONSULT_NUM))SERVICE_NUM2,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM) QUERY_NUM,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM) DEAL_NUM,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM,sum(BUSI_NUM) BUSI_NUM,sum(YI_NUM) YI_NUM," +
	      		"sum(ASIST_NUM) ASIST_NUM,sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM)*100 PERC_NUM,sum(PERC_NUM2)*100 PERC_NUM2,a.MONTH_ID MONTH_ID "
	            +" from CS_CHANNEL_GLOBAL_MONTH a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=3 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	            +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.month_id='"+dateTime+"'");
	     }
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	   //全渠道总体分析日报表格数据
	public List<Map<String, Object>> getChannelGlobal_Week(Map<String, Object> queryData) {
	      //String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      //String week = Convert.toString(queryData.get("week"));
		  String startDate = Convert.toString(queryData.get("startDate")).replace("-", "");
	      String endDate = Convert.toString(queryData.get("endDate")).replace("-", "");
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
	      StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	    		  "CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.CHANNEL_TYPE_NAME, a.CHANNEL_TYPE_ID) = '3' AND count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE A.CHANNEL_TYPE_NAME END CHANNEL_TYPE_NAME,nvl(a.CHANNEL_TYPE_ID,'99') CHANNEL_TYPE_ID," +
	    		  "NVL(to_char(sum(SERVICE_NUM)),'-') SERVICE_NUM,NVL(to_char(sum(PAY_NUM)),'-') PAY_NUM,NVL(to_char(sum(QUERY_NUM)),'-') QUERY_NUM,NVL(to_char(sum(CONSULT_NUM)),'-') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM)),'-') DEAL_NUM,NVL(to_char(sum(FEEDBACK_NUM)),'-') FEEDBACK_NUM," +
		      		"NVL(to_char(sum(FEEDBACK2_NUM)),'-') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'-') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'-') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'-') FAULT_NUM," +
		      		"NVL(to_char(sum(OTHER_NUM)),'-') OTHER_NUM,decode(sum(distinct TOTAL_NUM),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM)*100,2)) PERC_NUM,decode(sum(distinct TOTAL_NUM2),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2)*100,2)) PERC_NUM2"
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY a, (select ORDER_ID,zone_code from META_DIM_ZONE start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," +
	              		"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
	      sql.append(" and a.day_id>='"+startDate+"'");
	      sql.append(" and a.day_id<='"+endDate+"'");
	      sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID ) in ('0','3') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL");
          sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID,z.CHANNEL_TYPE_ID");
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	//全渠道总体分析日报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData_Week(String startDate,String endDate, Map<String,Object> map) {
		startDate=Convert.toString(startDate).replace("-", "");
		endDate=Convert.toString(endDate).replace("-", "");
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 // String week = Convert.toString(map.get("week"));
		  String channelTypeCode ="".equals(Convert.toString(map.get("channelTypeCode")))?"1":Convert.toString(map.get("channelTypeCode"));
	     		  StringBuffer sql = new StringBuffer("select z.* from (select b.ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM) QUERY_NUM,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM) DEAL_NUM,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM) PERC_NUM,sum(PERC_NUM2) PERC_NUM2"
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY a,meta_dim_zone b," +
	              "(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              	"where a.region_id=b.zone_code and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE and b.zone_code='"+zoneCode+"'");
	     		 sql.append(" and a.day_id>='"+startDate+"'");
	   	         sql.append(" and a.day_id<='"+endDate+"'");
        sql.append(" group by b.ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id) z ");
		sql.append(" order by z.PAR_REGION_ID, z.ORDER_ID,z.REGION_ID");
	  return getDataAccess().queryForMap(sql.toString());
  } 
	//全渠道总体分析日报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData_WeekLink(String dateTime, Map<String,Object> map) {
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  String channelTypeCode ="".equals(Convert.toString(map.get("channelTypeCode")))?"1":Convert.toString(map.get("channelTypeCode"));
	     		  StringBuffer sql = new StringBuffer("select z.* from (select b.ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM) QUERY_NUM,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM) DEAL_NUM,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM) PERC_NUM,sum(PERC_NUM2) PERC_NUM2"
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY a,meta_dim_zone b," +
	              "(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              	"where a.region_id=b.zone_code and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE and b.zone_code='"+zoneCode+"'");
	     		 sql.append(" and a.day_id='"+dateTime+"'");
        sql.append(" group by b.ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id) z ");
		sql.append(" order by z.PAR_REGION_ID, z.ORDER_ID,z.REGION_ID");
	  return getDataAccess().queryForMap(sql.toString());
  }
	//全渠道总体分析日报表柱状图
	public List<Map<String, Object>> getBarChannelGlobal_Week(Map<String, Object> queryData){
		String startDate = Convert.toString(queryData.get("startDate")).replace("-", "");
	    String endDate = Convert.toString(queryData.get("endDate")).replace("-", "");
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		//String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM) QUERY_NUM,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM) DEAL_NUM,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM) PERC_NUM,sum(PERC_NUM2) PERC_NUM2"
	             +" from META_USER.CS_CHANNEL_GLOBAL_DAY a, (select ORDER_ID,level,zone_code from META_DIM_ZONE where  level=2 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 sql.append(" and a.day_id>='"+startDate+"'");
	     sql.append(" and a.day_id<='"+endDate+"'");
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析日报表柱状图21地市
	public List<Map<String, Object>> get21BarChannelGlobal_Week(Map<String, Object> queryData){
		String zoneCode ="0000";
		String startDate = Convert.toString(queryData.get("startDate")).replace("-", "");
	    String endDate = Convert.toString(queryData.get("endDate")).replace("-", "");
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM) QUERY_NUM,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM) DEAL_NUM,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM) PERC_NUM "
	            +" from META_USER.CS_CHANNEL_GLOBAL_DAY a, (select ORDER_ID,level,zone_code from META_DIM_ZONE where  level=3 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	            +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 sql.append(" and a.day_id>='"+startDate+"'");
	     sql.append(" and a.day_id<='"+endDate+"'");
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	   //全渠道总体分析周报表表格数据
	public List<Map<String, Object>> getChannelGlobal_Day(Map<String, Object> queryData) {
		//  String week = Convert.toString(queryData.get("weekNo"));
	      String dateTime = Convert.toString(queryData.get("dateTime"));
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
	      StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2) = '3' AND  count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME2 END CHANNEL_TYPE_NAME,nvl(c.CHANNEL_TYPE_code2, '99') CHANNEL_TYPE_ID," +
	      		"NVL(to_char(sum(SERVICE_NUM)),'-') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'-') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'-') PAY_NUM,NVL(to_char(sum(QUERY_NUM)),'-') QUERY_NUM,NVL(to_char(sum(CONSULT_NUM)),'-') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM)),'-') DEAL_NUM,NVL(to_char(sum(FEEDBACK_NUM)),'-') FEEDBACK_NUM," +
	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'-') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'-') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'-') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'-') FAULT_NUM," +
	      		"NVL(to_char(sum(OTHER_NUM)),'-') OTHER_NUM,decode(sum(distinct TOTAL_NUM_4_WEEK),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM_4_WEEK)*100,2)) PERC_NUM,decode(sum(distinct TOTAL_NUM2_4_WEEK),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2_4_WEEK)*100,2)) PERC_NUM2,a.MONTH_NO MONTH_NO "
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+2)b," +
	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2 from meta_dm.d_v_channel_type e where e.STATE = '1' and e.VIEW_STATE = '"+Convert.toString(queryData.get("changType"))+"' start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
	      if(dateTime!=null&&!("".equals(dateTime))){
    		  sql.append(" and a.WEEK_NAME='"+dateTime+"'");
         }
          sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
          sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	//全渠道总体分析周报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData_Day(String week,String month, Map<String,Object> map) {
		 // String week = Convert.toString(map.get("weekNo"));
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  String channelTypeCode ="".equals(Convert.toString(map.get("channelTypeCode")))?"1":Convert.toString(map.get("channelTypeCode"));
	     		  StringBuffer sql = new StringBuffer("select z.* from (select b.ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,(sum(SERVICE_NUM)-sum(QUERY_NUM)-sum(CONSULT_NUM))SERVICE_NUM2,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM) QUERY_NUM,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM) DEAL_NUM,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,decode(sum(distinct TOTAL_NUM_4_WEEK),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM_4_WEEK)*100,2)) PERC_NUM,decode(sum(distinct TOTAL_NUM2_4_WEEK),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2_4_WEEK)*100,2)) PERC_NUM2,a.MONTH_NO MONTH_NO "
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY a,meta_dim_zone b," +
	              "(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              	"where a.region_id=b.zone_code and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE and b.zone_code='"+zoneCode+"'");
		if(month!=null&&!("".equals(month))){
	  		  sql.append(" and a.MONTH_NO='"+month+"'");
	     }if(week!=null&&!("".equals(week))){
    		  sql.append(" and a.week_no='"+week+"'");
         }
        sql.append(" group by b.ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.MONTH_NO) z ");
		sql.append(" order by z.PAR_REGION_ID, z.ORDER_ID,z.REGION_ID");
	  return getDataAccess().queryForMap(sql.toString());
  }  
	//全渠道总体分析周报表柱状图
	public List<Map<String, Object>> getBarChannelGlobal_Day(Map<String, Object> queryData){
		//String week = Convert.toString(queryData.get("weekNo"));
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		String dateTime = Convert.toString(queryData.get("dateTime"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,(sum(SERVICE_NUM)-sum(QUERY_NUM)-sum(CONSULT_NUM))SERVICE_NUM2,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM) QUERY_NUM,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM) DEAL_NUM,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,decode(sum(distinct TOTAL_NUM_4_WEEK),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM_4_WEEK)*100,2)) PERC_NUM,decode(sum(distinct TOTAL_NUM2_4_WEEK),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2_4_WEEK)*100,2)) PERC_NUM2,a.MONTH_NO MONTH_NO "
	             +" from META_USER.CS_CHANNEL_GLOBAL_DAY a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=2 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.WEEK_NAME='"+dateTime+"'");
	     }/*if(week!=null&&!("".equals(week))){
    		  sql.append(" and a.week_no="+week);
         }*/
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.MONTH_NO) z ");
	   sql.append(" order by z.PAR_REGION_ID, ORDER_ID,z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析周报表柱状图21地市
	public List<Map<String, Object>> get21BarChannelGlobal_Day(Map<String, Object> queryData){
		String zoneCode ="0000";
		String dateTime = Convert.toString(queryData.get("dateTime"));
		//String week = Convert.toString(queryData.get("weekNo"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,(sum(SERVICE_NUM)-sum(QUERY_NUM)-sum(CONSULT_NUM))SERVICE_NUM2,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM) QUERY_NUM,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM) DEAL_NUM,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,decode(sum(distinct TOTAL_NUM_4_WEEK),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM_4_WEEK)*100,2)) PERC_NUM,decode(sum(distinct TOTAL_NUM2_4_WEEK),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2_4_WEEK)*100,2)) PERC_NUM2,a.MONTH_NO MONTH_NO "
	            +" from META_USER.CS_CHANNEL_GLOBAL_DAY a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=3 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	            +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.WEEK_NAME='"+dateTime+"'");
	     }/*if(week!=null&&!("".equals(week))){
    		  sql.append(" and a.week_no="+week);
         }*/
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.MONTH_NO) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//按照查询周查询月和周
	public Map<String, Object> queryMonthAndWeek(String WeekMon) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select MONTH_NO,WEEK_NO from (select distinct MONTH_NO,WEEK_NO from CS_CHANNEL_GLOBAL_DAY where WEEK_NAME=? order by WEEK_NO desc)");
		return getDataAccess().queryForMap(buffer.toString(), WeekMon);
	}
	//按月查询最大周
	public String queryMaxWeek(String WeekMon) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select Max(WEEK_NO) from CS_CHANNEL_GLOBAL_DAY where MONTH_NO=? ");
		return getDataAccess().queryForString(buffer.toString(), WeekMon);
	}
	//渠道服务_存储过程
	public Map<String, Object> getChannelSer_pg(Map<String, Object> queryData) {
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
	//渠道服务类指标日监测报表柱状图下钻区域
	public List<Map<String, Object>> getBarChannelSerMonitorDay_Zone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 //String prodTypeCode =Convert.toString(queryData.get("prodTypeCode"));
		 String ind="".equals(Convert.toString(queryData.get("selectCol")))?"1":Convert.toString(queryData.get("selectCol"));//默认
		 String sql="select rownum,region_id,zone_name,current_value from (select region_id,zone_name,case when max(ind_id) in('1','2','3','4','5','6') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2)),'FM99990.00') when max(ind_id) in('7') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2),2))) else to_char(round(sum(NUM1),2)) end current_value" +
		 		" from CS_CHANNEL_MONITOR_DAY a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+1 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and DAY_ID='"+dateTime+"' and ind_id='"+ind+"'";
		 /*if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and prod_type_id='"+prodTypeCode+"'";
		 }*/
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//渠道服务类指标日监测报表柱状图下级地市循环_网格数据
	public List<Map<String, Object>> getBarChannelSerMonitorDay_DownZone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		// String prodTypeCode =Convert.toString(queryData.get("prodTypeCode"));
		 String ind="".equals(Convert.toString(queryData.get("indId")))?"1":Convert.toString(queryData.get("indId"));//默认
		 String sql="select rownum,region_id,zone_name,current_value from (select region_id,zone_name,case when max(ind_id) in('1','2','3','4','5','6') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2)),'FM99990.00') when max(ind_id) in('7') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2),2))) else to_char(round(sum(NUM1),2)) end current_value" +
		 		" from CS_CHANNEL_MONITOR_DAY a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+2 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and DAY_ID='"+dateTime+"' and ind_id='"+ind+"'";
		 /*if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and prod_type_id='"+prodTypeCode+"'";
		 }*/
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//渠道服务类指标日监测报表取上月数据/折线图
	public  Map<String,Object> getChartData_ChannelSerMonitorDay(String dateTime, Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String ind="".equals(Convert.toString(map.get("indId")))?"1":Convert.toString(map.get("indId"));//默认
		String sql = "select case when max(ind_id) in('1','2','3','4','5','6') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2)),'FM99990.00') when max(ind_id) in('7') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2),2))) else to_char(round(sum(NUM1),2)) end current_value" +
			    " from CS_CHANNEL_MONITOR_DAY a " + 
					"where  a.region_id='"+ zoneCode+"'"; 
         sql+=" and a.DAY_ID='"+dateTime+"' and a.ind_id='"+ind+"'";
	  return getDataAccess().queryForMap(sql.toString());
  } 
	//渠道服务类指标日监测报表按时间取数（柱状图）
	public   List<Map<String,Object>> getChartData_ChannelSerMonitorDayByDate(String dateTime, Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String ind="".equals(Convert.toString(map.get("indId")))?"1":Convert.toString(map.get("indId"));//默认 
		 String changeCode="".equals(Convert.toString(map.get("changeCode")))?"1":Convert.toString(map.get("changeCode"));
		 String sql1="select rownum,order_id,ZONE_CODE,ZONE_NAME,DIM_LEVEL from (";
			if("0".equals(changeCode)){
				sql1+="select order_id,ZONE_CODE, ZONE_NAME,zone_par_code,DIM_LEVEL from meta_dim_zone_branch " +
				"WHERE zone_par_code= (SELECT zone_par_code FROM meta_dim_zone_branch WHERE ZONE_CODE = '"+zoneCode+"')" +
						" order by order_id";
			}else{
			    sql1+="select * from (select order_id,ZONE_CODE, ZONE_NAME,DIM_LEVEL from meta_dim_zone_branch start with ZONE_CODE = '"+zoneCode+"' " +
					"connect by prior ZONE_CODE = zone_par_code)b  WHERE b.DIM_LEVEL = " +
					"(SELECT DIM_LEVEL+'"+changeCode+"' FROM meta_dim_zone_branch WHERE ZONE_CODE ='"+zoneCode+"')" +
							" order by dim_level,order_id";
			}
			sql1+=")where rownum<23";
		 
		String sql = "select REGION_ID,ZONE_NAME,case when max(ind_id) in('1','2','3','4','5','6') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2)),'FM99990.00') when max(ind_id) in('7') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2),2))) else to_char(round(sum(NUM1),2)) end current_value" +
			    " from CS_CHANNEL_MONITOR_DAY a,(select ZONE_CODE,ZONE_NAME,order_id,dim_level from ("+sql1+") )b where a.REGION_ID = b.ZONE_CODE " + 
                " and a.DAY_ID='"+dateTime+"' and a.ind_id='"+ind+"'"+
                " group by REGION_ID,ZONE_NAME,order_id order by  b.order_id";
	  return getDataAccess().queryForList(sql.toString());
  }
	//渠道服务类指标日监测报表按时间取数（折线图）
	public   List<Map<String,Object>> getChartData_ChannelSerMonitorDaySumLine(String startDate,String endDate, Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String ind="".equals(Convert.toString(map.get("indId")))?"1":Convert.toString(map.get("indId"));//默认 
		 StringBuffer sql = new StringBuffer("select * from (select * from meta_dim_datetype where date_id >= '"+startDate+"' and date_id <= '"+endDate+"' " +
		      		" and date_type = '0') f left join(select DAY_ID,case when max(ind_id) in('1','2','3','4','5','6') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2)),'FM99990.00') when max(ind_id) in('7') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2),2))) else to_char(round(sum(NUM1),2)) end current_value" +
			    " from CS_CHANNEL_MONITOR_DAY a,meta_dim_zone_branch c" +
			    " where a.region_id = c.ZONE_CODE(+) and ZONE_CODE ='"+zoneCode+"' " +
                " and a.DAY_ID>='"+startDate+"' and a.DAY_ID<='"+endDate+"' and a.ind_id='"+ind+"'"+
		        " group by a.DAY_ID)g on f.date_id=g.DAY_ID order by date_id " );
	  return getDataAccess().queryForList(sql.toString());
  }
}
