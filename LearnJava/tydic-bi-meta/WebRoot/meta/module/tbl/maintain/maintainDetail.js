/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        maintain.js
 *Description：
 *       维护表类具体信息JS
 *Dependent：
 *       dhtmlx.js、dwr相关JS文件、dhtmlxExtend.js、tool。js等。
 *Author:
 *        刘斌
 *Finished：
 *       2011-11-14
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/

/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
///**
// * 声明dwrCaller
// */
//var dwrCaller = new biDwrCaller();
//dwrCaller.isDealOneParam(false);

/**
 * 页面初始化方法
 */

var isInitBasicInfo=false;
var maintainDetailInit=function(){

    var mainLayout = new dhtmlXLayoutObject(document.getElementById("detail"), "1C");
    mainLayout.cells("a").hideHeader();
    var tabbar=mainLayout.cells("a").attachTabbar();
    tabbar.setHrefMode("iframes");
    tabbar.addTab("basicInfo", "基本信息", "120px");
    tabbar.addTab("tableRef", "表类关系", "120px");
    tabbar.addTab("tableInstance", "表类实例", "120px");
    tabbar.addTab("diffAnalysis", "差异分析", "120px");
    tabbar.addTab("changeHistory", "变动历史", "120px");
    tabbar.setHrefMode("iframes-on-demand");
    tabbar.setContentHref("changeHistory",urlEncode(getBasePath()+"/meta/module/tbl/maintain/changeHistory.jsp?tableId="+tableId));
    //差异分析
    tabbar.setContentHref("diffAnalysis",urlEncode(getBasePath()+"/meta/module/tbl/diff/diffAnalysis.jsp?tableId="
        +tableId+"&tableVersion="+tableVersion+"&tableName="+tableName));
    tabbar.setContentHref("tableRef", urlEncode(getBasePath()+"/meta/module/tbl/maintain/tableRef.jsp?tableId="
        +tableId+"&tableVersion="+tableVersion+"&tableName="+tableName));
    //修改基本信息
    tabbar.setContentHref("basicInfo",urlEncode(getBasePath()+"/meta/module/tbl/maintain/basicInfo.jsp"+"?tableId="+tableId+"&tableVersion="+tableVersion
        +"&tableName="+tableName));
    //实例表类
    tabbar.setContentHref("tableInstance",urlEncode(getBasePath()+"/meta/module/tbl/maintain/tableInstance.jsp"+"?tableId="+tableId+"&tableVersion="+tableVersion
        +"&tableName="+tableName));

    tabbar.attachEvent("onSelect", function() {
        if(arguments[0]=="basicInfo"){
            if(isDimTable=="Y"){
                tabbar.setContentHref("basicInfo", urlEncode(getBasePath()+"/meta/module/tbl/dim/updateDim.jsp?tableId="+tableId+"&tableVersion="+tableVersion));
                return true;
            }else{
                if(!isInitBasicInfo){
                    //修改基本信息
                    tabbar.setContentHref("basicInfo",urlEncode(getBasePath()+"/meta/module/tbl/maintain/basicInfo.jsp"+"?tableId="+tableId+"&tableVersion="+tableVersion
                        +"&tableName="+tableName));
                    isInitBasicInfo=true;
                }
                return true;
            }
        }else if(arguments[0] == "diffAnalysis"){
            if(tableState == 1){
            	 tabbar.setContentHref("tableInstance",urlEncode(getBasePath()+"/meta/module/tbl/maintain/tableInstance.jsp"+"?tableId="+tableId+"&tableVersion="+tableVersion
        		+"&tableName="+tableName));
            	 return true;
            }else{
            	dhx.alert("表类状态为有效才能同步到实例！");
            }
        }else{
            return true;
        }
    });
    tabbar.setTabActive(focus);
};

dhx.ready(maintainDetailInit);
