<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.customerSatisfied.CustomerSatisfiedDAO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    CustomerSatisfiedDAO dao=new CustomerSatisfiedDAO();
	String month = request.getParameter("dateTime");
	if(month==null || month.equals("")){
		 month=dao.getMaxMonth_ChannelCS();
	}
	 List<Map<String, Object>> monList=dao.getDateTimeList_ChannelCS();
	 String filePath="/upload/excelHeader/channelCustService.xls";
	 String areaCode="0000";
	 String zoneCode  =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
	 String iCustCode = request.getParameter("iCustCode")==null?"":request.getParameter("iCustCode");
	 String channelTypeCode = request.getParameter("channelTypeCode")==null?"1":(String)request.getParameter("channelTypeCode");
	 String channelType = request.getParameter("channelType")==null?"所有类型":(String)request.getParameter("channelType");
	
	if (iCustCode=="1" || "1".equals(iCustCode))
    {
    	 filePath="/upload/excelHeader/channelCustServiceSj.xls";
    }
    else if (iCustCode=="2" || "2".equals(iCustCode))
    {
    		 filePath="/upload/excelHeader/channelCustServiceKffq.xls";
    }  
    else if (iCustCode=="4" || "4".equals(iCustCode))
    {
    		 filePath="/upload/excelHeader/channelCustServiceKfdj.xls";
    }
    else if (iCustCode=="5" || "5".equals(iCustCode))
    {
    		 filePath="/upload/excelHeader/channelCustServiceCzsr.xls";
    }
    else if (iCustCode=="6" || "6".equals(iCustCode))
    {
    		 filePath="/upload/excelHeader/channelCustServiceZwsc.xls"; 
    }
    else if (iCustCode=="7" || "7".equals(iCustCode))
    {
    		 filePath="/upload/excelHeader/channelCustServiceCplx.xls";
    }
    else if (iCustCode=="8" || "8".equals(iCustCode))
    {
    		 filePath="/upload/excelHeader/channelCustServiceYhpp.xls";
    }
    else if (iCustCode=="9" || "9".equals(iCustCode))
    {
    		filePath="/upload/excelHeader/channelCustServiceFffs.xls";
    }	
    else 
    {
    	filePath="/upload/excelHeader/channelCustServiceXfsc.xls";
    }
    
%>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
     <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
     <script type="text/javascript"   src="<%=rootPath%>/js/common/channelTypeTree.js"></script>
     <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ChannelTypeAction.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
		function impExcel(){
			var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
			var  zone=$("zone").value;//区域
		    var queryCond="月份："+dateTime+"    区域："+zone;;
	   	    $("excelCondition").value=queryCond; 
	   	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel.jsp";//多级表头
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
	    }    
	</script>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
       <input type="hidden" id="excelHeader"  name="excelHeader"  value=<%=menuName %> />
       <input type="hidden" id="excelData"    name="excelData"   />  
       <input type="hidden" id="excelCondition"    name="excelCondition"     />
       <input type="hidden" id="filePath"    name="filePath" value=<%=filePath %> />
        <input type="hidden" id="row"    name="row" value="4" />
       <input type="hidden" id="iCustCode"  name="iCustCode"   value="<%=iCustCode %>" />
				<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%' style='border: 1px solid #87CEFF;' border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="900"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		<td style="width: 180px;">&nbsp;&nbsp;&nbsp;月份:
          <select id="dateTime"    name="dateTime" style="width: 120px;">
           <% for (Map<String, Object> key : monList) {  
           %>
               <% if(((String)key.get("DATE_NO")).equals(month) ){ %>
                  <option value="<%=key.get("DATE_NO") %>" selected="selected"><%=key.get("DATE_NO").toString() %></option>
               <% } else{%>   
                  <option value="<%=key.get("DATE_NO") %>"><%=key.get("DATE_NO").toString() %></option>
               <% }%> 
           <% }%>    
          
          </select>	
          </td>
           <td style="width: 170px;">区域:
			   <input type="hidden""  id="zoneCode"  value="<%=zoneCode%>" name="zoneCode" />
			   <input type="text"     id="zone"  size="17"      name="zone" />
		   </td>
		    <td>渠道类型:
		   <input type="hidden""  id="channelTypeCode"  value=<%=channelTypeCode%>   name="channelTypeCode" />
		   <input type="text"     id="channelType"  size="20"    value=<%=channelType%>  name="channelType" />
	       <td>	       
			 <td> 数值类型: 
			<select id="number_state"  name="number_state" style="width: 80px;">
			<option value="1" selected>量</option>
			<option value="2">占比</option>
			</select>		  
			</td>
	     <td style="width: 60px;"><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td style="width: 60px;"><input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
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
                       </td>
                   </tr> 
	      </table>
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="channelCustService.js"></script>