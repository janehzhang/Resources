var firstViewAnaly=function(){
	
	this.getData=function(){
		//第一象限，号码归属地域维度，点击“放大镜”按钮弹出窗口显示当前该窗口所有数据
		$("#img_hm").click(function(){
			//第一窗口 放大
	    	$("#detail").empty();
	    	$("#detail").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");

			var FirstViewEntity = new Object();
			FirstViewEntity.date = $("#date").val();
			FirstViewEntity.window_id = 1;
			FirstViewEntity.is_top5="false";
			FirstViewEntity.window_id = 1;
			
			var value = $("input[name='map']:checked").val();
			if($("#map").val() != 0){
				FirstViewEntity.type1 = 11;
				FirstViewEntity.chartType = 3;
			}else{
				if(value == 1){
					FirstViewEntity.type1 = 1;
					FirstViewEntity.chartType = 1;
					alert("目前视图为地图视图,不支持放大操作!!!");
					return;
		    	}else {
		    		
		    	}
			}
			
	    	MultDimenAnalyAction.getAllDataFromWindows(FirstViewEntity,function(res){
	    		multDimenAnaly.renderCharts(res);
	    		$('#title').html("号码归属地域维度-"+res.w1_x_name);
	    	});
	    	
	    	$('#title').html("号码归属地域维度");
			$('.theme-popover-mask').fadeIn(100);
			$('.theme-popover').slideDown(200);
    	});
		
		/**
		 * 地图
		 */
		//第一象限，单选按钮事件监听
		$(".radio_map").click(function(){
			
			$("#map option").eq(0).attr("selected",true);
			
//			$("#map option").each(function(){
//			   if($(this).val() == '0'){
//			      $(this).attr('selected', 'selected');
//			   }
//			});
			
			var value = $("input[name='map']:checked").val();
			if(value == 1){
//				$("#p_1_2_2").empty();
				$("#map").attr("disabled",false);
				//mapScreen = "";
				//声明一个对象，用于存储来自页面的变量
				var FirstViewEntity = new Object();
		    	FirstViewEntity.date = $("#date").val();
		    	FirstViewEntity.type1 = 1;
		    	FirstViewEntity.type2 = 2;
		    	FirstViewEntity.type3 = 3;
		    	FirstViewEntity.type4 = 48;
		    	FirstViewEntity.map_level = 2;
		    	FirstViewEntity.chartType = 1;
//		    	FirstViewEntity.current_condition = mapScreen;
		    	FirstViewEntity.is_top5="true";
				multDimenAnaly.refreshAllData(FirstViewEntity);
			}else if(value == 2){
				$("#p_1_2_2").empty();
		    	//第一窗口
		    	$("#detail").empty();
		    	$("#detail").html("<img src=\"./images/chart_loading.gif\" align=\"middle\" height=\"96px\" width=\"100px\"  alt=\"数据加载中...\" />");
		    	
		    	$("#map").attr("disabled",true);
				
		    	
		    	//mapScreen = "";
				//声明一个对象，用于存储来自页面的变量
				var FirstViewEntity = new Object();
		    	FirstViewEntity.date = $("#date").val();
		    	FirstViewEntity.type1 = 11;
		    	FirstViewEntity.map_level = 3;
		    	FirstViewEntity.chartType = 3;
//		    	FirstViewEntity.current_condition = mapScreen;
		    	FirstViewEntity.is_top5="false";
		    	FirstViewEntity.chartContainer="detail";
		    	MultDimenAnalyAction.getFirstViewMapDef(FirstViewEntity,function(res){
		    		multDimenAnaly.renderCharts(res);
		    	});
				

		    	$('#title').html("按地市");
		    	$('.theme-popover-mask').fadeIn(250);
//		    	$('.pop_main').fadeIn(250);
				$('.theme-popover').slideDown(200);
				
		    	
			}
		});
		
		/**
		 * 下拉片区
		 */
		//第一象限，下拉片区监听事件
		$("#map").bind("change",function(){ 
			$("#p_1_2_2").empty();
			var title = "地市";
			var pCode = null;
			if($(this).val() != 0){
				if($(this).val() == 2){
					pCode = "0001";
					title = "珠三角1";
				}else if($(this).val() == 3){
					pCode = "0002";
					title = "珠三角2";
				}else if($(this).val() == 4){
					pCode = "0003";
					title = "粤东";
				}else if($(this).val() == 5){
					pCode = "0004";
					title = "粤西";
				}else{
					pCode = "0005";
					title = "粤北";
				}
				mapScreen = "  1_pianqu_11:'"+ pCode +"'";
				//声明一个对象，用于存储来自页面的变量
				var FirstViewEntity = new Object();
		    	FirstViewEntity.date = $("#date").val();
		    	FirstViewEntity.type1 = 11;
		    	FirstViewEntity.type2 = 2;
		    	FirstViewEntity.type3 = 3;
		    	FirstViewEntity.type4 = 48;
		    	FirstViewEntity.type5 = 51;
		    	FirstViewEntity.map_level = 3;
		    	FirstViewEntity.chartType = 3;
		    	FirstViewEntity.w1_y_name = "投诉量";
		    	FirstViewEntity.current_condition = mapScreen;
		    	FirstViewEntity.is_top5="true";
		    	FirstViewEntity.view_id=1;
		    	
		    	var value = $("input[name='asse']:checked").val();
		    	if(value == 1){
					FirstViewEntity.type5 = 51;
					FirstViewEntity.w5_x_name="按省公司";
				}else if(value == 2){
					FirstViewEntity.type5 = 52;
					FirstViewEntity.w5_x_name="按地市";
				}
				multDimenAnaly.refreshAllData(FirstViewEntity);
			}
		});
		
		
		$("#imgView1").click(function(){
			if(first_param_Len=="1"){
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
			FirstViewEntity.fallbackWindow = 1;
			
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
		    		FirstViewEntity.type1 = 11;
		    		FirstViewEntity.chartType = 3;
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
		
		
		
	}
	
	
}