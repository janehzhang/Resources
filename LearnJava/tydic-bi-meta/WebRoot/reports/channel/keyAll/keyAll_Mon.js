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
 

var reportType=$("reportType").value;

 
var indexInit=function(){	
	
    //执行查询数据
    excuteInitData();
      
}

var excuteInitData=function(){
		 	indexExp();
		 var map=new Object();
		 map.month=$("dateTime").value;
		 
		 map.reportType=reportType;
 	     dhx.showProgress("正在执行......");
	     NewTwoChannelAction.queryKeyAll_pg(map, function (res) {
	     	   dhx.closeProgress();
	 			 if (res == null) {
	 				   dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
	 				     return;
	 			 }else{
	 				 buildTable(res);
	 			 }
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
 if(reportType=='1001'){
  var tableHead="<tr>"
		+"<td bgcolor='#cde5fd' rowspan='3' style='width:4%'><span class='title'>地区</span></td>"
	    +"<td bgcolor='#cde5fd' rowspan='3' style='width:4%' ><span class='title'>分公司</span></td>"
	    +"<td bgcolor='#cde5fd' colspan='7'><span class='title' align='center' >触点满意率</span></td>" 
	    +"<td bgcolor='#cde5fd' colspan='2'><span class='title' align='center'>网络质量</span></td>"
	    +"<td bgcolor='#cde5fd' colspan='1'><span class='title' align='center'>营业触点</span></td>"
	    +"</tr>"
	    +"<tr>"
	    +"<td bgcolor='#cde5fd'  colspan='7'><span class='title'>即时满意率</span></td>"
	    +"<td bgcolor='#cde5fd' colspan='2'><span class='title'>宽带用户感知体验良好率</span></td>"
	    /*+"<td bgcolor='#cde5fd' colspan='2' ><span class='title'>移动网络质量申告率</span></td>"*/
	    +"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>营业厅数量</span></td>"
	   +"</tr>"
	    
	    +"<tr>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>宽带装移机满意率</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>宽带维修满意率</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>实体渠道满意率</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>网厅满意率</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>掌厅满意率</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>号百满意率</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>10000号满意率</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>月均宽带故障修复及时</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>宽带故障申告率</span></td>"
	    /*+"<td bgcolor='#cde5fd' ><span class='title' align='center'>移动网络质量语音业务申告率</span></td>"
	     +"<td bgcolor='#cde5fd' ><span class='title' align='center'>移动网络质量数据业务申告率</span></td>"*/
	 +"</tr>";
	 }
	 
	 if(reportType=='1002'){
 		 var tableHead="<tr>"
			+"<td bgcolor='#cde5fd' rowspan='2' style='width:4%'><span class='title'>地区</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2' style='width:4%' ><span class='title'>分公司</span></td>"
		    /*+"<td bgcolor='#cde5fd' colspan='5'><span class='title' align='center' >工信部申诉率</span></td>" */
		    +"<td bgcolor='#cde5fd' colspan='5'><span class='title' align='center'>本地抱怨率</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='3'><span class='title' align='center'>投诉一次性解决率</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='3'><span class='title' align='center'>投诉处理回访满意率</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2'><span class='title' align='center'>重复投诉处理率</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2'><span class='title' align='center'>投诉处理及时率</span></td>"
		    /*+"<td bgcolor='#cde5fd' colspan='2'><span class='title' align='center'>升级督办</span></td>"*/
		    +"</tr>"
		     +"<tr>"
		   /* +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>工信部申诉量</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>工信部申诉率（宗/百万用户）</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>环比升降幅度</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>累计月均值</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>同比升降幅度</span></td>"*/
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>本地抱怨总量</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>本地抱怨率</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>环比升降幅度</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>累计月均值</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>同比升降幅度</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title' align='center'>当月投诉一次性解决率</span></td>"
		     +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>环比升降幅度</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>截至本月投诉一次性解决率</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>当月投诉处理回访满意率</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title' align='center'>环比升降幅度</span></td>"
		     +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>截至本月投诉处理回访满意率</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>当月重复投诉处理率</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>环比升降幅度</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title' align='center'>当月投诉处理及时率</span></td>"	     
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>环比升降幅度</span></td>"
		    /*+"<td bgcolor='#cde5fd' ><span class='title' align='center'>当月升级督办率</span></td>"
		     +"<td bgcolor='#cde5fd' ><span class='title' align='center'>督办率环比升降幅度</span></td>"		  */   
		 +"</tr>";
	 } 
	 if(reportType=='1003'){
 		 var tableHead="<tr>"
			+"<td bgcolor='#cde5fd' rowspan='4' style='width:3%'><span class='title'>地区</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='4' style='width:3%'><span class='title'>分公司</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='4' style='width:4%'><span class='title' align='center' >移动用户数到达值（万户）</span></td>" 
		    +"<td bgcolor='#cde5fd' colspan='23'><span class='title' align='center'>考核指标</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='17'><span class='title' align='center'>离网观察指标</span></td>"		    
		    +"</tr>"
			+"<tr>"
		    +"<td bgcolor='#cde5fd'  colspan='13'><span class='title'>移动用户月均离网率</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='5'><span class='title'>机补用户月均离网率</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='5' ><span class='title'>新发展用户月均离网率</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='11'><span class='title'>移动用户当月离网率</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='3' ><span class='title'>移动机补用户当月离网率</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='3'><span class='title'>新发展用户当月离网率</span></td>"
		    +"</tr>"
		    +"<tr>"
		    +"<td bgcolor='#cde5fd'  colspan='5'><span class='title'>全网用户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2'><span class='title'>后付费用户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2' ><span class='title'>预付费用户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2'><span class='title'>政企用户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2' ><span class='title'>公众用户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='5'><span class='title'>全网用户</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='5'><span class='title'>全网用户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='3'><span class='title'>全网用户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2' ><span class='title'>后付费</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2'><span class='title'>预付费</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2' ><span class='title'>政企</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2'><span class='title'>公众</span></td>"
		    +"<td bgcolor='#cde5fd'  colspan='3'><span class='title'>全网用户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='3'><span class='title'>全网用户</span></td>"
		    +"</tr>"
		    
		     +"<tr>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>至本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>目标值</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较目标值</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>排名</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>至本月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>至本月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>至本月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>至本月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>至本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>目标值</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较目标值</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>排名</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>至本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>目标值</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较目标值</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>排名</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>排名</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>排名</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>排名</span></td>" 		     
		 +"</tr>";
	 } 	
	 if(reportType=='1004'){
 		 var tableHead="<tr>"
			+"<td bgcolor='#cde5fd' rowspan='4' style='width:4%'><span class='title'>地区</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='4' style='width:4%' ><span class='title'>分公司</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='4' style='width:6%'><span class='title' align='center' >宽带用户数到达值（万户）</span></td>" 
		    +"<td bgcolor='#cde5fd' colspan='13'><span class='title' align='center'>离网观察指标</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='8'><span class='title' align='center'>存量用户质量观察指标</span></td>"		    
		    +"</tr>"
			+"<tr>"
		    +"<td bgcolor='#cde5fd'  colspan='7'><span class='title'>宽带月均离网率</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='6'><span class='title'>宽带当月离网率</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2' colspan='2'><span class='title'>宽带预存融合率</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2' colspan='2'><span class='title'>宽带预存率</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2' colspan='2'><span class='title'>宽带到期续约率</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2' colspan='2'><span class='title'>光纤宽带12M及以上占比</span></td>"
		    +"</tr>"
		    +"<tr>"
		    +"<td bgcolor='#cde5fd'  colspan='3'><span class='title'>全网用户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2'><span class='title'>后付费</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2' ><span class='title'>预付费</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2'><span class='title'>全体用户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2' ><span class='title'>后付费</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2'><span class='title'>预付费</span></td>" 
		    +"</tr>"
		     +"<tr>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>至本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>排名</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>至本月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>至本月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较上月</span></td>"
		  	+"<td bgcolor='#cde5fd'  ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>本月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较上月</span></td>"	     
		 +"</tr>";
	 } 		 
	if(reportType=='1005'){
 		 var tableHead="<tr>"
			+"<td bgcolor='#cde5fd' rowspan='4' style='width:3%'><span class='title'>地区</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='4' style='width:3%' ><span class='title'>分公司</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='31'><span class='title' align='center' >考核指标</span></td>" 
		   	+"</tr>"
			+"<tr>"
		    +"<td bgcolor='#cde5fd' colspan='17'><span class='title'>中 高 端 客 户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='15'><span class='title'>中高端客户收入保有</span></td>"
		    +"</tr>"
		    +"<tr>"
		    +"<td bgcolor='#cde5fd' colspan='5'><span class='title'>中高端客户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='6'><span class='title'>移动中高端拍照用户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='6'><span class='title'>钻金银会员拍照客户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='4'><span class='title'>中高端客户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='5' ><span class='title'>移动中高端拍照用户</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='5'><span class='title'>钻金银拍照客户</span></td>" 
		    +"</tr>"
		     +"<tr>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>本月保有率</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>目标值</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较目标值</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>排名</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>拍照用户数（万户）</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>本月保有用户数（万户）</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>本月保有率</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>排名</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>拍照客户数（万户）</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>本月保有客户数（万户）</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>本月保有率</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>排名</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>月均收入保有率</span></td>"
		  	+"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较目标值</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较去年同期</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>排名</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>拍照总收入（万元）</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>当月保有用户收入（万元）</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>当月收入保有率</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>较上月</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>排名</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>拍照总收入（万元）</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>当月保有会员收入（万元）</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>当月收入保有率</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>较上月</span></td>"	  
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>排名</span></td>"	 	     
		 +"</tr>";
	 } 		  
	if(reportType=='1006'){
 		 var tableHead="<tr>"
			+"<td bgcolor='#cde5fd' rowspan='3' style='width:3%'><span class='title'>地区</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='3' style='width:3%' ><span class='title'>分公司</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='3'><span class='title' align='center' >VIP经理人数</span></td>" 
		    +"<td bgcolor='#cde5fd' rowspan='3'><span class='title' align='center' >VPN经理人数</span></td>" 
		    +"<td bgcolor='#cde5fd' rowspan='3'><span class='title' align='center' >客户密码激活率</span></td>" 
		    +"<td bgcolor='#cde5fd' colspan='11'><span class='title' align='center' >新媒体服务量</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='9'><span class='title' align='center' >新媒体用户数</span></td>" 
		    +"<td bgcolor='#cde5fd' colspan='3'><span class='title' align='center' >考核指标</span></td>" 
		    +"<td bgcolor='#cde5fd' colspan='8'><span class='title' align='center' >10000号基础运营数据</span></td>"  
		   	+"</tr>"
			+"<tr>"
		    +"<td bgcolor='#cde5fd' colspan='3'><span class='title'>中国电信广东客服（QQ）</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='3'><span class='title'>中国电信广东客服（微信）</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='3'><span class='title'>中国电信客服（易信）</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>中国电信广东客服（微博）</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>10000知道/爱问</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2'><span class='title'>中国电信广东客服（QQ）</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2'><span class='title'>中国电信广东客服（微信）</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2'><span class='title'>中国电信易信客服</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='2'><span class='title'>中国电信广东客服（微博）</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>掌厅注册用户数</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>10000号自助服务占比</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>在线客服用户满意率</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>在线客服客户问题一次解决率</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>10000号坐席数</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>外包人数</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>20秒接通率</span></td>"
		    +"<td bgcolor='#cde5fd' colspan='3'><span class='title'>话务量</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>10000+9人工服务量</span></td>"
		    +"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>速拨干线业务量</span></td>"
		    +"</tr>"
		    +"<tr>"
		    +"<td bgcolor='#cde5fd'  ><span class='title'>人工</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title'>自助</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title'>合计</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title'>人工</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title'>自助</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title'>合计</span></td>" 
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>人工</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>自助</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>合计</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>好友数</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>QQ客服活跃用户数</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>好友数</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>活跃用户数</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>关注数</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>捆绑数</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>粉丝数</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>活跃好友数</span></td>"
		    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>人工</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>自助</span></td>"
		    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>10001自助</span></td>"
		 +"</tr>";
	 } 
	 
	if(reportType=='1007'){
  		var tableHead="<tr>"
		+"<td bgcolor='#cde5fd' rowspan='2' style='width:6%'><span class='title'>地区</span></td>"
	    +"<td bgcolor='#cde5fd' rowspan='2' style='width:6%' ><span class='title'>分公司</span></td>"
	    +"<td bgcolor='#cde5fd' colspan='4'><span class='title' align='center' >综合满意度</span></td>" 
	    +"<td bgcolor='#cde5fd' colspan='2'><span class='title' align='center'>渠道服务感知良好率</span></td>"
	    +"</tr>"
	    +"<tr>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>综合满意度</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>3G/4G 业务满意度</span></td>"
	    +"<td bgcolor='#cde5fd' ><span class='title' align='center'>宽带业务满意度</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>行业应用业务满意度</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>第三方测评</span></td>"
	    +"<td bgcolor='#cde5fd'  ><span class='title' align='center'>即时满意测评</span></td>"  
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
     if(dataColumn&&dataColumn.length>0){  
	       for(var i=0;i<dataColumn.length;i++){
	    	tableData +="<tr onmouseover=\"this.style.backgroundColor='#eef6fe'\" onmouseout=\"this.style.backgroundColor='#ffffff'\">";
	    	  for(var j=0;j<headColumn.length;j++) {
   		            var tempColumn=headColumn[j]; 
   		               if(tempColumn.indexOf('_')==-1){ 
		    		      tableData +="<td>"+dataColumn[i][tempColumn]+"</td>"; 
   		            	  excelData += dataColumn[i][tempColumn]+"}";
		    		} 
   		      }
	    	  tableData +="</tr>";
			  excelData +="]";
	    	}   
	       } else {
		     tableData +="<tr>"
		                   +"<td colspan='100'>没有数据显示</td>"
		               +"</tr>";
       } 
     $("excelData").value= excelData;//Excel数据
	return tableData;
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
 //导出
function exportExcel(){
			
		    var  dateTime=$("dateTime").value;
		    var queryCond="月份："+dateTime+"" ; 
		    var excelUrl="";
	   	    $("excelCondition").value=queryCond; 
			    if(reportType=='1001'){
				  	excelUrl="/portalCommon/module/procedure/impExcel/implAaeaChannelMoreKeyExcel2.jsp";
				 }
				if(reportType=='1002'){
				 	excelUrl="/portalCommon/module/procedure/impExcel/implAaeaChannelMoreKeyExcel2.jsp";
				 }	 
				if(reportType=='1003'){
				 	excelUrl="/portalCommon/module/procedure/impExcel/implAaeaChannelMoreKeyExcel.jsp";
				}
				if(reportType=='1004'){
				   excelUrl="/portalCommon/module/procedure/impExcel/implAaeaChannelMoreKeyExcel.jsp";
				}
			    if(reportType=='1005'){
				  excelUrl="/portalCommon/module/procedure/impExcel/implAaeaChannelMoreKeyExcel.jsp";
				}
			    if(reportType=='1006'){
					excelUrl="/portalCommon/module/procedure/impExcel/implAaeaChannelMoreKeyExcel2.jsp";
				}			   	    
	     	var url = getBasePath()+excelUrl;
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
}

//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 var explainData="";
	 var map=new Object();
	if(reportType=='1001'){
	  	map.rptId="1020";
	 }
	if(reportType=='1002'){
	 	 map.rptId="1021";
	 }	 
	if(reportType=='1003'){
	 	 map.rptId="1022";
	}
	if(reportType=='1004'){
	 	map.rptId="1023";
	}
    if(reportType=='1005'){
	 	map.rptId="1024";
	}
    if(reportType=='1006'){
		 map.rptId="1025";
	}		
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