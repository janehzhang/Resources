/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 王春生
 * @description
 * @date 12-4-5
 * -
 * @modify
 * @modifyDate
 * -
 */

dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath(); //获得当前服务器的根地址

var createRadioToDiv = function(div,codes,radioname){
    if(codes!=null && codes.length>0){
        for(var i=0;i<codes.length;i++){
            var span = document.createElement("SPAN");
            var ro = document.createElement("INPUT");
            ro.setAttribute("type","radio");
            ro.setAttribute("name",radioname);
            ro.setAttribute("value",codes[i].value);
            ro.setAttribute("title",codes[i].name);
            ro.setAttribute("id",radioname+codes[i].value);
            if(i==0){
                ro.setAttribute("checked","checked");
            }
            span.appendChild(ro);
            var ft = document.createElement("SPAN");
            ft.innerHTML = codes[i].name+"&nbsp;";
            span.appendChild(ft);
            div.appendChild(span);
        }
    }
};

//初始码表数据
var initCode = function(){
    var catShowMode = getCodeByRemoveValue("CATEGORY_SHOW_MODE");
    var catType= getCodeByRemoveValue("DATA_CATEGORY_TYPE",3);
    if(catShowMode){
        createRadioToDiv($("showModeDIV"),catShowMode,"showMode");
    }else{
        dhx.alert("展示方式码表数据未被初始！");
    }
    if(catType){
        createRadioToDiv($("typeDIV"),catType,"type");
    }else{
        dhx.alert("分类类型码表数据未被初始！");
    }
};
var rtGrid = null;//分类表格树
var qGrid = null;//关系查询表
var relGrid = null;//关系表
//初始界面
var initCategory = function () {
    var layout = new dhtmlXLayoutObject(document.body,"2E");
    layout.cells("a").setText("数据分类");
    layout.cells("b").hideHeader();
    layout.cells("a").setHeight(80);
    layout.cells("a").fixSize(false, true);
    layout.hideConcentrate();
//    layout.hideSpliter();//移除分界边框。

    initCode();

    layout.cells("a").attachObject("queryForm");//添加表单

    var buttonToolBar = layout.cells("b").attachToolbar();  //dhx的本身方法,用于在layout中添加按钮行
    buttonToolBar.addButton("addTong", 1, "新增根级", base + "/meta/resource/images/addMenu.png",base + "/meta/resource/images/addMenu.png");
    buttonToolBar.addButton("subLevel", 2, "确认层次更改", base + "/meta/resource/images/changeLevel.png",base + "/meta/resource/images/changeLevel.png");
    buttonToolBar.attachEvent("onclick", function(id){
        buttonEventHandle(id);
    });

    var param=new biDwrMethodParam();
    param.setParamConfig([{
        index:0,type:"fun",value:function(){
            return Tools.getFormValues('queryForm');
        }
    }]);
    var rtConverter = new dhtmlxTreeGridDataConverter({
        idColumn:"categoryId",pidColumn:"parentId",
        filterColumns:["categoryName","_buttons"],
        cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
            if (columnName == '_buttons') {
                var addT = "<a href='#' onclick='addRt("+rowData.categoryId+",0)'>新增同级</a>";
                var addS = "<a href='#' onclick='addRt("+rowData.categoryId+",1)'>新增下级</a>";
                var edit = "<a href='#' onclick='editRt("+rowData.categoryId+")'>修改</a>";
                var del = "<a href='#' onclick='delRt("+rowData.categoryId+")'>删除</a>";
                var editRel = "<a href='#' style='visibility:"+(rowData.children?'hidden':'visible')+"' onclick='editRtRel("+rowData.categoryId+")'>关联数据</a>";
                return addT+"&nbsp;&nbsp;"+addS+"&nbsp;&nbsp;"+edit+"&nbsp;&nbsp;"+del+"&nbsp;&nbsp;"+editRel;
            }
            return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
        },
        isFormatColumn:false
    });
    rtGrid = new Grid(layout.cells("b").attachGrid(),{
        headNames:"名称,操作",
        treeFlag:true,
        widthsP:"60,40",
        pageSize:0
    });
    rtGrid.genApi.setColumnCfg(1,{type:"ro",align:"center"});
    var actionurl = Tools.dwr({
        dwrMethod:DataCategoryMagAction.queryDataCategory,
        param:param,
        converter:rtConverter
    });
    rtGrid.loadData(actionurl);
};

var aouWin = null;
//尝试打开一个新增或编辑分类窗体
var showAOUWin = function(w,h,title,data){
    w = w||300;
    h = h||200 ;
    data = data || {};
    title = title || "维护分类属性";
    if(!aouWin){
        var x = new dhtmlXWindows();
        var winid = "aouWin";
        aouWin = x.createWindow(winid,0,0,w,h);
        aouWin.stick();
        aouWin.denyResize();
        aouWin.denyPark();

//        aouWin.denyMove();

        //关闭一些不用的按钮。
        aouWin.button("minmax1").hide();
        aouWin.button("park").hide();
        aouWin.button("stick").hide();
        aouWin.button("sticked").hide();

        //绑定对象
        aouWin.attachObject("categoryForm");

        //添加验证
        //对表单添加验证
        dhtmlxValidation.addValidation($("categoryForm"), [
            {target:"_categoryName",rule:"NotEmpty,MaxLength[64]"}
        ],"true");


        //改变关闭事件
        aouWin.attachEvent("onClose",function(){
            aouWin.setModal(false);
            dhtmlxValidation.clearAllTip();
            aouWin.hide();
            return false;
        });
        aouWin.hide();
    }
    aouWin.center();
    aouWin.setModal(true);
    aouWin.setText(title);
    if(aouWin.isHidden()){
        aouWin.show();
        $("_id").value=data.id || "";
        $("_parentId").value=data.parentId || 0;
        $("_parentName").value=data.parentName || "根分类";
        $("_categoryName").value=data.typeName || "";
        if(data.showMode){
            $("showMode"+data.showMode).cheched = true;
        }
        $("_categoryName").focus();
    }else{
        aouWin.close();
    }
};

var saveCat = function(){
    if(dhtmlxValidation.validate("categoryForm")){
        alert(1);
    }
};
//新增  flag=0同级  flag=1下级
var addRt = function(id,flag){
    var rtName = rtGrid.getUserData(id,"rtName");
    showAOUWin(300,200);
};
//编辑
var editRt = function(id){
    var rtName = rtGrid.getUserData(id,"rtName");
    showAOUWin(300,200);
};
//删除
var delRt = function(id){
    var gdlName = rtGrid.getUserData(id,"gdlName");
    dhx.confirm("确认将分类["+gdlName+"]删除？",function(r){
        if(r){

        }
    });
};

var relWin = null;
//尝试打开一个关系维护窗体
var showRelWin = function(w,h,data,title){
    w = w||750;
    h = h||460 ;
    data = data || {};
    title = title || "分类关联数据关系";
    if(!relWin){
        var x = new dhtmlXWindows();
        var winid = "relWin";
        relWin = x.createWindow(winid,0,0,w,h);
        relWin.stick();
        relWin.denyResize();
        relWin.denyPark();

//        aouWin.denyMove();

        //关闭一些不用的按钮。
        relWin.button("minmax1").hide();
        relWin.button("park").hide();
        relWin.button("stick").hide();
        relWin.button("sticked").hide();

        //绑定对象
        relWin.attachObject("relTable");

        //改变关闭事件
        relWin.attachEvent("onClose",function(){
            relWin.setModal(false);
            dhtmlxValidation.clearAllTip();
            relWin.hide();
            return false;
        });
        relWin.hide();
    }
    relWin.center();
    relWin.setModal(true);
    relWin.setText(title);
    if(relWin.isHidden()){
        relWin.show();

    }else{
        relWin.close();
    }
};
//保存关系信息
var saveRel = function(){

};
//表类关系
var editRtRel = function(id){
    var rtName = rtGrid.getUserData(id,"rtName");
    showRelWin(750,460,"");
};
//addData
var jiaData = function(){

};
var jiaAllData = function(){

};
var jianData = function(){

};
var jianAllData = function(){

};

//tool按钮事件
var buttonEventHandle = function(buttonId){
    if(buttonId == "subLevel"){
        alert("层级更改");
    } else{
        addRt(0,"");
    }
};


dhx.ready(initCategory);