/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        calcGdlMag.js
 *Description：计算指标维护
 *
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

var gdlId_VAL = gdlId;//初始指标ID，如果存在则说明是修改计算指标
var exprEditor;//计算表达式编辑器
var gdlInfo_VAL = null;//指标信息
var gdlTree_VAL = null;//指标树
var dragGdlId = null;  //拖动ID
var editorGdl = {};//处于编辑区的指标
var parentGdl = [];//父指标
var exprStr_VAL = "";//计算表达式

//初始界面
function pageInit() {
    initExprEdit(); //初始计算表达式编辑器
    if(gdlId_VAL){
        initGdlInfo();
    }else{
        initFormData();
    }
    initLeftTree();//初始左边的树

    var searchBtn = document.getElementById("searchBtn");
    var checkBtn = document.getElementById("checkBtn");
    var newBtn = document.getElementById("newBtn");
    var clearBtn = document.getElementById("clearBtn");
    var kwdInput = document.getElementById("keyWord");
    attachObjEvent(searchBtn,"onclick",searchGroupInfo);//搜索分类树
    attachObjEvent(checkBtn,"onclick",checkExpr);//校验计算表达式
    attachObjEvent(newBtn,"onclick",saveGdlInfo);//保存指标
    attachObjEvent(clearBtn,"onclick",clearReset);//清空重置表单
    attachObjEvent(kwdInput,"onkeyup",function(e){
        e = e || window.event;
        if(e.keyCode==13)
            searchGroupInfo();
    });

    dhtmlxValidation.addValidation($("gdlFormTable"), [
        {target:"gdlName",rule:"NotEmpty,MaxLength[64]"},
        {target:"gdlCode",rule:"NotEmpty,MaxLength[64]"},
        {target:"gdlGroup",rule:"NotEmpty"},
        {target:"gdlUnit",rule:"NotEmpty"},
        {target:"gdlDesc",rule:"NotEmpty"}
    ],"true");
}

//初始编辑器
function initExprEdit(){
    exprEditor = document.getElementById("exprFrame").contentWindow;
    exprEditor.document.designMode = 'On';
    exprEditor.document.contentEditable = true;

    //IE与FireFox有点不同，为了兼容FireFox，所以必须创建一个新的document。
    exprEditor.document.open();
    exprEditor.document.writeln('<html>' +
        '<head>' +
        '<style type="text/css">p{margin:2px 1px;}span.gdl_lbspn{margin:1px 3px;padding:1px 2px;border:1px solid #c6cde3;background-color:#ebe0ce;font-size:15px;}</style>' +
        '</head>' +
        '<body style="padding:3px 1px;font-size:21px"></body>' +
        '</html>');
    exprEditor.document.close();
}

//初始左边树数据
function initLeftTree(){
    if(!gdlTree_VAL){
        var gdlTreeDIV = document.getElementById("gdlTreeDIV");
        var h = document.body.offsetHeight - 50 ;
        gdlTree_VAL = DHTMLXFactory.createTree("gdlTree",gdlTreeDIV,gdlTreeDIV.offsetWidth,h,0);
        gdlTree_VAL.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
//        gdlTree_VAL.enableDragAndDrop(true);
        //拖前
        gdlTree_VAL.attachEvent("onBeforeDrag", function(sId){
            return ((sId+"").split("_").length>1);
        });
        //拖出
        gdlTree_VAL.attachEvent("onDragIn", function(dId,lId,id,sObject,tObject){
            dragGdlId = dId;
        });
        gdlTree_VAL.attachEvent("onDblClick", function(itemId){
            if((itemId+"").split("_").length>1)
                createLabelToEditor(itemId);
            return true;
        });

        gdlTree_VAL.setXMLAutoLoadingBehaviour("function");
        gdlTree_VAL.setXMLAutoLoading(dynLoadChildGroup);//设置异步加载

        searchGroupInfo();//搜索查询分类
    }
}

//初始指标信息
function initGdlInfo(){
    dhx.showProgress("请求数据中");
    GdlCalcAction.getGdlInfo(gdlId_VAL,function(gdl){
        dhx.closeProgress();
        if(gdl){
            gdlInfo_VAL = gdl;
            var gdlName = document.getElementById("gdlName");
            var gdlCode = document.getElementById("gdlCode");
            var gdlUnit = document.getElementById("gdlUnit");
            var gdlDesc = document.getElementById("gdlDesc");
            //初始表单其他信息
            if(gdlInfo_VAL){
                gdlName.value = gdlInfo_VAL["GDL_NAME"];
                gdlCode.value = gdlInfo_VAL["GDL_CODE"];
                gdlUnit.value = gdlInfo_VAL["GDL_UNIT"];
                gdlDesc.innerHTML = gdlInfo_VAL["GDL_BUS_DESC"].replace(/\n/g,"<br>");
                initGdlExpr();
                initFormData();//初始表单数据
            }
        }else{
            alert("未找到对应指标，或指标已下线!");
        }
    });
}

//修改指标时，初始计算表达式
function initGdlExpr(){
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

//搜索分类树
function searchGroupInfo(){
    var kwd = document.getElementById("keyWord").value.trim();
    dhx.showProgress("请求数据中");
    GdlCalcAction.queryGdlTree({keyWord:kwd},function(data){
        dhx.closeProgress();
        gdlTree_VAL.deleteChildItems(gdlTree_VAL.rootId);
        bindGdlTreeData(data,0);
    });
}

//动态加载子节点
function dynLoadChildGroup(id,tree){
    if(id){
        GdlCalcAction.queryGdlTree({parentId:id},function(data){
            bindGdlTreeData(data,id);
        });
    }
}

//绑定树数据
function bindGdlTreeData(data,rid){
    if(data){
        var groups = data["GROUPS"];
        var gdls = data["GDLS"];
        for(var i=0;i<groups.length;i++){
            var pid = groups[i]["PAR_GROUP_ID"];
            var itemid = groups[i]["GDL_GROUP_ID"];
            var txt = groups[i]["GROUP_NAME"];
            gdlTree_VAL.insertNewChild(pid,itemid,txt,null,"folderClosed.gif",0,0,0,1);
        }
        for(var j=0;j<gdls.length;j++){
            var itemid = rid+"_"+gdls[j]["GDL_ID"];
            var txt = gdls[j]["GDL_NAME"];
            gdlTree_VAL.insertNewChild(rid,itemid,txt,null,0,0,0,0,0);
            gdlTree_VAL.setUserData(itemid,"GDL_INFO",gdls[j]);
        }
    }
}

//设置表单属性
function setFormData(){
    var fmtTerm = TermReqFactory.getTermReq(1).getTermControl("NUM_FORMAT");
    var groupTerm = TermReqFactory.getTermReq(1).getTermControl("GDL_GROUP");

    var gdlName = document.getElementById("gdlName");
    var gdlCode = document.getElementById("gdlCode");
    var gdlUnit = document.getElementById("gdlUnit");
    var gdlDesc = document.getElementById("gdlDesc");
    //初始表单其他信息
    if(gdlInfo_VAL){
        gdlName.value = gdlInfo_VAL["GDL_NAME"];
        gdlCode.value = gdlInfo_VAL["GDL_CODE"];
        gdlUnit.value = gdlInfo_VAL["GDL_UNIT"];
        gdlDesc.innerHTML = gdlInfo_VAL["GDL_BUS_DESC"].replace(/\n/g,"<br>");
        initGdlExpr();
    }else{
        gdlName.value = "";
        gdlCode.value = "";
        gdlUnit.value = "";
        gdlDesc.innerHTML = "";
        exprEditor.document.body.innerHTML = "";
    }

    dhx.showProgress("请求数据中");
    TermReqFactory.getTermReq(1).init(function(tmVal){
        dhx.closeProgress();
    });
}

//初始表单
function initFormData(){
    var tmReq = TermReqFactory.createTermReq(1);
    var fmtTerm = tmReq.createTermControl("gdlNumFormat","NUM_FORMAT");
    fmtTerm.setCodeRule("GDL_NUMFORMAT",0);
    fmtTerm.enableReadonly(true);

    var groupTerm = tmReq.createTermControl("gdlGroup","GDL_GROUP");
    groupTerm.setClassRule("tydic.meta.module.gdl.group.GdlGroupTreeImpl",2);
    groupTerm.mulSelect = true;
    groupTerm.setWidth(550);
    groupTerm.render();
    groupTerm.selTree.enableMuiltselect(true,function(d){
        return d[2]!="0";//根分类不允许选择
    });
    if(gdlInfo_VAL){
        fmtTerm.defaultValue = [gdlInfo_VAL["NUM_FORMAT"]];
        groupTerm.setBindDataCall(function(t){
            var selTree=t.selTree;
            var gdlGroupInfos = gdlInfo_VAL["GDL_GROUPINFO"];
            t.selTree.tree.openAllItems(selTree.tree.rootId);
            for(var i=0;i<gdlGroupInfos.length;i++){
                selTree.tree.setCheck(selTree._priFix+gdlGroupInfos[i]["GDL_GROUP_ID"],true);
            }
            var checkIdStr=selTree.tree.getAllChecked();
            if(checkIdStr=="")return;
            t.inputObj.setAttribute("code",checkIdStr);
            checkIdStr=checkIdStr.split(",");
            t.inputObj.value="";
            for(var i=0;i<checkIdStr.length;i++){
                t.inputObj.checkIds[checkIdStr[i]]=selTree.tree.getItemText(checkIdStr[i]);
                if(t.inputObj.value)
                    t.inputObj.value+=","+t.inputObj.checkIds[checkIdStr[i]];
                else
                    t.inputObj.value = t.inputObj.checkIds[checkIdStr[i]];
            }
        });
    }
    dhx.showProgress("请求数据中");
    TermReqFactory.getTermReq(1).init(function(tmVal){
        dhx.closeProgress();
    });
}

/**
 * 根据指标ID在编辑器区域创建一个指标label
 * 指标详细信息从树节点获取
 * 一般树节点双击或拖动时创建一个
 * @param itemid
 *
 */
function createLabelToEditor(itemid){
    var gdl_info = gdlTree_VAL.getUserData(itemid,"GDL_INFO");
    if(!gdl_info)return;
    var gdlId = itemid.split("_")[1];
    var gdlName = gdl_info["GDL_NAME"];
//    var gdlDesc = gdl_info["GDL_BUS_DESC"];
//    var gdlDims = gdl_info["BIND_DIMS"];
    editorGdl[gdlId] = gdl_info;//

    var lb = exprEditor.document.createElement("SPAN");
    lb.innerHTML = gdlName;
    lb.className = "gdl_lbspn";
    lb.contentEditable = false;//lebel自身内容不可编辑
    lb.setAttribute("gdlValue",gdlId);
    exprEditor.document.body.appendChild(lb);
}

//向编辑器追加一段代码
function insertHtmlToEditor(txt){
    if (exprEditor.document.selection.type.toLowerCase() != "none"){
        exprEditor.document.selection.clear();
    }
    exprEditor.document.selection.createRange().pasteHTML(txt);
}

//校验计算表达式
function checkExpr(){
    var str = exprEditor.document.body.innerHTML;
    var gdllbs = exprEditor.document.getElementsByTagName("SPAN");
    var hasGdl = false;
    var exprGdls = {};//存放包含的指标集
    exprStr_VAL = "";
    var _exprStr_VAL = str;
    if(gdllbs && gdllbs.length>0){
        hasGdl = true;
        for(var i=0;i<gdllbs.length;i++){
            var gdlId = gdllbs[i].getAttribute("gdlValue");
            exprGdls[gdlId] = gdlId;
            str = str.replace(gdllbs[i].outerHTML,"("+gdlId+")");
            _exprStr_VAL = _exprStr_VAL.replace(gdllbs[i].outerHTML,"{"+gdlId+"}");
        }
    }
    if(str=="" || !hasGdl){
        alert("请选入指标，并编写表达式!");
        return;
    }

    str = str.replaceAll("<P>","");
    str = str.replaceAll("</P>","");
    str = str.replaceAll("&nbsp;","");

    if(str.indexOf("\/\/")!=-1 || str.indexOf("\/\\*")!=-1){
        alert("表达式错误!");
        return;
    }


//    var xx = exprEditor.document.getElementById('bb');
//    var expr = exprEditor.document.body.innerHTML.replaceAll(xx.outerHTML,"指标");
//    var a = expr.replace(/&nbsp;/g,"");

    if(str.indexOf("+")!=-1 || str.indexOf("-")!=-1 || str.indexOf("*")!=-1 || str.indexOf("/")!=-1){
        try{
            var a = 0;
            eval("a = "+str+";");
            if(isNaN(a) || isNaN(parseInt(a))){
                alert("表达式错误!")
            }else{
                if(analysisPublicBindDims(exprGdls)){
                    exprStr_VAL = _exprStr_VAL;
//                    alert("通过:"+str);
                    return true;
                }else{
                    alert("所选指标无公共维度，无法创建计算指标!");
                }
            }
        }catch(e){
            alert("表达式错误!")
        }
    }else{
        alert("表达式必须至少包含【+ - * /】四则运算其中一种!");
    }
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
                    bind[bindDims[j]["DIM_TABLE_ID"]] = bindDims[j];
                }else{
                    if(!bind[bindDims[j]["DIM_TABLE_ID"]]){
                        //说明不存在与交集中
                        bind[bindDims[j]["DIM_TABLE_ID"]] = null;
                        delete bind[bindDims[j]["DIM_TABLE_ID"]];
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

//保存
function saveGdlInfo(){
    if(!checkExpr())
        return;
    if(!(dhtmlxValidation.validate("gdlFormTable")))return;

    var gdlName = document.getElementById("gdlName").value;
    var gdlCode = document.getElementById("gdlCode").value;
    var gdlUnit = document.getElementById("gdlUnit").value;
    var gdlDesc = document.getElementById("gdlDesc").innerHTML.trim();
    var gdlGroup = TermReqFactory.getTermReq(1).getTermControl("GDL_GROUP").getKeyValue();
    var numFormat = TermReqFactory.getTermReq(1).getTermControl("NUM_FORMAT").getKeyValue();

    if(gdlGroup==""){
        alert("请设置指标分类!");
        return;
    }

    var data = gdlInfo_VAL||{};
    data["GDL_CALC_EXPR"] = exprStr_VAL;
    data["GDL_NAME"] = gdlName;
    data["GDL_CODE"] = gdlCode;
    data["GDL_UNIT"] = gdlUnit;
    data["GDL_BUS_DESC"] = gdlDesc;
    data["GDL_GROUPINFO"] = gdlGroup;
    data["NUM_FORMAT"] = numFormat;
    data["PARENT_GDLS"] = parentGdl.join(",");

    dhx.showProgress("保存数据中!");
    GdlCalcAction.saveCalcGdl(data,function(ret){
        dhx.closeProgress();
        if(ret.flag=="error"){
            alert("保存指标出错!\n"+(ret.msg || ""));
        }else if(ret.flag=="codeExists"){
            alert(ret.msg||"指标编码重复!");
        }else if(ret.flag=="calcExists"){
            alert(ret.msg||"发现有相同计算表达式的指标!");
        }else{
            alert("保存成功!");
            window.parent.closeTab(gdlInfo_VAL?"修改计算指标("+gdlInfo_VAL['GDL_CODE']+")":"创建计算指标");
        }
    });

}

//清空或重置
function clearReset(){
    if(gdlId_VAL){
        //重置

    }else{
        //清空
        exprEditor.document.body.innerHTML = "";
    }
    setFormData();
}



dhx.ready(pageInit);