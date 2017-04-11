/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        test.js
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
//查询归并类型Action定义
dwrCaller.addAutoAction("queryDimType", TableApplyAction.queryDimType,tableId,{
    dwrConfig:true,
    converter:new  dhtmxGridDataConverter({
        filterColumns:["dimTypeName","dimTypeCode","dimTypeDesc"],
        idColumnName:"dimTypeId"
    })
});
dwrCaller.addAutoAction("queryDimLevels", TableApplyAction.queryDimLevels,tableId,{
    dwrConfig:true,
    converter:new  dhtmxGridDataConverter({
        filterColumns:["dimLevel","dimLevelName"],
        onBeforeConverted:function(data){
          if(!data||data.length==0){
              return [{DIM_LEVEL:{value:"查询无数据",colspan:2}}];
          }
        }
    }),
    isShowProcess:false
});
var vieDimTypeInit=function(){
    //布局初始化
    var viewDimTypeLayout = new dhtmlXLayoutObject(document.body, "2E");
    viewDimTypeLayout.cells("a").setText("<span style='font-weight: normal;'>维度编码信息：</span>" + tableNameCn);
    viewDimTypeLayout.cells("a").fixSize(false, true);
    viewDimTypeLayout.cells("b").setText("层级信息");

    var typeGrid= viewDimTypeLayout.cells("a").attachGrid();
    typeGrid.setHeader("拥有归并类型名称,编码,描述");
    typeGrid.setHeaderAlign("center,center,center");
    typeGrid.setInitWidthsP("30,10,60");
    typeGrid.setColTypes("ro,ro,ro");
    typeGrid.enableCtrlC();
    typeGrid.enableResizing("true,true,true");
    typeGrid.setColAlign("left,center,left");
    typeGrid.init();
    typeGrid.load(dwrCaller.queryDimType, "json");

    var dimlevelGrid=viewDimTypeLayout.cells("b").attachGrid();
    dimlevelGrid.setHeader("层级,层级名称");
    dimlevelGrid.setHeaderAlign("center,center");
    dimlevelGrid.setInitWidthsP("30,70");
    dimlevelGrid.setColTypes("ro,ro");
    dimlevelGrid.enableCtrlC();
    dimlevelGrid.enableResizing("true,true");
    dimlevelGrid.setColAlign("center,left");
    dimlevelGrid.enableColSpan(true);
    dimlevelGrid.init();
    typeGrid.attachEvent("onRowSelect",function(id){
        dimlevelGrid.clearAll();
        dimlevelGrid.load(dwrCaller.queryDimLevels+"&dimTypeId="+id,"json");
    });
    typeGrid.attachEvent("onXLE", function(){
        if(typeGrid.getRowsNum() && !typeGrid.getSelectedRowId()){
            typeGrid.selectRow(0,true);//默认选择一行
        }});

}
dhx.ready(vieDimTypeInit);