/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        addCompositeGdl.js
 *Description： 添加复合指标
 *
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

var gdlGroupTree = null;//指标分类树
var gdlID_VAL = gdlId;//指标ID
var optFlag_VAL = optFlag;//操作标识，0新增，1修改
var dimCountTypeFlag = DIM_CODE_VIEWFLAG;//支撑维度编码记录数小于5时，显示为多选checkbox，否则为下拉树或combo
var dimCodeInfo = {};//缓存维度编码数据
var initSupportDim = {};//初始支撑维度
var initBindDimData = {};//初始绑定维度数据

var gdlGroupInfo = {};//缓存初始分类数据
var initGdlData = null;//初始指标数据

var dimCodeLevel = {};//缓存每个维度选择的层级

//初始界面
function pageInit() {
    if(!gdlID_VAL){
        alert("无指标参数，创建复合指标失败！");
        window.close();
    }else{
        initGdlInfo();//获取指标基本信息，以及一些验证
    }
}

//初始指标基本信息（包含一些维度信息）
function initGdlInfo(){
    dhx.showProgress("请求数据中");
    GdlCompositeMagAction.getGdlInfo(gdlID_VAL,function(data){
        dhx.closeProgress();
        if(data){
            initGdlData = data;
            TermReqFactory.createTermReq(1);//创建一个请求包
            initDIMInfos(data["SUPPORT_DIMS"],data["BIND_DIMS"]);
            initGDLBaseInfo(data);
            dhx.showProgress("请求数据中");
            TermReqFactory.getTermReq(1).init(function(val){
                dhx.closeProgress();
                if(optFlag_VAL && !(initGdlData["CAN_MODIFY"])){
                    for(var k in initSupportDim){
                        if(!initBindDimData[k]){
                            var tr = document.getElementById("TR_"+k);
                            if(tr){
                                tr.style.display = "none";//不可修改时，设把未绑定的维度隐藏
                            }
                        }
                    }

                }

            });
            var newBtn = document.getElementById("newBtn");
            var backBtn = document.getElementById("backBtn");
            attachObjEvent(newBtn,"onclick",saveGdlInfo);
            attachObjEvent(backBtn,"onclick",backPage);

            dhtmlxValidation.addValidation($("gdlFormTable"), [
                {target:"gdlName",rule:"NotEmpty,MaxLength[64]"},
                {target:"gdlCode",rule:"NotEmpty,MaxLength[64]"},
                {target:"gdlGroup",rule:"NotEmpty"},
                {target:"gdlUnit",rule:"NotEmpty"},
                {target:"gdlDesc",rule:"NotEmpty"}
            ],"true");

        }else{
            alert("未找到指标，或指标已被下线!");
        }
    });
}

/**
 * 初始维度信息
 * @param support 支撑
 * @param bind 绑定
 */
function initDIMInfos(support,bind){
    if(support){
        for(var i=0;i<support.length;i++){
            createDimTr(support[i]);
        }
        initDimCodeByDefault(bind);
    }
}

//根据默认值初始维度编码
function initDimCodeByDefault(b){
    if(b && b.length>0){
        initBindDimData = {};
        for(var i=0;i<b.length;i++){
            var tableId = b[i]["DIM_TABLE_ID"];
            initBindDimData[tableId] = tableId;
            var typeId = b[i]["DIM_TYPE_ID"];
            var code = b[i]["DIM_CODE"];

            var typeTerm = TermReqFactory.getTermReq(1).getTermControl("DIMTYPE_"+tableId);
            typeTerm.defaultValue = [typeId];
            var term = TermReqFactory.getTermReq(1).getTermControl("DIM_"+tableId);

            var codeMap = {};
            if(term){
                var defV = code.split(",");
                for(var j=0;j<defV.length;j++){
                    codeMap[defV[j]] = 1;
                }
                term.defaultValue = defV;
                term.classRuleParams["DIM_TYPE_ID"] = typeId;
                var sp = document.getElementById("TYPE_SPAN_"+typeId);
                var chdiv = document.getElementById("CH_DIV_"+tableId);
                var tmdiv = document.getElementById("TM_DIV_"+tableId);
                if(sp){
                    tmdiv.style.display = "none";
                    chdiv.style.display = "block";
                    var sps = sp.parentNode.getChildNodes();
                    for(var x=0;x<sps.length;x++){
                        if(sps[x].id!=sp.id){
                            sps[x].style.display = "none";
                        }
                    }
                    sp.style.display = "block";
                    code += ",";
                    var chks = document.getElementsByName("CHK_"+tableId+"_"+typeId);
                    if(chks){
                        for(var j=0;j<chks.length;j++){
                            chks[j].checked = (code.indexOf(chks[j].value + ",") != -1);
                            if(chks[j].checked)
                                codeMap[chks[j].value] = 1;
                        }
                    }
                }else{
                    chdiv.style.display = "none";
                    tmdiv.style.display = "block";
                }
            }
            dimCodeInfo[tableId] = codeMap;
        }
    }
}

//初始指标基本信息
function initGDLBaseInfo(data){
    if(data){
        var gdlName = document.getElementById("gdlName");
        var gdlCode = document.getElementById("gdlCode");
        var gdlUnit = document.getElementById("gdlUnit");
        var gdlGroup = document.getElementById("gdlGroup");
        var gdlDesc = document.getElementById("gdlDesc");
        gdlUnit.value = data["GDL_UNIT"];
        if(optFlag_VAL){
            gdlName.value = data["GDL_NAME"];
            gdlCode.value = data["GDL_CODE"];
            gdlDesc.innerHTML = data["GDL_BUS_DESC"].replace(/\n/g,"<br>");
        }

        var gdlginfos = data["GDL_GROUPINFO"];
        if(gdlginfos){
            for(var i=0;i<gdlginfos.length;i++){
                gdlGroupInfo[gdlginfos[i]["GDL_GROUP_ID"]] = 1;
            }
        }
        var groupTerm = TermReqFactory.getTermReq(1).createTermControl("gdlGroup","GDL_GROUP");
        groupTerm.setClassRule("tydic.meta.module.gdl.group.GdlGroupTreeImpl",2);
        groupTerm.mulSelect = true;
        groupTerm.setWidth(610);
        groupTerm.setBindDataCall(function(t){
            var selTree=t.selTree;
            t.selTree.tree.openAllItems(selTree.tree.rootId);
            for(var key in gdlGroupInfo){
                selTree.tree.setCheck(selTree._priFix+key,true);
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
        groupTerm.render();
        groupTerm.selTree.enableMuiltselect(true,function(d){
            return d[2]!="0";//根分类不允许选择
        });

        var formatTerm = TermReqFactory.getTermReq(1).createTermControl("gdlNumFormat","NUM_FORMAT");
        formatTerm.setCodeRule("GDL_NUMFORMAT",data["NUM_FORMAT"]||0);
        formatTerm.enableReadonly(true);
    }
}

//初始指标分类树
function initGdlGroupTree(){

}

//用支撑维度数据，创建一个
function createDimTr(data){
    var tab = document.getElementById("dimtermTable");
    var typelist = data["DIM_TYPE_LIST"];
    if(typelist && typelist.length>=1){//多归并
        var cc=0;
        var typeTerm = null;
        var chDiv = null;
        var termDiv = null;
        var dimTerm = null;
        var colPreFix = "";
        var typeVal = [];//归并值
        for(var i=0;i<typelist.length;i++){
            var typeid = typelist[i]["DIM_TYPE_ID"];
            if(!(data["DIM_TYPE_INFO_"+typeid]))continue;
            var mlv = data["DIM_TYPE_INFO_"+typeid][0];
            var tot = data["DIM_TYPE_INFO_"+typeid][1];
            if(tot==0)continue;
            if(cc==0){
                initSupportDim[data["DIM_TABLE_ID"]] = data["DIM_TABLE_ID"];
                var tr = document.createElement("TR");
                tr.id = "TR_"+data["DIM_TABLE_ID"];
                tab.appendChild(tr);
                var th = document.createElement("TH");
                th.innerHTML = data["DIM_NAME_CN"]+":";
                th.style.minWidth = "150px";
                tr.appendChild(th);

                var td = document.createElement("TD");
                tr.appendChild(td);
                var td2 = document.createElement("TD");
                tr.appendChild(td2);

                td.style.verticalAlign = "middle";
                td.id = "Sel_"+data["DIM_TABLE_ID"];
                typeTerm = TermReqFactory.getTermReq(1).createTermControl("Sel_"+data["DIM_TABLE_ID"],"DIMTYPE_"+data["DIM_TABLE_ID"]);
                typeTerm.setWidth(100);
                typeTerm.enableReadonly(true);

                chDiv = document.createElement("DIV");
                chDiv.id = "CH_DIV_"+data["DIM_TABLE_ID"];
                termDiv = document.createElement("DIV");
                termDiv.id = "TM_DIV_"+data["DIM_TABLE_ID"];
                td2.appendChild(chDiv);
                td2.appendChild(termDiv);

                if(tot<=dimCountTypeFlag){//checkboxs
                    termDiv.style.display = "none";
                }else{ //多选树或combo
                    chDiv.style.display = "none";
                }
                dimTerm = TermReqFactory.getTermReq(1).createTermControl(termDiv,"DIM_"+data["DIM_TABLE_ID"]);
                colPreFix = typelist[i]["TABLE_DIM_PREFIX"];
            }

            if(tot<=dimCountTypeFlag){//checkboxs
                var sp = document.createElement("SPAN");
                sp.id = "TYPE_SPAN_"+typeid;
                chDiv.appendChild(sp);
                var dataVal = data["DIM_TYPE_DATA_"+typeid];
                var innerStr = "";
                for(var k=0;k<dataVal.length;k++){
                    innerStr += "<input type='checkbox' name='CHK_"+data["DIM_TABLE_ID"]+"_"+typeid+"' " +
                        " id='CHK_"+data["DIM_TABLE_ID"]+"_"+typeid+"_"+dataVal[k][colPreFix+"_CODE"]+"' " +
                        " value='"+dataVal[k][colPreFix+"_CODE"]+"'>"+dataVal[k][colPreFix+"_NAME"]+"&nbsp;&nbsp;";
                }
                sp.innerHTML = innerStr;
            }
            typeVal[typeVal.length] = [typeid,typelist[i]["DIM_TYPE_NAME"]];
            cc++;
        }
        if(typeTerm){
            typeTerm.setListRule(0,typeVal,typeVal[0][0]);
        }
        if(dimTerm){
            dimTerm.setClassRule("tydic.meta.common.term.DimTreeServiceImpl",mlv>1?2:1,null,{
                DIM_TABLE_ID:data["DIM_TABLE_ID"],
                DIM_TABLE_NAME:data["DIM_TABLE_NAME"],
                DIM_TYPE_ID:typeVal[0][0],
                TABLE_DIM_PREFIX:colPreFix
            });
            dimTerm.setParentTerm(typeTerm);
            typeTerm._dimTableId = data["DIM_TABLE_ID"];
            typeTerm.setValueChange(function(v,t){
                var sp = document.getElementById("TYPE_SPAN_"+v[0]);
                var chdiv = document.getElementById("CH_DIV_"+t._dimTableId);
                var tmdiv = document.getElementById("TM_DIV_"+t._dimTableId);
                if(sp){
                    tmdiv.style.display = "none";
                    chdiv.style.display = "block";
                    var sps = sp.parentNode.getChildNodes();
                    for(var x=0;x<sps.length;x++){
                        if(sps[x].id!=sp.id){
                            sps[x].style.display = "none";
                        }
                    }
                    sp.style.display = "block";
                    return false;
                }else{
                    chdiv.style.display = "none";
                    tmdiv.style.display = "block";
                    TermReqFactory.getTermReq(1).getTermControl("DIM_"+t._dimTableId).classRuleParams["DIM_TYPE_ID"]=v[0];
                    TermReqFactory.getTermReq(1).getTermControl("DIM_"+t._dimTableId).defaultValuePath="";
                    return true;
                }
            });

            dimTerm.mulSelect = true;
            dimTerm.enableReadonly(true);
            dimTerm.render();
            if(mlv>1){
                if(mlv>2){
                    dimTerm.selTree.enableAutoSize(false);
                    var mw = 140 + (mlv-2)*50;
                    dimTerm.setListWidth(Math.min(mw,350),200);
                }
                dimTerm.selTree.tree.dimTableId = data["DIM_TABLE_ID"];
                dimTerm.selTree.tree.attachEvent("onCheck",checkDimTree);
            }

            if(optFlag_VAL && !(initGdlData["CAN_MODIFY"])){
                dimTerm.dimTableId = data["DIM_TABLE_ID"];
                dimTerm.setBindDataCall(function(t){
                    var typeTm = TermReqFactory.getTermReq(1).getTermControl("DIMTYPE_"+t.dimTableId);
                    typeTm.combo.disable(true);
                    if(t.combo){
                        t.combo.disable(true);
                    }
                    if(t.selTree){
                        t.inputObj.disabled = true;
                    }
                    var chs = document.getElementsByName("CHK_"+t.dimTableId+"_"+typeTm.getKeyValue());
                    if(chs){
                        for(var xx =0;xx<chs.length;xx++)
                            chs[xx].disabled = true;
                    }
                });
            }
        }
    }
}

//check维度树时的逻辑
function checkDimTree(id,state){
    if(state==1){
        var level = this.getUserData(id,"USER_DATA_"+5);
        var value = id.replace(this.selTree._priFix,"");
        if(!dimCodeLevel[this.dimTableId])
            dimCodeLevel[this.dimTableId] = {};

        if(dimCodeLevel[this.dimTableId]==level){//相同层级
            dimCodeInfo[this.dimTableId][value] = 1 ;
        }else{
            var value = id.replace(this.selTree._priFix,"");
            var codeMap = dimCodeInfo[this.dimTableId];
            if(codeMap){
                for(var k in codeMap){
                    this.setCheck(this.selTree._priFix+k,false);
                }
            }
            dimCodeInfo[this.dimTableId] = {};
            dimCodeInfo[this.dimTableId][value] = 1 ;
            dimCodeLevel[this.dimTableId] = level;
        }
    }
    var checkIdStr=this.getAllChecked();
    var t = this.selTree.termControl;
    t.inputObj.setAttribute("code",checkIdStr);
    checkIdStr=checkIdStr.split(",");
    t.inputObj.value="";
    for(var j=0;j<checkIdStr.length;j++){
        t.inputObj.checkIds[checkIdStr[j]]=this.getItemText(checkIdStr[j]);
        if(t.inputObj.value)
            t.inputObj.value+=","+t.inputObj.checkIds[checkIdStr[j]];
        else
            t.inputObj.value = t.inputObj.checkIds[checkIdStr[j]];
    }
}

/**
 * 获取绑定维度信息，返回map
 * key=tableId
 * value = {
 *     typeId:'',
 *     codes:''
 * }
 */
function getBindDimInfos(){
    var data = {};
    var orderId = 0;
    for(var k in initSupportDim){
        var typeTerm = TermReqFactory.getTermReq(1).getTermControl("DIMTYPE_"+k);
        var typeid = typeTerm.getKeyValue();
        if(typeid){
            var sp = document.getElementById("TYPE_SPAN_"+typeid);
            var chks = document.getElementsByName("CHK_"+k+"_"+typeid);
            if(sp){
                if(chks){
                    var code = [];
                    for(var j=0;j<chks.length;j++){
                        if(chks[j].checked)
                            code[code.length] = chks[j].value;
                    }
                    if(code.length>0){
                        orderId++;
                        data[k] = {
                            orderId:orderId,
                            tableId:k,
                            typeId:typeid,
                            level:1,
                            codes:code.join(",")
                        };
                    }
                }
            }else{
                var term = TermReqFactory.getTermReq(1).getTermControl("DIM_"+k);
                if(term){
                    var cs = term.getKeyValue();
                    if(cs){
                        orderId++;
                        data[k] = {
                            orderId:orderId,
                            tableId:k,
                            typeId:typeid,
                            level:dimCodeLevel[k] || 1,
                            codes:cs
                        };
                    }
                }
            }
        }
    }
    return data;
}

//保存提交
function saveGdlInfo(){
    var d = getBindDimInfos();
    var l = length(d);
    if(l==0){
        alert("请设置绑定维度条件!");
        return;
    }
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

    var data = initGdlData;
    data["GDL_NAME"] = gdlName;
    data["GDL_CODE"] = gdlCode;
    data["GDL_UNIT"] = gdlUnit;
    data["GDL_BUS_DESC"] = gdlDesc;
    data["GDL_GROUPINFO"] = gdlGroup;
    data["NUM_FORMAT"] = numFormat;
    if(optFlag_VAL && data["CAN_MODIFY"]){  //修改，有修改权限
        data["BIND_DIMS"] = d;
    }else if(!optFlag_VAL){ //新增
        data["BIND_DIMS"] = d;
    }else{
        delete data["BIND_DIMS"];
    }
    data["OPT_FLAG"] = optFlag_VAL;
    dhx.showProgress("保存数据中");
    GdlCompositeMagAction.saveCompositeGdl(data,function(ret){
        dhx.closeProgress();
        if(ret.flag=="error"){
            alert("保存指标出错!\n"+(ret.msg || ""));
        }else if(ret.flag=="codeExists"){
            alert(ret.msg||"指标编码重复!");
        }else if(ret.flag=="dimExists"){
            alert(ret.msg||"相关绑定维度发现重复指标!");
        }else{
            alert("保存成功!");
            window.parent.closeTab(optFlag_VAL?"修改复合指标("+initGdlData['GDL_CODE']+")":"创建复合指标");
        }
    });
}

//返回页面
function backPage(){
    var gdlName = document.getElementById("gdlName");
    var gdlCode = document.getElementById("gdlCode");
    var gdlUnit = document.getElementById("gdlUnit");
    var gdlDesc = document.getElementById("gdlDesc");
    if(optFlag_VAL && initGdlData){
        gdlName.value = initGdlData["GDL_NAME"];
        gdlCode.value = initGdlData["GDL_CODE"];
        gdlDesc.innerHTML = initGdlData["GDL_BUS_DESC"].replace(/\n/g,"<br>");
    }else{
        gdlName.value = "";
        gdlCode.value = "";
        gdlDesc.innerHTML = "";
    }

//    TermReqFactory.getTermReq(1).clearValue();
//    TermReqFactory.getTermReq(1).init();

//    //分类和展示格式重置
    var groupTerm = TermReqFactory.getTermReq(1).getTermControl("GDL_GROUP");
    var formatTerm = TermReqFactory.getTermReq(1).getTermControl("NUM_FORMAT");
    var selTree=groupTerm.selTree;
    var checkIdStr=selTree.tree.getAllChecked();
    if(checkIdStr!=""){
        checkIdStr=checkIdStr.split(",");
        for(var i=0;i<checkIdStr.length;i++){
            selTree.tree.setCheck(checkIdStr[i],false);
        }
        groupTerm.inputObj.setAttribute("code","");
        groupTerm.inputObj.value = "";
    }

    for(var key in gdlGroupInfo){
        selTree.tree.setCheck(selTree._priFix+key,true);
    }
    checkIdStr=selTree.tree.getAllChecked();
    if(checkIdStr=="")return;
    groupTerm.inputObj.setAttribute("code",checkIdStr);
    checkIdStr=checkIdStr.split(",");
    groupTerm.inputObj.value="";
    for(var i=0;i<checkIdStr.length;i++){
        groupTerm.inputObj.checkIds[checkIdStr[i]]=selTree.tree.getItemText(checkIdStr[i]);
        if(groupTerm.inputObj.value)
            groupTerm.inputObj.value+=","+groupTerm.inputObj.checkIds[checkIdStr[i]];
        else
            groupTerm.inputObj.value = groupTerm.inputObj.checkIds[checkIdStr[i]];
    }

    formatTerm.combo.selectOption(initGdlData["NUM_FORMAT"]);

}


dhx.ready(pageInit);