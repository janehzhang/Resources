<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,java.util.Calendar,tydic.meta.common.DateUtil"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
 <%
	    String zoneCode       =(String)request.getParameter("zoneCode");	
	    String startTime      =(String)request.getParameter("startTime");	
	    String endTime        =(String)request.getParameter("endTime");	
	    String indexId        =request.getParameter("indexId")==null?"":request.getParameter("indexId");	
	    String prodType       =request.getParameter("prodType")==null?"":request.getParameter("prodType");	
      
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
      
      if("".equals(indexId))
      {
    	  indexId="3";
      }
      
    %>
<head>
    <%@include file="../../../public/head.jsp" %>
    <%@include file="../../../public/include.jsp" %>
    <title><%=menuName %></title>
    <meta http-equiv="pragma" content="no-cache" />
    <script type="text/javascript" src="openLayer.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/FaultDetailAction.js"></script>
    <script type="text/javascript" src="<%=path%>/js/Charts/FusionCharts.js"></script> 
    <script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
   
</head>
    <script type="text/javascript">
	   var zoneCode="<%=zoneCode %>";
	       zoneCode=(zoneCode != 'null' && zoneCode != "")?zoneCode:userInfo['localCode'];
   </script>
<body style="overflow: visible;">
   <form id="queryform" name="queryform">
        <input type="hidden" id="excelTitle"   name="excelTitle"    value="<%=menuName %>" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />
        <input type="hidden" id="totalCount"   name="totalCount"    />   
        <input type="hidden" id="excelCondition"   name="excelCondition"    />     
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
													<table  width="1150"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
													                  <td width="5%">归档日期:</td>
													                  <td width="11%">
													                      <input     id="startTime" name="startTime"  value="<%=monthFirstDay %>"  readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
													                  </td>
													                  <td width="2%">至</td>
													                  <td width="11%">
													                      <input     id="endTime"   name="endTime"    value="<%=monthEndNextDay %>" readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
													                  </td>
													                  <td width="3%">区 域:</td>
													                  <td width="14%">
																		  <input type="hidden"  id="zoneCode"       name="zoneCode" />
																		  <input type="text"    id="zone"           name="zone"  style="width: 160px;height:18px;line-height:18px;" readonly="readonly"/>
													                  </td>
													                  <td width="5%">业务类型:</td>
													                  <td width="10%">
													                      <select id="prodType" name="prodType">
													                          <option value=""     <% if("".equals(prodType)){%> selected  <% }%>  >==请选择==</option>
													                          <option value="10"   <% if("10".equals(prodType)){%> selected  <% }%>  >固话</option>
													                         <!-- 
													                          <option value="20"   <% if("20".equals(prodType)){%> selected  <% }%>  >小灵通</option>
													                          <option value="30"   <% if("30".equals(prodType)){%> selected  <% }%>  >移动</option>
													                         -->
													                          <option value="40"   <% if("40".equals(prodType)){%> selected  <% }%>  >宽带</option>
													                          <option value="50"   <% if("50".equals(prodType)){%> selected  <% }%>  >光宽</option>
													                          <option value="-999" <% if("-999".equals(prodType)){%> selected  <% }%>  >其它</option>
													                      </select>
													                  </td>
													                  <td width="5%"  style="display: none;">指标项:</td>
													                  <td width="12%" style="display: none;">
													                      <select id="indexId" name="indexId">
													                          <option value=""   <% if("".equals(indexId)){%> selected  <% }%> >==请选择==</option>
													                          <option value="1"  <% if("1".equals(indexId)){%> selected  <% }%> >障碍申告量</option>
													                          <option value="2"  <% if("2".equals(indexId)){%> selected  <% }%> >障碍申告预处理量</option>
													                          <option value="3"  <% if("3".equals(indexId)){%> selected  <% }%> >障碍申告形成工单量</option>
													                      </select>
													                   </td>
													                   <td nowrap width="6%">
													                       <input type="button"  class="poster_btn"  id="queryBtn"  name="queryBtn"   onclick="queryData();" style="width:60px;" value="查  询"/>
													                   </td>
													                   <td width="20%">
																	       <input type="button"  class="poster_btn"                     onclick="impExcel(this.form);"       style="width:60px;" value="导 出"/>					                    
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
					alert("数据记录大于32000,无法导出")
					return
				}
				var  startTime=$("startTime").value;
				var  endTime=$("endTime").value;
		  	    var  zone=$("zone").value;//文本框
		  	    var  prodType=$("prodType").value;
			    var  queryCond="归档日期："+startTime+"     至："+endTime+"     区域:"+zone+"     业务类型："+$("prodType").options[$("prodType").selectedIndex].text;
		 	    $("excelCondition").value=queryCond;   
		        //alert($('queryCondition').innerHTML);
	     	    //var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel.jsp";
	     	    dhx.showProgress("正在执行......");
	     	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel_faultDetail.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	}
</script>
<script type="text/javascript" src="faultDetail.js"></script>