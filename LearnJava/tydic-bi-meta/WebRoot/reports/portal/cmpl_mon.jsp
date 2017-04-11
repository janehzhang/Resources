<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.util.Date,tydic.reports.ReportsMonDAO" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    String dateTime=(String)request.getParameter("dateTime");
    String areaCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
	String zoneCode=(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
	String prodTypeCode=(String)request.getParameter("prodTypeCode")==null?"-1":(String)request.getParameter("prodTypeCode");
	String cmplBusiTypeCode=(String)request.getParameter("cmplBusiTypeCode")==null?"1":(String)request.getParameter("cmplBusiTypeCode");
    ReportsMonDAO dao=new ReportsMonDAO();
    List<Map<String, Object>> monList=dao.getSelectMon();
    if(dateTime==null || dateTime.equals("") ){
    	dateTime=dao.getMaxMon();
    }
	request.setAttribute("dateTime",dateTime);
	request.setAttribute("zoneCode",zoneCode);
	request.setAttribute("prodTypeCode",prodTypeCode);
	request.setAttribute("cmplBusiTypeCode",cmplBusiTypeCode);
%>
<head>
    <title>本地全业务抱怨率报表</title>
    <%@include file="../../portalCommon/public/head.jsp"%>
    <%@include file="../../portalCommon/public/include.jsp" %>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ReportsMonAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplBusiTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ProdTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/prodTypeTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/cmplBusiTypeTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
       	<script type="text/javascript">
		function impExcel(){
		        var prodTypeId =   $('prodTypeCode').value==''?"-1":$('prodTypeCode').value;
		        var cmplBusiTypeId =   $('cmplBusiTypeCode').value==''?"1":$('cmplBusiTypeCode').value;
	     	    var url = getBasePath()+"/reports/portal/cmpl_mon_impExcel.jsp?prodTypeId='"+prodTypeId+"'&cmplBusiTypeId='"+cmplBusiTypeId+"'";
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
<body style="height: 100%;width: 100%">
	 <form id="queryform" name="queryform">
	   <input type="hidden" name="title"    value="本地全业务抱怨率报表">
	    <input type="hidden" name="colNames" value="大区 ,地市,抱怨量,计费用户数,抱怨率,环比,同比">
		<div id="container" style="height: 100%;width: 100%">
		 <div style="margin-top: 5px" align="left"><tr><td>&nbsp;&nbsp;&nbsp;月份:
          <select id="dateTime"    name="dateTime" style="width: 100px;">
           <% for (Map<String, Object> key: monList){ 
        	   if(dateTime.equals(key.get("MONTH_ID"))){
           %>
           <option value="<%=key.get("MONTH_ID")%>" selected><%=key.get("MONTH_ID").toString()%></option>
          <%}else{ %>
           <option value="<%=key.get("MONTH_ID") %>"><%=key.get("MONTH_ID").toString() %></option>
               <%  }
               }%>    
          </select>		</td>
           <td>&nbsp;&nbsp;&nbsp;区域:
		   <input type="hidden""  id=zoneCode value=<%=zoneCode%> name="zoneCode" />
		   <input type="text"    id="zone"            name="zone" /></td>
		   <td>&nbsp;&nbsp;&nbsp;产品类型:
		   <input type="hidden""  id="prodTypeCode"  value=<%=prodTypeCode%>  name="prodTypeCode"  />
		   <input type="text"    id="prodType" name="prodType" /></td>
		   <td>&nbsp;&nbsp;&nbsp;投诉表象:
		   <input type="hidden""  id="cmplBusiTypeCode"  value=<%=cmplBusiTypeCode%>   name="cmplBusiTypeCode"/>
		   <input type="text"    id="cmplBusiType"          name="cmplBusiType"  /></td>
	       <td>&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td>&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导出报表" onclick="impExcel();"         style="width:80px;" /></td>
	      <td><input type="button" id="city" name="city" class="poster_btn"  value='切换地市' onclick="lookCity(this);" style="width:70px;"/></td></tr></div>   
	      <!-- 顶部图形 -->	
	<table> <tr> <td>  
	 <div id="chartdiv2" >	               
	 </div>        
	 </td> <td>
	 <div id="chartdiv1">	               
	 </div>               
	 </td>
	 </tr>
	 </table>
	 <div id="right_1">  
	    <div id="chartTable" ></div>  </br>  
	    <div id="index_exp"  class="tips"></div>   
	 </div>     
    </div>
		 <br/>     
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="cmpl_mon.js"></script> 
