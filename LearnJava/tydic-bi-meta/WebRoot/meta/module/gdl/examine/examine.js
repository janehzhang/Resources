/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co.Ltd.
 *All rights reserved.
 *
 *Filename：
 *        examine.js
 *Description：
 *       指标审核
 *Dependent：
 *
 *Author:
 *        刘弟伟
 ********************************************************/

var viewTableWin = null;   //查看信息窗体
var dataTable = null;//数据表格

/**
 * 页面初始
 */
function pageInit(){
    var termReq = TermReqFactory.createTermReq(1);
    var gdlTypeTerm=termReq.createTermControl("gdlType","gdlType"); //下拉框
    var alertTypeTerm=termReq.createTermControl("alertType","alertType"); //下拉框
    var gdlStateTerm=termReq.createTermControl("gdlState","gdlState");//下拉框
    var keyword = termReq.createTermControl("keyWord","keyWord");
	keyword.setWidth(380);
    
    gdlTypeTerm.setClassRule("tydic.meta.module.gdl.group.GdlGroupTreeImpl",2);
    gdlTypeTerm.mulSelect = true;
    gdlTypeTerm.setWidth(380);
    gdlTypeTerm.setBindDataCall(function(t){
        t.selTree.tree.openAllItems(t.selTree.tree.rootId);
    });
    gdlTypeTerm.enableReadonly(true);
    gdlTypeTerm.render();
    gdlTypeTerm.selTree.enableMuiltselect(true,function(d){
        return d[2]!="0";//根分类不允许选择
    });

    alertTypeTerm.setAppendData([[0,"全部"]]);
    alertTypeTerm.setCodeRule("GDL_ALERT_TYPE",[0],[3,4]); //设置条件数据来自码表
    alertTypeTerm.enableReadonly(true);
    gdlStateTerm.setCodeRule("GDL_AUDIT_STATE",[0]); //设置条件数据来自码表
    gdlStateTerm.enableReadonly(true);
    //设置条件文本框回车事件
    keyword.setInputEnterCall(function(){
        dataTable.refreshData();
    });

    dataTableInit(); //初始数据表格  初始之后dataTable才会被实例化
    dataTable.setReFreshCall(queryData); //设置表格刷新的回调方法，即实际查询数据的方法

    dhx.showProgress("请求数据中");
    termReq.init(function(termVals){
        dhx.closeProgress();
        dataTable.refreshData();
    }); //打包请求数据，初始，传入回调函数，里面开始查询数据

    var queryBtn = document.getElementById("queryBtn");
    queryBtn.onclick = function(){
        dataTable.refreshData();
    };
}

/**
 * 数据表格初始
 */
function dataTableInit(){
    var dd = document.getElementById("dataDiv");
    var pageContent = document.getElementById("pageContent");
    var queryFormDIV = document.getElementById("queryFormDIV");
    queryFormDIV.style.width = document.body.offsetWidth -10  + "px";
//    dd.style.height = pageContent.offsetHeight-queryFormDIV.offsetHeight - 20 + "px";
    dataTable = new meta.ui.DataTable("dataDiv");//第二个参数表示是否是表格树
    dataTable.setColumns({
        GDL_CODE:"编码",
        GDL_NAME:"名称",
        GDL_TYPE:"类型",
        AUDIT_STATE:"审核状态",
        GDL_UNIT:"单位",
        USER_ID:"创建人",
        GDL_BUS_DESC:"业务解释",
        ALTER_TYPE:"变更类型",
        opt:"操作"
    },"GDL_CODE,GDL_NAME,GDL_TYPE_NAME,AUDIT_STATE_NAME,GDL_UNIT,USER_NAMECN,GDL_BUS_DESC,ALTER_TYPE_NAME");//数组格式
    
    dataTable.setRowIdForField("GDL_ID"); //设置行ID对应的字段索引，以及父ID对应的字段索引
    dataTable.setPaging(true,20);//分页
    dataTable.setSorting(false,{
        GDL_NAME:"asc"
    },1);//排序
    dataTable.render();//绘制函数，一些set方法必须在绘制函数之前，绘制函数之后内置的源生dhtmlxGrid对象被初始

    dataTable.setFormatCellCall(function(rid,cid,data,colName){
    	if(cid == 8){
            //将第8列变成超链接，点击时弹出一个window
        	var gdl_id = dataTable.getUserData(rid,'GDL_ID');
        	var gdl_version = dataTable.getUserData(rid,'GDL_VERSION');
        	var auditSTate = dataTable.getUserData(rid,'AUDIT_STATE');
        	var alertType = dataTable.getUserData(rid,'ALTER_TYPE');
        	if(auditSTate == 0){
        		if(alertType == 1){
        			return "<a href='javascript:void(0)' onclick='addExamine("+gdl_id+","+gdl_version+");return false;'>审核</a>";
        		}else{
        			return "<a href='javascript:void(0)' onclick='alertExamine("+gdl_id+","+gdl_version+");return false;'>审核</a>";
        		}	
             }else{
            	 if(alertType == 1){
        			return "<a href='javascript:void(0)' onclick='showAddExamine("+gdl_id+","+gdl_version+");return false;'>查看</a>";
        		}else{
        			return "<a href='javascript:void(0)' onclick='showAlertExamine("+gdl_id+","+gdl_version+");return false;'>查看</a>";
        		}	
             }
        }

        return data[cid];
    }); //单元格特殊处理回调
    
    return dataTable;
}


var addExamine = function(gdl_id,gdl_version){
	openMenu("指标审核","/meta/module/gdl/examine/examineOperate.jsp?gdlId="+gdl_id+"&gdlVersion="+gdl_version,"top",null,true);
}
var alertExamine = function(gdl_id,gdl_version){
	openMenu("指标审核","/meta/module/gdl/examine/alertExamine.jsp?gdlId="+gdl_id+"&gdlVersion="+gdl_version,"top",null,true);
}
var showAddExamine = function(gdl_id,gdl_version){
	openMenu("全息视图","/meta/module/gdl/examine/addExamineInfo.jsp?gdlId="+gdl_id+"&gdlVersion="+gdl_version,"top",null,true);
}
var showAlertExamine = function(gdl_id,gdl_version){
	openMenu("全息视图","/meta/module/gdl/examine/alertExamineInfo.jsp?gdlId="+gdl_id+"&gdlVersion="+gdl_version,"top",null,true);
}


/**
 * 查询数据
 * @param dataTable
 * @param params
 */
function queryData(dataTable,params){
    var termVals=TermReqFactory.getTermReq(1).getKeyValue();
    //termVals["COLUMN_SORT"] = params.sort;
    dhx.showProgress("请求数据中");
    GdlExamineAction.getExamineInfo(termVals,{posStart:params.page.rowStart,count:params.page.pageSize},function(data){
        dhx.closeProgress();
        var total = 0;
        if(data && data[0])
            total = data[0]["TOTAL_COUNT_"];
        dataTable.bindData(data,total); //查询出数据后，必须显示调用绑定数据的方法
    });
}

dhx.ready(pageInit);