<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,
                                java.util.Calendar,
                                tydic.portalCommon.DateUtil,
                                tydic.portalCommon.procedure.ts.skip.CmplGxbMonAction"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <%@include file="../../../../public/head.jsp" %>
    <%@include file="../../../../public/include.jsp" %>
    <title><%=menuName %></title>
    <meta http-equiv="pragma" content="no-cache" />
    <script type="text/javascript" src="<%=path%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/CmplGxbMonAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript" src="<%=path%>/js/Charts/FusionCharts.js"></script> 
    <script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <%
      CmplGxbMonAction action =new CmplGxbMonAction();
      String dateTime =action.getNewMonth().substring(0,7);
      dateTime=dateTime.replaceAll("-","");
      String[] months =DateUtil.getAddMonths(30,dateTime);
      
      String areaCode ="0000";
      String zoneCode =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
      String userCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
      
      String ind =(String)request.getParameter("ind")==null?"102":(String)request.getParameter("ind");
    %>
</head>
<body style="overflow: visible;">
   <div id="div_src" style="position: absolute; width: 100%; height:100%;z-index: 1000;top: 0px; left: 0px; background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:scroll;"
	                  title="" class="dragBar">
	  <center style="color: #CCCCCC;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;">数据加载中，请稍等....</center>
   </div>
   <form id="queryform" name="queryform">
        <!--Excel导出参数  -->
        <%@include file="include/excelParam.jsp" %>   
        <input type="hidden" id="cid"  name="cid"   />
	    <input type="hidden" id="cid1"  name="cid1"   />
	    <input type="hidden" id="userCode"    name="userCode" value="<%=userCode %>"/> 
        <input type="hidden" id="userCodeData"    name="userCodeData" />
	    <table width='100%'  border='0' cellpadding='0px' cellspacing='0px'>
				   <tr>
						<td valign="top" width="100%">
						   <!-- 查询条件 -->
	                       <%@include file="include/selectCond.jsp" %>     
                        </td>
			       </tr>
			       <tr>
			          <td width="100%"> 	
			               <!-- 顶部图形 -->	  		                  
                           <%@include file="include/chart.jsp" %>     
				      </td>
				   </tr>
			       <tr>
			          <td width="100%"> 
			                <!-- 数据 -->
		                   <div id="dataTable" style="margin: 0px;padding: 0px;width: 100%;height: auto;">
		                   </div>
                       </td>
                   </tr>   
	    </table>
   </form>
  <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript">
function impExcel(form){
		dhx.showProgress("正在执行......");
		exportImage(); 
		window.setTimeout("exportExcel()",8000);
	    window.setTimeout("dhx.closeProgress()",8000);
	}
</script>
<script type="text/javascript" src="cmplGxb.js"></script>