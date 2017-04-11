dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var globZoneTree = null;
var first_param_Len = 0;
var second_param_Len = 0;
var third_param_Len = 0;
var fourth_param_Len = 0;
var fifth_param_Len = 0;

var MultDimenAnaly=function(){
	
    this.init = function(){
    	
    	MultDimenAnalyAction.clear(function(){});//清楚条件缓存
    	
    	first_param_Len = 0;
    	second_param_Len = 0;
    	third_param_Len = 0;
    	fourth_param_Len = 0;
    	fifth_param_Len = 0;
    	
    	$(".td_2").multiselect({
            noneSelectedText: "请选择",
            checkAllText: "全选",
            uncheckAllText: '全不选',
            selectedList:20,
            buttonWidth:'60px'
        });
    	$("#div2").hide();
    	$("#ods_link").click(function(){
    		var menuId = $(this).attr("sub2MenuID");
	      	var menuName = $(this).attr("sub2MenuName");
		    var menuUrl=$(this).attr("menuUrl"); 
		    parent.openTreeTab("","高级统计分析报表 ", "http://132.121.64.43:9595/EDA-EP/myDocument/modelQuery!toModelQueryPage.action?maxZq=201605&modelList_id=213fdd5617f94dec9ed63c1376446062&account="+userNameen+"&flag=&systemId=kffx&corpId=440&userName="+userNamecn+"&modelType=1", 'top');
    	});
    	$('.close').click(function(){
    		$('.theme-popover-mask').fadeOut(100);
    		$('.theme-popover').slideUp(200);
    	});
    	
    	$("#local").click(function(){
    		$("#bdts").attr("src","images/bdts.png");
    		$("#yjts").attr("src","images/yjts1.png");
    		$("#div1").show();
    		$("#div2").hide();
    	});
    	$("#no_local").click(function(){
    		$("#yjts").attr("src","images/yjts.png");
    		$("#bdts").attr("src","images/bdts1.png");
    		$("#div1").hide();
    		$("#div2").show();
    	});
    	//自定义时间查询
    	$('#queryData').click(function(){
    		$("#date").val($("#txtBeginDate").val()+"&"+$("#txtEndDate").val());
    		multDimenAnaly.getAutoDefDateData($("#txtBeginDate").val()+"&"+$("#txtEndDate").val());
    	});
    	
		//第五象限，考核维度，点击“放大镜”按钮弹出窗口显示当前该窗口所有数据
		$("#img_khwd").click(function(){
	    	$("#detail").empty();
	    	$("#detail").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");

			var FirstViewEntity = new Object();
			FirstViewEntity.date = $("#date").val();
			FirstViewEntity.window_id = 5;
			FirstViewEntity.is_top5="false";
			
			var value = $("input[name='asse']:checked").val();
	    	if(value==1){
	    		$('#title').html("显示全部-考核维度-按省公司");
	    		FirstViewEntity.w5_x_name = "按省公司";
	    	}else{
		    	$('#title').html("考核维度-显示全部-按地市");
		    	FirstViewEntity.w5_x_name = "按地市";
	    	}
	    	
	    	MultDimenAnalyAction.getAllDataFromWindows(FirstViewEntity,function(res){
	    		multDimenAnaly.renderCharts(res);
	    	});
	    	
			$('.theme-popover-mask').fadeIn(100);
			$('.theme-popover').slideDown(200);
    	});
    	
    	//第二窗口
    	$("#p_2_2").empty();
    	$("#p_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第一窗口
    	$("#p_1_2_2").empty();
    	$("#p_1_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第三窗口
    	$("#p_3_2").empty();
    	$("#p_3_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第四窗口
    	$("#p_4_2_2").empty();
    	$("#p_4_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第五窗口
    	$("#p_5_2_2").empty();
    	$("#p_5_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");

    	
    	//第一象限默认数据加载
		var FirstViewEntity = new Object();
    	FirstViewEntity.date = $("#date").val();
    	FirstViewEntity.type1 = 1;
    	FirstViewEntity.map_level = 2;
    	FirstViewEntity.chartType = 1;
    	FirstViewEntity.view_id=1;
    	MultDimenAnalyAction.getFirstViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    	
		
    	
    	//第二象限默认数据加载
		var FirstViewEntity = new Object();
    	FirstViewEntity.date = $("#date").val();
    	FirstViewEntity.type2 = 2;
    	FirstViewEntity.map_level = 2;
    	FirstViewEntity.view_id=2;
    	MultDimenAnalyAction.getSecondViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    	
//    	MultDimenAnalyAction.getPicXmlData($("#date").val(),"2","一级产品","投诉量",function(res){
//    		multDimenAnaly.buildPic(res, "p_2_2", "MSCombi2D.swf");
//    	});
    	
//    	MultDimenAnalyAction.getPicXmlData($("#date").val(),"3","一级表象","投诉量",function(res){
//    		multDimenAnaly.buildPic(res, "p_3_2", "MSCombi2D.swf");
//    	});
    	
    	//第三象限默认数据加载
		var FirstViewEntity = new Object();
    	FirstViewEntity.date = $("#date").val();
    	FirstViewEntity.type3 = 3;
    	FirstViewEntity.map_level = 2;
    	FirstViewEntity.view_id=3;
    	MultDimenAnalyAction.getThirdViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    	
//    	MultDimenAnalyAction.getPieXmlData($("#date").val(),"48", null, function(res){
//    		multDimenAnaly.buildPic(res, "p_4_2_2", "../../../js/Charts/Pie2D.swf");
//    	});
    	
    	//第四象限默认数据加载
		var FirstViewEntity = new Object();
    	FirstViewEntity.date = $("#date").val();
    	FirstViewEntity.type4 = 48;
    	FirstViewEntity.map_level = 2;
    	FirstViewEntity.view_id=4;
    	MultDimenAnalyAction.getFourthViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    	
//    	MultDimenAnalyAction.getPicXmlData($("#date").val(),"51","考核单位","投诉量",function(res){
//    		multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
//    	});
    	
    	//第五象限默认数据加载
		var FirstViewEntity = new Object();
    	FirstViewEntity.date = $("#date").val();
    	FirstViewEntity.type5 = 51;
    	FirstViewEntity.map_level = 2;
    	FirstViewEntity.view_id=5;
    	MultDimenAnalyAction.getFifthViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    	
//    	MultDimenAnalyAction.getMapXmlData($("#date").val(),"1",function(res){
//    		multDimenAnaly.buildPic(res, "p_1_2_2", "FCMap_GuangDong.swf");
//    	});
    	

    	//第二象限
		var secondviewAnaly = new secondViewAnaly();
		secondviewAnaly.getData();
		
		//第三象限
		var thirdview = new thirdViewAnaly();
		thirdview.getData();
		
		//第五象限
		var fifthView = new fifthViewAnaly();
		fifthView.getData();
		
		//第一象限
		var firstView = new firstViewAnaly();
		firstView.getData();

		//第四象限
		var fourthView = new fourthViewAnaly();
		fourthView.getData();
		
		//可视化视图，统计时间选择，即页面顶部选择“上月”、“上周”、“昨日”
		$("#selectDate").bind("change",function(){
			if($("#selectDate").val()=="autoDefine"){
				//标签显示隐藏控制
				var startTime = document.getElementById("startTime");
				if(startTime.style.visibility=="hidden"||startTime.style.visibility==""){
					startTime.style.visibility="visible";
				}
				//标签显示隐藏控制
				var txtBeginDate = document.getElementById("txtBeginDate");
				if(txtBeginDate.style.visibility=="hidden"||txtBeginDate.style.visibility==""){
					txtBeginDate.style.visibility="visible";
				}
				//标签显示隐藏控制
				var endTime = document.getElementById("endTime");
				if(endTime.style.visibility=="hidden"||endTime.style.visibility==""){
					endTime.style.visibility="visible";
				}
				//标签显示隐藏控制
				var txtEndDate = document.getElementById("txtEndDate");
				if(txtEndDate.style.visibility=="hidden"||txtEndDate.style.visibility==""){
					txtEndDate.style.visibility="visible";
				}
				//标签显示隐藏控制
				var queryData = document.getElementById("queryData");
				if(queryData.style.visibility=="hidden"||queryData.style.visibility==""){
					queryData.style.visibility="visible";
				}	
			}else{
				//标签显示隐藏控制
				var startTime = document.getElementById("startTime");
				if(startTime.style.visibility=="visible"||startTime.style.visibility==""){
					startTime.style.visibility="hidden";
				}
				//标签显示隐藏控制
				var txtBeginDate = document.getElementById("txtBeginDate");
				if(txtBeginDate.style.visibility=="visible"||txtBeginDate.style.visibility==""){
					txtBeginDate.style.visibility="hidden";
				}
				//标签显示隐藏控制
				var endTime = document.getElementById("endTime");
				if(endTime.style.visibility=="visible"||endTime.style.visibility==""){
					endTime.style.visibility="hidden";
				}
				//标签显示隐藏控制
				var txtEndDate = document.getElementById("txtEndDate");
				if(txtEndDate.style.visibility=="visible"||txtEndDate.style.visibility==""){
					txtEndDate.style.visibility="hidden";
				}
				//标签显示隐藏控制
				var queryData = document.getElementById("queryData");
				if(queryData.style.visibility=="visible"||queryData.style.visibility==""){
					queryData.style.visibility="hidden";
				}
				$("#date").val($("#selectDate").val());
				multDimenAnaly.init();
			}
		});
		

		
    };
    
    //钻取数据执行的方法
    this.show = function(par, level, title, type){
//    	alert(multDimenAnaly.ascii2native(title));
    	if(null == par){
    		return;
    	}
    	if(level == 3){
    		return;
    	}
    	if(type == 51 || type == 52){
    		var asse = $("input[name='asse']:checked").val();
	    	if(asse == 1){
				MultDimenAnalyAction.getPieAsseData($("#date").val(),"51", "考核单位","投诉量", screen, function(res){
					if(res.indexOf("<set value=") < 0){
		    			$('#p_5_2_2').html("无数据...");
		    		}else{
		    			multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
		    		}
		    		
		    	});
			}else if(asse == 2){
				MultDimenAnalyAction.getPieAsseData($("#date").val(),"52","地市","投诉量", screen,function(res){
					if(res.indexOf("<set value=") < 0){
		    			$('#p_5_2_2').html("无数据...");
		    		}else{
		    			multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
		    		}
		    	});
			}
	    	return;
    	}
    	var url = "../../../js/Charts/Column2D.swf";
//    	multDimenAnaly.buildPic(this.dataXml(), "detail", url);
    	MultDimenAnalyAction.getDrillXmlData($("#date").val(), type, par, level+1, null, function(res){
    		multDimenAnaly.buildPic(res, "detail", url);
    	});
    	$('#title').html(title);
    	$('.theme-popover-mask').fadeIn(100);
		$('.theme-popover').slideDown(200);
    	
    };
    
    //联动执行的方法
    this.showLD = function(par, level, title, type){
    	if(null == par){
    		return;
    	}
    	if(level == 3){
    		return;
    	}
    	$("#p_2_2").empty();
    	$("#p_3_2").empty();
    	$("#p_5_2_2").empty();
    	parentId = par;
    	titleVar = title; 
    	var url = "../../../js/Charts/Column2D.swf";
//    	multDimenAnaly.buildPic(this.dataXml(), "detail", url);
    	if(type == 2){
    		
    	}
    	
    	MultDimenAnalyAction.getDrillXmlData($("#date").val(), "22" , par, level+1, title, function(res){
    		if(res.indexOf("<set label=") < 0){
    			$('#p_2_2').html("无数据...");
    		}else{
//    			multDimenAnaly.buildPic(multDimenAnaly.build(), "p_2_2", url);
    			var chart = new FusionCharts(url, "ChartId", "100%", "100%");  
    	        chart.setDataXML(res);    
    	        chart.render("p_2_2"); 
    		}
    	});
    	MultDimenAnalyAction.getDrillXmlData($("#date").val(), "32" , par, level+1, title, function(res){
    		if(res.indexOf("<set label=") < 0){
    			$('#p_3_2').html("无数据...");
    		}else{
//    			multDimenAnaly.buildPic(res, "p_3_2", url);
    			var chart = new FusionCharts(url, "ChartId", "100%", "100%");  
    	        chart.setDataXML(res);    
    	        chart.render("p_3_2"); 
    		}
    	});
    	var value = $("input[name='asse']:checked").val();
		if(value == 1){
			MultDimenAnalyAction.getLdAsseData($("#date").val(),"511", par, "考核单位-"+title,"投诉量",function(res){
				if(res.indexOf("<set value=") < 0){
	    			$('#p_5_2_2').html("无数据...");
	    		}else{
	    			multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
	    		}
	    		
	    	});
		}else if(value == 2){
			MultDimenAnalyAction.getLdAsseData($("#date").val(),"521", par,"地市-"+title,"投诉量",function(res){
				if(res.indexOf("<set value=") < 0){
	    			$('#p_5_2_2').html("无数据...");
	    		}else{
	    			multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
	    		}
	    	});
		}
//    	MultDimenAnalyAction.getDrillXmlData($("#date").val(), "102" ,par, level+1, title, function(res){
//    		multDimenAnaly.buildPic(res, "p_5_2_2", url);
//    	});
    };
    

    
    this.showMapAll = function(code,title){
    	$('.theme-poover').slideUp(200);
		$('.theme-popover-mask').fadeOut(250);
		var url = "../../../js/Charts/Column2D.swf";
    	$("#p_1_2_2").empty();
    	MultDimenAnalyAction.getMapCountyData($("#date").val(), code, title, function(res){
    		multDimenAnaly.buildPic(res, "p_1_2_2", url);
    	});
    };
    
    //渲染报表图形的执行方法
    this.buildPic = function(data, divid, url){
    	if(data.indexOf("dataset")==-1 & data.indexOf("categories")==-1 &data.indexOf("set")==-1 &data.indexOf("entity")==-1){
    		$('#detail').html("无数据...");
    		return;
    	}
    	var chart = new FusionCharts(url, "ChartId", "100%", "100%");  
        chart.setDataXML(data);    
        chart.render(divid); 
    };
    
    //使用FusionchartsJS渲染报表  add by:lisn@tydic.com
    this.renderCharts=function(data){
    	if(data.json_xml == "0"){
    		first_param_Len = data.first_param_Len;
    		second_param_Len = data.second_param_Len;
    		third_param_Len = data.third_param_Len;
    		fourth_param_Len = data.fourth_param_Len;
    		fifth_param_Len = data.fifth_param_Len;
    		
    		var qq = JSON.parse(data.json);
    		var abc = new FusionCharts(qq).render();    		
    	}else{
    		var chart = new FusionCharts(data.chartType, "ChartId", "100%", "100%");  
            chart.setDataXML(data.json);    
            chart.render(data.renderDiv); 
    	}
    
    };
    
    //公共方法入口，该方法用于根据最新条件重新加载整个视图页面的所有数据
    //
    this.refreshAllData = function(FirstViewEntity){
    	//第二窗口
    	$("#p_2_2").empty();
    	$("#p_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第一窗口
    	$("#p_1_2_2").empty();
    	$("#p_1_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第三窗口
    	$("#p_3_2").empty();
    	$("#p_3_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第四窗口
    	$("#p_4_2_2").empty();
    	$("#p_4_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第五窗口
    	$("#p_5_2_2").empty();
    	$("#p_5_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");

    	//根据最新条件，重新加载第一象限数据
    	MultDimenAnalyAction.getFirstViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    	//根据最新条件，重新加载第二象限数据
    	MultDimenAnalyAction.getSecondViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    	//根据最新条件，重新加载第三象限数据
    	MultDimenAnalyAction.getThirdViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    	//根据最新条件，重新加载第四象限数据
    	MultDimenAnalyAction.getFourthViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    	//根据最新条件，重新加载第五象限数据
    	MultDimenAnalyAction.getFifthViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    };
    this.refreshFirstData = function(FirstViewEntity){
    	//根据最新条件，重新加载第一象限数据
    	MultDimenAnalyAction.getFirstViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    };
    this.refreshSecondData = function(FirstViewEntity){
    	//根据最新条件，重新加载第二象限数据
    	MultDimenAnalyAction.getSecondViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    };
    this.refreshThirdData = function(FirstViewEntity){
    	//根据最新条件，重新加载第三象限数据
    	MultDimenAnalyAction.getThirdViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    };
    this.refreshFourthData = function(FirstViewEntity){
    	//根据最新条件，重新加载第四象限数据
    	MultDimenAnalyAction.getFourthViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    };
    this.refreshFifthData = function(FirstViewEntity){
    	//根据最新条件，重新加载第五象限数据
    	MultDimenAnalyAction.getFifthViewMapDef(FirstViewEntity,function(res){
    		multDimenAnaly.renderCharts(res);
    	});
    };
    
    //第一象限钻取
    this.getSubAreData = function(parent, level){
    	$('.theme-popover-mask').fadeOut(250);
    	$('.theme-poover').slideUp(200);
		
    	//第二窗口
    	$("#p_2_2").empty();
    	$("#p_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第一窗口
    	$("#p_1_2_2").empty();
    	$("#p_1_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第三窗口
    	$("#p_3_2").empty();
    	$("#p_3_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第四窗口
    	$("#p_4_2_2").empty();
    	$("#p_4_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第五窗口
    	$("#p_5_2_2").empty();
    	$("#p_5_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");

		
		//声明一个对象，用于存储来自页面的变量
		var FirstViewEntity = new Object();
    	FirstViewEntity.date = $("#date").val();
    	FirstViewEntity.type1 = 11;
    	FirstViewEntity.type2 = 2;
    	FirstViewEntity.type3 = 3;
    	
    	var value = $("input[name='field＿name']:checked").val();
    	FirstViewEntity.type4 = 4+""+value;
    	value = $("input[name='asse']:checked").val();
    	if(value == 1){
    		FirstViewEntity.type5 = 51;
    	}else if(value == 2){
    		FirstViewEntity.type5 = 52;
    	}
    	FirstViewEntity.map_level = level+1;
    	FirstViewEntity.chartType = 3;
    	FirstViewEntity.current_condition = "1_pianqu_11: '" + parent + "'";
    	
    	FirstViewEntity.view_id=1;
    	
		multDimenAnaly.refreshSecondData(FirstViewEntity);
		
		
		var viewEntity = new Object();
		viewEntity.date = $("#date").val();
		viewEntity.type1 = 11;
		viewEntity.type2 = 2;
		viewEntity.type3 = 3;
		viewEntity.map_level = level + 1;
		value = $("input[name='field＿name']:checked").val();
		viewEntity.type4 = 4+""+value;
    	value = $("input[name='asse']:checked").val();
    	if(value == 1){
    		viewEntity.type5 = 51;
    	}else if(value == 2){
    		viewEntity.type5 = 52;
    	}
		viewEntity.current_condition = "1_pianqu_11: '" + parent + "'";
//		viewEntity.dr_level = level;
		viewEntity.chartType = 3;

		viewEntity.view_id=1;
		
		multDimenAnaly.refreshThirdData(viewEntity);
		multDimenAnaly.refreshFourthData(viewEntity);
		multDimenAnaly.refreshFifthData(viewEntity);
		
		
		value = $("input[name='map']:checked").val();
		if($("#map").val() != 0){
			viewEntity.type1 = 11;
			viewEntity.chartType = 3;
		}else{
			if(value == 1){
				viewEntity.type1 = 1;
				viewEntity.chartType = 1;
	    	}else {
	    		
	    	}
		}
		
		multDimenAnaly.refreshFirstData(viewEntity);
    };
    
    
    this.getSubProdData = function(parent, level){
    	if(level>=3){
    		alert("目前层级已达到最深钻取层级,不能继续往下钻取...");
    		return;
    	}
    	//第二窗口
    	$("#p_2_2").empty();
    	$("#p_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第一窗口
    	$("#p_1_2_2").empty();
    	$("#p_1_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第三窗口
    	$("#p_3_2").empty();
    	$("#p_3_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第四窗口
    	$("#p_4_2_2").empty();
    	$("#p_4_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第五窗口
    	$("#p_5_2_2").empty();
    	$("#p_5_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");

    	//声明一个对象，用于存储来自页面的变量
		var FirstViewEntity = new Object();
    	FirstViewEntity.date = $("#date").val();
    	FirstViewEntity.type1 = 11;
    	FirstViewEntity.type2 = 2;
    	FirstViewEntity.type3 = 3;
    	
    	var value = $("input[name='field＿name']:checked").val();
    	FirstViewEntity.type4 = 4+""+value;
    	value = $("input[name='asse']:checked").val();
    	if(value == 1){
    		FirstViewEntity.type5 = 51;
    	}else if(value == 2){
    		FirstViewEntity.type5 = 52;
    	}
    	FirstViewEntity.dr_level = level + 1;
    	FirstViewEntity.chartType = 3;
    	FirstViewEntity.current_condition = "2_djxz_2: '" + parent + "%'";
    	
    	FirstViewEntity.view_id=2;
    	
		multDimenAnaly.refreshSecondData(FirstViewEntity);
		
		
		var viewEntity = new Object();
		viewEntity.date = $("#date").val();
		viewEntity.type1 = 11;
		viewEntity.type2 = 2;
		viewEntity.type3 = 3;
		value = $("input[name='field＿name']:checked").val();
		viewEntity.type4 = 4+""+value;
    	value = $("input[name='asse']:checked").val();
    	if(value == 1){
    		viewEntity.type5 = 51;
    	}else if(value == 2){
    		viewEntity.type5 = 52;
    	}
		viewEntity.current_condition = "2_drill_3: '" + parent + "%'";
//		viewEntity.dr_level = level;
		viewEntity.chartType = 3;
		viewEntity.view_id=4;
		
		multDimenAnaly.refreshThirdData(viewEntity);
		multDimenAnaly.refreshFourthData(viewEntity);
		multDimenAnaly.refreshFifthData(viewEntity);
		
		value = $("input[name='map']:checked").val();
		if($("#map").val() != 0){
			viewEntity.type1 = 11;
			viewEntity.chartType = 3;
		}else{
			if(value == 1){
				viewEntity.type1 = 1;
				viewEntity.chartType = 1;
	    	}else {
	    		viewEntity.type5 = 11;
	    	}
		}
		
		multDimenAnaly.refreshFirstData(viewEntity);
    };
    
    this.getSubBusiData = function(parent, level){
    	if(level>=3){
    		alert("目前层级已达到最深钻取层级,不能继续往下钻取...");
    		return;
    	}
    	//第二窗口
    	$("#p_2_2").empty();
    	$("#p_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第一窗口
    	$("#p_1_2_2").empty();
    	$("#p_1_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第三窗口
    	$("#p_3_2").empty();
    	$("#p_3_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第四窗口
    	$("#p_4_2_2").empty();
    	$("#p_4_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
    	//第五窗口
    	$("#p_5_2_2").empty();
    	$("#p_5_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");

    	
    	//声明一个对象，用于存储来自页面的变量
		var FirstViewEntity = new Object();
    	FirstViewEntity.date = $("#date").val();
    	FirstViewEntity.type1 = 11;
    	FirstViewEntity.type2 = 2;
    	FirstViewEntity.type3 = 3;
    	
    	var value = $("input[name='field＿name']:checked").val();
    	FirstViewEntity.type4 = 4+""+value;
    	value = $("input[name='asse']:checked").val();
    	if(value == 1){
    		FirstViewEntity.type5 = 51;
    	}else if(value == 2){
    		FirstViewEntity.type5 = 52;
    	}
    	FirstViewEntity.dr_level = level + 1;
    	FirstViewEntity.chartType = 3;
    	FirstViewEntity.current_condition = "3_djxz_2: '" + parent + "%'";
    	
    	FirstViewEntity.view_id=3;
    	
		multDimenAnaly.refreshThirdData(FirstViewEntity);
		
		
		var viewEntity = new Object();
		viewEntity.date = $("#date").val();
		viewEntity.type1 = 11;
		viewEntity.type2 = 2;
		viewEntity.type3 = 3;
		value = $("input[name='field＿name']:checked").val();
		viewEntity.type4 = 4+""+value;
    	value = $("input[name='asse']:checked").val();
    	if(value == 1){
    		viewEntity.type5 = 51;
    	}else if(value == 2){
    		viewEntity.type5 = 52;
    	}
		viewEntity.current_condition = "3_drill_3: '" + parent + "%'";
//		viewEntity.dr_level = level;
		viewEntity.chartType = 3;
		
		var value_radio_windowsOne = $("input[name='map']:checked").val();
		if($("#map").val() != 0){
			viewEntity.type1 = 11;
			viewEntity.chartType = 3;
		}else{
			if(value_radio_windowsOne == 1){
				viewEntity.type1 = 1;
				viewEntity.chartType = 1;
	    	}else {
	    		viewEntity.chartType = 3;
	    	}
		}
		
		FirstViewEntity.view_id=3;
		
		
		multDimenAnaly.refreshFirstData(viewEntity);
		multDimenAnaly.refreshSecondData(viewEntity);
		multDimenAnaly.refreshFourthData(viewEntity);
		multDimenAnaly.refreshFifthData(viewEntity);
    };
    
    //自定义时间刷新各个窗口数据
    this.getAutoDefDateData = function(date){
    	
    	multDimenAnaly.init();
    	//第一象限默认数据加载
//		var FirstViewEntity = new Object();
//    	FirstViewEntity.date = date;
//    	FirstViewEntity.type1 = 1;
//    	MultDimenAnalyAction.getFirstViewMapDef(FirstViewEntity,function(res){
//    		multDimenAnaly.renderCharts(res.json);
//    	});
//    	
//    	MultDimenAnalyAction.getSecondViewMapDef(FirstViewEntity,function(res){
//    		multDimenAnaly.renderCharts(res.json);
//    	});
//    	
//    	MultDimenAnalyAction.getThirdViewMapDef(FirstViewEntity,function(res){
//    		multDimenAnaly.renderCharts(res.json);
//    	});
//    	
//    	MultDimenAnalyAction.getFourthViewMapDef(FirstViewEntity,function(res){
//    		multDimenAnaly.renderCharts(res.json);
//    	});
//    	
//    	MultDimenAnalyAction.getFifthViewMapDef(FirstViewEntity,function(res){
//    		multDimenAnaly.renderCharts(res.json);
//    	});
    }
    
    
    
    this.ascii2native = function(asciicode) {
        asciicode = asciicode.split("\\u");
        var nativeValue = asciicode[0];
        for (var i = 1; i < asciicode.length; i++) {
            var code = asciicode[i];
            nativeValue += String.fromCharCode(parseInt("0x" + code.substring(0, 4)));
            if (code.length > 4) {
                nativeValue += code.substring(4, code.length);
            }
        }
        return nativeValue;
    }
    
    this.buildPop = function(){
    	var popVer = "<div class=\"theme-popover\" >  "+
    	"     <div class=\"theme-poptit\">  "+
    	"	  <a href=\"javascript:;\" title=\"关闭\" class=\"close\">×</a>  "+
    	"	  <h3 id=\"title\">{title}</h3>  "+
    	"     </div>  "+
    	"     <div class=\"theme-popbod dform\" id=\"detail\">  "+
    	"	    "+
    	"     </div>  "+
    	"</div>  "+
    	"<div class=\"theme-popover-mask\"></div>  ";
    	return popVer;
    };
    
    this.build = function(){
    	var sql = "<chart palettecolors=\"#0075c2\" "+
    	"	bgcolor=\"#ffffff\" "+
    	"	borderalpha=\"20\" "+
    	"	canvasborderalpha=\"0\" "+
    	"	usePlotGradientColor=\"0\" "+
    	"	plotborderalpha=\"10\" "+
    	"	showYAxisValues=\"0\" "+
    	"	placevaluesinside=\"1\" "+
    	"	baseFontSize=\"12\" "+
    	"	xAxisName=\"固网宽带\" "+
    	"	yAxisName=\"投诉量\" "+
    	"	valuefontcolor=\"#ffffff\" "+
    	"	showxaxisline=\"1\" "+
    	"	plotFillAngle=\"45\" "+
    	"	xaxislinecolor=\"#999999\" "+
    	"	divlinecolor=\"#999999\" "+
    	"	divlinedashed=\"1\" "+
    	"	showalternatehgridcolor=\"0\" "+
    	"	subcaptionfontbold=\"0\" "+
    	"	subcaptionfontsize=\"14\" "+
    	"	formatNumberScale=\"0\" "+
    	"	unescapeLinks=\"0\" >  	"+
    	"	"+
    	"<set label=\"其他\" value=\"1313\" color=\"#0033FF\" link=\"JavaScript:multDimenAnaly.showLD(902901, 2, '其他');\" />  "+
    	"<set label=\"宽带提速\" value=\"16\" color=\"#FF9933\" link=\"JavaScript:multDimenAnaly.showLD(902903, 2, '宽带提速');\" />  "+
    	"<set label=\"宽带智能提速\" value=\"5\" color=\"#00FFCC\" link=\"JavaScript:multDimenAnaly.showLD(902905, 2, '宽带智能提速');\" />  "+
    	"<set label=\"校园网\" value=\"2\" color=\"#FFFF00\" link=\"JavaScript:multDimenAnaly.showLD(902902, 2, '校园网');\" /> 	"+
    	"</chart>";
    	return sql;
    }
    
};

//程序进入时初始化执行方法
var multDimenAnaly = new MultDimenAnaly();
//执行
$(document).ready(function(){
//	 alert(userNameen);
//	 alert(userNamecn);
	multDimenAnaly.init();
	
	$("#txtBeginDate").calendar({
        controlId: "divBeginDate",                                 // 弹出的日期控件ID，默认: $(this).attr("id") + "Calendar"
        speed: 200,                                           // 三种预定速度之一的字符串("slow", "normal", or "fast")或表示动画时长的毫秒数值(如：1000),默认：200
        complement: true,                                     // 是否显示日期或年空白处的前后月的补充,默认：true
        readonly: true,                                       // 目标对象是否设为只读，默认：true
        upperLimit: new Date(),                               // 日期上限，默认：NaN(不限制)
        lowerLimit: new Date("2011/01/01"),                   // 日期下限，默认：NaN(不限制)
        callback: function () {                               // 点击选择日期后的回调函数
           // alert("您选择的日期是：" + $("#txtBeginDate").val());
        }
    });
	
	$("#txtEndDate").calendar({
        controlId: "divEndDate",                                 // 弹出的日期控件ID，默认: $(this).attr("id") + "Calendar"
        speed: 200,                                           // 三种预定速度之一的字符串("slow", "normal", or "fast")或表示动画时长的毫秒数值(如：1000),默认：200
        complement: true,                                     // 是否显示日期或年空白处的前后月的补充,默认：true
        readonly: true,                                       // 目标对象是否设为只读，默认：true
        upperLimit: new Date(),                               // 日期上限，默认：NaN(不限制)
        lowerLimit: new Date("2011/01/01"),                   // 日期下限，默认：NaN(不限制)
        callback: function () {                               // 点击选择日期后的回调函数
        }
    });
});