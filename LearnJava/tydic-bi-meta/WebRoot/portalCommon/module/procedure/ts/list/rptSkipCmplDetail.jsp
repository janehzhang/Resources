<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,java.util.Calendar,tydic.meta.common.DateUtil"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <%@include file="../../../../public/head.jsp" %>
    <%@include file="../../../../public/include.jsp" %>
    <title><%=menuName %></title>
    <meta http-equiv="pragma" content="no-cache" />
    <script type="text/javascript" src="openLayer.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ProdTypeAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/RptSkipCmplDetailAction.js"></script>
    <script type="text/javascript" src="<%=path%>/js/Charts/FusionCharts.js"></script> 
    <script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/prodTypeTree.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <%
	    String zoneCode       =(String)request.getParameter("zoneCode");	
	    String startTime      =(String)request.getParameter("startTime");	
	    String endTime        =(String)request.getParameter("endTime");	
	    String prodType       =request.getParameter("prodType")==null?"":request.getParameter("prodType");//产品类型
	    String businessType   =request.getParameter("businessType")==null?"":request.getParameter("businessType");//投诉业务类型
	    String reasonType     =request.getParameter("reasonType")==null?"":request.getParameter("reasonType");////投诉原因
	    String channelType    =request.getParameter("channelType")==null?"":request.getParameter("channelType");//投诉渠道
	    String indexId        =request.getParameter("indexId")==null?"":request.getParameter("indexId");//指标类型	
	    
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
		  monthFirstDay  =formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),Nowdate.getDate()-1));  //本月当前天的前一天
		  monthEndNextDay=formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),Nowdate.getDate()-1));//本月当前天的前一天
	  }
	  
    %>
</head>
  <script type="text/javascript">
	   var zoneCode="<%=zoneCode %>";
	       zoneCode=(zoneCode != 'null' && zoneCode != "")?zoneCode:userInfo['localCode'];
	   var prodType="<%=prodType %>";
	       prodType=(prodType != 'null' && prodType != "")?prodType:'-1';
   </script>
<body style="overflow: visible;">
   <form id="queryform" name="queryform">
        <input type="hidden" id="excelTitle"   name="excelTitle"    value="<%=menuName %>" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"     />
        <input type="hidden" id="totalCount"   name="totalCount"    /> 
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="businessType" name="businessType" value="<%=businessType  %>" />      
        <input type="hidden" id="reasonType"   name="reasonType"   value="<%=reasonType  %>" />  
        <input type="hidden" id="channelType"  name="channelType"  value="<%=channelType  %>" />  
        <input type="hidden" id="indexId"      name="indexId"      value="<%=indexId  %>" /> 
               
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
													<table id='queryCondition' width="1000"  border="0" cellpadding="0" cellspacing="0">
											                <tr>
											                  <td width="9%">&nbsp;&nbsp;&nbsp;归档日期:</td>
											                  <td width="11%">
											                      <input     id="startTime" name="startTime"  value="<%=monthEndNextDay %>"  readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
											                  </td>
											                  <td width="2%">&nbsp;&nbsp;&nbsp;至&nbsp;&nbsp;&nbsp;</td>
											                  <td width="11%">
											                      <input     id="endTime"   name="endTime"    value="<%=monthEndNextDay %>" readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
											                  </td>
											                  <td width="7%">&nbsp;&nbsp;&nbsp;区 域:</td>
											                  <td width="11%">
																  <input type="hidden"  id="zoneCode"       name="zoneCode" />
																  <input type="text"    id="zone"           name="zone"  style="width: 160px;height:18px;line-height:18px;" readonly="readonly"/>
											                  </td>
											                  <td width="9%">&nbsp;&nbsp;&nbsp;产品类型:</td>
											                  <td width="11%">
														           <input type="hidden""  id="prodTypeCode"         name="prodTypeCode" />
																   <input type="text"     id="prodType"             name="prodType"    style="height:18px;line-height:18px;" readonly="readonly"/>
											                  </td>											                  
											                  <td nowrap width="7%">
											                       <input type="button"   class="poster_btn"  id="queryBtn"  name="queryBtn"   onclick="queryData();" style="width:60px;"   value="查  询" />
											                  </td>
											                  <td width="20%">
											                      &nbsp;&nbsp;&nbsp;<input type="button"   class="poster_btn"                     onclick="impExcel(this.form);"       style="width:60px;"  value="导 出" />
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
		        //alert($('queryCondition').innerHTML);
	     	    //var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel.jsp";
	     	    var  zone=$("zone").value;//区域
	     	    var prodType=$("prodType").value;
    			var startTime   =$("startTime").value;
    			var endTime     =$("endTime").value;
	     	    var zoneCode=$("zoneCode").value;
	     	    var prodTypeCode=$("prodTypeCode").value;
	     	    var queryCond="回访日期:"+startTime+"    至:"+endTime+"    区域："+zone+"     产品类型:"+prodType;
    			 $("excelCondition").value=queryCond; 
	     	    dhx.showProgress("正在执行......");
	     	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel_rptSkipCmplDetail.jsp";
	     	    
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	}
</script>
<script type="text/javascript" src="rptSkipCmplDetail.js"></script>