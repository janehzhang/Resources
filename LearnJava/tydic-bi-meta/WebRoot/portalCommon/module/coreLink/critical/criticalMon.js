/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
var globZoneTree = null;
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
	//1.加载地域树 
     loadZoneTreeChkBox(userInfo['localCode'],queryform);
     
       //执行查询数据
       excuteInitData();
}
var excuteInitData=function(){
     var map=new Object();
     map.zoneCode   =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
     map.dateTime   =$("dateTime").value;
     CriticalMonAction.getTableData(map, {callback:function (res) {
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
					 buildTable(res);
				        
		   }
		});

}
function buildTable(data){
	  var tableStr="<table width='800' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
		  +"<tr>"
		    +"<td bgcolor='#cde5fd'>地域</td>"
		    +"<td bgcolor='#cde5fd'>临界值投诉量</td>"
		    +"<td bgcolor='#cde5fd'>总投诉量</td>"
		    +"<td bgcolor='#cde5fd'>计费用户数</td>"
		    +"<td bgcolor='#cde5fd'>临界值投诉率</td>"
		 +"</tr>";
	                if(data&&data.length>0){  
						for(var i=0;i<data.length;i++){
						       tableStr =tableStr+
						           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
								     +"<td align=\"left\">"+data[i].ZONE_NAME+"</td>"
								     +"<td>"+data[i].CRITICAL_NUM+"</td>"
								     +"<td>"+data[i].TOTAL_CMPL_NUM+"</td>"
								     +"<td>"+data[i].NUM1+"</td>"
								     +"<td>"+data[i].CRITICAL_PER+"‰"+"</td>"
								   +"</tr>";
						       }
				   }else{
							     tableStr =tableStr+
							      "<tr>"
							       +"<td colspan='10'>没有数据显示</td>"
							     +"</tr>";
						 } 



    tableStr +="</table>";
    $('chartTable').innerHTML=tableStr;
}
var queryData=function(){
	 //加载表格
     excuteInitData();
}
dhx.ready(indexInit);