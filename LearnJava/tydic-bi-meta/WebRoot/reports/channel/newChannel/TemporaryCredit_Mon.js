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
 
var channelTypeCode =   "1";
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
 		 
 	     dhx.showProgress("正在执行......");
	     NewTwoChannelAction.getTemporaryCreditMonthData(map, function (res) {
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
    autoRowSpan($("tab1"),0,1);
    autoRowSpan($("tab1"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	var tableHead="<tr>"
		+"<td bgcolor='#cde5fd' style='width:1%' rowspan='3' ><span class='title'>大区</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='3' ><span class='title'>分公司</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' colspan='6' ><span class='title' align='center' >整体情况</span></td>" 
	    +"<td bgcolor='#cde5fd' style='width:11%' colspan='6' ><span class='title' align='center' >临时授信受理情况</span></td>" 
	    +"<td bgcolor='#cde5fd' style='width:38%' colspan='36'><span class='title' align='center'>临时授信用户分布</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:9%' colspan='3'><span class='title' align='center'>（有限信用用户）临时授信情况</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:11%' colspan='3' ><span class='title' align='center' >信用服务投诉情况</span></td>" 
	 +"</tr>"
	 +"<tr>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>临时授信用户数</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:2%' colspan='2'><span class='title'>服务属性</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%' colspan='3'><span class='title'>临时授信前用户状态</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:7%' colspan='2'><span class='title'>受理方式</span></td>"	    
	    +"<td bgcolor='#cde5fd' style='width:4%' colspan='4'><span class='title'>临时授信用户停复机情况</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:14%' colspan='12'><span class='title'>入网渠道分布</span></td>"	    
	    +"<td bgcolor='#cde5fd' style='width:5%' colspan='5'><span class='title'>基础授信系数分布</span></td>"	    
	    +"<td bgcolor='#cde5fd' style='width:6%' colspan='6'><span class='title'>网龄分布（月）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:7%' colspan='7'><span class='title'>信用星级</span></td>"	    
	    +"<td bgcolor='#cde5fd' style='width:6%' colspan='6'><span class='title'>ARPU分布（元）</span></td>"
	  +"</tr>"
	  +"<tr>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>政企</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>公众</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>信控停机</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>正常</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>其它</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>自助受理</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>人工受理</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>短厅</span></td>"	    
	    //+"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>网厅</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>掌厅（WAP厅）</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>微信</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>其它</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>临时授信后T+8天内被信控停机用户数</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>信停率</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>截至当前复通用户数</span></td>"	    
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>复通率</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>电子渠道</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>政企直销代理</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>10000号</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>代理商（CP/SP)</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>政企直销经理</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>专营店</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>便利点</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>独立店</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>连锁店</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>公众直销代理</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>公众直销经理</span></td>"	    
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>自有厅</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>互联网代理</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>其它</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(0,1]</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(1,2]</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(2,3]</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(3,4]</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>其它</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(0,3]</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(3,6]</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(6,12]</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(12,18]</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(18,24]</span></td>"	    
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>24以上</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>5星级</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>4星级</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>3星级</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>2星级</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>1星级</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>0星级</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>未评级</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(0,20]</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(20,49]</span></td>"	    
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(49,89]</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(89,189]</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>(189,289]</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' ><span class='title'>>289</span></td>"
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
	var startDate=$("startDate").value;
	var endDate=$("endDate").value;
	
	var url="/reports/channel/channelTouch/allChannelSerZone_Week.jsp?zoneCode="+zoneCode+"&startDate="+startDate+"&endDate="+endDate+"&menuId="+menuId;
	 
	return parent.openTreeTab("155013"+zoneCode+channelTypeCode, "渠道服务一级报表"+"["+zoneName+"]清单", base+url, 'top');
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
		    var  dateTime=$("dateTime").value;			 
			var  zone=$("zone").value;//区域
		    var queryCond="月份："+dateTime+"  区域："+zone; 
	   	    $("excelCondition").value=queryCond; 
	   	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel.jsp";
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
	 map.rptId="1028";
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