var modTabShowTableType=0;

function refreshTabData(tabId)
{
	if(tabId)
		return readTabData(tabId);
	var rptTab=getAllTabs();
	for(var i=0;i<rptTab.length;i++)
		readTabData(rptTab[i].tabId);
}

//绑定页面数据
function bindTabDataPriv(rptTab)
{
	var tabId=rptTab.tabId;
	if(rptTab.rptData)
	{
//		if(!rptTab["showType"].inited)setPrivTableShowType(rptTab);
		if(rptTab.showType.value==0 || rptTab.showType.value==2)
		{
			bindTabData(rptTab,rptTab.rptData);
		}
		if(rptTab.showType.value==1 || rptTab.showType.value==2)
			bindGraphPriv(rptTab,rptTab.rptData);
	}
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

////绑定表格展示头
//function bindTabDataPrivHead(rptTab)
//{
//	var tabId=rptTab.tabId;
//	var colums=rptTab.rptData[0];
//	var cssName=formatTabCss(rptTab);
//	var headDiv=$(tabId+'_tableHeadDiv');
//	var strHtml='<table '+(cssName[0]?cssName[0]:' cellpadding="1" cellspacing="1" border="0" ')+' align=center class="'+cssName[0]
//			+'" id="'+tabId+'_privTable_head" style="background-color:'+rptTab.table.backGroundColor.value+';">';	//表,表头,行,交替行
// 
//	var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
//	var selCols=rptTab.table.columns.value;
//	strHtml+="<tr class='"+cssName[1]+"' style='background-color:"+rptTab.table.tabHeadBackColor.value+";color:"
//			+rptTab.table.tabHeadForeColor.value+";'>";
//	var selSortCol=rptTab.table.colNames;
// 	for(var i=0;i<selSortCol.length;i++)
//	{
//		var colName=selSortCol[i];
//		var colAttr=rptData.colMap[colName];
//		var colPro=selCols[colName];
//		var colDisplayAttr="";
//		if(colPro.width.value && colPro.width.value!="0")colDisplayAttr+="width:"+colPro.width.value+"px;";
//		if(colPro.backColor.value)colDisplayAttr+="background-color:"+colPro.backColor.value;
//		strHtml+="<td nowrap align=center id='"+tabId+"_headTd_"+i+"' style='padding-left:0px;padding-right:0px;"+colDisplayAttr+"'>"+
//				(colAttr.nameCn.value?colAttr.nameCn.value:colName)+"</td>";
//	}
//	strHtml+="</tr></table>";
//	headDiv.innerHTML=strHtml;
//	rptTab.table.colWidth=[];
//	for(var i=0;i<selSortCol.length;i++)
//	{
//		rptTab.table.colWidth[i]=$(tabId+"_headTd_"+i).offsetWidth+10;
//		$(tabId+"_headTd_"+i).style.width=rptTab.table.colWidth[i]+"px";
//	}
//}
////绑定数据行
//function bindTabDataPrivRow(rptTab)
//{
//	var tabId=rptTab.tabId;
//	var rows=rptTab.rptData[1];
//	var cssName=formatTabCss(rptTab);
//	var contentDiv=$(tabId+'_tableRowDiv');
//	var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
//	var selCols=rptTab.table.columns.value;
//	var colNames= rptTab.table.colNames;
//	
//	var colAttrs=[];
//	var colPros=[];
//	var colDisplayAttrs=[];
//	for(var i=0;i<colNames.length;i++)
//	{
//		var colName=colNames[i];
//		colAttrs[i]=rptData.colMap[colName];
//		colPros[i]=selCols[colName];
//		colDisplayAttrs[i]="";
//		if(colAttrs[i].rowUnited)colAttrs[i].rowUnited=false;
//		if(parseInt(colPros[i].width.value))colDisplayAttrs[i]+="width:"+colPros[i].width.value+"px;";
//		if(colPros[i].backColor.value)colDisplayAttrs[i]+="background-color:"+colPros[i].backColor.value;
//		for (var n = 0; n < rptTab.table.rowUniteCols.value.length; n++)
//		{
//			if(colName==rptTab.table.rowUniteCols.value[n])
//			{
//				colAttrs[i].rowUnited=true;
//				break;
//			}
//		}
//	}	
//	var strHtml='<table '+(cssName[0]?cssName[0]:' cellpadding="1" cellspacing="1" border="0" ')+'  align=center  class="'+cssName[0]
//			+'"  id="'+tabId+'_privTable_rows" style="background-color:'+rptTab.table.backGroundColor.value+';">';
//	for( var r=0;r<rows.length;r++)
//	{
//		strHtml+="<tr  class='"+(r%2==0?cssName[2]:cssName[3])+"' style='background-color:"+
//				(r%2==0?rptTab.table.rowBackColor.value:rptTab.table.rowAlternantBackColor.value)+
//				"'  onmouseover='tr_onmouseover(this)' onmouseout='tr_onmouseoutR(this)'>";	//tabHeadBackColor
//		for(var i=0;i<colNames.length;i++)
//		{
//			var si=colAttrs[i].srcIndex;
//			var data=rows[r][si];
//			var sameVals = 1;
//			if (colAttrs[i].rowUnited)
//			{
//				if (r > 0 && rows[r-1][si]==data)
//					continue;
//				for (var n = r+1; n < rows.length; n++)
//				{
//					if (rows[n][si]==data)
//						sameVals++;
//					else
//						break;
//				}
//			}
//			var style="";
//			var tdAttr="";
//			if(colAttrs[i].dbType.value==0)style+="text-align:right;";
//			if(colPros[i].alertTerms.value.length>0)style=alertPrivDataCol(rows[r],colPros[i],colAttrs[i]);	//	一行数据用于预警计算
//			if(colPros[i].dataFormart.value)data=formatPrivDataCol(data,colPros[i].dataFormart.value);
//			if (sameVals > 1)
//				tdAttr += " rowspan=" + sameVals;
//			strHtml+="<td id='"+tabId+"_rowTd_"+r+"_"+i+"' "+tdAttr+" style='padding-left:0px;padding-right:0px;"+colDisplayAttrs[i]+
//						style+"'>&nbsp;"+data+"&nbsp;</td>";
//		}
//		strHtml+="</tr>";
//	}
//	strHtml+="</table>";
//	contentDiv.innerHTML=strHtml;
//	contentDiv.style.width=$(tabId+'_tableHeadDiv').offsetWidth+"px";
//	var width=0;
//	for(var i=0;i<rptTab.table.colWidth.length;i++)width+=rptTab.table.colWidth[i];
//	if(rows.length)
//	{
//		for(var i=0;i<colNames.length;i++)
//		{
//			$(tabId+"_rowTd_0_"+i).style.width=rptTab.table.colWidth[i]+"px";
//			if($(tabId+"_headTd_"+i).offsetWidth < $(tabId+"_rowTd_0_"+i).offsetWidth)
//			{
//				$(tabId+"_headTd_"+i).style.width=$(tabId+"_rowTd_0_"+i).offsetWidth+"px";
//				rptTab.table.colWidth[i]=$(tabId+"_rowTd_0_"+i).offsetWidth;
//			}
//		}
//		contentDiv.style.width=$(tabId+'_tableHeadDiv').offsetWidth+"px";
//		var width=0;
//		for(var i=0;i<rptTab.table.colWidth.length;i++)width+=rptTab.table.colWidth[i];
//	}
//	if(width < $(tabId+'_tableDiv').offsetWidth)
//	{
//		var headDiv=$(tabId+'_tableHeadDiv');
//		headDiv.style.width="100%";
//		contentDiv.style.width="100%";
//		$(tabId+'_privTable_head').style.width="100%";
//		$(tabId+'_privTable_rows').style.width="100%";
//	}
//}
////滚动条冻结表头和列
//function moduleTabScroll(tabId,obj)
//{
//	try
//	{
//		var top=0,left=0;
//		if(obj.scrollTop)
//			top=obj.scrollTop-1;
//		if(obj.scrollLeft)
//			left=obj.scrollLeft-1;
//		var rptTab=getAllTabs(tabId);
//		var rowLen=rptTab.rptData[1].length;
//		var colFreeze=rptTab.table.colFreeze.value;
//		if(modTabShowTableType)
//		{
//			var headDiv=$(tabId+'_tableHeadDiv');
//			if(headDiv)headDiv.style.top= top+"px";
//		}
//		else
//		{
//			$(tabId+"_ShowTableHead").style.position="relative";
//			$(tabId+"_ShowTableHead").style.top= top+"px";
//			$(tabId+"_ShowTableHead").style.zIndex=999992;
//		}
//		if(!rptTab.table.colFreeze.map)
//		{
//			rptTab.table.colFreeze.map={};
//			for(var c=0;c<colFreeze.length;c++)
//				rptTab.table.colFreeze.map[colFreeze[c]]=c+1;
//		}
//		
//		for(var i=0;i<rptTab.rptData[0].length;i++)
//		{
//			var colObj=$(tabId+"_headTd_"+i);
//			if(!colObj)continue;
//			if(top)// || left
//			{
//				if(colObj.className.indexOf("freezeBorderBottom")<0)
//					 colObj.className+=' freezeBorderBottom';
//			}
//			else
//			{
//				colObj.className=colObj.className.replace("freezeBorderBottom","").trim(); 
//			}	
//		}
//		for(var i=0;i<rptTab.rptData[0].length;i++)
//		{
//			var colName=rptTab.rptData[0][i];
//			var index=rptTab.table.columns.value[colName].index;
//			var colObj=$(tabId+"_headTd_"+index);
//			if(!colObj)continue;
//			var c=rptTab.table.colFreeze.map[colName];
//			if(c)
//			{
//				colObj.style.position="relative";
//				colObj.style.left= left+"px";
//				colObj.style.zIndex=999992;
//				 if(left && c==colFreeze.length)
//				{
//					 if(colObj.className.indexOf("freezeBorderRight")<0)
//						colObj.className+=" freezeBorderRight";
//				}
//				 else if(left==0)
//					 colObj.className=colObj.className.replace("freezeBorderRight","").trim();
//			}
//		}
//		for( var r=0;r<rowLen;r++)	//行头
//		{
//			for(var c=0;c<colFreeze.length;c++)
//			{
//				var index=rptTab.table.columns.value[colFreeze[c]].index;
//				//rptTab.table.colNames
//				var colObj=$(tabId+"_rowTd_"+r+"_"+index);
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
//		}
//	}
//	catch(ex)
//	{
//		
//	}
//}
