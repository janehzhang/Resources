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
var globProdTypeTree = null;
var globCmplBusiTypeTree = null;

var titileInfo="本地抱怨率->当月值";
var titileInfo1="本地抱怨率->当月值";
$("titleInfo1").innerHTML=titileInfo;
$("titleInfo2").innerHTML=titileInfo1;

//查询条件参数
var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var prodTypeCode =   $('prodTypeCode').value==''?"-1":$('prodTypeCode').value;
var cmplBusiTypeCode =   $('cmplBusiTypeCode').value==''?"1":$('cmplBusiTypeCode').value;

//by qx
tZoneCode=$("zoneCode").value;
tDefaultZoneCode=$("defaultZoneCode").value;
if(tZoneCode!=tDefaultZoneCode){//下钻
    $("changeCode").value='1';
}else{
	$("changeCode").value='2';
}
var changeCode = $("changeCode").value;

var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** Tree head **/
//区域树
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCodeGrid");
var zoneConverter=dhx.extend({idColumn:"zoneCode",pidColumn:"zoneParCode",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
dwrCaller.addAction("querySubZoneCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZoneCodeGrid_Dg(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
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

var indexInit=function(){
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     //加载业务类型树
     loadProdTypeTree(prodTypeCode, queryform);
     //加载投诉现象树
     loadCmplBusiTypeTree(cmplBusiTypeCode ,queryform);
     getUserSubCode();
     //执行查询数据 by qx
     excuteInitData(changeCode);
}
//by qx
var excuteInitData=function(changeCode){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	 var map=new Object();
	 map.reportId="10011";
	 map.parameters="REGION_ID$MONTH_ID$PROD_TYPE_ID$CMPL_BUSI_TYPE_ID"; 
	 var zoneId=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
	 var prodTypeId =   $('prodTypeCode').value==''?"-1":$('prodTypeCode').value;
	 var cmplBusiTypeId =   $('cmplBusiTypeCode').value==''?"1":$('cmplBusiTypeCode').value;
     map.values= zoneId+"$"+$("dateTime").value.replaceAll("-", "")+"$"+prodTypeId+"$"+cmplBusiTypeId;
     map.field="CMPL";
     map.rptIndex="allBusiCmplMon"; 
     map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
     map.dateTime= $("dateTime").value;
     map.prodTypeCode =   $('prodTypeCode').value==''?"-1":$('prodTypeCode').value;
     map.cmplBusiTypeCode =   $('cmplBusiTypeCode').value==''?"1":$('cmplBusiTypeCode').value;
     //by qx
     map.changeCode=changeCode;
     dhx.showProgress("正在执行......");
     CmplIndexAction.getCmplSum_pg_Dg(map, function (res) {
     	$('chartdiv1').innerHTML='';
     	$('chartdiv2').innerHTML='';
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("查询失败，请重试!");
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
    	   if(headColumn[i].length>6){
	          tableHead +="<td bgcolor='#cde5fd'><span class='title'>"+headColumn[i]+"</span></td>";  
    	   }else{
    		   tableHead +="<td nowrap bgcolor='#cde5fd'><span class='title'>"+headColumn[i]+"</span></td>";  
    	   }
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
  var tableData="";
  var  excelData="";
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
		    		            	   if(tempColumn == '分公司' && i!=0)
			    		        	 {
		    		            		   if(dataColumn[i].层级_ID<'6'){
					    		        	     tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
					    		        	  }else{
					    		        		  tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
					    		        	  }   
			    		        	 }else if(tempColumn == '本地抱怨量')
			    		        	 {
			    		        		 if(userCodeData.indexOf("]"+dataColumn[i].分公司_ID+"]")==-1){
			    		        			 tableData +="<td nowrap>"+dataColumn[i][tempColumn]+"</td>"; 
			    		        		 }else{
			    		        		    tableData +="<td nowrap onclick=\"drillList('"+dataColumn[i].分公司+"','"+dataColumn[i].分公司_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";
			    		        		 }			    		        	 } else{
				    		        	 tableData +="<td nowrap>"+dataColumn[i][tempColumn]+"</td>"; 
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
//查询 by qx
var queryData=function(){
	changeCode=getChangeCode(); //by qx
	excuteInitData(changeCode);
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
	var prodTypeCode = $('prodTypeCode').value==''?"-1":$('prodTypeCode').value;
	var cmplBusiTypeCode =   $('cmplBusiTypeCode').value==''?"1":$('cmplBusiTypeCode').value;
	var url="/reports/cmpl/mon/allBusiCmpl_Mon_Dg.jsp?dateTime="+$("dateTime").value+"&zoneCode="
	+zoneCode+"&prodTypeCode="+prodTypeCode+"&cmplBusiTypeCode="+cmplBusiTypeCode+"&menuId="+menuId;
	return parent.openTreeTab("123006"+zoneCode,"本地抱怨率月报表"+"["+zoneName+"]",base+url,'top');	
}
//抱怨量钻取
var drillList=function(zoneName,zoneCode){
	 var dateTime=$("dateTime").value;//201306
	 var year= dateTime.substring(0,4);
	 var month=dateTime.substring(4,6);
	 var startDate=year+"-"+month+"-01";
	 var endDate=year+"-"+month+"-"+getLastDay(year,month);
	var url="/reports/cmpl/cmpl_list_mon.jsp?startDate="+startDate+"&endDate="+endDate+"&zoneCode="
	+zoneCode+"&prodTypeCode="+$('prodTypeCode').value+"&cmplBusiTypeCode="+$('cmplBusiTypeCode').value+"&indId=3"+"&transmitName="+"本地抱怨清单["+zoneName+"]";
	return parent.openTreeTab("122013"+zoneCode,"本地抱怨清单["+zoneName+"]",base+url,'top');
	
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
//切换地市  by qx
var lookCity = function (obj){
    var map=new Object();
        map.dateTime=$("dateTime").value;
        map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
        map.prodTypeCode =   $('prodTypeCode').value==''?"-1":$('prodTypeCode').value;
        map.cmplBusiTypeCode =   $('cmplBusiTypeCode').value==''?"1":$('cmplBusiTypeCode').value;
        map.field="CMPL";
        map.rptIndex="allBusiCmplMon";
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
			  CmplIndexAction.loadSet21AreaChart(map, function (res) {
				  dhx.closeProgress();
	 	         if(res != null){
	                     build21Chart(res);
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
function exportExcel(){
		var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
	    var  zone=$("zone").value;//区域
	    var  prodType=$("prodType").value;//业务类型
	    var  cmplBusiType=$("cmplBusiType").value;//投诉表象
	    var queryCond="月份："+dateTime+"    区域："+zone+"    产品类型:"+prodType+"    投诉表象："+cmplBusiType;
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
   	        var charts1 = getChartFromId("ChartId1_"+cid1);   //生成的FusionCharts图表本身的标识
   	         var charts2 = getChartFromId("ChartId2_"+cid);   //生成的FusionCharts图表本身的标识
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