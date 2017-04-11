/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       	termControl.js
 *Description：条件控件封装
 *
 *Dependent：
 *
 *Author:
 *        hans
 ********************************************************/
//	绑定条件 下拉树预览数据
meta.term.termControl.prototype.bindTreeData=function(data,rootId)
{
	if(data.length==0)return;
	if(this.dynaLoad==0)
		this.selTree.clearData();
	var selId=this.selTree.tree.getSelectedItemId();
	if(selId)selId=selId.split("_")[2];
	var lastNodeId="";
	this.selTree.appendData(data);
	if(!this.dynaLoad)
		this.selTree.tree.closeAllItems(rootId?rootId:this.selTree.tree.rootId); 
	this.selTree.inited=true;
}
//
meta.term.termControl.termTreeListOpenItem=function (itemId)
{
	var selTree=this.selTree;
	var term=selTree.termControl;
	selTree.tree.openItem(itemId);
	return true;
}
//	条件 下拉树 异步刷新事件
meta.term.termControl.termTreeListFlashClk=function (id,id2)
{
	Debug("onClick");
	var selTree=this.selTree;
	var term=selTree.termControl;
	return meta.term.termControl.termTreeListFlash(id,"onClick",term);
}
meta.term.termControl.termTreeListFlashSel=function (id)
{
	Debug("onSelect");
	var selTree=this.selTree;
	var term=selTree.termControl;
return meta.term.termControl.termTreeListFlash(id,"onSelect",term);
}
meta.term.termControl.termTreeListFlashDbl=function (id)
{
	Debug("onDblClick");
	var selTree=this.selTree;
	var term=selTree.termControl;
	return meta.term.termControl.termTreeListFlash(id,"onDblClick",term);
}
//meta.term.termControl.prototype.termTreeDynaLoad=function(id,tree)
//{
//	Debug(id);
////	if(!id)return;
//	var selTree=tree.selTree;
//	var term=selTree.termControl;
////	if(rptTerm.dhxTree.getUserData(id,"refresh"))
////	{
////		if(type=="onDblClick")
////			meta.term.termControl.termTreeListSelect(id);
////		return true;
////	}
////	this.setUserData(id,"refresh","true");
////	this.deleteChildItems(id);
//	var thisCfm=term.getConfig();
//	thisCfm.dataRule=thisCfm.dataRule.split(";")[1];
//	if(!thisCfm.dataRule)return;
//	thisCfm.dataRule=thisCfm.dataRule.replace(new RegExp("{"+thisCfm.termName+"}","ig"),(id+"").replace(selTree._priFix,""));
//	thisCfm.value=term.getValue();
//	
//	TermControl.getTermData([thisCfm],function(res)//异步刷新数据
//	{
//		if(res==null || res=="null" || res[0]=="false")
//		{
//			alert("读取条件控件数据失败,msg："+(res && res.sort?res[1]:res));
//			return false;
//		}
//		term.bindTreeData(res[1],id);
//	});
//}
//	条件 下拉树 异步刷新
meta.term.termControl.termTreeListFlash=function (id,type,term)
{
	if(!term)term=this.selTree.termControl;
	if(!term.dynaLoad)return true;
	var selTree=term.selTree;
	if(term.mulSelect && type=="onDblClick")
	{
		meta.term.termControl.termTreeListSelect(id,term);
		term.selTree.tree.closeItem(id);
	}
	if(selTree.tree.getUserData(id,"refresh"))
	{
		if(type=="onDblClick" && !term.mulSelect)
		{
			meta.term.termControl.termTreeListSelect(id,term);
			term.selTree.tree.closeItem(id);
		}
		return true;
	}
	if(id==term.selTree.tree.rootId)return;
	Debug("type:"+type+"   onOpenStart id="+id);
	selTree.tree.setUserData(id,"refresh","true");
	var thisCfm=term.getConfig();
	thisCfm.dataRule=thisCfm.dataRule.split(";")[1];
	if(!thisCfm.dataRule)return;
	thisCfm.dataRule=thisCfm.dataRule.replace(new RegExp("{"+thisCfm.termName+"}","ig"),(id+"").replace(selTree._priFix,""));
	thisCfm.value=term.getValue();
	
	TermControl.getTermData([thisCfm],function(res)//异步刷新数据
	{
		term.selTree.tree.openItem(id);term.selTree.tree.closeItem(id);
		if(res==null || res=="null" || res[0]=="false")
		{
			alert("读取条件控件数据失败,msg："+(res && res.sort?res[1]:res));
			return false;
		}
		term.bindTreeData(res[1],id);
		term.selTree.tree.openItem(id);
	});
}
//	条件 下拉树 值选择
meta.term.termControl.termTreeListSelect=function (id,term)
{
	if(!term)term=this.selTree.termControl;
	if(!(term instanceof meta.term.termControl))term=term.selTree.termControl;
	var selTree=term.selTree;
	if(term.mulSelect)
	{
		if(!term.inputObj.checkIds)term.inputObj.checkIds={};
		selTree.tree.setCheck(id,!term.inputObj.checkIds[id]);
		meta.term.termControl.termTreeCheck(id,!term.inputObj.checkIds[id],term);
	}
	else
	{
		var val=selTree.tree.getSelectedItemText();
		term.inputObj.value=val;//selTree.getItemValue(id);
		term.inputObj.setAttribute("code",id);
		selTree.box.style.display = "none";
		term.valueChange();
	}
	return false;
} 
meta.term.termControl.termTreeCheck =function(id,state,term)
{
	if(!term)term=this.selTree.termControl;
	if(term.dynaLoad)meta.term.termControl.termTreeListFlash(id,"",term);
	var selTree=term.selTree;
	var checkIdStr=selTree.tree.getAllChecked();
	if(!checkIdStr){term.inputObj.value="";return;}
	checkIdStr=checkIdStr.split(",");
	term.inputObj.value="";
	if(!term.inputObj.checkIds)term.inputObj.checkIds={};
	Destroy.clearVarVal(term.inputObj.checkIds);
	for(var i=0;i<checkIdStr.length;i++)
	{
		term.inputObj.checkIds[checkIdStr[i]]=selTree.tree.getItemText(checkIdStr[i]);
		if(term.inputObj.value)term.inputObj.value+=","+term.inputObj.checkIds[checkIdStr[i]];
		else term.inputObj.value=term.inputObj.checkIds[checkIdStr[i]];
	}
	term.valueChange();
}
