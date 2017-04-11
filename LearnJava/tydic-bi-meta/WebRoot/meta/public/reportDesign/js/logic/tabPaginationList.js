function getTabPageCfmStr(tabId)
{
	var pageStr='<table width="100%" border="1px" cellpadding="0" cellspacing="0"   class="PageStyle" style="font-size: 12px; height:22px; ">'
	pageStr+='<tr><td nowrap style="height: 20px; padding-right:15px;" class="bottom"> ';
	pageStr+='<div id="'+tabId+'_PageDes" align=right Style="float:left;font-size: 12px;padding-left:20px;" ></div>';
	pageStr+='<div style="float:right;"> ';
	pageStr+='&nbsp;&nbsp;&nbsp;<a id="'+tabId+'_getFirstPage">首页</a>&nbsp;&nbsp;&nbsp;<a id="'+tabId+'_getPreiPage" >上一页</a>&nbsp;&nbsp;&nbsp;<a id="'+tabId
				+'_getNextPage" >下一页</a>&nbsp;&nbsp;&nbsp;';
	pageStr+='<a id="'+tabId+'_getLastPage" >尾页</a>&nbsp;&nbsp;&nbsp;<label id="MessageLabel" >转到：</label>';
	pageStr+='<select ID="'+tabId+'_PageDropDownList" onchange="changeTabTablePage(\''+tabId+'\')" style=" width:60px; padding-right: 0px; display: inline; padding-left: 0px;';
	pageStr+='padding-bottom: 0px; margin: 0px; overflow: visible; border-top-style: inset;';
	pageStr+='padding-top: 0px; border-right-style: inset; border-left-style: inset; position: relative;';
	pageStr+='height: 18px; border-bottom-style: inset; font-size: 12px; vertical-align: middle;';
	pageStr+='direction: ltr; text-indent: 0px; line-height: 0px; letter-spacing: 0px; text-align: left;">';
	pageStr+='</select></div>';
	pageStr+='</td></tr></table>';
	return pageStr;
}
function changeTabTablePage(tabId)
{
	var rptTab=getAllTabs(tabId);
	rptTab.table.currentPageNum=$(tabId+'_PageDropDownList').value;
	readTabData(tabId);
}
//表头排序
function tabTableSorting(tabId,colName)
{
	var rptTab=getAllTabs(tabId);//需要记录上次的排序，多字段排序
	if(!rptTab.table.orderColMap)rptTab.table.orderColMap={};
	if(!rptTab.table.orderCols)rptTab.table.orderCols=[];
	if(rptTab.table.orderColMap[colName] && rptTab.table.orderCols[0]==colName)
	{
		if(rptTab.table.orderColMap[colName]=="asc")
			rptTab.table.orderColMap[colName]="desc";
		else
			rptTab.table.orderColMap[colName]="asc";
	}
	else
	{
		if(rptTab.table.orderColMap[colName])
		{
			for(var i=1;i<rptTab.table.orderCols.length;i++)
				if(rptTab.table.orderCols[i]==colName)
				{
					rptTab.table.orderCols.remove(i);
					break;
				}
		}
		rptTab.table.orderColMap[colName]="asc";
		rptTab.table.orderCols.unshift(colName);
	}
 	if(rptTab.table.orderCols.length>5)//保留五个
	{
		for(var i=rptTab.table.orderCols.length-1;i>=5;i--)
		{
			delete rptTab.table.orderColMap[rptTab.table.orderCols[i]];
			rptTab.table.orderCols.remove(i);
		}
	}
	if(rptTab.table.dataTransRule.transFlag &&(scriptRunPage==0 || rptTab.table.dataTransRule.designerPriv)) //横纵转换
	{
		bindTabData(rptTab,rptTab.rptData);
		return ;
	}
	rptTab.table.currentPageNum=1;
	$(tabId+'_PageDropDownList').selectedIndex =rptTab.table.currentPageNum-1;
	changeTabTablePage(tabId);
}
function getTableOrderbyStr(rptTab)
{
	var res="";
	var orderHave={};
	if(!rptTab.table.orderColMap)rptTab.table.orderColMap={};
	if(!rptTab.table.orderCols)rptTab.table.orderCols=[];
	for(var i=0;i<rptTab.table.colFreeze.value;i++)//增加锁定列
	{
		if(rptTab.table.orderColMap[rptTab.table.colFreeze.value[i]])
		{
			if(res)res+=","+rptTab.table.colFreeze.value[i]+" "+rptTab.table.orderColMap[rptTab.table.colFreeze.value[i]];
			else res+=rptTab.table.colFreeze.value[i]+" "+rptTab.table.orderColMap[rptTab.table.colFreeze.value[i]];
			orderHave[rptTab.table.colFreeze.value[i]]=true;
		}
		else
		{
			if(res)res+=","+rptTab.table.colFreeze.value[i]+" asc";
			else res+=rptTab.table.colFreeze.value[i]+" asc";
		}
	}
	if(rptTab.table.orderCols)
	for(var i=0;i<rptTab.table.orderCols.length;i++)
	{
		if(!orderHave[rptTab.table.orderCols[i]])
		{
			if(res)res+=","+rptTab.table.orderCols[i]+" "+rptTab.table.orderColMap[rptTab.table.orderCols[i]];
			else res+=rptTab.table.orderCols[i]+" "+rptTab.table.orderColMap[rptTab.table.orderCols[i]];
		}
	}
	clearVarVal(orderHave);
	return res;
}
function buildPagination(rptTab)
{
	var tabId=rptTab.tabId;
	if(!rptTab.table.autoPagination.value)return;
	if(rptTab.table.currentPageNum==1)
	{
		if(rptTab.rptData[1].length>0)
		{
			rptTab.table.currentRowCounts=rptTab.rptData[1][0][rptTab.rptData[0].length-1];//记录数
			rptTab.table.currentPageCount=parseInt((rptTab.table.currentRowCounts+rptTab.table.autoPagination.value-1)/rptTab.table.autoPagination.value);
		}
		else
		{
			rptTab.table.currentRowCounts=0;
			rptTab.table.currentPageCount=1;
		}
	}
	bandTabPageSelect(rptTab);
  	setTabPager(rptTab);
} 
function bandTabPageSelect(rptTab)
{
	var tabId=rptTab.tabId;
    $(tabId+'_PageDes').innerHTML ="总条"+rptTab.table.currentRowCounts+"条记录,每页"+rptTab.table.autoPagination.value+
    			"条,当前第"+rptTab.table.currentPageNum+"/"+rptTab.table.currentPageCount+"页";
    var pageDDList=$(tabId+'_PageDropDownList');
	if(pageDDList.length==rptTab.table.currentPageCount)
    {
    	pageDDList.selectedIndex =rptTab.table.currentPageNum-1;
        return;
    }
    pageDDList.length=0;
    for(i=1 ;i<=rptTab.table.currentPageCount;i++)
    {
		pageDDList.options[pageDDList.length] = new Option('第'+i+'页',i);
       	if(rptTab.table.currentPageNum==i)
       		pageDDList.selectedIndex =i-1;
    }
}
function getTabFirstPage(tabId) 
{
	var rptTab=getAllTabs(tabId);
	rptTab.table.currentPageNum=1;
	$(tabId+'_PageDropDownList').selectedIndex =rptTab.table.currentPageNum-1;
	changeTabTablePage(tabId);
}
function getTabNextPage(tabId) 
{
	var rptTab=getAllTabs(tabId);
	rptTab.table.currentPageNum++; 
	if(rptTab.table.currentPageNum>rptTab.table.currentPageCount) rptTab.table.currentPageNum=rptTab.table.currentPageCount;
	$(tabId+'_PageDropDownList').selectedIndex =rptTab.table.currentPageNum-1;
	changeTabTablePage(tabId);
}
function  getTabPreiPage(tabId)
{
	var rptTab=getAllTabs(tabId);
	rptTab.table.currentPageNum--;
	if(rptTab.table.currentPageNum<=0) rptTab.table.currentPageNum=1;
	$(tabId+'_PageDropDownList').selectedIndex =rptTab.table.currentPageNum-1;
	changeTabTablePage(tabId);
} 
function getTabLastPage(tabId) 
{
	var rptTab=getAllTabs(tabId);
	rptTab.table.currentPageNum=rptTab.table.currentPageCount;
	$(tabId+'_PageDropDownList').selectedIndex =rptTab.table.currentPageNum-1;
	changeTabTablePage(tabId);
}
function setTabPager(rptTab)
{
	var tabId=rptTab.tabId;
	var getfirstpageLink=$(tabId+"_getFirstPage");
	var getPreiPageLink=$(tabId+"_getPreiPage");
	var getNextPageLink=$(tabId+"_getNextPage");
	var getLastPageLink=$(tabId+"_getLastPage");
    getfirstpageLink.disabled =false;    
    getPreiPageLink.disabled =false;    
    getNextPageLink.disabled =false;    
    getLastPageLink.disabled =false;    
    getfirstpageLink.href='#';
    getfirstpageLink.onclick=function(evel){getTabFirstPage(tabId);return false;};
    getPreiPageLink.href='#';
    getPreiPageLink.onclick=function(evel){getTabPreiPage(tabId);return false;};
    getNextPageLink.href='#';
    getNextPageLink.onclick=function(evel){getTabNextPage(tabId);return false;};
    getLastPageLink.href='#';
    getLastPageLink.onclick=function(evel){getTabLastPage(tabId);return false;};
    if(rptTab.table.currentPageNum==rptTab.table.currentPageCount && rptTab.table.currentPageNum==1)
    {
        getfirstpageLink.disabled =true;    
	    getPreiPageLink.disabled =true;    
	    getNextPageLink.disabled =true;    
	    getLastPageLink.disabled =true;
		getfirstpageLink.onclick=null;
		getPreiPageLink.onclick=null;
		getNextPageLink.onclick=null;
		getLastPageLink.onclick=null;
        return;
    }
    if(rptTab.table.currentPageNum==rptTab.table.currentPageCount)
    {
        getNextPageLink.onclick=null
        getNextPageLink.disabled =true;
        getLastPageLink.onclick=null
        getLastPageLink.disabled =true;
        return;
    }
    if(rptTab.table.currentPageNum==1)
    {
        getfirstpageLink.onclick=null;
        getfirstpageLink.disabled =true;    
        getPreiPageLink.onclick=null;
        getPreiPageLink.disabled =true;    
        return;
    }
}