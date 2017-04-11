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
var zoneCode = $('zoneCode').value==""?"0000":$("zoneCode").value;

var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPath1");
var zoneConverter=dhx.extend({idColumn:"zoneCode",pidColumn:"zoneParCode",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
dwrCaller.addAction("querySubZoneCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZone1(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
var indexInit=function(){
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     //执行查询数据
     excuteInitData();
     indexExp();
}


var excuteInitData=function(){
	 var map=new Object();
     map.dateTime  =$("dateTime").value;
     map.zoneCode  =$("zoneCode").value==""?"0000":$("zoneCode").value;
     map.isRealName=$("isRealName").value;
     map.custType  =$("custType").value;
     map.custLevel =$("custLevel").value;
     map.prodId    =$("prodId").value;
     map.erId      =$("erId").value;
     dhx.showProgress("正在执行......");
     CodeMonitorAction.getCustCodeMonitor_pg(map, function (res) {
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				            return;
 				        }
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
var  excelHeader="";
var   tableHead="<tr>";
var custLevel=$("custLevel").value;
var isRealName=$("isRealName").value;
var temp="";
if(isRealName=='1'){
	temp="本地总体实名VIP客户数";
}else{
	temp="本地总体非实名VIP客户数";
}
if(custLevel=='1'){//VIP
	tableHead="<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='2'><span class='title'>分公司</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>VIP客户本月激活客户密码数</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>客户密码累计激活VIP客户数</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>未激活客户密码VIP客户数</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>"+temp+"</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>客户密码当月激活率</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>客户密码整体激活率</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' ><span class='title'>钻卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>金卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>银卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>钻卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>金卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>银卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>钻卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>金卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>银卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>钻卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>金卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>银卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>钻卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>金卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>银卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>钻卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>金卡用户</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>银卡用户</span></td>";
}else{//普通
	 for(var i=0;i<headColumn.length;i++){
		 if(headColumn[i].indexOf('_')==-1){//字段 有 "-"  表示不展示
		     tableHead +="<td nowrap bgcolor='#cde5fd'><span class='title'>"+headColumn[i]+"</span></td>";
	     } 
	 }
}
   tableHead+="</tr>";
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
		    		            	   if((dataColumn[i][tempColumn])=="%"){
		    		            			   tableData +="<td>-</td>";
		    		            			   excelData += "-}";
		    		            	   }else{
						    		       tableData +="<td>"+dataColumn[i][tempColumn]+"</td>";
					    		           excelData += dataColumn[i][tempColumn]+"}";
		    		            	   }
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
//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 var explainData="";
	 var rptId="";
	 var map=new Object(); 
	 var isRealName=$("isRealName").value;
	 var custType  =$("custType").value;
	 var custLevel =$("custLevel").value;
	 var prodId    =$("prodId").value;
	 var erId      =$("erId").value;
	 if(isRealName=="1"&&custType=="1"&&custLevel==""&&prodId==""&&erId==""){
		 rptId="14"
	 }else if(isRealName==""&&custType=="2"&&custLevel==""&&prodId==""&&erId==""){
		 rptId="15"
	 }else if(isRealName==""&&custType=="2"&&custLevel==""&&prodId==""&&erId=="1"){
		 rptId="17"
	 }else if(isRealName=="0"&&custType==""&&custLevel==""&&prodId=="1"&&erId==""){
		 rptId="16"
	 }else if(isRealName=="0"&&custType==""&&custLevel==""&&prodId=="2"&&erId==""){
		 rptId="18"
	 }else if(isRealName=="1"&&custType==""&&custLevel=="1"&&prodId==""&&erId==""){
		 rptId="14"
	 }else if(isRealName=="0"&&custType==""&&custLevel=="1"&&prodId==""&&erId==""){
		 rptId="16"
	 }else if(isRealName==""&&custType==""&&custLevel==""&&prodId=="4"&&erId==""){
		 rptId="14"
	 }else if(isRealName==""&&custType==""&&custLevel==""&&prodId=="3"&&erId==""){
		 rptId="14"
	 }
	 map.rptId=rptId;
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
dhx.ready(indexInit);