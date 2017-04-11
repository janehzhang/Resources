<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="tydic.reports.customerSatisfied.CustomerSatisfiedDAO"%>
<%@ page import="tydic.portalCommon.DateUtil" %>
<%@ page import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<%
    String vTypeId=(String)request.getParameter("vTypeId");
    String dateType="2";
	String month =(String)request.getParameter("dateTime");
	String demension =(String)request.getParameter("demension");
	CustomerSatisfiedDAO dao=new CustomerSatisfiedDAO();
	if(month==null || month.equals("")){
		month=dao.getMaxWeek_EveryTypeDemension(dateType,vTypeId,demension);
	}
    String excName="";
    String filePath="";
     if("0".equals(vTypeId)){//宽带新装
    	if("0".equals(demension)){//客户在网时长
    		excName="宽带新装满意度月报_在网时长";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonOnLine.xls";
    	}if("1".equals(demension)){//受理渠道
    		excName="宽带新装满意度月报_渠道";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonChannel.xls";
    	}if("2".equals(demension)){//客户群长
    		excName="宽带新装满意度月报_客户群";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGroup.xls";
    	}if("3".equals(demension)){//客户等级
    		excName="宽带新装满意度月报_客户等级";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGrade.xls";
    	}if("4".equals(demension)){//ARPU值
    		excName="宽带新装满意度月报_ARPU值";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonARPU.xls";
    	}if("5".equals(demension)){//套餐类型
    		excName="宽带新装满意度月报_套餐类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonMeal.xls";
    	}if("6".equals(demension)){//装维员(营业员)
    		excName="宽带新装满意度月报_服务人员";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonInstaller.xls";
    	}if("9".equals(demension)){//产品类型
    		excName="宽带新装满意度月报_产品类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonProdType.xls";
    	}
    }if("1".equals(vTypeId)){//宽带修障
    	if("0".equals(demension)){//客户在网时长
    		excName="宽带修障满意度月报_在网时长";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonOnLine.xls";
    	}if("1".equals(demension)){//受理渠道
    		excName="宽带修障满意度月报_渠道";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonChannel.xls";
    	}if("2".equals(demension)){//客户群长
    		excName="宽带修障满意度月报_客户群";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGroup.xls";
    	}if("3".equals(demension)){//客户等级
    		excName="宽带修障满意度月报_客户等级";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGrade.xls";
    	}if("4".equals(demension)){//ARPU值
    		excName="宽带修障满意度月报_ARPU值";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonARPU.xls";
    	}if("5".equals(demension)){//套餐类型
    		excName="宽带修障满意度月报_套餐类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonMeal.xls";
    	}if("6".equals(demension)){//装维员(营业员)
    		excName="宽带修障满意度月报_服务人员";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonInstaller.xls";
    	}if("9".equals(demension)){//产品类型
    		excName="宽带修障满意度月报_产品类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonProdType.xls";
    	}
    }
   if("2".equals(vTypeId)){//营业厅服务
	    if("0".equals(demension)){//客户在网时长
   		    excName="营业厅服务满意度月报_在网时长";
          	filePath="/upload/excelHeader/serviceSatisfiedHB_MonOnLine.xls";
	   	}if("1".equals(demension)){//受理渠道
	   		excName="营业厅服务满意度月报_渠道";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonChannel.xls";
	   	}if("2".equals(demension)){//客户群长
	   		excName="营业厅服务满意度月报_客户群";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGroup.xls";
	   	}if("3".equals(demension)){//客户等级
	   		excName="营业厅服务满意度月报_客户等级";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGrade.xls";
	   	}if("4".equals(demension)){//ARPU值
	   		excName="营业厅服务满意度月报_ARPU值";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonARPU.xls";
	   	}if("5".equals(demension)){//套餐类型
	   		excName="营业厅服务满意度月报_套餐类型";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonMeal.xls";
	   	}if("6".equals(demension)){//装维员(营业员)
	   		excName="营业厅服务满意度月报_服务人员";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonInstaller.xls";
	   	}if("9".equals(demension)){//产品类型
    		excName="营业厅服务满意度月报_产品类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonProdType.xls";
    	}if("10".equals(demension)){//终端类型
    		excName="营业厅服务满意度月报_终端类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonTerminal.xls";
    	}
    }if("5".equals(vTypeId)){//10000号
    	if("0".equals(demension)){//客户在网时长
    		excName="10000号服务满意度月报_在网时长";
        	filePath="/upload/excelHeader/serviceSatisfied10000_MonOnLine.xls";
    	}if("1".equals(demension)){//受理渠道
    		excName="10000号服务满意度月报_渠道";
        	filePath="/upload/excelHeader/serviceSatisfied10000_MonChannel.xls";
    	}if("2".equals(demension)){//客户群长
    		excName="10000号服务满意度月报_客户群";
        	filePath="/upload/excelHeader/serviceSatisfied10000_MonGroup.xls";
    	}if("3".equals(demension)){//客户等级
    		excName="10000号服务满意度月报_客户等级";
        	filePath="/upload/excelHeader/serviceSatisfied10000_MonGrade.xls";
    	}if("4".equals(demension)){//ARPU值
    		excName="10000号服务满意度月报_ARPU值";
        	filePath="/upload/excelHeader/serviceSatisfied10000_MonARPU.xls";
    	}if("5".equals(demension)){//套餐类型
    		excName="10000号服务满意度月报_套餐类型";
        	filePath="/upload/excelHeader/serviceSatisfied10000_MonMeal.xls";
    	}if("6".equals(demension)){//话务员
    		excName="10000号服务满意度月报_服务人员";
        	filePath="/upload/excelHeader/serviceSatisfied10000_MonInstaller.xls";
    	}if("7".equals(demension)){//测评方式
	   		excName="10000号服务满意度日报_测评方式";
	       	filePath="/upload/excelHeader/serviceSatisfied10000_MonTmethod.xls";
	   	}if("8".equals(demension)){//业务类型
    		excName="10000号服务满意度月报_业务类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonBusiness.xls";
    	}if("9".equals(demension)){//产品类型
    		excName="10000号服务满意度月报_产品类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonProdType.xls";
    	}if("10".equals(demension)){//终端类型
    		excName="10000号服务满意度月报_终端类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonTerminal.xls";
    	}
    }if("6".equals(vTypeId)){//网厅
    	if("0".equals(demension)){//客户在网时长
    		excName="网厅服务满意度月报_在网时长";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonOnLine.xls";
    	}if("1".equals(demension)){//受理渠道
    		excName="网厅服务满意度月报_渠道";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonChannel.xls";
    	}if("2".equals(demension)){//客户群长
    		excName="网厅服务满意度月报_客户群";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGroup.xls";
    	}if("3".equals(demension)){//客户等级
    		excName="网厅服务满意度月报_客户等级";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGrade.xls";
    	}if("4".equals(demension)){//ARPU值
    		excName="网厅服务满意度月报_ARPU值";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonARPU.xls";
    	}if("5".equals(demension)){//套餐类型
    		excName="网厅服务满意度月报_套餐类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonMeal.xls";
    	}if("9".equals(demension)){//产品类型
    		excName="网厅服务满意度月报_产品类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonProdType.xls";
    	}if("10".equals(demension)){//终端类型
    		excName="网厅服务满意度月报_终端类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonTerminal.xls";
    	}
    }if("7".equals(vTypeId)){//掌厅
    	if("0".equals(demension)){//客户在网时长
    		excName="掌厅服务满意度月报_在网时长";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonOnLine.xls";
    	}if("1".equals(demension)){//受理渠道
    		excName="掌厅服务满意度月报_渠道";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonChannel.xls";
    	}if("2".equals(demension)){//客户群长
    		excName="掌厅服务满意度月报_客户群";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGroup.xls";
    	}if("3".equals(demension)){//客户等级
    		excName="掌厅服务满意度月报_客户等级";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGrade.xls";
    	}if("4".equals(demension)){//ARPU值
    		excName="掌厅服务满意度月报_ARPU值";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonARPU.xls";
    	}if("5".equals(demension)){//套餐类型
    		excName="掌厅服务满意度月报_套餐类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonMeal.xls";
    	}if("9".equals(demension)){//产品类型
    		excName="掌厅服务满意度月报_产品类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonProdType.xls";
    	}if("10".equals(demension)){//终端类型
    		excName="掌厅服务满意度月报_终端类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonTerminal.xls";
    	}
    } if("8".equals(vTypeId)){//号百
    	if("0".equals(demension)){//客户在网时长
	   		excName="号百服务满意度月报_在网时长";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonOnLine.xls";
	   	}if("1".equals(demension)){//受理渠道
	   		excName="号百服务满意度月报_渠道";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonChannel.xls";
	   	}if("2".equals(demension)){//客户群长
	   		excName="号百服务满意度月报_客户群";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGroup.xls";
	   	}if("3".equals(demension)){//客户等级
	   		excName="号百服务满意度月报_客户等级";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGrade.xls";
	   	}if("4".equals(demension)){//ARPU值
	   		excName="号百服务满意度月报_ARPU值";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonARPU.xls";
	   	}if("5".equals(demension)){//套餐类型
	   		excName="号百服务满意度月报_套餐类型";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonMeal.xls";
	   	}if("6".equals(demension)){//装维员(营业员)
	   		excName="号百服务满意度月报_服务人员";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonInstaller.xls";
	   	}if("7".equals(demension)){//测评方式
	   		excName="号百服务满意度月报_测评方式";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonTmethod.xls";
	   	}if("8".equals(demension)){//业务类型
	   		excName="号百服务满意度月报_业务类型";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonBusiness.xls";
	   	}if("9".equals(demension)){//产品类型
    		excName="号百服务满意度月报_产品类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonProdType.xls";
    	}if("10".equals(demension)){//终端类型
    		excName="号百服务满意度月报_终端类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonTerminal.xls";
    	}
   }if("9".equals(vTypeId)){//实体渠道评价器
	    if("0".equals(demension)){//客户在网时长
   		    excName="实体渠道评价器服务满意度月报_在网时长";
          	filePath="/upload/excelHeader/serviceSatisfiedHB_MonOnLine.xls";
	   	}if("1".equals(demension)){//受理渠道
	   		excName="实体渠道评价器服务满意度月报_渠道";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonChannel.xls";
	   	}if("2".equals(demension)){//客户群长
	   		excName="实体渠道评价器服务满意度月报_客户群";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGroup.xls";
	   	}if("3".equals(demension)){//客户等级
	   		excName="实体渠道评价器服务满意度月报_客户等级";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGrade.xls";
	   	}if("4".equals(demension)){//ARPU值
	   		excName="实体渠道评价器服务满意度月报_ARPU值";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonARPU.xls";
	   	}if("5".equals(demension)){//套餐类型
	   		excName="实体渠道评价器服务满意度月报_套餐类型";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonMeal.xls";
	   	}if("6".equals(demension)){//装维员(营业员)
	   		excName="实体渠道评价器服务满意度月报_服务人员";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonInstaller.xls";
	   	}if("9".equals(demension)){//产品类型
    		excName="实体渠道评价器服务满意度月报_产品类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonProdType.xls";
    	}if("10".equals(demension)){//终端类型
    		excName="实体渠道评价器服务满意度月报_终端类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonTerminal.xls";
    	}
    }if("10".equals(vTypeId)){//实体渠道短信
	    if("0".equals(demension)){//客户在网时长
   		    excName="实体渠道短信服务满意度月报_在网时长";
          	filePath="/upload/excelHeader/serviceSatisfiedHB_MonOnLine.xls";
	   	}if("1".equals(demension)){//受理渠道
	   		excName="实体渠道短信服务满意度月报_渠道";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonChannel.xls";
	   	}if("2".equals(demension)){//客户群长
	   		excName="实体渠道短信服务满意度月报_客户群";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGroup.xls";
	   	}if("3".equals(demension)){//客户等级
	   		excName="实体渠道短信服务满意度月报_客户等级";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonGrade.xls";
	   	}if("4".equals(demension)){//ARPU值
	   		excName="实体渠道短信服务满意度月报_ARPU值";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonARPU.xls";
	   	}if("5".equals(demension)){//套餐类型
	   		excName="实体渠道短信服务满意度月报_套餐类型";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonMeal.xls";
	   	}if("6".equals(demension)){//装维员(营业员)
	   		excName="实体渠道短信服务满意度月报_服务人员";
	       	filePath="/upload/excelHeader/serviceSatisfiedHB_MonInstaller.xls";
	   	}if("9".equals(demension)){//产品类型
    		excName="实体渠道短信服务满意度月报_产品类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonProdType.xls";
    	}if("10".equals(demension)){//终端类型
    		excName="实体渠道短信服务满意度月报_终端类型";
        	filePath="/upload/excelHeader/serviceSatisfiedHB_MonTerminal.xls";
    	}
    }
    String areaCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
    String zoneCode =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
    List<Map<String, Object>> monthList=dao.getDateTimeList_EveryTypeDemension(dateType,vTypeId,demension);
%>
<head>
    <title><%=excName %></title>
    <%@include file="../../../portalCommon/public/head.jsp"%>
    <%@include file="../../../portalCommon/public/include.jsp" %>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/Charts/FusionCharts.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ReportConfigAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/warning.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <script type="text/javascript">
		function impExcel(){
			var actType   =$("vTypeId").value;
			var demension   =$("demension").value;
			var url="";
		    if(actType=='8'){//号百
			    if(demension=='8'){//业务类型
			    	 url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel.jsp";
			    }else{
			    	url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcelDemension.jsp";
			    }
		    }else{
		    	url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcelDemension.jsp"; 
		    }   
			dhx.showProgress("正在执行......");
			var  dateTime=$("dateTime").options[$("dateTime").selectedIndex].text;//时间
		    var  zone=$("zone").value;//区域
		    var queryCond="月份："+dateTime+"    区域："+zone;
	   	    $("excelCondition").value=queryCond;    
			document.forms[0].method = "post";
			document.forms[0].action=url;
			document.forms[0].target="hiddenFrame";
			document.forms[0].submit();
			window.setTimeout("dhx.closeProgress()",2000);
	    }    
	</script>
</head>
<body style="width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
	 <form id="queryform" name="queryform">
	   <input type="hidden" id="excelTitle"   name="excelTitle"    value=<%=excName%> />
        <input type="hidden" id="excelHeader"  name="excelHeader"   />
        <input type="hidden" id="excelData"    name="excelData"   />  
        <input type="hidden" id="totalCount"   name="totalCount"    /> 
        <input type="hidden" id="vTypeId"    name="vTypeId"  value=<%=vTypeId%> />  
         <input type="hidden" id="demension"    name="demension"  value=<%=demension%> /> 
         <input type="hidden" id="dateType"    name="dateType"  value=<%=dateType%> /> 
         <input type="hidden" id="excelCondition"    name="excelCondition"     />
         <input type="hidden" id="filePath"    name="filePath" value=<%=filePath %> />
          <input type="hidden" id="row"    name="row" value="4" /> 
				<table width='100%' >
					<tr>
						<td valign="top" width="100%">
							   <table width='100%' style='border: 1px solid #87CEFF;' border='0'  cellpadding='0px' cellspacing='0px'>
										<tr>
										     <td>
													<table  width="600"  border="0" cellpadding="0" cellspacing="0">
													                <tr>
		<td style="width: 150px;">&nbsp;&nbsp;&nbsp;月份:
          <select id="dateTime"    name="dateTime" style="width: 100px;">
           <% for (Map<String, Object> key : monthList) {  
           %>
               <% if(((String)key.get("DATE_NO")).equals(month) ){ %>
                  <option value="<%=key.get("DATE_NO") %>" selected="selected"><%=key.get("DATE_NO").toString() %></option>
               <% } else{%>   
                  <option value="<%=key.get("DATE_NO") %>"><%=key.get("DATE_NO").toString() %></option>
               <% }%> 
           <% }%>    
          
          </select>	
          </td>
           <td style="width: 150px;">区域:
		   <input type="hidden""  id="zoneCode"  value="<%=zoneCode%>" name="zoneCode" />
		   <input type="text"     id="zone"  size="17"      name="zone" /></td>
	       <td style="width: 70px;"><input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();"    style="width:60px;" /></td>
	     <td style="width: 70px;"><input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出" onclick="impExcel();"         style="width:60px;" /></td>
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
<script type="text/javascript" src="serviceSatisfied_Mon.js"></script>