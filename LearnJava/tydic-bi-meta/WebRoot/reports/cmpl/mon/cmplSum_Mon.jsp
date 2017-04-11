<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date,tydic.reports.complain.monitorDay.CmplIndexDAO" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    CmplIndexDAO dao = new CmplIndexDAO();
    String maxMon=dao.getMaxMonth("CS_CMPL_SUMMARY_MON_S");
    String dateTime =(String)request.getParameter("dateTime")==null?maxMon:(String)request.getParameter("dateTime");
    String areaCode="0000";
    String zoneCode  =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
    String userCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
    String ind  =(String)request.getParameter("ind")==null?"NUM5":(String)request.getParameter("ind");
	List<Map<String, Object>> monList = dao.getMonList("CS_CMPL_SUMMARY_MON_S");
	request.setAttribute("dateTime", dateTime);
	request.setAttribute("zoneCode", zoneCode);
%>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplIndexAction.js"></script>
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
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName%> />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="cid"  name="cid"   />
        <input type="hidden" id="cid1"  name="cid1"   />
        <input type="hidden" id="defaultZoneCode"    name="defaultZoneCode" value=<%=areaCode %>   />
        <input type="hidden" id="changeCode"    name="changeCode"/>
        <input type="hidden" id="userCode"    name="userCode" value="<%=userCode %>"/> 
        <input type="hidden" id="userCodeData"    name="userCodeData" /> 
	    <table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%' style='border: 1px solid #87CEFF;'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="550"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
													               <td width='10%'>&nbsp;&nbsp;&nbsp;月份:
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
           <td width='10%'>区域:
		   <input type="hidden""  id=zoneCode value=<%=zoneCode%>    name="zoneCode" />
		   <input type="text"    id="zone"            name="zone" /></td>
	       <td width='5%'><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td width='5%'>&nbsp;&nbsp;&nbsp;&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
	     <%--<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	     &nbsp;&nbsp;&nbsp;&nbsp;指标项:
          <select id="ind"    name="ind" style="width: 200px;" onchange="changeCol(this);">
           <option value="NUM1" <% if("NUM1".equals(ind)){%> selected  <% }%>>工信部申诉量</option>  
           <option value="NUM2" <% if("NUM2".equals(ind)){%> selected  <% }%>>工信部申诉率</option>  
           <option value="NUM3" <% if("NUM3".equals(ind)){%> selected  <% }%>>越级投诉处理及时率</option>  
           <option value="NUM4" <% if("NUM4".equals(ind)){%> selected  <% }%>>本地抱怨量</option>  
           <option value="NUM5" <% if("NUM5".equals(ind)){%> selected  <% }%>>本地抱怨率</option>  
           <option value="NUM6" <% if("NUM6".equals(ind)){%> selected  <% }%>>本地投诉量</option>  
           <option value="NUM7" <% if("NUM7".equals(ind)){%> selected  <% }%>>本地投诉率</option>  
           <option value="NUM8" <% if("NUM8".equals(ind)){%> selected  <% }%>>移动投诉处理及时率</option>  
           <option value="NUM9" <% if("NUM9".equals(ind)){%> selected  <% }%>>固话投诉处理及时率</option>  
           <option value="NUM10" <% if("NUM10".equals(ind)){%> selected  <% }%>>宽带投诉处理及时率</option>  
           <option value="NUM11" <% if("NUM11".equals(ind)){%> selected  <% }%>>其他投诉处理及时率</option>  
           <option value="NUM12" <% if("NUM12".equals(ind)){%> selected  <% }%>>移动增值业务投诉率</option>  
           <option value="NUM13" <% if("NUM13".equals(ind)){%> selected  <% }%>>固网增值业务投诉率</option> 
           <option value="NUM14" <% if("NUM14".equals(ind)){%> selected  <% }%>>营销投诉率</option> 
           <option value="NUM15" <% if("NUM15".equals(ind)){%> selected  <% }%>>重复投诉率</option> 
           <option value="NUM16" <% if("NUM16".equals(ind)){%> selected  <% }%>>全业务投诉量（集团）</option> 
           <option value="NUM17" <% if("NUM17".equals(ind)){%> selected  <% }%>>全业务投诉率（集团）</option> 
          </select>		
          </td>
	      <td>&nbsp;
	      <input type="button" id="city" name="city" class="poster_btn"  value='切换地市' onclick="lookCity(this);" style="width:60px;"/></td>
													                --%></tr>
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
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'><span style='font:12px;font-weight:bold;' id='titleInfo1'></span>
												</td>
												<td align='right'>指标项:
										          <select id="ind"    name="ind" style="width: 200px;" onchange="changeCol(this);">
										           <option value="NUM1" <% if("NUM1".equals(ind)){%> selected  <% }%>>工信部申诉量</option>  
										           <option value="NUM2" <% if("NUM2".equals(ind)){%> selected  <% }%>>工信部申诉率</option>  
										           <option value="NUM3" <% if("NUM3".equals(ind)){%> selected  <% }%>>越级投诉处理及时率</option>  
										           <option value="NUM4" <% if("NUM4".equals(ind)){%> selected  <% }%>>本地抱怨量</option>  
										           <option value="NUM5" <% if("NUM5".equals(ind)){%> selected  <% }%>>本地抱怨率</option>  
										           <option value="NUM6" <% if("NUM6".equals(ind)){%> selected  <% }%>>本地投诉量</option>  
										           <option value="NUM7" <% if("NUM7".equals(ind)){%> selected  <% }%>>本地投诉率</option>  
										           <option value="NUM8" <% if("NUM8".equals(ind)){%> selected  <% }%>>移动投诉处理及时率</option>  
										           <option value="NUM9" <% if("NUM9".equals(ind)){%> selected  <% }%>>固话投诉处理及时率</option>  
										           <option value="NUM10" <% if("NUM10".equals(ind)){%> selected  <% }%>>宽带投诉处理及时率</option>  
										           <option value="NUM11" <% if("NUM11".equals(ind)){%> selected  <% }%>>其他投诉处理及时率</option>  
										           <option value="NUM12" <% if("NUM12".equals(ind)){%> selected  <% }%>>移动增值业务投诉率</option>  
										           <option value="NUM13" <% if("NUM13".equals(ind)){%> selected  <% }%>>固网增值业务投诉率</option> 
										           <option value="NUM14" <% if("NUM14".equals(ind)){%> selected  <% }%>>营销投诉率</option> 
										           <option value="NUM15" <% if("NUM15".equals(ind)){%> selected  <% }%>>重复投诉率</option> 
										           <option value="NUM16" <% if("NUM16".equals(ind)){%> selected  <% }%>>全业务投诉量（集团）</option> 
										           <option value="NUM17" <% if("NUM17".equals(ind)){%> selected  <% }%>>全业务投诉率（集团）</option> 
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
<script type="text/javascript" src="cmplSum_Mon.js"></script>