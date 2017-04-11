package tydic.reports.CommonUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tydic.frame.common.Log;
import tydic.frame.common.aop.AopHandler;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.yhd.utils.Pager;

import com.sun.org.apache.commons.beanutils.BeanUtils;

public class DownLoadUtil {
	
	/**
	 * 通过request对象获取用户所有的请求参数
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getParameters(HttpServletRequest request){
		
		Map<String, Object> paramMap=new HashMap<String,Object>();
	    Map params=request.getParameterMap();
		String key="";
		Object value="";
		String[] str=new String[1];
		try {
		Set keySet=params.entrySet();
		for(Iterator itr=keySet.iterator();itr.hasNext();){
			Map.Entry<String, Object> me=(Map.Entry<String, Object>)itr.next();
			key=me.getKey();
			str=(String[])me.getValue();
			paramMap.put(key,value==null?"":new String(str[0].getBytes("ISO-8859-1"), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return paramMap;
	}
	
	/**
	 * 通过反射调用目标类的目标方法，获取查询到的结果集
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getData(Map<String, Object> param){
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
	public static List<Map<String,String>> formatData(Map<String, Object> rsData){
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
	public static String createFile(Map<String, Object> param) throws Exception {
		int pageSize = 5000;
		String excelName = param.get("excelTitle").toString()+"_"+System.currentTimeMillis();//excel文件名
		String excelHeader = param.get("excelHeader").toString();//excel标题行
		String fileType=param.get("fileType").toString().toLowerCase();//导出的文件类型
		String realPath = null;
		BufferedWriter csvFileOutputStream = null;
		try {
		realPath =new File(new File("").getCanonicalPath()).getParent()+"/webapps/tydic-bi-meta/upload/template/"+ excelName + "."+fileType;
		int totalCount= param.get("totalCount")==null?(pageSize-1):Convert.toInt(param.get("totalCount").toString());
		
		//根据数据量判断每次的导出量
		if(totalCount>=100000)
			pageSize = 10000;
		
		LinkedHashMap map = new LinkedHashMap();
		List<Map<String,String>> data=null;
		
		String[][] arr;
		String[] header=excelHeader.split(",");//数组
		int h=1;//少行
		arr=new String[h][header.length];//用两变量实现态定义.
		
		   Pager pager=new Pager();
		   pager.setCurrNum(1);//当前页,从第1页开始
		   pager.setSize(pageSize+1); //每次查询多少条
		   pager.setTotalNum(Convert.toInt(totalCount));
		
		//创建标题行
		for(int j=0;j<header.length;j++){
	   	      map.put(j, header[j]);
	    }
		
        File csvFile = new File(realPath);
        csvFile.createNewFile();
        csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(csvFile), "GBK"), 1024);
   
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
	
	/**
	 * 向用户浏览器输出IO
	 * @param path
	 */
	public static void responseFile(HttpServletResponse response,String path){
		String filename = path.substring(path.lastIndexOf("/") + 1);  
		 try {
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));  
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}  
	       
	     InputStream in = null;  
	     OutputStream out = null;  
	         
	     try {  
          in = new FileInputStream(path);  
         int len = 0;  
           byte[] buffer = new byte[1024*10];  
          out = response.getOutputStream();  
      while((len = in.read(buffer)) > 0) {  
             out.write(buffer,0,len);  
            // out.flush();
          }  
	            
       }catch(Exception e) {
	            throw new RuntimeException(e);  
	      }finally {  
          if(in != null) {  
	               try {  
               in.close();  
	               }catch(Exception e) {  
	                  e.printStackTrace();
             }  
	                  
          }  

}
	}

}
