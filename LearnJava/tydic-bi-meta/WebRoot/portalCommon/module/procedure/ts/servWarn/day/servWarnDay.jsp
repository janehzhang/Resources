<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,java.util.Calendar,tydic.meta.common.DateUtil,
tydic.portalCommon.procedure.ts.servWarn.day.ServWarnDayAction"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <%@include file="../../../../../public/head.jsp" %>
    <%@include file="../../../../../public/include.jsp" %>
    <title><%=menuName %></title>
    <meta http-equiv="pragma" content="no-cache" />
    <script type="text/javascript" src="<%=path%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ProdTypeAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ServWarnDayAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript" src="<%=path%>/js/Charts/FusionCharts.js"></script> 
    <script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/prodTypeTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <%
      ServWarnDayAction  action= new ServWarnDayAction();
      String  monthEndNextDay=action.getNewDate().substring(0,10);
      String areaCode="0000";
      String zoneCode  =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
      String userCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
    %>
</head>
<body style="overflow: visible;">
	<div id="div_src" style="position: absolute; width: 100%; height:100%;z-index: 1000;top: 0px; left: 0px; background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:scroll;"
	                  title="" class="dragBar">
	  <center style="color: #CCCCCC;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;">数据加载中，请稍等....</center>
   </div>
   <form id="queryform" name="queryform">
        <input type="hidden" id="excelTitle"   name="excelTitle"    value="<%=menuName %>" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"     />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="cid"  name="cid"   />
        <input type="hidden" id="cid1"  name="cid1"   />
           <input type="hidden" id="filePath"    name="filePath" value="/upload/excelHeader/servWarnDay.xls" />
          <input type="hidden" id="row"    name="row" value="4" />
          <input type="hidden" id="defaultZoneCode"    name="defaultZoneCode" value=<%=areaCode %>   />
         <input type="hidden" id="changeCode"    name="changeCode"/>
         <input type="hidden" id="userCode"    name="userCode" value="<%=userCode %>"/> 
        <input type="hidden" id="userCodeData"    name="userCodeData" /> 
         <input type="hidden""  id=zoneCode   value="<%=zoneCode%>"  name="zoneCode" />
	    <table width='100%'  border='0' cellpadding='0px' cellspacing='0px'>
	    
					<tr>
						<td valign="top" width="100%">
							   <table style='border: 1px solid #87CEFF;' width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="1050"  border="0" cellpadding="0" cellspacing="0">
										               <tr>
										                  <td width="4%">日 期:</td>
										                  <td width="14%">
										                      <input     id="dayTime"   name="dayTime"    value="<%=monthEndNextDay %>" readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
										                  </td>
										                  <td width="4%">区 域:</td>
										                  <td width="17%">
															  <input type="hidden"  id="zoneCode"       name="zoneCode" value="<%=zoneCode %>"/>
															  <input type="text"    id="zone"           name="zone"  style="width: 160px;height:18px;line-height:18px;" readonly="readonly"/>
										                  </td> 	
										              	  <td width="7%">产品类型:</td>
										              	  <td width="10%">
														   <input type="hidden""  id="prodTypeCode"    name="prodTypeCode" />
														   <input type="text"     id="prodType"        name="prodType"   style="width: 160px;height:18px;line-height:18px;" readonly="readonly"/>    
		                                                  </td>										              	 
										                  <td nowrap width="6%">
										                     <input type="button"    class="poster_btn" id="queryBtn"  name="queryBtn"     onclick="queryData();"        style="width:60px;"  value="查  询" />
										                  </td>
										                  <td nowrap width="6%">
										                     <input type="button"   class="poster_btn" id="impBtn"    name="impBtn"       onclick="impExcel(this.form);"         style="width:60px;" value="导  出" />
										                  </td>
										                  									                  
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
										<table style='border: 1px solid #87CEFF;' width='100%' height='240px' border='0' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'>
												<span style='font:12px;font-weight:bold;' id='titleInfo2'>当日值</span>
												</td>
											 </tr>
											<tr>
												<td>
													<div id='chartdiv_line'></div>
												</td>
											</tr>
										</table>
									</td>
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='240px' border='0' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'>
												<span style='font:12px;font-weight:bold;' id='titleInfo2'>当日投诉量</span>
												</td>
												<td align='right'>
												<input type='button' id='city' name='city' class='poster_btn1' value='横向对比'  onclick="lookCity(this)" style='width:70px;'/>
												</td>
											 </tr>
											<tr>
												<td colspan="2">
													<div id='chartdiv_bar'></div>
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
		                   <div id="dataTable" style="margin: 0px;padding: 0px;width: 100%;height: auto;">
		                   
		                   </div>
                       </td>
                   </tr>   
	      </table>
     </form>
     <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript">
//业务类型
var   loadProdTypeSelect=function(){
	 var map=new Object();
     ServWarnDayAction.getProdTypeList(map, {callback:function (res) {
				        if (res != null) {
	        	   	       for(var i=0;i<res.length;i++)
						    {
	        	   	    	   var op=new Option(res[i].PROD_TYPE_NAME,res[i].PROD_TYPE_ID);
	        	   	    	   $('prodType').add(op);
						    }
				        }
		   }
		});  
}
function impExcel(form){
		dhx.showProgress("正在执行......");
		exportImage(); 
		window.setTimeout("exportExcel()",8000);
	    window.setTimeout("dhx.closeProgress()",8000);
	}
</script>
<script type="text/javascript" src="servWarnDay.js"></script>