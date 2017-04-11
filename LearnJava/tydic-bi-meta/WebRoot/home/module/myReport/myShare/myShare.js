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
    idColumnName:"shareListId",
    filterColumns:["reportName","createUser","zoneName","deptName","createTime","shareTime","_buttons"],
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        if(columnName=="reportName"){
            var tmp = "'"+cellValue+"'";
            return '<a href="javascript:openReport('+rowData.reportId+','+tmp+')">'+cellValue+'</a>'+(rowData.reportState==0?"（已下线）":"");
        }
        if(columnName == '_buttons') {
            return "getButtonsCol";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};

var convert = new dhtmxGridDataConverter(convertConfig);

/**
 * 初始化方法
 */
var myShareInit = function(){
    var myShareLayout = new dhtmlXLayoutObject(document.getElementById("container"), "2E");
    myShareLayout.cells("a").setText("共享给我的报表");
    myShareLayout.cells("b").hideHeader();
    myShareLayout.cells("a").setHeight(75);
    myShareLayout.cells("a").fixSize(false, true);
    myShareLayout.hideConcentrate();
    myShareLayout.hideSpliter();//移除分界边框。

    //加载查询表单
    var queryform = myShareLayout.cells("a").attachForm([
        {type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
        {type:"input",label:"报表名称：",name:"keyWord"},
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
    dwrCaller.addAutoAction("queryMyShareReport", "MyShareAction.queryMyShareReport", loadParam);
    dwrCaller.addDataConverter("queryMyShareReport", convert);

    window["getButtonsCol"]=function(){
        return [
            {name:"share",text:"删除",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
                imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData) {
                deleteMyShare(rowData);
            }
            }
        ];

    };

    mygrid = myShareLayout.cells("b").attachGrid();
    mygrid.setHeader("报表名称,创建人,创建地域,创建部门,创建时间,共享时间,操作");
    mygrid.setInitWidthsP("15,15,15,15,15,15,10");
    mygrid.setColAlign("left,center,center,center,center,center,center");
    mygrid.setHeaderAlign("left,center,center,center,center,center,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,ro,sb");
//    mygrid.setSkin("xp");
    mygrid.init();
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.queryMyShareReport, "json");

    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {
            //进行数据查询。
            mygrid.clearAll();
            mygrid.load(dwrCaller.queryMyShareReport, "json");
        }
    });
    // 添加Enter查询事件
    queryform.getInput("keyWord").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            mygrid.clearAll();
            mygrid.load(dwrCaller.queryMyShareReport, "json");
        }
    }

};

/**
 * 打开报表链接
 * @param reportId
 */
var openReport = function(reportId, reportName){
    window.open(urlEncode(getBasePath()+"/home/module/commentRpt/commentRpt.jsp?rptId="+reportId));
};

/**
 * 删除别人共享给我的报表
 * @param rowData
 */
var deleteMyShare = function(rowData){
    dhx.confirm("确定要删除该共享给您的报表吗？", function(r){
        if(r){
            MyShareAction.deleteOneShare(rowData.id, function(r){
                if(r){
                    dhx.alert("删除成功！");
                    mygrid.updateGrid([{id:rowData.id}], "delete");
                }else{
                    dhx.alert("删除失败，请重试！");
                }
            });
        }
    });
};

dhx.ready(myShareInit);