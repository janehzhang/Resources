
var rptTitileDiv=$("rptTitileDiv");
var rptTitileTr=$("rptTitileTr");
var rptTermTr=$("rptTermTr");
var rptTermDiv=$("rptTermDiv");
var rptModuleTr=$("rptModuleTr");
var rptModuleDiv=$("rptModuleDiv");
var rptTitleConerId="rptTitileDiv";
var scriptRunPage=0;
var rptConer=rptModuleDiv;
var rptCanvas=$("rptCanvas_Model");
rptConer.style.overflow="auto";
var bodyWidth=document.documentElement.offsetWidth;
var bodyHeight=document.documentElement.offsetHeight;
window.dhx_globalImgPath = imageRoot+"images/imgs/"; 
rptWins.setEffect("move",true);
rptWins.setEffect("resize",true);
rptWins.attachEvent("onMoveFinish", onWindowMove);
rptWins.attachEvent("onResizeFinish", onWindowResze);
rptWins.attachEvent("onMaximize", onWindowResze);
rptWins.attachEvent("onMinimize", onWindowResze);

proWin= rptWins.createWindow("proWin",bodyWidth-700, 200, 400, 300);
proWin.setText("属性对话框");
proWin.setIcon("property.gif", "property.gif");
proWin.keepInViewport(true);
proWin.button("park").hide();
showGuage(true,"读取报表配置数据中……<p>大表查询需要一定时间，请耐心等待!");

//追加提示信息
function addTipMsg(msg)
{
	var obj=$('waitDivSpan');
	if(obj)
	{
		obj.innerHTML+="<bt/>"+msg;
	}
}
//显示提示信息
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
//		proWin.hide();
//		proWin.setModal(false);
	}
	
}
//
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
//		var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];//数据源判断 
//		return rptData.ready;
		return true;
	}
	else if(type=3)
	{
		return parDataInited;
	}
}
function onWindowMove(win)
{
	calcCanvasByWin(win);
}

function onWindowResze(win)
{
	var wid=win.getId();
	var mod=getMod(wid);
	for(var tabId in mod.tabs)
	{
		moduleTabContentSize(tabId);
	}
	calcCanvasByWin(win);
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
	if(x<rptConer.scrollLeft )
		rptConer.scrollLeft=x;
	else if(x+dim[0] >rptConer.scrollLeft+rptConer.offsetWidth)
		rptConer.scrollLeft=x+dim[0]-rptConer.offsetWidth;
	if(y<rptConer.scrollTop)
		rptConer.scrollTop=y;
	else if(y+dim[1]>rptConer.scrollTop+rptConer.offsetHeight)
		rptConer.scrollTop=y+dim[1] -rptConer.offsetHeight; 
}
