attachObjEvent(window,"onload",pageInit);

var startdt=new Date();
function pageInit()
{
	if(previewRpt)
	{
		clearVarVal(reportConfig,toJsonFilter);
//		reportConfig=window.opener.reportConfig;
		reportConfig=eval("rpt="+window.opener.rptJsonData);
		buildReportPage();
		showGuage();
	}
	else
	{
		pageInit1();
	}
}
//初始化报表展现页面
function pageInit1()
{
//	/rptId var isCustUser=0;var userId=1;
	var startdt=new Date();
//	if(!initType && !dhtmlx._isIE)
//	{
//		pageInit2();return;
//	}
	ReportDesignerAction.readRptInitCfg(initType,rptId,function(res){
		if(res==null || res[0]=="false")
		{
			alert("读取报表配置发生错误，"+(res?"："+res[1]:"")+".请刷新重试")
			return;
		}
		addTipMsg("完成配置读取，解析中……");
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
		addTipMsg("解析完成，生成页面中……");
		buildReportPage();
		if(debug)
		{
			var dt2=new Date();
			var m=(dt2.getHours()-startdt.getHours())*60+dt2.getMinutes()-startdt.getMinutes();
			var s=m*60+dt2.getSeconds()-startdt.getSeconds();
			var mm=s*1000+dt2.getMilliseconds()-startdt.getMilliseconds();
			var st=(mm/1000);
			Debug("readRptJsCfg完成配置读取用时："+st + "s");
		}
		showGuage();
	});
}
function pageInit2()
{
//	/rptId var isCustUser=0;var userId=1;
	ReportDesignerAction.readRptCfg(rptId,function(res){
		if(res==null || res[0]=="false")
		{
			alert("读取报表配置发生错误，"+(res?"："+res[1]:"")+".请刷新重试")
			return;
		} 
		addTipMsg("完成配置读取，解析中……");
		parseRptRule(res[1]);//解析构造reportConfig
		addTipMsg("解析完成，生成页面中……");
		buildReportPage();
		if(debug)
		{
			var dt2=new Date();
			var m=(dt2.getHours()-startdt.getHours())*60+dt2.getMinutes()-startdt.getMinutes();
			var s=m*60+dt2.getSeconds()-startdt.getSeconds();
			var mm=s*1000+dt2.getMilliseconds()-startdt.getMilliseconds();
			var st=(mm/1000);
			Debug("readRptCfg完成配置读取用时："+st + "s");
		}
		showGuage();
	});
}
//构造reportConfig
function parseRptRule(cfg)
{
	
}
function buildReportPage()
{
	try
	{
		addDestroyVar && addDestroyVar(reportConfig);
		loadCssText(reportConfig.styleCssText.value);
		buildTitle();
		buildPageTerm(rptTermDiv,reportConfig);
		buildDataSrc();
		var oneModuleOneTab=(getAllTabs().length==1);
		if(reportConfig.rptTitle.showFlag.value)
			rptTitileTr.style.display="";
		if(reportConfig.termNames.value.length)
			rptTermTr.style.display="";
		if(oneModuleOneTab)
			moduleTabHeightFix=$("rptTitileDiv").offsetHeight;
		buildPageModules();
		
		if(debug)
		{
			var dt2=new Date();
			var m=(dt2.getHours()-startdt.getHours())*60+dt2.getMinutes()-startdt.getMinutes();
			var s=m*60+dt2.getSeconds()-startdt.getSeconds();
			var mm=s*1000+dt2.getMilliseconds()-startdt.getMilliseconds();
			var st=(mm/1000);
			Debug("加载完成 用时："+st + "s");
		}
		try{
			bodyResize();
		}catch(e) {
			
		}
		return true;
	}
	catch(ex)
	{
		if(typeof ex=="string")alert(ex);
		else
		{
			var msg="";
			for(var a in ex)
				msg+=a+":"+ex[a]+"\n";
			alert(msg);
		}
		return false;
	}
	
}
function bodyResize()
{
	var oneModuleOneTab=(getAllTabs().length==1);
	if(oneModuleOneTab)
	{
		var rptTab=getAllTabs()[0];
		var tabShowCon=$(rptTab.tabId+'_tabShowConter');
		bodyWidth=$("bodyConter").offsetWidth;
		bodyHeight=$("bodyConter").offsetHeight;
 		tabShowCon.insertBefore(rptTitileDiv,tabShowCon.childNodes[0]);
 		rptTitileDiv.style.width=bodyWidth-20+"px";
 		var cover=$(rptTab.tabId+'_estopCover');
		if(!cover)
			rptTitileDiv.style.width=cover.offsetWidth+"px";
 		rptCanvas.style.width=bodyWidth+"px";
 		rptCanvas.style.height=bodyHeight+"px";
 		reportConfig.modules.value[0].window.win.setDimension(bodyWidth,bodyHeight);
 		rptTitileTr.style.display="none";
 		moduleTabContentSize(rptTab.tabId);
 		moduleTabContentSize(rptTab.tabId);
	}
}
function buildDataSrc()
{
	for(var tabId in reportConfig.dataCfms.value)
	{
		var rptData=reportConfig.dataCfms.value[tabId];
		if(!rptData.colMap)
		{
			rptData.colMap={};
			for(var i=0;i<rptData.colAttrs.value.length;i++)
			{
	 			rptData.colAttrs.value[i].srcIndex=i;
				rptData.colMap[rptData.colAttrs.value[i].name.value]=rptData.colAttrs.value[i];
			}	
		}
	}
}
function openDesigner()
{
	if(previewRpt)
		window.close();
	else
	{
			window.rptJsonData=JSON.stringify(reportConfig,toJsonFilter);
			winopen(designerUrl+rptId,"designer");
	}
}
function buildTitle()
{
	var obj=reportConfig.rptTitle;
	rptTitileDiv.innerHTML=obj.title.value;//float:right;
	if(!obj.showFlag.value)
	{
		rptTitileDiv.style.display="none";
		rptTitileTr.style.display="none";
	}
	rptTitileDiv.style.textAlign=obj.align.value;
	rptTitileDiv.style.height=obj.height.value+"px";
	rptTitileDiv.style.backgroundColor=obj.bgcolor.value;
	rptTitileDiv.style.fontSize =obj.font.size.value+"px";
	rptTitileDiv.parentNode.style.verticalAlign  ="text-bottom";
	if(obj.font.bold.value)
		rptTitileDiv.style.fontWeight  ="bold";
	else
		rptTitileDiv.style.fontWeight  ="";
	rptTitileDiv.style.color=obj.font.color.value;
	if(obj.cssName.value)rptTitileDiv.className =obj.cssName.value;
}
function buildPageModules()
{
	for(var i=0;i<reportConfig.modules.value.length;i++)
	{
		var rptMod=reportConfig.modules.value[i];
		var modId=rptMod.modId;
		initModule(rptMod);//AddRptModel();	//	必需先初始化模块页面
		calcCanvasByWin(rptMod.window.win);
	}
}
function getMod(modId)
{
	for(var i=0;i<reportConfig.modules.value.length;i++)
	{
		if(reportConfig.modules.value[i].modId==modId)return reportConfig.modules.value[i];
	}
}
//获取报表页面模块显示页面集合
function getAllTabs(tabId)
{
	if(tabId)
	{
		for(var i=0;i<reportConfig.modules.value.length;i++) //for(var mod in reportConfig.modules.value)
		{
			var mod=reportConfig.modules.value[i];
			for(var tab in mod.tabs)
			{
				if(!mod.tabs[tab].type || mod.tabs[tab].type!="reportModuleTab")continue;
				if(mod.tabs[tab].tabId==tabId)
					return mod.tabs[tab];
			}
		}
	}
	else
	{
		var tabs=[];
		for(var mod in reportConfig.modules.value)
		{
			mod=reportConfig.modules.value[mod];
			for(var tab in mod.tabs)
			{
				if(!mod.tabs[tab].type || mod.tabs[tab].type!="reportModuleTab")continue;
				tabs[tabs.length]=mod.tabs[tab];
			}
		}
		return tabs;
	}
}
function exportExcel(tabId)
{
	var rptTab=getAllTabs(tabId);
	rptTab.dowLoadType=0;
	var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
	var dataSrcId=rptData.dataSrcId.value;
	var sql=rptData.dataSql.value;
	var params=getMacroParams(3,rptTab);	//	条件值
	for(var i=0;i<rptTab.inParams.value.length;i++)//需要添加页面初始输入参数
		params[rptTab.inParams.value.name]=rptTab.inParams.value.value;

	var rptTitleStr=reportConfig.rptTitle.title.value;
	if(reportConfig.modules.value.length>1)
		rptTitleStr+="_"+getMod(rptTab.modId).moduleName.value;
	if(getMod(rptTab.modId).tabs.length>2)
		rptTitleStr+="_"+rptTab.tabName.value;
	if(!rptId)rptId=0;
	$("reportId_input").value=rptId+","+rptTab.dowLoadType;
//if(previewRpt || initType==1 || initType==2)	//预览的 
//{
		$("reportConfig_input").value =JSON.stringify(reportConfig,toJsonDataFilter);
//	}
//	else if(rptId)	//	修改 ，服务端从数据库读取
//	{
//		$("reportConfig_input").value="";
//	}
	$("reportParams_input").value=JSON.stringify(params,toJsonFilter);
	$("reportDownTab_input").value=tabId;
	$("reportTitle_input").value=rptTitleStr;
	$("reportDownForm").submit();
}
function toJsonDataFilter(key, value)
{
	if(filterKeys[key])return null;
	if(typeof value === 'function')return null;
	if(value &&((value.hasOwnProperty && value.hasOwnProperty('tagName')) || value.tagName))return null;
	return value;
}
 