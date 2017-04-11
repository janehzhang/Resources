package tydic.portalCommon.util;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import tydic.frame.SystemVariable;
import tydic.frame.common.Log;

public class SendDataThread extends Thread {
	
	private String methodName;
	private String xml;
	private String  url;
	private String  namespace;
	public SendDataThread(String methodName, String xml, String url, String namespace) {
		this.methodName = methodName;
		this.xml = xml;
		this.url = url;
		this.namespace = namespace;
	}
    public void run() {
		Service service = new Service();
		Call call = null;
		String str = null;
		try {
			call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(url));
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(namespace+"/"+methodName);
			call.setOperationName(new QName(namespace, methodName));
			call.addParameter("XmlData", org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN); 
			// 设置该方法的返回值
			call.setReturnType(XMLType.XSD_STRING);
			Log.debug(xml);
			str = (String) call.invoke(new Object[]{xml});
			Log.debug(str);
	
		} catch (Exception e) {
			e.printStackTrace();
			
		}
   }
	
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
}
