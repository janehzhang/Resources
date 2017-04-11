<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<%@include file="../../../../meta/public/head.jsp"%>
	<head>
		<title></title>
		<link rel="stylesheet" type="text/css"
			href="../../../../meta/resource/css/meta.css"/>
		<script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=PROBLEM_SOURCE,PROBLEM_STEP,PROBLEM_URGENCY,PROBLEM_TYPE,IS_BOOL,PROBLEM_WAY,ATTACHMENT_TYPE"></script>
        <script type="text/javascript" src="<%=rootPath%>/dwr/interface/SerProManageAction.js">
        </script>
	    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DeptAction.js"></script>
	        <script type="text/javascript">
	                 var mainProblemId=<%=request.getParameter("mainProblemId")%>;	
	       </script>
</head>
<body>
<form id="queryform" name="queryform">
</form>
<iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="problemDetail.js"></script>