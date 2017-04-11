<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,java.util.Calendar,tydic.meta.common.DateUtil"  pageEncoding="UTF-8"%>
<%@ page import="tydic.reports.customerSatisfied.CustomerSatisfiedDAO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <%@include file="../../../portalCommon/public/head.jsp" %>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
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
      String orderIdStr     =(String)request.getParameter("orderId");	//startTime_deptId
	 
	  String startTime      =orderIdStr.split("_")[0];
	  String endTime        =orderIdStr.split("_")[0];
      String deptId			=orderIdStr.split("_")[1];
      String vTypeId		=orderIdStr.split("_")[2];
      String targetId		=orderIdStr.split("_")[3];
      String judgeResultId	=orderIdStr.split("_")[4];
      String guid			=orderIdStr.split("_")[5];
      
      String zoneCode       =(String)request.getParameter("zoneCode");
      if(zoneCode==null||"".equals(zoneCode))
      		zoneCode=deptId;
	 // String startTime      =(String)request.getParameter("startTime");	
	 // String endTime        =(String)request.getParameter("endTime");
	  //String orderId        =(String)request.getParameter("orderId");	
	  
	  if(menuName.equals("")){
	  	menuName="预警监控推送异常清单反馈操作";
	  }
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      Date Nowdate=new Date(); 
      String  monthFirstDay="";
      String  monthEndNextDay="";
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
    	 CustomerSatisfiedDAO dao=new CustomerSatisfiedDAO();
    	 String dayTime=dao.getNewDay("CS_VISIT_EWAM_RESULT");//预警监控
         String year1=dayTime.substring(0,4);
  		 String month1=dayTime.substring(4,6);
  		 String day1=dayTime.substring(6,8);
  		 dayTime=year1+"-"+month1+"-"+day1;
  		 monthFirstDay  =dayTime;
    	 monthEndNextDay=dayTime;
      }
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
        <input type="hidden" id="excelTitle"   name="excelTitle"    value="<%=menuName %>" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"     />
        <input type="hidden" id="totalCount"   name="totalCount"    /> 
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="vTypeId"    name="vTypeId"   value="<%=vTypeId %>"  />
        <input type="hidden" id="targetId"    name="targetId"   value="<%=targetId %>"  />
        <input type="hidden" id="judgeResultId"    name="judgeResultId"   value="<%=judgeResultId %>"  />
        
	    <table width='100%'  border='0' cellpadding='0px' cellspacing='0px'>
					<tr>
						<td valign="top" width="100%">
							   <table style='border: 1px solid #87CEFF;' width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table id='queryCondition' width="1100"  border="0" cellpadding="0" cellspacing="0">
											                <tr>
											                  <td width="8%" style="visibility: hidden">回访日期:</td>
											                  <td width="10%" style="visibility: hidden">
											                      <input     id="startTime" name="startTime"  value="<%=monthFirstDay %>"  readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
											                  </td>
											                  <td width="3%" style="visibility: hidden">至</td>
											                  <td width="10%" style="visibility: hidden">
											                      <input     id="endTime"   name="endTime"    value="<%=monthEndNextDay %>" readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
											                  </td>
											                  <!--
											                  <td width="6%" style="visibility: hidden">工单号:</td>
											                  <td width="11%" style="visibility: hidden">
																  <input type="text"    id="orderId" value="" name="orderId"  style="width: 250px;height:18px;line-height:18px;" />
											                  </td>
											                  -->
											                  <td width="1%" style="visibility: hidden">区 域:</td>
											                  <td width="1%">
																  <input type="hidden"  id="zoneCode"       name="zoneCode" value="<%=zoneCode %>"/>
																  <input type="text"    id="zone"           name="zone"  style="width: 150px;height:18px;line-height:18px;visibility: hidden" readonly="readonly"/>
											                  </td>
											                  <td  width="8%" style="visibility: hidden">
											                     <input type="button"   class="poster_btn"  id="queryBtn"  name="queryBtn" value="查  询" onclick="queryData()" style="width:60px;" />
											                  </td>										                   
											                  <td  width="8%" style="visibility: hidden">
											                     <input type="button"    class="poster_btn"                     onclick="impExcel(this.form);"   value="导 出"    style="width:60px;" />
											                  </td>
											                  <td  width="8%" >
											                     <input type="button"    class="poster_btn"                     onclick="finish('<%=guid%>');"   value="完成待办"    style="width:60px;" />
											                  <td width="25%" style="5%">
											                  <div style="color: red">点击按钮结束待办事项</div></td>
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

<script type="text/javascript" src="ShowEWAMDetail.js"></script>


