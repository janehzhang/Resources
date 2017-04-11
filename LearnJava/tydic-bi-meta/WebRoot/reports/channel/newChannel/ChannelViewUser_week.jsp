<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.util.*"%>
<%@page import="tydic.reports.channel.newChannel.NewChannelDao"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
    NewChannelDao dao=new NewChannelDao();
	 String areaCode="0000";
	 String zoneCode = request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
	 String queryType= (String)request.getParameter("queryType");
	 List<Map<String, Object>> weekList=dao.getWeekList2("CS_CHANNEL_VIEW");
%>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/NewTwoChannelAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplIndexAction.js"></script>
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
		var queryType = '<%= queryType%>';
 </script>
	
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />
         <input type="hidden" id="excelCondition"    name="excelCondition"     />
         <input type="hidden" id="filePath"    name="filePath" value="/upload/excelHeader/twoChannel_Day.xls" />
          <input type="hidden" id="row"    name="row" value="5" />
          <input type="hidden" id="explain"    name="explain"   />
	    <table width='300%' >
					<tr>
						<td valign="top" width="300%">
							   <table  width='100%'  border='0'  style='border: 1px solid #87CEFF;'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="1050"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		<td>&nbsp;&nbsp;&nbsp;周期:
          <select id="dateTime"    name="dateTime" style="width: 150px;"> 
         <% for (Map<String, Object> key : weekList) {
        	   if(weekList.equals(key.get("DATE_NO").toString())){
           %>
           <option value="<%=key.get("DATE_NO")%>" selected><%=key.get("DATE_NO").toString() %></option>
               <%}else{%>
               <option value="<%=key.get("DATE_NO")%>"><%=key.get("DATE_NO").toString() %></option>
            	   <% }
            	   }%> 
          </select>
         </td> 
       <td>&nbsp;&nbsp;&nbsp;区域:
		   <input type="hidden""  id="zoneCode"  value=<%=zoneCode%>  name="zoneCode" />
		   <input type="text"     id="zone"  size="14"      name="zone" />
	  </td>
	       <td>&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td>&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="exportExcel();"         style="width:60px;" /></td>
	     </tr>
													</table>			        
										    </td>
										</tr>
							  </table>
							  </td>
							  </tr>	
			        <tr>
			          <td width="300%"> 
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
<script type="text/javascript" src="ChannelViewUser_week.js"></script>