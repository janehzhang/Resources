/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
var globZoneTree = null;
var globProdTypeTree = null;
var zoneCode=$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
//by qx
tZoneCode=$("zoneCode").value;
tDefaultZoneCode=$("defaultZoneCode").value;
if(tZoneCode!=tDefaultZoneCode){//下钻
    $("changeCode").value='1';
}else{
	$("changeCode").value='2';
}
var changeCode = $("changeCode").value;

/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCode");
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
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
     loadProdTypeTree(-1, queryform); //根结点 
     //3.控制地市按钮权限
    // controlCityButton();
     //执行查询数据
     excuteInitData();
}
var excuteInitData=function(){
     var map=new Object();
     var zoneCode      =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
     var dayTime       =$("dayTime").value;
     var prodType      =$("prodTypeCode").value=="-1"?"":$("prodTypeCode").value;
 
     map.reportId=reportId;
     map.parameters="REGION_ID$DAY_ID$PROD_TYPE_ID";
     map.values     =zoneCode+ "$" + dayTime.replaceAll("-", "")+"$"+prodType;
     
     map.zoneCode=zoneCode;
     map.dayTime=dayTime.replaceAll("-", "");
     map.prodType=prodType; 	 
     map.changeCode=getChangeCode();
     //查询图形
     selectChart(map)
     //查询数据表格
     selectTable(map);
}

/**
 * 查询数据表格
 * @param {Object} map
 * 
 */
var selectTable=function(map)
{      

     dhx.showProgress("正在执行......");
     ServWarnDayAction.getTableData(map, function (res) {
    	              dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("查询失败,请稍后重试!");
				            return;
				        }
					    buildTable(res);
    });
}
/**
 *  构建表格
 * @param {Object} data
 */
function buildTable(data){
	     tableStr ="<table id='mytbl_1' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
           tableStr+=tableHeader(data.headColumn);
		   tableStr+=tableData(data.dataColumn,data.headColumn);
         tableStr +="</table>"; 
           $('dataTable').innerHTML=tableStr;
}
/**
 *  构造表头
 * @param {Object} data
 */
function tableHeader(headColumn){
	var  excelHeader="";

	       for(var i=0;i<headColumn.length;i++)
	       {
	    	  if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	    	  {
	    		 excelHeader += headColumn[i]+",";
	    	  } 
	         
	       }
	       
	 var  tableHead="<tr>";  
	           tableHead +="<td nowrap bgcolor='#cde5fd' rowSpan='2'><span class='title'>TOP10排名</span></td>";
	           tableHead +="<td nowrap bgcolor='#cde5fd' colspan='3'><span class='title'>本地投诉现象</span></td>";
	           tableHead +="<td nowrap bgcolor='#cde5fd' rowSpan='2'><span class='title'>当日投诉量</span></td>";
	           tableHead +="<td nowrap bgcolor='#cde5fd' rowSpan='2'><span class='title'>昨日投诉量</span></td>";
	           tableHead +="<td nowrap bgcolor='#cde5fd' rowSpan='2'><span class='title'>环比</span></td>";
	      tableHead +="</tr>";
	      
	      tableHead +="<tr>";  
	           tableHead +="<td nowrap bgcolor='#cde5fd'><span class='title'>第一级</span></td>";
	           tableHead +="<td nowrap bgcolor='#cde5fd'><span class='title'>第二级</span></td>";
	           tableHead +="<td nowrap bgcolor='#cde5fd'><span class='title'>第三级</span></td>";
	      tableHead +="</tr>";
	      
	$("excelHeader").value=excelHeader;//Excel表头
	return tableHead;
}
/**
 *  构造表格数据
 * @param {Object} data
 */
function tableData(dataColumn,headColumn){
	  var  excelData="";
	   var tableData="";
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
				    		        
				    		        	if(tempColumn == 'TOP10排名'||tempColumn == '当日投诉量'||tempColumn == '昨日投诉量'||tempColumn == '环比')
				    		        	 {
				    		        	    tableData +="<td nowrap align='center'>"+dataColumn[i][tempColumn]+"</td>";   
				    		        	 }
				    		        	 else
				    		        	 {
				    		                tableData +="<td nowrap align='left'>"+dataColumn[i][tempColumn]+"</td>";  
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
var selectChart=function(map)
{

    //构建曲线图
	bulidLineChart(map);
	//构建柱状图
	bulidBarChart(map);
}
/**
 * 构建曲线图
 * @param {Object} map
 */
var bulidLineChart=function(map)
{
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("chartdiv_line").innerHTML="<center>正在加载.......</center>";
	dhx.showProgress("正在执行......");
    ServWarnDayAction.bulidLineChart(map, {callback:function (res) {
    	        dhx.closeProgress();
    	        if(res != null && res.XML != null){
	          		   var chart = new FusionCharts(base+"/js/Charts/MSLine.swf", "chartdiv_1"+cid, "100%", "200","0", "0");
					   chart.setDataXML(res.XML);
			           chart.render("chartdiv_line");
		         }else{
		        	  $('chartdiv_line').innerHTML="<center>没有图形显示</center>";
		         }
		   }
		});
	
}
/**
 * 构建柱状图
 * @param {Object} map
 */
var bulidBarChart=function(map)
{   
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	dhx.showProgress("正在执行......");
    ServWarnDayAction.bulidBarChart(map, {callback:function (res) {
    	dhx.closeProgress();
    	         if(res != null && res.XML != null){
	          		   var chart = new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "chartdiv_2"+cid1, "100%", "200","0", "0");
					   chart.setDataXML(res.XML);
			           chart.render("chartdiv_bar");
		         }else{
		        	   $('chartdiv_bar').innerHTML="<center>没有图形显示</center>";
		         }
   	 $("div_src").style.display = "none";
	 $("div_src").style.zindex = "-1";
	 
    	         
		   }
    
		});
}
//控制地市按钮权限
var controlCityButton=function()
{
   if(userInfo['localCode']!='0000') { //不是广东省  （钻取权限控制）
      var array=document.getElementsByName('city');
       for(var i=0;i<array.length;i++){ 
               array[i].style.display = "none";
         }
    }
}

var lookCity = function (){
	 var obj = event.srcElement;
	 var map=new Object();
	 map.zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
     map.dayTime     =($("dayTime").value).replaceAll("-", "");
     map.prodType    =$("prodTypeCode").value=="-1"?"":$("prodTypeCode").value;

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
               bulidBarChart(map);
}
//查询
var queryData=function(){
    excuteInitData();
}
function exportExcel(){
		var  dayTime=$("dayTime").value;//文本框
	    var  zone=$("zone").value;//文本框
	    var  prodType=$("prodType").value;//文本框
	    var queryCond="日 期："+dayTime+" 区 域："+zone+" 产品类型:"+prodType;
   	    $("excelCondition").value=queryCond;
   	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel_chart.jsp";
		document.forms[0].method = "post";
		document.forms[0].action=url;
		document.forms[0].target="hiddenFrame";
		document.forms[0].submit();
}
function exportImage(){
			var cid=$("cid").value;
			var cid1=$("cid1").value;
   	        var charts1 = getChartFromId("chartdiv_1"+cid);   //生成的FusionCharts图表本身的标识
   	         var charts2 = getChartFromId("chartdiv_2"+cid1);   //生成的FusionCharts图表本身的标识
   	        charts1.exportChart(); 
   	        charts2.exportChart(); 
   	    }	
/**
 *  打开链接菜单
 * 
 */
var openMenuHref=function(zoneName,zoneCode,indexName,indexId){
	   var url="/portalCommon/module/procedure/faultDetail/faultDetail.jsp?zoneCode="+zoneCode+
	   "&startTime="+$("dayTime").value+"&indexId="+indexId+"&prodType="+$("prodType").value;
	   //alert(url)
	    return parent.openTreeTab("119003"+zoneCode+indexId, "故障清单"+"["+zoneName+indexName+"]", base+url, 'top');
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