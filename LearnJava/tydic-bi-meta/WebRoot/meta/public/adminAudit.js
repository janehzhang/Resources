/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var loadUserParam=new biDwrMethodParam();//loaduser Action参数设置。
var userNamecn= userNamecn;
var systemId=421;//用户关联菜单默认的系统代码

/**
 * 定义全局的部门、岗位、地域树
 */
var globZoneTree = null;
/**
 * User表Data转换器
 */
var userConvertConfig = {
    idColumnName:"userId",
    filterColumns:["userNameen","userNamecn","zoneName","deptName","stationName","state","returnReason","createDate","actorName","_buttons","roleName","zoneId","userEmail","userMobile","roleId","userRoleId"],
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
        if(columnName == "_buttons"){//如果是操作按钮列
        	if(rowData.state=='管理员开通'){
	        	var   str ="";
	        	str +="<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=passUser("+rowData["userId"]+")>审核</a></span>";
	        	str +="<span style='padding-left:5px;'><a style='cursor:pointer;color:blue;'  onclick=returnUser("+rowData["userId"]+")>退回</a></span>";
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
        queryRole:{methodName:"MenuAction.queryRoleData1"
            ,converter:new dhtmlxComboDataConverter({
                valueColumn:"roleId",textColumn:"roleName",isAdd:true
            })},
    update:function(afterCall, param){}
});
dwrCaller.addAutoAction("returnBack","UserManageAction.returnBack");//主单退回
/**
 * 用户页面初始化方法
 */
var userInit=function(){
    var userLayout = new dhtmlXLayoutObject(document.body,"2E");
    userLayout.cells("a").setText("管理员审核");
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
            /*{type:"combo",label:"状态：",name:"userState",inputWidth: 150,
                className:"combo",readonly:"readonly",options:[
                {value:"-1",text:"全部",selected:true}
            ]} ,
            {type:"newcolumn"},*/
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
    //给form 表单的  userState 的combo 对象赋值
    /*var userStateData = getComboByRemoveValue("APPLY_STATE");
    queryform.getCombo("userState").addOption(userStateData.options);

    queryform.getCombo("userState").readonly(true,false);
    queryform.getCombo("userState").attachEvent("onBlur",function(){
        queryform.getCombo("userState").closeAll();
    });*/

    if(userNamecn != null){
        queryform.setValue("userName",userNamecn);
    }
    var stateTypeData = getComboByRemoveValue("APPLY_STATE");
    queryform.getCombo("state").addOption(stateTypeData.options); 
    /*queryform.setFormData( {
		"userState" : "20"
	});*/
    queryform.defaultValidateEvent();
    //定义loadUser Action的参数来源于表单queryform数据。
    loadUserParam.setParamConfig([{
        index:0,type:"fun",value:function(){
            var data=queryform.getFormData();
            if(data.zoneId == null || data.zoneId ==""){
                data.zoneId=user.zoneId.toString();
            }
            return data;
        }
    }]);
    loadUserParam.setParamConfig([
        {
            index:0,type:"fun",value:function(){
            var formData=queryform.getFormData();
            if(formData.zoneId == null || formData.zoneId ==""){
                formData.zoneId=user.zoneId.toString();
            }
            formData.userName=Tools.trim(queryform.getInput("userName").value);
            return formData;   //结果为map,与caller联合使用,在dwr的调用方法中,方法中的参数与这里对应
        }
        }
    ]);


    //加载部门树
   // loadDeptTreeChkbox(null,queryform);
    //加载地域树
    loadZoneTreeChkBox(user.zoneId,queryform);
    //加载岗位树
   // loadStationTreeChkBox(null,queryform);
  
    dwrCaller.addAutoAction("loadUser", "UserManageAction.queryUser", loadUserParam,function(data){});
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
 * 审核用户
 * @param rowId
 */
var passUser=function(rowId){
    var rowData=mygrid.getRowData(rowId);//获取行数据
    var dhxWindow=new dhtmlXWindows();
    dhxWindow.createWindow("modifyWindow",0,0,520,300);
    var modifyWindow=dhxWindow.window("modifyWindow");
    modifyWindow.setModal(true);
    modifyWindow.stick();
    modifyWindow.setDimension(770,450);
    modifyWindow.center();
    modifyWindow.denyResize();
    modifyWindow.denyPark();
    modifyWindow.setText("审核用户:"+rowData.data[0]);
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
           											{type: "block", offsetLeft:40,list:[
                                                                             {type:"input",label:"账号：",name:"userNameen",labelWidth:40},
                                                                             {type:"newcolumn"},
                                                                             {type:"label",label:"<span style='color: red'>*</span>",labelWidth:0}
                                                                             ]},
                                                                             {type: "block", list:[
                                                                             {type:"label",label:"<span style='color: red;font-size:10px;'>建议与OA账号一致，可从OA单点登录系统。</span>",offsetLeft:40,labelWidth:0}
                                                                             ]},
                                                                             {type: "block", offsetLeft:40,list:[
                                                                             {type:"input",label:"姓名：",name:"userNamecn",labelWidth:40},
                                                                             {type:"newcolumn"},
                                                                             {type:"label",label:"<span style='color: red'>*</span>",labelWidth:0}
                                                                             ]},
                                                                             {type: "block", offsetLeft:40,list:[
                                                                             {type:"input",label:"地域：",name:"zone",labelWidth:40},
                                                                             {type:"newcolumn"},
                                                                             {type:"label",label:"<span style='color: red'>*</span>",labelWidth:0}
                                                                             ]},
                                                                             {type: "block", list:[
                                                                             {type:"label",label:"<span style='color: red;font-size:10px;'>涉及清单数据安全，请按实际选择。</span>",offsetLeft:40}
                                                                             ]},
                                                                             {type: "block", offsetLeft:40,list:[
                                                                             {type:"input",label:"部门：",name:"deptName",labelWidth:40},
                                                                             {type:"newcolumn"},
                                                                             {type:"label",label:"<span style='color: red;font-size:10px;'>*</span>",offsetLeft:0}
                                                                             ]},
                                                                             {type: "block", offsetLeft:40,list:[
                                                                             {type:"input",label:"岗位：",name:"stationName",labelWidth:40},
                                                                             {type:"newcolumn"},
                                                                             {type:"label",label:"<span style='color: red;font-size:10px;'>*</span>",offsetLeft:0}
                                                                             ]},
                                                                             {type: "block",offsetLeft:40, list:[
                                                                             {type:"input",label:"手机：",name:"userMobile",validate:"Mobile",labelWidth:40}
                                                                             ]},
                                                                             {type: "block",offsetLeft:40, list:[
                                                                             {type:"input",label:"邮箱：",name:"userEmail",validate:"ValidEmail",labelWidth:40},
                                                                             {type:"newcolumn"},
                                                                             {type:"label",label:"<span style='color: red;font-size:10px;'>*</span>",offsetLeft:0}
                                                                             ]},
                                                                             {type: "block",offsetLeft:30, list:[
                                                                           {type:"combo",label:"工作台：",inputWidth: 120,name:"userRoleId",options:[{value:"213543",text:"管理层"},{value:"213542",text:"决策层"},{value:"213544",text:"执行层"}]},
                                                                           {type:"newcolumn"},
                                                                           {type:"label",label:"<span style='color: red'>*</span>",labelWidth:0}
                                                                           ]},
                                                                           {type: "block",offsetLeft:40, list:[
                                                                           {type:"combo",label:"角色：",inputWidth: 120,name:"roleId",className:'queryItem',labelWidth:40,readonly:"readonly"},
                                                                           {type:"newcolumn"},
                                                                           {type:"label",label:"<span style='color: red'>*</span><span id='_belongRoleLabel' style='font-size:12px;color:#000;font-weight:bold;'></span>"},
                                                                           {type:"newcolumn"},
                                                                           {type:"button",label:"按角色授权",inputWidth: 100,name:"queryMenu",value:"按角色授权"}
                                                                           ]},
                                                                           {type: "block", list:[
                                                                           {type:"label",label:"<span style='color: red;font-size:10px;'>通过角色进行系统权限授权，如角色权限不足，可先申请角色。</span>",offsetLeft:40}
                                                                           ]}, 
                                                                         {type:"hidden",label:"userId"},
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
            index:1,type:"static",value:rowId
        }
    ]);
    
    
    var loadRoleMenuParam = new biDwrMethodParam();//loadMenu Action参数设置。
    //定义loadMen Action的参数来源于表单queryform数据。
    loadRoleMenuParam.setParamConfig([
        {
            index:0,type:"fun",value:function(){
            return systemId;
        }
        },{
            index:1,type:"fun",value:function(){
            return modifyForm.getItemValue("roleId");
        }
        }
    ]);
    dwrCaller.addAutoAction("loadRoleMenu","UserManageAction.loadRoleMenu",loadRoleMenuParam,function(data){
        dhx.closeProgress();
        systemId= 421;
    });
    dwrCaller.addDataConverter("loadRoleMenu",menuTreeDataConverter);
    
    var loadRoleMenuParam1 = new biDwrMethodParam();//loadMenu Action参数设置。
    //定义loadMen Action的参数来源于表单queryform数据。
    loadRoleMenuParam1.setParamConfig([
        {
            index:0,type:"fun",value:function(){
            return systemId;
        }
        },{
            index:1,type:"fun",value:function(){
            return rowData.data[14];
        }
        }
    ]);
    dwrCaller.addAutoAction("loadRoleMenu1","UserManageAction.loadRoleMenu",loadRoleMenuParam1,function(data){
        dhx.closeProgress();
        systemId= 421;
    });
    dwrCaller.addDataConverter("loadRoleMenu1",menuTreeDataConverter);
    
    
    
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
    var loadRoleTreeData=function(){
        var childs = tree.getSubItems(0);
        if(childs){
            var childIds = (childs + "").split(",");
            for(var i = 0; i < childIds.length; i++){
                tree.deleteItem(childIds[i]);
            }
        }
        tree.loadJSON(dwrCaller.loadRoleMenu)
}
    var loadRoleTreeData1=function(){
        var childs = tree.getSubItems(0);
        if(childs){
            var childIds = (childs + "").split(",");
            for(var i = 0; i < childIds.length; i++){
                tree.deleteItem(childIds[i]);
            }
        }
        tree.loadJSON(dwrCaller.loadRoleMenu1)
}
    loadRoleTreeData1(); 
    var add={};
    /**
     * 当树选择一个checkbox框的数据处理与样式处理操作。
     * @param id
     */
    var whenTreeCheck=function(id){
    	var stateDeal=function(menuID){
            var menuData=tree.getUserData(menuID,"menuData");
            var state= tree._idpull[menuID].checkstate;
            if(menuData.checked!=state){//如果原来的状态与现有状态吻合,则不算新增也不算移除
            	if(state!=null){
            	 if(state){//算作新增
                     //tree.setItemStyle(menuID,modifyStyle.remove);
                     tree.setCheck(menuID,0);
                 }else{//算作删除
                     //tree.setItemStyle(menuID,modifyStyle.add);
                     tree.setCheck(menuID,1);
                 }
            	}else{
            		tree.setCheck(menuID,0);
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
                                                          {type:"button",label:"通过",name:"save",value:"通过",offsetLeft:158,offsetTop:5},
                                                          {type:"newcolumn"},
                                                          {type:"button",label:"重置",name:"reset",value:"重置",offsetTop:5},
                                                          {type:"newcolumn"},
                                                          {type:"button",value:"关闭",name:"close",offsetTop:5}
                                                      ]);
    //添加验证
    modifyForm.defaultValidateEvent();
    if(rowData.data[15] == null){
    	rowData.data[15]='213543';
    }
    //添加验证
    var initForm=function(){
        var initData={
        		userNamecn:rowData.data[1],//用户中文名
                userNameen:rowData.data[0],//用户拼音
                userMobile:rowData.data[13],//用户电话
                userEmail:rowData.data[12], //用户邮箱
                userId:rowData.id,
                vipFlag:rowData.userdata.vipFlag,
                state:20,
                roleId:rowData.data[14],//关联角色
                userRoleId:rowData.data[15],//所属系统
                deptName:rowData.data[3],//部门
                stationName:rowData.data[4]//岗位     
        }
        dhx.extend(initData,rowData.userdata);
        modifyForm.setFormData(initData);
    }
    //初始化表单数据
    modifyForm.getCombo("roleId").readonly(true,false);
    modifyForm.getCombo("roleId").attachEvent("onBlur",function(){
        modifyForm.getCombo("roleId").closeAll();
        return false;
    });
    modifyForm.getCombo("roleId").loadXML(dwrCaller.queryRole, function(){
        if(rowData.data[14] != null){
            modifyForm.setFormData({roleId:rowData.data[14]});
        }else{
            modifyForm.setFormData({roleId:""});
        }
    });
    //加载地域树
    loadZoneTree(rowData.userdata.zoneId,modifyForm);
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
    buttonForm.attachEvent("onButtonClick", function(id){
    	var validateAdd=function(){
    		var data = modifyForm.getFormData();
    	        			if(data.userNameen==null||data.userNameen==''){
    	        				alert('请填写账号！');
    	        				return false;
    	        			}if(data.userNamecn==null||data.userNamecn==''){
    	        				alert("请填写姓名！");
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
    	
    	if(id=="save"){//通过
    		if(modifyForm.validate()&&validateAdd()){
    			modifyForm.setFormData({state:"11"});
	    		//var data=getData();
	    		dhx.confirm("是否审批通过账号权限？",function(r){
                if(r){
                	// submit(data);
                	 modifyForm.send(dwrCaller.passUser); 
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
        }
    });
    modifyForm.attachEvent("onButtonClick", function(id){  
    	 if(id=="queryMenu"){
        	loadRoleTreeData();
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
    dwrCaller.addAction("passUser", function(afterCall,param){
        UserManageAction.passUser(param,function(data){
            if(data.type=="error"||data.type=="invalid"){
                dhx.alert("对不起，审核出错，请重试！");
            }else if(data.sid==-1){
                dhx.alert("审核失败，邮箱地址已存在！");
            }else if(data.sid==-2){
                dhx.alert("审核失败，用户名已存在！");
            }else if(data.sid==-3){
                dhx.alert("审核失败，OA登录名已存在！");
            }else{
                dhx.alert("审核成功！");
                modifyWindow.close();
                dhxWindow.unload();
                mygrid.clearAll();
                mygrid.load(dwrCaller.loadUser, "json");
            }
        })
    });
}
dwrCaller.addAutoAction("insertRefMenu", "UserManageAction.insertRefMenu",{
    dwrConfig:true,
    processMessage:"正在提交关联菜单数据，请稍后...",
    isDealOneParam:false
})


var returnUser=function(userId){
	var rowData = mygrid.getRowData(userId);//获取行数据
    //初始化处理弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("returnWindow", 0, 0, 350, 1000);
    var returnWindow = dhxWindow.window("returnWindow");
    returnWindow.setModal(true);
    returnWindow.stick();
    returnWindow.setDimension(550, 300);
    returnWindow.center();
    returnWindow.setPosition(returnWindow.getPosition()[0],returnWindow.getPosition()[1]-50);
    returnWindow.denyResize();
    returnWindow.denyPark();
    returnWindow.button("minmax1").hide();
    returnWindow.button("park").hide();
    returnWindow.button("stick").hide();
    returnWindow.button("sticked").hide();
    returnWindow.setText("退回申请");
    returnWindow.keepInViewport(true);
    returnWindow.show(); 
        returnForm = returnWindow.attachForm( [ 
        {type : "newcolumn"},
        {type:"input",offsetLeft:20,rows:5,label:"退回原因：",inputWidth: 400,name:"returnReason",validate:"NotEmpty,MaxLength[200]" }, 
        {type:"newcolumn"},
        {type:"block",offsetTop:10,list:[
        {type:"button",label:"退回：",name:"returnBack",value:"退回",offsetLeft:200},
        {type:"newcolumn"},
        {type:"button",label:"关闭",name:"shutup",value:"关闭"},
        {type:"hidden",label:"actorName"}
        ]}
        ]);
	//派送响应函数
	returnForm.setFormData( {
		"userId" : userId,
		"state" : 20 //省公司审核
	});
	returnForm.setFormData({actorName:user['userNamecn']});
	returnForm.defaultValidateEvent();
	returnForm.attachEvent("onButtonClick", function(id) {
		 var data = returnForm.getFormData();
		 var validateAdd=function(){
				if(data.returnReason.length>200){
	 				alert("退回原因字数不能超过200");
	 				return  false;
	 			}
				return true;
			}
		if(returnForm.validate()&&validateAdd()){
	    if (id == "returnBack") {
	    	dhx.confirm("是否确认退回申请？",function(r){
                if(r){
                	 //保存
        	        dwrCaller.executeAction("returnBack",data,function(data){
        	            if(data.type == "error" || data.type == "invalid"){
        	                dhx.alert("对不起，处理失败，请重试！");
        	            }
        	            else{
        	            	dhx.alert("退回成功！");
        	            	returnWindow.close();
                            dhxWindow.unload();
                            mygrid.clearAll();
                            mygrid.load(dwrCaller.loadUser, "json");
        	            }
        	        });
                }
     	       
	    	})
	}
		}
		if(id == "shutup"){
			returnWindow.close();
            dhxWindow.unload();
        }
		
	});
}

var buttonConvertConfig={
    filterColumns:["checked","name","enName"],
    isFormatColumn:false,
    /**
     * 此函数目的将格式为类似为 addButton:新增，delete；删除.格式化为列数据
     * @param data 固定为数组，索引位0表示选中的menu已经注册的按钮，格式为： addButton:新增，delete；删除
     * 索引位1表示已经排除的按钮列表，用“,”号分割。
     */
    onBeforeConverted:function(data){
        if(data){
            var pageButton=data[0];
            this.exclude=data[1];
            this.menuData=data[2];
            if(pageButton){
                var buttons=pageButton.split(",");
                var list=[];
                for(var i=0;i<buttons.length;i++){
                    var temp=buttons[i].split(":");
                    list.push({enName:temp[0],name:temp[1]});
                }
                return list;
            }
        }
        this.exclude=undefined;
        this.menuData=undefined;
        return Tools.EmptyList;
    },
    userData:function(){
        return this.menuData;
    }
}
var buttonConverter=new dhtmxGridDataConverter(buttonConvertConfig);
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
    	alert(0);
        form.setFormData({dept:tree.getItemText(nodeId),deptId:nodeId});
        alert(nodeId);
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
    var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)
            || global.constant.defaultRoot;
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
    var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)
            || global.constant.defaultRoot;
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
    //加载部门树数据。加载用户所在部门及其子部门。
    selectMenu=1;
    var beginId=1;
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
dhx.ready(userInit);