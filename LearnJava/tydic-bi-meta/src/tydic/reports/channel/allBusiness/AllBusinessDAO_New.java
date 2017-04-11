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
import tydic.portalCommon.DateUtil;

public class AllBusinessDAO_New extends MetaBaseDAO {
	//获取查询条件_类型 不包含 “其他类型”
	public List<Map<String, Object>> getSelectType() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select CHANNEL_TYPE_CODE as CHANNEL_TYPE_ID,CHANNEL_TYPE_NAME,ORDER_ID from meta_dm.d_v_channel_type_new t where DIM_LEVEL='2' and t.view_state=1 order by ORDER_ID");
		return getDataAccess().queryForList(buffer.toString());
	}

	/***
	 * 根据类型编码查询
	 * @param channel_type_code
	 * @return
	 */
	public List<Map<String, Object>> getSelectType_Code(String channel_type_code) {
		StringBuffer buffer = new StringBuffer();
		if(channel_type_code.equals("1")){
			buffer.append("select CHANNEL_TYPE_CODE as CHANNEL_TYPE_ID,CHANNEL_TYPE_NAME,ORDER_ID from meta_dm.d_v_channel_type_new t where DIM_LEVEL='2'  order by ORDER_ID");
		}else {
			buffer.append("select CHANNEL_TYPE_CODE as CHANNEL_TYPE_ID,CHANNEL_TYPE_NAME,ORDER_ID from meta_dm.d_v_channel_type_new t where (t.channel_type_code='"+channel_type_code+"' or channel_type_par_code='"+channel_type_code+"' ) and  t.dim_level = 2 order by ORDER_ID");
		}
		return getDataAccess().queryForList(buffer.toString());
	}
	
	
	 /***
	  * 查询其他类型下的 类型
	  * @return
	  */
	public List<Map<String, Object>> getSelectOtherType() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select CHANNEL_TYPE_CODE as CHANNEL_TYPE_ID,CHANNEL_TYPE_NAME,ORDER_ID from meta_dm.d_v_channel_type_new t where DIM_LEVEL='2' and t.view_state=2 order by ORDER_ID");
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
		  Integer year = Convert.toInt(Convert.toString(queryData.get("dateTime")).substring(0, 4));
		  Integer month = Convert.toInt(Convert.toString(queryData.get("dateTime")).substring(4, 6));
		  String dateTime=Convert.toString(queryData.get("dateTime"));
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
	      StringBuffer sql2=new StringBuffer("select sum(service_num) TOTAL_NUM from META_USER.CS_CHANNEL_GLOBAL_MONTH_New A");
          sql2.append(" where a.month_id='"+dateTime+"'");
          sql2.append(" and a.region_id='"+zoneCode+"'");
          sql2.append(" and a.channel_type_id in (select CHANNEL_TYPE_CODE from (select CHANNEL_TYPE_CODE,CHANNEL_TYPE_PAR_CODE from meta_dm.d_v_channel_type_new where view_state='"+Convert.toString(queryData.get("changType"))+"'  start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior  CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE))");
	      String totalNum=getDataAccess().queryForString(sql2.toString(), null);
	      
          StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,NVL(c.CHANNEL_TYPE_PAR_NAME, '-') CHANNEL_TYPE_PAR_NAME, NVL(c.CHANNEL_TYPE_PAR_CODE, '99') CHANNEL_TYPE_PAR_CODE, c.ORDER_ID2,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
  	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME, a.PAR_REGION_ID, a.REGION_NAME,  a.region_id,  a.MONTH_ID,  c.CHANNEL_TYPE_PAR_NAME, c.CHANNEL_TYPE_PAR_CODE, c.ORDER_ID2, c.CHANNEL_TYPE_NAME, c.CHANNEL_TYPE_code) = '31' then  '合计'  WHEN grouping_id(a.PAR_REGION_NAME,  a.PAR_REGION_ID, a.REGION_NAME, a.region_id,  a.MONTH_ID, c.CHANNEL_TYPE_PAR_NAME,  c.CHANNEL_TYPE_PAR_CODE,  c.ORDER_ID2,  c.CHANNEL_TYPE_NAME,   c.CHANNEL_TYPE_code) = '7' then"+
                 " '小计'  ELSE  c.CHANNEL_TYPE_NAME  END CHANNEL_TYPE_NAME,  nvl(c.CHANNEL_TYPE_code, '99999') CHANNEL_TYPE_ID," +
  	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
  	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
  	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,trim(to_char(decode("+totalNum+",0,0,round(sum(SERVICE_NUM) / "+totalNum+" ,2)* 100),'FM99990.00')) PERC_NUM,decode(sum(distinct TOTAL_NUM2),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_ID MONTH_ID "
  	              +" from CS_CHANNEL_GLOBAL_MONTH_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+1)b," +
  	              		"(select d1.*, d2.channel_type_par_name  from (select CHANNEL_TYPE_CODE, CHANNEL_TYPE_NAME, CHANNEL_TYPE_PAR_CODE,  ORDER_ID as ORDER_ID2 from meta_dm.d_v_channel_type_new where view_state='"+Convert.toString(queryData.get("changType"))+"' start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior  CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE) d1,   (select channel_type_name as channel_type_par_name,  channel_type_code as channel_type_par_code   from meta_dm.d_v_channel_type_new) d2 where d1.channel_type_par_code = d2.channel_type_par_code )c " +
  	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
  	      if(dateTime!=null&&!("".equals(dateTime))){
  	    	sql.append(" and a.month_id='"+dateTime+"'");
           }
            sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_ID,c.CHANNEL_TYPE_PAR_NAME, c.CHANNEL_TYPE_PAR_CODE, c.ORDER_ID2,c.CHANNEL_TYPE_NAME, c.CHANNEL_TYPE_code)having grouping_id(a.PAR_REGION_NAME, a.PAR_REGION_ID, a.REGION_NAME, a.region_id, a.MONTH_ID,c.CHANNEL_TYPE_PAR_NAME, c.CHANNEL_TYPE_PAR_CODE, c.ORDER_ID2, c.CHANNEL_TYPE_NAME, c.CHANNEL_TYPE_code) in ('0','7','31') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL order by z.PAR_REGION_ID, z.ORDER_ID, z.REGION_ID, z.CHANNEL_TYPE_PAR_CODE, z.ORDER_ID2,  z.CHANNEL_TYPE_ID");
          
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
	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
	      sql.append(" and a.month_id='"+dateTime+"'");
	      sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID ) in ('0','3') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL");
          sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID,z.CHANNEL_TYPE_ID");
          sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_new a1, meta_dm.d_v_channel_type_new a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE GROUP BY T1.ORDER_ID,T1.PAR_REGION_ID,T1.PAR_REGION_NAME,T1.REGION_NAME,T1.REGION_ID order by T1.PAR_REGION_ID");
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
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
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
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
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
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 sql.append(" and a.month_id='"+dateTime+"'");
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析月报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData(String dateTime, Map<String,Object> map) {
	      String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(map.get("channelTypeCode")))?"1":Convert.toString(map.get("channelTypeCode"));
	      String sqlTemp="";
	      String changType=Convert.toString(map.get("changType"));
	      if(!"".equals(changType)&&changType!=""){
	    	  sqlTemp=" and e.VIEW_STATE = '"+Convert.toString(map.get("changType"))+"'";
	      }
        StringBuffer sql = new StringBuffer("select * from(select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.month_no,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2) = '3' AND  count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME2 END CHANNEL_TYPE_NAME,nvl(c.CHANNEL_TYPE_code2, '99') CHANNEL_TYPE_ID," +
	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM),'0','0.00',trim(to_char(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM) * 100,'FM99990.00'))) PERC_NUM,decode(sum(distinct TOTAL_NUM2),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2) * 100,'FM99990.00'))) PERC_NUM2,a.month_no month_no "
	              +" from CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+1)b," +
	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2 from meta_dm.d_v_channel_type_new e where e.STATE = '1' "+sqlTemp+" start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
    		  sql.append(" and a.month_no='"+dateTime+"'");
          sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.month_no,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.month_no,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type_new f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
          sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
          sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_new a1, meta_dm.d_v_channel_type_new a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE ORDER BY T2.ORDER_ID");
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
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.month_id='"+dateTime+"'");
	     }
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id) z ");
	   sql.append(" order by z.PAR_REGION_ID, ORDER_ID,z.REGION_ID");
       sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME from meta_dm.d_v_channel_type_new a1, meta_dm.d_v_channel_type_new a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE");
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
	            +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
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
		  String startDate = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String endDate = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
	      StringBuffer sql2=new StringBuffer("select sum(service_num) TOTAL_NUM from META_USER.CS_CHANNEL_GLOBAL_DAY_New A");
          sql2.append(" where a.day_id>='"+startDate+"'");
          sql2.append(" and a.day_id<='"+endDate+"'");
          sql2.append(" and a.region_id='"+zoneCode+"'");
          sql2.append(" and a.channel_type_id in (select CHANNEL_TYPE_CODE from(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_PAR_CODE from meta_dm.d_v_channel_type_new   where view_state='"+Convert.toString(queryData.get("changType"))+"'   start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior  CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE) )");
	      String totalNum=getDataAccess().queryForString(sql2.toString(), null);
	      
	      
	      StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,NVL(c.CHANNEL_TYPE_PAR_NAME, '-') CHANNEL_TYPE_PAR_NAME, NVL(c.CHANNEL_TYPE_PAR_CODE, '99') CHANNEL_TYPE_PAR_CODE,c.ORDER_ID2,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	    		  "CASE WHEN grouping_id(a.PAR_REGION_NAME, a.PAR_REGION_ID, a.REGION_NAME, a.region_id,  c.CHANNEL_TYPE_PAR_NAME, c.CHANNEL_TYPE_PAR_CODE, c.ORDER_ID2,  c.CHANNEL_TYPE_NAME,  a.CHANNEL_TYPE_ID) = '31' then  '合计'"+
                   "WHEN grouping_id(a.PAR_REGION_NAME, a.PAR_REGION_ID,  a.REGION_NAME,  a.region_id,  c.CHANNEL_TYPE_PAR_NAME,   c.CHANNEL_TYPE_PAR_CODE, c.ORDER_ID2,  c.CHANNEL_TYPE_NAME,  a.CHANNEL_TYPE_ID) = '7' then '小计'  ELSE  c.CHANNEL_TYPE_NAME END CHANNEL_TYPE_NAME,nvl(a.CHANNEL_TYPE_ID, '999999') CHANNEL_TYPE_ID," +
	    		  "NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
		      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
		      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,trim(to_char(decode("+totalNum+",0,0,round(sum(SERVICE_NUM) / "+totalNum+" ,2)* 100),'FM99990.00')) PERC_NUM,decode(sum(distinct TOTAL_NUM2),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2)*100,2)) PERC_NUM2"
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"') + 1)b," +
	              		"(select d1.*, d2.channel_type_par_name from (select CHANNEL_TYPE_CODE, CHANNEL_TYPE_NAME, CHANNEL_TYPE_PAR_CODE,  ORDER_ID as ORDER_ID2 from meta_dm.d_v_channel_type_new  where view_state='"+Convert.toString(queryData.get("changType"))+"'  start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"'   connect by prior CHANNEL_TYPE_CODE =     CHANNEL_TYPE_PAR_CODE) d1,  (select channel_type_name as channel_type_par_name,    channel_type_code as channel_type_par_code     from meta_dm.d_v_channel_type_new) d2  where d1.channel_type_par_code =   d2.channel_type_par_code )c " +
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
	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
	      sql.append(" and a.day_id>='"+startDate+"'");
	      sql.append(" and a.day_id<='"+endDate+"'");
	      sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID ) in ('0','3') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL");
          sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID,z.CHANNEL_TYPE_ID");
          sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_new a1, meta_dm.d_v_channel_type_new a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE GROUP BY T1.ORDER_ID,T1.PAR_REGION_ID,T1.PAR_REGION_NAME,T1.REGION_NAME,T1.REGION_ID order by T1.PAR_REGION_ID");
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
	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
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
	              "(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
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
	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+selType+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
	      sql.append(" and a.day_id>='"+startDate+"'");
	      sql.append(" and a.day_id<='"+endDate+"'");
	      sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID ) in ('0','3') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL");
         sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID,z.CHANNEL_TYPE_ID");
         sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_new a1, meta_dm.d_v_channel_type_new a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE ORDER BY T2.ORDER_ID");
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
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
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
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
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
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
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
	            +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
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
	      String dateTime = Convert.toString(queryData.get("dateTime"));
	      String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(queryData.get("channelTypeCode")))?"1":Convert.toString(queryData.get("channelTypeCode"));
	      StringBuffer sql2=new StringBuffer("select sum(service_num) TOTAL_NUM from META_USER.CS_CHANNEL_GLOBAL_DAY_New A");
          sql2.append(" where a.WEEK_NAME='"+dateTime+"'");
          sql2.append(" and a.region_id='"+zoneCode+"'");
          sql2.append(" and a.channel_type_id in (select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new where view_state='"+Convert.toString(queryData.get("changType"))+"' start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior  CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)");
	      String totalNum=getDataAccess().queryForString(sql2.toString(), null);
	      
	      StringBuffer sql = new StringBuffer("select  z.* from (select ORDER_ID,NVL(c.CHANNEL_TYPE_PAR_NAME, '-') CHANNEL_TYPE_PAR_NAME,NVL(c.CHANNEL_TYPE_PAR_CODE, '99') CHANNEL_TYPE_PAR_CODE,c.ORDER_ID2,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME, a.PAR_REGION_ID, a.REGION_NAME, a.region_id, a.MONTH_NO,c.CHANNEL_TYPE_PAR_NAME,c.CHANNEL_TYPE_PAR_CODE,c.ORDER_ID2, c.CHANNEL_TYPE_NAME, c.CHANNEL_TYPE_code) = '31' then '合计' WHEN grouping_id(a.PAR_REGION_NAME, a.PAR_REGION_ID, a.REGION_NAME, a.region_id, a.MONTH_NO,c.CHANNEL_TYPE_PAR_NAME,c.CHANNEL_TYPE_PAR_CODE,c.ORDER_ID2, c.CHANNEL_TYPE_NAME, c.CHANNEL_TYPE_code) = '7' then '小计'   ELSE c.CHANNEL_TYPE_NAME END CHANNEL_TYPE_NAME,  nvl(c.CHANNEL_TYPE_code, '99999') CHANNEL_TYPE_ID," +
	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,trim(to_char(decode("+totalNum+",0,0,round(sum(SERVICE_NUM) / "+totalNum+" ,2)* 100),'FM99990.00')) PERC_NUM,decode(sum(distinct TOTAL_NUM2_4_WEEK),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2_4_WEEK) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_NO MONTH_NO "
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL <getdimlevel('"+zoneCode+"')+1)b," +
	              		"(select d1.*, d2.channel_type_par_name from (select CHANNEL_TYPE_CODE, CHANNEL_TYPE_NAME,  CHANNEL_TYPE_PAR_CODE,   ORDER_ID as ORDER_ID2    from meta_dm.d_v_channel_type_new where view_state='"+Convert.toString(queryData.get("changType"))+"' start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE =  CHANNEL_TYPE_PAR_CODE) d1,   (select channel_type_name as channel_type_par_name,   channel_type_code as channel_type_par_code    from meta_dm.d_v_channel_type_new) d2   where d1.channel_type_par_code =   d2.channel_type_par_code)c " +
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
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
                 "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		 sql.append(" and a.week_name='"+dateTime+"'");
	   sql.append(" group by ORDER_ID,a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//全渠道总体分析周报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData_Day(String week,String month, Map<String,Object> map) {
	      String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
	      String channelTypeCode ="".equals(Convert.toString(map.get("channelTypeCode")))?"1":Convert.toString(map.get("channelTypeCode"));
	      String sqlTemp="";
	      String changType=Convert.toString(map.get("changType"));
	      if(!"".equals(changType)&&changType!=""){
	    	  sqlTemp=" and e.VIEW_STATE = '"+Convert.toString(map.get("changType"))+"'";
	      }
	      StringBuffer sql = new StringBuffer("select * from (select  z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2) = '3' AND  count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME2 END CHANNEL_TYPE_NAME,nvl(c.CHANNEL_TYPE_code2, '99') CHANNEL_TYPE_ID," +
	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM_4_WEEK),'0','0.00',trim(to_char(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM_4_WEEK) * 100,'FM99990.00'))) PERC_NUM,decode(sum(distinct TOTAL_NUM2_4_WEEK),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2_4_WEEK) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_NO MONTH_NO "
	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+1)b," +
	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2 from meta_dm.d_v_channel_type_new e where e.STATE = '1' "+sqlTemp+"start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
  		  sql.append(" and a.WEEK_NO='"+week+"'");
  		sql.append(" and a.MONTH_NO='"+month+"' and a.WEEK_NAME IS NOT NULL ");
        sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type_new f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
        sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
        sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_new a1, meta_dm.d_v_channel_type_new a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE ORDER BY T2.ORDER_ID");
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
	             +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
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
		              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME,CHANNEL_TYPE_PAR_CODE from meta_dm.d_v_channel_type_new where view_state='"+Convert.toString(queryData.get("changType"))+"'  start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
		              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE ");
		      sql.append(" and a.day_id>='"+dateTime+"'");
		      sql.append(" and a.day_id<='"+dateTime+"'");
		      sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID ) in ('0','3') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL");
	          sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID,z.CHANNEL_TYPE_ID");
	          sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_new a1, meta_dm.d_v_channel_type_new a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE group by CHANNEL_TYPE_PAR_CODE,CHANNEL_TYPE_PAR_NAME ORDER BY 1");
		}
		else{
			sql.append("select * from (select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
		    		  "CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME, a.CHANNEL_TYPE_ID) = '3' AND count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME END CHANNEL_TYPE_NAME,nvl(a.CHANNEL_TYPE_ID,'99') CHANNEL_TYPE_ID," +
		    		  "NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
			      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
			      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM),0,0,round(sum(SERVICE_NUM)/sum(distinct TOTAL_NUM)*100,2)) PERC_NUM,decode(sum(distinct TOTAL_NUM2),0,0,round(sum(SERVICE_NUM2)/sum(distinct TOTAL_NUM2)*100,2)) PERC_NUM2"
		              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_New a, (select ORDER_ID, zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"') || '%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"') + 1)b," +
		              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_NAME,CHANNEL_TYPE_PAR_CODE from meta_dm.d_v_channel_type_new  where view_state='"+Convert.toString(queryData.get("changType"))+"' start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
		              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE ");
		      sql.append(" and a.day_id>='"+dateTime+"'");
		      sql.append(" and a.day_id<='"+dateTime+"'");
		      sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,c.CHANNEL_TYPE_NAME,a.CHANNEL_TYPE_ID ) in ('0','3') ) z where z.CHANNEL_TYPE_NAME IS NOT NULL");
	          sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID,z.CHANNEL_TYPE_ID");
	          sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_new a1, meta_dm.d_v_channel_type_new a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE ");
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
	  	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2 from meta_dm.d_v_channel_type_new e where e.STATE = '1' and e.VIEW_STATE = '"+Convert.toString(queryData.get("changType"))+"' start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
	  	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
	  	      if(dateTime!=null&&!("".equals(dateTime))){
	      		  sql.append(" and a.WEEK_NAME='"+dateTime+"'");
	           }
	            sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type_new f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
	            sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
	            sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_new a1, meta_dm.d_v_channel_type_new a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE GROUP BY CHANNEL_TYPE_PAR_CODE,CHANNEL_TYPE_PAR_NAME ORDER BY 1");

		}
		else{
			  sql.append("select * from (select  z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
		  	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2) = '3' AND  count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME2 END CHANNEL_TYPE_NAME,nvl(c.CHANNEL_TYPE_code2, '99') CHANNEL_TYPE_ID," +
		  	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
		  	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
		  	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM_4_WEEK),'0','0.00',trim(to_char(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM_4_WEEK) * 100,'FM99990.00'))) PERC_NUM,decode(sum(distinct TOTAL_NUM2_4_WEEK),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2_4_WEEK) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_NO MONTH_NO "
		  	              +" from META_USER.CS_CHANNEL_GLOBAL_DAY_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+1)b," +
		  	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2 from meta_dm.d_v_channel_type_new e where e.STATE = '1' and e.VIEW_STATE = '"+Convert.toString(queryData.get("changType"))+"' start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
		  	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE");
		  	      if(dateTime!=null&&!("".equals(dateTime))){
		      		  sql.append(" and a.WEEK_NAME='"+dateTime+"'");
		           }
		            sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_NO,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type_new f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
		            sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
		            sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_new a1, meta_dm.d_v_channel_type_new a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE");
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
		  	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_ID,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2) = '3' AND  count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME2 END CHANNEL_TYPE_NAME,nvl(c.CHANNEL_TYPE_code2, '99') CHANNEL_TYPE_ID," +
		  	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
		  	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
		  	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM),'0','0.00',trim(to_char(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM) * 100,'FM99990.00'))) PERC_NUM,decode(sum(distinct TOTAL_NUM2),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_ID MONTH_ID "
		  	              +" from CS_CHANNEL_GLOBAL_MONTH_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+1)b," +
		  	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2,CHANNEL_TYPE_PAR_CODE from meta_dm.d_v_channel_type_new e where e.STATE = '1' and  e.view_state='"+Convert.toString(queryData.get("changType"))+"' start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
		  	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE ");
		  	      if(dateTime!=null&&!("".equals(dateTime))){
		      		  sql.append(" and a.month_id='"+dateTime+"'");
		           }
		            sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_ID,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_ID,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type_new f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
		            sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
		            sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_new a1, meta_dm.d_v_channel_type_new a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE GROUP BY CHANNEL_TYPE_PAR_CODE,CHANNEL_TYPE_PAR_NAME ORDER BY 1");

		}
		else{
			 sql.append("select * from(select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
		  	      		"CASE WHEN grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_ID,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2) = '3' AND  count(distinct CHANNEL_TYPE_ID) <> 1 THEN '合计' ELSE c.CHANNEL_TYPE_NAME2 END CHANNEL_TYPE_NAME,nvl(c.CHANNEL_TYPE_code2, '99') CHANNEL_TYPE_ID," +
		  	      		"NVL(to_char(sum(SERVICE_NUM)),'0') SERVICE_NUM,NVL(to_char(sum(SERVICE_NUM2)),'0') SERVICE_NUM2,NVL(to_char(sum(PAY_NUM)),'0') PAY_NUM,NVL(to_char(sum(QUERY_NUM1)),'0') QUERY_NUM1,NVL(to_char(sum(QUERY_NUM2)),'0') QUERY_NUM2,NVL(to_char(sum(QUERY_NUM3)),'0') QUERY_NUM3,NVL(to_char(sum(QUERY_NUM4)),'0') QUERY_NUM4,NVL(to_char(sum(QUERY_NUM5)),'0') QUERY_NUM5,NVL(to_char(sum(CONSULT_NUM)),'0') CONSULT_NUM,NVL(to_char(sum(DEAL_NUM1)),'0') DEAL_NUM1,NVL(to_char(sum(DEAL_NUM2)),'0') DEAL_NUM2,NVL(to_char(sum(DEAL_NUM3)),'0') DEAL_NUM3,NVL(to_char(sum(DEAL_NUM4)),'0') DEAL_NUM4,NVL(to_char(sum(FEEDBACK_NUM)),'0') FEEDBACK_NUM," +
		  	      		"NVL(to_char(sum(FEEDBACK2_NUM)),'0') FEEDBACK2_NUM,NVL(to_char(sum(CHANGE_NUM)),'0') CHANGE_NUM,NVL(to_char(sum(COMPLAIN_NUM)),'0') COMPLAIN_NUM,NVL(to_char(sum(FAULT_NUM)),'0') FAULT_NUM," +
		  	      		"NVL(to_char(sum(OTHER_NUM)),'0') OTHER_NUM,decode(sum(distinct TOTAL_NUM),'0','0.00',trim(to_char(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM) * 100,'FM99990.00'))) PERC_NUM,decode(sum(distinct TOTAL_NUM2),'0','0.00',trim(to_char(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2) * 100,'FM99990.00'))) PERC_NUM2,a.MONTH_ID MONTH_ID "
		  	              +" from CS_CHANNEL_GLOBAL_MONTH_NEW a, (select ORDER_ID,zone_code from META_DIM_ZONE d where D.TREECODE LIKE gettreecode('"+zoneCode+"')||'%' AND D.DIM_LEVEL < getdimlevel('"+zoneCode+"')+1)b," +
		  	              		"(select CHANNEL_TYPE_CODE,CHANNEL_TYPE_CODE2,CHANNEL_TYPE_NAME,CHANNEL_TYPE_NAME2,CHANNEL_TYPE_PAR_CODE from meta_dm.d_v_channel_type_new e where e.STATE = '1' and   e.view_state='"+Convert.toString(queryData.get("changType"))+"' start with CHANNEL_TYPE_CODE = '"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
		  	              "where a.REGION_ID = b.ZONE_CODE and a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE ");
 
		  	      if(dateTime!=null&&!("".equals(dateTime))){
		      		  sql.append(" and a.month_id='"+dateTime+"'");
		           }
		            sql.append(" group by rollup(ORDER_ID,a.PAR_REGION_NAME, a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_ID,c.CHANNEL_TYPE_NAME2, c.CHANNEL_TYPE_code2)having grouping_id(a.PAR_REGION_NAME,a.PAR_REGION_ID,a.REGION_NAME,a.region_id,a.MONTH_ID,c.CHANNEL_TYPE_NAME2,c.CHANNEL_TYPE_code2 ) in ('0','3') ) z,meta_dm.d_v_channel_type_new f where z.CHANNEL_TYPE_NAME IS NOT NULL and z.CHANNEL_TYPE_ID=f.CHANNEL_TYPE_code(+)");
		            sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID,f.ORDER_ID");
		            sql.append(" ) T1,(select a1.CHANNEL_TYPE_CODE CHANNEL_TYPE_CODE,a2.CHANNEL_TYPE_CODE CHANNEL_TYPE_PAR_CODE ,a2.CHANNEL_TYPE_NAME CHANNEL_TYPE_PAR_NAME,a1.ORDER_ID ORDER_ID from meta_dm.d_v_channel_type_new a1, meta_dm.d_v_channel_type_new a2 where a1.CHANNEL_TYPE_PAR_CODE = a2.CHANNEL_TYPE_CODE) T2 where T1.CHANNEL_TYPE_ID = T2.CHANNEL_TYPE_CODE");		
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
	            +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c " +
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
	//获取渠道信息
	public Map<String,Object> getChannel(String channelCode){
		StringBuffer sql = new StringBuffer(" select * from meta_dm.d_v_channel_type_new where CHANNEL_TYPE_CODE='"+channelCode+"'");
		return getDataAccess().queryForMap(sql.toString());
	}
	/////////////////////////////////////////
	////////////////////////////////////////
	//////////////////////////////////////
	
	///////////////////////////////
	//全渠道服务区域获取最大日期
	public String getNewDay(String tabStr) {
		String sql = "select max(t.day_id)day_id from "+tabStr+" t ";
		return getDataAccess().queryForString(sql);
	}
	//按照查询周查询月和周
	public Map<String, Object> queryMonthAndWeek(String WeekMon) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select MONTH_NO,WEEK_NO from (select distinct MONTH_NO,WEEK_NO from CS_CHANNEL_GLOBAL_DAY_New where WEEK_NAME=? order by WEEK_NO desc)");
		return getDataAccess().queryForMap(buffer.toString(), WeekMon);
	}
	//获取查询条件_月份
	public List<Map<String, Object>> getSelectMon() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct month_id as month_id from CS_CHANNEL_GLOBAL_DAY_New  where month_id is not null  order by month_id desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	public String getNewMonth() {
		String sql = "select to_date(max(t.month_id),'yyyy-MM') month_no from CS_CHANNEL_GLOBAL_DAY_New t ";
		return getDataAccess().queryForString(sql);
	}
	//全渠道总体情况分析周报_最大周
	public String getMaxWeek() {
		String sql = "select rownum,WEEK_NO ,WEEK_NAME from (select distinct WEEK_NO ,WEEK_NAME from CS_CHANNEL_GLOBAL_DAY_New " +
		          "where week_name is not null  order by WEEK_NO desc) where rownum=1";
		return getDataAccess().queryForString(sql);
	}
	//全渠道总体情况分析周报_周列表
	public List<Map<String, Object>> getSelectWeek_Day() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select WEEK_NO, WEEK_NAME,MONTH_NO from (select distinct WEEK_NO as WEEK_NO,WEEK_NAME as WEEK_NAME,MONTH_NO from CS_CHANNEL_GLOBAL_DAY_New where WEEK_NAME is not null)" +
				" order by MONTH_NO desc,WEEK_NO desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	//全渠道服务区域_日、月、周存储过程
	public Map<String, Object> getAllChannelSerZone_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName=ConstantStoreProc.RPT_CS_CHANNEL_SERVICE_AREA;
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
	//渠道服务区域_按周取数
	public  Map<String,Object> getChartDataWeek_ChannelZone(String weekNo,String monthNo, Map<String,Object> map) {
		String channelTypeCode = Convert.toString(map.get("channelTypeCode"));
	    String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
        StringBuffer sql = new StringBuffer("select sum(SERVICE_NUM)SERVICE_NUM,sum(SERVICE_NUM2)SERVICE_NUM2,sum(PAY_NUM)PAY_NUM,sum(QUERY_NUM1)QUERY_NUM1," +
      		"sum(QUERY_NUM2)QUERY_NUM2,sum(QUERY_NUM3)QUERY_NUM3,sum(QUERY_NUM4)QUERY_NUM4,sum(QUERY_NUM5)QUERY_NUM5,sum(CONSULT_NUM)CONSULT_NUM," +
      		"sum(DEAL_NUM1)DEAL_NUM1,sum(DEAL_NUM2)DEAL_NUM2,sum(DEAL_NUM3)DEAL_NUM3,sum(DEAL_NUM4)DEAL_NUM4,sum(FEEDBACK_NUM)FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM)FEEDBACK2_NUM,sum(CHANGE_NUM)CHANGE_NUM,sum(COMPLAIN_NUM)COMPLAIN_NUM,sum(FAULT_NUM)FAULT_NUM," +
	      		"sum(OTHER_NUM)OTHER_NUM,decode(sum(distinct TOTAL_NUM),'0','0.00',round(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM) * 100,2)) PERC_NUM," +
	      		"decode(sum(distinct TOTAL_NUM2),'0','0.00',round(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2) * 100,2)) PERC_NUM2 "
	              +" from CS_CHANNEL_GLOBAL_DAY_NEW a," 
	              +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c ");
  		  sql.append(" where a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE and region_id='"+zoneCode+"' and month_no='"+monthNo+"' and week_no='"+weekNo+"'");
	  return getDataAccess().queryForMap(sql.toString());
	}
	//渠道区域_按时间取数（日、月）
	public  Map<String,Object> getChartData_ChannelZone(String dateTime, Map<String,Object> map) {
		String channelTypeCode = Convert.toString(map.get("channelTypeCode"));
		String dateType = Convert.toString(map.get("dateType"));
		String strTemp="";
		if("0".equals(dateType)||dateType=="0"){//天
			strTemp="and day_id='"+dateTime+"'";
		}if("2".equals(dateType)||dateType=="2"){//月
            strTemp="and month_id='"+dateTime+"'";
		}
	    String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
        StringBuffer sql = new StringBuffer("select sum(SERVICE_NUM)SERVICE_NUM,sum(SERVICE_NUM2)SERVICE_NUM2,sum(PAY_NUM)PAY_NUM,sum(QUERY_NUM1)QUERY_NUM1," +
      		"sum(QUERY_NUM2)QUERY_NUM2,sum(QUERY_NUM3)QUERY_NUM3,sum(QUERY_NUM4)QUERY_NUM4,sum(QUERY_NUM5)QUERY_NUM5,sum(CONSULT_NUM)CONSULT_NUM," +
      		"sum(DEAL_NUM1)DEAL_NUM1,sum(DEAL_NUM2)DEAL_NUM2,sum(DEAL_NUM3)DEAL_NUM3,sum(DEAL_NUM4)DEAL_NUM4,sum(FEEDBACK_NUM)FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM)FEEDBACK2_NUM,sum(CHANGE_NUM)CHANGE_NUM,sum(COMPLAIN_NUM)COMPLAIN_NUM,sum(FAULT_NUM)FAULT_NUM," +
	      		"sum(OTHER_NUM)OTHER_NUM,decode(sum(distinct TOTAL_NUM),'0','0.00',round(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM) * 100,2)) PERC_NUM," +
	      		"decode(sum(distinct TOTAL_NUM2),'0','0.00',round(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2) * 100,2)) PERC_NUM2 "
	              +" from CS_CHANNEL_GLOBAL_DAY_NEW a,"
	              +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c ");
  		  sql.append("where a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE and region_id='"+zoneCode+"' "+strTemp);
  		return getDataAccess().queryForMap(sql.toString());
	}
	public  Map<String,Object> getChartData_ChannelSerZone(String startTime,String endTime,Map<String,Object> map) {
		String channelTypeCode = Convert.toString(map.get("channelTypeCode"));
		String dateType = Convert.toString(map.get("dateType"));
		String strTemp="";
		if("0".equals(dateType)||dateType=="0"){//天
			strTemp="and day_id>='"+startTime+"' and day_id<='"+endTime+"' ";
		}if("2".equals(dateType)||dateType=="2"){//月
            strTemp="and month_id>='"+startTime+"' and month_id<='"+endTime+"'";
		}if("1".equals(dateType)||dateType=="1"){//周
            strTemp="and week_name>='"+startTime+"' and week_name<='"+endTime+"'";
		}
	    String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
        StringBuffer sql = new StringBuffer("select sum(SERVICE_NUM)SERVICE_NUM,sum(SERVICE_NUM2)SERVICE_NUM2,sum(PAY_NUM)PAY_NUM,sum(QUERY_NUM1)QUERY_NUM1," +
      		"sum(QUERY_NUM2)QUERY_NUM2,sum(QUERY_NUM3)QUERY_NUM3,sum(QUERY_NUM4)QUERY_NUM4,sum(QUERY_NUM5)QUERY_NUM5,sum(CONSULT_NUM)CONSULT_NUM," +
      		"sum(DEAL_NUM1)DEAL_NUM1,sum(DEAL_NUM2)DEAL_NUM2,sum(DEAL_NUM3)DEAL_NUM3,sum(DEAL_NUM4)DEAL_NUM4,sum(FEEDBACK_NUM)FEEDBACK_NUM," +
	      		"sum(FEEDBACK2_NUM)FEEDBACK2_NUM,sum(CHANGE_NUM)CHANGE_NUM,sum(COMPLAIN_NUM)COMPLAIN_NUM,sum(FAULT_NUM)FAULT_NUM," +
	      		"sum(OTHER_NUM)OTHER_NUM,decode(sum(distinct TOTAL_NUM),'0','0.00',round(sum(SERVICE_NUM) / sum(distinct TOTAL_NUM) * 100,2)) PERC_NUM," +
	      		"decode(sum(distinct TOTAL_NUM2),'0','0.00',round(sum(SERVICE_NUM2) / sum(distinct TOTAL_NUM2) * 100,2)) PERC_NUM2 "
	              +" from CS_CHANNEL_GLOBAL_DAY_NEW a,"
	              +"(select CHANNEL_TYPE_CODE from meta_dm.d_v_channel_type_new start with CHANNEL_TYPE_CODE ='"+channelTypeCode+"' connect by prior CHANNEL_TYPE_CODE = CHANNEL_TYPE_PAR_CODE)c ");
  		  sql.append("where a.CHANNEL_TYPE_ID=c.CHANNEL_TYPE_CODE and region_id='"+zoneCode+"' "+strTemp);
  		return getDataAccess().queryForMap(sql.toString());
	}
}
