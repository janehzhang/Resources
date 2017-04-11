<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date,tydic.meta.sys.code.CodeManager,tydic.portalCommon.coreLink.coreCommon.BusiStepGeneralAction" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
      String dateTime=(String)request.getParameter("dateTime");	
    //String indName=(String)request.getParameter("indName");
    //indName= new String(indName.trim().getBytes("ISO-8859-1"), "UTF-8"); 
      String indId=(String)request.getParameter("indId"); 
      String indName=CodeManager.getName("HUGJHJ_TYPE", indId);
      
      BusiStepGeneralAction  bsga=new BusiStepGeneralAction();
	  String newMonth=(String)bsga.getNewMonth(indId).substring(0,7);
	  String[] months=DateUtil.getAddMonths(16,newMonth);
%>
<head>
    <%@include file="../../../public/head.jsp"%>
    <%@include file="../../../public/include.jsp" %>
    <title><%=indName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/BusiStepGeneralAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/DeptAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <script type="text/javascript">
	   var indId=<%=indId %>;
	</script>
	   	<script type="text/javascript">
		function impExcel(){
	     	    var url = getBasePath()+"/portalCommon/module/coreLink/impExcel/busi_imp_excel.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	    }
	</script>
</head>
<body style="height: 100%;width: 100%">
	<form id="queryform" name="queryform">
	    <input type="hidden" name="title"    value="<%=indName %>" >
	    <input type="hidden" name="colNames" value="客户服务诉求,具体指标,指标权重,指标指向,预警值,当月值,投诉占比,偏离度,扣分">
		<div id="container" style="height: 100%;width: 100%">
		 <div style="margin-top: 5px" align="center">月份:
	  	   
	  	   <!--
	  	   <input name="dateTime" id="dateTime" value="${dateTime}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM'})" >
		    -->
         
         <select id="dateTime"    name="dateTime" style="width: 100px;">
           <% for(String month:months ){
                
                if(dateTime !=null && month.equals(dateTime) ){
                //System.out.println("====="+dateTime);
          %>
                  <option value="<%=month %>" selected="selected"><%=month %></option>
              <%}else{%>    
                   <option value="<%=month %>"><%=month %></option>
         <%     }
            }
         %>
          </select>	
          		    
		   &nbsp;&nbsp;&nbsp;&nbsp;区域:
		   <input type="hidden"  id="zoneId"       name="zoneId" />
		   <input type="text"    id="zone"         name="zone"   />
		   <input type="hidden"  id="indId"        name="indId"  value="<%=indId %>" />
	       &nbsp;&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"  style="width:60px;" />
	       &nbsp;&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出"    onclick="impExcel();"   style="width:60px;" />
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
<script type="text/javascript" src="busiStepGeneral.js"></script>