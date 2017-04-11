<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date,tydic.reports.channel.allBusiness.AllChannelDAO" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
	String week = request.getParameter("weekNo");
	AllChannelDAO dao=new AllChannelDAO();
	if(week==null || week.equals("")){
	    week=dao.getMaxWeek("CS_CHANNEL_GLOBAL_DAY_NEW_Q");
	}
	String zoneCode = request.getParameter("zoneCode")==null?"0000":(String)request.getParameter("zoneCode");
	List<Map<String, Object>> weekList=dao.getWeekList2("CS_CHANNEL_VIEW");
%>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/AllChannelAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
		function impExcel(){
			var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
			var  zone=$("zone").value;//区域
		    var queryCond="查询周："+dateTime+"    区域："+zone;;
	   	    $("excelCondition").value=queryCond; 
	   	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel.jsp";//多级表头
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
	    <input type="hidden" id="excelHeader"   name="excelHeader" />
        <input type="hidden" id="excelData"    name="excelData"   />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="cid"  name="cid"   />
        <input type="hidden" id="cid1"  name="cid1"   />
        <input type="hidden" id="filePath"    name="filePath" value="/upload/excelHeader/channelKeyIndex.xls" />
          <input type="hidden" id="row"    name="row" value="3" />    
		<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%'  border='0'  style='border: 1px solid #87CEFF;'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="650"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
													                 <td>&nbsp;&nbsp;&nbsp;查询周:
          <select id="dateTime"    name="dateTime" style="width: 160px;">
           <% for (Map<String, Object> key : weekList) {
        	   if(week.equals(key.get("WEEK_NAME").toString())){
           %>
           <option value="<%=key.get("WEEK_NAME")%>" selected><%=key.get("WEEK_NAME").toString() %></option>
               <%}else{%>
               <option value="<%=key.get("WEEK_NAME")%>"><%=key.get("WEEK_NAME").toString() %></option>
            	   <% }
            	   }%>    
          </select>		</td>
           <td>区域:
		   <input type="hidden""  id="zoneCode" value=<%=zoneCode%> name="zoneCode" />
		   <input type="text"     id="zone"  size="18"      name="zone" />
	       <td><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td>&nbsp;&nbsp;&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
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
		                   <div id="chartTable" style="margin: 0px;padding: 0px;width: 100%;height: auto;"> 
		                   </div>
                       </td>
                   </tr> 
	      </table>	
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="channelKeyIndex_Week.js"></script>