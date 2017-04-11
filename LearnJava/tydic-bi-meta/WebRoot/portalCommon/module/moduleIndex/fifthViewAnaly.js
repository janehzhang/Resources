var fifthViewAnaly=function(){
	
	this.getData=function(){
		
		//第五象限，选择同比、环比下拉框时触发事件，弹出窗口
		$("#rate_asse").bind("change",function(){
			var ratioType = "";
		    if($(this).val() != 0){
		    	if($(this).val() == 1){
		    		ratioType = "SAME_RATIO";
		    	}else if ($(this).val() == 2){
		    		ratioType = "LOOP_RATIO";
		    	}
			    var value = $("input[name='asse']:checked").val();
			    var title = "";
				if(value == 1){
					title = "考核维度-同比";
					$("#detail").empty();
			    	var html = "<div class=\"tab_div_c_up\" id=\"p_5_2_2_1\"></div> <div class=\"tab_div_c_down\" id=\"p_5_2_2_2\"></div> ";
			    	$("#detail").append(html);
					var FirstViewEntity = new Object();
			    	FirstViewEntity.date = $("#date").val();
			    	FirstViewEntity.type5 = 51;
			    	FirstViewEntity.chartContainer = "p_5_2_2_1";
			    	FirstViewEntity.current_condition = ratioType;
			    	
			    	MultDimenAnalyAction.getFifthViewRateDef(FirstViewEntity,function(res){
			    		multDimenAnaly.renderCharts(res);
			    	});
			    	FirstViewEntity = new Object();
			    	FirstViewEntity.date = $("#date").val();
			    	FirstViewEntity.type5 = 51;
			    	//是否需要下钻
			    	FirstViewEntity.is_link = "false";
			    	FirstViewEntity.chartContainer = "p_5_2_2_2";
			    	MultDimenAnalyAction.getFifthViewRateDef(FirstViewEntity,function(res){
			    		multDimenAnaly.renderCharts(res);
			    		$('#title').html(title+"-"+res.w5_x_name);
			    	});
			    	
//			    	$('#title').html(title);
		    		$('.theme-popover-mask').fadeIn(100);
		    		$('.theme-popover').slideDown(200);
		    		
		    		$(this).get(0).selectedIndex = 0;
				}else if(value == 2){
					title = "考核维度-环比";
					$("#detail").empty();
			    	var html = "<div class=\"tab_div_c_up\" id=\"p_5_2_2_1\"></div> <div class=\"tab_div_c_down\" id=\"p_5_2_2_2\"></div> ";
			    	$("#detail").append(html);
					var FirstViewEntity = new Object();
			    	FirstViewEntity.date = $("#date").val();
			    	FirstViewEntity.type5 = 52;
			    	FirstViewEntity.chartContainer = "p_5_2_2_1";
			    	FirstViewEntity.current_condition = ratioType;
			    	
			    	MultDimenAnalyAction.getFifthViewRateDef(FirstViewEntity,function(res){
			    		multDimenAnaly.renderCharts(res);
			    	});
			    	FirstViewEntity = new Object();
			    	FirstViewEntity.date = $("#date").val();
			    	FirstViewEntity.type5 = 52;
			    	//是否需要下钻
			    	FirstViewEntity.is_link = "false";
			    	FirstViewEntity.chartContainer = "p_5_2_2_2";
			    	MultDimenAnalyAction.getFifthViewRateDef(FirstViewEntity,function(res){
			    		multDimenAnaly.renderCharts(res);
			    		$('#title').html(title+"-"+res.w5_x_name);
			    	});
			    	
			    	$('#title').html(title);
		    		$('.theme-popover-mask').fadeIn(100);
		    		$('.theme-popover').slideDown(200);
		    		
		    		$(this).get(0).selectedIndex = 0;
				}
			    
		    }
		});
		
		/**
		 * 考核维度
		 */
		//第五象限，考核维度单选按钮事件监听
		$(".radio_asse").click(function(){
			$("#p_5_2_2").empty();
	    	$("#p_5_2_2").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");

			var value = $("input[name='asse']:checked").val();
			var FirstViewEntity = new Object();
	    	FirstViewEntity.date = $("#date").val();
			if(value == 1){
				FirstViewEntity.type5 = 51;
				FirstViewEntity.w5_x_name="按省公司";
				multDimenAnaly.refreshFifthData(FirstViewEntity);
			}else if(value == 2){
				FirstViewEntity.type5 = 52;
				FirstViewEntity.w5_x_name="按地市";
				multDimenAnaly.refreshFifthData(FirstViewEntity);
			}
		});
		
		
		$("#imgView5").click(function(){
			if(fifth_param_Len=="1"){
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
			FirstViewEntity.fallbackWindow = 5;
			
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
        	
//			MultDimenAnalyAction.getFallbackCondition(FirstViewEntity,function(res){
//				multDimenAnaly.renderCharts(res.json1);//渲染窗口
//				multDimenAnaly.renderCharts(res.json2);
//				multDimenAnaly.renderCharts(res.json3);
//				multDimenAnaly.renderCharts(res.json4);
//				multDimenAnaly.renderCharts(res.json5);
//	    	});
			
        	FirstViewEntity.which_first = 5;
			MultDimenAnalyAction.getFallbackCondition(FirstViewEntity,function(res){
				multDimenAnaly.renderCharts(res);//渲染窗口
	    	});
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
			
		});
		
		
	}
	
}