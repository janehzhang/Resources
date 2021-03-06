<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date,tydic.reports.channel.allBusiness.AllChannelDAO" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
    String dateTime=(String)request.getParameter("dateTime");	
    String actType = request.getParameter("actType");//渠道分类类型
    AllChannelDAO dao=new AllChannelDAO();
    //List<Map<String,Object>>typeList=dao.getChannelTypeList(actType);
    List<Map<String,Object>>monList=dao.getDateTimeList_ChannelServ(actType,"2","CS_CHANNEL_VIEW");
    if(dateTime==null || dateTime.equals("") ){
      dateTime=dao.getMaxDate_ChannelServ(actType,"2","CS_CHANNEL_VIEW");
    }
    String areaCode="0000";
	String zoneCode = request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
	String channelTypeCode = request.getParameter("channelTypeCode")==null?"1":(String)request.getParameter("channelTypeCode");
	String channelType = request.getParameter("channelType")==null?"所有类型":(String)request.getParameter("channelType");
	String selectCol = request.getParameter("selectCol")==null?"SERVICE_NUM":(String)request.getParameter("selectCol");
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
	     <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
	    <input type="hidden" id="excelHeader"   name="excelHeader" />
        <input type="hidden" id="excelData"    name="excelData"   />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="cid"  name="cid"   />
        <input type="hidden" id="cid1"  name="cid1"   />
        <input type="hidden" id="explain"    name="explain"   />
        <input type="hidden" id="filePath"    name="filePath" value="/upload/excelHeader/allBusinessSecond_Day_New.xls" />
          <input type="hidden" id="row"    name="row" value="4" />    
		<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%'  border='0'  style='border: 1px solid #87CEFF;'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="1050"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
													                 <td>&nbsp;&nbsp;&nbsp;月份:
          <select id="dateTime"    name="dateTime" style="width: 80px;">
           <% for(Map<String, Object> key:monList ){  
            if(dateTime.equals(key.get("DATE_NO").toString())){%>
           <option value="<%=key.get("DATE_NO") %>" selected><%=key.get("DATE_NO").toString() %></option>
               <%  }else{%>
               <option value="<%=key.get("DATE_NO")%>"><%=key.get("DATE_NO").toString() %></option>
            	   <% }
            	   }%>  
          </select>		</td>
           <td>区域:
		   <input type="hidden""  id="zoneCode" value=<%=zoneCode%> name="zoneCode" />
		   <input type="text"     id="zone"  size="18"      name="zone" />
		   <td>渠道类型:
		   <input type="hidden""  id="channelTypeCode"  value=<%=channelTypeCode%>  name="channelTypeCode" />
		   <input type="text"     id="channelType"  size="20"  value=<%=channelType%>    name="channelType" />
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
								                   <select id="selectCol"    name="selectCol" style="width: 130px;" onchange="changeCol(this);">
										           <option value="QUERY_NUM1" <%if("QUERY_NUM1".equals(selectCol)){ %> selected <%} %>>查询_费用查询</option>
										           <option value="QUERY_NUM2" <%if("QUERY_NUM2".equals(selectCol)){ %> selected <%} %>>查询_进度查询</option>
										           <option value="QUERY_NUM3" <%if("QUERY_NUM3".equals(selectCol)){ %> selected <%} %>>查询_积分查询</option>
										           <option value="QUERY_NUM4" <%if("QUERY_NUM4".equals(selectCol)){ %> selected <%} %>>查询_信息查询</option>
										           <option value="QUERY_NUM5" <%if("QUERY_NUM5".equals(selectCol)){ %> selected <%} %>>查询_常用查询</option>  
										           <option value="QUERY_NUM" <%if("QUERY_NUM".equals(selectCol)){ %> selected <%} %>>查询总量</option>        
										           <option value="DEAL_NUM1" <%if("DEAL_NUM1".equals(selectCol)){ %> selected <%} %>> 办理_增值业务</option>
										           <option value="DEAL_NUM2" <%if("DEAL_NUM2".equals(selectCol)){ %> selected <%} %>> 办理_套餐销售</option>
										           <option value="DEAL_NUM3" <%if("DEAL_NUM3".equals(selectCol)){ %> selected <%} %>> 办理_套餐变更</option>
										           <option value="DEAL_NUM4" <%if("DEAL_NUM4".equals(selectCol)){ %> selected <%} %>> 办理_基础服务</option>
										           <option value="DEAL_NUM" <%if("DEAL_NUM".equals(selectCol)){ %> selected <%} %>> 办理总量</option>
										           <option value="SERVICE_NUM" <%if("SERVICE_NUM".equals(selectCol)){ %> selected <%} %>>总服务量</option><%--
										           <option value="PERC_NUM" <%if("PERC_NUM".equals(selectCol)){ %> selected <%} %>>占比</option>
										          --%>
										          </select>
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
<script type="text/javascript" src="channelSerTouchSecond_Mon.js"></script>