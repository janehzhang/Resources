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

var dateTime = $("dateTime").value;
var zoneCode =   $('zoneCode').value==""?userInfo['localCode']:$("zoneCode").value;
var parameters=$("parameters").value;

if(user['zoneId']!='1') { //不是广东省  （钻取权限控制）
	var array=document.getElementsByName('city');
	    for(var i=0;i<array.length;i++){ 
	      array[i].style.display = "none";
	}
}
var treeConverter=new dhtmxTreeDataConverter({
    idColumn:"deptCode",pidColumn:"parentCode",
    isDycload:false,textColumn:"deptName"
});
/** Tree head **/
dwrCaller.addAutoAction("loadZoneTree","ZoneAction.queryZoneByPath1");
var zoneConverter=dhx.extend({idColumn:"zoneCode",pidColumn:"zoneParCode",
    textColumn:"zoneName"
},treeConverter,false);
dwrCaller.addDataConverter("loadZoneTree",zoneConverter);
dwrCaller.addAction("querySubZoneCode",function(afterCall,param){
    var tempCovert=dhx.extend({isDycload:true},zoneConverter,false);
    ZoneAction.querySubZone1(param.id,function(data){
        data=tempCovert.convert(data);
        afterCall(data);
    })
});
var indexInit=function(){
	//1.加载地域树 
     loadZoneTreeChkBox(zoneCode,queryform);
     //执行查询数据
     excuteInitData();
     indexExp();
}


var excuteInitData=function(){
	 var map=new Object();
     map.dateTime     =$("dateTime").value;
     map.zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
     zoneCode    =$("zoneCode").value==""?userInfo['localCode']:$("zoneCode").value;
     indexId =$("userDimension").value;
     
     map.reportId=$("rptId").value;
     map.parameters=parameters; 
     map.values= $("dateTime").value.replaceAll("-", "")+"$"+zoneCode+"$"+indexId;  
     dhx.showProgress("正在执行......");
     ServiceReActiveAction.getCustKeep_pg(map, function (res) {
    	               dhx.closeProgress();
 				        if (res == null) {
 				            dhx.alert("读取数据发生错误,请稍后重试，或者联系系统管理员!");
 				            return;
 				        }
 					 buildTable(res);
    });	
}
function buildTable(data){
    tableStr ="<table  id='tab1' width='100%' height='auto'  border='0' cellpadding='0' cellspacing='0' class='tab_01'>";  
    tableStr+=tableHeader(data.headColumn);
    tableStr+=tableData(data.dataColumn,data.headColumn);
    tableStr +="</table>"; 
    $('chartTable').innerHTML=tableStr;
    autoRowSpan($("tab1"),3,0);
}
/**
*  构造表头
* @param {Object} data
*/
function tableHeader(headColumn){
var  excelHeader="";
var  tableHead="";
var tempId=$("rptId").value;
if(tempId=='30021'){
	tableHead="<tr>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>大区</span></td>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>分公司</span></td>"
		+"<td nowrap bgcolor='#cde5fd'  style='width:500px' colspan='158'><span class='title'>投诉</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>总计</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6'><span class='title'>缴费问题</span></td>"
		+"<td bgcolor='#cde5fd' colspan='13'><span class='title'>开通/变更/停用问题</span></td>"
		+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>互联互通</span></td>"
		+"<td bgcolor='#cde5fd' colspan='10'><span class='title'>积分及客户关怀</span></td>"
		+"<td bgcolor='#cde5fd' colspan='9'><span class='title'>国际及港澳台漫游质量</span></td>"
		+"<td bgcolor='#cde5fd' colspan='19'><span class='title'>渠道服务质量</span></td>"
		+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>装、拆、移机服务</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>帐单清单发票问题</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6'><span class='title'>修障问题</span></td>"
		+"<td bgcolor='#cde5fd' colspan='11'><span class='title'>营销及规则政策类</span></td>"
		+"<td bgcolor='#cde5fd' colspan='10'><span class='title'>终端及UIM卡</span></td>"
		+"<td bgcolor='#cde5fd' colspan='8'><span class='title'>充值问题</span></td>"
		+"<td bgcolor='#cde5fd' colspan='20'><span class='title'>费用争议</span></td>"
		+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>网络质量</span></td>"
		+"<td bgcolor='#cde5fd' colspan='16'><span class='title'>无法正常使用（非终端问题）</span></td>"
		+"<td bgcolor='#cde5fd' colspan='7'><span class='title'>业务推送</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6'><span class='title'>通知提醒</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' ><span class='title'>催缴问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>代理代收费（POS机、付费易卡等）</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>银行托收费</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>网上缴费</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>无故取消</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>业务开通不及时</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>取消不及时</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>定购不成功</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>异常停开机</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>信控停机/信用度管理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>复机不及时</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户资料出错</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>业务变更问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>无法取消订制</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>系统无客户申请记录</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>与移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>与联通</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>物流派送问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>服务内容不满意</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>网站问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>积分规则及计算问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>俱乐部网站无法正常使用</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>会员身份认证问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>积分查询问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>兑换问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>礼品质量问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>无网无信号</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>无法正常登记</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>无法使用数据业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>有信号、无法主被叫</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>有信号、无法收发短信</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>功能申请类</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>无来电显示或显示不正常</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>自助终端</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>业务办理差错</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>服务时限</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>解释、说明、宣传不清晰/错误</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>无法正常办理业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>功能及响应时限</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>难打通</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>无法登录</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>服务态度</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>内容功能不完善、不正确</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>IVR导航</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>在线客服（QQ/网页）答复内容不满意</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>在线客服（QQ/网页）没答复或者答复不及时</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>在线客服（QQ/网页）营销推广活动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>代理服务点</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>营业厅</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>诱导办理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>装、拆、移机不及时</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>装、拆、移机质量差</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>装、拆、移机人员服务态度问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>投递问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>帐单展示问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>超时未修复</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>障碍频繁发生</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>维修人员服务态度问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>修障质量差</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>节目内容不满意</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>业务规则不合理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>单方更改套餐</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>资费未明示</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>诱导信息</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>不平等格式条款</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>业务内容不清晰</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>业务下线争议</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宣传与实际不符</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>上网卡</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>CDMA终端</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>UIM卡</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>调制解调器等终端设备</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小灵通终端</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>行业应用终端</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话终端</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听设备</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>购卡不方便/难</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>充值不准确/不成功</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>充值不及时</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>充值不到帐</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>充值后不能正常使用</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>空中充值/自动充值问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>未取消套餐赠送业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>停、拆机后仍收费</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>一次性费用争议</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>通话时长</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>代收费用/信息费</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>终端维修费用争议</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>上网时长/流量</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>边界漫游争议</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>生效规则争议</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>余额争议</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>违约金</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>装移维期间费用争议</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>月基本费争议</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>实时话费争议</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>重复话单</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>超长/超短/超频话单</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>优惠类（赠送话费）费用争议</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>套餐费用争议</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>省际漫游质量</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>上网质量</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>非漫游质量</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>省内漫游质量</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>定位使用问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>乱码</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户端软件问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>延迟收发</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>打不开首页</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>短信延迟</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>无法收发</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>无法正常登陆</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>WIFI热点欢迎短信</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>重复收发</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>网站其他使用问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>行业应用功能异常</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>行业应用产品业务平台问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>行业应用产品手机客户端软件问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带续约推送</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>营销语音推送</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带网络推送</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>WiFi推送</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>WAP PUSH 推送</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>短信推送/群发</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>未收到提醒</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>提醒不及时</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>提醒错误</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>重复提醒</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>";
}if(tempId=='30022'){
	tableHead="<tr>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>大区</span></td>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>分公司</span></td>"
		+"<td nowrap bgcolor='#cde5fd'  style='width:500px' colspan='75'><span class='title'>需求</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>总计</span></td>"
		+"<td bgcolor='#cde5fd' colspan='2'><span class='title'>缴费问题</span></td>"
		+"<td bgcolor='#cde5fd' colspan='7'><span class='title'>积分及客户关怀</span></td>"
		+"<td bgcolor='#cde5fd' colspan='18'><span class='title'>业务办理</span></td>"
		+"<td bgcolor='#cde5fd' colspan='10'><span class='title'>装、拆、移机服务</span></td>"
		+"<td bgcolor='#cde5fd' colspan='2'><span class='title'>表扬</span></td>"
		+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>建议类</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>修障问题</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>终端及UIM卡</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>客户举报</span></td>"
		+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>充值问题</span></td>"
		+"<td bgcolor='#cde5fd' colspan='7'><span class='title'>局方设备</span></td>"
		+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>费用类</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>广州分公司代理商支撑单</span></td>"
		+"<td bgcolor='#cde5fd' colspan='2'><span class='title'>客户个人信息保护问题</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' ><span class='title'>取消催缴</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>会员等级查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>会员卡补寄</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>积分业务咨询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>积分业务办理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>积分业务取消</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>屏蔽/解限需求</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>礼品配送</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>延期缴费</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户违章停机（复机）</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>核查骚扰电话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>业务功能增删</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>要求找客户经理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>营销/政策咨询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>密码设置</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>更改资料</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>业务催办</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>帐单发票补/催投递及信息变更</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>修改10000营销配送信息（广州专用）</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>集团商旅积分查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>赠品质量问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>集团要求处理的业务需求</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>地址核查</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>变更上门装机/维修时间/联系人/联系电话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>未交款催装机</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户类型核查</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>已到施工环节客户要求撤消装移单</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>缓装用户要求装机</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>已交款未超时催装机</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>未预约催装机（未超24小时）</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>表扬装、拆、移机人员服务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>-</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>转至行业应用专席应答</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户建议（无需回复客户）</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>刷卡感应问题（无需回复客户）</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>营业环境</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>未超时催修复/处理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>表扬维修人员服务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>变更上门时间/联系人/联系电话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>补/换卡或终端</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>接到怀疑诈骗的催缴电话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>不良信息举报</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>延长充值卡有效期</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户错充值要求处理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>实体充值卡密码错误或因有数字被刮花需查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>余额转移</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>基站等设备要求装移拆</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>沙井盖破损/丢失</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>电话外线凌乱</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>线缆、电杆破损</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>怀疑线路被盗用</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小额快速退费</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户催退款</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>系统问题（广州分公司专用）</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>线路问题（广州分公司专用）</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>-</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>";
}if(tempId=='30023'){
	tableHead="<tr>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>大区</span></td>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>分公司</span></td>"
		+"<td nowrap bgcolor='#cde5fd'  style='width:500px' colspan='125'><span class='title'>咨询</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>总计</span></td>"
		+"<td bgcolor='#cde5fd' colspan='14'><span class='title'>优惠促销</span></td>"
		+"<td bgcolor='#cde5fd' colspan='2'><span class='title'>传播画面口径</span></td>" //-8
		+"<td bgcolor='#cde5fd' colspan='6'><span class='title'>最新问题解释口径</span></td>"
		+"<td bgcolor='#cde5fd' colspan='9'><span class='title'>我的e家</span></td>"
		+"<td bgcolor='#cde5fd' colspan='11'><span class='title'>天翼移动业务</span></td>"//+1
		+"<td bgcolor='#cde5fd' colspan='10'><span class='title'>政企业务</span></td>"//+1
		+"<td bgcolor='#cde5fd' colspan='6'><span class='title'>互联网业务</span></td>"
		+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>增值业务</span></td>"
		+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>语音类</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6'><span class='title'>公话/卡类业务</span></td>"
		+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>号码百事通</span></td>"
		+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>客户俱乐部</span></td>"
		+"<td bgcolor='#cde5fd' colspan='8'><span class='title'>账务计费</span></td>"
		+"<td bgcolor='#cde5fd' colspan='6'><span class='title'>其他知识</span></td>"
		+"<td bgcolor='#cde5fd' colspan='12'><span class='title'>渠道服务</span></td>"
		+"<td bgcolor='#cde5fd' colspan='9'><span class='title'>2013年第三季热点问题</span></td>"//+1
		+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>手机终端</span></td>"
		+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>短信群发</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' ><span class='title'>我的e家优惠</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>天翼移动优惠</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带优惠</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网优惠</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>增值优惠</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>语音类优惠</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>公话卡类优惠</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>号码百事通优惠</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户俱乐部优惠</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>政企优惠</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>重要节日及活动优惠</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>龙计划</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话优惠</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>-</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>短信通知</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>割接及障碍通知</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>语音通知</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>网络PUSH通知</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他通知</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e9套餐/(含光速e家)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e8套餐</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e6套餐</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>补贴优惠</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e家应用</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>咨询新装办理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>续约</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>基本功能</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>基本功能-呼叫转移</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>基本功能-停复机</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>套餐</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>国际业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>无线宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>3G应用</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>3G流量包</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>手机流量包</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>政企固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动产品</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带产品</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商务领航</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>行业应用</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>增值转型业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>资源出租</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>VPN群</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航业务咨询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>光纤宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带拨号业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>窄带拨号业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网专线业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>语音类</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固定电话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小灵通</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>长途业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>特服号码</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>17909卡</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>200卡</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>公用电话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>充值付费卡</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他卡类</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>基本业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>声讯业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>号薄与广告类</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>俱乐部介绍</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>会员服务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户积分计划</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>电脑管家</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>缴费方式</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>清单查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>发票账单</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>实时信控</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>“南粤金税”发票抽奖</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>话费有效期</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户密码</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>卫星业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他运营商</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>业务知识英文版</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>营业厅</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>网上营业厅</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>掌上营业厅</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>短信营业厅</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>IM客服</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>直销渠道</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>社会渠道</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户经理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>电信微博</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他渠道</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>微信客服</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>火车通</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客运通</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>爱音乐</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>院线通</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带提速</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>光纤升级</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>光纤平移</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>2G手机</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>3G智能机</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>央视报道</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>短信群发相关业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>短信屏蔽</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>";
}if(tempId=='30024'){
	tableHead="<tr>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>大区</span></td>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>分公司</span></td>"
		+"<td nowrap bgcolor='#cde5fd'  style='width:500px' colspan='30'><span class='title'>查询</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>总计</span></td>"
		+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>信息查询</span></td>"
		+"<td bgcolor='#cde5fd' colspan='10'><span class='title'>进度查询</span></td>" 
		+"<td bgcolor='#cde5fd' colspan='6'><span class='title'>话费查询</span></td>"
		+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>积分查询</span></td>"
		+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>其他查询</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户信息查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>产品信息查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>使用状态/欠停查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>套餐/优惠受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>故障处理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>投诉处理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他进度查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>装机进度查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>实时/月结话费（含总额）查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>余额/赠金查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>话费明细</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>扣费退费</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航查费</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户积分</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>当月积分</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>积分兑换</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户经理查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>PUK码/PIN码查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>传真件查询</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>";
}if(tempId=='30025'){
	tableHead="<tr>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>大区</span></td>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>分公司</span></td>"
		+"<td nowrap bgcolor='#cde5fd'  style='width:500px' colspan='43'><span class='title'>办理</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>总计</span></td>"
		+"<td bgcolor='#cde5fd' colspan='14'><span class='title'>新装/办理</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>取消</span></td>" 
		+"<td bgcolor='#cde5fd' colspan='9'><span class='title'>改功能</span></td>"
		+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>移机</span></td>"
		+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>账务受理</span></td>"
		+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>其他受理</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>预受理</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e家全业务(含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e家非全业务(不含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航全业务(含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航非全业务(不含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>优惠受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固网增值业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动增值业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>龙计划</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航业务受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>套餐/优惠取消</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>增值业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>停复机</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>套餐/优惠变更</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固网增值业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动增值业务</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>光纤平移</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>扣费、充值</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>临时信用度调整</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>投递信息修改</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>账务信息修改</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小灵通业务受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>卡类业务受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>套餐销售品</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>基础销售品</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>";
}if(tempId=='30026'){
	tableHead="<tr>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>大区</span></td>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>分公司</span></td>"
		+"<td nowrap bgcolor='#cde5fd'  style='width:500px' colspan='22'><span class='title'>报障</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>总计</span></td>"
		+"<td bgcolor='#cde5fd' colspan='9'><span class='title'>故障申告</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>未超时催修</span></td>" 
		+"<td bgcolor='#cde5fd' colspan='7'><span class='title'>已超时催修</span></td>"
		+"<td bgcolor='#cde5fd' colspan='2'><span class='title'>故障追加/修改信息</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>大面积故障</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>工程割接</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他故障申告</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航故障</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>未超时催修</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>已超时首次催修</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>已超时再次催修</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>已超时不愿联系</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>已超时联系无效</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>越级客户催修</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>故障追加/修改信息</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>";
}if(tempId=='30027'){
	tableHead="<tr>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>大区</span></td>"
		+"<td nowrap bgcolor='#cde5fd' style='width:50px' rowspan='3'><span class='title'>分公司</span></td>"
		+"<td nowrap bgcolor='#cde5fd'  style='width:500px' colspan='141'><span class='title'>投诉</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' rowspan='2'><span class='title'>总计</span></td>"
		+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>装移拆服务</span></td>"
		+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>维修服务</span></td>" //-8
		+"<td bgcolor='#cde5fd' colspan='11'><span class='title'>费用争议</span></td>"
		+"<td bgcolor='#cde5fd' colspan='11'><span class='title'>缴费充值</span></td>"
		+"<td bgcolor='#cde5fd' colspan='10'><span class='title'>帐单清单发票问题</span></td>"//+1
		+"<td bgcolor='#cde5fd' colspan='7'><span class='title'>网络质量与使用</span></td>"//+1
		+"<td bgcolor='#cde5fd' colspan='10'><span class='title'>业务办理</span></td>"
		+"<td bgcolor='#cde5fd' colspan='10'><span class='title'>规则政策</span></td>"
		+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>终端</span></td>"
		+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>增值业务</span></td>"
		+"<td bgcolor='#cde5fd' colspan='15'><span class='title'>行业应用</span></td>"
		+"<td bgcolor='#cde5fd' colspan='2'><span class='title'>号百业务</span></td>"
		+"<td bgcolor='#cde5fd' colspan='2'><span class='title'>积分/礼品兑换问题</span></td>"
		+"<td bgcolor='#cde5fd' colspan='12'><span class='title'>渠道服务</span></td>"
		+"<td bgcolor='#cde5fd' colspan='4'><span class='title'>其它投诉</span></td>"
		+"<td bgcolor='#cde5fd' colspan='2'><span class='title'>垃圾短信</span></td>"//+1
		+"<td bgcolor='#cde5fd' colspan='2'><span class='title'>限制短信发送条数</span></td>"
		+"<td bgcolor='#cde5fd' colspan='2'><span class='title'>客户信息安全</span></td>"
		+"<td bgcolor='#cde5fd' colspan='12'><span class='title'>热点关注</span></td>"
		+"<td bgcolor='#cde5fd' colspan='5'><span class='title'>催/追单</span></td>"
		+"<td bgcolor='#cde5fd' colspan='3'><span class='title'>非法营销问题</span></td>"
		+"</tr><tr>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带/数据专线</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e家全业务(含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e家非全业务(不含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航全业务(含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航非全业务(不含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>信控</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e家全业务(含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e家非全业务(不含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航全业务(含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航非全业务(不含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>充值付费问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e家全业务(含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e家非全业务(不含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航全业务(含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航非全业务(不含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"	
		+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动语音</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动数据</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小灵通</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e家全业务(含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e家非全业务(不含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航全业务(含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航非全业务(不含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"	
		+"<td bgcolor='#cde5fd' ><span class='title'>e家全业务(含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>e家非全业务(不含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航全业务(含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航非全业务(不含移动产品)</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网视听(ITV)受理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"	
		+"<td bgcolor='#cde5fd' ><span class='title'>固话</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>宽带</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>移动</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>语音类</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>互联网</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>翼机通</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>翼定位</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>天翼对讲</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>加密通信</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>旺铺助手</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>警务e通</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>天翼黑莓</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>税务e通</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>天翼税通</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其它行业应用</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>商航投诉</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>手机看店</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>标准化移动办公</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>翼办公</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>-</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>-</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>10000</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>掌厅/短厅/网厅</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>IM客服</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>微博客服</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>营业厅</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>客户经理</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>主动营销</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>俱乐部</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>配送</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其它</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>微信客服</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>系统升级/割接</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>系统故障</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其它投诉</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>-</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>-</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>-</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>强绑在线解决</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>强绑下单解决</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>强关在线解决</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>强关下单解决</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>强订在线解决</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>强订下单解决</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小灵通双停拆机</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>强推在线解决</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>强推下单解决</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>强发在线解决</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>强发下单解决</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>催单</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>追单</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>复机未超时</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>停机</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>499元非法营销问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>其他非法营销问题</span></td>"
		+"<td bgcolor='#cde5fd' ><span class='title'>小计</span></td>";
}
    tableHead+="</tr>";
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
					    		       tableData +="<td>"+dataColumn[i][tempColumn]+"</td>";	 
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
//查询
var queryData=function(){
	excuteInitData();
}
//指标解释
function indexExp(){
	 $('index_exp').innerHTML='';
	 var explainData="";
	 var rptId="";
	 var indexId =$("userDimension").value;
	 var map=new Object(); 
	 if(indexId=="01"){
		 rptId="69";
	 }else if(indexId=="02"){
		 rptId="70";
	 }else if(indexId=="03"){
		 rptId="71";
	 }else if(indexId=="04"){
		 rptId="72";
	 }else if(indexId=="05"){
		 rptId="73";
	 }else if(indexId=="06"){
		 rptId="74";
	 }else if(indexId=="07"){
		 rptId="75";
	 }else if(indexId=="08"){
		 rptId="76";
	 }else if(indexId=="09"){
		 rptId="45";
	 }else if(indexId=="10"){
		 rptId="46";
	 }else if(indexId=="11"){
		 rptId="47";
	 }else if(indexId=="12"){
		 rptId="48";
	 }else if(indexId=="13"){
		 rptId="49";
	 }else if(indexId=="14"){
		 rptId="50";
	 }else if(indexId=="15"){
		 rptId="51";
	 }else if(indexId=="16"){
		 rptId="52";
	 }else if(indexId=="17"){
		 rptId="53";
	 }else if(indexId=="18"){
		 rptId="54";
	 }else if(indexId=="19"){
		 rptId="55";
	 }else if(indexId=="20"){
		 rptId="56";
	 }else if(indexId=="21"){
		 rptId="57";
	 }else if(indexId=="22"){
		 rptId="58";
	 }else if(indexId=="23"){
		 rptId="59";
	 }else if(indexId=="24"){
		 rptId="60";
	 }else if(indexId=="25"){
		 rptId="61";
	 }else if(indexId=="26"){
		 rptId="62";
	 }else if(indexId=="27"){
		 rptId="63";
	 }else if(indexId=="28"){
		 rptId="64";
	 }
	 map.rptId=rptId;
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
dhx.ready(indexInit);