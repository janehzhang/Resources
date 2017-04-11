/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *
 *Description：  指标分类管理JS
 *
 *Dependent：
 *
 *Author:
 *        刘斌
 *Finished：
 *       2012-03-28
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/

/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller = new biDwrCaller();

//全局分类类型CODE
var gType = null;
//全局
var gTypeName = null;

//表格转换配置
var convertConfig = {
    idColumn:"gdlGroupId",pidColumn:"parGroupId",
    filterColumns:["groupName", "_buttons"],
    isDycload:true,
    cellDataFormat:function( rowIndex,  columnIndex,columnName,  cellValue,rowData) {
        if(columnName == '_buttons'){//如果是第二列。即操作按钮列
            return "getGroupButtons";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName,cellValue, rowData);
    }
};

/**
 * JS内部类 用于数据转换
 */
var groupTreeDataConverter=new dhtmlxTreeGridDataConverter(convertConfig);

/**
 * 初始化方法
 */
var groupInit = function(){
    var groupLayout = new dhtmlXLayoutObject(document.getElementById("container"), "2E");
    groupLayout.cells("a").setText("指标分类管理");
    groupLayout.cells("b").hideHeader();
    groupLayout.cells("a").setHeight(80);
    groupLayout.cells("a").fixSize(false, true);
    groupLayout.hideConcentrate();
    groupLayout.hideSpliter();//移除分界边框。
    //加载查询表单
    var queryform = groupLayout.cells("a").attachForm([
        {type:"settings",position: "label-left", inputWidth: 120},
        {type:"combo",label:"指标分类类型：",name:"groupTypeId",inputHeight : 17
            ,readonly:true} ,
        {type:"newcolumn"},
        {type:"hidden",name:"tmp"},
        {type:"button",name:"typeMag",value:"维护指标分类类型",offsetLeft :0,offsetTop : 0}
    ]);
    queryform.getCombo("groupTypeId").addOption(getCodeComboByType("GDL_GROUP_TYPE").options);
    var loadGroupParam = new biDwrMethodParam();//loadGroup Action参数设置。
    loadGroupParam.setParamConfig([{
        index:0,type:"fun",value:function(){
            return queryform.getFormData();
        }
    }]);
    dwrCaller.addAutoAction("loadGroup","_GdlGroupAction.queryGroupTree",loadGroupParam);
    dwrCaller.addDataConverter("loadGroup", groupTreeDataConverter);
    dwrCaller.addAutoAction("loadSubGroup","_GdlGroupAction.querySubGroupTree");
    dwrCaller.addDataConverter("loadSubGroup", groupTreeDataConverter);

    var base = getBasePath();
	//button详细定义
    var buttons={
        addSame:{name:"addSame",text:"新增同级分类",imgEnabled:base+"/meta/resource/images/addSubmenu.png",
            imgDisabled :base+"/meta/resource/images/addSubmenu.png",onclick:function(rowData){
                newGroup(rowData, true);
            }},
        addKids:{name:"addKids",text:"新增下级分类",imgEnabled:base+"/meta/resource/images/users.png",
            imgDisabled :base+"/meta/resource/images/users.png",onclick:function(rowData){
                newGroup(rowData, false);
            }},
        modify:{name:"modify",text:"修改",imgEnabled:base+"/meta/resource/images/users.png",
            imgDisabled :base+"/meta/resource/images/users.png",onclick:function(rowData){
                modifyGroup(rowData);
            }},
        remove:{name:"remove",text:"删除",imgEnabled:base+"/meta/resource/images/users.png",
            imgDisabled :base+"/meta/resource/images/users.png",onclick:function(rowData){
                removeGroup(rowData.id);
            }},
        confirmLevel:{name:"confirmLevel",text:"确认层级变更",imgEnabled:base+"/meta/resource/images/ok.png",
            imgDisabled :base+"/meta/resource/images/ok.png",onclick:function(rowData){

            }}
    };
    var buttonCols=["addSame","addKids","modify","remove"];

    window["getGroupButtons"] = function(){
        var res = [];
        for(var i = 0; i < buttonCols.length; i++){
            res.push(buttons[buttonCols[i]]);
        }
        return res;
    };

    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id){
        if(id=="typeMag"){
            typeMag();
        }
    });

    myToolBar = groupLayout.cells("b").attachToolbar();
    myToolBar.addButton("addSame",1,"新增同级分类",getBasePath()+"/meta/resource/images/addSubmenu.png",getBasePath()+"/meta/resource/images/addSubmenu.png");
    myToolBar.attachEvent("onclick",function(id){
        if(id=="addSame"){
            newGroup(null, true);
        }
    });


    mygrid = groupLayout.cells("b").attachGrid();
    mygrid.setImagePath(dhtmlx.image_path+"csh_"+dhtmlx.skin+"/");
    mygrid.setHeader("指标分类,操作");
    mygrid.setHeaderBold();
    mygrid.setInitWidthsP("75,25");
    mygrid.setColAlign("left,center");
    mygrid.setHeaderAlign("left,center");
    mygrid.setColTypes("tree,sb");
    mygrid.setColSorting("na,na");
    mygrid.enableTreeGridLines();
    mygrid.setEditable(false);
    mygrid.setColumnIds("groupName,target");
    mygrid.enableDragAndDrop(true);//设置拖拽
    mygrid.setDragBehavior("complex");
//    mygrid.attachEvent("onDrag", function(sId, tId, sObj, tObj, sInd, tInd){
//        alert(sObj);
//        alert("sId " + sId);
//        alert("tId " + tId);
//        return true;
//    });


    mygrid.init();
//    mygrid.load(dwrCaller.loadGroup, "json");
    mygrid.kidsXmlFile = dwrCaller.loadSubGroup;

    queryform.getCombo("groupTypeId").attachEvent("onChange",function(){
        gType = queryform.getCombo("groupTypeId").getSelectedValue();
        gTypeName = queryform.getCombo("groupTypeId").getSelectedText();
        mygrid.clearAll();
        mygrid.load(dwrCaller.loadGroup,"json");
    });

    queryform.getCombo("groupTypeId").selectOption(0,true,true);
};

var typeWindow = null;
/**
 * 指标分类类型管理
 */
var typeMag = function(){
    if(!typeWindow){
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("typeWindow", 0, 0, 400, 200);
        typeWindow = dhxWindow.window("typeWindow");
        typeWindow.setModal(true);
        typeWindow.stick();
        typeWindow.setDimension(500, 380);
        typeWindow.center();
        typeWindow.denyResize();
        typeWindow.denyPark();
        typeWindow.button("minmax1").hide();
        typeWindow.button("park").hide();
        typeWindow.button("stick").hide();
        typeWindow.button("sticked").hide();
        typeWindow.setModal(true);
        typeWindow.attachEvent("onClose", function(){
            typeWindow.hide();
            typeWindow.setModal(false);
            return false;
        });
        typeWindow.setText("维护指标分类类型");
        typeWindow.show();
        var typeLayout = new dhtmlXLayoutObject(typeWindow, "1C");
        typeLayout.cells("a").hideHeader();
        typeLayout.cells("a").attachObject($("typeContext"));
        dhx.html.addCss($("_typeTable"), global.css.gridTopDiv);
        var ok = Tools.getButtonNode("确定");
        ok.style.marginLeft = "170px";
        ok.style.marginTop = "5px";
        ok.style.cssFloat="left";
        ok.style.styleFloat="left";
        ok.onclick=function(){
            if(validateType()){
                var typeDatas = getTypeRowData();
                _GdlGroupAction.updateGroupType(typeDatas,rmTypeId,function(r){
                    if(r){
                        dhx.alert("操作成功！",function(){
                            self.location.reload()
                        });
//                        typeWindow.close();
                    }else{
                        dhx.alert("操作失败，请重试！");
                    }

                });
            }
        };
        $("_typeWindowButton").appendChild(ok);
        var cancel=Tools.getButtonNode("关闭");
        cancel.style.styleFloat = "left";
        cancel.style.cssFloat = "left";
        cancel.style.marginTop = "5px";
        cancel.style.marginLeft = "20px";
        cancel.onclick=function(){
            typeWindow.hide();
            typeWindow.setModal(false);
        };
        $("_typeWindowButton").appendChild(cancel);
        groupTypeInit();
    }else{
        clearTypeColumnData();
        groupTypeInit();
        typeWindow.setModal(true);
        typeWindow.show();
    }
};

//分类类型维护窗口初始化界面
var groupTypeInit = function(){
    var types = getCodeByType("GDL_GROUP_TYPE");
    for(var i=0; i<types.length; i++){
        addTypeRow(types[i]);
    }

};

//清空分类类型列信息
var clearTypeColumnData=function(){
    var tableObj1 = $("_typeContentTable");
    for(var rowIndex = 1; rowIndex < tableObj1.rows.length ; rowIndex++){
        var row=  tableObj1.rows[rowIndex--];
        row.parentNode.removeChild(row);
    }
    typeRowIndex = 0;
    rmTypeId = [];
};

var typeRowIndex = 0;
var rmTypeId = [];
//添加一行分类类型数据，若rowData为空则新增一个空行
var addTypeRow = function(rowData){
    if(!rowData){
        rowData = {codeId:-1,text:"",value:""};
    }
    var tr = document.createElement("tr");
    $("_typeContent").appendChild(tr);
    tr.id="_typeContentRow_"+(typeRowIndex++);
    tr._code = rowData.value;
    tr._codeId = rowData.codeId;
    var td = tr.insertCell(0);
    td.align = "left";
    var input = dhx.html.create("input", {type:'text',name:"typeText",id:"_text_"+typeRowIndex,value:rowData.text,style:"width: 90%"});
    td.appendChild(input);
    input = dhx.html.create("input", {type:'hidden',name:"typeId",value:rowData.codeId});
    td.appendChild(input);
    td = tr.insertCell(1);
    td.align = "center";
    td.innerHTML = '<img src="../../../resource/images/cancel.png" title="删除" onclick="removeType(this)" style="width:16px;height: 16px;cursor: pointer">';
    dhtmlxValidation.addValidation(tr,[
        {target:"_text_"+typeRowIndex, rule:'MaxLength[32],NotEmpty'}
    ]);
};
//删除一行
var removeType=function(obj){
    var codeId = obj.parentNode.parentNode._code;
    _GdlGroupAction.canDeleteType(codeId,function(r){
        if(r){
            var trId = obj.parentNode.parentNode.id;
            $(trId).parentNode.removeChild($(trId));
            rmTypeId.push(obj.parentNode.parentNode._codeId);
        }else{
            dhx.alert("该类型下有指标分类，不允许删除！")
        }
    });
};

/**
 * 取得行数据
 */
var getTypeRowData = function(){
    var rtnData = [];
    var tableObj = $("_typeContent");
    for(var i = 0; i < tableObj.rows.length; i++){
        var rowData = {};
        for(var j = 0; j < tableObj.rows[i].cells.length; j++){
            var elements = tableObj.rows[i].cells[j].getElementsByTagName("input");
            for(var z = 0; z < elements.length; z++){
                if([elements[z].name]){
                    rowData[elements[z].name] = dwr.util.getValue(elements[z]);
                }
            }
        }
        if(rowData.typeText&&rowData.typeId){
            rtnData.push(rowData);
        }
    }
    return rtnData;
};

/**
 * 有效性验证
 */
var validateType = function(){
    var tableObj = $("_typeContent");
    var validateRes=true;
    for(var i = 1; i < tableObj.rows.length; i++){
        validateRes=dhtmlxValidation.validate(tableObj.rows[i])&&validateRes;
    }
    return validateRes;
};

/**
 * 新增同级分类
 * @param rowData
 */
var newGroup = function(rowData,isSameLevel){
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("addWindow", 0, 0, 250, 180);
    var addWindow = dhxWindow.window("addWindow");
    addWindow.setModal(true);
    addWindow.stick();
    addWindow.setDimension(250, 180);
    addWindow.center();
    addWindow.denyResize();
    addWindow.denyPark();
    addWindow.button("minmax1").hide();
    addWindow.button("park").hide();
    addWindow.button("stick").hide();
    addWindow.button("sticked").hide();
    addWindow.setText("新增指标分类");
    addWindow.keepInViewport(true);
    addWindow.show();
    var parText = "";
    var parId = "0";
    if(isSameLevel){//如果是新增同级
        if(rowData&&rowData.getParentId()&&rowData.getParentId()!=0){
            parText = mygrid.getRowData(rowData.getParentId()).data[0];
            parId = rowData.getParentId();
        }
    }else{//如果是新增下级
        parText = rowData.data[0];
        parId = rowData.id;
    }
    var addFormData=[
        {type:"input",offsetLeft:20,offsetTop:5,label:"分类类型：",inputWidth: 120,name:"typeName",disabled:true,value:gTypeName},
        {type:"input",offsetLeft:20,offsetTop:5,label:"上级分类：",inputWidth: 120,name:"parGroup",disabled:true,value:parText},
        {type:"input",offsetLeft:20,offsetTop:5,label:"分类名称：",inputWidth: 120,name:"groupName",validate:"NotEmpty,MaxLength[32]"},
        {type:"block",offsetTop:10,list:[
            {type:"button",label:"保存",name:"save",value:"保存",offsetLeft:60},
            {type:"newcolumn"},
            {type:"button",label:"关闭",name:"close",value:"关闭"}
        ]},
        {type:"hidden",name:"parId",value:parId},
        {type:"hidden",name:"isSameLevel",value:""+isSameLevel},
        {type:"hidden",name:"typeId",value:""+gType}
    ];
    var addForm = addWindow.attachForm(addFormData);
    //添加验证
    addForm.defaultValidateEvent();
    addForm.attachEvent("onButtonClick", function(id) {
        if(id=="save"){
            if(addForm.validate()){
                _GdlGroupAction.addGroup(addForm.getFormData(),function(data){
                    if(data){
                        dhx.alert("保存成功！");
                        mygrid.updateGrid(groupTreeDataConverter.convert(data), "insert");
                        addForm.clear();
                        addWindow.close();
                        dhxWindow.unload();
                    }else{
                        dhx.alert("保存失败，请重试！");
                    }
                });
            }
        }
        if(id == "close"){
            addForm.clear();
            addWindow.close();
            dhxWindow.unload();
        }
    })

};

/**
 * 修改指标分类
 * @param rowData
 */
var modifyGroup = function(rowData){
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("modifyWindow", 0, 0, 250, 180);
    var modifyWindow = dhxWindow.window("modifyWindow");
    modifyWindow.setModal(true);
    modifyWindow.stick();
    modifyWindow.setDimension(250, 180);
    modifyWindow.center();
    modifyWindow.denyResize();
    modifyWindow.denyPark();
    modifyWindow.button("minmax1").hide();
    modifyWindow.button("park").hide();
    modifyWindow.button("stick").hide();
    modifyWindow.button("sticked").hide();
    modifyWindow.setText("修改指标分类");
    modifyWindow.keepInViewport(true);
    modifyWindow.show();
    var parText = "";
    if(rowData.getParentId()&&rowData.getParentId()!=0){
        parText = mygrid.getRowData(rowData.getParentId()).data[0];
    }
    var modifyFormData=[
        {type:"input",offsetLeft:20,offsetTop:5,label:"分类类型：",inputWidth: 120,name:"typeName",disabled:true,value:gTypeName},
        {type:"input",offsetLeft:20,offsetTop:5,label:"上级分类：",inputWidth: 120,name:"parGroup",disabled:true,value:parText},
        {type:"input",offsetLeft:20,offsetTop:5,label:"分类名称：",inputWidth: 120,name:"groupName",validate:"NotEmpty,MaxLength[32]",value:rowData.data[0]},
        {type:"block",offsetTop:10,list:[
            {type:"button",label:"保存",name:"save",value:"保存",offsetLeft:60},
            {type:"newcolumn"},
            {type:"button",label:"关闭",name:"close",value:"关闭"}
        ]},
        {type:"hidden",name:"groupId",value:rowData.id}
    ];
    var modifyForm = modifyWindow.attachForm(modifyFormData);
    //添加验证
    modifyForm.defaultValidateEvent();

    modifyForm.attachEvent("onButtonClick", function(id) {
        if(id=="save"){
            if(modifyForm.validate()){
                _GdlGroupAction.updateGroup(modifyForm.getFormData(),function(data){
                    if(data){
                        dhx.alert("保存成功！");
                        mygrid.updateGrid(groupTreeDataConverter.convert(data), "update");
                        modifyForm.clear();
                        modifyWindow.close();
                        dhxWindow.unload();
                    }else{
                        dhx.alert("保存失败，请重试！");
                    }
                });
            }
        }
        if(id == "close"){
            modifyForm.clear();
            modifyWindow.close();
            dhxWindow.unload();
        }
    })

};

/**
 * 删除
 * @param id
 */
var removeGroup = function(id){
    if(mygrid.hasChildren(id)!=0){
        dhx.alert("该分类下有子分类，不能删除！");
        return;
    }
    dhx.confirm("确认删除该指标分类吗？",function(r){
        if(r){
            _GdlGroupAction.canBeRemoved(id,function(r){
                if(r){
                    _GdlGroupAction.removeGroup(id,function(r){
                        if(r){
                            dhx.alert("删除成功！");
                            mygrid.updateGrid([{id:id}], "delete");
                        }else{
                            dhx.alert("删除失败，请重试！");
                        }
                    });
                }else{
                    dhx.alert("该指标分类下已经有指标存在，不能删除！");
                }
            });
        }
    });
};

/**
 * 备份treegrid数据
 * @param id
 */
var backUpGridData = function(id){
    //数据备份
    var ids = [id];
    var currRowdata = {};
    var subIds = mygrid.getSubItems(id);
    if(subIds){
        ids = ids.concat(subIds.split(","));
    }
    for(var i = 0; i < ids.length; i++){
        currRowdata[ids[i]] = mygrid.getRowData(ids[i]);
    }
    return currRowdata;
};

/**
 * 还原TreeGrid数据
 * @param id
 */
var restoreGridData = function(id, currRowdata){
    var ids = [id];
    var subIds = mygrid.getSubItems(id);
    if(subIds){
        ids = ids.concat(subIds.split(","));
    }
    for(var i = 0; i < ids.length; i++){
        var cell= mygrid.cells(ids[i], 0).cell;
        mygrid.cells(ids[i], 0).cell.parentNode._attrs = currRowdata[ids[i]];
        if(cell._cellType=="img"){
            mygrid.cells(ids[i],3).cell.lastChild.style.width="18px";
            mygrid.cells(ids[i],3).cell.lastChild.style.height="18px";
        }
        if(mygrid.hasChildren(ids[i])){
            mygrid.rowsAr[ids[i]]&&(mygrid.rowsAr[ids[i]].imgTag.nextSibling.src=imagePath+"/folderOpen.gif");
        } else{
            mygrid.rowsAr[ids[i]]&&(mygrid.rowsAr[ids[i]].imgTag.nextSibling.src=imagePath+"/leaf.gif");
        }
    }
};


dhx.ready(groupInit);


