//获取指定属性名称的值
function getObjectVal(id,names)
{
	try
	{
		var tmp=rptObjects[id][names[0]];
		for(var i=1;i<names.length;i++)
		{
			tmp=tmp[names[i]];
		}
		return tmp.value;
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
//获取列表值
function getListVal(hashObj,textName)
{
	var str=[];
	textName=textName?textName:hashObj.text;
	if(hashObj.vp=="array" && hashObj.value.length)
	{
		if(textName)
		{
			for(var i=0;i<hashObj.value.length;i++)
				if(hashObj.value[i][textName].value)
					str[str.length]=hashObj.value[i][textName].value;
				else
					str[str.length]=hashObj.value[i][textName];
			return str.join(",");
		}
		else
			return hashObj.value.join(",");
	}
	else if(hashObj.vp=="map")
	{
		for(var iv in hashObj.value)
		{
//			Debug("map:name "+hashObj.text);
			if(hashObj.value[iv][textName].value)
				str[str.length]=hashObj.value[iv][textName].value;
			else
				str[str.length]=hashObj.value[iv][textName];
		}
		return str.join(",");
	}
	return hashObj.value?hashObj.value.sort?hashObj.value.join():hashObj.value:"";//keys+" 空值";
	
	
	//rptSrcDataCfm.colAttrs ===> columnAttr
	//rptSrcDataCfm.indexExegesisContent==>indexExegesis
	//reportModuleTab.termNames==>reportTerm
	//reportModuleTab.outParams==>paramConfig
	//reportModuleTab.inParams==>paramConfig
	//reportModuleTab.table.columns==>colConfig()
	//						colTotal ==>colTotalMothed
	//				colConfig.alertTerms==>colAlert
	//reportConfig.termNames==>  reportTerm
	//reportConfig.modules==>   reportModule
	//reportConfig.termCfms==> reportTerm
	//reportConfig.dataCfms==>rptSrcDataCfm 
	//
	//
}
//打开属性关联窗口
function openListProperty(el)
{
	if(el.srcElement)el=el.srcElement;
	var obj=currentObj.obj;
	var keys=el.id.split("_")[0];
	switch(obj.type)
	{
		case "reportConfig":
		{
			switch(keys)
			{
			case "termCfms":
			case "termNames":	//	以小表格条件列表 一行一个
				termWin.show();
				termWin.bringToTop();
				break;
			case "dataCfms":
				dataSrcWin.show();
				dataSrcWin.bringToTop();
				break;
			}
		}
		break;
		case "reportModuleTab":
		{
			switch(keys)
			{
			case	"termNames":
				termWin.show();
				termWin.bringToTop();
				//showProWin(initShowProwin,setProwinValue,keys);
				break;
			case "inParams":
			case "outParams":	//	已取消输出参数列表，默认为所有输出和查询字段
			case "table$colUniteRule":		//	表头合并规则
			case "table$columns":			//	表格显示字段列属性
			case "table$dataTransRule":		//	横纵转换规则
			case "graph$columns":			//	图形字段列属性
			case "graph$dataTransRule":		//	横纵转换规则
				showProWin(initShowProwin,setProwinValue,keys);
				//使用模态窗口
				break;
///	case 	"dataFormart"://colConfig()  sel  未绑定数据
//	case 	"alertTerms"://colConfig()   list
			}
		}
		break;
	}
}
//	绑定属性列表
function bindPropertyList(valueTd,keys,hashObj)
{
	var nameTd=$(keys.join("$")+"_name");
	var valId=keys.join("$")+"_pro_value";
	var key=keys[keys.length-1]; 
	var obj=currentObj.obj;
	switch(key)
	{
	case	"termNames":	//		reportModuleTab() reportConfig() list	条件列表
//		valueTd.innerHTML="<input type='text' readonly style='width:80%;height:20px;' class='inuptText' id='" 
//				+valId+"' title='"+hashObj.explain.replace(/<br\/>/ig,'\n')+"'/> <input type=button id="+valId
//				+"_more value='…' onclick='openListProperty(this)' style='width:25px;' class='pro_more_button'/> ";
//		$(valId).value=getListVal(keys,hashObj.value);
//		if(propertySetType==0)$(valId).focus();
//		break;
		//	case 	"inParams": //reportModuleTab() list	//报表页post传入参数，展示时需要检查的依赖参数
	case 	"outParams"://reportModuleTab()  list
	case "colUniteRule"://reportModuleTab().table list
	case "columns":	//reportModuleTab().table 	list   colName:colConfig()   
	case "dataTransRule"://reportModuleTab().table list
		valueTd.innerHTML="<input type='text' readonly style='width:100px;height:20px;' class='inuptText' id='" 
				+valId+"' onclick='openListProperty(this)' title='"+hashObj.explain.replace(/<br\/>/ig,'\n')+"'/> <input type=button id="+valId
				+"_more value='…'  onclick='openListProperty(this)' style='width:25px;' class='pro_more_button'/>";
		$(valId).value=getListVal(hashObj);
		if(propertySetType==0)$(valId).focus();
		break;
	case	"dataCfms":
	case 	"modules":
	case 	"termCfms":
		valueTd.innerHTML="<input type='text' readonly "+(hashObj.readonly?"disabled":"")+"  style='width:100%;height:20px;' class='inuptText' id='" 
				+valId+"'  onclick='openListProperty(this)'  title='"+hashObj.explain.replace(/<br\/>/ig,'\n')+"'/> ";
		$(valId).value=getListVal(hashObj);
		if(propertySetType==0)$(valId).focus();
		if(event && event.srcElement && (event.srcElement.id==valueTd.id||event.srcElement.id==nameTd.id))
			openListProperty($(valId));
		break;
	case "dataSrcId":	//rptSrcDataCfm() sel  在数据源管理窗口实现了
//		valueTd.innerHTML="<select style='width:100%;height:20px;' class='selectText' id='"+valId+"' onblur='setPropertyValue(this)' ></select>";
//		var list=$(valId);
//		for(var i=0;i<dataSourceList.length;i++)
//		{
//			for(var i=0;i<hashObj.list.length;i++)
//			{
//				list.options[list.length]=new Option(hashObj.list[i].text,hashObj.list[i].value);
//				if(value==hashObj.list[i].value)
//				{
//					list.options[list.options.length-1].selected=true;
//					hashObj.text=hashObj.list[i].text;
//				}
//			}
//		}
		break;
	case "dataSourceId":	//reportTerm() sel	在条件窗口实现了
		break;
	case "dataSrcName"://reportModuleTab()  sel
		var list=$(valId);
		list.length=0;
		list.options[list.length]=new Option("请选择数据源","");
		for(var dc in reportConfig.dataCfms.value)
		{
			list.options[list.length]=new Option(reportConfig.dataCfms.value[dc].dataName.value,reportConfig.dataCfms.value[dc].tabId);
			if(hashObj.value==dc)
			{
				list.options[list.options.length-1].selected=true;
				hashObj.text=dc.dataName;
			}
		}
		hashObj.value=list.value;
		break;
	case "linkModelIds"://reportModuleTab() combobox
//	case "showColNames":	//reportModuleTab().table combo
	case "rowUniteCols"://reportModuleTab().table combobox
	case "colFreeze":	//	reportModuleTab().table combobox
		if(hashObj.readonly)
		{
			valueTd.innerHTML="<input type='text' readonly "+(hashObj.readonly?"disabled":"")+"  style='width:100%;height:20px;' class='inuptText' id='" 
					+valId+"'  onclick='openListProperty(this)'  title='"+hashObj.explain.replace(/<br\/>/ig,'\n')+"'/> ";
			break;
		}
		valueTd.innerHTML="<DIV type='text' style='width:100%;height:20px;' class='inuptText' id='" 
				+valId+"'  onclick='openListProperty(this)'  title='"+hashObj.explain.replace(/<br\/>/ig,'\n')+"'></DIV> ";
		if(hashObj.combo)
		{
			hashObj.combo.clearAll(true);
			destructorDHMLX(hashObj.combo);
			hashObj.combo=null;
			delete hashObj.combo;
		}
		hashObj.combo=new dhtmlXCombo(valId,valId+"_val",130,'checkbox');
		hashObj.combo.DHTML_TYPE="dhtmlXCombo";
		hashObj.combo.enableOptionAutoWidth(true);
		hashObj.combo.readonly(true,true);
		hashObj.combo.attachEvent("onCheck", function(value, state){
				if(hashObj.combo.getSelectedIndex()==-1 && state)
				{
					hashObj.combo.selIndex=true;
					hashObj.combo.selectOption(hashObj.combo.getIndexByValue(value),false,false);
				}
				comboCheck(hashObj);
				Debug(keys+":\""+hashObj.value+"\"");
				bindRptProperty(currentObj.id,keys,currentObj.obj);
				return true;
			});
		hashObj.combo.attachEvent("onSelectionChange", function(){
				var checked=hashObj.combo.getOption(hashObj.combo.getSelectedValue()).data()[2];
				if(!hashObj.combo.selIndex)//hashObj.combo.getSelectedIndex()!=hashObj.combo.selectedIndex && 
				{
					hashObj.combo.setChecked(hashObj.combo.getSelectedIndex(),!checked); 
					if(checked)
					{
						var checked_array = hashObj.combo.getChecked();
						if(checked_array.length)
					  	{
							hashObj.combo.selIndex=true;
							hashObj.combo.selectOption(hashObj.combo.getIndexByValue(checked_array[0]),false,false);
					  	}
					}
				}
				hashObj.combo.selIndex=false;
				comboCheck(hashObj);Debug(keys+":\""+hashObj.value+"\"");
				bindRptProperty(currentObj.id,keys,currentObj.obj);
				return true;
  			}); 
		if(key=="linkModelIds")
			bindLinkModuleList(hashObj);
		else 
			bindColNameList(key);
		break;
	case "duckColName"://reportModuleTab().table sel 未绑定数据    
		bindDuckColName();
		break;
	case "tabHeadBackColor":
		break;

	}
}
//刷新绑定数据列名称列表
function binDataColNameList()
{
	bindDuckColName();
	bindColNameList();
}
//绑定钻取字段选择列表
function bindDuckColName()
{
	var valId=$("table$duckColName_pro_value");
	var rptTab=currentObj.obj;
	var tabId=currentObj.id;
	var list=$(valId);
	list.length=0;
	list.options[list.length]=new Option("不钻取","");
	var cols=getDataColNames(rptTab.dataSrcName.value,0);	//	获取维度列
 	for(var i=0;i<cols.length;i++)
	{
 		if(!rptTab.table.columns.value[cols[i].name.value])continue;
 		list.options[list.length]=new Option(cols[i].nameCn.value,cols[i].name.value);
 		if(rptTab.table.duckColName.value==cols[i].name.value)
 			list.selectedIndex=list.length-1;
	}
 	if(!rptTab.table.columns.value[rptTab.table.duckColName.value])rptTab.table.duckColName.value="";

// 	var cols=getDataColNames(rptTab.dataSrcName.value,0);	//	按显示排序
//	buildTabColNames(rptTab);
//	var dimColMap={};
//	for(var i=0;i<cols.length;i++)dimColMap[cols[i].name.value]=cols[i];
// 	for(var i=0;i<rptTab.table.colNames.length;i++)
//	{
// 		var colName=rptTab.table.colNames[i];
// 		if(!dimColMap[colName])continue;
// 		list.options[list.length]=new Option(dimColMap[colName].nameCn.value,colName);
// 		if(rptTab.table.duckColName.value==colName)
// 			list.selectedIndex=list.length-1;
//	}
}
//绑定字段列表复选框 table. showColNames rowUniteCols
function bindColNameList(key)
{
	if(currentObj.type!='reportModuleTab' )return;
	var cols=[];
	var hashObj=null;
	var rptTab=currentObj.obj;
	if(key)
	{
//		if(key=="showColNames")
//		{
//			var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
//			if(!rptData)return;
//			cols=rptData.colAttrs.value;
//			hashObj=currentObj.obj.table.showColNames;
//		}
//		else 
		if(key=="rowUniteCols")
		{
			cols=getDataColNames(rptTab.dataSrcName.value,0);
			if(!cols)return;
			hashObj=currentObj.obj.table.rowUniteCols;
		}
		else if(key=="colFreeze")
		{
			cols=getDataColNames(rptTab.dataSrcName.value,0);
			if(!cols)return;
			hashObj=currentObj.obj.table.colFreeze;
		}
		hashObj.combo.clearAll();
		for(var i=0;i<cols.length;i++)
		{
			if(!rptTab.table.columns.value[cols[i].name.value])continue;
			hashObj.combo.addOption(cols[i].name.value,cols[i].nameCn.value);
		}
		for(var i=0;i<hashObj.value.length;i++)
		{
			try
			{
				if(rptTab.table.columns.value[hashObj.value[i]])//选中字段
					hashObj.combo.setChecked(hashObj.combo.getIndexByValue(hashObj.value[i]),true); 
			}
			catch(ex)
			{
				if(typeof ex=="string")
				{
					alert("设置["+key+"]选中状态错误："+ex);
				}
				else
				{
					var msg="";
					for(var a in ex)
						msg+=a+":"+ex[a]+"\n";
					alert("设置["+key+"]选中状态错误："+msg);
				}
			}
		}
		comboCheck(hashObj);
	}
	else
	{
		var rptData=reportConfig.dataCfms.value[rptTab.dataSrcName.value];
		if(!rptData)return;
//		hashObj=currentObj.obj.table.showColNames;
//		if(hashObj)
//		{
//			cols=rptData.colAttrs.value;
//			hashObj.combo.clearAll();
//			for(var i=0;i<cols.length;i++)
//			{
//				hashObj.combo.addOption(cols[i].name.value,cols[i].nameCn.value);
//			}
//			for(var i=0;i<hashObj.value.length;i++)
//			{
//				try
//				{
//					hashObj.combo.setChecked(hashObj.combo.getIndexByValue(hashObj.value[i]),true); 
//				}
//				catch(ex)
//				{
//					alert("设置联动模块选中状态错误："+ex);
//				}
//			}
//			comboCheck(hashObj);
//		}
		var keys=["rowUniteCols","colFreeze"];
		var colTypes=[1,1];
		for(var n=0;n<keys.length;n++)
		{
			cols=getDataColNames(rptTab.dataSrcName.value,0);
			if(!cols)continue;
			hashObj=currentObj.obj.table[keys[n]];
			hashObj.combo.clearAll();
			for(var i=0;i<cols.length;i++)
			{
				if(!rptTab.table.columns.value[cols[i].name.value])continue;
				hashObj.combo.addOption(cols[i].name.value,cols[i].nameCn.value);
			}
			for(var i=0;i<hashObj.value.length;i++)
			{
				try
				{
					if(rptTab.table.columns.value[hashObj.value[i]])//选中字段
						hashObj.combo.setChecked(hashObj.combo.getIndexByValue(hashObj.value[i]),true); 
				}
				catch(ex)
				{
					if(typeof ex=="string")
					{
						alert("设置["+keys[n]+"]选中状态错误："+ex);
					}
					else
					{
						var msg="";
						for(var a in ex)
							msg+=a+":"+ex[a]+"\n";
						alert("设置["+keys[n]+"]选中状态错误："+msg);
					}
				}
			}
			comboCheck(hashObj);
		}
	}
}
//组合框值设定
function comboCheck(hashObj)
{
//	var op=hashObj.combo.getOption(hashObj.combo.getSelectedValue()).select();
	
	var checked_array = hashObj.combo.getChecked();//alert(checked_array);
	var txt="";
	for(var i=0;i<checked_array.length;i++)
	{
		if(i)txt+=",";
		txt+=hashObj.combo.getOption(checked_array[i]).text;
	}
  	hashObj.combo.setComboText(txt);
	hashObj.value=checked_array;
	Debug(hashObj.label+":"+hashObj.value);
}
//更新模块列表
function linkModuleList()
{
	var list=[];
	for(var i=0;i<reportConfig.modules.value.length;i++)
	{
	 	list[i]={value:reportConfig.modules.value[i].modId,text:reportConfig.modules.value[i].moduleName.value };
	}
	for(var obj in rptObjects)
		if(rptObjects[obj].type=='reportModuleTab'  )	//	绑定所有页面模块列表下拉选择框
		{
			var hashObj=rptObjects[obj].linkModelIds;
			rptObjects[obj].linkModelIds.list=list;
			if(currentObj.id==obj)
				bindLinkModuleList(hashObj);
		}
}
//绑定联动模块列表
function bindLinkModuleList(hashObj)
{
	for(var i=0;i<hashObj.list.length;i++)
	{
		if(hashObj.list[i].value!=currentObj.obj.modId)
			hashObj.combo.addOption(hashObj.list[i].value,hashObj.list[i].text);
	}
	for(var i=0;i<hashObj.value.length;i++)
	{
		try
		{
			hashObj.combo.setChecked(hashObj.combo.getIndexByValue(hashObj.value[i]),true); 
		}
		catch(ex)
		{
			alert("设置联动模块选中状态错误："+ex);
		}
	}
	comboCheck(hashObj);
}
//	打开对象窗口或者切换选中对象
function openObjectWin(key)
{
	if(!key)
	{
		var el=event.srcElement;
		key=el.id.split("_")[0];
	}
	Debug(key);
	switch(key)
	{
	case "rptTitle":
		setSelectId(rptTitleConerId);
		break;
	case "termNames":
		break;
	case "modules":	// 模块列表，无意义
		break;
	case "dataCfms":// 数据源列表，已经列出名称，并单击打开
		break;
	case "termCfms":	// 条件列表，已经列出名称，并单击打开
		break;
	case "reportModuleTab":// 条件列表，已经列出名称，并单击打开
		break;
	}
}