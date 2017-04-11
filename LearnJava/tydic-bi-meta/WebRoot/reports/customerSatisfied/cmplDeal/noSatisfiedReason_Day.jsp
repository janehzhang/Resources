<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="tydic.reports.customerSatisfied.CustomerSatisfiedDAO"%>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
     String zoneCode       =(String)request.getParameter("zoneCode");
     String dateTime =(String)request.getParameter("dateTime");
     if(dateTime==null || dateTime.equals("")){
    	  CustomerSatisfiedDAO dao=new CustomerSatisfiedDAO();
   	      dateTime=dao.getMaxDate_EveryType("3").substring(0,8);
	      String dayYear=dateTime.substring(0,4);
	      String dayMon=dateTime.substring(4,6);
	      String day=dateTime.substring(6,8);
	      dateTime=dayYear+"-"+dayMon+"-"+day;
 	}
      request.setAttribute("dateTime",dateTime);
%>
<head>
    <title>投诉处理不满意原因TOP日报</title>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
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
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value="投诉处理不满意原因TOP日报" />
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
        <input name="dateTime" id="dateTime" size="16" value="${dateTime}"  readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=dateTime %>'})" ></td>
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
<script type="text/javascript" src="noSatisfiedReason_Day.js"></script>