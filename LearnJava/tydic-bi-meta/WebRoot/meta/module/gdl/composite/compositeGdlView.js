/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        addCompositeGdl.js
 *Description： 查看复合指标
 *
 *Dependent：
 *
 *Author:
 *        刘弟伟
 *Date：
 * 		2012-06-15
 ********************************************************/

var gdlID_VAL = gdlId;//指标ID
var gdlVersion_VAL = gdlVersion;//初始指标版本
var initGdlData = null;//初始指标数据

//初始界面
function pageInit() {
    if(!gdlID_VAL){
        alert("缺少指标参数！");
        window.close();
    }else{
        initGdlInfo();//获取指标基本信息，以及一些验证
    }
}

//初始指标基本信息（包含一些维度信息）
function initGdlInfo(){
    dhx.showProgress("请求数据中");
    GdlCompositeMagAction.viewGdlInfo(gdlID_VAL,gdlVersion_VAL,function(data){
        dhx.closeProgress();
        if(data){
            TermReqFactory.createTermReq(1);//创建一个请求包
            initDIMInfos(data["BIND_DIMS"],data["CODE_NAME"]);
             
            initGDLBaseInfo(data);
            dhx.showProgress("请求数据中");
            TermReqFactory.getTermReq(1).init(function(val){
                dhx.closeProgress();
            });
            
            var backBtn = document.getElementById("backBtn");
            var showxygx = document.getElementById("show_xygx");
            attachObjEvent(backBtn,"onclick",function(){
            	window.parent.closeTab("复合指标("+initGdlData['GDL_CODE']+")");
            });
            attachObjEvent(showxygx,"onclick",function(){
            	openMenu("指标血缘视图("+initGdlData['GDL_CODE']+")","/meta/module/gdl/rel/relGdlView.jsp?gdlId="+gdlID_VAL,"top","gdlrel_"+gdlID_VAL);
            });
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
function initDIMInfos(bindDims,codeName){
    if(bindDims){
        for(var i=0;i<bindDims.length;i++){
            createDimTr(bindDims[i],codeName[i]);
        }
    }
}

//初始指标基本信息
function initGDLBaseInfo(data){
    if(data){
        initGdlData = data;
        var gdlName = document.getElementById("gdlName");
        var gdlCode = document.getElementById("gdlCode");
        var username = document.getElementById("username");
        var gdlType = document.getElementById("gdlType");
        var gdlUnit = document.getElementById("gdlUnit");
        var gdlState = document.getElementById("gdlstate");
        var gdlGroup = document.getElementById("gdlGroup");
        var gdlDesc = document.getElementById("gdlDesc");
        gdlName.innerHTML = data["GDL_NAME"];
        gdlCode.innerHTML = data["GDL_CODE"];
        gdlType.innerHTML = data["GDL_TYPE_NAME"];
        username.innerHTML = data["USER_NAMECN"];
        gdlUnit.innerHTML = data["GDL_UNIT"];
        gdlState.innerHTML = data["GDL_STATE_NAME"];
        gdlDesc.innerHTML = data["GDL_BUS_DESC"].replace(/\n/g,"<br>");

        
        var gdlGroup = document.getElementById("gdlGroup");
        var gdlginfos = data["GDL_GROUPINFO"];
        var groupname = [];
        if(gdlginfos){
            for(var i=0;i<gdlginfos.length;i++){
                groupname[i] = gdlginfos[i]["GROUP_NAME"]; 
            }
       }
        $("gdlGroup").innerHTML = groupname.join();
    }
}

//用支撑维度数据，创建一个
function createDimTr(data,codeName){
   var tab = document.getElementById("dimtermTable");
   var tr = document.createElement("TR");
    tab.appendChild(tr);
    var th = document.createElement("TD");
    th.innerHTML = data["DIM_NAME_CN"];
    tr.appendChild(th);
    th.style.textAlign = "right";
    var td = document.createElement("TD");
    td.innerHTML = codeName["CODE_NAME"];
    tr.appendChild(td);
}

dhx.ready(pageInit);