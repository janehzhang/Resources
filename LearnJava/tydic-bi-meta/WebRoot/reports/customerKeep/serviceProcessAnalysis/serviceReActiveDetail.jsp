<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.customerKeep.serviceProcessAnalysis.ServiceReActiveDAO" %>
<%@ page import="tydic.portalCommon.DateUtil,java.util.*,java.text.SimpleDateFormat,java.util.Calendar" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
   String startDate  =(String)request.getParameter("startDate");
   String endDate  =(String)request.getParameter("endDate");
   String areaCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
   String zoneCode          =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");	
   String prodId = request.getParameter("prodId")==null?"":(String)request.getParameter("prodId");  
   if(startDate ==null ||"".equals(startDate)||endDate ==null || "".equals(endDate)){
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   Date Nowdate=new Date(); 
	   endDate=formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),Nowdate.getDate()-1)); //本月当前天的前一天
	   startDate=formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),Nowdate.getDate()-1)); //本月当前天的前一天
   }
%>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript" src="openLayer.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ServiceReActiveAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
     <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />    
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
     <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	    <input type="hidden" id="excelTitle"   name="excelTitle"     value="<%=menuName %>" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />
        <input type="hidden" id="totalCount"   name="totalCount"    /> 
        <input type="hidden" id="excelCondition"    name="excelCondition"     /> 
        <input type="hidden" id="prodId"    name="prodId" value="<%=prodId %>" />
        	    <table width='100%'  border='0' cellpadding='0px' cellspacing='0px'>
					<tr>
						<td valign="top" width="100%">
							   <table style='border: 1px solid #87CEFF;' width='100%'  border='0'  cellpadding='0px' cellspacing='0px'><tr>
										     <td>
													<table id='queryCondition' width="730"  border="0" cellpadding="0" cellspacing="0">
											                <tr>
											                  <td width="7%">&nbsp;&nbsp;&nbsp;日期从:</td>
											                  <td width="12%">
											                     <input name="startDate" id="startDate" size="13" value="<%=startDate %>" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=endDate %>'})" >
											                  </td>
											                  <td width="2%">至&nbsp;&nbsp;&nbsp;</td>
											                  <td width="12%">
											                     <input name="endDate" id="endDate" size="13" value="<%=endDate %>" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=endDate %>'})" >
											                  </td>
											                  <td width="5%">&nbsp;区 域:</td>
											                  <td width="13%">
																  <input type="hidden"  id="zoneCode"   value="<%=zoneCode%>"     name="zoneCode" />
																  <input type="text"    id="zone"           name="zone"  style="width: 120px;height:18px;line-height:18px;" readonly="readonly"/>
											                  </td>	
											                   <%--<td align='right'>业务类型：
																  <select id="prodId"    name="prodId" style="width: 100px;" >
														           <option value="" <%if("".equals(prodId)){ %> selected <%} %>>----</option>
														           <option value="10" <%if("10".equals(prodId)){ %> selected <%} %>>固话</option>
														           <option value="20" <%if("20".equals(prodId)){ %> selected <%} %>>小灵通</option>
														           <option value="30" <%if("30".equals(prodId)){ %> selected <%} %>>移动</option>
														           <option value="40" <%if("40".equals(prodId)){ %> selected <%} %>>宽带</option>
														          </select>
																</td> 									                  
											                  --%><td width="10%">
											                       <input type="button"   class="poster_btn"  id="queryBtn"  name="queryBtn"   onclick="queryData();" style="width:60px;"   value="查  询" />
											                  </td>
											                  <td width="10%">
											                      &nbsp;&nbsp;&nbsp;<input type="button"   class="poster_btn"                     onclick="impExcel(this.form);"       style="width:60px;"  value="导 出" />
											                  </td>
											                </tr>
													</table>			        
										    </td>
										</tr>
							  </table>	
                              <div id="chartTable" style="margin: 0px;padding: 0px;width: 100%;height: auto;">
                             
                              </div> 
                              <center>
	                              <div id="pageDiv"   style="margin: 0px;padding: 0px;width: 100%;height: auto;"
	                               align="left" ></div>   
	                          </center>       
                          </td>
			       </tr>
	      </table>
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript">
	function impExcel(form){
		 var totalCount   =$("totalCount").value;
		 if(totalCount>32000){
		 	alert("记录大于32000，无法导出")
		 }else{
	     	    var startDate=$("startDate").value;
	     	    var endDate=$("endDate").value;
	     	    var zone=$("zone").value;
	     	    var prodId=$("prodId").value;
	     	    var queryCond="日期从:"+startDate+"    至:"+endDate+"    区域："+zone+"    业务类型："+prodId;
    			$("excelCondition").value=queryCond;
	     	    dhx.showProgress("正在执行......");
	     	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel_reActiveDetail.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
		 }
	}
</script>
<script type="text/javascript" src="serviceReActiveDetail.js"></script>