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
	<script type="text/javascript" src="guildlineAtt.js"></script>
	<script type="text/javascript">
		var tabId=<%=request.getParameter("reportId")%>;
	</script>
	<style text="text/css">
	 .btnStyle{
       padding-top:5px;
       padding-left:120px;
     }
	</style>
  </head>
   
  <body style="width:100%;height:100%;overflow-x:hidden;overflow-y:auto;">
    <div id="main" style="width:100%;height:100%;">
     <div id="divSlect" style="width:201px; height:100px; display:none;background-color:#fff">
         <div id="div1" style="height: 20px;width:90px;float:left;background-color:#0472b8;margin-left:5px;margin-bottom:5px;color:#0472b8" onclick="javascript:showColor()">0472b8</div>
         <div id="div2" style="height: 20px;width:90px;float:right;background-color:#b80433;margin-bottom:5px; color:#b80433" onclick="javascript:showColor()">b80433</div>
         <div id="div3" style="height: 20px;width:90px;float:left;background-color:#7b04b8;margin-left:5px;margin-bottom:5px; color:#7b04b8" onclick="javascript:showColor()">7b04b8</div>
         <div id="div4" style="height: 20px;width:90px;float:right;background-color:#088135;margin-bottom:5px; color:#088135" onclick="javascript:showColor()">088135</div>
         <div id="div5" style="height: 20px;width:90px;float:left;background-color:#ff6600;margin-left:5px;margin-bottom:5px; color:#ff6600" onclick="javascript:showColor()">ff6600</div>
         <div id="div6" style="height: 20px;width:90px;float:right;background-color:#814608;margin-bottom:5px; color:#814608" onclick="javascript:showColor()">814608</div>
         <div id="div7" style="height: 20px;width:90px;float:left;background-color:#d8b700;margin-left:5px;margin-bottom:5px; color:#d8b700" onclick="javascript:showColor()">d8b700</div>
         <div id="div8" style="height: 20px;width:90px;float:right;background-color:#d562a3;margin-bottom:5px;color:#d562a3" onclick="javascript:showColor()">d562a3</div>
       </div>
     </div> 
 </body>
</html>
