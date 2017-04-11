/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        tableView.js
 *Description：
 *       表类查询-全息视图
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *       刘斌
 *Date：
 *       2011-11-1-16-43

 ********************************************************/

/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();

//当前表基本信息
var tableBasicInfo = {};
var tableInstId;

//表列信息相关Action以及数据转换器 start
var colsConfig = {
    filterColumns:["colName","colNameCn","colDatatype","colNullabled","defaultVal","isPrimary","colBusComment","thisdim"],
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData) {
        var userData = {};
        return userData;
    }
};
var colsConverter = new dhtmxGridDataConverter(colsConfig);
dwrCaller.addAutoAction("queryColsByTableId", "TableViewAction.queryColsByTableId",tableId,tableVersion,1);
dwrCaller.addDataConverter("queryColsByTableId", colsConverter);

//表列信息相关Action以及数据转换器 end

//表基本信息相关Action
dwrCaller.addAutoAction("queryBasicInfoByTableId","TableViewAction.queryBasicInfoByTableId");

//查询维度列信息Action
dwrCaller.addAutoAction("queryDimInfoByColId","TableViewAction.queryDimInfoByColId");

//表实例查询相关Action以及数据转换器 end

//转换维表关联为链接
var transToLink=function(data){
    if(data == ""){
        return "";
    }
    var url="<a href='#' onclick='linkDim("+data+")'>维度列</a>";
    return url;
};

/**
 * 申请调整
 */
var goModify=function(){
    openMenu(tableName+"表类维护","/meta/module/tbl/maintain/maintainDetail.jsp?focus=basicInfo"+
        "&tableId="+tableId+"&tableName="+tableName+"&tableVersion="+tableVersion,
        "top");
};

/**
 * 展示建表SQL
 */
var generalSql=function(){
//    alert(tableBasicInfo["generalSql"]);
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("sqlWindow", 0, 0, 400, 300);
    var sqlWindow = dhxWindow.window("sqlWindow");
    sqlWindow.center();
    sqlWindow.denyResize();
    sqlWindow.denyPark();
    sqlWindow.setText("建表SQL详情");
    sqlWindow.keepInViewport(true);
    sqlWindow.setModal(true);

    //关闭一些不用的按钮。
    sqlWindow.button("minmax1").hide();
    sqlWindow.button("park").hide();
    sqlWindow.button("stick").hide();
    sqlWindow.button("sticked").hide();
    sqlWindow.show();
    sqlWindow.attachEvent("onClose", function(){
        sqlWindow.hide();
        sqlWindow.setModal(false);
        return false;
    });
    var sqlLayout = new dhtmlXLayoutObject(sqlWindow, "1C");
    sqlLayout.cells("a").hideHeader();
    sqlLayout.cells("a").attachObject($("_generalSqlForm"));
    $("_generalSql").innerHTML = tableBasicInfo["generalSql"];
};


/**
 * 查看维表关联
 * @param data
 */
var linkDim=function(data){
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("linkDimWindow", 0, 0, 400, 222);
    var linkDimWindow = dhxWindow.window("linkDimWindow");
    linkDimWindow.setModal(true);
    linkDimWindow.stick();
    linkDimWindow.setDimension(400, 222);
    linkDimWindow.center();
    linkDimWindow.denyResize();
    linkDimWindow.denyPark();
    linkDimWindow.setText("维表关联属性");
    linkDimWindow.keepInViewport(true);

    //关闭一些不用的按钮。
    linkDimWindow.button("minmax1").hide();
    linkDimWindow.button("park").hide();
    linkDimWindow.button("stick").hide();
    linkDimWindow.button("sticked").hide();
    linkDimWindow.show();
    linkDimWindow.attachEvent("onClose", function(){
        linkDimWindow.hide();
        linkDimWindow.setModal(false);
        return false;
    })
    var refMenuLayout = new dhtmlXLayoutObject(linkDimWindow, "1C");
    refMenuLayout.cells("a").hideHeader();
    refMenuLayout.cells("a").attachObject($("_linkDimInfoForm"));
    dwrCaller.executeAction("queryDimInfoByColId",data,tableVersion,function(data){
        if(data["TABLE_NAME"]){
            $("_dimTableName").innerHTML = data["TABLE_NAME"];
        }
        if(data["TABLE_NAME_CN"]){
            $("_dimTableNameCn").innerHTML = data["TABLE_NAME_CN"];
        }
        if(data["TABLE_BUS_COMMENT"]){
            $("_dimTableBusComment").innerHTML = data["TABLE_BUS_COMMENT"];
        }
        if(data["COL_NAME"]){
            $("_dimColName").innerHTML = data["COL_NAME"];
        }
        if(data["DIM_TYPE_NAME"]){
            $("_dimType").innerHTML = data["DIM_TYPE_NAME"];
        }
        if(data["DIM_LEVEL_NAME"]){
            $("_dimLevel").innerHTML = data["DIM_LEVEL_NAME"];
        }
    });

};

var isInitBasicInfo=false;
/**
 * 页面初始化方法
 */
var viewDetailInit=function(){

    var viewDetailLayout = new dhtmlXLayoutObject(document.getElementById("detail"), "1C");
    viewDetailLayout.cells("a").hideHeader();
    var tabbar=viewDetailLayout.cells("a").attachTabbar();
    tabbar.addTab("basicInfo", "基本信息", "120px");
    tabbar.addTab("tableRef", "表类关系", "120px");
    tabbar.addTab("tableInstance", "表类实例", "120px");
    tabbar.addTab("changeHistory", "变动历史", "120px");
    tabbar.setHrefMode("iframes-on-demand");
    tabbar.setContentHref("changeHistory",getBasePath()+"/meta/module/tbl/maintain/changeHistory.jsp?tableId="+tableId);
    tabbar.setContentHref("tableRef", getBasePath()+"/meta/module/tbl/view/viewRef.jsp?tableId="
        +tableId+"&tableVersion="+tableVersion);
    tabbar.attachEvent("onSelect", function() {
        if(arguments[0]=="basicInfo"){
            if(isDimTable=="Y"){
                tabbar.setContentHref("basicInfo", getBasePath()+"/meta/module/tbl/dim/viewDim.jsp?tableId="+tableId+"&tableVersion="+tableVersion);
                return true;
            }else{

                if(!isInitBasicInfo){
                    //基本信息TAB页
                    var basicInfoLayout=tabbar.cells("basicInfo").attachLayout("3E");
                    basicInfoLayout.hideConcentrate();//隐藏缩放按钮
                    basicInfoLayout.hideSpliter();
                    basicInfoLayout.cells("a").attachObject($("_baseInfoForm"));
                    basicInfoLayout.cells("a").hideHeader();
                    basicInfoLayout.cells("b").hideHeader();
                    basicInfoLayout.cells("a").setHeight("264");
                    basicInfoLayout.cells("b").setHeight("420");
                    basicInfoLayout.cells("c").hideHeader();
                    basicInfoLayout.cells("c").attachObject($("_buttonForm"));
                    //列信息表单
                    var colsGrid= basicInfoLayout.cells("b").attachGrid();
                    colsGrid.setHeader("名称,列中文名,类型,允许空,默认值,主键,注释,维度");
                    colsGrid.setHeaderAlign("center,center,center,center,center,center,center,center");
                    colsGrid.setInitWidthsP("15,10,10,10,10,15,15,15");
                    colsGrid.setColTypes("ro,ro,ro,ch,ro,ch,ro,ro");
                    colsGrid.enableCtrlC();
                    colsGrid.enableResizing("true,true,true,true,true,true,true,true");
                    colsGrid.setColumnIds("colName","colNameCn","colDatatype","colNullabled","defaultVal","isPrimary","colBusComment","thisdim");
                    colsGrid.setColAlign("left,left,left,center,left,center,left,center");
                    colsGrid.setColumnCustFormat(7, transToLink);
                    colsGrid.setEditable(false);
                    colsGrid.init();
                    colsGrid.load(dwrCaller.queryColsByTableId, "json");
//                    colsGrid.defaultPaging(15);
                    isInitBasicInfo=true;
                    dwrCaller.executeAction("queryBasicInfoByTableId",tableId,tableVersion,function(data){
                        tableBasicInfo=data;
                        if(tableBasicInfo["TABLE_NAME_CN"])
                            $("_tableNamecn").innerHTML = "<span style=''>"+tableBasicInfo["TABLE_NAME_CN"]+"</span>";
                        if(tableBasicInfo["TABLE_NAME"])
                            $("_tableName").innerHTML = "<span style=''>"+tableBasicInfo["TABLE_NAME"]+"</span>";
                        if(tableBasicInfo["CODE_NAME"])
                            $("_codeName").innerHTML = "<span style=''>"+tableBasicInfo["CODE_NAME"]+"</span>";
                        if(tableBasicInfo["TABLE_GROUP_NAME"])
                            $("_tableGroupName").innerHTML = "<span style=''>"+tableBasicInfo["TABLE_GROUP_NAME"]+"</span>";
                        if(tableBasicInfo["TABLE_BUS_COMMENT"])
                            $("_tableBusComment").innerHTML = "<span style=''>"+tableBasicInfo["TABLE_BUS_COMMENT"]+"</span>";
                        if(tableBasicInfo["PARTITION_SQL"])
                            $("_partitionSql").innerHTML = "<span style=''>"+tableBasicInfo["PARTITION_SQL"]+"</span>";
                        //查询出审核相关信息
                        if(data.auditData.auditDate)
                            $("_auditDate").innerHTML = data.auditData.auditDate;
                        if(data.auditData.applyUser)
                            $("_applyUser").innerHTML = data.auditData.applyUser;
                        if(data.auditData.applyDate)
                            $("_applyTime").innerHTML = data.auditData.applyDate;
                        if(data.auditData.auditUser)
                            $("_auditUser").innerHTML = data.auditData.auditUser;
                        if(data.auditData.auditMark)
                            $("_auditMark").innerHTML = data.auditData.auditMark;
                    });
                }
                return true;
            }
        }else{
            if(!isInitBasicInfo){
                //基本信息TAB页
                var basicInfoLayout=tabbar.cells("basicInfo").attachLayout("3E");
                basicInfoLayout.hideConcentrate();//隐藏缩放按钮
                basicInfoLayout.hideSpliter();
                basicInfoLayout.cells("a").attachObject($("_baseInfoForm"));
                basicInfoLayout.cells("a").hideHeader();
                basicInfoLayout.cells("b").hideHeader();
                basicInfoLayout.cells("a").setHeight("255");
                basicInfoLayout.cells("b").setHeight("420");
                basicInfoLayout.cells("c").hideHeader();
                basicInfoLayout.cells("c").attachObject($("_buttonForm"));
                //列信息表单
                var colsGrid= basicInfoLayout.cells("b").attachGrid();
                colsGrid.setHeader("名称,列中文名,类型,允许空,默认值,主键,注释,维度");
                colsGrid.setHeaderAlign("center,center,center,center,center,center,center,center");
                colsGrid.setInitWidthsP("15,10,10,10,10,15,15,15");
                colsGrid.setColTypes("ro,ro,ro,ch,ro,ch,ro,ro");
                colsGrid.enableCtrlC();
                colsGrid.enableResizing("true,true,true,true,true,true,true,true");
                colsGrid.setColumnIds("colName","colNameCn","colDatatype","colNullabled","defaultVal","isPrimary","colBusComment","thisdim");
                colsGrid.setColAlign("left,left,left,center,left,center,left,center");
                colsGrid.setColumnCustFormat(7, transToLink);
                colsGrid.setEditable(false);
                colsGrid.init();
                colsGrid.load(dwrCaller.queryColsByTableId, "json");
//                colsGrid.defaultPaging(15);
                isInitBasicInfo=true;
                dwrCaller.executeAction("queryBasicInfoByTableId",tableId,tableVersion,function(data){
                    tableBasicInfo=data;
                    if(tableBasicInfo["TABLE_NAME_CN"])
                        $("_tableNamecn").innerHTML = "<span style=''>"+tableBasicInfo["TABLE_NAME_CN"]+"</span>";
                    if(tableBasicInfo["TABLE_NAME"])
                        $("_tableName").innerHTML = "<span style=''>"+tableBasicInfo["TABLE_NAME"]+"</span>";
                    if(tableBasicInfo["CODE_NAME"])
                        $("_codeName").innerHTML = "<span style=''>"+tableBasicInfo["CODE_NAME"]+"</span>";
                    if(tableBasicInfo["TABLE_GROUP_NAME"])
                        $("_tableGroupName").innerHTML = "<span style=''>"+tableBasicInfo["TABLE_GROUP_NAME"]+"</span>";
                    if(tableBasicInfo["TABLE_BUS_COMMENT"])
                        $("_tableBusComment").innerHTML = "<span style=''>"+tableBasicInfo["TABLE_BUS_COMMENT"]+"</span>";
                    if(tableBasicInfo["PARTITION_SQL"])
                            $("_partitionSql").innerHTML = "<span style=''>"+tableBasicInfo["PARTITION_SQL"]+"</span>";
                    //查询出审核相关信息
                    if(data.auditData.applyUser)
                        $("_applyUser").innerHTML = data.auditData.applyUser;
                    if(data.auditData.applyDate)
                        $("_applyTime").innerHTML = data.auditData.applyDate;
                    if(data.auditData.auditUser)
                        $("_auditUser").innerHTML = data.auditData.auditUser;
                    if(data.auditData.auditMark)
                        $("_auditMark").innerHTML = data.auditData.auditMark;
                });
            }

            return true;
        }
    });

    //实例表类
    tabbar.setContentHref("tableInstance",getBasePath()+"/meta/module/tbl/maintain/tableInstance.jsp"+"?tableId="+tableId+"&tableVersion="+tableVersion
        +"&tableName="+tableName);
    tabbar.setTabActive(focus);
    var saveBut = Tools.getButtonNode("申请调整");
    $("_submit").appendChild(saveBut);
    saveBut.style.marginLeft = (Math.round($("_submit").offsetWidth / 2)-130) + "px";
    saveBut.style.cssFloat="left";
    saveBut.style.styleFloat="left";
    saveBut.style.marginTop= -15+"px";
    saveBut.onclick = function(){
        goModify();
    }

    var generalsql = Tools.getButtonNode("查看建表SQL");
    $("_submit").appendChild(generalsql);
    generalsql.style.marginLeft = 10+"px";
    generalsql.style.cssFloat="left";
    generalsql.style.styleFloat="left";
    generalsql.style.marginTop= -15+"px";
    generalsql.onclick = function(){
        generalSql();
    }
};

dhx.ready(viewDetailInit);