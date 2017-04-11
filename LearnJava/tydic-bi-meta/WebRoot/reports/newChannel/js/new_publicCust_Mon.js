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
 
var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
 
if(user['zoneId']!='1') { //不是广东省  （钻取权限控制）
	var array=document.getElementsByName('city');
	    for(var i=0;i<array.length;i++){ 
	      array[i].style.display = "none";
	}
}
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPath1");
var zoneConverter=dhx.extend({idColumn:"zoneCode",pidColumn:"zoneParCode",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
dwrCaller.addAction("querySubZoneCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZone1(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
var indexInit=function(){
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     //执行查询数据
       excuteInitData();
      indexExp();
}


var excuteInitData=function(){

	 var map=new Object();
     map.dateTime     =$("dateTime").value;
     map.zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
     map.zqType = $("zqType").value;
     map.numberType = $("numberType").value;
     zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
    
     dhx.showProgress("正在执行......");
     NewChannelAction.getZqData(map, function (res) {
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				            return;
 				        }
 		 buildTable(res);	
    });	
  	
}
function buildTable(data){
    tableStr ="<table  id='tab1' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    //autoRowSpan($("tab1"),0,2);
    autoRowSpan($("tab1"),0,1);
    autoRowSpan($("tab1"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
 
var  excelHeader="";
var  tableHead="";
var zqType=$("zqType").value;
var numberType = $("numberType").value;
	tableHead="<tr>"
	    +"<td nowrap bgcolor='#cde5fd' style='width:60px' rowspan='2'><span class='title'>一级分类</span></td>"
		+"<td nowrap bgcolor='#cde5fd' style='width:60px' rowspan='2'><span class='title'>二级分类</span></td>"
		+"<td nowrap bgcolor='#cde5fd' style='width:60px' rowspan='2'><span class='title'>三级分类</span></td>"
 		+"<td nowrap bgcolor='#cde5fd'  style='width:60px'  rowspan='2'><span class='title'>广东省</span></td>"
		+"<td nowrap bgcolor='#cde5fd'  style='width:100px' colspan='4'><span class='title'>珠三角1</span></td>"
		+"<td nowrap bgcolor='#cde5fd'  style='width:100px' colspan='4'><span class='title'>珠三角2</span></td>"
		+"<td nowrap bgcolor='#cde5fd'  style='width:100px' colspan='4'><span class='title'>粤东</span></td>"
		+"<td nowrap bgcolor='#cde5fd'  style='width:100px' colspan='4'><span class='title'>粤西</span></td>"
		+"<td nowrap bgcolor='#cde5fd'  style='width:100px' colspan='5'><span class='title'>粤北</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>深圳</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>广州</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>东莞</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>佛山</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>中山</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>惠州</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>江门</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>珠海</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>汕头</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>揭阳</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>潮州</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>汕尾</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>湛江</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>茂名</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>阳江</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>云浮</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>肇庆</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>梅州</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>清远</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>河源</span></td>"
		+"<td bgcolor='#cde5fd' style='width:50px'><span class='title'>韶关</span></td>"
    tableHead+="</tr>";
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
					    		       tableData +="<td>"+dataColumn[i][tempColumn]+"</td>";	 
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
	excuteInitData();
}
//指标解释
function indexExp(){
     var numberType1 = $("numberType").value;
	 $('index_exp').innerHTML='';
	 var explainData="";
	 var rptId="";
	 var indexName =$("zqType").value;
	 var map=new Object(); 
	 if(indexName=="cxhw"){ //查询话务
	 	if(numberType1=="01"){
	 		rptId="49";
	 	}if(numberType1=="02"){
	 		rptId="50";
	 	}if(numberType1=="03"){
	 		 rptId="51";
	 	}if(numberType1=="04"){
	 		rptId="52";
	 	}
	 }if(indexName=="zxhw"){//咨询话务
		 if(numberType1=="01"){
		 		rptId="45";
		 	}if(numberType1=="02"){
		 		rptId="46";
		 	}if(numberType1=="03"){
		 		 rptId="47";
		 	}if(numberType1=="04"){
		 		rptId="48";
		 	}
	 }if(indexName=="blhw"){//办理话务
	 	if(numberType1=="01"){
		 	 rptId="53";
		 }if(numberType1=="02"){
		 	 rptId="54";
		 }if(numberType1=="03"){
		 	 rptId="55";
		 }if(numberType1=="04"){
		 	 rptId="56";
		 }
	 }if(indexName=="bzhw"){//保障话务
	 	if(numberType1=="01"){
		 	 rptId="57";
		 }if(numberType1=="02"){
		 	 rptId="58";
		 }if(numberType1=="03"){
		 	 rptId="59";
		 }if(numberType1=="04"){
		 	 rptId="60";
		 }	 
	 }if(indexName=="tshw"){//投诉话务
	 	if(numberType1=="01"){
		 	 rptId="61";
		 }if(numberType1=="02"){
		 	 rptId="62";
		 }if(numberType1=="03"){
		 	 rptId="63";
		 }if(numberType1=="04"){
		 	 rptId="64";
		 }	 
	 }if(indexName=="gzxd"){//故障下单
	 	if(numberType1=="01"){
		 	 rptId="69";
		 }if(numberType1=="02"){
		 	 rptId="70";
		 }if(numberType1=="03"){
		 	 rptId="71";
		 }if(numberType1=="04"){
		 	 rptId="72";
		 }	 
	 }if(indexName=="gzlTsxd"){//工作流投诉下单
	 	if(numberType1=="01"){
		 	 rptId="69";
		 }if(numberType1=="02"){
		 	 rptId="70";
		 }if(numberType1=="03"){
		 	 rptId="71";
		 }if(numberType1=="04"){
		 	 rptId="72";
		 }	 
	 }if(indexName=="gzlXqxd"){//工作流需求下单
	 	if(numberType1=="01"){
		 	 rptId="73";
		 }if(numberType1=="02"){
		 	 rptId="74";
		 }if(numberType1=="03"){
		 	 rptId="75";
		 }if(numberType1=="04"){
		 	 rptId="76";
		 }	 
	 }
	 map.rptId=rptId;
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

//第一列合并
var autoRowSpan=function(tb,row,col){
    var lastValue="";
    var value="";
    var pos=1;
    for(var i=row;i<tb.rows.length;i++)
    {
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
dhx.ready(indexInit);