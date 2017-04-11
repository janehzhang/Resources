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
var indId="";
var indName="";
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
   
 
dwrCaller.addAutoAction("getCompareRate","CmplRateAction.getCompareRate",loadParam);

var init=function(){
     //加载地域树
	var queryform =$('queryform');
    loadZoneTreeChkBox(user.zoneId,queryform);
    //初始化参数
    //执行查询数据
    excuteInitData();
}
var excuteInitData=function(){
	dwrCaller.executeAction("getCompareRate",function(data){
	    $('buttonDiv').innerHTML='';
	    $('chartdiv').innerHTML='';
		$('chartTable').innerHTML='';
    	if(data&&data.list.length>0){
          var buttonStr="<table cellpadding='0px' cellspacing='0px'  border='0' width='800px'>"
		           +"<tr>"
		             +"<td align='center'>"
		             +"<input type='button'  class='poster_btn' id='compareRate'  name='compareRate'   value='投诉率对比'     onclick='getCompareRate();'      style='width:auto;' />"
		             +"<input type='button'  class='poster_btn' id='pianLiRate'   name='pianLiRate'    value='投诉率偏离值'   onclick='getPianLiRate();'       style='width:auto;' />"
		             +"<input type='button'  class='poster_btn' id='monthRate'    name='monthRate'     value='月份投诉率'     onclick='getMonthRate();'        style='width:auto;' />"
		             +"<input type='button'  class='poster_btn' id='areaRate'     name='areaRate'      value='区域投诉率'     onclick='getAreaRate();'         style='width:auto;' />"
		             +"</td>"
		            +"</tr>"
	            +"</table>";
    		$('buttonDiv').innerHTML=buttonStr;
    		
    	    chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "800", "400", "0", "0");
            chart.setDataXML(data.chartMap);
            chart.render("chartdiv");
            
        var tableStr="<table width='800' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
		  +"<tr>"
		    +"<td height='36' bgcolor='#cde5fd'><strong>月份</strong></td>"
		    +"<td bgcolor='#cde5fd'><strong>生命周期</strong></td>"
		    +"<td bgcolor='#cde5fd'><a id='tip' href='#'>环节权重<span id='tip_info'>十二个核心环节固定值，可在权重配置修改</span></a></td>"
		    +"<td bgcolor='#cde5fd'><a id='tip' href='#'>全业务用户数<span id='tip_info'>计费用户数</span></a></td>"
		    +"<td bgcolor='#cde5fd'><a id='tip' href='#'>投诉量<span id='tip_info'>不同投诉现象的投诉量计做各环节的投诉量</span></a></td>"
		    +"<td bgcolor='#cde5fd'><a id='tip' href='#'>投诉率<span id='tip_info'>投诉量/全业务用户数</span></a></td>"
		    +"<td bgcolor='#cde5fd'><a id='tip' href='#'>投诉率均值<span id='tip_info'>投诉率近3个月平均值</span></a></td>"
		    +"<td bgcolor='#cde5fd'><a id='tip' href='#'>偏离度<span id='tip_info'>（投诉率-投诉率均值）/投诉率均值</span></a></td>"
		    +"<td bgcolor='#cde5fd'><a id='tip' href='#'>分值<span id='tip_info'>偏离度*环节权重，若偏离度大于0则分值为0</span></a></td>"
		  +"</tr>";

      var total_BUSI_STEP_WEIGHT=0;//总权重
      var total_SCORE=0;//总分值
      
       for(var i=0;i<data.list.length;i++){
        tableStr =tableStr+
           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
		     +"<td>"+data.list[i].MONTH_ID+"</td>"
		     +"<td>"+data.list[i].BUSI_STEP_NAME+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP_ID+"','"+data.list[i].BUSI_STEP_NAME+"')\">"+data.list[i].BUSI_STEP_WEIGHT+'%'+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP_ID+"','"+data.list[i].BUSI_STEP_NAME+"')\">"+data.list[i].SUBS_NUM+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP_ID+"','"+data.list[i].BUSI_STEP_NAME+"')\">"+data.list[i].CMPL_NUM+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP_ID+"','"+data.list[i].BUSI_STEP_NAME+"')\">"+data.list[i].CMPL_RATE+'%'+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP_ID+"','"+data.list[i].BUSI_STEP_NAME+"')\">"+data.list[i].CMPL_RATE_AVG+'%'+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP_ID+"','"+data.list[i].BUSI_STEP_NAME+"')\">"+data.list[i].OFFSET+'%'+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP_ID+"','"+data.list[i].BUSI_STEP_NAME+"')\">"+data.list[i].SCORE+'%'+"</td>"
		    +"</tr>";
           total_BUSI_STEP_WEIGHT +=data.list[i].BUSI_STEP_WEIGHT;
		   total_SCORE+=data.list[i].SCORE;
         }
        tableStr =tableStr+
          "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
		     +"<td colspan='2'>合计</td>"
		     +"<td>"+total_BUSI_STEP_WEIGHT+'%'+"</td>"
		     +"<td>&nbsp;&nbsp;</td>"
		     +"<td>&nbsp;&nbsp;</td>"
		     +"<td>&nbsp;&nbsp;</td>"
		     +"<td>&nbsp;&nbsp;</td>"
		     +"<td>&nbsp;&nbsp;</td>"
		     +"<td>"+toDecimal(total_SCORE)+'%'+"</td>"
		  +"</tr>";
       
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
            formData.indId=indId;
            formData.indName=indName;
            return formData;
           }
        }
    ]);
    return loadParam;
}
//查询
var queryData=function(){
    var loadParam=initParam();
    dwrCaller.addAutoAction("getCompareRate","CmplRateAction.getCompareRate",loadParam);
	excuteInitData();
}

//投诉率对比
var getCompareRate=function(){
    var loadParam=initParam();
    dwrCaller.addAutoAction("getCompareRate","CmplRateAction.getCompareRate",loadParam);
    dwrCaller.executeAction("getCompareRate",function(data){
	    $('chartdiv').innerHTML='';
    	if(data&&data.list.length>0){
    		chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+Math.random(), "800", "400", "0", "0");
            chart.setDataXML(data.chartMap);
            chart.render("chartdiv");
	      }
      });
}
//投诉率偏离值
var getPianLiRate=function(){
    var loadParam=initParam();
    dwrCaller.addAutoAction("getPianLiRate","CmplRateAction.getPianLiRate",loadParam);
    dwrCaller.executeAction("getPianLiRate",function(data){
	    $('chartdiv').innerHTML='';
       if(data&&data.list.length>0){
    		chart=new FusionCharts(base+"/js/Charts/PowerCharts/Radar.swf", "ChartId2_"+Math.random(), "800", "400", "0", "0");
            chart.setDataXML(data.chartMap);
            chart.render("chartdiv");
	      }
      });
}
//月份投诉率
var getMonthRate=function(){
    var loadParam=initParam();
    dwrCaller.addAutoAction("getMonthRate","CmplRateAction.getMonthRate",loadParam);
    dwrCaller.executeAction("getMonthRate",function(data){
	    $('chartdiv').innerHTML='';
       if(data){
    		 chart=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId3_"+Math.random(), "800", "400", "0", "0");
             chart.setXMLData(data.chartMap);
             chart.render("chartdiv");
	     }
      });
}
//区域投诉率
var getAreaRate=function(){
    var loadParam=initParam();
    dwrCaller.addAutoAction("getAreaRate","CmplRateAction.getAreaRate",loadParam);
    dwrCaller.executeAction("getAreaRate",function(data){
	    $('chartdiv').innerHTML='';
       if(data){
    		 chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId4_"+Math.random(), "800", "400", "0", "0");
             chart.setXMLData(data.chartMap);
             chart.render("chartdiv");
	     }
      });
}

//单击
var fchart=function(ind_id,ind_name){
    indId=ind_id;
    indName=ind_name;
    //月环节
	 if(chart.id.split("_")[0] == "ChartId3"){
      	    	var loadParam=initParam();
			    dwrCaller.addAutoAction("getMonthRate","CmplRateAction.getMonthRate",loadParam);
			    dwrCaller.executeAction("getMonthRate",function(data){
				    $('chartdiv').innerHTML='';
			       if(data){
			    	    chart=new FusionCharts(base+"/js/Charts/MSLine.swf",  "ChartId3_"+Math.random() , "800", "400", "0", "0");
			            chart.setDataXML(data.chartMap);
			            chart.render("chartdiv");
				      }
			      });
	 }
	//区域
	if(chart.id.split("_")[0]=="ChartId4"){
				 	var loadParam=initParam();
				    dwrCaller.addAutoAction("getAreaRate","CmplRateAction.getAreaRate",loadParam);
				    dwrCaller.executeAction("getAreaRate",function(data){
					$('chartdiv').innerHTML='';
				       if(data){
				    		chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf","ChartId4_"+Math.random(), "800", "400", "0", "0");
				            chart.setDataXML(data.chartMap);
				            chart.render("chartdiv");
					      }
				      });			 
	 }
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
dhx.ready(init);