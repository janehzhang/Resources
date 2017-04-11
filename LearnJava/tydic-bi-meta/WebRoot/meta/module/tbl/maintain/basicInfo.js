/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        basicInfo.js
 *Description：
 *       维护表类具体信息JS
 *Dependent：
 *       dhtmlx.js、dwr相关JS文件、dhtmlxExtend.js、tool。js等。
 *Author:
 *        熊久亮
 *Finished：
 *       2011-11-14
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller = new biDwrCaller();
var updateCount = 0;
dwrCaller.addAutoAction("isAudit","MaintainRelAction.isAudit");
/***========================== 基本信息TAB页 begin ==================================================*/
var initBasicInfo = function(){
    var updataBasicInfoLayout = new dhtmlXLayoutObject(document.getElementById("detail"), "2E");
    updataBasicInfoLayout.cells("a").hideHeader();
    var tabbar=updataBasicInfoLayout.cells("a").attachTabbar();
    updataBasicInfoLayout.hideConcentrate();//隐藏缩放按钮
    updataBasicInfoLayout.hideSpliter();
    updataBasicInfoLayout.cells("a").attachObject($("updataTableBase_Form"));
    updataBasicInfoLayout.cells("a").hideHeader();
    updataBasicInfoLayout.cells("b").hideHeader();
    updataBasicInfoLayout.cells("a").setHeight(169);
    //updataBasicInfoLayout.cells("b").setHeight(400);
    initBaseInfo();

    //设置列表单样式
    updataBasicInfoLayout.cells("b").attachObject($("_columnFormDiv"));
    updataBasicInfoLayout.cells("b").hideHeader();
    $("_clumnContentTable").parentNode.style.height = ($("_columnFormDiv").offsetHeight) + "px";
    $("_columnHeadTable").style.width= $("_clumnContentDiv").clientWidth+"px";
    dhx.html.addCss($("_columnTableDiv"), global.css.gridTopDiv);

    /**----------------------------------设置表单验证信息 begin----------------------------------*/
    dhtmlxValidation.addValidation($("updataTableBase_Form"), [
        {target:"table_name_cn",rule:"NotEmpty"},
        {target:"table_attType",rule:"NotEmpty"},
        {target:"table_operType",rule:"NotEmpty"}
    ])
    /**----------------------------------设置表单验证信息 begin----------------------------------*/

    dwrCaller.addAutoAction("queryAllColumnsByTableID","TableDimAction.queryAllColumnsByTableID",tableId,{
        dwrConfig:true,
        converter: new columnNameConverter()
    });

    //对调函数对生成的表格进行赋值
    initBaseTableInfo(false);

    /**--------------------------按钮区 begin--------------------------**/

        //提交按钮
    var submit = Tools.getButtonNode("保存");
    $("_submit").appendChild(submit);
    submit.style.marginLeft = (Math.round($("_submit").offsetWidth / 2)-120) + "px";
    submit.style.cssFloat="left";
    submit.style.styleFloat="left";
    submit.style.marginTop="-20px";
    submit.onclick = function(){
        submitValidate();
    }

    var reset = Tools.getButtonNode("重置");
    $("_submit").appendChild(reset);
    reset.style.marginLeft = 10+ "px";
    reset.style.cssFloat="left";
    reset.style.styleFloat="left";
    reset.style.marginTop="-20px";
    reset.onclick = function(){
        resetFormData();
    }

    var showSQL = Tools.getButtonNode("SQL预览");
    $("_submit").appendChild(showSQL);
    showSQL.style.marginLeft = 10+ "px";
    showSQL.style.cssFloat="left";
    showSQL.style.styleFloat="left";
    showSQL.style.marginTop="-20px";
    showSQL.onclick = function(){
        checkUpSql();
    }
    /**--------------------------按钮区 end--------------------------**/

    /**----------------------表类数据源相关Action begin---------------------*/
    var tableDataSourceConverter = new dhtmlxComboDataConverter({
        valueColumn:"dataSourceId",
        textColumn:"dataSourceName"
    });

    /**----------------------表类数据源相关Action end---------------------*/
    tabbar.attachEvent("onSelect", function() {
        if(arguments[0]=="basicInfo"){
            if(isDimTable=="Y"){
                tabbar.setHrefMode("iframes");
                tabbar.setContentHref("basicInfo", urlEncode(getBasePath()+"/meta/module/tbl/dim/updateDim.jsp?tableId="+tableId+"&tableVersion="+tableVersion));
                return true;
            }else{
                if(!isInitBasicInfo){
                    initBasicInfo();
                    isInitBasicInfo=true;
                }
                return true;
            }
        }else{
            return true;
        }
    });
    tabbar.setTabActive(focus);
     dwrCaller.executeAction("isAudit",tableId,tableVersion,function(data){ 
                   if(data){
                        dhx.alert("该基本信息已经在审核中,不能提交!");
                        return false;
                    }
                });
};
/***========================== 基本信息TAB页 end ==================================================*/
function initBaseTableInfo(isFromReset){
    dwrCaller.executeAction("queryAllColumnsByTableID",tableVersion,function(columnDatas){
        if(!isFromReset){//是否由重置而来
            addColumnRow(null,1);
        }
        if(columnDatas&&columnDatas.length>0){
            for(var i = 0; i < columnDatas.length ; i++){
                var closData = columnDatas[i];
                copyColumnData(closData,columnNum);
//				var colsName = closData.colName;
//				//如果名字不为空时，名称列disabled
//				if(colsName!= null || colsName != ""){
//					 $("_colName"+columnNum).setAttribute("disabled",true);
//				}
                addColumnRow(null,true);
            }
        }
    })
}
/**
 * 注册数据转换器
 */

//业务类型下拉菜单数据转换
var TableOperType = new dhtmlxComboDataConverter({
    valueColumn:"tableGroupId",
    textColumn:"tableGroupName"
});

//注册后台action,按表ID获取表信息
dwrCaller.addAutoAction("loadTableBaseInfo","MaintainRelAction.queryTableBaseInfoByTableId",tableId);

/**
 * 表属性下拉菜单
 */
var tableTypeData = null;


var lastTableType = null;//最后一次显示时的tableType.
/**
 * 注册业务类型下拉菜单ACTION
 */
var tableAttribute={};
dwrCaller.addAutoAction("loadTableOperType","MaintainRelAction.queryTableOperationType")
dwrCaller.addDataConverter("loadTableOperType", TableOperType);
//根据数据源查询用户
var tableOwnerConverter = new dhtmlxComboDataConverter({
    valueColumn:"username",
    textColumn:"username"
})
dwrCaller.addAutoAction("getUserNameByDataSourceId", "TableApplyAction.getUserNameByDataSourceId");
dwrCaller.addDataConverter("getUserNameByDataSourceId", tableOwnerConverter);
dwrCaller.isShowProcess("getUserNameByDataSourceId", false);
//初始化基本信息

// 表空间数据源相关Action
var tableSpaceConverter = new dhtmlxComboDataConverter({
	valueColumn:"name",
    textColumn:"name"
});
dwrCaller.addAutoAction("queryTableSpace","TableApplyAction.queryTableSpaceByDataSourceId");
dwrCaller.addDataConverter("queryTableSpace",tableSpaceConverter);
dwrCaller.isShowProcess("queryTableSpace", false);

function initBaseInfo(){
    var tableNameSpan = document.getElementById("tableName");
    tableNameSpan.value= tableName;
    dwrCaller.executeAction("loadTableBaseInfo",tableVersion, function(data){
        tableBasicInfo = data;
        if(tableBasicInfo["TABLE_BUS_COMMENT"])
        document.getElementById("_tableBusComment").innerHTML =  tableBasicInfo["TABLE_BUS_COMMENT"];
        if(tableBasicInfo["PARTITION_SQL"])
        document.getElementById("_partitionSql").innerHTML =  tableBasicInfo["PARTITION_SQL"];
        if(tableBasicInfo["TABLE_NAME_CN"])
        document.getElementById("table_name_cn").value = tableBasicInfo["TABLE_NAME_CN"];
        if(tableBasicInfo["DATA_SOURCE_NAME"])
        document.getElementById("data_source").value = tableBasicInfo["DATA_SOURCE_NAME"];
//        if(tableBasicInfo["DATA_SOURCE_ID"])
        document.getElementById("data_sourceHidden").value = tableBasicInfo["DATA_SOURCE_ID"];
        if(tableBasicInfo["TABLE_GROUP_ID"])
        document.getElementById("tableGroupId").value = tableBasicInfo["TABLE_GROUP_ID"];
        if(tableBasicInfo["TABLE_GROUP_NAME"])
        document.getElementById("table_operType").value = tableBasicInfo["TABLE_GROUP_NAME"];
        
//        setTimeout(function(){
            copyTableType(tableBasicInfo);
//        },100);

        dwrCaller.executeAction("getUserNameByDataSourceId",tableBasicInfo["DATA_SOURCE_ID"],function(data){
            $("_tableOwner").innerHTML="";
            if(data){
                if(!(data.options.length==1 && !data.options[0].text)){
                    Tools.addOption("_tableOwner", data);
                }
            }
            document.getElementById("_tableOwner").value = tableBasicInfo["TABLE_OWNER"];
            dhx.env.isIE&&CollectGarbage();
        });
        
       dwrCaller.executeAction("queryTableSpace",tableBasicInfo["DATA_SOURCE_ID"],function(data){
	       document.getElementById("_tableSpace").length = 0; 
		   if(data){
	            if(!(data.options.length==1 && !data.options[0].text)){
	                Tools.addOption("_tableSpace", data);
	            }
	        }
		   	document.getElementById("_tableSpace").value = tableBasicInfo["TABLE_SPACE"];
	        dhx.env.isIE&&CollectGarbage();
	    });

        lastTableType = data['TABLE_TYPE_ID'];
        //添加一行
//        addColumnRow(null,1);

    });

    //加载层次分类下拉菜单
    tableTypeData = getComboByRemoveValue("TABLE_TYPE");
    Tools.addOption("table_attType", tableTypeData);


    var tableGroupTree = new loadTableGroupTree($("table_operType"), $("tableGroupId"));
    //为_tableGroup添加事件
    Tools.addEvent($("table_operType"), "click", function(){
        var tableType = dwr.util.getValue("table_attType");
        if(!tableType){
            dhx.alert("请您选选择一个层次分类！");
            return;
        }
        tableGroupTree.treeDiv.style.width = $("table_operType").offsetWidth + 'px';
        Tools.divPromptCtrl(tableGroupTree.treeDiv, $("table_operType"), true);
        tableGroupTree.treeDiv.style.display = "block";
        tableGroupTree.showGroupTree(tableType);
    });
}

//设置属性下拉菜单选中
var tableTypeId = "";
function copyTableType(data){
    tableTypeId = data['TABLE_TYPE_ID'];
    document.getElementById('table_attType').value = tableTypeId;
}

function changeAttType(){
    dwr.util.setValue("table_operType","");
    dwr.util.setValue("tableGroupId","");
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

/**--------------------------------------添加行数据 begin-----------------------------**/

//查询字段类型
var dataTypeData = getCodeComboByType("DATA_TYPE");;//缓存DATATYPE数据

//列字段输入跟随Action定义
dwrCaller.addAutoAction("matchCols", "TableApplyAction.matchCols");
dwrCaller.addDataConverter("matchCols", new dhtmlxComboDataConverter({
    valueColumn:"colName",
    textColumn:"colName",
    userData: function(rowData){
        return rowData;
    }
}));
dwrCaller.isShowProcess("matchCols", false);
var columnNum = 0;//行递增索引。
var dataTypeCombo = {};//数据类型combo对象。

function addColumnRow(rowIndex,isNewData){
    rowIndex = (rowIndex == undefined || rowIndex == null) ? (++columnNum) : rowIndex;

    if(!$("_columnRow" + rowIndex)){
        if(columnNum < rowIndex){
            columnNum = rowIndex;
        }
        var tr = document.createElement("tr");
        tr._index = rowIndex;
        tr.id = "_columnRow" + rowIndex;
        $("_clumnContentBody").appendChild(tr);

        /***名称列*/
        var td = tr.insertCell(0);
        td.align = "center";
        var input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"colName",id:"_colName" +
                                                                                                 rowIndex,type:"TEXT",style:"width: 90%;text-transform: uppercase"});
        //输入联想
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
	                                       this._options[i].text.toUpperCase().replace(new RegExp(this._keyWord, "i"),
	                                               "<font color=red>" + this._keyWord.toUpperCase() + "</font>") + "</td>";
	                        contentHtml += '<td style="text-align: left;">' + this._options[i].userdata.datatype +
	                                       '</td>';
	                        if(this._options[i].userdata.colBusComment){
	                        	contentHtml += '<td style="text-align: left;white-space:nowrap">' + this._options[i].userdata.colBusComment + '</td>';
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
	                this._div.style.width = "400px";
	                this._content.style.width = "400px";
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
                delete data.colId;
                copyColumnData(data,rowIndex);
                this.showAndHide("hide");
            }
        });
        //添加事件，验证列名重复
        Tools.addEvent(input, "keyup", function(e){
            validateCol();
        })

        td.appendChild(input);

        //添加事件，开始输入时，新增一行。
        Tools.addEvent(input, "keyup", function(e){
            var target=(e||window.event).target||(e||window.event).srcElement;
            var thisTr = target.parentNode.parentNode;
            //新增空白行。
            if(!thisTr.nextSibling){
                addColumnRow(parseInt(thisTr._index) + 1);
            }
        });

        td.appendChild(dhx.html.create("input",{type:"hidden",name:"colId",id:"_colId"+rowIndex}));

        /**中文名称*/
        var td = tr.insertCell(1);
        td.align = "center";
        var input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"colNameCn",id:"_colNameCn" +
                                                                                                   rowIndex,type:"TEXT",style:"width: 90%;"});
        td.appendChild(input);

        /***数据类型**/
        td = tr.insertCell(2);
        td.align = "center";
        var inputDiv = dhx.html.create("select", {style:"width:90%",id:"_colDatatype" + rowIndex,name:"colDatatype"});
        td.appendChild(inputDiv);
        var dataCombo = new dhtmlXCombo(inputDiv);
        if(dataTypeData){
            dataCombo.addOption(dataTypeData.options);
        }
        if(lastTableType == 3 && rowIndex <= 3){
            dataCombo.disable(true);
        }
//        dataCombo.setComboText("NUMBER");
        dataCombo.enableOptionAutoPositioning(true);
        dataTypeCombo[rowIndex] = dataCombo;

        /***主键***/
        td = tr.insertCell(3);
        td.align = "center";
        input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"isPrimary",id:"_isPrimary" +
                                                                                               rowIndex,type:"checkbox",style:"width: 90%;"});
        //如果主键选中，就不能选中空
        input.onclick=function(e){
            e=e||window.event;
            var target=e.target||e.srcElement;
            var clickIndex=target.parentNode.parentNode._index;
            if(target.checked){
                $("_colNullabled"+clickIndex).checked=false;
            }
        }
        td.appendChild(input);

        /****默认值***/
        td = tr.insertCell(4);
        td.align = "center";
        input = dhx.html.create("input", {type:'text',id:"_defaultVal" + rowIndex,name:"defaultVal",style:"width: 90%"});
        td.appendChild(input);

        /****允许空****/
        td = tr.insertCell(5);
        td.align = "center";
        input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"colNullabled",id:"_colNullabled" +
                                                                                                  rowIndex,type:"checkbox",style:"width: 90%;"});
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

        /***注释****/
        td = tr.insertCell(6);
        td.align = "center";
        input = dhx.html.create("input", {className:"dhxlist_txt_textarea",name:"colBusComment",id:"_colBusComment" +
                                                                                                   rowIndex,type:"text",style:"width: 80%;"});
        td.appendChild(input);
        td.innerHTML += '<img src="../../../resource/images/more.png" alt="更多" onclick="openComment(this)">';

        /******维度****/
        td = tr.insertCell(7);
        td.align = "center";
        td.innerHTML='<input type="text" id="_dimInfo'+rowIndex+'" name="dimInfo" class="dhxlist_txt_textarea" readOnly="true" style="width:80%" onclick="selectDim(this)">';
		if(!(lastTableType == 3 && rowIndex <= 3)){
            td.innerHTML += '<img src="../../../resource/images/cancel.png" style="width:12px;height:12px;" title="清除维度信息" onclick="clearComment(this,'+rowIndex+')">';
        }
        //新增维度有关的hidden隐藏域
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"colBusType",value:1,id:"_colBusType" + rowIndex}));//默认非维度，指标
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"dimLevel",id:"_dimLevel" + rowIndex}));//维度层级
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"dimColId",id:"_dimColId" + rowIndex}));//关联的列ID。
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"dimTableId",id:"_dimTableId" + rowIndex}));//关联的维度表ID
        td.appendChild(dhx.html.create("input", {type:"hidden",name:"dimTypeId",id:"_dimTypeId" + rowIndex}));

        /*******操作*******/
        td = tr.insertCell(8);
        td.align = "center";
        if(isNewData != true){	//如果是新增数据才有操作按钮
        var lastTr=Tools.findPreviousSibling(tr);
        if(lastTr&&lastTr._index){
        if(!(lastTableType == 3 && rowIndex <= 3)){
             lastTr.lastChild.innerHTML = '<img src="../../../resource/images/cancel.png" alt="删除" onclick="removeRow(this)" style="width:16px;height: 16px;cursor: pointer">' +
                                         '<img src="../../../resource/images/move_up.png" id=' + "_moveUp" + rowIndex +
                                         ' alt="上移" onclick="moveUp(this,\'_moveUp\',\'_moveDown\')" style="width:16px;height: 16px;margin-left: 5px;cursor: pointer;">' +
                                         '<img src="../../../resource/images/move_down.png" id=' + "_moveDown" + rowIndex +
                                         ' alt="下移" onclick="moveDown(this,\'_moveUp\',\'_moveDown\')" style="width:16px;height: 16px;margin-left: 5px;cursor: pointer;">';
        }
        }
        }

        //增加表单验证
        dhtmlxValidation.addValidation(tr, [
            {target:"_colName" + rowIndex,rule:"NotEmpty,MaxLength[32],ValidAplhaNumeric,ValidByCallBack[columnNameCheck]"},
            {target:dataTypeCombo[rowIndex].DOMelem_input,rule:"NotEmpty"},
            {target:dataTypeCombo[rowIndex].DOMelem_input,rule:"NotEmpty,ValidByCallBack[dataTypeValidate]"},
//            {target:"_colNameCn"+rowIndex,rule:"NotEmpty"},
            {target:"_colBusComment" + rowIndex,rule:"MaxLength[1000]"},
            {target:"_defaultVal" + rowIndex,rule:"ValidByCallBack[defaultValValidate&"+rowIndex+"]"}
        ]);
        return rowIndex;
    }
}
/**默认值验证*/
var defaultValValidate=function(value,rowIndex){
	// 根据行索引获得数据类型信息
	var dataType = dataTypeCombo[rowIndex].getComboText();
	//数据类型为number，验证合法性。
//	alert(dataType)
	//数字验证（包含小数）。
	var reg = /^([0-9]+|(-?[0-9]+[\.]?[0-9]+))$/;
	if(dataType.toString().search("NUMBER")> -1){
		if(dataType.toString().indexOf("(") !=-1){
			var _dataType=dataType.toString().split("(")[1].toString().split(")")[0];
		}else{
			var _dataType=dataType;
		}
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
	}else if(dataType.toString().search("INTEGER")> -1){
		if(!reg.exec(value)){
			return "数据类型不匹配！"
		}
	}
	return true;
}
/**
 * 列名验证。
 * @param value
 */
function columnNameCheck(value){
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
 *数据类型验证
 */
var supportStr = "";
function dataTypeValidate(value){
    //先找到支持的数据类型，构造正则表达式
    if(!supportStr){
        if(dataTypeData && dataTypeData.options.length > 0){
            for(var i = 0; i < dataTypeData.options.length; i++){
                supportStr += dataTypeData.options[i].text + ((i == dataTypeData.options.length - 1) ? "" : "|");
            }
            //number 特殊处理
            supportStr += "|NUMBER\\(\\d+,\\-?\\d+\\)|NUMBER\\(\\d+\\)";
            supportStr=supportStr.replace(/NVARCHAR2/ig,"");
            //varchar2
            supportStr=supportStr.replace(/VARCHAR2/ig,"");
            supportStr=supportStr.replace(/VARCHAR/ig,"");
            supportStr=supportStr.replace(/CHAR/ig,"");
            supportStr=supportStr.replace(/RAW/ig,"");
            supportStr += "|NVARCHAR2\\(\\d+\\)";
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
/**--------------------------------------添加行数据 end-----------------------------**/

/**--------------------------------------向表格内添加数据 begin----------------------------*/
function copyColumnData(data,rowIndex){
    if(lastTableType==3&&rowIndex<=3){//维度接口表
        $("_colName" + rowIndex).disabled=true;
        $("_colNullabled" + rowIndex).disabled=true;
        $("_defaultVal" + rowIndex).disabled=true;
        $("_isPrimary" + rowIndex).disabled=true;
        $("_colBusType" + rowIndex).disabled=true;
        $("_dimInfo" + rowIndex).disabled=true;
    }

    data.colName&&dwr.util.setValue("_colName" + rowIndex, data.colName);
    if(data != null){
        data.colId!=null&&data.colId!=undefined&&dwr.util.setValue("_colId" + rowIndex, data.colId);
    }
    
    (data.datatype||data.colDatatype)&&dataTypeCombo[rowIndex]&&dataTypeCombo[rowIndex].setComboText(data.datatype||data.colDatatype);
    data.colNullabled!=null &&data.colNullabled!=undefined&&dwr.util.setValue("_colNullabled" + rowIndex, data.colNullabled);
    data.defaultVal!=null&&data.defaultVal!=undefined&&dwr.util.setValue("_defaultVal" + rowIndex, data.defaultVal);
    data.isPrimary!=null&&data.isPrimary!=undefined&&dwr.util.setValue("_isPrimary" + rowIndex, data.isPrimary);
    data.colNameCn!=null&&data.colNameCn!=undefined&&dwr.util.setValue("_colNameCn" + rowIndex, data.colNameCn);
    data.colBusComment&&dwr.util.setValue("_colBusComment" + rowIndex, data.colBusComment);
    data.dimLevel!=null&&data.dimLevel!=undefined&&dwr.util.setValue("_dimLevel" + rowIndex, data.dimLevel);
    data.dimColId!=null&&data.dimColId!=undefined&&dwr.util.setValue("_dimColId" + rowIndex, data.dimColId);
    data.dimTableId!=null&&data.dimTableId!=undefined&&dwr.util.setValue("_dimTableId" + rowIndex, data.dimTableId);
    data.dimTypeId!=null&&data.dimTypeId!=undefined&&dwr.util.setValue("_dimTypeId" + rowIndex, data.dimTypeId);
    //如果是维度，还需设置维度的显示信息。
    if(data.colBusType == 0){
    	$("_colBusType" + rowIndex).value = 0;
        dwr.util.setValue("_dimInfo" + rowIndex,
                data.dimTableName + "," + data.dimColName + "," + data.dimTypeName + "," + data.dimLevel);
    }
}
/**--------------------------------------向表格内添加数据 end----------------------------*/

/**---------------------------------------打开注释面板 begin-------------------------------------*/
function openComment(img) {
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
}

var clearComment = function(img,rowIndex) {
    var input = $("_dimInfo" + img.parentNode.parentNode._index);
    input.value = "";
    $("_colBusType" + rowIndex).value = 1;
    $("_dimLevel" + rowIndex).value = null;
    $("_dimColId" + rowIndex).value = null;
    $("_dimTableId" + rowIndex).value = null;
    $("_dimTypeId" + rowIndex).value = null;
};
/**---------------------------------------打开注释面板 end-------------------------------------*/


/**-------------------------------------加载维度信息 begin ----------------------------------------*/
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
//选择维度
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
        var dimTableId=dwr.util.getValue("_dimTableId" + dimWindow._rowIndex);
        queryDimGrid.clearAll();
        param.setParam(0,{dimTableId:dimTableId},true);
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
                dwrCaller.executeAction("queryDimType", rId,  function(data){
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
        ok.style.marginLeft = "210px";
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
            if(rowData)
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
    dimWindow._rowIndex = rowIndex;
    queryDimInfo();
    dimWindow.setModal(true);

    dimWindow.show();
}
/**-------------------------------------加载维度信息 end ----------------------------------------*/

/**-------------------------------------加载维度树形结构 begin-----------------------------------*/

/**
 * 添加查询表分类Action
 */
dwrCaller.addAutoAction("queryTableGroup", "TableApplyAction.queryTableGroup",{
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
    this.tree = tree;
    this.showGroupTree = function(tableType){
        dwrCaller.executeAction("queryTableGroup", tableType, function(data){
            treeData[tableType] = data;
            var childs=tree.getAllSubItems(0);
            if(childs){
                var childIds=childs.split(",");
                for(var i=0;i<childIds.length;i++){
                    tree.deleteItem(childIds[i]);
                }
            }
            tree.loadJSONObject(data);

        })
    }
}
/**-------------------------------------加载维度树形结构 end-----------------------------------*/

//删除当前行
function removeRow(img){
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
function moveUp(img,moveUp,moveDown){
    //获取其上一个TR节点
    var tr = img.parentNode.parentNode;
    if(tr.previousSibling.offsetHeight){
        tr.parentNode.insertBefore(tr, tr.previousSibling);
    }
}

/**
 * 向下移动一行
 * @param {Object} img
 * @param {Object} moveUp
 * @param {Object} moveDown
 */
function moveDown(img,moveUp,moveDown){
    //获取其上一个TR节点
    var tr = img.parentNode.parentNode;
    var tempNode = tr.nextSibling;
    if(tempNode.nextSibling){
        tr.parentNode.insertBefore(tr, tempNode.nextSibling);
    }
}

/**
 * 数据合法性验证，表单验证和表格验证。
 */
function validate(){
    //提交数据前进行数据验证。
    var validateRes=true;
    validateRes=validateRes&&dhtmlxValidation.validate("updataTableBase_Form");
    var tableObj = $("_clumnContentTable");
    if(tableObj.rows.length==2){
        dhx.alert("您至少要新增一个列配置！");
        return false;
    }
    for(var i = 1; i < tableObj.rows.length - 1; i++){
        validateRes=dhtmlxValidation.validate(tableObj.rows[i])&&validateRes;
    }
    return validateRes;
}

//验证列信息
function validateCol(){
    var validateRes=true;
    var tableObj = $("_clumnContentTable");
    for(var i = 1; i < tableObj.rows.length - 1; i++){
        validateRes=dhtmlxValidation.validate(tableObj.rows[i])&&validateRes;
    }
    return validateRes;
}

/**
 * 获取表单数据
 */
function getFormData(){
    //获取基本信息表单数据
    var tableData = Tools.getFormValues("updataTableBase_Form");
    //表状态属于修改状态。
    tableData.tableState =1 ;
    tableData.tableId = tableId;
    var columnData = [];
    //表状态属于0
    tableData.tableVersion = tableVersion;
    tableData.tableTypeId=2;//维度类型。
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
        if(!rowData.colBusComment&&$("_colBusComment"+i)){
            rowData.colBusComment=$("_colBusComment"+i).value;
        }
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
                break;
            }
            default:
                break;
        }
        rowData.colNullabled=rowData.colNullabled?1:0;
        rowData.isPrimary=rowData.isPrimary?1:0;
        rowData.tableId = tableId;
        columnData.push(rowData)
    }
    var res = {};
    res.tableData = tableData;
    res.columnDatas = columnData;
    return res;
}

/***
 * 检验sql是否合法
 */
dwrCaller.addAutoAction("generateSql", "TableApplyAction.generateSql");
dwrCaller.isDealOneParam("validate",false);
function checkUpSql(){
    if(validate()){	//表单数据合法
        //开始验证生成的SQL合法性
        var formData = getFormData();
        dwrCaller.executeAction("generateSql",formData,function(sql){
            var dhxWindow = new dhtmlXWindows();
            dhxWindow.createWindow("sqlWindow", 0, 0, 450, 350);
            var sqlWindow = dhxWindow.window("sqlWindow");
            sqlWindow.stick();
            sqlWindow.setModal(true);
            sqlWindow.setDimension(550, 350);
            sqlWindow.button("minmax1").hide();
            sqlWindow.button("park").hide();
            sqlWindow.button("stick").hide();
            sqlWindow.button("sticked").hide();
            sqlWindow.center();
            sqlWindow.denyResize();
            sqlWindow.denyPark();
            sqlWindow.setText("SQL预览");
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
        })
    }
}

/**
 * 表单提交
 */

//注册修改ACTION
dwrCaller.addAutoAction("updateTableInfo","MaintainRelAction.updataTableBaseInfoByTableId");
//注册查询实例表名action
var _dwrCallTableName = new biDwrCaller({
    queryTableName:{methodName:"TableViewAction.queryTableName",
        converter:new dhtmlxComboDataConverter({
            valueColumn:"tableInstId",textColumn:"tableName",isAdd:true
        })}
});

/**
 * 提交表单
 * @return {TypeName}
 */
function submitValidate(){
//	if(updateCount > 0){
//		dhx.alert("您修改的表类正在审核，请关闭修改界面！");
//		return false;
//	}
    if(validate()){	//表单数据合法
        //获取界面数据
        var formData = getFormData();
        //检查提交信息是否有变化
        MaintainRelAction.checkIsSame(formData,function(r){
            if(r){
                dhx.alert("该表类信息没有发生变化，不能提交！");
            }else{
                dwrCaller.executeAction("updateTableInfo",{formData:formData},function(data){
                    if(data == 0){
                        dhx.alert("保存成功");
//                clearTablData();
//                clearAllColumnData();
                        updateCount ++;
                        return false;
                    }else if(data==2){
                        dhx.alert("该基本信息已经在审核中，不能再提交，谢谢！");
                        return false;
                    }else{
                        dhx.alert("修改失败，请重新再试！");
                        return false;
                    }
                });
            }
        });
    }
}

/**
 * 重置表单数据
 */
function resetFormData(){
    //清空表单基本信息表单
    document.getElementById("updataTableBase_Form").reset();
    //清空列信息
    var tableObj = $("_clumnContentTable");
    for(var rowIndex = 1; rowIndex < tableObj.rows.length - 1; rowIndex++){
        var row=  tableObj.rows[rowIndex--];
        row.parentNode.removeChild(row);
    }
    //清空下拉菜单的值
    $("table_attType").innerHTML = "";
    //重新加载基本信息
    initBaseInfo();
    //重新加载列信息
    initBaseTableInfo(true);
}
/**------------------------------------openMetaTable 打开元数据 begin--------------------- */
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

/**------------------------------openMetaTable 打开元数据 end-----------------------*/

/**------------------------------openSystemTable打开系统表 begin---------------------*/

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

var  clearTablData=function(){
    dwr.util.setValue($("_tableName"),"");
    dwr.util.setValue($("_tableNameCn"),"");
    dwr.util.setValue($("_tableType"),"");
    dwr.util.setValue($("_tableGroup"),"");
    dwr.util.setValue($("_tableGroupId"),"");
    dwr.util.setValue($("_tableSpace"),"");
    dwr.util.setValue($("_partitionSql"),"");
    dwr.util.setValue($("_tableBusComment"),"");
    dwr.util.setValue($("_partitionSql"),"");
    dwr.util.setValue($("_dataSource"),"");
    dwr.util.setValue($("_tableOwner"),"");
}
var clearAllColumnData=function(){
    var tableObj = $("_clumnContentTable");
    for(var rowIndex = 1; rowIndex < tableObj.rows.length - 1; rowIndex++){
        var row=  tableObj.rows[rowIndex--];
        row.parentNode.removeChild(row);
    }
}
/**------------------------------openSystemTable打开系统表 end---------------------*/
dhx.ready(initBasicInfo);