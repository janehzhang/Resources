<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp"%>
<%@include file="../../../public/include.jsp"%>
<html>
  <head>
    <title>指标配置折线图</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=path%>/dwr/interface/ReportConfigAction.js"></script>
	<script type="text/javascript" src="guildlineInfo.js"></script>
	<script type="text/javascript">
		var tabId = window.parent.tableId;
	</script>
	<style text="text/css">
	 .btnStyle{
       padding-top:5px;
       padding-left:120px;
     }
     ul{padding:0px;margin:0px;list-style:none;}
    .divSlect{width:200px;height:100px;line-height:25px;position:absolute;display:none;}
     li{cursor:pointer;padding:0px 5px; line-height:25px;height:25px; }
     .aStyle{
       margin-left:10px;font-size:12px;cursor:pointer
     }
	</style>

  </head>
  
  <body style="overflow-x:hidden;overflow-y:auto;">
    <div id="main" style="width:100%;height:100%">
       <div id="divSlect" style="width:201px; height:100px;border:1px solid #A4BED4;display:none;">
        <div id = "title" style="width:201px;height:20px;background-color:#D5E6FD">
         <span><a class="aStyle">全选</a></span><span><a class="aStyle">反选</a></span><span><a class="aStyle">确定</a></span>
        </div>
        <div id="checkList" style="width:201px;height:78px;overflow-y:auto;background-color:#fff"></div>
        <div id="addTimeInterval"></div>
       </div>
    </div>
  </body>
</html>
