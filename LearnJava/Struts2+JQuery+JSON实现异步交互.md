Struts2+JQuery+JSON实现异步交互，包括从后台获得单个值、对象、List和Map数据。
并从前台的表达中获取值把值进行序列化通过JQueryajax({})传到后台和后台的对象进行绑定。
第一步：在MyEclipse中创建JavaWeb工厂并把Struts2 和 json的jar包添加到工程中
创建后台：
# UserInfo实体类 
	public class UserInfo implements Serializable {   
	    private static final long serialVersionUID = 3952189513312630860L;   
	    private int userId;   
	    private String userName;   
	    private String password;   
	    public int getUserId() {   
	        return userId;   
	    }   
	    public void setUserId(int userId) {   
	        thuserId = userId;   
	    }   
	    public String getUserName() {   
	        return userName;   
	    }   
	    public void setUserName(String userName) {   
	        thuserName = userName;   
	    }   
	    public String getPassword() {   
	        return password;   
	    }   
	    public void setPassword(String password) {   
	        thpassword = password;   
	    }   
	}  

#Action类
    public class JsonJqueryStruts2Action extends ActionSupport {   
	  
	    private static final long serialVersionUID = 3518833679938898354L;   
	       
	    private String message;  //使用json返回单个值   
	    private UserInfo userInfo; //使用json返回对象   
	    private List<UserInfo> userInfosList;     //使用josn返回List对象   
	    private Map<String,UserInfo> userInfosMap;    //使用json返回Map对象   
	    //为上面的的属性提供get，Set方法   
	    省略
	    /**   
	     * <p>   
	     *  返回单个值     
	     */   
	    public String returnMessage(){   
	        thmessage = "成功返回单个值";    
	        return "message";    
	    }   
	    /**   
	     * <p>   
	     *  返回UserInfo对象  
	     */   
	    public String returnUserInfo(){   
	        userInfo = new UserInfo();   
	        userInsetUserId(10000);    
	        userInsetUserName("张三");    
	        userInsetPassword("000000");    
	        return "userInfo";    
	    }   
	    /**   
	     *  返回List对象 
	     */   
	    public String returnList(){   
	        userInfosList = new ArrayList<UserInfo>();   
	        UserInfo u1 = new UserInfo();   
	        setUserId(10000);    
	        setUserName("张三");    
	        setPassword("000000");    
	        UserInfo u2 = new UserInfo();   
	        setUserId(10001);    
	        setUserName("李四");    
	        setPassword("111111");    
	        UserInfo u3 = new UserInfo();   
	        setUserId(10002);    
	        setUserName("王五");    
	        setPassword("222222");    
	        UserInfo u4 = new UserInfo();   
	        setUserId(10003);    
	        setUserName("赵六");    
	        setPassword("333333");    
	        userInfosLiadd(u1);   
	        userInfosLiadd(u2);   
	        userInfosLiadd(u3);   
	        userInfosLiadd(u4);   
	        return "list";    
	    }   
	    /**   
	     * <p>   
	     *  返回Map对象   
	     * </p>   
	     * @return   
	     */   
	    public String returnMap(){   
	        userInfosMap = new HashMap<String,UserInfo>();   
	        UserInfo u1 = new UserInfo();   
	        setUserId(10000);    
	        setUserName("张三");    
	        setPassword("000000");    
	        UserInfo u2 = new UserInfo();   
	        setUserId(10001);    
	        setUserName("李四");    
	        setPassword("111111");    
	        UserInfo u3 = new UserInfo();   
	        setUserId(10002);    
	        setUserName("王五");    
	        setPassword("222222");    
	        UserInfo u4 = new UserInfo();   
	        setUserId(10003);    
	        setUserName("赵六");    
	        setPassword("333333");    
	        userInfosMput(getUserId()+"", u1);    
	        userInfosMput(getUserId()+"", u2);    
	        userInfosMput(getUserId()+"", u3);    
	        userInfosMput(getUserId()+"", u4);    
	        return "map";    
	    }   
	    /**   
	     * <p>   
	     *  获得对象，也就是通过表达获得对象(异步的)   
	     * </P>   
	     * @return   
	     */   
	    public String gainUserInfo(){   
	        Systoprintln("用户ID："+userIngetUserId());    
	        Systoprintln("用户名："+userIngetUserName());    
	        Systoprintln("密码："+userIngetPassword());    
	        return "userInfo";    
	    }   
	    //获得单个值就不用写了和平常一样     
	} 
 
  struxml
	<?xml version=0" encoding="UTF-8" ?>   
	<!DOCTYPE struts PUBLIC   
	    "-//Apache Software Foundation//DTD Struts Configuration0//EN"   
	    "http://struapacorg/dtds/struts0.dtd">    
	  
	<struts>   
	    <package name="default" namespace="/" extends="json-default">    
	        <action name="jsontest" class="struts2jsonjqueteactiJsonJqueryStruts2Action">    
	            <!-- 返回单个值的result -->   
	            <result name="message" type="json"></result>    
	            <!-- 返回UserInfo对象的 -->   
	            <result name="userInfo" type="json"></result>    
	            <!-- 返回List对象的 -->   
	            <result name="list" type="json"></result>    
	            <!-- 返回Map对象的 -->   
	            <result name="map" type="json"></result>    
	        </action>   
	    </package>   
	</struts> 

wxml 和 jsp页面：
	<%@ page language="java" pageEncoding="GBK"%>  
	<%    
	    String path = requegetContextPath();    
	%>   
	  
	<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML01 Transitional//EN">   
	<html>   
	  <head>   
	    <title>Struts2+JQuery+JSON</title>   
	    <meta http-equiv="pragma" content="no-cache">   
	    <meta http-equiv="cache-control" content="no-cache">
	    <meta http-equiv="expires" content="0">        
	    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">   
	    <meta http-equiv="description" content="This is my page">   
	    <script type="text/javascript" src="<%=path %>/js/jquejs"></script>   
	    <script type="text/javascript" src="<%=path %>/js/jsjs"></script>   
	  </head>   
	  <body>   
	    <input id="getMessage" type="button" value="获取单个值"/>&nbsp;&nbsp;    
	    <input id="getUserInfo" type="button" value="获取UserInfo对象"/>&nbsp;&nbsp;    
	    <input id="getList" type="button" value="获取List对象"/>&nbsp;&nbsp;    
	    <input id="getMap" type="button" value="获取Map对象"/>&nbsp;&nbsp;    
	    <br>   
	    <br>   
	    <br>   
	    <!-- 要显示信息的层 -->   
	    <div id="message"></div>   
	    <form>   
	        用户ID：<input name="userInuserId" type="text"/><br/>   
	        用户名：<input name="userInuserName" type="text"/><br/>   
	        密&nbsp;&nbsp;&nbsp;码：<input name="userInpassword" type="text"/><br>   
	        <input id="regRe" type="button" value="注册"/>   
	    </form>   
	  </body>
	</html> 
	

Js部分：

//初始加载页面时   
$(documenready(function(){    
 //为获取单个值的按钮注册鼠标单击事件    
 $("#getMessageclick(function(){    
 getJSON("jsontest!returnMessaaction",function(data){ 
   //操作符可以从damessage中获得Action中message的值    
   $("#messagehtml("<font color='red'>"+damessage+"</font>");    
  });   
 }); 

 //为获取UserInfo对象按钮添加鼠标单击事件    
 $("#getUserInfoclick(function(){    
 getJSON("jsontest!returnUserInaction",function(data){    
   //清空显示层中的数据    
   $("#messagehtml("");    
   //为显示层添加获取到的数据    
   //获取对象的数据用dauserIn属性    
   $("#messageappend("<div><font color='red'>用户ID："+dauserInuserId+"</font></div>")    
        append("<div><font color='red'>用户名："+dauserInuserName+"</font></div>")    
        append("<div><font color='red'>密码："+dauserInpassword+"</font></div>")    
  });   
 }); 

 //为获取List对象按钮添加鼠标单击事件    
 $("#getListclick(function(){    
 getJSON("jsontest!returnLiaction",function(data){    
   //清空显示层中的数据    
   $("#messagehtml("");    
   //使用jQuery中的each(data,function(){});函数    
   //从dauserInfosList获取UserInfo对象放入value之中    
  each(dauserInfosList,function(i,value){    
    $("#messageappend("<div>第"+(i+1)+"个用户：</div>")    
     append("<div><font color='red'>用户ID："+valuserId+"</font></div>")    
        append("<div><font color='red'>用户名："+valuserName+"</font></div>")    
        append("<div><font color='red'>密码："+valpassword+"</font></div>");    
   }); 
  });   
});   

//为获取Map对象按钮添加鼠标单击事件    
 $("#getMapclick(function(){    
 getJSON("jsontest!returnMaction",function(data){    
  //清空显示层中的数据    
   $("#messagehtml("");    
   //使用jQuery中的each(data,function(){});函数    
   //从dauserInfosList获取UserInfo对象放入value之中    
   //key值为Map的键值    
  each(dauserInfosMap,function(key,value){    
    $("#messageappend("<div><font color='red'>用户ID："+valuserId+"</font></div>")    
        append("<div><font color='red'>用户名："+valuserName+"</font></div>")    
        append("<div><font color='red'>密码："+valpassword+"</font></div>");    
   });   
  });   
 }); 
 //向服务器发送表达数据    
 $("#regReclick(function(){    
  //把表单的数据进行序列化    
  var params = $("formserialize();    
  //使用jQuery中ajax({});Ajax方法    
 ajax({   
   url:"jsontest!gainUserInaction",    
   type:"POST",    
   data:params,   
   dataType:"json",    
   success:function(data){    
    //清空显示层中的数据    
   $("#messagehtml("");    
   //为显示层添加获取到的数据    
   //获取对象的数据用dauserIn属性    
   $("#messageappend("<div><font color='red'>用户ID："+dauserInuserId+"</font></div>")    
        append("<div><font color='red'>用户名："+dauserInuserName+"</font></div>")    
        append("<div><font color='red'>密码："+dauserInpassword+"</font></div>")    
   }   
  });   
 });   
}); 

