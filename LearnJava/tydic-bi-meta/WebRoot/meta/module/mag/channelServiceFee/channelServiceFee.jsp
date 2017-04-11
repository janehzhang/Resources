<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.util.*"%>
<%@page import="tydic.reports.channel.newChannel.NewChannelDao"%>
<%@page import="tydic.reports.query.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
String dateTime=(String)request.getParameter("dateTime");
   NewChannelDao dao=new NewChannelDao();
   ChannelTypeDAO chanTypeDao = new ChannelTypeDAO();
	 String areaCode="0000";
	 List<Map<String,Object>> monList=dao.getSelectMon2("CS_CHANNEL_VIEW");
	 
	 List<Map<String,Object>> proList=chanTypeDao.terminalPath();
	 
	 if(dateTime==null || dateTime.equals("") ){
      	dateTime=dao.getNewMonth("CS_CHANNEL_VIEW");
    }
	 String zoneCode = request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
	String queryType= (String)request.getParameter("queryType");	 
	  
	  String channelTypeCode = request.getParameter("channelTypeCode")==null?"1":(String)request.getParameter("channelTypeCode");
	String channelType = request.getParameter("channelType")==null?"所有类型":(String)request.getParameter("channelType");
	
	
	
	
	
	
%>
<head>
    <%@include file="../../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/NewTwoChannelAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplIndexAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
     <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ChannelTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/channelTypeTree.js"></script>
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
         <input type="hidden" id="filePath"    name="filePath" value="/upload/excelHeader/channelServiceFee.xls" />
          <input type="hidden" id="row"    name="row" value="5" />
          <input type="hidden" id="explain"    name="explain"   />
	    <table width='100%' >
		  <tr>
			 <td valign="top" width="100%">
				 <table  width='100%'  border='0'  style='border: 1px solid #87CEFF;'  cellpadding='0px' cellspacing='0px'>
					 <tr>
					  <td>
						 <table  width="1050"  border="0" cellpadding="0" cellspacing="0">
					      <tr>
							<td>&nbsp;&nbsp;&nbsp;月份:
					          <select id="dateTime"    name="dateTime" style="width: 80px;">
					           <% for(Map<String, Object> key:monList ){  
					            if(dateTime.equals(key.get("MONTH_ID").toString())){%>
					           <option value="<%=key.get("MONTH_ID") %>" selected><%=key.get("MONTH_ID").toString() %></option>
					               <%  }else{%>
					               <option value="<%=key.get("MONTH_ID")%>"><%=key.get("MONTH_ID").toString() %></option>
					            	   <% }
					            	   }%>  
					          </select>
					          </td>
						       <td>&nbsp;&nbsp;&nbsp;区域:
								   <input type="hidden"  id="zoneCode"  value=<%=zoneCode%>  name="zoneCode" />
								   <input type="text"     id="zone"  size="14"      name="zone" />
							  </td>
							  <td>渠道类型:
								   <input type="hidden"  id="channelTypeCode"  value=<%=channelTypeCode%>   name="channelTypeCode" />
								   <input type="text"     id="channelType"  size="20"    value=<%=channelType%>  name="channelType" />
							  <td>
							  <td>产品类型: <td>
							  <td> 
							  <select id="terminalType1"  name="terminalType1" style="width: 80px;">
							   <option value="0" selected>全部</option>
					           <% for(Map<String, Object> proKey:proList ){ %>
					           <option value="<%=proKey.get("KEY_ID") %>"><%=proKey.get("PROD_TYPE1_NAME").toString() %></option>
					              
					           <%  }%>  
					          </select>
							  
							  </td>
							  
							  <td>数值类型: <td>
							  <td> 
							  <select id="number_state"  name="number_state" style="width: 80px;">
							  <option value="1" selected>服务量</option>
							   <option value="0">占比</option>
					           
					          </select>
							  
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
			          <td width="50%"> 
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
<script type="text/javascript" src="channelServiceFee.js"></script>