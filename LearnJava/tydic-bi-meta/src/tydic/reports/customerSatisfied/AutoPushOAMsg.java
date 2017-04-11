package tydic.reports.customerSatisfied;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import tydic.frame.common.Log;
import tydic.frame.common.aop.AopHandler;
import tydic.meta.module.mag.timer.IMetaTimer;

public class AutoPushOAMsg implements IMetaTimer{

	private CustomerSatisfiedDAO customerSatisfiedDAO = new CustomerSatisfiedDAO();; 
	@Override
	public void init() {
	}

	@Override
	public void run(String timerName) {
		Map param=new HashMap<String, String>();
		Date currentDay=new Date();

		SimpleDateFormat sdf=	new SimpleDateFormat("yyyy-MM-dd");
		Date lastDay=new Date(currentDay.getTime() - 24 * 60 * 60 * 1000);
		
		//如果前一天是周五周六，那么工单全部放在周一推送，但是不同日期的工单仍然需要分开推送
		if(lastDay.getDay()==5||lastDay.getDay()==6){
			return;
		}
		
		String date=sdf.format(lastDay);
		param.put("zoneCode", "0000");
		param.put("startTime", date);
		param.put("endTime", date);
		param.put("class", "tydic.reports.customerSatisfied.CustomerSatisfiedAction");
		param.put("method", "sendNoSatisDetailData");
		boolean isPush=customerSatisfiedDAO.OAPushCount(new Date());
		if(isPush){
			push(param);
		}
		
		//如果当前天是星期一，则推送周五和周六的工单
		if(lastDay.getDay()==0){
			//推送前两天的工单
			isPush=customerSatisfiedDAO.OAPushCount(new Date(new Date().getTime()-24 * 60 * 60 * 1000));
			param.put("startTime", sdf.format(new Date(new Date().getTime()-2*24 * 60 * 60 * 1000)));
			param.put("endTime", sdf.format(new Date(new Date().getTime()-2*24 * 60 * 60 * 1000)));
			if(isPush) push(param);
			
			//推送前三天的工单
			isPush=customerSatisfiedDAO.OAPushCount(new Date(new Date().getTime()-2*24 * 60 * 60 * 1000));
			param.put("startTime", sdf.format(new Date(new Date().getTime()-3*24 * 60 * 60 * 1000)));
			param.put("endTime", sdf.format(new Date(new Date().getTime()-3*24 * 60 * 60 * 1000)));
			if(isPush)  push(param);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void push(Map<String, Object> param){
		String className= param.get("class").toString();
		String methodName=param.get("method").toString();
		try {
			//加载需要被调用的action的二进制文件
				Object obj = Class.forName(className).newInstance();
				Object args[]=new Object[1];
				args[0]=param;
				Method method = obj.getClass().getMethod(methodName,Map.class);
				
				//使用调用aophanler对action类中的dao对象扫描，并注入
				AopHandler handler=new AopHandler(obj);
				handler.invoke(null, method, args);
				
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
				Log.error("传入的参数类型不匹配", e);
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				Log.error("无效的方法名"+className+"."+methodName, e);
			}
			
		 catch (ClassNotFoundException e) {
			Log.error("无效的类名"+className, e);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	

	
	    }
