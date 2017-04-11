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
	     NewTwoChannelAction.getCreditManageMonthData(map, function (res) {
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
		+"<td bgcolor='#cde5fd' style='width:1%' rowspan='4' ><span class='title'>大区</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='4' ><span class='title'>分公司</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:21%' colspan='11' ><span class='title' align='center' >参与信用管理用户</span></td>" 
	    +"<td bgcolor='#cde5fd' style='width:71%' colspan='19' ><span class='title' align='center' >（有限信用用户）信控停复机</span></td>" 
	    +"<td bgcolor='#cde5fd' style='width:45%' colspan='15'><span class='title' align='center'>（有限信用用户）信控欠费及收入影响</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:9%' colspan='3'><span class='title' align='center'>（有限信用用户）临时授信情况</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:11%' colspan='3' ><span class='title' align='center' >信用服务投诉情况</span></td>" 
	 +"</tr>"
	 +"<tr>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='3'><span class='title'>总数（万户）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='3'><span class='title'>固定信用客户数（万户）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='3'><span class='title'>零信用用户（万户）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:24%' colspan='8'><span class='title'>有限信用用户（万户）</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:36%' colspan='12'><span class='title'>当月信停情况：</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:36%' colspan='7'><span class='title'>复机情况</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='3'><span class='title'>总收入（万元）</span></td>"	    
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='3'><span class='title'>本月信控增收金额（万元）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='3'><span class='title'>近半年信控月均增收金额（万元）</span></td>"	    
	    +"<td bgcolor='#cde5fd' style='width:24%' colspan='8'><span class='title'>当月欠费及收入</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:11%' colspan='4'><span class='title'>（截至当月）累计欠费及收入</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='3'><span class='title'>临时授信用户数</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='3'><span class='title'>户均临时授信金额</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='3'><span class='title'>临时授信用户信停率</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='3'><span class='title'>信用服务投诉量（宗）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='3'><span class='title'>信用服务投诉率</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:1%' rowspan='3'><span class='title'>信用服务投诉处理率</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='3'><span class='title'>客户满意率</span></td>"
	  +"</tr>"
	  +"<tr>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>用户数</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>5星级</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>4星级</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>3星级</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>2星级</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>1星级</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>0星级</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>未评级</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1.5%' rowspan='2'><span class='title'>当月信控停机用户数（万户）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>停机用户信控停机率</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>信控停机率</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1.5%' rowspan='2'><span class='title'>当月复通用户数（万户）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>当月信停复机率</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>当月户均信停天数</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:18%' colspan='6'><span class='title'>当月信停最长停机天数的用户数分布（万户）</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>当月信控停机总次数</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>当月户均停机次数</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>3天复机率（当月）</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>5天复机率（当月）</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>7天复机率（当月）</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>当月复机率（当月）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>半年内信控停机总次数</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>半年内户均停机次数</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>3天复机率（半年内）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>5天复机率（半年内）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>7天复机率（半年内）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>30天复机率（半年内）</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>当月欠费用户数（万户）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>当月新增欠费额</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>当月欠费占收比</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>其中:信控内欠费占比</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>其中:超信控欠费占比</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>当月异常欠费用户数</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>当月异常用户占比</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>当月异常用户欠费占比</span></td>"
	    
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>累计欠费用户数（万户）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>累计欠费额</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>其中:信控内欠费占比</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%' rowspan='2'><span class='title'>其中:超信控欠费占比</span></td>"
	  +"</tr>"
	  +"<tr>"
	    +"<td bgcolor='#cde5fd' style='width:1%'><span class='title'>1天</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%'><span class='title'>(1,3]天</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%'><span class='title'>(3,5]天</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%'><span class='title'>(5,7]天</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%'><span class='title'>(7,30]天</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:1%'><span class='title'>30天以上</span></td>"
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
	 map.rptId="1027";
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