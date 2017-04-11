<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="tydic.reports.customerSatisfied.CustomerSatisfiedDAO"%>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
    String vTypeId=(String)request.getParameter("vTypeId");
    String month =(String)request.getParameter("dateTime");
    CustomerSatisfiedDAO dao=new CustomerSatisfiedDAO();
    if(month==null || month.equals("")){
    	month=dao.getMaxWeek_EveryTypeMod("2",vTypeId);
    }
    String excName="";
    String filePath="";
    if("0".equals(vTypeId)){
    	excName="（集团）宽带新装区域满意度月报";
    	filePath="/upload/excelHeader/serviceSatisfiedNew_Mon.xls";
    }if("1".equals(vTypeId)){
    	excName="（集团）宽带修障区域满意度月报";
    	filePath="/upload/excelHeader/serviceSatisfiedRepair_Mon.xls";
    }if("2".equals(vTypeId)){
    	excName="（集团）营业厅服务区域满意度月报";
    	filePath="/upload/excelHeader/serviceSatisfiedBank_Mon.xls";
    }if("5".equals(vTypeId)){
    	excName="（集团）10000号服务区域满意度月报";
    	filePath="/upload/excelHeader/serviceSatisfied10000_Mon.xls";
    }if("6".equals(vTypeId)){
    	excName="（集团）网厅服务区域满意度月报";
    	filePath="/upload/excelHeader/serviceSatisfiedNetHall_Mon.xls";
    }if("7".equals(vTypeId)){
    	excName="（集团）掌厅服务区域满意度月报";
    	filePath="/upload/excelHeader/serviceSatisfiedPalmHall_Mon.xls";
    }if("8".equals(vTypeId)){
    	excName="（集团）号百服务区域满意度月报";
    	filePath="/upload/excelHeader/serviceSatisfiedHB_Mon.xls";
    }
     List<Map<String, Object>> monthList=dao.getDateTimeList_EveryTypeMod("2",vTypeId);
     String areaCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
     String zoneCode =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
     String selectCol =(String)request.getParameter("selectCol")==null?"SATISFY_LV":(String)request.getParameter("selectCol");
%>
<head>
    <title><%=excName %></title>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ReportConfigAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/warning.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
		function impExcel(){
			var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
		    var  zone=$("zone").value;//区域
		    var queryCond="月份："+dateTime+"    区域："+zone;
	   	    $("excelCondition").value=queryCond; 
	 	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel.jsp";
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
	    }    
	</script>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
<div id="div_src" style="position: absolute; width: 100%; height:100%;z-index: 1000;top: 0px; left: 0px; background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:scroll;"
	                  title="" class="dragBar">
	    <center style="color: #CCCCCC;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;">数据加载中，请稍等....</center>
   </div>
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=excName%> />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   /> 
        <input type="hidden" id="vTypeId"    name="vTypeId"  value=<%=vTypeId%> />   
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="filePath"    name="filePath" value=<%=filePath %> />
        <input type="hidden" id="row"    name="row" value="4" />
        <input type="hidden" id="defaultZoneCode"    name="defaultZoneCode" value=<%=areaCode %>   />
        <input type="hidden" id="changeCode"    name="changeCode"/>
        <input type="hidden" id="defaultCode"    name="defaultCode"/>
				<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%' style='border: 1px solid #87CEFF;' border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="550"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
													               <td width='10%'>&nbsp;&nbsp;&nbsp;月份:
          <select id="dateTime"    name="dateTime" style="width: 100px;">
           <% for (Map<String, Object> key : monthList) {  
           %>
               <% if(((String)key.get("DATE_NO")).equals(month) ){ %>
                  <option value="<%=key.get("DATE_NO") %>" selected="selected"><%=key.get("DATE_NO").toString() %></option>
               <% } else{%>   
                  <option value="<%=key.get("DATE_NO") %>"><%=key.get("DATE_NO").toString() %></option>
               <% }%> 
           <% }%>    
          
          </select>	
          </td>
           <td width='10%'>区域:
		   <input type="hidden""  id="zoneCode"  value="<%=zoneCode%>" name="zoneCode" />
		   <input type="text"     id="zone"  size="17"      name="zone" /></td>
	       <td width='5%'><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td width='5%'><input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
	     <%--<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <select id="selectCol"    name="selectCol" style="width: 160px;" onchange="changeCol(this);">
          <option value="V_SUCC_NUM" <%if("V_SUCC_NUM".equals(selectCol)){ %> selected <%} %>>测评有效样本数</option>
          <option value="V_SUCC_NUM_LV" <%if("V_SUCC_NUM_LV".equals(selectCol)){ %> selected <%} %>>测评参与率</option>
          
           <option value="V_SATISFY_NUM" <%if("V_SATISFY_NUM".equals(selectCol)){ %> selected <%} %>>非常满意</option>
           <option value="SATISFY_NUM" <%if("SATISFY_NUM".equals(selectCol)){ %> selected <%} %>>满意</option>
           <option value="NO_SATISFY_NUM" <%if("NO_SATISFY_NUM".equals(selectCol)){ %> selected <%} %>>不满意</option>
           
           <option value="SATISFY" <%if("SATISFY".equals(selectCol)){ %> selected <%} %>>测评满意数</option>
           <option value="SATISFY_LV" <%if("SATISFY_LV".equals(selectCol)){ %> selected <%} %>>测评满意率</option>
          </select>
          </td>
	      <td>
	     <input type="button" id="city" name="city" class="poster_btn"  value='切换地市' onclick="lookCity(this);" style="width:80px;"/></td>
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
										<table  width='100%' height='200px' style='border: 1px solid #87CEFF;' border='0' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'><span style='font:12px;font-weight:bold;' id='titleInfo1'></span>
												</td>
												<td align='right'>
												 <select id="selectCol"    name="selectCol" style="width: 160px;" onchange="changeCol(this);">
										          <option value="V_SUCC_NUM" <%if("V_SUCC_NUM".equals(selectCol)){ %> selected <%} %>>测评有效样本数</option>
										          <option value="V_SUCC_NUM_LV" <%if("V_SUCC_NUM_LV".equals(selectCol)){ %> selected <%} %>>测评参与率</option>
										          <%--
										           <option value="V_SATISFY_NUM" <%if("V_SATISFY_NUM".equals(selectCol)){ %> selected <%} %>>非常满意</option>
										           <option value="SATISFY_NUM" <%if("SATISFY_NUM".equals(selectCol)){ %> selected <%} %>>满意</option>
										           <option value="NO_SATISFY_NUM" <%if("NO_SATISFY_NUM".equals(selectCol)){ %> selected <%} %>>不满意</option>
										           --%>
										           <option value="SATISFY" <%if("SATISFY".equals(selectCol)){ %> selected <%} %>>测评满意数</option>
										           <option value="SATISFY_LV" <%if("SATISFY_LV".equals(selectCol)){ %> selected <%} %>>测评满意率</option>
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
												<input type='button' id='city' name='city' class='poster_btn1' value='横向对比'  onclick="lookCity(this,'008','VALUE2','2')" style='width:70px;'/>
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
<script type="text/javascript" src="serviceSatisfied_Mon.js"></script>