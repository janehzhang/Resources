<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.customerSatisfied.CustomerSatisfiedDAO"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    CustomerSatisfiedDAO dao=new CustomerSatisfiedDAO();
    String week = request.getParameter("dateTime");
    if(week==null || week.equals("")){
        week=dao.getMaxDateTime_CustomerSatisfied("1");
    }
     List<Map<String, Object>> weekList=dao.getDateTimeList("1");
     request.setAttribute("dateTime",week);
     
     String month = request.getParameter("dateTime1");
     if(month==null || month.equals("")){
    	 month=dao.getMaxDateTime_CustomerSatisfied("2");
     }
      List<Map<String, Object>> monList=dao.getDateTimeList("2");
      request.setAttribute("dateTime1",month);
      
      String  dayTime=dao.getMaxDate_Enter().substring(0,8);
      String dayYear=dayTime.substring(0,4);
      String dayMon=dayTime.substring(4,6);
      String day=dayTime.substring(6,8);
      dayTime=dayYear+"-"+dayMon+"-"+day;
      request.setAttribute("dateTime2",dayTime);
      String areaCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
      String zoneCode  =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
%>
<head>
    <title>服务触点满意度总体监测</title>
    <%@include file="../../portalCommon/public/head.jsp"%>
    <%@include file="../../portalCommon/public/include.jsp" %>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
	<script type="text/javascript">
		function impExcel(form){
		     	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel.jsp";
					document.forms[0].method = "post";
					document.forms[0].action=url;
					document.forms[0].target="hiddenFrame";
					document.forms[0].submit();
		}
		function impExcel1(form){
     	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel1.jsp";
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
}
		function impExcel2(form){
     	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel2.jsp";
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
}
	</script>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	<input type="hidden" id="excelTitle"   name="excelTitle"    value="客户满意率监测周报" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />  
        
        <input type="hidden" id="excelTitle1"   name="excelTitle1"    value="客户满意率监测月报" />
        <input type="hidden" id="excelHeader1"  name="excelHeader1"   />
        <input type="hidden" id="excelData1"    name="excelData1"   /> 
        
        <input type="hidden" id="excelTitle2"   name="excelTitle2"    value="客户满意率监测日报" />
        <input type="hidden" id="excelHeader2"  name="excelHeader2"   />
        <input type="hidden" id="excelData2"    name="excelData2"   /> 
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="zoneCode"    name="zoneCode"  value=<%=zoneCode%> />  
        	<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="1050"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		    <td style="width: 220px;">&nbsp;&nbsp;&nbsp;日期:
		    <input name="dateTime2" id="dateTime2" value="${dateTime2}"  readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=dayTime %>'})" >
          </td>
	       <td><input type='button'  class="poster_btn" id="queryBtn2"  name="queryBtn2"  value="查  询"    onclick="queryData2();"    style="width:60px;" /></td>
	     <td><input type='button'  class="poster_btn" id="impBtn2"    name="impBtn2"    value="导  出" onclick="impExcel2();" style="width:60px;" /></td>
		<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='200px' border='0' cellpadding='0' cellspacing='0'>
											<tr>
												<td>
													<div id='chartdiv2'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='200px' border='0' cellpadding='0' cellspacing='0'>
											<tr>
												<td>
													<div id='chartTable2'></div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
				      </td>
				   </tr>
                      
	      </table>
				<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%' border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="1050"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		    <td style="width: 220px;">&nbsp;&nbsp;&nbsp;周期:
          <select id="dateTime"    name="dateTime" style="width: 160px;"> 
          <% for (Map<String, Object> key: weekList){
          if(week.equals(key.get("DATE_NO"))){
           %>
          <option value="<%=key.get("DATE_NO")%>" selected><%=key.get("DATE_NO").toString()%></option>
          <%}else{ %>
         
             <option value="<%=key.get("DATE_NO")%>" ><%=key.get("DATE_NO").toString()%></option>
          <% 
          }
          }%>  
          </select>		</td>
	       <td><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td><input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
		<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='200px' border='0' cellpadding='0' cellspacing='0'>
											<tr>
												<td>
													<div id='chartdiv'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='200px' border='0' cellpadding='0' cellspacing='0'>
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
	      <table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="1050"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		    <td style="width: 220px;">&nbsp;&nbsp;&nbsp;月份:
          <select id="dateTime1"    name="dateTime1" style="width: 160px;"> 
          <% for (Map<String, Object> key: monList){
          if(month.equals(key.get("DATE_NO"))){
           %>
          <option value="<%=key.get("DATE_NO")%>" selected><%=key.get("DATE_NO").toString()%></option>
          <%}else{ %>
         
             <option value="<%=key.get("DATE_NO")%>" ><%=key.get("DATE_NO").toString()%></option>
          <% 
          }
          }%>  
          </select>		</td>
	       <td><input type='button'  class="poster_btn" id="queryBtn1"  name="queryBtn1"  value="查  询"    onclick="queryData1();"    style="width:60px;" /></td>
	     <td><input type='button'  class="poster_btn" id="impBtn1"    name="impBtn1"    value="导  出" onclick="impExcel1();"         style="width:60px;" /></td>
		<td >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='200px' border='0' cellpadding='0' cellspacing='0'>
											<tr>
												<td>
													<div id='chartdiv1'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='200px' border='0' cellpadding='0' cellspacing='0'>
											<tr>
												<td>
													<div id='chartTable1'></div>
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
<script type="text/javascript" src="satisfiedMonitor_Week.js"></script>