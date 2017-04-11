/**
 * 页面初始化。
 */

dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();

//当前系统的主页Path
var base = getBasePath();
var chart = null;

var globZoneTree = null;

var dateTime = $("dateTime").value;
var zoneId =   $('zoneId').value;
if(zoneId==''||zoneId==null){zoneId='1';}

var loadParam = new biDwrMethodParam();
    loadParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
        	var  formData=new Object();
            formData.dateTime=dateTime;
            formData.zoneId=zoneId;
            return formData;
        }
        }
    ]);
   
 
dwrCaller.addAutoAction("getChartData","CustomerCoreScoreAction.getChartData",loadParam);

var userInit=function(){
     //加载地域树
	var queryform =$('queryform');
    loadZoneTreeChkBox(user.zoneId,queryform);
    //初始化参数
    //执行查询数据
    excuteData();
}


var excuteData=function(){
	dwrCaller.executeAction("getChartData",function(data){
		$('chartdiv').innerHTML='';
		$('chartTable').innerHTML='';
    	if(data&&data.list.length>0){
    		chart=new FusionCharts(base+"/js/Charts/MSColumn3DLineDY.swf", "ChartId", "800", "400", "0", "0");
            chart.setDataXML(data.chartMap);
            chart.render("chartdiv");
            var tableStr="<table width='800' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
		  +"<tr>"
		    +"<td height='36' bgcolor='#cde5fd'><strong>生命周期</strong></td>"
		    +"<td bgcolor='#cde5fd'><a id='tip' href='#'>环节权重<span id='tip_info'>根据十二个关键环节重要度，通过层次分析法设定</span></a></td>"
		    +"<td bgcolor='#cde5fd'><a id='tip' href='#'>环节得分<span id='tip_info'>环节权重-环节偏离量，点击详情查看环节偏离量</span></a></td>"
		    +"<td bgcolor='#cde5fd'><a id='tip' href='#'>转换100分制得分<span id='tip_info'>环节得分/环节权重*100</span></a></td>"
		    +"<td bgcolor='#cde5fd'><strong>排名</strong></td>"
		 +"</tr>";
      
      var total_BUSI_STEP_WEIGHT=0;
      var total_STEP_SCORE=0;
      var total_REAL_SCORE=0;
      for(var i=0;i<data.list.length;i++){
        tableStr =tableStr+
           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
		    +"<td>"+data.list[i].BUSI_STEP_NAME+"</td>"
		     +"<td>"+data.list[i].BUSI_STEP_WEIGHT+'%'+"</td>"
		     +"<td>"+data.list[i].STEP_SCORE+'%'+"</td>"
		     +"<td>"+data.list[i].REAL_SCORE+"</td>"
		     +"<td>"+data.list[i].SORT_NUM+"</td>"
		    +"</tr>";
           total_BUSI_STEP_WEIGHT +=data.list[i].BUSI_STEP_WEIGHT;
           total_STEP_SCORE +=data.list[i].STEP_SCORE;
           total_REAL_SCORE   +=data.list[i].REAL_SCORE;
      }
      
           tableStr =tableStr+
           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
		    +"<td>所有环节</td>"
		     +"<td>"+total_BUSI_STEP_WEIGHT+'%'+"</td>"
		     +"<td>"+toDecimal(total_STEP_SCORE)+'%'+"</td>"
		     +"<td>"+toDecimal(total_REAL_SCORE)+"</td>"
		     +"<td>&nbsp;&nbsp;</td>"
		    +"</tr>";
           tableStr =tableStr+
           "</table>"; 
              $('chartTable').innerHTML=tableStr;
	      }else{
	    	  $('chartTable').innerHTML='<br/>没找到任何数据';
	      }
      });
	
}


dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPath");
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptId",pidColumn:"parentId",
    isDycload:false,textColumn:"deptName"
});
var zoneConverter=dhx.extend({idColumn:"zoneId",pidColumn:"zoneParId",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);

dwrCaller.addAction("querySubZone",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZone(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});

var loadZoneTreeChkBox=function(selectZone,form){
    //加载部门树数据。加载用户所在部门及其子部门。
    selectZone=selectZone|| global.constant.defaultRoot;
    var beginId=(user.userId==global.constant.adminId?global.constant.defaultRoot:user.zoneId)
            || global.constant.defaultRoot;
    //创建Div层
    var div=dhx.html.create("div",{
        style:"display;none;position:absolute;border: 1px #eee solid;height: 200px;width: 180px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:"_zoneDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height:200px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;"
    });
    div.appendChild(treeDiv);
    //创建buttondiv
//    var butDiv=dhx.html.create("div",{
//        style:"position:relative;border: 1px #eee solid;border-bottom:0px;height:30px;overflow:auto;;padding-top:0xp;margin-top:6xp" +
//              "z-index:1000;padding-left:55px"
//    })
//    div.appendChild(butDiv);

    //移动节点位置至指定节点下。
    //var target=form.getInput("zone");
    var target=document.getElementById("zone");
    var targetId=document.getElementById("zoneId");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, 0);
 
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableCheckBoxes(true);
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubZone);
    tree.attachEvent("onCheck",function(id,state){
        var checkedData = tree.getAllChecked();
        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
        var zones = "";
        var zonesId ="";
        for (i = 0;i< nodes.length;i++){
            if(zones == ""){
                zones =  tree.getItemText(nodes[i]).toString();
            }else{
                zones =   zones + "," +  tree.getItemText(nodes[i]).toString() ;
            }
            if(zonesId == ""){
                zonesId = nodes[i].toString();
            }else{
                zonesId = zonesId   + "," + nodes[i].toString();
            }
        }
        if(zones == 0){
            zones = "";
            zonesId = "";
        }
        target.value = zones;
        targetId.value=zonesId;
        //alert(zonesId);
        //form.setFormData({zone:zones,zoneId:zonesId});
    });


    tree.attachEvent("onSelect",function(id){
        tree.setCheck(id,1);
        var checkedData = tree.getAllChecked();
        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
        var zones = "";
        var zonesId ="";
        for (i = 0;i< nodes.length;i++){
            if(zones == ""){
                zones =  tree.getItemText(nodes[i]).toString();
            }else{
                zones =   zones + "," +  tree.getItemText(nodes[i]).toString() ;
            }
            if(zonesId == ""){
                zonesId = nodes[i].toString();
            }else{
                zonesId = zonesId   + "," + nodes[i].toString();
            }
        }
        if(zones == 0){
            zones = "";
            zonesId = null;
        }
        //form.setFormData({zone:zones,zoneId:zonesId});
    });


//    var button = Tools.getButtonNode("确定");
//    button.style.marginTop="5px";
////    button.type = "button";
//    butDiv.appendChild(button);
//    // 点击button事件
//    button.onclick = function(){
//        var checkedData = tree.getAllChecked();
//        var nodes = typeof checkedData == "string" ? checkedData.split(","):[checkedData];
//        var zones = "";
//        var zonesId ="";
//        for (i = 0;i< nodes.length;i++){
//            if(zones == ""){
//                zones =  tree.getItemText(nodes[i]).toString();
//            }else{
//                zones =   zones + "," +  tree.getItemText(nodes[i]).toString() ;
//            }
//            if(zonesId == ""){
//                zonesId = nodes[i].toString();
//            }else{
//                zonesId = zonesId   + "," + nodes[i].toString();
//            }
//        }
//        if(zones == 0){
//            zones = "";
//            zonesId = null;
//        }
//        form.setFormData({zone:zones,zoneId:zonesId});
//        div.style.display = "none";
//
//    }
//    //树双击鼠标事件
//    tree.attachEvent("onDblClick",function(nodeId){
//        form.setFormData({zone:tree.getItemText(nodeId),zoneId:nodeId});
//        //关闭树
//        div.style.display="none";
//    });
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
        Tools.addEvent(target,"click",function(){
            // div.style.width = target.offsetWidth+'px';
            Tools.divPromptCtrl(div,target,true);
            div.style.display="block";
        })
    })
    div.style.display="none";
}
//参数初始化公共部门
var initParam=function(){
    var loadParam = new biDwrMethodParam();
    loadParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
        	var  formData=new Object();
            formData.dateTime=$("dateTime").value;
            formData.zoneId=$("zoneId").value;
            //formData.title="环节得分";
            return formData;
        }
        }
    ]);
    return loadParam;
}
//查询
var queryData=function(){
    var loadParam=initParam();
	dwrCaller.addAutoAction("getChartData","CustomerCoreScoreAction.getChartData",loadParam);
	excuteData();
}
//详情
var detail=function(){
	openTab('客户服务核心环节详细得分','portalCommon/module/coreLink/customerCoreScoreDetail.jsp?dateTime='+$("dateTime").value);
}
//权重配置
var wieghtConfig=function(){
  openTab('权重配置','portalCommon/module/coreLink/config/busiStepConfig.jsp');
}

var openTab=function(menu_name,url){
	return parent.openMenu(menu_name,url,'top');
}

//四舍五入 
var toDecimal=function(x) {     
         var f = parseFloat(x);     
         if (isNaN(f)) {       
           return;      
        }            
        f = Math.round(x*100)/100;        
         return f;
 } 
dhx.ready(userInit);

