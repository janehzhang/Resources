
package tydic.portalCommon.implData;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.meta.common.DateUtil;
import tydic.portalCommon.implData.util.DocHelper;
import tydic.portalCommon.implData.util.ImService;


public class ImplDataAction {
	
	ImplDataDAO implDataDAO;

	public void setImplDataDAO(ImplDataDAO implDataDAO) {
		this.implDataDAO = implDataDAO;
	}
	
	  /**
     * 方法：执行数据导入
     * @param data
     * @return boolean
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws FileNotFoundException 
     */
    public boolean importData(Map<String,Object> data) throws FileNotFoundException, DocumentException, IOException{
        String startTime=DateUtil.getDateforStamp(data.get("startDate").toString());
        String endTime=  DateUtil.getDateforStamp(data.get("endDate").toString());
        List<String> timeList= beforeTime(startTime,endTime);
        String method="findDRApp";
   try{
		        BaseDAO.beginTransaction();
	       for (String currTime : timeList){
	    	     implDataDAO.deleteImplData(currTime);
	             String respData=ImService.invokeMethod(method, xml(currTime));//参数
	        	 Thread.sleep(3000);
	            //解释XML
	            System.out.println("=====>:"+respData);
	            Document parseXML = DocHelper.parseXML(respData);
	            //int datasValue= Integer.parseInt(parseXML.selectSingleNode("//datas/@cols").getStringValue()) ;
	            List<Element> list = parseXML.selectNodes("//imresult/datas/messages");
	            Iterator<Element> iter = list.iterator();
	            while (iter.hasNext()) {   
	            	Map<String,Object> map =new HashMap<String,Object>();
	 	            map.put("DAY_ID", currTime);	
	            	Element element = (Element)iter.next();
	            	Attribute cityCode=element.attribute("cvs");
	            	Attribute cityName=element.attribute("city");
	            	map.put("CITY_CODE", cityCode.getValue());
	            	map.put("CITY_NAME", cityName.getValue());
	            	
	                Iterator childIter = element.elementIterator("contents"); 
	                while (childIter.hasNext()) {   
	                	Element childElement = (Element)childIter.next();
	                	
	                	if((childElement.attribute("id").getValue()).equals("100001")){
	                		 Iterator<Element> childIter_2=childElement.elementIterator("types");
	                		  while (childIter_2.hasNext()) {  
	                			  Element childElement_2 = (Element)childIter_2.next();
	                			  
	                			  if((childElement_2.attribute("name").getValue()).equals("咨询")){
	                				  map.put("CONSULT_NUM", childElement_2.getTextTrim());
	                			  }
	                			  if((childElement_2.attribute("name").getValue()).equals("办理")){
	                				  map.put("DEAL_NUM", childElement_2.getTextTrim());
	                			  }
	                			  if((childElement_2.attribute("name").getValue()).equals("投诉")){
	                				  map.put("COMPLAINT_NUM", childElement_2.getTextTrim());
	                			  }
	                			  if((childElement_2.attribute("name").getValue()).equals("报障")){
	                				  map.put("FAULT_NUM", childElement_2.getTextTrim());
	                			  }
	                			  if((childElement_2.attribute("name").getValue()).equals("其他")){
	                				  map.put("OTHER_NUM", childElement_2.getTextTrim());
	                			  }
	                			  if((childElement_2.attribute("name").getValue()).equals("查询")){
	                				  map.put("QUERY_NUM", childElement_2.getTextTrim());
	                			  }
	                			  if((childElement_2.attribute("name").getValue()).equals("自定义")){
	                				  map.put("DEFINE_NUM", childElement_2.getTextTrim());
	                			  }
	                		  }
	                		 
	                	}
	                	
	                	
	                  	if((childElement.attribute("id").getValue()).equals("100002")){
	                  		map.put("ACC_FRIEND_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100003")){
	                  		map.put("NEW_FRIEND_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100004")){
	                  		map.put("MAN_REQUEST_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100005")){
	                  		map.put("MAN_SERVICE_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100006")){
	                  		map.put("ANSWER_ONE_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100007")){
	                  		map.put("ANSWER_FIVE_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100008")){
	                  		map.put("MAN_ANSWER_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100009")){
	                  		map.put("SELF_SERVICE_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100010")){
	                  		map.put("ONLINE_MSG_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100011")){
	                  		map.put("ANSWER_24HOUR_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100012")){
	                  		map.put("DISP_SOLVE_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100013")){
	                  		map.put("REPEAT_SERV_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100014")){
	                  		map.put("SATISFIED_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100015")){
	                  		map.put("ASSESS_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100016")){
	                  		map.put("ONLINE_STFD_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100017")){
	                  		map.put("ONLINE_ASSESS_NUM", childElement.getTextTrim());
	                	}
	                   	
	                }
	            	implDataDAO.insertImportData(map);
	            }
	            
	             
	        }
	         BaseDAO.commit();
	         return true;
         } catch(Exception e){
             Log.error("",e);
             BaseDAO.rollback();
             return false;
         }
   
    }
    
	/**
     * 方法：执行数据导入
     * @param data
     * @return boolean
	 * @throws IOException 
	 * @throws DocumentException 
	 * @throws FileNotFoundException 
     */
    public boolean importCurrData(String currDay) throws FileNotFoundException, DocumentException, IOException{
        String startTime= currDay;
        String endTime=   currDay;
        List<String> timeList= beforeTime(startTime,endTime);
        String method="findDRApp";
        ImplDataDAO implDataDAO = new ImplDataDAO();
   try{
		        BaseDAO.beginTransaction();
	       for (String currTime : timeList){
	    	   implDataDAO.deleteImplData(currTime);
	             String respData=ImService.invokeMethod(method, xml(currTime));//参数
	        	 Thread.sleep(3000);
	            //解释XML
	            Log.debug("=====>:"+respData);
	            Document parseXML = DocHelper.parseXML(respData);
	            //int datasValue= Integer.parseInt(parseXML.selectSingleNode("//datas/@cols").getStringValue()) ;
	            List<Element> list = parseXML.selectNodes("//imresult/datas/messages");
	            Iterator<Element> iter = list.iterator();
	            while (iter.hasNext()) {   
	            	Map<String,Object> map =new HashMap<String,Object>();
	 	            map.put("DAY_ID", currTime);	
	            	Element element = (Element)iter.next();
	            	Attribute cityCode=element.attribute("cvs");
	            	Attribute cityName=element.attribute("city");
	            	map.put("CITY_CODE", cityCode.getValue());
	            	map.put("CITY_NAME", cityName.getValue());
	            	
	                Iterator childIter = element.elementIterator("contents"); 
	                while (childIter.hasNext()) {   
	                	Element childElement = (Element)childIter.next();
	                	
	                	if((childElement.attribute("id").getValue()).equals("100001")){
	                		 Iterator<Element> childIter_2=childElement.elementIterator("types");
	                		  while (childIter_2.hasNext()) {  
	                			  Element childElement_2 = (Element)childIter_2.next();
	                			  
	                			  if((childElement_2.attribute("name").getValue()).equals("咨询")){
	                				  map.put("CONSULT_NUM", childElement_2.getTextTrim());
	                			  }
	                			  if((childElement_2.attribute("name").getValue()).equals("办理")){
	                				  map.put("DEAL_NUM", childElement_2.getTextTrim());
	                			  }
	                			  if((childElement_2.attribute("name").getValue()).equals("投诉")){
	                				  map.put("COMPLAINT_NUM", childElement_2.getTextTrim());
	                			  }
	                			  if((childElement_2.attribute("name").getValue()).equals("报障")){
	                				  map.put("FAULT_NUM", childElement_2.getTextTrim());
	                			  }
	                			  if((childElement_2.attribute("name").getValue()).equals("其他")){
	                				  map.put("OTHER_NUM", childElement_2.getTextTrim());
	                			  }
	                			  if((childElement_2.attribute("name").getValue()).equals("查询")){
	                				  map.put("QUERY_NUM", childElement_2.getTextTrim());
	                			  }
	                			  if((childElement_2.attribute("name").getValue()).equals("自定义")){
	                				  map.put("DEFINE_NUM", childElement_2.getTextTrim());
	                			  }
	                		  }
	                		 
	                	}
	                	
	                	
	                  	if((childElement.attribute("id").getValue()).equals("100002")){
	                  		map.put("ACC_FRIEND_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100003")){
	                  		map.put("NEW_FRIEND_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100004")){
	                  		map.put("MAN_REQUEST_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100005")){
	                  		map.put("MAN_SERVICE_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100006")){
	                  		map.put("ANSWER_ONE_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100007")){
	                  		map.put("ANSWER_FIVE_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100008")){
	                  		map.put("MAN_ANSWER_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100009")){
	                  		map.put("SELF_SERVICE_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100010")){
	                  		map.put("ONLINE_MSG_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100011")){
	                  		map.put("ANSWER_24HOUR_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100012")){
	                  		map.put("DISP_SOLVE_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100013")){
	                  		map.put("REPEAT_SERV_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100014")){
	                  		map.put("SATISFIED_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100015")){
	                  		map.put("ASSESS_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100016")){
	                  		map.put("ONLINE_STFD_NUM", childElement.getTextTrim());
	                	}
	                   	if((childElement.attribute("id").getValue()).equals("100017")){
	                  		map.put("ONLINE_ASSESS_NUM", childElement.getTextTrim());
	                	}
	                   	
	                }
	                implDataDAO.insertImportData(map);
	            }
	            
	             
	        }
	         BaseDAO.commit();
	         return true;
         } catch(Exception e){
             e.printStackTrace();
             BaseDAO.rollback();
             return false;
         }
   
    }   
    
    private String xml(String startTime){
    	String str="<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>\n"+
		"<params>\n"+
			"<head>\n" +
				"<uak>C65CB36AF2C832F5E05C683C8217DCF0</uak>\n" +
			"</head>\n"+
			"<content>\n"+
				"<stime>"+startTime+"</stime>\n"+
			"</content>\n"+
		"</params>\n";
		return str;
	}  
    
	
	/**
	 * 解释返回的数据
	 * 
	 * @param doc
	 * @return
	 */
	public static String parseRespData(Document doc, String elementInfo) {
		String returnCode = getElText(doc, elementInfo);
		return returnCode;
	}
	private static String getElText(Node parentNode, String path) {
		Node node = parentNode.selectSingleNode(path);
		if (node != null) {
			return node.getText();
		} else {
			return null;
		}
	}	
   /**
    * 日期循环 	
    * @param startTime
    * @param endTime
    * @return
    */
   private List<String> beforeTime(String startTime, String endTime) {
		List<String> list = new ArrayList<String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		try {
			start.setTime(format.parse(startTime));
			end.setTime(nextDay(format.parse(endTime)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		while (start.before(end)) {
			list.add(format.format(start.getTime()));
			start.add(Calendar.DAY_OF_MONTH, 1);
		}
		return list;
	}
   
	/**
	 * 
	 * <p>Description 将传入的日期加一天</p>
	 * <p>Copyright Copyright(c)2007</p>
	 * @create time: 2007-3-2 下午03:38:49
	 * @version 1.0
	 * @param Date 被加的时间对象
	 * @modified records:
	 */
	public static Date nextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		return calendar.getTime();
	}
}
