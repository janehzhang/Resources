<%@ page import="tydic.meta.common.Constant" %>
<%@ page import="tydic.meta.module.mag.role.RoleAction,tydic.meta.module.mag.role.RoleDAO" %>
<%@ page import="java.util.Map,tydic.meta.module.mag.role.RoleAction" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <title>广东电信客户服务分析系统</title>
    <%@include file="../../public/head.jsp"%>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/LoginAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/UserAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/MenuAction.js"></script>
    <script type="text/javascript" src="<%=rootPath%>/dwr/interface/ChannelIndexAction.js"></script>
    <style type="text/css">
        html{overflow-x: hidden; overflow-y: auto;} 
        #north{ 
		  max-width:300;  
		  max-height:100px;   
		  /*由于IE6.0以及以前版本的IE不支持上边两个属性,所以加上以下两条语句.这里要说明的是expression只有IE支持*/    
		  width: expression(this.width > 300 && this.width / 300 >= this.height / 100 ? 300 : true);    
		  height: expression(this.height > 100 && this.width / 300 < this.height / 100 ? 100 : true);   
		}
        #nav{
            background: url('<%=rootPath%>/meta/resource/images/title-bg1.jpg');
            padding-left:10px;
            height: 35px;
            padding: 0px;
            margin: 0px;
            margin-top:3px;
        }
       #navUl{
            height: 25px;
            padding:0px;
            margin:0px;
            list-style: none;
        }
        #navUl li{
            float:left;
            padding:0px;
            margin:0px;
            text-align:center;
            padding-top:5px;
            padding-right:5px;
            height:25px;
            width:110px;
            background: url('<%=rootPath%>/meta/resource/images/menu-line.png') no-repeat;
            background-position: right;
        }
        #navUl li a{
            text-align:center;
            text-decoration: none;
            font-size: 12px;
            color: black;
            width:100px;
            height:28px;
            float: right;
            border: none;
            padding-top: 0px;
            border-radius: 6px;
            border-top-right-radius:6px;
            border-top-left-radius :6px;
        }
        #navUl li a img{
            max-width:16px;
            min-height:16px;
            border: none;
            margin-right: 2px;
        }
        #_menu_toolbar{
            position:relative;
            right:4%;
            width:400px;
            top:5px; 
            vertical-align:middle;
        }
        #_menu_toolbar a{
            text-decoration: none;
            font-size: 12px;
            color: black;
        }
        #_menu_toolbar a span{
            padding:0px;
            margin:0px;
            height:100%;
        }
        #_menu_toolbar a img{
            padding:0px;
            margin:0px;
            padding-left: 2px;
            padding-right: 1px;
            width: 16px;
            height: 16px;
            border: none;
        }
        table.dhtmlxLayoutPolyContainer_dhx_skyblue td.dhtmlxLayoutPolySplitterHorInactive{
            height: 1px;
            border: none;
        }

        table.dhtmlxLayoutPolyContainer_dhx_skyblue td.dhtmlxLayoutPolySplitterHorInactive div{
            display: none;
        }
        table.dhtmlxLayoutPolyContainer_dhx_skyblue td.dhtmlxLayoutSinglePoly div.dhxcont_global_content_area{
            border: none;
        }
       #red {color:red;} 
       
    </style>
 
    <style type="text/css">
	    a:link,a:visited{ text-decoration:none;  /*超链接无下划线*/}
	    a:hover{ text-decoration:underline;  /*鼠标放上去有下划线*/}
	</style>
	    <%
	        RoleAction  roleCtr= new RoleAction();
	        Map<String,Object> sysInfo=(Map<String,Object>)session.getAttribute(LoginConstant.SESSION_META_SYSTEM_INFO);
	        String systemId=sysInfo.get("groupId").toString();
	        if(systemId==null){//如果系统ID为null，从Session中获取当前用户的默认系统ID
	            Map<String,Object> userInfo=(Map<String,Object>)session.getAttribute(LoginConstant.SESSION_KEY_USER);
	            systemId=userInfo.get("groupId")==null
	                    ?String.valueOf(Constant.DEFAULT_META_SYSTEM_ID):userInfo.get("groupId").toString();
	        }
	        out.println(
	                "<script type=\"text/javascript\">var getSystemId=function(){var systemId='"
	                        +(systemId==null? Constant.DEFAULT_META_SYSTEM_ID:systemId)+"'; " +
	                        "return parseInt(systemId||1);}</script>"
	        );//打印一段JS代码，用于返回当前systemId
	        
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
</head>
<body>
	<form id="queryform" name="queryform">
	 	  <input type="hidden" id="userNameen" name="userNameen"  value="<%=userInfo.get("userNamecn").toString() %>"/>
		<div id="div_src"
			style="position:absolute;width:100%;height:100%;z-index:-1;top:0px;left:0px; overflow:scroll;">
		</div>
		<div id="north"
			style="overflow: hidden;background-color: #C9DAF4;left:0px;bottom:0px;background-image: url('<%=rootPath%>/portalCommon/resource/images/u41_normal3.jpg');background-repeat: no-repeat;">
			<div style="height: 45px;" align="right">
				<div id="_menu_toolbar" valign="center" style="width:80%;top:5px;bottom:0px;">
				
            		<!-- <a href="javascript:showMyFavorite()">
						<img src="<%=rootPath%>/meta/resource/images/premium.png"
							alt="" align="middle" style="height: 20px;width: 20px" border="0" />
						<span style="color:#0066CC;font-weight:bold;font-size:11px">我的收藏</span>
					</a>
					 -->
				
					<table  style="font-size:6px;">
						<tr style="height:80%;padding-bottom:0px;" >
							<td align="center"  style="padding-bottom:0px;padding-right:10px;"><a style="padding-bottom:0px;" href="#" onclick="openMenuRank(<%=systemId%>);">
							<img style="height: 16px;width: 16px" border="0" src="<%=rootPath%>/images/menuRank.png" /></a></td>
							<td align="center"  style="padding-bottom:0px;padding-right:10px;"><a style="padding-bottom:0px;" href="#" onclick="openAppRank(<%=systemId%>);">
							<img style="height: 16px;width: 16px" border="0" src="<%=rootPath%>/images/login.png" /></a></td>
							
							<td align="center"  style="padding-bottom:0px;padding-right:10px;"><a style="padding-bottom:0px;" href="#" onclick="openVisitRank(<%=systemId%>);">
							<img style="height: 16px;width: 16px" border="0" src="<%=rootPath%>/images/access.png" /></a></td>
														
							<td align="center"  style="padding-bottom:0px;padding-right:10px;"><a style="padding-bottom:0px;" href="#" onclick="manageLocalUser();">
							<img style="height: 16px;width: 16px" border="0" src="<%=rootPath%>/images/userInfo.png" /></a></td>
							
							<td align="center"  style="padding-bottom:0px;padding-right:10px;"><a style="padding-bottom:0px;" href="#" onclick="openApplyUser();">							
							<img style="height: 16px;width: 16px" border="0" src="<%=rootPath%>/images/apply.png" /></a></td>
							<%--
								此处在导航判断是否显示分公司/省公司审核，根据角色id判断														
							--%>
							<% 	if("213763".equals(tempUser)||tempUser=="213763"){ %>
								<td align="center"  style="padding-bottom:0px;padding-right:10px;"><a style="padding-bottom:0px;" href="#" onclick="openPartAduit();">
								<img style="height: 16px;width: 16px" border="0"  src="<%=rootPath%>/images/aduit.png" /></a></td>
							<%}
							 else if("213762".equals(tempUser)||tempUser=="213762"){%>
								<td align="center"  style="padding-bottom:0px;padding-right:10px;"><a style="padding-bottom:0px;" href="#" onclick="openProvAduit();">
								<img style="height: 16px;width: 16px" border="0"  src="<%=rootPath%>/images/aduit.png" /></a></td>
							<%} %>
							
							<td align="center"  style="padding-bottom:0px;padding-right:10px;"><a style="padding-bottom:0px;" href="#" onclick="loadApplyOperMan();">
							<img style="height: 16px;width: 16px" border="0"  src="<%=rootPath%>/images/manual_icon1.png" /></a></td>					
							<td align="center"  style="padding-bottom:0px;padding-right:10px;"><a style="padding-bottom:0px;" href="#" onclick="loadTranMan();">
							<img style="height: 16px;width: 16px" border="0"  src="<%=rootPath%>/images/manual_icon2.png" /></a></td>						
							<td align="center"  style="padding-bottom:0px;padding-right:10px;"><a style="padding-bottom:0px;" href="#" onclick="loadOperMan();">
							<img style="height: 16px;width: 16px" border="0"  src="<%=rootPath%>/images/manual_icon3.png" /></a></td>						
							<td align="center"  style="padding-bottom:0px;padding-right:10px;"><a style="padding-bottom:0px;" href="#" onclick="changePassword();">
							<img style="height: 16px;width: 16px" border="0"  src="<%=rootPath%>/images/changePassword.png" /></a></td>						
							<td align="center"  style="padding-bottom:0px;padding-right:10px;"><a style="padding-bottom:0px;" href="#" onclick="logout();">
							<img style="height: 16px;width: 16px" border="0"  src="<%=rootPath%>/images/logout.png" /></a></td>						
							<td ></td>
						</tr>
						<tr style="padding-top:0px;">
							<td align="center"  style="padding-top:0px;padding-right:10px;"><a style="padding-top:0px;color: rgb(0, 102, 204); font-weight: bold; font-size: 12px;" href="#" onclick="openMenuRank(<%=systemId%>);">应用访问</a></td>

							<td align="center"  style="padding-top:0px;padding-right:10px;"><a style="padding-top:0px;color: rgb(0, 102, 204); font-weight: bold; font-size: 12px;" href="#" onclick="openAppRank(<%=systemId%>);">用户访问</a></td>							
							<td align="center"  style="padding-top:0px;padding-right:10px;"><a style="padding-top:0px;color: rgb(0, 102, 204); font-weight: bold; font-size: 12px;" href="#" onclick="openVisitRank(<%=systemId%>);">登录次数</a></td>
							<td align="center"  style="padding-top:0px;padding-right:10px;"><a style="padding-top:0px;color: rgb(0, 102, 204); font-weight: bold; font-size: 12px;" href="#" onclick="manageLocalUser();">账号列表</a></td>
							<td align="center"  style="padding-top:0px;padding-right:10px;"><a style="padding-top:0px;color: rgb(0, 102, 204); font-weight: bold; font-size: 12px; " href="#" onclick="openApplyUser();">账号申请</a></td>
							
							<% 	if("213763".equals(tempUser)||tempUser=="213763"){ %>
								<td align="center"  style="padding-top:0px;padding-right:10px;"><a style="padding-top:0px;color: rgb(0, 102, 204); font-weight: bold; font-size: 12px;" href="#" onclick="openPartAduit();">分公司审核</a></td>
							<%}
							 else if("213762".equals(tempUser)||tempUser=="213762"){%>
							 	<td align="center"  style="padding-top:0px;padding-right:10px;"><a style="padding-top:0px;color: rgb(0, 102, 204); font-weight: bold; font-size: 12px;" href="#" onclick="openProvAduit();">省公司审核</a></td>
							<%} %>
														
							<td align="center"  style="padding-top:0px;padding-right:10px;"><a style="padding-top:0px;color: rgb(0, 102, 204); font-weight: bold; font-size: 12px; " href="#" onclick="loadApplyOperMan();">申请指引</a></td>																				
							<td align="center"  style="padding-top:0px;padding-right:10px;"><a style="padding-top:0px;color: rgb(0, 102, 204); font-weight: bold; font-size: 12px; " href="#" onclick="loadTranMan();">培训材料</a></td>						
							<td align="center"  style="padding-top:0px;padding-right:10px;"><a style="padding-top:0px;color: rgb(0, 102, 204); font-weight: bold; font-size: 12px; " href="#" onclick="loadOperMan();">操作手册</a></td>												
							<td align="center"  style="padding-top:0px;padding-right:10px;"><a style="padding-top:0px;color: rgb(0, 102, 204); font-weight: bold; font-size: 12px; " href="#" onclick="changePassword();">修改密码</a></td>											
							<td align="center"  style="padding-top:0px;padding-right:10px;"><a style="padding-top:0px;color: rgb(0, 102, 204); font-weight: bold; font-size: 12px; " href="#" onclick="logout();">退出系统</a></td>
							<td align="center"  style="padding-top:0px;padding-right:10px;"> <FONT style="color: rgb(51, 102, 153);"> <span style="font-size: 12px;" id="welcome"> </span> </FONT></td>						
						</tr>
					</table>

				</div>
			</div>
			
			
			<div id="nav" style="border:2px;">
				<ul id="navUl">
				</ul>
			</div>
		</div>
	</form>
	<iframe name="hiddenFrame" width=0 height=0 src=""></iframe>
 
</body>
</html>
<script type="text/javascript">
    var _sessionInfo = {};
    var getSessionAttribute=function(attr,realTime){
        if(realTime||!_sessionInfo[attr]){
            SessionManager.getAttribute(attr,{async:false,callback:function(data){
                _sessionInfo[attr]=data;
            }
            })
        }
        return _sessionInfo[attr];
    };
    if (window.screen) {//判断浏览器是否支持window.screen判断浏览器是否支持screen
        var myw = screen.availWidth;   //定义一个myw，接受到当前全屏的宽
        var myh = screen.availHeight;  //定义一个myw，接受到当前全屏的高
        try{
            window.moveTo(0, 0);           //把window放在左上脚
            window.resizeTo(myw, myh);     //把当前窗体的长宽跳转为myw和myh
        }catch(e){
        }
        if(window.outerWidth!=screen.availWidth || window.outerHeight!=screen.availHeight){
            window.outerWidth=screen.availWidth;
            window.outerHeight=screen.availHeight;
        }

    }    /**
     * 根据菜单数据配置处理URL信息。
     * @param url
     * @param userAttr 用户属性字符串。
     */
    var urlDeal=function(url){
        var user=getSessionAttribute("user");
        var zone=getSessionAttribute("zoneInfo");
        var dept=getSessionAttribute("deptInfo");
        var station=getSessionAttribute("stationInfo");
        var winUrl = url;
        //检测url,如果不是以http开头的，默认是本系统路径
        if (winUrl) {
            if (winUrl.toLowerCase().indexOf("http://") == -1) {
                winUrl = getBasePath() + "/" + winUrl;
            }
            //替换宏变量
            winUrl=winUrl.replace(/(\w+)=\[(([0-9a-zA-Z]+):)?(\w+)\]/g,function($0,$1,$2,$3,$4){
                if(!$3||$3.toLowerCase()=="<%=LoginConstant.URL_MARCO_USER%>"){//用户属性
                    return $1+"="+encodeURIComponent(user[Tools.tranColumnToJavaName($4)]);
                }else if($3.toLowerCase()=="<%=LoginConstant.URL_MARCO_ZONE%>"){
                    return $1+"="+encodeURIComponent(zone[Tools.tranColumnToJavaName($4)]);
                }else if($3.toLowerCase()=="<%=LoginConstant.URL_MARCO_DEPT%>"){
                    return $1+"="+encodeURIComponent(dept[Tools.tranColumnToJavaName($4)]);
                }else if($3.toLowerCase()=="<%=LoginConstant.URL_MARCO_STATION%>"){
                    return $1+"="+encodeURIComponent(station[Tools.tranColumnToJavaName($4)]);
                }
            });
        }
        return winUrl;
    }
    var tabHeight,tabWidth;
    tabHeight=$("div_src").offsetHeight-88;
    tabWidth=$("div_src").offsetWidth;
</script>
<script type="text/javascript" src="index.js"></script>
