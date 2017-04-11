<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%@page import="tydic.reports.detailMarket.DetailMarketDAO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
	DetailMarketDAO dao=new DetailMarketDAO();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String dateTime=formatter.format(new Date());
	dateTime=DateUtil.addMonth(dateTime, -1);
	request.setAttribute("dateTime",DateUtil.formatToTimeNosce(dateTime));
	List<Map<String, Object>> servList=dao.getServList();
	String areaCode="0000";
	String zoneCode = request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
	String servTypeId = request.getParameter("servTypeId")==null?"901":(String)request.getParameter("servTypeId");
%>
<head>
    <%@include file="../../portalCommon/public/head.jsp"%>
    <%@include file="../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/DetailMarketAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
 
	<style type="text/css"> 
		.unl {
	    text-decoration:underline;
		color: #0000FF;
		cursor: pointer;
	}
	</style>
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
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />
         <input type="hidden" id="excelCondition"    name="excelCondition"     />
          <input type="hidden" id="explain"    name="explain"   />
         <input type="hidden" id="cid"  name="cid"   />
         <input type="hidden" id="cid1"  name="cid1"   />
          <input type="hidden" id="filePath"    name="filePath" value=<%="/upload/excelHeader/detailMarketCmlp4.xls" %> />
        <input type="hidden" id="row"    name="row" value="4" /> 
	    <table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table  width='100%'  border='0'  style='border: 1px solid #87CEFF;'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="850"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		<td style="width: 250px;">&nbsp;&nbsp;&nbsp;月份:
          <input name="dateTime" id="dateTime" value="${dateTime}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM'})" >
         </td> 
       <td>&nbsp;&nbsp;&nbsp;区域:
		   <input type="hidden""  id="zoneCode"  value=<%=zoneCode%>  name="zoneCode" />
		   <input type="text"     id="zone"  size="14"      name="zone" />
	  </td>
	  <td style="width: 250px;">&nbsp;&nbsp;&nbsp;服务类型:
          <select id="servTypeId"    name="servTypeId" style="width: 150px;"> 
          <% for (Map<String, Object> key: servList){
          if(servTypeId.equals(key.get("CMPL_PROD_TYPE_CODE"))){
           %>
          <option value="<%=key.get("CMPL_BUSI_TYPE_CODE")%>" selected><%=key.get("CMPL_BUSI_TYPE_NAME").toString()%></option>
          <%}else{ %>
             <option value="<%=key.get("CMPL_BUSI_TYPE_CODE")%>" ><%=key.get("CMPL_BUSI_TYPE_NAME").toString()%></option>
          <% 
          }
          }%>  
          </select>
         </td>
	       <td>&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td>&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
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
									<td width='100%'>
										 <table  width='100%' height='200px' style='border: 1px solid #87CEFF;' border='0' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'><span style='font:12px;font-weight:bold;' id='titleInfo1'></span>
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
							<table id='topTable' width='100%' height='auto!important' border='0' cellpadding='0px' cellspacing='2px'>
								<tr>
									<td width='100%'>
										 <table  width='100%' height='200px' style='border: 1px solid #87CEFF;' border='0' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'><span style='font:12px;font-weight:bold;' id='titleInfo1'></span>
												</td>
												</tr>
											<tr>
												<td colspan='2'>
													<div id='chartdiv2'></div>
												</td>
											</tr>
										</table>
										
									</td>
				   </tr>
				   </table>
				   </td>
				   </tr>
			        <tr>
			          <td width="50%"> 
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
<script type="text/javascript" src="cmplBussGrumble4_Mon.js"></script>