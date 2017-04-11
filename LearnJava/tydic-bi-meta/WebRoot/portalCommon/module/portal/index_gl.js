/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
var moduleMenu = null; 
var resData={};
var menuTab=null;
var pageSize = 12;//页面显示条数
var dataCounts = 0;//数据总数
var pageCounts = 0;//总页数
var globZoneTree = null;
var indexCdColWaring;//预警集合
function getCookieVal (offset) 
{
   var endstr = document.cookie.indexOf (";", offset);
   if (endstr == -1)
      endstr = document.cookie.length;
   return unescape(document.cookie.substring(offset, endstr));
	}

function GetCookie(name) 
{
   var arg = name + "=";
   var alen = arg.length;
   var clen = document.cookie.length;
   var i = 0;
   while (i < clen) 
      {
      var j = i + alen;
      if (document.cookie.substring(i, j) == arg)
         return getCookieVal (j);
      i = document.cookie.indexOf(" ", i) + 1;
      if (i == 0) 
         break; 
      }
   return null;
 }
 function SetCookie(name, value) 
 {
   var argv = SetCookie.arguments;
   var argc = SetCookie.arguments.length;
   var expires=new Date(); 
   expires.setTime(expires.getTime()+(3650*24*60*60*1000)); 
   var path = (3 < argc) ? argv[3] : null;
   var domain = (4 < argc) ? argv[4] : null;
   var secure = (5 < argc) ? argv[5] : false;
   document.cookie = name + "=" + escape (value) +
     ((expires == null) ? "" : ("; expires=" + expires.toGMTString())) +
     ((path == null) ? "" : ("; path=" + path)) +
     ((domain == null) ? "" : ("; domain=" + domain)) +
	 ((secure == true) ? "; secure" : "");
 }
/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCode");
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
var zoneConverter=dhx.extend({idColumn:"zoneCode",pidColumn:"zoneParCode",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);

dwrCaller.addAction("querySubZoneCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZoneCode(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
/** Tree head **/
//by qx
tZoneCode=$("zoneCode").value;
tDefaultZoneCode=$("defaultZoneCode").value;
if(tZoneCode!=tDefaultZoneCode){
    $("changeCode").value='1';
    $("changeName").value="横向对比";
}else{
	$("changeCode").value='2';
	$("changeName").value='横向对比';
}
var changeCode = $("changeCode").value;
var changeName=$("changeName").value;

var indexInit=function(){
	//1.加载地域树 
	dhx.showProgress("正在执行......");
    loadZoneTreeChkBox(tZoneCode,queryform);
    //2.初始化Tab标签
    

    loadTab($("tabId").value,changeCode);
//初始化公告列表
    excuteNotice(1,dataCounts,pageCounts,4);
    //初始化待办
    toDeal();
    
    
    
    

}
function  onSelect(obj){
  if(obj.value=='1'){ //日重点报表
		$("tdDateTime").innerHTML="日 期:";
	    $("divDay").style.display =   "block";
	    $("divMonth").style.display = "none";
	}else{//月重点报表
		$("tdDateTime").innerHTML="月 份:";
	    $("divDay").style.display =   "none";
	    $("divMonth").style.display = "block";
	}
     changeCode=$("changeCode").value;
     currentTab='投诉类';
     loadTab($("tabId").value,changeCode);	 
}
//查询
var queryData=function(){
	 changeCode=getChangeCode(); //by qx
	 $("changeName").value="横向对比";
	 buildGrid(changeCode);//加载表格
	 //加载预警信息
     loadIndexCdWarn();
     //加载指标解释
     loadExp();
     return true;
}

function loadTab(tabId,changeCode){
   var map=new Object();
       map.tabId=tabId;
       map.changeCode=changeCode;
    $("rpt_tabbar").innerHTML="";
    menuTab= new dhtmlXTabBar("rpt_tabbar", "top");
    menuTab.setSkin('dhx_skyblue');
    menuTab.setImagePath("../../../meta/resource/dhtmlx/imgs/");
    menuTab.setHrefMode("ajax-html");
    menuTab.enableTabCloseButton(false);
    menuTab.enableScroll(false);
    menuTab.enableAutoReSize(true);
    menuTab.setSize(width-5,height+10);
    moduleMenu =new Object();
    map.zoneCode= $("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
    if(tabId=="1"){
          map.dataDate=$("dayTime").value;
    }else{
          map.dataDate=$("monthTime").value;
    }
    PortalCommonCtrlr.getViewTabs(map,function (res) { 
         if (res == null) {
            $("rpt_tabbar").innerHTML = "获取数据失败！";
            return;
        }
        if (res.length == 0) {
            $("rpt_tabbar").innerHTML = "未配置菜单项或者您无权限查看重点指标！";
            return;
        }
        for(i=0;i<res.length-1;i++){				//把关键环节的位置放到tab数组的的最后
        	var key=res[i].TYPE_ID;
    	    var value=res[i].TYPE_NAME;
        	if(value=="关键环节"){
        		res[i].TYPE_ID=res[i+1].TYPE_ID;
        		res[i].TYPE_NAME=res[i+1].TYPE_NAME;
        		res[i+1].TYPE_ID=key;
        		res[i+1].TYPE_NAME=value;
        	}
        }
       for (var i=0; i<res.length ; i++) {
    	    var key=res[i].TYPE_ID;
    	    var value=res[i].TYPE_NAME;
    	    if (currentTab == "") {
                currentTab = value;
            }
            menuTab.addTab(value, formatTabname(value), getShowLen(value, charPoint, minTabLen));
            
            var ik = document.createElement("DIV");
            ik.id = "menu_tab_" + key;
            document.body.appendChild(ik);
            menuTab.setContent(value, ik.id);
            ik.style.width  = "100%";
            ik.style.height = "100%";
            moduleMenu[value]=ik.id;
            ik.innerHTML = value;
       }
       menuTab.setTabActive(currentTab);//getActiveTab()
       menuTab.attachEvent("onSelect", checkDataFresh);
       checkDataFresh(currentTab);
    });
}

//检查条件是变化并刷新数据
function checkDataFresh() {
    if (arguments.length){
        currentTab = arguments[0];
    }
       //加载预警信息
       loadIndexCdWarn();
    tZoneCode=$("zoneCode").value;
   	tDefaultZoneCode=$("defaultZoneCode").value;
   	if(tZoneCode!=tDefaultZoneCode){
   		changeCode1="1";
   	}else{
   		changeCode1=changeCode;
   	}
       buildGrid(changeCode1);
      //加载指标解释
       loadExp();
      return true;
}

function  buildGrid(changeCode) {
	    var divId = moduleMenu[currentTab];
        var div =  $(divId);
        div.innerHTML = "<center>数据加载中，请稍等....<center>";
        var map=new Object();
            map.tabId =$("tabId").value;
            map.changeCode=changeCode;
            map.zoneCode= $("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
            map.typeId=currentTab;
         if($("tabId").value=="1"){
				  //标签ID,时间,地域
		          map.dataDate=$("dayTime").value;
		  }else{
		          map.dataDate=$("monthTime").value;
		 }
		PortalCommonCtrlr.getTableData(map, {callback:function (res) {
				        if (res == null) {
				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
				            return;
				        }
				       resData=res;
		               buildTable(res,div,changeCode);
		   }
		});
}
function buildTable(data,div,changeCode){
    var map=new Object();
        map.tabId=$("tabId").value;
        map.zoneCode= $("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
        map.changeCode=changeCode;
	var tableStr="";
	if($("tabId").value=="1"){
           tableStr ="<table id='mytbl_1' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
           tableStr+="<tr>"
					    +"<td bgcolor='#cde5fd'><span class='title'>指标项</span></td>"
					    +"<td bgcolor='#cde5fd'><span class='title'>当日值</span></td>"
					    +"<td bgcolor='#cde5fd'><span class='title'>本月累计</span></td>"
					    +"<td bgcolor='#cde5fd'><span class='title'>上月同期累计</span></td>"
					    +"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
					    +"<td bgcolor='#cde5fd'><span class='title'>上年同期累计</span></td>"
					    +"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
					    +"<td bgcolor='#cde5fd'><span class='title'>本月累计平均值</span></td>"
					    //+"<td bgcolor='#cde5fd'></td>"
					+"</tr>";
   if(data&&data.length>0){  
       for(var i=0;i<data.length;i++){
        tableStr =tableStr+
           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
      
       if(data[i].ONE_MENU != null && data[i].ONE_MENU != "" )
       {
    	   tableStr +="<td align=\"left\" onclick=\"openMenuInfo('"+data[i].ONE_MENU +"','"+ data[i].URL+"','"+ data[i].TABID+"','"+ data[i].REPORTNAME+"','"+ data[i].INDEX_CD+"');\"  class='unl'>"+data[i].INDEX_NAME+"</td>"; 
       }
        else
       {
           tableStr +="<td align=\"left\">"+data[i].INDEX_NAME+"</td>";
       }
		    
   tableStr +="<td>"+addWaringStyle(data[i].VALUE2,data[i].INDEX_CD,'VALUE2')+"</td>"
		     +"<td>"+addWaringStyle(data[i].VALUE3,data[i].INDEX_CD,'VALUE3')+"</td>"
		     +"<td>"+addWaringStyle(data[i].VALUE4,data[i].INDEX_CD,'VALUE4')+"</td>"
		     +"<td>"+addWaringStyle(data[i].VALUE5,data[i].INDEX_CD,'VALUE5')+"</td>"
		     +"<td>"+addWaringStyle(data[i].VALUE6,data[i].INDEX_CD,'VALUE6')+"</td>"
		     +"<td>"+addWaringStyle(data[i].VALUE7,data[i].INDEX_CD,'VALUE7')+"</td>"
		     +"<td>"+addWaringStyle(data[i].VALUE8,data[i].INDEX_CD,'VALUE8')+"</td>"
		     //+"<td onclick=\"openArea(1,'日重点指标','"+ data[i].INDEX_CD+"','"+ data[i].INDEX_NAME+"');\" class='unl'>辖区</td>"
		   +"</tr>";
           
         if(i==0){
	          map.indexCd=data[i].INDEX_CD;
	          map.dataDate=$("dayTime").value;
	          map.field="VALUE2";
	          map.chartTitle='日重点指标->'+data[i].INDEX_NAME;
          }
       
       }
   }else{
	     tableStr =tableStr+
	      "<tr>"
	       +"<td colspan='10'>没有数据显示</td>"
	     +"</tr>";
   }    
	
	}else{
	      tableStr ="<table id='mytbl_2' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
          tableStr+="<tr>"
				    +"<td bgcolor='#cde5fd'><span class='title'>指标项</span></td>"
				    +"<td bgcolor='#cde5fd'><span class='title'>当月值</span></td>"
				    +"<td bgcolor='#cde5fd'><span class='title'>上月值</span></td>"
				    +"<td bgcolor='#cde5fd'><span class='title'>环比</span></td>"
				    +"<td bgcolor='#cde5fd'><span class='title'>上年同期累计</span></td>"
				    +"<td bgcolor='#cde5fd'><span class='title'>同比</span></td>"
				    +"<td bgcolor='#cde5fd'><span class='title'>本年平均值</span></td>"
				    //+"<td bgcolor='#cde5fd'></td>"
				+"</tr>";
    if(data&&data.length>0){  
	       for(var i=0;i<data.length;i++){
	        tableStr =tableStr+
	           "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";  
	   if(data[i].ONE_MENU != null && data[i].ONE_MENU != "" )
       {
	        tableStr +="<td align=\"left\" onclick=\"openMenuInfo('"+data[i].ONE_MENU +"','"+ data[i].URL+"','"+ data[i].TABID+"','"+ data[i].REPORTNAME+"','"+ data[i].INDEX_CD+"');\" class='unl'>"+data[i].INDEX_NAME+"</td>";	 
       }
	    else
       {
	    	tableStr +="<td align=\"left\" >"+data[i].INDEX_NAME+"</td>";
       }
			    
	   tableStr +="<td>"+addWaringStyle(data[i].VALUE2,data[i].INDEX_CD,'VALUE2')+"</td>"
			     +"<td>"+addWaringStyle(data[i].VALUE3,data[i].INDEX_CD,'VALUE3')+"</td>"
			     +"<td>"+addWaringStyle(data[i].VALUE4,data[i].INDEX_CD,'VALUE4')+"</td>"
			     +"<td>"+addWaringStyle(data[i].VALUE6,data[i].INDEX_CD,'VALUE6')+"</td>"
			     +"<td>"+addWaringStyle(data[i].VALUE7,data[i].INDEX_CD,'VALUE7')+"</td>"
			     +"<td>"+addWaringStyle(data[i].VALUE8,data[i].INDEX_CD,'VALUE8')+"</td>"
		         //+"<td onclick=\"openArea(2,'月重点指标','"+ data[i].INDEX_CD+"','"+ data[i].INDEX_NAME+"');\" class='unl'>辖区</td>"
			   +"</tr>";
		       if(i==0){
		          map.indexCd=data[i].INDEX_CD;
				  map.dataDate=$("monthTime").value;
		          map.field="VALUE2";
		          map.chartTitle='月重点指标->'+data[i].INDEX_NAME+'->当月值';
	             }
	       
	      }
   }else{
	     tableStr =tableStr+
	      "<tr>"
	       +"<td colspan='10'>没有数据显示</td>"
	     +"</tr>";
   } 
  }
     
    tableStr +="</table>"; 
    div.innerHTML=tableStr;
   
    //3.加载配置图形
    loadSetChart(map);
}

//加载配置图形
function loadSetChart(map){
	dhx.showProgress("正在执行......");
      PortalCommonCtrlr.loadSetChart(map, {callback:function (res) {
    	  dhx.closeProgress();
    	         if(res != null){
                        buildChart(res);
		         }else{
		        	  //$('top').innerHTML='<br/>没找到任何数据';
		         }
		   }
		});
}

//构建图形
function buildChart(res){
	var tableStr="<table id='topTable' width='100%' height='auto!important' border='0'  cellpadding='0px' cellspacing='2px'>";
	     tableStr +="<tr>";
			 for(var i=0;i<res.length;i++){
			   if((i+1)%2 != 0){
		tableStr +="<td width='50%'>"
						      +"<table style='border: 1px solid #87CEFF;' width='100%' height='auto!important'  border='0'  cellpadding='0' cellspacing='0'>"
							                +"<tr height='20px' style='background:url("+base+"/images/fpage_04.gif);'>"
							                   +"<td nowrap align='left' class='title_ma1'>"
							                      +"<span style='font:12px;font-weight:bold;' id='titleInfo'>"+res[i].chartTitle+"</span>"                                        
							                   +"</td>"
							                   	+"<td align='right'>"
							                   	+retSelect(res[i])
							                   	+"&nbsp;&nbsp;<input type='button' id='ps' name='ps' class='poster_btn1'  value='批 示'  onclick=\"psInfo()\"  style='width:60px;'/>"
							                   +"</td>"
							                 +"</tr>"
							                 
							                 +"<tr>"
							                        +"<td colspan='2'>"
							                              +"<div id='chartdiv_"+res[i].ID+"'></div>"
							                         +"</td>"
							                 +"</tr>"
							  +"</table>" 
			        +"</td>";
				}else{
		  tableStr +="<td width='50%'>"
						      +"<table style='border: 1px solid #87CEFF;' width='100%' height='auto!important'  border='0'  cellpadding='0' cellspacing='0'>"
							                +"<tr height='20px' style='background:url("+base+"/images/fpage_04.gif);'>"
							                   +"<td nowrap align='left' class='title_ma1'>"
							                      +"<span style='font:12px;font-weight:bold;' id='titleInfo'>"+res[i].chartTitle+"</span>"                                        
							                   +"</td>"
							                   	 +"<td align='right'>"
							                   	     +"<input type='button' id='city' name='city' class='poster_btn1'  value='"+$("changeName").value+"'  onclick=\"lookCity(this,'"+res[i].indexCd+"','"+res[i].SHOW_ID+"','"+res[i].ID+"')\"  style='width:70px;'/>"
							                     +"</td>"
							                 +"</tr>"
							                 
							                 +"<tr>"
							                        +"<td colspan='2'>"
							                               +"<div id='chartdiv_"+res[i].ID+"'></div>"
							                         +"</td>"
							                 +"</tr>"
							  +"</table>" 
			           +"</td>";
		  if((i+1)!= res.length)
		  {
		   tableStr +="</tr>"
                     +"<tr>";
		  }	
		}
	   }
	 tableStr+="</tr>";
	 tableStr +="</table>";
    $('top').innerHTML=tableStr;
    newChart(res);
}
//新建图形
function newChart(res){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;	
	 for(var i=0;i<res.length;i++){
		 if(res[i].ID=="1"){
		    var chart = new FusionCharts(base+"/js/Charts/"+res[i].CHART_NAME, "ChartId_"+cid, "100%", "200","0", "0");//MSLine.swf
			chart.setDataXML(res[i].XML);
			chart.render("chartdiv_"+res[i].ID);
					           }
		 else if(res[i].ID=="2"){
			var chart = new FusionCharts(base+"/js/Charts/"+res[i].CHART_NAME, "ChartId_"+cid1, "100%", "200","0", "0");//MSLine.swf
			chart.setDataXML(res[i].XML);
			chart.render("chartdiv_"+res[i].ID);
		 }
		}
	    $("tabId").style.display = "block";
	    $("div_src").style.display = "none";
        $("div_src").style.zindex = "-1";
        dhx.closeProgress();
}


function retSelect(res){
	var str ="<select id='pointer' name='pointer' style='width:auto' onchange=\"changeChart(this,'"+res.ID+"','"+
	res.SHOW_ID+"','"+res.SHOW_NAME+"','"+res.CHART_NAME+"','"+res.SHAPE_ID+"','"+res.CURRENT_COLOR+"','"+res.LAST_COLOR+"')\">"
			    +"<option value=''>==请选择==</option>";
   if(resData&&resData.length>0){  
       for(var i=0;i<resData.length;i++){  
		  if(resData[i].INDEX_CD==res.indexCd){
			  str += "<option selected value='"+resData[i].INDEX_CD+"'>"+resData[i].INDEX_NAME+"</option>";
		     }else{
    	      str += "<option          value='"+resData[i].INDEX_CD+"'>"+resData[i].INDEX_NAME+"</option>";
    	     }
	   }
   }
	 str += "</select>";
	return  str;
}
//通过指标来改变图形
function changeChart(obj,id,showId,showName,chartName,shapeId,currentColor,lastColor){
       var map=new Object();
      var indexCd=obj.value;
           map.tabId  =$("tabId").value;
           map.zoneCode =$("zoneCode").value;
	var tabName="";
	if($("tabId").value=="1"){
		tabName="日重点指标";
		   map.dataDate=$("dayTime").value;
		   map.chartTitle=tabName+'->'+obj.options[obj.options.selectedIndex].text;
		}else{
	    tabName="月重点指标";
	       map.dataDate=$("monthTime").value;
	       map.chartTitle=tabName+'->'+obj.options[obj.options.selectedIndex].text+'->当月值';
	    }
	       //map.id=id;
		   //map.chartName=chartName;
		  // map.shapeId=shapeId;
		   //map.currentColor=currentColor;
		   //map.lastColor=lastColor;
		   map.indexCd=indexCd;
	       map.field=showId;
	       
	       map.changeCode=$("changeCode").value;
	if(indexCd != ""){
		//1.Title
		//$("titleInfo_"+id).innerHTML=tabName+'->'+obj.options[obj.options.selectedIndex].text+'->'+showName;
		//2.Table
		//loadSingleChart(map);
		 loadSetChart(map);
	}
}


/**
function loadSingleChart(map){
	var chart = new FusionCharts(base+"/js/Charts/"+map.chartName, "ChartId_"+map.id, "100%", "200","0", "0");
	PortalCommonCtrlr.getSingleChart(map, {callback:function (res) {
							   chart.setDataXML(res);
					           chart.render("chartdiv_"+map.id);
	}});
}**/


function loadExp(){
var map=new Object();
    map.tabId=$("tabId").value;
    PortalCommonCtrlr.getIndexExp(map, {callback:function (data) {
    	indexExp(data);
	}});
}
function loadIndexCdWarn(){
	var tabId=$("tabId").value;
    /*根据tabId获取预警信息*/
    PortalCommonCtrlr.getIndexCdWaringMes(tabId,{
        async:false,
        callback:function(mapList) {
            indexCdColWaring = mapList;
        }
    });
}

function indexExp(data){
	var tableStr="<table width='100%' border='0'  cellpadding='0' cellspacing='0'>";
		for(var i=0;i<data.length;i++){
        tableStr +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
				      +"<td align=\"left\"><font style=\"font-weight:bold;\" >"+data[i].INDEX_NAME+'：'+"</font>"+data[i].INDEX_EXPLAIN+"</td>"
				  +"</tr>";
		}
	   tableStr +="</table>"; 
       index_exp.innerHTML=tableStr;
}

/**
 * 钻取
 * @param {Object} tabId
 * @param {Object} indexId
 */
function openArea(tabId,tabName,indexId,indexName){
	   var  dataDate="";
	
	   if(tabId=="1"){dataDate=$("dayTime").value;}else{dataDate=$("monthTime").value;}
        var  zoneCode= $("zoneCode").value;
        var url = base+"/portalCommon/module/portal/areaDrill.jsp?dataDate=" + dataDate + "&tabId=" + tabId
                   + "&indexId=" + indexId + "&indexName=" + indexName + "&zoneCode=" + zoneCode +"&tabName=" +tabName;
        url = encodeURI(url);
        var param="height="+screen.availHeight+"px,width="+screen.availWidth+"px,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no";
        window.open (url,'newwindow',param);
      //openTab(tabId + "_" + indexId, indexName, url, 0);
}

function openTab(menu_id, menu_name, url, type) {
    return parent.openMenu(menu_name, url, 'top');
}

var openMenu=function(menuName,menuUrl,target,menuId,isRefresh){
	     var param="height=500px,width=1200px,top=200,left=100,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no";
        window.open (base+menuUrl,'newwindow',param);
}
/**
 * 指标批示
 */
function psInfo(indexCd,indexName){
	var indexCd=$("pointer").value;
	var indexName=$("pointer").options[$("pointer").options.selectedIndex].text;
	if($("pointer").value==""){
		 dhx.alert("请选择指标项进行批示.");
		 return;
	}
    var dhxWindow = new dhtmlXWindows();
    dhxWindow.createWindow("addWindow", 0, 0, 250, 310);
    var addWindow = dhxWindow.window("addWindow");
    addWindow.setModal(true);
    addWindow.stick();
    addWindow.setDimension(550, 310);
    addWindow.center();
    addWindow.setPosition(addWindow.getPosition()[0], addWindow.getPosition()[1] - 100);
    addWindow.denyResize();
    addWindow.denyPark();
    addWindow.button("minmax1").hide();
    addWindow.button("park").hide();
    addWindow.button("stick").hide();
    addWindow.button("sticked").hide();
    addWindow.setText("批示内容");
    addWindow.keepInViewport(true);
    addWindow.show();
    //建立表单。
    var addForm = addWindow.attachForm([
        {type:"block", offsetTop:15, list:[
            {type:"input", offsetLeft:40, label:"批示名称：", inputWidth:370, name:"noticeTitle", validate:"NotEmpty,MaxLength[64]",value:indexName}
        ]},
        {type:"block",list:[
            {type:"input", offsetLeft:40, label:"批示标题：", inputWidth:370, name:"noticeTitle", validate:"NotEmpty,MaxLength[64]"},
            {type:"newcolumn"},
            {type:"label", label:"<span style='color: red'>*</span>"}
        ]},
        {type:"hidden", name:"noticeUser", value:userInfo['userId']},
        {type:"block", list:[
            {type:"input", offsetLeft:40, rows:4, label:"批示内容：", inputWidth:370, name:"noticeContent", validate:"NotEmpty,MaxLength[600]"},
            {type:"newcolumn"},
            {type:"label", label:"<span style='color: red'>*</span>"}
        ]},
        {type:"hidden", name:"noticeDisplayZones"},
        {type:"block", offsetTop:10, list:[
            {type:"button", label:"保存", name:"save", value:"保存", offsetLeft:200},
            {type:"newcolumn"},
            {type:"button", label:"关闭", name:"close", value:"关闭"}
        ]}
    ]);
    //添加验证
    //查询表单事件处理
       //查询表单事件处理
    addForm.attachEvent("onButtonClick", function (id) {    
    	if (id == "close") {
            addForm.clear();
            addWindow.close();
            dhxWindow.unload();
        }
    });
}

dwrCaller.addAutoAction("indexToDeal","SerProManageAction.indexToDeal");
var excuteNotice = function(currentPage,dataCounts,pageCounts,pageSize){
	var noticeStr = "";
	PortalCommonCtrlr.getNoticeByPara(currentPage,dataCounts,pageCounts,pageSize,{//初始化调用：第1页
		async:false,
		callback:function(obj){
			dataCounts = obj[0];
			pageCounts = obj[2];
			var data = obj[1];
			if(data != null){
				for(var i = 0; i < data.length; i++){
					 noticeStr +="<li class='li_2'><span>"+data[i]["INIT_DATE"]+"</span><a href=\"javascript:showNotice("+data[i]["NOTICE_ID"]+");\" style='cursor:hand'>"+data[i]["NOTICE_TITLE"]+" </a></li>"
					            +"<div id='u156_line'></div>";
					 
				}
              for(var i=0;i<data.length;i++){
				if(data[i].POP_STATE==1){
					showNotice(data[i].NOTICE_ID);
					setTimeout("closeLayer()",10000);
				
				}
              }
			}
		}
	});
	document.getElementById("noticeList").innerHTML = noticeStr;
}

/**
 * 打开公告信息列表
 * @param groupId
 */
function openNotice(groupId){
	openMenu("公告信息","/portalCommon/module/portal/noticeList.jsp","top")
}
/**
 *打开登录排名
 * @param groupId
 */
function openVisitRank(groupId){
     openMenu("用户登录情况","/meta/module/mag/log/loginLog.jsp?groupId="+groupId,"top");
}
/**
 *打开登录排名
 * @param groupId
 */
function openApplyUser(){
     openMenu("账号申请","/meta/public/userRegister.jsp","top");
}
/**
 *打开省公司审核
 * @param groupId
 */
function openProvAduit(){
     openMenu("省公司审核","/meta/public/provicialAudit.jsp","top");
}
/**
 *打开分公司审核
 * @param groupId
 */
function openPartAduit(){
     openMenu("分公司审核","/meta/public/partAudit.jsp","top");
}
/**
 *打开应用排名
 * @param groupId
 */
function openAppRank(groupId){
    //openMenu("报表访问情况","/meta/module/mag/log/menuVisitInfo.jsp?groupId="+groupId,"top");
	  openMenu("用户访问情况","/meta/module/mag/log/userVisitNum.jsp?groupId="+groupId,"top");
}
//展示公告的详细信息
var showNotice = function(noticeId){
	var showMes = "";
	//查找公告信息
	PortalCommonCtrlr.getNoticeById(noticeId,{
		async:false,
		callback:function(data){
			//标题
			showMes += "<div class='show_content2' onMousedown=\"StartDrag(this)\" onMouseup=\"StopDrag(this)\" onMousemove=\"Drag(this)\">";
			showMes += "<a href=\"javascript:closeLayer();\" class=\"closed2\">× 关闭</a><h2 class=\"gg_title2\">"+data["NOTICE_TITLE"]+"</h2>";
			showMes += "<div class=\"xian2\"></div>";
			showMes += "<p><ul class=\"gg_info2\">";
			showMes += "<li>发布日期："+data["INIT_DATE"]+"</li>";
			//公告等级
			var levelname = levelName(1*data["NOTICE_LEVEL"]);
			showMes += "<li>公告等级：<span>"+levelname+"</span></li>";
			showMes += "</ul></p>";
			//公告内容
			showMes += "<p class=\"clear2\"></p>";
			showMes += "<p class=\"gg_content2\">公告内容：<br/>";
			showMes += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + data["NOTICE_CONTENT"] + "</p>";
		    showMes += "<br/><br/>";
			showMes += "<p><ul class=\"gg_info3\"><li>附件下载：<a href=\"#\" onclick=\"downloadattrs('"+data["AFFIX_PATH"]+"')\">"+data["AFFIX_NAME"]+"</a>&nbsp;&nbsp;&nbsp;&nbsp;</li>";
			showMes += "</ul></p></div>";
		}
	});
	//弹出div层
	//document.getElementById("showMesId").innerHTML = showMes;

	openLayer('showMesId',showMes,500,300);
   
}
//公告等级
var levelName = function(levelId){
	var levelName = "";
	if(levelId == 1)
		levelName = "一般";
	else if(levelId == 2)	
		levelName = "较急";
	else if(levelId == 3)	
		levelName = "加急";
	else if(levelId == 4)	
		levelName = "紧急";
	return levelName;
}
function downloadattrs(file)
{
	document.forms[0].target="hiddenFrame";
 	var url = base + "/portalCommon/module/serviceManage/serProManage/download.jsp?file="+ file;
	window.open(url,"hiddenFrame","");
}
//待办列表
var toDeal=function(){
    var loadParam=initParam();
	$("num_0").innerHTML='';//待处理
	$("num_1").innerHTML='';//处理中
	$("num_2").innerHTML='';//待评估
	$("num_3").innerHTML='';//归档
	var num_0=0;//待处理
	var num_1=0;
	var num_2=0;
	var num_3=0;
	dwrCaller.executeAction("indexToDeal",loadParam,function(data){
			if(data != null){
			        for(var i=0;i<data.length;i++){
			     			 if(data[i].MAIN_STATE==1){
			     				num_0=num_0+1;
							}else if(data[i].MAIN_STATE==2){
								num_1=num_1+1;
							}else if(data[i].MAIN_STATE==3){
							}else if(data[i].MAIN_STATE==4){
							}else if(data[i].MAIN_STATE==5){
								num_3=num_3+1;
							}else if(data[i].MAIN_STATE==6){
								num_2=num_2+1;
								}else{
								}
							}  
			        }
			slowExecute(num_0,num_1,num_2,num_3);
			
	});
}

function slowExecute(num_0,num_1,num_2,num_3){
	
    $("num_0").innerHTML=num_0;
	$("num_1").innerHTML=num_1;
	$("num_2").innerHTML=num_2;
	$("num_3").innerHTML=num_3;
}
//参数初始化公共部分
var initParam=function(){
    var loadParam = new biDwrMethodParam();
    loadParam.setParamConfig([
        {
            index:0,type:"fun",value:function() {
        	var  formData=new Object();
            return formData;
        }
        }
    ]);
    return loadParam;
}
/**
 * 打开待办列表
 * @param groupId
 */
function openToDeal(dealId){
	if(dealId=='1'){
	   openMenu("主单待处理","/portalCommon/module/serviceManage/serProManage/indexProblemMain.jsp?problemStep="+dealId,"top");
	}
	if(dealId=='2'){
		openMenu("副单待处理","/portalCommon/module/serviceManage/serProManage/indexProblemMain.jsp?problemStep="+dealId,"top");
	}
	if(dealId=='3'){
		openMenu("待确认","/portalCommon/module/serviceManage/serProManage/indexProblemMain.jsp?problemStep="+dealId,"top");
	}
	if(dealId=='4'){
		openMenu("被退回","/portalCommon/module/serviceManage/serProManage/indexProblemMain.jsp?problemStep="+dealId,"top");
	}
	if(dealId=='5'){
		openMenu("已归档","/portalCommon/module/serviceManage/serProManage/indexProblemMain.jsp?problemStep="+dealId,"top");
	}
	if(dealId=='6'){
		openMenu("待评估","/portalCommon/module/serviceManage/serProManage/indexProblemMain.jsp?problemStep="+dealId,"top");
	}
}

var lookCity = function (obj,indexCd,showId,chartId){
       var map=new Object();
           map.tabId  =$("tabId").value;
           map.zoneCode =$("zoneCode").value;
	   if($("tabId").value=="1"){
		   map.dataDate=$("dayTime").value;
		}else{
	       map.dataDate=$("monthTime").value;
	    }
		   map.indexCd=indexCd;
		   map.field=showId;
		   
		   tZoneCode=$("zoneCode").value;
	     	if(tZoneCode!='0000') { 
	 			  if(obj.value=='横向对比'){//地市
	 				  obj.value='返回';
	 				  map.changeCode="0";//地市同级
	 				  $("changeCode").value='0';
	 				 $("changeName").value="返回";
	 			  }else if(obj.value=='返回'){//地市
	 				  obj.value='横向对比';
	 				  map.changeCode="1";//地市下一级
	 				  $("changeCode").value='1';
	 				 $("changeName").value="横向对比";
	 			  }
	   		}else{
	   		     if(obj.value=='横向对比'){//省公司下一级
	 				  obj.value='返回';
	 				  map.changeCode="1";//省公司下一级
	 				$("changeCode").value='1';
	 				$("changeName").value="返回";
	 			  }else if(obj.value=='返回'){//省公司下两级
	 				  obj.value='横向对比';
	 				  map.changeCode="2";//省公司下两级
	 				$("changeCode").value='2';
	 				$("changeName").value="横向对比";
	 			  }
	   		} 
		dhx.showProgress("正在执行......");
		PortalCommonCtrlr.loadSet21AreaChart(map, {callback:function (res) {
			 dhx.closeProgress();
    	         if(res != null){
                        build21Chart(res,chartId);
		         }
		   }
		});
}
function  build21Chart(map,chartId){
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
   $("chartdiv_"+chartId).innerHTML="<center>正在加载.......</center>";
   var chart = new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId_"+cid1, "100%", "200","0", "0");//MSColumn2D.swf
   chart.setDataXML(map.XML);
   chart.render("chartdiv_"+chartId);
}

/**
 *  打开链接菜单
 * 
 */
 function openMenuInfo(oneMenu,url,tabId,reportName,ind){
		   url+="?dateTime="+$("dayTime").value+"&zoneCode="+$("zoneCode").value+"&ind="+ind;
	    parent.parent.menuChangeOut(oneMenu,url,tabId,reportName);
 }
 
function addWaringStyle(dataValue,indexCd,col)
{
	 //判断是否需要预警
	var isWaringFlag = "";
	    isWaringFlag = isWaring(indexCdColWaring,dataValue,indexCd,col);
	var fontStyle_s = "";//预警的颜色s
	var fontStyle_e = "";//预警的颜色e
	if(isWaringFlag != null && isWaringFlag != ""){
	    fontStyle_s = "<font style=\"background-color:red;\" title=\"预警:"+isWaringFlag+"\">&nbsp;&nbsp;";
	    fontStyle_e = "&nbsp;&nbsp;</font>";
	}
    return fontStyle_s+dataValue+fontStyle_e;
}
 //是否预警
var isWaring = function(indexArray,dataValue,indexCd,col){
    if(typeof indexArray == "undefined" || indexArray == undefined || indexArray == "undefined" || indexArray == null)
        return "";
    if(indexArray.length <= 0)
        return "";
    if(parseInt(dataValue) == "NaN"  && ( ""+dataValue.indexOf("%") > 0 || ""+dataValue.indexOf("‰")> 0 ) )
       return "";
    
    for(var r = 0; r < indexArray.length; r++){
    	var flag=false;
        var waringType = indexArray[r].WARING_TYPE;
        var value1 =     indexArray[r].WARING_VALUE;
        var value2 = "";
        if(waringType == "BTWEEN AND"){
                value2 = indexArray[r].WARING_VALUE2;
         }
        var columnId = indexArray[r].COLUMN_ID;
        var indexId=   indexArray[r].INDEX_CD;
         if(indexId==indexCd && columnId==col )
         {
            flag= judWaring(waringType,dataValue,value1,value2);
         }
         if(flag)
         {
            return waringType +"&nbsp;"+value1+"&nbsp;"+value2;
         }
    }
        return "";
}
var judWaring = function(waringType,dataValue,value,value2){
	dataValue=formatData(dataValue);
    if(waringType == ">="){
        if(1*dataValue >= 1*value)
            return true;
        else
            return false;
    }else if(waringType == "<="){
        if(1*dataValue <= 1*value)
            return true;
        else
            return false;
    }else if(waringType == "<"){
        if(1*dataValue < 1*value)
            return true;
        else
            return false;
    }else if(waringType == ">"){
        if(1*dataValue > 1*value)
            return true;
        else
            return false;
    }else if(waringType == "="){
        if(1*dataValue == 1*value)
            return true;
        else
            return false;
    }else if(waringType == "!="){
        if(1*dataValue != 1*value)
            return true;
        else
            return false;
    }else if(waringType == "BTWEEN AND"){
        if(1*dataValue <= 1*value2 && 1*dataValue >= 1*value)
            return true;
        else
            return false;
    }
}

/**
 *格式化数据
 * @param data
 */
function formatData(data)
{
	data=""+data;//转化字符串
	return 1*(data.replace("%","").replace("‰",""));
}
function exportExcel(){
		 $("typeId").value=currentTab;
	     	    var url = getBasePath()+"/portalCommon/module/portal/impExcel/index_imp_excel_chart.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
}
function exportImage(){
			var cid=$("cid").value;
			var cid1=$("cid1").value;
   	        var charts1 = getChartFromId("ChartId_"+cid1);   //生成的FusionCharts图表本身的标识
   	         var charts2 = getChartFromId("ChartId_"+cid);   //生成的FusionCharts图表本身的标识
   	        charts1.exportChart(); 
   	        charts2.exportChart(); 
}
//获取changeCode
function getChangeCode(){
	 var changeCode="";
	 tZoneCode=$("zoneCode").value;
	if(tZoneCode!="0000"){
		 $("changeCode").value="1";
		 changeCode="1";
	}else{
	    $("changeCode").value="2";
	    changeCode="2";
	}
	$("city").value='横向对比';
	return changeCode;
}
dhx.ready(indexInit);