<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.complain.monitorDay.CmplIndexDAO" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    CmplIndexDAO dao = new CmplIndexDAO();
    String maxMon=dao.getMaxMonth("CS_CMPL_SKIP_REASON_MON");
    String dateTime =(String)request.getParameter("dateTime")==null?maxMon:(String)request.getParameter("dateTime");	
    String areaCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
    String zoneCode  =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
    String channelType=(String)request.getParameter("channelType")==null?"":(String)request.getParameter("channelType");
    String reasonId=(String)request.getParameter("reasonId")==null?"":(String)request.getParameter("reasonId");

	List<Map<String, Object>> monList = dao.getMonList("CS_CMPL_SKIP_REASON_MON");
	List<Map<String, Object>> channelList = dao.getChannelList("CS_CMPL_SKIP_REASON_MON");
	List<Map<String, Object>> reasonList = dao.getReasonList("CS_CMPL_SKIP_REASON_MON");

	request.setAttribute("dateTime", dateTime);
	request.setAttribute("zoneCode", zoneCode);
%>
<head>
    <title>越级申诉投诉原因分析月报表</title>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplIndexAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
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
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value="越级申诉投诉原因分析月报表" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
	    <table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="1080"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		 <td>&nbsp;&nbsp;&nbsp;月份:
          <select id="dateTime"    name="dateTime" style="width: 80px;">
           <%
           	for (Map<String, Object> key : monList) {
           	if(dateTime.equals(key.get("MONTH_ID"))){
           %>
           <option value="<%=key.get("MONTH_ID")%>" selected><%=key.get("MONTH_ID").toString()%></option>
           <%
              }
           else{
            %>  
           <option value="<%=key.get("MONTH_ID")%>"><%=key.get("MONTH_ID").toString()%></option>
            <%
            }
            }
            %>    
          </select>	
          </td>
         <td>区域:
		   <input type="hidden""  id=zoneCode         name="zoneCode" />
		   <input type="text"    id="zone"            name="zone" /></td>
           <td>&nbsp;&nbsp;&nbsp;投诉原因:
          <select id="reasonId"    name="reasonId" style="width: 140px;">
          <option value="" selected>所有</option>
           <%
           	for (Map<String, Object> key : reasonList) {
           	if(reasonId.equals(key.get("CMPL_REASON1_ID"))){
           %>
           <option value="<%=key.get("CMPL_REASON1_ID")%>" selected><%=key.get("CMPL_REASON1_NAME").toString()%></option>
           <%
              }
           else{
            %>  
           <option value="<%=key.get("CMPL_REASON1_ID")%>"><%=key.get("CMPL_REASON1_NAME").toString()%></option>
            <%
            }
            }
            %>    
          </select>	
          </td>
           <td>&nbsp;&nbsp;&nbsp;投诉渠道:
          <select id="channelType"    name="channelType" style="width: 130px;">
          <option value="" selected>所有</option>
           <%
           	for (Map<String, Object> key : channelList) {
           	if(channelType.equals(key.get("CHANNEL_TYPE_ID"))){
           %>
           <option value="<%=key.get("CHANNEL_TYPE_ID")%>" selected><%=key.get("CHANNEL_TYPE_NAME").toString()%></option>
           <%
              }
           else{
            %>  
           <option value="<%=key.get("CHANNEL_TYPE_ID")%>"><%=key.get("CHANNEL_TYPE_NAME").toString()%></option>
            <%
            }
            }
            %>    
          </select>	
          </td>
	       <td><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td>&nbsp;&nbsp;&nbsp;&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导出报表" onclick="impExcel();"         style="width:70px;" /></td>
	      <td>&nbsp;指标项:
          <select id="ind"    name="ind" style="width: 140px;" onchange="changeCol(this);">
           <option value="NUM1" selected>越级投诉量</option>  
           <option value="NUM3">越级投诉率</option> 
          </select>		
          </td>
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
													<div id='chartdiv1'></div>
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
<script type="text/javascript" src="cmplReasonProblem_Mon.js"></script>