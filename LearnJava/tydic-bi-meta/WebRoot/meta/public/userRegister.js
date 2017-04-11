/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var loadUserParam=new biDwrMethodParam();//loaduser Action参数设置。
var userNamecn= userNamecn;
var systemId=421;//用户关联菜单默认的系统代码
var zoneId='';
var actName='';
if(user==null){
	zoneId='1';
	actName='';
}else{
	zoneId=user.zoneId;
	actName=user.userNamecn;
}
/**
 * 定义全局的部门、岗位、地域树
 */
var globZoneTree = null;
var globMenuTree = null;

/**
 * User表Data转换器
 */
var userConvertConfig = {
    idColumnName:"userId",
    filterColumns:["userNameen","userNamecn","zoneName","deptName","stationName","state","returnReason","createDate","actorName","_buttons","roleName","zoneId","userEmail","userMobile"],
    /**
     * 覆盖父类方法：数据转换
     * @param rowIndex
     * @param rowData
     */
    userData:function(rowIndex,rowData){
        var userData ={};
        if(rowData["createDate"] != null){
            userData["createDate"]=rowData["createDate"];
        } else {
            userData["createDate"]="";
        }
        if(rowData["oaUserName"] != null){
            userData["oaUserName"]=rowData["oaUserName"];
        } else {
            userData["oaUserName"]="";
        }
        userData.deptId=rowData.deptId;
        userData.stationId=rowData.stationId;
        userData.zoneId=rowData.zoneId;
        userData.groupId=rowData.groupId;
        userData.vipFlag=rowData.vipFlag;
        userData.state = rowData.state;
        userData.actorName = rowData.actorName;
        userData.roleId = rowData.roleId;
        userData.createDate = rowData.createDate;
        return userData;
    },
    /**
     * 获取下拉框Button的值
     * @param rowIndex
     * @param columnIndex
     * @param cellValue
     * @param rowData
     */
    cellDataFormat:function( rowIndex,  columnIndex,columnName,  cellValue,rowData) {
    	if (columnName == "_buttons") {//操作按钮列	
    		if(rowData.state=='退回'||rowData.state=='归档'){
    			    var   str ="";
					str +="<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=modifyUser("+rowData["userId"]+")>修改</a></span>";
					return str;
    		}
			}
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName,cellValue, rowData);
    }
}
var userDataConverter=new dhtmxGridDataConverter(userConvertConfig);
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
    compare : function(data1, data2){
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
/**
 * 构造dwrCaller
 */
var dwrCaller = new biDwrCaller({
    querySystem:{methodName:"UserManageAction.queryMenuSystem"
        ,converter:new dhtmlxComboDataConverter({
            valueColumn:"groupId",textColumn:"groupName",isAdd:true
        })},
    update:function(afterCall, param){}
});

/**
 * 用户页面初始化方法
 */
var userInit=function(){
    var userLayout = new dhtmlXLayoutObject(document.body,"2E");
    userLayout.cells("a").setText("账号申请");
    userLayout.cells("b").hideHeader(true);
    userLayout.cells("a").setHeight(80);
    userLayout.cells("a").fixSize(false,true);
    userLayout.hideConcentrate();
    userLayout.hideSpliter();//移除分界边框。
    //加载查询表单
    var queryform = userLayout.cells("a").attachForm([

        {type: "block", list:[
            {type:"settings",position: "label-left",labelAlign:"right",inputWidth: 120,labelLeft:0 ,
                inputLeft:0,offsetTop:0},
            {type:"input",label:"姓名：",name:"userName",inputHeight:17,inputWidth:150,validate:"MaxLength[20]"},
            {type:"newcolumn"},
            {type:"input",label:"地域：",name:"zone",inputHeight:17,inputWidth: 150},
            {type:"newcolumn"},
            {type : "combo",label : "状态：",name : "state",options:[{value:"",text:"全部",selected:true}],inputWidth:120,readonly:true},
            {type:"newcolumn"},
            {type:"input",label:"部门：",name:"deptName",inputHeight:17,inputWidth: 150},
            {type:"newcolumn"},
            {type:"input",label:"岗位：",name:"stationName",inputHeight:17,inputWidth:150},
            {type:"newcolumn"},
            {type:"button",name:"query",value:"查询",offsetLeft:45,offsetTop:3}
        ]}
    ]);
    if(userNamecn != null){
        queryform.setValue("userName",userNamecn);
    }
    //创建下拉列表
    var stateTypeData = getComboByRemoveValue("APPLY_STATE");
    queryform.getCombo("state").addOption(stateTypeData.options); 
    /*queryform.setFormData( {
		"userState" : "7"
	});*/
    queryform.defaultValidateEvent();
    //定义loadUser Action的参数来源于表单queryform数据。
    loadUserParam.setParamConfig([{
        index:0,type:"fun",value:function(){
            var data=queryform.getFormData();
            if(data.zoneId == null || data.zoneId ==""){
                data.zoneId=zoneId.toString();
            }
            return data;
        }
    }]);
    //加载地域树 
    loadZoneTreeChkBox(zoneId,queryform);
    //加载岗位树
   // loadStationTreeChkBox(null,queryform);
var base=getBasePath();

var buttons={
    applyUser:{name:"applyUser",text:"账号申请",imgEnabled:base+"/meta/resource/images/addUser.png",
        imgDisabled:base+"/meta/resource/images/addUser.png",onclick:function(rowData){
            applyUser();
        }},
    modifyUser:{name:"modifyUser",text:"修改",imgEnabled:base+"/meta/resource/images/editUser.png",
        imgDisabled:base+"/meta/resource/images/editUser.png",onclick:function(rowData){
            modifyUser(rowData.id);
        }}
};

// 按钮权限过滤,checkRule.
var buttonRole=["modifyUser","applyUser"];
var bottonRoleRow = [];
for(var i=0; i<buttonRole.length; i++){
    if(buttonRole[i]=="applyUser"){
        bottonRoleRow.push(buttonRole[i]);
    }
}

var buttonRoleCol = [];
for(var i=0; i<buttonRole.length; i++){
    if(buttonRole[i]!="applyUser"){//操作列不加入新增按钮
        buttonRoleCol.push(buttonRole[i]);
    }
}
window["getRoleButtonsCol"]=function(rowId,cellId){
    var rowdata = mygrid.getRowData(rowId);
    var res=[];
    if(rowdata.userdata.state == 1){
        for(var i=0;i<buttonRoleCol.length;i++){
            if(buttonRoleCol[i]=="applyUser"){
                buttons["applyUser"].text="申请";
                res.push(buttons["applyUser"])
            }else{
                res.push(buttons[buttonRoleCol[i]]);
            }

        }
    }else{
        for(var i=0;i<buttonRoleCol.length;i++){
            if(buttonRoleCol[i]=="applyUser"){
                buttons["applyUser"].text="申请";
            }else{
                res.push(buttons[buttonRoleCol[i]]);
            }

        }
    }

    return res;
};
//定义全局函数，用于获取有权限的button列表定义
window["getButtons"]=function(){
    var res=[];
    for(var i=0;i<bottonRoleRow.length;i++){
        res.push(buttons[bottonRoleRow[i]]);
    }
    return res;
};



var buttonToolBar=userLayout.cells("b").attachToolbar();
var pos=1;
var filterButton=window["getButtons"]();
for(var i=0;i<filterButton.length;i++){
    buttonToolBar.addButton(filterButton[i].name,pos++,filterButton[i].text,
            filterButton[i].imgEnabled,filterButton[i].imgDisabled);
    var button=buttonToolBar.getItemNodeById(filterButton[i].name);
    button.setAttribute("id",filterButton[i].name);
}
    dwrCaller.addAutoAction("loadUser", "UserManageAction.queryUser", loadUserParam,
            function(data){
            }
    );
    dwrCaller.addDataConverter("loadUser",userDataConverter);
    //添加datagrid
    mygrid= userLayout.cells("b").attachGrid();
    mygrid.setHeader("<span style='font-weight: bold;'>账号</span>,<span style='font-weight: bold;'>姓名</span>," +
                     "<span style='font-weight: bold;'>地域</span>,<span style='font-weight: bold;'>部门</span>," +
                     "<span style='font-weight: bold;'>岗位</span>," +
                     "<span style='font-weight: bold;'>状态</span>,<span style='font-weight: bold;'>被退原因</span>," +
                     "<span style='font-weight: bold;'>操作时间</span>,<span style='font-weight: bold;'>操作人</span>," +
                     "<span style='font-weight: bold;'>操作</span>");
    mygrid.setInitWidthsP("8,8,10,10,10,10,12,12,10,10");
    mygrid.setColAlign("center,center,center,center,center,center,center,left,center,center");
    mygrid.setHeaderAlign("center,center,center,center,center,center,center,center,center,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
    mygrid.setColSorting("na,na,na,na,na,na,na,na,na,na");
    mygrid.setEditable(true);
    mygrid.enableMultiselect(true);
    mygrid.enableCtrlC();
    mygrid.setColumnIds("userNameen,userNamecn,zoneName,deptName,stationName,state,returnReason,createDate,actorName,target");
    //隐藏显示tooltip
    mygrid.enableTooltips("true,true,true,true,true,true,true,true,true,false");
    mygrid.enableSelectCheckedBoxCheck(1);
    mygrid.init();
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.loadUser,"json");

    //添加buttonToolBar事件
    buttonToolBar.attachEvent("onclick",function(id){
        if(id=="applyUser"){//申请用户
            applyUser();
        }
        if(id == "modifyUser"){//修改用户
            var selecteddRowId= mygrid.getCheckedRows(0);
            if(selecteddRowId == null || selecteddRowId==""){
                dhx.alert("请选择一行进行修改!");
                return ;
            }else if(selecteddRowId.split(",").length>1){
                dhx.alert("只能选择一行进行修改!");
                return ;
            }else{
                modifyUser(selecteddRowId);
            }
        }
    })


    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id){
        if(id=="query"){
            //进行数据查询。
            if(queryform.validate()){
                mygrid.clearAll();
                mygrid.load(dwrCaller.loadUser,"json");
            }
        }
    });
    queryform.getInput("userName").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            mygrid.clearAll();
            mygrid.load(dwrCaller.loadUser, "json");
        }
    }
}

/**
 * 申请用户
 */
var applyUser=function(){
    var dhxWindow=new dhtmlXWindows();
    dhxWindow.createWindow("applyWindow",0,0,520,300);
    var applyWindow=dhxWindow.window("applyWindow");
    applyWindow.setModal(true);
    applyWindow.stick();
    applyWindow.setDimension(770,390);
    applyWindow.center();
    applyWindow.denyResize();
    applyWindow.denyPark();
    //关闭一些不用的按钮。
    applyWindow.button("minmax1").hide();
    applyWindow.button("park").hide();
    applyWindow.button("stick").hide();
    applyWindow.button("sticked").hide();
    applyWindow.setText("账号申请");
    applyWindow.keepInViewport(true);
    applyWindow.show();
    
    var modifyLayout = new dhtmlXLayoutObject(applyWindow,"3U");
    modifyLayout.cells("c").setHeight(20);
    modifyLayout.cells("c").fixSize(true, true);
    modifyLayout.cells("c").hideHeader();
    modifyLayout.cells("a").fixSize(true, true);
    modifyLayout.cells("a").hideHeader();
    modifyLayout.cells("b").fixSize(true, true);
    modifyLayout.cells("b").hideHeader();
    modifyLayout.hideSpliter();//移除分界边框。
    
  //建立表单。
    var applyForm=modifyLayout.cells("a").attachForm([                                   
                      {type: "block", list:[
                      {type:"input",offsetLeft:40,label:"账号：",name:"userNameen",/*validate:"ValidAplhaNumeric,MaxLength[50]",*/labelWidth:40},
                      {type:"newcolumn"},
                      {type:"label",label:"<span style='color: red;font-size:10px;'>*</span>",offsetLeft:0}
                      ]},
                      {type: "block", list:[
                      {type:"label",label:"<span style='color: red;font-size:10px;'>建议与OA账号一致，可从OA单点登录系统。</span>",offsetLeft:40,labelWidth:0}
                      ]},
                      {type: "block", list:[
                      {type:"input",offsetLeft:40,label:"姓名：",name:"userNamecn"/*,validate:"NotEmpty,MaxLength[20],IllegalChar"*/,labelWidth:40},
                      {type:"newcolumn"},
                      {type:"label",label:"<span style='color: red;font-size:10px;'>*</span>",offsetLeft:0}
                      ]},
                      {type: "block", list:[
                      {type:"input",offsetLeft:40,label:"地域：",name:"zone",/*validate:"NotEmpty",*/labelWidth:40},
                      {type:"newcolumn"},
                      {type:"label",label:"<span style='color: red;font-size:10px;'>*</span>",offsetLeft:0}
                      ]},
                      {type: "block", list:[
                      {type:"label",label:"<span style='color: red;font-size:10px;'>涉及清单数据安全，请按实际选择。</span>",offsetLeft:40}
                      ]},
                      {type: "block", list:[
                      {type:"input",offsetLeft:40,label:"部门：",name:"deptName",labelWidth:40},
                      {type:"newcolumn"},
                      {type:"label",label:"<span style='color: red;font-size:10px;'>*</span>",offsetLeft:0}
                      ]},
                      {type: "block", list:[
                      {type:"input",offsetLeft:40,label:"岗位：",name:"stationName",labelWidth:40},
                      {type:"newcolumn"},
                      {type:"label",label:"<span style='color: red;font-size:10px;'>*</span>",offsetLeft:0}
                      ]},
                      {type: "block", list:[
                      {type:"input",offsetLeft:40,label:"手机：",name:"userMobile",validate:"Mobile",labelWidth:40}
                      ]},
                      {type: "block", list:[
                      {type:"input",offsetLeft:40,label:"邮箱：",name:"userEmail",validate:"ValidEmail,MaxLength[64]",labelWidth:40},
                      {type:"newcolumn"},
                      {type:"label",label:"<span style='color: red;font-size:10px;'>*</span>",offsetLeft:0}
                      ]}, 
                  {type:"hidden",label:"userId"},
                  {type:"hidden",label:"actorName"},
                  {type:"hidden",name:"zoneId"},
                  {type:"hidden",name:"headShip"},
                  {type:"hidden",name:"groupId"},
                  {type:"hidden",name:"vipFlag"},
                  {type:"hidden",name:"oaUserName"},
                  {type:"hidden",name:"menuId"}
                  ]);
   
    var loadMenuParam = new biDwrMethodParam();//loadMenu Action参数设置。
    //定义loadMen Action的参数来源于表单queryform数据。
    loadMenuParam.setParamConfig([
        {
            index:0,type:"fun",value:function(){
            return systemId;
        }
        },{
            index:1,type:"static",value:9999999
        }
    ]);
    var modifyStyle = {
            add:"font-weight:bold;color:#64B201;",
            remove:"font-weight:bold;color:#CCCCCC",
            clear:"font-weight:normal;font-style::normal;text-decoration:none;color:black;border-bottom:0px;"
        };  
    dwrCaller.addAutoAction("loadMenu","UserManageAction.queryMenuTreeData",loadMenuParam,function(data){
        dhx.closeProgress();
        systemId= 421;
    });
    dwrCaller.addDataConverter("loadMenu",menuTreeDataConverter);
    var tree = modifyLayout.cells("b").attachTree();
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
    var add={}, remove={};
    /**
     * 当树选择一个checkbox框的数据处理与样式处理操作。
     * @param id
     */
    var whenTreeCheck=function(id){
    	var stateDeal=function(menuID){
            var menuData=tree.getUserData(menuID,"menuData");
            var state= tree._idpull[menuID].checkstate;
            if(menuData.checked==state){//如果原来的状态与现有状态吻合,则不算新增也不算移除
                add[menuID]=null;
                delete add[menuID];
                remove[menuID]=null;
                delete remove[menuID];
                tree.setItemStyle(menuID,modifyStyle.clear);
            }else{
                if(state){//算作新增
                    add[menuID]=menuData;
                    remove[menuID]=null;
                    delete remove[menuID];
                    tree.setItemStyle(menuID,modifyStyle.add);
                }else{//算作删除
                    add[menuID]=null;
                    delete add[menuID];
                    remove[menuID]=menuData;
                    tree.setItemStyle(menuID,modifyStyle.remove);
                }
            }
        }
        var scanIds=[id];
        //寻找ID所有子节点
        var subIds=tree.getAllSubItems(id);
        if(subIds){
            scanIds=scanIds.concat(subIds.split(","));
        }
        //加入所有父节点
        var parId=tree.getParentId(id);
        while(parId){
            scanIds.push(parId);
            parId=tree.getParentId(parId);
        }
        for(var i=0;i<scanIds.length;i++){
            stateDeal(scanIds[i]);
        }
    }
    tree.attachEvent("onCheck",function(id,state){
        whenTreeCheck(id);
    });
    var buttonForm = modifyLayout.cells("c").attachForm([
                                                         {type:"settings",position: "label-left",labelAlign:"right",inputWidth: 120,labelLeft:0 ,
                                                             inputLeft:0,offsetTop:0},
                                                         {type:"button",label:"提交",name:"save",value:"提交",offsetLeft:158,offsetTop:5},
                                                         {type:"newcolumn"},
                                                         {type:"button",label:"重置",name:"reset",value:"重置",offsetTop:5},
                                                         {type:"newcolumn"},
                                                         {type:"button",value:"关闭",name:"close",offsetTop:5}
                                                     ]);
    //添加验证
    applyForm.defaultValidateEvent();
    applyForm.setFormData({zoneId:""});
    applyForm.setFormData({actorName:actName});
    //加载部门树
   // loadDeptTree(null,applyForm);
    //加载地域树
    loadZoneTree(null,applyForm);
    //加载菜单树
    //loadMenuTreeChkBox(null,applyForm);
    var getData=function(){
        //获取新增的关联菜单。
        if(!Tools.isEmptyObject(add)){
        	var addMenus="";
            for(var key in add){
                addMenus=addMenus+key+',';
            }
        }
        //获取删除关联的菜单
        if(!Tools.isEmptyObject(remove)){
            var deleteMenus=[];
            for(var key in remove){
                deleteMenus.push({menuId:key,roleFlag:(remove[key].isRoleFrom==undefined||remove[key].isRoleFrom==null?0:remove[key].isRoleFrom)});
            }
            result.removeRefMenu=deleteMenus;
        }
        return addMenus;
    }
    /**
     * 添加 onButtonClick 事件
     */
    buttonForm.attachEvent("onButtonClick", function(id){
    	var validateAdd=function(){
    		var data = applyForm.getFormData();
    		var menuIds=getData();
    	        			if(data.userNameen==null||data.userNameen==''){
    	        				alert('请填写账号！');
    	        				return false;
    	        			}if(data.userNamecn==null||data.userNamecn==''){
    	        				alert("请填写姓名！");
    	        				return false;
    	        			}if(data.zone==null||data.zone==''){
    	        				alert("请选择地域！");
    	        				return false;
    	        			}if(data.deptName==null||data.deptName==''){
    	        				alert("请填写部门！");
    	        				return false;
    	        			}if(data.stationName==null||data.stationName==''){
    	        				alert("请填写岗位！");
    	        				return false;
    	        			}if(data.userEmail==null||data.userEmail==''){
    	        				alert("请填写邮箱！");
    	        				return false;
    	        			}if(menuIds==null||menuIds.split(',')==''){
    	        				alert("请选择需要申请的菜单权限！");
    	        				return false;
    	        			}
    	        			return true;
    	        		}
    	
    	if(id=="save"){//申请
    		var data = applyForm.getFormData();
    		var zoneID=data.zoneId;
    		if(applyForm.validate()&&validateAdd()){
    			if(zoneID==1){
    				applyForm.setFormData({state:"20"});//省公司用户
        		}else{
        			applyForm.setFormData({state:"2"});
        		}
    		var menuIds=getData();
    		dhx.confirm("是否按需提交申请？菜单权限以审核为准！",function(r){
                if(r){
                	applyForm.setFormData({menuId:menuIds});
                	applyForm.send(dwrCaller.applyUser); 
                }
            })
    		} 
        }else if (id=="reset"){//重置
            applyForm.clear();
            applyForm.setFormData({zone:globZoneTree.getItemText(""),zoneId:""});
        }else if(id=="close"){
        	applyWindow.close();
        }
    });
  //关闭Window事件
    applyWindow.attachEvent("onClose", function(win){
    	applyForm.clear();
        dhxWindow.unload();
    })
    /**
     * 注册申请用户事件
     */
    dwrCaller.addAction("applyUser", function(afterCall,param){
        UserManageAction.applyUser(param,function(data){
            if(data.type=="error"||data.type=="invalid"){
                dhx.alert("对不起，申请出错，请重试！");
            }else if(data.sid==-1){
                dhx.alert("申请出错，邮箱地址已存在！");
            }else if(data.sid==-2){
                dhx.alert("申请出错，姓名已存在！");
            }else if(data.sid==-3){
                dhx.alert("申请出错，账号已存在！");
            }else {
                dhx.alert("已申请，请留意审核状态！");
                applyWindow.close();
                mygrid.clearAll();
                mygrid.load(dwrCaller.loadUser, "json");
            }
        });
    });
}

/**
 * 修改用户
 * @param rowId
 */
var modifyUser=function(rowId){
	var rowData=mygrid.getRowData(rowId);//获取行数据
    var dhxWindow=new dhtmlXWindows();
    dhxWindow.createWindow("modifyWindow",0,0,520,300);
    var modifyWindow=dhxWindow.window("modifyWindow");
    modifyWindow.setModal(true);
    modifyWindow.stick();
    modifyWindow.setDimension(770,390);
    modifyWindow.center();
    modifyWindow.denyResize();
    modifyWindow.denyPark();
    modifyWindow.setText("修改用户:"+rowData.data[0]);
    modifyWindow.keepInViewport(true);
    //关闭一些不用的按钮。
    modifyWindow.button("minmax1").hide();
    modifyWindow.button("park").hide();
    modifyWindow.button("stick").hide();
    modifyWindow.button("sticked").hide();
    modifyWindow.show();
    var modifyLayout = new dhtmlXLayoutObject(modifyWindow,"3U");
    modifyLayout.cells("c").setHeight(20);
    modifyLayout.cells("c").fixSize(true, true);
    modifyLayout.cells("c").hideHeader();
    modifyLayout.cells("a").fixSize(true, true);
    modifyLayout.cells("a").hideHeader();
    modifyLayout.cells("b").fixSize(true, true);
    modifyLayout.cells("b").hideHeader();
    modifyLayout.hideSpliter();//移除分界边框。
    //建立表单。
    var modifyForm=modifyLayout.cells("a").attachForm([
    		                        {type: "block",offsetLeft:40, list:[
				                        {type:"input",label:"账号：",name:"userNameen",labelWidth:40},
    		                            {type:"newcolumn"},
    		                            {type:"label",label:"<span style='color: red'>*</span>",labelWidth:0}
    		                            ]},
    		                            {type: "block", list:[
    		                            {type:"label",label:"<span style='color: red;font-size:10px;'>建议与OA账号一致，可从OA单点登录系统。</span>",offsetLeft:40,labelWidth:0}
    		                            ]},
    		                            {type: "block",offsetLeft:40, list:[
    		                            {type:"input",label:"姓名：",name:"userNamecn",labelWidth:40},
    		                            {type:"newcolumn"},
    		                            {type:"label",label:"<span style='color: red'>*</span>",labelWidth:0}
    		                            ]},
    		                            {type: "block", offsetLeft:40,list:[
    		                            {type:"input",label:"地域：",name:"zone",labelWidth:40},
    		                            {type:"newcolumn"},
    		                            {type:"label",label:"<span style='color: red'>*</span>"}
    		                            ]},
    		                            {type: "block", list:[
    		                            {type:"label",label:"<span style='color: red;font-size:10px;'>涉及清单数据安全，请按实际选择。</span>",offsetLeft:40}
    		                            ]},
    		                            {type: "block",offsetLeft:40, list:[
    		                            {type:"input",label:"部门：",name:"deptName",labelWidth:40},
    		                            {type:"newcolumn"},
    		                            {type:"label",label:"<span style='color: red;font-size:10px;'>*</span>",offsetLeft:0}
    		                            ]},
    		                            {type: "block", offsetLeft:40,list:[
    		                            {type:"input",label:"手机：",name:"userMobile",validate:"Mobile",labelWidth:40}
    		                            ]},
    		                            {type: "block",offsetLeft:40, list:[
    		                            {type:"input",label:"岗位：",name:"stationName",labelWidth:40},
    		                            {type:"newcolumn"},
    		                            {type:"label",label:"<span style='color: red;font-size:10px;'>*</span>",offsetLeft:0}
    		                            ]},
    		                            {type: "block",offsetLeft:40, list:[
    		                            {type:"input",label:"邮箱：",name:"userEmail",validate:"ValidEmail",labelWidth:40},
    		                            {type:"newcolumn"},
    		                            {type:"label",label:"<span style='color: red;font-size:10px;'>*</span>",offsetLeft:0}
    		                            ]},
    		                        {type:"hidden",label:"userId"},
    		                        {type:"hidden",label:"actorName"},
    		                       // {type:"hidden",name:"deptId"},
    		                        {type:"hidden",name:"zoneId"}
    		                        //{type:"hidden",name:"stationId"}
    		                    ]);
    var loadMenuParam = new biDwrMethodParam();//loadMenu Action参数设置。
    //定义loadMen Action的参数来源于表单queryform数据。
    loadMenuParam.setParamConfig([
        {
            index:0,type:"fun",value:function(){
            return systemId;
        }
        },{
            index:1,type:"static",value:rowId
        }
    ]);   
    var modifyStyle = {
            add:"font-weight:bold;color:#64B201;",
            remove:"font-weight:bold;color:#CCCCCC",
            clear:"font-weight:normal;font-style::normal;text-decoration:none;color:black;border-bottom:0px;"
        };  
    dwrCaller.addAutoAction("loadMenu","UserManageAction.queryMenuTreeData",loadMenuParam,function(data){
        dhx.closeProgress();
        systemId= 421;
    });
    dwrCaller.addDataConverter("loadMenu",menuTreeDataConverter);
    var tree = modifyLayout.cells("b").attachTree();
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
    var add={}, remove={};//新增关联菜单数据、移除关联菜单数据。均以MENU_ID为主键，新增和删除的关联以menuData为value
    /**
     * 当树选择一个checkbox框的数据处理与样式处理操作。
     * @param id
     */
    var whenTreeCheck=function(id){
        /*var stateDeal=function(menuID){
            var menuData=tree.getUserData(menuID,"menuData");
            	add[menuID]=menuData;
            	tree.setItemStyle(menuID,modifyStyle.add);
        }
        var scanIds=[id];
        //寻找ID所有子节点
        var subIds=tree.getAllSubItems(id);
        if(subIds){
            scanIds=scanIds.concat(subIds.split(","));
        }
        //加入所有父节点
        var parId=tree.getParentId(id);
        while(parId){
            scanIds.push(parId);
            parId=tree.getParentId(parId);
        }
        for(var i=0;i<scanIds.length;i++){
            stateDeal(scanIds[i]);
        }*/
    	var stateDeal=function(menuID){
            var menuData=tree.getUserData(menuID,"menuData");
            var state= tree._idpull[menuID].checkstate;
            if(menuData.checked==state){//如果原来的状态与现有状态吻合,则不算新增也不算移除
                add[menuID]=null;
                delete add[menuID];
                remove[menuID]=null;
                delete remove[menuID];
                tree.setItemStyle(menuID,modifyStyle.clear);
            }else{
                if(state){//算作新增
                    add[menuID]=menuData;
                    remove[menuID]=null;
                    delete remove[menuID];
                    tree.setItemStyle(menuID,modifyStyle.add);
                }else{//算作删除
                    add[menuID]=null;
                    delete add[menuID];
                    remove[menuID]=menuData;
                    tree.setItemStyle(menuID,modifyStyle.remove);
                }
            }
        }
        var scanIds=[id];
        //寻找ID所有子节点
        var subIds=tree.getAllSubItems(id);
        if(subIds){
            scanIds=scanIds.concat(subIds.split(","));
        }
        //加入所有父节点
        var parId=tree.getParentId(id);
        while(parId){
            scanIds.push(parId);
            parId=tree.getParentId(parId);
        }
        for(var i=0;i<scanIds.length;i++){
            stateDeal(scanIds[i]);
        }
    }
    tree.attachEvent("onCheck",function(id,state){
        whenTreeCheck(id);
    	return false;
    });
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
    var buttonForm = modifyLayout.cells("c").attachForm([
                                                         {type:"settings",position: "label-left",labelAlign:"right",inputWidth: 120,labelLeft:0 ,
                                                             inputLeft:0,offsetTop:0},
                                                         /*{type:"button",label:"保存草稿",name:"save",value:"保存草稿",offsetLeft:208,offsetTop:5},
                          		                         {type:"newcolumn"},*/
                                                         {type:"button",label:"提交",name:"submit",value:"提交",offsetLeft:208,offsetTop:5},
                                                         {type:"newcolumn"},
                                                         {type:"button",label:"重置",name:"reset",value:"重置",offsetTop:5},
                                                         {type:"newcolumn"},
                                                         {type:"button",value:"关闭",name:"close",offsetTop:5}
                                                     ]);
  //添加验证
    modifyForm.defaultValidateEvent(); 
    var initForm=function(){
        var initData={
            userNamecn:rowData.data[1],//用户中文名
            userNameen:rowData.data[0],//用户拼音
            userMobile:rowData.data[13],//用户电话
            userEmail:rowData.data[12], //用户邮箱
            //userRoleId:userData.data[11],
            //createDate:userData.userdata.createDate, //建立日期
            //headShip:userData.data[9], //职务
            userId:rowData.id,
            autoRight:2,//默认手动添加权限
            vipFlag:0,
            deptName:rowData.data[3],//部门
            stationName:rowData.data[4]//岗位      
        }
        dhx.extend(initData,rowData.userdata);
        modifyForm.setFormData(initData);
    }
    //加载部门树
   // loadDeptTree(userData.userdata.deptId,modifyForm);
    //加载地域树
    var zoneId=rowData.data[11];
    loadZoneTree(zoneId,modifyForm);
    //加载岗位树
   // loadStationTree(userData.userdata.stationId,modifyForm);
    initForm();
    var getData=function(){
        var result={};
        //获取新增的关联菜单。
        if(!Tools.isEmptyObject(add)){
            var addMenus=[];
            for(var key in add){
                addMenus.push({menuId:key,roleFlag:(add[key].isRoleFrom==undefined||add[key].isRoleFrom==null?0:add[key].isRoleFrom)});
            }
            result.addRefMenu=addMenus;
        }
        //获取删除关联的菜单
        if(!Tools.isEmptyObject(remove)){
            var deleteMenus=[];
            for(var key in remove){
                deleteMenus.push({menuId:key,roleFlag:(remove[key].isRoleFrom==undefined||remove[key].isRoleFrom==null?0:remove[key].isRoleFrom)});
            }
            result.removeRefMenu=deleteMenus;
        }
        return result;
    }
    /**
     * 添加 onButtonClick 事件
     */
    var submit=function(data){
        dwrCaller.executeAction("insertRefMenu",data,rowId,function(rs){
            if(rs){
                //dhx.alert("菜单提交成功！");
                dhxWindow._removeWindowGlobal(modifyWindow);
                mygrid.clearAll();
                mygrid.load(dwrCaller.loadUser, "json");
            }else{
                dhx.alert("菜单提交失败！");
            }
        });
    }
    /**
     * 添加 onButtonClick 事件
     */
    buttonForm.attachEvent("onButtonClick",function(id){
    	var validateAdd=function(){
    		var data = modifyForm.getFormData();
    	        			if(data.userNameen==null||data.userNameen==''){
    	        				alert('请填写账号！');
    	        				return false;
    	        			}if(data.userNamecn==null||data.userNamecn==''){
    	        				alert("请填写姓名！");
    	        				return false;
    	        			}if(rowData.data[11]==null||rowData.data[11]==''){
    	        				alert("请选择地域！");
    	        				return false;
    	        			}if(data.deptName==null||data.deptName==''){
    	        				alert("请填写部门！");
    	        				return false;
    	        			}if(data.stationName==null||data.stationName==''){
    	        				alert("请填写岗位！");
    	        				return false;
    	        			}if(data.userEmail==null||data.userEmail==''){
    	        				alert("请填写邮箱！");
    	        				return false;
    	        			}
    	        			return true;
    	        		}
    	
        if(id=="save"){//保存
        	if(modifyForm.validate()&&validateAdd()){
        	var data=getData();
    		dhx.confirm("是否保存草稿？",function(r){
                if(r){
                	modifyForm.setFormData({state:"7"});
                	 submit(data);
                	 modifyForm.send(dwrCaller.saveUser); 
                }
            })
        	}
        }else if (id=="reset"){//重置
            modifyForm.clear();
            loadTreeData();
            initForm();
            modifyForm.setFormData({zone:globZoneTree.getItemText(rowData.userdata.zoneId),zoneId:rowData.userdata.zoneId});
            if(rowData.userdata.groupId != null){
                modifyForm.setFormData({groupId:rowData.userdata.groupId});
            }else{
                modifyForm.setFormData({groupId:""});
            }
        }else if(id=="close"){
            modifyWindow.close();
        }else if(id=="submit"){
        	var data = modifyForm.getFormData();
    		var zoneID=data.zoneId;
        	if(modifyForm.validate()&&validateAdd()){
        		modifyForm.setFormData({actorName:user['userNamecn']});
        		if(zoneID==1){
        			modifyForm.setFormData({state:"20"});//省公司用户
        		}else{
        	        modifyForm.setFormData({state:"2"});
        		}
        	var data=getData();
    		dhx.confirm("是否提交申请？",function(r){
                if(r){
                	 submit(data);
                	 modifyForm.send(dwrCaller.modifyUser); 
                }
            })
        	}
        }
    });
    //关闭Window事件
    modifyWindow.attachEvent("onClose", function(win){
        modifyForm.clear();
        dhxWindow.unload();
    })

    /**
     * 注册DWR
     */
    dwrCaller.addAction("modifyUser", function(afterCall,param){
        UserManageAction.modifyUser(param,function(data){
            if(data.type=="error"||data.type=="invalid"){
                dhx.alert("对不起，提交出错，请重试！");
            }else if(data.sid==-1){
                dhx.alert("提交失败，邮箱地址已存在！");
            }else if(data.sid==-2){
                dhx.alert("提交失败，用户名已存在！");
            }else if(data.sid==-3){
                dhx.alert("提交失败，OA登录名已存在！");
            }else{
                dhx.alert("已申请，请留意审核状态！");
                modifyWindow.close();
                //新增之后及时刷新页面
                mygrid.updateGrid(userDataConverter.convert(data.successData),"update");
            }
        })
    });
    dwrCaller.addAction("saveUser", function(afterCall,param){
        UserManageAction.modifyUser(param,function(data){
            if(data.type=="error"||data.type=="invalid"){
                dhx.alert("对不起，保存草稿出错，请重试！");
            }else if(data.sid==-1){
                dhx.alert("保存草稿失败，邮箱地址已存在！");
            }else if(data.sid==-2){
                dhx.alert("保存草稿失败，用户名已存在！");
            }else if(data.sid==-3){
                dhx.alert("保存草稿失败，OA登录名已存在！");
            }else{
                dhx.alert("保存草稿成功！");
                modifyWindow.close();
                //新增之后及时刷新页面
                mygrid.updateGrid(userDataConverter.convert(data.successData),"update");
            }
        })
    });
}
dwrCaller.addAutoAction("insertRefMenu", "UserManageAction.insertRefMenu",{
    dwrConfig:true,
    processMessage:"正在提交关联菜单数据，请稍后...",
    isDealOneParam:false
})
dwrCaller.addAutoAction("loadDeptTree","DeptAction.queryDeptByPath");
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptId",pidColumn:"parentId",
    isDycload:false,textColumn:"deptName"
});
dwrCaller.addDataConverter("loadDeptTree",treeConverter);
//树动态加载Action
dwrCaller.addAction("querySubDept",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},treeConverter,false);
    DeptAction.querySubDept(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
/**
 * 部门树input框Html。
 * @param selectDept 已经选择了的部门。
 */
var loadDeptTree=function(selectDept,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectDept=selectDept|| global.constant.defaultRoot;
    var beginId=1;
    //创建tree Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target=form.getInput("dept");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubDept);
    //树双击鼠标事件
    tree.attachEvent("onDblClick",function(nodeId){
        form.setFormData({dept:tree.getItemText(nodeId),deptId:nodeId});
        //关闭树
        div.style.display="none";
    });
    dwrCaller.executeAction("loadDeptTree",beginId,selectDept,function(data){
        tree.loadJSONObject(data);
        globDeptTree = tree;
        if(selectDept){
            tree.selectItem(selectDept); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    });
    div.style.display="none";
}

//为查询时可多选，新添加的TREE
var loadDeptTreeChkbox=function(selectDept,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectDept=selectDept|| global.constant.defaultRoot;
    var beginId=1;
    //创建tree Div层
    var div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 1px #eee solid;height: 190px;width: 180px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:'_deptDiv'
    });
    document.body.appendChild(div);
    //创建tree div
    var treeDiv = dhx.html.create("div", {
        style:"position:relative;height: 190px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    div.appendChild(treeDiv);
    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableCheckBoxes(true);
    tree.enableThreeStateCheckboxes(1);
    tree.enableHighlighting(true);
    tree.setDataMode("json");
    tree.attachEvent("onCheck",function(id,state){
        var checkedData = [];
        var allChecked = tree.getAllChecked();
        //寻找ID所有子节点

        var nodes = typeof allChecked == "string" ? allChecked.split(",") : [allChecked];
        var depts = "";
        var deptsId = "";
        for (i = 0;i < nodes.length; i++){
            var scanIds = typeof tree.getAllSubItems(nodes[i]) == "string" ? tree.getAllSubItems(nodes[i]).split(","): [tree.getAllSubItems(nodes[i])];
            if( scanIds.length > 1 ){
                for (j = 0; j< scanIds.length; j++){
                    if(depts == ""){
                        depts =  tree.getItemText(scanIds[j]).toString();
                    }else{

                        depts =   depts + "," +  tree.getItemText(scanIds[j]).toString() ;
                    }
                    if(deptsId == ""){
                        deptsId = scanIds[j].toString();
                    }else{
                        deptsId = deptsId   + "," + scanIds[j].toString();
                    }
                }
            }else{
                if(depts == ""){
                    depts =  tree.getItemText(nodes[i]).toString();
                }else{
                    depts =   depts + "," +  tree.getItemText(nodes[i]).toString() ;
                }
                if(deptsId == ""){
                    deptsId = nodes[i].toString();
                }else{
                    deptsId = deptsId   + "," + nodes[i].toString();
                }
            }



        }
        if(depts == 0){
            depts = "";
            deptsId = null;
        }

        form.setFormData({dept:depts,deptId:deptsId});
    });
    tree.attachEvent("onSelect",function(id){
        tree.setCheck(id,1);
        var checkedData = [];
        var allChecked = tree.getAllChecked();
        //寻找ID所有子节点

        var nodes = typeof allChecked == "string" ? allChecked.split(",") : [allChecked];
        var depts = "";
        var deptsId = "";
        for (i = 0;i < nodes.length; i++){
            var scanIds = typeof tree.getAllSubItems(nodes[i]) == "string" ? tree.getAllSubItems(nodes[i]).split(","): [tree.getAllSubItems(nodes[i])];
            if( scanIds.length > 1 ){
                for (j = 0; j< scanIds.length; j++){
                    if(depts == ""){
                        depts =  tree.getItemText(scanIds[j]).toString();
                    }else{

                        depts =   depts + "," +  tree.getItemText(scanIds[j]).toString() ;
                    }
                    if(deptsId == ""){
                        deptsId = scanIds[j].toString();
                    }else{
                        deptsId = deptsId   + "," + scanIds[j].toString();
                    }
                }
            }else{
                if(depts == ""){
                    depts =  tree.getItemText(nodes[i]).toString();
                }else{
                    depts =   depts + "," +  tree.getItemText(nodes[i]).toString() ;
                }
                if(deptsId == ""){
                    deptsId = nodes[i].toString();
                }else{
                    deptsId = deptsId   + "," + nodes[i].toString();
                }
            }
        }
        if(depts == 0){
            depts = "";
            deptsId = null;
        }

        form.setFormData({dept:depts,deptId:deptsId});
    });


    tree.setXMLAutoLoading(dwrCaller.querySubDept);
    var that = this;
    //移动节点位置至指定节点下。
    var target=form.getInput("dept");
    dwrCaller.executeAction("loadDeptTree",beginId,selectDept,function(data){
        tree.loadJSONObject(data);
        globDeptTree = tree;
        if(selectDept){
            tree.selectItem(selectDept); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    });
    div.style.display="none";
}

dwrCaller.addAutoAction("loadStationTree","StationAction.queryStationByPath");
var stationConverter=dhx.extend({idColumn:"stationId",pidColumn:"parStationId",
    textColumn:"stationName"
},treeConverter,false);
dwrCaller.addDataConverter("loadStationTree",stationConverter);
//树动态加载Action
dwrCaller.addAction("querySubStation",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},stationConverter,false);
    StationAction.querySubStation(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});

/**
 * 岗位input输入框Html
 * @param name
 * @param value
 */
var loadStationTree=function(selectStation,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectStation=selectStation|| global.constant.defaultRoot;
    var beginId=1;
    //创建tree Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target=form.getInput("station");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubStation);
    //树双击鼠标事件
    tree.attachEvent("onDblClick",function(nodeId){
        form.setFormData({station:tree.getItemText(nodeId),stationId:nodeId});
        //关闭树
        div.style.display="none";
    });
    dwrCaller.executeAction("loadStationTree",beginId,selectStation,function(data){
        tree.loadJSONObject(data);
        globStationTree = tree;
        if(selectStation){
            tree.selectItem(selectStation); //选中指定节点
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
//添加岗位可以多选
var loadStationTreeChkBox=function(selectStation,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectStation=selectStation|| global.constant.defaultRoot;
    var beginId=1;
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;width: 180px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:"_stationDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"display;none;position:relaive;height:200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    div.appendChild(treeDiv);
    //移动节点位置至指定节点下。
    var target=form.getInput("station");
    target.readOnly=true;
    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableCheckBoxes(true);
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.attachEvent("onCheck",function(id,state){
        var checkedData = tree.getAllChecked();
        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
        var stations = "";
        var stationsId ="";
        for (i = 0;i< nodes.length;i++){
            if(stations == ""){
                stations =  tree.getItemText(nodes[i]).toString();
            }else{
                stations =   stations + "," +  tree.getItemText(nodes[i]).toString() ;
            }
            if(stationsId == ""){
                stationsId = nodes[i].toString();
            }else{
                stationsId = stationsId   + "," + nodes[i].toString();
            }
        }
        if(stations == 0){
            stations = "";
            stationsId = null;
        }
        form.setFormData({station:stations,stationId:stationsId});
    });
    tree.attachEvent("onSelect",function(id){
        tree.setCheck(id,1);
        var checkedData = tree.getAllChecked();
        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
        var stations = "";
        var stationsId ="";
        for (i = 0;i< nodes.length;i++){
            if(stations == ""){
                stations =  tree.getItemText(nodes[i]).toString();
            }else{
                stations =   stations + "," +  tree.getItemText(nodes[i]).toString() ;
            }
            if(stationsId == ""){
                stationsId = nodes[i].toString();
            }else{
                stationsId = stationsId   + "," + nodes[i].toString();
            }
        }
        if(stations == 0){
            stations = "";
            stationsId = null;
        }
        form.setFormData({station:stations,stationId:stationsId});
    });


    tree.setXMLAutoLoading(dwrCaller.querySubStation);
    dwrCaller.executeAction("loadStationTree",beginId,selectStation,function(data){
        tree.loadJSONObject(data);
        globStationTree = tree;
        if(selectStation){
            tree.selectItem(selectStation); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    })
    div.style.display="none";
}


dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPath");
var zoneConverter=dhx.extend({idColumn:"zoneId",pidColumn:"zoneParId",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
//树动态加载Action
dwrCaller.addAction("querySubZone",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZone(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
//菜单树
dwrCaller.addAutoAction("loadMenuTree","ZoneAction.queryMenuByPath");
var menuConverter=dhx.extend({idColumn:"menuId",pidColumn:"parentId",
    textColumn:"menuName"
},treeConverter,false);
dwrCaller.addDataConverter("loadMenuTree",menuConverter);
//树动态加载Action
dwrCaller.addAction("querySubMenu",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},menuConverter,false);
    ZoneAction.querySubMenu(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
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
    tree.setXMLAutoLoading(dwrCaller.querySubZone);
    //树双击鼠标事件
    tree.attachEvent("onDblClick",function(nodeId){
        form.setFormData({zone:tree.getItemText(nodeId),zoneId:nodeId});
        //关闭树
        div.style.display="none";
    });
    dwrCaller.executeAction("loadZoneTree",beginId,selectZone,function(data){
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

var loadZoneTreeChkBox=function(selectZone,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectZone=selectZone|| global.constant.defaultRoot;
    var beginId=1;
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;width: 180px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:"_zoneDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height:200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;"
    });
    div.appendChild(treeDiv);
    //移动节点位置至指定节点下。
    var target=form.getInput("zone");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableCheckBoxes(true);
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubZone);
    tree.attachEvent("onCheck",function(id,state){
        var checkedData = tree.getAllChecked();
        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
        var zones = "";
        var zonesId ="";
        for (i = 0;i< nodes.length;i++){
            if(zones == ""){
                zones =  tree.getItemText(nodes[i]).toString();
            }else{
                zones =   zones + "," +  tree.getItemText(nodes[i]).toString() ;
            }
            if(zonesId == ""){
                zonesId = nodes[i].toString();
            }else{
                zonesId = zonesId   + "," + nodes[i].toString();
            }
        }
        if(zones == 0){
            zones = "";
            zonesId = null;
        }
        form.setFormData({zone:zones,zoneId:zonesId});
    });


    tree.attachEvent("onSelect",function(id){
        tree.setCheck(id,1);
        var checkedData = tree.getAllChecked();
        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
        var zones = "";
        var zonesId ="";
        for (i = 0;i< nodes.length;i++){
            if(zones == ""){
                zones =  tree.getItemText(nodes[i]).toString();
            }else{
                zones =   zones + "," +  tree.getItemText(nodes[i]).toString() ;
            }
            if(zonesId == ""){
                zonesId = nodes[i].toString();
            }else{
                zonesId = zonesId   + "," + nodes[i].toString();
            }
        }
        if(zones == 0){
            zones = "";
            zonesId = null;
        }
        form.setFormData({zone:zones,zoneId:zonesId});
    });
    dwrCaller.executeAction("loadZoneTree",beginId,selectZone,function(data){
        tree.loadJSONObject(data);
        globZoneTree = tree;
        if(selectZone){
            tree.selectItem(selectZone); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    })
    div.style.display="none";
}
var loadMenuTreeChkBox=function(selectMenu,form){
    //加载菜单树
    var beginId=1;
    selectMenu=selectMenu|| global.constant.defaultRoot;
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;width: 180px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:"_menuDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height:200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;"
    });
    div.appendChild(treeDiv);
    //移动节点位置至指定节点下。
    var target=form.getInput("menu");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableCheckBoxes(true);
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubMenu);
    tree.attachEvent("onCheck",function(id,state){
        var checkedData = tree.getAllChecked();
        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
        var menus = "";
        var menusId ="";
        for (i = 0;i< nodes.length;i++){
            if(menus == ""){
                menus =  tree.getItemText(nodes[i]).toString();
            }else{
                menus =   menus + "," +  tree.getItemText(nodes[i]).toString() ;
            }
            if(menusId == ""){
                menusId = nodes[i].toString();
            }else{
                menusId = menusId   + "," + nodes[i].toString();
            }
        }
        if(menus == 0){
            menus = "";
            menusId = null;
        }
        form.setFormData({menu:menus,menuId:menusId});
    });


    tree.attachEvent("onSelect",function(id){
        tree.setCheck(id,1);
        var checkedData = tree.getAllChecked();
        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
        var menus = "";
        var menusId ="";
        for (i = 0;i< nodes.length;i++){
            if(menus == ""){
                menus =  tree.getItemText(nodes[i]).toString();
            }else{
                menus =   menus + "," +  tree.getItemText(nodes[i]).toString() ;
            }
            if(menusId == ""){
                menusId = nodes[i].toString();
            }else{
                menusId = menusId   + "," + nodes[i].toString();
            }
        }
        if(menus == 0){
            menus = "";
            menusId = null;
        }
        form.setFormData({menu:menus,menuId:menusId});
    });
    dwrCaller.executeAction("loadMenuTree",beginId,selectMenu,function(data){
        tree.loadJSONObject(data);
        globMenuTree = tree;
        if(selectMenu){
            tree.selectItem(selectMenu); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    })
    div.style.display="none";
}

/**
 * 是否是VIP用户
 * @param name
 * @param value
 */
var isVipRadioTemplate=function(name,value){
    if(value==null||value==""){
        value=0;
    }
    //设置定时器，便于为radio设置事件响应函数,用于设置选中的值。
    setTimeout(function(){
        Tools.addEvent($("_isVipRadioTemplate1"),"click",function(){
            $("_isVipRadioTemplate1").parentNode.parentNode.parentNode._value=1;
        });
        Tools.addEvent($("_isVipRadioTemplate2"),"click",function(){
            $("_isVipRadioTemplate2").parentNode.parentNode.parentNode._value=0;
        });
    },100);
    return"<input type='radio' name='_isVipRadioTemplate' id='_isVipRadioTemplate1' value='1' "+(value==1?"checked":"")+">"
                  +"是&nbsp;&nbsp;&nbsp;<input type='radio' name='_isVipRadioTemplate' id='_isVipRadioTemplate2' value='0' "+(value==0?"checked":"")+">否";
}
function dump_obj(myObject) {  
	   var s = "";  
	   for (var property in myObject) {  
		   s = s + "\n "+property +": " + myObject[property] ;  
		  }  
		  alert(s);  
		} 
dhx.ready(userInit);