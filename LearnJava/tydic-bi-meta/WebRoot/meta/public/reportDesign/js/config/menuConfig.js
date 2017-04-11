

//menu.renderAsContextMenu();

//menu.attachEvent("onXLS", menuXLS);
//menu.attachEvent("onXLE", menuXLE);

//menu.attachEvent("onTouch", menuTouch);
//menu.attachEvent("onShow", menuShow);
//menu.attachEvent("onHide", menuHide);
//menu.showContextMenu(100,100);
//menu.addContextZone("reportContainer");  //右键 
//alert(menu.isContextZone("reportContainer"));
//menu.removeContextZone(rptConerId);
//menu.attachEvent("onCheckboxClick", menuCheckboxClick);
//menu.attachEvent("onRadioClick", menuRadioClick);
menu.setIconsPath(imageRoot+"images/menuIcons/imgs/");
menu.addNewSibling(null, "file", "文件(F)", false); 
menu.addNewSibling("file", "data", "窗口(W)", false);
menu.addNewSibling("data", "view", "视图(V)", false);
menu.addNewSibling("view", "help", "帮助(H)", false);
//文件
menu.addNewChild("file", 0, "file_new", "新建(N)", false,"new.gif"); 
menu.setHotKey("file_new", "Ctrl+N"); 
menu.addNewChild("file", 1, "file_open", "打开(O)", false,"open.gif","open_dis.gif"); 
menu.setHotKey("file_open", "Ctrl+O");  
menu.addNewSeparator("file_new"); 
menu.addNewChild("file", 3, "file_save", "保存(S)", false,"save.gif","save_dis.gif"); 
menu.setHotKey("file_save", "Ctrl+S");
menu.addNewChild("file", 4, "file_saveas", "另存为...", false,"save_as.gif","save_as_dis.gif"); 
menu.setHotKey("file_saveas", "Ctrl+Shift+S");
menu.addNewSeparator("file_saveas"); 
menu.addNewChild("file", 6, "file_close", "关闭(C)", false,"close.gif","close.gif"); 
menu.setHotKey("file_close", "Ctrl+Q");
//编辑
menu.addNewChild("data", 0, "add_model", "添加模块", false,"addModel.gif","addModel.gif"); 
menu.setHotKey("add_model", "Ctrl+M");
menu.addNewChild("data", 0, "data_source", "数据源", false,"dataSource.png","dataSource.png"); 
menu.setHotKey("data_source", "Ctrl+D");
menu.addNewSeparator("data_source"); 
menu.addNewChild("data", 2, "data_term", "查询条件(T)", false,"term.png","term.png"); 
menu.setHotKey("data_term", "Ctrl+T");
//视图
menu.addCheckbox("child","view", 0, "view_pro_window", "属性窗口(P)", true);
menu.addCheckbox("child","view", 1, "view_tool_text", "工具栏标签", true);
menu.addNewSeparator("view_tool_text"); 
menu.addNewChild("view", 3, "view_preview", "预览报表","preview.png","preview.png");
menu.setHotKey("view_preview", "Ctrl+Shift+V");
//帮助
menu.addNewChild("help", 0, "help_about", "关于(A)", false,"aboutas.gif","aboutas.gif");
menu.addNewChild("help", 1, "help_help", "帮助(H)", false,"help.jpg","help.jpg");
menu.setHotKey("edit_close", "Ctrl+H");
menu.attachEvent("onClick", menuClick);

if(isCustUser)
{
	menu.setItemDisabled("file_new");
	menu.setItemDisabled("file_open");
	menu.setItemDisabled("add_model");
}

var menuFun={
	"file_new": "createNewRpt()"//"新建(N)" 
	,"file_open": "openRpt()"//"打开(O)"
	,"file_save":"saveRpt()"// "保存(S)"
	,"file_saveas":"saveAsRpt()"// "另存为..."
	,"file_close": "rptClose()"//"关闭(C)"
	,"add_model":"addModel()"
	,"data_source": "onDocWindowClose(dataSrcWin)"	//打开属性窗口
	,"data_term":"onDocWindowClose(termWin)"// "查询条件(T)"
	,"view_pro_window": "change_property_state()"	//	打开属性窗口
	,"view_tool_text": "changeToolTextState()"//"工具栏标签"
	,"view_preview":"previewRpt()"//预览
	,"help_about": "onDocWindowClose(aboutAsWin)"//"关于(A)"
	,"help_help": "onDocWindowClose(helpWin)"//"帮助(H)"
};

function menuClick(id, zoneId, casState)
{
	if(menuFun[id])
	{
		Debug("执行："+menuFun[id]);
		eval(menuFun[id]);
		return;
	}
	
//	switch(id)
//	{
//		default:
//			var lwin=rptWins.window("rptModel1");
//			lwin.park();
//	}
	
//	Debug ("<b>"+zoneId+" onClick</b> "+id+"'" + 
//		menu.getItemText(id) + "' was clicked, ctrl: " + 
//		(casState && casState["ctrl"] ? "<b>true</b>": "false") + ", alt: " +
//		(casState && casState["alt"] ? "<b>true</b>": "false") + ", shift: " + 
//		(casState && casState["shift"] ? "<b>true</b>": "false"));
}

//function menuCheckboxClick(id, state,zoneId, casState) {
//Debug ( "<b>onCheckboxClick</b> '" + menu.getItemText(id) + "' was clicked, ctrl: " + (casState["ctrl"] ? "<b>true</b>": "false") + ", alt: " + (casState["alt"] ? "<b>true</b>": "false") + ", shift: " + (casState["shift"] ? "<b>true</b>": "false") + "<br>");
//    return true;
//}
//function menuRadioClick(group, idChecked, idClicked, zoneId, casState) {
//Debug( "<b>onRadioClick</b> '" + menu.getItemText(clicked) + "' was clicked, ctrl: " + (casState["ctrl"] ? "<b>true</b>": "false") + ", alt: " + (casState["alt"] ? "<b>true</b>": "false") + ", shift: " + (casState["shift"] ? "<b>true</b>": "false") + "<br>");
//    return true;
//}
