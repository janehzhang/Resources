function bindRptTabDataTransRule()
{
	var rptTab=currentObj.obj;
	var tabId=rptTab.tabId;
	proWin.setText(rptTab.tabName.value+"-数据横纵转换设置对话框");
	proWin.attachHTMLString("<div id='"+tabId+"_dataTransRule_proWin' style='width:100%;height:100%;overflow:auto;' >数据横纵转换设置对话框</div>");
	proWin.proBox=$(tabId+"_dataTransRule_proWin");
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
	proWin.setDimension(150,250);
	proWin.center();
	proWin.setDimension(580,300);
	var strHtml='<TABLE cellpadding="1" cellspacing="1" border="0" width=100% height=100% class="proWinColumnAttrTable" id="'+tabId+'_dataTransRule_table" >';
	strHtml+='<tr><td nowrap class="dataSrcColAttrAlrRow dataSrcColAttrTD" style="width:120px;height:22px;" >打横维度字段：</td>'
		+'<td class="dataSrcColAttrRow">' ;
//	for(var i=0;)
	var cols=getDataColNames(rptTab.dataSrcName.value,0);	//	获取维度列
	var transRule=rptTab.table.dataTransRule.value;
	transRule[2]=transRule[2]?transRule[2]:"指标";
	var disabled=rptTab.table.dataTransRule.readonly?" disabled ":"";
 	for(var i=0;i<cols.length;i++)
	{
 		if(!rptTab.table.columns.value[cols[i].name.value])continue;
 	 	strHtml+='<input id="'+tabId+'_dataTransRule_'+cols[i].name.value+'" type=checkbox ';
 	 	if(transRule[0] && transRule[0].length)
 	 		for(var c=0;c<transRule[0].length;c++)
	 	 	{
	 	 		if(transRule[0][c]==cols[i].name.value)
	 	 		{
	 	 			strHtml+=' checked ';
	 	 			break;
	 	 		}
	 	 	}
 	 	strHtml+=disabled+'onclick="changeDataTransRule(0,0,this)" name="'+cols[i].name.value
 	 		+'" /><label for="'+tabId+'_dataTransRule_'+cols[i].name.value+'" style="width:180px;">'+cols[i].nameCn.value+'</label>';
 	 	if(i%2==1)strHtml+="</br>";
	}
	strHtml+='</td></tr><tr><td nowrap class="dataSrcColAttrAlrRow dataSrcColAttrTD" style="width:120px;height:22px;" >打纵指标字段：</td>'
		+'<td class="dataSrcColAttrRow">';
	cols=getDataColNames(rptTab.dataSrcName.value,1);	//	获取指标列
 	for(var i=0;i<cols.length;i++)
	{
 		if(!rptTab.table.columns.value[cols[i].name.value])continue;
 	 	strHtml+='<input id="'+tabId+'_dataTransRule_'+cols[i].name.value+'" type=checkbox';
 	 	if(transRule[1] && transRule[1].length)
 	 	for(var c=0;c<transRule[1].length;c++)
 	 	{
 	 		if(transRule[1][c]==cols[i].name.value)
 	 		{
 	 			strHtml+=' checked ';
 	 			break;
 	 		}
 	 	}
 	 	strHtml+=disabled+' onclick="changeDataTransRule(0,1,this)" '+disabled+' name="'+cols[i].name.value
 	 		+'" /><label for="'+tabId+'_dataTransRule_'+cols[i].name.value+'" style="width:180px;">'+cols[i].nameCn.value+'</label>';
 	 	if(i%2==1)strHtml+="</br>";
	}
	strHtml+='</td></tr><tr><td nowrap class="dataSrcColAttrAlrRow dataSrcColAttrTD" style="width:120px;height:22px;" >打纵后指标列名称：</td>'
		+'<td class="dataSrcColAttrRow dataSrcColAttrTD"><input value="'+(transRule[2]?transRule[2]:"指标")+'" '+disabled
		+' id='+tabId+'_dataTransRule2 type=text onchange="changeDataTransRule(0,2,this)" style="width:120px;"/> </td></tr>';
	strHtml+='<tr><td nowrap class="dataSrcColAttrAlrRow dataSrcColAttrTD" style="width:120px;height:22px;" >预览转换后数据：</td>'+
			'<td class="dataSrcColAttrRow dataSrcColAttrTD"> <input id="'+tabId+
				'_dataTransRule_privFlag" type=checkbox onclick="changeDataTransRulePriv(this)" '+(rptTab.table.dataTransRule.designerPriv?"checked":"")+' /><label for="'+
			tabId+'_dataTransRule_privFlag">是否预览转换后数据</label></td></tr>';
	strHtml+="<tr><td colspan=2 height=80% class=dataSrcColAttrRow ></td></tr></table>";
	box.innerHTML=strHtml;
	strHtml=null;
}
function changeDataTransRule(type,index,obj)
{
	if(type==0)
	{
		var rptTab=currentObj.obj;
		var tabId=rptTab.tabId;
		if(index<2)
		{
			var colName=obj.getAttribute("name");
			var transRule=rptTab.table.dataTransRule.value[index];
			if(obj.checked)
			{
				if(!transRule)
				{
					rptTab.table.dataTransRule.value[index]=[];
					transRule=rptTab.table.dataTransRule.value[index];
				}
				for(var c=0;c<transRule.length;c++)
		 	 	{
		 	 		if(transRule[c]==colName)
		 	 			return;
		 	 	}
				transRule[transRule.length]=colName;
			}
			else
			{
				if(!transRule)
				{
					rptTab.table.dataTransRule.value[index]=[];
					transRule=rptTab.table.dataTransRule.value[index];
				}
				for(var c=0;c<transRule.length;c++)
		 	 	{
		 	 		if(transRule[c]==colName)
		 	 		{
		 	 			transRule.remove(c);
		 	 			return;
		 	 		}
		 	 	}
			}
		}
		else
		{
			rptTab.table.dataTransRule.value[2]=obj.value;
		}
		rptTab.table.dataTransRule.transFlag=getDataTransFlag(rptTab,0);
	}
}
function changeDataTransRulePriv(obj)
{
	var rptTab=currentObj.obj;
	var tabId=currentObj.id;
	rptTab.table.dataTransRule.designerPriv=obj.checked;
	rptTab.table.dataTransRule.transFlag=getDataTransFlag(rptTab,0);
	if(rptTab.table.dataTransRule.transFlag)
	{
		for(var i=0;i<rptTab.table.dataTransRule.value[0].length;i++)
		{
			if(rptTab.table.dataTransRule.value[0][i]==rptTab.table.duckColName.value )
			{
				rptTab.table.duckColName.value="";//横纵转换不能钻取
				bindPropertyValue(["table","duckColName"],rptTab.table.duckColName,true);
				break;
			}
		}
		rptTab.table.autoPagination.value=0;//不能分页
		rptTab.table.autoPagination.readonly=true;
		bindPropertyValue(["table","autoPagination"],rptTab.table.autoPagination,true);
		readTabData(tabId);
	}
	else
	{
		rptTab.table.autoPagination.readonly=false;
		bindPropertyValue(["table","autoPagination"],rptTab.table.autoPagination,true);
		bindTabDataPriv(tabId,["table","columns"],rptTab);//刷新转换后的数据
	}
}
function setRptTabDataTransRule()
{
	var rptTab=currentObj.obj;
	var tabId=currentObj.id;
	rptTab.table.dataTransRule.transFlag=getDataTransFlag(rptTab,0);
	if(rptTab.table.dataTransRule.transFlag)
	{
		for(var i=0;i<rptTab.table.dataTransRule.value[0].length;i++)
		{
			if(rptTab.table.dataTransRule.value[0][i]==rptTab.table.duckColName.value )
			{
				rptTab.table.duckColName.value="";//横纵转换不能钻取
				bindPropertyValue(["table","duckColName"],rptTab.table.duckColName,true);
				continue;
			}
			for(var c =0;c<rptTab.table.colFreeze.length;c++)//转换列不冻结
			{
				if(rptTab.table.dataTransRule.value[0][i]==rptTab.table.colFreeze[c])
				{
					rptTab.table.colFreeze.reomve(c);
					break;
				}
			}
		}
		rptTab.table.autoPagination.value=0;//不能分页
		rptTab.table.autoPagination.readonly=true;
		bindPropertyValue(["table","autoPagination"],rptTab.table.autoPagination,true);
	}
	else
	{
		rptTab.table.autoPagination.readonly=false;
		bindPropertyValue(["table","autoPagination"],rptTab.table.autoPagination,true);
	}
	rptTab.table.dataTransRule.value[2]=$(tabId+'_dataTransRule2').value;
	$("table$dataTransRule_pro_value").value=getListVal(rptTab.table.dataTransRule);
//	bindTabDataPriv(tabId,["table","columns"],rptTab);//刷新转换后的数据
	return true;
}
//绑定表格列头合并规则
function bindRptTabColUniteRule()
{
	var rptTab=currentObj.obj;
	var tabId=currentObj.id;
	var rptData=null;
	proWin.setText(rptTab.tabName.value+"-表头合并规则设置对话框");
	proWin.attachHTMLString("<div id='"+tabId+"_colUniteRule_proWin' style='width:100%;height:100%;'>表头合并规则设置对话框</div>");
}
//回写列头合并规则
function setRptTabColUniteRule()
{
	
	return true;
} 
