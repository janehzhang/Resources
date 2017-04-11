<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="tydic.reports.customerSatisfied.CustomerSatisfiedDAO"%>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    String dateType="2";
	String month =(String)request.getParameter("dateTime");
	String vTypeId =(String)request.getParameter("vTypeId")==null?"11":(String)request.getParameter("vTypeId");
    
    CustomerSatisfiedDAO dao=new CustomerSatisfiedDAO();
    if(month==null || month.equals("")){
    	month=dao.getMaxDateTime_CustomerSatisfiedSum("2",vTypeId);
    }
    String filePath="/upload/excelHeader/noSatisfiedReasonInstall_Week.xls";
    String areaCode="0000";
    String zoneCode =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
    List<Map<String, Object>> monthList=dao.getDateListSum("2");
    
%>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/warning.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
		function impExcel(){
			var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
		    var  vTypeId=$("vTypeId").value;
		    var queryCond="月份："+dateTime;
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
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
        <input type="hidden" id="excelHeader"  name="excelHeader"  value=<%=menuName %> />
        <input type="hidden" id="excelData"    name="excelData"   />  
         <input type="hidden" id="dateType"    name="dateType"  value=<%=dateType%> /> 
         <input type="hidden" id="excelCondition"    name="excelCondition"     />
         <input type="hidden" id="filePath"    name="filePath" value=<%=filePath %> />
          <input type="hidden" id="row"    name="row" value="3" /> 
          <input type="hidden" id="vTypeId"    name="vTypeId" value="<%=vTypeId %>" /> 
				<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%' style='border: 1px solid #87CEFF;' border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="500"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		 <td style="width: 200px;">月份:
          <select id="dateTime"    name="dateTime" style="width: 160px;"> 
          <% for (Map<String, Object> key: monthList){
          if(month.equals(key.get("DATE_NO".replaceAll("_","~")))){
           %>
          <option value="<%=key.get("DATE_NO").toString().replaceAll("_","~")%>" selected><%=key.get("DATE_NO").toString().replaceAll("_","~")%></option>
          <%}else{ %>
         
             <option value="<%=key.get("DATE_NO").toString().replaceAll("_","~")%>" ><%=key.get("DATE_NO").toString().replaceAll("_","~")%></option>
          <% 
          }
          }%>  
          </select>		</td>
		   <td style="width: 70px;"><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td style="width: 70px;"><input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
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
<script type="text/javascript" src="noSatisfiedReasonInstall_Mon.js"></script>