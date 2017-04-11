<%@page contentType="text/html;charset=UTF-8" import="java.util.*,java.text.SimpleDateFormat,java.util.Calendar" language="java" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<%@include file="../../../public/head.jsp" %>
	<head>
		<title>账号列表</title>
	    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/UserAction.js"></script>
	    <script type="text/javascript"  src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
	    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/MenuAction.js"></script>
    	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/UserManageAction.js"></script>
    	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/DeptAction.js"></script>
    	<script type="text/javascript" src="<%=rootPath%>/dwr/interface/RoleAction.js"></script>
    	<script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=APPLY_STATE"></script> 
	    <script type="text/javascript"  src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
	    <script type="text/javascript"  src="<%=rootPath%>/js/common/zoneTree.js"></script>
	    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
	    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css"   />
	    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css" />
	    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css" />
	   <%
	     String groupId = request.getParameter("groupId");
	     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	     Date Nowdate=new Date(); 
	     String  monthFirstDay=  formatter.format(new   Date(Nowdate.getYear(),Nowdate.getMonth(),1));//本月的第一天
		 String	 monthEndNextDay=formatter.format(new Date(Nowdate.getYear(),Nowdate.getMonth(),Nowdate.getDate()));//本月当前天的前一天    
	   %>
	</head>
	<body style="overflow-y: auto">
  <form id="queryform" name="queryform" >
        <input type="hidden" id="excelTitle"   name="excelTitle"    value="账号列表" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   value="用户名,OA账号,地 域,部门,岗位 ,角色,管理员身份,权限菜单"/>
        <input type="hidden" id="excelData"    name="excelData"     />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="excelUName"    name="execlUName"     />
        <input type="hidden" id="excelZone"    name="excelZone"     />
	    <table width='100%'  border='0' cellpadding='0px' cellspacing='0px'>
					<tr>
						<td valign="top" width="100%">
							   <table style='border: 1px solid #87CEFF; ' width='100%'  border='0'  cellpadding='0px' cellspacing='0px' >
									   <tr height='25px' style='background:url(<%=rootPath %>/images/fpage_04.gif);'>
											 <td nowrap align='left' class='title_ma1'>
												查询条件                                  
											 </td>
										</tr>
										<tr>
										     <td >
													<table  width="1000"  border="0" cellpadding="0" cellspacing="0">
										                <tr>
										                  <%-- <td width="5%">开始日期:</td>
										                  <td width="11%">
										                      <input     id="startTime" name="startTime"  value="<%=monthFirstDay %>"  readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
										                  </td>
										                  <td width="5%">结束日期:</td>
										                  <td width="11%">
										                      <input     id="endTime"   name="endTime"    value="<%=monthEndNextDay %>" readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=monthEndNextDay %>'})" >
										                  </td> --%>
										                  <td width="5%">用户名:</td>
										                  <td width="11%">
										                      <input     id="uName"   name="uName"   >
										                  </td>
										                  <td width="3%">区 域:</td>
										                  <td width="14%">
															  <input type="hidden"  id="zoneCode"       name="zoneCode" />
															  <input type="text"    id="zone"           name="zone"  style="width: 160px;height:18px;line-height:18px;" readonly="readonly"/>
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
				//var  startTime=$("startTime").value;//文本框
			   // var  endTime=$("endTime").value;//文本框
			    //构造表格数据
			    if (!createExcelDataAll()){//如果没有数据
			    	if(!confirm("表格没有数据,是否导出空表？")) return;
			     }			    
			   	var uName=$("excelUName").value;
			    var  zone=$("excelZone").value;//文本框
			    var   queryCond="";
			   
			    if(uName == null || uName =="") {queryCond="区域："+zone;}
			   	else {
			   		queryCond="用户名："+ uName +" 区域："+zone;
			   	}		    
		   	    $("excelCondition").value=queryCond;		   	    
	     	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/implExcel.jsp";
				document.forms[0].method = "post";
				document.forms[0].action=url;
				document.forms[0].target="hiddenFrame";
				document.forms[0].submit();
	}
    function dump_obj(myObject) {  
	  var s = "";  
	  for (var property in myObject) {  
	   s = s + "\n "+property +": " + myObject[property] ;  
	  }  
	  alert(s);  
	} 
</script>
<script type="text/javascript"   src="localUser.js"></script>
