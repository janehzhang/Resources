
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">     
<head>
<title>报表设计器</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
<link rel="stylesheet" href="style.css" type="text/css"></link>
 

<style type="text/css"> 

</style>
 <script type="text/javascript" src="js/dhtmlx/dhtmlxcommon.js"></script>
<script type="text/javascript" src="js/dhtmlx/dhtmlx.js"></script> 
 

<script>


</script>
<%
 String rootPath = request.getContextPath();
 %>
<script type='text/javascript' src='<%=rootPath%>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=rootPath%>/dwr/util.js'></script>
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/ReportDesignerAction.js"></script>

<link rel="stylesheet" href="js/dhtmlx/dhtmlx.css" type="text/css"></link>
<script type="text/javascript" src="js/common/basic.js"></script>
<script type="text/javascript" src="js/common/Common.js"></script>
<script type="text/javascript" src="js/common/DateFun.js"></script>
<script type="text/javascript" src="js/common/Drag.js"></script>
<script type="text/javascript" src="js/common/EnterToTab.js"></script>

<script type="text/javascript" src="js/common/httpRequest.js"></script>
<script type="text/javascript" src="js/common/OPString.js"></script>
<script type="text/javascript" src="js/common/TableMouse.js"></script>
<script type="text/javascript" src="js/common/Valid.js"></script>
</head>
<body scroll=no >
 
<input id="inputa"/>
<input id="inputb"/> 
<div id=dDIV style="width:300px;height: 200px; position: relative"></div>
</body>
</html>
<script type="text/javascript" src="js/common/proGrid.js"></script>

<script type="text/javascript">
var debug=getQuery("debug");
var userId=0;
var dataSql="";		//	数据查询SQL
var colNames=[];	//	查询字段中文名
var dataTabName="";
var initParams=window.dialogArguments;
var initType=getQuery("rptType");	//	报表初始化类型
var rptId=getQuery("rptId");		//	报表配置ID

var parInited=false;	//	是否使用参数初始化设计器数据
var dataReadOnly=false;	//	标识数据是否只读
var isCustUser=0;		//	控制按钮权限，多模块多数据源设置 0 全部权限      1 不能添加数据源和模块 不能新创建 

</script>
 