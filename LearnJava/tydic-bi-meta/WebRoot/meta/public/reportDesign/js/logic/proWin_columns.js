//绑定表格列设置对话框
function bindRptTabColumns(rptTab)
{
	if(!rptTab)rptTab=currentObj.obj;
	var tabId=rptTab.tabId;
	proWin.setText(rptTab.tabName.value+"-列属性设置对话框");
	proWin.attachHTMLString("<div id='"+tabId+"_columns_proWin' style='width:100%;height:100%;overflow:auto;'onscroll='columnsTabScroll(\""+tabId+"\",this)'>列属性设置对话框</div>");
	proWin.proBox=$(tabId+"_columns_proWin");
	var box=proWin.proBox;
	var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
	if(!rptData)
	{
		alert("请先选择数据源");
		proWin.setModal(false);
		proWin.hide();
		return;
	}
	if(!rptData.ready)	//	等待
	{
		alert("选择的数据源未准备好，请检查是否存在错误");
		proWin.setModal(false);
		proWin.hide();
		return;
	}
	proWin.setDimension(600,250);
	proWin.center();
	proWin.setDimension(800,400);
//	proWin.setPosition(bodyWidth-900,(bodyHeight-400)/2); 
	
	var cols=rptData.colAttrs.value;	//	全字段
	var selCols=rptTab.table.columns.value;
	var disabled=rptTab.table.columns.readonly?" disabled ":"";
	var selSortCol=sortSelCol(selCols);	//	返回排序后的数组
	var strHtml='<TABLE cellpadding="1" cellspacing="1" border="0" width=100% class="proWinColumnAttrTable" id="'+tabId+'_columns_table" ><tr id="'+tabId+'_columns_headTr">';
	strHtml+='<td nowrap class="dataSrcColAttrHead dataSrcColAttrTD" style="width:15px;"><input '+disabled+' id='+tabId+'_all_chk type=checkbox onclick="changeColSelAll()"/><label for='+tabId+'_all_chk style="display:none;">是否显示</label></td>';
	var col=null;
	if(selSortCol.length==0)
		col=new colConfig();
	else
		col=selCols[selSortCol[0]];
	strHtml+=bindRptTabColumnHead(rptTab,col);
	if(selSortCol.length==0){col=null;}
	strHtml+='<td nowrap class="dataSrcColAttrHead" style="width:40px;" align="center"><img class=hand src="images/arrowUp.gif" onclick="moveColRow(1)" /> <img  class=hand  src="images/arrowDown.gif"  onclick="moveColRow(0)"/> </td></tr>';
	proWin.bindTabId=tabId;
	proWin.bindData=[];
	for(var i=0;i<selSortCol.length;i++)
	{
		var col=selCols[selSortCol[i]];
		strHtml+=bindRptTabColumnRow(rptTab,col,rptData,true);
		proWin.bindData[proWin.bindData.length]=col;
	}
	for(var i=0;i<cols.length;i++)
	{
		var colAttr=cols[i];
		if(selCols[colAttr.name.value])continue;
		var col=new colConfig();
		col.colName.value=colAttr.name.value;
		col.colNameCn=colAttr.nameCn;//引用
		strHtml+=bindRptTabColumnRow(rptTab,col,rptData);
		proWin.bindData[proWin.bindData.length]=col;
	}
	strHtml+="<tr><td colspan=10 height=60% class=dataSrcColAttrRow ></td></tr></table>";
	box.innerHTML=strHtml;
	strHtml=null;
	changeColSelAll(true);
}
function bindRptTabColumnHead(rptTab,col)
{
	var strHtml="";
	strHtml+='<td nowrap class="dataSrcColAttrHead" width="100px">'+col.colName.label+'</td>'; 
//	strHtml+='<td nowrap class="dataSrcColAttrHead" style="width:120px;height:22px;">'+col.colNameCn.label+'</td>';
	if(parseInt(rptTab.table.totalType.value)==1)
		strHtml+='<td nowrap class="dataSrcColAttrHead" width="100px">'+col.totalTxt.label+'</td>';
	strHtml+='<td nowrap class="dataSrcColAttrHead" width="80px">'+col.width.label+'</td>';
	strHtml+='<td nowrap class="dataSrcColAttrHead" width="80px">'+col.backColor.label+'</td>';
	strHtml+='<td nowrap class="dataSrcColAttrHead" width="80px">'+col.dataFormart.label+'</td>';
	strHtml+='<td nowrap class="dataSrcColAttrHead">'+col.alertTerms.label+'</td>';
	if(rptTab.linkRptId.value!=0 || rptTab.linkModelIds.value.length || rptTab.linkType.value)
	strHtml+='<td nowrap class="dataSrcColAttrHead">'+col.linkFlag.label+'</td>';
	
	return strHtml;
}
function bindRptTabColumnRow(rptTab,col,rptData,checked)
{
	var disabled=rptTab.table.columns.readonly?" disabled ":"";
	var tabId=rptTab.tabId;
	var strHtml='<tr id="'+tabId+'_'+col.colName.value+'_tr" style="height:22px;"  class="'+(proWin.bindData.length%2==0?"dataSrcColAttrRow":"dataSrcColAttrAlrRow")+
			'" ondblclick="colRowDblClick(\''+tabId+'\',\''+col.colName.value
			+'\')" onmouseover="tr_onmouseover(this)" onmouseout="tr_onmouseoutR(this)" onclick="tr_onclick(this);proWin.bindCol=\''+col.colName.value+'\';" >';
 
	strHtml+='<td class="dataSrcColAttrTD" style="padding-right: 5px;"  align="center"><input '+disabled+' id="'+tabId+'_'+col.colName.value+'_check" type=checkbox '+(checked?"checked":"")+' onclick="changeColSel(this,\''+col.colName.value+'\')" /></td>';
//	strHtml+='<td nowrap class="dataSrcColAttrTD"  >'+col.colName.value+'</td>';
	
	strHtml+='<td nowrap class="dataSrcColAttrTD"  >'+col.colNameCn.value+'</td>';
	if(parseInt(rptTab.table.totalType.value)==1)
	strHtml+='<td nowrap class="dataSrcColAttrTD" title="'+col.totalTxt.explain+'"><input type=text style="width:90px;" onchange="changeColPro(this,\''+col.colName.value+'\',\'totalTxt\')" value="'+col.totalTxt.value+'"/></td>';
	strHtml+='<td nowrap class="dataSrcColAttrTD" title="'+col.width.explain+'"><input type=text style="width:70px;" onchange="changeColPro(this,\''+col.colName.value+'\',\'width\')" value="'+col.width.value+'" onkeydown ="checkEnter(this);onlynumber();" /></td>';
	strHtml+='<td nowrap class="dataSrcColAttrTD" title="'+col.backColor.explain+'"><input onchange="changeColPro(this,\''+col.colName.value+'\',\'backColor\')" style="width:70px;height:22px;backGround-color:'+col.backColor.value+'" onclick="colColorChange(this,\''+col.colName.value+'\',\'backColor\')" value="'+col.backColor.value+'"/></td>';
	strHtml+='<td nowrap class="dataSrcColAttrTD" title="'+col.dataFormart.explain+'"><select style="width100px;" onchange="changeColPro(this,\''+col.colName.value+'\',\'dataFormart\')"><option value="">无</option>';
	for(var l=0;l<dataFormatPattern.length;l++)
	{
		strHtml+='<option '+(col.dataFormart.value==dataFormatPattern[l]?"selected":"")+' value="'+dataFormatPattern[l]+'">'+dataFormatPattern[l]+'</option>';
	}
	strHtml+='</select></td>';
//		strHtml+='<td nowrap class="dataSrcColAttrTD">'+col.alertFlag.value+'</td>';
	strHtml+='<td nowrap class="dataSrcColAttrTD" title="'+col.alertTerms.explain+
		'"><input'+disabled+' type=text style="width:180px;" onclick="buildColAlertTab(this,\''+col.colName.value+'\',\'alertTerms\')" value="'+col.alertTerms.value+'" /></td>';
	if(rptTab.linkRptId.value!=0 || rptTab.linkModelIds.value.length || rptTab.linkType.value)
		strHtml+='<td nowrap class="dataSrcColAttrTD" title="'+col.linkFlag.explain+'"><input type="checkbox" '+(col.linkFlag.value?"checked":"")+' onclick="changeColPro(this,\''+col.colName.value+'\',\'linkFlag\')" /></td>';
	strHtml+='<td nowrap  align=center ><img class=hand src="images/arrowUp.gif" onclick="moveColRow(1,\''+col.colName.value
		+'\')" /> <img  class=hand  src="images/arrowDown.gif"  onclick="moveColRow(0,\''+col.colName.value+'\')"/> </td>';
	strHtml+='</tr>';
	return strHtml;
}
//锁定表头
function columnsTabScroll(tabId,obj)
{
	try
	{
		var top=0,left=0;
		if(obj.scrollTop)
		{
			top=obj.scrollTop-1;
		}
		if(obj.scrollLeft)
		{
			left=obj.scrollLeft-1;
		}
		if($(tabId+"_columns_headTr"))
		{
			$(tabId+"_columns_headTr").style.top= top+"px";
			$(tabId+"_columns_headTr").style.position="relative";
		}
	}
	catch(ex)
	{
		
	}
}
//获取选择列排序数组
function sortSelCol(selCols)
{
	var res=[];
	var minIndex=0;
	var sortMap={};
	for(var c in selCols)
	{
		var dcol=null;
		for(var col in selCols)
		{
			if(sortMap[col])continue;
			if(selCols[col].index<=minIndex)
			{
				dcol=col;
				sortMap[col]=true;
				res[res.length]=dcol;
				continue;
			}
		}
		minIndex++;
	}
	clearVarVal(sortMap);
//	Debug("sortSelCol(selCols)="+res);
	return res;
}
//移动列顺序	//	1:向上 0向下
function moveColRow(type,colName)
{
	if(!colName)colName=proWin.bindCol;
	if(!colName)return;
	var rptTab=currentObj.obj;
	var tabId=currentObj.id;
	if(rptTab.dataSrcName.value==colName)return;
	var colTr=$(proWin.bindTabId+"_"+colName+"_tr");
	var colTab=$(proWin.bindTabId+'_columns_table');
	Debug("moveColRow "+type+" "+colName);
	var lColName=null,nColName=null;
	var lindex=-1,nindex=-1,thisIndex=-1;
	for(var i=0;i<proWin.bindData.length;i++)
	{
		if(proWin.bindData[i].colName.value==colName)
		{
			thisIndex=i;
			lColName=i>0?proWin.bindData[i-1].colName.value:null;
			lindex=i>0?i-1:-1;
			nColName=proWin.bindData.length>i+1? proWin.bindData[i+1].colName.value:null;
			nindex=proWin.bindData.length>i+1?i+1:-1;
		}
	}
	if(type==1 && lColName)
	{
		var lColTr=$(proWin.bindTabId+"_"+lColName+"_tr");
		colTr.swapNode(lColTr); //向上
		proWin.bindData.swap(thisIndex,lindex);
	}
	else if(type==0 && nColName)
	{
		var nColTr=$(proWin.bindTabId+"_"+nColName+"_tr");
		colTr.swapNode(nColTr);  //向下
		proWin.bindData.swap(thisIndex,nindex);
	}
	setColDataIndex();
}
//重新设置列对象索引位置
function setColDataIndex()
{
	proWin.bindConfigChange=true;
	for(var i=0;i<proWin.bindData.length;i++)
		proWin.bindData[i].index=i;
}
//行双击事件
function colRowDblClick(tabId,colName)
{return;
	var rptTab=currentObj.obj;
	var tabId=currentObj.id;
	if(rptTab.table.columns.readonly)return;
	var chk=$(tabId+'_'+colName+'_check');
	chk.checked=!chk.checked;
	changeColSel(chk,colName);
}
//获取指定列对象
function getBindCol(colName)
{
	for(var i=0;i<proWin.bindData.length;i++)
	{
		if(colName==proWin.bindData[i].colName.value)
		{
			return proWin.bindData[i];
		}
	}
}
//选中事件
function changeColSel(obj,colName)
{
	var rptTab=currentObj.obj;
	var tabId=currentObj.id;
	if(rptTab.table.columns.readonly)return;
	Debug("changeColSel "+colName +" "+obj.checked);
	var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
	var cols=rptData.colAttrs.value;	//	全字段
	var selCols=rptTab.table.columns.value;
	//加入选中列
	if(obj.checked)
	{
		selCols[colName]=getBindCol(colName);
	}
	else
	{
		selCols[colName]=null;
		delete selCols[colName];
		if(colName==rptTab.dataSrcName.value)
			rptTab.dataSrcName.value="";
	}
	changeColSelAll(true);
	proWin.bindConfigChange=true;
	flashRptTabPrivData();
}
//全选事件和检查
function changeColSelAll(auto)
{
	var rptTab=currentObj.obj;
	var tabId=currentObj.id;
	var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
	var cols=rptData.colAttrs.value;	//	全字段
	var selCols=rptTab.table.columns.value;
	var allChk=$(tabId+'_all_chk');
	if(auto)
	{
		var len=0;
		for(var i in selCols)len++;
		if(len==cols.length)
			allChk.checked=true;
		else
			allChk.checked=false;
		return;
	}
	Debug("allcheck:"+allChk.checked);
	for(var i=0;i<proWin.bindData.length;i++)
	{
		var colName=proWin.bindData[i].colName.value;
		var chk=$(tabId+'_'+colName+'_check');
		chk.checked=allChk.checked;
		if(chk.checked)//加入选中列
		{
			selCols[colName]=getBindCol(colName);
			flashRptTabPrivData();
		}
		else
		{
			selCols[colName]=null;
			delete selCols[colName];
		}
		proWin.bindConfigChange=true;
	}
	flashRptTabPrivData();
}
//颜色框事件 
function colColorChange(obj,name,pro)
{
	var pos=proWin.getPosition();
	var rvalue=ShowDialog("color_picker.htm",obj.value,pos[0]+event.x-300,event.y+pos[1]-70,250,245)
//	var rvalue=ShowDialog("color_picker2.htm",obj.innerHTML,event.x-300,event.y-70,250,245)
	if(!rvalue)return;
	obj.value=rvalue;
	obj.style.backgroundColor=obj.value;
	changeColPro(obj,name,pro)
}
//更新字段设置属性
function changeColPro(obj,name,pro)
{
	var rptTab=currentObj.obj;
	var tabId=currentObj.id;
	rptTab.table.columns.value[name][pro].value=obj.value;
	proWin.bindConfigChange=true;
	flashRptTabPrivData();//刷新数据 
}
//显示预警框设置
function buildColAlertTab(obj,name,pro)
{
	var rptTab=currentObj.obj;
	var tabId=currentObj.id;
	var alertTerms=rptTab.table.columns.value[name][pro].value;
	
//	proWin.bindConfigChange=true;
}
//关闭窗口回调函数
function setRptTabColumns()
{
	flashRptTabPrivData();
	var tabId=proWin.bindTabId;
	var rptTab=getAllTabs(tabId);
	var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
	var selCols=rptTab.table.columns.value;
	if(rptTab.table.dataTransRule.transFlag)
	{
		for(var i=0;i<rptTab.table.dataTransRule.value[0].length;i++)
		{
			var colName=rptTab.table.dataTransRule.value[0][i];
			if(!selCols[colName])
			{
				if(confirm("横纵转换中打横字段["+rptData.colMap[colName].nameCn.value+"]未选择，可能导致横纵转换设置无效,是否确定关闭。"))
				{
					rptTab.table.dataTransRule.value[0].remove(i);
					i--;
				}
				else
				{
					return false;
				}
			}
		}
		for(var i=0;i<rptTab.table.dataTransRule.value[1].length;i++)
		{
			var colName=rptTab.table.dataTransRule.value[1][i];
			if(!selCols[colName])
			{
				if(confirm("横纵转换中打纵字段["+rptData.colMap[colName].nameCn.value+"]未选择，可能导致横纵转换设置无效,是否确定关闭。"))
				{
					rptTab.table.dataTransRule.value[1].remove(i);
					i--;
				}
				else
				{
					return false;
				}
			}
		}
		$("table$dataTransRule_pro_value").value=getListVal(rptTab.table.dataTransRule);
		rptTab.table.dataTransRule.transFlag=getDataTransFlag(rptTab,0);
	}
	for(var i=0;i<proWin.bindData.length;i++)
	{
		if(!selCols[proWin.bindData[i].colName.value])
		{
			clearVarVal(proWin.bindData[i],function(key,val){if(key=="colNameCn")return false;else return true;});//清理对象
			proWin.bindData[i]=null;
		}
	}
	proWin.bindKey="";
	proWin.bindTabId=null;
	proWin.bindData=null;
	proWin.proBox=null;
	proWin.bindCol=null;
	$("table$columns_pro_value").value=getListVal(rptTab.table.columns);
	return true;
}
//刷新预览数据
function flashRptTabPrivData()
{
	if(!proWin.bindConfigChange)return;
	proWin.bindConfigChange=false;
	var tabId=proWin.bindTabId;
	var rptTab=getAllTabs(tabId);
	var selSortCol=sortSelCol(rptTab.table.columns.value);	//	返回排序后的数组
	if(selSortCol.length==0)
	{
		for(var i=0;i<proWin.bindData.length;i++)	//	默认全部显示
			rptTab.table.columns.value[proWin.bindData[i].colName.value]=proWin.bindData[i];
		selCols=rptTab.table.columns.value;
		selSortCol=sortSelCol(rptTab.table.columns.value);
	}
	clearVarVal(rptTab.table.colNames);
	rptTab.table.colNames=selSortCol;
	binDataColNameList();
	bindTabDataPriv(tabId,["table","columns"],rptTab);//刷新数据
}
