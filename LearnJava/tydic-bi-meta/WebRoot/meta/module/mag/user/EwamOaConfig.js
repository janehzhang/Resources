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
var page=null;
/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCenterCode");
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
    ZoneAction.queryProCenterCode(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
/** Tree head **/

dwrCaller.addAutoAction("loadZoneTree1","ZoneAction.queryZoneByPathToBuss");
var zoneConverter1=dhx.extend({idColumn:"zoneId",pidColumn:"zoneParId",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree1",zoneConverter1);
//树动态加载Action
dwrCaller.addAction("querySubZone1",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter1,false);
    ZoneAction.querySubZoneToBuss(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
dwrCaller.addAutoAction("deleteOaUser","LoginLogAction.deleteEwamUser");
dwrCaller.addAutoAction("insertUser","LoginLogAction.insertEwamUser");
dwrCaller.addAutoAction("editUser","LoginLogAction.editEwamUser");

/** Tree head **/
var zoneInfo = top.getSessionAttribute('zoneInfo');
var indexInit = function() {
	 page=Page.getInstance();
	 page.init();
     //1.加载地域树 
	 loadZoneTreeChkBox(zoneInfo.zoneCode,queryform);
	 //执行查询数据
     excuteInitData();
}
var excuteInitData=function(){
     var map=new Object();
     map.staffName   =$("staffName").value;
     map.staffCode     =$("staffCode").value;
     map.zoneCode    =$("zoneCode").value==""?zoneInfo.zoneCode:$("zoneCode").value;
     map.pageCount=  page.pageCount;    //每页显示多少条数
     map.currPageNum=page.currPageNum;//当前第几页
     dhx.showProgress("正在执行......");
     LoginLogAction.getEwamUsersList(map, function (res) {
    	               dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("查询失败,请稍后重试!");
				            return;
				        }
					 buildTable(res);
    });
}

function buildTable(res){
	 var  excelData="";
	     tableStr ="<table  width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
         tableStr +="<tr>";
         tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>地市</span></td>"; 
         tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>分公司</span></td>"; 
         tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>营销中心</span></td>"; 
         tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>姓名</span></td>"; 
         tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>OA账号</span></td>"; 
         tableStr +="<td nowrap bgcolor='#cde5fd'><span class='title'>操作</span></td>"; 
         tableStr +="</tr>";
  if(res&&res.dataColumn.length>0)
	{
		for(var i=0;i<res.dataColumn.length;i++)
	 {
		tableStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
		tableStr=tableStr
		+"<td width='15%'>"+res.dataColumn[i].CITY_NAME+"</td>"
		+"<td width='20%'>"+res.dataColumn[i].AREA_NAME+"</td>"
		+"<td width='20%'>"+res.dataColumn[i].DEPT_NAME+"</td>"
        +"<td width='15%'>"+res.dataColumn[i].STAFF_NAME+"</td>"
        +"<td width='15%'>"+res.dataColumn[i].STAFF_CODE+"</td>"
        +"<td width='15%'><a href='javascript:void(0)' onClick=\"editUser('"+res.dataColumn[i].SEQ+"')\">修改</a>&nbsp;" 
        +"<a href='javascript:void(0)' onClick=\"deleteUser('"+res.dataColumn[i].SEQ+"')\">删除</a>"
        +"</td>";
		excelData=excelData
		+res.dataColumn[i].CITY_NAME+"}"
		+res.dataColumn[i].AREA_NAME+"}"
		+res.dataColumn[i].DEPT_NAME+"}"
		+res.dataColumn[i].STAFF_NAME+"}"
        +res.dataColumn[i].STAFF_CODE+"}"; 
		tableStr +="</tr>";
		excelData +="]";
	 }		         
   }
 else
  {
	   tableStr +="<tr>"
		        +"<td colspan='100'>没有数据显示</td>"
	            +"</tr>";
  } 
     tableStr +="</table>";
     $('dataTable').innerHTML=tableStr;
     $("excelData").value= excelData;//Excel数据
     $("totalCount").value=res.allPageCount;
     page.buildPage(res.allPageCount, 1);//调用公共分页函数page.js
}

//--------------------------------操作---------------------------------
var deleteUser=function(seq){
    dhx.confirm("确定要删除该记录吗？", function(r){
        if(r){
            dwrCaller.executeAction("deleteOaUser",seq,function(data){
                if(data.type == "error" || data.type == "invalid"){
                    dhx.alert("对不起，删除失败，请重试！");
                }else{
                    dhx.alert("删除成功！");
                    queryData();
                    }
            });
        }
    })
}

applyUser=function(){
	    //初始化新增弹出窗口。
	    var dhxWindow = new dhtmlXWindows();
	    dhxWindow.createWindow("addWindow", 0, 0, 100, 200);
	    var addWindow = dhxWindow.window("addWindow");
	    addWindow.setModal(true);
	    addWindow.stick();
	    addWindow.setDimension(450, 300);
	    addWindow.center();
	    addWindow.setPosition(addWindow.getPosition()[0]-120,addWindow.getPosition()[1]-20);
	    addWindow.denyResize();
	    addWindow.denyPark();
	    addWindow.button("minmax1").hide();
	    addWindow.button("park").hide();
	    addWindow.button("stick").hide();
	    addWindow.button("sticked").hide();
	    addWindow.setText("申请用户");
	    addWindow.keepInViewport(true);
	    addWindow.show();
	    /**
	     * 新增用户
	     */
	    var addFormData=[
	        {type:"block",offsetTop:20,list:[
	            {type:"input",offsetLeft:90,label:"姓 名： ",inputWidth: 200,name:"staffName",validate:"NotEmpty,MaxLength[64]"},
	            {type:"newcolumn"},
	            {type:"label",label:"<span style='color: red'>*</span>"}
	        ]},
	        {type:"block",offsetTop:15,list:[
	            {type:"input",offsetLeft:75,label:"OA    账号：",inputWidth: 200,name:"staffCode",validate:"NotEmpty,MaxLength[64]"},
	            {type:"newcolumn"},
	            {type:"label",label:"<span style='color: red'>*</span>"}
	        ]},
	        {type: "block", offsetTop:15,list:[
	             {type:"input",offsetLeft:70,label:"负责区域：",inputWidth: 200,name:"zone"},
	             {type:"newcolumn"},
	             {type:"label",label:"<span style='color: red'>*：请选择OA推送人员区域</span>",offsetLeft:145}
	        ]},
	        {type:"block",offsetTop:15,list:[
	            {type:"button",label:"提交",name:"save",value:"提交",offsetLeft:160},
	            {type:"newcolumn"},
	            {type:"button",label:"关闭",name:"close",value:"关闭",offsetLeft:10}
	        ]},
	        {type:"hidden",name:"zoneId"},
	        {type:"hidden",name:"dimLevel"}
	    ];
	    //建立表单。
	    var addForm = addWindow.attachForm(addFormData);
	    addForm.setFormData({zoneId:"1",dimLevel:"1",zone:"广东省"});
	    loadZoneTree(zoneInfo.zoneId,addForm);
	    //新增表单事件处理
	    addForm.attachEvent("onButtonClick", function(id) {
	        var validateAdd=function(){
	        	var data = addForm.getFormData();
		    	if(data.staffName==null||data.staffName==''){
		    	    alert('请填写姓名！');
		    	    return false;
		    	}if(data.staffCode==null||data.staffCode==''){
		    	    alert('请填写OA账号！');
		    	    return false;
		    	}if(data.zone==null||data.zone==''){
		    	    alert('请选择OA推送人员区域！');
		    	    return false;
		    	}if(data.dimLevel<1){
		    	    alert('请选择OA推送人员区域！');
		    	    return false;
		    	}
		    	  return true;
	      }
	        var insertUser=function(param){
		    	if(validateAdd()){
		        dhx.confirm("是否确定申请该用户？", function(r){
		            if(r){
		                dwrCaller.executeAction("insertUser",param,function(data){
		                    if(data.sid==-2){
		                        dhx.alert("对不起，申请出错，请重试！");
		                    }if(data.sid==-1){
		                        dhx.alert("该营销中心已配置用户，请重试！");
		                    }else{
		                        dhx.alert("申请成功！");
		                        addWindow.close();
		                        queryData();
		                        }
		                });
		            }
		        })
		    	}
		    }
	        if (id == "save") {
	        	var data= addForm.getFormData();
	        	LoginLogAction.getDimLevelById(data.zoneId, function (res) {
		    		 dhx.closeProgress();
		    		 if (res == null) {
				            dhx.alert("查询失败,请稍后重试!");
				            return;
				        }else{
				        	addForm.setFormData({dimLevel:res.DIM_LEVEL});
				        	data = addForm.getFormData();
				        	insertUser(data);
				        }
	             }); 	        	
	        }
	        if(id == "close"){
	            addWindow.close();
	            dhxWindow.unload();
	        }
	    });
	   
}

var editUser=function(seq){
	    //初始化新增弹出窗口。
	    var dhxWindow = new dhtmlXWindows();
	    dhxWindow.createWindow("editWindow", 0, 0, 100, 200);
	    var editWindow = dhxWindow.window("editWindow");
	    editWindow.setModal(true);
	    editWindow.stick();
	    editWindow.setDimension(450, 300);
	    editWindow.center();
	    editWindow.setPosition(editWindow.getPosition()[0]-100,editWindow.getPosition()[1]-80);
	    editWindow.denyResize();
	    editWindow.denyPark();
	    editWindow.button("minmax1").hide();
	    editWindow.button("park").hide();
	    editWindow.button("stick").hide();
	    editWindow.button("sticked").hide();
	    editWindow.setText("修改用户");
	    editWindow.keepInViewport(true);
	    editWindow.show();
	    /**
	     * 新增用户
	     */
	    var editFormData=[
                           {type:"block",offsetTop:20,list:[
                 	            {type:"input",offsetLeft:90,label:"姓 名： ",inputWidth: 200,name:"staffName",validate:"NotEmpty,MaxLength[64]"},
                 	            {type:"newcolumn"},
                 	            {type:"label",label:"<span style='color: red'>*</span>"}
                 	        ]},
                 	        {type:"block",offsetTop:15,list:[
                 	            {type:"input",offsetLeft:75,label:"OA    账号：",inputWidth: 200,name:"staffCode",validate:"NotEmpty,MaxLength[64]"},
                 	            {type:"newcolumn"},
                 	            {type:"label",label:"<span style='color: red'>*</span>"}
                 	        ]},
                 	        {type: "block", offsetTop:15,list:[
                 	             {type:"input",offsetLeft:70,label:"负责区域：",inputWidth: 200,name:"zone"},
                 	             {type:"newcolumn"},
                 	             {type:"label",label:"<span style='color: red'>*：请选择OA推送人员区域</span>",offsetLeft:145}
                 	        ]},
                 	        {type:"block",offsetTop:15,list:[
                 	            {type:"button",label:"保存",name:"save",value:"保存",offsetLeft:160},
                 	            {type:"newcolumn"},
                 	            {type:"button",label:"关闭",name:"close",value:"关闭",offsetLeft:10}
                 	        ]},
                 	        {type:"hidden",name:"zoneId"},
                 	        {type:"hidden",name:"dimLevel"}
	    ];
	    //建立表单。
	    var editForm = editWindow.attachForm(editFormData);
	    var initForm=function(){
	    	LoginLogAction.getEwamUserById(seq, function (res) {
	               dhx.closeProgress();
			        if (res == null) {
			            dhx.alert("修改失败，请重试!");
			            return;
			        }
			        var initData={
			        		staffName:res.STAFF_NAME,//姓名
			        		staffCode:res.STAFF_CODE,//OA账号
			        		zone:res.ZONE_NAME,//营销中心姓名
			        		zoneId:res.ZONE_CODE,//营销中心id
			        		seq:seq,
			        		dimLevel:res.DIM_LEVEL
			        }
			        editForm.setFormData(initData);
             });  
	    }
	    loadZoneTree(null,editForm);
	    initForm();
	    //新增表单事件处理
	    editForm.attachEvent("onButtonClick", function(id) {
	        var validateAdd=function(){
	        	var data = editForm.getFormData();
		    	if(data.staffName==null||data.staffName==''){
		    	    alert('请填写姓名！');
		    	    return false;
		    	}if(data.staffCode==null||data.staffCode==''){
		    	    alert('请填写OA账号！');
		    	    return false;
		    	}if(data.zone==null||data.zone==''){
		    	    alert('请选择OA推送人员区域！');
		    	    return false;
		    	}if(data.dimLevel<1){
		    	    alert('请选择OA推送人员区域！');
		    	    return false;
		    	}
		    	  return true;
	      }
	        var editUser=function(param){
		    	if(validateAdd()){
		        dhx.confirm("是否确定修改该用户？", function(r){
		            if(r){
		                dwrCaller.executeAction("editUser",param,function(data){
		                    if(data.sid==-2){
		                        dhx.alert("对不起，修改出错，请重试！");
		                    }if(data.sid==-1){
		                        dhx.alert("该营销中心已配置用户，请重试！");
		                    }else{
		                        dhx.alert("修改成功！");
		                        editWindow.close();
		                        queryData();
		                        }
		                });
		            }
		        })
		    	}
		    }
	        if (id == "save") {
	        	var data= editForm.getFormData();
	        	LoginLogAction.getDimLevelById(data.zoneId, function (res) {
		    		 dhx.closeProgress();
		    		 if (res == null) {
				            dhx.alert("修改失败,请稍后重试!");
				            return;
				        }else{
				        	editForm.setFormData({dimLevel:res.DIM_LEVEL});
				        	data = editForm.getFormData();
				        	editUser(data);
				        }
	             }); 
	        }
	        if(id == "close"){
	        	editWindow.close();
	            dhxWindow.unload();
	        }
	    });
	   
}
/**
 * 地域树加载
 * @param name
 * @param value
 */
var loadZoneTree=function(selectZone,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectZone=selectZone|| global.constant.defaultRoot;
    var beginId=0;
    //创建tree Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target=form.getInput("zone");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubZone1);
    //树双击鼠标事件
    tree.attachEvent("onDblClick",function(nodeId){
        form.setFormData({zone:tree.getItemText(nodeId),zoneId:nodeId});
        //关闭树
        div.style.display="none";
    });
    dwrCaller.executeAction("loadZoneTree1",beginId,selectZone,function(data){
        tree.loadJSONObject(data);
        globZoneTree = tree;
        if(selectZone){
            tree.selectItem(selectZone); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    })
    div.style.display="none";
}

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
	page.resetPage();
    excuteInitData();
}
dhx.ready(indexInit);