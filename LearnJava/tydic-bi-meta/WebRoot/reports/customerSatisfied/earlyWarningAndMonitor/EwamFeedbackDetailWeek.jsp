<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,java.util.Calendar,tydic.meta.common.DateUtil"  pageEncoding="UTF-8"%>
<%@ page import="tydic.reports.customerSatisfied.CustomerSatisfiedDAO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <%@include file="../../../portalCommon/public/head.jsp" %>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title>预警监控推送异常清单反馈操作</title>
    <meta http-equiv="pragma" content="no-cache" />
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <%
      String vTypeId    =(String)request.getParameter("vTypeId"); 
	  vTypeId=vTypeId==null?"":vTypeId;
	  
	  String targetId    =(String)request.getParameter("targetId"); 
	  vTypeId=targetId==null?"":targetId;
      
      String zoneCode   =(String)request.getParameter("zoneCode");
      zoneCode=zoneCode==null?"0000":zoneCode;
      
	  String startTime      =(String)request.getParameter("startTime");	
	  String endTime        =(String)request.getParameter("endTime");
	  //String orderId        =(String)request.getParameter("orderId");	
	  	  
	  //String menuName="预警监控推送异常清单反馈操作";
	  
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      Date Nowdate=new Date(); 
      String  monthFirstDay="";
      String  monthEndNextDay="";
      CustomerSatisfiedDAO dao=new CustomerSatisfiedDAO();
      if( (startTime !=null &&  !"".equals(startTime)) && (endTime !=null &&  !"".equals(endTime)) )
      {
    	  monthFirstDay  =startTime;
    	  monthEndNextDay=endTime;
      }
      else if( startTime !=null &&  !"".equals(startTime) )
      {
    	  monthFirstDay   =startTime;
    	  monthEndNextDay =startTime;  
      }
      else
      {
    	 
    	 String dayTime=dao.getNewDay("CS_VISIT_EWAM_RESULT");//预警监控
         String year1=dayTime.substring(0,4);
  		 String month1=dayTime.substring(4,6);
  		 String day1=dayTime.substring(6,8);
  		 dayTime=year1+"-"+month1+"-"+day1;
  		 monthFirstDay  =dayTime;
    	 monthEndNextDay=dayTime;
      }
      String week = request.getParameter("weekTime");
      List<Map<String, Object>> weekList=dao.getDateTimeListSum("1","19");
    %>
</head>
   <script type="text/javascript">
	   var zoneCode="<%=zoneCode %>";
	       zoneCode=(zoneCode != 'null' && zoneCode != "")?zoneCode: userInfo['localCode'];
   		   zoneCode = zoneCode != null?zoneCode:"0000";  	
   	   var typeId=<%=vTypeId %>;
   </script>
<body style="overflow: visible;">
   <form id="queryform" name="queryform">
        <input type="hidden" id="excelTitle"   name="excelTitle"    />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"     />
        <input type="hidden" id="totalCount"   name="totalCount"    /> 
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="fileType"    name="fileType"     /> 
        <input type="hidden" value="tydic.reports.customerSatisfied.CustomerSatisfiedAction"   name="class"     />
        <input type="hidden" value="getzqSatisfyList"    name="classMethod"   />
        
	    <table width='100%'  border='0' cellpadding='0px' cellspacing='0px'>
					<tr>
						<td valign="top" width="100%">
							   <table style='border: 1px solid #87CEFF;' width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table id='queryCondition' width="1100"  border="0" cellpadding="0" cellspacing="0">
											                <tr>
											                  <td width="1%" style="display: none">回访日期:</td>
											                  <td width="1%" style="display: none">
											                      <input     id="startTime" name="startTime"  value="<%=monthFirstDay %>"  readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
											                  </td>
											                  <td width="1%" style="display: none">至</td>
											                  <td width="1%" style="display: none">
											                      <input     id="endTime"   name="endTime"    value="<%=monthEndNextDay %>" readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
											                  </td>
											                  <td width="18%">&nbsp;&nbsp;&nbsp;预警周期:
                                                                 <select id="weekTime"    name="weekTime" style="width: 160px;">
                                                                  <% for (Map<String, Object> key : weekList) {  
                                                                  %>
                                                                   <% if(((String)key.get("DATE_NO")).equals(week) ){ %>
                                                                    <option value="<%=key.get("DATE_NO").toString().replaceAll("_","~") %>" selected="selected"><%=key.get("DATE_NO").toString().replaceAll("_","~") %></option>
                                                                   <% } else{%>   
                                                                    <option value="<%=key.get("DATE_NO").toString().replaceAll("_","~") %>"><%=key.get("DATE_NO").toString().replaceAll("_","~") %></option>
                                                                                   <% }%> 
                                                                          <% }%>    
                                                                  </select>	
                                                              </td>
											                  <!--
											                  <td width="6%" style="visibility: hidden">工单号:</td>
											                  <td width="11%" style="visibility: hidden">
																  <input type="text"    id="orderId" value="" name="orderId"  style="width: 250px;height:18px;line-height:18px;" />
											                  </td>
											                  -->
											                  <td width="3%" >区 域:</td>
											                  <td width="11%">
																  <input type="hidden"  id="zoneCode"       name="zoneCode" value="<%=zoneCode %>"/>
																  <input type="text"     id="zone"  size="17"      name="zone" />
											                  </td>
											                  <td width="5%">测评体系:</td>
											                  <td width="10%">
											                  		<select id="vTypeId" name="vTypeId">
											                          <option value="" <% if("".equals(vTypeId)){%>   selected="selected"  <% }%>>==请选择==</option>
											                          <option value="11" <% if("11".equals(vTypeId)){%>   selected="selected"  <% }%>>宽带IVR装移机</option>
											                          <option value="12" <% if("12".equals(vTypeId)){%>   selected="selected"  <% }%>>宽带IVR修障</option>
 																	</select>
 																</td>
											                   <td width="5%">测评指标:</td>
											                  <td  width="10%">
											                  		<select id="targetId" name="targetId">
											                          <option value="" <% if("".equals(targetId)){%>   selected="selected"  <% }%>>==请选择==</option>
											                          <option value="0" <% if("0".equals(targetId)){%>   selected="selected"  <% }%>>邀请量</option>
											                          <option value="1" <% if("1".equals(targetId)){%>   selected="selected"  <% }%>>参与率</option>
											                          <option value="2" <% if("2".equals(targetId)){%>   selected="selected"  <% }%>>满意率</option>											                          
											                       </select>
											                  </td>
											                  <td  width="8%" >
											                     <input type="button"   class="poster_btn"  id="queryBtn"  name="queryBtn" value="查  询" onclick="queryData()" style="width:60px;" />
											                  </td>										                   
											                  <td  width="8%" >
											                     <input type="button"    class="poster_btn"                     onclick="impExcel(this.form);"   value="导 出"    style="width:60px;" />
											                  </td>
											                  <!--
											                  <td  width="8%" >
											                     <input type="button"    class="poster_btn"                     onclick="finish(this.form);"   value="完成待办"    style="width:60px;" />
											                  <td width="25%" style="5%">
											                  <div style="color: red">点击按钮结束待办事项</div></td>
											                  -->
											                  <!--
											                  <td  width="10%">
											                     <input type="button"    class="poster_btn"                     onclick="sendMess(this.form);"   value="发送代办"    style="width:80px;" />
											                  </td>
											                --></tr>
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
function dump_obj(myObject) {  
	  var s = "";  
	  for (var property in myObject) {  
	   s = s + "\n "+property +": " + myObject[property] ;  
	  }  
	  alert(s);  
	} 
	function impExcel(form){
	 var  zone=$("zone").value;//区域
	 var zoneCode    =$("zoneCode").value==""?zoneCode:$("zoneCode").value;
     var startTime   =$("startTime").value;
     var endTime     =$("endTime").value;
     var totalCount   =$("totalCount").value;
     var queryCond="回访日期:"+startTime+"    至:"+endTime+"     区域："+zone;
     $("excelCondition").value=queryCond; 
	     	    dhx.showProgress("正在执行......");
	     	    var url =   getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcelNoSatisDetail.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	}
</script>

<script type="text/javascript" src="EwamFeedbackDetailWeek.js"></script>


