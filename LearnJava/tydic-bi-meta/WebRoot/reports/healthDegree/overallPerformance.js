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
	$("cid2").value=Math.random();
	var cid2=$("cid2").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
    var map=new Object();
    map.dateTime   =$("dateTime").value;
    map.zoneCode  =$("zoneCode").value==""?"0000":$("zoneCode").value;
    map.field   ="IND_SCORE";
    dhx.showProgress("正在执行......");
    HealthDegreeAction.getOverallPerformance_Pg(map, function (res) {
    	$('chartdiv1').innerHTML='';
    	$('chartdiv2').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
		chart1 = new FusionCharts(base + "/js/Charts/MSColumn2D.swf","ChartId1_"+ cid1, "100%", "200", "0", "0");
		chart2 = new FusionCharts(base + "/portal/module/portal/map/FCMap_GuangDong.swf","ChartId2_"+ cid2, "100%", "200", "0", "0");
		chart1.setDataXML(res.barChartMap);
		chart2.setDataXML(res.chartMap);
		chart1.render("chartdiv1");
		chart2.render("chartdiv2");
		$("div_src").style.display = "none";
		$("div_src").style.zindex = "-1";
		buildTable(res);
   });	
}
function buildTable(data){
    tableStr ="<table id='tab1' width='100%' height='250px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
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
		    		            	   if(tempColumn=='大区'){
		    		            		   tableData +="<td nowrap align='center' onclick=\"goMenuHref('"+dataColumn[i].大区+"','"+dataColumn[i].大区_编码+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
		    		            	   }else if(tempColumn=='1月'){
	    		            			   tableData +="<td><span title='"+dataColumn[i].ind_score_1+"'>"+dataColumn[i][tempColumn]+"</span></td>"; 
	    		            		   }else if(tempColumn=='2月'){
	    		            			   tableData +="<td><span title='"+dataColumn[i].ind_score_2+"'>"+dataColumn[i][tempColumn]+"</span></td>"; 
	    		            		   }else if(tempColumn=='3月'){
	    		            			   tableData +="<td><span title='"+dataColumn[i].ind_score_3+"'>"+dataColumn[i][tempColumn]+"</span></td>"; 
	    		            		   }else if(tempColumn=='4月'){
	    		            			   tableData +="<td><span title='"+dataColumn[i].ind_score_4+"'>"+dataColumn[i][tempColumn]+"</span></td>"; 
	    		            		   }else if(tempColumn=='5月'){
	    		            			   tableData +="<td><span title='"+dataColumn[i].ind_score_5+"'>"+dataColumn[i][tempColumn]+"</span></td>"; 
	    		            		   }else if(tempColumn=='6月'){
	    		            			   tableData +="<td><span title='"+dataColumn[i].ind_score_6+"'>"+dataColumn[i][tempColumn]+"</span></td>"; 
	    		            		   }else if(tempColumn=='7月'){
	    		            			   tableData +="<td><span title='"+dataColumn[i].ind_score_7+"'>"+dataColumn[i][tempColumn]+"</span></td>"; 
	    		            		   }else if(tempColumn=='8月'){
	    		            			   tableData +="<td><span title='"+dataColumn[i].ind_score_8+"'>"+dataColumn[i][tempColumn]+"</span></td>"; 
	    		            		   }else if(tempColumn=='9月'){
	    		            			   tableData +="<td><span title='"+dataColumn[i].ind_score_9+"'>"+dataColumn[i][tempColumn]+"</span></td>"; 
	    		            		   }else if(tempColumn=='10月'){
	    		            			   tableData +="<td><span title='"+dataColumn[i].ind_score_10+"'>"+dataColumn[i][tempColumn]+"</span></td>"; 
	    		            		   }else if(tempColumn=='11月'){
	    		            			   tableData +="<td><span title='"+dataColumn[i].ind_score_11+"'>"+dataColumn[i][tempColumn]+"</span></td>"; 
	    		            		   }else if(tempColumn=='12月'){
	    		            			   tableData +="<td><span title='"+dataColumn[i].ind_score_12+"'>"+dataColumn[i][tempColumn]+"</span></td>"; 
	    		            		   } else if(tempColumn=='排名'){
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
// 查询
var queryData = function() {
	excuteInitData();
}
var goMenuHref = function(zoneName, zoneCode) {
	var dateTime = $("dateTime").value;
	var url = "/reports/healthDegree/businessItem.jsp?zoneCode=" + zoneCode
			+ "&dateTime=" + dateTime+ "&transmitName=" + zoneName + "业务项";
	return parent.openTreeTab("148002" + zoneCode + dateTime, zoneName + "业务项",
			base + url, 'top');
}
function exportImage() {
	var cid1 = $("cid1").value;
	var cid2 = $("cid2").value;
	var charts1 = getChartFromId("ChartId1_" + cid1); // 生成的FusionCharts图表本身的标识
	var charts2 = getChartFromId("ChartId2_" + cid2); // 生成的FusionCharts图表本身的标识
	charts1.exportChart();
	charts2.exportChart();
}
function exportExcel() {
	var dateTime = $("dateTime").options[$("dateTime").selectedIndex].text;// 时间
	var queryCond = "月份：" + dateTime;
	$("excelCondition").value = queryCond;
	var url = getBasePath()
			+ "/portalCommon/module/procedure/impExcel/implExcel_chart.jsp";
	document.forms[0].method = "post";
	document.forms[0].action = url;
	document.forms[0].target = "hiddenFrame";
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