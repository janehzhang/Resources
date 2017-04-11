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
//加载维表基本信息。
var dimInfo=null;//维表基本信息
var dymaicColData=null;//维表动态字段。
var columnDatas=null;//维表所有字段信息。
var dymaicColNames = [];//动态字段名称
//归并类型定义，以归并类型ID作为键值，value作为归并类型信息，如:{1:{dimTypeId:1,dimTypeName:"行政区域"...}}
var dimTypeInfo = {};
var initColData = ["ID","PAR_ID","CODE","NAME","DESC","DIM_TYPE_ID","STATE","DIM_LEVEL","MOD_FLAG","ORDER_ID","DIM_TABLE_ID"];
/**
 * 初始化Action定义
 */
dwrCaller.addAction("initData",function(){
    //批量调用
    DWREngine.beginBatch();
    //查询维表信息
    TableDimAction.queryDimTablesInfoById(tableId,{
        callback:function(data){
            dimInfo= new columnNameConverter().convert(data);
            //查询列信息。
            TableApplyAction.queryColumnsByTableID(tableId,tableVersion,{
                callback:function(data){
                    columnDatas=new columnNameConverter().convert(data);
                    dymaicColData = dynamicCols(dimInfo.tableDimPrefix, columnDatas);
                    for(var i = 0; i < dymaicColData.length; i++){
                        dymaicColNames.push(dymaicColData[i].colName);
                    }
                },
                async:false
            })
        },
        async:false
    });
    TableApplyAction.queryDimType(tableId,{
        callback:function(data){
            data=new columnNameConverter().convert(data);
            for(var i = 0; i < data.length; i++){
                dimTypeInfo[data[i].dimTypeId] = data[i];
            }
            dimTypeInfo.length = data.length;
        },
        async:false
    });
    DWREngine.endBatch();
});

/**
 * 返回动态字段列
 */
var dynamicCols = function(prefix, columnDatas){
    var dynamicColData = [];
    for(var j = 0; j < columnDatas.length; j++){
        var columnName = columnDatas[j].colName;
        var isInitCol = false;//是否是系统固定列
        for(var i = 0; i < initColData.length; i++){
            if(i < 5){
                if((prefix + "_" + initColData[i]).toUpperCase() == columnName.toUpperCase()){
                    isInitCol = true;
                    break;
                }
            } else{
                if(initColData[i].toUpperCase() == columnName.toUpperCase()){
                    isInitCol = true;
                    break;
                }
            }
        }
        if(!isInitCol){
            dynamicColData.push(columnDatas[j]);
        }
    }
    return dynamicColData;
}

/**
 * 页面初始化方法
 */
var viewDetailInit=function(){
    dwrCaller.executeAction("initData");
    var viewDetailLayout = new dhtmlXLayoutObject(document.body, "1C");
    viewDetailLayout.cells("a").hideHeader();
    var tabbar=viewDetailLayout.cells("a").attachTabbar();
    tabbar.setHrefMode("iframes-on-demand");
    tabbar.addTab("dimType", "归并类型", "100px");
    tabbar.addTab("maintainCodeData", "编码归并", "100px");
    tabbar.addTab("codeMapping", "编码映射", "100px");
    tabbar.addTab("intRelValue", "接口表查询", "100px");
    //归并类型。
    tabbar.setContentHref("dimType",urlEncode(getBasePath()+"/meta/module/dim/dimtype/viewDimType.jsp"+"?tableId="+tableId+"&tableVersion="+tableVersion
                                                      +"&tableNameCn="+encodeURIComponent(encodeURIComponent(tableNameCn))+"&tableName="+tableName+"&tableOwner="+tableOwner));
//    //编码归并
    tabbar.setContentHref("maintainCodeData",urlEncode(getBasePath()+"/meta/module/dim/merge/mergeView.jsp"+"?tableId="+tableId+"&tableVersion="+tableVersion
                                                               +"&tableNameCn="+encodeURIComponent(encodeURIComponent(tableNameCn))+"&tableName="+tableName+"&tableOwner="+tableOwner));
//    //编码映射。
    tabbar.setContentHref("codeMapping",urlEncode(getBasePath()+"/meta/module/dim/mapping/mappingView.jsp"+"?tableId="+tableId+"&tableVersion="+tableVersion
                                                          +"&tableNameCn="+encodeURIComponent(encodeURIComponent(tableNameCn))+"&tableName="+tableName+"&tableOwner="+tableOwner));
//
    tabbar.setContentHref("intRelValue",urlEncode(getBasePath()+"/meta/module/dim/intRel/intRelValueQuery.jsp?tableId="+tableId +"&tableName="+tableName+"&tableOwner="+tableOwner
                                                          +"&tableNameCn="+ encodeURIComponent(encodeURIComponent(tableNameCn))));

    tabbar.setTabActive(focus);
}


dhx.ready(viewDetailInit);