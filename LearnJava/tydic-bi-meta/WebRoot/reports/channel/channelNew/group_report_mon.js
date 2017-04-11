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
 
 
var indexInit=function(){	
	
    //执行查询数据
    excuteInitData();
      
}

var excuteInitData=function(){
		 	//indexExp();
		 var map=new Object();
		 map.month=$("dateTime").value;
 	     dhx.showProgress("正在执行......");
	     NewTwoChannelAction.getGroupChannelReport_Pg(map, function (res) {
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
    autoRowSpan($("tab1"),1,1); 
    autoRowSpan($("tab1"),1,0); 
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
  var tableHead="<tr>"
		+"<td bgcolor='#cde5fd' colspan='25' style='width:4%'><span class='title' align='center' >中国电信各渠道服务量统计模板</span></td>"
		+"</tr>"
		+"<tr>"
	    +"<td bgcolor='#cde5fd' rowspan='2' ><span class='title'>一级指标</span></td>"
	    +"<td bgcolor='#cde5fd' rowspan='2' ><span class='title' align='center' >二级指标</span></td>" 
	    +"<td bgcolor='#cde5fd' rowspan='2' ><span class='title' align='center'>三级指标</span></td>"
	    +"<td bgcolor='#cde5fd' rowspan='2' ><span class='title' align='center'>单位</span></td>"
	    +"<td bgcolor='#cde5fd' colspan='13' ><span class='title' align='center'>线上渠道</span></td>"
	    +"<td bgcolor='#cde5fd' colspan='8' ><span class='title' align='center'>线下渠道</span></td>"
	    +"</tr>"
	    +"<tr>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>网厅</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>欢GO</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>翼支付</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>10000/10001自助语音</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>短厅</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>易信公众号</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>新媒体客服</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>电商渠道</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>积分商城</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>自助终端</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>微信公众号</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>IM客服自助</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>小计</span></td>" 
	    
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>自有厅</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>专营店</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>开放渠道</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>直销渠道</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>10000人工</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>IM人工</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>VIP客户服务经理</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>小计</span></td>" 
	    
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
 
//查询
var queryData=function(){
	 
	excuteInitData();
	
}

//列合并
var autoRowSpan=function(tb,row,col){

    var lastValue="";
    var value="";
    var pos=1;
    for(var i=row;i<tb.rows.length;i++){
        value = tb.rows[i].cells[col].innerText;
        
        if(lastValue == value) {
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
		    var queryCond="月份："+dateTime+"" ; 
		    var excelUrl="";
		   	    $("excelCondition").value=queryCond; 
			    excelUrl="/portalCommon/module/procedure/impExcel/implAaeaChannelMoreKeyExcel2.jsp";
			var url = getBasePath()+excelUrl;
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
	   map.rptId="1025";
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