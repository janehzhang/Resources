/**
 * 
 * 副单待处理js文件
 */
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
	logLayout.cells("a").setText("副单待处理");
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
        {type : "input",label : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;发起部门：",name : "createDeptName",inputWidth: "80"},
        {type : "newcolumn"},
        {type : "input",label : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;主题：",name : "theme",inputWidth: "100"},
        {type : "newcolumn"},
        {type : "combo",label : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;来源：",name : "source",options:[{value:"",text:"全部",selected:true}],inputWidth:70,inputHeight:22,readonly:true},
        {type:"newcolumn"},
        {type : "combo",label : "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;状态：",name : "problemStep",options:[{value:"",text:"全部",selected:true}],inputWidth:70,inputHeight:22,readonly:true},
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
	tomarrow.setDate(tomarrow.getDate() + 2);
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
	dwrCaller.addAutoAction("queryServiceProblem", "SerProManageAction.queryServiceProblem",
			queryServiceProblemParams, function(data) {
			});
	dwrCaller.addDataConverter("queryServiceProblem", new dhtmxGridDataConverter( {
		idColumnName : "mainProblemId",
		filterColumns : [ "theme", "problemDescription", "source", "urgencyDegree",
				"processLimited", "problemType", "createTime","createActorName","isEvaluationPlan","attachmentName","opr","attachmentAddress"],
		cellDataFormat : function(rowIndex, columnIndex, columnName, cellValue,
				rowData) {
			if (columnName == "opr") {//操作按钮列
				return "getDetailButtons";
			} else if(columnName=="attachmentName"){
				if(cellValue!="无"){
				var str ="<a href=\"javascript:void(0)\"  onclick=\"loadAttachmentButton('"+rowData.attachmentAddress+"')\" >"+cellValue+"</a>";
	    		return str;	
			}
		}
		     	return this._super.cellDataFormat(rowIndex, columnIndex,
						columnName, cellValue, rowData);
		}

	}));
	queryform.setFormData( {
		"mainstate" : "2"
	});
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
    var base = getBasePath();
	var buttons = {
		queryServiceProblemDetailInfoByID : {
			name : "button1",
			text : "查看详情",
			imgEnabled : getBasePath() + "/meta/resource/images/view.png",
			imgDisabled : getBasePath() + "/meta/resource/images/view.png",
			onclick : function(rowData) {
				queryServiceProblemDetailInfoByID(rowData.id);
			}
		},
		dealServiceProblem : {
			name : "dealServiceProblem",
			text : "处理",
			imgEnabled : getBasePath() + "/meta/resource/images/view.png",
			imgDisabled : getBasePath() + "/meta/resource/images/view.png",
			onclick : function(rowData) {
			dealServiceProblem(rowData.id);
			}
		}
	};
	var buttonRole = ["dealServiceProblem","queryServiceProblemDetailInfoByID"];
    var buttonRoleCol = [];
    for(var i=0; i<buttonRole.length; i++){
        if(buttonRole[i]!="addServiceProblemDetailInfoByID"){
            buttonRoleCol.push(buttonRole[i]);
        }
    }
    
	window["getDetailButtons"] = function() {
		var res = [];
		for ( var i = 0; i < buttonRoleCol.length; i++) {
			if(buttonRoleCol[i] == "queryServiceProblemDetailInfoByID"){
        		buttons["queryServiceProblemDetailInfoByID"].text="查看详情";
        		res.push(buttons["queryServiceProblemDetailInfoByID"])
			}else{
				res.push(buttons[buttonRoleCol[i]]);
			}
		}
		return res;
	};

	//添加grid,用于将查询出来的日志记录显示出来
	logGrid = logLayout.cells("b").attachGrid();
	logGrid.setHeader("主题,问题描述,来源,紧急程度,处理时限,问题类别,建单时间,建单人,评估计划,参考资料,操作");
	logGrid.setInitWidthsP("8,10,8,7,7,10,15,7,6,9,13");
	logGrid.setColAlign("center,left,left,left,right,left,right,left,left,left,center");
	logGrid.setHeaderAlign("center,center,center,center,center,center,center,center,center,center,center");
	logGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,sb");
    logGrid.enableCtrlC();
	logGrid.setColSorting("na,na,na,na,na,na,na,na,na,na,na");
	logGrid.setEditable(false);
	logGrid.setColumnIds("theme,problemDescription,source,urgencyDegree,processLimited,problemType,createTime,createActorName,isEvaluationPlan,attachmentName,target");
	logGrid.enableTooltips("true,true,true,true,true,true,true,true,true,true,false");
	logGrid.init();
    logGrid.defaultPaging(20);
	logGrid.load(dwrCaller.queryServiceProblem, "json");
	//查询响应函数
	queryform.attachEvent("onButtonClick", function(id) {
		if (id == "query") {
			logGrid.clearAll();
			logGrid.load(dwrCaller.queryServiceProblem, "json");
		}
	});
};
dwrCaller.addAutoAction("nextViceServiceProblem","SerProManageAction.nextDealServiceProblem");

/**
 * 副单处理
 */
var dealServiceProblem=function(mainProblemId){
    //初始化处理弹出窗口。
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("dealWindow", 0, 0, 350, 1000);
    var dealWindow = dhxWindow.window("dealWindow");
    dealWindow.setModal(true);
    dealWindow.stick();
    dealWindow.setDimension(550, 300);
    dealWindow.center();
    dealWindow.setPosition(dealWindow.getPosition()[0],dealWindow.getPosition()[1]-50);
    dealWindow.denyResize();
    dealWindow.denyPark();
    dealWindow.button("minmax1").hide();
    dealWindow.button("park").hide();
    dealWindow.button("stick").hide();
    dealWindow.button("sticked").hide();
    dealWindow.setText("处理服务问题单");
    dealWindow.keepInViewport(true);
    dealWindow.show(); 
    queryDetaiform1 = dealWindow.attachForm( [ 
        {type : "newcolumn"},
        {type:"block",offsetTop:15,list:[
        {type:"input",offsetLeft:20,rows:4,label:"处理意见：",inputWidth: 397,name:"dealOpinion",validate:"NotEmpty,MaxLength[600]"},
        {type:"newcolumn"},
        {type:"label",label:"<span style='color: red'>*</span>"}
        ]},
        {type : "newcolumn"},
        {type : "input",offsetLeft:20,label : "人员选择：",name : "nextActorName",inputWidth: "150"},
        {type:"hidden",name:'nextActorId',id:'nextActorId'},
        {type:"hidden",name:'nextDeptId',id:'nextDeptId'},
        {type:"hidden",name:'nextDeptName',id:'nextDeptName'},
        
        {type:"hidden",name:"dealActorId",value:user.userId},
        {type:"hidden",name:"dealActorName",value:user.userNamecn},
        {type:"hidden",name:"dealDeptId",value:user.deptId},
        {type:"hidden",name:"dealDeptName",value:user.deptName},
        {type : "newcolumn"},
        {type:"combo",offsetLeft:20,label:"短信通知：",name:"isSMSNotice",inputWidth:"150",readonly:true},
        {type : "newcolumn"},
        {type : "input",offsetLeft:20,label : "联系电话：",name : "nextActorNo",inputWidth: "150"},
        {type : "newcolumn"},
        {type:"combo",offsetLeft:20,label:"附件类型：",inputWidth: 150,name:"attachmentType",readonly:true},
        {type:"newcolumn"},
        {type:"input",offsetLeft:20,label:"附件名称：",inputWidth: 150,name:"attachmentName"},
        {type:"newcolumn"},
        {type:"block",className:"blockStyle",list:[
        {type:"label",label:'<div style="padding-left: 13px; " class="item_label_left"><div class="dhxlist_txt_label align_left">' +
        '<label for="_fileupload">附件地址：</label></div><div class="dhxlist_cont" style="height:20px;">' +
        '<form enctype="multipart/form-data" action="upload" name="uploadForm" id="_uploadForm" method="post">' +
        '<input style="width:250px;" class="dhxlist_txt_textarea" name="attachmentAddress" id="_fileupload" type="FILE" accept="images/*"></form></div></div>',name:"file",labelWidth:620},
        {type:'hidden',name: "attachmentAddress"}]}, 
        {type:"newcolumn"},
        {type:"block",offsetTop:10,list:[
        {type:"button",label:"确认：",name:"nextDeal",value:"确认",offsetLeft:200},
        {type:"newcolumn"},
        {type:"button",label:"关闭",name:"close",value:"关闭"}
        ]}
        ]);
    var tableAttachmentData = getComboByRemoveValue("ATTACHMENT_TYPE");
    queryDetaiform1.getCombo("attachmentType").addOption(tableAttachmentData.options);
    var isSMSNoticeData = getComboByRemoveValue("IS_BOOL");//是否短信通知
    queryDetaiform1.getCombo("isSMSNotice").addOption(isSMSNoticeData.options);
    //loadDeptTreeNext("",queryDetaiform1);
    //加载人员选择树     
    var target=queryDetaiform1.getInput("nextActorName");
     Tools.addEvent(target,"click",function(){
     	         var url=base+"/meta/module/mag/selectedUser/ryxz.jsp";
 				var obj= window.showModalDialog(url+'?t='+Math.random(),'newWin','center:yes;status:no;scroll:no;help:no;dialogWidth=800px;dialogHeight=500px');
 				if (typeof(obj) != "undefined") { 
 				       target.value=obj.names;
 				       queryDetaiform1.setFormData({nextActorId:obj.ids});
 				       queryDetaiform1.setFormData({nextDeptId:obj.deptIds});
 				       queryDetaiform1.setFormData({nextDeptName:obj.deptNames});
 				       
 				}
         });
	//派送响应函数
	queryDetaiform1.setFormData( {
		"mainProblemId" : mainProblemId
	});
    $("_uploadForm").setAttribute("action",urlEncode(getBasePath()+"/portalCommon/module/serviceManage/serProManage/attachmentPost.jsp"));
    queryDetaiform1.defaultValidateEvent();
    queryDetaiform1.attachEvent("onButtonClick", function(id) {
        var data = queryDetaiform1.getFormData();     
		var validateAdd=function(){
			if(data.isSMSNotice==1){
    			if(data.nextActorNo==null){
    				alert('请填写联系电话');
    				return false;
    			}if(isNaN(data.nextActorNo)){
    				alert("联系电话输入有误(号码为数字)");
    				return false;
    			}
    		}
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
    	if(queryDetaiform1.validate()&&validateAdd()){
	    if (id == "nextDeal") {
            //先上传文件
            var file=null;
            var supportFile=['png','gif','jpg','jpeg','doc','txt','.xls','pdf'];
            if(file=dwr.util.getValue("_fileupload")){
                if(new RegExp("["+supportFile.join("|")+"]$","i").test(file)){
                    var iframeName="_uploadFrame";
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
                                        dealWindow.close();
                                        dhxWindow.unload();
                                        logGrid.clearAll();
                                        logGrid.load(dwrCaller.queryServiceProblem, "json");
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
                                        dealWindow.close();
                                        dhxWindow.unload();
                                        logGrid.clearAll();
                                        logGrid.load(dwrCaller.queryServiceProblem, "json");
                                    }
                                })
                            };
                        }
                    }
                    var fm = $("_uploadForm");
                    fm.target = iframeName;
                    $("_uploadForm").submit();
                }else{
                    dhx.alert("请选择一个图片文件，支持格式png/gif/jpg/jpeg/doc/txt/pdf/xls");
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
                        dealWindow.close();
                        dhxWindow.unload();
                        logGrid.clearAll();
                        logGrid.load(dwrCaller.queryServiceProblem, "json");
                    }
                })
            }
	}
    	}
    	if(id == "close"){
    		dealWindow.close();
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
var loadDeptTreeNext=function(selectDept,form){
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
    var target=form.getInput("nextDept");
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
        form.setFormData({nextDept:tree.getItemText(nodeId),deptId:nodeId});
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

//下载附件
var loadAttachmentButton=function(attachmentAddress){
   	downloadattrs(attachmentAddress);
   }
function downloadattrs(file){
	document.forms[0].target="hiddenFrame";
    var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ file;
   	window.open(url,"hiddenFrame","");
   }
dhx.ready(ServiceProblemInit);
