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
	//1.加载地域树 
    loadZoneTreeChkBox(zoneCode,queryform);
    //执行查询数据
    excuteInitData();
}

var excuteInitData=function(){
    var map=new Object();
    map.startTime   =$("dateTime").value;
    map.endTime   =$("dateTime").value;
    map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
    map.dateType   ="1";//周报
    map.actType   =$("vTypeId").value;//触点
    
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getNosatisfied_ResultSum(map, function (res) {
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
    autoRowSpan($("tab1"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
var vTypeId=$("vTypeId").value;
function tableHeader(headColumn){
	var excelHeader="";
	if("5"==vTypeId){
		var tableHead="<tr>"
		+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>不满意原因</span></td>"
		+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>上周不满意量</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>本周</span></td>"
		+"</tr>"
		+"<tr>"
		+"<td bgcolor='#cde5fd'><span class='title'>不满意量</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>占比</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>较上周</span></td>";
		tableHead+="</tr>";
		}
	else {
		var tableHead="<tr>"
		+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>不满意原因</span></td>"
		+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>上周不满意量</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>本周</span></td>"
		+"</tr>"
		+"<tr>"
		+"<td bgcolor='#cde5fd'><span class='title'>不满意量</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>占比</span></td>"
		+"<td bgcolor='#cde5fd'><span class='title'>较上周</span></td>";
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