<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%@include file="../../../public/head.jsp" %>
<head>
  <script type="text/javascript">
    function testFun(data){
    	if(data==0){
    	 window.parent.parent.parent.openMenu("出错数据详情","/meta/module/dim/merge/codeUploadErrorData.jsp","top",null,true);
    	}
    	if(data==1){
    	 window.parent.parent.parent.openMenu("出错数据详情","/meta/module/dim/mapping/mappUploadErrorData.jsp","top",null,true);
    	}
    }
  </script>
  <style type="text/css">
    span{
        color:red
    }
  </style>
</head>
<body style="background: #ffffff;">
  <%
      String msg=(String)request.getAttribute("errorMsg");
      if(msg==null||msg.equalsIgnoreCase(null)){
    	  msg ="对不起上传失败,这也许是您使用了excel的兼容模式!";
      }
   %> <div id="showMsg" style="padding-left: 15px"><%=msg%>
   </div>
   
</body>
</html>
