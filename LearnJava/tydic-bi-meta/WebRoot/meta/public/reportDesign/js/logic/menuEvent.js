
//新建报表
function createNewRpt()
{
	//清空数据和配置
}
//打开报表
function openRpt()
{
	
}
var saving=false;
//保存报表
function saveRpt(rpType)
{
	if(saving)return;
	try
	{
	saving=true;
 	showGuage(true,"数据保存中……");
	var waitSpan=$("waitDivSpan");
	waitSpan.innerHTML+="<br/>开始解析脚本规则……";
	
	//序列化之前转换时间默认值保存
	var termDefaultVals={};
	for(var term in reportConfig.termCfms.value)
	{
		var rptTerm=reportConfig.termCfms.value[term];
		if(typeof rptTerm.defaultValue.value=="string")rptTerm.defaultValue.value=rptTerm.defaultValue.value.split(",");
		if(rptTerm.termType.value=="2")
		{
			termDefaultVals[term]=rptTerm.defaultValue.value[0];
			var d=parseInt(rptTerm.defaultValue.value[0].replace(/-/g,""));
			if(!rptTerm.defaultValue.value[0] || !d)
				rptTerm.defaultValue.value[0]=-1;
			else
			{
				var dt2=new Date();//86400000
				var m=new Date(d/10000,(d%10000)/100-1,d%100);//
				//dt2.getFullYear()*10000+(dt2.getMonth()+1)*100+dt2.getDate();
				rptTerm.defaultValue.value[0]=parseInt((m-dt2)/86400000);
			}
		}
	}
	var rptJsonData=window.rptJsonData=JSON.stringify(reportConfig,toJsonFilter);
	for(var term in reportConfig.termCfms.value)//还原时间默认值
		if(termDefaultVals[term])reportConfig.termCfms.value[term].defaultValue.value[0]=termDefaultVals[term];
	var rptCfm={};//全局属性
	if(rpType)
		rptCfm.REPORT_ID=0;
	else
		rptCfm.REPORT_ID=reportConfig.REPORT_ID;
	rptCfm.REPORT_NAME=reportConfig.rptTitle.title.value;
	rptCfm.SHOW_NAME_FLAG=reportConfig.rptTitle.showFlag.value?1:0;
	rptCfm.REPORT_DESC=reportConfig.reportDesc.value;
	rptCfm.USER_ID =userId;
	rptCfm.TERM_ROW_SIZE=reportConfig.termRowSize.value;
	rptCfm.USER_DEFINE_CSS=reportConfig.styleCssText.value;
	rptCfm.QUERY_TERM_IDS=getListVal(reportConfig.termNames,"value");//条件ID列表
	rptCfm.CLIENT_JS =rptJsonData;
	rptCfm.modules=getListVal(reportConfig.modules,"modId");//模块ID列表;
	rptCfm.dataCfms=getListVal(reportConfig.dataCfms,"tabId");//数据源ID列表;
	rptCfm.termCfms=getListVal(reportConfig.termCfms,"tabId");//条件ID列表;
	rptCfm.REPORT_TITLE_CFG=JSON.stringify(reportConfig.rptTitle,toJsonFilter);	//报表展现使用

	var termCfm={};//条件集合
	for(var term in reportConfig.termCfms.value)
	{
		if(term=="type")continue;
		var tmCfm=reportConfig.termCfms.value[term];
		termCfm[term]={};
		var tm=termCfm[term];
		tm.PARANT_TERM_ID=tmCfm.parTermName.value;
		tm.TERM_LABEL=tmCfm.termName.value;
		tm.TERM_TYPE=tmCfm.termType.value;
		tm.TERM_TYPE=tmCfm.termType.value;
		tm.VALUE_TYPE=tmCfm.valueType.value;
		tm.VALUE_COLNAME=tmCfm.valueColName.value;
		tm.TEXT_COLNAME=tmCfm.textColName.value;
		tm.SHOW_LENGTH=tmCfm.showLength.value;
		tm.SRC_TYPE=tmCfm.srcType.value;
		tm.QUERY_DATA=tmCfm.data.value;
		tm.DATA_SOURCE_ID=tmCfm.dataSrcId.value;
		tm.DEFAULT_VALUE=tmCfm.defaultValue.value.join();
		if(tmCfm.termType.value=="2")
		{
			var d=parseInt(tmCfm.defaultValue.value[0].replace(/-/g,""));
			if(tm.DEFAULT_VALUE=="" || !d)
				tm.DEFAULT_VALUE=-1;
			else
			{
				var dt2=new Date();//86400000
				var m=new Date(d/10000,(d%10000)/100-1,d%100);//
				//dt2.getFullYear()*10000+(dt2.getMonth()+1)*100+dt2.getDate();
				tm.DEFAULT_VALUE=parseInt((m-dt2)/86400000);
			}
		}
		tm.APPEND_TEXT=tmCfm.appendText.value;
		tm.APPEND_VALUE=tmCfm.appendValue.value;
		tm.DIM_DATA_LEVELS=tmCfm.dimDataLevels.value.join();
		tm.DIM_TABLE_NAME=tmCfm.DIM_TABLE_NAME;
		tm.DIM_TYPE_ID=tmCfm.DIM_TYPE_ID;
		tm.DIM_TABLE_ID=tmCfm.DIM_TABLE_ID;
	}
	var dataCfm={};//数据源集合
	var dataCfmCols={};
	for(var dataId in reportConfig.dataCfms.value)
	{
		if(dataId=="type")continue;
		var dtCfm=reportConfig.dataCfms.value[dataId];
		dataCfm[dataId]={};
		var tm=dataCfm[dataId];
		tm.DATA_NAME=dtCfm.dataName.value;
		tm.TABLE_ID=dtCfm.TABLE_ID;
		tm.TABLE_NAME=dtCfm.mainDataTab.value;
		tm.DATA_SRC_TYPE=dtCfm.dataSrcType.value;
		tm.DATA_SOURCE_ID=dtCfm.dataSrcId.value;
		tm.DATA_QUERY_SQL=dtCfm.dataSql.value;
//		tm.DATA_TRANS_RULE=dtCfm.dataTransRule.value;
		tm.APPEND_GDLDESC=dtCfm.indexExegesisContent.value;
		
		dataCfmCols[dataId]=[];
		var dataCfmCol=dataCfmCols[dataId];
		for(var i=0;i<dtCfm.colAttrs.value.length;i++)
		{
			var col=dtCfm.colAttrs.value[i];
			dataCfmCol[i]={};
			var dcol=dataCfmCol[i];
			dcol.COL_NAME=col.name.value;
			dcol.COL_NAME_CN=col.nameCn.value;
			dcol.COL_BUS_TYPE=col.busType.value;
			dcol.COL_BUS_COMMENT=col.indexExegesis.value;

			dcol.COL_NAME=col.name.value;
			dcol.COL_NAME=col.name.value;
			dcol.COL_NAME=col.name.value;
			dcol.DIM_TABLE_ID=col.DIM_TABLE_ID;
			dcol.DIM_TYPE_ID= col.DIM_TYPE_ID;
			dcol.DIM_DATA_LEVELS=col.DIM_DATA_LEVELS.join();
			dcol.GDL_ID=col.GDL_ID;
			dcol.TABLE_ID=col.TABLE_ID;
			dcol.COL_ID=col.COL_ID;
			dcol.GROUP_METHOD=col.GROUP_METHOD;
		}
	}
	var moduleCfm={};//模块集合
	var rptTab={};//模块页集合
	for(var i=0;i<reportConfig.modules.value.length;i++)
	{
		var mod=reportConfig.modules.value[i];
		moduleCfm[mod.modId]={};
		var mdCfm=moduleCfm[mod.modId];
		mdCfm.MODULE_TITLE=mod.moduleName.value;
		mdCfm.MUL_PANNEL=mod.tabPannel.value?1:0;
		mdCfm.WINDOW_SHOW_PARAMS=JSON.stringify(mod.window,toJsonFilter);
		rptTab[mod.modId]=[];//	模块多Tab页
		var modTabs=rptTab[mod.modId];
		for(var tab in mod.tabs)
		{
			if(tab=="type")continue;
			modTabs[modTabs.length]={};
			var tab=mod.tabs[tab];
			var modTab=modTabs[modTabs.length-1];
			modTab.ORDER_ID=modTabs.length;
			
			modTab.TAB_NAME=tab.tabName.value;
			modTab.RPT_DATA_ID=tab.dataSrcName.value;
			modTab.SHOW_FLAG=tab.showFlag.value?1:0;
			modTab.SHOW_TYPE=tab.showType.value;
			modTab.QUERY_TERM_IDS=getListVal(tab.termNames,"value");
			modTab.TERM_ROW_SIZE=tab.termRowSize.value;
			modTab.DOWNLOAD_FLAG=tab.downLoadFlag.value?1:0;
			modTab.LINK_RPT_ID=tab.linkRptId.value;
			modTab.LINK_OPEN_TYPE=tab.linkOpenType.value;
			modTab.LINK_MODULE_IDS=tab.linkModelIds.value.join();
			modTab.LINK_TYPE=tab.linkType.value;
			modTab.IN_PARAMS=getListVal(tab.inParams.value);
			modTab.OUT_PARAMS=getListVal(tab.outParams.value);
			modTab.TABLE_SHOW_COLS= sortSelCol(tab.table.columns.value).join();	
			
			modTab.TABLE_WIDTH=tab.table.width.value;
			modTab.TABLE_HEIGHT=tab.table.height.value;
			modTab.TABLE_COLORS=tab.table.backGroundColor.value+";"+tab.table.tabHeadBackColor.value+";"
				+tab.table.tabHeadForeColor.value+";"+tab.table.rowBackColor.value+";"+tab.table.rowAlternantBackColor.value ;
			modTab.TABLE_CSSNAMES=tab.table.cssName.value;
			modTab.TABLE_COL_CONFIGS=JSON.stringify(tab.table.columns.value,toJsonFilter);;
			modTab.DUCK_COLNAME=tab.table.duckColName.value;
			modTab.ROW_UNITE_COLS=tab.table.rowUniteCols.value.join();
			modTab.COL_UNITE_RULE=tab.table.colUniteRule.value;
//			modTab.COLLECT_FLAG=tab.table.colTotal.value?1:0;
			modTab.GRAPH_TYPE=tab.graph.graphType.value;
			modTab.GRAPH_PARAMS=tab.graph.graphParams;
//			alert(modTab.TABLE_COL_CONFIGS.length);
		}
	}
	
	waitSpan.innerHTML+="<br/>开始传输数据保存……";
	ReportDesignerAction.saveRpt(rptCfm,termCfm,dataCfm,dataCfmCols,moduleCfm,rptTab,function(res){
			showGuage();
			saving=false;
			clearVarVal([rptCfm,termCfm,dataCfm,dataCfmCols,moduleCfm,rptTab]);
			if(!res || res[0]=="false")
			{
				alert("保存数据发生错误："+(res?res[1]:res));
				return;
			}
			reportConfig.REPORT_ID=res[2];
			alert(res[1]+" Ctrl+C 复制预览地址：\n"+previewUrl+reportConfig.REPORT_ID);
//			rptTab.rptData=res;	//	保存数据
//			bindTabDataPriv(tabId,keys,rptTab);	//	绑定下拉框预览数据
		});
	}
	catch(ex)
	{
		if(typeof ex=="string")alert(ex);
		else
		{
			var msg="";
			for(var a in ex)
				msg+=a+":"+ex[a]+"\n";
			alert(msg);
		}
		saving=false;
	}
//
}
//保存报表
function saveAsRpt()
{
	saveRpt(true);
}
function rptClose()
{
	
}
function previewRpt()
{
	window.rptJsonData=JSON.stringify(reportConfig,toJsonFilter);
	winopen(previewUrl+"&previewRpt=designer","previewRpt");
}
//添加模块
function addModel()
{
	AddRptModel();
}
