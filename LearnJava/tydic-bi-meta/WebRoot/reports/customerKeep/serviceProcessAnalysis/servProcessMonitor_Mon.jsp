<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.customerKeep.serviceProcessAnalysis.ServiceReActiveDAO" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
    ServiceReActiveDAO dao=new ServiceReActiveDAO();
    String month = request.getParameter("dateTime");
	if(month==null || month.equals("")){
		 month=dao.getNewMonth("CS_SERVICE_MONITOR_MON");
	}
	 String areaCode="0000";
	 String zoneCode = request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
	 String userCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
	 String prodTypeCode = request.getParameter("prodTypeCode")==null?"":(String)request.getParameter("prodTypeCode");
	 String indId = request.getParameter("indId")==null?"1":(String)request.getParameter("indId");
	 List<Map<String, Object>> monList=dao.getSelectMon("CS_SERVICE_MONITOR_MON");
	 List<Map<String, Object>> prodTypeList=dao.getSelectProdType("CS_SERVICE_MONITOR_MON");
	 List<Map<String, Object>> indList=dao.getSelectIndType("CS_SERVICE_MONITOR_MON");
	 String parameters=dao.getParameters("30011");
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
	<style type="text/css"> 
		.unl {
	    text-decoration:underline;
		color: #0000FF;
		cursor: pointer;
	}
	</style>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
 <div id="div_src" style="position: absolute; width: 100%; height:100%;z-index: 1000;top: 0px; left: 0px; background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:scroll;"
	                  title="" class="dragBar">
	  <center style="color: #CCCCCC;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;">数据加载中，请稍等....</center>
 </div>
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />
        <input type="hidden" id="parameters"    name="parameters" value=<%=parameters%> />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="cid"  name="cid"   />
        <input type="hidden" id="cid1"  name="cid1"   />
        <input type="hidden" id="defaultZoneCode"    name="defaultZoneCode" value=<%=areaCode %>   />
        <input type="hidden" id="changeCode"    name="changeCode"/>
        <input type="hidden" id="userCode"    name="userCode" value="<%=userCode %>"/>
	    <table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%' style='border: 1px solid #87CEFF;' border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="950"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		<td width='15%'>&nbsp;&nbsp;&nbsp;月份:
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
       <td width='15%'>&nbsp;&nbsp;区域:
		   <input type="hidden""  id="zoneCode"  value=<%=zoneCode%>  name="zoneCode" />
		   <input type="text"     id="zone"  size="14"      name="zone" />
	  </td>
	  <td width='15%'>&nbsp;&nbsp;业务类型:
          <select id="prodTypeCode"    name="prodTypeCode" style="width: 100px;"> 
           <option value="" >------</option>
          <% for (Map<String, Object> key: prodTypeList){
          if(prodTypeCode.equals(key.get("PROD_TYPE_ID"))){
           %>
          <option value="<%=key.get("PROD_TYPE_ID")%>" selected><%=key.get("PROD_TYPE_NAME").toString()%></option>
          <%}else{ %>
             <option value="<%=key.get("PROD_TYPE_ID")%>" ><%=key.get("PROD_TYPE_NAME").toString()%></option>
          <% 
          }
          }%>  
          </select>
         </td>
	       <td width='10%'>&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td width='10%'>&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
	     <%--<td>&nbsp;&nbsp;
	     <select id="indId"    name="indId" style="width: 170px;" onchange="changeCol(this);"> 
          <% for (Map<String, Object> key: indList){
          if(indId.equals(key.get("IND_ID"))){
           %>
          <option value="<%=key.get("IND_ID")%>" selected><%=key.get("IND_NAME").toString()%></option>
          <%}else{ %>
             <option value="<%=key.get("IND_ID")%>" ><%=key.get("IND_NAME").toString()%></option>
          <% 
          }
          }%>  
          </select>
          </td> 
	      <td>&nbsp;&nbsp;
	      <input type="button" id="city" name="city" class="poster_btn"  value='切换地市' onclick="lookCity(this);" style="width:80px;"/></td>    
													                --%>
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
												<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'><span style='font:12px;font-weight:bold;' id='titleInfo1'></span>
												</td>
											    <td align='right'>指标项：										    
												   <select id="indId"    name="indId" style="width: 170px;" onchange="changeCol(this);"> 
											          <% for (Map<String, Object> key: indList){
											          if(indId.equals(key.get("IND_ID"))){
											           %>
											          <option value="<%=key.get("IND_ID")%>" selected><%=key.get("IND_NAME").toString()%></option>
											          <%}else{ %>
											             <option value="<%=key.get("IND_ID")%>" ><%=key.get("IND_NAME").toString()%></option>
											          <% 
											          }
											          }%>  
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
										<table style='border: 1px solid #87CEFF;' width='100%' height='200px' border='0' cellpadding='0' cellspacing='0'>
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
<script type="text/javascript" src="servProcessMonitor_Mon.js"></script>