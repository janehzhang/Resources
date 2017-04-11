<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCellStyle"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFFont"%>
<%@ page import="org.apache.poi.hssf.util.*"%>
<%@ page import="tydic.portalCommon.coreLink.util.*"%>
<%@ page import="tydic.portalCommon.util.CSVUtils"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="tydic.meta.common.yhd.utils.Pager,tydic.frame.common.utils.Convert"%>
<%@ page import="tydic.reports.channel.allBusiness.AllChannelAction"%>

<iframe id="hiddenStream" name="hiddenStream" width="0" height="0"  frameborder="0" src=""></iframe>
<%
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
try{
	String rootPath=request.getContextPath();
	String excelName = new String(request.getParameter("excelTitle").getBytes("ISO-8859-1"), "UTF-8");
    String condition = new String(request.getParameter("excelCondition").getBytes("ISO-8859-1"), "UTF-8");
	String excelHeader = new String(request.getParameter("excelHeader").getBytes("ISO-8859-1"), "UTF-8");
	String realPath = request.getRealPath("/upload/template/"+ excelName + ".csv");
	String totalCount= request.getParameter("totalCount");
	String startTime=    request.getParameter("startTime");
	String endTime=    request.getParameter("endTime");
	String zoneCode=    request.getParameter("zoneCode");
	String servId=    request.getParameter("servId");
	String channelId= request.getParameter("channelId");
	
	
	
	   String[][] arr;
	   String[] header=excelHeader.split(",");//数组
	   int h=1;//少行
	   arr=new String[h][header.length];//用两变量实现态定义.
	   
	   List data = new ArrayList();
	
	   Map<String, Object> paramMap=new HashMap<String,Object>();
	   paramMap.put("startTime",startTime);
	   paramMap.put("endTime",endTime);
	   paramMap.put("zoneCode",zoneCode);
	   paramMap.put("channelId",channelId);
	   paramMap.put("servId",servId);
	   
	   Pager pager = Pager.getInstance();
	   pager.setCurrNum(1);//当前页,从第1页开始
	   pager.setSize(2000); //每次查询多少条
	   pager.setTotalNum(Convert.toInt(totalCount));
	 
	   AllChannelAction  action = new AllChannelAction();
	   List<Map<String, Object>> dataColumn=null;
	   LinkedHashMap map = new LinkedHashMap();
	   List exportData = new ArrayList<Map>();
	  
	   for(int j=0;j<header.length;j++){
	   	      map.put(j, header[j]);
	   }
	   for(int  i=1; i <= pager.getTotalPage(); i++)
       {
   	       pager.setCurrNum(i);
   	       paramMap.put("page",pager);
	   	   Map<String, Object>  resultMap=action.expIndexData(paramMap);
	   	   String[] headColumn=(String[])resultMap.get("headColumn");
	   	   dataColumn=(List<Map<String, Object>>)resultMap.get("dataColumn"); 
	   	   
	   	   for(Map<String, Object> map1: dataColumn){
	   		int k=0;	   		
	   		Map rowData = new LinkedHashMap<String, String>();
	   		   for(String headStr: headColumn){
	   		   if(headStr.indexOf('_')==-1){
	   			rowData.put(k+"",Convert.toString(map1.get(headStr)).replaceAll("&nbsp;&nbsp;", ""));
	   			k++;
	   		   }
	   		   }
	   		   exportData.add(rowData);
	   	   } 
	   	   
       }
       CSVUtils.createCSVFile(exportData, map, realPath);
	%>
	<SCRIPT LANGUAGE="JavaScript">
  	parent.dhx.closeProgress();
  	var path="<%=rootPath %>/portalCommon/module/procedure/impExcel/iframe/outStreamIframeCsv.jsp";
      document.getElementById("hiddenStream").src=path+"?excelName=<%=excelName %>";
  </SCRIPT>
  <% 
}
catch(Exception e)
{
	 %>	
	   <SCRIPT LANGUAGE="JavaScript">
	      alert("导出文件出错,请重试！");
	   	parent.dhx.closeProgress();
	  </SCRIPT>
	  <% 	   
	   e.printStackTrace();
}
%>