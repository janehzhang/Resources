<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@include file="../../../../meta/public/head.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="zh-cn" lang="zh-cn">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>个人中心</title>
    <link href="../../rptIndex/rptIndex.css" rel="stylesheet" type="text/css" />
    <link href="user.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript">
        var base = getBasePath();
        var user = getSessionAttribute("user");
        function settab_user(name,num,n){
            for(i=1;i<=n;i++){
                var menu=document.getElementById(name+i);
                var con=document.getElementById(name+"_"+"user"+i);
                menu.className=i==num?"on_user":"";
                con.style.display=i==num?"block":"none";
            }
        }
        function changeColor(obj) {
            var	lis= document.getElementsByName("changeColor");
            for(var i=0;i<lis.length;i++) {
                lis[i].className = "";
            }
            obj.className = 'current';
        }
    </script>
    <script type="text/javascript" src="myCenter.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/LoginAction.js"></script>
    <script type="text/javascript" src="../../../resource/js/home_common.js"></script>
</head>
<body onload="initFun('<%=request.getParameter("openli")%>')" style="overflow: auto;">
<div class="both">
    <div class="header">
        <div class="top-1">
            <span class="hyc"> <span id="current-user" class="current-user">XXX</span>&nbsp;您好，欢迎来到四川电信自助取数平台！ </span>
            <div class="tip">
                <a class="tips5" href="javascript:void(0);" onclick="linkRptIndex();">返回首页</a>
                <a class="tips1" href="javascript:void(0);" onclick="linkContactUs();">联系我们</a>
                <a class="tips2" href="javascript:void(0);" onclick="linkFAQ();">FAQ</a>
                <a class="tips3" href="javascript:void(0);" onclick="linkHelp();">帮助</a>
                <a class="tips4" href="javascript:void(0);" onclick="linkLogout();">退出</a>
            </div>
        </div>
        <div style="clear:both"></div>
        <div class="top-2">
            <div class="logo"></div>
        </div>
    </div>

    <div class="content">
        <div class="left" style="width:200px;margin-right: 5px;">
            <div class="menuleft1" style="float:left;border:1px solid #a7bed0;">
                <ul class="open">
                    <span class="left-btt">个人中心 <img src="img/left-xl1.png" width="12" height="12" /><img class="user-posi" src="img/user.png" width="20" height="20" /></span>
                    <li><a class="user-h" href="javascript:changeRightFrame(1)" name="changeColor" id="myRecent"  onclick="changeColor(this)"><img src="img/left-jt1.png" width="12" height="12" />我最近做过</a></li>
                    <li><a class="user-h" href="javascript:changeRightFrame(2)" name="changeColor" id="myFavorite" onclick="changeColor(this)"><img src="img/left-jt1.png" width="12" height="12" />我的收藏</a></li>
                    <li><a class="user-h" href="javascript:changeRightFrame(3)" name="changeColor" id="myShare" onclick="changeColor(this)"><img src="img/left-jt1.png" width="12" height="12" />别人共享给我</a></li>
                    <li><a class="user-h" href="javascript:changeRightFrame(4)" name="changeColor" id="myCreate" onclick="changeColor(this)"><img src="img/left-jt1.png" width="12" height="12" />我的创建</a></li>
                    <li><a class="user-h" href="javascript:changeRightFrame(5)" name="changeColor" id="myinfo" onclick="changeColor(this)"><img src="img/left-jt1.png" width="12" height="12" />个人资料</a></li>
                    <li><a class="user-h" href="javascript:changeRightFrame(6)" name="changeColor" id="modifyPwd" onclick="changeColor(this)"><img src="img/left-jt1.png" width="12" height="12" />修改密码</a></li>
                </ul>
                <div style="clear:both"></div>
            </div>
        </div>

        <div class="mid-user">
            <div id="rightDetail"></div>
        </div>
    </div>
</div>
</body>
</html>