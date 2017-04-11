/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        index.js
 *Description：
 *       主页JS文件，实现主页功能
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        张伟
 *Finished：
 *       2011-09-11-9-23
 *Modified By：
 *
 * Modified Date:
 *
 * Modified Reasons:	

 ********************************************************/

/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user=getSessionAttribute("user");
var dwrCaller=new biDwrCaller();



//当前系统的主页Path
var base = getBasePath();
//根级菜单数据
var rootMenuData=null;
var frameLoadSucess=false;
//添加根菜单访问Action
dwrCaller.addAutoAction("queryRootMenu","LoginAction.queryRootMenu",getSystemId());
  
//dwrCaller.addDataConverter("queryRootMenu",new columnNameConverter());
var indexInit=function(){
	 $("welcome").innerHTML="欢迎您，"+user.userNamecn+'&nbsp;&nbsp;'+getCurDate();
    var indexLayout = new dhtmlXLayoutObject(document.body, "2E");
    //主页导航栏layout
    indexLayout.cells("a").hideHeader(true);
    indexLayout.cells("a").setHeight(80);
    indexLayout.cells("a").fixSize(true,true);

    //添加导航栏设置。
    indexLayout.cells("a").attachObject($("north"));

    //主页菜单栏layout
//    indexLayout.cells("b").fixSize(true,true);
    //菜单栏设置
//    indexLayout.cells("b").attachObject(dhx.html.create("div",{id:"menuTreeBox",style:"width:200;height:100%"}));

    //内容layout
   indexLayout.cells("b").fixSize(true,true);
   indexLayout.cells("b").hideHeader(true);
    //添加一个内容iframe
    var iframe = document.createElement("iframe");
    iframe.name="main";
    iframe.id= "main" ;
    iframe.style.width = "100%";
    iframe.style.height = "100%";
    iframe.frameBorder=0;
    iframe.scrolling="no";
    iframe.src=urlEncode("main.jsp");
    if (iframe.attachEvent){
        iframe.attachEvent("onload", function(){
            frameLoadSucess=true;//子窗体加载成功
        });
    } else {
        iframe.onload = function(){
            frameLoadSucess=true;
        };
    }
    indexLayout.cells("b").attachObject(iframe);
    indexLayout.hideConcentrate();

    //执行Action 访问根节点
    dwrCaller.executeAction("queryRootMenu",function(data){
        //交由子窗口处理进行隐藏菜单筛选操作。
        if(frameLoadSucess){
            data= window.frames["main"].window["filterHiddenMenu"](data); //过滤隐藏菜单。
        }else{
            var arg=arguments;
            setTimeout(function(){arg.callee.call(this,data)},10);
            return;
        }
        if(data){
            data=dhx.isArray(data)?data:[data];
            rootMenuData=data;
            var str="";
            for(var i=0;i<data.length;i++){
                str+='<li><a  name =\"a\"  href="javascript:menuChange('+i+');"  style=\"color:#FFFFFF\">'+data[i].menuName+'</a></li>';
            }
            $("navUl").innerHTML= str;
           // alert(str);
            delete str;
            //加载第一个菜单数据
            menuChange(0);
        }
        navInit();
    });
    //导航栏工具条设置
    
   
    //设置批示未读条数
    //setPsIsRead();

}

 //设置批示未读条数
var setPsIsRead=function(){
	PortalInstructionAction.getPsSendList({
        	async:false,
        	callback:function(data){
        		$("red").innerHTML=data.length;
        	}
     });
}
/**
 * 注册logout Action
 */
dwrCaller.addAutoAction("logout","LoginAction.logout",function(){
    window.location=getBasePath();
});


var logout=function(){
  dhx.confirm("您确定要退出系统吗?",function(r){
        if(r){
            //发送退出消息，清空session，并重定向页面
            dwrCaller.executeAction("logout");
          }
    });
	
	/** if(confirm("您确定要退出系统吗?")){
		 dwrCaller.executeAction("logout");
	 }**/
}

/**********************************************************/

var base = getBasePath();

var openMenu=function(menuName,menuUrl,target,menuId,isRefresh){
	
    var param="height=500px,width=1200px,top=200,left=100,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no";
   window.open (base+menuUrl,'newwindow',param);
}


/**
 *管理本地用户
 * @param groupId
 */
function manageLocalUser(groupId){
     openMenu("管理本地用户","/meta/module/mag/user/localUser.jsp?groupId="+groupId,"top");
}

/**
 *打开用户申请
 * @param groupId
 */
function openApplyUser(){		
     openMenu("账号申请","/meta/public/userRegister.jsp","top");
}


/**
 *打开省公司审核
 * @param groupId
 */
function openProvAduit(){
     openMenu("省公司审核","/meta/public/provicialAudit.jsp","top");
}
/**
 *打开分公司审核
 * @param groupId
 */
function openPartAduit(){
     openMenu("分公司审核","/meta/public/partAudit.jsp","top");
}


var loadOperMan = function(){
	var file= "meta/public/upload/serviceProblem/广东电信客户服务分析系统用户操作手册.doc"
	document.forms[0].target="hiddenFrame";
 	var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ file;
	window.open(url,"hiddenFrame","");
}

var loadApplyOperMan = function(){
	var file= "meta/public/upload/serviceProblem/账号申请流程操作指引.docx"
	document.forms[0].target="hiddenFrame";
 	var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ file;
	window.open(url,"hiddenFrame","");
}

var loadTranMan = function(){
	var file= "meta/public/upload/serviceProblem/客户服务分析系统培训材料v3.ppt"
	document.forms[0].target="hiddenFrame";
 	var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ file;
	window.open(url,"hiddenFrame","");
}




/**
 *打开用户申请
 * @param groupId
 */
function openApplyUser(){
		
     openMenu("账号申请","/meta/public/userRegister.jsp","top");
}

/**
 *打开应用排名
 * @param groupId
 */
function openAppRank(groupId){
	  openMenu("用户访问","/meta/module/mag/log/userVisitNum.jsp?groupId="+groupId,"top");
}

/**
 *打开登录排名
 * @param groupId
 */
function openVisitRank(groupId){
     openMenu("登录次数","/meta/module/mag/log/loginLog.jsp?groupId="+groupId,"top");
}


/**
 *打开应用访问
 * @param groupId
 */
function openMenuRank(groupId){
     openMenu("应用访问","/meta/module/mag/log/userVisitNumRank.jsp?groupId="+groupId,"top");
}


//var loadTranMan = function(){
//	var file= "meta/public/upload/serviceProblem/客户服务分析系统培训材料v3.ppt"
//	document.forms[0].target="hiddenFrame";
// 	var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ file;
//	window.open(url,"hiddenFrame","");
//}
//
//var base = getBasePath();
//
//var openMenu=function(menuName,menuUrl,target,menuId,isRefresh){
//	
//    var param="height=500px,width=1200px,top=200,left=100,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no";
//   window.open (base+menuUrl,'newwindow',param);
//}

function loadZZsys()
{
	    document.forms[0].action="http://132.121.165.45:8085/meta/meta/login_sso.jsp";
		document.forms[0].method="post";
		document.forms[0].target="_blank";
		document.forms[0].submit();
	    //var url="http://132.121.165.45:8085/meta/meta/public/userSynLogin.jsp?userNamecn=颜海东&url=home/module/selfService/selfIndex2.jsp";
	   //window.open(url,"blank");
}
  
//add yanhd 手机版下载
var uploadAndroid = function(){
	//window.open("../../resource/other/khfwfx_Android.apk","blank");
	var attchmetPath="portal/resource/other/khfwfx_Android.apk";
	document.forms[0].target="hiddenFrame";
 	var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ attchmetPath;
	window.open(url,"hiddenFrame","");
}
//add yanhd 手机版下载
var uploadIOS = function(){
	//window.open("../../resource/other/khfwfx_Android.apk","blank");
	var attchmetPath="portal/resource/other/khfwfx_IOS.ipa";
	document.forms[0].target="hiddenFrame";
 	var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ attchmetPath;
	window.open(url,"hiddenFrame","");
}
var i;
var changePassword=function(){
    i=0;
    if(frameLoadSucess){
        window.frames["main"].window["openMenu"]("修改密码","/portal/module/portal/modifyPwd.jsp","top");
    }else{
        if(i++ >5){
            dhx.alert("次功能异常，请联系管理员");
            return;
        };
        setTimeout(arguments.callee,50);
    }
}

var openLogRep=function(groupId){
    i=0;
     if(frameLoadSucess){
        window.frames["main"].window["openMenu"]("访问统计","meta/module/mag/log/logreports.jsp?groupId="+groupId,"top");
    }else{
         if(i++ >5){
            dhx.alert("次功能异常，请联系管理员");
            return;
        };
        setTimeout(arguments.callee,50);
    }
}


//打开批示信息
var lookOpenPs=function(){
   window.frames["main"].window["openMenu"]("批示信息","portalCommon/module/index/psList.jsp","top");
}

dwrCaller.addAutoAction("changeSystem","LoginAction.changeSystem");
var systemChange = function(){
    window.location.href="index.jsp?systemId="+document.getElementById('systemSelect').value;
    //访问DWr，记录切换系统系统
    dwrCaller.executeAction("changeSystem");
}
/**
 * 
 * @param myObject
 * @return
 */
function dump_obj(myObject) {  
   var s = "";  
   for (var property in myObject) {  
	   s = s + "\n "+property +": " + myObject[property] ;  
	  }  
	  alert(s);  
	} 
/**
 * 切换menu
 * @param menuId
 */
var menuChange=function(menuIndex){
    var obj=document.getElementsByName("a");  
    i=0;
    var menuData=rootMenuData[menuIndex];
    for(var i=0;i<obj.length;i++){   //选中菜单变色 
    	if(menuIndex==i ){
    	 obj[i].style.background="#FFFFFF";
    	 obj[i].style.color="#002200";
    	 }else{
          obj[i].style.background="";
          obj[i].style.color="#FFFFFF";
    	 }
    }
    
    if(menuIndex==0){//如果是第一个导航菜单，默认其为tab模式
        menuData.target="top";
    }
    //交由子窗口处理进行菜单打开操作。
    if(frameLoadSucess){
        //进行iframe销毁，内存回收
        try {
        	CollectGarbage();
        } catch (e) {
        	dump_obj(e)
        }
           window.frames["main"].window["menuClick"](menuData.menuId,menuData);
    }else{
        if(i++ >5){
            dhx.alert("次功能异常，请联系管理员");
            return;
        };
        setTimeout("menuChange("+menuIndex+")",50);
    }
}

/**
 *  供外部调用的接口
 * @param {Object} menuIndex
 * @param {Object} url
 * @param {Object} tabid
 * @param {Object} reportname
 * @return {TypeName} 
 */
var menuChangeOut=function(menuIndex,url,tabid,reportname){
    var obj=document.getElementsByName("a");  
    i=0;
    var menuData=rootMenuData[menuIndex];
    menuData.menuUrl=url;
    menuData.tabid=tabid;
    menuData.reportname=reportname;
    
    for(var i=0;i<obj.length;i++){   //选中菜单变色 
    	if(menuIndex==i ){
    	 obj[i].style.background="#FFFFFF";
    	 obj[i].style.color="#002200";
    	 } else{
          obj[i].style.background="";
          obj[i].style.color="#FFFFFF";
    	 }
    }
    
    if(menuIndex==0){//如果是第一个导航菜单，默认其为tab模式
        menuData.target="top";
    }
    //交由子窗口处理进行菜单打开操作。
    if(frameLoadSucess){
	    CollectGarbage();
        window.frames["main"].window["menuClick"](menuData.menuId,menuData);
    }else{
        if(i++ >5){
            dhx.alert("次功能异常，请联系管理员");
            return;
        };
        setTimeout("menuChange("+menuIndex+")",50);
    }
}

var nav=function(c, config){
    this.config = config || {speed: 10, num: 2};
    this.container = (typeof(c)=="object") ? c : document.getElementById(c);
    this.lineHeight = this.container.offsetHeight;
    this.scrollTimeId = null;
    var _this = this;
    this.__construct = function (){
        var inner,el,href;
        inner = _this.container.childNodes[0].innerHTML;
        href = _this.container.childNodes[0].href;
        el = document.createElement("a");
//        el.innerHTML = inner;
        el.href = href;
        el.className = 'hover';
        if(!dhx.env.isIE){
            el.style.width=_this.container.offsetWidth+'px';
        }
        _this.container.appendChild(el);
        //注册事件
        _this.container.onmouseover = function (){_this.start()};
        _this.container.onmouseout  = function (){_this.end()};
        _this.container.onclick = function (){_this.toggle()};
    }();
    this.toggle = function(){
        for(var i=0;i<$("navUl").childNodes.length;i++){
            dwr.util.removeClassName($("navUl").childNodes[i],"on");
        }
        var target=_this.container;
        dwr.util.addClassName(target,'on');
        navInit();
    };
    this.start = function (){
        _this.clear();
        _this.scrollTimeId = setTimeout(function(){_this.scrollUp();}, _this.config.speed);
    };
    this.end = function (){
        _this.clear();
        _this.scrollTimeId = setTimeout(function(){_this.scrollDown();}, _this.config.speed);
    };
    this.scrollUp = function (){
        var c = _this.container;
        if(c.scrollTop >= _this.lineHeight){c.scrollTop = _this.lineHeight;return;}
        c.scrollTop += _this.config.num;
        _this.scrollTimeId = setTimeout(function(){_this.scrollUp();}, _this.config.speed);
    };
    this.scrollDown = function (){
        var c = _this.container;
        if(c.scrollTop <= 0){c.scrollTop = 0;return;}
        c.scrollTop -= _this.config.num;
        _this.scrollTimeId = setTimeout(function(){_this.scrollDown();}, _this.config.speed);
    };
    this.clear = function (){clearTimeout(_this.scrollTimeId)};
}
var navInit=function(){
    for(var i=0;i<$("navUl").childNodes.length;i++){
        if($("navUl").childNodes[i].nodeName.toLowerCase()!="#text"){
            new nav($("navUl").childNodes[i],{speed:10,num:4});
        }
    }
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
	 //var years =d.getYear(); 
	 var years =d.getFullYear(); 
	 var month =d.getMonth()+1;
	 var days  =d.getDate(); 
	 var ndate = years+"年"+month+"月"+days+"日 "+" "+week;
	 return ndate;
}
///**
//**弹出菜单窗口
//**/
//var allMenuData = [];
//
//var dhxWindow = new dhtmlXWindows();
//
//我的收藏夹菜单
//dwrCaller.addAutoAction("queryFavoriteMenu","ChannelIndexAction.queryFavoriteMenu","116003",user.userId);
//  
///**
// * Menu树Data转换器定义
// */
//var menuConvertConfig = {
//    idColumn:"menuId",pidColumn:"parentId",
//    textColumn:"menuName",
//    isDycload:false ,
//    /**
//     * 实现 userData，将一些数据作为其附加属性
//     * @param rowIndex
//     * @param rowData
//     * @return
//     */
//    userData:function(rowIndex,rowData){
//        return {menuData:rowData};
//    },
//    isOpen:function(){
//        return false;
//    },
//    afterCoverted:function(data){
//        if(data){
//            for(var i = 0; i < data.length; i++){
//                data[i].open = data[i].items ? true : false;
//            }
//        }
//        return data;
//    },
//    compare : function(data1, data2){
//        if(data1.userdata[0]["content"].orderId == undefined
//                || data1.userdata[0]["content"].orderId == null){
//            return false;
//        }
//        if(data2.userdata[0]["content"].orderId == undefined
//                || data2.userdata[0]["content"].orderId == null){
//            return false;
//        }
//        return data1.userdata[0]["content"].orderId <= data2.userdata[0]["content"].orderId
//    }
//}
//var menuTreeDataConverter=new dhtmxTreeDataConverter(menuConvertConfig);
//dwrCaller.addDataConverter("queryFavoriteMenu",menuTreeDataConverter);
//
//
//
//var showMyFavorite = function(){
//    //loadSubdMenuData(116003);
//    dhxWindow.setSkin('dhx_skyblue');
//    if(!dhxWindow.isWindow("addWindow")){
//      dhxWindow.createWindow("addWindow", 1024, 0, 0, 0);
//      var addWindow = dhxWindow.window("addWindow");
//    
//    addWindow.denyResize();//拒绝调整大小
//    addWindow.denyMove();//禁止移动
//    addWindow.setModal(false);//表示背影可以编辑
//    addWindow.button("minmax1").hide();
//    addWindow.button("stick").hide();
//    addWindow.button("sticked").hide();
//    addWindow.setText("我的收藏夹");
//    addWindow.keepInViewport(true);
//    addWindow.setDimension(200, 500); 
//    addWindow.show();
//    
//     var refMenuLayout = new dhtmlXLayoutObject(addWindow,"2E");
//     refMenuLayout.cells("a").setHeight(20);
//     refMenuLayout.cells("a").hideHeader();
//     refMenuLayout.cells("a").fixSize(true, true);
//     var buttonForm = refMenuLayout.cells("a").attachForm([
//        {type:"settings",position: "label-left"},
//        {type:"button",value:"添加菜单",name:"execute"},
//        {type:"newcolumn"},
//        {type:"button",value:"删除菜单",name:"close"}
//    ]);
//     refMenuLayout.cells("b").hideHeader();
//     refMenuLayout.cells("b").fixSize(true, true);
//     
//     var tree = refMenuLayout.cells("b").attachTree();
//     tree.setSkin('dhx_skyblue');
//	 tree.setImagePath(dhtmlx.image_path + "csh_books" + "/");
//     tree.setDataMode("json");
//	 var loadTreeData=function(){
//       var childs = tree.getSubItems(0);
//         if(childs){
//                var childIds = (childs + "").split(",");
//                for(var i = 0; i < childIds.length; i++){
//                    tree.deleteItem(childIds[i]);
//                }
//            }
//         
//    }
//    tree.loadJSON(dwrCaller.queryFavoriteMenu);
//	 //loadTreeData();
//	 
//	 
//添加事件处理
//	 tree.attachEvent("onClick", function(id) {
//	  tree.openItem(id);
//       parent.openTreeTab(id,"absd", base+'/'+id, 'top');
// 	 
//    });	 
//    
//    //按钮事件 
//   buttonForm.attachEvent("onButtonClick",function(id){
//    	alert("button");
//    });   
//        
//        
//        
//    } 
//};

//window.onload=indexInit;
dhx.ready(indexInit);