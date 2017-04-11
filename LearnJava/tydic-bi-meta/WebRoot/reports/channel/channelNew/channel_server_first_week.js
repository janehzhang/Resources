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
var zoneCode =   $('zoneCode').value==""?"0000":$("zoneCode").value;
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
    dwrCaller.addAutoAction("loadChannelTypeTreePart","NewTwoChannelAction.queryChannelTypePathPart");
	var channelTypeConverter=dhx.extend({idColumn:"channelTypeCode",pidColumn:"channelTypeParCode",
	    textColumn:"channelTypeName"
	},treeConverter,false);
	dwrCaller.addDataConverter("loadChannelTypeTreePart",channelTypeConverter);
	dwrCaller.addAction("querySubChannelTypePart",function(afterCall,param){
	    var tempCovert=dhx.extend({isDycload:true},channelTypeConverter,false);
	    NewTwoChannelAction.querySubChannelTypePart_new(param.id,function(data){
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
   //  indexExp();
}
 

var excuteInitData=function(){
 
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	 var map=new Object();
	 map.level=1;
	 
	 map.dateTime=$("dateTime").value;
	 map.startDate=$("dateTime").value;
	 map.endDate=$("dateTime").value; 
	 
	 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
	 map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
	 map.dateType="1";//周
	 map.showAllChannel=flag;
	  
     dhx.showProgress("正在执行......");
    
     NewTwoChannelAction.getChannelSerNewFirst_Pg(map, function (res) {
     	 
    	 dhx.closeProgress();
 		 if (res == null) {
 			 dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 			 return;
 		 }
 		 
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
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>大区</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>渠道大类</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>渠道小类</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%'><span class='title' align='center' >查询</span></td>" 
	    +"<td bgcolor='#cde5fd' style='width:6%'><span class='title' align='center'>办理</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%'><span class='title' align='center'>充缴</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>咨询</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>故障</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>投诉</span></td>"
 	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>其它</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>总服务量</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>占比</span></td>"
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
//钻取
var drillArea=function(zoneName,zoneCode,channelTypeCode)
{	
	var startDate=$("startDate").value;
	var endDate=$("endDate").value;
	var selectCol=$("selectCol").value;
	var url="/reports/channel/newChannel/AreaServer_Day.jsp?zoneCode="+zoneCode+"&startDate="+startDate+
	"&endDate="+endDate+"&channelTypeCode="+channelTypeCode+"&menuId="+menuId;
	return parent.openTreeTab("164010"+zoneCode+channelTypeCode, "区域渠道服务一级周报表"+"["+zoneName+"]", base+url, 'top');
	
}
//查询
var queryData=function(){
 
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
	AllChannelAction.getParChannel($('channelTypeCode').value,$('actType').value,function (res) {
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
//通过列改变图形
function changeCol(obj){
	var titileInfo=$("channelType").value+"->"+$("selectCol").options[$("selectCol").selectedIndex].text;
	$("titleInfo1").innerHTML=titileInfo;
	 var map=new Object();
	 map.dateTime=$("endDate").value;
     map.field=$("selectCol").value;
     //map.typeList=$("typeList").value;
     map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value; 
     map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;   
	 map.startDate=$("startDate").value;
	 map.endDate=$("endDate").value; 
	 map.dateType="0";//天
	 map.showAllChannel=flag;
	 map.actType=$("actType").value;//分类类型：1传统新媒体渠道，2其他渠道，3人工自助渠道     
     buildChart(map);
}
//加载配置图形
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
//构建折线图和柱状图
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
		    var  startDate=$("dateTime").value;//文本框\
		    var  endDate=$("dateTime").value;//文本框
		    var  zone=$("zone").value;//文本框
		    var  channelType=$("channelType").value;//文本框
		    var queryCond="查询周："+startDate+"    区域："+zone+"    渠道类型: "+channelType; 
		     
	   	    $("excelCondition").value=queryCond; 
	    	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implChannelMoreExcel.jsp";
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
				$("channel").value!='渠道小类';
			}
			excuteInitData();
			loadChannelTypeTreePart_all(temp,queryform);
}
//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 var explainData="";
	 var map=new Object(); 
	 map.rptId="1001";
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
    		 } 
	         }
	   }
	});
}
dhx.ready(indexInit);