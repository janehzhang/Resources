<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="tydic.portalCommon.coreLink.util.*"%>
<%
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("GBK");
String excelName = new String(request.getParameter("excelName").getBytes("ISO-8859-1"),  "GBK");
String realPath =  request.getRealPath("/upload/template/"+ excelName + ".csv");
try{
	TxtUtil.returnTxt(response, realPath);
	out.clear();
	out = pageContext.pushBody();
 }catch(Exception e)
 {
   e.printStackTrace();	 
 }
%>