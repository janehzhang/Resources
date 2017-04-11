<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.channel.allBusiness.AllChannelDAO"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    AllChannelDAO dao=new AllChannelDAO();
	String startDate = request.getParameter("startDate"); 
	String endDate = request.getParameter("endDate"); 
	if(startDate==null || startDate.equals("")){
		startDate=dao.getNewDay("CS_CHANNEL_OVERALL_GOALS");
		String year=startDate.substring(0,4);
		String month=startDate.substring(4,6);
		String day=startDate.substring(6,8);
		startDate=year+"-"+month+"-"+day;
		endDate=startDate;
	}  
	request.setAttribute("startDate", startDate);
	request.setAttribute("endDate", endDate);
	String areaCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
    String zoneCode = request.getParameter("zoneCode")==null?"0000":(String)request.getParameter("zoneCode");
%>
<head>   
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/AllChannelAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/Charts/FusionChartsExportComponent.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
	<script type="text/javascript">
	function impExcel(form){
		dhx.showProgress("正在执行......");
		exportImage(); 
		window.setTimeout("exportExcel()",8000);
	    window.setTimeout("dhx.closeProgress()",8000);
	}
	</script>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	    <input type="hidden" id="excelTitle"   name="excelTitle"    value="<%=menuName %>" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />  
        <input type="hidden" id="excelCondition"    name="excelCondition"/> 
        <input type="hidden" id="cid"  name="cid"   />
	<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%'  border='0'  style='border: 1px solid #87CEFF;'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="750"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
           <td style="width: 180px;">&nbsp;&nbsp;日期从:
	  	       <input name="startDate" id="startDate" size="18" value="${startDate}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=startDate %>'})" >
		   </td>
		   <td style="width: 180px;">&nbsp;&nbsp;至:
		       <input name="endDate"  id="endDate"    size="18" value="${endDate}" readonly="true" class='Wdate'   onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=endDate %>'})" >
		   &nbsp;&nbsp;</td>
           <td>区域:
           <input type="hidden""  id="zoneCode" value=<%=zoneCode%> name="zoneCode" />
		   <input type="text"     id="zone"  size="17"      name="zone" /></td>
	       <td><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td>&nbsp;&nbsp;&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>

													                </tr>
													</table>			        
										    </td>
										</tr>
							  </table>	    
                          </td>
			       </tr>
			        <!-- 顶部图形 -->	   
			       <tr>
			          <td width="100%"> 			                  
							<table id='topTable' width='100%' height='auto!important' border='0' cellpadding='0px' cellspacing='2px'>
								<tr>
									<td width='50%'>
										<table width='100%' height='200px' border='0' style='border: 1px solid #87CEFF;' cellpadding='0' cellspacing='0'>
											<tr>
												<td >
													<div id='chartdiv'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table width='100%' height='200px' border='0' style='border: 1px solid #87CEFF;' cellpadding='0' cellspacing='0'>
											<tr>
												<td >
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
<script type="text/javascript" src="channelOverGoals_Day.js"></script>