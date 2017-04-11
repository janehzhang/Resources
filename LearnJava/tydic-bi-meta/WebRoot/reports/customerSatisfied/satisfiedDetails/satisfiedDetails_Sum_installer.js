/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();
var globZoneTree = null;
//当前系统的主页Path
var base = getBasePath();
//查询条件参数
var dateTime = $("dateTime").value;
var zoneCode  =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var zoneCode2 =   $('zoneCode2').value==""?userInfo['localCode']:$("zoneCode2").value;
var zoneCode1 =   $('zoneCode1').value==""?userInfo['localCode']:$("zoneCode1").value;

var page=null;
page=Page.getInstance();
page.init();

var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCenterCode");
var zoneConverter=dhx.extend({idColumn:"zoneCode",pidColumn:"zoneParCode",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
dwrCaller.addAction("querySubZoneCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.queryProCenterCode(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
/** Tree head **/
//by qx
tZoneCode=$("zoneCode").value;
tDefaultZoneCode=$("defaultZoneCode").value;
if(tZoneCode!=tDefaultZoneCode){//下钻
    $("changeCode").value='1';
    $("defaultCode").value='1';
}else{
	$("changeCode").value='2';
	$("defaultCode").value='2';
}
var changeCode = $("changeCode").value;
var defaultCode=$("defaultCode").value;

var userInit=function(){

	clickTab();
	//1.加载地域树 
	loadZoneTreeChkBox(zoneCode,queryform);
    loadZoneTreeChkBox2(zoneCode2,queryform);
    loadZoneTreeChkBox1(zoneCode1,queryform);
    //执行查询数据
    //queryData1();
	//queryData();
   // queryData2();
}
function clickTab(){
	
	var obj= this;
	var clickTab=$("clickTab").value;
	if(clickTab=="tab1"){//月报
		obj.id="tab1"
	    changeTab1(obj);
	}else if(clickTab=="tab2"){//周
		obj.id="tab2"
		changeTab2(obj);
	}else if(clickTab=="tab3"){//日
		obj.id="tab3"
		changeTab3(obj);
	}else{
		obj.id="tab3"
	    changeTab3(obj);
	}
}
//日
function changeTab3(obj){
	var id = obj.id;
	for(var i=0;i<3;i++){
		var value = "tab"+(i+1);
		if(value!=id){
			$("info"+(i+1)).style.display="none";
			$("tab"+(i+1)).style.color="#002200";
		}else{
			$("info"+(i+1)).style.display="";
			$("tab"+(i+1)).style.color="#FFFFFF";
		}	
	}
	window.setTimeout("queryData2()",1000);	
	indexExp();
	getUserSubCode();
	
}
//周
function changeTab2(obj){
	var id = obj.id;
	for(var i=0;i<3;i++){
		var value = "tab"+(i+1);
		if(value!=id){
			$("info"+(i+1)).style.display="none";
			$("tab"+(i+1)).style.color="#002200";
		}else{
			$("info"+(i+1)).style.display="";
			$("tab"+(i+1)).style.color="#FFFFFF";
		}	
	}
	window.setTimeout("queryData()",1000);
	indexExp();
	getUserSubCode();
}
//月
function changeTab1(obj){
	var id = obj.id;
	for(var i=0;i<3;i++){
		var value = "tab"+(i+1);
		if(value!=id){
			$("info"+(i+1)).style.display="none";
			$("tab"+(i+1)).style.color="#002200";
		}else{
			$("info"+(i+1)).style.display="";
			$("tab"+(i+1)).style.color="#FFFFFF";
		}	
	}
	window.setTimeout("queryData1()",1000);	
	indexExp();
	getUserSubCode();
}
//根据触点不同变更表头
var vTypeId=$("vTypeId").value;

function buildHeader(){
	var tableHead="";
	
	 tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>地市</span></td>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>分公司</span></td>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>营服中心</span></td>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>工号</span></td>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>装维人员名称</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='5' style='width:150px'><span class='title'>总体评价</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='2' style='width:150px'><span class='title'>用户反馈已修复占比</span></td>"
			+"<td bgcolor='#cde5fd' colspan='4' style='width:300px'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:50px' rowspan='2'><span class='title'>测评满意数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>未修复</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>已修复</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当前</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>"
	        tableHead+="</tr>";
			  
	return tableHead;
}
//获取changeCode
function getChangeCode(){
	 var changeCode="";
	defaultCode=$("defaultCode").value; //by qx
	tZoneCode=$("zoneCode").value;
	tDefaultZoneCode=$("defaultZoneCode").value;
	if(tZoneCode!=tDefaultZoneCode){
		 $("changeCode").value="1";
		 changeCode="1";
	}else{
	    $("changeCode").value=defaultCode;
	    changeCode=defaultCode;
	}
	$("city").value='横向对比';
	return changeCode;
} 
//获取changeCode
function getChangeCode2(){
	 var changeCode="";
	defaultCode=$("defaultCode").value; //by qx
	tZoneCode=$("zoneCode2").value;
	tDefaultZoneCode=$("defaultZoneCode").value;
	if(tZoneCode!=tDefaultZoneCode){
		 $("changeCode").value="1";
		 changeCode="1";
	}else{
	    $("changeCode").value=defaultCode;
	    changeCode=defaultCode;
	}
	$("city2").value='横向对比';
	return changeCode;
} 
//获取changeCode
function getChangeCode1(){
	var changeCode="";
	defaultCode=$("defaultCode").value; //by qx
	tZoneCode=$("zoneCode1").value;
	tDefaultZoneCode=$("defaultZoneCode").value;
	if(tZoneCode!=tDefaultZoneCode){
		 $("changeCode").value="1";
		 changeCode="1";
	}else{   
	    $("changeCode").value=defaultCode;
	    changeCode=defaultCode;
	}
	$("city1").value='横向对比';
	return changeCode;
} 
function showdiv(obj,event,zoneName,zoneCode){
	var e=window.event||event;
	
	var target = obj;
	var parent = obj.parentNode;
	var zIndex = obj.style.zIndex;
	
	var left = target.offsetLeft;
	var top = target.offsetTop;
	while(target = target.offsetParent){
		left += target.offsetLeft;
		top += target.offsetTop;
    }
	var vTypeId=$("vTypeId").value; //触点Id
	var menuIndex="";
	var height="";
	if(vTypeId=='0'||vTypeId=='1'||vTypeId=='2'||vTypeId=='9'||vTypeId=='10'){
		menuIndex="menu";
		height="80";
	}else if(vTypeId=='5'){
		menuIndex="menu2";
		height="120";
	}else if(vTypeId=='8'){
		menuIndex="menu2";
		height="160";
	}
	document.getElementById(menuIndex).style.width = 140+'px';
	document.getElementById(menuIndex).style.height =height+'px';
	$("zoneCodeChoose").value=zoneCode;
	$("zoneNameChoose").value=zoneName;
	document.getElementById(menuIndex).style.display="block"
	document.getElementById(menuIndex).style.left=parseInt(left)-140+"px";
	document.getElementById(menuIndex).style.top =parseInt(top)-height+"px";

    e.returnValue=false;
	e.cancelBubble=true;
}

function showdiv1(obj,event,zoneName,zoneCode){
	var e=window.event||event;
	var target = obj;
	var parent = obj.parentNode;
	var zIndex = obj.style.zIndex;
	
	var left = target.offsetLeft;
	var top = target.offsetTop;
	while(target = target.offsetParent){
		left += target.offsetLeft;
		top += target.offsetTop;
    }
	var vTypeId=$("vTypeId1").value; //触点Id
	var menuIndex="";
	var height="";
	if(vTypeId=='0'||vTypeId=='1'){
		menuIndex="menu14";
		height="290";
	}else if(vTypeId=='2' ||vTypeId=='20'||vTypeId=='9'||vTypeId=='10'){
		menuIndex="menu10";
		height="330";
	}else if(vTypeId=='5'){
		menuIndex="menu12";
		height="360";
	}else if(vTypeId=='8'){
		menuIndex="menu12";
		height="390";
	}else{
		menuIndex="menu13";
		height="280";
	}
	document.getElementById(menuIndex).style.width = 200+'px';
	document.getElementById(menuIndex).style.height =height+'px';
	document.getElementById(menuIndex).style.display="block";
	$("zoneCodeChoose").value=zoneCode;
	$("zoneNameChoose").value=zoneName;
	document.getElementById(menuIndex).style.left=parseInt(left)-200+"px";
	document.getElementById(menuIndex).style.top =parseInt(top)-height+"px";
	
	e.returnValue=false;
	e.cancelBubble=true;
}
function showdiv2(obj,event,zoneName,zoneCode){
	var e=window.event||event;
	
	var target = obj;
	var parent = obj.parentNode;
	var zIndex = obj.style.zIndex;
	
	var left = target.offsetLeft;
	var top = target.offsetTop;
	while(target = target.offsetParent){
		left += target.offsetLeft;
		top += target.offsetTop;
    }
	var vTypeId=$("vTypeId2").value; //日触点Id
	var menuIndex="";
	var height="";
	if(vTypeId=='0'||vTypeId=='1'||vTypeId=='2'||vTypeId=='9'||vTypeId=='10'){
		menuIndex="menu20";
		height="80";
	}else if(vTypeId=='5'){
		menuIndex="menu22";
		height="120";
	}else if(vTypeId=='8'){
		menuIndex="menu22";
		height="160";
	}
	document.getElementById(menuIndex).style.width = 140+'px';
	document.getElementById(menuIndex).style.height =height+'px';
	document.getElementById(menuIndex).style.display="block"
	$("zoneCodeChoose").value=zoneCode;
	$("zoneNameChoose").value=zoneName;
	document.getElementById(menuIndex).style.left=parseInt(left)-140+"px";
	document.getElementById(menuIndex).style.top =parseInt(top)-height+"px";

    e.returnValue=false;
	e.cancelBubble=true;
}
function hidemenuie() {
	document.getElementById("menu").style.display = "none";
}
document.onclick=hidemenuie;
//周报
var queryData=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	var titileInfo=$("selectCol").options[$("selectCol").selectedIndex].text+"->本周值";
	$("titleInfo1").innerHTML=titileInfo;
	$("titleInfo2").innerHTML=titileInfo;
	
	var changeCode=getChangeCode();
    var map=new Object();
    map.startDate =$("dateTime").value;
    map.endDate   =$("dateTime").value;
    map.zoneCode=$('zoneCode').value==""?zoneCode:$("zoneCode").value;
    map.dateType   ="1";//日期类型--周报
    map.actType   =$("vTypeId").value; //触点名称
    map.field   =$("selectCol").value;
    map.rptId="kdzyj";
    map.changeCode=changeCode;
    dhx.showProgress("正在执行......");
    ADSLVisitUpdateListAction.getInstallerSumData(map, function (res) {
    	//$('chartdiv1').innerHTML='';
    	//$('chartdiv2').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				      /*   chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
				    	 chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
				         chart1.setDataXML(res.barChartMap);
				         chart2.setDataXML(res.lineChartMap);
				         chart1.render("chartdiv1");
				         chart2.render("chartdiv2"); */
				         $("div_src").style.display = "none";
				         $("div_src").style.zindex = "-1";
						 buildTable(res);
   });	
}
function buildTable(data){
    tableStr ="<table  id='table' width='100%'   border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    autoRowSpan($("table"),0,1);
    autoRowSpan($("table"),0,0);
    $("totalCount").value=data.allPageCount;
    page.buildPage(data.allPageCount , 1);//调用公共分页函数page.js
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	var tableHead=buildHeader();
	var  excelHeader="";
	for(var i=0;i<headColumn.length;i++)
    {
      if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	  {
         excelHeader += headColumn[i]+",";
      }
    }	
  $("excelHeader").value=excelHeader;//Excel表头
  return tableHead;
}
/**
*  构造表格数据
* @param {Object} data
*/
function tableData(dataColumn,headColumn){
  var tableData="";
  var  excelData="";
  var vTypeId=$("vTypeId").value; //触点Id
  var zoneCode=$("zoneCode").value;
  var userCode=$("userCode").value;
  var userCodeData=$("userCodeData").value;

     if(dataColumn&&dataColumn.length>0)
	    {  
	       for(var i=0;i<dataColumn.length;i++)
	       {
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	if(userCodeData.indexOf("]"+dataColumn[i].分公司_编码+"]")==-1){
		    	  for(var j=0;j<headColumn.length;j++)
	   		        {
	   		               var tempColumn=headColumn[j];  
	   		               if(tempColumn.indexOf('_')==-1)
			    		       {
	   		            	   if(tempColumn=='不满意原因排名'){
	   		            			tableData+="<td nowrap align='center' onclick=\"lookMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"
	   		            	   }else if(tempColumn=='更多维度分析'){
	   		            		   tableData +="<td><input type='button'  onclick=\"showdiv(this,event,'"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"')\"  class='poster_btn' style='width:40px;' value='"+dataColumn[i][tempColumn]+"'/></td>";
	   		            	   }else{
	   		            	       tableData +="<td nowrap style='height:30px'  align='center'>"+addWaringStyle(dataColumn[i][tempColumn], null ,tempColumn)+"</td>";
	   		            	   }
			    		             excelData += dataColumn[i][tempColumn]+"}";
			    		       } 
	   		      }
		    	}else{
			    		     for(var j=0;j<headColumn.length;j++)
			    		      {
			    		               var tempColumn=headColumn[j]; 
					    		       
			    		               if(tempColumn.indexOf('_')==-1)
					    		       {
			    		            	   if(tempColumn=='不满意原因排名'){
			    		            		tableData+="<td nowrap align='center' style='height:50px' onclick=\"lookMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		            	   }else if(tempColumn=='更多维度分析'){
			    		            		   tableData +="<td><input type='button'  onclick=\"showdiv(this,event,'"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"')\"  class='poster_btn' style='width:40px;' value='"+dataColumn[i][tempColumn]+"'/></td>";
			    		            	   }else if(tempColumn=='邀请客户数'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"','0')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		            	   }else if(tempColumn=='受理量'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"','5')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		            	   }else if(tempColumn=='测评有效样本数'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"','1')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		            	   }else if(tempColumn=='测评满意数'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"','2')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		            	   }else if(tempColumn=='不满意量'){
			    		            		tableData +="<td nowrap align='center' onclick=\"goMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"','3')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='对客户代表不满意'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','1')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='对其他不满意'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','2')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='非常满意'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','101')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='比较满意'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','102')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='不满意'){
			    		            		tableData +="<td nowrap align='center' onclick=\"returnMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','103')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		            	   }else if(tempColumn=='一般满意'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','104')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='不能正常使用'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','105')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='可正常使用'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','106')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else{
			    		            	       tableData +="<td nowrap style='heigth:30px' align='center'>"+addWaringStyle(dataColumn[i][tempColumn], null ,tempColumn)+"</td>";
			    		            	   }
					    		             excelData += dataColumn[i][tempColumn]+"}";
					    		       } 
			    		      } 
		    	}  
		    tableData +="</tr>";
		    excelData +="]";
	       }
	    }
        else
       {
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
     return tableData;
}
//月报
var queryData1=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	
	/*var titileInfo=$("selectCol1").options[$("selectCol1").selectedIndex].text+"->本月值";
	$("titleInfo11").innerHTML=titileInfo;
	$("titleInfo21").innerHTML=titileInfo;*/
	
	var changeCode=getChangeCode1();
    var map=new Object();
    map.startDate =$("dateTime1").value;
    map.endDate   =$("dateTime1").value;
    map.zoneCode=$('zoneCode1').value==""?zoneCode:$("zoneCode1").value;
    map.dateType   ="2";//日期类型--月报
    map.actType   =$("vTypeId1").value; //触点名称
    map.rptId="kdzyj";
   /* map.field   =$("selectCol1").value;*/
    map.changeCode=changeCode;
    dhx.showProgress("正在执行......");
    ADSLVisitUpdateListAction.getInstallerSumData(map, function (res) {
    	/*$('chartdiv11').innerHTML='';
    	$('chartdiv21').innerHTML='';*/
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				         chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
				    	 chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
				         chart1.setDataXML(res.barChartMap);
				         chart2.setDataXML(res.lineChartMap);
				        /* chart1.render("chartdiv11");
				         chart2.render("chartdiv21"); */
				         $("div_src").style.display = "none";
				         $("div_src").style.zindex = "-1";
						 buildTable1(res);
   });	
}
function buildTable1(data){
    tableStr ="<table  id='table1' width='100%' height='50px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader1(data.headColumn);
    tableStr+=tableData1(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable1').innerHTML=tableStr;
    autoRowSpan($("table1"),0,1);
    autoRowSpan($("table1"),0,0);
    $("totalCount").value=data.allPageCount;
    page.buildPage(data.allPageCount , 1);//调用公共分页函数page.js
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader1(headColumn){
	var tableHead=buildHeader();
	var  excelHeader="";
	for(var i=0;i<headColumn.length;i++)
    {
      if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	  {
         excelHeader += headColumn[i]+",";
      }
    }	
  $("excelHeader").value=excelHeader;//Excel表头
  return tableHead;
}
/**
*  构造表格数据
* @param {Object} data
*/
function tableData1(dataColumn,headColumn){
  var tableData="";
  var  excelData="";
  var vTypeId=$("vTypeId1").value; //触点Id
  var zoneCode=$("zoneCode1").value;
  var userCode=$("userCode").value;
  var userCodeData=$("userCodeData").value;
     if(dataColumn&&dataColumn.length>0)
	    {  
	       for(var i=0;i<dataColumn.length;i++)
	       {
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	if(userCodeData.indexOf("]"+dataColumn[i].分公司_编码+"]")==-1){
		    	  for(var j=0;j<headColumn.length;j++)
	   		        {
	   		               var tempColumn=headColumn[j];  
	   		               if(tempColumn.indexOf('_')==-1)
			    		       {
	   		            	   if(tempColumn=='不满意原因排名'){
   	   		            			tableData+="<td nowrap align='center' onclick=\"lookMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"
	   		            	   }else if(tempColumn=='更多维度分析'){
	   		            		   tableData +="<td><input type='button'  onclick=\"showdiv1(this,event,'"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"')\"  class='poster_btn' style='width:40px;' value='"+dataColumn[i][tempColumn]+"'/></td>";
	   		            	   }else{
	   		            	       tableData +="<td nowrap style='height:30px'  align='center'>"+addWaringStyle(dataColumn[i][tempColumn], null ,tempColumn)+"</td>";
	   		            	   }
			    		             excelData += dataColumn[i][tempColumn]+"}";
			    		       } 
	   		      }
		    	}else{
			    		     for(var j=0;j<headColumn.length;j++)
			    		      {
			    		               var tempColumn=headColumn[j]; 
					    		       
			    		               if(tempColumn.indexOf('_')==-1)
					    		       {
			    		            	   if(tempColumn=='不满意原因排名'){
			    		            	      tableData+="<td nowrap align='center' style='height:50px' onclick=\"lookMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		            	   }else if(tempColumn=='更多维度分析'){
			    		            		   tableData +="<td><input type='button'  onclick=\"showdiv1(this,event,'"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"')\"  class='poster_btn' style='width:40px;' value='"+dataColumn[i][tempColumn]+"'/></td>";
			    		            	   }else if(tempColumn=='邀请客户数'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"','0')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		            	   }else if(tempColumn=='受理量'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"','5')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		            	   }else if(tempColumn=='测评有效样本数'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"','1')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		            	   }else if(tempColumn=='测评满意数'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"','2')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		            	   }else if(tempColumn=='不满意量'){
			    		            		tableData +="<td nowrap align='center' onclick=\"goMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+vTypeId+"','3')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='对客户代表不满意'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','1')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='对其他不满意'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','2')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='非常满意'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','101')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='比较满意'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','102')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='不满意'){
			    		            		 tableData +="<td nowrap align='center' onclick=\"returnMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','103')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='一般满意'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','104')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='不能正常使用'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','105')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else if(tempColumn=='可正常使用'){
			    		            		   tableData +="<td nowrap align='center' onclick=\"returnMenuHref1('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','106')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
			    		            	   }else{
			    		            	       tableData +="<td nowrap style='height:30px' align='center'>"+addWaringStyle(dataColumn[i][tempColumn], null ,tempColumn)+"</td>";
			    		            	   }
					    		             excelData += dataColumn[i][tempColumn]+"}";
					    		       } 
			    		      } 
		    	}
		    tableData +="</tr>";
		    excelData +="]";
	       }
	    }
        else
       {
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
return tableData;
}

//日报
var queryData2=function(){
	var page=null;
	page=Page.getInstance();
	page.init();	
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	
	var titileInfo=$("selectCol2").options[$("selectCol2").selectedIndex].text+"->当日值";
	$("titleInfo12").innerHTML=titileInfo;
	$("titleInfo22").innerHTML=titileInfo;
	
	var changeCode=getChangeCode2();
    var map=new Object();
    map.startDate =$("startDate").value;
    map.endDate   =$("endDate").value;
    map.zoneCode=$('zoneCode2').value==""?zoneCode:$("zoneCode2").value;
    map.dateType   ="0";//日期类型--日报
    map.actType   =$("vTypeId2").value; //触点名称
    map.field   =$("selectCol2").value;
    map.rptId="kdzyj";
    
    map.pageCount=  page.pageCount;    //每页显示多少条数
    map.currPageNum=page.currPageNum;//当前第几页
    map.changeCode=changeCode;
    dhx.showProgress("正在执行......");
    ADSLVisitUpdateListAction.getInstallerSumData(map, function (res) {
    	//$('chartdiv12').innerHTML='';
    	//$('chartdiv22').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				        // chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
				    	 //chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
				         //chart1.setDataXML(res.barChartMap);
				         //chart2.setDataXML(res.lineChartMap);
				         //chart1.render("chartdiv12");
				        // chart2.render("chartdiv22"); 
				         $("div_src").style.display = "none";
				         $("div_src").style.zindex = "-1";
				         
						 buildTable2(res);
						 
   });	
}



function excuteInitData(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	
	var titileInfo=$("selectCol2").options[$("selectCol2").selectedIndex].text+"->当日值";
	$("titleInfo12").innerHTML=titileInfo;
	$("titleInfo22").innerHTML=titileInfo;
	
	var changeCode=getChangeCode2();
    var map=new Object();
    map.startDate =$("startDate").value;
    map.endDate   =$("endDate").value;
    map.zoneCode=$('zoneCode2').value==""?zoneCode:$("zoneCode2").value;
    map.dateType   ="0";//日期类型--日报
    map.actType   =$("vTypeId2").value; //触点名称
    map.field   =$("selectCol2").value;
    map.rptId="kdzyj";
    
    map.pageCount=  page.pageCount;    //每页显示多少条数
    map.currPageNum=page.currPageNum;//当前第几页
    map.changeCode=changeCode;
    dhx.showProgress("正在执行......");
    ADSLVisitUpdateListAction.getInstallerSumData(map, function (res) {
    	//$('chartdiv12').innerHTML='';
    	//$('chartdiv22').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				     /*    chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
				    	 chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
				         chart1.setDataXML(res.barChartMap);
				         chart2.setDataXML(res.lineChartMap);
				         chart1.render("chartdiv12");
				         chart2.render("chartdiv22"); */
				         $("div_src").style.display = "none";
				         $("div_src").style.zindex = "-1";
						 buildTable2(res);
   });	
}



function buildTable2(data){
    tableStr ="<table  id='table2' width='100%'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader2(data.headColumn);
    tableStr+=tableData2(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable2').innerHTML=tableStr;
    autoRowSpan($("table2"),0,1);
    autoRowSpan($("table2"),0,0);
    $("totalCount").value=data.allPageCount;
    page.buildPage(data.allPageCount , 1);//调用公共分页函数page.js
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader2(headColumn){
	var tableHead=buildHeader();
	var  excelHeader="";
	for(var i=0;i<headColumn.length;i++)
    {
      if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	  {
         excelHeader += headColumn[i]+",";
      }
    }	
  $("excelHeader").value=excelHeader;//Excel表头
  return tableHead;
}
/**
*  构造表格数据
* @param {Object} data
*/
function tableData2(dataColumn,headColumn){
  var tableData="";
  var  excelData="";
  var vTypeId=$("vTypeId2").value; //触点Id
  var zoneCode=$("zoneCode2").value;
  var userCode=$("userCode").value;
  var userCodeData=$("userCodeData").value;
 
     if(dataColumn&&dataColumn.length>0)
	    { 
	       for(var i=0;i<dataColumn.length;i++)
	       {
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";

//	    	if(userCodeData.indexOf("]"+dataColumn[i].分公司_编码+"]")==-1){
//	    		
//		    	
//		    	}else{
//			    		     for(var j=0;j<headColumn.length;j++)
//			    		      {
//			    		               var tempColumn=headColumn[j]; 
//			    		               if(tempColumn.indexOf('_')==-1)
//					    		         excelData += dataColumn[i][tempColumn]+"}";
//			    		      }
//		    	} 
	    	
	    	  for(var j=0;j<headColumn.length;j++)
 		        {
 		               var tempColumn=headColumn[j];  
 		               if(tempColumn == 'RN') continue;
 		               if(tempColumn.indexOf('_')==-1)
		    		       {
 		            	       tableData +="<td nowrap style='height:30px'  align='center'>"+addWaringStyle(dataColumn[i][tempColumn], null ,tempColumn)+"</td>";
		    		           excelData += dataColumn[i][tempColumn]+"}";
		    		       } 
 		      }
		    tableData +="</tr>";
		    excelData +="]";
	       }
	       
	    }
        else
       {
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
return tableData;
}
//第一列合并
var autoRowSpan=function(tb,row,col){//0,16
    var lastValue="";
    var value="";
    var pos=1;
    for(var i=row;i<tb.rows.length;i++)
    {
        //value = tb.rows[i].cells[col].innerText;
       // if(lastValue == value)
       // {
            //tb.rows[i].deleteCell(col);
            //tb.rows[i-pos].cells[col].rowSpan = tb.rows[i-pos].cells[col].rowSpan+1;
            //pos++;
       // }else{
            lastValue = value;
            pos=1;
       // }
    }
}
//周
var lookCity = function (obj){
    var map=new Object();
        map.startDate=$("dateTime").value;
        map.field=$("selectCol").value;
        map.dateType   ="1";//周
        map.actType   =$("vTypeId").value;//触点
        map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
        tZoneCode=$("zoneCode").value;
    	tDefaultZoneCode=$("defaultZoneCode").value;//0000 
    		if(tZoneCode!='0000') { 
			  if(obj.value=='横向对比'){//地市
				  obj.value='返回';
				  map.changeCode="0";//地市同级
				  $("changeCode").value='0';
			  }else if(obj.value=='返回'){//地市
				  obj.value='横向对比';
				  map.changeCode="1";//地市下一级
				  $("changeCode").value='1';
			  }
    		}else{
    		 if(obj.value=='横向对比'){//省公司下一级
  				  obj.value='返回';
  				  map.changeCode="1";//省公司下一级
  				$("changeCode").value='1';
  			  }else if(obj.value=='返回'){//省公司下两级
  				  obj.value='横向对比';
  				  map.changeCode="2";//省公司下两级
  				$("changeCode").value='2';
  			  }
    		}
		  dhx.showProgress("正在执行......"); 
		  CustomerSatisfiedAction.loadSet21AreaChartSum(map, {callback:function (res) {
			 dhx.closeProgress();
 	         if(res != null){
                     build21Chart(res);
		         }
		   }
		});
    	
}
function  build21Chart(map){
	   $("cid1").value=Math.random();
	   var cid1=$("cid1").value;
	  // $("chartdiv1").innerHTML="";
	   //chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
	   //chart.setDataXML(map.XML);
	  // chart.render("chartdiv1");
	}
//月
var lookCity1 = function (obj){
    var map=new Object();
        map.startDate=$("dateTime1").value;
        /*map.field=$("selectCol1").value;*/
        map.dateType   ="2";//月报
        map.actType   =$("vTypeId1").value;//触点
        map.zoneCode=$('zoneCode1').value==""?userInfo['localCode']:$("zoneCode1").value; 
        tZoneCode=$("zoneCode1").value;
    	tDefaultZoneCode=$("defaultZoneCode").value;
    	if(tZoneCode!='0000') { 
			  if(obj.value=='横向对比'){//地市
				  obj.value='返回';
				  map.changeCode="0";//地市同级
				  $("changeCode").value='0';
			  }else if(obj.value=='返回'){//地市
				  obj.value='横向对比';
				  map.changeCode="1";//地市下一级
				  $("changeCode").value='1';
			  }
  		}else{
  		     if(obj.value=='横向对比'){//省公司下一级
				  obj.value='返回';
				  map.changeCode="1";//省公司下一级
				$("changeCode").value='1';
			  }else if(obj.value=='返回'){//省公司下两级
				  obj.value='横向对比';
				  map.changeCode="2";//省公司下两级
				$("changeCode").value='2';
			  }
  		}
		  dhx.showProgress("正在执行......"); 
		  CustomerSatisfiedAction.loadSet21AreaChartSum(map, {callback:function (res) {
			 dhx.closeProgress();
 	         if(res != null){
                     build21Chart1(res);
		         }
		   }
		});
    	
}
function  build21Chart1(map){
	  /* $("cid1").value=Math.random();
	   var cid1=$("cid1").value;
	   $("chartdiv11").innerHTML="";
	   chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
	   chart.setDataXML(map.XML);
	   chart.render("chartdiv11");*/
	}
//日
var lookCity2 = function (obj){
    var map=new Object();
        map.startDate=$("startDate").value;
        map.endDate=$("endDate").value;
        map.field=$("selectCol2").value;
        map.dateType   ="0";//日报
        map.actType   =$("vTypeId2").value;//触点
        map.zoneCode=$('zoneCode2').value==""?userInfo['localCode']:$("zoneCode2").value; 
        tZoneCode=$("zoneCode2").value;
    	tDefaultZoneCode=$("defaultZoneCode").value;
    		if(tZoneCode!='0000') { 
			  if(obj.value=='横向对比'){//地市
				  obj.value='返回';
				  map.changeCode="0";//地市同级
				  $("changeCode").value='0';
			  }else if(obj.value=='返回'){//地市
				  obj.value='横向对比';
				  map.changeCode="1";//地市下一级
				  $("changeCode").value='1';
			  }
    		}else{
    		 if(obj.value=='横向对比'){//省公司下一级
  				  obj.value='返回';
  				  map.changeCode="1";//省公司下一级
  				$("changeCode").value='1';
  			  }else if(obj.value=='返回'){//省公司下两级
  				  obj.value='横向对比';
  				  map.changeCode="2";//省公司下两级
  				$("changeCode").value='2';
  			  }
    		}
		  dhx.showProgress("正在执行......"); 
		  CustomerSatisfiedAction.loadSet21AreaChartSum(map, {callback:function (res) {
			 dhx.closeProgress();
 	         if(res != null){
                     build21Chart2(res);
		         }
		   }
		});
    	
}
function  build21Chart2(map){
	   $("cid1").value=Math.random();
	   var cid1=$("cid1").value;
	  // $("chartdiv12").innerHTML="";
	   //chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
	   //chart.setDataXML(map.XML);
	   //chart.render("chartdiv12");
	}
/**
 *  打开链接菜单-查看不满意原因
 * 
 */
//周
var  lookMenuHref=function(zoneName,zoneCode,vTypeId){
	   var time=$("dateTime").value;
	   var url="/reports/customerSatisfied/noSatisfied/noSatisfiedReason_Sum.jsp?zoneCode="+zoneCode+"&dateTime="+time+"&vTypeId="+vTypeId+"&menuId="+menuId;
	   return parent.openTreeTab("133005"+zoneCode, "不满意原因排名"+"["+zoneName+"]", base+url, 'top');
}
//月
var  lookMenuHref1=function(zoneName,zoneCode,vTypeId){
	   var time=$("dateTime1").value;
	   var url="/reports/customerSatisfied/noSatisfied/noSatisfiedReason_Sum.jsp?zoneCode="+zoneCode+"&dateTime1="+time+"&vTypeId="+vTypeId+"&menuId="+menuId;
	   return parent.openTreeTab("133005"+zoneCode, "不满意原因排名"+"["+zoneName+"]", base+url, 'top');
}
//日
var  lookMenuHref2=function(zoneName,zoneCode,vTypeId){
	   var startTime=$("startDate").value;
	   var endTime=$("endDate").value;
	   var url="/reports/customerSatisfied/noSatisfied/noSatisfiedReason_Sum.jsp?zoneCode="+zoneCode+"&startDate="+startTime+"&endDate="+endTime+"&vTypeId="+vTypeId+"&menuId="+menuId;
	   return parent.openTreeTab("133005"+zoneCode, "不满意原因排名"+"["+zoneName+"]", base+url, 'top');
}
//查看满意度量值
//周
var  goMenuHref=function(zoneName,zoneCode,vTypeId,indexId){
	   var dateTime=$("dateTime").value;
	   var startTime=dateTime.substr(0,dateTime.indexOf("~"));
	   startTime =startTime.substr(0,4)+"-"+startTime.substr(4,2)+"-"+startTime.substr(6,2) ;
	   var endTime=dateTime.substr(dateTime.indexOf("~")+1,dateTime.lenght);
	   endTime =endTime.substr(0,4)+"-"+endTime.substr(4,2)+"-"+endTime.substr(6,2) ;
		  var url;
		  var tab_name="";
		  if(vTypeId=='0'){//0宽带新装
			 url="/portalCommon/module/procedure/visitDetail/ADSL_visitList.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="宽带装移机";
		  }if(vTypeId=='1'){//1宽带修障
			 url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="宽带修障";
		  }if(vTypeId=='2'){//2营业厅服务
			 url="/portalCommon/module/procedure/visitDetail/hf/ADSL_visitList.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId+"&actType="+vTypeId;
			 tab_name="实体渠道";
		  }if(vTypeId=='20'){//营业厅服务非实体
	        url="/portalCommon/module/procedure/visitDetail/hf/no_ADSL_visitList.jsp?zoneCode="+zoneCode+
		    "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId+"&actType="+vTypeId;
	        tab_name="非实体渠道";
	  	  }if(vTypeId=='3'){//3投诉处理
			 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="投诉处理";
		  }if(vTypeId=='4'){//4电子渠道(无)
			 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="电子渠道";
		  }if(vTypeId=='5'){//10000号
			 url="/portalCommon/module/procedure/visitDetail/s10000/10000_visitSatisfyList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="10000号";
		  }if(vTypeId=='6'){//6网厅
			 url="/portalCommon/module/procedure/visitDetail/swt/wt_visitSatisfyList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="网厅";
		  }if(vTypeId=='7'){//7掌厅
			 url="/portalCommon/module/procedure/visitDetail/szt/zt_visitSatisfyList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="掌厅";
		  }if(vTypeId=='8'){//8号百
			 url="/portalCommon/module/procedure/visitDetail/shb/hb_visitSatisfyList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="号百";
		  }if(vTypeId=='9'){//实体渠道评价器
			 url="/portalCommon/module/procedure/visitDetail/ishop/ADSL_visitList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId+"&actType="+vTypeId;
			 tab_name="实体渠道评价器";
	      }if(vTypeId=='10'){//实体渠道短信
	    	  url="/portalCommon/module/procedure/visitDetail/dx/ADSL_visitList.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId+"&actType="+vTypeId+"&testMethod=1";
			  tab_name="实体渠道短信";
	      }if(vTypeId=='11'){//11宽带IVR新装
			  url="/portalCommon/module/procedure/visitDetail/ADSL_visitList_ivr_zyj.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			  tab_name="宽带IVR装移机";
		  }if(vTypeId=='12'){//12宽带IVR修障
			  url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList_ivr_xz.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			  tab_name="宽带IVR修障";
		 }
		 return parent.openTreeTab("124017"+zoneCode+indexId+vTypeId, zoneName+tab_name+"即时回访清单", base+url, 'top');
}
//月 
var  goMenuHref1=function(zoneName,zoneCode,vTypeId,indexId){
	  var dateTime=$("dateTime1").value;
	  //var vTypeName=$("vTypeId1").options[$("vTypeId1").selectedIndex].text;
	  var year= dateTime.substring(0,4);
	  var month=dateTime.substring(4,6);
	  var startTime=year+"-"+month+"-01";
	  var endTime=year+"-"+month+"-"+getLastDay(year,month);
	  var url;
	  var tab_name="";
	  if(vTypeId=='0'){//0宽带新装
		 url="/portalCommon/module/procedure/visitDetail/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
		 tab_name="宽带装移机";
	  }if(vTypeId=='1'){//1宽带修障
		 url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
		 tab_name="宽带修障";
	  }if(vTypeId=='2'){//2营业厅服务
		 url="/portalCommon/module/procedure/visitDetail/hf/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId+"&actType="+vTypeId
		 tab_name="实体渠道";
	  }if(vTypeId=='20'){//营业厅服务非实体
	     tab_name="非实体渠道";
	     url="/portalCommon/module/procedure/visitDetail/hf/no_ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId+"&actType="+vTypeId;
	  }if(vTypeId=='3'){//3投诉处理
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
		 tab_name="投诉处理";
	  }if(vTypeId=='4'){//4电子渠道(无)
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
		 tab_name="电子渠道";
	  }if(vTypeId=='5'){//10000号
		 url="/portalCommon/module/procedure/visitDetail/s10000/10000_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
		 tab_name="10000号";
	  }if(vTypeId=='6'){//6网厅
		 url="/portalCommon/module/procedure/visitDetail/swt/wt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
		 tab_name="网厅";
	  }if(vTypeId=='7'){//7掌厅
		 url="/portalCommon/module/procedure/visitDetail/szt/zt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
		 tab_name="掌厅";
	  }if(vTypeId=='8'){//8号百
		 url="/portalCommon/module/procedure/visitDetail/shb/hb_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
		 tab_name="号百";
	  } if(vTypeId=='9'){//实体渠道评价器
			 url="/portalCommon/module/procedure/visitDetail/ishop/ADSL_visitList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId+"&actType="+vTypeId;
			 tab_name="实体渠道评价器";
	  }if(vTypeId=='10'){//实体渠道评价器
    	  url="/portalCommon/module/procedure/visitDetail/dx/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId+"&actType="+vTypeId+"&testMethod=1";
		  tab_name="实体渠道短信";
      }if(vTypeId=='11'){//11宽带IVR新装
		  url="/portalCommon/module/procedure/visitDetail/ADSL_visitList_ivr_zyj.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
		  tab_name="宽带IVR装移机";
	  }if(vTypeId=='12'){//12宽带IVR修障
		  url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList_ivr_xz.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
		  tab_name="宽带IVR修障";
	 }
	 return parent.openTreeTab("124017"+zoneCode+indexId+vTypeId, zoneName+tab_name+"即时回访清单", base+url, 'top');
}
//日  查看 测评有效样本数
var  goMenuHref2=function(zoneName,zoneCode,vTypeId,indexId){
	   var  startTime=$("startDate").value;//时间
	   var  endTime=$("endDate").value;//时间
	   //var vTypeName=$("vTypeId2").options[$("vTypeId2").selectedIndex].text;
	   var startTime=startTime;
	   var endTime =endTime;
	   var tab_name="";
	   var url;
		  if(vTypeId=='0'){//0宽带新装
			 url="/portalCommon/module/procedure/visitDetail/ADSL_visitList.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="宽带装移机";
		  }if(vTypeId=='1'){//1宽带修障
			 url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="宽带修障";
		  }if(vTypeId=='2'){//2营业厅服务
			 url="/portalCommon/module/procedure/visitDetail/hf/ADSL_visitList.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId+"&actType="+vTypeId;
			 tab_name="实体渠道";
		  }if(vTypeId=='20'){//营业厅服务非实体
		  	tab_name="非实体渠道";
	         url="/portalCommon/module/procedure/visitDetail/hf/no_ADSL_visitList.jsp?zoneCode="+zoneCode+
		    "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId+"&actType="+vTypeId;
	      }if(vTypeId=='3'){//3投诉处理
			 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="投诉处理";
		  }if(vTypeId=='4'){//4电子渠道(无)
			 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="电子渠道";
		  }if(vTypeId=='5'){//10000号
			 url="/portalCommon/module/procedure/visitDetail/s10000/10000_visitSatisfyList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="10000号";
		  }if(vTypeId=='6'){//6网厅
			 url="/portalCommon/module/procedure/visitDetail/swt/wt_visitSatisfyList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="网厅";
		  }if(vTypeId=='7'){//7掌厅
			 url="/portalCommon/module/procedure/visitDetail/szt/zt_visitSatisfyList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="掌厅";
		  }if(vTypeId=='8'){//8号百
			 url="/portalCommon/module/procedure/visitDetail/shb/hb_visitSatisfyList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			 tab_name="号百";
		  }if(vTypeId=='9'){//实体渠道评价器
				 url="/portalCommon/module/procedure/visitDetail/ishop/ADSL_visitList.jsp?zoneCode="+zoneCode+
				 "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId+"&actType="+vTypeId;
				 tab_name="实体渠道评价器";
	      }if(vTypeId=='10'){//实体渠道评价器
	    	  url="/portalCommon/module/procedure/visitDetail/dx/ADSL_visitList.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId+"&actType="+vTypeId+"&testMethod=1";
			  tab_name="实体渠道短信";
	      }if(vTypeId=='11'){//11宽带IVR新装
			  url="/portalCommon/module/procedure/visitDetail/ADSL_visitList_ivr_zyj.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			  tab_name="宽带IVR装移机";
		  }if(vTypeId=='12'){//12宽带IVR修障
			  url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList_ivr_xz.jsp?zoneCode="+zoneCode+
			  "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
			  tab_name="宽带IVR修障";
		 } 
		   
		 return parent.openTreeTab("124017"+zoneCode+indexId+vTypeId, zoneName+tab_name+"即时回访清单", base+url, 'top');
}
//按维度查看满意度详情
//周
var openMenuHref=function(demension,zoneCode,zoneName){
	$("menu").style.display ="none";
	$("menu1").style.display ="none";
	$("menu2").style.display ="none";
	var dateTime=$("dateTime").value;
	var actType=$("vTypeId").value;
	var firstCol="";
	if(demension=='0'){
		firstCol="在网时长";
	}if(demension=='1'){
		firstCol="受理渠道";
	}if(demension=='2'){
		firstCol="客户群";
	}if(demension=='3'){
		firstCol="客户等级（VIP、普通）";
	}if(demension=='4'){
		firstCol="客户价值（ARPU分档）";
	}if(demension=='5'){
		firstCol="套餐名称";
	}if(demension=='6'){
		if(actType=='2'){
			firstCol="营业员";
		}if(actType=='0'||actType=='1'){
			firstCol="装维员";
		}if(actType=='5'||actType=='6'||actType=='7'||actType=='8'){
			firstCol="话务员";
		}if(actType=='9'||actType=='10'){
			firstCol="服务人员工号";
		}
	}if(demension=='7'){
		firstCol="测评方式";
	}if(demension=='8'){
		firstCol="业务类型";
	}
	//var vTypeName=$("vTypeId").options[$("vTypeId").selectedIndex].text;//触点
	var url="/reports/customerSatisfied/dimension/serviceSatisfied_Week.jsp?zoneCode="+zoneCode+"&dateTime="+dateTime+"&vTypeId="+actType+"&demension="+demension;
  return parent.openTreeTab("131002121"+actType+zoneCode+demension, zoneName+"满意度按"+firstCol+"分析", base+url, 'top');
}
//月
var openMenuHref1=function(demension,zoneCode,zoneName){
	$("menu10").style.display ="none";
	//$("menu11").style.display ="none";
	$("menu12").style.display ="none";
	$("menu13").style.display ="none";
	$("menu14").style.display ="none";
	var dateTime=$("dateTime1").value;
	var actType=$("vTypeId1").value;
	//var vTypeName=$("vTypeId1").options[$("vTypeId1").selectedIndex].text;//触点
	var firstCol="";
	if(demension=='0'){
		firstCol="在网时长";
	}if(demension=='1'){
		firstCol="受理渠道";
	}if(demension=='2'){
		firstCol="客户群";
	}if(demension=='3'){
		firstCol="客户等级（VIP、普通）";
	}if(demension=='4'){
		firstCol="客户价值（ARPU分档）";
	}if(demension=='5'){
		firstCol="套餐名称";
	}if(demension=='6'){
		if(actType=='2'){
			firstCol="营业员";
		}if(actType=='0'||actType=='1'){
			firstCol="装维员";
		}if(actType=='5'||actType=='6'||actType=='7'||actType=='8'){
			firstCol="话务员";
		}if(actType=='9'||actType=='10'){
			firstCol="服务人员工号";
		}
	}if(demension=='7'){
		firstCol="测评方式";
	}if(demension=='8'){
		firstCol="业务类型";
	}if(demension=='9'){
		firstCol="产品类型";
	}if(demension=='10'){
		firstCol="终端类型";
	}
	var url="/reports/customerSatisfied/dimension/serviceSatisfied_Mon.jsp?zoneCode="+zoneCode+"&dateTime="+dateTime+"&vTypeId="+actType+"&demension="+demension;
  return parent.openTreeTab("131002121"+actType+zoneCode+demension, zoneName+"满意度按"+firstCol+"分析", base+url, 'top');
}
//日
var openMenuHref2=function(demension,zoneCode,zoneName){
	$("menu20").style.display ="none";
	$("menu21").style.display ="none";
	$("menu22").style.display ="none";
	var startTime=$("startDate").value;
    var endTime=$("endDate").value;
	var actType=$("vTypeId2").value;
	//var vTypeName=$("vTypeId2").options[$("vTypeId2").selectedIndex].text;//触点
	var firstCol="";
	if(demension=='0'){
		firstCol="在网时长";
	}if(demension=='1'){
		firstCol="受理渠道";
	}if(demension=='2'){
		firstCol="客户群";
	}if(demension=='3'){
		firstCol="客户等级（VIP、普通）";
	}if(demension=='4'){
		firstCol="客户价值（ARPU分档）";
	}if(demension=='5'){
		firstCol="套餐名称";
	}if(demension=='6'){
		if(actType=='2'){
			firstCol="营业员";
		}if(actType=='0'||actType=='1'){
			firstCol="装维员";
		}if(actType=='5'||actType=='6'||actType=='7'||actType=='8'){
			firstCol="话务员";
		}if(actType=='9'||actType=='10'){
			firstCol="服务人员工号";
		}
	}if(demension=='7'){
		firstCol="测评方式";
	}if(demension=='8'){
		firstCol="业务类型";
	}
	var url="/reports/customerSatisfied/dimension/serviceSatisfied_Day.jsp?zoneCode="+zoneCode+"&startDate="+startTime+"&endDate="+endTime+"&vTypeId="+actType+"&demension="+demension;
  return parent.openTreeTab("131002121"+actType+zoneCode+demension, zoneName+"满意度按"+firstCol+"分析", base+url, 'top');
}
//非常满意、不满意跳转到不满意原因查看
//周
var returnMenuHref=function(zoneName,zoneCode,reasonId){
	  var dateTime=$("dateTime").value;
	  //var vTypeName=$("vTypeId").options[$("vTypeId").selectedIndex].text;
	  var startTime=dateTime.substr(0,dateTime.indexOf("~"));
	  startTime =startTime.substr(0,4)+"-"+startTime.substr(4,2)+"-"+startTime.substr(6,2) ;
	  var endTime=dateTime.substr(dateTime.indexOf("~")+1,dateTime.lenght);
	  endTime =endTime.substr(0,4)+"-"+endTime.substr(4,2)+"-"+endTime.substr(6,2) ;
	  var vTypeId=$("vTypeId").value;
	  var tab_name="";
	  var url;
	  if(vTypeId=='0'){//0宽带新装
		 tab_name="宽带装移机";
		 url="/portalCommon/module/procedure/visitDetail/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='1'){//1宽带修障
		  tab_name="宽带修障";
		 url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='2'){//2营业厅服务
		  tab_name="实体渠道";
		 url="/portalCommon/module/procedure/visitDetail/hf/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&actType="+vTypeId;
	  }if(vTypeId=='20'){//营业厅服务非实体
	     tab_name="非实体渠道";
	     url="/portalCommon/module/procedure/visitDetail/hf/no_ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&actType="+vTypeId;
	  }if(vTypeId=='3'){//3投诉处理
		  tab_name="投诉处理";
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='4'){//4电子渠道(无)
		  tab_name="电子渠道";
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='5'){//10000号
		  tab_name="10000号";
		 url="/portalCommon/module/procedure/visitDetail/s10000/10000_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='6'){//6网厅
		  tab_name="网厅";
		 url="/portalCommon/module/procedure/visitDetail/swt/wt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='7'){//7掌厅
		  tab_name="掌厅";
		 url="/portalCommon/module/procedure/visitDetail/szt/zt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='8'){//8号百
		  tab_name="号百";
		 url="/portalCommon/module/procedure/visitDetail/shb/hb_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='9'){//
		  tab_name="实体渠道评价器";
			 url="/portalCommon/module/procedure/visitDetail/ishop/ADSL_visitList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&actType="+vTypeId;;
      }if(vTypeId=='10'){//实体渠道评价器
    	  url="/portalCommon/module/procedure/visitDetail/dx/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&actType="+vTypeId+"&testMethod=1";
		  tab_name="实体渠道短信";
      }if(vTypeId=='11'){//11宽带IVR新装
		  url="/portalCommon/module/procedure/visitDetail/ADSL_visitList_ivr_zyj.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
		  tab_name="宽带IVR装移机";
	  }if(vTypeId=='12'){//12宽带IVR修障
		  url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList_ivr_xz.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
		  tab_name="宽带IVR修障";
	 }
	 return parent.openTreeTab("124017"+zoneCode+vTypeId+reasonId, zoneName+tab_name+"即时回访清单", base+url, 'top');
}
//月
var returnMenuHref1=function(zoneName,zoneCode,reasonId){
	  var dateTime=$("dateTime1").value;
	  //var vTypeName=$("vTypeId1").options[$("vTypeId1").selectedIndex].text;
	  var year= dateTime.substring(0,4);
	  var month=dateTime.substring(4,6);
	  var startTime=year+"-"+month+"-01";
	  var endTime=year+"-"+month+"-"+getLastDay(year,month);
	  var vTypeId=$("vTypeId1").value;
	  var tab_name="";
	  var url;
	  if(vTypeId=='0'){//0宽带新装
		  tab_name="宽带装移机";
		 url="/portalCommon/module/procedure/visitDetail/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='1'){//1宽带修障
		  tab_name="宽带修障";
		 url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='2'){//2营业厅服务
		  tab_name="实体渠道";
		 url="/portalCommon/module/procedure/visitDetail/hf/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&actType="+vTypeId;
	  }if(vTypeId=='20'){//营业厅服务非实体
	  	tab_name="非实体渠道";
	     url="/portalCommon/module/procedure/visitDetail/hf/no_ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&actType="+vTypeId;
	  }if(vTypeId=='3'){//3投诉处理
		  tab_name="投诉处理";
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='4'){//4电子渠道(无)
		  tab_name="电子渠道";
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='5'){//10000号
		  tab_name="10000号";
		 url="/portalCommon/module/procedure/visitDetail/s10000/10000_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='6'){//6网厅
		  tab_name="网厅";
		 url="/portalCommon/module/procedure/visitDetail/swt/wt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='7'){//7掌厅
		  tab_name="掌厅";
		 url="/portalCommon/module/procedure/visitDetail/szt/zt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='8'){//8号百
		  tab_name="号百";
		 url="/portalCommon/module/procedure/visitDetail/shb/hb_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='9'){//8号百
		  tab_name="实体渠道评价器";
			 url="/portalCommon/module/procedure/visitDetail/ishop/ADSL_visitList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&actType="+vTypeId;
      }if(vTypeId=='10'){//实体渠道评价器
    	  url="/portalCommon/module/procedure/visitDetail/dx/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&actType="+vTypeId+"&testMethod=1";
		  tab_name="实体渠道短信";
      }if(vTypeId=='11'){//11宽带IVR新装
		  url="/portalCommon/module/procedure/visitDetail/ADSL_visitList_ivr_zyj.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
		  tab_name="宽带IVR装移机";
	  }if(vTypeId=='12'){//12宽带IVR修障
		  url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList_ivr_xz.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
		  tab_name="宽带IVR修障";
	  }
	 return parent.openTreeTab("124017"+zoneCode+vTypeId+reasonId, zoneName+tab_name+"即时回访清单", base+url, 'top');
}
//日
var returnMenuHref2=function(zoneName,zoneCode,reasonId){
	  var  startTime=$("startDate").value;//时间
	  var  endTime=$("endDate").value;//时间
	  //var vTypeName=$("vTypeId2").options[$("vTypeId2").selectedIndex].text;
	  var startTime=startTime;
	  var endTime =endTime;
	  var vTypeId=$("vTypeId2").value;
	  var tab_name="";
	  var url;
	  if(vTypeId=='0'){//0宽带新装
		  tab_name="宽带装移机";
		 url="/portalCommon/module/procedure/visitDetail/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='1'){//1宽带修障
		  tab_name="宽带修障";
		 url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='2'){//2营业厅服务
		  tab_name="实体渠道";
		 url="/portalCommon/module/procedure/visitDetail/hf/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&actType="+vTypeId;
	  }if(vTypeId=='20'){//营业厅服务非实体
	  		tab_name ="非实体渠道";
	     url="/portalCommon/module/procedure/visitDetail/hf/no_ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&actType="+vTypeId;
	  }if(vTypeId=='3'){//3投诉处理
		  tab_name="投诉处理";
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='4'){//4电子渠道(无)
		  tab_name="电子渠道";
		 url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='5'){//10000号
		  tab_name="10000号";
		 url="/portalCommon/module/procedure/visitDetail/s10000/10000_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='6'){//6网厅
		  tab_name="网厅";
		 url="/portalCommon/module/procedure/visitDetail/swt/wt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='7'){//7掌厅
		  tab_name="掌厅";
		 url="/portalCommon/module/procedure/visitDetail/szt/zt_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='8'){//8号百
		  tab_name="号百";
		 url="/portalCommon/module/procedure/visitDetail/shb/hb_visitSatisfyList.jsp?zoneCode="+zoneCode+
		 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
	  }if(vTypeId=='9'){//8号百
		  tab_name="实体渠道评价器";
			 url="/portalCommon/module/procedure/visitDetail/ishop/ADSL_visitList.jsp?zoneCode="+zoneCode+
			 "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&actType="+vTypeId;
      }if(vTypeId=='10'){//实体渠道评价器
    	  url="/portalCommon/module/procedure/visitDetail/dx/ADSL_visitList.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId+"&actType="+vTypeId+"&testMethod=1";
		  tab_name="实体渠道短信";
      }if(vTypeId=='11'){//11宽带IVR新装
		  url="/portalCommon/module/procedure/visitDetail/ADSL_visitList_ivr_zyj.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
		  tab_name="宽带IVR装移机";
	  }if(vTypeId=='12'){//12宽带IVR修障
		  url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList_ivr_xz.jsp?zoneCode="+zoneCode+
		  "&startTime="+startTime+"&endTime="+endTime+"&satisType="+reasonId;
		  tab_name="宽带IVR修障";
	  }
	 return parent.openTreeTab("124017"+zoneCode+vTypeId+reasonId, zoneName+tab_name+"即时回访清单", base+url, 'top');
}
//获得某月的最后一天
function getLastDay(year,month) {
	var new_year = year;
   //取当前的年份
	var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）
  if(month>12) {
	new_month -=12; //月份减
	new_year++; //年份增
	}
	var new_date = new
	Date(new_year,new_month,1); //取当年当月中的第一天
    return (new Date(new_date.getTime()-1000*60*60*24)).getDate();//获取当月最后一天日期
}
//钻取
var drillArea=function(zoneName,zoneCode)
{	
	var dateTime=$("dateTime").value;
	var selectCol=$("selectCol").value;
	var url="/reports/customerSatisfied/cmplDeal/serviceSatisfied_Day.jsp?zoneCode="+zoneCode+"&dateTime="+dateTime+"&selectCol="+selectCol+"&menuId="+menuId;
	return parent.openTreeTab("124013"+zoneCode, "投诉处理满意率日报"+"["+zoneName+"]", base+url, 'top');
}
function changeDate(obj){
	 var map=new Object();
	 map.vTypeId=obj.value;
	 map.dateType="1";
	 CustomerSatisfiedAction.getDateListByIndex(map, {callback:function (res) {
		if (res != null) {
	      for(var i=0;i<res.length;i++)
			 {
	        	 var op=new Option(res[i].DATE_NO,res[i].DATE_NO);
	        	 if(res[i].DATE_NO==dateTime){op.setAttribute("selected",true);}
	        	   	$('dateTime').add(op);
			}
		}
	 }
  }); 
}
function changeDate1(obj){
	 var map=new Object();
	 map.vTypeId=obj.value;
	 map.dateType="2";
	 CustomerSatisfiedAction.getDateListByIndex(map, {callback:function (res) {
		if (res != null) {
	      for(var i=0;i<res.length;i++)
			 {
	        	 var op=new Option(res[i].DATE_NO,res[i].DATE_NO);
	        	 if(res[i].DATE_NO==dateTime1){op.setAttribute("selected",true);}
	        	   	$('dateTime1').add(op);
			}
		}
	 }
 }); 
}
//通过列改变图形-周 
/*function changeCol(obj){
	var titileInfo=$("selectCol").options[$("selectCol").selectedIndex].text+"->本周值";
	$("titleInfo1").innerHTML=titileInfo;
	$("titleInfo2").innerHTML=titileInfo;
	var map=new Object();
	map.changeCode=$("changeCode").value;
    map.startDate   =$("dateTime").value;
    map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
    map.dateType   ="1";//周报
    map.actType   =$("vTypeId").value;//投诉处理
    map.field=obj.value;
    buildChart(map);
}*/
//加载配置图形-周
/*function buildChart(map){
	dhx.showProgress("正在执行......");
	CustomerSatisfiedAction.getCustomerSatisfiedDetails_GraphSum(map, {callback:function (res) {
		dhx.closeProgress();
    	         if(res != null){
                        buildBLChart(res);
		         }
	}
		});
   }*/
//构建折线图和柱状图-周
/*function  buildBLChart(map){
	   $("cid").value=Math.random();
	   var cid=$("cid").value;
	   $("cid1").value=Math.random();
	   var cid1=$("cid1").value;
	  // $("chartdiv1").innerHTML="";
	  // $("chartdiv2").innerHTML="";
	  // chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
	  // chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
	  // chart1.setDataXML(map.barChartMap);
      // chart2.setDataXML(map.lineChartMap);
      // chart1.render("chartdiv1");
      // chart2.render("chartdiv2"); 
   }*/
//通过列改变图形-月
/*function changeCol1(obj){
	var titileInfo=$("selectCol1").options[$("selectCol1").selectedIndex].text+"->本月值";
	$("titleInfo11").innerHTML=titileInfo;
	$("titleInfo21").innerHTML=titileInfo;
	
	var map=new Object();
	map.changeCode=$("changeCode").value;
    map.startDate   =$("dateTime1").value;
    map.zoneCode=$('zoneCode1').value==""?userInfo['localCode']:$("zoneCode1").value;
    map.dateType   ="2";//月报
    map.actType   =$("vTypeId1").value;//投诉处理
    map.field=obj.value;
    buildChart1(map);
}*/
//加载配置图形
/*function buildChart1(map){
	dhx.showProgress("正在执行......");
	CustomerSatisfiedAction.getCustomerSatisfiedDetails_GraphSum(map, {callback:function (res) {
		dhx.closeProgress();
    	         if(res != null){
                        buildBLChart1(res);
		         }
	}
		});
   }*/
//构建折线图和柱状图
/*function  buildBLChart1(map){
	   $("cid").value=Math.random();
	   var cid=$("cid").value;
	   $("cid1").value=Math.random();
	   var cid1=$("cid1").value;
	   $("chartdiv11").innerHTML="";
	   $("chartdiv21").innerHTML="";
	  // chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
	   //chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
	  // chart1.setDataXML(map.barChartMap);
      // chart2.setDataXML(map.lineChartMap);
       chart1.render("chartdiv11");
       chart2.render("chartdiv21"); 
   }*/
//通过列改变图形-日
/*function changeCol2(obj){
	var titileInfo=$("selectCol2").options[$("selectCol2").selectedIndex].text+"->当日值";
	$("titleInfo12").innerHTML=titileInfo;
	$("titleInfo22").innerHTML=titileInfo;
	var map=new Object();
	map.changeCode=$("changeCode").value;
    map.startDate   =$("startDate").value;
    map.endDate   =$("endDate").value;
    map.zoneCode=$('zoneCode2').value==""?userInfo['localCode']:$("zoneCode2").value;
    map.dateType   ="0";//日报
    map.actType   =$("vTypeId2").value;//投诉处理
    map.field=obj.value;
    buildChart2(map);
}*/
//加载配置图形
/*function buildChart2(map){
	dhx.showProgress("正在执行......");
	CustomerSatisfiedAction.getCustomerSatisfiedDetails_GraphSum(map, {callback:function (res) {
		dhx.closeProgress();
    	         if(res != null){
                        buildBLChart2(res);
		         }
	}
		});
   }*/


//构建折线图和柱状图
/*function  buildBLChart2(map){
	   $("cid").value=Math.random();
	   var cid=$("cid").value;
	   $("cid1").value=Math.random();
	   var cid1=$("cid1").value;
	  // $("chartdiv12").innerHTML="";
	  // $("chartdiv22").innerHTML="";
	   chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
	   chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
	   chart1.setDataXML(map.barChartMap);
       chart2.setDataXML(map.lineChartMap);
       chart1.render("chartdiv12");
       chart2.render("chartdiv22"); 
   }*/
/*function exportImage(){
		var cid=$("cid").value;
	    var cid1=$("cid1").value;
        var charts2 = getChartFromId("ChartId2_"+cid);   //生成的FusionCharts图表本身的标识
        var charts1 = getChartFromId("ChartId1_"+cid1);   //生成的FusionCharts图表本身的标识
        charts1.exportChart(); 
        charts2.exportChart(); 
   }*/
//月
function exportExcel1(){
	    var  dateTime=$("dateTime1").options[$("dateTime1").selectedIndex].text;//时间
        var  zone=$("zone1").value;//区域
        //var  vTypeId=$("vTypeId1").options[$("vTypeId1").selectedIndex].text;//触点
        var  vTypeId=$("vTypeId").value;
        var queryCond="月份："+dateTime+"    区域："+zone;
	    $("excelCondition").value=queryCond; 
		 var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcelDemensionHf.jsp";
		document.forms[0].method = "post";
		document.forms[0].action=url;
		document.forms[0].target="hiddenFrame";
		document.forms[0].submit();
}
//周
function exportExcel(){
	var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
    var  zone=$("zone").value;//区域
    //var  vTypeId=$("vTypeId").options[$("vTypeId").selectedIndex].text;//触点
    var  vTypeId=$("vTypeId").value;
    var queryCond="周期："+dateTime+"    区域："+zone;
    $("excelCondition").value=queryCond; 
	 var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcelDemensionHf.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
//日
function exportExcel2(){
	var  startTime=$("startDate").value;//时间
	var  endTime=$("endDate").value;//时间
    var  zone=$("zone2").value;//区域
    //var  vTypeId=$("vTypeId2").options[$("vTypeId2").selectedIndex].text;//触点
    var  vTypeId=16;
    var queryCond="日期从："+startTime+"   至："+endTime+"    区域："+zone;
	$("excelCondition").value=queryCond; 
	 var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcelDemensionHf.jsp";

	$("startTime").value=startTime;
	$("endTime").value=endTime;
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 $('index_exp1').innerHTML='';
	 $('index_exp2').innerHTML='';
	 var  vTypeId=$("vTypeId").value;
	 var tempIndexId='';
	 if(vTypeId=='9'){
		 tempIndexId='4';
	 }else if(vTypeId=='2'){
		 tempIndexId='5';
	 }else if(vTypeId=='11'){
		 tempIndexId='6';
	 }else if(vTypeId=='12'){
		 tempIndexId='7';
	 }else{
		 tempIndexId='3';
	 }
	 var explainData="";
	 var map=new Object(); 
	 map.rptId=tempIndexId;
	 CmplIndexAction.getIndexExplain(map, {callback:function (data) {
         if(data != null){
        	 var tableStr="<table width='100%' border='0'  cellpadding='0' cellspacing='0'>";
    		 if(data&&data.expList.length>0){	
    			 for(var i=0;i<data.expList.length;i++){
    				 tableStr =tableStr+
    		         "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
    		           +"<td align=\"left\"><font style='font-weight:bold;' >&nbsp;"+data.expList[i].INDEX_NAME+"  :  "+"</font>"
    		           +data.expList[i].INDEX_EXPLAIN+"</td>"   
    		           +"</tr>";
    				 explainData+=data.expList[i].INDEX_NAME+"  :  "+ data.expList[i].INDEX_EXPLAIN+"]";
    			 }
    			 tableStr +="</table>"; 
    			 $("explain").value=explainData;
    			 $('index_exp').innerHTML=tableStr;
    			 $('index_exp1').innerHTML=tableStr;
    			 $('index_exp2').innerHTML=tableStr;
    		 } 
	         }
	   }
	});
}
//所有子节点
function getUserSubCode(){
	 var userCodeData="]";
	 var map=new Object(); 
	 map.userCode=$("userCode").value;
	 CustomerSatisfiedAction.getUserSubCode(map, {callback:function (data) {
         if(data != null){
    		 if(data&&data.userCodeList.length>0){	
    			 for(var i=0;i<data.userCodeList.length;i++){
    				 userCodeData+=data.userCodeList[i].ZONE_CODE+"]";
    			 }
    		 } 
	         }
         $("userCodeData").value= userCodeData;
	   }
	});
}
/**
 * 
 * @param myObject
 * @return
 */
function dump_obj(myObject) {  
   var s = "";  
   for (var property in myObject) {  
	   s = s + "\n "+property +": " + myObject[property] ;  
	  }  
	  alert(s);  
	} 
/** end **/
dhx.ready(userInit);