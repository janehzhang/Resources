<%@page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<%@include file="../../../public/head.jsp" %>
	<head>
		<title>OA推送账号配置</title>
	    <script type="text/javascript"  src="<%=rootPath%>/dwr/interface/LoginLogAction.js"></script>
	    <script type="text/javascript"  src="<%=rootPath%>/dwr/interface/ZoneAction.js"></script>
	    <script type="text/javascript"  src="<%=rootPath%>/js/common/zoneTree.js"></script>
	    <script type="text/javascript" src="<%=rootPath%>/js/common/page.js"></script>
	    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css"   />
	    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css" />
	    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css" />
	</head>
	<body style="overflow-y: auto;">
  <form id="queryform" name="queryform">
        <input type="hidden" id="excelTitle"   name="excelTitle"    value="OA推送账号配置当前页" />
        <input type="hidden" id="excelHeader"  name="excelHeader"   value="地市,分公司,营销中心,姓名,账号,操作"/>
        <input type="hidden" id="excelData"    name="excelData"     />
        <input type="hidden" id="excelCondition"    name="excelCondition"     />
        <input type="hidden" id="totalCount"    name="totalCount"     />
	    <table width='100%'  border='0' cellpadding='0px' cellspacing='0px'>
					<tr>
						<td valign="top" width="100%">
							   <table style='border: 1px solid #87CEFF;' width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
									   <tr height='25px' style='background:url(<%=rootPath %>/images/fpage_04.gif);'>
											 <td nowrap align='left' class='title_ma1'>
												查询条件                                  
											 </td>
										</tr>
										<tr>
										     <td>
													<table  width="800"  border="0" cellpadding="0" cellspacing="0">
										                <tr>
										                  <td width="3%">姓名:</td>
										                  <td width="11%">
										                       <input     id="staffName"   name="staffName"   >
										                  </td>
										                  <td width="3%">OA账号:</td>
										                  <td width="11%">
										                      <input     id="staffCode"   name="staffCode"   >
										                  </td>
										                  <td width="3%">区 域:</td>
										                  <td width="14%">
															  <input type="hidden"  id="zoneCode"       name="zoneCode" />
															  <input type="text"    id="zone"           name="zone"  style="width: 160px;height:18px;line-height:18px;" readonly="readonly"/>
										                  </td>										                  
										                   <td nowrap width="6%">
										                       <input type="button"  class="poster_btn"  id="queryBtn"  name="queryBtn"   onclick="queryData();" style="width:60px;" value="查  询"/>
										                   </td>
										                   <%--<td width="20%">
														       <input type="button"  class="poster_btn"      onclick="impExcel(this.form);"       style="width:60px;" value="导 出"/>					                    
										                   </td>
										                --%></tr>
													</table>			        
										    </td>
									  </tr>
									  <tr>
										     <td>
													<table  width="100%" height='30px' border="0" cellpadding="0" cellspacing="0" bgcolor='#cde5fd' >
													  <tr >
														  <td width="1%">
					                                         <input type="button"  onclick="applyUser()" style='width:32px;height:26px;background:url(<%=rootPath %>/meta/resource/images/addRole.png);'>
														  </td>
														  <td width="80%">新增</td>
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
			alert("数记录数大于32000,无法导出")
			return;
		}	
		var  staffName=$("staffName").value;//文本框
	    var  staffCode=$("staffCode").value;//文本框
	    var  zone=$("zone").value;//文本框
	    var  queryCond="姓名："+staffName+" OA账号："+staffCode+" 区域："+zone;
 	    var url = getBasePath()+"/portalCommon/module/procedure/impExcel/selfDefine/implExcel_OaUserDetail.jsp";
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
<script type="text/javascript"   src="OaConfig.js"></script>
