/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       favCommon.js
 *Description：
 *       收藏订阅功能共用js
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，公共JS
 *Author:
 *       李国民
 *Date：
 *       12-3-20
 ********************************************************/


/**************************添加收藏**************************/	

	var dwrCaller = new biDwrCaller();
	var comReportId;	//报表id
	var queryForm;		//收藏报表
	var querySubForm;	//收藏并订阅报表
	
    //添加报表收藏传入值
	var params = new biDwrMethodParam();
    params.setParamConfig([
        {
            index:0,type:"fun",value:function(){
	            var formData=queryForm.getFormData();
	            formData.reportId =	comReportId;
	            return formData;
	        }
        }
    ]);
	//添加报表收藏
	dwrCaller.addAutoAction("insertFavorite", "RepSupermarketAction.insertFavorite",params);
	dwrCaller.isShowProcess("insertFavorite",true);

	//添加报表收藏并订阅
	dwrCaller.addAutoAction("insertFavoriteAndSub", "RepSupermarketAction.insertFavoriteAndSub");
	dwrCaller.isShowProcess("insertFavoriteAndSub",true);


	//收藏报表
	favorites = function(reportId,reportName,dataType,e){
		comReportId = reportId;		//把报表id存为全局变量
    	queryForm.setFormData({reportName:reportName});
		$("favDiv").style.display="block";
		//如果添加并订阅选中，则打开订阅div
		if(queryForm.isItemChecked("isSub")){
			$("subDiv").style.display="block";
		}
		setComboValue(dataType);
	}
	
	//根据模式数据类型（天报表还是月报表）,设置发送频度选择值
	setComboValue = function(dataType){
		querySubForm.getCombo("sendSequnce").clearAll(true);
	    //加载发送频度数据
		var empStr = "";
		if(dataType==11){//模型为按天
			empStr = "每天";
		}else if(dataType==22){//模型为按月
			empStr = "每月";
		}
	    var reportSendType = getComboByTypeNames("PERORT_SEND_TYPE",empStr);
	    querySubForm.getCombo("sendSequnce").addOption(reportSendType.options);
	}

	//生成添加收藏夹页面
	initForm = function(){
		var dhxLayout = new dhtmlXLayoutObject("favDiv","1C");
		var topCell = dhxLayout.cells("a");
		topCell.setText("将报表放入自己的收藏夹中");
		topCell.fixSize(true,true);
		queryForm = topCell.attachForm(	
			[
				{type: "block",  list:[
					{type:'input',label:'报表名称：',name:'reportName',labelWidth:80, inputWidth:160,inputHeight:17,readonly:true}
				]},
				{type: "block", list:[
					{type:'input',label:'创建位置：',name:'favorName',validate:"NotEmpty",labelWidth:80,inputWidth:160,inputHeight:17,readonly:true}
				]},
				{type: "block", list:[
			        {type:"checkbox",label:'添加并订阅',name:"isSub"},
	        		{type:"newcolumn"},
			        {type:"button",name:"insert",value:"添加",offsetLeft:0,offsetTop:0},
	        		{type:"newcolumn"},
			        {type:"button",name:"cancel",value:"取消",offsetLeft:0,offsetTop:0}
				]}
		    ]
	    );
		queryForm.defaultValidateEvent();		//form内数据验证方法
		//当checkbox改变值时，调用方法，显示或者隐藏订阅div
		queryForm.attachEvent("onChange", function(id, value,checkState){
			if(id == "isSub"){
				if(checkState){
					$("subDiv").style.display="block";
				}else{
					$("subDiv").style.display="none";
				}
			}
		});
		//添加按钮点击事件
		queryForm.attachEvent("onButtonClick", function(id) {
	        if (id == "insert") {
        		if(queryForm.validate()){
		            //添加收藏
					dwrCaller.executeAction('insertFavorite',function(rs){
				        //隐藏div
						$("favDiv").style.display="none";
						$("subDiv").style.display="none";
						if(rs==1){
							dhx.alert("收藏失败！该报表已经存在于收藏中！");
						}else if(rs==2){
							dhx.alert("报表收藏成功！");
						}else{
							dhx.alert("收藏失败！请联系管理员！");
						}
					});
	        	}
	        }else if (id == "cancel") {
	            //隐藏div
				$("favDiv").style.display="none";
				$("subDiv").style.display="none";
	        }
	    });
		$("favDiv").style.display="none";	//生成form后隐藏div
		
		var subLayout = new dhtmlXLayoutObject("subDiv","1C");
		var subCell = subLayout.cells("a");
		subCell.hideHeader();
		subCell.fixSize(true,true);
		querySubForm = subCell.attachForm(
			[
				{type: "block",  list:[
					{type: "label", label: "发送方式：",labelWidth:80},
	        		{type:"newcolumn"},
					{type: "checkbox",label: "邮件", name: "sendMethod1", value:"1",checked:true},
	        		{type:"newcolumn"},
					{type: "checkbox",label: "彩信", name: "sendMethod2", value:"2"},
	        		{type:"newcolumn"},
					{type: "checkbox",label: "短信", name: "sendMethod3", value:"3"}
				]},
				{type: "block",  list:[
					{type: "radio", name: "typeFlag", value: "1", label: "定时发送：",checked: true,list:[
				        {type:"combo",label:"报表发送频度：",name:"sendSequnce",
				        	inputHeight:17,labelWidth:80,inputWidth:140,readonly:true},
				        {type:"newrows"},
						{type:"calendar",name: "sendTime",label:"初次发送时间：",labelWidth:80,inputWidth:140,inputHeight:17,
				        	dateFormat: "%Y-%m-%d %H:%i:%s", skin: "dhx_skyblue",enableTime: true,readonly:true}
					]},
					{type: "newrows"},
					{type: "radio", name: "typeFlag", value: "2", label: "固定发送：",list:[
						{type:"calendar",name: "fixedTime",label:"固定发送时间：",labelWidth:80,inputWidth:140,inputHeight:17,
							dateFormat: "%Y-%m-%d %H:%i:%s", skin: "dhx_skyblue",enableTime: true,readonly:true}
					]}
				]},
				{type: "block",  list:[
			    	{type:"button",name:"insertAndSub",value:"添加并订阅"}
				]}
		    ]
	    );
		querySubForm.defaultValidateEvent();		//form内数据验证方法
		var tomorrow = new Date();
		tomorrow.setDate(new Date().getDate()+1);
		var dataCalendar = querySubForm.getCalendar("fixedTime");    
		dataCalendar.setSensitiveRange(tomorrow, null);
		dataCalendar.loadUserLanguage('zh');	//将控件语言设置成中文
		var dataCalendar = querySubForm.getCalendar("sendTime");    
		dataCalendar.setSensitiveRange(tomorrow, null);
		dataCalendar.loadUserLanguage('zh');	//将控件语言设置成中文
		
		//设置按钮点击事件
		querySubForm.attachEvent("onButtonClick", function(id) {
	        if (id == "insertAndSub") {
        		if(queryForm.validate()){
	            	var formData = getData();
	            	if(formData){
			            //收藏并订阅报表
						dwrCaller.executeAction('insertFavoriteAndSub',formData,function(rs){
					        //隐藏div
							$("favDiv").style.display="none";
							$("subDiv").style.display="none";
							if(rs==1){
								dhx.alert("收藏并订阅失败！该报表已经存在于收藏中！无法收藏！");
							}else if(rs==2){
								dhx.alert("报表收藏并订阅成功！");
							}else{
								dhx.alert("收藏并订阅失败！请联系管理员！");
							}
						});
	            	}
	        	}
	        }
	    });
		$("subDiv").style.display="none";	//生成form后隐藏div
		
	    //加载业务树
		queryGroupTree(null);
	}
	
	/**
	 * 加载业务类型树
	 */
	dwrCaller.addAutoAction("queryFavoriteGroup", "RepSupermarketAction.queryFavoriteGroup");
	dwrCaller.isShowProcess("queryFavoriteGroup", false);
	dwrCaller.addDataConverter("queryFavoriteGroup", new dhtmxTreeDataConverter({
	    idColumn:"favoriteId",textColumn:"favoriteName",
	    pidColumn:"parentId",isDycload:false
	}));
	
	var div = null,target = null,tree = null; 
	/**
	 * 加载业务类型树
	 * @param selectGroup
	 * @param levelRefID 层次管理ID
	 */
	var queryGroupTree=function(){
	    //创建tree Div层
	    var div=dhx.html.create("div",{
	        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;overflow: auto;padding: 0;margin: 0;" +
	              "z-index:1000"
	    });
	    document.body.appendChild(div);
	    //移动节点位置至指定节点下。
    	var target=queryForm.getInput("favorName");
	    target.readOnly=true;
	    //生成树
	    tree = new dhtmlXTreeObject(div, div.style.width, div.style.height, 0);
	    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
	    tree.enableThreeStateCheckboxes(true);
	    tree.enableHighlighting(true);
	    tree.enableSingleRadioMode(true);
	    tree.setDataMode("json")
	    //点击事件，每次都需查询数据
	    Tools.addEvent(target,"click",function(){
	      	var childs = tree.getSubItems(0);
	        if(childs){
	         	var childIds = (childs + "").split(",");
	     		for(var i = 0; i < childIds.length; i++){
	      			tree.deleteItem(childIds[i]);
	       		}
	      	}
		    dwrCaller.executeAction("queryFavoriteGroup",function(data){
		  		tree.loadJSONObject(data);
		   	});
	      	div.style.width = target.offsetWidth+'px';
	     	Tools.divPromptCtrl(div,target,true);
	      	div.style.display="block";
	  	});
	    //树双击鼠标事件
	    tree.attachEvent("onDblClick",function(nodeId){
	    	//favoriteId
        	queryForm.setFormData({favorName:tree.getItemText(nodeId),favoriteId:nodeId});
	        //关闭树
	        div.style.display="none";
	    });
	}
	
	//得到收藏并订阅储存数据
	var getData = function(){
		var data = querySubForm.getFormData();
	    data.reportId =	comReportId;
		var pushType = "";
		if(querySubForm.isItemChecked("sendMethod1")){	//如果选中，添加值
			pushType += querySubForm.getItemValue("sendMethod1")+",";
		}
		if(querySubForm.isItemChecked("sendMethod2")){	//如果选中，添加值
			pushType += querySubForm.getItemValue("sendMethod2")+",";
		}
		if(querySubForm.isItemChecked("sendMethod3")){	//如果选中，添加值
			pushType += querySubForm.getItemValue("sendMethod3")+",";
		}
		if(pushType==""){
			dhx.alert("请至少选择一种发送方式！");
			return null;
		}
		data.pushType = pushType.substr(0,pushType.length-1);;			//保存发送方式
		var typeFlag = querySubForm.getCheckedValue("typeFlag");
		if(typeFlag==1){
			var sendSequnce = querySubForm.getCombo("sendSequnce").getSelectedValue();
			if(!sendSequnce){
				dhx.alert("请选择发送频度！");
				return null;
			}
			var sendTime = querySubForm.getInput("sendTime").value
			if(!sendTime){
				dhx.alert("请选择初次发送时间！");
				return null;
			}
   			data.sendBaseTime = sendTime;		//保存初次发送时间
		}else{
			var fixedTime = querySubForm.getInput("fixedTime").value
			if(!fixedTime){
				dhx.alert("请选择固定发送时间！");
				return null;
			}
    		data.sendBaseTime = fixedTime;		//保存固定发送时间
    		data.sendSequnce = 0;				//发送频度为固定时间发送
		}
		data.favoriteId = queryForm.getFormData().favoriteId;
		return data;
	}