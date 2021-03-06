/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();
var globZoneTree = null;

//当前系统的主页Path
var base = getBasePath();
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
/** Tree head **/

var userInit=function(){
	loadIndexCdWarn(menuName);
	//1.加载地域树 
    loadZoneTreeChkBox(zoneCode,queryform);
    //执行查询数据
    excuteInitData();
}

var excuteInitData=function(){
    var map=new Object();
    map.dateTime   =$("dateTime").value;
    map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
    map.dateType   ="2";
    map.actType   ="1";
    map.field=$("selectCol").value;
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getCustomerSatisfied_everyType(map, function (res) {
    	$('chartdiv1').innerHTML='';
    	$('chartdiv2').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				     chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "100%", "200", "0", "0");
			    	 chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+Math.random(), "100%", "200", "0", "0");
			         chart1.setDataXML(res.barChartMap);
			         chart2.setDataXML(res.lineChartMap);
			         chart1.render("chartdiv1");
			         chart2.render("chartdiv2"); 
					 buildTable(res);
					 $("city").value='切换地市'
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
	var  excelHeader="";
	var  tableHead="<tr>"
	+"<td nowrap bgcolor='#cde5fd' rowspan='2'><span class='title'>区域</span></td>"
	+"<td nowrap bgcolor='#cde5fd' rowspan='2'><span class='title'>分公司</span></td>"
	+"<td nowrap bgcolor='#cde5fd' rowspan='2'><span class='title'>回访量</span></td>"
	+"<td nowrap bgcolor='#cde5fd' rowspan='2'><span class='title'>回访成功量</span></td>"
	+"<td bgcolor='#cde5fd' style='width:540px' colspan='3'><span class='title'>回访成功率</span></td>"
	+"<td nowrap bgcolor='#cde5fd' rowspan='2'><span class='title'>满意量</span></td>"
	+"<td bgcolor='#cde5fd' style='width:540px' colspan='3'><span class='title'>满意率</span></td>"
	+"<td nowrap bgcolor='#cde5fd' rowspan='2'><span class='title'>不满意量</span></td>"
	+"<td bgcolor='#cde5fd' style='width:540px' colspan='3'><span class='title'>不满意率</span></td>"
	+"<td nowrap bgcolor='#cde5fd' rowspan='2'><span class='title'>区域排名</span></td>"
	+"<td nowrap bgcolor='#cde5fd' rowspan='2'><span class='title'>不满意原因</span></td>"
	+"</tr>"
	+"<tr>"
	+"<td bgcolor='#cde5fd'><span class='title'>本月</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>上月</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>本月</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>上月</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>本月</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>上月</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>";
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
		    		    	   if(tempColumn == '分公司'&&i!=0)
				    		      {
				    		        tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
				    		      }
			    		    	 else if(tempColumn == '回访量')
		    		          {
		    		        	 tableData +="<td nowrap align='center' onclick=\"openMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+tempColumn+"','0')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
		    		          }
		    		          else if(tempColumn == '回访成功量')
		    		          {
		    		        	 tableData +="<td nowrap align='center' onclick=\"openMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+tempColumn+"','1')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";   
		    		          }
		    		          else if(tempColumn == '满意量')
		    		          {
		    		        	 tableData +="<td nowrap align='center' onclick=\"openMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+tempColumn+"','2')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";   
		    		          }
		    		          else if(tempColumn == '不满意量')
		    		          {
		    		             tableData +="<td nowrap align='center'  onclick=\"openMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"','"+tempColumn+"','3')\"  class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
		    		          }
		    		          else
		    		          {
		    		             if(dataColumn[i][tempColumn]=='查看')
		    		             {
		    		             tableData +="<td nowrap align='center' onclick=\"lookMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_编码+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";   	
		    		             }
		    		            else
		    		             {
		    		              tableData +="<td nowrap align='center'>"+addWaringStyle(dataColumn[i][tempColumn], null ,tempColumn)+"</td>";  
		    		             }  
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
        map.dateTime=$("dateTime").value;
        map.field=$("selectCol").value;
        map.dateType   ="2";
        map.actType   ="1";
        map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
		  if(obj.value=='切换地市'){
			  obj.value='切换区域';
			  map.changeZone="0";
		  }else{
			  obj.value='切换地市';
			  map.changeZone="1";
		  }  
		  dhx.showProgress("正在执行......");
		  CustomerSatisfiedAction.loadSet21AreaChart(map, {callback:function (res) {
			  dhx.closeProgress();
 	         if(res != null){
                     build21Chart(res);
		         }
		   }
		});
}
function  build21Chart(map){
	   $("chartdiv1").innerHTML="";
	   chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId3_"+Math.random(), "100%", "200", "0", "0");
	   chart.setDataXML(map.XML);
	   chart.render("chartdiv1");
	}
/**
 *  打开链接菜单
 * 
 */
var  lookMenuHref=function(zoneName,zoneCode){
	   var dateTime=$("dateTime").value;
	   var url="/reports/customerSatisfied/broadbandRepair/noSatisfiedReason_Mon.jsp?zoneCode="+zoneCode+"&dateTime="+dateTime;
	   return parent.openTreeTab("122024"+zoneCode, "宽带修障不满意原因TOP月报"+"["+zoneName+"]", base+url, 'top');
}
/**
 *  打开链接菜单
 * 
 */
var openMenuHref=function(zoneName,zoneCode,indexName,indexId){
	   var dateTime=$("dateTime").value;//201306
	   var year= dateTime.substring(0,4);
	   var month=dateTime.substring(4,6);
	   var startTime=year+"-"+month+"-01";
	   var endTime=year+"-"+month+"-"+getLastDay(year,month);
	 var url="/portalCommon/module/procedure/visitDetail/update/ADSL_visitUpdateList.jsp?zoneCode="+zoneCode+
	   "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId;
	 return parent.openTreeTab("122008"+zoneCode+indexId, "宽带修障即时回访清单"+"["+zoneName+indexName+"]", base+url, 'top');
}
//钻取
var drillArea=function(zoneName,zoneCode)
{	
	var dateTime=$("dateTime").value;
	var selectCol=$("selectCol").value;
	var url="/reports/customerSatisfied/broadbandRepair/serviceSatisfied_Mon.jsp?zoneCode="+zoneCode+"&dateTime="+dateTime+"&selectCol="+selectCol+"&menuId="+menuId;
	return parent.openTreeTab("122022"+zoneCode, "宽带修障满意率月报"+"["+zoneName+"]", base+url, 'top');
}
//获得某月的最后一天
function getLastDay(year,month) {
	var new_year = year;
   //取当前的年份
	var new_month = month++;//取下一个月的第一天，方便计算（最后一天不固定）
  if(month>12) {
	new_month -=12; //月份减
	new_year++; //年份增
	}
	var new_date = new
	Date(new_year,new_month,1); //取当年当月中的第一天
    return (new Date(new_date.getTime()-1000*60*60*24)).getDate();//获取当月最后一天日期
} 
//通过列改变图形
function changeCol(obj){
	var map=new Object();
    map.dateTime   =$("dateTime").value;
    map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
    map.dateType   ="2";
    map.actType   ="1";
    map.field=obj.value;
    buildChart(map);
}
//加载配置图形
function buildChart(map){
	dhx.showProgress("正在执行......");
	CustomerSatisfiedAction.getCustomerSatisfied_everyType(map, {callback:function (res) {
		dhx.closeProgress();
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
	   chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "100%", "200", "0", "0");
	   chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+Math.random(), "100%", "200", "0", "0");
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
dhx.ready(userInit);