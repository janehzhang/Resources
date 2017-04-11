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
	this.selTree.tree.closeAllItems(rootId?rootId:this.selTree.tree.rootId); 
	this.selTree.inited=true;
}

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
	return meta.term.termControl.termTreeListFlash(id,"onClick");
}
meta.term.termControl.termTreeListFlashSel=function (id)
{
	return meta.term.termControl.termTreeListFlash(id,"onSelect");
}
meta.term.termControl.termTreeListFlashDbl=function (id)
{
	return meta.term.termControl.termTreeListFlash(id,"onDblClick");
}
//	条件 下拉树 异步刷新
meta.term.termControl.termTreeListFlash=function (id,type)
{
	var selTree=this.selTree;
	var term=selTree.termControl;
	if(rptTerm.dhxTree.getUserData(id,"refresh"))
	{
		if(type=="onDblClick")
			meta.term.termControl.termTreeListSelect(id);
		return true;
	}
	this.setUserData(id,"refresh","true");
	this.deleteChildItems(id);
	var thisCfm=term.getConfig();
	thisCfm.duckSql=thisCfm.dataRule.split(";")[1];
	if(!thisCfm.duckSql)return;
	thisCfm.duckSql.replace(new Regex("/"+thisCfm.termName+"/ig"),id+"");
	TermControl.getTermData(thisCfm,function(res)//异步刷新数据
	{
		if(res==null || res=="null" || res[0]=="false")
		{
			alert("读取条件控件数据失败,msg："+(res && res.sort?res[1]:res));
			return false;
		}
		term.bindTreeData(res[1]);
	});
}
//	条件 下拉树 值选择
meta.term.termControl.termTreeListSelect=function (id)
{
	var selTree=this.selTree;
	var term=selTree.termControl;
	var val=this.getSelectedItemText();
	term.inputObj.value=val;//selTree.getItemValue(id);
	term.inputObj.setAttribute("code",id);
	selTree.box.style.display = "none";
	term.valueChange();
	return true;
} 