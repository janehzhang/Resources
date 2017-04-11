 <%@ page language="java"  pageEncoding="UTF-8" %>
<%@ page  import="java.net.*" import="tydic.meta.rpt.*"import="tydic.meta.common.*"import="java.util.*"%>
<%@page import="tydic.frame.jdbc.DataAccess"%>
<%@page import="tydic.frame.DataSourceManager"%>
<%@page import="tydic.frame.SystemVariable"%> 
<% 
   	response.setContentType("Application/msexcel"); 
 	String reportId=request.getParameter("reportId_input");
 	String reportConfig=request.getParameter("reportConfig_input");
 	String reportParams=request.getParameter("reportParams_input");
 	String reportDownTab=request.getParameter("reportDownTab_input");
 	String reportTitle=request.getParameter("reportTitle_input");
 	reportId=Common.parseChinese(reportId);
 	reportConfig=Common.parseChinese(reportConfig);
 	reportParams=Common.parseChinese(reportParams);
 	reportDownTab=Common.parseChinese(reportDownTab);
 	reportTitle=Common.parseChinese(reportTitle);
 	int dowLoadType=0;
 	if(reportId.split(",").length==2)
 	{
 		dowLoadType=Integer.parseInt(reportId.split(",")[1]);
 	}
 	reportId=reportId.split(",")[0];
 	if(reportTitle.equals(""))
 	{
 		reportTitle="报表下载";
 	}
  	if(dowLoadType==0)
  	{
  		response.setCharacterEncoding("GB2312");
  		response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(reportTitle+".csv","UTF-8")+";charset=GB2312");
  	}
  	else if(dowLoadType==1)
  	{
  		response.setCharacterEncoding("GB2312");
  		response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(reportTitle+".xls","UTF-8")+";charset=UTF-8");
  	}
	System.out.println("报表["+reportTitle+"]下载类型："+dowLoadType);
  	
 	try
 	{
 		ReportServerDisplayAction serv=new ReportServerDisplayAction();
 		String[] fileName=serv.reportDownLoad(reportId, reportConfig, reportParams, reportDownTab,response);
 	}
 	catch(Exception ex)
 	{
 		ex.printStackTrace();
 		response.getWriter().print(ex.getMessage());
 	}
	response.getWriter().close();
%>
