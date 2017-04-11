/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        dept.js
 *Description：
 *       部门查询
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        刘斌
 *Finished：
 *       2011-09-29-14-35
 *Modified By：
 *		 程钰
 *Modified Date:
 *       2011-10-08-09-30
 *Modified Reasons:
 * 		 添加部门下属用户

 * Modified By:
 *       熊小平
 *Modified Date:
 *       2011-10-10-16-30
 *Modified Reasons:
 * 		 添加部门查询时的条件传递
 ********************************************************/
/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var loadDeptParam = new biDwrMethodParam();//loadDept Action参数设置。
/**
 * 树Data转换器定义
 */
var convertConfig = {
	idColumn : "deptId",
	pidColumn : "parentId",
	filterColumns : [ "deptName", "deptState", "_buttons" ],
	isDycload : true,
	/**
	 * 列转义，设置第二列的值固定为getRoleButtons，此为操作列，这个值为一个JS函数，用于获取下拉button的值。
	 * @param rowIndex
	 * @param columnIndex   特别说明，如果实现了filterColumns（）方法，这里的columnIndex是filterColumns后的index
	 * @param cellValue
	 * @param rowData
	 * @return
	 */
	cellDataFormat : function(rowIndex, columnIndex, columnName, cellValue,
			rowData) {
		if (columnName == "_buttons") {//如果是第二列。即操作按钮列
			return "getDeptButtons";
		}
		return this._super.cellDataFormat(rowIndex, columnIndex, columnName,
				cellValue, rowData);
	}
}
/**
 * JS内部类 用于数据转换
 */
var deptTreeDataConverter = new dhtmlxTreeGridDataConverter(convertConfig);
/**
 * User表Data转换器
 */
var userConvertConfig = {
	idColumnName : "userId",
	filterColumns : [ "userNamecn", "userNameen", "userMobile",
			"userEmail", "zoneName", "deptName", "stationName", "headShip" ],
	/**
	 * 覆盖父类方法：数据转换
	 * @param rowIndex
	 * @param rowData
	 */
	userData : function(rowIndex, rowData) {
		var userData = {};
		if (rowData["createDate"] != null) {
			userData["createDate"] = rowData["createDate"];
		} else {
			userData["createDate"] = "";
		}
		if (rowData["oaUserName"] != null) {
			userData["oaUserName"] = rowData["oaUserName"];
		} else {
			userData["oaUserName"] = "";
		}
		return userData;
	}

}
/**
 * JS内部类 用于数据转换
 */
var userDataConverter = new dhtmxGridDataConverter(userConvertConfig);

/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller( {
	querySubDept : {
		methodName : "DeptAction.querySubDept",
		converter : deptTreeDataConverter
	},
	update : function(afterCall, param) {
	}
});


loadDepttreeDataConverter = dhx.extend( {}, deptTreeDataConverter);
loadDepttreeDataConverter.setDycload(false);


/**
 * 初始化页面加载方法
 */
var deptInit = function() {
	var base = getBasePath();
	//button详细定义
	var buttons = {
		refUser : {
			name : "refUser",
			text : "下属用户",
			imgEnabled : base + "/meta/resource/images/users.png",
			imgDisabled : base + "/meta/resource/images/users.png",
			onclick : function(rowData) {
				if (dhx.isArray(rowData)) {
					return;
				}
				refUser(rowData.id);
			}
		}
	};
	//按钮权限列表;
	//TODO 按钮权限过滤,checkRule.
	
	var buttonDept = [ "refUser" ];
//    var buttonDept = getRoleButtons();

	//定义全局函数，用于获取有权限的button列表定义
	window["getDeptButtons"] = function() {
		var res = [];
		for ( var i = 0; i < buttonDept.length; i++) {
			res.push(buttons[buttonDept[i]]);
		}
		return res;
	};

	var deptLayout = new dhtmlXLayoutObject(document.body, "2E");
	deptLayout.cells("a").setText("部门查询");
	deptLayout.cells("b").hideHeader();
	deptLayout.cells("a").setHeight(80);
	deptLayout.cells("a").fixSize(false, true);
    deptLayout.hideSpliter();//移除分界边框。
	deptLayout.hideConcentrate();
	//加载查询表单
	var queryform = deptLayout.cells("a").attachForm( [ {
        type : "setting",
        position : "label-left",
        labelWidth : 120,
        inputWidth : 120
    }, {
        type : "input",
        label : "部门名称：",
        name : "deptName",
        inputHeight : 17
    }, {
        type : "newcolumn"
    }, {
        type : "button",
        name : "query",
        value : "查询",
        offsetLeft :0,
        offsetTop : 0
    }, {type:"hidden", name:"template"}

    ]);

	//定义loadRole Action的参数来源于表单queryform数据。
	loadDeptParam.setParamConfig( [ {
		index : 0,
		type : "fun",
		value : function() {
			var formData=queryform.getFormData();
            formData.deptName=Tools.trim(queryform.getInput("deptName").value);
            return formData;
		}
	} ]);
	dwrCaller.addAutoAction("loadDept", "DeptAction.queryDeptTreeData",
		loadDeptParam, function(data) {
		});
	dwrCaller.addDataConverter("loadDept", loadDepttreeDataConverter);
	var pos = 1;
	var filterButton = window["getDeptButtons"]();
	//加载部门列表
	mygrid = deptLayout.cells("b").attachGrid();
	mygrid.setImagePath(dhtmlx.image_path + "csh_" + dhtmlx.skin + "/");
	mygrid.setHeader("部门名称,状态,操作");
    mygrid.setHeaderBold();
	mygrid.setInitWidthsP("70,15,15");
	mygrid.setColAlign("left,center,center");
	mygrid.setHeaderAlign("left,center,center");
	mygrid.setColTypes("tree,ro,sb");
    mygrid.enableCtrlC();
	mygrid.setColSorting("na,na,na");
	mygrid.enableTreeGridLines();
	mygrid.setEditable(false);
	mygrid.enableMultiselect(true);
	mygrid.setColumnCustFormat(1, validOrNot);//第二列进行转义
	mygrid.setColumnIds("deptName,deptState,target");
	mygrid.init();
	mygrid.load(dwrCaller.loadDept, "json");
	//mygrid.defualtPaging(16);
	mygrid.kidsXmlFile = dwrCaller.querySubDept;

	//查询表单事件处理
	queryform.attachEvent("onButtonClick", function(id) {
		if (id == "query") {
			//进行数据查询。
			mygrid.clearAll();
			mygrid.load(dwrCaller.loadDept, "json");
		}
	});
	// Enter查询事件
    queryform.getInput("deptName").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            mygrid.clearAll();
			mygrid.load(dwrCaller.loadDept, "json");
        }
    }

    /**
     *工具栏按钮事件处理中转
     *@param buttonId 按钮的名称
     *@param rowData 当前行的数据
     */
    var buttonEventHandel = function(buttonId, rowData){
        if(buttons[buttonId].onclick){
            buttons[buttonId].onclick(rowData.length == 1 ? rowData[0] : rowData);
        }
    }//end

    //右键菜单
//    var menu = new dhtmlXMenuObject();
//    menu.renderAsContextMenu();
//    menu.attachEvent("onClick", function(menuItemId){
//        var data = mygrid.contextID.split("_");
//        //rowInd_colInd;
//        var rowId = data[0];
//        var rowData = mygrid.getRowData(rowId);
//        buttonEventHandel(menuItemId, rowData);
//        //选中contextmenu指定行
//        mygrid.doClick(mygrid.cells(data[0],data[1]).cell,true);
//        return true;
//    })
//    var pos = 0;
//    for(var i = 0; i < filterButton.length; i++){
//        menu.addNewChild(null, pos++, filterButton[i].name, filterButton[i].text, false, filterButton[i].imgEnabled,
//            filterButton[i].imgDisabled);
//    }
//
//    mygrid.enableContextMenu(menu);

}
/**
 * 菜单下属用户.
 * @param rowData
 */
var refUser = function(rowData) {
    var tempData = mygrid.getRowData(rowData);//获取行数据
	var loadUserParam = new biDwrMethodParam();
	var dhxWindow = new dhtmlXWindows();
	dhxWindow.createWindow("refUserWindow", 0, 0, 550,380);
	var refUserWindow = dhxWindow.window("refUserWindow");
	refUserWindow.setModal(true);
	//refUserWindow.stick();
	refUserWindow.setDimension(550,380);
	refUserWindow.center();
	refUserWindow.denyResize();
	refUserWindow.denyPark();
	refUserWindow.setText(tempData.data[0]+'</span>'+'<span style="font-weight:normal;">下属用户</span>');
	//refUserWindow.hideHeader();
	refUserWindow.keepInViewport(true);
    //关闭一些不用的按钮。
    refUserWindow.button("minmax1").hide();
    refUserWindow.button("park").hide();
    refUserWindow.button("stick").hide();
    refUserWindow.button("sticked").hide();
	refUserWindow.show();
	var refUserLayout = new dhtmlXLayoutObject(refUserWindow, "2E");
	//refUserLayout.cells("a").setText("查询条件");
	//refUserLayout.cells("b").setText("用户列表");
	refUserLayout.cells("a").hideHeader();
	refUserLayout.cells("b").hideHeader();
	refUserLayout.cells("a").setHeight(50);
	refUserLayout.cells("a").fixSize(false, true);
    refUserLayout.hideSpliter();//移除分界边框。
	refUserLayout.hideConcentrate();
	//加载查询表单
	var queryform = refUserLayout.cells("a").attachForm( [ {
		type : "setting",
		position : "label-left",
		labelWidth : 120,
		inputWidth : 120
	}, {
		type : "input",
		label : "姓名：",
		name : "userName"
	}, {
		type : "newcolumn"
	}, {
		type : "hidden",
		name : "userDept",
		value : rowData
	}, {
		type : "button",
		name : "query",
		value : "查询"
	} ]);
	//定义loadUser Action的参数来源于表单queryform数据。
	loadUserParam.setParamConfig( [ {
		index : 0,
		type : "fun",
		value : function() {
			var formData=queryform.getFormData();
            formData.userName=Tools.trim(queryform.getInput("userName").value);
            return formData;
		}
	} ]);
	//添加加载用户事件
	dwrCaller.addAutoAction("loadRefUser", "DeptAction.queryRefUser",
			loadUserParam, function(data) {
				//            alert(data);
		});
	dwrCaller.addDataConverter("loadRefUser", userDataConverter);
	//
	//用户列表
	var refUserGrid = refUserLayout.cells("b").attachGrid();
	refUserGrid.setHeader("姓名,拼音,手机,邮箱,地域,部门,岗位,职务");
	refUserGrid.setInitWidthsP("10,10,10,26,10,10,15,10");
	refUserGrid
			.setColAlign("left,left,center,left,left,left,left,left");
	refUserGrid
			.setHeaderAlign("left,center,center,center,center,center,center,center");
    refUserGrid.setHeaderBold();
	refUserGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro");
    refUserGrid.enableCtrlC();
	refUserGrid.setColSorting("na,na,na,na,na,na,na,na");
	refUserGrid.enableMultiselect(true);
	refUserGrid
			.setColumnIds("userNamecn,userNameen,userMobile,userEmail,zone,dept,station,headShip");
	refUserGrid.init();
    refUserGrid.defaultPaging(10);
	refUserGrid.load(dwrCaller.loadRefUser, "json");
	//查询表单事件处理
	queryform.attachEvent("onButtonClick", function(id) {
		if (id == "query") {
			//进行数据查询。
			refUserGrid.clearAll();
			refUserGrid.load(dwrCaller.loadRefUser, "json");
		}
	});
	// Enter查询事件
    queryform.getInput("userName").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            refUserGrid.clearAll();
			refUserGrid.load(dwrCaller.loadRefUser, "json");
        }
    }

}

dhx.ready(deptInit);