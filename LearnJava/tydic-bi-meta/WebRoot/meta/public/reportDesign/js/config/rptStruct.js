////	绑定表格展示头
//function bindTabDataPrivHead(rptTab)
//{
//	var sortFlag=getSortFalg(rptTab);
//	var tabId=rptTab.tabId;
//	var colums=rptTab.rptData[0];
//	var cssName=formatTabCss(rptTab);
//	var headDiv=$(tabId+'_tableHeadDiv');
//	var strHtml='<table '+(cssName[0]?cssName[0]:' cellpadding="1" cellspacing="1" border="0" ')+' align=center class="'+cssName[0]
//			+'" id="'+tabId+'_privTable_head" style="background-color:'+rptTab.table.backGroundColor.value+';">';	//表,表头,行,交替行
// 
//	var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
//	var selCols=rptTab.table.columns.value;
//	var selSortCol=rptTab.table.colNames;
////	var selSortCol=sortSelCol(selCols);	//	返回排序后的数组
////	if(selSortCol.length==0)
////	{
////		rptTab.table.columns.value={};	//	清空列属性设置
////		for(var i=0;i<rptData.colAttrs.value.length;i++)	//	默认全部显示
////		{
////			var col=new colConfig();
////			var colAttr=rptData.colAttrs.value[i];
////			col.colName.value=colAttr.name.value;
////			col.index=colAttr.srcIndex;
////			col.colNameCn=colAttr.nameCn;
////			rptTab.table.columns.value[colAttr.name.value]=col;
////		}
////		selCols=rptTab.table.columns.value;
////		selSortCol=sortSelCol(selCols);
////	}
////							
////	rptTab.table.colNames=selSortCol;
//	strHtml+="<tr class='"+cssName[1]+"' style='background-color:"+rptTab.table.tabHeadBackColor.value+";color:"
//			+rptTab.table.tabHeadForeColor.value+";'>";
// 	for(var i=0;i<selSortCol.length;i++)
//	{
//		var colName=selSortCol[i];
//		var colAttr=rptData.colMap[colName];
//		var colPro=selCols[colName];
//		var colDisplayAttr="";
//		if(colPro.width.value && colPro.width.value!="0")colDisplayAttr+="width:"+colPro.width.value+"px;";
//		if(colPro.backColor.value)colDisplayAttr+="background-color:"+colPro.backColor.value;
//		var colNameCn=colAttr.nameCn.value?colAttr.nameCn.value:colName;
//		if(sortFlag)
//		{
//			colNameCn="<a href='javascript:void(0);' class='bottom' onclick='tabTableSorting(\""+tabId+"\",\""+colName+"\",this);return false;'>"+colNameCn+"</a>";
//			if(rptTab.table.orderByColumn==colName)
//			{
//				if(rptTab.table.orderDircetion=="asc")
//					colNameCn+="<span style='color:#FF0000'>↓</span>";
//				else
//					colNameCn+="<span style='color:#FF0000'>↑</span>";
//			}
//			
//		}
//		strHtml+="<td nowrap align=center id='"+tabId+"_headTd_"+i+"' style='position:relative;padding-left:0px;padding-right:0px;"+
//				colDisplayAttr+"'>"+colNameCn+"</td>";
//	}
// 	if(rptTab.table.totalType.value==2)//行合计，需要加一列
// 		strHtml+="<td nowrap align=center id='"+tabId+"_headTd_"+selSortCol.length+"' style='padding-left:0px;padding-right:0px;"+colDisplayAttr+"'>合 计</td>";
//	strHtml+="</tr></table>";
//	headDiv.innerHTML=strHtml;
//	rptTab.table.colWidth=[];
//	for(var i=0;i<selSortCol.length;i++)
//	{
//		rptTab.table.colWidth[i]=$(tabId+"_headTd_"+i).offsetWidth+10;
//		$(tabId+"_headTd_"+i).style.width=rptTab.table.colWidth[i]+"px";
//	}
//	if(rptTab.table.totalType.value==2)
//	{
//		rptTab.table.colWidth[selSortCol.length]=$(tabId+"_headTd_"+selSortCol.length).offsetWidth+10;
//		$(tabId+"_headTd_"+selSortCol.length).style.width=rptTab.table.colWidth[selSortCol.length]+"px";
//	}
////	if(parseInt(rptTab.table.totalType.value)==1)$(tabId+'_tableRowDiv').style.display="";
////	else $(tabId+'_tableRowDiv').style.display="none";
//}
////	绑定数据行
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
//	var colTotal=[];
//	for(var i=0;i<colNames.length;i++)
//	{
//		var colName=colNames[i];
//		colAttrs[i]=rptData.colMap[colName];
//		colPros[i]=selCols[colName];
//		colTotal[i]=colPros[i].totalTxt.value?colPros[i].totalTxt.value:0;
//		if(colAttrs[i].busType.value!=1)
//			colTotal[i]=colTotal[i]?colTotal[i]:"-";
//		colDisplayAttrs[i]="";
//		if(colAttrs[i].rowUnited)colAttrs[i].rowUnited=false;
//		if(parseInt(colPros[i].width.value))colDisplayAttrs[i]+="width:"+colPros[i].width.value+"px;";
//		if(colPros[i].backColor.value)colDisplayAttrs[i]+="background-color:"+colPros[i].backColor.value+";";
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
//		
//		var dataTotal=0;
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
//			if(rptTab.table.totalType.value==2 && colAttrs[i].busType.value==1)dataTotal+=parseFloat(data);
//			if(rptTab.table.totalType.value==1 && !colPros[i].totalTxt.value && colAttrs[i].busType.value==1)//列合计
//				colTotal[i]+=parseFloat(data);
//			var style="";
//			var tdAttr="";
//			if(colAttrs[i].dbType.value==0 || colAttrs[i].busType.value==1)style+="text-align:right;";
//			if(colPros[i].alertTerms.value.length>0)style+=alertPrivDataCol(rows[r],colPros[i],colAttrs[i]);	//	一行数据用于预警计算
//			if(colPros[i].dataFormart.value)data=formatPrivDataCol(data,colPros[i].dataFormart.value);
//			if (sameVals > 1)
//				tdAttr += " rowspan=" + sameVals;
//			strHtml+="<td id='"+tabId+"_rowTd_"+r+"_"+i+"' "+tdAttr+" style='padding-left:0px;padding-right:0px;"+colDisplayAttrs[i]+
//						style+"'>&nbsp;"+data+"&nbsp;</td>";
//		}
//		if(rptTab.table.totalType.value==2)//行合计，需要加一列
//		{
//			strHtml+="<td id='"+tabId+"_rowTd_"+r+"_"+colNames.length+"' style='padding-left:0px;padding-right:0px;text-align:right;'>&nbsp;"+
//					formatPrivDataCol(dataTotal,",###.##")+"&nbsp;</td>";
//		}
//		strHtml+="</tr>";
//	}
//	if(rptTab.table.totalType.value==1)//添加一行
//	{
//		strHtml+="<tr  class='"+(rows.length%2==0?cssName[2]:cssName[3])+"' style='background-color:"+
//				(rows.length%2==0?rptTab.table.rowBackColor.value:rptTab.table.rowAlternantBackColor.value)+
//				"'  onmouseover='tr_onmouseover(this)' onmouseout='tr_onmouseoutR(this)'>";
//		for(var i=0;i<colNames.length;i++)
//		{
//			var style="text-align:left;";
//			if(colAttrs[i].dbType.value==0 || colAttrs[i].busType==1)style="text-align:right;";
//			if(typeof colTotal[i]=="number")
//				colTotal[i]=formatPrivDataCol(colTotal[i],",###.##");
//			strHtml+="<td id='"+tabId+"_rowTd_"+rows.length+"_"+i+"' style='padding-left:0px;padding-right:0px;"+colDisplayAttrs[i]+
//					style+"'>&nbsp;"+colTotal[i]+"&nbsp;</td>";
//		}
//		strHtml+="</tr>";
//	}
//	strHtml+="</table>";
//	contentDiv.innerHTML=strHtml;
//	contentDiv.style.width=$(tabId+'_tableHeadDiv').offsetWidth+"px";
////	return;
//	var width=0;
//	for(var i=0;i<rptTab.table.colWidth.length;i++)width+=rptTab.table.colWidth[i];
//	if(rows.length || rptTab.table.totalType.value==1)
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
//		if(rptTab.table.totalType.value==2)
//		{
//			$(tabId+"_rowTd_0_"+colNames.length).style.width=rptTab.table.colWidth[colNames.length]+"px";
//			$(tabId+"_headTd_"+colNames.length).style.width=$(tabId+"_rowTd_0_"+colNames.length).offsetWidth+"px";
//			rptTab.table.colWidth[colNames.length]=$(tabId+"_rowTd_0_"+colNames.length).offsetWidth;
//		}
//		contentDiv.style.width=$(tabId+'_tableHeadDiv').offsetWidth+"px";
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