var navMins=[0,0];
var moduleTabHeightFix=0;
var duckInitLevel=0;	//0自己    1下一级
var isIE=dhtmlx._isIE;

function getSortFalg(rptTab)
{
	var tabId=rptTab.tabId;
	if(!rptTab.table.autoPagination.value)return true;
	var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
	if(rptData.IS_LISTING)return false;
	if(rptTab.rptData[1].length>0)
		rptTab.table.currentRowCounts=rptTab.rptData[1][0][rptTab.rptData[0].length-1];//记录数
	if(rptTab.table.currentRowCounts && rptTab.table.currentRowCounts>200000)return false;
	return true;
}
function getDimTransCode(codeValue)
{
	var codes=codeValue.split(";");
	var transCode={};
	for(var i=0;i<codes.length;i++)
	{
		var code=codes[i].split(":");
		if(code.length==2)
			transCode[code[0]]=transCode[code[1]]
	}
	return transCode;
}
function buildTabColNames(rptTab)
{
	var selSortCol=sortSelCol(rptTab.table.columns.value);	//	返回排序后的数组
	if(selSortCol.length==0)
	{
		var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
		for(var i=0;i<rptData.colAttrs.value.length;i++)	//	默认全部显示
		{
			var col=new colConfig();
			var colAttr=rptData.colAttrs.value[i];
			col.colName.value=colAttr.name.value;
			col.index=colAttr.srcIndex;
			col.colNameCn=colAttr.nameCn;
			rptTab.table.columns.value[colAttr.name.value]=col;
		}
		selCols=rptTab.table.columns.value;
		selSortCol=sortSelCol(rptTab.table.columns.value);
	}
	if(selSortCol!=rptTab.table.colNames)
	{
		clearVarVal(rptTab.table.colNames);
		rptTab.table.colNames=selSortCol;
	}
}
//	同步页面内容宽高
function moduleTabContentSize(tabId,resize)
{
	if(!dhtmlx._isIE)
	{
		navMins[0]=22;
		navMins[1]=20;
	}
	var tabPage=$(tabId+"_content");
	if(!tabPage)return;
	var tabPagePar=tabPage.parentNode;
	tabPage.style.width=tabPagePar.offsetWidth+"px";
	tabPage.style.height=tabPagePar.offsetHeight+"px";
	var tabShowConter=$(tabId+'_tabShowConter');
	if(!tabShowConter)return;
	tabShowConter.style.width=tabPagePar.offsetWidth-2+"px";
	tabShowConter.style.height=tabPagePar.offsetHeight-2+"px";
	var dataPrivTable=$(tabId+'_dataPrivTable');
	if(!dataPrivTable)return;//预览框
	var tableDiv=$(tabId+'_tableDiv');
	var tablePaginationDiv=$(tabId+'_tablePaginationDiv');
	var graphDiv=$(tabId+'_graphDiv');
	var termConter=$(tabId+'_termConter');
	var rptTab=getAllTabs(tabId);
	//判断是否需要竖滚动条
	tableDiv.style.width=100+"px";	//表格框宽度
	tablePaginationDiv.style.width=100+"px";
	graphDiv.style.width=100+"px";	//图形框宽度
//	if(resize)
	if(dataPrivTable.offsetHeight>tabShowConter.offsetHeight)
		 dataPrivTable.style.width=tabShowConter.offsetWidth-25+"px";
	else 
		dataPrivTable.style.width=tabShowConter.offsetWidth-navMins[0]+"px";
	dataPrivTable.style.height=tabShowConter.offsetHeight-0-moduleTabHeightFix-navMins[1]+"px";
	var tabWidth=dataPrivTable.offsetWidth-2;
	var tabHeight=tabShowConter.offsetHeight-moduleTabHeightFix-navMins[1];
	
	tableDiv.style.width=tabWidth+"px";	//表格框宽度
	tablePaginationDiv.style.width=tabWidth+"px";
	graphDiv.style.width=tabWidth+"px";	//图形框宽度
	if(rptTab.table.width.value.indexOf("%")!=-1)
	{
		var w=parseInt(rptTab.table.width.value) * tabWidth/100;
		tableDiv.style.width=w+'px';
	}
	else
	{
		tableDiv.style.width=rptTab.table.width.value+"px";
	}
	var showType=rptTab.showType.value;
	var graphTr=$(tabId+'_graphTR');
	var tableTr=$(tabId+'_tableTR');
	var graphTD=$(tabId+'_graphTD');
	var tableTD=$(tabId+'_tableTD');
	var h=rptTab.table.height.value;
 	if(rptTab.table.autoPagination.value)
		tablePaginationDiv.style.display="";
	else
		tablePaginationDiv.style.display="none";
	if(showType==0)	//表格
	{
		if(h.indexOf("%")!=-1)
		{
			h=parseInt(h) * (tabHeight-termConter.offsetHeight-2)/100;
			if(h>tabHeight-termConter.offsetHeight)
				h=tabHeight-termConter.offsetHeight;
		}
		if(h<100)h=100;
		tableDiv.style.height=h-tablePaginationDiv.offsetHeight+"px";	//	不自动计算
		tableTD.style.height=h-tablePaginationDiv.offsetHeight+"px";
		graphTr.style.display="none";
		tableTr.style.display="";
		tableTD.style.height="100%";
		if(tableDiv)tableDiv.style.display="";
	}
	else if(showType==1)//图形
	{
		graphTr.style.display="";
		tableTr.style.display="none";
		graphTD.style.height="100%";
		if(tableDiv)tableDiv.style.display="none";
		$(rptTab.tabId+'_graphTR').style.display="";
		$(rptTab.tabId+'_tableTR').style.display="none";
		$(rptTab.tabId+'_graphTD').style.height="100%";
	}
	else if(showType==2)//表格+ 图形
	{
		if(h.indexOf("%")!=-1)
		{
			h=parseInt(h) * (tabHeight-termConter.offsetHeight-2)*6/10/100;//表格60%高  图形40%高
			if(h>tabHeight-termConter.offsetHeight)
				h=tabHeight-termConter.offsetHeight;
		}
		if(h<100)h=100;
		tableDiv.style.display="";
		tableDiv.style.height=h-tablePaginationDiv.offsetHeight+"px";	//	不自动计算
		tableTD.style.height=h-tablePaginationDiv.offsetHeight+"px";
		graphTr.style.display="";
		tableTr.style.display="";
		graphTD.style.height="40%";
		tableTD.style.height="50%";
	}
	if(!dhtmlx._isIE)tabShowConter.style.overflow="scroll";
	Debug("dataPrivTable.WH="+dataPrivTable.offsetWidth+":"+dataPrivTable.offsetHeight);
}
// 	获取条件宏变量值列表 1:条件 2:数据源 3:页面
function getMacroParams(type,obj)
{
	var params={};
	for(var tabId in reportConfig.termCfms.value)
	{
		var parName,parValue;
		var parName2,parValue2;
		var rptTerm=reportConfig.termCfms.value[tabId];
		Debug(tabId+" "+rptTerm.valueColName.value+" defaultValue:"+rptTerm.defaultValue.value);
		
		if(rptTerm.valueColName.value)// && !params[rptTerm.valueColName.value]
		{
			parName=rptTerm.valueColName.value;
			parValue=rptTerm.defaultValue.value[0];
			if(rptTerm.termType.value=="0" && parValue)parValue=rptTerm.defaultValue.value[1];
			else if(rptTerm.termType.value=="2" && parValue)parValue=(parValue+"").replace(/-/g,"");
		}
		if(rptTerm.textColName.value)// && !params[rptTerm.textColName.value]
		{
			parName2=rptTerm.textColName.value;
			parValue2=rptTerm.defaultValue.value[1];
			if(rptTerm.termType.value=="0" && parValue)parValue2=rptTerm.defaultValue.value[2];
		}
		parValue=parValue?parValue:"";
		parValue2=parValue2?parValue2:"";
		if($(tabId+'_term_privObj'))
		{
			if(rptTerm.termType.value=="0")
			{
				if($(tabId+'_term_privObj').selectedIndex!=-1)
				{
					parValue=$(tabId+'_term_privObj').options[$(tabId+'_term_privObj').selectedIndex].value+"";
					parValue2=$(tabId+'_term_privObj').options[$(tabId+'_term_privObj').selectedIndex].text;
				}
			}
			else if(rptTerm.termType.value=="1")
			{
				var val=$(tabId+'_term_privObj').getAttribute("code");
				val=val?val:parValue;
				parValue=val;
				parValue2=$(tabId+'_term_privObj').value+"";
			}
			else  if(rptTerm.termType.value=="2")
			{
				parValue=$(tabId+'_term_privObj').value.replace(/-/g,"")+"";
			}			
			else  if(rptTerm.termType.value=="5")
			{
				parValue=$(tabId+'_term_privObj').value.replace(/-/g,"")+"";
			}			
		}

		var find=false;
		switch(type)
		{
		case 1:	//条件
			rptTerm=obj;
			if(rptTerm.tabId!=tabId && rptTerm.parTermName.value!=tabId)break;
			find=true;
			break;
		case 2:	//	数据源,需要通过正则表达式判断宏变量
			var rptdata=obj;
			
			find=true;
			break;
		case 3:	//	页面
			var rptTab=obj;
			for(var i=0;i<rptTab.termNames.value.length;i++)
			{
				if(rptTab.termNames.value[i].value==tabId)
				{
					find=true;
					break;
				}
			}
			if(!find)
			for(var i=0;i<reportConfig.termNames.value.length;i++)
			{
				if(reportConfig.termNames.value[i].value==tabId)
				{
					find=true;
					break;
				}
			}
			break;
		default:
			find=true;
		break;
		}
//		Debug(parName+" find:"+find+" Value:"+parValue);
//		Debug(parName2+" find:"+find+" Value:"+parName2);
		if(find && parName2)params[parName2]=parValue2+"";
		if(find && parName )params[parName]=parValue+"";
	}
//	Debug("params begin");
//	Debug(params);
//	Debug("params end");
	return params;
}
function getTermAttrsMap(rptTab,params)
{
	var termAttrs={};
	if(rptTab.type=="reportModuleTab")
	{
		for(var tabId in reportConfig.termCfms.value)
		{
			termAttrs[tabId]={};
			var term=reportConfig.termCfms.value[tabId];
			termAttrs[tabId].termName=term.termName.value;
		}
	}
	else if(rptTab.type=="reportTerm")//
	{
		var term=rptTab;
		 termAttrs.termName=term.termName.value;
	}
	return termAttrs;
}
function getDataSrcMap(rptData)
{
	var dataSrcAttrs={};
	dataSrcAttrs.colAttrs=getDataSrcColsMap(rptData);

	return dataSrcAttrs;
}
function getDataSrcColsMap(rptData)
{
	var map={};
	for(var i=0;i<rptData.colAttrs.value.length;i++)
		map[rptData.colAttrs.value[i].name.value]=getDataSrcColMap(rptData.colAttrs.value[i]);
	return map;
}
function getDataTransRuleList(rptTab)
{
	var list=[];
	list[0]=rptTab.table.colNames.join(",");
	list[1]=rptTab.table.dataTransRule.value[0]?rptTab.table.dataTransRule.value[0].join(","):"";
	list[2]=rptTab.table.dataTransRule.value[1]?rptTab.table.dataTransRule.value[1].join(","):"";
	list[3]=rptTab.table.dataTransRule.value[2];
	return list;
}
function getDataSrcColMap(col)
{
	var map={};
	map.srcIndex=col.srcIndex;
	map.name=col.name.value;
	map.nameCn=col.nameCn.value;
	map.busType=col.busType.value;
	map.dbType=col.dbType.value;
	map.dimCodeTransSql=col.dimCodeTransSql.value;
	map.DIM_TABLE_ID=col.DIM_TABLE_ID;
	map.DIM_TYPE_ID=col.DIM_TYPE_ID;
	map.DIM_DATA_LEVELS=col.DIM_DATA_LEVELS.join(",");
	map.GDL_ID          =col.GDL_ID      ;
	map.TABLE_ID        =col.TABLE_ID    ;
	map.COL_ID          =col.COL_ID      ;
	map.GROUP_METHOD    =col.GROUP_METHOD;
	return map;
}
function formatTabCss(rptTab)
{
	var cssName=rptTab.table.cssName.value.split(",");
	if(!cssName[0])cssName[0]="";
	if(!cssName[1])cssName[1]="Head_TD";
	if(!cssName[2])cssName[2]="Row_TD";
	if(!cssName[3])cssName[3]="Alt_Row_TD";
	return cssName;
}
//	滚动条冻结表头和列
function moduleTabScroll(tabId,obj)
{
	try
	{
		var table=$(tabId+'_privTable_head');
		var tabHead=$(tabId+"_ShowTableHead");
		var top=0,left=0;
		if(obj.scrollTop)
			top=obj.scrollTop-1;
		if(obj.scrollLeft)
			left=obj.scrollLeft-1;
		if(isIE)
		{
			if(tabHead.offsetTop!=obj.scrollTop)//使用绝对定位
			{
				
			}
			return;
		}
		//先判断是否存在数据横纵转换 moduleTabScrollForTanrsData(tabId,obj)
		tabHead.style.position="relative";
		tabHead.style.top= top+"px";
		tabHead.style.zIndex=999992;
		var rptTab=getAllTabs(tabId);
		var rowLen=table.rows.length;
		var colFreeze=rptTab.table.colFreeze.value;
		if(!rptTab.table.colFreeze.map)
		{
			rptTab.table.colFreeze.map={};
			var selColIndex={};
			for(var c=0;c<table.colNames.length;c++)selColIndex[table.colNames[c]]=c;
			for(var c=0;c<colFreeze.length;c++)
			{
				rptTab.table.colFreeze.map[colFreeze[c]]=[];
				rptTab.table.colFreeze.map[colFreeze[c]][0]=c+1;
				rptTab.table.colFreeze.map[colFreeze[c]][1]=selColIndex[colFreeze[c]];
			}
			clearVarVal(selColIndex);
		}
		if(top)// || left
		{
			for(var i=0;i<table.colNames.length;i++)
			{
				var colObj=$(tabId+"_headTd_"+i);
				if(!colObj)continue;
				if(colObj.className.indexOf("freezeBorderBottom")<0)
					 colObj.className+=' freezeBorderBottom';
			}
			if(rptTab.table.totalType.value==2)
			{
				var colObj=$(tabId+"_headTd_"+table.colNames.length);
				if(colObj && colObj.className.indexOf("freezeBorderBottom")<0)
					 colObj.className+=' freezeBorderBottom';
			}
		}
		else
		{
			for(var i=0;i<table.colNames.length;i++)
			{
				var colObj=$(tabId+"_headTd_"+i);
				if(!colObj)continue;
				colObj.className=colObj.className.replace("freezeBorderBottom","").trim(); 
			}
			if(rptTab.table.totalType.value==2)
			{
				var colObj=$(tabId+"_headTd_"+table.colNames.length);
				if(colObj && colObj.className.indexOf("freezeBorderBottom")<0)
					colObj.className=colObj.className.replace("freezeBorderBottom","").trim(); 
			}
		}
		for(var i=0;i<table.colNames.length;i++)
		{
			var colName=table.colNames[i];
			var index=i;//rptTab.table.columns.value[colName].index;
			var colObj=$(tabId+"_headTd_"+index);
			if(!colObj)continue;
			var c=rptTab.table.colFreeze.map[colName];
			if(c)
			{
				colObj.style.position="relative";
				colObj.style.left= left+"px";
				colObj.style.zIndex=999992;
				 if(left && c[0]==colFreeze.length)
				{
					 if(colObj.className.indexOf("freezeBorderRight")<0)
						colObj.className+=" freezeBorderRight";
				}
				 else if(left==0)
					 colObj.className=colObj.className.replace("freezeBorderRight","").trim();
			}
		}
		if(colFreeze.length)
		for( var r=0;r<rowLen;r++)	//行头
		{
			for(var c=0;c<colFreeze.length;c++)
			{
				var index=rptTab.table.colFreeze.map[colFreeze[c]][1];//rptTab.table.columns.value[colFreeze[c]].index;
				//table.colNames
				var colObj=$(tabId+"_rowTd_"+r+"_"+index);
				if(colObj)//由于合并可能不存在
				{
					colObj.style.position="relative";
					colObj.style.left= left+"px";
					colObj.style.zIndex=999991;
					if(c+1==colFreeze.length)
					{
						if(left)
						{
							if(colObj.className.indexOf("freezeBorderRight")<0)
								colObj.className+=" freezeBorderRight";
						}
						else
						{
							colObj.className=colObj.className.replace("freezeBorderRight","").trim();
						}
					}
				}
			}
		}
//		if(rptTab.table.totalType.value==1)//列合计
//			for(var c=0;c<colFreeze.length;c++)
//			{
//				var index=rptTab.table.colFreeze.map[colFreeze[c]][1];//rptTab.table.columns.value[colFreeze[c]].index;
//				//table.colNames
//				var colObj=$(tabId+"_rowTd_"+rowLen+"_"+index);
//				if(colObj)//由于合并可能不存在
//				{
//					colObj.style.position="relative";
//					colObj.style.left= left+"px";
//					colObj.style.zIndex=999991;
//					if(c+1==colFreeze.length)
//					{
//						if(left)
//						{
//							if(colObj.className.indexOf("freezeBorderRight")<0)
//								colObj.className+=" freezeBorderRight";
//						}
//						else
//						{
//							colObj.className=colObj.className.replace("freezeBorderRight","").trim();
//						}
//					}
//				}
//			}
	}
	catch(ex)
	{
		
	}
}
