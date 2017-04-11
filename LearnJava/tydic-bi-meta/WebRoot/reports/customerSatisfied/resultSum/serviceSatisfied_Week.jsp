<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="tydic.reports.customerSatisfied.CustomerSatisfiedDAO"%>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    String dateType="1";
	String week =(String)request.getParameter("dateTime");
    CustomerSatisfiedDAO dao=new CustomerSatisfiedDAO();
    if(week==null || week.equals("")){
        week=dao.getMaxDate_ResultSum(dateType);//周报，宽带新装
    }
    String filePath="/upload/excelHeader/serviceSatisfied_ResultSumWeek.xls";
    String areaCode="0000";
    String zoneCode =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
    List<Map<String, Object>> weekList=dao.getDateTimeList_ResultSum(dateType);
%>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ReportConfigAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/warning.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
		function impExcel(){
			var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
		    var  zone=$("zone").value;//区域
		    var queryCond="周期："+dateTime+"    区域："+zone;
	   	    $("excelCondition").value=queryCond; 
	   	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel.jsp";
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
	    }    
	</script>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
        <input type="hidden" id="excelHeader"  name="excelHeader"  value=<%=menuName %> />
        <input type="hidden" id="excelData"    name="excelData"   />  
        <input type="hidden" id="totalCount"   name="totalCount"    /> 
         <input type="hidden" id="dateType"    name="dateType"  value=<%=dateType%> /> 
         <input type="hidden" id="excelCondition"    name="excelCondition"     />
         <input type="hidden" id="filePath"    name="filePath" value=<%=filePath %> />
          <input type="hidden" id="row"    name="row" value="5" /> 
				<table width='350%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%' style='border: 1px solid #87CEFF;' border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="600"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		<td style="width: 220px;">&nbsp;&nbsp;&nbsp;周期:
          <select id="dateTime"    name="dateTime" style="width: 160px;">
           <% for (Map<String, Object> key : weekList) {  
           %>
               <% if(((String)key.get("DATE_NO")).equals(week) ){ %>
                  <option value="<%=key.get("DATE_NO") %>" selected="selected"><%=key.get("DATE_NO").toString() %></option>
               <% } else{%>   
                  <option value="<%=key.get("DATE_NO") %>"><%=key.get("DATE_NO").toString() %></option>
               <% }%> 
           <% }%>    
          
          </select>	
          </td>
           <td style="width: 150px;">区域:
		   <input type="hidden""  id="zoneCode"  value="<%=zoneCode%>" name="zoneCode" />
		   <input type="text"     id="zone"  size="17"      name="zone" /></td>
	       <td style="width: 70px;"><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td style="width: 70px;"><input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
	     </tr>
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
                       </td>
                   </tr> 
	      </table>
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="serviceSatisfied_Week.js"></script>