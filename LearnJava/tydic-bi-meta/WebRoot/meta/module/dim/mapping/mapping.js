/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        mapping.js
 *Description：
 *       编码映射JS文件
 *Dependent：
 *        dhtmlx.js、dwr相关JS文件、dhtmlxExtend.js、tool。js等。
 *Author:
 *        张伟
 *Finished：
 *       2011-11-16
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
var dwrCaller = new biDwrCaller();
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dimTableName = null;
var dimTablePrefix = null;
var clickCount = 0;
var dimTypeInfo = {};
var intTableOwner = null;
//归并扩展表信息，包含键值与表META_DIM_TABLES定义相同
var dimInfo = null;
var dymaicColData=null;//维表动态字段。
//接口表信息。
var dimTabIntRel=null;
//新增的映射，以SYS_ID作为第一层键值。SRC_CODE作为第二键值
var addMapping={};
//取消的映射，以SYS_ID作为第一层键值。SRC_CODE作为第二键值
var cancelMapping={}
//所有已经映射的集合，以SYS_ID作为第一键值，SRC_CODE作为第第二层键值。
var allMapping={};
//所有可以拖动的叶子节点的CODE和ID之间的映射，以DIM_TYPE_ID作为第一层键值，以CODE作为第二层键值
var codeIdMapping={};
var isAllAudit = false;
var auditMapping=null;
var path={};
var modifyStyles={
    treeMotAgreeeCheck:"text-decoration : line-through;font-style: italic;color: #808080;",
    treeSelected:"color:#64B201;",
    clear:"font-weight:normal;font-style::normal;text-decoration:none;color:black;border-bottom:0px; font-style: normal;"
};

var treeConverterConfig = {
    idColumn:"id",pidColumn:"parId",textColumn:"name",
    isDycload:true,
//    isFormatColumn:false,
    userData:function(rowIndex, rowData){
        return {rowData:rowData};
    },
    getNodeText:function(rowIndex, rowData){
        return  rowData["code"]+"--"+rowData[this.textColumn];
//        return  rowData[(dimInfo.tableDimPrefix+"_CODE").toUpperCase()]+"--"+rowData[this.textColumn];
    }
}
var treeConverter = new dhtmxTreeDataConverter(treeConverterConfig);

//动态加载维度值时所调用的参数
var codeParam = new biDwrMethodParam();
codeParam.setParamConfig([{
    index:0,type:"fun",value:function(){
        var formData = {};
		formData.tableName		=	tableName;
        formData.tableDimPrefix	=	dimInfo.tableDimPrefix;
        formData.tableUser		=	parent.tableUser;
        formData.dimType		=	dwr.util.getValue("_dimType");
        formData.lastLevelFlag	=	dimInfo.lastLevelFlag?1:0;
        return formData;
	}
}]);
dwrCaller.addAutoAction("queryDimTreeByNode", "DimMappingAction.queryDimTreeByNode",codeParam);
dwrCaller.addDataConverter("queryDimTreeByNode", new dhtmxTreeDataConverter({
    idColumn:"id",pidColumn:"parId",textColumn:"name",
    isDycload:true,
    userData:function(rowIndex, rowData){
		return {rowData:rowData};
    },
    getNodeText:function(rowIndex, rowData){
        return  rowData["code"]+"--"+rowData[this.textColumn];
//        return  rowData[(dimInfo.tableDimPrefix+"_CODE").toUpperCase()]+"--"+rowData[this.textColumn];
    }
}));
dwrCaller.isShowProcess("queryDimTreeByNode", false);			//设置是否显示进度条
//动态加载维度值时所调用的参数

//判断一个值是否在数组中
function inArrayFun(value,mappArr) {
    var type = typeof value
    for(var i in mappArr) {
        if(mappArr[i] == value) {
            return true;
        }
    }
    return false;
}
//弹出窗口数据
var upLoadWinData = [{type: "settings", position: "label-left"},
    {type:"block",className:"blockStyle",list:[
        {type:"label",label:'<div style="padding-left: 26px; " class="item_label_left"><div class="dhxlist_txt_label align_left">' +
            '<label for="_fileupload">文件地址：</label></div><div class="dhxlist_cont" style="height:20px;">' +
            '<form enctype="multipart/form-data" action="upload?fileUploadCalss=tydic.meta.module.dim.mapping.IFileUploadImpl" id="_uploadForm" method="post">' +
            '<input style="width:250px;" class="dhxlist_txt_textarea" name="iconUrl" id="_fileupload" type="FILE" accept="image/*"></form></div></div>',name:"file"},
        {type:'hidden',name: "iconUrl"}]},
    {type:"block",offsetTop:10,list:[
        {type: "settings", position: "label-left"},
        {type:"button",label:"上传",name:"upload",value:"上传",offsetLeft:150}
    ]}
];
var batchImportWin = function(){
    var dhxWindow = new dhtmlXWindows();
    var batchWindow =dhxWindow.createWindow("batchWindow", 0, 0, 300, 150);
    batchWindow.stick();
    batchWindow.button("minmax1").hide();
    batchWindow.button("park").hide();
    batchWindow.button("stick").hide();
    batchWindow.button("sticked").hide();
    batchWindow.center();
    batchWindow.denyResize();
    batchWindow.denyPark();
    batchWindow.setText("批量导入编码映射");
    batchWindow.keepInViewport(true);
    batchWindow.show();
    batchWindow.attachURL(urlEncode(getBasePath()+"/meta/module/dim/mapping/mappUpload.jsp?sysId="+sysId+"&dimTableId="+tableId+"&dimTypeId="+dwr.util.getValue("_dimType")+"&dimTableName="+tableName+"&dimTablePrefix="+dimInfo.tableDimPrefix+"&tableOwner="+dimInfo.tableOwner));
    batchWindow.attachEvent("onClose",function(){
        window.location.reload();
    })
}

var queryform=null;
var sysId=null;
var currentTypeId=null;
var gridConvertConfig = {
    filterColumns:["SRC_NAME","SRC_CODE","CODE",
        "NAME","ISMAPPING"],
    onBeforeConverted:function(data){
        var  mappingStatus=queryform.getItemValue("mappingStatus");
        sysId=queryform.getItemValue("system")
        currentTypeId= dwr.util.getValue("_dimType");
        data=data||[];
//        if(mappingStatus==0||mappingStatus==1){
        if(data){
            for(var i=0;i<data.length;i++){
                //判读标准，如果其子节点数目为0，即为最后一级，因为其不显示，将其CODE和NAME替换为父级
                if(data[i].ISMAPPING){
                    if(!dimInfo.lastLevelFlag&&!data[i].CHILDRENCOUNT){
                        data[i].CODE=data[i].PAR_CODE;
                        data[i].NAME=data[i].PAR_NAME;
                    }
                }
                var SRC_CODE=data[i].SRC_CODE
                //当状态为未映射时，去除刚新增的映射。新增刚取消的映射。
                if(mappingStatus==1){
                    if(addMapping[sysId]&&
                        addMapping[sysId][SRC_CODE]&&addMapping[sysId][SRC_CODE].SRC_CODE==data[i].SRC_CODE){
                        data.splice(i--,1);
                    }
                }else if(mappingStatus==0||mappingStatus==2){//当状态为所有/映射未审核/映射已审核时，去除已经取消的映射，新增刚新增的映射
                    if(cancelMapping[sysId]&&cancelMapping[sysId][SRC_CODE]){
                        data[i].CODE="";
                        data[i].NAME="";
                        data[i].ISMAPPING=0;
                    }
                    if(addMapping[sysId]&&addMapping[sysId][SRC_CODE]){
                        data[i].CODE=addMapping[sysId][SRC_CODE].CODE;
                        data[i].NAME=addMapping[sysId][SRC_CODE].NAME;
                        data[i].ISMAPPING=1;
                    }
                }
                //记录所有的映射。
                allMapping[sysId]=allMapping[sysId]||{};
                if(data[i].ISMAPPING==1){
                    allMapping[sysId][data[i].SRC_CODE]=data[i];
                }
            }
            //循环完成时，如果状态为未映射，如果有取消的映射。加入。
            if(mappingStatus==1&&cancelMapping[sysId]){
                for(var key in cancelMapping[sysId]){
                    data.push(cancelMapping[sysId][key]);
                }
            }else if(mappingStatus==2&&addMapping[sysId]){//未审核将新增的加入
                for(var key in addMapping[sysId]){
                    data.push(addMapping[sysId][key]);
                }
            }
        }
    },
    isFormatColumn:false,
    userData:function(rowIndex,rowData){
        return {ITEM_ID:rowData.ITEM_ID,HIS_ID:rowData.HIS_ID};
    },
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        var srcCode = rowData.SRC_CODE;
        var isAudit = inArrayFun(srcCode,auditMapping);
        if(isAudit){
            return{style:"background:#ccc",value:cellValue};
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}
var gridConverter=new dhtmxGridDataConverter(gridConvertConfig);

//加载系统数据Action
dwrCaller.addAutoAction("systemOption",DimMappingAction.systemOption,tableId,{
    dwrConfig:true,
    async:false,
    converter:new columnNameConverter()
});
//判断当前归并类型下是否有数据未审核Action
dwrCaller.addAutoAction("isAllAduit",DimMappingAction.isAllAudit,{
    dwrConfig:true,
    async:false
});
//当前归并类型下是否还有数据没有审核
var isAllAuditFun = function(){
    var currTypeId = dwr.util.getValue("_dimType");
    dwrCaller.executeAction("isAllAduit",tableId,currTypeId,function(rs){
        isAllAudit  = rs;
    })
//		DimMappingAction.isAllAudit(tableId,currTypeId,function(rs){
//		isAllAudit = rs;
//		alert(isAllAudit)
//        !rs&&dhx.alert("此维度表有映射未审核，此界面展示与审核后会不一致，" +
//                       "审核完成之前不能提交审核！");
//    });
}
//查询当前源系统中当前维度表中正在被审核的数据
dwrCaller.addAutoAction("quertMappingData",DimMappingAction.queryMappAuditData,{
    dwrConfig:true,
    async:false
});

//查询数据中已映射条数，未映射条数，未审核条数
dwrCaller.addAutoAction("queryMappingCount",DimMappingAction.queryMappingCount);

//编码映射初始化
var mappingInit=function(){
	dwrCaller.executeAction("systemOption",function(data){
        dimTabIntRel=data;
    });
    
    if(!dimTabIntRel||dimTabIntRel.length==0){
        $("_treeGrid").innerHTML="<span style='font-weight: bolder;'>表："+tableName.toUpperCase()+"未注册接口表，无法映射</span>";
        dhx.alert("表："+tableName.toUpperCase()+"未注册接口表，不能映射");
        return;
    }
    //查询是否有未审核的数据**************************************************************************************/
    //维表基本信息
    intTableOwner = dimTabIntRel[0].tableOwner;
    areaIntTableName = dimTabIntRel[0].intTabName;
    dimInfo=window.parent.dimInfo;
    dimInfo.tableOwner =parent.tableUser;
    dimInfo.intTableOwner = intTableOwner;
    dimInfo.areaIntTableName = areaIntTableName;
    dimTypeInfo=window.parent.dimTypeInfo;
    dymaicColData=window.parent.dymaicColData;
    //布局初始化
    var mappingLayout = new dhtmlXLayoutObject(document.body, "2E");
    
    //查询数据中已映射条数，未映射条数，未审核条数
    dwrCaller.executeAction("queryMappingCount",dimInfo,function(date){
    	if(date){
    		var strText = "<span style='font-weight: normal;'>维护编码映射：</span>" + tableNameCn;
    		strText += "  <span style='font-weight: normal;'>已映射数：<span style='color:red;'>"+date.mappCount+"</span></span>";
    		strText += ", <span style='font-weight: normal;'>未映射数：<span style='color:red;'>"+date.noMappCount+"</span></span>";
    		strText += ", <span style='font-weight: normal;'>未审核数：<span style='color:red;'>"+date.noAuditCount+"</span></span>";
            if(date.noAuditCount){
                queryform.disableItem("batchImport");
            }
//            unAuditCount=date.noAuditCount;
    		mappingLayout.cells("a").setText(strText);
    		if(date.dimCount>2000){
            	dhx.alert("检查到当前维度表中数据量过大，建议使用批量导入编码映射！");
    		}
    	}
    });
    mappingLayout.cells("a").setText("<span style='font-weight: normal;'>维护编码映射：</span>" + tableNameCn);
    mappingLayout.cells("b").hideHeader();
    mappingLayout.cells("a").setHeight(70);
    mappingLayout.cells("a").fixSize(false, true);
    mappingLayout.hideConcentrate();
    mappingLayout.hideSpliter();//移除分界边框。

    
    //加载查询表单
    queryform = mappingLayout.cells("a").attachForm([
        {type:"settings",position: "label-left", inputWidth: 120},
        {type:"select",label:"源系统：",name:"system"},
        {type:"newcolumn"},
        {type:"input",label:"关键字：",name:"keyWord"},
        {type:"newcolumn"},
        {type:"select",label:"映射状态：",name:"mappingStatus",options:[
            {text:"所有",value:"0" },
            {text:"已映射",value:"3" },
            {text:"尚未映射",value:"1" },
            {text:"尚未审核",value:"2" }
        ]},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"},
        {type:"newcolumn"},
        {type:"button",name:"batchImport",value:"批量导入编码映射"}
    ]);
    mappingLayout.cells("b").attachObject($("_layout"));
    //加入应用下拉数据
    Tools.addOption(queryform.getSelect("system"),new dhtmlxComboDataConverter({
        valueColumn:"sysId",
        textColumn:"sysName",
        isFormatColumn:false
    }).convert(dimTabIntRel));

    var toolBar = new dhtmlXToolbarObject("_toolbar");
    toolBar.addText("text",1,'<div style="margin-top: -4px">归并类型：<select id="_dimType" style="width: 100px"></select></div>');
    var dimTypeSelect=$("_dimType");
    var dimCount=0;
    for(var key in dimTypeInfo){
        if(key != "length"){
            dimTypeSelect.options[dimCount++]=new Option(dimTypeInfo[key].dimTypeName,dimTypeInfo[key].dimTypeId);
        }
    }
    var sysId=dwr.util.getValue(queryform.getSelect("system"));
    dwrCaller.executeAction("quertMappingData",tableId,sysId,function(mappData){
        if(mappData!=null&&mappData.length!=0){
            auditMapping = [];
            for(var i = 0 ;i<mappData.length;i++){
                auditMapping.push(mappData[i].SRC_CODE);
            }
        }
    });
    //初始化Grid
    var imagePath = dhtmlx.image_path + "csh_" + dhtmlx.skin + "/";
    dataGrid = new dhtmlXGridObject("_treeGrid");
//    dataGrid.setImagePath(imagePath);
    dataGrid.setIconPath(imagePath);
    dataGrid.setHeader("源系统,#cspan,元数据维度,#cspan,#cspan",null,["text-align:center;","text-align:center"]);
    dataGrid.attachHeader("原编码名称,原编码,维度编码,维度值,映射状态"
        ,["text-align:left;","text-align:center;","text-align:center","text-align:center;","text-align:center"]);
    dataGrid.setInitWidthsP("30,10,25,25,10");
    dataGrid.enableResizing("true,true,true,true,true");
    dataGrid.setColTypes("ro,ro,ro,ro,ro");
    dataGrid.setColAlign("left,center,center,center,center");
    dataGrid.setColSorting("na,na,na,na,na");
    dataGrid.setColumnCustFormat(4,function(value){
        return value==0?"未映射":"已映射";
    })
    dataGrid.defaultPaging(20);
    dataGrid.setEditable(false);
    dataGrid.enableDragAndDrop(true);//设置可以接受拖拽
    dataGrid.init();
    dataGrid.attachEvent("onBeforeDrag",function(){//设置grid本身不可以拖拽
        return false;
    });
//    dataGrid.render_row=function(ind){
//		if (!this.rowsBuffer[ind]){
//            return -1;
//        }
//		if (this.rowsBuffer[ind]._parser){
//			var r = this.rowsBuffer[ind];
//			if (this.rowsAr[r.idd] && this.rowsAr[r.idd].tagName=="TR")
//				return this.rowsBuffer[ind]=this.rowsAr[r.idd];
//			var row = this._prepareRow(r.idd);
//			this.rowsBuffer[ind]=row;
//			this.rowsAr[r.idd]=row;
//
//			r._parser.call(this, row, r.data);
//			this._postRowProcessing(row);
//			return row;
//		}
//		return this.rowsBuffer[ind];
//	}
    dataGrid.attachEvent("onDrag", function(sId,tId,sObj,tObj,sInd,tInd){
        if(tObj.cells(tId,4).getValue()!="未映射"){
            dhx.alert("当前行已有映射，需双击当前行取消映射才能再次映射！");
            return false;
        }
        var srcCode = tObj.cells(tId,1).getValue();
        var isAudit = inArrayFun(srcCode,auditMapping);
        if(isAudit){
            return false;
        }
        var treeRowData=sObj.getUserData(sId,"rowData");
        tObj.cells(tId,2).setValue(treeRowData["code"]);
        tObj.cells(tId,3).setValue(treeRowData["name"]);
        tObj.cells(tId,4).setValue(1);
        afterMapping(tId,sId);
        return false;//取消默认的拖动设置;
    });
    dataGrid.attachEvent("onRowDblClicked",function(rId,cInd){
        if(dataGrid.cells(rId,4).getValue()=="已映射"){
            doCancelMapping(rId)
        }
    });
    //当选中行，如果已有映射，tree也选择到映射到的code
    dataGrid.attachEvent("onRowSelect", function(id,ind){
        var code=dataGrid.cells(id,2).getValue();
        var dimType= dwr.util.getValue("_dimType");
        //查找树ID
        var treeId =null;
        try{
            treeId=codeIdMapping[dimType][code];
        }catch(exception){

        }
        dimTypeTree.selectItem(treeId);
        dimTypeTree.focusItem(treeId);
    });
    dataGrid.entBox.style.marginTop = "-4px";

	/******树搜索功能开始*******/
	Tools.addTip4Input($('searchCode'), 'input_click', '输入值可以进行字段树快速定位');
	$('searchCode').style.display = "block"
	//注册搜索区域的onkeyUps事件，用于搜索字段
    $("searchCode").onkeyup = function () {
        var searchInputValue = dwr.util.getValue("searchCode");
        if (searchInputValue) {
            dimTypeTree.findItem(searchInputValue);
        }
    }
	/******树搜索功能结束*******/
    
    $("_tree").style.height=($("_treeGridLayout").offsetHeight-100)+"px";
    $("_tree").parentNode.style.height= ($("_treeGridLayout").offsetHeight-100)+"px";
    //归并类型树初始化。
    dimTypeTree = new dhtmlXTreeObject($("_tree"), $("_tree").offsetWidth+"px", $("_tree").offsetHeight+"px", 0);
    dimTypeTree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    dimTypeTree.enableThreeStateCheckboxes(true);
    dimTypeTree.enableHighlighting(true);
    dimTypeTree.enableSingleRadioMode(true);
    dimTypeTree.setDataMode("json");
    dimTypeTree.enableDragAndDrop(true, true);
    dimTypeTree.enableMercyDrag(true);
    dimTypeTree.setXMLAutoLoading(dwrCaller.queryDimTreeByNode);
     
    //树解析完事件后调用
    dimTypeTree.attachEvent("onXLE", function (dimTree, parId){
        var dimType= dwr.util.getValue("_dimType");
        var childs = dimTypeTree.getAllSubItems(0);
        if(childs){
            var childIds = (childs + "").split(",");
            for(var i = 0; i < childIds.length; i++){
                var code=dimTypeTree.getUserData(childIds[i], "rowData")['code'];
                if(!dimTypeTree.hasChildren(childIds[i])){//无子节点为叶子节点
                    codeIdMapping[dimType]=codeIdMapping[dimType]||{};
                    codeIdMapping[dimType][code]=childIds[i];
                }
            }
        }
        if(allMapping[sysId]){
            for(var key in allMapping[sysId]){
                //寻找treeId
                var code=allMapping[sysId][key].CODE;
                try{
                    var id=codeIdMapping[dimType][code];
                }catch(exception){

                }
                if(id){
                    if(!dimInfo.lastLevelFlag){//末级显示情况下允许多对一进行拖动
                        dimTypeTree.setItemStyle(id,modifyStyles.treeSelected);
                        dimTypeTree.setUserData(id,"canDrag",true);
                    }else{
                        dimTypeTree.setItemStyle(id,modifyStyles.treeMotAgreeeCheck);
                        dimTypeTree.setUserData(id,"canDrag",false);
                    }
                }
            }
        }
    });
    
    dimTypeTree.attachEvent("onDragIn", function(dId,lId,id,sObject,tObject){
        if(!isAllAudit){
            return false;//树之间不允许拖动
        }
    });
    dimTypeTree.attachEvent("onBeforeDrag", function(sId){ //非叶子节点不允许拖动
        if(dimTypeTree.hasChildren(sId)){
            return false
        }else{
            if(dimTypeTree.getUserData(sId,"canDrag")==false){
                return false
            }
        }
        return true;
    });
    treeConverter.isOpen = function(){
        return false;
    };
    treeConverter.compare
        = function(data1, data2){
        if(data1.userdata.ORDER_ID == undefined || data1.userdata.ORDER_ID == null){
            return false;
        }
        if(data2.userdata.ORDER_ID == undefined || data2.userdata.ORDER_ID == null){
            return false;
        }
        return data1.userdata.ORDER_ID <= data2.userdata.ORDER_ID
    };
    treeConverter.afterCoverted=function(data){
        if(data){
            for(var i = 0; i < data.length; i++){
                if(data[i]._pid_temp_!=0){
                    data.splice(i--,1);
                }else{
                    data[i].open = data[i].item ? true : false;
                }
            }
        }
        return data;
    };
    
    //设置查询参数
	mappingParam.setParamConfig([
	    {    
			index:0,type:"fun",value:function(){ //索引0位的参数来源由表单提供。
	            var queryData= queryform.getFormData();
	            queryData.dimTypeId=  dwr.util.getValue("_dimType");
	            return queryData;
	    	}
	   	},
	    {
	        index:1,type:"static",value:dimInfo
	    }
	]);
    //查询数据
    query();
    queryform.attachEvent("onButtonClick", function(id){
        if(id == "query"){
            query();
        }
        if(id=="batchImport"){
          
                batchImportWin();
               
            }
    });
    dimTypeSelect.onchange=function(){
        query();
    };
    //提交审核按钮
    var sumbit = Tools.getButtonNode("提交审核");
    $("_button").appendChild(sumbit);
    sumbit.style.marginLeft = (Math.round(($("_button").offsetWidth - 100) / 2)) + "px";
//    sumbit.style.marginTop = ($("_button").offsetHeight - 20) / 2 + "px";
    sumbit.style.cssFloat = "left";
    sumbit.style.styleFloat = "left";
    sumbit.onclick = submitAudit;
}
//查询树数据action定义
dwrCaller.addAutoAction("queryDimTreeData",DimMappingAction.queryDimTreeData,{
    dwrConfig:true,
    converter:treeConverter
});
dwrCaller.isShowProcess("queryDimTreeData", false);			//设置是否显示进度条
//查询映射数据Action定义
//查询接口表参数条件
var mappingParam = new biDwrMethodParam();
dwrCaller.addAutoAction("queryDimMapping","DimMappingAction.queryDimMapping",mappingParam);
dwrCaller.addDataConverter("queryDimMapping",gridConverter);
dwrCaller.isShowProcess("queryDimMapping", true);			//设置是否显示进度条
	
/**
 * 映射完之后的样式设置与数据缓存。
 * @param gridData
 * @param treeData
 * @param treeId
 */
var afterMapping=function(gridId,treeId){
    var mappingData={};
    var srcCode=  dataGrid.cells(gridId,1).getValue();
    mappingData.SRC_CODE=srcCode;
    mappingData.SRC_NAME=dataGrid.cells(gridId,0).getValue();
    mappingData.CODE=dataGrid.cells(gridId,2).getValue();
    mappingData.NAME=dataGrid.cells(gridId,3).getValue();
    mappingData.ISMAPPING=dataGrid.cells(gridId,4).getValue();
    mappingData._ADD=true;
    mappingData.ITEM_ID=treeId;
    mappingData.DIM_TYPE_ID=currentTypeId;
    mappingData.DIM_TABLE_ID=tableId;
    if(cancelMapping[sysId]&&cancelMapping[sysId][srcCode]){
        //如果存在于取消数据中，说明此数据为取消之后再次新增。
        if(cancelMapping[sysId][srcCode].CODE==mappingData.CODE){
            //用户取消之后再次新增
            cancelMapping[sysId][srcCode]=null;
            delete cancelMapping[sysId][srcCode];
        }else{//如果该数据对应code值不一致，则表示为修改。
            addMapping[sysId]=addMapping[sysId]||{};
            addMapping[sysId][mappingData.SRC_CODE]=mappingData;
        }
    }else{
        addMapping[sysId]=addMapping[sysId]||{};
        addMapping[sysId][mappingData.SRC_CODE]=mappingData;
    }
    allMapping[sysId][mappingData.SRC_CODE]=mappingData;
    //设置树样式，设置其不能拖动。
    if(!dimInfo.lastLevelFlag){//末级显示情况下允许多对一进行拖动
        dimTypeTree.setItemStyle(treeId,modifyStyles.treeSelected);
        dimTypeTree.setUserData(treeId,"canDrag",true);
    }else{
        dimTypeTree.setItemStyle(treeId,modifyStyles.treeMotAgreeeCheck);
        dimTypeTree.setUserData(treeId,"canDrag",false);
    }
}
/**
 * 取消映射申请。
 * @param gridId
 * @param treeId
 */
var doCancelMapping=function(gridId){
    var srcCode=  dataGrid.cells(gridId,1).getValue();
    var code=dataGrid.cells(gridId,2).getValue();
    var name=dataGrid.cells(gridId,3).getValue();
    if(!(addMapping[sysId]&&addMapping[sysId][srcCode])){//如果新增记录有此CODE，说明是新增再取消，不算做是真正的取消，不记录取消申请。
        var mappingData={};
        mappingData.SRC_CODE=dataGrid.cells(gridId,1).getValue();
        mappingData.SRC_NAME=dataGrid.cells(gridId,0).getValue();
        mappingData.CODE=code;
        mappingData.NAME=name;
        mappingData.ISMAPPING=0;
        mappingData.ITEM_ID=dataGrid.getUserData(gridId,"ITEM_ID");
        mappingData.HIS_ID=dataGrid.getUserData(gridId,"HIS_ID");
        mappingData.DIM_TYPE_ID=currentTypeId;
        mappingData.DIM_TABLE_ID=tableId;
        mappingData._ADD=true;
        cancelMapping[sysId]=cancelMapping[sysId]||{};
        cancelMapping[sysId][mappingData.SRC_CODE]=mappingData;
    }
    if(addMapping[sysId]){
        addMapping[sysId][srcCode]=null;
        delete addMapping[sysId][srcCode];
    }
    if(allMapping[sysId]){
        allMapping[sysId][srcCode]=null;
        delete allMapping[sysId][srcCode];
    }
    var dimType= dwr.util.getValue("_dimType");
    //查找树ID
    var treeId = null;
    try{
        treeId =codeIdMapping[dimType][code];
    }catch(exception){

    }
    if(treeId){
    	if(!dimInfo.lastLevelFlag){//末级不显示情况下允许多对一进行拖动
            //末级不显示查看是否还有其他的多对一关联
            var allCancel=true;
            if(allMapping[sysId]){
                for(var key in allMapping[sysId]){
                    if(allMapping[sysId][key].CODE==code){
                        allCancel=false;
                        break;
                    }
                }
            }
            if(allCancel){
                dimTypeTree.setItemStyle(treeId,modifyStyles.clear);
            }
        }else{//末级显示
            dimTypeTree.setItemStyle(treeId,modifyStyles.clear);
        }
        dimTypeTree.setUserData(treeId,"canDrag",true);
    }
    dataGrid.cells(gridId,2).setValue("");
    dataGrid.cells(gridId,3).setValue("");
    dataGrid.cells(gridId,4).setValue(0);
}

/**
 * 查询。
 */
var query=function(){
    //查询已审核数据
	var sysId=dwr.util.getValue(queryform.getSelect("system"));
    dwrCaller.executeAction("quertMappingData",tableId,sysId,function(mappData){
        if(mappData!=null&&mappData.length!=0){
            auditMapping = [];
            for(var i = 0 ;i<mappData.length;i++){
                auditMapping.push(mappData[i].SRC_CODE);
            }
        }
    });
    
    //清空树数据
    var childs = dimTypeTree.getSubItems(0);
    if(childs){
        var childIds = (childs + "").split(",");
        for(var i = 0; i < childIds.length; i++){
            dimTypeTree.deleteItem(childIds[i]);
        }
    }
    //查询树数据
    var dimType=dwr.util.getValue("_dimType");
    dwrCaller.executeAction("queryDimTreeData",tableName
        ,dimInfo.tableDimPrefix,parent.tableUser,dimType,dimInfo.lastLevelFlag?1:0,
        function(data){
    		if(data){
           		dimTypeTree.loadJSONObject(data);
            }
            //查询映射数据
		    dataGrid.clearAll();
		    dataGrid.load(dwrCaller.queryDimMapping,"json");
		    $("_treeGrid").style.height=$("_treeGrid").parentNode.offsetHeight+"px";
    	}
    );
}
dwrCaller.addAutoAction("sumbitAudit",DimMappingAction.sumbitAudit);
/**
 * 提交审核
 */
var submitAudit=function(){
    var auditData={};
    auditData.sysId = dwr.util.getValue(queryform.getSelect("system"));
    var batchId=dhx.uid();
    var addMappingTemp=dhx.fullCopy(addMapping);
    var cancelMappingTemp=dhx.fullCopy(cancelMapping);
    //分三种映射申请，新增映射，修改映射，删除映射。
    //修改映射的判断标准是同时存在于新增和删除变量中。
    if(!Tools.isEmptyObject(addMappingTemp)
        &&!Tools.isEmptyObject(cancelMappingTemp)){
        var updateData=[];
        for(var key in addMappingTemp){
            if(!cancelMappingTemp[key]){
                continue;
            }
            for(var srcCode in addMappingTemp[key]){
                if(cancelMappingTemp[key][srcCode]&&!cancelMappingTemp[key][srcCode].HIS_ID){//同时
                    var temp=addMappingTemp[key][srcCode];
                    temp.SRC_SYS_ID=key;
                    temp.MOD_FLAG=8;//修改
                    temp.ITEM_CODE=temp.CODE;
                    temp.ITEM_NAME=temp.NAME;
                    temp.BATCH_ID=batchId;
                    updateData.push(temp);
//                    temp.SRC_NAME=temp.SRC_NAME.substring(temp.SRC_NAME.indexOf("->"),temp.SRC_NAME.length);
                    delete temp.CODE;
                    delete temp.NAME;
                    addMappingTemp[key][srcCode]=null;
                    delete addMappingTemp[key][srcCode];
                    cancelMappingTemp[key][srcCode]=null;
                    delete cancelMappingTemp[key][srcCode];
                }
                Tools.isEmptyObject(cancelMappingTemp[key])&&(delete cancelMappingTemp[key]);
            }
            Tools.isEmptyObject(addMappingTemp[key])&&(delete addMappingTemp[key]);
        }
        if(updateData.length>0){
            auditData.updateData=updateData;
        }
    }
    //新增申请映射
    if(!Tools.isEmptyObject(addMappingTemp)){
        var insertData=[];
        for(var srcSysId in addMappingTemp){
            for(var srcCode in addMappingTemp[srcSysId]){
                var temp=addMappingTemp[srcSysId][srcCode];
                temp.SRC_SYS_ID=srcSysId;
                temp.MOD_FLAG=2;//新增
                temp.ITEM_CODE=temp.CODE;
                temp.ITEM_NAME=temp.NAME;
                temp.BATCH_ID=batchId;
//                temp.SRC_NAME=temp.SRC_NAME.substring(temp.SRC_NAME.indexOf("->"),temp.SRC_NAME.length);
                delete temp.CODE;
                delete temp.NAME;
                insertData.push(temp);
            }
        }
        auditData.insertData=insertData;
    }
    //尚未通过审核的元素被取消
    if(!Tools.isEmptyObject(cancelMappingTemp)){
        var cancelNotAuditData=null;
        for(var srcSysId in cancelMappingTemp){
            for(var srcCode in cancelMappingTemp[srcSysId]){
                if(cancelMappingTemp[srcSysId][srcCode].HIS_ID){//存在历史ID，则未审核。
//                    var temp=cancelMappingTemp[srcSysId][srcCode];
//                    temp.SRC_SYS_ID=srcSysId;
//                    temp.MOD_FLAG=9;//删除
                    cancelNotAuditData=cancelNotAuditData||[];
                    cancelNotAuditData.push(cancelMappingTemp[srcSysId][srcCode].HIS_ID);
                    cancelMappingTemp[srcSysId][srcCode]=null;
                    delete cancelMappingTemp[srcSysId][srcCode];
                }
            }
            Tools.isEmptyObject(cancelMappingTemp[srcSysId])&&(delete cancelMappingTemp[srcSysId]);
        }
        if(cancelNotAuditData){
            auditData.cancelNotAuditData=cancelNotAuditData;
        }
    }
    //已经通过审核的删除申请
    if(!Tools.isEmptyObject(cancelMappingTemp)){
        var cancelAuditData=null;
        for(var srcSysId in cancelMappingTemp){
            for(var srcCode in cancelMappingTemp[srcSysId]){
                var temp=cancelMappingTemp[srcSysId][srcCode];
                temp.SRC_SYS_ID=srcSysId;
                temp.MOD_FLAG=9;//删除
                temp.ITEM_CODE=temp.CODE;
                temp.ITEM_NAME=temp.NAME;
                temp.BATCH_ID=batchId;
//                temp.SRC_NAME=temp.SRC_NAME.substring(temp.SRC_NAME.lastIndexOf("->"),
//                        temp.SRC_NAME.length);
                delete temp.CODE;
                delete temp.NAME;
                cancelAuditData=cancelAuditData||[];
                cancelAuditData.push(temp);
                cancelMappingTemp[srcSysId][srcCode]=null;
                delete cancelMappingTemp[srcSysId][srcCode];
            }
        }
        if(cancelAuditData){
            auditData.cancelAuditData=cancelAuditData;
        }
    }
    if(Tools.isEmptyObject(auditData)){
        dhx.alert("您未进行映射操作，不能提交审核！")
    }else{
        auditData.dymaicCloCount=dymaicColData?dymaicColData.length:0;
        auditData.dimInfo=dimInfo;
        dwrCaller.executeAction("sumbitAudit",auditData,function(data){
            if(data){
                cancelMapping={};
                addMapping={};
                dhx.alert("提交审核成功，待审核通过后生效！");
            }else{
                dhx.alert("提交失败，这有可能是由于已经有数据处于待审核中！");
            }
        })
    }

}


dhx.ready(mappingInit);
