<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Date,tydic.reports.customerKeep.serviceProcessAnalysis.ServiceReActiveAction" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  String dateTime=formatter.format(new Date());
  dateTime=DateUtil.lastDay(dateTime, -1);
  request.setAttribute("dateTime",dateTime);
%>
<head>
    <title>移动和宽带用户停机复开及时率日报表</title>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ServiceReActiveAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <script type="text/javascript">
		function impExcel(){
	     	    var url = getBasePath()+"/reports/customerKeep/serviceProcessAnalysis/impExcel/serviceReActiveDay_impExcel.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	    }   
	</script>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	   <input type="hidden" name="title"    value="移动和宽带用户停机复开及时率日报表">
	    <input type="hidden" name="colNames" value="大区,地市,复机工单总数,3分钟复通工单数,3分钟复通及时率,30分钟复通工单数,30分钟复通及时率,复机工单总数,10分钟复通工单数,10分钟复通及时率,30分钟复通工单数,30分钟复通及时率">
		<div id="container" style="height: 100%;width: 100%">
		 <div style="margin-top: 5px" align="left"><tr><td>&nbsp;&nbsp;&nbsp;日期:
          <input name="dateTime" id="dateTime" value="${dateTime}"  readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=dateTime %>'})" ></td>
           <td>&nbsp;&nbsp;&nbsp;区域:
		   <input type="hidden""  id="zoneCode"    name="zoneCode" />
		   <input type="text"     id="zone"  size="14"      name="zone" />
	       <td>&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td>&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导出报表" onclick="impExcel();"         style="width:80px;" /></td>
	      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	      <input type="button" id="city" name="city" class="poster_btn"  value='切换地市' onclick="lookCity(this);" style="width:70px;"/></td></tr></div>   
	      <!-- 顶部图形 -->	
	<table> <tr> <td>  
	 <div id="chartdiv2" >	               
	 </div>        
	 </td> <td>
	 <div id="chartdiv1">	               
	 </div>               
	 </td>
	 </tr>
	 </table>
	 <div id="right_1">  
	    <div id="chartTable" ></div>  </br>  
	    <div id="index_exp"  class="tips"></div>   
	 </div>     
    </div>
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="serviceReActive_Day.js"></script>