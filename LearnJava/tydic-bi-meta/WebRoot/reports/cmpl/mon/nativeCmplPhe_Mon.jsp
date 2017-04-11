<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="tydic.reports.complain.monitorDay.CmplIndexDAO" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%   
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String dateTime=formatter.format(new Date());
	dateTime=DateUtil.addMonth(dateTime, -1);
	request.setAttribute("dateTime",DateUtil.formatToTimeNosce(dateTime));	
    String prodTypeCode=(String)request.getParameter("prodTypeCode")==null?"-1":(String)request.getParameter("prodTypeCode");
    String ind=(String)request.getParameter("ind")==null?"NUM1":(String)request.getParameter("ind");  
    String areaCode="0000";
    String zoneCode  =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
    String userCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
    String cmplBusinessType=(String)request.getParameter("cmplBusinessType")==null?"":(String)request.getParameter("cmplBusinessType");
    CmplIndexDAO dao = new CmplIndexDAO();
	List<Map<String, Object>> businessList = dao.getBusinessList("CS_CMPL_PROBLEM_MON");

	request.setAttribute("zoneCode", zoneCode);
%>
<head>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplIndexAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ProdTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/prodTypeTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript"> 
function impExcel(form){
		dhx.showProgress("正在执行......");
		exportImage(); 
		window.setTimeout("exportExcel()",8000);
	    window.setTimeout("dhx.closeProgress()",8000);
} 
	</script>
	<style type="text/css"> 
		.unl {
	    text-decoration:underline;
		color: #0000FF;
		cursor: pointer;
	}
	</style>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
   <div id="div_src" style="position: absolute; width: 100%; height:100%;z-index: 1000;top: 0px; left: 0px; background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:scroll;"
	                  title="" class="dragBar">
	  <center style="color: #CCCCCC;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;">数据加载中，请稍等....</center>
   </div>
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=menuName %> />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="cid"  name="cid"   />
        <input type="hidden" id="cid1"  name="cid1"   />
        <input type="hidden" id="filePath"    name="filePath" value="/upload/excelHeader/nativeCmplPhe_Mon.xls" />
          <input type="hidden" id="row"    name="row" value="4" />
          <input type="hidden" id="userCode"    name="userCode" value="<%=userCode %>"/> 
          <input type="hidden" id="userCodeData"    name="userCodeData" />
	    <table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%' style='border: 1px solid #87CEFF;'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="1000"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		<td width='15%'>&nbsp;&nbsp;&nbsp;月份:
          <input name="dateTime" id="dateTime" value="${dateTime}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyyMM'})" >
        </td>
           <td width='12%'>&nbsp;&nbsp;区域:
		   <input type="hidden""  id=zoneCode   value=<%=zoneCode%>      name="zoneCode" />
		   <input type="text"    id="zone"    size=15         name="zone" /></td>
          <td width='16%'>&nbsp;&nbsp;投诉现象:
          <select id="cmplBusinessType"    name="cmplBusinessType" style="width: 140px;">
            <option value="" selected>所有</option>
           <%
           	for (Map<String, Object> key : businessList) {
           	if(cmplBusinessType.equals(key.get("CMPL_BUSINESS_TYPE1_ID"))){
           %>
           <option value="<%=key.get("CMPL_BUSINESS_TYPE1_ID")%>" selected><%=key.get("CMPL_BUSINESS_TYPE1_NAME").toString()%></option>
           <%
              }
           else{
            %>  
           <option value="<%=key.get("CMPL_BUSINESS_TYPE1_ID")%>"><%=key.get("CMPL_BUSINESS_TYPE1_NAME").toString()%></option>
            <%
            }
            }
            %>    
          </select>	
          </td>
          <td width='13%'>产品类型:
           <input type="hidden"  id="prodTypeCode"   value=<%=prodTypeCode%>      name="prodTypeCode"  />
		   <input type="text"    id="prodType"      size=18           name="prodType" />
		  </td>
	      <td width='5%'><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"  style="width:60px;" /></td>
	      <td width='5%'>&nbsp;&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"  style="width:60px;" /></td>
	     
													                </tr>
													</table>			        
										    </td>
										</tr>
							  </table>	
 							  </td>
							  </tr>	
			        <!-- 顶部图形 -->	     
			       <tr>
			          <td width="100%"> 			                  
							<table id='topTable' width='100%' height='auto!important' border='0' cellpadding='0px' cellspacing='2px'>
								<tr>
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='200px' border='0' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'><span style='font:12px;font-weight:bold;' id='titleInfo1'></span>
												</td>
											    <td style="width: 180px;">指标项:
									             <select id="ind"    name="ind" style="width: 100px;" onchange="changeCol(this);">
									              <option value="NUM1"  <%if("NUM1".equals(ind)){ %>selected <%} %>>本地投诉量</option>  
									              <option value="NUM3"  <%if("NUM3".equals(ind)){ %>selected <%} %>>本地投诉率</option> 
									             </select>		
									            </td>
									        </tr>											
											<tr>
												<td colspan='2'><%--这个区域有两列，所以该表要跨越两列
												--%><div id='chartdiv2'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='200px' border='0' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'><span style='font:12px;font-weight:bold;' id='titleInfo2'></span>
												</td>
									        </tr>
											<tr>
												<td>
													<div id='chartdiv1'></div>
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
		                   <div id="chartTable" style="margin: 0px;padding: 0px;width: 100%;height: auto;"> 
		                   </div>
		                    <div id="index_exp"  class="tips"></div>   
                       </td>
                   </tr>    
	      </table>
	</form>
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="nativeCmplPhe_Mon.js"></script>