/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller = new biDwrCaller();
var queryform = $('queryform');
var globZoneTree = null;
var user= getSessionAttribute("user");
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
var zoneInfo = top.getSessionAttribute('zoneInfo');

var indexInit = function() {
     //1.加载地域树 
     loadZoneTreeChkBox(zoneInfo.zoneCode,queryform);
	 //执行查询数据
     excuteInitData();
}
var excuteInitData=function(){
     var map=new Object();
     map.startTime   =$("startTime").value;
     map.endTime     =$("endTime").value;
     map.zoneCode    =$("zoneCode").value==""?zoneInfo.zoneCode:$("zoneCode").value;
     dhx.showProgress("正在执行......");
     LoginLogAction.getMenuVisitCount(map, function (res) {
    	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("查询失败,请稍后重试!");
				            return;
				        }
					 buildTable(res);
    });
}


function buildTable(data){
	 var  excelData="";
	 
	     tableStr ="<table  width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
            tableStr +="<tr>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>应用名</span></td>"; 
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>应用路径</span></td>"; 
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>访问次数</span></td>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>操作</span></td>"; 
            tableStr +="</tr>";
           var total=0;
		   if(data&&data.length>0)
		   {  
			     tableStr =tableStr+
		           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
		             +"<td>合计</td>"
		             +"<td colspan='1'></td>"
		             +"<td id='totalNum'></td>"
		             +"<td onclick=\"lookDetailInfo('');\"  class='unl'>查看详情</td>"
				   +"</tr>";
		       for(var i=0;i<data.length;i++)
		       {
		        tableStr =tableStr+
		           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		   tableStr +="<td>"+data[i].MENU_NAME+"</td>"
		             +"<td  align='left'>"+data[i].MENU_PATH+"</td>"
				     +"<td>"+data[i].CNT+"</td>"
				     +"<td onclick=\"lookDetailInfo('"+data[i].MENU_ID+"');\"  class='unl'>查看详情</td>"
				  +"</tr>";
		          total +=1*data[i].CNT;
		          excelData += data[i].MENU_NAME+"}" 
		                     + data[i].MENU_PATH +"}"
		                     + data[i].CNT+"}" 
		                     + "查看详情";
		          excelData +="]";
		       }
		        
		   }
		    else
		   {
			      tableStr =tableStr+
			      "<tr>"
			          +"<td colspan='100'>没有数据显示</td>"
			      +"</tr>";
		   }  
        
         tableStr +="</table>";
         $('dataTable').innerHTML=tableStr;
         $("totalNum").innerHTML=total;
         $("excelData").value= excelData;//Excel数据
}

//--------------------------------详细访问信息---------------------------------
function lookDetailInfo(menuId) {	
	var width =1100;
	var height=800;
    var top  = 100;
    var left = 100;
    var zoneCode = $("zoneCode").value==""?zoneInfo.zoneCode:$("zoneCode").value;
    var url = base + "/meta/module/mag/log/detail/menuVisitDetail.jsp?"
    	url +="menuId1=" + menuId + "&zoneCode="+ zoneCode
   			+"&startTime="+$('startTime').value+"&endTime="+$('endTime').value;
    var param="dialogWidth:"+width+"px;dialogHeight:"+height+"px;dialogLeft:"+left+"px;dialogTop:"+top+"px;center:yes;help:no;resizable:no;status:no;scroll:yes";
   // alert(url);
    window.showModalDialog(url,window,param);
}

//查询
var queryData=function(){
    excuteInitData();
}
dhx.ready(indexInit);