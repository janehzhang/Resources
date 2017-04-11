/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
var globZoneTree = null;
var page=null;

/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCode");
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
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
/** Tree head **/

var indexInit=function(){
	 page=Page.getInstance();
	 page.init();
	 
	 //1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     //执行查询数据
     excuteInitData();
}
var excuteInitData=function(){
     var map=new Object();
     map.zoneCode    =$("zoneCode").value==""?zoneCode:$("zoneCode").value;
     map.startTime   =$("startTime").value;
     map.endTime     =$("endTime").value;
    // map.actType     =$("actType").value;
     map.indexType   =$("indexType").value;
    // map.reasonId    =$("reasonId").value;
     map.satisType   =$("satisType").value;
     map.notSatisType2=$("notSatisType2").value;
     map.notSatisType1=$("notSatisType1").value;
    map.indexType   =$("indexType").value;
    map.visitChannel   =$("visitChannel").value;
    map.ripId   =$("ripId").value;
     map.pageCount=  page.pageCount;    //每页显示多少条数
     map.currPageNum=page.currPageNum;//当前第几页
     
     dhx.showProgress("正在执行......");
     ADSLVisitListAction.getTableDataIVRZYJ(map, function (res) {
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
			    		         if(tempColumn.indexOf('_')==-1)
				    		       {
			    		               if(tempColumn == '回访结果')
			    		               {
			    		            	 if(dataColumn[i].满意度 == "不满意" || dataColumn[i].满意度 == "未按时上门" || dataColumn[i].满意度 == "未按时开通"  || dataColumn[i].满意度 == "服务态度不好"|| dataColumn[i].满意度 == "服务人员技术水平差"|| dataColumn[i].满意度 == "其他原因")//不满意, 非常不满意 ,其它
			    		            	  {
			    		            		 
			    		                    tableData +="<td><span style='float: left;'>"+dataColumn[i][tempColumn]+"</span>";
			    		                      tableData +="<img onclick=\"fillVisitResult('"+dataColumn[i].主键_ID+"','"+dataColumn[i].统计_时间+"')\" border='0' style='cursor:pointer;float:right;' src='../../../../images/btn-edit.gif' title='点击可编辑'/>";
			    		            	   tableData +="</td>"; 
			    		            	  }
			    		            	 else
			    		            	  {
			    		            		tableData +="<td nowrap align='left'>"+dataColumn[i][tempColumn]+"</td>";   
			    		            	  }
			    		               }else
			    		               {
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

//查询
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
var fillVisitResult=function(id , date)
{
	//alert(date.value)
    var el = event.srcElement.previousSibling;
	var width =300;
	var height=300;
    var top  =  el.offsetTop+height;
    var left = el.offsetWidth+el.clientWidth+el.offsetLeft+width;
    var url=base+"/portalCommon/module/procedure/visitDetail/openVisitResultIVR.jsp?id="+id+"&date="+date+"&name="+el.innerHTML;
    var param="dialogWidth:"+width+"px;dialogHeight:"+height+"px;dialogLeft:"+left+"px;dialogTop:"+top+"px;center:yes;help:no;resizable:no;status:no;scroll:no";
    var retVal = window.showModalDialog(url,window,param);
  if(retVal != undefined)
    {
       el.innerHTML=retVal;
    }
	 
}


/*
 * 下载文件
*/
function loadOperMan(){
	
	var file= "meta/public/upload/serviceProblem/客服分析系统与外呼平台对数操作手册.doc"
	document.forms[0].target="hiddenFrame";
 	var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ file;
	window.open(url,"hiddenFrame","");
	
} 

dhx.ready(indexInit);