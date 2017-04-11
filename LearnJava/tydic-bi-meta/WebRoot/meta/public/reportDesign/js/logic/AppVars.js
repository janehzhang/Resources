var toolbarId="toolbar_Contaier";
var menubarId="menubar_Contaier";
var rptConerId="reportContainer";
var proConerId="propertyContainer";
var proMoveHandleId="propertyMoveHandle";
var proMoveVHandleId="propertyMoveVhandle";
var tabPaneId="tabContainer";
var proListId="property_list";
var proIntroId="property_intro";
var proTitleId="property_title";
var proIdsConerId="property_idsContainer";
var rptCanvasId="rptCanvas_Model";
var rptTermConerId="reportConfig_termConter";
var rptTitleConerId="reportTitle_Container";
var imageRoot="./";

var toolBar=$(toolbarId);
var menuBar=$(menubarId);
var rptConer=$(rptConerId);
var proConer=$(proConerId);
var proMoveHandle=$(proMoveHandleId);
var proMoveVHandle=$(proMoveVHandleId);
var tabPane=$(tabPaneId);
var proList=$(proListId);
var proIntro=$(proIntroId);
var proTitle=$(proTitleId);
var proIdsConer=$(proIdsConerId);
var rptCanvas=$(rptCanvasId);
var rptTermConer=$(rptTermConerId);
var rptTitleConer=$(rptTitleConerId);

var dataSrcWin,termWin,aboutAsWin,helpWin;
//if(document.documentElement.clientHeight)
//{
//	document.body.style.height=document.documentElement.clientHeight+"px";
//	tabPane.style.height=document.body.offsetHeight+"px";
//	rptCanvas.style.height=document.body.offsetHeight+"px";
//}
var bodyWidth=document.documentElement.offsetWidth;
var bodyHeight=document.documentElement.offsetHeight;

var realTimeRefreshData=true;
var scriptRunPage=1;

window.dhx_globalImgPath = imageRoot+"images/imgs/";
docWins.setImagePath(imageRoot+"images/menuIcons/imgs/");
//docWins.enableAutoViewport(true); 
//docWins.attachViewportTo(rptCanvasId);
//docWins.setEffect("move",true);
//docWins.setEffect("resize",true);

proWin= docWins.createWindow("proWin",bodyWidth-700, 200, 400, 300);
proWin.setText("属性对话框");
proWin.setIcon("property.gif", "property.gif");
proWin.keepInViewport(true);
proWin.button("park").hide();
showGuage(true,"设计器初始化中……");

dataSrcWin = docWins.createWindow("dataSrcWin",350, 100, 800, 400);	//	数据源窗口
dataSrcWin.setText("数据源配置");
dataSrcWin.setIcon("dataSource.png", "dataSource.png");
dataSrcWin.keepInViewport(true);
dataSrcWin.button("minmax1").hide();

termWin = docWins.createWindow("termWin",350, 100, 700, 400);	//条件窗口
termWin.setText("条件配置");
termWin.setIcon("term.gif", "term.gif");
termWin.keepInViewport(true);
termWin.button("minmax1").hide();

aboutAsWin = docWins.createWindow("aboutAsWin",200, 60, 600, 380);	//	关于窗口
helpWin= docWins.createWindow("helpWin",500, 200, 400, 300);

aboutAsWin.setText("关于 Html Report Designer");
aboutAsWin.setIcon("aboutas.gif", "aboutas.gif");
aboutAsWin.keepInViewport(true);
aboutAsWin.button("minmax1").hide();
aboutAsWin.button("park").hide();
aboutAsWin.center();
aboutAsWin.denyResize();
aboutAsWin.denyMove();
aboutAsWin.attachURL("about.htm",true);
helpWin.setText("帮助");
helpWin.setIcon("help.gif", "help.gif");
helpWin.keepInViewport(true);
helpWin.button("minmax1").hide();


dataSrcWin.hide();
termWin.hide();
helpWin.hide();
aboutAsWin.hide();
//proWin.setModal(true);

function showGuage(flag,msg)
{
	if(flag)
	{
		proWin.setModal(true);
		proWin.show();
		proWin.bringToTop();
		proWin.hideHeader();
	 	proWin.setDimension(400,170);
		proWin.center();
		proWin.attachHTMLString("<table border=0  style='width:100%;height:100%;' align=left><tr>"
			+"<td width=150px ><img border=0 src='images/loading.gif' style='width:150px;height:150px;' align=absmiddle /> </td>"
			+"<td style='padding-left:15px;' id='waitDivSpan'>"+(msg?msg:"加载中 ")+"</td></tr></table>");
	}
	else
	{
		proWin.hide();
		proWin.setModal(false);
	}
	
}


docWins.attachEvent("onClose", onDocWindowClose);
function onDocWindowClose(win)
{
	if(win.isHidden())
	{
		win.show();
		win.bringToTop();
		var tabId=win.getId();
		if(tabId=="dataSrcWin")
		{
			for(var tabId in reportConfig.dataCfms.value)
			{
				dataCfm.dataTabBar.setTabActive(tabId);
				break;
			}
		}
		else if(tabId=='termWin')
		{
			for(var tabId in reportConfig.termCfms.value)
			{
				termCfm.termTabBar.setTabActive(tabId);
				break;
			}
		}
	}
	else
	{
		if(!win.callFun || win.callFun(win))
			win.hide();
	}
//	Debug(win.getId()+" change show hide;cur: "+win.isHidden());
	if(!win.isHidden() && (win.getId()=="aboutAsWin" || win.getId()=="proWin"))
	 	win.setModal(true);
	else
		win.setModal(false);
	return false;
}
//定义各子对象属性列表
var rptComAttr={
	reportConfig:"reportConfig",	
	reportTitle:"title",	
	rptSrcDataCfm: "dataName",
	reportTerm:"termName",
	reportModule:"moduleName",
	reportModuleTab:"tabName"
}; 
//以文件ID为关键字存储各对象
var rptObjects={}; 
var currentObj={
	id:"",
	obj:null,
	type:"",
	lastNode:null
	};
var propertySetType=1;	//	属性设置框生成模式 1自动生成，0 单击生成
var initPropertyType={"color":1,"chk":1,"list":1,"mulTxt":1};

function showContent(val,w,h)
{
	if(!val.trim())return;
	$("flowDivDyContent").innerHTML=maskHTMLCode(val);
	if(w)$("flowDivDyContent").style.width=w;
	if(h)$("flowDivDyContent").style.height=h;
	setdiv("flowDivDyContent");
}
//异步条件执行调度
var runInterval={
	exists:{},
	waitFuns:[],//	等待执行的函数列表
	waitObj:[],//等待检查对象
	waitType:[],//类型
	waitFunParams:[],//	等待执行的函数参数列表
	remove:function (i){
		this.waitFuns.remove(i);
		this.waitObj.remove(i);
		this.waitType.remove(i);
		this.waitFunParams.remove(i);
		
	},
	add:function(Fun,Obj,type,funParams)
	{
		var i=this.waitFuns.length;
		this.waitFuns[i]=Fun;
		this.waitObj[i]=Obj;
		this.waitType[i]=type;
		this.waitFunParams[i]=funParams;
	}
};
var runInter =window.setInterval(runWaitFun,100);
//运行异步条件函数
function runWaitFun()
{
	for(var i=0;i<runInterval.waitFuns.length;i++)
	{
		var waitObj=runInterval.waitObj[i];
		var type=runInterval.waitType[i];
		if(waitTerm(waitObj,type))	//	满足
		{
			var fun=runInterval.waitFuns[i];
			var funParams=runInterval.waitFunParams[i];
			//从调度中删除
			runInterval.remove(i);
            fun.apply(this,funParams);
		}
	}
}
//异步执行条件判断
function waitTerm(waitObj,type)
{
	if(type==0)		//	条件等待
	{
		var rptTerm=waitObj;	//	等待依赖条件
		for(var tabId in reportConfig.termCfms.value)
		{
			if(rptTerm.tabId==tabId)continue;
			if(rptTerm.parTermName.value!=tabId)continue;
			var rptTerm2=reportConfig.termCfms.value[tabId];
			return rptTerm2.ready;			
		}
		return true;
	}
	else if(type==1)			//	数据源
	{
		var rptData=waitObj;	//	等待关联条件
		var dataSql=rptData.dataSql.value;
		for(var tabId in reportConfig.termCfms.value)//通过变量判断使用了的条件
		{
			var rptTerm=reportConfig.termCfms.value[tabId];
			if(rptTerm["valueColName"].value || rptTerm["textColName"].value)
			{
				var re = new RegExp("\\{"+rptTerm["valueColName"].value+"\\}","i");
				var re2 = new RegExp("\\{"+rptTerm["textColName"].value+"\\}","i");
				if(dataSql.search(re)!=-1 || dataSql.search(re2)!=-1)
				{
					if(rptTerm.ready==false)
						return false;
				}
			}
		}
		return true;
	}
	else if(type==2)			//	预览数据
	{
		var rptTab=waitObj;	//	等待关联条件 数据源
		for(var i=0;i<rptTab.termNames.value.length;i++)	//条件判断
		{
			var termId=rptTab.termNames.value[i].value;
			var rptTerm=reportConfig.termCfms.value[termId];
			if(rptTerm.ready==false)
				return false;
		}
		var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];//数据源判断 
		return rptData.ready;
	}
	else if(type=3)
	{
		return parDataInited;
	}
}
function checkEnter(obj)
{
	if(event && event.keyCode==13)
	{
		if(obj.srcElement)obj=obj.srcElement;
		if(obj.onchange)obj.onchange();
		if(obj.onblur)obj.onblur();
	}
}