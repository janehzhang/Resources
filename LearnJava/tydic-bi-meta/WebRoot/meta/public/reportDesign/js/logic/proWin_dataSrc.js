 //绑定表格列头合并规则
function bindRptTabColUniteRule()
{
	var rptTab=currentObj.obj;
	var tabId=currentObj.id;
	var rptData=null;
	proWin.setText(rptTab.tabName.value+"-表头合并规则设置对话框");
	proWin.attachHTMLString("<div id='"+tabId+"_colUniteRule_proWin' style='width:100%;height:100%;'>表头合并规则设置对话框</div>");
}
//回写列头合并规则
function setRptTabColUniteRule()
{
	
} 
