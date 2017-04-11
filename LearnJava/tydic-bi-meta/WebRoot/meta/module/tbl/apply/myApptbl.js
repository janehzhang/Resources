/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        apply.js
 *Description：
 *       表类申请JS
 *Dependent：
 *       dhtmlx.js
 *Author:
 *        杨苏维
 *Finished：
 *       2012-1-16
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
/**
 * 表类已提供的宏变量
 */
var tblMacro = [ '{YY}', '{YYYY}', '{MM}', '{DD}', '{HH}', '{MI}', '{SS}',
		'{M}', '{D}', '{YYYYMM}', '{YYYYMMN}', '{YYYYMMP}', '{YYYYMMDD}',
		'{YYYYMMDDP}', '{N_YY}', '{N_YYYY}', '{N_MM}', '{N_DD}', '{N_HH}',
		'{N_MI}', '{N_SS}', '{N_M}', '{N_D}', '{N_YYYYMM}', '{N_YYYYMMN}',
		'{N_YYYYMMP}', '{N_YYYYMMDD}', '{N_YYYYMMDDP}', '{LOCAL_CODE}',
		'{LOCAL_NAME}' ];
var dwrCaller = new biDwrCaller();
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user = getSessionAttribute("user");
var loadUserParam = new biDwrMethodParam();//loaduserData Action参数设置。
var typeStateData = getCodeByType("TABLE_STATE");


var tableTypeData = null;

/**
 * 用户页面初始化方法
 */
var myApptblInit = function() {
	var userLayout = new dhtmlXLayoutObject(document.body, "2E");
	userLayout.cells("a").setText("我的表类申请");
	userLayout.cells("b").hideHeader(true);
	userLayout.cells("a").setHeight(80);
	userLayout.cells("a").fixSize(false, true);
	userLayout.hideConcentrate();
	userLayout.hideSpliter();//移除分界边框。
	//加载查询表单
	var queryform = userLayout.cells("a").attachForm( [
	{type : "block",
		list : [ {type : "settings",position : "label-left",labelAlign : "right",inputWidth : 120,labelLeft : 0,inputLeft : 0,offsetTop : 0},
			{type : "select",label : "层次分类：",name : "tableTypeId",inputWidth : 150,inputHeight : 22,style:"padding-bottom:3px;",
			options : [ {value : "-1",text : "全部",selected : true} ]}, 
		{type : "newcolumn"}, 
		{type : "input",offsetLeft : 5,label : "业务类型：",name : "tableGroup",inputWidth : 150,inputHeight : 17}, 
		{type : "newcolumn"}, 
		{type : "combo",offsetLeft : 5,label : "状态：",name : "tableState",inputWidth : 150,inputHeight : 22,
			options : [ {value : "-1",text : "全部",selected : true}, 
				{value : "3",text : "待审批",selected : false}]}, 
		{type : "newcolumn"}, 
		{type : "input",offsetLeft : 5,label : "关键字：",name : "keyWord",inputHeight : 17,inputWidth : 120}, 
		{type : "newcolumn"}, 
		{type : "button",name : "query",value : "查询",offsetLeft : 20,offsetTop : 0} ]} 
	]);
	queryform.defaultValidateEvent();
	//定义loadUserData Action的参数来源于表单queryform数据。

    tableStateData=getComboByRemoveValue("TABLE_STATE",[0,2,6,7]);
	queryform.getCombo("tableState").addOption(tableStateData.options);

	//加载元素所属系统下拉数据
    tableTypeData=getComboByRemoveValue("TABLE_TYPE");
    tableTypeData=tableTypeData.options||tableTypeData;
    Tools.addOption(queryform.getSelect("tableTypeId"), tableTypeData);
	 var tableGroupTree = new loadTableGroupTree(queryform);
	queryform.getSelect("tableTypeId").onchange=function(){
        queryform.setFormData({tableGroup:"",tableGroupId:""})
    };

	 Tools.addEvent(queryform.getInput("tableGroup"),"click",function(){
		 var tableTypeId = queryform.getItemValue("tableTypeId");
		 //tableGroupTree.treeDiv.style.width = 100 + 'px';
		 Tools.divPromptCtrl(tableGroupTree.treeDiv,queryform.getInput("tableGroup"), true);
		 tableGroupTree.treeDiv.style.display = "block";
         tableGroupTree.showGroupTree(tableTypeId);
         dhx.env.isIE&&CollectGarbage();
	 });
	 
	 
	 var tableDataConfig = {
	    filterColumns:["tableName","tableNameCn","tableVersion", "codeName","tableGroupName","dataSourceName","tableOwner","appDate","relType","userName","stateDate","_buttons"],
	    userData:function(rowIndex, rowData) {
	        var userData = {};
	        return userData;
	    },
	    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
	    	switch (columnName) {
	    		case 'userName':
	    			if(rowData.userNamecn!=null){
						return rowData.userNamecn;
					}else{
						return "";
					}
	    			break;
	    		case 'stateDate':
					if(rowData.stateDate!=null){
						return rowData.stateDate;
	    		  	}else{
		    			  return "";
	    		  	}
	    			break;
	    		case 'tableVersion':
					if(rowData.tableVersion==1){
	    			  	return "新增";
	    		  	}else{
	    			  	return "调整";
	    		  	}
	    			break;
	    		case 'relType':
	    			for(var i=0;i<typeStateData.length;i++){
	    				if(rowData.relType==typeStateData[i].value){
	    					if(rowData.relType==0||rowData.relType==2){
	    						return "待审核";
	    					}else{
	    						return typeStateData[i].name;
	    					}
	    					break;
	    				}
	    			}
	    			break;
	    		case '_buttons':
	        		var lastRelId 	= rowData.relId,
	        			tableId 	= rowData.tableId,
		        		date 		= rowData.appDate,
		        		relType 	= rowData.relType,
		        		tname		= rowData.tableName,
		        		stateMark 	= rowData.stateMark,
		        		tableVersion= rowData.tableVersion,
	        		    tableState  = rowData.tableState,
	        		    tableTypeId = rowData.tableTypeId;
		        	
	        		if(date!=null){
		        		date = date.substring(0,10).toString().split("-");
		        		date = date[0]+date[1]+date[2];
		        	}
		        	if(relType<3){
	            		return "<a href='#' onclick='tableExamine("+tableId+","+date+","+relType+","+tableVersion+","+rowData.relId+","+1+");return false;'>查看</a>";
		           	}else if(relType == 4){
		           		if(tableTypeId != 2){
		           			return "<a href='#' onclick=\"showTableInfo('basicInfo',"+tableId+",'"+tname+"',"+tableVersion+");return false;\">查看</a> &nbsp;&nbsp;" ;
        				   // " <a href='#' onclick=\"goModify("+tableId+",'"+tname+"',"+tableVersion+");return false;\">申请调整</a>";
		           		}else{
		           			return "<a href='#' onclick=\"showTableInfo('basicInfo',"+tableId+",'"+tname+"',"+tableVersion+");return false;\">查看</a> &nbsp;&nbsp;" ;
        				   // " <a href='#' onclick=\"goModifyDim("+tableId+",'"+tname+"',"+tableVersion+");return false;\">修改基本信息</a>";
		           		}
        				
		           	}else{
		           		return "<a href='#' onclick=\"tableExamine("+tableId+","+date+","+relType+","+tableVersion+","+rowData.relId+","+2+");return false;\">查看</a> &nbsp;&nbsp;" 
		           		
        				//" <a href='#' onclick=\"showPopUpWindow('basicInfo',"+tableId+",'"+tname+"',"+tableTypeId+","+tableVersion+","+tableState+","+1+");return false;\">重新申请</a>";
		           	}
	    			break;
			}
	        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
	    }
	};
    
    
    var tableConverter = new dhtmxGridDataConverter(tableDataConfig);
    var tableParam = new biDwrMethodParam(); 
    tableParam.setParamConfig([{
            index:0,type:"fun",value:function() {
	    		var formData=queryform.getFormData();
	            formData.keyWord=Tools.trim(queryform.getInput("keyWord").value);
	            return formData;
            }
        }
    ]);
    dwrCaller.addAutoAction("queryTable","MyApptblAction.queryTablesByCondition", tableParam);
    dwrCaller.addDataConverter("queryTable",tableConverter);
    
    
    queryform.attachEvent("onButtonClick",function(id){//查询事件
    	if(id=="query"){
    		tableGrid.clearAll();
    		tableGrid.load(dwrCaller.queryTable,"json");
    		//tableGrid.defaultPaging(20);
    	}
    })
    
    
    queryform.getInput("keyWord").onkeypress=function(e){// 添加Enter查询事件
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            tableGrid.clearAll();
    		tableGrid.load(dwrCaller.queryTable,"json");
        }
	}	
    
    //表格的数据展示
    var tableGrid = userLayout.cells("b").attachGrid();
	    tableGrid.setHeader("表类名称,表类中文名,类型,层次分类,业务类型,数据源,所属用户,申请时间,状态,审核人,审核时间,操作");
		tableGrid.setInitWidthsP("10,13,7,7,7,7,7,10,5,7,10,10");
		tableGrid.setColAlign("left,left,left,left,left,left,left,left,left,left,left,left,center");
		tableGrid.setHeaderAlign("center,center,center,center,center,center,center,center,center,center,center");
		tableGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
		tableGrid.setColSorting("na,na,na,na,na,na,na,na,na,na,na,na");
		tableGrid.setEditable(false);	
		tableGrid.enableCtrlC();
		tableGrid.setColumnIds("tableName","tableNameCn","tableVersion","codeName", "tableGroupName","dataSourceName","tableOwner","appDate","relType","userName","stateDate");
		tableGrid.init();
		tableGrid.load(dwrCaller.queryTable,"json");
		tableGrid.defaultPaging(20);
}

/**
 * 查看表类申请信息
 */
var tableExamine = function(tableId,date,relType,tableVersion,lastRelId,myApp){
	openMenu("表类查看","/meta/module/tbl/examine/examineOperate.jsp?tableId="+tableId+"&date="+date+"&relType="+relType+"&tableVersion="+tableVersion+"&menuId="+menuId+"&lastRelId="+lastRelId+"&myApp="+myApp,"top",null,true);
}

/**
 * 查看通过的表类信息
 */
var showTableInfo= function(value,tableId,tname,tableVersion){
	openMenu(tname+"全息视图","/meta/module/tbl/view/viewDetail.jsp?focus="+value+
    	"&tableId="+tableId+"&tableName="+encodeURIComponent(tname)+"&tableVersion="+tableVersion,
    	"top"
    );
}

/**
 * 申请调整
 */
var goModify=function(tableId,tname,tableVersion){
    openMenu(tname+"表类维护","/meta/module/tbl/maintain/maintainDetail.jsp?focus=basicInfo"+
        "&tableId="+tableId+"&tableName="+encodeURIComponent(tname)+"&tableVersion="+tableVersion,
        "top");
};

/**
 * 申请调整
 */
var goModifyDim=function(tableId,tname,tableVersion){
    openMenu(tname+"修改维度表","/meta/module/tbl/dim/updateDim.jsp?tableId="+tableId+"&tableVersion="+tableVersion,
        "top");
};


///**
// * 跳转到维护界面
// * @param value
// */
//var showPopUpWindow=function(value,tableId,tname,tableTypeId,tableVersion,tableState,myApp){
//    //TODO 判断是否是维度表
//	if(tableTypeId != 2){
//        openMenu(tname+"表类维护","/meta/module/tbl/maintain/maintainDetail.jsp?focus="+value+
//            "&tableId="+tableId+"&tableName="+encodeURIComponent(tname)+"&tableVersion="+tableVersion+"&tableState="+tableState+"&myApp="+myApp,
//            "top");
//    }else{
//        openMenu(tname+"纬度表修改","/meta/module/tbl/maintain/maintainDetail.jsp?focus="+value+
//            "&tableId="+tableId+"&tableName="+encodeURIComponent(tname)+"&tableVersion="+tableVersion+"&tableState="+tableState+"&isDimTable=Y"+"&myApp="+myApp,"top");
//    }
//};

//跳转到维护界面
var showPopUpWindow=function(value,tableId,tname,tableTypeId,tableVersion,tableState,myApp){
	if(tableTypeId != 2){
    openMenu(tname+"表重新申请","/meta/module/tbl/apply/myTableDetails.jsp"+"?tableId="+tableId+"&tableVersion="+tableVersion
        +"&tableName="+encodeURIComponent(tname)+"&myApp="+myApp,"top");
    }else{
    	openMenu(tname+"表重新申请","/meta/module/tbl/apply/againApply.jsp?tableId="+tableId+"&tableVersion="+tableVersion+"&myApp="+myApp,"top");
    }
    
};
/**
 * 添加查询表分类Action
 */
dwrCaller.addAutoAction("queryTableGroup", "MyApptblAction.queryTableGroup");
dwrCaller.addDataConverter("queryTableGroup", new dhtmxTreeDataConverter( {
	idColumn : "tableGroupId",
	pidColumn : "parGroupId",
	textColumn : "tableGroupName",
	isDycload : false
}));
var ischange = false;
var treeData = {};
var lastTableType = null;//最后一次显示时的tableType.
dwrCaller.isShowProcess("queryTableGroup", false);


var loadTableGroupTree = function(queryform) {
	//创建tree Div层
    var div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;width: 180px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    this.treeDiv = div;
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    //树双击鼠标事件
    tree.attachEvent("onDblClick", function(nodeId){
        queryform.setFormData({tableGroup:tree.getItemText(nodeId).toString(),tableGroupId:nodeId});
        div.style.display = "none";
    });
    div.style.display = "none";
    this.tree = tree
    this.showGroupTree = function(tableTypeId){
        var show = function(){
            if(lastTableType != tableTypeId){
                var childs=tree.getAllSubItems(0);
                if(childs){
                	childs = childs+",";
                    var childIds=childs.split(",");
                    for(var i=0;i<childIds.length;i++){
                        tree.deleteItem(childIds[i]);
                    }
                }
                tree.loadJSONObject(treeData[tableTypeId]);
            }
        }
        if(!treeData[tableTypeId]){
            dwrCaller.executeAction("queryTableGroup", tableTypeId, function(data){
                treeData[tableTypeId] = data;
                show();
                	lastTableType = tableTypeId;
            })
        } else{
            show();
            lastTableType=tableTypeId;
        }
    }
}


dhx.ready(myApptblInit);
