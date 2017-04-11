/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        rptIndex.js
 *Description：
 *    首页框架，控制，以及主体逻辑JS
 *    整个JS代码定义结构顺序依次为  全局变量，init类，查询请求后台类，界面交互控制类
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var user = getSessionAttribute("user");

//全局变量 及 常量
var seledBusType_VAL = null;             //当前选择的大类
var seledSubBusType_VAL = {};         //存放每个大类下选择的小类 {} json对象形式
//选择指标
var checkField_VAL = {issue:new Array(0),col:new Array(0),seld:0};
var totalFieldNum_VAL = 0;              //界面出现过的总字段数量
var totalSearchField_VAL = new Array(0); //搜索字段snNum 集
var totalAllField = {};                 //存放所有字段
var searchPlanID_VAL = "search";              //搜索面板ID
//记录默认排序状态
var orderModel_VAL = "INS_CNT";
var order_VAL = {
    CREATE_TIME:"DESC",
    INS_CNT:"DESC",
    DIM_CNT:"ASC"
};
var modelKeyWord_VAL = null;              //模型关键字
var pageSize_VAL = 10;                  //模型显示一页数量
var totalNum_VAL = 0;                   //总记录数
var pageStart_VAL = 0;                  //从第一页开始
var rightDIVStartTop_VAL = 0;               //相似报表层起始位置
var boxMenuStartTop_VAL = 0;                //盒子菜单起始位置
var refreshBeforeScrollTop_VAL = 0;          //记住刷新数据前滚动条位置
var fieldNullKey_VAL = "指标关键字";
var modelNullKey_VAL = "可输入模型关键字搜索";


/**======我是分隔线（其上全局变量，其下初始）==========**/

//初始界面
var initRptIndex = function () {
    document.getElementById("current-user").innerHTML = user.userNamecn;

    initRptIndexLeft();
    initBusType();

    document.getElementById("queryFieldInput").value = fieldNullKey_VAL;
    document.getElementById("queryModelInput").value = modelNullKey_VAL;
    //滚动条事件，左边的BoxMenu和右边的相似报表层需要跟随滑动
    rightDIVStartTop_VAL = document.getElementById("approximateDIV").offsetTop;     //设置相似报表层的初始位置
    boxMenuStartTop_VAL = document.getElementById("menuleft").offsetTop;            //设置boxMenu的初始位置
    window.onscroll = function(){
        var scroTop = document.documentElement.scrollTop + document.body.scrollTop;//此种写法取得的值兼容主流浏览器
        movePosBoxMenu(scroTop);
        movePosRightDIV(scroTop);
    };

    queryModelsAndApproximateRpt();
};

//初始业务分类大类
var initBusType = function(){
    RptIndexAction.queryBusTypeByShowIndex(function(data){
        if(data && data.length){
            for(var i=0;i<data.length;i++){
                //创建一个大类标签
                var li = createBusTypeTab({
                    snNum:i+1,
                    typeId:data[i]["COL_TYPE_ID"],
                    typeName:data[i]["COL_TYPE_NAME"]
                });
                document.getElementById("busTypeUL").appendChild(li);

                //创建大类容器
                var div = createSubBusTypePlan(i+1,data[i]["COL_TYPE_ID"]);
                document.getElementById("busTypeDIV").appendChild(div);
                //初始一个大类标签下的子类和字段
                initSubBusTypeAndField(data[i]["COL_TYPE_ID"]);
            }
        }
    });
};
//初始业务分类小类以及其下的字段
var initSubBusTypeAndField = function(typeId){
    RptIndexAction.querySubTypeAndFieldsByParent(typeId,function(data){
        if(data && data.length){
            for(var i=0;i<data.length;i++){
                //创建一个子类标签 以及其下的字段
                createSubBusTypeTabAndField({
                    snNum:i+1,
                    typeId:data[i]["typeId"],
                    typeName:data[i]["typeName"],
                    fieldList:data[i]["fieldList"],
                    parentTypeId:typeId
                });
            }
        }
    });
};

/**=========我是分隔线（其上初始，其下查询后台）=======**/

//根据关键字搜索指标字段
var queryFields = function(){
    var searchFieldDIV = document.getElementById("searchFieldDIV");
    if(!searchFieldDIV){
        createSearchPlan();
        searchFieldDIV = document.getElementById("searchFieldDIV")
    }
    selectedBusType(searchPlanID_VAL);
    clearQueryFields();
    var kwd = document.getElementById("queryFieldInput").value.trim();
    kwd = (kwd!=fieldNullKey_VAL) ? kwd : "";
    RptIndexAction.queryFields(kwd,function(data){
        if(data && data.length){
            for(var i=0;i<data.length;i++){
                var sn = ++totalFieldNum_VAL;
                totalSearchField_VAL[totalSearchField_VAL.length] = sn;
                var sp = createField({
                    snNum:sn,
                    fieldName:data[i]["fieldName"],
                    issues:data[i]["issues"],
                    cols:data[i]["cols"],
                    cnt:data[i]["cnt"]
                });
                searchFieldDIV.appendChild(sp);
            }
        }else{
            searchFieldDIV.innerHTML = "未找到匹配指标";
        }
    });
};

//查询模型和相似报表
var queryModelsAndApproximateRpt = function(){
    //保存关键字
    modelKeyWord_VAL = document.getElementById("queryModelInput").value.trim();
    modelKeyWord_VAL = (modelKeyWord_VAL!=modelNullKey_VAL) ? modelKeyWord_VAL : "";

    //计算模型ID交集，和列ID交集
    computeIssuesAndColsBySeledField();

    totalNum_VAL = 0;
    pageStart_VAL = 0;
    clearModels();
    queryModels();
    queryApproximateRpt();
};

//根据表单条件查询模型
var queryModels = function(){
    var modelsContDIV = document.getElementById("modelsContDIV");
    RptIndexAction.queryModels(modelKeyWord_VAL,
        checkField_VAL["issue"],
        checkField_VAL["seld"],
        orderModel_VAL,
        order_VAL[orderModel_VAL],
        pageStart_VAL,
        pageSize_VAL,
        function(data){
            if(data && data.length){
                for(var i=0;i<data.length;i++){
                    if(i==0){
                        totalNum_VAL = data[i]["TOTAL_COUNT_"];
                        document.getElementById("totalNumDesc").innerHTML = "共找到相关模型<font style='color:red;font-weight:bold;'>"+totalNum_VAL+"</font>个";
                    }
                    var div = createOneModel({
                        snNum:i+1,
                        issueId:data[i]["ISSUE_ID"],
                        tableAlias:data[i]["TABLE_ALIAS"],
                        issueNote:data[i]["ISSUE_NOTE"],
                        isListing:data[i]["IS_LISTING"],
                        dimCnt:data[i]["DIM_CNT"],
                        dimColAlias:(data[i]["DIM_COL_ALIAS"]||"").replaceAll(",","、"),
                        gdlCnt:data[i]["GDL_CNT"]||0,
                        gdlColAlias:(data[i]["GDL_COL_ALIAS"]||"").replaceAll(",","、"),
                        insCnt:data[i]["INS_CNT"]||0,
                        createTime:data[i]["CREATE_TIME"],
                        auditType:data[i]["AUDIT_TYPE"]
                    });
                    modelsContDIV.appendChild(div);
                }
                //判断更多页面显示与否
                if((pageSize_VAL+pageStart_VAL) >= totalNum_VAL){
                    document.getElementById("moreModelDIV").style.display = "none";
                }else{
                    document.getElementById("moreModelDIV").style.display = "block";
                    document.getElementById("moreModelNum").innerHTML = "还有<font style='color:#0000FF;font-weight:bold;'>"+
                        (totalNum_VAL - (pageSize_VAL+pageStart_VAL)) +
                        "</font>个模型未展示";
                }
                window.scrollTo(0,refreshBeforeScrollTop_VAL);
            }else{
                if(pageStart_VAL==0){
                    document.getElementById("totalNumDesc").innerHTML = "未找到匹配模型";
                    document.getElementById("moreModelDIV").style.display = "none";
                    //……无记录
                }
            }
        }
    );
};

//根据字段条件查询相似报表
var queryApproximateRpt = function(){
    clearApproximateRpt();
    document.getElementById("approximateDIV").style.visibility = "visible";
    var approximateContDIV = document.getElementById("approximateContDIV");
    if(checkField_VAL["seld"]){
        RptIndexAction.queryApproximateRptByField(checkField_VAL["col"],checkField_VAL["seld"],10,function(data){
            if(data && data.length){
                for(var i=0;i<data.length;i++){
                    var li = createApproximateRptLi({
                        snNum:i+1,
                        rptId:data[i]["REPORT_ID"],
                        rptName:data[i]["REPORT_NAME"],
                        rptNote:data[i]["REPORT_NOTE"],
                        rptListing:data[i]["IS_LISTING"],
                        rptCreateTime:data[i]["CREATE_TIME"],
                        approximateP:data[i]["APPROXIMATE_P"]
                    });
                    approximateContDIV.appendChild(li);
                }
            }else{
                approximateContDIV.appendChild(createNoDataLi("未发现相似报表"));
            }
        });
    }else{
        approximateContDIV.appendChild(createNoDataLi("选择指标才可匹配"));
    }
};

//点击排序
var changeOrderMode = function(of){
    if(orderModel_VAL!=of){
        document.getElementById("od-"+orderModel_VAL).className = "mid-h"+(order_VAL[orderModel_VAL]=="ASC"?"s":"x");
        orderModel_VAL = of;
        document.getElementById("od-"+orderModel_VAL).className = "mid-c"+(order_VAL[orderModel_VAL]=="ASC"?"s":"x");
    }else{
        document.getElementById("od-"+orderModel_VAL).className = "mid-c"+(order_VAL[orderModel_VAL]=="ASC"?"x":"s");
        if(order_VAL[of]=="ASC"){
            order_VAL[of] = "DESC";
        }else{
            order_VAL[of] = "ASC";
        }
    }
    pageStart_VAL = 0;
    if(modelKeyWord_VAL!=null){
        clearModels();
        queryModels();
    }
};

//查询获取下一屏模型(实现时根据分页来辅助实现)
var queryNextPageModel = function(){
    refreshBeforeScrollTop_VAL = document.documentElement.scrollTop + document.body.scrollTop;
    pageStart_VAL += pageSize_VAL;
    queryModels();
};

//清空模型
var clearModels = function(){
    refreshBeforeScrollTop_VAL = document.documentElement.scrollTop + document.body.scrollTop;
    document.getElementById("modelsContDIV").innerHTML = "";
};
//清空相似报表
var clearApproximateRpt = function(){
    document.getElementById("approximateContDIV").innerHTML = "";
};

//清空搜索指标面板
var clearQueryFields = function(){
    var searchFieldDIV = document.getElementById("searchFieldDIV");
    for(var i=0;i<totalSearchField_VAL.length;i++){
        var idx = totalSearchField_VAL[i];
        if(!checkField_VAL["chk_"+idx]){
            delete totalAllField["f_"+idx];
        }
    }
    searchFieldDIV.innerHTML = "";
};

/**====我是分隔线（其上查询后台，其下界面交互控制）=====**/

//收起/关闭字段面板
var openOrCloseFields = function(li){
    if(li.opend==null || li.opend==undefined){
        li.opend = 1;
    }
    if(li.opend){
        li.opend = 0;
        li.className = "oc_fileds oc_close";
        document.getElementById("busTypeDIV").style.display = "none";
        if(seledBusType_VAL){
            document.getElementById("mid_top_net"+seledBusType_VAL).className = "";
        }
    }else{
        li.opend = 1;
        li.className = "oc_fileds oc_open";
        document.getElementById("busTypeDIV").style.display = "block";
        if(seledBusType_VAL){
            document.getElementById("mid_top_net"+seledBusType_VAL).className="on_mid_top";
        }
    }
};

//check点击某个字段时 调用
var checkField = function(chk,idx){
    var selectedFieldDIV = document.getElementById("selectedFieldDIV");
    if(chk.checked){
        var sp = createFieldLabel(idx,chk.value);
        selectedFieldDIV.appendChild(sp);
        checkField_VAL["chk_"+idx] = idx;
    }else{
        delSelField(idx);
    }
    var e = this.event || window.event;
    e.cancelBubble = true;
};

//删除一个选中的指标
var delSelField = function(idx){
    var selectedFieldDIV = document.getElementById("selectedFieldDIV");
    selectedFieldDIV.removeChild(document.getElementById("field_label_"+idx));
    delete checkField_VAL["chk_"+idx];
    if(document.getElementById("field_"+idx)){
        document.getElementById("field_"+idx).checked = false;
    }
};

//selected某个大类
var selectedBusType = function(id){
    if(seledBusType_VAL){
        var oldLi = document.getElementById("mid_top_net"+seledBusType_VAL);
        var oldDiv = document.getElementById("mid_top_net_mid_top"+seledBusType_VAL);
        oldLi.className = "";
        oldDiv.style.display = "none";
    }
    var newLi = document.getElementById("mid_top_net"+id);
    var newDiv = document.getElementById("mid_top_net_mid_top"+id);
    newLi.className = "on_mid_top";
    newDiv.style.display = "block";
    seledBusType_VAL = id;
    var ocBtn = document.getElementById("oc_btn");
    if(ocBtn.opend==0){
        openOrCloseFields(ocBtn);
    }
};

//selected某个小类
var selectedSubBusType = function(pid,id){
    var oid = seledSubBusType_VAL["p_"+pid] ;
    if(oid){
        var oldLi = document.getElementById("top_bottom"+oid);
        var oldDiv = document.getElementById("top_foot_bottomnet_top_bottom_"+oid);
        oldLi.className = "";
        oldDiv.style.display = "none";
    }
    var newLi = document.getElementById("top_bottom"+id);
    var newDiv = document.getElementById("top_foot_bottomnet_top_bottom_"+id);
    newLi.className = "hover";
    newDiv.style.display = "block";
    seledSubBusType_VAL["p_"+pid] = id;
};

//清空选择的指标
var clearSelectedFields = function(){
    var selectedFieldDIV = document.getElementById("selectedFieldDIV");
    var sps = selectedFieldDIV.childNodes;
    for(var i=0;i<sps.length;i++){
        delete checkField_VAL["chk_"+sps[i].idx];
        if(document.getElementById("field_"+sps[i].idx)){
            document.getElementById("field_"+sps[i].idx).checked = false;
        }
    }
    selectedFieldDIV.innerHTML = "";
};

//模型搜索框回车事件
var enterModelInput = function(){
    var e = this.event || window.event;
    if (e.keyCode == 13){
        queryModelsAndApproximateRpt();
        return false;
    }
};

//字段搜索框回车事件
var enterFieldInput = function(){
    var e = this.event || window.event;
    if (e.keyCode == 13){
        queryFields();
        return false;
    }
};

//重置模型关键字输入框的值
var resetModelInput = function(inp,flag){
    if(flag==1 && inp.value==modelNullKey_VAL){
        inp.value = "";
        inp.className = "zhc";
    }else if(flag==0 && inp.value.trim()==""){
        inp.value = modelNullKey_VAL;
        inp.className = "huise";
    }
};

//重置指标关键字输入框的值
var resetFieldInput = function(inp,flag){
    if(flag==1 && inp.value==fieldNullKey_VAL){
        inp.value = "";
        inp.className = "zhc";
    }else if(flag==0 && inp.value.trim()==""){
        inp.value = fieldNullKey_VAL;
        inp.className = "huise";
    }
};

//鼠标移到某字段时浮动层显示其解释
var showFieldDescDIV = function(){

};
//隐藏浮动解释层
var hideFieldDescDIV = function(){

};

/**
 * 滑动定位右边的层（放相似报表的层），窗体滚动时，动态定位新位置
 * @param t 滚动条位置 top
 */
var movePosRightDIV = function(t){
    t = t||document.documentElement.scrollTop + document.body.scrollTop;
    var ratPos = document.getElementById("ratPos");
    var approximateDIV = document.getElementById("approximateDIV");
    var oldHeight = ratPos.offsetTop + ratPos.offsetHeight + 149 ;
    if(t>oldHeight){
        approximateDIV.style.top = (t-oldHeight)+"px";
    }else{
        var ph = ((document.getElementById("selectedFieldDIV").offsetHeight<25)
            ? 29
            : document.getElementById("selectedFieldDIV").offsetHeight) + 240;
        if(document.getElementById("busTypeDIV").style.display=="none"){
            ph -= 282;
        }
        approximateDIV.style.top = (rightDIVStartTop_VAL - oldHeight + ph)+"px";
    }
};

//通过选择的字段计算模型和列  如果选择了字段则返回true，否则返回false
var computeIssuesAndColsBySeledField = function(){
    delete checkField_VAL["issue"] ;
    delete checkField_VAL["col"] ;
    delete checkField_VAL["seld"];
    var us = {};
    var cs = {};
    var i = 0;
    for(var k in checkField_VAL){
        var idx = checkField_VAL[k];
        var issues = totalAllField["f_"+idx].issues;
        var cols = totalAllField["f_"+idx].cols;
        cs = dhx.extend(cs,cols,true);
        if(i==0){
            us = dhx.extend({},issues,true);
        }else{
            for(var uk in us){
                if(!issues[uk]){
                    delete us[uk];
                }
            }
        }
        i++;
    }
    checkField_VAL["issue"] = new Array(0) ;
    checkField_VAL["col"] = new Array(0) ;
    for(var uak in us){
        checkField_VAL["issue"][checkField_VAL["issue"].length] = us[uak];
    }
    for(var cak in cs){
        checkField_VAL["col"][checkField_VAL["col"].length] = cs[cak];
    }
    checkField_VAL["seld"] = i;
};


dhx.ready(initRptIndex);