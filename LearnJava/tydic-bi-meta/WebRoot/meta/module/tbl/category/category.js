/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        category.js
 *Description：
 *        表类分类JS
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        王晶
 *Finished：
 *       2011-10-26
 *Modified By：
 *      
 *Modified Date:
 *       
 *Modified Reasons:
 *     
 ********************************************************/

/***********全局变量start********************************/
dhtmlx.image_path = getDefaultImagePath();
var user= getSessionAttribute("user");
dhtmlx.skin = getSkin();
var tableTypeId = null; // 表的类型的id
var dwrCaller =null;
var buttonArray = null;
var base = getBasePath(); //获得当前服务器的根地址
/***********全局变量end********************************/

/***********页面本身数据展现start******************************************/
//表格数据的展示数据信息,由数据转化器调用
var convertConfig ={
	idColumn:"tableGroupId",pidColumn:"parGroupId",
    //filterColumns:["parGroupId","tableGroupName","codeName","tableTypeId","_buttons"],
	filterColumns:["tableGroupName","codeName","_buttons"],
    isDycload:true,
     //将最后一列转换成图片
    cellDataFormat:function(rowIndex,columnIndex,columnName,cellValue,rowData){
        if(columnName == "_buttons"){
        	if(rowData.parGroupId==0){
        		return "getRootSelectButtons";
        	}
            return "getSelectButtons";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex,columnName, cellValue, rowData);
     },
     userData:function(rowIndex,rowData){
    	var userData ={};
        userData["parentName"]=rowData["parTableGroupName"];
        userData["tableTypeId"] = rowData["codeId"];
        return userData;
     },
     onBeforeConverted:function(data,columnName){   //如果子节点没有数据,默认添加一行
    	if(data){
    		if(data==""){
    		   var rootData = {};
    		   rootData.PAR_GROUP_ID=0;
    		   rootData.CODE_ID=queryform.getCombo("tableCategory").getSelectedValue();
    		   rootData.CODE_NAME=queryform.getCombo("tableCategory").getSelectedText();
    		   rootData.TABLE_GROUP_NAME=rootData.CODE_NAME+"层次分类根节点";
    		   dwrCaller.executeAction("insertTable",{tableName:rootData.TABLE_GROUP_NAME,parentId:0,tableTypeId:rootData.CODE_ID},function(insertData){
    			   var typeId = queryform.getCombo("tableCategory").getSelectedValue();
    		       tableGrid.clearAll();
    		       tableGrid.load(dwrCaller.queryTable,"json");
                });
    		   return rootData;
    		 }
    	}
    	
     }
}//end convertConfig
//表格显示的树转换器
var tableTreeConverter = new dhtmlxTreeGridDataConverter(convertConfig);
//参数组件
var loadTableParam = new biDwrMethodParam();//loadMenu Action参数设置。

//层次分类数据源(初始时来自码表）
var tableTypeData = null;

//caller 组件的预先加载的方法
var dwrCallerConfig={              //caller组件的配置,里面具体的方法是可以自己定义的,比如查询,修改这些需要执行的方法,与封装的caller控件一起使用
    querySubTable:{methodName:"TableCategoryAction.querySubTableGroup",converter:tableTreeConverter}
}; // end config

dwrCaller = new biDwrCaller(dwrCallerConfig);// caller组件

//codeId为表类型的参数,默认是0,意为查询全部的表,当查询具体表类型的时候更改codeId的值
dwrCaller.addAutoAction("queryTable","TableCategoryAction.queryTableGroup",loadTableParam,function(data){});
dwrCaller.addDataConverter("queryTable",tableTreeConverter);
dwrCaller.isDealOneParam("queryTable",false);
dwrCaller.addAutoAction("insertTable","TableCategoryAction.insertTableGroup");
var queryform  = null;
var categoryInit = function(){
    var codeId = null ;//表类型的id;
    var categoryLayout = new dhtmlXLayoutObject(document.body, "2E"); //建立一个布局
    categoryLayout.cells("a").setText("表类业务类型管理");
    categoryLayout.cells("a").setHeight(80);
    categoryLayout.cells("a").fixSize(false, true);
    categoryLayout.hideConcentrate();//隐藏缩放按钮
    categoryLayout.cells("b").hideHeader();//隐藏下面的布局title
	categoryLayout.hideSpliter();
    //建立页面上半部门的查询表单
    queryform = categoryLayout.cells("a").attachForm([
        {type:"setting"},
        {type:"combo",className:'queryItem',label:"<span>层次分类：</span>",name:"tableCategory",labelWidth:60,inputWidth:160,readonly:true},
        {type:"newcolumn"},
        {type:"button",name:"query",value:"查询"}
    ]); // end queryform

    //加载combo的表类类型的数据(层次表)
    tableTypeData = getComboByRemoveValue("TABLE_TYPE");
    queryform.getCombo("tableCategory").addOption(tableTypeData.options);
    queryform.getCombo("tableCategory").selectOption(0,true,true);


    //点击的查询事件
    queryform.attachEvent("onButtonClick",function(id){
    	if(id=="query"){
    	    var typeId = queryform.getCombo("tableCategory").getSelectedValue();
    		tableGrid.clearAll();
    		tableGrid.load(dwrCaller.queryTable,"json");
    	}
    })//end query
    //设置参数
    loadTableParam.setParamConfig([
        {
            index:0,type:"fun",value:function(){
             return queryform.getFormData();
            }
        }
    ]); //end para 
    
    //表格中按钮列的处理
    var buttons = {
    	addTabelType:{
    		    name:'addTabelType',text:'新增同级',imgEnabled:base + "/meta/resource/images/addMenu.png",
                imgDisabled:base + "/meta/resource/images/addMenu.png",onclick:function(rowdata){
    	          if(dhx.isArray(rowdata)&&rowdata.length>0){
    	        	  dhx.alert("对不起,您只能选择一条记录进行操作");
    	        	  return;
    	          }
    	          inserTable(rowdata,"bother");
                }//end onclick
    	},
    	addSubType:{
    		   name:'addSubType',text:'新增下级',imgEnabled:base + "/meta/resource/images/addSubmenu.png",
               imgDisabled :base + "/meta/resource/images/addSubmenu.png",onclick:function(rowdata){
    		    if(dhx.isArray(rowdata)&&rowdata.length>0){
    	        	  dhx.alert("对不起,您只能选择一条记录进行操作");
    	        	  return;
    	        }
    	         inserTable(rowdata,"child");
               }//end onclick
    	},
    	updateTableType:{
    		   name:'updateTableType',text:'修改',imgEnabled:base + "/meta/resource/images/menu.png",
               imgDisabled:base + "/meta/resource/images/menu.png",onclick:function(rowdata){
    		    if(dhx.isArray(rowdata)&&rowdata.length>0){
    	        	  dhx.alert("对不起,您只能选择一条记录进行操作");
    	        	  return;
    	        }
    		   updateTable(rowdata)
               }//end onclick
    	},
    	deleteTableType:{
    		   name:'deleteTableType',text:'删除',imgEnabled:base + "/meta/resource/images/delete.png",
               imgDisabled :base + "/meta/resource/images/delete.png", onclick:function(rowdata){
    		     if(dhx.isArray(rowdata)&&rowdata.length>0){
    	        	  dhx.alert("对不起,您只能选择一条记录进行操作");
    	        	  return;
    	         }
    		     deleteGroupFun(rowdata.id);
               }//end onclick
    	},
    	lookUpTableInfo:{
    		  name:'lookUpTableInfo',text:'查看表类',imgEnabled:base + "/meta/resource/images/lookup.png",
              imgDisabled :base + "/meta/resource/images/lookup.png", onclick:function(rowdata){
    		    if(dhx.isArray(rowdata)&&rowdata.length>0){
    	        	 dhx.alert("对不起,您只能选择一条记录进行操作");
    	        	 return;
    	        }
    		    lookUpTable(rowdata.id,rowdata.data[0]);
              }//end onclick
    	}
    }//end buttons
    
    //定义有权限的按钮
    buttonArray =["addTabelType","addSubType","updateTableType","deleteTableType","lookUpTableInfo"];
    
    window["getSelectButtons"] = function(argments){
    	var res = [];
        if(argments!=undefined){
	        for(var i = 0; i < buttonArray.length; i++){
	            res.push(buttons[buttonArray[i]]);
	        }
	    }else{
	    	 res.push(buttons[buttonArray[1]]);
	    }
        return res;
    };
    window["getRootSelectButtons"] = function(){
    	var res = [];
	    res.push(buttons[buttonArray[1]]);
        return res;
    };
    //表格的展示
        tableGrid = categoryLayout.cells("b").attachGrid();
	    tableGrid.setImagePath(dhtmlx.image_path + "csh_" + dhtmlx.skin + "/");
	    tableGrid.setHeader("业务类型,层次分类,操作");
	    tableGrid.setInitWidthsP("40,40,20");
	    tableGrid.setColAlign("left,left,center");
	    tableGrid.setHeaderAlign("center,center,center");
	    tableGrid.setColTypes("tree,ro,sb");
	    tableGrid.setColSorting("na,na,na");
	    tableGrid.enableTreeGridLines();
        tableGrid.setHeaderBold();
	    tableGrid.setEditable(false);
	    tableGrid.enableTooltips("true,true,false");
	    tableGrid.setColumnIds("tableGroupName,codeName");//关键,这里的每一项是和前面的转换器中的filterColumns属性值一一对应的
	    //tableGrid.enableDragAndDrop(true);//设置拖拽
	    tableGrid.init(); //必须加这句,表格才会被显示出来
	    var typeId = queryform.getCombo("tableCategory").getSelectedValue();
		//alert(typeId)
	    tableGrid.load(dwrCaller.queryTable,"json");
	    tableGrid.kidsXmlFile = dwrCaller.querySubTable; //加载子系统
    
 } //end categoryInit
//***********页面本身数据展现end******************************************/
//************新增表单start**********************************************/
   //新增的表格的数据
/************新增表单start**********************************************/
  var tableData = [{type:"setting",position: "label-left", labelWidth:60, inputWidth: 150},
	                {type:"block",list:[{type:"label",label:"<span style='color:#000;font-size:12px;padding-left:1px;'>父级分类：</span>"},
	                	                {type:"newcolumn"},
	                	                {type:"label",name:'parentTableName',labelWidth:200}
	                	                ]
	                },
	                {type:"block",list:[{type:"label",label:"<span style='color:#000;font-size:12px;padding-left:1px;'>父级类型：</span>"},
	                	                {type:"newcolumn"},
	                	                {type:"label",name:'parentTableType'}
	                	                ]
	                },
	                {type:"block",list:[{type:"label",name:'tableType',label:"<span style='color:#000;font-size:12px;padding-left:1px;'>层次类型：</span>"},
	                	                 {type:"newcolumn"},
	                                     {type:"label",name:'tableTypeName'}
	                                   ]
	                },
	                {type:"block",list:[{type:"input",name:"tableName",id:"_tableName",label:"<span>类型名称：</span>",inputHeight:18,inputWidth:200,validate: "NotEmpty,MaxLength[32]"},
	                	                {type:"newcolumn"},
	                	                {type:"label",label:'<span style="color:red;">*</span>'}
	                	               ]
	                },    
	                {type:"hidden",name:'parentId'},
	                {type:"hidden",name:'tableTypeId'},
	                {type:"block",style:'margin-top:15px;',list:[{type:"button",label:"保存",name:"save",value:"保存",offsetLeft:70},
                                        {type:"newcolumn"},
							            {type:"button",label:"清空",name:"reset",value:"清空"},
							            {type:"newcolumn"},
							            {type:"button",label:"关闭",name:"close",value:"关闭"}]
				   }
	   ]//end tableData
	   var inserTable = function(rowdata,flag){
		   var comboData = null;
		   var loadParam = new biDwrMethodParam();
		   var dhxWindow = new dhtmlXWindows();
	       dhxWindow.createWindow("addWindow", 0, 0,350,190);
	       var addWindow = dhxWindow.window("addWindow");
	       addWindow.setModal(true);
		   addWindow.button("minmax1").hide();
		   addWindow.button("minmax2").hide();
		   addWindow.button("park").hide();
		   addWindow.denyResize();
		   addWindow.denyPark();
		   addWindow.setText("新增业务类型");
		   addWindow.keepInViewport(true);
		   addWindow.show();
	       addWindow.center();
	       var addForm = addWindow.attachForm(tableData);
	       addForm.defaultValidateEvent();
       
       	//dwrCaller.addAutoAction("queryLevelType","TableViewAction.queryLevelType",rowdata._pid_temp_,function(){});
	    
       //页面数据的初始化 
       	var initForm = function(){
    	   addForm.setItemValue("parentId",rowdata._pid_temp_);
    	   var parentTableName = rowdata.userdata.parentName;
    		   if(parentTableName==null||parentTableName==undefined){
    			   parentTableName = "根节点"
    		   }
    	   if(flag == "bother"){
    		   addForm.setItemValue("parentId",rowdata._pid_temp_);
    		   addForm.setItemLabel("parentTableName","<span style='color:#000;font-size:12px;font-weight:bold'>"+parentTableName+"</span>");
    		   addForm.setItemLabel("parentTableType","<span style='color:#000;font-size:12px;font-weight:bold'>"+rowdata.data[1]+"</span>");
    		   
    		   dwrCaller.addAutoAction("queryLevelType", "TableViewAction.queryLevelType", queryform.getCombo("tableCategory").getSelectedValue(),function(data) {});

			   dwrCaller.addDataConverter("queryLevelType", new dhtmlxComboDataConverter({
		               valueColumn:"codeItem",textColumn:"codeName",isAdd:true,userData:function(rowdata){
		                       return rowdata.codeItem;
		                   }
		       }));	   
    	   }//end if
    	    if(flag == "child"){
    	      addForm.setItemValue("parentId",rowdata.id);
    	      addForm.setItemLabel("parentTableName","<span style='color:#000;font-size:12px;font-weight:bold'>"+rowdata.data[0]+"</span>");
    		  addForm.setItemLabel("parentTableType","<span style='color:#000;font-size:12px;font-weight:bold'>"+rowdata.data[1]+"</span>");
    		  dwrCaller.addAutoAction("queryLevelType", "TableViewAction.queryLevelType", rowdata.id,function(data) {});

			   dwrCaller.addDataConverter("queryLevelType", new dhtmlxComboDataConverter({
		               valueColumn:"codeItem",textColumn:"codeName",isAdd:true,userData:function(rowdata){
		                       return rowdata.codeItem;
		                   }
		       }));
    	   }
    	    addForm.setItemLabel("tableTypeName","<span style='color:#000;font-size:12px;font-weight:bold'>"+queryform.getCombo("tableCategory").getSelectedText()+"</span>");
       }//end initForm
       initForm();
       //加载combo的数据
       //表单的事件
       addForm.attachEvent("onButtonClick",function(id){
    	   if(id=="save"){
    		  if(addForm.validate()){
	    		  addForm.setItemValue("tableTypeId",queryform.getCombo("tableCategory").getSelectedValue());
	    		  loadParam.setParamConfig([{
	                 index:0,type:"fun",value:function(){
	                 return addForm.getFormData();
	              }
	             }]); //end param
	    		 dwrCaller.executeAction("insertTable",loadParam,function(data){
	    			  if(data){
	    				 dhx.alert("新增成功");
		    			 tableGrid.clearAll();
		    			 tableGrid.load(dwrCaller.queryTable,"json");
		    			 addWindow.close();
	    			 }
	    		 });
    		 }
    	   }//end save
    	   if(id=="reset"){
    		   addForm.clear();
    		   initForm();
    	   }
    	   if(id=="close"){
               	addForm.clear();
            	addWindow.close();
            	dhxWindow.unload();
    	   }
       })////end event

   }//end insert
///************新增表单end**********************************************/
//
///************修改表单start**********************************************/
  var updateTableData = [{type:"setting",position: "label-left"},
			               {type:"block",width:300,list:[{type:"input",name:'tableGroupName',validate:'NotEmpty,MaxLength[32]"',label:"<span>类型名称：</span>",inputHeight:18,inputWidth:180},
                                               {type:"newcolumn"},
                                               {type:"label",label:'<span style="color:red;">*</span>'}
			                	               ]  
			               },
			               {type:"hidden",name:'tableTypeId'},
			               {type:"hidden",name:'tableGroupId'},
			               {type:"block",style:'margin-top:10px;',list:[{type:"button",label:"保存",name:"save",value:"保存",offsetLeft:70},
		                                        {type:"newcolumn"},
									            {type:"button",label:"重置",name:"reset",value:"重置"},
									            {type:"newcolumn"},
									            {type:"button",label:"关闭",name:"close",value:"关闭"}]
				   }
	   ]//end tableData
  var updateTable = function(rowdata){
	   var comboData= null;
	   var loadParam = new biDwrMethodParam();
       var dhxWindow = new dhtmlXWindows();
       dhxWindow.createWindow("modifyWindow", 0, 0,350,100);
       var modifyWindow = dhxWindow.window("modifyWindow");
       modifyWindow.setModal(true);
      //modifyWindow.setDimension(600, 550);
       modifyWindow.center();
	   modifyWindow.button("minmax1").hide();
	   modifyWindow.button("minmax2").hide();
	   modifyWindow.button("park").hide();
	   modifyWindow.denyResize();
	   modifyWindow.denyPark();
	   modifyWindow.setText("修改业务类型");
	   modifyWindow.keepInViewport(true);
	   modifyWindow.show();
       var modifyForm = modifyWindow.attachForm(updateTableData);
        modifyForm.defaultValidateEvent();
       //页面数据的初始化 
       var initForm = function(){
    	      modifyForm.setItemValue("tableGroupName",rowdata.data[0]);
    	      modifyForm.setItemValue("tableType",rowdata.data[1]);
    		  
       }//end initForm
       initForm();
       //加载combo的数据
//       modifyForm.getCombo("tableType").loadXML(dwrCaller.queryTabaleCategory, function(data){
//    	                                 comboData = data._data;
//    	                                 modifyForm.setItemValue("tableType",rowdata.data[1]);
//                                         //modifyForm.getCombo("tableType").selectOption(0,true,true);
//    	                                  modifyForm.getCombo("tableType").disable('true');
//       });
       
       modifyForm.attachEvent("onButtonClick",function(id){ 
	    	 if(id=="save"){
	    		 if(modifyForm.validate()){
//		    		  var index =modifyForm.getCombo("tableType").getSelectedIndex();
//		              var codeItem = comboData.options[index].userdata;      
//		    		  modifyForm.setItemValue("tableTypeId",codeItem);
		    		  modifyForm.setItemValue("tableGroupId",rowdata.id);
		    		  loadParam.setParamConfig([{
		                 index:0,type:"fun",value:function(){
		                 return modifyForm.getFormData();
		              }
		             }]); //end param
		    		 dwrCaller.addAutoAction("updateTable","TableCategoryAction.updateTableGroup",loadParam,function(data){
		    			 if(data){
		    				 dhx.alert("修改成功");
			    			 tableGrid.clearAll();
			    			 tableGrid.load(dwrCaller.queryTable,"json");
			    			 modifyWindow.close();
		    			 }
			         });
		    		 dwrCaller.executeAction("updateTable");
	    	  }
    	   }//end save
    	   if(id=="reset"){
    		   modifyForm.clear();
    		   initForm();
    	   }
    	   if(id=="close"){
    		   modifyWindow.close();
    		   modifyForm.unload();
               dhxWindow.unload();
    	   }
       })////end event
  }//end update
///************修改表单start**********************************************/
//  var deleteTable = function(tableId){
//	  
//  }//end detele
///************删除以及查询其他start**********************************************/
   var deleteGroupFun = function(groupId){
	   dhx.confirm("是否要删除该业务类型？",delGroup);
	   function delGroup(r) {
		   if(!r)	return;
		   dwrCaller.addAutoAction("deleteTableGroup","TableCategoryAction.deleteTableGroup",{groupId:groupId},function(data){
			         if(data){
			        	 dhx.alert("删除成功!");
		    			 tableGrid.clearAll();
		    			 tableGrid.load(dwrCaller.queryTable,"json");
	    			 }else{
	    				 dhx.alert("本业务类型下存在表类或已经有子类型，不能删除。");
	    			 }
	         });
		 	dwrCaller.executeAction("deleteTableGroup");
 	}
   }//end delete
///************删除以及查询其他end**********************************************/
//查看表类的处理方法
var lookUpTable = function(tableGroupId,tableGroupName){
    openMenu("查看表类","/meta/module/tbl/view/tableView.jsp?tableGroupId="+tableGroupId+"&tableTypeId="+queryform.getCombo("tableCategory").getSelectedValue()
        +"&tableGroupName="+encodeURIComponent(encodeURIComponent(tableGroupName)),"top",9002,true);
};
 
dhx.ready(categoryInit);