/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        dimtype.js
 *Description：
 *        维度归并类型管理JS
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        王晶
 *Finished：
 *       2011-11-07
 *Modified By：
 *      
 *Modified Date:
 *       
 *Modified Reasons:
 *     
 ********************************************************/
/****************全局设置start*****************************************/
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller = new biDwrCaller();
/****************全局设置end*******************************************/
//维表名称的加载方法 
var tablesConverter = new dhtmlxComboDataConverter({
    valueColumn:"dimTableId",
    textColumn:"tableName"
});
dwrCaller.addAutoAction("queryDimTable", "DimTypeAction.queryDimTable");
dwrCaller.addDataConverter("queryDimTable", tablesConverter);
var initPage = function(){
    var typeLayout = new dhtmlXLayoutObject("container", "2E");
	    typeLayout.cells("a").setText("维护归并类型");
        typeLayout.cells("b").hideHeader();
        typeLayout.cells("a").setHeight(80);
        typeLayout.cells("a").fixSize(false, true);
        typeLayout.hideConcentrate();
        typeLayout.hideSpliter();
        var queryData = [{type:"setting",position: "label-left"},
    	                 {type:"block",list:[          
    	            	                 {type:"combo",label:"维表名称：",name:"tableId",labelWidth:55,inputWidth:160,inputHeight:17,className:'inputStyle queryItem',readonly:true},
    	            	                 {type:"newcolumn"},
                                         {type:"button",name:"query",value:"查询",className:'inputStyle'}
                                        ]}
    	
    ]//end queryData
    var queryform = typeLayout.cells("a").attachForm(queryData);
    //加载维表名称
    queryform.getCombo("tableId").loadXML(dwrCaller.queryDimTable, function() {
        queryform.getCombo("tableId").selectOption(0,true,true);
        var deftableId = queryform.getCombo("tableId").getSelectedValue();
        typeLayout.cells("b").attachURL(urlEncode(getBasePath()+"/meta/module/dim/dimtype/dimtypeInfo.jsp?tableId="+deftableId));
    });
   
    //查询事件
    queryform.attachEvent("onButtonClick",function(){
     var tableId = queryform.getCombo("tableId").getSelectedValue();
     if(tableId==null||tableId==undefined){
    	 tableId = 0;
     }
     typeLayout.cells("b").attachURL(urlEncode(getBasePath()+"/meta/module/dim/dimtype/dimtypeInfo.jsp?tableId="+tableId));
    })//end queryEvent
     
}//end initPage
dhx.ready(initPage);
