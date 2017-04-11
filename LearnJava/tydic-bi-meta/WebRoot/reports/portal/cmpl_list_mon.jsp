<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Date,tydic.reports.ReportsMonAction" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
   java.util.Date datetime=new java.util.Date();
   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
   String endDate=formatter.format(datetime);
   String startDate=DateUtil.fistDay(datetime);
   String dateTime  =(String)request.getParameter("dateTime");
  
   if(dateTime !=null &&  !"".equals(dateTime)){
	   if(dateTime.length()>7){
	      startDate=dateTime;
		  endDate=dateTime;
	   }else{
		  startDate=dateTime+"-01";
		  endDate=dateTime+"-"+DateUtil.getDays( dateTime, "yyyy-MM");
	   }
   }
   String zoneCode          =(String)request.getParameter("zoneCode");	
   String prodTypeCode      =(String)request.getParameter("prodTypeCode");	
   String cmplBusiTypeCode  =(String)request.getParameter("cmplBusiTypeCode");	
   String indId          =(String)request.getParameter("indId");
   request.setAttribute("indId",indId); 	
   request.setAttribute("startDate",startDate); 
   request.setAttribute("endDate",endDate);
%>
<head>
    <title>本地全业务抱怨清单</title>
    <%@include file="../../portalCommon/public/head.jsp"%>
    <%@include file="../../portalCommon/public/include.jsp" %>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ReportsMonAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplBusiTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ProdTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/prodTypeTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/cmplBusiTypeTree.js"></script>
    
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <script type="text/javascript">
	   var zoneCode="<%=zoneCode %>";
	   zoneCode=(zoneCode!='null' && zoneCode!="")?zoneCode:userInfo['localCode'];
	   var prodTypeCode="<%=prodTypeCode %>";
	   prodTypeCode=(prodTypeCode!='null' && prodTypeCode!="")?prodTypeCode:"-1";
	   var cmplBusiTypeCode="<%=cmplBusiTypeCode %>";
	   cmplBusiTypeCode=(cmplBusiTypeCode!='null' && cmplBusiTypeCode!="")?cmplBusiTypeCode:"1";
	</script>
    <script type="text/javascript">
		function impExcel(){
				var prodTypeId =   $('prodTypeCode').value==''?"-1":$('prodTypeCode').value;
		        var cmplBusiTypeId =   $('cmplBusiTypeCode').value==''?"1":$('cmplBusiTypeCode').value;
	     	    var url = getBasePath()+"/reports/portal/cmpl_list_mon_impExcel.jsp?prodTypeId='"+prodTypeId+"'&cmplBusiTypeId='"+cmplBusiTypeId+"'";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	    }   
	</script>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	   <input type="hidden""  id="indId"    name="indId" value="${indId}" />
	   <input type="hidden" name="title"    value="本地全业务抱怨清单">
	    <input type="hidden" name="colNames" value="工单流水号 ,产品类型,服务类别,工单来源,工单类别,申告号码,申告地市,客户名称,联系电话1,联系电话2,受理时间,受理班组ID,受理班组(简称）,受理班组（全称）,归档时间,归档类型,工单历时,工单超时时长,超时时间,重复次数,分公司责任班组ID,分公司责任班组,分公司责任班组(描述）,定性内容,回访满意度,派外系统流水号,服务等级">
		<div id="container" style="height: 100%;width: 100%">
		</br>
		
		 <div style="margin-top: 5px" align="left">&nbsp;&nbsp;日期从:
	  	   <input name="startDate" id="startDate" size="12" value="${startDate}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=endDate %>'})" >
		   &nbsp;&nbsp;至:
		   <input name="endDate"  id="endDate"    size="12" value="${endDate}" readonly="true" class='Wdate'   onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=endDate %>'})" >
		   &nbsp;&nbsp;区域:
		   <input type="hidden""  id="zoneCode"    name="zoneCode" />
		   <input type="text"     id="zone"  size="14"      name="zone" />
		   &nbsp;&nbsp;产品类型:
		   <input type="hidden""  id="prodTypeCode"    name="prodTypeCode" />
		   <input type="text"    id="prodType"   size="18"      name="prodType"     />
		   &nbsp;&nbsp;投诉表象:
		   <input type="hidden""  id="cmplBusiTypeCode"   name="cmplBusiTypeCode" />
		   <input type="text"     id="cmplBusiType" size="18"      name="cmplBusiType" />
	       &nbsp;&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" />
	       &nbsp;&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导出" onclick="impExcel();"         style="width:80px;" />
	      </div></br>   
          <div id="right_1">
	           <div id="chartTable" style="margin: 0px;padding: 0px;width: 90%;height: auto;"></div> 
	           <div id="pageTag" align="center" ></div>
         </div>
		 <br/>     
	 </div>
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="cmpl_list_mon.js"></script>