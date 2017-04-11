/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user=getSessionAttribute("user");
var dwrCaller=new biDwrCaller();

var indexLayout;
//当前系统的主页Path
var base = getBasePath();
 

dwrCaller.addAutoAction("queryFavoriteMenu","ChannelIndexAction.queryFavoriteMenu",user.userId);

dwrCaller.executeAction("queryFavoriteMenu",function(data){
	if(data){
		var str="";
 	 	data=dhx.isArray(data)?data:[data];	
 	 	 for(var i=0;i<data.length;i++){
			 	if(i==0){
			 	  str="<tr>";
			 	}
			 	if(i%4==0 && i>0){
			 	   str +="</tr><tr>"
			 	   str +="<td width='150' class='table_inside td'><a href='#' class='td_a'  menuName="+data[i].MENU_NAME+" menuUrl="+data[i].MENU_URL+" menuID="+data[i].MENU_ID+"><img src='images/btn_0"+i+".gif' width='150' height='150' /><p>"+data[i].MENU_NAME+"</p></a></td>";  
			 	}else{
			 	   str +="<td width='150' class='table_inside td'><a href='#' class='td_a'  menuName="+data[i].MENU_NAME+" menuUrl="+data[i].MENU_URL+" menuID="+data[i].MENU_ID+"><img src='images/btn_0"+i+".gif' width='150' height='150' /><p>"+data[i].MENU_NAME+"</p></a></td>";  
				}
				if(i==data.length-1){
					str +="</tr>";
				}				
	 	 }	 	
 	 	$("#table_01").empty().append(str);	
	}

  		$(function(){
 		   	$('.td_a').live('click', function() {
 		    
		      	var menuId = $(this).attr("menuID");
		      	var menuName = $(this).attr("menuName");
			    var menuUrl=$(this).attr("menuUrl");
			    //记录日志
			     MenuVisitLogAction.writeMenuLog({menuId:menuId}); 			    
			    if(menuUrl.indexOf("8081") > 0){
			    	parent.openTreeTab(menuId,menuName, menuUrl, 'top');
			    }else{
				    if(menuUrl.indexOf("?") > 0){
				    	parent.openTreeTab(menuId,menuName, base+'/'+menuUrl+'&menuId='+menuId, 'top');
				    }else{
				     
				       parent.openTreeTab(menuId,menuName, base+'/'+menuUrl+'?menuId='+menuId, 'top');
				    }			  
			  }
		   	}); 	
  
 		 });


});
