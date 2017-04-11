/**
 * 页面初始化。
 */

dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();

var queryform =$('queryform');
//当前系统的主页Path
var base = getBasePath();
var chart = null;
var globZoneTree = null;
var dateTime = $("dateTime").value;
var zoneId =   userInfo['zoneId'];
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
   
 
dwrCaller.addAutoAction("getHjCoreData","CustomerCoreScoreDetailAction.getHjCoreData",loadParam);

var userInit=function(){
	
    //1.加载地域树 
    loadZoneTreeChkBox(zoneId,queryform);
    
    //初始化参数
    //执行查询数据
    excuteInitData();
}


var excuteInitData=function(){
    $("city").style.display  ="none";
	dwrCaller.executeAction("getHjCoreData",function(data){
		$('buttonDiv').innerHTML='';
	    $('chartdiv').innerHTML='';
		$('chartTable').innerHTML='';
    	if(data&&data.list.length>0){
   var buttonStr="<table cellpadding='0px' cellspacing='0px'  border='0' width='800px'>"
		           +"<tr>"
		             +"<td align='center'>"
		             +"<input type='button'  class='poster_btn' id='hjdfBtn'    name='hjdfBtn'   value='环节得分'     onclick='hjdfData();'         style='width:auto;' />"
		             +"<input type='button'  class='poster_btn' id='hjpllBtn'   name='hjpllBtn'  value='环节偏离量'   onclick='hjpllData();'        style='width:auto;' />"
		             +"<input type='button'  class='poster_btn' id='yhjdfBtn'   name='yhjdfBtn'  value='环节得分月份趋势'   onclick='yhjdfData();'   style='width:auto;' />"
		             +"<input type='button'  class='poster_btn' id='qyhjdfBtn'  name='qyhjdfBtn' value='环节得分区域分布' onclick='qyhjdfData();'    style='width:auto;' />"
		             +"</td>"
		            +"</tr>"
	            +"</table>";
    		
    		$('buttonDiv').innerHTML=buttonStr;
    		
    		chart=new FusionCharts(base+"/js/Charts/MSCombiDY2D.swf", "ChartId1_"+Math.random(), "800", "400", "0", "0");
    		chart1=new FusionCharts(base+"/portal/module/portal/map/map.swf", "ChartId0_"+Math.random(), "800", "400", "0", "0");
    		chart1.render("chartdiv1");
    		chart1.setDataXML("<chart exposehoverevent='1'></chart>")
            chart.setDataXML(data.chartMap);
            chart.render("chartdiv");
            var tableStr="<table width='800' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
		  +"<tr>"
		    +"<td height='36' bgcolor='#cde5fd'><strong>生命周期</strong></td>"
		    +"<td bgcolor='#cde5fd'>环节权重(%)</td>"
		    +"<td bgcolor='#cde5fd'>扣分</td>"
		    //+"<td bgcolor='#cde5fd'><a id='tip' href='#'>投诉偏离量<span id='tip_info'>该环节的投诉率引起的偏离量，见生命周期投诉率报表</span></a></td>"
		   // +"<td bgcolor='#cde5fd'><a id='tip' href='#'>总偏离量<span id='tip_info'>环节偏离量+投诉偏离量</span></a></td>"
		    +"<td bgcolor='#cde5fd'>环节得分</td>"
		    +"<td bgcolor='#cde5fd'>100分制</td>"
		 +"</tr>";
       
      var total_BUSI_STEP_WEIGHT=0;
      var total_STEP_OFFSET=0;
      var total_CMPL_OFFSET=0;
      var total_TOTAL_OFFSET=0;
      var total_STEP_SCORE=0;
      var total_REAL_SCORE=0;
      
      var trTemp="";
       for(var i=0;i<data.list.length;i++){
        trTemp =trTemp+
           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
             +"<td><a href='javascript:void(0)' onClick=\"openTab('"+data.list[i].BUSI_STEP_NAME+"','/portalCommon/module/coreLink/coreCommon/busiStepGeneral.jsp?dateTime="+$('dateTime').value+"&indId="+data.list[i].BUSI_STEP_ID+"&indName="+data.list[i].BUSI_STEP_NAME+"')\">"
		     +data.list[i].BUSI_STEP_NAME+"</a></td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP_ID+"','"+data.list[i].BUSI_STEP_NAME+"')\">"+data.list[i].BUSI_STEP_WEIGHT+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP_ID+"','"+data.list[i].BUSI_STEP_NAME+"')\">"+data.list[i].STEP_OFFSET+"</td>"
		    // +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP_ID+"','"+data.list[i].BUSI_STEP_NAME+"')\">"+data.list[i].CMPL_OFFSET+'%'+"</td>"
		    // +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP_ID+"','"+data.list[i].BUSI_STEP_NAME+"')\">"+data.list[i].TOTAL_OFFSET+'%'+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP_ID+"','"+data.list[i].BUSI_STEP_NAME+"')\">"+data.list[i].STEP_SCORE+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP_ID+"','"+data.list[i].BUSI_STEP_NAME+"')\">"+data.list[i].REAL_SCORE+"</td>"
		    +"</tr>";
        
           total_BUSI_STEP_WEIGHT +=data.list[i].BUSI_STEP_WEIGHT;
		   total_STEP_OFFSET  +=data.list[i].STEP_OFFSET;
		   total_CMPL_OFFSET  +=data.list[i].CMPL_OFFSET;
		   total_TOTAL_OFFSET +=data.list[i].STEP_OFFSET+data.list[i].CMPL_OFFSET;
           total_STEP_SCORE   +=data.list[i].STEP_SCORE;
           //total_REAL_SCORE   +=data.list[i].REAL_SCORE;
         }
        tableStr =tableStr+
           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
		    +"<td>所有环节</td>"
		     +"<td>"+total_BUSI_STEP_WEIGHT+"</td>"
		     +"<td>"+toDecimal(total_STEP_OFFSET)+"</td>"
		    // +"<td>"+toDecimal(total_CMPL_OFFSET)+'%'+"</td>"
		    // +"<td>"+toDecimal(total_TOTAL_OFFSET)+'%'+"</td>"
		     +"<td>"+toDecimal(total_STEP_SCORE)+"</td>"
		     +"<td>"+toDecimal(total_STEP_SCORE)+"</td>"
		  +"</tr>";
        
              tableStr +=trTemp;
              $('chartTable').innerHTML=tableStr;
              
           var expStr="<table <table width='800' border='0' cellpadding='0' cellspacing='0' class='tab_01'>";
           	+"<tr>"
			    +"<td  bgcolor='#cde5fd'><strong>客户服务诉求</strong></td>"
		    expStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
				       +"<td align=\"left\"><font style=\"font-weight:bold;\" >环节权重:</font>十二个核心环节固定值，可在权重配置修改</td>"
				   +"</tr>";
            expStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
				       +"<td align=\"left\"><font style=\"font-weight:bold;\" >扣分:</font>该环节的所有诉求指标偏离分值合计，点击该环节链接查看诉求指标的偏离量</td>"
				   +"</tr>";
            expStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
				       +"<td align=\"left\"><font style=\"font-weight:bold;\" >环节得分:</font>环节权重+扣分</td>"
				   +"</tr>";
            expStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
				       +"<td align=\"left\"><font style=\"font-weight:bold;\" >100分制:</font>环节得分/环节权重*100</td>"
				   +"</tr>";
	       expStr +="</table>"; 
         $('chartTable').innerHTML +=expStr;
         
              
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
        style:"display;none;position:absolute;border: 1px #eee solid;height: 400px;width: 400px;overflow: auto;padding: 0;margin: 0;" +
              "z-index:1000;background-color:white",id:"_zoneDiv"
    });
    document.body.appendChild(div);
    //创建tree Div层
    var treeDiv=dhx.html.create("div",{
        style:"position:relative;height:400px;width:400;overflow: auto;padding: 0;margin: 0;" +
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
    var target=document.getElementById("zone");
    var targetId=document.getElementById("zoneId");
    target.readOnly=true;

    //生成树
    var tree = new dhtmlXTreeObject(treeDiv, treeDiv.style.width, treeDiv.style.height, 0);
    tree.setImagePath(getDefaultImagePath() + "csh_" + getSkin() + "/");
    tree.enableThreeStateCheckboxes(true);
    tree.enableHighlighting(true);
    tree.enableSingleRadioMode(true);
    tree.setDataMode("json");
    tree.setXMLAutoLoading(dwrCaller.querySubZone);
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
           // div.style.width = target.offsetWidth+'px';
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



//参数初始化公共部门
var initParam=function(){
    var loadParam = new biDwrMethodParam();
    loadParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
        	var  formData=new Object();
            formData.dateTime=$("dateTime").value;
            formData.zoneId=  $("zoneId").value;
            formData.indId=   $("indId").value;
            return formData;
        }
        }
    ]);
    
    return loadParam;
	
}

//查询
var queryData=function(){
    var loadParam=initParam();
    dwrCaller.addAutoAction("getHjCoreData","CustomerCoreScoreDetailAction.getHjCoreData",loadParam);
	excuteInitData();
}

//环节得分
var hjdfData=function(){
    $("city").style.display =    "none";
    var loadParam=initParam();
    dwrCaller.addAutoAction("getHjCoreData","CustomerCoreScoreDetailAction.getHjCoreData",loadParam);
    dwrCaller.executeAction("getHjCoreData",function(data){
	    $('chartdiv').innerHTML='';
    	if(data&&data.list.length>0){
    		chart=new FusionCharts(base+"/js/Charts/MSCombiDY2D.swf", "ChartId1_"+Math.random(), "800", "400", "0", "0");
            chart.setDataXML(data.chartMap);
            chart.render("chartdiv");
	      }
      });
}


//环节偏离量
var hjpllData=function(){
    $("city").style.display =   "none";
	var loadParam=initParam();
    dwrCaller.addAutoAction("getHjBllData","CustomerCoreScoreDetailAction.getHjBllData",loadParam);
    dwrCaller.executeAction("getHjBllData",function(data){
	    $('chartdiv').innerHTML='';
       if(data&&data.list.length>0){
    		chart=new FusionCharts(base+"/js/Charts/PowerCharts/Radar.swf", "ChartId2_"+Math.random(), "800", "400", "0", "0");
            chart.setDataXML(data.chartMap);
            chart.render("chartdiv");
	      }
      });
}

//月环节得分
var yhjdfData=function(){
    $("city").style.display =   "none";
    var loadParam=initParam();
    dwrCaller.addAutoAction("getMonHjCoreData","CustomerCoreScoreDetailAction.getMonHjCoreData",loadParam);
    dwrCaller.executeAction("getMonHjCoreData",function(data){
	    $('chartdiv').innerHTML='';
       if(data){
    		 chart=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId3_"+Math.random(), "800", "400", "0", "0");
             chart.setXMLData(data.chartMap);
             chart.render("chartdiv");
	     }
      });
}

//区域环节得分
var qyhjdfData=function(){
  if(userInfo['localCode']=='0000') { //不是广东省  （钻取权限控制）	
	 $("city").style.display =   "block";
	}
    var loadParam=initParam();
    dwrCaller.addAutoAction("getAreaHjCoreData","CustomerCoreScoreDetailAction.getAreaHjCoreData",loadParam);
    dwrCaller.executeAction("getAreaHjCoreData",function(data){
	   $('chartdiv').innerHTML='';
       if(data){
    		 chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId4_"+Math.random(), "800", "400", "0", "0");
             chart.setXMLData(data.chartMap);
             chart.render("chartdiv");
	     }
      });
}

var selectData=function(value){
   if($('chartdiv').innerHTML.indexOf("ChartId3_") !=-1){
	   yhjdfData();
   }
  if($('chartdiv').innerHTML.indexOf("ChartId4_") !=-1){
	   qyhjdfData();
   }
}
//单击
var fchart=function(ind_id,ind_name){
	//alert("请选择");
	/**   
	    indId=ind_id;
	    indName=ind_name;
	    //月环节
		 if(chart.id.split("_")[0] == "ChartId3"){
	       var loadParam=initParam();
		    dwrCaller.addAutoAction("getMonHjCoreData","CustomerCoreScoreDetailAction.getMonHjCoreData",loadParam);
		    dwrCaller.executeAction("getMonHjCoreData",function(data){
			    $('chartdiv').innerHTML='';
		       if(data){
		    		 chart=new FusionCharts(base+"/js/Charts/Line.swf", "ChartId3_"+Math.random(), "800", "400", "0", "0");
		             chart.setXMLData(data.chartMap);
		             chart.render("chartdiv");
			     }
		      });
		 }
		//区域
		if(chart.id.split("_")[0]=="ChartId4"){
                 var loadParam=initParam();
                 dwrCaller.addAutoAction("getAreaHjCoreData","CustomerCoreScoreDetailAction.getAreaHjCoreData",loadParam);
                 dwrCaller.executeAction("getAreaHjCoreData",function(data){
	             $('chartdiv').innerHTML='';
			      if(data){
			    		 chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId4_"+Math.random(), "800", "400", "0", "0");
			             chart.setXMLData(data.chartMap);
			             chart.render("chartdiv");
				 }
      });
			 
		 }
	**/
 
}

//投诉率
var rateData=function(){
	openTab('生命周期投诉率','/portalCommon/module/coreLink/rate/cmplRate.jsp?dateTime='+$("dateTime").value);
}
var openTab=function(menuName,url){
    var menuId="";
	switch(menuName){
	    case "业务咨询":{menuId="116070"; break;}
	    case "业务办理":{menuId="116071"; break;}
	    case "业务开通":{menuId="116072"; break;}
	    case "装移机":  {menuId="116073"; break;}
	    case "网络质量":{menuId="116074"; break;}
	    case "产品质量":{menuId="116075"; break;}
	    case "使用提醒":{menuId="116076"; break;}
	    case "计费与账单":{menuId="116077"; break;}
	    case "充值缴费":{menuId="116078"; break;}
	    case "故障修复":{menuId="116079"; break;}
	    case "投诉处理":{menuId="116080"; break;}
	    case "客户关怀":{menuId="116081"; break;}
	    default:{alert("异常！");}
    }
	return parent.openTreeTab(menuId,menuName, base+url,'top');
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


var lookCity = function (obj){
       var map=new Object();
           map.zoneId  =$("zoneId").value;
		   map.dateTime=$("dateTime").value;
		   map.indId=$("indId").value;
		  if(obj.value=='切换地市'){
			  obj.value='切换区域';
			  map.changeZone="0";
		  }else{
			  obj.value='切换地市';
			  map.changeZone="1";
		  }
		CustomerCoreScoreDetailAction.loadSet21AreaChart(map, {callback:function (res) {
    	         if(res != null){
                        build21Chart(res);
		         }
		   }
		});
}

function  build21Chart(data){
     $('chartdiv').innerHTML='';
       if(data){
    		 chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId4_"+Math.random(), "800", "400", "0", "0");
             chart.setXMLData(data.chartMap);
             chart.render("chartdiv");
	     }
}
dhx.ready(userInit);