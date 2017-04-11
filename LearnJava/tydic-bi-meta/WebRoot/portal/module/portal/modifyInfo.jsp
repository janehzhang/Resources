<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="../../public/head.jsp"%>
<%@include file="../../public/include.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>修改个人基本资料</title>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/UserAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DeptAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/StationAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript">
        var dwrCaller =new biDwrCaller();
        /**
         * 定义全局的部门、岗位、地域树
         */
        var globDeptTree=null;
        var globStationTree = null;
        var globZoneTree = null;
        userInfoInit = function(){
        	var userLayout = new dhtmlXLayoutObject(document.body,"1C");
            userLayout.cells("a").setText("修改个人基本资料");
            userLayout.cells("a").setHeight(70);
            userLayout.cells("a").fixSize(false,true);
            userLayout.hideConcentrate();
            userLayout.hideSpliter();//移除分界边框。
            //userLayout.keepInViewport(true);
            //userLayout.show();
            //加载查询表单
            /**
    		 * 修改用户表单
    		 */
    		//建立表单。
    		var modifyForm=userLayout.cells("a").attachForm(modifyUserFormData);
    		var initForm=function(){
    	        var initData={
    	            userNamecn:user.userNamecn,//用户中文名
    	            userNameen:user.userNameen,//用户拼音
    	            userMobile:user.userMobile,//用户电话
    	            userEmail:user.userEmail, //用户邮箱
    	            userId:user.userId,
    	            stationId:user.stationId,
    	            zoneId:user.zoneId,
    	            deptId:user.deptId
    	        };
    	        modifyForm.setFormData(initData);
    	    };
    		//加载部门树
    	    loadDeptTree(user.deptId,modifyForm);
    	    //加载地域树
    	    loadZoneTree(user.zoneId,modifyForm);
    	    //加载岗位树
    	    loadStationTree(user.stationId,modifyForm);
    	    initForm();
    	    /**
    	     * 添加 onButtonClick 事件
    	     */
    	    modifyForm.attachEvent("onButtonClick",function(id){
    	        if(id=="save"){//保存
    	            modifyForm.send(dwrCaller.modifyUser);
    	        }else if (id=="reset"){//重置
    	            modifyForm.clear();
    	            initForm();
    	            modifyForm.setFormData({zone:globZoneTree.getItemText(user.zoneId),zoneId:user.zoneId});
    	            modifyForm.setFormData({dept:globDeptTree.getItemText(user.deptId),deptId:user.deptId});
    	            modifyForm.setFormData({station:globStationTree.getItemText(user.stationId),stationId:user.stationId});
    	        }else if(id=="close"){
    	        	userLayout.close();
    	        }
    	    });
    	    /**
    	     * 注册DWR
    	     */
    	    dwrCaller.addAction("modifyUser", function(afterCall,param){
    	        UserAction.modifyUserInfo(param,function(data){
    	            if(data.type=="error"||data.type=="invalid"){
    	                dhx.alert("对不起，修改出错，请重试！");
    	            }else{
    	                dhx.alert("修改成功");
    	                userLayout.close();
    	            }
    	        })
    	    });
        }
        
				 
		var modifyUserFormData=[
		    {type: "block", list:[
		        {type:"settings",position: "label-left",labelAlign:"right",labelWidth:80,inputWidth: 120,labelLeft:0 ,
		            inputLeft:0,offsetTop:0},
		        {type:"input",label:"<span style='color: red'>*</span>姓名：",name:"userNamecn"},
		        {type:"newcolumn"},
		        {type:"input",label:"<span style='color: red'>*</span>地域：",name:"zone"}
		    ]},
		    {type: "block", list:[
		        {type:"settings",position: "label-left",labelAlign:"right",labelWidth:80,inputWidth: 120,labelLeft:0 ,
		            inputLeft:0,offsetTop:0},
		        {type:"input",label:"拼音：",name:"userNameen"},
		        {type:"newcloumn"},
		        {type:"input",label:"<span style='color: red'>*</span>部门：",name:"dept"}
		    ]},
		    {type: "block", list:[
		        {type:"settings",position: "label-left",labelAlign:"right",labelWidth:80,inputWidth: 120,labelLeft:0 ,
		            inputLeft:0,offsetTop:0},
		        {type:"input",label:"手机：",name:"userMobile"},
		        {type:"newcloumn"},
		        {type:"input",label:"<span style='color: red'>*</span>岗位：",name:"station"}
		    ]},
		    {type: "block", list:[
		        {type:"settings",position: "label-left",labelAlign:"right",labelWidth:80,inputWidth: 120,labelLeft:0 ,
		            inputLeft:0,offsetTop:0},
		        {type:"input",label:"<span style='color: red'>*</span>邮箱：",name:"userEmail"}
		    ]},
		    {type:"block",offsetTop:15,list:[
		        {type:"button",label:"保存",name:"save",value:"保存",offsetLeft:120},
		        {type:"newcolumn"},
		        {type:"button",label:"重置",name:"reset",value:"重置"},
		        {type:"newcolumn"},
		        {type:"button",label:"关闭",name:"close",value:"关闭"}
		    ]},
		    {type:"hidden",label:"userId",name:"userId"},
		    {type:"hidden",label:"zoneId",name:"zoneId"},
		    {type:"hidden",label:"stationId",name:"stationId"},
		    {type:"hidden",label:"deptId",name:"deptId"},
		];
		/********************地域部门岗位树状加载方法start*********************/
		dwrCaller.addAutoAction("loadDeptTree","DeptAction.queryDeptByPath");
		var treeConverter=new dhtmxTreeDataConverter({
		    idColumn:"deptId",pidColumn:"parentId",
		    isDycload:false,textColumn:"deptName"
		});
		dwrCaller.addDataConverter("loadDeptTree",treeConverter);
		//树动态加载Action
		dwrCaller.addAction("querySubDept",function(afterCall,param){
		    var tempCovert=dhx.extend({isDycload:true},treeConverter,false);
		    DeptAction.querySubDept(param.id,function(data){
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
		    var beginId=global.constant.defaultRoot;
		    //创建tree Div层
		    var div=dhx.html.create("div",{
		        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
		              "z-index:1000"
		    });
		    document.body.appendChild(div);
		    //移动节点位置至指定节点下。
		    var target=form.getInput("dept");
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
		        });
		    });
		    div.style.display="none";
		};
		
		dwrCaller.addAutoAction("loadStationTree","StationAction.queryStationByPath");
		var stationConverter=dhx.extend({idColumn:"stationId",pidColumn:"parStationId",
		    textColumn:"stationName"
		},treeConverter,false);
		dwrCaller.addDataConverter("loadStationTree",stationConverter);
		//树动态加载Action
		dwrCaller.addAction("querySubStation",function(afterCall,param){
		    var tempCovert=dhx.extend({isDycload:true},stationConverter,false);
		    StationAction.querySubStation(param.id,function(data){
		        data=tempCovert.convert(data);
		        afterCall(data);
		    });
		});
		
		/**
		 * 岗位input输入框Html
		 * @param name
		 * @param value
		 */
		var loadStationTree=function(selectStation,form){
		    //加载部门树数据。加载用户所在部门及其子部门。
		    selectStation=selectStation|| global.constant.defaultRoot;
		    var beginId=global.constant.defaultRoot;
		    //创建tree Div层
		    var div=dhx.html.create("div",{
		        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
		              "z-index:1000"
		    });
		    document.body.appendChild(div);
		    //移动节点位置至指定节点下。
		    var target=form.getInput("station");
		    target.readOnly=true;
		
		    //生成树
		    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
		    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
		    tree.enableThreeStateCheckboxes(true);
		//    tree.enableSmartRendering();
		    tree.enableHighlighting(true);
		    tree.enableSingleRadioMode(true);
		    tree.setDataMode("json");
		    tree.setXMLAutoLoading(dwrCaller.querySubStation);
		    //树双击鼠标事件
		    tree.attachEvent("onDblClick",function(nodeId){
		        form.setFormData({station:tree.getItemText(nodeId),stationId:nodeId});
		        //关闭树
		        div.style.display="none";
		    });
		    dwrCaller.executeAction("loadStationTree",beginId,selectStation,function(data){
		        tree.loadJSONObject(data);
		        globStationTree = tree;
		        if(selectStation){
		            tree.selectItem(selectStation); //选中指定节点
		            //将input框选中
		            target.value=tree.getSelectedItemText();
		        }
		        //为div添加事件
		        Tools.addEvent(target,"click",function(){
		            div.style.width = target.offsetWidth+'px';
		            Tools.divPromptCtrl(div,target,true);
		            div.style.display="block";
		        })
		    })
		    div.style.display="none";
		}
		
		dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPath");
		var zoneConverter=dhx.extend({idColumn:"zoneId",pidColumn:"zoneParId",
		    textColumn:"zoneName"
		},treeConverter,false);
		dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
		//树动态加载Action
		dwrCaller.addAction("querySubZone",function(afterCall,param){
		    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
		    ZoneAction.querySubZone(param.id,function(data){
		        data=tempCovert.convert(data);
		        afterCall(data);
		    });
		});
		/**
		 * 地域树加载
		 * @param name
		 * @param value
		 */
		var loadZoneTree=function(selectZone,form){
		    //加载部门树数据。加载用户所在部门及其子部门。
		    selectZone=selectZone|| global.constant.defaultRoot;
		    var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)
		            || global.constant.defaultRoot;
		    //创建tree Div层
		    var div=dhx.html.create("div",{
		        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
		              "z-index:1000"
		    });
		    document.body.appendChild(div);
		    //移动节点位置至指定节点下。
		    var target=form.getInput("zone");
		    target.readOnly=true;
		
		    //生成树
		    var tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
		    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
		    tree.enableThreeStateCheckboxes(true);
		//    tree.enableSmartRendering();
		    tree.enableHighlighting(true);
		    tree.enableSingleRadioMode(true);
		    tree.setDataMode("json");
		    tree.setXMLAutoLoading(dwrCaller.querySubZone);
		    //树双击鼠标事件
		    tree.attachEvent("onDblClick",function(nodeId){
		        form.setFormData({zone:tree.getItemText(nodeId),zoneId:nodeId});
		        //关闭树
		        div.style.display="none";
		    });
		    dwrCaller.executeAction("loadZoneTree",beginId,selectZone,function(data){
		        tree.loadJSONObject(data);
		        globZoneTree = tree;
		        if(selectZone){
		            tree.selectItem(selectZone); //选中指定节点
		            //将input框选中
		            target.value=tree.getSelectedItemText();
		        }
		        //为div添加事件
		        Tools.addEvent(target,"click",function(){
		            div.style.width = target.offsetWidth+'px';
		            Tools.divPromptCtrl(div,target,true);
		            div.style.display="block";
		        });
		    });
		    div.style.display="none";
		}
		/********************地域部门岗位树状加载方法end*********************/
		dhx.ready(userInfoInit);
    </script>
    <style type="text/css">
        body {
            background-color: #ffffff;
            color: #333333;
            font-size: 12px;
            font-family: "宋体",arial,Times, serif;
            line-height: 24px;
            word-break: break-all;
            margin: 0;
            padding: 0;
            height: 100%;
        }
        td{font-size: 12px;
            font-family: "宋体",arial,Times, serif;
        }
        .xiugaimima-bg{
            background:url(images/xiugaimima-bg_1.gif) no-repeat top left;
            width:608px;
            height:490px;
            margin: 0 auto;
        }

    </style>

</head>
<body style="height: 100%;width: 100%">
<div id="container" style="height: 100%;width: 100%"></div>
</body>
</html>
