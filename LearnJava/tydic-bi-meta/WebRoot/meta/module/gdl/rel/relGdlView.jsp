<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/GdlRelGdlAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/resource/js/flow.js"></script>
    <script type="text/javascript" src="relGdlView.js"></script>
    <script type="text/javascript" >
        //指标ID
        var gdlId = <%=request.getParameter("gdlId")!=null?request.getParameter("gdlId"):"" %>;

    </script>
    <style type="text/css">
        .Block{ margin:10px auto; padding:0; font-size:12px; }
        .Block li{ position:relative; list-style:none; float:left; width:90px; height:25px; line-height:25px; text-align:left;}
        .Block span{ position:absolute; top:5px; left:60px; display:block; width:10px; height:10px; background:#f00; border:1px solid #999;}
        #blue{ background:#E4E4E4; border:1px solid #999;}
        #gr{ background:#FFFF93; border:1px solid #999;}
        #yl{ background:#0ff; border:1px solid #999;}
    </style>
</head>
<body style="height: 100%;width: 100%">
<div id="flow_div" style="height: 100%;width: auto;"></div>
<div style="position: absolute;right: 0;top: 0; z-index: 1000; height: 40px; width: 400px;">
    <ul class="Block">
        <li>当前指标：<span></span></li>
        <li>基础指标：<span id="blue"></span></li>
        <li>复合指标：<span id="gr"></span></li>
        <li>计算指标：<span id="yl"></span></li>
    </ul>

</div>
</body>
</html>