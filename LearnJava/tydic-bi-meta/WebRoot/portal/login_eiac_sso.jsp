<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8" import="tydic.frame.SystemVariable" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>单点登录</title>
    </head>
    <body>
    <%
      String requestMethod=request.getMethod();
      requestMethod=requestMethod.toUpperCase();
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("UTF-8");
      
      String orderId=(String)request.getParameter("orderId")==null?"":(String)request.getParameter("orderId");
     
    %>
	
	    <script type="text/javascript">
    window.location.href="/tydic-bi-meta/reports/customerSatisfied/noSatisDetail/ShowNotSatisDetail.jsp?orderId=<%=orderId%>";
    </script>
    </body>
</html>
