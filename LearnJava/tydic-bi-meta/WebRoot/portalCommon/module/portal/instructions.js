/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       batchImport.js
 *Description：
 *       通用门户--首页--公告信息列表
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       吴喜丽
 *Date：
 *       2012-06-13
 ********************************************************/
var init = function(){
	var listData;
	if(isWD){
		PortalInstructionAction.getWDInstructions({
	        async:false,
	        callback:function(data){
	        	listData = data;
	        }
	    });
	}else{
		
	}
}
dhx.ready(init);