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
var channelTypeCode =   $('channelTypeCode').value==""?'1':$("channelTypeCode").value;
var channelTypeCode1 =   $('channelTypeCode1').value==""?'1':$("channelTypeCode1").value;
var channelTypeCode2 =   $('channelTypeCode2').value==""?'1':$("channelTypeCode2").value;
var zoneCode  =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var zoneCode2 =   $('zoneCode2').value==""?userInfo['localCode']:$("zoneCode2").value;
var zoneCode1 =   $('zoneCode1').value==""?userInfo['localCode']:$("zoneCode1").value;

	 
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
	    NewTwoChannelAction.querySubChannelTypePart_new2(param.id,function(data){
	        data=tempCovert.convert(data);
	        afterCall(data);
	    })
	});



var indexInit=function(){	
     clickTab();
 	//1.加载地域树 
 	 loadZoneTreeChkBox(zoneCode,queryform);
     loadZoneTreeChkBox2(zoneCode2,queryform);
     loadZoneTreeChkBox1(zoneCode1,queryform);
   //1.加载渠道类型树
     loadChannelTypeTreePart(channelTypeCode,queryform);
     loadChannelTypeTreePart1(channelTypeCode1,queryform);
     loadChannelTypeTreePart2(channelTypeCode2,queryform);
}
function clickTab(){
	var obj= this;
	var clickTab=$("clickTab").value;
	if(clickTab=="tab1"){//月报
		obj.id="tab1"
	    changeTab1(obj);
	}else if(clickTab=="tab2"){//周
		obj.id="tab2"
		changeTab2(obj);
	}else if(clickTab=="tab3"){//日
		obj.id="tab3"
		changeTab3(obj);
	}else{
		obj.id="tab3"
	    changeTab3(obj);
	}
}
function changeTab3(obj){
	var id = obj.id;
	for(var i=0;i<3;i++){
		var value = "tab"+(i+1);
		if(value!=id){
			$("info"+(i+1)).style.display="none";
			$("tab"+(i+1)).style.color="#002200";
		}else{
			$("info"+(i+1)).style.display="";
			$("tab"+(i+1)).style.color="#FFFFFF";
		}	
	}
	window.setTimeout("queryData2()",1000);		
}
//周
function changeTab2(obj){
	var id = obj.id;
	for(var i=0;i<3;i++){
		var value = "tab"+(i+1);
		if(value!=id){
			$("info"+(i+1)).style.display="none";
			$("tab"+(i+1)).style.color="#002200";
		}else{
			$("info"+(i+1)).style.display="";
			$("tab"+(i+1)).style.color="#FFFFFF";
		}	
	}
	window.setTimeout("queryData()",1000);
}
//月
function changeTab1(obj){
	var id = obj.id;
	for(var i=0;i<3;i++){
		var value = "tab"+(i+1);
		if(value!=id){
			$("info"+(i+1)).style.display="none";
			$("tab"+(i+1)).style.color="#002200";
		}else{
			$("info"+(i+1)).style.display="";
			$("tab"+(i+1)).style.color="#FFFFFF";
		}	
	}
	window.setTimeout("queryData1()",1000);	
}

function getHeader(){
var thirdType=$("thirdType").value;
var headerLevel=$("headerLevel").value;
var tableHead="";
if(headerLevel=="1"){
	tableHead="<tr>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>渠道大类</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>渠道小类</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%'><span class='title' align='center' >查询</span></td>" 
	    +"<td bgcolor='#cde5fd' style='width:6%'><span class='title' align='center'>充值交费</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%'><span class='title' align='center'>办理</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>咨询</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>故障申告</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>投诉</span></td>"
 	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>其它（非集团）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>总服务量</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>占比</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>上月同期总服务量</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:6%' ><span class='title'>总服务量同比</span></td>"
	 +"</tr>";
}else if(headerLevel=="2"){
    tableHead="<tr>"
	    //+"<td bgcolor='#cde5fd' style='width:6%' rowspan='2'><span class='title'>大区</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:2%' rowspan='2' ><span class='title'>渠道大类</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:2%' rowspan='2' ><span class='title'>渠道小类</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:15%' colspan='5' ><span class='title' align='center' >查询</span></td>" 
	    +"<td bgcolor='#cde5fd' style='width:9%' colspan='2' ><span class='title' align='center' >充值交费</span></td>" 
	    +"<td bgcolor='#cde5fd' style='width:27%' colspan='9'><span class='title' align='center'>办理</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:24%' colspan='8'><span class='title' align='center'>咨询</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:12%' colspan='4' ><span class='title' align='center' >故障申告</span></td>" 
	    +"<td bgcolor='#cde5fd' style='width:9%' colspan='3' ><span class='title' align='center' >投诉</span></td>" 
	    +"<td bgcolor='#cde5fd' style='width:9%' rowspan='2'  ><span class='title' align='center' >其他（非集团）</span></td>" 
	    //+"<td bgcolor='#cde5fd' style='width:6%' rowspan='2'><span class='title'>总服务量</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:6%' rowspan='2'><span class='title'>占比</span></td>"
	 +"</tr>"
	 +"<tr>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>费用查询</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>积分查询</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>信息查询</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>订单查询</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他（非集团）</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>查询总量</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>交费</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>充值</span></td>"	    
	    
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>新装（非集团）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>加装</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>变更</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>续约</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>退订</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>购买</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>信息变更</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>基础服务</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>办理总量</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他（非集团）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>优惠活动类咨询</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>服务信息类咨询</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>资源类咨询</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>业务信息</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>产品演示</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>使用指南</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他知识（非集团）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他（非集团）</span></td>"
	    //+"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>咨询总量</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>预处理（自助排障）</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>派单</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>催装、催修</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>预处理</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>派单</span></td>"
	    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
	 +"</tr>";
}else{
	if(thirdType==0){//办理
		   tableHead="<tr>"
			    //+"<td bgcolor='#cde5fd'  rowspan='3' ><span class='title'>大区</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%' rowspan='2' ><span class='title'>渠道大类</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%' rowspan='2' ><span class='title'>渠道小类</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:6%' colspan='2'><span class='title'>新装（非集团）</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:12%' colspan='4'><span class='title'>加装</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:15%' colspan='5'><span class='title'>变更</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:6%' colspan='2' ><span class='title'>续约</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:21%' colspan='7'><span class='title'>退订</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:9%' colspan='3'><span class='title'>购买</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:9%' colspan='3'><span class='title'>信息变更</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:26%' colspan='12'><span class='title'>基础服务</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%' rowspan='2' ><span class='title'>其他（非集团）</span></td>"
			    //+"<td bgcolor='#cde5fd'  colspan='29'><span class='title' align='center'>办理</span></td>"
			    //+"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>总服务量</span></td>"
			    //+"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>占比</span></td>"
			 +"</tr>"
			 +"<tr>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>套餐</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
			    
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>可选包</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>传统增值业务</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>新兴增值业务</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
			    
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>套餐</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>政企产品</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>行业应用</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>缴费方式</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
			    
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>套餐</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
			    
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>套餐</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>政企产品</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>行业应用</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>可选包</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>传统增值业务</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>新兴增值业务</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
			    
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>购买充值卡</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>购买流量卡</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
			    
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>身份信息变更</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>实名信息补登记</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
			    
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>国际漫游功能</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>补换卡</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>密码服务</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>停机保号服务</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>信用服务</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>发票账单服务</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>交费助手</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>提现服务</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>积分兑换</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>移机</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>过户</span></td>"
			    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
			 +"</tr>";
			}else if(thirdType==1) {//查询
			 tableHead="<tr>"
				    //+"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>大区</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%' rowspan='2' ><span class='title'>渠道大类</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%' rowspan='2' ><span class='title'>渠道小类</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:18%' colspan='6'><span class='title'>费用查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:9%' colspan='3'><span class='title'>积分查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:24%' colspan='8' ><span class='title'>信息查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:15%' colspan='5'><span class='title'>订单查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%' rowspan='2' ><span class='title'>其他（非集团）</span></td>"
				    //+"<td bgcolor='#cde5fd'  colspan='18'><span class='title' align='center'>查询</span></td>"
				    //+"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>总服务量</span></td>"
				    //+"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>占比</span></td>"
				 +"</tr>"
				 +"<tr>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>话费查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>详单查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>账单查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>充值交费记录</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>翼支付/交费助手账户查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
				   
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>客户积分查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>积分兑换查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
				    
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>客户资料查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>套餐信息查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>增值业务</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>协议到期查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>服务提醒定制查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>客户状态查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>客户经理查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
				    
				    
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>装移进度查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>修障进度查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>投诉处理进度查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>线上订单配送查询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
				 +"</tr>";
			}else if(thirdType==2) {   //咨询
			    tableHead="<tr>"
				    //+"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>大区</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%' rowspan='2' ><span class='title'>渠道大类</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%' rowspan='2' ><span class='title'>渠道小类</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%' rowspan='2'><span class='title'>优惠活动类</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:18%' colspan='6'><span class='title'>服务信息类</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:9%' colspan='3' ><span class='title'>资源类咨询</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:18%' colspan='6'><span class='title'>业务信息</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:6%' colspan='2'><span class='title'>产品演示</span></td>"
				     +"<td bgcolor='#cde5fd' style='width:9%' colspan='3'><span class='title'>使用指南</span></td>"
				     +"<td bgcolor='#cde5fd' style='width:9%' colspan='3'><span class='title'>其他知识</span></td>"
					+"<td bgcolor='#cde5fd' style='width:3%' rowspan='2' ><span class='title'>其他（非集团）</span></td>"

				    //+"<td bgcolor='#cde5fd'  colspan='14'><span class='title' align='center'>咨询</span></td>"
				    //+"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>总服务量</span></td>"
				    //+"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>占比</span></td>"
				 +"</tr>"
				 +"<tr>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>服务网点信息</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>服务热线信息</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>号码信息</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>办理渠道</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>密码服务</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
				    
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>网络覆盖</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>终端适配</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
				    
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>套餐信息</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>规则政策</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>政企产品</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>行业应用</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>增值业务</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
				    
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>行业应用</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
				    
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>业务使用指南</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>终端使用指南</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"
				    
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他业务</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他运营商</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:3%'><span class='title'>其他</span></td>"				     
				 +"</tr>";
			}else{   //充值交费
			    tableHead="<tr>"
				   // +"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>大区</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:5%'  rowspan='2' ><span class='title'>渠道大类</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:5%' rowspan='2' ><span class='title'>渠道小类</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:30%' colspan='6' ><span class='title'>交费</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:25%' colspan='5' ><span class='title'>充值</span></td>"
				    //+"<td bgcolor='#cde5fd'  colspan='14'><span class='title' align='center'>咨询</span></td>"
				    //+"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>总服务量</span></td>"
				    //+"<td bgcolor='#cde5fd'  rowspan='3'><span class='title'>占比</span></td>"
				 +"</tr>"
				 
				 +"<tr>"
				    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>现金交费</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>银行卡交费</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>托收</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>支票交费</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>第三方支付</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>其他</span></td>"
				    
				    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>翼支付/交费助手充值</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>充值卡充值</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>流量卡充值</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>空中充值</span></td>"
				    +"<td bgcolor='#cde5fd' style='width:5%'><span class='title'>其他</span></td>"			    	     
				 +"</tr>";
			}	
}
return tableHead;
}

//周报
var queryData=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	var titileInfo=$("selectCol").options[$("selectCol").selectedIndex].text+"->本周值";
	$("titleInfo1").innerHTML=titileInfo;
	$("titleInfo2").innerHTML=titileInfo;
	 var map=new Object();
     map.dateTime=$("dateTime").value;
	 map.startDate=$("dateTime").value;
	 map.endDate=$("dateTime").value; 
	 map.zoneCode=$('zoneCode').value==""?"0000":$("zoneCode").value;
	 map.channelTypeCode=$('channelTypeCode').value==""?'1':$("channelTypeCode").value;
	 map.thirdType=$("thirdType").value;
	 map.headerLevel=$("headerLevel").value;
	 map.dateType="1";//周 
	 map.field   =$("selectCol").value;
     dhx.showProgress("正在执行......");
     NewTwoChannelAction.getChannelSerChart_Pg(map, function (res) {
    	 $('chartdiv1').innerHTML='';
     	 $('chartdiv2').innerHTML='';
    	 dhx.closeProgress();
 		 if (res == null) {
 			 dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 			 return;
 		 }
 		chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
   	    chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
        chart1.setDataXML(res.barChartMap);
        chart2.setDataXML(res.lineChartMap);
        chart1.render("chartdiv1");
        chart2.render("chartdiv2"); 
 		 $("div_src").style.display = "none";
		 $("div_src").style.zindex = "-1";
 		 buildTable(res);
    });	
}
function buildTable(data){
    tableStr ="<table  id='tab11' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    autoRowSpan($("tab11"),0,1);
    autoRowSpan($("tab11"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
	var tableHead=getHeader();
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
  if(dataColumn&&dataColumn.length>0){  
	   for(var i=0;i<dataColumn.length;i++){
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	  for(var j=0;j<headColumn.length;j++) {
   		               var tempColumn=headColumn[j];   
   		               if(tempColumn.indexOf('_')==-1) { 
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
	 }else{
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
     
	return tableData;
}
//月报
var queryData1=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	var titileInfo=$("selectCol1").options[$("selectCol1").selectedIndex].text+"->本月值";
	$("titleInfo11").innerHTML=titileInfo;
	$("titleInfo21").innerHTML=titileInfo;
     var map=new Object();
     map.dateTime=$("dateTime1").value;
	 map.startDate=$("dateTime1").value;
	 map.endDate=$("dateTime1").value; 
	 map.zoneCode=$('zoneCode1').value==""?"0000":$("zoneCode1").value;
	 map.channelTypeCode=$('channelTypeCode1').value==""?'1':$("channelTypeCode1").value;
	 map.dateType="2";//天	 
	 map.thirdType=$("thirdType").value;
	 map.headerLevel=$("headerLevel").value;
	 map.field   =$("selectCol1").value;
    dhx.showProgress("正在执行......");
    NewTwoChannelAction.getChannelSerChart_Pg(map, function (res) {
    	$('chartdiv11').innerHTML='';
    	$('chartdiv21').innerHTML='';  	
   	 dhx.closeProgress();
		 if (res == null) {
			 dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
			 return;
		 }
		 chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
    	 chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
         chart1.setDataXML(res.barChartMap);
         chart2.setDataXML(res.lineChartMap);
         chart1.render("chartdiv11");
         chart2.render("chartdiv21"); 
		 $("div_src").style.display = "none";
		 $("div_src").style.zindex = "-1";
		 buildTable1(res);
   });	
}
function buildTable1(data){
    tableStr ="<table  id='tab22' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader1(data.headColumn);
    tableStr+=tableData1(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable1').innerHTML=tableStr;
    autoRowSpan($("tab22"),0,1);
    autoRowSpan($("tab22"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader1(headColumn){
	var tableHead=getHeader();
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
function tableData1(dataColumn,headColumn){
  var tableData="";
  var  excelData="";
  if(dataColumn&&dataColumn.length>0){  
	   for(var i=0;i<dataColumn.length;i++){
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	  for(var j=0;j<headColumn.length;j++) {
   		               var tempColumn=headColumn[j];   
   		               if(tempColumn.indexOf('_')==-1) { 
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
	 }else{
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
     
	return tableData;
}
//日报
var queryData2=function(){
	$("cid").value=Math.random();
	var cid=$("cid").value;
	$("cid1").value=Math.random();
	var cid1=$("cid1").value;
	var titileInfo=$("selectCol2").options[$("selectCol2").selectedIndex].text+"->当日值";
	$("titleInfo12").innerHTML=titileInfo;
	$("titleInfo22").innerHTML=titileInfo;
	 var map=new Object();
	 map.startDate=$("startDate").value;
	 map.endDate=$("endDate").value; 
	 map.zoneCode=$('zoneCode2').value==""?"0000":$("zoneCode2").value;
	 map.channelTypeCode=$('channelTypeCode2').value==""?'1':$("channelTypeCode2").value;
	 map.dateType="0";//天	
	 map.thirdType=$("thirdType").value;
	 map.headerLevel=$("headerLevel").value;
	 map.field   =$("selectCol2").value;
    dhx.showProgress("正在执行......");
    NewTwoChannelAction.getChannelSerChart_Pg(map, function (res) {
    	$('chartdiv12').innerHTML='';
    	$('chartdiv22').innerHTML='';
      	 dhx.closeProgress();
   		 if (res == null) {
   			 dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
   			 return;
   		 }
   		 chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
   	     chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
         chart1.setDataXML(res.barChartMap);
         chart2.setDataXML(res.lineChartMap);
         chart1.render("chartdiv12");
         chart2.render("chartdiv22"); 
   		 $("div_src").style.display = "none";
   		 $("div_src").style.zindex = "-1";
   		 buildTable2(res);
      });	
}
function buildTable2(data){
    tableStr ="<table  id='tab33' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader2(data.headColumn);
    tableStr+=tableData2(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable2').innerHTML=tableStr;
    autoRowSpan($("tab33"),0,1);
    autoRowSpan($("tab33"),0,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader2(headColumn){
	var tableHead=getHeader();
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
function tableData2(dataColumn,headColumn){
  var tableData="";
  var  excelData="";
  if(dataColumn&&dataColumn.length>0){  
	   for(var i=0;i<dataColumn.length;i++){
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	  for(var j=0;j<headColumn.length;j++) {
   		               var tempColumn=headColumn[j];   
   		               if(tempColumn.indexOf('_')==-1) { 
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
	 }else{
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
     
	return tableData;
}
//通过列改变图形-日
function changeCol2(obj){
	var titileInfo=$("selectCol2").options[$("selectCol2").selectedIndex].text+"->当日值";
	$("titleInfo12").innerHTML=titileInfo;
	$("titleInfo22").innerHTML=titileInfo;
	var map=new Object();
    map.startDate   =$("startDate").value;
    map.endDate   =$("endDate").value;
    map.zoneCode=$('zoneCode2').value==""?userInfo['localCode']:$("zoneCode2").value;
    map.channelTypeCode=$('channelTypeCode2').value==""?'1':$("channelTypeCode2").value;
    map.dateType   ="0";//日报
    map.thirdType=$("thirdType").value;
	map.headerLevel=$("headerLevel").value;
    map.field=obj.value;
    buildChart2(map);
}
//加载配置图形
function buildChart2(map){
	dhx.showProgress("正在执行......");
	NewTwoChannelAction.getChannelSerChart(map, {callback:function (res) {
		dhx.closeProgress();
    	         if(res != null){
                        buildBLChart2(res);
		         }
	}
		});
   }
//构建折线图和柱状图
function  buildBLChart2(map){
	   $("cid").value=Math.random();
	   var cid=$("cid").value;
	   $("cid1").value=Math.random();
	   var cid1=$("cid1").value;
	   $("chartdiv12").innerHTML="";
	   $("chartdiv22").innerHTML="";
	   chart1=new FusionCharts(base+"/js/Charts/MSColumn2D.swf", "ChartId1_"+cid1, "100%", "200", "0", "0");
	   chart2=new FusionCharts(base+"/js/Charts/MSLine.swf", "ChartId2_"+cid, "100%", "200", "0", "0");
	   chart1.setDataXML(map.barChartMap);
       chart2.setDataXML(map.lineChartMap);
       chart1.render("chartdiv12");
       chart2.render("chartdiv22"); 
   }
//周
var drillArea=function(zoneName,zoneCode,channelTypeCode)
{	
	var startDate=$("dateTime").value;
	var endDate=$("dateTime").value;
	var url="/reports/channel/channelNew/channelServerSum.jsp?zoneCode="+zoneCode+"&startDate="+startDate+
	"&endDate="+endDate+"&channelTypeCode="+channelTypeCode+"&menuId="+menuId;
	return parent.openTreeTab("164010"+zoneCode+channelTypeCode, "线上线下渠道服务总体报表"+"["+zoneName+"]", base+url, 'top');
	
}
//月
var drillArea1=function(zoneName,zoneCode,channelTypeCode)
{	
	var startDate=$("dateTime1").value;
	var endDate=$("dateTime1").value;
	var url="/reports/channel/channelNew/channelServerSum.jsp?zoneCode="+zoneCode+"&startDate="+startDate+
	"&endDate="+endDate+"&channelTypeCode="+channelTypeCode+"&menuId="+menuId;
	return parent.openTreeTab("164010"+zoneCode+channelTypeCode, "线上线下渠道服务总体报表"+"["+zoneName+"]", base+url, 'top');
	
}
//日
var drillArea2=function(zoneName,zoneCode,channelTypeCode)
{	
	var startDate=$("startDate").value;
	var endDate=$("endDate").value;
	var url="/reports/channel/channelNew/channelServerSum.jsp?zoneCode="+zoneCode+"&startDate="+startDate+
	"&endDate="+endDate+"&channelTypeCode="+channelTypeCode+"&menuId="+menuId;
	return parent.openTreeTab("164010"+zoneCode+channelTypeCode, "线上线下渠道服务总体报表"+"["+zoneName+"]", base+url, 'top');
	
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
//日
function exportExcel2(){
		    var  startDate=$("startDate").value;//文本框\
		    var  endDate=$("endDate").value;//文本框
		    var  zone=$("zone2").value;//文本框
		    var  channelType=$("channelType2").value;//文本框
		    var queryCond="日期从："+startDate+"    至："+endDate+"    区域："+zone+"    渠道类型: "+channelType; 
	   	    $("excelCondition").value=queryCond; 
	    	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implChannelMoreExcel.jsp";
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
}
//周
function exportExcel(){
	var  startDate=$("dateTime").value;//文本框\
    var  endDate=$("dateTime").value;//文本框
    var  zone=$("zone").value;//文本框
    var  channelType=$("channelType").value;//文本框
    var queryCond="查询周："+startDate+"    区域："+zone+"    渠道类型: "+channelType; 
	$("excelCondition").value=queryCond; 
	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implChannelMoreExcel.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
//月
function exportExcel1(){
    var  startDate=$("dateTime1").value;//文本框\
    var  endDate=$("dateTime1").value;//文本框
    var  zone=$("zone1").value;//文本框
    var  channelType=$("channelType1").value;//文本框
    var queryCond="月份："+startDate+"    区域："+zone+"    渠道类型: "+channelType; 
    $("excelCondition").value=queryCond; 
	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implChannelMoreExcel.jsp";
	document.forms[0].method = "post";
	document.forms[0].action=url;
	document.forms[0].target="hiddenFrame";
	document.forms[0].submit();
}
dhx.ready(indexInit);