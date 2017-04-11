/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        commentRptCreateNode.js
 *Description：
 *        评论报表页面，创建或设置  HTML元素代码的JS
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

//创建一条评论
var createComment = function(data){
    var li = document.createElement("LI");
    var hf = data.newReply>0 ? ("回复(新"+data.newReply+")") : "回复";
    li.className = "info";
    li.innerHTML = "<span class='info_user'>"+data.userName+":</span>"+
        "<span class='info_content'>"+data.content.replaceAll("\n","<br>")+"</span>"+
        "<div class='info_hfd'>"+
            "<span class='info_time'>"+data.comTime+"</span> "+
        "</div>"+
        "<div style='clear:both;position:relative;'>" +
            "<span class='info_a_hf'>" +
                "<span class='pln"+(data.newReply>0?" a_close":"")+"' id='hf_a_"+data.commentId+"' onclick='openOrCloseSubComment("+data.commentId+")'>"+hf+"</span>" +
            "</span> "+
        "</div>"+
        "<div class='subdiv' id='subDIV"+data.commentId+"'>"+
            "<ul class='subul' id='replyUL"+data.commentId+"'></ul>"+
            "<div class='morehf'>"+
                "<span id='reply_more_"+data.commentId+"' class='replymore' onclick='moreReplyComments("+data.commentId+")'>更早回复↓</span>"+
                "<span class='sub_hf_btn' id='hf_btn_span"+data.commentId+"'>" +
                    "<input type='button' class='poster_btn' value='我也说一句' onclick='openOrCloseSubCommentText()'>" +
                "</span>" +
                "<span class='sub_hf' id='hf_txt_span"+data.commentId+"'>" +
                    "<textarea class='huise' id='hf_txt"+data.commentId+"'></textarea>" +
                    "<input type='button' class='poster_btn' value='回复' onclick='submitSubComment()'>" +
                    "<input type='button' class='poster_btn' value='取消' onclick='openOrCloseSubCommentText()'>" +
                "</span>" +
            "</div>"+
        "</div>"+
        "<div style='clear:both;'></div>";
    return li;
};

//创建一个回复
var createReplyComment = function(data){
    var li = document.createElement("LI");
    li.className = "subinfo";
    li.innerHTML = "<span class='sub_user'>"+data.userName+":</span>"+
        "<span class='sub_content'>"+data.content.replaceAll("\n","<br>")+"</span>"+
        "<span class='sub_time'>"+data.comTime+"</span>";
    return li;
};

//创建一个标示无记录的LI
var createNoDataLi = function(str){
    var li = document.createElement("LI");
    li.className = "nodata";
    li.innerHTML = str || "无记录";
    return li;
};

/**
 * 设置报表评论信息和分数
 */
var setRptCommentTotalSPAN = function(){
    var comtotalSPAN = document.getElementById("comtotalSPAN");
    var title = (rptUser_VAL==user.userId )?"自己创建的报表无法打分":(gradeNum_VAL>0?"今日你已打过分":"每人每日可打分一次");
    var com_grade =
        "<span class='com_grade'>" +
            "<span class='gradenum' title='"+(avgGrade_VAL>0?("得分:"+avgGrade_VAL):"暂无评分")+"'>"+(avgGrade_VAL>0?avgGrade_VAL:"--")+"</span>" +
            "<span class='gradespan' title='"+title+"' id='gradespan'>"+
            "</span>" +
            "<span id='grade_des'></span>"+
        "</span>";
    comtotalSPAN.innerHTML = "<span><span style='font-size:14px;color:#facfbc;font-weight:bold;margin-right:5px;'>"+comTotal_VAL+"</span>条评论信息，总体评分:</span>"+com_grade;
    createGradeSPAN();
};

//创建评分区域
var createGradeSPAN = function(score){
    score = score||avgGrade_VAL;
    var gradespan = document.getElementById("gradespan");
    gradespan.innerHTML = "";
    for(var i=1;i<=5;i++){
        var sp = document.createElement("SPAN");
        if(score<i){
            if(score>(i-1)){
                sp.className = "grade grade_half";
            }else{
                sp.className = "grade grade_out";
            }
        }else{
            sp.className = "grade grade_on";
        }
        sp.id = "grade_"+i;
        sp.score = i;
        if(rptUser_VAL!=user.userId && !gradeNum_VAL){
            sp.onmouseover = function(){
                overGrade(this.score);
            };
            sp.onmouseout = function(){
                outGrade();
            };
            sp.onclick = function(){
                grade(this.score);
            };
        }
        gradespan.appendChild(sp);
    }
};

/**
 * 设置报表信息
 * @param data
 */
var setRptInfo = function(data){
    document.getElementById("rptInfo").innerHTML = "<span class='rpt_user'>"+data['USER_NAME']+"</span>" +
        "&nbsp;创建于<span class='rpt_time'>"+data['CREATE_TIME']+"</span>";
};