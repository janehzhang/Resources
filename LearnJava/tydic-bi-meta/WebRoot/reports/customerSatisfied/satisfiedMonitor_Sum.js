/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();

var zoneCode  =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var zoneCode2 =   $('zoneCode2').value==""?userInfo['localCode']:$("zoneCode2").value;
var zoneCode1 =   $('zoneCode1').value==""?userInfo['localCode']:$("zoneCode1").value;

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
//当前系统的主页Path
var base = getBasePath();
//查询条件参数   
var userInit=function(){
	//1.加载地域树 
    loadZoneTreeChkBox(zoneCode,queryform);
    loadZoneTreeChkBox2(zoneCode2,queryform);
    loadZoneTreeChkBox1(zoneCode1,queryform);
    //执行查询数据
	//queryData();
    //queryData1();
    queryData2();
}
//日
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
	queryData2();
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
	queryData();
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
	queryData1();
}
//周报
var queryData=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
    var map=new Object();
    map.startDate =$("dateTime").value;
    map.endDate   =$("dateTime").value;
    map.dateType   ="1"; 
    map.field   ="SATISFY_LV"; 
    map.zoneCode   =$("zoneCode").value;
    map.isIshop   =$("isIshop").value;
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getCustomerSatisfiedSum(map, function (res) {
    	$('chartdiv').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				     chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid, "500", "230", "0", "0");
			    	 chart.setDataXML(res.barChartMap);
			         chart.render("chartdiv");
					 buildTable(res);
   });	
}
function buildTable(data){
    tableStr ="<table id='tab1' width='100%' height='250px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
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
				    		          if(tempColumn == '触点') 
				    		          {
				    		        	 tableData +="<td nowrap onclick=\"openMenuHref('"+dataColumn[i].触点+"','"+dataColumn[i].触点_编码+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
				    		          }
				    		          else
				    		          {
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
//查询月报
var queryData1=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
    var map=new Object();
    map.startDate =$("dateTime1").value;
    map.endDate   =$("dateTime1").value;
    map.dateType   ="2"; //月报
    map.field   ="SATISFY_LV"; 
    map.zoneCode   =$("zoneCode1").value;
    map.isIshop   =$("isIshop").value;
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getCustomerSatisfiedSum(map, function (res) {
    	$('chartdiv1').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				     chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid, "500", "230", "0", "0");
			    	 chart.setDataXML(res.barChartMap);
			         chart.render("chartdiv1");
					 buildTable1(res);
   });
} 
function buildTable1(data){
    tableStr ="<table id='tab1' width='100%' height='250px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader1(data.headColumn);
    tableStr+=tableData1(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
      $('chartTable1').innerHTML=tableStr;
    //  autoRowSpan($("mytbl"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader1(headColumn){
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
function tableData1(dataColumn,headColumn){
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
				    		          if(tempColumn == '触点')
				    		          {
				    		        	 tableData +="<td nowrap onclick=\"openMenuHref1('"+dataColumn[i].触点+"','"+dataColumn[i].触点_编码+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
				    		          }
				    		          else
				    		          {
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
//查询日报
var queryData2=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
    var map=new Object();
    map.startDate =$("startDate").value;
    map.endDate   =$("endDate").value;
    map.dateType   ="0"; 
    map.field   ="SATISFY_LV"; 
    map.zoneCode   =$("zoneCode2").value;
    map.isIshop   =$("isIshop").value;
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getCustomerSatisfiedSum(map, function (res) {
    	$('chartdiv2').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				     chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid, "500", "230", "0", "0");
			    	 chart.setDataXML(res.barChartMap);
			         chart.render("chartdiv2");
					 buildTable2(res);
   });
} 
function buildTable2(data){
    tableStr ="<table  id='tab1' width='100%' height='250px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader2(data.headColumn);
    tableStr+=tableData2(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
      $('chartTable2').innerHTML=tableStr;
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader2(headColumn){
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
function tableData2(dataColumn,headColumn){
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
				    		          if(tempColumn == '触点')
				    		          {
				    		        	 tableData +="<td nowrap onclick=\"openMenuHref2('"+dataColumn[i].触点+"','"+dataColumn[i].触点_编码+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
				    		          }
				    		          else
				    		          {
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
 *  打开链接菜单
 * 
 */
//周
var openMenuHref=function(actName,actType){
	var dateTime=$("dateTime").value;
	var zoneCode=$("zoneCode").value;
	var zoneName=$("zone").value;
	var url="/reports/customerSatisfied/satisfiedDetails/satisfiedDetails_Sum.jsp?zoneCode="+zoneCode+"&dateTime="+dateTime+"&vTypeId="+actType+"&menuId="+menuId;
  return parent.openTreeTab("133007"+actType, actName+"满意度测评详情"+"["+zoneName+"]", base+url, 'top');
}
//月
var openMenuHref1=function(actName,actType){
	var dateTime=$("dateTime1").value;
	var zoneCode=$("zoneCode1").value;
	var zoneName=$("zone1").value;
	var url="/reports/customerSatisfied/satisfiedDetails/satisfiedDetails_Sum.jsp?zoneCode="+zoneCode+"&dateTime1="+dateTime+"&vTypeId="+actType+"&menuId="+menuId;
  return parent.openTreeTab("133007"+actType, actName+"满意度测评详情"+"["+zoneName+"]", base+url, 'top');
}
//日
var openMenuHref2=function(actName,actType){
	var startTime=$("startDate").value;
    var endTime=$("endDate").value;
	var zoneCode=$("zoneCode2").value;
	var zoneName=$("zone2").value;
	var url="/reports/customerSatisfied/satisfiedDetails/satisfiedDetails_Sum.jsp?zoneCode="+zoneCode+"&startDate="+startTime+"&endDate="+endTime+"&vTypeId="+actType+"&menuId="+menuId;
  return parent.openTreeTab("133007"+actType, actName+"满意度测评详情"+"["+zoneName+"]", base+url, 'top');
}
function exportImage(){
    var cid=$("cid").value;
    var charts = getChartFromId("ChartId1_"+cid);   //生成的FusionCharts图表本身的标识
    charts.exportChart(); 
}
//月
  function exportExcel1(){
    var  dateTime=$("dateTime1").options[$("dateTime1").selectedIndex].text;//时间
    var  zone=$("zone1").value;//区域
    var queryCond="月份："+dateTime+"    区域："+zone;
    $("excelCondition").value=queryCond; 
    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_oneChart.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
//周
  function exportExcel(){
	var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
	var  zone=$("zone").value;//区域
	var queryCond="周期："+dateTime+"    区域："+zone;
	$("excelCondition").value=queryCond; 
	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_oneChart.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
  }
//日
  function exportExcel2(){
	var  startTime=$("startDate").value;//时间
	var  endTime=$("endDate").value;//时间
	var  zone=$("zone2").value;//区域
	var queryCond="日期从："+startTime+"   至："+endTime+"    区域："+zone;
	$("excelCondition").value=queryCond; 
	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_oneChart.jsp";
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
dhx.ready(userInit);