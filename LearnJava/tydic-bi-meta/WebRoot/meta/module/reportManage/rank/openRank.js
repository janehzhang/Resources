/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *
 *Description：
 *
 *Dependent：
 *
 *Author:
 *        刘斌
 *Finished：
 *       2012-04-11
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

/**
 * 数据转换Convert
 */
var convertConfig = {
    idColumnName:"reportId",
    filterColumns:["reportName","reportNote","userNamecn","createTime","count"],
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        if(columnName=="reportName"){
            var tmp = "'"+cellValue+"'";
            return '<a href="javascript:openReport('+rowData.reportId+','+tmp+')">'+cellValue+'</a>';
        }
        if(columnName=="count"){
            if(cellValue!=0){
                return '<a href="javascript:openDetail('+rowData.reportId+')">'+cellValue+'次</a>';
            }else{
                return '0次'
            }
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};
var convert = new dhtmxGridDataConverter(convertConfig);

var detailConvertConfig = {
    filterColumns:["userNamecn","zoneName","deptName","stationName","useTime"]
};
var detailConvert = new dhtmxGridDataConverter(detailConvertConfig);

/**
 * 初始化方法
 */
var openRankInit = function(){
    var openRankLayout = new dhtmlXLayoutObject(document.getElementById("container"), "2E");
    openRankLayout.cells("a").setText("报表打开排名");
    openRankLayout.cells("b").hideHeader();
    openRankLayout.cells("a").setHeight(75);
    openRankLayout.cells("a").fixSize(false, true);
    openRankLayout.hideConcentrate();
    openRankLayout.hideSpliter();//移除分界边框。

    //加载查询表单
    var queryform = openRankLayout.cells("a").attachForm([
        {type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
        {type:"input",label:"关键字：",name:"keyWord"},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"},
        {type:"hidden",name:"template"}
    ]);

    var loadParam = new biDwrMethodParam();
    loadParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
            var formData=queryform.getFormData();
            formData.keyWord=Tools.trim(queryform.getInput("keyWord").value);
            return formData;
        }
        }
    ]);
    dwrCaller.addAutoAction("loadOpenRank", "OpenRankAction.queryOpenRank", loadParam);
    dwrCaller.addDataConverter("loadOpenRank", convert);

    mygrid = openRankLayout.cells("b").attachGrid();
    mygrid.setHeader("报表名称,报表说明,创建人员,创建时间,打开次数");
    mygrid.setInitWidthsP("20,40,15,15,10");
    mygrid.setColAlign("left,left,left,center,center");
    mygrid.setHeaderAlign("left,center,center,center,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro");
    mygrid.init();
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.loadOpenRank, "json");

    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {
            //进行数据查询。
            mygrid.clearAll();
            mygrid.load(dwrCaller.loadOpenRank, "json");
        }
    });
    // 添加Enter查询事件
    queryform.getInput("keyWord").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            mygrid.clearAll();
            mygrid.load(dwrCaller.loadOpenRank, "json");
        }
    }
};

/**
 * 打开报表链接
 * @param reportId
 */
var openReport = function(reportId, reportName){
    openMenu(reportName,"/home/module/commentRpt/commentRpt.jsp?rptId="+reportId,"top");
};

/**
 * 打开访问详情
 */
var openDetail = function(reportId){
    var rowData = mygrid.getRowData(reportId);//获取行数据
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("detailWindow", 0, 0, 650, 300);
    var detailWindow = dhxWindow.window("detailWindow");
    detailWindow.setModal(true);
    detailWindow.stick();
    detailWindow.setDimension(650, 300);
    detailWindow.center();
    detailWindow.denyResize();
    detailWindow.denyPark();
    detailWindow.setText(rowData.userdata.reportName+ '<span style="font-weight:normal;">&nbsp;打开日志</span>');
    detailWindow.keepInViewport(true);

    //关闭一些不用的按钮。
    detailWindow.button("minmax1").hide();
    detailWindow.button("park").hide();
    detailWindow.button("stick").hide();
    detailWindow.button("sticked").hide();
    detailWindow.show();
    var detailLayout = new dhtmlXLayoutObject(detailWindow, "1C");
    detailLayout.cells("a").hideHeader();

    dwrCaller.addAutoAction("queryForOpenDetail", "OpenRankAction.queryForOpenDetail", reportId);
    dwrCaller.addDataConverter("queryForOpenDetail", detailConvert);
    var detailGrid = detailLayout.cells("a").attachGrid();
    detailGrid.setHeader("用户,地域,部门,岗位,打开时间");
    detailGrid.setInitWidthsP("20,20,20,20,20");
    detailGrid.setColAlign("left,left,left,left,center");
    detailGrid.setHeaderAlign("left,center,center,center,center");
    detailGrid.setColTypes("ro,ro,ro,ro,ro");
    detailGrid.init();
    detailGrid.defaultPaging(20);
    detailGrid.load(dwrCaller.queryForOpenDetail, "json");

};

dhx.ready(openRankInit);