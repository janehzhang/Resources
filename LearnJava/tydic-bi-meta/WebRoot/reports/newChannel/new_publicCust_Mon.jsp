<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.util.*"%>
<%@page import="tydic.reports.newChannel.Dao.NewChannelDao"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    NewChannelDao dao=new NewChannelDao();
	String excelTitle="";
	String filePath="/upload/excelHeader/publicCustNewChannel_Mon1.xls";
	String width="";
	String zqType=(String)request.getParameter("zqType");
	String numberType=(String)request.getParameter("numberType");
	 String areaCode="0000";
	 String zoneCode = request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
	 String month = request.getParameter("dateTime");
		if(month==null || month.equals("")){
		    month=dao.getMonthData(zqType);
		}
	 List<Map<String, Object>> monList=dao.getAllMonthData(zqType);
	  
%>
<head>
    <%@include file="../../portalCommon/public/head.jsp"%>
    <%@include file="../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/NewChannelAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplIndexAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
	function impExcel(form){
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
	<style type="text/css"> 
		.unl {
	    text-decoration:underline;
		color: #0000FF;
		cursor: pointer;
	}
	</style>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />
         <input type="hidden" id="excelCondition"    name="excelCondition"     />
          <input type="hidden" id="filePath"    name="filePath" value=<%=filePath %> />
          <input type="hidden" id="row"    name="row" value="4" />
          <input type="hidden" id="zqType"    name="zqType" value=<%=zqType%> />
          <input type="hidden" id="explain"    name="explain"   />
          <input type="hidden" id="numberType"    name="numberType" value=<%=numberType %>   />
          
	    <table width="100%" >
					<tr>
						<td valign="top" width="100%"><div align="left"> 
							   </div><table  width='100%'  border='0'  style='border: 1px solid #87CEFF;'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td><div align="left"> 
		 </div>
	<table  width="500"  border="0" cellpadding="0" cellspacing="0">
		<tr>
		<td style="width: 150px;"><div align="left">&nbsp;&nbsp;&nbsp;月份: 
          <select id="dateTime" name="dateTime" style="width: 100px;">  
          <% for (Map<String, Object> key: monList){ 
          if(month.equals(key.get("MONTH_ID"))){ 
           %> 
          <option value='<%=key.get("MONTH_ID")%>' selected="selected"><%=key.get("MONTH_ID").toString()%></option> 
          <%}else{ %> 
             <option value='<%=key.get("MONTH_ID")%>'><%=key.get("MONTH_ID").toString()%></option> 
          <%  
          } 
          }%>   
          </select></div>
         </td> 
       <td style="width: 160px;">&nbsp;&nbsp;&nbsp;区域:
		   <input type="hidden""  id="zoneCode"  value=<%=zoneCode%>  name="zoneCode" />
		   <input type="text"     id="zone"  size="14"      name="zone" />
	  </td>
	     <td>&nbsp;
	     <input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();" style="width:60px;"/>
	     </td>
	     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	     <td>
	     <input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"  style="width:60px;"/>
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
<script type="text/javascript" src="js/new_publicCust_Mon.js"></script>