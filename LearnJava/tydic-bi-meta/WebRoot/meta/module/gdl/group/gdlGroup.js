/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        gdlGroup.js
 *Description： 指标分类管理
 *
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

var dataTable = null;//表格树
var maintainWin = null;//维护分类窗体（新增，修改）
var dragFlag = false;//是否拖动过

//初始界面
function pageInit() {
    var termReq = TermReqFactory.createTermReq(1);
    var kwd = termReq.createTermControl("kwd","KEY_WORD");

    kwd.setInputEnterCall(function(){
        dataTable.refreshData();
    });

    dataTableInit(); //初始数据表格  初始之后dataTable才会被实例化
    dataTable.setReFreshCall(queryData); //设置表格刷新的回调方法，即实际查询数据的方法

    dhx.showProgress("请求数据中");
    termReq.init(function(termVals){
        dhx.closeProgress();
        dataTable.refreshData();
    }); //打包请求数据，初始，传入回调函数，里面开始查询数据

    var queryBtn = document.getElementById("queryBtn");
    var newBtn = document.getElementById("newBtn");
    var levelBtn = document.getElementById("levelBtn");
    attachObjEvent(queryBtn,"onclick",function(){
        dataTable.refreshData();
    });
    attachObjEvent(newBtn,"onclick",function(){
        showMaintainWin(0,-1);
    });
    attachObjEvent(levelBtn,"onclick",function(){
        saveTypeLevel();
    });
//    levelBtn.style.display = "none";//当层次或顺序发生改变时才出现此按钮
}

//初始数据表格树
function dataTableInit(){
    var dd = document.getElementById("dataDiv");
    var pageContent = document.getElementById("pageContent");
    var queryFormDIV = document.getElementById("queryFormDIV");
    queryFormDIV.style.width = document.body.offsetWidth -10  + "px";
    dd.style.height = pageContent.offsetHeight-queryFormDIV.offsetHeight - 6 + "px";
    dataTable = new meta.ui.DataTable("dataDiv",true);//第二个参数表示是否是表格树
    dataTable.setColumns({
        GROUP_NAME:MenuLoaclInfo.COL_TITLE,
        opt:MenuLoaclInfo.COL_OPT
    },"GROUP_NAME,GDL_GROUP_ID");
    dataTable.setRowIdForField("GDL_GROUP_ID","PAR_GROUP_ID");
    dataTable.setDynload(queryChild,"HAS_CHILD");//设置异步加载的函数接口
    dataTable.setPaging(false);//分页
    dataTable.setSorting(false);
    dataTable.render();//绘制函数，一些set方法必须在绘制函数之前，绘制函数之后内置的源生dhtmlxGrid对象被初始

    dataTable.setFormatCellCall(function(rid,cid,data,colId){
        if(cid==1){
            return "<a href='javascript:void(0)' onclick='showMaintainWin(\""+rid+"\",1);return false;'>"+MenuLoaclInfo.NEWTYPE_BTN+"</a>"+
                "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='showMaintainWin(\""+rid+"\",-1);return false;'>"+MenuLoaclInfo.NEWCHILD_BTN+"</a>"+
                "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='showMaintainWin(\""+rid+"\",0);return false;'>"+MenuLoaclInfo.MODIFY_A+"</a>"+
                "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='deleteGroup(\""+rid+"\");return false;'>"+MenuLoaclInfo.DELETE_A+"</a>";
        }
        return data[cid];
    });
    dataTable.setFormatRowCall(function(r,data){
        return r;
    });//行特殊处理回调

    dataTable.grid.enableDragAndDrop(true);
//    dataTable.grid.setDragBehavior("sibling");

    dataTable.grid.attachEvent("onBeforeDrag", function(sId){
        var sPid = dataTable.getUserData(sId,"PAR_GROUP_ID");
        if(sPid=="0"){
            this.setDragBehavior("sibling");//根分类不允许动层级
        }else{
            this.setDragBehavior("complex");//非根分类，随便拖
        }
        return true;
    });

    dataTable.grid.attachEvent("onDrag", function(sId,tId,sObj,tObj,sInd,tInd){
        var sPid = dataTable.getUserData(sId,"PAR_GROUP_ID");
        if(sPid=="0"){
            var tPid = dataTable.getUserData(tId,"PAR_GROUP_ID");
            if(sPid!=tPid)
                return false;
        }
        return true;
    });

    dataTable.grid.attachEvent("onDrop", function(sId, tId, dId, sObj, tObj, sCol, tCol){
        if(tId==undefined || tId==null)
            return false;
        dragFlag = true;
        return true;
    });

    attachObjEvent(window,"onresize",function(){
        var dd = document.getElementById("dataDiv");
        var queryFormDIV = document.getElementById("queryFormDIV");
        dd.style.width = document.body.offsetWidth - 5 + "px";
        dd.style.height = document.body.offsetHeight-queryFormDIV.offsetHeight - 6 + "px";
        dataTable.grid.setSizes();
    });

    return dataTable;
}

//查询数据
function queryData(){
    var termVals=TermReqFactory.getTermReq(1).getKeyValue();
    dhx.showProgress("请求数据中");
    GdlGroupAction.queryGroup(termVals,function(data){
        dhx.closeProgress();
        dataTable.bindData(data);
    });

}

//查询子
function queryChild(id,dataTable,params){
    dhx.showProgress("请求数据中");
    GdlGroupAction.queryChild(id,function(data){
        dhx.closeProgress();
        dataTable.loadChildData(data);  //异步加载查询的数据必须显示调用此方法绑定数据
    });

}

/**
 * 维护分类窗体（修改，新增）
 * @param rid 行ID
 * @param flag 0修改，1新增同级，-1下级
 */
function showMaintainWin(rid,flag){
    var title = "";
    var groupNameEL = document.getElementById("groupName");
    var gName = dataTable.getUserData(rid,"GROUP_NAME");
    var gId = dataTable.getUserData(rid,"GDL_GROUP_ID");
    var parName = dataTable.getUserData(rid,"PAR_GROUP_NAME");
    var parId = dataTable.getUserData(rid,"PAR_GROUP_ID");
    document.getElementById("groupId").value = "0";
    groupNameEL.value = "";
    if(flag==0){
        title = MenuLoaclInfo.WIN_TITLE_2;
        document.getElementById("parentId").value = (parId!=null?parId:"0");
        document.getElementById("parentName").value = (parName!=null?parName:MenuLoaclInfo.GROUP_ROOT);
        document.getElementById("groupId").value = gId;
        groupNameEL.value = gName;
    }else if(flag==1){
        title = MenuLoaclInfo.WIN_TITLE_1;
        if(parName){
            document.getElementById("parentId").value = parId;
            document.getElementById("parentName").value = parName;
        }else{
            document.getElementById("parentId").value = "0";
            document.getElementById("parentName").value = MenuLoaclInfo.GROUP_ROOT;
        }
    }else if(flag==-1){
        title = MenuLoaclInfo.WIN_TITLE_1;
        document.getElementById("parentId").value = gId || "0";
        document.getElementById("parentName").value = gName || MenuLoaclInfo.GROUP_ROOT;
    }
    if(!maintainWin){
        maintainWin = DHTMLXFactory.createWindow("1","maintainWin",0,0,300,150);
        maintainWin.stick();
        maintainWin.denyResize();
        maintainWin.denyPark();
        maintainWin.button("minmax1").hide();
        maintainWin.button("park").hide();
        maintainWin.button("stick").hide();
        maintainWin.button("sticked").hide();
        maintainWin.center();

        var groupFormDIV = document.getElementById("groupFormDIV");
        maintainWin.attachObject(groupFormDIV);
        var saveBtn = document.getElementById("saveBtn");
        var calBtn = document.getElementById("calBtn");
        attachObjEvent(saveBtn,"onclick",saveTypeInfo);
        attachObjEvent(document.getElementById("groupName"),"onkeyup",function(e){
            e = e || window.event;
            if(e.keyCode==13){
                saveTypeInfo();
            }
        });
        attachObjEvent(calBtn,"onclick",function(){maintainWin.close();});


        maintainWin.attachEvent("onClose",function(){
            maintainWin.setModal(false);
            this.hide();
            return false;
        });

        dhtmlxValidation.addValidation(groupFormDIV, [
            {target:"groupName",rule:"NotEmpty,MaxLength[64]"}
        ],"true");
    }
    maintainWin.setText(title);
    maintainWin.setModal(true);
    maintainWin.show();
    maintainWin.center();
    groupNameEL.focus();
}

//保存分类信息
function saveTypeInfo(){
    if(!(dhtmlxValidation.validate("groupFormDIV")))return;
    var data = Tools.getFormValues("groupForm");
    dhx.showProgress("保存数据中");
    GdlGroupAction.saveGroupInfo(data,function(ret){
        dhx.closeProgress();
        maintainWin.close();
        if(ret.flag=="error"){
            alert(MenuLoaclInfo.ALERT_ERROR);
        }else if(ret.flag=="exists"){
            alert(MenuLoaclInfo.ALERT_NAMECF);
        }else{
            alert(MenuLoaclInfo.ALERT_OPT_OK);
//                dataTable.refreshData();
            dataTable.refreshNode(data.parentId);
        }
    });
}

//删除分类
function deleteGroup(rid){
    dhx.confirm(MenuLoaclInfo.ALERT_DELETE_0,function(r){
        if(r){
            var id = dataTable.getUserData(rid,"GDL_GROUP_ID");
            var pid = dataTable.getUserData(rid,"PAR_GROUP_ID");
            GdlGroupAction.deleteGroupInfo(id,function(ret){
                if(ret.flag=="error"){
                    alert(MenuLoaclInfo.ALERT_ERROR);
                }else if(ret.flag=="true"){
//                    dataTable.refreshData();
                    dataTable.refreshNode(pid);
//                    pid = null;
                }else if(ret.flag=="false"){
                    alert(MenuLoaclInfo.ALERT_DELETE_1);
                }
            });

        }
    });
}

//确认分类层次
function saveTypeLevel(){
    if(!dragFlag){
        alert("未拖动过,无改变!");
    }else{
        var data = dataTable.getDragAfterLevelAndOrderData();
        dhx.showProgress("保存数据中");
        GdlGroupAction.saveGroupLevel(data,function(ret){
            dhx.closeProgress();
            if(ret.flag=="error"){
                alert(MenuLoaclInfo.ALERT_ERROR);
            }else if(ret.flag=="true"){
                dataTable.refreshData();
            }
        });
    }
}


dhx.ready(pageInit);