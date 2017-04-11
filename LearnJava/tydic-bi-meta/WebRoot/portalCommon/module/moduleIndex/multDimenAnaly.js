dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var globZoneTree = null;

var MultDimenAnaly=function(){
	var screenArray = new Array();
	var levelArray = new Array();
	var t1_s = 1;
	var t2_s = 1;
	var t3_s = 1;
	var t4_s = 1;
	var t5_s = 1;
	var parType = 2;
	var mapLevel = 1;
	var parentId = null;
	var titleVar = null;
	var asseScreen = null;
	var ldScreen = null;
	var mapScreen = null;
    this.init = function(){
    	screenArray[0] = null;
    	screenArray[1] = null;
    	screenArray[2] = null;
    	screenArray[3] = null;
    	levelArray[0] = null;
    	levelArray[1] = null;
    	levelArray[2] = null;
    	levelArray[3] = null;
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
    	MultDimenAnalyAction.getPicXmlData($("#date").val(),"2","一级产品","投诉量",function(res){
    		multDimenAnaly.buildPic(res, "p_2_2", "MSCombi2D.swf");
    	});
    	
    	MultDimenAnalyAction.getPicXmlData($("#date").val(),"3","一级表象","投诉量",function(res){
    		multDimenAnaly.buildPic(res, "p_3_2", "MSCombi2D.swf");
    	});
    	
    	MultDimenAnalyAction.getPieXmlData($("#date").val(),"48", null, null, null, null,  function(res){
    		multDimenAnaly.buildPic(res, "p_4_2_2", "../../../js/Charts/Pie2D.swf");
    	});
    	MultDimenAnalyAction.getPicXmlData($("#date").val(),"51","考核单位","投诉量",function(res){
    		multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
    	});
    	
    	MultDimenAnalyAction.getMapXmlData($("#date").val(),"1",function(res){
    		multDimenAnaly.buildPic(res, "p_1_2_2", "FCMap_GuangDong.swf");
    	});
		
		//-----------------------------------------------//
		$("#rate_prod").bind("change",function(){ 
			var ratioType = "";
		    if($(this).val() != 0){
		    	if($(this).val() == 1){
		    		ratioType = "SAME_RATIO";
		    	}else if ($(this).val() == 2){
		    		ratioType = "LOOP_RATIO";
		    	}
		    	var tt = "";
		    	if(t2_s == 1){
					pp = null;
	    			tt = "一级";
	    		}else if (t2_s == 2){
	    			tt = "二级";
	    		}else if (t2_s == 3){
	    			tt = "三级";
	    		}
			    MultDimenAnalyAction.getPicRatioData($("#date").val(),"2",ratioType,tt+"产品","投诉量", parentId, t2_s, function(res){
//			    	alert(res);
			    	$("#detail").empty();
			    	var html = "<div class=\"tab_div_c_up\" id=\"p_2_2_1\"></div> <div class=\"tab_div_c_down\" id=\"p_2_2_2\"></div> ";
			    	$("#detail").append(html);
		    		multDimenAnaly.buildPic(res.pic, "p_2_2_2", "MSCombi2D.swf");
		    		multDimenAnaly.buildPic(res.ratio, "p_2_2_1", "Scatter.swf");	
		    		
		        	$('#title').html("产品维度");
		        	$('.theme-popover-mask').fadeIn(100);
		    		$('.theme-popover').slideDown(200);
		    	});
			    
		    }else{
		    	MultDimenAnalyAction.getPicXmlData($("#date").val(),"2","一级产品","投诉量",function(res){
		    		multDimenAnaly.buildPic(res, "p_2_2", "MSCombi2D.swf");
		    	});
		    }
		}); 
		
		$("#rate_busi").bind("change",function(){
			var ratioType = "";
		    if($(this).val() != 0){
		    	if($(this).val() == 1){
		    		ratioType = "SAME_RATIO";
		    	}else if ($(this).val() == 2){
		    		ratioType = "LOOP_RATIO";
		    	}
		    	var tt = "";
		    	if(t3_s == 1){
					pp = null;
	    			tt = "一级";
	    		}else if (t3_s == 2){
	    			tt = "二级";
	    		}else if (t3_s == 3){
	    			tt = "三级";
	    		}
			    MultDimenAnalyAction.getPicRatioData($("#date").val(),"3",ratioType,tt+"表象","投诉量", parentId, t3_s, function(res){
			    	$("#detail").empty();
			    	var html = "<div class=\"tab_div_c_up\" id=\"p_3_2_1\"></div> <div class=\"tab_div_c_down\" id=\"p_3_2_2\"></div> ";
			    	$("#detail").append(html);
		    		multDimenAnaly.buildPic(res.pic, "p_3_2_2", "MSCombi2D.swf");
		    		multDimenAnaly.buildPic(res.ratio, "p_3_2_1", "Scatter.swf");	
		    		
		    		$('#title').html("表象维度");
			    	$('.theme-popover-mask').fadeIn(100);
		    		$('.theme-popover').slideDown(200);
		    	});
			    
			    
			    
		    }else{
		    	MultDimenAnalyAction.getPicXmlData($("#date").val(),"3","一级表象","投诉量",function(res){
		    		multDimenAnaly.buildPic(res, "p_3_2", "MSCombi2D.swf");
		    	});
		    }
		});
		
		$("#rate_asse").bind("change",function(){
			var ratioType = "";
		    if($(this).val() != 0){
		    	if($(this).val() == 1){
		    		ratioType = "SAME_RATIO";
		    	}else if ($(this).val() == 2){
		    		ratioType = "LOOP_RATIO";
		    	}
			    var value = $("input[name='asse']:checked").val();
				if(value == 1){
					 MultDimenAnalyAction.getPicRatioData($("#date").val(),"51",ratioType,"考核单位","投诉量", parentId, 1, function(res){
					    	$("#detail").empty();
					    	var html = "<div class=\"tab_div_c_up\" id=\"p_5_2_2_1\"></div> <div class=\"tab_div_c_down\" id=\"p_5_2_2_2\"></div> ";
					    	$("#detail").append(html);
				    		multDimenAnaly.buildPic(res.pic, "p_5_2_2_2", "MSCombi2D.swf");
				    		multDimenAnaly.buildPic(res.ratio, "p_5_2_2_1", "Scatter.swf");
				    		
				    		$('#title').html("考核维度");
				    		$('.theme-popover-mask').fadeIn(100);
				    		$('.theme-popover').slideDown(200);
				    	});
				}else if(value == 2){
					MultDimenAnalyAction.getPicRatioData($("#date").val(),"52",ratioType,"地市","投诉量", parentId, 1, function(res){
				    	$("#detail").empty();
				    	var html = "<div class=\"tab_div_c_up\" id=\"p_5_2_2_1\"></div> <div class=\"tab_div_c_down\" id=\"p_5_2_2_2\"></div> ";
				    	$("#detail").append(html);
			    		multDimenAnaly.buildPic(res.pic, "p_5_2_2_2", "MSCombi2D.swf");
			    		multDimenAnaly.buildPic(res.ratio, "p_5_2_2_1", "Scatter.swf");	
			    		
			    		$('#title').html("考核维度");
			    		$('.theme-popover-mask').fadeIn(100);
			    		$('.theme-popover').slideDown(200);
			    	});
				}
			    
		    }else{
		    	MultDimenAnalyAction.getPicXmlData($("#date").val(),"51","考核单位","投诉量",function(res){
		    		multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
		    	});
    	
		    }
		});
		
		
		$("#img1").click(function(){
			var tt = "";
			var pp = parentId;
			if(t2_s == 1){
				pp = null;
    			tt = "一级";
    		}else if (t2_s == 2){
    			tt = "二级";
    		}else if (t2_s == 3){
    			tt = "三级";
    		}
			$("#detail").empty();
			MultDimenAnalyAction.getAllXmlData($("#date").val(),"21",tt+"产品","投诉量",pp, t2_s, function(res){
	    		multDimenAnaly.buildPic(res, "detail", "../../../js/Charts/Column2D.swf");
	    	});
	    	$('#title').html("产品维度-全部");
	    	$('.theme-popover-mask').fadeIn(100);
			$('.theme-popover').slideDown(200);
    	});
		$("#img2").click(function(){
			var tt = "";
			var pp = parentId;
			if(t3_s == 1){
				pp = null;
    			tt = "一级";
    		}else if (t3_s == 2){
    			tt = "二级";
    		}else if (t3_s == 3){
    			tt = "三级";
    		}
			$("#detail").empty();
			MultDimenAnalyAction.getAllXmlData($("#date").val(),"31",tt+"表象","投诉量",pp, t3_s, function(res){
	    		multDimenAnaly.buildPic(res, "detail", "../../../js/Charts/Column2D.swf");
	    	});
	    	$('#title').html("表象维度-全部");
			$('.theme-popover-mask').fadeIn(100);
			$('.theme-popover').slideDown(200);
    	});
		
		$("#img3").click(function(){
			$("#detail").empty();
			MultDimenAnalyAction.getPicXmlData($("#date").val(),"2","一级产品","投诉量",function(res){
	    		multDimenAnaly.buildPic(res, "detail", "MSCombi2D.swf");
	    	});
	    	$('#title').html("产品维度-TOP5");
			$('.theme-popover-mask').fadeIn(100);
			$('.theme-popover').slideDown(200);
    	});
		
		$("#img_hm").click(function(){
			MultDimenAnalyAction.getPicXmlData($("#date").val(),"2","一级产品","投诉量",function(res){
	    		multDimenAnaly.buildPic(res, "detail", "MSCombi2D.swf");
	    	});
	    	$('#title').html("产品维度-TOP5");
			$('.theme-popover-mask').fadeIn(100);
			$('.theme-popover').slideDown(200);
    	});
		

		/**
		 * 饼图
		 */
		
		$(".radio_pie").click(function(){
			var value = $("input[name='field＿name']:checked").val();
			MultDimenAnalyAction.getPieXmlData($("#date").val(),"4"+value, null, null,  null, null, function(res){
	    		multDimenAnaly.buildPic(res, "p_4_2_2", "../../../js/Charts/Pie2D.swf");
	    	});
			val = value;
		});
		
		val =8;
		var choose_btn_n = $('.checkbox .choose');

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

		$('.bar_close').click(function(){
			$('.smc').fadeOut(250);
		});		

		$('.smc .select').click(function(){
			var screen = null;
			if(val == 1){
				screen = " and aa.serv_lev in ";
				$('.mark_value1').fadeOut(250);					
			}else if(val == 2){
				screen = " and aa.is_4g_disc in ";
				$('.mark_value2').fadeOut(250);
			}else if(val == 3){
				screen = " and aa.bhgq_type in ";
				$('.mark_value3').fadeOut(250);
			}else if(val == 4){
				screen = " and aa.payment_id in ";
				$('.mark_value4').fadeOut(250);
			}else if(val == 5){
				screen = " and aa.optical_fiber_type in ";
				$('.mark_value5').fadeOut(250);
			}else if(val == 6){
				screen = " and aa.is_intelligence in ";
				$('.mark_value6').fadeOut(250);
			}else if(val == 7){
				screen = " and aa.SERV_GRP_TYPE in ";
				$('.mark_value7').fadeOut(250);
			}else if(val == 8){
				screen = " and aa.DIVIDE_MARKET_6_NAME in ";
				$('.mark_value8').fadeOut(250);				
			}	
			
			var str=""; 
			$("input[name='val_"+val+"']:checkbox").each(function(){ 
				if ('checked' == $(this).attr('checked')) { 
					str += '\'' + $(this).attr('value')+'\','; 
				} 
			});
			screen = screen + "("+str.substring(0, str.length-1)+")";
			
			$("#p_4_2_2").empty();
			$("#p_2_2").empty();
			$("#p_3_2").empty();
			$("#p_5_2_2").empty();
			MultDimenAnalyAction.getPieXmlData($("#date").val(),"4" + val, screen, null, null, null,  function(res){
	    		multDimenAnaly.buildPic(res, "p_4_2_2", "../../../js/Charts/Pie2D.swf");
	    	});
			
			MultDimenAnalyAction.getPicLDData($("#date").val(),"2","一级产品","投诉量", screen,function(res){
	    		multDimenAnaly.buildPic(res, "p_2_2", "MSCombi2D.swf");
	    	});
	    	
	    	MultDimenAnalyAction.getPicLDData($("#date").val(),"3","一级表象","投诉量", screen,function(res){
	    		multDimenAnaly.buildPic(res, "p_3_2", "MSCombi2D.swf");
	    	});
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
			
		});
		
		
		
		/**
		 * 地图
		 */
		$(".radio_map").click(function(){
			var value = $("input[name='map']:checked").val();
			if(value == 1){
				$("#p_1_2_2").empty();
				MultDimenAnalyAction.getMapXmlData($("#date").val(),"1",function(res){
		    		multDimenAnaly.buildPic(res, "p_1_2_2", "FCMap_GuangDong.swf");
		    	});
			}else if(value == 2){
				/*
				MultDimenAnalyAction.getAllXmlData($("#date").val(),"12",function(res){
		    		multDimenAnaly.buildPic(res, "p_1_2_2", "../../../js/Charts/Column2D.swf");
		    	});*/
				$("#p_1_2_2").empty();
				
		    	MultDimenAnalyAction.getAllXmlData($("#date").val(),"12","地市","投诉量", null, 1, function(res){
		    		multDimenAnaly.buildPic(res, "detail", "../../../js/Charts/Column2D.swf");
		    	});
		    	$('#title').html("按地市");
//		    	$('.theme-popover-mask').fadeIn(100);
				$('.theme-popover').slideDown(200);
				$('#pop').fadeIn(250);
		    	
			}
		});
		

		/**
		 * 考核维度
		 */
		$(".radio_asse").click(function(){
			$("#p_5_2_2").empty();
			var value = $("input[name='asse']:checked").val();
			if(value == 1){
				if(parentId==null){
					if($("#rate_asse").val() == 0){
						MultDimenAnalyAction.getPicXmlData($("#date").val(),"51","考核单位","投诉量",function(res){
				    		multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
				    	});
						
					}else if($("#rate_asse").val() == 1){
						MultDimenAnalyAction.getPicRatioData($("#date").val(),"51","SAME_RATIO","考核单位","投诉量",parentId, 1, function(res){
					    	var html = "<div class=\"tab_div_c_up\" id=\"p_5_2_2_1\"></div> <div class=\"tab_div_c_down\" id=\"p_5_2_2_2\"></div> ";
					    	$("#p_5_2_2").append(html);
				    		multDimenAnaly.buildPic(res.pic, "p_5_2_2_2", "MSCombi2D.swf");
				    		multDimenAnaly.buildPic(res.ratio, "p_5_2_2_1", "Scatter.swf");		    		
				    	});
					}else if($("#rate_asse").val() == 2){
						MultDimenAnalyAction.getPicRatioData($("#date").val(),"51","LOOP_RATIO","考核单位","投诉量",parentId, 1, function(res){
					    	var html = "<div class=\"tab_div_c_up\" id=\"p_5_2_2_1\"></div> <div class=\"tab_div_c_down\" id=\"p_5_2_2_2\"></div> ";
					    	$("#p_5_2_2").append(html);
				    		multDimenAnaly.buildPic(res.pic, "p_5_2_2_2", "MSCombi2D.swf");
				    		multDimenAnaly.buildPic(res.ratio, "p_5_2_2_1", "Scatter.swf");		    		
				    	});
					}
				}else{
					if(parType == 2 || parType == 21 || parType == 22 ){
						asseScreen = "and a.CMPL_PROD_TYPE_ID like '"+parentId+"%'";
					}else if(parType == 3 || parType == 31 || parType == 32){
						asseScreen = "and a.cmpl_business_type_id like '"+parentId+"%'";
					}
						
					MultDimenAnalyAction.getLdAsseData($("#date").val(),"511", asseScreen, "考核单位-"+titleVar,"投诉量", null, null, function(res){
						if(res.indexOf("<set value=") < 0){
			    			$('#p_5_2_2').html("无数据...");
			    		}else{
			    			multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
			    		}
			    		
			    	});
				}
				
				
			}else if(value == 2){
				if(parentId==null){
					if($("#rate_asse").val() == 0){
						MultDimenAnalyAction.getPicXmlData($("#date").val(),"52","地市","投诉量",function(res){
				    		multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
				    	});
					}else if($("#rate_asse").val() == 1){
						MultDimenAnalyAction.getPicRatioData($("#date").val(),"52","SAME_RATIO","地市","投诉量", parentId, 1, function(res){
					    	var html = "<div class=\"tab_div_c_up\" id=\"p_5_2_2_1\"></div> <div class=\"tab_div_c_down\" id=\"p_5_2_2_2\"></div> ";
					    	$("#p_5_2_2").append(html);
				    		multDimenAnaly.buildPic(res.pic, "p_5_2_2_2", "MSCombi2D.swf");
				    		multDimenAnaly.buildPic(res.ratio, "p_5_2_2_1", "Scatter.swf");		    		
				    	});
					}else if($("#rate_asse").val() == 2){
						MultDimenAnalyAction.getPicRatioData($("#date").val(),"52","LOOP_RATIO","地市","投诉量", parentId, 1, function(res){
					    	var html = "<div class=\"tab_div_c_up\" id=\"p_5_2_2_1\"></div> <div class=\"tab_div_c_down\" id=\"p_5_2_2_2\"></div> ";
					    	$("#p_5_2_2").append(html);
				    		multDimenAnaly.buildPic(res.pic, "p_5_2_2_2", "MSCombi2D.swf");
				    		multDimenAnaly.buildPic(res.ratio, "p_5_2_2_1", "Scatter.swf");		    		
				    	});
					}
				}else{
					if(parType == 2 || parType == 21 || parType == 22 ){
						asseScreen = "and a.CMPL_PROD_TYPE_ID like '"+parentId+"%'";
					}else if(parType == 3 || parType == 31 || parType == 32){
						asseScreen = "and a.cmpl_business_type_id like '"+parentId+"%'";
					}
					MultDimenAnalyAction.getLdAsseData($("#date").val(),"521", asseScreen, "地市-"+titleVar,"投诉量", null, null, function(res){
						if(res.indexOf("<set value=") < 0){
			    			$('#p_5_2_2').html("无数据...");
			    		}else{
			    			multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
			    		}
			    		
			    	});
				}
				
			}
		});
		
		/**
		 * 下拉片区
		 */
		$("#map").bind("change",function(){ 
			$("#p_1_2_2").empty();
			var title = "地市";
			var pCode = "0001";
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
				MultDimenAnalyAction.getMap1XmlData($("#date").val(),$(this).val(), title,"投诉量",function(res){
					if(res.indexOf("<set label=") < 0){
						$('#p_1_2_2').html("无数据...");
		    		}else{
		    			multDimenAnaly.buildPic(res, "p_1_2_2", "../../../js/Charts/Column2D.swf");
		    		}
		    		
		    	});
				
				mapLevel = 2;
				mapScreen = "  and b.zone_code = '"+ pCode +"'  ";
				/****添加联动*****/
				$("#p_2_2").empty();
		    	$("#p_3_2").empty();
		    	$("#p_4_2_2").empty();
		    	$("#p_5_2_2").empty();
		    	if(parentId == null){
		    		MultDimenAnalyAction.getMapLDData($("#date").val(),"2","一级产品","投诉量", mapScreen, mapLevel, function(res){
			    		multDimenAnaly.buildPic(res, "p_2_2", "MSCombi2D.swf");
			    	});
		    		MultDimenAnalyAction.getMapLDData($("#date").val(),"3","一级表象","投诉量", mapScreen, mapLevel, function(res){
			    		multDimenAnaly.buildPic(res, "p_3_2", "MSCombi2D.swf");
			    	});
			    	
//			    	MultDimenAnalyAction.getPieXmlData($("#date").val(),"48", null, null, null, null,  function(res){
//			    		multDimenAnaly.buildPic(res, "p_4_2_2", "../../../js/Charts/Pie2D.swf");
//			    	});
//			    	MultDimenAnalyAction.getMapLDData($("#date").val(),"51","考核单位","投诉量", mapScreen, mapLevel, function(res){
//			    		multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
//			    	});
		    	}else{
		    		var tt = "";
		    		if(t3_s == 1){
		    			tt = "一级";
		    		}else if (t3_s == 2){
		    			tt = "二级";
		    		}else if (t3_s == 3){
		    			tt = "三级";
		    		}
		    		var url = "../../../js/Charts/Column2D.swf";
		    		MultDimenAnalyAction.getDrLDXmlData($("#date").val(), "32" , parentId, t3_s, tt + "表象", mapLevel, mapScreen, function(res){
		        		if(res.indexOf("<set label=") < 0){
		        			$('#p_3_2').html("无数据...");
		        		}else{
		        			var chart = new FusionCharts(url, "ChartId", "100%", "100%");  
		        	        chart.setDataXML(res);    
		        	        chart.render("p_3_2"); 
		        		}
		        	});
		    		
		    		if(t2_s == 1){
		    			tt = "一级";
		    		}else if (t2_s == 2){
		    			tt = "二级";
		    		}else if (t2_s == 3){
		    			tt = "三级";
		    		}
		    		MultDimenAnalyAction.getDrLDXmlData($("#date").val(), "22" , parentId, t2_s, tt + "产品", mapLevel, mapScreen, function(res){
		        		if(res.indexOf("<set label=") < 0){
		        			$('#p_3_2').html("无数据...");
		        		}else{
//		        			multDimenAnaly.buildPic(res, "p_3_2", url);
		        			var chart = new FusionCharts(url, "ChartId", "100%", "100%");  
		        	        chart.setDataXML(res);    
		        	        chart.render("p_2_2"); 
		        		}
		        	});
		    		
		    		
		    		
		    	}
		    	/**客户属性-**/
	    		
	        	var value = $("input[name='field＿name']:checked").val();
	    		MultDimenAnalyAction.getPieXmlData($("#date").val(),"4"+value, null, ldScreen, mapLevel, mapScreen, function(res){
	        		multDimenAnaly.buildPic(res, "p_4_2_2", "../../../js/Charts/Pie2D.swf");
	        	});
	    		
	    		
	    		/**   考核维度   */
	        	
	        	value = $("input[name='asse']:checked").val();
	    		if(value == 1){
	    			MultDimenAnalyAction.getLdAsseData($("#date").val(),"511", asseScreen, "考核单位","投诉量", mapLevel, mapScreen, function(res){
	    				if(res.indexOf("<set value=") < 0){
	    	    			$('#p_5_2_2').html("无数据...");
	    	    		}else{
	    	    			multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
	    	    		}
	    	    		
	    	    	});
	    		}else if(value == 2){
	    			MultDimenAnalyAction.getLdAsseData($("#date").val(),"521", asseScreen,"地市","投诉量", mapLevel, mapScreen, function(res){
	    				if(res.indexOf("<set value=") < 0){
	    	    			$('#p_5_2_2').html("无数据...");
	    	    		}else{
	    	    			multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
	    	    		}
	    	    	});
	    		}
		    	
			}
		});
		
		$("#date").bind("change",function(){ 
			multDimenAnaly.init();
		});
		
    };
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
    	parType = type;
    	var screen = null;
    	var url = "../../../js/Charts/Column2D.swf";
//    	multDimenAnaly.buildPic(this.dataXml(), "detail", url);
//    	alert(type);
    	if(type == 2 || type == 21 || type == 22 ){
    		
    		t2_s = level;
    		screen = " and aa."//饼图的查询条件
    		asseScreen = "and a.CMPL_PROD_TYPE_ID like '"+par+"%'";
    		var tt = "";
    		if(t2_s+1 == 1){
    			tt = "一级";
    		}else if(t2_s+1 == 2){
    			tt = "二级";
    		}else{
    			tt = "三级";
    		}
    		MultDimenAnalyAction.getDrillXmlData($("#date").val(), "22" , par, t2_s+1, tt + "产品-" + title, function(res){
        		if(res.indexOf("<set label=") < 0){
        			$('#p_2_2').html("无数据...");
        		}else{
//        			multDimenAnaly.buildPic(multDimenAnaly.build(), "p_2_2", url);
        			var chart = new FusionCharts(url, "ChartId", "100%", "100%");  
        	        chart.setDataXML(res);    
        	        chart.render("p_2_2"); 
        		}
        	});
    		if(t3_s == 1){
    			tt = "一级";
    		}else if(t3_s == 2){
    			tt = "二级";
    		}else{
    			tt = "三级";
    		}
    		MultDimenAnalyAction.getDrLDXmlData($("#date").val(), "32" , par, t3_s, tt + "表象", null, null, function(res){
        		if(res.indexOf("<set label=") < 0){
        			$('#p_3_2').html("无数据...");
        		}else{
//        			multDimenAnaly.buildPic(res, "p_3_2", url);
        			var chart = new FusionCharts(url, "ChartId", "100%", "100%");  
        	        chart.setDataXML(res);    
        	        chart.render("p_3_2"); 
        		}
        	});
    		
    		ldScreen = " and a.cmpl_prod_type_id like '"+ par +"%'";
    		
    		/**客户属性-**/
        	var value = $("input[name='field＿name']:checked").val();
    		MultDimenAnalyAction.getPieXmlData($("#date").val(),"4"+value, null, ldScreen,  null, null, function(res){
        		multDimenAnaly.buildPic(res, "p_4_2_2", "../../../js/Charts/Pie2D.swf");
        	});
    		t2_s=t2_s+1;
    		
    	}else if(type == 3 || type == 31 || type == 32){
    		asseScreen = "and a.cmpl_business_type_id like '"+par+"%'";
    		t3_s = level;
    		var tt = "";
    		if(t2_s == 1){
    			tt = "一级";
    		}else if(t2_s == 2){
    			tt = "二级";
    		}else{
    			tt = "三级";
    		}
    		MultDimenAnalyAction.getDrLDXmlData($("#date").val(), "22" , par, t2_s, tt + "产品", null, null, function(res){
        		if(res.indexOf("<set label=") < 0){
        			$('#p_2_2').html("无数据...");
        		}else{
//        			multDimenAnaly.buildPic(multDimenAnaly.build(), "p_2_2", url);
        			var chart = new FusionCharts(url, "ChartId", "100%", "100%");  
        	        chart.setDataXML(res);    
        	        chart.render("p_2_2"); 
        		}
        	});
    		if(t3_s+1 == 1){
    			tt = "一级";
    		}else if(t3_s+1 == 2){
    			tt = "二级";
    		}else{
    			tt = "三级";
    		}
    		MultDimenAnalyAction.getDrillXmlData($("#date").val(), "32" , par, t3_s+1, tt+ "表象-" +title, function(res){
        		if(res.indexOf("<set label=") < 0){
        			$('#p_3_2').html("无数据...");
        		}else{
//        			multDimenAnaly.buildPic(res, "p_3_2", url);
        			var chart = new FusionCharts(url, "ChartId", "100%", "100%");  
        	        chart.setDataXML(res);    
        	        chart.render("p_3_2"); 
        		}
        	});
    		
    		ldScreen = " and a.cmpl_business_type_id like '"+ par +"%'";
    		
    		/**客户属性-**/
        	var value = $("input[name='field＿name']:checked").val();
    		MultDimenAnalyAction.getPieXmlData($("#date").val(),"4"+value, null, ldScreen, null, null,  function(res){
        		multDimenAnaly.buildPic(res, "p_4_2_2", "../../../js/Charts/Pie2D.swf");
        	});
    		
    		t3_s=t3_s+1;
    	}
    	
    	
    	
    	
    	/**   考核维度   */
    	
    	var value = $("input[name='asse']:checked").val();
		if(value == 1){
			MultDimenAnalyAction.getLdAsseData($("#date").val(),"511", asseScreen, "考核单位-"+title,"投诉量", null, null, function(res){
				if(res.indexOf("<set value=") < 0){
	    			$('#p_5_2_2').html("无数据...");
	    		}else{
	    			multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
	    		}
	    		
	    	});
		}else if(value == 2){
			MultDimenAnalyAction.getLdAsseData($("#date").val(),"521", asseScreen,"地市-"+title,"投诉量", null, null, function(res){
				if(res.indexOf("<set value=") < 0){
	    			$('#p_5_2_2').html("无数据...");
	    		}else{
	    			multDimenAnaly.buildPic(res, "p_5_2_2", "MSCombi2D.swf");
	    		}
	    	});
		}
		
		
		var value = $("input[name='map']:checked").val();
		if(value == 1){
			$("#p_1_2_2").empty();
			MultDimenAnalyAction.getMapXmlData($("#date").val(),"1",function(res){
	    		multDimenAnaly.buildPic(res, "p_1_2_2", "FCMap_GuangDong.swf");
	    	});
		}else if(value == 2){
	    	
		}
		
//    	MultDimenAnalyAction.getDrillXmlData($("#date").val(), "102" ,par, level+1, title, function(res){
//    		multDimenAnaly.buildPic(res, "p_5_2_2", url);
//    	});
    };
    
    this.showMap = function(code,title){
    	var url = "../../../js/Charts/Column2D.swf";
//    	multDimenAnaly.buildPic(this.dataXml(), "detail", url);
    	$("#p_1_2_2").empty();
    	MultDimenAnalyAction.getMapCountyData($("#date").val(), code, title, function(res){
    		multDimenAnaly.buildPic(res, "p_1_2_2", url);
    	});
    	
    };
    
    this.showMapAll = function(code,title){
    	$('.theme-poover').slideUp(200);
		$('#pop').fadeOut(250);
		var url = "../../../js/Charts/Column2D.swf";
    	$("#p_1_2_2").empty();
    	MultDimenAnalyAction.getMapCountyData($("#date").val(), code, title, function(res){
    		multDimenAnaly.buildPic(res, "p_1_2_2", url);
    	});
    };
    function tt(){
    	
    }
    
    this.buildPic = function(data, divid, url){
    	if(data.indexOf("dataset")==-1 & data.indexOf("categories")==-1 &data.indexOf("set")==-1 &data.indexOf("entity")==-1){
    		$('#detail').html("无数据...");
    		return;
    	}
    	var chart = new FusionCharts(url, "ChartId", "100%", "100%");  
        chart.setDataXML(data);    
        chart.render(divid); 
    };
    
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

var multDimenAnaly = new MultDimenAnaly();
//执行
$(document).ready(function(){
//	 alert(userNameen);
//	 alert(userNamecn);
	multDimenAnaly.init();
	
});