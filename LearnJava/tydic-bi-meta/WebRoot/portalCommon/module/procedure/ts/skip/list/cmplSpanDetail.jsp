<%@page language="java" import="java.util.*,
                                java.text.SimpleDateFormat,
                                java.util.Calendar,
                                tydic.meta.common.DateUtil,
                                tydic.portalCommon.procedure.ts.skip.list.CmplSpanDetailAction"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
 <%
	    //String zoneCode       =(String)request.getParameter("zoneCode");	
	    String monthID      =(String)request.getParameter("dateTime");
	    
        CmplSpanDetailAction action=new CmplSpanDetailAction();
	    String dateTime=action.getNewMonth().substring(0,7);
	    dateTime=dateTime.replaceAll("-","");
	    String[] months =DateUtil.getAddMonths(30,dateTime);
	    
	    String areaCode =((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
	    String zoneCode =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
    %>
<head>
    <%@include file="../../../../../public/head.jsp" %>
    <%@include file="../../../../../public/include.jsp" %>
    <title><%=menuName %></title>
    <meta http-equiv="pragma" content="no-cache" />
    <script type="text/javascript" src="<%=path%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/CmplSpanDetailAction.js"></script>
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
        <!--Excel导出参数  -->
        <%@include file="../include/excelParam.jsp" %>   
        <input type="hidden" id="dataType" name="dataType"   value="1"/> <!-- 越级投诉月监测 -->
	    <table width='100%'  border='0' cellpadding='0px' cellspacing='0px'>
					<tr>
						<td valign="top" width="100%">
							   <table style='border: 1px solid #87CEFF;' width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
										 <tr>
										     <td>
												
												<table id='queryCondition' width="1000"  border="0" cellpadding="0" cellspacing="0">
											                <tr>
																 <td width="2%">月 份:</td>
												                  <td width="10%">
												                      <select id="dateTime" name="dateTime">
												                        <% 
												                       for(String month:months )
												                       { 
												                        	if(monthID !=null && month.equals(monthID) )
												                            {
												                         %>
															              <option value="<%=month %>" selected="selected"><%=month %></option>
															             <% 
															                }
												                        	else
												                        	{ 
															              %>
															              <option value="<%=month %>"><%=month %></option>
															            <%  }
												                        }
															            %> 
												                      </select>
												                  </td>
												                  <td width="2%">区 域:</td>
												                  <td width="10%">
																	  <input type="hidden"  id="zoneCode"       name="zoneCode" />
																	  <input type="text"    id="zone"           name="zone"  style="width: 160px;height:18px;line-height:18px;" readonly="readonly"/>
												                  </td>
												                  <td nowrap width="30%">
												                     <input type="button"    class="poster_btn" id="queryBtn"  name="queryBtn"    onclick="queryData();"        style="width:60px;"  value="查  询"/>
												                      <input type="button"    class="poster_btn" id="impBtn"    name="impBtn"      onclick="impExcel();"         style="width:60px;"  value="导  出"/> 
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
	function impExcel(){
			var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//文本框
		    var  zone=$("zone").value;//文本框
		    var  queryCond="月 份："+dateTime +" 区域："+zone;
		    $("excelCondition").value=queryCond;   
		    dhx.showProgress("正在执行......");
		  	var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel_cmplDetail.jsp";
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
	}
</script>
<script type="text/javascript" src="cmplSpanDetail.js"></script>