package tydic.portalCommon.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.dom4j.Document;
import org.dom4j.Element;

import tydic.frame.SystemVariable;
import tydic.frame.common.Log;

public class AccCheckedItemService implements IAccCheckedItemService {
	public static  String  methodName="queryUnifyStaffInfo";
	private String url = SystemVariable.getString("AccCheckedServiceUrl","");
	private String namespace = SystemVariable.getString("AccCheckedNamespace","");
	
	@Override
	public Map<String,Object> createAccCheckedItem(AccCheckedItemBean bean) {
		// TODO Auto-generated method stub
		Map<String,Object> RspXmlMap = new HashMap<String,Object>();
		String AccStr = AccChecked(bean);
		if(!AccStr.equals("")){
			try {
				//加载xml字符串
				Document document=org.dom4j.DocumentHelper.parseText(AccStr);
				
				//获取根节点
				Element ContractRoot=document.getRootElement();
				
				for (Iterator<Element> itemRoot = ContractRoot.elementIterator(); itemRoot.hasNext();) {
					// 得到叶子节点
					Element itemContRoot = itemRoot.next();
					// 遍历遍历TcpCont下的所有节点
					for (Iterator<Element> itemInfo = itemContRoot.elementIterator(); itemInfo.hasNext();) {
						// 得到叶子节点下的所有节点					
						Element info = itemInfo.next();			
						RspXmlMap.put(info.getName(), info.getText());
					}
				}
				//System.out.println(RspXmlMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		return RspXmlMap;
	}
	
	public String AccChecked(AccCheckedItemBean bean){
		String xml = getCreateAccCheckedItemXml(bean);
		Service service = new Service();
		Call call = null;
		String str = "";
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
		return str;
	}
			
/*	public static void main(String[] args) {
		//获取当前日期			
		Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 可以方便地修改日期格式
		String createTime=sdf.format(c.getTime());
		
		AccCheckedItemBean bean = new AccCheckedItemBean();
		bean.setReqCode("CSAS" + createTime);
    	bean.setActionCode("1");
    	bean.setSysCode("CSAS");
    	bean.setSysPwd("3C731E9ED83B6DA51C032C8B5148D70CAC63DBD6");
    	bean.setServiceCode("queryUnifyStaffInfo");
    	//bean.setCityId("");
    	bean.setReqTime(createTime);
    	bean.setVersion("3.0");
    	bean.setSUBSTAFF_ID("zhengsh");
    	bean.setSTAFF_PASSWD("csas@321");
    	bean.setIP_ADDR("132.97.220.252");
    	bean.setNET_IP("132.121.165.45");
    	bean.setQRY_TYPE("3");
    	bean.setACT_CODE("LOGIN");
    	bean.setAD_SYN("1");
    	
    	IAccCheckedItemService iacis=new AccCheckedItemService();
    	iacis.createAccCheckedItem(bean);
    	
    	//System.out.println(getCreateAccCheckedItemXml(bean));
	}*/
	
	private static String getCreateAccCheckedItemXml(AccCheckedItemBean bean) {
		// TODO Auto-generated method stub
		String AccStr = "<ContractRoot>\n" +
								"<TcpCont>\n" +
									"<ReqCode><![CDATA[" + bean.getReqCode() + "]]></ReqCode>\n" +
									"<ActionCode><![CDATA[" + bean.getActionCode() + "]]></ActionCode>\n" +
									"<SysCode><![CDATA[" + bean.getSysCode() + "]]></SysCode>\n" +
									"<SysPwd><![CDATA[" + bean.getSysPwd() + "]]></SysPwd>\n" +
									"<ServiceCode><![CDATA[" + bean.getServiceCode() + "]]></ServiceCode>\n" +
									"<CityId><![CDATA[" + bean.getCityId() + "]]></CityId>\n" +
									"<ReqTime><![CDATA[" + bean.getReqTime() + "]]></ReqTime>\n" +
									"<Version><![CDATA[" + bean.getVersion() + "]]></Version>\n" +
									"<bak1></bak1>\n" +
									"<bak2></bak2>\n" +
								"</TcpCont>\n" +
								"<SvcCont>\n" +
									"<SOO type='VERIFY_PWD'>\n" +
										"<STAFF_INFO>\n" +
											"<SUB_STAFF>\n" +
												"<STAFF_ID><![CDATA[" + bean.getSUBSTAFF_ID() + "]]></STAFF_ID>\n" +
											"</SUB_STAFF>\n" +
											"<STAFF_PASSWD><![CDATA[" + bean.getSTAFF_PASSWD() + "]]></STAFF_PASSWD>\n" +
											"<STAFF_ID><![CDATA["+ bean.getSTAFF_ID() +"]]></STAFF_ID>\n" +
										"</STAFF_INFO>\n" +
									"<IP_ADDR>\n" +
										"<IP_ADDR><![CDATA[" + bean.getIP_ADDR() + "]]></IP_ADDR>\n" +
										"<NET_IP><![CDATA[" + bean.getNET_IP() + "]]></NET_IP>\n" +
									"</IP_ADDR>\n" +
									"</SOO>\n" +
									"<SOO type= 'QUERY_STAFFINFO'>\n" +
										"<PUB_QRY>\n" +
											"<QRY_TYPE><![CDATA[" + bean.getQRY_TYPE() + "]]></QRY_TYPE>\n" +
											"<ACT_CODE><![CDATA[" + bean.getACT_CODE() + "]]></ACT_CODE>\n" +
											"<AD_SYN><![CDATA[" + bean.getAD_SYN() + "]]></AD_SYN>\n" +
										"</PUB_QRY>\n" +
									"</SOO>\n" +
								"</SvcCont>\n" +
						"</ContractRoot>";
		Log.info(AccStr);
		return AccStr;
	}

}
