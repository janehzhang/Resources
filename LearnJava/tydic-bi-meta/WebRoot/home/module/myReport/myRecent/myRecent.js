/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *
 *Description：
 *
 *Dependent：
 *
 *Author:
 *        刘斌
 *Finished：
 *       2012-04-10
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/

/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller = new biDwrCaller();


/**
 * 初始化方法
 */
var recentInit = function(){
    var recentLayout = new dhtmlXLayoutObject(document.getElementById("container"), "1C");
    recentLayout.cells("a").hideHeader();
    recentLayout.hideConcentrate();
    recentLayout.hideSpliter();//移除分界边框。

    var tabbar = recentLayout.cells("a").attachTabbar();
    tabbar.addTab("myOpened", '<span style="font-weight: bold;">最近打开的</span>', "120px");
    tabbar.addTab("beCommented", '<span style="font-weight: bold;">我被评论</span>', "120px");
    tabbar.addTab("iCommented", '<span style="font-weight: bold;">我评论过</span>', "120px");
    tabbar.setTabActive("myOpened");
    tabbar.setHrefMode("iframes-on-demand");
    tabbar.setContentHref("myOpened",getBasePath()+"/home/module/myReport/myRecent/myOpen.jsp");
    tabbar.setContentHref("beCommented",getBasePath()+"/home/module/myReport/myRecent/myBeCommented.jsp");
    tabbar.setContentHref("iCommented",getBasePath()+"/home/module/myReport/myRecent/myCommented.jsp");
};

dhx.ready(recentInit);