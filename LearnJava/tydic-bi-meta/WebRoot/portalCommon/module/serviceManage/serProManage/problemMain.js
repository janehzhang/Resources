//确认
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
//DWR
var dwrCaller = new biDwrCaller();
var queryServiceProblemParams = new biDwrMethodParam();//查询参数
var user= getSessionAttribute("user");
var queryform;
/**
 * 初始化函数
 */
var ServiceProblemInit = function() {
	//第一步，先建立一个Layout
	var logLayout = new dhtmlXLayoutObject(document.body, "2E");
	logLayout.cells("a").setText("服务问题单待处理");
	logLayout.cells("b").hideHeader();
	logLayout.cells("a").setHeight(75);
	logLayout.cells("a").fixSize(false, true);
	logLayout.hideConcentrate();
	logLayout.hideSpliter();

	//添加查询表单
    queryform = logLayout.cells("a").attachForm( [ {
        type : "setting",
        position : "label-left"
        },
        {type : "calendar",
            label : "日期从：",
            name : "startDate",
            dateFormat : "%Y-%m-%d",
            weekStart : "7",
            value: firstDay(),
            inputWidth: "80",
            readonly:"readonly"
        },
        {type : "newcolumn"},
        {type : "calendar",
            label : "至：",
            name : "endDate",
            dateFormat : "%Y-%m-%d",
            weekStart : "7",
            value : GetDateStr(1),
            inputWidth: "80",
            readonly:"readonly"
        },
        {type : "newcolumn"},
        {type : "input",label : "发起部门：",name : "createDeptName",inputWidth: "80"},
        {type : "newcolumn"},
        {type : "input",label : "主题：",name : "theme",inputWidth: "100"},
        {type : "newcolumn"},
        {type : "combo",label : "来源：",name : "source",options:[{value:"",text:"全部",selected:true}],inputWidth:80,inputHeight:22,readonly:true},
        {type:"newcolumn"},
        {type : "combo",label : "状态：",name : "problemStep",options:[{value:"",text:"全部",selected:true}],inputWidth:80,inputHeight:22,readonly:true},
        {type : "newcolumn"},
        {
            type : "button",
            name : "query",
            value : "查询",
            offsetLeft:10
        }
        ]);
    //创建下拉列表
    var tableTypeData = getComboByRemoveValue("PROBLEM_SOURCE");
    queryform.getCombo("source").addOption(tableTypeData.options); 
    var tableStepData = getComboByRemoveValue("PROBLEM_STEP");
    queryform.getCombo("problemStep").addOption(tableStepData.options);
    
	var startDate = queryform.getCalendar("startDate");
	var endDate = queryform.getCalendar("endDate");
	
	//将日历控件语言设置成中文
	startDate.loadUserLanguage('zh');
	endDate.loadUserLanguage('zh');

	//将未来的日期设定为不可操作
	var today = new Date();
	var tomarrow = new Date();
	tomarrow.setDate(tomarrow.getDate() + 1);
	startDate.setInsensitiveRange(tomarrow, null);
	endDate.setInsensitiveRange(tomarrow, null);

	//日历改变事件，使startDate不晚于endDate，endDate不早于startDate
	startDate.attachEvent("onClick", function(date) {
		endDate.setSensitiveRange(date, today);
	});
	endDate.attachEvent("onClick", function(date) {
		date.setDate(date.getDate()+1);
		startDate.setInsensitiveRange(date, null);
	});
	
	/**
	 * 查询问题的响应函数
	 * @param {Object} data
	 */
	dwrCaller.addAutoAction("queryProblem", "SerProManageAction.queryProblem",
			queryServiceProblemParams, function(data) {
			});
	dwrCaller.addDataConverter("queryProblem", new dhtmxGridDataConverter( {
		idColumnName : "mainProblemId",
		filterColumns : ["theme", "problemDescription", "source", "urgencyDegree","processLimited", "problemType", "createTime","createActorName","isEvaluationPlan",
				"attachmentName","mainState","attachmentAddress","returnReason","acceptActorName","acceptActorId","createActorId","problemSolution","plannedFinishTime","evaluationAttachment","evaluationAttachmentName","firstAcceptActorId"],
		cellDataFormat : function(rowIndex, columnIndex, columnName, cellValue,
				rowData) {
     		if (columnName == "mainState") {//操作按钮列	
     			 if(cellValue==1){ 
					if(rowData.acceptActorId==user.userId){
						if(rowData.problemSolution!=null){
							var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"queryEvaluationDetail('"+rowData.mainProblemId+"')\" >评估计划</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"returnServiceProblemInfo('"+rowData.mainProblemId+"')\" >退&nbsp;回</a>"+
							"&nbsp;&nbsp; <a href=\"javascript:void(0)\"  onclick=\"dealMain('"+rowData.mainProblemId+"')\" >处&nbsp;理</a>";
							return str;
						}else{
							var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"returnServiceProblemInfo('"+rowData.mainProblemId+"')\" >退&nbsp;回</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"dealMain('"+rowData.mainProblemId+"')\" >处&nbsp;理</a>";
							return str;
						}
					}else{
						var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>";
						return str;
					}
				}else if(cellValue==2){
					if(rowData.acceptActorId==user.userId){
					   if(rowData.problemSolution!=null){
							var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"queryEvaluationDetail('"+rowData.mainProblemId+"')\" >评估计划</a>" +
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"dealVice('"+rowData.mainProblemId+"')\" >处&nbsp;理</a>";
							return str;
						}else{
							var str ="<a href=\"javascript:void(0)\"  onclick=\"dealVice('"+rowData.mainProblemId+"')\" >处&nbsp;理</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>";
							return str;
						}
					}else if(rowData.acceptActorId!=user.userId){
						var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>";
						return str;
					}
				}else if(cellValue==3){
					if(rowData.createActorId==user.userId){
						if(rowData.problemSolution!=null){
							var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"queryEvaluationDetail('"+rowData.mainProblemId+"')\" >评估计划</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"queryAffirmEvaluationReport('"+rowData.mainProblemId+"')\" >评估报告</a>";
							return str;
						}else{
							var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"dealAffirm('"+rowData.mainProblemId+"')\" >确&nbsp;认</a>";
							return str;
						}
					}else{
						var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>";
						return str;
					}
				}else if(cellValue==4){
					if(rowData.createActorId==user.userId){
						if(rowData.problemSolution!=null){
							var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"queryEvaluationDetail('"+rowData.mainProblemId+"')\" >评估计划</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"queryReturnReason('"+rowData.mainProblemId+"')\" >被退原因</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"dealDispatch('"+rowData.mainProblemId+"')\" >派&nbsp;送</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"delServiceProblem('"+rowData.mainProblemId+"')\" >删&nbsp;除</a>";
							return str;
						}else{
							var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>"+
						    "&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"queryReturnReason('"+rowData.mainProblemId+"')\" >被退原因</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"dealDispatch('"+rowData.mainProblemId+"')\" >派&nbsp;送</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"delServiceProblem('"+rowData.mainProblemId+"')\" >删&nbsp;除</a>";
							return str;
						}
					}else{
						var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>";
						return str;
					}
				}else if(cellValue==5){
					if(rowData.problemSolution!=null){
						var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>"+
						"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"queryEvaluationDetail('"+rowData.mainProblemId+"')\" >评估计划</a>"+
						"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"queryFileEvaluationReport('"+rowData.mainProblemId+"')\" >评估报告</a>";
						return str;
					}else{
						var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>";
						return str;
					}
				}else if(cellValue==6){
					if(rowData.createActorId==user.userId){
						if(rowData.problemSolution!=null){
							var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"queryEvaluationDetail('"+rowData.mainProblemId+"')\" >评估计划</a>"+
							"&nbsp;&nbsp;<a href=\"javascript:void(0)\"  onclick=\"queryAffirmEvaluationReport('"+rowData.mainProblemId+"')\" >待评估</a>";
							return str;
						}else{
							var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>"
							return str;
						}
					}else{
						var str ="<a href=\"javascript:void(0)\"  onclick=\"queryServiceProblemDetailInfoByID('"+rowData.mainProblemId+"')\" >查看详情</a>";
						return str;
					}
				}
			}else if(columnName=="attachmentName"){
				if(cellValue!="无"){
				var str ="<a href=\"javascript:void(0)\"  onclick=\"loadAttachmentButton('"+rowData.attachmentAddress+"')\" >"+cellValue+"</a>";
	    		return str;
			   }
		  }
				return this._super.cellDataFormat(rowIndex, columnIndex,
						columnName, cellValue, rowData);
		}

	}));
	//设置查询参数，来自于queryform表单
	queryServiceProblemParams.setParamConfig( [ {
		index : 0,
		type : "fun",
		value : function() {
			return queryform.getFormData();
		}
	} ]);
	
	//加载部门树
    loadDeptTree(1,queryform);
	//按钮添加
/*	var buttons = {
		addServiceProblemInfo:{name:"addServiceProblemInfo",text:"新增",imgEnabled:base + "/meta/resource/images/addGroup.png",
            imgDisabled:base + "/meta/resource/images/addGroup.png",onclick:function(rowData) {
		    addServiceProblemInfo();
        }}
	};
	var buttonRole = [ "addServiceProblemInfo" ];
    //过滤显示顶部按钮
    var bottonRoleRow = [];
    for(var i=0; i<buttonRole.length; i++){
        if(buttonRole[i] == "addServiceProblemInfo"){
            bottonRoleRow.push(buttonRole[i]);
        }
    }
    //定义全局函数，用于获取有权限的button列表定义
    window["getButtons"] = function() {
        var res = [];
        for(var i=0;i<bottonRoleRow.length;i++){
            res.push(buttons[bottonRoleRow[i]]);
        }
        return res;
    };
    var buttonToolBar = logLayout.cells("b").attachToolbar();
    var pos = 1;
    var filterButton = window["getButtons"]();
    for (var i = 0; i < filterButton.length; i++) {
        buttonToolBar.addButton(filterButton[i].name, pos++, filterButton[i].text,
                filterButton[i].imgEnabled, filterButton[i].imgDisabled);
        var button=buttonToolBar.getItemNodeById(filterButton[i].name);
        button.setAttribute("id",filterButton[i].name);
    }
    
    //添加buttonToolBar事件
    buttonToolBar.attachEvent("onclick", function(id) {
        if(id=="addServiceProblemInfo"){
        	addServiceProblemInfo();
        }
    })*/
	//添加grid,用于将查询出来的日志记录显示出来
	logGrid = logLayout.cells("b").attachGrid();
	logGrid.setHeader("主题,问题描述,来源,紧急程度,处理时限,问题类别,建单时间,建单人,评估计划,参考资料,操作");
	logGrid.setInitWidthsP("8,10,8,6,7,6,12,7,6,9,21");
	logGrid.setColAlign("left,left,left,left,right,left,right,left,left,left,left");
	logGrid.setHeaderAlign("center,center,center,center,center,center,center,center,center,center,center");
	logGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
    logGrid.enableCtrlC();
	logGrid.setColSorting("na,na,na,na,na,na,na,na,na,na,na");
	logGrid.setEditable(false);
	logGrid.setColumnIds("theme,problemDescription,source,urgencyDegree,processLimited,problemType,createTime,createActor,isEvaluationPlan,attachmentName,mainState");
	logGrid.enableTooltips("true,true,true,true,true,true,true,true,true,true,true");
	logGrid.init();
    logGrid.defaultPaging(20);
	logGrid.load(dwrCaller.queryProblem, "json");
	//查询响应函数
	queryform.attachEvent("onButtonClick", function(id) {
		if (id == "query") {
			logGrid.clearAll();
			logGrid.load(dwrCaller.queryProblem, "json");
		}
	});	
};
//注册添加系统Action
dwrCaller.addAutoAction("nextDealServiceProblem","SerProManageAction.nextDealServiceProblem");//主单处理
dwrCaller.addAutoAction("nextReturnServiceProblem","SerProManageAction.nextReturnServiceProblem");//主单退回
dwrCaller.addAutoAction("nextViceServiceProblem","SerProManageAction.nextDealServiceProblem");//副单处理
dwrCaller.addAutoAction("insertServiceProblem","SerProManageAction.insertServiceProblem");//新增
dwrCaller.addAutoAction("loadServiceProblem","SerProManageAction.loadServiceProblem");//附件下载
dwrCaller.addAutoAction("affirmServiceProblem","SerProManageAction.affirmServiceProblem");//确认
dwrCaller.addAutoAction("sendServiceProblem","SerProManageAction.sendServiceProblem");//被退回再次派送
dwrCaller.addAutoAction("delServiceProblem","SerProManageAction.delServiceProblem");//被退回删除
dwrCaller.addAutoAction("satisfiedEvaluationReport","SerProManageAction.satisfiedEvaluationReport");//评估满意度
//确认
var dealAffirm=function(mainProblemId){
//处理数据
	queryform.setFormData( {
		"mainProblemId" : mainProblemId,
		"dealActorId":user.userId,
        "dealActorName":user.userNamecn,
        "dealDeptId":user.deptId,
        "dealDeptName":user.deptName
	});
	        var data = queryform.getFormData();
	        dhx.confirm("确认归档该记录吗？", function(r){
	            if(r){
	        //保存
	        dwrCaller.executeAction("affirmServiceProblem",data,function(data){
	            if(data.type == "error" || data.type == "invalid"){
	                dhx.alert("对不起，处理失败，请重试！");
	            }
	            else{
	            	dhx.alert("确认成功");
	            	logGrid.clearAll();
                    logGrid.load(dwrCaller.queryProblem, "json");
	            }
	        });
	  }
	        })
	}
/**
 * 查看被退回原因
 */
var queryReturnReason=function(mainProblemId){
	var rowData = logGrid.getRowData(mainProblemId);//获取行数据
    //初始化处理弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("reasonWindow", 0, 0, 350, 500);
    var reasonWindow = dhxWindow.window("reasonWindow");
    reasonWindow.setModal(true);
    reasonWindow.stick();
    reasonWindow.setDimension(350, 200);
    reasonWindow.center();
    reasonWindow.setPosition(reasonWindow.getPosition()[0],reasonWindow.getPosition()[1]-50);
    reasonWindow.denyResize();
    reasonWindow.denyPark();
    reasonWindow.button("minmax1").hide();
    reasonWindow.button("park").hide();
    reasonWindow.button("stick").hide();
    reasonWindow.button("sticked").hide();
    reasonWindow.setText("查看被退原因");
    reasonWindow.keepInViewport(true);
    reasonWindow.show(); 
    queryReturnReasonForm = reasonWindow.attachForm( [ 
            {type:"label",offsetLeft:40,label:"被退原因："+rowData.data[12],name:"returnReason"},
            {type:"label",offsetLeft:40,label:"退单人："+rowData.data[13],name:"acceptActorName"},
            {type:"button",label:"关闭",offsetLeft:130,name:"close",value:"关闭"}
        ]);
    queryReturnReasonForm.attachEvent("onButtonClick", function(id) {
        if(id == "close"){
        	reasonWindow.close();
            dhxWindow.unload();
        }
    });
}
/**
 * 查看评估计划
 */
var queryEvaluationDetail=function(mainProblemId){
	var rowData = logGrid.getRowData(mainProblemId);//获取行数据
    //初始化处理弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("evaluationWindow", 0, 0, 350, 1000);
    var evaluationWindow = dhxWindow.window("evaluationWindow");
    evaluationWindow.setModal(true);
    evaluationWindow.stick();
    evaluationWindow.setDimension(450, 200);
    evaluationWindow.center();
    evaluationWindow.setPosition(evaluationWindow.getPosition()[0],evaluationWindow.getPosition()[1]-50);
    evaluationWindow.denyResize();
    evaluationWindow.denyPark();
    evaluationWindow.button("minmax1").hide();
    evaluationWindow.button("park").hide();
    evaluationWindow.button("stick").hide();
    evaluationWindow.button("sticked").hide();
    evaluationWindow.setText("查看评估计划");
    evaluationWindow.keepInViewport(true);
    evaluationWindow.show(); 
    queryEvaluationDetailForm = evaluationWindow.attachForm( [ 
		{type:"label",offsetLeft:20,rows:10,label:"问题解决方案："+rowData.data[16],name:"problemSolution"},
		{type:"label",offsetLeft:20,label:"计划完成时间："+rowData.data[17],name:"plannedFinishTime"},
		{type:"label",offsetLeft:20,label:"链接附件：<a href=\"#\" onclick=\"downloadattrs('"+rowData.data[18]+"')\">"+rowData.data[19]+"</a>",  labelWidth:370},
		{type:"button",label:"关闭",offsetLeft:200,name:"close",value:"关闭"}
        ]);
    queryEvaluationDetailForm.attachEvent("onButtonClick", function(id) {
        if(id == "close"){
        	evaluationWindow.close();
            dhxWindow.unload();
        }
    });
}

//待评估
var queryAffirmEvaluationReport=function(mainProblemId){
	    var rowData = logGrid.getRowData(mainProblemId);//获取行数据
		var dhxWindow = new dhtmlXWindows();
		dhxWindow.createWindow("visitDetailInfoWindow", 0, 0, 1250, 380);
		var visitDetailInfoWindow = dhxWindow.window("visitDetailInfoWindow");
		visitDetailInfoWindow.setModal(true);
		visitDetailInfoWindow.setDimension(410, 380);
		visitDetailInfoWindow.center();
		visitDetailInfoWindow.denyResize();
		visitDetailInfoWindow.denyPark();
		visitDetailInfoWindow.setText("评估报告列表");
		visitDetailInfoWindow.keepInViewport(true);
		visitDetailInfoWindow.button("minmax1").hide();
		visitDetailInfoWindow.button("park").hide();
		visitDetailInfoWindow.button("stick").hide();
		visitDetailInfoWindow.button("sticked").hide();
		visitDetailInfoWindow.show();
		var visitDetailInfoLayout = new dhtmlXLayoutObject(visitDetailInfoWindow,"2E");
		visitDetailInfoLayout.cells("a").hideHeader();
		visitDetailInfoLayout.cells("a").fixSize(false, true);
		visitDetailInfoLayout.cells("b").setText("评估报告满意度调查");
		visitDetailInfoLayout.hideConcentrate();
		//添加满意度调查表单
		   queryEvaluationInfoByIdForm = visitDetailInfoLayout.cells("b").attachForm( [ 
		        {type : "newcolumn"},
		        {type : "combo",label : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;是否满意评估报告：",name : "isSatisfiedEvaluation",options:[{value:"0",text:"是",selected:true},{value:"1",text:"否"}],inputWidth:90,inputHeight:12,readonly:true},
		        {type:"hidden",name:"dealActorId",value:user.userId},
		        {type:"hidden",name:"dealActorName",value:user.userNamecn},
		        {type:"hidden",name:"dealDeptId",value:user.deptId},
		        {type:"hidden",name:"dealDeptName",value:user.deptName},
		        {type : "newcolumn"},
		        {
		            type : "button",
		            name : "satisfied",
		            value : "确认",
		            offsetLeft:10
		        },
		        {type : "newcolumn"}, 
				{type:"button",label:"关闭",offsetLeft:10,name:"close",value:"关闭"}
		        ]);
			var queryEvaluationInfoByIdParams = new biDwrMethodParam();//按ID查询详细访问信息参数
			dwrCaller.addAutoAction("queryEvaluationInfoById",
					"SerProManageAction.queryEvaluationInfoById", queryEvaluationInfoByIdParams,
					function(data) {
					});
			dwrCaller.addDataConverter("queryEvaluationInfoById",
					new dhtmxGridDataConverter( {
						idColumnName : "dealProblemId",
						filterColumns : [ "evaluationDetail","dealActorName","dealTime"],
						cellDataFormat : function(rowIndex, columnIndex, columnName, cellValue,
								rowData) {
						if(columnName == "dealTime"){
							return utcToDate(cellValue);
						}
						    return this._super.cellDataFormat(rowIndex, columnIndex,
										columnName, cellValue, rowData);
						}
					}));
			queryEvaluationInfoByIdParams.setParamConfig( [ {
					index : 0,
					type : "fun",
					value : function() {
					     var data=queryEvaluationInfoByIdForm.getFormData();
					     data.mainProblemId=mainProblemId;
					     data.acceptActorId=rowData.data[20];
					     data.nextActorId=rowData.data[20];
						return data;
					}
			} ]);
		//详细访问信息列表
		var visitDetailInfoGrid = visitDetailInfoLayout.cells("a").attachGrid();
		visitDetailInfoGrid.setHeader("评估报告,评估人,评估时间");
		visitDetailInfoGrid.setInitWidthsP("50,25,25");
		visitDetailInfoGrid.setColAlign("left,left,left");
		visitDetailInfoGrid
				.setHeaderAlign("center,center,center");
		visitDetailInfoGrid.setColTypes("ro,ro,ro");
	    visitDetailInfoGrid.enableCtrlC();
		visitDetailInfoGrid.setColSorting("na,na,na");
		visitDetailInfoGrid.enableMultiselect(false);
		visitDetailInfoGrid
				.setColumnIds("evaluationDetail,dealActorName,dealTime");
		visitDetailInfoGrid.init();
		visitDetailInfoGrid.load(dwrCaller.queryEvaluationInfoById, "json");
		visitDetailInfoGrid.defaultPaging(20);
		queryEvaluationInfoByIdForm.attachEvent("onButtonClick", function(id) {
		        if(id == "close"){
		        	visitDetailInfoWindow.close();
		            dhxWindow.unload();
		        }
		        if(id=="satisfied"){
		        	  dwrCaller.executeAction("satisfiedEvaluationReport",queryEvaluationInfoByIdParams,function(dataMain){//满意度处理
                          if(dataMain.type == "error" || dataMain.type == "invalid"){
                              dhx.alert("对不起，满意度处理出错，请重试！");
                          }
                          else{
                              dhx.alert("满意度处理成功");
                              visitDetailInfoWindow.close();
                              dhxWindow.unload();
                              logGrid.clearAll();
                              logGrid.load(dwrCaller.queryProblem, "json");
                          }
                      })
		        }
		    });
}
//归档_查看评估报告
var queryFileEvaluationReport=function(mainProblemId){
	var queryEvaluationInfoByIdParams = new biDwrMethodParam();//按ID查询详细访问信息参数
	dwrCaller.addAutoAction("queryEvaluationInfoById",
			"SerProManageAction.queryEvaluationInfoById", queryEvaluationInfoByIdParams,
			function(data) {
			});
	dwrCaller.addDataConverter("queryEvaluationInfoById",
			new dhtmxGridDataConverter( {
				idColumnName : "dealProblemId",
				filterColumns : [ "evaluationDetail","dealActorName","dealTime"],
				cellDataFormat : function(rowIndex, columnIndex, columnName, cellValue,
						rowData) {
				if(columnName == "dealTime"){
					return utcToDate(cellValue);
				}
						return this._super.cellDataFormat(rowIndex, columnIndex,
								columnName, cellValue, rowData);
				}
			}));
	queryEvaluationInfoByIdParams.setParamConfig( [ {
			index : 0,
			type : "fun",
			value : function() {
			     var data=queryform.getFormData();
			     data.mainProblemId=mainProblemId;
				return data;
			}
	} ]);
		var dhxWindow = new dhtmlXWindows();
		dhxWindow.createWindow("visitDetailInfoWindow", 0, 0, 1250, 380);
		var visitDetailInfoWindow = dhxWindow.window("visitDetailInfoWindow");
		visitDetailInfoWindow.setModal(true);
		visitDetailInfoWindow.setDimension(510, 280);
		visitDetailInfoWindow.center();
		visitDetailInfoWindow.denyResize();
		visitDetailInfoWindow.denyPark();
		visitDetailInfoWindow.setText("查看评估报告");
		visitDetailInfoWindow.keepInViewport(true);
		visitDetailInfoWindow.button("minmax1").hide();
		visitDetailInfoWindow.button("park").hide();
		visitDetailInfoWindow.button("stick").hide();
		visitDetailInfoWindow.button("sticked").hide();
		visitDetailInfoWindow.show();
		var visitDetailInfoLayout = new dhtmlXLayoutObject(visitDetailInfoWindow,"1C");
		visitDetailInfoLayout.cells("a").hideHeader();
		visitDetailInfoLayout.cells("a").fixSize(false, true);
		visitDetailInfoLayout.hideConcentrate();
		//详细访问信息列表
		var visitDetailInfoGrid = visitDetailInfoLayout.cells("a").attachGrid();
		visitDetailInfoGrid.setHeader("评估报告,评估人,评估时间");
		visitDetailInfoGrid.setInitWidthsP("50,25,25");
		visitDetailInfoGrid.setColAlign("left,left,left");
		visitDetailInfoGrid
				.setHeaderAlign("center,center,center");
		visitDetailInfoGrid.setColTypes("ro,ro,ro");
	    visitDetailInfoGrid.enableCtrlC();
		visitDetailInfoGrid.setColSorting("na,na,na");
		visitDetailInfoGrid.enableMultiselect(false);
		visitDetailInfoGrid
				.setColumnIds("evaluationDetail,dealActorName,dealTime");
		visitDetailInfoGrid.init();
		visitDetailInfoGrid.load(dwrCaller.queryEvaluationInfoById, "json");
		visitDetailInfoGrid.defaultPaging(20);
}
/**
 * 派送
 */
var dealDispatch=function(mainProblemId){
    //初始化处理弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("dealDispatchWindow", 0, 0, 350, 1000);
    var dealDispatchWindow = dhxWindow.window("dealDispatchWindow");
    dealDispatchWindow.setModal(true);
    dealDispatchWindow.stick();
    dealDispatchWindow.setDimension(400, 250);
    dealDispatchWindow.center();
    dealDispatchWindow.setPosition(dealDispatchWindow.getPosition()[0],dealDispatchWindow.getPosition()[1]-50);
    dealDispatchWindow.denyResize();
    dealDispatchWindow.denyPark();
    dealDispatchWindow.button("minmax1").hide();
    dealDispatchWindow.button("park").hide();
    dealDispatchWindow.button("stick").hide();
    dealDispatchWindow.button("sticked").hide();
    dealDispatchWindow.setText("再次派送服务问题单");
    dealDispatchWindow.keepInViewport(true);
    dealDispatchWindow.show(); 
    dealDispatchForm = dealDispatchWindow.attachForm( [ 
        {type : "input",offsetLeft:50,label : "人员选择：",name : "acceptActorName",inputWidth: "150",validate:"NotEmpty" },
        {type:"hidden",name:"dealActorId",value:user.userId},
        {type:"hidden",name:"dealActorName",value:user.userNamecn},
        {type:"hidden",name:"dealDeptId",value:user.deptId},
        {type:"hidden",name:"dealDeptName",value:user.deptName},
        {type:"combo",offsetLeft:50,label:"短信通知：",inputWidth: 150,name:"isSMSNotice",readonly:true},
        //{type : "input",offsetLeft:50,label : "联系电话：",name : "acceptActorNo",inputWidth: "150"},
        {type:"block",offsetTop:10,list:[
        {
            type : "button",
            name : "nextDeal",
            value : "确认",
            offsetLeft:120
        },
        {type:"newcolumn"},
        {type:"button",label:"关闭",name:"close",value:"关闭",offsetLeft:10}
        ]}
        ]);
    var isSMSNoticeData = getComboByRemoveValue("IS_BOOL");//是否短信通知
    dealDispatchForm.getCombo("isSMSNotice").addOption(isSMSNoticeData.options);
    
    //loadDeptTreeNext("",dealDispatchForm);
  //加载人员选择树     
    var target=dealDispatchForm.getInput("acceptActorName");
     Tools.addEvent(target,"click",function(){
     	         var url=base+"/meta/module/mag/selectedUser/ryxz.jsp";
 				var obj= window.showModalDialog(url+'?t='+Math.random(),'newWin','center:yes;status:no;scroll:no;help:no;dialogWidth=800px;dialogHeight=500px');
 				if (typeof(obj) != "undefined") { 
 				       target.value=obj.names;
 				       dealDispatchForm.setFormData({acceptActorName:obj.names});
 				       dealDispatchForm.setFormData({acceptActorId:obj.ids});
 				       dealDispatchForm.setFormData({acceptDeptId:obj.deptIds});
 				       dealDispatchForm.setFormData({acceptDeptName:obj.deptNames});
 				       
 				}
         });
	//派送响应函数
	dealDispatchForm.setFormData( {
		"mainProblemId" : mainProblemId
	});
	
	dealDispatchForm.defaultValidateEvent();
	dealDispatchForm.attachEvent("onButtonClick", function(id) {
		var data = dealDispatchForm.getFormData();
		/*var validateAdd=function(){
			if(data.isSMSNotice==1){
    			if(data.acceptActorNo==null){
    				alert('请填写联系电话');
    				return false;
    			}if(isNaN(data.acceptActorNo)){
    				alert("联系电话输入有误(号码为数字)");
    				return false;
    			}
    		}
			return true;
		}*/
		if(dealDispatchForm.validate){//()&&validateAdd()
	    if (id == "nextDeal") {	        
	        //处理
	        dwrCaller.executeAction("sendServiceProblem",data,function(data){
	            if(data.type == "error" || data.type == "invalid"){
	                dhx.alert("对不起，派送失败，请重试！");
	            }
	            else{
	            	dhx.alert("派送成功");
	            	dealDispatchWindow.close();
                    dhxWindow.unload();
                    logGrid.clearAll();
                    logGrid.load(dwrCaller.queryProblem, "json");
	            }
	        })
	}
		}
		if(id == "close"){
    		dealDispatchWindow.close();
            dhxWindow.unload();
        }
	});
	
}
/**
 * 删除
 * @param dataId
 */
var delServiceProblem=function(dataId){
    dhx.confirm("确定要删除该记录吗？", function(r){
        if(r){
            dwrCaller.executeAction("delServiceProblem",dataId,function(data){
                if(data.type == "error" || data.type == "invalid"){
                    dhx.alert("对不起，删除失败，请重试！");
                }else{
                    dhx.alert("删除成功！");
                    var dataArray = [];
                    for (var i = 0; i < data.length; i ++) {
                        dataArray.push({id:data[i].sid});
                    }
                    logGrid.updateGrid(dataArray, "delete");
                    logGrid.clearAll();
                    logGrid.load(dwrCaller.queryProblem, "json");
                }
            });
        }
    })
}
/**
 * 新增服务问题单
 */
var addServiceProblemInfo=function(){
	//初始化新增弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("addWindow", 0, 0, 350, 1000);
    var addWindow = dhxWindow.window("addWindow");
    addWindow.setModal(true);
    addWindow.stick();
    addWindow.setDimension(550, 700);
    addWindow.center();
    addWindow.setPosition(addWindow.getPosition()[0],addWindow.getPosition()[1]-50);
    addWindow.denyResize();
    addWindow.denyPark();
    addWindow.button("minmax1").hide();
    addWindow.button("park").hide();
    addWindow.button("stick").hide();
    addWindow.button("sticked").hide();
    addWindow.setText("新增服务问题单");
    addWindow.keepInViewport(true);
    addWindow.show();    
    /**
     * 添加服务问题单
     */
    var addFormData=[
        {type:"block",offsetTop:15,list:[
            {type:"input",offsetLeft:40,label:"问题主题：",inputWidth: 370,name:"theme",validate:"NotEmpty,MaxLength[64]"},
            {type:"newcolumn"},
            {type:"label",label:"<span style='color: red'>*</span>"}
        ]},
        {type:"hidden",name:"mainProblemId"},
        {type:"hidden",name:"problemStep"},
        {type:"hidden",name:"createTime"},
        {type:"hidden",name:"createActorId",value:user.userId},
        {type:"hidden",name:"createActorName",value:user.userNamecn},
        {type:"hidden",name:"createDeptId",value:user.deptId},
        {type:"hidden",name:"createDeptName",value:user.deptName},
        
        {type:"hidden",name:"evaluationAttachment"},
        {type:"hidden",name:"evaluationAttachmentName"},
        {type:"hidden",name:"problemSolution"},
        {type:"hidden",name:"plannedFinishTime"},
        {type:"block",list:[
            {type:"input",offsetLeft:40,rows:4,label:"问题描述：",inputWidth: 370,name:"problemDescription",validate:"NotEmpty,MaxLength[600]" },
            {type:"newcolumn"},
            {type:"label",label:"<span style='color: red'>*</span>"}
        ]},
        {type:"block",list:[
            {type:"combo",offsetLeft:40,label:"问题来源：",inputWidth: 150,name:"source",readonly:true},
            {type:"newcolumn"},
            {type:"label",label:"<span style='color: red'>*</span>"},
            {type:"newcolumn"},
            {type:"combo",label:"紧急程度：",inputWidth: 130,name:"urgencyDegree",readonly:true},
            {type:"newcolumn"},
            {type:"label",label:"<span style='color: red'>*</span>"}
        ]},
        {type:"block",list:[
            {type:"input",offsetLeft:40,label:"处理时限：",inputWidth: 150,name:"processLimited",validate:"NotEmpty"},
            {type:"newcolumn"},
            {type:"label",label:"<span style='color: red'>*</span>"},
            {type:"newcolumn"},
            {type:"combo",label:"问题类别：",inputWidth: 130,name:"problemType",readonly:true},
            {type:"newcolumn"},
            {type:"label",label:"<span style='color: red'>*</span>"}
        ]},
        {type:"block",list:[
        {type:"input",offsetLeft:40,label:"人员选择：",inputWidth: 150,name:"acceptActorName",readonly:true,validate:"NotEmpty"},
        {type:"hidden",name:'acceptActorId',id:'acceptActorId'},
        {type:"hidden",name:'acceptDeptId',id:'acceptDeptId'},
        {type:"hidden",name:'acceptDeptName',id:'acceptDeptName'},
        {type:"newcolumn"},
        {type:"label",label:"<span style='color: red'>*</span>"},
        {type:"newcolumn"},
        {type:"combo",label:"短信通知：",inputWidth: 130,name:"isSMSNotice",readonly:true}
        ]},      
        {type:"block",list:[
        {type:"combo",offsetLeft:40,label:"发起方式：",inputWidth: 150,name:"noticeWay",readonly:true},
        {type:"newcolumn"},
        {type:"label",label:"<span style='color: red'>*</span>"},
        {type:"newcolumn"},
        {type:"input",label:"受理号码：",inputWidth: 130,name:"acceptActorNo"}
        ]},
        {type:"block",list:[
        {type:"input",offsetLeft:40,label:"附件名称：",inputWidth: 150,name:"attachmentName"}, 
        {type:"newcolumn"},
        {type:"combo",offsetLeft:13,label:"附件类型：",inputWidth: 130,name:"attachmentType",readonly:true}
        ]},
        {type:"newcolumn"},       
        {type:"block",className:"blockStyle",list:[
        {type:"label",offsetLeft:15,label:'<div style="padding-left: 26px; " class="item_label_left"><div class="dhxlist_txt_label align_left">' +
        '<label for="_fileupload">附件地址：</label></div><div class="dhxlist_cont" style="height:20px;">' +
        '<form enctype="multipart/form-data" action="upload" name="uploadForm" id="_uploadForm1" method="post">' +
        '<input style="width:250px;" class="dhxlist_txt_textarea" name="attachmentAddress" id="_fileupload1" type="FILE" accept="images/*"></form></div></div>',name:"file1",labelWidth:620},
        {type:'hidden',name: "attachmentAddress"}]},    
        {type:"newcolumn"},
        {type:"block",list:[
        {type:"input",offsetLeft:40,rows:4,label:"处理意见：",inputWidth: 370,name:"dealOpinion",validate:"NotEmpty,MaxLength[200]"},
        {type:"newcolumn"},
        {type:"label",label:"<span style='color: red'>*</span>"}
        ]},
        {type:"block",offsetTop:10,list:[
            {type:"button",label:"评估计划：",name:"isEvauationPlan",value:"评估计划",offsetLeft:150},
            {type:"newcolumn"},
            {type:"button",label:"保存",name:"save",value:"保存"},
            {type:"newcolumn"},
            {type:"button",label:"关闭",name:"close",value:"关闭"}
        ]}
    ]; 
    //建立表单。
    var addForm = addWindow.attachForm(addFormData);
    $("_uploadForm1").setAttribute("action",urlEncode(getBasePath()+"/portalCommon/module/serviceManage/serProManage/attachmentPost.jsp"));
    //加载下拉类型
    var problemSourceData = getComboByRemoveValue("PROBLEM_SOURCE");//来源
    addForm.getCombo("source").addOption(problemSourceData.options);
    addForm.getCombo("source").selectOption(0,false,false);
    var problemUrgencyData = getComboByRemoveValue("PROBLEM_URGENCY");//紧急情况
    addForm.getCombo("urgencyDegree").addOption(problemUrgencyData.options);
    addForm.getCombo("urgencyDegree").selectOption(0,false,false);
    var problemTypeData = getComboByRemoveValue("PROBLEM_TYPE");//问题类别
    addForm.getCombo("problemType").addOption(problemTypeData.options);
    addForm.getCombo("problemType").selectOption(0,false,false);
    var isSMSNoticeData = getComboByRemoveValue("IS_BOOL");//是否短信通知
    addForm.getCombo("isSMSNotice").addOption(isSMSNoticeData.options);
    var noticeWayData = getComboByRemoveValue("PROBLEM_WAY");//发起方式
    addForm.getCombo("noticeWay").addOption(noticeWayData.options);
    addForm.getCombo("noticeWay").selectOption(0,false,false);
    var attachmentTypeData = getComboByRemoveValue("ATTACHMENT_TYPE");//附件类型
    addForm.getCombo("attachmentType").addOption(attachmentTypeData.options);
    //加载人员选择树     
      var target=addForm.getInput("acceptActorName");
       Tools.addEvent(target,"click",function(){
       	         var url=base+"/meta/module/mag/selectedUser/ryxz.jsp";
   				var obj= window.showModalDialog(url+'?t='+Math.random(),'newWin','center:yes;status:no;scroll:no;help:no;dialogWidth=800px;dialogHeight=500px');
   				if (typeof(obj) != "undefined") { 
   				       target.value=obj.names;
   				       addForm.setFormData({acceptActorId:obj.ids});
   				       addForm.setFormData({acceptDeptId:obj.deptIds});
   				       addForm.setFormData({acceptDeptName:obj.deptNames});
   				       
   				}
           });
    //loadDeptTreeAdd("",addForm);
    //添加验证
    addForm.defaultValidateEvent();
    //查询表单事件处理
    addForm.attachEvent("onButtonClick", function(id) {
    	var data = addForm.getFormData();
    	var validateAdd=function(){
    if(data.isSMSNotice==1){
        			if(data.acceptActorNo==null){
        				alert('请填写受理号码');
        				return false;
        			}if(isNaN(data.acceptActorNo)){
        				alert("受理号码输入有误(号码为数字)");
        				return false;
        			}
        		}if(isNaN(data.processLimited)){
    				alert("处理时限输入有误(时限为数字)");
    				return false;
    			}if(data.dealOpinion.length>200){
    				alert('处理意见字数不能超过200');
    				return false;
    		    }if(data.problemDescription.length>200){
    				alert('问题描述字数不能超过200');
    				return false;
    		    }if(data.theme.length>40){
    				alert('问题主题字数不能超过40');
    				return false;
    		    }if(dwr.util.getValue("attachmentAddress")!=''){
    			if(data.attachmentType==null){
    				alert('请选择附件类型');
    				return false;
    			}if(data.attachmentName==null){
    				alert('请填写附件名称');
    				return false;
    			}
    			if(data.attachmentName.length>20){
    				alert("附件名称不能超过20字");
    				return false;
    			}
    		}else{
    			if(data.attachmentType!=null||data.attachmentName!=null){
    				alert('请选择上传的附件');
    				return false;
    			}
    		}	
    		return true;
    		}
    	if(addForm.validate()&&validateAdd()){
        if (id == "save") {
            //先上传文件
            var file1=null;
            var supportFile1=['png','gif','jpg','jpeg','doc','txt','.xls','pdf'];
            if(file1=dwr.util.getValue("_fileupload1")){
                if(new RegExp("["+supportFile1.join("|")+"]$","i").test(file1)){
                    var iframeName="_uploadFrame1";
                    var ifr=$(iframeName);
                    //生成一个隐藏iframe，并设置form的target为该iframe，模拟ajax效果
                    if(!ifr){
                        ifr=document.createElement("iframe");
                        ifr.name=iframeName;
                        ifr.id=iframeName;
                        ifr.style.display="none";
                        document.body.appendChild(ifr);
                        if (ifr.attachEvent){
                            ifr.attachEvent("onload", function(){
                            	data.attachmentAddress=window.frames[iframeName].fileName;
                                dwrCaller.executeAction("insertServiceProblem",data,function(data){
                                    if(data.type == "error" || data.type == "invalid"){
                                        dhx.alert("对不起，新增出错，请重试！");
                                    }
                                    else{
                                        dhx.alert("新增成功");
                                        addWindow.close();
                                        dhxWindow.unload();
                                        logGrid.clearAll();
                                        logGrid.load(dwrCaller.queryProblem, "json");
                                    }
                                })
                            });
                        } else {
                            ifr.onload = function(){
                            	data.attachmentAddress=window.frames[iframeName].fileName;
                                dwrCaller.executeAction("insertServiceProblem",data,function(data){
                                    if(data.type == "error" || data.type == "invalid"){
                                        dhx.alert("对不起，新增出错，请重试！");
                                    }
                                    else{
                                        dhx.alert("新增成功");
                                        addWindow.close();
                                        dhxWindow.unload();
                                        logGrid.clearAll();
                                        logGrid.load(dwrCaller.queryProblem, "json");
                                    }
                                })
                            };
                        }
                    }
                    var fm = $("_uploadForm1");
                    fm.target = iframeName;
                    $("_uploadForm1").submit();
                }else{
                    dhx.alert("请选择一个图片文件，支持格式png/gif/jpg/jpeg/doc/txt/pdf/xls/xlsx/docx/sql");
                    return;
                }
            }else{
                dhx.showProgress("新增服务问题单", "正在提交表单数据...");
                dwrCaller.executeAction("insertServiceProblem",data,function(data){
                    if(data.type == "error" || data.type == "invalid"){
                        dhx.alert("对不起，新增出错，请重试！");
                    }
                    else{
                        dhx.alert("新增成功");
                        addWindow.close();
                        dhxWindow.unload();
                        logGrid.clearAll();
                        logGrid.load(dwrCaller.queryProblem, "json");
                    }
                })
            }
        }
    }
    	if(id == "close"){
            addWindow.close();
            dhxWindow.unload();
        }
        if(id == "isEvauationPlan"){
        	evauationPlan();
        }
    });
    /**
     * 评估计划弹出框
     */
    var evauationPlan=function(){
        //初始化处理弹出窗口。
    	var eData = addForm.getFormData();
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("evauationWindow", 0, 0, 350, 1000);
        var evauationWindow = dhxWindow.window("evauationWindow");
        evauationWindow.setModal(true);
        evauationWindow.stick();
        evauationWindow.setDimension(700, 350);
        evauationWindow.center();
        evauationWindow.setPosition(evauationWindow.getPosition()[0],evauationWindow.getPosition()[1]-50);
        evauationWindow.denyResize();
        evauationWindow.denyPark();
        evauationWindow.button("minmax1").hide();
        evauationWindow.button("park").hide();
        evauationWindow.button("stick").hide();
        evauationWindow.button("sticked").hide();
        evauationWindow.setText("添加评估计划");
        evauationWindow.keepInViewport(true);
        evauationWindow.show(); 
        if(eData.problemSolution==null||eData.problemSolution==""){
        evauationForm = evauationWindow.attachForm( [ 
            {type : "newcolumn"},
            {type:"block",offsetTop:15,list:[
            {type:"input",offsetLeft:10,rows:10,label:"问题解决方案：",inputWidth: 537,name:"problemSolution",validate:"NotEmpty,MaxLength[1000]"},
            {type:"newcolumn"},
            {type:"label",label:"<span style='color: red'>*</span>"}
            ]},
            {type : "newcolumn"},      
            {type : "calendar",
            	offsetLeft:10,
                label : "计划完成时间：",
                name : "plannedFinishTime",
                dateFormat : "%Y-%m-%d",
                weekStart : "7",
                value :new Date(),
                inputWidth: "187",
                readonly:"readonly"
            },
            {type : "newcolumn"}, 
            {type:"block",className:"blockStyle",list:[
            {type:"label",offsetLeft:10,label:'<div style="padding-left: 2px; " class="item_label_left"><div class="dhxlist_txt_label align_left">' +
            '<label for="_fileupload">附&nbsp;&nbsp;件&nbsp;&nbsp;地&nbsp;&nbsp;址：</label></div><div class="dhxlist_cont" style="height:20px;">' +
            '<form enctype="multipart/form-data" action="upload" name="uploadForm" id="_uploadForm2" method="post">' +
            '<input style="width:250px;" class="dhxlist_txt_textarea" name="evaluationAttachment" id="_fileupload2" type="FILE" accept="images/*"></form></div></div>',name:"file",labelWidth:620},
            {type:'hidden',name: "evaluationAttachment"},
            {type:'hidden',name: "evaluationAttachmentName"}]},   
            {type:"newcolumn"},
            {type:"block",offsetTop:10,list:[
            {type:"button",label:"确认：",name:"evaluationDeal",value:"确认",offsetLeft:280},
            {type:"newcolumn"},
            {type:"button",label:"关闭",name:"close",value:"关闭"}
            ]}
            ]);
        }else{
            evauationForm = evauationWindow.attachForm( [ 
            {type : "newcolumn"},
            {type:"block",offsetTop:15,list:[
            {type:"input",offsetLeft:10,rows:10,label:"问题解决方案：",inputWidth: 537,name:"problemSolution",value:eData.problemSolution,validate:"NotEmpty,MaxLength[1000]"},
            {type:"newcolumn"},
            {type:"label",label:"<span style='color: red'>*</span>"}
            ]},
            {type : "newcolumn"},      
            {type : "calendar",
            offsetLeft:10,
            label : "计划完成时间：",
            name : "plannedFinishTime",
            dateFormat : "%Y-%m-%d",
            weekStart : "7",
            value :eData.plannedFinishTime,
            inputWidth: "187",
            readonly:"readonly"
            },
            {type : "newcolumn"}, 
            {type:"block",className:"blockStyle",list:[
            {type:"label",offsetLeft:9,label:'<div style="padding-left: 2px; " class="item_label_left"><div class="dhxlist_txt_label align_left">' +
            '<label for="_fileupload">附&nbsp;&nbsp;件&nbsp;&nbsp;地&nbsp;&nbsp;址：</label></div><div class="dhxlist_cont" style="height:20px;">' +
            '<form enctype="multipart/form-data" action="upload" name="uploadForm" id="_uploadForm2" method="post">' +
            '<input style="width:250px;" class="dhxlist_txt_textarea" name="evaluationAttachment", id="_fileupload2" type="FILE" accept="images/*"></form></div></div>',name:"file",labelWidth:620},
            {type:'hidden',name: "evaluationAttachment",value :eData.evaluationAttachment},
            {type:'hidden',name: "evaluationAttachmentName",value :eData.evaluationAttachmentName}]},
            {type:"newcolumn"},
            {type:"block",className:"blockStyle",list:[
            {type:"label",offsetLeft:15,label:"链&nbsp;&nbsp;接&nbsp;&nbsp;附&nbsp;&nbsp;件：<a href=\"#\" onclick=\"downloadattrs('"+eData.evaluationAttachment+"')\">"+eData.evaluationAttachmentName+"</a>",  labelWidth:620}
            ]},
            {type:"newcolumn"},
            {type:"block",offsetTop:10,list:[
            {type:"button",label:"确认：",name:"evaluationDeal",value:"确认",offsetLeft:280},
            {type:"newcolumn"},
            {type:"button",label:"关闭",name:"close",value:"关闭"}
            ]}
          ]);   	
        }
        $("_uploadForm2").setAttribute("action",urlEncode(getBasePath()+"/portalCommon/module/serviceManage/serProManage/attachmentPost.jsp"));
        evauationForm.defaultValidateEvent();
        evauationForm.attachEvent("onButtonClick", function(id) {
    		var evauationData = evauationForm.getFormData();
        	if(evauationForm.validate()){
    	    if (id == "evaluationDeal") {
                //先上传文件
                var file=null;
                var supportFile=['png','gif','jpg','jpeg','doc','txt','.xls','pdf','docx','xlsx','sql'];
                if(file=dwr.util.getValue("_fileupload2")||dwr.util.getValue("evaluationAttachmentName")){
                    if(new RegExp("["+supportFile.join("|")+"]$","i").test(file)){
                    	var originalFilename =file.substring(file.lastIndexOf("\\")+1,file.length);
                        var iframeName="_uploadFrame2";
                        var ifr=$(iframeName);
                        //生成一个隐藏iframe，并设置form的target为该iframe，模拟ajax效果
                        if(!ifr){
                            ifr=document.createElement("iframe");
                            ifr.name=iframeName;
                            ifr.id=iframeName;
                            ifr.style.display="none";
                            document.body.appendChild(ifr);
                        }
                        var fm = $("_uploadForm2");
                        fm.target = iframeName;
                        $("_uploadForm2").submit();
                    }else{
                        dhx.alert("请选择一个图片文件，支持格式png/gif/jpg/jpeg/doc/txt/pdf/xls/xlsx/docx/sql");
                        return;
                    }
                }
       dhx.confirm("确认添加此次评估计划么？", function(r){
    	      if(r){
    	    	  if(file=dwr.util.getValue("_fileupload2")||dwr.util.getValue("evaluationAttachmentName")){
    	    		  addForm.setFormData( {
    	                  	"problemSolution":evauationData.problemSolution,
    	                    "plannedFinishTime":utcToDate(evauationData.plannedFinishTime),
    	                    "evaluationAttachment" :window.frames[iframeName].fileName,
    	                    "evaluationAttachmentName" :originalFilename
    	                  });
    	    	  }else{
	                  addForm.setFormData( {
	                  	"problemSolution":evauationData.problemSolution,
	                    "plannedFinishTime":utcToDate(evauationData.plannedFinishTime)
	                  });
    	    	  }
		            evauationWindow.close();
		            dhxWindow.unload();
    	   }
    	});
   }
}
   if(id == "close"){
        		evauationWindow.close();
                dhxWindow.unload();
  }
   });
    }
}

/**
 * 主单处理
 */
var dealMain=function(mainProblemId){
	var rowData = logGrid.getRowData(mainProblemId);//获取行数据
    //初始化处理弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("dealMainWindow", 0, 0, 550, 1000);
    var dealMainWindow = dhxWindow.window("dealMainWindow");
    dealMainWindow.setModal(true);
    dealMainWindow.stick();
    dealMainWindow.setDimension(650, 400);
    dealMainWindow.center();
    dealMainWindow.setPosition(dealMainWindow.getPosition()[0],dealMainWindow.getPosition()[1]-50);
    dealMainWindow.denyResize();
    dealMainWindow.denyPark();
    dealMainWindow.button("minmax1").hide();
    dealMainWindow.button("park").hide();
    dealMainWindow.button("stick").hide();
    dealMainWindow.button("sticked").hide();
    dealMainWindow.setText("处理服务问题单");
    dealMainWindow.keepInViewport(true);
    dealMainWindow.show(); 
    if(rowData.data[16]!=null){
    dealMainForm = dealMainWindow.attachForm( [ 
       {type : "newcolumn"},
       {type:"block",offsetTop:15,list:[
       {type:"input",offsetLeft:50,rows:4,label:"处理意见：",inputWidth: 397,name:"dealOpinion",validate:"NotEmpty,MaxLength[200]"},
       {type:"newcolumn"},
       {type:"label",label:"<span style='color: red'>*</span>"}
       ]},
       {type : "newcolumn"},
       {type:"block",offsetTop:15,list:[
       {type:"input",offsetLeft:50,rows:4,label:"评估报告：",inputWidth: 397,name:"evaluationDetail",validate:"NotEmpty,MaxLength[1000]"},
       {type:"newcolumn"},
       {type:"label",label:"<span style='color: red'>*</span>"}
       ]},
       {type : "newcolumn"},
       {type : "input",offsetLeft:50,label : "人员选择：",name : "nextActorName",inputWidth: "150"},
       {type:"hidden",name:'nextActorId',id:'nextActorId',value:rowData.data[20]},
       {type:"hidden",name:'nextDeptId',id:'nextDeptId'},
       {type:"hidden",name:'nextDeptName',id:'nextDeptName'},
       
       {type:"hidden",name:"dealActorId",value:user.userId},
       {type:"hidden",name:"dealActorName",value:user.userNamecn},
       {type:"hidden",name:"dealDeptId",value:user.deptId},
       {type:"hidden",name:"dealDeptName",value:user.deptName},
       {type : "newcolumn"},      
       {type:"combo",offsetLeft:20,label:"短信通知：",name:"isSMSNotice",inputWidth:"150",readonly:true},
       {type : "newcolumn"},
       //{type : "input",offsetLeft:50,label : "联系电话：",name : "nextActorNo",inputWidth: "150"},
       //{type : "newcolumn"},
       {type:"input",offsetLeft:50,label:"附件名称：",inputWidth: 150,name:"attachmentName"},
       {type:"newcolumn"},
       {type:"combo",offsetLeft:20,label:"附件类型：",inputWidth: 150,name:"attachmentType",readonly:true},
       {type:"newcolumn"},
       {type:"block",className:"blockStyle",list:[
       {type:"label",label:'<div style="padding-left: 47px; " class="item_label_left"><div class="dhxlist_txt_label align_left">' +
       '<label for="_fileupload">附件地址：</label></div><div class="dhxlist_cont" style="height:20px;">' +
       '<form enctype="multipart/form-data" action="upload" name="uploadForm" id="_uploadForm3" method="post">' +
       '<input style="width:250px;" class="dhxlist_txt_textarea" name="attachmentAddress" id="_fileupload3" type="FILE" accept="images/*"></form></div></div>',name:"file",labelWidth:620},
       {type:'hidden',name: "attachmentAddress"}]}, 
       {type:"newcolumn"},
       {type:"block",offsetTop:10,list:[
       {type:"button",label:"确认：",name:"nextDeal",value:"确认",offsetLeft:250},
       {type:"newcolumn"},
       {type:"button",label:"关闭",name:"close",value:"关闭"}
       ]}
    ]);	
    }else{
    dealMainForm = dealMainWindow.attachForm( [ 
        {type : "newcolumn"},
        {type:"block",offsetTop:15,list:[
        {type:"input",offsetLeft:50,rows:4,label:"处理意见：",inputWidth: 397,name:"dealOpinion",validate:"NotEmpty,MaxLength[200]"},
        {type:"newcolumn"},
        {type:"label",label:"<span style='color: red'>*</span>"}
        ]},
        {type : "newcolumn"},
        {type : "input",offsetLeft:50,label : "人员选择：",name : "nextActorName",inputWidth: "150"},
        {type:"hidden",name:'nextActorId',id:'nextActorId',value:rowData.data[20]},
        {type:"hidden",name:'nextDeptId',id:'nextDeptId'},
        {type:"hidden",name:'nextDeptName',id:'nextDeptName'},
        
        {type:"hidden",name:"dealActorId",value:user.userId},
        {type:"hidden",name:"dealActorName",value:user.userNamecn},
        {type:"hidden",name:"dealDeptId",value:user.deptId},
        {type:"hidden",name:"dealDeptName",value:user.deptName},
        {type : "newcolumn"},      
        {type:"combo",offsetLeft:20,label:"短信通知：",name:"isSMSNotice",inputWidth:"150",readonly:true},
        {type : "newcolumn"},
        //{type : "input",offsetLeft:50,label : "联系电话：",name : "nextActorNo",inputWidth: "150"},
        //{type : "newcolumn"},
        {type:"input",offsetLeft:50,label:"附件名称：",inputWidth: 150,name:"attachmentName"},
        {type:"newcolumn"},
        {type:"combo",offsetLeft:20,label:"附件类型：",inputWidth: 150,name:"attachmentType",readonly:true},
        {type:"newcolumn"},
        {type:"block",className:"blockStyle",list:[
        {type:"label",label:'<div style="padding-left: 47px; " class="item_label_left"><div class="dhxlist_txt_label align_left">' +
        '<label for="_fileupload">附件地址：</label></div><div class="dhxlist_cont" style="height:20px;">' +
        '<form enctype="multipart/form-data" action="upload" name="uploadForm" id="_uploadForm3" method="post">' +
        '<input style="width:250px;" class="dhxlist_txt_textarea" name="attachmentAddress" id="_fileupload3" type="FILE" accept="images/*"></form></div></div>',name:"file",labelWidth:620},
        {type:'hidden',name: "attachmentAddress"}]}, 
        {type:"newcolumn"},
        {type:"block",offsetTop:10,list:[
        {type:"button",label:"确认：",name:"nextDeal",value:"确认",offsetLeft:250},
        {type:"newcolumn"},
        {type:"button",label:"关闭",name:"close",value:"关闭"}
        ]}
        ]);
    }
    var tableAttachmentData = getComboByRemoveValue("ATTACHMENT_TYPE");
    dealMainForm.getCombo("attachmentType").addOption(tableAttachmentData.options);
    var isSMSNoticeData = getComboByRemoveValue("IS_BOOL");//是否短信通知
    dealMainForm.getCombo("isSMSNotice").addOption(isSMSNoticeData.options);
  //加载人员选择树     
    var target=dealMainForm.getInput("nextActorName");
     Tools.addEvent(target,"click",function(){
     	         var url=base+"/meta/module/mag/selectedUser/ryxz.jsp";
 				var obj= window.showModalDialog(url+'?t='+Math.random(),'newWin','center:yes;status:no;scroll:no;help:no;dialogWidth=800px;dialogHeight=500px');
 				if (typeof(obj) != "undefined") { 
 				       target.value=obj.names;
 				      dealMainForm.setFormData({nextActorId:obj.ids});
 				      dealMainForm.setFormData({nextDeptId:obj.deptIds});
 				      dealMainForm.setFormData({nextDeptName:obj.deptNames});
 				       
 				}
         });
   // loadDeptTreeNext("",dealMain);
	//派送响应函数
     dealMainForm.setFormData( {
		"mainProblemId" : mainProblemId
	});
    $("_uploadForm3").setAttribute("action",urlEncode(getBasePath()+"/portalCommon/module/serviceManage/serProManage/attachmentPost.jsp"));
    dealMainForm.defaultValidateEvent();
    dealMainForm.attachEvent("onButtonClick", function(id) {
		var data = dealMainForm.getFormData();
    	var validateAdd=function(){
    		/*if(data.isSMSNotice==1){
    			if(data.nextActorNo==null){
    				alert('请填写联系电话');
    				return false;
    			}if(isNaN(data.nextActorNo)){
    				alert("联系电话输入有误(号码为数字)");
    				return false;
    			}
    		}*/
    		if(dwr.util.getValue("attachmentAddress")!=''){
    			if(data.attachmentType==null){
    				alert('请选择附件类型');
    				return false;
    			}if(data.attachmentName==null){
    				alert('请填写附件名称');
    				return false;
    			}
    			if(data.attachmentName.length>20){
    				alert("附件名称不能超过20字符");
    				return false;
    			}
    		}else{
    			if(data.attachmentType!=null||data.attachmentName!=null){
    				alert('请选择上传的附件');
    				return false;
    			}
    		}	
    		return true;
    		}
    	if(dealMainForm.validate()&&validateAdd()){
	    if (id == "nextDeal") {
	        //处理数据
	        var data = dealMainForm.getFormData();
            //先上传文件
            var file=null;
            var supportFile=['png','gif','jpg','jpeg','doc','txt','.xls','pdf','docx','xlsx','sql'];
            if(file=dwr.util.getValue("_fileupload3")){
                if(new RegExp("["+supportFile.join("|")+"]$","i").test(file)){
                    var iframeName="_uploadFrame3";
                    var ifr=$(iframeName);
                    //生成一个隐藏iframe，并设置form的target为该iframe，模拟ajax效果
                    if(!ifr){
                        ifr=document.createElement("iframe");
                        ifr.name=iframeName;
                        ifr.id=iframeName;
                        ifr.style.display="none";
                        document.body.appendChild(ifr);
                        if (ifr.attachEvent){
                            ifr.attachEvent("onload", function(){
                            	data.attachmentAddress=window.frames[iframeName].fileName;
                                dwrCaller.executeAction("nextDealServiceProblem",data,function(data){
                                    if(data.type == "error" || data.type == "invalid"){
                                        dhx.alert("对不起，处理出错，请重试！");
                                    }
                                    else{
                                        dhx.alert("处理成功");
                                        dealMainWindow.close();
                                        dhxWindow.unload();
                                        logGrid.clearAll();
                                        logGrid.load(dwrCaller.queryProblem, "json");
                                    }
                                })
                            });
                        } else {
                            ifr.onload = function(){
                            	data.attachmentAddress=window.frames[iframeName].fileName;
                                dwrCaller.executeAction("nextDealServiceProblem",data,function(data){
                                    if(data.type == "error" || data.type == "invalid"){
                                        dhx.alert("对不起，处理出错，请重试！");
                                    }
                                    else{
                                        dhx.alert("处理成功");
                                        dealMainWindow.close();
                                        dhxWindow.unload();
                                        logGrid.clearAll();
                                        logGrid.load(dwrCaller.queryProblem, "json");
                                    }
                                })
                            };
                        }
                    }
                    var fm = $("_uploadForm3");
                    fm.target = iframeName;
                    $("_uploadForm3").submit();
                }else{
                    dhx.alert("请选择一个图片文件，支持格式png/gif/jpg/jpeg/doc/txt/pdf/xls/xlsx/docx/sql");
                    return;
                }
            }else{
                dhx.showProgress("服务问题管理", "正在提交表单数据...");
                dwrCaller.executeAction("nextDealServiceProblem",data,function(data){
                    if(data.type == "error" || data.type == "invalid"){
                        dhx.alert("对不起，处理出错，请重试！");
                    }
                    else{
                        dhx.alert("处理成功");
                        dealMainWindow.close();
                        dhxWindow.unload();
                        logGrid.clearAll();
                        logGrid.load(dwrCaller.queryProblem, "json");
                    }
                })
            }
	}
    	}
    	if(id == "close"){
    		dealMainWindow.close();
            dhxWindow.unload();
        }
	});
}
/**
 * 退回
 */
var returnServiceProblemInfo=function(mainProblemId){
	var rowData = logGrid.getRowData(mainProblemId);//获取行数据
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
    returnWindow.setText("退单处理");
    returnWindow.keepInViewport(true);
    returnWindow.show(); 
        returnForm = returnWindow.attachForm( [ 
        {type : "newcolumn"},
        {type:"input",offsetLeft:20,rows:5,label:"退单原因：",inputWidth: 400,name:"returnReason",validate:"NotEmpty,MaxLength[200]" },
        {type : "newcolumn"},
        {type:"combo",offsetLeft:20,label:"短信通知：",inputWidth: 145,name:"isSMSNotice",readonly:true},
        //{type:"newcolumn"},
       // {type:"input",offsetLeft:20,label:"联系电话：",inputWidth: 160,name:"returnNo"},
        {type:"hidden",name:"dealActorId",value:user.userId},
        {type:"hidden",name:"dealActorName",value:user.userNamecn},
        {type:"hidden",name:"dealDeptId",value:user.deptId},
        {type:"hidden",name:"dealDeptName",value:user.deptName},
        
        {type:"hidden",name:"nextActorId",value:rowData.data[12]},
        {type:"hidden",name:"nextActorName",value:rowData.data[7]},
        {type:"hidden",name:"nextDeptId",value:rowData.data[13]},
        {type:"hidden",name:"nextDeptName",value:rowData.data[14]},
        {type:"newcolumn"},
        {type:"block",offsetTop:10,list:[
        {type:"button",label:"退回：",name:"nextReturn",value:"退回",offsetLeft:200},
        {type:"newcolumn"},
        {type:"button",label:"关闭",name:"shutup",value:"关闭"}
        ]}
        ]);
	//派送响应函数
	returnForm.setFormData( {
		"mainProblemId" : mainProblemId
	});
	var isSMSNoticeData = getComboByRemoveValue("IS_BOOL");//是否短信通知
	returnForm.getCombo("isSMSNotice").addOption(isSMSNoticeData.options);
	returnForm.defaultValidateEvent();
	returnForm.attachEvent("onButtonClick", function(id) {
		 var data = returnForm.getFormData();
		 var validateAdd=function(){
				/*if(data.isSMSNotice==1){
	    			if(data.returnNo==null){
	    				alert('请填写联系电话');
	    				return false;
	    			}if(isNaN(data.returnNo)){
	    				alert("联系电话输入有误(号码为数字)");
	    				return false;
	    			}
	    		}*/
				if(data.returnReason.length>200){
	 				alert("退单原因字数不能超过200");
	 				return  false;
	 			}
				return true;
			}
		if(returnForm.validate()&&validateAdd()){
	    if (id == "nextReturn") {
	        //保存
	        dwrCaller.executeAction("nextReturnServiceProblem",data,function(data){
	            if(data.type == "error" || data.type == "invalid"){
	                dhx.alert("对不起，处理失败，请重试！");
	            }
	            else{
	            	dhx.alert("退回成功");
	            	returnWindow.close();
                    dhxWindow.unload();
                    logGrid.clearAll();
                    logGrid.load(dwrCaller.queryProblem, "json");
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
/**
 * 副单处理
 */
var dealVice=function(mainProblemId){
	var rowData = logGrid.getRowData(mainProblemId);//获取行数据
    //初始化处理弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("dealViceWindow", 0, 0, 350, 1000);
    var dealViceWindow = dhxWindow.window("dealViceWindow");
    dealViceWindow.setModal(true);
    dealViceWindow.stick();
    dealViceWindow.setDimension(650, 400);
    dealViceWindow.center();
    dealViceWindow.setPosition(dealViceWindow.getPosition()[0],dealViceWindow.getPosition()[1]-50);
    dealViceWindow.denyResize();
    dealViceWindow.denyPark();
    dealViceWindow.button("minmax1").hide();
    dealViceWindow.button("park").hide();
    dealViceWindow.button("stick").hide();
    dealViceWindow.button("sticked").hide();
    dealViceWindow.setText("处理服务问题单");
    dealViceWindow.keepInViewport(true);
    dealViceWindow.show(); 
    if(rowData.data[16]!=null){
    dealViceForm = dealViceWindow.attachForm( [ 
        {type : "newcolumn"},
        {type:"block",offsetTop:15,list:[
        {type:"input",offsetLeft:50,rows:4,label:"处理意见：",inputWidth: 397,name:"dealOpinion",validate:"NotEmpty,MaxLength[600]"},
        {type:"newcolumn"},
        {type:"label",label:"<span style='color: red'>*</span>"}
        ]},
        {type : "newcolumn"},
        {type:"block",offsetTop:15,list:[
        {type:"input",offsetLeft:50,rows:4,label:"评估报告：",inputWidth: 397,name:"evaluationDetail",validate:"NotEmpty,MaxLength[1000]"},
        {type:"newcolumn"},
        {type:"label",label:"<span style='color: red'>*</span>"}
        ]},
        {type : "newcolumn"},
        {type : "input",offsetLeft:50,label : "人员选择：",name : "nextActorName",inputWidth: "150"},
        {type:"hidden",name:'nextActorId',id:'nextActorId'},
        {type:"hidden",name:'nextDeptId',id:'nextDeptId'},
        {type:"hidden",name:'nextDeptName',id:'nextDeptName'},
        
        {type:"hidden",name:"dealActorId",value:user.userId},
        {type:"hidden",name:"dealActorName",value:user.userNamecn},
        {type:"hidden",name:"dealDeptId",value:user.deptId},
        {type:"hidden",name:"dealDeptName",value:user.deptName},
        {type : "newcolumn"},
        {type:"combo",offsetLeft:20,label:"短信通知：",name:"isSMSNotice",inputWidth:"150",readonly:true},
        //{type : "newcolumn"},
        //{type : "input",offsetLeft:50,label : "联系电话：",name : "nextActorNo",inputWidth: "150"},
        {type : "newcolumn"},
        {type:"input",offsetLeft:50,label:"附件名称：",inputWidth: 150,name:"attachmentName"},
        {type:"newcolumn"},
        {type:"combo",offsetLeft:20,label:"附件类型：",inputWidth: 150,name:"attachmentType",readonly:true},
        {type:"newcolumn"},
        {type:"block",className:"blockStyle",list:[
        {type:"label",label:'<div style="padding-left: 47px; " class="item_label_left"><div class="dhxlist_txt_label align_left">' +
        '<label for="_fileupload">附件地址：</label></div><div class="dhxlist_cont" style="height:20px;">' +
        '<form enctype="multipart/form-data" action="upload" name="uploadForm" id="_uploadForm4" method="post">' +
        '<input style="width:250px;" class="dhxlist_txt_textarea" name="attachmentAddress" id="_fileupload4" type="FILE" accept="images/*"></form></div></div>',name:"file",labelWidth:620},
        {type:'hidden',name: "attachmentAddress"}]}, 
        {type:"newcolumn"},
        {type:"block",offsetTop:10,list:[
        {type:"button",label:"确认：",name:"nextDeal",value:"确认",offsetLeft:250},
        {type:"newcolumn"},
        {type:"button",label:"关闭",name:"close",value:"关闭"}
        ]}
        ]);
    }else{
    dealViceForm = dealViceWindow.attachForm( [ 
	       {type : "newcolumn"},
	       {type:"block",offsetTop:15,list:[
	       {type:"input",offsetLeft:50,rows:4,label:"处理意见：",inputWidth: 397,name:"dealOpinion",validate:"NotEmpty,MaxLength[600]"},
	       {type:"newcolumn"},
	       {type:"label",label:"<span style='color: red'>*</span>"}
	       ]},
	       {type : "newcolumn"},
	       {type : "input",offsetLeft:50,label : "人员选择：",name : "nextActorName",inputWidth: "150"},
	       {type:"hidden",name:'nextActorId',id:'nextActorId'},
	       {type:"hidden",name:'nextDeptId',id:'nextDeptId'},
	       {type:"hidden",name:'nextDeptName',id:'nextDeptName'},
	       
	       {type:"hidden",name:"dealActorId",value:user.userId},
	       {type:"hidden",name:"dealActorName",value:user.userNamecn},
	       {type:"hidden",name:"dealDeptId",value:user.deptId},
	       {type:"hidden",name:"dealDeptName",value:user.deptName},
	       {type : "newcolumn"},
	       {type:"combo",offsetLeft:20,label:"短信通知：",name:"isSMSNotice",inputWidth:"150",readonly:true},
	       //{type : "newcolumn"},
	       //{type : "input",offsetLeft:50,label : "联系电话：",name : "nextActorNo",inputWidth: "150"},
	       {type : "newcolumn"},
	       {type:"input",offsetLeft:50,label:"附件名称：",inputWidth: 150,name:"attachmentName"},
	       {type:"newcolumn"},
	       {type:"combo",offsetLeft:20,label:"附件类型：",inputWidth: 150,name:"attachmentType",readonly:true},
	       {type:"newcolumn"},
	       {type:"block",className:"blockStyle",list:[
	       {type:"label",label:'<div style="padding-left: 47px; " class="item_label_left"><div class="dhxlist_txt_label align_left">' +
	       '<label for="_fileupload">附件地址：</label></div><div class="dhxlist_cont" style="height:20px;">' +
	       '<form enctype="multipart/form-data" action="upload" name="uploadForm" id="_uploadForm4" method="post">' +
	       '<input style="width:250px;" class="dhxlist_txt_textarea" name="attachmentAddress" id="_fileupload4" type="FILE" accept="images/*"></form></div></div>',name:"file",labelWidth:620},
	       {type:'hidden',name: "attachmentAddress"}]}, 
	       {type:"newcolumn"},
	       {type:"block",offsetTop:10,list:[
	       {type:"button",label:"确认：",name:"nextDeal",value:"确认",offsetLeft:250},
	       {type:"newcolumn"},
	       {type:"button",label:"关闭",name:"close",value:"关闭"}
	       ]}
     ]);	
    }
    var tableAttachmentData = getComboByRemoveValue("ATTACHMENT_TYPE");
    dealViceForm.getCombo("attachmentType").addOption(tableAttachmentData.options);
    var isSMSNoticeData = getComboByRemoveValue("IS_BOOL");//是否短信通知
    dealViceForm.getCombo("isSMSNotice").addOption(isSMSNoticeData.options);
    //loadDeptTreeNext("",dealViceForm);
    //加载人员选择树     
    var target=dealViceForm.getInput("nextActorName");
     Tools.addEvent(target,"click",function(){
     	         var url=base+"/meta/module/mag/selectedUser/ryxz.jsp";
 				var obj= window.showModalDialog(url+'?t='+Math.random(),'newWin','center:yes;status:no;scroll:no;help:no;dialogWidth=800px;dialogHeight=500px');
 				if (typeof(obj) != "undefined") { 
 				       target.value=obj.names;
 				       dealViceForm.setFormData({nextActorId:obj.ids});
 				       dealViceForm.setFormData({nextDeptId:obj.deptIds});
 				       dealViceForm.setFormData({nextDeptName:obj.deptNames});
 				       
 				}
         });
	//派送响应函数
	dealViceForm.setFormData( {
		"mainProblemId" : mainProblemId
	});
    $("_uploadForm4").setAttribute("action",urlEncode(getBasePath()+"/portalCommon/module/serviceManage/serProManage/attachmentPost.jsp"));
    dealViceForm.defaultValidateEvent();
    dealViceForm.attachEvent("onButtonClick", function(id) {
        var data = dealViceForm.getFormData();     
		var validateAdd=function(){
			/*if(data.isSMSNotice==1){
    			if(data.nextActorNo==null){
    				alert('请填写联系电话');
    				return false;
    			}if(isNaN(data.nextActorNo)){
    				alert("联系电话输入有误(号码为数字)");
    				return false;
    			}
    		}*/
    		if(dwr.util.getValue("attachmentAddress")!=''){
    			if(data.attachmentType==null){
    				alert('请选择附件类型');
    				return false;
    			}if(data.attachmentName==null){
    				alert('请填写附件名称');
    				return false;
    			}
    			if(data.attachmentName.length>20){
    				alert("附件名称不能超过20字");
    				return false;
    			}
    		}else{
    			if(data.attachmentType!=null||data.attachmentName!=null){
    				alert('请选择上传的附件');
    				return false;
    			}
    		}	
    		return true;
    		}
    	if(dealViceForm.validate()&&validateAdd()){
	    if (id == "nextDeal") {
            //先上传文件
            var file=null;
            var supportFile=['png','gif','jpg','jpeg','doc','txt','.xls','pdf','docx','xlsx','sql'];
            if(file=dwr.util.getValue("_fileupload4")){
                if(new RegExp("["+supportFile.join("|")+"]$","i").test(file)){
                    var iframeName="_uploadFrame4";
                    var ifr=$(iframeName);
                    //生成一个隐藏iframe，并设置form的target为该iframe，模拟ajax效果
                    if(!ifr){
                        ifr=document.createElement("iframe");
                        ifr.name=iframeName;
                        ifr.id=iframeName;
                        ifr.style.display="none";
                        document.body.appendChild(ifr);
                        if (ifr.attachEvent){
                            ifr.attachEvent("onload", function(){
                            	data.attachmentAddress=window.frames[iframeName].fileName;
                                dwrCaller.executeAction("nextViceServiceProblem",data,function(data){
                                    if(data.type == "error" || data.type == "invalid"){
                                        dhx.alert("对不起，处理出错，请重试！");
                                    }
                                    else{
                                        dhx.alert("处理成功");
                                        dealViceWindow.close();
                                        dhxWindow.unload();
                                        logGrid.clearAll();
                                        logGrid.load(dwrCaller.queryProblem, "json");
                                    }
                                })
                            });
                        } else {
                            ifr.onload = function(){
                            	data.attachmentAddress=window.frames[iframeName].fileName;
                                dwrCaller.executeAction("nextViceServiceProblem",data,function(data){
                                    if(data.type == "error" || data.type == "invalid"){
                                        dhx.alert("对不起，处理出错，请重试！");
                                    }
                                    else{
                                        dhx.alert("处理成功");
                                        dealViceWindow.close();
                                        dhxWindow.unload();
                                        logGrid.clearAll();
                                        logGrid.load(dwrCaller.queryProblem, "json");
                                    }
                                })
                            };
                        }
                    }
                    var fm = $("_uploadForm4");
                    fm.target = iframeName;
                    $("_uploadForm4").submit();
                }else{
                    dhx.alert("请选择一个图片文件，支持格式png/gif/jpg/jpeg/doc/txt/pdf/xls/xlsx/docx/sql");
                    return;
                }
            }else{
                dhx.showProgress("服务问题管理", "正在提交表单数据...");
                dwrCaller.executeAction("nextViceServiceProblem",data,function(data){
                    if(data.type == "error" || data.type == "invalid"){
                        dhx.alert("对不起，处理出错，请重试！");
                    }
                    else{
                        dhx.alert("处理成功");
                        dealViceWindow.close();
                        dhxWindow.unload();
                        logGrid.clearAll();
                        logGrid.load(dwrCaller.queryProblem, "json");
                    }
                })
            }
	}
    	}
    	if(id == "close"){
    		dealViceWindow.close();
            dhxWindow.unload();
        }
	});
}
/**
 * 查看详情
 */

var queryServiceProblemDetailInfoByID=function(mainProblemId){ 	
	openTab('处理过程','portalCommon/module/serviceManage/serProManage/problemDetail.jsp?mainProblemId='+mainProblemId);
}
var openTab=function(menu_name,url){
	return parent.openMenu(menu_name,url,'top');
}
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
    var beginId=global.constant.defaultRoot;
    //创建tree Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target=form.getInput("createDeptName");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
//    tree.enableSmartRendering();
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubDept);
    //树双击鼠标事件
    tree.attachEvent("onDblClick",function(nodeId){
        form.setFormData({createDeptName:tree.getItemText(nodeId),deptId:nodeId});
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
            div.style.width = target.offsetWidth+80+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    })
     div.style.display="none";
}

/**
 * 部门树input框Html。
 * @param selectDept 已经选择了的部门。
 */
var loadDeptTreeAdd=function(selectDept,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectDept=selectDept|| global.constant.defaultRoot;
    var beginId=global.constant.defaultRoot;
    //创建tree Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target=form.getInput("acceptDept");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
//    tree.enableSmartRendering();
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubDept);
    //树双击鼠标事件
    tree.attachEvent("onDblClick",function(nodeId){
        form.setFormData({acceptDept:tree.getItemText(nodeId),deptId:nodeId});
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
            div.style.width = target.offsetWidth+80+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    })
     div.style.display="none";
}
var firstDay = function(){ 
	var Nowdate=new Date(); 
	var MonthFirstDay=(new Date(Nowdate.getFullYear(),Nowdate.getMonth(),1)); 
	return MonthFirstDay; 
}
function GetDateStr(AddDayCount) {
    var dd = new Date();
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = dd.getFullYear();
    var m = dd.getMonth()+1;//获取当前月份的日期
    var d = dd.getDate();
    return y+"-"+m+"-"+d;
}
var loadAttachmentButton=function(attachmentAddress){
	downloadattrs(attachmentAddress);
}
function downloadattrs(file)
{
	document.forms[0].target="hiddenFrame";
 	var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ file;
	window.open(url,"hiddenFrame","");
}
var utcToDate=function(utcCurrTime) {
    utcCurrTime = utcCurrTime + "";
    var date = "";
    var month = new Array();
    month["Jan"] = 1;
    month["Feb"] = 2;
    month["Mar"] = 3;
    month["Apr"] = 4;
    month["May"] = 5;
    month["Jun"] = 6;
    month["Jul"] = 7;
    month["Aug"] = 8;
    month["Sep"] = 9;
    month["Oct"] = 10;
    month["Nov"] = 11;
    month["Dec"] = 12;
    var week = new Array();
    week["Mon"] = "一";
    week["Tue"] = "二";
    week["Wed"] = "三";
    week["Thu"] = "四";
    week["Fri"] = "五";
    week["Sat"] = "六";
    week["Sun"] = "日";
    str = utcCurrTime.split(" ");
    date = str[5] + "-";
    date = date + month[str[1]]+"-"+str[2];
    return date;
}
dhx.ready(ServiceProblemInit);