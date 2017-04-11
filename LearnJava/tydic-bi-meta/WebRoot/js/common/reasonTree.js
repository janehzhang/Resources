/**
 *  地区树 start
 */
var loadReasonTreeChkBox=function(selectReason,form){
    //加载原因树数据。
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;	
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 400px;width: 350px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white;",id:"_reasonDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height:400px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;",id:"treeDiv"
    });
    div.appendChild(treeDiv);
    //移动节点位置至指定节点下。
    var target=   document.getElementById("reason");
    var targetId= document.getElementById("reasonCode");
    target.readOnly=true;
    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubReasonCode);
        //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
    	target.value = tree.getItemText(nodeId);
        targetId.value=nodeId;
    	
        //关闭树
        div.style.display="none";
    });

     dwrCaller.executeAction("loadReasonTree",beginId,selectReason,function(data){
        tree.loadJSONObject(data);
        globReasonTree = tree;
        if(selectReason){
            tree.selectItem(selectReason); //选中指定节点
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
var loadReasonTreeChkBox2=function(selectReason,form){
    //加载部门树数据。加载用户所在部门及其子部门。
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;
	
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 400px;width: 350px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white;",id:"_reasonDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height:400px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;"
    });
    div.appendChild(treeDiv);
    var target=   document.getElementById("reason2");
    var targetId= document.getElementById("reasonCode2");
    target.readOnly=true;
    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubReasonCode);
        //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
    	target.value = tree.getItemText(nodeId);
        targetId.value=nodeId;
    	
        //关闭树
        div.style.display="none";
    });

     dwrCaller.executeAction("loadReasonTree",beginId,selectReason,function(data){
        tree.loadJSONObject(data);
        globReasonTree = tree;
        if(selectReason){
            tree.selectItem(selectReason); //选中指定节点
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
var loadReasonTreeChkBox1=function(selectReason,form){
    //加载部门树数据。加载用户所在部门及其子部门。
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;
	
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 400px;width: 350px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white;",id:"_reasonDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height:400px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;"
    });
    div.appendChild(treeDiv);
    var target=   document.getElementById("reason1");
    var targetId= document.getElementById("reasonCode1");
    target.readOnly=true;
    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubReasonCode);
        //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
    	target.value = tree.getItemText(nodeId);
        targetId.value=nodeId;
    	
        //关闭树
        div.style.display="none";
    });

     dwrCaller.executeAction("loadReasonTree",beginId,selectReason,function(data){
        tree.loadJSONObject(data);
        globReasonTree = tree;
        if(selectReason){
            tree.selectItem(selectReason); //选中指定节点
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