/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();

var userInit=function(){ 
    excuteInitData();
}

var excuteInitData=function(){
    var map=new Object();
    $("cid").value=Math.random();
	var cid=$("cid").value;
    map.dateTime   =$("dateTime").value;
    map.zoneCode  =$("zoneCode").value==""?"0001":$("zoneCode").value;
    map.field="IND_SCORE";
    dhx.showProgress("正在执行......");
    HealthDegreeAction.getBusinessItem_Pg(map, function (res) {
    	$('chartdiv').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
		chart= new FusionCharts(base + "/js/Charts/PowerCharts/Radar.swf","ChartId_" + cid, "950", "350", "0", "0");
		chart.setDataXML(res.radarMap);
		chart.render("chartdiv");
		buildTable(res);
   });	
}
function buildTable(data){
    tableStr ="<table id='tab1' width='950' height='250px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
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
	var excelHeader="";
	var tableHead="<tr>";
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
		    		            	   if(tempColumn=='总得分'){
		    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref('"+dataColumn[i].地市+"','"+dataColumn[i].地市_ID+"','','')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
		    		            	   }else if(tempColumn=='即时回访满意率'){
		    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref('"+dataColumn[i].地市+"','"+dataColumn[i].地市_ID+"','1001','即时回访满意率')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
		    		            	   }else if(tempColumn=='投诉率'){
		    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref('"+dataColumn[i].地市+"','"+dataColumn[i].地市_ID+"','1002','投诉率')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
		    		            	   }else if(tempColumn=='客户维系'){
		    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref('"+dataColumn[i].地市+"','"+dataColumn[i].地市_ID+"','1003','客户维系')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
		    		            	   }else if(tempColumn=='宽带专项服务提升'){
		    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref('"+dataColumn[i].地市+"','"+dataColumn[i].地市_ID+"','1004','宽带专项服务提升')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
		    		            	   }else if(tempColumn=='“划小”责任提升'){
		    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref('"+dataColumn[i].地市+"','"+dataColumn[i].地市_ID+"','1005','“划小”责任提升')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
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
var  goMenuHref=function(zoneName,zoneCode,indexId,indexName){
	   var dateTime=$("dateTime").value;
	   var url="/reports/healthDegree/businessIndex.jsp?zoneCode="+zoneCode+
		"&dateTime="+dateTime+"&indexId="+indexId+ "&transmitName=" + zoneName +indexName+"业务指标"; 
		 return parent.openTreeTab("148003"+zoneCode+dateTime+indexId, zoneName+indexName+"业务指标", base+url, 'top');
}
function exportImage() {
	 var cid=$("cid").value;
	 var charts = getChartFromId("ChartId_"+cid);   //生成的FusionCharts图表本身的标识
	 charts.exportChart(); 
}
function exportExcel() {
	var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
    var queryCond="月份："+dateTime;
	$("excelCondition").value=queryCond; 
	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_oneRadarChar.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
//第一列合并
var autoRowSpan=function(tb,row,col){//0,16
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
dhx.ready(userInit);