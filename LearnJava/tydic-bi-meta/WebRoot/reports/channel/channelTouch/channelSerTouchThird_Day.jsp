<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.channel.allBusiness.AllChannelDAO"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
    AllChannelDAO dao=new AllChannelDAO();
    String dateTime = request.getParameter("dateTime"); 
    String actType = request.getParameter("actType");//渠道分类类型：1传统新媒体渠道，2其他渠道，3人工自助渠道
    String thirdType = request.getParameter("thirdType");//0：办理  1：查询
    //List<Map<String,Object>>typeList=dao.getChannelTypeList(actType);
	String startDate = request.getParameter("startDate"); 
    String endDate = request.getParameter("endDate"); 
	if(startDate==null || startDate.equals("")){
		startDate=dao.getMaxDate_ChannelServ(actType,"0","CS_CHANNEL_VIEW");
		String year=startDate.substring(0,4);
    	String month=startDate.substring(4,6);
    	String day=startDate.substring(6,8);
    	startDate=year+"-"+month+"-"+day;
    	endDate=startDate;
	}  
	request.setAttribute("startDate", startDate);
    request.setAttribute("endDate", endDate);
    String userCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
    String zoneCode = request.getParameter("zoneCode")==null?"0000":(String)request.getParameter("zoneCode");
	String channelTypeCode = request.getParameter("channelTypeCode")==null?"1":(String)request.getParameter("channelTypeCode");
	String channelType = request.getParameter("channelType")==null?"所有类型":(String)request.getParameter("channelType");
	String tempStr="";
	String excPath="";
	if("0".equals(thirdType)||thirdType=="0"){
		tempStr="DEAL_NUM";
		excPath="/upload/excelHeader/allBusinessThird_Day_New.xls";
	}else{
		tempStr="QUERY_NUM";
		excPath="/upload/excelHeader/allBusinessThird_Day_New1.xls";
	}
	String selectCol = request.getParameter("selectCol")==null?tempStr:(String)request.getParameter("selectCol");
%>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/AllChannelAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplIndexAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ChannelTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/channelTypeTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
	    function impExcel(form){
		dhx.showProgress("正在执行......");
		exportImage(); 
		window.setTimeout("exportExcel()",8000);
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
	 	<input type="hidden" id="actType"    name="actType" value=<%=actType %> />
	 	<input type="hidden" id="thirdType"    name="thirdType" value=<%=thirdType %> />
	    <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
	    <input type="hidden" id="excelHeader"   name="excelHeader" />
        <input type="hidden" id="excelData"    name="excelData"   />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="explain"    name="explain"   />
        <input type="hidden" id="cid"  name="cid"   />
        <input type="hidden" id="cid1"  name="cid1"   />
        <input type="hidden" id="filePath"    name="filePath" value=<%=excPath %> />
        <input type="hidden" id="row"    name="row" value="5" />
        <input type="hidden" id="userCode"    name="userCode" value="<%=userCode %>"/> 
        <input type="hidden" id="userCodeData"    name="userCodeData" />
				<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%'  border='0'  style='border: 1px solid #87CEFF;'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="1050"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
           <td style="width: 180px;">&nbsp;&nbsp;日期从:
	  	       <input name="startDate" id="startDate" size="18" value="${startDate}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=startDate %>'})" >
		   </td>
		   <td style="width: 180px;">&nbsp;&nbsp;至:
		       <input name="endDate"  id="endDate"    size="18" value="${endDate}" readonly="true" class='Wdate'   onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=endDate %>'})" >
		   &nbsp;&nbsp;</td>
           <td>区域:
           <input type="hidden""  id="zoneCode" value=<%=zoneCode%> name="zoneCode" />
		   <input type="text"     id="zone"  size="17"      name="zone" />
		   <td>渠道类型:
		   <input type="hidden""  id="channelTypeCode"  value=<%=channelTypeCode%>   name="channelTypeCode" />
		   <input type="text"     id="channelType"  size="20"    value=<%=channelType%>  name="channelType" />
	       <td><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td>&nbsp;&nbsp;&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>

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
												<%--<td align='right'>类型:
          <select id="typeList"    name="typeList" style="width: 120px;" onchange="changeCol(this);">
           <% for(Map<String, Object> key:typeList ){  
            if(false){%>
           <option value="<%=key.get("CHANNEL_TYPE_ID") %>" selected><%=key.get("CHANNEL_TYPE_NAME").toString() %></option>
               <%  }else{%>
               <option value="<%=key.get("CHANNEL_TYPE_ID")%>"><%=key.get("CHANNEL_TYPE_NAME").toString() %></option>
            	   <% }
            	   }%>  
          </select>		</td>
												 
											--%></tr>
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
												
												<td align='left'>
												  <%if ("0".equals(thirdType)||thirdType=="0"){ %>
												 <select id="selectCol"    name="selectCol" style="width: 180px;" onchange="changeCol(this);">       
										           <option value="DEAL_NUM11" <%if("DEAL_NUM11".equals(selectCol)){ %> selected <%} %>> 办理_增值业务_订购</option>
										           <option value="DEAL_NUM13" <%if("DEAL_NUM13".equals(selectCol)){ %> selected <%} %>> 办理_增值业务_变更</option>
										           <option value="DEAL_NUM12" <%if("DEAL_NUM12".equals(selectCol)){ %> selected <%} %>> 办理_增值业务_注销</option>
										           <option value="DEAL_NUM14" <%if("DEAL_NUM14".equals(selectCol)){ %> selected <%} %>> 办理_增值业务_其他</option>
										            
										           <option value="DEAL_NUM21" <%if("DEAL_NUM21".equals(selectCol)){ %> selected <%} %>> 办理_套餐销售_订购</option>
										           <option value="DEAL_NUM23" <%if("DEAL_NUM23".equals(selectCol)){ %> selected <%} %>> 办理_套餐销售_换购机</option>
										           <option value="DEAL_NUM22" <%if("DEAL_NUM22".equals(selectCol)){ %> selected <%} %>> 办理_套餐销售_注销</option>
										           <option value="DEAL_NUM24" <%if("DEAL_NUM24".equals(selectCol)){ %> selected <%} %>> 办理_套餐销售_预受理</option>
										           <option value="DEAL_NUM25" <%if("DEAL_NUM25".equals(selectCol)){ %> selected <%} %>> 办理_套餐销售_其他</option>

										           <option value="DEAL_NUM31" <%if("DEAL_NUM31".equals(selectCol)){ %> selected <%} %>> 办理_套餐变更</option>

										           <option value="DEAL_NUM41" <%if("DEAL_NUM41".equals(selectCol)){ %> selected <%} %>> 办理_基础服务_补换卡</option>
										           <option value="DEAL_NUM42" <%if("DEAL_NUM42".equals(selectCol)){ %> selected <%} %>> 办理_基础服务_积分兑换</option>
										           <option value="DEAL_NUM43" <%if("DEAL_NUM43".equals(selectCol)){ %> selected <%} %>> 办理_基础服务_密码操作</option>
										           <option value="DEAL_NUM44" <%if("DEAL_NUM44".equals(selectCol)){ %> selected <%} %>> 办理_基础服务_实名登记</option>
										           <option value="DEAL_NUM45" <%if("DEAL_NUM45".equals(selectCol)){ %> selected <%} %>> 办理_基础服务_停复机</option>
										           <option value="DEAL_NUM46" <%if("DEAL_NUM46".equals(selectCol)){ %> selected <%} %>> 办理_基础服务_信息变更</option>
										           <option value="DEAL_NUM47" <%if("DEAL_NUM47".equals(selectCol)){ %> selected <%} %>> 办理_基础服务_信用额度调整</option>
										           <option value="DEAL_NUM48" <%if("DEAL_NUM48".equals(selectCol)){ %> selected <%} %>> 办理_基础服务_业务变更</option>
										           <option value="DEAL_NUM49" <%if("DEAL_NUM49".equals(selectCol)){ %> selected <%} %>> 办理_基础服务_移机</option>
										           <option value="DEAL_NUM410" <%if("DEAL_NUM410".equals(selectCol)){ %> selected <%} %>> 办理_基础服务_其它</option>

										           <option value="DEAL_NUM" <%if("DEAL_NUM".equals(selectCol)){ %> selected <%} %>>总服务量</option>
										          </select>
										          <%}else{ %>
										          <select id="selectCol"    name="selectCol" style="width: 180px;" onchange="changeCol(this);">       
										           <option value="QUERY_NUM11" <%if("QUERY_NUM11".equals(selectCol)){ %> selected <%} %>> 查询_费用查询_话费查询</option>
										           <option value="QUERY_NUM12" <%if("QUERY_NUM12".equals(selectCol)){ %> selected <%} %>> 查询_费用查询_话费明细</option>
										           <option value="QUERY_NUM13" <%if("QUERY_NUM13".equals(selectCol)){ %> selected <%} %>> 查询_费用查询_扣费退费</option>
										           <option value="QUERY_NUM14" <%if("QUERY_NUM14".equals(selectCol)){ %> selected <%} %>> 查询_费用查询_套餐使用情况</option>
										           <option value="QUERY_NUM15" <%if("QUERY_NUM15".equals(selectCol)){ %> selected <%} %>> 查询_费用查询_余额/赠金</option>
										           <option value="QUERY_NUM16" <%if("QUERY_NUM16".equals(selectCol)){ %> selected <%} %>> 查询_费用查询_其它</option>
										            
										           <option value="QUERY_NUM21" <%if("QUERY_NUM21".equals(selectCol)){ %> selected <%} %>> 查询_进度查询_受理进度</option>
										           <option value="QUERY_NUM22" <%if("QUERY_NUM22".equals(selectCol)){ %> selected <%} %>> 查询_进度查询_报障进度</option>
										           <option value="QUERY_NUM23" <%if("QUERY_NUM23".equals(selectCol)){ %> selected <%} %>> 查询_进度查询_投诉进度</option>
										           <option value="QUERY_NUM24" <%if("QUERY_NUM24".equals(selectCol)){ %> selected <%} %>> 查询_进度查询_咨询进度</option>
										           <option value="QUERY_NUM25" <%if("QUERY_NUM25".equals(selectCol)){ %> selected <%} %>> 查询_进度查询_其它</option>

										           <option value="QUERY_NUM31" <%if("QUERY_NUM31".equals(selectCol)){ %> selected <%} %>> 查询_积分查询_积分兑换查询</option>
										           <option value="QUERY_NUM31" <%if("QUERY_NUM32".equals(selectCol)){ %> selected <%} %>> 查询_积分查询_客户积分</option>
										           <option value="QUERY_NUM31" <%if("QUERY_NUM33".equals(selectCol)){ %> selected <%} %>> 查询_积分查询_其它</option>

										           <option value="QUERY_NUM41" <%if("QUERY_NUM41".equals(selectCol)){ %> selected <%} %>> 查询_信息查询_PUK/PIN码</option>
										           <option value="QUERY_NUM42" <%if("QUERY_NUM42".equals(selectCol)){ %> selected <%} %>> 查询_信息查询_客户信息</option>
										           <option value="QUERY_NUM43" <%if("QUERY_NUM43".equals(selectCol)){ %> selected <%} %>> 查询_信息查询_客户状态</option>
										           <option value="QUERY_NUM44" <%if("QUERY_NUM44".equals(selectCol)){ %> selected <%} %>> 查询_信息查询_业务信息</option>
										           <option value="QUERY_NUM45" <%if("QUERY_NUM45".equals(selectCol)){ %> selected <%} %>> 查询_信息查询_其它</option>
										           
										           <option value="QUERY_NUM51" <%if("QUERY_NUM51".equals(selectCol)){ %> selected <%} %>> 查询_常用查询_WLAN热点</option>
										           <option value="QUERY_NUM52" <%if("QUERY_NUM52".equals(selectCol)){ %> selected <%} %>> 查询_常用查询_号码归属地</option>
										           <option value="QUERY_NUM53" <%if("QUERY_NUM53".equals(selectCol)){ %> selected <%} %>> 查询_常用查询_客户经理</option>
										           <option value="QUERY_NUM54" <%if("QUERY_NUM54".equals(selectCol)){ %> selected <%} %>> 查询_常用查询_区号</option>
										           <option value="QUERY_NUM55" <%if("QUERY_NUM55".equals(selectCol)){ %> selected <%} %>> 查询_常用查询_营业厅</option>
										           <option value="QUERY_NUM56" <%if("QUERY_NUM56".equals(selectCol)){ %> selected <%} %>> 查询_常用查询_其它</option>

										           <option value="QUERY_NUM" <%if("QUERY_NUM".equals(selectCol)){ %> selected <%} %>>总服务量</option>
										          </select>
										          <%} %>
												</td> 
												<td nowrap align='right' class='title_ma1'>
												<span style='font:12px;font-weight:bold;' id='titleInfo2'></span>
												<input type='button' id='channel' name='channel' class='poster_btn1' value='渠道小类'  onclick="showAllChannel(this)" style='width:70px;'/>
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
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="channelSerTouchThird_Day.js"></script>