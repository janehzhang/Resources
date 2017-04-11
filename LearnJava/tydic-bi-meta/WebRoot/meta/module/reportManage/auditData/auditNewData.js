
/******************************************************
 *Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 *All rights reserved.
 *
 *Filename：
 *        auditData.js
 *Description：
 *       审核最新数据
 *Dependent：
 *       dhtmlx.js，dwr 有关JS，dhtmxExtend.js。。。
 *Author:
 *        李国民
 ********************************************************/

/**
 * 审核最新数据
 */
var auditNewData = function() {
	
	/**
	 * 获取全局变量。
	 */
	dhtmlx.image_path = getDefaultImagePath();
	dhtmlx.skin = getSkin();
	
	var isM = 22;		//月标识
	var isD = 11;		//天表示
    var localVariable = "{LOCAL_CODE}";	//地域宏变量标识
	var macroVariable = ["{YY}","{YYYY}","{YYYYMM}","{YYYYMMDD}"]; //时间宏变量
	var code = "0000";		//地域全省标识
	var a_self = this;
	var mygrid;
	var codes;			//地域组，全省时用
	var zoneCount;		//有几个全省地区
	var instCount;		//全省时，有几个实体表
	var isLocal=false;	//是否为地域宏变量表类
	
	/**
	 * 定义全局变量
	 */
	a_self.dwrCaller = new biDwrCaller();

	/******定义action*****/
	//查询模型表信息
	a_self.dwrCaller.addAutoAction("getIssueInfo", "AuditDataAction.getIssueInfo");
	a_self.dwrCaller.isShowProcess("getIssueInfo",true);
	//查询模型审核信息
	a_self.dwrCaller.addAutoAction("getAuditInfo", "AuditDataAction.getAuditInfo");
	a_self.dwrCaller.isShowProcess("getAuditInfo",true);
	//查询实体表是否存在，且判断是否存在差异
	a_self.dwrCaller.addAutoAction("checkTableInst", "AuditDataAction.checkTableInst");
	a_self.dwrCaller.isShowProcess("checkTableInst",true);
	//查询实体表需要审核的数据
	a_self.dwrCaller.addAutoAction("getTableInstColName", "AuditDataAction.getTableInstColName");
	
    //查询实体表数据条件
	var params = new biDwrMethodParam();
	var dhxForm = new dhtmlXForm("form4Hidden");
    params.setParamConfig([
        {
            index:0,type:"fun",value:function(){
	            var formData=dhxForm.getFormData();
	            formData.issueId		=	issueId;
	            formData.localCode		=	$("localCode").value;
	            formData.tableName		=	$('tableInst').value.split(',')[0];
	            formData.tableOwner		=	a_self.tableOwner;
	            formData.dataSourceId	=	a_self.dataSourceId;
	            formData.dataDate		=	dataDate;
	            formData.dataPeriod		=	dataPeriod;
	            return formData;
	        }
        }
    ]);
    //查询实体表数据
	var instDataConverter = new dhtmxGridDataConverter({
		filterColumns:[]
	});
	a_self.dwrCaller.addAutoAction("getTableInstData", "AuditDataAction.getTableInstData",params);
	a_self.dwrCaller.addDataConverter("getTableInstData", instDataConverter);
	
	
	var auidtparams = new biDwrMethodParam();
    auidtparams.setParamConfig([
        {
            index:0,type:"fun",value:function(){
	            var aduit=dhxForm.getFormData();
	            aduit.auditLogId		=	a_self.auditLogId;
	            aduit.auditCfgId		=	a_self.auditCfgId;
	            aduit.dataDate			=	dataDate;
	            aduit.auditConclude		=	a_self.auditConclude;
	            aduit.showOpinion		=	$("showOpinion").value;
	            aduit.auditOpinion		=	$("auditOpinion").value;
	            aduit.localCode			=	$("localCode").value;
	            aduit.codes				=	codes;
	            return aduit;
	        }
        }
    ]);
	a_self.dwrCaller.addAutoAction("auditOperation", "AuditDataAction.auditOperation",auidtparams);
	
	
	/**
	 * 初始化类
	 */
	this.init = function() {
		a_self.serGrid(); 		//生成grid
		a_self.setDataDate(); 	//设置日期
		a_self.binding();		//绑定事件
		a_self.setLocal();		//设置地域
	};
	
//	//生成grid
	this.serGrid = function(){
		a_self.dwrCaller.executeAction('getTableInstColName',issueId,function(data) {
			var colAlias = "";		//头信息
			var colName = "";		//列名
			var colHeard = ""		//列头位置
			var colAlign = "";		//列位置
			var colBoole = "";		//列尺寸
			var colTypes = "";		//列类型
			var colSorts = "";
			var colWidth = "";
			var isSP = data.length>11;
			for(var i=0;i<data.length;i++){
				colAlias += data[i].COL_ALIAS+",";
				colName += Tools.tranColumnToJavaName(data[i].COL_NAME.toLowerCase(),true)+",";
				if(data[i].COL_BUS_TYPE == 1){
					colAlign += "right,";
				}else{
					colAlign += "left,";
				}
				colHeard += "center,";
				colBoole += "true,";
				colTypes += "ro,";
				colSorts += "na,";
                instDataConverter.filterColumns.push(Tools.tranColumnToJavaName(data[i].COL_NAME.toLowerCase(),true));
				colWidth += "120,";
			}
			colAlias = colAlias.substr(0,colAlias.length-1);
			colName = colName.substr(0,colName.length-1);
			colHeard = colHeard.substr(0,colHeard.length-1);
			colAlign = colAlign.substr(0,colAlign.length-1);
			colBoole = colBoole.substr(0,colBoole.length-1);
			colTypes = colTypes.substr(0,colTypes.length-1);
			colSorts = colSorts.substr(0,colSorts.length-1);
			colWidth = colWidth.substr(0,colWidth.length-1);
			
			mygrid = new dhtmlXGridObject("tableInst_show");	
			mygrid.setHeader(colAlias);
    		mygrid.setColumnIds(colName);
    		if(isSP){	//如果超过长度，这是每一列宽度
            	mygrid.setInitWidths(colWidth);
    		}
			mygrid.setColAlign(colAlign);
			mygrid.setHeaderAlign(colHeard);
			mygrid.enableResizing(colBoole);
			mygrid.enableTooltips(colBoole);
			mygrid.setColTypes(colTypes);
			mygrid.setColSorting(colSorts);
			mygrid.setEditable(false);	
			mygrid.init();
			mygrid.defaultPaging(20);
		});
		
		//为审核按钮绑定事件
		Tools.addEvent($("submit"),'click',function(){
			var localCode = $("localCode").value;
			if(isLocal && localCode == code){	
				//如果表类为地域宏变量表类，并且地域为全省时，验证实体表是否齐全
				if(zoneCount != instCount){
					dhx.confirm("地域所对应实体表不齐全，是否继续全部审核？",function(r){
						if(r){
							auditOperation();		//审核操作
						}
					});
				}
			}else{
				auditOperation();		//审核操作
			}
		});
	}
	
	//审核数据操作
	auditOperation = function(){
		var tem=document.getElementsByName("auditConclude");      
		for(var i=0;i<tem.length;i++)                      
		{
			if(tem[i].checked){
				a_self.auditConclude = tem[i].value;
			}
		}
		if(!$("auditOpinion").value){
			dhx.alert("请输入审核意见！");
			return;
		}
		a_self.dwrCaller.executeAction('auditOperation',function(data){
			if(data){
				dhx.alert("审核成功！");
				a_self.loadData();		//重新加载
			}else{
				dhx.alert("审核失败！请联系管理员！");
        		$('submit').disabled = "true";
			}
		});
	}
	
	//设置数据日期
	this.setDataDate = function(){
		if(!dataDate){	//如果数据日期不存在，则为审核最新数据
	    	var today = new Date();
			if(dataPeriod==isM){
				//按月时
		    	var year = today.getFullYear()+"";		//获取当前年份
		    	var month = today.getMonth()+"";	 	//获取当前月份(0-11,0代表1月)
	    		a_self.showYear = year;
	    		a_self.showMonth = month;	//保存为全局变量
	    		
		    	if(month.length==1){	//不足两位数，补够两位数
		    		month = "0"+month;
		    	}
	    		dataDate = year+month;
			}else{
				var tomorrow = new Date();
				tomorrow.setDate(today.getDate()+(maxDate*1));	//减去约定天数
		    	var year = tomorrow.getFullYear()+"";	//获取当前年份
		    	var month = tomorrow.getMonth()+"";	 	//获取当前月份(0-11,0代表1月)
				var day = tomorrow.getDate()+"";		//获取减去约定天数后的日期
				//按天
		    	if(month.length==1){	//不足两位数，补够两位数,因为是按照天查询，月份+1
		    		month = month*1+1;
		    		month = "0"+month;
		    	}
		    	if(day.length==1){
		    		day = "0"+day;
		    	}
	    		dataDate = year+month+day;
	    		a_self.showDataDate = year+"-"+month+"-"+day;
			}
		}else{  
			//如果数据日期存在，则为审核当前日期数据
			var tem = dataDate+"";	//转换成字符串
			if(dataPeriod==isM){
				//按月时
	    		a_self.showYear = tem.substr(0,4)*1;
	    		a_self.showMonth = tem.substr(4,2)*1;	//保存为全局变量
			}else{
				a_self.showDataDate = tem.substr(0,4)+"-"+tem.substr(4,2)+"-"+tem.substr(6,2);
			}
		}
		
		if(dataPeriod==isM){
			//按月时
			var yearOptions=["2010","2011","2012","2013","2014","2015","2016","2017","2018",
				"2019","2020","2021","2022","2023","2024"];
			var monthOptions=["1","2","3","4","5","6","7","8","9","10","11","12"];
			for(var m=0;m<yearOptions.length;m++){
				$("dataDate1").options.add(new Option(yearOptions[m]+"年", yearOptions[m]));
			}
			for(var m=0;m<monthOptions.length;m++){
				$("dataDate2").options.add(new Option(monthOptions[m]+"月", monthOptions[m]));
			}
			$("dataDate1").value = a_self.showYear;
			$("dataDate2").value = a_self.showMonth;
			$("showD").style.display="none";
			$("showM").style.display="block";
			
			//给下拉框绑定事件
		    Tools.addEvent($("dataDate1"),'change',function(){
				changeDateBySelect();		//重新加载
			});		
		    //给下拉框绑定事件
		    Tools.addEvent($("dataDate2"),'change',function(){
				changeDateBySelect();		//重新加载
			});
		}else{
			//按天时
			a_self.Calendar = new dhtmlXCalendarObject("dataDate");//绑定日期控件
			a_self.Calendar.setDateFormat("%Y-%m-%d");
			a_self.Calendar.hideTime();		//隐藏时间选择，只选择日期
			$("dataDate").value=a_self.showDataDate;
		    a_self.Calendar.loadUserLanguage('zh'); //将日历控件语言设置成中文
		    var today1 = new Date();
		    var tomorrow1 = new Date();
			tomorrow1.setDate(today1.getDate()+(maxDate*1)+1);	//减去约定天数(因为为负数，所以+
		    a_self.Calendar.setInsensitiveRange(tomorrow1,null);//设置可选最大日期
	    
		    //给时间控件绑定事件
		    Tools.addEvent(a_self.Calendar,'click',function(){
				changeDataDate();		//重新加载
			});
		}
	}
	
	//下拉列表选择值，重新加载数据
	changeDateBySelect = function(){
		var dataDate1 = $("dataDate1").value;
		var dataDate2 = $("dataDate2").value;
		if(dataDate2.length==1){
			dataDate2 = "0"+dataDate2;
		}
		dataDate = dataDate1+dataDate2+"";
		a_self.loadData();		//加载数据
	}
	
	//日期控件选择值，重新加载数据
	changeDataDate = function(){
		var dateEmp = $("dataDate").value;
		dataDate = dateEmp;
		while(dateEmp.indexOf("-" )!=-1) {
		     dateEmp = dateEmp.replace("-",""); 
		}
		dataDate = dateEmp;
		a_self.loadData();		//加载数据
	}
	
	//查询地域，根据审核模式查询地域值
	a_self.dwrCaller.addAutoAction("getZoneList", "AuditDataAction.getZoneList");
	a_self.dwrCaller.isShowProcess("getZoneList",true);
	//设置地域
	this.setLocal = function(){
		//查询地域，根据审核模式查询地域值
		a_self.dwrCaller.executeAction("getZoneList",auditProp,function(data){
			if(data){
				var obj=$('localCode');
				if(auditProp == 1){
					//如果为全省审，添加全省选项
					obj.add(new Option("全省",code)); 
				}
				zoneCount = data.length;
				codes = "";
				for(var i=0;i<data.length;i++){
					if(i==0){
						a_self.DATA_AREA = data[i].ZONE_CODE;
						codes += data[i].ZONE_CODE;
					}else{
						codes += ","+data[i].ZONE_CODE;
					}
					obj.add(new Option(data[i].ZONE_NAME,data[i].ZONE_CODE)); 
				}
				a_self.setIssueInfo();		//加载模型信息
			}
		});
		//给下拉框绑定事件
		Tools.addEvent($("localCode"),'change',function(){
			a_self.DATA_AREA = $('localCode').value;
			a_self.loadData();		//加载数据
		});	
	}
	
	//绑定事件
	this.binding = function(){
		//给实体表下拉列表绑定事件
		Tools.addEvent($("tableInst"),'change',function(){
			a_self.DATA_AREA = $('tableInst').value.split(',')[1];
			a_self.checkTaleInst();	//验证实体表
		});	
	}
	
	//查询模型信息
	this.setIssueInfo = function(){
		//查询模型表信息
		a_self.dwrCaller.executeAction('getIssueInfo',issueId,function(data) {
			$('tableInfo').innerHTML 	= data.DATA_SOURCE_NAME+"."+data.TABLE_OWNER+"."+data.TABLE_NAME;
			$('dataPeriod').innerHTML 	= data.SHOW_DATA_PERIOD?data.SHOW_DATA_PERIOD:"";
			if(dataPeriod==isD){
				$('maxDate').innerHTML 		= data.MAX_DATE?data.MAX_DATE:"";
			}else if(dataPeriod==isM){
				$('maxDate').innerHTML		= "前一月";
			}
			$('tableAlias').innerHTML 	= data.TABLE_ALIAS?data.TABLE_ALIAS:"";
			$('issueNote').value 		= data.ISSUE_NOTE?data.ISSUE_NOTE:"";		//审核表id
			a_self.tableId 		= data.TABLE_ID?data.TABLE_ID:"";					//表类id
			a_self.tableName 	= data.TABLE_NAME?data.TABLE_NAME:"";				//表类名
			a_self.tableOwner 	= data.TABLE_OWNER?data.TABLE_OWNER:"";				//所属用户
			a_self.dataSourceId = data.DATA_SOURCE_ID!=null?data.DATA_SOURCE_ID:"";	//数据源id
			a_self.auditCfgId 	= data.AUDIT_CFG_ID?data.AUDIT_CFG_ID:"";			//模型范围表id
			
			a_self.loadData();		//加载数据
		});
	}
	
	//查询审核模型信息
	this.loadData = function() {
		//初始化审核信息值
		$('auditMark').innerHTML	= "";
		$('showOpinion').value 		= "";
		$('auditOpinion').value 	= "";
		$('show').checked = "checked";
		a_self.auditLogId 	= "";	//审核表id
		a_self.getTableInstName();	//得到实体表名
	};
	
	//得到实体表类名
	this.getTableInstName = function() {
		var staIndex = a_self.tableName.indexOf("{");
		var endIndex = a_self.tableName.indexOf("}");
		var tableInstName = a_self.tableName;	//实体表名
		if((staIndex!=-1&&endIndex!=-1)&&endIndex>staIndex){
			//如果表类名中存在'{'以及'}',并且}的下标大于{下标时，表示该表类为宏变量表类
			for(var i=0;i<macroVariable.length;i++){
				var macro = macroVariable[i];
				if(tableInstName.indexOf(macro)!=-1){
					var temDate = dataDate+"";
					switch (macro) {
				 		case '{YY}':
				 			temDate = temDate.substr(2,4);
				            break;
				  		case '{YYYY}':
				 			temDate = temDate.substr(0,4);
				            break;
				  		case '{YYYYMM}':
				 			temDate = temDate.substr(0,6);
				            break;
				  		case '{YYYYMMDD}':
				            break;
					}
					tableInstName = tableInstName.replace(macro,temDate);
					break;
				}
			}
			
			//判断该表类是否包含地域宏变量
			if(tableInstName.indexOf(localVariable)!=-1){
				//匹配成功，则该表类为地域宏变量表类
				var localCode = $('localCode').value;
				if(localCode!=code){
					//如果地域不为全省，则匹配出唯一实体表
					a_self.DATA_AREA = localCode;
					tableInstName = tableInstName.replace(localVariable,localCode);
					isLocal = true; //提交时验证开关
				}else{
					isLocal = false;
				}
			}
		}
		a_self.setTableInst(tableInstName);	//设置实体表列表
	}
	
	//查询实体表列表
	a_self.dwrCaller.addAutoAction("getTableInstList", "AuditDataAction.getTableInstList");
	a_self.dwrCaller.isShowProcess("getTableInstList",true);
	//设置实体表列表
	this.setTableInst = function(tableInstName){
		$('tableInst').options.length=0;	//清空实体表列表
		if(tableInstName.indexOf(localVariable)!=-1){
			//匹配成功，则表示表类为地域宏变量且地域选择为全省
			var tableName = tableInstName.replace(localVariable,"%");
			a_self.dwrCaller.executeAction('getTableInstList',
				tableName,a_self.tableId,function(data) {
				if(data){
					codes = "";
					instCount = data.length;
					for(var i=0;i<data.length;i++){
						$('tableInst').add(new Option(
							data[i].TABLE_NAME,data[i].TABLE_NAME+","+data[i].DATA_AREA));
						if(i==0){
							a_self.DATA_AREA = data[i].DATA_AREA;
							codes += data[i].DATA_AREA;
						}else{
							codes += ","+data[i].DATA_AREA;
						}
					}
					a_self.checkTaleInst();	//验证实体表
				}else{
					dhx.alert("实体表不存在，请联系数据管理人员！");
				}
			});
		}else{
			//则表示只有唯一实体表，添加到实体表列表中
			$('tableInst').add(new Option(tableInstName,tableInstName+","+a_self.DATA_AREA)); 
			a_self.checkTaleInst();	//验证实体表
		}
	}
	
	//验证实体表是否存在，是否存在差异
	this.checkTaleInst = function(){
		var tableInstName = $('tableInst').value.split(',')[0];
		//查询该实体表是否存在，并验证该实体表是否正确
		a_self.dwrCaller.executeAction('checkTableInst',
			tableInstName,a_self.tableId,function(data) {
    			mygrid.clearAll();		//清空数据
				if(data==1){	//验证通过
    				$('submit').disabled = "";
					a_self.setAuditInfo();	//加载审核信息
				}else if(data==2){	//存在差异
					$('submit').disabled = "true";
					dhx.alert("实体表与表类之间存在差异，请联系数据管理人员！")
				}else if(data==3){	//实体表不存在
    				$('submit').disabled = "true";
					dhx.alert("对应实体表不存在，请联系数据管理人员！")
				}else{
    				$('submit').disabled = "true";
				}
			}
		);
	}
	
	//加载审核信息
	this.setAuditInfo = function(){
		//查询审核信息
		a_self.dwrCaller.executeAction('getAuditInfo',issueId,dataDate,a_self.DATA_AREA,function(data) {
			if(data){
				$('auditMark').innerHTML 	= data.AUDIT_MARK?HTMLEncode(data.AUDIT_MARK):"";
				$('showOpinion').value 		= data.SHOW_OPINION?data.SHOW_OPINION:"";
				$('auditOpinion').value 	= "";
				if(data.AUDIT_CONCLUDE!=null&&data.AUDIT_CONCLUDE==0){
					//当审核结论不为null，且只为0时，
					$('noShow').checked = "checked";
				}else{
					$('show').checked = "checked";
				}
				a_self.auditLogId 	= data.AUDIT_LOG_ID?data.AUDIT_LOG_ID:"";			//审核表id
			}
			a_self.getTableInstColData();	//得到实体表数据	定到grid上
		});
		
	}
	
	//得到实体表数据绑定到grid上
	this.getTableInstColData = function(){
		mygrid.load(a_self.dwrCaller.getTableInstData, "json");
	}
	
}

var genObj = function() {
	var instance = new auditNewData();
	instance.init();
}
dhx.ready(genObj);
