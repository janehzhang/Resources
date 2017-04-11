/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *
 *Description：指标与表类关系查看
 *
 *Dependent：
 *
 *Author:
 *        刘斌
 *Finished：
 *       2012-06-05
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();

var flow = null;
var gdlVersion = null;
var gdlType = null;
var gdlCode = null;

/**
 * 初始化init
 */
function relViewInit(){
    GdlRelAction.queryToFlowRelTable(gdlId,function(data){
        if(data.gdlType!=null){
            gdlType = data.gdlType;
        }
        if(data.gdlVersion!=null){
            gdlVersion = data.gdlVersion;
        }
        if(data.gdlCode !=null){
            gdlCode = data.gdlCode;
        }
        //装载DATA
        if(!flow){
            flow = new Flow({
            div:'flow_div',
            layout:'queue',
            swf:getBasePath() + '/meta/resource/swf/Flow.swf',
            onReady:function(flow){
                flow.hidePalette();
                flow.hideSelectBtn();
                flow.hideLinkBtn();
                flow.hideEditBtn();
                flow.hideRemoveBtn();
                flow.loadNodes(data);
            },
            onNodeDblClick:function(nodeId){
                //节点双击事件：显示具体表信息
                flowNodeDbClk(nodeId);
            },onLinkDblClick:function(from,to){
                //箭头双击事件：显示具体关联的列信息
                linkDbClick(from,to);
            }
        });
        }else{
            flow.loadNodes(data);
        }
    });

}

var linkViewWin = null;

var linkTabbar = null;

function linkDbClick(from, to){
    if(!linkViewWin){
        //初始化新增弹出窗口。
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("linkViewWin", 0, 0, 550, 360);
        linkViewWin = dhxWindow.window("linkViewWin");

        linkViewWin.setModal(true);
        linkViewWin.stick();
        linkViewWin.setDimension(550, 360);
        linkViewWin.center();
        linkViewWin.denyResize();
        linkViewWin.denyPark();
        linkViewWin.button("minmax1").hide();
        linkViewWin.button("park").hide();
        linkViewWin.button("stick").hide();
        linkViewWin.button("sticked").hide();
        linkViewWin.setModal(true);
        linkViewWin.keepInViewport(true);
        linkViewWin.attachEvent("onClose", function(){
            linkViewWin.hide();
            linkViewWin.setModal(false);
            return false;
        });

        linkViewWin.setText("指标与表类关联字段选择");
        linkViewWin.show();

        var addLayout = new dhtmlXLayoutObject(linkViewWin, "1C");
        addLayout.cells("a").hideHeader();
        addLayout.cells("a").attachObject($("linkSetDiv"));
        checkButton = Tools.getButtonNode("关闭",getBasePath()+'/meta/resource/images/cancel.png');
        checkButton.style.marginLeft='210px';
        checkButton.onclick=function(){
            linkViewWin.close();
        };
        checkButton.style.cssFloat='left';
        checkButton.style.paddingTop="5px";
        checkButton.style.styleFloat='left';

        $("_addButtonDiv").appendChild(checkButton);

        //加入TAB
        linkTabbar = new dhtmlXTabBar("linkDiv", "top");
        linkTabbar.addTab("a1", "关联列", "100px");
        linkTabbar.addTab("a2", "绑定维度", "100px");
        linkTabbar.addTab("a3", "支撑维度", "100px");
        linkTabbar.setTabActive("a1");
    }
    linkViewWin._tableId = from.replace("tbl_","");
    initLinkDetail(from.replace("tbl_",""));
    linkViewWin.setModal(true);
    linkViewWin.show();

}

var isInit = false;
var dwrCaller = new biDwrCaller();
//关联表列信息列表
var relColGrid = null;
//绑定维度信息列表
var bandDimGrid = null;
//支撑维度信息列表
var supportDimGrid = null;

var relColConfig = {
    idColumnName:"colId",
    filterColumns:["colName","colNameCn","colDatatype"]
};
var relColConverter = new dhtmxGridDataConverter(relColConfig);

var bandDimConverter = new dhtmxGridDataConverter({filterColumns:["dimNameCn","dimTypeName","dimCode"]});

var supportDimConvert = new dhtmxGridDataConverter({filterColumns:["dimNameCn","groupMethod"]});

function initLinkDetail(tableId){
    if(!isInit){
        linkTabbar.cells("a1").attachObject($("relColGridDiv"));
        linkTabbar.cells("a2").attachObject($("bandDimGridDiv"));
        linkTabbar.cells("a3").attachObject($("supportDimGridDiv"));
    }
    dwrCaller.addAutoAction("queryRelCol", "GdlRelAction.queryGdlColByTableId",tableId);
    dwrCaller.addDataConverter("queryRelCol", relColConverter);
    dwrCaller.addAutoAction("queryBandDim", "GdlRelAction.queryBandDim",gdlId,tableId);
    dwrCaller.addDataConverter("queryBandDim", bandDimConverter);
    dwrCaller.addAutoAction("querySupportDim", "GdlRelAction.querySupportDim",gdlId,tableId);
    dwrCaller.addDataConverter("querySupportDim", supportDimConvert);
    //初始化表列信息
    if(!relColGrid){
        relColGrid = new dhtmlXGridObject('relColGridDiv');
        relColGrid.setHeader("列名,列中文名,类型");
        relColGrid.setInitWidthsP("32,34,34");
        relColGrid.setColAlign("left,left,left");
        relColGrid.setColTypes("ro,ro,ro");
        relColGrid.setHeaderAlign("center,center,center");
        relColGrid.init();
        relColGrid.defaultPaging(10);
        relColGrid.load(dwrCaller.queryRelCol, "json");

    }else{
        relColGrid.clearAll();
        relColGrid.load(dwrCaller.queryRelCol, "json");
    }
    //初始化绑定维度信息列表
    if(!bandDimGrid){
        bandDimGrid = new dhtmlXGridObject('bandDimGridDiv');
        bandDimGrid.setHeader("维度名,归并类型,维度值");
        bandDimGrid.setInitWidthsP("33,33,33");
        bandDimGrid.setColAlign("left,left,left");
        bandDimGrid.setColTypes("ro,ro,ro");
        bandDimGrid.setHeaderAlign("center,center,center");
        bandDimGrid.init();
        bandDimGrid.defaultPaging(10);
        bandDimGrid.load(dwrCaller.queryBandDim, "json");
    }else{
        //重新加载
        bandDimGrid.clearAll();
        bandDimGrid.load(dwrCaller.queryBandDim, "json");
    }

    //初始化支撑维度信息列表
    if(!supportDimGrid){
        supportDimGrid = new dhtmlXGridObject('supportDimGridDiv');
        supportDimGrid.setHeader("维度名,分组计算方法");
        supportDimGrid.setInitWidthsP("50,50");
        supportDimGrid.setColAlign("left,left");
        supportDimGrid.setColTypes("ro,ro");
        supportDimGrid.setHeaderAlign("center,center");
        supportDimGrid.setColumnCustFormat(1,transGroupMethod);
        supportDimGrid.init();
        supportDimGrid.defaultPaging(10);
        supportDimGrid.load(dwrCaller.querySupportDim, "json");
    }else{
        //重新加载
        supportDimGrid.clearAll();
        supportDimGrid.load(dwrCaller.querySupportDim, "json");
    }
    //初始化选择的列信息
    GdlRelAction.queryInfoByTableIdAndGdlId(tableId, gdlId, function(data){
        if(data.COL_NAME){
            $("_relCol").value=data.COL_NAME + " " + (data.COL_NAME_CN==null?"":data.COL_NAME_CN);
        }
    });


    isInit = true;

}

function transGroupMethod(val){
    return getNameByTypeValue("GDL_GROUP_METHOD",val);
}

/**
 * 节点双击事件
 * @param nodeId
 */
function flowNodeDbClk(nodeId){
    if(nodeId.indexOf("gdl_")>=0){//指标查看
        if(gdlType==0){
            //基础指标查看
            openMenu("基础指标("+gdlCode+")","/meta/module/gdl/gdlBasic/viewGdlBasic.jsp?gdlId="+gdlId+"&gdlVersion="+gdlVersion,"top","bsgdlview_"+gdlId);
        }else if(gdlType==1){
            //复合指标
            openMenu("复合指标("+gdlCode+")","/meta/module/gdl/composite/compositeGdlView.jsp?gdlId="+gdlId+"&gdlVersion="+gdlVersion,"top","comgdlview_"+gdlId);
        }else if(gdlType==2){
            //计算
            openMenu("计算指标("+gdlCode+")","/meta/module//gdl/calc/calcGdlView.jsp?gdlId="+gdlId+"&gdlVersion="+gdlVersion,"top","calcgdlview_"+gdlId);
        }
    }else{//表类查看
        GdlRelAction.flowNodeDetail(nodeId, function(data){
            if(data.TABLE_TYPE_ID != 2){//不是维表
                openMenu(data.TABLE_NAME+"全息视图","/meta/module/tbl/view/viewDetail.jsp?focus=basicInfo"+
                    "&tableId="+data.TABLE_ID+"&tableName="+data.TABLE_NAME+"&tableVersion="+data.TABLE_VERSION,
                    "top","relTableView_"+data.TABLE_ID);
            }else{
                openMenu(data.TABLE_NAME+"全息视图","/meta/module/tbl/view/viewDetail.jsp?focus=basicInfo"+
                    "&tableId="+data.TABLE_ID+"&tableName="+data.TABLE_NAME+"&tableVersion="+data.TABLE_VERSION+"&isDimTable=Y",
                    "top","relTableView_"+data.TABLE_ID);
            }
        });
    }
}

dhx.ready(relViewInit);

