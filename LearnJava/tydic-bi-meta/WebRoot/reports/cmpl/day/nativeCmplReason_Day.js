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

//查询条件参数

var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var ind = $("ind").value;

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
	 /*map.reportId="10007";
	 map.parameters="REGION_ID$MONTH_ID$CMPL_REASON1_ID$CHANNEL_TYPE_ID"; 
	 var zoneId=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
     map.values= zoneId+"$"+$("dateTime").value.replaceAll("-", "")+"$"+$("reasonId").value+"$"+$("channelType").value;
     map.field=$("ind").value;
     map.rptIndex="nativeCmplReasonMon"; 
     map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
     map.dateTime= $("dateTime").value;
     map.reasonId= $("reasonId").value;
     map.channelType= $("channelType").value;*/
     
     map.startDate = $('startDate').value;
	 map.endDate = $('endDate').value; 
	 map.zoneCode = $('zoneCode').value==""?"0000":$('zoneCode').value;
	 map.reasonId= $("reasonId").value;
     map.channelType= $("channelType").value;
	 map.indexType = $('indexType').value==''?"10":$('indexType').value;
     
     dhx.showProgress("正在执行......");
     CmplIndexAction.getCmplReasonDayData(map, function (res) {
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
var  excelHeader="";
var  tableHead="<tr>"
	+"<td bgcolor='#cde5fd' style='width:540px' colspan='3'><span class='title'>本地投诉原因 </span></td>"
	+"<td bgcolor='#cde5fd' style='width:540px' colspan='2'><span class='title'>本地投诉量</span></td>"
	+"<td bgcolor='#cde5fd' style='width:540px' colspan='3'><span class='title'>本地投诉率</span></td>"
	+"</tr>"
	+"<tr>"
	+"<td bgcolor='#cde5fd'><span class='title'>第一级</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>第二级</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>第三级</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>本地投诉量</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>平均值</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>本地投诉率</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
	+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>";
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
		    		            	   if(tempColumn == '第一级'&&i!=0)
			    		        	 {
			    		        	   tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].第一级+"','"+dataColumn[i].第一级_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
			    		        	 }/*else if(tempColumn == '业务投诉量'){
			    		        		 if(dataColumn[i].第三级_ID=='-1001'){//合计
			    		        			 tableData +="<td onClick=\"drillList('','')\"  class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		        		 }else if(dataColumn[i].第三级_ID=='-1000'){//小计
			    		        			 tableData +="<td onClick=\"drillList('"+dataColumn[i].第一级+"','"+dataColumn[i].第一级_ID+"')\"  class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		        		 }else if(dataColumn[i].第三级_ID=='-999'){//空
			    		        			 if(dataColumn[i].第二级_ID=='-999'){
			    		        				 tableData +="<td onClick=\"drillList('"+dataColumn[i].第一级+"','"+dataColumn[i].第一级_ID+"')\"  class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		        			 }else{
			    		        				 tableData +="<td onClick=\"drillList('"+dataColumn[i].第二级+"','"+dataColumn[i].第二级_ID+"')\"  class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		        			 }
			    		        		 }else{//有数据
			    		        			 tableData +="<td onClick=\"drillList('"+dataColumn[i].第三级+"','"+dataColumn[i].第三级_ID+"')\"  class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		        		 }
			    		        	 }*/
		    		            	   else{
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
var drillArea=function(reason,reasonId)
{	
	var startDate = $('startDate').value;
	var endDate = $('endDate').value;
	var zoneCode=$("zoneCode").value;
	var channelType=$("channelType").value;
	var indexType = $('indexType').value;
	var url="/reports/cmpl/day/nativeCmplReason_Day.jsp?zoneCode="+zoneCode+"&startDate="+startDate+"&endDate="+endDate+"&indexId="+indexType+"&reasonId="+reasonId+"&channelType="+channelType+"&menuId="+menuId;
	return parent.openTreeTab("122034"+zoneCode, "投诉原因分析日报表"+"["+reason+"]", base+url, 'top');
}
var drillList=function(reasonName,reasonId){
	 var startDate = $('startDate').value;
	 var endDate = $('endDate').value;
	 var zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
	 var channelType= $('channelType').value==''?"-1":$('channelType').value;
  	 var url="/reports/cmpl/cmpl_list_mon.jsp?startDate="+startDate+"&endDate="+endDate+"&zoneCode="+zoneCode+"&channelType="+channelType+"&reasonId="+reasonId;
	 return parent.openTreeTab("122013"+reasonId+reasonName,"本地投诉清单",base+url,'top');	
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
	$("titleInfo1").innerHTML=$("ind").options[$("ind").selectedIndex].text+"->当月值";
	$("titleInfo2").innerHTML=$("ind").options[$("ind").selectedIndex].text+"->当月值";
	var map=new Object();
	map.field=obj.value;
    map.rptIndex="nativeCmplReasonMon"; 
    map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
    map.reasonId= $("reasonId").value;
    map.channelType= $("channelType").value;
    buildChart(map);
}
//加载配置图形
function buildChart(map){
	dhx.showProgress("正在执行......");
	CmplIndexAction.getCmplSkipChange(map, {callback:function (res) {
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
function exportExcel(){
		//var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//文本框
	    //var  zone=$("zone").value;//文本框
	    //var  reasonId=$("reasonId").options[$("reasonId").selectedIndex].text;//文本框
	    //var  channelType=$("channelType").options[$("channelType").selectedIndex].text;//文本框
	    //var queryCond="月份："+dateTime+"    区域："+zone+"    投诉原因:"+reasonId +"    投诉渠道:"+channelType;
	    
	    var startDate = $('startDate').value;
		var endDate = $('endDate').value;
	    var  zone=$("zone").value;//文本框
	    var  reasonId=$("reasonId").options[$("reasonId").selectedIndex].text;//文本框
	    var  channelType=$("channelType").options[$("channelType").selectedIndex].text;//文本框
	    var  indexType=$("indexType").options[$("indexType").selectedIndex].text;//文本框
	    var queryCond="日期从："+startDate+" 至："+endDate+" 区域："+zone+" 投诉原因："+reasonId+"	投诉渠道："+channelType+" 考核对象："+indexType ;  
	    
   	    $("excelCondition").value=queryCond;
   	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel.jsp";
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
/** end **/
dhx.ready(indexInit);