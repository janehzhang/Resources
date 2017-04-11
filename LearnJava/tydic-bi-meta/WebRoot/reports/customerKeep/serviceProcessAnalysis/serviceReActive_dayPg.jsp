<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.customerKeep.serviceProcessAnalysis.ServiceReActiveDAO" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
    ServiceReActiveDAO dao=new ServiceReActiveDAO();
	String dateTime = request.getParameter("dateTime"); 
	String dateType = "1"; 
	if(dateTime==null || dateTime.equals("")){
		dateTime=dao.getNewDay("CS_SERVICE_RE_ACTIVE_DAY");
		String year=dateTime.substring(0,4);
		String month=dateTime.substring(4,6);
		String day=dateTime.substring(6,8);
		dateTime=year+"-"+month+"-"+day;
	}
	 request.setAttribute("dateTime", dateTime);
	// String prodId = request.getParameter("prodId")==null?"":(String)request.getParameter("prodId"); 
	 String areaCode="0000";
	 String zoneCode = request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
	 String selectCol = request.getParameter("selectCol")==null?"YD_CBS_COSTTIME":(String)request.getParameter("selectCol");
%>
<head> 
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ServiceReActiveAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
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
<div id="div_src" style="position: absolute; width: 100%; height:100%;z-index: 1000;top: 0px; left: 0px; background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:scroll;"
	                  title="" class="dragBar">
	  <center style="color: #CCCCCC;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;">数据加载中，请稍等....</center>
     </div>
	 <form id="queryform" name="queryform">
	 	<input type="hidden" id="changeCode"    name="changeCode"/>
		<input type="hidden" id="defaultZoneCode"    name="defaultZoneCode" value=<%=areaCode %>   />
		<input type="hidden""  id="zoneCode"  value=<%=zoneCode%>  name="zoneCode" />
	    <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
	    <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="cid"  name="cid"   />
        <input type="hidden" id="cid1"  name="cid1"   />
        <input type="hidden" id="dateType"    name="dateType" value="<%=dateType %>" />
        <input type="hidden" id="filePath"    name="filePath" value="/upload/excelHeader/serviceReActive_dayNew.xls" />
        <input type="hidden" id="row"    name="row" value="4" />
	    <table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%'  border='0'  style='border: 1px solid #87CEFF;'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="550"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		<td width='20%'>&nbsp;&nbsp;&nbsp;日期:
          <input name="dateTime" id="dateTime" value="${dateTime}"  readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=dateTime %>'})" ></td>
           <td width='20%'> &nbsp;&nbsp;&nbsp;区域:
		   <input type="hidden""  id="zoneCode"  value="<%=zoneCode%>"  name="zoneCode" />
		   <input type="text"     id="zone"  size="20"      name="zone" /></td>
	    <%--<td align='right'>业务类型：
		  <select id="prodId"    name="prodId" style="width: 100px;" >
           <option value="" <%if("".equals(prodId)){ %> selected <%} %>>----</option>
           <option value="10" <%if("10".equals(prodId)){ %> selected <%} %>>固话</option>
           <option value="20" <%if("20".equals(prodId)){ %> selected <%} %>>小灵通</option>
           <option value="30" <%if("30".equals(prodId)){ %> selected <%} %>>移动</option>
           <option value="40" <%if("40".equals(prodId)){ %> selected <%} %>>宽带</option>
          </select>
		</td> 
	     --%><td width='10%'>&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td width='10%'>&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
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
										<table  width='100%' height='200px' style='border: 1px solid #87CEFF;' border='0' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'><span style='font:12px;font-weight:bold;' id='titleInfo1'></span>
												</td>
												<td align='right'>指标项：
												   <select id="selectCol"    name="selectCol" style="width: 180px;" onchange="changeCol(this);">
										           <option value="YD_CBS_COSTTIME" <%if("YD_CBS_COSTTIME".equals(selectCol)){ %> selected <%} %>>移动CBS环节平均历时</option>
										           <option value="YD_CRM_COSTTIME" <%if("YD_CRM_COSTTIME".equals(selectCol)){ %> selected <%} %>>移动CRM环节平均历时</option>
										           <option value="YD_SPS_COSTTIME" <%if("YD_SPS_COSTTIME".equals(selectCol)){ %> selected <%} %>>移动SPS环节平均历时</option>
										           <option value="YD_TOTAL_AVG_COSTTIME" <%if("YD_TOTAL_AVG_COSTTIME".equals(selectCol)){ %> selected <%} %>>移动环节平均总历时</option>
										           <option value="KD_CBS_COSTTIME" <%if("KD_CBS_COSTTIME".equals(selectCol)){ %> selected <%} %>>宽带CBS环节平均历时</option>
										           <option value="KD_CRM_COSTTIME" <%if("KD_CRM_COSTTIME".equals(selectCol)){ %> selected <%} %>>宽带CRM环节平均历时</option>
										           <option value="KD_SPS_COSTTIME" <%if("KD_SPS_COSTTIME".equals(selectCol)){ %> selected <%} %>>宽带SPS环节平均历时</option>
										           <option value="KD_TOTAL_AVG_COSTTIME" <%if("KD_TOTAL_AVG_COSTTIME".equals(selectCol)){ %> selected <%} %>>宽带环节平均总历时</option>
										          </select>
												</td>
											</tr>
											<tr>
												<td colspan='2'>
													<div id='chartdiv2'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table width='100%' height='200px' border='0' style='border: 1px solid #87CEFF;' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'>
												<span style='font:12px;font-weight:bold;' id='titleInfo2'></span>
												</td>
												<td align='right'>
												<input type='button' id='city' name='city' class='poster_btn1' value='横向对比'  onclick="lookCity(this)" style='width:70px;'/>
												</td>
											 </tr>
											<tr>
												<td colspan='2'>
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
<script type="text/javascript" src="serviceReActive_dayPg.js"></script>