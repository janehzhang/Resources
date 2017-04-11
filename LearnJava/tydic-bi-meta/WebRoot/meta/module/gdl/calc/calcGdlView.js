/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        calcGdlView.js
 *Description：
 *      查看计算指标
 *
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/
var gdlId_VAL = gdlId;//初始指标ID
var gdlVersion_VAL = gdlVersion;//初始指标版本
var gdlInfo_VAL = null;//指标信息
var editorGdl = {};//处于编辑区的指标
var parentGdl = [];//父指标

//初始界面
function pageInit(){
    if(gdlId_VAL){
        initGdlInfo();
    }else{
        alert("缺少指标参数");
    }
}

//初始指标信息
function initGdlInfo(){
    var initFun = gdlVersion_VAL ? GdlCalcAction.viewGdlInfo : GdlCalcAction.getGdlInfo;
    var arr = [gdlId_VAL];
    if(gdlVersion_VAL){
        arr[arr.length] = gdlVersion_VAL;
    }
    arr[arr.length] = function(gdl){
        dhx.closeProgress();
        if(gdl){
            gdlInfo_VAL = gdl;
            var gdlName = document.getElementById("gdlName");
            var gdlCode = document.getElementById("gdlCode");
            var gdlUnit = document.getElementById("gdlUnit");
            var gdlNumFmt = document.getElementById("gdlNumFormat");
            var gdlDesc = document.getElementById("gdlDesc");
            var gdlType = document.getElementById("gdlType");
            var gdlState = document.getElementById("gdlState");
            var gdlCreator = document.getElementById("gdlCreator");
            var gdlCreateTime  = document.getElementById("gdlCreateTime");
            var gdlGroup = document.getElementById("gdlGroup");
            gdlName.innerHTML = gdlInfo_VAL["GDL_NAME"];
            gdlCode.innerHTML = gdlInfo_VAL["GDL_CODE"];
            gdlUnit.innerHTML = gdlInfo_VAL["GDL_UNIT"];
            gdlNumFmt.innerHTML = gdlInfo_VAL["GDL_NUMFORMAT_NAME"];
            gdlDesc.innerHTML = gdlInfo_VAL["GDL_BUS_DESC"].replace(/\n/g,"<br>");
            gdlType.innerHTML  = gdlInfo_VAL["GDL_TYPE_NAME"];
            gdlState.innerHTML = gdlInfo_VAL["GDL_STATE_NAME"];
            gdlCreator.innerHTML = gdlInfo_VAL["USER_NAMECN"];
            gdlCreateTime.innerHTML = gdlInfo_VAL["VALID_TIME"];
            var groups  = gdlInfo_VAL["GDL_GROUPINFO"];
            var str = "";
            for(var i=0;i<groups.length;i++){
                str += groups[i]["GROUP_NAME"]+",&nbsp;";
            }
            str = (str+",").replace(",&nbsp;,","");
            gdlGroup.innerHTML = str;
            initGdlExpr();

            var closeBtn = document.getElementById("closeBtn");
            attachObjEvent(closeBtn,"onclick",function(){
                if(window.parent && window.parent.closeTab){
                    window.parent.closeTab("计算指标("+gdlInfo_VAL['GDL_CODE']+")");
                }else{
                    window.close();
                }
            });
        }else{
            alert("未找到对应指标，或指标已下线!");
        }
    };
    dhx.showProgress("请求数据中");
    initFun.apply(window,arr);
}

//初始计算表达式
function initGdlExpr(){
    var exprEditor = document.getElementById("exprFrame").contentWindow;
    //IE与FireFox有点不同，为了兼容FireFox，所以必须创建一个新的document。
    exprEditor.document.open();
    exprEditor.document.writeln('<html>' +
        '<head>' +
        '<style type="text/css">p{margin:2px 1px;}span.gdl_lbspn{margin:1px 3px;padding:1px 2px;border:1px solid #c6cde3;background-color:#ebe0ce;font-size:15px;}</style>' +
        '</head>' +
        '<body style="padding:3px 1px;font-size:21px"></body>' +
        '</html>');
    exprEditor.document.close();


    var exprStr = gdlInfo_VAL["GDL_CALC_EXPR"];
    var expr_Gdls = gdlInfo_VAL["EXPR_GDLS"];

    var regExp = new RegExp("\\{(\\d+)\\}","g");
    exprStr = exprStr.replace(regExp,function(v,gid){
        var gdlName = expr_Gdls[gid]["GDL_NAME"];
        var lb = "<span name='LB_GDL_"+gid+"' gdlValue='"+gid+"' class='gdl_lbspn' contentEditable=false>" +
            gdlName +
            "</span>";
        editorGdl[gid] = expr_Gdls[gid];
        return lb;
    });
    exprEditor.document.body.innerHTML = exprStr;

    analysisPublicBindDims(editorGdl);

}

//计算公共绑定维度
function analysisPublicBindDims(exprGdls){
    var i=0;
    var bind = {};
    parentGdl = [];
    for(var k in exprGdls){
        parentGdl[parentGdl.length] = k;
        var gdlInfo = editorGdl[k];
        var bindDims = gdlInfo["BIND_DIMS"];
        if(bindDims && bindDims.length>0){
            for(var j=0;j<bindDims.length;j++){
                if(i==0){
                    bind[bindDims[j]["DIM_COL_ID"]] = bindDims[j];
                }else{
                    if(!bind[bindDims[j]["DIM_COL_ID"]]){
                        //说明不存在与交集中
                        bind[bindDims[j]["DIM_COL_ID"]] = null;
                        delete bind[bindDims[j]["DIM_COL_ID"]];
                    }
                }
            }
            i++;
        }
    }

    var supportDimTD = document.getElementById("supportDimTD");
    var str = "";
    for(var bk in bind){
        str += bind[bk]["DIM_NAME_CN"]+",&nbsp;";
    }
    if(str!=""){
        str = (str+",").replace(",&nbsp;,","");
    }
    supportDimTD.innerHTML = str;
    return (str!="");
}

dhx.ready(pageInit);