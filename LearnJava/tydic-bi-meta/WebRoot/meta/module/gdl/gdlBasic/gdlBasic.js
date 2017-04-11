/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       gdlBasic.js
 *Description：
 *       指标管理-基础指标申请
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       李国民
 *Date：
 *       2012-06-05
 ********************************************************/

var dataTable = null;	//数据表
//初始界面
function pageInit() {
    var termReq = TermReqFactory.createTermReq(1);
    var dataSourceId = termReq.createTermControl("dataSourceId","DATA_SOURCE_ID");	//数据源
    var tableOwner = termReq.createTermControl("tableOwner","TABLE_OWNER");			//所属用户
    var keywords = termReq.createTermControl("keywords","KEYWORDS");				//关键字

    dataSourceId.setAppendData([["","全部"]]);
    dataSourceId.setListRule(1,"SELECT DATA_SOURCE_ID, DATA_SOURCE_NAME "+
                            "FROM META_DATA_SOURCE WHERE DATA_SOURCE_TYPE='TABLE' AND DATA_SOURCE_STATE=1" +
                            " ORDER BY DATA_SOURCE_ID","");   //设置条件数据来自SQL
    
    tableOwner.setListRule(1,"select username a,username b from all_users order by USERNAME","");
    tableOwner.setParentTerm(dataSourceId); //设置级联，改变数据源时，级联刷新所属用户
    
    dataSourceId.setValueChange(function(value){
        if(value){
            tableOwner.dataSrcId = value[0];
        }
        if(value[0]===""){
            tableOwner.clearValue();
            return false;
        }
        return true;
    });  //值改变回调
	
    //设置关键字回车事件
    keywords.setInputEnterCall(function(){
    	dataTable.Page.currPageNum =1;		//重置分页
        dataTable.refreshData();
    });
	var queryBtn = document.getElementById("queryBtn");				//查询按钮
    attachObjEvent(queryBtn,"onclick",function(){
    	dataTable.Page.currPageNum =1;		//重置分页
        dataTable.refreshData();
    });
	
	dataTableInit(); //初始数据表格  
    dataTable.setReFreshCall(queryData); //设置表格刷新的回调方法，即实际查询数据的方法

    dhx.showProgress("请求数据中");
    termReq.init(function(termVals){
    	tableOwner.clearValue();	//初始时，清空所属用户值
        dhx.closeProgress();
        dataTable.refreshData();
    }); //打包请求数据，初始，传入回调函数，里面开始查询数据
}

//初始数据grid
function dataTableInit(){
	var dtatGridDiv = document.getElementById("dataGridDiv");
    var pageContent = document.getElementById("pageContent");
    var queryFormDIV = document.getElementById("queryFormDIV");
    queryFormDIV.style.width = document.body.offsetWidth-10  + "px";
    dtatGridDiv.style.height = pageContent.offsetHeight-queryFormDIV.offsetHeight -5 + "px";
    dataTable = new meta.ui.DataTable("dataGridDiv",false);//第二个参数表示是否是表格树
    dataTable.setColumns({
        TABLE_NAME:"表类名",
        TABLE_NAME_CN:"表类中文名称",
        TABLE_TYPE_NAME:"层次分类",
        TABLE_GROUP_NAME:"业务类型",
        GDL_NUMBER:"已创建/可创建指标数",
        OPT:"操作"
    },"TABLE_NAME,TABLE_NAME_CN,TABLE_TYPE_NAME,TABLE_GROUP_NAME,GDL_NUMBER,TABLE_ID");
    dataTable.setRowIdForField("TABLE_ID","TABLE_ID");
    dataTable.setPaging(true,20);//分页
    dataTable.setSorting(false);
    dataTable.render();//绘制函数，一些set方法必须在绘制函数之前，绘制函数之后内置的源生dhtmlxGrid对象被初始
    
    dataTable.setFormatCellCall(function(rid,cid,data,colId){
        if(colId=="OPT"){
        	var tableName = dataTable.getUserData(rid,"TABLE_NAME");	//取得某一行的某一列数据
        	var tableId = data[cid];
            return "<a href='javascript:void(0)' onclick='addGdlBasic(\""+tableName+"\",\""+tableId+"\");'>创建基础指标</a>";
        }
        return data[cid];
    });

    return dataTable;
}

//查询数据
function queryData(dataTable,params){
    var termVals=TermReqFactory.getTermReq(1).getKeyValue();
    dhx.showProgress("请求数据中");
    GdlBasicMagAction.queryTableInfoToGdl(termVals,{posStart:params.page.rowStart,count:params.page.pageSize},function(data){
        dhx.closeProgress();
        var total = 0;
        if(data && data[0]){
            total = data[0]["TOTAL_COUNT_"];
        }
        dataTable.bindData(data,total);
    });

}

//创建基础指标
function addGdlBasic(tableName,tableId){
	openMenu(tableName+"基础指标创建","/meta/module/gdl/gdlBasic/addGdlBasic.jsp?tableId="+tableId,"top");
}

dhx.ready(pageInit);