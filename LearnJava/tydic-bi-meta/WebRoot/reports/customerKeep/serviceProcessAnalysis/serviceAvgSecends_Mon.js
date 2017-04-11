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
var titileInfo=$("selectCol").options[$("selectCol").selectedIndex].text+"->当月值";
var titileInfo1=$("selectCol").options[$("selectCol").selectedIndex].text+"->当月值";
$("titleInfo1").innerHTML=titileInfo;
$("titleInfo2").innerHTML=titileInfo1;

if(tZoneCode!=tDefaultZoneCode){//下钻
    $("changeCode").value='1';
}else{
	$("changeCode").value='2';
}

//查询条件参数
//查询系统参数
var indId="";
var indName="";

var dateTime = $("dateTime").value;
var zoneId =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;

var loadParam = new biDwrMethodParam();
    loadParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
        	var  formData=new Object();
        	formData.field=$("selectCol").value;
        	formData.dateTime=$("dateTime").value;
            formData.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
            formData.changeCode=$("changeCode").value;
            return formData;
        }
        }
    ]);
  
dwrCaller.addAutoAction("getServiceAvgSecends_Mon","ServiceReActiveAction.getServiceAvgSecends_Mon",loadParam);

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
     loadZoneTreeChkBox(zoneId,queryform);
     //执行查询数据
     excuteInitData();
}


var excuteInitData=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	dwrCaller.executeAction("getServiceAvgSecends_Mon",function(data){
		 $('chartdiv1').innerHTML='';
		 $('chartdiv2').innerHTML='';
		$('chartTable').innerHTML='';
		var  excelData="";
		var tableStr="<table width='100%' id='mytbl' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
			 +"<tr>"
			   +"<td bgcolor='#cde5fd' style='width:60px' rowspan='2'><span class='title'>大区</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:60px' rowspan='2'><span class='title'>分公司</span></td>"
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
			    +"<td bgcolor='#cde5fd' ><span class='title'>环节平均总历时(秒)</span></td>"
			 +"</tr>";
    	if(data&&data.list.length>0){	
    		chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
    		chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
    		chart1.setDataXML(data.barChartMap);
            chart2.setDataXML(data.lineChartMap);
            chart1.render("chartdiv1");
            chart2.render("chartdiv2"); 
       for(var i=0;i<data.list.length;i++){
        tableStr =tableStr+
           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
             +"<td>"+data.list[i].PAR_REGION_NAME+"</td>";
	        if(data.list[i].REGION_ID!=$('zoneCode').value){
	         	tableStr=tableStr +"<td nowrap onclick=\"drillArea('"+data.list[i].REGION_NAME+"','"+data.list[i].REGION_ID+"')\" class='unl'>"+data.list[i].REGION_NAME+"</td>";
	         }else{
	         	tableStr=tableStr+"<td>"+data.list[i].REGION_NAME+"</td>";
	         }
	        tableStr=tableStr+"<td>"+data.list[i].YD_CBS_COSTTIME+"</td>"
             +"<td>"+data.list[i].YD_CRM_COSTTIME+"</td>"
             +"<td>"+data.list[i].YD_SPS_COSTTIME+"</td>"
             +"<td>"+data.list[i].YD_TOTAL_AVG_COSTTIME+"</td>"
             +"<td>"+data.list[i].KD_CBS_COSTTIME+"</td>"
             +"<td>"+data.list[i].KD_CRM_COSTTIME+"</td>"
             +"<td>"+data.list[i].KD_SPS_COSTTIME+"</td>"
             +"<td>"+data.list[i].KD_TOTAL_AVG_COSTTIME+"</td>"
		    +"</tr>";
	        excelData += data.list[i].PAR_REGION_NAME+"}"
            +data.list[i].REGION_NAME+"}"
            +data.list[i].YD_CBS_COSTTIME+"}"
            +data.list[i].YD_CRM_COSTTIME+"}"
            +data.list[i].YD_SPS_COSTTIME+"}"
            +data.list[i].YD_TOTAL_AVG_COSTTIME+"}"
            +data.list[i].KD_CBS_COSTTIME+"}"
            +data.list[i].KD_CRM_COSTTIME+"}"
            +data.list[i].KD_SPS_COSTTIME+"}"
            +data.list[i].KD_TOTAL_AVG_COSTTIME+"}]";
	        }
		       tableStr +="</table>"; 
		       $('chartTable').innerHTML=tableStr;
		       $("excelData").value= excelData;//Excel数据
		       autoRowSpan(mytbl,0,1);
		       autoRowSpan(mytbl,0,0);  
	      }else{
	    	  $('chartTable').innerHTML='<br/>没找到任何数据';
	      }
    	//$("city").value='切换地市'
    				$("div_src").style.display = "none";
						$("div_src").style.zindex = "-1";
      });
	
}
//查询
var queryData=function(){
	var titileInfo=$("selectCol").options[$("selectCol").selectedIndex].text+"->当月值";
	var titileInfo1=$("selectCol").options[$("selectCol").selectedIndex].text+"->当月值";
	$("titleInfo1").innerHTML=titileInfo;
	$("titleInfo2").innerHTML=titileInfo1;
	changeCode=getChangeCode(); //by qx
	excuteInitData();
}
//钻取
var drillArea=function(zoneName,zoneCode)
{	var dateTime=$("dateTime").value;
	var year= dateTime.substring(0,4);
	var month=dateTime.substring(4,6);
    var time=year+"-"+month+"-"+getLastDay(year,month);
    var selectCol=$("selectCol").value;    
    var url="/reports/customerKeep/serviceProcessAnalysis/serviceReActive_dayPg.jsp?zoneCode="+zoneCode+"&dateTime="+time+"&selectCol="+selectCol+"&menuId="+menuId;
    return parent.openTreeTab("143001"+zoneCode, "停机复开各环节平均时间日报"+"["+zoneName+"]", base+url, 'top');
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
        map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
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
		  ServiceReActiveAction.loadSet21AreaChart_AvgSecendsMon(map, {callback:function (res) {
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
	 var titileInfo=$("selectCol").options[$("selectCol").selectedIndex].text+"->当月值";
	var titileInfo1=$("selectCol").options[$("selectCol").selectedIndex].text+"->当月值";
	$("titleInfo1").innerHTML=titileInfo;
	$("titleInfo2").innerHTML=titileInfo1;
	var map=new Object();
	 map.dateTime=$("dateTime").value;
     map.field=obj.value;
     map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;      
     map.changeCode=$("changeCode").value;
     buildChart(map);
}
//加载配置图形
function buildChart(map){
	dhx.showProgress("正在执行......");
	ServiceReActiveAction.getServiceAvgSecends_Mon(map, {callback:function (res) {
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
		var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
			    var  zone=$("zone").value;//区域
			    var queryCond="月份："+dateTime+"    区域："+zone;
		   	    $("excelCondition").value=queryCond;
		    	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcelMod_chart.jsp";
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
dhx.ready(indexInit);