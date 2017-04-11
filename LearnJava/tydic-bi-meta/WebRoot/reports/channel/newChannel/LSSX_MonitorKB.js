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
     
     //loadChannelTypeTreePart(channelTypeCode,queryform);
     //执行查询数据
     excuteInitData();
      
}


var excuteInitData=function(){
		 indexExp();
		 var map=new Object();
		 map.dateTime=$("dateTime").value;
		 //map.startDate=$("dateTime").value;
		 //map.endDate=$("dateTime").value;  
		 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
 		 map.dateType="2";//天
 		 
 		 map.pageCount=page.pageCount;    //每页显示多少条数
	     map.currPageNum=page.currPageNum;//当前第几页
 		 
 	     dhx.showProgress("正在执行......");
	     NewTwoChannelAction.getLSSXMonitorKBData(map, function (res) {
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
 
    tableStr ="<table  id='tab1' width='120%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    //autoRowSpan($("tab1"),0,1);
    //autoRowSpan($("tab1"),0,0);
    
    $("totalCount").value=data.allPageCount;
    page.buildPage(data.allPageCount , 1);//调用公共分页函数page.js
    return data.allPageCount;
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
//钻取
var drillArea=function(zoneName,zoneCode){	
	var startDate=$("startDate").value;
	var endDate=$("endDate").value;
	
	var url="/reports/channel/channelTouch/allChannelSerZone_Week.jsp?zoneCode="+zoneCode+"&startDate="+startDate+"&endDate="+endDate+"&menuId="+menuId;
	 
	return parent.openTreeTab("155013"+zoneCode+channelTypeCode, "渠道服务一级报表"+"["+zoneName+"]清单", base+url, 'top');
}
//查询
var queryData=function(){
	page.resetPage(); 
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
		    var  dateTime=$("dateTime").value;			 
			var  zone=$("zone").value;//区域
		    var queryCond="月份："+dateTime+"  区域："+zone;
		    var totalCount   =$("totalCount").value;
     	    $("fileType").value="txt";
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
/*function downloadFile(){
	var  dateTime=$("dateTime").value;
	var filePath = "E:/移动后付费用户信用管理宽表_" + dateTime + ".txt";
	//文件由AutoCreateTXTFile方法自动生成，且路径设置必须与AutoCreateTXTFile方法里的路径设置一致
	$("path").value = filePath;
 	var url = getBasePath()+"/downLoadServlet";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].submit();
	setInterval("closePro()",10000);
}*/

//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 var explainData="";
	 var map=new Object(); 
	 map.rptId="1029";
	 CmplIndexAction.getIndexExplain(map, {callback:function (data) {
         if(data != null){
        	 var tableStr="<table width='100%' border='0'  cellpadding='0' cellspacing='0'>";
    		 if(data&&data.expList.length>0){	
    			 for(var i=0;i<data.expList.length;i++){
    				 tableStr =tableStr+
    		         "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
    		           +"<td align=\"left\"><font style='font-weight:bold;' >&nbsp;"+data.expList[i].INDEX_NAME+"  :  "+"</font>"
    		           +data.expList[i].INDEX_EXPLAIN+"</td>"   
    		           +"</tr>";
    				 explainData+=data.expList[i].INDEX_NAME+"  :  "+ data.expList[i].INDEX_EXPLAIN+"]";
    			 }
    			 tableStr +="</table>"; 
    			 $("explain").value=explainData;
    			 $('index_exp').innerHTML=tableStr;
    		 } 
	         }
	   }
	});
}
 
dhx.ready(indexInit);