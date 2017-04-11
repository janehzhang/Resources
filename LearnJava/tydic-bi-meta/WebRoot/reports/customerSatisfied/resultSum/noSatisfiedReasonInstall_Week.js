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


var userInit=function(){
    excuteInitData();
}

var excuteInitData=function(){
    var map=new Object();
    map.startTime   =$("dateTime").value;
    map.dateType   ="1";//周报
    map.actType   =$("vTypeId").value;//触点
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getNoSatisfyInstall(map, function (res) {
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
					 buildTable(res);
   });	
}
function buildTable(res){
	 var vTypeId   =$("vTypeId").value;//触点
	 var tempName='';
	 if(vTypeId=='11'){
		 tempName='装移';
	 }else{
		 tempName='修障';
	 }
	 var  excelData="";
     var tableStr ="<table  width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
     tableStr +="<tr>";
     tableStr +="<td nowrap colspan='2' bgcolor='#cde5fd'><span class='title'>类型</span></td>"; 
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>深圳</span></td>"; 
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>广州</span></td>"; 
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>东莞</span></td>"; 
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>佛山</span></td>"; 
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>中山</span></td>"; 
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>惠州</span></td>"; 
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>江门</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>珠海</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>汕头</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>揭阳</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>潮州</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>汕尾</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>湛江</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>茂名</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>阳江</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>云浮</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>肇庆</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>清远</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>梅州</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>河源</span></td>";
     tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>韶关</span></td>";
     tableStr +="</tr>";
if(res&&res.dataColumn.length>0)
{
	tableStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	tableStr=tableStr+"<td width='4%' rowspan='2' bgcolor='#cde5fd'><span class='title'>"+tempName+"</span></td>";
	excelData=excelData+tempName+"}";
	tableStr=tableStr+"<td width='4%' bgcolor='#cde5fd'><span class='title'>装维人员</span></td>";
	excelData=excelData+"装维人员"+"}"
	for(var i=0;i<21;i++)
 {
	tableStr=tableStr+"<td width='4%'>"+res.dataColumn[i].INSTALL_NAME+"</td>";
	excelData=excelData+res.dataColumn[i].INSTALL_NAME+"}"
 }
	tableStr +="</tr>";
	excelData +="]";
	tableStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	excelData=excelData+tempName+"}";
	tableStr=tableStr+"<td width='4%' bgcolor='#cde5fd'><span class='title'>不满意数量</span></td>";
	excelData=excelData+"不满意数量"+"}"
	for(var i=0;i<21;i++)
 {
	tableStr=tableStr
	+"<td width='4%'>"+res.dataColumn[i].NO_SATISFY_NUM+"</td>";
	excelData=excelData
	+res.dataColumn[i].NO_SATISFY_NUM+"}"
 }
	tableStr +="</tr>";
	excelData +="]";
}
else
{
   tableStr +="<tr>"
	        +"<td colspan='100'>没有数据显示</td>"
            +"</tr>";
} 
 tableStr +="</table>";
 $('chartTable').innerHTML=tableStr;
 $("excelData").value= excelData;//Excel数据
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