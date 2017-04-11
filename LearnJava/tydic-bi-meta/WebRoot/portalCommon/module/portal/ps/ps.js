/**
 * 页面初始化。
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var loadUserParam=new biDwrMethodParam();//loaduser Action参数设置。
/**
 * 定义全局的部门、岗位、地域树
 */
var globDeptTree=null;
var dwrCaller=new biDwrCaller();
/**
 * 用户页面初始化方法
 */
var userInit=function(){
    var applyWindow = new dhtmlXLayoutObject(document.body,"1C");
	var applyWindows = applyWindow.cells("a");
	    applyWindows.setText("批示信息");
	    
    //加载查询表单
    var addForm = applyWindows.attachForm([
	  {type:"setting",position: "label-left"},
	  {type:"block", list:[
                      {type:'block',list:[
                    	  {type:"combo",label:'报表名称：',name:'reportId',inputWidth:370,offsetLeft:33},
                    	  {type:"newcolumn"}
                       ]},
                      {type:'block',list:[
                    	  {type:"combo",label:'指标名称：',name:'indexId',inputWidth:370,offsetLeft:33},
                    	  {type:"newcolumn"}
                       ]},
                      {type:'block',list:[
                    	  {type:"input",label:'人员选择：',name:'userNames',id:'userNames',inputWidth:370,offsetLeft:33,validate:"NotEmpty",readonly:"readonly"},
                    	  {type:"hidden",name:'userIds',id:'userIds'},
                    	  {type:"newcolumn"},
                          {type:"label",label:'<span style="color:red">*</span>'}
                       ]},
                      {type:'block',list:[
                    	  {type:"input",label:'批示标题：',name:'noticeTitle',inputWidth:370,offsetLeft:33,validate:"NotEmpty,MaxLength[64]"},
                    	  {type:"newcolumn"},
                          {type:"label",label:'<span style="color:red">*</span>'}
                       ]},
                      {type:'block',list:[
                    	  {type:"input",label:'批示内容：',name:'noticeContent',inputWidth:370,offsetLeft:33,rows:4,validate:"NotEmpty,MaxLength[600]"},
                    	  {type:"newcolumn"},
                          {type:"label",label:'<span style="color:red">*</span>'}
                       ]}
                       
        ]},
       {type:"block",className:'btnStyle',list:[
		                               {type:"button",name:"save",value:"保存", offsetLeft:150},
                                       {type:"newcolumn"},
                                       {type:"button",name:"close",value:"关闭"}
		                               ]}
    	
  
       
        
    ]);
         //添加验证
    addForm.defaultValidateEvent();
     //加载报表名称 
    var options = [];
    ReportConfigAction.getReportTabMes(null,{
        async:false,
        callback:function(list) {
            for(var i = 0; i < list.length; i++){
                options.push({text:list[i].TAB_NAME,value:list[i].TAB_ID});
            }
        }
    });
   addForm.getCombo("reportId").addOption(options);
   addForm.getCombo("reportId").selectOption(0,false,false);
   
   //加载指标名称
    var tabId=addForm.getCombo("reportId").getSelectedValue();
    var options = [];
    ReportConfigAction.getUserIndexsByTabId(tabId,{
        async:false,
        callback:function(list) {
            for(var i = 0; i < list.length; i++){
                options.push({text:list[i].INDEX_NAME,value:list[i].INDEX_CD});
            }
        }
    });
   addForm.getCombo("indexId").addOption(options);
   addForm.getCombo("indexId").selectOption(0,false,false);
   //事件
   addForm.attachEvent("onChange", function (id, value){
    	if(id=="reportId"){
    		if(value != ""){
    		     addForm.getCombo("indexId").clearAll();//清空	
    	         var options = [];
    	         ReportConfigAction.getUserIndexsByTabId(value,{
			        async:false,
			        callback:function(list) {
			            for(var i = 0; i < list.length; i++){
			                options.push({text:list[i].INDEX_NAME,value:list[i].INDEX_CD});
			            }
			        }
			    });
			   addForm.getCombo("indexId").addOption(options);
			   addForm.getCombo("indexId").selectOption(0,false,false);
    	   }
    	}
	});
    
  //加载部门树
 // loadDeptTreeChkbox(null,addForm);
   
   var target=addForm.getInput("userNames");
    Tools.addEvent(target,"click",function(){
    	         var url=base+"/meta/module/mag/selectedUser/ryxz.jsp";
				var obj= window.showModalDialog(url+'?t='+Math.random(),'newWin','center:yes;status:no;scroll:no;help:no;dialogWidth=800px;dialogHeight=500px');
				if (typeof(obj) != "undefined") { 
				       target.value=obj.names;
				       addForm.setFormData({userIds:obj.ids});
				}
           
        });
  //查询表单事件处理
  addForm.attachEvent("onButtonClick", function (id) {
        if (id == "save") {
            if (addForm.validate()) {
                //保存
                var data = addForm.getFormData();
                var reportName = addForm.getCombo("reportId").getSelectedText();
                data.reportName=reportName;
                var indexName =  addForm.getCombo("indexId").getSelectedText();
                data.indexName=indexName;
               PortalInstructionAction.addPsInfo(data,function(rs){
                    if(rs){
                        dhx.alert("新增成功");
                        applyWindow.unload();
                    }else{
                        dhx.alert("对不起，新增出错，请重试！");
                    }
                });
            }

        }
        if (id == "close") {
          applyWindow.unload();
        }
    });
    
}


dwrCaller.addAutoAction("loadDeptTree","UserAction.queryUserByPath");
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptId",pidColumn:"parentId",
    isDycload:false,textColumn:"deptName"
});
dwrCaller.addDataConverter("loadDeptTree",treeConverter);
//树动态加载Action
dwrCaller.addAction("querySubDept",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},treeConverter,false);
    UserAction.querySubUser(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
/**
 * 部门树input框Html。
 * @param selectDept 已经选择了的部门。
 */
var loadDeptTree=function(selectDept,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectDept=selectDept|| global.constant.defaultRoot;
//    var beginId=global.constant.defaultRoot;
    var beginId=1;
    //创建tree Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    document.body.appendChild(div);
    //移动节点位置至指定节点下。
    var target=form.getInput("user");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
//    tree.enableSmartRendering();
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubDept);
    //树双击鼠标事件
    tree.attachEvent("onDblClick",function(nodeId){
        form.setFormData({dept:tree.getItemText(nodeId),deptId:nodeId});
        //关闭树
        div.style.display="none";
    });
    dwrCaller.executeAction("loadDeptTree",beginId,selectDept,function(data){
        tree.loadJSONObject(data);
        globDeptTree = tree;
        if(selectDept){
            tree.selectItem(selectDept); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    });
    div.style.display="none";
}

//为查询时可多选，新添加的TREE
var loadDeptTreeChkbox=function(selectDept,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectDept=selectDept|| global.constant.defaultRoot;
//    var beginId=global.constant.defaultRoot;
    var beginId=1;
    //创建tree Div层
    var div = dhx.html.create("div", {
        style:"display;none;position:absolute;border: 1px #eee solid;height: 190px;width: 180px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:'_deptDiv'
    });
    document.body.appendChild(div);
    //创建tree div
    var treeDiv = dhx.html.create("div", {
        style:"position:relative;height: 190px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000"
    });
    div.appendChild(treeDiv);
    //创建一个按钮div层
//    var buttonDiv = dhx.html.create("div", {
//        style:"position:relative;border: 1px #eee solid;height: 30px;overflow: auto;padding-top:0px;margin-top: 6px;" +
//              "z-index:1000;padding-left:50px"
//    })
//    div.appendChild(buttonDiv);
    //创建一个button


    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableCheckBoxes(true);
    tree.enableThreeStateCheckboxes(1);
    tree.enableHighlighting(true);
    tree.setDataMode("json");
    tree.attachEvent("onCheck",function(id,state){
        var checkedData = [];
        var allChecked = tree.getAllChecked();
        //寻找ID所有子节点

        var nodes = typeof allChecked == "string" ? allChecked.split(",") : [allChecked];
        var depts = "";
        var deptsId = "";
        for (i = 0;i < nodes.length; i++){
            var scanIds = typeof tree.getAllSubItems(nodes[i]) == "string" ? tree.getAllSubItems(nodes[i]).split(","): [tree.getAllSubItems(nodes[i])];
            if( scanIds.length > 1 ){
                for (j = 0; j< scanIds.length; j++){
                    if(depts == ""){
                        depts =  tree.getItemText(scanIds[j]).toString();
                    }else{

                        depts =   depts + "," +  tree.getItemText(scanIds[j]).toString() ;
                    }
                    if(deptsId == ""){
                        deptsId = scanIds[j].toString();
                    }else{
                        deptsId = deptsId   + "," + scanIds[j].toString();
                    }
                }
            }else{
                if(depts == ""){
                    depts =  tree.getItemText(nodes[i]).toString();
                }else{
                    depts =   depts + "," +  tree.getItemText(nodes[i]).toString() ;
                }
                if(deptsId == ""){
                    deptsId = nodes[i].toString();
                }else{
                    deptsId = deptsId   + "," + nodes[i].toString();
                }
            }



        }
        if(depts == 0){
            depts = "";
            deptsId = null;
        }

        form.setFormData({user:depts,userId:deptsId});
//        div.style.display = "none";
    });
    tree.attachEvent("onSelect",function(id){
        tree.setCheck(id,1);
        var checkedData = [];
        var allChecked = tree.getAllChecked();
        //寻找ID所有子节点

        var nodes = typeof allChecked == "string" ? allChecked.split(",") : [allChecked];
        var depts = "";
        var deptsId = "";
        for (i = 0;i < nodes.length; i++){
            var scanIds = typeof tree.getAllSubItems(nodes[i]) == "string" ? tree.getAllSubItems(nodes[i]).split(","): [tree.getAllSubItems(nodes[i])];
            if( scanIds.length > 1 ){
                for (j = 0; j< scanIds.length; j++){
                    if(depts == ""){
                        depts =  tree.getItemText(scanIds[j]).toString();
                    }else{

                        depts =   depts + "," +  tree.getItemText(scanIds[j]).toString() ;
                    }
                    if(deptsId == ""){
                        deptsId = scanIds[j].toString();
                    }else{
                        deptsId = deptsId   + "," + scanIds[j].toString();
                    }
                }
            }else{
                if(depts == ""){
                    depts =  tree.getItemText(nodes[i]).toString();
                }else{
                    depts =   depts + "," +  tree.getItemText(nodes[i]).toString() ;
                }
                if(deptsId == ""){
                    deptsId = nodes[i].toString();
                }else{
                    deptsId = deptsId   + "," + nodes[i].toString();
                }
            }



        }
        if(depts == 0){
            depts = "";
            deptsId = null;
        }

        form.setFormData({user:depts,userId:deptsId});
//        div.style.display = "none";
    });


    tree.setXMLAutoLoading(dwrCaller.querySubDept);
//    div.style.display = "none";
    var that = this;
    //移动节点位置至指定节点下。
    var target=form.getInput("user");
    //target.readOnly=true;


    dwrCaller.executeAction("loadDeptTree",beginId,selectDept,function(data){
        tree.loadJSONObject(data);
        globDeptTree = tree;
        if(selectDept){
            tree.selectItem(selectDept); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
        }
        //为div添加事件
        Tools.addEvent(target,"click",function(){
            //div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    });
    div.style.display="none";
}
dhx.ready(userInit);