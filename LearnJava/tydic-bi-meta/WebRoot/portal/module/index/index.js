/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        index.js
 *Description：
 *       主页JS文件，实现主页功能
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        张伟
 *Finished：
 *       2011-09-11-9-23
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/

/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user=getSessionAttribute("user");
var dwrCaller=new biDwrCaller();
//当前系统的主页Path
var base = getBasePath();
//根级菜单数据
var rootMenuData=null;
var frameLoadSucess=false;
//添加根菜单访问Action
dwrCaller.addAutoAction("queryRootMenu","LoginAction.queryRootMenu",getSystemId());
//dwrCaller.addDataConverter("queryRootMenu",new columnNameConverter());
var indexInit=function(){
    var indexLayout = new dhtmlXLayoutObject(document.body, "2E");
    //主页导航栏layout
    indexLayout.cells("a").hideHeader(true);
    indexLayout.cells("a").setHeight(88);
    indexLayout.cells("a").fixSize(true,true);

    //添加导航栏设置。
    indexLayout.cells("a").attachObject($("north"));

    //主页菜单栏layout
//    indexLayout.cells("b").fixSize(true,true);
    //菜单栏设置
//    indexLayout.cells("b").attachObject(dhx.html.create("div",{id:"menuTreeBox",style:"width:200;height:100%"}));

    //内容layout
    indexLayout.cells("b").fixSize(true,true);
    indexLayout.cells("b").hideHeader(true);
    //添加一个内容iframe
    var iframe = document.createElement("iframe");
    iframe.name="main";
    iframe.id= "main" ;
    iframe.style.width = "100%";
    iframe.style.height = "100%";
    iframe.frameBorder=0;
    iframe.scrolling="no";
    iframe.src=urlEncode("main.jsp");
    if (iframe.attachEvent){
        iframe.attachEvent("onload", function(){
            frameLoadSucess=true;//子窗体加载成功
        });
    } else {
        iframe.onload = function(){
            frameLoadSucess=true;
        };
    }
    indexLayout.cells("b").attachObject(iframe);
    indexLayout.hideConcentrate();

    //执行Action 访问根节点
    dwrCaller.executeAction("queryRootMenu",function(data){
        //交由子窗口处理进行隐藏菜单筛选操作。
        if(frameLoadSucess){
            data= window.frames["main"].window["filterHiddenMenu"](data); //过滤隐藏菜单。
        }else{
            var arg=arguments;
            setTimeout(function(){arg.callee.call(this,data)},10);
            return;
        }
        if(data){
            data=dhx.isArray(data)?data:[data];
            rootMenuData=data;
            var str="";
            for(var i=0;i<data.length;i++){
                if(i==0){
                    str+='<li><a href="javascript:menuChange('+i+');"><img src="'+base+data[i].iconUrl+'" align="middle" />'+
                         data[i].menuName+'</a></li>';
                    //加载第一个菜单数据
                    menuChange(i);
                }else{
                    str+='<li><a href="javascript:menuChange('+i+');"><img src="'+base+data[i].iconUrl+'" align="middle" />'+
                         data[i].menuName+'</a></li>';
                }
            }
            $("navUl").innerHTML= str;
            delete str;
        }
        navInit();
    });

    //导航栏工具条设置
}

/**
 * 注册logout Action
 */
dwrCaller.addAutoAction("logout","LoginAction.logout",function(){
    window.location=getBasePath();
});


var logout=function(){
    dhx.confirm("您确定要退出系统吗?",function(r){
        if(r){
            //发送退出消息，清空session，并重定向页面
            dwrCaller.executeAction("logout");
        }
    });
}
var loadOperMan = function(){
	window.open("../../resource/other/manual.doc","blank");
}
var i;
var changePassword=function(){
    var date = new Date();
    i=0;
    if(frameLoadSucess){
        window.frames["main"].window["openMenu"]("修改密码","/portal/module/portal/modifyPwd.jsp?"+date.getTime(),"top");
    }else{
        if(i++ >5){
            dhx.alert("此功能异常，请联系管理员");
            return;
        };
        setTimeout(arguments.callee,50);
    }
    delete date;
}

var openAppRank=function(){
     var date = new Date();
     i=0;
     if(frameLoadSucess){
        window.frames["main"].window["openMenu"]("应用排名","/meta/module/mag/log/menuVisitInfo.jsp?groupId=20&"+date.getTime(),"top");
    }else{
        if(i++ >5){
            dhx.alert("此功能异常，请联系管理员");
            return;
        };
        setTimeout(arguments.callee,50);
    }
    delete date;
}

var openLogRep=function(){
    var date = new Date();
    i=0;
     if(frameLoadSucess){
        window.frames["main"].window["openMenu"]("访问统计","meta/module/mag/log/logreports.jsp?groupId=20&"+date.getTime(),"top");
    }else{
         if(i++ >5){
            dhx.alert("此功能异常，请联系管理员");
            return;
        };
        setTimeout(arguments.callee,50);
    }
}

var openVisitRank=function(){
    var date = new Date();
    i=0;
     if(frameLoadSucess){
        window.frames["main"].window["openMenu"]("访问排名","/meta/module/mag/log/loginLog.jsp?groupId=20&"+date.getTime(),"top");
    }else{
         if(i++ >5){
            dhx.alert("此功能异常，请联系管理员");
            return;
        };
        setTimeout(arguments.callee,50);
    }
    delete date;
}

dwrCaller.addAutoAction("changeSystem","LoginAction.changeSystem");
var systemChange = function(){
    window.location.href="index.jsp?systemId="+document.getElementById('systemSelect').value;
    //访问DWr，记录切换系统系统
    dwrCaller.executeAction("changeSystem");
}

/**
 * 切换menu
 * @param menuId
 */
var menuChange=function(menuIndex){
    i=0;
    var menuData=rootMenuData[menuIndex];
    if(menuIndex==0){//如果是第一个导航菜单，默认其为tab模式
        menuData.target="top";
    }
    //交由子窗口处理进行菜单打开操作。
    if(frameLoadSucess){
        window.frames["main"].window["menuClick"](menuData.menuId,menuData);
    }else{
        if(i++ >5){
            dhx.alert("此功能异常，请联系管理员");
            return;
        };
        setTimeout("menuChange("+menuIndex+")",50);
    }
}

var nav=function(c, config){
    this.config = config || {speed: 10, num: 2};
    this.container = (typeof(c)=="object") ? c : document.getElementById(c);
    this.lineHeight = this.container.offsetHeight;
    this.scrollTimeId = null;
    var _this = this;
    this.__construct = function (){
        var inner,el,href;
        inner = _this.container.childNodes[0].innerHTML;
        href = _this.container.childNodes[0].href;
        el = document.createElement("a");
//        el.innerHTML = inner;
        el.href = href;
        el.className = 'hover';
        if(!dhx.env.isIE){
            el.style.width=_this.container.offsetWidth+'px';
        }
        _this.container.appendChild(el);
        //注册事件
        _this.container.onmouseover = function (){_this.start()};
        _this.container.onmouseout  = function (){_this.end()};
        _this.container.onclick = function (){_this.toggle()};
    }();
    this.toggle = function(){
        for(var i=0;i<$("navUl").childNodes.length;i++){
            dwr.util.removeClassName($("navUl").childNodes[i],"on");
        }
        var target=_this.container;
        dwr.util.addClassName(target,'on');
        navInit();
    };
    this.start = function (){
        _this.clear();
        _this.scrollTimeId = setTimeout(function(){_this.scrollUp();}, _this.config.speed);
    };
    this.end = function (){
        _this.clear();
        _this.scrollTimeId = setTimeout(function(){_this.scrollDown();}, _this.config.speed);
    };
    this.scrollUp = function (){
        var c = _this.container;
        if(c.scrollTop >= _this.lineHeight){c.scrollTop = _this.lineHeight;return;}
        c.scrollTop += _this.config.num;
        _this.scrollTimeId = setTimeout(function(){_this.scrollUp();}, _this.config.speed);
    };
    this.scrollDown = function (){
        var c = _this.container;
        if(c.scrollTop <= 0){c.scrollTop = 0;return;}
        c.scrollTop -= _this.config.num;
        _this.scrollTimeId = setTimeout(function(){_this.scrollDown();}, _this.config.speed);
    };
    this.clear = function (){clearTimeout(_this.scrollTimeId)};
}
var navInit=function(){
    for(var i=0;i<$("navUl").childNodes.length;i++){
        if($("navUl").childNodes[i].nodeName.toLowerCase()!="#text"){
            new nav($("navUl").childNodes[i],{speed:10,num:4});
        }
    }
}


//window.onload=indexInit;
dhx.ready(indexInit);