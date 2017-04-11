/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();

//当前系统的主页Path
var base = getBasePath();
var chart = null;
var indId="";
var indName="";
var globZoneTree = null;
var globProdTypeTree = null;
var globCmplBusiTypeTree = null;
//查询条件参数
var dateTime = $("dateTime").value;
var zoneId =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var prodTypeId =   $('prodTypeCode').value==''?"-1":$('prodTypeCode').value;
var cmplBusiTypeId =   $('cmplBusiTypeCode').value==''?"1":$('cmplBusiTypeCode').value;

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
        	formData.field="COMPLAIN_NUM";
            formData.dateTime=$("dateTime").value;
            formData.zoneId=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
            formData.prodTypeId=$('prodTypeCode').value==''?"-1":$('prodTypeCode').value;
            formData.cmplBusiTypeId=$('cmplBusiTypeCode').value==''?"1":$('cmplBusiTypeCode').value;
            return formData;
        }
        }
    ]);
   
 
dwrCaller.addAutoAction("getCmplReport_Mon","ReportsMonAction.getCmplReport_Mon",loadParam);

var userInit=function(){
     //加载地域树
	var queryform =$('queryform'); 
    loadZoneTreeChkBox(zoneId,queryform);
    //加载业务类型树
    loadProdTypeTree(prodTypeId, queryform);
    //加载投诉现象树
    loadCmplBusiTypeTree(cmplBusiTypeId ,queryform);
    //执行查询数据
    excuteInitData();
    indexExp();
}


var excuteInitData=function(){
	dwrCaller.executeAction("getCmplReport_Mon",function(data){
	    $('chartdiv1').innerHTML='';
	    $('chartdiv2').innerHTML='';
		$('chartTable').innerHTML='';
		var tableStr="<table width='100%' id='mytbl' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
			  +"<tr>"
			    +"<td height='26' bgcolor='#cde5fd'><span class='title'>大区</span></td>"
			    +"<td bgcolor='#cde5fd'><span class='title'>分公司</span></td>"
			    +"<td bgcolor='#cde5fd'><span class='title'>本地全业务抱怨量</span></td>"
			    +"<td bgcolor='#cde5fd'><span class='title'>计费用户数（上月）</span></td>"
			    +"<td bgcolor='#cde5fd'><span class='title'>本地全业务抱怨率</span></td>"
			    +"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			    +"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
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
             +"<td>"
		     +data.list[i].PAR_REGION_NAME+"</td>";
        if(data.list[i].REGION_ID!=$('zoneCode').value){
        	tableStr=tableStr +"<td nowrap onclick=\"drillArea('"+data.list[i].REGION_NAME+"','"+data.list[i].REGION_ID+"')\" class='unl'>"+data.list[i].REGION_NAME+"</td>";
        }else{
        	tableStr=tableStr+"<td>"+data.list[i].REGION_NAME+"</td>";
        }
        tableStr=tableStr +"<td onClick=\"drillList('"+data.list[i].REGION_ID+"','"+data.list[i].REGION_NAME+"')\"  class='unl'>"+data.list[i].COMPLAIN_NUM+"</td>"
		     +"<td>"+data.list[i].NUM1+"</td>"
		     +"<td>"+data.list[i].CMPL+'‰'+"</td>"
		     +"<td>"+data.list[i].CMPL_HB+'%'+"</td>"
		     +"<td>"+data.list[i].CMPL_TB+'%'+"</td>"
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

//业务类型树
dwrCaller.addAutoAction("loadProdTypeTree","ProdTypeAction.queryProdTypeByPath");
var prodTypeConverter=dhx.extend({idColumn:"cmplProdTypeCode",pidColumn:"cmplProdTypeParCode",
    textColumn:"cmplProdTypeName"
},treeConverter,false);
dwrCaller.addDataConverter("loadProdTypeTree",prodTypeConverter);
//树动态加载Action
dwrCaller.addAction("querySubProdType",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},prodTypeConverter,false);
    ProdTypeAction.querySubProdType(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});

//投诉现象树
dwrCaller.addAutoAction("loadCmplBusiTypeTree","CmplBusiTypeAction.queryCmplBusiTypeByPath");
var cmplBusiTypeConverter=dhx.extend({idColumn:"cmplBusiTypeCode",pidColumn:"cmplBusiTypeParCode",
    textColumn:"cmplBusiTypeName"
},treeConverter,false);
dwrCaller.addDataConverter("loadCmplBusiTypeTree",cmplBusiTypeConverter);
//树动态加载Action
dwrCaller.addAction("querySubCmplBusiType",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},cmplBusiTypeConverter,false);
    CmplBusiTypeAction.querySubCmplBusiType(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});

//查询
var queryData=function(){
	excuteInitData();
}
//四舍五入 
var toDecimal=function(x) {     
         var f = parseFloat(x);     
         if (isNaN(f)) {       
           return;      
        }            
        f = Math.round(x*100)/100;        
         return f;
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
	var dateTime=$("dateTime").value;
	var cmplBusiTypeCode=$("cmplBusiTypeCode").value;
	var prodTypeCode=$("prodTypeCode").value;
	var url="/reports/portal/cmpl_mon.jsp?zoneCode="+zoneCode+"&dateTime="+dateTime+"&cmplBusiTypeCode="+cmplBusiTypeCode+"&prodTypeCode="+prodTypeCode;
	return parent.openTreeTab("116086"+zoneCode, "本地全业务抱怨率报表"+"["+zoneName+"]", base+url, 'top');
}
/**
 * yanhd add start
 */
var drillList=function(zoneCode,zoneName){
	 var dateTime=$("dateTime").value;//201306
	 var year= dateTime.substring(0,4);
	 var month=dateTime.substring(4,6);
	 var startDate=year+"-"+month+"-01";
	 var endDate=year+"-"+month+"-"+getLastDay(year,month);
	var url="/reports/cmpl/cmpl_list_mon.jsp?startDate="+startDate+"&endDate="+endDate+"&zoneCode="
	+zoneCode+"&prodTypeCode="+$('prodTypeCode').value+"&cmplBusiTypeCode="+$('cmplBusiTypeCode').value+"&indId=3";
	return parent.openTreeTab("122013"+zoneCode+zoneName,"本地抱怨量清单",base+url,'top');
	
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
        map.prodTypeId=$('prodTypeCode').value;
        map.cmplBusiTypeId=$('cmplBusiTypeCode').value;
		  if(obj.value=='切换地市'){
			  obj.value='切换区域';
			  map.changeZone="0";
		  }else{
			  obj.value='切换地市';
			  map.changeZone="1";
		  }  
	   ReportsMonAction.loadSet21AreaChart(map, {callback:function (res) {
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
					  +"<td align=\"left\"><font style=\"font-weight:bold;\" >&nbsp;本地全业务抱怨量：</font>本地投诉工单量+需求工单量</td>"
				    +"</tr>"
				    +"<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
	                   +"<td align=\"left\"><font style=\"font-weight:bold;\" >&nbsp;计费用户数：</font>对于固话、宽带等产品是计费用户，对于移动等产品为出账用户</td>"
	                +"</tr>"
	                +"<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
		              +"<td align=\"left\"><font style=\"font-weight:bold;\" >&nbsp;本地全业务抱怨率：</font>（本地投诉工单量+需求工单量）/上月计费用户数</td>"
		             +"</tr>";
	tableStr +="</table>"; 
	$('index_exp').innerHTML=tableStr;
}
dhx.ready(userInit);