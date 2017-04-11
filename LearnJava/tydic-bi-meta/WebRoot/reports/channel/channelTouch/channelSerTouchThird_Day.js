/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();

var user= getSessionAttribute("user");
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var queryform =$('queryform');
var globZoneTree = null;
var globChannelTypeTree=null;
var flag='false';		//是否显示渠道小类
//查询系统参数
var zoneCode =   $('zoneCode').value==""?"0000":$("zoneCode").value;
var channelTypeCode =   $('channelTypeCode').value==""?'1':$("channelTypeCode").value;
var titileInfo=$("channelType").value+"->"+$("selectCol").options[$("selectCol").selectedIndex].text;

if(channelTypeCode!='1')
{
	$("channel").value='返回';
}
else if($("channel").value!='渠道大类'){
{
	$("channel").value='渠道小类';
}
}
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** zone Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPathCode");
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
var actType=$("actType").value;
if(actType=='1'){//传统渠道
	dwrCaller.addAutoAction("loadChannelTypeTreePart","ChannelTypeAction.queryChannelTypeByPathPart");
	var channelTypeConverter=dhx.extend({idColumn:"channelTypeCode",pidColumn:"channelTypeParCode",
	    textColumn:"channelTypeName"
	},treeConverter,false);
	dwrCaller.addDataConverter("loadChannelTypeTreePart",channelTypeConverter);
	dwrCaller.addAction("querySubChannelTypePart",function(afterCall,param){
	    var tempCovert=dhx.extend({isDycload:true},channelTypeConverter,false);
	    ChannelTypeAction.querySubChannelTypePart_new(param.id,function(data){
	        data=tempCovert.convert(data);
	        afterCall(data);
	    })
	});
}else if(actType=='2'){//其他渠道
	dwrCaller.addAutoAction("loadChannelTypeTreePart","ChannelTypeAction.queryChannelTypeByPathPart");
	var channelTypeConverter=dhx.extend({idColumn:"channelTypeCode",pidColumn:"channelTypeParCode",
	    textColumn:"channelTypeName"
	},treeConverter,false);
	dwrCaller.addDataConverter("loadChannelTypeTreePart",channelTypeConverter);
	dwrCaller.addAction("querySubChannelTypePart",function(afterCall,param){
	    var tempCovert=dhx.extend({isDycload:true},channelTypeConverter,false);
	    ChannelTypeAction.querySubChannelTypePartOther_new(param.id,function(data){
	        data=tempCovert.convert(data);
	        afterCall(data);
	    })
	});
}else if(actType=='3'){//人工自助
	dwrCaller.addAutoAction("loadChannelTypeTreePart","ChannelTypeAction.queryChannelType");
	var channelTypeConverter=dhx.extend({idColumn:"channelTypeCode",pidColumn:"channelTypeParCode",
	    textColumn:"channelTypeName"
	},treeConverter,false);
	dwrCaller.addDataConverter("loadChannelTypeTreePart",channelTypeConverter);
	dwrCaller.addAction("querySubChannelTypePart",function(afterCall,param){
	    var tempCovert=dhx.extend({isDycload:true},channelTypeConverter,false);
	    ChannelTypeAction.querySubChannel(param.id,function(data){
	        data=tempCovert.convert(data);
	        afterCall(data);
	    })
	});
}

var indexInit=function(){	
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
   //1.加载渠道类型树
     loadChannelTypeTreePart(channelTypeCode,queryform);
     //执行查询数据
     excuteInitData();
     indexExp();
     getUserSubCode();
}
 

var excuteInitData=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	
	 var map=new Object();
	 map.level=3;
	 map.thirdType=$("thirdType").value;
	 map.field=$("selectCol").value;
	 //map.typeList=$("typeList").value;
	 map.dateTime=$("endDate").value;
	 map.startDate=$("startDate").value;
	 map.endDate=$("endDate").value; 
	 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
	 map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
	 map.dateType="0";//天
	 map.showAllChannel=flag;
	 map.actType=$("actType").value;;//分类类型：1传统新媒体渠道，2其他渠道，3人工自助渠道
     dhx.showProgress("正在执行......");
     AllChannelAction.getChannelSerClassThird_Pg(map, function (res) {
     	$('chartdiv1').innerHTML='';
     	$('chartdiv2').innerHTML='';
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				            return;
 				        }
 				    chart1=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
 			    	chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
			    	chart1.setDataXML(res.pieChartMap);
 			        chart2.setDataXML(res.lineChartMap);
 			        chart1.render("chartdiv1");
 			        chart2.render("chartdiv2"); 
 			        $("div_src").style.display = "none";
			        $("div_src").style.zindex = "-1";
 					buildTable(res);
    });
}
function buildTable(data){
    tableStr ="<table  id='tab1' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    autoRowSpan($("tab1"),0,1);
    autoRowSpan($("tab1"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	var thirdType=$("thirdType").value;
	if(thirdType==0){//办理
	var tableHead="<tr>"
	    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='3'><span class='title'>大区</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='3' ><span class='title'>渠道大类</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='3' ><span class='title'>渠道小类</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:70%' colspan='20'><span class='title' align='center'>办理</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='3'><span class='title'>总服务量</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='3'><span class='title'>占比</span></td>"
	 +"</tr>"
	 +"<tr>"
	    +"<td bgcolor='#cde5fd' style='width:15%' colspan='4'><span class='title'>增值业务</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:15%' colspan='5'><span class='title'>套餐销售</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:4%' rowspan='2' ><span class='title'>套餐变更</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:66%' colspan='10'><span class='title'>基础服务</span></td>"
	 +"</tr>"
	 +"<tr>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>订购</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>变更</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>注销</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>其它</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>订购</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>换购机</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>预受理</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>注销</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>其它</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>补换卡</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>积分兑换</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>密码操作</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>实名登记</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>停复机</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>信息变更</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>信用额度调整</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>业务变更</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>移机</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>其它</span></td>"
	 +"</tr>";
	}else{//查询
		var tableHead="<tr>"
		    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='3'><span class='title'>大区</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='3' ><span class='title'>渠道大类</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='3' ><span class='title'>渠道小类</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:70%' colspan='25'><span class='title' align='center'>查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='3'><span class='title'>总服务量</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:6%' rowspan='3'><span class='title'>占比</span></td>"
		 +"</tr>"
		 +"<tr>"
		    +"<td bgcolor='#cde5fd' style='width:25%' colspan='6'><span class='title'>费用查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:20%' colspan='6'><span class='title'>进度查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:15%' colspan='3' ><span class='title'>积分查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:20%' colspan='4'><span class='title'>信息查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:20%' colspan='6'><span class='title'>常用查询</span></td>"
		 +"</tr>"
		 +"<tr>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>话费查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>话费明细</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>扣费退费</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>套餐使用情况</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>余额/赠金</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>其它</span></td>"
		    
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>宽带固话装移进度</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>修障进度</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>投诉处理进度</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>终端配送进度</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>终端维修进度</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>其它</span></td>"
		    
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>积分兑换查询</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>客户积分</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>其它</span></td>"
		    
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>客户信息</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>客户状态</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>业务信息</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>其它</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>WiFi热点</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>号码归属地</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>客户经理</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>区号</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>营业厅</span></td>"
		    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>其它</span></td>"
		 +"</tr>";
	}
var  excelHeader="";
      for(var i=0;i<headColumn.length;i++)
      {
       if(headColumn[i].indexOf('_')==-1)//字段 有 "-"  表示不展示
	    {
	       excelHeader += headColumn[i]+",";
        }
      }
    $("excelHeader").value=excelHeader;//Excel表头
return tableHead;
}
/**
*  构造表格数据
* @param {Object} data
*/
function tableData(dataColumn,headColumn){
  var tableData="";
  var  excelData="";
  var thirdType=$("thirdType").value;
  var userCode=$("userCode").value;
  var userCodeData=$("userCodeData").value;
  var tempI='';
  var tempJ='';
  var tempK='';
  var channelTypeCode=$("channelTypeCode").value;
  if(actType=='1'){//传统新媒体
	  if(channelTypeCode=='1'){//所有
		  tempI=6;
		  tempJ=17;
		  tempK=18;
	  }else if(channelTypeCode=='10'){//传统
		  tempI=6;
		  tempJ=7;
		  tempK=28;
	  }else if(channelTypeCode=='14'){//新媒体
		  tempI=10;
		  tempJ=11;
		  tempK=28;
	  }else{
		  tempI=1;
		  tempJ=2;
		  tempK=28;
	  }
  }else{//人工自助
	  if(channelTypeCode=='1'){//所有
		  tempI=5;
		  tempJ=17;
		  tempK=18;
	  }else if(channelTypeCode=='10'){//人工
		  tempI=5;
		  tempJ=6;
		  tempK=28;
	  }else if(channelTypeCode=='14'){//自助
		  tempI=11;
		  tempJ=12;
		  tempK=28;
	  }else{
		  tempI=1;
		  tempJ=2;
		  tempK=28;
	  }
  }  
  var actType=$("actType").value;//分类类型：1传统新媒体渠道，2其他渠道，3人工自助渠道
     if(dataColumn&&dataColumn.length>0)
	    {  
	       for(var i=0;i<dataColumn.length;i++)
	       {
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	//if(userCodeData.indexOf("]"+dataColumn[i].区域_ID+"]")==-1){
		    	  for(var j=0;j<headColumn.length;j++)
	   		        {
	   		               var tempColumn=headColumn[j];  
	   		               if(tempColumn.indexOf('_')==-1)
			    		       {
	   		            	   if(tempColumn == '渠道小类' &&dataColumn[i][tempColumn]!='小计'&&dataColumn[i][tempColumn]!='合计'){
	   		            		if(actType!="2"){
			    		            tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].区域+"','"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	
	   		            		 }else{
	   		            			tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
	   		            		 }	   		            	   }else{
	   		            		tableData +="<td nowrap align='center'>"+dataColumn[i][tempColumn]+"</td>";
	   		            	   }
			    		             excelData += dataColumn[i][tempColumn]+"}";
			    		       } 
	   		     // }
		    	}/*else{
	    		for(var j=0;j<headColumn.length;j++)
  		         {
  		               var tempColumn=headColumn[j];    
  		               if(tempColumn.indexOf('_')==-1)
		    		       { 
  		            	   if(thirdType==1)//查询
  		            	   {
	   		            	 if(tempColumn == '渠道小类' &&dataColumn[i][tempColumn]!='小计'&&dataColumn[i][tempColumn]!='合计')
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].区域+"','"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '话费查询' &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10010001')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '话费明细'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10010002')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '扣费退费'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10010003')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '套餐使用情况'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10010004')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '余额赠金'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10010005')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '其它1'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10010006')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '宽带固话装移进度'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10020001')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '修障进度'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10020002')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '投诉处理进度'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10020003')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '终端配送进度'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10020004')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '终端维修进度'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10020005')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '其它2'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10020006')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '积分兑换查询'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10030001')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '客户积分'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10030002')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '其它3'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10030003')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '客户信息'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10040001')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '客户状态'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10040002')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '业务信息'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10040003')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '其它4'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10040005')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == 'WLAN热点'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10050001')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '号码归属地'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10050002')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '客户经理'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10050003')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '区号'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10050004')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '营业厅'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10050005')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '其它5'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10050006')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '查询总量'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else{
	    		        		 tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
	    		        	 }
  		            	   }else//办理
  		            	   {
	    		        	 if(tempColumn == '订购'  &&i!=tempI&&i!=tempJ&&i!=tempK)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30010001')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '变更'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30010003')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '注销'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30010002')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '其它11'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30010004')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '订购1'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30020001')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '换购机'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30020003')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '预受理'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30020004')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '注销1'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30020002')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '其它12'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30020005')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '套餐变更'  &&i!=6&&i!=17&&i!=18)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','10050006')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '补换卡'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30040001')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '积分兑换'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30040002')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '密码操作'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30040003')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '实名登记'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30040004')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '停复机'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30040005')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '信息变更'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30040006')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '信用额度调整'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30040007')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '业务变更'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30040008')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '移机'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30040009')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '其它14'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30040010')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else if(tempColumn == '办理总量'  &&i!=tempI&&i!=tempJ&&i!=tempK&&dataColumn[i][tempColumn]!=0)
	    		        	 {
			    		         tableData +="<td nowrap onclick=\"goToDetails('"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"','30')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	 
	    		        	 }else {
		    		        	 tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
	    		        	 }
  		            	   }*/
  		            	        // excelData += dataColumn[i][tempColumn]+"}";
		    		       //} 
		    	  tableData +="</tr>";
				  excelData +="]";
  		      }
	    	//}
	    	  
	    	}   
	     //  }
        else
       {
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
return tableData;
}
//钻取
var drillArea=function(zoneName,zoneCode,channelTypeCode)
{	
	var startDate=$("startDate").value;
	var endDate=$("endDate").value;
	var selectCol=$("selectCol").value;
	var thirdType=$("thirdType").value;
	var url="";
	var tempId="";
	var tempName="";
	if(thirdType=='0'){//办理
		 url="/reports/channel/newChannel/AreaServerThridTransaction_Day.jsp?zoneCode="+zoneCode+"&startDate="+startDate+
		      "&endDate="+endDate+"&channelTypeCode="+channelTypeCode+"&menuId="+menuId;
		 tempId="179001";
		 tempName="区域渠道服务三级办理日报";
	}else{//查询
		 url="/reports/channel/newChannel/AreaServerThird_Day.jsp?zoneCode="+zoneCode+"&startDate="+startDate+
		     "&endDate="+endDate+"&channelTypeCode="+channelTypeCode+"&menuId="+menuId;
		 tempId="178001";
		 tempName="区域渠道服务三级查询日报";
	}
	return parent.openTreeTab(tempId+zoneCode+channelTypeCode, tempName+"["+zoneName+"]", base+url, 'top');
	
}
var goToDetails=function(zoneCode,channelId,servId){
	var startDate=$("startDate").value;
	var endDate=$("endDate").value;
	var url="/reports/channel/channelTouch/rptChannelSerDetail.jsp?zoneCode="+zoneCode+"&startTime="+startDate+
	"&endTime="+endDate+"&channelId="+channelId+"&servId="+servId+"&thirdType="+$("thirdType").value;
return parent.openTreeTab("168002"+zoneCode ,"渠道服务清单", base+url, 'top');
}
//查询
var queryData=function(){
	if($("channelTypeCode").value!='1')
	{
		$("channel").value='返回';
	}
	else if($("channel").value!='渠道大类'){
	{
		$("channel").value='渠道小类';
	}
	}
	excuteInitData();
}

//第一列合并
var autoRowSpan=function(tb,row,col){
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
            pos++;
        }else{
            lastValue = value;
            pos=1;
        }
    }
}
/**
 * 
 * @param myObject
 * @return
 */
function dump_obj(myObject) {  
   var s = "";  
   for (var property in myObject) {  
	   s = s + "\n "+property +": " + myObject[property] ;  
	  }  
	  alert(s);  
	} 
/** end **/

var showAllChannel = function (obj){
	AllChannelAction.getParChannel($('channelTypeCode').value,$('actType').value,function (res) {
		if(res=='1'){
			flag='true';
		}
		if(res!='0'){
			$('channelTypeCode').value=res;
		}
		if(flag=='true'){
		flag='false';
		 $("channel").value='渠道小类'
	}
	else{
		flag='true';
		$("channel").value='渠道大类'
	}
	excuteInitData();
	loadChannelTypeTreePart_all($('channelTypeCode').value,queryform);
	
	});
	
}
//通过列改变图形
function changeCol(obj){
	var titileInfo=$("channelType").value+"->"+$("selectCol").options[$("selectCol").selectedIndex].text;
	$("titleInfo1").innerHTML=titileInfo;
	 var map=new Object();
	 map.dateTime=$("endDate").value;
     map.field=$("selectCol").value;
     //map.typeList=$("typeList").value;
     map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value; 
     map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;   
	 map.startDate=$("startDate").value;
	 map.endDate=$("endDate").value; 
	 map.dateType="0";//天
	 map.showAllChannel=flag;
	 map.actType=$("actType").value;//分类类型：1传统新媒体渠道，2其他渠道，3人工自助渠道     
     buildChart(map);
}
//加载配置图形
function buildChart(map){
	dhx.showProgress("正在执行......");
	AllChannelAction.getChannelSerClass_Chart_FirstSecThi(map, {callback:function (res) {
		dhx.closeProgress();
    	         if(res != null){
                        buildBLChart(res);
		         }
	}
		});
   }
//构建折线图和柱状图
function  buildBLChart(map){
		$("cid").value=Math.random();
		var cid=$("cid").value;
		$("cid1").value=Math.random();
		var cid1=$("cid1").value;
	   $("chartdiv1").innerHTML="";
	   $("chartdiv2").innerHTML="";
	   chart1=new FusionCharts(base+"/js/Charts/Pie3D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
	   chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
	   chart1.setDataXML(map.pieChartMap);
       chart2.setDataXML(map.lineChartMap);
       chart1.render("chartdiv1");
       chart2.render("chartdiv2"); 
   }
function exportExcel(){
		    var  startDate=$("startDate").value;//文本框\
		    var  endDate=$("endDate").value;//文本框
		    var  zone=$("zone").value;//文本框
		    var  channelType=$("channelType").value;//文本框
		    var queryCond="日期从："+startDate+"    至："+endDate+"    区域："+zone+"    渠道类型: "+channelType;  
	   	    $("excelCondition").value=queryCond; 
	    	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel_twoChartHeader.jsp";
	    	//var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel_chart.jsp";
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
}
function exportImage(){
			var cid=$("cid").value;
			var cid1=$("cid1").value;
   	        var charts1 = getChartFromId("ChartId1_"+cid1);   //生成的FusionCharts图表本身的标识
   	         var charts2 = getChartFromId("ChartId2_"+cid);   //生成的FusionCharts图表本身的标识
   	        charts1.exportChart(); 
   	        charts2.exportChart(); 
}
function myJs(temp){
			$('channelTypeCode').value=temp;
			if(temp=='1'){
				flag='false';
				$("channel").value!='渠道小类';
			}
			excuteInitData();
			loadChannelTypeTreePart_all(temp,queryform);
}
//所有子节点
function getUserSubCode(){
	 var userCodeData="]";
	 var map=new Object(); 
	 map.userCode=$("userCode").value;
	 CustomerSatisfiedAction.getUserSubCode(map, {callback:function (data) {
         if(data != null){
    		 if(data&&data.userCodeList.length>0){	
    			 for(var i=0;i<data.userCodeList.length;i++){
    				 userCodeData+=data.userCodeList[i].ZONE_CODE+"]";
    			 }
    		 } 
	         }
         $("userCodeData").value= userCodeData;
	   }
	});
}
//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 var explainData="";
	 var tempId='';
	 var thirdType=$("thirdType").value;
		if(thirdType==0){//办理
			tempId='1004';
		}else{
			tempId='1003';
		}
	 var map=new Object(); 
	 map.rptId=tempId;
	 CmplIndexAction.getIndexExplain(map, {callback:function (data) {
         if(data != null){
        	 var tableStr="<table width='100%' border='0'  cellpadding='0' cellspacing='0'>";
    		 if(data&&data.expList.length>0){	
    			 for(var i=0;i<data.expList.length;i++){
    				 tableStr =tableStr+
    		         "<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">"
    		           +"<td align=\"left\"><font style='font-weight:bold;' >&nbsp;"+data.expList[i].INDEX_NAME+"  :  "+"</font>"
    		           +data.expList[i].INDEX_EXPLAIN+"</td>"   
    		           +"</tr>";
    				 explainData+=data.expList[i].INDEX_NAME+"  :  "+ data.expList[i].INDEX_EXPLAIN+"]";
    			 }
    			 tableStr +="</table>"; 
    			 $("explain").value=explainData;
    			 $('index_exp').innerHTML=tableStr;
    		 } 
	         }
	   }
	});
}
dhx.ready(indexInit);