/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       favCommon.js
 *Description：
 *       分页功能js
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，公共JS
 *Author:
 *       李国民
 *Date：
 *       12-3-20
 ********************************************************/

	var totalCount=0;		//查询总数
	var page=1;				//当前显示第几页（默认1）
	var pageCount=1;		//总共几页
	var count=7;			//每页展示几条（默认7）
	var pageBean ={};		//分页对象
		pageBean.posStart = 0;	
		pageBean.count = count;
	
	//设置分页值
	setPage = function(pageValue){
		page = pageValue;
		pageBean.posStart = (page-1)*count;
		pageBean.count = count;
	}
	
	//展现分页
	showPage = function(){
		var tem = totalCount/count+"";
		if(tem.indexOf(".")!=-1){
			pageCount = parseInt(tem*1)+1;
		}else{
			pageCount = tem*1;
		}
		var pageStr = "";
		if(pageCount==1){		//如果总分页数等于1表示没有分页
			pageStr += "首页&nbsp;&nbsp;上一页&nbsp;&nbsp;下一页&nbsp;&nbsp;末页&nbsp;&nbsp;&nbsp;&nbsp;";
			
		}else{
			if(page==1){  //第一页时，首页和上一页没有数据
				pageStr += "首页&nbsp;&nbsp;上一页&nbsp;&nbsp;";
			}else{
				pageStr += "<a onClick='doPage(1)' style='color:blue;'>首页</a>&nbsp;&nbsp;";
				pageStr += "<a onClick='doPage("+(page-1)+")' style='color:blue;'>上一页</a>&nbsp;&nbsp;";
			}
			if(page==pageCount){
				pageStr += "下一页&nbsp;&nbsp;末页&nbsp;&nbsp;&nbsp;&nbsp;";
			}else{
				pageStr += "<a onClick='doPage("+(page+1)+")' style='color:blue;'>下一页</a>&nbsp;&nbsp;";
				pageStr += "<a onClick='doPage("+pageCount+")' style='color:blue;'>末页</a>&nbsp;&nbsp;&nbsp;&nbsp;";
			}
		}
		pageStr += "每页显示"+count+"条&nbsp;&nbsp;总共"+totalCount+"条&nbsp;&nbsp;当前"+page+"/"+pageCount+"页";
		return pageStr;
	}