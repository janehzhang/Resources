/**
 * 页面初始化。
 */

dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var user= getSessionAttribute("user");
var dwrCaller=new biDwrCaller();
//当前系统的主页Path
var base = getBasePath();
var linkName = $("linkName").value;
dwrCaller.addAutoAction("getInitDataList","BusiStepConfigAction.getInitDataList",linkName);

dwrCaller.addAutoAction("getLeaf1DataByParam", "BusiStepConfigAction.getLeaf1DataByParam");

var init=function(){
	dwrCaller.executeAction("getInitDataList",function(data){
	   if(data&&data.list.length>0){
			var tableStr="<table id='mytbl' border='0' cellpadding='0' cellspacing='0' class='tab_01'>"
						 +"<tr>"
						    +"<td height='36' bgcolor='#cde5fd'><strong>生命周期</strong></td>"
						    +"<td bgcolor='#cde5fd'><strong>环节权重(%)</strong></td>"
						    +"<td bgcolor='#cde5fd'><strong>客户服务诉求<a href='#' onClick='openWinLevel_1()' id='a' ><img src='"+base+"/images/add.gif' width='17' height='17' align='absmiddle' /></a></strong></td>"
						    +"<td bgcolor='#cde5fd'><strong>诉求比重(%)</strong></td>"
						    +"<td bgcolor='#cde5fd'><strong>诉求权重(%)</strong></td>"
						    +"<td bgcolor='#cde5fd'><strong>指标<a href='#'        onClick='openWinLevel_2()' id='a' ><img src='"+base+"/images/add.gif' width='17' height='17' align='absmiddle' /></a></strong></td>"
						    +"<td bgcolor='#cde5fd'><strong>指标指向</strong></td>"
						    +"<td bgcolor='#cde5fd'><strong>指标权重(%)</strong></td>"
						  +"</tr>";
		       for(var i=0;i<data.list.length;i++){
		    	       tableStr =tableStr
		    	        +"<tr>"
		    	       	   +"<td>"+data.list[i].step1Name+"</td>"
                           +"<td>"+data.list[i].weight1+"</td>"
		    	       	   
                           +"<td id='"+data.list[i].step2Id+"' onmouseover='showValue(this)' onmouseout='hideValue(this)'>"
                                      +data.list[i].step2Name+"&nbsp;&nbsp;" 
                                      +"<span class='hidespan'><a href='#' onClick='remove("+data.list[i].step2Id+")' id='a'>"
						              +"<img src='"+base+"/images/remov.gif' /></a></span>" 
                            +"</td>"
                           
                           
                           +"<td>"+data.list[i].percentate2+"</td>"
			    		   +"<td>"+toDecimal((data.list[i].percentate2*data.list[i].weight1)/100,4) +"</td>"
			    		  
			    		   +"<td id='"+data.list[i].step3Id+"' onmouseover='showValue(this)' onmouseout='hideValue(this)'>" 
						          +data.list[i].step3Name+"&nbsp;&nbsp;" 
						    
						          +"<span class='hidespan'><a href='#' onClick='remove("+data.list[i].step3Id+")' id='a'>"
						          +"<img src='"+base+"/images/remov.gif' /></a></span>" 
						          
						   +"</td>"
						   
						   +"<td>"+data.list[i].direction3+"</td>"
						   +"<td>"+toDecimal(data.list[i].weight3,4)+"</td>"
		    	        +"</tr>";
		       }
			  
		       $('linkTable').innerHTML=tableStr;
		       autoRowSpan2(mytbl,0,2);
		       autoRowSpan1(mytbl,0,0);
		       
		}
	});
}

var selectData=function(value){
    dwrCaller.addAutoAction("getInitDataList","BusiStepConfigAction.getInitDataList",value);
	init();
}


//新增层级一
var openWinLevel_1 = function(){
    var dhxWindow = new dhtmlXWindows();
    var winSize=Tools.propWidthDycSize(15,20,15,20);
    winSize.width=400;
    dhxWindow.createWindow("importWindow", 0, 0, 750, 580);
    var importWindow = dhxWindow.window("importWindow");
    importWindow.setModal(true);
    importWindow.setDimension(winSize.width, 300);
    importWindow.center();
    importWindow.denyResize();
    importWindow.denyPark();
    var buttonstr = "";
	importWindow.setText("新增客户服务诉求");	
	importWindow.keepInViewport(true);
    importWindow.show();
	 var dhxLayout = new dhtmlXLayoutObject(importWindow, "1C");
    dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.hideHeader();
    topCell.setHeight(150);
    topCell.fixSize(true,true);

    var importform = topCell.attachForm(
             [
	        {type:"settings", position: "label-left"},
	        {type:"block",className:"blockStyle",list:[
	            {type:"select",label:"生命周期：",name:"parentId",offsetLeft : 10,offsetTop : 10,inputHeight:22,inputWidth: 120},
                {type:"newcolumn"}
	        ]},
	        {type:"block",className:"blockStyle",list:[
	    	    {type:"input",label:"诉求名称：",name:"kfname",inputHeight:17,inputWidth: 150}
	        ]},
	        {type:"block",className:"blockStyle",list:[
	    	    {type:"input",label:"诉求比重：",name:"bz",inputHeight:17,inputWidth: 150}
	        ]},
	      
	     /**
	        {type:"block",className:"blockStyle",list:[
	    	    {type:"input",label:"诉求权重：",name:"weight",inputHeight:17,inputWidth: 150}
	        ]},
	        **/
	       
	        {type:"block",className:"blockStyle",list:[
	           {type:"select",label:"指标指向：",name:"direction",offsetLeft : 10,offsetTop : 10,inputHeight:22,inputWidth: 40},
                {type:"newcolumn"}
	        ]},
	        {type:"block",offsetTop:20,inputTop :20,list:[
	           {type:"button",name:"submit",value:"提 交",offsetLeft : 100,offsetTop : 0},
	           {type:"newcolumn"},
	           {type:"button",name:"close", value:"关 闭",offsetLeft : 0,offsetTop : 0}
	        ]}
	        
	    ]
    );
     dwrCaller.executeAction("getLeaf1DataByParam",function(data){
     var parentIdSelect= importform.getSelect("parentId");
        var  count=0;
        for(var i=0;i<data.length;i++){
        	 parentIdSelect.options[count++]=new Option(data[i].BUSI_STEP_NAME,data[i].BUSI_STEP_ID);
        }
      }) ;
    
     var directionSelect= importform.getSelect("direction");
     directionSelect.options[0] = new Option("-1","-1");
     directionSelect.options[1] = new Option("1","1");
     
     importform.attachEvent("onButtonClick", function(id) {
    	if (id == "submit"){
    		   importWindow.close();
    		   dhx.showProgress("正在执行保存数据....");
    	    BusiStepConfigAction.saveConfig(importform.getFormData(),function(rs){
            		dhx.closeProgress();
            		if (rs) 
            		{
		                dhx.alert("数据保存成功！");
		                init();
		            } else 
		            {
		                dhx.alert("数据保存失败，请重试！");
		            }
            	});
        }
       if (id == "close"){
    		importWindow.close();
        }
    });
     
    
 }

//新增层级二
var openWinLevel_2 = function(){
    var dhxWindow = new dhtmlXWindows();
    var winSize=Tools.propWidthDycSize(15,20,15,20);
    winSize.width=400;
    dhxWindow.createWindow("importWindow", 0, 0, 750, 580);
    var importWindow = dhxWindow.window("importWindow");
    importWindow.setModal(true);
    importWindow.setDimension(winSize.width, 300);
    importWindow.center();
    importWindow.denyResize();
    importWindow.denyPark();
    var buttonstr = "";
	importWindow.setText("新增指标");	
	importWindow.keepInViewport(true);
    importWindow.show();
	 var dhxLayout = new dhtmlXLayoutObject(importWindow, "1C");
    dhxLayout.hideConcentrate();
    dhxLayout.hideSpliter();
    var topCell = dhxLayout.cells("a");
    topCell.hideHeader();
    topCell.setHeight(150);
    topCell.fixSize(true,true);

    var importform = topCell.attachForm(
             [
	        {type:"settings", position: "label-left"},
	        {type:"block",className:"blockStyle",list:[
	            {type:"select",label:"生命周期：",name:"parentId",offsetLeft : 10,offsetTop : 10,inputHeight:22,inputWidth: 150,options:[{value:"",text:"==请选择==",selected:true}]},
                {type:"newcolumn"}
	        ]},
	       
	        {type:"block",className:"blockStyle",list:[
	            {type:"select",label:"客户诉求：",name:"p_parentId",offsetLeft : 10,offsetTop : 10,inputHeight:22,inputWidth: 150,options:[{value:"",text:"==请选择==",selected:true}]},
                {type:"newcolumn"}
	        ]},
	        
	        {type:"block",className:"blockStyle",list:[
	    	    {type:"input",label:"指标名称：",name:"kfname",offsetLeft : 10,inputHeight:17,inputWidth: 150}
	        ]},
	        {type:"block",className:"blockStyle",list:[
	    	    {type:"input",label:"指标权重：",name:"weight",offsetLeft : 10,inputHeight:17,inputWidth: 150}
	        ]},
	       
	        {type:"block",className:"blockStyle",list:[
	           {type:"select",label:"指标指向：",name:"direction",offsetLeft : 10,offsetTop : 10,inputHeight:22,inputWidth: 40},
                {type:"newcolumn"}
	        ]},
	        {type:"block",offsetTop:20,inputTop :20,list:[
	           {type:"button",name:"submit",value:"提 交",offsetLeft : 100,offsetTop : 0},
	           {type:"newcolumn"},
	           {type:"button",name:"close", value:"关 闭",offsetLeft : 0,offsetTop : 0}
	        ]}
	        
	    ]
    );
    
    
    dwrCaller.executeAction("getLeaf1DataByParam",function(data){
     var parentIdSelect= importform.getSelect("parentId");
         parentIdSelect.length=0;//清空
         parentIdSelect.options[0]=new Option("==请选择==","");
        var  count=1;
        for(var i=0;i<data.length;i++){
        	 parentIdSelect.options[count++]=new Option(data[i].BUSI_STEP_NAME,data[i].BUSI_STEP_ID);
        }
      }) ;
    
    // var sysId=dwr.util.getValue(importform.getSelect("parentId"));
    //a.options[a.selectedIndex].value
    
   importform.attachEvent("onChange", function (id, value){
    	if(id=="parentId"){
    		 var p_parentIdSelect= importform.getSelect("p_parentId");
		     var  count=0;
		      p_parentIdSelect.length=0;//清空
           if(value != ""){
		    		BusiStepConfigAction.findSonNote(value,function(data){
		            		 for(var i=0;i<data.length;i++){
		            		   p_parentIdSelect.options[count++]=new Option(data[i].BUSI_STEP_NAME,data[i].BUSI_STEP_ID);
		            		 }
		             });
    	    }else{
    	            p_parentIdSelect.options[0]=new Option("==请选择==","");
    	    }
    	}
	   
	});
    
 
     
     var directionSelect= importform.getSelect("direction");
     directionSelect.options[0] = new Option("-1","-1");
     directionSelect.options[1] = new Option("1","1");
     
     importform.attachEvent("onButtonClick", function(id) {
    if (id == "submit"){
    		   importWindow.close();
    		   dhx.showProgress("正在执行保存数据....");
    		   BusiStepConfigAction.saveThreeConfig(importform.getFormData(),function(rs){
            		dhx.closeProgress();
            		if (rs) 
            		{
		                dhx.alert("数据保存成功！");
		                init();
		            } else 
		            {
		                dhx.alert("数据保存失败，请重试！");
		            }
            	});
        }
       if (id == "close"){
    		importWindow.close();
        }
    });
     
    
 }
var onCheck=function (obj){
		     obj.style.color="red";
		     obj.style.border="1px solid red";
 }
//修改权重
var onWeight=function(obj){
		     obj.style.color="";
		     obj.style.border="none";
		     var  formData=new Object();
			     formData.ids=obj.ids;
				 formData.values=obj.value;
		         BusiStepConfigAction.updateWeight(formData,function(rs){
            		if (rs) 
            		{
		                //dhx.alert("数据修改成功！");
            			init();
		            } else 
		            {
		                dhx.alert("数据修改失败，请重试！");
		            }
            	});
        
}
//修改比重
var onPercentate=function(obj){
			 obj.style.color="";
		     obj.style.border="none";
		     var  formData=new Object();
			     formData.ids=obj.ids;
				 formData.values=obj.value;
		         BusiStepConfigAction.updatePercentate(formData,function(rs){
            		if (rs) 
            		{
		                //dhx.alert("数据修改成功！");
            			init();
		            } else 
		            {
		                dhx.alert("数据修改失败，请重试！");
		            }
            	});
}
//修改指向
var onDirection=function(obj){
			 obj.style.color="";
		     obj.style.border="none";
		     var  formData=new Object();
			     formData.ids=obj.ids;
				 formData.values=obj.value;
		         BusiStepConfigAction.updateDirection(formData,function(rs){
            		if (rs) 
            		{
		                //dhx.alert("数据修改成功！");
		            } else 
		            {
		                dhx.alert("数据修改失败，请重试！");
		            }
            	});
}
//删除
var remove=function(id){
	dhx.confirm("您真的要删除此条数据吗！",function(r){
    if(r){
    	 BusiStepConfigAction.deleteConfig(id,function(rs){
            		if (rs) 
            		{
		                dhx.alert("数据删除成功！");
		                init();
		            } else 
		            {
		                dhx.alert("数据删除失败，请重试！");
		            }
            	});
	    }	
    });
}
//显示
var showValue=function(obj){
  if(obj.id != ""){
		obj.style.backgroundColor='#eef6fe';
		obj.childNodes[1].style.display="inline-block";
	}
}
//隐藏
var hideValue=function(obj){
    obj.style.backgroundColor='#ffffff';
    obj.childNodes[1].style.display="none";
}
//四舍五入 
var toDecimal=function(x) {     
         var f = parseFloat(x);     
         if (isNaN(f)) {       
           return;      
        }            
        f = Math.round(x*10000)/10000;        
         return f;
 } 
var autoRowSpan2=function(tb,row,col){
        var lastValue="";
        var value="";
        var pos=1;
        for(var i=row;i<tb.rows.length;i++)
        {
            value = tb.rows[i].cells[col].innerText;
            if(lastValue == value)
            {
                tb.rows[i].deleteCell(col);
                tb.rows[i-pos].cells[col].rowSpan = tb.rows[i-pos].cells[col].rowSpan+1;
                
                
                tb.rows[i].deleteCell(col);
                tb.rows[i-pos].cells[col+1].rowSpan = tb.rows[i-pos].cells[col+1].rowSpan+1;
                
                tb.rows[i].deleteCell(col);
                tb.rows[i-pos].cells[col+2].rowSpan = tb.rows[i-pos].cells[col+2].rowSpan+1;
                
                pos++;
            }else{
                lastValue = value;
                pos=1;
            }
        }
}
var autoRowSpan1=function(tb,row,col){
        var lastValue="";
        var value="";
        var pos=1;
        for(var i=row;i<tb.rows.length;i++)
        {
            value = tb.rows[i].cells[col].innerText;
            if(lastValue == value)
            {
                tb.rows[i].deleteCell(col);
                tb.rows[i-pos].cells[col].rowSpan = tb.rows[i-pos].cells[col].rowSpan+1;
                
                tb.rows[i].deleteCell(col);
                tb.rows[i-pos].cells[col+1].rowSpan = tb.rows[i-pos].cells[col+1].rowSpan+1;
                pos++;
            }else{
                lastValue = value;
                pos=1;
            }
        }
}
dhx.ready(init);