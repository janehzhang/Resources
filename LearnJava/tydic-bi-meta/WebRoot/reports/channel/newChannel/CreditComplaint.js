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
var page=null;

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
	 page=Page.getInstance();
	 page.init();
	
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     
    // loadChannelTypeTreePart(channelTypeCode,queryform);
     //执行查询数据
     excuteInitData();
      
}


var excuteInitData=function(){
			//indexExp();
		 var map=new Object();
		 //map.dateTime=$("dateTime").value;
		 map.startDate=$("startDate").value;
		 map.endDate=$("endDate").value; 
		 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
 		 map.dateType="0";//天
 		 
 		 map.pageCount=  page.pageCount;    //每页显示多少条数
 	     map.currPageNum=page.currPageNum;//当前第几页
 		 
 	     dhx.showProgress("正在执行......");
	     NewTwoChannelAction.getCreditComplaintData(map, function (res) {
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
 
    tableStr ="<table  id='tab1' width='200%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
     
    tableStr+=tableHeader(data.headColumn);
   
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
     
    //autoRowSpan($("tab1"),0,1);
    //autoRowSpan($("tab1"),0,0);
    $("totalCount").value=data.allPageCount;
    page.buildPage(data.allPageCount , 1);//调用公共分页函数page.js
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
	if(isEffectDate())
	{
		 page.resetPage();
		 excuteInitData();
	}
	else
	{
	  alert("不能选择跨月！");	
	}
}
// 比较两个日期
var isEffectDate=function()
{
	var startDate=$("startDate").value;
	startDate=startDate.substr(5,2);
	var endDate=  $("endDate").value;
	endDate=endDate.substr(5,2);
	if(startDate==endDate){
		return true;
	}else{
	   return false;
	}
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
			/*var channelType =$("channelType").value;
		    var  startDate=$("startDate").value;//时间
			var  endDate=$("endDate").value;//时间
			var  zone=$("zone").value;//区域
		    var queryCond="日期从："+startDate+"    至："+endDate+"    区域："+zone+"   渠道类型:"+channelType; 
	   	    $("excelCondition").value=queryCond; 
	     	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implChannelMoreExcel.jsp";
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();*/
			
			var startDate=$("startDate").value;
     	    var endDate=$("endDate").value;
     	    var zone=$("zone").value;     	    
     	    var queryCond="日期从:"+startDate+"    至:"+endDate+"    区域："+zone;
     	    var totalCount   =$("totalCount").value;
     	    $("fileType").value="csv";
			$("excelCondition").value=queryCond;
     	    dhx.showProgress("正在执行......");
     	    var url = getBasePath()+"/downLoadServlet";
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
			setInterval("closePro()",20000);
}

function closePro(){
	dhx.closeProgress();
	}
 
dhx.ready(indexInit);