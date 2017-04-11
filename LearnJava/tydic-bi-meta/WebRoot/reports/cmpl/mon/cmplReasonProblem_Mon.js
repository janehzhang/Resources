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

//查询条件参数

var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;

var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** Tree head **/
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
var indexInit=function(){
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     //执行查询数据
     excuteInitData();
}


var excuteInitData=function(){
	 var map=new Object();
	 map.reportId="10005";
	 map.parameters="REGION_ID$MONTH_ID$CMPL_REASON1_ID$CHANNEL_TYPE_ID"; 
	 var zoneId=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
     map.values= zoneId+"$"+$("dateTime").value.replaceAll("-", "")+"$"+$("reasonId").value+"$"+$("channelType").value;
     map.field=$("ind").value;
     map.rptIndex="cmplReasonProblemMon"; 
     map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
     map.dateTime= $("dateTime").value;
     map.reasonId= $("reasonId").value;
     map.channelType= $("channelType").value;
     dhx.showProgress("正在执行......");
     CmplIndexAction.getCmplSkip_pg(map, function (res) {
     	$('chartdiv1').innerHTML='';
     	$('chartdiv2').innerHTML='';
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				            return;
 				        }
 				     chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "500", "230", "0", "0");
 			    	 chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+Math.random(), "500", "230", "0", "0");
 			         chart1.setDataXML(res.barChartMap);
 			         chart2.setDataXML(res.lineChartMap);
 			         chart1.render("chartdiv1");
 			         chart2.render("chartdiv2"); 
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
var  excelHeader="";
var  tableHead="<tr>"
	+"<td bgcolor='#cde5fd' style='width:540px' colspan='3'><span class='title'>越级申诉/投诉原因</span></td>"
	+"<td bgcolor='#cde5fd' style='width:540px' colspan='2'><span class='title'>业务投诉量</span></td>"
	+"<td bgcolor='#cde5fd' style='width:540px' colspan='3'><span class='title'>业务投诉率</span></td>"
	+"</tr>"
	+"<tr>"
	+"<td bgcolor='#cde5fd'><span class='title'>第一级</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>第二级</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>第三级</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>业务投诉量</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>平均值</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>业务投诉率</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>";
tableHead+="</tr>";
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
		    		            	   if(tempColumn == '第一级'&&i!=0)
			    		        	 {
			    		        	   tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].第一级+"','"+dataColumn[i].第一级_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
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
//钻取
var drillArea=function(reason,reasonId)
{	
	$("reasonId").value=reasonId;
	var op=new Option(reason,reasonId);
	op.setAttribute("selected",true);
	 $('reasonId').add(op);
		excuteInitData();
}
//通过列改变图形
function changeCol(obj){
	var map=new Object();
	map.field=obj.value;
    map.rptIndex="cmplReasonProblemMon"; 
    map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
    map.dateTime= $("dateTime").value;
    map.reasonId= $("reasonId").value;
    map.channelType= $("channelType").value;
    buildChart(map);
}
//加载配置图形
function buildChart(map){
	CmplIndexAction.getCmplSkipChange(map, {callback:function (res) {
    	         if(res != null){
                        buildBLChart(res);
		         }
	}
		});
   }
//构建折线图和柱状图
function  buildBLChart(map){
	   $("chartdiv1").innerHTML="";
	   $("chartdiv2").innerHTML="";
	   chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "500", "230", "0", "0");
	   chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+Math.random(), "500", "230", "0", "0");
	   chart1.setDataXML(map.barChartMap);
       chart2.setDataXML(map.lineChartMap);
       chart1.render("chartdiv1");
       chart2.render("chartdiv2"); 
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
dhx.ready(indexInit);