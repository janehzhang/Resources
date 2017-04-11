rptWins.setImagePath(imageRoot+"images/menuIcons/imgs/");

rptWins.attachEvent("onResizeFinish", onWindowResze);
rptWins.attachViewportTo(rptCanvasId); 
//rptWins.setViewport(0, 0,0,0,rptCanvas);
rptConer.style.overflow="scroll";
rptWins.setEffect("move",true);
rptWins.setEffect("resize",true);
rptWins.attachEvent("onMoveFinish", onWindowMove);
rptWins.attachEvent("onClose", onWindowClose);
rptWins.attachEvent("onFocus", onRptModelFocus);
//docWins.attachEvent("onFocus", onRptModelFocus);
rptWins.attachEvent("onMaximize", onWindowResze);
rptWins.attachEvent("onMinimize", onWindowResze);
var rptWinIdSeq=0;
//添加报表模块
function AddRptModel()
{
	var rptMod=new reportModule();
 	rptMod.window.win=addWindow(rptMod);
	rptMod.setSelf();
	var wid=rptMod.window.win.getId();
	rptMod.modId=wid;
	var index=reportConfig.modules.value.length;
	reportConfig.modules.value[index]=rptMod;
	if(index==0)rptMod.window.hideHeader.value=true;
	if(!isCustUser)rptMod.tabPannel.type="chk";
	addIds(rptMod,wid,rptMod.moduleName.value);
	initModelTab(rptMod);
	linkModuleList();
}
function initModule(rptMod)
{
	if(!isCustUser)rptMod.tabPannel.type="chk";
	if(!rptMod.window.win)
		rptMod.window.win=addWindow(rptMod);
	addIds(rptMod,rptMod.modId,rptMod.moduleName.value);
	//需要先绑定Tab页面
	initModelTab(rptMod);
	
	changeRptModel(rptMod.modId,[],rptMod);
	calcCanvasByWin(rptMod.window.win);
}
function addWindow(rptMod)//modId,modName
{
	var p = 0;
 	var lwin;
 	var modName=rptMod.moduleName.value;
	rptWins.forEachWindow(function(ow){p++;lwin=ow;});
    if (p > 10) 
    {
        alert("Too many windows");
        return;
    }
    var id =rptMod.modId?rptMod.modId:"rptModule" + (++rptWinIdSeq);
    var w = 500,h = 300,x = 0,y = 0;
    if(p==1)
	{
    	lwin.button("stick").show();
    	lwin.button("close").show();
    	lwin.allowResize();
    	lwin.allowMove();
    	lwin.allowPark();
    	lwin.setDimension(rptConer.offsetWidth/2,rptConer.offsetHeight/2);
		lwin.setPosition(0,0);
		lwin.showHeader();
		reportConfig.modules.value[0].window.hideHeader.value=false;
		onWindowResze(lwin);
	}
    //;
    var win = rptWins.createWindow(id, x, y, w, h);
    p++;
    win.setText(modName?modName:"模块"+rptWinIdSeq);
    win.setIcon("bedit.gif", "bedit.gif");
//    win.button("stick").show();	
    if(p==1)
    {
		rptTermConer.style.display="none";
		win.hideHeader();
//    	win.denyResize();
//    	win.denyMove();
//    	win.denyPark();
		win.setPosition(0,0);
    	win.setDimension(rptConer.offsetWidth-30,rptConer.offsetHeight-30);
    }
    else
    {
    	reportConfig.rptTitle.showFlag.value=true;
    	rptTermConer.style.display="";
    	if(p==2)
    	{
    		win.setPosition(rptConer.offsetWidth/2,0);
    		win.setDimension(rptConer.offsetWidth/2-10,rptConer.offsetHeight/2);
    	}
    	else
    	{
    		win.center();
    	}
    	
    	win.allowResize();
    	win.allowMove();
    	win.allowPark();
	}
    win.setMinDimension(250,180);
    if(!rptMod.modId)rptMod.modId=id;
    return win;
}
//rptWins.attachEvent("onParkUp", function(win){
//       Debug("onParkUp"); // code here
//       return false;
//    });
//rptWins.attachEvent("onParkDown", function(win){
//       Debug("onParkDown"); // code here
//       return true;
//    });
//根据模块ID获取模块对象
function getModule(modId)
{
	for(var i=0;i<reportConfig.modules.value.length;i++)
	{
		if(reportConfig.modules.value[i].modId==modId)
			return reportConfig.modules.value[i];
	}
}
function onWindowMove(win)
{
	calcCanvasByWin(win);
	rptObjects[win.getId()].setSelf();
	if(currentObj.obj.type=="reportModule")idlist_change(true);
}
function onWindowResze(win)
{
	var wid=win.getId();
	rptObjects[wid].setSelf();
	for(var tabId in rptObjects[wid].tabs)
	{
		moduleTabContentSize(tabId);
	}
	calcCanvasByWin(win);
	if(currentObj.obj.type=="reportModule")idlist_change(true);
}
//同步页面内容宽高
//function moduleTabContentSize(tabId)
//{
//	var tabPage=$(tabId+"_content");
//	if(!tabPage)return;
//	var tabPagePar=tabPage.parentNode;
//	tabPage.style.width=tabPagePar.offsetWidth+"px";
//	tabPage.style.height=tabPagePar.offsetHeight+"px";
//	var tabShowConter=$(tabId+'_tabShowConter');
//	if(!tabShowConter)return;
//	tabShowConter.style.width=tabPagePar.offsetWidth-2+"px";
//	tabShowConter.style.height=tabPagePar.offsetHeight-2+"px";
//	var dataPrivTable=$(tabId+'_dataPrivTable');
//	if(!dataPrivTable)return;//预览框
//	var tableDiv=$(tabId+'_tableDiv');
//	var tablePaginationDiv=$(tabId+'_tablePaginationDiv');
//	var graphDiv=$(tabId+'_graphDiv');
//	var termConter=$(tabId+'_termConter');
//	var rptTab=getAllTabs(tabId);
//	//判断是否需要竖滚动条
//	tableDiv.style.width=100+"px";	//表格框宽度
//	tablePaginationDiv.style.width=100+"px";
//	graphDiv.style.width=100+"px";	//图形框宽度
//	if(dataPrivTable.offsetHeight>tabShowConter.offsetHeight)
//		 dataPrivTable.style.width=tabShowConter.offsetWidth-25+"px";
//	else 
//		dataPrivTable.style.width=tabShowConter.offsetWidth-0+"px";
//	dataPrivTable.style.height=tabShowConter.offsetHeight-0+"px";
//	var tabWidth=dataPrivTable.offsetWidth-4;
//	var tabHeight=tabShowConter.offsetHeight;
//	
//	tableDiv.style.width=tabWidth+"px";	//表格框宽度
//	tablePaginationDiv.style.width=tabWidth+"px";
//	graphDiv.style.width=tabWidth+"px";	//图形框宽度
//	if(rptTab.table.width.value.indexOf("%")!=-1)
//	{
//		var w=parseInt(rptTab.table.width.value) * tabWidth/100;
//		tableDiv.style.width=w+'px';
//	}
//	else
//	{
//		tableDiv.style.width=rptTab.table.width.value+"px";
//	}
//	var showType=rptTab.showType.value;
//	var graphTr=$(tabId+'_graphTr');
//	var tableTr=$(tabId+'_tableTr');
//	var graphTD=$(tabId+'_graphTD');
//	var tableTD=$(tabId+'_tableTD');
//	var h=rptTab.table.height.value;
//	if(showType==0)	//表格
//	{
//		if(h.indexOf("%")!=-1)
//		{
//			h=parseInt(h) * (tabHeight-termConter.offsetHeight-2)/100;
//			if(h>tabHeight-termConter.offsetHeight)
//				h=tabHeight-termConter.offsetHeight;
//		}
//		if(h<100)h=100;
//		tableDiv.style.height=h-tablePaginationDiv.offsetHeight-2+"px";	//	不自动计算
//	}
//	else if(showType==1)//图形
//	{
//		$(rptTab.tabId+'_graphTr').style.display="";
//		$(rptTab.tabId+'_tableTr').style.display="";
//		$(rptTab.tabId+'_graphTD').style.height="40%";
//		$(rptTab.tabId+'_tableTD').style.height="50%";
//	}
//	else if(showType==2)//表格+ 图形
//	{
//		if(h.indexOf("%")!=-1)
//		{
//			h=parseInt(h) * (tabHeight-termConter.offsetHeight-2)*6/10/100;//表格60%高  图形40%高
//			h=tabHeight-termConter.offsetHeight;
//		}
//		if(h<100)h=100;
//		tableDiv.style.height=h-tablePaginationDiv.offsetHeight-2+"px";	//	不自动计算
//	}
//	Debug("dataPrivTable.WH="+dataPrivTable.offsetWidth+":"+dataPrivTable.offsetHeight);
//}
function onRptModelFocus(win)
{
//	Debug(win.getId()+" focus");
	rptWins.curModel=win;
	if(typeof(onClick)=='function')
		onClick(win.getId());
	win.bringToTop();
}
function calcCanvasByWin(win)
{
	var pos=win.getPosition();
	var x=pos[0];
	var y=pos[1];
	if(x<0)x=0;
	if(y<0)y=0;
	win.setPosition(x,y);
	var dim=win.getDimension();
	if(parseInt(x)+parseInt(dim[0])> rptCanvas.offsetWidth)
	{
		rptCanvas.style.width=parseInt(x)+parseInt(dim[0]);
	}
	if(parseInt(y)+parseInt(dim[1])> rptCanvas.offsetHeight)
	{
		rptCanvas.style.height=parseInt(y)+parseInt(dim[1]);
	}
//	Debug(x+dim[0]+"__rptCanvas.offsetWidth="+rptCanvas.offsetWidth);
	if(x<rptConer.scrollLeft )
		rptConer.scrollLeft=x;
	else if(x+dim[0] >rptConer.scrollLeft+rptConer.offsetWidth)
		rptConer.scrollLeft=x+dim[0]-rptConer.offsetWidth;
	if(y<rptConer.scrollTop)
		rptConer.scrollTop=y;
	else if(y+dim[1]>rptConer.scrollTop+rptConer.offsetHeight)
		rptConer.scrollTop=y+dim[1] -rptConer.offsetHeight;
}
function onWindowClose(win)
{
	var p = 0;
	var lwin;
    rptWins.forEachWindow(function(ow){p++;if(ow.getId()!=win.getId())lwin=ow;});
    if(p==2)
    {
    	Debug(lwin.getId());
    	lwin.allowResize();
    	lwin.denyMove();
    	lwin.denyPark();
 		lwin.setPosition(0,0);
 		lwin.hideHeader();
    	lwin.setDimension(rptConer.offsetWidth-10,rptConer.offsetHeight-50);
    	rptTermConer.style.display="none";
    	reportConfig.modules.value[0].window.hideHeader.value=true;
		rptObjects[lwin.getId()].setSelf();
		onWindowResze(lwin);
	}
    removeModelAndTabs(win);
    var wid=win.getId();
    for(var i=0;i<reportConfig.modules.value.length;i++)
    {
    	if(wid==reportConfig.modules.value[i].window.win.getId())
    	{
    		reportConfig.modules.value.remove(i);
    		break;
    	}
    }
    linkModuleList();
//    Debug("reportConfig.modules.value.length="+reportConfig.modules.value.length);
//    win.innerHTML="";
	return true;
}

