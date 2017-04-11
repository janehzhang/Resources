<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@page import="tydic.reports.channel.newChannel.NewChannelDao"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
   NewChannelDao dao=new NewChannelDao();
     String monthId = request.getParameter("monthId");
     if(monthId==null || monthId.equals("")){
    	 monthId=dao.getMaxMonth("CS_CHANNEL_PREFER_SUMMARY","6");
     }
	 String areaCode="0000";
	 String zoneCode = request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
	 List<Map<String, Object>> monthList=dao.getMonList("CS_CHANNEL_PREFER_SUMMARY","6");
%>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
     <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/NewTwoChannelAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
	    function impExcel(form){
		dhx.showProgress("正在执行......");
		window.setTimeout("exportExcel()",8000);
	    window.setTimeout("dhx.closeProgress()",8000);
}   
	</script>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <div id="div_src" style="position: absolute; width: 100%; height:100%;z-index: 1000;top: 0px; left: 0px; background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:scroll;"
	                  title="" class="dragBar">
	  <center style="color: #CCCCCC;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;">数据加载中，请稍等....</center>
     </div> 
	 <form id="queryform" name="queryform">
	    <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
	    <input type="hidden" id="excelHeader"   name="excelHeader" />
        <input type="hidden" id="excelData"    name="excelData"   />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
         <input type="hidden" id="explain"    name="explain"   />
				<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%'  border='0'  style='border: 1px solid #87CEFF;'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="550"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
													               <td>&nbsp;&nbsp;&nbsp;月份:
          <select id="dateTime"    name="dateTime" style="width: 100px;">
           <% for (Map<String, Object> key : monthList) {
        	   if(monthId.equals(key.get("MONTH_ID").toString())){
           %>
           <option value="<%=key.get("MONTH_ID")%>" selected><%=key.get("MONTH_ID").toString() %></option>
               <%}else{%>
          <option value="<%=key.get("MONTH_ID")%>"><%=key.get("MONTH_ID").toString() %></option>
          <% }
          }%>    
          </select>		</td>
           <td>区域:
           <input type="hidden""  id="zoneCode" value=<%=zoneCode%> name="zoneCode" />
		   <input type="text"     id="zone"  size="17"      name="zone" />
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
		                    <div id="index_exp"  class="tips"></div>   
                       </td>
                   </tr>  
	      </table>
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="channelPreferSummary.js"></script>