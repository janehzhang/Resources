<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../public/head.jsp"%>
<%@include file="../../../public/include.jsp"%>
<html>
  <head>
    <title>预警配置</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<script type="text/javascript" src="<%=path %>/dwr/interface/ReportConfigAction.js"></script>
	<script type="text/javascript" src="<%=path %>/dwr/interface/MenuAction.js"></script>
	<style type="text/css">
        .blockStyle{
            font-size:12px;
            height:22px;
            margin-top:0px;
            width:400px;
            *padding:5px;
        }
        .queryItem *{
            padding: 0px;
            margin: 0px;
            float: left;
            display:block;
        }
        .checkBox{
            margin-left:3px;
            margin-top:5px;
            height:22px;

        }
        .inputStyle{
            margin-top:5px;
            margin-left:3px;
        }
        .radioStyle{
            margin-left:-5px;
        }
        .queryInput{
            margin-left:-15px;
        }
    </style>
  </head>
	<body>
	</body>
</html>
<script type="text/javascript">
    function dump_obj(myObject) {  
	  var s = "";  
	  for (var property in myObject) {  
	   s = s + "\n "+property +": " + myObject[property] ;  
	  }  
	  alert(s);  
	} 
</script>
<script type="text/javascript" src="warningConfig.js"></script>
