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
var globChannelTypeTree=null;

//查询条件参数
//查询系统参数
var indId="";
var indName="";

var startDate=$('startDate').value;
var endDate=$('endDate').value;
//var week = $("week").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var channelTypeCode =   $('channelTypeCode').value==""?'1':$("channelTypeCode").value;

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
        	formData.field="PAY_NUM";
        	formData.startDate=$('startDate').value;
        	formData.endDate=$('endDate').value;
        	//formData.dateTime=$("dateTime").value;
        	//formData.week=$("week").value;
        	formData.reportId='1';//1
            formData.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
            formData.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
            return formData;
        }
        }
    ]);
dwrCaller.addAutoAction("getChannelGlobal_Mon","AllBusinessAction.getChannelGlobal_Mon",loadParam);

var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** zone Tree head **/
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
/** channel Tree head **/
dwrCaller.addAutoAction("loadChannelTypeTree","ChannelTypeAction.queryChannelTypeByPath");
var channelTypeConverter=dhx.extend({idColumn:"channelTypeCode",pidColumn:"channelTypeParCode",
    textColumn:"channelTypeName"
},treeConverter,false);
dwrCaller.addDataConverter("loadChannelTypeTree",channelTypeConverter);
dwrCaller.addAction("querySubChannelType",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},channelTypeConverter,false);
    ChannelTypeAction.querySubChannelType(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});

var indexInit=function(){
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
   //1.加载渠道类型树
     loadChannelTypeTree(channelTypeCode,queryform);
     //执行查询数据
     excuteInitData();
}


var excuteInitData=function(){
	dwrCaller.executeAction("getChannelGlobal_Mon",function(data){
		 $('chartdiv1').innerHTML='';
		 $('chartdiv2').innerHTML='';
		$('chartTable').innerHTML='';  
		//大区,地市,渠道类型,服务总量,缴费笔数,查询量,咨询量,办理量,回馈量,反馈量,开通、变更、停用量,投诉量,障碍量,商旅服务量,
		//翼健康服务量,通信助理服务量,其它量,渠道服务量占比
		var tableStr="<table width='1350px' id='mytbl' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
			  +"<tr>"
			   +"<td bgcolor='#cde5fd' style='width:80px'><strong>大区</strong></td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'><strong>地市</strong></td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>渠道类型</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>缴费笔数</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>查询量</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>咨询量</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>办理量</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>回馈量</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>反馈量</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>开通、变更、停用量</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>投诉量</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>障碍量</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>其它量</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>服务总量</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>渠道服务量占比</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>渠道服务量占比(不含查询、咨询量)</td>"
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
             +"<td>"+data.list[i].CHANNEL_TYPE_NAME+"</td>"
             +"<td>"+data.list[i].PAY_NUM+"</td>"
             +"<td>"+data.list[i].QUERY_NUM+"</td>"
             +"<td>"+data.list[i].CONSULT_NUM+"</td>"
             +"<td>"+data.list[i].DEAL_NUM+"</td>"
             +"<td>"+data.list[i].FEEDBACK_NUM+"</td>"
             +"<td>"+data.list[i].FEEDBACK2_NUM+"</td>"
             +"<td>"+data.list[i].CHANGE_NUM+"</td>"
             +"<td>"+data.list[i].COMPLAIN_NUM+"</td>"
             +"<td>"+data.list[i].FAULT_NUM+"</td>"
             +"<td>"+data.list[i].OTHER_NUM+"</td>"
             +"<td>"+data.list[i].SERVICE_NUM+"</td>"
             +"<td>"+data.list[i].PERC_NUM+"%"+"</td>"
             +"<td>"+data.list[i].PERC_NUM2+"%"+"</td>"
		    +"</tr>";
         }
		       tableStr +="</table>"; 
		       $('chartTable').innerHTML=tableStr;
		       autoRowSpan(mytbl,0,1);
		       autoRowSpan(mytbl,0,0);
	      }else{
	    	  $('chartTable').innerHTML='<br/>没找到任何数据';
	      }
    	$("city").value='切换地市'
      });
	
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
        //map.dateTime=$("dateTime").value;
        //map.week=$("week").value;
        map.startDate=$('startDate').value;
        map.endDate=$('endDate').value;
        map.field="PAY_NUM";
        map.reportId='1';//报表id为1
        map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
        map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
		  if(obj.value=='切换地市'){
			  obj.value='切换区域';
			  map.changeZone="0";
		  }else{
			  obj.value='切换地市';
			  map.changeZone="1";
		  }  
		  AllBusinessAction.loadSet21AreaChart(map, {callback:function (res) {
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
dhx.ready(indexInit);