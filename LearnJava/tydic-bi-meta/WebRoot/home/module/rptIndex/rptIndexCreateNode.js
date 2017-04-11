/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        rptIndexCreateNode.js
 *Description：
 *    首页创建 动态创建HTML元素  JS
 *    初始界面或ajax动态刷新时需要调用
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

//创建一个业务大类Tab
var createBusTypeTab = function(data){
    var typeId = data.typeId;
    var typeName = data.typeName;
    var li = document.createElement("LI");
    li.innerHTML = typeName;
    if(data.snNum==1){
//        li.style.marginLeft = "10px";
//        li.style.borderLeftStyle = "solid";
//        li.style.borderLeftWidth = "1px";
//        li.style.borderLeftColor = "#CCCCCC";

        li.className = "on_mid_top";
        seledBusType_VAL = typeId;
    }
    li.id = "mid_top_net"+typeId;
    li.typeId = typeId;
    li.onclick = function(){
        selectedBusType(this.typeId);
    };
    return li;
};

//创建搜索面板
var createSearchPlan = function(){
    var li = createBusTypeTab({
        typeId:searchPlanID_VAL,
        typeName:"搜索结果"
    });
    document.getElementById("busTypeUL").appendChild(li);
    var div = document.createElement("DIV");
    div.id = "mid_top_net_mid_top"+searchPlanID_VAL;
    div.innerHTML = "<div id='searchFieldDIV' class='ssuo-content'></div>";
    document.getElementById("busTypeDIV").appendChild(div);
    return div;
};

//创建一个业务小类Tab和字段 面板
var createSubBusTypePlan = function(pos,typeId){
    var subBusTypeTabID = "lib_menubox_top_bottomnet"+typeId;
    var subBusTypeContID = "lib_contentbox_top_bottomnet"+typeId;
    var div = document.createElement("DIV");
    div.id = "mid_top_net_mid_top"+typeId;
    if(pos!=1){
        div.style.display = "none";
    }
    div.innerHTML = "<div id='lib_top_foot_bottomnet' class='lib_top_bottomnet'>" +
        "<div class='lib_menubox_top_bottomnet'><ul id='"+subBusTypeTabID+"'></ul></div>" +
        "<div class='lib_contentbox_top_bottomnet' id='"+subBusTypeContID+"'></div>" +
        "</div>";
    return div;
};

//创建业务小类里面的标签和字段
var createSubBusTypeTabAndField = function(data){
    var pid = data.parentTypeId;
    var typeId = data.typeId;
    var typeName = data.typeName;
    var fieldList = data.fieldList;
    var ul = document.getElementById("lib_menubox_top_bottomnet"+pid);
    var fd = document.getElementById("lib_contentbox_top_bottomnet"+pid);

    //添加标签
    var li = document.createElement("LI");
    li.id = "top_bottom"+typeId;
    li.innerHTML = typeName;
    if(data.snNum==1){
        li.className = "hover";
        seledSubBusType_VAL["p_"+pid] = typeId;
    }
    li.pid = pid;
    li.typeId = typeId;
    li.onmouseover = function(){
        selectedSubBusType(this.pid,this.typeId);
    };
    ul.appendChild(li);

    //构建指标层 以及添加字段
    var fieldD = document.createElement("DIV");
    fieldD.id = "top_foot_bottomnet_top_bottom_"+typeId;
    if(data.snNum!=1){
        fieldD.style.display = "none";
    }
    if(fieldList && fieldList.length){
        for(var i=0;i<fieldList.length;i++){
            fieldD.appendChild(createField({
                snNum:++totalFieldNum_VAL,
                fieldName:fieldList[i]["fieldName"],
                issues:fieldList[i]["issues"],
                cols:fieldList[i]["cols"],
                cnt:fieldList[i]["cnt"]
            }));
        }
    }
    fd.appendChild(fieldD);
};

//创建指标字段
var createField = function(data){
    totalAllField["f_"+data.snNum] = {
        issues:data.issues,
        cols:data.cols
    };
    var sp = document.createElement("SPAN");
    sp.innerHTML = "<input class=\'checkboxs\' type=\'checkbox\' onclick='checkField(this," + data.snNum + ")'" +
        " id=\'field_"+data.snNum+"\' value=\'" + data.fieldName + "\'/><span>" + data.fieldName + "</span>";
    sp.className = "checkbox-top";
    sp.title = data.fieldName;
    sp.idx = data.snNum;
    sp.onclick = function(){
        var ch = document.getElementById("field_"+this.idx);
        ch.click();
    };
    return sp;
};

//创建一个已选指标的label
var createFieldLabel = function(idx,name){
    var sp = document.createElement("SPAN");
    sp.className = "yxzb1";
    sp.id = "field_label_"+idx;
    sp.idx = idx;
    sp.innerHTML = "<span>"+name + "</span><img class='delf' title='取消选择' onclick='delSelField("+idx+")'/>";
    return sp;
};

//创建一个模型内容 （查询后，以及 构建下一屏内容时 会调用）
var createOneModel = function(data){
    var model = document.createElement("DIV");
    model.className = "model-content";
//    model.title = data.issueNote;
    model.innerHTML = "<a href='javascript:void(0);' class='biaoti' onclick='openModel("+data.issueId+")'>"+data.tableAlias+"</a>" +
        "<span class='yy'>(应用"+data.insCnt+"个)</span>" +
        "<a href='javascript:void(0);' class='cjbb' onclick='openNewRptByModel("+data.issueId+")'>创建报表</a>"+
        "<ul class='mid-foot-content'>"+
            "<li><span class='mid-foot-bt'>维度("+data.dimCnt+")</span>" +
            "<span class='mid-foot-content'>"+data.dimColAlias+"</span></li>"+
            "<li><span class='mid-foot-bt'>指标("+data.gdlCnt+")</span>" +
            "<span class='mid-foot-content'>"+data.gdlColAlias+"</span></li>"+
            "<li><span class='mid-foot-bt'>创建时间</span>" +
        "<span class='mid-foot-content'>"+data.createTime+"</span></li>"+
        "</ul>";
    return model;
};

//创建相似报表一条记录
var createApproximateRptLi = function(data){
    var li = document.createElement("LI");
    var title = (data.rptListing?"清单":"报表")+" 相似度"+data.approximateP+"\n创建于"+data.rptCreateTime;
    li.className = "hasdata";
    li.innerHTML = "<a href='javascript:void(0);' onclick='openRpt("+data.rptId+")'>"+data.rptName+"</a>" +
        "<span class='bfb'>"+data.approximateP+"</span>";
    li.title = title;
    return li;
};

//创建我的收藏 一条记录
var createMyFavLi = function(data){
    var className = (data.rptUserId!=user.userId) ? (data.rptRightLevel ? "share-left":"users-left") : "user-left";
    var title = (data.rptRightLevel?"共享":"公共")+(data.rptListing?"清单":"报表")+"\n"+
        (data.rptUserId==user.userId?"我自己 ":"")+"创建于"+data.rptCreateTime;
    var li = document.createElement("LI");
    li.className = "hasdata";
    li.innerHTML = "<a href='javascript:void(0);' onclick='openRpt("+data.rptId+")'>" +
        "<span class='imags'></span>" +
         "   <span class='content_1'>"+data.snNum+"、"+data.rptName+"</span>" +
          "  <span title='"+title+"' class='"+className+"'></span>" +
        "</a>";
    return li;
};
//创建我的报表 一条记录
var createMyRptLi = function(data){
    var title = (data.rptListing?"清单":"报表")+" 打开"+data.rptOpenNum+"次"+"\n创建于"+data.rptCreateTime;
    var li = document.createElement("LI");
    li.className = "hasdata";
    li.innerHTML = "<a href='javascript:void(0);' onclick='openRpt("+data.rptId+")'>" +
        "<span class='imags'></span>" +
        "   <span class='content_3'>"+data.snNum+"、"+data.rptName+"</span>" +
        "<span class='open-num' title='"+title+"'>("+data.rptOpenNum+")</span>"+
        "</a>";
    return li;
};
//创建我订阅 一条记录
var createMyPushLi = function(data){
    var className = (data.rptUserId!=user.userId) ? (data.rptRightLevel ? "share-left":"users-left") : "user-left";
    var title = (data.rptRightLevel?"共享":"公共")+(data.rptListing?"清单":"报表")+"\n" +
        (data.rptUserId==user.userId?"我自己 ":"")+"创建于"+data.rptCreateTime;
    var li = document.createElement("LI");
    li.className = "hasdata";
    li.innerHTML = "<a href='javascript:void(0);' onclick='openRpt("+data.rptId+")'>" +
        "<span class='imags'></span>" +
        "   <span class='content_1'>"+data.snNum+"、"+data.rptName+"</span>" +
        "  <span title='"+title+"' class='"+className+"'></span>" +
        "</a>";
    return li;
};
//创建别人共享给我 一条记录
var createShareToMeLi = function(data){
    var title = (data.rptListing?"清单":"报表")+"\n"+data.rptUserNameCn +" 创建于"+data.rptCreateTime;
    var li = document.createElement("LI");
    li.className = "hasdata";
    li.innerHTML = "<a href='javascript:void(0);' onclick='openRpt("+data.rptId+")'>" +
        "<span class='imags'></span>" +
        "   <span class='content_2'>"+data.snNum+"、"+data.rptName+"</span>" +
        "  <span title='"+title+"' class='users-name'>"+data.rptUserNameCn+"</span>" +
        "</a>";
    return li;
};
//创建公共报表 一条记录
var createPublicRptLi = function(data){
    var title = (data.rptListing?"清单":"报表")+"\n"+data.rptUserNameCn +" 创建于"+data.rptCreateTime;
    var li = document.createElement("LI");
    li.className = "hasdata";
    li.innerHTML = "<a href='javascript:void(0);' onclick='openRpt("+data.rptId+")'>" +
        "<span class='imags'></span>" +
        "   <span class='content_2'>"+data.snNum+"、"+data.rptName+"</span>" +
        "  <span title='"+title+"' class='users-name'>"+data.rptUserNameCn+"</span>" +
        "</a>";
    return li;
};
//创建一条表示无记录的li
var createNoDataLi = function(tip){
    var li = document.createElement("LI");
    li.className = "nodata";
    li.innerHTML = "<a href='javascript:void(0);'>"+(tip||"无记录")+"</a>";
    return li;
};