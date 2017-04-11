/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        intRelManger.js
 *Description：
 *        维度表映射编码JS
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        王晶
 *Finished：
 *       2011-12-02
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
var user=getSessionAttribute("user");
var initData ={};
var typeData = null;
var mygrid = null;
var intTableId = null;
var sysCount =0;
/****************全局设置end*******************************************/
//加载应用系统等下拉框的数据,依次是加载系统名称,数据源名称,原系统维度表名称,接口表类名称
var dwrCallerConfig = {
	querySystemData:{methodName:"DimIntRelMangerAction.querySystemData",converter:new dhtmlxComboDataConverter({
                      valueColumn:"sysId",textColumn:"sysName",isAdd:true,async:false
                     })},
	queryDataSource:{methodName:"DimIntRelMangerAction.queryDataSource",converter:new dhtmlxComboDataConverter({
                      valueColumn:"dataSourceId",textColumn:"dataSourceName",isAdd:true,async:false
                     })},
	 queryDimTableData:{methodName:"DimIntRelMangerAction.queryDimTableData",converter:new dhtmlxComboDataConverter({
                    valueColumn:"dimTableId",textColumn:"tableName",isAdd:true,async:false
                     })},
	 queryIntTableNameData:{methodName:"DimIntRelMangerAction.queryIntTableNameData",converter:new dhtmlxComboDataConverter({
                       valueColumn:"tableId",textColumn:"tableName",isAdd:true,async:false
                     })},
     queryIntTableNameDataByUpdate:{methodName:"DimIntRelMangerAction.queryIntTableNameDataByUpdate",converter:new dhtmlxComboDataConverter({
                       valueColumn:"tableId",textColumn:"tableName",isAdd:true,async:false
                     })}
}
var dwrCaller = new biDwrCaller(dwrCallerConfig);
//修改的action
dwrCaller.addAutoAction("updateData","DimIntRelMangerAction.updateData"); 
//新增的action
dwrCaller.addAutoAction("addData","DimIntRelMangerAction.insertData");
//修改的action
dwrCaller.addAutoAction("delData","DimIntRelMangerAction.deleteData");
//加载表格数据的配置数据
var converterConfig = {
    filterColumns:["tableName","sysName","intTabName","srcTabName","dataMappMark","dataMappSql","_buttons"],
    userData:function(rowIndex, rowData){
        var userData = {};
        return userData;
    },
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        if(columnName == '_buttons'){
                  var mappMark = rowData.dataMappMark==null?"":rowData.dataMappMark;
                  var mappSql = rowData.dataMappSql==null?"":rowData.dataMappSql;
                  var srcTabName = rowData.srcTabName==null?"":rowData.srcTabName;
         return"<a href='#' onclick=\"updateFun("+rowData.sysId+","+rowData.dataSourceId+","+rowData.dimTableId+","+rowData.userId+","+rowData.intTabId+",'"+srcTabName+"','"+mappMark+"','"+mappSql+"'"+");return false;\">修改</a>   <a href='#' onclick='deleteFun("+rowData.sysId+","+rowData.dataSourceId+","+rowData.dimTableId+","+rowData.userId+");return false;'>删除</a>";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}
/**按应用系统加载数据源
 * @param  obj 当前form的名称 sysId当前选择的应用系统的id
 * @return {数据源id:数据源名称} 
 */
 var queryDataSourceBySysId = function (obj,sysId){
	 if(obj!=null&&sysId!=0){
		 obj.getCombo("dataId").loadXML(dwrCaller.queryDataSource+"&sysId="+sysId,function(data){
			if(data._data.options.length==0){
				obj.getCombo("dataId").setComboText("");
				obj.getCombo("tableId").setComboText("");
				obj.getCombo("intTableId").setComboText("");
			}else{
			var dsId = obj.getCombo("dataId").getSelectedValue();
			queryDataDimByDSId(obj,dsId);
			queryDataIntByDSId(obj,dsId);
			obj.getCombo("dataId").attachEvent('onChange',function(){
			   var dsId = obj.getCombo("dataId").getSelectedValue();
			    queryDataDimByDSId(obj,dsId);
		        queryDataIntByDSId(obj,dsId);
		   });
			}
			
	   });
	 }
 }
 /**按数据源加载维度表
 * @param  obj 当前form的名称 dsId当前数据源的id
 * @return {TypeName} addForm
 */
 var queryDataDimByDSId = function (obj,dsId){
	 if(obj!=null){
		 obj.getCombo("tableId").loadXML(dwrCaller.queryDimTableData+"&dsId="+dsId,function(data){
			 if(data._data.options.length==0){
				 obj.getCombo("tableId").setComboText("");
			 }
	   });
	 }
 }

 /**按数据源加载接口表
 * @param  obj 当前form的名称 dsId当前数据源的id
 * @return {TypeName} addForm
 */
 var queryDataIntByDSId = function (obj,dsId){
	 if(obj!=null){
		 obj.getCombo("intTableId").loadXML(dwrCaller.queryIntTableNameData+"&dsId="+dsId,function(data){
		   if(data._data.options.length==0){
				 obj.getCombo("intTableId").setComboText("");
			}
	   });
	 }
 }
 
/**
 * 新增修改之后的弹出页面处理方法
 * 
 */
var addFormDate=[
	  {type:"setting",position: "label-left"},
	  {type:"block", list:[{type:"combo",label:'源系统：',name:'sysId',inputWidth:200,inputHeight:20,readonly:true,offsetLeft:42},
		                   {type:"combo",label:'数据源：',name:'dataId',inputWidth:200,inputHeight:20,readonly:true,offsetLeft:42},
		                    {type:"combo",label:'接口表类：',name:'intTableId',inputWidth:200,inputHeight:20,offsetLeft:30},
                            {type:"combo",label:'维度表类：',name:'tableId',inputWidth:200,inputHeight:20,offsetLeft:30},
                            {type:"block",list:[{type:"input",label:'源系统表名称：',name:'srcTableName',inputWidth:300,offsetLeft:6,validate:'NotEmpty'}, {type:"newcolumn"},{type:"label",label:'<span style="color:red">*</span>'}]},
                            {type:"input",label:'映射说明：',name:'mappMark',inputWidth:300,rows:2,offsetLeft:30},
                            {type:"input",label:'映射规则SQL：',name:'mappSql',inputWidth:300,rows:2,offsetLeft:7},
                            {type:"hidden",name:'intTableName'},
                            {type:"hidden",name:'oldDataSourceId'}
        ]},
	  {type:"block",className:'btnStyle',list:[{type:'button',name:"addBtn",value:"新增"},
		                               {type:"newcolumn"},
		                               {type:'button',name:"resetBtn",value:"清空"}]}
	];

var addFun = function(){
       var dhxWindow = new dhtmlXWindows();
       var popWindow = dhxWindow.createWindow("popWindow", 0, 0,500,290);
       popWindow.setModal(true);
       popWindow.center();
	   popWindow.button("minmax1").hide();
	   popWindow.button("minmax2").hide();
	   popWindow.button("park").hide();
	   popWindow.denyResize();
	   popWindow.denyPark();
	   popWindow.show();
	   popWindow.setText("新增维度表映射接口关系");
	   var addForm =  popWindow.attachForm(addFormDate);
	   addForm.getCombo("intTableId").enableFilteringMode(true);
	   addForm.getCombo("intTableId").enableOptionAutoPositioning(true);
	   //addForm.getCombo("intTableId").setAutoOpenListWidth(true);
	   addForm.getCombo("tableId").enableFilteringMode(true);
	   addForm.getCombo("tableId").enableOptionAutoPositioning(true);
	   //addForm.getCombo("tableId").setAutoOpenListWidth(true);
	   addForm.getCombo("sysId").loadXML(dwrCaller.querySystemData,function(data){
		  var sysId = addForm.getCombo("sysId").getSelectedValue();
		  queryDataSourceBySysId(addForm,sysId);
		  addForm.getCombo("sysId").attachEvent('onChange',function(){
			   var sysId = addForm.getCombo("sysId").getSelectedValue();
			   queryDataSourceBySysId(addForm,sysId);
		  });
	   });
       addForm.defaultValidateEvent();
       addForm.attachEvent("onButtonClick",function(id){
		   var addParam = new biDwrMethodParam();
		   addParam.setParamConfig([
           {
             index:0,type:"fun",value:function(){
        	      var intTableName = addForm.getCombo("intTableId").getSelectedText();
	        	  addForm.setItemValue("intTableName",intTableName);
	        	  addForm.setItemValue("userId",user.userId);
	              return addForm.getFormData();
             }
          }
        ]);
		   if(id=="addBtn"){
			   if(addForm.validate()){
				   dwrCaller.executeAction("addData",addParam,function(data){
					   if(data==1){
						   popWindow.close();
						   dhxWindow.unload();
						   mygrid.clearAll();  
	                       mygrid.load(dwrCaller.queryDate,"json");
					   }
					   if(data==-2){
						    dhx.alert("对不起,你选择的维度接口表已经被关联,请重新选择!");
					   }
					   if(data==-1){
						    dhx.alert("对不起,你选择的源系统的维度表已经存在,无需再次新增");
						    popWindow.close();
						    dhxWindow.unload();
					   }if(data==0){
						    dhx.alert("对不起,新增失败");
						    popWindow.close();
						    dhxWindow.unload();
					   }
				   });
			   }
		   }
		   if(id=="resetBtn"){
			   addForm.clear();
		   }
	   });
}
//from数据

var formDate=[
	  {type:"setting",position: "label-left"},
	  {type:"block", list:[{type:"combo",label:'源系统：',name:'sysId',inputWidth:200,inputHeight:20,readonly:true,offsetLeft:42},
		                  {type:"combo",label:'数据源：',name:'dataId',inputWidth:200,inputHeight:20,readonly:true,offsetLeft:42},
		                  {type:"combo",label:'接口表类：',name:'intTableId',inputWidth:200,inputHeight:20,readonly:true,offsetLeft:30},
                          {type:"combo",label:'维度表类：',name:'tableId',inputWidth:200,inputHeight:20,readonly:true,offsetLeft:30},
                          {type:"block",list:[{type:"input",label:'源系统表名称：',name:'srcTableName',inputWidth:300,offsetLeft:6,validate:'NotEmpty'}, {type:"newcolumn"},{type:"label",label:'<span style="color:red">*</span>'}]},
                          {type:"input",label:'映射说明：',name:'mappMark',inputWidth:300,rows:2,offsetLeft:30},
                          {type:"input",label:'映射规则SQL：',name:'mappSql',inputWidth:300,rows:2,offsetLeft:7},
                          {type:"hidden",name:'userId'},
                          {type:"hidden",name:'oldSysId'},
                          {type:"hidden",name:'oldTableId'},
                          {type:"hidden",name:'intTableName'},
                          {type:"hidden",name:'oldDataSourceId'}
        ]},
	  {type:"block",className:'btnStyle',list:[{type:'button',name:"updateBtn",value:"修改"},
		                               {type:"newcolumn"},
		                               {type:'button',name:"resetBtn",value:"重置",offsetLeft:5}]}
	];

var updateFun = function(oldSysId,olddataId,oldTableId,userId,intTableId,srcTableName,mapingSql,mappingMask){
       var dhxWindow = new dhtmlXWindows();
       var popWindow = dhxWindow.createWindow("popWindow", 0, 0,500,290);
       popWindow.setModal(true);
       popWindow.center();
	   popWindow.button("minmax1").hide();
	   popWindow.button("minmax2").hide();
	   popWindow.button("park").hide();
	   popWindow.denyResize();
	   popWindow.denyPark();
	   popWindow.show();
	   popWindow.setText("修改维度表映射接口关系");
	   var updateForm = popWindow.attachForm(formDate);
	   updateForm.defaultValidateEvent();
	   var initData={
		   sysId:oldSysId,
		   dataId:olddataId,
		   tableId:oldTableId,
		   intTableId:intTableId,
		   srcTableName:srcTableName,
		   mappMark:mapingSql,
		   mappSql:mappingMask
	   }
	   updateForm.getCombo("intTableId").enableFilteringMode(true);
	   updateForm.getCombo("intTableId").enableOptionAutoPositioning(true);
	   //updateForm.getCombo("intTableId").setAutoOpenListWidth(true);
	   updateForm.getCombo("tableId").enableFilteringMode(true);
	   updateForm.getCombo("tableId").enableOptionAutoPositioning(true);
	   //updateForm.getCombo("tableId").setAutoOpenListWidth(true);
//	   if(++sysCount>=2){
//		   updateForm.getCombo("sysId").attachEvent('onSelectionChange',function(){
//			   var sysId = updateForm.getCombo("sysId").getSelectedValue();
//			   queryDataSourceBySysId(updateForm,sysId);
//		    });
//	   }
	   var sysCount = 0 ; //应用系统的加载的计数器,在修改的时候,因为给combo赋值,控件自身会调用onSelectionChange事件,为带出修改的信息,当计数器大于2的时候才去加载更改之后的数据
	   var dsCount =  0 ; //
	   var temp=0;
	   updateForm.getCombo("sysId").loadXML(dwrCaller.querySystemData,function(data){
		 (++temp==4)&&updateForm.setFormData(initData);
	   });
//	   updateForm.getCombo("sysId").attachEvent('onSelectionChange',function(){
//			   var sysId = updateForm.getCombo("sysId").getSelectedValue();
//			   if(++sysCount>=2){
//			      queryDataSourceBySysId(updateForm,sysId);
//			   }
//	   });
	    updateForm.getCombo("dataId").loadXML(dwrCaller.queryDataSource+"&sysId=0",function(data){
	    	(++temp==4)&&updateForm.setFormData(initData);
	    });
//	    updateForm.getCombo("dataId").attachEvent('onSelectionChange',function(){
//			   var dataId = updateForm.getCombo("dataId").getSelectedValue();
//			   if(++dsCount>=2){
//			      queryDataIntByDSId(updateForm,dataId);
//			      queryDataDimByDSId(updateForm,dataId);
//			   }
//		    });
	    updateForm.getCombo("intTableId").loadXML(dwrCaller.queryIntTableNameData+"&dsId=-1",function(data){
	    	(++temp==4)&&updateForm.setFormData(initData);
	    });
	    updateForm.getCombo("tableId").loadXML(dwrCaller.queryDimTableData+"&dsId=-1",function(data){
	    	(++temp==4)&&updateForm.setFormData(initData);
	    });
//	    
//	   updateForm.getCombo("dataId").loadXML(dwrCaller.queryDataSource+"&sysId=0",function(data){
//			updateForm.getCombo("dataId").attachEvent('onChange',function(){
//			   var dsId = updateForm.getCombo("dataId").getSelectedValue();
//			    queryDataDimByDSId(updateForm,dsId);
//		        queryDataIntByDSId(updateForm,dsId);
//			   
//		  });
//	   });
	   updateForm.attachEvent("onButtonClick",function(id){
		   var updateParam = new biDwrMethodParam();
		   updateParam.setParamConfig([
           {
             index:0,type:"fun",value:function(){
	        	  updateForm.setItemValue("oldSysId",oldSysId);
	        	  updateForm.setItemValue("oldDataSourceId",olddataId);
	        	  updateForm.setItemValue("oldTableId",oldTableId);
	        	  var intTableName = updateForm.getCombo("intTableId").getSelectedText();
	        	  updateForm.setItemValue("intTableName",intTableName);
	        	  updateForm.setItemValue("userId",userId);
	              return updateForm.getFormData();               
             }
          }
        ]);
		   if(id=="updateBtn"){
			   if(updateForm.validate()){
				   dwrCaller.executeAction("updateData",updateParam,function(data){
					   if(data==1){
						   popWindow.close();
						   dhxWindow.unload();
						   mygrid.clearAll();  
	                       mygrid.load(dwrCaller.queryDate,"json");
					   }
				   });
			   }
		   }
		   if(id=="resetBtn"){
			   updateForm.setFormData(initData);
		   }
	   });
}
var deleteFun = function(oldSysId,olddataId,oldTableId,userId){
	dhx.confirm("你确定删除本条记录?",function(b){
		if(b){
			dwrCaller.executeAction("delData",{sysId:oldSysId,dataId:olddataId,tableId:oldTableId,userId:userId},function(data){
				   if(data==1){
					   mygrid.clearAll();
                       mygrid.load(dwrCaller.queryDate,"json");
				   }
			 });
		}
	})
	
}

var initPage = function(){
    var mappLayout = new dhtmlXLayoutObject("container", "2E");
	    mappLayout.cells("a").setText("维度表接口映射规则维护");
        mappLayout.cells("b").hideHeader();
        mappLayout.cells("a").setHeight(80);
        mappLayout.cells("a").fixSize(false, true);
        mappLayout.hideConcentrate();
        mappLayout.hideSpliter();
        var queryData = [{type:"setting",position: "label-left"},
    	                 {type:"block",list:[     
    	            	                 {type:"input",label:"表类：",name:'tableName',inputWidth:120,inputHeight:17,offsetLeft:10},
    	            	                 {type:"newcolumn"},
                                         {type:"input",label:"接口表：",name:'mappTableName',inputWidth:120,inputHeight:17,offsetLeft:10},
    	            	                 {type:"newcolumn"},
                                         {type:"input",label:"源系统表：",name:'srcTableName',inputWidth:120,inputHeight:17,offsetLeft:10},
    	            	                 {type:"newcolumn"},
                                         {type:"button",name:"query",value:"查询",offsetLeft:20},
                                         {type:"newcolumn"},
                                         {type:"button",name:"add",value:"新增",offsetLeft:5}
                                        ]}
    	
    ]
    var queryform = mappLayout.cells("a").attachForm(queryData);  
    //查询事件 设置参数(获取form的值,在表格load的时候传参数用参数组件)
    var mappParam = new biDwrMethodParam();
    mappParam.setParamConfig([
        {
            index:0,type:"fun",value:function(){
            var formData =  queryform.getFormData();
            formData.tableName = Tools.trim(queryform.getInput("tableName").value);
            formData.mappTableName = Tools.trim(queryform.getInput("mappTableName").value);
            formData.srcTableName = Tools.trim(queryform.getInput("srcTableName").value);
            return formData;
            }
        }
    ]);
    //转化器
    var gridConverter = new dhtmxGridDataConverter(converterConfig);
    //加载表格数据的action
    dwrCaller.addAutoAction("queryDate", DimIntRelMangerAction.queryDataByCondition,mappParam,{
    dwrConfig:true,
    converter:gridConverter
   });
    queryform.attachEvent("onButtonClick",function(id){
	    if(id=="query"){
		     mygrid.clearAll();  
		     mygrid.load(dwrCaller.queryDate,"json");
	     }
	     if(id=="add"){
	    	 addFun();
	     }
    })//end queryEvent
     /**
   * 按enter键实现查询
   */
  var enterFun = function (inputName){
	  // 添加Enter查询事件
    queryform.getInput(inputName).onkeypress=function(e){
        e=e||window.event;
        var keyCode=e.keyCode;
        if(keyCode==13){
             mygrid.clearAll();
		     mygrid.load(dwrCaller.queryDate,"json");
        }
    }
  }
    enterFun("tableName");
    enterFun("mappTableName");
    enterFun("srcTableName");
    mygrid = mappLayout.cells("b").attachGrid();
	mygrid.setHeader("表类,系统名称,接口表名称,源表名称,规则说明,映射SQL,操作");
	mygrid.setInitWidthsP("30,10,23,10,10,10,7");
	mygrid.setHeaderAlign("center,center,center,center,center,center");
	mygrid.setColAlign("left,left,left,left,left,left,left");
	mygrid.setColTypes("ro,ro,ro,ro,ro,ro,ro");
	mygrid.setColSorting("na,na,na,na,na,na,na");
	mygrid.setEditable(false);
	mygrid.setColumnIds("tableName","sysName","intTabName","srcTabName","dataMappMark","dataMappSql");
	mygrid.init();
    mygrid.defaultPaging(20);
	mygrid.load(dwrCaller.queryDate,"json");

}//end initPage
dhx.ready(initPage);
