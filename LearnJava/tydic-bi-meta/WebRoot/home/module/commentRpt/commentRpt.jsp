<%--
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王春生
 * @description 
 * @date 12-4-17
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>打开报表</title>
    <%@include file="../../public/head.jsp"%>
    <link rel="stylesheet" type="text/css" href="commentRpt.css" />
    <style type="text/css">
        #openRptFrame{
            height:100%;
            width:100%;
        }
    </style>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=PERORT_SEND_TYPE"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/CommentRptAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/FavoriteAction.js"></script>
    <script type="text/javascript">
        <%
            String rptId = request.getParameter("rptId");
            if(rptId==null || "".equals(rptId))
                rptId = request.getParameter("reportId");
            if(rptId==null || "".equals(rptId))
                rptId = request.getParameter("report_id");
            if(rptId==null || "".equals(rptId))
                rptId = request.getParameter("reportID");

            out.println("var rptId = "+rptId+";");
        %>
    </script>

    <script type="text/javascript" src="commentRpt.js"></script>
    <script type="text/javascript" src="commentRptCreateNode.js"></script>
    <script type="text/javascript" src="commentRptLink.js"></script>
    <script type="text/javascript" src="favPush.js"></script>
</head>
<body style='width:100%;height:100%;'>
<div class="both">
    <div class="header" id="headerDIV">
        <div class="top-1">
            <span class="hyc" id="rptInfo">
                <span class="rpt_user">张三</span>&nbsp;创建于<span class="rpt_time">2012-02-03 12:23</span>
            </span>
            <span class="comtotal" id="comtotalSPAN">
                <span>0条评论信息，总体评分:</span>
                <span class="com_grade" id="com_grade">
                    <span class="gradenum"></span>
                    <span class="gradespan">
                        <%--<span class="gradea grade_on" id="grade1" onclick="grade(1);">★</span>--%>
                        <%--<span class="gradea grade_out" id="grade2" onclick="grade(2);">☆</span>--%>
                        <%--<span class="gradea grade_out" id="grade3" onclick="grade(3);">☆</span>--%>
                        <%--<span class="gradea grade_out" id="grade4" onclick="grade(4);">☆</span>--%>
                        <%--<span class="gradea grade_out" id="grade5" onclick="grade(5);">☆</span>--%>
                    </span>
                    <span id="grade_des" class="grade_des"></span>
                </span>
            </span>
            <div class="tip" id="rpt_opt_div">
                <%--<a class="tips0" href="javascript:void(0);" onclick="linkRptIndex();">首页</a>--%>
                <%--<a class="tips1" href="javascript:void(0);" onclick="linkContactUs();">联系我们</a>--%>
                <%--<a class="tips2" href="javascript:void(0);" onclick="linkFAQ();">FAQ</a>--%>
                <%--<a class="tips3" href="javascript:void(0);" onclick="linkHelp();">帮助</a>--%>
                <%--<a class="tips4" href="javascript:void(0);" onclick="linkLogout();">退出</ a>--%>
                <a class="fav" href="javascript:void(0);" onclick="showFavPushRpt();">收藏订阅</a>
                <a class="copy" href="javascript:void(0);" onclick="copyRpt();">复制报表</a>
                <a class="modify" id="modifyRptA" style="display: none" href="javascript:void(0);" onclick="modifyRpt();">修改报表</a>
            </div>
            <span class="oc_span"><img title="展开/收起 评论区" id="openCloseImg" class="close" alt="" onclick="openOrCloseComment()"></span>
        </div>
    </div>
    <div class="content">
        <div class="rpt-content" id="rpt-content">
            <iframe id="openRptFrame" marginwidth="0"
                    marginheight="0" frameborder="0" scrolling="none"></iframe>
        </div>
        <%--<div class="openCommentBtn" id="openBtn" title="展开/收起 评论区"><img onclick="openOrCloseComment()" class="right" alt=""></div>--%>
        <div class="rpt-comment" id="rpt-comment">
            <div class="commentdiv">
                <span class="cominfo">评论区</span>
                <span class="comtext" id="com_span">
                    <textarea class="zhc" id="pl_txt"></textarea>
                    <span class="start">
                        <input type="button" class="poster_btn" onclick="submitComment()" value="评价"></span>
                    <div style="clear:both;"></div>
                </span>
                <ul class="infoul" id="commentUL">
                    <li class="info">
                        <span class="info_user">谭洪涛</span>
                        <span class="info_content">非常感谢！这张报表对我部非常有用！非常感谢！这张报表对我部非常有用！</span>
                        <div class="info_hfd">
                            <span class="info_time">2012-03-03 12:12:21</span>
                        </div>
                        <div style="clear:both;position:relative;">
                            <span class="info_a_hf"><a class="pln" href="javascript:void(0);" id="hf_a_0" onclick="openOrCloseSubComment(0)" title="回复"></a></span>
                        </div>
                        <div class="subdiv" id="subDIV0">
                            <ul class="subul" id="replyUL0">
                                <li class="subinfo">
                                    <span class="sub_user">XX</span>
                                    <span class="sub_content">asdfasdf</span>
                                    <span class="sub_time">2012-03-03 12:12:21</span>
                                </li>
                            </ul>
                            <div class="morehf">
                                <span id="reply_more_0" class="replymore" onclick="moreReplyComments(0)">更早回复↓</span>
                                <span class="sub_hf_btn" id="hf_btn_span0">
                                    <input type="button" value="我也说一句" onclick="openOrCloseSubCommentText()"></span>
                                <span class="sub_hf" id="hf_txt_span0">
                                    <textarea class="huise" id="hf_txt0"></textarea>
                                    <input type="button" value="回复" onclick="submitSubComment()"/>
                                    <input type="button" value="取消" onclick="openOrCloseSubCommentText()"></span>
                            </div>
                        </div>
                    </li>
                </ul>
                <span id="com_more" class="commore" onclick="moreComments()">更早评论↓</span>
            </div>
        </div>
    </div>
</div>
<div class="touming" id="touming"></div>
<div class="fav" id="favDIV">
    <span class="favtitle">&nbsp;收藏订阅<span class="favclose" title="关闭" onclick="hideFavPushRpt()"></span></span>
    <table width='100%' height="100%" border='0' cellpadding='0' cellspacing='0'
           style="position: relative;table-layout: fixed;">
        <colgroup>
            <col width="25%"/>
            <col width="75%"/>
            </colgroup>
        <tr style="height:16px;">
            <td style="text-align:center;color:#0000AA;border-bottom:1px #D0E5FF solid;" colspan="2" id="favtitle"></td>
        </tr>
        <tr>
            <td class="leftDim">操作:</td>
            <td class="rightTd">
                <span id="os0" style="display: none"><input type="checkbox" id="opt0" name="opt" onclick="clickOptSet(this,0)" value="0"/>收藏</span>
                <span id="os1" style="display: none"><input type="checkbox" id="opt1" name="opt" onclick="clickOptSet(this,1)" value="1"/>取消收藏</span>
                <span id="os2" style="display: none"><input type="checkbox" id="opt2" name="opt" onclick="clickOptSet(this,2)" value="2"/>订阅</span>
                <span id="os3" style="display: none"><input type="checkbox" id="opt3" name="opt" onclick="clickOptSet(this,3)" value="3"/>取消订阅</span>
                <span id="os4" style="display: none"><input type="checkbox" id="opt4" name="opt" onclick="clickOptSet(this,4)" value="4"/>修改</span>
            </td>
        </tr>
        <tr>
            <td class="leftDim">收藏分类:</td>
            <td class="rightTd">
                <input type="hidden" id="favTypeId" name="favTypeId"/>
                <input type="text" style="width:207px;height:18px; position: relative;" id="favTypeName" name="favTypeName"/>
                </td>
        </tr>
        <tbody id="pushinfo" style="display:none;">
            <tr>
                <td class="leftDim">订阅方式</td>
                <td class="rightTd">
                    <span id="fs1" style="display: none"><input type="checkbox" name="sendMethod1" id="sendMethod1" value="1"/>邮件</span>
                    <span id="fs2" style="display: none"><input type="checkbox" name="sendMethod2" id="sendMethod2" value="2"/>彩信</span>
                    <span id="fs3" style="display: none"><input type="checkbox" name="sendMethod3" id="sendMethod3" value="3"/>短信</span>
                </td>
            </tr>
            <tr>
                <td class="leftDim">定时发送<input type="radio" onclick="clickSendFlag(this)" id="typeFlag1" name="typeFlag" value="1"></td>
                <td class="rightTd" id="dstd">
                    <span>发送频度:</span>
                    <span><select style="width:154px;height: 23px;" name="sendSequnce" id="sendSequnce"></select></span>
                    <div style="clear:both;height:5px;"></div>
                    <span>初发时间:</span>
                    <input type="text" style="width:150px;height:18px;" readonly="readonly" id="sendTime" name="sendTime"></td>
            </tr>
            <tr>
                <td class="leftDim">固定发送<input type="radio" onclick="clickSendFlag(this)" id="typeFlag2"  name="typeFlag" value="2"></td>
                <td class="rightTd" id="gdtd">
                    <span>发送时间:</span>
                    <span><input type="text" style="width:150px;height:18px;" readonly="readonly" id="fixedTime" name="fixedTime"></span>
                </td>
            </tr>
        </tbody>
        <tr>
            <td colspan="2" style="text-align: center;padding-top:10px;">
                <input type='button' class="poster_btn" onclick="submitFavOpt()" value='提交'/>
                <input type='button' class="poster_btn" onclick="hideFavPushRpt()" value='取消'/>
            </td>
        </tr>
    </table>
</div>
</body>
</html>