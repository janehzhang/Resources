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
var attGrid = null;
var repTable = null;
var dhxWindow = null;
var myCP = null;
var indexInfo=[]; //加载指标名称的数组
var indexInfo2=[];//加载指标编码的数组
var currColor = null;
var currIndexName = null;
var currIndexCd = null;
var currIsShow = null;
var	currColChnName = null;
var indexCd2 = null;
/**
 * 获取当前报表的指标名称与code
 */
var getIndexMes = function(){
	
}
//阻止冒泡事件的处理方法
function cancelBubble (event){
    var e = (event) ? event : window.event;
    if(e.preventDefault){
        e.preventDefault();
    }
    e.cancelBubble=true;
}
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

var dwrCallerConfig={
	queryTimeIntervalData:{methodName:"ReportConfigAction.getInterval",converter:new dhtmlxComboDataConverter({
                           valueColumn:"rulrId",textColumn:"ruleName",isAdd:true,async:false
    })},
    queryColInfoData:{methodName:"ReportConfigAction.queryColInfoData",converter:new dhtmlxComboDataConverter({
                           valueColumn:"colId",textColumn:"colChnName",isAdd:true,async:false
    })}
}
/**
 *加载表格数据
 */
var dwrCaller = new biDwrCaller(dwrCallerConfig);
var repConvertConfig ={
    idColumn:"tabId",
    filterColumns:["brokenLineName","indexCd","brokenLineColor","ruleName","brokenLineDesc","colChnName","isShowCol","_buttons"],
    userData:function(rowIndex,rowData){
	    var userData ={};
        return userData;
    },
    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData){
    	if(columnName == 'isShowCol'){
          if(rowData.isShowCol==1){
        	  return "展示";
          }else{
        	  return "不展示";
          }
        }
        if(columnName == '_buttons'){
          return"<a href='#' onclick=\"updateFunGuildlineAtt('"+rowData.indexCd+"','"+rowData.brokenLineName+"','"+rowData.brokenLineDesc+"',"+rowData.timeIntervalId+",'"+rowData.brokenLineColor+"',"+rowData.isShowCol+",'"+rowData.colChnName+"');\">修改</a>   <a href='#' onclick=\"deleteGuildlineAtt('"+rowData.indexCd+"',"+rowData.timeIntervalId+");return false;\">删除</a>";
        }
        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
    }
}
var repConverter = new dhtmxGridDataConverter(repConvertConfig);
dwrCaller.addAutoAction("getRepChartByTabId",ReportConfigAction.getRepChartByTabId,{
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
dwrCaller.addAutoAction("addData","ReportConfigAction.saveChart"); 
var addFormDate=[
	  {type:"setting",position: "label-left"},
	  {type:"block", list:[
                      {type:'block',list:[{type:"combo",label:'指标名称：',name:'indexName',inputWidth:200,offsetLeft:33}, {type:"newcolumn"},{type:"label",label:'<span style="color:red">*</span>'}]},
                      {type:'block',list:[{type:"label",label:'<span style="font-size:12px;color:#000">指标编码：</span>',offsetLeft:33}, {type:"newcolumn"},{type:'label',label:'<span id ="indexCode" style="font-size:12px;color:#000;"></span>',name:'indexCode',readonly:true,inputWidth:200}]},
                      {type:'block',list:[{type:"input",label:'指标颜色配置：',name:'indexColor',inputWidth:200,offsetLeft:10,validate:'NotEmpty'},{type:"newcolumn"}, {type:"label",label:'<span style="color:red">*</span>'}]}, 
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
                      {type:'block',list:[{type:"combo",label:'时间间隔规则：',name:'timeInterval',inputWidth:200,offsetLeft:10},{type:"newcolumn"}, {type:"label",label:'<span style="color:red">*</span>'}]},
                      {type:'block',list:[{type:"combo",label:'关联列：',name:'columnId',inputWidth:200,offsetLeft:45,},{type:"newcolumn"}, {type:"label",label:'<span style="color:red">*</span>'}]},
                      {type:'block',list:[{type:"combo",label:'是否展示：',name:'isShowCol',inputWidth:200,offsetLeft:33,options:[{value:"1",text:"展示",selected:true},{value:"0",text:"不展示"}]},{type:"newcolumn"}, {type:"label",label:'<span style="color:red">*</span>'}]},
                      {type:'block',list:[{type:"input",label:'描述：',name:'indexDesc',inputWidth:200,rows:2,offsetLeft:57}]}
        ]},
	  {type:"block",className:'btnStyle',list:[{type:'button',name:"addBtn",value:"新增"},
		                               {type:"newcolumn"},
		                               {type:'button',name:"resetBtn",value:"清空"}]}
	]
var showColor = function(divList,divEle){
	var divArr = $$('div',divList);
	var divObj = null;
	for(var i = 0;i<divArr.length;i++){
		divObj = divArr[i];
		divObj.onclick = function(){
			divEle.value = this.innerHTML;
			 $("divSlect").style.display='none';
		}
	}
	
}
var addGuildlineAtt = function(){
	 var popWindow = dhxWindow.createWindow("popWindow", 0, 0,450,260);
         popWindow.setModal(true);
         popWindow.center();
	     popWindow.button("minmax1").hide();
	     popWindow.button("minmax2").hide();
	     popWindow.button("park").hide();
	     popWindow.denyResize();
	     popWindow.denyPark();
	     popWindow.show();
	     popWindow.setText("新增指标折线属性");
	 var addForm =popWindow.attachForm(addFormDate);
	 var indexName = addForm.getCombo("indexName");
	 indexName.addOption(indexInfo);
	 indexName.attachEvent('onChange', function(){
		 document.getElementById("indexCode").innerHTML=indexName.getSelectedValue();
	 });
	 addForm.getCombo("timeInterval").loadXML(dwrCaller.queryTimeIntervalData+"&state=0&type=1",function(data){});
	 addForm.getCombo("columnId").loadXML(dwrCaller.queryColInfoData+"&tableId="+tabId,function(data){});
	 var ele = addForm.getInput("indexColor");
	 ele.onclick = function(){
		var p =getAbsPoint(this);
        $("divSlect").style.top = p.y+this.offsetHeight +'px';
        $("divSlect").style.left = p.x +'px';
        $("divSlect").style.zIndex='100';
        $("divSlect").style.display='block';
        $("divSlect").style.position='relative';
        showColor($("divSlect"),this);
	 }
	 addForm.defaultValidateEvent();
	  addForm.attachEvent("onChange",function(id){
		  if(id=='timeIntervalType'){
			   var timeIntervalType = addForm.getItemValue("timeIntervalType");
			   addForm.getCombo("timeInterval").loadXML(dwrCaller.queryTimeIntervalData+"&state=0&type="+timeIntervalType,function(data){});
		  }
	  });
	 addForm.attachEvent("onButtonClick",function(id){
	   if(id=="addBtn"){
		   if(addForm.validate()){
			   var indexCd = addForm.getCombo("indexName").getSelectedValue();
			   var indexName = addForm.getCombo("indexName").getSelectedText();
			   var indexDesc = addForm.getItemValue("indexDesc");
			   var brokenLineColor =addForm.getInput("indexColor").value;
			   var timeIntervalId = addForm.getCombo("timeInterval").getSelectedValue();
			   var columnId = addForm.getCombo("columnId").getSelectedValue();
			   var isShowCol = addForm.getCombo("isShowCol").getSelectedValue();
			   dwrCaller.executeAction("addData",{tabId:tabId,indexCd:indexCd,brokenLineName:indexName,brokenLineDesc:indexDesc,brokenLineColor:brokenLineColor,timeIntervalId:timeIntervalId,columnId:columnId,isShowCol:isShowCol},function(data){
				   if(data=='3'){
					   popWindow.close();
					   dhx.alert("新增成功");
					  // attGrid.load(dwrCaller.getRepChartByTabId+"?tabId="+tabId,"json");
					     window.location.reload();
				   }
				   if(data=='1'){
					    dhx.alert("新增失败,当前报表不存在指标名称");
				   }
				   if(data=='2'){
					    dhx.alert("新增失败,当前报表已经有重复记录");
				   }
			   });
		   }else{
			   dhx.alert("对不起,新增失败");
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
dwrCaller.addAutoAction("updateData","ReportConfigAction.updateChart"); 
var updateDate=[
	  {type:"setting",position: "label-left"},
	  {type:"block", list:[
                {type:'block',list:[{type:"combo",label:'指标名称：',name:'indexName',inputWidth:200,offsetLeft:33}, {type:"newcolumn"},{type:"label",label:'<span style="color:red">*</span>'}]},
                {type:'block',list:[{type:"label",label:'<span style="font-size:12px;color:#000">指标编码：</span>',offsetLeft:33}, {type:"newcolumn"},{type:'label',label:'<span id ="indexCode2" style="font-size:12px;color:#000;"></span>',name:'indexCode',readonly:true,inputWidth:200}]},
                {type:'block',list:[{type:"input",label:'指标颜色配置：',name:'indexColor',inputWidth:200,offsetLeft:10,validate:'NotEmpty'},{type:"newcolumn"}, {type:"label",label:'<span style="color:red">*</span>'}]}, 
                 {type:'block',list:[{type:"combo",label:'关联列：',name:'columnId',inputWidth:200,offsetLeft:45,},{type:"newcolumn"}, {type:"label",label:'<span style="color:red">*</span>'}]},
                 {type:'block',list:[{type:"combo",label:'是否展示：',name:'isShowCol',inputWidth:200,offsetLeft:33,options:[{value:"1",text:"展示",selected:true},{value:"0",text:"不展示"}]},{type:"newcolumn"}, {type:"label",label:'<span style="color:red">*</span>'}]},
                 {type:'block',list:[{type:"input",label:'描述：',name:'indexDesc',inputWidth:200,rows:2,offsetLeft:57}]}
        ]},
	  {type:"block",className:'btnStyle',list:[{type:'button',name:"updateBtn",value:"修改"},
		                               {type:"newcolumn"},
		                               {type:'button',name:"resetBtn",value:"重置"}]}
	]
var updateFunGuildlineAtt = function(indexCd,indexName,indexDesc,timeIntervalId,color,isShowCol,colChnName){
	currIndexName = indexName;
	currIndexCd = indexCd;
	currIsShow = isShowCol;
	currColChnName = colChnName;
	indexCd2 = indexCd;
	 var updteWindow = dhxWindow.createWindow("updteWindow", 0, 0,380,220);
         updteWindow.setModal(true);
         updteWindow.center();
	     updteWindow.button("minmax1").hide();
	     updteWindow.button("minmax2").hide();
	     updteWindow.button("park").hide();
	     updteWindow.denyResize();
	     updteWindow.denyPark();
	     updteWindow.show();
	     updteWindow.setText("修改指标折线属性");
	 var updateForm =updteWindow.attachForm(updateDate);
	 updateForm.setItemValue("indexName", indexName);
	 document.getElementById("indexCode2").innerHTML=indexCd;
	 updateForm.setItemValue("indexDesc", indexDesc);
	 updateForm.setItemValue("indexColor",color);
	 var initDate={
		 indexName:indexName,
		 indexDesc:indexDesc,
		 indexColor:color,
	     isShowCol:isShowCol
	 }
	 if(isShowCol==0){
		 updateForm.getCombo("isShowCol").setComboText("不展示");
		 updateForm.getCombo("isShowCol").setComboValue(0);
	 }
	 updateForm.getCombo("columnId").setComboText(colChnName);
	 var indexName = updateForm.getCombo("indexName");
	 indexName.addOption(indexInfo);
	 indexName.attachEvent('onChange', function(){
		 document.getElementById("indexCode2").innerHTML=indexName.getSelectedValue();
	 });
	 var ele = updateForm.getInput("indexColor");
	 ele.onclick = function(){
		var p =getAbsPoint(this);
        $("divSlect").style.top = p.y+this.offsetHeight +'px';
        $("divSlect").style.left = p.x +'px';
        $("divSlect").style.zIndex='100';
        $("divSlect").style.display='block';
        $("divSlect").style.position='relative';
        showColor($("divSlect"),this);
	 }
	 updateForm.getCombo("columnId").loadXML(dwrCaller.queryColInfoData+"&tableId="+tabId,function(data){
		 updateForm.getCombo("columnId").setComboText(colChnName);
	 });
	 updateForm.defaultValidateEvent();
	 updateForm.attachEvent("onButtonClick",function(id){
	   if(id=="updateBtn"){
		   if(updateForm.validate()){
			   var indexName1= updateForm.getCombo("indexName").getSelectedText();
			   var indexCd1 = updateForm.getCombo("indexName").getSelectedValue();
			   if(indexName1==""){
				   indexName1 = currIndexName;
				   indexCd1 = currIndexCd;
			   }
			   var indexDesc1 = updateForm.getItemValue("indexDesc");
			   var columnId = updateForm.getCombo("columnId").getSelectedValue();
			   var isShowCol = updateForm.getCombo("isShowCol").getSelectedValue();
			   var brokenLineColor =updateForm.getInput("indexColor").value;
			   dwrCaller.executeAction("updateData",{brokenLineName:indexName1,brokenLineDesc:indexDesc1,brokenLineColor:brokenLineColor,tabId:tabId,indexCd:indexCd1,timeIntervalId:timeIntervalId,columnId:columnId,isShowCol:isShowCol},function(data){
				   if(data){
					   updteWindow.close();
					   attGrid.load(dwrCaller.getRepChartByTabId+"?tabId="+tabId,"json");
					   dhx.alert("修改成功!");
					   window.location.reload();
				   }
			   });
		   }else{
			   dhx.alert("对不起,修改失败");
		   }
	   }
	   if(id=="resetBtn"){
		   updateForm.clear();
		   updateForm.setFormData(initDate);
		   document.getElementById("indexCode2").innerHTML=indexCd2;
		   if(currIsShow==0){
		    updateForm.getCombo("isShowCol").setComboText("不展示");
		    updateForm.getCombo("isShowCol").setComboValue(0);
	      }
		updateForm.getCombo("columnId").loadXML(dwrCaller.queryColInfoData+"&tableId="+tabId,function(data){
		    updateForm.getCombo("columnId").setComboText(currColChnName);
	    });
	   }
   });
}
/**
 * 删除的方法
 */
dwrCaller.addAutoAction("deleteData","ReportConfigAction.delChart"); 
var deleteGuildlineAtt = function(indexCd,timeIntervalId){
	dhx.confirm("你确定删除本条记录?",function(b){
		if(b){
			dwrCaller.executeAction("deleteData",{tabId:tabId,indexCd:indexCd,timeIntervalId:timeIntervalId},function(data){
				if(data){
					attGrid.load(dwrCaller.getRepChartByTabId+"?tabId="+tabId,"json");
					dhx.alert("删除成功!");
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
var guildlineAttInit = function(){
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
    toolbar.addButton('addRep', 2, '新增', base + "/meta/resource/images/addMenu.png");
    toolbar.addSpacer("tableName");
    toolbar.addText("levelSelect",3,"<span>&nbsp;&nbsp;</span>");
   
    toolbar.attachEvent("onclick", function(id){
    	if(id == "addRep"){
    		addGuildlineAtt();
    	}
    });
    //数据表单
    attLayout.cells("a").hideHeader();
    attGrid = attLayout.cells("a").attachGrid();
    attGrid.setHeader("指标名称,指标编码,图形颜色,当期时间间隔规则,描述,关联列名称,是否展示,操作");
    attGrid.setInitWidthsP("15,10,10,15,20,10,8,12");
    attGrid.setColAlign("left,left,left,left,left,left,center,center");
    attGrid.setHeaderAlign("center,center,center,center,center,center,center,center");
    attGrid.setColTypes("ro,ro,ro,co,ro,ro,ro,ro");
    attGrid.setColSorting("na,na,na,na,na,na,na,na");
    attGrid.enableResizing("true,true,true,true,true,true,true,true");
    attGrid.setColumnIds("brokenLineName","indexCd","brokenLineColor","ruleName","brokenLineDesc","colChnName","isShowCol");
    attGrid.enableDragAndDrop(true);//设置拖拽
    //attGrid.setDragBehavior("complex");
    attGrid.init();
    attGrid.load(dwrCaller.getRepChartByTabId+"?tabId="+tabId,"json");    
}
dhx.ready(guildlineAttInit);