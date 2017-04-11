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
 *       2012-04-17
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
    filterColumns:["reportName","commentCount","commentContext","userNamecn", "commentTime","_buttons"],
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        if(columnName=="reportName"){
            var tmp = "'"+cellValue+"'";
            return '<a href="javascript:openReport('+rowData.reportId+','+tmp+')">'+cellValue+'</a>'+(rowData.reportState==0?"（已下线）":"");
        }
        if(columnName == '_buttons') {
            return "getButtonsCol";
        }
        if(columnName == 'commentCount'){
            return cellValue + '次';
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};

var convert = new dhtmxGridDataConverter(convertConfig);

dwrCaller.addAutoAction("queryMyBeCommented", "MyRecentAction.queryMyBeCommented");
dwrCaller.addDataConverter("queryMyBeCommented", convert);

//初始化方法
var mybeCommentedInit = function(){
    var myOpenLayout = new dhtmlXLayoutObject(document.getElementById("container"), "1C");
    myOpenLayout.cells("a").hideHeader();
    window["getButtonsCol"]=function(){
        return [
            {name:"modify",text:"查看详细",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
                imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                    seeDetail(rowData);
                }
            }
        ];
    };
    mygrid = myOpenLayout.cells("a").attachGrid();
    mygrid.setHeader("报表名称,被评论,最新评论内容,最新评论人,最新评论时间,操作");
    mygrid.setInitWidthsP("18,6,38,10,18,10");
    mygrid.setColAlign("left,center,left,center,center,center");
    mygrid.setHeaderAlign("left,center,center,center,center,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,sb");
    mygrid.init();
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.queryMyBeCommented, "json");
};


/**
 * 打开报表链接
 * @param reportId
 */
var openReport = function(reportId, reportName){
    window.open(urlEncode(getBasePath()+"/home/module/commentRpt/commentRpt.jsp?rptId="+reportId));
};

var commentConvertConfig = {
    idColumnName:"commentId",
    filterColumns:["reportName","userNamecn","commentContext","commentTime"],
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        if(columnName == 'reCount'){
            if(cellValue == 0){
                return '无'
            }else{
                return cellValue + '个';
            }
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};
var commentDataConverter=new dhtmxGridDataConverter(commentConvertConfig);


/**
 * 查看详细
 * @param rowData
 */
var seeDetail = function(rowData){
    var dhxWindow=new dhtmlXWindows();
    dhxWindow.createWindow("commonDetailWindow",0,0,600,400);
    var commonDetailWindow=dhxWindow.window("commonDetailWindow");
    ///commonDetailWindow.stick();
    commonDetailWindow.setModal(true);
    //commonDetailWindow.stick();
    commonDetailWindow.setDimension(600,400);
    commonDetailWindow.center();
    commonDetailWindow.denyResize();
    commonDetailWindow.denyPark();
    commonDetailWindow.setText(rowData.userdata.reportName + ' 的评论');
    commonDetailWindow.keepInViewport(true);
    //关闭一些不用的按钮。
    commonDetailWindow.button("minmax1").hide();
    commonDetailWindow.button("park").hide();
    commonDetailWindow.button("stick").hide();
    commonDetailWindow.button("sticked").hide();
    commonDetailWindow.show();
    var seeLayout = new dhtmlXLayoutObject(commonDetailWindow,"1C");
    seeLayout.cells("a").hideHeader(true);
    seeLayout.hideSpliter();//移除分界边框。
    seeLayout.hideConcentrate();
    dwrCaller.addAutoAction("queryCommentByReportId",MyRecentAction.queryCommentByReportId,rowData.userdata.reportId);
    dwrCaller.addDataConverter("queryCommentByReportId", commentDataConverter);
    var seeGrid=seeLayout.cells("a").attachGrid();
    seeGrid.setHeader("报表名称,评论人,评论内容,评论时间");
    seeGrid.setInitWidthsP("20,10,45,25");
    seeGrid.setColAlign("left,left,left,center");
    seeGrid.setHeaderAlign("left,center,center,center");
    seeGrid.setColTypes("ro,ro,ro,ro");
    seeGrid.init();
    seeGrid.defaultPaging(20);
    seeGrid.load(dwrCaller.queryCommentByReportId, "json");
};

dhx.ready(mybeCommentedInit);