/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        mergeView.js
 *Description：
 *       归并编码查看JS文件
 *Dependent：
 *       dhtmlx.js、dwr相关JS文件、dhtmlxExtend.js、tool。js等。
 *Author:
 *        张伟
 *Finished：
 *       2011-11-22
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
var dwrCaller = new biDwrCaller();
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
//归并类型定义，以归并类型ID作为键值，value作为归并类型信息，如:{1:{dimTypeId:1,dimTypeName:"行政区域"...}}
var dimTypeInfo = {};
//归并扩展表信息，包含键值与表META_DIM_TABLES定义相同
var dimInfo = null;
var dymaicColData = [];//动态字段信息
var dymaicColNames = [];//动态字段名称
//查询对应编码所有归并类型Action定义
dwrCaller.addAutoAction("queryDimTypesByCode", DimMergeAction.queryDimTypesByCode, tableId, tableName, {
    dwrConfig:true,
    isShowProcess:false,
    converter:new columnNameConverter()
});
/**
 * 加载子数据
 */
dwrCaller.addAutoAction("querySub",DimMergeAction.querySub,{
    dwrConfig:true,
    isDealOneParam:false,
    onBeforeLoad:function(params){
        params[3]=params[3].id;
        params[4]=dimInfo.tableOwner;
    }
});

var treeGridConverterConfig = {
    isDycload:false,
    isFormatColumn:false,
    userData:function(rowIndex, rowData){
        return rowData;
    }
}
var treeGridConverter = new dhtmlxTreeGridDataConverter(treeGridConverterConfig);
//数转换器
var treeConverterConfig = {
    isDycload:false,
    isFormatColumn:false,
    userData:function(rowIndex, rowData){
        return {rowData:rowData};
    }
}
var treeConverter = new dhtmxTreeDataConverter(treeConverterConfig);
/*
 * 加载维度数据Action定义
 */
dwrCaller.addAutoAction("queryDimData", DimMergeAction.queryDimData, {
    dwrConfig:true,
    converter: treeGridConverter,
    isShowProcess:true,
    isDealOneParam:false
});

dwrCaller.addAutoAction("queryCodeDimTypeInfo",DimMergeAction.queryCodeDimTypeInfo,{
    dwrConfig:true,
    converter: new dhtmxGridDataConverter({
        filterColumns:["dimTypeCode","dimTypeName","dimTypeDesc","itemParName"]
    }),
    isShowProcess:false,
    isDealOneParam:false
})

/**
 * 初始化函数。
 */
var mergeViewInit=function(){
    dimInfo=window.parent.dimInfo;
    dimTypeInfo=window.parent.dimTypeInfo;
    dymaicColData=window.parent.dymaicColData;
    var mergeLayout = new dhtmlXLayoutObject(document.body, "2E");
    mergeLayout.cells("a").setText("<span style='font-weight: normal;'>查看编码数据：</span>" + tableNameCn);
    mergeLayout.cells("b").hideHeader();
    mergeLayout.cells("a").setHeight(70);
    mergeLayout.cells("a").fixSize(false, true);
    mergeLayout.hideConcentrate();
    mergeLayout.hideSpliter();//移除分界边框。
    //加载查询表单
    queryform = mergeLayout.cells("a").attachForm([
        {type:"settings",position: "label-left", labelWidth: 70, inputWidth: 120},
        {type:"select",label:"归并类型：",name:"dimType"},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"}
    ]);
    var dimTypeSelect= queryform.getSelect("dimType");
    var dimCount=0;
    for(var key in dimTypeInfo){
        if(key!='length'){
            dimTypeSelect.options[dimCount++]=new Option(dimTypeInfo[key].dimTypeName,dimTypeInfo[key].dimTypeId);
        }
    }
    mergeLayout.cells("b").attachObject($("_layout"));
    $("_middleArea").style.height = ($("_layout").offsetHeight - 45) + "px";
    $("_middleArea").parentNode.style.height = ($("_layout").offsetHeight - 45) + "px";
    $("_dimDataGrid").style.height = $("_middleArea").offsetHeight + "px";
    $("_dimTypeGrid").style.height = ($("_dimDataGrid").offsetHeight) + "px";

    $("_toolbar").style.width = $("_toolbar").parentNode.offsetWidth - 2 + "px";
    var toolBar = new dhtmlXToolbarObject("_toolbar");
    toolBar.addText("title",1,"维度编码信息");

    var codeToolbar = new dhtmlXToolbarObject("_codeToolbar");
    codeToolbar.addText("text", 1, "所有归并类型编码");
    $("_codeToolbar").style.width = $("_toolbar").parentNode.offsetWidth - 2 + "px";

    //模板HTML设置样式
//    dhx.html.addCss($("_columnDimTableDiv_{template}"), global.css.gridTopDiv);
    //初始化归并类型编码Grid
//    var template = $("_template").innerHTML.replace(/\{template\}/g, "right");
//    $("_dimTypeGrid").innerHTML = template;
//    $("_clumnDimContentDiv_right").style.height = ($("_dimTypeGrid").offsetHeight) + "px";
//    $("_columnDimTableDiv_right").style.marginTop = "-6px";
    var typeGrid= new dhtmlXGridObject("_dimTypeGrid");
    typeGrid.setHeader("归并编码,归并类型,归并描述,父级");
    typeGrid.setHeaderAlign("center,center,center,center");
    typeGrid.setInitWidthsP("20,30,30,20");
    typeGrid.setColTypes("ro,ro,ro,ro");
    typeGrid.enableCtrlC();
    typeGrid.enableResizing("true,true,true,true");
    typeGrid.setColAlign("center,left,left,left");
    typeGrid.init();
    typeGrid.entBox.style.marginTop = "-4px";

    //参数控件初始化
    var mergeParam = new biDwrMethodParam();
    mergeParam.setParamConfig([
        {
            index:0,type:"fun",value:function(){ //索引0位的参数来源由表单提供。
            var queryData= queryform.getFormData();
            queryData.lastLevelFlag=dimInfo.lastLevelFlag;
            queryData.tableUser = tableOwner;
            return queryData;
        }},
        {
            index:1,type:"static",value:tableName
        }//第二个参数为表名
    ]);

    var query = function(){
        //第三个和第四个参数运行时确定
        treeDataGrid.clearAll();
        typeGrid.clearAll();
        treeDataGrid.load(dwrCaller.queryDimData + mergeParam, "json");
    }
    queryform.attachEvent("onButtonClick", function(id){
        if(id == "query"){
            query();
        }
    }) ;

    var initTreeGrid = function(columnData){
        var imagePath = dhtmlx.image_path + "csh_" + dhtmlx.skin + "/";
        treeDataGrid = new dhtmlXGridObject("_dimDataGrid");
        treeDataGrid.setImagePath(imagePath);
        treeDataGrid.setIconPath(imagePath);
        var header = "编码项,编码名称,归并数量,状态,";
        var alginStr = "left,left,center,center";
        var colTypeStr = "tree,ro,ro,ro";
        var colSortStr = "na,na,na,na";
        //total是指以多少开始计算，当动态字段数目>3个时，出横向滚动条。
        var total = 100 + (dymaicColData.length > 3 ? (dymaicColData.length - 3) * 20 : 0);
        var initP = [3,3,2,2];//比例
        var totalY = 10;
        var colParam = [];
        var filterColumns = [(dimInfo.tableDimPrefix + "_CODE").toUpperCase(),
            (dimInfo.tableDimPrefix + "_NAME").toUpperCase()
            ,"DIMTYPE_CNT","STATE"];
        //初始化百分比
        for(var i = 0; i < dymaicColData.length; i++){
            initP.push(2);
            totalY += 2;
            //动态显示列：如果中文名为空时，显示其字段名称。
            if(dymaicColData[i].colNameCn){
                header += dymaicColData[i].colNameCn+",";
            }
            else{
                header += dymaicColData[i].colName+",";
            }
            alginStr += ",left";
            colTypeStr += ",ro";
            colSortStr += ",na";
            colParam.push(dymaicColData[i].colName);
            filterColumns.push(dymaicColData[i].colName.toUpperCase());
        }
        //转换器初始化
        treeGridConverter.setIdColumn((dimInfo.tableDimPrefix + "_ID").toUpperCase());
        treeGridConverter.setPidColumn((dimInfo.tableDimPrefix + "_PAR_ID").toUpperCase());
        treeGridConverter.setFilterColumns(filterColumns);
        treeGridConverter.isOpen = function(){
            return false;//默认所有均不展开
        }
        treeGridConverter.haveChild=function(rowIndex, rowData){
            return rowData["CHILDREN"] &&parseInt(rowData["CHILDREN"])>=1;
        }
        //展开第一级
        treeGridConverter.afterCoverted = function(data){
            if(data){
                for(var i = 0; i < data.length; i++){
                    data[i].open = data[i].rows ? true : false;
                }
            }
            return data;
        }
        //根据ORDER_ID进行排序
        treeGridConverter.compare = function(data1, data2){
            if(data1.userdata.ORDER_ID == undefined || data1.userdata.ORDER_ID == null){
                return false;
            }
            if(data2.userdata.ORDER_ID == undefined || data2.userdata.ORDER_ID == null){
                return false;
            }
            return data1.userdata.ORDER_ID <= data2.userdata.ORDER_ID
        }
        treeConverter.setIdColumn(treeGridConverter.idColumnName);
        treeConverter.setPidColumn(treeGridConverter.pidColumn);
        treeConverter.setTextColumn(filterColumns[1]);
        treeConverter.isOpen = treeGridConverter.isOpen;
        treeConverter.compare= treeGridConverter.compare;
        treeConverter.afterCoverted =  function(data){
            if(data){
                for(var i = 0; i < data.length; i++){
                    data[i].open = data[i].item ? true : false;
                }
            }
            return data;
        };
        dymaicColNames = colParam;
        filterColumns.push("_buttons");//操作列
        //设置加载数据的第三个参数，动态列名
        mergeParam.setParam(2, colParam, false);
        //设置加载数据的第四个参数，表字段前缀
        mergeParam.setParam(3, dimInfo.tableDimPrefix, false);
         mergeParam.setParam(4,tableOwner, false);
        //计算每个百分比
        var strInitP = "";
        for(var i = 0; i < initP.length; i++){
            strInitP += (initP[i] * total / totalY) + (i == initP.length - 1 ? "" : ",");
        }
        treeDataGrid.setHeader(header);
        treeDataGrid.setInitWidthsP(strInitP);
        treeDataGrid.setColAlign(alginStr);
        treeDataGrid.setHeaderAlign(alginStr);
        treeDataGrid.setColTypes(colTypeStr);
        treeDataGrid.setColSorting(colSortStr);
        treeDataGrid.enableTreeGridLines();
        treeDataGrid.setEditable(false);
        treeDataGrid.setColumnCustFormat(3, validOrNot);
        treeDataGrid.init();
        var param=new biDwrMethodParam();
        param.setParam(0,tableName);
        param.setParam(1,dimInfo.tableDimPrefix);
        param.setParam(2,dymaicColNames);
        var tempConverter=dhx.extend({},treeGridConverter,true);
        tempConverter.isDycload=true;
        dwrCaller.addDataConverter("querySub",tempConverter);
        treeDataGrid.kidsXmlFile=dwrCaller.querySub+param;

        //查询数据
        query();
        treeDataGrid.entBox.style.marginTop = "-4px";
        var typeGridParam=new biDwrMethodParam();
        typeGridParam.setParam(0,tableName);
        typeGridParam.setParam(1,dimInfo.tableDimPrefix);
        typeGridParam.setParam(2,tableOwner);
        treeDataGrid.attachEvent("onRowSelect",function(id){
            var code=treeDataGrid.cells(id,0).getValue();
            typeGridParam.setParam(3,code,false);
            typeGrid.clearAll();
            typeGrid.load(dwrCaller.queryCodeDimTypeInfo+typeGridParam,"json");
        });
        treeDataGrid.attachEvent("onXLE", function(){
            if(treeDataGrid.getRowsNum() && !treeDataGrid.getSelectedRowId()){
                treeDataGrid.selectRow(0,true);//默认选择一行
            }});
    }
    initTreeGrid(window.parent.columnDatas)

};
dhx.ready(mergeViewInit);