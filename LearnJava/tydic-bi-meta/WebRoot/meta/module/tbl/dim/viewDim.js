/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        viewDim.js
 *Description：
 *       作用，描述引用了
 *Dependent：
 *       写依赖文件
 *Author:
 *        张伟
 *Finished：
 *       2011-11-21
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
var dwrCaller = new biDwrCaller();
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var tableBasicInfo=null;

//表列信息相关Action以及数据转换器 start
var colsConfig = {
    filterColumns:["colName","colDatatype","colNullabled","defaultVal","isPrimary","colBusComment","dimColId"]
};
var colsConverter = new dhtmxGridDataConverter(colsConfig);

dwrCaller.addAutoAction("queryTableByIdAndVersion",TableDimAction.queryTableByIdAndVersion,tableId,tableVersion);
dwrCaller.addAutoAction("queryAllColumnsByTableID", TableDimAction.queryAllColumnsByTableID,tableId,tableVersion,{
    dwrConfig:true,
    converter:colsConverter
});
//查询维度列信息Action
dwrCaller.addAutoAction("queryDimInfoByColId",TableViewAction.queryDimInfoByColId);

//加载归并类型信息Action定义
dwrCaller.addAutoAction("queryTypeInfos",DimTypeAction.queryTypeInfos,tableId,{
    dwrConfig:true ,
    converter:new  dhtmxGridDataConverter({
        filterColumns:["dimTypeName","dimTypeCode","dimTypeDesc","dimLevel"]
    })
});
var viewInit=function(){
    dwrCaller.executeAction("queryTableByIdAndVersion",function(data){
        tableBasicInfo=data;
        $("_tableDataSource").innerHTML = "<span style='font-size: 12px;'>"+tableBasicInfo["DATA_SOURCE_NAME"]+"</span>";
        //$("_tableNamecn").innerHTML = "<span style='font-size: 12px;'>"+tableBasicInfo["TABLE_NAME_CN"]+"</span>";
        $("_tableName").innerHTML = "<span style='font-size: 12px;'>"+tableBasicInfo["TABLE_NAME"]+"</span>";
//        $("_codeName").innerHTML = "<span style='font-size: 11px;'>"+tableBasicInfo["CODE_NAME"]+"</span>";
        $("_tableGroupName").innerHTML = "<span style='font-size: 12px;'>"+tableBasicInfo["TABLE_GROUP_NAME"]+"</span>";
        $("_tableBusComment").innerHTML = "<span style='font-size: 12px;'>"
                                                  +(tableBasicInfo["TABLE_BUS_COMMENT"]?tableBasicInfo["TABLE_BUS_COMMENT"]:"&nbsp;")+"</span>";
        $("_tableState").innerHTML= "<span style='font-size: 12px;'>"
                                                  +(tableBasicInfo["TABLE_STATE"]?"有效":"无效")+"</span>"
        $("_dimTablePrefix").innerHTML = "<span style='font-size: 12px;'>"+tableBasicInfo["TABLE_DIM_PREFIX"]+"</span>";
        $("_dimMaxLevel").innerHTML = "<span style='font-size: 12px;'>"+tableBasicInfo["TABLE_DIM_LEVEL"]+"</span>";
        $("_lastLevelFlag").innerHTML = "<span style='font-size: 12px;'>"+(tableBasicInfo["LAST_LEVEL_FLAG"]?"是":"否")+"</span>";
        //查询出审核相关信息
        if(data.auditData.applyUser)
        $("_applyUser").innerHTML = data.auditData.applyUser;
        if(data.auditData.applyDate)
        $("_applyTime").innerHTML = data.auditData.applyDate;
        if(data.auditData.auditUser)
        $("_auditUser").innerHTML = data.auditData.auditUser;
        if(data.auditData.auditMark)
        $("_auditContext").innerHTML = data.auditData.auditMark;

    });
    var basicInfoLayout=new dhtmlXLayoutObject(document.body,"3E");
//    basicInfoLayout.hideConcentrate();//隐藏缩放按钮
//    basicInfoLayout.hideSpliter();
    basicInfoLayout.cells("a").attachObject($("_baseInfoForm"));
    basicInfoLayout.cells("a").hideHeader();
    basicInfoLayout.cells("b").hideHeader();
    basicInfoLayout.cells("a").setHeight("240");
    basicInfoLayout.cells("c").setHeight("120");
    basicInfoLayout.cells("c").hideHeader();
    basicInfoLayout.hideSpliter();
//    basicInfoLayout.cells("c").attachObject($("_buttonForm"));
    //列信息表单
    var colsGrid= basicInfoLayout.cells("b").attachGrid();
    colsGrid.setHeader("名称,类型,允许空,默认值,主键,注释,维表关联");
    colsGrid.setHeaderAlign("left,center,center,center,center,center,center");
    colsGrid.setInitWidthsP("15,15,10,15,15,15,15");
    colsGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro");
    colsGrid.enableCtrlC();
    colsGrid.enableResizing("true,true,true,true,true,true,true");
    colsGrid.setColAlign("left,center,center,center,center,left,center");
    colsGrid.setColumnCustFormat(2, yesOrNo);//第2列进行转义
    colsGrid.setColumnCustFormat(4, yesOrNo);
    colsGrid.setColumnCustFormat(6, transToLink);
    colsGrid.init();
    colsGrid.load(dwrCaller.queryAllColumnsByTableID, "json");
    //归并类型Grid
    var typeGrid= basicInfoLayout.cells("c").attachGrid();
    typeGrid.setHeader("拥有归并类型名称,编码,描述,层级");
    typeGrid.setHeaderAlign("center,center,center,center");
    typeGrid.setInitWidthsP("20,10,20,50");
    typeGrid.setColTypes("ro,ro,ro,ro");
    typeGrid.enableCtrlC();
    typeGrid.enableResizing("true,true,true,true");
    typeGrid.setColAlign("left,center,left,left");
    typeGrid.init();
    typeGrid.load(dwrCaller.queryTypeInfos, "json");
}
//转换维表关联为链接
var transToLink=function(data){
    if(!data){
        return "";
    }
    var url="<a href='#' onclick='linkDim("+data+")'>维度列</a>";
    return url;
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
        $("_dimTableName").innerHTML = data["TABLE_NAME"];
       // $("_dimTableNameCn").innerHTML = data["TABLE_NAME_CN"];
        $("_dimTableBusComment").innerHTML = data["TABLE_BUS_COMMENT"];
        $("_dimColName").innerHTML = data["COL_NAME"];
        $("_dimType").innerHTML = data["DIM_TYPE_NAME"];
        $("_dimLevel").innerHTML = data["DIM_LEVEL_NAME"];
    });

}

dhx.ready(viewInit);