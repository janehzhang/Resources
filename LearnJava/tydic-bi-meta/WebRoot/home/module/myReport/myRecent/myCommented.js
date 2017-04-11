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
    idColumnName:"commentId",
    filterColumns:["reportName","userNamecn","commentContext","commentTime", "reCount","newReCount"],
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        if(columnName=="reportName"){
            var tmp = "'"+cellValue+"'";
            return '<a href="javascript:openReport('+rowData.reportId+','+tmp+')">'+cellValue+'</a>'+(rowData.reportState==0?"（已下线）":"");
        }
        if(columnName == 'reCount'){
            if(cellValue!=0){
                return cellValue + '个';
            }else{
                return '无';
            }
        }
        if(columnName == 'newReCount'){
            if(cellValue!=0){
                var tmp = "'"+rowData.reportId+"'";
                return cellValue + '个（' + '<a href="javascript:openNewRe('+tmp+')">查看</a>' +'）'
            }else{
                return '无';
            }
        }

        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};

var convert = new dhtmxGridDataConverter(convertConfig);

dwrCaller.addAutoAction("queryMyCommented", "MyRecentAction.queryMyCommented");
dwrCaller.addDataConverter("queryMyCommented", convert);

//初始化方法
var myCommentedInit = function(){
    var myOpenLayout = new dhtmlXLayoutObject(document.getElementById("container"), "1C");
    myOpenLayout.cells("a").hideHeader();
    mygrid = myOpenLayout.cells("a").attachGrid();
    mygrid.setHeader("报表名称,创建人,我评论的内容,我评论的时间,回复总数,未读回复");
    mygrid.setInitWidthsP("20,10,35,15,10,10");
    mygrid.setColAlign("left,left,left,center,center,center");
    mygrid.setHeaderAlign("left,center,center,center,center,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,ro");
    mygrid.init();
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.queryMyCommented, "json");
};


/**
 * 打开报表链接
 * @param reportId
 */
var openReport = function(reportId, reportName){
    window.open(urlEncode(getBasePath()+"/home/module/commentRpt/commentRpt.jsp?rptId="+reportId));
};

var openNewRe = function(data){
    window.open(urlEncode(getBasePath()+"/home/module/commentRpt/commentRpt.jsp?rptId="+data));
};

dhx.ready(myCommentedInit);