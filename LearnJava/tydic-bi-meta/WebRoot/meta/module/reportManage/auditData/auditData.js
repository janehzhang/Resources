/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       import.js
 *Description：
 *       报表管理-审核数据
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       李国民
 *Date：
 *       2012-03-07
 ********************************************************/

/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
/**
 * 声明dwrCaller
 */
var dwrCaller = new biDwrCaller();

var ownerCheck = false;
var isM = 22;		//月标识
var isD = 11;		//天标识

//查询数据源
dwrCaller.addAutoAction("queryTableDataSource", "TableViewAction.queryTableDataSource");
dwrCaller.addDataConverter("queryTableDataSource", new dhtmlxComboDataConverter({
	    valueColumn:"dataSourceId",
	    textColumn:"dataSourceName"
	})
);

//查询对用数据源下的用户信息
dwrCaller.addAutoAction("queryDbUsers","TableApplyAction.queryDbUsers");
dwrCaller.isShowProcess("queryDbUsers",false);


var pageSize = 20;
var queryForm;
var param = new biDwrMethodParam();
param.setParamConfig([
    {
       index:0,type:"fun",value:function() {
			var data = queryForm.getFormData();
			if(!data.dataSourceId){
				data.dataSourceId = "0";
			}
			return data;
   	   	}
    }
]);

//查询信息
dwrCaller.addAutoAction("getAuditData","AuditDataAction.getAuditData",param);
dwrCaller.addDataConverter("getAuditData", new dhtmxGridDataConverter({
	    idColumnName:"issueId",
	    filterColumns:["tableAlias","showDataPeriod","dataDate","auditConclude","showMaxDate","link"],
	    
		/**
	     * 实现 userData，将一些数据作为其附加属性
	     */
	    userData:function(rowIndex, rowData) {
	        var userData = {};
	        return userData;
	    },
	    
	    /**
	     * 添加连接
	     */
	    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
	    	if(columnName == 'showMaxDate'){
	    		if(rowData["dataPeriod"]==isM){	//按月审核
	    			return "前一月";
	    		}else{
	    			return rowData["showMaxDate"];
	    		}
	    	}else if(columnName=='link'){
				var str = "<span style='padding-right:20px;'><a style='cursor:pointer;color:blue;' " +
				" onclick=openViewData("+rowData["issueId"]+","+rowData["dataPeriod"]+",'"+rowData["tableAlias"]+"','"+rowData["auditProp"]+"')>查看</a></span>";
				str += "<span style='padding-left:20px;'><a style='cursor:pointer;color:blue;' " +
				" onclick=openAuditNewData("+rowData["issueId"]+","+rowData["dataPeriod"]+",'"+rowData["tableAlias"]+"','"+rowData["auditProp"]+"','"+rowData["maxDate"]+"')>审核最新数据</a></span>";
	    		return str;
	    	}
	        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
	    }
}));

openViewData = function(issueId,dataPeriod,tableAlias,auditProp){
	openMenu(tableAlias+"审核日志","/meta/module/reportManage/auditData/auditViewData.jsp?issueId="+issueId+"&dataPeriod="+dataPeriod+"&auditProp="+auditProp,"top");
}

openAuditNewData = function(issueId,dataPeriod,tableAlias,auditProp,maxDate){
	openMenu(tableAlias+"审核数据","/meta/module/reportManage/auditData/auditNewData.jsp?issueId="+issueId+"&dataPeriod="+dataPeriod+"&auditProp="+auditProp+"&maxDate="+maxDate,"top");
}
	

/**
 * 加载业务类型树
 */
dwrCaller.addAutoAction("queryTableGroup", "TableApplyAction.queryTableGroup");
dwrCaller.addDataConverter("queryTableGroup", new dhtmxTreeDataConverter({
    idColumn:"tableGroupId",pidColumn:"parGroupId",textColumn:"tableGroupName",
    isDycload:false
}));
dwrCaller.isShowProcess("queryTableGroup", false);
dwrCaller.addAutoAction("queryGroupTree",TableViewAction.queryGroupTree);
var groupTreeConverter=new dhtmxTreeDataConverter({
    idColumn:"tableGroupId",pidColumn:"parGroupId",
    isDycload:false,textColumn:"tableGroupName"
});
dwrCaller.addDataConverter("queryGroupTree",groupTreeConverter);
//树动态加载Action
dwrCaller.addAction("querySubGroup",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},groupTreeConverter,false);
    TableViewAction.querySubGroup(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
var div = null,target = null;
/**
 * 加载业务类型树
 * @param selectGroup
 * @param form
 * @param levelRefID 层次管理ID
 */
var queryGroupTree=function(selectGroup,form){
    selectGroup=selectGroup|| global.constant.defaultRoot;
    var beginId=global.constant.defaultRoot;
    //创建tree Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target=form.getInput("tableGroup");
    target.readOnly=true;
    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
//    tree.enableSmartRendering();
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubGroup);
    //树双击鼠标事件
    tree.attachEvent("onDblClick",function(nodeId){
        form.setFormData({tableGroup:tree.getItemText(nodeId),tableGroupId:nodeId});
        //关闭树
        div.style.display="none";
    });
    div.style.display="none";
    dwrCaller.executeAction("queryGroupTree",beginId,selectGroup,function(data){
        if(selectGroup){
            tree.selectItem(selectGroup); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            //联动
            var tableTypeId = form.getCombo("tableTypeId").getSelectedValue();
            if(tableTypeId==-1||tableTypeId==""){
                return;
            }
            var childs = tree.getSubItems(0);
            if(childs){
                var childIds = (childs + "").split(",");
                for(var i = 0; i < childIds.length; i++){
                    tree.deleteItem(childIds[i]);
                }
            }
            dwrCaller.executeAction("queryTableGroup",tableTypeId,function(data){
                tree.loadJSONObject(data);
            });

            div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    });
}

/**
 * 初始化界面
 */
var init=function(){
	var tableLayout = new dhtmlXLayoutObject(document.getElementById("container"), "2E");
    tableLayout.cells("a").setText("审核数据");
    tableLayout.cells("b").hideHeader();
    tableLayout.cells("a").setHeight(70);
    tableLayout.cells("a").fixSize(false, true);
    tableLayout.hideSpliter();
    tableLayout.hideConcentrate();
	var tableData = [
        {type:"combo",label:"数据源：",name:"dataSourceId",options:[{value:"-1",text:"全部"}],inputHeight:22,inputWidth:110,readonly:true},
        {type:"newcolumn"},
        {type:"combo",label:"所属用户：",name:"tableOwner",inputHeight:22,inputWidth:80,readonly:true},
        {type:"newcolumn"},
	    {type:"combo",label:"层次分类：",name:"tableTypeId",options:[{value:"-1",text:"全部",selected:true}],inputWidth:100,inputHeight:22,readonly:true},
	    {type:"newcolumn"},
	    {type:'input',label:'业务类型：',name:'tableGroup',inputWidth:100,inputHeight:17,readonly:true},
	    {type:"newcolumn"},
        {type:"input",label:"关键字：",name:"keyword",inputWidth:100,inputHeight:17},
        {type:"newcolumn"},
        {type:"button",name:"queryBtn",value:"查询",offsetLeft:0,offsetTop:0}
    ];
    queryForm = tableLayout.cells("a").attachForm(tableData);
	queryForm.getCombo("dataSourceId").enableFilteringMode(false);
	queryForm.getCombo("tableOwner").enableFilteringMode(false);
	//加载数据源
	queryForm.getCombo("dataSourceId").loadXML(dwrCaller.queryTableDataSource, function(){
		queryForm.getCombo("dataSourceId").selectOption(0); //初始化页面默认显示第一条数据
		ownerCheck = true;
	});
    //添加数据源选择事件，查询对应数据源下的所属用户
	queryForm.getCombo("dataSourceId").attachEvent("onChange", function(){
		var val = queryForm.getCombo("dataSourceId").getSelectedValue();
		var text = queryForm.getCombo("dataSourceId").getSelectedText();
        if(val == ""&&text != ""){
            val = 0;
        }else if(val == "-1"&&text == "全部"){
            val = -1;
        }
		queryForm.getCombo("tableOwner").clearAll(true);
		if(ownerCheck){
			dwrCaller.executeAction("queryDbUsers",val,function(data){
		    	var options=[];
	            if(data){
	                data=dhx.isArray(data)?data:[data];
	                for(var i=0;i<data.length;i++){
	                    options.push({text:data[i],value:data[i]});
	                }
	            }
		  		queryForm.getCombo("tableOwner").addOption(options);
	            TableApplyAction.queryDefaultUserByDataSource(val, function(name){
	                if(queryForm.getCombo("tableOwner").optionsArr&&queryForm.getCombo("tableOwner").optionsArr.length){
	                    for(var i=0; i<queryForm.getCombo("tableOwner").optionsArr.length; i++){
	                        if(queryForm.getCombo("tableOwner").optionsArr[i].text==name){
	                            queryForm.getCombo("tableOwner").selectOption(i,false,false);
	                            break;
	                        }
	                    }
	                }
	            });
		 	});
		}
	});  
	
    //加载层次分类
    tableTypeData = getComboByRemoveValue("TABLE_TYPE",[2,3]);
    queryForm.getCombo("tableTypeId").addOption(tableTypeData.options);

    //绑定层次分类combo数据改变的时候业务类型combo的同步
	queryForm.getCombo("tableTypeId").attachEvent('onChange',function(){
    	queryForm.setFormData({tableGroup:"",tableGroupId:""});
	});
    //加载业务树
	queryGroupTree(null,queryForm,queryForm.getCombo("tableTypeId").getSelectedValue());
	
	queryForm.attachEvent("onButtonClick", function(id) {
        if (id == "queryBtn") {
            //进行数据查询。
        	if(queryForm.validate()){
        		grid.clearAll();
            	grid.load(dwrCaller.getAuditData, "json");
        	}
        }
    });
    
    var grid = tableLayout.cells("b").attachGrid();
		grid.setHeader("数据别名,频度,数据时间,是否展现,应用约定,<span>操作</span>" );
		grid.setInitWidthsP("20,15,15,15,15,20");
		grid.setColAlign("left,center,center,center,center,center");
		grid.setHeaderAlign("center,center,center,center,center,center");
		grid.setColTypes("ro,ro,ro,ro,ro,ro");
		grid.setColSorting("na,na,na,na,na,na");
		grid.setEditable(false);	
		grid.init();
    	grid.defaultPaging(20);
		grid.load(dwrCaller.getAuditData,"json");
}
dhx.ready(init);