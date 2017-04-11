<%@ page import="tydic.meta.web.session.SessionManager" import="java.util.*" %>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:v="urn:schemas-microsoft-com:vml">
<%
String rootPath = request.getContextPath();
String servName=request.getServerName();
String basePath = request.getScheme()+"://"+servName+":"+request.getServerPort()+rootPath;
String designerUrl=basePath+request.getServletPath().replace("report.jsp","")+"designer.jsp?previewRpt=report&rptId=";

boolean isCustUser=SessionManager.isCustomer(session.getId());;
int userId=SessionManager.getCurrentUserID(session.getId());
HashMap<String,Object> useZoneInfo=(HashMap<String,Object>) session.getAttribute("zoneInfo");
System.out.println(useZoneInfo);
%>
<head>
<title>查看报表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<link rel="stylesheet" href="style.css" type="text/css" />
 
<style type="text/css"> 
.FixedTitleRow 
{ 
    position: relative; 
    top: expression(this.parentNode.parentNode.parentNode.parentNode.scrollTop); 
	z-index:2;
}
.FixedTitleColumn 
{ 
	position: relative; 
    left: expression(this.parentNode.parentNode.parentNode.parentNode.parentNode.scrollLeft);
    z-index:3;  
}
.FixedDataColumn 
{ 
	position: relative; 
    z-index:1;
    left: expression(this.parentNode.parentNode.parentNode.parentNode.parentNode.scrollLeft);  
} 
</style>
 <script type="text/javascript" src="js/dhtmlx/dhtmlxcommon.js"></script>
<script type="text/javascript" src="js/dhtmlx/dhtmlx.js"></script> 
 

<script type='text/javascript' src='<%=rootPath%>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=rootPath%>/dwr/util.js'></script>
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/ReportDesignerAction.js"></script>

<link rel="stylesheet" href="js/dhtmlx/dhtmlx.css" type="text/css" />
<script type="text/javascript" src="js/common/basic.js"></script>
<script type="text/javascript" src="js/common/Common.js"></script>
<script type="text/javascript" src="js/common/DateFun.js"></script>
<script type="text/javascript" src="js/common/Drag.js"></script>
<script type="text/javascript" src="js/common/EnterToTab.js"></script>
<script type="text/javascript" src="js/common/formatNumber.js"></script>

<script type="text/javascript" src="js/common/httpRequest.js"></script>
<script type="text/javascript" src="js/common/OPString.js"></script>
<script type="text/javascript" src="js/common/TableMouse.js"></script>
<script type="text/javascript" src="js/common/Valid.js"></script>
<script type="text/javascript" src="js/common/json.js"></script>

</head>
<body scroll=no > 
<table border=0 width=100% height=100% style="padding: 0px;" cellpadding="0px" cellspacing="0px">
<tr id="rptTitileTr" height=20px style="display:none"><td   valign="middle" > 
<div id="rptTitileDiv"  style="background-color:#0Ff000;padding-top:10px;border:1px;">标题栏</div>
</td></tr>
<tr id="rptTermTr" style="display:none" height=20px><td ><div id="rptTermDiv">报表条件栏</div></td></tr>
<tr id="rptModuleTr"><td height=100% ><div id="rptModuleDiv" style="width: 100%;height:100%;overflow:auto;">
<div id="rptCanvas_Model" style="height:100%;width:100%;background-color:#F0F0f0" class="cssTest"></div>
</div></td></tr>
</table>
<div id=debugDiv style="width: 100%;height:100px;display:none;"></div>
<div id=flowDivDyContent style="width: 100%;height:100px;display:none;"></div>
<iframe  id="downLoadIframe" name="downLoadIframe" style="display: none;width:0px;height:0px;"></iframe>
<form id=reportDownForm target="downLoadIframe"  method="post" action="reportDown.jsp" style="display: none">
<input id=reportId_input type="text" name="reportId_input"  />
<textarea id=reportConfig_input name="reportConfig_input" rows="" cols=""></textarea>
<textarea id=reportParams_input  name="reportParams_input" rows="" cols=""></textarea>
<input id=reportDownTab_input name="reportDownTab_input" type="text" />
<input id=reportTitle_input name="reportTitle_input" type="text" />
</form>
<div id='bodyConter' style="position:absolute;top:0px;left:0px;height:100%;width:100%;right:0px;bottom:0px;z-index=-1111;" onresize="bodyResize()"></div>
</body>
</html>

<script type="text/javascript">
var debug=getQuery("debug");
var rptId=getQuery("rptId");		//	报表配置ID
var initType=getQuery("initType");		//	报表配置ID
if(!initType)initType=0;
var isCustUser=<%=isCustUser%>;
var userId=<%=userId%>;
var user_local='<%=useZoneInfo.get("zoneCode")%>';//用户所属本地网 {zoneParId=0, zoneDesc=null, zoneCode=0000, areaId=0, zoneName=四川省, dimLevel=1, dimTypeId=4, zoneId=1}
var user_area=<%=useZoneInfo.get("areaId")%>;//用户所属营业区
var user_zone=(user_area?user_area:user_local)+"";	//	用户地域编码

var previewRpt=getQuery("previewRpt");
previewRpt=(previewRpt=="designer"?true:false);
var designerUrl="<%=designerUrl%>";
var privRowCount=500;	//	记录条数
if(!previewRpt && !rptId)
{
	alert("参数错误");
	history.back();
	window.close();
}
var dataReadOnly=false;	//	标识数据是否只读
if(isCustUser)dataReadOnly=true;
var rptWins = new dhtmlXWindows();
var imageRoot="./";
rptWins.setImagePath(imageRoot+"images/menuIcons/imgs/");
rptWins.enableAutoViewport(false);
rptWins.attachViewportTo("rptCanvas_Model"); 

if(debug)$("debugDiv").style.display="";
function Debug(msg)
{
	if(debug)
	{
		if(typeof msg=="string")
			debugDiv.innerHTML=msg+"<br/>"+debugDiv.innerHTML;
		else
		{
			if(typeof msg=="object")
			{
				if(msg.sort)
					debugDiv.innerHTML=msg+"<br/>"+debugDiv.innerHTML;
				else
				{
					for(var obj in msg)
						debugDiv.innerHTML=obj+":"+msg[obj]+"<br/>"+debugDiv.innerHTML;
				}
			}
		}
	}
}
</script>
<script type="text/javascript" src="js/report/AppVars.js"></script> 
<script type="text/javascript" src="js/config/reportAttrConfig.js"></script>

<script type="text/javascript" src="js/logic/pubFun.js"></script> 
<script type="text/javascript" src="js/report/buildReportShow.js"></script> 

<script type="text/javascript" src="js/logic/tabPaginationList.js"></script>

<script type="text/javascript" src="js/report/pageTermInit.js"></script> 
<script type="text/javascript" src="js/report/pageModuleInit.js"></script>
<script type="text/javascript" src="js/report/pageModuleTabInit.js"></script> 
<script type="text/javascript" src="js/logic/paramInit.js"></script> 
<script type="text/javascript" src="js/report/pageInit.js"></script> 

<script type="text/javascript">

var aa=eval("x={'b':'2','a':'1'};");
var test=[1,2,3,4,5];
window.status=aa.a+"_"+aa.b;
 
//window.setTimeout("autoStr()",5000);
var d=20120407;
var dt=new Date(d/10000,d%10000/100,d%100);
var dt2=new Date(2012,4, 8);
dt2.setDate(dt2.getDate()+30);
//alert(dt2); 
function autoStr()
{
	var startdt=new Date();
	
	var str= reportConfig.rptTitle.setSelf.toString();
	var att=eval("aa="+str);
	
	str=(JSON.stringify(reportConfig.rptTitle,toJsonFilter));
	var att=eval("aa="+str);
	//new Date(year, month, date[, hours[, minutes[, seconds[,ms]]]]) 
	var dt2=new Date();
	var m=(dt2.getHours()-startdt.getHours())*60+dt2.getMinutes()-startdt.getMinutes();
	var s=m*60+dt2.getSeconds()-startdt.getSeconds();
	var mm=s*1000+dt2.getMilliseconds()-startdt.getMilliseconds();
	var st=(mm/1000);
	//alert("完成 用时："+st + "s");
	//var rptConfig=eval("rpt="+txtContent.value);
	//alert(rptConfig);
}

</script>