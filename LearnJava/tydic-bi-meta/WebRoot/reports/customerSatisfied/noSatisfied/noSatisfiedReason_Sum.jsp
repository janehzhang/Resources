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
     String areaCode="0000";
     String vTypeId =(String)request.getParameter("vTypeId")==null?"2":(String)request.getParameter("vTypeId");
     String zoneCode =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
     String userCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
     String reasonCode = request.getParameter("reasonCode")==null?"0000":(String)request.getParameter("reasonCode");
     CustomerSatisfiedDAO dao=new CustomerSatisfiedDAO();
     //日
     String startTime =(String)request.getParameter("startDate");
     String endTime =(String)request.getParameter("endDate");
     String time="";
     if(startTime==null || startTime.equals("")){
	   	      time=dao.getMaxDate_EnterSum(vTypeId).substring(0,8);
		      String dayYear=time.substring(0,4);
		      String dayMon=time.substring(4,6);
		      String day=time.substring(6,8);
		      time=dayYear+"-"+dayMon+"-"+day;
		      startTime=time;
		      endTime=time;
 	}else{
 		clickTab="tab3";
 	} 
     List<Map<String, Object>> vTypeList=dao.getVTypeList();
     
     String week = request.getParameter("dateTime");
     if(week==null || week.equals("")){
         week=dao.getMaxDateTime_CustomerSatisfiedSum("1",vTypeId); 
     }else{
    	 clickTab="tab2";
  	}
      List<Map<String, Object>> weekList=dao.getDateTimeListSum("1",vTypeId);
      
      String month = request.getParameter("dateTime1");
      if(month==null || month.equals("")){
     	 month=dao.getMaxDateTime_CustomerSatisfiedSum("2",vTypeId);
      }else{
    	  clickTab="tab1";
      }
       List<Map<String, Object>> monList=dao.getDateTimeListSum("2",vTypeId);
       request.setAttribute("dateTime",week);
       request.setAttribute("dateTime1",month);
       request.setAttribute("startDate",startTime);
       request.setAttribute("endDate",endTime);
     %>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ReasonAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/reasonTree.js"></script>
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
	</script>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value="<%=menuName %>报表" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />  
        <input type="hidden" id="excelCondition"    name="excelCondition"   />
        <input type="hidden" id="clickTab"    name="clickTab" value="<%=clickTab%>"/> 
        <input type="hidden" id="cid"  name="cid"   /> 
        <input type="hidden" id="userCode"    name="userCode" value="<%=userCode %>"/> 
        <input type="hidden" id="userCodeData"    name="userCodeData" /> 
         <input type="hidden" id="vTypeId"  name="vTypeId"  value="<%=vTypeId%>" />
        <input type="hidden" id="vTypeId1"  name="vTypeId1"  value="<%=vTypeId%>" />
        <input type="hidden" id="vTypeId2"  name="vTypeId2"  value="<%=vTypeId%>" />
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
													<table  width="950"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
           <td style="width: 180px;">&nbsp;&nbsp;日期从:
	  	       <input name="startDate" id="startDate" size="18" value="${startDate}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=startTime %>'})" >
		   </td>
		   <td style="width: 180px;">&nbsp;&nbsp;至:
		       <input name="endDate"  id="endDate"    size="18" value="${endDate}" readonly="true" class='Wdate'   onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=endTime %>'})" >
		   &nbsp;&nbsp;</td>
           <td width='17%'>区域:
			   <input type="hidden""  id="zoneCode2"  value="<%=zoneCode%>" name="zoneCode2" />
			   <input type="text"     id="zone2"  size="17"      name="zone2" />
		   </td>
		   <%if("5".equals(vTypeId)||"11".equals(vTypeId)||"12".equals(vTypeId)){ %>
		   <td style="width:250px;">不满意原因:
		   <input type="hidden""  id="reasonCode2"  value=<%=reasonCode%>   name="reasonCode2" />
		   <input type="text"     id="reason2"  size="25"      name="reason2" />
		   </td>
		   <%}%>
	    <td><input type='button'  class="poster_btn" id="queryBtn2"  name="queryBtn2"  value="查  询"    onclick="queryData2();"    style="width:60px;" /></td>
	    <td><input type='button'  class="poster_btn" id="impBtn2"    name="impBtn2"    value="导  出" onclick="impExcel2();" style="width:60px;" /></td>
		<td >
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
							<table id='topTable' width='100%' height='auto!important' border='0' cellpadding='0px' cellspacing='2px'>
								<tr>
									<td width='50%' >
										<table style='border: 1px solid #87CEFF;' width='100%' height='280px' border='0' cellpadding='0' cellspacing='0'>
											<tr>
												<td>
													<div id='chartdiv2'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='280px' border='0' cellpadding='0' cellspacing='0'>
											<tr>
												<td>
													<div id='chartTable2'></div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
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
													<table  width="850"  border="0" cellpadding="0" cellspacing="0">
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
		   <input type="text"     id="zone"  size="17"      name="zone" /></td>
		    <%if("5".equals(vTypeId)||"11".equals(vTypeId)||"12".equals(vTypeId)){ %>
		   <td style="width:250px;">不满意原因:
		   <input type="hidden""  id="reasonCode"  value=<%=reasonCode%>   name="reasonCode" />
		   <input type="text"     id="reason"  size="25"      name="reason" />
		   </td>
		   <%}%>
		 <td style="width:60px;"><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td style="width:60px;"><input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
													                </tr>
													</table>			        
										    </td>
										</tr>
							  </table>	
							      </td>
			       </tr>
                   <tr>
			          <td width="100%"> 			                  
							<table id='topTable' width='100%' height='auto!important' border='0' cellpadding='0px' cellspacing='2px'>
								<tr>
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='250px' border='0' cellpadding='0' cellspacing='0'>
											<tr>
												<td>
													<div id='chartdiv'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='250px' border='0' cellpadding='0' cellspacing='0'>
											<tr>
												<td>
													<div id='chartTable'></div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
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
													<table  width="850"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		    <td style="width: 170px;">&nbsp;&nbsp;&nbsp;月份:
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
		   </td>
		    <%if("5".equals(vTypeId)||"11".equals(vTypeId)||"12".equals(vTypeId)){ %>
		   <td style="width:250px;">不满意原因:
		   <input type="hidden""  id="reasonCode1"  value=<%=reasonCode%>   name="reasonCode1" />
		   <input type="text"     id="reason1"  size="25"      name="reason1" />
		   </td>
		   <%}%>
		 <td style="width:60px;"><input type='button'  class="poster_btn" id="queryBtn1"  name="queryBtn1"  value="查  询"    onclick="queryData1();"    style="width:60px;" /></td>
	     <td style="width:60px;"><input type='button'  class="poster_btn" id="impBtn1"    name="impBtn1"    value="导  出" onclick="impExcel1();"         style="width:60px;" /></td>
													                </tr>
													</table>			        
										    </td>
										</tr>
							  </table>	
							      </td>
			       </tr>
 <tr>
			          <td width="100%"> 			                  
							<table id='topTable' width='100%' height='auto!important' border='0' cellpadding='0px' cellspacing='2px'>
								<tr>
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='250px' border='0' cellpadding='0' cellspacing='0'>
											<tr>
												<td>
													<div id='chartdiv1'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='250px' border='0' cellpadding='0' cellspacing='0'>
											<tr>
												<td>
													<div id='chartTable1'></div>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
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
<script type="text/javascript" src="noSatisfiedReason_Sum.js"></script>