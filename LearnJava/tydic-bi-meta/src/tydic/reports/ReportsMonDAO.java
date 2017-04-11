package tydic.reports;

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

public class ReportsMonDAO extends MetaBaseDAO {
	//获取查询条件_月份
	public List<Map<String, Object>> getSelectMon() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct month_id as month_id from  CS_CMPL_COMPLAIN_MON  order by month_id desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	//抱怨率报表_最大月份
	public String getMaxMon() {
		String sql = "select max(a.month_id) month_id from CS_CMPL_COMPLAIN_MON a ";
		return getDataAccess().queryForString(sql);
	}
	//投诉清单有数据_最近日期
	public String getMaxDate() {
		String sql = "select max(a.day_id) day_id from cs_cmpl_complain_detail a ";
		return getDataAccess().queryForString(sql);
	}
   //本地全业务抱怨率月报表表格数据
	public List<Map<String, Object>> getCmplReport_Mon(Map<String, Object> queryData) {
	      String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneId ="".equals(Convert.toString(queryData.get("zoneId")))?"0":Convert.toString(queryData.get("zoneId"));
	      String prodTypeId ="".equals(Convert.toString(queryData.get("prodTypeId")))?"-1":Convert.toString(queryData.get("prodTypeId"));
	      String cmplBusiTypeId ="".equals(Convert.toString(queryData.get("cmplBusiTypeId")))?"1":Convert.toString(queryData.get("cmplBusiTypeId"));
	      StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id,sum(a.COMPLAIN_NUM)COMPLAIN_NUM,max(a.NUM1)NUM1,decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM)/max(a.NUM1)*1000,2)) cmpl,a.MONTH_ID MONTH_ID,"
	        	  +" DECODE(DECODE(max(LAST_M_NUM1),0,0,SUM(LAST_M_COMPLAIN_NUM)/max(LAST_M_NUM1)),0,0,"
	              +"DECODE(max(NUM1),0,0,ROUND(((SUM(COMPLAIN_NUM)/max(NUM1))/(SUM(LAST_M_COMPLAIN_NUM)/max(LAST_M_NUM1)"
	              +")-1)*100,2))) cmpl_hb,"//环比
	        	  +" DECODE(DECODE(max(LAST_Y_NUM1),0,0,SUM(LAST_Y_COMPLAIN_NUM)/max(LAST_Y_NUM1)),0,0,"
	              +"DECODE(max(NUM1),0,0,ROUND(((SUM(COMPLAIN_NUM)/max(NUM1))/(SUM(LAST_Y_COMPLAIN_NUM)/max(LAST_Y_NUM1))-1)*100,2)))cmpl_tb"//同比
	              +" from CS_CMPL_COMPLAIN_MON a, (select zone_id,ZONE_CODE,DIM_LEVEL,ORDER_ID from META_DIM_ZONE start with ZONE_CODE ='"+zoneId+"' connect by prior ZONE_CODE = zone_par_code)b," +
	              		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeId+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
	              				"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeId+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
	              						"where a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.REGION_ID = b.ZONE_CODE ");
	      if(dateTime!=null&&!("".equals(dateTime))){
    		  sql.append(" and a.month_id='"+dateTime+"'");
         }
          sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id,ORDER_ID) z ");
          sql.append(" order by z.PAR_REGION_ID, ORDER_ID,z.REGION_ID");
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	//取上月、上年数据/折线图
	public  Map<String,Object> getChartData(String dateTime, Map<String,Object> map) {
		  String zoneId ="".equals(Convert.toString(map.get("zoneId")))?"0":Convert.toString(map.get("zoneId"));
	      String prodTypeId ="".equals(Convert.toString(map.get("prodTypeId")))?"-1":Convert.toString(map.get("prodTypeId"));
	      String cmplBusiTypeId ="".equals(Convert.toString(map.get("cmplBusiTypeId")))?"1":Convert.toString(map.get("cmplBusiTypeId"));
	      StringBuffer sql = new StringBuffer("select z.* from (select b.ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id,sum(a.COMPLAIN_NUM)COMPLAIN_NUM,max(a.NUM1)NUM1,decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM)/max(a.NUM1)*1000,2)) cmpl,a.MONTH_ID MONTH_ID,"
	        	  +" DECODE(DECODE(max(LAST_M_NUM1),0,0,SUM(LAST_M_COMPLAIN_NUM)/max(LAST_M_NUM1)),0,0,"
	              +"DECODE(max(NUM1),0,0,ROUND(((SUM(COMPLAIN_NUM)/max(NUM1))/(SUM(LAST_M_COMPLAIN_NUM)/max(LAST_M_NUM1)"
	              +")-1)*100,2))) cmpl_hb,"//环比
	        	  +" DECODE(DECODE(max(LAST_Y_NUM1),0,0,SUM(LAST_Y_COMPLAIN_NUM)/max(LAST_Y_NUM1)),0,0,"
	              +"DECODE(max(NUM1),0,0,ROUND(((SUM(COMPLAIN_NUM)/max(NUM1))/(SUM(LAST_Y_COMPLAIN_NUM)/max(LAST_Y_NUM1))-1)*100,2)))cmpl_tb"//同比
	              +" from CS_CMPL_COMPLAIN_MON a,meta_dim_zone b," +
	              		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeId+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
	              				"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeId+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
	              						"where a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.region_id=b.zone_code and b.zone_code='"+zoneId+"'");
		if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.month_id='"+dateTime+"'");
	     }
        sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id,b.ORDER_ID)z");
		sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID");
	  return getDataAccess().queryForMap(sql.toString());
  }  
	public String getNewMonth() {
		String sql = "select to_date(max(t.month_id),'yyyy-MM') month_no from CS_CMPL_COMPLAIN_MON t ";
		return getDataAccess().queryForString(sql);
	}
	//柱状图
	public List<Map<String, Object>> getBarCmplReport_Mon(Map<String, Object> queryData){
		String zoneId ="".equals(Convert.toString(queryData.get("zoneId")))?"0":Convert.toString(queryData.get("zoneId"));
	    String prodTypeId ="".equals(Convert.toString(queryData.get("prodTypeId")))?"-1":Convert.toString(queryData.get("prodTypeId"));
	    String cmplBusiTypeId ="".equals(Convert.toString(queryData.get("cmplBusiTypeId")))?"1":Convert.toString(queryData.get("cmplBusiTypeId"));
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id,sum(a.COMPLAIN_NUM)COMPLAIN_NUM,max(a.NUM1)NUM1,decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM)/max(a.NUM1)*1000,2)) cmpl,a.MONTH_ID MONTH_ID,"
	        	  +" DECODE(DECODE(max(LAST_M_NUM1),0,0,SUM(LAST_M_COMPLAIN_NUM)/max(LAST_M_NUM1)),0,0,"
	              +"DECODE(max(NUM1),0,0,ROUND(((SUM(COMPLAIN_NUM)/max(NUM1))/(SUM(LAST_M_COMPLAIN_NUM)/max(LAST_M_NUM1)"
	              +")-1)*100,2))) cmpl_hb,"//环比
	        	  +" DECODE(DECODE(max(LAST_Y_NUM1),0,0,SUM(LAST_Y_COMPLAIN_NUM)/max(LAST_Y_NUM1)),0,0,"
	              +"DECODE(max(NUM1),0,0,ROUND(((SUM(COMPLAIN_NUM)/max(NUM1))/(SUM(LAST_Y_COMPLAIN_NUM)/max(LAST_Y_NUM1))-1)*100,2)))cmpl_tb"//同比
	              +" from CS_CMPL_COMPLAIN_MON a, (select level,zone_id,ZONE_CODE,ORDER_ID from META_DIM_ZONE where  level=2 start with ZONE_CODE ='"+zoneId+"' connect by prior ZONE_CODE = zone_par_code)b," +
	              		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeId+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
	              				"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeId+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
	              						"where a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.REGION_ID = b.ZONE_CODE ");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.month_id='"+dateTime+"'");
	     }
	   sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id,b.ORDER_ID) z ");
	   sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//柱状图21地市
	public List<Map<String, Object>> get21BarCmplReport_Mon(Map<String, Object> queryData){
		String zoneId ="0";
	    String prodTypeId ="".equals(Convert.toString(queryData.get("prodTypeId")))?"-1":Convert.toString(queryData.get("prodTypeId"));
	    String cmplBusiTypeId ="".equals(Convert.toString(queryData.get("cmplBusiTypeId")))?"1":Convert.toString(queryData.get("cmplBusiTypeId"));
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		StringBuffer sql = new StringBuffer("select z.* from (select b.ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id,sum(a.COMPLAIN_NUM)COMPLAIN_NUM,max(a.NUM1)NUM1,decode(max(a.num1),0,0,round(sum(a.COMPLAIN_NUM)/max(a.NUM1)*1000,2)) cmpl,a.MONTH_ID MONTH_ID,"
	        	  +" DECODE(DECODE(max(LAST_M_NUM1),0,0,SUM(LAST_M_COMPLAIN_NUM)/max(LAST_M_NUM1)),0,0,"
	              +"DECODE(max(NUM1),0,0,ROUND(((SUM(COMPLAIN_NUM)/max(NUM1))/(SUM(LAST_M_COMPLAIN_NUM)/max(LAST_M_NUM1)"
	              +")-1)*100,2))) cmpl_hb,"//环比
	        	  +" DECODE(DECODE(max(LAST_Y_NUM1),0,0,SUM(LAST_Y_COMPLAIN_NUM)/max(LAST_Y_NUM1)),0,0,"
	              +"DECODE(max(NUM1),0,0,ROUND(((SUM(COMPLAIN_NUM)/max(NUM1))/(SUM(LAST_Y_COMPLAIN_NUM)/max(LAST_Y_NUM1))-1)*100,2)))cmpl_tb"//同比
	              +" from CS_CMPL_COMPLAIN_MON a, (select level,ZONE_CODE,ORDER_ID from META_DIM_ZONE where  level=3 start with ZONE_CODE ='"+zoneId+"' connect by prior ZONE_CODE = zone_par_code)b," +
	              		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeId+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
	              				"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeId+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
	              						"where a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.REGION_ID = b.ZONE_CODE ");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.month_id='"+dateTime+"'");
	     }
	   sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id,b.ORDER_ID) z ");
	   sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	   //清单表格数据
		public List<Map<String, Object>> getListCmplReport_Mon(Map<String, Object> queryData,Map page) {
			  String indId = Convert.toString(queryData.get("indId"));
			  String startDate = Convert.toString(queryData.get("startDate")).replace("-", "");
		      String endDate = Convert.toString(queryData.get("endDate")).replace("-", "");
		      String zoneId ="".equals(Convert.toString(queryData.get("zoneId")))?"0":Convert.toString(queryData.get("zoneId"));
		      String prodTypeId ="".equals(Convert.toString(queryData.get("prodTypeId")))?"-1":Convert.toString(queryData.get("prodTypeId"));
		      String cmplBusiTypeId ="".equals(Convert.toString(queryData.get("cmplBusiTypeId")))?"1":Convert.toString(queryData.get("cmplBusiTypeId"));
		      StringBuffer sql = new StringBuffer("select t.* from(select ROW_NUMBER() OVER(ORDER BY ARCHIVE_DATE) ROW_NUM,NVL(b.zone_name,'-')zone_name," +
		      		"NVL(BILL_ID,'-')BILL_ID,NVL(HANDLE_OPTR_ID,'-')HANDLE_OPTR_ID,NVL(ARCHIVE_OPTR_ID,'-')ARCHIVE_OPTR_ID,NVL(CMPL_PROD_TYPE_NAME,'-')PROD_TYPE_NAME,NVL(CMPL_BUSINESS_TYPE_NAME,'-')CMPL_BUSINESS_TYPE_NAME," +
		      		"NVL(CMPL_CHANNEL_NAME,'-')CMPL_CHANNEL_NAME,NVL(CMPL_BILL_TYPE_NAME,'-')CMPL_BILL_TYPE_NAME,NVL(CUST_NBR,'-')CUST_NBR," +
		      		"NVL(CUST_NAME,'-')CUST_NAME,NVL(CUST_LINK_NBR,'-')CUST_LINK_NBR,NVL(CUST_LINK_NBR2,'-')CUST_LINK_NBR2," +
		      		"NVL(to_char(HANDLE_DATE, 'yyyy-mm-dd hh24:mi:ss'),'-')HANDLE_DATE,NVL(HANDLE_DEPT_ID,'-')HANDLE_DEPT_ID," +
		      		"NVL(HANDLE_DEPT_NAME,'-')HANDLE_DEPT_NAME,NVL(HANDLE_DEPT_NAME_DESC,'-')HANDLE_DEPT_NAME_DESC, " +
		      		"NVL(to_char(ARCHIVE_DATE, 'yyyy-mm-dd hh24:mi:ss'),'-')ARCHIVE_DATE,NVL(FILE_TYPE_NAME,'-')FILE_TYPE_NAME," +
		      		"NVL(BILL_TIME_COST,0)BILL_TIME_COST,NVL(BILL_TIMEOUT_COST,0)BILL_TIMEOUT_COST,NVL(TIMEOUT_DURATION,0)TIMEOUT_DURATION,NVL(REPEAT_NUM,0)REPEAT_NUM," +
		      		"NVL(DUTY_DEPT_ID,'-')DUTY_DEPT_ID,NVL(DUTY_DEPT_NAME,'-')DUTY_DEPT_NAME,NVL(DUTY_DEPT_NAME_DESC,'-')DUTY_DEPT_NAME_DESC,NVL(ARCHIVE_DESC,'-')ARCHIVE_DESC," +
		      		"NVL(SATISFY_NAME,'-')SATISFY_NAME,NVL(CALL_SEQ,'-')CALL_SEQ,NVL(CUST_LEVEL_NAME,'-')CUST_LEVEL_NAME,NVL(LOCAL_ID,'-')LOCAL_ID, CMPL_PROD_TYPE_ID,CMPL_BUSINESS_TYPE_ID,day_id from cs_cmpl_complain_detail a," +
		      		"(select zone_code,zone_name from META_DIM_ZONE start with zone_code ='"+zoneId+"' connect by prior zone_code = zone_par_code)b," +
              		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeId+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
              		"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeId+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
              		"where a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.local_ID = b.ZONE_CODE ");
		      sql.append(" and a.day_id>='"+startDate+"'");
		      sql.append(" and a.day_id<='"+endDate+"'");
		      if(indId!=null&&!"".equals(indId)){
		    	  if("4".equals(indId)){
		    		  sql.append(" and a.cmpl_bill_type_id='02'");
		    	  } 
		      }
	             sql.append(" order by a.day_id )t");
	             sql.append(" where t.ROW_NUM BETWEEN "+page.get("start")+" AND "+page.get("end") );
	           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
		       return list;
		}
		   //取清单报表数据列条数
		public Integer getCountListCmplReport_Mon(Map<String, Object> queryData) {
			  String indId = Convert.toString(queryData.get("indId"));
		      String startDate = Convert.toString(queryData.get("startDate")).replace("-", "");
		      String endDate = Convert.toString(queryData.get("endDate")).replace("-", "");
		      String zoneId ="".equals(Convert.toString(queryData.get("zoneId")))?"0":Convert.toString(queryData.get("zoneId"));
		      String prodTypeId ="".equals(Convert.toString(queryData.get("prodTypeId")))?"-1":Convert.toString(queryData.get("prodTypeId"));
		      String cmplBusiTypeId ="".equals(Convert.toString(queryData.get("cmplBusiTypeId")))?"1":Convert.toString(queryData.get("cmplBusiTypeId"));
		      StringBuffer sql = new StringBuffer("select count(a.day_id) from " +
			      		"cs_cmpl_complain_detail a," +
			      		"(select zone_code,zone_name from META_DIM_ZONE start with zone_code ='"+zoneId+"' connect by prior zone_code = zone_par_code)b," +
	              		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeId+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
	              		"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeId+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
	              		"where a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.LOCAL_ID = b.ZONE_CODE ");
		      sql.append(" and a.day_id>='"+startDate+"'");
		      sql.append(" and a.day_id<='"+endDate+"'"); 
		      if(indId!=null&&!"".equals(indId)){
		    	  if("4".equals(indId)){
		    		  sql.append(" and a.cmpl_bill_type_id='02'");
		    	  } 
		      }
	           Integer rows= getDataAccess().queryForInt(sql.toString());
		       return rows;
		}
		   //清单表格数据导出
		public List<Map<String, Object>> getListCmplReport_MonExp(Map<String, Object> queryData) {
			  String indId = Convert.toString(queryData.get("indId"));
			  String startDate = Convert.toString(queryData.get("startDate")).replace("-", "");
		      String endDate = Convert.toString(queryData.get("endDate")).replace("-", "");
		      String zoneId ="".equals(Convert.toString(queryData.get("zoneId")))?"0":Convert.toString(queryData.get("zoneId"));
		      String prodTypeId ="".equals(Convert.toString(queryData.get("prodTypeId")))?"-1":Convert.toString(queryData.get("prodTypeId"));
		      String cmplBusiTypeId ="".equals(Convert.toString(queryData.get("cmplBusiTypeId")))?"1":Convert.toString(queryData.get("cmplBusiTypeId"));
		      StringBuffer sql = new StringBuffer("select ROW_NUMBER() OVER(ORDER BY ARCHIVE_DATE) ROW_NUM,NVL(b.zone_name,'-')zone_name," +
		      		"NVL(BILL_ID,'-')BILL_ID,NVL(CMPL_PROD_TYPE_NAME,'-')PROD_TYPE_NAME,NVL(CMPL_BUSINESS_TYPE_NAME,'-')CMPL_BUSINESS_TYPE_NAME," +
		      		"NVL(CMPL_CHANNEL_NAME,'-')CMPL_CHANNEL_NAME,NVL(CMPL_BILL_TYPE_NAME,'-')CMPL_BILL_TYPE_NAME,NVL(CUST_NBR,'-')CUST_NBR," +
		      		"NVL(CUST_NAME,'-')CUST_NAME,NVL(CUST_LINK_NBR,'-')CUST_LINK_NBR,NVL(CUST_LINK_NBR2,'-')CUST_LINK_NBR2," +
		      		"NVL(to_char(HANDLE_DATE, 'yyyy-mm-dd hh24:mi:ss'),'-')HANDLE_DATE,NVL(HANDLE_DEPT_ID,'-')HANDLE_DEPT_ID," +
		      		"NVL(HANDLE_DEPT_NAME,'-')HANDLE_DEPT_NAME,NVL(HANDLE_DEPT_NAME_DESC,'-')HANDLE_DEPT_NAME_DESC, " +
		      		"NVL(to_char(ARCHIVE_DATE, 'yyyy-mm-dd hh24:mi:ss'),'-')ARCHIVE_DATE,NVL(FILE_TYPE_NAME,'-')FILE_TYPE_NAME," +
		      		"NVL(BILL_TIME_COST,0)BILL_TIME_COST,NVL(BILL_TIMEOUT_COST,0)BILL_TIMEOUT_COST,NVL(TIMEOUT_DURATION,0)TIMEOUT_DURATION,NVL(REPEAT_NUM,0)REPEAT_NUM," +
		      		"NVL(DUTY_DEPT_ID,'-')DUTY_DEPT_ID,NVL(DUTY_DEPT_NAME,'-')DUTY_DEPT_NAME,NVL(DUTY_DEPT_NAME_DESC,'-')DUTY_DEPT_NAME_DESC,NVL(ARCHIVE_DESC,'-')ARCHIVE_DESC," +
		      		"NVL(SATISFY_NAME,'-')SATISFY_NAME,NVL(CALL_SEQ,'-')CALL_SEQ,NVL(CUST_LEVEL_NAME,'-')CUST_LEVEL_NAME,NVL(LOCAL_ID,'-')LOCAL_ID, CMPL_PROD_TYPE_ID,CMPL_BUSINESS_TYPE_ID,day_id from cs_cmpl_complain_detail a," +
		      		"(select zone_code,zone_name from META_DIM_ZONE start with zone_code ='"+zoneId+"' connect by prior zone_code = zone_par_code)b," +
              		"(select CMPL_PROD_TYPE_CODE from tbas_dm.d_v_cmpl_prod_condition start with CMPL_PROD_TYPE_CODE ='"+prodTypeId+"' connect by prior CMPL_PROD_TYPE_CODE = CMPL_PROD_TYPE_PAR_CODE)c," +
              		"(select CMPL_BUSI_TYPE_CODE from tbas_dm.d_v_cmpl_business_condition start with CMPL_BUSI_TYPE_CODE ='"+cmplBusiTypeId+"' connect by prior CMPL_BUSI_TYPE_CODE = CMPL_BUSI_TYPE_PAR_CODE)d " +
              		"where a.CMPL_BUSINESS_TYPE_ID = d.CMPL_BUSI_TYPE_CODE  and a.CMPL_PROD_TYPE_ID = c.CMPL_PROD_TYPE_CODE and a.local_ID = b.ZONE_CODE ");
		      sql.append(" and a.day_id>='"+startDate+"'");
		      sql.append(" and a.day_id<='"+endDate+"'");
		      if(indId!=null&&!"".equals(indId)){
		    	  if("4".equals(indId)){
		    		  sql.append(" and a.cmpl_bill_type_id='02'");
		    	  } 
		      }
	             sql.append(" order by a.day_id ");
	           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
		       return list;
		}
		

		/**
		 * 方法描述：分页显示记录
		 * 
		 * @param:
		 * @return:
		 * @version: 1.0
		 * @author: yanhaidong
		 * @version: 2013-5-21 下午04:17:15
		 */
		public Map<String, Object> getTableData(Map<String, Object> map, Pager page) {
			Map<String, Object> mapList =new  HashMap<String, Object>();
			String storeName="";
			String ripId = MapUtils.getString(map, "ripId",  null);
			if("1".equals(ripId)){
				 storeName = ConstantStoreProc.RPT_CMPL_DETAIL_NEW;	
			}
			else{
				 storeName = ConstantStoreProc.RPT_CMPL_DETAIL;
			}
			
			Object[] params = {
					MapUtils.getString(map, "startDate", null).replaceAll("-", ""),
					MapUtils.getString(map, "endDate",   null).replaceAll("-", ""),
					MapUtils.getString(map, "zoneCode",  null),
					MapUtils.getString(map, "prodTypeCode",  null),
					MapUtils.getString(map, "cmplBusiTypeCode",  null),
					MapUtils.getString(map, "indId",  null),
					//MapUtils.getString(map, "channelType",  null),
					//MapUtils.getString(map, "reasonId",  null),
				    page.getStartRow(),
					page.getEndRow(),
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
		
		
		
		public Map<String, Object> getGroupChannelList_Pg(Map<String, Object> map, Pager page) {
			Map<String, Object> mapList =new  HashMap<String, Object>();
			String storeName=ConstantStoreProc.RPT_CMPL_DETAIL_N;
			Object[] params = {
					MapUtils.getString(map, "startDate", null).replaceAll("-", ""),
					MapUtils.getString(map, "endDate",   null).replaceAll("-", ""),
					MapUtils.getString(map, "zoneCode",  null),
					MapUtils.getString(map, "prodTypeCode",  null),
					MapUtils.getString(map, "cmplBusiTypeCode",  null),
					MapUtils.getString(map, "indId",  null),
					//MapUtils.getString(map, "channelType",  null),
					//MapUtils.getString(map, "reasonId",  null),
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
		
		/**
		 * 
		  * 方法描述：获得总记录数
		  * @param: 
		  * @return: 
		  * @version: 1.0
		  * @author: yanhaidong
		  * @version: 2013-5-23 下午01:56:48
		 */
		public Integer getDataCount(Map<String, Object> paramMap) {
			Object startTime =  paramMap.get("startDate");
	        Object endTime =    paramMap.get("endDate");
	        Object zoneCode =  paramMap.get("zoneCode");
	        String startDateTime=Convert.toString(startTime).replaceAll("-", "");
	        String endDateTime=Convert.toString(endTime).replaceAll("-", "");
	        Object prodTypeCode =  paramMap.get("prodTypeCode");
	        Object cmplBusiTypeCode =  paramMap.get("cmplBusiTypeCode");
	        Object channelType =  paramMap.get("channelType");
	        Object reasonId =  paramMap.get("reasonId");
	        String indId = Convert.toString(paramMap.get("indId"));
	        String ripId = MapUtils.getString(paramMap, "ripId",  null);
		    StringBuffer sql=new StringBuffer();
		                  sql.append("SELECT COUNT(DAY_ID) FROM TBAS_DM.ft_cmpl_d_t_"+startDateTime.substring(0, 6)+" f WHERE 1=1");
	                  List<Object> params=new ArrayList<Object>();
	                      sql.append(" AND f.DAY_ID >='"+startDateTime+"'"); 
	                      sql.append(" AND f.DAY_ID <='"+endDateTime+"'");  
	                  if(zoneCode != null && !"".equals(zoneCode))
	                  {
	                	  sql.append(" AND f.CUSTGRP_TREECODE like (select treecode from meta_dim_zone_custgrp z where zone_code =?)||'%' ");
	                	  params.add(Convert.toString(zoneCode));
	                  }if(prodTypeCode != null && !"".equals(prodTypeCode))
	                  {
	                	  sql.append(" and exists (select 1 from tbas_dm.d_v_cmpl_prod_condition a where " +
	                	  		"a.cmpl_prod_type_code = f.CMPL_PROD_TYPE_ID connect by prior" +
	                	  		" a.cmpl_prod_type_code = a.cmpl_prod_type_par_code  start with a.cmpl_prod_type_code ='"+prodTypeCode+"') ");
	                  }if(cmplBusiTypeCode != null && !"".equals(cmplBusiTypeCode))
	                  {
	                	  sql.append(" and exists (select 1 from tbas_dm.d_v_cmpl_business_condition a where" +
	                	  		" a.cmpl_busi_type_code = f.CMPL_BUSINESS_TYPE_ID connect by prior " +
	                	  		"a.cmpl_busi_type_code = a.cmpl_busi_type_par_code start with a.cmpl_busi_type_code ='"+cmplBusiTypeCode+"') ");
	                  }
			if (indId != null && !"".equals(indId)) {
				if ("4".equals(indId)) {
					sql.append(" and PROCESS_KEY ='complaint' and PREPROCESS_SUCCESS_FLAG='0' and f.cmpl_bill_type_id='02'");
				}if ("3".equals(indId)) {
					sql.append(" and PROCESS_KEY ='complaint' and PREPROCESS_SUCCESS_FLAG='0' and f.cmpl_bill_type_id in('01','02')");
				}if ("2".equals(indId)) {
					sql.append(" and PROCESS_KEY ='skipGradeComplaint' and PREPROCESS_SUCCESS_FLAG='0' and f.CMPL_TYPE_ID='01'");
				}
			}
		if("0000".equals(Convert.toString(zoneCode)) && !"1".equals(ripId))
	    {	
				sql.append(" and is_province_check=1  ");
	     }
		else if(!"1".equals(ripId))
		{
	       sql.append(" and is_city_check=1 ");
		}
		sql.append(" and local_id is not null ");
		sql.append(" and CMPL_BUSINESS_TYPE_ID not in ('910','909912','914906',");
		sql.append("'911','911901','911902','911903','911904','911905',");
		sql.append("'930','930901','930902','930903','930904')");
			
			return getDataAccess().queryForInt(sql.toString(), params.toArray());
		}
}
