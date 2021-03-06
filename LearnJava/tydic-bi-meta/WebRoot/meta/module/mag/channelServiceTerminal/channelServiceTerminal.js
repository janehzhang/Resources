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
 
var channelTypeCode =   $('channelTypeCode').value==""?'1':$("channelTypeCode").value;

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
//渠道类型加载
dwrCaller.addAutoAction("loadChannelTypeTreePart","ChannelTypeAction.queryChannelTypeByPathPart");
	var channelTypeConverter=dhx.extend({idColumn:"channelTypeCode",pidColumn:"channelTypeParCode",
	    textColumn:"channelTypeName"
	},treeConverter,false);
	dwrCaller.addDataConverter("loadChannelTypeTreePart",channelTypeConverter);
	dwrCaller.addAction("querySubChannelTypePart",function(afterCall,param){
	    var tempCovert=dhx.extend({isDycload:true},channelTypeConverter,false);
	    ChannelTypeAction.querySubChannelTypePart_new(param.id,function(data){
	        data=tempCovert.convert(data);
	        afterCall(data);
	    })
	});
	

var indexInit=function(){	
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     
     loadChannelTypeTreePart(channelTypeCode,queryform);
     //执行查询数据
       
      excuteInitData();
}


var excuteInitData=function(){
	
		 var map=new Object();
		 
		 map.dateTime=$("dateTime").value;
		 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
 		 map.channelTypeCode = $("channelTypeCode").value;
 		 map.terminal=$('terminalType1').value==""?"0":$("terminalType1").value;
 		 map.state=$('number_state').value;
 	     dhx.showProgress("正在执行......");
	     NewTwoChannelAction.channelServiceTERMINAL(map, function (res) {
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
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>服务分类</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='4' style='width:150px'><span class='title'>服务量/占比</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='4' style='width:150px'><span class='title'>用户量/占比</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>移动</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>宽带</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>固话</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>小灵通</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>校园市场</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>农村市场</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>大客户</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>商业客户</span></td>"
			
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