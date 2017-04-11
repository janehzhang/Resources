<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,java.util.Calendar,tydic.meta.common.DateUtil"  pageEncoding="UTF-8"%>
<%@ page import="tydic.reports.customerSatisfied.CustomerSatisfiedDAO"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <%@include file="../../../public/head.jsp" %>
    <%@include file="../../../public/include.jsp" %>
    <title><%=menuName %></title>
    <meta http-equiv="pragma" content="no-cache" />
    <script type="text/javascript" src="<%=path%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ADSLVisitListAction.js"></script>
    <script type="text/javascript" src="<%=path%>/js/Charts/FusionCharts.js"></script> 
    <script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <%
      String zoneCode       =(String)request.getParameter("zoneCode");
	  String startTime      =(String)request.getParameter("startTime");	
	  String endTime        =(String)request.getParameter("endTime");	
	  String indexId        =(String)request.getParameter("indexId"); 
	  indexId=indexId==null?"":indexId;
	  String reasonId       =(String)request.getParameter("reasonId"); 
	  reasonId=reasonId==null?"":reasonId;
	  String satisType       =(String)request.getParameter("satisType"); 
	  String notSatisType1       =(String)request.getParameter("notSatisType1"); 
	  notSatisType1=notSatisType1==null?"":notSatisType1;
	  String notSatisType2       =(String)request.getParameter("notSatisType2"); 
	  notSatisType2=(notSatisType2==null||"Â Â ".equals(notSatisType2)||notSatisType2=="Â Â ")?"":notSatisType2;
	  satisType=satisType==null?"":satisType;
	  String ripId =request.getParameter("ripId")==null?"-1":request.getParameter("ripId");
	  String rptId =(String)request.getParameter("rptId")==null?"1":(String)request.getParameter("rptId");
	  long timeStamp=System.currentTimeMillis();
	  if(menuName.equals("")){
		  if("1".equals(rptId)){
			  menuName="光宽装移即时回访清单";
		  }
		  else if ("3".equals(rptId)){
			  menuName="光宽移机即时回访清单";  
		  }
		  else{
			  menuName="光宽新装即时回访清单"; 
		  }
	  }
	  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      Date Nowdate=new Date(); 
      //String  monthFirstDay=formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),1));//本月的第一天
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
    	 String dayTime=dao.getNewDay("CS_VISIT_IVRZYJ_AREA_1");//光宽装移
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
   </script>
<body style="overflow: visible;">
   <form id="queryform" name="queryform">
        <input type="hidden" id="excelTitle"   name="excelTitle"    value="<%=menuName %>" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"     />
        <input type="hidden" id="totalCount"   name="totalCount"    /> 
        
        <input type="hidden" id="fileType"    name="fileType"     /> 
        <input type="hidden" value="tydic.portalCommon.procedure.visitDetail.ADSLVisitListAction"   name="class"     />
        <input type="hidden" value="getTableDataIVRZYJ"    name="classMethod"   />
        <input type="hidden" id="rptId"  name="rptId"  value="<%=rptId%>" />
        <input type="hidden" id="notSatisType1"  name="notSatisType1"  value="<%=notSatisType1  %>" /> 
        <input type="hidden" id="notSatisType2"  name="notSatisType2"  value="<%=notSatisType2  %>" /> 
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="ripId"  name="ripId"  value="<%=ripId  %>" /> 
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
													<table id='queryCondition' width="1100"  border="0" cellpadding="0" cellspacing="0">
											                <tr>
											                  <td width="7%">回访日期:</td>
											                  <td width="10%">
											                      <input     id="startTime" name="startTime"  value="<%=monthFirstDay %>"  readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
											                  </td>
											                  <td width="3%">至</td>
											                  <td width="10%">
											                      <input     id="endTime"   name="endTime"    value="<%=monthEndNextDay %>" readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
											                  </td>
											                  <td width="6%">区 域:</td>
											                  <td width="11%">
																  <input type="hidden"  id="zoneCode"       name="zoneCode" />
																  <input type="text"    id="zone"           name="zone"  style="width: 150px;height:18px;line-height:18px;" readonly="readonly"/>
											                  </td>
											                  <td width="8%">数据类型:</td>
											                  <td width="6%">
											                  		<select id="indexType" name="indexType">
											                          <option value="" <% if("".equals(indexId)){%>   selected="selected"  <% }%>>==请选择==</option>
											                          <option value="0" <% if("0".equals(indexId)){%>   selected="selected"  <% }%>>邀请客户数</option>
											                          <option value="1" <% if("1".equals(indexId)){%>   selected="selected"  <% }%>>测评有效样本数</option>
											                          <option value="2" <% if("2".equals(indexId)){%>   selected="selected"  <% }%>>测评满意数</option>
											                          <option value="3" <% if("3".equals(indexId)){%>   selected="selected"  <% }%>>不满意原因提及数</option>
											                       </select>
											                  </td>
											                 <td width="6%">满意度:</td>
											                  <td width="11%">
											                  		<select id="satisType" name="satisType">
											                          <option value="" <% if("".equals(satisType)){%>   selected="selected"  <% }%>>==请选择==</option>
											                          <option value="105" <% if("105".equals(satisType)){%>   selected  <% }%> >不能正常使用</option>
											                          <option value="106" <% if("106".equals(satisType)){%>   selected  <% }%> >可正常使用</option>
											                          <option value="101" <% if("101".equals(satisType)){%>   selected="selected"  <% }%>>非常满意</option>
											                          <option value="102" <% if("102".equals(satisType)){%>   selected="selected"  <% }%>>满意</option>
											                          <option value="103" <% if("103".equals(satisType)){%>   selected  <% }%> >不满意</option>
 																	</select>
 																</td>
											                  <td  width="8%">
											                     <input type="button"   class="poster_btn"  id="queryBtn"  name="queryBtn" value="查  询" onclick="queryData()" style="width:60px;" />
											                  </td>										                   
											                  <td  width="8%">
											                     <input type="button"    class="poster_btn"    onclick="impExcel(this.form);"   value="导 出"    style="width:60px;" />
											                  </td>
											                   <td >
											                     <input type="button"   class="poster_btn"   onclick="impExcelText(this.form);"   value="文本导出"  style="width:70px;"  />
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
     var indexType   =$("indexType").value;
     var rptId   =$("rptId").value;
     var satisType   =$("satisType").value;
     var totalCount   =$("totalCount").value;
     var queryCond="回访日期:"+startTime+"    至:"+endTime+"     区域："+zone+"     数据类型:"+$("indexType").options[$("indexType").selectedIndex].text+"    满意度："+$("satisType").options[$("satisType").selectedIndex].text;
     $("excelCondition").value=queryCond; 
     $("fileType").value="csv"
	     	    dhx.showProgress("正在执行......");
	     	   // var url =   getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcelDemensionIVRZYJ.jsp";
	     	   var url = getBasePath()+"/downLoadServlet";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
				setInterval("closePro()",20000);
	}
	
	function impExcelText(form){
	    var  zone=$("zone").value;//区域
		var zoneCode    =$("zoneCode").value==""?zoneCode:$("zoneCode").value;
	     var startTime   =$("startTime").value;
	     var endTime     =$("endTime").value;
	     var indexType   =$("indexType").value;
	     var rptId   =$("rptId").value;
	     var satisType   =$("satisType").value;
	     var totalCount   =$("totalCount").value;
      var queryCond="回访日期:"+startTime+"    至:"+endTime+"     区域："+zone+"     数据类型:"+$("indexType").options[$("indexType").selectedIndex].text+"    满意度："+$("satisType").options[$("satisType").selectedIndex].text;    	$("excelCondition").value=queryCond; 
	     $("fileType").value="txt"; 
	     dhx.showProgress("正在执行......");
	      //var url = getBasePath()+"/portalCommon/module/procedure/impExcel/ADSL_visitIVRList_implTxt.jsp";
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
<script type="text/javascript" src="ADSL_visitList_gk_zyj.js"></script>