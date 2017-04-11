/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       import.js
 *Description：
 *       表类管理-导入实体表-差异化分析
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       李国民
 *Date：
 *       2012-01-18
 ********************************************************/

dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var currentDiffCount=0;//当前差异数。
var dwrCaller = new biDwrCaller();
//差异分析Action定义
dwrCaller.addAutoAction("diffAnalysis",ImportTableAction.getDiffList,dataSourceID,owner,tableName,tableId,tableVersion,{
    dwrConfig:true,
    converter:new dhtmxGridDataConverter({
        filterColumns:["configColumnName","configColumnNameCn","configDataType","configIsPrimary","configColNullabled","configDefaultVal",
            "instColumnName","instDataType","instIsPrimary","instColNullabled","instDefaultVal","diff"],
        onBeforeConverted:function(data){
            //判断是否是出错信息。
            if(data&&data.length==1){
                if(data[0]._errorMessage){
                    dhx.alert(data[0]._errorMessage);
                    return Tools.EmptyList;
                }
            }
            if(data){
                $("_diffCount").innerHTML=data[data.length-1].DIFF_COUNT;
                data.splice(data.length-1,1);
            }
            //进行排序，将新增和删除的字段列于最前
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
                if(value2&&value2.DIFF==1){
                    return 1;
                }
                //其次类型差异
                if(value1&&value1.DIFF==3){
                    return -1;
                }
                if(value2&&value2.DIFF==3){
                    return 1;
                }
                //其次字段名修改
                if(value1&&value1.DIFF==7){
                    return -1;
                }
                if(value2&&value2.DIFF==7){
                    return 1;
                }
                //其次默认值不同
                if(value1&&value1.DIFF==6){
                    return -1;
                }
                if(value2&&value2.DIFF==6){
                    return 1;
                }
                //其次主键不同
                if(value1&&value1.DIFF==5){
                    return -1;
                }
                if(value2&&value2.DIFF==5){
                    return 1;
                }
                return 0;
            })

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
                        ||columnName == 'instIsPrimary'||columnName == 'instIsPrimary')&&cellValue=="--"){//如果是checkBox列，且无值
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
    processMessage:"正在进行差异分析，耗时可能较长..."
})

var init=function(){
    var diffAnalysisLayout = new dhtmlXLayoutObject(document.body, "2E");
    diffAnalysisLayout.cells("a").setText("差异分析");
    diffAnalysisLayout.cells("b").hideHeader();
    diffAnalysisLayout.cells("b").setHeight(30);
    diffAnalysisLayout.cells("b").fixSize(true, true);
    diffAnalysisLayout.hideConcentrate();
    diffAnalysisLayout.hideSpliter();//移除分界边框。
//    var buttonForm = diffAnalysisLayout.cells("b").attachForm([
//        {type:"settings",position: "label-left", labelWidth: 70, inputWidth: 120},
//        {type:"button",value:"关闭",name:"close",offsetLeft:diffAnalysisLayout.cells("b").getWidth()/2-80}
//    ]);
//    
//    buttonForm.attachEvent("onButtonClick",function(id){
//        if(id=="close"){
//        	window.close();
//        }
//    })
    
    //加入grid
    diffAnalysisGrid=diffAnalysisLayout.cells("a").attachGrid();
    diffAnalysisGrid.setHeader("表类名："+metaTableName+",#cspan,#cspan,#cspan,#cspan,#cspan,实体表名："+tableName+",#cspan,#cspan,#cspan" +
                               ",#cspan,差异描述",null,["text-align:center;","text-align:center","text-align:center;vertical-align: middle;"]);
    diffAnalysisGrid.attachHeader("列名,列中文名,列类型,主键,可为空,默认值,列名,列类型,主键,可为空,默认值,#rspan"
            ,["text-align:center;","text-align:center;","text-align:center","text-align:center;","text-align:center"
                ,"text-align:center;","text-align:center;","text-align:center","text-align:center;","text-align:center"]);
    diffAnalysisGrid.setInitWidthsP("10,10,10,6,6,8,10,10,6,6,8,10");
    diffAnalysisGrid.enableResizing("true,true,true,true,true,true,true,true,true,true,true,true");
    diffAnalysisGrid.setColTypes("ro,ro,ro,ch,ch,ro,ro,ro,ch,ch,ro,ro");
    diffAnalysisGrid.setColAlign("left,left,left,center,center,center,left,left,center,center,center,left");
    diffAnalysisGrid.setColSorting("na,na,na,na,na,na,na,na,na,na,na,na");
    diffAnalysisGrid.init();
    diffAnalysisGrid.setColumnCustFormat(11, function(value){
        var mapping={1:"字段差异",2:"字段差异",3:"类型差异",4:"可为空不同",5:"主键不同",6:"默认值不同",7:"字段名修改"};
        if(value){
            return (value+"").replace(/(\d)(,)?/g,function($0,$1,$2){
                return mapping[$1]+($2?$2:"");
            })
        }
        return "";
    });//第1列进行转义
    diffAnalysisGrid.attachFooter("差异数：,#cspan,<span id='_diffCount'>"+currentDiffCount+"</span>,#cspan," +
                                  ",#cspan,#cspan,#cspan,#cspan,#cspan,#cspan",["text-align:right;","text-align:left;","text-align:left;","text-align:left;","text-align:center;"])
    diffAnalysisGrid.setEditable(false);
    diffAnalysisGrid.load(dwrCaller.diffAnalysis,"json");
}

dhx.ready(init);