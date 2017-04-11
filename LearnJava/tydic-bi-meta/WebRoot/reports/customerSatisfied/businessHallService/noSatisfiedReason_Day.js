/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();
var globZoneTree = null;

//当前系统的主页Path
var base = getBasePath();
//查询条件参数
var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?zoneCode:$("zoneCode").value;
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

var userInit=function(){
	//1.加载地域树 
    loadZoneTreeChkBox(zoneCode,queryform);
    //执行查询数据
    excuteInitData();
}

var excuteInitData=function(){
    var map=new Object();
    map.dateTime   =$("dateTime").value;
    map.zoneCode=$('zoneCode').value==""?zoneCode:$("zoneCode").value;
    map.dateType   ="0";//日期类型--日报
    map.actType   ="2"; //触点名称--营业厅服务
    map.field   ="NO_SATISFY_NUM";//不满意量
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getNOSatisfiedReason_Week(map, function (res) {
    	$('chartdiv').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				     chart=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+Math.random(), "490", "230", "0", "0");
			         chart.setDataXML(res.pieChartMap);
			         chart.render("chartdiv");
					 buildTable(res);
   });	
}
function buildTable(data){
    tableStr ="<table  id='tab1' width='490px' height='230'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    autoRowSpan($("tab1"),1,1);
    autoRowSpan($("tab1"),1,0);
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
		    		        	   	 if(tempColumn == '不满意量')
				    		           {
		    		        	   		 tableData +="<td nowrap align='center' onclick=\"openMenuHref('"+dataColumn[i].分公司+"','"+tempColumn+"','3','"+dataColumn[i].原因_编码+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>"; 
				    		           }
		    		        	   	   else
				    		           {
				    		        	tableData +="<td nowrap align='center'>"+dataColumn[i][tempColumn]+"</td>"; 
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

/**
 *  打开链接菜单
 * 
 */
var openMenuHref=function(zoneName,indexName,indexId,reasonId){
	  var zoneCode=$('zoneCode').value;
	  var dateTime=$("dateTime").value;
	   var startTime=dateTime;
	   var endTime =dateTime;
	 var url="/portalCommon/module/procedure/visitDetail/hf/ADSL_visitList.jsp?zoneCode="+zoneCode+
	   "&startTime="+startTime+"&endTime="+endTime+"&reasonId="+reasonId+"&indexId="+indexId;
	 return parent.openTreeTab("124015"+zoneCode+indexId, "营业厅服务即时回访清单"+"["+zoneName+indexName+"]", base+url, 'top');
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
dhx.ready(userInit);