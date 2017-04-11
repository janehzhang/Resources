<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,java.util.Calendar,tydic.meta.common.DateUtil"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <%@include file="../../../portalCommon/public/head.jsp" %>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <meta http-equiv="pragma" content="no-cache" />
    <script type="text/javascript" src="openLayer.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/AllChannelAction.js"></script>
    <script type="text/javascript" src="<%=path%>/js/Charts/FusionCharts.js"></script> 
    <script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <%
      String startTime      =(String)request.getParameter("startTime");	
      String endTime        =(String)request.getParameter("endTime");	
      String thirdType = request.getParameter("thirdType");
      
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
    	  monthFirstDay  =formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),Nowdate.getDate()-1));  //本月当前天的前一天
    	  monthEndNextDay=formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),Nowdate.getDate()-1));//本月当前天的前一天
      }
      String zoneCode = request.getParameter("zoneCode")==null?"0000":(String)request.getParameter("zoneCode");
      String channelId = request.getParameter("channelId")==null?"14001":(String)request.getParameter("channelId");
  	  String servId = request.getParameter("servId")==null?"0000":(String)request.getParameter("servId");
    %>
</head>
<body style="overflow: visible;">
   <form id="queryform" name="queryform">
        <input type="hidden" id="excelTitle"   name="excelTitle"    value="<%=menuName %>" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"     />
        <input type="hidden" id="totalCount"   name="totalCount"    />     
        <input type="hidden" id="excelCondition"    name="excelCondition"/>
        <input type="hidden" id="channelId"    name="channelId" value="<%=channelId %>"/>
        <input type="hidden" id="thirdType"    name="thirdType" value=<%=thirdType %> />
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
											                  <td width="5%">日期从:</td>
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
																  <input type="text"    id="zone"           name="zone"  style="width: 140px;height:18px;line-height:18px;" readonly="readonly"/>
											                  </td>
											                   <td width="7%">服务分类:</td>
											                  <td width="15%">
																  <input type="hidden"  id="servId"       name="servId" value=<%=servId %>   />
																  <input type="text"    id="serv"           name="serv"  style="width: 150px;height:18px;line-height:18px;" readonly="readonly"/>
											                  </td><%--
											                  <td align='left'>
												  <%if ("0".equals(thirdType)||thirdType=="0"){ %>
												 <select id="servId"    name="servId" style="width: 180px;" onchange="changeCol(this);">       
										           <option value="30010001" <%if("30010001".equals(servId)){ %> selected <%} %>> 办理_增值业务_订购</option>
										           <option value="30010002" <%if("30010002".equals(servId)){ %> selected <%} %>> 办理_增值业务_变更</option>
										           <option value="30010003" <%if("30010003".equals(servId)){ %> selected <%} %>> 办理_增值业务_注销</option>
										           <option value="30010004" <%if("30010004".equals(servId)){ %> selected <%} %>> 办理_增值业务_其他</option>
										            
										           <option value="30020001" <%if("30020001".equals(servId)){ %> selected <%} %>> 办理_套餐销售_订购</option>
										           <option value="30020002" <%if("30020002".equals(servId)){ %> selected <%} %>> 办理_套餐销售_换购机</option>
										           <option value="30020003" <%if("30020003".equals(servId)){ %> selected <%} %>> 办理_套餐销售_注销</option>
										           <option value="30020004" <%if("30020004".equals(servId)){ %> selected <%} %>> 办理_套餐销售_预受理</option>
										           <option value="30020005" <%if("30020005".equals(servId)){ %> selected <%} %>> 办理_套餐销售_其他</option>

										           <option value="30040001" <%if("30040001".equals(servId)){ %> selected <%} %>> 办理_基础服务_补换卡</option>
										           <option value="30040002" <%if("30040002".equals(servId)){ %> selected <%} %>> 办理_基础服务_积分兑换</option>
										           <option value="30040003" <%if("30040003".equals(servId)){ %> selected <%} %>> 办理_基础服务_密码操作</option>
										           <option value="30040004" <%if("30040004".equals(servId)){ %> selected <%} %>> 办理_基础服务_实名登记</option>
										           <option value="30040005" <%if("30040005".equals(servId)){ %> selected <%} %>> 办理_基础服务_停复机</option>
										           <option value="30040006" <%if("30040006".equals(servId)){ %> selected <%} %>> 办理_基础服务_信息变更</option>
										           <option value="30040007" <%if("30040007".equals(servId)){ %> selected <%} %>> 办理_基础服务_信用额度调整</option>
										           <option value="30040008" <%if("30040008".equals(servId)){ %> selected <%} %>> 办理_基础服务_业务变更</option>
										           <option value="30040009" <%if("30040009".equals(servId)){ %> selected <%} %>> 办理_基础服务_移机</option>
										           <option value="30040010" <%if("30040010".equals(servId)){ %> selected <%} %>> 办理_基础服务_其它</option>

										          </select>
										          <%}else{ %>
										          <select id="servId"    name="servId" style="width: 180px;" onchange="changeCol(this);">       
										           <option value="10010001" <%if("10010001".equals(servId)){ %> selected <%} %>> 查询_费用查询_话费查询</option>
										           <option value="10010002" <%if("10010002".equals(servId)){ %> selected <%} %>> 查询_费用查询_话费明细</option>
										           <option value="10010003" <%if("10010003".equals(servId)){ %> selected <%} %>> 查询_费用查询_扣费退费</option>
										           <option value="10010004" <%if("10010004".equals(servId)){ %> selected <%} %>> 查询_费用查询_套餐使用情况</option>
										           <option value="10010005" <%if("10010005".equals(servId)){ %> selected <%} %>> 查询_费用查询_余额/赠金</option>
										           <option value="10010006" <%if("10010006".equals(servId)){ %> selected <%} %>> 查询_费用查询_其它</option>
										            
										           <option value="10020001" <%if("10020001".equals(servId)){ %> selected <%} %>> 查询_进度查询_受理进度</option>
										           <option value="10020002" <%if("10020002".equals(servId)){ %> selected <%} %>> 查询_进度查询_报障进度</option>
										           <option value="10020003" <%if("10020003".equals(servId)){ %> selected <%} %>> 查询_进度查询_投诉进度</option>
										           <option value="10020004" <%if("10020004".equals(servId)){ %> selected <%} %>> 查询_进度查询_咨询进度</option>
										           <option value="10020005" <%if("10020005".equals(servId)){ %> selected <%} %>> 查询_进度查询_其它</option>

										           <option value="10030001" <%if("10030001".equals(servId)){ %> selected <%} %>> 查询_积分查询_积分兑换查询</option>
										           <option value="10030002" <%if("10030002".equals(servId)){ %> selected <%} %>> 查询_积分查询_客户积分</option>
										           <option value="10030003" <%if("10030003".equals(servId)){ %> selected <%} %>> 查询_积分查询_其它</option>

										           <option value="10040001" <%if("10040001".equals(servId)){ %> selected <%} %>> 查询_信息查询_PUK/PIN码</option>
										           <option value="10040002" <%if("10040002".equals(servId)){ %> selected <%} %>> 查询_信息查询_客户信息</option>
										           <option value="10040003" <%if("10040003".equals(servId)){ %> selected <%} %>> 查询_信息查询_客户状态</option>
										           <option value="10040004" <%if("10040004".equals(servId)){ %> selected <%} %>> 查询_信息查询_业务信息</option>
										           <option value="10040005" <%if("10040005".equals(servId)){ %> selected <%} %>> 查询_信息查询_其它</option>
										           
										           <option value="10050001" <%if("10050001".equals(servId)){ %> selected <%} %>> 查询_常用查询_WLAN热点</option>
										           <option value="10050002" <%if("10050002".equals(servId)){ %> selected <%} %>> 查询_常用查询_号码归属地</option>
										           <option value="10050003" <%if("10050003".equals(servId)){ %> selected <%} %>> 查询_常用查询_客户经理</option>
										           <option value="10050004" <%if("10050004".equals(servId)){ %> selected <%} %>> 查询_常用查询_区号</option>
										           <option value="10050005" <%if("10050005".equals(servId)){ %> selected <%} %>> 查询_常用查询_营业厅</option>
										           <option value="10050006" <%if("10050006".equals(servId)){ %> selected <%} %>> 查询_常用查询_其它</option>
										          </select>
										          <%} %>
												</td> 
											                  --%><td nowrap width="7%">
											                    <input type="button"   class="poster_btn"  id="queryBtn"  name="queryBtn"   onclick="queryData();" style="width:60px;"   value="查  询" />
											                  </td>
											                  <td width="7%">
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
				//if(totalCount>32000){
				//	alert("数记录数大于32000,无法导出")
				//	return;
				//}	
				var  startTime=$("startTime").value;
				var  endTime=$("endTime").value;
		  	    var  zone=$("zone").value;//文本框
		  	    var  servId=$("servId").value;//文本框
		  	    var  channelId=$("channelId").value;//文本框  
			    var  queryCond="日期从："+startTime+"     至："+endTime+"     区域:"+zone+"   服务条件："+servId;
		 	    $("excelCondition").value=queryCond;   
	     	    dhx.showProgress("正在执行......");
	     	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel_rptChannelIndexDetail.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	}
</script>
<script type="text/javascript" src="rptChannelIndexDetail.js"></script>