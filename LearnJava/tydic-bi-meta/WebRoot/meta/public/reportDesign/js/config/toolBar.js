
toolbar.setIconsPath(imageRoot+"images/toolIcons/imgs/");
var buttons=[
	[1,"new","新建", "new.gif", "new_dis.gif"],
	[0,"sep1"],
	[1,"open" , "打开", "open.gif", "open_dis.gif"],
	[1,"save" , "保存", "save.gif", "save_dis.gif"],
	[1,"saveas" , "另存为","save_as.gif","save_as_dis.gif"],
	[0,"sep2"],
	[1,"addModel","添加模块","addModel.gif","addModel.gif"],
	[0,"sep3"],
	[1,"dataSource","数据源","dataSource.png","dataSource.png"],
	[1,"term","条件窗口","term.png","term.png"],
	[1,"attr","属性窗口","property.png","property.png"],
	[1,"preview","预览窗口","preview.png","preview.png"]
	];

for(var i=0;i<buttons.length;i++)
{
	if(buttons[i][0]==1)
	{
		toolbar.addButton(buttons[i][1],i,buttons[i][2],buttons[i][3],buttons[i][4]);
	}
	else if(buttons[i][0]==0)
	{
		toolbar.addSeparator(buttons[i][1],i);
	}
}
if(isCustUser)
{
	toolbar.disableItem("addModel");
	toolbar.disableItem("new");
	toolbar.disableItem("open");	
}

var toolFun={
	"new":"createNewRpt()"	//	新建
	,"open":"openRpt()"	//	打开
	,"save":"saveRpt()"	//	保存
	,"saveas":"saveAsRpt()"//
	,"addModel":"addModel()"
	,"dataSource":"onDocWindowClose(dataSrcWin)"
	,"term":"onDocWindowClose(termWin)"
	,"attr":"change_property_state()"
	,"preview":"previewRpt()"
};
//toolbar.setItemToolTipTemplate("new", "%v");
toolbar.setItemToolTip("new", "new");
//toolbar.setValue("new", "1", false);
//Debug( toolbar.getValue("new"));
toolbar.attachEvent("onClick",toolOnClick);
function toolOnClick(id)
{
	if(toolFun[id])
	{
		Debug("执行："+toolFun[id]);
		eval(toolFun[id]);
		return;
	}
}
function changeToolTextState()
{
	if(toolbar.getItemText(buttons[0][1]))
	{
		for(var i=0;i<buttons.length;i++)
		{
			if(buttons[i][0]==1)
				toolbar.setItemText(buttons[i][1],"");
		}
	}
	else
	{
		for(var i=0;i<buttons.length;i++)
		{
			if(buttons[i][0]==1)
				toolbar.setItemText(buttons[i][1],buttons[i][2]);
		}
	}
	

}
//toolbar.attachEvent("onStateChange", function(id, state){Debug("工具栏事件：onStateChange "+id+"__"+state);});    
//toolbar.attachEvent("onValueChange", function(id, state){Debug("工具栏事件：onValueChange "+id+"__"+state);});    

