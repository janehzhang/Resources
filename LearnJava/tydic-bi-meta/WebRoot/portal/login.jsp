<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en" style="width:100%;height: 100%" >
<head>
    <title>广东电信客户服务分析系统</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link type="text/css" rel="stylesheet" href="resource/css/cas.css">
    <link type="text/css" rel="stylesheet" href="resource/css/common.css">
    <link type="text/css" rel="stylesheet" href="resource/css/login.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/meta/resource/dhtmlx/dhtmlx.css">
</head>
<%
    String rootPath = request.getContextPath();
	String base = request.getScheme() + "://" +  request.getServerName() + ":" + request.getServerPort() +  request.getContextPath();
%>
<body id="cas"  style="width:100%;height: 99.9%" onload="bindImgAction();">
<style type="text/css">
    <!--
    *{padding:0px;margin:opx;}
    body,html{
        background-repeat: repeat;
        font-size: 12px;
        margin: 0;
        padding: 0;
        line-height: 21px;
        background:url(<%=request.getContextPath() %>/portal/resource/images/login_Bg.jpg) no-repeat;
    }
    .div{
        background-repeat: repeat;
        font-size: 12px;
        margin: 0 ;
        padding: 0;
        line-height: 21px;
        text-align:center;
        height:100%; overflow:hidden;
        background:url(<%=request.getContextPath() %>/portal/resource/images/login_Bg.jpg) no-repeat -24px -17px;
    }
    .page-bg{
        background-image: url(<%=request.getContextPath() %>/portal/resource/images/login-pagebg.gif) no-repeat;
        background-repeat: repeat;
        font-size: 12px;
        margin: 0;
        padding: 0;
        line-height: 21px;
        text-align:center;
    }
    .side4-gray{
        border: 1px solid #d5d5d5;
        border-collapse: collapse;
    }
    .fl{
        float: left;
    }

    .btn-login{
        background:url(<%=request.getContextPath() %>/portal/resource/images/btn-login.gif) no-repeat 0 0 ;
        height:21px;
        width:59px;
        border:none;
        cursor:pointer;}

    .btn-chongzhi{
        background:url(<%=request.getContextPath() %>/portal/resource/images/btn-chongzhi.gif) no-repeat 0 0 ;
        height:21px;
        width:59px;
        border:none;
        cursor:pointer;}

    .input120{
        width:130px;
        margin-left: 2px;
        padding: 0 2px;
        vertical-align:middle;
        border: #a1c0db solid 1px;
    }
    .tl{
        text-align: left;
    }
    -->
</style>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/engine.js'></script>
<script type='text/javascript' src='<%=request.getContextPath() %>/dwr/util.js'></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/dwr/interface/LoginAction.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/meta/resource/dhtmlx/dhtmlx.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/meta/resource/js/dhtmlxExtend.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/meta/resource/js/dhtmlxMessage.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/meta/resource/js/dhtmlxDataConverter.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/meta/resource/js/dhtmlx_i18n_zh.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/meta/resource/js/commonFormater.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/common/des.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/common/base64.js"></script>

<script>
    var getBasePath = function() { //获取根目录
        return "<%=rootPath%>";
    }
    var sysPath = "<%=base%>";
  /** add yhd start **/
    function getCookieVal (offset) 
    {
	   var endstr = document.cookie.indexOf (";", offset);
	   if (endstr == -1)
	      endstr = document.cookie.length;
	   return unescape(document.cookie.substring(offset, endstr));
   	}

	function GetCookie(name) 
    {
	   var arg = name + "=";
	   var alen = arg.length;
	   var clen = document.cookie.length;
	   var i = 0;
	   while (i < clen) 
	      {
	      var j = i + alen;
	      if (document.cookie.substring(i, j) == arg)
	         return getCookieVal (j);
	      i = document.cookie.indexOf(" ", i) + 1;
	      if (i == 0) 
	         break; 
	      }
	   return null;
     }
     function SetCookie(name, value) 
     {
	   var argv = SetCookie.arguments;
	   var argc = SetCookie.arguments.length;
	   var expires=new Date(); 
	   expires.setTime(expires.getTime()+(3650*24*60*60*1000)); 
	   var path = (3 < argc) ? argv[3] : null;
	   var domain = (4 < argc) ? argv[4] : null;
	   var secure = (5 < argc) ? argv[5] : false;
	   document.cookie = name + "=" + escape (value) +
	     ((expires == null) ? "" : ("; expires=" + expires.toGMTString())) +
	     ((path == null) ? "" : ("; path=" + path)) +
	     ((domain == null) ? "" : ("; domain=" + domain)) +
		 ((secure == true) ? "; secure" : "");
     }
     
 function onloads(){
	    document.getElementById("loginId").focus();
		loginId =  GetCookie("loginId");
		password = GetCookie("password");
		if(loginId !=null && loginId !=""){
			document.getElementById("loginId").value = loginId;
			document.getElementById("password").focus();
		}if(password != null && password != ""){
			document.getElementById("password").value = password;
			document.getElementById("validateCode").focus();
		}	
	}
     
   /** add yhd end **/   
     
    function onSubmit(){
    		if(dwr.util.getValue("loginId")==''){
    			document.getElementById("loginId").focus();
                dhx.alert("请输入Email、中文名或者手机号码！");
                return false;
            }
            if(dwr.util.getValue("password")==''){
                dhx.alert("请输入密码！");
                return false;
            }
            if(dwr.util.getValue("validateCode") == ""){
            	document.getElementById("validateCode").focus();
                dhx.alert("请输入验证码！");
                return false;
            }
            
            //加密用户密码
            var passwd = dwr.util.getValue("password");
          	//var md5Passwd = hex_md5(passwd);
          	var key1 = dwr.util.getValue("validateCode");
          	var  enResult = strEnc(passwd,key1,"KEY2","KEY3"); 
          	$("password").value=enResult;
          	//加密用户名
          	var loginId = dwr.util.getValue("loginId");
          	var base64loginId = base64encode(utf16to8(loginId));
          	//alert(base64loginId);
          	$("loginId").value=base64loginId;
          	
        if(dwr.util.getValue("loginId")!='' && dwr.util.getValue("password")!=''){
        	
        	if(document.getElementById("cbUserName").checked){
				SetCookie("loginId",document.getElementById("loginId").value);
			}
			if(document.getElementById("cbPassword").checked){
				SetCookie("password",document.getElementById("password").value);
			}
			
        	//dhx.showProgress("登录","正在提交登录数据，请您稍候...");          
            //提交动作，访问登录Action
            LoginAction.login(dwr.util.getFormValues("submitForm"),"meta",function(data){
              // dhx.closeProgress();
               changeCode();
               dwr.util.setValue("password","");
               var date = new Date();
                switch(data){
                    case "ERROR_GROUP_DISENBLE":{
                        document.getElementById("loginId").focus();
                        dhx.alert("用户所属系统状态失效，请联系管理员！");
                        break;
                    }
                    case "ERROR_DISABLED":{
                    	document.getElementById("loginId").focus();
                        dhx.alert("该用户已被禁用，不能使用！");
                        break;
                    }
                    case "ERROR_LOCKING":{
                    	document.getElementById("loginId").focus();
                        dhx.alert("该用户已被锁定，不能使用！");
                        break;
                    }
                    case "ERROR_AUDITING":{
                    	document.getElementById("loginId").focus();
                        dhx.alert("该用户正在被审核中，暂不能使用！");
                        break;
                    }
                    case "ERROR_USER_PASSWD":{
                    	document.getElementById("loginId").focus();
                        dhx.alert("用户名或密码错误，请确认！");
                        break;
                    }
                    case "ERROR_NAME_REPEAT":{
                    	document.getElementById("loginId").focus();
                        dhx.alert("您输入的中文名重复，请使用Email登录！");
                        break;
                    }
                    case "ERROR_VALIDATECODE":{
                    	document.getElementById("password").focus();
                        dhx.alert("您输入的验证码错误，请重新输入！");
                        break;
                    }
                    case "ERROR_VALIDATEOVERDUE":{
                    	document.getElementById("password").focus();
                       	dhx.alert("验证码已过期，请重新输入！");
                       	break;
                    }
                    case "ERROR_ACC_CHECK_PASSWD":{
                    	document.getElementById("password").focus();
                       	dhx.alert("ACC认证失败，统一账号密码输入错误！");
                       	break;
                    }
                    case "ERROR_ACC_LOCKING":{
                    	document.getElementById("password").focus();
                       	dhx.alert("ACC认证失败，统一账号被冻结，请联系ACC管理员！");
                       	break;
                    }
                    default:{
                        window.location.href="<%=request.getContextPath() %>/meta/module/index/index.jsp?"+date.getTime();
                    }
                }
                //document.getElementById("loginId").focus();
            });
            return false;
        }else{
            return false;
        }
    }
    setTimeout(function(){
        document.getElementById("loginId").focus();
    },100);
    
    //更换验证码图片
    function changeCode()
    {
    	document.getElementById("validateCode").value = "";
    	var _validateCodeImgId = document.getElementById("validateCodeImg");
    	var date = new Date();
		_validateCodeImgId.src="<%=request.getContextPath() %>/meta/public/verifyCode.jsp?"+date.getTime(); 
    }
    
    function bindImgAction() {
		var img = document.getElementById('validateCodeImg');
		img.onclick = function() {
			changeCode();
		}
		onloads();
	}
    
    function dump_obj(myObject) {  
 	   var s = "";  
 	   for (var property in myObject) {  
 		   s = s + "\n "+property +": " + myObject[property] ;  
 		  }  
 		  alert(s);  
 		}
    
    var userRegistDialog=function(){
   	 	//初始化新增弹出窗口。
    	var dhxWindow = new dhtmlXWindows();
    	dhxWindow.createWindow("addWindow", 0, 0, 1250, 520);
    	var addWindow = dhxWindow.window("addWindow");
    	addWindow.setModal(true);
    	addWindow.stick();
    	addWindow.setDimension(1250, 520);
   	 	addWindow.center();
    	addWindow.setPosition(addWindow.getPosition()[0],50);
    	addWindow.denyResize();
    	addWindow.denyPark();
    	addWindow.button("minmax1").hide();
    	addWindow.button("park").hide();
    	addWindow.button("stick").hide();
    	addWindow.button("sticked").hide();
    	//addWindow.button("close").hide();
    	addWindow.setText("");
    	addWindow.keepInViewport(true);
    	addWindow.show();
    	
    	addWindow.attachEvent("onClose", function(win){
    		window.top.location=sysPath;
	        return true;
	    });
    	
    	var modifyForm = addWindow.attachForm(userData);
	}
    // 从首页注册传入一个标识 1
	var url =getBasePath()+"/meta/public/userRegister.jsp";
	
	var userData=[
	    {type:"block",width:1200,list:[
	        {type:"label",label:"<iframe frameborder=0 border=0 frameborder='no' marginwidth='0' marginheight='0' scrolling='no' src='"+url+"' style='width:1200px;height:450px;'></iframe>",labelWidth:1200}
	    ]}
	]
    
    var referToApp = function(){
    	//var url =getBasePath()+"/meta/public/userRegister.jsp";
    	//window.location.href = url;
    	userRegistDialog();
    }
</script>
<form id="submitForm" name="submitForm" action="" method="post" onsubmit="return onSubmit();" target="_parent" style="width:100%; height:100%;">
        <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" class="div">
            <tr style="height: 10%">
            </tr>
            <tr>
                <td height="100" bordercolor="#993300" valign="bottom" align="center" >
                    <table border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td>&nbsp;</td>
                            <td class="tc">
                                <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td><img src="<%=request.getContextPath() %>/portal/resource/images/login_teleLogo.gif" style="width:139px;height:45px;"/>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                        <td class="tc" style=" font-size:12px; color:#FFF; vertical-align: middle;" >
                                            <b style="font-size:18px;line-height:24px;">广东电信客户服务分析系统</b><br />
                                        </td>
                                    </tr>
                                </table>
                            </td>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                </td>
            </tr>
            <!--2-->
            <tr>
                <td id="main" align="center"  valign="top">
                    <table border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td valign="top" >
                                <table style="background: url('<%=request.getContextPath() %>/portal/resource/images/login_signIn.jpg') no-repeat;" width="442" height="238" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td width="150" height="85">&nbsp;</td>
                                        <td width="20"></td>
                                        <td align="left"></td>
                                    </tr>
                                    <tr>
                                        <td nowrap="nowrap" height="30" align="right" style="font-size:14px;font-weight:bold">用户名:</td>
                                        <td><img src="<%=request.getContextPath() %>/portal/resource/images/user.gif" width="10" height="15" /></td>
                                        <td align="left">
                                            <input id="loginId" name="loginId" tabindex="1" accesskey="n" type="text"      value="" size="20" checked="checked" style="width: 150px;" onblur="javascript:this.value=this.value.replace(/(^\s*)|(\s*$)/g, '');"/>
                                        </td>
                                        <td align="left" nowrap="nowrap">
	                                        <!-- 
	                                        	<a href="<%=request.getContextPath() %>/meta/public/userApply.jsp">用户申请</a>
	                                         -->
	                                         <a style="position:relative;right:80px" href="#" onclick="referToApp()">用户申请</a>
										 </td>
                                    </tr>
                                    <tr>
                                        <td height="30" align="right" style="font-size:14px; font-weight:bold;">密　码:</td>
                                        <td><img src="<%=request.getContextPath() %>/portal/resource/images/password.gif" width="12" height="15" /></td>
                                        <td align="left">
                                            <input id="password" name="password"  tabindex="2" accesskey="p" type="password" value="" size="20" onpaste="return false" style="width: 150px;"/>
                                           </td>
                                    </tr>
									 <tr>
                                        <td height="30" align="right" style="font-size:14px; font-weight:bold" nowrap="nowrap">验证码:</td>
                                        <td><img src="<%=request.getContextPath() %>/portal/resource/images/valdate.gif" width="10" height="15" /></td>
                                        <td align="left" nowrap="nowrap">
											<input id="validateCode" name="validateCode" tabindex="2" accesskey="x" type="text" value="" size="10"/>
											<img title="点击改变验证码" id="validateCodeImg" src="<%=request.getContextPath() %>/meta/public/verifyCode.jsp?<%=System.currentTimeMillis() %>" style="cursor:pointer;height: 25px;width: 60px;margin-bottom: -9px"/>
											<a href="javascript:changeCode()">看不清，换一张</a>
					                    </td>
                                    </tr>
								    
								    <tr>
									    <td  align="left">&nbsp;</td>
									    <td colspan="2" align="left" valign="middle">
									        <input type="checkbox" name="cbUserName" id="cbUserName" tabIndex="6"/>
									        <strong> 记住用户名</strong>
									        <input type="checkbox" name="cbPassword" id="cbPassword" tabIndex="7"/> 
									        <strong> 记住密码</strong>
									    </td>
								    </tr>
                                    <tr>
                                        <td height="30">&nbsp;</td>
                                        <td></td>
                                        <td align="left"><input type="hidden" name="lt" value="_c22B633C0-6DF4-D137-58A3-46429F62FC74_k65A59DCE-8CEE-8FD0-699D-935811E619C7" />
                                            <input type="hidden" name="_eventId" value="submit"  class="btn-login"  />
                                            <input class="btn-login" id="submit" name="submit" accesskey="l"   value="" tabindex="4" type="submit" />
                                            &nbsp;&nbsp;<input class="btn-chongzhi" name="reset" accesskey="c" value=""  tabindex="5" type="reset" />
                                            <br>

                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="3" height="60" style="padding:5px;color:red;">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td height="10" colspan="4" valign="top" style="font-size:14px;" align="center"><a href='<%=request.getContextPath() %>/portal/resource/other/IE8.exe' target="blank">为保证最佳浏览质量请使用IE8及更高版本，下载插件请点此进入</a></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>

            <!--3-->
            <tr>
                <td height="20" bgcolor="#2970A9">
                    <table width="100%" height="20" border="0" cellpadding="0" cellspacing="0" align="center">
                        <tr align="center">
                            <td align="center"  valign="middle" style="color:#CADDEE;height: 100%">
                                <div style="vertical-align: middle;padding: 0px;position: relative;height: 100%;text-align: center;">
                                    	版权所有：中国电信广东公司</div></td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>


</form>
</body>
</html>


