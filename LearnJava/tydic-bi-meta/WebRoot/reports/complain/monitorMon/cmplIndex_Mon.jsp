<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date,tydic.reports.complain.monitorDay.CmplIndexDAO" %>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    CmplIndexDAO dao = new CmplIndexDAO();
    String maxMon=dao.getMaxMonth("");
    String dateTime =(String)request.getParameter("dateTime")==null?maxMon:(String)request.getParameter("dateTime");	
    String ind =(String)request.getParameter("ind")==null?"102":(String)request.getParameter("ind");	
    String areaCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
    String zoneCode  =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");	
    	
	List<Map<String, Object>> indList = dao.getIndListMon();
	List<Map<String, Object>> monList = dao.getMonList("");

	request.setAttribute("dateTime", dateTime);
	request.setAttribute("ind", ind);
	request.setAttribute("zoneCode", zoneCode);
%>
<head>
    <title>投诉类指标月监测报表</title>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CmplIndexAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ProdTypeAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/prodTypeTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <script type="text/javascript">
		function impExcel(){
	     	    var url = getBasePath()+"/reports/complain/monitorMon/impExcel/cmplIndexMon_impExcel.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
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
	 <form id="queryform" name="queryform">
	   <input type="hidden" name="title"    value="投诉类指标月监测报表">
	    <input type="hidden" name="colNames" value="大区,地市,指标项,当月值,上月值,环比,同比,本年平均">
	    <table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="980"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
													               <td>&nbsp;&nbsp;&nbsp;月份:
          <select id="dateTime"    name="dateTime" style="width: 80px;">
           <%
           	for (Map<String, Object> key : monList) {
           	if(dateTime.equals(key.get("MONTH_ID"))){
           %>
           <option value="<%=key.get("MONTH_ID")%>" selected><%=key.get("MONTH_ID").toString()%></option>
           <%
              }
           else{
            %>  
           <option value="<%=key.get("MONTH_ID")%>"><%=key.get("MONTH_ID").toString()%></option>
            <%
            }
            }
            %>    
          </select>	
          </td>
           <td>区域:
		   <input type="hidden""  id=zoneCode         name="zoneCode" />
		   <input type="text"    id="zone"            name="zone" /></td>
		   <td>业务类型:
           <input type="hidden""  id="prodTypeCode"         name="prodTypeCode"  />
		   <input type="text"    id="prodType"              name="prodType" /></td>
          </td>
          <td>指标项:
          <select id="ind"    name="ind" style="width: 140px;" onchange="changeCol(this);">
           <%
           	for (Map<String, Object> key : indList) {
           	if(ind.equals(key.get("IND_ID"))){
           %>
           <option value="<%=key.get("IND_ID")%>" selected><%=key.get("IND_NAME").toString()%></option>
           <%
              }
           else{
            %>  
           <option value="<%=key.get("IND_ID")%>"><%=key.get("IND_NAME").toString()%></option>
            <%
            }
            }
            %>    
          </select>		
          </td>
	       <td><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td>&nbsp;&nbsp;&nbsp;&nbsp;<input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导出报表" onclick="impExcel();"         style="width:70px;" /></td>
	      <td style="width:100px;">&nbsp;&nbsp;
	      <input type="button" id="city" name="city" class="poster_btn"  value='切换地市' onclick="lookCity(this);" style="width:70px;"/></td>
													                </tr>
													</table>			        
										    </td>
										</tr>
							  </table>	
                              	<table> <tr> <td>  
	 <div id="chartdiv2" >	               
	 </div>        
	 </td> <td>
	 <div id="chartdiv1">	               
	 </div>               
	 </td>
	 </tr>
	 </table>
	 <div id="right_1">  
	    <div id="chartTable" ></div>  </br>  
	    <div id="index_exp"  class="tips"></div>   
	 </div>      
                          </td>
			       </tr>
	      </table>
	</form>	
   <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript" src="cmplIndex_Mon.js"></script>