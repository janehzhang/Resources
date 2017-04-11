<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
		String dateTime=(String)request.getParameter("dateTime");	
		if(dateTime==null || dateTime.equals("") ){
		  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		  dateTime=formatter.format(new Date());
		  dateTime=DateUtil.addMonth(dateTime, -1);
		  dateTime=DateUtil.formatToTimeNosce(dateTime);
		}
		request.setAttribute("dateTime",dateTime);
%>
<head>
    <title>生命周期投诉率</title>
    <%@include file="../../../public/head.jsp"%>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplRateAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/DeptAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <script type="text/javascript">
		function impExcel(){
	     	    var url = getBasePath()+"/portalCommon/module/coreLink/impExcel/cmpl_imp_excel.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	    }
	</script>
</head>
<body style="height: 100%;width: 100%">
	<form id="queryform" name="queryform">
	<input type="hidden" name="title"    value="生命周期投诉率">
	    <input type="hidden" name="colNames" value="月份,生命周期 ,环节权重,全业务用户数,投诉量,投诉率,投诉率均值,偏离度,分值">
		<div id="container" style="height: 100%;width: 100%">
		 <div style="margin-top: 5px" align="center">月份:
	  	   <input name="dateTime" id="dateTime" value="${dateTime}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM'})" >
		   &nbsp;&nbsp;&nbsp;&nbsp;区域:
		   <input type="hidden"  id="zoneId"       name="zoneId"/>
		   <input type="text"    id="zone"         name="zone" />
	       &nbsp;&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" />
	        &nbsp;&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"   value="导  出"    onclick="impExcel();"     style="width:60px;" />
	      </div> 
	     <div id="buttonDiv"  align="center">
		    
	     </div>
	     <div id="chartdiv"   align="center"></div>
		 <div id="chartTable" align="center"></div> 
		 <br/>     
	  </div>
	</form>	
	<iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="cmplRate.js"></script> 
