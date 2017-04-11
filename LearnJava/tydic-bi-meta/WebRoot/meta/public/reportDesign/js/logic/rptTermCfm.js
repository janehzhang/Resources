var termCfm={
	termConter:null,
	termTabBar:null,
	termCount:1,
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
	},
	myCalendar:null
};
function initTermSrcConter()
{
	if(!termCfm.termTabBar)		//	判断是否从窗口参数传递了数据源信息  rptData.dataSrcTypeEnabled=false
	{
		termWin.show();
		termCfm.termTabBar=termWin.attachTabbar();
		termCfm.termConter=document.createElement("DIV");	//使TAB自动适应窗口最大化
		termCfm.termConter.innerHTML=("<div id='term_content' style='width:100%;height:100%;overflow:auto;display:none;' ></div>");
		termWin.appendObject(termCfm.termConter);
		
		termCfm.termTabBar.attachEvent("onTabClose",termClose);
		termCfm.termTabBar.attachEvent("onSelect", termTabSelect);
		termCfm.termTabBar.enableTabCloseButton(true);
		termCfm.termTabBar.enableScroll(false);
		termCfm.termTabBar.enableAutoReSize(true);
//		var tabId="termData"+termCfm.termCount;
//		var tabName="条件"+termCfm.termCount;
		termCfm.termTabBar.setSkin('dhx_skyblue');
		termCfm.termTabBar.setImagePath(imageRoot+"js/dhtmlx/imgs/");
//		termCfm.tabWidth[tabId]=100;
//		termCfm.termTabBar.addTab(tabId, tabName,"100px");
//		termCfm.termTabBar.setTabActive(tabId);
//		Debug("termCfm.curTabId="+termCfm.curTabId);
		var padiv=termCfm.termConter.parentNode.getElementsByTagName("DIV")[2];
//		var paddiv=termWin.getElementsByTagName("DIV")[2];
//		padiv.style.textAlign="right";
		termCfm.addButton=document.createElement("DIV");
		var nav=document.createElement("DIV");
		padiv.insertBefore(nav, padiv.lastChild);
		padiv.insertBefore(termCfm.addButton, padiv.lastChild);

		if(parInited)
		{
			var maxTermC=0;
			for(var term in reportConfig.termCfms.value)
			{
				var rptTerm=reportConfig.termCfms.value[term];
				initTerm(rptTerm);
				var c=parseInt(term.replace("termData",""));
				if(c>maxTermC)maxTermC=c;
			}
			termCfm.termCount=maxTermC+1;
			//如果转入参数，需要初始化显示第一个模块中第一个TAB 数据
			//设置 reportConfig.termCfms.value[tabId] 属性值
			//需要添加窗口传入参数，初始第一个数据源 
		}
		else
		{
			var rptTerm=new reportTerm();
			var tabName="本地网";
			rptTerm.termName.value=tabName;
			rptTerm.dataSrcId.value=0;
			rptTerm.data.value="select z.zone_code,z.zone_name from meta_dim_zone z where z.zone_par_id =(select a.zone_id from meta_dim_zone a where a.zone_code='0000' and dim_type_id=3) ";
			rptTerm.valueColName.value="local_code";
			rptTerm.textColName.value="local_name";
//			rptTerm.appendValue.value="0000";
//			rptTerm.appendText.value="四川省";
			var parTabId=addTerm(rptTerm,0);
			
			rptTerm=new reportTerm();
			tabName="营业区";
			rptTerm.termName.value=tabName;
			rptTerm.dataSrcId.value=0;
			rptTerm.data.value="select z.zone_code,z.zone_name from meta_dim_zone z where z.zone_par_id	\
								=(select a.zone_id from meta_dim_zone a where a.zone_code='{local_code}' and dim_type_id=3)";
			rptTerm.valueColName.value="area_code";
			rptTerm.textColName.value="area_name";
			rptTerm.parTermName.value=parTabId;
			rptTerm.appendValue.value="";
			rptTerm.appendText.value="全部";
			addTerm(rptTerm,0);
			
		}
//		addButton.style.textAlign="right";
		nav.style.textAlign="right";
		nav.style.position="relative";
//		nav.style.left="155px";
		nav.style.width="200px";
		nav.style.zIndex="100001";
		nav.style.styleFloat="right";
		nav.style.cssFloat="right";
//		nav.style.paddingBottom ="5px;"
//		padiv.style.textAlign="right";
		nav.innerHTML="<input type=button class=btbgb value='配置向导' id='termCfmLeaderBt' onlick='openTermCfmWin()' style='margin: 0px;' />";
		termCfm.addButton.style.position="relative";
		termCfm.addButton.style.paddingBottom="5px";
		termCfm.addButton.style.top="2px";
		termCfm.addButton.style.width="20px";
		termCfm.addButton.style.zIndex="100000";
		termCfm.addButton.innerHTML="<input type=button value='+' id='addTermTabButton' class=sbts style='width:20px;height:20px;margin: 0px;' onclick='addTerm()'/>" ;
		Debug(termCfm.addButton.offsetLeft);
		if(isCustUser)
		{
			$("addTermTabButton").style.display="none";
			$("termCfmLeaderBt") .style.display="none";
			termCfm.termTabBar.enableTabCloseButton(false);
		}
		termWin.hide();
	}
}

//添加条件TAB
function addTerm(rptTerm,initedType)
{
	var tabName="";
	var tabId="";
	if(!rptTerm)
	{
		
		tabId="termData"+termCfm.termCount;
		tabName="条件"+termCfm.termCount;
		termCfm.termCount++;
		reportConfig.termCount++;
		rptTerm=new reportTerm();
		rptTerm.termName.value=tabName;
		rptTerm.dataSrcId.value=0;
		rptTerm.data.value="select z.zone_code, z.zone_name,  (select a.zone_code from meta_dim_zone a where a.zone_id= z.zone_par_id \
						)  par_id from meta_dim_zone z order by 1,2";
//		rptTerm.data.value= "select z.zone_code,z.zone_name from meta_dim_zone z where z.zone_par_id =0; select z.zone_code,z.zone_name from meta_dim_zone z where z.zone_par_id =(select a.zone_id from meta_dim_zone a where a.zone_code='{ZONE_CODE}')";
		rptTerm.tabId=tabId;
		reportConfig.termCfms.value[tabId]=rptTerm;
	}
	else
	{
		tabName=rptTerm.termName.value;
		tabId=rptTerm.tabId;
		switch(initedType)
		{
		case 0:
			tabId=tabId?tabId:"termData"+termCfm.termCount;
			tabName=tabName?tabName:"条件"+termCfm.termCount;
			rptTerm.termName.value=tabName;
			rptTerm.tabId=tabId;
			termCfm.termCount++;
			reportConfig.termCount++;
		case 1:
			reportConfig.termCfms.value[tabId]=rptTerm;
			break;
		}
	}
	initTerm(rptTerm);
	return tabId;
}

function initTerm(rptTerm)
{
	var tabName=rptTerm.termName.value;
	var tabId=rptTerm.tabId;
	termCfm.termTabBar.addTab(tabId, tabName,"100px");
	termCfm.termTabBar.setTabActive(tabId);
	termCfm.tabWidth[tabId]=100;
	termCfm.addButton.style.left=(termCfm.getTabWidth()+5)+"px";
	initTermTab(tabId,tabName);
	if(reportConfig.termCount>=reportConfig.maxDataCount)
		termCfm.addButton.style.display="none";
//	if(currentObj.type=="reportModuleTab" || currentObj.type=="reportConfig")	//更新数据源下拉列表
//		idlist_change(true);
	termCountChange();

}
//删除条件数据
function termClose(tabId,force)
{
	if(reportConfig.termCount==1 && !force)return false;
	//使用中的条件不允许删除
	for(var obj in rptObjects)
	{
		if(rptObjects[obj].type=='reportModuleTab')	//	判断是否使用了条件
		{
			for(var i=0;i<rptObjects[obj].termNames.value.length;i++)
				if(rptObjects[obj].termNames.value[i].value==tabId)
			{
				alert("不允许删除,模块页面["+obj+"]关联使用了条件["+tabId+":"+reportConfig.termCfms.value[tabId].termName.value+"]");
				return false;
			}
		}
		if(rptObjects[obj].type=='reportConfig')
		{
			for(var i=0;i<rptObjects[obj].termNames.value.length;i++)
				if(rptObjects[obj].termNames.value[i].value==tabId)
			{
				alert("不允许删除,主页面["+obj+"]关联使用了条件["+tabId+":"+reportConfig.termCfms.value[tabId].termName.value+"]");
				return false;
			} 
		}
	}
	
	for(var tbId in reportConfig.termCfms.value)
	{
		if(reportConfig.termCfms.value[tbId].parTermName.value==tabId)
		{
			alert("不允许删除,条件["+tbId+":"+reportConfig.termCfms.value[tbId].termName.value+"]关联使用了条件["+tabId+":"+reportConfig.termCfms.value[tabId].termName.value+"]");
			return false;
		}
	}
//	Debug(length(reportConfig.termCfms.value));
	if(reportConfig.termCfms.value[tabId])
	{
		termCfm.tabWidth[tabId]=null;
		destructorDHMLX(reportConfig.termCfms.value[tabId]);
		clearVarVal(reportConfig.termCfms.value[tabId]);
		reportConfig.termCfms.value[tabId]=null;
		delete reportConfig.termCfms.value[tabId];
		delete termCfm.tabWidth[tabId];
	}
//	Debug(length(reportConfig.termCfms.value));
	reportConfig.termCount--;
	termCfm.addButton.style.left=(termCfm.getTabWidth()+5)+"px";
	if(reportConfig.termCount<reportConfig.maxDataCount)
		termCfm.addButton.style.display="";
	//更新属性窗口中有数据源下拉列表的控件值
	if(currentObj.obj.type=="reportModuleTab")
		idlist_change(true);
	termCountChange();
	return true;
}
//条件页选择事件
function termTabSelect(id,last_id)
{
	termCfm.curTabId=id;
	Debug(termCfm.curTabId);
	return true;
}
//条件页数改变事件
function termCountChange()
{
	for(var tabId in reportConfig.termCfms.value)
	{
		var list=$(tabId+'_term_parTermName');
		if(!list)continue;
		var rptTerm=reportConfig.termCfms.value[tabId];
		list.length=0;
		list.options[list.length]=new Option("无级联依赖","");
		for(var term in reportConfig.termCfms.value)
		{
			if(tabId!=term)
			{
				list.options[list.length]=new Option(reportConfig.termCfms.value[term].termName.value,term);
				if(rptTerm.parTermName.value==term)
					list.selectedIndex=list.length-1;
			}
		}
	}
}
//初始化条件TAB界面
function initTermTab(tabId,tabName)
{
	var rptTerm=reportConfig.termCfms.value[tabId];
	var strHtml='<div style="overflow:auto;height:100%;width:100%;" type=termPriv ida='+tabId+'_term  ><TABLE  type=termPriv ida='+tabId+'_term  cellpadding="0" cellspacing="1" border="0" class="dataSrcTable" >';
	strHtml+='<tr><td colspan=4 height=22px class="dataSrcTitleTd"  type=termPriv ida='+tabId+'_term >基本属性</td></tr>';
	strHtml+='<tr><td height=20px class="dataSrcLabelTd" nowrap type=termPriv ida='+tabId+'_term ><span class=red>*</span>';
	strHtml+=rptTerm.termName.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<input type=text maxlength=20 '+(rptTerm.termName.readonly?"readonly":"")+' title="'+rptTerm.termName.explain+'" class=input150px value="'+rptTerm.termName.value+
		'" name="termName" onkeydown="checkMaxLengh(this)" onchange="termValueChange(this)"/> </td>';
	//parTermName
	strHtml+='<td class="dataSrcLabelTd" nowrap>';
	strHtml+=rptTerm.parTermName.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<select id="'+tabId+'_term_parTermName" '+(rptTerm.parTermName.readonly?"DISABLED":"")+'  class=input150px name="parTermName" title="'+rptTerm.parTermName.explain+'" onchange="termValueChange(this)"><option value="" >无级联依赖</option>';
	for(var term in reportConfig.termCfms.value)
	{
		if(tabId!=term)
			strHtml+='<option value="'+term+'" '+(rptTerm.parTermName.value==term?"selected":"")+'  >'+reportConfig.termCfms.value[term].termName.value+'</option>';
	}
	strHtml+='</select>';
	strHtml+='</td></tr>';
	//termType
	strHtml+='<tr><td height=20px class="dataSrcLabelTd" nowrap type=termPriv ida='+tabId+'_term ><span class=red>*</span>';
	strHtml+=rptTerm.termType.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<select id="'+tabId+'_term_termType" '+(rptTerm.termType.readonly?"DISABLED":"")+' class=input150px name="termType"  title="'+rptTerm.termType.explain+'" onchange="termValueChange(this)">';
	for(var i=0;i< rptTerm.termType.list.length;i++)
	{
		strHtml+='<option value="'+rptTerm.termType.list[i].value+'" '+(rptTerm.termType.value==rptTerm.termType.list[i].value?"selected":"")+'  >'+rptTerm.termType.list[i].text+'</option>';
	}
	strHtml+='</select>';
	strHtml+='</td>';
	//valueType
	strHtml+='<td class="dataSrcLabelTd" nowrap><span class=red>*</span>';
	strHtml+=rptTerm.valueType.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<select  class=input150px '+(rptTerm.valueType.readonly?"DISABLED":"")+' name="dataSrcId" title="'+rptTerm.valueType.explain+'" onchange="termValueChange(this)">';
	for(var i=0;i< rptTerm.valueType.list.length;i++)
	{
		strHtml+='<option value="'+rptTerm.valueType.list[i].value+'" '+(rptTerm.valueType.value==rptTerm.valueType.list[i].value?"selected":"")+'  >'+rptTerm.valueType.list[i].text+'</option>';
	}
	strHtml+='</select>';
	strHtml+='</td></tr>';
	//valueColName
	strHtml+='<tr><td height=20px class="dataSrcLabelTd" nowrap type=termPriv ida='+tabId+'_term ><span class=red>*</span>';
	strHtml+=rptTerm.valueColName.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<input id="'+tabId+'_term_valueColName" type=text maxlength=20 '+(rptTerm.valueColName.readonly?"readonly":"")+' title="'+rptTerm.valueColName.explain+'" class=input150px value="'+rptTerm.valueColName.value+
		'" name="valueColName" onkeydown="checkMaxLengh(this)" onchange="if(checkName(this))termValueChange(this)"/>';
	strHtml+='</td>';
	//showLength
	strHtml+='<td class="dataSrcLabelTd" nowrap>';
	strHtml+=rptTerm.showLength.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<input type=text maxlength=20 '+(rptTerm.showLength.readonly?"readonly":"")+' title="'+rptTerm.showLength.explain+'" class=input150px value="'+rptTerm.showLength.value+
		'" name="showLength"  onKeyDown="onlynumber()" onchange="if(isInt(this.value))termValueChange(this)"/>';
	strHtml+='</td></tr>';
	//textColName
	strHtml+='<tr><td id="'+tabId+'_term_textColNameTd" class="dataSrcLabelTd" nowrap type=termPriv ida='+tabId+'_term >';
	strHtml+=rptTerm.textColName.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<input id="'+tabId+'_term_textColName" '+(rptTerm.textColName.readonly?"readonly":"")+' type=text maxlength=20 title="'+rptTerm.textColName.explain+'" class=input150px value="'+rptTerm.textColName.value+
		'" name="textColName" onkeydown="checkMaxLengh(this)" onchange="if(checkName(this))termValueChange(this)"/>';
	strHtml+='</td>';
	//defaultValue
	strHtml+='<td height=20px class="dataSrcLabelTd" nowrap>';
	strHtml+=rptTerm.defaultValue.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" id="'+tabId+'_term_defaultValueTd">';	//	默认值单元格 跟预览展示同步
	strHtml+='</td></tr>';
	//srcType
	strHtml+='<tr><td id="'+tabId+'_term_srcTypeTd" class="dataSrcLabelTd" nowrap type=termPriv ida='+tabId+'_term >';
	strHtml+=rptTerm.srcType.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<select id="'+tabId+'_term_srcType" '+(rptTerm.srcType.readonly?"DISABLED":"")+' class=input150px name="srcType"  title="'+rptTerm.srcType.explain+'" onchange="termValueChange(this)">';
	for(var i=0;i< rptTerm.srcType.list.length;i++)
	{
		strHtml+='<option value="'+rptTerm.srcType.list[i].value+'" '+(rptTerm.srcType.value==rptTerm.srcType.list[i].value?"selected":"")+'  >'+rptTerm.srcType.list[i].text+'</option>';
	}
	strHtml+='</select>';
	strHtml+='</td>';
	//dataSrcId
	strHtml+='<td id="'+tabId+'_term_dataSrcIdTd" height=20px class="dataSrcLabelTd" nowrap><span class=red>*</span>';
	strHtml+=rptTerm.dataSrcId.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<select id="'+tabId+'_term_dataSrcId" '+(rptTerm.dataSrcId.readonly?"DISABLED":"")+' class=input150px  title="'+rptTerm.dataSrcId.explain+'"  name="dataSrcId" onchange="termValueChange(this)">';
	for(var i=0;i< dataSourceList.length;i++)
	{
		strHtml+='<option value="'+dataSourceList[i][0]+'" '+(rptTerm.dataSrcId.value==dataSourceList[i][0]?"selected":"")+'  >'+dataSourceList[i][1]+'</option>';
	}
	strHtml+='</select>';
	strHtml+='</td></tr>';
	//data
	strHtml+='<tr><td id="'+tabId+'_term_dataTd" height=20px class="dataSrcLabelTd"  nowrap type=termPriv ida='+tabId+'_term >';
	strHtml+=rptTerm.data.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" colspan=3>';
	strHtml+='<TEXTAREA rows=4 maxlength=3999 '+(rptTerm.data.readonly?"readonly":"")+' id="'+tabId+'_term_data" style="width:100%" onkeydown="checkMaxLengh(this)"  name="data" onchange="termValueChange(this)">'+
			rptTerm.data.value+'</TEXTAREA>'
	strHtml+='</td></tr>';
 	//appendValue
	strHtml+='<tr id="'+tabId+'_term_appendOptionTr"><td height=20px class="dataSrcLabelTd" nowrap type=termPriv ida='+tabId+'_term >';
	strHtml+=rptTerm.appendValue.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<input id="'+tabId+'_term_appendValue" '+(rptTerm.appendValue.readonly?"readonly":"")+' type=text maxlength=20 title="'+rptTerm.appendValue.explain+'" class=input150px value="'+rptTerm.appendValue.value+
		'" name="appendValue" onkeydown="checkMaxLengh(this)" onchange="termValueChange(this)"/>';
	strHtml+='</td>';
	//appendText
	strHtml+='<td class="dataSrcLabelTd" nowrap>';
	strHtml+=rptTerm.appendText.label+'：</td>';
	strHtml+='<td class="dataSrcInputTd" >';
	strHtml+='<input id="'+tabId+'_term_appendText" '+(rptTerm.appendText.readonly?"readonly":"")+' type=text maxlength=20 title="'+rptTerm.appendText.explain+'" class=input150px value="'+rptTerm.appendText.value+
		'" name="appendText"  onKeyDown="checkMaxLengh(this)" onchange="termValueChange(this)"/>';
	strHtml+='</td></tr>';

	strHtml+='<tr><td colspan=4 height=22px class="dataSrcTitleTd" type=termPriv ida='+tabId+'_term >预览</td></tr>';
	strHtml+='<tr><td colspan=2 height=40px  class="dataSrcLabelTd" style="padding-left:0px;text-align: left;" id='+tabId+'_term ida='+tabId+'  type=termConter  > '
		+'</td><td colspan=2 style="background-color:#FFFFFF;padding-left:5px;color:#333333" >可拖动预览框到界面实现条件配置，拖回预览栏则解除与界面的绑定 </td></tr>';
	strHtml+='</table></div>';
	termCfm.termTabBar.setContentHTML(tabId,strHtml);
	strHtml=null;
	rptTermCtl($(tabId+"_term"),rptTerm);
	if(parInited)//	自动添加到第一模块第一页的条件栏上
	{
		var tabs=getAllTabs();
		for(var i=0;i<tabs.length;i++ )
		{
			var find=false;
			for(var j=0;j<tabs[i].termNames.value.length;j++)
			{
				if(tabs[i].termNames.value[j].value==tabId)
				{
					find=true;
					break;
				}
			}
			if(!find)continue;
			var conPar=tabs[i].tabId+"_termConter";
			property_params.outObj=$(tabId+"_term");
			property_params.Handle=$(tabId+"_term_termConer");
			property_params.inObj=$(conPar);
			property_params.term_dragging=true;
			rptTermUp(true);
			break;
		}
		if(!find)
		for(var j=0;j< reportConfig.termNames.value.length;j++)
		{
			if(reportConfig.termNames.value[j].value==tabId)
			{
				find=true;
				var conPar="reportConfig_termConter"; 
				property_params.outObj=$(tabId+"_term");
				property_params.Handle=$(tabId+"_term_termConer");
				property_params.inObj=$(conPar);
				property_params.term_dragging=true;
				rptTermUp(true);
				break;
			}
		}
		
		//var winTabId=rptWins.curModel.getId()+"@tab0";
	}
	else
	{
		var winTabId=reportConfig.modules.value[0].window.win.getId()+"@tab"+reportConfig.modules.value[0].tabCount;
		var conPar=winTabId+"_termConter";
		property_params.outObj=$(tabId+"_term");
		property_params.Handle=$(tabId+"_term_termConer");
		property_params.inObj=$(conPar);
		property_params.term_dragging=true;
		rptTermUp();
	}	
	setTermDataExplain(rptTerm);
} 
//根据类型设置条件数据字段解释
function setTermDataExplain(rptTerm)
{
	var dataObj=$(termCfm.curTabId+'_term_data');
	if(rptTerm.termType.value=="2")
		return dataObj.title=rptTerm.data.explain.split("$")[rptTerm.termType.value];
	return dataObj.title=rptTerm.data.explain.split("$")[rptTerm.termType.value].split("；")[rptTerm.srcType.value];
}
//条件值变更事件
function termValueChange(obj)
{
//	var d=termCfm.termTabBar.getActiveTab();
//	Debug(d);
	
	var tabId=termCfm.curTabId;
	var rptTerm=reportConfig.termCfms.value[tabId];
	var name=obj.getAttribute("name");
	
	var oldVal=rptTerm[name].value;
	obj.value=obj.value.trim();
	rptTerm[name].value=obj.value;
	switch(name)
	{
	case "termName":
		var l=lenbyte(obj.value);
		if(l*8.5>100)
			termCfm.tabWidth[tabId]=l*8.5;
		termCfm.termTabBar.setLabel(tabId,obj.value,termCfm.tabWidth[tabId]);
		termCfm.addButton.style.left=(termCfm.getTabWidth()+5)+"px";
		break;
	case "termType":
		rptTerm.defaultValue.value=[];
		rptTerm.data.value="";
		$(tabId+'_term_data').value="";
		changeTermType();
		break;
	case "srcType":
		if(rptTerm[name].value=="0")
		{
			$(tabId+"_term_dataSrcIdTd").disabled=true;
			$(tabId+"_term_dataSrcId").disabled=true;
			$(tabId+"_term_data").value="";
			rptTerm.data.value="";
		}
		else
		{
			$(tabId+"_term_dataSrcIdTd").disabled=false;
			$(tabId+"_term_dataSrcId").disabled=false;
			$(tabId+"_term_data").value="";
			rptTerm.data.value="";
		}
		break;
	case "valueColName":
	case "textColName":	//	验证变量名称唯一
//		alert(obj.value);
		rptTerm[name].value=rptTerm[name].value.toUpperCase();
		obj.value=obj.value.toUpperCase();
		if(checkMacro(name,rptTerm[name].value)==false)
		{
			alert("宏变量名称不唯一");
			obj.value=oldVal;
			rptTerm[name].value=oldVal;
		}
		break;
	case "data":	//变更在生成预览时验证
		break;
	case "appendText":
		updateListTermPrivData(rptTerm);
		break;
	}
	rptTermCtl($(tabId+"_term"),rptTerm,name,oldVal);
	clearVarVal(oldVal);
	setTermDataExplain(rptTerm);
	Debug(tabId+"_"+name+"_"+rptTerm[name].value);
}
//宏变量唯一性检查,不存在返回true
function checkMacro(name,value)
{ 
	var curTerm=reportConfig.termCfms.value[termCfm.curTabId];
	if(value=="")return true;//curTerm[name].
	if(curTerm["textColName"].value!="" && curTerm["valueColName"].value==curTerm["textColName"].value)return false;
	for(var tabId in reportConfig.termCfms.value)
	{
		if(termCfm.curTabId==tabId)continue;
		var rptTerm=reportConfig.termCfms.value[tabId];
		if(value==rptTerm["valueColName"].value || value==rptTerm["textColName"].value)
		{
			return false;
		}
	}
	return true;
}
//条件类型变化切换控件有效性
function changeTermType()
{
	var tabId=termCfm.curTabId;
	var rptTerm=reportConfig.termCfms.value[tabId];
	var obj=rptTerm.termType;
	$(tabId+"_term_srcTypeTd").disabled=false;
	$(tabId+"_term_srcType").disabled=false;
	$(tabId+"_term_textColNameTd").disabled=false;
	$(tabId+"_term_textColName").disabled=false;
	$(tabId+"_term_dataSrcIdTd").disabled=false;
	$(tabId+"_term_dataSrcId").disabled=false;
	$(tabId+"_term_dataTd").innerHTML=rptTerm.data.label+'：';
	if(obj.value=='0')
	{
		//$(tabId+"_appendOptionTr").style.display="";
		$(tabId+"_term_appendOptionTr").disabled=false;
		$(tabId+"_term_appendValue").disabled=false;
		$(tabId+"_term_appendText").disabled=false;
	}
	else
	{
//		$(tabId+"_appendOptionTr").style.display="none";
		$(tabId+"_term_appendOptionTr").disabled=true;
		$(tabId+"_term_appendValue").disabled=true;
		$(tabId+"_term_appendText").disabled=true;
		$(tabId+"_term_appendValue").value="";
		$(tabId+"_term_appendText").value="";
		rptTerm["appendValue"].value="";
		rptTerm["appendText"].value="";
		if(obj.value=='2')
		{
			$(tabId+"_term_srcTypeTd").disabled=true;
			$(tabId+"_term_srcType").disabled=true;
			$(tabId+"_term_textColNameTd").disabled=true;
			$(tabId+"_term_textColName").disabled=true;
			$(tabId+"_term_dataSrcIdTd").disabled=true;
			$(tabId+"_term_dataSrcId").disabled=true;
			$(tabId+"_term_dataTd").innerHTML="值区间：";
		}
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


