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
	 	<script type='text/javascript' src='<%=rootPath%>/dwr/engine.js'></script>  
		<script type='text/javascript' src='<%=rootPath%>/dwr/util.js'></script> 
		    <style type="text/css">
        .blockStyle{
            font-size:12px;
            height:22px;
            margin-top:0px;
            width:500px;
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
<form id="queryform" name="queryform">
</form>
<iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="SproblemMain.js"></script>
