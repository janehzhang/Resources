/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
var globZoneTree = null;
var zoneCode=$("zoneCode").value==null?userInfo['localCode']:$("zoneCode").value;

var titileInfo=$("indexId").options[$("indexId").selectedIndex].text+"->当日值";
var titileInfo1=$("indexId").options[$("indexId").selectedIndex].text+"->本期累计";
$("titleInfo1").innerHTML=titileInfo;
$("titleInfo2").innerHTML=titileInfo1;
/*function loadTitleInfo(){
	//alert(1);
	var titileInfo=$("indexId").options[$("indexId").selectedIndex].text+"->当日值";
	//alert(2);
	var titileInfo1=$("indexId").options[$("indexId").selectedIndex].text+"->本期累计";
	$("titleInfo1").innerHTML=titileInfo;
	$("titleInfo2").innerHTML=titileInfo1;
}*/

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

var indexInit=function(){
	 //加载预警信息
     loadIndexCdWarn(menuName);
	 //1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     //2.加载业务类型
     loadProdTypeSelect();
     //3.加载指标类型
     //loadIndexSelect();
     //4.控制地市按钮权限
      //controlCityButton();
     // loadTitleInfo();
     getUserSubCode();
     //执行查询数据
      excuteInitData(changeCode);
}
var excuteInitData=function(changeCode){
     var map=new Object();
     map.zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
     map.dayTime     =$("dayTime").value;
     map.indexId     =$("indexId").value == ""?'1' : $("indexId").value;
     map.prodType    =$("prodType").value;
     map.changeCode=changeCode;
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
	 map.indexId='';
     dhx.showProgress("正在执行......");
     MaintionMonitorDayAction.getTableData(map, function (res) {
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
           
           autoRowSpan($("mytbl_1"),1,1);//合并单元格 
          
}
/**
 *  构造表头
 * @param {Object} data
 */
function tableHeader(headColumn){
	var  excelHeader="";
	var  tableHead="<tr>";
	       for(var i=0;i<headColumn.length;i++)
	       {
	    	  if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	    	  {
	    		tableHead +="<td nowrap bgcolor='#cde5fd'><span class='title'>"+headColumn[i]+"</span></td>";  
	    		 excelHeader += headColumn[i]+",";
	    	  } 
	         
	       }
	     tableHead+="</tr>";
	    
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
	   var userCodeData=$("userCodeData").value;
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
				    		          if(tempColumn == '当日值' && dataColumn[i].指标项_ID==3)
				    		          {
				    		        	  if(userCodeData.indexOf("]"+dataColumn[i].区域_ID+"]")==-1){
				    		        		  tableData +="<td nowrap align='center'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>";  
				    		        	  }else{
				    		        	      tableData +="<td nowrap align='center' onclick=\"openMenuHref('"+dataColumn[i].分公司+"','"+dataColumn[i].区域_ID+"','"+dataColumn[i].指标项+"','"+dataColumn[i].指标项_ID+"')\" class='unl'>"+addWaringStyle(dataColumn[i][tempColumn], dataColumn[i].指标项 ,tempColumn)+"</td>"; 
				    		        	  }
				    		          }
				    		          else
				    		          {
				    		        	if(tempColumn == '分公司' && i!=0)
				    		        	 {
				    		        	   tableData +="<td nowrap align='center' onclick=\"drillArea('"+dataColumn[i].分公司+"','"+dataColumn[i].区域_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
				    		        	 }
				    		        	else if(tempColumn=='指标项')
				    		        	 {
				    		               tableData +="<td nowrap align='left'>"+addWaringStyle(dataColumn[i][tempColumn],dataColumn[i].指标项 ,tempColumn)+"</td>";  
				    		        	 }
				    		        	 else
				    		        	 {
				    		               tableData +="<td nowrap align='center'>"+addWaringStyle(dataColumn[i][tempColumn],dataColumn[i].指标项 ,tempColumn)+"</td>";  
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
	//$("chartdiv_line").innerHTML="<center>正在加载.......</center>";
	dhx.showProgress("正在执行......");
    MaintionMonitorDayAction.bulidLineChart(map, {callback:function (res) {
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
	//$("chartdiv_bar").innerHTML="<center>正在加载.......</center>";
	dhx.showProgress("正在执行......");
    MaintionMonitorDayAction.bulidBarChart(map, {callback:function (res) {
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
/*var controlCityButton=function()
{
   if(userInfo['localCode']!='0000') { //不是广东省  （钻取权限控制）
      var array=document.getElementsByName('city');
       for(var i=0;i<array.length;i++){ 
               array[i].style.display = "none";
         }
    }
}*/

var lookCity = function (obj){
	 //var obj = event.srcElement;
	 var map=new Object();
	 map.zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
     map.dayTime     =$("dayTime").value;
     map.indexId     =$("indexId").value;
     map.prodType    =$("prodType").value;
 /*      if(obj.value=='切换地市'){
			  obj.value='切换区域';
			  map.changeZone="0";
	   }else{
			  obj.value='切换地市';
			  map.changeZone="1";
	   }*/
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
//合并单元格
var autoRowSpan=function(tb,row,col){
  for (var i=col; i >-1; i--)//列
   {
	    var lastValue="";
        var value="";
        var pos=1;
        for(var j=row;j<tb.rows.length;j++) //行
        {
            value = tb.rows[j].cells[i].innerText;
            if(lastValue == value)
            {
                tb.rows[j].deleteCell(i);
                tb.rows[j-pos].cells[i].rowSpan = tb.rows[j-pos].cells[i].rowSpan+1;
                pos++;
            }else{
                lastValue = value;
                pos=1;
            }
        }
   }
 }
//查询
var queryData=function(){
	changeCode=getChangeCode(); //by qx
	excuteInitData(changeCode);
} 

//改变图形
var changeImage=function(obj)
{
	$("titleInfo1").innerHTML=$("indexId").options[$("indexId").selectedIndex].text+"->当日值";
	$("titleInfo2").innerHTML=$("indexId").options[$("indexId").selectedIndex].text+"->本期累计"; 
	 
	
	 var map=new Object();
     map.zoneCode    =$("zoneCode").value;
     map.dayTime     =$("dayTime").value;
     map.indexId     ==$("indexId").value == ""?'1' : $("indexId").value;
     map.prodType    =$("prodType").value;
	 changeCode=$("changeCode").value;
     map.changeCode=changeCode;
	    
     //查询图形
     selectChart(map)
}


//钻取
var drillArea=function(zoneName,zoneCode)
{
	var url="/portalCommon/module/procedure/zd/day/maintionMonitorDay.jsp?zoneCode="+zoneCode+"&dateTime="+$("dayTime").value+"&indexId="+$("indexId").value+"&prodType="+$("prodType").value+"&menuId="+menuId;
	    return parent.openTreeTab("119013"+zoneCode, "装维类指标日监测报表"+"["+zoneName+"]", base+url, 'top');
}

/**
 *  打开链接菜单
 * 
 */
var openMenuHref=function(zoneName,zoneCode,indexName,indexId){
	   var url="/portalCommon/module/procedure/faultDetail/faultDetail.jsp?zoneCode="+zoneCode+
	   "&startTime="+$("dayTime").value+"&indexId="+indexId+"&prodType="+$("prodType").value+"&transmitName="+"故障清单["+zoneName+"]";
	   //alert(url)
	    return parent.openTreeTab("119003"+zoneCode+indexId, "故障清单"+"["+zoneName+indexName+"]", base+url, 'top');
 }
function exportExcel(){
		var  dayTime=$("dayTime").value;//文本框
	    var  zone=$("zone").value;//文本框
	    var  prodType=$("prodType").options[$("prodType").selectedIndex].text;//文本框
	    var queryCond="日期："+dayTime+" 区域："+zone+" 业务类型: "+prodType;
   	    $("excelCondition").value=queryCond;
    	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_chart.jsp";
		document.forms[0].method = "post";
		document.forms[0].action=url;
		document.forms[0].target="hiddenFrame";
		document.forms[0].submit();
}
function exportImage(){
			var cid=$("cid").value;
			var cid1=$("cid1").value;
   	        var charts1 = getChartFromId("chartdiv_2"+cid1);   //生成的FusionCharts图表本身的标识
   	         var charts2 = getChartFromId("chartdiv_1"+cid);   //生成的FusionCharts图表本身的标识
   	        charts1.exportChart(); 
   	        charts2.exportChart(); 
}
//所有子节点
function getUserSubCode(){
	 var userCodeData="]";
	 var map=new Object(); 
	 map.userCode=$("userCode").value;
	 CustomerSatisfiedAction.getUserSubCode(map, {callback:function (data) {
         if(data != null){
    		 if(data&&data.userCodeList.length>0){	
    			 for(var i=0;i<data.userCodeList.length;i++){
    				 userCodeData+=data.userCodeList[i].ZONE_CODE+"]";
    			 }
    		 } 
	         }
         $("userCodeData").value= userCodeData;
	   }
	});
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