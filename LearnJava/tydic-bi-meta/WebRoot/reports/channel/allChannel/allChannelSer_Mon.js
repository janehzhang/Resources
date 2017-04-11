/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
var globZoneTree = null;
var globChannelTypeTree=null;
var flag='false';		//是否显示渠道小类
//by qx
var titileInfo=$("typeList").options[$("typeList").selectedIndex].text+"->"+$("selectCol").options[$("selectCol").selectedIndex].text;
$("titleInfo1").innerHTML=titileInfo;
//查询系统参数
var indId="";
var indName="";

var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var channelTypeCode =   $('channelTypeCode').value==""?'1':$("channelTypeCode").value;
if(channelTypeCode!='1')
{
	$("channel").value='返回';
}
else if($("channel").value!='渠道大类'){
{
	$("channel").value='渠道小类';
}
}

var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** zone Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCode");
var zoneConverter=dhx.extend({idColumn:"zoneCode",pidColumn:"zoneParCode",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
dwrCaller.addAction("querySubZoneCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZoneCode(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
/** channel Tree head **/
dwrCaller.addAutoAction("loadChannelTypeTreePart","ChannelTypeAction.queryChannelType");
var channelTypeConverter=dhx.extend({idColumn:"channelTypeCode",pidColumn:"channelTypeParCode",
    textColumn:"channelTypeName"
},treeConverter,false);
dwrCaller.addDataConverter("loadChannelTypeTreePart",channelTypeConverter);
dwrCaller.addAction("querySubChannelTypePart",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},channelTypeConverter,false);
    ChannelTypeAction.querySubChannel(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});

var indexInit=function(){
	 //1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);  
    //1.加载渠道类型树
     loadChannelTypeTreePart(channelTypeCode,queryform);
     //执行查询数据
     excuteInitData();
}

var excuteInitData=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	
	 var map=new Object();
	 map.field=$("selectCol").value;
	 map.selType=$("typeList").value;
	 map.changType='1';
	 map.dateTime=$("dateTime").value;
	 map.startDate=$("dateTime").value;
	 map.endDate=$("dateTime").value; 
	 map.reportId='3';
	 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
	 map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
	 map.changeCode=$("changeCode").value;
	 map.dateType="1";//周
	 map.showAllChannel=flag;
	 map.actType=$("actType").value;//分类类型：1传统新媒体渠道，2其他渠道，3人工自助渠道
     dhx.showProgress("正在执行......");
     AllChannelAction.getChannelGlobal_Mon(map, function (res) {
     	$('chartdiv1').innerHTML='';
     	$('chartdiv2').innerHTML='';
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				            return;
 				        }
 				    chart1=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
 			    	chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
			    	chart1.setDataXML(res.pieChartMap);
 			        chart2.setDataXML(res.lineChartMap);
 			        chart1.render("chartdiv1");
 			        chart2.render("chartdiv2"); 
 			        $("div_src").style.display = "none";
			        $("div_src").style.zindex = "-1";
 					buildTable(res);
    });
}
//查询
var queryData=function(){
	excuteInitData();
}
//钻取
var drillArea=function(zoneName,zoneCode,channelTypeCode)
{	
	var dateTime=$("dateTime").value;
	var url="/reports/channel/allBusinessAllZone/allBusiness_Mon_New.jsp?zoneCode="+zoneCode+"&dateTime="+dateTime+"&channelTypeCode="+channelTypeCode+"&menuId="+menuId;
	return parent.openTreeTab("119004"+zoneCode, "全渠道服务量分析月报"+"["+zoneName+"]", base+url, 'top');
}
//第一列合并
var autoRowSpan=function(tb,row,col){
    var lastValue="";
    var value="";
    var pos=1;
    for(var i=row;i<tb.rows.length;i++)
    {
        value = tb.rows[i].cells[col].innerText;
        if(lastValue == value)
        {
            tb.rows[i].deleteCell(col);
            tb.rows[i-pos].cells[col].rowSpan = tb.rows[i-pos].cells[col].rowSpan+1;
            pos++;
        }else{
            lastValue = value;
            pos=1;
        }
    }
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
	} 
/** end **/

var showAllChannel = function (obj){
	AllChannelAction.getParChannel($('channelTypeCode').value, function (res) {
		if(res=='1'){
			flag='true';
		}
		if(res!='0'){
			$('channelTypeCode').value=res;
		}
		if(flag=='true'){
		flag='false';
		 $("channel").value='渠道小类'
	}
	else{
		flag='true';
		$("channel").value='渠道大类'
	}
	excuteInitData();
	loadChannelTypeTreePart_all($('channelTypeCode').value,queryform);
	
	});
	
}
var lookCity = function (obj){
    var map=new Object();
        map.dateTime=$("dateTime").value;
        map.reportId='2';
        map.field=$("selectCol").value;
        map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
        map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
        tZoneCode=$("zoneCode").value;
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
		  AllChannelAction.loadSet21AreaChart(map, {callback:function (res) {
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
	   $("chartdiv1").innerHTML="";
	   chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
	   chart.setDataXML(map.XML);
	   chart.render("chartdiv1");
	}
//通过列改变图形
function changeCol(obj){
	var titileInfo=$("typeList").options[$("typeList").selectedIndex].text+"->"+$("selectCol").options[$("selectCol").selectedIndex].text;
	$("titleInfo1").innerHTML=titileInfo;
	var map=new Object();
	 map.dateTime=$("dateTime").value;
	 map.typeList=$("typeList").value;
	 map.field=$("selectCol").value;
     map.reportId='2';
     map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
     map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
     map.changeCode=$("changeCode").value;
     flag='false';
     if($("channel").value!='返回'){
     if(flag=='true'){
		 $("channel").value='渠道大类'
	 }
	 else{
		$("channel").value='渠道小类'
	 }
     }
     map.showAllChannel=flag;
     buildChart(map);
}
//加载配置图形
function buildChart(map){
	dhx.showProgress("正在执行......");
	AllChannelAction.getChannelGlobal_Mon(map, {callback:function (res) {
		dhx.closeProgress();
    	         if(res != null){
                        buildBLChart(res);
		         }
	}
		});
   }
function  buildBLChart(map){
		$("cid").value=Math.random();
		var cid=$("cid").value;
		$("cid1").value=Math.random();
		var cid1=$("cid1").value;
	   $("chartdiv1").innerHTML="";
	   $("chartdiv2").innerHTML="";
	   chart1=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
	   chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
	   chart1.setDataXML(map.pieChartMap);
       chart2.setDataXML(map.lineChartMap);
       chart1.render("chartdiv1");
       chart2.render("chartdiv2"); 
   }
function exportExcel(){
		var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//文本框
		    var  zone=$("zone").value;//文本框
		    var  channelType=$("channelType").value;//文本框
		    var queryCond="月份："+dateTime+"    区域："+zone+"    渠道类型: "+channelType;  
	   	    $("excelCondition").value=queryCond; 
	   	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcelMod_chart2.jsp";
			document.forms[0].method = "post";
			document.forms[0].action=url; 
		    document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
}
function exportImage(){
			var cid=$("cid").value;
			var cid1=$("cid1").value;
   	        var charts1 = getChartFromId("ChartId1_"+cid1);   //生成的FusionCharts图表本身的标识
   	         var charts2 = getChartFromId("ChartId2_"+cid);   //生成的FusionCharts图表本身的标识
   	        charts1.exportChart(); 
   	        charts2.exportChart(); 
}
//获取changeCode
function getChangeCode(){
	 var changeCode="";
	 tZoneCode=$("zoneCode").value;
	if(tZoneCode!="0000"){
		 $("changeCode").value="1";
		 changeCode="1";
	}else{
	    $("changeCode").value="2";
	    changeCode="2";
	}
	$("city").value='横向对比';
	return changeCode;
}
function myJs(temp){
			$('channelTypeCode').value=temp;
			if(temp=='1'){
				flag='false';
				$("channel").value!='渠道小类';
			}
			loadChannelTypeTreePart_all(temp,queryform);
			excuteInitData();
			
}
dhx.ready(indexInit);
