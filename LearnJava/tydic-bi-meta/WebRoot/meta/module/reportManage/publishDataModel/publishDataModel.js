/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        publishDataManage.js
 *Description：
 *       发布数据模型，包括：查询发布数据、发布数据、查看发布数据、修改发布数据..etc
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        tanwc
 *modifier:
 * 		  李国民
 ********************************************************/

/**
 * 发布数据模型类
 *
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();

var publishDataModel = function() {
    var p_self = this;
    var dhxWindow = new dhtmlXWindows();

    p_self.dataSourceId 	= null;
    p_self.userName 		= null;
    p_self.levelTypeId 		= null;
    p_self.businessTypeId 	= null;
    p_self.keyword 			= null;

    p_tipLength = 4;	//字符截断的长度
   	//表类字段为指标时，可以选择发布时的字段类型
    var colBusTypeArray = [{'value':'1','name':'指标'},{'value':'2','name':'标识'}];
    var localVariable = "{LOCAL_CODE}";	//地域宏变量标识
	var macroVariable = ["{YY}","{YYYY}","{YYYYMM}","{YYYYMMDD}"]; //时间宏变量
	var localCheck = false;		//表类是否为地域宏变量
	var dateCheck = false;		//表类是否为时间宏变量
	var dateType = 0;			//宏变量类型，1为年，2为月，3为天
    var isD = 11;
    var isM = 22;

    /**
     * 定义全局变量
     */
    p_self.layout = new dhtmlXLayoutObject(document.getElementById('publishDataModel'), "2E");
    p_self.layout.hideConcentrate();

    p_self.dwrCaller 		= new biDwrCaller();
    p_self.searchHtml 		= $('issueDataModel_search');			//缓存html
    p_self.issueDataModel 	= $('issueDataModel');					//缓存数据模型html
    p_self.modifyHtml 		= $('issueDataModel_modify');			//缓存数据模型html
    p_self.offlineHtml 		= $('issueDataModel_offline');			//缓存数据模型html

    var config = {
        idColumnName:"dataSourceId",
        filterColumns:["","dataSourceName","sysName","dataSourceUser"],
        userData:function(rowIndex, rowData) {
            var userData = {};
            userData["dataSourceName"] = rowData["dataSourceName"];
            userData["sysName"] = rowData["sysName"];
            userData["dataSourceUser"] = rowData["dataSourceUser"];
            return userData;
        },
        cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
            if (columnName == '_buttons') {
                return "getButtons";
            }
            return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
        }
    }

    //报表模型信息
    var dataModelConfig = {
        idColumnName:"issueId",
        filterColumns:["tableAlias","tableName","showDataCycle","auditType","issueNote","_buttons"],
        userData:function(rowIndex, rowData) {
            var userData = {};
            userData["dataSourceName"] = rowData["dataSourceName"];
            userData["tableName"] = rowData["tableName"];
            userData["tableOwner"] = rowData["tableOwner"];
            userData["tableId"] = rowData["tableId"];
            userData["tableKeyword"] = rowData["tableKeyword"];
            userData["issueId"] = rowData["issueId"];
            userData["tableAlias"] = rowData["tableAlias"];
            userData["auditType"] = rowData["auditType"];
            userData["issueState"] = rowData["issueState"];
            userData["issueNote"] = rowData["issueNote"];
            userData["dataCycle"] = rowData["dataCycle"];
            userData["maxDate"] = rowData["maxDate"];
            userData["startTime"] = rowData["startTime"];
            userData["isListing"] = rowData["isListing"];
            userData["subscribeType"] = rowData["subscribeType"];
            userData["auditProp"] = rowData["auditProp"];
            return userData;
        },
        cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
            if (columnName == '_buttons') {
                return "getButtons";
            }
            if (columnName == 'auditType') {
                if(rowData["auditType"] == 0) {
                    return "自动";
                }else {
                    return "人工";
                }
            }
            return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
        }
    }

    var tableTypeConfig = {
        idColumnName:"tableId",
        filterColumns:["","tableName","dataSourceName","tableOwner","tableTypeName","tableGroupName","tableBusComment"],
        userData:function(rowIndex, rowData) {
            var userData = {};
            userData["tableName"] 		= rowData["tableName"];
            userData["dataSourceName"] 	= rowData["dataSourceName"];
            userData["tableGroupName"] 	= rowData["tableGroupName"];
            userData["tableBusComment"] = rowData["tableBusComment"];
            userData["tableOwner"] 		= rowData["tableOwner"];
            userData["tableId"] 		= rowData["tableId"];
            userData["tableTypeId"] 	= rowData["tableTypeId"];
            return userData;
        },
        cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
            if (columnName == '_buttons') {
                return "getButtons";
            }
            return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
        }
    }

    //发布模型的时候的表类字段信息
    var insertColInfo = {
        idColumnName:"colId",
        filterColumns:["","colName","colAlias","filedType","colBusType","amountFlag","dimLevels","dimCodes"],
        userData:function(rowIndex, rowData) {
            var userData = {};
            userData["colId"] = rowData["colId"];
            userData["colName"] = rowData["colName"];
            userData["colAlias"] = rowData["colAlias"];
            userData["colBusType"] = rowData["colBusType"];
            userData["dimTableId"] = rowData["dimTableId"];
            userData["dimTypeId"] = rowData["dimTypeId"];
            userData["dimLevels"] = rowData["dimLevels"];
            userData["dimLevelIds"] = rowData["dimLevelIds"];
            userData["oldLevel"] = rowData["oldLevel"];
            return userData;
        },
        cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
            if(columnName == 'colBusType') {//字段属性转换
                if(rowData["colBusType"] == 0) {		//0为维度
                	if(rowData["dimTableId"]==dimZoneId){
                		return "地域维度";
                	}else if(rowData["dimTableId"]==dimDateId){
                		return "时间维度";
                	}else{
                   		return "维度";
                	}
                }else if(rowData["colBusType"] == 1) {		//1为指标
                	var str = "<select onclick='cancelBubble()' id='colBusType"+rowData["colId"]+"' identify='"+rowData["colId"]+"' onChange='changeBusType(this)'>";
                	for(var m=0;m<colBusTypeArray.length;m++){
                		str += "<option value='"+colBusTypeArray[m].value+"'>"+colBusTypeArray[m].name+"</option>";
                	}
                	str += "</select>";
                    return str;
                }
            }
            if(columnName == 'colAlias') {//别名转换
                if(rowData["colAlias"] ==	null ){
                    return "<input onclick='cancelBubble()' id='alias_row_"+rowData["colId"]+"' type='text' value='' style='height:15px' />";
                }else {
                    return "<input onclick='cancelBubble()' id='alias_row_"+rowData["colId"]+"' type='text' value='"+rowData["colAlias"]+"' style='height:15px' />";
                }
            }
            if(columnName == 'filedType') {	//字段分类
                return '<input type="text" id="field_'+rowData["colId"]+'" onClick ="loadFieldType(this);" style="height:15px;width:150px;" readonly="readonly" />';
            }
            if(columnName == 'amountFlag') {//合计转换
                if(rowData["colBusType"] == 1) {
                    return "<input id='amountFlag_"+rowData["colId"]+"' type='checkbox' checked='checked' />&nbsp;";
                }
            }
            if(columnName == 'dimLevels') {//支撑维度层次转换
                if(rowData["colBusType"] != 0) {
                    return '';
                }
                var html = "";
                var array = "";
                if(rowData["dimLevels"] != null) {
                    array = rowData["dimLevels"].split(',');
                }
                var dimLevelIds = "";
                if(rowData["dimLevels"] != null) {
                    dimLevelIds = rowData["dimLevelIds"].split(',');
                }
                /*********根据时间宏变量或者地域宏变量过滤维度开始***********/
                var j=0;
                var endArray = new Array();
                var endDimLevelsIds = new Array();
                for(var i=0;i<array.length;i++) {
                	if((localCheck&&rowData["dimTableId"]==dimZoneId)&&(i==0&&array.length!=1)){
                		continue;
                	}
                	if((dateCheck&&rowData["dimTableId"]==dimDateId)&&(i<(dateType*1-1)&&(i!=array.length-1))){
                		continue;
                	}
                	endArray.push(array[i]);
                	endDimLevelsIds.push(dimLevelIds[i]);
                	j++;
                }
                array = endArray;
                dimLevelIds = endDimLevelsIds;
                /*********根据时间宏变量或者地域宏变量过滤维度结束***********/
                for(var i=0;i<array.length;i++) {
                    if(i == array.length-1) {
                        html += "<input noCancel='true' identify="+dimLevelIds[i]+" checked='checked' disabled='disabled' identify="+dimLevelIds[i]+" value="+array[i]+" name='level_"+rowData["colId"]+"' " +
                                "id='level_checked_"+rowData["colId"]+"_"+i+"' rowData='"+rowData["colId"]+"' " +
                                "curLevel='"+i+"' allLevel='"+array.length+"' " +
                                "type='checkbox' /><label title='"+array[i]+"' for='level_checked_"+rowData["colId"]+"_"+i+"'>"+Tools.toEllipsis(array[i],null,p_tipLength)[0]+"</label>";
                    }else {
                        html += "<input noCancel='false' identify="+dimLevelIds[i]+" value="+array[i]+" name='level_"+rowData["colId"]+"' " +
                                "id='level_checked_"+rowData["colId"]+"_"+i+"' rowData='"+rowData["colId"]+"' " +
                                "curLevel='"+i+"' allLevel='"+array.length+"' onClick='bindLevelAction(this);' " +
                                "type='checkbox' /><label title='"+array[i]+"' for='level_checked_"+rowData["colId"]+"_"+i+"'>"+Tools.toEllipsis(array[i],$('level_checked_"+rowData["colId"]+"_"+i+"'),p_tipLength)[0]+"</label>";
                    }
                }
                return html;
            }
            if(columnName == 'dimCodes') {
                if(rowData["colBusType"] == 0) {//维度
                	return '<input readonly="readonly"  type="text"  id="dimCodes_'+rowData["colId"]+'" treeLevels="" name="'+rowData["colId"]+'" ' +
                	'onClick ="loadDimCodes(this,'+rowData["dimTableId"]+','+rowData["dimTypeId"]+');" style="height:15px" />';
                }else if(rowData["colBusType"] == 1){	//指标
                    return '<input id="dimCodes_'+rowData["colId"]+'" type="text" readonly="readonly" name="dimCodeNotDim" onClick ="setDimCodes(this);" style="height:15px;display:none;" />&nbsp;';
                }
            }
            return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
        }
    }

    //查看已发布字段信息
    var pubColInfo = {
        idColumnName:"colId",
        filterColumns:["colName","colAlias","colTypeId","colBusType","amountFlag","dimLevels","dimCodes"],
        userData:function(rowIndex, rowData) {
            var userData = {};
            return userData;
        },
        cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
            if(columnName == 'colAlias') {//别名转换
            	return rowData["colAlias"]?rowData["colAlias"]:"";
            }
            if(columnName == 'colTypeId') {//字段分类
            	return rowData["colTypeName"]?rowData["colTypeName"]:"";
            }
            if(columnName == 'colBusType') {//字段属性转换
                if(rowData["colBusType"] == 0) {		//0为维度
                	if(rowData["dimTableId"]==dimZoneId){
                		return "地域维度";
                	}else if(rowData["dimTableId"]==dimDateId){
                		return "时间维度";
                	}else{
                   		return "维度";
                	}
                }else if(rowData["colBusType"] == 1) {		//1为指标
                    return "指标";
                }else {
                    return "标识";
                }
            }
            if(columnName == 'amountFlag') {//合计转换
                if(rowData["colBusType"] == 1) {
                    if(rowData["amountFlag"] ==	'1' ){
                        return "<input disabled='true' type='checkbox' checked='true'/>";
                    }else {
                        return "<input disabled='true' type='checkbox' />";
                    }
                }else {
                    return "";
                }
            }
           	if(columnName == 'dimLevels') {//支撑维度粒度转换
                if(rowData["colBusType"] != 0) {
                    return '';
                }
                var html = "";
                var array = "";
                if(rowData["dimLevels"] != null) {
                    array = rowData["dimLevels"].split(',');
                }
                var dimLevelIds = "";
                if(rowData["dimLevels"] != null) {
                    dimLevelIds = rowData["dimLevelIds"].split(',');
                }
                var allDimLevels = "";		//发布时，选中的id
                if(rowData["allDimLevels"] != null) {
                    allDimLevels = rowData["allDimLevels"].split(',');
                }
                for(var i=0;i<array.length;i++) {
                	var ischeck = false;
                	for(var m=0;m<allDimLevels.length;m++){
                		if(dimLevelIds[i] == allDimLevels[m]){
                			ischeck = true;
                			break;
                		}
                	}
                    if(ischeck) {		//如果未true，表示已经被选中过
                        html += "<input noCancel='true' identify="+dimLevelIds[i]+" checked='checked' disabled='disabled' " +
                        		"identify="+dimLevelIds[i]+" value="+array[i]+" name='level_"+rowData["colId"]+"' " +
                                "id='level_checked_"+rowData["colId"]+"_"+i+"' rowData='"+rowData["colId"]+"' " +
                                "curLevel='"+i+"' allLevel='"+array.length+"' " +
                                "type='checkbox' /><label title='"+array[i]+"' for='level_checked_"+rowData["colId"]+"_"+i+"'>"+Tools.toEllipsis(array[i],null,p_tipLength)[0]+"</label>";
                    }else {
                        html += "<input noCancel='false' identify="+dimLevelIds[i]+" disabled='disabled' value="+array[i]+" " +
                        		"name='level_"+rowData["colId"]+"' " +
                                "id='level_checked_"+rowData["colId"]+"_"+i+"' rowData='"+rowData["colId"]+"' " +
                                "curLevel='"+i+"' allLevel='"+array.length+"' onClick='bindLevelAction(this);' " +
                                "type='checkbox' /><label title='"+array[i]+"' for='level_checked_"+rowData["colId"]+"_"+i+"'>"+Tools.toEllipsis(array[i],$('level_checked_"+rowData["colId"]+"_"+i+"'),p_tipLength)[0]+"</label>";
                    }
                }
                return html;
            }
            if(columnName == 'dimCodes') {	//维度值
				if(rowData["colBusType"] == 0) {//维度
	            	return rowData["dimNames"]?("除了："+rowData["dimNames"]+"之外"):"";
				}else{
	            	return rowData["dimCodes"]?rowData["dimCodes"]:"";
				}
            }
            return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
        }
    }

    //修改发布字段信息
    var modifyPubColInfo = {
        idColumnName:"colId",
        filterColumns:["checks","colName","colAlias","colTypeId","colBusType","amountFlag","dimLevels","dimCodes"],
        userData:function(rowIndex, rowData) {
            var userData = {};
            userData["columnId"] = rowData["columnId"];
            userData["colId"] = rowData["colId"];
            userData["colName"] = rowData["colName"];
            userData["colAlias"] = rowData["colAlias"];
            userData["colBusType"] = rowData["colBusType"];
            userData["dimTableId"] = rowData["dimTableId"];
            userData["dimTypeId"] = rowData["dimTypeId"];
            userData["allDimLevels"] = rowData["allDimLevels"];
            userData["dimCodes"] = rowData["dimCodes"];
            userData["colTypeId"] = rowData["colTypeId"];
            userData["colTypeName"] = rowData["colTypeName"];
            userData["amountId"] = rowData["amountId"];
            userData["dimLevels"] = rowData["dimLevels"];
            userData["dimLevelIds"] = rowData["dimLevelIds"];
            userData["dimNames"] = rowData["dimNames"];
            userData["isUsed"] = rowData["isUsed"];
            return userData;
        },
        cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
        	//alert(rowData["isUsed"]);
            if(columnName == 'checks') {
                if(rowData["columnId"] == null ){	//如果列id为空，表示以前没有选中过的
                    return "<input type='checkbox' name='modify_select' id='modify_select_"+rowData["colId"]+"' />";
                }else {
                	if(rowData["isUsed"] == 0) {
                		return "<input type='checkbox' name='modify_select'  checked='checked' id='modify_select_"+rowData["colId"]+"' />";
                	}else {
                    	return "<input type='checkbox' name='modify_select' disabled='disabled' checked='checked' id='modify_select_"+rowData["colId"]+"' />";
                    }
                }
            }
            if(columnName == 'colBusType') {//字段属性转换
                if(rowData["colBusType"] == 0) {		//0为维度
                	if(rowData["dimTableId"]==dimZoneId){
                		return "地域维度";
                	}else if(rowData["dimTableId"]==dimDateId){
                		return "时间维度";
                	}else{
                   		return "维度";
                	}
                }else if(rowData["colBusType"] == 1 ) {		//1为指标
	                if(rowData["columnId"] == null || rowData["isUsed"] == 0){	//如果列id为空，表示以前没有选中过的
		                var str = "<select onclick='cancelBubble()' id='colBusType"+rowData["colId"]+"' identify='"+rowData["colId"]+"' onChange='changeBusType(this)'>";
	                	for(var m=0;m<colBusTypeArray.length;m++){
	                		var selectStr = "";
	            			if(rowData["colBusType"] == colBusTypeArray[m].value){
	            				selectStr = "selected='selected'";
	            			}
	                		str += "<option value='"+colBusTypeArray[m].value+"' "+selectStr+">"+colBusTypeArray[m].name+"</option>";
                		}
	                	str += "</select>";
	                    return str;
	                }else{
                    	return "指标";
                    }
                }else {
                	if(rowData["columnId"] == null || rowData["isUsed"] == 0){	//如果列id为空，表示以前没有选中过的
		                var str = "<select onclick='cancelBubble()' id='colBusType"+rowData["colId"]+"' identify='"+rowData["colId"]+"' onChange='changeBusType(this)'>";
	                	for(var m=0;m<colBusTypeArray.length;m++){
	                		var selectStr = "";
	            			if(rowData["colBusType"] == colBusTypeArray[m].value){
	            				selectStr = "selected='selected'";
	            			}
	                		str += "<option value='"+colBusTypeArray[m].value+"' "+selectStr+">"+colBusTypeArray[m].name+"</option>";
                		}
	                	str += "</select>";
	                    return str;
	                }else {
                    	return "标识";
                    }
                }
            }
            if(columnName == 'colAlias') {//别名转换
                if(rowData["colAlias"] == null){
                    return "<input onclick='cancelBubble()' type='text' id='alias_row_"+rowData["colId"]+"'/>";
                }else {
                    return "<input onclick='cancelBubble()' type='text' id='alias_row_"+rowData["colId"]+"' value='"+rowData["colAlias"]+"' />";
                }
            }
            if(columnName == 'colTypeId') {		//字段分类
                if(rowData["colTypeId"] == null) {
                    return '<input type="text" readonly="readonly" id="field_'+rowData["colId"]+'" onClick ="loadFieldType(this);" />';
                }else {
                    return '<input type="text" readonly="readonly" class="'+rowData["colTypeId"]+'" identify='+rowData["colTypeId"]+' ' +
                    'value='+rowData["colTypeName"]+' id="field_'+rowData["colId"]+'" onClick ="loadFieldType(this);" />';
                }
            }
            if(columnName == 'amountFlag') {//合计转换
                if(rowData["colBusType"] == 1) {	//如果为指标
                    if(rowData["amountFlag"] ==	'1' ){
                        return "<input id='amountFlag_"+rowData["colId"]+"' type='checkbox' checked='true'/>";
                    }else {
                        return "<input id='amountFlag_"+rowData["colId"]+"' type='checkbox' />";
                    }
                }else {
                    return "<input id='amountFlag_"+rowData["colId"]+"' type='checkbox' style='display:none;'/>";
                }
            }
            
            if(columnName == 'dimLevels') {//支撑维度粒度转换
                if(rowData["colBusType"] != 0) {
                    return '';
                }
                var html = "";
                var array = "";
                if(rowData["dimLevels"] != null) {
                    array = rowData["dimLevels"].split(',');
                }
                var dimLevelIds = "";
                if(rowData["dimLevels"] != null) {
                    dimLevelIds = rowData["dimLevelIds"].split(',');
                }
                var allDimLevels = "";		//发布时，选中的id
                if(rowData["allDimLevels"] != null) {
                    allDimLevels = rowData["allDimLevels"].split(',');
                }
                /*********根据时间宏变量或者地域宏变量过滤维度开始***********/
                var j=0;
                var endArray = new Array();
                var endDimLevelsIds = new Array();
                for(var i=0;i<array.length;i++) {
                	if((localCheck&&rowData["dimTableId"]==dimZoneId)&&(i==0&&array.length!=1)){
                		continue;
                	}
                	if((dateCheck&&rowData["dimTableId"]==dimDateId)&&(i<(dateType*1-1)&&(i!=array.length-1))){
                		continue;
                	}
                	endArray.push(array[i]);
                	endDimLevelsIds.push(dimLevelIds[i]);
                	j++;
                }
                array = endArray;
                dimLevelIds = endDimLevelsIds;
                /*********根据时间宏变量或者地域宏变量过滤维度结束***********/
                var dimCodesState = false;
                for(var i=0;i<array.length;i++) {
                	var ischeck = false;
                	for(var m=0;m<allDimLevels.length;m++){
                		if(dimLevelIds[i] == allDimLevels[m]){
                			ischeck = true;
                			break;
                		}
                	}
                    if(ischeck) {		//如果未true，表示已经被选中过
                    	if(rowData["isUsed"] == 0) {
                    		if(i == array.length-1) {
		                        html += "<input noCancel='true' identify="+dimLevelIds[i]+" checked='checked' disabled='disabled' identify="+dimLevelIds[i]+" value="+array[i]+" name='level_"+rowData["colId"]+"' " +
		                                "id='level_checked_"+rowData["colId"]+"_"+i+"' rowData='"+rowData["colId"]+"' " +
		                                "curLevel='"+i+"' allLevel='"+array.length+"' " +
		                                "type='checkbox' /><label title='"+array[i]+"' for='level_checked_"+rowData["colId"]+"_"+i+"'>"+Tools.toEllipsis(array[i],null,p_tipLength)[0]+"</label>";
		                    }else{
		                    	if(dimCodesState) {
			                        html += "<input noCancel='false' identify="+dimLevelIds[i]+" checked='checked' disabled='disabled' value="+array[i]+" name='level_"+rowData["colId"]+"' " +
			                                "id='level_checked_"+rowData["colId"]+"_"+i+"' rowData='"+rowData["colId"]+"' " +
			                                "curLevel='"+i+"' allLevel='"+array.length+"' onClick='bindLevelAction(this);' " +
			                                "type='checkbox' /><label title='"+array[i]+"' for='level_checked_"+rowData["colId"]+"_"+i+"'>"+Tools.toEllipsis(array[i],$('level_checked_"+rowData["colId"]+"_"+i+"'),p_tipLength)[0]+"</label>";
		                        }else {
		                        	 html += "<input noCancel='false' identify="+dimLevelIds[i]+" checked='checked' value="+array[i]+" name='level_"+rowData["colId"]+"' " +
			                                "id='level_checked_"+rowData["colId"]+"_"+i+"' rowData='"+rowData["colId"]+"' " +
			                                "curLevel='"+i+"' allLevel='"+array.length+"' onClick='bindLevelAction(this);' " +
			                                "type='checkbox' /><label title='"+array[i]+"' for='level_checked_"+rowData["colId"]+"_"+i+"'>"+Tools.toEllipsis(array[i],$('level_checked_"+rowData["colId"]+"_"+i+"'),p_tipLength)[0]+"</label>";
									dimCodesState	 = true;
		                        }		                    
		                    }
                    	}else {
	                        html += "<input noCancel='true' identify="+dimLevelIds[i]+" checked='checked' disabled='disabled' " +
	                        		"identify="+dimLevelIds[i]+" value="+array[i]+" name='level_"+rowData["colId"]+"' " +
	                                "id='level_checked_"+rowData["colId"]+"_"+i+"' rowData='"+rowData["colId"]+"' " +
	                                "curLevel='"+i+"' allLevel='"+array.length+"' " +
	                                "type='checkbox' /><label title='"+array[i]+"' for='level_checked_"+rowData["colId"]+"_"+i+"'>"+Tools.toEllipsis(array[i],null,p_tipLength)[0]+"</label>";
						}
                    }else {
	                    if(i == array.length-1) {
	                        html += "<input noCancel='true' identify="+dimLevelIds[i]+" checked='checked' disabled='disabled' identify="+dimLevelIds[i]+" value="+array[i]+" name='level_"+rowData["colId"]+"' " +
	                                "id='level_checked_"+rowData["colId"]+"_"+i+"' rowData='"+rowData["colId"]+"' " +
	                                "curLevel='"+i+"' allLevel='"+array.length+"' " +
	                                "type='checkbox' /><label title='"+array[i]+"' for='level_checked_"+rowData["colId"]+"_"+i+"'>"+Tools.toEllipsis(array[i],null,p_tipLength)[0]+"</label>";
	                    }else {
	                        html += "<input noCancel='false' identify="+dimLevelIds[i]+" value="+array[i]+" name='level_"+rowData["colId"]+"' " +
	                                "id='level_checked_"+rowData["colId"]+"_"+i+"' rowData='"+rowData["colId"]+"' " +
	                                "curLevel='"+i+"' allLevel='"+array.length+"' onClick='bindLevelAction(this);' " +
	                                "type='checkbox' /><label title='"+array[i]+"' for='level_checked_"+rowData["colId"]+"_"+i+"'>"+Tools.toEllipsis(array[i],$('level_checked_"+rowData["colId"]+"_"+i+"'),p_tipLength)[0]+"</label>";
	                    }
                    }
                }
                return html;
            }
            if(columnName == 'dimCodes') {	//维度值
                if(rowData["colBusType"] == 0) {//维度
	                if(rowData["columnId"] == null){	//如果列id为空，表示以前没有选中过的
	                	return '<input readonly="readonly"  type="text"  id="dimCodes_'+rowData["colId"]+'" treeLevels="" name="'+rowData["colId"]+'" ' +
	                	'onClick ="loadDimCodes(this,'+rowData["dimTableId"]+','+rowData["dimTypeId"]+','+rowData["isUsed"]+');" style="height:15px" />';
                	}else{
	                	return '<input readonly="readonly" type="text" id="dimCodes_'+rowData["colId"]+'" name="'+rowData["colId"]+'" ' +
	                	' onClick ="loadDimCodes(this,'+rowData["dimTableId"]+','+rowData["dimTypeId"]+','+rowData["isUsed"]+');" ' +
	                	' value="'+(rowData["dimNames"]?("除了："+rowData["dimNames"]+"之外"):"")+'" ' +
	                	' deDimCodes="'+(rowData["dimCodes"]?rowData["dimCodes"]:"")+'" ' +
	                	' deDimNames="'+(rowData["dimNames"]?("除了："+rowData["dimNames"]+"之外"):"")+'" ' +
	                	' dimCodes="'+(rowData["dimCodes"]?rowData["dimCodes"]:"")+'" ' +
	                	' treeLevels="" style="height:15px" />';
                	}
                }else if(rowData["colBusType"] == 2){	//标识
	                if(rowData["columnId"] == null ){	//如果列id为空，表示以前没有选中过的
	                    return '<input id="dimCodes_'+rowData["colId"]+'" type="text" readonly="readonly" name="dimCodeNotDim" onClick ="setDimCodes(this);" style="height:15px;" />&nbsp;';
	                }else{
	                    return '<input id="dimCodes_'+rowData["colId"]+'" type="text" readonly="readonly" name="dimCodeNotDim" ' +
	                    'onClick ="setDimCodes(this);" value="'+(rowData["dimCodes"]?rowData["dimCodes"]:"")+'" style="height:15px" />';
                    }
                }else{	//指标
	                if(rowData["columnId"] == null || rowData["isUsed"] == 0){	//如果列id为空，表示以前没有选中过的
	                    return '<input id="dimCodes_'+rowData["colId"]+'" type="text" readonly="readonly" name="dimCodeNotDim" onClick ="setDimCodes(this);" style="height:15px;display:none;" />&nbsp;';
	                }else{
                		return '&nbsp;'
	                }
                }
            }
            return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
        }
    }

    //定义action

    var param = new biDwrMethodParam();
    var form = new dhtmlXForm("modelForm4Hidden");
    param.setParamConfig([
        {
            index:0,type:"fun",value:function(){
            var formData=form.getFormData();
            formData.dataSourceId	=	p_self.dataSourceId;
            formData.userName		=	p_self.userName;
            formData.levelTypeId	=	p_self.levelTypeId;
            formData.businessTypeId	=	p_self.businessTypeId;
            formData.keyword		=	p_self.keyword;
            return formData;
        }
        }
    ]);
    p_self.dwrCaller.addAutoAction("queryDataModel", "IssueDataModelAction.queryDataModel",param);
    p_self.dwrCaller.addDataConverter("queryDataModel", new dhtmxGridDataConverter(dataModelConfig));

    p_self.dwrCaller.addAutoAction("queryDataSource", "IssueDataModelAction.queryDataSource");
    p_self.dwrCaller.isShowProcess("queryDataSource", false);
    p_self.dwrCaller.addDataConverter("queryDataSource", new dhtmxGridDataConverter(config));

    p_self.dwrCaller.addAutoAction("getUserNameByDataSourceId", "IssueDataModelAction.getUserNameByDataSourceId");
    p_self.dwrCaller.isShowProcess("getUserNameByDataSourceId", false);

    p_self.dwrCaller.addAutoAction("queryTableGroup", "TableApplyAction.queryTableGroup");
    p_self.dwrCaller.addDataConverter("queryTableGroup", new dhtmxTreeDataConverter({
        idColumn:"tableGroupId",pidColumn:"parGroupId",textColumn:"tableGroupName",
        isDycload:false
    }));
    p_self.dwrCaller.isShowProcess("queryTableGroup", false);

    p_self.loadMenuParam = new biDwrMethodParam();

    //发布时选择的表类信息
    var tableParam = new biDwrMethodParam();
    tableParam.setParamConfig([
        {
            index:0,type:"fun",value:function(){
            var formData=form.getFormData();
            formData.dataSourceId	=	p_self.tableDataSourceId?p_self.tableDataSourceId:'';
            formData.tableOwner		=	p_self.tableUserName?p_self.tableUserName:'';
            formData.tableTypeId	=	p_self.tableLevelType?p_self.tableLevelType:'';
            formData.tableGroupId	=	p_self.tableBusinessType?p_self.tableBusinessType:'';
            formData.keyword		=	p_self.tableSearchKeyword?p_self.tableSearchKeyword:'';
            return formData;
        }
        }
    ]);
    p_self.dwrCaller.addAutoAction("queryTableType", "IssueDataModelAction.queryTableType",tableParam);
    p_self.dwrCaller.addDataConverter("queryTableType", new dhtmxGridDataConverter(tableTypeConfig));

    //查询发布时的字段
    p_self.dwrCaller.addAutoAction("queryTableTypeColInfo", "IssueDataModelAction.queryTableTypeColInfo");
    p_self.dwrCaller.addDataConverter("queryTableTypeColInfo", new dhtmxGridDataConverter(insertColInfo));

    //查询 已发布的字段
    p_self.dwrCaller.addAutoAction("queryPublishColInfo", "IssueDataModelAction.queryPublishColInfo");
    p_self.dwrCaller.addDataConverter("queryPublishColInfo", new dhtmxGridDataConverter(pubColInfo));

   	//查询修改时的字段
    p_self.dwrCaller.addAutoAction("queryModifyPublishColInfo", "IssueDataModelAction.queryModifyPublishColInfo");
    p_self.dwrCaller.addDataConverter("queryModifyPublishColInfo", new dhtmxGridDataConverter(modifyPubColInfo));

    p_self.dwrCaller.addAutoAction("validateName","IssueDataModelAction.validateName");
	p_self.dwrCaller.isShowProcess("validateName",false);

	//发布
    var insertLoadParam = new biDwrMethodParam();
    var dhxForm = new dhtmlXForm("form4Hidden");
    insertLoadParam.setParamConfig([
        {
            index:0,type:"fun",value:function(){
            var formData=dhxForm.getFormData();
            formData.subscribeType	=	p_self.subscribeType;	// 发送方式
            formData.tableId		=	p_self.tableId;			//表类ID
            formData.tableAlias		=	p_self.tableAlias;		//模型别名
            formData.tableKeyword	=	p_self.tableKeyword;	//关键字
            formData.auditType		=	p_self.auditType;		//审核方式
            formData.colData		= 	p_self.colData;			//字段信息
            formData.logInfo		= 	p_self.logInfo;			//日志信息
            formData.dataCycle		=	p_self.dataCycle;		//数据周期
            formData.appRule		=	p_self.appRule;			//应用约定
            formData.auditProp		=	p_self.auditProp;		//审核地域
            formData.effectTime		=	p_self.effectTime;		//生效时间
            formData.dataRemark		=	p_self.dataRemark;		//模型说明
            formData.isListing		=	p_self.isListing;		//是否清单
            
            return formData;
        }
        }
    ]);
    p_self.dwrCaller.addAutoAction("insertPublishDataModel", "IssueDataModelAction.insertPublishDataModel",insertLoadParam);

    var updateLoadParam = new biDwrMethodParam();
    var dhxForm = new dhtmlXForm("form4Hidden");
    updateLoadParam.setParamConfig([
        {
            index:0,type:"fun",value:function(){
            var formData=dhxForm.getFormData();
            formData.subscribeType	=	p_self.subscribeType_modify;	// 发送方式
            formData.issueId		=	p_self.issueId_modify			//模型id
            formData.tableId		=	p_self.tableId_modify;			//表类ID
            formData.tableAlias		=	p_self.tableAlias_modify;		//模型别名
            formData.tableKeyword	=	p_self.tableKeyword_modify;		//关键字
            formData.dataRemark		=	p_self.dataRemark_modify;		//模型说明
            formData.effectTime		=	p_self.effectTime_modify;		//生效时间
            formData.auditType		=	p_self.auditType_modify;		//审核方式
            formData.dataCycle		=	p_self.dataCycle_modify;		//数据周期
            formData.appRule		=	p_self.appRule_modify;			//应用约定
            formData.auditProp		=	p_self.auditProp_modify;		//审核地域
            formData.isListing		=	p_self.isListing_modify;		//是否清单
            formData.colData		= 	p_self.colData_modify;
            formData.updataColData	= 	p_self.updateColData_modify;
            formData.delCols 		=   p_self.delCols;
            return formData;
        }
        }
    ]);
    p_self.dwrCaller.addAutoAction("updatePublishDataModel", "IssueDataModelAction.updatePublishDataModel",updateLoadParam);

    /**
     * 初始化类
     */
    this.init = function() {
    	//把所支持的时间维度从码表中得到
        this.searchPannel._init();
        this.contentPannel._init();

    };

    /**
     * 加载数据源
     * @param relateObj 	关联对象
     * @param containerId 	容器对象，保存数据源展现div的dom的ID ,动态传入避免ID冲突
     */
    this.loadDataSource = function(relateObj,containerId,userByDB,keyWord,typeId){
        var self = this;
        var $dataSource 	= 	$( relateObj );	//数据源DOM
        var $userByDB		=	$( userByDB );	//关联用户DOM
        var $keyWord		=	$( keyWord );	//关键字
        if($(relateObj) == null) {
            alert('参数传入错误，请传入有效的relateObj!');
        }
        
        p_self.dwrCaller.executeAction("queryDataSource",function(data){

            var html = "<option value='-1'>--请选择--</option>";
            for(var i=0;i<data.rows.length;i++) {
                if(i==0) {
                    $dataSource.options.remove(i);
                    $dataSource.options.add(new Option("--请选择--","-1"));
                }
                $dataSource.options.add(new Option(data.rows[i].userdata.dataSourceName,data.rows[i].id));
            }
        });
        
        Tools.addEvent($dataSource, "change", function(){
            var obj1 = $dataSource.options[$dataSource.selectedIndex];
            var dataSourceId 	= $dataSource.options[$dataSource.selectedIndex].value;
            var user 			= $dataSource.options[$dataSource.selectedIndex].id;

            if(dataSourceId == -1) {
                $userByDB.innerHTML = "";
                $userByDB.options.add(new Option("",-1,true,true));
                return;
            }

            p_self.dwrCaller.executeAction("getUserNameByDataSourceId",dataSourceId,function(data){
                var html = '';
                $userByDB.innerHTML = "";
                for(var i=0;i<data.length;i++) {
                    if(i==0) {
                        $userByDB.options.remove(i);
                    }
                    if(data[i].USERNAME.toUpperCase() == data[i].DEFAULT_NAME) {
                        $userByDB.options.add(new Option(data[i].USERNAME,data[i].USERNAME,true,true));
                    }else {
                        $userByDB.options.add(new Option(data[i].USERNAME,data[i].USERNAME));
                    }
                }
            });
        });
    };

    /**
     * 加载层次分类
     * @param relateObj 关联对象，即层次分类数据要绑定到的dom ID
     * @param linkObj	联动对象，即关联对象动作发生时联动的对象 ID
     */
    this.loadLevelType = function(relateObj,linkObj) {
        var self = this;
        levelTypeData=getComboByRemoveValue("TABLE_TYPE",[2,3]);
        levelTypeData=levelTypeData.options||levelTypeData;
        Tools.addOption(relateObj, levelTypeData);
        Tools.addEvent($(relateObj), "change", function(){
            $(linkObj).value="";
            $(linkObj).setAttribute("identify","");
            $(linkObj).style.color = "#000";
        });
    };

    /**
     * 加载业务分类
     * @param relateObj 关联对象，即层次分类数据要绑定到的dom ID
     * @param linkObj	联动对象，即关联对象动作发生时联动的对象 ID
     * @param hiddenId  隐藏域ID ，用于存值   例如：businessTypeId
     */
    this.loadBusinessType = function(relateObj,linkObj,hiddenId) {
        var self = this
        $levelType  	= 	$( 'levelType' )	//层次分类DOM
        $businessType 	= 	$( 'businessType' )	;//业务分类DOM
        var treeData = {};
        var lastTableType = null;

        var loadData = function(target, hide){
            var div = dhx.html.create("div", {
                style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
                        "z-index:1000"
            });
            this.treeDiv = div;
            document.body.appendChild(div);
            target.readOnly = true;
            var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
            tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
            tree.enableThreeStateCheckboxes(true);
            tree.enableHighlighting(true);
            tree.enableSingleRadioMode(true);
            tree.setDataMode("json");
            tree.attachEvent("onDblClick", function(nodeId){
                target.value = tree.getItemText(nodeId);
                hide.value = nodeId;
                target.setAttribute("identify",nodeId);
                div.style.display = "none";
            });
            this.tree = tree
            this.showGroupTree = function(tableType){
                var show = function(){
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
                }
                if(!treeData[tableType]){
                    p_self.dwrCaller.executeAction("queryTableGroup", tableType, function(data){
                        treeData[tableType] = data;
                        show();
                        lastTableType=tableType;
                    });
                } else{
                    show();
                    lastTableType=tableType;
                }
            }
        }
        var businessData = new loadData($(relateObj), $(businessTypeId));
        Tools.addEvent($(relateObj), "click", function(){
            var tableType = dwr.util.getValue(linkObj);
            if(!tableType){
                return;
            }
            businessData.treeDiv.style.width = $(relateObj).offsetWidth + 'px';
            Tools.divPromptCtrl(businessData.treeDiv, $(relateObj), true);
            businessData.treeDiv.style.display = "block";
            businessData.showGroupTree(tableType);
            dhx.env.isIE&&CollectGarbage();
        });
    };
	var modelGrid;
    /**
     * 查询区域
     * @memberOf {TypeName}
     */
    this.searchPannel = {

        _init:function() {
            var self = this;
            self.build();
        },
        build:function() {
            var self = this;
            p_self.layout.cells("a").setText("发布数据查询");
            p_self.layout.cells("a").setHeight(70);
            p_self.layout.cells("a").attachObject('pub_serach');
            self.bindSearchAction();
            p_self.loadDataSource('data_source','dataSourceContainer_1','userByDB');
            p_self.loadLevelType('levelType','businessType');
            p_self.loadBusinessType('businessType','levelType','businessTypeId');
        },
        /**
         * 绑定查询事件
         */
        bindSearchAction:function() {
            var self = this;
            var $dataSource 	= 	$( 'data_source' )	//数据源DOM
            $userByDB		=	$( 'userByDB'	)	//用户名
            $levelType  	= 	$( 'levelType' )	//层次分类DOM
            $businessType 	= 	$( 'businessType' )	//业务分类DOM
            $keyword		= 	$( 'keyword' )		//关键字DOM
            $search			= 	$( 'search' );		//查询按钮DOM

            Tools.addEvent($search,'click',function(){
				var dataSouceId 	= $dataSource.options[$dataSource.selectedIndex].value ;
				var userName		= $userByDB.options[$userByDB.selectedIndex].value;
				var levelTypeId 	= $levelType.options[$levelType.selectedIndex].value;
				var businessTypeId 	= $businessType.getAttribute("identify");
				var keyword = $keyword.value;
				p_self.dataSourceId = dataSouceId;
	            p_self.userName =userName;
		        p_self.levelTypeId = levelTypeId;
		      	p_self.businessTypeId =businessTypeId;
	          	p_self.keyword = keyword;
	          	modelGrid.clearAll();
	          	modelGrid.load(p_self.dwrCaller.queryDataModel, "json");
			});
            
	        //关键字回车事件
	        Tools.addEvent($('keyword'), "keydown", function(event){
	        	if (event.keyCode==13){		//当为回车时，查询信息
					var dataSouceId 	= $dataSource.options[$dataSource.selectedIndex].value ;
					var userName		= $userByDB.options[$userByDB.selectedIndex].value;
					var levelTypeId 	= $levelType.options[$levelType.selectedIndex].value;
					var businessTypeId 	= $businessType.getAttribute("identify");
					var keyword = $keyword.value;
					p_self.dataSourceId = dataSouceId;	
		            p_self.userName =userName;
			        p_self.levelTypeId = levelTypeId;
			      	p_self.businessTypeId =businessTypeId;
		          	p_self.keyword = keyword;
		          	modelGrid.clearAll();
		          	modelGrid.load(p_self.dwrCaller.queryDataModel, "json");
	        	}
	        });
		}
	}
    
	//验证地域宏变量和时间宏变量
	var dateCheckAndLocalCheck = function(tableName){
		var staIndex = tableName.indexOf("{");
		var endIndex = tableName.indexOf("}");
		localCheck = false;
		dateCheck = false;
		if((staIndex!=-1&&endIndex!=-1)&&endIndex>staIndex){
			//表类带宏变量时，如果带地域宏变量，设置验证为true
			if(tableName.indexOf(localVariable)!=-1){
				localCheck = true;
			}
			
			//表类带宏变量时，如果带时间宏变量，设置验证为true
			for(var i=0;i<macroVariable.length;i++){
				var macro = macroVariable[i];
				if(tableName.indexOf(macro)!=-1){
					switch (macro) {
				 		case '{YY}':
				 			dateType = 1;
				            break;
				  		case '{YYYY}':
				 			dateType = 1;
				            break;
				  		case '{YYYYMM}':
				 			dateType = 2;
				            break;
				  		case '{YYYYMMDD}':
				 			dateType = 3;
				            break;
					}
					dateCheck = true;
					break;
				}
			}
		}
	}

    /**
     * 内容面板
     */
    this.contentPannel = {
        /**
         * 初始化内容区域
         */
        _init:function(){
            var self = this;
            self.build();

            self.bindAction2PublishData();
        },
        /**
         * 构建字段分类内容表格
         * @memberOf {TypeName}
         */
        build:function() {
            var self = this;

            p_self.layout.cells("b").hideHeader();
            p_self.layout.hideConcentrate();
            p_self.layout.hideSpliter();//移除分界边框。

            modelGrid = p_self.layout.cells("b").attachGrid();
            modelGrid.setHeader("模型名,表类名,数据周期,审核方式,模型说明,<span>操作</span>" +
                    "<a id='publishData' style='position:relative;left:15px;width: 18px; height: 18px;' href='javascript:void(0)'><span>发布数据模型</span></a>" );
            modelGrid.setInitWidthsP("20,22,10,10,22,16");
            modelGrid.setColAlign("left,left,center,center,left,center");
            modelGrid.setHeaderAlign("center,center,center,center,center,center");
            modelGrid.enableResizing("true,true,true,true,true,true");
            modelGrid.setColTypes("ro,ro,ro,ro,ro,sb");
            modelGrid.setColSorting("na,na,na,na,na,na");
            modelGrid.enableTooltips("true,true,true,true,true,false");
            modelGrid.setColumnIds("tableAlias,tableName,showDataCycle,auditType,issueNote,''");
            modelGrid.setEditable(false);
			modelGrid.enableMultiselect(true);
            modelGrid.init();
            modelGrid.defaultPaging(20);
            modelGrid.load(p_self.dwrCaller.queryDataModel, "json");
            
		    modelGrid.entBox.onselectstart=function(){
		        return true;
		    }
            
            self.addButtons2Layout();	//添加按钮
        },

        /**
         * 刷新内容区域列表数据，用于查询、修改、删除等重新加载列表
         * @memberOf {TypeName}
         */
        refreshList :function(data) {
            var self = this;
            modelGrid.clearAll();
            modelGrid.parse(data,"json");
        },
        /**
         * 刷新表格内容
         */
        reloadGrid	:function() {
            var self = this;
            var $dataSource 	= 	$( 'data_source' )	//数据源DOM
            $userByDB		=	$( 'userByDB'	)	//用户名
            $levelType  	= 	$( 'levelType' )	//层次分类DOM
            $businessType 	= 	$( 'businessType' )	//业务分类DOM
            $keyword		= 	$( 'keyword' )		//关键字DOM

            p_self.dataSourceId		=	$dataSource.options[$dataSource.selectedIndex].value;
            p_self.userName 		= 	$userByDB.options[$userByDB.selectedIndex].value;
            p_self.levelTypeId 		= 	$levelType.options[$levelType.selectedIndex].value;
            p_self.businessTypeId 	= 	$businessType.getAttribute("identify");
            p_self.keyword 			= 	$keyword.value;
			
            p_self.dwrCaller.executeAction('queryDataModel',function(data){
                self.refreshList(data);
            });

        },
        /**
         * 绑定发布数据事件
         */
        bindAction2PublishData:function() {
            var self = this;
            var $publishData = $('publishData');

            Tools.addEvent($publishData,'click',self.openIssueDataModel);
            self.Calendar = new dhtmlXCalendarObject("effectTime");//绑定日期控件到生效时间
            self.Calendar.setDateFormat("%Y-%m-%d %H:%i:%s");
            self.Calendar.setSensitiveRange(new Date(), null);
		    self.Calendar.loadUserLanguage('zh'); //将日历控件语言设置成中文
            self.Calendar.attachEvent("onClick",function(date){
            	Tools.removeClass($('effectTime'),'input_click');
                $('effectTime').setAttribute('ucflag',1);
            });
        },

        /**
         * 打开发布数据模型
         * @paran type
         */
        openIssueDataModel:function() {
            var self = this;

            dhxWindow.createWindow("importWindow", 0, 0, 450, 430);
            var winsize=Tools.propWidthDycSize(1,1,5,10);
            self.importWindow = dhxWindow.window("importWindow");
            self.importWindow.stick();
            self.importWindow.setModal(true);
            self.importWindow.setDimension(winsize.width);
            self.importWindow.button("minmax1").hide();
            self.importWindow.button("park").hide();
            self.importWindow.button("stick").hide();
            self.importWindow.button("sticked").hide();
            self.importWindow.center();
            self.importWindow.denyResize();
            self.importWindow.denyPark();
            self.importWindow.setText("发布数据模型");
            self.importWindow.keepInViewport(true);

            self.importWindow.attachEvent("onClose", function (id, value, checkedState){
                Tools.removeEvent($('selectTableType'), "click", loadTableTypeSelectInfo);
                Tools.removeEvent($('publishCancel'),'click',closePublishModelDialog);
                Tools.removeEvent($('publishSubmit'),'click',publishSubmit);
                return true;
            });

            var layout = new dhtmlXLayoutObject(self.importWindow, "1C");

            layout.cells("a").setHeight(150);
            layout.cells("a").fixSize(false, true);
            layout.cells("a").firstChild.style.height = (layout.cells("a").firstChild.offsetHeight + 5) + "px";
            layout.cells("a").hideHeader();
            layout.cells("a").attachObject(p_self.issueDataModel);
            dhtmlxValidation.addValidation($('dataAlias'),"NotEmpty,MaxLength[100]");
            dhtmlxValidation.addValidation($('_keyword'),"MaxLength[100]");
            dhtmlxValidation.addValidation($('_dataRemark'),"NotEmpty,MaxLength[4000]");

            Tools.removeEvent($('selectTableType'), "click", loadTableTypeSelectInfo);
            Tools.addEvent($('selectTableType'), "click", loadTableTypeSelectInfo);
            loadTableTypeSelectInfo();
            function loadTableTypeSelectInfo(){
    			//初始化值
            	p_self.tableDataSourceId = '';
            	p_self.tableUserName = '';
            	p_self.tableLevelType = '';
            	p_self.tableBusinessType = '';
            	p_self.tableSearchKeyword = '';
                var div = dhx.html.create("div", {
                    style:"display;block;position:absolute;height:430px;width:99.5%;padding: 0;margin: 0;" +
                            "z-index:1000"
                });
                div.innerHTML = '<div style="width:100%;">' +
                        '<div style="height:66px;*height:66px;">' +
                        '<div style="margin-top:10px;margin-left:10px;">'+
                        '数据源：<select style="width:130px;height:20px;margin-right:10px;" id="tableType_dataSource" ></select>'+
                        '所属用户：<select style="width:110px;height:20px;margin-right:10px;" id="tableType_user" ><option value="-1" style="color: #aaa;font-style: italic;">请先选择数据源</option></select>'+
                        '层次分类：<select style="width:100px;height:20px;height:20px;margin-right:10px;" id="tableType_levelType" type="text" value=""><option value="">--请选择--</option></select>'+
                        '业务类型：<input type="text" style="width:110px;height:14px;color: #aaa;" id="tableType_businessType" value="请先选择层次分类" />'+
                        '<input type="hidden" id="tableType_businessTypeId"/><br/>'+
                        '表类名：<input type="text" id="tableType_keyword" style="width:660px;margin-top:5px;height: 17px;"/>'+
                        '&nbsp;&nbsp;<input type="button" value="查询" style="height:21px;" class="poster_btn" id="tableType_search"/>'+
                        '</div>'+
                        '</div>'+
                        '<div id="tableTypeContainer" style="height:270px;*height:270px;"></div>' +
                        '<div style="height:20px;*height:20px;position:relative;left:750px;top:13px;">' +
                        '<input id="selectOK" type="button" class="poster_btn" style="margin-right:10px;" value="确定"/>' +
                        '<input id="selectCancel" type="button" class="poster_btn" value="取消" /></div></div>';

                dhxWindow.createWindow("dhtxWindow1", 0, 0, 450, 430);
                dhtxWindow1 = dhxWindow.window("dhtxWindow1");
                dhtxWindow1.stick();
                dhtxWindow1.setModal(true);
                dhtxWindow1.setDimension(winsize.width);
                dhtxWindow1.button("minmax1").hide();
                dhtxWindow1.button("park").hide();
                dhtxWindow1.button("stick").hide();
                dhtxWindow1.button("sticked").hide();
                dhtxWindow1.center();
                dhtxWindow1.denyResize();
                dhtxWindow1.denyPark();
                dhtxWindow1.setText("选择表类");
                dhtxWindow1.keepInViewport(true);
                var layout1 = new dhtmlXLayoutObject(dhtxWindow1, "1C");
                layout1.cells("a").fixSize(false, true);
                layout1.cells("a").firstChild.style.height = (layout.cells("a").firstChild.offsetHeight + 5) + "px";
                layout1.cells("a").hideHeader();
                layout1.cells("a").attachObject(div);

                self.grid = new dhtmlXGridObject('tableTypeContainer');
                self.grid.setHeader("选择,表类名称,数据源,所属用户,层次分类,业务类型,表类说明");
                self.grid.setInitWidthsP("5,20,10,10,15,15,25");
                self.grid.setColAlign("center,left,left,left,left,left,left");
                self.grid.setHeaderAlign("left,center,center,center,center,center,center");
                self.grid.setColTypes("ra,ro,ro,ro,ro,ro,ro");
                self.grid.setColSorting("na,na,na,na,na,na,na");
                self.grid.setEditable(true);
                self.grid.enableMultiselect(true);
                self.grid.setImagePath(getDefaultImagePath());
                self.grid.setColumnIds("'',tableName,dataSourceName,dataSourceName,tableOwner,tableTypeName,tableGroupName,tableBusComment");
                self.grid.enableTooltips("false,false,false,false,false,false");
                self.grid.init();
    			self.grid.defaultPaging(20);
                self.grid.load(p_self.dwrCaller.queryTableType,"json");
                
                //添加行选中事件
                self.grid.attachEvent("onRowSelect",function(rowId,cellIndex){
        			self.grid.cells(rowId,0).setValue(1);
				});
                
                p_self.loadDataSource('tableType_dataSource','dataSourceContainer_1','tableType_user');
                p_self.loadLevelType('tableType_levelType','tableType_businessType');
                p_self.loadBusinessType('tableType_businessType','tableType_levelType','tableType_businessTypeId');

                //关键字回车事件
		        Tools.addEvent($('tableType_keyword'), "keydown", function(event){
		        	if (event.keyCode==13){		//当为回车时，查询信息
	                    var dataSourceId 	= $('tableType_dataSource').options[$('tableType_dataSource').selectedIndex].value;
	                    var levelType 		= $('tableType_levelType').value;
	                    var businessType 	= $('tableType_businessType').getAttribute("identify");
	                    var keyword 		= $('tableType_keyword').value;
	                    var userName		= '';
	                    if(1*($('tableType_user').selectedIndex) > -1) {
	                        userName		= $('tableType_user').options[$('tableType_user').selectedIndex].value;
	                    }
	                    if(dataSourceId == '-1') {
	                        dataSourceId = '';
	                    }
	            		p_self.tableDataSourceId 	= dataSourceId;		//数据源
	            		p_self.tableUserName 		= userName			//所属用户
	            		p_self.tableLevelType 		= levelType;		//层次分类
	            		p_self.tableBusinessType 	= businessType;		//业务类型
	            		p_self.tableSearchKeyword 	= keyword;			//关键字
	            		
	            		//清空值，重新赋值
	                    self.grid.clearAll();
	                	self.grid.load(p_self.dwrCaller.queryTableType,"json");
		        	}
		        });
                
                //表类选择取消
                Tools.addEvent($('selectCancel'), "click", function(){
                    dhxWindow.window("dhtxWindow1").close();
                });
                
                //添加行双击事件
                self.grid.attachEvent("onRowDblClicked",function(rowId,cellIndex){
                	selectTable(rowId);
				});

                //表类选择确认
                Tools.addEvent($('selectOK'), "click", function(){
                    var selectValue = self.grid.getCheckedRows(0).split(',')[0];//由于数据重复，会取出多个选择值，故去重，待后台数据做去重处理
                    if(selectValue == null || selectValue == '') {
                        dhx.alert('请您先选择表类！');
                    }else {
                    	selectTable(selectValue);
                    }
                });
                
                //表类选中事件
                var selectTable = function(selectValue){
                	var html_mes = self.grid.getUserData(selectValue,"dataSourceName");
                	var tableOwner = "";
                	if(self.grid.getUserData(selectValue,"tableOwner") != null && 
                			self.grid.getUserData(selectValue,"tableOwner") != "null"){
                		tableOwner = self.grid.getUserData(selectValue,"tableOwner");
                		html_mes += "." + tableOwner;
                	}
                	html_mes += "." + self.grid.getUserData(selectValue,"tableName");			
                    $('tableTypeInfo').innerHTML = html_mes;
                    $('tableTypeInfo').setAttribute("tableId",self.grid.getUserData(selectValue,"tableId"));
                    
                	self.tableName = self.grid.getUserData(selectValue,"tableName");		//表类名
					dateCheckAndLocalCheck(self.tableName);	
					
					/********设置发送方式开始********/
					var pushType = getCodeByType("PUSH_TYPE");
					var subTypeHtml = "";
					for(var m=0;m<pushType.length;m++){
						subTypeHtml += '<input name="subscribeType" id="pushType'+m+'" type="checkbox" value="'+pushType[m].value+'"/><span style="position:relative;top:-2px;margin-right:15px;"><label for="pushType'+m+'">'+pushType[m].name+'</label></span>';
					}
					$('subType').innerHTML=subTypeHtml;
					/********设置发送方式结束********/
					
                    //初始化值
                    $('dataAlias').value = "";
                    //默认当前时间
                    var nowToday = new Date();
	    			$('effectTime').value = ChangeTimeToString(nowToday);
            		$('_keyword').value = "";
            		$('_dataRemark').value = "";

                    dhxWindow.window("dhtxWindow1").close();
                    //加载表类字段数据
                    if(!p_self.grid1){
                        p_self.grid1 = new dhtmlXGridObject('issueDataModelTable');
                        p_self.grid1.clearAll();
                        p_self.grid1.setHeader("{#checkBox},字段名,别名,字段分类,字段属性,合计,已支撑维度粒度,维度值");
                        p_self.grid1.setInitWidthsP("4,14,14,14,14,4,22,14");

                        p_self.grid1.setColAlign("center,left,left,left,center,center,left,left");
                        p_self.grid1.setHeaderAlign("left,center,center,center,center,center,center,center");
                        p_self.grid1.setColTypes("ch,ro,ro,ro,ro,ro,ro,ro");
                        p_self.grid1.setColSorting("na,na,na,na,na,na,na,na");
                        p_self.grid1.setEditable(true);
                        p_self.grid1.enableMultiselect(true);
                        p_self.grid1.setImagePath(getDefaultImagePath());
                        p_self.grid1.setColumnIds("'',colName,colAlias,'',colBusType,'',dimLevels,''");
                        p_self.grid1.enableTooltips("false,false,false,false,false,false,false,false");
                        p_self.grid1.init();
                        
                        //grid上文字可以复制操作
					    p_self.grid1.entBox.onselectstart=function(){
					        return true;
					    }
                    }

                    p_self.dwrCaller.executeAction('queryTableTypeColInfo',selectValue,function(data){
                        p_self.grid1.clearAll();		//清空数据
                        p_self.grid1.parse(data,"json");
                        p_self.grid1.checkAll(true);	//选中全部
                    })
                }

                //表类查询
                Tools.addEvent($('tableType_search'), "click", function(){
                    var dataSourceId 	= $('tableType_dataSource').options[$('tableType_dataSource').selectedIndex].value;
                    var levelType 		= $('tableType_levelType').value;
                    var businessType 	= $('tableType_businessType').getAttribute("identify");
                    var keyword 		= $('tableType_keyword').value;
                    var userName		= '';
                    if(1*($('tableType_user').selectedIndex) > -1) {
                        userName		= $('tableType_user').options[$('tableType_user').selectedIndex].value;
                    }
                    if(dataSourceId == '-1') {
                        dataSourceId = '';
                    }
            		p_self.tableDataSourceId 	= dataSourceId;		//数据源
            		p_self.tableUserName 		= userName			//所属用户
            		p_self.tableLevelType 		= levelType;		//层次分类
            		p_self.tableBusinessType 	= businessType;		//业务类型
            		p_self.tableSearchKeyword 	= keyword;			//关键字
            		
            		//清空值，重新赋值
                    self.grid.clearAll();
                	self.grid.load(p_self.dwrCaller.queryTableType,"json");
                });
            }

            //绑定取消事件
            Tools.addEvent($('publishCancel'),'click',closePublishModelDialog);
            function closePublishModelDialog (){
   		 		$("codeDiv").style.display = "none";		//点击取消时取消标识输入层
                dhxWindow.window("importWindow").close();
            }
           	
            //绑定发布事件
            Tools.addEvent($('publishSubmit'),'click',publishSubmit);
            function publishSubmit() {
   		 		$("codeDiv").style.display = "none";		//点击发布时取消标识输入层
                if($('tableTypeInfo').innerHTML == null || $('tableTypeInfo').innerHTML == '') {
                    dhx.alert('请先选择表类！');
                    return;
                }
                //验证信息
                if(!dhtmlxValidation.validate("dataAlias")||!dhtmlxValidation.validate("_dataRemark")) {
	                return;
	            }
                
                //判断模型别名是否存在
                p_self.dwrCaller.executeAction("validateName",$('dataAlias').value,function(boo){
            		if(boo){
            			dhx.alert("模型别名已存在，重新输入，谢谢！");
            			return;
            		}else{
		                p_self.tableId 			= $('tableTypeInfo').getAttribute("tableId");	//表类ID
		                p_self.tableAlias 		= Tools.getInputValue($('dataAlias'));			//模型别名
		                p_self.tableKeyword 	= Tools.getInputValue($('_keyword'));			//关键字
		                p_self.dataRemark 		= Tools.getInputValue($('_dataRemark'));		//模型说明
		                p_self.effectTime		= Tools.getInputValue($('effectTime'));			//生效时间
		                p_self.auditType 		= Tools.getCheckedRadio('auditType').value;		//审核方式
		                p_self.dataCycle 		= Tools.getCheckedRadio('dataCycle').value;		//数据周期
		                p_self.appRule 			= Tools.getCheckedRadio('appRule').value;		//应用约定
		                p_self.auditProp		= Tools.getCheckedRadio('auditProp').value;		//审核地域
		                p_self.isListing 		= Tools.getCheckedRadio('isListing').value;		//是否清单
		                p_self.subscribeType	= "";											//发送方式
					    /*******发送方式得到值开始***********/
		                var length = document.getElementsByName("subscribeType").length;
					    for(var i=0;i<length;i++) {
					        if($('pushType'+i)) {
					            //找到所有选中记录
					            if($('pushType'+i).checked) {
					                p_self.subscribeType += $('pushType'+i).value+",";
					            }
					        }
					    }
					    if(p_self.subscribeType.length>0){
					    	p_self.subscribeType = p_self.subscribeType.substr(0,p_self.subscribeType.length-1);
					    }
					    if(p_self.subscribeType==""){
	            			dhx.alert("请至少选择一种发送方式！");
	            			return;
					    }
					    /*******发送方式得到值结束***********/
					    
		                var checkeds=p_self.grid1.getCheckedRows(0).split(',');
		                var colArray = new Array();
		                if(checkeds =="" ||checkeds == null || checkeds.length < 1 ) {
		                    dhx.alert("您还未选择需要发布的字段！");
		                    return;
		                }
		                
						var checkColAlias = true;	//字段验证正常开关
						var dimZoneCheck = false;		//验证选择字段中是否存在地域维度
						var dimDateCheck = false;		//验证选择字段中是否存在时间维度
		               	//得到发布的字段
		                for(var i=0;i<checkeds.length;i++) {
		                    var colId 		= p_self.grid1.getUserData(checkeds[i],'colId');
		                    var colName 	= p_self.grid1.getUserData(checkeds[i],'colName');		//得到字段列名
		                    var colBusType 	= p_self.grid1.getUserData(checkeds[i],'colBusType');	//字段标识
		                    var dimTableId 	= p_self.grid1.getUserData(checkeds[i],'dimTableId');	//维度表id
		                    var dimTypeId 	= p_self.grid1.getUserData(checkeds[i],'dimTypeId');	//归并类型id
		                    var dimType  	= "";		//维度列类型
		                    
		                    //如果该字段为维度，判断其是否为地域或者时间维度标识
				            if(colBusType == 0) {
				           		if(dimTableId==dimZoneId){
				             		dimType = 2;		//地域维度标识
				             		dimZoneCheck = true;
				              	}else if(dimTableId==dimDateId){
				             		dimType = 1;		//时间维度标识
				             		dimDateCheck = true;
				              	}
				            }
		                    
		                    var colAlias 	= $('alias_row_'+checkeds[i]).value;
							if(colAlias == null || colAlias == ''||colAlias =='undefined'){
								dhx.alert('字段别名不能为空，请输入字段别名！');
								checkColAlias = false;
								break;
							}
		
							//字段分类
		                    var colTypeId = '';
		                    if($('field_'+checkeds[i])){
		                        if($('field_'+checkeds[i]).getAttribute("identify") == null ||$('field_'+checkeds[i]).getAttribute("identify") =='undefined'){
		                            colTypeId = '';
									dhx.alert('字段分类不能为空，请选择字段分类！');
									checkColAlias = false;
									break;
		                        }else {
		                            colTypeId = $('field_'+checkeds[i]).getAttribute("identify");
		                        }
		                    }
		
		                    //维度支撑粒度
		                    var dimLevels = "";
		                    var tmps = document.getElementsByName('level_'+checkeds[i]);
		                    for(var j=0;j<tmps.length;j++) {
		                        if(tmps[j].checked == true) {
		                       		dimLevels += tmps[j].getAttribute('identify')+',';
		                        }
		                    }
		                    if(dimLevels!=""){
		                    	dimLevels = dimLevels.substr(0,dimLevels.length-1);
		                    }
		
		                    //是否合计
		                    var amountFlag = "";
		                    if($('amountFlag_'+checkeds[i])){
		                        if($('amountFlag_'+checkeds[i]).checked == true){
		                            amountFlag = 1;
		                        }else {
		                            amountFlag = 0;
		                        }
		                    }
		
		                    //维度值
		                    var dimCodes = "";
		                    if($("dimCodes_"+checkeds[i])) {
			                    if(colBusType == 0){
			                        dimCodes = $("dimCodes_"+checkeds[i]).getAttribute("dimCodes");
			                    }else{//非维度的时候，用户输入的维度值，此值用于生成指标、标识的条件，具体见原型
			                        colBusType = $('colBusType'+checkeds[i]).value;
			                        if(colBusType==2){	//字段为标识时
			                        	amountFlag = 0;	//是否合计变为不合计
				                    	if($("dimCodes_"+checkeds[i])) {
				                            dimCodes = $("dimCodes_"+checkeds[i]).value;
				                        }
			                        }
			                    }
		                    }
		
		                    var data = {'colId'			:	colId,
		                    	'colName'		:	colName,
		                        'colAlias'		:	colAlias,
		                        'colBusType'	:	colBusType,
		                        'colTypeId'		:	colTypeId,
		                        'tableId'		:	p_self.tableId,
		                        'dimLevels'		:	dimLevels,
		                        'dimCodes'		:	dimCodes,
		                        'selectedFlag'	:	'1',
		                        'amountFlag'	:	amountFlag,
		                        'dimTableId'    :   dimTableId,
		                        'dimTypeId'     :   dimTypeId,
		                        'dimType'		:	dimType
		                    };
		                    colArray.push(data);
		                }
		                //字段验证正常通过后，发布模型
		                if(checkColAlias){	
			                if(!dimDateCheck){	//如果为false，表示发布字段中不存在时间或者地域维度，不允许发布
				                dhx.alert("时间维度必须发布！");
				            	return;
			                }
			                p_self.colData = colArray;
			                p_self.dwrCaller.executeAction('insertPublishDataModel',function(rs){
			                    if(rs){
			                        dhx.alert(p_self.tableAlias+"数据模型发布成功！");
						          	modelGrid.clearAll();
						          	modelGrid.load(p_self.dwrCaller.queryDataModel, "json");
			                        dhxWindow.window("importWindow").close();
			                    }else {
			                        dhx.alert("数据模型发布失败！请联系管理员！");
			                    }
			                });
		                }else{
		                	return;
		                }
		     		}
            	});
            }
        },

        /**
         * 添加按钮功能。for example : 增加同级分类、修改 、删除 等操作
         * @memberOf {TypeName}
         */
        addButtons2Layout:function() {
            var self = this;
            var buttons = {
                look:{name:"look",text:"查看",
                    onclick:function(rowData){
                        self.look(rowData);
                    }},
                modify:{name:"modify",text:"修改",
                    onclick:function(rowData){
                        if(dhx.isArray(rowData)){
                            dhx.alert("一次只能选择一个报表数据进行修改，您多选了！");
                            return;
                        }
                        self.modify(rowData);
                    }
                }
            }
            var buttonTags = ["look","modify"];
            var bottonRoleRow = [];
            for(var i = 0; i < buttonTags.length; i++){
                bottonRoleRow.push(buttonTags[i]);
            }
            //全局方法getButtons
            window["getButtons"] = function(){
                var res = [];
                for(var i = 0; i < bottonRoleRow.length; i++){
                    res.push(buttons[bottonRoleRow[i]]);
                }
                return res;
            };
        },

        /**
         * 查看数据模型
         * @param obj  当前选中的数据模型对象
         */
        look:function(obj) {
            var self = this;

            dhxWindow.createWindow("searchWindow", 0, 0, 450, 430);
            var winsize=Tools.propWidthDycSize(1,1,5,10);
            self.searchWindow = dhxWindow.window("searchWindow");
            self.searchWindow.stick();
            self.searchWindow.setModal(true);
            self.searchWindow.setDimension(winsize.width);
            self.searchWindow.button("minmax1").hide();
            self.searchWindow.button("park").hide();
            self.searchWindow.button("stick").hide();
            self.searchWindow.button("sticked").hide();
            self.searchWindow.center();
            self.searchWindow.denyResize();
            self.searchWindow.denyPark();
            self.searchWindow.setText("查看数据模型");
            self.searchWindow.keepInViewport(true);

            self.searchWindow.attachEvent("onClose", function (id, value, checkedState){
                Tools.removeEvent($('publishCancel_search'),'click',closeSearchPublishModelDialog);
                return true;
            });
            
            var layout = new dhtmlXLayoutObject(self.searchWindow, "1C");
            layout.cells("a").fixSize(false, true);
            layout.cells("a").firstChild.style.height = (layout.cells("a").firstChild.offsetHeight + 5) + "px";
            layout.cells("a").hideHeader();
            layout.cells("a").attachObject(p_self.searchHtml);

            $('tableTypeInfo_search').innerHTML = obj.userdata.dataSourceName+'.'+obj.userdata.tableOwner+'.'+obj.userdata.tableName;
            $('dataAlias_search').innerHTML 	= obj.userdata.tableAlias?obj.userdata.tableAlias:'&nbsp;';		//模型别名
            $('_keyword_search').innerHTML 		= obj.userdata.tableKeyword?obj.userdata.tableKeyword:'&nbsp;';	//关键字
            $('_dataRemark_search').innerHTML 	= obj.userdata.issueNote?HTMLEncode(obj.userdata.issueNote):'&nbsp;';			//模型说明
            $('effectTime_serach').innerHTML 	= obj.userdata.startTime?obj.userdata.startTime:'&nbsp;';			//生效时间
			
            //数据审核方式
            $('auditType_search').innerHTML = getNameByTypeValue('ISSUE_AUDIT_TYPE',obj.userdata.auditType);
			//数据周期
            $('dataCycle_search').innerHTML = getNameByTypeValue('DATA_CYCLE',obj.userdata.dataCycle);
            //应用约定
            if(obj.userdata.dataCycle==isD){	//按天
            	$('appRule_search').innerHTML = getNameByTypeValue('PRT_AGREED',obj.userdata.maxDate);
            }else if(obj.userdata.dataCycle==isM){	//按月
            	$('appRule_search').innerHTML = "前一月";
            }
            //审核模式
        	$('auditProp_search').innerHTML = getNameByTypeValue('AUDIT_PROP',obj.userdata.auditProp);
            //是否清单表
            if(obj.userdata.isListing == '1') {
            	$('isListing_search').innerHTML = "清单";
            }else{
            	$('isListing_search').innerHTML = "报表";
            }
            
            var subTypes = obj.userdata.subscribeType?(obj.userdata.subscribeType+"").split(","):"";
            var subType_serach = "";
            for(var m=0;m<subTypes.length;m++){
            	subType_serach += getNameByTypeValue('PUSH_TYPE',subTypes[m])+",";
            }
            if(subType_serach.length>0){
            	subType_serach = subType_serach.substr(0,subType_serach.length-1);
            }
            $('subType_serach').innerHTML = subType_serach;
            
            //加载表类字段数据
            if(!p_self.gridSearch){
                p_self.gridSearch = new dhtmlXGridObject('issueDataModelTable_search');
                p_self.gridSearch.clearAll();
                p_self.gridSearch.setHeader("字段名,别名,字段分类,字段属性,合计,已支撑维度粒度,维度值");
                p_self.gridSearch.setInitWidthsP("15,15,15,14,5,21,15");
                p_self.gridSearch.setColAlign("left,left,left,center,center,left,left");
                p_self.gridSearch.setHeaderAlign("center,center,center,center,center,center,center");
                p_self.gridSearch.setColTypes("ro,ro,ro,ro,ro,ro,ro");
                p_self.gridSearch.setColSorting("na,na,na,na,na,na,na");
                p_self.gridSearch.setEditable(true);
                p_self.gridSearch.enableMultiselect(true);
                p_self.gridSearch.setImagePath(getDefaultImagePath());
                p_self.gridSearch.enableTooltips("false,false,false,false,false,false,false");
                p_self.gridSearch.init();
                
			    p_self.gridSearch.entBox.onselectstart=function(){
			        return true;
			    }
            }

            p_self.dwrCaller.executeAction('queryPublishColInfo',obj.userdata.issueId,function(data){
                p_self.gridSearch.clearAll();
                p_self.gridSearch.parse(data,"json");
                p_self.gridSearch.checkAll(true);
            });

            Tools.addEvent($('publishCancel_search'),'click',closeSearchPublishModelDialog);
            function closeSearchPublishModelDialog (){
                dhxWindow.window("searchWindow").close();
            }
        },
        
        /**
         * 修改数据模型
         * @param obj 当前要修改的数据模型对象
         */
        modify:function(obj) {
            var self = this;
            
            dhxWindow.createWindow("modifyWindow", 0, 0, 450, 430);
            var winsize=Tools.propWidthDycSize(1,1,5,10);
            self.modifyWindow = dhxWindow.window("modifyWindow");
            self.modifyWindow.stick();
            self.modifyWindow.setModal(true);
            self.modifyWindow.setDimension(winsize.width);
            self.modifyWindow.button("minmax1").hide();
            self.modifyWindow.button("park").hide();
            self.modifyWindow.button("stick").hide();
            self.modifyWindow.button("sticked").hide();
            self.modifyWindow.center();
            self.modifyWindow.denyResize();
            self.modifyWindow.denyPark();
            self.modifyWindow.setText("修改数据模型");
            self.modifyWindow.keepInViewport(true);
            
            self.modifyWindow.attachEvent("onClose", function (id, value, checkedState){
                Tools.removeEvent($('publishCancel_modify'),'click',closeSearchPublishModelDialog);
                Tools.removeEvent($('publishSubmit_modify'),'click',publishSubmit);
                return true;
            });
            var layout = new dhtmlXLayoutObject(self.modifyWindow, "1C");

            layout.cells("a").fixSize(false, true);
            layout.cells("a").firstChild.style.height = (layout.cells("a").firstChild.offsetHeight + 5) + "px";
            layout.cells("a").hideHeader();
            layout.cells("a").attachObject(p_self.modifyHtml);
            
			//添加验证            
            dhtmlxValidation.addValidation($('dataAlias_modify'),"NotEmpty,MaxLength[100]");
            dhtmlxValidation.addValidation($('_keyword_modify'),"MaxLength[100]");
            dhtmlxValidation.addValidation($('_dataRemark_modify'),"NotEmpty,MaxLength[4000]");

            $('tableTypeInfo_modify').innerHTML = obj.userdata.dataSourceName+'.'+obj.userdata.tableOwner+'.'+obj.userdata.tableName;
            $('dataAlias_modify')	.value 		= obj.userdata.tableAlias?obj.userdata.tableAlias:'';		//模型别名
            $('_keyword_modify')	.value 		= obj.userdata.tableKeyword?obj.userdata.tableKeyword:'';	//关键字
            $('_dataRemark_modify')	.value 		= obj.userdata.issueNote?obj.userdata.issueNote:'';			//模型说明
			$('effectTime_modify')	.innerHTML	= obj.userdata.startTime?obj.userdata.startTime:'&nbsp;';			//生效时间
			var subscribeType = obj.userdata.subscribeType?obj.userdata.subscribeType:'';			//发送方式

            self.tableName = obj.userdata.tableName;		//表类名
			dateCheckAndLocalCheck(self.tableName);	
					
			/********设置发送方式开始********/
			var subscribeTypes = subscribeType.split(',');
			var pushType = getCodeByType("PUSH_TYPE");
			var subTypeHtml = "";
			for(var m=0;m<pushType.length;m++){
				var checked = "";
				for(var n=0;n<subscribeTypes.length;n++){
					if(pushType[m].value == subscribeTypes[n]){
						checked = 'checked="checked"';
					}
				}
				subTypeHtml += '<input '+checked+' name="subscribeType_modify" id="pushType_modify'+m+'" type="checkbox" value="'+pushType[m].value+'"/><span style="position:relative;top:-2px;margin-right:15px;"><label for="pushType_modify'+m+'">'+pushType[m].name+'</label></span>';
			}
			$('subType_modify').innerHTML=subTypeHtml;
			/********设置发送方式结束********/
			
			//数据周期
            if(obj.userdata.dataCycle == '11') {
                $('forDate_modify').checked = 'true';
				$('before1Day_modify').checked=true;
				$('before1DayLabel_modify').innerHTML="前一天";
				$('before2Day_modify').style.display="";
				$('before2DayLabel_modify').style.display="";
				$('before3Day_modify').style.display="";
				$('before3DayLabel_modify').style.display="";
            }else {
                $('forMonth_modify').checked = 'true';
				$('before1Day_modify').checked=true;
				$('before1DayLabel_modify').innerHTML="前一月";
				$('before2Day_modify').style.display="none";
				$('before2DayLabel_modify').style.display="none";
				$('before3Day_modify').style.display="none";
				$('before3DayLabel_modify').style.display="none";
            }
            //应用约定
            if(obj.userdata.maxDate == '-1') {
                $('before1Day_modify').checked = 'true';
            }else if(obj.userdata.maxDate == '-2'){
                $('before2Day_modify').checked = 'true';
            }else if(obj.userdata.maxDate == '-3') {
                $('before3Day_modify').checked = 'true';
            }
            //审核模式
            if(obj.userdata.auditProp == '1') {
                $('province_modify').checked = 'true';
            }else{
                $('branch_modify').checked = 'true';
            }
            //是否清单表
            if(obj.userdata.isListing == '1') {
                $('isYes_modify').checked = 'true';
				$('Automatic_modify').checked=true;
				$('artificial_modify').disabled=true;
            }else{
                $('isNO_modify').checked = 'true';
				$('artificial_modify').checked=true;
				$('artificial_modify').disabled=false;
            }
            //数据审核方式
            if(obj.userdata.auditType == '0') {
                $('Automatic_modify').checked = 'true';
            }else {
                $('artificial_modify').checked = 'true';
            }
            
            //加载表类字段数据
            if(!p_self.gridModify){
                p_self.gridModify = new dhtmlXGridObject('issueDataModelTable_modify');
                p_self.gridModify.clearAll();
                p_self.gridModify.setHeader("选择,字段名,别名,字段分类,字段属性,合计,已支撑维度粒度,维度值");
                p_self.gridModify.setInitWidthsP("4,14,14,14,14,4,22,14");
                p_self.gridModify.setColAlign("center,left,left,left,center,center,left,left");
                p_self.gridModify.setHeaderAlign("left,center,center,center,center,center,center,center");
                p_self.gridModify.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro");
                p_self.gridModify.setColSorting("na,na,na,na,na,na,na,na");
                p_self.gridModify.setEditable(true);
                p_self.gridModify.enableMultiselect(true);
                p_self.gridModify.setImagePath(getDefaultImagePath());
                p_self.gridModify.setColumnIds("'',colName,colAlias,'colTypeId',colBusType,'amountFlag',dimLevels,'dimCodes'");
                p_self.gridModify.enableTooltips("false,false,false,false,false,false,false,false");
                p_self.gridModify.init();
                
			    p_self.gridModify.entBox.onselectstart=function(){
			        return true;
			    }
            }

            p_self.dwrCaller.executeAction('queryModifyPublishColInfo',obj.userdata.tableId,obj.userdata.issueId,function(data){
                p_self.gridModify.clearAll();
                p_self.gridModify.parse(data,"json");
            });

            self.Calendar = new dhtmlXCalendarObject("effectTime_modify");//绑定日期控件到生效时间
            self.Calendar.setDateFormat("%Y-%m-%d %H:%i:%s");
		    self.Calendar.loadUserLanguage('zh'); //将日历控件语言设置成中文
            self.Calendar.attachEvent("onClick",function(date){
                Tools.removeClass($('effectTime_modify'),'input_click');
                $('effectTime_modify').setAttribute('ucflag',1);
            });

            //绑定取消事件
            Tools.addEvent($('publishCancel_modify'),'click',closeSearchPublishModelDialog);
            function closeSearchPublishModelDialog (){
   		 		$("codeDiv").style.display = "none";		//点击取消时取消标识输入层
                dhxWindow.window("modifyWindow").close();
            }

            //绑定修改发布事件
            Tools.addEvent($('publishSubmit_modify'),'click',publishSubmit);
            function publishSubmit() { 
            	$("codeDiv").style.display = "none";		//点击发布时取消标识输入层
                //验证信息
                if(!dhtmlxValidation.validate("dataAlias_modify")||!dhtmlxValidation.validate("_dataRemark_modify")) {
	                return;
	            }
//				if(Tools.getInputValue($('dataAlias_modify'))!=obj.userdata.tableAlias){	//如果不相等，验证是否重名
//				     //判断模型别名是否存在
//				    p_self.dwrCaller.executeAction("validateName",$('dataAlias_modify').value,function(boo){
//						if(boo){
//							dhx.alert("模型别名已存在，重新输入，谢谢！");
//							return;
//						}else{
//							
//						}
//				    });
//				}

                p_self.issueId_modify			= obj.userdata.issueId;									//模型id
                p_self.tableId_modify 			= obj.userdata.tableId;									//表类ID
                p_self.tableAlias_modify 		= Tools.getInputValue($('dataAlias_modify'));			//模型别名
                p_self.tableKeyword_modify 		= Tools.getInputValue($('_keyword_modify'));			//关键字
                p_self.dataRemark_modify 		= Tools.getInputValue($('_dataRemark_modify'));			//模型说明
                p_self.effectTime_modify		= Tools.getInputValue($('effectTime_modify'));			//生效时间
                p_self.auditType_modify 		= Tools.getCheckedRadio('auditType_modify').value;		//审核方式
                p_self.dataCycle_modify 		= Tools.getCheckedRadio('dataCycle_modify').value;		//数据周期
                p_self.appRule_modify 			= Tools.getCheckedRadio('appRule_modify').value;		//应用约定
		        p_self.auditProp_modify			= Tools.getCheckedRadio('auditProp_modify').value;		//审核地域
		        p_self.isListing_modify 		= Tools.getCheckedRadio('isListing_modify').value;		//是否清单
				p_self.subscribeType_modify	= "";											//发送方式
			    /*******发送方式得到值开始***********/
                var length = document.getElementsByName("subscribeType_modify").length;
			    for(var i=0;i<length;i++) {
			        if($('pushType_modify'+i)) {
			            //找到所有选中记录
			            if($('pushType_modify'+i).checked) {
			                p_self.subscribeType_modify += $('pushType_modify'+i).value+",";
			            }
			        }
			    }
			    if(p_self.subscribeType_modify.length>0){
			    	p_self.subscribeType_modify = p_self.subscribeType_modify.substr(0,p_self.subscribeType_modify.length-1);
			    }
			    if(p_self.subscribeType_modify==""){
        			dhx.alert("请至少选择一种发送方式！");
        			return;
			    }
			    /*******发送方式得到值结束***********/
		        
                var colArray = new Array();
                var updateCols = new Array();
                var delCols = new Array();
				var checkColAlias = true;	//字段验证正常开关
		        //得到所有的字段信息
                var allRows = p_self.gridModify.getAllRowIds().split(',').unique();
                var colArray = new Array();
                var isCheckedOne = false;
                var dimDateCheck = false;
                for(var i=0;i<allRows.length;i++) {
                    var columnId 	= p_self.gridModify.getUserData(allRows[i],'columnId');		//模型字段列id
                    var isUsed		= p_self.gridModify.getUserData(allRows[i],'isUsed');
                    if($('modify_select_'+allRows[i]).checked) {
                    	isCheckedOne = true;
                    }
                    if(columnId == null) {
                        if($('modify_select_'+allRows[i])) {
                            if(!$('modify_select_'+allRows[i]).checked){
                                continue;
                            }
                        }
                    }else if(isUsed == 0){
                    	if($('modify_select_'+allRows[i])) {
                            if(!$('modify_select_'+allRows[i]).checked){
                            	var colName 	= p_self.gridModify.getUserData(allRows[i],'colName');		//得到字段列名
                            	var data = {
                            		'columnId'		:	columnId
                            	}
                            	delCols.push(data);
                                continue;
                            }
                        }
                    }
                    

                    var colId 		= p_self.gridModify.getUserData(allRows[i],'colId');		//表类列id
                    var colName 	= p_self.gridModify.getUserData(allRows[i],'colName');		//得到字段列名
                    var colBusType 	= p_self.gridModify.getUserData(allRows[i],'colBusType');	//字段标识
                    var dimTableId 	= p_self.gridModify.getUserData(allRows[i],'dimTableId');	//维度表id
                    var dimTypeId 	= p_self.gridModify.getUserData(allRows[i],'dimTypeId');	//归并类型id
                    var dimType  	= "";		//维度列类型
                    
                    //如果该字段为维度，判断其是否为地域或者时间维度标识
		            if(colBusType == 0) {
		           		if(dimTableId==dimZoneId){
		             		dimType = 2;		//地域维度标识
		             		dimZoneCheck = true;
		              	}else if(dimTableId==dimDateId){
		             		dimType = 1;		//时间维度标识
		             		dimDateCheck = true;
		              	}
		            }
		            
                    var colAlias 	= $('alias_row_'+allRows[i]).value;
					if(colAlias == null || colAlias == ''||colAlias =='undefined'){
						dhx.alert('字段别名不能为空，请输入字段别名！');
						checkColAlias = false;
						break;
					}

					//字段分类
                    var colTypeId = '';
                    if($('field_'+allRows[i])){
                        if($('field_'+allRows[i]).getAttribute("identify") == null ||$('field_'+allRows[i]).getAttribute("identify") =='undefined'){
                            colTypeId = '';
							dhx.alert('字段分类不能为空，请选择字段分类！');
							checkColAlias = false;
							break;
                        }else {
                            colTypeId = $('field_'+allRows[i]).getAttribute("identify");
                        }
                    }

                    //维度支撑粒度
                    var dimLevels = "";
                    var tmps = document.getElementsByName('level_'+allRows[i]);
                    for(var j=0;j<tmps.length;j++) {
                        if(tmps[j].checked == true) {
                       		dimLevels += tmps[j].getAttribute('identify')+',';
                        }
                    }
                    if(dimLevels!=""){
                    	dimLevels = dimLevels.substr(0,dimLevels.length-1);
                    }

                    //是否合计
                    var amountFlag = "";
                    if($('amountFlag_'+allRows[i])){
                        if($('amountFlag_'+allRows[i]).checked == true){
                            amountFlag = 1;
                        }else {
                            amountFlag = 0;
                        }
                    }

                    //维度值
                    var dimCodes = "";
                    if($("dimCodes_"+allRows[i])) {
	                    if(colBusType == 0){
	                        dimCodes = $("dimCodes_"+allRows[i]).getAttribute("dimCodes");
	                    }
	                    else{
	                    	if($('colBusType'+allRows[i])){
	                        	colBusType = $('colBusType'+allRows[i]).value;
	                    	}
	                        if(colBusType==2){	//字段为标识时
	                        	amountFlag = 0;	//是否合计变为不合计
		                    	if($("dimCodes_"+allRows[i])) {
		                            dimCodes = $("dimCodes_"+allRows[i]).value;
		                        }
	                        }
	                    }
                    }

                    var data = {'colId'			:	colId,
                    	'colName'		:	colName,
                        'colAlias'		:	colAlias,
                        'colBusType'	:	colBusType,
                        'colTypeId'		:	colTypeId,
                        'tableId'		:	p_self.tableId,
                        'dimLevels'		:	dimLevels,
                        'dimCodes'		:	dimCodes,
                        'selectedFlag'	:	'1',
                        'amountFlag'	:	amountFlag,
                        'dimTableId'    :   dimTableId,
                        'dimTypeId'     :   dimTypeId,
                        'dimType'		:	dimType,
                        'columnId'		:	columnId
                    };
                    if(columnId!=null) {	//如果不为空，表示为已发布字段，保存修改。
                       	updateCols.push(data);
                    }else {
                        colArray.push(data);	//保存新选择的字段，以便新增
                    }
                }
                
                //字段验证正常通过后，修改模型
                if(checkColAlias){
                	if(!isCheckedOne) {
                		dhx.alert("至少选择一个字段发布！");
                	}
					if(!dimDateCheck){	//如果为false，表示发布字段中不存在时间或者地域维度，不允许发布
						dhx.alert("时间维度必须发布！");
						return;
					}
	                p_self.colData_modify = colArray;
	                p_self.updateColData_modify = updateCols;
	                p_self.delCols = delCols;
	                p_self.dwrCaller.executeAction('updatePublishDataModel',function(rs){
	                    if(rs){
	                        dhx.alert(p_self.tableAlias_modify+"数据模型修改成功！");
				          	modelGrid.clearAll();
				          	modelGrid.load(p_self.dwrCaller.queryDataModel, "json");
                			dhxWindow.window("modifyWindow").close();
	                    }else {
	                        dhx.alert("数据模型修改失败！请联系管理员！");
	                    }
	                });
                }else{
                	return;
                }
            }
        }
    }
}

var genObj = function() {
    this.instance = new publishDataModel();
    this.instance.init();
}
dhx.ready(genObj);

var tableType = null;
var treeData = null;
var lastTableType = null;
var tmpObj = null;
var dwrCaller = new biDwrCaller();

dwrCaller.addAutoAction("queryColType", "ColTypeAction.queryColType");
dwrCaller.addDataConverter("queryColType", new dhtmxTreeDataConverter({
    idColumn:"colTypeId",pidColumn:"parentId",textColumn:"colTypeName",
    isDycload:false
}));
dwrCaller.isShowProcess("queryColType", false);

//加载字段分类树
var loadFieldType = function(obj) {
    var loadData1 = function(target, hide){
        var div = dhx.html.create("div", {
            style:"display;none;position:absolute;border: 2px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
                    "z-index:1000"
        });
        this.treeDiv = div;
        document.body.appendChild(div);
        target.readOnly = true;
        var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
        tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
        tree.enableThreeStateCheckboxes(true);
        tree.enableHighlighting(true);
        tree.enableSingleRadioMode(true);
        tree.setDataMode("json");
        tree.attachEvent("onDblClick", function(nodeId){
        	var parentId = tree.getUserData(nodeId,'parentId');
        	if(parentId==0){//当选择为顶级分类时
        		if(tree.hasChildren(nodeId) == 0){	//没有子分类时，给出提示。
                	dhx.alert("根目录不能选择，请选择子分类！");
        		}
        	}else{
	            target.value = tree.getItemText(nodeId);
	            target.setAttribute("identify", nodeId);
	            div.style.display = "none";
        	}
        	tree.openItem(nodeId);
        });
        this.tree = tree;
        this.showGroupTree = function(tableType){
            var show = function(){
                var childs=tree.getAllSubItems(0);
                if(childs){
                    var childIds=childs.toString().split(",");
                    for(var i=0;i<childIds.length;i++){
                        tree.deleteItem(childIds[i]);
                    }
                }
                tree.loadJSONObject(treeData);
                if(target.getAttribute("identify")){	//如果不为空，选中该分类
                	tree.selectItem(target.getAttribute("identify"));
                }
                
                childs = null;
            }
            if(!treeData){
                dwrCaller.executeAction("queryColType", function(data){
                    treeData = data;
                    show();
                    lastTableType=tableType;
                });
            } else{
                show();
                lastTableType=tableType;
            }
        }
    }

    if(tmpObj!=null){	//如果不为空，隐藏当前treeDiv,并设置为空
   		tmpObj.treeDiv.style.display = "none";
   		tmpObj = null;
    }
    tmpObj = new loadData1(obj);
    tmpObj.treeDiv.style.width = obj.offsetWidth + 'px';
    Tools.divPromptCtrl(tmpObj.treeDiv, obj, true);
    tmpObj.treeDiv.style.display = "block";
    tmpObj.showGroupTree();
    dhx.env.isIE&&CollectGarbage();
}

/*****************动态加载维度值*************/
//动态加载维度值时所调用的参数
var codeParam = new biDwrMethodParam();
codeParam.setParamConfig([{
    index:0,type:"fun",value:function(){
        var formData = {};
		formData.dimLevel		=	dimLevelParam;
        formData.dimTableId		=	dimTableIdParam;
        formData.dimTypeId		=	dimTypeIdParam;
        return formData;
	}
}]);
dwrCaller.addAutoAction("queryCodesByNode", "IssueDataModelAction.queryCodesByNode",codeParam);
dwrCaller.addDataConverter("queryCodesByNode", new dhtmxTreeDataConverter({
    idColumn:"id",pidColumn:"parId",textColumn:"name",
    isDycload:true,
    userData:function(rowIndex, rowData) {
        var userData = {};
        userData["code"] 	= rowData["code"];
        userData["id"] 		= rowData["id"];
        return userData;
    }
}));
dwrCaller.isShowProcess("queryCodesByNode", false);			//设置是否显示进度条
/*****************初始加载维度值*************/
dwrCaller.addAutoAction("queryDimCodes", "IssueDataModelAction.queryDimCodes");
dwrCaller.addDataConverter("queryDimCodes", new dhtmxTreeDataConverter({
    idColumn:"id",pidColumn:"parId",textColumn:"name",
    isDycload:true,
    userData:function(rowIndex, rowData) {
        var userData = {};
        userData["code"] 	= rowData["code"];
        userData["id"] 		= rowData["id"];
        return userData;
    }
}));
dwrCaller.isShowProcess("queryDimCodes", false);			//设置是否显示进度条
var treeData2 = null;
var dimLevelParam = "";
var dimTableIdParam = "";
var dimTypeIdParam = "";
var dimDivName = "";		//当前打开的treediv，用于打开下一个div时，隐藏该div
//加载维度值tree
var loadDimCodes = function (obj,dimTableId,dimTypeId,isUsed) {
    var length = document.getElementsByName('level_'+obj.name).length;
    var dimLevel = "";
    for(var i=0;i<length;i++) {
        if($('level_checked_'+obj.name+'_'+i)) {
            //找到所有选中记录
            if($('level_checked_'+obj.name+'_'+i).checked) {
                dimLevel += parseInt($('level_checked_'+obj.name+'_'+i).getAttribute('identify'))+",";
            }
        }
    }
    if(dimLevel.length>0){
    	dimLevel = dimLevel.substr(0,dimLevel.length-1);
    }
    dimLevelParam = dimLevel;
    dimTableIdParam = dimTableId;
    dimTypeIdParam = dimTypeId;
    var loadData1 = function(target){
    	
        var div = dhx.html.create("div", {
            style:"display;none;position:absolute;border: 2px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
                    "z-index:1000"
        });
        dimDivName = 'tree_'+obj.id;
        div.id = 'tree_'+obj.id;
        this.treeDimDiv = div;
        document.body.appendChild(div);
        target.readOnly = true;
        
        var dimTree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
        dimTree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
        dimTree.enableHighlighting(true);
        dimTree.enableSingleRadioMode(true);		
        dimTree.enableCheckBoxes(true,true);
        dimTree.enableThreeStateCheckboxes(1);
        dimTree.setXMLAutoLoading(dwrCaller.queryCodesByNode);
        dimTree.setDataMode("json");
        
        //树解析完事件后调用
        dimTree.attachEvent("onXLE", function (dimTree, parId){
        	var isUpdate = obj.getAttribute("deDimCodes")!=null;
	        dimTree.setCheck(0,1);		//设置所有值选中
            var childs=dimTree.getAllCheckedBranches();	//得到所有选中的值
            if(childs){
                //设置取消选中的值
                if(obj.getAttribute("dimCodes")){
                	var dimCodes = obj.getAttribute("dimCodes").toString().split(",");
                    var childIds=childs.toString().split(",");
                    for(var i=0;i<childIds.length;i++){
           				var code = dimTree.getUserData(childIds[i],"code");
                    	for(var j=0;j<dimCodes.length;j++){
                    		if(code==dimCodes[j]){
                    			dimTree.setCheck(childIds[i],false);
                    			break;
                    		}
                    	}
                    }
                }
            }
            
            var childs=dimTree.getAllCheckedBranches();	//得到处理过后所有选中的值
            if(childs){
           		//设置禁用操作发布时选择的值
                if(isUpdate && isUsed > 0){	//当该属性不为空时，表示为修改。
            		var deDimCodes = obj.getAttribute("deDimCodes").toString().split(",");
                    var childIds=childs.toString().split(",");
                    for(var i=0;i<childIds.length;i++){
                    	var isOk = true;	//开关控制，true为禁止操作
           				var code = dimTree.getUserData(childIds[i],"code");
                    	for(var j=0;j<deDimCodes.length;j++){
                    		if(code==deDimCodes[j]){
                    			isOk = false;	//当相等时，表示该值还可以操作，则不禁用
                    			break;
                    		}
                    	}
                    	if(isOk){
                    		dimTree.disableCheckbox(childIds[i],true);
                    	}
                    }
                }
            }
            childs = null;
            
            if(!isUpdate){		//当不为修改时，时时改变值
	            //改变选中值
	            target.value = "";
	            var unChecked = new Array();
	            unChecked = dimTree.getAllUnchecked().toString().split(',');
	            var value = "";
	            var codes = "";
	            var check = false;	//开关，表示不存在没被选中的
	            for(var i=0;i<unChecked.length;i++) {
	            	if(unChecked[i] =='') {
	                    continue;
	                }
	                var val = dimTree.getItemText(unChecked[i]);
	                var code = dimTree.getUserData(unChecked[i],"code");
	                value += val+',';
	                codes += code+',';
	            	check = true;
	            }
	            if(check){	//如果为true，表示存在没有被选中的选项
	            	value = value.substr(0,value.length-1);
	            	codes = codes.substr(0,codes.length-1);
	            }
	        	obj.value = value?("除了："+value+"之外"):"";
	            obj.setAttribute("dimCodes",codes);
            }
        });

        //选中事件
        dimTree.attachEvent("onCheck", function(id,state){
        	//改变选中值
            target.value = "";
            var unChecked = new Array();
            unChecked = dimTree.getAllUnchecked().toString().split(',');
            
            var value = "";
            var codes = "";
            var check = false;	//开关，表示不存在没被选中的
            for(var i=0;i<unChecked.length;i++) {
            	if(unChecked[i] =='') {
                    continue;
                }
                var val = dimTree.getItemText(unChecked[i]);
                var code = dimTree.getUserData(unChecked[i],"code");
                value += val+',';
                codes += code+',';
            	check = true;
            }
            if(check){	//如果为true，表示存在没有被选中的选项
            	value = value.substr(0,value.length-1);
            	codes = codes.substr(0,codes.length-1);
            }
            obj.value = value?("除了："+value+"之外"):"";
            obj.setAttribute("dimCodes",codes);
        });

        this.dimTree = dimTree;
		//展现数据
        this.showGroupTree = function(){
            var show = function(){
                var childs=dimTree.getAllSubItems(0);
                if(childs){
                    var childIds=childs.toString().split(",");
                    for(var i=0;i<childIds.length;i++){
                        dimTree.deleteItem(childIds[i]);
                    }
                }
                if(treeData2){
	                dimTree.loadJSONObject(treeData2);
                }
            }
            dwrCaller.executeAction("queryDimCodes", dimTableId,dimTypeId,dimLevel,function(data){
                treeData2 = data;
                show();
            });
        }
    }
    if(dimDivName!=""){
    	$(dimDivName).style.display = "none";
    }
    if($('tree_'+obj.id)) {
        dimDivName = 'tree_'+obj.id;
    	var treeLevels = obj.getAttribute("treeLevels");
        if(treeLevels != dimLevel) {
            obj.setAttribute("treeLevels",dimLevel);
            
            $('tree_'+obj.id).innerHTML='';
            $('tree_'+obj.id).parentNode.removeChild($('tree_'+obj.id));

            var tmpObj = new loadData1(obj);
            tmpObj.treeDimDiv.style.width = obj.offsetWidth + 'px';
            Tools.divPromptCtrl(tmpObj.treeDimDiv, obj, true);
            tmpObj.treeDimDiv.style.display = "block";
            tmpObj.showGroupTree();
        }else {
            Tools.divPromptCtrl($('tree_'+obj.id), obj, true);
            $('tree_'+obj.id).style.display = "block";
       		dhx.env.isIE&&CollectGarbage();
        }
    }else {
        obj.setAttribute("treeLevels",dimLevel);
        var tmpObj = new loadData1(obj);
        tmpObj.treeDimDiv.style.width = obj.offsetWidth + 'px';
        Tools.divPromptCtrl(tmpObj.treeDimDiv, obj, true);
        tmpObj.treeDimDiv.style.display = "block";
        tmpObj.showGroupTree();
        dhx.env.isIE&&CollectGarbage();
    }
}

var codesId = null;			//标识id
var codesGrid = null;		//全局grid
//初始化标识设置框
var dimCodesInit = function(){
    $("codeDiv").style.display = "block";
	var dhxLayout = new dhtmlXLayoutObject("codeDiv","2E");
	dhxLayout.cells("a").setText("设置标识值");
    dhxLayout.cells("a").setHeight(280);
    dhxLayout.cells("a").fixSize(true, true);
    dhxLayout.cells("b").hideHeader();
    dhxLayout.cells("b").fixSize(true, true);
    dhxLayout.hideSpliter();
    dhxLayout.hideConcentrate();
	
    codesGrid = dhxLayout.cells("a").attachGrid();
	codesGrid.setHeader("值,名称,操作");
	codesGrid.setInitWidthsP("40,40,20");
	codesGrid.setColAlign("left,left,center");
	codesGrid.setHeaderAlign("center,center,center");
	codesGrid.setColTypes("ro,ro,ro");
	codesGrid.setColSorting("na,na,na");
	codesGrid.setEditable(false);	
	codesGrid.init();
	
    codesGrid.entBox.onselectstart=function(){
        return true;
    }
	queryForm = dhxLayout.cells("b").attachForm(	
		[
			{type: "block", list:[
		        {type:"button",name:"submit",value:"确定",offsetLeft:0,offsetTop:0},
        		{type:"newcolumn"},
		        {type:"button",name:"cancel",value:"取消",offsetLeft:0,offsetTop:0}
			]}
	    ]
    );
	//添加按钮点击事件
	queryForm.attachEvent("onButtonClick", function(id) {
        if (id == "submit") {
        	if(addDimCodesValue()){
   		 		$("codeDiv").style.display = "none";
        	}
        }else if (id == "cancel") {
            //隐藏div
   		 	$("codeDiv").style.display = "none";
        }
    });
}

//添加标识值
var addDimCodesValue = function(){
	var allIds = codesGrid.getAllRowIds();
	allIds = allIds.replaceAll("codes_","");
	if(allIds){
		var rowsId = allIds.split(',');
		var dimCodesStr = "";
		for(var i=0;i<rowsId.length-1;i++){
			var codeValue = $("codeValue"+rowsId[i]).value;
			var codeName = $("codeName"+rowsId[i]).value;
			//验证
	        if(!dhtmlxValidation.validate("codeValue"+rowsId[i])||!dhtmlxValidation.validate("codeName"+rowsId[i])) {
	       		return false;
	        }
			if(i==0){
				dimCodesStr += codeValue+":"+codeName;
			}else{
				dimCodesStr += ";"+codeValue+":"+codeName;
			}
		}
		$(codesId).value = dimCodesStr;
	}
	return true;
}

var isFirst = true;
//设置标识值
var setDimCodes = function(obj){
   	codesId = obj.id;
   	var objValue = obj.value;
	if(isFirst){
		isFirst = false;
		dimCodesInit();
	}
	codesGrid.clearAll();		//清空数据
   	if(objValue){	//如果有数据绑定到grid上
		var dimValues = objValue.split(';');
		for(var i=0;i<dimValues.length;i++){
			var codeValues = dimValues[i].split(':');
			addColumnRow(codeValues[0],codeValues[1]);
		}
   	}
	addColumnRow();	//增加一行空白数据
    $("codeDiv").style.display = "block";
}

var columnNum = 0;//行递增索引。
//新增一行列配置
var addColumnRow = function(codeValue,codeName){
    rowIndex = ++columnNum;
 
    var codeValueStr = "<input type='text' onclick='cancelBubble()' style='width: 90%;height:15px;line-height:15px;'" +
    		" onkeyup='addRow("+rowIndex+")' id='codeValue"+rowIndex+"' value='"+(codeValue?codeValue:"")+"'>";
	var codeNameStr = "<input type='text' onclick='cancelBubble()' style='width: 90%;height:15px;line-height:15px;'" +
			" onkeyup='addRow("+rowIndex+")' id='codeName"+rowIndex+"' value='"+(codeName?codeName:"")+"'>";
	var imgStr = "<img id='codeImg"+rowIndex+"' src='../../../resource/images/cancel.png' alt='删除' " +
			" onclick='removeRow("+rowIndex+")' style='width:16px;height:16px;cursor:pointer;display:none;'>";
	
	codesGrid.addRow("codes_"+rowIndex,[codeValueStr,codeNameStr,imgStr+" "]);
    
	var obj = $("codeImg"+(rowIndex*1-1));
	if(obj){	//如果存在，
		obj.style.display = "block";
	}
	
    dhtmlxValidation.addValidation($('codeValue'+rowIndex),"NotEmpty,MaxLength[32]");
    dhtmlxValidation.addValidation($('codeName'+rowIndex),"NotEmpty,MaxLength[32]");
}

//新增一行数据
var addRow = function(rowIndex){
	if(rowIndex == columnNum){		//只有当为最后一条操作数据时，才新增数据
		addColumnRow();
	}
}

//删除一行数据
var removeRow = function(rowIndex){
	dhx.confirm("确定删除？删除后只能重新操作！",function(b){
		if(b){
		    codesGrid.deleteRow("codes_"+rowIndex);
		}
	})
}

//绑定支撑维度层次选择动作
var bindLevelAction = function(obj) {
    var allLength = obj.getAttribute('allLevel');		//总层级
    var curLength = obj.getAttribute('curLevel');		//当前选择目标的长度

    if($('dimCodes_'+obj.getAttribute('rowData')).getAttribute("deDimCodes")){	
    	//如果存在值，表示为修改，把以前的值作为初始值
	    $('dimCodes_'+obj.getAttribute('rowData')).value = $('dimCodes_'+obj.getAttribute('rowData')).getAttribute("deDimNames");
	    $('dimCodes_'+obj.getAttribute('rowData')).setAttribute("dimCodes",$('dimCodes_'+obj.getAttribute('rowData')).getAttribute("deDimCodes"));
    }else{
    	//当点击取消维度粒度选项的时候，维度编码值被清空
	    $('dimCodes_'+obj.getAttribute('rowData')).value = '';
	    //并且清空编码值
	    $('dimCodes_'+obj.getAttribute('rowData')).setAttribute("dimCodes",'');
    }
    
    if(obj.checked == true) {
        for(var i=parseInt(curLength)+1;i<allLength;i++) {
            $('level_checked_'+obj.getAttribute('rowData')+'_'+i).checked = true;
            $('level_checked_'+obj.getAttribute('rowData')+'_'+i).disabled = true;
        }
    }else {
        var j = parseInt(curLength)+1;
        if(j < allLength) {
            //有noCancel标志的不能被取消，用于最后一个维度粒度和修改的时候用、
            if($('level_checked_'+obj.getAttribute('rowData')+'_'+j).getAttribute("noCancel") != 'true'){
                $('level_checked_'+obj.getAttribute('rowData')+'_'+j).disabled = false;
            }
        }
    }
}

//字段选择，是指标还是标识
var changeBusType = function(obj){
	var val = obj.value;
	var ide = obj.getAttribute("identify");
	if(val==1){
		$('amountFlag_'+ide).style.display="";	//是否合计显示
		$('dimCodes_'+ide).style.display="none";	//标识数据框隐藏
	}else{
		$('amountFlag_'+ide).style.display="none";	//是否合计隐藏
		$('dimCodes_'+ide).style.display="";	//标识数据框显示
	}
}

//选择模型类型时事件
var cheangeIssueType = function(val,typeName){
	if(typeName=="insert"){
		if(val==0){	//报表
			$('artificial').checked=true;
			$('artificial').disabled=false;
		}else if(val==1){	//清单
			$('Automatic').checked=true;
			$('artificial').disabled=true;
		}
	}else if(typeName=="update"){
		if(val==0){	//报表
			$('artificial_modify').checked=true;
			$('artificial_modify').disabled=false;
		}else if(val==1){//清单
			$('Automatic_modify').checked=true;
			$('artificial_modify').disabled=true;
		}
	}
}

//选择审核模型时事件
var cheangeAuditType = function(val,typeName){
	if(typeName=="insert"){
		if(val==11){//按天
			$('before1Day').checked=true;
			$('before1DayLabel').innerHTML="前一天";
			$('before2Day').style.display="";
			$('before2DayLabel').style.display="";
			$('before3Day').style.display="";
			$('before3DayLabel').style.display="";
		}else if(val==22){//按月
			$('before1Day').checked=true;
			$('before1DayLabel').innerHTML="前一月";
			$('before2Day').style.display="none";
			$('before2DayLabel').style.display="none";
			$('before3Day').style.display="none";
			$('before3DayLabel').style.display="none";
		}
	}else if(typeName=="update"){
		if(val==11){//按天
			$('before1Day_modify').checked=true;
			$('before1DayLabel_modify').innerHTML="前一天";
			$('before2Day_modify').style.display="";
			$('before2DayLabel_modify').style.display="";
			$('before3Day_modify').style.display="";
			$('before3DayLabel_modify').style.display="";
		}else if(val==22){//按月
			$('before1Day_modify').checked=true;
			$('before1DayLabel_modify').innerHTML="前一月";
			$('before2Day_modify').style.display="none";
			$('before2DayLabel_modify').style.display="none";
			$('before3Day_modify').style.display="none";
			$('before3DayLabel_modify').style.display="none";
		}
	}
}

//时间格式化 ，转换为 YYYY-MM-DD hh:mm:ss 格式
var ChangeTimeToString = function(DateIn){
	var nowYear = DateIn.getFullYear();
	//月
    var nowMonth = (DateIn.getMonth()+1)+"";
    if(nowMonth.length==1){
    	nowMonth = "0"+nowMonth;
    }
    //天
    var nowDay = DateIn.getDate()+"";
    if(nowDay.length==1){
    	nowDay = "0"+nowDay;
    }
    //时
    var nowHours = DateIn.getHours()+"";
    if(nowHours.length==1){
    	nowHours = "0"+nowHours;
    }
    //分
    var nowMinutes = DateIn.getMinutes()+"";
    if(nowMinutes.length==1){
    	nowMinutes = "0"+nowMinutes;
    }
    //秒
    var nowSeconds = DateIn.getSeconds()+"";
    if(nowSeconds.length==1){
    	nowSeconds = "0"+nowSeconds;
    }
    var time = nowYear+"-"+nowMonth+"-"+nowDay+" "+nowHours+":"+nowMinutes+":"+nowSeconds;
    return time;
}

//取消grid上输入框需要点击两次才能光标定位的问题
function cancelBubble(e) {
    e = e || window.event;
    if (!e)return false;
    if (e.preventDefault) {
        e.preventDefault();
    }
    e.cancelBubble = true;
    return false;
}
