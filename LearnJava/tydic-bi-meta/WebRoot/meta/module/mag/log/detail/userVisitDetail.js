/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller = new biDwrCaller();
var queryform = $('queryform');
var indexInit = function() {
	 //执行查询数据
     excuteInitData();
}
var excuteInitData=function(){
     var map=new Object();
     map.startTime=startTime;
     map.endTime=endTime;
     map.userName=userName;
     dhx.showProgress("正在执行......");
     LoginLogAction.getUserVisitDetail(map, function (res) {
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
            tableStr +="<tr>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>用户名</span></td>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>地 域</span></td>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>部 门</span></td>"; 
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>岗 位</span></td>"; 
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>菜单名</span></td>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>访问路径</span></td>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>访问时间</span></td>";
            tableStr +="</tr>";
		   if(data&&data.length>0)
		   {  
		       for(var i=0;i<data.length;i++)
		       {
		        tableStr =tableStr+
		           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		   tableStr +="<td  align='left'>"+data[i].USER_NAMECN+"</td>"
		             +"<td>"+data[i].SHOW_ZONE_NAME+"</td>"
				     +"<td>"+data[i].DEPT_NAME+"</td>"
				     +"<td>"+data[i].STATION_NAME+"</td>"
				     +"<td align='left'>"+data[i].MENU_NAME+"</td>"
				     +"<td align='left'>"+data[i].MENU_PATH+"</td>"
				     +"<td>"+data[i].VISIT_TIME+"</td>"
				  +"</tr>";
		   
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
}
dhx.ready(indexInit);