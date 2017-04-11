/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        intRelQuery.js
 *Description：
 *       接口表查询JS
 *Dependent：
 *        dhtmlx.js、dwr相关JS文件、dhtmlxExtend.js、tool。js等。
 *Author:
 *        张伟
 *Finished：
 *       2011-11-19
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
var dwrCaller = new biDwrCaller();
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var sysData = [];
var dimData = [];

//table信息，以DimTableID为键值，以其中文名为value
var dimTable={};
//获取所有有效维表Action定义
dwrCaller.addAutoAction("queryValidDimTables",DimIntRelAction.queryValidDimTables,{
    dwrConfig:true,
    converter:new  dhtmlxComboDataConverter({
        valueColumn:"tableId",
        textColumn:"tableNameCn",
        onBeforeConverted:function(data){
            if(data&&data.length>0){
                for(var i=0;i<data.length;i++){
                    dimTable[data[i].TABLE_ID]=data[i].TABLE_NAME_CN;
                }
                data.unshift({TABLE_ID:"",TABLE_NAME_CN:""});
            }
            
        }
    }),
    async:false
});
/**
 * 查询系统信息Action定义
 */
dwrCaller.addAutoAction("queryAllSystem",DimIntRelAction.queryAllSystem,{
    dwrConfig:true,
    converter:new  dhtmlxComboDataConverter({
        valueColumn:"groupId",
        textColumn:"groupName",
         onBeforeConverted:function(data){
    	data.unshift({GROUP_ID:"",GROUP_NAME:""});
    }
    }),
    async:false
})

var intRelConfig = {
    filterColumns:["dimTableId","sysName","intTabName","_buttons"],
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData){
        return rowData;
    },
    /**
     * 获取下拉框Button的值
     * @param rowIndex
     * @param columnIndex
     * @param cellValue
     * @param rowData
     */
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        if(columnName == '_buttons'){//如果是第3列。即操作按钮列
            return "getRoleButtonsCol";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
};
/**
 * 数据展现转换器
 */
var intRelConverter = new dhtmxGridDataConverter(intRelConfig);

dwrCaller.addAutoAction("queryIntRelInfo",DimIntRelAction.queryIntRelInfo,{
    dwrConfig:true,
    converter:intRelConverter
})

var intRelInit=function(){
    //布局初始化"queryValidDimTables
	dwrCaller.executeAction("queryValidDimTables");
    var intRelLayout = new dhtmlXLayoutObject(document.body, "2E");
    intRelLayout.cells("a").setText("查询接口表");
    intRelLayout.cells("b").hideHeader();
    intRelLayout.cells("a").setHeight(80);
    intRelLayout.cells("a").fixSize(false, true);
    intRelLayout.hideConcentrate();
    intRelLayout.hideSpliter();//移除分界边框。

    //加载查询表单
    queryform = intRelLayout.cells("a").attachForm([
        {type:"settings",position: "label-left",inputWidth: 120},
        {type:"combo",label:"源系统：",name:"system"},
        {type:"newcolumn"},
        {type:"combo",label:"维度名称：",name:"dimTable"},
        
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"}
    ]);
//    //加载维度数据
//    dwrCaller.executeAction("queryValidDimTables",function(data){
//        Tools.addOption(queryform.getSelect("dimTable"),data);
//    })
//    //加载系统信息数据
//    dwrCaller.executeAction("queryAllSystem",function(data){
//        Tools.addOption(queryform.getCombo("system"),data);
//    });
    //加载维度数据
    
    //加载系统信息数据
    queryform.getCombo("dimTable").enableFilteringMode(false);
    queryform.getCombo("system").enableFilteringMode(false);
    queryform.getCombo("dimTable").setOptionHeight(200);
    queryform.getCombo("system").setOptionHeight(200);
    queryform.getCombo("system").loadXML(dwrCaller.queryAllSystem);
    queryform.getCombo("dimTable").loadXML(dwrCaller.queryValidDimTables);
    queryform.attachEvent("onChange",function(id){
    	if(id=="system"){
    		 
    	}
    })
    $("_sqlInfo").style.height=($("_layout").offsetHeight-$("_grid").offsetHeight-90)+"px";
    $("_sqlInfo").parentNode.style.height= ($("_layout").offsetHeight-$("_grid").offsetHeight-180)+"px";
    intRelLayout.cells("b").attachObject("_layout");
    intRelGrid=new dhtmlXGridObject("_grid");
    intRelGrid.setHeader("维度名称,源系统,接口表表名,操作");
    intRelGrid.setInitWidthsP("30,30,30,10");
    intRelGrid.setColAlign("left,left,left,center");
    intRelGrid.setHeaderAlign("center,center,center,center");
    intRelGrid.setColTypes("ro,ro,ro,sb");
    intRelGrid.setColSorting("na,na,na,na");
    intRelGrid.enableResizing("true,true,true,true");
    intRelGrid.setColumnCustFormat(0,function(value){
    	 return dimTable[value];
    });//第5列进行转义
    intRelGrid.enableCtrlC();
    intRelGrid.init();
//    intRelGrid.load(dwrCaller.loadUser,"json");
    intRelGrid.defaultPaging(10);
    intRelGrid.attachEvent("onRowSelect",function(id){
        var rowdata=intRelGrid.getRowData(id).userdata;
        $("_ruleComment").innerHTML=rowdata.dataMappMark==null?"&nbsp;":rowdata.dataMappMark;
        $("_mappingSQL").innerHTML=rowdata.dataMappSql==null?"&nbsp;":rowdata.dataMappSql;
    });
    intRelGrid.attachEvent("onXLE", function(){
        if(intRelGrid.getRowsNum() && !intRelGrid.getSelectedRowId()){
            intRelGrid.selectRow(0,true);//默认选择一行
        }});

    var queryParam = new biDwrMethodParam();
    queryParam.setParamConfig([
        {
            index:0,type:"fun",value:function(){ //索引0位的参数来源由表单提供。
            return  queryform.getItemValue("system");
        }},
        {
            index:1,type:"fun",value:function(){
            return queryform.getItemValue("dimTable")
        }
        }
    ]);
    var query=function(){
        intRelGrid.clearAll();
        intRelGrid.load(dwrCaller.queryIntRelInfo+queryParam,"json");
    }
    query();
    queryform.attachEvent("onButtonClick", function(id){
        if(id == "query"){
            query();
        }
    });
    var toolBar = new dhtmlXToolbarObject("_toolbar");
    toolBar.addText("text",1,'<span style="font-weight: bold;">映射规则信息</span>');

    var buttons = {
        intRelValue:{name:"intRelValue",text:"维度接口值",imgEnabled:getBasePath() + "/meta/resource/images/transparent.gif",
            imgDisabled:getBasePath() + "/meta/resource/images/transparent.gif",onclick:function(rowData){
                var url="/meta/module/dim/intRel/intRelValueQuery.jsp?tableId="+rowData.userdata.dimTableId+"&tableOwner="+rowData.userdata.tableOwner
                                +"&sysId="+rowData.userdata.sysId+"&tableName="+dimTable[rowData.userdata.dimTableId].TABLE_NAME
                                +"&tableNameCn="+ encodeURIComponent(encodeURIComponent(dimTable[rowData.userdata.dimTableId]))
                                +"&sysName="+  encodeURIComponent(encodeURIComponent(intRelGrid.cells(rowData.id,1).getValue()));
                window.parent.closeTab("维度接口值");
                openMenu("维度接口值",url,"top");
            }
        }}
    window['getRoleButtonsCol'] = function(){
        return [buttons.intRelValue];
    };
}

dhx.ready(intRelInit);