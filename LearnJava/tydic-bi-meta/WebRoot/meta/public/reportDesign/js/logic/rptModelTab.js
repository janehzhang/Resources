//var modTabShowTableType=1;if(!dhtmlx._isIE)modTabShowTableType=0;
//初始化模块TAB页内容
function initModelTab(obj)
{
	if(!obj)
	{
		obj=rptObjects[rptWins.curModel.getId()];
	}
	obj.inited=true;
	if(obj.tabPannel.value)	//	Tab模式
	{
		if(!obj.window.win.tabBar)
		{
			for(var tab in obj.tabs)
			{
				if(tab=="type")continue;
				tab=obj.tabs[tab];
				var tabContent=$(tab.tabId+"_content");
				if(tabContent)
				{
					for(var i=0;i<tab.termNames.value.length;i++)	//需要释放与之关联的条件
					{
						var termId=tab.termNames.value[i].value;
						$(termId+'_term').appendChild($(termId+'_term_termConer'));
					}
					tabContent.parentNode.removeChild(tabContent);
				}
			}
			obj.window.win.detachObject();
			obj.window.win.tabBar=obj.window.win.attachTabbar();
			var tabBar=obj.window.win.tabBar;
			tabBar.enableTabCloseButton(true);
			tabBar.enableScroll(false);
			tabBar.enableAutoReSize(true);
			tabBar.setSkin('dhx_skyblue');
			tabBar.setImagePath(imageRoot+'js/dhtmlx/imgs/');
			tabBar.attachEvent("onTabClose",moduleTabClose);
			tabBar.attachEvent("onSelect", moduleTabSelect);
			
			if(!obj.window.win.dataConter)obj.window.win.dataConter=document.createElement("DIV");	//使TAB自动适应窗口最大化
			obj.window.win.appendObject(obj.window.win.dataConter);
			var padiv=obj.window.win.dataConter.parentNode.getElementsByTagName("DIV")[2];
 			tabBar.addButton=document.createElement("DIV");
 			padiv.insertBefore(tabBar.addButton, padiv.lastChild);
			tabBar.addButton.style.position="relative";

			tabBar.addButton.style.left="105px";
			tabBar.addButton.style.paddingBottom="5px";
			tabBar.addButton.style.top="2px";
			tabBar.addButton.style.width="20px";
			tabBar.addButton.style.zIndex="100000";
			tabBar.addButton.innerHTML="<input type=button value='+' id='addModuleTabButton' class=sbts "+
				"style='width:20px;height:20px;margin: 0px;' onclick='addModuleTab(\""+obj.modId+"\")'/>" ;

			var tabLen=0;
			for(var tab in obj.tabs)
			{
				if(tab=="type")continue;
				tabLen++;
				var tabId=tab;//obj.window.win.getId()+"@tab"+obj.tabCount;
				tab=obj.tabs[tab];
				var index=parseInt(tabId.replace(tab.modId+"@tab",""));//rptModule3@tab1
				if(index>obj.tabCount)obj.tabCount=index;
				var tabName=tab.tabName.value;
				if(!tabBar.tabWidth)tabBar.tabWidth={};
				tabBar.tabWidth[tabId]=100;	//	动态算长度
				tabBar.addTab(tabId,tabName,tabBar.tabWidth[tabId]+"px");
				addIds(tab,tabId,tabName);//添加到全局
 				var strHtml="<div id='"+tabId+"_content' style='position:relative;width:100%;height:100%;' onclick=\"tabClick(this,'"+tabId+"')\">测试层</div>";
 				tabBar.setContentHTML(tabId,strHtml);
 				if(tabLen==1)tabBar.setTabActive(tabId);
 				initModelTabContent(obj,tabId);	
 				if(inited && tab.dataSrcName.value!="")	//	报表配置数据初始化才存在
 					changeRptModelTab(tabId,["dataSrcName"],tab);
			}
			obj.window.win.tabBar.addButton.style.left=(getTabWidth(obj.window.win.tabBar.tabWidth)+5)+"px";

			if(tabLen==0)
			{
				addModuleTab(obj.modId);	
			}
			if(isCustUser)
				tabBar.enableTabCloseButton(false);
		}
	}
	else
	{
		var needFlagTab=obj.window.win.tabBar?true:false;
		if(obj.window.win.tabBar)
		{
			var saveTab=null;
			for(var tab in obj.tabs)
			{
				if(tab=="type")continue;
				tab=obj.tabs[tab];
				for(var i=0;i<tab.termNames.value.length;i++)	//需要释放与之关联的条件
				{
					var termId=tab.termNames.value[i].value;
					$(termId+'_term').appendChild($(termId+'_term_termConer'));
				}
				if(!saveTab)	//	保留数据，假删除，只是删除引用
				{
				 	rptObjects[tab.tabId]=null;
					delete rptObjects[tab.tabId];
					deleteId(tab.tabId);
					saveTab=true;
					continue;
				}
				var tabContent=$(tab.tabId+"_content");
				if(tabContent)
					tabContent.parentNode.removeChild(tabContent);
				obj.tabs[tab.tabId]=null;
				delete 	obj.tabs[tab.tabId];
				deleteId(tab.tabId);
				rptObjects[tab.tabId]=null;
				delete rptObjects[tab.tabId];
				destructorDHMLX(tab);
				clearVarVal(tab);
			}
			obj.window.win.tabBar.addButton.parentNode.removeChild(obj.window.win.tabBar.addButton);
			obj.window.win.tabBar.clearAll();
			obj.window.win.detachObject();
			obj.window.win.tabBar=null;
			obj.tabCount=1;
		}
		obj.tabCount=1;
		var tabId=obj.window.win.getId()+"@tab"+obj.tabCount;
		if(!obj.tabs[tabId])
		{
			//添加到全局
			var tabName="页面"+obj.tabCount;
			var modTab=new reportModuleTab();
			modTab.modId=obj.modId;
			modTab.tabId=tabId;
			modTab.tabName.value=tabName;
			addIds(modTab,tabId,tabName);
			obj.tabs[tabId]=rptObjects[tabId];//{type:"label",label:"",value:getObjectVal(tabId,["tabName"]),explain:"TAB标签页面列表"};//new reportModuleTab();
			obj.window.win.attachHTMLString("<div id='"+tabId+"_content' style='position:absolute;width:100%;height:100%;' onclick=\"tabClick(this,'"+tabId+"')\">测试层</div>");
		}
		else if(!$(tabId+"_content") && !rptObjects[tabId])
		{
			var tabName= obj.tabs[tabId].tabName.value;
			addIds(obj.tabs[tabId],tabId,tabName);
			rptObjects[tabId]=obj.tabs[tabId];
			obj.window.win.attachHTMLString("<div id='"+tabId+"_content' style='position:absolute;width:100%;height:100%;' onclick=\"tabClick(this,'"+tabId+"')\">测试层</div>");
		}
		else  if(!$(tabId+"_content") && rptObjects[tabId])
		{
			obj.window.win.attachHTMLString("<div id='"+tabId+"_content' style='position:absolute;width:100%;height:100%;' onclick=\"tabClick(this,'"+tabId+"')\">测试层</div>");
		}
		initModelTabContent(obj,tabId);
		if(needFlagTab)changeRptModelTab(tabId,["dataSrcName"],rptObjects[tabId]);
	}
}
//模块Tab页面关闭事件，删除页面
function moduleTabClose(tabId)
{
	return false;
}
//选中页面
function moduleTabSelect(id,last_id)
{
//	var modId=id.split("@")[0];
	setSelectId(id);
	window.setTimeout("moduleTabContentSize('"+id+"')",10);
	return true;
}
function getTabWidth(tabWidth)
{
	var res=0;
	for(var i in tabWidth)
	{
		res+=tabWidth[i];
	}
	return res;
}
function addModuleTab(modId)
{
	var modTab=getModule(modId);
	var obj=modTab;
	modTab.tabCount++;
	var tabName="页面"+modTab.tabCount;
	var tabId=modId+"@tab"+modTab.tabCount;
	Debug(tabName+":"+tabId);
	if(!obj.window.win.tabBar.tabWidth)obj.window.win.tabBar.tabWidth={};
	obj.window.win.tabBar.tabWidth[tabId]=100;
	obj.window.win.tabBar.addTab(tabId,tabName,"100px");
	obj.window.win.tabBar.setTabActive(tabId);
	obj.window.win.tabBar.addButton.style.left=(getTabWidth(obj.window.win.tabBar.tabWidth)+5)+"px";
	var rptTab=new reportModuleTab();
	rptTab.tabId=tabId;
	rptTab.modId=obj.modId;
	rptTab.tabName.value=tabName;
	addIds(rptTab,tabId,tabName);//添加到全局
	obj.tabs[tabId]=rptObjects[tabId];//{type:"label",label:"",value:tabId,explain:"TAB标签页面列表"};//new reportModuleTab();
 	var strHtml="<div id='"+tabId+"_content' style='width:100%;height:100%;' onclick=\"tabClick(this,'"+tabId+"')\">测试层</div>";
 	obj.window.win.tabBar.setContentHTML(tabId,strHtml);
 	obj.window.win.tabBar.setTabActive(tabId);
	initModelTabContent(obj,tabId);	
}
//删除模块Tab页
function removeModelAndTabs(win)
{
	var wid=win.getId();
	var obj=rptObjects[wid];
	for(var tab in obj.tabs)
	{
		if(obj.tabs[tab] && rptObjects[tab])
		{
			for(var i=0;i<rptObjects[tab].termNames.value.length;i++)	//需要释放与之关联的条件
			{
				var termId=rptObjects[tab].termNames.value[i].value;
				Debug("释放条件 ID："+termId)
				$(termId+'_term').appendChild($(termId+'_term_termConer'));
			}
			destructorDHMLX(obj.tabs[tab]);
			obj.tabs[tab]=null;
			delete obj.tabs[tab];
			deleteId(tab);
		}
	}
    deleteId(wid);
}
//Tab页单击事件
function tabClick(obj,tabId)
{
	onClick(tabId);
	Debug(tabId);
}
//初始化页面内容
function initModelTabContent(mod,tabId)
{
	var tab=$(tabId+"_content");
	var tabCon=$(tabId+'_tabShowConter');
	var rptTab=rptObjects[tabId];
	if(!tabCon)
	{
		var strHtml='<div id="'+tabId+'_tabShowConter" style="position:absolute;overflow:auto;height:100%;width:100%;border: 0px;top:0px;left:0px;padding:0px;" type="termPriv" ida="'+tabId+'_termConter" >'+
			'<TABLE  type="termPriv" ida="'+tabId+'_termConter" style="position:relative;" cellpadding="0" cellspacing="1" border="0" width=100% class="dataPrivTable" id='+tabId+'_dataPrivTable>';
		strHtml+='<tr ><td height=50px class="rptTermBackColor" style="height:10%;padding-left: 10px;" id="'+tabId+'_termConter" type="termConter" >模块页面条件栏----可以将条件窗口中的预览框拖入此处</td></tr>';
		strHtml+='<tr id="'+tabId+'_tableTR" style="display:'+(rptTab.showType.value==1?"none":"")+'"><td id="'+tabId+
				'_tableTD" nowrap align=center style="background-color: #F9F9F9;height:'+(parseInt(rptTab.showType.value)==0?"90":"50")+'%;" valign=top>'+
				'<div id="'+tabId+'_tableDiv"   style="position:absolute;left:1px;width:100%;height:100%;overflow:auto;border: 0px;" onscroll="moduleTabScroll(\''+tabId+'\',this)"  type="termPriv" ida="'+tabId+'_termConter" >'+
					'<div id="'+tabId+'_tableHeadDiv" style="position:relative;border: 0px;top:0px;left:0px;padding:0px;z-index:9999;" type="termPriv" ida="'+tabId+'_termConter" >模块表格头<p>&nbsp;</p></div>'+
					'<div id="'+tabId+'_tableRowDiv"  style="position:relative;border: 0px;top:0px;left:0px;padding:0px;z-index:9998;" type="termPriv" ida="'+tabId+'_termConter" >模块数据行 </div>';
		strHtml+='</div></td></tr> ';
		strHtml+='<tr id="'+tabId+'_tablePaginationDiv" style="height:22px;display:'+	(rptTab.table.autoPagination.value>0?'':'none')+
					';"><td  ><div style="position:relative;top:0px;left:0px;width:100%;height:22px;border: 0px;top:0px;left:0px;padding:0px;z-index:9998;"  type="termPriv" ida="'+tabId+'_termConter" >'+
					getTabPageCfmStr(tabId)+'</div></td></tr>';
		strHtml+='<tr type="termPriv" ida="'+tabId+'_termConter" id="'+tabId+'_graphTR"  style="position:relative;display:'+(rptTab.showType.value==0?"none":"")+'"><td id="'+tabId+
				'_graphTD" nowrap align=center style="background-color: #F9F9F9;height:'+(rptTab.showType.value==1?"90":"50")+'%;"  valign=top >'+
					'<div type="termPriv" ida="'+tabId+'_termConter" id="'+tabId+'_graphDiv" style="position:relative;" >模块图形展示区</div>'+
					'</td></tr>';
		strHtml+='</table>';
		strHtml+='<div id="'+tabId+'_estopCover" style="z-index:-1111;backGround-image:url(images/filterBg.png);position:absolute;left:0px;top:0px;width:100%;height:100%;"></div>'
				+'</div>';

		tab.innerHTML=strHtml;
//		calcTabPrivSize(tabId);
		strHtml=null;
	}
	else
	{
		tab.appendChild(tabCon);
	}
	bindTerms(tabId);
	$(tabId+'_tabShowConter').style.width=$(tabId+'_tabShowConter').parentNode.offsetWidth+"px";
	Debug("模块页大小： "+$(tabId+'_tabShowConter').parentNode.offsetWidth);
}
function bindTerms(tabId)
{
	var rptTab=getAllTabs(tabId);
	var conPar=rptTab.tabId+"_termConter";
	for(var j=0;j<rptTab.termNames.value.length;j++)
	{
		property_params.Handle=$(rptTab.termNames.value[j].value+"_term_termConer");
		if(!property_params.Handle)continue;
		property_params.outObj=$(rptTab.termNames.value[j].value+"_term");
		property_params.inObj=$(conPar);
		property_params.term_dragging=true;
		rptTermUp(true);
	}
}
//模块Tab页属性变化改变事件 obj rptObjects[id];
function changeRptModelTab(tabId,keys,rptTab)
{
	switch(keys[0])
	{
	case "tabName":	//	多模块页时需要单独处理
		if(rptObjects[rptTab.modId].tabPannel.value)
			rptObjects[rptTab.modId].window.win.tabBar.setLabel(tabId,rptTab.tabName.value);
		break;
	case "dataSrcName":
		clearVarVal(rptTab.table.columns.value,function(key,val){if(key=="colNameCn")return false;else return true;});//清理对象
		if(rptTab.dataSrcName.value=="")
		{
			var headDiv=$(tabId+'_tableHeadDiv');
			var contentDiv=$(tabId+'_tableRowDiv');
			headDiv.innerHTML="模块表格头<p>&nbsp;</p>";
			headDiv.style.width="100%";
			contentDiv.innerHTML="模块数据行";
			contentDiv.style.width="100%";
			$(tabId+'_graphDiv').innerHTML="模块图形展示区";
			$(tabId+'_graphDiv').style.width="100%";
			$(tabId+'_tablePaginationDiv').style.display="none";
			clearVarVal(rptTab.rptData);
			rptTab.rptData=null;
			return;
		}
		if(!waitTerm(rptTab,2))
		{
			runInterval.add(changeRptModelTab,rptTab,2,[tabId,keys,rptTab]);
			return;
		}
		rptTab.table.columns.value={};//需要清理
		buildTabColNames(rptTab);
		readTabData(tabId);
		break;
	case "table":
		if(keys[1]=="width" || keys[1]=="height")
		{
//			calcTabPrivSize(tabId);
			break;
		}
		else if(keys[1]=="autoPagination")//分页不钻取，可转换 按分页前数据转换显示
		{
			rptTab.table.autoPagination.value=parseInt(rptTab.table.autoPagination.value);
			var pageDiv=$(tabId+'_tablePaginationDiv');
			if(rptTab.table.autoPagination.value)
			{
				pageDiv.style.display="";
				rptTab.table.duckColName.value="";//不钻取
			}
			else
			{
				pageDiv.style.display="none";
			}
			readTabData(tabId);
			break;
		}
		else if(keys[1]=="duckColName")
		{
			if(!rptTab.table.duckColName.value)break;
			rptTab.table.autoPagination.value=0;//钻取不分区，不转换
			rptTab.table.dataTransRule.designerPriv=false;
			rptTab.table.dataTransRule.value=[];
			rptTab.table.dataTransRule.transFlag=false;
			var dIndex=rptTab.table.columns.value[rptTab.table.duckColName.value].index;
			rptTab.table.columns.value[rptTab.table.duckColName.value].index=0;
			for(var col in rptTab.table.columns.value)
			{
				if(col==rptTab.table.duckColName.value)continue;
				var colPro=rptTab.table.columns.value[col];
				if(colPro.index<dIndex)
					colPro.index++;
			}
			var selSortCol=sortSelCol(rptTab.table.columns.value);	//	返回排序后的数组
			clearVarVal(rptTab.table.colNames);
			rptTab.table.colNames=selSortCol;
		}
		else if(keys[1]=="colFreeze")
		{
			clearVarVal(rptTab.table.colFreeze.map);
			rptTab.table.colFreeze.map=null;
		}
	default:
		bindTabDataPriv(tabId,keys,rptTab);
		break;
	}
	moduleTabContentSize(tabId);
}

//绑定页面数据
function bindTabDataPriv(tabId,keys,rptTab)
{
	if(rptTab.rptData)
	{
		if(!rptTab["showType"].inited)setPrivTableShowType(rptTab);
		if(!keys[1] || (keys[1]=="backGroundColor" || keys[1]=="tabHeadBackColor" 
			|| keys[1]=="tabHeadForeColor" || keys[1]=="rowBackColor"
			|| keys[1]=="rowAlternantBackColor" || keys[1]=="cssName" 
			|| keys[1]=="rowUniteCols" || keys[1]=="colUniteRule"
			|| keys[1]=="totalType" || keys[1]=="duckColName"  || keys[1]=="columns"))	//	表格变更
		{
			if(rptTab.showType.value==0 || rptTab.showType.value==2)
			{
//				buildTabColNames(rptTab);
				bindTabData(rptTab,rptTab.rptData);
			}
		}
		if(rptTab.showType.value==1 || rptTab.showType.value==2)
		{
			bindGraphPriv(rptTab,rptTab.rptData);
		}
	}
}

////滚动条冻结表头和列
//function moduleTabScroll(tabId,obj)
//{
//	try
//	{
//		var top=0,left=0;
//		if(obj.scrollTop)
//		{
//			top=obj.scrollTop-1;
//		}
//		if(obj.scrollLeft)
//		{
//			left=obj.scrollLeft-1;
//		}
//		var headDiv=$(tabId+'_tableHeadDiv');
//		headDiv.style.top= top+"px";
//		if($(tabId+"_headTd_0"))	//	表头
//		{
//			var rptTab=rptObjects[tabId];
//			var rowLen=rptTab.rptData[1].length;
//			var rptTab=getAllTabs(tabId);
//			var colFreeze=rptTab.table.colFreeze.value;
//			for(var c=0;c<colFreeze.length;c++)
//			{
//				var index=rptTab.table.columns.value[colFreeze[c]].index;
//				var colObj=$(tabId+"_headTd_"+index);
//				colObj.style.left= left+"px";
//				colObj.style.position="relative";
//			}
//			for( var r=0;r<rowLen;r++)	//行头
//			{
//				for(var c=0;c<colFreeze.length;c++)
//				{
//					var index=rptTab.table.columns.value[colFreeze[c]].index;
//					//rptTab.table.colNames
//					var colObj=$(tabId+"_rowTd_"+r+"_"+index);
//					if(colObj)//由于合并可能不存在
//					{
//						colObj.style.position="relative";
//						colObj.style.left= left+"px";
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
