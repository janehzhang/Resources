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
//查询系统参数
var channelTypeCode =   $('channelTypeCode').value==""?'1':$("channelTypeCode").value;
var channelTypeCode1 =   $('channelTypeCode1').value==""?'1':$("channelTypeCode1").value;
var channelTypeCode2 =   $('channelTypeCode2').value==""?'1':$("channelTypeCode2").value;
var zoneCode  =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var zoneCode2 =   $('zoneCode2').value==""?userInfo['localCode']:$("zoneCode2").value;
var zoneCode1 =   $('zoneCode1').value==""?userInfo['localCode']:$("zoneCode1").value;

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

var actType=$("actType").value;
	if(actType=='2'){//其他渠道
		dwrCaller.addAutoAction("loadChannelTypeTreePart","ChannelTypeAction.queryChannelTypeByPathPart");
		var channelTypeConverter=dhx.extend({idColumn:"channelTypeCode",pidColumn:"channelTypeParCode",
		    textColumn:"channelTypeName"
		},treeConverter,false);
		dwrCaller.addDataConverter("loadChannelTypeTreePart",channelTypeConverter);
		dwrCaller.addAction("querySubChannelTypePart",function(afterCall,param){
		    var tempCovert=dhx.extend({isDycload:true},channelTypeConverter,false);
		    ChannelTypeAction.querySubChannelTypePartOther_new(param.id,function(data){
		        data=tempCovert.convert(data);
		        afterCall(data);
		    })
		});
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
}

function getHeader(){
var thirdType=$("thirdType").value;
var headerLevel=$("headerLevel").value;
var tableHead="";
if(headerLevel=="1"){
	tableHead="<tr>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>渠道大类</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>渠道小类</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%'><span class='title' align='center' >查询</span></td>" 
	    +"<td bgcolor='#cde5fd' style='width:6%'><span class='title' align='center'>充值交费</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%'><span class='title' align='center'>办理</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>咨询</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>故障申告</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>投诉</span></td>"
 	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>其他（非集团）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>总服务量</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>占比</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>上月同期总服务量</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>总服务量同比</span></td>"
	 +"</tr>";
}

return tableHead;
}


//周报
var queryData=function(){	 
	 $("cid1").value=Math.random();
	 var cid1=$("cid1").value;
	 
	 $("cid2").value=Math.random();
	 var cid2=$("cid2").value;

	 var map=new Object();
	 map.level=1;
     map.dateTime=$("dateTime").value;
	 map.startDate=$("dateTime").value;
	 map.endDate=$("dateTime").value; 
	 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
	 map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
	 map.thirdType=$("thirdType").value;
	 map.headerLevel=$("headerLevel").value;
	 map.dateType="1";//周 
	 map.field=$("selectCol").value;
	 map.actType=$("actType").value;//分类类型：1传统新媒体渠道，2其他渠道，3人工自助渠道
	 map.showAllChannel=flag;
     dhx.showProgress("正在执行......");
     AllChannelAction.getChannelSerClassFirst_Pg(map, function (res) {
	    $('chartdiv7').innerHTML='';
	    $('chartdiv8').innerHTML='';
	    dhx.closeProgress();
	 		if (res == null) {
	 			 dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
	 		 return;
	 		 }
	 	chart1=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid2, "100%", "200", "0", "0");
	 	chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid1, "100%", "200", "0", "0");
 		chart1.setDataXML(res.pieChartMap);
	 	chart2.setDataXML(res.lineChartMap);
	 	chart1.render("chartdiv7");
	 	chart2.render("chartdiv8"); 
 		$("div_src").style.display = "none";
		$("div_src").style.zindex = "-1";
 		buildTable(res);
    });	
}

function buildTable(data){
    tableStr ="<table  id='table' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    autoRowSpan($("table"),0,1);
    autoRowSpan($("table"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	var tableHead=getHeader();
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
  if(dataColumn&&dataColumn.length>0){  
	   for(var i=0;i<dataColumn.length;i++){
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	  for(var j=0;j<headColumn.length;j++) {
   		               var tempColumn=headColumn[j];   
   		               if(tempColumn.indexOf('_')==-1) { 
	   		            	 if(tempColumn == '渠道小类' &&dataColumn[i][tempColumn]!='小计'&&dataColumn[i][tempColumn]!='合计')
	    		        	 {
	   		            		 tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].区域+"','"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	
	    		        	 }else{
		    		        	 tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
	    		        	 }
   		            	         excelData += dataColumn[i][tempColumn]+"}";
		    		       } 
   		      }
	    	  tableData +="</tr>";
			  excelData +="]";
	    	}   
	 }else{
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
     
	return tableData;
}
//月报
var queryData1=function(){
	 $("cid1").value=Math.random();
	 var cid1=$("cid1").value;
	 
	 $("cid2").value=Math.random();
	 var cid2=$("cid2").value;

	 
     var map=new Object();
     map.level=1;
     map.dateTime=$("dateTime1").value;
	 map.startDate=$("dateTime1").value;
	 map.endDate=$("dateTime1").value; 
	 map.zoneCode=$('zoneCode1').value==""?"0000":$("zoneCode1").value;
	 map.channelTypeCode=$('channelTypeCode1').value==""?'1':$("channelTypeCode1").value;
	 map.dateType="2";//月	 
	 map.thirdType=$("thirdType").value;
	 map.headerLevel=$("headerLevel").value;
	 map.field=$("selectCol").value;
	 map.showAllChannel=flag;
	 map.actType=$("actType").value;//分类类型：1传统新媒体渠道，2其他渠道，3人工自助渠道
    dhx.showProgress("正在执行......");
    AllChannelAction.getChannelSerClassFirst_Pg(map, function (res) {
     	$('chartdiv13').innerHTML='';
     	$('chartdiv14').innerHTML='';
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				            return;
 				        }
 				    chart1=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid2, "100%", "200", "0", "0");
 			    	chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid1, "100%", "200", "0", "0");
			    	chart1.setDataXML(res.pieChartMap);
 			        chart2.setDataXML(res.lineChartMap);
 			        chart1.render("chartdiv13");
 			        chart2.render("chartdiv14"); 
		 $("div_src").style.display = "none";
		 $("div_src").style.zindex = "-1";
		 buildTable1(res);
   });	
}
function buildTable1(data){
    tableStr ="<table  id='table1' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader1(data.headColumn);
    tableStr+=tableData1(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable1').innerHTML=tableStr;
    autoRowSpan($("table1"),0,1);
    autoRowSpan($("table1"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader1(headColumn){
	var tableHead=getHeader();
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
  if(dataColumn&&dataColumn.length>0){  
	   for(var i=0;i<dataColumn.length;i++){
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	  for(var j=0;j<headColumn.length;j++) {
   		               var tempColumn=headColumn[j];   
   		               if(tempColumn.indexOf('_')==-1) { 
	   		            	 if(tempColumn == '渠道小类' &&dataColumn[i][tempColumn]!='小计'&&dataColumn[i][tempColumn]!='合计')
	    		        	 {
	   		            		 tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].区域+"','"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	
	    		        	 }else{
		    		        	 tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
	    		        	 }
   		            	         excelData += dataColumn[i][tempColumn]+"}";
		    		       } 
   		      }
	    	  tableData +="</tr>";
			  excelData +="]";
	    	}   
	 }else{
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
     
	return tableData;
}
//日报
var queryData2=function(){
	 $("cid2").value=Math.random();
	 var cid2=$("cid2").value;
	
	 $("cid1").value=Math.random();
	 var cid1=$("cid1").value;
	 var map=new Object();
	 map.level=1;
	 map.dateTime=$("endDate").value;
	 map.startDate=$("startDate").value;
	 map.endDate=$("endDate").value; 
	 map.zoneCode=$('zoneCode2').value==""?"0000":$("zoneCode2").value;
	 map.channelTypeCode=$('channelTypeCode2').value==""?'1':$("channelTypeCode2").value;
	 map.dateType="0";//天	
	 map.thirdType=$("thirdType").value;
	 map.headerLevel=$("headerLevel").value;
	 map.actType=$("actType").value;//分类类型：1传统新媒体渠道，2其他渠道，3人工自助渠道
	 map.field=$("selectCol").value;
	 map.showAllChannel=flag;
     dhx.showProgress("正在执行......");
     AllChannelAction.getChannelSerClassFirst_Pg(map, function (res) {
    	$('chartdiv1').innerHTML='';
    	$('chartdiv2').innerHTML='';
      	 dhx.closeProgress();
   		 if (res == null) {
   			 dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
   			 return;
   		 }
   		chart1=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid2, "100%", "200", "0", "0");
   	    chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid1, "100%", "200", "0", "0");
   	    chart1.setDataXML(res.pieChartMap);
        chart2.setDataXML(res.lineChartMap);
        chart1.render("chartdiv1");
        chart2.render("chartdiv2"); 
        $("div_src").style.display = "none";
        $("div_src").style.zindex = "-1";
   		 buildTable2(res);
      });	
}
function buildTable2(data){
    tableStr ="<table  id='table2' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader2(data.headColumn);
    tableStr+=tableData2(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable2').innerHTML=tableStr;
    autoRowSpan($("table2"),0,1);
    autoRowSpan($("table2"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader2(headColumn){
	var tableHead=getHeader();
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
  if(dataColumn&&dataColumn.length>0){  
	   for(var i=0;i<dataColumn.length;i++){
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	  for(var j=0;j<headColumn.length;j++) {
   		               var tempColumn=headColumn[j];   
   		               if(tempColumn.indexOf('_')==-1) { 
	   		            	 if(tempColumn == '渠道小类' &&dataColumn[i][tempColumn]!='小计'&&dataColumn[i][tempColumn]!='合计')
	    		        	 {
	   		            		 tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].区域+"','"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	
	    		        	 }else{
		    		        	 tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
	    		        	 }
   		            	         excelData += dataColumn[i][tempColumn]+"}";
		    		       } 
   		      }
	    	  tableData +="</tr>";
			  excelData +="]";
	    	}   
	 }else{
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
     
	return tableData;
}
//周
var drillArea=function(zoneName,zoneCode,channelTypeCode)
{	
	var startDate=$("dateTime").value;
	var endDate=$("dateTime").value;
	var url="/reports/channel/channelTouch/allChannelSerZone_Week.jsp?zoneCode="+zoneCode+"&startDate="+startDate+
	"&endDate="+endDate+"&channelTypeCode="+channelTypeCode+"&menuId="+menuId;
	return parent.openTreeTab("164010"+zoneCode+channelTypeCode, "其他渠道服务总体报表"+"["+zoneName+"]", base+url, 'top');
	
}
//月
var drillArea1=function(zoneName,zoneCode,channelTypeCode)
{	
	var startDate=$("dateTime1").value;
	var endDate=$("dateTime1").value;
	var url="/reports/channel/newChannel/AreaServer_Mon.jsp?zoneCode="+zoneCode+"&startDate="+startDate+
	"&endDate="+endDate+"&channelTypeCode="+channelTypeCode+"&menuId="+menuId;
	return parent.openTreeTab("164010"+zoneCode+channelTypeCode, "其他渠道服务总体报表"+"["+zoneName+"]", base+url, 'top');
	
}
//日
var drillArea2=function(zoneName,zoneCode,channelTypeCode)
{	
	var startDate=$("startDate").value;
	var endDate=$("endDate").value;
	var url="/reports/channel/newChannel/AreaServer_Day.jsp?zoneCode="+zoneCode+"&startDate="+startDate+
	"&endDate="+endDate+"&channelTypeCode="+channelTypeCode+"&menuId="+menuId;
	return parent.openTreeTab("164010"+zoneCode+channelTypeCode, "其他渠道服务总体报表"+"["+zoneName+"]", base+url, 'top');
	
}

//通过列改变图形-周 
function changeCol(obj){
	var map=new Object();
	map.dateTime=$("dateTime").value;
	map.startDate=$("dateTime").value;
	map.endDate=$("dateTime").value; 
    map.level=1;
    //map.typeList=$("typeList").value;
    map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value; 
    map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
    map.thirdType=$("thirdType").value;
	map.headerLevel=$("headerLevel").value;
	map.dateType="1";//周
	map.field=obj.value;
	map.showAllChannel=flag;
    map.actType=$("actType").value;//分类类型：1传统新媒体渠道，2其他渠道，3人工自助渠道     
    buildChart(map);
	
	var map=new Object();
	map.dateTime=$("dateTime").value;
	map.startDate=$("dateTime").value;
	map.endDate=$("dateTime").value; 
	map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
	map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
	map.thirdType=$("thirdType").value;
	map.headerLevel=$("headerLevel").value;
	map.dateType="1";//周 
	map.field=obj.value;
    buildChart(map);
}
//加载配置图形-周
function buildChart(map){
	dhx.showProgress("正在执行......");
	AllChannelAction.getChannelSerClass_Chart_FirstSecThi(map, {callback:function (res) {
		dhx.closeProgress();
    	         if(res != null){
                        buildBLChart(res);
		         }
	}
		});
   }
//构建折线图和柱状图-周
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

//通过列改变图形-月
function changeCol1(obj){
	//var titileInfo=$("channelType").value+"->"+$("selectCol").options[$("selectCol").selectedIndex].text;
	//$("titleInfo1").innerHTML=titileInfo;
	 var map=new Object();
	 map.dateTime=$("dateTime1").value;
	 map.startDate=$("dateTime1").value;
	 map.endDate=$("dateTime1").value; 
     //map.field=$("selectCol").value;
     //map.typeList=$("typeList").value;
     map.zoneCode=$('zoneCode1').value==""?"0000":$("zoneCode1").value; 
     map.channelTypeCode=$('channelTypeCode1').value==""?'1':$("channelTypeCode1").value;    
	 map.dateType="2";//月
	 map.thirdType=$("thirdType").value;
	 map.headerLevel=$("headerLevel").value;
	 map.field=obj.value;
	 map.showAllChannel=flag;
	 map.actType=$("actType").value;//分类类型：1传统新媒体渠道，2其他渠道，3人工自助渠道     
     buildChart1(map);
	
}
//加载配置图形
function buildChart1(map){
	dhx.showProgress("正在执行......");
	AllChannelAction.getChannelSerClass_Chart(map, {callback:function (res) {
		dhx.closeProgress();
    	         if(res != null){
                        buildBLChart1(res);
		         }
	}
		});
   }
//构建折线图和柱状图
function  buildBLChart1(map){
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

//通过列改变图形-日
function changeCol2(obj){
	//var titileInfo=$("channelType").value+"->"+$("selectCol").options[$("selectCol").selectedIndex].text;
	//$("titleInfo1").innerHTML=titileInfo;
	 var map=new Object();
	 map.startDate=$("startDate").value;
	 map.endDate=$("endDate").value;
	 //map.dateTime=$("endDate").value;
     map.field=$("selectCol").value;
     //map.typeList=$("typeList").value;
     map.zoneCode=$('zoneCode2').value==""?"0000":$("zoneCode2").value; 
     map.channelTypeCode=$('channelTypeCode2').value==""?'1':$("channelTypeCode2").value;    
	 map.dateType="0";//天
	 map.thirdType=$("thirdType").value;
	 map.headerLevel=$("headerLevel").value;
	 map.field=obj.value;
	 map.showAllChannel=flag;
	 map.actType=$("actType").value;//分类类型：1传统新媒体渠道，2其他渠道，3人工自助渠道     
     buildChart2(map);
}
//加载配置图形
function buildChart2(map){
	dhx.showProgress("正在执行......");
	AllChannelAction.getChannelSerClass_Chart_FirstSecThi(map, {callback:function (res) {
		dhx.closeProgress();
    	         if(res != null){
                        buildBLChart2(res);
		         }
	}
		});
   }
//构建折线图和柱状图
function  buildBLChart2(map){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;

	$('chartdiv1').innerHTML='';
	$('chartdiv2').innerHTML='';
	chart1=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
	chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
	chart1.setDataXML(map.pieChartMap);
    chart2.setDataXML(map.lineChartMap);
	chart1.render("chartdiv1");
	chart2.render("chartdiv2"); 
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
	  alert(s);  
	} 
/** end **/
function exportImage(){
	var cid1=$("cid1").value;
	var cid2=$("cid2").value;
    var charts1 = getChartFromId("ChartId1_"+cid1);   //生成的FusionCharts图表本身的标识
    var charts2 = getChartFromId("ChartId2_"+cid2);   //生成的FusionCharts图表本身的标识
    charts1.exportChart();
    charts2.exportChart(); 
   // charts3.exportChart(); 
    //charts4.exportChart(); 
    //charts5.exportChart();
    //charts6.exportChart(); 
}

//日
function exportExcel2(){
	var  startDate=$("startDate").value;//文本框\
	var  endDate=$("endDate").value;//文本框
	var  zone=$("zone2").value;//文本框
	var  selectCol=$("selectCol").value;//文本框
	var queryCond="日期从："+startDate+"    至："+endDate+"    区域："+zone; 
	$("excelCondition").value=queryCond; 
	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implChannelMoreExcel.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
//周
function exportExcel(){
	var  startDate=$("dateTime").value;//文本框\
    var  endDate=$("dateTime").value;//文本框
    var  zone=$("zone").value;//文本框
    var  selectCol=$("selectCol").value;//文本框
    var queryCond="查询周："+startDate+"    区域："+zone ; 
	$("excelCondition").value=queryCond; 
	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implChannelMoreExcel.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
//月
function exportExcel1(){
    var  startDate=$("dateTime1").value;//文本框\
    var  endDate=$("dateTime1").value;//文本框
    var  zone=$("zone1").value;//文本框
    var  selectCol=$("selectCol").value;//文本框
    var queryCond="月份："+startDate+"    区域："+zone ; 
    $("excelCondition").value=queryCond; 
    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implChannelMoreExcel.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
var indexInit=function(){	
     clickTab();
 	//1.加载地域树 
 	 loadZoneTreeChkBox(zoneCode,queryform);
     loadZoneTreeChkBox2(zoneCode2,queryform);
     loadZoneTreeChkBox1(zoneCode1,queryform);
   //1.加载渠道类型树
     loadChannelTypeTreePart(channelTypeCode,queryform);
     loadChannelTypeTreePart1(channelTypeCode1,queryform);
     loadChannelTypeTreePart2(channelTypeCode2,queryform);
}
dhx.ready(indexInit);