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
//var globDeptTree = null;

var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
//var deptCode =   $('deptCode').value==""?"-1":$("deptCode").value;

if(user['zoneId']!='1') { //不是广东省  （钻取权限控制）
	var array=document.getElementsByName('city');
	    for(var i=0;i<array.length;i++){ 
	      array[i].style.display = "none";
	}
}

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
//责任部门树
/*dwrCaller.addAutoAction("loadDeptTree","CmplDeptAction.queryDeptByPathCode");
var deptConverter=dhx.extend({idColumn:"cmplDeptCode",pidColumn:"cmplDeptParCode",
    textColumn:"cmplDeptName"
},treeConverter,false);
dwrCaller.addDataConverter("loadDeptTree",deptConverter);
dwrCaller.addAction("querySubDeptCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},deptConverter,false);
    CmplDeptAction.querySubDeptCode(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});*/

var indexInit=function(){
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
    // loadDeptTreeChkBox(deptCode,queryform);
     //执行查询数据
     excuteInitData();
}


var excuteInitData=function(){
	 var map=new Object();
	 map.reportId="30002";
	 map.parameters="CMPL_REGION_ID$MONTH_ID$DUTY_DEPT_ID"; 
	 var zoneId=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
	 var deptCode="-1";
     map.values= zoneId+"$"+$("dateTime").value.replaceAll("-", "")+"$"+deptCode;
     map.field="OFFLINE_VALUE";
     map.rptIndex="cmplOffLineDeptMon"; 
     map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
     map.dateTime= $("dateTime").value;
     map.deptCode="-1";
     dhx.showProgress("正在执行......");
     CmplIndexAction.getCmplSkip_pg(map, function (res) {
     	//$('chartdiv1').innerHTML='';
     	//$('chartdiv2').innerHTML='';
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				            return;
 				        }
 				     /*chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "500", "230", "0", "0");
 			    	 chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+Math.random(), "500", "230", "0", "0");
 			         chart1.setDataXML(res.barChartMap);
 			         chart2.setDataXML(res.lineChartMap);
 			         chart1.render("chartdiv1");
 			         chart2.render("chartdiv2"); 
 					 $("city").value='切换地市'*/
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
		    		            	   if(tempColumn == '责任班组'){
		    		            		   tableData +="<td nowrap align='left'>"+dataColumn[i][tempColumn]+"</td>"; 
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
var drillArea=function(zoneName,zoneCode)
{	
	var url="/reports/cmpl/mon/cmplOffOnlineDept_Mon.jsp?dateTime="+$("dateTime").value+"&zoneCode="
	+zoneCode;
	return parent.openTreeTab("123001"+zoneCode,"宽带及移动投诉用户离网责任部门报表"+"["+zoneName+"]",base+url,'top');	
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
var lookCity = function (obj){
    var map=new Object();
        map.dateTime=$("dateTime").value;
        map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
        map.prodType=$("prodTypeCode").value;
        map.ind=$("ind").value;
        map.field="OFFLINE_VALUE";
        map.rptIndex="cmplIndexMon"; 
		  if(obj.value=='切换地市'){
			  obj.value='切换区域';
			  map.changeZone="1";
		  }else{
			  obj.value='切换地市';
			  map.changeZone="0";
		  }  
		  CmplIndexAction.loadSet21AreaChart(map, {callback:function (res) {
 	         if(res != null){
                     build21Chart(res);
		         }
		   }
		});
}
function  build21Chart(map){
	   $("chartdiv1").innerHTML="";
	   chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId3_"+Math.random(), "500", "230", "0", "0");
	   chart.setDataXML(map.XML);
	   chart.render("chartdiv1");
	}
//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 dwrCaller.executeAction("getCmplIndexExp",function(data){
		 var tableStr="<table width='970' border='0' cellpadding='0' cellspacing='0'>";
		 if(data&&data.expList.length>0){	
			 for(var i=0;i<data.expList.length;i++){
				 tableStr =tableStr+
		         "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
		           +"<td align=\"left\"><font style='font-weight:bold;' >&nbsp;"+data.expList[i].INDEX_NAME+"  :  "+"</font>"
		           +data.expList[i].INDEX_EXPLAIN+"</td>"   
		           +"</tr>";
			 }
			 tableStr +="</table>"; 
			 $('index_exp').innerHTML=tableStr;
		 } 
	 });
}
//通过列改变图形
function changeCol(obj){
	var map=new Object();
	map.dateTime=$("dateTime").value;
    map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
    map.prodType=$("prodTypeCode").value;
    map.ind=obj.value;
    map.field="OFFLINE_VALUE";
    map.rptIndex="cmplIndexMon"; 
    buildChart(map);
}
//加载配置图形
function buildChart(map){
	CmplIndexAction.getCmplIndexChange(map, {callback:function (res) {
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
	   $("city").value="切换地市";
	   chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "500", "230", "0", "0");
	   chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+Math.random(), "500", "230", "0", "0");
	   chart1.setDataXML(map.barChartMap);
       chart2.setDataXML(map.lineChartMap);
       chart1.render("chartdiv1");
       chart2.render("chartdiv2"); 
   }
dhx.ready(indexInit);