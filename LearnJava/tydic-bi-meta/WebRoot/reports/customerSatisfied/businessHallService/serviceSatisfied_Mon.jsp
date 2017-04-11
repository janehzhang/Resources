<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.customerSatisfied.CustomerSatisfiedDAO"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    String month =(String)request.getParameter("dateTime");
    CustomerSatisfiedDAO dao=new CustomerSatisfiedDAO();
    if(month==null || month.equals("")){
    	month=dao.getMaxWeek_EveryType("2","2");
    }
     List<Map<String, Object>> monthList=dao.getDateTimeList_EveryType("2","2");
     String areaCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
     String zoneCode =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
     String selectCol =(String)request.getParameter("selectCol")==null?"SATISFY_NUM_LV":(String)request.getParameter("selectCol");
%>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
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
	     	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	    }    
	</script>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value="<%=menuName %>月报" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />   
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
				<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="1050"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
													               <td style="width: 220px;">&nbsp;&nbsp;&nbsp;日期:
          <select id="dateTime"    name="dateTime" style="width: 160px;">
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
           <td style="width: 160px;">区域:
		   <input type="hidden""  id="zoneCode"  value="<%=zoneCode%>"   name="zoneCode" />
		   <input type="text"     id="zone"  size="17"      name="zone" /></td>
	       <td style="width: 100px;"><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:80px;" /></td>
	     <td style="width: 80px;"><input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导出报表" onclick="impExcel();"         style="width:80px;" /></td>
	     <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <select id="selectCol"    name="selectCol" style="width: 160px;" onchange="changeCol(this);">
           <option value="V_NUM" <%if("V_NUM".equals(selectCol)){ %> selected <%} %>>回访量</option>
           <option value="V_SUCC_NUM" <%if("V_SUCC_NUM".equals(selectCol)){ %> selected <%} %>>回访成功量</option>
           <option value="V_SUCC_NUM_LV" <%if("V_SUCC_NUM_LV".equals(selectCol)){ %> selected <%} %>>回访成功率</option>
           <option value="SATISFY_NUM" <%if("SATISFY_NUM".equals(selectCol)){ %> selected <%} %>>满意量</option>
           <option value="SATISFY_NUM_LV" <%if("SATISFY_NUM_LV".equals(selectCol)){ %> selected <%} %>>满意率</option>
           <option value="NO_SATISFY_NUM" <%if("NO_SATISFY_NUM".equals(selectCol)){ %> selected <%} %>>不满意量</option>
           <option value="NO_SATISFY_NUM_LV" <%if("NO_SATISFY_NUM_LV".equals(selectCol)){ %> selected <%} %>>不满意率</option>
          </select>
          </td>
	      <td>
	     <input type="button" id="city" name="city" class="poster_btn"  value='切换地市' onclick="lookCity(this);" style="width:80px;"/></td>
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
											<tr>
												<td>
													<div id='chartdiv2'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table width='100%' height='200px' border='0' style='border: 1px solid #87CEFF;' cellpadding='0' cellspacing='0'>
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
<script type="text/javascript" src="serviceSatisfied_Mon.js"></script>