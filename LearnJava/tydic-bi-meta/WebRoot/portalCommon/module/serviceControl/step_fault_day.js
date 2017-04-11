/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       
 *Description：
 *       宽带故障申告全流程各环节管控（日报）JS
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author: 全霞
 *
 *Finished：
 *       2012-11-24-10-51
 *Modified By：
 *
 *Modified Date:
 *
 *Modified Reasons:

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
var indexType= $("indexType").value;
var dateTime= $("dateTime").value;
dwrCaller.addAutoAction("getFaultInstallDay","StepFaultAction.getStepFaultDay",dateTime,indexType);

/**
 * 用户页面初始化方法
 */
var indexInit=function(){
	  dwrCaller.executeAction("getFaultInstallDay",function(data){
		          if(data){
                    	 $("cmpl_num").innerHTML=data.cmpl_num; 
                    	 $("cmpl_last_num").innerHTML=data.cmpl_last_num; 
                    	 $("deal_num").innerHTML=data.deal_num; 
                    	 $("deal_last_num").innerHTML=data.deal_last_num; 
                    	 $("dispach_num").innerHTML=data.dispach_num; 
                    	 $("dispach_last_num").innerHTML=data.dispach_last_num; 
                    	 $("overtime_num").innerHTML=data.overtime_num; 
                    	 $("overtime_last_num").innerHTML=data.overtime_last_num; 
                    	 $("hangup_num").innerHTML=data.hangup_num; 
                    	 $("hangup_last_num").innerHTML=data.hangup_last_num;
                    	 $("return_num").innerHTML=data.return_num;
                    	 $("return_last_num").innerHTML=data.return_last_num;
                    	 
                }
	  });
}
var queryBtn = document.getElementById("queryBtn");
var queryData=function(){
	var indexType= $("indexType").value;
	var dateTime= $("dateTime").value;
	dwrCaller.addAutoAction("getFaultInstallDay","StepFaultAction.getStepFaultDay",dateTime,indexType);
	indexInit();
}

function formatFloat(src, pos)
{
    return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);
}

dhx.ready(indexInit);
  