<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.healthDegree.HealthDegreeDAO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    HealthDegreeDAO dao=new HealthDegreeDAO();
	String month = request.getParameter("dateTime");
	if(month==null || month.equals("")){
		 month=dao.getMaxMonth("CS_HEALTHY_MAIN");
	}
	 List<Map<String, Object>> monList=dao.getMonList("CS_HEALTHY_MAIN");
	 String zoneCode  =(String)request.getParameter("zoneCode")==null?"755":(String)request.getParameter("zoneCode");
	 String indexId  ="".equals(request.getParameter("indexId"))||request.getParameter("indexId")==null?"0000":request.getParameter("indexId");
	 System.out.print(indexId);
%>
<head>
    <%@include file="../../portalCommon/public/head.jsp"%>
    <%@include file="../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/HealthDegreeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
		function impExcel(){
			var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
		    var queryCond="月份："+dateTime;
	   	    $("excelCondition").value=queryCond; 
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
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
       <input type="hidden" id="excelHeader"  name="excelHeader"  value=<%=menuName %> />
       <input type="hidden" id="excelData"    name="excelData"   />  
       <input type="hidden" id="excelCondition"    name="excelCondition"     />
       <input type="hidden" id="zoneCode"    name="zoneCode"  value=<%=zoneCode %>  />  
       <input type="hidden" id="indexId"    name="indexId"  value=<%=indexId %>  /> 
				<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%' style='border: 1px solid #87CEFF;' border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="400"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		<td style="width: 180px;">&nbsp;&nbsp;&nbsp;月份:
          <select id="dateTime"    name="dateTime" style="width: 120px;">
           <% for (Map<String, Object> key : monList) {  
           %>
               <% if(((String)key.get("MONTH_ID")).equals(month) ){ %>
                  <option value="<%=key.get("MONTH_ID") %>" selected="selected"><%=key.get("MONTH_ID").toString() %></option>
               <% } else{%>   
                  <option value="<%=key.get("MONTH_ID") %>"><%=key.get("MONTH_ID").toString() %></option>
               <% }%> 
           <% }%>    
          
          </select>	
          </td>
          <%--<td style="width: 160px;">&nbsp;&nbsp;&nbsp;区域:
		   <input type="hidden""  id="zoneCode"  value=<%=zoneCode%>  name="zoneCode" />
		   <input type="text"     id="zone"  size="14"      name="zone" />
	     </td>
	     --%><td style="width: 60px;"><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td style="width: 60px;"><input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
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
                       </td>
                   </tr> 
	      </table>
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="businessIndex.js"></script>