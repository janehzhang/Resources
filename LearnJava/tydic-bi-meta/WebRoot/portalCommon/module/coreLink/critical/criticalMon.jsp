<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date,tydic.portalCommon.coreLink.critical.CriticalMonAction" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
  CriticalMonAction  cma=new CriticalMonAction();
  String newMonth=(String)cma.getNewMonth().substring(0,7);
  String[] months=DateUtil.getAddMonths(16,newMonth);
%>
<head>
    <title>临界值报表</title>
    <%@include file="../../../public/head.jsp"%>
    <%@include file="../../../public/include.jsp" %>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CriticalMonAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <script type="text/javascript">
		function impExcel(){
			   var url = getBasePath()+"/portalCommon/module/coreLink/impExcel/critical_imp_excel.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	    }
	</script>
</head>
<body style="height: 100%;width: 100%">
	 <form id="queryform" name="queryform">
	    
	    <input type="hidden" name="title"    value="客户服务核心环节-临界值报表">
	    <input type="hidden" name="colNames" value="地域	,临界值投诉量,总投诉量,计费用户数,临界值投诉率">
		<div id="container" style="height: 100%;width: 100%">
		<div style="margin-top: 5px" align="center">月份:
          <select id="dateTime"    name="dateTime" style="width: 100px;">
	           <% for(String month:months ){  %>
	           <option value="<%=month %>"><%=month %></option>
	           <%  }%>    
          </select>	
		   &nbsp;&nbsp;&nbsp;&nbsp;区域:
		   <input type="hidden"    id="zoneCode"            name="zoneCode"  />
		   <input type="text"      id="zone"                name="zone"    />
	       &nbsp;&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"        style="width:60px;" />
	       &nbsp;&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出"    onclick="impExcel();"         style="width:60px;" />
	     </div> 
	     <br/>
	     <div id="chartdiv"   align="center"></div>
		 <div id="chartTable" align="center"></div> 
		 <br/> 
		     
	 </div>
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="criticalMon.js"></script> 