<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Date,tydic.reports.channel.allBusiness.AllBusinessAction" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
   java.util.Date datetime=new java.util.Date();
   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
   String endDate=formatter.format(datetime);
   String startDate=DateUtil.lastDay(endDate,-6);
   request.setAttribute("startDate",startDate); 
   request.setAttribute("endDate",endDate);
%>
<head>
    <title>全渠道总体情况分析日报表</title>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/AllBusinessAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ChannelTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/channelTypeTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <script type="text/javascript">
		function impExcel(){
	     	    var url = getBasePath()+"/reports/channel/allBusiness/impExcel/allBusinessWeek_impExcel.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	    }   
	</script>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	   <input type="hidden" name="title"    value="全渠道总体情况分析日报表">
	    <input type="hidden" name="colNames" value="大区,地市,渠道类型,缴费笔数,查询量,咨询量,办理量,回馈量,反馈量,开通、变更、停用量,投诉量,障碍量,其它量,服务总量,渠道服务量占比,渠道服务量占比(不含查询、咨询量)">
		<div id="container" style="height: 100%;width: 100%"><table>
		 <tr><td>&nbsp;&nbsp;日期从:
	  	   <input name="startDate" id="startDate" size="12" value="${startDate}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=endDate %>'})" >
		   </td><td>&nbsp;&nbsp;至:
		   <input name="endDate"  id="endDate"    size="12" value="${endDate}" readonly="true" class='Wdate'   onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=endDate %>'})" >
		   &nbsp;&nbsp;</td>
         <!--<td>&nbsp;&nbsp;&nbsp;周:
          <select id="week"    name="week" style="width: 100px;">
           <option value="">--</option> 
           <option value="1">第一周</option>
           <option value="2">第二周</option>
           <option value="3">第三周</option>
           <option value="4">第四周</option>
           <option value="5">第五周</option>
          </select>		</td>  --> 
           <td>&nbsp;&nbsp;&nbsp;区域:
		   <input type="hidden""  id="zoneCode"    name="zoneCode" />
		   <input type="text"     id="zone"  size="14"      name="zone" /></td>
		   <td>&nbsp;&nbsp;&nbsp;渠道类型:
		   <input type="hidden""  id="channelTypeCode"    name="channelTypeCode" />
		   <input type="text"     id="channelType"  size="14"      name="channelType" /></td>
	       <td>&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td>&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导出报表" onclick="impExcel();"         style="width:80px;" /></td>
	      <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" id="city" name="city" class="poster_btn"  value='切换地市' onclick="lookCity(this);" style="width:70px;"/></td></tr></table></div>   
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
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="allBusiness_Week.js"></script>