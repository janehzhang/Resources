package tydic.portalCommon.serviceControl;

import java.util.Map;

import tydic.meta.common.MetaBaseDAO;
/**
 * 
 * @author yanhd
 *
 */
public class StepInstallDAO extends MetaBaseDAO{
    /**
     * 获得天的数据
     * @param dateTime
     * @return
     */
	public StepInstallBean getStepInstallDay(String dateTime,String indexType){
		 
		StepInstallBean bean =new StepInstallBean();
		 
		 Map<String, Object> obj1=getSFHJDay(dateTime,indexType);
		 Map<String, Object> last_obj1=getLast_SFHJDay(StringUtil.getLastMon(dateTime.substring(0, dateTime.length()-2)),indexType);
		 
		 Map<String, Object> obj2=getZYPZDay(dateTime,indexType);
		 Map<String, Object> last_obj2=getLast_ZYPZDay(StringUtil.getLastMon(dateTime.substring(0, dateTime.length()-2)),indexType);
		 
		 Map<String, Object> obj3=getSJSGDay(dateTime,indexType);
		 Map<String, Object> last_obj3=getLast_SJSGDay(StringUtil.getLastMon(dateTime.substring(0, dateTime.length()-2)),indexType);
		 
		 Map<String, Object> obj4=getWSGHJDay(dateTime,indexType);
		 Map<String, Object> last_obj4=getLast_WSGHJDay(StringUtil.getLastMon(dateTime.substring(0, dateTime.length()-2)),indexType);
		 
		 
		 //订单异常
	     Map<String, Object> order_obj1=getDelayDay(dateTime,indexType);
	     Map<String, Object>  order_last_obj1=getLast_DelayDay(StringUtil.getLastMon(dateTime.substring(0, dateTime.length()-2)),indexType);
		 
	     Map<String, Object> order_obj2=getWaitDay(dateTime,indexType);
	     Map<String, Object>  order_last_obj2=getLast_WaitDay(StringUtil.getLastMon(dateTime.substring(0, dateTime.length()-2)),indexType);
		 
	     Map<String, Object>  order_obj3=getCancelListDay(dateTime,indexType);
	     Map<String, Object>  order_last_obj3=getLast_CancelListDay(StringUtil.getLastMon(dateTime.substring(0, dateTime.length()-2)),indexType);
	     
		try{
		  //收费环节	
		  if(null!=obj1){
			   bean.setNum1(obj1.get("NUM1") ==null?"0":obj1.get("NUM1"));
			   bean.setAvgTime_num1(obj1.get("AVGTIME_NUM1") ==null?"0":obj1.get("AVGTIME_NUM1"));
		    }else{
		       bean.setNum1("0");
			   bean.setAvgTime_num1("0");   
		    }
		  
		   if(null!=last_obj1){
			   bean.setAvgTime_lastNum1(last_obj1.get("LASTAVGTIME1") ==null?"0":last_obj1.get("LASTAVGTIME1"));
			   bean.setAvg1(last_obj1.get("LASTAVGNUM1") ==null?"0":last_obj1.get("LASTAVGNUM1"));
		    }else{
                bean.setAvgTime_lastNum1("0");
                bean.setAvg1("0");
		    }  
		  //资源配置  
		   if(null != obj2){
			   bean.setNum2(obj2.get("NUM2") ==null?"0":obj2.get("NUM2"));
			   bean.setAvgTime_num2(obj2.get("AVGTIME_NUM2") ==null?"0":obj2.get("AVGTIME_NUM2"));
		    }else{
		       bean.setNum2("0");
			   bean.setAvgTime_num2("0");   
		    }	   
		   if(null != last_obj2){
			   bean.setAvgTime_lastNum2(last_obj2.get("LASTAVGTIME2") ==null?"0":last_obj2.get("LASTAVGTIME2"));
			   bean.setAvg2(last_obj2.get("LASTAVGNUM2") ==null?"0":last_obj2.get("LASTAVGNUM2"));
		    }else{
                bean.setAvgTime_lastNum2("0");
                bean.setAvg2("0");
		    }		 
		   //数据施工
		   if(null != obj3){
			   bean.setNum3(obj3.get("NUM3") ==null?"0":obj3.get("NUM3"));
			   bean.setAvgTime_num3(obj3.get("AVGTIME_NUM3") ==null?"0":obj3.get("AVGTIME_NUM3"));
		    }else{
		       bean.setNum3("0");
			   bean.setAvgTime_num3("0");   
		    }	   
		   if(null != last_obj3){
			   bean.setAvgTime_lastNum3(last_obj3.get("LASTAVGTIME3") ==null?"0":last_obj3.get("LASTAVGTIME3"));
			   bean.setAvg3(last_obj3.get("LASTAVGNUM3") ==null?"0":last_obj3.get("LASTAVGNUM3"));
		    }else{
                bean.setAvgTime_lastNum3("0");
                bean.setAvg3("0");
		    }			   		   
		   //外线施工环
		   if(null != obj4){
			   bean.setNum4(obj4.get("NUM4") ==null?"0":obj4.get("NUM4"));
			   bean.setAvgTime_num4(obj4.get("AVGTIME_NUM4") ==null?"0":obj4.get("AVGTIME_NUM4"));
		    }else{
		       bean.setNum4("0");
			   bean.setAvgTime_num4("0");   
		    }	   
		   if(null != last_obj4){
			   bean.setAvgTime_lastNum4(last_obj4.get("LASTAVGTIME4") ==null?"0":last_obj4.get("LASTAVGTIME4"));
			   bean.setAvg4(last_obj4.get("LASTAVGNUM4") ==null?"0":last_obj4.get("LASTAVGNUM4"));
		    }else{
                bean.setAvgTime_lastNum4("0");
                bean.setAvg4("0");
		    }
		   
		   //缓装单
		   if(null != order_obj1){
			   bean.setOrderExcepiton_num1(order_obj1.get("AVGTIME") ==null?"0":order_obj1.get("AVGTIME"));
			   bean.setOrderExcepiton_num2(order_obj1.get("NUM") ==null?"0":order_obj1.get("NUM"));
		    }else{
		       bean.setOrderExcepiton_num1("0");
			   bean.setOrderExcepiton_num2("0");   
		    }
		   if(null != order_last_obj1){
			   bean.setOrderExcepiton_avg1(order_last_obj1.get("AVGTIME") ==null?"0":order_last_obj1.get("AVGTIME"));
			   bean.setOrderExcepiton_avg2(order_last_obj1.get("NUM") ==null?"0":order_last_obj1.get("NUM"));
		    }else{
		       bean.setOrderExcepiton_avg1("0");
			   bean.setOrderExcepiton_avg2("0");   
		    }
		   //待装单
		  if(null != order_obj2){
			   bean.setOrderExcepiton_num3(order_obj2.get("AVGTIME") ==null?"0":order_obj2.get("AVGTIME"));
			   bean.setOrderExcepiton_num4(order_obj2.get("NUM") ==null?"0":order_obj2.get("NUM"));
		  }else{
		       bean.setOrderExcepiton_num3("0");
			   bean.setOrderExcepiton_num4("0");   
		  }
		 if(null != order_last_obj2){
			   bean.setOrderExcepiton_avg3(order_last_obj2.get("AVGTIME") ==null?"0":order_last_obj2.get("AVGTIME"));
			   bean.setOrderExcepiton_avg4(order_last_obj2.get("NUM") ==null?"0":order_last_obj2.get("NUM"));
		  }else{
		       bean.setOrderExcepiton_avg3("0");
			   bean.setOrderExcepiton_avg4("0");   
		    }
	      //撤单
		  if(null != order_obj3){
			   bean.setOrderExcepiton_num5(order_obj3.get("NUM") ==null?"0":order_obj3.get("NUM"));
		  }else{
		       bean.setOrderExcepiton_num5("0");
		  }
		 if(null != order_last_obj3){
			   bean.setOrderExcepiton_avg5(order_last_obj3.get("NUM") ==null?"0":order_last_obj3.get("NUM"));
		  }else{
		       bean.setOrderExcepiton_avg5("0");
          }
		 
		 
		 
		  
		}catch(Exception e){
			e.printStackTrace();
		}
		return bean;
    } 
	//收费环节
	public Map<String, Object> getSFHJDay(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM5,2) as avgTime_num1, (NUM10+NUM11+NUM12) as num1 "+
				"FROM CS_BROADBAND_MAINT_DAY WHERE REGION_ID='0000' AND DAY_ID='"+dateTime+"'");
		if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
		return getDataAccess().queryForMap(sql.toString());
	}
	
	
	public Map<String, Object> getLast_SFHJDay(Integer dateTime,String indexType) {
		StringBuffer sql = new StringBuffer("select round(sum(NUM5)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2) LASTAVGTIME1,"
                     +" round(sum(NUM10+NUM11+NUM12)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2) LASTAVGNUM1"
                     +" from CS_BROADBAND_MAINT_DAY where substr(day_id,1,6)='"+dateTime+"' and REGION_ID='0000'");
	    if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
	    return getDataAccess().queryForMap(sql.toString());
	}
	
	
	
	//资源配置
	public Map<String, Object> getZYPZDay(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM16,2) as avgTime_num2, (NUM24+NUM25) as num2 "+
		"FROM CS_BROADBAND_MAINT_DAY WHERE REGION_ID='0000' AND DAY_ID='"+dateTime+"'");
		if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
		return getDataAccess().queryForMap(sql.toString());
	}
    
	public Map<String, Object> getLast_ZYPZDay(Integer dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("select round(sum(NUM16)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2)   LASTAVGTIME2,"
                     +" round(sum(NUM24+NUM25)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2) LASTAVGNUM2"
                     +" from CS_BROADBAND_MAINT_DAY where substr(day_id,1,6)='"+dateTime+"' and REGION_ID='0000'");
	    if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
	    
	    return getDataAccess().queryForMap(sql.toString());
   }
	
	
   //数据施工
	public Map<String, Object> getSJSGDay(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM27,2) as avgTime_num3, (NUM30+NUM31) as num3 "+
		"FROM CS_BROADBAND_MAINT_DAY WHERE REGION_ID='0000' AND DAY_ID='"+dateTime+"'");
		if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
		return getDataAccess().queryForMap(sql.toString());
	}
    
	public Map<String, Object> getLast_SJSGDay(Integer dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("select round(sum(NUM27)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2)   LASTAVGTIME3,"
                     +" round(sum(NUM30+NUM31)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2) LASTAVGNUM3"
                     +" from CS_BROADBAND_MAINT_DAY where substr(day_id,1,6)='"+dateTime+"' and REGION_ID='0000'");
		if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
	    return getDataAccess().queryForMap(sql.toString());
   }
	
	 //外线施工环
	public Map<String, Object> getWSGHJDay(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM5*14.32,2) as avgTime_num4, (NUM34+NUM35+NUM36) as num4 "+
		"FROM CS_BROADBAND_MAINT_DAY WHERE REGION_ID='0000' AND DAY_ID='"+dateTime+"'");
		if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
		return getDataAccess().queryForMap(sql.toString());
	}
    
	public Map<String, Object> getLast_WSGHJDay(Integer dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("select round(sum(NUM5)*15.35/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2)   LASTAVGTIME4,"
                     +" round(sum(NUM34+NUM35+NUM36)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2)   LASTAVGNUM4"
                     +" from CS_BROADBAND_MAINT_DAY where substr(day_id,1,6)='"+dateTime+"' and REGION_ID='0000'");
		if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
	    return getDataAccess().queryForMap(sql.toString());
   }
	
	
	
	
	
	
	//缓装库
	public Map<String, Object> getDelayDay(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM39,2) as AVGTIME, NUM38 as NUM "+
		"FROM CS_BROADBAND_MAINT_DAY WHERE REGION_ID='0000' AND DAY_ID='"+dateTime+"'");
		if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
		return getDataAccess().queryForMap(sql.toString());
	}
    
	public Map<String, Object> getLast_DelayDay(Integer dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("select round(sum(NUM39)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2)   AVGTIME,"
                     +" round(sum(NUM38)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2)   NUM"
                     +" from CS_BROADBAND_MAINT_DAY where substr(day_id,1,6)='"+dateTime+"' and REGION_ID='0000'");
		if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
	    return getDataAccess().queryForMap(sql.toString());
   }
	
	//待装库
	public Map<String, Object> getWaitDay(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM42,2) as AVGTIME, NUM41 as NUM "+
		"FROM CS_BROADBAND_MAINT_DAY WHERE REGION_ID='0000' AND DAY_ID='"+dateTime+"'");
		if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
		return getDataAccess().queryForMap(sql.toString());
	}
    
	public Map<String, Object> getLast_WaitDay(Integer dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("select round(sum(NUM42)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2)   AVGTIME,"
                     +" round(sum(NUM41)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2)   NUM"
                     +" from CS_BROADBAND_MAINT_DAY where substr(day_id,1,6)='"+dateTime+"' and REGION_ID='0000'");
		if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
	    return getDataAccess().queryForMap(sql.toString());
   }
	
	//撤单
	public Map<String, Object> getCancelListDay(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT 0 as AVGTIME,NUM37 as NUM "+
		"FROM CS_BROADBAND_MAINT_DAY WHERE REGION_ID='0000' AND DAY_ID='"+dateTime+"'");
		if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
		return getDataAccess().queryForMap(sql.toString());
	}
    
	public Map<String, Object> getLast_CancelListDay(Integer dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("select round(sum(0)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2)   AVGTIME,"
	    	         +" round(sum(NUM37)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2)   NUM"
                     +" from CS_BROADBAND_MAINT_DAY where substr(day_id,1,6)='"+dateTime+"' and REGION_ID='0000'");
		if(indexType!=null){
	    	sql.append("AND IS_FTTOH='"+indexType+"'");
	    }
	    return getDataAccess().queryForMap(sql.toString());
   }
	
	
	/**
	 * 获得月的数据
	 * @param dateTime
	 * @return
	 */
	public StepInstallBean getStepInstallMon(String dateTime,String indexType){
		 StepInstallBean bean =new StepInstallBean();
		 String lastDateTime=String.valueOf(StringUtil.getLastMon(dateTime));
		
		 Map<String, Object> obj1=getSFHJMon(dateTime,indexType);
		 Map<String, Object> last_obj1=getSFHJMon(lastDateTime,indexType);
		 
		 Map<String, Object> obj2=getZYPZMon(dateTime,indexType);
		 Map<String, Object> last_obj2=getZYPZMon(lastDateTime,indexType);
		 
		 Map<String, Object> obj3=getSJSGMon(dateTime,indexType);
		 Map<String, Object> last_obj3=getSJSGMon(lastDateTime,indexType);
		 
		 Map<String, Object> obj4=getWSGHJMon(dateTime,indexType);
		 Map<String, Object> last_obj4=getWSGHJMon(lastDateTime,indexType);
		 
		 
		 //订单异常
	     Map<String, Object> order_obj1=getDelayMon(dateTime,indexType);//缓装
	     Map<String, Object>  order_last_obj1=getDelayMon(lastDateTime,indexType);
		 
	     Map<String, Object> order_obj2=getWaitMon(dateTime,indexType);//待装
	     Map<String, Object>  order_last_obj2=getWaitMon(lastDateTime,indexType);
		 
	     Map<String, Object>  order_obj3=getCancelListMon(dateTime,indexType);
	     Map<String, Object>  order_last_obj3=getCancelListMon(lastDateTime,indexType); 
		 
		try{
		  //收费环节	
		  if(null!=obj1){
			   bean.setNum1(obj1.get("NUM1") ==null?"0":obj1.get("NUM1"));
			   bean.setAvgTime_num1(obj1.get("AVGTIME_NUM1") ==null?"0":obj1.get("AVGTIME_NUM1"));
		    }else{
		       bean.setNum1("0");
			   bean.setAvgTime_num1("0");   
		    }
		  if(null!=last_obj1){
			   bean.setAvg1(last_obj1.get("NUM1") ==null?"0":last_obj1.get("NUM1"));
			   bean.setAvgTime_lastNum1(last_obj1.get("AVGTIME_NUM1") ==null?"0":last_obj1.get("AVGTIME_NUM1"));
		    }else{
		       bean.setAvg1("0");
               bean.setAvgTime_lastNum1("0");
		    }  
		  //资源配置  
		   if(null != obj2){
			   bean.setNum2(obj2.get("NUM2") ==null?"0":obj2.get("NUM2"));
			   bean.setAvgTime_num2(obj2.get("AVGTIME_NUM2") ==null?"0":obj2.get("AVGTIME_NUM2"));
		    }else{
		       bean.setNum2("0");
			   bean.setAvgTime_num2("0");   
		    }	   
		   if(null != last_obj2){
			   bean.setAvg2(last_obj2.get("NUM2") ==null?"0":last_obj2.get("NUM2"));
			   bean.setAvgTime_lastNum2(last_obj2.get("AVGTIME_NUM2") ==null?"0":last_obj2.get("AVGTIME_NUM2"));
		    }else{
		       bean.setAvg2("0");
               bean.setAvgTime_lastNum2("0");
		    }		 
		   //数据施工
		   if(null != obj3){
			   bean.setNum3(obj3.get("NUM3") ==null?"0":obj3.get("NUM3"));
			   bean.setAvgTime_num3(obj3.get("AVGTIME_NUM3") ==null?"0":obj3.get("AVGTIME_NUM3"));
		    }else{
		       bean.setNum3("0");
			   bean.setAvgTime_num3("0");   
		    }	   
		   if(null != last_obj3){
			   bean.setAvg3(last_obj3.get("NUM3") ==null?"0":last_obj3.get("NUM3"));
			   bean.setAvgTime_lastNum3(last_obj3.get("AVGTIME_NUM3") ==null?"0":last_obj3.get("AVGTIME_NUM3"));
		    }else{
		       bean.setAvg3("0");	
               bean.setAvgTime_lastNum3("0");
		    }			   		   
		   //外线施工环节
		   if(null != obj4){
			   bean.setNum4(obj4.get("NUM4") ==null?"0":obj4.get("NUM4"));
			   bean.setAvgTime_num4(obj4.get("AVGTIME_NUM4") ==null?"0":obj4.get("AVGTIME_NUM4"));
		    }else{
		       bean.setNum4("0");
			   bean.setAvgTime_num4("0");   
		    }	   
		   if(null != last_obj4){
			   bean.setAvg4(last_obj4.get("NUM4") ==null?"0":last_obj4.get("NUM4"));
			   bean.setAvgTime_lastNum4(last_obj4.get("AVGTIME_NUM4") ==null?"0":last_obj4.get("AVGTIME_NUM4"));
		    }else{
		       bean.setAvg4("0");	
               bean.setAvgTime_lastNum4("0");
		    }
		   
		   
		   //缓装单
		   if(null != order_obj1){
			   bean.setOrderExcepiton_num1(order_obj1.get("AVGTIME") ==null?"0":order_obj1.get("AVGTIME"));
			   bean.setOrderExcepiton_num2(order_obj1.get("NUM") ==null?"0":order_obj1.get("NUM"));
		    }else{
		       bean.setOrderExcepiton_num1("0");
			   bean.setOrderExcepiton_num2("0");   
		    }
		   if(null != order_last_obj1){
			   bean.setOrderExcepiton_avg1(order_last_obj1.get("AVGTIME") ==null?"0":order_last_obj1.get("AVGTIME"));
			   bean.setOrderExcepiton_avg2(order_last_obj1.get("NUM") ==null?"0":order_last_obj1.get("NUM"));
		    }else{
		       bean.setOrderExcepiton_avg1("0");
			   bean.setOrderExcepiton_avg2("0");   
		    }
		   //待装单
		  if(null != order_obj2){
			   bean.setOrderExcepiton_num3(order_obj2.get("AVGTIME") ==null?"0":order_obj2.get("AVGTIME"));
			   bean.setOrderExcepiton_num4(order_obj2.get("NUM") ==null?"0":order_obj2.get("NUM"));
		  }else{
		       bean.setOrderExcepiton_num3("0");
			   bean.setOrderExcepiton_num4("0");   
		  }
		 if(null != order_last_obj2){
			   bean.setOrderExcepiton_avg3(order_last_obj2.get("AVGTIME") ==null?"0":order_last_obj2.get("AVGTIME"));
			   bean.setOrderExcepiton_avg4(order_last_obj2.get("NUM") ==null?"0":order_last_obj2.get("NUM"));
		  }else{
		       bean.setOrderExcepiton_avg3("0");
			   bean.setOrderExcepiton_avg4("0");   
		    }
	      //撤单
		  if(null != order_obj3){
			   bean.setOrderExcepiton_num5(order_obj3.get("NUM") ==null?"0":order_obj3.get("NUM"));
		  }else{
		       bean.setOrderExcepiton_num5("0");
		  }
		 if(null != order_last_obj3){
			   bean.setOrderExcepiton_avg5(order_last_obj3.get("NUM") ==null?"0":order_last_obj3.get("NUM"));
		  }else{
		       bean.setOrderExcepiton_avg5("0");
          }
		  
		}catch(Exception e){
			e.printStackTrace();
		}
		return bean;
    }

	//收费环节
	public Map<String, Object> getSFHJMon(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM7,2) as avgTime_num1, round(NUM8*100,2) as num1 "+
				"FROM CS_BROADBAND_MAINT_MON WHERE REGION_ID='0000' AND MONTH_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append("AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
	//资源配置环节 
	public Map<String, Object> getZYPZMon(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM17,2) as avgTime_num2, round(NUM18*100,2) as num2 "+
				"FROM CS_BROADBAND_MAINT_MON WHERE REGION_ID='0000' AND MONTH_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append("AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
	//数据施工环节
	public Map<String, Object> getSJSGMon(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM24,2) as avgTime_num3, round(NUM25*100,2) as num3 "+
				"FROM CS_BROADBAND_MAINT_MON WHERE REGION_ID='0000' AND MONTH_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append("AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
	//外线施工环节     
	public Map<String, Object> getWSGHJMon(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM34+NUM30,2) as avgTime_num4, decode(NUM28,0,0,round(NUM28/NUM29*100,2)) as num4 "+
				"FROM CS_BROADBAND_MAINT_MON WHERE REGION_ID='0000' AND MONTH_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append("AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}

	//缓装库
	public Map<String, Object> getDelayMon(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM72,2) as AVGTIME, round(NUM41*100,2) as NUM "+
		"FROM CS_BROADBAND_MAINT_MON WHERE REGION_ID='0000' AND MONTH_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append("AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
	//待装库
	public Map<String, Object> getWaitMon(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM73,2) as AVGTIME, round(NUM44*100,2) as NUM "+
		"FROM CS_BROADBAND_MAINT_MON WHERE REGION_ID='0000' AND MONTH_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append("AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
	//撤单
	public Map<String, Object> getCancelListMon(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT 0 as AVGTIME, round(NUM38*100,2) as NUM "+
		"FROM CS_BROADBAND_MAINT_MON WHERE REGION_ID='0000' AND MONTH_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append("AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}

	
}
