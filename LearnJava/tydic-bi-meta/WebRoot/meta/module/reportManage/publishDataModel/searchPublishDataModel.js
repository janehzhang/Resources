dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
p_tipLength = 4;	//字符截断的长度
var dwrCaller= new biDwrCaller();

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
           	return rowData["dimNames"]?rowData["dimNames"]:"";
		}else{
           	return rowData["dimCodes"]?rowData["dimCodes"]:"";
		}
		}
		return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
	}
}
dwrCaller.addAutoAction("queryPublishColInfo", "IssueDataModelAction.queryPublishColInfo");
dwrCaller.addDataConverter("queryPublishColInfo", new dhtmxGridDataConverter(pubColInfo));




function init() {
	IssueDataModelAction.queryDataModelByIssueId(issueId,function(data) {
		if(data == '') {
			dhx.alert("未查到发布数据！");
			return;
		}
		initBasicInfo(data[0]);
	});
	$('issueDataModelTable_search').style.height = (document.body.offsetHeight - issueDataModelInfo_search.offsetHeight-5) +'px';
	loadColInfo();
}

/**
 *初始化基本信息
 **/
var initBasicInfo = function(data) {
	$('tableTypeInfo_search').innerHTML = data.DATA_SOURCE_NAME+'.'+data.TABLE_OWNER+'.'+data.TABLE_NAME;
	$('dataAlias_search').innerHTML 	= data.TABLE_ALIAS?data.TABLE_ALIAS:'';		//模型别名
	$('_keyword_search').innerHTML 		= data.TABLE_KEYWORD?data.TABLE_KEYWORD:'';	//关键字
	$('_dataRemark_search').innerHTML 	= data.ISSUE_NOTE?data.ISSUE_NOTE:'';			//模型说明
	$('effectTime_serach').innerHTML 	= data.START_TIME?data.START_TIME:'';			//生效时间
	//数据审核方式
	$('auditType_search').innerHTML = getNameByTypeValue('ISSUE_AUDIT_TYPE',data.AUDIT_TYPE);
	//数据周期
	$('dataCycle_search').innerHTML = getNameByTypeValue('DATA_CYCLE',data.DATA_CYCLE);
	//应用约定
	$('appRule_search').innerHTML = getNameByTypeValue('PRT_AGREED',data.MAX_DATE);
	//审核模式
	$('auditProp_search').innerHTML = getNameByTypeValue('AUDIT_PROP',data.AUDIT_PROP);
	//是否清单表
	if(data.IS_LISTING == '1') {
		$('isListing_search').innerHTML = "清单";
	}else{
		$('isListing_search').innerHTML = "报表";
	}
}


var loadColInfo = function() {
	var search = new dhtmlXGridObject('issueDataModelTable_search');
    search.clearAll();
    search.setHeader("字段名,别名,字段分类,字段属性,合计,已支撑维度粒度,维度值");
    search.setInitWidthsP("15,15,15,14,5,21,15");
    search.setColAlign("left,left,left,center,center,left,left");
    search.setHeaderAlign("center,center,center,center,center,center,center");
    search.setColTypes("ro,ro,ro,ro,ro,ro,ro");
    search.setColSorting("na,na,na,na,na,na,na");
    search.setEditable(true);
    search.enableMultiselect(true);
    search.setImagePath(getDefaultImagePath());
    search.enableTooltips("false,false,false,false,false,false,false");
    search.init();
    
    dwrCaller.executeAction('queryPublishColInfo',issueId,function(data){
		search.clearAll();
		search.parse(data,"json");
		search.checkAll(true);
	});
}


dhx.ready(init);




