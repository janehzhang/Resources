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
      excuteInitData();   
}


var excuteInitData=function(){
		 var map=new Object();
		 map.tab5 = $("tab5").value;
 		 map.tab6 = $("tab6").value;
 		 map.tab7 = $("tab7").value;
 	     dhx.showProgress("正在执行......");
	     NewTwoChannelAction.systemInterface(map, function (res) {
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
    
    
}
/**
*  构造表头
* @param {Object} dataqueryData
*/
function tableHeader(headColumn){
	var tableHead="<tr>"
		+"<td bgcolor='#cde5fd' ><span class='title'>接口协议</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title'>接口名称</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center' >接口表</span></td>" 
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>源数据表</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>接口方式</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>上天数据</span></td>" 
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>当天数据</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'> 最新时间</span></td>"
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
   		            	if(dataColumn[i]['MARK_1']=='0'){
   		            		if(tempColumn=='TIME'){
   		            			tableData +="<td style='color:red'>"+dataColumn[i]['TIME']+"</td>"; 
   	 		            	    excelData += dataColumn[i][tempColumn]+"}";
   		            		}
   		            		else{
   		            			tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
   	   	 		            	excelData += dataColumn[i][tempColumn]+"}";
   		            		}
   		            	}
   		            	else{
   		            		tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
   	 		            	excelData += dataColumn[i][tempColumn]+"}";
   		            	}
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

function exportExcel(){
    var  tab=$("tab").value;//表名
 	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implChannelMoreExcel.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
 
dhx.ready(indexInit);