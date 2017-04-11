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
var zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;

//查询条件参数
//查询系统参数
var page=null;

var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCodeGrid");
var zoneConverter=dhx.extend({idColumn:"zoneCode",pidColumn:"zoneParCode",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
dwrCaller.addAction("querySubZoneCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZoneCodeGrid(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
var indexInit=function(){
	 page=Page.getInstance();
	 page.init();
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     //执行查询数据
     excuteInitData();
}


var excuteInitData=function(page_index){
    var map=new Object();
    map.zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
    map.startDate   =$("startDate").value;
    map.endDate     =$("endDate").value;
    map.prodId     =$("prodId").value==""?'':$("prodId").value;
    map.pageCount=  page.pageCount;    //每页显示多少条数
    map.currPageNum=page.currPageNum;//当前第几页
    
    dhx.showProgress("正在执行......");
    ServiceReActiveAction.getServiceReActiveDetail_Pg(map, function (res) {
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
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
    $('chartTable').innerHTML=tableStr;
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
       if(headColumn[i].indexOf('_ID')==-1)//字段 有 "-"  表示不展示
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
			    		       if(tempColumn.indexOf('_ID')==-1)
			    		       {
				    		       if(dataColumn[i][tempColumn].length>50){
				    		    	   tableData +="<td nowrap align='left' title='"+dataColumn[i][tempColumn]+"'>"+dataColumn[i][tempColumn].substring(0,50)+"</td>";
				    		       }else{
				    		           tableData +="<td nowrap align='left'>"+dataColumn[i][tempColumn]+"</td>";
				    		       }
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

// 查询
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
dhx.ready(indexInit);