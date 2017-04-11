/**
 * 分页组件。
 * @constructor
 */
function Page() {
}
/**
 * 分页初始化
 */
Page.prototype.init = function () {
    //当前页
    page.currPageNum = 1;
    //每页条数
    page.pageCount = global.constant.pageSize|| 0;
   
    //总共多少页
    page.allPageCount = 0;
}

/**
 * 构建分页Html
 * @param reportQueryData
 */
Page.prototype.buildPage = function (totalCount, isResetPage) 
{
//alert("sdfs " + totalCount);
 if(parseInt(totalCount) > 0)
 {
   page.allPageCount = Math.ceil(totalCount / page.pageCount);
   var bar="<span style='color:#000000'>每页显示</span>";
		  bar +="<select name='pageSize' id='pageSize' onchange='page.changePageCount(this);'>";
					      bar +="<option value='15' "; bar+=(page.pageCount==15)?'selected':''; bar +=">15</option>";
                          bar +="<option value='20' "; bar+=(page.pageCount==20)?'selected':''; bar +=">20</option>";
                          bar +="<option value='30' "; bar+=(page.pageCount==30)?'selected':''; bar +=">30</option>";
                          bar +="<option value='40' "; bar+=(page.pageCount==40)?'selected':''; bar +=">40</option>";
                          bar +="<option value='50' "; bar+=(page.pageCount==50)?'selected':''; bar +=">50</option>";
		  bar +="</select>条记录，";
	bar += "<span style='color:#000000'>共" + totalCount + "条记录，共"
	     + page.allPageCount + "页，当前第 " + page.currPageNum + "页</span>&nbsp";
	
  if (page.currPageNum == 1) 
   {
	     bar += "<font color='gray'>首页</font>&nbsp;&nbsp;";
   }
  else
  {
		 bar += "<a href='javascript:void(0);' onclick='page.firstPage();'>首页</a>&nbsp;&nbsp;";
		 bar += "<a href='javascript:void(0);' onclick='page.prePage();' >上一页</a>&nbsp;&nbsp;";
  }
  if (page.currPageNum == page.allPageCount)
  {
		bar += "<a href='javascript:void(0);'><font color='gray'>下一页</font></a>&nbsp;&nbsp;";
		bar += "<a href='javascript:void(0);'><font color='gray'>尾页</font></a>&nbsp;&nbsp;";
  } 
   else
   {
	    bar += "<a href='javascript:void(0);' onclick='page.nextPage();'>下一页</a>&nbsp;&nbsp;";
	    bar += "<a href='javascript:void(0);' onclick='page.endPage();'>尾页</a>&nbsp;&nbsp;";
   }
	    bar+="<span style='color:#000000'>跳转到第</span>" +
	         "<input style='height:15px;width:40px' type='text'   value='"+page.currPageNum+"' id='_pageNum' name='_pageNum' >页" +
	         "<input style='height:27px;width:37px' type='button' value='GO' onclick='page.changePage()' />";
	   
	
      $("pageDiv").innerHTML = bar;
      $("pageDiv").style.display = "block"; 
 }
 else
 {
     $("pageDiv").style.display = "none"; 
 }	
}


/**
 * 重置分页
 */
Page.prototype.resetPage = function () {
    page.currPageNum =  1;
    page.allPageCount = 0;

}
/**
 * 排序获取实例
 */
Page.getInstance = function () {
    if(page==null)
    {
    	page= new Page();
    }
    return page;
}


Page.prototype.changePageCount=function (obj){
	page.currPageNum = 1;
	page.pageCount=obj.value;
	excuteInitData();
}


/**
 * 首页
 */
Page.prototype.firstPage = function () 
{
	 page.currPageNum = 1;
	 excuteInitData();
}
/**
 * 上一页
 */
Page.prototype.prePage = function () 
{
	 page.currPageNum = page.currPageNum-1;
	 excuteInitData();
}

/**
 * 下一页
 */
Page.prototype.nextPage = function () 
{
	 page.currPageNum = page.currPageNum+1;
	 excuteInitData();
}

/**
 * 末页
 */
Page.prototype.endPage = function () 
{
	
	 page.currPageNum = page.allPageCount;
	 excuteInitData();
}

/**
 * 跳转
 */
Page.prototype.changePage = function () {
   var pageNum= document.getElementById('_pageNum').value;
    if (!pageNum || pageNum == page.currPageNum) {
        return;
    }
    if (!parseInt(pageNum)) {
        dhx.alert("请输入一个合法的页码！");
        return;
    } else if (parseInt(pageNum) < 0 || parseInt(pageNum) > page.allPageCount) {
        dhx.alert("请输入一个合法的页码！");
        return;
    }
    page.currPageNum = parseInt(pageNum);
    excuteInitData();
}