package tydic.reports.channel.newChannel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.commons.beanutils.BeanUtils;

import tydic.frame.common.Log;
import tydic.frame.common.aop.AopHandler;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.yhd.utils.Pager;
import tydic.meta.module.mag.timer.IMetaTimer;

public class AutoCreateTXTFile implements IMetaTimer {
	private NewChannelDao NewChannelDao = new NewChannelDao();
	
	@Override
	public void init() {
	}

	@Override
	public void run(String timerName) {
		// TODO Auto-generated method stub
		Map param=new HashMap<String, String>();
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		String curTime = sdf.format(currentTime);
		
		String excelHeader = "分公司,月份,网龄,服务属性,缴费类型,是否固定信用额度用户,是否零信用额度用户,信用等级,信用额度调当月整类型,是否临时授信,套餐类型,渠道小类,用户状态,停机类型,是否欠费,是否异常用户,半年内是否有信控停机记录,连续停机月数分档,连续欠费月数分档,最长信控停机天数,基本信用额度,基本套餐费PPM,统计当月出账用户数,统计当月出账收入,统计当月代收费用,近3个月月均主叫MOU,近3个月月均上网流量,统计当月临时授信额度,统计当月累计信停次数,统计当月累计信停天数,统计当月信停复机间隔天数,半年累计信停次数,半年累计信停天数,半年信停复机间隔天数";
		int totalCount = (int)NewChannelDao.CreditManageKBDataCount(curTime);
		
		param.put("zoneCode", "0000");
		param.put("dateTime", curTime);
		param.put("excelHeader", excelHeader);
		param.put("fileType", "txt");
		param.put("totalCount", totalCount);
		param.put("excelTitle", "移动后付费用户信用管理宽表");
		param.put("class", "tydic.reports.channel.newChannel.NewChannelAction");
		param.put("classMethod", "getCreditManageKBData");
		
		if(totalCount>0){
			try {
				createFile(param);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * 通过反射调用目标类的目标方法，获取查询到的结果集
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getData(Map<String, Object> param){
		String className= param.get("class").toString();
		String methodName=param.get("classMethod").toString();
		Map<String, Object> data=null;
		try {
			//加载需要被调用的action的二进制文件
				Object obj = Class.forName(className).newInstance();
				Object args[]=new Object[1];
				args[0]=param;
				Method method = obj.getClass().getMethod(methodName,Map.class);
				
				//使用调用aophanler对action类中的dao对象扫描，并注入
				AopHandler handler=new AopHandler(obj);
				data=(Map<String, Object>) handler.invoke(null, method, args);
				
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
		return data;
	}
	
	/**
	 * 获取到map数据集后将数据进行二次处理和封装
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> formatData(Map<String, Object> rsData){
		   List<Map<String,String>> exportData = new ArrayList<Map<String,String>>();
		   String[] headColumn=(String[])rsData.get("headColumn");
		   List<Map<String, Object>> dataColumn = (List<Map<String, Object>>)rsData.get("dataColumn"); 
			   	   
		int k=0;
		for(Map<String, Object> map1: dataColumn){
			  k=0;	   		
			  Map<String, String> rowData = new LinkedHashMap<String, String>();
			  for(String headStr: headColumn){
				  if(headStr.indexOf('_')==-1){
			   		rowData.put(k+"",Convert.toString(map1.get(headStr))+"\t");
			   		k++;
			   	  }
			  }
			  exportData.add(rowData);
			  rowData=null;
	      } 
		return exportData;
	}
	
	/**
	 * 1.必要的参数：前台报表查询的action类名、调用的方法名
	 * 2.指定文件的格式（csv、txt）,文件的标题，查询过滤条件、总条数、生成的文件名
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String createFile(Map<String, Object> param) throws Exception {
		String excelName = param.get("excelTitle").toString() + "_" + param.get("dateTime").toString();//excel文件名
		String excelHeader = param.get("excelHeader").toString();//excel标题行
		String fileType=param.get("fileType").toString().toLowerCase();//导出的文件类型
		String realPath = null;
		BufferedWriter csvFileOutputStream = null;
		try {
		realPath = "E:/" + excelName + "." + fileType;
		int totalCount= param.get("totalCount")==null?3999:Convert.toInt(param.get("totalCount").toString());
		
		LinkedHashMap map = new LinkedHashMap();
		List<Map<String,String>> data=null;
		
		String[][] arr;
		String[] header=excelHeader.split(",");//数组
		int h=1;//少行
		arr=new String[h][header.length];//用两变量实现态定义.
		
		   Pager pager=new Pager();
		   pager.setCurrNum(1);//当前页,从第1页开始
		   pager.setSize(4001); //每次查询多少条
		   pager.setTotalNum(Convert.toInt(totalCount));
		
		//创建标题行
		for(int j=0;j<header.length;j++){
	   	      map.put(j, header[j]);
	    }
		
        File csvFile = new File(realPath);
        csvFile.createNewFile();
        csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(csvFile), "GB2312"), 1024);
   
        // 写入文件头部
        for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator
                .hasNext();) {
            java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator
                    .next();
            if(realPath.endsWith(".csv"))//导出csv格式时候的头部格式
            {
            	 csvFileOutputStream.write("\""+ propertyEntry.getValue().toString() + "\"");
                 if (propertyIterator.hasNext()) 
                 {
                     csvFileOutputStream.write(",");
                 }
            }else//导出txt格式时候的头部格式
            {
            	 csvFileOutputStream.write( propertyEntry.getValue().toString());
                 if (propertyIterator.hasNext()) 
                 {
                     csvFileOutputStream.write("|");
                 }
            }
           
        }
        csvFileOutputStream.newLine();
        String clounm="";
		//循环调用存储过程
		for(int  i=1; i <= pager.getTotalPage(); i++)
		{
			pager.setCurrNum(i);
			param.put("currPageNum", i);
			param.put("pageCount", pager.getSize());
			param.put("pager", pager);
			data=formatData(getData(param));
			  for (Iterator iterator = data.iterator(); iterator.hasNext();) {  
	                Object row = (Object) iterator.next();               
	                for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {  
	                    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();  
	                    try {
	                    	clounm=BeanUtils.getProperty(row, propertyEntry.getKey().toString());
	                    	clounm=clounm==null?"":clounm.replaceAll("\'", "").replaceAll("\"", "").replace("\r", "").replaceAll("\n", "");
	                    	
	                    if(realPath.endsWith(".csv"))
	                    	csvFileOutputStream.write("\""  + clounm.replaceAll("&nbsp;&nbsp;", "") + "\"");  
	                    else
	                    	csvFileOutputStream.write(BeanUtils.getProperty(row, propertyEntry.getKey().toString()).replaceAll("&nbsp;&nbsp;", "").replaceAll("\t", "") );  
	                    
	                   if (propertyIterator.hasNext()) {
	                	   if(realPath.endsWith(".csv"))
	                		   csvFileOutputStream.write(",");
	                	   else
	                		   csvFileOutputStream.write("|");
	                    }  
	                    } catch (Exception e) {
							e.printStackTrace();
						}
	               }  
	                if (iterator.hasNext()) {  
	                   csvFileOutputStream.newLine();  
	                }  
	           }  
			  csvFileOutputStream.newLine();  
			  csvFileOutputStream.flush();
			  data=null;
		}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			 try {  
	              csvFileOutputStream.close();  
	              System.gc();
	            } catch (IOException e) {  
	               e.printStackTrace();
	           }  
		}
		return realPath;
	}
}
