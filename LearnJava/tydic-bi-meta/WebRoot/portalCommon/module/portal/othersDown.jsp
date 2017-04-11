<%@page language="java" import="java.util.*"  pageEncoding="UTF-8"%>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="tydic.portalCommon.DateUtil,tydic.meta.module.mag.role.RoleDAO" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%
	java.util.Date datetime=new java.util.Date();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	String endDate=formatter.format(datetime);
	String startDate=DateUtil.fistDay(datetime);
   
   request.setAttribute("startDate",startDate); 
   request.setAttribute("endDate",endDate);
%>
<head>
    <title>管理层</title> 
    <%@include file="../../public/include.jsp" %>
    <%@include file="../../public/head.jsp" %>
    <script type="text/javascript" src="openLayer.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/UserAction.js"></script>
    <script type="text/javascript"   src="<%=rootPath%>/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/SerProManageAction.js"></script>
    <script type="text/javascript" src="<%=path %>/dwr/interface/PortalCommonCtrlr.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/DeptAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/meta/public/code.jsp?types=PROBLEM_SOURCE,PROBLEM_STEP,PROBLEM_URGENCY,PROBLEM_TYPE,IS_BOOL,PROBLEM_WAY,ATTACHMENT_TYPE"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
   
    <style type="text/css"> 
	body{margin:0;padding:0 0 12px 0;font-size:12px;line-height:22px;font-family:"宋体","Arial Narrow",HELVETICA;background:#fff;}
     form,ul,li,p,h1,h2,h3,h4,h5,h6{margin:0;padding:0;}input,select{font-size:12px;line-height:16px;}img{border:0;}ul,li{list-style-type:none}
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
	
	.show_content{ width:160px; margin:5px auto; text-align:center;}
	.gg_title{ font-size:12px; font-weight:bold; color:#0182dd;margin:10px auto;}
	.xian{ width:90%; margin:0 auto; height:1px; overflow:hidden; background:#0082de;}
	.gg_info{ width:auto; margin-left:15%;}
	.gg_info li{ float:left; list-style:none; height:25px; line-height:25px; text-align:left; color:#9c9b9b; margin-right:25px;}
	.gg_content{ text-align:left; padding-left:30px; margin-top:10px; }
	.closed{ float:right; display:block; width:50px; height:22px; line-height:25px; text-decoration:none; cursor:pointer; color:#333; margin-top:-5px;}
	.clear{ clear:both;}
</style>
    <style type="text/css">
      .poster_btn {
			    cursor: pointer;
			    display: inline-block;
			    font-size: 12px;
			    height: 22px;
			    line-height: 18px;
			    overflow: hidden;
			    top: 5px;
			    right: 0;
			    padding-bottom: 1px;
			    margin:5px 5px 5px 5px;
			    border-radius: 2px;
			    color: black;
			    border: 1px solid #198EB4;
			    background-color: #D6E8FF;
			    background-position: 0 0 !important;
			}
         #left{float: left; height: auto; width: 18.8%;}
		
		 #left_1{height:140px; width: 100%; border: 1px solid #87CEFF;margin-top:6px;margin-left:4px;}
		 #left_5 {
			height: 160px;
			width: 100%;
			border: 1px solid #87CEFF;
			margin-top:2px;
			margin-left:4px;
		    }
		.u_1{width:99%;height:auto;magin: 0px;padding: 0px;float: left;display:inline;list-style:none;margin-left: 0px}
	    .li_1{list-style-position:outside;list-style-type:none;background-image:url(<%=path %>/images/u72_normal.gif);background-repeat: no-repeat;
	          background-position:0px; padding-left: 20px; margin-top: 5px} 
	          
		 #left_2{height: 160px; width: 100%; border: 1px solid #87CEFF;margin-top:3px;margin-left:4px;}
	     .u_2{width:99%;height:auto;magin: 0px;padding: 0px;float: left;display:inline;list-style:none;margin-left: 0px}
	     .li_2{width:100%;list-style-position:outside;list-style-type:none;margin-top: 0px;display: inline;}
	     .li_2 span{color: blue; float: right;} 
		
		#u156_line{width:100%;height:2px;background-image:url(<%=path %>/images/u87.png);background-repeat:no-repeat;}
		#left_3{height: 100px; width: 100%; border: 1px solid #87CEFF;margin-top:3px; margin-left:4px;}
	    #right{ float: right; height: 470px; width: 79%; border: 1px solid #87CEFF;margin-top:6px;margin-right:8px;}	    
    </style>
     <style type="text/css">
	    a:link,a:visited{ text-decoration:none;  /*超链接无下划线*/}
	    a:hover{ text-decoration:underline;  /*鼠标放上去有下划线*/}
	 </style>
        <%
        Map<String,Object> sysInfo=(Map<String,Object>)session.getAttribute(LoginConstant.SESSION_META_SYSTEM_INFO);
        String systemId=sysInfo.get("groupId").toString();
        if(systemId==null){//如果系统ID为null，从Session中获取当前用户的默认系统ID
            Map<String,Object> userInfo=(Map<String,Object>)session.getAttribute(LoginConstant.SESSION_KEY_USER);
            systemId=userInfo.get("groupId")==null
                    ?String.valueOf(Constant.DEFAULT_META_SYSTEM_ID):userInfo.get("groupId").toString();
        }
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
        out.println(
                "<script type=\"text/javascript\">var getSystemId=function(){var systemId='"
                        +(systemId==null? Constant.DEFAULT_META_SYSTEM_ID:systemId)+"'; " +
                        "return parseInt(systemId||1);}</script>"
        );//打印一段JS代码，用于返回当前systemId
    %>
</head>
<body>
	<div id="main" style="solid #87CEFF;width: 100%;height:auto!important;overflow-y: visible;overflow-x: visible;">
		<div id="left">
			 <div id="left_1">    
	      		  <table  width="100%" border="0"  cellpadding="0" cellspacing="0"  background="<%=path %>/images/fpage_04.gif">
	                <tr>
	                  <td class="title_ma">待办事宜</td>
	                  <td align="right"><img src="<%=path %>/images/fpage_10.gif" width="0px" height="25"></td>
	                </tr>
	              </table>
	             <ul class="u_1">
		              <li class="li_1"><a href="#" onclick="openToDeal('1');" style="cursor:hand">待处理 <font color="red">(<span id="num_0"></span>)</font></a></li>
		              <li class="li_1"><a href="#" onclick="openToDeal('2');" style="cursor:hand">处理中 <font color="red">(<span id="num_1"></span>)</font></a></li>
		              <li class="li_1"><a href="#" onclick="openToDeal('6');" style="cursor:hand">待评估 <font color="red">(<span id="num_2"></span>)</font></a></li>
		              <li class="li_1"><a href="#" onclick="openToDeal('5');" style="cursor:hand">已归档  <font color="red">(<span id="num_3"></span>)</font></a></li>
	             </ul>
	             
			 </div> 
			 <div id="left_2">    
	      		  <table width="100%" border="0"  cellpadding="0" cellspacing="0" background="<%=path %>/images/fpage_04.gif">
	                <tr>
	                  <td class="title_ma">公告列表</td>
	                  <td class="title_mo" align="right"><a href="#" onclick="openNotice(<%=systemId %>);">更多&gt;&gt;</a></td>
	                  <td align="right"><img src="<%=path %>/images/fpage_10.gif" width="0px" height="25px"></td>
	                </tr>
	              </table>
	             <MARQUEE onmouseover=this.stop() onmouseout=this.start() scrollAmount=1 scrollDelay=60 direction=up width='100%' height='80'> 
				   <ul class="u_2"    id="noticeList"></ul>
				 </MARQUEE>
	             <ul class="u_2"  id="noticeList"></ul>
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
										  <%} %>		 --%> 		 
		 </div> 
		  <div id="right">
		   <form id="queryform" name="queryform"> 
		  <table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" background="<%=path %>/images/fpage_04.gif">
                <tr>
                  <td class="title_ma">工单处理列表</td>
                  <td align="right"><img src="<%=path %>/images/fpage_10.gif" width="6" height="25"></td>
                </tr>
                <tr>
              </table>  
		 <div style="margin-top: 5px" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;日期从:
	  	  <!-- <input name="startDate" id="startDate" type="text" value="${startDate}" readonly="true" onclick="calendar.show(this);" size="10" maxlength="10" readonly="readonly "/>  -->
	  	   <input name="startDate" id="startDate" size="23" value="${startDate}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" >
		   &nbsp;&nbsp;&nbsp;至:
		   <input name="endDate" size="23" id="endDate" value="${endDate}" readonly="true" class='Wdate' onclick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" >
		   &nbsp;
	       <input type='button'  class="poster_btn" id="queryBtn"  name="queryBtn"  value="查  询"    onclick="queryData();" style="width:60px;" />
	      </div>       
		<br/>
          <div id="tasksList"   align="center"></div>
          <div class="page" id="taskListPage" align="right"></div>
         </form>
              <br/>
		 </div>	
	</div>
</body>
 <iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
</html>
<script type="text/javascript" src="othersDown.js"></script>