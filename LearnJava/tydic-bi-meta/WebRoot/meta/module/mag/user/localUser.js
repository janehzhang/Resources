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
var systemId=421;//用户关联菜单默认的系统代码
var page=null;
var tempData;
var excelData = "";
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
	 page=Page.getInstance();
	 page.init();
	 page.pageCount = 20;
     //1.加载地域树 
	loadZoneTreeChkBox(zoneInfo.zoneCode,queryform);
	 //执行查询数据
     excuteInitData();
}
var excuteInitData=function(){
     var map=new Object();
     //map.startTime   =$("startTime").value;
     //map.endTime     =$("endTime").value;    
     map.userName    =$("uName").value;
     map.zoneCode    =$("zoneCode").value==""?zoneInfo.zoneCode:$("zoneCode").value;
     
     map.pageCount=  page.pageCount;    //每页显示多少条数
     map.currPageNum=page.currPageNum;//当前第几页
     //dhx.alert(page.pageCount + " " + page.currPageNum);
     dhx.showProgress("正在执行......");
     UserAction.queryLocalUser(map, function (res) {
    	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("查询失败,请稍后重试!");
				            return;
				        }				        
					 buildTable(res);
					 tempData = res;
    });
}


function buildTable(data){
	 //var  excelData="";
	 
	     tableStr ="<table  width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
            tableStr +="<tr>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>用户名</span></td>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>OA账号</span></td>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>地 域</span></td>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>部 门</span></td>"; 
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>岗 位</span></td>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>角色</span></td>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>管理员身份</span></td>";
                  tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>权限菜单</span></td>";
            tableStr +="</tr>";
           var total=0;
           var i=0;
		   if(data&&data.length>0)
		   {  
		       for( i=(page.currPageNum-1)*page.pageCount;i<page.currPageNum*page.pageCount&& i<data.length;i++)
		       {
		    	   if (data[i].ISADMIN == 1 || data[i].ISADMIN == 2){
				        tableStr +=
					           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\" style='color: #FF0000'>";    		   
		    	   }else{
				        tableStr +=
					           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		    	   }
		   tableStr +="<td>"+data[i].USER_NAMECN+"</td>"
		   			 +"<td>"+data[i].USER_NAMEEN+"</td>"
		             +"<td>"+data[i].ZONE_NAME+"</td>"
				     +"<td>"+data[i].DEPT_NAME+"</td>"
				     +"<td>"+data[i].STATION_NAME+"</td>"
				     +"<td>"+data[i].ROLE_NAME+"</td>";
	          if(data[i].ISADMIN == 1)  tableStr += "<td>省公司管理员</td>"
		          else if(data[i].ISADMIN == 2) tableStr += "<td>分公司管理员</td>"
		          else        tableStr += "<td>否</td>"
				     //+"<td>"+data[i].ISADMIN+"</td>"
		        tableStr +="<td onclick=\"lookMenuTree('"+ data[i].USER_ID +"');\"  class='unl'>查看详情</td>"
		        tableStr +="</tr>";
		          //total +=1*data[i].CNT;
//		          excelData += data[i].USER_NAMECN+"}"
//		          			 + data[i].USER_NAMEEN +"}"
//		                     + data[i].ZONE_NAME +"}"
//		                     + data[i].DEPT_NAME +"}"
//		                     + data[i].STATION_NAME +"}"
//		                     + data[i].ROLE_NAME+"}"; 
//		          if(data[i].ISADMIN == 1)  excelData += "省公司管理员}"
//		          else if(data[i].ISADMIN == 2) excelData += "分公司管理员}"
//		          else        excelData += "否}"
//		                     + "查看详情";
//		          excelData +="]";
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
         //$("excelData").value = excelData;//Excel数据
         page.buildPage(data.length, 1);//调用公共分页函数page.js
}


//--------------------------------查看菜单权限---------------------------------
/**
 * Menu树Data转换器定义
 */
var menuConvertConfig = {
	idColumn:"menuId",pidColumn:"parentId",
    textColumn:"menuName",
    isDycload:false ,
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex,rowData){
        return {menuData:rowData};
    },
    isOpen:function(){
        return false;
    },
    
    afterCoverted:function(data){
        if(data){       	
            for(var i = 0; i < data.length; i++){
                data[i].open = data[i].items ? true : false;
            }
        }
        return data;
    },
    compare
            : function(data1, data2){
        if(data1.userdata[0]["content"].orderId == undefined
                || data1.userdata[0]["content"].orderId == null){
            return false;
        }
        if(data2.userdata[0]["content"].orderId == undefined
                || data2.userdata[0]["content"].orderId == null){
            return false;
        }
        return data1.userdata[0]["content"].orderId <= data2.userdata[0]["content"].orderId
    }
}
var menuTreeDataConverter=new dhtmxTreeDataConverter(menuConvertConfig);


function lookMenuTree(userId) {
	//alert("userId:"+ userId);
	//var rowData=mygrid.getRowData(rowId);//获取行数据
    var dhxWindow=new dhtmlXWindows();
    dhxWindow.createWindow("modifyWindow",0,0,520,300);
    var modifyWindow=dhxWindow.window("modifyWindow");
    modifyWindow.setModal(true);
    modifyWindow.stick();
    modifyWindow.setDimension(770,440);
    modifyWindow.center();
    modifyWindow.denyResize();
    modifyWindow.denyPark();
    modifyWindow.setText("用户权限菜单");
    modifyWindow.keepInViewport(true);
    //关闭一些不用的按钮。
    modifyWindow.button("minmax1").hide();
    modifyWindow.button("park").hide();
    modifyWindow.button("stick").hide();
    modifyWindow.button("sticked").hide();
    modifyWindow.show(); 
    var modifyLayout = new dhtmlXLayoutObject(modifyWindow,"1C");
    
//    modifyLayout.cells("c").setHeight(20);
//    modifyLayout.cells("c").fixSize(true, true);
//    modifyLayout.cells("c").hideHeader();
//    modifyLayout.cells("a").fixSize(true, true);
//    modifyLayout.cells("a").hideHeader();
    modifyLayout.cells("a").fixSize(true, true);
    modifyLayout.cells("a").hideHeader();
    modifyLayout.hideSpliter();//移除分界边框。
   
    var loadMenuParam = new biDwrMethodParam();//loadMenu Action参数设置。
    //定义loadMen Action的参数来源于表单queryform数据。
    loadMenuParam.setParamConfig([
        {
            index:0,type:"fun",value:function(){
            return systemId;
        }
        },{
            index:1,type:"static",value:userId
        }
    ]);
    
    var modifyStyle = {
            add:"font-weight:bold;color:#64B201;",
            remove:"font-weight:bold;color:#CCCCCC",
            clear:"font-weight:normal;font-style::normal;text-decoration:none;color:black;border-bottom:0px;"
        };  
    dwrCaller.addAutoAction("loadMenu","UserManageAction.queryMenuTreeData1",loadMenuParam,function(data){
        dhx.closeProgress();
        systemId= 421;
    });
    dwrCaller.addDataConverter("loadMenu",menuTreeDataConverter);
    
    var tree = modifyLayout.cells("a").attachTree();
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableSmartCheckboxes(false);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.enableCheckBoxes(true);
    
    var loadTreeData=function(){
            var childs = tree.getSubItems(0);
            if(childs){
                var childIds = (childs + "").split(",");
                for(var i = 0; i < childIds.length; i++){
                    tree.deleteItem(childIds[i]);
                }
            }
            tree.loadJSON(dwrCaller.loadMenu);        
    }
    	loadTreeData();    
    var add={};
    //转换器加入默认选中逻辑
    menuTreeDataConverter.addItemConfig=function(rowIndex,rowData){
        var checked=rowData.checked;
        var menuId=rowData.menuId;
        var style=modifyStyle.clear;
        if(checked==1){
            style=modifyStyle.add;
            checked=1;
        }else if(checked==0){
            style=modifyStyle.remove;
            checked=0;
        }
        return {checked:checked,style:style};
    }
   
}
//##############################################
function createExcelDataAll(){
	excelData = "";
	$("excelData").value = excelData;//Excel数据
	var data = tempData ;
    //alert("data len:"+ tempData.length + map.userName);
    		 if(data && data.length >0)
    		   {  		
    			 var i=0;
    		       for(i=0; i<data.length;i++)
    		       {	          
    			          excelData += data[i].USER_NAMECN+"}"
    	       			 			+ data[i].USER_NAMEEN +"}"
    				                + data[i].ZONE_NAME +"}"
    				                + data[i].DEPT_NAME +"}"
    				                + data[i].STATION_NAME +"}"
    				                + data[i].ROLE_NAME+"}"; 
    				       if(data[i].ISADMIN == 1)  		excelData += "省公司管理员}";
    				       else if(data[i].ISADMIN == 2) 	excelData += "分公司管理员}";
    				       else        						excelData += "否}";
    						       excelData += "查看详情";
    						       excelData +="]";
    		       }
    		       //alert("data len:"+ data.length);
    		       $("excelData").value = excelData;//Excel数据
    		      return true;
    		   }  	
    		 return false;
}


//****************************************************

//查询
var queryData=function(){
	$("excelUName").value = $("uName").value;
	$("excelZone").value  = $("zone").value;
	page.resetPage();
    excuteInitData();
}
dhx.ready(indexInit);