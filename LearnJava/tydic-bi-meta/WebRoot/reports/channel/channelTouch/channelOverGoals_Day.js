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
//查询系统参数
var zoneCode =   $('zoneCode').value==""?"0000":$("zoneCode").value;

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
var indexInit=function(){	
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     excuteInitData();
}
 

var excuteInitData=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	 var map=new Object();
	 map.startDate=$("startDate").value;
	 map.endDate=$("endDate").value; 
	 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
	 map.dateType="0";//天
	 map.field="ELEMENT_NUM";
     dhx.showProgress("正在执行......");
     AllChannelAction.getChannelOverGoals_Pg(map, function (res) {
     	$('chartdiv').innerHTML='';
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				            return;
 				        }
 				    chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId_"+cid, "100%", "200", "0", "0");
			    	chart.setDataXML(res.barChartMap);
 			        chart.render("chartdiv");
 					buildTable(res);
    });
}
function buildTable(data){
	
    tableStr ="<table  id='tab1' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	var  tableHead="<tr>";
	var  excelHeader="";
	      for(var i=0;i<headColumn.length;i++)
	      {
	       if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
		    {
		       tableHead +="<td nowrap bgcolor='#cde5fd'><span class='title'>"+headColumn[i]+"</span></td>";  
		       excelHeader += headColumn[i]+",";
	        }
	      }
	    tableHead+="</tr>";
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
		    		        	 tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
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
function exportExcel(){
		    var  startDate=$("startDate").value;//文本框\
		    var  endDate=$("endDate").value;//文本框
		    var  zone=$("zone").value;//文本框
		    var queryCond="日期从："+startDate+"    至："+endDate+"    区域："+zone;  
	   	    $("excelCondition").value=queryCond; 
	   	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_oneChart.jsp";
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
}
function exportImage(){
			var cid=$("cid").value;
   	        var charts = getChartFromId("ChartId_"+cid);   //生成的FusionCharts图表本身的标识
   	        charts.exportChart(); 
}
dhx.ready(indexInit);