var dataCfm={
	dataConter:null,
	dataTabBar:null,
	dataCount:1,
	top:105,
	addButton:null,
	curTabId:"",
	tabWidth:{},
	getTabWidth:function()
	{
		var wd=0;
		for(var w in this.tabWidth)
		{
			wd+=this.tabWidth[w];
		}
		return wd;
	}
	
};

function initDataSrcConter()
{
	if(!dataCfm.dataTabBar)		//	判断是否从窗口参数传递了数据源信息  rptData.dataSrcTypeEnabled=false
	{
		dataSrcWin.show();
//		dataSrcWin.attachHTMLString("<div id='data_src_content' style='width:100%;height:100%;overflow:auto;' "+
//				"onclick=\"dataWinClick('data_src_content')\"></div>");
//		dataCfm.dataConter=$("data_src_content");
////		dataCfm.dataConter.innerHTML="";
//		dataCfm.dataTabBar = new dhtmlXTabBar("data_src_content", "top");
		
		dataCfm.dataTabBar=dataSrcWin.attachTabbar();
		dataCfm.dataConter=document.createElement("DIV");	//使TAB自动适应窗口最大化
//		dataCfm.dataConter.innerHTML=("<div id='data_src_content' style='width:100%;height:100%;overflow:auto;display:none;' ></div>");
		dataSrcWin.appendObject(dataCfm.dataConter);
		
		dataCfm.dataTabBar.attachEvent("onTabClose",dataSrcClose);
		dataCfm.dataTabBar.attachEvent("onSelect", dataSrcTabSelect);
		dataCfm.dataTabBar.enableScroll(false);
		dataCfm.dataTabBar.enableAutoReSize(true);
		dataCfm.dataTabBar.setSkin('dhx_skyblue');
		dataCfm.dataTabBar.setImagePath(imageRoot+"js/dhtmlx/imgs/");
//		var tabId="srcData"+dataCfm.dataCount;
//		var tabName="数据源"+dataCfm.dataCount;
//		dataCfm.tabWidth[tabId]=100;
//		dataCfm.dataTabBar.addTab(tabId, tabName,"100px");
//		dataCfm.dataTabBar.setTabActive(tabId);
		var padiv=dataCfm.dataConter.parentNode.getElementsByTagName("DIV")[2];
//		var paddiv=dataSrcWin.getElementsByTagName("DIV")[2];
//		padiv.style.textAlign="right";
		dataCfm.addButton=document.createElement("DIV");
		var nav=document.createElement("DIV");
		padiv.insertBefore(nav, padiv.lastChild);
		padiv.insertBefore(dataCfm.addButton, padiv.lastChild);

		//	判断是否从窗口参数传递了数据源信息 
		if(parInited)
		{
			//如果转入参数，需要初始化显示第一个模块中第一个TAB 数据
			//设置 reportConfig.dataCfms.value[tabId] 属性值
			//需要添加窗口传入参数，初始第一个数据源 ，并绑定到模块第一页上
			var maxTermC=0;
			for(var tabId in reportConfig.dataCfms.value)
			{
				var rptData=reportConfig.dataCfms.value[tabId];
				initData(rptData);
				var c=parseInt(tabId.replace("srcData",""));
				if(c>maxTermC)maxTermC=c;
			}
			dataCfm.dataCount=maxTermC+1;
		}
		else
		{
			var tabId=addDataSrc(); 
		}
		nav.style.textAlign="right";
		nav.style.position="relative";
//		nav.style.left="155px";
		nav.style.width="200px";
		nav.style.zIndex="100001";
		nav.style.styleFloat="right";
		nav.style.cssFloat="right";
//		nav.style.paddingBottom ="5px;"
		
//		padiv.style.textAlign="right";
		nav.innerHTML="<input type=button class=btbgb value='配置向导' id='dataCfmLeaderBt' onlick='openDataSrcCfmWin()' style='margin: 0px;' />";
		dataCfm.addButton.style.position="relative";

		dataCfm.addButton.style.left="105px";
		dataCfm.addButton.style.paddingBottom="5px";
		dataCfm.addButton.style.top="2px";
		dataCfm.addButton.style.width="20px";
		dataCfm.addButton.style.zIndex="100000";
		dataCfm.addButton.innerHTML="<input type=button value='+' id='addDataSrcTabButton' class=sbts style='width:20px;height:20px;margin: 0px;' onclick='addDataSrc()'/>" ;
		Debug(dataCfm.addButton.offsetLeft);
//		if(parInited)
//			dataCfm.dataTabBar.enableTabCloseButton(false);
//		else
		dataCfm.dataTabBar.enableTabCloseButton(true);
		if(isCustUser)
		{
			$("addDataSrcTabButton").style.display="none";
			$("dataCfmLeaderBt") .style.display="none";
			dataCfm.dataTabBar.enableTabCloseButton(false);
		}
		window.setTimeout("dataSrcWin.hide()",100);
	}
}
//添加数据TAB
function addDataSrc(rptData,initedType)
{
	var tabName="";
	var tabId="";
	if(!rptData)
	{
		tabId="srcData"+dataCfm.dataCount;
		tabName="数据源"+dataCfm.dataCount;
		dataCfm.dataCount++;
		reportConfig.dataCount++;
		rptData=new rptSrcDataCfm();
		rptData.dataSrcType.value=0;
		rptData.dataSrcId.value=0;
		rptData.dataSql.value="";
		rptData.tabId=tabId;
		rptData.mainDataTab.value="";
		rptData.dataName.value=tabName;
		reportConfig.dataCfms.value[tabId]=rptData;
	}
	else
	{
		tabName=rptData.dataName.value;
		tabId=rptData.tabId;
		switch(initedType)
		{
		case 0:
			if(!tabId)tabId="srcData"+dataCfm.dataCount;
			if(!tabName)tabName="数据源"+dataCfm.dataCount;
			rptData.dataName.value=tabName;
			rptData.tabId=tabId;
			dataCfm.dataCount++;
			reportConfig.dataCount++;
		case 1:
			reportConfig.dataCfms.value[tabId]=rptData;
			break;
		}
	}
	initData(rptData);
	return tabId;
}
function initData(rptData)
{
	var tabName=rptData.dataName.value;
	var tabId=rptData.tabId;
	dataCfm.dataTabBar.addTab(tabId, tabName,"100px");
	dataCfm.dataTabBar.setTabActive(tabId);
	dataCfm.tabWidth[tabId]=100;
	dataCfm.addButton.style.left=(dataCfm.getTabWidth()+5)+"px";
	initDataTab(tabId,tabName);
	if(reportConfig.dataCount>=reportConfig.maxDataCount)
		dataCfm.addButton.style.display="none";
	if(currentObj.obj.type=="reportModuleTab" || currentObj.obj.type=="reportConfig")	//更新数据源下拉列表
	{
		var keys=[(currentObj.obj.type=="reportModuleTab"?"dataSrcName":"dataCfms")];
		var valueTd=$(keys.join("$")+"_value");
		var hashObj=currentObj.obj[keys[0]];
//		var valId=(currentObj.obj.type=="reportModuleTab"?"dataSrcName":"dataCfms")+"_pro_value";
		bindPropertyList(valueTd,keys,hashObj);
//		idlist_change(true);
	} 
};
//删除数据源数据
function dataSrcClose(tabId)
{
	if(reportConfig.dataCount==1)return false;
	//使用中的数据源不允许删除
	for(var obj in rptObjects)
	{
		if(rptObjects[obj].type=='reportModuleTab')	//	判断是否使用了数据源
		{
			if(rptObjects[obj].dataSrcName.value==tabId)
			{
				alert("已经使用的数据源不允许删除");
				return false;
			}
		}
	}
//	Debug(length(reportConfig.dataCfms.value));
	if(reportConfig.dataCfms.value[tabId])
	{
		dataCfm.tabWidth[tabId]=null;
		reportConfig.dataCfms.value[tabId]=null;
		delete reportConfig.dataCfms.value[tabId];
		delete dataCfm.tabWidth[tabId];
	}
//	Debug(length(reportConfig.dataCfms.value));
	reportConfig.dataCount--;
	dataCfm.addButton.style.left=(dataCfm.getTabWidth()+5)+"px";
	if(reportConfig.dataCount<reportConfig.maxDataCount)
		dataCfm.addButton.style.display="";
	//更新属性窗口中有数据源下拉列表的控件值
	if(currentObj.obj.type=="reportModuleTab")
		idlist_change(true);
	return true;
}
//数据Tab选择事件
function dataSrcTabSelect(id,last_id)
{
	dataCfm.curTabId=id;
	Debug(dataCfm.curTabId);
	return true;
}
//字段最大值效验
function checkMaxLengh(obj)
{
	//Debug(String.fromCharCode(event.keyCode));
	var maxLen=obj.getAttribute("maxlength");
	if(!maxLen)return;
	maxLen=parseInt(maxLen);
	if(lenbyte(obj.value)>=maxLen)
		event.returnValue=false;
}
//命名规范有效性检查
function checkName(obj)
{
	obj.value=obj.value.trim();
	var names=obj.value.split(obj.value);
	if(!isValidName(names[names.length-1]))
	{
		obj.focus();
		alert("字符：\""+obj.value+"\" 不符合数据库命名规范");
		obj.select();
		return false;
	}
	names=null;
	return true;
}
//初始化TAB界面
function initDataTab(tabId,tabName)
{
	var rptData=reportConfig.dataCfms.value[tabId];
	//var disabled="DISABLED";//(rptData.dataSrcTypeEnabled?"":"DISABLED");
	//dataCfm.dataTabBar.setContentHTML(tabId,rptData.dataSrcId.list.join(","));
	var strHtml='<div style="overflow:auto;height:100%;width:100%;"><TABLE cellpadding="1" cellspacing="1" border="0" class="dataSrcTable" >';
	strHtml+='<tr><td colspan=4 height=22px class="dataSrcTitleTd" >基本属性</td></tr>';
	strHtml+='<tr><td class="dataSrcLabelTd" nowrap><span class=red>*</span>';
	strHtml+=rptData.dataName.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<input '+(rptData.dataName.readonly?"readonly":"")+' type=text maxlength=20 class=input150px value="'+rptData.dataName.value+'" name="dataName" onkeydown="checkMaxLengh(this)" onchange="dataSrcNameChange(this)"/>';
	strHtml+='</td>';
	//数据来源类型 dataSrcType
	strHtml+='<td class="dataSrcLabelTd" nowrap>';
	strHtml+=rptData.dataSrcType.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<select '+(rptData.dataSrcType.readonly?"DISABLED":"")+' class=input150px name="dataSrcType" onchange="dataSrcNameChange(this)">';
	for(var i=0;i< rptData.dataSrcType.list.length;i++)
	{
		strHtml+='<option value="'+rptData.dataSrcType.list[i].value+'" '+(rptData.dataSrcType.value==rptData.dataSrcType.list[i].value?"selected":"")+'  >'+rptData.dataSrcType.list[i].text+'</option>';
	}
	strHtml+='</select>';
	strHtml+='</td></tr>';
	//数据源ID dataSrcId
	strHtml+='<tr><td class="dataSrcLabelTd" nowrap><span class=red>*</span>';
	strHtml+=rptData.dataSrcId.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<select '+(rptData.dataSrcId.readonly?"DISABLED":"")+' class=input150px  name="dataSrcId" onchange="dataSrcNameChange(this)">';
	for(var i=0;i< dataSourceList.length;i++)
	{
		strHtml+='<option value="'+dataSourceList[i][0]+'" '+(rptData.dataSrcId.value==dataSourceList[i][0]?"selected":"")+'  >'+dataSourceList[i][1]+'</option>';
	}
	strHtml+='</select>';
	strHtml+='</td>';
	//主源表 mainDataTab
	strHtml+='<td class="dataSrcLabelTd" nowrap><span class=red>*</span>';
	strHtml+=rptData.mainDataTab.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<input '+(rptData.mainDataTab.readonly?"readonly":"")+' type=text maxlength=64 class=input150px value="'+rptData.mainDataTab.value+
			'" name="mainDataTab" onkeydown="checkMaxLengh(this)" onchange="if(checkName(this))dataSrcNameChange(this)"/>'+ 
			"<input type=button onclick='showProWin(initDataSrcGuide,setDataSrcGuide,[\""+tabId+"\"]);' value='…'  style='width:25px;' class='pro_more_button'/>";
	strHtml+='</td></tr>';
	//数据SQL dataSql
	strHtml+='<tr><td class="dataSrcLabelTd"  nowrap>';
	strHtml+=rptData.dataSql.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" colspan=3>';
	strHtml+='<TEXTAREA title="'+rptData.dataSql.explain+'" rows=5 '+(rptData.dataSql.readonly?"readonly":"")+' maxlength=3999  style="width:100%"  onkeydown="checkMaxLengh(this)"  name="dataSql" onchange="dataSrcNameChange(this)">'+
			rptData.dataSql.value+'</TEXTAREA>'
	strHtml+='</td></tr>';
	//数据字段列表
	strHtml+='<tr class="dataSrcTitleTd"><td colspan=4 height=22px class="dataSrcTitleTd" vALIGN=middle >'+
			'<img id="'+tabId+'_colRefresh" class="hand" src='+imageRoot+'images/refresh.png boder=0 ALIGN=absmiddle onclick="readColAttr()" />'+
			rptData.colAttrs.label+'</td></tr>';
	strHtml+='<tr><td colspan=4 id="'+tabId+'_colAttrsConter">';
	strHtml+='</td></tr>';
	//	指标解释 indexExegesisContent
	strHtml+='<tr><td colspan=4 height=22px class="dataSrcTitleTd" vALIGN=middle >' +rptData.indexExegesisContent.label
		+'<span class="gdlExplain">----'+rptData.indexExegesisContent.explain+'</span></td></tr>';
	strHtml+='<tr><td colspan=4 id="'+tabId+'_colGdlExpConter">';
	strHtml+='</td></tr>';
	strHtml+='<tr><td class="dataSrcInputTd" colspan=4>';
	strHtml+='<TEXTAREA rows=5 maxlength=3999 '+(rptData.indexExegesisContent.readonly?"readonly":"")+' style="width:100%" onkeydown="checkMaxLengh(this)" name="indexExegesisContent" onchange="if(checkGdlFormat(this))dataSrcNameChange(this)">'+
			rptData.indexExegesisContent.value+'</TEXTAREA>'
	strHtml+='</td></tr>';
	strHtml+='</table></div>';
	dataCfm.dataTabBar.setContentHTML(tabId,strHtml);
	strHtml=null;
	readColAttr();
}
//检查指标解释格式
function checkGdlFormat(obj)
{
	var tabId=dataCfm.curTabId;
	var rptData=reportConfig.dataCfms.value[tabId];
	var name=obj.getAttribute("name");
	if(rptData[name].value==obj.value)return false;
	var gdlExps=obj.value.split(";");
	for(var i=0;i<gdlExps.length;i++)
	{
		if(!gdlExps[i].trim())continue;
		var gdl=gdlExps[i].split(":");
		if(!gdl[0].trim())
		{
			alert("格式不正确,第"+(i+1)+"个指标名称为空");
			obj.focus();
			return false;
		}
		if(gdl.length!=2)
		{
			alert("格式不正确,请注意全角半角，内容：\""+gdlExps[i]+"\"");
			obj.focus();
			return false;
		}
		else if(lenbyte(gdl[0])>32)
		{
			alert("指标名称过长："+gdl[0]);
			obj.focus();
			return false;
		}
	}
	return true;
}
//数据源属性变更
function dataSrcNameChange(obj)
{
//	var d=dataCfm.dataTabBar.getActiveTab();
//	Debug(d);
	
	var tabId=dataCfm.curTabId;
	var rptData=reportConfig.dataCfms.value[tabId];
	var name=obj.getAttribute("name");
	
	var oldVal=rptData[name].value;
	rptData[name].value=obj.value;
	switch(name)
	{
	case "dataName":
		var l=lenbyte(obj.value);
		if(l*8.5>100)
			dataCfm.tabWidth[tabId]=l*8.5;
		dataCfm.dataTabBar.setLabel(tabId,obj.value,dataCfm.tabWidth[tabId]);
		dataCfm.addButton.style.left=(dataCfm.getTabWidth()+5)+"px";

		break;
	case "mainDataTab":
		if(oldVal.toUpperCase().trim()!=obj.value.toUpperCase().trim())
			checkTableExists(obj.value);
		break;
	case "dataSrcId":
		readColAttr();
		checkTableExists(obj.value);
		break;
	case "dataSql":
		if(oldVal.trim()!=obj.value.trim())
			readColAttr();
		break;
	}
	clearVarVal(oldVal);

	Debug(tabId+"_"+name+"_"+rptData[name].value);
}
//检查表是否存在
function checkTableExists()
{
	var tabId=dataCfm.curTabId;
	var rptData=reportConfig.dataCfms.value[tabId]; 
	var tabName=rptData.mainDataTab.value;
	var dataSrcId=rptData.dataSrcId.value;
	//无宏变量才检查
}
//读取查询字段信息
function readColAttr()
{
	var tabId=dataCfm.curTabId;
	var rptData=reportConfig.dataCfms.value[tabId];
	var dataSrcType=rptData.dataSrcType.value;
	var sql=rptData.dataSql.value;
	var dataSrcId=rptData.dataSrcId.value;
	if(parseInt(dataSrcType)< 3)	//	从数据库读取字段属性
	{
		if(event && event.srcElement && event.srcElement.id==tabId+'_colRefresh')
		{
			if(sql.trim()=="" || sql.length<18)alert("查询SQL长度不足");
			if(!confirm("确定要刷新吗？\n字段列表中修改的属性将丢失！"))return ;
		}
		var oldReady=rptData.ready;
		rptData.ready=false;
		if(!waitTerm(rptData,1))
		{
			runInterval.add(readColAttr,rptData,1,[]);
			return;
		}
		if(sql.trim()=="" || sql.length<18)return ;
		if(parInited)//	从参数获取列定义
		{
			rptData.colMap={};
			for(var i=0;i<rptData.colAttrs.value.length;i++)
			{
 				rptData.colAttrs.value[i].srcIndex=i;
				rptData.colMap[rptData.colAttrs.value[i].name.value]=rptData.colAttrs.value[i];
			}
			bindColAttrTable(tabId);
			rptData.ready=true;
			if(currentObj.obj.type=="reportModuleTab"  && currentObj.obj.dataSrcName.value==tabId)
				binDataColNameList();	//	只刷新列引用对象
		}
		else
		{
			var params=getMacroParams(2,rptData);
			ReportDesignerAction.qryDataCol(dataSrcId,sql,params,function(res){
				if(!res){alert("读取数据发生错误");return;}
				if(res[0]=='false'){alert(res[1]);return;}
				rptData.gdlExp={};
				for(var i=0;i<res[1].length;i++)//指标解释  名称，定义
				{//t.gdl_col_name,t.gdl_name,t.gdl_bus_desc,t.gdl_unit
					rptData.gdlExp[res[1][i][0]]=res[1][i];
				}
				rptData.colMap={};
				for(var i=0;i<rptData.colAttrs.value.length;i++)
				{
					rptData.colMap[rptData.colAttrs.value[i].name.value]=rptData.colAttrs.value[i];
					rptData.colMap[rptData.colAttrs.value[i].name.value].used=false;
				}
				rptData.colAttrs.value.length=0;
				for(var i=0;i<res[0].length;i++)
				{
					if(rptData.colMap[res[0][i]])
					{
						rptData.colAttrs.value[i]=rptData.colMap[res[0][i]];
						rptData.colMap[res[0][i]].used=true;
						rptData.colAttrs.value[i].srcIndex=i;
					}
					else
					{
						rptData.colAttrs.value[i]=new columnAttr();
						rptData.colAttrs.value[i].name.value=res[0][i];
						rptData.colAttrs.value[i].srcIndex=i;
						
						if(rptData.gdlExp[res[0][i]])
						{
							rptData.colAttrs.value[i].nameCn.value=rptData.gdlExp[res[0][i]][1];	//指标名称
							rptData.colAttrs.value[i].indexExegesis.value=rptData.gdlExp[res[0][i]][2];	//指标解释
						}
						if(!rptData.colAttrs.value[i].nameCn.value)
							rptData.colAttrs.value[i].nameCn.value=rptData.colAttrs.value[i].name.value;
						if(rptData.colAttrs.value[i].name.value=="MONTH_NO" ||
								rptData.colAttrs.value[i].name.value=="DATE_NO" ||
								rptData.colAttrs.value[i].name.value=="LOCAL_CODE" ||
								rptData.colAttrs.value[i].name.value=="AREA_ID")
						{
							rptData.colAttrs.value[i].busType.value=0;
						}
					}
	//				if(res[1][i]=="NUMBER")
	//					rptData.colAttrs.value[i].dbType.value=0;
	//				if(res[1][i]=="VARCHAR2")
	//					rptData.colAttrs.value[i].dbType.value=1;
	//				if(res[1][i]=="DATE")
	//					rptData.colAttrs.value[i].dbType.value=2;
				}
				for(var name in rptData.colMap)
				{
					if(!rptData.colMap[name].used)
					{
						clearVarVal(rptData.colMap[name]);
						rptData.colMap[name]=null;
						delete rptData.colMap[name];
					}
				}
				rptData.colMap={};
				for(var i=0;i<rptData.colAttrs.value.length;i++)
					rptData.colMap[rptData.colAttrs.value[i].name.value]=rptData.colAttrs.value[i];
				bindColAttrTable(tabId);
				if(currentObj.obj.type=="reportModuleTab"  && currentObj.obj.dataSrcName.value==tabId)
					binDataColNameList();	//	只刷新列引用对象
//					idlist_change(true);	//	全刷新
//				if(oldReady)
				rptData.ready=true;
				var allTabs=getAllTabs();
				for(var i=0;i<allTabs.length;i++)//	更新绑定此数据源的页面
				{
					if(allTabs[i].dataSrcName.value!=tabId)continue;
					if(oldReady==false || confirm("数据源【"+rptData.dataName.value+"】发生改变，页面【"+allTabs[i].text+"】是否重置展示属性列设置？"))
					{
						for(var col in allTabs[i].table.columns.value)
						{
							clearVarVal(allTabs[i].table.columns.value[col]);
							allTabs[i].table.columns.value[col]=null;
							delete allTabs[i].table.columns.value[col];
						}
						allTabs[i].table.columns.value={};	//	清空列属性设置
						for(var c=0;c<rptData.colAttrs.value.length;c++)	//	默认全部显示
						{
							var col=new colConfig();
							var colAttr=rptData.colAttrs.value[c];
							col.colName.value=colAttr.name.value;
							col.index=colAttr.srcIndex;
							col.colNameCn=colAttr.nameCn;//引用
							allTabs[i].table.columns.value[colAttr.name.value]=col;
						}
					}
					else
					{
						for(var col in allTabs[i].table.columns.value)	//	保留相同字段名称的设置值
						{
							if(!rptData.colMap[col])
							{
								allTabs[i].table.columns.value[col]=null;
								delete allTabs[i].table.columns.value[col];
								continue;
							}
							clearVarVal(allTabs[i].table.columns.value[col].colNameCn);
							allTabs[i].table.columns.value[col].colNameCn=rptData.colMap[col].nameCn;//引用
						}
					}
					if(currentObj.obj.type=="reportModuleTab")
					{
						var valId="table$columns_pro_value";
						$(valId).value=getListVal(currentObj.obj.table.columns);
					}
					if(inited)
					{
						var dt=new Date();
						if((dt-initedDate)>5000)//初始化五秒后数据源更改方执行页面刷新
							changeRptModelTab(allTabs[i].tabId,["dataSrcName"],allTabs[i]);
					}
				}
			});
		}
	}
}
//数据源字段属性变更
function dataSrcColAttrChange(obj)
{
	var tabId=dataCfm.curTabId;
	var rptData=reportConfig.dataCfms.value[tabId];
	var name=obj.getAttribute("name");
	var index=obj.getAttribute("index");
	rptData.colAttrs.value[index][name].value=obj.value;
	Debug(index+"__"+name+"_=_"+obj.value);
	if(currentObj.type=="reportModuleTab")
	{
		if(name=="nameCn" || name=="busType") 
		if(currentObj.obj.dataSrcName.value==tabId)
			binDataColNameList();
	}
	//更新所有引用了此数据源展示Tab页
	for(var obj in rptObjects)
	{
		if(rptObjects[obj].type=='reportModuleTab' && rptObjects[obj].dataSrcName.value==tabId)
		{
			bindTabDataPriv(obj,["table","columns"],rptObjects[obj]);
		}
	}
			//bindTabDataPriv(tabId,keys,rptTab);
		
}
//绑定字段属性
function bindColAttrTable(tabId)
{
	var td=$(tabId+'_colAttrsConter');
	var rptData=reportConfig.dataCfms.value[tabId];
	//属性表格
	var strHtml='<TABLE cellpadding="1" cellspacing="1" border="0" width=100% class="dataSrcColAttrTable"><tr>';
	if(rptData.colAttrs.value.length==0)
	{
		var col=new columnAttr();
		strHtml+='<td nowrap class="dataSrcColAttrHead" style="width:120px;">'+col.name.label+'</td>';
		strHtml+='<td nowrap class="dataSrcColAttrHead">'+col.nameCn.label+'<span class=red>*</span></td>';
		strHtml+='<td nowrap class="dataSrcColAttrHead">'+col.busType.label+'<span class=red>*</span></td>';
		strHtml+='<td nowrap class="dataSrcColAttrHead">关联维表/指标</td>';
		strHtml+='<td nowrap class="dataSrcColAttrHead">'+col.indexExegesis.label+'</td> ';
		strHtml+='<td nowrap class="dataSrcColAttrHead">'+col.dimCodeTransSql.label+'</td></tr>';
		col=null;
		delete col;
	}
	else
	{
		var col=rptData.colAttrs.value[0];
		strHtml+='<td nowrap class="dataSrcColAttrHead" style="width:120px;">'+col.name.label+'</td>';
		strHtml+='<td nowrap class="dataSrcColAttrHead">'+col.nameCn.label+'<span class=red>*</span></td>';
		strHtml+='<td nowrap class="dataSrcColAttrHead">'+col.busType.label+'<span class=red>*</span></td>';
		strHtml+='<td nowrap class="dataSrcColAttrHead">关联维表/指标</td>';
		strHtml+='<td nowrap class="dataSrcColAttrHead">'+col.indexExegesis.label+'</td> ';
		strHtml+='<td nowrap class="dataSrcColAttrHead">'+col.dimCodeTransSql.label+'</td></tr>';
	}
//	this.DIM_TABLE_ID=0;
//	this.DIM_TYPE_ID=0;
//	this.DIM_DATA_LEVELS=[];
//	this.GDL_ID=0;
//	this.GROUP_METHOD=0;
	for(var i=0;i<rptData.colAttrs.value.length;i++)
	{
		var col=rptData.colAttrs.value[i];
		if(i%2==0)
			strHtml+='<tr class="dataSrcColAttrRow">';
		else
			strHtml+='<tr class="dataSrcColAttrAlrRow">';
		strHtml+='<td nowrap class="dataSrcColAttrTD">'+col.name.value+'</td>';
		strHtml+='<td nowrap class="dataSrcColAttrTD" ><input style="width:100px;"  '+(col.name.readonly?"readonly":"")
				+'  maxlength=32  onkeydown="checkMaxLengh(this);checkEnter(this);"  name="nameCn" index="'+i
				+'" onchange="dataSrcColAttrChange(this)" value="'+col.nameCn.value+'"/></td>';
		//数据类型
		strHtml+='<td nowrap class="dataSrcColAttrTD" ><select style="width:70px;" '+(col.busType.readonly?"DISABLED":"")+' name="busType" index="'+i+'"  onchange="dataSrcColAttrChange(this)">';
		for(var n=0;n<col.busType.list.length;n++ )
		{
			strHtml+='<option value="'+col.busType.list[n].value+'" '+(col.busType.list[n].value==col.busType.value?"selected":"")+'>'+
				col.busType.list[n].text+'</option>';
		}
		strHtml+='</select></td>';
		strHtml+='<td class="dataSrcColAttrTD"><input readonly style="width:70px;" id="'+tabId+'_DIMGDL_ATTR_'+i+'" index="'+i+
			'" /><input type=button  value="…"  onclick="showProWin(initDataSrcGuide,setDataSrcGuide,[\''+tabId+'\','+i+',\''+col.name.value+
			'\',\''+tabId+'_DIMGDL_ATTR_'+i+'\']);" style="width:20px;" class="pro_more_button"/></td>';		//	字段指标解释
		strHtml+='<td class="dataSrcColAttrTD"><TEXTAREA rows=1 '+(col.indexExegesis.readonly?'readonly  ':'onchange="dataSrcColAttrChange(this)" onkeydown="checkMaxLengh(this)"')+
				' onmouseover="showContent(this.value,250,120)" onmouseout="hideObj(\'flowDivDyContent\')" maxlength=4000 style="width:110px;" name="indexExegesis" index="'+i+'"   >'+col.indexExegesis.value+'</TEXTAREA></td>';		//	字段指标解释
		strHtml+='<td class="dataSrcColAttrTD"><TEXTAREA rows=1 '+(col.dimCodeTransSql.readonly?'readonly ':'onchange="dataSrcColAttrChange(this)" onkeydown="checkMaxLengh(this)"')+
				' onmouseover="showContent(this.value,250,120)" onmouseout="hideObj(\'flowDivDyContent\')" maxlength=4000 style="width:110px;" name="dimCodeTransSql" index="'+i+'" >'+col.dimCodeTransSql.value+'</TEXTAREA></td>';		//	字段指标解释
		strHtml+='</tr>';
	}
	strHtml+='</table>';
	td.innerHTML=strHtml;
	strHtml=null;
}
//打开向导窗口
function openDataSrcCfmWin()
{
	var tabId=dataCfm.curTabId;
	var rptData=reportConfig.dataCfms.value[tabId];
	var dataSrcType=rptData.dataSrcType.value;
	if(dataSrcType==0)//直接SQL
	{
		
	}
	else if(dataSrcType==1)//指标组合SQL
	{
		
	}
	else if(dataSrcType==2)//表配置SQL
	{
		
	}
	else if(dataSrcType==3)//web Service
	{
		
	}
}

//	tabbar.enableAutoReSize(true); tabbar.enableAutoSize(false, true);
//	tabbar.setHrefMode("ajax-html");
//tabbar.setContentHref("a1", "../common/test_page_1.html");
//		tabbar.setContent("a2", "html_2");
//tabbar.setContentHTML(
//		var index=reportConfig.modules.value.length;
//	reportConfig.modules.value[index]=new reportModule();
// 	reportConfig.modules.value[index].window.win=addWindow();
//	reportConfig.modules.value[index].setSelf();
//	if(index==0)reportConfig.modules.value[index].window.hideHeader.value=true;
//	var wid=reportConfig.modules.value[index].window.win.getId();
//	addIds(reportConfig.modules.value[index],wid,reportConfig.modules.value[index].moduleName.value);


