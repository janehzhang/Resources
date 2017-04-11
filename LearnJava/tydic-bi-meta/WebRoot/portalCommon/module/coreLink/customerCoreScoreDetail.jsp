<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date,tydic.portalCommon.coreLink.CustomerCoreScoreDetailAction" %>
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
  
  CustomerCoreScoreDetailAction  ccsa=new CustomerCoreScoreDetailAction();
  String newMonth=(String)ccsa.getNewMonth().substring(0,7);
  String[] months=DateUtil.getAddMonths(16,newMonth);
%>
<head>
    <%@include file="../../public/head.jsp"%>
    <%@include file="../../public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerCoreScoreDetailAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/DeptAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
       	<script type="text/javascript">
		function impExcel(){
	     	    var url = getBasePath()+"/portalCommon/module/coreLink/impExcel/detail_imp_excel.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	    }
	</script>
</head>
<body style="height: 100%;width: 100%">
	 <form id="queryform" name="queryform">
	    <input type="hidden" name="title"    value=<%=menuName %>">
	    <input type="hidden" name="colNames" value="生命周期,环节权重(%),扣分,环节得分,100分制">
		<div id="container" style="height: 100%;width: 100%">
	 <table style="margin-top: 5px" width="700px" align="center"  border='0' cellpadding='0' cellspacing='0'>
		 <tr>
		   <td> 月 份:</td>
		   <td>
				 <select id="dateTime"    name="dateTime" style="width: 100px;">
		           <% for(String month:months ){  %>
		           <option value="<%=month %>"><%=month %></option>
		               <%  }%>    
		         </select>	
		   </td>
		   <td>区 域:</td>
		   <td>
		   		  <input type="hidden""  id="zoneId"            name="zoneId"   />
		          <input type="text"     id="zone"              name="zone"    />
		   </td>
		   <td>环 节:</td>
		   <td>
		   	   <select id="indId"  name="indId"  style="width: 150px;" onchange="selectData(this.value)">
					<option value="">所有环节</option>
					<option value="1000">业务咨询</option>
					<option value="1001">业务办理</option>
					<option value="1002">业务开通</option>
					<option value="1003">装移机</option>
					<option value="1004">网络质量</option>
					<option value="1005">产品质量</option>
					<option value="1006">使用提醒</option>
					<option value="1007">计费与账单</option>
					<option value="1008">充值缴费</option>
					<option value="1009">故障修复</option>
					<option value="1010">投诉处理</option>
					<option value="1011">客户关怀</option>
		      </select>		   
		   </td>
		   <td>
		     <input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"       style="width:60px;" />
		     &nbsp;&nbsp;
		   </td>
		   <td>
		     <input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出"    onclick="impExcel();"        style="width:60px;" />
		     &nbsp;&nbsp;
		   </td>
		</tr>
	  </table> 
	     <div id="buttonDiv"   align="center">
	     </div>
		 <div align="right" style="width: 900px">
		   <input type='button' id='city' name='city' class='poster_btn'  value='切换地市'  onclick="lookCity(this)"  style='width:70px;display: none;'/>
		 </div>
	     <div id="chartdiv"   align="center"></div>
	     <div id="chartdiv1"   align="center"></div>
		 <div id="chartTable" align="center"></div> 
		 <br/>     
	 </div>
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="customerCoreScoreDetail.js"></script> 
