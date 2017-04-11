
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
//var dateTime= $("dateTime").value;
 //定义另外的函数方法
dwrCaller.addAutoAction("queryAllSubMenu","ChannelIndexAction.queryAllSubMenu","119007",user.userId);

 //记录日志
//dwrCaller.addAutoAction("menuVisitLog", "MenuVisitLogAction.writeMenuLog", function() {});

//if (menuId != undefined || menuId != null){
//        dwrCaller.executeAction("menuVisitLog", [
//            {menuId:menuId}
//        ]);
//   }
//执行这个方法
dwrCaller.executeAction("queryAllSubMenu",function(data){
	 if(data){
	    var str="";
	    var menuId="";
	    var menuName="";
	 	 data=dhx.isArray(data)?data:[data];
	 	 for(var i=0;i<data.length;i++){
	 	  str +="<li class='line'><a href='#' menuName="+data[i].MENU_NAME+" menuId="+data[i].MENU_ID+"><img src="+data[i].IMG_URL+" width='120' height='120' /><p>"+data[i].MENU_NAME+"</p></a></li>";
	 	 	if(i==0){
	 	 		menuId=data[0].MENU_ID;
	 	 		menuName=data[0].MENU_NAME;
	 	 	}
	 	 }
	 	  $("#toppavId").empty().append(str);
	 	 delete str;
	 	 subMenuData(menuId,menuName);
	 	 subMenuDataNo(menuId,menuName);	 	 
	 }
	 
		$(function(){ 
		     //执行点击事件
		    $('.line a').bind('click', function() {
		      	var menuId = $(this).attr("menuId");
		      	var menuName = $(this).attr("menuName");
			     subMenuData(menuId,menuName);
			     $("#rightId").empty();
			     //subMenuDataNo(menuId,menuName);
		   	});
		   	
		   	 $('.ali').live('click', function() {
		      	var menuId = $(this).attr("subMenuID");
		      	var menuName = $(this).attr("subMenuName");
			     subMenuDataNo(menuId,menuName);
		   	});
		   	 $('.div_05').live('click', function() {
		      	var menuId = $(this).attr("subMenuID");
		      	var menuName = $(this).attr("subMenuName");
			     
			     subMenuDataNo(menuId,menuName);
		   	});
		   	
		   	$('.td_class').live('click', function() {
		      	var menuId = $(this).attr("sub2MenuID");
		      	var menuName = $(this).attr("sub2MenuName");
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
				    }			    }
		   	});
		    
		  });
  
});
/***
***处理二级菜单数据加载
***/
function subMenuData(menuId,menuName){
	  ChannelIndexAction.querySubMenu(menuId,user.userId,function(res){
	    var str2="";
		 	 if(res.length>0){
		 	    res=dhx.isArray(res)?res:[res];
		 	  for(var k=0;k<res.length;k++){
		 	     if(k==0){
		 	         menuId=res[k].MENU_ID;
		 	         menuName=res[k].MENU_NAME;
		 	     }
		 	     str2 +="<li><div class='a1'></div><a class='ali' href='#' subMenuName="+res[k].MENU_NAME+" subMenuID="+res[k].MENU_ID+">"+res[k].MENU_NAME+"</a></li>";
		 	   //  str2 +='<tr><td><div class="div_04" subMenuName='+res[k].MENU_NAME+' subMenuID='+res[k].MENU_ID+'>'+res[k].MENU_NAME+'</div></td></tr>';  
		 	   }
		 	 }else{
		 	    str2="<li><div class='a1'></div><a class='ali' href='#' subMenuName="+menuName+" subMenuID="+menuId+">"+menuName+"</a></li>";
		 	 }
		 	  
		 	   $("#leftId").empty().append(str2);
		 	  delete str2;
		 	 subMenuDataNo(menuId,menuName);
		 	  
	  });

}
/***
***处理最后根报表加载
***/
function subMenuDataNo(menuId,menuName){
 	ChannelIndexAction.querySubMenu2(menuId,user.userId,function(res){
 	if(res.length>0){
		 res=dhx.isArray(res)?res:[res];
		 var str2="<tr>";
		 var str1="<tr>";
		 var str3="<tr>";
		 var str4="<tr>";
		 var str="";
		 var str0="";
		 for(var k=0;k<res.length;k++){
			if(res[k].ARRT==0){
			 	if(k==0){
			 	  str0="<tr>";
			 	}
			 	if(k%4==0 && k>0){
			 	   str0 +="</tr><tr>"
			 	   str0 +='<td><img src="images/home1_r10_c8.jpg" width="12" height="12" /><a href="#" class="td_class" sub2MenuName='+res[k].MENU_NAME+' menuUrl='+res[k].MENU_URL+' sub2MenuID='+res[k].MENU_ID+'>'+res[k].MENU_NAME+'</a></td>';  
			 	}else{
			 	 str0 +='<td><img src="images/home1_r10_c8.jpg" width="12" height="12" /><a href="#" class="td_class" sub2MenuName='+res[k].MENU_NAME+' menuUrl='+res[k].MENU_URL+' sub2MenuID='+res[k].MENU_ID+'>'+res[k].MENU_NAME+'</a></td>';  
				}
				if(k==res.length-1){
					str0 +="</tr>";
				}				
			}

			if(res[k].ARRT==1){
				 str1+='<td><img src="images/home1_r10_c8.jpg" width="12" height="12" /><a href="#" class="td_class" sub2MenuName='+res[k].MENU_NAME+' menuUrl='+res[k].MENU_URL+' sub2MenuID='+res[k].MENU_ID+'>'+res[k].MENU_NAME+'</a></td>';  
			    //str1+='<td class="td_class" sub2MenuName='+res[k].MENU_NAME+' menuUrl='+res[k].MENU_URL+' sub2MenuID='+res[k].MENU_ID+'><img src="images/home1_r10_c8.jpg" width="12" height="12" />'+res[k].MENU_NAME+'</td>';  
			}
			if(res[k].ARRT==2){
			    str2+='<td><img src="images/home1_r10_c8.jpg" width="12" height="12" /><a href="#" class="td_class" sub2MenuName='+res[k].MENU_NAME+' menuUrl='+res[k].MENU_URL+' sub2MenuID='+res[k].MENU_ID+'>'+res[k].MENU_NAME+'</a></td>';  
			}
			if(res[k].ARRT==3){
			    str3+='<td><img src="images/home1_r10_c8.jpg" width="12" height="12" /><a href="#" class="td_class" sub2MenuName='+res[k].MENU_NAME+' menuUrl='+res[k].MENU_URL+' sub2MenuID='+res[k].MENU_ID+'>'+res[k].MENU_NAME+'</a></td>';  
			}			 
			if(res[k].ARRT==4){
			    str4+='<td><img src="images/home1_r10_c8.jpg" width="12" height="12" /><a href="#" class="td_class" sub2MenuName='+res[k].MENU_NAME+' menuUrl='+res[k].MENU_URL+' sub2MenuID='+res[k].MENU_ID+'>'+res[k].MENU_NAME+'</a></td>';  
			}			 
		  }
		  str1+="</tr>";
		  str2+="</tr>";
		  str3+="</tr>";
		  str4+="</tr>";
		  
		  str=str0+str1+str2+str3+str4;
	  }	
	  	 
		  $("#baoBiaoTable").empty().append(str);
		   delete str1;
		   delete str2;
		   delete str3;
		   delete str4;
		   delete str0;		   
		   delete str;
 	
 	});
}
