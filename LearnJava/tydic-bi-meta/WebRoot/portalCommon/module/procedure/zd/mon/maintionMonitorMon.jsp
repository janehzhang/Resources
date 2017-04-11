<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,java.util.Calendar,tydic.portalCommon.DateUtil,tydic.portalCommon.procedure.zd.mon.MaintionMonitorMonAction,tydic.portalCommon.procedure.zd.mon.MaintionMonitorMonDAO"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta http-equiv="pragma" content="no-cache" />
    <%@include file="../../../../public/head.jsp" %>
    <%@include file="../../../../public/include.jsp" %>
    <title><%=menuName %></title>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/MaintionMonitorMonAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/dwr/interface/CustomerSatisfiedAction.js"></script>
    <script type="text/javascript" src="<%=path%>/js/Charts/FusionCharts.js"></script> 
    <script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <%
     /**
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      String dateTime=formatter.format(new Date());
             dateTime=DateUtil.addMonth(dateTime, -1);
      String[] months =DateUtil.getAddMonths(28,dateTime);
      **/
      MaintionMonitorMonAction action=new MaintionMonitorMonAction();
      String dateTime=action.getNewMonth().substring(0,7);
      dateTime=dateTime.replaceAll("-","");
      dateTime =(String)request.getParameter("dateTime")==null?dateTime:(String)request.getParameter("dateTime");	
      String[] months =DateUtil.getAddMonths(28,dateTime);
      String areaCode="0000";
      String zoneCode  =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
      String userCode=((Map)request.getSession().getAttribute("zoneInfo")).get("zoneCode").toString();
      String prodType=(String)request.getParameter("prodType")==null?"":(String)request.getParameter("prodType");
      String indexId=(String)request.getParameter("indexId")==null?"1":(String)request.getParameter("indexId");
      
      MaintionMonitorMonDAO dao = new MaintionMonitorMonDAO();
      List<Map<String, Object>> indexIDlist = dao.getIndexList();
    %>
</head>
<script type="text/javascript">
	   var prodType="<%=prodType %>";
	   prodType=(prodType!='null' && prodType!="")?prodType:"";
	   var indexId="<%=indexId %>";
	   indexId=(indexId!='null' && indexId!="")?indexId:"1";
	</script>
<body style="overflow: visible;">
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
        <input type="hidden" id="defaultZoneCode"    name="defaultZoneCode" value=<%=areaCode %>   />
        <input type="hidden" id="changeCode"    name="changeCode"/>
        <input type="hidden" id="userCode"    name="userCode" value="<%=userCode %>"/> 
        <input type="hidden" id="userCodeData"    name="userCodeData" />
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
													<table  width="1050"  border="0" cellpadding="0" cellspacing="0">
										               <tr>
										                  <td width="4%">月 份:</td>
										                  <td width="14%">
										                      <select id="dateTime" name="dateTime">
										                        <% for(String month:months ){  %>
													              <option value="<%=month %>"><%=month %></option>
													            <%  }%> 
										                      </select>
										                  </td>
										                  <td width="4%">区 域:</td>
										                  <td width="17%">
															  <input type="hidden"  id="zoneCode"  value="<%=zoneCode %>"     name="zoneCode" />
															  <input type="text"    id="zone"           name="zone"  style="width: 160px;height:18px;line-height:18px;" readonly="readonly"/>
										                  </td>
										                  <td width="6%">业务类型:</td>
										                  <td width="12%">
										                      <select id="prodType" name="prodType">
										                          <option value="">==请选择==</option>
										                      </select>
										                  </td>  
										                  <%--
										                  <td width="6%">指标项:</td>
										                  <td width="14%">
										                      <select id="indexId" name="indexId" onchange="changeImage(this)">
										                         <!--   <option value="">==请选择==</option> -->
										                      </select>
										                  </td>                            
										                  --%>
										                  <td nowrap width="6%">
										                     <input type="button"    class="poster_btn" id="queryBtn"  name="queryBtn"   onclick="queryData();"        style="width:60px;"  value="查  询"/>
										                  </td>
										                  <td nowrap width="6%">
										                     <input type="button"   class="poster_btn" id="impBtn"    name="impBtn"     onclick="impExcel(this.form);"         style="width:60px;"  value="导  出"/>
										                  </td>
										                  <%--
										                  <td nowrap width="20%">
										                     <input type="button"    class='poster_btn' id='city'     name='city'        onclick="lookCity()"          style='width:60px;' value="切换地市"/>
										                  </td>										                  
										                --%>
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
											<tr>
											   <tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'><span style='font:12px;font-weight:bold;' id='titleInfo1'></span>
												</td>
												<td align='right'>指标项：
												   <select id="indexId" name="indexId"  onchange="changeImage(this)">
														<%
											           	for (Map<String, Object> key : indexIDlist) {
											           	if(indexIDlist.equals(key.get("IND_ID"))){
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
							                  </tr>
											<tr>
												<td colspan='2'>
													<div id='chartdiv_line'></div>
												</td>
											</tr>												
										</table>
									</td>
									<td width='50%'>
										<table style='border: 1px solid #87CEFF;' width='100%' height='200px' border='0' cellpadding='0' cellspacing='0'>
											<tr height='20px' style='background:url(/tydic-bi-meta/images/fpage_04.gif);'>
												<td nowrap align='left' class='title_ma1'>
												<span style='font:12px;font-weight:bold;' id='titleInfo2'></span>
												</td>
												<td align='right'>
												<input type='button' id='city' name='city' class='poster_btn1' value='横向对比'  onclick="lookCity(this)" style='width:70px;'/>
												</td>
											</tr>
											<tr>
												<td colspan='2'>
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
function dump_obj(myObject) {  
	  var s = "";  
	  for (var property in myObject) {  
	   s = s + "\n "+property +": " + myObject[property] ;  
	  }  
	  alert(s);  
	} 
//月 份
/**
  var   loadMonthSelect=function(){
	 var map=new Object();
     MaintionMonitorMonAction.getMonthList(map, {callback:function (res) {
				        if (res != null) {
	        	   	       for(var i=0;i<res.length;i++)
						    {
	        	   	    	   var op=new Option(res[i].MONTH_ID,res[i].MONTH_ID);
	        	   	    	   $('dateTime').add(op);
						    }
				        }
		   }
		});
}**/
//业务类型
var   loadProdTypeSelect=function(){
	 var map=new Object();
     MaintionMonitorMonAction.getProdTypeList(map, {callback:function (res) {
				        if (res != null) {
	        	   	       for(var i=0;i<res.length;i++)
						    {
	        	   	    	   var op=new Option(res[i].PROD_TYPE_NAME,res[i].PROD_TYPE_ID);
	        	   	    	   if(res[i].PROD_TYPE_ID==prodType){op.setAttribute("selected",true);}
	        	   	    	   $('prodType').add(op);
						    }
				        }
		   }
		});  
} 
//指标类型
<%--var   loadIndexSelect=function(){
	 var map=new Object();
     MaintionMonitorMonAction.getIndexList(map, {callback:function (res) {
				        if (res != null) {
	        	   	       for(var i=0;i<res.length;i++)
						    {
	        	   	    	   var op=new Option(res[i].IND_NAME,res[i].IND_ID);
	        	   	    	   if(res[i].IND_ID==indexId){op.setAttribute("selected",true);}
	        	   	    	    $('indexId').add(op);
						    }
				        }
		   }
		});
}
//控制地市按钮权限
var controlCityButton=function()
{
   if(userInfo['localCode']!='0000') { //不是广东省  （钻取权限控制）
      var array=document.getElementsByName('city');
       for(var i=0;i<array.length;i++){ 
               array[i].style.display = "none";
         }
    }
}--%>
function impExcel(form){
		dhx.showProgress("正在执行......");
		exportImage(); 
		window.setTimeout("exportExcel()",8000);
	    window.setTimeout("dhx.closeProgress()",8000);
} 
</script>
<script type="text/javascript" src="maintionMonitorMon.js"></script>