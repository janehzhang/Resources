/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *
 *Description：
 *
 *Dependent：
 *
 *Author:
 *        刘斌
 *Finished：
 *       2012-02-13
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
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();

var dataSourceConvertConfig = {
    idColumnName:"dataSourceId",
    filterColumns:["dataSourceName","dataSourceOraname","dataSourceUser",
        "dataSourceType","dataSourceRule","sysName",
        "dataSourceMinCount","dataSourceState","_buttons"],
    /**
     * 获取下拉框Button的值
     * @param rowIndex
     * @param columnIndex
     * @param cellValue
     * @param rowData
     */
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
        if (columnName == '_buttons') {//如果是第3列。即操作按钮列
            if(rowData.dataSourceState==0){//无效表
                return "getRoleButtonsColOff";
            }else{
                return "getRoleButtonsColOn";
            }
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    },
    /**
     * 实现 userData，将一些数据作为其附加属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData) {
        var userData = {};
        userData.dataSourcePass = rowData.dataSourcePass;
        userData.sysId = rowData.sysId;
        userData.dataSourceIntro = rowData.dataSourceIntro;
        return userData;
    }

};

var dataSourceDataConverter = new dhtmxGridDataConverter(dataSourceConvertConfig);

//数据源 码表
var dataSourceTypeData = null;
/**
 * 初始化方法
 */
var dataSourceInit = function(){
    var dataSourceLayout = new dhtmlXLayoutObject(document.getElementById("container"), "2E");
    dataSourceLayout.cells("a").setText("数据源管理");
    dataSourceLayout.cells("b").hideHeader();
    dataSourceLayout.cells("a").setHeight(75);
    dataSourceLayout.cells("a").fixSize(false, true);
    dataSourceLayout.hideConcentrate();
    dataSourceLayout.hideSpliter();//移除分界边框。

    //加载查询表单
    var queryform = dataSourceLayout.cells("a").attachForm([
        {type:"setting",position: "label-left", labelWidth: 120, inputWidth: 120},
        {type:"input",label:"关键字：",name:"dataSourceName",inputHeight : 17},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询",offsetLeft :0,offsetTop : 0}
    ]);

    var loadDataSourceParam = new biDwrMethodParam();
    loadDataSourceParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
            var formData=queryform.getFormData();
            formData.dataSourceName=Tools.trim(queryform.getInput("dataSourceName").value);
            return formData;
        }
        }
    ]);
    dwrCaller.addAutoAction("loadDataSource", "DataSourceAction.queryDataSource", loadDataSourceParam);
    dwrCaller.addDataConverter("loadDataSource", dataSourceDataConverter);

    //初始数据源下拉列表
    dataSourceTypeData = getComboByRemoveValue("DATA_SOURCE_TYPE");
    Tools.addOption("_dataSourceType", dataSourceTypeData);

    var base = getBasePath();
    var buttons = {
        reloadDataSource:{name:"reloadDataSource",text:"重载",imgEnabled:base + "/meta/resource/images/addGroup.png",
            imgDisabled:base + "/meta/resource/images/addGroup.png",onclick:function(rowData) {
                if(rowData.data[3]!="TABLE"){
                    dhx.alert("对不起，文件类型的数据源不能进行重载！");
                    return;
                }
                reloadDataSource(rowData.id);
            }},
        testDataSource:{name:"testDataSource",text:"测试",imgEnabled:base + "/meta/resource/images/addGroup.png",
            imgDisabled:base + "/meta/resource/images/addGroup.png",onclick:function(rowData) {
                testDataSource(rowData);
            }},
        addDataSource:{name:"addDataSource",text:"新增",imgEnabled:base + "/meta/resource/images/addGroup.png",
            imgDisabled:base + "/meta/resource/images/addGroup.png",onclick:function(rowData) {
                initSourceWindow();
            }},
        modifyDataSource:{name:"modifyDataSource",text:"修改",imgEnabled:base + "/meta/resource/images/editGroup.png",
            imgDisabled:base + "/meta/resource/images/editGroup.png",onclick:function(rowData) {
                initSourceWindow(rowData.id);
            }
        },
        onlineDataSource:{name:"onlineDataSource",text:"启用",imgEnabled:base + "/meta/resource/images/editGroup.png",
            imgDisabled:base + "/meta/resource/images/editGroup.png",onclick:function(rowData) {
                dhx.confirm("确认将"+rowData.data[0]+"启用？",function(r){
                    if(r){
                        onlineDataSource(rowData.id);
                    }
                });
            }
        },
        offlineDataSource:{name:"offlineDataSource",text:"停用",imgEnabled:base + "/meta/resource/images/editGroup.png",
            imgDisabled:base + "/meta/resource/images/editGroup.png",onclick:function(rowData) {
                dhx.confirm("确认将"+rowData.data[0]+"停用？",function(r){
                    if(r){
                        offlineDataSource(rowData.id);
                    }
                });
            }
        }

    };

//    var buttonRole = getRoleButtons();
    //过滤显示顶部按钮
    var bottonRoleRow = [];
    for(var i in buttons){
        if(i == "addDataSource"){
            bottonRoleRow.push(i);
        }
    }

    //过滤列按钮摆放
    var buttonRoleColon = ["reloadDataSource","testDataSource","modifyDataSource","offlineDataSource"];
    //getRoleButtonsCol
    window["getRoleButtonsColOn"]=function(){
        var res=[];
        for(var i=0;i<buttonRoleColon.length;i++){
            res.push(buttons[buttonRoleColon[i]]);
        }
        return res;
    };

    var buttonRoleColoff = ["reloadDataSource","testDataSource","modifyDataSource","onlineDataSource"];
    window["getRoleButtonsColOff"]=function(){
        var res=[];
        for(var i=0;i<buttonRoleColoff.length;i++){
            res.push(buttons[buttonRoleColoff[i]]);
        }
        return res;
    };

    //定义全局函数，用于获取有权限的button列表定义
    window["getButtons"] = function() {
        var res = [];
        for(var i=0;i<bottonRoleRow.length;i++){
            res.push(buttons[bottonRoleRow[i]]);
        }
        return res;
    };
    var buttonToolBar = dataSourceLayout.cells("b").attachToolbar();
    var pos = 1;
    var filterButton = window["getButtons"]();
    for (var i = 0; i < filterButton.length; i++) {
        buttonToolBar.addButton(filterButton[i].name, pos++, filterButton[i].text,
                filterButton[i].imgEnabled, filterButton[i].imgDisabled);
        var button=buttonToolBar.getItemNodeById(filterButton[i].name);
        button.setAttribute("id",filterButton[i].name);
    }

    //添加buttonToolBar事件
    buttonToolBar.attachEvent("onclick", function(id) {
        if(id=="addDataSource"){
            initSourceWindow();
        }
        if(id=="modifyDataSource"){
            var selecteddRowId = mygrid.getCheckedRows(0);

            if (selecteddRowId == null || selecteddRowId == "") {
                dhx.alert("请选择一行进行修改!");
                return;
            } else if (selecteddRowId.split(",").length > 1) {
                dhx.alert("只能选择一行进行修改!");
                return;
            } else {
                modifyDataSource(selecteddRowId);
            }
        }
    });

    mygrid = dataSourceLayout.cells("b").attachGrid();
    mygrid.setHeader("名称,主机名称,用户,类型,规则,所属系统,最小连接数,状态,操作");
    mygrid.setHeaderAlign("left,center,center,center,center,center,center,center,center");
    mygrid.setInitWidthsP("10,10,10,10,23,10,7,5,15");
    mygrid.setColAlign("left,left,left,center,left,left,center,center,center");
    mygrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,sb");
    mygrid.enableCtrlC();
    mygrid.enableResizing("true,true,true,true,true,true,true,true,false");
    mygrid.enableMultiselect(true);
    mygrid.setColumnIds("dataSourceName","dataSourceOraname","dataSourceUser",
            "dataSourceType","dataSourceRule","sysName",
            "dataSourceMinCount","dataSourceState");
    mygrid.enableTooltips("true,true,true,true,true,true,true,true,false");
    mygrid.setColumnCustFormat(7, dataSourceOnlineOrNot);//转义
    mygrid.setColumnCustFormat(3, dataSourceTableOrFile);//转义
    mygrid.setColumnCustFormat(4, transRule);//转义
    mygrid.init();
    mygrid.defaultPaging(20);
    mygrid.load(dwrCaller.loadDataSource, "json");

    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id) {
        if (id == "query") {
            //进行数据查询。
            mygrid.clearAll();
            mygrid.load(dwrCaller.loadDataSource, "json");
        }
    });
    // 添加Enter查询事件
    queryform.getInput("dataSourceName").onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            mygrid.clearAll();
            mygrid.load(dwrCaller.loadDataSource, "json");
        }
    }
    //加载所有的表类型。
    dwrCaller.executeAction("querySysInfo", function(data){
        data=data.options||data;
        groupData=data;
//        Tools.addOption("_tableType", data);
        dhx.env.isIE&&CollectGarbage();
    })
};

var groupData = null;
var initButton = false;
var addWindow = null;
/**
 * 新增数据源
 */
var initSourceWindow = function(dataSourceId){
    if(!addWindow){
        //初始化新增弹出窗口。
        var dhxWindow = new dhtmlXWindows();
        dhxWindow.createWindow("addWindow", 0, 0, 540, 300);
        addWindow = dhxWindow.window("addWindow");
        addWindow.setModal(true);
        addWindow.stick();
        addWindow.setDimension(540, 300);
        addWindow.center();
        addWindow.denyResize();
        addWindow.denyPark();
        addWindow.button("minmax1").hide();
        addWindow.button("park").hide();
        addWindow.button("stick").hide();
        addWindow.button("sticked").hide();
        addWindow.setModal(true);
        addWindow.keepInViewport(true);
        addWindow.attachEvent("onClose", function(){
            addWindow.hide();
            addWindow.setModal(false);
            return false;
        });
        addWindow.show();
        var addLayout = new dhtmlXLayoutObject(addWindow, "1C");
        addLayout.cells("a").hideHeader();
        addLayout.cells("a").attachObject($("_nodeDetail"));
        if(groupData){
            Tools.addOption("_sysId", groupData);
        }

        checkButton = Tools.getButtonNode("测试连接",getBasePath()+'/meta/resource/images/link.png');
        checkButton.style.marginLeft='115px';
        checkButton.onclick=checkBtnClk;
        checkButton.style.cssFloat='left';
        checkButton.style.paddingTop="2px";
        checkButton.style.styleFloat='left';

        addButton = Tools.getButtonNode("保存",getBasePath()+'/meta/resource/images/ok.png');
        addButton.onclick=saveBtnClk;
        addButton.style.cssFloat='left';
        addButton.style.paddingTop="2px";
        addButton.style.styleFloat='left';
        addButton.style.align='center';

        cancelButton = Tools.getButtonNode("取消",getBasePath()+'/meta/resource/images/cancel.png');
        cancelButton.style.cssFloat='left';
        cancelButton.style.styleFloat='left';
        cancelButton.style.paddingTop="2px";
        cancelButton.onclick = function() {
            addWindow.close();
        };
        $("_addButtonDiv").appendChild(checkButton);
        $("_addButtonDiv").appendChild(addButton);
        $("_addButtonDiv").appendChild(cancelButton);

        // 添加验证
        dhtmlxValidation.addValidation($("_nodeDetail"),[
            {target:"_dataSourceName", rule:"NotEmpty,MaxLength[64]"},
            {target:"_dataSourceOraname", rule:"NotEmpty,ValidAplhaNumeric,MaxLength[100]"},
            {target:"_dataSourceUser", rule:"NotEmpty,ValidAplhaNumeric,MaxLength[20]"},
            {target:"_dataSourcePass", rule:"NotEmpty,ValidAplhaNumeric,MaxLength[20]"},
            {target:"_dataSourceMinCount", rule:"NotEmpty,ValidInteger,MaxLength[9]"},
            {target:"_sysId", rule:"NotEmpty"},
            {target:"_ruleIp", rule:"ValidIPv4"},
            {target:"_rulePort", rule:"ValidNumeric"},
            {target:"_ruleInst", rule:"ValidAplhaNumeric,MaxLength[200]"},
//            {target:"_dataSourceRule", rule:"MaxLength[256],ValidByCallBack[vDataSourceRule]"},
            {target:"_dataSourceIntro", rule:"MaxLength[512]"},
            {target:"_fieldSplit", rule:"Asic,NotEmpty,MaxLength[10]"},
            {target:"_rowSplit", rule:"Asic,NotEmpty,MaxLength[10]"}
        ],"true");

    }else{
        addWindow.setModal(true);
        addWindow.show();
    }
    if(dataSourceId){//修改
        addWindow.setText("修改数据源");
        initPropPage(dataSourceId);
    }else{//新增
        addWindow.setText("新增数据源");
        initPropPage();
    }
};

/**
 * 保存按钮功能
 */
var saveBtnClk = function(){
    if(dhtmlxValidation.validate("_nodeDetail")){
        var saveFun = function(){
            var saveDate = {};
            saveDate.dataSourceId=$("_dataSourceId").value;
            saveDate.dataSourceName=$("_dataSourceName").value;
            saveDate.dataSourceOraname=$("_dataSourceOraname").value;
            saveDate.dataSourceUser=$("_dataSourceUser").value;
            saveDate.dataSourcePass=$("_dataSourcePass").value;
            saveDate.dataSourceMinCount=$("_dataSourceMinCount").value;
            saveDate.sysId=$("_sysId").value;

            if($("_dataSourceType").value=="TABLE"){//类型为数据库
                saveDate.dataSourceType=$("_dataSourceType").value;
                if($("_ruleIp").value==""||$("_rulePort").value==""||$("_ruleInst").value==""){
                    dhx.alert("请填写数据源规则！");
                    return;
                }
                saveDate.dataSourceRule="jdbc:oracle:thin:@"+$("_ruleIp").value+":"+$("_rulePort").value+":"+$("_ruleInst").value;
            }else{
                var sourceType="";
                if(!$("_ftp").checked&&!$("_zip").checked&&!$("_binary").checked){
                    dhx.alert("请至少选择一个FTP、ZIP或BINARY属性！");
                    return;
                }
                if(!$("_fileDmp").checked&&!$("_fileTxt").checked){
                    dhx.alert("请选择一个文件类型！");
                    return;
                }
                if($("_fileTxt").checked&&($("_fieldSplit").value==""||$("_fieldSplit").value=="字段分隔符"||$("_rowSplit").value==""||$("_rowSplit").value=="行分隔符")){
                    dhx.alert("请填写完整TXT文件的行分隔符以及列分隔符！");
                    return;
                }
                if($("_fileDmp").checked&&($("_fieldSplit").value==""||$("_fieldSplit").value=="字段分隔符"||$("_rowSplit").value==""||$("_rowSplit").value=="行分隔符")){
                    dhx.alert("请填写完整DMP文件的行分隔符以及列分隔符！");
                    return;
                }
                $("_ftp").checked?sourceType=sourceType+"FTP":sourceType=sourceType+"LOCAL";
                $("_zip").checked?sourceType=sourceType+"|ZIP":"";
                $("_binary").checked?sourceType=sourceType+"|BINARY":"";
                $("_fileDmp").checked?sourceType=sourceType+"|DMP":"";
                $("_fileTxt").checked?sourceType=sourceType+"|TXT":"";
                saveDate.dataSourceType=sourceType;
//            if($("_fileTxt").checked){
                saveDate.dataSourceRule=$("_fieldSplit").value+"+*+*+"+$("_rowSplit").value;
//            }else{
//                saveDate.dataSourceRule="";
//            }
            }
            saveDate.dataSourceIntro=$("_dataSourceIntro").value;
            DataSourceAction.saveOrupdate(saveDate, function(data){
                if(data.type=="error"||data.type=="invalid"){
                    dhx.alert("对不起，操作失败，请重试！");
                }else{
                    if(saveDate.dataSourceId == -1){
                        dhx.alert("新增成功！");
                        mygrid.updateGrid(dataSourceDataConverter.convert(data.successData),"insert");
                        addWindow.close();
                    }else{
                        dhx.alert("修改成功！");
                        mygrid.updateGrid(dataSourceDataConverter.convert(data.successData),"update");
                        addWindow.close();
                    }
                }
            });
        }

        if($("_dataSourceType").value=="TABLE"){//类型为数据库
            //TODO 先测试数据源
            var info = {};
//    info.dataSourceRule = $("_dataSourceRule").value;
            info.dataSourceRule = "jdbc:oracle:thin:@"+$("_ruleIp").value+":"+$("_rulePort").value+":"+$("_ruleInst").value;
            info.dataSourceUser = $("_dataSourceUser").value;
            info.dataSourcePass = $("_dataSourcePass").value;
            dwrCaller.executeAction("testDataSource", info, function(data){
                if(!data){
                    saveFun();
                }else{
                    dhx.confirm("连接无效！ 是否继续保存？", function(r){
                        if(r){
                            saveFun();
                        }
                    });
                }
            });
        }else{
            saveFun();
        }
    }
};

/**
 * 初始化新增以及修改弹出页面
 * @param dataSourceId
 */
var initPropPage = function(dataSourceId){
    if(dataSourceId){//修改页面
        var rowData = mygrid.getRowData(dataSourceId);
        $("_dataSourceId").value=rowData.id;
        $("_dataSourceName").value=rowData.data[0];
        $("_dataSourceOraname").value=rowData.data[1];
        $("_dataSourceUser").value=rowData.data[2];
        $("_dataSourcePass").value=rowData.userdata.dataSourcePass;
        if(rowData.data[6]){
            $("_dataSourceMinCount").value=rowData.data[6];
        }else{
            $("_dataSourceMinCount").value=deffaultMinCon;
        }
        if(rowData.userdata.dataSourceIntro){
            $("_dataSourceIntro").value=rowData.userdata.dataSourceIntro;
        }else{
            $("_dataSourceIntro").value="";
        }

        if(rowData.data[3]!="TABLE"){//类型为文件
            $("_dataSourceType").value="FILE";
            if(rowData.data[3].indexOf("FTP")>-1){
                $("_ftp").checked=true;
            }
            if(rowData.data[3].indexOf("ZIP")>-1){
                $("_zip").checked=true;
            }
            if(rowData.data[3].indexOf("BINARY")>-1){
                $("_binary").checked=true;
            }
            if(rowData.data[3].indexOf("TXT")>-1){//TXT型
                $("_fileTxt").checked=true;
                //解析字段分隔符和行分隔符
                if(rowData.data[4]&&rowData.data[4].split("+*+*+")[0]){
                    $("_fieldSplit").value=rowData.data[4].split("+*+*+")[0];
                    $("_fieldSplit").style.color="#000000";
                    $("_fieldSplit").style.fontStyle="normal";
                }
                if(rowData.data[4]&&rowData.data[4].split("+*+*+").length>1){
                    $("_rowSplit").value=rowData.data[4].split("+*+*+")[1];
                    $("_rowSplit").style.color="#000000";
                    $("_rowSplit").style.fontStyle="normal";
                }
//                $("_fieldSplit").disabled=false;
//                $("_rowSplit").disabled=false;
            }else if(rowData.data[3].indexOf("DMP")>-1){//DMP型
                $("_fileDmp").checked=true;
//                $("_fieldSplit").disabled=true;
//                $("_rowSplit").disabled=true;
//                $("_fieldSplit").value="字段分隔符";
//                $("_fieldSplit").style.color="#808080";
//                $("_fieldSplit").style.fontStyle="italic";
//                $("_rowSplit").value="行分隔符";
//                $("_rowSplit").style.color="#808080";
//                $("_rowSplit").style.fontStyle="italic";

                //解析字段分隔符和行分隔符
                if(rowData.data[4]&&rowData.data[4].split("+*+*+")[0]){
                    $("_fieldSplit").value=rowData.data[4].split("+*+*+")[0];
                    $("_fieldSplit").style.color="#000000";
                    $("_fieldSplit").style.fontStyle="normal";
                }
                if(rowData.data[4]&&rowData.data[4].split("+*+*+").length>1){
                    $("_rowSplit").value=rowData.data[4].split("+*+*+")[1];
                    $("_rowSplit").style.color="#000000";
                    $("_rowSplit").style.fontStyle="normal";
                }
            }

//            $("_dataSourceRule").value="jdbc:oracle:thin:@IP地址:端口号:数据库实例名";
//            $("_dataSourceRule").style.color="#808080";
//            $("_dataSourceRule").style.fontStyle="italic";

            $("_ruleIp").value="IP地址";
            $("_ruleIp").style.color="#808080";
            $("_ruleIp").style.fontStyle="italic";
            $("_ruleInst").value="数据库实例名";
            $("_ruleInst").style.color="#808080";
            $("_ruleInst").style.fontStyle="italic";
            $("_rulePort").value="1521";

            $("_ruleTable").style.display="none";
            $("_ruleIp").style.display="none";
            $("_rulePort").style.display="none";
            $("_ruleInst").style.display="none";

            $("_ruleFile").style.display="";
            $("_fieldSplit").style.display="";
            $("_rowSplit").style.display="";

            checkButton.style.display="none";
            addButton.style.marginLeft='180px';
        }else{//类型为数据库
            $("_dataSourceType").value=rowData.data[3];

//            $("_dataSourceRule").value=rowData.data[4];
//            $("_dataSourceRule").style.color="#000000";
//            $("_dataSourceRule").style.fontStyle="normal";

            try{
                var dataSourceRule = rowData.data[4];
                var tmp = dataSourceRule.substring(18, dataSourceRule.length);
                var tmpArray = tmp.split(":");
                $("_ruleIp").value = tmpArray[0];
                $("_ruleIp").style.color="#000000";
                $("_ruleIp").style.fontStyle="normal";
                $("_rulePort").value = tmpArray[1];
                $("_ruleInst").value = tmpArray[2];
                $("_ruleInst").style.color="#000000";
                $("_ruleInst").style.fontStyle="normal";
            }catch (e){

            }

            $("_ruleTable").style.display="";
            $("_ruleIp").style.display="";
            $("_rulePort").style.display="";
            $("_ruleInst").style.display="";
            $("_ruleFile").style.display="none";
            $("_fieldSplit").style.display="none";
            $("_rowSplit").style.display="none";

            checkButton.style.display="";
            addButton.style.marginLeft='0px';
            $("_ftp").checked=true;
            $("_zip").checked=false;
            $("_binary").checked=false;
            $("_fileDmp").checked=true;
            $("_fileTxt").checked=false;
//            $("_fieldSplit").disabled="true";
//            $("_rowSplit").disabled="true";
            $("_fieldSplit").value="字段分隔符";
            $("_fieldSplit").style.color="#808080";
            $("_fieldSplit").style.fontStyle="italic";
            $("_rowSplit").value="行分隔符";
            $("_rowSplit").style.color="#808080";
            $("_rowSplit").style.fontStyle="italic";
        }
        $("_sysId").value=rowData.userdata.sysId;


    }else{//新增页面
        $("_dataSourceName").value="";
        $("_dataSourceId").value=-1;
        $("_dataSourceOraname").value="";
        $("_dataSourceUser").value="";
        $("_dataSourcePass").value="";
        $("_dataSourceMinCount").value=deffaultMinCon;
        $("_dataSourceType").value="TABLE";

//        $("_dataSourceRule").value="jdbc:oracle:thin:@IP地址:端口号:数据库实例名";
//        $("_dataSourceRule").style.color="#808080";
//        $("_dataSourceRule").style.fontStyle="italic";
        $("_ruleIp").value="IP地址";
        $("_ruleIp").style.color="#808080";
        $("_ruleIp").style.fontStyle="italic";
        $("_ruleInst").value="数据库实例名";
        $("_ruleInst").style.color="#808080";
        $("_ruleInst").style.fontStyle="italic";
        $("_rulePort").value="1521";

        $("_ftp").checked=true;
        $("_zip").checked=false;
        $("_binary").checked=false;
        $("_fileDmp").checked=true;
        $("_fileTxt").checked=false;
        $("_dataSourceIntro").value="";
        $("_ruleTable").style.display="";
        $("_ruleIp").style.display="";
        $("_rulePort").style.display="";
        $("_ruleInst").style.display="";
        $("_ruleFile").style.display="none";
        $("_fieldSplit").style.display="none";
        $("_rowSplit").style.display="none";
        checkButton.style.display="";
        addButton.style.marginLeft='0px';
//        $("_fieldSplit").disabled="true";
//        $("_rowSplit").disabled="true";
        $("_fieldSplit").value="字段分隔符";
        $("_fieldSplit").style.color="#808080";
        $("_fieldSplit").style.fontStyle="italic";
        $("_rowSplit").value="行分隔符";
        $("_rowSplit").style.color="#808080";
        $("_rowSplit").style.fontStyle="italic";
    }
};

/**
 * 类型转换
 */
var typeChange = function(obj){
    if(obj.value=="TABLE"){
        $("_ruleTable").style.display="";
        $("_ruleIp").style.display="";
        $("_rulePort").style.display="";
        $("_ruleInst").style.display="";
        $("_ruleFile").style.display="none";
        $("_fieldSplit").style.display="none";
        $("_rowSplit").style.display="none";
        checkButton.style.display="";
        addButton.style.marginLeft='0px';
    }else if(obj.value=="FILE"){
        $("_ruleTable").style.display="none";
        $("_ruleIp").style.display="none";
        $("_rulePort").style.display="none";
        $("_ruleInst").style.display="none";
        $("_ruleFile").style.display="";
        $("_fieldSplit").style.display="";
        $("_rowSplit").style.display="";
        checkButton.style.display="none";
        addButton.style.marginLeft='180px';
    }

};

///**
// * 类型为文件时，选择TXT时显示输入行分隔符和字段分隔符信息
// * @param obj
// */
//var typeRadioCheck = function(obj){
//    if(obj.value=="txt"){
//        $("_fieldSplit").disabled=false;
//        $("_rowSplit").disabled=false;
//    }
//    if(obj.value=="dmp"){
//        $("_fieldSplit").disabled="true";
//        $("_rowSplit").disabled="true";
//    }
//};

/**
 * 数据验证
 */
var vDataSourceRule = function(data){
    if($("_dataSourceType").value=="TABLE"){
        if(data==""){
            return "请填写数据源规则！";
        }
    }
    return true;
};

/**
 * 系统表下拉数据转换器
 */
var sysInfoCodeSelectConverter = new dhtmlxComboDataConverter({
    valueColumn:'sysId',
    textColumn:"sysName"
});

var tableTypeData=null;
dwrCaller.addAutoAction("querySysInfo", DataSourceAction.querySysInfo);
dwrCaller.addDataConverter("querySysInfo", sysInfoCodeSelectConverter);

/**
 * 类型为文件时，过滤显示
 * @param value
 */
var transRule = function(value){
    if(value&&value.indexOf("+*+*+")>0){
        var rtn = "行分隔符";
        rtn=rtn+value.split("+*+*+")[0];
        rtn = rtn + " 列分隔符";
        if(value.split("+*+*+").length>1){
            rtn = rtn + value.split("+*+*+")[1];
        }
        return rtn;
    }else{
        return value;
    }
};

/**
 * 数据源上线
 * @param id
 */
var onlineDataSource = function(id){
    DataSourceAction.onlineDataSource(id, function(data){
        if(data.type=="error"||data.type=="invalid"){
            dhx.alert("对不起，操作失败，请重试！");
        }else{
            dhx.alert("数据源已启用！")
            mygrid.updateGrid(dataSourceDataConverter.convert(data.successData),"update");
        }
    });

};

/**
 * 数据源下线
 * @param id
 */
var offlineDataSource = function(id){
    DataSourceAction.offlineDataSource(id, function(data){
        if(data.type=="error"||data.type=="invalid"){
            dhx.alert("对不起，操作失败，请重试！");
        }if(data.sid==0){
            dhx.alert("对不起，该数据源被其它表类引用，不能停用！");
        }else{
            dhx.alert("数据源已停用！")
            mygrid.updateGrid(dataSourceDataConverter.convert(data.successData),"update");
        }
    });
};

/**
 * 测试连接
 */
var checkBtnClk = function(){
    if($("_ruleIp").value==""||$("_rulePort").value==""||$("_ruleInst").value==""){
        dhx.alert("请填写数据源规则");
        return;
    }
    if($("_dataSourceUser").value==""){
        dhx.alert("请填写用户名称");
        return;
    }
    if($("_dataSourcePass").value==""){
        dhx.alert("请填写登录密码");
        return;
    }
    var info = {};
//    info.dataSourceRule = $("_dataSourceRule").value;
    info.dataSourceRule = "jdbc:oracle:thin:@"+$("_ruleIp").value+":"+$("_rulePort").value+":"+$("_ruleInst").value;
    info.dataSourceUser = $("_dataSourceUser").value;
    info.dataSourcePass = $("_dataSourcePass").value;
    dwrCaller.executeAction("testDataSource", info, function(data){
        if(!data){
            dhx.alert("连接有效！");
        }else{
            dhx.alert("连接无效！出错信息："+data);
        }
    });
};
dwrCaller.addAutoAction("testDataSource",DataSourceAction.testDataSource);

/**
 * 测试数据源
 * @param rowData
 */
var testDataSource = function(rowData){
    if(rowData.data[3]!="TABLE"){
        dhx.alert("对不起，文件类型的数据源不能进行测试！");
        return;
    }
    var info = {};
    info.dataSourceRule = rowData.data[4];
    info.dataSourceUser = rowData.data[2];
    info.dataSourcePass = rowData.userdata.dataSourcePass;
    dwrCaller.executeAction("testDataSource", info, function(data){
        if(!data){
            dhx.alert("连接有效！");
        }else{
            dhx.alert("连接无效！出错信息："+data);
        }
    });
};

var fieldSplitClk = function(obj){
    if(obj.value=="字段分隔符"){
        obj.value="";
        obj.style.color="#000000";
        obj.style.fontStyle="normal";
    }
};

var rowSplitClk = function(obj){
    if(obj.value=="行分隔符"){
        obj.value="";
        obj.style.color="#000000";
        obj.style.fontStyle="normal";
    }
};

var dataSourceRuleClk = function(obj){
    if(obj.value=="IP地址"||obj.value=="数据库实例名"){
        obj.value="";
        obj.style.color="#000000";
        obj.style.fontStyle="normal";
    }
};

/**
 * 重载数据源
 */
var reloadDataSource = function(dataSourceId){
    dhx.showProgress(null,"正在重载数据源...");
    DataSourceAction.reloadDataSource(dataSourceId, function(data){
        if(!data){
            dhx.alert("重载成功！");
        }else{
            dhx.alert("重载成功！数据源连接出错，出错信息："+data);
        }
        dhx.closeProgress();
    });
};

/**
 * 数据源规则失去焦点事件
 * @param obj
 */
var dataSourceRuleBlu = function(obj){
    if(obj.value==""){
        if(obj.name=="ruleIp"){
            obj.value="IP地址";
        }
        if(obj.name=="ruleInst"){
            obj.value="数据库实例名";
        }
        obj.style.color="#808080";
        obj.style.fontStyle="italic";
    }
};

dhx.ready(dataSourceInit);