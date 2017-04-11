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
 *       2011-11-29
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
/**
 * 声明dwrCaller
 */
var typeStateData = getCodeByType("TABLE_STATE");	//码表（表操作状态）
var dwrCaller = new biDwrCaller();
//表变动历史相关Action以及数据转换器 start
var changeHistoryConfig = {
    filterColumns:["","applyusername","applytype","applydate","tableState","tableVersion","auditusername","auditdate","relType"],
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData) {
        var userData = {};
        return userData;
    },
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
        if (columnIndex == 0 && (rowData.rn==2||rowData.rn==1)) {//第一列，checkbox列
            return 1;
        }
        if(columnName=="applytype"){
			if(cellValue==1){
	    		return "申请";
	    	}else{
	   			return "修改";
	    	}
        }
        if(columnName=="relType"){
	   		for(var i=0;i<typeStateData.length;i++){
	    		if(cellValue==typeStateData[i].value){
	    			return typeStateData[i].name;
	    		}
	   		}
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};
var changeHistoryConverter = new dhtmxGridDataConverter(changeHistoryConfig);
dwrCaller.addAutoAction("queryHistoryByTableId", "TableViewAction.queryHistoryByTableId",tableId,{
	dwrConfig:true,
    isShowProcess:false
});
dwrCaller.addDataConverter("queryHistoryByTableId", changeHistoryConverter);
//表变动历史相关Action以及数据转换器 end

var changeHistoryInit=function(){
    changeHistoryLayout = new dhtmlXLayoutObject(document.getElementById("detail"), "2E");
    changeHistoryLayout.cells("a").hideHeader();
    changeHistoryLayout.cells("b").hideHeader();
    changeHistoryLayout.cells("a").setHeight(180);
    changeHistoryLayout.hideConcentrate();
    changeHistoryLayout.hideSpliter();//移除分界边框。
    changeHistoryGrid = changeHistoryLayout.cells("a").attachGrid();
    changeHistoryGrid.setHeader("{#checkBox},申请人,申请操作,操作时间,表状态,版本,审核人,审核时间,审核结果");
    changeHistoryGrid.setHeaderAlign("center,left,center,center,center,center,center,center,center");
    changeHistoryGrid.setInitWidthsP("3,11,11,15,15,12,11,11,11");
    changeHistoryGrid.setColTypes("ch,ro,ro,ro,ro,ro,ro,ro,ro");
    changeHistoryGrid.enableCtrlC();
    changeHistoryGrid.enableResizing("false,true,true,true,true,true,true,true,true");
    changeHistoryGrid.setColumnIds("","applyusername","applytype","applydate","tableState","tableVersion","auditusername","auditdate","relType");
    changeHistoryGrid.setColumnCustFormat(4, tableState);//第3列进行转义
    changeHistoryGrid.setColAlign("center,left,center,left,center,center,left,left,left");
    changeHistoryGrid.enableMultiselect(true);
    changeHistoryGrid.init();
    changeHistoryGrid.load(dwrCaller.queryHistoryByTableId, function(){
        compare();
    }, "json");
    changeHistoryGrid.defaultPaging(5);

    //TODO 右键比较差异
    var base = getBasePath();
    var buttons={
        compare:{name:"compare",text:"比较",imgEnabled:base+"/meta/resource/images/users.png",
            imgDisabled :base+"/meta/resource/images/users.png"}
    };
    //TODO 按钮权限过滤,checkRule.
    var buttonStation=["compare"];
//    var buttonStation=getRoleButtons();

    //定义全局函数，用于获取有权限的button列表定义
    window["getStationButtons"] = function(){
        var res = [];
        for(var i = 0; i < buttonStation.length; i++){
            res.push(buttons[buttonStation[i]]);
        }
        return res;
    };
    var filterButton = window["getStationButtons"]();
    //右键菜单
    var menu = new dhtmlXMenuObject();
    menu.renderAsContextMenu();
    menu.attachEvent("onClick", function(menuItemId){
        compare();
        return true;
    });
    var pos = 0;
    for(var i = 0; i < filterButton.length; i++){
        menu.addNewChild(null, pos++, filterButton[i].name, filterButton[i].text, false, filterButton[i].imgEnabled,
            filterButton[i].imgDisabled);
    }
    changeHistoryGrid.enableContextMenu(menu);
    changeHistoryGrid.enableSelectCheckedBoxCheck(1);
    changeHistoryGrid.attachEvent("onCellChanged", function(rId,cInd,nValue){
        if(cInd == 0){
            compare();
        }
    });
};

var currentDiffCount=0;//当前差异数。
//差异分析Action定义
dwrCaller.addAutoAction("historyCompare",TableViewAction.historyCompare,{
    dwrConfig:true,
    converter:new dhtmxGridDataConverter({
        filterColumns:["configColumnName","configDataType","configIsPrimary","configColNullabled","configDefaultVal","configColNamecn",
            "instColumnName","instDataType","instIsPrimary","instColNullabled","instDefaultVal","instColNamecn","diff"],
        onBeforeConverted:function(data){
            //判断是否是出错信息。
            if(data&&data.length==1){
                if(data[0]._errorMessage){
                    dhx.alert(data[0]._errorMessage);
                    return Tools.EmptyList;
                }
            }
            if(data){
//                $("_diffCount").innerHTML=data[data.length-1].DIFF_COUNT;
                data.splice(data.length-1,1);
            }
            //进行排序，将新增和删除的字段列于最前
            if(data){
                data.sort(function(value1,value2){
                    //删除最前
                    if(value1&&value1.DIFF==2){
                        return -1;
                    }
                    if(value2&&value2.DIFF==2){
                        return 1;
                    }
                    //其次新增
                    if(value1&&value1.DIFF==1){
                        return -1;
                    }
                    if(value2&&value2.DIFF==2){
                        return 1;
                    }
                    return 0;
                })
            }

        },
        userData:function(rowIndex, rowData){
            if(rowData.diff&&(rowData.diff=="2"||rowData.diff=="1")){
                rowData.match=false;
            }else{
                rowData.match=true;
            }
            return rowData;
        },
        cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
            var formatValue={};
            if((columnName == 'configIsPrimary'||columnName == 'configColNullabled'
                        ||columnName == 'instIsPrimary'||columnName == 'instColNullabled')&&cellValue=="--"){//如果是checkBox列，且无值
                formatValue={type:"ro",value:cellValue};
            }
            if(columnName!="diff"){//差异加上背景色
                if(rowData.diff){
                    if(rowData.diff==1||rowData.diff==2){ //新增和删除列
                        formatValue.style="background-color: #fdffc3;";
                        formatValue.value=cellValue;
                    }else{
                        switch (columnName) {
                            case  "configDataType":
                            case "instDataType":{ //字段类型差异
                                if(rowData.diff.indexOf("3")!=-1){
                                    formatValue.style="background-color: #fdffc3;";
                                    formatValue.value=cellValue;
                                }
                                break;
                            }
                            //主键差异
                            case "instIsPrimary" :
                            case "configIsPrimary":{
                                if(rowData.diff.indexOf("5")!=-1){
                                    formatValue.style="background-color: #fdffc3;";
                                    formatValue.value=cellValue;
                                }
                                break;
                            }
                            //是否空置差异
                            case "configColNullabled" :
                            case "instColNullabled":{
                                if(rowData.diff.indexOf("4")!=-1){
                                    formatValue.style="background-color:#fdffc3;";
                                    formatValue.value=cellValue;
                                }
                                break;
                            }
                            //默认值差异
                            case "instDefaultVal" :
                            case "configDefaultVal":{
                                if(rowData.diff.indexOf("6")!=-1){
                                    formatValue.style="background-color: #fdffc3;";
                                    formatValue.value=cellValue;
                                }
                                break;
                            }
                            //字段中文名差异
                            case "configColNamecn" :
                            case "instColNamecn" :{
                                if(rowData.diff.indexOf("7")!=-1){
                                    formatValue.style="background-color: #fdffc3;";
                                    formatValue.value=cellValue;
                                }
                                break;
                            }
                            //字段名差异
                            case "configColumnName" :
                            case "instColumnName" : {
                                if(rowData.diff.indexOf("9")!=-1){
                                    formatValue.style="background-color: #fdffc3;";
                                    formatValue.value=cellValue;
                                }
                                break;
                            }

                        }
                    }
                }
            }
            if(Tools.isEmptyObject(formatValue)){
                return cellValue
            }else{
                return formatValue;
            }
        }
    }),
    processMessage:"正在进行历史版本比较，耗时可能较长..."
})



/**
 * 历史版本比较
 */
var compare = function(){
    var rowIds = changeHistoryGrid.getCheckedRows(0);
    var rowIdArray = rowIds.split(",");
    if(rowIdArray.length != 2 && rowIdArray.length != 1){
//        dhx.alert("请选择两条数据进行比较！");
        return;
    }
    var rowData1 = changeHistoryGrid.getRowData(rowIdArray[0]);

    var version2;
    var version2desc;
    if(rowIdArray.length == 1){
        version2 = -9999;
        version2desc = "--";
    }else{
        var rowData2 = changeHistoryGrid.getRowData(rowIdArray[1]);
        version2 = rowData2.data[5];
        version2desc = version2;
    }
    var compareGrid=changeHistoryLayout.cells("b").attachGrid();
    compareGrid.setHeader("版本："+rowData1.data[5]+",#cspan,#cspan,#cspan,#cspan,#cspan,版本：&nbsp;&nbsp;" + version2desc +
                               "</select>,#cspan,#cspan,#cspan,#cspan,#cspan,差异描述",null,["text-align:center;","text-align:center","text-align:center;vertical-align: middle;"]);
    compareGrid.attachHeader("列名,列类型,主键,可为空,默认值,列中文名,列名,列类型,主键,可为空,默认值,列中文名,#rspan"
            ,["text-align:center;","text-align:center;","text-align:center","text-align:center;","text-align:center"
                ,"text-align:center;","text-align:center;","text-align:center","text-align:center;","text-align:center","text-align:center","text-align:center"]);
    compareGrid.setInitWidthsP("10,10,5,5,8,6,10,10,5,5,8,6,12");
    compareGrid.enableResizing("true,true,true,true,true,true,true,true,true,true,true,true,true");
    compareGrid.setColTypes("ro,ro,ch,ch,ro,ro,ro,ro,ch,ch,ro,ro,ro");
    compareGrid.setColAlign("left,left,center,center,center,center,left,left,center,center,center,center,left");
    compareGrid.setColSorting("na,na,na,na,na,na,na,na,na,na,na,na,na");
    compareGrid.init();
    compareGrid.load(dwrCaller.historyCompare+"&tableId="+tableId+"&tableVersion1="+rowData1.data[5]+"&tableVersion2="+version2,"json");
    compareGrid.setColumnCustFormat(12, function(value){
        var mapping={1:"字段新增",2:"字段删除",3:"类型差异",4:"可为空不同",5:"主键不同",6:"默认值不同",7:"字段中文名修改",8:"字段注释修改",9:"字段名修改"};
        if(version2==-9999){
            return "--";
        }
        if(value){
            return (value+"").replace(/(\d)(,)?/g,function($0,$1,$2){
                return mapping[$1]+($2?$2:"");
            })
        }
        return "";
    });//第1列进行转义
    compareGrid.setEditable(false);

};

dhx.ready(changeHistoryInit);