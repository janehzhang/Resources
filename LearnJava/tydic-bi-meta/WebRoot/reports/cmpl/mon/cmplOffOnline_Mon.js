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

var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;

if(user['zoneId']!='1') { //不是广东省  （钻取权限控制）
	var array=document.getElementsByName('city');
	    for(var i=0;i<array.length;i++){ 
	      array[i].style.display = "none";
	}
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
var indexInit=function(){
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     //执行查询数据
     excuteInitData();
}


var excuteInitData=function(){
	 var map=new Object();
	 map.reportId="30001";
	 map.parameters="REGION_ID$MONTH_ID"; 
	 var zoneId=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
     map.values= zoneId+"$"+$("dateTime").value.replaceAll("-", "");
     map.field="OFFLINE_VALUE";
     map.rptIndex="cmplOffLineMon"; 
     map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
     map.dateTime= $("dateTime").value;
     dhx.showProgress("正在执行......");
     CmplIndexAction.getCmplSkip_pg(map, function (res) {
     //	$('chartdiv1').innerHTML='';
     	//$('chartdiv2').innerHTML='';
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				            return;
 				        }
 				     /*chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "500", "230", "0", "0");
 			    	 chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+Math.random(), "500", "230", "0", "0");
 			         chart1.setDataXML(res.barChartMap);
 			         chart2.setDataXML(res.lineChartMap);
 			         chart1.render("chartdiv1");
 			         chart2.render("chartdiv2"); */
 					 buildTable(res);
 					// $("city").value='切换地市'
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
	var  tableHead="";
	var  excelHeader="";
    tableHead+="<tr>"
    	        +"<td nowrap bgcolor='#cde5fd' rowspan='2'><span class='title'>申告地市</span></td>"
				+"<td nowrap bgcolor='#cde5fd' rowspan='2'><span class='title'>离网月份</span></td>"
				+"<td bgcolor='#cde5fd' style='width:540px' colspan='12'><span class='title'>投诉月份的离网用户数</span></td>"
		 +"</tr>"
		 +"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>一月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>二月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>三月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>四月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>五月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>六月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>七月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>八月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>九月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>十月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>十一月</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>十二月</span></td>";
		    tableHead+="</tr>";
      for(var i=0;i<headColumn.length;i++)
      {
       if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	    {
	      // tableHead +="<td nowrap bgcolor='#cde5fd'><span class='title'>"+headColumn[i]+"</span></td>";  
	       excelHeader += headColumn[i]+",";
        }
      }
   // tableHead+="</tr>";
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
		    		            	   if(tempColumn == '区域' && i!=0)
			    		        	 {
		    		            		   if(dataColumn[i].区域_层级<'4'){
		    		            			   tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].区域+"','"+dataColumn[i].区域_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 	 
				    		        	  }else{
				    		        		  tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
				    		        	  }
			    		        	 }else{
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
var drillArea=function(zoneName,zoneCode)
{	
	var url="/reports/cmpl/mon/cmplOffOnline_Mon.jsp?dateTime="+$("dateTime").value+"&zoneCode="
	+zoneCode;
	return parent.openTreeTab("123001"+zoneCode,"年度离网用户投诉分布情况月报表"+"["+zoneName+"]",base+url,'top');
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
        map.field="OFFLINE_VALUE";
        map.rptIndex="cmplOffLineMon"; 
		  if(obj.value=='切换地市'){
			  obj.value='切换区域';
			  map.changeZone="1";
		  }else{
			  obj.value='切换地市';
			  map.changeZone="0";
		  }  
		  CmplIndexAction.loadSet21AreaSkip(map, {callback:function (res) {
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