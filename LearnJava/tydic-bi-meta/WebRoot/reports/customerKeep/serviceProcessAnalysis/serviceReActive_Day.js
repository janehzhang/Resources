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

//查询条件参数
//查询系统参数
var indId="";
var indName="";

var dateTime = $("dateTime").value;
var zoneId =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;

if(user['zoneId']!='1') { //不是广东省  （钻取权限控制）
	var array=document.getElementsByName('city');
	    for(var i=0;i<array.length;i++){ 
	      array[i].style.display = "none";
	}
}

var loadParam = new biDwrMethodParam();
    loadParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
        	var  formData=new Object();
        	formData.field="YD_SUM_NUM";
        	formData.dateTime=$("dateTime").value;
            formData.zoneId=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
            return formData;
        }
        }
    ]);
  
dwrCaller.addAutoAction("getServiceReActive_Day","ServiceReActiveAction.getServiceReActive_Day",loadParam);

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
     loadZoneTreeChkBox(zoneId,queryform);
     //执行查询数据
     excuteInitData();
}


var excuteInitData=function(){
	dwrCaller.executeAction("getServiceReActive_Day",function(data){
		 $('chartdiv1').innerHTML='';
		 $('chartdiv2').innerHTML='';
		$('chartTable').innerHTML='';
		var tableStr="<table width='970' id='mytbl' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
			  +"<tr>"
			   +"<td bgcolor='#cde5fd'><strong>大区</strong></td>"
			    +"<td bgcolor='#cde5fd'><strong>地市</strong></td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>移动复机工单总数</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>移动3分钟复通工单数</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>移动3分钟复通及时率</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>移动30分钟复通工单数</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>移动30分钟复通及时率</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>宽带复机工单总数</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>宽带10分钟复通工单数</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>宽带10分钟复通及时率</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>宽带30分钟复通工单数</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>宽带30分钟复通及时率</td>"
			 +"</tr>";
    	if(data&&data.list.length>0){	
    		chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "500", "230", "0", "0");
    		chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+Math.random(), "500", "230", "0", "0");
    		chart1.setDataXML(data.barChartMap);
            chart2.setDataXML(data.lineChartMap);
            chart1.render("chartdiv1");
            chart2.render("chartdiv2"); 
       for(var i=0;i<data.list.length;i++){
        tableStr =tableStr+
           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
             +"<td>"+data.list[i].PAR_REGION_NAME+"</td>"
             +"<td>"+data.list[i].REGION_NAME+"</td>"      
             +"<td>"+data.list[i].YD_SUM_NUM+"</td>"
             +"<td>"+data.list[i].YD_3MIN_NUM+"</td>"
             +"<td>"+data.list[i].YD_3MIN_NUM_LV+"</td>"
             +"<td>"+data.list[i].YD_30MIN_NUM+"</td>"
             +"<td>"+data.list[i].YD_30MIN_NUM_LV+"</td>"
             +"<td>"+data.list[i].KD_SUM_NUM+"</td>"
             +"<td>"+data.list[i].KD_10MIN_NUM+"</td>"
             +"<td>"+data.list[i].KD_10MIN_NUM_LV+"</td>"
             +"<td>"+data.list[i].KD_30MIN_NUM+"</td>"
             +"<td>"+data.list[i].KD_30MIN_NUM_LV+"</td>"
		    +"</tr>";
         }
		       tableStr +="</table>"; 
		       $('chartTable').innerHTML=tableStr;
		       autoRowSpan(mytbl,0,0);
	      }else{
	    	  $('chartTable').innerHTML='<br/>没找到任何数据';
	      }
    	$("city").value='切换地市'
      });
	
}

//参数初始化公共部门

var initParam=function(){
    var loadParam = new biDwrMethodParam();
    loadParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
        	var  formData=new Object();
        	formData.dateTime=$("dateTime").value;
            formData.zoneId=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
            formData.indId=indId;
            formData.indName=indName;
            return formData;
        }
        }
    ]);
    return loadParam;
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
var drillList=function(zoneCode,zoneName){
	var url="/reports/portal/cmpl_list_mon.jsp?dateTime="+$('dateTime').value+"&zoneCode="
	+zoneCode+"&prodTypeCode="+$('prodTypeCode').value+"&cmplBusiTypeCode="+$('cmplBusiTypeCode').value;
	return parent.openTabSub("本地全业务抱怨清单",base+url,'top');
	
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
        map.zoneId=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
		  if(obj.value=='切换地市'){
			  obj.value='切换区域';
			  map.changeZone="0";
		  }else{
			  obj.value='切换地市';
			  map.changeZone="1";
		  }  
		  ServiceReActiveAction.loadSet21AreaChart_ReActiveDay(map, {callback:function (res) {
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
	var tableStr="<table width='100%' border='0'  cellpadding='0' cellspacing='0'>";
	      tableStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
					  +"<td align=\"left\"><font style=\"font-weight:bold;\" >本地全业务抱怨量：</font>本地投诉工单量+需求工单量</td>"
				    +"</tr>"
				    +"<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
	                   +"<td align=\"left\"><font style=\"font-weight:bold;\" >计费用户数：</font>对于固话、宽带等产品是计费用户，对于移动等产品为出账用户</td>"
	                +"</tr>"
	                +"<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
		              +"<td align=\"left\"><font style=\"font-weight:bold;\" >本地全业务抱怨率：</font>（本地投诉工单量+需求工单量）/上月计费用户数</td>"
		             +"</tr>";
	tableStr +="</table>"; 
	$('index_exp').innerHTML=tableStr;
}
dhx.ready(indexInit);