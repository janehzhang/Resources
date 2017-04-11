/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        commentRpt.js
 *Description：
 *        打开评论报表JS，其主要实现评论报表的功能，打开报表逻辑以iframe的形式嵌入
 *        JS代码结构从上到下依次：全局变量，初始界面，访问提交后台，界面控制，超链接（如果有）
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var user = getSessionAttribute("user");

//全局变量
var rptId_VAL = rptId;              //报表ID（rptId 由JSP输出）
var graded_VAL = 0;                 //当前人是否评分次数（默认为0，初始时会动态改变其值）
var commentDivOpend_VAL = null;     //评论区是否打开
var openCommentId_VAL = null;          //记住当前打开的评论回复
var comPageStart_VAL = 0;       //评论起始页
var comPageSize_VAL = 10;       //评论一页大小
var comTotal_VAL = 0;           //记录评论总数
var avgGrade_VAL = 0;               //报表平均分
var gradeNum_VAL = 0;               //设置当前评分次数
var replyPageSize_VAL = 5;            //回复一页大小
var replyPageStart_VAL = {};        //记录每个评论的回复起始页
var replyTotal_VAL = {};            //记录每个评论下回复总数
var rptUser_VAL = 0;        //报表打开权限
var rptAuditType_VAL = 0;   //数据类型
var rptSubscribeType_VAL = null;  //支持订阅方式
var rptFavInfo_VAL = null;     //收藏订阅初始信息
var rptFavTree_VAL = null;     //收藏分类树



//初始界面
var initThisPage = function () {
    if(!rptId_VAL){
        alert("无报表参数，打开报表失败！");
        window.close();
    }else{
        window.onresize = function(){
            commentDivOpend_VAL = !commentDivOpend_VAL;
            openOrCloseComment();
        };

        initRptInfo();//获取报表基本信息，以及验证是否有权限，如果获取到则加载评论信息
    }
};

//获取报表信息
var initRptInfo = function(){
    CommentRptAction.getRptInfo(rptId_VAL,function(data){
        if(data){
            avgGrade_VAL = data["avgGrade"];
            gradeNum_VAL = data["gradeNum"];
            rptAuditType_VAL = data["AUDIT_TYPE"];
            rptSubscribeType_VAL = data["SUBSCRIBE_TYPE"];
            setRptInfo(data);
            rptUser_VAL = data["USER_ID"];
            if(rptUser_VAL==user.userId){
                document.getElementById("modifyRptA").style.display = "block";
//                document.getElementById("com_span").style.display = "none";
            }

            if(data["currentUserAuth"]){
                document.getElementById("headerDIV").style.display = "block";
                openOrCloseComment();
                //子框架加载打开报表
                var initTypePar = (data["DESIGNER_RPT_ID"]==rptId_VAL ? "" : "&initType=2") ;//如果经过设计则不穿此参数
                document.getElementById("openRptFrame").src = urlEncode(base+"/meta/public/reportDesign/report.jsp?rptId="+rptId_VAL+initTypePar);

                recordOpenRptLog();//打开日志
                //初始评论评分信息
                clearComments();
                queryCommentList();
            }else{
                alert("此报表你无权限打开！\n只有【公共的，你创建的，你收藏的，别人共享给你的】此四类报表才有权限打开.");
                window.close();
            }
        }else{
            alert("报表不存在，或已下线！");
            window.close();
        }
    });
};

//查询评论
var queryCommentList = function(){
    var commentUL = document.getElementById("commentUL");
    CommentRptAction.queryCommentsInfo(rptId_VAL,comPageStart_VAL,comPageSize_VAL,function(data){
        if(data && data.length){
            comTotal_VAL = data[0]["TOTAL_COUNT_"];
            for(var i=0;i<data.length;i++){
                replyPageStart_VAL[data[i]["COMMENT_ID"]] = 0;
                commentUL.appendChild(createComment({
                    snNum:i+1,
                    commentId:data[i]["COMMENT_ID"],
                    userId:data[i]["USER_ID"],
                    content:data[i]["COMMENT_CONTEXT"],
                    comTime:data[i]["COMMENT_TIME"],
                    parentId:data[i]["PAR_COMMENT_ID"],
                    newReply:data[i]["NEW_REPLY"],
                    userName:data[i]["USER_NAMECN"]
                }));
            }

            if((comPageSize_VAL+comPageStart_VAL) >= comTotal_VAL){
                document.getElementById("com_more").style.display = "none";
            }else{
                document.getElementById("com_more").style.display = "block";
                document.getElementById("com_more").innerHTML = "共"+comTotal_VAL+"(更早评论↓)";
            }
        }else{
            document.getElementById("com_more").style.display = "none";
            commentUL.appendChild(createNoDataLi("暂时无人评论!"));
//            if(rptUser_VAL==user.userId){
//                openOrCloseComment();
//            }
            //无评论信息
        }
        setRptCommentTotalSPAN();
    });
};

//查询某评论下回复信息
var queryReplyComments = function(commentId){
    var replyUL = document.getElementById("replyUL"+commentId);
    CommentRptAction.queryReplyComments(rptId_VAL,commentId,replyPageStart_VAL[commentId],replyPageSize_VAL,function(data){
        var hf_btn_span = document.getElementById("hf_btn_span"+commentId);
        var hf_txt_span = document.getElementById("hf_txt_span"+commentId);
        replyUL.style.display = "block";
        if(data && data.length){
            replyTotal_VAL[commentId] = data[0]["TOTAL_COUNT_"];
            for(var i=0;i<data.length;i++){
                var li = createReplyComment({
                    snNum:i+1,
                    commentId:data[i]["COMMENT_ID"],
                    userId:data[i]["USER_ID"],
                    content:data[i]["COMMENT_CONTEXT"],
                    comTime:data[i]["COMMENT_TIME"],
                    parentId:data[i]["PAR_COMMENT_ID"],
                    userName:data[i]["USER_NAMECN"]
                });
                replyUL.appendChild(li);
            }
            hf_txt_span.style.display = "none";
            hf_btn_span.style.display = "block";

            if((replyPageStart_VAL[commentId]+replyPageSize_VAL) >= replyTotal_VAL[commentId]){
                document.getElementById("reply_more_"+commentId).style.display = "none";
            }else{
                document.getElementById("reply_more_"+commentId).style.display = "block";
                document.getElementById("reply_more_"+commentId).innerHTML = "共"+replyTotal_VAL[commentId]+"(更早回复↓)";
            }
        }else{
            document.getElementById("reply_more_"+commentId).style.display = "none";
            replyTotal_VAL[commentId] = 0;
            //无回复
            replyUL.appendChild(createNoDataLi());
            hf_btn_span.style.display = "none";
            hf_txt_span.style.display = "block";
        }
    });
};



/*******其上初始界面；其下访问提交后台*********/

//记录报表打开日志
var recordOpenRptLog = function(){
    CommentRptAction.recordOpenRptLog(rptId_VAL);
};

//提交评论
var submitComment = function(){
    var pl_txt = document.getElementById("pl_txt");
    var str = trim((pl_txt.value || pl_txt.innerHTML));
    if(str!=""){
        pl_txt.innerHTML = "";
        comPageStart_VAL = 0;
        CommentRptAction.commentRpt(rptId_VAL,str,function(ret){
            if(ret==-1){
                alert("评论出错，请重试!");
            }else{
                clearComments();
                queryCommentList();
            }
        });
    }else{
        alert("请输入评论信息");
    }
};

//提交回复
var submitSubComment = function(){
    var hf_txt = document.getElementById("hf_txt"+openCommentId_VAL);
    var str = trim((hf_txt.value || hf_txt.innerHTML));
    if(str!=""){
        hf_txt.innerHTML = "";
        replyPageStart_VAL[openCommentId_VAL] = 0;
        CommentRptAction.replyCommentRpt(rptId_VAL,openCommentId_VAL,str,function(ret){
            if(ret==-1){
                alert("回复出错，请重试!");
            }else{
                clearReplyComments(openCommentId_VAL);
                queryReplyComments(openCommentId_VAL);
            }
        });
    }else{
        alert("请输入回复信息");
    }
};

//设置评论下回复已读
var setReplyRead = function(el){
    CommentRptAction.setReplyRead(openCommentId_VAL,function(ret){
        if(ret==-1){
            alert("标记回复已读出错，请重试!");
        }else{
            el.innerHTML = "收起回复";
            el.className = "pln a_open";
        }
    });
};

/*******其上访问提交后台；其下界面交互控制*********/

//打分
var grade = function(num){
    if(!gradeNum_VAL){
        gradeNum_VAL = 1;
        CommentRptAction.gradeRpt(rptId_VAL,num,function(data){
            if(data>0){
                avgGrade_VAL = data;
                setRptCommentTotalSPAN();
            }else{
                gradeNum_VAL = 0;
                alert("报表打分失败!");
            }
        });
    }
};
//鼠标移入五星
var overGrade = function(num){
    if(!gradeNum_VAL){
        for(var i=1;i<=5;i++){
            var sp = document.getElementById("grade_"+i);
            if(num<i){
                sp.className = "grade grade_out";
            }else{
                sp.className = "grade grade_on";
            }
        }
        var des = "";
        switch (num){
            case 1 :
                des = "很差";
                break;
            case 2:
                des = "较差";
                break;
            case 3:
                des = "还行";
                break;
            case 4:
                des = "推荐";
                break;
            case 5:
                des = "力荐";
                break;
        }
        document.getElementById("grade_des").innerHTML = des;
    }
};
//鼠标移除五星
var outGrade = function(){
    if(!gradeNum_VAL){
        createGradeSPAN();
        document.getElementById("grade_des").innerHTML = "";
    }
};

//更多评论
var moreComments = function(){
    comPageStart_VAL += comPageSize_VAL;
    queryCommentList();
};
//更多回复
var moreReplyComments = function(commentId){
    replyPageStart_VAL[commentId] += replyPageSize_VAL;
    queryReplyComments(commentId);
};

//清空评论
var clearComments = function(){
    var commentUL = document.getElementById("commentUL");
    openCommentId_VAL = null;
    commentUL.innerHTML = "";
    comPageStart_VAL = 0;
    comTotal_VAL = 0;
    replyPageStart_VAL = {};
    replyTotal_VAL = {};
};
//清空某评论回复
var clearReplyComments = function(commentId){
    var replyUL = document.getElementById("replyUL"+commentId);
    replyUL.innerHTML = "";
    replyPageStart_VAL[commentId] = 0;
    replyTotal_VAL[commentId] = 0;
};

//打开关闭评论区
var openOrCloseComment = function(ph){
    commentDivOpend_VAL = !commentDivOpend_VAL;
    var hh = document.getElementById("headerDIV").offsetHeight;
    var mw = document.getElementById("headerDIV").offsetWidth;
    var mh = document.body.offsetHeight;
    mh = Math.max(document.body.clientHeight,mh);
    mh = Math.max(document.documentElement.clientHeight,mh);


    var rptDiv = document.getElementById("rpt-content");
    var comDiv = document.getElementById("rpt-comment");
    var openCloseImg = document.getElementById("openCloseImg");
    if(commentDivOpend_VAL){
        comDiv.style.display = "block";
        openCloseImg.className = "close";
        rptDiv.style.width = (mw-comDiv.offsetWidth)+"px";
        rptDiv.style.minWidth = (mw-comDiv.offsetWidth)+"px";
    }else{
        comDiv.style.display = "none";
        openCloseImg.className = "open";
        rptDiv.style.width = mw+"px";
    }
    rptDiv.style.height = (mh-hh-1)+"px";
    comDiv.style.height = (mh-hh-1)+"px";
};

//打开或关闭回复
var openOrCloseSubComment = function(commentId){
    var el = document.getElementById("hf_a_"+commentId);
    var a = el.innerText=="收起回复" ? "回复" : "收起回复";
    if(openCommentId_VAL==commentId){
        var subDIV = document.getElementById("subDIV"+commentId);
        if(subDIV.style.display!="none"){
            subDIV.style.display = "none";   //收起回复
            el.className = "pln";
        }else{
            subDIV.style.display = "block";//打开回复
            el.className = "pln a_open";
        }
        el.innerHTML = a;
    }else{
        if(openCommentId_VAL!=null){
            var o_subDIV = document.getElementById("subDIV"+openCommentId_VAL);
            o_subDIV.style.display = "none"; //收起原回复
            document.getElementById("hf_a_"+openCommentId_VAL).innerHTML = "回复";
            document.getElementById("hf_a_"+openCommentId_VAL).className = "pln";
        }
        openCommentId_VAL = commentId;
        var n_subDIV = document.getElementById("subDIV"+openCommentId_VAL);
        n_subDIV.style.display = "block";  //打开新回复
        if(el.innerText.indexOf("新")){
            setReplyRead(el);
        }else{
            el.className = "pln a_open";
            el.innerHTML = a;
        }
        if(document.getElementById("replyUL"+openCommentId_VAL).innerText==""){
            clearReplyComments(openCommentId_VAL);
            queryReplyComments(openCommentId_VAL);
        }else{
            var hf_btn_span = document.getElementById("hf_btn_span"+openCommentId_VAL);
            var hf_txt_span = document.getElementById("hf_txt_span"+openCommentId_VAL);
            if(document.getElementById("replyUL"+openCommentId_VAL).childNodes[0].className=="nodata"){
                hf_btn_span.style.display = "none";
                hf_txt_span.style.display = "block";
            }else{
                hf_txt_span.style.display = "none";
                hf_btn_span.style.display = "block";
            }
        }
    }
};

//打开或关闭回复文本框
var openOrCloseSubCommentText = function(){
    var hf_btn_span = document.getElementById("hf_btn_span"+openCommentId_VAL);
    var hf_txt_span = document.getElementById("hf_txt_span"+openCommentId_VAL);
    if(hf_btn_span.style.display!="none"){
        hf_btn_span.style.display = "none";
        hf_txt_span.style.display = "block";
    }else{
        hf_txt_span.style.display = "none";
        hf_btn_span.style.display = "block";
    }
};

dhx.ready(initThisPage);