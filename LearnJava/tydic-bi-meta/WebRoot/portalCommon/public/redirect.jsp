<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%--作者:张伟--%>
<%--时间:2011-09-30--%>
<%--描述:重定向JSP，如果没有权限，用于跳转到登陆主页--%>
<%
    String rootPath = request.getContextPath();
%>
<head>
    <title></title>
</head>
<body>
<script type="text/javascript">
    window.top.location.href=<%=rootPath+"/"%>;
</script>
</body>
</html>