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
     //执行查询数据
      excuteInitData();
}


var excuteInitData=function(){
		indexExp();
		 var map=new Object();
 		 map.dateTime=$("dateTime").value;
		 map.startDate=$("dateTime").value;
		 map.endDate=$("dateTime").value; 
		 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
 		 map.dateType="1";//周
 		 map.queryType=queryType;
 	     dhx.showProgress("正在执行......");
	     NewTwoChannelAction.queryChannelAll_pg(map, function (res) {
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
	var tableHead="<tr>"
		    +"<td bgcolor='#cde5fd'  rowspan='2'><span class='title'>大区</span></td>"
		    +"<td bgcolor='#cde5fd'  rowspan='2' ><span class='title'>区域</span></td>"
		    +"<td bgcolor='#cde5fd'  rowspan='2' ><span class='title'>总服务量</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='2' ><span class='title' align='center' >10000号人工</span></td>" 
		    +"<td bgcolor='#cde5fd'  colspan='2'><span class='title' align='center'>10000号自助</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='2'><span class='title' align='center'>10001号自助</span></td>" 
		    +"<td bgcolor='#cde5fd'  colspan='2'><span class='title' align='center'>速拨干线</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='2'><span class='title' align='center'>自营厅</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>短厅</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>网厅</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>WAP厅</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>客户端</span></td>"
		   +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>自助终端</span></td>"
		   +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>QQ客服人工</span></td>"
		   +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>QQ客服自助</span></td>"
		   +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>微信客服人工</span></td>"
		   +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>微信客服自助</span></td>"  
		  +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>易信客服人工</span></td>" 
		  +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>易信客服自助</span></td>" 
		 +"</tr>"
		 +"<tr>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title'>服务量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>占比</span></td>"
		 +"</tr>";
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
//钻取
var drillArea=function(zoneName,zoneCode){	
	var dateTime=$("dateTime").value;
	var url="/reports/channel/channelTouch/allChannelSerZone_Week.jsp?zoneCode="+zoneCode+"&weekNo="+dateTime+"&menuId="+menuId;
	
	return parent.openTreeTab("155013"+zoneCode+channelTypeCode, "渠道偏好视图周报"+"["+zoneName+"]", base+url, 'top');
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
 
function exportExcel(){
		var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//文本框
		    var  zone=$("zone").value;//文本框
		    var queryCond="查询周："+dateTime+"    区域："+zone;  
	   	    $("excelCondition").value=queryCond; 
	     	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implChannelMoreExcel.jsp";
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
}


//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 var explainData="";
	 var map=new Object(); 
	 if(queryType==0){
	 	map.rptId="1005";
	 }
	 if(queryType==10){
	  map.rptId="1007";
	 }
	 if(queryType==20){
	  map.rptId="1010";
	 }
	 if(queryType==30){
	  map.rptId="1008";
	 }
	 if(queryType==40){
	  map.rptId="1011";
	 }	
	 if(queryType==50){
	  map.rptId="1012";
	 }	
	 if(queryType==60){
	  map.rptId="1009";
	 }		 	 	 	
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