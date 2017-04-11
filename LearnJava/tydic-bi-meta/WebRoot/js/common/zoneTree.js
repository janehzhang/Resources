/**
 *  地区树 start
 */
var loadZoneTreeChkBox=function(selectZone,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    //selectZone=selectZone|| global.constant.defaultRoot;
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;
	
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 400px;width: 350px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white;",id:"_zoneDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height:400px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;",id:"treeDiv"
    });
    div.appendChild(treeDiv);
    //创建buttondiv
//    var butDiv=dhx.html.create("div",{
//        style:"position:relative;border: 1px #eee solid;border-bottom:0px;height:30px;overflow:auto;padding-top:0xp;margin-top:6xp" +
//              "z-index:1000;padding-left:55px"
//    })
//    div.appendChild(butDiv);

    //移动节点位置至指定节点下。
    //var target=form.getInput("zone");
    var target=   document.getElementById("zone");
    var targetId= document.getElementById("zoneCode");
    target.readOnly=true;
    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubZoneCode);
        //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
    	target.value = tree.getItemText(nodeId);
        targetId.value=nodeId;
    	
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
            targetId.value=tree.getSelectedItemId();
        }
        //为div添加事件
        Tools.addEvent(target,"focus",function(){
            div.style.width = 350+'px';
            div.style.height = 400+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        Tools.addEvent(target,"click",function(){
            div.style.width = 350+'px';
            div.style.height = 400+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        
    });
    div.style.display="none";
}
/**
 *  渠道服务报表三级分类树形结构 start
 */
var loadChannelSerTree=function(selectServ,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    //selectServ=selectServ|| global.constant.defaultRoot;
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;
	
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 400px;width: 350px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white;",id:"_servDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height:400px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;",id:"treeDiv"
    });
    div.appendChild(treeDiv);
    //创建buttondiv
//    var butDiv=dhx.html.create("div",{
//        style:"position:relative;border: 1px #eee solid;border-bottom:0px;height:30px;overflow:auto;padding-top:0xp;margin-top:6xp" +
//              "z-index:1000;padding-left:55px"
//    })
//    div.appendChild(butDiv);

    //移动节点位置至指定节点下。
    //var target=form.getInput("serv");
    var target=   document.getElementById("serv");
    var targetId= document.getElementById("servId");
    target.readOnly=true;
    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubChannelServId);
        //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
    	target.value = tree.getItemText(nodeId);
        targetId.value=nodeId;
    	
        //关闭树
        div.style.display="none";
    });

     dwrCaller.executeAction("loadChannelSerTree",beginId,selectServ,function(data){
        tree.loadJSONObject(data);
        globServTree = tree;
        if(selectServ){
            tree.selectItem(selectServ); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
            targetId.value=tree.getSelectedItemId();
        }
        //为div添加事件
        Tools.addEvent(target,"focus",function(){
            div.style.width = 350+'px';
            div.style.height = 400+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        Tools.addEvent(target,"click",function(){
            div.style.width = 350+'px';
            div.style.height = 400+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        
    });
    div.style.display="none";
}
/**
 *  地区树 end
 */
//满意度测评总体情况日报
var loadZoneTreeChkBox2=function(selectZone,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    //selectZone=selectZone|| global.constant.defaultRoot;
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;
	
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 400px;width: 350px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white;",id:"_zoneDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height:400px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;"
    });
    div.appendChild(treeDiv);
    //创建buttondiv
//    var butDiv=dhx.html.create("div",{
//        style:"position:relative;border: 1px #eee solid;border-bottom:0px;height:30px;overflow:auto;padding-top:0xp;margin-top:6xp" +
//              "z-index:1000;padding-left:55px"
//    })
//    div.appendChild(butDiv);

    //移动节点位置至指定节点下。
    //var target=form.getInput("zone");
    var target=   document.getElementById("zone2");
    var targetId= document.getElementById("zoneCode2");
    target.readOnly=true;
    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubZoneCode);
        //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
    	target.value = tree.getItemText(nodeId);
        targetId.value=nodeId;
    	
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
            targetId.value=tree.getSelectedItemId();
        }
        //为div添加事件
        Tools.addEvent(target,"focus",function(){
            div.style.width = 350+'px';
            div.style.height = 400+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        Tools.addEvent(target,"click",function(){
            div.style.width = 350+'px';
            div.style.height = 400+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        
    });
    div.style.display="none";
}
//满意度测评总体情况月报
var loadZoneTreeChkBox1=function(selectZone,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    //selectZone=selectZone|| global.constant.defaultRoot;
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;
	
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 400px;width: 350px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white;",id:"_zoneDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height:400px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;"
    });
    div.appendChild(treeDiv);
    //创建buttondiv
//    var butDiv=dhx.html.create("div",{
//        style:"position:relative;border: 1px #eee solid;border-bottom:0px;height:30px;overflow:auto;padding-top:0xp;margin-top:6xp" +
//              "z-index:1000;padding-left:55px"
//    })
//    div.appendChild(butDiv);

    //移动节点位置至指定节点下。
    //var target=form.getInput("zone");
    var target=   document.getElementById("zone1");
    var targetId= document.getElementById("zoneCode1");
    target.readOnly=true;
    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubZoneCode);
        //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
    	target.value = tree.getItemText(nodeId);
        targetId.value=nodeId;
    	
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
            targetId.value=tree.getSelectedItemId();
        }
        //为div添加事件
        Tools.addEvent(target,"focus",function(){
            div.style.width = 350+'px';
            div.style.height = 400+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        Tools.addEvent(target,"click",function(){
            div.style.width = 350+'px';
            div.style.height = 400+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        
    });
    div.style.display="none";
}
var loadZoneTreeChkBox_map=function(selectZone,form){
   //加载部门树数据。加载用户所在部门及其子部门。
    //selectZone=selectZone|| global.constant.defaultRoot;
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;
	
    //创建Div层
    var div=$("_zoneDiv");
    var treeDiv=$("treeDiv");
    document.body.appendChild(div);
  //  div.remoteChild(treeDiv)
    //创建tree Div层
  //  var treeDiv=dhx.html.create("div",{
  //      style:"position:relative;height:400px;overflow: auto;padding: 0;margin: 0;" +
  //            "z-index:1000;",id:"treeDiv"
  //  });
    div.appendChild(treeDiv);
    //创建buttondiv
//    var butDiv=dhx.html.create("div",{
//        style:"position:relative;border: 1px #eee solid;border-bottom:0px;height:30px;overflow:auto;padding-top:0xp;margin-top:6xp" +
//              "z-index:1000;padding-left:55px"
//    })
//    div.appendChild(butDiv);

    //移动节点位置至指定节点下。
    //var target=form.getInput("zone");
    var target=   document.getElementById("zone");
    var targetId= document.getElementById("zoneCode");
    target.readOnly=true;
    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubZoneCode);
        //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
    	target.value = tree.getItemText(nodeId);
        targetId.value=nodeId;
    	
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
            targetId.value=tree.getSelectedItemId();
        }
        //为div添加事件
        Tools.addEvent(target,"focus",function(){
            div.style.width = 350+'px';
            div.style.height = 400+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        Tools.addEvent(target,"click",function(){
            div.style.width = 350+'px';
            div.style.height = 400+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        
    });
    div.style.display="none";
}