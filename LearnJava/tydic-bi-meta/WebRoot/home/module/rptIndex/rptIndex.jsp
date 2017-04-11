<%--
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王春生
 * @description 
 * @date 12-4-9
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>首页</title>
    <%@include file="../../public/head.jsp"%>
    <link rel="stylesheet" type="text/css" href="rptIndex.css" />
    <script type="text/javascript" src="<%=rootPath%>/home/resource/js/boxMenu.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/LoginAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/RptIndexAction.js"></script>

    <%--rptIndex.js必须放在第一个--%>
    <script type="text/javascript" src="rptIndex.js"></script>
    <script type="text/javascript" src="rptIndexLeft.js"></script>
    <script type="text/javascript" src="rptIndexCreateNode.js"></script>
    <script type="text/javascript" src="rptIndexLink.js"></script>

</head>
<body style='width:100%;height:100%;'>
<div class="both">
    <div class="header" id="headerDIV">
        <div class="top-1">
            <span class="hyc"><span id="current-user" class="current-user">XXX</span>&nbsp;您好，欢迎来到四川电信自助取数平台！ </span>
            <div class="tip">
                <a class="tips0" href="javascript:void(0);" onclick="linkPersonCenter();">个人中心</a>
                <a class="tips1" href="javascript:void(0);" onclick="linkContactUs();">联系我们</a>
                <a class="tips2" href="javascript:void(0);" onclick="linkFAQ();">FAQ</a>
                <a class="tips3" href="javascript:void(0);" onclick="linkHelp();">帮助</a>
                <a class="tips4" href="javascript:void(0);" onclick="linkLogout();">退出</a>
            </div>
        </div>
        <div class="top-2">
            <div class="logo"></div>
        </div>
    </div>
    <!-- end -->
    <div class="content">
        <div class="left">
            <div id="menuleft" class="menuleft" style="float:left;">
                <ul class="open">
                    <span class="left-bt"><span><span class="ult"></span>我收藏的报表</span><span class="more" onclick="moreMyFav();" title="更多我的收藏"></span></span>
                </ul>
                <ul>
                    <span class="left-bt"><span><span class="ult"></span>我创建的报表</span><span class="more" onclick="moreMyRpt();" title="更多我创建"></span></span>
                </ul>
                <ul>
                    <span class="left-bt"><span><span class="ult"></span>我订阅的报表</span><span class="more" onclick="moreMyPush();" title="更多我的订阅"></span></span>
                </ul>
                <ul>
                    <span class="left-bt"><span><span class="ult"></span>共享给我的报表</span><span class="more" onclick="moreShareToMe();" title="更多共享"></span></span>
                </ul>
                <ul>
                    <span class="left-bt"><span><span class="ult"></span>公共报表</span>
                        <%--<span class="more" onclick="morePublicRpt();" title="更多公共报表"></span>--%>
                    </span>
                </ul>
                <script type="text/javascript">
                    function setTab(name,cursel,n){
                        for(i=1;i<=n;i++){
                            var menu=document.getElementById(name+i);
                            var top_bottom=document.getElementById("top_foot_bottomnet_"+name+"_"+i);
                            menu.className=i==cursel?"hover":"";
                            top_bottom.style.display=i==cursel?"block":"none";
                        }
                    }
                    function settab_mid_top(name,num,n){
                        for(i=1;i<=n;i++){
                            var menu=document.getElementById(name+i);
                            var con=document.getElementById(name+"_"+"mid_top"+i);
                            menu.className=i==num?"on_mid_top":"";
                            con.style.display=i==num?"block":"none";
                        }
                    }
                </script>
                <div style="clear:both"></div>
            </div>
        </div>
        <div class="mid">
            <div class="mid-top">
                <div class="tab">
                    <div style="float:left;width:85%;">
                        <ul id="busTypeUL">
                            <li opend="1" id="oc_btn" class="oc_fileds oc_open" onclick="openOrCloseFields(this);" title="收起/展开"></li>
                        </ul>
                    </div>
                    <div class="top-ssuo">
                        <span class="ssuo">
                            <input id="queryFieldInput" class="huise" type="text"
                                   onkeyup="enterFieldInput()" onblur="resetFieldInput(this,0)" onfocus="resetFieldInput(this,1)" />
                            <a href="javascript:void(0);" onclick="queryFields()" title="点击按关键字搜索指标" class="btn-ssmx"></a>
                        </span>
                    </div>
                </div>
                <div class="list" id="busTypeDIV">
                </div>
                <div style="clear:both;"></div>
                <div class="cond" id="ratPos">
                    <div class="yxzb2">
                        <span class="mid-btt">
                            <span>已选指标</span>
                            <img class="clearImg" src="../../resource/images/orange/refresh.gif" onclick="clearSelectedFields()" title="清除选择的指标"/>
                        </span>
                        <div class="xzzb" id="selectedFieldDIV"></div>
                    </div>
                    <div class="mid-px">
                        <span class="mid-bt">排序方式</span>
                        <span class="mid-cx" id="od-INS_CNT" onclick="changeOrderMode('INS_CNT')" title="根据[应用数]排序">应用数</span>
                        <span class="mid-hs" id="od-DIM_CNT" onclick="changeOrderMode('DIM_CNT')" title="根据[维度数量]排序">维度数</span>
                        <span class="mid-hx" id="od-CREATE_TIME" onclick="changeOrderMode('CREATE_TIME')" title="根据[创建时间]排序">创建时间</span>
                        <span class="search">
                            <input id="queryModelInput" class="huise" type="text" onkeyup="enterModelInput()" onblur="resetModelInput(this,0)" onfocus="resetModelInput(this,1)" />
                        </span>
                        <span class="mid-bt1" id="totalNumDesc"></span>
                    </div>
                    <div class="kwd">
                        <span class="sousuoimg" title="点击按选择指标和关键字搜索" onclick="queryModelsAndApproximateRpt()">搜&nbsp;索</span>
                    </div>
                </div>

            </div>
            <div style="clear:both;"></div>
            <div class="mid-foot">
                <div class="mx">
                    <div id="modelsContDIV"></div>
                    <div class="more" id="moreModelDIV" style="display: none">
                        <span id="moreModelNum"></span>
                        <a href="javascript:void(0);" onclick="queryNextPageModel()">查看更多信息↓</a>
                    </div>
                </div>

                <div class="xs" id="approximateDIV" style="visibility:hidden;">
                    <span class="lt"></span><span class="rt"></span><span class="lb"></span><span class="rb"></span>
                    <span class="bbbt">相似报表</span>
                    <ul class="mid-bb" id="approximateContDIV">
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <!-- end -->
</div>
</body>
</html>