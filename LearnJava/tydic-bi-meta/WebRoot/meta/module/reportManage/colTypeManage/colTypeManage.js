
/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        colTypeManage.js
 *Description：
 *       字段分类管理，用于管理字段字段分类：新增分类、删除分类、修改分类、分类层次调整..etc。
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        tanwc
 ********************************************************/

/**
 * 字段分类管理类
 * 		构成：内容面板，公共操作区
 * 
 */
var colTypeManage = function() {
	var c_self = this;
	
	/**
	 * 定义字段分类管理的全局变量
	 */
	c_self.layout = new dhtmlXLayoutObject(document.body, "1C");
	
	c_self.dwrCaller = new biDwrCaller();
	dhtmlx.image_path = getDefaultImagePath();
	dhtmlx.skin = getSkin();
	
	var config = {
	    idColumn:"colTypeId",pidColumn:"parentId",
	    filterColumns:["colTypeName","_buttons"],
	    isDycload:true,
	    userData:function(rowIndex, rowData) {
	        var userData = {};
            userData["colTypeId"] = rowData["colTypeId"];
	        userData["parentId"] = rowData["parentId"];
	        userData["colTypeName"] = rowData["colTypeName"];
	        userData["colTypeOrder"] = rowData["colTypeOrder"];
	        userData["children"] = rowData["children"];
	        userData["showIndexpageFlag"] = rowData["showIndexpageFlag"];
	        return userData;
	    },
	    
	    cellDataFormat:function(rowIndex, columnIndex, columnName, cellValue, rowData) {
	        if (columnName == '_buttons') {
	            return "getButtons";
	        }
	        return this._super.cellDataFormat(rowIndex, columnIndex, columnName, cellValue, rowData);
	    }
	}
	
	c_self.coverter = new dhtmlxTreeGridDataConverter(config);
	
	c_self.dwrCaller.addAutoAction("loadColType", "ColTypeAction.getAllColType");
	c_self.dwrCaller.addDataConverter("loadColType", c_self.coverter);
	
	c_self.dwrCaller.addAction("loadSubColType",function(afterCall,id){
	    var tempCovert=dhx.extend({isDycload:true},c_self.coverter,false);
	    ColTypeAction.getSubColType(id,function(data){
	        data=tempCovert.convert(data);
	        afterCall(data);
	    })
	});
	
	c_self.dwrCaller.addAutoAction("changeLevel", ColTypeAction.changeLevel);
	c_self.dwrCaller.isDealOneParam("changeLevel", false);
	
	
	/**
	 * 初始化类
	 */
	this.init = function() {
		this.contentPannel._init();
	};
	
	/**
	 * 内容面板
	 */
	this.contentPannel = {
		/**
		 * 初始化内容区域
		 */
		_init:function(){
			var self = this;
			self.build();
			self.loadData();
			self._bindAction2LevelModify();
			self._bindAction2AddRootColType();
			self.bindSortAction();
			
		},
		/**
		 * 构建字段分类内容表格
		 * @memberOf {TypeName}
		 */
		build:function() {
			var self = this;
		    c_self.layout.hideConcentrate();
			c_self.layout.cells("a").setText("字段分类维护");
			
			self.grid = c_self.layout.cells("a").attachGrid();
		    self.imagePath = dhtmlx.image_path + "csh_" + dhtmlx.skin + "/";
		    self.grid.setImagePath(self.imagePath);
			self.grid.setHeader("字段分类,<span>操作</span>" +
								"<span style='position:relative;left:10px;width: 18px; height: 18px;'>" +
								"<a id='levelModify' href='javascript:void(0)'>确定层级修改</a>" +
								"<a id='addColType2Root' style='margin-left:20px;' href='javascript:'>新增顶级分类</a>" +
								"<span>");
			self.grid.setInitWidthsP("60,40");
		    self.grid.setColAlign("left,center");
		    self.grid.setHeaderAlign("center,center");
		    self.grid.enableResizing("true,true");
		    self.grid.setColTypes("tree,sb");
		    self.grid.setColSorting("na,na");
		    self.grid.setColumnIds("menuName");
		    self.grid.enableTooltips("true,true");
		    self.grid.enableTreeGridLines();
		    self.grid.setEditable(false);
		    self.grid.enableDragAndDrop(true);
		    self.grid.setDragBehavior("complex");
		    self.grid.init();
		    self.grid.kidsXmlFile = c_self.dwrCaller.loadSubColType;
		},
		
		/**
		 * 加载字段分类数据
		 */
		loadData:function() {
			var self = this;
			self.grid.load(c_self.dwrCaller.loadColType, "json");
   			self.addButtons2Layout();
		},
		/**
		 * 绑定确定层级修改事件
		 */
		_bindAction2LevelModify:function() {
			var self = this;
			var $levelModify =$('levelModify');
			Tools.addEvent($levelModify,'click',levelModify);
			function levelModify(){
				if(Tools.isEmptyObject(self.modifyMoveData)){
	                dhx.alert("您还没有修改字段分类层级或顺序！");
	            }else{
	                var levelData=[];
	                for(var colTypeId in self.modifyMoveData){
	                    levelData.push({colTypeId:colTypeId,colTypeOrder:self.modifyMoveData[colTypeId].colTypeOrder,parentId:self.modifyMoveData[colTypeId].parentId});
	                }
	                c_self.dwrCaller.executeAction("changeLevel",levelData,self.orderData,function(data){
	                    if(data){
	                        self.grid.forEachRow(function(id){
	                           self.grid.setRowTextStyle(id,self.modifyStyle.clear);
	                        });
	                        self.modifyMoveData={};
	                        self.orderData={};
							self.grid.clearAll();
							self.grid.load(c_self.dwrCaller.loadColType, "json");
	                        dhx.alert("层级修改成功！");
	                    }else{
	                        dhx.alert("修改失败，请重试！");
	                    }
	                });
	            }
			}
		},
		/**
		 * 绑定事件到新增顶级分类
		 * @memberOf {TypeName} 
		 */
		_bindAction2AddRootColType:function() {
			var self = this;
			var $addColType2Root = $('addColType2Root');
			Tools.addEvent($addColType2Root,'click',function(){
				self.OperColType('','root');
			});
		},
		/**
		 * 添加按钮功能。for example : 增加同级分类、修改 、删除 等操作
		 * @memberOf {TypeName} 
		 */
		addButtons2Layout:function() {
			var self = this;
			var buttons = {
	        	addColType:{name:"addColType",text:"新增同级分类",
		            onclick:function(rowData){
		                self.OperColType(rowData, "same");
		            }},
		        addSubColType:{name:"addSubColType",text:"新增下级分类",
		           onclick:function(rowData){
		                self.OperColType(rowData, "next");
		            }} ,
	            modifyColType:{name:"modifyColType",text:"修改",
	            	onclick:function(rowData){
		                self.OperColType(rowData, "modify");
		            }
				},
		        deleteColType:{name:"deleteColType",text:"删除",
		            onclick:function(rowData){
		                self.deleteColType(rowData);
		            }}
		        
            }
			var buttonTags = ["addColType","addSubColType","modifyColType","deleteColType"];
			var bottonRoleRow = [];
	    	for(var i = 0; i < buttonTags.length; i++){
	           		bottonRoleRow.push(buttonTags[i]);
		    }
	    	//全局方法getButtons
			window["getButtons"] = function(){
			    var res = [];
			    for(var i = 0; i < bottonRoleRow.length; i++){
			        res.push(buttons[bottonRoleRow[i]]);
			    }
			    return res;
			};
		},
		
		/**
		 * 操作字段分类
		 * @param obj  当前选中的字段分类对象
		 * @param type 新增类型: same、next、root 分别代表新增同级和新增下级,修改类型：modify
		 */
		OperColType:function(obj,type) { 
			var self = this;
			var dialogTitle = '';
			var parentId = '';
			if( type == 'root' ){
				dialogTitle = '新增顶级分类';
				parentId = '0';
			}else if( type == "next" ) {
				dialogTitle = '新增下级分类';
				parentId = obj.userdata.colTypeId;
			}else if( type == "same" ){
				dialogTitle = '新增同级分类';
				parentId = obj.userdata.parentId;
			}else if( type == "modify" ){
				dialogTitle = '修改分类';
			}
		    var dhxWindow = new dhtmlXWindows();
		    var winSize=Tools.propWidthDycSize(15,20,15,20);
		    winSize.width=550;
		    dhxWindow.createWindow("window", 0, 0, 150, 280);
		    var window = dhxWindow.window("window");
		    window.setModal(true);
		    window.setDimension(400, 200);
		    window.center();
		    window.button("minmax1").hide();
		    window.button("minmax2").hide();
		    window.button("park").hide();
		    window.denyResize();
		    window.denyPark();
		    window.setText(dialogTitle);
		    window.keepInViewport(true);
		    window.show();
	        var addColTypeLayout = new dhtmlXLayoutObject(window, "1C");
		    addColTypeLayout.cells("a").hideHeader();
		    addColTypeLayout.cells("a").fixSize(true, true);
		    
		    var data = [ 
				{type: "block",offsetTop:20,list:[
		            {type:"input",id:'colTypeName',label:"<span style='margin-left:25px;line-height:22px;'>名称：</span>",
		            	name:"colTypeName",inputWidth:250,inputHeight:20,validate:"NotEmpty,MaxLength[100]"}
		         ]},
				{type: "block",offsetTop:5,list:[
			        {type:"checkbox",label:'是否首页显示',inputHeight:20,name:"isShowIndex",checked:"true"}
		         ]},
			    {type:"block",offsetTop:20,inputTop :20,list:[
		            {type: "settings", position: "label-left"},
		            {type:"button",label:"保存",name:"save",value:"保存",offsetLeft:135},
		            {type:"newcolumn"},
		            {type:"button",label:"关闭",name:"close",value:"关闭"}
		        ]}
		    ]
	        
	        var Form = addColTypeLayout.cells("a").attachForm(data);
		    Form.defaultValidateEvent();
			if(type == 'modify'){
				Form.setItemValue('colTypeName',obj.userdata.colTypeName);
				if(obj.userdata.showIndexpageFlag == 1){
					Form.checkItem("isShowIndex");
				}else{
					Form.uncheckItem("isShowIndex");
				}
			    Form.attachEvent("onButtonClick", function(id){
			        if(id == "save"){
			        	if(Form.validate()) {
	        		        var name = Form.getFormData().colTypeName;
	        		        var isShowIndex = Form.isItemChecked("isShowIndex")?1:0;
					        ColTypeAction.updateColType(obj.userdata.colTypeName,obj.userdata.colTypeId,Form.getFormData().colTypeName,obj.userdata.parentId,isShowIndex, function(data){
					        	if( data.type == "error" || data.type == "invalid" ){
					        		dhx.alert("修改失败！");
					        		return;
					        	}else if( data.type == "nameExist" ) {
					        		dhx.alert("修改失败，同一层级下名称不能重复！");
					        		return;
					        	}
	        	                dhx.alert("修改成功！");
	            				window.close();
					        	self.grid.updateGrid(c_self.coverter.convert(data.successData), "update");
					        });
			        	}
			        } else if(id == "close"){
			            window.hide();
			            Form.unload();
			            dhxWindow.unload();
			        }
		    	});
			}else {
			    Form.attachEvent("onButtonClick", function(id){
			        if(id == "save"){
			        	if(Form.validate()) {
	        		        var name = Form.getFormData().colTypeName;
	        		        var isShowIndex = Form.isItemChecked("isShowIndex")?1:0;

					        ColTypeAction.addColType(name,parentId,isShowIndex, function(data){
					        	if( data.type == "error" || data.type == "invalid" ){
					        		dhx.alert("新增失败！");
					        		return;
					        	}else if( data.type == "nameExist" ) {
					        		dhx.alert("新增失败，同一层级下名称不能重复！");
					        		return;
					        	}
	        	                dhx.alert( "新增成功！" );
					        	self.grid.updateGrid(c_self.coverter.convert(data.successData), "insert");
								self.grid.clearAll();
								self.grid.load(c_self.dwrCaller.loadColType, "json");
	            				window.close();
					        });
			        	}
			        } else if(id == "close"){
			            window.hide();
			            Form.unload();
			            dhxWindow.unload();
			        }
		    	});
		    }
		},
		/**
		 * 修改字段分类
		 * @param obj 当前要修改的字段分类对象
		 */
		updateColType:function(obj) {
			var self = this;
			alert('修改ID为【'+obj.id+'】的分类');
			
		},
		/**
		 * 删除字段分类
		 * @param {Object} obj 当前要删除的字段分类对象
		 */
		deleteColType:function(obj) {
			var self = this;
			c_self.dwrCaller.addAutoAction("deleteColType", "ColTypeAction.deleteColType", function(data){
			    dhx.closeProgress();
			    if(data.type == "error" || data.type == "invalid"){
			    	if(data.sid == -1) {
			    		dhx.alert("对不起，该字段分类下有子类，不能删除！");
			    	}else if(data.sid == -2){
			        	dhx.alert("对不起，该字段分类被模型引用，不能删除！");
			        }else{
			        	dhx.alert("对不起，删除出错，请重试！");
			        }
			    } else{
			        dhx.alert("删除成功");
					self.grid.clearAll();
					self.grid.load(c_self.dwrCaller.loadColType, "json");
			        self.grid.deleteRow(data.sid);
			    }
			})
		    dhx.confirm("确定要删除该字段分类？", function(r){
		        if(r){
		            dhx.showProgress("字段分类管理", "正在提交删除请求...");
		            c_self.dwrCaller.executeAction("deleteColType", obj.userdata.colTypeId ,obj.userdata.parentId);
		        }
		    });
		},
		
		/**
		 * 绑定排序事件
		 */
		bindSortAction:function() {
			var self = this;
			var moveDataSrcParId={};//移动数据原始父节点。
			self.modifyMoveData={};//修改的层级移动数据，以colTypeId作为键值，value为{colTypeOrder，parentId}
			self.modifyStyle = {
			    move:"font-weight:bold;color:#EEC900",
			    order: "font-weight:bold;color:#7A378B",
			    clear:"font-weight:normal;font-style::normal;text-decoration:none;color:black;border-bottom:0px;"
			};
			self.orderData={};
			self.grid.attachEvent("onDrag", function(sId, tId, sObj, tObj, sInd, tInd){
		        if(!tId){ //拖动至grid区域以外，直接返回。
		            return false;
		        }
		        //缓存TD节点以及子节点数据
		        currRowdata = backUpGridData(sId);
		        var x = self.grid._h2.get[sId];
		        //记录节点原来位置
		        if(currRowdata[sId]._orgIndex == null || currRowdata[sId]._orgIndex == undefined){
		            currRowdata[sId]._orgIndex = x.index;
		        }
		        //如果源ID的父节点只有一个子节点了，将其图标设置为叶子节点的图标。
		        var sparId = self.grid.getParentId(sId);
		        if(self.grid.getSubItems(sparId).split(",").length == 1){
		            self.grid.rowsAr[sparId]&&(self.grid.rowsAr[sparId].imgTag.nextSibling.src=self.imagePath+"/leaf.gif");
		            self.grid._h2.get[sparId].has_kids=false;//无子菜单表示
		        }
		        //缓存当前图标列的类型
		        //iconImage=self.grid.cells(sId,3).cell._cellType;
		        return true;
		    });
		    self.grid.attachEvent("onDrop", function(sId, tId, dId, sObj, tObj, sCol, tCol){
		        var sparId = self.grid.getParentId(sId);
		        var tparId = self.grid.getParentId(tId);
		        if(sparId != tparId){ //父子节点拖动
		            self.grid.rowsAr[tId]&&(self.grid.rowsAr[tId].imgTag.nextSibling.src=self.imagePath+"/folderOpen.gif");
		        }
		        sparId&& (self.grid.rowsAr[sparId]&&(self.grid.rowsAr[sparId].imgTag.nextSibling.src=self.imagePath+"/folderOpen.gif"));
		        restoreGridData(sId, currRowdata);
		        dealMoveData(sId, tId, currRowdata[sId],sparId != tparId);
		        //self.grid.setCellExcellType(sId,3,iconImage);
		    });
		    self.grid.attachEvent("onOpenStart", function(id,state){
		        //因为dhtmlx在移动过程中，会自动展开下级，且会生成一个新的ID，其ID由时间种子生成，大于Java所表示的Integer的最大值。
		        //这里返回不让其展开，以免报错
		        if(state==-1&&parseInt(id)>2147483648){
		            return false;
		        }
		        return true;
		    });
		    
		    var backUpGridData = function(id){
			    //数据备份
			    var ids = [id];
			    var currRowdata = {};
			    var subIds = self.grid.getSubItems(id);
			    if(subIds){
			        ids = ids.concat(subIds.split(","));
			    }
			    for(var i = 0; i < ids.length; i++){
			        currRowdata[ids[i]] = self.grid.getRowData(ids[i]);
			    }
			    return currRowdata;
			}
		    
		    var restoreGridData = function(id, currRowdata){
			    var ids = [id];
			    var subIds = self.grid.getSubItems(id);
			    if(subIds){
			        ids = ids.concat(subIds.split(","));
			    }
			    for(var i = 0; i < ids.length; i++){
			        var cell= self.grid.cells(ids[i], 0).cell;
			        self.grid.cells(ids[i], 0).cell.parentNode._attrs = currRowdata[ids[i]];
			        if(cell._cellType=="img"){
			            self.grid.cells(ids[i],3).cell.lastChild.style.width="18px";
			            self.grid.cells(ids[i],3).cell.lastChild.style.height="18px";
			        }
			        if(self.grid.hasChildren(ids[i])){
			            self.grid.rowsAr[ids[i]]&&(self.grid.rowsAr[ids[i]].imgTag.nextSibling.src=dhtmlx.image_path + "csh_" + dhtmlx.skin + "/"+"/folderOpen.gif");
			        } else{
			            self.grid.rowsAr[ids[i]]&&(self.grid.rowsAr[ids[i]].imgTag.nextSibling.src=dhtmlx.image_path + "csh_" + dhtmlx.skin + "/"+"/leaf.gif");
			        }
			    }
			}
		    
		    var dealMoveData = function(sId, tId, sRowData,isChildMove){
			    var moveData = {colTypeOrder:sRowData.userdata.colTypeOrder,parentId:sRowData.userdata.parentId};
			    if(isChildMove){
			        moveData.parentId=tId;
			    }else{
			        moveData.parentId=self.grid.getParentId(tId);
			    }
			    if(moveDataSrcParId[sId] == undefined || moveDataSrcParId[sId] == null){
			        moveDataSrcParId[sId] = sRowData.getParentId();
			    }
			    var x = self.grid._h2.get[sId];
			    self.modifyMoveData[sId]=moveData;
			    var parId=moveData.parentId;
			    //节点再次移动，比较节点原来的父ID与现在的父ID是否相同，相同则已经移到了原来的位置
			    if(isChildMove){
			        if(moveDataSrcParId[sId] == tId){
			            if(sRowData._orgIndex == x.index){
			                self.grid.setRowTextStyle(sId,self.modifyStyle.clear);
			                self.modifyMoveData[sId]=null;
			                delete self.modifyMoveData[sId];
			            }else{
			                self.grid.setRowTextStyle(sId,self.modifyStyle.order);
			            }
			        }else{
			            self.grid.setRowTextStyle(sId, self.modifyStyle.move);
			        }
			    }else{
			        if(moveDataSrcParId[sId] == parId){
			            if(sRowData._orgIndex == x.index){
			                self.grid.setRowTextStyle(sId,self.modifyStyle.clear);
			                self.modifyMoveData[sId]=null;
			                delete self.modifyMoveData[sId];
			            }else{
			                self.grid.setRowTextStyle(sId,self.modifyStyle.order);
			            }
			        }else{
			            self.grid.setRowTextStyle(sId,self.modifyStyle.move);
			        }
			    }
			    //排序处理     计算order值。原理：同层级order值从前往后依次从1递加。
			    var childCount=self.grid.hasChildren(parId);
			    if(childCount>0){
			        for(var i=0;i<childCount;i++){
			            var childId=parseInt(self.grid.getChildItemIdByIndex(parId,i));
			            if(self.grid.getRowData(childId).userdata.colTypeOrder!=(i+1)){
			                self.orderData[childId]=i+1;
			            }else{
			                delete  self.orderData[childId];
			            }
			        }
			    }
			}
		}
	}
}


/**
 * 生成字段分类管理类实例
 */
var genObj = function() {
	var instance = new colTypeManage();
	instance.init()
}
dhx.ready(genObj);