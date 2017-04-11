<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="tydic.reports.customerSatisfied.CustomerSatisfiedDAO"%>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    String clickTab="";
	CustomerSatisfiedDAO dao=new CustomerSatisfiedDAO();
	String vTypeId =(String)request.getParameter("vTypeId")==null?"2":(String)request.getParameter("vTypeId");
	String testMethod =(String)request.getParameter("testMethod")==null?"":(String)request.getParameter("testMethod");//评价器
	//周
	String week = request.getParameter("dateTime");
	if(week==null || week.equals("")){
	    week=dao.getMaxDateTime_CustomerSatisfiedSum("1",vTypeId);
	}else{
    	 clickTab="tab2";
  	}
	List<Map<String, Object>> weekList=null;
	if(vTypeId=="5"||"5".equals(vTypeId)){
		weekList=dao.getDateTimeList10000("1");
	}else{
		weekList=dao.getDateTimeListSum("1");
	}
	request.setAttribute("dateTime",week);
	//月
	String month = request.getParameter("dateTime1");
	if(month==null || month.equals("")){
		month=dao.getMaxDateTime_CustomerSatisfiedSum("2",vTypeId);
	}else{
   	    clickTab="tab1";
    }
	List<Map<String, Object>> monList=dao.getDateTimeListSum("2",vTypeId);
	request.setAttribute("dateTime1",month);
	  
	  //日
	String startTime =(String)request.getParameter("startDate");
	String endTime =(String)request.getParameter("endDate");
	String dayTime="";
	if(startTime==null || startTime.equals("")){
		//if(vTypeId.equals("5")){
		//	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	     //   dayTime=formatter.format(new Date());
	     //   dayTime=DateUtil.lastDay(dayTime, -2);
	    //    startTime=dayTime;
		//	endTime=dayTime;
		//}else{
			dayTime=dao.getMaxDate_EnterSum(vTypeId).substring(0,8);
		    String dayYear=dayTime.substring(0,4);
			String dayMon=dayTime.substring(4,6);
			String day=dayTime.substring(6,8);
			dayTime=dayYear+"-"+dayMon+"-"+day;
			startTime=dayTime;
			endTime=dayTime;
		//}
	}else{
	    clickTab="tab3";
	}
	  request.setAttribute("startDate",startTime);
	  request.setAttribute("endDate",endTime);
	  
	  
	  String areaCode="0000";
	  String zoneCode  =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
      String selectCol =(String)request.getParameter("selectCol")==null?"SATISFY_LV":(String)request.getParameter("selectCol");
      String userCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
      
      List<Map<String, Object>> vTypeList=dao.getVTypeList();
%>
<head>
    
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplIndexAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ReportConfigAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/warning.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
	function impExcel(form){
		dhx.showProgress("正在执行......");
		exportImage(); 
		window.setTimeout("exportExcel()",8000);
	    window.setTimeout("dhx.closeProgress()",8000);
	}
	function impExcel1(form){
		dhx.showProgress("正在执行......");
		exportImage(); 
		window.setTimeout("exportExcel1()",8000);
	    window.setTimeout("dhx.closeProgress()",8000);
	}
	function impExcel2(form){
		dhx.showProgress("正在执行......");
		exportImage(); 
		window.setTimeout("exportExcel2()",8000);
	    window.setTimeout("dhx.closeProgress()",8000);
	}
	function method(obj,event)  
	 { 
	    var e = event || window.event;  
	    var elem = e.srcElement||e.target;         
	      while(elem)  
	    {   
	         if(elem.id == "menu"||elem.id == "menu1"||elem.id == "menu2"||elem.id == "menu10"||elem.id == "menu11"||
	    	         elem.id == "menu12"||elem.id == "menu20"||elem.id == "menu21"||elem.id == "menu22"||elem.id == "menu13"||elem.id == "menu14")  
	            {  
	                 return;  
	            }  
	                elem = elem.parentNode;       
	    }  
	            //隐藏div的方法  
	            document.getElementById("menu").style.display="none";
	            document.getElementById("menu1").style.display="none";
	            document.getElementById("menu2").style.display="none";
	            document.getElementById("menu10").style.display="none";
	            document.getElementById("menu11").style.display="none";
	            document.getElementById("menu12").style.display="none";
	            document.getElementById("menu13").style.display="none";
	            document.getElementById("menu14").style.display="none";
	            document.getElementById("menu20").style.display="none";
	            document.getElementById("menu21").style.display="none";
	            document.getElementById("menu22").style.display="none";
		            						
	} 	     
	</script>
	
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;" onMouseDown="method(this,event)">
   <div id="div_src" style="position: absolute; width: 100%; height:100%;z-index: 1000;top: 0px; left: 0px; background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:scroll;"
	                  title="" class="dragBar">
	  <center style="color: #CCCCCC;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;">数据加载中，请稍等....</center>
   </div>
		<div id="menu" style="display:none;border:1px solid #6699cc;position:absolute;z-index:1000;background:#B7FF4A;" />
		    <li><input type='button'  class="poster_btn"  value="服务人员工号"    onclick="openMenuHref(6,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:100px;" /></li>
		</div>
		<div id="menu1" style="display:none;border:1px solid #6699cc;position:absolute;z-index:1000;background:#B7FF4A;" />
		    <li><input type='button'  class="poster_btn"  value="服务人员工号"    onclick="openMenuHref(6,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:100px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="业务类型"    onclick="openMenuHref(8,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:100px;" /></li>
		</div>
		<div id="menu2" style="display:none;border:1px solid #6699cc;position:absolute;z-index:1000;background:#B7FF4A;" />
		    <li><input type='button'  class="poster_btn"  value="服务人员工号"    onclick="openMenuHref(6,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:100px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="业务类型"    onclick="openMenuHref(8,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:100px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="测评方式"    onclick="openMenuHref(7,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:100px;" /></li>
		</div>
		
		
		<div id="menu10" style="display:none;border:1px solid #6699cc;position:absolute;z-index:1000;background:#B7FF4A;">
		    <li><input type='button'  class="poster_btn"  value="服务人员工号"    onclick="openMenuHref1(6,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户群"    onclick="openMenuHref1(2,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户等级（VIP、普通）" onclick="openMenuHref1(3,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户价值（ARPU分档）" onclick="openMenuHref1(4,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="在网时长"    onclick="openMenuHref1(0,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="套餐类型"    onclick="openMenuHref1(5,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="受理渠道"    onclick="openMenuHref1(1,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li> 
		    <li><input type='button'  class="poster_btn"  value="产品类型"    onclick="openMenuHref1(9,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="终端类型"    onclick="openMenuHref1(10,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>          
		</div>
		<div id="menu14" style="display:none;border:1px solid #6699cc;position:absolute;z-index:1000;background:#B7FF4A;">
		    <li><input type='button'  class="poster_btn"  value="服务人员工号"    onclick="openMenuHref(6,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户群"    onclick="openMenuHref1(2,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户等级（VIP、普通）" onclick="openMenuHref1(3,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户价值（ARPU分档）" onclick="openMenuHref1(4,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="在网时长"    onclick="openMenuHref1(0,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="套餐类型"    onclick="openMenuHref1(5,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="受理渠道"    onclick="openMenuHref1(1,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="产品类型"    onclick="openMenuHref1(9,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>   
		</div>
		<div id="menu11" style="display:none;border:1px solid #6699cc;position:absolute;z-index:1000;background:#B7FF4A;">
		    <li><input type='button'  class="poster_btn"  value="服务人员工号"    onclick="openMenuHref1(6,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="业务类型"    onclick="openMenuHref1(8,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户群"    onclick="openMenuHref1(2,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户等级（VIP、普通）" onclick="openMenuHref1(3,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户价值（ARPU分档）" onclick="openMenuHref1(4,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="在网时长"    onclick="openMenuHref1(0,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="套餐类型"    onclick="openMenuHref1(5,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="受理渠道"    onclick="openMenuHref1(1,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li> 
		    <li><input type='button'  class="poster_btn"  value="产品类型"    onclick="openMenuHref1(9,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="终端类型"    onclick="openMenuHref1(10,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>            
		</div>
		<div id="menu12" style="display:none;border:1px solid #6699cc;position:absolute;z-index:1000;background:#B7FF4A;">
		    <li><input type='button'  class="poster_btn"  value="服务人员工号"    onclick="openMenuHref1(6,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="业务类型"    onclick="openMenuHref1(8,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="测评方式"    onclick="openMenuHref1(7,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户群"    onclick="openMenuHref1(2,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户等级（VIP、普通）" onclick="openMenuHref1(3,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户价值（ARPU分档）" onclick="openMenuHref1(4,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="在网时长"    onclick="openMenuHref1(0,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="套餐类型"    onclick="openMenuHref1(5,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="受理渠道"    onclick="openMenuHref1(1,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="产品类型"    onclick="openMenuHref1(9,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="终端类型"    onclick="openMenuHref1(10,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>             
		</div>
		<div id="menu13" style="display:none;border:1px solid #6699cc;position:absolute;z-index:1000;background:#B7FF4A;">
		    <li><input type='button'  class="poster_btn"  value="客户群"    onclick="openMenuHref1(2,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户等级（VIP、普通）" onclick="openMenuHref1(3,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="客户价值（ARPU分档）" onclick="openMenuHref1(4,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="在网时长"    onclick="openMenuHref1(0,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="套餐类型"    onclick="openMenuHref1(5,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="受理渠道"    onclick="openMenuHref1(1,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li> 
		    <li><input type='button'  class="poster_btn"  value="产品类型"    onclick="openMenuHref1(9,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="终端类型"    onclick="openMenuHref1(10,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:150px;" /></li>            
		</div>
			
		<div id="menu20" style="display:none;border:1px solid #6699cc;position:absolute;z-index:1000;background:#B7FF4A;">
		    <li><input type='button'  class="poster_btn"  value="服务人员工号"    onclick="openMenuHref2(6,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:100px;" /></li>
		</div>
		<div id="menu21" style="display:none;border:1px solid #6699cc;position:absolute;z-index:1000;background:#B7FF4A;">
		    <li><input type='button'  class="poster_btn"  value="服务人员工号"    onclick="openMenuHref2(6,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:100px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="业务类型"    onclick="openMenuHref2(8,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:100px;" /></li>
		</div>
		<div id="menu22" style="display:none;border:1px solid #6699cc;position:absolute;z-index:1000;background:#B7FF4A;">
		    <li><input type='button'  class="poster_btn"  value="服务人员工号"    onclick="openMenuHref2(6,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:100px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="业务类型"    onclick="openMenuHref2(8,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:100px;" /></li>
		    <li><input type='button'  class="poster_btn"  value="测评方式"    onclick="openMenuHref2(7,$('zoneCodeChoose').value,$('zoneNameChoose').value);"    style="width:100px;" /></li>
		</div>
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value="<%=menuName %>" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   /> 
        <input type="hidden" id="excelCondition"    name="excelCondition"/> 
        <input type="hidden" id="filePath"    name="filePath" value=<%="/upload/excelHeader/satisfiedDetails_Sum.xls"%> />
        <input type="hidden" id="row"    name="row" value="4" />

        <input type="hidden" id="clickTab"    name="clickTab" value="<%=clickTab%>"/> 
        <input type="hidden" id="zoneCodeChoose"    name="zoneCodeChoose" value="${zoneCodeChoose}"/> 
        <input type="hidden" id="zoneNameChoose"    name="zoneNameChoose" value="${zoneNameChoose}"/> 
        <input type="hidden" id="userCode"    name="userCode" value="<%=userCode %>"/> 
        <input type="hidden" id="userCodeData"    name="userCodeData" /> 
        <input type="hidden" id="explain"    name="explain"   />
        <input type="hidden" id="cid"  name="cid"   />
        <input type="hidden" id="cid1"  name="cid1"   />
        <input type="hidden" id="vTypeId"  name="vTypeId"  value="<%=vTypeId%>" />
        <input type="hidden" id="vTypeId1"  name="vTypeId1"  value="<%=vTypeId%>" />
        <input type="hidden" id="vTypeId2"  name="vTypeId2"  value="<%=vTypeId%>" />
        <input type="hidden" id="testMethod"  name="testMethod"  value="<%=testMethod%>" />
		<table style="width:300px;text-align:center">
		<tr>
			<td> 
			    <input type='button'  class="poster_btn" style="width:100px;text-align:center;background:url('<%=rootPath%>/meta/resource/images/title-bg1.jpg');color:#002200" id="tab3"  name="tab3"  value="日报"    onclick="changeTab3(this);" />
			</td>
			<td>
			    <input type='button'  class="poster_btn" style="width:100px;text-align:center;background:url('<%=rootPath%>/meta/resource/images/title-bg1.jpg');color:#002200" id="tab2"  name="tab2"  value="周报"    onclick="changeTab2(this);" />
			</td>
			<td>
			    <input type='button'  class="poster_btn" style="width:100px;text-align:center;background:url('<%=rootPath%>/meta/resource/images/title-bg1.jpg');color:#FFFFFF" id="tab1"  name="tab1"  value="月报"    onclick="changeTab1(this);" />
				
			</td>
		</tr>
	</table>  
        <input type="hidden" id="defaultZoneCode"    name="defaultZoneCode" value=<%=areaCode %>   />
        <input type="hidden" id="changeCode"    name="changeCode"/>
        <input type="hidden" id="defaultCode"    name="defaultCode"/>
	    <table width='100%'>
		  <tr>
			<td>
				<div id="info3" style="display:none;" >
				<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%' style='border: 1px solid #87CEFF;'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="850"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
           <td style="width: 180px;">日期从:
	  	       <input name="startDate" id="startDate" size="18" value="${startDate}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=dayTime %>'})" >
		   </td>
		   <td style="width: 150px;">至:
		       <input name="endDate"  id="endDate"    size="18" value="${endDate}" readonly="true" class='Wdate'   onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=dayTime %>'})" >
		   </td>
           <td style="width: 170px;">区域:
			   <input type="hidden""  id="zoneCode2"  value="<%=zoneCode%>" name="zoneCode2" />
			   <input type="text"     id="zone2"  size="17"      name="zone2" />
		   </td>
		    <%--<td style="width: 170px;">触点:
	          <select id="vTypeId2"    name="vTypeId2" style="width: 120px;"> 
	          <% for (Map<String, Object> key: vTypeList){
	          if(vTypeId.equals(key.get("V_TYPE_ID"))){
	           %>
	          <option value="<%=key.get("V_TYPE_ID")%>" selected><%=key.get("V_TYPE_NAME").toString()%></option>
	          <%}else{ %>
	         
	             <option value="<%=key.get("V_TYPE_ID")%>" ><%=key.get("V_TYPE_NAME").toString()%></option>
	          <% 
	          }
	          }%>  
	          </select>
           </td>
	       --%><td style="width:60px;"><input type='button'  class="poster_btn" id="queryBtn2"  name="queryBtn2"  value="查  询"    onclick="queryData2();"    style="width:60px;" /></td>
	     <td style="width:60px;"><input type='button'  class="poster_btn" id="impBtn2"    name="impBtn2"    value="导  出" onclick="impExcel2();" style="width:60px;" /></td>
													                </tr>
													</table>			        
										    </td>
										</tr>
							  </table>	
							      </td>
			       </tr>
                  <!-- 顶部图形 -->	     
			       <tr>
			          <td width="100%"> 			                  
							<table id='topTable2' width='100%' height='auto!important' border='0' cellpadding='0px' cellspacing='2px'>
								<tr>
									<td width='50%'>
										<table  width='100%' height='200px' style='border: 1px solid #87CEFF;' border='0' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'><span style='font:12px;font-weight:bold;' id='titleInfo12'></span>
												</td>
												<td align='right'>
												 <select id="selectCol2"    name="selectCol2" style="width: 160px;" onchange="changeCol2(this);">
										          <option value="V_SUCC_NUM" <%if("V_SUCC_NUM".equals(selectCol)){ %> selected <%} %>>测评有效样本数</option>
										          <option value="V_SUCC_NUM_LV" <%if("V_SUCC_NUM_LV".equals(selectCol)){ %> selected <%} %>>测评参与率</option>
										           <option value="SATISFY" <%if("SATISFY".equals(selectCol)){ %> selected <%} %>>测评满意数</option>
										           <option value="SATISFY_LV" <%if("SATISFY_LV".equals(selectCol)){ %> selected <%} %>>测评满意率</option>
										          </select>
												</td>
											</tr>
											<tr>
												<td colspan='2'>
													<div id='chartdiv22'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table width='100%' height='200px' border='0' style='border: 1px solid #87CEFF;' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'>
												<span style='font:12px;font-weight:bold;' id='titleInfo22'></span>
												</td>
												<td align='right'>
												<input type='button' id='city2' name='city2' class='poster_btn1' value='横向对比'  onclick="lookCity2(this)" style='width:70px;'/>
												</td>
											</tr>
											<tr>
												<td colspan='2'>
													<div id='chartdiv12'></div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
				      </td>
				   </tr>
			       <tr>
			          <td width="100%"> 
		                   <div id="chartTable2" style="margin: 0px;padding: 0px;width: 100%;height: auto;"> 
		                   </div>
		                    <div id="index_exp2"  class="tips"></div>   
                       </td>
                   </tr> 
                      
	      </table>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div id="info2" style="display:none;">
				<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%' style='border: 1px solid #87CEFF;' border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="730"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		    <td style="width: 200px;">周期:
          <select id="dateTime"    name="dateTime" style="width: 160px;"> 
          <% for (Map<String, Object> key: weekList){
          if(week.equals(key.get("DATE_NO"))){
           %>
          <option value="<%=key.get("DATE_NO")%>" selected><%=key.get("DATE_NO").toString()%></option>
          <%}else{ %>
         
             <option value="<%=key.get("DATE_NO")%>" ><%=key.get("DATE_NO").toString()%></option>
          <% 
          }
          }%>  
          </select>		</td>
          <td style="width: 170px;">区域:
		   <input type="hidden""  id="zoneCode"  value="<%=zoneCode%>" name="zoneCode" />
		   <input type="text"     id="zone"  size="17"      name="zone" /></td>
		    <%--<td style="width: 170px;">&nbsp;&nbsp;&nbsp;触点:
	          <select id="vTypeId"    name="vTypeId" style="width: 120px;"> 
	          <% for (Map<String, Object> key: vTypeList){
	          if(vTypeId.equals(key.get("V_TYPE_ID"))){
	           %>
	          <option value="<%=key.get("V_TYPE_ID")%>" selected><%=key.get("V_TYPE_NAME").toString()%></option>
	          <%}else{ %>
	         
	             <option value="<%=key.get("V_TYPE_ID")%>" ><%=key.get("V_TYPE_NAME").toString()%></option>
	          <% 
	          }
	          }%>  
	          </select>
           </td>
	       --%><td style="width:60px;"><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td style="width:60px;"><input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
													                </tr>
													</table>			        
										    </td>
										</tr>
							  </table>	
							      </td>
			       </tr>
                   <!-- 顶部图形 -->	     
			       <tr>
			          <td width="100%"> 			                  
							<table id='topTable' width='100%' height='auto!important' border='0' cellpadding='0px' cellspacing='2px'>
								<tr>
									<td width='50%'>
										<table  width='100%' height='200px' style='border: 1px solid #87CEFF;' border='0' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'><span style='font:12px;font-weight:bold;' id='titleInfo1'></span>
												</td>
												<td align='right'>
												 <select id="selectCol"    name="selectCol" style="width: 160px;" onchange="changeCol(this);">
										          <option value="V_SUCC_NUM" <%if("V_SUCC_NUM".equals(selectCol)){ %> selected <%} %>>测评有效样本数</option>
										          <option value="V_SUCC_NUM_LV" <%if("V_SUCC_NUM_LV".equals(selectCol)){ %> selected <%} %>>测评参与率</option>
										           <option value="SATISFY" <%if("SATISFY".equals(selectCol)){ %> selected <%} %>>测评满意数</option>
										           <option value="SATISFY_LV" <%if("SATISFY_LV".equals(selectCol)){ %> selected <%} %>>测评满意率</option>
										          </select>
												</td>
											</tr>
											<tr>
												<td colspan='2'>
													<div id='chartdiv2'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table width='100%' height='200px' border='0' style='border: 1px solid #87CEFF;' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'>
												<span style='font:12px;font-weight:bold;' id='titleInfo2'></span>
												</td>
												<td align='right'>
												<input type='button' id='city' name='city' class='poster_btn1' value='横向对比'  onclick="lookCity(this)" style='width:70px;'/>
												</td>
											</tr>
											<tr>
												<td colspan='2'>
													<div id='chartdiv1'></div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
				      </td>
				   </tr>
			       <tr>
			          <td width="100%"> 
		                   <div id="chartTable" style="margin: 0px;padding: 0px;width: 100%;height: auto;"> 
		                   </div>
		                    <div id="index_exp"  class="tips"></div>   
                       </td>
                   </tr> 
                      
	      </table>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<div id="info1">
				 <table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%' style='border: 1px solid #87CEFF;'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="700"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		    <td style="width: 150px;">月份:
          <select id="dateTime1"    name="dateTime1" style="width: 100px;"> 
          <% for (Map<String, Object> key: monList){
          if(month.equals(key.get("DATE_NO"))){
           %>
          <option value="<%=key.get("DATE_NO")%>" selected><%=key.get("DATE_NO").toString()%></option>
          <%}else{ %>
         
             <option value="<%=key.get("DATE_NO")%>" ><%=key.get("DATE_NO").toString()%></option>
          <% 
          }
          }%>  
          </select>		</td>
          <td style="width: 170px;">区域:
			   <input type="hidden""  id="zoneCode1"  value="<%=zoneCode%>" name="zoneCode1" />
			   <input type="text"     id="zone1"  size="17"      name="zone1" />
		   </td><%--
		    <td style="width: 170px;">&nbsp;&nbsp;&nbsp;触点:
	          <select id="vTypeId1"    name="vTypeId1" style="width: 120px;"> 
	          <% for (Map<String, Object> key: vTypeList){
	          if(vTypeId.equals(key.get("V_TYPE_ID"))){
	           %>
	          <option value="<%=key.get("V_TYPE_ID")%>" selected><%=key.get("V_TYPE_NAME").toString()%></option>
	          <%}else{ %>
	         
	             <option value="<%=key.get("V_TYPE_ID")%>" ><%=key.get("V_TYPE_NAME").toString()%></option>
	          <% 
	          }
	          }%>  
	          </select>
           </td>
	       --%><td style="width:60px;"><input type='button'  class="poster_btn" id="queryBtn1"  name="queryBtn1"  value="查  询"    onclick="queryData1();"    style="width:60px;" /></td>
	     <td style="width:60px;"><input type='button'  class="poster_btn" id="impBtn1"    name="impBtn1"    value="导  出" onclick="impExcel1();"         style="width:60px;" /></td>
													                </tr>
													</table>			        
										    </td>
										</tr>
							  </table>	
							      </td>
			       </tr>
                   <!-- 顶部图形 -->	     
			       <tr>
			          <td width="100%"> 			                  
							<table id='topTable1' width='100%' height='auto!important' border='0' cellpadding='0px' cellspacing='2px'>
								<tr>
									<td width='50%'>
										<table  width='100%' height='200px' style='border: 1px solid #87CEFF;' border='0' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'><span style='font:12px;font-weight:bold;' id='titleInfo11'></span>
												</td>
												<td align='right'>
												 <select id="selectCol1"    name="selectCol1" style="width: 160px;" onchange="changeCol1(this);">
										          <option value="V_SUCC_NUM" <%if("V_SUCC_NUM".equals(selectCol)){ %> selected <%} %>>测评有效样本数</option>
										          <option value="V_SUCC_NUM_LV" <%if("V_SUCC_NUM_LV".equals(selectCol)){ %> selected <%} %>>测评参与率</option>
										           <option value="SATISFY" <%if("SATISFY".equals(selectCol)){ %> selected <%} %>>测评满意数</option>
										           <option value="SATISFY_LV" <%if("SATISFY_LV".equals(selectCol)){ %> selected <%} %>>测评满意率</option>
										          </select>
												</td>
											</tr>
											<tr>
												<td colspan='2'>
													<div id='chartdiv21'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table width='100%' height='200px' border='0' style='border: 1px solid #87CEFF;' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'>
												<span style='font:12px;font-weight:bold;' id='titleInfo21'></span>
												</td>
												<td align='right'>
												<input type='button' id='city1' name='city1' class='poster_btn1' value='横向对比'  onclick="lookCity1(this)" style='width:70px;'/>
												</td>
											</tr>
											<tr>
												<td colspan='2'>
													<div id='chartdiv11'></div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
				      </td>
				   </tr>
			       <tr>
			          <td width="100%"> 
		                   <div id="chartTable1" style="margin: 0px;padding: 0px;width: 100%;height: auto;"> 
		                   </div>
		                    <div id="index_exp1"  class="tips"></div>   
                       </td>
                   </tr> 
                      
	      </table>
				</div>
			</td>
		</tr>
	</table>  
	      
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="satisfiedDetails_Sum_Dg.js"></script>