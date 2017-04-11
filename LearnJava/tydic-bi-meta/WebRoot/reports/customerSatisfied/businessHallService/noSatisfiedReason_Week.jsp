<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.customerSatisfied.CustomerSatisfiedDAO"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
	CustomerSatisfiedDAO dao=new CustomerSatisfiedDAO();
	String week = request.getParameter("dateTime");
	String zoneCode       =(String)request.getParameter("zoneCode");
	if(week==null || week.equals("")){
	    week=dao.getMaxWeek_EveryType("1","2");
	}
	 List<Map<String, Object>> weekList=dao.getDateTimeList_EveryType("1","2");
	 request.setAttribute("dateTime",week);
%>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
		function impExcel(){
	     	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	    }    
	</script>
</head>
   <script type="text/javascript">
	   var zoneCode="<%=zoneCode %>";
	       zoneCode=(zoneCode != 'null' && zoneCode != "")?zoneCode:userInfo['localCode'];
   </script>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   /> 
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
				<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="1000"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
													               <td style="width: 220px;">&nbsp;&nbsp;&nbsp;日期:
          <select id="dateTime"    name="dateTime" style="width: 160px;">
           <% for (Map<String, Object> key : weekList) {  
           %>
                <% if(((String)key.get("DATE_NO")).equals(week) ){ %>
                  <option value="<%=key.get("DATE_NO") %>" selected="selected"><%=key.get("DATE_NO").toString() %></option>
               <% } else{%>   
                  <option value="<%=key.get("DATE_NO") %>"><%=key.get("DATE_NO").toString() %></option>
               <% }%> 
           <%  }%>    
          </select>		</td>
           <td style="width: 160px;">区域:
		   <input type="hidden""  id="zoneCode"    name="zoneCode" />
		   <input type="text"     id="zone"  size="17"      name="zone" /></td>
	       <td style="width: 100px;"><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:80px;" /></td>
	     <td style="width: 80px;"><input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导出报表" onclick="impExcel();"         style="width:80px;" /></td>
	      <td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	     </td>
													                </tr>
													</table>			        
										    </td>
										</tr>
							  </table>
							  </td>
			       </tr>
 <tr>
			          <td width="100%"> 			                  
							<table id='topTable' width='100%' height='auto!important' border='0' cellpadding='0px' cellspacing='2px'>
								<tr>
									<td>
										<table style='border: 1px solid #87CEFF;' width='100%' height='270px' border='0' cellpadding='0' cellspacing='0'>
											<tr>
												<td>
													<div id='chartdiv'></div>
												</td>
											</tr>
										</table>
									</td>
									<td>
										<table style='border: 1px solid #87CEFF;' width='100%' height='270px' border='0' cellpadding='0' cellspacing='0'>
											<tr>
												<td>
													<div id='chartTable'></div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
				      </td>
				   </tr>
	      </table>
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="noSatisfiedReason_Week.js"></script>