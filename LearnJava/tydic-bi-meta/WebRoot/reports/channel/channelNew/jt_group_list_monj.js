/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
var globZoneTree = null;
var globServTree=null;
var page=null;


var indexInit=function(){
	 page=Page.getInstance();
	 page.init();
	 
     //执行查询数据
     excuteInitData();
}
var excuteInitData=function(){
     var map=new Object();
     map.month=$("dateTime").value;

     map.pageCount=  page.pageCount;    //每页显示多少条数
     map.currPageNum=page.currPageNum;//当前第几页
     
     dhx.showProgress("正在执行......");
     NewTwoChannelAction.getGroupChannelList_Pg(map, function (res) {
    	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("查询失败,请稍后重试!");
				            return;
				        }
					 buildTable(res);
    });
}
function buildTable(data){
	     tableStr ="<table  width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
           tableStr+=tableHeader(data.headColumn);
		   tableStr+=tableData(data.dataColumn,data.headColumn);
        tableStr +="</table>"; 
         $('dataTable').innerHTML=tableStr;
         
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
	    	   var tempColumn=headColumn[i];    
              if(tempColumn.indexOf('_')==-1){
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
	    var  excelData="";
	    var  tableData="";
	      if(dataColumn&&dataColumn.length>0)
		    {  
		       for(var i=0;i<dataColumn.length;i++)
		       {
		    	   
		    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
			    		     for(var j=0;j<headColumn.length;j++)
			    		      {
			    		    	 var tempColumn=headColumn[j];    
			   		               if(tempColumn.indexOf('_')==-1){
					    		       tableData +="<td nowrap align='left'>"+dataColumn[i][tempColumn]+"</td>";
					    		       excelData += dataColumn[i][tempColumn]+"}";
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
	page.resetPage();
	excuteInitData();
}
//比较两个日期
var isEffectDate=function()
{
	var startTime=$("startTime").value;
	startTime=startTime.substr(5,2);
	var endTime=  $("endTime").value;
	endTime=endTime.substr(5,2);
	if(startTime==endTime){
	   return true;
	}else{
	   return false;
	}
}
dhx.ready(indexInit);