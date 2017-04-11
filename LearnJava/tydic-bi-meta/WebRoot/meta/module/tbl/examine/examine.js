/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        category.js
 *Description：
 *        表类查询与审核JS
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        王晶
 *Finished：
 *       2011-11-01
 *Modified By：
 *      
 *Modified Date:
 *       
 *Modified Reasons:
 *     
 ********************************************************/
/*************全局设置start*******************************************/
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var dwrCaller = new biDwrCaller();
var typeStateData = getCodeByType("TABLE_STATE");
/*************全局设置end*******************************************/


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
//        tree.loadJSONObject(data);
        if(selectGroup){
            tree.selectItem(selectGroup); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            //联动
            var tableTypeId = form.getCombo("tableTypeId").getSelectedValue();
            //alert(tableTypeId);
            if(tableTypeId==-1||tableTypeId==""){
                dhx.alert("请先选择一个层次分类！");
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

//层次分类数据源(初始时来自码表）
var tableTypeData = null;


/**************加载查询条件end********************************************************/

/**************表格数据展示start******************************************************************/

var tableInit = function(){	
	var tableLayout = new dhtmlXLayoutObject(document.getElementById("container"), "2E");
	    tableLayout.cells("a").setText("表类审批");
        tableLayout.cells("b").hideHeader();
        tableLayout.cells("a").setHeight(70);
        tableLayout.cells("a").fixSize(false, true);
        tableLayout.hideSpliter();
        tableLayout.hideConcentrate();
		var tableData = [{type:"setting",position: "label-left"},
    	             {type:"block",className:'blockStyle',list:[
    	            	 				 {type:"combo",label:"层次分类：",name:"tableTypeId",options:[{value:"-1",text:"全部"}],labelWidth:55,inputWidth:100,inputHeight:22,className:'inputStyle queryItem',readonly:true},
    	            	                 {type:"newcolumn"},
    	            	 				 {type:'input',name:'tableGroup',label:'业务类型：',className:'inputStyle queryItem',labelWidth:55,inputWidth:120,inputHeight:17},
    	            	                 {type:'hidden',name:'tableGroupId',value:"0"},
    	            	                 {type:'newcolumn'},
    	            	                 {type:"combo",label:"状态：",name:"stateRel",inputHeight:22,options:[{value:3,text:"待审核"}],
											className:'inputStyle queryItem',labelWidth:31,inputWidth:80,readonly:true},
                                         {type:"newcolumn"},
                                         {type:"input",label:"表类关键字：",name:"keyWord",labelWidth:67,inputWidth:120,inputHeight:17,className:'inputStyle'},
                                         {type:"newcolumn"},
                                         {type:"button",name:"query",value:"查询",className:'inputStyle',offsetLeft :0,offsetTop : 0}
                                        ]
                    }];
    
    var queryform = tableLayout.cells("a").attachForm(tableData);
    
    tableStateData=getComboByRemoveValue("TABLE_STATE",[0,2,6,7]);
	queryform.getCombo("stateRel").addOption(tableStateData.options);

    //加载层次分类
    tableTypeData = getComboByRemoveValue("TABLE_TYPE");
    queryform.getCombo("tableTypeId").addOption(tableTypeData.options);

    //绑定层次分类combo数据改变的时候业务类型combo的同步
	queryform.getCombo("tableTypeId").attachEvent('onChange',function(){
    	queryform.setFormData({tableGroup:""});
	});

    //加载业务树
   queryGroupTree(null,queryform,queryform.getCombo("tableTypeId").getSelectedValue());
    
    var tableDataConfig = {
	    filterColumns:["tableName","tableNameCn","tableVersion","codeName","tableGroupName","dataSourceName","tableOwner","userName","stateDate","relType","_buttons"],
	    userData:function(rowIndex, rowData) {
	        var userData = {};
	        return userData;
	    },
	    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
	    	switch (columnName) {
	    		case 'userName':
	    			if(rowData.userNamecn!=null){
						return rowData.userNamecn;
					}else{
						return "";
					}
	    			break;
	    		case 'stateDate':
					if(rowData.stateDate!=null){
						return rowData.stateDate;
	    		  	}else{
		    			  return "";
	    		  	}
	    			break;
	    		case 'tableVersion':
					if(rowData.tableVersion==1){
	    			  	return "新增";
	    		  	}else{
	    			  	return "调整";
	    		  	}
	    			break;
	    		case 'relType':
	    			for(var i=0;i<typeStateData.length;i++){
	    				if(rowData.relType==typeStateData[i].value){
	    					if(rowData.relType==0||rowData.relType==2){
	    						return "待审核";
	    					}else{
	    						return typeStateData[i].name;
	    					}
	    					break;
	    				}
	    			}
	    			break;
	    		case '_buttons':
	        		var lastRelId 	= rowData.relId,
	        			tableId 	= rowData.tableId,
		        		date 		= rowData.stateDate,
		        		relType 	= rowData.relType,
		        		tname		= rowData.tableName,
		        		stateMark 	= rowData.stateMark,
		        		tableVersion= rowData.tableVersion;
		        	
	        		if(date!=null){
		        		date = date.substring(0,10).toString().split("-");
		        		date = date[0]+date[1]+date[2];
		        	}
		        	if(relType<3){
	            		return "<a href='#' onclick='tableExamine("+tableId+","+date+","+relType+","+tableVersion+","+rowData.relId+");return false;'>审核</a>";
		           	}else{
        				return "<a href='#' onclick=\"showTableInfo('basicInfo',"+tableId+",'"+tname+"',"+tableVersion+");return false;\">查看</a>";
		           	}
	    			break;
			}
	        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
	    }
	};
    
    
    var tableConverter = new dhtmxGridDataConverter(tableDataConfig);
    var tableParam = new biDwrMethodParam(); 
    tableParam.setParamConfig([{
            index:0,type:"fun",value:function() {
	    		var formData=queryform.getFormData();
	            formData.keyWord=Tools.trim(queryform.getInput("keyWord").value);
	            return formData;
            }
        }
    ]);
    dwrCaller.addAutoAction("queryTable","TableExamineAction.queryTablesByCondition", tableParam);
    dwrCaller.addDataConverter("queryTable",tableConverter);
    
    
    queryform.attachEvent("onButtonClick",function(id){//查询事件
    	if(id=="query"){
    		tableGrid.clearAll();
    		tableGrid.load(dwrCaller.queryTable,"json");
    		//tableGrid.defaultPaging(20);
    	}
    })
    
    
    queryform.getInput("keyWord").onkeypress=function(e){// 添加Enter查询事件
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
            tableGrid.clearAll();
    		tableGrid.load(dwrCaller.queryTable,"json");
        }
	}	
    
    //表格的数据展示
    var tableGrid = tableLayout.cells("b").attachGrid();
	    tableGrid.setHeader("表类名称,表类中文名,类型,层次分类,业务类型,数据源,所属用户,申请人,日期,状态,操作");
		tableGrid.setInitWidthsP("12,12,7,10,10,9,8,7,13,6,6");
		tableGrid.setColAlign("left,left,left,left,left,left,left,left,left,left,left");
		tableGrid.setHeaderAlign("center,center,center,center,center,center,center,center,center,center");
		tableGrid.setColTypes("ro,ro,ro,ro,ro,ro,ro,ro,ro,ro,ro");
		tableGrid.setColSorting("na,na,na,na,na,na,na,na,na,na,na");
		tableGrid.setEditable(false);	
		tableGrid.enableCtrlC();
		tableGrid.setColumnIds("tableName","tableNameCn","tableVersion","codeName","tableGroupName","dataSourceName","tableOwner","userName","stateDate","relType");
		tableGrid.init();
    	tableGrid.defaultPaging(20);
		tableGrid.load(dwrCaller.queryTable,"json");
}
var tableExamine = function(tableId,date,relType,tableVersion,lastRelId){
	openMenu("表类审核","/meta/module/tbl/examine/examineOperate.jsp?tableId="+tableId+"&date="+date+"&relType="+relType+"&tableVersion="+tableVersion+"&menuId="+menuId+"&lastRelId="+lastRelId,"top",null,true);
}
var showTableInfo= function(value,tableId,tname,tableVersion){
	openMenu(tname+"全息视图","/meta/module/tbl/view/viewDetail.jsp?focus="+value+
    	"&tableId="+tableId+"&tableName="+encodeURIComponent(tname)+"&tableVersion="+tableVersion,
    	"top"
    );
}

dhx.ready(tableInit);