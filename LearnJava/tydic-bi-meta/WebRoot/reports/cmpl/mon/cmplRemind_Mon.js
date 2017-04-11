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

var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var parameters=$("parameters").value;

var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPath1");
var zoneConverter=dhx.extend({idColumn:"zoneCode",pidColumn:"zoneParCode",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
dwrCaller.addAction("querySubZoneCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZone1(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
var indexInit=function(){
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
   //加载业务类型树
   //  loadProdTypeTree(prodTypeCode, queryform);
     //执行查询数据
     excuteInitData();
}


var excuteInitData=function(){
	 var map=new Object();
     map.dateTime     =$("dateTime").value;
     map.zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;

     $("cid").value=Math.random();
     var cid=$("cid").value;
     $("cid1").value=Math.random();
     var cid1=$("cid1").value;

     zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
     
     map.reportId="10012";
     map.parameters=parameters; 
     map.values= $("dateTime").value.replaceAll("-", "")+"$"+zoneCode;
     
     dhx.showProgress("正在执行......");
     ServiceReActiveAction.getCmplRemind_pg(map, function (res) {
       $('chartdiv1').innerHTML='';
       $('chartdiv2').innerHTML='';
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				            return;
 				        }
 				     chart1=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid, "100%", "200", "0", "0");
 			    	 chart2=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId2_"+cid1, "100%", "200", "0", "0");
 			         chart1.setDataXML(res.pieChartMap);
 			         chart2.setDataXML(res.pieChartMap2);
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
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
var  excelHeader="";
var tableHead="</tr>";
      for(var i=0;i<headColumn.length;i++)
      {
       if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	    {
	       excelHeader += headColumn[i]+",";
	       tableHead +="<td nowrap bgcolor='#cde5fd'><span class='title'>"+headColumn[i]+"</span></td>"; 
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
		    		            	   if(tempColumn == '产品类型' && i!=0)
			    		        	 {
					    		         tableData +="<td>"+dataColumn[i][tempColumn]+"</td>";	 	
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

function exportImage(){
	var cid=$("cid").value;
    var cid1=$("cid1").value;
    var charts1 = getChartFromId("ChartId1_"+cid);   //生成的FusionCharts图表本身的标识
    var charts2 = getChartFromId("ChartId2_"+cid1);   //生成的FusionCharts图表本身的标识
    charts1.exportChart(); 
    charts2.exportChart(); 
}

function exportExcel(){
	var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//月份
    var  zoneCode=$("zone").value;//区域
    var  queryCond="月份："+dateTime+"    区域："+zoneCode;
    $("excelCondition").value=queryCond; 
    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_chart.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
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