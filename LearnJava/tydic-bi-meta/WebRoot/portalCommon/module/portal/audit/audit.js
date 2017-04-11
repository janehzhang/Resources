/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        audit.js
 *Description：
 *        数据审核JS
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *       程钰
 *Finished：
 *       12-2-29
 *Modified By：
 *
 *Modified Date:
 *
 *Modified Reasons:
 *
 ********************************************************/
/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user = getSessionAttribute("user");
var loadAuditList = new biDwrMethodParam();//loadZone Action参数设置。
var auditConvertConfig = {
    idColumnName:"auditId",
    filterColumns:["","auid","rown",
        "yy","pd","dataDate","sjssgs","auditConclude","auditOpinion","showOpinion","auditNote","auditMark"],
    cellDataFormat:function(rowIndex,columnIndex,columnName,cellValue,rowData){
        if(columnName=='auditConclude'){
            if(cellValue == '0'){
                return "不展现";
            }
            if(cellValue == '1'){
                return "展现";
            }
        }
        return cellValue;
    }
}
var auditDataConverter=new dhtmxGridDataConverter(auditConvertConfig);
var dhtmlxSelectDataConverter = new dhtmlxComboDataConverter({
    valueColumn:"id",textColumn:"text",isAdd:true
});
var dwrCaller = new biDwrCaller();
var auditInit = function(){
    var base = getBasePath();
    
    //button详细定义
    var buttons={
        edit:{name:"edit",text:"编辑",imgEnabled:base+"/meta/resource/images/editGroup.png",
            imgDisabled :base+"/meta/resource/images/editGroup.png",onclick:function(rowData){
            }},
        cancle:{name:"cancle",text:"撤销编辑",imgEnabled:base+"/meta/resource/images/back.png",
            imgDisabled :base+"/meta/resource/images/back.png",onclick:function(rowData){
            }},
        offer:{name:"offer",text:"提交",imgEnabled:base+"/meta/resource/images/ok.png",
            imgDisabled :base+"/meta/resource/images/ok.png",onclick:function(rowData){
            }}
    };
    //TODO 按钮权限过滤,checkRule.
    var buttonAudit=["edit","cancle","offer"];
//    var buttonAudit=getRoleButtons();
    //定义全局函数，用于获取有权限的button列表定义
    var bottonRoleRow = buttonAudit;

    window["getButtons"] = function(){
        var res = [];
        for(var i = 0; i < bottonRoleRow.length; i++){
            res.push(buttons[bottonRoleRow[i]]);
        }
        return res;
    };
    /************************定义加载数据的action start*********************************/
    dwrCaller.addAutoAction("loadAudit", "AuditAction.listAudits", loadAuditList, function(data){
    });
    dwrCaller.addDataConverter("loadAudit", auditDataConverter);
    /************************定义加载数据的action end***********************************/
   
    /************************定义数据导入action start***********************************/
    dwrCaller.addAutoAction("queryReportName", "ReportConfigAction.queryReportName");
    
    dwrCaller.addAutoAction("importData","ReportConfigAction.importData");
     /************************义数据导入action end***********************************/
    
    var auditLayout = new dhtmlXLayoutObject(document.body, "2E");
    auditLayout.cells("a").setText("数据审核");
    auditLayout.cells("b").hideHeader();
    auditLayout.cells("a").setHeight(80);
    auditLayout.cells("a").fixSize(false,true);
    auditLayout.hideSpliter();//移除分界边框。
    auditLayout.hideConcentrate();

    //加载查询表单
    var queryform = auditLayout.cells("a").attachForm([
        {type:"setting",position: "label-left", labelWidth: 150, inputWidth: 150},
        {type : "combo",label : "应用：",name : "auditApply",
            options : [ {value : "",text : "所有",selected : true} ]},
        {type:"newcolumn"},
        {type : "combo",label : "状态：",name : "auditStatus",
            options : [ {value : "",text : "所有",selected : true}]},
        {type:"newcolumn"},
        {type : "calendar",label : "提供数据的时间：",name : "auditDate",dateFormat : "%Y-%m-%d",weekStart : "7"
            ,value : new Date(),readonly:"readonly"
        },
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"},
        {type:"newcolumn"},
        {type:"button",name:"datainit",value:"数据审核初始化"}
    ]);
    var auditDate = queryform.getCalendar('auditDate');
    auditDate.loadUserLanguage('zh');
    //数据是否展示
    var showData = getComboByRemoveValue("IS_SHOW");
//    queryform.getSelect("auditStatus").addOption(showData.options);
    queryform.getCombo("auditStatus").addOption(showData.options);
    dwrCaller.addAutoAction("queryApply","AuditAction.queryApply");
    dwrCaller.addDataConverter("queryApply",dhtmlxSelectDataConverter);
    dwrCaller.executeAction("queryApply",function(data){
        if(data!=null){
            Tools.addOption(queryform.getSelect("auditApply"),data.options);
        }
    });
    //定义loadRole Action的参数来源于表单queryform数据。
    loadAuditList.setParamConfig([{
        index:0,type:"fun",value:function(){
            return queryform.getFormData();
        }
    }]);
    //这里将有权限的按钮添加到表格的title中
    var buttonToolBar = auditLayout.cells("b").attachToolbar();  //dhx的本身方法,用于在layout中添加按钮行
    var pos = 1;
    var filterButton = window["getButtons"]();
    for(var i = 0; i < filterButton.length; i++){
        buttonToolBar.addButton(filterButton[i].name, pos++, filterButton[i].text, filterButton[i].imgEnabled,
            filterButton[i].imgDisabled);
        var button=buttonToolBar.getItemNodeById(filterButton[i].name);
        button.setAttribute("id",filterButton[i].name);
    }
    //加载地域列表
    mygrid = auditLayout.cells("b").attachGrid();
//    mygrid.setImagePath(dhtmlx.image_path+"csh_"+dhtmlx.skin+"/");
    mygrid.setHeader("{#checkBox},ID,RowNumber,应用,频度,数据时间,数据所属公司,审核结论,可展现的补充意见,不可展现的原因,应用约定,历史审核记录");
    mygrid.setInitWidthsP("2.5,4,4.5,8,4,7,9,7,13,10,10,21");
    mygrid.setHeaderBold();
    mygrid.setColAlign("center,center,center,left,center,center,center,left,left,left,left,left");
    mygrid.setHeaderAlign("left,left,left,left,left,left,left,left,left,left,left,left");
    mygrid.setColTypes("ch,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
//    mygrid.setColTypes("ch,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
    mygrid.setColSorting("na,na,na,na,na,na,na,na,na,na,na,na");
//    mygrid.setEditable(false);
//    mygrid.setColumnCustFormat(7,isSee);//第二列进行转义
    mygrid.enableMultiline(true);
    mygrid.enableSelectCheckedBoxCheck(1);
    mygrid.setColumnIds("'',auid,rown,yy,pd,dataDate,sjssgs,auditConclude,auditOpinion,showOpinion,auditNote,auditMark");
    mygrid.init();
    mygrid.load(dwrCaller.loadAudit,"json");
    mygrid.attachEvent("onCellChanged",function(rId,cInd,nValue){
        if(cInd==0&&nValue==0){
           cancleGrid(rId);
        }
    });
    //查询表单事件处理
    queryform.attachEvent("onButtonClick", function(id){
        if(id=="query"){
            //进行数据查询。
            mygrid.clearAll();
            mygrid.load(dwrCaller.loadAudit,"json");
            isModifyId = [];
        }
        else if(id == "datainit")
        {
        	openWin();
        }
    });
    //添加buttonToolBar事件
    buttonToolBar.attachEvent("onclick",function(id){
        if(id == "edit"){//修改用户
            var selecteddRowId= mygrid.getCheckedRows(0);
            if(selecteddRowId == null || selecteddRowId==""){
                dhx.alert("请选择一行进行修改!");
                return ;
            }else{
                modifyGird(selecteddRowId);
            }
        }
        if(id == "cancle"){//删除用户
            var selecteddRowId= mygrid.getCheckedRows(0);
            if(selecteddRowId == null || selecteddRowId==""){
                dhx.alert("请选择至少一行撤销修改!");
                return ;
            }else{
                cancleGrid(selecteddRowId);
            }
        }
        if(id == "offer"){
            var selecteddRowId= mygrid.getCheckedRows(0);
            if(selecteddRowId == null || selecteddRowId=="" ){
                dhx.alert("请选择至少一行数据提交!");
                return ;
            }else if(isModifyId.length == 0){
                dhx.alert("请在选中编辑按钮编辑数据后提交!");
                return ;
            }
            else{
                offerData();
            }
        }
    })
}
var isModifyId = [];
//修改函数
var modifyGird = function(rowIds){
    for(var i=0; i<rowIds.toString().split(",").length; i++){
        var rowId = rowIds.toString().split(",")[i];
        var userData=mygrid.getRowData(rowId)
        userData.data[9] = (userData.data[9]==null?'':userData.data[9]);
        userData.data[8] = (userData.data[8]==null?'':userData.data[8]);
        userData.data[7] = (userData.data[7]==null?'不展现':userData.data[7])
        mygrid.cells(rowId,9).
            setValue("<textarea id='"+rowId+"auditnote"+"' rows='4' cols='13'>"+userData.data[9]+"</textarea>");
        mygrid.cells(rowId,8).
            setValue("<textarea rows='4' id='"+rowId+"showopinion"+"' cols='18'>"+userData.data[8]+"</textarea>");
        var html ;
        if(userData.data[7]=='不展现'){
            html = "<select id='"+rowId+"auditconclude"+"'><option value='0' selected >不展现</option><option value='1' >展现</option></select>";
        }else{
            html = "<select id='"+rowId+"auditconclude"+"'><option value='0'  >不展现</option><option value='1'selected >展现</option></select>";
        }
        mygrid.cells(rowId,7).setValue(html);
        isModifyId.push(rowId);
    }
}
var cancleGrid  = function(rowIds){
    for(var i=0; i<rowIds.toString().split(",").length; i++){
        var rowId = rowIds.toString().split(",")[i];
        var userData=mygrid.getRowData(rowId)
        userData.data[9] = (userData.data[9]==null?'':userData.data[9]);
        userData.data[8] = (userData.data[8]==null?'':userData.data[8]);
        userData.data[7] = (userData.data[7]==null?'不展现':userData.data[7])
        mygrid.cells(rowId,9).setValue(userData.data[9]);
        mygrid.cells(rowId,8).setValue(userData.data[8]);
        mygrid.cells(rowId,7).setValue(userData.data[7]);
        for(var j=0; j<isModifyId.length; j++){
            if(isModifyId[j] == rowId){
                isModifyId.splice(j,1);
            }
        }
    }
}
var offerData = function(){
     var offerdatas = {};
     var modifyResult = true;
     var count = 0;
     for(var i=0; i<isModifyId.length; i++){

        var rowId = isModifyId[i];
        var userData=mygrid.getRowData(rowId)
        offerdatas.auditconclude = $(rowId+"auditconclude").options[$(rowId+"auditconclude").selectedIndex].value;
        offerdatas.showopinion = $(rowId+"showopinion").value;
        offerdatas.auditnote = $(rowId+"auditnote").value;
        offerdatas.auid = userData.data[1];
        AuditAction.commitAudit(offerdatas,function(data){
            if(data == '0'){
                modifyResult = false;
                dhx.alert("id为"+userData.data[1]+"的记录审核失败，请联系管理员！");
            }else{
                mygrid.cells(rowId,11).setValue(data);
            }
            count++;
            if(count == isModifyId.length){
                if(modifyResult){
                    dhx.alert("审核成功");
                    offerdatas = {};
                    isModifyId = [];
                }
            }
        });
        if(modifyResult){
            var auditCon;
            if(offerdatas.auditconclude == '1'){
                auditCon = "展现";
            }else{
                auditCon = "不展现";
            }
            mygrid.cells(rowId,9).setValue(offerdatas.auditnote==null?"":offerdatas.auditnote);
            mygrid.cells(rowId,8).setValue(offerdatas.showopinion==null?"":offerdatas.showopinion);
            mygrid.cells(rowId,7).setValue(auditCon);
        }
    }
}
var openWin = function(){
	var dhxWindow = new dhtmlXWindows();
    var winSize=Tools.propWidthDycSize(15,20,15,20);
    winSize.width=400;
    dhxWindow.createWindow("importWindow", 0, 0, 750, 480);
    var importWindow = dhxWindow.window("importWindow");
    importWindow.setModal(true);
    importWindow.setDimension(winSize.width, 200);
    importWindow.center();
    importWindow.denyResize();
    importWindow.denyPark();
    var buttonstr = "";
	importWindow.setText("数据审核初始化");	
	importWindow.keepInViewport(true);
    importWindow.show();
	 var dhxLayout = new dhtmlXLayoutObject(importWindow, "1C");
    dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.hideHeader();
    topCell.setHeight(80);
    topCell.fixSize(true,true);

    var firstday = new Date();
    var today = new Date();
    firstday.setDate(1);
    var importform = topCell.attachForm(
             [
	        {type: "settings", position: "label-left"},
	        {type:"block",className:"blockStyle",list:[
	            {type:"select",label:"报表名称：",name:"reportname",offsetLeft : 10,offsetTop : 10,inputHeight:22,inputWidth: 120,options:[{value:"",text:"全部",selected:true}]},
                {type:"newcolumn"}
	        ]},
	        {type:"block",className:"blockStyle",list:[
	            {type:"calendar",label:"报告日期：",offsetLeft : 10,offsetTop : 10, inputHeight:17,name:"startDate",dateFormat:"%Y-%m-%d",weekStart:"7",value:firstday,validate: "NotEmpty",readonly:true},
                {type:"newcolumn"},
                {type:"calendar",label:"至",name:"endDate",dateFormat:"%Y-%m-%d",weekStart:"7",value:today,offsetTop : 10,inputHeight:17,validate: "NotEmpty",readonly:true},
                {type:"newcolumn"}
	        ]},
	        
	        {type:"block",className:"blockStyle",list:[
	           {type:"select",label:"展示类型：",name:"Exhibitiontype",offsetLeft : 10,offsetTop : 10,inputHeight:22,inputWidth: 120,options:[{value:"",text:"全部",selected:true}]},
                {type:"newcolumn"}
	        ]},
	        {type:"block",offsetTop:20,inputTop :20,list:[
	           {type:"button",name:"generatedata",value:"批量生成数据",offsetLeft : 100,offsetTop : 0}
	        ]}
	        
	    ]
    );
    
 
    dwrCaller.executeAction("queryReportName",function(data){
      var reportNameSelect= importform.getSelect("reportname");
        var reportNameCount=1;
         reportNameSelect.options[0]=new Option("全部","");
        
        for(var key in data){
            if(key!='length'){
                reportNameSelect.options[reportNameCount++]=new Option(data[key].TAB_NAME,data[key].TAB_ID);
            }
        }
        
    }) ;
    
     var ExhibitiontypeSelect= importform.getSelect("Exhibitiontype");
     ExhibitiontypeSelect.options[0] = new Option("不展示","0");
     ExhibitiontypeSelect.options[1] = new Option("展示","1");

    var startCalendar = importform.getCalendar("startDate");
    var endCalendar = importform.getCalendar("endDate");

    //将日历控件语言设置成中文
    startCalendar.loadUserLanguage('zh');
    endCalendar.loadUserLanguage('zh');
    
    importform.attachEvent("onButtonClick", function(id) {
    	if (id == "generatedata") 
    	{
    		dhx.confirm("执行此操作将自动删除该日期范围内已经存在的数据！",function(r){
            importWindow.close();
    		if(r)
            {
            	//批量生成数据。
    			dhx.showProgress("正在执行数据导入");
            	ReportConfigAction.importData(importform.getFormData(),function(rs)
            	{
            		dhx.closeProgress();
            		if (rs) 
            		{
		                dhx.alert("数据导入成功！");
		            } else 
		            {
		                dhx.alert("数据导入失败，请重试！");
		            }
            	});
            }
            });
        }
    });
}
//修改表格内容封装，固定修改第
dhx.ready(auditInit);