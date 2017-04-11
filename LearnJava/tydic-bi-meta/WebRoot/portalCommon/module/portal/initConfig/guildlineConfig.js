/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       guildlineConfig.js
 *Description：
 *       通用门户指标配置折线图
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js
 *Author:
 *       王晶
 *Date：
 *       2012-05-17
 ********************************************************/
/**
 * 获取全局变量。
 */
var tableId = tabId;
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var queryForm = null;//查询的form
var tabbar =null; 
/**
  * init 函数
  */
/*
 * 加载报表的处理方法
 */
var laodRepData = function(){
	var options = [];
    var selectIndex = 0;
    var tabName = "";
    ReportConfigAction.getReportTabMes(null,{
    	async:false,
    	callback:function(list) {
	    	for(var i = 0; i < list.length; i++){
	    		if(tabId == null || tabId == "null")
	    			tabId = list[selectIndex].TAB_ID;
	    		if(1*tabId == list[i].TAB_ID)
	    			selectIndex = i;
	    		tableId = tabId;
	    		tabName = list[selectIndex].TAB_NAME;
	    		options.push({text:list[i].TAB_NAME,value:list[i].TAB_ID});
	    	}
    	}
    });
    queryForm.getCombo("repName").addOption(options);
    queryForm.getCombo("repName").selectOption(selectIndex,false,false);
}
/**
  *根据报表加载报表的具体解释 
  */
var loadDate = function(tableId){
	ReportConfigAction.getReportTabById(tableId,{callback:function(res){
		var repType = "";
		if(res.RPT_TYPE == "1")
			repType = "日报";
		else if(res.RPT_TYPE == "2")
			repType = "月报";
		$('_repType').innerText = repType;
		$('_orderId').innerText = res.ORDER_ID;
		$('_rolldownLayer').innerText = res.ROLLDOWN_LAYER;
		$('_defaultGrid').innerText = res.DEFAULT_GRID;
		var tabDesc = "";
		if(res.TAB_DESC != null && res.TAB_DESC != "null")
			tabDesc = res.TAB_DESC;
		$('_tabDesc').innerText = tabDesc;
	}});
}

var guildlineConfigInit = function(){
	  var configLayout = new dhtmlXLayoutObject("container", "2E"); //页面整体布局,分为了三块
        configLayout.cells("a").setHeight(180);
        configLayout.cells("a").fixSize(false, true);
        configLayout.cells("b").setText("指标折线图属性");
        configLayout.cells("b").fixSize(false, true);
        configLayout.hideConcentrate();
        configLayout.hideSpliter();
        var sunLayout = configLayout.cells("a").attachLayout("2E")  //将页面的第一块分成了上下两块
        sunLayout.cells("a").setText("指标折线图配置");
        sunLayout.cells("a").setHeight(70);
        sunLayout.cells("a").fixSize(false, true);
        sunLayout.cells("b").hideHeader();
        sunLayout.hideConcentrate();
        sunLayout.hideSpliter();
        var queryData = [{type:"setting",position: "label-left"},
    	                 {type:"block",list:[     
    	            	                 {type:"combo",label:"选择报表：",name:'repName',inputWidth:120,inputHeight:17,offsetLeft:10}
                                        ]}
    	
       ]
       queryForm = sunLayout.cells("a").attachForm(queryData);
       queryForm.getCombo("repName").enableFilteringMode(false);
       laodRepData();
      queryForm.attachEvent("onChange", function(id){
    	  if(id=='repName'){
    	   tableId = queryForm.getCombo("repName").getSelectedValue();
           loadDate(tableId);
           tabbar = configLayout.cells("b").attachTabbar(); //选项卡,加载指标折线属性与指标解释
           tabbar.setHrefMode("iframes-on-demand");
           tabbar.addTab("guildlineAtt", "指标折线属性", "120px");
           tabbar.addTab("guildlineInfo", "指标解释", "120px");
           tabbar.setContentHref("guildlineAtt",urlEncode(getBasePath()+"/portalCommon/module/portal/initConfig/guildlineAtt.jsp?reportId="+queryForm.getCombo("repName").getSelectedValue()));
           tabbar.setContentHref("guildlineInfo",urlEncode(getBasePath()+"/portalCommon/module/portal/initConfig/guildlineInfo.jsp?reportId="+queryForm.getCombo("repName").getSelectedValue()));
           tabbar.setTabActive("guildlineAtt");
    	  }
      });
      var repInfo = sunLayout.cells("b").attachObject("_repTabMes");  //加载报表解释
      loadDate(tabId);
      tabbar = configLayout.cells("b").attachTabbar(); //选项卡,加载指标折线属性与指标解释
	  tabbar.setHrefMode("iframes-on-demand");
      tabbar.addTab("guildlineAtt", "指标折线属性", "120px");
      tabbar.addTab("guildlineInfo", "指标解释", "120px");
      tabbar.setContentHref("guildlineAtt",urlEncode(getBasePath()+"/portalCommon/module/portal/initConfig/guildlineAtt.jsp?reportId="+queryForm.getCombo("repName").getSelectedValue()));
      tabbar.setContentHref("guildlineInfo",urlEncode(getBasePath()+"/portalCommon/module/portal/initConfig/guildlineInfo.jsp?reportId="+queryForm.getCombo("repName").getSelectedValue()));
      tabbar.setTabActive("guildlineAtt");
//      tabbar.attachEvent("onSelect", function (id, last_id) {
//        if (id != null) {
//            if (id == "guildlineInfo"){
//            	  tabbar.setContentHref("guildlineInfo",urlEncode(getBasePath()+"/portalCommon/module/portal/initConfig/guildlineInfo.jsp?reportId="+queryForm.getCombo("repName").getSelectedValue()));
//            	  tabbar.setTabActive("guildlineInfo");
//            }
//            return true;
//        } else
//            return false;
//    });
} 
dhx.ready(guildlineConfigInit);