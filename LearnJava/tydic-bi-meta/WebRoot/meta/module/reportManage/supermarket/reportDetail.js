/******************************************************
 *Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *       reportDetail.js
 *Description：
 *       报表详情js
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，公共JS
 *Author:
 *       李国民
 *Date：
 *       12-3-16
 ********************************************************/

var repSupermarket = function() {
	/**
	 * 获取全局变量。
	 */
	dhtmlx.image_path = getDefaultImagePath();
	dhtmlx.skin = getSkin();
	var a_self = this;
	a_self.dwrCaller = new biDwrCaller();
	
	
	//查询报表详细
	a_self.dwrCaller.addAutoAction("getReportDetail", "RepSupermarketAction.getReportDetail");
	a_self.dwrCaller.isShowProcess("getReportDetail",true);

	/**
	 * 初始化类
	 */
	this.init = function() {
		a_self.setLayout();		//设置layout
		a_self.loadData();		//加载数据
		a_self.binding();		//绑定事件
		initForm();				//加载表单
	};
	
	this.setLayout = function(){
		var tableLayout = new dhtmlXLayoutObject(document.getElementById("container"), "2E");
		tableLayout.cells("a").attachObject("showReportDetail");
   		tableLayout.cells("a").setText("报表信息");
	    tableLayout.cells("a").setHeight(220);
	    tableLayout.cells("a").fixSize(false, true);
		tableLayout.cells("b").attachObject("showReport");
   		tableLayout.cells("b").setText("报表预览");
	    tableLayout.hideSpliter();
	    tableLayout.hideConcentrate();
	    
	    //设置加载报表预览
	    $('showReport').innerHTML 	= '<iframe id="reportOpen" name="reportOpen" align="middle" marginwidth=0 marginheight=0 hspace=0 ' +
	    'src="../showReport/showReport.jsp?reportId=<%=reportId %>&showTypeId=<%=showTypeId %>"  ' +
	    'frameborder=no width="100%"  height="100%"></iframe>';
	}
	
	this.loadData = function(){	
		a_self.dwrCaller.executeAction('getReportDetail',reportId,function(data){
 			if(data){
				$('reportName').innerHTML 		= data.REPORT_NAME;			//报表名称
				$('reportTypeName').innerHTML 	= data.REPORT_TYPE_NAME;	//报表分类名
				$('userNameCn').innerHTML 		= data.USER_NAMECN;			//创建人
				$('deptName').innerHTML 		= data.DEPT_NAME;			//创建部门
				$('startDate').innerHTML 		= data.START_DATE;			//生效时间
				$('showEffectTime').innerHTML 	= data.SHOW_EFFECT_TIME;	//有效时间
				$('reportKeyword').innerHTML 	= data.REPORT_KEYWORD;		//关键字
				$('reportNote').value 			= data.REPORT_NOTE;			//报表说明
				a_self.reportName = data.REPORT_NAME;
				a_self.auditType = data.AUDIT_TYPE;
			}
		});
		
	}
	
	//绑定事件
	this.binding = function() {
		//绑定报表收藏功能
		Tools.addEvent($("rptFav"),'click',function(){ 
			favorites(reportId,a_self.reportName,a_self.auditType);		
		});
	}
	
}

var genObj = function() {
	var instance = new repSupermarket();
	instance.init();
}
dhx.ready(genObj);