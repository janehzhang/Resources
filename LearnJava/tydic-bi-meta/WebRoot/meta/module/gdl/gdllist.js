/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        gdllist.js
 *Description：
 *
 *Dependent：
 *
 *Author:
 *        王春生
 ********************************************************/

var dataTable = null;//表格
var viewFlag_VAL = viewFlag;// 0维护视图，1查看视图

//初始界面
function pageInit() {
    var termReq = TermReqFactory.createTermReq(1);
    var kwd = termReq.createTermControl("kwd","KEY_WORD");
    kwd.setWidth(380);
    kwd.setInputEnterCall(function(){
        dataTable.Page.currPageNum = 1;
        dataTable.refreshData();
    });
    var gdlTypeTerm = termReq.createTermControl("gdlType","GDL_TYPE");
    gdlTypeTerm.setAppendData([["","全部"]]);
    gdlTypeTerm.setCodeRule("GDL_TYPE","");
    gdlTypeTerm.enableReadonly(true);

    var gdlStateTerm = termReq.createTermControl("gdlState","GDL_STATE");
//    gdlStateTerm.setAppendData([["","全部"]]);
//    gdlStateTerm.setCodeRule("GDL_STATE","");
    gdlStateTerm.setListRule(0,[["","全部"],[1,"在线"],[3,"已下线"]],1);
    gdlStateTerm.enableReadonly(true);

    var gdlGroupTerm = termReq.createTermControl("gdlGroup","GDL_GROUP");
    gdlGroupTerm.setClassRule("tydic.meta.module.gdl.group.GdlGroupTreeImpl",2);
    gdlGroupTerm.mulSelect = true;
    gdlGroupTerm.setWidth(380);
    gdlGroupTerm.render();
    gdlGroupTerm.selTree.enableMuiltselect(true,function(d){
        return d[2]!="0";//根分类不允许选择
    });
    gdlGroupTerm.setBindDataCall(function(t){
        t.selTree.tree.openAllItems(t.selTree.tree.rootId);
    });

    dataTableInit(); //初始数据表格  初始之后dataTable才会被实例化
    dataTable.setReFreshCall(queryData); //设置表格刷新的回调方法，即实际查询数据的方法

    dhx.showProgress("请求数据中");
    termReq.init(function(termVals){
        dhx.closeProgress();
        dataTable.refreshData();
    }); //打包请求数据，初始，传入回调函数，里面开始查询数据

    var queryBtn = document.getElementById("queryBtn");
    var newBtn = document.getElementById("newBtn");
    attachObjEvent(queryBtn,"onclick",function(){
        dataTable.Page.currPageNum = 1;
        dataTable.refreshData();
    });
    attachObjEvent(newBtn,"onclick",function(){
        createCalcGdl();
    });
//    if(viewFlag_VAL)
//        newBtn.style.display = "none";
}

//初始数据表格
function dataTableInit(){
    var dd = document.getElementById("dataDiv");
    var pageContent = document.getElementById("pageContent");
    var queryFormDIV = document.getElementById("queryFormDIV");
    queryFormDIV.style.width = document.body.offsetWidth -10  + "px";
//    dd.style.height = document.body.offsetHeight-queryFormDIV.offsetHeight - 15 + "px";
    dataTable = new meta.ui.DataTable("dataDiv");//第二个参数表示是否是表格树
    dataTable.setColumns({
        GDL_CODE:"编码",
        GDL_NAME:"名称",
        GDL_TYPE:"类型",
        GDL_STATE:"状态",
        GDL_UNIT:"单位",
        USER_ID:"创建人",
        GDL_BUS_DESC:"业务解释",
        opt:"操作"
    },"GDL_CODE,GDL_NAME,GDL_TYPE_NAME,GDL_STATE_NAME,GDL_UNIT,USER_NAMECN,GDL_BUS_DESC,GDL_ID");
    dataTable.setRowIdForField("GDL_ID");
    dataTable.setPaging(true,20);//分页
    dataTable.setSorting(false,{
        GDL_NAME:"asc",
        GDL_CODE:"asc"
    });
    dataTable.render();//绘制函数，一些set方法必须在绘制函数之前，绘制函数之后内置的源生dhtmlxGrid对象被初始
    dataTable.grid.setInitWidthsP("12,13,8,8,5,8,16,30");

    dataTable.setFormatCellCall(function(rid,cid,data,colId){
        if(colId=="OPT"){
            var gdlId = dataTable.getUserData(rid,"GDL_ID");
            var state = dataTable.getUserData(rid,"GDL_STATE");
            var gdltype = dataTable.getUserData(rid,"GDL_TYPE");
            var str = "";
            if(viewFlag_VAL){
                //查看视图
                str += "<a href='javascript:void(0)' onclick='viewGdl("+gdlId+");return false;'>查看</a>";
                if(gdltype!=2){
                    str += "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='viewGdlTblRel("+gdlId+");return false;'>查看表关系</a>";
                }
                str += "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='viewGdlRel("+gdlId+");return false;'>血缘视图</a>";
                if(state==1){     //在线状态
                    if(gdltype!=2){
                        str += "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='createComosite("+gdlId+");return false;'>创建复合指标</a>";
                    }
                }
            }else{
                //维护视图
                if(state==1){     //在线状态
                    str += "<a href='javascript:void(0)' onclick='upOrDownLine("+gdlId+",0);return false;'>下线</a>";
                    str += "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='updateGdl("+gdlId+");return false;'>修改</a>";
                    if(gdltype!=2){
                        str += "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='magGdlTblRel("+gdlId+");return false;'>维护表关系</a>";
                    }
                    str += "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='viewGdlRel("+gdlId+");return false;'>血缘视图</a>";
                    if(gdltype!=2){
                        str += "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='createComosite("+gdlId+");return false;'>创建复合指标</a>";
                    }
                }else{ //下线状态
                    str += "<a href='javascript:void(0)' onclick='upOrDownLine("+gdlId+",1);return false;'>重新上线</a>";
                    str += "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='viewGdl("+gdlId+");return false;'>查看</a>";
                    if(gdltype!=2){
                        str += "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='viewGdlTblRel("+gdlId+");return false;'>查看表关系</a>";
                    }
                    str += "&nbsp;&nbsp;<a href='javascript:void(0)' onclick='viewGdlRel("+gdlId+");return false;'>血缘视图</a>";
                }
            }
            return str;
        }else if(colId=="GDL_STATE"){
            var state = dataTable.getUserData(rid,"GDL_STATE");
            return state==1?"在线":"已下线";
        }
        return data[cid];
    });

    attachObjEvent(window,"onresize",function(){
        var dd = document.getElementById("dataDiv");
        var queryFormDIV = document.getElementById("queryFormDIV");
        dd.style.width = document.body.offsetWidth - 10 + "px";
//        dd.style.height = document.body.offsetHeight-queryFormDIV.offsetHeight - 15 + "px";
        dataTable.grid.setSizes();
    });

    return dataTable;
}

//查询数据
function queryData(dt,params){
    var termVals=TermReqFactory.getTermReq(1).getKeyValue();
    termVals["_COLUMN_SORT"] = params.sort;
    dhx.showProgress("请求数据中");
    GdlAction.queryGdl(termVals,{posStart:params.page.rowStart,count:params.page.pageSize},function(data){
        dhx.closeProgress();
        var total = 0;
        if(data && data[0])
            total = data[0]["TOTAL_COUNT_"];
        dataTable.bindData(data,total); //查询出数据后，必须显示调用绑定数据的方法
    });

}

//创建计算指标
function createCalcGdl(){
    openMenu("创建计算指标","/meta/module/gdl/calc/calcGdlMag.jsp","top");
}

//上下线，state=1 上线；state=0下线
function upOrDownLine(gdlId,state){
    var gdlName = dataTable.getUserData(gdlId,"GDL_NAME");
    var gdlVersion = dataTable.getUserData(gdlId,"GDL_VERSION");
    var cfm = "你确定将指标["+gdlName+"]<br>"+(state==1?'重新上线':'下线')+"吗?";
    dhx.confirm(cfm,function(r){
        if(r){
            dhx.showProgress("提交数据中");
            GdlAction.upOrDownGdl(gdlId,state==1?1:0,gdlVersion,function(ret){
                dhx.closeProgress();
                dataTable.refreshData();
                gdlVersion = null;
            });
        }
    });
}

//创建复合指标
function createComosite(gdlId){
    openMenu("创建复合指标","/meta/module/gdl/composite/compositeGdlMag.jsp?gdlId="+gdlId,"top","comcgdl_"+gdlId,true);
}

//修改指标
function updateGdl(gdlId){
    var gdlType = dataTable.getUserData(gdlId,"GDL_TYPE");
    var gdlVersion = dataTable.getUserData(gdlId,"GDL_VERSION");
    var gdlCode = dataTable.getUserData(gdlId,"GDL_CODE");
    if(gdlType==0){
        //基础指标
        openMenu("修改基础指标("+gdlCode+")","/meta/module/gdl/gdlBasic/updateGdlBasic.jsp?gdlId="+gdlId+"&gdlVersion="+gdlVersion,"top","bsgdl_"+gdlId);
    }else if(gdlType==1){
        //复合指标
        openMenu("修改复合指标("+gdlCode+")","/meta/module/gdl/composite/compositeGdlMag.jsp?gdlId="+gdlId+"&optFlag=1","top","comgdl_"+gdlId);
    }else if(gdlType==2){
        //计算指标
        openMenu("修改计算指标("+gdlCode+")","/meta/module/gdl/calc/calcGdlMag.jsp?gdlId="+gdlId,"top","calcgdl_"+gdlId);
    }

}

//查看指标
function viewGdl(gdlId){
    var gdlType = dataTable.getUserData(gdlId,"GDL_TYPE");
    var gdlVersion = dataTable.getUserData(gdlId,"GDL_VERSION");
    var gdlCode = dataTable.getUserData(gdlId,"GDL_CODE");
    if(gdlType==0){
        //基础指标查看
        openMenu("基础指标("+gdlCode+")","/meta/module/gdl/gdlBasic/viewGdlBasic.jsp?gdlId="+gdlId+"&gdlVersion="+gdlVersion,"top","bsgdlview_"+gdlId);
    }else if(gdlType==1){
        //复合指标
        openMenu("复合指标("+gdlCode+")","/meta/module/gdl/composite/compositeGdlView.jsp?gdlId="+gdlId+"&gdlVersion="+gdlVersion,"top","comgdlview_"+gdlId);
    }else if(gdlType==2){
        //计算
        openMenu("计算指标("+gdlCode+")","/meta/module/gdl/calc/calcGdlView.jsp?gdlId="+gdlId+"&gdlVersion="+gdlVersion,"top","calcgdlview_"+gdlId);
    }

}

//管理表关系
function magGdlTblRel(gdlId){
    var gdlCode = dataTable.getUserData(gdlId,"GDL_CODE");
    var gdlVersion = dataTable.getUserData(gdlId,"GDL_VERSION");
    openMenu("指标与表关系维护("+gdlCode+")","/meta/module/gdl/rel/relTableMag.jsp?gdlId="+gdlId,"top","tblrel_"+gdlId);
}

//查看表关系
function viewGdlTblRel(gdlId){
    var gdlCode = dataTable.getUserData(gdlId,"GDL_CODE");
    var gdlVersion = dataTable.getUserData(gdlId,"GDL_VERSION");
    openMenu("指标表关系视图("+gdlCode+")","/meta/module/gdl/rel/relTableView.jsp?gdlId="+gdlId+"&gdlVersion="+gdlVersion,"top","tblrelview_"+gdlId);
}

//查看指标血缘
function viewGdlRel(gdlId){
    var gdlCode = dataTable.getUserData(gdlId,"GDL_CODE");
    var gdlVersion = dataTable.getUserData(gdlId,"GDL_VERSION");
    openMenu("指标血缘视图("+gdlCode+")","/meta/module/gdl/rel/relGdlView.jsp?gdlId="+gdlId+"&gdlVersion="+gdlVersion,"top","gdlrel_"+gdlId);
}

dhx.ready(pageInit);