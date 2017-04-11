/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        category.js
 *Description：
 *        表类审核查看页面JS
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        王晶
 *Finished：
 *       2011-11-01
 *Modified By：
 *
 *Modified Date:
 *
 *Modified Reasons:
 *
 ********************************************************/
/*******************全局变量设置start***********************************************/
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller = new biDwrCaller();
var user =getSessionAttribute("user"); //取得当前登录的用户
var tableName = null;
var dhxWindow =null;
var dataSourceInfo = null;
var tableSpace = null;
var tableOwener = null;
var statusWindow=null;
var resultWindow = null;
/*******************全局变量设置end************************************************/

/*******************表的基本信息start****************************************************************/
dwrCaller.addAutoAction("queryBasicInfoByTableId","TableViewAction.queryBasicInfoByTableId");
/*******************表的基本信息end******************************************************************/

/********************表的列的展示start**************************************************************************/
//表列信息相关Action以及数据转换器 start
var colsConfig = {
    filterColumns:["preColName","preColDataType","preColNullabled","preDefaultVal","preIsPrimary","preColBusComment","preThisDim",
    	"colName","colDataType","colNullabled","defaultVal","isPrimary","colBusComment","thisDim","diff"],
    userData:function(rowIndex, rowData) {
        var userData = {};
        return userData;
    },
	cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
            var formatValue={};
            if((columnName == 'colNullabled'||columnName == 'defaultVal'||columnName == 'preColNullabled'
            			||columnName == 'preDefaultVal'||columnName == 'preIsPrimary'
                        ||columnName == 'isPrimary')&&cellValue=="--"){//如果是checkBox列，且无值
                formatValue={type:"ro",value:cellValue};
            }
            if(columnName!="diff"){//差异加上背景色
                if(rowData.diff){
                    if(rowData.diff==1||rowData.diff==2){ //新增和删除列
                        formatValue.style="background-color: #fdffc3;";
                        formatValue.value=cellValue;
                    }else{
                        switch (columnName) {
                            case  "colDataType":
                            case "preColDataType":{ //字段类型差异
                                if(rowData.diff.indexOf("3")!=-1){
                                    formatValue.style="background-color: #fdffc3;";
                                    formatValue.value=cellValue;
                                }
                                break;
                            }
                            //主键差异
                            case "isPrimary" :
                            case "preIsPrimary":{
                                if(rowData.diff.indexOf("5")!=-1){
                                    formatValue.style="background-color: #fdffc3;";
                                    formatValue.value=cellValue;
                                }
                                break;
                            }
                            //是否空置差异
                            case "colNullabled" :
                            case "preColNullabled":{
                                if(rowData.diff.indexOf("4")!=-1){
                                    formatValue.style="background-color:#fdffc3;";
                                    formatValue.value=cellValue;
                                }
                                break;
                            }
                            //默认值差异
                            case "defaultVal" :
                            case "preDefaultVal":{
                                if(rowData.diff.indexOf("6")!=-1){
                                    formatValue.style="background-color: #fdffc3;";
                                    formatValue.value=cellValue;
                                }
                                break;
                            }
                            //维度差异
                            case "thisDim" :
                            case "preThisDim":{
                                if(rowData.diff.indexOf("7")!=-1){
                                    formatValue.style="background-color: #fdffc3;";
                                    formatValue.value=cellValue;
                                }
                                break;
                            }
                             //注释差异
                            case "colBusComment" :{
                            	  break;
                            }
                            case "preColBusComment":{
                                if(rowData.diff.indexOf("8")!=-1){
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
};

var colsConfig2 = {
    filterColumns:["colName","colNameCn","colDatatype","colNullabled","defaultVal","isPrimary","colBusComment","thisdim"],
    userData:function(rowIndex, rowData) {
        var userData = {};
        return userData;
    }
};
var colsConverter = new dhtmxGridDataConverter(colsConfig);
dwrCaller.addAutoAction("queryColsByTableId", "TableViewAction.queryColsByTableId",tableId,tableVersion,relType);
if(relType == 2) {
	dwrCaller.addDataConverter("queryColsByTableId", colsConverter);
}else {
	dwrCaller.addDataConverter("queryColsByTableId", new dhtmxGridDataConverter(colsConfig2));
}

/*************************表的列的展示end****************************************************************************************/

/***************************维度列的弹出窗口start********************************************************************/
dwrCaller.addAutoAction("queryDimInfoByColId","TableViewAction.queryDimInfoByColId");
dwrCaller.addAutoAction("showCreateTableSql",TableExamineAction.getCreateTableSql,{
    dwrConfig:true,
    async:false
});
dwrCaller.addAutoAction("validataTable",TableExamineAction.validataTable,{
    dwrConfig:true,
    async:false,
    isShowProcess:false
});
dwrCaller.addAutoAction("insertInsTbl","TableExamineAction.insertInsTableByInfo");
dwrCaller.addAutoAction("validataTableIns",TableExamineAction.queryTableInstanceByTableInfo,{
    dwrConfig:true,
    async:false,
    isShowProcess:false
});
dwrCaller.addAutoAction("changeTableState","TableExamineAction.insertTableState",{
    dwrConfig:true,
    isShowProcess:false
});
dwrCaller.addAutoAction("queryDataSourceByTable",DiffAction.queryDataSourceByTable,tableId,tableVersion,
        {
            dwrConfig:true,
            callback: function(data){
                dataSourceInfo=data;
            },
            async:false,
            isShowProcess:false
        }
);

//执行一段SQL语句Action定义
dwrCaller.addAutoAction("executeSQL",DiffAction.executeSQL,{
    dwrConfig:true,
    isDealOneParam:false,
    isShowProcess:false
});


var linkDim = function(value){
    dhxWindow.createWindow("linkDimWindow", 0, 0, 400, 222);
    var linkDimWindow = dhxWindow.window("linkDimWindow");
    linkDimWindow.setModal(true);
    linkDimWindow.stick();
    linkDimWindow.setDimension(400, 222);
    linkDimWindow.center();
    linkDimWindow.denyResize();
    linkDimWindow.denyPark();
    linkDimWindow.setText("维表关联属性");
    linkDimWindow.keepInViewport(true);
    linkDimWindow.button("minmax1").hide();
    linkDimWindow.button("park").hide();
    linkDimWindow.button("stick").hide();
    linkDimWindow.button("sticked").hide();
    linkDimWindow.show();
    linkDimWindow.attachEvent("onClose", function(){
        linkDimWindow.hide();
        linkDimWindow.setModal(false);
        return false;
    })
    var linkLayout = new dhtmlXLayoutObject(linkDimWindow, "1C");
    linkLayout.cells("a").hideHeader();
    linkLayout.cells("a").attachObject("dimInfo");
    dwrCaller.executeAction("queryDimInfoByColId",value,tableVersion,function(data){
        $("_dimTableName").innerHTML = data["TABLE_NAME"]==null?"&nbsp;":data["TABLE_NAME"];
        $("_dimTableNameCn").innerHTML = data["TABLE_NAME_CN"]==null?"&nbsp;":data["TABLE_NAME_CN"];
        $("_dimTableBusComment").innerHTML = data["TABLE_BUS_COMMENT"]==null?"&nbsp;":data["TABLE_BUS_COMMENT"];
        $("_dimColName").innerHTML = data["TABLE_BUS_COMMENT"]==null?"&nbsp;":data["TABLE_BUS_COMMENT"];
        $("_dimType").innerHTML = data["DIM_TYPE_NAME"]==null?"&nbsp;":data["DIM_TYPE_NAME"];
        $("_dimLevel").innerHTML = data["DIM_LEVEL_NAME"]==null?"&nbsp;":data["DIM_LEVEL_NAME"];
    });
}
/***************************维度列的弹出窗口end********************************************************************/

/************************辅助检查结果start************************************************************************/
    //sql辅助检查结果的处理方法
    //验证之前的操作,用于判断当前表类是否存在实例表等
var checkTable =function(){
    var type = 0 ;
    if(tableName.indexOf("{")>-1
            &&tableName.indexOf("}")){ //代表该表为带宏变量
        type =1;
    }else{
        dwrCaller.executeAction("validataTable",dataSourceInfo,tableName,tableOwener,function(data){
            if(data){
                dwrCaller.executeAction("validataTableIns",tableId,tableName,function(value){
                    if(value){
                        type=3;
                    }else{
                        type=4;
                    }
                });
            }else{
                type = 2;
            }
        });//end ajax
    }
    return type;
}
//弹出建表sql的窗口
var showSql = function(tableParam){
    dwrCaller.executeAction("showCreateTableSql",tableId,tableVersion,function(sqlData){
        if(sqlData==null){
            dhx.alert("对不起,当前表类的建表SQL生成失败");
        }else{
            //var dhxWindow = new dhtmlXWindows();
            dhxWindow.createWindow("sqlWindow", 0, 0, 480,400);
            var sqlWindow = dhxWindow.window("sqlWindow");
            sqlWindow.stick();
            sqlWindow.setModal(true);
            sqlWindow.button("minmax1").hide();
            sqlWindow.button("park").hide();
            sqlWindow.button("stick").hide();
            sqlWindow.button("sticked").hide();
            sqlWindow.center();
            sqlWindow.denyResize();
            sqlWindow.denyPark();
            sqlWindow.setText("建表SQL预览");
            sqlWindow.keepInViewport(true);
            //建一个DIV
            var div=document.createElement("div");
            div.style.width="100%";
            div.style.height="100%";
            div.style.position="relative";
            var btnDiv = document.createElement("div");
            var sqlDiv = document.createElement("div");
            sqlDiv.setAttribute("id","sqlDiv");
            div.appendChild(sqlDiv);
            sqlDiv.style.width="100%";
            sqlDiv.style.height="300px";
            sqlDiv.style.overflow="auto";
            sqlDiv.innerHTML = sqlData;
            div.appendChild(btnDiv);
            var buttonForm = new dhtmlXForm(btnDiv,[{type:"block",className:'excuBtn',list:[
                {type:"button",name:"executeSql",value:"执行当前建表SQL"}

            ]}
            ]);
            //buttonForm.attachEvent("onButtonClick",function(id){
                //if(id=="executeSql"){
                    sqlWindow.close();
                    isSuccess = executeSql(sqlData,tableParam);
                    return isSuccess;
                //}
            //});
            sqlWindow.attachObject(div);
            sqlWindow.show();
        }
    });
}
//执行sql的方法
var executeSql = function(sqlData,tableParam){
    var winDiv=null;
    sqlData = sqlData.replace(/<BR\/>/ig,"");
    var sqls=sqlData.split(";");
    if(!sqls[sqls.length-1]||Tools.trim(sqls[sqls.length-1])==""){
        sqls.splice(sqls.length-1,1);
    }
    if(!statusWindow){
        statusWindow = dhxWindow.createWindow("statusWindow",0,0,400,220);
        statusWindow.setModal(true);
        statusWindow.stick();
        statusWindow.setDimension(700,400);
        statusWindow.center();
        statusWindow.denyResize();
        statusWindow.denyPark();
        statusWindow.button("minmax1").hide();
        statusWindow.button("park").hide();
        statusWindow.button("stick").hide();
        statusWindow.button("close").hide();
        statusWindow.button("sticked").hide();
        statusWindow.setText("SQL执行情况");
        statusWindow.keepInViewport(true);
        statusWindow.show();
        winDiv=document.createElement("div");
        winDiv.style.width="680px";
        winDiv.style.height="360px";
        winDiv.style.position="relative";
        winDiv.style.overflow="auto";
        winDiv.innerHTML="SQL开始执行...</br>";
        statusWindow.attachObject(winDiv);
        statusWindow.attachEvent("onClose",function(){
           statusWindow.hide();
           resultWindow.close();
           window.parent.closeTab("表类审核");
                    });
    }
    var isSycSuccess=true;
    var execute=function(i){
        winDiv.innerHTML+="正在执行SQL:"+(i==0?"</br>":"");
        winDiv.innerHTML+=sqls[i].replace(/\r\n/g,"</br>").replace(/\n/g,"</br>")+"</br>"
        dwrCaller.executeAction("executeSQL",sqls[i],dataSourceInfo,function(data){
            if(data.result){//执行SQl，
                winDiv.innerHTML+=((i==sqls.length-1)?"SQL执行完成！</br>":"此段SQL执行成功，准备执行下一段SQL</br>");
                if(i!=sqls.length-1){
                    execute(i+1);
                }else{
                    statusWindow.button("close").show();
                }
            }else{
                winDiv.innerHTML+="SQL执行出差，程序异常退出，出错信息:</br>"+data.errorMessage;
                isSycSuccess=false;
                if(i!=sqls.length-1){
                    dhx.alert("当前SQL执行出错",function(r){
                        statusWindow.button("close").show();

                    })
                }else{
                    statusWindow.button("close").show();
                    
                }
            }
            winDiv.scrollTop=winDiv.scrollHeight;
            //SQL全部执行完成之后的操作
            if(i==sqls.length-1){
                if(isSycSuccess){//成功执行返回true
                    isOK(tableParam);
                    return true;
// 									statusWindow.button("close").show();
//                                	statusWindow.attachEvent("onClose",function(){
//	    				          		window.parent.closeTab("表类审核");
//	    				          		return true;
//                                	});
                }
            }
        });
    }
    execute(0);
}

//辅助检查Action定义
dwrCaller.addAutoAction("validate", "TableExamineAction.validate");
dwrCaller.addDataConverter("validate",new dhtmxGridDataConverter({
    filterColumns:["checkItem","checkValue","desc","res"],isFormatColumn:false,
    cellDataFormat:function(rowIndex,columnIndex,columnName,cellValue,rowData){
        if(columnName=="res"){
            if(cellValue){
                return  getBasePath()+"/meta/resource/images/ok.png";
            }else{
                isValidateFalse=true;
                return  getBasePath()+"/meta/resource/images/cancel.png";
            }
        }
        return this._super.cellDataFormat(rowIndex,columnIndex,columnName,cellValue,rowData);
    }
}))
dwrCaller.isDealOneParam("validate",false);

//辅助检查结果的处理窗口
var lookupResult = function(tableId){
    dwrCaller.executeAction("validate",tableId,tableVersion,function(validateResult){
        dhxWindow.createWindow("resultWindow", 0, 0,600,400);
        resultWindow = dhxWindow.window("resultWindow");
        resultWindow.setModal(true);
        resultWindow.stick();
        resultWindow.center();
        resultWindow.denyResize();
        resultWindow.denyPark();
        resultWindow.setText("辅助检查结果");
        resultWindow.keepInViewport(true);
        resultWindow.button("minmax1").hide();
        resultWindow.button("park").hide();
        resultWindow.button("stick").hide();
        resultWindow.button("sticked").hide();
        var resultLayout = new dhtmlXLayoutObject(resultWindow, "2E");
        resultLayout.cells("a").hideHeader();
        resultLayout.cells("a").setHeight(240);
        resultLayout.cells("b").hideHeader();
        var myGrid = resultLayout.cells("a").attachGrid();
        myGrid.setHeader("检查项,检查值,说明,检查结果");
        myGrid.setInitWidthsP("18,15,55,12");
        myGrid.setColAlign("left,left,left,center");
        myGrid.setHeaderAlign("center,center,center,center");
        myGrid.setColTypes("ro,ro,ro,img");
        myGrid.setColSorting("na,na,na,na");
        myGrid.setEditable(false);
        myGrid.enableCtrlC();
        myGrid.enableMultiline(true);
        myGrid.init();
        myGrid.parse(validateResult,"json");
        myGrid.setRowTextStyle("1", "height:90px");
        resultLayout.hideConcentrate();
        resultLayout.hideSpliter();//移除分界边框。
        resultWindow.show();
        resultWindow.attachEvent("onClose", function(){
            resultWindow.hide();
            resultWindow.setModal(false);
            //                dhxWindow.unload();
        })
        var myForm = resultLayout.cells("b").attachForm([
        	{type:'block',list:[
        		{type:'input',name:'result',rows:3,label:'审核意见：',inputWidth:450,inputHeight:51,validate:'NotEmpty,MaxLength[128]'},
        		{type: "newcolumn"},{type:"label",label:'<span style="color:red;">*</span>'}]},
        		{type:'block',className:'resultButton',list:[{type:'hidden',name:'tableName'},
        			{type:'hidden',name:'resuleflag'},
        			{type:'hidden',name:'user',value:user.userId},
        			{type:'hidden',name:'tableVersion',value:tableVersion},
        			{type:'hidden',name:'tableId',value:tableId},
        			{type:'hidden',name:'reltype',value:relType},
        			{type:'hidden',name:'lastRelId',value:lastRelId},
        			{type:'button',name:'ok',value:'通过'},
	            {type: "newcolumn"},
	            {type:'button',name:'disallow',value:'驳回'},
	            {type: "newcolumn"},
	            {type:'button',name:'cancel',value:'取消'}

        ]}]);
        //审核和拒绝的处理方法
        myForm.defaultValidateEvent();
        var tableParam = new biDwrMethodParam();
        tableParam.setParamConfig([{
            index:0,type:"fun",value:function() {
                return myForm.getFormData();
            }
        }
        ]);
        myForm.attachEvent("onButtonClick",function(id){
        	if(id=="cancel") {
        		resultWindow.close();
        	}
            if(id=="disallow"){
                if(myForm.validate()){
                    myForm.setItemValue("resuleflag",0);
                    myForm.setItemValue("tableName",tableName);
                    dwrCaller.executeAction("changeTableState",tableParam,function(data){
                        if(data==1){
                            dhx.alert("操作成功!",function(){
                                resultWindow.close();
                                window.parent.closeTab("表类审核");
                            });
                        }else{
                            dhx.alert("操作失败!这有可能是由于该表的版本已经被更改",function(){
                                resultWindow.close();
                                window.parent.closeTab("表类审核");
                            });
                        }
                    });
                }
            }
            if(id=="ok"){
                if(myForm.validate()){
                    myForm.setItemValue("resuleflag",1);
                    myForm.setItemValue("tableName",tableName);

                    /*******先执行sql*********/
                    var type = checkTable();
                    if(type==1){    //type为1，表示带宏变量直接通过审核
                        dwrCaller.executeAction("changeTableState",tableParam,function(data){
                            if(data){
                                TableExamineAction.updateInstByTableId(tableId,tableVersion,function(){
                                    dhx.confirm("审核成功，是否进行差异比对",function(r){
                                        if(r){
                                            openMenu("差异分析","meta/module/tbl/diff/diffAnalysis.jsp?tableId="+tableId+"&tableVersion="+tableVersion+"&tableName="+tableName,"top");
                                            var tabId=window.parent.getTabIdByName("差异分析");
                                            window.parent.closeTab("表类审核",tabId);
                                        }else{
                                            window.parent.closeTab("表类审核");
                                        }
                                    });
                                });
                          		return true;
                            }else{
                                dhx.alert("操作失败!这有可能是由于该表的版本已经被更改",function(){
                                    resultWindow.close();
                                    window.parent.closeTab("表类审核");
                                });
                            }
                        });
                    }
                    else if(type==2){  //type为2，新增，需要执行sql
                        dwrCaller.executeAction("insertInsTbl",{tableName:tableName,tableId:tableId,tableSpace:tableSpace,tableOwener:tableOwener},function(value){
                            showSql(tableParam);  //执行sql
                        });
                    }else if(type==3){ //type为3
                        // 更新实例表信息为最新版本
                        dwrCaller.executeAction("changeTableState",tableParam,function(data){
                            if(data){
                                TableExamineAction.updateInstByTableId(tableId,tableVersion,function(){
                                    dhx.confirm("审核成功，是否进行差异比对",function(r){
                                        if(r){
                                            openMenu("差异分析","meta/module/tbl/diff/diffAnalysis.jsp?tableId="+tableId+"&tableVersion="+tableVersion+"&tableName="+tableName,"top");
                                            var tabId=window.parent.getTabIdByName("差异分析");
                                            window.parent.closeTab("表类审核",tabId);
                                        }else{
                                            window.parent.closeTab("表类审核");
                                        }
                                    })

                                });
                            }else{
                                dhx.alert("操作失败!这有可能是由于该表的版本已经被更改",function(){
                                    resultWindow.close();
                                    refreshTab(closeMenuId,getBasePath()+"/meta/module/tbl/examine/examine.jsp");
                                    window.parent.closeTab("表类审核");
                                });
                            }
                        });
                    }else if(type==4){ //type为4
                        dwrCaller.executeAction("changeTableState",tableParam,function(data){
                            if(data){
                                dwrCaller.executeAction("insertInsTbl",{tableName:tableName,tableId:tableId,tableSpace:tableSpace,tableOwener:tableOwener},function(value){
                                    if(value==1){
                                        dhx.confirm("审核成功，是否进行差异比对",function(r){
                                            if(r){
												window.parent.addTabRefreshOnActiveByName("表类管理");
                                                openMenu("差异分析","meta/module/tbl/diff/diffAnalysis.jsp?tableId="+tableId+"&tableVersion="+tableVersion+"&tableName="+tableName,"top");
                                                var tabId=window.parent.getTabIdByName("差异分析");
                            					window.parent.closeTab("表类审核",tabId);
                                            }else{
                                            	window.parent.closeTab("表类审核");
                                            }
                                        });
                                    }
                                });
                            }else{
                                dhx.alert("操作失败!这有可能是由于该表的版本已经被更改",function(){
                                    resultWindow.close();
                                    refreshTab(closeMenuId,getBasePath()+"/meta/module/tbl/examine/examine.jsp");
                                    window.parent.closeTab("表类审核");
                                });
                            }
                        });
                    }
                }
            }
        });


    });
}

//建表成功后，关掉窗口
var isOK = function(tableParam){
    dwrCaller.executeAction("changeTableState",tableParam,function(data){
        if(data){
            dhx.alert("审核成功！",function(){//成功执行后关掉主窗口。
                window.parent.closeTab("表类审核");
                return true;
            });
        }else{
            dhx.alert("操作失败!这有可能是由于该表的版本已经被更改",function(){
                refreshTab(closeMenuId,getBasePath()+"/meta/module/tbl/examine/examine.jsp");
                window.parent.closeTab("表类审核");
            });
        }
    });
}

/***********************辅助检查结果end************************************************************************************************/

//页面初始化函数
var pageInit = function(){
    //表类管理标签页应该触发时应该刷新。
    window.parent.addTabRefreshOnActiveByName("表类管理");
    
	$("dimInfo").style.cssText="display:none";
    dhxWindow =new dhtmlXWindows();
    dwrCaller.executeAction("queryDataSourceByTable");
    dwrCaller.executeAction("queryBasicInfoByTableId",tableId,tableVersion,function(data){
    	if(relType==4){
    		 $("_auditState").innerHTML = "审核通过";
    	}
    	if(relType==5){
    		 $("_auditState").innerHTML = "审核驳回";
    	}
        tableName = data["TABLE_NAME"];
        tableSpace = data["TABLE_SPACE"];
        tableOwener = data["TABLE_OWNER"];
        $("_tableDataSource").innerHTML = data["DATA_SOURCE_NAME"]==null?"&nbsp;":data["DATA_SOURCE_NAME"];
        $("_tableOwner").innerHTML = tableOwener==null?"&nbsp;":tableOwener;
        $("_tableNamecn").innerHTML = data["TABLE_NAME_CN"]==null?"&nbsp;":data["TABLE_NAME_CN"];
        $("_tableName").innerHTML = data["TABLE_NAME"]==null?"&nbsp;":data["TABLE_NAME"];
        $("_codeName").innerHTML = data["CODE_NAME"]==null?"&nbsp;":data["CODE_NAME"];
        $("_tableGroupName").innerHTML = data["TABLE_GROUP_NAME"]==null?"&nbsp;":data["TABLE_GROUP_NAME"];
        $("_tableBusComment").innerHTML = data["TABLE_BUS_COMMENT"]?data["TABLE_BUS_COMMENT"]:'&nbsp;';
        if(data.auditData.applyUser){
            $("_applyUser").innerHTML = data.auditData.applyUser;
        }
        if(data.auditData.applyDate){
            $("_applyTime").innerHTML = data.auditData.applyDate;
        }
        $("_tableSQL").innerHTML = data["PARTITION_SQL"]?data["PARTITION_SQL"]:'&nbsp;';
    });
    
    /**
     * 当表类操作类型为修改的时候，做差异分析
     * @param {Object} value
     * @return {TypeName} 
     */
    if(relType==2){
	    var colsGrid= new dhtmlXGridObject('tableColumnInfo');
	    
	    colsGrid.setHeader("修改前版本,#cspan,#cspan,#cspan,#cspan,#cspan,#cspan,修改后版本&nbsp;&nbsp;" +
	                               ",#cspan,#cspan,#cspan,#cspan,#cspan,#cspan,差异描述",null,["text-align:center;border-top-color:#a4bed4;","text-align:center;border-top-color:#a4bed4;","text-align:center;vertical-align: middle;border-top-color:#a4bed4;"]);
	    
	    colsGrid.attachHeader("名称,类型,允许空,默认值,主键,注释,维度,名称,类型,允许空,默认值,主键,注释,维度,#rspan"
	            ,["text-align:center;","text-align:center;","text-align:center","text-align:center;","text-align:center"
	                ,"text-align:center;","text-align:center;","text-align:center","text-align:center;","text-align:center","text-align:center;","text-align:center","text-align:center;","text-align:center"]);
	    colsGrid.setInitWidthsP("8,8,6,6,5,6,6,8,8,6,6,5,6,6,10");
	    //colsGrid.enableResizing("true,true,true,true,true,true,true,true,true,true,true,true,true,true,true");
	    colsGrid.setColTypes("ro,ro,ch,ro,ch,ro,ro,ro,ro,ch,ro,ch,ro,ro,ro");
	    colsGrid.setColAlign("left,left,center,center,center,left,center,left,left,center,center,center,left,center,center");
	    colsGrid.setColSorting("na,na,na,na,na,na,na,na,na,na,na,na,na,na,na");
	    colsGrid.init();
		colsGrid.setColumnCustFormat(6, function(value,rowdata,columnIndex){
			try{
		        if(value!=""&&value!=null&&value!="--"){
		            return "<a href='#' onclick='linkDim(\""+value+"\")'>维度列</a>";
		        }else{
		            return "";
		        }
	        }catch(e) {
	        	alert(e);
	        }
	    });
		
		colsGrid.setColumnCustFormat(13, function(value,rowdata,columnIndex){
	        if(value!=""&&value!=null&&value!="--"){
	            return "<a href='#' onclick='linkDim(\""+value+"\")'>维度列</a>";
	        }else{
	            return "";
	        }
	    });
	    
	    colsGrid.setColumnCustFormat(14, function(value){
	        var mapping={1:"字段新增",2:"字段删除",3:"类型差异",4:"可为空不同",5:"主键不同",6:"默认值不同",7:"维表关联差异",8:"注释修改"};
	        if(value){
	            return (value+"").replace(/(\d)(,)?/g,function($0,$1,$2){
	                return mapping[$1]+($2?$2:"");
	            })
	        }
	        return "";
	    });
	    colsGrid.setEditable(false);
	    var queryDiff=function(){
	        colsGrid.clearAll();
	        colsGrid.load(dwrCaller.queryColsByTableId,"json");
	    };
		queryDiff();
    }else {
    	var colsGrid= new dhtmlXGridObject('tableColumnInfo');
	    colsGrid.setHeader("名称,列中文名,类型,允许空,默认值,主键,注释,维度");
	    colsGrid.setHeaderAlign("left,center,center,center,center,center,center,center");
	    colsGrid.setInitWidthsP("15,15,10,10,10,10,15,15");
	    colsGrid.setColTypes("ro,ro,ro,ch,ro,ch,ro,ro");
	    colsGrid.enableCtrlC();
	    colsGrid.enableResizing("true,true,true,true,true,true,true,true");
	    //colsGrid.setColumnIds("colName","colDataType","colNullabled","defaultVal","isPrimary","colBusComment","thisdim");
	    colsGrid.setColAlign("left,left,left,center,center,center,left,center");
	    //colsGrid.setColumnCustFormat(2, yesOrNo);//第2列进行转义
	    //colsGrid.setColumnCustFormat(4, yesOrNo);
	    colsGrid.setColumnCustFormat(7, function(value,rowdata,columnIndex){
	        if(value!=""&&value!=null){
	            return "<a href='#' onclick='linkDim("+value+")'>维度列</a>";
	        }else{
	            return "";
	        }
	    });
	    colsGrid.init();
	    colsGrid.load(dwrCaller.queryColsByTableId, "json");
	    colsGrid.setEditable(false);
    }
    
  if(myApp == 1){ 
    
  }else if(myApp == 2){
	  if(tableVersion==1){
      var buttonForm = new dhtmlXForm("buttonDiv",[{type:"block",className:'buttonStyle',list:[
        {type:"button",name:"lookup",inputTop:30,value:"发起新申请"}
    ]}
    ]);
    buttonForm.attachEvent("onButtonClick",function(id){
        if(id=="lookup"){
        	dwrCaller.executeAction("queryBasicInfoByTableId",tableId,tableVersion,function(data){
            showPopUpWindow('basicInfo',tableId,data["TABLE_NAME"],data["CODE_NAME"],tableVersion,tableState,myApp);
            });
        }
    });
  }
  }else{
	 var buttonForm = new dhtmlXForm("buttonDiv",[{type:"block",className:'buttonStyle',list:[
        {type:"button",name:"lookup",inputTop:30,value:"审核"}
    ]}
    ]);
    buttonForm.attachEvent("onButtonClick",function(id){
        if(id=="lookup"){
            lookupResult(tableId);
        }
    });
	  
  }
  
  //跳转到维护界面
  var showPopUpWindow=function(value,tableId,tname,tableTypeId,tableVersion,tableState,myApp){
	if(tableTypeId != '维度表'){
    openMenu(tname+"申请调整","/meta/module/tbl/apply/myTableDetails.jsp"+"?tableId="+tableId+"&tableVersion="+tableVersion
        +"&tableName="+encodeURIComponent(tname)+"&myApp="+myApp,"top");
    }else{
    	openMenu(tname+"申请调整","/meta/module/tbl/apply/againApply.jsp?tableId="+tableId+"&tableVersion="+tableVersion+"&myApp="+myApp,"top");
    }
    
  };
}//end pageInit
dhx.ready(pageInit);