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

var titileInfo=$("ind").options[$("ind").selectedIndex].text+"->当月值";
var titileInfo1=$("ind").options[$("ind").selectedIndex].text+"->当月值";
$("titleInfo1").innerHTML=titileInfo;
$("titleInfo2").innerHTML=titileInfo1;

//查询系统参数
var indId="";
var indName="";

var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var ind = $("ind").value;

//by qx
tZoneCode=$("zoneCode").value;
tDefaultZoneCode=$("defaultZoneCode").value;
if(tZoneCode!=tDefaultZoneCode){//下钻
    $("changeCode").value='1';
}else{
	$("changeCode").value='2';
}
var changeCode = $("changeCode").value;
dwrCaller.addAutoAction("getCmplIndexExp","CmplIndexAction.getCmplIndexExp","cmplIndexMon");

var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCodeGrid");
var zoneConverter=dhx.extend({idColumn:"zoneCode",pidColumn:"zoneParCode",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
dwrCaller.addAction("querySubZoneCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZoneCodeGrid(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});

var indexInit=function(){
    //加载预警信息
    loadIndexCdWarn(menuName);
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     //执行查询数据
     excuteInitData(changeCode);
     indexExp();
}

var excuteInitData=function(changeCode){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	 var map=new Object();
     map.zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
     map.dateTime     =$("dateTime").value;
     map.prodTypeCode     ='-1';
     map.ind=$("ind").value;
     map.field="CURRENT_VALUE";
     map.rptIndex="cmplIndexMon"; 
     map.changeCode=changeCode;
     dhx.showProgress("正在执行......");
     CmplIndexAction.getCmplIndex_pg(map, function (res) {
     	$('chartdiv1').innerHTML='';
     	$('chartdiv2').innerHTML='';
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				            return;
 				        }
 				     chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
 			    	 chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
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
		    		            	   if(tempColumn == '分公司' && i!=0)
			    		        	 {
		    		            		   if(dataColumn[i].区域_层级<'6'){
					    		        	     tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].分公司+"','"+dataColumn[i].地市_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
					    		        	  }else{
					    		        		  tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
					    		        	  }
			    		        	 }else if(tempColumn == '指标项')
  			    		        	 {
				    		        	   tableData +="<td nowrap align='left'>"+dataColumn[i][tempColumn]+"</td>";	 
				    		         }else{
				    		        	 tableData +="<td>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>"; 
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
	changeCode=getChangeCode(); //by qx
	excuteInitData(changeCode);
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
	var url="/reports/cmpl/mon/cmplIndex_Mon.jsp?dateTime="+$("dateTime").value+"&zoneCode="
	+zoneCode+"&ind="+$('ind').value+"&menuId="+menuId;
	return parent.openTreeTab("122015"+zoneCode,"投诉类指标月监测报表"+"["+zoneName+"]",base+url,'top');	
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
        map.prodType='-1';
        map.ind=$("ind").value;
        map.field="CURRENT_VALUE";
        map.rptIndex="cmplIndexMon"; 
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
		  CmplIndexAction.loadSet21AreaChart(map, {callback:function (res) {
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
//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 var explainData="";
	 var map=new Object(); 
	 map.rptId="2";
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
//通过列改变图形
function changeCol(obj){
	$("titleInfo1").innerHTML=$("ind").options[$("ind").selectedIndex].text+"->当月值";
	$("titleInfo2").innerHTML=$("ind").options[$("ind").selectedIndex].text+"->当月值";
	var map=new Object();
	map.dateTime=$("dateTime").value;
    map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
    map.prodType='-1';
    map.ind=obj.value;
    map.field="CURRENT_VALUE";
    map.rptIndex="cmplIndexMon"; 
    map.changeCode=$("changeCode").value;
    buildChart(map);
}
//加载配置图形
function buildChart(map){
	dhx.showProgress("正在执行......");
	CmplIndexAction.getCmplIndexChange(map, {callback:function (res) {
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
function exportImage(){
			var cid=$("cid").value;
			var cid1=$("cid1").value;
   	        var charts2 = getChartFromId("ChartId2_"+cid);   //生成的FusionCharts图表本身的标识
   	         var charts1 = getChartFromId("ChartId1_"+cid1);   //生成的FusionCharts图表本身的标识
   	        charts1.exportChart(); 
   	        charts2.exportChart(); 
   	    }	
function exportExcel(){
	var  dateTime=$("dateTime").value;//文本框
	    var  zone=$("zone").value;//文本框
	    var  indexName=$("ind").options[$("ind").selectedIndex].text;//文本框
	    var queryCond="日期："+dateTime+" 区域："+zone;
   	    $("excelCondition").value=queryCond; 
 	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_chart.jsp";
		document.forms[0].method = "post";
		document.forms[0].action=url;
		document.forms[0].target="hiddenFrame";
		document.forms[0].submit();
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
dhx.ready(indexInit);