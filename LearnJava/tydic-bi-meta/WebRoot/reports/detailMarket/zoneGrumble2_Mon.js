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
    //执行查询数据
     excuteInitData();
      
}


var excuteInitData=function(){
		 $("cid").value=Math.random();
		 var cid=$("cid").value;
		 $("cid1").value=Math.random();
		 var cid1=$("cid1").value;
		 $("cid2").value=Math.random();
		 var cid2=$("cid2").value;
		 $('chartdiv1').innerHTML='';
	     $('chartdiv2').innerHTML='';
	     $('chartdiv3').innerHTML='';
		 var map=new Object();
		 map.startDate=$("dateTime").value;
		 map.endDate=$("dateTime").value; 
		 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
 		 map.dateType="2";//月
 		 map.rptType="2";//报表2
 		 map.field="BYL";
 		 map.field1="_SUM";
 	     dhx.showProgress("正在执行......");
 	     DetailMarketAction.queryDetailMarket2_pg(map, function (res) {
	     	   dhx.closeProgress();
	 			 if (res == null) {
	 				   dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
	 				     return;
	 			 }else{
	 				chart1=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid, "100%", "230", "0", "0");
	 	 		    chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid1, "100%", "230", "0", "0");
	 	 		    chart3=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId3_"+cid2, "100%", "230", "0", "0");
	 	 		    chart1.setDataXML(res.pieChartMap);
		 	        chart2.setDataXML(res.lineChartMap);
		 	        chart3.setDataXML(res.barChartMap);
 			        chart1.render("chartdiv1");
 		 	        chart2.render("chartdiv2");
 		 	        chart3.render("chartdiv3");
	 				buildTable(res);
	 			 }
	    });
}
function buildTable(data){
    tableStr ="<table  id='tab1' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    autoRowSpan($("tab1"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	var tableHead="<tr>"
		+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>区域</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6'><span class='title'>本地抱怨量</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6'><span class='title'>本地抱怨率（‰）</span></td>"
		+"</tr>"
		+"<tr>"
		+"<td bgcolor='#cde5fd'><span class='title'>校园市场</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>农村市场</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>大客户</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>商业客户</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>城市家庭</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>流动市场</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>校园市场</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>农村市场</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>大客户</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>商业客户</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>城市家庭</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>流动市场</span></td>";
		tableHead+="</tr>";
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
   		             
   		            if(tempColumn.indexOf('_')==-1){ 
		    		      tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
   		            	  excelData += dataColumn[i][tempColumn]+"}";
		    		} 
   		      }
	    	  tableData +="</tr>";
			  excelData +="]";
	    	}   
	       } else {
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
 //导出
//导出
function exportImage(){
	var cid=$("cid").value;
    var cid1=$("cid1").value;
    var cid2=$("cid2").value;
    var charts1 = getChartFromId("ChartId1_"+cid);   //生成的FusionCharts图表本身的标识
    var charts2 = getChartFromId("ChartId2_"+cid1);   //生成的FusionCharts图表本身的标识
    var charts3 = getChartFromId("ChartId3_"+cid2);   //生成的FusionCharts图表本身的标识
    charts1.exportChart(); 
    charts2.exportChart(); 
    charts3.exportChart(); 
}
function exportExcel(){
		    var  startDate=$("dateTime").value;//时间
			var  endDate=$("dateTime").value;//时间
			var  zone=$("zone").value;//区域
		    var queryCond="月份："+startDate+"    区域："+zone; 
	   	    $("excelCondition").value=queryCond; 
	   	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_threeChartHeader.jsp";
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
}
 
dhx.ready(indexInit);