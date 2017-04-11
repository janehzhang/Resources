/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       
 *Description：
 *       用户维护模块所有JS
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author: 颜海东
 *
 *Finished：
 *       2011-09-23-10-51
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
//var user=getSessionAttribute("user");
var dwrCaller=new biDwrCaller();
//当前系统的主页Path
var base = getBasePath();

var dateTime= $("dateTime").value;
dwrCaller.addAutoAction("getStepInstallDay","StepInstallAction.getStepInstallDay",dateTime);

/**
 * 用户页面初始化方法
 */
var indexInit=function(){
	  dwrCaller.executeAction("getStepInstallDay",function(data){
		          if(data){
		        	     var temNum=0;   
                    	 $("widthNum_0").innerHTML=data.avgTime_num1;
                    	 $("num_0").innerHTML=data.num1;
                    	 if(data.avgTime_num1>50){
                    		 temNum=formatFloat(48+(data.avgTime_num1-50)/150*48,4)+'%';
                    	 }else{
                    		 temNum=formatFloat(data.avgTime_num1/50*48,4)+'%';
                    	 }
                    	 $("widthNum_0").style.width=temNum;
                    	 
                    	 $("widthAvg_0").innerHTML=data.avgTime_lastNum1;
                    	 $("avg_0").innerHTML=formatFloat(data.avg1,0);
                    	 if(data.avgTime_lastNum1>50){
                    		 temNum=formatFloat(48+(data.avgTime_lastNum1-50)/150*48,4)+'%';
                    	 }else{
                    		 temNum=formatFloat(data.avgTime_lastNum1/50*48,4)+'%';
                    	 }
                    	 $("widthAvg_0").style.width=temNum;
                    	 
                    	 $("widthNum_1").innerHTML=data.avgTime_num2;
                    	 $("num_1").innerHTML=data.num2;
                    	 if(data.avgTime_num2>50){
                    		 temNum=formatFloat(48+(data.avgTime_num2-50)/150*48,4)+'%';
                    	 }else{
                    		 temNum=formatFloat(data.avgTime_num2/50*48,4)+'%';
                    	 }
                    	 $("widthNum_1").style.width=temNum;
                    	 
                    	 $("widthAvg_1").innerHTML=data.avgTime_lastNum2;
                    	 $("avg_1").innerHTML=formatFloat(data.avg2,0);
                    	 if(data.avgTime_lastNum2>50){
                    		 temNum=formatFloat(48+(data.avgTime_lastNum2-50)/150*48,4)+'%';
                    	 }else{
                    		 temNum=formatFloat(data.avgTime_lastNum2/50*48,4)+'%';
                    	 }
                    	 $("widthAvg_1").style.width=temNum;
  
                    	 $("widthNum_2").innerHTML=data.avgTime_num3;
                    	 $("num_2").innerHTML=data.num3;
                    	 if(data.avgTime_num3>50){
                    		 temNum=formatFloat(48+(data.avgTime_num3-50)/150*48,4)+'%';
                    	 }else{
                    		 temNum=formatFloat(data.avgTime_num3/50*48,4)+'%';
                    	 }
                    	 $("widthNum_2").style.width=temNum;
                    	 
                    	 $("widthAvg_2").innerHTML=data.avgTime_lastNum3;
                    	 $("avg_2").innerHTML=formatFloat(data.avg3,0);
                    	 if(data.avgTime_lastNum3>50){
                    		 temNum=formatFloat(48+(data.avgTime_lastNum3-50)/150*48,4)+'%';
                    	 }else{
                    		 temNum=formatFloat(data.avgTime_lastNum3/50*48,4)+'%';
                    	 }
                    	 $("widthAvg_2").style.width=temNum;
                    	 
                    	 $("widthNum_3").innerHTML=data.avgTime_num4;
                    	 $("num_3").innerHTML=data.num4;
                    	 if(data.avgTime_num4>50){
                    		 temNum=formatFloat(48+(data.avgTime_num4-50)/150*48,4)+'%';
                    	 }else{
                    		 temNum=formatFloat(data.avgTime_num4/50*48,4)+'%';
                    	 }
                    	 $("widthNum_3").style.width=temNum;
                    	 
                    	 $("widthAvg_3").innerHTML=data.avgTime_lastNum4;
                    	 $("avg_3").innerHTML=formatFloat(data.avg4,0);
                    	 if(data.avgTime_lastNum4>50){
                    		 temNum=formatFloat(48+(data.avgTime_lastNum4-50)/150*48,4)+'%';
                    	 }else{
                    		 temNum=formatFloat(data.avgTime_lastNum4/50*48,4)+'%';
                    	 }
                    	 $("widthAvg_3").style.width=temNum;
                    	
                    	 //合计 
                    	 $("num_4").innerHTML=formatFloat(data.avgTime_num1 +data.avgTime_num2+data.avgTime_num3+data.avgTime_num4,2);
                    	 $("avg_4").innerHTML=formatFloat(data.avgTime_lastNum1+data.avgTime_lastNum2+data.avgTime_lastNum3+data.avgTime_lastNum4,2);
                    	 
                    	 $("num_5").innerHTML=formatFloat(data.num1,0)+formatFloat(data.num2,0)+formatFloat(data.num3,0)+formatFloat(data.num4,0);
                    	 $("avg_5").innerHTML=formatFloat(data.avg1,0)+formatFloat(data.avg2,0)+formatFloat(data.avg3,0)+formatFloat(data.avg4,0);
                    	 
                    	 /* 订单异常 */ 
                    	 $("orderExcepiton_num1").innerHTML=data.orderExcepiton_num1;
                    	 if(data.orderExcepiton_num1>50){
                    		 temNum=formatFloat(48+(data.orderExcepiton_num1-50)/150*48,4)+'%';
                    	 }else{
                    		 temNum=formatFloat(data.orderExcepiton_num1/50*48,4)+'%';
                    	 }
                    	 $("orderExcepiton_num1").style.width=temNum;
                    	 
                    	 $("orderExcepiton_avg1").innerHTML=formatFloat(data.orderExcepiton_avg1,0);
                    	 if(data.orderExcepiton_avg1>50){
                    		 temNum=formatFloat(48+(data.orderExcepiton_avg1-50)/150*48,4)+'%';
                    	 }else{
                    		 temNum=formatFloat(data.orderExcepiton_avg1/50*48,4)+'%';
                    	 }
                    	 $("orderExcepiton_avg1").style.width=temNum;
                    	 
                    	 $("orderExcepiton_num2").innerHTML=data.orderExcepiton_num2;
                    	 $("orderExcepiton_avg2").innerHTML=formatFloat(data.orderExcepiton_avg2,0);
                    	 
                    	 $("orderExcepiton_num3").innerHTML=data.orderExcepiton_num3;
                    	 if(data.orderExcepiton_num3>50){
                    		 temNum=formatFloat(48+(data.orderExcepiton_num3-50)/150*48,4)+'%';
                    	 }else{
                    		 temNum=formatFloat(data.orderExcepiton_num3/50*48,4)+'%';
                    	 }
                    	 $("orderExcepiton_num3").style.width=temNum;
                    	 
                    	 $("orderExcepiton_avg3").innerHTML=formatFloat(data.orderExcepiton_avg3,0);
                    	 if(data.orderExcepiton_avg3>50){
                    		 temNum=formatFloat(48+(data.orderExcepiton_avg3-50)/150*48,4)+'%';
                    	 }else{
                    		 temNum=formatFloat(data.orderExcepiton_avg3/50*48,4)+'%';
                    	 }
                    	 $("orderExcepiton_avg3").style.width=temNum;
                    	 
                    	 $("orderExcepiton_num4").innerHTML=data.orderExcepiton_num4;
                    	 $("orderExcepiton_avg4").innerHTML=formatFloat(data.orderExcepiton_avg4,0);
                    	 
                         $("orderExcepiton_num5").innerHTML=data.orderExcepiton_num5;
                    	 $("orderExcepiton_avg5").innerHTML=formatFloat(data.orderExcepiton_avg5,0);
                }
	  });
}
var queryBtn = document.getElementById("queryBtn");
var queryData=function(){
	var dateTime= $("dateTime").value;
	var indexType= $("indexType").value;
	dwrCaller.addAutoAction("getStepInstallDay","StepInstallAction.getStepInstallDay",dateTime,indexType);
	indexInit();
}


function formatFloat(src, pos)
{
    return Math.round(src*Math.pow(10, pos))/Math.pow(10, pos);
}

dhx.ready(indexInit);
  