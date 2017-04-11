package tydic.reports.channel.allBusiness;

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
import java.util.Locale;
import java.util.Map;

import oracle.jdbc.OracleTypes;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DBOutParameter;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.yhd.constant.ConstantStoreProc;
import tydic.meta.common.yhd.utils.Pager;
import tydic.portalCommon.DateUtil;

public class AllChannelDAO extends MetaBaseDAO {
	//获取查询条件_类型
	public List<Map<String, Object>> getSelectType() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select CHANNEL_TYPE_CODE as CHANNEL_TYPE_ID,CHANNEL_TYPE_NAME,ORDER_ID from meta_dm.d_v_channel_type_c where DIM_LEVEL='2' order by to_number(CHANNEL_TYPE_PAR_CODE),to_number(ORDER_ID)");
		return getDataAccess().queryForList(buffer.toString());
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
		  String totalNum=getTotalNum(queryData);
		  Integer year = Convert.toInt(Convert.toString(queryData.get("dateTime")).substring(0, 4));
		  Integer month = Convert.toInt(Convert.toString(queryData.get("dateTime")).substring(4, 6));
		  String dateTime=Convert.toString(queryData.get("dateTime"));
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
          StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,NVL(c.CHANNEL_TYPE_PAR_NAME, '-') CHANNEL_TYPE_PAR_NAME, NVL(c.CHANNEL_TYPE_PAR_CODE, '99') CHANNEL_TYPE_PAR_CODE, c.ORDER_ID2,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
  	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME, a.PAR_REGION_ID, a.REGION_NAME,  a.region_id,  a.MONTH_NO,  c.CHANNEL_TYPE_PAR_NAME, c.CHANNEL_TYPE_PAR_CODE, c.ORDER_ID2, c.CHANNEL_TYPE_NAME, c.CHANNEL_TYPE_code) = '31' then  '合计'  WHEN grouping_id(a.PAR_REGION_NAME,  a.PAR_REGION_ID, a.REGION_NAME, a.region_id,  a.MONTH_NO, c.CHANNEL_TYPE_PAR_NAME,  c.CHANNEL_TYPE_PAR_CODE,  c.ORDER_ID2,  c.CHANNEL_TYPE_NAME,   c.CHANNEL_TYPE_code) = '7' then"+
                 " '小计'  ELSE  c.CHANNEL_TYPE_NAME  END CHANNEL_TYPE_NAME,  nvl(c.CHANNEL_TYPE_code, '99999') CHANNEL_TYPE_ID," +
  	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
  	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
  	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,to_char(decode(to_number("+totalNum+"),0,0,round(sum(SERVICE_NUM)/to_number("+totalNum+")*100,2)),'FM99990.00') PERC_NUM,decode(sum(distinct TOTAL_NUM2),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_NO MONTH_NO "
  	              +" from CS_CHANNEL_GLOBAL_DAY_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+1)b," +
  	              		"(select d1.*, d2.channel_type_par_name  from (select CHANNEL_TYPE_CODE, CHANNEL_TYPE_NAME, CHANNEL_TYPE_PAR_CODE,  ORDER_ID as ORDER_ID2 from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior  CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE) d1,   (select channel_type_name as channel_type_par_name,  channel_type_code as channel_type_par_code   from meta_dm.d_v_channel_type_c) d2 where d1.channel_type_par_code = d2.channel_type_par_code)c " +
  	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
  	      if(dateTime!=null&&!("".equals(dateTime))){
  	    	sql.append(" and a.month_no='"+dateTime+"'");
           }
            sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_PAR_NAME, c.CHANNEL_TYPE_PAR_CODE, c.ORDER_ID2,c.CHANNEL_TYPE_NAME, c.CHANNEL_TYPE_code)having grouping_id(a.PAR_REGION_NAME, a.PAR_REGION_ID, a.REGION_NAME, a.region_id, a.MONTH_NO,c.CHANNEL_TYPE_PAR_NAME, c.CHANNEL_TYPE_PAR_CODE, c.ORDER_ID2, c.CHANNEL_TYPE_NAME, c.CHANNEL_TYPE_code) in ('0','7','31') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL order by z.PAR_REGION_ID, z.ORDER_ID, z.REGION_ID, z.CHANNEL_TYPE_PAR_CODE, z.ORDER_ID2,  z.CHANNEL_TYPE_ID");
          
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	 //全渠道总体分析月报表格数据
	public List<Map<String, Object>> getChannelGlobalZone_Mon1(Map<String, Object> queryData) {
	      String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
	      StringBuffer sql = new StringBuffer("select T1.ORDER_ID,T1.PAR_REGION_NAME,T1.PAR_REGION_ID,T1.REGION_NAME, T1.REGION_ID,SUM(SERVICE_NUM) SERVICE_NUM,SUM(SERVICE_NUM2) SERVICE_NUM2,SUM(PAY_NUM) PAY_NUM, SUM(QUERY_NUM1) QUERY_NUM1,SUM(QUERY_NUM2) QUERY_NUM2,SUM(QUERY_NUM3) QUERY_NUM3, SUM(QUERY_NUM4) QUERY_NUM4, SUM(QUERY_NUM5) QUERY_NUM5,SUM(CONSULT_NUM) CONSULT_NUM,SUM(DEAL_NUM1) DEAL_NUM1,SUM(DEAL_NUM2) DEAL_NUM2,SUM(DEAL_NUM3) DEAL_NUM3,SUM(DEAL_NUM4) DEAL_NUM4,SUM(FEEDBACK_NUM) FEEDBACK_NUM,SUM(FEEDBACK2_NUM) FEEDBACK2_NUM,SUM(CHANGE_NUM) CHANGE_NUM,SUM(COMPLAIN_NUM) COMPLAIN_NUM, SUM(FAULT_NUM) FAULT_NUM,SUM(OTHER_NUM) OTHER_NUM from (select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	    		  "CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME, a.CHANNEL_TYPE_ID) = '3' AND count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME END CHANNEL_TYPE_NAME,nvl(a.CHANNEL_TYPE_ID,'99') CHANNEL_TYPE_ID," +
	    		  "NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
		      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
		      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,to_char(decode(sum(distinct TOTAL_NUM),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM)*100,2)),'FM99990.00') PERC_NUM,decode(sum(distinct TOTAL_NUM2),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2)*100,2)) PERC_NUM2"
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%' AND D.DIM_LEVEL in (getdimlevel('"+zoneCode+"'),'3') )b," +
	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
	      sql.append(" and a.month_id='"+dateTime+"'");
	      sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID ) in ('0','3') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL");
          sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID,z.CHANNEL_TYPE_ID");
          sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_c a1, meta_dm.d_v_channel_type_c a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE GROUP BY T1.ORDER_ID,T1.PAR_REGION_ID,T1.PAR_REGION_NAME,T1.REGION_NAME,T1.REGION_ID order by T1.PAR_REGION_ID");
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	//全渠道总体分析日报表
	public List<Map<String, Object>> getChannelGlobalZone_Mon(Map<String, Object> queryData){
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String changeCode =Convert.toString(queryData.get("changeCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,NVL(to_char(sum(PERC_NUM)),'0') PERC_NUM,NVL(to_char(sum(PERC_NUM2)),'0') PERC_NUM2"
	             +" from META_USER.CS_CHANNEL_GLOBAL_MONTH_New a, (select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%'  AND D.DIM_LEVEL in (getdimlevel('"+zoneCode+"'), 3)   start with zone_code = '"+zoneCode+"'  connect by prior zone_code = zone_par_code)b," 
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 sql.append(" and a.month_id='"+dateTime+"'");
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析月报表柱状图
	public List<Map<String, Object>> getBarChannelGlobalZone_Mon(Map<String, Object> queryData){
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String changeCode =Convert.toString(queryData.get("changeCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM1) QUERY_NUM1,sum(QUERY_NUM2) QUERY_NUM2,sum(QUERY_NUM3) QUERY_NUM3,sum(QUERY_NUM4) QUERY_NUM4,sum(QUERY_NUM5) QUERY_NUM5,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM1) DEAL_NUM1,sum(DEAL_NUM2) DEAL_NUM2,sum(DEAL_NUM3) DEAL_NUM3,sum(DEAL_NUM4) DEAL_NUM4,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM) PERC_NUM,sum(PERC_NUM2) PERC_NUM2"
	             +" from META_USER.CS_CHANNEL_GLOBAL_MONTH_New a, (select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%'  AND D.DIM_LEVEL = '"+changeCode+"'+1  start with zone_code = '"+zoneCode+"'  connect by prior zone_code = zone_par_code)b," 
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 sql.append(" and a.month_id='"+dateTime+"'");
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析月报表柱状图
	public List<Map<String, Object>> getBarChannelGlobalZone_Mon2(Map<String, Object> queryData){
		String dateTime = Convert.toString(queryData.get("lastMon")).replace("-", "");
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String changeCode =Convert.toString(queryData.get("changeCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM1) QUERY_NUM1,sum(QUERY_NUM2) QUERY_NUM2,sum(QUERY_NUM3) QUERY_NUM3,sum(QUERY_NUM4) QUERY_NUM4,sum(QUERY_NUM5) QUERY_NUM5,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM1) DEAL_NUM1,sum(DEAL_NUM2) DEAL_NUM2,sum(DEAL_NUM3) DEAL_NUM3,sum(DEAL_NUM4) DEAL_NUM4,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM) PERC_NUM,sum(PERC_NUM2) PERC_NUM2"
	             +" from META_USER.CS_CHANNEL_GLOBAL_MONTH_New a, (select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%'  AND D.DIM_LEVEL = '"+changeCode+"'+1  start with zone_code = '"+zoneCode+"'  connect by prior zone_code = zone_par_code)b," 
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 sql.append(" and a.month_id='"+dateTime+"'");
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析月报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData(String dateTime, Map<String,Object> map) {
		  String selType = Convert.toString(map.get("typeList"));
	      String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
        StringBuffer sql = new StringBuffer("select * from(select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2) = '3' AND  count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME2 END CHANNEL_TYPE_NAME,nvl(c.CHANNEL_TYPE_code2, '99') CHANNEL_TYPE_ID," +
	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM),'0','0.00',trim(to_char(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM) * 100,'FM99990.00'))) PERC_NUM,decode(sum(distinct TOTAL_NUM2),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_NO MONTH_NO "
	              +" from CS_CHANNEL_GLOBAL_DAY_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+1)b," +
	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2 from meta_dm.d_v_channel_type_c e where e.STATE = '1' and e.VIEW_STATE = '1' start with CHANNEL_TYPE_CODE = '"+selType+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
    		  sql.append(" and a.month_no='"+dateTime+"'");
          sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type_c f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
          sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
          sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_c a1, meta_dm.d_v_channel_type_c a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE ORDER BY T2.ORDER_ID");
	  return getDataAccess().queryForMap(sql.toString());
  }  
	//全渠道总体分析月报表柱状图
	public List<Map<String, Object>> getBarChannelGlobal_Mon(Map<String, Object> queryData){
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		StringBuffer sql = new StringBuffer("select * from (select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,(sum(SERVICE_NUM)-sum(QUERY_NUM1)-sum(QUERY_NUM2)-sum(QUERY_NUM3)-sum(QUERY_NUM4)-sum(QUERY_NUM5)-sum(CONSULT_NUM))SERVICE_NUM2,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM1) QUERY_NUM1,sum(QUERY_NUM2) QUERY_NUM2,sum(QUERY_NUM3) QUERY_NUM3,sum(QUERY_NUM4) QUERY_NUM4,sum(QUERY_NUM5) QUERY_NUM5,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM1) DEAL_NUM1,sum(DEAL_NUM2) DEAL_NUM2,sum(DEAL_NUM3) DEAL_NUM3,sum(DEAL_NUM4) DEAL_NUM4,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM)*100 PERC_NUM,sum(PERC_NUM2)*100 PERC_NUM2,a.MONTH_ID MONTH_ID "
	             +" from CS_CHANNEL_GLOBAL_MONTH_NEW a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=2 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.month_id='"+dateTime+"'");
	     }
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id) z ");
	   sql.append(" order by z.PAR_REGION_ID, ORDER_ID,z.REGION_ID");
       sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME from meta_dm.d_v_channel_type_c a1, meta_dm.d_v_channel_type_c a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析月报表柱状图21地市
	public List<Map<String, Object>> get21BarChannelGlobal_Mon(Map<String, Object> queryData){
		String zoneCode ="0000";
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,(sum(SERVICE_NUM)-sum(QUERY_NUM1)-sum(QUERY_NUM2)-sum(QUERY_NUM3)-sum(QUERY_NUM4)-sum(QUERY_NUM5)-sum(CONSULT_NUM))SERVICE_NUM2,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM1) QUERY_NUM1,sum(QUERY_NUM2) QUERY_NUM2,sum(QUERY_NUM3) QUERY_NUM3,sum(QUERY_NUM4) QUERY_NUM4,sum(QUERY_NUM5) QUERY_NUM5,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM1) DEAL_NUM1,sum(DEAL_NUM2) DEAL_NUM2,sum(DEAL_NUM3) DEAL_NUM3,sum(DEAL_NUM4) DEAL_NUM4,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM)*100 PERC_NUM,sum(PERC_NUM2)*100 PERC_NUM2,a.MONTH_ID MONTH_ID "
	            +" from CS_CHANNEL_GLOBAL_MONTH_NEW a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=3 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	            +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.month_id='"+dateTime+"'");
	     }
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	public String getTotalNum(Map<String, Object> queryData) {
		
		String startDate = Convert.toString(queryData.get("dateTime")).replace("-", "");
	    String endDate = Convert.toString(queryData.get("dateTime")).replace("-", "");
	    String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	    String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		String sql = "select sum(SERVICE_NUM) from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"') + 1)b," +
	              		"(select CHANNEL_TYPE_CODE, CHANNEL_TYPE_PAR_CODE from meta_dm.d_v_channel_type_c  start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"'   connect by prior CHANNEL_TYPE_CODE =     CHANNEL_TYPE_PAR_CODE) c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE ";
		 if("1".equals(queryData.get("reportId").toString())){
			 sql+="and a.day_id>='"+startDate+"' and a.day_id<='"+endDate+"'";
		 }if("2".equals(queryData.get("reportId").toString())){
			 sql+="and a.month_no='"+startDate+"'";
		 }if("3".equals(queryData.get("reportId").toString())){
			 sql+="and a.WEEK_NAME='"+startDate+"'";
		 }
		return getDataAccess().queryForString(sql);
	}
	   //全渠道总体分析日报表格数据
	public List<Map<String, Object>> getChannelGlobal_Week(Map<String, Object> queryData) {
		  String totalNum=getTotalNum(queryData);
		  String startDate = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String endDate = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
	      StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,NVL(c.CHANNEL_TYPE_PAR_NAME, '-') CHANNEL_TYPE_PAR_NAME, NVL(c.CHANNEL_TYPE_PAR_CODE, '99') CHANNEL_TYPE_PAR_CODE,c.ORDER_ID2,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	    		  "CASE WHEN grouping_id(a.PAR_REGION_NAME, a.PAR_REGION_ID, a.REGION_NAME, a.region_id,  c.CHANNEL_TYPE_PAR_NAME, c.CHANNEL_TYPE_PAR_CODE, c.ORDER_ID2,  c.CHANNEL_TYPE_NAME,  a.CHANNEL_TYPE_ID) = '31' then  '合计'"+
                   "WHEN grouping_id(a.PAR_REGION_NAME, a.PAR_REGION_ID,  a.REGION_NAME,  a.region_id,  c.CHANNEL_TYPE_PAR_NAME,   c.CHANNEL_TYPE_PAR_CODE, c.ORDER_ID2,  c.CHANNEL_TYPE_NAME,  a.CHANNEL_TYPE_ID) = '7' then '小计'  ELSE  c.CHANNEL_TYPE_NAME END CHANNEL_TYPE_NAME,nvl(a.CHANNEL_TYPE_ID, '999999') CHANNEL_TYPE_ID," +
	    		  "NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
		      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
		      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,to_char(decode(to_number("+totalNum+"),0,0,round(sum(SERVICE_NUM)/to_number("+totalNum+")*100,2)),'FM99990.00') PERC_NUM,decode(sum(distinct TOTAL_NUM2),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2)*100,2)) PERC_NUM2"
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"') + 1)b," +
	              		"(select d1.*, d2.channel_type_par_name from (select CHANNEL_TYPE_CODE, CHANNEL_TYPE_NAME, CHANNEL_TYPE_PAR_CODE,  ORDER_ID as ORDER_ID2 from meta_dm.d_v_channel_type_c  start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"'   connect by prior CHANNEL_TYPE_CODE =     CHANNEL_TYPE_PAR_CODE) d1,  (select channel_type_name as channel_type_par_name,    channel_type_code as channel_type_par_code     from meta_dm.d_v_channel_type_c) d2  where d1.channel_type_par_code =   d2.channel_type_par_code)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
	      sql.append(" and a.day_id>='"+startDate+"'");
	      sql.append(" and a.day_id<='"+endDate+"'");
	      sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_PAR_NAME,c.CHANNEL_TYPE_PAR_CODE,c.ORDER_ID2,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id, c.CHANNEL_TYPE_PAR_NAME, c.CHANNEL_TYPE_PAR_CODE, c.ORDER_ID2,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID ) in ('0','7','31') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL");
          sql.append(" order by z.PAR_REGION_ID, ORDER_ID, z.REGION_ID,z.CHANNEL_TYPE_PAR_CODE,z.ORDER_ID2，z.CHANNEL_TYPE_ID");
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	 //全渠道总体分析日报表格数据
	public List<Map<String, Object>> getChannelGlobal_WeekZone(Map<String, Object> queryData) {
		  String startDate = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String endDate = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
	      StringBuffer sql = new StringBuffer("select T1.ORDER_ID,T1.PAR_REGION_NAME,T1.PAR_REGION_ID,T1.REGION_NAME, T1.REGION_ID,SUM(SERVICE_NUM) SERVICE_NUM,SUM(SERVICE_NUM2) SERVICE_NUM2,SUM(PAY_NUM) PAY_NUM, SUM(QUERY_NUM1) QUERY_NUM1,SUM(QUERY_NUM2) QUERY_NUM2,SUM(QUERY_NUM3) QUERY_NUM3, SUM(QUERY_NUM4) QUERY_NUM4, SUM(QUERY_NUM5) QUERY_NUM5,SUM(CONSULT_NUM) CONSULT_NUM,SUM(DEAL_NUM1) DEAL_NUM1,SUM(DEAL_NUM2) DEAL_NUM2,SUM(DEAL_NUM3) DEAL_NUM3,SUM(DEAL_NUM4) DEAL_NUM4,SUM(FEEDBACK_NUM) FEEDBACK_NUM,SUM(FEEDBACK2_NUM) FEEDBACK2_NUM,SUM(CHANGE_NUM) CHANGE_NUM,SUM(COMPLAIN_NUM) COMPLAIN_NUM, SUM(FAULT_NUM) FAULT_NUM,SUM(OTHER_NUM) OTHER_NUM from (select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	    		  "CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME, a.CHANNEL_TYPE_ID) = '3' AND count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME END CHANNEL_TYPE_NAME,nvl(a.CHANNEL_TYPE_ID,'99') CHANNEL_TYPE_ID," +
	    		  "NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
		      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
		      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,to_char(decode(sum(distinct TOTAL_NUM),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM)*100,2)),'FM99990.00') PERC_NUM,decode(sum(distinct TOTAL_NUM2),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2)*100,2)) PERC_NUM2"
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%' AND D.DIM_LEVEL in (getdimlevel('"+zoneCode+"'),'3') )b," +
	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
	      sql.append(" and a.day_id>='"+startDate+"'");
	      sql.append(" and a.day_id<='"+endDate+"'");
	      sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID ) in ('0','3') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL");
          sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID,z.CHANNEL_TYPE_ID");
          sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_c a1, meta_dm.d_v_channel_type_c a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE GROUP BY T1.ORDER_ID,T1.PAR_REGION_ID,T1.PAR_REGION_NAME,T1.REGION_NAME,T1.REGION_ID order by T1.PAR_REGION_ID");
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	//全渠道总体分析日报表格数据
	public List<Map<String, Object>> getChannelGlobalZone_Day(Map<String, Object> queryData) {
		  String startDate = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String endDate = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
	      StringBuffer sql = new StringBuffer("select ORDER_ID,PAR_REGION_NAME,PAR_REGION_ID,REGION_ID,REGION_NAME,SUM(SERVICE_NUM) SERVICE_NUM,SUM(SERVICE_NUM2) SERVICE_NUM2,SUM(PAY_NUM) PAY_NUM,SUM(QUERY_NUM1) QUERY_NUM1,SUM(QUERY_NUM2) QUERY_NUM2,SUM(QUERY_NUM3) QUERY_NUM3,SUM(QUERY_NUM4) QUERY_NUM4,SUM(QUERY_NUM5) QUERY_NUM5,SUM(CONSULT_NUM) CONSULT_NUM,SUM(DEAL_NUM1) DEAL_NUM1,SUM(DEAL_NUM2) DEAL_NUM2,SUM(DEAL_NUM3) DEAL_NUM3,SUM(DEAL_NUM4) DEAL_NUM4,SUM(COMPLAIN_NUM) COMPLAIN_NUM,SUM(FAULT_NUM) FAULT_NUM,SUM(OTHER_NUM) OTHER_NUM from (select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	    		  "CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME, a.CHANNEL_TYPE_ID) = '3' AND count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME END CHANNEL_TYPE_NAME,nvl(a.CHANNEL_TYPE_ID,'99') CHANNEL_TYPE_ID," +
	    		  "NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
		      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
		      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,to_char(decode(sum(distinct TOTAL_NUM),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM)*100,2)),'FM99990.00') PERC_NUM,decode(sum(distinct TOTAL_NUM2),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2)*100,2)) PERC_NUM2"
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%' AND D.DIM_LEVEL in( getdimlevel('"+zoneCode+"'),3))b," +
	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
	      sql.append(" and a.day_id>='"+startDate+"'");
	      sql.append(" and a.day_id<='"+endDate+"'");
	      sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID ) in ('0','3') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL");
          sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID,z.CHANNEL_TYPE_ID");
          sql.append(" ) T1 GROUP BY ORDER_ID,PAR_REGION_NAME,PAR_REGION_ID,REGION_ID,REGION_NAME	ORDER BY PAR_REGION_ID,T1.ORDER_ID");
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	//全渠道总体分析日报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData_Week(String startDate,String endDate, Map<String,Object> map) {
		startDate=Convert.toString(startDate).replace("-", "");
		endDate=Convert.toString(endDate).replace("-", "");
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  String channelTypeCode ="".equals(Convert.toString(map.get("channelTypeCode")))?"1":Convert.toString(map.get("channelTypeCode"));
	     		  StringBuffer sql = new StringBuffer("select z.* from (select b.ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM1) QUERY_NUM1,sum(QUERY_NUM2) QUERY_NUM2,sum(QUERY_NUM3) QUERY_NUM3,sum(QUERY_NUM4) QUERY_NUM4,sum(QUERY_NUM5) QUERY_NUM5,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM1) DEAL_NUM1,sum(DEAL_NUM2) DEAL_NUM2,sum(DEAL_NUM3) DEAL_NUM3,sum(DEAL_NUM4) DEAL_NUM4,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM) PERC_NUM,sum(PERC_NUM2) PERC_NUM2"
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a,meta_dim_zone b," +
	              "(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              	"where a.region_id=b.zone_code and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE and b.zone_code='"+zoneCode+"'");
	     		 sql.append(" and a.day_id>='"+startDate+"'");
	   	         sql.append(" and a.day_id<='"+endDate+"'");
        sql.append(" group by b.ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id) z ");
		sql.append(" order by z.PAR_REGION_ID, z.ORDER_ID,z.REGION_ID");
	  return getDataAccess().queryForMap(sql.toString());
  } 
	//全渠道总体分析日报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData_WeekLink(String dateTime, Map<String,Object> map) {
		  String startDate =dateTime;
		  String endDate = dateTime;
	      String selType= Convert.toString(map.get("selType"));
	      String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
	      StringBuffer sql = new StringBuffer("select * from (select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	    		  "CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME, a.CHANNEL_TYPE_ID) = '3' AND count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME END CHANNEL_TYPE_NAME,nvl(a.CHANNEL_TYPE_ID,'99') CHANNEL_TYPE_ID," +
	    		  "NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
		      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
		      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM)*100,2)) PERC_NUM,decode(sum(distinct TOTAL_NUM2),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2)*100,2)) PERC_NUM2"
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"') + 1)b," +
	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+selType+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
	      sql.append(" and a.day_id>='"+startDate+"'");
	      sql.append(" and a.day_id<='"+endDate+"'");
	      sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID ) in ('0','3') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL");
         sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID,z.CHANNEL_TYPE_ID");
         sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_c a1, meta_dm.d_v_channel_type_c a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE ORDER BY T2.ORDER_ID");
	  return getDataAccess().queryForMap(sql.toString());
  }
	//全渠道总体分析日报表柱状图
	public List<Map<String, Object>> getBarChannelGlobal_Week(Map<String, Object> queryData){
		String startDate = Convert.toString(queryData.get("startDate")).replace("-", "");
	    String endDate = Convert.toString(queryData.get("endDate")).replace("-", "");
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String changeCode =Convert.toString(queryData.get("changeCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM1) QUERY_NUM1,sum(QUERY_NUM2) QUERY_NUM2,sum(QUERY_NUM3) QUERY_NUM3,sum(QUERY_NUM4) QUERY_NUM4,sum(QUERY_NUM5) QUERY_NUM5,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM1) DEAL_NUM1,sum(DEAL_NUM2) DEAL_NUM2,sum(DEAL_NUM3) DEAL_NUM3,sum(DEAL_NUM4) DEAL_NUM4,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM) PERC_NUM,sum(PERC_NUM2) PERC_NUM2"
	             +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID,level,zone_code from META_DIM_ZONE where  level='"+changeCode+"'+1 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 sql.append(" and a.day_id>='"+startDate+"'");
	     sql.append(" and a.day_id<='"+endDate+"'");
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析周报表柱状图
	public List<Map<String, Object>> getBarChannelGlobal_Week2(Map<String, Object> queryData){
	    String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String changeCode =Convert.toString(queryData.get("changeCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM1) QUERY_NUM1,sum(QUERY_NUM2) QUERY_NUM2,sum(QUERY_NUM3) QUERY_NUM3,sum(QUERY_NUM4) QUERY_NUM4,sum(QUERY_NUM5) QUERY_NUM5,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM1) DEAL_NUM1,sum(DEAL_NUM2) DEAL_NUM2,sum(DEAL_NUM3) DEAL_NUM3,sum(DEAL_NUM4) DEAL_NUM4,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM) PERC_NUM,sum(PERC_NUM2) PERC_NUM2"
	             +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID,level,zone_code from META_DIM_ZONE where  level='"+changeCode+"'+1 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 sql.append(" and a.week_name='"+dateTime+"'");
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析周报表柱状图-获取上周
	public List<Map<String, Object>> getBarChannelGlobal_WeekLast(Map<String, Object> queryData){
	    String dateTime = Convert.toString(queryData.get("lastWeek")).replace("-", "");
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String changeCode =Convert.toString(queryData.get("changeCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM1) QUERY_NUM1,sum(QUERY_NUM2) QUERY_NUM2,sum(QUERY_NUM3) QUERY_NUM3,sum(QUERY_NUM4) QUERY_NUM4,sum(QUERY_NUM5) QUERY_NUM5,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM1) DEAL_NUM1,sum(DEAL_NUM2) DEAL_NUM2,sum(DEAL_NUM3) DEAL_NUM3,sum(DEAL_NUM4) DEAL_NUM4,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM) PERC_NUM,sum(PERC_NUM2) PERC_NUM2"
	             +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID,level,zone_code from META_DIM_ZONE where  level='"+changeCode+"'+1 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 sql.append(" and a.week_name='"+dateTime+"'");
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
	      		"sum(SERVICE_NUM) SERVICE_NUM,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM1) QUERY_NUM1,sum(QUERY_NUM2) QUERY_NUM2,sum(QUERY_NUM3) QUERY_NUM3,sum(QUERY_NUM4) QUERY_NUM4,sum(QUERY_NUM5) QUERY_NUM5,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM1) DEAL_NUM1,sum(DEAL_NUM2) DEAL_NUM2,sum(DEAL_NUM3) DEAL_NUM3,sum(DEAL_NUM4) DEAL_NUM4,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,sum(PERC_NUM) PERC_NUM "
	            +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID,level,zone_code from META_DIM_ZONE where  level=3 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	            +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
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
		  String totalNum=getTotalNum(queryData);
	      String dateTime = Convert.toString(queryData.get("dateTime"));
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
	      StringBuffer sql = new StringBuffer("select  z.* from (select ORDER_ID,NVL(c.CHANNEL_TYPE_PAR_NAME, '-') CHANNEL_TYPE_PAR_NAME,NVL(c.CHANNEL_TYPE_PAR_CODE, '99') CHANNEL_TYPE_PAR_CODE,c.ORDER_ID2,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME, a.PAR_REGION_ID, a.REGION_NAME, a.region_id, a.MONTH_NO,c.CHANNEL_TYPE_PAR_NAME,c.CHANNEL_TYPE_PAR_CODE,c.ORDER_ID2, c.CHANNEL_TYPE_NAME, c.CHANNEL_TYPE_code) = '31' then '合计' WHEN grouping_id(a.PAR_REGION_NAME, a.PAR_REGION_ID, a.REGION_NAME, a.region_id, a.MONTH_NO,c.CHANNEL_TYPE_PAR_NAME,c.CHANNEL_TYPE_PAR_CODE,c.ORDER_ID2, c.CHANNEL_TYPE_NAME, c.CHANNEL_TYPE_code) = '7' then '小计'   ELSE c.CHANNEL_TYPE_NAME END CHANNEL_TYPE_NAME,  nvl(c.CHANNEL_TYPE_code, '99999') CHANNEL_TYPE_ID," +
	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,to_char(decode(to_number("+totalNum+"),0,0,round(sum(SERVICE_NUM)/to_number("+totalNum+")*100,2)),'FM99990.00') PERC_NUM,decode(sum(distinct TOTAL_NUM2_4_WEEK),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2_4_WEEK) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_NO MONTH_NO "
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL <getdimlevel('"+zoneCode+"')+1)b," +
	              		"(select d1.*, d2.channel_type_par_name from (select CHANNEL_TYPE_CODE, CHANNEL_TYPE_NAME,  CHANNEL_TYPE_PAR_CODE,   ORDER_ID as ORDER_ID2    from meta_dm.d_v_channel_type_c  start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE =  CHANNEL_TYPE_PAR_CODE) d1,   (select channel_type_name as channel_type_par_name,   channel_type_code as channel_type_par_code    from meta_dm.d_v_channel_type_c) d2   where d1.channel_type_par_code =   d2.channel_type_par_code)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
	      if(dateTime!=null&&!("".equals(dateTime))){
    		  sql.append(" and a.WEEK_NAME='"+dateTime+"'");
         }
          sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_PAR_NAME,c.CHANNEL_TYPE_PAR_CODE,c.ORDER_ID2,c.CHANNEL_TYPE_NAME, c.CHANNEL_TYPE_code)having grouping_id(a.PAR_REGION_NAME, a.PAR_REGION_ID, a.REGION_NAME, a.region_id, a.MONTH_NO,c.CHANNEL_TYPE_PAR_NAME,c.CHANNEL_TYPE_PAR_CODE,c.ORDER_ID2, c.CHANNEL_TYPE_NAME, c.CHANNEL_TYPE_code) in ('0','7','31') ) z  where z.CHANNEL_TYPE_NAME IS NOT NULL order by z.PAR_REGION_ID, z.ORDER_ID, z.REGION_ID,z.CHANNEL_TYPE_PAR_CODE,z.ORDER_ID2,z.CHANNEL_TYPE_ID");           
          List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	//全渠道总体分析周报表表格数据-区域详情
	public List<Map<String, Object>> getChannelGlobal_Week2(Map<String, Object> queryData){
		String startDate = Convert.toString(queryData.get("startDate")).replace("-", "");
	    String endDate = Convert.toString(queryData.get("endDate")).replace("-", "");
	    String dateTime = Convert.toString(queryData.get("dateTime"));
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String changeCode =Convert.toString(queryData.get("changeCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,NVL(to_char(sum(PERC_NUM)),'0') PERC_NUM,NVL(to_char(sum(PERC_NUM2)),'0') PERC_NUM2"
	             +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a,(select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%' AND D.DIM_LEVEL in (getdimlevel('"+zoneCode+"'),'"+changeCode+"'+1) start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 sql.append(" and a.week_name='"+dateTime+"'");
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析周报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData_Day(String week,String month, Map<String,Object> map) {
	      String dateTime = Convert.toString(map.get("dateTime"));
	      String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
	      String selType=Convert.toString(map.get("selType"));
	      StringBuffer sql = new StringBuffer("select * from (select  z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2) = '3' AND  count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME2 END CHANNEL_TYPE_NAME,nvl(c.CHANNEL_TYPE_code2, '99') CHANNEL_TYPE_ID," +
	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM_4_WEEK),'0','0.00',trim(to_char(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM_4_WEEK) * 100,'FM99990.00'))) PERC_NUM,decode(sum(distinct TOTAL_NUM2_4_WEEK),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2_4_WEEK) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_NO MONTH_NO "
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+1)b," +
	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2 from meta_dm.d_v_channel_type_c e where e.STATE = '1' and e.VIEW_STATE = '1' start with CHANNEL_TYPE_CODE = '"+selType+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
  		  sql.append(" and a.WEEK_NO='"+week+"'");
  		sql.append(" and a.MONTH_NO='"+month+"' and a.WEEK_NAME IS NOT NULL ");
        sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type_c f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
        sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
        sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_c a1, meta_dm.d_v_channel_type_c a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE ORDER BY T2.ORDER_ID");
         Map<String,Object> list= getDataAccess().queryForMap(sql.toString());
	       return list;
  }  
	//全渠道总体分析周报表柱状图
	public List<Map<String, Object>> getBarChannelGlobal_Day(Map<String, Object> queryData){
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		String dateTime = Convert.toString(queryData.get("dateTime"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,(sum(SERVICE_NUM)-sum(QUERY_NUM1)-sum(QUERY_NUM2)-sum(QUERY_NUM3)-sum(QUERY_NUM4)-sum(QUERY_NUM5)-sum(CONSULT_NUM))SERVICE_NUM2,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM1) QUERY_NUM1,sum(QUERY_NUM2) QUERY_NUM2,sum(QUERY_NUM3) QUERY_NUM3,sum(QUERY_NUM4) QUERY_NUM4,sum(QUERY_NUM5) QUERY_NUM5,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM1) DEAL_NUM1,sum(DEAL_NUM2) DEAL_NUM2,sum(DEAL_NUM3) DEAL_NUM3,sum(DEAL_NUM4) DEAL_NUM4,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,decode(sum(distinct TOTAL_NUM_4_WEEK),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM_4_WEEK)*100,2)) PERC_NUM,decode(sum(distinct TOTAL_NUM2_4_WEEK),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2_4_WEEK)*100,2)) PERC_NUM2,a.MONTH_NO MONTH_NO "
	             +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=2 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.WEEK_NAME='"+dateTime+"'");
	     }
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.MONTH_NO) z ");
	   sql.append(" order by z.PAR_REGION_ID, ORDER_ID,z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	
	//全渠道总体分析日报表饼状图
	public List<Map<String, Object>> getAllBussinessPieList_Day(Map<String, Object> queryData){
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		String dateTime = Convert.toString(queryData.get("dateTime"));
		String showAllChannel=Convert.toString(queryData.get("showAllChannel"));
		dateTime=dateTime.replaceAll("-", "");
		StringBuffer sql = new StringBuffer();
		if(channelTypeCode.equals("1")&&showAllChannel.equals("false")){
			sql.append("select CHANNEL_TYPE_PAR_CODE as CHANNEL_TYPE_CODE, CHANNEL_TYPE_PAR_NAME as CHANNEL_TYPE_NAME,  sum(SERVICE_NUM) as SERVICE_NUM, sum(PAY_NUM) as PAY_NUM,  sum(QUERY_NUM1) as QUERY_NUM1,  sum(QUERY_NUM2) as QUERY_NUM2, sum(QUERY_NUM3) as QUERY_NUM3,  sum(QUERY_NUM4) as QUERY_NUM4,   sum(QUERY_NUM5) as QUERY_NUM5,   sum(CONSULT_NUM) as CONSULT_NUM,  sum(DEAL_NUM1) as DEAL_NUM1, sum(DEAL_NUM2) as DEAL_NUM2,    sum(DEAL_NUM3) as DEAL_NUM3,   sum(DEAL_NUM4) as DEAL_NUM4,   sum(COMPLAIN_NUM) as COMPLAIN_NUM, sum(FAULT_NUM) as FAULT_NUM,   sum(OTHER_NUM) as OTHER_NUM , sum(PERC_NUM) as PERC_NUM from (select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
		    		  "CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME, a.CHANNEL_TYPE_ID) = '3' AND count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME END CHANNEL_TYPE_NAME,nvl(a.CHANNEL_TYPE_ID,'99') CHANNEL_TYPE_ID," +
		    		  "NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
			      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
			      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM)*100,2)) PERC_NUM,decode(sum(distinct TOTAL_NUM2),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2)*100,2)) PERC_NUM2"
		              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"') + 1)b," +
		              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
		              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		      sql.append(" and a.day_id>='"+dateTime+"'");
		      sql.append(" and a.day_id<='"+dateTime+"'");
		      sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID ) in ('0','3') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL");
	          sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID,z.CHANNEL_TYPE_ID");
	          sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_c a1, meta_dm.d_v_channel_type_c a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE group by CHANNEL_TYPE_PAR_CODE,CHANNEL_TYPE_PAR_NAME ORDER BY 1");
		}
		else{
			sql.append("select * from (select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
		    		  "CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME, a.CHANNEL_TYPE_ID) = '3' AND count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME END CHANNEL_TYPE_NAME,nvl(a.CHANNEL_TYPE_ID,'99') CHANNEL_TYPE_ID," +
		    		  "NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
			      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
			      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM)*100,2)) PERC_NUM,decode(sum(distinct TOTAL_NUM2),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2)*100,2)) PERC_NUM2"
		              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"') + 1)b," +
		              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
		              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		      sql.append(" and a.day_id>='"+dateTime+"'");
		      sql.append(" and a.day_id<='"+dateTime+"'");
		      sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID ) in ('0','3') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL");
	          sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID,z.CHANNEL_TYPE_ID");
	          sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_c a1, meta_dm.d_v_channel_type_c a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE ");
		}
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析周报表饼状图
	public List<Map<String, Object>> getAllBussinessPieList_Week(Map<String, Object> queryData){
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		String dateTime = Convert.toString(queryData.get("dateTime"));
		String showAllChannel=Convert.toString(queryData.get("showAllChannel"));
		StringBuffer sql = new StringBuffer();
		if(channelTypeCode.equals("1")&&showAllChannel.equals("false")){	          
	          sql.append("select CHANNEL_TYPE_PAR_CODE as CHANNEL_TYPE_CODE, CHANNEL_TYPE_PAR_NAME as CHANNEL_TYPE_NAME,  sum(SERVICE_NUM) as SERVICE_NUM, sum(PAY_NUM) as PAY_NUM,  sum(QUERY_NUM1) as QUERY_NUM1,  sum(QUERY_NUM2) as QUERY_NUM2, sum(QUERY_NUM3) as QUERY_NUM3,  sum(QUERY_NUM4) as QUERY_NUM4,   sum(QUERY_NUM5) as QUERY_NUM5,   sum(CONSULT_NUM) as CONSULT_NUM,  sum(DEAL_NUM1) as DEAL_NUM1, sum(DEAL_NUM2) as DEAL_NUM2,    sum(DEAL_NUM3) as DEAL_NUM3,   sum(DEAL_NUM4) as DEAL_NUM4,   sum(COMPLAIN_NUM) as COMPLAIN_NUM, sum(FAULT_NUM) as FAULT_NUM,   sum(OTHER_NUM) as OTHER_NUM , sum(PERC_NUM) as PERC_NUM from  (select  z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	  	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2) = '3' AND  count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME2 END CHANNEL_TYPE_NAME,nvl(c.CHANNEL_TYPE_code2, '99') CHANNEL_TYPE_ID," +
	  	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
	  	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
	  	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM_4_WEEK),'0','0.00',trim(to_char(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM_4_WEEK) * 100,'FM99990.00'))) PERC_NUM,decode(sum(distinct TOTAL_NUM2_4_WEEK),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2_4_WEEK) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_NO MONTH_NO "
	  	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+1)b," +
	  	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2 from meta_dm.d_v_channel_type_c e where e.STATE = '1' and e.VIEW_STATE = '1' start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	  	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
	  	      if(dateTime!=null&&!("".equals(dateTime))){
	      		  sql.append(" and a.WEEK_NAME='"+dateTime+"'");
	           }
	            sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type_c f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
	            sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
	            sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_c a1, meta_dm.d_v_channel_type_c a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE GROUP BY CHANNEL_TYPE_PAR_CODE,CHANNEL_TYPE_PAR_NAME ORDER BY 1");

		}
		else{
			  sql.append("select * from (select  z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
		  	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2) = '3' AND  count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME2 END CHANNEL_TYPE_NAME,nvl(c.CHANNEL_TYPE_code2, '99') CHANNEL_TYPE_ID," +
		  	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
		  	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
		  	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM_4_WEEK),'0','0.00',trim(to_char(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM_4_WEEK) * 100,'FM99990.00'))) PERC_NUM,decode(sum(distinct TOTAL_NUM2_4_WEEK),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2_4_WEEK) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_NO MONTH_NO "
		  	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+1)b," +
		  	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2 from meta_dm.d_v_channel_type_c e where e.STATE = '1' and e.VIEW_STATE = '1' start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
		  	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		  	      if(dateTime!=null&&!("".equals(dateTime))){
		      		  sql.append(" and a.WEEK_NAME='"+dateTime+"'");
		           }
		            sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type_c f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
		            sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
		            sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_c a1, meta_dm.d_v_channel_type_c a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE");
		}
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析月报表饼状图
	public List<Map<String, Object>> getAllBussinessPieList_Mon(Map<String, Object> queryData){
		Integer year = Convert.toInt(Convert.toString(queryData.get("dateTime")).substring(0, 4));
		Integer month = Convert.toInt(Convert.toString(queryData.get("dateTime")).substring(4, 6));
		String startDate=Convert.toString(queryData.get("dateTime"))+"01";
		String endDate=DateUtil.getLastDayOfMonth(year, month);
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		String dateTime = Convert.toString(queryData.get("dateTime"));
		String showAllChannel=Convert.toString(queryData.get("showAllChannel"));
		StringBuffer sql = new StringBuffer();
		if(channelTypeCode.equals("1")&&showAllChannel.equals("false")){	          
			 	 sql.append("select CHANNEL_TYPE_PAR_CODE as CHANNEL_TYPE_CODE, CHANNEL_TYPE_PAR_NAME as CHANNEL_TYPE_NAME,  sum(SERVICE_NUM) as SERVICE_NUM, sum(PAY_NUM) as PAY_NUM,  sum(QUERY_NUM1) as QUERY_NUM1,  sum(QUERY_NUM2) as QUERY_NUM2, sum(QUERY_NUM3) as QUERY_NUM3,  sum(QUERY_NUM4) as QUERY_NUM4,   sum(QUERY_NUM5) as QUERY_NUM5,   sum(CONSULT_NUM) as CONSULT_NUM,  sum(DEAL_NUM1) as DEAL_NUM1, sum(DEAL_NUM2) as DEAL_NUM2,    sum(DEAL_NUM3) as DEAL_NUM3,   sum(DEAL_NUM4) as DEAL_NUM4,   sum(COMPLAIN_NUM) as COMPLAIN_NUM, sum(FAULT_NUM) as FAULT_NUM,   sum(OTHER_NUM) as OTHER_NUM , sum(PERC_NUM) as PERC_NUM from  (select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
		  	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2) = '3' AND  count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME2 END CHANNEL_TYPE_NAME,nvl(c.CHANNEL_TYPE_code2, '99') CHANNEL_TYPE_ID," +
		  	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
		  	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
		  	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM),'0','0.00',trim(to_char(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM) * 100,'FM99990.00'))) PERC_NUM,decode(sum(distinct TOTAL_NUM2),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_NO MONTH_NO "
		  	              +" from CS_CHANNEL_GLOBAL_DAY_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+1)b," +
		  	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2 from meta_dm.d_v_channel_type_c e where e.STATE = '1' and e.VIEW_STATE = '1' start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
		  	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		  	      if(dateTime!=null&&!("".equals(dateTime))){
		      		  sql.append(" and a.month_no='"+dateTime+"'");
		           }
		            sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type_c f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
		            sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
		            sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_c a1, meta_dm.d_v_channel_type_c a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE GROUP BY CHANNEL_TYPE_PAR_CODE,CHANNEL_TYPE_PAR_NAME ORDER BY 1");

		}
		else{
			 sql.append("select * from(select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
		  	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2) = '3' AND  count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME2 END CHANNEL_TYPE_NAME,nvl(c.CHANNEL_TYPE_code2, '99') CHANNEL_TYPE_ID," +
		  	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
		  	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
		  	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM),'0','0.00',trim(to_char(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM) * 100,'FM99990.00'))) PERC_NUM,decode(sum(distinct TOTAL_NUM2),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_NO MONTH_NO "
		  	              +" from CS_CHANNEL_GLOBAL_MONTH_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+1)b," +
		  	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2 from meta_dm.d_v_channel_type_c e where e.STATE = '1' and e.VIEW_STATE = '1' start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
		  	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		  	      if(dateTime!=null&&!("".equals(dateTime))){
		      		  sql.append(" and a.month_no='"+dateTime+"'");
		           }
		            sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type_c f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
		            sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
		            sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_c a1, meta_dm.d_v_channel_type_c a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE");		
		            }
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	
	//全渠道总体分析周报表柱状图21地市
	public List<Map<String, Object>> get21BarChannelGlobal_Day(Map<String, Object> queryData){
		String zoneCode ="0000";
		String dateTime = Convert.toString(queryData.get("dateTime"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"sum(SERVICE_NUM) SERVICE_NUM,(sum(SERVICE_NUM)-sum(QUERY_NUM1)-sum(QUERY_NUM2)-sum(QUERY_NUM3)-sum(QUERY_NUM4)-sum(QUERY_NUM5)-sum(CONSULT_NUM))SERVICE_NUM2,sum(PAY_NUM) PAY_NUM,sum(QUERY_NUM1) QUERY_NUM1,sum(QUERY_NUM2) QUERY_NUM2,sum(QUERY_NUM3) QUERY_NUM3,sum(QUERY_NUM4) QUERY_NUM4,sum(QUERY_NUM5) QUERY_NUM5,sum(CONSULT_NUM) CONSULT_NUM,sum(DEAL_NUM1) DEAL_NUM1,sum(DEAL_NUM2) DEAL_NUM2,sum(DEAL_NUM3) DEAL_NUM3,sum(DEAL_NUM4) DEAL_NUM4,sum(FEEDBACK_NUM) FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM) FEEDBACK2_NUM,sum(CHANGE_NUM) CHANGE_NUM,sum(COMPLAIN_NUM) COMPLAIN_NUM,sum(FAULT_NUM) FAULT_NUM," +
	      		"sum(OTHER_NUM) OTHER_NUM,decode(sum(distinct TOTAL_NUM_4_WEEK),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM_4_WEEK)*100,2)) PERC_NUM,decode(sum(distinct TOTAL_NUM2_4_WEEK),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2_4_WEEK)*100,2)) PERC_NUM2,a.MONTH_NO MONTH_NO "
	            +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=3 start with zone_code ='"+zoneCode+"' connect by prior zone_code = zone_par_code)b," 
	            +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_c start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.WEEK_NAME='"+dateTime+"'");
	     }
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.MONTH_NO) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	
	//按月查询最大周
	public String queryMaxWeek(String WeekMon) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select Max(WEEK_NO) from CS_CHANNEL_GLOBAL_DAY_New where MONTH_NO=? ");
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
		 String ind="".equals(Convert.toString(queryData.get("selectCol")))?"1":Convert.toString(queryData.get("selectCol"));//默认
		 String sql="select rownum,region_id,zone_name,current_value from (select region_id,zone_name,case when max(ind_id) in('1','2','3','4','5','6') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2)),'FM99990.00') when max(ind_id) in('7') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2),2))) else to_char(round(sum(NUM1),2)) end current_value" +
		 		" from CS_CHANNEL_MONITOR_DAY a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+1 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and DAY_ID='"+dateTime+"' and ind_id='"+ind+"'";
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//渠道服务类指标日监测报表柱状图下级地市循环_网格数据
	public List<Map<String, Object>> getBarChannelSerMonitorDay_DownZone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String ind="".equals(Convert.toString(queryData.get("indId")))?"1":Convert.toString(queryData.get("indId"));//默认
		 String sql="select rownum,region_id,zone_name,current_value from (select region_id,zone_name,case when max(ind_id) in('1','2','3','4','5','6') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2)),'FM99990.00') when max(ind_id) in('7') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2),2))) else to_char(round(sum(NUM1),2)) end current_value" +
		 		" from CS_CHANNEL_MONITOR_DAY a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+2 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and DAY_ID='"+dateTime+"' and ind_id='"+ind+"'";
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
	
	   //全渠道服务_日、月、周存储过程
	public Map<String, Object> getChannelGlobalSer_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CS_CHANNEL_SERVICE;
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
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
	
     /////////////////////////////////////////
	////////////////////////////////////////////
	//////////////////////////////////////////新渠道服务报表方法
	//按照渠道类型获取渠道小类列表
	public List<Map<String, Object>> getChannelTypeList(String channelType) {
		String strSql="";
		if("1".equals(channelType)||channelType=="1"){
			strSql="select CHANNEL_TYPE_CODE as CHANNEL_TYPE_ID,CHANNEL_TYPE_NAME,ORDER_ID from " +
					"meta_dm.d_v_channel_type_new t where DIM_LEVEL='2' and t.view_state=1 order by ORDER_ID";
		}if("2".equals(channelType)||channelType=="2"){
			strSql="select CHANNEL_TYPE_CODE as CHANNEL_TYPE_ID,CHANNEL_TYPE_NAME,ORDER_ID from " +
					"meta_dm.d_v_channel_type_new t where DIM_LEVEL='2' and t.view_state=2 order by ORDER_ID";
		}if("3".equals(channelType)||channelType=="3"){
			strSql="select CHANNEL_TYPE_CODE as CHANNEL_TYPE_ID,CHANNEL_TYPE_NAME,ORDER_ID from " +
					"meta_dm.d_v_channel_type_c where DIM_LEVEL='2' order by to_number(CHANNEL_TYPE_PAR_CODE),to_number(ORDER_ID)";
		}
		return getDataAccess().queryForList(strSql.toString());
	}
	//渠道分类报表获取最大周
	public String getMaxWeek() {
		String sql = "select rownum,WEEK_NO ,WEEK_NAME from (select distinct WEEK_NO ,WEEK_NAME from CS_CHANNEL_GLOBAL_DAY_New " +
		          "where week_name is not null  order by WEEK_NO desc) where rownum=1";
		return getDataAccess().queryForString(sql);
	}
	//渠道分类报表获取最大周
	public String getMaxWeek1(String actType) {
		String sqlTemp="";
		if("2".equals(actType)||actType=="2"){//其他渠道
			sqlTemp=" and channel_type_id in('14002','14003','12001')";
		}
		String sql = "select rownum,WEEK_NO ,WEEK_NAME from (select distinct WEEK_NO ,WEEK_NAME from CS_CHANNEL_GLOBAL_DAY_New " +
		          "where week_name is not null  "+sqlTemp+"order by WEEK_NO desc) where rownum=1";
		return getDataAccess().queryForString(sql);
	}
	//渠道分类报表获取最大周
	public String getMaxWeek2(String actType) {
		String sqlTemp="";
		if("2".equals(actType)||actType=="2"){//其他渠道
			sqlTemp=" and channel_id in('14002','14003','12001')";
		}
		String sql = "select max(week_name) from CS_CHANNEL_GLOBAL_DAY_NEW_Q where week_name is not null"+sqlTemp+" order by week_name desc";
		return getDataAccess().queryForString(sql);
	}
	//渠道分类报表获取最大周
	public String getMaxWeek(String tabStr) {
		String sql = "select rownum,WEEK_NO ,WEEK_NAME from (select distinct WEEK_NO ,WEEK_NAME from "+tabStr+
		          " where week_name is not null  order by WEEK_NO desc) where rownum=1";
		return getDataAccess().queryForString(sql);
	}
	//渠道分类报表获取最大日期
	public String getNewDay(String tabStr) {
		String sql = "select max(t.day_id)day_id from "+tabStr+" t ";
		return getDataAccess().queryForString(sql);
	}
	//渠道分类报表获取最大日期
	public String getDayNew(String tabStr) {
		String sql = " select GET_MAX_PARTITION_DATE('"+tabStr+"',8) from dual";
		return getDataAccess().queryForString(sql);
	}
	//渠道分类报表获取最大日期
	public String getNewDay(String tabStr,String actType) {
		String sqlTemp="";
		if("2".equals(actType)||actType=="2"){//其他渠道
			sqlTemp=" where channel_type_id in('14002','14003','12001')";
		}
		String sql = "select max(t.day_id)day_id from "+tabStr+" t "+sqlTemp;
		return getDataAccess().queryForString(sql);
	}
	//渠道分类报表（一、二、三级）获取最大日期
	public String getMaxDate(String tabStr,String actType) {
		String sqlTemp="";
		if("2".equals(actType)||actType=="2"){//其他渠道
			sqlTemp=" where channel_id in('14002','14003','12001')";
		}
		String sql = "select max(t.day_id)day_id from "+tabStr+" t "+sqlTemp;
		return getDataAccess().queryForString(sql);
	}
	public String getMaxDateNew(String tabStr,String actType) {
		String sqlTemp=" where DATE_TYPE='0' ";
		if("2".equals(actType)||actType=="2"){//其他渠道
			sqlTemp=" and channel_id in('14002','14003','12001')";
		}
		String sql = "select max(t.V_DATE)day_id from "+tabStr+" t "+sqlTemp;
		return getDataAccess().queryForString(sql);
	}
	//渠道分类报表获取周列表
	public List<Map<String, Object>> getWeekList() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select WEEK_NO, WEEK_NAME,MONTH_NO from (select distinct WEEK_NO as WEEK_NO,WEEK_NAME as WEEK_NAME,MONTH_NO from CS_CHANNEL_GLOBAL_DAY_New where WEEK_NAME is not null)" +
				" order by MONTH_NO desc,WEEK_NO desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//渠道分类报表获取周列表
	public List<Map<String, Object>> getWeekList1(String actType) {
		String sqlTemp="";
		if("2".equals(actType)||actType=="2"){//其他渠道
			sqlTemp=" and channel_type_id in('14002','14003','12001')";
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("select WEEK_NO, WEEK_NAME,MONTH_NO from (select distinct WEEK_NO as WEEK_NO,WEEK_NAME as WEEK_NAME,MONTH_NO from CS_CHANNEL_GLOBAL_DAY_New where WEEK_NAME is not null "
				+sqlTemp+")" +
				" order by MONTH_NO desc,WEEK_NO desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//渠道分类报表获取周列表
	public List<Map<String, Object>> getWeekList2(String actType) throws ParseException {
		String sqlTemp="";
		if("2".equals(actType)||actType=="2"){//其他渠道
			sqlTemp=" and channel_id in('14002','14003','12001')";
		}
	   StringBuffer buffer = new StringBuffer();
		//最大周的最后一天
       String endDateByWeek=getMaxWeek2(actType).substring(9,17);
       String year=endDateByWeek.substring(0,4);
       String month=endDateByWeek.substring(4,6);
       String day=endDateByWeek.substring(6,8);
       String endDate=year+"-"+month+"-"+day;
       
		//有数据的最大日
       String maxDate=getMaxDateByWeek(actType);
       String year1=maxDate.substring(0,4);
       String month1=maxDate.substring(4,6);
       String day1=maxDate.substring(6,8);
       String endDate1=year1+"-"+month1+"-"+day1;
       SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
       if(java.sql.Date.valueOf(endDate).after(java.sql.Date.valueOf(endDate1))){ //endDate>endDate1
			buffer.append("select distinct WEEK_NAME as WEEK_NAME  from CS_CHANNEL_GLOBAL_DAY_NEW_Q where WEEK_NAME is not null " +
					"and WEEK_NAME<>(select max(WEEK_NAME)from CS_CHANNEL_GLOBAL_DAY_NEW_Q)"
					+sqlTemp +
					" order by WEEK_NAME desc");
		}else{//等于
			buffer.append("select WEEK_NAME from (select distinct WEEK_NAME as WEEK_NAME  from CS_CHANNEL_GLOBAL_DAY_NEW_Q where WEEK_NAME is not null "
					+sqlTemp+")" +
					" order by WEEK_NAME desc");
		}
		return getDataAccess().queryForList(buffer.toString());	
	}
	//查询最大日期by qx
	public String getMaxDateByWeek(String actType){
		String sqlTemp="";
		if("2".equals(actType)||actType=="2"){//其他渠道
			sqlTemp=" and channel_id in('14002','14003','12001')";
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("select max(v_date) from cs_channel_view where v_date is not null"+sqlTemp);
		return getDataAccess().queryForString(buffer.toString());
	}
	//渠道分类报表获取周列表
	public List<Map<String, Object>> getWeekList(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select WEEK_NO, WEEK_NAME,MONTH_NO from (select distinct WEEK_NO as WEEK_NO,WEEK_NAME as WEEK_NAME,MONTH_NO from "+tabStr+" where WEEK_NAME is not null)" +
				" order by MONTH_NO desc,WEEK_NO desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//按照查询周查询月和周
	public Map<String, Object> queryMonthAndWeek(String WeekMon) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select MONTH_NO,WEEK_NO from (select distinct MONTH_NO,WEEK_NO from CS_CHANNEL_GLOBAL_DAY_New where WEEK_NAME=? order by WEEK_NO desc)");
		return getDataAccess().queryForMap(buffer.toString(), WeekMon);
	}
	//按照查询周查询月（一、二、三级报表）
	public Map<String, Object> getMonth(String WeekId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select MONTH_NO from (select to_char(to_date(substr('"+WeekId+"', 10, 8), 'yyyymmdd') - 3,'yyyymm')MONTH_NO from dual )");
		return getDataAccess().queryForMap(buffer.toString());
	}
	//按照查询周查询周号（一、二、三级报表）
	public Map<String, Object> getWeekNo(String WeekId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select WEEK_NO from (select ceil(substr(to_char(to_date(substr('"+WeekId+"',10,8), 'yyyymmdd') - 3, 'yyyymmdd'), 7, 2) / 7) week_no from dual )");
		return getDataAccess().queryForMap(buffer.toString());
	}
	//按照查询周列表（一、二、三级报表）
	public String getCurWeekArray(String WeekId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select GET_BELONG_PER_WEEK('"+WeekId+"') from dual");
		return getDataAccess().queryForString(buffer.toString());
	}
	//按照查询周列表（一、二、三级报表）
	public String getLastWeekArray(String WeekId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select GET_BELONG_PER_MONTH_WEEK('"+WeekId+"') from dual");
		return getDataAccess().queryForString(buffer.toString());
	}
	//获取查询条件_月份
	public List<Map<String, Object>> getSelectMon() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct MONTH_ID as month_id from CS_CHANNEL_GLOBAL_DAY_NEW  where month_id is not null  order by MONTH_ID desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	//获取查询条件_月份
	public List<Map<String, Object>> getSelectMon1(String actType) {
		String sqlTemp="";
		if("2".equals(actType)||actType=="2"){//其他渠道
			sqlTemp=" and channel_type_id in('14002','14003','12001')";
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct MONTH_ID as month_id from CS_CHANNEL_GLOBAL_DAY_NEW  where month_id is not null " +
				sqlTemp+" order by MONTH_ID desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	/****
	 * 获取首次加载的日期
	 * @param actType
	 * @param vDateType
	 * @param tabStr
	 * @return
	 */
	public String getMaxDate_ChannelServ(String actType,String vDateType,String tabStr){
		
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
	/***
	 * 获取周月的全部日期数据
	 * @param actType
	 * @param dateType
	 * @param tabStr
	 * @return
	 */
	public List<Map<String, Object>>getDateTimeList_ChannelServ(String actType,String dateType,String tabStr){
		 
		StringBuffer buffer = new StringBuffer();
		if(dateType.equals("2")){
			buffer.append("select   substr(t.partition_name,2) DATE_NO from SYS.USER_SEGMENTS t where t.segment_name='"+tabStr+"' and length(substr(t.partition_name,2))=6");
		}else{
			buffer.append("select  replace(substr(t.partition_name,2),'_','~') DATE_NO from SYS.USER_SEGMENTS t where t.segment_name='"+tabStr+"' and length(substr(t.partition_name,2))=17");
		}
		
		return getDataAccess().queryForList(buffer.toString());
	}
	//获取查询条件_月份
	public List<Map<String, Object>> getSelectMon2(String actType) {
		String sqlTemp="";
		if("2".equals(actType)||actType=="2"){//其他渠道
			sqlTemp=" and channel_id in('14002','14003','12001')";
		}
	   StringBuffer buffer = new StringBuffer();
		//有数据的最大天20140831
       String maxDate=getMaxDate("CS_CHANNEL_GLOBAL_DAY_NEW_Q",actType);
       String year=maxDate.substring(0,4);
       String month=maxDate.substring(4,6);
       String day=maxDate.substring(6,8);
       String endDate=year+"-"+month+"-"+day;
       //有数据的月份的最后一天20140831
       String lastDay=DateUtil.getLastDayOfMonth(Integer.parseInt(year),Integer.parseInt(month));    //
       String year1=lastDay.substring(0,4);
       String month1=lastDay.substring(4,6);
       String day1=lastDay.substring(6,8);
       String endDate1=year1+"-"+month1+"-"+day1;
       if(java.sql.Date.valueOf(endDate1).after(java.sql.Date.valueOf(endDate))){ //endDate1>endDate
			buffer.append("select distinct MONTH_ID as MONTH_ID  from CS_CHANNEL_GLOBAL_DAY_NEW_Q where MONTH_ID is not null " +
					"and MONTH_ID<>(select max(MONTH_ID)from CS_CHANNEL_GLOBAL_DAY_NEW_Q)"
					+sqlTemp +
					" order by MONTH_ID desc");
		}else{//等于
			buffer.append("select MONTH_ID from (select distinct MONTH_ID as MONTH_ID  from CS_CHANNEL_GLOBAL_DAY_NEW_Q where MONTH_ID is not null "
					+sqlTemp+")" +
					" order by MONTH_ID desc");
		}
		return getDataAccess().queryForList(buffer.toString());
	}
	//获取查询条件_月份
	public List<Map<String, Object>> getSelectMon(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct MONTH_ID as month_id from "+tabStr+"  where month_id is not null  order by MONTH_ID desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	//获取最大月份
	public String getNewMonth() {
		String sql = "select max(t.MONTH_ID) month_no from CS_CHANNEL_GLOBAL_DAY_NEW t ";
		return getDataAccess().queryForString(sql);
	}
	//获取最大月份
	public String getNewMonth1(String actType) {
		String sqlTemp="";
		if("2".equals(actType)||actType=="2"){//其他渠道
			sqlTemp=" where channel_type_id in('14002','14003','12001')";
		}
		String sql = "select max(t.MONTH_ID) month_no from CS_CHANNEL_GLOBAL_DAY_NEW t "+sqlTemp;
		return getDataAccess().queryForString(sql);
	}
	//获取最大月份
	public String getNewMonth2(String actType) {
		String sqlTemp="";
		if("2".equals(actType)||actType=="2"){//其他渠道
			sqlTemp=" where channel_id in('14002','14003','12001')";
		}
		String sql = "select max(t.MONTH_ID) month_no from cs_channel_view t "+sqlTemp;
		return getDataAccess().queryForString(sql);
	}
	//获取最大月份
	public String getNewMonth(String tabStr) {
		String sql = "select max(t.MONTH_ID) month_no from  "+tabStr+"  t";
		return getDataAccess().queryForString(sql);
	}
	//获取渠道信息
	public Map<String,Object> getChannel(String channelCode,String actType){
		String strSql="";
		if("3".equals(actType)||actType=="3"){
			strSql="meta_dm.d_v_channel_type_c";
		}else{
			strSql="meta_dm.d_v_channel_type_new";
		}
		StringBuffer sql = new StringBuffer(" select * from "+strSql+" where CHANNEL_TYPE_CODE='"+channelCode+"'");
		return getDataAccess().queryForMap(sql.toString());
	}
	//渠道分类_日、月、周存储过程
	public Map<String, Object> getChannelSerClass_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CS_CHANNEL_SERVICE;
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
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
	//渠道总体目标_日、月、周存储过程
	public Map<String, Object> getChannelOverGoals_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CS_CHANNEL_OVER_GOALS;
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
	//渠道偏好_日、月、周存储过程
	public Map<String, Object> queryChannelPrefer_pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String preferType = Convert.toString(queryData.get("preferType"));
		String storeName="";
		if("1".equals(preferType)||preferType=="1"){
			storeName=ConstantStoreProc.RPT_CS_CHANNEL_SAT_PREFER;//满意率
		}else if("2".equals(preferType)||preferType=="2"){
			storeName=ConstantStoreProc.RPT_CS_CHANNEL_COMP_PREFER;//抱怨率
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
		return mapList;
	}
	//渠道分类_日、月、周存储过程(一级)
	public Map<String, Object> getChannelSerClassFirst_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CS_CHANNEL_SERVICE_FIRST;
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
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
	//渠道分类_日、月、周存储过程(二级)
	public Map<String, Object> getChannelSerClassSecond_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CS_CHANNEL_SERVICE_SECOND;
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
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
	//渠道分类_日、月、周存储过程(三级)
	public Map<String, Object> getChannelSerClassThird_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CS_CHANNEL_SERVICE_THIRD;
		Object[] params = {
				MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endDate", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "channelTypeCode", null),
				MapUtils.getString(queryData, "dateType", null),
				MapUtils.getString(queryData, "actType", null),
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
	
	//渠道分类_按周取数
	public  Map<String,Object> getChartDataWeek_ChannelSerClass(String weekNo,String monthNo, Map<String,Object> map) {
		String channelTypeCode = Convert.toString(map.get("channelTypeCode"));
	    String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
	    String actType = Convert.toString(map.get("actType"));
		String strTable="";
		String tempStr="";
	    if("1".equals(actType)||actType=="1"){//传统新媒体
			strTable="meta_dm.d_v_channel_type_new";
			tempStr=" and c.view_state='1'";
		}if("2".equals(actType)||actType=="2"){//其他
			strTable="meta_dm.d_v_channel_type_new";
			tempStr=" and c.view_state='2'";
		}if("3".equals(actType)||actType=="3"){//人工自助
			strTable="meta_dm.d_v_channel_type_c";
		}
        StringBuffer sql = new StringBuffer("select sum(SERVICE_NUM)SERVICE_NUM,sum(SERVICE_NUM2)SERVICE_NUM2,sum(PAY_NUM)PAY_NUM," +
      		"sum(QUERY_NUM1)QUERY_NUM1,sum(QUERY_NUM2)QUERY_NUM2,sum(QUERY_NUM3)QUERY_NUM3,sum(QUERY_NUM4)QUERY_NUM4,sum(QUERY_NUM5)QUERY_NUM5," +
      		"sum(QUERY_NUM1)+sum(QUERY_NUM2)+sum(QUERY_NUM3)+sum(QUERY_NUM4)+sum(QUERY_NUM5)QUERY_NUM,sum(CONSULT_NUM)CONSULT_NUM," +
      		"sum(DEAL_NUM1)DEAL_NUM1,sum(DEAL_NUM2)DEAL_NUM2,sum(DEAL_NUM3)DEAL_NUM3,sum(DEAL_NUM4)DEAL_NUM4," +
      		"sum(DEAL_NUM1)+sum(DEAL_NUM2)+sum(DEAL_NUM3)+sum(DEAL_NUM4)DEAL_NUM,sum(FEEDBACK_NUM)FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM)FEEDBACK2_NUM,sum(CHANGE_NUM)CHANGE_NUM,sum(COMPLAIN_NUM)COMPLAIN_NUM,sum(FAULT_NUM)FAULT_NUM," +
	      		"sum(OTHER_NUM)OTHER_NUM,decode(sum(distinct TOTAL_NUM),'0','0.00',round(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM) * 100,2)) PERC_NUM," +
	      		"decode(sum(distinct TOTAL_NUM2),'0','0.00',round(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2) * 100,2)) PERC_NUM2 "
	              +" from CS_CHANNEL_GLOBAL_DAY_NEW a,"
	              +"(select CHANNEL_TYPE_CODE,view_state from "+strTable+" start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c ");
  		  sql.append("where a.channel_type_id=c.channel_type_code "+tempStr +" and region_id='"+zoneCode+"'  and month_no='"+monthNo+"' and week_no='"+weekNo+"'");
	  return getDataAccess().queryForMap(sql.toString());
	}
	
    public String getTotalNum(Map<String, Object> queryData,String tempTotal) {
    	String tempTab="";
    	if("0".equals(tempTotal)||tempTotal=="0"){//新媒体
    		tempTab="meta_dm.d_v_channel_type_new";
    	}else{//自助
    		tempTab="meta_dm.d_v_channel_type_c";
    	}
    	String dateType = Convert.toString(queryData.get("dateType"));
		String startDate=Convert.toString(queryData.get("startDate")).replaceAll("-", "");
		String endDate=Convert.toString(queryData.get("endDate")).replaceAll("-", "");
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String tempStr="";
		if("0".equals(dateType)||dateType=="0"){//日
			tempStr=" and V_DATE>='"+startDate+"' and V_DATE<='"+endDate+"' and DATE_TYPE='0' ";
		}if("1".equals(dateType)||dateType=="1"){//周
			tempStr=" and V_DATE='"+startDate+"' and DATE_TYPE='1'";
		}if("2".equals(dateType)||dateType=="2"){//月
			tempStr=" V_DATE='"+startDate+"' and DATE_TYPE='2'";
		}
		StringBuffer sql = new StringBuffer("select sum(cnt_num) FROM CS_CHANNEL_VIEW A,"+tempTab+" C WHERE A.CHANNEL_ID = C.CHANNEL_TYPE_CODE" +
				" and c.channel_type_par_code in('10','14') and region_treecode like gettreecode('"+zoneCode+"')||'%'"+tempStr);
		return getDataAccess().queryForString(sql.toString());
	}
	public  List<Map<String, Object>>  getBarChannnelOverGoals(Map<String,Object> map) {
		String dateType = Convert.toString(map.get("dateType"));
		String startDate=Convert.toString(map.get("startDate")).replaceAll("-", "");
		String endDate=Convert.toString(map.get("endDate")).replaceAll("-", "");
		String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		String tempStr="";
	    if("0".equals(dateType)||dateType=="0"){//日
			tempStr=" and day_id>='"+startDate+"' and day_id<='"+endDate+"'";
		}if("1".equals(dateType)||dateType=="1"){//周
			tempStr=" and week_id='"+startDate+"'";
		}if("2".equals(dateType)||dateType=="2"){//月
			tempStr="month_id='"+startDate+"'";
		}
		String totalNum0=getTotalNum(map,"0");
		String totalNum1=getTotalNum(map,"1");
		StringBuffer sql = new StringBuffer("select CASE WHEN CHANNEL_ID='0' THEN '新媒体渠道' WHEN CHANNEL_ID='1' THEN '自助渠道' end CHANNEL_NAME," +
				" CASE WHEN CHANNEL_ID='0' THEN ROUND(sum(ELEMENT_NUM)/"+totalNum0+"*100,2)" +
				" WHEN CHANNEL_ID='1' THEN ROUND(SUM(ELEMENT_NUM)/"+totalNum1+"*100,2) END ELEMENT_NUM from CS_CHANNEL_OVERALL_GOALS" +
				" where region_treecode like gettreecode('"+zoneCode+"')||'%'"+tempStr+" group by channel_id order by channel_id");
		return getDataAccess().queryForList(sql.toString());
	}
	//渠道分类_按周取数(一、二、三级)
	public  Map<String,Object> getChartDataWeek_FirstSecThi(String weekId, Map<String,Object> map) {
		String channelTypeCode = Convert.toString(map.get("channelTypeCode"));
	    String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
	    String actType = Convert.toString(map.get("actType"));
		String strTable="";
		String tempStr="";
	    if("1".equals(actType)||actType=="1"){//传统新媒体
			strTable="meta_dm.d_v_channel_type_new";
			tempStr=" and c.view_state='1'";
		}if("2".equals(actType)||actType=="2"){//其他
			strTable="meta_dm.d_v_channel_type_new";
			tempStr=" and c.view_state='2'";
		}if("3".equals(actType)||actType=="3"){//人工自助
			strTable="meta_dm.d_v_channel_type_c";
		}
		String level="".equals(Convert.toString(map.get("level")))?"1":Convert.toString(map.get("level"));
		String sumService="";
		if("1".equals(level)||level=="1"){//一级	
			sumService="sum(cnt_num)";
		}if("2".equals(level)||level=="2"){//二级
			sumService="sum(case when channel_serv_id like '10%' OR channel_serv_id like '30%' then cnt_num end)";
		}if("3".equals(level)||level=="3"){//三级
			sumService="sum(case when channel_serv_id like '30%' then cnt_num end)";
		}
        StringBuffer sql = new StringBuffer("select "+sumService+"SERVICE_NUM,sum(case when channel_serv_id like '60%' then cnt_num end)PAY_NUM," +
      		"sum(case when channel_serv_id like '10%' then cnt_num end)QUERY_NUM,sum(case when channel_serv_id like '30%' then cnt_num end)DEAL_NUM," +
      		"sum(case when channel_serv_id like '20%' then cnt_num end)CONSULT_NUM,sum(case when channel_serv_id like '40%' then cnt_num end)COMPLAIN_NUM," +
      		"sum(case when channel_serv_id like '50%' then cnt_num end)FAULT_NUM,sum(case when channel_serv_id like '99%' then cnt_num end)OTHER_NUM," +
      		"sum(case when channel_serv_id like '1001%' then cnt_num end)QUERY_NUM1,sum(case when channel_serv_id like '1002%' then cnt_num end)QUERY_NUM2," +
      		"sum(case when channel_serv_id like '1003%' then cnt_num end)QUERY_NUM3,sum(case when channel_serv_id like '1004%' then cnt_num end)QUERY_NUM4," +
      		"sum(case when channel_serv_id like '1005%' then cnt_num end)QUERY_NUM5,sum(case when channel_serv_id like '3001%' then cnt_num end)DEAL_NUM1," +
      		"sum(case when channel_serv_id like '3002%' then cnt_num end)DEAL_NUM2,sum(case when channel_serv_id like '3003%' then cnt_num end)DEAL_NUM3," +
      		"sum(case when channel_serv_id like '3004%' then cnt_num end)DEAL_NUM4," +
      		"sum(case when channel_serv_id like '30010001%' then cnt_num end)DEAL_NUM11,sum(case when channel_serv_id like '30010002%' then cnt_num end)DEAL_NUM12," +
      		"sum(case when channel_serv_id like '30010003%' then cnt_num end)DEAL_NUM13,sum(case when channel_serv_id like '30010004%' then cnt_num end)DEAL_NUM14," +
      		"sum(case when channel_serv_id like '30020001%' then cnt_num end)DEAL_NUM21,sum(case when channel_serv_id like '30020002%' then cnt_num end)DEAL_NUM22," +
      		"sum(case when channel_serv_id like '30020003%' then cnt_num end)DEAL_NUM23,sum(case when channel_serv_id like '30020004%' then cnt_num end)DEAL_NUM24," +
      		"sum(case when channel_serv_id like '30020005%' then cnt_num end)DEAL_NUM25,sum(case when channel_serv_id like '30040001%' then cnt_num end)DEAL_NUM41," +
      		"sum(case when channel_serv_id like '30040001%' then cnt_num end)DEAL_NUM42,sum(case when channel_serv_id like '30040003%' then cnt_num end)DEAL_NUM43," +
      		"sum(case when channel_serv_id like '30040004%' then cnt_num end)DEAL_NUM44,sum(case when channel_serv_id like '30040005%' then cnt_num end)DEAL_NUM45," +
      		"sum(case when channel_serv_id like '30040006%' then cnt_num end)DEAL_NUM46,sum(case when channel_serv_id like '30040007%' then cnt_num end)DEAL_NUM47," +
      		"sum(case when channel_serv_id like '30040008%' then cnt_num end)DEAL_NUM48,sum(case when channel_serv_id like '30040009%' then cnt_num end)DEAL_NUM49," +
      		"sum(case when channel_serv_id like '30040010%' then cnt_num end)DEAL_NUM410,sum(case when channel_serv_id like '10010001%' then cnt_num end)QUERY_NUM11," +
      		"sum(case when channel_serv_id like '10010002%' then cnt_num end)QUERY_NUM12,sum(case when channel_serv_id like '10010003%' then cnt_num end)QUERY_NUM13," +
      		"sum(case when channel_serv_id like '10010004%' then cnt_num end)QUERY_NUM14,sum(case when channel_serv_id like '10010005%' then cnt_num end)QUERY_NUM15," +
      		"sum(case when channel_serv_id like '10010006%' then cnt_num end)QUERY_NUM16,sum(case when channel_serv_id like '10020001%' then cnt_num end)QUERY_NUM21," +
      		"sum(case when channel_serv_id like '10020002%' then cnt_num end)QUERY_NUM22,sum(case when channel_serv_id like '10020003%' then cnt_num end)QUERY_NUM23," +
      		"sum(case when channel_serv_id like '10020004%' then cnt_num end)QUERY_NUM24,sum(case when channel_serv_id like '10020005%' then cnt_num end)QUERY_NUM25," +
      		"sum(case when channel_serv_id like '10020006%' then cnt_num end)QUERY_NUM26,sum(case when channel_serv_id like '10030001%' then cnt_num end)QUERY_NUM31," +
      		"sum(case when channel_serv_id like '10030002%' then cnt_num end)QUERY_NUM32,sum(case when channel_serv_id like '10030003%' then cnt_num end)QUERY_NUM33," +
      		"sum(case when channel_serv_id like '10040001%' then cnt_num end)QUERY_NUM41,sum(case when channel_serv_id like '10040002%' then cnt_num end)QUERY_NUM42," +
      		"sum(case when channel_serv_id like '10040003%' then cnt_num end)QUERY_NUM43,sum(case when channel_serv_id like '10040004%' then cnt_num end)QUERY_NUM44," +
      		"sum(case when channel_serv_id like '10050001%' then cnt_num end)QUERY_NUM51,sum(case when channel_serv_id like '10050002%' then cnt_num end)QUERY_NUM52," +
      		"sum(case when channel_serv_id like '10050003%' then cnt_num end)QUERY_NUM53,sum(case when channel_serv_id like '10050004%' then cnt_num end)QUERY_NUM54," +
      		"sum(case when channel_serv_id like '10050005%' then cnt_num end)QUERY_NUM55,sum(case when channel_serv_id like '10050006%' then cnt_num end)QUERY_NUM56" 
	              +" from CS_CHANNEL_VIEW a,(SELECT ORDER_ID, ZONE_CODE FROM META_DIM_ZONE D WHERE D.TREECODE LIKE GETTREECODE('"+zoneCode+"')||'%' ) B,"//AND D.DIM_LEVEL  < '4'
	              +"(select CHANNEL_TYPE_CODE,view_state from "+strTable+" start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c ");
  		  sql.append("where a.CHANNEL_ID=c.channel_type_code and A.REGION_ID = B.ZONE_CODE  "+tempStr +" and V_DATE='"+weekId+"'");//and a.region_id != '0000'
	  return getDataAccess().queryForMap(sql.toString());
	}
	//渠道分类_按时间取数（日、月）
	public  Map<String,Object> getChartData_ChannelSerClass(String dateTime, Map<String,Object> map) {
		String channelTypeCode = Convert.toString(map.get("channelTypeCode"));
		String dateType = Convert.toString(map.get("dateType"));
		String strTemp="";
		if("0".equals(dateType)||dateType=="0"){//天
			strTemp="and day_id='"+dateTime+"'";
		}if("2".equals(dateType)||dateType=="2"){//月
            strTemp="and MONTH_ID='"+dateTime+"'";
		}
		String actType = Convert.toString(map.get("actType"));
		String strTable="";
		String tempStr="";
	    if("1".equals(actType)||actType=="1"){//传统新媒体
			strTable="meta_dm.d_v_channel_type_new";
			tempStr=" and c.view_state='1'";
		}if("2".equals(actType)||actType=="2"){//其他
			strTable="meta_dm.d_v_channel_type_new";
			tempStr=" and c.view_state='2'";
		}if("3".equals(actType)||actType=="3"){//人工自助
			strTable="meta_dm.d_v_channel_type_c";
		}
	    String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
        StringBuffer sql = new StringBuffer("select sum(SERVICE_NUM)SERVICE_NUM,sum(SERVICE_NUM2)SERVICE_NUM2,sum(PAY_NUM)PAY_NUM," +
      		"sum(QUERY_NUM1)QUERY_NUM1,sum(QUERY_NUM2)QUERY_NUM2,sum(QUERY_NUM3)QUERY_NUM3,sum(QUERY_NUM4)QUERY_NUM4,sum(QUERY_NUM5)QUERY_NUM5," +
      		"sum(QUERY_NUM1)+sum(QUERY_NUM2)+sum(QUERY_NUM3)+sum(QUERY_NUM4)+sum(QUERY_NUM5)QUERY_NUM,sum(CONSULT_NUM)CONSULT_NUM," +
      		"sum(DEAL_NUM1)DEAL_NUM1,sum(DEAL_NUM2)DEAL_NUM2,sum(DEAL_NUM3)DEAL_NUM3,sum(DEAL_NUM4)DEAL_NUM4," +
      		"sum(DEAL_NUM1)+sum(DEAL_NUM2)+sum(DEAL_NUM3)+sum(DEAL_NUM4)DEAL_NUM,sum(FEEDBACK_NUM)FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM)FEEDBACK2_NUM,sum(CHANGE_NUM)CHANGE_NUM,sum(COMPLAIN_NUM)COMPLAIN_NUM,sum(FAULT_NUM)FAULT_NUM," +
	      		"sum(OTHER_NUM)OTHER_NUM,decode(sum(distinct TOTAL_NUM),'0','0.00',round(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM) * 100,2)) PERC_NUM," +
	      		"decode(sum(distinct TOTAL_NUM2),'0','0.00',round(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2) * 100,2)) PERC_NUM2 "
	             +" from CS_CHANNEL_GLOBAL_DAY_NEW a," +
	            "(select CHANNEL_TYPE_CODE,view_state from "+strTable+" start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c ");
  		  sql.append("where a.channel_type_id=c.channel_type_code "+tempStr +" and region_id='"+zoneCode+"'"+strTemp);
  		return getDataAccess().queryForMap(sql.toString());
	}
	//渠道分类_按时间取数（日、月）一、二、三级
	public  Map<String,Object> getChartData_FirstSecThi(String dateTime, Map<String,Object> map) {
		String channelTypeCode = Convert.toString(map.get("channelTypeCode"));
		String dateType = Convert.toString(map.get("dateType"));
		String strTemp="";
		if("0".equals(dateType)||dateType=="0"){//天
			strTemp="and V_DATE='"+dateTime+"' and DATE_TYPE='0' ";
		}if("2".equals(dateType)||dateType=="2"){//月
            strTemp="and V_DATE='"+dateTime+"' and DATE_TYPE='2' ";
		}
		String actType = Convert.toString(map.get("actType"));
		String strTable="";
		String tempStr="";
	    if("1".equals(actType)||actType=="1"){//传统新媒体
			strTable="meta_dm.d_v_channel_type_new";
			tempStr=" and c.view_state='1'";
		}if("2".equals(actType)||actType=="2"){//其他
			strTable="meta_dm.d_v_channel_type_new";
			tempStr=" and c.view_state='2'";
		}if("3".equals(actType)||actType=="3"){//人工自助
			strTable="meta_dm.d_v_channel_type_c";
		}
		String level="".equals(Convert.toString(map.get("level")))?"1":Convert.toString(map.get("level"));
		String sumService="";
		if("1".equals(level)||level=="1"){//一级	
			sumService="sum(cnt_num)";
		}if("2".equals(level)||level=="2"){//二级
			sumService="sum(case when channel_serv_id like '10%' OR channel_serv_id like '30%' then cnt_num end)";
		}if("3".equals(level)||level=="3"){//三级
			sumService="sum(case when channel_serv_id like '30%' then cnt_num end)";
		}
	    String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
        StringBuffer sql = new StringBuffer("select "+sumService+"SERVICE_NUM,sum(case when channel_serv_id like '60%' then cnt_num end)PAY_NUM," +
      		"sum(case when channel_serv_id like '10%' then cnt_num end)QUERY_NUM,sum(case when channel_serv_id like '30%' then cnt_num end)DEAL_NUM," +
      		"sum(case when channel_serv_id like '20%' then cnt_num end)CONSULT_NUM,sum(case when channel_serv_id like '40%' then cnt_num end)COMPLAIN_NUM," +
      		"sum(case when channel_serv_id like '50%' then cnt_num end)FAULT_NUM,sum(case when channel_serv_id like '99%' then cnt_num end)OTHER_NUM," +
      		"sum(case when channel_serv_id like '1001%' then cnt_num end)QUERY_NUM1,sum(case when channel_serv_id like '1002%' then cnt_num end)QUERY_NUM2," +
      		"sum(case when channel_serv_id like '1003%' then cnt_num end)QUERY_NUM3,sum(case when channel_serv_id like '1004%' then cnt_num end)QUERY_NUM4," +
      		"sum(case when channel_serv_id like '1005%' then cnt_num end)QUERY_NUM5,sum(case when channel_serv_id like '3001%' then cnt_num end)DEAL_NUM1," +
      		"sum(case when channel_serv_id like '3002%' then cnt_num end)DEAL_NUM2,sum(case when channel_serv_id like '3003%' then cnt_num end)DEAL_NUM3," +
      		"sum(case when channel_serv_id like '3004%' then cnt_num end)DEAL_NUM4," +
      		"sum(case when channel_serv_id like '30010001%' then cnt_num end)DEAL_NUM11,sum(case when channel_serv_id like '30010002%' then cnt_num end)DEAL_NUM12," +
      		"sum(case when channel_serv_id like '30010003%' then cnt_num end)DEAL_NUM13,sum(case when channel_serv_id like '30010004%' then cnt_num end)DEAL_NUM14," +
      		"sum(case when channel_serv_id like '30020001%' then cnt_num end)DEAL_NUM21,sum(case when channel_serv_id like '30020002%' then cnt_num end)DEAL_NUM22," +
      		"sum(case when channel_serv_id like '30020003%' then cnt_num end)DEAL_NUM23,sum(case when channel_serv_id like '30020004%' then cnt_num end)DEAL_NUM24," +
      		"sum(case when channel_serv_id like '30020005%' then cnt_num end)DEAL_NUM25,sum(case when channel_serv_id like '30040001%' then cnt_num end)DEAL_NUM41," +
      		"sum(case when channel_serv_id like '30040001%' then cnt_num end)DEAL_NUM42,sum(case when channel_serv_id like '30040003%' then cnt_num end)DEAL_NUM43," +
      		"sum(case when channel_serv_id like '30040004%' then cnt_num end)DEAL_NUM44,sum(case when channel_serv_id like '30040005%' then cnt_num end)DEAL_NUM45," +
      		"sum(case when channel_serv_id like '30040006%' then cnt_num end)DEAL_NUM46,sum(case when channel_serv_id like '30040007%' then cnt_num end)DEAL_NUM47," +
      		"sum(case when channel_serv_id like '30040008%' then cnt_num end)DEAL_NUM48,sum(case when channel_serv_id like '30040009%' then cnt_num end)DEAL_NUM49," +
      		"sum(case when channel_serv_id like '30040010%' then cnt_num end)DEAL_NUM410,sum(case when channel_serv_id like '10010001%' then cnt_num end)QUERY_NUM11," +
      		"sum(case when channel_serv_id like '10010002%' then cnt_num end)QUERY_NUM12,sum(case when channel_serv_id like '10010003%' then cnt_num end)QUERY_NUM13," +
      		"sum(case when channel_serv_id like '10010004%' then cnt_num end)QUERY_NUM14,sum(case when channel_serv_id like '10010005%' then cnt_num end)QUERY_NUM15," +
      		"sum(case when channel_serv_id like '10010006%' then cnt_num end)QUERY_NUM16,sum(case when channel_serv_id like '10020001%' then cnt_num end)QUERY_NUM21," +
      		"sum(case when channel_serv_id like '10020002%' then cnt_num end)QUERY_NUM22,sum(case when channel_serv_id like '10020003%' then cnt_num end)QUERY_NUM23," +
      		"sum(case when channel_serv_id like '10020004%' then cnt_num end)QUERY_NUM24,sum(case when channel_serv_id like '10020005%' then cnt_num end)QUERY_NUM25," +
      		"sum(case when channel_serv_id like '10020006%' then cnt_num end)QUERY_NUM26,sum(case when channel_serv_id like '10030001%' then cnt_num end)QUERY_NUM31," +
      		"sum(case when channel_serv_id like '10030002%' then cnt_num end)QUERY_NUM32,sum(case when channel_serv_id like '10030003%' then cnt_num end)QUERY_NUM33," +
      		"sum(case when channel_serv_id like '10040001%' then cnt_num end)QUERY_NUM41,sum(case when channel_serv_id like '10040002%' then cnt_num end)QUERY_NUM42," +
      		"sum(case when channel_serv_id like '10040003%' then cnt_num end)QUERY_NUM43,sum(case when channel_serv_id like '10040004%' then cnt_num end)QUERY_NUM44," +
      		"sum(case when channel_serv_id like '10050001%' then cnt_num end)QUERY_NUM51,sum(case when channel_serv_id like '10050002%' then cnt_num end)QUERY_NUM52," +
      		"sum(case when channel_serv_id like '10050003%' then cnt_num end)QUERY_NUM53,sum(case when channel_serv_id like '10050004%' then cnt_num end)QUERY_NUM54," +
      		"sum(case when channel_serv_id like '10050005%' then cnt_num end)QUERY_NUM55,sum(case when channel_serv_id like '10050006%' then cnt_num end)QUERY_NUM56" 
      		 +" from CS_CHANNEL_VIEW a,(SELECT ORDER_ID, ZONE_CODE FROM META_DIM_ZONE D WHERE D.TREECODE LIKE GETTREECODE('"+zoneCode+"')||'%' ) B,"//AND D.DIM_LEVEL  < '4'
             +"(select CHANNEL_TYPE_CODE,view_state from "+strTable+" start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c ");
		  sql.append("where a.CHANNEL_ID=c.channel_type_code and A.REGION_ID = B.ZONE_CODE "+tempStr+strTemp);//and a.region_id != '0000' 
  		return getDataAccess().queryForMap(sql.toString());
	}
	//渠道分类报表饼图列表
	public List<Map<String, Object>> getChannelPieList(Map<String, Object> queryData){
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		String startDate = Convert.toString(queryData.get("startDate")).replaceAll("-", "");
		String endDate = Convert.toString(queryData.get("endDate")).replaceAll("-", "");
		String showAllChannel=Convert.toString(queryData.get("showAllChannel"));
		String dateType = Convert.toString(queryData.get("dateType"));
		String actType = Convert.toString(queryData.get("actType"));
		String strTemp="";
		String strTable="";
		String tempStr="";
		if("0".equals(dateType)||dateType=="0"){//天
			strTemp=" and day_id>='"+startDate+"' and day_id<='"+endDate+"'";
		}if("2".equals(dateType)||dateType=="2"){//月
            strTemp=" and MONTH_ID='"+startDate+"'";
		}if("1".equals(dateType)||dateType=="1"){//周
            strTemp=" and WEEK_NAME='"+startDate+"'";
		}
		if("1".equals(actType)||actType=="1"){//传统新媒体
			strTable="meta_dm.d_v_channel_type_new";
			tempStr=" and a1.view_state='1'";
		}if("2".equals(actType)||actType=="2"){//其他
			strTable="meta_dm.d_v_channel_type_new";
			tempStr=" and a1.view_state='2'";
		}if("3".equals(actType)||actType=="3"){//人工自助
			strTable="meta_dm.d_v_channel_type_c";
		}
		String level="".equals(Convert.toString(queryData.get("level")))?"1":Convert.toString(queryData.get("level"));
		String sumService="";
		if("1".equals(level)||level=="1"){
			sumService="sum(SERVICE_NUM)";
		}else{
			sumService="sum(QUERY_NUM1+QUERY_NUM2+QUERY_NUM3+QUERY_NUM4+QUERY_NUM5+DEAL_NUM1+DEAL_NUM2+DEAL_NUM3+DEAL_NUM4)";
		}
		StringBuffer sql = new StringBuffer();
		if(channelTypeCode.equals("1")){
			 if(showAllChannel.equals("false")){
				 sql.append("select CHANNEL_TYPE_PAR_CODE as CHANNEL_TYPE_CODE,CHANNEL_TYPE_PAR_NAME as CHANNEL_TYPE_NAME,"+sumService+" as SERVICE_NUM," +
							"sum(PAY_NUM) as PAY_NUM,sum(QUERY_NUM1)QUERY_NUM1,sum(QUERY_NUM2)QUERY_NUM2,sum(QUERY_NUM3)QUERY_NUM3,sum(QUERY_NUM4)QUERY_NUM4,sum(QUERY_NUM5)QUERY_NUM5," +
							"sum(QUERY_NUM1)+sum(QUERY_NUM2)+sum(QUERY_NUM3)+sum(QUERY_NUM4)+sum(QUERY_NUM5) as QUERY_NUM," +
							"sum(CONSULT_NUM) as CONSULT_NUM,sum(DEAL_NUM1)DEAL_NUM1,sum(DEAL_NUM2)DEAL_NUM2,sum(DEAL_NUM3)DEAL_NUM3,sum(DEAL_NUM4)DEAL_NUM4," +
							"sum(DEAL_NUM1)+sum(DEAL_NUM2)+sum(DEAL_NUM3)+sum(DEAL_NUM4) as DEAL_NUM," +
							"sum(COMPLAIN_NUM) as COMPLAIN_NUM,sum(FAULT_NUM) as FAULT_NUM,sum(OTHER_NUM) as OTHER_NUM from META_USER.CS_CHANNEL_GLOBAL_DAY_New t1," +
							"(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a1.view_state,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID " +
							"from "+strTable+" a1, "+strTable+" a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE "+tempStr+") t2 where " +
							" T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE");  
					 sql.append(" and region_id='"+zoneCode+"'"+strTemp);
				 sql.append(" group by CHANNEL_TYPE_PAR_CODE, CHANNEL_TYPE_PAR_NAME");
				 sql.append(" ORDER BY CHANNEL_TYPE_PAR_CODE");
			}else{
				sql.append("select a.CHANNEL_TYPE_Id as CHANNEL_TYPE_CODE,a.CHANNEL_TYPE_NAME as CHANNEL_TYPE_NAME,"+sumService+" as SERVICE_NUM," +
						"sum(PAY_NUM) as PAY_NUM,sum(QUERY_NUM1)QUERY_NUM1,sum(QUERY_NUM2)QUERY_NUM2,sum(QUERY_NUM3)QUERY_NUM3,sum(QUERY_NUM4)QUERY_NUM4,sum(QUERY_NUM5)QUERY_NUM5," +
						"sum(QUERY_NUM1)+sum(QUERY_NUM2)+sum(QUERY_NUM3)+sum(QUERY_NUM4)+sum(QUERY_NUM5) as QUERY_NUM," +
						"sum(CONSULT_NUM) as CONSULT_NUM,sum(DEAL_NUM1)+sum(DEAL_NUM2)+sum(DEAL_NUM3)+sum(DEAL_NUM4) as DEAL_NUM," +
						"sum(DEAL_NUM1)DEAL_NUM1,sum(DEAL_NUM2)DEAL_NUM2,sum(DEAL_NUM3)DEAL_NUM3,sum(DEAL_NUM4)DEAL_NUM4,sum(COMPLAIN_NUM) as COMPLAIN_NUM," +
						"sum(FAULT_NUM) as FAULT_NUM,sum(OTHER_NUM) as OTHER_NUM from CS_CHANNEL_GLOBAL_DAY_New a,(select *from "+strTable+" start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE )" +
						"a1 where a.channel_type_id=a1.channel_type_code "+tempStr);
				 sql.append(" and region_id='"+zoneCode+"'"+strTemp);
				 sql.append(" group by a.CHANNEL_TYPE_Id, a.CHANNEL_TYPE_NAME,ORDER_ID,CHANNEL_TYPE_PAR_CODE");
				 sql.append(" ORDER BY CHANNEL_TYPE_PAR_CODE,ORDER_ID,CHANNEL_TYPE_Id");
			}
		}
		else{
			sql.append("select a.CHANNEL_TYPE_Id as CHANNEL_TYPE_CODE,a.CHANNEL_TYPE_NAME as CHANNEL_TYPE_NAME,"+sumService+" as SERVICE_NUM," +
					"sum(PAY_NUM) as PAY_NUM,sum(QUERY_NUM1)QUERY_NUM1,sum(QUERY_NUM2)QUERY_NUM2,sum(QUERY_NUM3)QUERY_NUM3,sum(QUERY_NUM4)QUERY_NUM4,sum(QUERY_NUM5)QUERY_NUM5," +
					"sum(QUERY_NUM1)+sum(QUERY_NUM2)+sum(QUERY_NUM3)+sum(QUERY_NUM4)+sum(QUERY_NUM5) as QUERY_NUM," +
					"sum(CONSULT_NUM) as CONSULT_NUM,sum(DEAL_NUM1)DEAL_NUM1,sum(DEAL_NUM2)DEAL_NUM2,sum(DEAL_NUM3)DEAL_NUM3,sum(DEAL_NUM4)DEAL_NUM4," +
					"sum(DEAL_NUM1)+sum(DEAL_NUM2)+sum(DEAL_NUM3)+sum(DEAL_NUM4) as DEAL_NUM,sum(COMPLAIN_NUM) as COMPLAIN_NUM," +
					"sum(FAULT_NUM) as FAULT_NUM,sum(OTHER_NUM) as OTHER_NUM from CS_CHANNEL_GLOBAL_DAY_New a,(select *from "+strTable+" start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE )" +
					"a1 where a.channel_type_id=a1.channel_type_code "+tempStr);
			 sql.append(" and region_id='"+zoneCode+"'"+strTemp);
			 sql.append(" group by a.CHANNEL_TYPE_Id, a.CHANNEL_TYPE_NAME");
			 sql.append(" ORDER BY CHANNEL_TYPE_Id");
		}
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//渠道分类报表饼图列表(一、二、三级)
	public List<Map<String, Object>> getChannelPieList_FirstSecThi(Map<String, Object> queryData){
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
		String startDate = Convert.toString(queryData.get("startDate")).replaceAll("-", "");
		String endDate = Convert.toString(queryData.get("endDate")).replaceAll("-", "");
		String showAllChannel=Convert.toString(queryData.get("showAllChannel"));
		String dateType = Convert.toString(queryData.get("dateType"));
		String actType = Convert.toString(queryData.get("actType"));
		String strTemp="";
		String strTable="";
		String tempStr="";
		if("0".equals(dateType)||dateType=="0"){//天
			strTemp=" and V_DATE>='"+startDate+"' and V_DATE<='"+endDate+"' and DATE_TYPE='0' ";
		}if("2".equals(dateType)||dateType=="2"){//月
            strTemp=" and V_DATE='"+startDate+"' and DATE_TYPE='2' ";
		}if("1".equals(dateType)||dateType=="1"){//周
            strTemp=" and V_DATE='"+startDate+"' and DATE_TYPE='1' ";
		}
		if("1".equals(actType)||actType=="1"){//传统新媒体
			strTable="meta_dm.d_v_channel_type_new";
			tempStr=" and a1.view_state='1'";
		}if("2".equals(actType)||actType=="2"){//其他
			strTable="meta_dm.d_v_channel_type_new";
			tempStr=" and a1.view_state='2'";
		}if("3".equals(actType)||actType=="3"){//人工自助
			strTable="meta_dm.d_v_channel_type_c";
		}
		String level="".equals(Convert.toString(queryData.get("level")))?"1":Convert.toString(queryData.get("level"));
		String sumService="";
		if("1".equals(level)||level=="1"){//一级	
			sumService="sum(cnt_num)";
		}if("2".equals(level)||level=="2"){//二级
			sumService="sum(case when channel_serv_id like '10%' OR channel_serv_id like '30%' then cnt_num end)";
		}if("3".equals(level)||level=="3"){//三级
			sumService="sum(case when channel_serv_id like '30%' then cnt_num end)";
		}
		StringBuffer sql = new StringBuffer();
		if(channelTypeCode.equals("1")){
			 if(showAllChannel.equals("false")){
				 sql.append("select CHANNEL_TYPE_PAR_CODE as CHANNEL_TYPE_CODE,CHANNEL_TYPE_PAR_NAME as CHANNEL_TYPE_NAME,"+sumService+" as SERVICE_NUM," +
							"sum(case when channel_serv_id like '60%' then cnt_num end)PAY_NUM," +
				      		"sum(case when channel_serv_id like '10%' then cnt_num end)QUERY_NUM,sum(case when channel_serv_id like '30%' then cnt_num end)DEAL_NUM," +
				      		"sum(case when channel_serv_id like '20%' then cnt_num end)CONSULT_NUM,sum(case when channel_serv_id like '40%' then cnt_num end)COMPLAIN_NUM," +
				      		"sum(case when channel_serv_id like '50%' then cnt_num end)FAULT_NUM,sum(case when channel_serv_id like '99%' then cnt_num end)OTHER_NUM," +
				      		"sum(case when channel_serv_id like '1001%' then cnt_num end)QUERY_NUM1,sum(case when channel_serv_id like '1002%' then cnt_num end)QUERY_NUM2," +
				      		"sum(case when channel_serv_id like '1003%' then cnt_num end)QUERY_NUM3,sum(case when channel_serv_id like '1004%' then cnt_num end)QUERY_NUM4," +
				      		"sum(case when channel_serv_id like '1005%' then cnt_num end)QUERY_NUM5,sum(case when channel_serv_id like '3001%' then cnt_num end)DEAL_NUM1," +
				      		"sum(case when channel_serv_id like '3002%' then cnt_num end)DEAL_NUM2,sum(case when channel_serv_id like '3003%' then cnt_num end)DEAL_NUM3," +
				      		"sum(case when channel_serv_id like '3004%' then cnt_num end)DEAL_NUM4, " +
				      		"sum(case when channel_serv_id like '30010001%' then cnt_num end)DEAL_NUM11,sum(case when channel_serv_id like '30010002%' then cnt_num end)DEAL_NUM12," +
				      		"sum(case when channel_serv_id like '30010003%' then cnt_num end)DEAL_NUM13,sum(case when channel_serv_id like '30010004%' then cnt_num end)DEAL_NUM14," +
				      		"sum(case when channel_serv_id like '30020001%' then cnt_num end)DEAL_NUM21,sum(case when channel_serv_id like '30020002%' then cnt_num end)DEAL_NUM22," +
				      		"sum(case when channel_serv_id like '30020003%' then cnt_num end)DEAL_NUM23,sum(case when channel_serv_id like '30020004%' then cnt_num end)DEAL_NUM24," +
				      		"sum(case when channel_serv_id like '30020005%' then cnt_num end)DEAL_NUM25,sum(case when channel_serv_id like '30040001%' then cnt_num end)DEAL_NUM41," +
				      		"sum(case when channel_serv_id like '30040001%' then cnt_num end)DEAL_NUM42,sum(case when channel_serv_id like '30040003%' then cnt_num end)DEAL_NUM43," +
				      		"sum(case when channel_serv_id like '30040004%' then cnt_num end)DEAL_NUM44,sum(case when channel_serv_id like '30040005%' then cnt_num end)DEAL_NUM45," +
				      		"sum(case when channel_serv_id like '30040006%' then cnt_num end)DEAL_NUM46,sum(case when channel_serv_id like '30040007%' then cnt_num end)DEAL_NUM47," +
				      		"sum(case when channel_serv_id like '30040008%' then cnt_num end)DEAL_NUM48,sum(case when channel_serv_id like '30040009%' then cnt_num end)DEAL_NUM49," +
				      		"sum(case when channel_serv_id like '30040010%' then cnt_num end)DEAL_NUM410,sum(case when channel_serv_id like '10010001%' then cnt_num end)QUERY_NUM11," +
				      		"sum(case when channel_serv_id like '10010002%' then cnt_num end)QUERY_NUM12,sum(case when channel_serv_id like '10010003%' then cnt_num end)QUERY_NUM13," +
				      		"sum(case when channel_serv_id like '10010004%' then cnt_num end)QUERY_NUM14,sum(case when channel_serv_id like '10010005%' then cnt_num end)QUERY_NUM15," +
				      		"sum(case when channel_serv_id like '10010006%' then cnt_num end)QUERY_NUM16,sum(case when channel_serv_id like '10020001%' then cnt_num end)QUERY_NUM21," +
				      		"sum(case when channel_serv_id like '10020002%' then cnt_num end)QUERY_NUM22,sum(case when channel_serv_id like '10020003%' then cnt_num end)QUERY_NUM23," +
				      		"sum(case when channel_serv_id like '10020004%' then cnt_num end)QUERY_NUM24,sum(case when channel_serv_id like '10020005%' then cnt_num end)QUERY_NUM25," +
				      		"sum(case when channel_serv_id like '10020006%' then cnt_num end)QUERY_NUM26,sum(case when channel_serv_id like '10030001%' then cnt_num end)QUERY_NUM31," +
				      		"sum(case when channel_serv_id like '10030002%' then cnt_num end)QUERY_NUM32,sum(case when channel_serv_id like '10030003%' then cnt_num end)QUERY_NUM33," +
				      		"sum(case when channel_serv_id like '10040001%' then cnt_num end)QUERY_NUM41,sum(case when channel_serv_id like '10040002%' then cnt_num end)QUERY_NUM42," +
				      		"sum(case when channel_serv_id like '10040003%' then cnt_num end)QUERY_NUM43,sum(case when channel_serv_id like '10040004%' then cnt_num end)QUERY_NUM44," +
				      		"sum(case when channel_serv_id like '10050001%' then cnt_num end)QUERY_NUM51,sum(case when channel_serv_id like '10050002%' then cnt_num end)QUERY_NUM52," +
				      		"sum(case when channel_serv_id like '10050003%' then cnt_num end)QUERY_NUM53,sum(case when channel_serv_id like '10050004%' then cnt_num end)QUERY_NUM54," +
				      		"sum(case when channel_serv_id like '10050005%' then cnt_num end)QUERY_NUM55,sum(case when channel_serv_id like '10050006%' then cnt_num end)QUERY_NUM56" +
				      		" from CS_CHANNEL_VIEW t1,META_DIM_ZONE t3," +
							"(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a1.view_state,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID " +
							"from "+strTable+" a1, "+strTable+" a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE "+tempStr+") t2 where " +
							" T1.CHANNEL_ID = T2.CHANNEL_TYPE_CODE and t1.REGION_ID = t3.ZONE_CODE  and t3.TREECODE LIKE GETTREECODE('"+zoneCode+"')||'%' "+strTemp);  //and t3.DIM_LEVEL < '4'
				 sql.append(" group by CHANNEL_TYPE_PAR_CODE, CHANNEL_TYPE_PAR_NAME");
				 sql.append(" ORDER BY CHANNEL_TYPE_PAR_CODE");//and t1.region_id!='0000'
			}else{
				sql.append("select a.CHANNEL_Id as CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME as CHANNEL_TYPE_NAME,"+sumService+" as SERVICE_NUM," +
						"sum(case when channel_serv_id like '60%' then cnt_num end)PAY_NUM," +
				      		"sum(case when channel_serv_id like '10%' then cnt_num end)QUERY_NUM,sum(case when channel_serv_id like '30%' then cnt_num end)DEAL_NUM," +
				      		"sum(case when channel_serv_id like '20%' then cnt_num end)CONSULT_NUM,sum(case when channel_serv_id like '40%' then cnt_num end)COMPLAIN_NUM," +
				      		"sum(case when channel_serv_id like '50%' then cnt_num end)FAULT_NUM,sum(case when channel_serv_id like '99%' then cnt_num end)OTHER_NUM," +
				      		"sum(case when channel_serv_id like '1001%' then cnt_num end)QUERY_NUM1,sum(case when channel_serv_id like '1002%' then cnt_num end)QUERY_NUM2," +
				      		"sum(case when channel_serv_id like '1003%' then cnt_num end)QUERY_NUM3,sum(case when channel_serv_id like '1004%' then cnt_num end)QUERY_NUM4," +
				      		"sum(case when channel_serv_id like '1005%' then cnt_num end)QUERY_NUM5,sum(case when channel_serv_id like '3001%' then cnt_num end)DEAL_NUM1," +
				      		"sum(case when channel_serv_id like '3002%' then cnt_num end)DEAL_NUM2,sum(case when channel_serv_id like '3003%' then cnt_num end)DEAL_NUM3," +
				      		"sum(case when channel_serv_id like '3004%' then cnt_num end)DEAL_NUM4," +
				      		"sum(case when channel_serv_id like '30010001%' then cnt_num end)DEAL_NUM11,sum(case when channel_serv_id like '30010002%' then cnt_num end)DEAL_NUM12," +
				      		"sum(case when channel_serv_id like '30010003%' then cnt_num end)DEAL_NUM13,sum(case when channel_serv_id like '30010004%' then cnt_num end)DEAL_NUM14," +
				      		"sum(case when channel_serv_id like '30020001%' then cnt_num end)DEAL_NUM21,sum(case when channel_serv_id like '30020002%' then cnt_num end)DEAL_NUM22," +
				      		"sum(case when channel_serv_id like '30020003%' then cnt_num end)DEAL_NUM23,sum(case when channel_serv_id like '30020004%' then cnt_num end)DEAL_NUM24," +
				      		"sum(case when channel_serv_id like '30020005%' then cnt_num end)DEAL_NUM25,sum(case when channel_serv_id like '30040001%' then cnt_num end)DEAL_NUM41," +
				      		"sum(case when channel_serv_id like '30040001%' then cnt_num end)DEAL_NUM42,sum(case when channel_serv_id like '30040003%' then cnt_num end)DEAL_NUM43," +
				      		"sum(case when channel_serv_id like '30040004%' then cnt_num end)DEAL_NUM44,sum(case when channel_serv_id like '30040005%' then cnt_num end)DEAL_NUM45," +
				      		"sum(case when channel_serv_id like '30040006%' then cnt_num end)DEAL_NUM46,sum(case when channel_serv_id like '30040007%' then cnt_num end)DEAL_NUM47," +
				      		"sum(case when channel_serv_id like '30040008%' then cnt_num end)DEAL_NUM48,sum(case when channel_serv_id like '30040009%' then cnt_num end)DEAL_NUM49," +
				      		"sum(case when channel_serv_id like '30040010%' then cnt_num end)DEAL_NUM410,sum(case when channel_serv_id like '10010001%' then cnt_num end)QUERY_NUM11," +
				      		"sum(case when channel_serv_id like '10010002%' then cnt_num end)QUERY_NUM12,sum(case when channel_serv_id like '10010003%' then cnt_num end)QUERY_NUM13," +
				      		"sum(case when channel_serv_id like '10010004%' then cnt_num end)QUERY_NUM14,sum(case when channel_serv_id like '10010005%' then cnt_num end)QUERY_NUM15," +
				      		"sum(case when channel_serv_id like '10010006%' then cnt_num end)QUERY_NUM16,sum(case when channel_serv_id like '10020001%' then cnt_num end)QUERY_NUM21," +
				      		"sum(case when channel_serv_id like '10020002%' then cnt_num end)QUERY_NUM22,sum(case when channel_serv_id like '10020003%' then cnt_num end)QUERY_NUM23," +
				      		"sum(case when channel_serv_id like '10020004%' then cnt_num end)QUERY_NUM24,sum(case when channel_serv_id like '10020005%' then cnt_num end)QUERY_NUM25," +
				      		"sum(case when channel_serv_id like '10020006%' then cnt_num end)QUERY_NUM26,sum(case when channel_serv_id like '10030001%' then cnt_num end)QUERY_NUM31," +
				      		"sum(case when channel_serv_id like '10030002%' then cnt_num end)QUERY_NUM32,sum(case when channel_serv_id like '10030003%' then cnt_num end)QUERY_NUM33," +
				      		"sum(case when channel_serv_id like '10040001%' then cnt_num end)QUERY_NUM41,sum(case when channel_serv_id like '10040002%' then cnt_num end)QUERY_NUM42," +
				      		"sum(case when channel_serv_id like '10040003%' then cnt_num end)QUERY_NUM43,sum(case when channel_serv_id like '10040004%' then cnt_num end)QUERY_NUM44," +
				      		"sum(case when channel_serv_id like '10050001%' then cnt_num end)QUERY_NUM51,sum(case when channel_serv_id like '10050002%' then cnt_num end)QUERY_NUM52," +
				      		"sum(case when channel_serv_id like '10050003%' then cnt_num end)QUERY_NUM53,sum(case when channel_serv_id like '10050004%' then cnt_num end)QUERY_NUM54," +
				      		"sum(case when channel_serv_id like '10050005%' then cnt_num end)QUERY_NUM55,sum(case when channel_serv_id like '10050006%' then cnt_num end)QUERY_NUM56"  +
				      		"  from CS_CHANNEL_VIEW a,META_DIM_ZONE t3,(select *from "+strTable+" start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE )" +
						"a1 where a.channel_id=a1.channel_type_code and a.REGION_ID = t3.ZONE_CODE and t3.TREECODE LIKE GETTREECODE('"+zoneCode+"')||'%' "+tempStr+strTemp);//and t3.DIM_LEVEL < '4'
				 sql.append(" group by a.CHANNEL_Id,CHANNEL_TYPE_NAME,a1.ORDER_ID,CHANNEL_TYPE_PAR_CODE");
				 sql.append(" ORDER BY CHANNEL_TYPE_PAR_CODE,a1.ORDER_ID,CHANNEL_Id");//and a.region_id!='0000' 
			}
		}
		else{
			sql.append("select a.CHANNEL_Id as CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME as CHANNEL_TYPE_NAME,"+sumService+" as SERVICE_NUM," +
					"sum(case when channel_serv_id like '60%' then cnt_num end)PAY_NUM," +
				      		"sum(case when channel_serv_id like '10%' then cnt_num end)QUERY_NUM,sum(case when channel_serv_id like '30%' then cnt_num end)DEAL_NUM," +
				      		"sum(case when channel_serv_id like '20%' then cnt_num end)CONSULT_NUM,sum(case when channel_serv_id like '40%' then cnt_num end)COMPLAIN_NUM," +
				      		"sum(case when channel_serv_id like '50%' then cnt_num end)FAULT_NUM,sum(case when channel_serv_id like '99%' then cnt_num end)OTHER_NUM," +
				      		"sum(case when channel_serv_id like '1001%' then cnt_num end)QUERY_NUM1,sum(case when channel_serv_id like '1002%' then cnt_num end)QUERY_NUM2," +
				      		"sum(case when channel_serv_id like '1003%' then cnt_num end)QUERY_NUM3,sum(case when channel_serv_id like '1004%' then cnt_num end)QUERY_NUM4," +
				      		"sum(case when channel_serv_id like '1005%' then cnt_num end)QUERY_NUM5,sum(case when channel_serv_id like '3001%' then cnt_num end)DEAL_NUM1," +
				      		"sum(case when channel_serv_id like '3002%' then cnt_num end)DEAL_NUM2,sum(case when channel_serv_id like '3003%' then cnt_num end)DEAL_NUM3," +
				      		"sum(case when channel_serv_id like '3004%' then cnt_num end)DEAL_NUM4," +
				      		"sum(case when channel_serv_id like '30010001%' then cnt_num end)DEAL_NUM11,sum(case when channel_serv_id like '30010002%' then cnt_num end)DEAL_NUM12," +
				      		"sum(case when channel_serv_id like '30010003%' then cnt_num end)DEAL_NUM13,sum(case when channel_serv_id like '30010004%' then cnt_num end)DEAL_NUM14," +
				      		"sum(case when channel_serv_id like '30020001%' then cnt_num end)DEAL_NUM21,sum(case when channel_serv_id like '30020002%' then cnt_num end)DEAL_NUM22," +
				      		"sum(case when channel_serv_id like '30020003%' then cnt_num end)DEAL_NUM23,sum(case when channel_serv_id like '30020004%' then cnt_num end)DEAL_NUM24," +
				      		"sum(case when channel_serv_id like '30020005%' then cnt_num end)DEAL_NUM25,sum(case when channel_serv_id like '30040001%' then cnt_num end)DEAL_NUM41," +
				      		"sum(case when channel_serv_id like '30040001%' then cnt_num end)DEAL_NUM42,sum(case when channel_serv_id like '30040003%' then cnt_num end)DEAL_NUM43," +
				      		"sum(case when channel_serv_id like '30040004%' then cnt_num end)DEAL_NUM44,sum(case when channel_serv_id like '30040005%' then cnt_num end)DEAL_NUM45," +
				      		"sum(case when channel_serv_id like '30040006%' then cnt_num end)DEAL_NUM46,sum(case when channel_serv_id like '30040007%' then cnt_num end)DEAL_NUM47," +
				      		"sum(case when channel_serv_id like '30040008%' then cnt_num end)DEAL_NUM48,sum(case when channel_serv_id like '30040009%' then cnt_num end)DEAL_NUM49," +
				      		"sum(case when channel_serv_id like '30040010%' then cnt_num end)DEAL_NUM410,sum(case when channel_serv_id like '10010001%' then cnt_num end)QUERY_NUM11," +
				      		"sum(case when channel_serv_id like '10010002%' then cnt_num end)QUERY_NUM12,sum(case when channel_serv_id like '10010003%' then cnt_num end)QUERY_NUM13," +
				      		"sum(case when channel_serv_id like '10010004%' then cnt_num end)QUERY_NUM14,sum(case when channel_serv_id like '10010005%' then cnt_num end)QUERY_NUM15," +
				      		"sum(case when channel_serv_id like '10010006%' then cnt_num end)QUERY_NUM16,sum(case when channel_serv_id like '10020001%' then cnt_num end)QUERY_NUM21," +
				      		"sum(case when channel_serv_id like '10020002%' then cnt_num end)QUERY_NUM22,sum(case when channel_serv_id like '10020003%' then cnt_num end)QUERY_NUM23," +
				      		"sum(case when channel_serv_id like '10020004%' then cnt_num end)QUERY_NUM24,sum(case when channel_serv_id like '10020005%' then cnt_num end)QUERY_NUM25," +
				      		"sum(case when channel_serv_id like '10020006%' then cnt_num end)QUERY_NUM26,sum(case when channel_serv_id like '10030001%' then cnt_num end)QUERY_NUM31," +
				      		"sum(case when channel_serv_id like '10030002%' then cnt_num end)QUERY_NUM32,sum(case when channel_serv_id like '10030003%' then cnt_num end)QUERY_NUM33," +
				      		"sum(case when channel_serv_id like '10040001%' then cnt_num end)QUERY_NUM41,sum(case when channel_serv_id like '10040002%' then cnt_num end)QUERY_NUM42," +
				      		"sum(case when channel_serv_id like '10040003%' then cnt_num end)QUERY_NUM43,sum(case when channel_serv_id like '10040004%' then cnt_num end)QUERY_NUM44," +
				      		"sum(case when channel_serv_id like '10050001%' then cnt_num end)QUERY_NUM51,sum(case when channel_serv_id like '10050002%' then cnt_num end)QUERY_NUM52," +
				      		"sum(case when channel_serv_id like '10050003%' then cnt_num end)QUERY_NUM53,sum(case when channel_serv_id like '10050004%' then cnt_num end)QUERY_NUM54," +
				      		"sum(case when channel_serv_id like '10050005%' then cnt_num end)QUERY_NUM55,sum(case when channel_serv_id like '10050006%' then cnt_num end)QUERY_NUM56"  +
				      		" from CS_CHANNEL_VIEW a,META_DIM_ZONE t3,(select *from "+strTable+" start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE )" +
						"a1 where a.channel_id=a1.channel_type_code and a.REGION_ID = t3.ZONE_CODE  and t3.TREECODE LIKE GETTREECODE('"+zoneCode+"')||'%' "+tempStr+strTemp);//and t3.DIM_LEVEL < '4'
			 sql.append(" group by a.CHANNEL_Id,CHANNEL_TYPE_NAME");//and a.region_id!='0000'
			 sql.append(" ORDER BY CHANNEL_Id");
		}
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//渠道服务关键指标_存储过程
	public Map<String, Object> getChannelKeyIndex_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CS_CHANNEL_SERV_AREA_KEY;
		Object[] params = {
				MapUtils.getString(queryData, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "endTime", null).replaceAll("-", ""),
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
	public Map<String, Object> getTableData(Map<String, Object> map, Pager page) {
		String storeName="";
		String channelId=Convert.toString(map.get("channelId"));
		if("10002".equals(channelId)||channelId=="10002"){//10000号人工
			storeName = ConstantStoreProc.RPT_CHANNEL_1WLAB_DETAIL;
		}else if("10001".equals(channelId)||"10003".equals(channelId)||"10009".equals(channelId)){//10000号自助、10001号自助、速播干线
			storeName = ConstantStoreProc.RPT_CHANNEL_1WAUTO_DETAIL;
		}else if("14001".equals(channelId)){//自营厅
			storeName = ConstantStoreProc.RPT_CHANNEL_BUSI_DETAIL;
		}else if("13004".equals(channelId)){//短厅
			storeName = ConstantStoreProc.RPT_CHANNEL_DT_DETAIL;
		}else if("13001".equals(channelId)){//网厅
			storeName = ConstantStoreProc.RPT_CHANNEL_WT_DETAIL;
		}else if("13002".equals(channelId)){//WAP厅
			storeName = ConstantStoreProc.RPT_CHANNEL_WAP_DETAIL;
		}else if("13003".equals(channelId)){//客户端
			storeName = ConstantStoreProc.RPT_CHANNEL_KHD_DETAIL;
		}else if("13005".equals(channelId)){//自助终端
			storeName = ConstantStoreProc.RPT_CHANNEL_ZZZD_DETAIL;
		}else if("11001".equals(channelId)||"11002".equals(channelId)||"11003".equals(channelId)){//qq、微信、易信人工
			storeName = ConstantStoreProc.RPT_CHANNEL_IMLAB_DETAIL;
		}else if("11004".equals(channelId)){//qq自助
			storeName = ConstantStoreProc.RPT_CHANNEL_QQAUTO_DETAIL;
		}else if("11005".equals(channelId)){//微信自助
			storeName = ConstantStoreProc.RPT_CHANNEL_WXAUTO_DETAIL;
		}else if("11006".equals(channelId)){//易信自助
			storeName = ConstantStoreProc.RPT_CHANNEL_YXAUTO_DETAIL;
		}else{
			storeName = ConstantStoreProc.RPT_CHANNEL_SERVICE_DETAIL;
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
	//渠道服务业务项清单_存储过程
	public Map<String, Object> getIndexTableData(Map<String, Object> map, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CHANNEL_INDEX_DETAIL;
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
}
