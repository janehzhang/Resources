/**
 * 页面初始化。
 */

dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
//当前系统的主页Path
var ind_id=indId;
var base = getBasePath();
var chart = null;

var step3Id="";
var step3Name="";

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
            formData.indId=ind_id;
            return formData;
        }
        }
    ]);
dwrCaller.addAutoAction("getIndPlValue","BusiStepGeneralAction.getIndPlValue",loadParam);

var userInit=function(){
     //加载地域树
    loadZoneTreeChkBox(zoneId,queryform);
    //初始化参数
    excuteInitData();
}
var excuteInitData=function(){
	dwrCaller.executeAction("getIndPlValue",function(data){
	    $('buttonDiv').innerHTML='';
	    $('chartdiv').innerHTML='';
		$('chartTable').innerHTML='';
	  
   if(data&&data.list.length>0){
	      
	   var buttonStr="<table cellpadding='0px' cellspacing='0px'  border='0' width='800px'>"
		           +"<tr>"
		             +"<td align='center'>"
		             +"<input type='button'  class='poster_btn' id='indPlValue'       name='indPlValue'     value='偏离雷达图'   onclick='getIndPlValue();'     style='width:100px;' />"
		             +"<input type='button'  class='poster_btn' id='monthIndValue'    name='monthIndValue'  value='时间趋势图'   onclick='getMonthIndValue();'  style='width:100px;' />"
		             +"<input type='button'  class='poster_btn' id='areaIndValue'     name='areaIndValue'   value='区域比较图'   onclick='getAreaIndValue();'   style='width:100px;' />"
		             +"</td>"
		            +"</tr>"
	            +"</table>";
    		 $('buttonDiv').innerHTML=buttonStr;
    		 
    	     chart=new FusionCharts(base+"/js/Charts/PowerCharts/Radar.swf", "ChartId1_"+Math.random() ,"900", "400", "0", "0");
             chart.setXMLData(data.chartMap);
             chart.render("chartdiv");
             
        
           var tableStr="<table id='mytbl' width='900' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
			  +"<tr>"
			    +"<td height='36' bgcolor='#cde5fd'><strong>客户服务诉求</strong></td>"
			    +"<td height='36' bgcolor='#cde5fd'><strong>具体指标</strong></td>"
			    +"<td bgcolor='#cde5fd'>指标权重</td>"
			    +"<td bgcolor='#cde5fd'>指标指向</td>"
			    +"<td bgcolor='#cde5fd'>预警值</td>"
			    +"<td bgcolor='#cde5fd'>当月值</td>"
			    +"<td bgcolor='#cde5fd'>投诉占比</td>"
			    +"<td bgcolor='#cde5fd'>偏离度</td>"
			    +"<td bgcolor='#cde5fd'>扣分</td>"
			 +"</tr>";
           
     
      var total_BUSI_STEP_WEIGHT=0;
      var total_SCORE=0;
      for(var i=0;i<data.list.length;i++){
        tableStr =tableStr+
           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
		     +"<td>"+data.list[i].BUSI_STEP2_NAME+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP3_ID+"','"+data.list[i].BUSI_STEP3_NAME+"')\">"+data.list[i].BUSI_STEP3_NAME+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP3_ID+"','"+data.list[i].BUSI_STEP3_NAME+"')\">"+data.list[i].BUSI_STEP_WEIGHT+'%'+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP3_ID+"','"+data.list[i].BUSI_STEP3_NAME+"')\">"+data.list[i].BUSI_STEP_DIRECTION+"</td>";
		 if(data.list[i].VALUETYPE_FLAG == '1'){  
            tableStr =tableStr+
		      "<td onClick=\"fchart('"+data.list[i].BUSI_STEP3_ID+"','"+data.list[i].BUSI_STEP3_NAME+"')\">"+data.list[i].ALARM_VALUE+'‰'+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP3_ID+"','"+data.list[i].BUSI_STEP3_NAME+"')\">"+data.list[i].CURRENT_VALUE+'‰'+"</td>";
		    }
		 else if(data.list[i].VALUETYPE_FLAG == '2'){  
	            tableStr =tableStr+
			      "<td onClick=\"fchart('"+data.list[i].BUSI_STEP3_ID+"','"+data.list[i].BUSI_STEP3_NAME+"')\">"+data.list[i].ALARM_VALUE+'%'+"</td>"
			     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP3_ID+"','"+data.list[i].BUSI_STEP3_NAME+"')\">"+data.list[i].CURRENT_VALUE+'%'+"</td>";
			}else{
		    tableStr =tableStr+ 	
		       "<td onClick=\"fchart('"+data.list[i].BUSI_STEP3_ID+"','"+data.list[i].BUSI_STEP3_NAME+"')\">"+data.list[i].ALARM_VALUE+"</td>"
		      +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP3_ID+"','"+data.list[i].BUSI_STEP3_NAME+"')\">"+data.list[i].CURRENT_VALUE+"</td>";
		    } 
		 var CMPL_PROPORTION=data.list[i].CMPL_PROPORTION+'%';
		 if(data.list[i].CMPL_PROPORTION==0){
			 CMPL_PROPORTION='-'
		 }
            tableStr =tableStr+ 
              "<td onClick=\"fchart('"+data.list[i].BUSI_STEP3_ID+"','"+data.list[i].BUSI_STEP3_NAME+"')\">"+CMPL_PROPORTION+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP3_ID+"','"+data.list[i].BUSI_STEP3_NAME+"')\">"+data.list[i].OFFSET+'%'+"</td>"
		     +"<td onClick=\"fchart('"+data.list[i].BUSI_STEP3_ID+"','"+data.list[i].BUSI_STEP3_NAME+"')\">"+data.list[i].SCORE+'%'+"</td>"
		    +"</tr>";
       total_BUSI_STEP_WEIGHT +=data.list[i].BUSI_STEP_WEIGHT*1;
       total_SCORE +=data.list[i].SCORE*1;
        }
         tableStr =tableStr+
           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
		     +"<td colspan='2'>合计</td>"
		     +"<td>"+toDecimal(total_BUSI_STEP_WEIGHT)+'%'+"</td>"
		     +"<td>&nbsp;&nbsp;</td>"
		     +"<td>&nbsp;&nbsp;</td>"
		     +"<td>&nbsp;&nbsp;</td>"
		     +"<td>&nbsp;&nbsp;</td>"
		     +"<td>&nbsp;&nbsp;</td>"
		     +"<td>"+toDecimal(total_SCORE)+'%'+"</td>"
		    +"</tr>";
      
             $('chartTable').innerHTML=tableStr;
		      autoRowSpan(mytbl,0,0);
		      
		      
		var expStr="<table width='900' border='0' cellpadding='0' cellspacing='0' class='tab_01'>";
           	+"<tr>"
			    +"<td  bgcolor='#cde5fd'><strong>客户服务诉求</strong></td>"
		    expStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
				       +"<td align=\"left\"><font style=\"font-weight:bold;\" >指标权重:</font>根据诉求指标重要度，通过层次分析法设定，该环节权重即为所有指标权重合计</td>"
				   +"</tr>";
            expStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
				       +"<td align=\"left\"><font style=\"font-weight:bold;\" >指标指向:</font>-1表示值越小发展越好，比如投诉率；1表示值越大发展越好，比如及时率</td>"
				   +"</tr>";
            expStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
				       +"<td align=\"left\"><font style=\"font-weight:bold;\" >预警值:</font>查询当月所在当年1月至上月的平均值</td>"
				   +"</tr>";
            expStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
				       +"<td align=\"left\"><font style=\"font-weight:bold;\" >当月值:</font>查询当月的值</td>"
				   +"</tr>";
            expStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
				       +"<td align=\"left\"><font style=\"font-weight:bold;\" >投诉占比:</font>该指标投诉率占总投诉率的比例</td>"
				   +"</tr>";
            expStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
				       +"<td align=\"left\"><font style=\"font-weight:bold;\" >偏离度:</font>指标指向*(当月值-预警值)/预警值</td>"
				   +"</tr>";
            expStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
				       +"<td align=\"left\"><font style=\"font-weight:bold;\" >扣分:</font><br/>1）投诉指标当月值<临界值，如偏离度<0，扣分=偏离度*投诉占比*权重，否则扣分=0;<br/>2）投诉指标当月值>=临界值，如偏离度<0，扣分=(偏离度-投诉占比)*权重，否则扣分=(0-投诉占比)*权重; <br/>3）非投诉指标如偏离度<0，扣分=偏离度*权重，否则扣分=0</td>"
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
        style:"position:relative;height:400px;width:400px;overflow: auto;padding: 0;margin: 0;" +
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
          //  div.style.width = target.offsetWidth+'px';
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
            formData.indId=ind_id;
            formData.step3Id=step3Id;
            formData.step3Name=step3Name;
            return formData;
        }
        }
    ]);
    
    return loadParam;
	
}

//查询
var queryData=function(){
    var loadParam=initParam();
    dwrCaller.addAutoAction("getIndPlValue","BusiStepGeneralAction.getIndPlValue",loadParam);
	excuteInitData();
}

//指标偏离值
var getIndPlValue=function(){
    var loadParam=initParam();
    dwrCaller.addAutoAction("getIndPlValue","BusiStepGeneralAction.getIndPlValue",loadParam);
    dwrCaller.executeAction("getIndPlValue",function(data){
	    $('chartdiv').innerHTML='';
    	if(data&&data.list.length>0){		
    	    chart=new FusionCharts(base+"/js/Charts/PowerCharts/Radar.swf" , "ChartId1_"+Math.random(), "900", "400", "0", "0");
            chart.setDataXML(data.chartMap);
            chart.render("chartdiv");
	      }
      });
}




//月份指标值  
var getMonthIndValue=function(){
  	var loadParam=initParam();
    dwrCaller.addAutoAction("getMonthIndValue","BusiStepGeneralAction.getMonthIndValue",loadParam);
    dwrCaller.executeAction("getMonthIndValue",function(data){
	    $('chartdiv').innerHTML='';
       if(data){
    	    chart=new FusionCharts(base+"/js/Charts/MSLine.swf",  "ChartId2_"+Math.random() , "900", "400", "0", "0");
            chart.setDataXML(data.chartMap);
            chart.render("chartdiv");
	      }
      });
}



//区域指标值
var getAreaIndValue=function(){
	var loadParam=initParam();
    dwrCaller.addAutoAction("getAreaIndValue","BusiStepGeneralAction.getAreaIndValue",loadParam);
    dwrCaller.executeAction("getAreaIndValue",function(data){
	    $('chartdiv').innerHTML='';
       if(data){
    		chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf","ChartId3_"+Math.random(), "900", "400", "0", "0");
            chart.setDataXML(data.chartMap);
            chart.render("chartdiv");
	      }
      });
}



var fchart=function(ind_id,ind_name){
	    step3Id=ind_id;
	    step3Name=ind_name;
	    //月环节
		 if(chart.id.split("_")[0]=="ChartId2"){
	    	    var loadParam=initParam();
			    dwrCaller.addAutoAction("getMonthIndValue","BusiStepGeneralAction.getMonthIndValue",loadParam);
			    dwrCaller.executeAction("getMonthIndValue",function(data){
				    $('chartdiv').innerHTML='';
			       if(data){
			    	    chart=new FusionCharts(base+"/js/Charts/MSLine.swf",  "ChartId2_"+Math.random() , "900", "400", "0", "0");
			            chart.setDataXML(data.chartMap);
			            chart.render("chartdiv");
				      }
			      });
		 }
		//区域
		if(chart.id.split("_")[0]=="ChartId3"){
				 	var loadParam=initParam();
				    dwrCaller.addAutoAction("getAreaIndValue","BusiStepGeneralAction.getAreaIndValue",loadParam);
				    dwrCaller.executeAction("getAreaIndValue",function(data){
					    $('chartdiv').innerHTML='';
				       if(data){
				    		chart=new FusionCharts(base+"/js/Charts/MSColumn2D.swf","ChartId3_"+Math.random(), "900", "400", "0", "0");
				            chart.setDataXML(data.chartMap);
				            chart.render("chartdiv");
					      }
				      });
		}
}

var autoRowSpan=function(tb,row,col){
        var lastValue="";
        var value="";
        var pos=1;
        for(var i=row;i<tb.rows.length;i++)
        {
            value = tb.rows[i].cells[col].innerText;
            if(lastValue == value)
            {
                tb.rows[i].deleteCell(col);
                tb.rows[i-pos].cells[col].rowSpan = tb.rows[i-pos].cells[col].rowSpan+1;
                pos++;
            }else{
                lastValue = value;
                pos=1;
            }
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
dhx.ready(userInit);