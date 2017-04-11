/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       diff.js
 *Description：
 *       表类管理-系统初始化导入表
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       张伟
 *Date：
 *       2011-11-10
 ********************************************************/


/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();
//表类数据源相关Action
dwrCaller.addAutoAction("queryTableDataSource", "TableApplyAction.queryTableDataSource");
//dwrCaller.addDataConverter("queryTableDataSource", new dhtmlxComboDataConverter({
//    valueColumn:"dataSourceId",
//    textColumn:"dataSourceName"
//})
//);


var diffListConfig = {
    filterColumns:["createDate","dataSourceName","diffCount","_buttons"],
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData){
        return rowData;
    },
    /**
     * 获取下拉框Button的值
     * @param rowIndex
     * @param columnIndex
     * @param cellValue
     * @param rowData
     */
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        if(columnName == '_buttons'){//如果是第3列。即操作按钮列
            return "getRoleButtonsCol";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    },
    onBeforeConverted:function(data){
        return data;
    }
};

//查询差异action定义。
dwrCaller.addAutoAction("queryDiffList",DiffAction.queryDiffList,{
    dwrConfig:true,
    converter: new dhtmxGridDataConverter(diffListConfig)
});
/**
 * 查询具体差异Action定义。
 */
dwrCaller.addAutoAction("queryDetialDiffList",DiffAction.queryDetialDiffList,{
    dwrConfig:true,
    converter: new dhtmxGridDataConverter({
        filterColumns:["tableName","codeName","groupName","dataSourceName","tableOwner","tableBusComment","_buttons"],
        userData:function(rowIndex, rowData){
            return rowData;
        },
        cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
            if(columnName == '_buttons'){//如果是第3列。即操作按钮列
                return "getDetialButtonsCol";
            }
            return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
        }
    })
});
var diffWarningInit=function(){
    var dhxLayout = new dhtmlXLayoutObject(document.body, "2E");
    dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.setText("差异预警");
    topCell.setHeight(80);
    topCell.fixSize(true,true);

    var bottomCell = dhxLayout.cells("b");
    bottomCell.fixSize(true,true);
    bottomCell.hideHeader();

    var today = new Date();
    var tomorrow = new Date();
    tomorrow.setDate(today.getDate()+1);
    var queryform = topCell.attachForm(
            [
                {type:"settings",position: "label-left"},
                {type:"calendar",label:"报告日期：", inputHeight:17,name:"startDate",dateFormat:"%Y-%m-%d",weekStart:"7",value:today,validate: "NotEmpty",readonly:true},
                {type:"newcolumn"},
                {type:"calendar",label:"至",name:"endDate",dateFormat:"%Y-%m-%d",weekStart:"7",value:today,inputHeight:17,validate: "NotEmpty",readonly:true},
                {type:"newcolumn"},
                {type:"select",label:"数据源：",name:"dataSource",inputHeight:22,inputWidth: 120,options:[{value:"",text:"全部",selected:true}]},
                {type:"newcolumn"},
                {type:"button",name:"queryBtn",value:"查询",offsetLeft : 0,offsetTop : 0}
            ]
    );

    //queryform.getCombo("dataSource").readonly(true,false);
//    queryform.getCombo("dataSource").loadXML(dwrCaller.queryTableDataSource, function(data){
//    	//alert(data._data.options);
//        queryform.getCombo("dataSource").setComboValue("");
//    });
    dwrCaller.executeAction("queryTableDataSource",function(data){
      var dataSourceSelect= queryform.getSelect("dataSource");
        var dataSourceCount=1;
        dataSourceSelect.options[0]=new Option("全部","");
        for(var key in data){
            if(key!='length'){
                dataSourceSelect.options[dataSourceCount++]=new Option(data[key].DATA_SOURCE_NAME,data[key].DATA_SOURCE_ID);
            }
        }
        
    }) ;
//    queryform.defaultValidateEvent();
    var startCalendar = queryform.getCalendar("startDate");
    var endCalendar = queryform.getCalendar("endDate");

    //将日历控件语言设置成中文
    startCalendar.loadUserLanguage('zh');
    endCalendar.loadUserLanguage('zh');

    startCalendar.setInsensitiveRange(tomorrow,null);
    endCalendar.setInsensitiveRange(tomorrow,null);

    var diffGrid  = bottomCell.attachGrid();
    diffGrid.setHeader("报告日期,数据源,表类差异数,操作");
    diffGrid.setInitWidthsP("25,30,30,15");
    diffGrid.setColAlign("center,left,center,center");
    diffGrid.setHeaderAlign("center,center,center,center");
    diffGrid.setColTypes("ro,ro,ro,sb");
    diffGrid.setColSorting("na,na,na,na");
    diffGrid.enableResizing("true,true,true,true");
    diffGrid.enableCtrlC();
    diffGrid.init();
    diffGrid.defaultPaging(10);
    //dwr参数封装
    var queryParam = new biDwrMethodParam();
    queryParam.setParamConfig([{
        index:0,type:"fun",value:function(){ //索引0位的参数来源由表单提供。
            return  queryform.getItemValue("startDate");
        }},{
        index:1,type:"fun",value:function(){
            return queryform.getItemValue("endDate");
        }
    },{
        index:2,type:"fun",value:function(){
            return queryform.getItemValue("dataSource");
        }
    }]);

    var query=function(){
        diffGrid.clearAll();
        diffGrid.load(dwrCaller.queryDiffList+queryParam,"json");
    }
    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "queryBtn") {
            //进行数据查询。
            if(queryform.validate()){
                query();
            }
        }

    });
    query();
    var buttons = {
        showDiff:{name:"showDiff",text:"查看具体差异表类",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
            imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData){
                showDiff(rowData);
            }
        }}
    window.getRoleButtonsCol= function(){
        return [buttons.showDiff];
    };
}
/**
 * 查询具体差异信息。
 */
var showDiff=function(rowData){
    var dhxWindow=new dhtmlXWindows();
    dhxWindow.createWindow("showDiffWindow",0,0,400,220);
    var showDiffWindow=dhxWindow.window("showDiffWindow");
    showDiffWindow.setModal(true);
    showDiffWindow.stick();
    showDiffWindow.setDimension(650,350);
    showDiffWindow.center();
    showDiffWindow.denyResize();
    showDiffWindow.denyPark();
    //关闭一些不用的按钮。
    showDiffWindow.button("minmax1").hide();
    showDiffWindow.button("park").hide();
    showDiffWindow.button("stick").hide();
    showDiffWindow.button("sticked").hide();
    showDiffWindow.setText("差异表类信息");
    showDiffWindow.keepInViewport(true);
    showDiffWindow.show();
    //layout
    var diffLayout =showDiffWindow.attachLayout("1C");
//    diffLayout.cells("a").setHeight("400");
//    diffLayout.cells("a").fixSize(true,true);
//    diffLayout.cells("b").hideHeader();
    diffLayout.hideSpliter();
    diffLayout.hideConcentrate();
    diffLayout.cells("a").setText("<span style='font-weight: normal;'>报告日期：</span>"+rowData.data[0]);
    //加入差异分析Grid
    var detialDiffGrid=diffLayout.cells("a").attachGrid();
    detialDiffGrid.setHeader("表类名称,业务类型,层次分类,数据源,所属用户,表类说明,操作");
    detialDiffGrid.setInitWidthsP("20,10,10,10,10,30,10");
    detialDiffGrid.setColAlign("left,left,left,left,left,left,center");
    detialDiffGrid.setHeaderAlign("center,center,center,center,center,center,center");
    detialDiffGrid.setColTypes("ro,ro,ro,ro,ro,ro,sb");
    detialDiffGrid.setColSorting("na,na,na,na,na,na,na");
    detialDiffGrid.enableResizing("true,true,true,true,true,true,true");
    detialDiffGrid.enableCtrlC();
    detialDiffGrid.init();
    detialDiffGrid.defaultPaging(10);
    var param=new biDwrMethodParam();
    param.setParam(0,rowData.userdata.dataSourceId,true);
    param.setParam(1,rowData.data[0],true);
    detialDiffGrid.load(dwrCaller.queryDetialDiffList+param,"json");
    var buttons = {
        diffAnalysis:{name:"diffAnalysis",text:"查看差异",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
            imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData){
                var url="meta/module/tbl/diff/diffAnalysis.jsp?tableId="
                                          +rowData.userdata.tableId+"&tableVersion="+rowData.userdata.tableVersion+"&tableName="+rowData.userdata.tableName;
                openMenu("表类差异",url,"top",null,true);
            }
        }}
    window.getDetialButtonsCol= function(){
        return [buttons.diffAnalysis];
    };
//关闭Window事件
    showDiffWindow.attachEvent("onClose", function(win){
        dhxWindow.unload();
    })

}

dhx.ready(diffWarningInit);