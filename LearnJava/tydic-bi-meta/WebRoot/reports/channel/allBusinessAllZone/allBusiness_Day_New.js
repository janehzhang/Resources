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
//by qx
tZoneCode=$("zoneCode").value;
tDefaultZoneCode=$("defaultZoneCode").value;
if(tZoneCode!=tDefaultZoneCode){//下钻
    $("changeCode").value='1';
}else{
	$("changeCode").value='2';
}
var changeCode = $("changeCode").value;
//查询系统参数
var indId="";
var indName="";
var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var channelTypeCode =   $('channelTypeCode').value==""?'1':$("channelTypeCode").value;

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
dwrCaller.addAutoAction("loadChannelTypeTreePart","ChannelTypeAction.queryChannelTypeByPathPart");
var channelTypeConverter=dhx.extend({idColumn:"channelTypeCode",pidColumn:"channelTypeParCode",
    textColumn:"channelTypeName"
},treeConverter,false);
dwrCaller.addDataConverter("loadChannelTypeTreePart",channelTypeConverter);
dwrCaller.addAction("querySubChannelTypePart",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},channelTypeConverter,false);
    ChannelTypeAction.querySubChannelTypePart_new(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});

var indexInit=function(){	
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
   //1.加载渠道类型树
     loadChannelTypeTreePart(channelTypeCode,queryform);
     getDimLevel();
     //执行查询数据
     excuteInitData();
}


var excuteInitData=function(){
	    var titileInfo=$("channelType").value+"->"+$("selectCol").options[$("selectCol").selectedIndex].text;
	    $("titleInfo1").innerHTML=titileInfo;
		$("cid").value=Math.random();
		var cid=$("cid").value;
		$("cid1").value=Math.random();
		var cid1=$("cid1").value;
		 var map=new Object();
		 map.field=$("selectCol").value;
		 map.selType=$("channelTypeCode").value;
		 map.changType=$("changType").value;
		 map.dateTime=$("dateTime").value;
		 map.startDate=$("dateTime").value;
		 map.endDate=$("dateTime").value; 
		 map.reportId='3';
		 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
		 map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
		 map.changeCode=$("changeCode").value;
		 map.dateType="1";//周
	     dhx.showProgress("正在执行......");
	     AllBusinessAction_New.getChannelGlobal_Zone(map, function (res) {
	     	$('chartdiv1').innerHTML='';
	     	$('chartdiv2').innerHTML='';
	    	               dhx.closeProgress();
	 				        if (res == null) {
	 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
	 				            return;
	 				        }
	 				     chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "1");
	 			    	 chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "1");
	 			         chart1.setDataXML(res.barChartMap);
	 			         chart2.setDataXML(res.lineChartMap);
	 			         chart1.render("chartdiv1");
	 			         chart2.render("chartdiv2"); 
	 			         $("div_src").style.display = "none";
				         $("div_src").style.zindex = "-1";
	 					 buildTable(res);
	    });
}
function buildTable(data){
    tableStr ="<table  id='tab1' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    autoRowSpan($("tab1"),0,1);
    autoRowSpan($("tab1"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	var tableHead="<tr>"
		    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='2'><span class='title'>大区</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='2' ><span class='title'>区域</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:25%' colspan='5' ><span class='title' align='center' >查询</span></td>" 
		    +"<td bgcolor='#cde5fd' style='width:20%' colspan='4'><span class='title' align='center'>办理</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='2'><span class='title'>充缴</span></td>" 
		    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='2'><span class='title'>咨询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='2'><span class='title'>投诉</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='2'><span class='title'>障碍</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='2'><span class='title'>其它</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='2'><span class='title'>总服务量</span></td>"
		 +"</tr>"
		 +"<tr>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>费用查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>进度查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>积分查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>信息查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>常用查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>增值业务</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>套餐销售</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>套餐变更</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>基础服务</span></td>"
		 +"</tr>";
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
  var isLink=isGotoLink();
     if(dataColumn&&dataColumn.length>0)
	    {  
	       for(var i=0;i<dataColumn.length;i++)
	       {
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	  for(var j=0;j<headColumn.length;j++)
   		         {
   		               var tempColumn=headColumn[j];    
   		               if(tempColumn.indexOf('_')==-1)
		    		       {
		    		         
   		            	 if(tempColumn == '区域' && i!=0&&isLink=='1')
    		        	 {
		            		  // if(dataColumn[i].区域_层级<'6'){
		    		        	     tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].区域+"','"+dataColumn[i].区域_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
		    		        	 // }else{
		    		        		//  tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
		    		        	//  }
		            		 //  tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].分公司+"','"+dataColumn[i].地市_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	   
    		        	 }else{
	    		        	 tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
    		        	 }
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
//钻取
var drillArea=function(zoneName,zoneCode)
{	
	var dateTime=$("dateTime").value;
	var selectCol=$("selectCol").value;
	var selectType=$("channelTypeCode").value;
	var channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
	var url="/reports/channel/allBusinessAllZone/allBusiness_Day_New.jsp?zoneCode="+zoneCode+"&weekNo="+dateTime+"&channelTypeCode="
	+channelTypeCode+"&selectType="+selectType+"&selectCol="+selectCol+"&menuId="+menuId;
	return parent.openTreeTab("150001"+zoneCode, "全渠道服务量分析周报"+"["+zoneName+"]", base+url, 'top');
	
}
//查询
var queryData=function(){
	changeCode=getChangeCode(); //by qx
	getDimLevel();
	excuteInitData();
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

var showAllChannel = function (obj){
	AllBusinessAction_New.getParChannel($('channelTypeCode').value, function (res) {
		if(res=='1'){
			flag='true';
		}
		if(res!='0'){
			$('channelTypeCode').value=res;
		}
		if(flag=='true'){
		flag='false';
		 $("channel").value='二级渠道'
	}
	else{
		flag='true';
		$("channel").value='一级渠道'
	}
	excuteInitData();
	loadChannelTypeTreePart_all($('channelTypeCode').value,queryform);
	
	});
	
}
var lookCity = function (obj){
		tZoneCode=$("zoneCode").value;
    	tDefaultZoneCode=$("defaultZoneCode").value;
   	 var map=new Object();
        map.dateTime=$("dateTime").value;
        map.reportId='3';
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
		  AllBusinessAction_New.loadSet21AreaChart(map, {callback:function (res) {
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
	var titileInfo=$("channelType").value+"->"+$("selectCol").options[$("selectCol").selectedIndex].text;
	$("titleInfo1").innerHTML=titileInfo;
	var map=new Object();
	 map.dateTime=$("dateTime").value;
     map.reportId='3';
     map.field=$("selectCol").value;
     map.selType=$("channelTypeCode").value;
     map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
     map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
     map.changeCode=$("changeCode").value;
     buildChart(map);
}
//加载配置图形
function buildChart(map){
	dhx.showProgress("正在执行......");
	AllBusinessAction_New.getChannelGlobal_Zone(map, {callback:function (res) {
		dhx.closeProgress();
    	         if(res != null){
                        buildBLChart(res);
		         }
	}
		});
   }
//构建折线图和柱状图
function  buildBLChart(map){
		$("cid").value=Math.random();
		var cid=$("cid").value;
		$("cid1").value=Math.random();
		var cid1=$("cid1").value;
	   $("chartdiv1").innerHTML="";
	   $("chartdiv2").innerHTML="";
	   chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
	   chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
	   chart1.setDataXML(map.barChartMap);
       chart2.setDataXML(map.lineChartMap);
       chart1.render("chartdiv1");
       chart2.render("chartdiv2"); 
   }
function exportExcel(){
		var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//文本框
		    var  zone=$("zone").value;//文本框
		    var  channelType=$("channelType").value;//文本框
		    var queryCond="查询周："+dateTime+"    区域："+zone+"    渠道类型: "+channelType;  
	   	    $("excelCondition").value=queryCond; 
	   	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_twoChartHeader.jsp";
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
function myJs(temp){
			$('channelTypeCode').value=temp;
			if(temp=='1'){
				flag='false';
				$("channel").value!='二级渠道';
			}
			excuteInitData();
			loadChannelTypeTreePart_all(temp,queryform);
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
//判断是否钻取
function getDimLevel(){
	 var zoneCode=$("zoneCode").value;
	 var dimLevel="";
	 GraphAction.getZoneLevel(zoneCode, {callback:function (data) {
         if(data != null){
        	   dimLevel+= data;
	         }
         $("dimLevel").value= dimLevel;
	   }
	});
}
function isGotoLink(){
	 var temp='';
	 var channelTypeCode=$("channelTypeCode").value;
	 var dimLevel=$("dimLevel").value;
	 if(channelTypeCode=='14001'||channelTypeCode=='10'||channelTypeCode=='1'){
		 if(dimLevel>=4){
			 temp='0';//不跳转
		 }else{
			 temp='1';//跳转
		 }
	 }else{
		 if(dimLevel>=3){
			 temp='0';//不跳转
		 }else{
			 temp='1';//跳转
		 }
	 }
	 return temp;
}
dhx.ready(indexInit);