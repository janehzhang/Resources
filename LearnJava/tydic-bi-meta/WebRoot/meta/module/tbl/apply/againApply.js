/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        addDim.js
 *Description：
 *       维度表类新增JS
 *Dependent：
 *
 *Author:
 *        张伟
 *Finished：
 *       2011-11-03
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
/**
 * 表类已提供的宏变量
 */
var tblMacro = ['{YY}','{YYYY}','{MM}','{DD}','{HH}','{MI}','{SS}','{M}','{D}','{YYYYMM}','{YYYYMMN}','{YYYYMMP}',
    '{YYYYMMDD}','{YYYYMMDDP}','{N_YY}','{N_YYYY}','{N_MM}','{N_DD}','{N_HH}','{N_MI}','{N_SS}','{N_M}','{N_D}',
    '{N_YYYYMM}','{N_YYYYMMN}','{N_YYYYMMP}','{N_YYYYMMDD}','{N_YYYYMMDDP}','{LOCAL_CODE}','{LOCAL_NAME}'];
var dwrCaller = new biDwrCaller();
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();

var getTblNameLength = function(value){
    var al = 0;
    value = value.replace(/{LOCAL_CODE}/ig,function(x){
        al += 4;
        return "";
    });
    value = value.replace(/{|}/g,"");
    return (value.length + al);
};

/**
 * 表类名验证。
 * @param value
 */
var tblNameCheck = function(value){
    //宏变量正则表达式。
    var macro = tblMacro.join("|");
    var tblNameRegStr = "^(" + macro + "|\\w)+$";
    var reg = new RegExp(tblNameRegStr);
    var res = reg.test(value) ? true : "表类名不符合规范！";
    if(res != true){
        return res;
    }
    if(getTblNameLength(value)>32){
        return "表类名长度超出32个字符！";
    }
    var dataSourceId=document.getElementById("_dataSource").value;
    var tableOwner = $('_tableOwner').value;
    //远程验证是否表已在数据库存在。
    TableApplyAction.isExistsMatchTables(value,dataSourceId,tableOwner,dimTableId, {
        async:false,
        callback:function(data){
            if(data){
                res = "您输入的表名在数据库中已存在，请重新输入！";
            } else{
                res = true;
            }
        }
    })
    return res;
}

/**
 * 系统表下拉数据转换器
 */
var sysCodeSelectConverter = new dhtmlxComboDataConverter({
    valueColumn:'codeItem',
    textColumn:"codeName"
})
dwrCaller.addAutoAction("queryTableType", TableApplyAction.queryTableType);
dwrCaller.addDataConverter("queryTableType", sysCodeSelectConverter);

//表类数据源相关Action
var tableDataSourceConverter = new dhtmlxComboDataConverter({
    valueColumn:"dataSourceId",
    textColumn:"dataSourceName"
})
var dataSourceData=null;
dwrCaller.addAutoAction("queryTableDataSource", "TableApplyAction.queryTableDataSource");
dwrCaller.addDataConverter("queryTableDataSource", tableDataSourceConverter);

//表名输入跟随Action定义
dwrCaller.addAutoAction("matchTables", "TableApplyAction.matchTables");
dwrCaller.addDataConverter("matchTables", new dhtmlxComboDataConverter({
    valueColumn:"tableId",
    textColumn:"tableName" ,
    userData: function(rowData){
        return rowData;
    }
}));
dwrCaller.isShowProcess("matchTables", false);
/**
 * 查询表类信息Action
 */
dwrCaller.addAutoAction("queryTableByIdAndVersion",TableDimAction.queryTableByIdAndVersion,dimTableId,tableVersion,{
    converter:new columnNameConverter(),
    dwrConfig:true
});
/**
 * 所有列信息Action定义
 */
dwrCaller.addAutoAction("queryAllColumnsByTableID",TableDimAction.queryAllColumnsByTableID,dimTableId,{
    dwrConfig:true,
    converter: new columnNameConverter()
});
var dimApplyInit = function(){
    var applyLayout = new dhtmlXLayoutObject(document.body, "2E"); //建立一个布局,document.body表明这个布局附着在哪里,可以是层或者window等,2E为布局格式,详见dhtmlX文档
    applyLayout.cells("a").setText("修改维度表");
    applyLayout.cells("a").setHeight(220);
    applyLayout.cells("a").fixSize(false, true);
    applyLayout.cells("a").attachObject($("_baseInfoForm"));
    applyLayout.cells("a").hideHeader();   //取消头部

    //设置需要动态读取与皮肤相关的CSS样式。
    dhx.html.addCss($("_tableBaseInfo"), global.css.formContentDiv);
    !dhx.env.isIE&&($("_tableBaseInfo").style.height="100%");
    applyLayout.hideConcentrate();//隐藏缩放按钮
//    applyLayout.cells("b").setText("编辑列属性");
    applyLayout.cells("b").attachObject($("_columnFormDiv"));
    applyLayout.cells("b").hideHeader();
//    $("_columnForm").style.height=($("_columnFormDiv").offsetHeight-30)/2+"px";
    $("_clumnContentDiv").style.height = ($("_columnFormDiv").offsetHeight - 80)/2 + "px";
    $("_columnHeadTable").style.width= ((dhx.env.isIE&&parseInt(dhx.env.ie)<=7)
            ?$("_clumnContentDiv").offsetWidth:$("_clumnContentDiv").clientWidth)+"px";
    $("_clumnDimContentDiv").style.height = ($("_columnFormDiv").offsetHeight - 80)/2 + "px";
    $("_clumnDimHeadTable").style.width=    ((dhx.env.isIE&&parseInt(dhx.env.ie)<=7)
            ?$("_clumnDimContentDiv").offsetWidth:$("_clumnDimContentDiv").clientWidth)+"px";
    if(dhx.env.isIE&&parseInt(dhx.env.ie)<=7){
        $("_columnDimForm").style.marginTop="-40px";
    }
    //设置CSS
    dhx.html.addCss($("_columnTableDiv"), global.css.gridTopDiv);
    dhx.html.addCss($("_columnDimTableDiv"), global.css.gridTopDiv);
    applyLayout.hideSpliter();
    //表分类下拉树数据准备。
    var tableGroupTree = new loadTableGroupTree($("_tableGroup"), $("_tableGroupId"));
    //为_tableGroup添加事件
    Tools.addEvent($("_tableGroup"), "click", function(){
        var tableType = 2;//维度表类型
        tableGroupTree.treeDiv.style.width = $("_tableGroup").offsetWidth + 'px';
        Tools.divPromptCtrl(tableGroupTree.treeDiv, $("_tableGroup"), true);
        tableGroupTree.treeDiv.style.display = "block";
        tableGroupTree.showGroupTree(tableType);
    });
    //加载所有的表类数据源
    dwrCaller.executeAction("queryTableDataSource", function(data){
        dataSourceData=data;
        Tools.addOption("_dataSource", data);
    })
    //表名输入跟随
    //输入时提醒。
    new Tools.completion($("_tableName"), {
        dwrAction:dwrCaller.matchTables,
        onTargetValeChange:function(value){
            if(value){
                //必须用call的方式保证onTargetValeChange在this对象上执行
                this._super.onTargetValeChange.call(this, value);
            } else{
                this.showAndHide("hide");
            }
        },
        onDivBuild:function(div){
            var title = dhx.html.create("div",
                    {style:"width:100%;hight:30px;background-color: #FFFFFF;z-index:1000;position:relative;font-weight:bold;" +
                           "margin-top:5px;margin-bottom: 5px;display:none"}, "已有类似表：");
            div.appendChild(title);
        },
        build:function(data){
        	if(data.options.length > 0){
        		 this._content.innerHTML = "";
	            var contentHtml = '<table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" >' +
	                              '<tbody>';
	            this._selectIndex = -1;//重置选择索引。
	            this._options = data.options || data;
	            //新增表头
	            contentHtml += "<tr style='background-color: #0099FF;'><td style='text-align: center;width: 20%'>表名</td>" +
	                           "<td style='text-align: center;width: 20%'>表中文名</td><td style='text-align: center;width: 20%'>表拥有者</td><td style='text-align: center;width: 40%'>注释</td></tr>"
	            if(this._options && this._options.length > 0){
	                for(var i = 0; i < this._options.length; i++){
	                    contentHtml += '<tr id="' + this.getSelectUniqueName(i) + '" style="' + this.style.unSelect +
	                                   '" onmouseover=Tools.completion._completions[' + +this._uid + '].suggestOver(this,' +
	                                   i + ')';
	                    contentHtml += " onmouseout=Tools.completion._completions[" + this._uid + "].suggestOut(this) ";
	                    contentHtml += " onmousedown=Tools.completion._completions[" + this._uid + "].select(" + i + ") >";
	                    contentHtml += "<td style='text-align: center;'>" +
	                                   this._options[i].text.replace(new RegExp(this._keyWord, "i"),
	                                           "<font color=red>" + this._keyWord + "</font>") + "</td>";
	                    contentHtml += '<td style="text-align: center;">' + this._options[i].userdata.tableNameCn + '</td>';
	                    contentHtml += '<td style="text-align: center;">' + this._options[i].userdata.tableOwner + '</td>';
	                    contentHtml += '<td style="text-align: center;">' + this._options[i].userdata.tableBusComment +
	                                   '</td>';
	                    contentHtml += "</tr>";
	                }
	            } else{
	                contentHtml += "<tr><td style='text-align: center;' colspan='3'>无匹配记录</td></tr>"
	            }
	            contentHtml += "</tbody></table>";
	            this._content.innerHTML = contentHtml;
	            this._div.style.width = "400px";
	            this._content.style.width = "400px";
	            this._div.style.display = "inline";
	            Tools.divPromptCtrl(this._div, this.target, true);
        	}
        }
    });

    //对表单添加验证
    dhtmlxValidation.addValidation($("_baseInfoForm"), [
        {target:"_tableName",rule:"NotEmpty,MaxLength[1000],ValidByCallBack[tblNameCheck]"},
        {target:"_tableDimPrefix",rule:"NotEmpty"},
        {target:"_tableGroup",rule:"NotEmpty"},
        {target:"_dataSource",rule:"NotEmpty" },
        {target:"_tableBusComment",rule:"MaxLength[1000]"},
        {target:"_partitionSql",rule:"MaxLength[1000]"},
        {target:"_tableSpace",rule:"MaxLength[64],ValidAplhaNumeric"},
        {target:"_tableNameCn",rule:"NotEmpty,MaxLength[64]"},
        {target:"_tableOwner",rule:"MaxLength[32],ValidAplhaNumeric"},
        {target:"_tableDimPrefix",rule:"MaxLength[12]"}
    ])
    addColumnRow();
    addDimColumnRow();
    //预览SQL按钮
    var showSQL = Tools.getButtonNode("预览SQL");
     showSQL.style.marginTop = "0px";
    showSQL.style.cssFloat="left";
    showSQL.style.styleFloat="left";
    showSQL.style.marginLeft="10px";
    showSQL.onclick = function(){
        genSql();
    }

    //重置按钮
    var reset = Tools.getButtonNode("重置");
    reset.style.marginTop = "0px";
    reset.style.marginLeft = "10px";
    reset.style.cssFloat="left";
    reset.style.styleFloat="left";
    reset.onclick = function(){
        resetFormData();
    }

    //提交按钮
    var submit = Tools.getButtonNode("提交");
    $("_submit").appendChild(submit);
    $("_submit").appendChild(reset);
    $("_submit").appendChild(showSQL);
    submit.style.marginLeft = (Math.round($("_submit").offsetWidth / 2)-100) + "px";
    submit.style.marginTop="0px";
    submit.style.cssFloat="left";
    submit.style.styleFloat="left";
    submit.onclick = function(){
        submitValidate();
    }
    /**  原样式方法
    //导入按钮图标
    var dataBaseImportDiv=dhx.html.create("div",{style:"position: absolute;right: 140px;top:6px;width: 105px;height: 18px;cursor: pointer"}
            ,"<img src='"+getBasePath()+"/meta/resource/images/dataBase.png' width='18px' height='18px' border='0' style='float:left'>");
    dataBaseImportDiv.innerHTML+="<div style='margin-top: 0px;position: relative;float:left;margin-left: 0px;'>从数据库表导入</div>"
    var tableImportDiv=dhx.html.create("div",{style:"position: absolute;right: 5px;top:6px;width: 115px;height: 18px;cursor: pointer"}
            ,"<img src='"+getBasePath()+"/meta/resource/images/tableImport.png' width='18px' height='18px' border='0' style='float:left'>");
    tableImportDiv.innerHTML+="<div style='margin-top: 0px;position: relative;float: left;margin-left: 0px;'>从元数据表类复制</div>"
    applyLayout.polyObj["a"].childNodes[0].firstChild.appendChild(dataBaseImportDiv);
    applyLayout.polyObj["a"].childNodes[0].firstChild.appendChild(tableImportDiv);
    //表类复制添加事件。
    tableImportDiv.onclick=importFormTable;
    dataBaseImportDiv.onclick=importFromDatabase;
    */
    //查询表类信息完成之后的初始化动作
    initInfo();
    Tools.addEvent($("_tableDimPrefix"),"keyup",resertColName);
}

var initInfo = function (){
	 //加载所有的表类数据源
    dwrCaller.executeAction("queryTableDataSource", function(data){
        dataSourceData=data;
        Tools.addOption("_dataSource", data);
    })
    
    dwrCaller.executeAction("queryTableByIdAndVersion",function(data){
        copyTablData(data);
        //查询列信息。
        dwrCaller.executeAction("queryAllColumnsByTableID",tableVersion,function(columnDatas){
            if(columnDatas&&columnDatas.length>0){
                for(var i=0;i<columnDatas.length;i++){
                    var columnData=columnDatas[i];
                    //父子元素初始化
                    if(data.dimKeyColId&&data.dimKeyColId==columnData.colId){
                        columnData.columAttribute=1;
                    }
                    if(data.dimParKeyColId&&data.dimParKeyColId==columnData.colId){
                        columnData.columAttribute=2;
                    }
                    //设置值。
                    copyColumnData(columnData,columnNum);
                    var initCol= isInitCol(data.tableDimPrefix,columnData.colName,columnNum);
                    var colsName = columnData.colName;
                    if(initCol && colsName.length > 0 || myApp != 1){
                        $("_colName"+columnNum).setAttribute("disabled",true);
                        $("_columAttribute"+columnNum).setAttribute("disabled",true);
                        dataTypeCombo[columnNum].disable(true);
                        $("_isPrimary"+columnNum).setAttribute("disabled",true);
                        $("_colNullabled"+columnNum).setAttribute("disabled",true);
                        $("_dimInfo"+columnNum).setAttribute("disabled",true);
                    }
                    //判断是否为驳回的表
                    if(myApp != 1 && initCol == false){
                    	initCol = true;
                    }else if(myApp == 1 && initCol == false){
                    	initCol = false;
                    }
                    addColumnRow(null,initCol,colsName);
                }
            }
        })
        //查询DIM TYPE
        dwrCaller.executeAction("queryDimType", dimTableId,function(dimTypeDatas){
            if(dimTypeDatas&&dimTypeDatas.options.length>0){
                for(var i=0;i<dimTypeDatas.options.length;i++){
                    var columnData=dimTypeDatas.options[i].userdata;
                    //设置值。
                    copyDimTypeData(columnData,dimColumnNum);
                    addDimColumnRow();
                }
            }
        })
    });
}

/**
 * 添加查询表分类Action
 */
dwrCaller.addAutoAction("queryTableGroup", TableApplyAction.queryTableGroup,{
    dwrConfig:true,
    onBeforeLoad:function(params){
    },
    callback:function(){

    },
    converter: new dhtmxTreeDataConverter({
        idColumn:"tableGroupId",pidColumn:"parGroupId",textColumn:"tableGroupName",
        isDycload:false
    })
    ,isShowProcess:false,
    isDealOneParam:true
});
var treeData = {};
var treeData2 = {};
var lastTableType2 = null;
var lastTableType = null;//最后一次显示时的tableType.

/**
 * 加载业务类型树形结构。
 * @param target
 * @param form
 */

var loadTableGroupTree = function(target, hide){
    //创建tree Div层
    div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    this.treeDiv = div;
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    target.readOnly = true;
    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    //树双击鼠标事件
    tree.attachEvent("onDblClick", function(nodeId){
        target.value = tree.getItemText(nodeId);
        hide.value = nodeId;
        //关闭树
        div.style.display = "none";
    });
    this.tree = tree;
    this.showGroupTree = function(tableType){
        var show = function(){
            if(lastTableType != tableType){
                var childs=tree.getAllSubItems(0);
                if(childs){
                    var childIds=childs.split(",");
                    for(var i=0;i<childIds.length;i++){
                        tree.deleteItem(childIds[i]);
                    }
                }
                tree.loadJSONObject(treeData[tableType]);
            }
        }
        if(!treeData[tableType]){
            dwrCaller.executeAction("queryTableGroup", tableType, function(data){
                treeData[tableType] = data;
                show();
                lastTableType=tableType;
            })
        } else{
            show();
            lastTableType=tableType;
        }
    }
}

var loadTableGroupTree2 = function(target, hide){
    //创建tree Div层
    div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    this.treeDiv = div;
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    target.readOnly = true;
    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    //树双击鼠标事件
    tree.attachEvent("onDblClick", function(nodeId){
        target.value = tree.getItemText(nodeId);
        hide.value = nodeId;
        //关闭树
        div.style.display = "none";
    });
    this.tree = tree;
    this.showGroupTree = function(tableType){
        var show = function(){
            if(lastTableType2 != tableType){
                var childs=tree.getAllSubItems(0);
                if(childs){
                    var childIds=childs.split(",");
                    for(var i=0;i<childIds.length;i++){
                        tree.deleteItem(childIds[i]);
                    }
                }
                tree.loadJSONObject(treeData2[tableType]);
            }
        }
        if(!treeData2[tableType]){
            dwrCaller.executeAction("queryTableGroup", tableType, function(data){
                treeData2[tableType] = data;
                show();
                lastTableType2=tableType;
            })
        } else{
            show();
            lastTableType2=tableType;
        }
    }
}

/**
 * 缩放实现。
 * @param div 缩放的div节点
 * @param collapseContent  缩放的区域。
 * @param extend 要扩展的的区域
 */
var collapse=function(div,collapseContent,extend){
    var isOpen=(div._isOpen==null||div._isOpen==undefined)?true:div._isOpen;
    div._isOpen=!isOpen;
    if(!$(collapseContent)._orgHeight){
        $(collapseContent)._orgHeight= $(collapseContent).offsetHeight
    }
    if(!$(extend)._orgHeight){
        $(extend)._orgHeight= $(extend).offsetHeight
    }
    if(isOpen){//是展开的，则收缩。
        $(collapseContent).style.display="none";
        div.style.backgroundPosition="0px 0";
        $(extend).style.height=  ($(extend).offsetHeight+$(collapseContent)._orgHeight)+"px";
    }else{//是收缩，展开
        div.style.backgroundPosition="-32px 0";
        $(collapseContent).style.height=($(collapseContent)._orgHeight)+"px";
        $(collapseContent).style.display="block";
        $(extend).style.height=  ($(extend)._orgHeight)+"px";
    }
}
/**
 * 打开列注释输入框。
 * @param img
 */
var openComment = function(img) {
    var input = $("_colBusComment" + img.parentNode.parentNode._index);
    if (!$("_columnComment")) {
        var div = document.createElement("div");
        div.style
                .cssText = "display;none;position:absolute;border: 1px #eee solid;overflow: auto;padding: 0;margin: 0;background-color: #F7F7F7;" +
                           "z-index:1000";
        div.style.width = (input.offsetWidth < 170 ? 170 : input.offsetWidth) + "px";
        div.style.height = "250px";
        div.id = "_columnComment";
        div.innerHTML = "<table cellpadding='0' cellspacing='0' style='width:100%;height: 100%'>" +
                        "<tr><td style='text-align: center;height: 90%' valign='middle'> <textarea class='dhxlist_txt_textarea' id='_comment' type='TEXT'style='width: 90%;height: 90%' rows='14'></textarea></td></tr>" +
                        "<tr><td id='_button' style='text-align: center;height: 10%'></td></tr></table>";
        document.body.appendChild(div);
        //添加确定按钮。
        var button = Tools.getButtonNode("确定", getBasePath() + '/meta/resource/images/ok.png');
        button.style.cssFloat="left";
        button.style.styleFloat="left";
        button.onclick = function(e) {
            $("_colBusComment"+$("_columnComment")._rowIndex).value = $("_comment").value;
            $("_columnComment").style.display = "none";
        }
        $("_button").appendChild(button);
        //添加取消按钮
        button = Tools.getButtonNode("取消", getBasePath() + '/meta/resource/images/cancel.png');
        button.style.cssFloat="left";
        button.style.styleFloat="left";
        button.onclick = function() {
            $("_columnComment").style.display = "none";
        }
        $("_button").appendChild(button);

    }
    $("_comment").value = input.value;
    $("_columnComment")._rowIndex=img.parentNode.parentNode._index;
    Tools.divPromptCtrl($("_columnComment"), input, false);
    $("_columnComment").style.display = "inline";
}

var dataTypeData = undefined;//缓存DATATYPE数据
dwrCaller.addAutoAction("queryColumnDataType", "TableApplyAction.queryColumnDataType", function(data){
    dataTypeData = data;
});
dwrCaller.addDataConverter("queryColumnDataType", sysCodeSelectConverter);

//列字段输入跟随Action定义
dwrCaller.addAutoAction("matchCols", "TableApplyAction.matchCols");
dwrCaller.addDataConverter("matchCols", new dhtmlxComboDataConverter({
    valueColumn:"colName",
    textColumn:"colName" ,
    userData: function(rowData){
        return rowData;
    }
}));
dwrCaller.isShowProcess("matchCols", false);
var columnNum = 0;//行递增索引。
var dataTypeCombo = {};//数据类型combo对象。
/**
 * 数据类型验证
 * @param value
 */
var supportStr = ""
var dataTypeValidate = function(value){
    //先找到支持的数据类型，构造正则表达式
    if(!supportStr){
        if(dataTypeData && dataTypeData.options.length > 0){
            for(var i = 0; i < dataTypeData.options.length; i++){
                supportStr += dataTypeData.options[i].text + ((i == dataTypeData.options.length - 1) ? "" : "|");
            }
            //number 特殊处理
            supportStr += "|NUMBER\\(\\d+,\\-?\\d+\\)|NUMBER\\(\\d+\\)";
            //varchar2
            supportStr=supportStr.replace(/VARCHAR2/ig,"");
            supportStr=supportStr.replace(/VARCHAR/ig,"");
            supportStr=supportStr.replace(/CHAR/ig,"");
            supportStr=supportStr.replace(/RAW/ig,"");
            supportStr += "|VARCHAR2\\(\\d+\\)";
            //varchar
            supportStr += "|VARCHAR\\(\\d+\\)";
            //char
            supportStr += "|CHAR\\(\\d+\\)";
            //raw
            supportStr += "|RAW\\(\\d+\\)";
//            supportStr += "|DATE\\(\\d+\\)";
        } else{
            return "未找到支持的数据类型，请先配置相应的数据类型！";
        }
    }
    var reg = new RegExp("^(" + supportStr + ")$", "i");
    var res = reg.exec(value);
    if(!res){
        return "不支持此数据类型或者未填长度大小！";
    }
    reg = /(\w+)(\((\d+)((,)(\-?\d+))?\))*/
    var match = reg.exec(value)
    switch(match[1].toLowerCase()){
        case "varchar":
        case "varchar2":
        case "nvarchar":
        case "nvarchar2":
        {
            if(match[3] > 4000 || match[3] < 1){
                return "VARCHAR、VARCHAR2、VARCHAR、NVARCHAR2取值范围为1至4000";
            }
            break;
        }
        case "raw":
        case "char":
        {
            if(match[3] > 2000 || match[3] < 1){
                return "CHAR长度取值范围为1至2000";
            }
            break;
        }
        case "number":
        {
            if(match[3] && (match[3] > 38 || match[3] < 1)){
                return "NUMBER类型精度取值范围为1至38";
            }
            if(match[6] && (match[6] > 127 || match[6] < -84)){
                return "NUMBER类型刻度取值范围为-84至127";
            }
            break;
        }
        default:
            break;
    }
    return true;
}
//维度基本列信息
var initColData=["ID","PAR_ID","CODE","NAME","DESC","DIM_TYPE_ID","DIM_TABLE_ID","STATE","DIM_LEVEL","MOD_FLAG","ORDER_ID"];
var initRowIndex={};//初始化字段的ROWINDEX
/**
 * 是否是维度初始列
 */
var isInitCol=function(prefix,columnName,rowIndex){
    var isInitCol=false;//是否是系统固定列
    for(var i=0;i<initColData.length;i++){
        if(i<5){
            if((prefix+"_"+initColData[i]).toUpperCase()==columnName.toUpperCase()){
                isInitCol=true;
                initRowIndex[initColData[i]]=rowIndex;
                break;
            }
        }else{
            if(initColData[i].toUpperCase()==columnName.toUpperCase()){
                isInitCol=true;
                initRowIndex[initColData[i]]=rowIndex;
                break;
            }
        }
    }
    return isInitCol;
}
/**
 * 根据输入的前缀重置需要有前缀的字段
 */
var resertColName=function(){
    var prex=dwr.util.getValue("_tableDimPrefix");
    for(var i=0;i<5;i++){
        var input=$("_colName"+initRowIndex[initColData[i]]);
        var columnName=prex?(prex.toUpperCase()+"_"+initColData[i]):initColData[i]
        dwr.util.setValue(input,columnName);
    }
}
/**
 * 列名验证。
 * @param value
 */
var columnNameCheck=function(value){
    var reg=/^[a-zA-Z]\w*/
    if(reg.exec(value)){
        //判断列名是否有重复
        var count=0;
        var tableObj = $("_clumnContentTable");
        for(var i = 1; i < tableObj.rows.length - 1; i++){
            var rowIndex=tableObj.rows[i]._index;
            if($("_colName"+rowIndex).value==value){
                count++
            }
        }
        return count==1?true:"列名有重复";
    }
    return "列名不符合规范！";
}
/**
 * 新增一行列配置
 */
var addColumnRow = function(rowIndex,isShowOper){
    rowIndex = (rowIndex == undefined || rowIndex == null) ? (++columnNum) : rowIndex;
    if(!$("_columnRow" + rowIndex)){
        if(columnNum < rowIndex){
            columnNum = rowIndex;
        }
        var tr = document.createElement("tr");
        tr._index = rowIndex;
        tr.id = "_columnRow" + rowIndex;
        $("_clumnContentBody").appendChild(tr);
        //名称列
        var td = tr.insertCell(0);
        td.align = "center";
        var colId=dhx.html.create("input",{name:"colId",id:"_colId"+rowIndex,type:"hidden"});
        td.appendChild(colId);
        var input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"colName",id:"_colName" +
                                                                                                 rowIndex,type:"TEXT",style:"width: 90%;"});
        //添加事件，开始输入时，新增一行。
        Tools.addEvent(input, "keyup", function(e){
            e=(e||window.event);
            var element=e.srcElement||e.target;
            var thisTr = element.parentNode.parentNode;
            //新增空白行。
            if(!thisTr.nextSibling){
                addColumnRow(parseInt(thisTr._index) + 1,false);
            }
        })
        //输入时提醒。
        new Tools.completion(input, {
            dwrAction:dwrCaller.matchCols,
            onTargetValeChange:function(value){
                if(value.length >= 4){//输入四个字母时输入跟随。
                    //必须用call的方式保证onTargetValeChange在this对象上执行
                    this._super.onTargetValeChange.call(this, value);
                } else{
                    this.showAndHide("hide");
                }
            },
            build:function(data){
            	if(data.options.length > 0){
	                this._content.innerHTML = "";
	                var contentHtml = '<table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" >' +
	                                  '<tbody>';
	                this._selectIndex = -1;//重置选择索引。
	                this._options = data.options || data;
	                //新增表头
	                contentHtml += "<tr style='background-color: #0099FF;'><td style='text-align: center;width: 25%'>名称</td>" +
	                               "<td style='text-align: center;width: 25%'>类型</td><td style='text-align: center;width: 50%'>注释</td></tr>"
	                if(this._options && this._options.length > 1){
	                    for(var i = 0; i < this._options.length; i++){
	                        contentHtml += '<tr id="' + this.getSelectUniqueName(i) + '" style="' + this.style.unSelect +
	                                       '" onmouseover=Tools.completion._completions[' + +this._uid +
	                                       '].suggestOver(this,' + i + ')';
	                        contentHtml += " onmouseout=Tools.completion._completions[" + this._uid + "].suggestOut(this) ";
	                        contentHtml += " onmousedown=Tools.completion._completions[" + this._uid + "].select(" + i +
	                                       ") >";
	                        contentHtml += "<td style='text-align: center;'>" +
	                                       this._options[i].text.replace(new RegExp(this._keyWord, "i"),
	                                               "<font color=red>" + this._keyWord + "</font>") + "</td>";
	                        contentHtml += '<td style="text-align: center;">' + this._options[i].userdata.datatype +
	                                       '</td>';
	                        contentHtml += '<td style="text-align: center;">' + this._options[i].userdata.colBusComment +
	                                       '</td>';
	                        contentHtml += "</tr>";
	                    }
	                } else{
	                    contentHtml += "<tr><td style='text-align: center;' colspan='3'>无匹配记录</td></tr>"
	                }
	                contentHtml += "</tbody></table>";
	                this._content.innerHTML = contentHtml;
	                this._div.style.width = "300px";
	                this._content.style.width = "300px";
	                this._div.style.display = "inline";
	                Tools.divPromptCtrl(this._div, this.target, true);
                }
            },
            select:function(index){
                var data = this._options[index].userdata;
                //获取行rowIndex;
                var rowIndex = this.target.parentNode.parentNode._index;
                copyColumnData(data,rowIndex);
                this.showAndHide("hide");
            }
        });
        td.appendChild(input);
        //中文名列
        td = tr.insertCell(1);
        td.align = "center";
        input = dhx.html.create("input", {name:"colNameCn",id:"_colNameCn" +
                                                              rowIndex,className:"dhxlist_txt_textarea",type:"TEXT",style:"width: 90%;"})
        td.appendChild(input);
        //类型列。
        td = tr.insertCell(2);
        td.align = "center";
        var inputDiv = dhx.html.create("select", {style:"width:90%",id:"_colDatatype" + rowIndex,name:"colDatatype"});
        td.appendChild(inputDiv);
        var dataCombo = new dhtmlXCombo(inputDiv);
        if(dataTypeData){
            dataCombo.addOption(dataTypeData.options);
        } else{
            dataCombo.loadXML(dwrCaller.queryColumnDataType,function(){
                dataCombo.setComboText("NUMBER");
            });
        }
        dataCombo.setComboText("NUMBER");
        dataTypeCombo[rowIndex] = dataCombo;
        //主键列。
        td = tr.insertCell(3);
        td.align = "center";
        input = dhx.html.create("input",
                {name:"isPrimary",id:"_isPrimary" + rowIndex,type:"checkbox",style:"width: 90%;margin-bottom:3px;"});
        //主键选择事件
        input.onclick=function(e){
            e=e||window.event;
            var target=e.target||e.srcElement;
            var clickIndex=target.parentNode.parentNode._index;
            if(target.checked){//如果是主键。不能允许空
                $("_colNullabled"+clickIndex).checked=false;
            }
        }
        td.appendChild(input);
        //是否允许空列
        td = tr.insertCell(4);
        td.align = "center";
        input = dhx.html.create("input",
                {name:"colNullabled",id:"_colNullabled" + rowIndex,type:"checkbox",style:"width: 90%;margin-bottom:5px;"})
        input.onclick=function(e){
            e=e||window.event;
            var target=e.target||e.srcElement;
            var clickIndex=target.parentNode.parentNode._index;
            if(target.checked&&$("_isPrimary"+clickIndex).checked){//如果运行为null
                dhx.alert("主键不能允许为NULL");
                target.checked=false;
            }
        }
        td.appendChild(input);
        //默认值列
        td = tr.insertCell(5);
        td.align = "center";
        input = dhx.html.create("input", {name:"defaultVal",id:"_defaultVal" +
                                                               rowIndex,className:"dhxlist_txt_textarea",type:"TEXT",style:"width: 90%;margin-bottom:-1px;"})
        td.appendChild(input);

        //注释列
        td = tr.insertCell(6);
        td.align = "center";
        input = dhx.html.create("input", {name:"colBusComment",id:"_colBusComment" +
                                                                  rowIndex,className:"dhxlist_txt_textarea",type:"TEXT",style:"width: 65%;margin-bottom:6px;"});
        td.appendChild(input);
        td.innerHTML += '<img src="../../../resource/images/more.png" alt="更多" onclick="openComment(this)">';

        //维度列
        td = tr.insertCell(7);
        td.align = "center";
        td.innerHTML='<input type="text" id="_dimInfo'+rowIndex+'" name="dimInfo" class="dhxlist_txt_textarea" readOnly="true" style="width:90%;margin-bottom:3px;" onclick="selectDim(this)">';

        //新增维度有关的hidden隐藏域
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"colBusType",value:1,id:"_colBusType" + rowIndex}));//默认非维度，指标
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"dimLevel",id:"_dimLevel" + rowIndex}));//维度层级
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"dimColId",id:"_dimColId" + rowIndex}));//关联的列ID。
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"dimTableId",id:"_dimTableId" + rowIndex}));//关联的维度表ID
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"dimTypeId",id:"_dimTypeId" + rowIndex}));

        //列属性
        td = tr.insertCell(8);
        td.align = "center";
        input = dhx.html.create("select", {name:"columAttribute",id:"_columAttribute" + rowIndex,style:"width: 90%;"} );
        //IE 下select的innerHtml无效
//        input.innerHTML="<option value='0'>--请选择--</option><option value='1'>子</option><option value='1'>父</option>";
        //替代为：
        input.options[0]=new Option("--请选择--",0);
        input.options[1]=new Option("子",1);
        input.options[2]=new Option("父",2);
//        input.innerHTML="<option value='0'>--请选择--<option value='1'>子<option value='1'>父";
        td.appendChild(input);

        td = tr.insertCell(9);
        td.align = "center";
        //新增一行不能删除。
        td.innerHTML = "&nbsp;";

        //已经编辑的行可以删除。
        if(isShowOper!=true){
            var lastTr=Tools.findPreviousSibling(tr);
            if(lastTr&&lastTr._index){
                lastTr.lastChild.innerHTML = '<img src="../../../resource/images/cancel.png" alt="删除" onclick="removeRow(this)" style="width:16px;height: 16px;cursor: pointer">' +
                                             '<img src="../../../resource/images/move_up.png" id=' + "_moveUp" + rowIndex +
                                             ' alt="上移" onclick="moveUp(this,\'_moveUp\',\'_moveDown\')" style="width:16px;height: 16px;margin-left: 5px;cursor: pointer;">' +
                                             '<img src="../../../resource/images/move_down.png" id=' + "_moveDown" + rowIndex +
                                             ' alt="下移" onclick="moveDown(this,\'_moveUp\',\'_moveDown\')" style="width:16px;height: 16px;margin-left: 5px;cursor: pointer;">';
            }
        }
        //为tr添加验证机制
        dhtmlxValidation.addValidation(tr, [
            {target:"_colName" + rowIndex,rule:"NotEmpty,MaxLength[32],ValidAplhaNumeric,ValidByCallBack[columnNameCheck]"},
            {target:dataTypeCombo[rowIndex].DOMelem_input,rule:"NotEmpty,ValidByCallBack[dataTypeValidate]"},
            {target:"_colNameCn" + rowIndex,rule:"MaxLength[32]"},
            {target:"_colBusComment" + rowIndex,rule:"MaxLength[1000]"},
            {target:"_defaultVal" + rowIndex,rule:"MaxLength[64]"}
        ]);
    }
    return rowIndex;
}
/**
 * 删除一行
 * @param img
 */
var removeRow = function(img){
    //获取行得index
    var rowIndex = img.parentNode.parentNode._index;
    var trId = "_columnRow" + rowIndex;
    $(trId).parentNode.removeChild($(trId));
    dataTypeCombo[rowIndex] = null;
    delete dataTypeCombo[rowIndex];
}
/**
 * 将一行进行向上移动
 * @param img
 * @param moveUp 向上移动节点名称
 * @param moveDown 向下移动节点名称
 */
var moveUp = function(img,moveUp,moveDown){
    //获取其上一个TR节点
    var tr = img.parentNode.parentNode;
    if(tr.previousSibling.offsetHeight){
        tr.parentNode.insertBefore(tr, tr.previousSibling);
    }
}

var moveDown = function(img,moveUp,moveDown){
    //获取其上一个TR节点
    var tr = img.parentNode.parentNode;
    var tempNode = tr.nextSibling;
    if(tempNode.nextSibling){
        tr.parentNode.insertBefore(tr, tempNode.nextSibling);
    }
}

/**
 * 查询维度信息Action定义
 */
dwrCaller.addAutoAction("queryDimInfo", "TableApplyAction.queryDimInfo");
dwrCaller.addDataConverter("queryDimInfo", new dhtmxGridDataConverter({
    idColumnName:"tableId",
    filterColumns:["","tableName","tableNameCn","tableBusComment"],
    userData:function(rowIndex, rowData){
        return rowData;
    }
}))

/**
 * 查询维度归并类型Action定义
 */
dwrCaller.addAutoAction("queryDimType", "TableApplyAction.queryDimType");
dwrCaller.addDataConverter("queryDimType", new dhtmlxComboDataConverter({
    valueColumn:'dimTypeId',
    textColumn:"dimTypeName",
    userData:function(rowData){
        return rowData;
    }
}));
dwrCaller.isShowProcess("queryDimType", false);
/**
 * 查询维度层级Action定义
 */
dwrCaller.addAutoAction("queryDimLevels", "TableApplyAction.queryDimLevels");
dwrCaller.addDataConverter("queryDimLevels", new dhtmlxComboDataConverter({
    valueColumn:'dimLevel',
    textColumn:"dimLevelName",
    textFormat:function(rowData, value, text){
        return value + "--" + text;
    },
    userData:function(rowData){
        return rowData;
    }
}));
dwrCaller.isShowProcess("queryDimLevels", false);
/**
 * 查询维度表列Action定义
 */
dwrCaller.addAutoAction("queryDimCols", "TableApplyAction.queryDimCols");
dwrCaller.addDataConverter("queryDimCols", new dhtmlxComboDataConverter({
    valueColumn:'colId',
    textColumn:"colName"
}));
dwrCaller.isShowProcess("queryDimCols", false);

var dimWindow = null;
/**
 * 选择维度信息。
 * @param input
 */
var selectDim = function(input){
    var rowIndex = input.parentNode.parentNode._index;
    //参数收集，参数来源于查询表单_queryDimForm
    var param = new biDwrMethodParam([
        {
            index:0,type: "ui",value:"_queryDimForm"
        }
    ]);
    //查询。
    var queryDimInfo = function(){
        queryDimGrid.clearAll();
        queryDimGrid.load(dwrCaller.queryDimInfo + param, "json");
        $("_refColumn").innerHTML = "";
        $("_dimType").innerHTML = "";
        $("_selectLevel").innerHTML = "";
        dhtmlxValidation.clearAllTip();
    }
    if(!dimWindow){
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("dimWindow", 0, 0, 450, 400);
        dimWindow = dhxWindow.window("dimWindow");
        dimWindow.stick();
        dimWindow.setDimension(550, 490);
        dimWindow.button("minmax1").hide();
        dimWindow.button("park").hide();
        dimWindow.button("stick").hide();
        dimWindow.button("sticked").hide();
        dimWindow.center();
        dimWindow.denyResize();
        dimWindow.denyPark();
        dimWindow.setText("选择维表");
        dimWindow.keepInViewport(true);
        dimWindow.show();
        dimWindow.attachEvent("onClose", function(){
            dimWindow.hide();
            dimWindow.setModal(false);
            return false;
        })
        //添加查询内容
        var dimLayout = new dhtmlXLayoutObject(dimWindow, "2E");
        dimLayout.cells("b").setText("设置维表关联属性");
        dimLayout.cells("a").setHeight(300);
        dimLayout.cells("a").fixSize(false, true);
        dimLayout.hideConcentrate();
        dimLayout.cells("a").firstChild.style.height = (dimLayout.cells("a").firstChild.offsetHeight + 5) + "px";
        dimLayout.cells("a").hideHeader();
        dimLayout.hideSpliter();//移除分界边框。
        //添加查询按钮
        var queryButton = Tools.getButtonNode("查询");
        //为button绑定事件
        queryButton.onclick = queryDimInfo;
        dimLayout.cells("a").attachObject("_dimInfoTable");
        //表分类下拉树数据准备。
        var dimGroupTree = new loadTableGroupTree($("_queryDimName"), $("_queryDimId"));
        //为_queryDimId添加事件
        Tools.addEvent($("_queryDimName"), "click", function(){
            dimGroupTree.treeDiv.style.width = $("_queryDimName").offsetWidth + 'px';
            Tools.divPromptCtrl(dimGroupTree.treeDiv, $("_queryDimName"), true);
            dimGroupTree.treeDiv.style.display = "block";
            dimGroupTree.showGroupTree(2);
        });

        $("_queryDimButton").appendChild(queryButton);
        //生成查询grid;
        queryDimGrid = new dhtmlXGridObject("_queryDimGrid");
        queryDimGrid.setHeader(",表名,中文名,注释");
        queryDimGrid.setInitWidthsP("6,30,30,34");
        queryDimGrid.setColAlign("center,center,center,center");
        queryDimGrid.setHeaderAlign("center,center,center,center");
        queryDimGrid.setColTypes("ra,ro,ro,ro");
        queryDimGrid.setColSorting("na,na,na,na");
        queryDimGrid.setEditable(true);
        queryDimGrid.enableMultiselect(false);
        queryDimGrid.enableCtrlC();
        queryDimGrid.init();
        queryDimGrid.defaultPaging(10);
        //添加radio点击事件。
        queryDimGrid.attachEvent("onCheck", function(rId, cInd, state){
            if(state){
                //先清空关联字段、归并类型、维层级下拉框数据。
                $("_refColumn").innerHTML = "";
                $("_dimType").innerHTML = "";
                $("_selectLevel").innerHTML = "";
                var colId=$("_dimColId" + dimWindow._rowIndex).value ;
                var dimType=  $("_dimTypeId" + dimWindow._rowIndex).value;
                //组装关联字段数据。
                dwrCaller.executeAction("queryDimCols", rId,queryDimGrid.getUserData(rId,"tableVersion"), function(data){
                    Tools.addOption("_refColumn", data);
                    if(colId){
                        dwr.util.setValue("_refColumn",colId);
                    }
                });
                //加载维度归并类型数据
                dwrCaller.executeAction("queryDimType", rId,  function(data){
                    Tools.addOption("_dimType", data);
                    if(dimType){
                        dwr.util.setValue("_dimType",dimType);
                    }
                    $("_dimType").onchange.apply(window); //触发onchange事件
                })
            }
        });
        queryDimGrid.attachEvent("onXLE",function(){
            var dimTableId=dwr.util.getValue("_dimTableId" + dimWindow._rowIndex);
            if(dimTableId){
                if(queryDimGrid.doesRowExist(dimTableId)){
                    queryDimGrid.cells(dimTableId,0).changeState(true);
                }
            }
        });
        //设置维表关联属性。
        dimLayout.cells("b").attachObject("_dimRefAttrTable");

        //设置确定和关闭按钮。
        var ok = Tools.getButtonNode("确定");
        ok.style.marginLeft = "180px";
        ok.style.marginTop = "10px";
        ok.style.cssFloat="left";
        ok.style.styleFloat="left";
        //确定按钮事件
        ok.onclick = function(){
            if(dhtmlxValidation.validate("_dimRefAttrForm")){
                var rowData = queryDimGrid.getRowData(queryDimGrid.getCheckedRows(0).split(",")[0]);
                var rowIndex = dimWindow._rowIndex;
                //依次设置选择的值
                $("_colBusType" + rowIndex).value = 0;
                $("_dimLevel" + rowIndex).value = dwr.util.getValue("_selectLevel");
                $("_dimColId" + rowIndex).value = dwr.util.getValue("_refColumn");
                $("_dimTableId" + rowIndex).value = rowData.id;
                $("_dimTypeId" + rowIndex).value = dwr.util.getValue("_dimType");
                //设置Input框的值。
                $("_dimInfo" + rowIndex).value = rowData.data[1] + "," + dwr.util.getText("_refColumn") + "," +
                                                 dwr.util.getText("_dimType") + "," + dwr.util.getText("_selectLevel")
                dimWindow.close();
            }
        }
        var close = Tools.getButtonNode("关闭");
        close.style.styleFloat = "left";
        close.style.cssFloat = "left";
        close.style.marginTop = "10px";
        //添加close事件
        close.onclick = function(){
            dimWindow.close();
            dhtmlxValidation.clearAllTip();
        }
        //维度归并类型下拉框选择事件响应。
        $("_dimType").onchange = function(){
            var level=$("_dimLevel" + dimWindow._rowIndex).value;
            $("_selectLevel").innerHTML = "";
            var rowData = queryDimGrid.getRowData(queryDimGrid.getCheckedRows(0).split(",")[0]);
            //查询出维层级。
            dwrCaller.executeAction("queryDimLevels", rowData.id,
                    dwr.util.getValue("_dimType"), function(data){
                        Tools.addOption("_selectLevel", data);
                        if(level){
                            dwr.util.setValue("_selectLevel",level);
                        }
                    })
        }
        $("_dimInfoWindowButton").appendChild(ok);
        $("_dimInfoWindowButton").appendChild(close);
        //添加验证
        dhtmlxValidation.addValidation("_dimRefAttrForm", [
            {target:"_refColumn",rule:"NotEmpty"},
            {target:"_dimType",rule:"NotEmpty"},
            {target:"_selectLevel",rule:"NotEmpty" }
        ]);
    }
    queryDimInfo();
    dimWindow.setModal(true);
    dimWindow._rowIndex = rowIndex;
    dimWindow.show();
}

var dimColumnNum=0;
var maxCode=1;
/**
 * 添加维度表列信息。
 * @param rowIndex
 */
var addDimColumnRow=function(rowIndex){
    rowIndex=(rowIndex==undefined||rowIndex==null)?(++dimColumnNum):rowIndex;
    if(!$("_dimColumnRow"+rowIndex)){
        if(dimColumnNum<rowIndex){
            dimColumnNum=rowIndex;
        }
        var tr = document.createElement("tr");
        tr._index = rowIndex;
        tr.id="_dimColumnRow"+rowIndex;
        $("_clumDimContentBody").appendChild(tr);

        //拥有的归并类型名称列
        var td = tr.insertCell(0);
        td.align = "center";
        var input = dhx.html.create("input",{className:"dhxlist_txt_textarea",name:"dimTypeName",id:"_dimTypeName" + rowIndex,type:"TEXT",style:"width: 90%;"});
        //添加事件，开始输入时，新增一行。
        Tools.addEvent(input, "keyup", function(e){
            var target=(e||window.event).target||(e||window.event).srcElement;
            var thisTr = target.parentNode.parentNode;
            //新增空白行。
            if(!thisTr.nextSibling){
                addDimColumnRow(parseInt(thisTr._index) + 1);
            }
        });
        td.appendChild(input);
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"dimTypeId",id:"_dimTypeIdTypeGrid" + rowIndex}));//归并类型ID隐藏域。
        //编码*列
        td = tr.insertCell(1);
        td.align = "center";
        input = dhx.html.create("input",{name:"dimTypeCode",id:"_dimTypeCode" + rowIndex, type:"TEXT", style:"width: 90%;",disabled:true,value:maxCode++});
        td.appendChild(input);

        //描述列
        td = tr.insertCell(2);
        td.align = "center";
        input = dhx.html.create("input",{className:"dhxlist_txt_textarea",name:"dimTypeDesc",id:"_dimTypeDesc" + rowIndex,type:"TEXT",style:"width: 90%;"});
        td.appendChild(input);

        //层级列
        td = tr.insertCell(3);
        td.align = "center";
        td.innerHTML='<input type="text" id="_level'+rowIndex+'" name="level" class="dhxlist_txt_textarea" readOnly="true" style="width:90%" onclick="selectLevel(this)">';
//        td = tr.insertCell(4);
//        td.align = "center";
//        input = dhx.html.create("input",
//                {name:"lastLevelFlag",id:"_lastLevelFlag" + rowIndex,type:"checkbox",style:"width: 90%;"});
//        td.appendChild(input);
//        td.innerHTML+='<img src="../../../resource/images/more.png" alt="更多" onclick="selectLevel(this)">';
        //末级是否映射列
        //操作列
        td = tr.insertCell(4);
        td.align = "center";
        td.innerHTML="&nbsp;";
        var lastTr=Tools.findPreviousSibling(tr);
        //前一个节点可以编辑了。
        if(lastTr&&lastTr._index){
            lastTr.lastChild
                    .innerHTML = '<img src="../../../resource/images/cancel.png" alt="删除" onclick="removeDimRow(this)" style="width:16px;height: 16px;cursor: pointer">';

        }
        dhtmlxValidation.addValidation(tr, [
            {target:"_dimTypeName" + rowIndex ,rule:"NotEmpty,MaxLength[20]"},
            {target:"_dimTypeDesc" + rowIndex ,rule:"MaxLength[1000]"},
            {target:"_level" + rowIndex ,rule:"NotEmpty"}
        ]);
    }
}

/**
 * 表格2删除一行
 * @param img
 */
var removeDimRow=function(img){
    //获取行得index
    var rowIndex=img.parentNode.parentNode._index;
    var trId="_dimColumnRow"+rowIndex;
    $(trId).parentNode.removeChild( $(trId));
    //重新计算归并编码。
    var tableObj = $("_clumnDimContentTable");
    for(var i = 1; i < tableObj.rows.length ; i++){
        dwr.util.setValue("_dimTypeCode"+tableObj.rows[i]._index,i);
    }
    maxCode=tableObj.rows.length;
}

var importWindow=null;
/**
 * 查询业务表类信息Action定义
 */
dwrCaller.addAutoAction("queryBusinessTables","TableApplyAction.queryBusinessTables");
dwrCaller.addDataConverter("queryBusinessTables", new dhtmxGridDataConverter({
    idColumnName:"tableId",
    filterColumns:["","tableName","tableNameCn","tableBusComment"],
    userData:function(rowIndex, rowData){
        return rowData;
    }
}));
/**
 * 查询列信息Action定义
 */
dwrCaller.addAutoAction("queryColumnsByTableID","TableApplyAction.queryColumnsByTableID");
dwrCaller.addDataConverter("queryColumnsByTableID", new dhtmxGridDataConverter({
    idColumnName:"colId",
    filterColumns:["","colName","colDatatype","colNullabled","isPrimary","defaultVal","colBusType","colBusComment"],
    userData:function(rowIndex, rowData){
        return rowData;
    }
}));

//地域维度层级定义窗口
//以MAP形式表示，每一行对应一个KEY:"levelWindow"+rowIndex，关闭的时候只是将"levelWindow"+rowIndex隐藏
var levelWindow={};
var selectLevelData={}; //选择了的维度层级数据，以选择的维度类型行号作为键值。每个行号对应一个数组，
//保存对应层级信息，其格式大致如下:
//{1：[{dimLevel:1,dimLevelName:'行政'}]}
/**
 * 选择设置维层级
 * @param input
 */
var selectLevel=function(input){
    var thisTr=input.parentNode.parentNode;
    var rowIndex=thisTr._index;
    var levelNameTd=$("_dimTypeName"+rowIndex);
    var levelName=levelNameTd.value;
    if(!levelWindow["levelWindow"+rowIndex]){
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("levelWindow"+rowIndex, 0, 0, 400, 200);
        levelWindow["levelWindow"+rowIndex] = dhxWindow.window("levelWindow"+rowIndex);
        levelWindow["levelWindow"+rowIndex].stick();
        levelWindow["levelWindow"+rowIndex].setDimension(400, 400);
        levelWindow["levelWindow"+rowIndex].button("minmax1").hide();
        levelWindow["levelWindow"+rowIndex].button("park").hide();
        levelWindow["levelWindow"+rowIndex].button("stick").hide();
        levelWindow["levelWindow"+rowIndex].button("sticked").hide();
        levelWindow["levelWindow"+rowIndex].center();
        levelWindow["levelWindow"+rowIndex].denyResize();
        levelWindow["levelWindow"+rowIndex].denyPark();
        levelWindow["levelWindow"+rowIndex].setText("地域维度层级定义");
        levelWindow["levelWindow"+rowIndex].keepInViewport(true);
        levelWindow["levelWindow"+rowIndex].setModal(true);
        levelWindow["levelWindow"+rowIndex].attachEvent("onClose",function(){
            levelWindow["levelWindow"+rowIndex].hide();
            levelWindow["levelWindow"+rowIndex].setModal(false);
            return false;
        });
        var newDiv = document.createElement("div");
        newDiv.style.cssText="height:100%;width:100%";
        newDiv.innerHTML=$("levelContext").innerHTML
                .replace(/\{index\}/g,rowIndex);
        for(var i = 0; i < newDiv.childNodes.length; i++){
            if(newDiv.childNodes[i].nodeName.toLowerCase()=="form"){
                levelWindow["levelWindow"+rowIndex].attachObject(newDiv.childNodes[i]);
                break;
            }
        }
        //加入按钮。
        var ok = Tools.getButtonNode("确定");
        ok.style.marginLeft = "100px";
        ok.style.marginTop = "5px";
        ok.style.cssFloat="left";
        ok.style.styleFloat="left";
        ok.onclick=function(){
            var data=[];
            var tableObj = $("_levelContentTable_"+rowIndex);
            var str="";
            var validateRes=true;
            if(tableObj.rows.length==2){
                dhx.alert("您至少需要填写一个维度层级!");
                return;
            }
            for(var i = 1; i < tableObj.rows.length-1 ; i++){
                data.push({dimLevel:dwr.util.getValue("_levelCode"+tableObj.rows[i]._index+rowIndex),
                    dimLevelName:dwr.util.getValue("_levelName"+tableObj.rows[i]._index+rowIndex)})
                str+=dwr.util.getValue("_levelCode"+tableObj.rows[i]._index+rowIndex)+"--"
                        +dwr.util.getValue("_levelName"+tableObj.rows[i]._index+rowIndex);
                if(i!=tableObj.rows.length-2){
                    str+=",";
                }
                validateRes=dhtmlxValidation.validate(tableObj.rows[i])&&validateRes;
            }
            if(validateRes){
                selectLevelData[rowIndex]=data;
                dwr.util.setValue($("_level"+rowIndex),str);
                levelWindow["levelWindow"+rowIndex].close();
            }
        }
        $("_levelWindowButton_"+rowIndex).appendChild(ok);
        var cancel=Tools.getButtonNode("关闭");
        cancel.style.styleFloat = "left";
        cancel.style.cssFloat = "left";
        cancel.style.marginTop = "5px";
        cancel.onclick=function(){
            levelWindow["levelWindow"+rowIndex].close();
        }
        $("_levelWindowButton_"+rowIndex).appendChild(cancel);
        //如果初始有数据，新增层级信息。
        if(selectLevelData[rowIndex]){
            for(var j=0;j<selectLevelData[rowIndex].length;j++){
                addLevelRow(null,rowIndex);
                dwr.util.setValue("_levelName"+levelRow[rowIndex].maxIndex+rowIndex,selectLevelData[rowIndex][j].dimLevelName)
            }
            addLevelRow(null,rowIndex);
        }
    }
    levelWindow["levelWindow"+rowIndex].show();
    $("_dimType_"+rowIndex).innerText =levelName;
    dhx.html.addCss($("_LevelTable_"+rowIndex), global.css.gridTopDiv);

    if(!levelRow[rowIndex]||(levelRow[rowIndex]==0)){
        levelRow[rowIndex]=0;
        addLevelRow(null,rowIndex);
    }
}
var levelRow = {};//地域维度层级定义弹出表列 ,包含maxIndex,maxLevel键值


/**
 * 地域维度层级定义表单中的添加行事件
 * @param rowIndex 当前行号
 * @param parRowIndex 主窗口中所点击的那行的行号
 */
var addLevelRow=function(rowIndex, parRowIndex){
    if(!levelRow[parRowIndex]){
        levelRow[parRowIndex]={};
        levelRow[parRowIndex].maxIndex=0;
        levelRow[parRowIndex].maxLevel=1;
    }
    rowIndex=(rowIndex==undefined||rowIndex==null)?(++levelRow[parRowIndex].maxIndex):rowIndex;
    if(!$("_levelRow_"+parRowIndex+"_"+rowIndex)){
        if(levelRow[parRowIndex].maxIndex<rowIndex){
            levelRow[parRowIndex].maxIndex=rowIndex;
        }
        var tr = document.createElement("tr");
        tr._index = rowIndex;
        tr.id="_levelRow_"+parRowIndex+"_"+rowIndex;
        $("_LevelContent_"+parRowIndex).appendChild(tr);

        //层级编码
        var td = tr.insertCell(0);
        td.align = "center";
        var input = dhx.html.create("input",{className:"dhxlist_txt_textarea",name:"levelCode",id:"_levelCode" + rowIndex+parRowIndex,type:"TEXT",style:"width: 90%;",
            value:levelRow[parRowIndex].maxLevel,disabled:true});
        levelRow[parRowIndex].maxLevel+=1;
        td.appendChild(input);

        //层级名称
        td = tr.insertCell(1);
        td.align = "center";
        var input = dhx.html.create("input",{className:"dhxlist_txt_textarea",name:"levelName",id:"_levelName" + rowIndex+parRowIndex,type:"TEXT",style:"width: 90%;"});
        //添加事件，开始输入时，新增一行。
        input.onkeyup=function(e){
            var target=(e||window.event).target||(e||window.event).srcElement;
            var thisTr=target.parentNode.parentNode;
            if(!thisTr.nextSibling){
                addLevelRow(parseInt(thisTr._index)+1, parRowIndex);
                thisTr.lastChild.innerHTML='<img src="../../../resource/images/cancel.png" alt="删除" onclick="removeLevelRow(this,'+parRowIndex+')">';
            }
        };
        td.appendChild(input);
        //操作列
        td = tr.insertCell(2);
        td.align = "center";
        td.innerHTML="&nbsp;";
        dhtmlxValidation.addValidation(tr, [
            {target:"_levelName" + rowIndex+parRowIndex ,rule:"NotEmpty,MaxLength[18]"}
        ]);
    }
}

/**
 * 删除维度层级一列
 * @param img
 */
var removeLevelRow=function(img, parRowIndex){
    //获取行得index
    var rowIndex=img.parentNode.parentNode._index;
    var trId="_levelRow_"+parRowIndex+"_"+rowIndex;
    $(trId).parentNode.removeChild( $(trId));
    levelRow[parRowIndex].maxIndex=levelRow[parRowIndex].maxIndex-1;
    //重新计算最大层级。
    var tableObj = $("_levelContentTable_"+parRowIndex);
    for(var i = 1; i < tableObj.rows.length ; i++){
        dwr.util.setValue("_levelCode"+tableObj.rows[i]._index+parRowIndex,i);
    }
    levelRow[parRowIndex].maxLevel=tableObj.rows.length;
}

/**
 * 获取页面数据
 * @param isDim：是否收集维度信息
 */
var getData = function(isDim){
    var tableData = Tools.getFormValues("_baseInfoForm");
    //表状态属于修改状态。
    tableData.tableState =2 ;
    tableData.tableId=dimTableId;
    //表状态属于0
    tableData.tableVersion = 1;
    tableData.tableTypeId=2;//维度类型。
    if(document.getElementById("_lastLevelFlag").checked == false){
        tableData.lastLevelFlag = 0
    }
    else{
        tableData.lastLevelFlag = 1;
    }
//    tableData.lastLevelFlag= tableData.lastLevelFlag?1:0;
    //列字段数据
    var columnData = [];
    var tableObj = $("_clumnContentTable");
    //维度扩展表信息
    var metaDimTable={};
    for(var i = 1; i < tableObj.rows.length - 1; i++){
        var rowData = {};
        for(var j = 0; j < tableObj.rows[i].cells.length; j++){
            var elements = tableObj.rows[i].cells[j].getElementsByTagName("input");
            for(var z = 0; z < elements.length; z++){
                if([elements[z].name]){
                    rowData[elements[z].name] = dwr.util.getValue(elements[z]);
                }
            }
        }
        rowData.colOrder=i;//列顺序
        rowData.tableState=tableData.tableState;//
        rowData.tableVersion=tableData.tableVersion;
        //数据类型处理。
        var reg = /(\w+)(\((\d+)((,)(\-?\d+))?\))*/
        var match = reg.exec(dataTypeCombo[tableObj.rows[i]._index].getComboText());
        rowData.fullDataType=dataTypeCombo[tableObj.rows[i]._index].getComboText();
        rowData.colDatatype=match[1].toUpperCase();
        switch(match[1].toLowerCase()){
            case "varchar":
            case "varchar2":
            case "nvarchar":
            case "nvarchar2":
            case "raw":
            case "char":
            {
                rowData.colSize=match[3];
                break;
            }
            case "number":
            {
                rowData.colSize=match[3]?match[3]:"";
                rowData.colPrec=match[6]?match[6]:"";
                //如果长度为0，ORACLE默认数据长度为38,7
//                if(!rowData.colSize){
//                    rowData.colSize=38;
//                    rowData.colPrec=7;
//                }else if(!rowData.colPrec){
//                    rowData.colPrec=0;
//                }
                break;
            }
            case "date":{
                if(!rowData.colSize){
                    rowData.colSize=7;
                }
                break;
            }
            default:
                break;
        }        rowData.colNullabled=rowData.colNullabled?1:0;
        rowData.isPrimary=rowData.isPrimary?1:0;
        //列属性处理
        if(dwr.util.getValue("_columAttribute"+i)=='1'){ //子节点
            metaDimTable.dimKeyColId=rowData.colName;
        }else if(dwr.util.getValue("_columAttribute"+i)=='2'){ //父节点
            metaDimTable.dimParKeyColId=rowData.colName;
        }
        columnData.push(rowData);
    }
    var res = {};
    res.tableData = tableData;
    res.columnDatas = columnData;
    if(isDim){
        //维度扩展表信息
        metaDimTable.tableName=tableData.tableName;
        metaDimTable.dataSourceId=tableData.dataSourceId;
        metaDimTable.tableDimPrefix=tableData.tableDimPrefix;
        tableData.tableDimPrefix=null;
        delete tableData.tableDimPrefix;
        res.dimTableData=metaDimTable;
        //META_DIM_TYPE信息
        var dimTypeDatas=[];
        tableObj = $("_clumnDimContentTable");
        var dimMaxLevel=0;//维度最大层级。
        for(var i = 1; i < tableObj.rows.length - 1; i++){
            var rowData = {};
            for(var j = 0; j < tableObj.rows[i].cells.length; j++){
                var elements = tableObj.rows[i].cells[j].getElementsByTagName("input");
                for(var z = 0; z < elements.length; z++){
                    if([elements[z].name]){
                        rowData[elements[z].name] = dwr.util.getValue(elements[z]);
                    }
                }
            }
//            rowData.lastLevelFlag= rowData.lastLevelFlag?1:0;
            rowData.dimTypeState=1;//状态有效。
            delete rowData.level;
//            res.dimTypeDatas=dimTypeDatas;
            //层级信息META_DIM_LEVEL
            var dimLevelDatas=selectLevelData[tableObj.rows[i]._index];
            var dimLevels=[];
            dimMaxLevel=Math.max(dimMaxLevel,dimLevelDatas.length);
            for(var j=0;j<dimLevelDatas.length;j++){
                var level=new Object();
                level.dimLevel=dimLevelDatas[j].dimLevel;
                level.dimLevelName=dimLevelDatas[j].dimLevelName;
                dimLevels.push(level);
            }
            rowData.dimLevelDatas=dimLevels;
            dimTypeDatas.push(rowData);
        }
        //维度最大层级
        metaDimTable.tableDimLevel=dimMaxLevel;
        res.dimTypeDates=dimTypeDatas;

    }
    return res;
}

/**
 * 提交数据验证
 * @param validateDimInfo 是否验证维度列表信息。
 */
var validate = function(validateDimInfo){
    //提交数据前进行数据验证。
    var validateRes=true;
    validateRes=validateRes&&dhtmlxValidation.validate("_baseInfoForm");
    var tableObj = $("_clumnContentTable");
    if(tableObj.rows.length==2){
        dhx.alert("您至少要新增一个列配置");
        return false;
    }
    for(var i = 1; i < tableObj.rows.length - 1; i++){
        validateRes=dhtmlxValidation.validate(tableObj.rows[i])&&validateRes;
    }
    //验证维度信息
    if(validateDimInfo){
        tableObj = $("_clumnDimContentTable");
        if(tableObj.rows.length==2){
            dhx.alert("您至少要新增一个维度归并类型");
            return false;
        }
        for(var i = 1; i < tableObj.rows.length - 1; i++){
            validateRes=dhtmlxValidation.validate(tableObj.rows[i])&&validateRes;
        }
    }
    return validateRes;
}
//预览SQL Action定义
dwrCaller.addAutoAction("generateSql", "TableApplyAction.generateSql");
dwrCaller.isDealOneParam("validate",false);
var genSql=function(){
    if(validate()){
        var data= getData();
        dwrCaller.executeAction("generateSql",data,function(sql){
            var dhxWindow = new dhtmlXWindows();
            dhxWindow.createWindow("sqlWindow", 0, 0, 450, 400);
            var sqlWindow = dhxWindow.window("sqlWindow");
            sqlWindow.stick();
            sqlWindow.setModal(true);
            sqlWindow.setDimension(550, 490);
            sqlWindow.button("minmax1").hide();
            sqlWindow.button("park").hide();
            sqlWindow.button("stick").hide();
            sqlWindow.button("sticked").hide();
            sqlWindow.center();
            sqlWindow.denyResize();
            sqlWindow.denyPark();
            sqlWindow.setText("预览SQL");
            sqlWindow.keepInViewport(true);
            //建一个DIV
            var div=document.createElement("div");
            div.style.width="100%";
            div.style.height="100%";
            div.style.position="relative";
            div.style.overflow="auto";
            div.innerHTML=sql;
            sqlWindow.attachObject(div);
            sqlWindow.show();
            sqlWindow.attachEvent("onClose", function(){
                dhxWindow.unload();
                dhxWindow=null;
            })
        });
    }
}
var isValidateFalse=false;//是否验证失败
//提交验证Action定义
dwrCaller.addAutoAction("validate", "TableApplyAction.validate");
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
//数据保存action定义
dwrCaller.addAutoAction("updateDim",TableDimAction.updateDim);
dwrCaller.isDealOneParam("updateDim",false);
//数据申请action定义
dwrCaller.addAutoAction("insertDim",TableDimAction.insertDim);
dwrCaller.isDealOneParam("insertDim",false);
//修改维度表状态
dwrCaller.addAutoAction("updateDimTableState",TableDimAction.updataDimTableSateByTableIdAndVersion);
/**
 * 提交验证。
 */
var submitValidate=function(){
    if(validate(true)){
        isValidateFalse=false;
        var data= getData(true);
        dwrCaller.executeAction("validate",data,function(validateResult){
            var dhxWindow = new dhtmlXWindows();
            dhxWindow.createWindow("validateWindow", 0, 0, 450, 400);
            var validateWindow = dhxWindow.window("validateWindow");
            validateWindow.stick();
            validateWindow.setModal(true);
            validateWindow.setDimension(550, 490);
            validateWindow.button("minmax1").hide();
            validateWindow.button("park").hide();
            validateWindow.button("stick").hide();
            validateWindow.button("sticked").hide();
            validateWindow.center();
            validateWindow.denyResize();
            validateWindow.denyPark();
            validateWindow.setText("辅助检查结果");
            validateWindow.keepInViewport(true);
            //添加查询内容
            var validateLayout = new dhtmlXLayoutObject(validateWindow, "2E");
            validateLayout.cells("a").setHeight(450);
            validateLayout.cells("a").fixSize(false, true);
            validateLayout.cells("a").firstChild.style.height = (validateLayout.cells("a").firstChild.offsetHeight + 5) + "px";
            validateLayout.cells("a").hideHeader();
            validateLayout.cells("b").hideHeader();
            var mygrid= validateLayout.cells("a").attachGrid();
            mygrid.setHeader("检查项,检查值,说明,检查结果");
            mygrid.setInitWidthsP("15,50,20,15");
            mygrid.setColAlign("center,center,center,center");
            mygrid.setHeaderAlign("center,center,center,center");
            mygrid.setColTypes("ro,ro,ro,img");
            mygrid.setColSorting("na,na,na,na");
            mygrid.setEditable(false);
            mygrid.enableCtrlC();
            mygrid.enableMultiline(true);
            mygrid.init();
            mygrid.parse(validateResult,"json");
            mygrid.setRowTextStyle("1", "height:90px");
            validateLayout.hideConcentrate();
            validateLayout.hideSpliter();//移除分界边框。
            validateWindow.show();
            validateWindow.attachEvent("onClose", function(){
                dhxWindow.unload();
                dhxWindow=null;
            })
            //新建按钮。
            var div=document.createElement("div");
            div.style.width="100%";
            div.style.height="100%";
            div.style.position="relative";
            //获取保存按钮
            var save=Tools.getButtonNode("保存");
            div.appendChild(save);
            save.style.marginLeft = "210px";
            save.style.styleFloat = "left";
            save.style.cssFloat = "left";
            save.style.marginTop = "10px";
            save.onclick=function(){
                var insertTable=function(){
                	if(myApp == 1){
                		dwrCaller.executeAction("insertDim",data,function(rs){
                        if(rs){
                            dhx.alert("保存成功！");
                            validateWindow.close();
                            removeAllColumnData();
                            clearConflationData();
                            initCol();
                        }else{
                            dhx.alert("保存失败，请重试！");
                        }
                      });
                   }else{
                	  dwrCaller.executeAction("updateDim",data,function(rs){
                        if(rs){
                            dhx.alert("修改成功！");
                            validateWindow.close();
                            dwrCaller.executeAction("updateDimTableState",data)
                        }else{
                            dhx.alert("保存失败，请重试！");
                        }
                    });
                   }
                    
                }
                if(isValidateFalse){
                    dhx.confirm("您有验证项未通过，是否继续保存？",function(r){
                        if(r){
                            insertTable();
                        }
                    })
                }else{
                    insertTable();
                }
            }
            var close = Tools.getButtonNode("关闭");
            close.style.marginLeft = "10px";
            close.style.marginTop = "10px";
            close.style.styleFloat = "left";
            close.style.cssFloat = "left";
            //添加close事件
            close.onclick = function(){
                validateWindow.close();
            }
            div.appendChild(close);
            validateLayout.cells("b").attachObject(div);
        });
    }
}

//重置按钮
var resetFormData = function(){
    //清空表单基本信息表单
    document.getElementById("_baseInfoForm").reset();
    //清空列信息
    var tableObj = $("_clumnContentBody");
    for(var rowIndex = 1; rowIndex < tableObj.rows.length - 1; rowIndex++){
        var row=  tableObj.rows[rowIndex--];
        row.parentNode.removeChild(row);
    }
    $("_dataSource").innerHTML="";
    //重新加载基本信息
    clearConflationData();
    initInfo();
}

var clearConflationData = function(){
    var tableObj = $("_clumDimContentBody");
    for(var rowIndex = 1; rowIndex < tableObj.rows.length - 1; rowIndex++){
        var row=  tableObj.rows[rowIndex--];
        row.parentNode.removeChild(row);
    }
    //清空后重新赋值编码
    for(var i = 1; i < tableObj.rows.length ; i++){
        dwr.util.setValue("_dimTypeCode"+tableObj.rows[i]._index,i);
    }
}
/**
 * 从元数据表类复制配置
 */
var importFormTable=function(){
    var queryTableGrid=null;
    //参数收集，参数来源于查询表单_queryDimForm
    var param = new biDwrMethodParam([
        {
            index:0,type: "ui",value:"_queryTables"
        }
    ]);
    //查询。
    var queryTables = function(){
        queryTableGrid.clearAll();
        queryColumnGrid.clearAll();
        queryTableGrid.load(dwrCaller.queryBusinessTables + param, "json");
        dhtmlxValidation.clearAllTip();
    }
    if(!importWindow){
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("importWindow", 0, 0, 450, 400);
        var winsize=Tools.propWidthDycSize(8,24,8,20);
        importWindow = dhxWindow.window("importWindow");
        importWindow.stick();
        importWindow.setModal(true);
        importWindow.setDimension(winsize.width, winsize.height);
        importWindow.button("minmax1").hide();
        importWindow.button("park").hide();
        importWindow.button("stick").hide();
        importWindow.button("sticked").hide();
        importWindow.center();
        importWindow.denyResize();
        importWindow.denyPark();
        importWindow.setText("导入表类信息");
        importWindow.keepInViewport(true);
        //添加查询内容
        var layout = new dhtmlXLayoutObject(importWindow, "2E");
        layout.cells("a").setHeight(Math.ceil(winsize.height/7)*3);
        layout.cells("a").fixSize(false, true);
        layout.cells("a").firstChild.style.height = (layout.cells("a").firstChild.offsetHeight + 5) + "px";
        layout.cells("a").hideHeader();
        layout.cells("b").setText("表字段信息");
        layout.cells("a").attachObject("_importFromTable");
        //添加查询按钮，
        var queryButton = Tools.getButtonNode("查询");
        queryButton.onclick=queryTables;
        $("_tableKeyWord").onkeypress=function(e){
            if((e||window.event).keyCode==13){
                queryTables();
            }
        }
        $("_queryTableButton").appendChild(queryButton);
        //业务类型下拉树数据准备。
        var tableGroupTree = new loadTableGroupTree2($("_queryTableGroup"), $("_queryTableGroupId"));
        //为_tableGroup添加事件
        Tools.addEvent($("_queryTableGroup"), "click", function(){
            var tableType =2;//维度表
            tableGroupTree.treeDiv.style.width = $("_queryTableGroup").offsetWidth + 'px';
            Tools.divPromptCtrl(tableGroupTree.treeDiv, $("_queryTableGroup"), true);
            tableGroupTree.treeDiv.style.display = "block";
            treeData = {};
            tableGroupTree.showGroupTree(tableType);
        });

        queryTableGrid = new dhtmlXGridObject("_queryTableGrid");
        queryTableGrid.setHeader(",表名,中文名,注释");
        queryTableGrid.setInitWidthsP("6,30,30,34");
        queryTableGrid.setColAlign("center,left,left,left");
        queryTableGrid.setHeaderAlign("center,center,center,center");
        queryTableGrid.setColTypes("ra,ro,ro,ro");
        queryTableGrid.setColSorting("na,na,na,na");
        queryTableGrid.setEditable(true);
        queryTableGrid.enableMultiselect(false);
        queryTableGrid.load(dwrCaller.queryBusinessTables + param,"json");
        queryTableGrid.defaultPaging(7);
        queryTableGrid.enableCtrlC();
        queryTableGrid.init();

        //表字段关联属性设置
        var div=document.createElement("div");
        div.innerHTML='<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;position: relative;">' +
                      '<tr style="height: 85%"><td style="width: 100%;height: 85%"><div id="_queryColumnGrid" style="width: 100%;height: 100%;position: relative;"></div><td></tr>' +
                      '<tr style="height: 15%"><td  id="_queryFromWinButton"></td></tr></table>';
        layout.cells("b").attachObject(div.lastChild);
        var queryColumnGrid = new dhtmlXGridObject("_queryColumnGrid");
        queryColumnGrid.setHeader("{#checkBox},名称,类型,可为空,主键,默认值,维度,注释");
        queryColumnGrid.setInitWidthsP("6,18,16,10,10,10,10,20");
        queryColumnGrid.setColAlign("center,left,center,center,center,center,center,left");
        queryColumnGrid.setHeaderAlign("center,center,center,center,center,center,center,center");
        queryColumnGrid.setColTypes("ch,ro,ro,ro,ro,ro,ro,ro");
        queryColumnGrid.setColSorting("na,na,na,na,na,na,na,na");
        queryColumnGrid.setEditable(true);
        queryColumnGrid.enableMultiselect(false);
        queryColumnGrid.setColumnCustFormat(3,yesOrNo);
        queryColumnGrid.setColumnCustFormat(4,yesOrNo);
        queryColumnGrid.setColumnCustFormat(6, function(value){
            return parseInt(value)==0?"是"
                    :(parseInt(value)==1?"否":value);
        })
//        queryColumnGrid.defaultPaging(5);
        queryColumnGrid.enableCtrlC();
        queryColumnGrid.init();
        (!dhx.env.isIE)&&(queryColumnGrid.entBox.style.marginTop="-20px");
        //设置确定和关闭按钮。
        var ok = Tools.getButtonNode("确定");
        ok.style.marginLeft = (winsize.width/2-100) +"px";
        (!dhx.env.isIE||parseInt(dhx.env.ie)>7)&&(ok.style.marginTop = "-10px");
        ok.style.cssFloat="left";
        ok.style.styleFloat="left";
        ok.onclick=function(){
            dhtmlxValidation.clearAllTip();
            var  selectTableId=queryTableGrid.getCheckedRows(0);
            if(!selectTableId){
                dhx.alert("您至少应该选择一个表类进行复制！");
                return ;
            }
            //获取table信息
            var tableData=queryTableGrid.getRowData(selectTableId).userdata;
            //获取列信息
            var selectedColumnIds=queryColumnGrid.getCheckedRows(0);
            var columnDatas=[];
            if(selectedColumnIds){
                var columns=selectedColumnIds.split(",");
                for(var i=0;i<columns.length;i++){
                    columnDatas.push(queryColumnGrid.getRowData(columns[i]).userdata);
                }
            }
            copyTablData(tableData,columnDatas);
            importWindow.close();
        }
        $("_queryFromWinButton").appendChild(ok);
        var cancel=Tools.getButtonNode("关闭");
        cancel.style.styleFloat = "left";
        cancel.style.cssFloat = "left";
        (!dhx.env.isIE||parseInt(dhx.env.ie)>7)&&(cancel.style.marginTop = "-10px");
        cancel.onclick=function(){
            importWindow.close();
        }
        $("_queryFromWinButton").appendChild(cancel);

        var radioCheck=function(rId){
            queryColumnGrid.clearAll();
            var rowData=queryTableGrid.getRowData(rId);
            dwrCaller.executeAction("queryColumnsByTableID", rId,rowData.userdata.tableVersion, function(data){
                queryColumnGrid.parse(data,"json");
                Tools.fireEvent($("_in_header_checkBox"),"click");
            });
        }
        //添加radio点击事件。查询其列字段
        queryTableGrid.attachEvent("onCheck", function(rId, cInd, state){
            if(state){
                radioCheck(rId);
            }
        });
        queryTableGrid.attachEvent("onRowSelect", function(id,ind){
            queryTableGrid.cells(id,0).setValue(1);
            radioCheck(id);
        })

        layout.hideConcentrate();
        layout.hideSpliter();//移除分界边框。
        importWindow.show();
        importWindow.attachEvent("onClose", function(){
            importWindow.hide();
            importWindow.setModal(false);
            return false;
        })
    }
    importWindow.setModal(true);
    importWindow.show();
}



/**
 * 查询用户信息Action定义
 */
dwrCaller.addAutoAction("queryDbUsers","TableApplyAction.queryDbUsers");
dwrCaller.isShowProcess("queryDbUsers",false);
/**
 * 查询数据库表Action定义
 */
dwrCaller.addAutoAction("queryDbTables","TableApplyAction.queryDbTables");
dwrCaller.addDataConverter("queryDbTables", new dhtmxGridDataConverter({
    idColumnName:"tableName",
    filterColumns:["","tableName","tableBusComment"],
    userData:function(rowIndex, rowData){
        rowData.dataSourceID=dwr.util.getValue("_tableDataSource");
        return rowData;
    }
}));
/**
 * 查询数据库表列信息Action定义
 */
dwrCaller.addAutoAction("queryDbTableColumns","TableApplyAction.queryDbTableColumns");
dwrCaller.addDataConverter("queryDbTableColumns", new dhtmxGridDataConverter({
    idColumnName:"colName",
    filterColumns:["","colName","colDatatype","colNullabled","isPrimary","defaultVal","colBusComment"],
    userData:function(rowIndex, rowData){
        rowData.colBusType=1;//非维度
        return rowData;
    }
}));
var importWindowFromDb=null;
/**
 * 从数据库中导入表信息。
 */
var importFromDatabase=function(){
    var queryTableGrid=null;
    //参数收集，参数来源于查询表单_queryDimForm
    var param = new biDwrMethodParam([
        {
            index:0,type: "ui",value:"_queryTablesFromDb"
        }
    ]);
    var userInit=false;//用户数据是否初始化完成。
    //查询。
    var queryTables = function(){
        if(userInit){
            queryTableGrid.clearAll();
            queryColumnGrid.clearAll();
            queryTableGrid.load(dwrCaller.queryDbTables + param, "json");
            dhtmlxValidation.clearAllTip();
        }else{
            setTimeout(arguments.callee,50);
        }
    }
    //查询用户信息
    var queryUsers=function(dataSourceId){
        dwrCaller.executeAction("queryDbUsers",dataSourceId,function(data){
            userInit=true;
            $("_owner").innerHTML="";
            var options=[];
            if(data){
                for(var i=0;i<data.length;i++){
                    options.push({text:data[i],value:data[i]});
                }
            }
            Tools.addOption($("_owner"),options);
        });
    }
    if(!importWindowFromDb){
        var dhxWindow = new dhtmlXWindows();
        var winsize=Tools.propWidthDycSize(8,24,8,20);
        dhxWindow.createWindow("importWindowFromDb", 0, 0, 450, 400);
        importWindowFromDb = dhxWindow.window("importWindowFromDb");
        importWindowFromDb.stick();
        importWindowFromDb.setModal(true);
        importWindowFromDb.setDimension(winsize.width, winsize.height);
        importWindowFromDb.button("minmax1").hide();
        importWindowFromDb.button("park").hide();
        importWindowFromDb.button("stick").hide();
        importWindowFromDb.button("sticked").hide();
        importWindowFromDb.center();
        importWindowFromDb.denyResize();
        importWindowFromDb.denyPark();
        importWindowFromDb.setText("导入数据库表信息");
        importWindowFromDb.keepInViewport(true);
        //添加查询内容
        var layout = new dhtmlXLayoutObject(importWindowFromDb, "2E");
        layout.cells("a").setHeight(Math.ceil(winsize.height/7)*3);
        layout.cells("a").fixSize(false, true);
        layout.cells("a").firstChild.style.height = (layout.cells("a").firstChild.offsetHeight + 5) + "px";
        layout.cells("a").hideHeader();
        layout.cells("b").setText("表字段信息");
        layout.cells("b").fixSize(true, true);
        layout.cells("a").attachObject("_importFromDb");
        //添加查询按钮，
        var queryButton = Tools.getButtonNode("查询");
        queryButton.onclick=queryTables;
        $("_tableKeyWordFromDb").onkeypress=function(e){
            if((e||window.event).keyCode==13){
                queryTables();
            }
        }
        $("_queryTableButtonFromDb").appendChild(queryButton);
        //数据源数据装载
        Tools.addOption("_tableDataSource",dataSourceData);
        queryUsers(dwr.util.getValue($("_tableDataSource")));
        $("_tableDataSource").onchange=function(){
            queryUsers(dwr.util.getValue($("_tableDataSource")));
        }

        queryTableGrid = new dhtmlXGridObject("_queryTableGridFromDb");
        queryTableGrid.setHeader(",表名,注释");
        queryTableGrid.setInitWidthsP("6,45,49");
        queryTableGrid.setColAlign("center,left,left");
        queryTableGrid.setHeaderAlign("center,center,center");
        queryTableGrid.setColTypes("ra,ro,ro");
        queryTableGrid.setColSorting("na,na,na");
        queryTableGrid.setEditable(true);
        queryTableGrid.enableMultiselect(false);
        queryTableGrid.defaultPaging(7);
        queryTables();
        queryTableGrid.enableCtrlC();
        queryTableGrid.init();

        //表字段关联属性设置
        var div=document.createElement("div");

        div.innerHTML='<table cellpadding="0" cellspacing="0" style="width:100%;height: 100%;position: relative;">' +
                      '<tr style="height: 85%"><td style="width: 100%;height: 85%"><div id="_queryColumnGridFromDb" style="width: 100%;height: 100%;position: relative;"></div><td></tr>' +
                      '<tr style="height: 15%"><td  id="_queryFromWinButtonFromDb"></td></tr></table>';
        layout.cells("b").attachObject(div.lastChild);
        var queryColumnGrid = new dhtmlXGridObject("_queryColumnGridFromDb");
        queryColumnGrid.setHeader("{#checkBox},名称,类型,可为空,主键,默认值,注释");
        queryColumnGrid.setInitWidthsP("6,23,16,10,10,10,25");
        queryColumnGrid.setColAlign("center,left,center,center,center,center,left");
        queryColumnGrid.setHeaderAlign("center,center,center,center,center,center,center");
        queryColumnGrid.setColTypes("ch,ro,ro,ro,ro,ro,ro");
        queryColumnGrid.setColSorting("na,na,na,na,na,na,na");
        queryColumnGrid.setEditable(true);
        queryColumnGrid.enableMultiselect(false);
        queryColumnGrid.setColumnCustFormat(3,yesOrNo);
        queryColumnGrid.setColumnCustFormat(4,yesOrNo);
//        queryColumnGrid.defaultPaging(5);
        queryColumnGrid.enableCtrlC();
        queryColumnGrid.init();
        (!dhx.env.isIE)&&(queryColumnGrid.entBox.style.marginTop="-15px");

        //radio选中事件
        var radioCheck=function(rId){
            queryColumnGrid.clearAll();
            var rowData=queryTableGrid.getRowData(rId);
            dwrCaller.executeAction("queryDbTableColumns",rId, rowData.userdata.tableOwner,parseInt(rowData.userdata.dataSourceID),function(data){
                queryColumnGrid.parse(data,"json");
                Tools.fireEvent($("_in_header_checkBox"),"click");
            });
        }
        //添加radio点击事件。查询其列字段
        queryTableGrid.attachEvent("onCheck", function(rId, cInd, state){
            if(state){
                //查询对应表列数据
                radioCheck(rId);
            }
        });
        queryTableGrid.attachEvent("onRowSelect", function(id,ind){
            queryTableGrid.cells(id,0).setValue(1);
            radioCheck(id);
        })

        //设置确定和关闭按钮。
        var ok = Tools.getButtonNode("确定");
        ok.style.marginLeft = (winsize.width/2-100) +"px";
        (!dhx.env.isIE||parseInt(dhx.env.ie)>7)&&(ok.style.marginTop = "-10px");
        ok.style.cssFloat="left";
        ok.style.styleFloat="left";
        ok.onclick=function(){
            dhtmlxValidation.clearAllTip();
            var  selectTableId=queryTableGrid.getCheckedRows(0);
            if(!selectTableId){
                dhx.alert("您至少应该选择一个表进行导入！");
                return ;
            }
            //获取table信息
            var tableData=queryTableGrid.getRowData(selectTableId).userdata;
            //获取列信息
            var selectedColumnIds=queryColumnGrid.getCheckedRows(0);
            var columnDatas=[];
            if(selectedColumnIds){
                var columns=selectedColumnIds.split(",");
                for(var i=0;i<columns.length;i++){
                    columnDatas.push(queryColumnGrid.getRowData(columns[i]).userdata);
                }
            }
            copyTablData(tableData,columnDatas);
            importWindowFromDb.close();
        }
        $("_queryFromWinButtonFromDb").appendChild(ok);
        var cancel=Tools.getButtonNode("关闭");
        cancel.style.styleFloat = "left";
        cancel.style.cssFloat = "left";
        (!dhx.env.isIE||parseInt(dhx.env.ie)>7)&&(cancel.style.marginTop = "-10px");
        cancel.onclick=function(){
            importWindowFromDb.close();
        }
        $("_queryFromWinButtonFromDb").appendChild(cancel);

        layout.hideConcentrate();
        layout.hideSpliter();//移除分界边框。
        importWindowFromDb.show();
        importWindowFromDb.attachEvent("onClose", function(){
            importWindowFromDb.hide();
            importWindowFromDb.setModal(false);
            return false;
        })
    }
    importWindowFromDb.setModal(true);
    importWindowFromDb.show();
}
//根据数据源查询用户
var tableOwnerConverter = new dhtmlxComboDataConverter({
    valueColumn:"username",
    textColumn:"username"
})
dwrCaller.addAutoAction("getUserNameByDataSourceId", "TableApplyAction.getUserNameByDataSourceId");
dwrCaller.addDataConverter("getUserNameByDataSourceId", tableOwnerConverter);
dwrCaller.isShowProcess("getUserNameByDataSourceId", false);
//添加表空间下拉可选
var tableSpaceConverter = new dhtmlxComboDataConverter({
	valueColumn:"name",
    textColumn:"name"
});
dwrCaller.addAutoAction("queryTableSpace","TableApplyAction.queryTableSpaceByDataSourceId");
dwrCaller.addDataConverter("queryTableSpace",tableSpaceConverter);
/**
 * 复制表类和列信息到页面
 * @param tableData
 * @param columnDatas
 */
var copyTablData=function(tableData,columnDatas){
    if(tableData){
        tableData.tableName&&dwr.util.setValue($("_tableName"),tableData.tableName);
        tableData.tableNameCn&&dwr.util.setValue($("_tableNameCn"),tableData.tableNameCn);
//        tableData.tableTypeId>=0&&dwr.util.setValue($("_tableType"),2);
        tableData.tableGroupId>0&&dwr.util.setValue($("_tableGroupId"),tableData.tableGroupId);
        tableData.tableGroupName&&dwr.util.setValue($("_tableGroup"),tableData.tableGroupName);
//        tableData.tableSpace&&dwr.util.setValue($("_tableSpace"),tableData.tableSpace);
        tableData.partitionSql&&dwr.util.setValue($("_partitionSql"),tableData.partitionSql);
        tableData.tableBusComment&&dwr.util.setValue($("_tableBusComment"),tableData.tableBusComment);
        tableData.dataSourceId>=0&&dwr.util.setValue($("_dataSource"),tableData.dataSourceId);
//        tableData.tableOwner&&dwr.util.setValue($("_tableOwner"),tableData.tableOwner);
        tableData.tableDimPrefix&&dwr.util.setValue($("_tableDimPrefix"),tableData.tableDimPrefix);
        dwrCaller.executeAction("getUserNameByDataSourceId",tableData.dataSourceId,function(data){
            $("_tableOwner").innerHTML="";
            if(data){
                if(!(data.options.length==1 && !data.options[0].text)){
                    Tools.addOption("_tableOwner", data);
                }
            }
      
           tableData.tableOwner&&dwr.util.setValue($("_tableOwner"),tableData.tableOwner);
            dhx.env.isIE&&CollectGarbage();
        });
        dwrCaller.executeAction("queryTableSpace",tableData.dataSourceId,function(data){
          document.getElementById("_tableSpace").innerHTML="";
          if(data){
             if(!(data.options.length==1 && !data.options[0].text)){
                 Tools.addOption("_tableSpace", data);
             }
          }
           tableData.tableOwner&&dwr.util.setValue($("_tableSpace"),tableData.tableSpace);
            
            dhx.env.isIE&&CollectGarbage();
        });
        //设置末级选中
        if(tableData.lastLevelFlag == 0){
            document.getElementById("_lastLevelFlag").checked = false;
        }else if(tableData.lastLevelFlag == 1){
            document.getElementById("_lastLevelFlag").checked = true;
        }
    }
    if(columnDatas&&columnDatas.length>0){
        for(var i=0;i<columnDatas.length;i++){
            var columnData=columnDatas[i];
            //设置值。
            copyColumnData(columnData,columnNum);
            addColumnRow();
        }
    }
}


var dataSourceChange = function(obj){
	dwrCaller.executeAction("getUserNameByDataSourceId",obj.value,function(data){
		document.getElementById("_tableOwner").length = 0;
        if(data){
            if(!(data.options.length==1 && !data.options[0].text)){
                Tools.addOption("_tableOwner", data);
            }
        }
        dhx.env.isIE&&CollectGarbage();
    });
    dwrCaller.executeAction("queryTableSpace",obj.value,function(data){
        document.getElementById("_tableSpace").length = 0;
        if(data){
            if(!(data.options.length==1 && !data.options[0].text)){
                Tools.addOption("_tableSpace", data);
            }
        }
        dhx.env.isIE&&CollectGarbage();
    });
}





/**
 * 复制一行列数据
 * @param columnData
 */
var copyColumnData=function(data,rowIndex){
    //依次赋值。
    data.colId&&dwr.util.setValue("_colId" + rowIndex, data.colId);
    data.colName&&dwr.util.setValue("_colName" + rowIndex, data.colName);
    data.colNameCn&&dwr.util.setValue("_colNameCn" + rowIndex, data.colNameCn);
    (data.datatype||data.colDatatype)&&dataTypeCombo[rowIndex].setComboText(data.datatype||data.colDatatype);
    data.colNullabled!=null &&data.colNullabled!=undefined&&dwr.util.setValue("_colNullabled" + rowIndex, data.colNullabled);
    data.defaultVal!=null&&data.defaultVal!=undefined&&dwr.util.setValue("_defaultVal" + rowIndex, data.defaultVal);
    data.isPrimary!=null&&data.isPrimary!=undefined&&dwr.util.setValue("_isPrimary" + rowIndex, data.isPrimary);
    data.colBusComment&&dwr.util.setValue("_colBusComment" + rowIndex, data.colBusComment);
    data.colBusType!=null&&data.colBusType!=undefined&&dwr.util.setValue("_colBusType" + rowIndex, data.colBusType);
    data.dimLevel!=null&&data.dimLevel!=undefined&&dwr.util.setValue("_dimLevel" + rowIndex, data.dimLevel);
    data.dimColId!=null&&data.dimColId!=undefined&&dwr.util.setValue("_dimColId" + rowIndex, data.dimColId);
    data.dimTableId!=null&&data.dimTableId!=undefined&&dwr.util.setValue("_dimTableId" + rowIndex, data.dimTableId);
    data.dimTypeId!=null&&data.dimTypeId!=undefined&&dwr.util.setValue("_dimTypeId" + rowIndex, data.dimTypeId);
    data.columAttribute&&dwr.util.setValue("_columAttribute"+rowIndex,data.columAttribute);
    //如果是维度，还需设置维度的显示信息。
    if(data.colBusType == 0){
        dwr.util.setValue("_dimInfo" + rowIndex,
                data.dimTableName + "," + data.dimColName + "," + data.dimTypeName + "," + data.dimLevel);

    }
}
/**
 * 复制一行归并类型数据
 * @param data
 * @param rowIndex
 */
var copyDimTypeData=function(data,rowIndex){
    dwr.util.setValue("_dimTypeName" + rowIndex, data.dimTypeName);
    dwr.util.setValue("_dimTypeDesc"+rowIndex,data.dimTypeDesc);
    //查询出维层级。
    if(!selectLevelData[rowIndex]){
        dwrCaller.executeAction("queryDimLevels", data.dimTableId, data.dimTypeId, function(dimLevels){
            var str="";
            var dimLevelData=[];
            if(dimLevels){
                for(var i=0;i<dimLevels.options.length;i++){
                    dimLevelData.push({dimLevel:dimLevels.options[i].userdata.dimLevel,dimLevelName:dimLevels.options[i].userdata.dimLevelName});
                    str+=dimLevels.options[i].userdata.dimLevel+"--"+Tools.trim(dimLevels.options[i].userdata.dimLevelName);
                    if(i!=dimLevels.options.length-1){
                        str+=",";
                    }
                }
                selectLevelData[rowIndex]=dimLevelData;
                dwr.util.setValue("_level"+rowIndex,str);
            }
        })
    }
//    dwr.util.setValue("_lastLevelFlag"+rowIndex,data.lastLevelFlag)
    if(data.dimTypeId){
        dwr.util.setValue("_dimTypeIdTypeGrid"+rowIndex,data.dimTypeId);
    }
}
var unload =function(){
    dwrCaller.unload();
    dwrCaller=null;
    tblMacro=null;
    sysCodeSelectConverter=null;
    copyDimTypeData=null;
}
dhx.ready(dimApplyInit);
