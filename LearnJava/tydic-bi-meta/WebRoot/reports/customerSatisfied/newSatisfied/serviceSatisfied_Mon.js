/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();
var globZoneTree = null;

var titileInfo=$("selectCol").options[$("selectCol").selectedIndex].text+"->本月值";
$("titleInfo1").innerHTML=titileInfo;
$("titleInfo2").innerHTML=titileInfo;

//当前系统的主页Path
var base = getBasePath();
//查询条件参数
var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
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
//by qx
tZoneCode=$("zoneCode").value;
tDefaultZoneCode=$("defaultZoneCode").value;
if(tZoneCode!=tDefaultZoneCode){//下钻
	var array=document.getElementsByName('city');
    for(var i=0;i<array.length;i++){ 
      array[i].style.display = "none";
    }
    $("changeCode").value='1';
    $("defaultCode").value='1';
}else{
	if(user['zoneId']!='1') { //地市用户
		$("changeCode").value='1';
		$("defaultCode").value='1';
	}if(user['zoneId']=='1') { //广东省用户  
		$("changeCode").value='2';
		$("defaultCode").value='2';
	}
}
var changeCode = $("changeCode").value;
var defaultCode=$("defaultCode").value;
var userInit=function(){
	loadIndexCdWarn(menuName);
	//1.加载地域树 
    loadZoneTreeChkBox(zoneCode,queryform);
    //执行查询数据
    excuteInitData(changeCode);
}

var excuteInitData=function(changeCode){
    var map=new Object();
    map.dateTime   =$("dateTime").value;
    map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
    map.dateType   ="2";//月报
    map.actType   =$("vTypeId").value;//各个触点
    map.field=$("selectCol").value;
    map.changeCode=changeCode;
    dhx.showProgress("正在执行......");
    CustomerSatisfiedAction.getCustomerSatisfied_everyTypeMod(map, function (res) {
    	$('chartdiv1').innerHTML='';
    	$('chartdiv2').innerHTML='';
   	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				     chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "100%", "200", "0", "0");
			    	 chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+Math.random(), "100%", "200", "0", "0");
			         chart1.setDataXML(res.barChartMap);
			         chart2.setDataXML(res.lineChartMap);
			         chart1.render("chartdiv1");
			         chart2.render("chartdiv2"); 
			         $("div_src").style.display = "none";
			         $("div_src").style.zindex = "-1";
					 buildTable(res);
					 //$("city").value='切换地市'
   });	
}
function buildTable(data){
    tableStr ="<table  id='tab1' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
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
function tableHeader(headColumn){
	var tempId=$("vTypeId").value;
	var tableHead="";
	var  excelHeader="";
	if(tempId=='0'){
		tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>区域</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>总体评价</span></td>"
		    +"<td nowrap bgcolor='#cde5fd'  colspan='7' style='width:350px'><span class='title'>不满意原因</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评满意数</span></td>"
			+"<td nowrap bgcolor='#cde5fd' style='width:200px' colspan='4'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>报装不方便</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>长时间无人联系</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>未准时按约上门</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>服务态度不好</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>多次上门未装好</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>施工不规范</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>其他</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>";
	}if(tempId=='1'){
		tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>区域</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>总体评价</span></td>"
		    +"<td nowrap bgcolor='#cde5fd'  colspan='7' style='width:350px'><span class='title'>不满意原因</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评满意数</span></td>"
			+"<td nowrap bgcolor='#cde5fd' style='width:200px' colspan='4'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>报修不方便</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>长时间无人联系</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>未准时按约上门</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>服务态度不好</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>多次上门未修好</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>施工不规范</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>其他</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>";
	}if(tempId=='2'){
		tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>区域</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td nowrap bgcolor='#cde5fd' colspan='3' style='width:150px'><span class='title'>总体评价</span></td>"
		    +"<td nowrap bgcolor='#cde5fd'  colspan='6' style='width:300px'><span class='title'>不满意原因</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评满意数</span></td>"
			+"<td nowrap bgcolor='#cde5fd' style='width:200px' colspan='4'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>服务态度</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>业务水平</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>等候时间</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>办事效率</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>营业环境</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>其他</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>";
	}if(tempId=='5'){//10000号
		tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>区域</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td nowrap bgcolor='#cde5fd' style='width:150px' colspan='3'><span class='title'>总体评价</span></td>"
		    +"<td nowrap bgcolor='#cde5fd' style='width:100px' colspan='2'><span class='title'>不满意原因</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评满意数</span></td>"
			+"<td nowrap bgcolor='#cde5fd' style='width:200px' colspan='4'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>对客服代表</span></td>"			
			+"<td bgcolor='#cde5fd'><span class='title'>对其他</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>";
	}if(tempId=='6'){//网厅
		tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>区域</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td nowrap bgcolor='#cde5fd' style='width:150px' colspan='3'><span class='title'>总体评价</span></td>"
		    +"<td nowrap bgcolor='#cde5fd' style='width:350px' colspan='7'><span class='title'>不满意原因</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评满意数</span></td>"
			+"<td nowrap bgcolor='#cde5fd' style='width:200px' colspan='4'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>操作不方便</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>响应速度慢</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>系统不稳定</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>办理失败</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>界面不美观</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>提示不清晰</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>其他</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>";
	}if(tempId=='7'){//掌厅
		tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>区域</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:150px' colspan='3'><span class='title'>总体评价</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:350px' colspan='7'><span class='title'>不满意原因</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评满意数</span></td>"
			+"<td nowrap bgcolor='#cde5fd' style='width:200px' colspan='4'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>操作不方便</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>响应速度慢</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>系统不稳定</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>办理失败</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>界面不美观</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>提示不清晰</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>其他</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>";
	}if(tempId=='8'){//号百
		tableHead="<tr>"
			+"<td nowrap bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>区域</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>邀请客户数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评有效样本数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评参与率</span></td>"
			+"<td bgcolor='#cde5fd' style='width:150px' colspan='3'><span class='title'>总体评价</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:100px' colspan='2'><span class='title'>不满意原因</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>测评满意数</span></td>"
			+"<td nowrap bgcolor='#cde5fd' style='width:200px' colspan='4'><span class='title'>测评满意率</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及数</span></td>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:50px'><span class='title'>不满意原因提及率</span></td>"
			+"</tr>"
			+"<tr>"
			+"<td bgcolor='#cde5fd'><span class='title'>非常满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>不满意</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>对客服代表</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>对其他</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>当期</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
			+"<td bgcolor='#cde5fd'><span class='title'>平均</span></td>";
	}
    
    tableHead+="</tr>";
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
	       for(var i=0;i<dataColumn.length;i++)
	       {
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		    		     for(var j=0;j<headColumn.length;j++)
		    		      {
		    		               var tempColumn=headColumn[j]; 
				    		       
		    		               if(tempColumn.indexOf('_')==-1)
				    		       {
				    		              tableData +="<td nowrap align='center'>"+addWaringStyle(dataColumn[i][tempColumn], null ,tempColumn)+"</td>";   
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
	defaultCode=$("defaultCode").value; //by qx
	tZoneCode=$("zoneCode").value;
	tDefaultZoneCode=$("defaultZoneCode").value;
	if(tZoneCode!=tDefaultZoneCode){
		var array=document.getElementsByName('city');
	    for(var i=0;i<array.length;i++){ 
	      array[i].style.display = "none";
	    }
		excuteInitData(1);
		 $("changeCode").value="1";
	}else{
		var array=document.getElementsByName('city');
	    for(var i=0;i<array.length;i++){ 
	      array[i].style.display = "";
	    }
	    $("city").value='横向对比'
	    $("changeCode").value=defaultCode;
	   excuteInitData(defaultCode);
	}
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
var lookCity = function (obj){
    var map=new Object();
        map.dateTime=$("dateTime").value;
        map.field=$("selectCol").value;
        map.dateType   ="2";//日报
        map.actType   =$("vTypeId").value;//各个触点值
        map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value; 
        tZoneCode=$("zoneCode").value;
    	tDefaultZoneCode=$("defaultZoneCode").value;
    	if(tZoneCode!=tDefaultZoneCode){
    		var array=document.getElementsByName('city');
    	    for(var i=0;i<array.length;i++){ 
    	      array[i].style.display = "none";
    	    }
    	    $("changeCode").value='1';
    	    return;
    	}else{
    		if(user['zoneId']!='1') { 
			  if(obj.value=='横向对比'){//地市
				  obj.value='返回';
				  map.changeCode="0";//地市同级
				  $("changeCode").value='0';
			  }else if(obj.value=='返回'){//地市
				  obj.value='横向对比';
				  map.changeCode="1";//地市下一级
				  $("changeCode").value='1';
			  }
    		}else{
    		 if(obj.value=='横向对比'){//省公司下一级
  				  obj.value='返回';
  				  map.changeCode="1";//省公司下一级
  				$("changeCode").value='1';
  			  }else if(obj.value=='返回'){//省公司下两级
  				  obj.value='横向对比';
  				  map.changeCode="2";//省公司下两级
  				$("changeCode").value='2';
  			  }
    		}
    	}
		  dhx.showProgress("正在执行......");    
		  CustomerSatisfiedAction.loadSet21AreaChartMod(map, {callback:function (res) {
			  dhx.closeProgress();
 	         if(res != null){
                     build21Chart(res);
		         }
		   }
		});
}
function  build21Chart(map){
	   $("chartdiv1").innerHTML="";
	   chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId3_"+Math.random(), "100%", "200", "0", "0");
	   chart.setDataXML(map.XML);
	   chart.render("chartdiv1");
	}
/**
 *  打开链接菜单
 * 
 */
var  lookMenuHref=function(zoneName,zoneCode){
	   var dateTime=$("dateTime").value;
	   var url="/reports/customerSatisfied/cmplDeal/noSatisfiedReason_Day.jsp?zoneCode="+zoneCode+"&dateTime="+dateTime;
	   return parent.openTreeTab("124014"+zoneCode, "投诉处理不满意原因TOP日报"+"["+zoneName+"]", base+url, 'top');
}
//钻取
var drillArea=function(zoneName,zoneCode)
{	
	var dateTime=$("dateTime").value;
	var selectCol=$("selectCol").value;
	var url="/reports/customerSatisfied/cmplDeal/serviceSatisfied_Day.jsp?zoneCode="+zoneCode+"&dateTime="+dateTime+"&selectCol="+selectCol+"&menuId="+menuId;
	return parent.openTreeTab("124013"+zoneCode, "投诉处理满意率日报"+"["+zoneName+"]", base+url, 'top');
}

/**
 *  打开链接菜单
 * 
 */
var openMenuHref=function(zoneName,zoneCode,indexName,indexId){
	   var dateTime=$("dateTime").value;
	   var startTime=dateTime;
	   var endTime =dateTime;
	 var url="/portalCommon/module/procedure/visitDetail/ts/ADSL_visitList.jsp?zoneCode="+zoneCode+
	   "&startTime="+startTime+"&endTime="+endTime+"&indexId="+indexId; 
	 return parent.openTreeTab("124017"+zoneCode+indexId, "投诉处理即时回访清单"+"["+zoneName+indexName+"]", base+url, 'top');
}
//通过列改变图形
function changeCol(obj){
	$("selectCol").options[$("selectCol").selectedIndex].text;
	$("titleInfo1").innerHTML=$("selectCol").options[$("selectCol").selectedIndex].text+"->本月值";
	$("titleInfo2").innerHTML=$("selectCol").options[$("selectCol").selectedIndex].text+"->本月值";
	var map=new Object();
    map.dateTime   =$("dateTime").value;
    map.zoneCode=$('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
    map.dateType   ="2";//月报
    map.actType   =$("vTypeId").value;//各个触点
    map.field=obj.value;
    defaultCode=$("defaultCode").value; //by qx
	tZoneCode=$("zoneCode").value;
	tDefaultZoneCode=$("defaultZoneCode").value;
	if(tZoneCode!=tDefaultZoneCode){
		var array=document.getElementsByName('city');
	    for(var i=0;i<array.length;i++){ 
	      array[i].style.display = "none";
	    }
		 $("changeCode").value="1";
	}else{
		var array=document.getElementsByName('city');
	    for(var i=0;i<array.length;i++){ 
	      array[i].style.display = "";
	    }
	    $("city").value='横向对比'
	    $("changeCode").value=defaultCode;
	}
    map.changeCode=$("changeCode").value;
    buildChart(map);
}
//加载配置图形
function buildChart(map){
	dhx.showProgress("正在执行......");
	CustomerSatisfiedAction.getCustomerSatisfied_everyTypeMod(map, {callback:function (res) {
		dhx.closeProgress();
    	         if(res != null){
                        buildBLChart(res);
		         }
	}
		});
   }
//构建折线图和柱状图
function  buildBLChart(map){
	   $("chartdiv1").innerHTML="";
	   $("chartdiv2").innerHTML="";
	   //$("city").value="切换地市";
	   chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "100%", "200", "0", "0");
	   chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+Math.random(), "100%", "200", "0", "0");
	   chart1.setDataXML(map.barChartMap);
       chart2.setDataXML(map.lineChartMap);
       chart1.render("chartdiv1");
       chart2.render("chartdiv2"); 
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