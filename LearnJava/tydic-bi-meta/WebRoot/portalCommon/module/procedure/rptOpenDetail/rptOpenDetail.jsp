<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,java.util.Calendar,tydic.meta.common.DateUtil"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <%@include file="../../../public/head.jsp" %>
    <%@include file="../../../public/include.jsp" %>
    <title><%=menuName %></title>
    <meta http-equiv="pragma" content="no-cache" />
    <script type="text/javascript" src="openLayer.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/RptOpenDetailAction.js"></script>
    <script type="text/javascript" src="<%=path%>/js/Charts/FusionCharts.js"></script> 
    <script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <%
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      Date Nowdate=new Date(); 
      String  monthFirstDay=formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),1));//本月的第一天
      String  monthEndNextDay=formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),Nowdate.getDate()-1)); //本月当前天的前一天
    %>
</head>
<body style="overflow: visible;">
   <form id="queryform" name="queryform">
        <input type="hidden" id="excelTitle"   name="excelTitle"    value="<%=menuName %>" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"     />
        <input type="hidden" id="totalCount"   name="totalCount"    />     
        <input type="hidden" id="excelCondition"    name="excelCondition"/>
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
													<table id='queryCondition' width="1000"  border="0" cellpadding="0" cellspacing="0">
											                <tr>
											                  <td width="7%">归档日期:</td>
											                  <td width="15%">
											                      <input     id="startTime" name="startTime"  value="<%=monthEndNextDay %>"  readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
											                  </td>
											                  <td width="2%">至</td>
											                  <td width="15%">
											                      <input     id="endTime"   name="endTime"    value="<%=monthEndNextDay %>" readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
											                  </td>
											                  <td width="5%">区 域:</td>
											                  <td width="15%">
																  <input type="hidden"  id="zoneCode"       name="zoneCode" />
																  <input type="text"    id="zone"           name="zone"  style="width: 160px;height:18px;line-height:18px;" readonly="readonly"/>
											                  </td>
											                  <td nowrap width="7%">
											                    <input type="button"   class="poster_btn"  id="queryBtn"  name="queryBtn"   onclick="queryData();" style="width:60px;"   value="查  询" />
											                  </td>
											                  <td width="33%">
											                     <input type="button"   class="poster_btn"                     onclick="impExcel(this.form);"       style="width:60px;"  value="导 出" />
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
				if(totalCount>32000){
					alert("数记录数大于32000,无法导出")
					return;
				}	
				var  startTime=$("startTime").value;
				var  endTime=$("endTime").value;
		  	    var  zone=$("zone").value;//文本框
			    var  queryCond="归档日期："+startTime+"     至："+endTime+"     区域:"+zone;
		 	    $("excelCondition").value=queryCond;   
		        //alert($('queryCondition').innerHTML);
	     	    //var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel.jsp";
	     	    dhx.showProgress("正在执行......");
	     	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel_rptOpenDetail.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	}
</script>
<script type="text/javascript" src="rptOpenDetail.js"></script>