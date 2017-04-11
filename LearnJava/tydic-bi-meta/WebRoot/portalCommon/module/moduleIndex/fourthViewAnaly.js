var fourthViewAnaly=function(){
	
	this.getData=function(){
		var val =8;
		//第四象限，饼图左侧单选按钮及复选框，选择后触发的操作
		$(".radio_pie").click(function(){
			var value = $("input[name='field＿name']:checked").val();
			val = value;
			//声明一个对象，用于存储来自页面的变量
			var FirstViewEntity = new Object();
	    	FirstViewEntity.date = $("#date").val();
	    	FirstViewEntity.type1 = 11;
	    	FirstViewEntity.type2 = 2;
	    	FirstViewEntity.type3 = 3;
	    	FirstViewEntity.type4 = 4+""+val;
	    	FirstViewEntity.type5 = 51;
	    	FirstViewEntity.chartType = 3;
	    	FirstViewEntity.view_id=4;
	    	FirstViewEntity.current_condition = null;
			multDimenAnaly.refreshFourthData(FirstViewEntity);
		});
		
		var choose_btn_n = $('.checkbox .choose');

		//第四象限，选择单选后弹窗
		choose_btn_n.click(function(){
			if(val == 1){
				$('.mark_value1').fadeIn(250);					
			}else if(val == 2){
				$('.mark_value2').fadeIn(250);
				$('.mark_value1 h4').text("是否4G用户");
			}else if(val == 3){
				$('.mark_value3').fadeIn(250);
			}else if(val == 4){
				$('.mark_value4').fadeIn(250);
			}else if(val == 5){
				$('.mark_value5').fadeIn(250);
				$('.mark_value5 h4').text("是否光带用户");
			}else if(val == 6){
				$('.mark_value6').fadeIn(250);
				$('.mark_value6 h4').text("是否智能终端");
			}else if(val == 7){
				$('.mark_value7').fadeIn(250);
			}else if(val == 8){
				$('.mark_value8').fadeIn(250);				
			}				
		});

		//第四象限，弹窗关闭监听事件
		$('.bar_close').click(function(){
			$('.smc').fadeOut(250);
		});		

		//第四象限，单选弹窗选择数据后的SQL组合
		$('.smc .select').click(function(){
			var screen = null;
			if(val == 1){
				screen = " 4_khsx_41 : ";
				$('.mark_value1').fadeOut(250);					
			}else if(val == 2){
				screen = " 4_khsx_42 : ";
				$('.mark_value2').fadeOut(250);
			}else if(val == 3){
				screen = " 4_khsx_43 : ";
				$('.mark_value3').fadeOut(250);
			}else if(val == 4){
				screen = " 4_khsx_44 : ";
				$('.mark_value4').fadeOut(250);
			}else if(val == 5){
				screen = " 4_khsx_45 : ";
				$('.mark_value5').fadeOut(250);
			}else if(val == 6){
				screen = " 4_khsx_46 : ";
				$('.mark_value6').fadeOut(250);
			}else if(val == 7){
				screen = " 4_khsx_47 : ";
				$('.mark_value7').fadeOut(250);
			}else if(val == 8){
				screen = " 4_khsx_48 : ";
				$('.mark_value8').fadeOut(250);				
			}	
			
			var str=""; 
			$("input[name='val_"+val+"']:checkbox").each(function(){ 
				if ('checked' == $(this).attr('checked')) { 
					str += '\'' + $(this).attr('value')+'\','; 
				} 
			});
			screen = screen + "("+str.substring(0, str.length-1)+")";
			
			//声明一个对象，用于存储来自页面的变量
			var FirstViewEntity = new Object();
	    	FirstViewEntity.date = $("#date").val();
	    	FirstViewEntity.type1 = 11;
	    	FirstViewEntity.type2 = 2;
	    	FirstViewEntity.type3 = 3;
	    	FirstViewEntity.type4 = 4+""+val;
	    	var value = $("input[name='asse']:checked").val();
	    	
	    	if(value == 1){
	    		FirstViewEntity.type5 = 51;
	    	}else if(value == 2){
	    		FirstViewEntity.type5 = 52;
	    	}
	    	
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
	    	
			FirstViewEntity.view_id=4;
			
	    	FirstViewEntity.current_condition = screen;
			multDimenAnaly.refreshAllData(FirstViewEntity);
			
		});
	}
	
	
}