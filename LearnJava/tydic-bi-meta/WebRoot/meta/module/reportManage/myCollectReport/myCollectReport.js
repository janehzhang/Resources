
/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        myCollectReport.js
 *Description：
 *       我收藏的报表，用于管理对报表模板的取消收藏和订阅..etc。
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        guoxf
 ********************************************************/
/**
 * 全局变量
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller = new biDwrCaller();

/**
 * 初始化
 */
var myCollectReport = function() {
	// 整理收藏夹
	Tools.addEvent($('clearFavorite'),'click',function(){
		clear();
	})
	var form = $("collectTreeForm");
    // 加载收藏夹树
    loadCollectTree(null,form);
    // 加载我收藏的报表
    loadCollectRpt();
	
}
/**
 * 收藏类型树Action
 */
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"favoriteId",pidColumn:"parentId",
    isDycload:false,textColumn:"favoriteName"
});
var collectTreeConverter=dhx.extend({idColumn:"favoriteId",pidColumn:"parentId",
    textColumn:"favoriteName"
},treeConverter,false);
dwrCaller.addAutoAction("loadCollectTree","CollectAction.getAllCollectType");
dwrCaller.addDataConverter("loadCollectTree",collectTreeConverter);
dwrCaller.isShowProcess("loadCollectTree", false);
//树动态加载Action
dwrCaller.addAction("loadSubCollectTree",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},collectTreeConverter,false);
    CollectAction.getSubCollect(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
dwrCaller.addAutoAction("loadCollectRpt","CollectAction.getAllCollectRpt");
dwrCaller.addAutoAction("cancelCollect","CollectAction.cancelCollectRpt");
dwrCaller.isShowProcess("cancelCollect", false);

/**
 * 收藏类型树加载
 */
var loadCollectTree=function(form){
    var tree = new dhtmlXTreeObject("collectTree","100%","100%",0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
// 	tree.enableItemEditor(true);//  设置树可以编辑某个节点
// 	tree._eItEd=true// 设置树可编辑
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.loadSubCollectTree);
    // 加载Tree
    dwrCaller.executeAction("loadCollectTree",function(data){
        tree.loadJSONObject(data);
    })
}
/**
 * 整理收藏夹
 */
function clear(){
	
	alert(1);
}
// 鼠标移动事件
function mouseOver(id){
	document.getElementById(id).style.display = "block"
}
function mouseOut(id){
	document.getElementById(id).style.display = "none"
}
// 取消收藏
function cancelCollect(id){
	dwrCaller.executeAction("cancelCollect",id,function(data){
		if(data == 1){
			$('report_'+id).parentNode.removeChild($('report_'+id));
		}
	});
}
// 缓存PUSH_TYPE数据
//var pushTypeData = getCodeComboByType("PUSH_TYPE");
//alert(pushTypeData)
function loadCollectRpt() {
	//加载数据
	dwrCaller.executeAction("loadCollectRpt",function(data){
		//根据返回的报表数据，绘制html，然后绑定到界面
		for(var i=0;i<data.length;i++) {
//			alert(data.length)
			var title = data[i]["REPORT_NAME"];
			var count = data[i]["NUM"];
			var comments = data[i]["REPORT_NOTE"];
			var clos = data[i]["COL_ALIAS"];
			var rptId = data[i]["REPORT_ID"];
			$('repMsg').innerHTML += 
			'<li id="report_'+rptId+'" class="content-11" onmouseover=mouseOver('+rptId+') onmouseout=mouseOut('+rptId+') >'+
			  	'<span class="content-name">'+title+'</span>'+
	          	'<span class="content-num">（岗位应用数：'+count+'）</span>'+
	          	'<div class="tips"  id='+rptId+' >'+
		          '<a class="qxsc" >'+
			          '<span style="margin-left:15px;display:inline;">'+
			          	   '（<input class="cheb-1" value="邮件" type=checkbox id="aho" />邮件'+
			          		'<input class="cheb-1" value="彩信" type=checkbox id="aho" />彩信'+
			          		'<input class="cheb-1" value="邮件" type=checkbox id="aho" />短信）'+
			          	'</span> '+
		          	'</a> '+
					'<a class="sc1" href="#" title="取消收藏" onclick=cancelCollect('+rptId+')></a>'+
          		'</div> '+
          		'<a class="content-text" href="#">'+comments+'（'+clos+'）</a>'+
          	'</li>';
		}
	})
}




dhx.ready(myCollectReport);