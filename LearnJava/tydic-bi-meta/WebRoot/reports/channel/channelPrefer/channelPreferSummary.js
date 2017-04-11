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
//查询系统参数
var zoneCode =   $('zoneCode').value==""?"0000":$("zoneCode").value;

	
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
   
var indexInit=function(){
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     //执行查询数据
      excuteInitData();
}
 

var excuteInitData=function(){
	 var map=new Object();
	 map.dateTime=$("dateTime").value;	 
	 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
     dhx.showProgress("正在执行......");
     NewTwoChannelAction.getChannelPreferSummary_Pg(map, function (res) {
    	 dhx.closeProgress();
 		 if (res == null) {
 			 dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 			 return;
 		 }
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
    autoRowSpan($("tab1"),0,2);
    autoRowSpan($("tab1"),0,1);
    autoRowSpan($("tab1"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	var tableHead="<tr>"
	    +"<td bgcolor='#cde5fd' style='width:40%' ><span class='title' align='center'>区域</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:30%' ><span class='title' align='center'>渠道接触数</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:30%' ><span class='title' align='center'>用户量</span></td>"
	 +"</tr>";
     var  excelHeader="";
      for(var i=0;i<headColumn.length;i++)
      {
       if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	    {
	       excelHeader += headColumn[i]+",";
        }
      }
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
  if(dataColumn&&dataColumn.length>0){  
	   for(var i=0;i<dataColumn.length;i++){
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	  for(var j=0;j<headColumn.length;j++) {
   		               var tempColumn=headColumn[j];   
		    		        tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
   		            	    excelData += dataColumn[i][tempColumn]+"}";
   		      }
	    	  tableData +="</tr>";
			  excelData +="]";
	    	}   
	 }else{
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
function exportExcel(){
		    var  startDate=$("dateTime").value;//文本框\
		    var  zone=$("zone").value;//文本框
		    var queryCond="月份："+startDate+"    区域："+zone; 
	   	    $("excelCondition").value=queryCond; 
	   	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel.jsp";//存储过程表头
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
}
dhx.ready(indexInit);