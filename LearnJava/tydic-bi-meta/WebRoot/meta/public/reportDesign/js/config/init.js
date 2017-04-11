var inited=false;
var initedDate=new Date();
var parDataInited=false;
//window.showModalDialog("modalDialogSource.htm", myObject, "dialogHeight:300px; dialogLeft:200px;");

var shortcutFun={
	"CTRL+N":"createNewRpt()" //"新建(N)" 
	,"CTRL+O":"openRpt()"// "打开(O)"
	,"CTRL+S":"saveRpt()"//"保存(S)"
	,"CTRL+SHIFT+S":"saveAsRpt()"//"另存为..."
	,"CTRL+Q":"rptClose()"//"关闭(C)"
	,"CTRL+M":"addModel()"
	,"CTRL+D":"onDocWindowClose(dataSrcWin)"	// "数据源"
	,"CTRL+T":"onDocWindowClose(termWin)"//"查询条件(T)"
	,"CTRL+P":"change_property_state()"//"属性窗口(P)"
	,"CTRL+SHIFT+V":"previewRpt()"	//预览报表
	,"CTRL+L":"changeToolTextState()"//"工具栏标签"
	,"CTRL+H":"onDocWindowClose(helpWin)"//"帮助(H)"
     };
// 
if(dhtmlx._isIE)
{	// IE5 way of setting up event handlers is way too good to ignore
	window.attachEvent("onload", onInit); 
	window.attachEvent("onresize", Resize);
	document.attachEvent("onkeydown", onKeyDown);
	document.attachEvent("onmousedown", MouseDown);
	document.attachEvent("onmouseup", MouseUp);
	document.attachEvent("onmousemove", MouseMove);
	rptTermConer.onclick=onClick; 
	rptTitleConer.onclick=onClick; 
	window.onbeforeunload=unRptDesignerload;
}
else //if(dhtmlx._isWebKit)
{
	window.onload=onInit;
	window.onresize=Resize;
	window.onbeforeunload=unRptDesignerload;
	document.onkeydown=onKeyDown;
	document.onmousedown = MouseDown;
	document.onmouseup = MouseUp;
	document.onmousemove = MouseMove;
	rptTermConer.onclick=onClick; 
	rptTitleConer.onclick=onClick; 
	onDocWindowClose(aboutAsWin);
//	onDocWindowClose(dataSrcWin);
	window.setTimeout("onDocWindowClose(aboutAsWin);",100);
//	window.setTimeout("onDocWindowClose(dataSrcWin);",1000);

}
 
//onInit();
function onInit()
{
//	proMoveHandle.style.left=tabPane.offsetWidth-300;
//	proConer.style.left=tabPane.offsetWidth-300;
	reSetControl();
	ReportDesignerAction.readInitData(function(res)
		{
			if(!res || res.length==0)
			{
				alert("读取源配置错误，无法进行报表配置，请刷新重试。");
				showGuage();
				return;
			}
			dataSourceList=res;
			if(parInited)
			{
				readRptInitParam();//通过参数初始数据
				initShowModuleTabData();
			}
			else
			{
				addIds(reportConfig,"reportConfig","报表全局配置",true);
				reportConfig.rptTitle.id.value=rptTitleConerId;
				reportConfig.rptTitle.font.size.value=16;
				reportConfig.rptTitle.title.value="Title";
			//	reportConfig.rptTitle.showFlag.value=false;
				addIds(reportConfig.rptTitle,reportConfig.rptTitle.id.value,reportConfig.rptTitle.title.value);
				//添加报表模块
				rptCanvas.style.height=rptConer.offsetHeight;
				rptCanvas.style.width=rptConer.offsetWidth;
				changeRptTitle("",rptObjects[reportConfig.rptTitle.id.value]);	//	初始化标题栏属性
				AddRptModel();	//	必需先初始化模块页面
				initTermSrcConter();
				initDataSrcConter();
				var rptTab=getAllTabs()[0];
				for(var dc in reportConfig.dataCfms.value)	//	绑定页面数据源,测试用
				{
					rptTab.dataSrcName.value=dc;
					break;
				}
				showGuage();
				parDataInited=true;
				changeRptModelTab(rptTab.tabId,["dataSrcName"],rptTab);
				idlist_change(true);
				inited=true;
			}
		}
	);
	proList.scrollTop=0;
}
function readRptInitParam()
{
	if(modificationRpt)
	{
		clearVarVal(reportConfig,toJsonFilter);
		reportConfig=eval("rpt="+window.opener.rptJsonData);
		if(userId!=reportConfig.USER_ID)
			reportConfig.REPORT_ID=0;	//	复制
		parDataInited=true;
	}
	else
	{
		startdt=new Date();
		ReportDesignerAction.readRptInitCfg(initType,rptId,function(res){
			if(res==null || res[0]=="false")
			{
				alert("读取报表初始化数据错误."+(res?"\n"+res[1]:""));
				parDataInited=true;
				return;
			}
			if(initType==1)	//	指标SQL配置初始化
			{
				rptGdlInit(res);
			}
			else if(initType==2)	//	模型SQL配置初始化
			{
				rptModelInit(res);
			}
			else if(rptId)	//	修改 
			{
				rptModificationInit(res);
			}
			
			clearVarVal(res);
		});		
	}
}
function initShowModuleTabData()
{
	if(!waitTerm(null,3))
	{
		runInterval.add(initShowModuleTabData,null,3,[]);
		return;
	}
	addIds(reportConfig,"reportConfig","报表全局配置",true);				
	addIds(reportConfig.rptTitle,reportConfig.rptTitle.id.value,reportConfig.rptTitle.title.value);
	rptCanvas.style.height=rptConer.offsetHeight;
	rptCanvas.style.width=rptConer.offsetWidth;
	changeRptTitle("",rptObjects[reportConfig.rptTitle.id.value]);	//	初始化标题栏属性
	//添加报表模块	
	for(var i=0;i<reportConfig.modules.value.length;i++)
	{
		var rptMod=reportConfig.modules.value[i];
		var modId=rptMod.modId;
		//
		initModule(rptMod);//AddRptModel();	//	必需先初始化模块页面
		var max=parseInt(modId.replace("rptModule",""));
		if(rptWinIdSeq<max)rptWinIdSeq=max;
	}
	if(reportConfig.modules.value.length==0)AddRptModel();	//	必需先初始化模块页面
	initTermSrcConter();
	initDataSrcConter();
	//刷新所有模块页面
	var rptTabs=getAllTabs();
	for(var i=0;i<rptTabs.length;i++)
		changeRptModelTab(rptTabs[i].tabId,["dataSrcName"],rptTabs[i]);
	showGuage();
	idlist_change(true);
	inited=true;
//	clearVarVal(rptParamCfg);
}
//获取报表页面模块显示页面集合
function getAllTabs(tabId)
{
	if(tabId)
	{
		for(var obj in rptObjects)
		{
			if(rptObjects[obj].type=="reportModuleTab" && rptObjects[obj].tabId==tabId)
				return rptObjects[obj];
		}
	}
	else
	{
		var tabs=[];
		for(var obj in rptObjects)
		{
			if(rptObjects[obj].type=="reportModuleTab")
				tabs[tabs.length]=rptObjects[obj];
		}
		return tabs;
	}
}
function Resize()
{
	reSetControl();
}
function onClick(sid)
{
	if(!sid)Debug("onClick :"+event.srcElement.id);
	var list=$("ids_list");
	var id=sid;
	if(!sid)id=event.srcElement.id;
	for(var i=0;i<list.length;i++)
	{
		if(list.options[i].value==id)
		{
			list.selectedIndex=i;
			idlist_change();
			break;
		}
	}
	return true;
}
function MouseDown()
{
	//barConfig.obj.dockBarOnMouseDown();
	//moveme_onmousedown();
	property_down();
	property_Vdown();
	//draw_Down();
	rptTermDown();
	if(event.srcElement)
	{
//		Debug("MouseDown  "+event.srcElement.id);
	}
	return true;
}
function MouseUp()
{
	//barConfig.obj.dockBarOnMouseUp();
	//moveme_onmouseup();
	property_up();
	property_Vup();
	//draw_Up();
	rptTermUp();
	if(event.srcElement)
	{
//		Debug("MouseUp  "+event.srcElement.id);
	}
	return true;
}
function MouseMove()
{	
	//barConfig.obj.dockBarOnMouseMove();
	property_move();
	property_Vmove();
	rptTermMove();
	//draw_Move();
	//moveme_onmousemove();
	if(event.srcElement)
	{
//		Debug("MouseMove  "+event.srcElement.id);
	}
	return true;
}
function onKeyDown(e)
{
//	Debug(e.srcElement.id+"_"+event.ctrlKey+"_"+event.keyCode+"_"+event.shiftKey+"_"+event.altKey);
	//ctrl+D 68 ctrl+T 84
	var key="";
	if(event.ctrlKey)key="CTRL+";
	if(event.shiftKey)key+="SHIFT+";
	if(event.altKey)key+="ALT+";
	key+=String.fromCharCode(event.keyCode);
	if(shortcutFun[key])
	{
		Debug("执行："+shortcutFun[key]);
		eval(shortcutFun[key]);
		event.returnValue=false;
		event.cancelBubble=false;
		return false;
	}
//	alert(event.keyCode);
	if(event && event.keyCode==27)
	{
		onEsckey();
	}
	return true;
}
function onEsckey()
{
	if(!aboutAsWin.isHidden())
	{
		onDocWindowClose(aboutAsWin);
	}
	if(!proWin.isHidden())
	{
		if(saving || !proWin.bindKey)return;
		onDocWindowClose(proWin);
	}
}
function reSetControl(types)
{
	Debug(rptConer.style.width);
	


//	proConer.style.left=tabPane.offsetWidth-proConer.offsetWidth-proMoveHandle.style.left;
	var proWidth=proConer.offsetWidth;
	window.status=""+proConer.style.display;
	if(proConer.style.display=="none")proWidth=0;
	Debug("显示："+proConer.style.display+" proWidth="+proWidth);
	rptConer.style.width=tabPane.offsetWidth-proMoveHandle.offsetWidth-proWidth;
	rptConer.style.height=tabPane.offsetHeight-toolBar.offsetHeight-menuBar.offsetHeight-2;
	if(proWidth)
	{
		proConer.style.top=toolBar.offsetHeight+menuBar.offsetHeight;
		proConer.style.height=tabPane.offsetHeight-toolBar.offsetHeight-menuBar.offsetHeight-2;
		proMoveHandle.style.top=proConer.offsetTop;
		proMoveHandle.style.height=proConer.offsetHeight;
		proMoveHandle.style.zIndex =100;
		proMoveHandle.style.left=rptConer.offsetWidth;
		proMoveHandle.style.top=proConer.offsetTop;
		proMoveVHandle.style.top=proTitle.offsetHeight+proIdsConer.offsetHeight+proList.offsetHeight+8-2;
//		/:;
		try {
		proIntro.style.position="relative";
//		proIntro.style.top=proTitle.offsetHeight+proIdsConer.offsetHeight+proList.offsetHeight+proMoveVHandle.offsetHeight+6;
		proIntro.style.top=proMoveVHandle.offsetHeight;
		proIntro.style.width=proConer.offsetWidth-3;
		proIntro.style.height=proConer.offsetHeight-proTitle.offsetHeight-proIdsConer.offsetHeight-proList.offsetHeight-proMoveVHandle.offsetHeight-8;
		proConer.style.left=proMoveHandle.offsetLeft+proMoveHandle.offsetWidth;
		proTitle.style.width=proConer.offsetWidth-$("close_img").offsetWidth-5;
		}catch(e) {
		
		}
	}
	Debug("proTitle.style.width="+proTitle.style.width);
	if(dhtmlx._isWebKit)
	{
		proIntro.style.width=proConer.offsetWidth-8;
	}
	Debug("rptCanvas "+rptCanvas.offsetWidth+"__"+rptConer.offsetWidth);
	if(rptCanvas.offsetWidth<rptConer.offsetWidth)rptCanvas.style.width=rptConer.offsetWidth;
	if(rptCanvas.offsetHeight<rptConer.offsetHeight)rptCanvas.style.height=rptConer.offsetHeight;
	//alert($("property_title").style.width+"__"+proConer.offsetWidth);
}

function unRptDesignerload()
{
	var uid=$("termData3_term_privObj");
	//清理变量
    event.returnValue='请正常退出系统!';
    
}
function destructorDHMLX(obj)
{
	if(!obj)return;
	if(obj.DHTML_TYPE)
	{
		switch(obj.DHTML_TYPE)
		{
		case "dhtmlXCombo":
		case "dhtmlXGrid":
		case "dhtmlXTree":
			if(obj.DHTML_TYPE=="dhtmlXCombo"){
				var a=dhx_glbSelectAr;
				for(var b=0;b<a.length;b++)
					if(a[b]==obj)
					{a[b]=null;a.splice(b,1);break;}
			}
			tryEval(obj.destructor);
			break;
		case "dhtmlXCalendar":
			obj.i=[];
			tryEval(obj.unload);
		}
		return;
	}
	for(var o in obj)
	{
		if(obj[o] && obj[o].DHTML_TYPE)
		{
			switch(obj[o].DHTML_TYPE)
			{
			case "dhtmlXCombo":
			case "dhtmlXGrid":
			case "dhtmlXTree":
				if(obj[o].DHTML_TYPE=="dhtmlXCombo"){
					var a=dhx_glbSelectAr;
					for(var b=0;b<a.length;b++)
						if(a[b]==obj[o])
						{a[b]=null;a.splice(b,1);break;}
				}
				tryEval(obj[o].destructor);
				break;
			case "dhtmlXCalendar":
				obj[o].i=[];
				tryEval(obj[o].unload);
			}
		}
		else if(obj[o] && obj[o]!=obj && typeof obj[o]=="object")
		{
			destructorDHMLX(obj[o]);
		}
	}
}
function myCalendarDestructor()
{
}
//var oPopup = window.createPopup();
//var oPopBody = oPopup.document.body;
//oPopBody.style.backgroundColor = "lightyellow";
//oPopBody.style.border = "solid black 1px";
//oPopBody.innerHTML = "Click outside <B>popup</B> to close.";
//oPopup.show(100, 100, 180, 25, document.body);
// 
//				var sel = document.selection.createRange();
//	            sel.text =value;
