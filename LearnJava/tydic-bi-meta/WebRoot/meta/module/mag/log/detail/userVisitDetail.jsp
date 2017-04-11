<%@page language="java" import="java.util.*"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <%@include file="../../../../public/head.jsp"    %>
    <title>用户访问详情</title>
    <meta http-equiv="pragma" content="no-cache" />
    <script type="text/javascript"  src="<%=rootPath%>/dwr/interface/LoginLogAction.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css"   />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css" />
    <%
       String  userName =new String(request.getParameter("userName").getBytes("ISO-8859-1"), "GBK");
       String  startTime=request.getParameter("startTime")==null?"":request.getParameter("startTime");
       String  endTime  =request.getParameter("endTime")==null?"":request.getParameter("endTime");
    %>
    
</head>
<script type="text/javascript">
      var userName= '<%=userName %>';
      var startTime='<%=startTime %>';
      var endTime=  '<%=endTime %>';
</script>
<body style="overflow: visible;">
   <form id="queryform" name="queryform">
        <input type="hidden" id="excelTitle"   name="excelTitle"    value="用户访问详情" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   value="用户名,部门,岗位,菜单名,菜单路径,日期"/>
        <input type="hidden" id="excelData"    name="excelData"     />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
	    <table width='100%'  border='0' cellpadding='0px' cellspacing='0px'>
					<tr>
						<td valign="top" width="100%">
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
	     	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	}
</script>
<script type="text/javascript"   src="userVisitDetail.js"></script>