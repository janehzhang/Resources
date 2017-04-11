<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.util.*"%>
<%@page import="tydic.reports.channel.newChannel.NewChannelDao"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
    String clickTab="";
    String headerLevel=request.getParameter("headerLevel")==null?"1":(String)request.getParameter("headerLevel");
    String thirdType=request.getParameter("thirdType")==null?"-9":(String)request.getParameter("thirdType");
    String filePath="";
    int row=0; 
    if(headerLevel.equals("1")){
    	filePath="/upload/excelHeader/channelServerNewFirst_Day1.xls";
    	row=5;
    }else if(headerLevel.equals("2")){
    	filePath="/upload/excelHeader/channelNewSecond_Day1.xls";
    	row=5;
    }else{
    	if(thirdType.equals("0")){//办理
    		filePath="/upload/excelHeader/channelNewThrid_BanLiDay1.xls";
        }else if(thirdType.equals("1")){//查询
        	filePath="/upload/excelHeader/channelNewThrid_QueryDay1.xls";
        }else if(thirdType.equals("2")){//咨询
        	filePath="/upload/excelHeader/channelNewThrid_ZixunDay1.xls";
        }else{//充值交费
        	filePath="/upload/excelHeader/channelNewThrid_chongjiaoDay1.xls";
         }
    	row=5;
    }
    //周
    NewChannelDao dao =new NewChannelDao();
	String week = request.getParameter("dateTime");
	if(week==null || week.equals("")){
		week=dao.getMaxDate_ChannelServ("1","CS_CHANNEL_VIEW_NEW_LOC");
        week=week.replace("_","~");
	}else{
		 clickTab="tab2";
	}
	List<Map<String, Object>> weekList=dao.getWeekList2("CS_CHANNEL_VIEW_NEW_LOC");
	request.setAttribute("dateTime",week);
	//月
	String month = request.getParameter("dateTime1");
	if(month==null || month.equals("")){
		month=dao.getMaxDate_ChannelServ("2","CS_CHANNEL_VIEW_NEW_LOC");
	}else{
		    clickTab="tab1";
	}
	List<Map<String, Object>> monList=dao.getSelectMon2("CS_CHANNEL_VIEW_NEW_LOC");
	request.setAttribute("dateTime1",month);
	  
	  //日
	String startTime =(String)request.getParameter("startDate");
	String endTime =(String)request.getParameter("endDate");
	String dayTime="";
	if(startTime==null || startTime.equals("")){
		dayTime=dao.getMaxDate_ChannelServ("0","CS_CHANNEL_VIEW_NEW_LOC");
		String year3=dayTime.substring(0,4);
    	String month3=dayTime.substring(4,6);
    	String day3=dayTime.substring(6,8);
    	startTime=year3+"-"+month3+"-"+day3;
		endTime=startTime;
			dayTime=startTime;
		
		
	}else{
	    clickTab="tab3";
	}
	request.setAttribute("startDate",startTime);
	request.setAttribute("endDate",endTime);
	  
    String areaCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
    String zoneCode = request.getParameter("zoneCode")==null?"0000":(String)request.getParameter("zoneCode");
	String channelTypeCode = request.getParameter("channelTypeCode")==null?"1":(String)request.getParameter("channelTypeCode");
	String channelType = request.getParameter("channelType")==null?"所有类型":(String)request.getParameter("channelType");
	String selectCol = request.getParameter("selectCol")==null?"SERVICE_NUM":(String)request.getParameter("selectCol");
%>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/NewTwoChannelAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ChannelTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/channelTypeTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
    //日
	  function impExcel2(form){
		dhx.showProgress("正在执行......");
		window.setTimeout("exportExcel2()",8000);
	    window.setTimeout("dhx.closeProgress()",8000);
       }
      //周
	  function impExcel(form){
			dhx.showProgress("正在执行......");
			window.setTimeout("exportExcel()",8000);
		    window.setTimeout("dhx.closeProgress()",8000);
	   }
	   //月
	  function impExcel1(form){
			dhx.showProgress("正在执行......");
			window.setTimeout("exportExcel1()",8000);
		    window.setTimeout("dhx.closeProgress()",8000);
	}   
	</script>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	  <div id="div_src" style="position: absolute; width: 100%; height:100%;z-index: 1000;top: 0px; left: 0px; background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:scroll;"
	                  title="" class="dragBar">
	  <center style="color: #CCCCCC;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;">数据加载中，请稍等....</center>
     </div> 
	 <form id="queryform" name="queryform">
	    <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
	    <input type="hidden" id="excelHeader"   name="excelHeader" />
        <input type="hidden" id="excelData"    name="excelData"   />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="filePath"    name="filePath" value="<%=filePath %>" />
        <input type="hidden" id="row"    name="row" value="<%=row %>" />
        <input type="hidden" id="clickTab"    name="clickTab" value="<%=clickTab%>"/> 
        <input type="hidden" id="headerLevel"    name="headerLevel" value="<%=headerLevel%>"/>
        <input type="hidden" id="thirdType"    name="thirdType" value="<%=thirdType%>"/>
        <table styleporte="width:300px;text-align:center">
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
	<table width='100%' >
   <!-- 日报 -->
   <tr>
	<td>
	<div id="info3" style="display:none;" >
	<table width='100%' >
	<tr>
	 <td valign="top" width="100%">
		<table width='100%' style='border: 1px solid #87CEFF;'  border='0'  cellpadding='0px' cellspacing='0px'>
		 <tr>
			<td>
				<table  width="840"  border="0" cellpadding="0" cellspacing="0">
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
		   <td style="width: 170px;">渠道类型:
			   <input type="hidden""  id="channelTypeCode2"  value="<%=channelTypeCode%>" name="channelTypeCode2" />
			   <input type="text"     id="channelType2"  size="17"      name="zone2" />
		   </td>
		   <td style="width:60px;"><input type='button'  class="poster_btn" id="queryBtn2"  name="queryBtn2"  value="查  询"    onclick="queryData2();"    style="width:60px;" /></td>
	     <td style="width:60px;"><input type='button'  class="poster_btn" id="impBtn2"    name="impBtn2"    value="导  出" onclick="impExcel2();" style="width:60px;" /></td>
		</tr>
		   </table>			        
		  </td>
		</tr>
	 </table>	
	</td>
	</tr>
   <!-- 表格数据 -->	     
	<tr>
	  <td width="100%"> 
		<div id="chartTable2" style="margin: 0px;padding: 0px;width: 100%;height: auto;"> 
		</div> 
        </td>
    </tr>                  
	 </table>
      </div>
     </td>
     </tr>
       <!-- 周报 -->
   <tr>
	<td>
	<div id="info2" style="display:none;" >
	<table width='100%' >
	<tr>
	 <td valign="top" width="100%">
		<table width='100%' style='border: 1px solid #87CEFF;'  border='0'  cellpadding='0px' cellspacing='0px'>
		 <tr>
		  <td>
		   <table  width="700"  border="0" cellpadding="0" cellspacing="0">
		<tr>
         <td style="width: 200px;">周期:
          <select id="dateTime"    name="dateTime" style="width: 160px;"> 
          <% for (Map<String, Object> key: weekList){
          if(week.equals(key.get("DATE_NO".replaceAll("_","~")))){
           %>
          <option value="<%=key.get("DATE_NO").toString().replaceAll("_","~")%>" selected><%=key.get("DATE_NO").toString().replaceAll("_","~")%></option>
          <%}else{ %>
         
             <option value="<%=key.get("DATE_NO").toString().replaceAll("_","~")%>" ><%=key.get("DATE_NO").toString().replaceAll("_","~")%></option>
          <% 
          }
          }%>  
          </select>		</td>
           <td style="width: 170px;">区域:
			   <input type="hidden""  id="zoneCode"  value="<%=zoneCode%>" name="zoneCode" />
			   <input type="text"     id="zone"  size="17"      name="zone" />
		   </td>
		   <td style="width: 170px;">渠道类型:
			   <input type="hidden""  id="channelTypeCode"  value="<%=channelTypeCode%>" name="channelTypeCode" />
			   <input type="text"     id="channelType"  size="17"      name="zone" />
		   </td>
		   <td style="width:60px;"><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td style="width:60px;"><input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();" style="width:60px;" /></td>
		</tr>
		   </table>			        
		  </td>
		</tr>
	 </table>	
	</td>
	</tr>
   <!-- 表格数据 -->	     
	<tr>
	  <td width="100%"> 
		<div id="chartTable" style="margin: 0px;padding: 0px;width: 100%;height: auto;"> 
		</div> 
        </td>
    </tr>                  
	 </table>
      </div>
     </td>
     </tr>
   <!-- 月报 -->
   <tr>
	<td>
	<div id="info1" style="display:none;" >
	<table width='100%' >
	<tr>
	 <td valign="top" width="100%">
		<table width='100%' style='border: 1px solid #87CEFF;'  border='0'  cellpadding='0px' cellspacing='0px'>
		 <tr>
		  <td>
		   <table  width="650"  border="0" cellpadding="0" cellspacing="0">
		<tr>
         <td style="width: 150px;">月份:
          <select id="dateTime1"    name="dateTime1" style="width: 100px;"> 
          <% for (Map<String, Object> key: monList){
          if(month.equals(key.get("MONTH_ID"))){
           %>
          <option value="<%=key.get("MONTH_ID")%>" selected><%=key.get("MONTH_ID").toString()%></option>
          <%}else{ %>
         
             <option value="<%=key.get("MONTH_ID")%>" ><%=key.get("MONTH_ID").toString()%></option>
          <% 
          }
          }%>  
          </select>		
         </td>
           <td style="width: 170px;">区域:
			   <input type="hidden""  id="zoneCode1"  value="<%=zoneCode%>" name="zoneCode1" />
			   <input type="text"     id="zone1"  size="17"      name="zone1" />
		   </td>
		   <td style="width: 170px;">渠道类型:
			   <input type="hidden""  id="channelTypeCode1"  value="<%=channelTypeCode%>" name="channelTypeCode1" />
			   <input type="text"     id="channelType1"  size="17"      name="zone1" />
		   </td>
		   <td style="width:60px;"><input type='button'  class="poster_btn" id="queryBtn1"  name="queryBtn1"  value="查  询"    onclick="queryData1();"    style="width:60px;" /></td>
	     <td style="width:60px;"><input type='button'  class="poster_btn" id="impBtn1"    name="impBtn1"    value="导  出" onclick="impExcel1();" style="width:60px;" /></td>
		</tr>
		   </table>			        
		  </td>
		</tr>
	 </table>	
	</td>
	</tr>
   <!-- 表格数据 -->	     
	<tr>
	  <td width="100%"> 
		<div id="chartTable1" style="margin: 0px;padding: 0px;width: 100%;height: auto;"> 
		</div> 
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
<script type="text/javascript" src="channelServerOtherSum.js"></script>