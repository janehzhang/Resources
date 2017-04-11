//菜单数据对象
var menuArray = null;
var openedTab = {};
var tabbar = null;
var tabCount = 0;

function openTab(i){
	if(tabbar&&menuArray){
		var menuData = menuArray[i];
		if(menuData.TARGET=='blank'){
			openWindow(menuData.MENU_ID,menuData);
		}else if(!openedTab[menuData.MENU_ID]){
			tabbar.addTab(menuData.MENU_ID,menuData.MENU_NAME,'*');
			openedTab[menuData.MENU_ID] = menuData;
			LoginAction.queryAllSubMenu(parseInt(menuData.MENU_ID),function(subMenu){
				if(subMenu&&subMenu.length>0){
					var layout = tabbar.cells(menuData.MENU_ID).attachLayout("2U","dhx_skyblue");
					var bCell = layout.cells('b');
					bCell.hideHeader();
					bCell.attachURL(rootPath+"/"+menuData.MENU_URL);
					
					var aCell = layout.cells('a');
					aCell.setText(menuData.MENU_NAME);
					aCell.setWidth(200);
					aCell.fixSize(true,false);
					var menuTree = aCell.attachTree();
					menuTree.setImagePath(dhtmlx.image_path + "csh_dhx_skyblue/");
					menuTree.enableIEImageFix(true);
					var jsArray = new Array();
					for(var i=0,len=subMenu.length;i<len;i++){
						var item = subMenu[i];
						if(item.IS_SHOW==0){
							
						}else if(item.PARENT_ID==menuData.MENU_ID){
							jsArray.push([item.MENU_ID,'0',item.MENU_NAME]);
						}else{
							jsArray.push([item.MENU_ID,item.PARENT_ID,item.MENU_NAME]);
						}
					}
					menuTree.loadJSArray(jsArray);
					jsArray.length=0;
					for(var i=0,len=subMenu.length;i<len;i++){
						var item = subMenu[i];
						menuTree.setUserData(item.MENU_ID,"target",item.TARGET);
						menuTree.setUserData(item.MENU_ID,"url",item.MENU_URL);
					}
					
					menuTree.attachEvent('onClick',function(id){
						var url = menuTree.getUserData(id,"url");
						url=url+"?menuId="+id;
						if(url&&url.toLowerCase().indexOf('http://')==-1){
							bCell.attachURL(rootPath+"/"+url);
						}else if(url&&url.toLowerCase().indexOf('http://')!=-1){
							bCell.attachURL(url);
						}else{
							
						}
			            return true;
			        });
					
				}else{
					tabbar.setContentHref(menuData.MENU_ID,rootPath+"/"+menuData.MENU_URL);
				}
			});
			tabCount++;
		}
		tabbar.setTabActive(menuData.MENU_ID);
	}
}

function openMenu(menuName,menuUrl,target,menuId,isRefresh){
	menuId = menuId?menuId:menuName;
	var menuData = {};
    //打开一个虚拟的菜单。
    menuName && (menuData.menuName = menuName);
    menuUrl && (menuData.menuUrl = menuUrl);
    target && (menuData.target = target);
    menuId && (menuData.menuId = menuId);
    menuData.parentId = tabbar.getActiveTab();
    menuData.isRefresh = isRefresh;
    if(!menuName||!menuUrl||!target){
        alert("缺少打开菜单的必选参数，不能打开一个菜单!");
    }else if(target=='blank'){
    	openWindow(menuId,menuData)
    }else if(target='top'){
    	if(!openedTab[menuId]){
    		tabbar.addTab(menuId,menuData.menuName,'*');
			openedTab[menuId] = menuData;
			tabbar.setContentHref(menuId,rootPath+"/"+menuUrl);
			tabCount++;
    	}
    	tabbar.setTabActive(menuId);
    }else{
    	
    }
}

function addTabRefreshOnActive(tabId){
	var tab = tabbar.cells(tabId);
	tab.setAttribute('isRefresh',true);
}

var _sessionInfo = {};
var getSessionAttribute=function(attr,realTime){
    if(realTime||!_sessionInfo[attr]){
        SessionManager.getAttribute(attr,{async:false,callback:function(data){
            _sessionInfo[attr]=data;
        }
        })
    }
    return _sessionInfo[attr];
};

function openWindow(menuId, menuData){
    var winName = "newWindow" + menuId;
    var winUrl = urlDeal(menuData.MENU_URL, menuData.USER_ATTR_LIST, menuId);
    var config = "toolbar=yes,resizable=yes,";
    var browserState = parseInt(menuData.navState);
    if((1 & browserState) == 1){//可以最大化
        config += ",fullScreen=yes";
    } else{
        config += "height=300, width=600,";//设置默认高宽度
    }
    if((2 & browserState) == 2){//可以有滚动
        config += "scrollbars=yes,";
    } else{
        config += "scrollbars=no,";
    }
    if((4 & browserState) == 4){//可以有菜单栏
        config += "menubar=yes,";
    } else{
        config += "menubar=no,";
    }
    if((8 & browserState) == 8){//可以有状态栏
        config += "status=yes,";
    } else{
        config += "status=no,";
    }
    if((16 & browserState) == 16){//可以有链接栏
        config += "location=yes";
    } else{
        config += "location=no";
    }
    var child = window.open('about:blank', winName, config);
    child.document.location.href = winUrl;
}

function logout(){
    if(confirm("您确定要退出系统吗?")){
        //发送退出消息，清空session，并重定向页面
        LoginAction.logout(function(data){
	        window.location=getBasePath();
	    });
    }
};

function systemChange(){
	LoginAction.changeSystem($('systemSelect').value,function(){
		window.location="home.jsp";
	});
}

dhx.ready(
	function(){
		tabbar = new dhtmlXTabBar("tabbar", "top"); 
		tabbar.enableTabCloseButton(true);
        tabbar.enableAutoReSize(true);
//        tabbar.enableScroll(false);
        tabbar.setHrefMode('iframes');
        tabbar.attachEvent("onSelect", function(id,last_id){
			var tab = tabbar.cells(id);      
			if(tab.getAttribute('isRefresh')){
				alert('刷新');
			}
            return true;
        });
        tabbar.attachEvent("onTabClose", function(id){
        	  if(tabCount==1){
        		  return false;
        	  }else{
        		  delete openedTab[id];	
        		  tabCount--;
              	  return true;
        	  }
        });
        LoginAction.queryRootMenu(systemId,function(data){
        	menuArray = data;
	        var html = '';
        	for(var i=0,length=data.length;i<length;i++){
        		var menuData = data[i];
        		var menuIconUrl = rootPath+menuData.ICON_URL;
        		menuIconUrl = menuIconUrl.replace("//","/");
        		html+="<li>";
        		html+='<a href="javascript:openTab('+i+');">';
				html+='<img style="border: currentColor; width: 32px;" align="middle" src="'+menuIconUrl+'">'+menuData.MENU_NAME;
				html+='</a>';
				html+='</li>';
        	}
        	$('navUl').innerHTML = html;
        	html = null;
        	if(data.length>0){
        		openTab(0);
        	}
        });
        MenuAction.queryMenuSystem(function(data){
	        //定义切换系统selectButton
	        if(data&&data.length>0){
	            var systemSelect = $("systemSelect");
	            for(var i=0;i<data.length;i++){
	                var option = document.createElement('option');
	                option.value = data[i].GROUP_ID;
	                option.innerHTML = data[i].GROUP_NAME;
	                systemSelect.appendChild(option);
	
	                if(data[i].groupId==systemId){
	                    option.selected = true;
	                }
	            }
	        }
	        data.length=0;
	    });
	}
);