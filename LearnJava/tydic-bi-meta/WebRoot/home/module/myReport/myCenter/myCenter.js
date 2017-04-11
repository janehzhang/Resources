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
 *       2012-04-16
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/

function initFun(openli){
    if(openli!=null && $(openli)!=null){
        $(openli).onclick();
        eval($(openli).href);
    }else{
        $("myRecent").className = 'current';
        $("rightDetail").innerHTML = '<iframe src="'+urlEncode('../myRecent/myRecent.jsp')+'" style="height: 550px;width: 900px; border:0px;"></iframe>';
    }
    document.getElementById("current-user").innerHTML = user.userNamecn;
}

/**
 * 改变菜单
 * @param value 1：我最近做过，2：我的收藏，3：别人共享给我，4：我的创建，5：个人资料完善，6：修改密码
 */
var changeRightFrame = function(value){
    switch(value){
        case 1: $("rightDetail").innerHTML = '<iframe src="'+urlEncode('../myRecent/myRecent.jsp')+'" style="height: 550px;width: 900px; border:0px;"></iframe>'; return;
        case 2: $("rightDetail").innerHTML = '<iframe src="'+urlEncode('../myFavorite/myFavorite.jsp')+'" style="height: 550px;width: 900px; border:0px;"></iframe>'; return;
        case 3: $("rightDetail").innerHTML = '<iframe src="'+urlEncode('../myShare/myShare.jsp')+'" style="height: 550px;width: 900px; border:0px;"></iframe>'; return;
        case 4: $("rightDetail").innerHTML = '<iframe src="'+urlEncode('../myCreate/myCreate.jsp')+'" style="height: 550px;width: 900px; border:0px;"></iframe>'; return ;
        case 5: $("rightDetail").innerHTML = '<iframe src="'+urlEncode("../../../../meta/module/mag/user/myinfo.jsp")+'" style="height: 420px;width: 900px; border:0px;"></iframe>'; return ;
        case 6: $("rightDetail").innerHTML = '<iframe src="'+urlEncode("../../../../portal/module/portal/modifyPwd.jsp")+'" style="height: 500px;width: 900px; border:0px;"></iframe>'; return ;
    }


};