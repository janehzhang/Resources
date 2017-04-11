/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co.Ltd.
 *All rights reserved.
 *
 *Filename：
 *        example.js
 *Description：
 *       新JS封装，测试示例代码
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

var viewTableWin = null;   //查看信息窗体
var dataTable = null;//数据表格

function classFun1(){
    return {"test":"测试参数"};//用于给后台节点动态传参数

}
function classFun2(){
    return "测试参数";//用于给后台接口动态传参数
}

/**
 * 页面初始
 */
function pageInit(){
    var termReq = TermReqFactory.createTermReq(1);
    var dataSourceTerm=termReq.createTermControl("dataSource","dataSource"); //下拉框
    var ownerTerm=termReq.createTermControl("owner","owner"); //下拉框
    var tableTypeTerm=termReq.createTermControl("tableType","tableType");//下拉框
    var tableGroupTerm=termReq.createTermControl("tableGroup","tableGroup");//下拉树
    var keyword = termReq.createTermControl("keyWord","keyWord");
    var testCode = termReq.createTermControl("testCode","TESTCODE");  //码表
    var classCombo = termReq.createTermControl("classCombo","classCombo"); //后台接口下拉框
    var classTree = termReq.createTermControl("classTree","classTree");//后台接口下拉树

    testCode.setCodeRule("PERORT_SEND_TYPE",2,4); //设置条件数据来自码表
    classCombo.setClassRule("tydic.meta.common.term.TestComboImpl",1,"",classFun1);//设置条件数据来自后台接口，可通过classFun1 来动态传回后台参数
    classTree.setClassRule("tydic.meta.common.term.TestTreeImpl",2,"",classFun2);//来自后台接口，请参考后台代码！

    dataSourceTerm.setAppendData([["","全部"]]);
    dataSourceTerm.setListRule(1,"SELECT DATA_SOURCE_ID, DATA_SOURCE_NAME "+
                            "FROM META_DATA_SOURCE WHERE DATA_SOURCE_TYPE='TABLE' AND DATA_SOURCE_STATE=1" +
                            " ORDER BY DATA_SOURCE_ID","0");   //设置条件数据来自SQL

    ownerTerm.setAppendData([["","请选择"]]);
    ownerTerm.setListRule(1,"select username a,username b from all_users order by USERNAME","");
    ownerTerm.setParentTerm(dataSourceTerm); //设置级联，即dataSourceTerm改变时，级联刷新ownerTerm
    dataSourceTerm.setValueChange(function(value){
        if(value){
            ownerTerm.dataSrcId = value[0];
        }
        if(value[0]===""){
            ownerTerm.clearValue();
            return false;
        }
        return true;
    });  //值改变回调

    tableTypeTerm.setAppendData([["","全部"]]);
    tableTypeTerm.setCodeRule("TABLE_TYPE",""); //条件数据来自码表

    //级联依赖时，SQL可以带宏变量 {termName} 指向一个条件，{fun:testfun} 指向外部一个变量或函数
    tableGroupTerm.setTreeRule(1,"SELECT T.TABLE_GROUP_ID,T.TABLE_GROUP_NAME,T.PAR_GROUP_ID " +
                            "FROM META_TABLE_GROUP T WHERE TABLE_TYPE_ID='{TABLETYPE}' ORDER BY TABLE_GROUP_ID");
    tableGroupTerm.setParentTerm(tableTypeTerm); //级联，依赖层次分类

    //设置条件文本框回车事件
    keyword.setInputEnterCall(function(){
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
    queryBtn.onclick = function(){
        dataTable.refreshData();
    };
}

/**
 * 数据表格初始
 */
function dataTableInit(){
    var dd = document.getElementById("dataDiv");
    var pageContent = document.getElementById("pageContent");
    var queryFormDIV = document.getElementById("queryFormDIV");
    dd.style.height = pageContent.offsetHeight-queryFormDIV.offsetHeight - 20 + "px";
    dataTable = new meta.ui.DataTable("dataDiv");//第二个参数表示是否是表格树
    dataTable.setColumns({
        TABLE_NAME:"表类名",
        TABLE_NAME_CN:"中文名",
        DATA_SOURCE_ID:"数据源",
        TABLE_OWNER:"所属用户",
        TABLE_TYPE_ID:"层次分类",
        opt:"操作"
//    },"TABLE_NAME,TABLE_NAME_CN,DATA_SOURCE_NAME,TABLE_OWNER,CODE_NAME");//map格式
//      },"0,1,11,2,16");//数组格式
//    },"TABLE_NAME,TABLE_NAME_CN,DATA_SOURCE_ID,TABLE_OWNER,TABLE_TYPE_ID,id,pid,opt");//map格式
    },"0,1,2,3,4,5,6");//数组格式

    dataTable.setRowIdForField(5,6); //设置行ID对应的字段索引，以及父ID对应的字段索引
    dataTable.setDynload(dynloadSub);//设置异步加载的函数接口
    dataTable.setPaging(true,20);//分页
    dataTable.setSorting(true,{
        DATA_SOURCE_ID:"asc",
//        TABLE_NAME:"",
        TABLE_NAME_CN:"",
        TABLE_OWNER:""
    },2);//排序

    dataTable.render();//绘制函数，一些set方法必须在绘制函数之前，绘制函数之后内置的源生dhtmlxGrid对象被初始

    //dataTable.grid  可对grid个性设置
//    dataTable.setGridColumnCfg(3,{type:"dhxCalendar"});
//    dataTable.setGridColumnCfg(0,{type:"sub_row_ajax"});
    dataTable.setGridColumnCfg(2,{type:"ch"});

    dataTable.setFormatCellCall(function(rid,cid,data){
        if(cid==5){
            //将第6列变成超链接，点击时弹出一个window
            return "<a href='javascript:void(0)' onclick='viewTableInfo(\""+rid+"\");return false;'>查看修改</a>";
        }else if(rid==1 && cid==3){
//            return "2010/02/03";
        }
        return data[cid];
    }); //单元格特殊处理回调

    dataTable.setFormatRowCall(function(r,data){
//        if(r.idd==2){
//            r.style.height = "55px";
//        }
        return r;
    });//行特殊处理回调
    return dataTable;
}

/**
 * 查询数据
 * @param dataTable
 * @param params
 */
function queryData(dataTable,params){
    var termVals=TermReqFactory.getTermReq(1).getKeyValue();
    termVals["COLUMN_SORT"] = params.sort;

    dhx.showProgress("请求数据中");
//    TestWCSAction.queryTables(termVals,{posStart:params.page.rowStart,count:params.page.pageSize},function(data){
    TestWCSAction.queryTableArray(termVals,{posStart:params.page.rowStart,count:params.page.pageSize},function(data){
        dhx.closeProgress();
        var total = 0;
        if(data && data[0])
            total = data[0]["TOTAL_COUNT_"];
        dataTable.bindData(data,total); //查询出数据后，必须显示调用绑定数据的方法
    });
}

/**
 * 异步加载函数，表格树时调用
 * @param id
 * @param dataTable
 * @param params
 */
function dynloadSub(id,dataTable,params){
    var termVals=TermReqFactory.getTermReq(1).getKeyValue();
    termVals["COLUMN_SORT"] = params.sort;
    termVals["PARENT_ID"] = id;
    dhx.showProgress("请求数据中");
//    TestWCSAction.queryTables(termVals,{posStart:params.page.rowStart,count:params.page.pageSize},function(data){
    TestWCSAction.queryTableArray(termVals,{posStart:params.page.rowStart,count:params.page.pageSize},function(data){
        dhx.closeProgress();
        dataTable.loadChildData(data);  //异步加载查询的数据必须显示调用此方法绑定数据
    });
}

/**
 * 查看表类信息
 */
function viewTableInfo(rid){
    if(!viewTableWin){
        viewTableWin = DHTMLXFactory.createWindow("win1","viewWin",0,0,500,300);//win对象必须通过此工厂创建
        viewTableWin.stick();
        viewTableWin.denyResize();
        viewTableWin.denyPark();
        viewTableWin.button("minmax1").hide();
        viewTableWin.button("park").hide();
        viewTableWin.button("stick").hide();
        viewTableWin.button("sticked").hide();
        viewTableWin.center();

        var tableFormDIV = document.getElementById("tableFormDIV");
        tableFormDIV.style.width = 500-20 + "px";
        viewTableWin.attachObject(tableFormDIV);
        var saveBtn = document.getElementById("saveBtn");
        var calBtn = document.getElementById("calBtn");
        attachObjEvent(saveBtn,"onclick",saveTableInfo);
        attachObjEvent(calBtn,"onclick",function(){viewTableWin.close();});


        viewTableWin.attachEvent("onClose",function(){
            viewTableWin.setModal(false);
            this.hide();
            return false;
        });
    }
    viewTableWin.setText("修改表类("+dataTable.getUserData(rid,0)+")信息");
    viewTableWin.setModal(true);
    viewTableWin.show();

    initTableForm(rid);
}

/**
 * 初始表类信息
 * @param rid
 */
function initTableForm(rid){
    var userData = dataTable.getUserData(rid);  //通过getUserData方法可获取某行的用户数据，查询了多少数据，就可获得多少数据
    var tableId = userData["TABLE_ID"];
    var tableName = userData["TABLE_NAME"];
    //...

    //设置表类信息到表单
}

/**
 * 保存表单
 */
function saveTableInfo(){

    //保存。。。如果成功则刷新表格
    if(1==1){
        dataTable.refreshData();
        viewTableWin.close();
    }
}


dhx.ready(pageInit);