/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        mappingView.js
 *Description：
 *        查看映射JS文件
 *Dependent：
 *       dhtmlx.js、dwr相关JS文件、dhtmlxExtend.js、tool。js等。
 *Author:
 *        张伟
 *Finished：
 *       2011-11-22
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:

 ********************************************************/
var dwrCaller = new biDwrCaller();
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
//接口表信息。
var dimTabIntRel=null;

var dimTypeInfo=null;
var dimInfo=null;
var gridConvertConfig = {
    filterColumns:["SRC_NAME","SRC_CODE","CODE",
        "NAME","ISMAPPING"],
    onBeforeConverted:function(data){
        var  mappingStatus=queryform.getItemValue("mappingStatus");
        var sysId=queryform.getItemValue("system")
        data=data||[];
        if(data){
            for(var i=0;i<data.length;i++){
                //判读标准，如果其子节点数目为0，即为最后一级，因为其不显示，将其CODE和NAME替换为父级
                if(data[i].ISMAPPING){
                    if(!dimInfo.lastLevelFlag&&!data[i].CHILDRENCOUNT){
                        data[i].CODE=data[i].PAR_CODE;
                        data[i].NAME=data[i].PAR_NAME;
                    }
                }
            }
        }
    },
    isFormatColumn:false
}
var gridConverter=new dhtmxGridDataConverter(gridConvertConfig);

//加载系统数据Action
dwrCaller.addAutoAction("systemOption",DimMappingAction.systemOption,tableId,{
    dwrConfig:true,
    async:false,
    converter:new columnNameConverter()
});
//查询映射数据Action定义
dwrCaller.addAutoAction("queryDimMapping",DimMappingAction.queryDimMapping,{
    dwrConfig:true,
    converter:gridConverter,
    isDealOneParam:false
});

var mappingViewInit=function(){
    dwrCaller.executeAction("systemOption",function(data){
        dimTabIntRel=data;
    });
    if(!dimTabIntRel||dimTabIntRel.length==0){
        document.body.innerHTML= "<span style='font-weight: bolder;'>表："+tableName.toUpperCase()+"未注册接口表，无法映射</span>";
       // dhx.alert("表："+tableName.toUpperCase()+"未注册接口表，无映射可查询");
        return;
    }
    intTableOwner = dimTabIntRel[0].tableOwner;
    dimInfo=window.parent.dimInfo;
    dimInfo.tableOwner = tableOwner;
    dimInfo.intTableOwner = intTableOwner;
    dimTypeInfo=window.parent.dimTypeInfo;
    //布局初始化
    var mappingLayout = new dhtmlXLayoutObject(document.body, "2E");
    mappingLayout.cells("a").setText("<span style='font-weight: normal;'>查看编码映射：</span>" + tableNameCn);
    mappingLayout.cells("b").hideHeader();
    mappingLayout.cells("a").setHeight(70);
    mappingLayout.cells("a").fixSize(false, true);
    mappingLayout.hideConcentrate();
    mappingLayout.hideSpliter();

    //加载查询表单
    queryform = mappingLayout.cells("a").attachForm([
        {type:"settings",position: "label-left", labelWidth: 70, inputWidth: 120},
        {type:"select",label:"应用：",name:"system"},
        {type:"newcolumn"},
        {type:"input",label:"关键字：",name:"keyWord"},
        {type:"newcolumn"},
        {type:"select",label:"映射状态：",name:"mappingStatus",options:[
            {text:"所有",value:"0" },
            {text:"尚未映射",value:"1" },
            {text:"尚未审核",value:"2" },
            {text:"映射已审核",value:"3" }
        ]},
        {type:"newcolumn"},
        {
            type:"select",label:"归并类型",name:"dimTypeId"
        },
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"}
    ]);

    //加入应用下拉数据
    Tools.addOption(queryform.getSelect("system"),new dhtmlxComboDataConverter({
        valueColumn:"sysId",
        textColumn:"sysName",
        isFormatColumn:false
    }).convert(dimTabIntRel));

    var dimTypeSelect=queryform.getSelect("dimTypeId");
    var dimCount=0;
    for(var key in dimTypeInfo){
        dimTypeSelect.options[dimCount++]=new Option(dimTypeInfo[key].dimTypeName,dimTypeInfo[key].dimTypeId);
    };
    //初始化查询grid
    var imagePath = dhtmlx.image_path + "csh_" + dhtmlx.skin + "/";
    dataGrid = mappingLayout.cells("b").attachGrid();
    dataGrid.setImagePath(imagePath);
    dataGrid.setIconPath(imagePath);
    dataGrid.setHeader("应用系统,#cspan,原数据维度,#cspan,#cspan",null,["text-align:center;","text-align:center"]);
    dataGrid.attachHeader("原编码名称,原编码,维度编码,维度值,映射状态"
            ,["text-align:left;","text-align:center;","text-align:center","text-align:center;","text-align:center"]);
    dataGrid.setInitWidthsP("30,10,25,25,10");
    dataGrid.enableResizing("true,true,true,true,true");
    dataGrid.setColTypes("ro,ro,ro,ro,ro");
    dataGrid.setColAlign("left,center,center,center,center");
    dataGrid.setColSorting("na,na,na,na,na");
    dataGrid.setColumnCustFormat(4,function(value){
        return value==0?"未映射":"已映射";
    })
    dataGrid.init();
    dataGrid.setEditable(false);

    var query=function(queryParam){
        dataGrid.clearAll();
        dataGrid.load(dwrCaller.queryDimMapping+queryParam,"json");
    }
    var mappingParam = new biDwrMethodParam();
    mappingParam.setParamConfig([
        {
            index:1,type:"fun",value:function(){ //索引0位的参数来源由表单提供。
            return queryform.getFormData();
        }},
        {
            index:0,type:"static",value:dimInfo
        },{
            index:2,type:"fun",value: function(){
                var sysId=dwr.util.getValue(queryform.getSelect("system"));
                for(var i=0;i<dimTabIntRel.length;i++){
                    if(dimTabIntRel[i].sysId==sysId){
                        return dimTabIntRel[i].intTabName;
                    }
                }
            }
        }
    ]);
    query(mappingParam);
    queryform.attachEvent("onButtonClick", function(id){
        if(id == "query"){
            query(mappingParam);
        }
    });
}
dhx.ready(mappingViewInit);