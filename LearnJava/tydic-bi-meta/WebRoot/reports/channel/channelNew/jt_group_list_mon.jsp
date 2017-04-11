<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,java.util.Calendar,tydic.meta.common.DateUtil,tydic.reports.channel.newChannel.NewChannelDao"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <%@include file="../../../portalCommon/public/head.jsp" %>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <meta http-equiv="pragma" content="no-cache" />
    <script type="text/javascript" src="openLayer.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/NewTwoChannelAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/AllChannelAction.js"></script>
    <script type="text/javascript" src="<%=path%>/js/Charts/FusionCharts.js"></script> 
    <script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <%
  	 String dateTime=(String)request.getParameter("dateTime");
   	 NewChannelDao dao=new NewChannelDao();
	 List<Map<String,Object>> monList=dao.getGroupMonthList_NEW("S_MONTH_ID","CS_CHANNEL_VIEW_NEW_LOC_GROUP");
	 if(dateTime==null || dateTime.equals("") ){
      	dateTime=dao.getMaxDate_ChannelServ_new("S_MONTH_ID","CS_CHANNEL_VIEW_NEW_LOC_GROUP");
    }	 
  	  
  	  
    %>
</head>
<body style="overflow: visible;">
   <form id="queryform" name="queryform">
        <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />
         <input type="hidden" id="excelCondition"    name="excelCondition"     />
          <input type="hidden" id="row"    name="row" value="6" />
          <input type="hidden" id="explain"    name="explain"   />
          <input type="hidden" id="totalCount"   name="totalCount"    /> 
        
	    <table width='100%'  border='0' cellpadding='0px' cellspacing='0px'>
					<tr>
						<td valign="top" width="100%">
							   <table style='border: 1px solid #87CEFF;' width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
									   <tr height='25px' style='background:url(<%=path%>/images/fpage_04.gif);'>
											 <td nowrap align='left' class='title_ma1'>
												查询条件                                  
											 </td>
										</tr>
										<tr>
										     <td>
													<table id='queryCondition' width="30%"  border="0" cellpadding="0" cellspacing="0">
											                <tr>
											                  <td>&nbsp;&nbsp;&nbsp;月份:
						         								 <select id="dateTime" name="dateTime" style="width: 80px;">
						          							 	<% for(Map<String, Object> key:monList ){  
						            							if(dateTime.equals(key.get("MONTH_ID").toString())){%>
						           								<option value="<%=key.get("MONTH_ID") %>" selected><%=key.get("MONTH_ID").toString() %></option>
						              							<%  }else{%>
						               							<option value="<%=key.get("MONTH_ID")%>"><%=key.get("MONTH_ID").toString() %></option>
						            	   						<% }
						            	   						}%>  
						         								 </select>
					          								</td>
					      							 <td>&nbsp;<input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
														<td >&nbsp;
											                     <input type="button"   class="poster_btn" onclick="impExcel(this.form);"       style="width:60px;"  value="导 出" />
											                  </td>											                
											                  </tr>
													</table>			        
										    </td>
										</tr>
							  </table>	
                              <div id="dataTable" style="margin: 0px;padding: 0px;width: 100%;height: auto;">
                             
                              </div> 
                              <center>
	                              <div id="pageDiv"   style="margin: 0px;padding: 0px;width: 100%;height: auto;"
	                               align="left" ></div>   
	                          </center>       
                          </td>
			       </tr>
	      </table>
    </form>
    <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript">
	function impExcel(form){
				var totalCount=$("totalCount").value;

				var  dateTime=$("dateTime").value;
				var  queryCond="月份："+dateTime;
		 	    $("excelCondition").value=queryCond;   
	     	    dhx.showProgress("正在执行......");
	     	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel_rptChannelJtList.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	}
</script>
<script type="text/javascript" src="jt_group_list_monj.js"></script>