package tydic.portalCommon.serviceControl;

import java.util.Map;

import tydic.meta.common.MetaBaseDAO;
/**
 * 
 * @author quanxia
 *
 */
public class StepFaultDAO extends MetaBaseDAO{
    /**
     * 获得天的数据
     * @param dateTime
     * @return
     */
	public StepFaultBean getStepFaultDay(String dateTime,String indexType){
		System.out.print(dateTime);
		StepFaultBean bean =new StepFaultBean();
		 
		 Map<String, Object> obj1=getFaultComplainDay(dateTime,indexType);//故障申告
		 Map<String, Object> last_obj1=getLast_FaultComplainDay(StringUtil.getLastMon(dateTime.substring(0, dateTime.length()-2)),indexType);//上月平均
		 
		 Map<String, Object> obj2=getToDealDay(dateTime,indexType);//预处理
		 Map<String, Object> last_obj2=getLast_ToDealDay(StringUtil.getLastMon(dateTime.substring(0, dateTime.length()-2)),indexType);
		 
		 Map<String, Object> obj3=getDispachListDay(dateTime,indexType);//派单
		 Map<String, Object> last_obj3=getLast_DispachListDay(StringUtil.getLastMon(dateTime.substring(0, dateTime.length()-2)),indexType);
		 
		 Map<String, Object> obj4=getOverTimeDay(dateTime,indexType);//超时
		 Map<String, Object> last_obj4=getLast_OverTimeDay(StringUtil.getLastMon(dateTime.substring(0, dateTime.length()-2)),indexType);
		 
	     Map<String, Object> obj5=getHangUpDay(dateTime,indexType);//挂起
	     Map<String, Object> last_obj5=getLast_HangUpDay(StringUtil.getLastMon(dateTime.substring(0, dateTime.length()-2)),indexType);
		 
	     Map<String, Object> obj6=getReturnDay(dateTime,indexType);//回访回退
	     Map<String, Object>  last_obj6=getLast_ReturnDay(StringUtil.getLastMon(dateTime.substring(0, dateTime.length()-2)),indexType);
	     
		try{
		  //故障申告
		  if(null!=obj1){
			   bean.setCmpl_num(obj1.get("CMPL_NUM") ==null?"0":obj1.get("CMPL_NUM"));
		    }else{
		       bean.setCmpl_num("0");
		    } 
		  if(null != last_obj1){
			   bean.setCmpl_last_num(last_obj1.get("CMPL_LAST_NUM") ==null?"0":last_obj1.get("CMPL_LAST_NUM"));
		    }else{
			   bean.setCmpl_last_num("0");   
		    }
		  //预处理
		   if(null != obj2){
			   bean.setDeal_num(obj2.get("DEAL_NUM") ==null?"0":obj2.get("DEAL_NUM"));
		    }else{
		       bean.setDeal_num("0");
		    }	
		   if(null != last_obj2){
			   bean.setDeal_last_num(last_obj2.get("DEAL_LAST_NUM") ==null?"0":last_obj2.get("DEAL_LAST_NUM"));
		    }else{
			   bean.setDeal_last_num("0");   
		    }
		   //派单
		   if(null != obj3){
			   bean.setDispach_num(obj3.get("DISPACH_NUM") ==null?"0":obj3.get("DISPACH_NUM"));
		    }else{
		       bean.setDispach_num("0");
		    }	   
		   if(null != last_obj3){
			   bean.setDispach_last_num(last_obj3.get("DISPACH_LAST_NUM") ==null?"0":last_obj3.get("DISPACH_LAST_NUM"));
		    }else{
			   bean.setDispach_last_num("0");   
		    }			   		   
		   //超时
		   if(null != obj4){
			   bean.setOvertime_num(obj4.get("OVERTIME_NUM") ==null?"0":obj4.get("OVERTIME_NUM"));
		    }else{
		       bean.setOvertime_num("0");
		    }	   
		   if(null != last_obj4){
			   bean.setOvertime_last_num(last_obj4.get("OVERTIME_LAST_NUM") ==null?"0":last_obj4.get("OVERTIME_LAST_NUM"));
		    }else{
			   bean.setOvertime_last_num("0");   
		    }
		   
		   //挂起
		   if(null != obj5){
			   bean.setHangup_num(obj5.get("HANGUP_NUM") ==null?"0":obj5.get("HANGUP_NUM"));
		    }else{
		       bean.setHangup_num("0");
		    }
		   if(null != last_obj5){
			   bean.setHangup_last_num(last_obj5.get("HANGUP_LAST_NUM") ==null?"0":last_obj5.get("HANGUP_LAST_NUM"));
		    }else{
		    	bean.setHangup_last_num("0");
		    }
		   //回访回退
		  if(null != obj6){
			   bean.setReturn_num(obj6.get("RETURN_NUM") ==null?"0":obj6.get("RETURN_NUM"));
		  }else{
		       bean.setReturn_num("0");
		  }
		 if(null != last_obj6){
			   bean.setReturn_last_num(last_obj6.get("RETURN_LAST_NUM") ==null?"0":last_obj6.get("RETURN_LAST_NUM"));
		  }else{
		       bean.setReturn_last_num("0");
		    }
		}catch(Exception e){
			e.printStackTrace();
		}
		return bean;
    } 
	//故障申告
	public Map<String, Object> getFaultComplainDay(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT SUM(NUM46) as cmpl_num "+
				"FROM CS_BROADBAND_MAINT_DAY WHERE REGION_ID='0000' AND DAY_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
	
	public Map<String, Object> getLast_FaultComplainDay(Integer dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("select round(sum(NUM46)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),0) cmpl_last_num "
                     +" from CS_BROADBAND_MAINT_DAY where substr(day_id,1,6)='"+dateTime+"' and REGION_ID='0000'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
	    return getDataAccess().queryForMap(sql.toString());
	}	
	
	//预处理
	public Map<String, Object> getToDealDay(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM49*100,2) as deal_num "+
		"FROM CS_BROADBAND_MAINT_DAY WHERE REGION_ID='0000' AND DAY_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
    
	public Map<String, Object> getLast_ToDealDay(Integer dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("select round(sum(NUM49)*100/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),2) deal_last_num " 
                +" from CS_BROADBAND_MAINT_DAY where substr(day_id,1,6)='"+dateTime+"' and REGION_ID='0000'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
	    return getDataAccess().queryForMap(sql.toString());
   }
	
	
   //派单
	public Map<String, Object> getDispachListDay(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT SUM(NUM48) as dispach_num "+
				"FROM CS_BROADBAND_MAINT_DAY WHERE REGION_ID='0000' AND DAY_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
    
	public Map<String, Object> getLast_DispachListDay(Integer dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("select round(sum(NUM48)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),0) dispach_last_num"
                +" from CS_BROADBAND_MAINT_DAY where substr(day_id,1,6)='"+dateTime+"' and REGION_ID='0000'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
	    return getDataAccess().queryForMap(sql.toString());
   }
	
	 //超时
	public Map<String, Object> getOverTimeDay(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT SUM(NUM51) as overtime_num "+
		"FROM CS_BROADBAND_MAINT_DAY WHERE REGION_ID='0000' AND DAY_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
    
	public Map<String, Object> getLast_OverTimeDay(Integer dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("select round(sum(NUM51)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),0) overtime_last_num "
                     +" from CS_BROADBAND_MAINT_DAY where substr(day_id,1,6)='"+dateTime+"' and REGION_ID='0000'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
	    return getDataAccess().queryForMap(sql.toString());
   }
	
	//挂起
	public Map<String, Object> getHangUpDay(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT SUM(NUM52) as hangup_num "+
		" FROM CS_BROADBAND_MAINT_DAY WHERE REGION_ID='0000' AND DAY_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
    
	public Map<String, Object> getLast_HangUpDay(Integer dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("select round(sum(NUM52)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),0) hangup_last_num "
                     +" from CS_BROADBAND_MAINT_DAY where substr(day_id,1,6)='"+dateTime+"' and REGION_ID='0000'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
	    return getDataAccess().queryForMap(sql.toString());
   }
	
	//回访回退
	public Map<String, Object> getReturnDay(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT SUM(NUM53) as return_num "+
		"FROM CS_BROADBAND_MAINT_DAY WHERE REGION_ID='0000' AND DAY_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
    
	public Map<String, Object> getLast_ReturnDay(Integer dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("select round(sum(NUM53)/(add_months(to_date('"+dateTime+"','yyyymm'),1) - to_date('"+dateTime+"','yyyymm')),0) return_last_num "
                +" from CS_BROADBAND_MAINT_DAY where substr(day_id,1,6)='"+dateTime+"' and REGION_ID='0000'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
	    return getDataAccess().queryForMap(sql.toString());
   }
	/**
	 * 获得月的数据
	 * @param dateTime
	 * @return
	 */
	public StepFaultBean getStepFaultMon(String dateTime,String indexType){
		 StepFaultBean bean =new StepFaultBean();
		 String lastDateTime=String.valueOf(StringUtil.getLastMon(dateTime));
		
		 Map<String, Object> obj1=getFaultComplainMon(dateTime,indexType);//故障申告
		 Map<String, Object> last_obj1=getFaultComplainMon(lastDateTime,indexType);
		 
		 Map<String, Object> obj2=getToDealMon(dateTime,indexType);//预处理
		 Map<String, Object> last_obj2=getToDealMon(lastDateTime,indexType);
		 
		 Map<String, Object> obj3=getDispachListMon(dateTime,indexType);//派单
		 Map<String, Object> last_obj3=getDispachListMon(lastDateTime,indexType);
		 
		 Map<String, Object> obj4=getOverTimeMon(dateTime,indexType);//超时
		 Map<String, Object> last_obj4=getOverTimeMon(lastDateTime,indexType);
		 
		 Map<String, Object> obj5=getHangUpMon(dateTime,indexType);//挂起
		 Map<String, Object> last_obj5=getHangUpMon(lastDateTime,indexType);
		 
		 Map<String, Object> obj6=getReturnMon(dateTime,indexType);//回访回退
		 Map<String, Object> last_obj6=getReturnMon(lastDateTime,indexType);
 
		 try{
			  //故障申告
			  if(null!=obj1){
				   bean.setCmpl_num(obj1.get("CMPL_NUM") ==null?"0":obj1.get("CMPL_NUM"));
			    }else{
			       bean.setCmpl_num("0");
			    } 
			  if(null != last_obj1){
				   bean.setCmpl_last_num(last_obj1.get("CMPL_NUM") ==null?"0":last_obj1.get("CMPL_NUM"));
			    }else{
				   bean.setCmpl_last_num("0");   
			    }
			  //预处理
			   if(null != obj2){
				   bean.setDeal_num(obj2.get("DEAL_NUM") ==null?"0":obj2.get("DEAL_NUM"));
			    }else{
			       bean.setDeal_num("0");
			    }	
			   if(null != last_obj2){
				   bean.setDeal_last_num(last_obj2.get("DEAL_NUM") ==null?"0":last_obj2.get("DEAL_NUM"));
			    }else{
				   bean.setDeal_last_num("0");   
			    }
			   //派单
			   if(null != obj3){
				   bean.setDispach_num(obj3.get("DISPACH_NUM") ==null?"0":obj3.get("DISPACH_NUM"));
			    }else{
			       bean.setDispach_num("0");
			    }	   
			   if(null != last_obj3){
				   bean.setDispach_last_num(last_obj3.get("DISPACH_NUM") ==null?"0":last_obj3.get("DISPACH_NUM"));
			    }else{
				   bean.setDispach_last_num("0");   
			    }			   		   
			   //超时
			   if(null != obj4){
				   bean.setOvertime_num(obj4.get("OVERTIME_NUM") ==null?"0":obj4.get("OVERTIME_NUM"));
			    }else{
			       bean.setOvertime_num("0");
			    }	   
			   if(null != last_obj4){
				   bean.setOvertime_last_num(last_obj4.get("OVERTIME_NUM") ==null?"0":last_obj4.get("OVERTIME_NUM"));
			    }else{
				   bean.setOvertime_last_num("0");   
			    }
			   
			   //挂起
			   if(null != obj5){
				   bean.setHangup_num(obj5.get("HANGUP_NUM") ==null?"0":obj5.get("HANGUP_NUM"));
			    }else{
			       bean.setHangup_num("0");
			    }
			   if(null != last_obj5){
				   bean.setHangup_last_num(last_obj5.get("HANGUP_NUM") ==null?"0":last_obj5.get("HANGUP_NUM"));
			    }else{
			    	bean.setHangup_last_num("0");
			    }
			   //回访回退
			  if(null != obj6){
				   bean.setReturn_num(obj6.get("RETURN_NUM") ==null?"0":obj6.get("RETURN_NUM"));
			  }else{
			       bean.setReturn_num("0");
			  }
			 if(null != last_obj6){
				   bean.setReturn_last_num(last_obj6.get("RETURN_NUM") ==null?"0":last_obj6.get("RETURN_NUM"));
			  }else{
			       bean.setReturn_last_num("0");
			    }
			}catch(Exception e){
				e.printStackTrace();
			}
		return bean;
    }

	//故障申告
	public Map<String, Object> getFaultComplainMon(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT decode(sum(NUM1),0,0,round(sum(NUM46)/sum(NUM1),2)) as cmpl_num "+
				"FROM CS_BROADBAND_MAINT_MON WHERE REGION_ID='0000' AND MONTH_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
	//预处理
	public Map<String, Object> getToDealMon(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM47*100,2) as deal_num "+
				"FROM CS_BROADBAND_MAINT_MON WHERE REGION_ID='0000' AND MONTH_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
	//派单
	public Map<String, Object> getDispachListMon(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT sum(0) as dispach_num "+
				"FROM CS_BROADBAND_MAINT_MON WHERE REGION_ID='0000' AND MONTH_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
	//超时
	public Map<String, Object> getOverTimeMon(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT decode(sum(NUM46),0,0,round(sum(NUM51+NUM52+NUM53)/sum(NUM46)*100,2)) as overtime_num "+
				"FROM CS_BROADBAND_MAINT_MON WHERE REGION_ID='0000' AND MONTH_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}

	//挂起
	public Map<String, Object> getHangUpMon (String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM48*100,2) as hangup_num "+
				"FROM CS_BROADBAND_MAINT_MON WHERE REGION_ID='0000' AND MONTH_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
	//回访退回
	public Map<String, Object> getReturnMon(String dateTime,String indexType) {
		StringBuffer sql=new StringBuffer("SELECT round(NUM50*100,2) as return_num "+
				"FROM CS_BROADBAND_MAINT_MON WHERE REGION_ID='0000' AND MONTH_ID='"+dateTime+"'");
		if(indexType!=null){
			sql.append(" AND IS_FTTOH='"+indexType+"'");
		}
		return getDataAccess().queryForMap(sql.toString());
	}
}
