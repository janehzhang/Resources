/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        commentRptLink.js
 *Description：
 *        打开评论报表页超链接JS
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

//复制报表
var copyRpt = function(rptId){
    rptId = rptId || rptId_VAL;
    window.open(urlEncode(base+"/meta/module/reportManage/build/reportConfig.jsp?reportId="+rptId+"&copyFlag=1"));
};
//修改报表
var modifyRpt = function(rptId){
    rptId = rptId || rptId_VAL;
    window.open(urlEncode(base+"/meta/module/reportManage/build/reportConfig.jsp?reportId="+rptId));
};