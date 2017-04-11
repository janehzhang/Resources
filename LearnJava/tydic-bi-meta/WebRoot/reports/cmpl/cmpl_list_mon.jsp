<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.ReportsMonDAO" %>
<%@ page import="tydic.portalCommon.DateUtil,java.util.*,java.text.SimpleDateFormat,java.util.Calendar" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<% 
   String startDate  =(String)request.getParameter("startDate");
   String endDate  =(String)request.getParameter("endDate");
   String areaCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
   String zoneCode          =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");	
   String prodTypeCode      =(String)request.getParameter("prodTypeCode")==null?"-1":(String)request.getParameter("prodTypeCode");//投诉产品类型
   String cmplBusiTypeCode  =(String)request.getParameter("cmplBusiTypeCode")==null?"1":(String)request.getParameter("cmplBusiTypeCode");//投诉业务类型	
   String indId          =(String)request.getParameter("indId")==null?"3":(String)request.getParameter("indId");//投诉指标类型
   String channelType    =request.getParameter("channelType")==null?"-1":request.getParameter("channelType");//投诉渠道
   String reasonId    =request.getParameter("reasonId")==null?"-1":request.getParameter("reasonId");//投诉渠道
   String ripId    =request.getParameter("ripId")==null?"-1":request.getParameter("ripId");
   

   if(startDate ==null ||"".equals(startDate)||endDate ==null || "".equals(endDate)){
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	   Date Nowdate=new Date(); 
	   endDate=formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),Nowdate.getDate()-1)); //本月当前天的前一天
	   startDate=formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),Nowdate.getDate()-1)); //本月当前天的前一天
   }
%>
<head>
    <%@include file="../../portalCommon/public/head.jsp"%>
    <%@include file="../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript" src="openLayer.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ReportsMonAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplBusiTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ProdTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
     <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <script type="text/javascript"   src="<%=rootPath%>/js/common/prodTypeTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/cmplBusiTypeTree.js"></script>
    
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
     <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
</head>
    <script type="text/javascript">
	   var zoneCode="<%=zoneCode %>";
	   zoneCode=(zoneCode!='null' && zoneCode!="")?zoneCode:userInfo['localCode'];
	   var prodTypeCode="<%=prodTypeCode %>";
	   prodTypeCode=(prodTypeCode!='null' && prodTypeCode!="")?prodTypeCode:"-1";
	   var cmplBusiTypeCode="<%=cmplBusiTypeCode %>";
	   cmplBusiTypeCode=(cmplBusiTypeCode!='null' && cmplBusiTypeCode!="")?cmplBusiTypeCode:"1";
	</script>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	   <input type="hidden""  id="indId"   value="<%=indId %>" name="indId" />
	    <input type="hidden" id="excelTitle"   name="excelTitle"     value="<%=menuName %>" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />
        <input type="hidden" id="totalCount"   name="totalCount"    /> 
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" value="tydic.reports.ReportsMonAction"   name="class"     />
        <input type="hidden" value="getTableData"    name="classMethod"   />
        <input type="hidden" id="fileType"    name="fileType"     /> 
         <input type="hidden" id="channelType"   name="channelType"   value="<%=channelType  %>" />  
        <input type="hidden" id="reasonId"  name="reasonId"  value="<%=reasonId  %>" />
        <input type="hidden" id="ripId"  name="ripId"  value="<%=ripId  %>" />   
        	    <table width='100%'  border='0' cellpadding='0px' cellspacing='0px'>
					<tr>
						<td valign="top" width="100%">
							   <table style='border: 1px solid #87CEFF;' width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
									   <%--<tr height='25px' style='background:url(<%=path%>/images/fpage_04.gif);'>
											 <td nowrap align='left' class='title_ma1'>
												查询条件                                  
											 </td>
										</tr>
										--%><tr>
										     <td>
													<table id='queryCondition' width="1030"  border="0" cellpadding="0" cellspacing="0">
											                <tr>
											                  <td width="7%">&nbsp;&nbsp;&nbsp;日期从:</td>
											                  <td width="7%">
											                     <input name="startDate" id="startDate" size="12" value="<%=startDate %>" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=endDate %>'})" >
											                  </td>
											                  <td width="2%">至&nbsp;&nbsp;&nbsp;</td>
											                  <td width="7%">
											                     <input name="endDate" id="endDate" size="12" value="<%=endDate %>" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=endDate %>'})" >
											                  </td>
											                  <td width="5%">&nbsp;区 域:</td>
											                  <td width="9%">
																  <input type="hidden"  id="zoneCode"   value="<%=zoneCode%>"     name="zoneCode" />
																  <input type="text"    id="zone"           name="zone"  style="width: 120px;height:18px;line-height:18px;" readonly="readonly"/>
											                  </td>
											                  <td width="7%">&nbsp;产品类型:</td>
											                  <td width="9%">
														           <input type="hidden""  id="prodTypeCode"  value="<%=prodTypeCode%>"       name="prodTypeCode" />
																   <input type="text"     id="prodType"             name="prodType"    style="height:18px;line-height:18px;" readonly="readonly"/>
											                  </td>	
											                  <td width="7%">&nbsp;投诉现象:</td>
											                  <td width="9%">
														           <input type="hidden""  id="cmplBusiTypeCode"  value="<%=cmplBusiTypeCode%>" name="cmplBusiTypeCode" />
		                                                           <input type="text"     id="cmplBusiType"     name="cmplBusiType" style="height:18px;line-height:18px;" readonly="readonly"/>
											                  </td>	 										                  
											                  <td nowrap width="7%">
											                       <input type="button"   class="poster_btn"  id="queryBtn"  name="queryBtn"   onclick="queryData();" style="width:60px;"   value="查  询" />
											                  </td>
											                   <td width="10%">
											                      &nbsp;&nbsp;&nbsp;<input type="button"   class="poster_btn"                     onclick="impExcel(this.form);"       style="width:60px;"  value="导 出" />
											                  </td> 
											                </tr>
													</table>			        
										    </td>
										</tr>
							  </table>	
                              <div id="chartTable" style="margin: 0px;padding: 0px;width: 100%;height: auto;">
                             
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
	     	    var startDate=$("startDate").value;
	     	    var endDate=$("endDate").value;
	     	    var zone=$("zone").value;
	     	    var prodType=$("prodType").value;
	     	    var cmplBusiType=$("cmplBusiType").value;
	     	     var queryCond="日期从:"+startDate+"    至:"+endDate+"    区域："+zone+"     产品类型:"+prodType+"    投诉现象："+cmplBusiType;
	     	    var totalCount   =$("totalCount").value;
	     	    $("fileType").value="csv"
    			 $("excelCondition").value=queryCond;
	     	    dhx.showProgress("正在执行......");
	     	    var url = getBasePath()+"/downLoadServlet";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
				setInterval("closePro()",20000);
	}
	function closePro(){
	dhx.closeProgress();
	}
</script>
<script type="text/javascript" src="cmpl_list_mon.js"></script>