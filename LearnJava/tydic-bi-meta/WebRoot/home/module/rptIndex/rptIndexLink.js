/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        rptIndexLink.js
 *Description：
 *    首页的超链接 需要的JS （rptIndex.js 或 rptIndexLeft.js  内都有可能调用）
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

//打开模型
var openModel = function(modelId){
    window.open(urlEncode(base+"/meta/module/reportManage/publishDataModel/searchPublishDataModel.jsp?issueId="+modelId));
};
//打开报表
var openRpt = function (rptId) {
    window.open(urlEncode(base+"/home/module/commentRpt/commentRpt.jsp?rptId="+rptId));
};
//根据模型新建报表
var openNewRptByModel = function(modelId){
    window.open(urlEncode(base+"/meta/module/reportManage/build/reportConfig.jsp?issueId="+modelId));
};

//更多我的收藏
 var moreMyFav = function(){
     window.open(urlEncode(base+"/home/module/myReport/myCenter/myCenter.jsp?openli=myFavorite"));
     var e = this.event || window.event;
     e.cancelBubble = true;
 };
//更多我的报表
var moreMyRpt = function(){
    window.open(urlEncode(base+"/home/module/myReport/myCenter/myCenter.jsp?openli=myCreate"));
    var e = this.event || window.event;
    e.cancelBubble = true;
};
//更多我的订阅
var moreMyPush = function(){
    window.open(urlEncode(base+"/home/module/myReport/myCenter/myCenter.jsp?openli=myFavorite"));
    var e = this.event || window.event;
    e.cancelBubble = true;
};
//更多别人共享给我
var moreShareToMe = function(){
    window.open(urlEncode(base+"/home/module/myReport/myCenter/myCenter.jsp?openli=myShare"));
    var e = this.event || window.event;
    e.cancelBubble = true;
};
//更多公共
var morePublicRpt = function(){
    window.open(urlEncode(base+"/home/module/myReport/myCenter/myCenter.jsp?openli=myPublic"));
    var e = this.event || window.event;
    e.cancelBubble = true;
};



