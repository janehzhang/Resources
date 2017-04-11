<%--
  Created by IntelliJ IDEA.
  User: 小生太痴癫
  Date: 12-5-10
  Time: 下午5:15
  To change this template use File | Settings | File Templates.
--%>
<%@include file="head.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<div id="test1" style="height: 410px;width: 500px"></div>
<script type="text/javascript">
    var createFrame = function () {
        var iframe = document.createElement("iframe");
        iframe.name = 'tabFrame_' + menuId;
        iframe.id = "test";
        iframe.style.width = "100%";
        iframe.style.height = "80%";
        iframe.frameBorder = 0;
        iframe.scrolling = "no";
        iframe.url="test1.jsp";
        return iframe;
    }
    var layout =  new dhtmlXLayoutObject("test1", "2E")
    layout.cells("b").attachObject(createFrame());
    //    document.body.innerHTML += '<iframe id="test" width="100%;" height="80%" src="test1.jsp"></iframe>';
    var refresh = function () {
        var iframe = document.getElementById("test");
//        iframe.contentWindow.document.write('');
//        iframe.contentWindow.document.close();
//        iframe.contentWindow.location.reload();
        document.getElementById("test").contentWindow.location = "test1.jsp";
    }
</script>
<input type="button" onclick="refresh()">
</body>
</html>