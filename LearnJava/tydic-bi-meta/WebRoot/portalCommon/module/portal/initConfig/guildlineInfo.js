/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       guildlineConfig.js
 *Description：
 *       通用门户指标配置折线图
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       王晶
 *Date：
 *       2012-05-17
 ********************************************************/
/**
 * 获取全局变量。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var addTimeInterval = null;
var arrValue=[];
var indexInfo=[]; //加载指标名称的数组
var indexInfo2=[];//加载指标编码的数组
var timeType = null;
var indexExpLain1 = null;
var timeIntervalIds =null;

//获取绝对坐标
var getAbsPoint = function (e)
{
    var x = e.offsetLeft;
    var y = e.offsetTop;
    while(e = e.offsetParent)
    {
        x += e.offsetLeft;
        y += e.offsetTop;
    }
    return {'x': x, 'y': y};
}; 
/**
 *加载表格数据
 */
var dwrCaller = new biDwrCaller();
var repConvertConfig ={
    idColumn:"tabId",
    filterColumns:["indexName","indexCd","indexExplain","ruleName","_buttons"],
    userData:function(rowIndex,rowData){
	    var userData ={};
        return userData;
    },
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
        if(columnName == '_buttons'){
          return"<a href='#' onclick=\"updateFunGuildlineInfo('"+rowData.indexCd+"','"+rowData.indexName+"','"+rowData.indexExplain+"','"+rowData.ruleName+"',"+rowData.ruleType+");\">修改</a>   <a href='#' onclick=\"deleteGuildlineInfo('"+rowData.indexCd+"');return false;\">删除</a>";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}
var repConverter = new dhtmxGridDataConverter(repConvertConfig);
dwrCaller.addAutoAction("getRepExpByTabId",ReportConfigAction.getRepExpByTabId,{
    dwrConfig:true,
    converter:repConverter,
    async:false,
    isShowProcess:false
});
/**
 *加载时间间隔
 */
/**
  * 新增指标属性
  */

//新增的action
//点击的方法
dwrCaller.addAutoAction("addData","ReportConfigAction.saveExp"); 
var addFormDate=[
	  {type:"setting",position: "label-left"},
	  {type:"block", list:[
                             {type:'block',list:[{type:"combo",label:'指标名称：',name:'indexName',inputWidth:200,offsetLeft:33,validate:'NotEmpty'}, {type:"newcolumn"},{type:"label",label:'<span style="color:red">*</span>'}]},
                             {type:'block',list:[{type:"label",label:'<span style="font-size:12px;color:#000">指标编码：</span>',offsetLeft:33}, {type:"newcolumn"},{type:'label',label:'<span id ="indexCode" style="font-size:12px;color:#000;"></span>',name:'indexCode',readonly:true,inputWidth:200}]},
                             {type:'block',list:[{type:"label",label:'<span style="color:#000;font-size:12px;padding-left:12px;">时间间隔类型：</span>'},
                    	                  {type:"newcolumn"},
                                          {type:"radio",value:1,name:"timeIntervalType",checked:true},
                                          {type:"newcolumn"},
                                          {type:"label",label:'<span style="color:#000;font-size:12px;">按天</span>'},
                                          {type:"newcolumn"},
                                          {type:"radio",value:2,name:"timeIntervalType"},
                                          {type:"newcolumn"},
                                          {type:"label",label:'<span style="color:#000;font-size:12px;">按月</span>'},
                                          {type:"newcolumn"},
                                          {type:"radio",value:3,name:"timeIntervalType"},
                                          {type:"newcolumn"},
                                          {type:"label",label:'<span style="color:#000;font-size:12px;">按周</span>'}
                                         ]},
                           {type:'block',list:[{type:"input",label:'时间间隔规则：',name:'timeInterval',inputWidth:200,offsetLeft:10,validate:'NotEmpty'},{type:"newcolumn"},{type:"label",label:'<span style="color:red">*</span>'},{type:"hidden",name:'addTimeInterval',inputWidth:200,offsetLeft:10}]},
                           {type:'block',list:[{type:"input",label:'指标解释：',name:'indexExpLain',inputWidth:200,rows:6,offsetLeft:33,validate:'NotEmpty'},{type:"newcolumn"},{type:"label",label:'<span style="color:red">*</span>'}]}
        ]},
	  {type:"block",className:'btnStyle',list:[{type:'button',name:"addBtn",value:"新增"},
		                               {type:"newcolumn"},
		                               {type:'button',name:"resetBtn",value:"清空"}]}
	]
var addGuildlineInfo = function(){
	 var popWindow = dhxWindow.createWindow("popWindow", 0, 0,380,240);
         popWindow.setModal(true);
         popWindow.center();
	     popWindow.button("minmax1").hide();
	     popWindow.button("minmax2").hide();
	     popWindow.button("park").hide();
	     popWindow.denyResize();
	     popWindow.denyPark();
	     popWindow.show();
	     popWindow.setText("新增指标解释");
	 var addForm =popWindow.attachForm(addFormDate);
	 addForm.defaultValidateEvent();
	 var indexName = addForm.getCombo("indexName");
	 indexName.addOption(indexInfo);
	 indexName.attachEvent('onChange', function(){
		document.getElementById("indexCode").innerHTML=indexName.getSelectedValue();
	 });
	 var ele = addForm.getInput("timeInterval");
	 ele.onclick = function(){
		var p =getAbsPoint(this);
		$("divSlect").style.top = p.y+this.offsetHeight +'px';
        $("divSlect").style.left = p.x +'px';
        $("divSlect").style.zIndex='100';
        $("divSlect").style.display='block';
        $("divSlect").style.position='relative';
        initCheckboxList($("divSlect"),this);
	 }
	  addForm.attachEvent("onChange",function(id){
		  if(id=='timeIntervalType'){
			   var timeIntervalType = addForm.getItemValue("timeIntervalType");
			   loadData(timeIntervalType);
		  }
	  });
	 addForm.attachEvent("onButtonClick",function(id){
	   if(id=="addBtn"){
		   if(true){
			   var indexCd = addForm.getCombo("indexName").getSelectedValue();
			   if(validateCode(indexCd)){
				    dhx.alert("对不起,此编码已经添加");
				    return;
			   }
			  else{
				  var indexName = addForm.getCombo("indexName").getSelectedText();
				  var indexCd = addForm.getCombo("indexName").getSelectedValue();
			      var indexExpLain = addForm.getItemValue("indexExpLain");
			      var addTimeInterval = addForm.getItemValue("addTimeInterval");
			     dwrCaller.executeAction("addData",{indexCd:indexCd,tabId:tabId,indexName:indexName,indexExpLain:indexExpLain,timeIntervalId:$("addTimeInterval").value},function(data){
					   if(data){
						   popWindow.close();
						   attGrid.load(dwrCaller.getRepExpByTabId+"?tabId="+tabId,"json");
					   }else{
						  dhx.alert("对不起,新增失败");
					   }
				   });
			   }
		   }
	   }
	   if(id=="resetBtn"){
		   addForm.clear();
	   }
   });
}
/**
 * 修改指标属性
 */
dwrCaller.addAutoAction("updateData","ReportConfigAction.updateExp"); 
var updateDate=[
	  {type:"setting",position: "label-left"},
	  {type:"block", list:[
                            {type:'block',list:[{type:"label",label:'<span style="font-size:12px;color:#000">指标名字：</span>',offsetLeft:33}, {type:"newcolumn"},{type:'label',label:'<span id ="indexName" style="font-size:12px;color:#000;"></span>',name:'indexName',readonly:true,inputWidth:200}]},
                            {type:'block',list:[{type:"input",label:'时间间隔规则：',name:'timeInterval',inputWidth:200,offsetLeft:10}]},
                            {type:'block',list:[{type:"input",label:'指标解释：',name:'indexExpLain',inputWidth:200,rows:6,offsetLeft:33,validate:'NotEmpty'},{type:"newcolumn"},{type:"label",label:'<span style="color:red">*</span>'}]}
        ]},
	  {type:"block",className:'btnStyle',list:[{type:'button',name:"updateBtn",value:"修改"},
		                               {type:"newcolumn"},
		                               {type:'button',name:"resetBtn",value:"重置"}]}
	]
var updateFunGuildlineInfo = function(indexCd,indexName,indexExpLain,timeIntervalId,type){
	 timeType = type;
	 indexExpLain1 = indexExpLain; 
	 timeIntervalIds =timeIntervalId;
	 var updteWindow = dhxWindow.createWindow("updteWindow", 0, 0,380,240);
         updteWindow.setModal(true);
         updteWindow.center();
	     updteWindow.button("minmax1").hide();
	     updteWindow.button("minmax2").hide();
	     updteWindow.button("park").hide();
	     updteWindow.denyResize();
	     updteWindow.denyPark();
	     updteWindow.show();
	     updteWindow.setText("修改指标解释");
	 var updateForm =updteWindow.attachForm(updateDate);
	 //updateForm.setItemValue("indexName", indexName);
	 document.getElementById("indexName").innerHTML=indexName;
	 updateForm.setItemValue("indexExpLain",indexExpLain );
	 updateForm.setItemValue("timeInterval",timeIntervalId);
	 updateForm.defaultValidateEvent();
	 var ele = updateForm.getInput("timeInterval");
	 ele.onclick = function(){
		var p =getAbsPoint(this);
        $("divSlect").style.top = p.y+this.offsetHeight +'px';
        $("divSlect").style.left = p.x +'px';
        $("divSlect").style.zIndex='100';
        $("divSlect").style.display='block';
        $("divSlect").style.position='relative';
        initCheckboxList($("divSlect"),this,timeType);
//        ReportConfigAction.getInterval2(1,timeType,{
//    	async:false,
//    	callback:function(list) {
//		    var liVaule = "<ul>" 
//	    	for(var i = 0; i < list.length; i++){
//	    		liVaule+="<li><input name='checkbox' type='checkbox' value="+list[i].RULR_ID+"><span>"+list[i].RULE_NAME+"</span></li>"
//	    	}
//		    liVaule+="</ul>"
//		   $("checkList").innerHTML=liVaule;
//    	}
//    });
	 }
	 updateForm.attachEvent("onButtonClick",function(id){
	   if(id=="updateBtn"){
		   if(updateForm.validate()){
			   var indexExpLain = updateForm.getItemValue("indexExpLain");
			   var timeInterval = updateForm.getItemValue("timeInterval");
			   dwrCaller.executeAction("updateData",{indexName:indexName,indexExpLain:indexExpLain,timeIntervalId:$("addTimeInterval").value,indexCd:indexCd,tabId:tabId},function(data){
				   if(data){
					   updteWindow.close();
					   attGrid.load(dwrCaller.getRepExpByTabId+"?tabId="+tabId,"json");
					    window.location.reload();
				   }
			   });
		   }else{
			   dhx.alert("对不起,修改失败");
		   }
	   }
	   if(id=="resetBtn"){
		   updateForm.clear();
	       updateForm.setItemValue("indexExpLain",indexExpLain1);
	       updateForm.setItemValue("timeInterval",timeIntervalIds);
	   }
   });
}
/**
 * 删除的方法
 */
dwrCaller.addAutoAction("deleteData","ReportConfigAction.delExp"); 
var deleteGuildlineInfo = function(indexCd){
	dhx.confirm("你确定删除本条记录?",function(b){
		if(b){
			dwrCaller.executeAction("deleteData",{indexCd:indexCd,tabId:tabId},function(data){
				if(data){
					dhx.alert("删除成功!");
					attGrid.load(dwrCaller.getRepExpByTabId+"?tabId="+tabId,"json");
					 window.location.reload();
				}else{
					dhx.alert("删除失败!");
				}
			})
		}
	})
}
/***
  * init函数
  */
var guildlineInfoInit = function(){
	dhxWindow = new dhtmlXWindows();
	ReportConfigAction.getIndexMes(tabId,{
		async:false,
    	callback:function(list){
		  if(list!=null&&list.length!=0){
			  for(var i = 0 ;i<list.length;i++){
				  var valueArr = [];
				  valueArr.push(list[i].INDEX_CD);
				  valueArr.push(list[i].INDEX_NAME);
				  indexInfo.push(valueArr);
				  var codeArr = [];
				  codeArr.push(list[i].INDEX_NAME);
				  codeArr.push(list[i].INDEX_CD);
				  indexInfo2.push(codeArr);
			  }
		  }
    	}
	});
	var attLayout = new dhtmlXLayoutObject("main","1C"); //页面整体布局
	attLayout.cells("a").fixSize(true,false);
    var toolbar = attLayout.cells("a").attachToolbar();
    toolbar.addSeparator("tableName",1);
    toolbar.addButton('addRep', 2, '新增', base + "/meta/resource/images/addMenu.png", base + "/meta/resource/images/addMenu.png");
    toolbar.addSpacer("tableName");
    toolbar.addText("levelSelect",3,"<span>&nbsp;&nbsp;</span>");
    toolbar.attachEvent("onclick", function(id){
    	if(id == "addRep"){
    		addGuildlineInfo();
    	}
    });
    //数据表单
    attLayout.cells("a").hideHeader();
    attGrid = attLayout.cells("a").attachGrid();
    attGrid.setHeader("指标名称,指标编码,指标解释,当期时间间隔规则,操作");
    attGrid.setInitWidthsP("15,10,43,20,12");
    attGrid.setColAlign("left,left,left,left,center");
    attGrid.setHeaderAlign("center,center,center,center,center");
    attGrid.setColTypes("ro,ro,ro,ro,ro");
    attGrid.setColSorting("na,na,na,na,na");
    attGrid.enableResizing("true,true,true,true,true");
    attGrid.setColumnIds("indexName","indexCd","indexExplain","ruleName");
    attGrid.enableDragAndDrop(true);//设置拖拽
    //attGrid.setDragBehavior("complex");
    attGrid.init();
    attGrid.load(dwrCaller.getRepExpByTabId+"?tabId="+tabId,"json");
}
/**
 *点击input出现加载时间间隔的处理方法
 */
var $ = function (id,obj) {  
    obj = obj|| document;
    return "string" == typeof id ? obj.getElementById(id) : id;   
};  
var $$ = function (name,obj) {  
    obj = obj|| document;
    return "string" == typeof name ? obj.getElementsByTagName(name) : name;   
}; 
//获取下一个节点
function getNextChild(obj) { 
    var result = obj.nextSibling; 
    while (!result.tagName) { 
         result = result.nextSibling; 
    } 
    return result; 
}

var initCheckboxList = function(divCheckboxList,divInput,type)
{
    var aList = $$('a',divCheckboxList);
    loadData(type);
    var checkBoxList = $$('input',divCheckboxList);
    aList[0].onclick=function()
    {
         for(var i=0;i<checkBoxList.length;i++){
            checkBoxList[i].checked=true;
        }
    };
    aList[1].onclick=function()
    {
        for(var i=0;i<checkBoxList.length;i++){
             checkBoxList[i].checked=!checkBoxList[i].checked;
        }
    };
     aList[2].onclick=function(){
         var arr=[];
            for(var i=0;i<checkBoxList.length;i++){
                if( checkBoxList[i].checked){
                    arrValue.push(checkBoxList[i].value);
                    arr.push(getNextChild(checkBoxList[i]).innerHTML);
                }
            }
            divInput.value = arr.join(',');
            divCheckboxList.checkedValue = arrValue.join(',');
            $("addTimeInterval").value = arrValue.join(',');
            divCheckboxList.style.display='none';
    };
    return arrValue.join(',')
}
  //加载时间间隔的数据
 var loadData = function(type){
	 ReportConfigAction.getInterval2(1,type,{
    	async:false,
    	callback:function(list) {
		    var liVaule = "<ul>" 
	    	for(var i = 0; i < list.length; i++){
	    		liVaule+="<li><input name='checkbox' type='checkbox' value="+list[i].RULR_ID+"><span>"+list[i].RULE_NAME+"</span></li>"
	    	}
		    liVaule+="</ul>"
		   $("checkList").innerHTML=liVaule;
    	}
    });
 }
 /**
   *判断新添加或者修改的指标的编码是否已经重复
   */
 var validateCode = function(code){
	 var flag = null;
	 ReportConfigAction.isExistCode(code,tabId,{
    	async:false,
    	callback:function(data) {
		  flag = data;
    	}
    });
	return flag;
 }
 /**
   *判断一个指标如果按天就不能选择按月等  
   */
dhx.ready(guildlineInfoInit);