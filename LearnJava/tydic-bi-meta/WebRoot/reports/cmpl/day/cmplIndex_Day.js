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
var zoneCode=$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;

var titileInfo=$("ind").options[$("ind").selectedIndex].text+"->当日值";
var titileInfo1=$("ind").options[$("ind").selectedIndex].text+"->本期累计";
$("titleInfo1").innerHTML=titileInfo;
$("titleInfo2").innerHTML=titileInfo1;

//by qx
tZoneCode=$("zoneCode").value;
tDefaultZoneCode=$("defaultZoneCode").value;
if(tZoneCode!=tDefaultZoneCode){//下钻
    $("changeCode").value='1';
}else{
	$("changeCode").value='2';
}
var changeCode = $("changeCode").value;

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
dwrCaller.addAutoAction("getCmplIndexExp","CmplIndexAction.getCmplIndexExp","cmplIndexDay");
var indexInit=function(){
	 //加载预警信息
     loadIndexCdWarn(menuName);
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     getUserSubCode();
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
     map.ind =$("ind").value==""?'1':$("ind").value;
     map.rptIndex="cmplIndexDay";
     map.field="CURRENT_NUM";
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
 				     chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "1");
 			    	 chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "1");
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
*   构造表格数据
* @param {Object} data
*/
function tableData(dataColumn,headColumn){
  var tableData="";
  var  excelData="";
  var zoneCode=$("zoneCode").value;
  var userCode=$("userCode").value;
  var userCodeData=$("userCodeData").value;
     if(dataColumn&&dataColumn.length>0)
	    {  
	       for(var i=0;i<dataColumn.length;i++)
	       {
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	if(userCode==dataColumn[i].地市_ID){
	    	  for(var j=0;j<headColumn.length;j++)
   		         {
   		               var tempColumn=headColumn[j]; 
		    		       
   		               if(tempColumn.indexOf('_')==-1)
		    		       {
		    		          
   		            	   	 if(dataColumn[i].指标_ID=='3'||dataColumn[i].指标_ID=='4'){
   		            	   		  if(tempColumn == '当日值')
   		            	   		   {
                                      tableData +="<td nowrap onclick=\"openMenuHref_1('"+dataColumn[i].地市_ID+"','"+dataColumn[i].指标_ID+"','"+dataColumn[i].指标项+"','"+dataColumn[i].分公司+"')\" class='unl'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>";		    		            	   			   
   		            	   		   }
   		            	   	 else if(tempColumn =='本期累计')
   		            	   		   {
   		            	   		   tableData +="<td nowrap onclick=\"openMenuHref_2('"+dataColumn[i].地市_ID+"','"+dataColumn[i].指标_ID+"','"+dataColumn[i].指标项+"','"+dataColumn[i].分公司+"')\" class='unl'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>";	   
   		            	   		   }
   		            	   	 else if(tempColumn =='上月同期累计')
   		            	   		   {
   		            	   	       tableData +="<td nowrap onclick=\"openMenuHref_3('"+dataColumn[i].地市_ID+"','"+dataColumn[i].指标_ID+"','"+dataColumn[i].指标项+"','"+dataColumn[i].分公司+"')\" class='unl'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>";	      
   		            	   		   }else if(tempColumn == '指标项')
 			    		        	 {
				    		        	   tableData +="<td nowrap align='left'>"+dataColumn[i][tempColumn]+"</td>";	 
				    		         }
   		            	   	 else
   		            	   	       {
	    		            	   		 if(tempColumn == '分公司' && i!=0 && dataColumn[i].区域_层级<'6')
			    		        		   {
			    		        	         tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].分公司+"','"+dataColumn[i].地市_ID+"')\" class='unl'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>";	 
			    		        	       }
			    		        		 else
			    		        		   {
			    		        		     tableData +="<td>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>";  
			    		        	       }
   		            	   	       }
		    		          }else{
			    		        		 if(tempColumn == '分公司' && i!=0 && dataColumn[i].区域_层级<'6')
			    		        		   {
			    		        	         tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].分公司+"','"+dataColumn[i].地市_ID+"')\" class='unl'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>";	 
			    		        	       }else if(tempColumn == '指标项')
		  			    		        	 {
					    		        	   tableData +="<td nowrap align='left'>"+dataColumn[i][tempColumn]+"</td>";	 
					    		         }
			    		        		 else
			    		        		   {
			    		        		    tableData +="<td>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>"; 
			    		        	      }
		    		        	  }
   		            	   
		    		             excelData += dataColumn[i][tempColumn]+"}";
		    		       } 
   		      }
	    	}else{
	    	  for(var j=0;j<headColumn.length;j++)
   		         {
   		               var tempColumn=headColumn[j]; 
		    		       
   		               if(tempColumn.indexOf('_')==-1)
		    		       {
		    		          
   		            	   	 if(dataColumn[i].指标_ID=='3'||dataColumn[i].指标_ID=='4'){
   		            	   		  if(tempColumn == '当日值')
   		            	   		   {
		   		            	   		if(userCodeData.indexOf("]"+zoneCode+"]")==-1){
		   		            	   		   tableData +="<td nowrap align='center'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>"; 
		   		            	   		}else{
                                           tableData +="<td nowrap onclick=\"openMenuHref_1('"+dataColumn[i].地市_ID+"','"+dataColumn[i].指标_ID+"','"+dataColumn[i].指标项+"','"+dataColumn[i].分公司+"')\" class='unl'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>";		    		            	   			   
		   		            	   		}
		   		            	   		}
   		            	   	 else if(tempColumn =='本期累计')
   		            	   		   {
			   		            	   	if(userCodeData.indexOf("]"+zoneCode+"]")==-1){
			   		            	     	tableData +="<td nowrap align='center'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>"; 
			   		            	   	}else{
			   		            	   	    tableData +="<td nowrap onclick=\"openMenuHref_2('"+dataColumn[i].地市_ID+"','"+dataColumn[i].指标_ID+"','"+dataColumn[i].指标项+"','"+dataColumn[i].分公司+"')\" class='unl'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>";	   
			   		            	   	}
   		            	   		   }
   		            	   	 else if(tempColumn =='上月同期累计')
   		            	   		   {
			   		            	  	if(userCodeData.indexOf("]"+zoneCode+"]")==-1){
			   		            	     	tableData +="<td nowrap align='center'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>"; 
			   		            	   	}else{
   		            	   	                tableData +="<td nowrap onclick=\"openMenuHref_3('"+dataColumn[i].地市_ID+"','"+dataColumn[i].指标_ID+"','"+dataColumn[i].指标项+"','"+dataColumn[i].分公司+"')\" class='unl'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>";	      
			   		            	   	}
			   		               }
   		            	   	 else if(tempColumn == '指标项')
 			    		        	 {
				    		        	   tableData +="<td nowrap align='left'>"+dataColumn[i][tempColumn]+"</td>";	 
				    		         }
   		            	   	 else
   		            	   	       {
	    		            	   		 if(tempColumn == '分公司' && i!=0 && dataColumn[i].区域_层级<'6')
			    		        		   {
			    		        	         tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].分公司+"','"+dataColumn[i].地市_ID+"')\" class='unl'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>";	 
			    		        	       }
			    		        		 else
			    		        		   {
			    		        		     tableData +="<td>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>";  
			    		        	       }
   		            	   	       }
		    		          }else{
			    		        		 if(tempColumn == '分公司' && i!=0 && dataColumn[i].区域_层级<'6')
			    		        		   {
			    		        	         tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].分公司+"','"+dataColumn[i].地市_ID+"')\" class='unl'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>";	 
			    		        	       }else if(tempColumn == '指标项')
		  			    		        	 {
					    		        	   tableData +="<td nowrap align='left'>"+dataColumn[i][tempColumn]+"</td>";	 
					    		         }
			    		        		 else
			    		        		   {
			    		        		    tableData +="<td>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>"; 
			    		        	      }
		    		        	  }
   		            	   
		    		             excelData += dataColumn[i][tempColumn]+"}";
		    		       } 
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
var lookCity = function (obj){
    var map=new Object();
	    map.zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
	    map.dateTime     =$("dateTime").value;
	    map.prodTypeCode     ='-1';
	    map.ind    =$("ind").value == ""?'1' : $("ind").value;
	    map.field="CURRENT_NUM";
	    map.rptIndex="cmplIndexDay"; 	
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

/**
 *  当日值
 * 
 */
var openMenuHref_1=function(zoneCode,indId,indName,regionName){
	 var time=$("dateTime").value;
	var url="/reports/cmpl/cmpl_list_mon.jsp?startDate="+time+"&endDate="+time+"&zoneCode="
	+zoneCode+"&prodTypeCode=-1"+"&indId="+indId+"&transmitName="+indName+"清单["+regionName+"]";
	return parent.openTreeTab("122013"+indId+zoneCode,indName+"清单["+regionName+"]",base+url,'top');
}
/**
 *  本期累计
 */
var openMenuHref_2=function(zoneCode,indId,indName,regionName){
	 var endTime=$("dateTime").value;
	 var startTime= endTime.substr(0,7)+"-01";
	var url="/reports/cmpl/cmpl_list_mon.jsp?startDate="+startTime+"&endDate="+endTime+"&zoneCode="
	+zoneCode+"&prodTypeCode=-1"+"&indId="+indId+"&transmitName="+indName+"清单["+regionName+"]";
	return parent.openTreeTab("122013"+indId+zoneCode,indName+"清单["+regionName+"]",base+url,'top');
}
/**
 *  上月同期累计 
 */
var openMenuHref_3=function(zoneCode,indId,indName,regionName){
	 var curTime=$("dateTime").value;
	 var year= curTime.substr(0,4);
	 var month=curTime.substr(5,2);
	 var day=curTime.substr(8,2);
	 var lastTime =year+"-"+month+"-01";
	var startTime=getUpMonth(lastTime);
    var endTime  =getUpMonth(curTime);
	var url="/reports/cmpl/cmpl_list_mon.jsp?startDate="+startTime+"&endDate="+endTime+"&zoneCode="
	+zoneCode+"&prodTypeCode=-1"+"&indId="+indId+"&transmitName="+indName+"清单["+regionName+"]";
	return parent.openTreeTab("122013"+indId+zoneCode,indName+"清单["+regionName+"]",base+url,'top');
}

//钻取
var drillArea=function(zoneName,zoneCode)
{	
	
	var url="/reports/cmpl/day/cmplIndex_Day.jsp?dateTime="+$("dateTime").value+"&zoneCode="
	+zoneCode+"&ind="+$('ind').value+"&menuId="+menuId;
	return parent.openTreeTab("122014"+zoneCode,"投诉类指标日监测报表"+"["+zoneName+"]",base+url,'top');
}
//通过列改变图形
function changeCol(obj){
	$("titleInfo1").innerHTML=$("ind").options[$("ind").selectedIndex].text+"->当日值";
	$("titleInfo2").innerHTML=$("ind").options[$("ind").selectedIndex].text+"->本期累计";
	
	var map=new Object();
	    map.zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
	    map.dateTime     =$("dateTime").value;
	    map.changeCode=$("changeCode").value;
	    map.ind    =$("ind").value == ""?'1' : $("ind").value;
	    map.field="CURRENT_NUM";
	    map.rptIndex="cmplIndexDay";
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
//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 var explainData="";
	 var map=new Object(); 
	 map.rptId="1";
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

function getUpMonth(t){
    var tarr = t.split('-');
    var year = tarr[0];                //获取当前日期的年
    var month = tarr[1];            //获取当前日期的月
    var day = tarr[2];                //获取当前日期的日
    var days = new Date(year,month,0);   
    days = days.getDate();//获取当前日期中的月的天数

    var year2 = year;
    var month2 = parseInt(month)-1;
    if(month2==0) {
        year2 = parseInt(year2)-1;
        month2 = 12;
    }
    var day2 = day;
    var days2 = new Date(year2,month2,0);
    days2 = days2.getDate();
    if(day2>days2) {
        day2 = days2;
    }
    if(month2<10) {
        month2 = '0'+month2;
    }

    var t2 = year2+'-'+month2+'-'+day2;
    return t2;
}
function exportImage(){
			var cid=$("cid").value;
			var cid1=$("cid1").value;
   	        var charts2 = getChartFromId("ChartId1_"+cid1);   //生成的FusionCharts图表本身的标识
   	         var charts1 = getChartFromId("ChartId2_"+cid);   //生成的FusionCharts图表本身的标识
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
//所有子节点
function getUserSubCode(){
	 var userCodeData="]";
	 var map=new Object(); 
	 map.userCode=$("userCode").value;
	 CustomerSatisfiedAction.getUserSubCode(map, {callback:function (data) {
         if(data != null){
    		 if(data&&data.userCodeList.length>0){	
    			 for(var i=0;i<data.userCodeList.length;i++){
    				 userCodeData+=data.userCodeList[i].ZONE_CODE+"]";
    			 }
    		 } 
	         }
         $("userCodeData").value= userCodeData;
	   }
	});
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
/** end **/

dhx.ready(indexInit);