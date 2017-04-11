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


var thirdType=$("thirdType").value;


//查询系统参数
var zoneCode =   $('zoneCode').value==""?"0000":$("zoneCode").value;
var channelTypeCode =   $('channelTypeCode').value==""?'1':$("channelTypeCode").value;

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
 
 
dwrCaller.addAutoAction("loadChannelTypeTreePart","NewTwoChannelAction.queryChannelTypePathPart");
	var channelTypeConverter=dhx.extend({idColumn:"channelTypeCode",pidColumn:"channelTypeParCode",
	    textColumn:"channelTypeName"
	},treeConverter,false);
dwrCaller.addDataConverter("loadChannelTypeTreePart",channelTypeConverter);
dwrCaller.addAction("querySubChannelTypePart",function(afterCall,param){
	var tempCovert=dhx.extend({isDycload:true},channelTypeConverter,false);
	     NewTwoChannelAction.querySubChannelTypePart_new(param.id,function(data){
	        data=tempCovert.convert(data);
	        afterCall(data);
  })
	
	});
 

var indexInit=function(){	
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
   //1.加载渠道类型树
     loadChannelTypeTreePart(channelTypeCode,queryform);
     //执行查询数据
     excuteInitData();
    // indexExp();
}
 

var excuteInitData=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	
	 var map=new Object();
	 map.level=3;
	 map.thirdType=thirdType;
	 //map.typeList=$("typeList").value;
	 map.dateTime=$("dateTime").value;
	 map.startDate=$("dateTime").value;
	 map.endDate=$("dateTime").value; 
	 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
	 map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
	 map.dateType="1";//周
	 map.showAllChannel=flag;
     dhx.showProgress("正在执行......");
     NewTwoChannelAction.getChannelSerNewThird_Pg(map, function (res) {
    	 dhx.closeProgress();
 		 if (res == null) {
 			  dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 			   return;
 			 }
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
	
	if(thirdType==0){//办理
	var tableHead="<tr>"
	    +"<td bgcolor='#cde5fd'  rowspan='3' ><span class='title'>大区</span></td>"
	    +"<td bgcolor='#cde5fd'  rowspan='3' ><span class='title'>渠道大类</span></td>"
	    +"<td bgcolor='#cde5fd'  rowspan='3' ><span class='title'>渠道小类</span></td>"
	    +"<td bgcolor='#cde5fd'  colspan='29'><span class='title' align='center'>办理</span></td>"
	    +"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>总服务量</span></td>"
	    +"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>占比</span></td>"
	 +"</tr>"
	 +"<tr>"
	    +"<td bgcolor='#cde5fd'  colspan='3'><span class='title'>加装</span></td>"
	    +"<td bgcolor='#cde5fd'  colspan='4'><span class='title'>变更</span></td>"
	    +"<td bgcolor='#cde5fd'  rowspan='1' ><span class='title'>续约</span></td>"
	    +"<td bgcolor='#cde5fd'  colspan='6'><span class='title'>退订</span></td>"
	    +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>购买</span></td>"
	    +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>信息变更</span></td>"
	    +"<td bgcolor='#cde5fd'  colspan='11'><span class='title'>基础服务</span></td>"
	 +"</tr>"
	 +"<tr>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>可选包</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>传统增值业务</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>新兴增值业务</span></td>"
	    
	    +"<td bgcolor='#cde5fd' ><span class='title'>套餐</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>政企产品</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>行业应用</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>缴费方式</span></td>"
	    
	    +"<td bgcolor='#cde5fd' ><span class='title'>套餐</span></td>"
	    
	    +"<td bgcolor='#cde5fd' ><span class='title'>套餐</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>政企产品</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>行业应用</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>可选包</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>传统增值业务</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>新兴增值业务</span></td>"
	    
	    +"<td bgcolor='#cde5fd' ><span class='title'>购买充值卡</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>购买流量卡</span></td>"
	    
	    +"<td bgcolor='#cde5fd' ><span class='title'>身份信息变更</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>实名登记</span></td>"
	    
	    +"<td bgcolor='#cde5fd' ><span class='title'>国际漫游功能</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>补换卡</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>密码服务</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>停机保号服务</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>信用服务</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>发票账单服务</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>缴费助手</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>提现服务</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>积分兑换</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>移机</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title'>过户</span></td>"
	    
	 +"</tr>";
	}else if(thirdType==1) {//查询
		var tableHead="<tr>"
		    +"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>大区</span></td>"
		    +"<td bgcolor='#cde5fd'  rowspan='3' ><span class='title'>渠道大类</span></td>"
		    +"<td bgcolor='#cde5fd'  rowspan='3' ><span class='title'>渠道小类</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='18'><span class='title' align='center'>查询</span></td>"
		    +"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>总服务量</span></td>"
		    +"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>占比</span></td>"
		 +"</tr>"
		 +"<tr>"
		    +"<td bgcolor='#cde5fd'  colspan='5'><span class='title'>费用查询</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>积分查询</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='7' ><span class='title'>信息查询</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='4'><span class='title'>订单查询</span></td>"
		 +"</tr>"
		 +"<tr>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>话费查询</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>详单查询</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>账单查询</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>充值缴费记录</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>翼支付/缴费助手账户查询</span></td>"
		   
		    +"<td bgcolor='#cde5fd' ><span class='title'>客户积分查询</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>积分兑换查询</span></td>"
		    
		    +"<td bgcolor='#cde5fd' ><span class='title'>客户资料查询</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>套餐信息查询</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>增值业务</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>协议到期查询</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>服务提醒定制查询</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>客户状态查询</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>客户经理查询</span></td>"
		    
		    
		    +"<td bgcolor='#cde5fd' ><span class='title'>装移进度查询</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>修障进度查询</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>投诉处理进度查询</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>线上订单配送查询</span></td>"
		 +"</tr>";
	}else{   //咨询
	    var tableHead="<tr>"
		    +"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>大区</span></td>"
		    +"<td bgcolor='#cde5fd'  rowspan='3' ><span class='title'>渠道大类</span></td>"
		    +"<td bgcolor='#cde5fd'  rowspan='3' ><span class='title'>渠道小类</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='14'><span class='title' align='center'>咨询</span></td>"
		    +"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>总服务量</span></td>"
		    +"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>占比</span></td>"
		 +"</tr>"
		 +"<tr>"
		    +"<td bgcolor='#cde5fd'  colspan='1'><span class='title'>优惠活动类咨询</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='4'><span class='title'>服务信息类咨询</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='2' ><span class='title'>资源类咨询</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='4'><span class='title'>业务信息</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='1'><span class='title'>产品演示</span></td>"
		     +"<td bgcolor='#cde5fd'  colspan='2'><span class='title'>使用指南</span></td>"
		 +"</tr>"
		 +"<tr>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>优惠活动类咨询</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>服务网点信息</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>服务热线信息</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>号码信息</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>办理渠道</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>网络覆盖</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>终端适配</span></td>"
		    
		    +"<td bgcolor='#cde5fd' ><span class='title'>套餐信息</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>规则政策</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>政企产品</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>行业应用</span></td>"
		    
		    +"<td bgcolor='#cde5fd' ><span class='title'>行业应用演示</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>业务使用指南</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title'>终端使用指南</span></td>"
		     
		 +"</tr>";
	}
var  excelHeader="";
      for(var i=0;i<headColumn.length;i++){
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
     if(dataColumn&&dataColumn.length>0)
	    {  
	       for(var i=0;i<dataColumn.length;i++)
	       {
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	  for(var j=0;j<headColumn.length;j++)
   		         {
   		               var tempColumn=headColumn[j];    
   		               if(tempColumn.indexOf('_')==-1)
		    		       { 
	   		            	 if(tempColumn == '渠道小类' &&dataColumn[i][tempColumn]!='小计'&&dataColumn[i][tempColumn]!='合计')
	    		        	 {
			    		            tableData +="<td nowrap onclick=\"drillArea('"+dataColumn[i].区域+"','"+dataColumn[i].区域_ID+"','"+dataColumn[i].渠道小类_ID+"')\" class='unl'>"+dataColumn[i][tempColumn]+"</td>";	
	   		            	  }else{
		    		        	 tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
	    		        	 }
   		            	         excelData += dataColumn[i][tempColumn]+"}";
		    		       } 
   		      }
	    	  tableData +="</tr>";
			  excelData +="]";
	    	}   
	       }
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
	var url="/reports/channel/newChannel/AreaServerSecond_Day.jsp?zoneCode="+zoneCode+"&startDate="+startDate+
	"&endDate="+endDate+"&channelTypeCode="+channelTypeCode+"&menuId="+menuId;
	return parent.openTreeTab("175001"+zoneCode+channelTypeCode, "区域渠道服务二级日报表"+"["+zoneName+"]", base+url, 'top');
	
}
//查询
var queryData=function(){
	
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
		    var  startDate=$("dateTime").value;//文本框\
		    var  endDate=$("dateTime").value;//文本框
		    var  zone=$("zone").value;//文本框
		    var  channelType=$("channelType").value;//文本框
		    var queryCond="查询周："+startDate+"    区域："+zone+"    渠道类型: "+channelType;  
	   	    $("excelCondition").value=queryCond; 
	    	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implAaeaChannelMoreExcel.jsp";
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
//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 var explainData="";
	 var map=new Object(); 
	 map.rptId="1002";
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