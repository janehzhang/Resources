/**
 *  业务类型树 start
 */
var loadProdTypeTree=function(selectZone,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    //selectZone=selectZone|| global.constant.defaultRoot;
	var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)|| global.constant.defaultRoot;
	
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 400px;width:400px;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:"_zoneDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height:400px;padding: 0;margin: 0;width:400px;" +
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
    var target=   document.getElementById("prodType");
    var targetId= document.getElementById("prodTypeCode");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, "0");
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubProdType);
        //树双击鼠标事件
    tree.attachEvent("onclick",function(nodeId){
    	target.value = tree.getItemText(nodeId);
        targetId.value=nodeId;
    	
        //关闭树
        div.style.display="none";
    });

     dwrCaller.executeAction("loadProdTypeTree",beginId,selectZone,function(data){
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
 *  业务类型树 end
 */