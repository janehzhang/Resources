/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        rptIndexLeft.js
 *Description：
 *    首页左边（我创建，我收藏，我订阅 ……相关JS）
 *    整个JS代码定义结构顺序依次为  全局变量，init类，查询请求后台类，界面交互控制类，超链接类
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

//全局变量
var boxMenu = null;         //盒子菜单对象

/**=======我是分隔线（其上全局变量，其下初始）===========**/

//初始首页左边(rptIndex.js里面入口调用)
var initRptIndexLeft = function () {
    boxMenu = new BoxMenu("menuleft",true);
    boxMenu.init();
    initMyFav();
    initMyCreate();
    initMyPush();
    initShareToMe();
    initPublicRpt();
};

//初始我的收藏
var initMyFav = function(){
    RptIndexAction.queryMyFavRpt(0,function(data){
        if(data && data.length){
            for(var i=0;i<data.length;i++){
                var li = createMyFavLi({
                    snNum:i+1,
                    rptId:data[i]["REPORT_ID"],
                    rptName:data[i]["REPORT_NAME"],
                    rptNote:data[i]["REPORT_NOTE"],
                    rptFavTime:data[i]["FAVORITE_REPORT_TIME"],
                    rptListing:data[i]["IS_LISTING"],
                    rptCreateTime:data[i]["CREATE_TIME"],
                    rptUserId:data[i]["USER_ID"],
                    rptRightLevel:data[i]["RIGHT_LEVEL"]
                });
                boxMenu.insertMenuToBox(0,li);
            }
        }else{
            boxMenu.insertMenuToBox(0,createNoDataLi());
        }
    });
};

//初始我创建
var initMyCreate = function(){
    RptIndexAction.queryMyCreateRpt(0,function(data){
        if(data && data.length){
            for(var i=0;i<data.length;i++){
                var li = createMyRptLi({
                    snNum:i+1,
                    rptId:data[i]["REPORT_ID"],
                    rptName:data[i]["REPORT_NAME"],
                    rptNote:data[i]["REPORT_NOTE"],
                    rptListing:data[i]["IS_LISTING"],
                    rptCreateTime:data[i]["CREATE_TIME"],
                    rptOpenNum:data[i]["OPEN_NUM"]
                });
                boxMenu.insertMenuToBox(1,li);
            }
        }else{
            boxMenu.insertMenuToBox(1,createNoDataLi());
        }
    });
};

//初始我的订阅
var initMyPush = function(){
    RptIndexAction.queryMyPushRpt(0,function(data){
        if(data && data.length){
            for(var i=0;i<data.length;i++){
                var li = createMyPushLi({
                    snNum:i+1,
                    rptId:data[i]["REPORT_ID"],
                    rptName:data[i]["REPORT_NAME"],
                    rptNote:data[i]["REPORT_NOTE"],
                    rptListing:data[i]["IS_LISTING"],
                    rptCreateTime:data[i]["CREATE_TIME"],
                    rptUserId:data[i]["USER_ID"],
                    rptRightLevel:data[i]["RIGHT_LEVEL"]
                });
                boxMenu.insertMenuToBox(2,li);
            }
        }else{
            boxMenu.insertMenuToBox(2,createNoDataLi());
        }
    });
};

//初始别人共享个我
var initShareToMe = function(){
    RptIndexAction.querySharingRpt(0,function(data){
        if(data && data.length){
            for(var i=0;i<data.length;i++){
                var li = createShareToMeLi({
                    snNum:i+1,
                    rptId:data[i]["REPORT_ID"],
                    rptName:data[i]["REPORT_NAME"],
                    rptNote:data[i]["REPORT_NOTE"],
                    rptListing:data[i]["IS_LISTING"],
                    rptCreateTime:data[i]["CREATE_TIME"],
                    rptUserId:data[i]["USER_ID"],
                    rptUserNameCn:data[i]["USER_NAMECN"]
                });
                boxMenu.insertMenuToBox(3,li);
            }
        }else{
            boxMenu.insertMenuToBox(3,createNoDataLi());
        }
    });
};

//初始公共报表
var initPublicRpt = function(){
    RptIndexAction.queryPublicRpt(0,function(data){
        if(data && data.length){
            for(var i=0;i<data.length;i++){
                var li = createPublicRptLi({
                    snNum:i+1,
                    rptId:data[i]["REPORT_ID"],
                    rptName:data[i]["REPORT_NAME"],
                    rptNote:data[i]["REPORT_NOTE"],
                    rptListing:data[i]["IS_LISTING"],
                    rptCreateTime:data[i]["CREATE_TIME"],
                    rptUserId:data[i]["USER_ID"],
                    rptUserNameCn:data[i]["USER_NAMECN"]
                });
                boxMenu.insertMenuToBox(4,li);
            }
        }else{
            boxMenu.insertMenuToBox(4,createNoDataLi());
        }
    });
};

/**==========我是分隔线（其上初始）=========================**/

/**
 * 移动定位左边的层(整个窗体出现滚动条时，浮动跟随滚动定位,rptIndex.js  滚动事件调用)
 * @param t 滚动条位置top
 */
var movePosBoxMenu = function(t){
    var headerDIV = document.getElementById("headerDIV");
    var menuleft = document.getElementById("menuleft");
    var oh = headerDIV.offsetTop + headerDIV.offsetHeight + 20 ;
    if(t>oh){
        menuleft.style.top = (t-oh+15)+"px";
    }else{
        menuleft.style.top = (boxMenuStartTop_VAL -oh)+"px";
    }
};
