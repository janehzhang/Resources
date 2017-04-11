/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();
var globZoneTree = null;
var page=null;

//当前系统的主页Path
var base = getBasePath();
//查询条件参数
var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?"0000":$("zoneCode").value;
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** Tree head **/
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
/** Tree head **/

var userInit=function(){
	page=Page.getInstance();
	page.init();
	loadIndexCdWarn(menuName);
	//1.加载地域树 
    loadZoneTreeChkBox(zoneCode,queryform);
    //执行查询数据
    excuteInitData();
}

var excuteInitData=function(){
    var map=new Object();
    map.dateTime   =$("dateTime").value;
    map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
    map.dateType   ="1";//周报
    map.actType   =$("vTypeId").value;//触点
    map.testMethod   =$("testMethod").value;
    map.pageCount=  page.pageCount;    //每页显示多少条数
    map.currPageNum=page.currPageNum;//当前第几页
    dhx.showProgress("正在执行......");

    CustomerSatisfiedAction.getServicesatisfiedResult(map, function (res) {
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
					 buildTable(res);
   });	
}
function buildTable(data){
    tableStr ="<table id='tab1' width='100%' height='250px'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
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
//var vTypeId=$("vTypeId").value;//触点
function tableHeader(headColumn){
	var excelHeader="";
	var tableHead="<tr>"
		+"<td bgcolor='#cde5fd' rowspan='3' style='width:100px'><span class='title'> 分公司</span></td>"
		//+"<td bgcolor='#cde5fd' colspan='9' style='width:450px'><span class='title'>号百即时回访满意率</span></td>"
		+"</tr>"
		+"<tr>"
		if($("vTypeId").value==2){
			tableHead+="<td bgcolor='#cde5fd' colspan='6'><span class='title'>本周</span></td>"
			+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>本月累计</span></td>"
		}
		else if($("vTypeId").value==9){
			tableHead+="<td bgcolor='#cde5fd' colspan='9'><span class='title'>本周</span></td>"
				+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>本月累计</span></td>"
		}else if($("vTypeId").value==10){
			tableHead+="<td bgcolor='#cde5fd' colspan='5'><span class='title'>本周</span></td>"
				+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>本月累计</span></td>"
		}else if($("vTypeId").value==11){
			tableHead+="<td bgcolor='#cde5fd' colspan='10'><span class='title'>本周</span></td>"
				+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>本月累计</span></td>"
		}else if($("vTypeId").value==12){
			tableHead+="<td bgcolor='#cde5fd' colspan='10'><span class='title'>本周</span></td>"
				+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>本月累计</span></td>"
		}
		else{
			tableHead+="<td bgcolor='#cde5fd' colspan='6'><span class='title'>本周</span></td>"
				+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>本月累计</span></td>"
		}
	
	tableHead+="</tr>"
		+"<tr>"
		if($("vTypeId").value==9){
			tableHead+="<td bgcolor='#cde5fd'><span class='title'>应评数</span></td>"
			//tableHead+="<td bgcolor='#cde5fd'><span class='title'>使用率</span></td>"	
		}
	     tableHead+="<td bgcolor='#cde5fd'><span class='title'>邀请数</span></td>"
	    	 if($("vTypeId").value==11){
		 			tableHead+="<td bgcolor='#cde5fd'><span class='title'>已装好</span></td>"
		 		}if($("vTypeId").value==12){
					tableHead+="<td bgcolor='#cde5fd'><span class='title'>已修复</span></td>"
				}
	    	 if($("vTypeId").value==11){
	 			tableHead+="<td bgcolor='#cde5fd'><span class='title'>未装好</span></td>"
	 		}if($("vTypeId").value==12){
				tableHead+="<td bgcolor='#cde5fd'><span class='title'>未修复</span></td>"
			}
	 		if($("vTypeId").value==11){
	 			tableHead+="<td bgcolor='#cde5fd'><span class='title'>装机成功率</span></td>"
	 		}if($("vTypeId").value==12){
				tableHead+="<td bgcolor='#cde5fd'><span class='title'>修复率</span></td>"
			}
	 		
	 		if($("vTypeId").value!=9){
	 			tableHead+="<td bgcolor='#cde5fd'><span class='title'>回复量</span></td>"
	 		}
	 		
	 			if($("vTypeId").value==9){
		 			tableHead+="<td bgcolor='#cde5fd'><span class='title'>使用率</span></td>"
		 		}	
	 		tableHead+="<td bgcolor='#cde5fd'><span class='title'>参与率</span></td>"
		if($("vTypeId").value!=10){
			 tableHead+="<td bgcolor='#cde5fd'><span class='title'>参与率变化</span></td>"
		}
	     tableHead+="<td bgcolor='#cde5fd'><span class='title'>满意率</span></td>"
	    if($("vTypeId").value!=10){	 
	    	tableHead+="<td bgcolor='#cde5fd'><span class='title'>满意率环比</span></td>"
	    }
	     if($("vTypeId").value==9){
	 			tableHead+="<td bgcolor='#cde5fd'><span class='title'>综合满意率</span></td>"
	 		}
		if($("vTypeId").value==9||$("vTypeId").value==10){
			tableHead+="<td bgcolor='#cde5fd'><span class='title'>全省排名</span></td>"
		}
	     tableHead+="<td bgcolor='#cde5fd'><span class='title'>满意率</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>片区排名</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>全省排名</span></td>";
tableHead+="</tr>";

if($("vTypeId").value==2){	
    tableHead="<tr>"
	+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>分公司</span></td>"
	+"<td nowrap bgcolor='#cde5fd' colspan='6' style='width:50%'><span class='title'>本周</span></td>"
	+"<td bgcolor='#cde5fd' colspan='4' style='width:45%'><span class='title'>本月累计</span></td>"
	+"</tr>"
	+"<tr>"
	+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>邀请量</span></td>"
	+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>回复量</span></td>"
	+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>参与率</span></td>"
	+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>参与率变化</span></td>"
	+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >满意率</span></td>"
	+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>满意率环比</span></td>"
	+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>满意率</span></td>"
	+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>环比</span></td>"
	+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>片区排名</span></td>"
	+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>全省排名</span></td>";
    tableHead+="</tr>";
    }else if($("vTypeId").value=='9'){
    	 tableHead="<tr>"
    			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>分公司</span></td>"
    			+"<td nowrap bgcolor='#cde5fd' colspan='10' style='width:60%'><span class='title'>本周</span></td>"
    			+"<td bgcolor='#cde5fd' colspan='4' style='width:35%'><span class='title'>本月累计</span></td>"
    			+"</tr>"
    			+"<tr>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>应评数</span></td>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>邀请数</span></td>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>有效数</span></td>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>使用率</span></td>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>参与率</span></td>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>参与率变化</span></td>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >满意率</span></td>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>满意率环比</span></td>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>综合满意率</span></td>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>综合满意率环比</span></td>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>综合满意率</span></td>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>综合满意率环比</span></td>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>片区排名</span></td>"
    			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>全省排名</span></td>";
    		    tableHead+="</tr>";
    }else if($("vTypeId").value=='10'){
   	 tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>分公司</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='5' style='width:50%'><span class='title'>本周</span></td>"
			+"<td bgcolor='#cde5fd' colspan='4' style='width:45%'><span class='title'>本月累计</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>邀请数</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>回复量</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>参与率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>全省排名</span></td>"
			
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >环比</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>片区排名</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>全省排名</span></td>";
		    tableHead+="</tr>";
}else if($("vTypeId").value=='11'){
  	 tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>分公司</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='10' style='width:50%'><span class='title'>本周</span></td>"
			+"<td bgcolor='#cde5fd' colspan='3' style='width:45%'><span class='title'>本月累计</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>邀请数</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>已装好</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>未装好</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>装机成功率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>有效数</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>参与率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>参与率变化</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>满意率环比</span></td>"
			
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >环比</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>全省排名</span></td>";
		    tableHead+="</tr>";
}else if($("vTypeId").value=='12'){
 	 tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>分公司</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='10' style='width:50%'><span class='title'>本周</span></td>"
			+"<td bgcolor='#cde5fd' colspan='3' style='width:45%'><span class='title'>本月累计</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>邀请数</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>已修复</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>未修复</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>修复率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>有效数</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>参与率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>参与率变化</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>满意率环比</span></td>"
			
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >环比</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>全省排名</span></td>";
		    tableHead+="</tr>";
}else if($("vTypeId").value=='18'){
	 tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:5%'><span class='title'>分公司</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='7' style='width:50%'><span class='title'>本周</span></td>"
			+"<td bgcolor='#cde5fd' colspan='4' style='width:45%'><span class='title'>本月累计</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>邀请数</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>有效数</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>参评率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>参评率变化</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >平均分</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>平均分环比</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >平均分</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title' >环比</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>满意率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>全省排名</span></td>";
		    tableHead+="</tr>";
}
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
	       for(var i=0;i<dataColumn.length;i++)//22
	       {
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		    		     for(var j=0;j<headColumn.length;j++)
		    		      {
		    		               var tempColumn=headColumn[j]; 
		    		               if(tempColumn.indexOf('_')==-1)
				    		       {  
		    		            	   if(dataColumn[i][tempColumn]=='%'){
		    		            		   tableData +="<td align='center'>-</td>"; 
					    		             excelData += "-"+"}";
		    		            	   }else{
		    		            		   tableData +="<td align='center'>"+dataColumn[i][tempColumn]+"</td>"; 
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
//第一列合并
var autoRowSpan=function(tb,row,col){//0,16
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
/**
 * 
 * @param myObject
 * @return
 */
function dump_obj(myObject) {  
   var s = "";  
   for (var property in myObject) {  
	   s = s + "\n "+property +": " + myObject[property] ;  
	  }  
	  alert(s);  
	} 
/** end **/
dhx.ready(userInit);