package tydic.portalCommon.implData.util;

import java.net.URL;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import tydic.frame.common.Log;


public class ImService 
{
	
	public static final String WebServiceUrlCXF ="http://esb.imp.ct10000.com:8820/IMInterfaceCXF/services/imDataReportService.ws";
	
	/**
	 * @param method
	 *            方法
	 * @param xml
	 *            数据
	 * @param status
	 *            接入服务
	 */
	public static String invokeMethod(String method, String xml) {
		Service service = new Service();
		Call call = null;
		String str = null;
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new URL(WebServiceUrlCXF));
			call.setOperationName(method);
			Object[] data = new Object[1];
			data[0] = xml;
		Log.debug(xml);	
			str = (String) call.invoke(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return str;
	}
	
	public static void main(String[] args) {
		String method="findDRApp";
		System.out.println("================>"+invokeMethod(method,xml()));
	}	
   public static String xml(){
		String str="<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>"+
		"<params>"+
			"<head>" +
				"<uak>C65CB36AF2C832F5E05C683C8217DCF0</uak>" +
			"</head>"+
			"<content>"+
				"<stime>2012-09-01</stime>"+
			"</content>"+
		"</params>";
		return str;
	}
}
