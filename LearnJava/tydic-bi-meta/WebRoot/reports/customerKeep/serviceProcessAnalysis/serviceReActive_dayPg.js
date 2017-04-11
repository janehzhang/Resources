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
//by qx
tZoneCode=$("zoneCode").value;
tDefaultZoneCode=$("defaultZoneCode").value;
var titileInfo=$("selectCol").options[$("selectCol").selectedIndex].text+"->当日值";
var titileInfo1=$("selectCol").options[$("selectCol").selectedIndex].text+"->当日值";
$("titleInfo1").innerHTML=titileInfo;
$("titleInfo2").innerHTML=titileInfo1;
if(tZoneCode!=tDefaultZoneCode){//下钻
    $("changeCode").value='1';
}else{
	$("changeCode").value='2';
}
var zoneCode=$("zoneCode").value==""?"0000":$("zoneCode").value;
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
	var map=new Object();
    map.zoneCode    =$("zoneCode").value==""?"0000":$("zoneCode").value;
    map.dateTime     =$("dateTime").value;
   // map.prodId     =$("prodId").value;
    map.dateType     =$("dateType").value;
    map.rptIndex="serviceReActiveDayP";
    map.field=$("selectCol").value;
    map.changeCode= $("changeCode").value;
    dhx.showProgress("正在执行......");
    ServiceReActiveAction.getServiceReActive_Pg(map, function (res) {
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
	       excelHeader += headColumn[i]+",";
        }
      }
      tableHead +="<td bgcolor='#cde5fd' style='width:60px' rowspan='2'><span class='title'>分公司</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:60px' rowspan='2'><span class='title'>日期</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:320px' colspan='4'><span class='title'>移动</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:320px' colspan='4'><span class='title'>宽带</span></td>"
		    +"</tr>"
		    +"<tr>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>CBS环节平均历时(秒)</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>CRM环节平均历时(秒)</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>SPS环节平均历时(秒)</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>环节平均总历时(秒)</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>CBS环节平均历时(秒)</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>CRM环节平均历时(秒)</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>SPS环节平均历时(秒)</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>环节平均总历时(秒)</span></td>";  
    tableHead+="</tr>";
    $("excelHeader").value=excelHeader;//Excel表头
return tableHead;
}
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
						    		         tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
				    		        	 } else if(tempColumn == '总工单数' )
				    		        	 {
						    		         tableData +="<td nowrap onclick=\"openMenuHref('"+dataColumn[i].分公司_ID+"','"+dataColumn[i].分公司+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
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
	var titileInfo=$("selectCol").options[$("selectCol").selectedIndex].text+"->当日值";
	var titileInfo1=$("selectCol").options[$("selectCol").selectedIndex].text+"->当日值";
	$("titleInfo1").innerHTML=titileInfo;
	$("titleInfo2").innerHTML=titileInfo1;
	changeCode=getChangeCode(); //by qx
	excuteInitData();
}
//钻取
var drillArea=function(zoneName,zoneCode)
{	
	var dateTime=$("dateTime").value;
	//var prodId=$("prodId").value;
	var selectCol=$("selectCol").value;
	var url="/reports/customerKeep/serviceProcessAnalysis/serviceReActive_dayPg.jsp?zoneCode="+zoneCode+"&dateTime="+dateTime+"&selectCol="+selectCol+"&menuId="+menuId;
	return parent.openTreeTab("143001"+zoneCode, "停机复开及时率日报"+"["+zoneName+"]", base+url, 'top');
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
        map.field=$("selectCol").value;
        map.rptIndex="serviceReActiveDayP";
        map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value; 
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
		  ServiceReActiveAction.getCompaChart(map, {callback:function (res) {
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
//通过列改变图形
function changeCol(obj){  
     var titileInfo=$("selectCol").options[$("selectCol").selectedIndex].text+"->当日值";
	 var titileInfo1=$("selectCol").options[$("selectCol").selectedIndex].text+"->当日值";
	 $("titleInfo1").innerHTML=titileInfo;
	 $("titleInfo2").innerHTML=titileInfo1;
	 var map=new Object();
	 map.dateTime=$("dateTime").value;	 
     map.field=obj.value;
     map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value; 
     map.rptIndex="serviceReActiveDayP";
     map.changeCode=$("changeCode").value;
     buildChart(map);
}
//加载配置图形
function buildChart(map){
	dhx.showProgress("正在执行......");
	ServiceReActiveAction.getServiceReActive_Diagram(map, {callback:function (res) {
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
function exportExcel(){
		    var  dateTime=$("dateTime").value
		    var  zone=$("zone").value;//区域
		    var queryCond="日期："+dateTime+"    区域："+zone;
	   	    $("excelCondition").value=queryCond;
	    	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_twoChartHeader.jsp";
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
}
function exportImage(){
			var cid=$("cid").value;
			var cid1=$("cid1").value;
   	        var charts1 = getChartFromId("ChartId1_"+cid1);   //生成的FusionCharts图表本身的标识
   	         var charts2 = getChartFromId("ChartId2_"+cid);   //生成的FusionCharts图表本身的标识
   	        charts1.exportChart(); 
   	        charts2.exportChart(); 
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
/**
 *  当日值
 * 
 */
var openMenuHref=function(zoneCode,regionName){
	var dateTime=$("dateTime").value;
	//var prodId=$("prodId").value;
	var url="/reports/customerKeep/serviceProcessAnalysis/serviceReActiveDetail.jsp?zoneCode="+zoneCode+"&startDate="+dateTime+"&endDate="+dateTime;
	return parent.openTreeTab("143003"+zoneCode,"停复机清单["+regionName+"]",base+url,'top');
}
dhx.ready(indexInit);