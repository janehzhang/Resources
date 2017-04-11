<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.customerKeep.serviceProcessAnalysis.ServiceReActiveDAO" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
    ServiceReActiveDAO dao=new ServiceReActiveDAO();
	String excelTitle="";
	String rptId="";
	String filePath="";
	String tabStr="";
	String width="";
	String userDimension=(String)request.getParameter("userDimension");
	 if("01".equals(userDimension)){
		 excelTitle="政企属性用户投诉下单分布情况月报表";
		 rptId="30021";
		 filePath="/upload/excelHeader/publicCustCmpl_Mon.xls";
		 tabStr="CS_CHANNEL_CMPL_DISTRIBUTION";
		 width="900%";
	 } if("02".equals(userDimension)){
		 excelTitle="VPN用户投诉下单分布情况月报表";
		 rptId="30021";
		 filePath="/upload/excelHeader/publicCustCmpl_Mon.xls";
		 tabStr="CS_CHANNEL_CMPL_DISTRIBUTION";
		 width="900%";
	 } if("03".equals(userDimension)){
		 excelTitle="行业应用用户投诉下单分布情况月报表";
		 rptId="30021";
		 filePath="/upload/excelHeader/publicCustCmpl_Mon.xls";
		 tabStr="CS_CHANNEL_CMPL_DISTRIBUTION";
		 width="900%";
	 } if("04".equals(userDimension)){
		 excelTitle="VPN及行业应用双重身份用户投诉下单分布情况月报表";
		 rptId="30021";
		 filePath="/upload/excelHeader/publicCustCmpl_Mon.xls";
		 tabStr="CS_CHANNEL_CMPL_DISTRIBUTION";
		 width="900%";
	 } if("05".equals(userDimension)){
		 excelTitle="政企属性用户需求下单分布情况月报表";
		 rptId="30022";
		 filePath="/upload/excelHeader/publicCustReq_Mon.xls";
		 tabStr="CS_CHANNEL_REQ_DISTRIBUTION";
		 width="400%";
	 } if("06".equals(userDimension)){
		 excelTitle="VPN用户需求下单分布情况月报表";
		 rptId="30022";
		 filePath="/upload/excelHeader/publicCustReq_Mon.xls";
		 tabStr="CS_CHANNEL_REQ_DISTRIBUTION";
		 width="400%";
	 } if("07".equals(userDimension)){
		 excelTitle="行业应用用户需求下单分布情况月报表";
		 rptId="30022";
		 filePath="/upload/excelHeader/publicCustReq_Mon.xls";
		 tabStr="CS_CHANNEL_REQ_DISTRIBUTION";
		 width="400%";
	 } if("08".equals(userDimension)){
		 excelTitle="VPN及行业应用双重身份用户需求下单分布情况月报表";
		 rptId="30022";
		 filePath="/upload/excelHeader/publicCustReq_Mon.xls";
		 tabStr="CS_CHANNEL_REQ_DISTRIBUTION";
		 width="400%";
	 } if("09".equals(userDimension)){
		 excelTitle="政企属性用户咨询话务情况月报表";
		 rptId="30023";
		 filePath="/upload/excelHeader/publicCustConsult_Mon.xls";
		 tabStr="CS_CHANNEL_CONSULT_DISTRI";
		 width="600%";
	 } if("10".equals(userDimension)){
		 excelTitle="VPN用户咨询话务情况月报表";
		 rptId="30023";
		 filePath="/upload/excelHeader/publicCustConsult_Mon.xls";
		 tabStr="CS_CHANNEL_CONSULT_DISTRI";
		 width="600%";
	 } if("11".equals(userDimension)){
		 excelTitle="行业应用用户咨询话务情况月报表";
		 rptId="30023";
		 filePath="/upload/excelHeader/publicCustConsult_Mon.xls";
		 tabStr="CS_CHANNEL_CONSULT_DISTRI";
		 width="600%";
	 } if("12".equals(userDimension)){
		 excelTitle="VPN及行业应用双重身份用户咨询话务情况月报表";
		 rptId="30023";
		 filePath="/upload/excelHeader/publicCustConsult_Mon.xls";
		 tabStr="CS_CHANNEL_CONSULT_DISTRI";
		 width="600%";
	 } if("13".equals(userDimension)){
		 excelTitle="政企属性用户查询话务情况月报表";
		 rptId="30024";
		 filePath="/upload/excelHeader/publicCustQuery_Mon.xls";
		 tabStr="CS_CHANNEL_QUERY_DISTRI";
		 width="150%";
	 } if("14".equals(userDimension)){
		 excelTitle="VPN用户查询话务情况月报表";
		 rptId="30024";
		 filePath="/upload/excelHeader/publicCustQuery_Mon.xls";
		 tabStr="CS_CHANNEL_QUERY_DISTRI";
		 width="150%";
	 } if("15".equals(userDimension)){
		 excelTitle="行业应用用户查询话务情况月报表";
		 rptId="30024";
		 filePath="/upload/excelHeader/publicCustQuery_Mon.xls";
		 tabStr="CS_CHANNEL_QUERY_DISTRI";
		 width="150%";
	 } if("16".equals(userDimension)){
		 excelTitle="VPN及行业应用双重身份用户查询话务情况月报表";
		 rptId="30024";
		 filePath="/upload/excelHeader/publicCustQuery_Mon.xls";
		 tabStr="CS_CHANNEL_QUERY_DISTRI";
		 width="150%";
	 }if("17".equals(userDimension)){
		 excelTitle="政企属性用户办理话务情况月报表";
		 rptId="30025";
		 filePath="/upload/excelHeader/publicCustDeal_Mon.xls";
		 tabStr="CS_CHANNEL_DEAL_DISTRI";
		 width="200%";
	 } if("18".equals(userDimension)){
		 excelTitle="VPN用户办理话务情况月报表";
		 rptId="30025";
		 filePath="/upload/excelHeader/publicCustDeal_Mon.xls";
		 tabStr="CS_CHANNEL_DEAL_DISTRI";
		 width="200%";
	 } if("19".equals(userDimension)){
		 excelTitle="行业应用用户办理话务情况月报表";
		 rptId="30025";
		 filePath="/upload/excelHeader/publicCustDeal_Mon.xls";
		 tabStr="CS_CHANNEL_DEAL_DISTRI";
		 width="200%";
	 } if("20".equals(userDimension)){
		 excelTitle="VPN及行业应用双重身份用户办理话务情况月报表";
		 rptId="30025";
		 filePath="/upload/excelHeader/publicCustDeal_Mon.xls";
		 tabStr="CS_CHANNEL_DEAL_DISTRI";
		 width="200%";
	 }if("21".equals(userDimension)){
		 excelTitle="政企属性用户报障话务情况月报表";
		 rptId="30026";
		 filePath="/upload/excelHeader/publicCustError_Mon.xls";
		 tabStr="CS_CHANNEL_ERROR_DISTRI";
		 width="150%";
	 } if("22".equals(userDimension)){
		 excelTitle="VPN用户报障话务情况月报表";
		 rptId="30026";
		 filePath="/upload/excelHeader/publicCustError_Mon.xls";
		 tabStr="CS_CHANNEL_ERROR_DISTRI";
		 width="150%";
	 } if("23".equals(userDimension)){
		 excelTitle="行业应用用户报障话务情况月报表";
		 rptId="30026";
		 filePath="/upload/excelHeader/publicCustError_Mon.xls";
		 tabStr="CS_CHANNEL_ERROR_DISTRI";
		 width="150%";
	 } if("24".equals(userDimension)){
		 excelTitle="VPN及行业应用双重身份用户报障话务情况月报表";
		 rptId="30026";
		 filePath="/upload/excelHeader/publicCustError_Mon.xls";
		 tabStr="CS_CHANNEL_ERROR_DISTRI";
		 width="150%";
	 }if("25".equals(userDimension)){
		 excelTitle="政企属性用户投诉话务情况月报表";
		 rptId="30027";
		 filePath="/upload/excelHeader/publicCustComplain_Mon.xls";
		 tabStr="CS_CHANNEL_COMPLAIN_DISTRI";
		 width="600%";
	 } if("26".equals(userDimension)){
		 excelTitle="VPN用户投诉话务情况月报表";
		 rptId="30027";
		 filePath="/upload/excelHeader/publicCustComplain_Mon.xls";
		 tabStr="CS_CHANNEL_COMPLAIN_DISTRI";
		 width="600%";
	 } if("27".equals(userDimension)){
		 excelTitle="行业应用用户投诉话务情况月报表";
		 rptId="30027";
		 filePath="/upload/excelHeader/publicCustComplain_Mon.xls";
		 tabStr="CS_CHANNEL_COMPLAIN_DISTRI";
		 width="600%";
	 } if("28".equals(userDimension)){
		 excelTitle="VPN及行业应用双重身份用户投诉话务情况月报表";
		 rptId="30027";
		 filePath="/upload/excelHeader/publicCustComplain_Mon.xls";
		 tabStr="CS_CHANNEL_COMPLAIN_DISTRI";
		 width="600%";
	 }
	 String areaCode="0000";
	 String zoneCode = request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
	 String month = request.getParameter("dateTime");
		if(month==null || month.equals("")){
			 month=dao.getNewMonthIndex(tabStr,userDimension);
		}
	 List<Map<String, Object>> monList=dao.getSelectMonIndex(tabStr,userDimension);
	 String parameters=dao.getParameters(rptId);
%>
<head>
    <title>excelTitle</title>
    <%@include file="../../portalCommon/public/head.jsp"%>
    <%@include file="../../portalCommon/public/include.jsp" %>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ServiceReActiveAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplIndexAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
	function impExcel(form){
		var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
	    var  zone=$("zone").value;//区域
	    var queryCond="月份："+dateTime+"    区域："+zone;
   	    $("excelCondition").value=queryCond; 
 	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel.jsp";
		document.forms[0].method = "post";
		document.forms[0].action=url;
		document.forms[0].target="hiddenFrame";
		document.forms[0].submit();
}  
	</script>
	<style type="text/css"> 
		.unl {
	    text-decoration:underline;
		color: #0000FF;
		cursor: pointer;
	}
	</style>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=excelTitle%> />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />
         <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="parameters"    name="parameters" value=<%=parameters%> />
         <input type="hidden" id="filePath"    name="filePath" value=<%=filePath %> />
          <input type="hidden" id="row"    name="row" value="5" />
          <input type="hidden" id="rptId"    name="rptId" value=<%=rptId%> />
          <input type="hidden" id="userDimension"    name="userDimension" value=<%=userDimension%> />
          <input type="hidden" id="explain"    name="explain"   />
	    <table width=<%=width%> >
					<tr>
						<td valign="top" width="100%">
							   <table  width='100%'  border='0'  style='border: 1px solid #87CEFF;'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="1050"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		<td style="width: 150px;">&nbsp;&nbsp;&nbsp;月份:
          <select id="dateTime"    name="dateTime" style="width: 100px;"> 
          <% for (Map<String, Object> key: monList){
          if(month.equals(key.get("MONTH_ID"))){
           %>
          <option value="<%=key.get("MONTH_ID")%>" selected><%=key.get("MONTH_ID").toString()%></option>
          <%}else{ %>
             <option value="<%=key.get("MONTH_ID")%>" ><%=key.get("MONTH_ID").toString()%></option>
          <% 
          }
          }%>  
          </select>
         </td> 
       <td style="width: 160px;">&nbsp;&nbsp;&nbsp;区域:
		   <input type="hidden""  id="zoneCode"  value=<%=zoneCode%>  name="zoneCode" />
		   <input type="text"     id="zone"  size="14"      name="zone" />
	  </td>
	       <td>&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td>&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td><%--
	    
	      <td>&nbsp;&nbsp;
	      <input type="button" id="city" name="city" class="poster_btn"  value='切换地市' onclick="lookCity(this);" style="width:70px;"/></td>    
													                --%></tr>
													</table>			        
										    </td>
										</tr>
							  </table>
							  </td>
							  </tr>	
							  <tr>
			          <td width="100%"> 
		                   <div id="chartTable" style="margin: 0px;padding: 0px;width: 100%;height: auto;"> 
		                   </div>
		                    <div id="index_exp"  class="tips"></div>   
                       </td>
                   </tr>    
	      </table>
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="publicCust_Mon.js"></script>