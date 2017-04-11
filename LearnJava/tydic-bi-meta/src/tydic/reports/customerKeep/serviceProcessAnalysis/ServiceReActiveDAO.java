package tydic.reports.customerKeep.serviceProcessAnalysis;

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

public class ServiceReActiveDAO extends MetaBaseDAO {
	//获取查询条件_月份
	public List<Map<String, Object>> getSelectMon() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct month_id as month_id from CS_SERVICE_RE_ACTIVE_MONTH  order by month_id desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	public String getNewMonth() {
		String sql = "select to_date(max(t.month_id),'yyyy-MM') month_no from CS_SERVICE_RE_ACTIVE_MONTH t ";
		return getDataAccess().queryForString(sql);
	}
	//获取查询条件_月份(共用)
	public List<Map<String, Object>> getSelectMon(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct month_id as month_id from "+tabStr+" order by month_id desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	//政企
	public List<Map<String, Object>> getSelectMonGov(String tabStr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct month_id as month_id from tbas_dm."+tabStr+" order by month_id desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	//政企index_id
	public List<Map<String, Object>> getSelectMonIndex(String tabStr,String indexId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct month_id as month_id from "+tabStr+" where index_id='"+indexId+"' order by month_id desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	//故障下单
	public List<Map<String, Object>> getListMonIndex(String tabStr,String indexId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct month_id as month_id from "+tabStr+" where SERV_GROUP='"+indexId+"' order by month_id desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	public List<Map<String, Object>> getSelectInd(String tabStr){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct ind_id,ind_name from "+tabStr+"  order by ind_id desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	public List<Map<String, Object>> getSelectProdType(String tabStr){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct prod_type_id,prod_type_name from "+tabStr+"  order by prod_type_id desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	public List<Map<String, Object>> getSelectProdTypePhoto(String tabStr){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct prod_type_id,prod_type_name from "+tabStr+" where prod_type_id<>'40' order by prod_type_id desc");
		return getDataAccess().queryForList(buffer.toString());
	}
	public List<Map<String, Object>> getSelectIndType(String tabStr){
		StringBuffer buffer = new StringBuffer();
		buffer.append("select distinct IND_ID,IND_NAME from "+tabStr+"  order by IND_ID");
		return getDataAccess().queryForList(buffer.toString());
	}
	public String getNewMonth(String tabStr) {
		String sql = "select to_date(max(month_id),'yyyy-MM') month_no from "+tabStr;
		return getDataAccess().queryForString(sql);
	}
	//政企
	public String getNewMonthGov(String tabStr) {
		String sql = "select to_date(max(month_id),'yyyy-MM') month_no from tbas_dm."+tabStr;
		return getDataAccess().queryForString(sql);
	}
	//政企index_id
	public String getNewMonthIndex(String tabStr,String indexId) {
		String sql = "select to_date(max(month_id),'yyyy-MM') month_no from "+tabStr+" where index_id='"+indexId+"'";
		return getDataAccess().queryForString(sql);
	}
	//故障下单
	public String getMaxMonthIndex(String tabStr,String indexId) {
		String sql = "select to_date(max(month_id),'yyyy-MM') month_no from "+tabStr+" where SERV_GROUP='"+indexId+"'";
		return getDataAccess().queryForString(sql);
	}
	public String getNewDay(String tabStr) {
		String sql = "select max(t.day_id)day_id from "+tabStr+" t ";
		return getDataAccess().queryForString(sql);
	}
	public String getParameters(String rptId) {
		String sql = "select PARAMETERS from t_rpt_config_4j where rpt_id="+rptId;
		return getDataAccess().queryForString(sql);
	}
   //移动和宽带用户停机复开及时率月报表表格数据
	public List<Map<String, Object>> getServiceReActive_Mon(Map<String, Object> queryData) {
	      String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneId ="".equals(Convert.toString(queryData.get("zoneId")))?"0000":Convert.toString(queryData.get("zoneId"));
	      StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id,sum(YD_SUM_NUM) YD_SUM_NUM," +
	      		"sum(YD_3MIN_NUM) YD_3MIN_NUM,trim(to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_3MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00')||'%') YD_3MIN_NUM_LV," +
	      		"sum(YD_30MIN_NUM) YD_30MIN_NUM,trim(to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_30MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00')||'%') YD_30MIN_NUM_LV," +
	      		"sum(KD_SUM_NUM) KD_SUM_NUM,sum(KD_10MIN_NUM) KD_10MIN_NUM,trim(to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_10MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00')||'%') KD_10MIN_NUM_LV ," +
	      		"sum(KD_30MIN_NUM)KD_30MIN_NUM ,trim(to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_30MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00')||'%')KD_30MIN_NUM_LV ,a.MONTH_ID MONTH_ID "
	              +" from CS_SERVICE_RE_ACTIVE_MONTH a, (select ORDER_ID,zone_code from META_DIM_ZONE start with zone_code ='"+zoneId+"' connect by prior zone_code = zone_par_code)b " +
	              "where a.REGION_ID = b.ZONE_CODE ");
	      if(dateTime!=null&&!("".equals(dateTime))){
    		  sql.append(" and a.month_id='"+dateTime+"'");
         }
          sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id,ORDER_ID) z ");
          sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID");
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	//提醒投诉分析报表——产品类型     饼图列表
	public  List<Map<String, Object>> getProductTypePieList(Map<String, Object> excuteInitData){
		 String zoneCode =       MapUtils.getString(excuteInitData, "zoneCode",null);
		 String dateTime = Convert.toString(excuteInitData.get("dateTime")).replaceAll("-", "");

         String sql="select o.产品类型,o.合计" +
        		" from(select  case when a.prod_type_id='914901' then '停机前提醒' when a.prod_type_id='914902' then '业务到期提醒' when a.prod_type_id='914903' then '套餐阈值提醒'" +
        		" when a.prod_type_id='914904' then '数据国际漫游流量提醒' when a.prod_type_id='000' then '合计'end 产品类型," +
        		" a.prod_type_id 产品类型_ID,to_char(DECODE(wtx,null,0,wtx)) 未收到提醒,to_char(DECODE(txbjs,null,0,txbjs)) 提醒不及时 ,to_char(DECODE(txcw,null,0,txcw)) 提醒错误,to_char(DECODE(cftx,null,0,cftx)) 重复提醒 ,to_char(DECODE(qt,null,0,qt)) 其他,to_char(DECODE(hj,null,0,hj)) 合计 ," +
        		" case when to_char(round((hj/ aa.s_cnt)*100,2),'FM99990.00') is null and  a.prod_type_id <> '000'  then '0.00%' when a.prod_type_id = '000' and hj<>0  then '100.00%'else to_char(DECODE((hj/ aa.s_cnt),null,0.00,round(hj/ aa.s_cnt*100,2)),'FM99990.00')||'%' end 占比" +
        		" from (select nvl(a.prod_type_id,'000') prod_type_id ,sum(a.wtx) wtx,sum(a.txbjs) txbjs,sum(a.txcw) txcw,sum(a.cftx) cftx,sum(a.qt) qt ,sum(a.hj) hj" +
        		" from(select t.prod_type_id,a.wtx,a.txbjs,a.txcw,a.cftx,a.qt,a.hj from (select cmpl_prod_type_code prod_type_id,t.cmpl_prod_type_name from  META_DM.D_CMPL_PROD_TYPE t where  t.cmpl_prod_type_code in ('914903', '914904', '914901', '914902')   )t" +
        		" left join (select prod_type_id , case when a.cmpl_business_type2_id ='928901'then nvl(a.num1,0) else 0  end wtx,case when a.cmpl_business_type2_id ='928902'then nvl(a.num1,0) else 0 end txbjs,case when a.cmpl_business_type2_id ='928903'then nvl(a.num1,0)else 0 end txcw" +
        		" ,case when a.cmpl_business_type2_id ='928904'then nvl(a.num1,0) else 0 end cftx,case when a.cmpl_business_type2_id ='928905'then nvl(a.num1,0) else 0 end qt ,a.num1 hj  from  CS_CMPL_PROBLEM_MON a where a.cmpl_business_type1_id='928'" +
        		" and a.month_id='"+dateTime+"' and a.region_id='"+zoneCode+"' and a.prod_type_id in ('914903', '914904', '914901', '914902') ) a on t.prod_type_id=a.prod_type_id ) a group by rollup(a.prod_type_id) ) a" +
        		" left join   (  select cmpl_prod_type_code  prod_type_id,s_cnt from (select cmpl_prod_type_code,t.cmpl_prod_type_name from  META_DM.D_CMPL_PROD_TYPE t where  t.cmpl_prod_type_code in ('914903', '914904', '914901', '914902')   )t" +
        		" left join ( select  sum(a.num1) s_cnt from  CS_CMPL_PROBLEM_MON a where a.cmpl_business_type1_id='928'" +
        		" and a.month_id='"+dateTime+"' and a.region_id='"+zoneCode+"' and a.prod_type_id in ('914903', '914904', '914901', '914902')  )  a on 1=1)  aa on  a.prod_type_id=aa.prod_type_id" +
        		" ) o" +
        		" where o.产品类型 <> '合计' and o.产品类型 <>'占比'" ;
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//提醒投诉分析报表——投诉表象     饼图列表
		public  List<Map<String, Object>> getComplainTypePieList(Map<String, Object> excuteInitData){
			 String zoneCode =       MapUtils.getString(excuteInitData, "zoneCode",null);
			 String dateTime = Convert.toString(excuteInitData.get("dateTime")).replaceAll("-", "");

	         String sql="select o.投诉表象,o.合计" +
	        		" from(select  case when a.cmpl_business_type2_id='928901' then '未收到提醒' when a.cmpl_business_type2_id='928902' then '提醒不及时' when a.cmpl_business_type2_id='928903' then '提醒错误'" +
	        		" when a.cmpl_business_type2_id='928904' then '重复提醒' when a.cmpl_business_type2_id='928905' then '其他' when a.cmpl_business_type2_id='000' then '合计'end 投诉表象," +
	        		" a.cmpl_business_type2_id 投诉表象_ID,to_char(DECODE(ting,null,0,ting)) 停机前提醒,to_char(DECODE(ye,null,0,ye)) 业务到期提醒 ,to_char(DECODE(tao,null,0,tao)) 套餐阈值提醒,to_char(DECODE(shu,null,0,shu))数据国际漫游流量提醒  ,to_char(DECODE(hj,null,0,hj)) 合计 ," +
	        		" case when to_char(round((hj/ aa.s_cnt)*100,2),'FM99990.00') is null and  a.cmpl_business_type2_id <> '000'  then '0.00%'  when a.cmpl_business_type2_id = '000' and hj<>0  then '100.00%' else to_char(DECODE((hj/ aa.s_cnt),null,0.00,round(hj/ aa.s_cnt*100,2)),'FM99990.00')||'%' end 占比" +
	        		" from (select nvl(a.cmpl_business_type2_id,'000') cmpl_business_type2_id ,sum(a.ting) ting,sum(a.ye) ye,sum(a.tao) tao,sum(a.shu) shu,sum(a.hj) hj" +
	        		" from(select distinct t.cmpl_business_type2_id,a.ting,a.ye,a.tao,a.shu,a.hj from  (select t.cmpl_business_type2_id,t.CMPL_BUSINESS_TYPE2_NAME from  CS_CMPL_PROBLEM_MON t  where t.cmpl_business_type1_id='928'  and t.month_id='201309' and t.region_id='0000' and t.prod_type_id in ('914903', '914904', '914901', '914902')   )t left join" +
	        		" (select cmpl_business_type2_id , case when a.prod_type_id ='914901'then nvl(a.num1,0) else 0  end ting,case when a.prod_type_id ='914902'then nvl(a.num1,0) else 0 end ye ,case when a.prod_type_id ='914903'then nvl(a.num1,0) else 0 end tao ,case when a.prod_type_id ='914904'then nvl(a.num1,0) else 0 end shu,a.num1 hj  from  CS_CMPL_PROBLEM_MON a" +
	        		" where a.cmpl_business_type1_id='928' and a.month_id='"+dateTime+"' and a.region_id='"+zoneCode+"' and a.prod_type_id in ('914903', '914904', '914901', '914902')" +
	        		" ) a on t.cmpl_business_type2_id=a.cmpl_business_type2_id    ) a group by rollup(a.cmpl_business_type2_id) ) a" +
	        		" left join   (  select distinct cmpl_business_type2_id  ,s_cnt from  (select t.cmpl_business_type2_id,t.CMPL_BUSINESS_TYPE2_NAME from  CS_CMPL_PROBLEM_MON t  where t.cmpl_business_type1_id='928'  and t.month_id='201309' and t.region_id='0000' and t.prod_type_id in ('914903', '914904', '914901', '914902')   )t" +
	        		" left join ( select  sum(a.num1) s_cnt from  CS_CMPL_PROBLEM_MON a" +
	        		" where a.cmpl_business_type1_id='928'  and a.month_id='"+dateTime+"' and a.region_id='"+zoneCode+"' and a.prod_type_id in ('914903', '914904', '914901', '914902')  )  a on 1=1)  aa" +
	        		" on  a.cmpl_business_type2_id=aa.cmpl_business_type2_id) o" +
	        		" where  o.投诉表象<>'合计'" +
	        		" order by o.投诉表象_ID " ;
	          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
		      return list;
		}
	//移动和宽带用户停机复开及时率月报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData(String dateTime, Map<String,Object> map) {
		  String zoneId ="".equals(Convert.toString(map.get("zoneId")))?"0000":Convert.toString(map.get("zoneId"));
	     		  StringBuffer sql = new StringBuffer("select z.* from (select b.ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id,sum(YD_SUM_NUM) YD_SUM_NUM," +
	      		"sum(YD_3MIN_NUM) YD_3MIN_NUM,to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_3MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00') YD_3MIN_NUM_LV," +
	      		"sum(YD_30MIN_NUM) YD_30MIN_NUM,to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_30MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00') YD_30MIN_NUM_LV," +
	      		"sum(KD_SUM_NUM) KD_SUM_NUM,sum(KD_10MIN_NUM) KD_10MIN_NUM,to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_10MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00') KD_10MIN_NUM_LV ," +
	      		"sum(KD_30MIN_NUM)KD_30MIN_NUM ,to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_30MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00')KD_30MIN_NUM_LV ,a.MONTH_ID MONTH_ID "
	              +" from CS_SERVICE_RE_ACTIVE_MONTH a,meta_dim_zone b " +
	              	"where a.region_id=b.zone_code and b.zone_code='"+zoneId+"'");
		if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.month_id='"+dateTime+"'");
	     }
        sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id,b.ORDER_ID)z");
		sql.append(" order by z.PAR_REGION_ID,z.ORDER_ID,z.REGION_ID");
	  return getDataAccess().queryForMap(sql.toString());
  }  
	//移动和宽带用户停机复开及时率月报表表柱状图
	public List<Map<String, Object>> getBarServiceReActive_Mon(Map<String, Object> queryData){
		String zoneId ="".equals(Convert.toString(queryData.get("zoneId")))?"0000":Convert.toString(queryData.get("zoneId"));
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id,sum(YD_SUM_NUM) YD_SUM_NUM," +
	      		"sum(YD_3MIN_NUM) YD_3MIN_NUM,to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_3MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00') YD_3MIN_NUM_LV," +
	      		"sum(YD_30MIN_NUM) YD_30MIN_NUM,to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_30MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00') YD_30MIN_NUM_LV," +
	      		"sum(KD_SUM_NUM) KD_SUM_NUM,sum(KD_10MIN_NUM) KD_10MIN_NUM,to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_10MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00') KD_10MIN_NUM_LV ," +
	      		"sum(KD_30MIN_NUM)KD_30MIN_NUM ,to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_30MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00')KD_30MIN_NUM_LV ,a.MONTH_ID MONTH_ID "
	              +" from CS_SERVICE_RE_ACTIVE_MONTH a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=2 start with zone_code ='"+zoneId+"' connect by prior zone_code = zone_par_code)b " 
	              +"where a.REGION_ID = b.ZONE_CODE ");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.month_id='"+dateTime+"'");
	     }
	   sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id,ORDER_ID) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//移动和宽带用户停机复开及时率月报表柱状图21地市
	public List<Map<String, Object>> get21BarServiceReActive_Mon(Map<String, Object> queryData){
		String zoneId ="0000";
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id,sum(YD_SUM_NUM) YD_SUM_NUM," +
	      		"sum(YD_3MIN_NUM) YD_3MIN_NUM,to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_3MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00') YD_3MIN_NUM_LV," +
	      		"sum(YD_30MIN_NUM) YD_30MIN_NUM,to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_30MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00') YD_30MIN_NUM_LV," +
	      		"sum(KD_SUM_NUM) KD_SUM_NUM,sum(KD_10MIN_NUM) KD_10MIN_NUM,to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_10MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00') KD_10MIN_NUM_LV ," +
	      		"sum(KD_30MIN_NUM)KD_30MIN_NUM ,to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_30MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00')KD_30MIN_NUM_LV ,a.MONTH_ID MONTH_ID "
	              +" from CS_SERVICE_RE_ACTIVE_MONTH a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=3 start with zone_code ='"+zoneId+"'connect by prior zone_code = zone_par_code)b " 
	              +"where a.REGION_ID = b.ZONE_CODE ");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.month_id='"+dateTime+"'");
	     }
	   sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id,ORDER_ID) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	   //移动和宽带用户停机复开及时率日报表表格数据
	public List<Map<String, Object>> getServiceReActive_Day(Map<String, Object> queryData) {
	      String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneId ="".equals(Convert.toString(queryData.get("zoneId")))?"0000":Convert.toString(queryData.get("zoneId"));
	      StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id,sum(YD_SUM_NUM) YD_SUM_NUM," +
	      		"sum(YD_3MIN_NUM) YD_3MIN_NUM,trim(to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_3MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00')||'%') YD_3MIN_NUM_LV," +
	      		"sum(YD_30MIN_NUM) YD_30MIN_NUM,trim(to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_30MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00')||'%') YD_30MIN_NUM_LV," +
	      		"sum(KD_SUM_NUM) KD_SUM_NUM,sum(KD_10MIN_NUM) KD_10MIN_NUM,trim(to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_10MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00')||'%') KD_10MIN_NUM_LV ," +
	      		"sum(KD_30MIN_NUM)KD_30MIN_NUM ,trim(to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_30MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00')||'%')KD_30MIN_NUM_LV ,a.day_id DAY_ID "
	              +" from CS_SERVICE_RE_ACTIVE_DAY a, (select zone_code ,ORDER_ID from META_DIM_ZONE start with zone_code ='"+zoneId+"' connect by prior zone_code = zone_par_code)b " +
	              "where a.REGION_ID = b.ZONE_CODE ");
	      if(dateTime!=null&&!("".equals(dateTime))){
    		  sql.append(" and a.day_id='"+dateTime+"'");
         }
          sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.day_id,ORDER_ID) z ");
          sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID");
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	} 
	//移动和宽带用户停机复开及时率日报表柱状图
	public List<Map<String, Object>> getBarServiceReActive_Day(Map<String, Object> queryData){
		String zoneId ="".equals(Convert.toString(queryData.get("zoneId")))?"0000":Convert.toString(queryData.get("zoneId"));
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id,sum(YD_SUM_NUM) YD_SUM_NUM," +
	      		"sum(YD_3MIN_NUM) YD_3MIN_NUM,trim(to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_3MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00')||'%') YD_3MIN_NUM_LV," +
	      		"sum(YD_30MIN_NUM) YD_30MIN_NUM,trim(to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_30MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00')||'%') YD_30MIN_NUM_LV," +
	      		"sum(KD_SUM_NUM) KD_SUM_NUM,sum(KD_10MIN_NUM) KD_10MIN_NUM,trim(to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_10MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00')||'%') KD_10MIN_NUM_LV ," +
	      		"sum(KD_30MIN_NUM)KD_30MIN_NUM ,trim(to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_30MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00')||'%')KD_30MIN_NUM_LV ,a.DAY_ID DAY_ID "
	              +" from CS_SERVICE_RE_ACTIVE_DAY a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=2 start with zone_code ='"+zoneId+"' connect by prior zone_code = zone_par_code)b " 
	              +"where a.REGION_ID = b.ZONE_CODE ");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.DAY_id='"+dateTime+"'");
	     }
	   sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.DAY_id,ORDER_ID) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//移动和宽带用户停机复开及时率日报表柱状图21地市
	public List<Map<String, Object>> get21BarServiceReActive_Day(Map<String, Object> queryData){
		String zoneId ="0000";
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id,sum(YD_SUM_NUM) YD_SUM_NUM," +
	      		"sum(YD_3MIN_NUM) YD_3MIN_NUM,trim(to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_3MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00')||'%') YD_3MIN_NUM_LV," +
	      		"sum(YD_30MIN_NUM) YD_30MIN_NUM,trim(to_char(DECODE(sum(YD_SUM_NUM),0,0,round((sum(YD_30MIN_NUM)/sum(YD_SUM_NUM))*100,2)),'FM99990.00')||'%') YD_30MIN_NUM_LV," +
	      		"sum(KD_SUM_NUM) KD_SUM_NUM,sum(KD_10MIN_NUM) KD_10MIN_NUM,trim(to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_10MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00')||'%') KD_10MIN_NUM_LV ," +
	      		"sum(KD_30MIN_NUM)KD_30MIN_NUM ,trim(to_char(DECODE(sum(KD_SUM_NUM),0,0,round((sum(KD_30MIN_NUM)/sum(KD_SUM_NUM))*100,2)),'FM99990.00')||'%')KD_30MIN_NUM_LV ,a.DAY_ID DAY_ID "
	              +" from CS_SERVICE_RE_ACTIVE_DAY a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=3 start with zone_code ='"+zoneId+"' connect by prior zone_code = zone_par_code)b " 
	              +"where a.REGION_ID = b.ZONE_CODE ");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.DAY_id='"+dateTime+"'");
	     }
	   sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.DAY_id,ORDER_ID) z ");
	   sql.append(" order by z.PAR_REGION_ID,ORDER_ID, z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//获取查询条件_月份
	public List<Map<String, Object>> getSelectMon_AvgSecends() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct month_id as month_id from CS_SERVICE_AVG_SECENDS_MON  order by month_id desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	public String getNewMonth_AvgSecends() {
		String sql = "select to_date(max(t.month_id),'yyyy-MM') month_no from CS_SERVICE_AVG_SECENDS_MON t ";
		return getDataAccess().queryForString(sql);
	}
	   //移动和宽带用户停机复开各环节平均时间月报表
	public List<Map<String, Object>> getServiceAvgSecends_Mon(Map<String, Object> queryData) {
	      String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneId ="".equals(Convert.toString(queryData.get("zoneId")))?"0000":Convert.toString(queryData.get("zoneId"));
	      StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		   "a.month_id month_id,"+
	      		   "decode(sum(a.YD_BILL_NUM),0,0,round(NVL(sum(a.YD_CBS_COSTTIME),0)/SUM(a.YD_BILL_NUM),2)) YD_CBS_COSTTIME,"+
	      		   "decode(sum(a.YD_BILL_NUM),0,0,round(sum(a.YD_CRM_COSTTIME)/SUM(a.YD_BILL_NUM),2))YD_CRM_COSTTIME,"+
	      		   " decode(sum(a.YD_BILL_NUM),0,0,round(sum(a.YD_SPS_COSTTIME)/SUM(a.YD_BILL_NUM),2))YD_SPS_COSTTIME,"+
	      		   "decode(SUM(a.YD_BILL_NUM),0,0,round((NVL(SUM(a.YD_CBS_COSTTIME),0)+SUM(a.YD_CRM_COSTTIME)+SUM(a.YD_SPS_COSTTIME))/SUM(a.YD_BILL_NUM),2)) YD_TOTAL_AVG_COSTTIME," +
	      		   "decode(sum(a.KD_BILL_NUM),0,0,round(NVL(sum(a.KD_CBS_COSTTIME),0)/SUM(a.KD_BILL_NUM),2)) KD_CBS_COSTTIME,"+
	      		   "decode(sum(a.KD_BILL_NUM),0,0,round(sum(a.KD_CRM_COSTTIME)/SUM(a.KD_BILL_NUM),2))KD_CRM_COSTTIME,"+
	      		   " decode(sum(a.KD_BILL_NUM),0,0,round(sum(a.KD_SPS_COSTTIME)/SUM(a.KD_BILL_NUM),2))KD_SPS_COSTTIME,"+
	      		   "decode(SUM(a.KD_BILL_NUM),0,0,round((NVL(SUM(a.KD_CBS_COSTTIME),0)+SUM(a.KD_CRM_COSTTIME)+SUM(a.KD_SPS_COSTTIME))/SUM(a.KD_BILL_NUM),2)) KD_TOTAL_AVG_COSTTIME "
	              +" from CS_SERVICE_AVG_SECENDS_MON a, (select zone_code,ORDER_ID from META_DIM_ZONE start with zone_code ='"+zoneId+"' connect by prior zone_code = zone_par_code)b " +
	              "where a.REGION_ID = b.ZONE_CODE ");
	      if(dateTime!=null&&!("".equals(dateTime))){
    		  sql.append(" and a.month_id='"+dateTime+"'");
         }
          sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id,ORDER_ID) z ");
          sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID");
           List<Map<String,Object>> list= getDataAccess().queryForList(sql.toString());
	       return list;
	}
	//移动和宽带用户停机复开各环节平均时间月报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData_AvgSecendsMon(String dateTime, Map<String,Object> map) {
		  String zoneId ="".equals(Convert.toString(map.get("zoneId")))?"0000":Convert.toString(map.get("zoneId"));
	     		  StringBuffer sql = new StringBuffer("select z.* from (select b.ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	   	      		   "a.month_id month_id,"+
	   	      		"decode(sum(a.YD_BILL_NUM),0,0,round(NVL(sum(a.YD_CBS_COSTTIME),0)/SUM(a.YD_BILL_NUM),2)) YD_CBS_COSTTIME,"+
		      		   "decode(sum(a.YD_BILL_NUM),0,0,round(sum(a.YD_CRM_COSTTIME)/SUM(a.YD_BILL_NUM),2))YD_CRM_COSTTIME,"+
		      		   " decode(sum(a.YD_BILL_NUM),0,0,round(sum(a.YD_SPS_COSTTIME)/SUM(a.YD_BILL_NUM),2))YD_SPS_COSTTIME,"+
		      		   "decode(SUM(a.YD_BILL_NUM),0,0,round((NVL(SUM(a.YD_CBS_COSTTIME),0)+SUM(a.YD_CRM_COSTTIME)+SUM(a.YD_SPS_COSTTIME))/SUM(a.YD_BILL_NUM),2)) YD_TOTAL_AVG_COSTTIME," +
		      		   "decode(sum(a.KD_BILL_NUM),0,0,round(NVL(sum(a.KD_CBS_COSTTIME),0)/SUM(a.KD_BILL_NUM),2)) KD_CBS_COSTTIME,"+
		      		   "decode(sum(a.KD_BILL_NUM),0,0,round(sum(a.KD_CRM_COSTTIME)/SUM(a.KD_BILL_NUM),2))KD_CRM_COSTTIME,"+
		      		   " decode(sum(a.KD_BILL_NUM),0,0,round(sum(a.KD_SPS_COSTTIME)/SUM(a.KD_BILL_NUM),2))KD_SPS_COSTTIME,"+
		      		   "decode(SUM(a.KD_BILL_NUM),0,0,round((NVL(SUM(a.KD_CBS_COSTTIME),0)+SUM(a.KD_CRM_COSTTIME)+SUM(a.KD_SPS_COSTTIME))/SUM(a.KD_BILL_NUM),2)) KD_TOTAL_AVG_COSTTIME "
	              +" from CS_SERVICE_AVG_SECENDS_MON a,meta_dim_zone b " +
	              	"where a.region_id=b.zone_code and b.zone_code='"+zoneId+"'");
	     if(dateTime!=null&&!("".equals(dateTime))){
	       	  sql.append(" and a.month_id='"+dateTime+"'");
	      }
        sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id,b.ORDER_ID) z ");
		sql.append(" order by z.PAR_REGION_ID, z.REGION_ID,z.ORDER_ID");
	  return getDataAccess().queryForMap(sql.toString());
  }  
	//移动和宽带用户停机复开各环节平均时间月报表柱状图
	public List<Map<String, Object>> getBarAvgSecends_Mon(Map<String, Object> queryData){
		String zoneId ="".equals(Convert.toString(queryData.get("zoneId")))?"0000":Convert.toString(queryData.get("zoneId"));
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		   "a.month_id month_id,"+
	      		 "decode(sum(a.YD_BILL_NUM),0,0,round(NVL(sum(a.YD_CBS_COSTTIME),0)/SUM(a.YD_BILL_NUM),2)) YD_CBS_COSTTIME,"+
	      		   "decode(sum(a.YD_BILL_NUM),0,0,round(sum(a.YD_CRM_COSTTIME)/SUM(a.YD_BILL_NUM),2))YD_CRM_COSTTIME,"+
	      		   " decode(sum(a.YD_BILL_NUM),0,0,round(sum(a.YD_SPS_COSTTIME)/SUM(a.YD_BILL_NUM),2))YD_SPS_COSTTIME,"+
	      		   "decode(SUM(a.YD_BILL_NUM),0,0,round((NVL(SUM(a.YD_CBS_COSTTIME),0)+SUM(a.YD_CRM_COSTTIME)+SUM(a.YD_SPS_COSTTIME))/SUM(a.YD_BILL_NUM),2)) YD_TOTAL_AVG_COSTTIME," +
	      		   "decode(sum(a.KD_BILL_NUM),0,0,round(NVL(sum(a.KD_CBS_COSTTIME),0)/SUM(a.KD_BILL_NUM),2)) KD_CBS_COSTTIME,"+
	      		   "decode(sum(a.KD_BILL_NUM),0,0,round(sum(a.KD_CRM_COSTTIME)/SUM(a.KD_BILL_NUM),2))KD_CRM_COSTTIME,"+
	      		   " decode(sum(a.KD_BILL_NUM),0,0,round(sum(a.KD_SPS_COSTTIME)/SUM(a.KD_BILL_NUM),2))KD_SPS_COSTTIME,"+
	      		   "decode(SUM(a.KD_BILL_NUM),0,0,round((NVL(SUM(a.KD_CBS_COSTTIME),0)+SUM(a.KD_CRM_COSTTIME)+SUM(a.KD_SPS_COSTTIME))/SUM(a.KD_BILL_NUM),2)) KD_TOTAL_AVG_COSTTIME "
	              +" from CS_SERVICE_AVG_SECENDS_MON a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=2 start with zone_code ='"+zoneId+"' connect by prior zone_code = zone_par_code)b " 
                   +"where a.REGION_ID = b.ZONE_CODE ");
		 if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.month_id='"+dateTime+"'");
	     }
		  sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id,ORDER_ID) z ");
	      sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//移动和宽带用户停机复开各环节平均时间月报表柱状图21地市
	public List<Map<String, Object>> get21BarAvgSecendsMon_Mon(Map<String, Object> queryData){
		String zoneId ="0000";
		String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
		StringBuffer sql = new StringBuffer("select z.* from (select ORDER_ID,a.PAR_REGION_NAME PAR_REGION_NAME,a.PAR_REGION_ID PAR_REGION_ID,a.REGION_NAME REGION_NAME,a.region_id region_id," +
	      		   "a.month_id month_id,"+
	      		 "decode(sum(a.YD_BILL_NUM),0,0,round(NVL(sum(a.YD_CBS_COSTTIME),0)/SUM(a.YD_BILL_NUM),2)) YD_CBS_COSTTIME,"+
	      		   "decode(sum(a.YD_BILL_NUM),0,0,round(sum(a.YD_CRM_COSTTIME)/SUM(a.YD_BILL_NUM),2))YD_CRM_COSTTIME,"+
	      		   " decode(sum(a.YD_BILL_NUM),0,0,round(sum(a.YD_SPS_COSTTIME)/SUM(a.YD_BILL_NUM),2))YD_SPS_COSTTIME,"+
	      		   "decode(SUM(a.YD_BILL_NUM),0,0,round((NVL(SUM(a.YD_CBS_COSTTIME),0)+SUM(a.YD_CRM_COSTTIME)+SUM(a.YD_SPS_COSTTIME))/SUM(a.YD_BILL_NUM),2)) YD_TOTAL_AVG_COSTTIME," +
	      		   "decode(sum(a.KD_BILL_NUM),0,0,round(NVL(sum(a.KD_CBS_COSTTIME),0)/SUM(a.KD_BILL_NUM),2)) KD_CBS_COSTTIME,"+
	      		   "decode(sum(a.KD_BILL_NUM),0,0,round(sum(a.KD_CRM_COSTTIME)/SUM(a.KD_BILL_NUM),2))KD_CRM_COSTTIME,"+
	      		   " decode(sum(a.KD_BILL_NUM),0,0,round(sum(a.KD_SPS_COSTTIME)/SUM(a.KD_BILL_NUM),2))KD_SPS_COSTTIME,"+
	      		   "decode(SUM(a.KD_BILL_NUM),0,0,round((NVL(SUM(a.KD_CBS_COSTTIME),0)+SUM(a.KD_CRM_COSTTIME)+SUM(a.KD_SPS_COSTTIME))/SUM(a.KD_BILL_NUM),2)) KD_TOTAL_AVG_COSTTIME "
	              +" from CS_SERVICE_AVG_SECENDS_MON a, (select level,ORDER_ID,zone_code from META_DIM_ZONE where  level=3 start with zone_code ='"+zoneId+"' connect by prior zone_code = zone_par_code)b " 
                  + "where a.REGION_ID = b.ZONE_CODE ");
		if(dateTime!=null&&!("".equals(dateTime))){
	  		  sql.append(" and a.month_id='"+dateTime+"'");
	     }
		  sql.append(" group by a.PAR_REGION_NAME,a.PAR_REGION_ID, a.REGION_NAME,a.region_id,a.month_id,ORDER_ID) z ");
	      sql.append(" order by z.PAR_REGION_ID,ORDER_ID,z.REGION_ID");
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//客户保有_存储过程
	public Map<String, Object> getCustKeep_pg(Map<String, Object> queryData) {
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
	//客户保有类指标月监测报表柱状图下钻区域
	public List<Map<String, Object>> getBarCustKeep_Zone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String prodTypeCode =Convert.toString(queryData.get("prodTypeCode"));
		 String ind="".equals(Convert.toString(queryData.get("selectCol")))?"4":Convert.toString(queryData.get("selectCol"));//默认显示宽带用户离网率
		 String sql="select rownum,region_id,zone_name,current_value from (select region_id,zone_name,to_char(DECODE(sum(NUM2),0,0,round(sum(NUM1)/sum(NUM2)*100,2)),'FM99990.00')current_value from CS_CUST_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+1 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"' and ind_id='"+ind+"'";
		 if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and prod_type_id='"+prodTypeCode+"'";
		 }
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//客户保有类指标月监测报表柱状图下级地市循环_网格数据
	public List<Map<String, Object>> getBarCustKeep_DownZone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String prodTypeCode =Convert.toString(queryData.get("prodTypeCode"));
		 String ind="".equals(Convert.toString(queryData.get("selectCol")))?"4":Convert.toString(queryData.get("selectCol"));//默认显示宽带用户离网率
		 String sql="select rownum,region_id,zone_name,current_value from (select region_id,zone_name,to_char(DECODE(sum(NUM2),0,0,round(sum(NUM1)/sum(NUM2)*100,2)),'FM99990.00')current_value from CS_CUST_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+2 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"' and ind_id='"+ind+"'";
		 if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and prod_type_id='"+prodTypeCode+"'";
		 }
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//客户保有类指标月监测报表取上月数据/折线图
	public  Map<String,Object> getChartData_CustKeepMon(String dateTime, Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String prodTypeCode =Convert.toString(map.get("prodTypeCode"));
		 String ind="".equals(Convert.toString(map.get("selectCol")))?"4":Convert.toString(map.get("selectCol"));//默认显示宽带用户离网率
		String sql = "select to_char(DECODE(sum(NUM2),0,0,round(sum(NUM1)/sum(NUM2)*100,2)),'FM99990.00')current_value" 
			  + " from CS_CUST_MON a " + 
					"where  a.region_id='"+ zoneCode+"'"; 
         sql+=" and a.month_id='"+dateTime+"' and a.ind_id='"+ind+"'";
         if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and a.prod_type_id='"+prodTypeCode+"'";
		 }
	  return getDataAccess().queryForMap(sql.toString());
  } 
	//中高端拍照用户收入保有率分析月报表柱状图下钻区域
	public List<Map<String, Object>> getBarCustPhotoRev_Zone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String prodTypeCode =Convert.toString(queryData.get("prodTypeCode"));
		 String sql="select rownum,region_id,zone_name,KEEP_LV from (select region_id,zone_name,to_char(DECODE(sum(NUM1),0,0,ROUND(SUM(NUM2)/SUM(NUM1)*100,2)),'FM99990.00')KEEP_LV from CS_CUST_PHOTO_REV_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+1 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"'";
		 if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and prod_type_id='"+prodTypeCode+"'";
		 }
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//中高端拍照用户收入保有率分析月报表柱状图下级地市循环_网格数据
	public List<Map<String, Object>> getBarCustPhotoRev_DownZone(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String prodTypeCode =Convert.toString(queryData.get("prodTypeCode"));
		 String sql="select rownum,region_id,zone_name,KEEP_LV from (select region_id,zone_name,to_char(DECODE(sum(NUM1),0,0,ROUND(SUM(NUM2)/SUM(NUM1)*100,2)),'FM99990.00')KEEP_LV from CS_CUST_PHOTO_REV_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+2 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"'";
		 if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and prod_type_id='"+prodTypeCode+"'";
		 }
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
         List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//中高端拍照用户收入保有率分析月报表取上月数据/折线图
	public  Map<String,Object> getChartData_CustPhotoRevMon(String dateTime, Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String prodTypeCode =Convert.toString(map.get("prodTypeCode"));
		String sql = "select to_char(DECODE(sum(NUM1),0,0,ROUND(SUM(NUM2)/SUM(NUM1)*100,2)),'FM99990.00')KEEP_LV" 
			  + " from CS_CUST_PHOTO_REV_MON a " + 
					"where  a.region_id='"+ zoneCode+"'"; 
         sql+=" and a.month_id='"+dateTime+"'";
         if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and a.prod_type_id='"+prodTypeCode+"'";
		 }
	  return getDataAccess().queryForMap(sql.toString());
  } 
	//主动服务类指标月监测报表
	public List<Map<String, Object>> getBarActiveServMonitor_Zone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String prodTypeCode =Convert.toString(queryData.get("prodTypeCode"));
		 String sql="select rownum,region_id,zone_name,CURRENT_VALUE from (select region_id,zone_name,IND_ID,case when max(ind_id) in ('3','6') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2)),'FM99990.00') when max(ind_id) in ('1','2') then " +
		 		"to_char(round(sum(num1)/1000000,2)) else to_char(round(sum(NUM1))) end CURRENT_VALUE from CS_ACTIVE_SERV_MONITOR_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+1 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"'";
		 if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and IND_ID='"+prodTypeCode+"'";
		 }
		  sql+=" group by order_id,region_id,zone_name,IND_ID,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//主动服务类指标月监测报表
	public List<Map<String, Object>> getBarActiveServMonitor_DownZone(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String prodTypeCode =Convert.toString(queryData.get("prodTypeCode"));
		 String sql="select rownum,region_id,zone_name,CURRENT_VALUE from (select region_id,zone_name,IND_ID,case when max(ind_id) in ('3','6') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2)),'FM99990.00') when max(ind_id) in ('1','2') then " +
		 		"to_char(round(sum(num1)/1000000,2)) else to_char(round(sum(NUM1))) end CURRENT_VALUE from CS_ACTIVE_SERV_MONITOR_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+2 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"'";
		 if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and IND_ID='"+prodTypeCode+"'";
		 }
		  sql+=" group by order_id,region_id,zone_name,IND_ID,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
         List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//主动服务类指标月监测报表
	public  Map<String,Object> getChartData_ActiveServMonitorMon(String dateTime, Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String prodTypeCode =Convert.toString(map.get("prodTypeCode"));
		String sql = "select ind_id,case when max(ind_id) in ('3','6') then " +
		 		"to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2)),'FM99990.00') when max(ind_id) in ('1','2') then " +
		 		"to_char(round(sum(num1)/1000000,2)) else to_char(round(sum(NUM1))) end CURRENT_VALUE" 
			  + " from CS_ACTIVE_SERV_MONITOR_MON a " + 
					"where  a.region_id='"+ zoneCode+"'"; 
         sql+=" and a.month_id='"+dateTime+"'";
         if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and a.IND_ID='"+prodTypeCode+"'";
		 }
         sql+=" group by a.IND_ID";
	  return getDataAccess().queryForMap(sql.toString());
  } 
	//积分客户分析月报表
	public List<Map<String, Object>> getBarIntegralCust_Zone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String sql="select rownum,region_id,zone_name,NUM1,NUM2,NUM3,NUM4,NUM5,NUM6,NUM7,NUM8,NUM9,NUM10,NUM11 from (select region_id,zone_name," +
		 		" sum(NUM1) NUM1,sum(NUM2) NUM2,sum(NUM3) NUM3,sum(NUM4) NUM4,sum(NUM5) NUM5,sum(NUM6) NUM6,sum(NUM7) NUM7,sum(NUM8) NUM8," +
		 		"sum(NUM9) NUM9,sum(NUM10) NUM10,sum(NUM11) NUM11,sum(NUM12) NUM12,sum(NUM13) NUM13,sum(NUM14) NUM14 from CS_ACTIVE_SERV_CUST_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+1 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"'";
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//积分客户分析月报表
	public List<Map<String, Object>> getBarIntegralCust_DownZone(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String sql="select rownum,region_id,zone_name,NUM1,NUM2,NUM3,NUM4,NUM5,NUM6,NUM7,NUM8,NUM9,NUM10,NUM11 from (select region_id,zone_name," +
	 		" sum(NUM1) NUM1,sum(NUM2) NUM2,sum(NUM3) NUM3,sum(NUM4) NUM4,sum(NUM5) NUM5,sum(NUM6) NUM6,sum(NUM7) NUM7,sum(NUM8) NUM8," +
	 		"sum(NUM9) NUM9,sum(NUM10) NUM10,sum(NUM11) NUM11,sum(NUM12) NUM12,sum(NUM13) NUM13,sum(NUM14) NUM14 from CS_ACTIVE_SERV_CUST_MON a,meta_user.meta_dim_zone_custgrp c " +
			"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
			"and c.dim_level = (select dim_level+2 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
			" and month_id='"+dateTime+"'";
	  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
         List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//积分客户分析月报表
	public  Map<String,Object> getChartData_IntegralCustMon(String dateTime, Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String sql = "select sum(NUM1) NUM1,sum(NUM2) NUM2,sum(NUM3) NUM3,sum(NUM4) NUM4,sum(NUM5) NUM5,sum(NUM6) NUM6,sum(NUM7) NUM7,sum(NUM8) NUM8," +
	 		"sum(NUM9) NUM9,sum(NUM10) NUM10,sum(NUM11) NUM11,sum(NUM12) NUM12,sum(NUM13) NUM13,sum(NUM14) NUM14" 
			  + " from CS_ACTIVE_SERV_CUST_MON a " + 
					"where  a.region_id='"+ zoneCode+"'"; 
         sql+=" and a.month_id='"+dateTime+"'";
	  return getDataAccess().queryForMap(sql.toString());
  }
	//积分分值分析月报表
	public List<Map<String, Object>> getBarIntegralScore_Zone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String sql="select rownum,region_id,zone_name,NUM1,NUM2,NUM3,NUM4,NUM5,NUM6,NUM7 from (select region_id,zone_name," +
		 		" sum(NUM1) NUM1,sum(NUM2) NUM2,sum(NUM3) NUM3,sum(NUM4) NUM4,sum(NUM5) NUM5,sum(NUM6) NUM6,sum(NUM7) NUM7" +
		 		" from CS_ACTIVE_SERV_SCORE_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+1 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"'";
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//积分分值分析月报表
	public List<Map<String, Object>> getBarIntegralScore_DownZone(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String sql="select rownum,region_id,zone_name,NUM1,NUM2,NUM3,NUM4,NUM5,NUM6,NUM7 from (select region_id,zone_name," +
	 		" sum(NUM1) NUM1,sum(NUM2) NUM2,sum(NUM3) NUM3,sum(NUM4) NUM4,sum(NUM5) NUM5,sum(NUM6) NUM6,sum(NUM7) NUM7" +
	 		" from CS_ACTIVE_SERV_SCORE_MON a,meta_user.meta_dim_zone_custgrp c " +
			"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
			"and c.dim_level = (select dim_level+2 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
			" and month_id='"+dateTime+"'";
	  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
         List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//积分分值分析月报表
	public  Map<String,Object> getChartData_IntegralScoreMon(String dateTime, Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String sql = "select sum(NUM1) NUM1,sum(NUM2) NUM2,sum(NUM3) NUM3,sum(NUM4) NUM4,sum(NUM5) NUM5,sum(NUM6) NUM6,sum(NUM7) NUM7" 
			  + " from CS_ACTIVE_SERV_SCORE_MON a " + 
					"where  a.region_id='"+ zoneCode+"'"; 
         sql+=" and a.month_id='"+dateTime+"'";
	  return getDataAccess().queryForMap(sql.toString());
  }
	//积分兑换分析月报表
	public List<Map<String, Object>> getBarIntegralExchange_Zone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String sql="select rownum,region_id,zone_name,NUM1,NUM2,NUM200,NUM3,NUM4,NUM5,NUM6,NUM7 from (select region_id,zone_name," +
		 		" sum(NUM1) NUM1,sum(NUM2) NUM2,to_char(DECODE(sum(NUM1),0,0,round(sum(NUM2)/(sum(NUM1)+sum(num38))*100,2)),'FM99990.00')NUM200," +
		 		"sum(NUM3) NUM3,sum(NUM4) NUM4,sum(NUM5) NUM5,sum(NUM6) NUM6,sum(NUM7) NUM7" +
		 		" from CS_ACTIVE_SERV_USE_SCORE_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+1 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"'";
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//积分兑换分析月报表
	public List<Map<String, Object>> getBarIntegralExchange_DownZone(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String sql="select rownum,region_id,zone_name,NUM1,NUM2,NUM200,NUM3,NUM4,NUM5,NUM6,NUM7 from (select region_id,zone_name," +
	 		" sum(NUM1) NUM1,sum(NUM2) NUM2,to_char(DECODE(sum(NUM1),0,0,round(sum(NUM2)/(sum(NUM1)+sum(num38))*100,2)),'FM99990.00')NUM200," +
	 		"sum(NUM3) NUM3,sum(NUM4) NUM4,sum(NUM5) NUM5,sum(NUM6) NUM6,sum(NUM7) NUM7" +
	 		" from CS_ACTIVE_SERV_USE_SCORE_MON a,meta_user.meta_dim_zone_custgrp c " +
			"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
			"and c.dim_level = (select dim_level+2 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
			" and month_id='"+dateTime+"'";
	  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
         List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//积分兑换分析月报表
	public  Map<String,Object> getChartData_IntegralExchangeMon(String dateTime, Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String sql = "select sum(NUM1) NUM1,sum(NUM2) NUM2,to_char(DECODE(sum(NUM1),0,0,round(sum(NUM2)/(sum(NUM1)+sum(num38))*100,2)),'FM99990.00')NUM200," +
		 		"sum(NUM3) NUM3,sum(NUM4) NUM4,sum(NUM5) NUM5,sum(NUM6) NUM6,sum(NUM7) NUM7" 
			  + " from CS_ACTIVE_SERV_USE_SCORE_MON a " + 
					"where  a.region_id='"+ zoneCode+"'"; 
         sql+=" and a.month_id='"+dateTime+"'";
	  return getDataAccess().queryForMap(sql.toString());
  }
	//VIP客户分析汇总月报表
	public List<Map<String, Object>> getBarVIPCust_Zone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String sql="select rownum,region_id,zone_name,NUM1,NUM2,NUM3,NUM4,NUM5,NUM6,NUM7,NUM8,NUM9,NUM10,NUM11,NUM12,NUM13,NUM14 from (select region_id,zone_name," +
		 		" sum(NUM1)NUM1,sum(NUM2)NUM2," +
		 		"sum(NUM3)NUM3,sum(NUM4)NUM4,sum(NUM5)NUM5,sum(NUM6)NUM6,sum(NUM7)NUM7,sum(NUM8)NUM8,sum(NUM9)NUM9,sum(NUM10)NUM10,sum(NUM11)NUM11,sum(NUM12)NUM12," +
		 		"sum(NUM13)NUM13,sum(NUM14)NUM14 from CS_ACTIVE_SERV_VIP_CUST_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+1 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"'";
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//VIP客户分析汇总月报表
	public List<Map<String, Object>> getBarVIPCust_DownZone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String sql="select rownum,region_id,zone_name,NUM1,NUM2,NUM3,NUM4,NUM5,NUM6,NUM7,NUM8,NUM9,NUM10,NUM11,NUM12,NUM13,NUM14 from (select region_id,zone_name," +
		 		" sum(NUM1)NUM1,sum(NUM2)NUM2," +
		 		"sum(NUM3)NUM3,sum(NUM4)NUM4,sum(NUM5)NUM5,sum(NUM6)NUM6,sum(NUM7)NUM7,sum(NUM8)NUM8,sum(NUM9)NUM9,sum(NUM10)NUM10,sum(NUM11)NUM11,sum(NUM12)NUM12," +
		 		"sum(NUM13)NUM13,sum(NUM14)NUM14 from CS_ACTIVE_SERV_VIP_CUST_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+2 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"'";
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//VIP客户分析汇总月报表
	public  Map<String,Object> getChartData_VIPCustMon(String dateTime, Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String sql = "select sum(NUM1)NUM1,sum(NUM2)NUM2," +
		 		"sum(NUM3)NUM3,sum(NUM4)NUM4,sum(NUM5)NUM5,sum(NUM6)NUM6,sum(NUM7)NUM7,sum(NUM8)NUM8,sum(NUM9)NUM9,sum(NUM10)NUM10,sum(NUM11)NUM11,sum(NUM12)NUM12,sum(NUM13)NUM13,sum(NUM14)NUM14" 
			  + " from CS_ACTIVE_SERV_VIP_CUST_MON a " + 
					"where  a.region_id='"+ zoneCode+"'"; 
         sql+=" and a.month_id='"+dateTime+"'";
	  return getDataAccess().queryForMap(sql.toString());
  }
	//俱乐部会员数分析月报表
	public List<Map<String, Object>> getBarClubMembers_Zone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String sql="select rownum,region_id,zone_name,club_type_id,CLUB_TYPE_NAME,NUM1,NUM2,NUM3,NUM4,NUM5 from (select region_id,zone_name," +
		 		"club_type_id,CLUB_TYPE_NAME,sum(NUM1)NUM1,sum(NUM4)NUM2,to_char(DECODE(sum(NUM1), 0, 0, round(sum(NUM4) / sum(NUM1), 2)),'FM99990.00')NUM3," +
		 		"sum(NUM7)NUM4,to_char(DECODE(sum(NUM1), 0, 0, round(sum(NUM7) / sum(NUM1), 2)),'FM99990.00')NUM5" +
		 		" from CS_ACTIVE_SERV_CLUB_NUM_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+1 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"'";
		  sql+=" group by order_id,region_id,zone_name,club_type_id,CLUB_TYPE_NAME,dim_level order by c.order_id, region_id,c.dim_level,club_type_id)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//俱乐部会员数分析月报表
	public List<Map<String, Object>> getBarClubMembers_DownZone(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String sql="select rownum,region_id,zone_name,club_type_id,CLUB_TYPE_NAME,NUM1,NUM2,NUM3,NUM4,NUM5 from (select region_id,zone_name," +
		 		"club_type_id,CLUB_TYPE_NAME,sum(NUM1)NUM1,sum(NUM4)NUM2,to_char(DECODE(sum(NUM1), 0, 0, round(sum(NUM4) / sum(NUM1), 2)),'FM99990.00')NUM3," +
		 		"sum(NUM7)NUM4,to_char(DECODE(sum(NUM1), 0, 0, round(sum(NUM7) / sum(NUM1), 2)),'FM99990.00')NUM5" +
		 		" from CS_ACTIVE_SERV_CLUB_NUM_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+2 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"'";
		  sql+=" group by order_id,region_id,zone_name,club_type_id,CLUB_TYPE_NAME,dim_level order by c.order_id, region_id,c.dim_level,club_type_id)where rownum<22 ";
         List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//俱乐部会员数分析月报表
	public  Map<String,Object> getChartData_ClubMembersMon(String dateTime, Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String sql = "select sum(NUM1)NUM1,sum(NUM4)NUM2,to_char(DECODE(sum(NUM1), 0, 0, round(sum(NUM4) / sum(NUM1), 2)),'FM99990.00')NUM3," +
		 		"sum(NUM7)NUM4,to_char(DECODE(sum(NUM1), 0, 0, round(sum(NUM7) / sum(NUM1), 2)),'FM99990.00')NUM5" 
			  + " from CS_ACTIVE_SERV_CLUB_NUM_MON a " + 
					"where  a.region_id='"+ zoneCode+"'"; 
         sql+=" and a.month_id='"+dateTime+"'";
	  return getDataAccess().queryForMap(sql.toString());
  }
	//服务过程监控类指标月监测报表
	public List<Map<String, Object>> getBarServProcessMonitorMon_Zone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String prodTypeCode =Convert.toString(queryData.get("prodTypeCode"));
		 String indId =Convert.toString(queryData.get("indId"));
		 String sql="select rownum,region_id,zone_name,ind_id,ind_name,CURRENT_VALUE from (select region_id,zone_name," +
		 		"ind_id,ind_name,case when max(ind_id) in ('3','5','7') then to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2)),'FM99990.00')"+
            " when max(ind_id)='1' then to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2),2))) else to_char(SUM(NUM1))end CURRENT_VALUE" +
		 		" from CS_SERVICE_MONITOR_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+1 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"' and ind_id='"+indId+"'";
		 if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and a.PROD_TYPE_ID='"+prodTypeCode+"'";
		 }
		  sql+=" group by order_id,region_id,zone_name,ind_id,ind_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//服务过程监控类指标月监测报表
	public List<Map<String, Object>> getBarServProcessMonitorMon_DownZone(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String prodTypeCode =Convert.toString(queryData.get("prodTypeCode"));
		 String indId =Convert.toString(queryData.get("indId"));
		 String sql="select rownum,region_id,zone_name,ind_id,ind_name,CURRENT_VALUE from (select region_id,zone_name," +
		 		"ind_id,ind_name,case when max(ind_id) in ('3','5','7') then to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2)),'FM99990.00')"+
           " when max(ind_id)='1' then to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2),2))) else to_char(SUM(NUM1))end CURRENT_VALUE" +
		 		" from CS_SERVICE_MONITOR_MON a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+2 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and month_id='"+dateTime+"' and ind_id='"+indId+"'";
		 if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and a.PROD_TYPE_ID='"+prodTypeCode+"'";
		 }
		  sql+=" group by order_id,region_id,zone_name,ind_id,ind_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
         List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//服务过程监控类指标月监测报表
	public  Map<String,Object> getChartData_ServProcessMonitorMon(String dateTime, Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String prodTypeCode =Convert.toString(map.get("prodTypeCode"));
		 String indId =Convert.toString(map.get("indId"));
		 String sql = "select case when max(ind_id) in ('3','5','7') then to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2)*100,2)),'FM99990.00')"+
           " when max(ind_id)='1' then to_char(DECODE(SUM(NUM2),0,0,ROUND(SUM(NUM1)/SUM(NUM2),2))) else to_char(SUM(NUM1))end CURRENT_VALUE" 
			  + " from CS_SERVICE_MONITOR_MON a " + 
					"where  a.region_id='"+ zoneCode+"' and ind_id='"+indId+"'"; 
		 if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and a.PROD_TYPE_ID='"+prodTypeCode+"'";
		 }
         sql+=" and a.month_id='"+dateTime+"'";
	  return getDataAccess().queryForMap(sql.toString());
  }
	//服务过程监控类指标日监测报表
	public List<Map<String, Object>> getBarServProcessMonitorDay_Zone(Map<String, Object> queryData){
		 String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String prodTypeCode =Convert.toString(queryData.get("prodTypeCode"));
		 String indId =Convert.toString(queryData.get("indId"));
		 String sql="select rownum,region_id,zone_name,CURRENT_VALUE from (select region_id,zone_name," +
		 		"case when max(ind_id) in('4','5','6') then DECODE(sum(NUM1),0,0,round(sum(NUM1)/sum(NUM2),2))"+
                " else SUM(NUM1) end CURRENT_VALUE" +
		 		" from CS_SERVICE_MONITOR_DAY a,meta_user.meta_dim_zone_custgrp c " +
				"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
				"and c.dim_level = (select dim_level+1 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
				" and day_id='"+dateTime+"' and ind_id='"+indId+"'";
		 if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and a.PROD_TYPE_ID='"+prodTypeCode+"'";
		 }
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
          List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//服务过程监控类指标日监测报表
	public List<Map<String, Object>> getBarServProcessMonitorDay_DownZone(Map<String, Object> queryData){
		String dateTime =Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
		 String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		 String prodTypeCode =Convert.toString(queryData.get("prodTypeCode"));
		 String indId =Convert.toString(queryData.get("indId"));
		 String sql="select rownum,region_id,zone_name,CURRENT_VALUE from (select region_id,zone_name," +
	 		"case when max(ind_id) in('4','5','6') then DECODE(sum(NUM1),0,0,round(sum(NUM1)/sum(NUM2),2))"+
            " else SUM(NUM1) end CURRENT_VALUE" +
	 		" from CS_SERVICE_MONITOR_DAY a,meta_user.meta_dim_zone_custgrp c " +
			"where a.region_id = c.ZONE_CODE(+) and c.treecode like (select treecode from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')||'%' " +
			"and c.dim_level = (select dim_level+2 from meta_dim_zone_custgrp where ZONE_CODE ='"+zoneCode+"')" +
			" and day_id='"+dateTime+"' and ind_id='"+indId+"'";
		 if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and a.PROD_TYPE_ID='"+prodTypeCode+"'";
		 }
		  sql+=" group by order_id,region_id,zone_name,dim_level order by c.order_id, region_id,c.dim_level)where rownum<22 ";
         List<Map<String,Object>> list=getDataAccess().queryForList(sql.toString());
	      return list;
	}
	//服务过程监控类指标日监测报表
	public  Map<String,Object> getChartData_ServProcessMonitorDay(String dateTime, Map<String,Object> map) {
		 String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		 String prodTypeCode =Convert.toString(map.get("prodTypeCode"));
		 String indId =Convert.toString(map.get("indId"));
		 String sql = "select case when max(ind_id) in('4','5','6') then DECODE(sum(NUM1),0,0,round(sum(NUM1)/sum(NUM2),2))"+
            " else SUM(NUM1) end CURRENT_VALUE" 
			  + " from CS_SERVICE_MONITOR_DAY a " + 
					"where  a.region_id='"+ zoneCode+"' and ind_id='"+indId+"'"; 
		 if(prodTypeCode!=null&&!("".equals(prodTypeCode))){
			 sql+=" and a.PROD_TYPE_ID='"+prodTypeCode+"'";
		 }
         sql+=" and a.day_id='"+dateTime+"'";
	  return getDataAccess().queryForMap(sql.toString());
  }
	public Map<String, Object> getFailureOrder_pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_CS_CHANNEL_FUALT_DISTRI;	
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
	   //停机复开及时率报表存储过程
	public Map<String, Object> getServiceReActive_Pg(Map<String, Object> queryData) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_TFJ_INTIME_DAY;	
		Object[] params = {
				MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "prodId", null),
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
	//停机复开及时率报表折线图取开始日期到结束日期之间的数据
	public List<Map<String, Object>> getLinePoint(String startTime,String endTime,Map<String, Object> queryData){
		String zoneCode ="".equals(Convert.toString(queryData.get("zoneCode")))?"0000":Convert.toString(queryData.get("zoneCode"));
		String prodId= Convert.toString(queryData.get("prodId"));
	    StringBuffer sql = new StringBuffer("select DAY_ID,to_char(round(decode(sum(case when PROD_ID=30 then cnt_num end ),0,0,(sum(case when STANDARD_LEVEL=1 then  cnt_num end )/sum(case when PROD_ID=30 then cnt_num end )))*100,2),'FM99990.00') YD_3MIN_NUM_LV,"+
		"to_char(round(decode(sum(case when PROD_ID=30 then cnt_num end ),0,0,(sum(case when STANDARD_LEVEL in(1,2) then  cnt_num end )/sum(case when PROD_ID=30 then cnt_num end )))*100,2),'FM99990.00') YD_30MIN_NUM_LV,"+
		"to_char(round(decode(sum(case when PROD_ID=40 then cnt_num end ),0,0,(sum(case when STANDARD_LEVEL=3 then  cnt_num end )/sum(case when PROD_ID=40 then cnt_num end )))*100,2),'FM99990.00') KD_10MIN_NUM_LV ,"+
		"to_char(round(decode(sum(case when PROD_ID=40 then cnt_num end ),0,0,(sum(case when STANDARD_LEVEL in(3,4) then  cnt_num end )/sum(case when PROD_ID=40 then cnt_num end )))*100,2),'FM99990.00')KD_30MIN_NUM_LV,"+
		"decode(sum(cnt_num),0,0,round(sum(case when PROD_ID=30 then  CBS_COSTTIME end )/SUM(cnt_num),2)) YD_CBS_COSTTIME,"+
		"decode(sum(cnt_num),0,0,round(sum(case when PROD_ID=30 then  CRM_COSTTIME end )/SUM(cnt_num),2))YD_CRM_COSTTIME,"+
		"decode(sum(cnt_num),0,0,round(sum(case when PROD_ID=30 then  SPS_COSTTIME end )/SUM(cnt_num),2))YD_SPS_COSTTIME,"+
		"decode(SUM(cnt_num),0,0,round(sum(case when PROD_ID=30 then COSTTIME end )/SUM(cnt_num),2)) YD_TOTAL_AVG_COSTTIME,"+
		"decode(sum(cnt_num),0,0,round(sum(case when PROD_ID=40 then  CBS_COSTTIME end )/SUM(cnt_num),2)) KD_CBS_COSTTIME,"+
		"decode(sum(cnt_num),0,0,round(sum(case when PROD_ID=40 then  CRM_COSTTIME end )/SUM(cnt_num),2))KD_CRM_COSTTIME,"+
		"decode(sum(cnt_num),0,0,round(sum(case when PROD_ID=40 then  SPS_COSTTIME end )/SUM(cnt_num),2))KD_SPS_COSTTIME,"+
		"decode(SUM(cnt_num),0,0,round(sum(case when PROD_ID=40 then COSTTIME end )/SUM(cnt_num),2)) KD_TOTAL_AVG_COSTTIME"+
	    " from CS_SERVICE_RE_ACTIVE_DAY where REGION_ID='"+zoneCode+"' " );
	    if(prodId!=null&&!("".equals(prodId))){
	   	    sql.append(" and PROD_ID='"+prodId+"'");
	   	}
	  	sql.append(" and DAY_ID>='"+startTime+"' and DAY_ID<='"+endTime+"' group by day_id order by day_id");
	    return getDataAccess().queryForList(sql.toString());
	}
	//移动和宽带用户停机复开及时率报表取上月、上年数据/折线图
	public  Map<String,Object> getChartData_ReActiveDay(String dateTime, Map<String,Object> map) {
		  String zoneCode ="".equals(Convert.toString(map.get("zoneCode")))?"0000":Convert.toString(map.get("zoneCode"));
		  int dateTimeLen= dateTime.length();
		  String prodId= Convert.toString(map.get("prodId"));
	     		  StringBuffer sql = new StringBuffer("select to_char(round(decode(sum(case when PROD_ID=30 then cnt_num end ),0,0,(sum(case when STANDARD_LEVEL=1 then  cnt_num end )/sum(case when PROD_ID=30 then cnt_num end )))*100,2),'FM99990.00') YD_3MIN_NUM_LV,"+
		"to_char(round(decode(sum(case when PROD_ID=30 then cnt_num end ),0,0,(sum(case when STANDARD_LEVEL in(1,2) then  cnt_num end )/sum(case when PROD_ID=30 then cnt_num end )))*100,2),'FM99990.00') YD_30MIN_NUM_LV,"+
		"to_char(round(decode(sum(case when PROD_ID=40 then cnt_num end ),0,0,(sum(case when STANDARD_LEVEL=3 then  cnt_num end )/sum(case when PROD_ID=40 then cnt_num end )))*100,2),'FM99990.00') KD_10MIN_NUM_LV ,"+
		"to_char(round(decode(sum(case when PROD_ID=40 then cnt_num end ),0,0,(sum(case when STANDARD_LEVEL in(3,4) then  cnt_num end )/sum(case when PROD_ID=40 then cnt_num end )))*100,2),'FM99990.00')KD_30MIN_NUM_LV,"+
		"decode(sum(cnt_num),0,0,round(sum(case when PROD_ID=30 then  CBS_COSTTIME end )/SUM(cnt_num),2)) YD_CBS_COSTTIME,"+
		"decode(sum(cnt_num),0,0,round(sum(case when PROD_ID=30 then  CRM_COSTTIME end )/SUM(cnt_num),2))YD_CRM_COSTTIME,"+
		"decode(sum(cnt_num),0,0,round(sum(case when PROD_ID=30 then  SPS_COSTTIME end )/SUM(cnt_num),2))YD_SPS_COSTTIME,"+
		"decode(SUM(cnt_num),0,0,round(sum(case when PROD_ID=30 then COSTTIME end )/SUM(cnt_num),2)) YD_TOTAL_AVG_COSTTIME,"+
		"decode(sum(cnt_num),0,0,round(sum(case when PROD_ID=40 then  CBS_COSTTIME end )/SUM(cnt_num),2)) KD_CBS_COSTTIME,"+
		"decode(sum(cnt_num),0,0,round(sum(case when PROD_ID=40 then  CRM_COSTTIME end )/SUM(cnt_num),2))KD_CRM_COSTTIME,"+
		"decode(sum(cnt_num),0,0,round(sum(case when PROD_ID=40 then  SPS_COSTTIME end )/SUM(cnt_num),2))KD_SPS_COSTTIME,"+
		"decode(SUM(cnt_num),0,0,round(sum(case when PROD_ID=40 then COSTTIME end )/SUM(cnt_num),2)) KD_TOTAL_AVG_COSTTIME"+
	    " from CS_SERVICE_RE_ACTIVE_DAY where REGION_ID='"+zoneCode+"' " );
		if(dateTimeLen>6){//日
	  		sql.append(" and DAY_ID='"+dateTime+"'");
	    }else{//月
	    	sql.append(" and MONTH_ID='"+dateTime+"'");
	    }
		if(prodId!=null&&!("".equals(prodId))){
	  		  sql.append(" and PROD_ID='"+prodId+"'");
	     }
	  return getDataAccess().queryForMap(sql.toString());
  } 
	public Integer getDataCount(Map<String, Object> paramMap) {
		Object startTime =  paramMap.get("startDate");
        Object endTime =    paramMap.get("endDate");
        Object zoneCode =  paramMap.get("zoneCode");
        String startDateTime=Convert.toString(startTime).replaceAll("-", "");
        String endDateTime=Convert.toString(endTime).replaceAll("-", "");
        String prodId = Convert.toString(paramMap.get("prodId"));
	    StringBuffer sql=new StringBuffer();
	    sql.append("SELECT COUNT(DAY_ID) FROM TBAS_DM.FT_TFJ_ORDER_"+startDateTime.substring(0, 6)+" f WHERE 1=1");
        List<Object> params=new ArrayList<Object>();
        sql.append(" AND f.DAY_ID >='"+startDateTime+"'"); 
        sql.append(" AND f.DAY_ID <='"+endDateTime+"'");  
        if(zoneCode != null && !"".equals(zoneCode))
        {
            sql.append(" AND f.REGION_TREECODE like (select treecode from meta_dim_zone z where zone_code =?)||'%' ");
            params.add(Convert.toString(zoneCode));
        }if(prodId != null && !"".equals(prodId))
        {
            sql.append(" and f.prod_id='"+prodId+"' ");
        }
		return getDataAccess().queryForInt(sql.toString(), params.toArray());
	}
	public Map<String, Object> getTableData(Map<String, Object> map, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_TFJ_ORDER_DETAIL;
		Object[] params = {
				MapUtils.getString(map, "startDate", null).replaceAll("-", ""),
				MapUtils.getString(map, "endDate",   null).replaceAll("-", ""),
				MapUtils.getString(map, "zoneCode",  null),
				MapUtils.getString(map, "prodId",  null),
			    page.getStartRow(),
				page.getEndRow(),
				new DBOutParameter(OracleTypes.INTEGER),
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
