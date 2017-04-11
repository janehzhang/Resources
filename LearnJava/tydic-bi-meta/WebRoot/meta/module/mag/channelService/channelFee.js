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
       
      excuteInitData();
}


var excuteInitData=function(){
	
		 var map=new Object();
		 
		 map.dateTime=$("dateTime").value;
		 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
		 map.service=$('serviceType1').value==""?"0":$("serviceType1").value;
 		 map.terminal=$('terminalType1').value==""?"0":$("terminalType1").value;
 		 map.state=$('number_state').value;
		 map.touchpoint='3';
 		dhx.showProgress("正在执行......");
	     NewTwoChannelAction.channelSix(map, function (res) {
	     	   dhx.closeProgress();
	 			 if (res == null) {
	 				   dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
	 				     return;
	 			 }else{
	 				 buildTable(res);
	 			 }
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
	 tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>区域</span></td>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:150px'><span class='title'>渠道类型</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='7' style='width:150px'><span class='title'>服务量/占比</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='7' style='width:150px'><span class='title'>用户量/占比</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>0元以下</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>0-50元</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>50-100元</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>100-150元</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>150-200元</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>200-400元</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>400以上</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>0元以下</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>0-50元</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>50-100元</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>100-150元</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>150-200元</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>200-400元</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>400以上</span></td>"
	        tableHead+="</tr>";
var  excelHeader="";
      for(var i=0;i<headColumn.length;i++){
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
   		               if(tempColumn.indexOf('_')==-1){ 
		    		      tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
   		            	  excelData += dataColumn[i][tempColumn]+"}";
		    		} 
   		      }
	    	  tableData +="</tr>";
			  excelData +="]";
	    	}   
	       } else {
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
 //导出
function exportExcel(){

 	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implChannelMoreExcel.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}

//指标解释



 
dhx.ready(indexInit);