<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="tydic.portalCommon.coreLink.util.*"%>
<%
String excelName = new String(request.getParameter("excelName").getBytes("ISO-8859-1"),  "UTF-8");
//System.out.print(excelName);
String realPath =  request.getRealPath("/upload/template/"+ excelName + ".txt");
try{
	TxtUtil.returnTxt(response, realPath);
	out.clear();
	out = pageContext.pushBody();
 }catch(Exception e)
 {
   e.printStackTrace();	 
 }
%>