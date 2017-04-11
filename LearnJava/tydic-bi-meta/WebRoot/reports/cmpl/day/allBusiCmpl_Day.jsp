<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    String startDate = request.getParameter("startDate"); 
	String endDate = request.getParameter("endDate"); 
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String dateTime=formatter.format(new Date());
	if(startDate==null || startDate.equals("")){
		dateTime=DateUtil.lastDay(dateTime, -1);
	}  
	request.setAttribute("startDate", dateTime);
	request.setAttribute("endDate", dateTime);
	String indexId  =(String)request.getParameter("indexId"); 
	indexId=indexId==null?"10":indexId;
	String dataType = (String)request.getParameter("dataType");
    
    String areaCode="0000";
    String zoneCode  =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
    String userCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
    String prodTypeCode=(String)request.getParameter("prodTypeCode")==null?"-1":(String)request.getParameter("prodTypeCode");
	String cmplBusiTypeCode=(String)request.getParameter("cmplBusiTypeCode")==null?"1":(String)request.getParameter("cmplBusiTypeCode");
%>
<head>
    
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplIndexAction.js"></script>
     <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplBusiTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ProdTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
     <script type="text/javascript"   src="<%=rootPath%>/js/common/prodTypeTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/cmplBusiTypeTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
function impExcel(form){
		dhx.showProgress("正在执行......");
		//exportImage(); 
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
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="cid"  name="cid"   />
        <input type="hidden" id="cid1"  name="cid1"   />
        <input type="hidden" id="filePath"    name="filePath" value="/upload/excelHeader/allBusiCmpl_Day.xls" />
        <input type="hidden" id="row"    name="row" value="4" />        
        <input type="hidden" id="defaultZoneCode"    name="defaultZoneCode" value=<%=areaCode %>   />
        <input type="hidden" id="changeCode"    name="changeCode"/>
        <input type="hidden" id="userCode"    name="userCode" value="<%=userCode %>"/> 
        <input type="hidden" id="userCodeData"    name="userCodeData" />
        <input type="hidden" id="dataType"    name="dataType" value="<%=dataType%>"/>
	    <table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%'  style='border: 1px solid #87CEFF;' border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width='100%'  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		   <td width='width: 180px;'>&nbsp;&nbsp;&nbsp;日期从:
               <input name="startDate" id="startDate" value="${startDate}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=startDate %>'})" >
           </td> 
		   <td style="width: 180px;">至:
		       <input name="endDate" id="endDate" value="${endDate}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=endDate %>'})" >
		   </td>
           <td style="width: 180px;">区域:
		   <input type="hidden""  id=zoneCode value=<%=zoneCode%>    name="zoneCode"/>
		   <input type="text"    id="zone"            name="zone" /></td>
		   <td style="width: 200px;">&nbsp;产品类型:
		   <input type="hidden""  id="prodTypeCode"  value=<%=prodTypeCode%>  name="prodTypeCode"  />
		   <input type="text"    id="prodType"  name="prodType" /></td>
		   <td style="width: 200px;">&nbsp;投诉表象:
		   <input type="hidden""  id="cmplBusiTypeCode"  value=<%=cmplBusiTypeCode%>   name="cmplBusiTypeCode"/>
		   <input type="text"    id="cmplBusiType"          name="cmplBusiType"  /></td>
		   <td style="width: 200px;">&nbsp;考核对象:
			<select id="indexType" name="indexType">
					<option value="10" <% if("10".equals(indexId)){%>   selected="selected"  <% }%>>所有类型</option>
					<option value="1" <% if("1".equals(indexId)){%>   selected="selected"  <% }%>>省公司考核</option>
					<option value="0" <% if("0".equals(indexId)){%>   selected="selected"  <% }%>>分公司考核</option>
			</select>
			</td>
	       <td width='5%'><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td width='5%'>&nbsp;&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
	     <td>&nbsp;&nbsp;</td>
	     </tr>
													</table>			        
										    </td>
										</tr>
							  </table>	
 							  </td>
							  </tr>	
			        <!-- 顶部图形 -->	     
			       <tr style="display: none">
			          <td width="100%"> 			                  
							<table id='topTable' width='100%' height='auto!important' border='0' cellpadding='0px' cellspacing='2px'>
								<tr>
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='200px' border='0' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'><span style='font:12px;font-weight:bold;' id='titleInfo1'></span>
												</td>
											</tr>
											<tr>
												<td colspan='2'>
													<div style="display: none" id='chartdiv2'></div>
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
													<div style="display: none" id='chartdiv1'></div>
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
<script type="text/javascript" src="allBusiCmpl_Day.js"></script>