
<%@ page import="tydic.meta.web.session.SessionManager" %>
<%@ page import="java.util.*" %>
<html>
      
      <!-- 
  <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
  

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "
http://www.w3.org/TR/html4/strict.dtd">

        <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "
http://www.w3.org/TR/html4/loose.dtd">

      
       -->
 <%
String rootPath = request.getContextPath();
String servName=request.getServerName();
String basePath = request.getScheme()+"://"+servName+":"+request.getServerPort()+rootPath;
String previewUrl=basePath+request.getServletPath().replace("designer.jsp","")+"report.jsp?rptId=";

boolean isCustUser=SessionManager.isCustomer(session.getId());
int userId=SessionManager.getCurrentUserID(session.getId());

HashMap<String,String> useZoneInfo=(HashMap<String,String>) session.getAttribute("zoneInfo");
System.out.println(useZoneInfo);
%>
<!-- 
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">   
-->
<head>
<title>报表设计器</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="style.css" type="text/css"></link>
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
 

<script>


</script>

<script type='text/javascript' src='<%=rootPath%>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=rootPath%>/dwr/util.js'></script>
<script type="text/javascript" src="<%=rootPath%>/dwr/interface/ReportDesignerAction.js"></script>

<link rel="stylesheet" href="js/dhtmlx/dhtmlx.css" type="text/css"></link>
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
<div id="tabContainer" style="background-color: #cccccc;width: 100%;height:100%;overflow:hidden;">
<!--菜单栏容器  -->
<div id="menubar_Contaier" onselectstart="return false"  style="height: 25px; "></div>
<!--工具栏容器-->
<div id="toolbar_Contaier" onselectstart="return false"  ></div>
<!--报表窗口容器
-->
<div id='reportContainer' style="height:100%;width: 800px;overflow:scroll;">
<div id="reportTitle_Container"  style="height:30px;background-color:#0Ff000;padding-top:5px;border:1px">标题栏</div>
<div id="reportConfig_termConter"  style="height:40px;background-color:" class="dataSrcTitleTd" type="termConter">报表条件栏----可以将条件窗口中的预览框拖入此处</div>
<div id="rptCanvas_Model" style="height:100%;width:100%;background-color:#FFffff" class="rptCanvas_Model"></div>
</div>
<!--属性窗口容器-->  
<div  id=propertyMoveHandle  class=propertyMoveHandle style="z-index: 100;top:50px;left:800px;" ></div> 
<DIV id=propertyContainer 
style="border: 1px solid #6699cc; position:absolute;left:800px;top:50px;padding-left:0px;z-index:10;height:100%;width:290px;
overflow:hidden; background-color:#FFFFFF" >
 		 <div nowrap style="width: 100%; background-color:#EBEBEB;padding-right:10px;
             vertical-align: bottom;  border-bottom: 1px solid #6699cc;
             height: 22px;" >
             <TABLE cellpadding="0" cellspacing="0" border="0">
             <TR>
             <TD  style="vertical-align: bottom;" id=property_title >&nbsp;&nbsp;&nbsp;&nbsp;属性窗口</TD>
             <TD>
             <img id="close_img"  src="images/close.png" width="16" height="14" valign="absmiddle" 
                  style="cursor:pointer;" onclick="change_property_state(true)"></TD></TR></TABLE>
            </div>
           <div style="background-color:#d4d0c8;padding-top:2px;padding-left:0px;height:25px;"
            class="property_ids" id=property_idsContainer >
           	<span  style="width:100%; background-color:#FFFFFF;height:23px;" id="obj_list">id列表</span>
           	</div>
           <div nowrap id="property_list" style="overflow:auto;height:70%;width:100%; background-color:#FFFFFF;">
           	<table height=20 width=100% cellspacing="1" cellpadding="0" border="0" class="property_table" bgcolor="#d4d0c8" id=property_table>
				<tr>
					<td height=18 style='width:10px;' nowrap bgcolor='#d4d0c8'>  </td>
					<td width='40%' align='left' nowrap bgcolor='#FFFFFF'> </td>
				 	<td width='60%' nowrap bgcolor='#FFFFFF'></td>
				</tr>
				<tr>
					<td height=18  nowrap bgcolor='#d4d0c8'> </td>
					<td width='40%' align='left' nowrap bgcolor='#FFFFFF'> </td>
				 	<td width='60%' nowrap bgcolor='#FFFFFF'></td>
				</tr>
			</table> 
			</div>
		<div  id=propertyMoveVhandle class=property_move_vhandle style="height:5"><img src='./images/tileback.gif'/></div>
 		 <div style="overflow:auto;height:10%;width:100%;padding-left:5px;" id="property_intro">
 		</div>
 	</DIV>
 </div>
<div  id=flowDivDyContent style="display: none;width:200px;height:100px;position: absolute;z-index: 1000;background-color:#E8F2FE;
			border:1px solid #1B6288;overflow: auto;" onmouseout="hideObj('flowDivDyContent')" onmouseover="this.style.display='';"></div> 
<textarea id=txtContent rows=20 cols=100 ></textarea>

</body>
</html>

<script type="text/javascript">
var debug=getQuery("debug");
var initType=getQuery("initType");	//	报表初始化类型
var rptId=getQuery("rptId");		//	报表配置ID
var modificationRpt=getQuery("previewRpt");
modificationRpt=(modificationRpt=="report"?true:false);
var parInited=false;	//	是否使用参数初始化设计器数据
var dataReadOnly=false;	//	标识数据是否只读
var isCustUser=0;		//	控制按钮权限，多模块多数据源设置 0 全部权限      1 不能添加数据源和模块 不能新创建 
if(isCustUser && initType)
	dataReadOnly=true;

var privRowCount=20;	//	预览记录条数
var userId = <%=userId%>;
var user_local='<%=useZoneInfo.get("zoneCode")%>';//用户所属本地网 {zoneParId=0, zoneDesc=null, zoneCode=0000, areaId=0, zoneName=四川省, dimLevel=1, dimTypeId=4, zoneId=1}
var user_area=<%=useZoneInfo.get("areaId")%>;//用户所属营业区
var user_zone=(user_area?user_area:user_local)+"";	//	用户地域编码

var previewUrl="<%=previewUrl%>";
var rptParamCfg=null;

rptId=rptId?rptId:0;
initType=initType?initType:0;
if(rptId || modificationRpt)
{
	parInited=true;	
}
else
{
	initType=0;
	rptId=0;
}
if(debug=="true")
	debug=true;
else 
	debug=false;
var toolbar = new dhtmlXToolbarObject("toolbar_Contaier");
var menu = new dhtmlXMenuObject("menubar_Contaier");
var rptWins = new dhtmlXWindows();
rptWins.enableAutoViewport(false);
var docWins = new dhtmlXWindows();
$("obj_list").innerHTML="<select id='ids_list' style='width:100%;' onchange='idlist_change()'></select>";


function Debug(msg)
{
	if(debug)proIntro.innerHTML=msg+"<br/>"+proIntro.innerHTML;
}
</script>
<script type="text/javascript" src="js/logic/property.js"></script>
<script type="text/javascript" src="js/logic/AppVars.js"></script>
<script type="text/javascript" src="js/config/rptModuleCfm.js"></script>
<script type="text/javascript" src="js/config/menuConfig.js"></script>
<script type="text/javascript" src="js/config/toolBar.js"></script>
<script type="text/javascript" src="js/logic/menuEvent.js"></script>
<script type="text/javascript" src="js/config/reportAttrConfig.js"></script>
<script type="text/javascript" src="js/logic/rptTermCtl.js"></script>

<script type="text/javascript" src="js/logic/pubFun.js"></script> 
<script type="text/javascript" src="js/report/buildReportShow.js"></script> 

<script type="text/javascript" src="js/logic/rptPropertyListBind.js"></script>
<script type="text/javascript" src="js/logic/rptPropertyBindChange.js"></script>
<script type="text/javascript" src="js/logic/proWin_columns.js"></script>
<script type="text/javascript" src="js/logic/proWin_colAlert.js"></script>
<script type="text/javascript" src="js/logic/proWin_dataSrc.js"></script>
<script type="text/javascript" src="js/logic/proWin_uniteCol.js"></script>

<script type="text/javascript" src="js/logic/rptCfmProperty.js"></script>
<script type="text/javascript" src="js/logic/rptDataSrcCfm.js"></script>

<script type="text/javascript" src="js/logic/rptTermCfm.js"></script>
<script type="text/javascript" src="js/logic/rptModelTab.js"></script>
<script type="text/javascript" src="js/logic/tabPaginationList.js"></script>

<script type="text/javascript" src="js/logic/paramInit.js"></script>
<script type="text/javascript" src="js/config/init.js"></script>


<script type="text/javascript" src="js/logic/contextMenuConfig.js"></script>



<script type="text/javascript">

 
var aa=eval("x={'b':'2','a':'1'};");
var test=[1,2,3,4,5];
window.status=aa.a+"_"+aa.b;
 
window.setTimeout("autoStr()",5000);
var d=20120407;
var dt=new Date(d/10000,d%10000/100,d%100);
var dt2=new Date(2012,4, 8);
dt2.setDate(dt2.getDate()+30);
//alert(dt2); 
function autoStr()
{
	return;
		var startdt=new Date();
		
		 
		
		str=(JSON.stringify(reportConfig.rptTitle,toJsonFilter));
		alert(str);
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