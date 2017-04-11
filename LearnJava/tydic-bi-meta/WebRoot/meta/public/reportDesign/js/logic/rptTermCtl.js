//	条件控制入口，obj 父级容器  
function rptTermCtl(obj,rptTerm,name,oldVal)
{
	var tabId=termCfm.curTabId;
	if($(obj.id+'_termConer'))
	{
		obj=$(obj.id+'_termConer');
		var labelTd=$(tabId+"_term_priv_labelTd");
		labelTd.innerHTML=rptTerm.termName.value+"：";
	}
	else
	{
		obj.innerHTML="<div id='"+obj.id+"_termConer'  ida="+tabId+" type='term' style='cursor:move;width:200px;height:25px;padding-left:10px;background-color:#C3C7C6;' title='按住灰色条拖动条件栏'>"
			+"</div>";
		obj=$(obj.id+'_termConer');
		obj.innerHTML="<table cellpadding='0' cellspacing='1' border='0' style='cursor:auto;width:100%;height:100%' class='rptTermTable' ><tr><TD id='"+
				tabId+"_term_priv_labelTd' nowrap style='cursor:auto;width:80px;' class='rptTermLabelTd' >"+
				rptTerm.termName.value+"：</td><td class='dataSrcInputTd' style='width:80%' id='"+tabId+"_term_privTd' > </td></tr></table>";
	}
	rptTermBuildPriv(rptTerm,name,oldVal);
}
function rptTermDown()
{
	if(!event.srcElement || !event.srcElement.getAttribute)return;
	if(event.srcElement.getAttribute('type')!='term' )return;
	Debug('drag obj '+event.srcElement.id);
	var el=event.srcElement;
	property_params.term_dragging=true;
	property_params.Handle=el;//.cloneNode();
	property_params.outObj=el.parentNode;
	document.body.appendChild(property_params.Handle);
	Debug("out "+el.parentNode);
	document.onselectstart=function(){return false;};
	property_params.Handle.style.zIndex=1110;
	property_params.Handle.style.position="absolute";
	property_params.tx=window.event.clientX;
	property_params.ty=window.event.clientY;
	property_params.Handle.style.top=property_params.ty-1;
	property_params.Handle.style.left=property_params.tx-1;
//	property_params.Handle.style.backgroundColor="#FFFFFF";
//	property_params.outObj.removeChild(el);
//	document.body.appendChild(property_params.Handle);

}
function rptTermUp(onlyMove)
{
	if(!property_params.term_dragging)return;
	property_params.term_dragging=false;
	var inObj=property_params.inObj;
	var right=false;
	if(inObj && inObj.getAttribute("type")=='termPriv')
		property_params.inObj=inObj=$(inObj.getAttribute('ida'));
	if(inObj &&  inObj.getAttribute("type")=='termConter')//inObj.id!=property_params.outObj.id && 允许交换顺序
	{
		right=true;
	}
	if(right && inObj.getAttribute('ida') && inObj.getAttribute('ida')!=property_params.Handle.getAttribute('ida'))	//	不是同一个条件窗口
	{
		right=false;
	}
	if(isCustUser && right && event && event.srcElement)//	不允许拖出
	{
		if(inObj.id!=property_params.outObj.id)
		{
//			var winTabId=reportConfig.modules.value[0].window.win.getId()+"@tab1";
//			//var winTabId=rptWins.curModel.getId()+"@tab1";
//			var conPar=winTabId+"_termConter";
			right=false;
		}
	}
	if(right)
	{
		inObj.appendChild(property_params.Handle);
		if(!onlyMove)
		{
			Debug("插入对象 "+inObj.id);
			var tabId=inObj.id.replace('_termConter','');
			Debug("列表对象 "+currentObj.id);
			var termId=property_params.Handle.id.replace('_term_termConer','');
			//拖出删除
			var tabIdOut=property_params.outObj.id.replace('_termConter','');
			var modTab=rptObjects[tabIdOut];
			if(modTab)
			{ 
				for(var i=0;i<modTab.termNames.value.length;i++)
				{
					if(modTab.termNames.value[i].value==termId)
					{
						modTab.termNames.value[i]=null;
						modTab.termNames.value.remove(i);
						reportConfig.termCfms.value[termId].parent=null;
						break;
					}
				}
			}	
			if(rptObjects[tabId])	//	拖入添加
			{
				var modTab=rptObjects[tabId];
				var i=modTab.termNames.value.length;
				modTab.termNames.value[i]={};
				modTab.termNames.value[i].value=termId;
				modTab.termNames.value[i].termName=reportConfig.termCfms.value[termId].termName.value;
				reportConfig.termCfms.value[termId].parent=modTab.tabId?modTab.tabId:"reportConfig";
			}
			if((currentObj.id==tabId || currentObj.id==tabIdOut) && (currentObj.obj.type=="reportModuleTab"||currentObj.obj.type=="reportConfig"))	//更新数据源下拉列表
				idlist_change(true);			
		}
	}
	else
	{
		property_params.outObj.appendChild(property_params.Handle);
	}
	property_params.Handle.style.position="relative";
	property_params.Handle.style.styleFloat="left";
	property_params.Handle.style.cssFloat="left";
	property_params.Handle.style.top="0px";//property_params.ty;
	property_params.Handle.style.left="0px";//property_params.tx;
	document.onselectstart=function(){return true;};
}
//移动条件栏
function rptTermMove()
{
	if(!property_params.term_dragging)return;
	property_params.width=window.event.clientX-property_params.tx;
	property_params.height=window.event.clientY-property_params.ty;
	if(event.srcElement.getAttribute("type")=='termPriv' || event.srcElement.id.indexOf('_termConter')>0)
	{
		if(!property_params.inObj)
			property_params.inObj=event.srcElement;
		if(event.srcElement.id!=property_params.Handle.id)
			property_params.inObj=event.srcElement;
	}
	if(parInited)		//	不允许拖出条件栏
	{
//		property_params.inObj=property_params.outObj;
	}
//	Debug('drag in '+property_params.inObj.id);

	property_params.Handle.style.left=window.event.clientX-1;//property_params.width;
	property_params.Handle.style.top=window.event.clientY-1;
//	Debug('move in '+event.srcElement.id);

}
//生成条件预览框
function rptTermBuildPriv(rptTerm,name,oldVal)
{
	var tabId=rptTerm.tabId;
	var defaultObjTd=$(tabId+'_term_defaultValueTd');	//默认值选择框
	var privObjTd=$(tabId+"_term_privTd");
	if(name=="termType" || name=="srcType" || privObjTd.innerHTML.trim()=="")		//	生成预览框
	{
		if(!dhtmlx._isIE && rptTerm.myCalendar)rptTerm.myCalendar.unload();
		if(rptTerm.dhxTree)
		{
			rptTerm.dhxTree.trBx.style.display="none";
			var trBx=rptTerm.dhxTree.trBx;
			destructorDHMLX(rptTerm.dhxTree);
			rptTerm.dhxTree=null;
			if(trBx)document.body.removeChild(trBx);
		}
		defaultObjTd.innerHTML="";
		privObjTd.innerHTML="";
		//显示类型
		if(rptTerm.termType.value=="0")			//	下拉框
		{
			var dfHtml= '<select id='+tabId+'_term_defaultValue type=text maxlength=50 termId="'+tabId+'" title="'+rptTerm.defaultValue.explain+
				'" class=input150px name="defaultValue" onkeydown="checkMaxLengh(this)" onchange="termPrivValueChange(this)"></select>';
			var strHtml='<select id='+tabId+'_term_privObj termId="'+tabId+'" style="width:'+rptTerm.showLength.value+'px;" onchange="termPrivValueChange(this)"></select>';
	
			defaultObjTd.innerHTML=dfHtml;
			privObjTd.innerHTML=strHtml;
			dfHtml=null;
			strHtml=null;
		}
		else if(rptTerm.termType.value=="1")	//	下拉树
		{
			var dfHtml= '<input id='+tabId+'_term_defaultValue readonly dhxTree="tree" termId="'+tabId+'" style="width:200px;overflow:auto;" title="'+rptTerm.defaultValue.explain+
					'" class=input150px  name="defaultValue" onclick="termPrivTreeList(this)" onchange="termPrivValueChange(this)"/>';
			var strHtml='<input id='+tabId+'_term_privObj readonly dhxTree="tree" termId="'+tabId+'" style="width:'+rptTerm.showLength.value+'px;" onclick="termPrivTreeList(this)" />';
			defaultObjTd.innerHTML=dfHtml;
			privObjTd.innerHTML=strHtml;
			dfHtml=null;
			strHtml=null;
			if(!$(tabId+"_treeBox"))
			{
				Debug("创建下拉树");
				var trBx=document.createElement("DIV");
				trBx.id=tabId+"__treeBox";
				document.body.appendChild(trBx);
				trBx.style.width="200px";
				trBx.style.height="250px";
				trBx.style.overflow="auto";
				trBx.style.zIndex="99999";
				trBx.style.position="absolute";
				trBx.style.display="none";
				trBx.style.backgroundColor="#E8F2FE";
				trBx.style.border="1px solid #1B6288";
				rptTerm.dhxTree = new dhtmlXTreeObject(tabId+"__treeBox","100%","100%",0);
				rptTerm.dhxTree.setImagePath(imageRoot+"images/treeIcons/imgs/");
				rptTerm.dhxTree.setSkin('dhx_skyblue');
				rptTerm.dhxTree.trBx=trBx;
				rptTerm.dhxTree.DHTML_TYPE="dhtmlXTree";
				trBx.onmouseout=function(){trBx.style.display='none';};
				trBx.onmouseover=function(){trBx.style.display='';};
				rptTerm.dhxTree.bindObj=$(tabId+'_term_defaultValue');
			}
		}
		else if(rptTerm.termType.value=="2")	//	时间框
		{
			var dfHtml= '<input id='+tabId+'_term_defaultValue readonly termId="'+tabId+'" type=text maxlength=50 title="'+rptTerm.defaultValue.explain+'" class=input150px value="'+rptTerm.defaultValue.value+
							'" name="defaultValue" onkeydown="checkMaxLengh(this)" />';
			var strHtml='<input id='+tabId+'_term_privObj  termId="'+tabId+'" style="width:'+rptTerm.showLength.value+'px;" />';
			defaultObjTd.innerHTML=dfHtml;
			privObjTd.innerHTML=strHtml;
			rptTerm.myCalendar=new dhtmlXCalendarObject([tabId+'_term_privObj', tabId+'_term_defaultValue']);
			rptTerm.myCalendar.unload=myCalendarDestructor;
			rptTerm.myCalendar.loadUserLanguage('zh');
			rptTerm.myCalendar.hideTime();
			rptTerm.myCalendar.DHTML_TYPE="dhtmlXCalendar";
			rptTerm.myCalendar.setDateFormat("%Y-%m-%d");
			rptTerm.myCalendar.attachEvent("onClick",function(date){
			    termPrivValueChange($(tabId+'_term_privObj'));
			    return true;
			});
//			alert($(tabId+'_term_privObj')._dhtmlxcalendar_uid +"___"+ $(tabId+'_term_privObj').getAttribute("_dhtmlxcalendar_uid"));
//			$(tabId+'_term_privObj')._dhtmlxcalendar_uid=$(tabId+'_term_privObj').getAttribute("_dhtmlxcalendar_uid");
//			$(tabId+'_term_defaultValue')._dhtmlxcalendar_uid=$(tabId+'_term_privObj').getAttribute("_dhtmlxcalendar_uid");
			dfHtml=null;
			strHtml=null;
		}
	}
	rptTermBuildPrivAttr(rptTerm,name,oldVal);	//	应用其它属性
}
//	格式化数字日期
function formatNumDate(val)
{
	try
	{
		var y=parseInt(val/10000);
		var m=parseInt(val/100 %100);
		var d=parseInt(val%100);
	 	var D=new Date(y, m-1,d);
		if(D.getFullYear()==y && D.getMonth()+1==m && D.getDate()==d)
			return y+"-"+m+"-"+d;
		else return null;
	}
	catch(ex)
	{
		return null;
	}	
}
//	对预览框应用其它属性
function rptTermBuildPrivAttr(rptTerm,name,oldVal)
{
	if(!name && !rptTerm.privInited)
	{
		name="data";
		oldVal=rptTerm.data.value;
	}
	var tabId=rptTerm.tabId;
	switch(name)
	{
	case "showLength":
		$(tabId+'_term_privObj').style.width=rptTerm.showLength.value;
		break;
	case "parTermName":
//	case "srcType":
	case "dataSrcId":
	case "data":
	case "appendValue":
		rptTerm.ready=false;
		if(!waitTerm(rptTerm,0))
		{
			runInterval.add(rptTermBuildPrivAttr,rptTerm,0,[rptTerm,name,oldVal]);
			return;
		}
		if(rptTerm.termType.value=="0")//下拉框
		{
			var destData=null;
			var defValList=$(tabId+'_term_defaultValue');
			var privList=$(tabId+'_term_privObj');
			defValList.length=0;
			privList.length=0;
			if(rptTerm.srcType.value=="1")	// 	SQL查询数据  //	需要宏变量处理 替换依赖父级条件变量
			{
				var dataSrcId=rptTerm.dataSrcId.value;
				var sql=rptTerm.data.value;
				var params=getMacroParams(1,rptTerm);
				if(sql.trim()=="" || sql.length<18)
				{
					alert("查询SQL长度不足");
					return;
				}
				if(rptTerm.appendValue.value || rptTerm.appendText.value)
				{	
					defValList.options.add(new Option(rptTerm.appendText.value,rptTerm.appendValue.value), 0);
					defValList.selectedIndex=0;
					privList.options.add(new Option(rptTerm.appendText.value,rptTerm.appendValue.value), 0);
					privList.selectedIndex=0;
				}
				Debug(sql);
				var termAttrs=getTermAttrsMap(rptTerm,params);//条件属性,到服务端二次判断权限
				ReportDesignerAction.qryDimTermData(dataSrcId,sql,params,0,termAttrs,function(res){
					clearVarVal(termAttrs);
					if(!res || res[0]=="false")
					{
						alert("错误："+(res?res[1]:res));
						$(tabId+'_term_data').focus();
						rptTerm.data.value=oldVal;
						return;
					}
					if($(tabId+'_term_valueColName').value.trim()=="")
					{
						if(checkMacro("valueColName",res[0][0]))
						{
							$(tabId+'_term_valueColName').value=res[0][0];
							rptTerm.valueColName.value=res[0][0];
						}
						else
						{
							$(tabId+'_term_valueColName').value=rptTerm.tabId+"_"+res[0][0];
							rptTerm.valueColName.value=rptTerm.tabId+"_"+res[0][0];
						}
					}
					if($(tabId+'_term_textColName').value.trim()=="")
					{
						if(checkMacro("valueColName",res[0][1]))
						{
							$(tabId+'_term_textColName').value=res[0][1];
							rptTerm.textColName.value=res[0][1];
						}
						else
						{
							$(tabId+'_term_textColName').value=rptTerm.tabId+"_"+res[0][1];
							rptTerm.textColName.value=rptTerm.tabId+"_"+res[0][1];
						}
					}
					bindListTermPrivData(rptTerm,res[1]);	//	绑定下拉框预览数据
				});
			}
			else
			{
				destData=rptTerm.data.value;	//	需要拆分
				destData=destData.split("\n");
				for(var s=0;s<destData.length;s++)
				{
					destData[s]=destData[s].split(",");
					if(destData[s].length!=2)
					{
						alert("数据列表格式配置错误，请参考提示。");
						$(tabId+'_term_data').focus();
						rptTerm.data.value=oldVal;
						return;
					}
				}
				bindListTermPrivData(rptTerm,destData);	//	绑定下拉框预览数据
			}
		}
		else if(rptTerm.termType.value=="1")//下拉树
		{
			//需要根据维度设置过滤无效数据和父级节点选中事件
			var destData=null;
			if(rptTerm.srcType.value=="1")	// 	SQL查询数据  //	需要宏变量处理 替换依赖父级条件变量
			{
				var dataSrcId=rptTerm.dataSrcId.value;
				var sql=rptTerm.data.value;
				var params=getMacroParams(1,rptTerm);
				rptTerm.dhxTree.inited=false;
				if(sql.split(";").length==2)
				{
					rptTerm.dhxTree.dyType=1;
					sql=sql.split(";")[0];
				}
				else
					delete rptTerm.dhxTree.dyType;
				rptTerm.dhxTree.deleteChildItems(0);
				if(sql.trim()=="" || sql.length<18)
				{
					alert("查询SQL长度不足");
					return;
				}
				Debug(sql);
				var termAttrs=getTermAttrsMap(rptTerm,params);//条件属性,到服务端二次判断权限
				ReportDesignerAction.qryDimTermData(dataSrcId,sql,params,0,termAttrs,function(res){
					clearVarVal(termAttrs);
					if(!res || res[0]=="false")
					{
						alert("错误："+(res?res[1]:res));
						$(tabId+'_term_data').focus();
						rptTerm.data.value=oldVal;
						return;
					}
					if($(tabId+'_term_valueColName').value.trim()=="")
					{
						if(checkMacro("valueColName",res[0][0]))
						{
							$(tabId+'_term_valueColName').value=res[0][0];
							rptTerm.valueColName.value=res[0][0];
						}
						else
						{
							$(tabId+'_term_valueColName').value=rptTerm.tabId+"_"+res[0][0];
							rptTerm.valueColName.value=rptTerm.tabId+"_"+res[0][0];
						}
					}
					if($(tabId+'_term_textColName').value.trim()=="")
					{
						if(checkMacro("valueColName",res[0][1]))
						{
							$(tabId+'_term_textColName').value=res[0][1];
							rptTerm.textColName.value=res[0][1];
						}
						else
						{
							$(tabId+'_term_textColName').value=rptTerm.tabId+"_"+res[0][1];
							rptTerm.textColName.value=rptTerm.tabId+"_"+res[0][1];
						}
					}
					bindTreeTermPrivData(rptTerm,res[1]);	//	绑定下拉框预览数据
				});
			}
			else
			{
				destData=rptTerm.data.value;	//	需要拆分
				destData=destData.split("\n");
				for(var s=0;s<destData.length;s++)
				{
					destData[s]=destData[s].split(",");
					if(destData[s].length!=3)
					{
						alert("数据列表格式配置错误，请参考提示。");
						$(tabId+'_term_data').focus();
						rptTerm.data.value=oldVal;
						return;
					}
				}
				bindTreeTermPrivData(rptTerm,destData);	//	绑定下拉框预览数据
			}
		}
		else if(rptTerm.termType.value=="2")	//	日期 
		{
			$(tabId+'_term_defaultValue').value=rptTerm.defaultValue.value[0];
			$(tabId+'_term_privObj').value=rptTerm.defaultValue.value[0];
			rptTerm.myCalendar.setDate(rptTerm.defaultValue.value[0]);
			var val=rptTerm.data.value;
			rptTerm.ready=true;
			if(val.trim()=="")return;
			var params=getMacroParams();
			for(var param in params)
			{
				val=val.toUpperCase().replace("{"+param.toUpperCase()+"}",params[param]);
			}
			//判断 是否使用SQL查询有效区间 select
			if(val.substr(0,6).toLowerCase()=="select")
			{
				val[0]=0;
				val[1]=0;
			}
			else
				val=val.split("-");
			//判断父级条件的值
			if(val.length==1)
			{
				val[0]=parseInt(val[0]);
				val[0]=formatNumDate(val[0]);
				val[1]=0;
			}
			else
			{
				val[0]=parseInt(val[0]);
				val[1]=parseInt(val[1]);
				if((val[0] && val[1]) && val[0]>val[1])
				{
					alert("时间有效性配置错误,开始值必需小于结束值");
					$(tabId+'_term_data').focus();
					rptTerm.data.value=oldVal;
				}
				val[0]=formatNumDate(val[0]);
				val[1]=formatNumDate(val[1]);
			}
			rptTerm.myCalendar.setSensitiveRange(val[0],val[1]);
		}
		break;
	}
}
//	绑定条件 下拉框预览数据
function bindListTermPrivData(rptTerm,destData)
{
	var tabId=rptTerm.tabId;
	var defValList=$(tabId+'_term_defaultValue');
	var privList=$(tabId+'_term_privObj');
//	defValList.length=0;
//	privList.length=0;
	for(var i=0;i<destData.length;i++)
	{
		defValList.options[defValList.length]=new Option(destData[i][1],destData[i][0]);
		privList.options[privList.length]=new Option(destData[i][1],destData[i][0]);
		if(privList.length-1==rptTerm.defaultValue.value[0] || 
				(rptTerm.defaultValue.value[0]==-1 && rptTerm.defaultValue.value[1]==destData[i][0]))
		{
			privList.selectedIndex=privList.length-1;
			defValList.selectedIndex=defValList.length-1;
		}
	}
//	if(!rptTerm.defaultValue.value[0] && !rptTerm.defaultValue.value[1] && defValList.length)
//	{
//		defValList.selectedIndex=0;
//		rptTerm.defaultValue.value[0]=defValList.value;
//		rptTerm.defaultValue.value[1]=defValList.options[defValList.selectedIndex].text;
//	}
//	if(rptTerm.appendValue.value || rptTerm.appendText.value)
//	{	
//		defValList.options.add(new Option(rptTerm.appendText.value,rptTerm.appendValue.value) , 0);
//		defValList.selectedIndex=0;
//		privList.options.add(new Option(rptTerm.appendText.value,rptTerm.appendValue.value) , 0);
//		privList.selectedIndex=0;
//	}
	rptTerm.ready=true;
	clearVarVal(destData);
	destData=null;
}
//	更新条件 下拉框预览数据
function updateListTermPrivData(rptTerm,name,oldVal)
{
	var tabId=rptTerm.tabId;
	var defValList=$(tabId+'_term_defaultValue');
	var privList=$(tabId+'_term_privObj');
	for(var i=0;i<defValList.length;i++)
	{
		if((defValList.options[i].value==rptTerm.appendValue.value) || (name=="appendValue" && defValList.options[i].value==oldVal))
		{
			defValList.options[i].text=rptTerm.appendText.value;
			defValList.options[i].value=rptTerm.appendValue.value;
			privList.options[i].text=rptTerm.appendText.value;
			privList.options[i].value=rptTerm.appendValue.value;
			termPrivValueChange(privList);
			return;
		}
	}
	if(rptTerm.appendValue.value || rptTerm.appendText.value)
	{
		defValList.options.add(new Option(rptTerm.appendText.value,rptTerm.appendValue.value) , 0);
		privList.options.add(new Option(rptTerm.appendText.value,rptTerm.appendValue.value) , 0);
		defValList.selectedIndex=0;privList.selectedIndex=0;
		termPrivValueChange(privList);
	}
}
//	绑定条件 下拉树预览数据
function bindTreeTermPrivData(rptTerm,destData)
{
	if(destData.length==0)return;
//	rptTerm.dhxTree.destructor();
	var termId=rptTerm.tabId;
	rptTerm.dhxTree.termId=termId;
	if(rptTerm.dhxTree.dyType)
	{
		var selId=rptTerm.dhxTree.getSelectedItemId();
		if(selId)selId=selId.split("_")[2];
		var lastNodeId="";
		for(var i=0;i<destData.length;i++)	//	value ,text ,parent
		{
			var parentId=0;
			if(selId)parentId=termId+"_treeNode_"+selId;
			var itemId=termId+"_treeNode_"+destData[i][0];
			var itemText=destData[i][1];
//			rptTerm.dhxTree.insertNewChild(parentId,itemId,itemText,0,"folderClosed.gif","folderOpen.gif","folderClosed.gif","",1);
			rptTerm.dhxTree.insertNewChild(parentId,itemId,itemText,0,0,0,0,"",1);
			if(destData[i][2])//DIM_LEVEL
			{
				rptTerm.dhxTree.setUserData(itemId,"DIM_LEVEL",""+destData[i][2]);
				if(rptTerm.dimDataLevels.dimMaxLevel>destData[i][2])
					rptTerm.dhxTree.insertNewChild(itemId,itemId+"sub",itemText,0,0,0,0,"",1);
			}
			else
			{
				rptTerm.dhxTree.insertNewChild(itemId,itemId+"sub",itemText,0,0,0,0,"",1);
			}
			if(destData[i][0]==rptTerm.defaultValue.value[0])
			{
				rptTerm.dhxTree.bindObj.value=itemText;
				rptTerm.dhxTree.bindObj.setAttribute("code",destData[i][0]);
				if(destData[i][2])rptTerm.dhxTree.bindObj.setAttribute("DIM_LEVEL",destData[i][2]);
				$(termId+"_term_privObj").value=destData[i][1];
				$(termId+"_term_privObj").setAttribute("code",destData[i][0]);
				if(destData[i][2])$(termId+"_term_privObj").setAttribute("DIM_LEVEL",destData[i][2]);
				rptTerm.dhxTree.selectDefaultVal=true;
			}
			else if(!rptTerm.defaultValue.value[0] && !rptTerm.defaultValue.value[1])
			{
				rptTerm.dhxTree.bindObj=$(termId+"_term_defaultValue");
				rptTerm.dhxTree.selectItem(itemId,true,false);
				var right=termTreeListSelect(itemId);//回写默认值 JS 
				if(right)
				{
					$(termId+"_term_privObj").value=destData[i][1];
					$(termId+"_term_privObj").setAttribute("code",destData[i][0]);
					rptTerm.defaultValue.value[0]=destData[i][0];
					rptTerm.defaultValue.value[1]=destData[i][1];
					if(destData[i][2])
					{
						$(termId+"_term_privObj").setAttribute("DIM_LEVEL",destData[i][2]);
						rptTerm.defaultValue.value[2]=destData[i][2];
					}
					rptTerm.dhxTree.selectDefaultVal=true;
				}
			}
			rptTerm.dhxTree.closeItem(itemId);
		}
	}
	else
	{
		for(var i=0;i<destData.length;i++)	//	value ,text ,parent
		{
			var parentId=termId+"_treeNode_"+destData[i][2];
			if(destData[i][2]=="0" || !destData[i][2])parentId=0;
			var itemId=termId+"_treeNode_"+destData[i][0];
			var itemText=destData[i][1];
			rptTerm.dhxTree.insertNewChild(parentId,itemId,itemText,0,0,0,0,"",1);
			if(destData[i][3])rptTerm.dhxTree.setUserData(itemId,"DIM_LEVEL",""+destData[i][3]);
			if(destData[i][0]==rptTerm.defaultValue.value[0])
			{
				rptTerm.dhxTree.bindObj.value=itemText;
				rptTerm.dhxTree.bindObj.setAttribute("code",destData[i][0]);
				if(destData[i][3])rptTerm.dhxTree.bindObj.setAttribute("DIM_LEVEL",destData[i][3]);
				
				$(termId+"_term_privObj").value=destData[i][1];
				$(termId+"_term_privObj").setAttribute("code",destData[i][0]);
				if(destData[i][3])$(termId+"_term_privObj").setAttribute("DIM_LEVEL",destData[i][3]);
				rptTerm.dhxTree.selectDefaultVal=true;
			}
			else if(!rptTerm.defaultValue.value[0] && !rptTerm.defaultValue.value[1])
			{
				rptTerm.dhxTree.bindObj=$(termId+"_term_defaultValue");
				rptTerm.dhxTree.selectItem(itemId,true,false);
				var right=termTreeListSelect(itemId);
				if(right)
				{
					$(termId+"_term_privObj").value=destData[i][1];
					$(termId+"_term_privObj").setAttribute("code",destData[i][0]);
					if(destData[i][3])
					{
					 	$(termId+"_term_privObj").setAttribute("DIM_LEVEL",destData[i][3]);
						rptTerm.defaultValue.value[2]=destData[i][3];
					}
					rptTerm.dhxTree.selectDefaultVal=true;
				}
			}
		}
	}
	selectDefaultTreeListVal(rptTerm);
	if(!rptTerm.dhxTree.inited)
	{
		if(rptTerm.dhxTree.dyType)
		{
			rptTerm.dhxTree.attachEvent("onDblClick", termTreeListFlashDbl);
			rptTerm.dhxTree.attachEvent("onSelect",termTreeListFlashSel);
			rptTerm.dhxTree.attachEvent("onClick", termTreeListFlashClk);
			rptTerm.dhxTree.attachEvent("onOpenStart", termTreeListFlash);
		}
		else
		{
			rptTerm.dhxTree.attachEvent("onSelect",termTreeListOpenItem);
			rptTerm.dhxTree.attachEvent("onClick", termTreeListOpenItem);
			rptTerm.dhxTree.attachEvent("onDblClick", termTreeListSelect);
		}
		rptTerm.dhxTree.inited=true;
		rptTerm.dhxTree.closeAllItems(0);
		rptTerm.dhxTree.openItem(termId+"_treeNode_"+destData[0][0]);
	}
	clearVarVal(destData);
	destData=null;
	selectDefaultTreeListVal(rptTerm);//选择默认值
	rptTerm.ready=true;
}
//递归选择默认值
function selectDefaultTreeListVal(rptTerm)
{
	if(rptTerm.dhxTree.selectDefaultVal)return;
}
function termTreeListOpenItem(itemId)
{
	var ids=itemId.split("_");
	var termId=ids[0];
	var rptTerm=reportConfig.termCfms.value[termId];
	rptTerm.dhxTree.openItem(itemId);
	return true;
}
//	条件 下拉树 异步刷新事件
function termTreeListFlashClk(id,id2)
{
	return termTreeListFlash(id,"onClick");
}
function termTreeListFlashSel(id)
{
	return termTreeListFlash(id,"onSelect");
}
function termTreeListFlashDbl(id)
{
	return termTreeListFlash(id,"onDblClick");
}
//	条件 下拉树 异步刷新
function termTreeListFlash(id,type)
{
	Debug("type="+type+" id="+id);
	var ids=id.split("_");
	var termId=ids[0];
	var rptTerm=reportConfig.termCfms.value[termId];
	var code=ids[2];
	var DIM_LEVEL=rptTerm.dhxTree.getUserData(id,"DIM_LEVEL");
	if(rptTerm.dhxTree.getUserData(id,"refresh") || rptTerm.dimDataLevels.dimMaxLevel==DIM_LEVEL)
	{
		if(type=="onDblClick")
			termTreeListSelect(id);
		return true;
	}
	rptTerm.dhxTree.setUserData(id,"refresh","true");
	rptTerm.dhxTree.deleteItem(id+"sub",true);
//	rptTerm.dhxTree.setItemImage2(id, "leaf.gif","folderOpen.gif","folderClosed.gif");
	var dataSrcId=rptTerm.dataSrcId.value;
	var sql=rptTerm.data.value;
	var params=getMacroParams(1,rptTerm);
	params[rptTerm.valueColName.value]=code;
	params[rptTerm.textColName.value]=rptTerm.dhxTree.getItemText(id);
	sql=sql.split(";")[1];
	if(sql.trim()=="" || sql.length<18)
	{
		alert("查询SQL长度不足");
		return;
	}
	Debug(sql);
	var termAttrs=getTermAttrsMap(rptTerm,params);//条件属性,到服务端二次判断权限
	ReportDesignerAction.qryDimTermData(dataSrcId,sql,params,0,termAttrs,function(res){
		clearVarVal(termAttrs);
		if(!res || res[0]=="false")
		{
			alert("刷新加载错误："+(res?res[1]:res));
			$(tabId+'_term_data').focus();
			return;
		}
		bindTreeTermPrivData(rptTerm,res[1]);	//	绑定下拉框预览数据
	});
}
//	条件 下拉树 值选择
function termTreeListSelect(id)
{
	var ids=id.split("_");
	var termId=ids[0];
	var rptTerm=reportConfig.termCfms.value[termId];
	var code=ids[2];
	var val=rptTerm.dhxTree.getSelectedItemText();
	var DIM_LEVEL=rptTerm.dhxTree.getUserData(id,"DIM_LEVEL");
	//判断模型是否支持些层级    编码是否有权限选择 
	var haveRight=false;
	if(rptTerm.dimDataLevels)
	{
		for(var i=0;i<rptTerm.dimDataLevels.value.length;i++)
		{
			if(rptTerm.dimDataLevels.value[i]==DIM_LEVEL)
			{
				haveRight=true;
				break;
			}
		}
	}
	else
	{
		haveRight=true;
	}
	if(DIM_LEVEL && !haveRight)return false;
	rptTerm.dhxTree.bindObj.value=val;
	rptTerm.dhxTree.bindObj.setAttribute("code",code);
	rptTerm.dhxTree.bindObj.setAttribute("DIM_LEVEL",DIM_LEVEL);
	rptTerm.dhxTree.trBx.style.display="none";
	termPrivValueChange(rptTerm.dhxTree.bindObj);
	return true;
}
//	显示下拉树列表
function termPrivTreeList(obj)
{
	if(obj.srcElement)obj=obj.srcElement;
	var termId=obj.getAttribute("termId");
	var rptTerm=reportConfig.termCfms.value[termId];
	var trBx=rptTerm.dhxTree.trBx;
//	if(rptTerm.dhxTree.bindObj==obj)
//	{
//		if(trBx.style.display=="")
//			trBx.style.display="none";
//		else
//			trBx.style.display="";
//		return ;
//	}
	
	var pos=getie(obj);
	trBx.style.top=(pos[0]+20)+"px";
	trBx.style.left=(pos[1]+10)+"px";
	rptTerm.dhxTree.bindObj=obj;
	//rptTerm.dhxTree termId+"_treeNode_"+destData[i][0];
	if(obj.getAttribute("code"))
	{
		var itemId=termId+"_treeNode_"+obj.getAttribute("code");
		rptTerm.dhxTree.selectItem(itemId,false,false);
	}
	trBx.style.display="";
//	setdiv(trBx.id);
}
//	预览值改变跟踪事件
function termPrivValueChange(obj)
{
	if(obj.srcElement)obj=obj.srcElement;
	var termId=obj.getAttribute("termId");
	var rptTerm=reportConfig.termCfms.value[termId];
	var name=obj.getAttribute("name");
	if(name)	//	默认值
	{
		rptTerm[name].value=[];
		if(rptTerm.termType.value=="0")
		{
			rptTerm[name].value[0]=obj.selectedIndex;
			rptTerm[name].value[1]=obj.value;
			rptTerm[name].value[2]=obj.options[obj.selectedIndex].text;
		}
		else if(rptTerm.termType.value=="1")
		{
			rptTerm[name].value[0]=$(termId+'_term_privObj').getAttribute("code");
			rptTerm[name].value[1]=obj.value;
			rptTerm[name].value[2]=$(termId+'_term_privObj').getAttribute("DIM_LEVEL");
		}
		else if(rptTerm.termType.value=="2")
			rptTerm[name].value[0]=obj.value.replace(/-/g,"");
	}
	else	//	预览框 //$(tabId+'_term_privObj').setAttribute("code",code);
	{
		//判断是否有依赖此条件的条件存在，存在则刷新
		termPrivFlashCheck(termId);
		//rptTermBuildPriv(rptTerm,"data",rptTerm.data.value);//强制刷新
		
	}
}
//	预览值改变依赖联动
function termPrivFlashCheck(termId)
{
	var have=false;
	var rptTerm=reportConfig.termCfms.value[termId];
	for(var tabId in reportConfig.termCfms.value)
	{
		if(termId==tabId)continue;
		var rptTerm2=reportConfig.termCfms.value[tabId];
		if(rptTerm2.parTermName.value!=termId)continue;
		rptTermBuildPrivAttr(rptTerm2,"data",rptTerm2.data.value);//强制刷新
		termPrivFlashCheck(tabId);
		if(reportConfig.termCfms.value[tabId].parent==rptTerm.parent)
			have=true;
	}
	if(have)return;
	if(rptTerm.parent=="reportConfig")
	{
		//刷新所有页面
		for(var tabId in rptObjects)
			if(rptObjects[tabId].type=='reportModuleTab')
		{
			try
			{
				if(rptObjects[tabId].rptData)
					readTabData(tabId,true);
			}
			catch(ex)
			{
				alert("刷新模块页面["+tabId+"]失败："+ex);
			}
		}
	}
	else if(rptTerm.parent)
	{
		try
		{
			if(rptObjects[rptTerm.parent] && rptObjects[rptTerm.parent].rptData)
				readTabData(rptTerm.parent,true);
		}
		catch(ex)
		{
			alert("刷新模块页面["+tabId+"]失败："+ex);
		}
	}
	
//	
//	if(have==false)	//	判断引用模块页面，进行页面自动刷新
//	{
//		for(var obj in rptObjects)
//		{
//			if(rptObjects[obj].type=='reportModuleTab' || rptObjects[obj].type=='reportConfig')	//	判断是否使用了条件
//			{
//				for(var i=0;i<rptObjects[obj].termNames.value.length;i++)
//					if(rptObjects[obj].termNames.value[i].value==termId)
//				{
//					have=true;
//					break;
//				}
//			}
//			if(have==false)continue;
//			if(rptObjects[obj].type=='reportModuleTab')//刷新模块页面
//			{
//				try
//				{
//					if(rptObjects[obj].rptData)	//	有数据对象才刷新
//						changeRptModelTab(obj,["dataSrcName"],rptObjects[obj]);
//				}
//				catch(ex)
//				{
//					alert("刷新模块页面["+obj+"]失败："+ex);
//				}
//			}
//			else
//			{
//				//刷新所有页面
//				for(var tabId in rptObjects)
//					if(rptObjects[tabId].type=='reportModuleTab')
//				{
//					try
//					{
//						if(rptObjects[tabId].rptData)
//							changeRptModelTab(tabId,["dataSrcName"],rptObjects[tabId]);
//					}
//					catch(ex)
//					{
//						alert("刷新模块页面["+tabId+"]失败："+ex);
//					}
//				}
//			}
//		}
//	}
}

//myCalendar = new dhtmlXCalendarObject(["calendar", "calendar2", "calendar3"]);
