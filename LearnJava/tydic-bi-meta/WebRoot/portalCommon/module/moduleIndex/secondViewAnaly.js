var secondViewAnaly=function(){
	
	this.getData=function(){
		
		$("#imgView2").click(function(){
			if(second_param_Len=="1"){
				alert("无可回退的操作步骤...");
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

	    	
			var FirstViewEntity = new Object();
			FirstViewEntity.date = $("#date").val();
			FirstViewEntity.fallbackWindow = 2;
			
			//判断第一象限需要展示的图表类型
			value = $("input[name='map']:checked").val();
			if($("#map").val() != 0){
				FirstViewEntity.type1 = 11;
				FirstViewEntity.chartType = 3;
			}else{
				if(value == 1){
					FirstViewEntity.type1 = 1;
					FirstViewEntity.chartType = 1;
		    	}else {
//		    		FirstViewEntity.type5 = 11;
		    	}
			}
			
//			FirstViewEntity.type1 = 11;
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
        	
        	FirstViewEntity.view_id=2;
        	
//			MultDimenAnalyAction.getFallbackCondition(FirstViewEntity,function(res){
//				multDimenAnaly.renderCharts(res.json1);//渲染窗口
//				multDimenAnaly.renderCharts(res.json2);
//				multDimenAnaly.renderCharts(res.json3);
//				multDimenAnaly.renderCharts(res.json4);
//				multDimenAnaly.renderCharts(res.json5);
//	    	});
			
			FirstViewEntity.which_first = 2;
			MultDimenAnalyAction.getFallbackCondition(FirstViewEntity,function(res){
				multDimenAnaly.renderCharts(res);//渲染窗口
	    	});			
			FirstViewEntity.which_first = 1;
			MultDimenAnalyAction.getFallbackCondition(FirstViewEntity,function(res){
				multDimenAnaly.renderCharts(res);//渲染窗口
	    	});
			FirstViewEntity.which_first = 3;
			MultDimenAnalyAction.getFallbackCondition(FirstViewEntity,function(res){
				multDimenAnaly.renderCharts(res);//渲染窗口
	    	});
			FirstViewEntity.which_first = 4;
			MultDimenAnalyAction.getFallbackCondition(FirstViewEntity,function(res){
				multDimenAnaly.renderCharts(res);//渲染窗口
	    	});
			FirstViewEntity.which_first = 5;
			MultDimenAnalyAction.getFallbackCondition(FirstViewEntity,function(res){
				multDimenAnaly.renderCharts(res);//渲染窗口
	    	});
		});
		
		//第二象限，选择同比、环比下拉框时触发事件，弹出窗口
		$("#rate_prod").bind("change",function(){ 
			var ratioType = "";
			var title = "";
		    if($(this).val() != 0){
		    	if($(this).val() == 1){
		    		ratioType = "SAME_RATIO";
		    		title = "同比";
		    	}else if ($(this).val() == 2){
		    		ratioType = "LOOP_RATIO";
		    		title = "环比";
		    	}
		    	$("#detail").empty();
		    	var html = "<div class=\"tab_div_c_up\" id=\"p_2_2_1\"></div> <div class=\"tab_div_c_down\" id=\"p_2_2_2\"></div> ";
		    	$("#detail").append(html);
		    	
				var FirstViewEntity = new Object();
		    	FirstViewEntity.date = $("#date").val();
		    	FirstViewEntity.type2 = 2;
		    	FirstViewEntity.chartContainer = "p_2_2_1";
		    	FirstViewEntity.current_condition = ratioType;
		    	MultDimenAnalyAction.getSecondViewRateDef(FirstViewEntity,function(res){
		    		multDimenAnaly.renderCharts(res);
		    	});
		    	FirstViewEntity = new Object();
		    	FirstViewEntity.date = $("#date").val();
		    	FirstViewEntity.type2 = 2;
		    	//是否需要下钻
		    	FirstViewEntity.is_link = "false";
		    	FirstViewEntity.chartContainer = "p_2_2_2";
		    	MultDimenAnalyAction.getSecondViewRateDef(FirstViewEntity,function(res){
		    		multDimenAnaly.renderCharts(res);
		    		$('#title').html(res.w2_x_name+"-"+title);
		    	});
		    	
//		    	$('#title').html(title);
	        	$('.theme-popover-mask').fadeIn(100);
	    		$('.theme-popover').slideDown(200);
	    		
	    		$(this).get(0).selectedIndex = 0;
			    
		    }
		}); 
		
		//第二象限，产品维度，点击“放大镜”按钮弹出窗口显示当前该窗口所有数据
		$("#img1").click(function(){
	    	$("#detail").empty();
	    	$("#detail").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");

			var FirstViewEntity = new Object();
			FirstViewEntity.date = $("#date").val();
			FirstViewEntity.window_id = 2;
			FirstViewEntity.is_top5="false";
	    	MultDimenAnalyAction.getAllDataFromWindows(FirstViewEntity,function(res){
	    		multDimenAnaly.renderCharts(res);
	    		$('#title').html(res.w2_x_name);
	    	});
	    	
//	    	$('#title').html("显示全部-产品维度-");
			$('.theme-popover-mask').fadeIn(100);
			$('.theme-popover').slideDown(200);
    	});
		
		//第二象限，产品维度，点击“放大镜”按钮弹出窗口显示当前该窗口所有数据
		//目前已注释，暂时没有使用
		$("#img3").click(function(){
			MultDimenAnalyAction.getPicXmlData($("#date").val(),"2","一级产品","投诉量",function(res){
	    		multDimenAnaly.buildPic(res, "detail", "MSCombi2D.swf");
	    	});
	    	$('#title').html("产品维度-TOP5");
			$('.theme-popover-mask').fadeIn(100);
			$('.theme-popover').slideDown(200);
    	});
		
		
	}
	
	
}