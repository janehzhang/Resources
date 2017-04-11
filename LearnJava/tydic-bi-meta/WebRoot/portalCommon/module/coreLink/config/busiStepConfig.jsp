<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="../../../public/head.jsp"%>
		<title>权重配置</title>
		<script type="text/javascript" src="<%=rootPath%>/dwr/interface/BusiStepConfigAction.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css" />
	</head>
	<body style="height: 100%;width: 100%">
       <form id="queryform" name="queryform">
         <br>
         <div style="width:75%; margin:0 auto;">
			生命周期筛选：
			<label>
			<select name="linkName" id="linkName" onchange="selectData(this.value)">
			<option value="">全部</option>
			<option value="1000">业务咨询</option>
			<option value="1001">业务办理</option>
			<option value="1002">业务开通</option>
			<option value="1003">装移机</option>
			<option value="1004">网络质量</option>
			<option value="1005">产品质量</option>
			<option value="1006">使用提醒</option>
			<option value="1007">计费与账单</option>
			<option value="1008">充值缴费</option>
			<option value="1009">故障修复</option>
			<option value="1010">投诉处理</option>
			<option value="1011">客户关怀</option>
			</select>
			</label>
		  </div>
		  <div id="linkTable" align="center">
		  
		  </div>
		</form>		
	</body>
</html>
<script type="text/javascript" src="busiStepConfig.js"></script>