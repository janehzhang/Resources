function AddRptModel()
{
	var rptMod=new reportModule();
	rptMod.modId="rptModule" + (++rptWinIdSeq);
	rptMod.moduleName.value="模块1";
	rptMod.window.top.value=0;
	rptMod.window.left.value=0;
	rptMod.window.width.value=bodyWidth;
	rptMod.window.height.value=bodyHeight-reportConfig.rptTitle.height.value;
	rptMod.window.lockDim.value=true;
	rptMod.window.resizable.value=false;
	rptMod.window.isMovable.value=false;
	rptMod.window.stick.value=false;
	rptMod.window.hideHeader.value=true;
	
	rptMod.window.win=addWindow(rptMod);
	if(!isCustUser)rptMod.tabPannel.type="chk";
	var index=reportConfig.modules.value.length;
	reportConfig.modules.value[index]=rptMod;
	rptMod.tabCount++;
	var rptTab=new reportModuleTab();
	rptTab.tabId=rptMod.modId+"@tab"+rptMod.tabCount;
	rptTab.modId=rptMod.modId;
	rptTab.tabName.value="页面"+rptMod.tabCount;
	rptMod.tabs[rptTab.tabId]=rptTab;
}
function initModule(rptMod)
{
	if(!rptMod.window.win)rptMod.window.win=addWindow(rptMod);
	initModelTab(rptMod);
}
var rptWinIdSeq=0;
function addWindow(rptMod)
{
	var id =rptMod.modId?rptMod.modId:"rptModule" + (++rptWinIdSeq);
    var w = parseInt(rptMod.window.width.value),h = parseInt(rptMod.window.height.value),
    		x = parseInt(rptMod.window.left.value),y = parseInt(rptMod.window.top.value);
    var win = rptWins.createWindow(id, x, y, w, h);
    win.setText(rptMod.moduleName.value);
    win.setIcon("leaf.gif", "leaf.gif");
//	 win.setPosition(parseInt(rptMod.window.left.value),parseInt(rptMod.window.top.value));
//	 win.setDimension(parseInt(rptMod.window.width.value),parseInt(rptMod.window.height.value));
    if(rptMod.window.lockDim && rptMod.window.lockDim.value)
    {
    	win.denyResize();
    	win.denyMove();
    	win.button("park").hide();
    	win.button("minmax1").hide();
    }
    else
    {
	 	if(rptMod.window.resizable.value)
			 win.allowResize();
		else
			 win.denyResize();
		if(rptMod.window.isMovable.value)
			 win.allowMove();
		else
			 win.denyMove();
		if(rptMod.window.minHide.value) win.button("park").hide();
		else  win.button("park").show();
	 	if(rptMod.window.maxHide.value) win.button("minmax1").hide();
		else  win.button("minmax1").show();
   }
	win.setModal(rptMod.window.isModal.value);
	if(rptMod.window.stick.value) win.button("stick").show();
	else  win.button("stick").hide();
	win.button("close").hide();
	if(rptMod.window.helpBt.value) win.button("help").show();
	else  win.button("help").hide();
	if(rptMod.window.hideHeader.value) win.hideHeader();
	else  win.showHeader();
	if(!rptMod.modId)rptMod.modId=id;
    return win;
}
//初始化模块TAB页内容
function initModelTab(rptMod)
{
	var obj=rptMod;
	var tabLen=0;
	for(var tab in obj.tabs)
	{
		if(tab=="type")continue;
		tabLen++;
	}
	if(tabLen==1)
	{
		obj.tabPannel.value=false;
		var tabId="";
		for(var tab in obj.tabs){if(tab=="type")continue;tabId=tab;break;}
		if(!$(tabId+"_content"))
		{
			obj.window.win.attachHTMLString("<div id='"+tabId+"_content' style='width:100%;height:100%;' >测试层</div>");
			initModelTabContent(obj,tabId);
		}
	}
	else	//	Tab模式
	{
		if(!obj.window.win.tabBar)
		{
			obj.window.win.detachObject();
			obj.window.win.tabBar=obj.window.win.attachTabbar();
			var tabBar=obj.window.win.tabBar;
			tabBar.enableScroll(false);
			tabBar.enableTabCloseButton(false);
			tabBar.enableAutoReSize(true);
			tabBar.setSkin('dhx_skyblue');
			tabBar.setImagePath(imageRoot+'js/dhtmlx/imgs/');
			tabBar.attachEvent("onSelect", moduleTabSelect);
			tabLen=0;
			for(var tab in obj.tabs)
			{
				if(tab=="type")continue;
				tabLen++;
				var tabId=tab;//obj.window.win.getId()+"@tab"+obj.tabCount;
				tab=obj.tabs[tab];
				if(!tab.showFlag.value)continue;
				var tabName=tab.tabName.value;
				if(!tabBar.tabWidth)tabBar.tabWidth={};
				tabBar.tabWidth[tabId]=100;	//	动态算长度
				tabBar.addTab(tabId,tabName,tabBar.tabWidth[tabId]+"px");
				if(tabLen==1)tabBar.setTabActive(tabId);
 				var strHtml="<div id='"+tabId+"_content' style='width:100%;height:100%;' >测试层</div>";
 				tabBar.setContentHTML(tabId,strHtml);
 				initModelTabContent(obj,tabId);	
 			}
		}
	}
}
function moduleTabSelect(id,last_id)
{
	window.setTimeout("moduleTabContentSize('"+id+"')",10);
	return true;
}
//初始化页面内容
function initModelTabContent(mod,tabId)
{
	var rptTab=getAllTabs(tabId);
	var tab=$(tabId+"_content");
	var tabCon=$(tabId+'_tabShowConter');
	if(!tabCon)
	{
		var strHtml='<div id="'+tabId+'_tabShowConter" style="position:absolute;overflow:auto;height:100%;width:100%;border: 0px;top:0px;left:0px;padding:0px;" type="termPriv" ida="'+tabId+'_termConter" >'+
			'<TABLE style="position:relative;" cellpadding="0" cellspacing="1" border="0" width=100% class="dataPrivTable" id='+tabId+'_dataPrivTable>';
		strHtml+='<tr ><td height=20px style="height:20px;" class="rptTermBackColor" id="'+tabId+'_termConter" type="termConter" >模块页面条件栏----可以将条件窗口中的预览框拖入此处</td></tr>';
		strHtml+='<tr id="'+tabId+'_tableTR" style="display:'+(rptTab.showType.value==1?"none":"")+'"><td id="'+tabId+
				'_tableTD" nowrap align=center style="background-color: #F9F9F9;height:'+(parseInt(rptTab.showType.value)==0?"100":"50")+'%;" valign=top>'+
				'<div id="'+tabId+'_tableDiv"   style="position:absolute;left:0px;width:100%;height:100%;overflow:auto;border: 0px;" onscroll="moduleTabScroll(\''+tabId+'\',this)"  type="termPriv" ida="'+tabId+'_termConter" >'+
					'<div id="'+tabId+'_tableHeadDiv" style="position:relative;border: 0px;top:0px;left:0px;padding:0px;z-index:9999;" type="termPriv" ida="'+tabId+'_termConter" >模块表格头<p>&nbsp;</p></div>'+
					'<div id="'+tabId+'_tableRowDiv"  style="position:relative;border: 0px;top:0px;left:0px;padding:0px;z-index:9998;" type="termPriv" ida="'+tabId+'_termConter" >模块数据行 </div>';
		strHtml+='</div></td></tr> ';
		strHtml+='<tr id="'+tabId+'_tablePaginationDiv" style="display:'+	(rptTab.table.autoPagination.value>0?'':'none')+
					';"><td style="height:22px;">'+getTabPageCfmStr(tabId)+'</td></tr>';
//		<div style="position:relative;top:0px;left:0px;width:100%;height:22px;border: 0px;top:0px;left:0px;padding:0px;z-index:9998;"  type="termPriv" ida="'+tabId+'_termConter" >'+
//					getTabPageCfmStr(tabId)+'</div>
		strHtml+='<tr type="termPriv" ida="'+tabId+'_termConter" id="'+tabId+'_graphTR"  style="position:relative;display:'+(rptTab.showType.value==0?"none":"")+'"><td id="'+tabId+
				'_graphTD" nowrap align=center style="background-color: #F9F9F9;height:'+(rptTab.showType.value==1?"100":"50")+'%;"  valign=top >'+
					'<div type="termPriv" ida="'+tabId+'_termConter" id="'+tabId+'_graphDiv" style="position:relative;" >模块图形展示区</div>'+
					'</td></tr></table>';
		strHtml+='<div id="'+tabId+'_estopCover" style="z-index:-1111;backGround-image:url(images/filterBg.png);position:absolute;left:0px;top:0px;width:100%;height:100%;"></div></div>';

		tab.innerHTML=strHtml;
		strHtml=null;
	}
	else
	{
		tab.appendChild(tabCon);
	}
	$(tabId+'_tabShowConter').style.width=$(tabId+'_tabShowConter').parentNode.offsetWidth+"px";
	Debug("模块页大小： "+$(tabId+'_tabShowConter').parentNode.offsetWidth);
	var tabDiv=$(tabId+'_tableDiv');
	if(rptTab.table.width.value.indexOf("%")!=-1)
		tabDiv.style.width=rptTab.table.width.value;
	else
		tabDiv.style.width=rptTab.table.width.value+"px";
	if(rptTab.table.height.value.indexOf("%")!=-1)
		tabDiv.style.height=rptTab.table.height.value;
	else
		tabDiv.style.height=rptTab.table.height.value;
	var tabTermDiv=$(tabId+'_termConter');
	buildPageTerm(tabTermDiv,rptTab);
	buildTabColNames(rptTab);
//	calcTabPrivSize(tabId);
	changeRptModelTab(tabId);
}
//function calcTabPrivSize(tabId)
//{
//	var rptTab=getAllTabs(tabId);
//	var tabDiv=$(tabId+'_tableDiv');
//	var parNode=tabDiv.parentNode;
//	var tabShowConter=$(tabId+'_tabShowConter');
//	var dataPrivTable=$(tabId+'_dataPrivTable');
//	if(rptTab.table.width.value.indexOf("%")!=-1)
//	{
//		var w=parseInt(rptTab.table.width.value) * parNode.offsetWidth/100;
//		tabDiv.style.width=w+'px';
//		if(dataPrivTable.offsetHeight>=tabShowConter.offsetHeight)
//		{
//			tabDiv.style.width=tabShowConter.offsetWidth-4+"px";
//			dataPrivTable.style.width=tabShowConter.offsetWidth-4+"px";
//		}
//	}
//	else
//	{
//		tabDiv.style.width=rptTab.table.width.value+"px";
//	}
//	var h=rptTab.table.height.value;
//	if(h.indexOf("%")!=-1)
//	{
//		h=parseInt(h) * (parNode.offsetHeight-2)/100;
//		if(rptTab.table.autoPagination.value>0 && h>parNode.offsetHeight-2-$(tabId+'_tablePaginationDiv').offsetHeight)
//		{
//			if(dataPrivTable.offsetHeight>tabShowConter.offsetHeight )
//				h-=$(tabId+'_tablePaginationDiv').offsetHeight*2;
//			else
// 				h=parNode.offsetHeight-2-$(tabId+'_tablePaginationDiv').offsetHeight;
//		}
//		tabDiv.style.height=h+'px';
//	}
//	else
//		tabDiv.style.height=h+'px';
//}
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
				break;
			}
		}
		if(dcol)
		{
			sortMap[col]=true;
			res[res.length]=dcol;
		}
		minIndex++;
	}
	clearVarVal(sortMap);
//	Debug("sortSelCol(selCols)="+res);
	return res;
}
//模块Tab页属性变化改变事件 obj 
function changeRptModelTab(tabId)
{
	var rptTab=getAllTabs(tabId);
	if(rptTab.dataSrcName.value=="")
	{
		throw "页面"+rptTab.tabName.value+"无关联数据源";
	}
	if(!waitTerm(rptTab,2))
	{
		runInterval.add(changeRptModelTab,rptTab,2,[tabId]);
		return;
	}
	readTabData(tabId);
}

