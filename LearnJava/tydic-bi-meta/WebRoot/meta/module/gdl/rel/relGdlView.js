/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *
 *Description：指标与指标关系查看
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

var flow = null;
var typeMap = null;
var versionMap = null;
var codeMap = null;

/**
 * 初始化init
 */
function relViewInit(){
    GdlRelGdlAction.queryToFlowRelGdl(gdlId, function(data){
        typeMap=data.typeMap;
        versionMap=data.versionMap;
        codeMap = data.codeMap;
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
//                flowLinkDbClk(from,to)
                }

            })
        }else{
            flow.loadNodes(data);
        }
    });
}

function flowNodeDbClk(nodeId){
    var gdlType = typeMap[nodeId];
    var gdlVersion = versionMap[nodeId];
    var gdlCode = codeMap[nodeId];
    var gdlId = nodeId.replaceAll("gdl_","");
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

}

dhx.ready(relViewInit);
