<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,java.util.Calendar,tydic.meta.common.DateUtil"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title>投诉类指标日监测清单报表</title>
    <meta http-equiv="pragma" content="no-cache" />
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <script type="text/javascript" src="openLayer.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=path%>/js/Charts/FusionCharts.js"></script> 
    <script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <%
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      Date Nowdate=new Date(); 
      String  monthFirstDay=formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),1));//本月的第一天
      String  monthEndNextDay=formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),Nowdate.getDate()-1)); //本月当前天的前一天
    %>
     <script type="text/javascript">
		function impExcel(){
	     	    var url = getBasePath()+"/reports/complain/monitorDay/impExcel/cmplIndexListDay_impExcel.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	    }   
	</script>
</head>
<body style="overflow: visible;">
   <form id="queryform" name="queryform">
	    <table width='100%'  border='0' cellpadding='0px' cellspacing='0px'>
					<tr>
						<td valign="top" width="100%">
							   <table style='border: 1px solid #87CEFF;' width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="1000"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
													                  <td width="9%">日期从::</td>
													                  <td width="15%">
													                      <input     id="startTime" name="startTime"  value="<%=monthEndNextDay %>"  readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
													                  </td>
													                  <td width="9%">至:</td>
													                  <td width="15%">
													                      <input     id="endTime"   name="endTime"    value="<%=monthEndNextDay %>" readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
													                  </td>
													                  <td width="5%">区 域:</td>
													                  <td width="15%">
																		  <input type="hidden"  id="zoneCode"       name="zoneCode" />
																		  <input type="text"    id="zone"           name="zone"  style="width: 160px;height:18px;line-height:18px;" readonly="readonly"/>
													                  </td>
													                  <td nowrap width="7%">
													                     <input type="button"  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"     onclick="queryData();"        style="width:60px;" />
													                  </td>
													                  <td width="33%">
													                     <input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出"     onclick="impExcel();"         style="width:60px;" />
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
</script>
<script type="text/javascript" src="cmplIndexList_Day.js"></script>