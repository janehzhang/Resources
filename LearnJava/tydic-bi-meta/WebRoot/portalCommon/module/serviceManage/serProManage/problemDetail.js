/**
 * 
 * 查看详情js文件
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
//DWR
var dwrCaller = new biDwrCaller();
var queryDetailServiceProblemParams = new biDwrMethodParam();//查询参数
var queryAttachmentServiceProblemParams=new biDwrMethodParam();//查询附件参数
var user= getSessionAttribute("user");
var queryDetailform;
/**
 * 初始化函数
 */
var ServiceProblemDetailInit = function() {
	//第一步，先建立一个Layout
	var visitDetailInfoLayout = new dhtmlXLayoutObject(document.body, "2E");
	visitDetailInfoLayout.cells("a").setText("处理过程");
	visitDetailInfoLayout.cells("b").hideHeader();
	visitDetailInfoLayout.cells("a").setHeight(75);
	visitDetailInfoLayout.cells("a").fixSize(false, true);
	visitDetailInfoLayout.hideConcentrate();
	visitDetailInfoLayout.hideSpliter();
	//添加查询表单
    queryDetaiform = visitDetailInfoLayout.cells("a").attachForm( [ 
        {type : "input",label : "发起部门：",name : "createDeptName",inputWidth: "70"},
        {type : "newcolumn"},
        {type : "input",label : "处理部门：",name : "dealDeptName",inputWidth: "70"},
        {type : "newcolumn"},
        {type : "input",label : "主题：",name : "theme",inputWidth: "90"},
        {type : "newcolumn"},
        {type : "combo",label : "来源：",name : "source",options:[{value:"",text:"全部",selected:true}],inputWidth:70,inputHeight:22,readonly:true},
        {type:"newcolumn"},
        {type : "combo",label : "状态：",name : "dealState",options:[{value:"",text:"全部",selected:true}],inputWidth:70,inputHeight:22,readonly:true},
        {type : "newcolumn"},
        {type : "combo",label : "附件类型：",name : "attachmentType",options:[{value:"",text:"全部",selected:true}],inputWidth:70,inputHeight:22,readonly:true},
        {type : "newcolumn"},
        {
            type : "button",
            name : "queryDetail",
            value : "查询",
            offsetLeft:10
        }
        ]); 
	queryDetaiform.setFormData( {
		"mainProblemId" : mainProblemId
	});
    //创建下拉列表
    var tableTypeData = getComboByRemoveValue("PROBLEM_SOURCE");
    queryDetaiform.getCombo("source").addOption(tableTypeData.options); 
    var tableStepData = getComboByRemoveValue("PROBLEM_STEP");
    queryDetaiform.getCombo("dealState").addOption(tableStepData.options);
    var attachmentTypeData = getComboByRemoveValue("ATTACHMENT_TYPE");
    queryDetaiform.getCombo("attachmentType").addOption(attachmentTypeData.options);
    loadDeptTreeDetail(user.zoneId,queryDetaiform);
    loadDeptTree(user.zoneId,queryDetaiform);
    
   // var startDate = queryDetaiform.getCalendar("startDate");
	//var endDate = queryDetaiform.getCalendar("endDate");
	
	//将日历控件语言设置成中文
	//startDate.loadUserLanguage('zh');
	////endDate.loadUserLanguage('zh');

	//将未来的日期设定为不可操作
	//var today = new Date();
	//var tomarrow = new Date();
	//tomarrow.setDate(tomarrow.getDate() + 2);
	//startDate.setInsensitiveRange(tomarrow, null);
	//endDate.setInsensitiveRange(tomarrow, null);

	//日历改变事件，使startDate不晚于endDate，endDate不早于startDate
	//startDate.attachEvent("onClick", function(date) {
	//	endDate.setSensitiveRange(date, today);
	//});
	//endDate.attachEvent("onClick", function(date) {
	//	date.setDate(date.getDate()+1);
	//	startDate.setInsensitiveRange(date, null);
	//});
	/**
	 * 查询问题详情的响应函数
	 * @param {Object} data
	 */
	dwrCaller.addAutoAction("queryDetailServiceProblem", "SerProManageAction.queryDetailServiceProblem",
			queryDetailServiceProblemParams, function(data) {
			});
	dwrCaller.addDataConverter("queryDetailServiceProblem", new dhtmxGridDataConverter( {
		idColumnName : "dealProblemId",
		filterColumns : ["theme","problemDescription","source","urgencyDegree","processLimited","problemType","dealState","dealActorName","dealTime","dealOpinion","attachmentName","attachmentAddress"],
		cellDataFormat : function(rowIndex, columnIndex, columnName, cellValue,
				rowData) {
			 if(columnName=="attachmentName"){
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
	queryDetailServiceProblemParams.setParamConfig( [ {
		index : 0,
		type : "fun",
		value : function() {
			return queryDetaiform.getFormData();
		}
	} ]);
		
	//详细访问信息列表
	var visitDetailInfoGrid = visitDetailInfoLayout.cells("b").attachGrid();
	visitDetailInfoGrid.setHeader("主题,问题描述,来源,紧急程度,处理时限,问题类别,环节,处理人,处理时间,处理说明,附件");
	visitDetailInfoGrid.setInitWidthsP("8,12,8,8,8,8,8,7,13,10,10");
	visitDetailInfoGrid.setColAlign("left,left,left,left,right,left,left,left,right,left,left");
	visitDetailInfoGrid.setHeaderAlign("center,center,center,center,center,center,center,center,center,center,center");
	visitDetailInfoGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
	visitDetailInfoGrid.enableCtrlC();
	visitDetailInfoGrid.setColSorting("na,na,na,na,na,na,na,na,na,na,na");
	visitDetailInfoGrid.setEditable(false);
	visitDetailInfoGrid
			.setColumnIds("theme,problemDescription,source,urgencyDegree,processLimited,problemType,dealState,dealActorName,dealTime,dealOpinion,attachmentAddress");
	visitDetailInfoGrid.enableTooltips("true,true,true,true,true,true,true,true,true,true,true");
	visitDetailInfoGrid.init();
	visitDetailInfoGrid.defaultPaging(20);
	visitDetailInfoGrid.load(dwrCaller.queryDetailServiceProblem,"json");
	//查询响应函数
	queryDetaiform.attachEvent("onButtonClick", function(id) {
		if (id == "queryDetail") {
			visitDetailInfoGrid.clearAll();
			visitDetailInfoGrid.load(dwrCaller.queryDetailServiceProblem, "json");
		}
	});
	
/*	*//**
	 * 查询附件响应函数
	 * @param {Object} data
	 *//*
	dwrCaller.addAutoAction("queryAttachmentServiceProblem", "SerProManageAction.queryAttachmentServiceProblem",
			queryAttachmentServiceProblemParams, function(data) {
			});
	dwrCaller.addDataConverter("queryAttachmentServiceProblem", new dhtmxGridDataConverter( {
		idColumnName : "dealProblemId",
		filterColumns : ["rn","attachmentName","attachmentType","attachmentAddress","_buttons" ],
		cellDataFormat : function(rowIndex, columnIndex, columnName, cellValue,
				rowData) {
			if (columnName == "_buttons") {//操作按钮列
				return "getButtons";
			} else {
				return this._super.cellDataFormat(rowIndex, columnIndex,
						columnName, cellValue, rowData);
			}
		}

	}));
	queryDetaiform.setFormData( {
		"mainProblemId" : mainProblemId
	});
	//设置查询参数，来自于queryform表单
	queryAttachmentServiceProblemParams.setParamConfig( [ {
		index : 0,
		type : "fun",
		value : function() {
			return queryDetaiform.getFormData();
		}
	} ]);
	
	//附件下载按钮添加
	var buttons = {
			loadAttachmentByID : {
			name : "loadAttachmentByID",
			text : "操作",
			imgEnabled : getBasePath() + "/meta/resource/images/view.png",
			imgDisabled : getBasePath() + "/meta/resource/images/view.png",
			onclick : function(rowData) {
			}
		}
	};
	var buttonCol = [ "loadAttachmentByID" ];
	window["getButtons"] = function() {
		var res = [];
		for ( var i = 0; i < buttonCol.length; i++) {
			if(buttonCol[i] == "loadAttachmentByID"){
				buttons["loadAttachmentByID"].text="下载";
        		res.push(buttons["loadAttachmentByID"])
			}else{
				res.push(buttons[buttonCol[i]]);
			}
		}
		return res;
	};*/
		
/*	//添加附件表单
	visitDetailInfoLayout.cells("c").setText("相关附件");
	//详细访问信息列表
	var visitAttachmentInfoGrid = visitDetailInfoLayout.cells("c").attachGrid();
	visitAttachmentInfoGrid.setHeader("序号,附件名称,附件类型,附件地址,操作");
	visitAttachmentInfoGrid.setInitWidthsP("10,20,30,30,10");
	visitAttachmentInfoGrid.setColAlign("right,left,left,left,left");
	visitAttachmentInfoGrid.setHeaderAlign("center,center,center,center,center");
	visitAttachmentInfoGrid.setColTypes("ro,ro,ro,ro,ro");
	visitAttachmentInfoGrid.enableCtrlC();
	visitAttachmentInfoGrid.setColSorting("na,na,na,na,na");
	visitAttachmentInfoGrid.setEditable(false);
	visitAttachmentInfoGrid
			.setColumnIds("rn,attachmentName,attachmentType,attachmentAddress,target");
	visitAttachmentInfoGrid.enableTooltips("true,true,true,true,false");
	visitAttachmentInfoGrid.init();
	visitAttachmentInfoGrid.defaultPaging(20);
	visitAttachmentInfoGrid.load(dwrCaller.queryAttachmentServiceProblem,"json");*/
};

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
        form.setFormData({createDeptName:tree.getItemText(nodeId),createDeptId:nodeId});
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
var loadDeptTreeDetail=function(selectDept,form){
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
    var target=form.getInput("dealDeptName");
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
        form.setFormData({dealDeptName:tree.getItemText(nodeId),dealDeptId:nodeId});
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
dhx.ready(ServiceProblemDetailInit);
