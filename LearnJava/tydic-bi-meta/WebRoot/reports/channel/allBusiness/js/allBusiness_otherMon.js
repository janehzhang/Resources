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
var flag='false';		//是否显示渠道小类
//by qx
/*tZoneCode=$("zoneCode").value;
tDefaultZoneCode=$("defaultZoneCode").value;*/
var titileInfo=$("typeList").options[$("typeList").selectedIndex].text+"->"+$("selectCol").options[$("selectCol").selectedIndex].text;
//var titileInfo1=$("selectCol").options[$("selectCol").selectedIndex].text+"->合计";
$("titleInfo1").innerHTML=titileInfo;
//$("titleInfo2").innerHTML=titileInfo1;

/*if(tZoneCode!=tDefaultZoneCode){//下钻
    $("changeCode").value='1';
}else{
	$("changeCode").value='2';
}*/
//查询系统参数
var indId="";
var indName="";

var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var channelTypeCode =   $('channelTypeCode').value==""?'1':$("channelTypeCode").value;
//var selectCol=$("selectCol").value;

var loadParam = new biDwrMethodParam();
    loadParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
        	var  formData=new Object();
        	formData.field=$("selectCol").value;
        	formData.dateTime=$("dateTime").value;
        	formData.typeList=$("typeList").value;
        	formData.reportId='2';
        	formData.changType='2';
        	formData.showAllChannel=flag;
            formData.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
            formData.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
            formData.changeCode=$("changeCode").value;
            
            if($('channelTypeCode').value!='1')
            	{
            		$("channel").value='返回';
            	}
            else if($("channel").value!='渠道大类'){
            	{
            		$("channel").value='渠道小类';
            	}
            }
            return formData;
        }
        }
    ]);
dwrCaller.addAutoAction("getChannelGlobal_Mon","AllBusinessAction_New.getChannelGlobal_Mon",loadParam);



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
dwrCaller.addAutoAction("loadChannelTypeTreePart","ChannelTypeAction.queryChannelTypeByPath");
var channelTypeConverter=dhx.extend({idColumn:"channelTypeCode",pidColumn:"channelTypeParCode",
    textColumn:"channelTypeName"
},treeConverter,false);
dwrCaller.addDataConverter("loadChannelTypeTreePart",channelTypeConverter);
dwrCaller.addAction("querySubChannelTypePart",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},channelTypeConverter,false);
    ChannelTypeAction.querySubChannelTypePartOther_new(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});

var indexInit=function(){
	 //1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);  
    //1.加载渠道类型树
     loadChannelTypeTreePart(channelTypeCode,queryform);
     //执行查询数据
     excuteInitData();
}

var excuteInitData=function(){
	dwrCaller.executeAction("getChannelGlobal_Mon",function(data){
		$("cid").value=Math.random();
		var cid=$("cid").value;
		$("cid1").value=Math.random();
		var cid1=$("cid1").value;
		 $('chartdiv1').innerHTML='';
		 $('chartdiv2').innerHTML='';
		 $('chartTable').innerHTML=''; 
		$('chartdiv1').innerHTML='';
		$('chartdiv2').innerHTML='';
		 var  excelData="";
		//大区,地市,渠道类型,服务总量,缴费笔数,查询量,咨询量,办理量,回馈量,反馈量,开通、变更、停用量,投诉量,障碍量,商旅服务量,
		//翼健康服务量,通信助理服务量,其它量,渠道服务量占比
		var tableStr="<table width='100%' id='mytbl' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
			+"<tr>"
		    +"<td bgcolor='#cde5fd' style='width:80px' rowspan='2'><span class='title'>大区</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:7%' rowspan='2'><span class='title'>渠道大类</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:7%' rowspan='2'><span class='title'>渠道小类</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:15%' colspan='5' ><span class='title' align='center' >查询</span></td>" 
		    +"<td bgcolor='#cde5fd' style='width:15%' colspan='4'><span class='title' align='center'>办理</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:80px' rowspan='2'><span class='title'>充缴</span></td>" 
		    +"<td bgcolor='#cde5fd' style='width:80px' rowspan='2'><span class='title'>咨询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:80px' rowspan='2'><span class='title'>投诉</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:80px' rowspan='2'><span class='title'>障碍</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:80px' rowspan='2'><span class='title'>其它</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:80px' rowspan='2'><span class='title'>总服务量</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:80px' rowspan='2'><span class='title'>占比</span></td>"
		    //+"<td bgcolor='#cde5fd' style='width:80px' rowspan='2'><span class='title'>剔除查/咨询后占比</span></td>"
		    //+"<td bgcolor='#cde5fd' style='width:80px' rowspan='2'><span class='title'>剔除查/咨询后总服务量</span></td>"
		 +"</tr>"
		 +"<tr>"
		    +"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>费用查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>进度查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>积分查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>信息查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>常用查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>增值业务</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>套餐销售</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>套餐变更</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:60px'><span class='title'>基础服务</span></td>"
		  /*  +"<td bgcolor='#cde5fd' style='width:60px'>回馈量</td>"
		    +"<td bgcolor='#cde5fd' style='width:80px'>反馈量</td>"
		    +"<td bgcolor='#cde5fd' style='width:80px'>开通、变更、停用量</td>"*/
			 +"</tr>";
    	if(data&&data.list.length>0){	
    		/*chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
    		chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
    		chart1.setDataXML(data.barChartMap);
            chart2.setDataXML(data.lineChartMap);
            chart1.render("chartdiv1");
            chart2.render("chartdiv2");*/ 
    		chart1=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
    		chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
    		chart1.setDataXML(data.pieChartMap);
    		chart2.setDataXML(data.lineChartMap);
    		chart1.render("chartdiv1");
    		chart2.render("chartdiv2");
       for(var i=0;i<data.list.length;i++){
        tableStr =tableStr+
           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
             +"<td>"+data.list[i].REGION_NAME+"</td>";
	    
	       /* if(data.list[i].REGION_ID!=$('zoneCode').value){
             	tableStr=tableStr +"<td nowrap onclick=\"drillArea('"+data.list[i].REGION_NAME+"','"+data.list[i].REGION_ID+"')\" class='unl'>"+data.list[i].REGION_NAME+"</td>";
             }
	        else{
	         	tableStr=tableStr+"<td>"+data.list[i].REGION_NAME+"</td>";
	         }*/
	        tableStr=tableStr+"<td>"+data.list[i].CHANNEL_TYPE_PAR_NAME+"</td>"
	        if(data.list[i].CHANNEL_TYPE_NAME=="小计"||data.list[i].CHANNEL_TYPE_NAME=="合计"){
            	 tableStr=tableStr+"<td>"+data.list[i].CHANNEL_TYPE_NAME+"</td>";
             }else{
            	 tableStr=tableStr+"<td nowrap onclick=\"drillArea('"+data.list[i].REGION_NAME+"','"+data.list[i].REGION_ID+"','"+data.list[i].CHANNEL_TYPE_ID+"')\" class='unl'>"+data.list[i].CHANNEL_TYPE_NAME+"</td>";
             }
             tableStr=tableStr
	        +"<td>"+data.list[i].QUERY_NUM1+"</td>"
            +"<td>"+data.list[i].QUERY_NUM2+"</td>"
            +"<td>"+data.list[i].QUERY_NUM3+"</td>"
            +"<td>"+data.list[i].QUERY_NUM4+"</td>"
            +"<td>"+data.list[i].QUERY_NUM5+"</td>"
            +"<td>"+data.list[i].DEAL_NUM1+"</td>"
            +"<td>"+data.list[i].DEAL_NUM2+"</td>"
            +"<td>"+data.list[i].DEAL_NUM3+"</td>"
            +"<td>"+data.list[i].DEAL_NUM4+"</td>"
            +"<td>"+data.list[i].PAY_NUM+"</td>"
            +"<td>"+data.list[i].CONSULT_NUM+"</td>"
           /* +"<td>"+data.list[i].FEEDBACK_NUM+"</td>"
            +"<td>"+data.list[i].FEEDBACK2_NUM+"</td>"
            +"<td>"+data.list[i].CHANGE_NUM+"</td>"*/
            +"<td>"+data.list[i].COMPLAIN_NUM+"</td>"
            +"<td>"+data.list[i].FAULT_NUM+"</td>"
            +"<td>"+data.list[i].OTHER_NUM+"</td>"
            +"<td>"+data.list[i].SERVICE_NUM+"</td>"
            +"<td>"+data.list[i].PERC_NUM+"%"+"</td>"
            //+"<td>"+data.list[i].PERC_NUM2+"%"+"</td>"
            //+"<td>"+data.list[i].SERVICE_NUM2+"</td>"
		    +"</tr>";
	        excelData += data.list[i].REGION_NAME+"}"
            +data.list[i].CHANNEL_TYPE_PAR_NAME+"}"
            +data.list[i].CHANNEL_TYPE_NAME+"}"
            +data.list[i].QUERY_NUM1+"}"
            +data.list[i].QUERY_NUM2+"}"
            +data.list[i].QUERY_NUM3+"}"
            +data.list[i].QUERY_NUM4+"}"
            +data.list[i].QUERY_NUM5+"}"
            +data.list[i].DEAL_NUM1+"}"
            +data.list[i].DEAL_NUM2+"}"
            +data.list[i].DEAL_NUM3+"}"
            +data.list[i].DEAL_NUM4+"}"
            +data.list[i].PAY_NUM+"}"
            +data.list[i].CONSULT_NUM+"}"
            +data.list[i].COMPLAIN_NUM+"}"
            +data.list[i].FAULT_NUM+"}"
            +data.list[i].OTHER_NUM+"}"
            +data.list[i].SERVICE_NUM+"}"
            +data.list[i].PERC_NUM+"%}]"
            //+data.list[i].PERC_NUM2+"}"
            //+data.list[i].SERVICE_NUM2+"}]";
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
	//changeCode=getChangeCode(); //by qx
	excuteInitData();
}
//钻取
var drillArea=function(zoneName,zoneCode,channelTypeCode)
{	
	var dateTime=$("dateTime").value;
	//var selectCol=$("selectCol").value;
	//var channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
	var url="/reports/channel/allBusinessAllZone/allBusiness_Mon_New.jsp?zoneCode="+zoneCode+"&dateTime="+dateTime+"&channelTypeCode="+channelTypeCode+"&menuId="+menuId;
	return parent.openTreeTab("119004"+zoneCode, "全渠道服务量分析月报"+"["+zoneName+"]", base+url, 'top');
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
	} 
/** end **/

var showAllChannel = function (obj){
	AllBusinessAction_New.getParChannel($('channelTypeCode').value, function (res) {
		if(res=='1'){
			flag='true';
		}
		if(res!='0'){
			$('channelTypeCode').value=res;
		}
		if(flag=='true'){
		flag='false';
		 $("channel").value='渠道小类'
	}
	else{
		flag='true';
		$("channel").value='渠道大类'
	}
	excuteInitData();
	loadChannelTypeTreePart_all($('channelTypeCode').value,queryform);
	
	});
	
}
var lookCity = function (obj){
    var map=new Object();
        map.dateTime=$("dateTime").value;
        map.reportId='2';
        map.field=$("selectCol").value;
        map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
        map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
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
		  AllBusinessAction_New.loadSet21AreaChart(map, {callback:function (res) {
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
	var titileInfo=$("typeList").options[$("typeList").selectedIndex].text+"->"+$("selectCol").options[$("selectCol").selectedIndex].text;
	//var titileInfo1=$("selectCol").options[$("selectCol").selectedIndex].text+"->合计";
	$("titleInfo1").innerHTML=titileInfo;
	//$("titleInfo2").innerHTML=titileInfo1;
	var map=new Object();
	 map.dateTime=$("dateTime").value;
	 map.typeList=$("typeList").value;
	 map.field=$("selectCol").value;
     map.reportId='2';
     map.changType='2';
     map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
     map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
     map.changeCode=$("changeCode").value;
     flag='false';
     if($("channel").value!='返回'){
     if(flag=='true'){
		 $("channel").value='渠道大类'
	 }
	 else{
		$("channel").value='渠道小类'
	 }
     }
     map.showAllChannel=flag;
     buildChart(map);
}
//加载配置图形
function buildChart(map){
	dhx.showProgress("正在执行......");
	AllBusinessAction_New.getChannelGlobal_Mon(map, {callback:function (res) {
		dhx.closeProgress();
    	         if(res != null){
                        buildBLChart(res);
		         }
	}
		});
   }
function  buildBLChart(map){
		$("cid").value=Math.random();
		var cid=$("cid").value;
		$("cid1").value=Math.random();
		var cid1=$("cid1").value;
	   $("chartdiv1").innerHTML="";
	   $("chartdiv2").innerHTML="";
	   //$("city").value="切换地市";
	   chart1=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
	   chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
	   chart1.setDataXML(map.pieChartMap);
       chart2.setDataXML(map.lineChartMap);
       chart1.render("chartdiv1");
       chart2.render("chartdiv2"); 
   }
function exportExcel(){
		var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//文本框
		    var  zone=$("zone").value;//文本框
		    var  channelType=$("channelType").value;//文本框
		    var queryCond="月份："+dateTime+"    区域："+zone+"    渠道类型: "+channelType;  
	   	    $("excelCondition").value=queryCond; 
	   	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcelMod_chart2.jsp";
	   	    //var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel_secondLevel.jsp"; 
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
function myJs(temp){
			$('channelTypeCode').value=temp;
			if(temp=='1'){
				flag='false';
				$("channel").value!='渠道小类';
			}
			loadChannelTypeTreePart_all(temp,queryform);
			excuteInitData();
			
}
dhx.ready(indexInit);
