<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date,tydic.portalCommon.coreLink.CustomerCoreScoreAction" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  String dateTime=formatter.format(new Date());
  dateTime=DateUtil.addMonth(dateTime, -1);
  request.setAttribute("dateTime",DateUtil.formatToTimeNosce(dateTime)); 
  
  CustomerCoreScoreAction  ccsa=new CustomerCoreScoreAction();
  String newMonth=(String)ccsa.getNewMonth().substring(0,7);
  String[] months=DateUtil.getAddMonths(12,newMonth);
%>
<head>
    <title>客户服务核心环节得分</title>
    <%@include file="../../public/head.jsp"%>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerCoreScoreAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/DeptAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
   	<script type="text/javascript">
		function impExcel(){
	     	    var url = getBasePath()+"/portalCommon/module/coreLink/impExcel/list_imp_excel.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	    }
	</script>
</head>
<body style="height: 100%;width: 100%">
	<form id="queryform" name="queryform">
	    <input type="hidden" name="title"    value="环节得分">
	    <input type="hidden" name="colNames" value="生命周期,环节权重,环节得分,转换100分制得分,排序">
	    
		<div id="container" style="height: 100%;width: 100%">
		 <div style="margin-top: 5px" align="center">月份:
	  	   <!-- 
	  	   <input name="dateTime" id="dateTime" value="${dateTime}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM'})" >
		   -->
          <select id="dateTime"    name="dateTime" style="width: 100px;">
           <% for(String month:months ){  %>
           <option value="<%=month %>"><%=month %></option>
               <%  }%>    
          </select>		   
		   &nbsp;&nbsp;&nbsp;&nbsp;区域:
		   <input type="hidden"  id="zoneId"       name="zoneId"/>
		   <input type="text"  id="zone"         name="zone"/>
	       &nbsp;&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"        style="width:60px;" />
	       <!--&nbsp;&nbsp;<input type='button'  class="poster_btn" id="detailBtn" name="detailBtn" value="详  情"    onclick="detail();"       style="width:60px;" />
	       &nbsp;&nbsp;<input type='button'  class="poster_btn" id="detailBtn" name="detailBtn" value="权重配置" onclick="wieghtConfig();"     style="width:80px;" />  -->
	       &nbsp;&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出"   onclick="impExcel();"          style="width:60px;" />
	      </div> 
	     <div id="chartdiv"   align="center"></div>
		 <div id="chartTable" align="center">
		 
		</div>      
		<br/>
	 </div>
	</form>	
    <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="customerCoreScore.js"></script>