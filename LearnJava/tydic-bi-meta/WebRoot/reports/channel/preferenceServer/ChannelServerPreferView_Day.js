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

var preferType=$("preferType").value;


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
			indexExp();
		 var map=new Object();
		 map.preferType=preferType;
		 map.startDate=$("startDate").value;
		 map.endDate=$("endDate").value; 
		 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
 		 map.dateType="0";//天
 		 map.channelTypeCode = $("channelTypeCode").value;
 		 
 	     dhx.showProgress("正在执行......");
	     NewTwoChannelAction.queryChannelCustView_pg(map, function (res) {
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
	 if(preferType=="1"){
			var tableHead="<tr>" 
				+"<td bgcolor='#cde5fd' ><span class='title'>大区</span></td>"
				+"<td bgcolor='#cde5fd' ><span class='title'>区域</span></td>"
				+"<td bgcolor='#cde5fd'  ><span class='title'>客户等级</span></td>"
				+"<td bgcolor='#cde5fd' ><span class='title' align='center' >查询</span></td>" 
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>办理</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>充缴</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>咨询</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>投诉</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>修障</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>其它</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>总服务量</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>占比(%)</span></td>"
			 	+"</tr>";
		}
		if(preferType=="2"){
			var tableHead="<tr>" 
				+"<td bgcolor='#cde5fd' ><span class='title'>大区</span></td>"
				+"<td bgcolor='#cde5fd' ><span class='title'>区域</span></td>"
				+"<td bgcolor='#cde5fd'  ><span class='title'>客户分群</span></td>"
				+"<td bgcolor='#cde5fd' ><span class='title' align='center' >查询</span></td>" 
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>办理</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>充缴</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>咨询</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>投诉</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>修障</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>其它</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>总服务量</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>占比(%)</span></td>"
			 	+"</tr>";
			
		}		
		if(preferType=="3"){
			var tableHead="<tr>" 
				+"<td bgcolor='#cde5fd' ><span class='title'>大区</span></td>"
				+"<td bgcolor='#cde5fd' ><span class='title'>区域</span></td>"
				+"<td bgcolor='#cde5fd'  ><span class='title'>品牌类型</span></td>"
				+"<td bgcolor='#cde5fd' ><span class='title' align='center' >查询</span></td>" 
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>办理</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>充缴</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>咨询</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>投诉</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>修障</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>其它</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>总服务量</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>占比(%)</span></td>"
			 	+"</tr>";
			
		}	
		if(preferType=="4"){
			 var tableHead="<tr>" 
			 		+"<td bgcolor='#cde5fd' ><span class='title'>大区</span></td>"
					+"<td bgcolor='#cde5fd' ><span class='title'>区域</span></td>"
					+"<td bgcolor='#cde5fd'  ><span class='title'>ARPU分级</span></td>"
					+"<td bgcolor='#cde5fd' ><span class='title' align='center' >查询</span></td>" 
				    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>办理</span></td>"
				    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>充缴</span></td>"
				    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>咨询</span></td>"
				    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>投诉</span></td>"
				    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>修障</span></td>"
				    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>其它</span></td>"
				    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>总服务量</span></td>"
				    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>占比(%)</span></td>"
				 	+"</tr>";
		}
		if(preferType=="5"){
		 	var tableHead="<tr>" 
		 	+"<td bgcolor='#cde5fd' ><span class='title'>大区</span></td>"
				 +"<td bgcolor='#cde5fd' ><span class='title'>区域</span></td>"
				 +"<td bgcolor='#cde5fd'  ><span class='title'>在网时长</span></td>"
				+"<td bgcolor='#cde5fd' ><span class='title' align='center' >查询</span></td>" 
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>办理</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>充缴</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>咨询</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>投诉</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>修障</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>其它</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>总服务量</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>占比(%)</span></td>"
			 	+"</tr>";
			
		}
		if(preferType=="6"){
			
		 var tableHead="<tr>" 
		 	+"<td bgcolor='#cde5fd' ><span class='title'>大区</span></td>"
				 +"<td bgcolor='#cde5fd' ><span class='title'>区域</span></td>"
				 +"<td bgcolor='#cde5fd'  ><span class='title'>付费方式</span></td>"
				+"<td bgcolor='#cde5fd' ><span class='title' align='center' >查询</span></td>" 
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>办理</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>充缴</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>咨询</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>投诉</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>修障</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>其它</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>总服务量</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>占比(%)</span></td>"
			 	+"</tr>";
		}
		if(preferType=="7"){
		 var tableHead="<tr>"
		 		+"<td bgcolor='#cde5fd' ><span class='title'>大区</span></td>"
				  +"<td bgcolor='#cde5fd' ><span class='title'>区域</span></td>"
				+"<td bgcolor='#cde5fd'  ><span class='title'>产品类型</span></td>"
				+"<td bgcolor='#cde5fd' ><span class='title' align='center' >查询</span></td>" 
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>办理</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>充缴</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>咨询</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>投诉</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>修障</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>其它</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>总服务量</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>占比(%)</span></td>"
			 	+"</tr>";
		}
		if(preferType=="8"){
		 var tableHead="<tr>"
		 		+"<td bgcolor='#cde5fd' ><span class='title'>大区</span></td>"
				  +"<td bgcolor='#cde5fd' ><span class='title'>区域</span></td>"
				+"<td bgcolor='#cde5fd'  ><span class='title'>六大市场类型</span></td>"
				+"<td bgcolor='#cde5fd' ><span class='title' align='center' >查询</span></td>" 
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>办理</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>充缴</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>咨询</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>投诉</span></td>"
			    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>修障</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>其它</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>总服务量</span></td>"
			    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>占比(%)</span></td>"
			 	+"</tr>";
		}
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
			var channelType =$("channelType").value;
		    var  startDate=$("startDate").value;//时间
			var  endDate=$("endDate").value;//时间
			var  zone=$("zone").value;//区域
		    var queryCond="日期从："+startDate+"    至："+endDate+"    区域："+zone+"   渠道类型:"+channelType; 
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
	 map.rptId="1001";
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