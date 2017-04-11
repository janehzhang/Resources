/**
 *  渠道类型 start
 */
var loadChannelTypeTree=function(selectChannelType,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    //selectZone=selectZone|| global.constant.defaultRoot;
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 400px;width: 400px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:"_channelTypeDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var channelTypeDiv=dhx.html.create("div",{
        style:"position:relative;height:400px;height:400px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;"
    });
    div.appendChild(channelTypeDiv);
    //创建buttondiv
//    var butDiv=dhx.html.create("div",{
//        style:"position:relative;border: 1px #eee solid;border-bottom:0px;height:30px;overflow:auto;padding-top:0xp;margin-top:6xp" +
//              "z-index:1000;padding-left:55px"
//    })
//    div.appendChild(butDiv);

    //移动节点位置至指定节点下。
    //var target=form.getInput("zone");
    var target=   document.getElementById("channelType");
    var targetId= document.getElementById("channelTypeCode");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(channelTypeDiv, channelTypeDiv.style.width, channelTypeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubChannelType);
        //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
    	target.value = tree.getItemText(nodeId);
        targetId.value=nodeId;
    	
        //关闭树
        div.style.display="none";
    });

     dwrCaller.executeAction("loadChannelTypeTree",beginId,selectChannelType,function(data){
        tree.loadJSONObject(data);
        globChannelTypeTree = tree;
        if(selectChannelType){
            tree.selectItem(selectChannelType); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
            targetId.value=tree.getSelectedItemId();
        }
        //为div添加事件
        Tools.addEvent(target,"focus",function(){
          //  div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        Tools.addEvent(target,"click",function(){
           // div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        
    });
    div.style.display="none";
}
/**
 *  渠道类型树 end
 */
//渠道类型部分节点去掉
var loadChannelTypeTreePart=function(selectChannelType,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    //selectZone=selectZone|| global.constant.defaultRoot;
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 400px;width: 400px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:"_channelTypeDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var channelTypeDiv=dhx.html.create("div",{
        style:"position:relative;height:400px;width:400;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;",id:"channelTypeDiv_tree"
    });
    div.appendChild(channelTypeDiv);
    //创建buttondiv
//    var butDiv=dhx.html.create("div",{
//        style:"position:relative;border: 1px #eee solid;border-bottom:0px;height:30px;overflow:auto;padding-top:0xp;margin-top:6xp" +
//              "z-index:1000;padding-left:55px"
//    })
//    div.appendChild(butDiv);

    //移动节点位置至指定节点下。
    //var target=form.getInput("zone");
    var target=   document.getElementById("channelType");
    var targetId= document.getElementById("channelTypeCode");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(channelTypeDiv, channelTypeDiv.style.width, channelTypeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubChannelTypePart);
        //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
    	target.value = tree.getItemText(nodeId);
        targetId.value=nodeId;
    	
        //关闭树
        div.style.display="none";
    });
     dwrCaller.executeAction("loadChannelTypeTreePart",beginId,selectChannelType,function(data){
        tree.loadJSONObject(data);
        globChannelTypeTree = tree;
        if(selectChannelType){
            tree.selectItem(selectChannelType); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
            targetId.value=tree.getSelectedItemId();
        }
        //为div添加事件
        Tools.addEvent(target,"focus",function(){
           // div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        Tools.addEvent(target,"click",function(){
            //div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        
    });
    div.style.display="none";
}
var loadChannelTypeTreePart1=function(selectChannelType,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    //selectZone=selectZone|| global.constant.defaultRoot;
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 400px;width: 400px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:"_channelTypeDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var channelTypeDiv=dhx.html.create("div",{
        style:"position:relative;height:400px;width:400;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;",id:"channelTypeDiv_tree"
    });
    div.appendChild(channelTypeDiv);
    //创建buttondiv
//    var butDiv=dhx.html.create("div",{
//        style:"position:relative;border: 1px #eee solid;border-bottom:0px;height:30px;overflow:auto;padding-top:0xp;margin-top:6xp" +
//              "z-index:1000;padding-left:55px"
//    })
//    div.appendChild(butDiv);

    //移动节点位置至指定节点下。
    //var target=form.getInput("zone");
    var target=   document.getElementById("channelType1");
    var targetId= document.getElementById("channelTypeCode1");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(channelTypeDiv, channelTypeDiv.style.width, channelTypeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubChannelTypePart);
        //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
    	target.value = tree.getItemText(nodeId);
        targetId.value=nodeId;
    	
        //关闭树
        div.style.display="none";
    });
     dwrCaller.executeAction("loadChannelTypeTreePart",beginId,selectChannelType,function(data){
        tree.loadJSONObject(data);
        globChannelTypeTree = tree;
        if(selectChannelType){
            tree.selectItem(selectChannelType); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
            targetId.value=tree.getSelectedItemId();
        }
        //为div添加事件
        Tools.addEvent(target,"focus",function(){
           // div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        Tools.addEvent(target,"click",function(){
            //div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        
    });
    div.style.display="none";
}
var loadChannelTypeTreePart2=function(selectChannelType,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    //selectZone=selectZone|| global.constant.defaultRoot;
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 400px;width: 400px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:"_channelTypeDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var channelTypeDiv=dhx.html.create("div",{
        style:"position:relative;height:400px;width:400;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;",id:"channelTypeDiv_tree"
    });
    div.appendChild(channelTypeDiv);
    //创建buttondiv
//    var butDiv=dhx.html.create("div",{
//        style:"position:relative;border: 1px #eee solid;border-bottom:0px;height:30px;overflow:auto;padding-top:0xp;margin-top:6xp" +
//              "z-index:1000;padding-left:55px"
//    })
//    div.appendChild(butDiv);

    //移动节点位置至指定节点下。
    //var target=form.getInput("zone");
    var target=   document.getElementById("channelType2");
    var targetId= document.getElementById("channelTypeCode2");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(channelTypeDiv, channelTypeDiv.style.width, channelTypeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubChannelTypePart);
        //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
    	target.value = tree.getItemText(nodeId);
        targetId.value=nodeId;
    	
        //关闭树
        div.style.display="none";
    });
     dwrCaller.executeAction("loadChannelTypeTreePart",beginId,selectChannelType,function(data){
        tree.loadJSONObject(data);
        globChannelTypeTree = tree;
        if(selectChannelType){
            tree.selectItem(selectChannelType); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
            targetId.value=tree.getSelectedItemId();
        }
        //为div添加事件
        Tools.addEvent(target,"focus",function(){
           // div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        Tools.addEvent(target,"click",function(){
            //div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        });
        
    });
    div.style.display="none";
}
/**
 *  渠道类型树 end
 */
//渠道类型部分节点去掉
var loadChannelTypeTreePart_all=function(selectChannelType,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    //selectZone=selectZone|| global.constant.defaultRoot;
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;
    //创建Div层
    var div=document.getElementById("_channelTypeDiv");
    //创建tree Div层
    var channelTypeDiv=document.getElementById("channelTypeDiv_tree");
    //创建buttondiv
//    var butDiv=dhx.html.create("div",{
//        style:"position:relative;border: 1px #eee solid;border-bottom:0px;height:30px;overflow:auto;padding-top:0xp;margin-top:6xp" +
//              "z-index:1000;padding-left:55px"
//    })
//    div.appendChild(butDiv);

    //移动节点位置至指定节点下。
    //var target=form.getInput("zone");
    var target=   document.getElementById("channelType");
    var targetId= document.getElementById("channelTypeCode");
    target.readOnly=true;
    //生成树
    var tree = new dhtmlXTreeObject(channelTypeDiv, channelTypeDiv.style.width, channelTypeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubChannelTypePart);
        //树双击鼠标事件
     dwrCaller.executeAction("loadChannelTypeTreePart",beginId,selectChannelType,function(data){
        tree.loadJSONObject(data);
        globChannelTypeTree = tree;
        if(selectChannelType){
            tree.selectItem(selectChannelType); //选中指定节点
            //将input框选中
            target.value=tree.getSelectedItemText();
            targetId.value=tree.getSelectedItemId();
        }
        //为div添加事件
        
        
    });
    div.style.display="none";
}