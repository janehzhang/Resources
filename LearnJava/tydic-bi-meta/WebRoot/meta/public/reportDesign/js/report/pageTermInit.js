var autoTermFreshTab=false;

function buildPageTerm(termConer,obj)
{
	var rowSize=parseInt(obj.termRowSize.value);
//	rowSize=rowSize?rowSize:-1;
	var downLoad=(obj.downLoadFlag && obj.downLoadFlag.value);
	var termLen=obj.termNames.value.length;
	if(obj.termNames.value.lenght==0 && (!obj.downLoadFlag || !obj.downLoadFlag.value))
	{
		termConer.style.display="none";
		return;
	}
	var termCfms=[];
	for(var i = 0; i < termLen; i++)
		termCfms[i]=reportConfig.termCfms.value[obj.termNames.value[i].value];
	var strHTML="<table width='100%' height=100% border='0' id='rptTermTable_" + termId+ "' bgcolor=#000 class=dataSrcTable  cellpadding='0' cellspacing='0'>";
	var termRowInit=false;
	for(var i = 0; i < termLen; i++)
	{
		var term=termCfms[i];
		var termId=term.tabId;
		if (rowSize && (i % rowSize) == 0)//termPrivValueChange(this)
			strHTML+=("<tr class='rptTermTr'>");
		else if(!termRowInit)termRowInit=true,strHTML+=("<tr class='rptTermTr'>");
		strHTML+=("<td width='10%' height=22px align='right'  nowrap id='"+termId+"_term_labelTd'>" + term.termName.value
				+ "：</td><td width='15%' style='padding-left:5px;' nowrap id='"+termId+"_term_privTd'></td>");
	}
	if (termLen || downLoad )
	{
		var searchHtml="";var tabId="";
		if(obj.type=="reportModuleTab")
			tabId=obj.tabId;
		searchHtml="<input value='查 询' type='button' class='btbg' onClick='refreshTabData(\""+tabId+"\");return false;'>";	//刷新Tab页
		var downHtml="<input name='downLoad_bt' onClick='exportExcel(\""+tabId+"\")' type='button' class='btbgb' value='导出Excel'/>";
		if (termLen)
		{
			if (rowSize == termLen)
			{
				strHTML+=("</tr><tr class='rptTermTr'><td colspan=" + (rowSize * 2 - 1) + " algin=right " +
						"style='padding-right:15px;padding-left:15px;text-align:"+(obj.tabId?"right":"left")+";'>"+searchHtml+" </td>");
				if (downLoad )
					strHTML+=("<td width='10%' align='right'  style='padding-right:15px;padding-left:15px;text-align: right;'>"+downHtml+ "</td>");
				else
					strHTML+=("<td width='10%'>&nbsp;</td>");
			}
			else
			{
				for (var c = termLen % rowSize; c < rowSize - 1; c++)
					strHTML+=("<td width='10%' >&nbsp;</td><td width='15%' >&nbsp;</td>");
				strHTML+=("<td   algin=right style='padding-right:15px;padding-left:15px;text-align: "+(obj.tabId?"right":"left")+";'>"+searchHtml+" </td>");
				if (downLoad )
					strHTML+=("<td width='10%' align='right'  style='padding-right:15px;padding-left:15px;text-align: right;'>"+downHtml+ " </td>");
				else
					strHTML+=("<td width='10%'>&nbsp;</td>");
			}
			strHTML+=("</tr>"); 

		}
		else
		{
			strHTML+=("<tr class='rptTermTr'><td width='100%' align='right' style='padding-right:15px;padding-left:15px;'>"+downHtml+ " </td></tr>");
		}
	}
	strHTML+="</table>";
	termConer.innerHTML=strHTML;
	strHTML="";
	for(var i = 0; i < termLen; i++)
		rptTermBuildPriv(termCfms[i]);
	termCfms=null;
}
//生成条件预览框
function rptTermBuildPriv(rptTerm)
{
	var tabId=rptTerm.tabId;
	var privObjTd=$(tabId+"_term_privTd");
	privObjTd.innerHTML="";
	var autoFreshTab="";
	if(autoTermFreshTab)autoFreshTab=",true";// && rptTerm.parent=="reportConfig"
	//显示类型
	if(rptTerm.termType.value=="0")			//	下拉框
	{
 		var strHtml='<select id='+tabId+'_term_privObj termId="'+tabId+'" style="width:'+rptTerm.showLength.value
 			+'px;" onchange="termPrivValueChange(this'+autoFreshTab+')"></select>';
		privObjTd.innerHTML=strHtml;
		strHtml=null;
	}
	else if(rptTerm.termType.value=="1")	//	下拉树
	{
		var strHtml='<input id='+tabId+'_term_privObj readonly termId="'+tabId+'" style="width:'+rptTerm.showLength.value
				+'px;" onclick="termPrivTreeList(this'+autoFreshTab+')" />';
		privObjTd.innerHTML=strHtml;
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
			rptTerm.dhxTree.bindObj=$(tabId+'_term_privObj');
		}
	}
	else if(rptTerm.termType.value=="2")	//	时间框
	{
		var strHtml='<input id='+tabId+'_term_privObj  readonly termId="'+tabId+'" style="width:'+rptTerm.showLength.value
			+'px;" onchange="termPrivValueChange(this'+autoFreshTab+')"/>';
		privObjTd.innerHTML=strHtml;
		rptTerm.myCalendar=null;
		rptTerm.myCalendar=new dhtmlXCalendarObject([tabId+'_term_privObj']);
		rptTerm.myCalendar.unload=function (){};
        rptTerm.myCalendar.loadUserLanguage('zh');
		rptTerm.myCalendar.setDateFormat("%Y-%m-%d");
		rptTerm.myCalendar.DHTML_TYPE="dhtmlXCalendar";
		strHtml=null;
	}else if(rptTerm.termType.value=="5") {
		var strHtml = "";
		if(rptTerm.data.value) {
			var values = rptTerm.data.value.split(";");
			var optionsHtml = "";
			for(var i=0;i<values.length;i++) {
				var tmps = values[i].split(":");
				optionsHtml += "<option value="+tmps[0]+">"+tmps[1]+"</option>";
			}
			strHtml='<select id='+tabId+'_term_privObj termId="'+tabId+'" style="width:'+rptTerm.showLength.value
					+'px;" onchange="termPrivValueChange(this'+autoFreshTab+')">'+optionsHtml+'</select>';
		}else {
			strHtml = '<input id='+tabId+'_term_privObj termId="'+tabId+'" type="text" />';
		}
		//var strHtml='<select id='+tabId+'_term_privObj termId="'+tabId+'" style="width:'+rptTerm.showLength.value
 		//	+'px;" onchange="termPrivValueChange(this'+autoFreshTab+')"></select>';
		privObjTd.innerHTML=strHtml;
		strHtml=null;
	}
	rptTermBuildPrivAttr(rptTerm);	//	应用其它属性
}
//	对预览框应用其它属性
function rptTermBuildPrivAttr(rptTerm)
{
	var tabId=rptTerm.tabId;
	$(tabId+'_term_privObj').style.width=rptTerm.showLength.value;
	rptTerm.ready=false;
	if(!waitTerm(rptTerm,0))	//等待父级条件
	{
		runInterval.add(rptTermBuildPrivAttr,rptTerm,0,[rptTerm]);
		return;
	}
	if(rptTerm.termType.value=="0")//下拉框
	{
		var destData=null;
		var privList=$(tabId+'_term_privObj');
		privList.length=0;
		if(rptTerm.appendValue.value || rptTerm.appendText.value)
		{	
			privList.options.add(new Option(rptTerm.appendText.value,rptTerm.appendValue.value) , 0);
			privList.selectedIndex=0;
		}
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
			var termAttrs=getTermAttrsMap(rptTerm,params);//条件属性,到服务端二次判断权限
			ReportDesignerAction.qryDimTermData(dataSrcId,sql,params,0,termAttrs,function(res){
				clearVarVal(termAttrs);
				if(!res || res[0]=="false")
				{
					throw "报表条件【"+rptTerm.termName.value+"】读取条件下拉框数据错误：\n"+(res?res[1]:res);
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
					throw("报表条件【"+rptTerm.termName.value+"】数据列表格式配置错误\n请重新修改报表配置并参考提示。");
					return;
				}
			}
			bindListTermPrivData(rptTerm,destData);	//	绑定下拉框预览数据
		}
	}
	else if(rptTerm.termType.value=="1")//下拉树
	{
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
				throw("报表条件【"+rptTerm.termName.value+"】配置错误，数据查询SQL长度不足");
				return;
			}
			var termAttrs=getTermAttrsMap(rptTerm,params);//条件属性,到服务端二次判断权限
			ReportDesignerAction.qryDimTermData(dataSrcId,sql,params,0,termAttrs,function(res){
				clearVarVal(termAttrs);
				if(!res || res[0]=="false")
				{
					throw("报表条件【"+rptTerm.termName.value+"】读取条件下拉树数据错误：\n"+(res?res[1]:res));
					return;
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
					throw("报表条件【"+rptTerm.termName.value+"】数据列表格式配置错误，\n请重新修改报表配置并参考提示。");
					return;
				}
			}
			bindTreeTermPrivData(rptTerm,destData);	//	绑定下拉框预览数据
		}
	}
	else if(rptTerm.termType.value=="2")	//	日期 
	{
		rptTerm.ready=true;
		$(tabId+'_term_privObj').value=rptTerm.defaultValue.value[0];
		rptTerm.myCalendar.setDate(rptTerm.defaultValue.value[0]);
		var val=rptTerm.data.value;
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
				throw("时间有效性配置错误,开始值必需小于结束值");
			val[0]=formatNumDate(val[0]);
			val[1]=formatNumDate(val[1]);
		}
		rptTerm.myCalendar.setSensitiveRange(val[0],val[1]);
	}else if(rptTerm.termType.value=="5") {
		rptTerm.ready=true;
	}
}
//	绑定条件 下拉框预览数据
function bindListTermPrivData(rptTerm,destData)
{
	var tabId=rptTerm.tabId;
	var privList=$(tabId+'_term_privObj');
	for(var i=0;i<destData.length;i++)
	{
		privList.options[privList.length]=new Option(destData[i][1],destData[i][0]);
		if(privList.length-1==rptTerm.defaultValue.value[0] || 
				(rptTerm.defaultValue.value[0]==-1 && rptTerm.defaultValue.value[1]==destData[i][0]))
		{
			privList.selectedIndex=privList.length-1;
		}
	}
	rptTerm.ready=true;
	clearVarVal(destData);
	destData=null;
} 
//	绑定条件 下拉树预览数据
function bindTreeTermPrivData(rptTerm,destData)
{
//	rptTerm.ready=true;
	if(destData.length==0)return;
	var termId=rptTerm.tabId;
	rptTerm.dhxTree.termId=termId;
	if(rptTerm.dhxTree.dyType)
	{
		var selId=rptTerm.dhxTree.getSelectedItemId();
		if(selId)selId=selId.split("_")[2];
		for(var i=0;i<destData.length;i++)	//	value ,text ,parent
		{
			var parentId=0;
			if(selId)parentId=termId+"_treeNode_"+selId;
			var itemId=termId+"_treeNode_"+destData[i][0];
			var itemText=destData[i][1];
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
				rptTerm.dhxTree.bindObj.setAttribute("code",rptTerm.defaultValue.value[0]?rptTerm.defaultValue.value[0]:"");
				if(destData[i][3])rptTerm.dhxTree.bindObj.setAttribute("DIM_LEVEL",destData[i][3]);
				rptTerm.dhxTree.selectDefaultVal=true;
			}
			else if(!rptTerm.defaultValue.value[0] && !rptTerm.defaultValue.value[1])
			{
				rptTerm.dhxTree.bindObj=$(termId+"_term_privObj");
				rptTerm.dhxTree.selectItem(itemId,false,false);
				var right=termTreeListSelect(itemId);
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
				rptTerm.dhxTree.bindObj.value=destData[i][1];//rptTerm.defaultValue.value[1]?rptTerm.defaultValue.value[1]:"";
				rptTerm.dhxTree.bindObj.setAttribute("code",rptTerm.defaultValue.value[0]?rptTerm.defaultValue.value[0]:"");
				if(destData[i][3])rptTerm.dhxTree.bindObj.setAttribute("DIM_LEVEL",destData[i][3]);
				rptTerm.dhxTree.selectDefaultVal=true;
			}
			else if(!rptTerm.defaultValue.value[0] && !rptTerm.defaultValue.value[1])
			{
				rptTerm.dhxTree.bindObj=$(termId+"_term_privObj");
				rptTerm.dhxTree.selectItem(itemId,false,false);
				var right=termTreeListSelect(itemId);
				if(right)
				{
					$(termId+"_term_privObj").value=destData[i][1];
					$(termId+"_term_privObj").setAttribute("code",destData[i][0]);
					rptTerm.defaultValue.value[0]=destData[i][0];
					rptTerm.defaultValue.value[1]=destData[i][1];
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
			return termTreeListSelect(id);
		return true;
	}
	rptTerm.dhxTree.setUserData(id,"refresh","true");
	rptTerm.dhxTree.deleteItem(id+"sub",true);
//	rptTerm.dhxTree.selectItem(id,false,false);
	
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
	rptTerm.dhxTree.bindObj.setAttribute("DIM_LEVEL",DIM_LEVEL);
	rptTerm.dhxTree.bindObj.setAttribute("code",code);
	rptTerm.dhxTree.trBx.style.display="none";
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
function termPrivValueChange(obj,autoFreshTab)
{
	if(obj.srcElement)obj=obj.srcElement;
	var termId=obj.getAttribute("termId");
	var rptTerm=reportConfig.termCfms.value[termId];
	//判断是否有依赖此条件的条件存在，存在则刷新
	termPrivFlashCheck(termId,autoFreshTab);
}
//	预览值改变依赖联动
function termPrivFlashCheck(termId,autoFreshTab)
{
	var have=false;
	var rptTerm=reportConfig.termCfms.value[termId];
	for(var tabId in reportConfig.termCfms.value)
	{
		if(termId==tabId)continue;
		var rptTerm2=reportConfig.termCfms.value[tabId];
		if(rptTerm2.parTermName.value!=termId)continue;
		rptTermBuildPrivAttr(rptTerm2);
		termPrivFlashCheck(tabId);
		if(reportConfig.termCfms.value[tabId].parent==rptTerm.parent)
			have=true;
	}
	if(!autoFreshTab)//	不需要支持条件值变动刷新页面
		return;
	if(rptTerm.parent=="reportConfig")	
	{
		refreshTabData();
		 
	}
	else if(rptTerm.parent)
	{
		try
		{
			refreshTabData(rptTerm.parent);
		}
		catch(ex)
		{
			alert("刷新模块页面["+tabId+"]失败："+ex);
		}
	}
}


