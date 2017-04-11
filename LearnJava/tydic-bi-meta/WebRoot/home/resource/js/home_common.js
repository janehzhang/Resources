/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        home_common.js.js
 *Description：
 *        home 系统通用的一些JS
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

//连接到首页
var linkRptIndex = function(){
    window.location = (urlEncode(base+"/home/module/rptIndex/rptIndex.jsp"));
};

//连接到个人中心
var linkPersonCenter = function(){
    window.location = (urlEncode(base+"/home/module/myReport/myCenter/myCenter.jsp"));
};
//连接到联系我们
var linkContactUs = function(){

};
//连接到FAQ
var linkFAQ = function(){

};
//连接到帮助
var linkHelp = function(){

};
//退出系统
var linkLogout = function(){
    if(confirm("您确定要退出系统吗?")){
        LoginAction.logout(function(data){
            window.location = base;
        });
    }
};