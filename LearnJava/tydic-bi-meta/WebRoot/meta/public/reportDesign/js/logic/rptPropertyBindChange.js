//回写对象值,绑定数据，更新应用各属性值
function bindRptProperty(id,keys,obj)
{
	Debug("绑定更新：type:"+obj.type+" ID:"+id+" KEYS:"+keys);
	switch(obj.type)
	{
		case "reportConfig":
			loadRptCss();
			break;
		case "reportTitle":
			changeRptTitle(keys,obj);
			break;
		case "reportModule":
			changeRptModel(id,keys,obj);
			break;
		case "reportModuleTab":
			changeRptModelTab(id,keys,obj);
			break;
	}
}
//加载报表CSS样式
function loadRptCss()
{
	var obj=rptObjects["reportConfig"];
	if(obj.styleCssText.value)
	{
		loadCssText(obj.styleCssText.value);
	}
}
//改变报表标题属性
function changeRptTitle(keys,obj)
{
	rptTitleConer.innerHTML=obj.title.value;
	if(obj.showFlag.value)rptTitleConer.style.display="";
	else rptTitleConer.style.display="none";
	rptTitleConer.style.textAlign=obj.align.value;
	rptTitleConer.style.height=obj.height.value+"px";
	rptTitleConer.style.backgroundColor=obj.bgcolor.value;
	rptTitleConer.style.fontSize =obj.font.size.value+"px";
	if(obj.font.bold.value)
		rptTitleConer.style.fontWeight  ="bold";
	else
		rptTitleConer.style.fontWeight  ="";
	rptTitleConer.style.color=obj.font.color.value;
	if(obj.cssName.value)rptTitleConer.className =obj.cssName.value;
}
//更新模块参数
function changeRptModel(id,keys,obj)
{
	obj.window.win.setText(obj.moduleName.value);
	Debug("left,top:"+obj.window.left.value+"__"+obj.window.top.value);
	obj.window.win.setPosition(parseInt(obj.window.left.value),parseInt(obj.window.top.value));
	obj.window.win.setDimension(parseInt(obj.window.width.value),parseInt(obj.window.height.value));
	if(obj.window.resizable.value)
		obj.window.win.allowResize();
	else
		obj.window.win.denyResize();
	if(obj.window.isMovable.value)
		obj.window.win.allowMove();
	else
		obj.window.win.denyMove();
	obj.window.win.setModal(obj.window.isModal.value);
	if(obj.window.stick.value)obj.window.win.button("stick").show();
	else obj.window.win.button("stick").hide();
//	if(obj.window.closeHide.value)obj.window.win.button("close").hide();
//	else obj.window.win.button("close").show();
	
	if(obj.window.minHide.value)obj.window.win.button("park").hide();
	else obj.window.win.button("park").show();
//	Debug("obj.window.minHide.value="+obj.window.minHide.value);
	if(obj.window.maxHide.value)obj.window.win.button("minmax1").hide();
	else obj.window.win.button("minmax1").show();
//	Debug("obj.window.maxHide.value="+obj.window.maxHide.value);
	if(obj.window.helpBt.value)obj.window.win.button("help").show();
	else obj.window.win.button("help").hide();
	if(obj.window.hideHeader.value)obj.window.win.hideHeader();
	else obj.window.win.showHeader();
	if(keys[1] &&(keys[1]=="left" || keys[1]=="width" || keys[1]=="top" || keys[1]=="height"))
		onWindowResze(obj.window.win);
//	calcCanvasByWin(obj.window.win);
//	obj.tabPannel.value=true;
	if(keys[0]=="tabPannel" || !obj.inited)
		initModelTab(obj);
}

//显示属性小窗口
function showProWin(initFun,callFun,keys)
{
	if(proWin.isHidden())
	{
		proWin.showHeader();
		proWin.show();
		proWin.bringToTop();
	}
 	proWin.setModal(true);
 	proWin.showHeader();
// 	if(keys==proWin.bindKey)return;
 	proWin.attachHTMLString("<div>"+keys+"</div>");
 	Debug("proWin.bindKey="+keys);
// 	clearProWinBindData();
 	proWin.callFun=callFun;
 	proWin.bindKey=keys;
 	if(initFun)initFun(keys);
}
//初始化属性小窗口内容值
function initShowProwin(keys)
{
	switch(keys)
	{
	case "inParams":
		break;
	case "outParams":
		break;
	case "table$colUniteRule":
		bindRptTabColUniteRule();
		break;
	case "table$columns":
		bindRptTabColumns();
		break;
	case "table$dataTransRule":
		bindRptTabDataTransRule();
		break;
	case "":
		break;
	}
}
//属性小窗口关闭回调
function setProwinValue(win)
{
	var keys=proWin.bindKey;//.split("$");
	var obj=currentObj.obj;
	var tabId=currentObj.id;
	Debug("id:"+tabId+"_type:"+obj.type);
	try
	{
		switch(keys)
		{
		case "inParams":
			break;
		case "outParams":
			break;
		case "table$colUniteRule":
			return setRptTabColUniteRule();
			break;
		case "table$columns":
			return setRptTabColumns();
			break;
		case "table$dataTransRule":
			return setRptTabDataTransRule();
			break;
		}
		return true;
	}
	catch(ex)
	{
		var msg="";
		if(ex.name)msg+=ex.name+":";
		if(ex.message)msg+=ex.message;
		if(!msg)msg=ex;
		alert(msg );
		return false;
	}
}

