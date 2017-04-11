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
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;

var prodType =   $('prodTypeCode').value==''?"-1":$('prodTypeCode').value;
var ind = $("ind").value;

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
        	formData.field="CURRENT_VALUE";
        	formData.rptIndex="cmplIndexMon"; 
        	formData.dateTime=$("dateTime").value;
        	formData.prodType=$('prodTypeCode').value==''?"-1":$('prodTypeCode').value;
        	formData.ind=$("ind").value;
            formData.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
            return formData;
        }
        }
    ]);
    
dwrCaller.addAutoAction("getCmplIndex","CmplIndexAction.getCmplIndex",loadParam);
dwrCaller.addAutoAction("getCmplIndexExp","CmplIndexAction.getCmplIndexExp","cmplIndexMon");

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
var indexInit=function(){
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
   //加载业务类型树
     loadProdTypeTree("-1", queryform);
     //执行查询数据
     excuteInitData();
     indexExp();
}


var excuteInitData=function(){
	dwrCaller.executeAction("getCmplIndex",function(data){
		 $('chartdiv1').innerHTML='';
		 $('chartdiv2').innerHTML='';
		$('chartTable').innerHTML='';
		var tableStr="<table width='970' id='mytbl' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
			  +"<tr>"
			   +"<td bgcolor='#cde5fd' style='width:60px'><strong>大区</strong></td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'><strong>地市</strong></td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>指标项</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>当月值</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>上月值</td>"
			    +"<td bgcolor='#cde5fd' style='width:80px'>环比</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>同比</td>"
			    +"<td bgcolor='#cde5fd' style='width:60px'>本年平均</td>"
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
             +"<td>"+data.list[i].IND_NAME+"</td>"
             +"<td>"+data.list[i].CURRENT_VALUE+"</td>"
             +"<td>"+data.list[i].LASTMON_VALUE+"</td>"
             +"<td>"+data.list[i].HB+"</td>"
             +"<td>"+data.list[i].TB+"</td>"
             +"<td>"+data.list[i].AVG_VALUE+"</td>"
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
        map.dateTime=$("dateTime").value;
        map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
        map.prodType=$("prodTypeCode").value;
        map.ind=$("ind").value;
        map.field="CURRENT_VALUE";
        map.rptIndex="cmplIndexMon"; 
		  if(obj.value=='切换地市'){
			  obj.value='切换区域';
			  map.changeZone="1";
		  }else{
			  obj.value='切换地市';
			  map.changeZone="0";
		  }  
		  CmplIndexAction.loadSet21AreaChart(map, {callback:function (res) {
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
	 dwrCaller.executeAction("getCmplIndexExp",function(data){
		 var tableStr="<table width='970' border='0' cellpadding='0' cellspacing='0'>";
		 if(data&&data.expList.length>0){	
			 for(var i=0;i<data.expList.length;i++){
				 tableStr =tableStr+
		         "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
		           +"<td align=\"left\"><font style='font-weight:bold;' >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+data.expList[i].INDEX_NAME+"  :  "+"</font>"
		           +data.expList[i].INDEX_EXPLAIN+"</td>"   
		           +"</tr>";
			 }
			 tableStr +="</table>"; 
			 $('index_exp').innerHTML=tableStr;
		 } 
	 });
}
//通过列改变图形
function changeCol(obj){
	var map=new Object();
	map.dateTime=$("dateTime").value;
    map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
    map.prodType=$("prodTypeCode").value;
    map.ind=obj.value;
    map.field="CURRENT_VALUE";
    map.rptIndex="cmplIndexMon"; 
    buildChart(map);
}
//加载配置图形
function buildChart(map){
	CmplIndexAction.getCmplIndexChange(map, {callback:function (res) {
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
	   chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "500", "230", "0", "0");
	   chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+Math.random(), "500", "230", "0", "0");
	   chart1.setDataXML(map.barChartMap);
       chart2.setDataXML(map.lineChartMap);
       chart1.render("chartdiv1");
       chart2.render("chartdiv2"); 
   }
dhx.ready(indexInit);