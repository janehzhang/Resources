<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,
                                java.util.Calendar,
                                tydic.meta.common.DateUtil,
                                tydic.meta.module.mag.role.RoleAction,tydic.meta.module.mag.role.RoleDAO"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title>管理层</title>
    <meta http-equiv="pragma" content="no-cache" />
    <%@include file="../../public/head.jsp" %>
    <%@include file="../../public/include.jsp" %>
    <script type="text/javascript" src="openLayer.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/UserAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/PortalCommonCtrlr.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/MenuVisitLogAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ZoneAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/SerProManageAction.js"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/DeptAction.js"></script>
    <script type="text/javascript" src="<%=path%>/portal/resource/js/TableMouse.js"></script>
    <script type="text/javascript" src="<%=path%>/portal/resource/js/formatNumber.js"></script>
    <script type="text/javascript" src="<%=path%>/portal/resource/js/Drag.js"></script>
    <script type="text/javascript" src="<%=path%>/js/Charts/FusionCharts.js"></script> 
    <script type="text/javascript" src="<%=path%>/dwr/interface/PortalInstructionAction.js"></script>
    <script type="text/javascript" src="<%=path%>/meta/public/code.jsp?types=NOTICE_LEVEL,NOTICE_FUNCITON,IS_SHOW"></script>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ReportConfigAction.js"></script>
    <script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/js/common/zoneTree.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <!-- 样 式文件 -->
    <style type="text/css"> 
	.gonggao{ width:auto; margin:30px auto;}
	.gonggao li{ height:25px; line-height:25px; list-style:none; border-bottom:1px dotted #dadada;}
	.gonggao li span{ height:25px;line-height:25px;float:right; margin-right:50px}
	.gonggao li a:link{ padding-left:16px;height:25px;line-height:25px; text-decoration:none; color:#737576;}
	.gonggao li a:visited{ padding-left:16px;height:25px;line-height:25px; text-decoration:none; color:#737576;}
	.gonggao li a:hover{ padding-left:16px;height:25px;line-height:25px; text-decoration:none; color:#0096ff;}
	.gonggao li a:active{ padding-left:16px;height:25px;line-height:25px; text-decoration:none; color:#0096ff;}
	
	.page{ float:right; margin:10px 20px;}
	.page a:link{ padding:5px 7px; margin:0 5px; text-decoration:none; border:1px solid #d6d6d6; color:#006a92;}
	.page a:visited{ padding:5px 7px; margin:0 5px; text-decoration:none; border:1px solid #3090bc; color:#fff; background:#73b2da;}
	.page a:hover{ padding:5px 7px; margin:0 5px; text-decoration:none; border:1px solid #3090bc;color:#fff; background:#73b2da;}
	.page a:active{ padding:5px 7px; margin:0 5px; text-decoration:none; border:1px solid #d6d6d6; color:#006a92;}

    .show_content2{ width:500px; margin:5px auto; text-align:center;}
	.gg_title2{ font-size:12px; font-weight:bold; color:#0182dd;margin:10px auto;}
	.xian2{ width:90%; margin:0 auto; height:1px; overflow:hidden; background:#0082de;}
	.gg_info2{ width:auto; margin-left:15%;}
	.gg_info3{ width:auto; text-align: right;}
	.gg_info2 li{ float:left; list-style:none; height:25px; line-height:25px; text-align:left; color:#9c9b9b; margin-right:25px;}
	.gg_content2{ text-align:left; padding-left:30px; margin-top:10px; }
	.closed2{ float:right; display:block; width:50px; height:22px; line-height:25px; text-decoration:none; cursor:pointer; color:#333; margin-top:-5px;}
	.clear2{ clear:both;}
    </style>
    <style type="text/css">
    .title {font-weight: bold;}
	.poster_btn {
		cursor: pointer;
		display: inline-block;
		font-size: 12px;
		height: auto;
		line-height: 18px;
		overflow: hidden;
		right: 0;
		padding-bottom: 1px;
		margin: 5px 5px 5px 5px;
		border-radius: 2px;
		color: black;
		border: 1px solid #198EB4;
		background-color: #D6E8FF;
		background-position: 0 0 !important;
	}
    .poster_btn1 {
		cursor: pointer;
		display: inline-block;
		font-size: 12px;
		height: auto;
		overflow: hidden;
		color: black;
		border: 1px solid #198EB4;
		background-color: #D6E8FF;
		background-position: 0 0 !important;
	}

	#left_1 {
		height: 220px;
		width: 100%;
		border: 1px solid #87CEFF;
	}
	.u_1 {
		width: 99%;
		height: auto;
		magin: 0px;
		padding: 0px;
		float: left;
		display: inline;
		list-style: none;
		margin-left: 0px
	}
	.li_1 {
		list-style-position: outside;
		list-style-type: none;
		background-image: url(<%=path%>/images/u72_normal.gif);
		background-repeat: no-repeat;
		background-position: 0px;
		padding-left: 20px;
		margin-top: 5px
	}
	#left_2 {
		height: 180px;
		width: 100%;
		border: 1px solid #87CEFF;
		margin-top: 2px;
	}
	.u_2 {
		width: 99%;
		height: auto;
		magin: 0px;
		padding: 0px;
		float: left;
		display: inline;
		list-style: none;
		margin-left: 0px
	}
	.li_2 {
		width: 100%;
		list-style-position: outside;
		list-style-type: none;
		margin-top: 0px;
		display: inline;
	}
    .li_2 span {
		color: blue;
		float: right;
	}
	#u156_line {
		width: 100%;
		height: 2px;
		background-image: url(<%=path%>/images/u87.png);
		background-repeat: no-repeat;
	}
	#left_3 {
		height: 120px;
		width: 100%;
		border: 1px solid #87CEFF;
		margin-top: 2px;
	}
	#left_4 {
		height: 100px;
		width: 100%;
		border: 1px solid #87CEFF;
		margin-top: 2px;
	}
	#left_5 {
		height: 160px;
		width: 100%;
		border: 1px solid #87CEFF;
		margin-top: 2px;
	}
	#chartdiv {
		border: 1px solid #87CEFF;
	}
	.unl {
	    text-decoration:underline;
		color: #0000FF;
		cursor: pointer;
	}
	#chartTitle {
		float: left;
		font: 14px;
		font-weight: bold;
	}
	#psButton {
		float: right;
		margin: 0;
		padding: 0;
	}
	#top,#foot {
		margin: 0 auto;
		padding: 0;
		width: 100%;
		height: auto !important;
	}
	#right_1 {
		margin: 0 0 0 2px;
		padding: 0;
		width: 100%;
		height: auto !important;
		border: 1px solid #87CEFF;
	}
	.tips {
		margin: 0 auto;
		padding: 0;
		width: 100%;
		height: auto !important;
	}
	.tab_01 {
		margin: 0 auto;
		padding: 0;
		border-top: #a9b8cd solid 1px;
		border-left: #a9b8cd solid 1px;
		text-align: center;
	}
	.tab_01 tbody tr td {
		padding: 3px 4px;
		border-top: #FFF solid 1px;
		border-left: #FFF solid 1px;
		border-right: #a9b8cd solid 1px;
		border-bottom: #a9b8cd solid 1px;
	}
   select{width:123px;height: 20px;line-height:23px;}
	</style>
	<style type="text/css">
		    a:link,a:visited{text-decoration:none;  /*超链接无下划线*/}
		    a:hover{ text-decoration:underline;  /*鼠标放上去有下划线*/}
	</style>
    <%
        RoleAction  roleCtr= new RoleAction();
    	Map<String, Object> sysInfo = (Map<String, Object>) session.getAttribute(LoginConstant.SESSION_META_SYSTEM_INFO);
    	String systemId = sysInfo.get("groupId").toString();
    	if (systemId == null) {//如果系统ID为null，从Session中获取当前用户的默认系统ID
    		Map<String, Object> userInfo = (Map<String, Object>) session.getAttribute(LoginConstant.SESSION_KEY_USER);
    		systemId = userInfo.get("groupId") == null ?  String.valueOf(Constant.DEFAULT_META_SYSTEM_ID) : userInfo.get("groupId").toString();
    	}
    	out.println("<script type=\"text/javascript\">var getSystemId=function(){var systemId='"
    			+ (systemId == null? Constant.DEFAULT_META_SYSTEM_ID: systemId)+ "'; "
    			+ "return parseInt(systemId||1);}</script>");//打印一段JS代码，用于返回当前systemId
       Map<String, Object> userInfo = (Map<String, Object>) session.getAttribute(LoginConstant.SESSION_KEY_USER);
       String userId=userInfo.get("userId")== null ?  "" : userInfo.get("userId").toString();
       String tempUser="";
       RoleDAO roleDAO=new RoleDAO();
       List<Map<String, Object>> roleIdList=roleDAO.queryRoleByUserId(userId);
       for (Map<String, Object> key : roleIdList) {
    	   if("213762".equals(key.get("ROLE_ID").toString())||key.get("ROLE_ID").toString()=="213762"){//省公司管理员
    		    tempUser="213762";
	       }if("213763".equals(key.get("ROLE_ID").toString())||key.get("ROLE_ID").toString()=="213763"){//分公司管理员
	    	    tempUser="213763";
	       }
       }
    %>
    <%
    	//Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
    	//calendar.add(Calendar.DATE, -1); //得到前一天
    	String newDate=  (String)request.getParameter("newDate");
    	String newMonth= (String)request.getParameter("newMonth");
    	newMonth=newMonth.replaceAll("-","");
    	String yestedayDate = newDate;
    	//String yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    	String[] months=DateUtil.getAddMonths(16,newMonth);
    	String areaCode="0000";
    	String zoneCode  =(String)request.getParameter("zoneCode")==null?areaCode:(String)request.getParameter("zoneCode");
    %>
</head>
<body style="overflow: visible;">
   <div id="div_src" style="position: absolute; width: 100%; height:100%;z-index: 1000;top: 0px; left: 0px; background-color:#F0F0F0;color:#00000;  border:1px solid #6699cc;  overflow-y:scroll;"
	                  title="" class="dragBar">
	    <center style="color: #CCCCCC;vertical-align: middle;font-size: 24px;height: 100%;line-height:20;">数据加载中，请稍等....</center>
   </div>
   <form id="queryform" name="queryform"   action=""  method="post">
        <input type="hidden" id="typeId" name="typeId" />
        <input type="hidden" id="userNameen" name="userNameen"  value="<%=userInfo.get("userNameen").toString() %>"/>
         <input type="hidden" id="defaultZoneCode"    name="defaultZoneCode" value=<%=areaCode %>   />
         <input type="hidden" id="changeCode"    name="changeCode"/>
         <input type="hidden" id="changeName"    name="changeName"/>
          <input type="hidden" id="cid"  name="cid"   />
        <input type="hidden" id="cid1"  name="cid1"   />
	    <table width='100%' border='0' cellpadding='0px' cellspacing='0px' style="margin: 0px; padding: 2px">
					<tr>
						<td valign="top" width="16%">
								  
										 <div id="left_1">    
										   <table  width="100%"  border="0"  cellpadding="0" cellspacing="0" background="<%=path%>/images/fpage_04.gif">
								                <tr>
								                  <td class="title_ma">地图</td>
								                  <td align="right"><img src="<%=path%>/images/fpage_10.gif" width="0px" height="25"></td>
								                </tr>
								              </table>
								      		  <table  width="100%"  border="0"  cellpadding="0" cellspacing="0" background="<%=path%>/images/fpage_04.gif">
								                <tr>
								                  <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=8,0,0,0" width="192px" height="180" id="myFlash"">
                                						<param name="allowScriptAccess" value="always" />
                               							<param name="movie" value="map.swf" />
                              						    <param name="quality" value="high" />
                              						    <embed src="map.swf" quality="high" width="192px" height="180" name="myFlash" allowScriptAccess="always" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" />
                          						  </object>
								                </tr>
								              </table>
								             
									    </div>
										 <div id="left_2">    
								      		  <table width="100%" border="0"  cellpadding="0" cellspacing="0" background="<%=path%>/images/fpage_04.gif">
								                <tr>
								                  <td class="title_ma">公告列表</td>
								                  <td class="title_mo" align="right"><a href="#" onclick="openNotice(<%=systemId%>);">更多&gt;&gt;</a></td>
								                  <td align="right"><img src="<%=path%>/images/fpage_10.gif" width="0px" height="25px"></td>
								                </tr>
								              </table>
                                               <!--  <MARQUEE onmouseover=this.stop() onmouseout=this.start() scrollAmount=1 scrollDelay=60 direction=up width='100%' height='80'>  -->
								                   <ul class="u_2"    id="noticeList"></ul>
								               <!-- </MARQUEE> -->
								             <div class="page" id="noticListPage"></div>
								             <div class="show_content" id="showMesId"></div>
										 </div>
										<%--  <% if("213763".equals(tempUser)||tempUser=="213763"){ %>
										 <div id="left_5">    
								      		  <table  width="100%"  border="0"  cellpadding="0" cellspacing="0" background="<%=path%>/images/fpage_04.gif">
								                <tr>
								                  <td class="title_ma">用户管理</td>
								                  <td align="right"><img src="<%=path%>/images/fpage_10.gif" width="0px" height="25"></td>
								                </tr>
								              </table>
								              <table   width="100%" border="0" cellpadding="0" cellspacing="0">
								                <tr>
								                  <td align="center"><a href="#" onclick="openApplyUser();"><img src="<%=path%>/images/apply.jpg" style="border:0;margin-top:10px; "/></a></td>
								                  <td align="center"><a href="#" onclick="openPartAduit();"><img src="<%=path%>/images/aduit.jpg" style="border:0;margin-top:10px; "/></a></td>
								                </tr>
								                <tr>
								                  <td align="center"><a href="#" onclick="openApplyUser();">账号申请</a></td>
								                  <td align="center"><a href="#" onclick="openPartAduit();">分公司审核</a></td>
								                </tr>
								              </table>
								              <table   width="100%" border="0" cellpadding="0" cellspacing="0">
								                <tr>
								                  <td align="center"><a href="#" onclick="openVisitRank(<%=systemId%>);"><img src="<%=path%>/images/access.jpg" style="border:0;margin-top:10px;"/></a></td>
								                  <td align="center"><a href="#" onclick="openAppRank(<%=systemId%>);"><img src="<%=path%>/images/login.jpg" style="border:0;margin-top:10px;"/></a></td>
								                </tr>
								                <tr>
								                  <td align="center"><a href="#" onclick="openVisitRank(<%=systemId%>);">用户登录情况</a></td>
								                  <td align="center"><a href="#" onclick="openAppRank(<%=systemId%>);">  用户访问情况</a></td>
								                </tr>
								              </table>
										  </div>
										   <%} else if("213762".equals(tempUser)||tempUser=="213762"){%>
										  <div id="left_5">    
								      		  <table  width="100%"  border="0"  cellpadding="0" cellspacing="0" background="<%=path%>/images/fpage_04.gif">
								                <tr>
								                  <td class="title_ma">用户管理</td>
								                  <td align="right"><img src="<%=path%>/images/fpage_10.gif" width="0px" height="25"></td>
								                </tr>
								              </table>
								              <table   width="100%" border="0" cellpadding="0" cellspacing="0">
								                <tr>
								                  <td align="center"><a href="#" onclick="openApplyUser();"><img src="<%=path%>/images/apply.jpg" style="border:0;margin-top:10px; "/></a></td>
								                  <td align="center"><a href="#" onclick="openProvAduit();"><img src="<%=path%>/images/aduit.jpg" style="border:0;margin-top:10px; "/></a></td>
								                </tr>
								                <tr>
								                  <td align="center"><a href="#" onclick="openApplyUser();">账号申请</a></td>
								                  <td align="center"><a href="#" onclick="openProvAduit();">省公司审核</a></td>
								                </tr>
								              </table>
								              <table   width="100%" border="0" cellpadding="0" cellspacing="0">
								                <tr>
								                   <td align="center"><a href="#" onclick="openVisitRank(<%=systemId%>);"><img src="<%=path%>/images/access.jpg" style="border:0;margin-top:10px;"/></a></td>
								                  <td align="center"><a href="#" onclick="openAppRank(<%=systemId%>);"><img src="<%=path%>/images/login.jpg" style="border:0;margin-top:10px;"/></a></td>
								                </tr>
								                <tr>
								                  <td align="center"><a href="#" onclick="openVisitRank(<%=systemId%>);">用户登录情况</a></td>
								                  <td align="center"><a href="#" onclick="openAppRank(<%=systemId%>);">用户访问情况</a></td>
								                </tr>
								              </table>
										  </div>
										  <%}else{ %>
										  <div id="left_5">    
								      		  <table  width="100%"  border="0"  cellpadding="0" cellspacing="0" background="<%=path%>/images/fpage_04.gif">
								                <tr>
								                  <td class="title_ma">用户管理</td>
								                  <td align="right"><img src="<%=path%>/images/fpage_10.gif" width="0px" height="25"></td>
								                </tr>
								              </table>
								              <table   width="100%" border="0" cellpadding="0" cellspacing="0">
								                <tr>
								                  <td align="center"><a href="#" onclick="openApplyUser();"><img src="<%=path%>/images/apply.jpg" style="border:0;margin-top:10px; "/></a></td>
								                  <td align="center" width="102px"></td>
								                </tr>
								                <tr>
								                  <td align="center"><a href="#" onclick="openApplyUser();">账号申请</a></td>
								                  <td align="center" width="102px"></td>
								                </tr>
								              </table>
								              <table   width="100%" border="0" cellpadding="0" cellspacing="0">
								                <tr>
								                   <td align="center"><a href="#" onclick="openVisitRank(<%=systemId%>);"><img src="<%=path%>/images/access.jpg" style="border:0;margin-top:10px;"/></a></td>
								                  <td align="center"><a href="#" onclick="openAppRank(<%=systemId%>);"><img src="<%=path%>/images/login.jpg" style="border:0;margin-top:10px;"/></a></td>
								                </tr>
								                <tr>
								                  <td align="center"><a href="#" onclick="openVisitRank(<%=systemId%>);">用户登录情况</a></td>
								                  <td align="center"><a href="#" onclick="openAppRank(<%=systemId%>);">  用户访问情况</a></td>
								                </tr>
								              </table>
										  </div>
										  <%} %> --%>
										  <div id="left_3">    
								      		  <table  width="100%" border="0"  cellpadding="0" cellspacing="0"  background="<%=path%>/images/fpage_04.gif">
								                <tr>
								                  <td class="title_ma">待办事宜</td>
								                  <td align="right"><img src="<%=path%>/images/fpage_10.gif" width="0px" height="25"></td>
								                </tr>
								              </table>
								             <ul class="u_1">
									              <li class="li_1"><a href="#" onclick="openToDeal('1');" style="cursor:hand">待处理 <font color="red">(<span id="num_0"></span>)</font></a></li>
									              <li class="li_1"><a href="#" onclick="openToDeal('2');" style="cursor:hand">处理中 <font color="red">(<span id="num_1"></span>)</font></a></li>
									              <li class="li_1"><a href="#" onclick="openToDeal('6');" style="cursor:hand">待评估 <font color="red">(<span id="num_2"></span>)</font></a></li>
									              <li class="li_1"><a href="#" onclick="openToDeal('5');" style="cursor:hand">已归档  <font color="red">(<span id="num_3"></span>)</font></a></li>
								             </ul> 
										 </div> 
								 <%--  
								   <% if(roleCtr.isRightRole("213602",userInfo.get("userId").toString())){ %>
										 <div id="left_4">    
								      		  <table  width="100%"  border="0"  cellpadding="0" cellspacing="0" background="<%=path%>/images/fpage_04.gif">
								                <tr>
								                  <td class="title_ma">应用系统</td>
								                  <td align="right"><img src="<%=path%>/images/fpage_10.gif" width="0px" height="25"></td>
								                </tr>
								              </table>
								             <table   width="100%" border="0" cellpadding="0" cellspacing="0">
								             
								           
								                <tr>
								                  <td align="center" width="50%"><a href="#" onclick="loadZZsys();"><img src="<%=path%>/meta/resource/images/main_bg.png"  width="43" height="50" style="border:0;"/></a></td>
								                  <td align="center" width="50%"></td>
								                </tr>
								                <tr>
								                  <td align="center"><a href="#"             onclick="loadZZsys();">智能取数</a></td>
								                  <td align="center"></td>
								                </tr>
								               
								              </table>
										  </div>	
							        <%} %>	
							    --%>    		  									  
									  
						</td>
						<td valign="top" width="84%">
 
   <table style='margin-left:2px; border: 1px solid #87CEFF;' width='100%'  border='0'  cellpadding='0px' cellspacing='0px'>
		   <tr height='25px' style='background:url(<%=path%>/images/fpage_04.gif);'>
				 <td nowrap align='left' class='title_ma1'>
					查询条件                                  
				 </td>
			</tr>
			<tr>
			     <td>
						<table  width="100%"  border="0" cellpadding="0" cellspacing="0">
						                <tr>
						                  <td width="6%">报表名称:</td>
						                  <td width="14%">
								<select id="tabId" name="tabId" onchange="onSelect(this);"  style="display: none">
								    <option value="1" selected="selected">日重点指标</option>
								    <option value="2">月重点指标</option>
								</select>
						                  </td>
						                  <td id="tdDateTime" width="5%">日 期:</td>
						                  <td width="15%">
						                     <div id="divDay">
						                      <input     id="dayTime" name="dayTime" value="<%=yestedayDate%>" readonly="true" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'<%=yestedayDate%>'})" >
						                     </div>  
						                     <div id="divMonth" style="display: none;">
						                   	  <select id="monthTime"    name="monthTime">
						                   	   <% for(String month:months ){  %>
									                  <option value="<%=month %>"><%=month %></option>
									           <%  }%>    
								               </select>
							                </div>
							  	
						                  </td>
						                  <td width="5%">区 域:</td>
						                  <td width="15%">
							   <input type="hidden"  id="zoneCode"  value=<%=zoneCode%>  name="zoneCode" />
							   <input type="text"    id="zone"           name="zone"  style="width: 160px;height:18px;line-height:18px;" readonly="readonly"/>
						                  </td>
						                  <td nowrap width="7%">
						                     <input type="button"  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"     onclick="queryData();"        style="width:60px;" />
						                  </td>
						                  <td width="33%">
						                     <input type='button'  class="poster_btn" id="impBtn"    name="impBtn"    value="导  出"     onclick="impExcel();"         style="width:60px;" />
						                  </td>
						                </tr>
						</table>			        
			    </td>
			</tr>
</table>	
 <!-- 顶部图形 -->	               
	 <div id="top">	               
	 </div>
    <div id="right_1">
	           <div id="rpt_tabbar" style="margin: 0px;padding: 0px;width: 100%;height: auto;"></div> 
               <div id="index_exp"  class="tips"></div>		           
    </div>
               </td>
			</tr>
	   </table>
   </form>
  <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</body>
</html>
<script type="text/javascript">
//全局用户数据
	var currentTab = "";
	var width  =$("index_exp").offsetWidth; 
	var	height =250;
	function impExcel(form){
		dhx.showProgress("正在执行......");
		exportImage(); 
		window.setTimeout("exportExcel()",8000);
	    window.setTimeout("dhx.closeProgress()",8000);
} 
    function dump_obj(myObject) {  
	  var s = "";  
	  for (var property in myObject) {  
	   s = s + "\n "+property +": " + myObject[property] ;  
	  }  
	  alert(s);  
	}
    
    
  function loadZZsys(){
	    document.forms[0].action="http://132.121.165.45:8085/meta/meta/login_sso.jsp";
		document.forms[0].method="post";
		document.forms[0].target="_blank";
		document.forms[0].submit();
	}
	
	//界面加载与SWF加载的同步
var isReady = false;
function JsReady() {
    return isReady;
}
isReady = true;

//取得flash对象
function getFlashMovieObject(movieName) {
    if (window.document[movieName]) {
        return window.document[movieName];
    } else if (navigator.appName.indexOf("Microsoft") == -1) {
        if (document.embeds && document.embeds[movieName])
            return document.embeds[movieName];
    }
    else {
        return document.getElementById(movieName);
    }
}

var isOnly = false;
	
	function gotoArea(area, isOnly) {
var Data1 = [
			['Area_200','0x0068B0','','A_200'],
			['Area_755','0x0068B0','天翼手机:-2000','A_755'],
			['Area_769','0x0068B0','天翼手机:120','A_769'],
			['Area_757','0x0068B0','天翼手机:120','A_757'],
			['Area_754','0x0068B0','天翼手机:120','A_754'],
			['Area_760','0x0068B0','天翼手机:120','A_760'],
			['Area_752','0x0068B0','天翼手机:-120','A_752'],
			['Area_750','0x0068B0','天翼手机:120','A_750'],
			['Area_759','0x0068B0','天翼手机净增:120','A_759'],
			['Area_663','0x0068B0','天翼手机净增:120','A_663'],
			['Area_756','0x0068B0','天翼手机净增:120','A_756'],
			['Area_668','0x0068B0','天翼手机净增:-120','A_668'],
			['Area_758','0x0068B0','天翼手机净增:120','A_758'],
			['Area_753','0x0068B0','天翼手机净增:120','A_753'],
			['Area_763','0x0068B0','天翼手机净增:120','A_763'],
			['Area_768','0x0068B0','天翼手机净增:120','A_768'],
			['Area_660','0x0068B0','天翼手机净增:120','A_660'],
			['Area_762','0x0068B0','天翼手机净增:120','A_762'],
			['Area_751','0x0068B0','天翼手机净增:120','A_751'],
			['Area_662','0x0068B0','天翼手机净增:120','A_662'],
			['Area_766','0x0068B0','天翼手机净增:120','A_766']
		];
   // getFlashMovieObject("myFlash").setMap(Data1);
    
    
}
//单击地图调用的js方法
		function callJs(v)
		{
			
			if(v=="0"){
				v="0000";
			}
			$("zoneCode").value=v;
			queryData();
			loadZoneTreeChkBox_map(v,queryform);
		}
</script>
<script type="text/javascript" src="index_gl.js"></script>