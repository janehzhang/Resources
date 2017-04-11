/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        apply.js
 *Description：
 *       表类申请JS
 *Dependent：
 *       dhtmlx.js
 *Author:
 *        张伟
 *Finished：
 *       2011-10-25
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

    TableApplyAction.isExistsMatchTables(value,dataSourceId, tableOwner,{
        async:false,
        callback:function(data){
            if(data){
                res = "您输入的表名在数据库中已存在，请重新输入！";
            } else{
                res = true;
            }
        }
    })
    if(res==true){
        //TODO 如果类名相同需要判断相同数据源、相同用户下除开本ID的表类信息只有一条
        TableApplyAction.ifExistTableFromMyApply(value,tableOwner,dataSourceId,tableId,{
            async:false,
            callback:function(r){
                if(r){
                    res = "您输入的表名已经在审核中！";
                } else{
                    res = true;
                    }}
            });
        }
    dhx.env.isIE&&CollectGarbage();
    return res;
}
/**对新增表的表名进行大写处理*/
//var inputDataChange = function(inputData){
//	document.getElementById("_tableName").value = inputData.toUpperCase(); 
//}

var tableTypeData=null;

//表类数据源相关Action
var tableDataSourceConverter = new dhtmlxComboDataConverter({
    valueColumn:"dataSourceId",
    textColumn:"dataSourceName"
})
var dataSourceData=null;
dwrCaller.addAutoAction("queryTableDataSource", "TableApplyAction.queryTableDataSource");
dwrCaller.addDataConverter("queryTableDataSource", tableDataSourceConverter);

//表名输入跟随Action定义
dwrCaller.addAutoAction("matchTables", "TableApplyAction.matchTables")
dwrCaller.addDataConverter("matchTables", new dhtmlxComboDataConverter({
    valueColumn:"tableId",
    textColumn:"tableName" ,
    userData: function(rowData){
        return rowData;
    }
}));
dwrCaller.isShowProcess("matchTables", false);

var tableOwnerConverter = new dhtmlxComboDataConverter({
    valueColumn:"username",
    textColumn:"username"
})
dwrCaller.addAutoAction("getUserNameByDataSourceId", "TableApplyAction.getUserNameByDataSourceId");
dwrCaller.addDataConverter("getUserNameByDataSourceId", tableOwnerConverter);
dwrCaller.isShowProcess("getUserNameByDataSourceId", false);
var dataSourceChange = function(obj){
	dwrCaller.executeAction("getUserNameByDataSourceId",obj.value,function(data){
		document.getElementById("_tableOwner").innerHTML = "";
        Tools.addOption("_tableOwner", [{value:"",text:"--请选择--"}]);
        if(data){
            if(!(data.options.length==1 && !data.options[0].text)){
                Tools.addOption("_tableOwner", data);
            }
        }
        dhx.env.isIE&&CollectGarbage();
    });
   dwrCaller.executeAction("queryTableSpace",obj.value,function(data){
       document.getElementById("_tableSpace").innerHTML = "";
       Tools.addOption("_tableSpace", [{value:"",text:"--请选择--"}]);
	   if(data){
            if(!(data.options.length==1 && !data.options[0].text)){
                Tools.addOption("_tableSpace", data);
            }
        }
        dhx.env.isIE&&CollectGarbage();
    });
}
// 表空间数据源相关Action
var tableSpaceConverter = new dhtmlxComboDataConverter({
	valueColumn:"name",
    textColumn:"name"
});
dwrCaller.addAutoAction("queryTableSpace","TableApplyAction.queryTableSpaceByDataSourceId");
dwrCaller.addDataConverter("queryTableSpace",tableSpaceConverter);
dwrCaller.isShowProcess("queryTableSpace", false);

var tableName="";

/**
 * 初始化页面，布局，准备数据。
 */
var applyInit = function(){
    var applyLayout = new dhtmlXLayoutObject("main", "2E"); //建立一个布局,document.body表明这个布局附着在哪里,可以是层或者window等,2E为布局格式,详见dhtmlX文档
    applyLayout.cells("a").setText("申请业务表类");
    applyLayout.cells("a").setHeight(250);
    applyLayout.cells("a").fixSize(false, true);
    applyLayout.cells("a").attachObject($("_baseInfoForm"));
    applyLayout.cells("a").hideHeader();   //取消头部

    //设置需要动态读取与皮肤相关的CSS样式。
    dhx.html.addCss($("_tableBaseInfo"), global.css.formContentDiv);
    !dhx.env.isIE&&($("_tableBaseInfo").style.height="100%");
    applyLayout.hideConcentrate();//隐藏缩放按钮
//    applyLayout.cells("b").setText("编辑列属性");
    applyLayout.cells("b").attachObject($("_columnForm"));
    applyLayout.cells("b").hideHeader();
    dhx.html.addCss($("_columnTableDiv"), global.css.gridTopDiv);
    $("_clumnContentDiv").style.height = (document.body.offsetHeight - 340) + "px";
    $("_columnHeadTable").style.width=  ((dhx.env.isIE&&parseInt(dhx.env.ie)<=7)
            ?$("_clumnContentDiv").offsetWidth:$("_clumnContentDiv").clientWidth)+"px";
    //设置CSS
    applyLayout.hideSpliter();
    tableTypeData=getComboByRemoveValue("TABLE_TYPE",2);
    tableTypeData=tableTypeData.options||tableTypeData;
    Tools.addOption("_tableType", tableTypeData);
    // 定义
    var tmpType = null;
    Tools.addEvent($("_tableType"), "change", function(){
        var tableType = dwr.util.getValue("_tableType");
        if(tableType == 3){
        	var tableObj = $("_clumnContentTable");
			for(var rowIndex = 1; rowIndex < tableObj.rows.length - 1; rowIndex++){
			    var row=  tableObj.rows[rowIndex--];
			    row.parentNode.removeChild(row);
			}
        	initCol();
        	tmpType = 3;
        }else{
        	if(tmpType == 3) {
	        	var tableObj = $("_clumnContentTable");
        		for(var rowIndex = 1; rowIndex < tableObj.rows.length - 1; rowIndex++){
					var row = tableObj.rows[rowIndex--];
					row.parentNode.removeChild(row);
				}
        		tmpType = null;
        	}
        }
        dhx.env.isIE&&CollectGarbage();
    });
    
    //表分类下拉树数据准备。
    var tableGroupTree = new loadTableGroupTree($("_tableGroup"), $("_tableGroupId"));
    //为_tableGroup添加事件
    Tools.addEvent($("_tableGroup"), "click", function(){
        var tableType = dwr.util.getValue("_tableType");
        if(!tableType){
            dhx.alert("请您先选择一个层次分类！");
            return;
        }
        tableGroupTree.treeDiv.style.width = $("_tableGroup").offsetWidth + 'px';
        Tools.divPromptCtrl(tableGroupTree.treeDiv, $("_tableGroup"), true);
        tableGroupTree.treeDiv.style.display = "block";
        tableGroupTree.showGroupTree(tableType);
        dhx.env.isIE&&CollectGarbage();
    });
    // 给_tableOwner添加先选择数据源的提示
    Tools.addEvent($("_tableOwner"), "click", function(){
        var dataSource = dwr.util.getValue("_dataSource");
        if(!dataSource){
            dhx.alert("请您先选择一个数据源！");
            return;
        }
    });
    // 给表空间添加先选择数据源的提示
    Tools.addEvent($("_tableSpace"), "click", function(){
        var dataSource = dwr.util.getValue("_dataSource");
        if(!dataSource){
            dhx.alert("请您先选择一个数据源！");
            return;
        }
    });
    
    //加载所有的表类数据源
    dwrCaller.executeAction("queryTableDataSource", function(data){
    	dataSourceData = null;
        dataSourceData=data;
        Tools.addOption("_dataSource", data);
        dhx.env.isIE&&CollectGarbage();
    })

    //表名输入跟随
    //输入时提醒。
    new Tools.completion($("_tableName"), {
        dwrAction:dwrCaller.matchTables,
        onTargetValeChange:function(value){
            if(value.length > 3){
                //必须用call的方式保证onTargetValeChange在this对象上执行
                this._super.onTargetValeChange.call(this, value);
            } else{
                this.showAndHide("hide");
            }
            dhx.env.isIE&&CollectGarbage();
        },
        onDivBuild:function(div){
            var title = dhx.html.create("div",
                    {style:"width:100%;hight:30px;background-color: #FFFFFF;z-index:1000;position:relative;font-weight:bold;" +
                           "margin-top:5px;margin-bottom: 5px;display:none"}, "已有类似表：");
            div.appendChild(title);
            dhx.env.isIE&&CollectGarbage();
        },
        build:function(data){
        	if(data.options.length > 0){
        		this._content.innerHTML = "";
	            var contentHtml = '<table cellpadding="0" cellspacing="0" style=" width: 100%;height: auto; table-layout: fixed;" >' +
	                              '<tbody>';
	            this._selectIndex = -1;//重置选择索引。
	            this._options = data.options || data;
	            //新增表头
	            contentHtml += "<tr style='background-color: #0099FF;'><td style='text-align: center;width: 30%'>表名</td>" +
	                           "<td style='text-align: center;width: 20%'>表中文名</td><td style='text-align: center;width: 10%'>所属用户</td></tr>"
	            if(this._options && this._options.length > 0){
	                for(var i = 0; i < this._options.length && i < 15; i++){
	                    contentHtml += '<tr id="' + this.getSelectUniqueName(i) + '" style="' + this.style.unSelect +
	                                   ';" onmouseover=Tools.completion._completions[' + +this._uid + '].suggestOver(this,' +
	                                   i + ')';
	                    contentHtml += " onmouseout=Tools.completion._completions[" + this._uid + "].suggestOut(this) ";
	                    contentHtml += " onmousedown=Tools.completion._completions[" + this._uid + "].select(" + i + ") >";
	                    contentHtml += "<td style='text-align: left;'>" +
	                                   this._options[i].text.substring(0,25).toUpperCase().replace(new RegExp(this._keyWord, "i"),
	                                           "<font color=red>" + this._keyWord.toUpperCase() + "</font>") + "</td>";
	                    contentHtml += '<td style="text-align: left;">' + this._options[i].userdata.tableNameCn.substring(0,15) + '</td>';
	                    if(this._options[i].userdata.tableOwner){
	                    	contentHtml += '<td style="text-align: left;">' + this._options[i].userdata.tableOwner + '</td>';
	                    }else{
	                    	contentHtml += '<td style="text-align: left;">' + '&nbsp;' + '</td>';
	                    }
	//                    contentHtml += '<td style="text-align: left;">' + this._options[i].userdata.tableBusComment +
	//                                   '</td>';
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
	            dhx.env.isIE&&CollectGarbage();
        	}else{
        		this.showAndHide("hide");
        	}
        }
    });

    //对表单添加验证
    dhtmlxValidation.addValidation($("_baseInfoForm"), [
        {target:"_tableName",rule:"NotEmpty,MaxLength[1000],ValidByCallBack[tblNameCheck]"},
        {target:"_tableType",rule:"NotEmpty",noFocus:"true"},
        {target:"_tableGroup",rule:"NotEmpty"},
        {target:"_dataSource",rule:"NotEmpty,ValidByCallBack[tblNameCheck]",noFocus:"true" },
        {target:"_tableBusComment",rule:"MaxLength[1000]"},
        {target:"_partitionSql",rule:"MaxLength[1000]"},
        {target:"_tableSpace",rule:"NotEmpty,MaxLength[64],ValidAplhaNumeric"},
        {target:"_tableNameCn",rule:"NotEmpty,MaxLength[64]"},
        {target:"_tableOwner",rule:"NotEmpty,MaxLength[32],ValidAplhaNumeric"}
    ],"true")
    //预览SQL按钮
    var showSQL = Tools.getButtonNode("预览SQL");
    showSQL.style.cssFloat="left";
    showSQL.style.styleFloat="left";
    showSQL.style.marginLeft="20px";
    showSQL.onclick = function(){
        genSql();
    }
    //提交按钮
    var submit = Tools.getButtonNode("提交");
    $("_submit").appendChild(submit);
    submit.style.marginLeft = (Math.round($("_submit").offsetWidth / 2)-100) + "px";
    $("_submit").appendChild(showSQL);
    submit.style.cssFloat="left";
    submit.style.styleFloat="left";
    submit.onclick = function(){
        submitValidate();
    }
    addColumnRow();

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
    initData();
    dhx.env.isIE&&CollectGarbage();

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
    Tools.divPromptCtrl($("_columnComment"), input, true);
    $("_columnComment").style.display = "inline";
    dhx.env.isIE&&CollectGarbage();
}

var clearComment = function(img,rowIndex) {
	var input = $("_dimInfo" + img.parentNode.parentNode._index);
	input.value = "";
	$("_colBusType" + rowIndex).value = 1;
}

var dataTypeData = getCodeComboByType("DATA_TYPE");;//缓存DATATYPE数据

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
//            supportStr=supportStr.replace(/NUMBER/ig,"");
            supportStr=supportStr.replace(/VARCHAR2/ig,"");
            supportStr=supportStr.replace(/VARCHAR/ig,"");
            supportStr=supportStr.replace(/CHAR/ig,"");
            supportStr=supportStr.replace(/RAW/ig,"");
//            supportStr=supportStr.replace(/DATE/ig,"");
            //number 特殊处理
            supportStr += "|NUMBER\\(\\d+,\\-?\\d+\\)|NUMBER\\(\\d+\\)";
            //varchar2
            supportStr += "|VARCHAR2\\(\\d+\\)";
            //varchar
            supportStr += "|VARCHAR\\(\\d+\\)";
            //char
            supportStr += "|CHAR\\(\\d+\\)";
            //raw
            supportStr += "|RAW\\(\\d+\\)";
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
/**默认值验证*/
var defaultValValidate=function(value,rowIndex){
	// 根据行索引获得数据类型信息
	var dataType = dataTypeCombo[rowIndex].getComboText();
	//数据类型为number，验证合法性。
//	alert(dataType)
	if(dataType.toString().search("NUMBER")> -1){
		if(dataType.toString().indexOf("(") !=-1){
			var _dataType=dataType.toString().split("(")[1].toString().split(")")[0];
		}else{
			var _dataType=dataType;
		}
		//数字验证（包含小数）。
		var reg = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/;
		// 获得数据类型信息
		if(!reg.exec(value)){
			return "数据类型不匹配！"
		}else if(_dataType.toString().indexOf(",") != -1){// 1-数据类型有小数验证
			if(value.indexOf(".") != -1){// 1.1-默认值有小数
				var _inputValue = value.toString().split(".");
				if(_inputValue[0].toString().length > parseInt(_dataType.split(",")[0]) || _inputValue[1].toString().length > parseInt(_dataType.split(",")[1])){
					return "数据精度错误！";
				}
			}else if(!value.indexOf(".") != -1){// 1.2-默认值没有小数
				if(value.toString().length > parseInt(_dataType.split(",")[0])){
					return "数据精度错误！";
				}
			}
		}else if(!_dataType.toString().indexOf(".") != -1){// 2-数据类型没有小数验证
			if(_dataType == "NUMBER"){// 数据默认长度22不匹配
				if(value.toString().length > 22){
					return "数据默认长度大于22！";
				}
			}
			if(value.toString().indexOf(".") != -1){// 2.1-默认值有小数
				return "数据精度错误！";
			}else if(!value.toString().indexOf(".") != -1){// 2.2-默认值没有小数
				if(value.toString().length > parseInt(_dataType)){
					return "数据精度错误！";
				}
			}
		}
	}else if(dataType.toString().search("CHAR")> -1){
		var _dataType=dataType.toString().split("(")[1].toString().split(")")[0];
		if(value.toString().length > parseInt(_dataType)){
			return "数据长度错误！"
		}
	}
	return true;
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
        var input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"colName",id:"_colName" +
                                                                                                 rowIndex,type:"TEXT",style:"width: 90%;height:15px;line-height:15px;text-transform: uppercase"});
        //添加事件，开始输入时，新增一行。
        Tools.addEvent(input, "keyup", function(e){
            e=(e||window.event);
            var element=e.srcElement||e.target;
            var thisTr = element.parentNode.parentNode;
            //新增空白行。
            if(!thisTr.nextSibling){
                addColumnRow(parseInt(thisTr._index) + 1);
            }
        })
        //输入时提醒。
        new Tools.completion(input, {
            dwrAction:dwrCaller.matchCols,
            onTargetValeChange:function(value){
	    		 if(value.length > 3){//输入四个字母时输入跟随。
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
	                contentHtml += "<tr style='background-color: #0099FF;'><td style='text-align: center;width: 35%'>名称</td>" +
	                               "<td style='text-align: center;width: 35%'>类型</td><td style='text-align: center;width: 30%'>注释</td></tr>"
	                if(this._options && this._options.length > 1){
	                    for(var i = 0; i < this._options.length; i++){
	                        contentHtml += '<tr id="' + this.getSelectUniqueName(i) + '" style="' + this.style.unSelect +
	                                       '" onmouseover=Tools.completion._completions[' + +this._uid +
	                                       '].suggestOver(this,' + i + ')';
	                        contentHtml += " onmouseout=Tools.completion._completions[" + this._uid + "].suggestOut(this) ";
	                        contentHtml += " onmousedown=Tools.completion._completions[" + this._uid + "].select(" + i +
	                                       ") >";
	                        contentHtml += "<td style='text-align: left;'>" +
	                                       this._options[i].text.substring(0,13).toUpperCase().replace(new RegExp(this._keyWord, "i"),
	                                               "<font color=red>" + this._keyWord.toUpperCase() + "</font>") + "</td>";
	                        contentHtml += '<td style="text-align: left;">' + this._options[i].userdata.datatype +
	                                       '</td>';
	                        if(this._options[i].userdata.colBusComment){
	                        	contentHtml += '<td style="text-align: left;">' + this._options[i].userdata.colBusComment + '</td>';
	                        }else{
	                        	contentHtml += '<td style="text-align: left;">' + '' + '</td>';
	                        }
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
            	} else{
            		this.showAndHide("hide");
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
                                                              rowIndex,className:"dhxlist_txt_textarea",type:"TEXT",style:"width: 90%;height:15px;line-height:15px;"})
        td.appendChild(input);
        //类型列。
        td = tr.insertCell(2);
        td.align = "center";
        var inputDiv = dhx.html.create("select", {style:"width:90%;height:15px;line-height:15px;",id:"_colDatatype" + rowIndex,name:"colDatatype"});
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
                {name:"isPrimary",id:"_isPrimary" + rowIndex,type:"checkbox",style:"width: 90%;"});
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
                {name:"colNullabled",id:"_colNullabled" + rowIndex,type:"checkbox",style:"width: 90%;"})
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
                                                               rowIndex,className:"dhxlist_txt_textarea",type:"TEXT",style:"width: 90%;height:15px;line-height:15px;"})
   		
        td.appendChild(input);
		
        
        //注释列
        td = tr.insertCell(6);
        td.align = "center";
        input = dhx.html.create("input", {name:"colBusComment",id:"_colBusComment" +
                                                                  rowIndex,className:"dhxlist_txt_textarea",type:"TEXT",style:"width: 65%;height:15px;line-height:15px;"});
        td.appendChild(input);
        td.innerHTML += '<img src="../../../resource/images/more.png" alt="更多" onclick="openComment(this)">';
        //维度列
        td = tr.insertCell(7);
        td.align = "center";
//        //for Ie7
        td.innerHTML='<input type="text" id="_dimInfo'+rowIndex+'" name="dimInfo" class="dhxlist_txt_textarea" readOnly="true" style="width:70%" onclick="selectDim(this)">';
        td.innerHTML += '<img src="../../../resource/images/cancel.png" style="width:12px;height:12px;" title="清除维度信息" onclick="clearComment(this,'+rowIndex+')">';

        //新增维度有关的hidden隐藏域
        
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"colBusType",value:1,id:"_colBusType" + rowIndex}));//默认非维度，指标
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"dimLevel",id:"_dimLevel" + rowIndex}));//维度层级
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"dimColId",id:"_dimColId" + rowIndex}));//关联的列ID。
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"dimTableId",id:"_dimTableId" + rowIndex}));//关联的维度表ID
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"dimTypeId",id:"_dimTypeId" + rowIndex}));
        td = tr.insertCell(8);
        td.align = "center";
        //新增一行不能删除。
        td.innerHTML = "&nbsp;";
        if(isShowOper!=false){
	        //已经编辑的行可以删除。
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
            {target:"_colNameCn" + rowIndex,rule:"MaxLength[32]"},
            {target:dataTypeCombo[rowIndex].DOMelem_input,rule:"NotEmpty,ValidByCallBack[dataTypeValidate]"},
            {target:"_colBusComment" + rowIndex,rule:"MaxLength[1000]"},
            {target:"_defaultVal" + rowIndex,rule:"ValidByCallBack[defaultValValidate&"+rowIndex+"]"}            
        ]);
    }
    return rowIndex;
}
/**
 * 删除一行
 * @param img
 */
var removeRow = function(img){
	dhx.confirm("确定删除？删除后只能重新操作！",function(b){
		if(b){
			//获取行得index
		    var rowIndex = img.parentNode.parentNode._index;
		    var trId = "_columnRow" + rowIndex;
		    $(trId).parentNode.removeChild($(trId));
		    dataTypeCombo[rowIndex] = null;
		    delete dataTypeCombo[rowIndex];
		}
	})
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
 * 添加查询表分类Action
 */
dwrCaller.addAutoAction("queryTableGroup", "TableApplyAction.queryTableGroup");
dwrCaller.addDataConverter("queryTableGroup", new dhtmxTreeDataConverter({
    idColumn:"tableGroupId",pidColumn:"parGroupId",textColumn:"tableGroupName",
    isDycload:false
}));
dwrCaller.isShowProcess("queryTableGroup", false);
var treeData = {};
var lastTableType = null;//最后一次显示时的tableType.
/**
 * 加载业务类型树形结构。
 * @param target
 * @param form
 */
var loadTableGroupTree = function(target, hide){
    //创建tree Div层
    var div = dhx.html.create("div", {
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
    this.tree = tree
    this.showGroupTree = function(tableType){
        var show = function(){
        	try{
            if(lastTableType != tableType){
                var childs=tree.getAllSubItems(0);
                if(childs){
                    var childIds=childs.toString().split(",");
                    for(var i=0;i<childIds.length;i++){
                        tree.deleteItem(childIds[i]);
                    }
                }
                tree.loadJSONObject(treeData[tableType]);
                childs = null;
            }
            }catch(e){
            	alert(e);
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
    textColumn:"dimTypeName"
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
        var radioCheck=function(rId){
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
            dwrCaller.executeAction("queryDimType", rId, function(data){
                Tools.addOption("_dimType", data);
                if(dimType){
                    dwr.util.setValue("_dimType",dimType);
                }
                $("_dimType").onchange.apply(window); //触发onchange事件
            })
        }
        //添加radio点击事件。
        queryDimGrid.attachEvent("onCheck", function(rId, cInd, state){
            if(state){
                radioCheck(rId);
            }
        });
        // 添加行点击事件
        queryDimGrid.attachEvent("onRowSelect",function(id,ind){
        	queryDimGrid.cells(id,0).setValue(1);
        	radioCheck(id);
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
/**
 * 获取页面数据
 */
var getData = function(){
    var tableData = Tools.getFormValues("_baseInfoForm");
    //表状态属于修改状态。
    tableData.tableState = 2;
    //表状态属于0
    tableData.tableVersion = 1;
    //列字段数据
    var columnData = [];
    var tableObj = $("_clumnContentTable");
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
        var fullDataType=dataTypeCombo[tableObj.rows[i]._index].getComboText();
        if(fullDataType.toUpperCase().indexOf("DATA")>0){
            fullDataType="DATE";//ORACLE建表语句DATE类型不能有长度
        }
        rowData.fullDataType=fullDataType;
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
        }
        rowData.colNullabled=rowData.colNullabled?1:0;
        rowData.isPrimary=rowData.isPrimary?1:0;
        columnData.push(rowData);
    }
    var res = {};
    res.tableData = tableData;
    res.columnDatas = columnData;
    return res;
}

/**
 * 提交数据验证
 */
var validate = function(){
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
dwrCaller.addAutoAction("insertTable","TableApplyAction.insertTable");
dwrCaller.isDealOneParam("insertTable",false);
/**
 * 提交验证。
 */
var submitValidate=function(){
    if(validate()){
        isValidateFalse=false;
        var data= getData();
        dwrCaller.executeAction("validate",data,function(validateResult){
            var dhxWindow = new dhtmlXWindows();
            dhxWindow.createWindow("validateWindow", 0, 0, 450, 400);
            var validateWindow = dhxWindow.window("validateWindow");
            validateWindow.stick();
            validateWindow.setModal(true);
            validateWindow.setDimension(550, 400);
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
            var validateLayout = new dhtmlXLayoutObject(validateWindow, "3C");
            validateLayout.cells("a").setHeight(400);
            validateLayout.cells("a").fixSize(false, true);
            validateLayout.cells("a").firstChild.style.height = (validateLayout.cells("a").firstChild.offsetHeight + 5) + "px";
            validateLayout.cells("a").hideHeader();
            validateLayout.cells("c").hideHeader();
            validateLayout.cells("b").hideHeader();
            validateLayout.cells("b").setHeight(100);
            var mygrid= validateLayout.cells("a").attachGrid();
            mygrid.setHeader("检查项,检查值,说明,检查结果");
            mygrid.setInitWidthsP("16,49,20,15");
            mygrid.setColAlign("left,left,left,center");
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
            var save=Tools.getButtonNode("确定");
            div.appendChild(save);
            save.style.marginLeft = "190px";
            save.style.styleFloat = "left";
            save.style.cssFloat = "left";
            save.style.marginTop = "10px";
            // 验证框显示内容
        	var htmlString = "<div>验证通过，点击确定保存该表！</div>";
            if(isValidateFalse){
            	htmlString = "<div>您有验证项未通过，是否确定？</div>";
            }
            validateLayout.cells("b").attachHTMLString(htmlString);
            save.onclick=function(){
                dwrCaller.executeAction("insertTable",data,function(rs){
                    if(rs){
                        dhx.alert("保存成功！");
                        validateWindow.close();
                        clearTablData();
                        clearAllColumnData(false);
                    }else{
                        dhx.alert("保存失败，请重试！");
                    }
                });
            }
            var close = Tools.getButtonNode("取消");
            close.style.marginLeft = "20px";
            close.style.marginTop = "10px";
            close.style.styleFloat = "left";
            close.style.cssFloat = "left";
            //添加close事件
            close.onclick = function(){
                validateWindow.close();
            }
            div.appendChild(close);
            validateLayout.cells("c").attachObject(div);
        });
    }
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
    },
    /**默认全部全选*/
     cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
		if (columnIndex == 0) { //如果是0则返回1全部选中
            return 1;
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}));

dwrCaller.addAutoAction("queryDbTableColumns","TableApplyAction.queryDbTableColumns");
dwrCaller.addDataConverter("queryDbTableColumns", new dhtmxGridDataConverter({
    idColumnName:"colName",
    filterColumns:["","colName","colDatatype","colNullabled","isPrimary","defaultVal","colBusComment"],
    userData:function(rowIndex, rowData){
        rowData.colBusType=1;//非维度
        return rowData;
    }
}));
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
        /*
        *修改人：吴喜丽
        *修改时间：2012-01-06
        *修改原因：BUG清单编号-DSSMSS-00000223
        *       表类管理----新增表类的时候，从元数据表类复制页面，层次分类下拉为空
        *修改处：增加了：Tools.addOption("_querytableType", tableTypeData);
        */
		Tools.addOption("_querytableType", tableTypeData);
		
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
        var tableGroupTree = new loadTableGroupTree($("_queryTableGroup"), $("_queryTableGroupId"));
        /*
        *修改人：吴喜丽
        *修改时间：2012-01-06
        *修改原因：BUG清单编号-DSSMSS-00000223
        *       表类管理----新增表类的时候，从元数据表类复制页面，层次分类下拉为空
        *修改处：增加了：Tools.addEvent($("_queryTableGroup"), "click", function(){
        */
        //为_tableGroup添加事件
        Tools.addEvent($("_queryTableGroup"), "click", function(){
        	var tableType = dwr.util.getValue("_querytableType");
	        if(!tableType){
	            dhx.alert("请您选选择一个层次分类！");
	            return;
	        }
            tableGroupTree.treeDiv.style.width = $("_queryTableGroup").offsetWidth + 'px';
            Tools.divPromptCtrl(tableGroupTree.treeDiv, $("_queryTableGroup"), true);
            tableGroupTree.treeDiv.style.display = "block";
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
        //添加radio点击事件。查询其列字段
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
    },
    /**默认全部全选*/
     cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
		if (columnIndex == 0) { //如果是0则返回1全部选中
            return 1;
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
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
        var winsize=Tools.propWidthDycSize(0,24,0,20);
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
        (!dhx.env.isIE)&&(queryColumnGrid.entBox.style.marginTop="-18px");

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
        });

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



/**
 * 查询表类信息Action
 */
dwrCaller.addAutoAction("queryTableByIdAndVersion",TableDimAction.queryTableByIdAndVersion,tableId,tableVersion,{
    converter:new columnNameConverter(),
    dwrConfig:true,
});
/**
 * 所有列信息Action定义
 */
dwrCaller.addAutoAction("queryAllColumnsByTableID",TableDimAction.queryAllColumnsByTableID,tableId,{
    dwrConfig:true,
    converter: new columnNameConverter()
});

/**
 * 初始化信息。
 * @param img
 */
var cont = 0;	
var initData = function(){

dwrCaller.executeAction("queryTableByIdAndVersion",function(data){
   var tableData = data;
   var obj = {value:tableData.dataSourceId};
   cont ++;
   dwrCaller.executeAction("getUserNameByDataSourceId",obj.value,function(data){
		cont ++;
		document.getElementById("_tableOwner").innerHTML = "";
        Tools.addOption("_tableOwner", [{value:"",text:"--请选择--"}]);
        if(data){
            if(!(data.options.length==1 && !data.options[0].text)){
                Tools.addOption("_tableOwner", data);
            }
        }
        dhx.env.isIE&&CollectGarbage();
      dwrCaller.executeAction("queryTableSpace",obj.value,function(data){
	   cont ++;
       document.getElementById("_tableSpace").innerHTML = "";
       Tools.addOption("_tableSpace", [{value:"",text:"--请选择--"}]);
	   if(data){
            if(!(data.options.length==1 && !data.options[0].text)){
                Tools.addOption("_tableSpace", data);
            }
        }
        dhx.env.isIE&&CollectGarbage();
        dwrCaller.executeAction("queryAllColumnsByTableID",tableVersion,function(columnDatas){
	   while(cont == 3){
		     copyTablData(tableData,columnDatas);
		     cont ++;
	   }
	});
    });
    });
   
});
	
}






/**
 * 复制表类和列信息到页面
 * @param tableData
 * @param columnDatas
 */
var copyTablData=function(tableData,columnDatas){
    if(tableData){
        tableData.tableName&&dwr.util.setValue($("_tableName"),tableData.tableName);
        if(tableData.tableName){
            tableName = tableData.tableName;
        }
        tableData.tableNameCn&&dwr.util.setValue($("_tableNameCn"),tableData.tableNameCn);
        tableData.tableTypeId>=0&&dwr.util.setValue($("_tableType"),tableData.tableTypeId);
        tableData.tableGroupName&&dwr.util.setValue($("_tableGroup"),tableData.tableGroupName);
        tableData.tableGroupId>0&&dwr.util.setValue($("_tableGroupId"),tableData.tableGroupId);
        tableData.tableSpace&&dwr.util.setValue($("_tableSpace"),tableData.tableSpace);
        tableData.partitionSql&&dwr.util.setValue($("_partitionSql"),tableData.partitionSql);
        tableData.tableBusComment&&dwr.util.setValue($("_tableBusComment"),tableData.tableBusComment);
        tableData.dataSourceId>=0&&dwr.util.setValue($("_dataSource"),tableData.dataSourceId);
        tableData.tableOwner&&dwr.util.setValue($("_tableOwner"),tableData.tableOwner);
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
var  clearTablData=function(){
    dwr.util.setValue($("_tableName"),"");
    dwr.util.setValue($("_tableNameCn"),"");
    dwr.util.setValue($("_tableType"),"");
    dwr.util.setValue($("_tableGroup"),"");
    dwr.util.setValue($("_tableGroupId"),"");
    dwr.util.setValue($("_tableSpace"),"");
    dwr.util.setValue($("_partitionSql"),"");
    dwr.util.setValue($("_tableBusComment"),"");
    dwr.util.setValue($("_dataSource"),"");
    dwr.util.setValue($("_tableOwner"),"");
}
/**
 * 复制一行列数据
 * @param columnData
 */
var copyColumnData=function(data,rowIndex){
    //依次赋值。
    dwr.util.setValue("_colName" + rowIndex, data.colName);
    data.colNameCn&&dwr.util.setValue("_colNameCn" + rowIndex, data.colNameCn);
    dataTypeCombo[rowIndex].setComboText(data.datatype||data.colDatatype);
    dwr.util.setValue("_colNullabled" + rowIndex, data.colNullabled);
    dwr.util.setValue("_defaultVal" + rowIndex, data.defaultVal);
    dwr.util.setValue("_isPrimary" + rowIndex, data.isPrimary);
    dwr.util.setValue("_colBusComment" + rowIndex, data.colBusComment);
    dwr.util.setValue("_colBusType" + rowIndex, data.colBusType);
    dwr.util.setValue("_dimLevel" + rowIndex, data.dimLevel);
    dwr.util.setValue("_dimColId" + rowIndex, data.dimColId);
    dwr.util.setValue("_dimTableId" + rowIndex, data.dimTableId);
    dwr.util.setValue("_dimTypeId" + rowIndex, data.dimTypeId);
    //如果是维度，还需设置维度的显示信息。
    
    if(data.colBusType == 0){
        dwr.util.setValue("_dimInfo" + rowIndex,
                data.dimTableName + "," + data.dimColName + "," + data.dimTypeName + "," + data.dimLevel);

    }
}
/**
 * 清空列信息表单数据
 * 修改人：吴喜丽
 * 修改时间：2012-01-05
 * 修改原因：增加confirm提示信息
 */
var clearAllColumnData=function(boo){
	var tableObj = $("_clumnContentTable");
	if(boo == true){
		dhx.confirm("确定清除？清除后只能重新操作！",function(b){
			if(b){
			    for(var rowIndex = 1; rowIndex < tableObj.rows.length - 1; rowIndex++){
			        var row=  tableObj.rows[rowIndex--];
			        row.parentNode.removeChild(row);
			    }
			    var tableType = dwr.util.getValue("_tableType");
			    if(tableType == 3){
		        	initCol();
		        }
			}
		})
	}else{
		for(var rowIndex = 1; rowIndex < tableObj.rows.length - 1; rowIndex++){
			 var row=  tableObj.rows[rowIndex--];
			 row.parentNode.removeChild(row);
		}
		var tableType = dwr.util.getValue("_tableType");
		if(tableType == 3){
		   initCol();
		}
	}
}
/*
*增加人：吴喜丽
* modify:tanwc  add colBusType:1   
*时间：2012-01-09
*原因：增加接口表基本列信息
*/
var initColData=[
	{colName:"SRC_ID",datatype:"NUMBER",colBusType:1,colNullabled:0,isPrimary:1,colBusComment:"ID",columAttribute:3,colNameCn:"ID"},
	{colName:"SRC_CODE",datatype:"VARCHAR2(100)",colBusType:1,colNullabled:0,isPrimary:0,colBusComment:"编码",columAttribute:1,colNameCn:"编码"},
    {colName:"SRC_PAR_CODE",datatype:"NUMBER(20)",colBusType:1,colNullabled:0,isPrimary:0,colBusComment:"父编码",columAttribute:2,colNameCn:"父编码"},
    {colName:"SRC_NAME",datatype:"VARCHAR2(500)",colBusType:1,colNullabled:0,isPrimary:0,colBusComment:"名称",columAttribute:0,colNameCn:"名称"}
]
/*
*增加人：吴喜丽
*时间：2012-01-09
*原因：增加接口表基本列信息
*	  初始化接口表基本列，这些列为必须
*/
var initCol=function(){
    for(var i=0;i<initColData.length;i++){
        copyColumnData(initColData[i],columnNum);
        //Name不可以修改
        $("_colName"+columnNum).setAttribute("disabled",true);
        dataTypeCombo[columnNum].disable(true);
        $("_isPrimary"+columnNum).setAttribute("disabled",true);
        $("_colNullabled"+columnNum).setAttribute("disabled",true);
//        $("_dimInfo"+columnNum).setAttribute("disabled",true);
        addColumnRow(null,false);
    }
}

window.unload=function(){
    dwrCaller.unload();
    dwrCaller=null;
    tblMacro=null;
}

//切换层次分类清空业务类型
function removeGroupType(){
	document.getElementById("_tableGroup").value = "";
}
dhx.ready(applyInit);
