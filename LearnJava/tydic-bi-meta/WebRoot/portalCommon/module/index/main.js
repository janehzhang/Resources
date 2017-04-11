/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        main.js
 *Description：
 *       主页内容框架页调度JS
 *Dependent：
 *       写依赖文件
 *Author:
 *        张伟
 *Finished：
 *       2011-9-26
 *Modified By：程钰
 *
 *Modified Date:
 *         2011-10-13
 *Modified Reasons:
 *          添加菜单访问日志方法

 ********************************************************/
/**
 * 框架内容页面
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user = window.parent.getSessionAttribute("user");
//允许的最大tab个数
var maxTabCont = 8;

var tabCount = 0;

var indexLayout = null;
var tabLayout=null;
//主页tabbar
var mainTabbar = null;

var base = getBasePath();
//已经打开的menuTabs
var menuTabs = {};
//所有的子菜单菜单数据，如无子节点，对应空数组
var subMenuData = [];
//所有菜单数据
var allMenuData = [];
var empty = [];
var dwrCaller = new biDwrCaller();
dwrCaller.isShowProcess(false);//不用显示进度条。
dwrCaller.addAction("queryAllSubMenu", function(afterCall, param, converter) {
    LoginAction.queryAllSubMenu(param, function(data) {
        var replaceData = filterHiddenMenu(data);
        afterCall(converter.convert(replaceData));
    })
});
var   treeConverter=new dhtmxTreeDataConverter({
    /**
     * 设置自定义用户属性
     * @param rowIndex
     * @param rowData
     * @return
     */
    userData:function(rowIndex, rowData) {
        return {menuData:rowData};
    },
    isDycload:false,idColumn:"menuId",pidColumn:"parentId",textColumn:"menuName",
    isFormatColumn:false,
    isOpen:function(){//默认所有都不展开。
        return false;
    },
    afterCoverted:function(data){//等转换完成展开第一级。即根节点都展开。
        if(data){
            for(var i=0;i<data.length;i++){
                data[i].open=true;
            }
        }
        return data;
    } ,
    compare : function(data1, data2){
        if(data1.userdata[0]["content"].orderId == undefined
            || data1.userdata[0]["content"].orderId == null){
            return false;
        }
        if(data2.userdata[0]["content"].orderId == undefined
            || data2.userdata[0]["content"].orderId == null){
            return false;
        }
        return data1.userdata[0]["content"].orderId <= data2.userdata[0]["content"].orderId
    }
});
dwrCaller.addDataConverter("queryAllSubMenu",treeConverter );
//dwrCaller.addAutoAction("menuVisitLog", "MenuVisitLogAction.writeMenuLog", function() {});
/**
 * 点击菜单操作
 * @param menuId
 * @param menuData
 * @param isRefresh 如果此菜单已打开，是否进行刷新
 */
var menuClick = function(menuId, menuData,isRefresh) {
    //记录访问日志。
    if (menuId != undefined || menuId != null){
//        dwrCaller.executeAction("menuVisitLog", [
//            {menuId:menuId}
//        ]);
    }else{
        //判断menuId是否为null，如果为 null，是在数据中一个未注册的ID,伪造一个假菜单数据。
        //但menuData中必须有的三个属性为，menuName,menuUrl, target
        menuId=encodeURI(menuData.menuName);
        menuData.menuId=menuId;
        //伪造菜单数据。
        subMenuData[menuId]=empty;
    }
    
    //清理内存
    closeTabIframe();
    //处理菜单动作，是新开窗口还是openTab
    if (menuData.target == "blank") { //新开一个窗口
        openWindow(menuId, menuData);
    } else {//open Tab
        loadSubdMenuData(menuId, menuData,isRefresh, openTab);
    }

}
/**
 * 加载子菜单数据
 * @param menuId
 */
var loadSubdMenuData = function(menuId, menuData,isRefresh, afterCall) {
    if (!subMenuData[menuId]) {
        treeConverter.setRoot(menuId);
        //查询所有的子节点。
        dwrCaller.executeAction("queryAllSubMenu", menuId, function(data) {
            treeConverter.setRoot(menuId);
            if (data) {
                var item = data.item;
                if (item && item.length > 0) { //有子节点
                    subMenuData[menuId] = item;
                    //中序遍历，依次访问子节点数据。
                    var nextScan = item;
                    while (true) {
                        var tempScan = [];
                        if (nextScan.length > 0) {
                            for (var i = 0; i < nextScan.length; i++) {
                                var node = nextScan[i];
                                if (node.item) {//有子节点
                                    subMenuData[node.id] = node.item;
                                    if (dhx.isArray(node.item)) {
                                        tempScan = tempScan.concat(node.item);//数组连接
                                    } else {
                                        tempScan.push(node.item);
                                    }
                                } else {
                                    subMenuData[node.id] = empty;
                                }
                            }
                            nextScan = tempScan;
                        } else {
                            break;
                        }
                    }
                }
            }
            if(!subMenuData[menuId]) {
                subMenuData[menuId] = empty;
            }
            return afterCall ? afterCall(menuId, menuData,isRefresh) : null;
        });
    } else {
        return afterCall ? afterCall(menuId, menuData,isRefresh) : null;
    }
}
/**
 * 打开批示信息
 * @param groupId
 */
function openInstructions(wdNum,obj){
	if(!wdNum)
		obj.setAttribute("disabled", "disabled");
	else
		openMenu("批示信息","/portalCommon/module/portal/instructions.jsp?isWD=true","top");
}





    /**
     * 新建一个frame节点。
     * @param menuIndex
     * @param menuData
     */
 var createFrame = function(menuId,url) {
        var iframe = document.createElement("iframe");
        iframe.name = 'tabFrame_' + menuId;
        iframe.id = 'tabFrame_' + menuId;
        iframe.style.width = "100%";
        iframe.style.height = "100%";
        iframe.frameBorder = 0;
        iframe.scrolling = "yes";
        if(dhx.env.isIE){
            window.attachEvent('onresize',function(event){
                if(iframe&&iframe.contentWindow&&iframe.contentWindow.resizeHandler){
                    iframe.contentWindow.resizeHandler();
                }
            });
        }
        iframe.src = url;
        return iframe;
    }
/**
 * 弹出一个新的Tab页，如果存在则设置当前的tab为active
 * @param menuId
 * @param menuData
 */
var openTab = function(menuId, menuData,isRefresh) {
    var url =urlDeal(menuData.menuUrl,menuData.userAttrList,menuId);

   
   /**
    *  var refreshTab=function(){
        try {
            var activeId = mainTabbar.getActiveTab().replace(/menu/, "");
        } catch(e) {
            alert("打开菜单出错，请确认菜单配置是否正确！");
            return;
        }
        document.getElementById("tabFrame" + activeId).src=url;
    }
    **/
        
        //开始
          document.body.innerHTML="";

	 if (!indexLayout) { //首页    
	          indexLayout = new dhtmlXLayoutObject(document.body, "1C");
	          //主页导航栏layout
			  indexLayout.cells("a").setHeight(500);
			  indexLayout.cells("a").hideHeader(true);
			  CollectGarbage();
			  indexLayout.cells("a").attachObject(createFrame(menuId,url));
		      indexLayout.hideConcentrate();
		      indexLayout.hideSpliter();//移除分界边框。
          
      }else{
    	     //当前打开得Tab个数
		      tabCount = 0;
    	     indexLayout=null;
    	     indexLayout = new dhtmlXLayoutObject(document.body, "1C");
    	    if(menuData.tabid ==null || menuData.tabid ==""){//和首页类似的页面
    	       //主页导航栏layout
    		  indexLayout.cells("a").setHeight(500);
			  indexLayout.cells("a").hideHeader(true);
			  CollectGarbage();
			  indexLayout.cells("a").attachObject(createFrame(menuId,url));
		      indexLayout.hideConcentrate();
		      indexLayout.hideSpliter();//移除分界边框。
    	    }else{
    	    	        
    	    	      //控制菜单
    	    	         tabLayout=null;
						 tabLayout  =  new dhtmlXLayoutObject(document.body, "2U");
						 //tabLayout.attachEvent("onExpand"); 
						 //tabLayout.attachEvent("onCollapse"); 
						 //tabLayout.cells("a").attachURL("common1/top.html"); 
						 
	                     tabLayout.cells("a").setWidth(205);
	                     tabLayout.cells("a").setText(menuData.menuName);
	                     tabLayout.cells("a").fixSize(false, false);
	                    //alert(menuId);
	                     if(menuId==84003 || menuId==119006 || menuId==119007 ){
	                        tabLayout.cells("a").expand();
	                     }else{
	                       tabLayout.cells("a").collapse();//默认收缩
	                     }
	                    
	                    // tabLayout.hideConcentrate();
	                    // tabLayout.cells("a").hideHeader(true); 
	                     tabLayout.hideSpliter();//移除分界边框。
						
						 var tree = tabLayout.cells("a").attachTree();
	                     	tree.setSkin('dhx_skyblue');
						    tree.setImagePath(dhtmlx.image_path + "csh_books" + "/");
						    tree.setIconsPath(dhtmlx.image_path + "csh_books" + "/");
		                    var treeData = {id:0,item:subMenuData[menuId]};
		                    tree.loadJSONObject(treeData);
		                       
		                      //右边
	                          tabLayout.cells("b").hideHeader();
						      mainTabbar =tabLayout.cells("b").attachTabbar();
					         //Tab设置
						      mainTabbar.setSkin("dhx_skyblue");
                              mainTabbar.setImagePath(dhtmlx.image_path);
						      mainTabbar.enableTabCloseButton(true);
						      mainTabbar.enableAutoReSize(true);
						      mainTabbar.enableScroll(true);
					        //每个tab加一个iframe
					          mainTabbar.attachEvent("onTabClose", function(id) {
					            if (tabCount == 1) {
					                return false;
					            }
					            var temp = mainTabbar.cells(id);
					            if (temp.getElementsByTagName('iframe')) {
					                var iframes = temp.getElementsByTagName('iframe');
					                for (var i = 0; i < iframes.length; i++) {
					                    try {
					                        var win = iframes[i].contentWindow;
					                        //如果子窗口中有卸载事件，执行
					                        if (win.unloadOnClose) {
					                            if (win.unloadOnClose() == false) {
					                                return false;
					                            }
					                        }
					                        //进行iframe销毁，内存回收
					                        try {
					                            iframes[i].contentWindow.document.write('');
					                            iframes[i].contentWindow.document.close();
					                            iframes[i].contentWindow.document.clear();
					                        } catch (e) {
					                        }
					                        dhx.env.isIE && iframes[i].removeNode(true);
					                    } catch (e) {
					                    }
					
					                }
					            }
					            menuTabs[id] = null;
					            delete menuTabs[id];
					            tabCount--;
					            return true;
					        });
						      
					       var tabId = "menu_" + menuData.tabid;
						   var len = menuData.reportname.replace(/[^\x00-\xff]/g, "**").length ;  
						  // alert(len);
							//6个字以内宽度为100，每增加一个字增加7px宽度
			                var width=len*8.5<100?100: len*8.6;
			                mainTabbar.addTab(tabId, menuData.reportname, width);
			                mainTabbar.setContent(tabId, createFrame(menuData.tabid,url));
                            mainTabbar.setTabActive(tabId);
                            menuTabs[tabId] = true;
			                tabCount++;
			                
			                //添加事件处理
		                    tree.attachEvent("onClick", function(id) {
		                         tree.openItem(id);
		                        //menuClick(id, tree.getUserData(id, "menuData"));
		                         //记录访问日志。
//							     if (id != undefined || id != null){
//							        dwrCaller.executeAction("menuVisitLog", [
//							            {menuId:id}
//							        ]);
//							     }
							   //  alert(id);
							  // tabLayout.cells("a").collapse();//收缩
							   if(allMenuData[id].children=="0"){//没有子结点的判断
		                             var tabId = "menu_" +id
		                         	//print_obj(allMenuData[id]);
		                             var tree_url=urlDeal(allMenuData[id].menuUrl,allMenuData[id].userAttrList,id);
		                              if (!menuTabs[tabId]) {//打开重复的报表，不重直接加载，不用打开。
		                            	    if (tabCount <= maxTabCont) {
						                        var len = allMenuData[id].menuName.replace(/[^\x00-\xff]/g, "**").length;
						                        //alert("2:"+ len);
					                            //6个字以内宽度为100，每增加一个字增加7px宽度
					                            var width=len*8.5<100?100: len*8.6;
							                    mainTabbar.addTab(tabId, allMenuData[id].menuName, width);
							                    mainTabbar.setContent(tabId, createFrame(id,tree_url));
							                    mainTabbar.setTabActive(tabId);
							                    menuTabs[tabId] = true;
		                            	        tabCount++;
//		                            	        if (tabCount > maxTabCont){
//		                            	        	mainTabbar.addTab("menu_12", "12312313", len);
//		                            	        }
		                            	     } else {
							                     alert("TAB已经超过当前最大数目限制，请先关闭！")
							                 }
		                                     
		                                 }else{
                    		               mainTabbar.setTabActive(tabId);
                    	                }
		                         }
		                    });
	                     
    	    	    indexLayout.cells("a").attachObject(tabLayout);
                    indexLayout.hideConcentrate();
                    indexLayout.hideSpliter();
    	    }
       }
}
/**
 *
 * @param tabTd
 */
var closeTab=function(menuId){
    if (menuTabs[menuId]) {
        mainTabbar.goToPrevTab();
        mainTabbar.removeTab(menuId);
        menuTabs[menuId]=null;
        delete menuTabs[menuId];
    }
}


/**
 * 根据菜单配置打开一个新窗口
 * @param menuId
 * @param menuData
 */
var openWindow = function(menuId, menuData) {
    var winName = "newWindow" + menuId;
    var winUrl = urlDeal(menuData.menuUrl,menuData.userAttrList,menuId);
    var config = "toolbar=yes,resizable=yes,";
    var browserState = parseInt(menuData.navState);
    if ((1 & browserState) == 1) {//可以最大化
        config += ",fullScreen=yes";
    } else {
        config += "height=300, width=600,";//设置默认高宽度
    }
    if ((2 & browserState) == 2) {//可以有滚动
        config += "scrollbars=yes,";
    } else {
        config += "scrollbars=no,";
    }
    if ((4 & browserState) == 4) {//可以有菜单栏
        config += "menubar=yes,";
    } else {
        config += "menubar=no,";
    }
    if ((8 & browserState) == 8) {//可以有状态栏
        config += "status=yes,";
    } else {
        config += "status=no,";
    }
    if ((16 & browserState) == 16) {//可以有链接栏
        config += "location=yes";
    } else {
        config += "location=no";
    }
    var child = window.open(winUrl, winName, config);
    //child.document.location.href = winUrl;
}
var urlDeal=function(winUrl,userAttrList,menuId){
    if(winUrl){
        //为其添加扩展参数menuId
        if(menuId&& winUrl.toLowerCase().indexOf("http://") == -1){
            winUrl = Tools.trim(winUrl) + (winUrl.indexOf("?") == -1 ? "?" : "&") + "menuId=" + menuId;
        }
        if(userAttrList){
            winUrl = Tools.trim(winUrl) + (winUrl.indexOf("?") == -1 ? "?" : "&") + userAttrList;
        }
        return window.parent.urlDeal(urlEncode(winUrl));
    }
}
/**
 * 全局初始化方法，调用时机在于弹出子窗口或者子ifame加载时候调用，用于为其做些初始化动作。
 * @param frameWin 子窗体或者子frame window对象
 * @param menuId
 */
var globalInit = function(frameWin, menuId) {
    //子窗体加载成功后为子窗体设置一个获取菜单按钮权限列表的函数getRoleButtons
    frameWin.getRoleButtons = function(id) {
        if (this.opener && this.opener.getRoleButtons)
            return this.opener.getRoleButtons(id ? id : menuId);
        else if (this.parent && this.parent.getRoleButtons) {
            return this.parent.getRoleButtons(id ? id : menuId)
        }
    };
    //子窗体加载成功后设置一个访问菜单的函数 openMenu.
    /**
     * 打开一个菜单，前三个为必须。
     * @param menuName 菜单名称
     * @param menuUrl 菜单Url
     * @param target 菜单目标窗口，目前支持right,top,target三种
     * @param menuId 非必选，如果有菜单ID，则会认为此菜单在数据库中已存在，需要记录菜单访问日志
     */
    frameWin.openMenu=function(menuName,menuUrl,target,menuId,isRefresh){
        //打开菜单操作。
        if (this.opener && this.opener.menuClick)
            return this.opener.openMenu(menuName,menuUrl,target,menuId,isRefresh);
        else if (this.parent && this.parent.menuClick) {
            return this.parent.openMenu(menuName,menuUrl,target,menuId,isRefresh)
        }
    }
    /**
     * 获取session信息
     * @param attr
     * @param realTime:是否实时查询。
     */
    frameWin.getSessionAttribute=function(attr,realTime){
        return window.parent.getSessionAttribute(attr,realTime);
    };

//    PortalCommonCtrlr.getDuty(function(res){
//        $('duty_man').innerHTML = res;
//    });
}
/**
 * 打开一个菜单
 * @param menuName
 * @param menuUrl
 * @param target
 * @param menuId
 * @param isRefresh 如果菜单已经打开，是否进行刷新。
 */
var openMenu=function(menuName,menuUrl,target,menuId,isRefresh){
    if(!menuName||!menuUrl||!target){
        alert("缺少打开菜单的必选参数，不能打开一个菜单!");
        return;
    }
    var menuData=undefined;
    if(menuId==undefined||menuId==null){
        //打开一个虚拟的菜单。
        menuData={menuName:menuName,menuUrl:menuUrl,target:target};
    }
    //打开菜单操作。
    return menuClick(menuId, menuData||allMenuData[menuId],isRefresh);
}
/**
 *  过滤隐藏菜单,并缓存菜单数据
 */
var filterHiddenMenu = function(data) {
    var columnConverter = new columnNameConverter();
    //首先过滤隐藏菜单。
    var replaceData = [];
    if (data) {
        for (var i = 0; i < data.length; i++) {
            var id = data[i].MENU_ID;
            allMenuData[id] = columnConverter.convert(data[i]);
            if (allMenuData[id].isShow != 0) {
                replaceData.push(allMenuData[id]);
            }
        }
        columnConverter = null;
    }
    return replaceData;
}


function print_obj(myObject) {  
   var s = "";  
   for (var property in myObject) {  
	   s = s + "\n "+property +": " + myObject[property] ;  
	  }  
	  alert(s);  
} 
/**
 * 根据MenuID获取指定的权限。
 * @param menuId
 */
var getRoleButtons = function(menuId) {
    //获取当前的菜单信息
    var menuData = allMenuData[menuId];
    if (!menuData) {
        return Tools.EmptyList;
    }
    var pageButton = menuData.pageButton;
    if (!pageButton) {
        return Tools.EmptyList;
    }
    return pageButton.split(",");
}
var  getCurDate = function(){ 
     var d = new Date();
	 var week; 
	 switch (d.getDay()){ 
	 case 1: week="星期一"; break; 
	 case 2: week="星期二"; break; 
	 case 3: week="星期三"; break; 
	 case 4: week="星期四"; break; 
	 case 5: week="星期五"; break; 
	 case 6: week="星期六"; break; 
	 default: week="星期天"; 
	} 
	 var years =d.getYear(); 
	 var month =d.getMonth()+1;
	 var days  =d.getDate(); 
	 var ndate = years+"年"+month+"月"+days+"日 "+" "+week;
	 return ndate;
}

var closeTabIframe=function(){
	menuTabs={};
     //for (var menu in  menuTabs) {
    	//  closeTab(menu);
     //}
}

var openTreeTab=function (menuId , menuName, url,isRefresh){
   if (menuId != undefined || menuId != null){
        dwrCaller.executeAction("menuVisitLog", [
            {menuId:menuId}
        ]);
     }
       var tabId = "menu_" +menuId
       
       if (!menuTabs[tabId]) {//打开重复的报表，不重直接加载，不用打开。
    	    if (tabCount <= maxTabCont) {
                var len = menuName.replace(/[^\x00-\xff]/g, "**").length;
                //6个字以内宽度为100，每增加一个字增加7px宽度
                //alert("3:"+ len);
                var width=len*8.5<100?100: len*8.6;
                mainTabbar.addTab(tabId, menuName, width);
                mainTabbar.setContent(tabId, createFrame(menuId,url));
                mainTabbar.setTabActive(tabId);
                menuTabs[tabId] = true;
    	        tabCount++;
    	     } else {
                 alert("TAB已经超过当前最大数目限制，请先关闭！")
             }
             
         }else{
            mainTabbar.setTabActive(tabId);
       }
   
}
