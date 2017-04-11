<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8" import="tydic.frame.SystemVariable" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>单点登录</title>
    </head>
    <body>
    <%
      String requestMethod=request.getMethod();
      requestMethod=requestMethod.toUpperCase();
      request.setCharacterEncoding("UTF-8");
      response.setCharacterEncoding("UTF-8");

        //发送到EAC验证
         String posturl="";
         String defaulturl=request.getRequestURL().toString();
         EIAC.EAC.SSO.AppSSOBLL app=new EIAC.EAC.SSO.AppSSOBLL();
         String IASID="";
         String IASKey="";
         String SSOURL="";
         String TimeStamp="";
         String ReturnURL="";         
         String UserAccount="";
         String Result="";
         String ErrorDescription="";
         String Authenticator="";
         if(request.getParameter("IASID")!=null)IASID=request.getParameter("IASID");
         if(request.getParameter("ReturnURL")!=null)ReturnURL=request.getParameter("ReturnURL");
         if(request.getParameter("UserAccount")!=null) UserAccount=request.getParameter("UserAccount");
         if(request.getParameter("Result")!=null) Result=request.getParameter("Result");
         if(request.getParameter("ErrorDescription")!=null) ErrorDescription=request.getParameter("ErrorDescription");
         if(request.getParameter("Authenticator")!=null) Authenticator=request.getParameter("Authenticator");
         if(request.getParameter("TimeStamp")!=null)TimeStamp=request.getParameter("TimeStamp");
         
         
        //可以读配置文件
         IASID=  SystemVariable.getString("IASID", "");
         IASKey= SystemVariable.getString("IASKey", "");
         SSOURL= SystemVariable.getString("PostUrl", "");
         //取时间
         java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         java.util.Date currentTime = new java.util.Date();//得到当前系统时间
         String str_date1 = formatter.format(currentTime); //将日期时间格式化
         if(TimeStamp=="")TimeStamp=str_date1.toString(); 

         //得到post的html 
         if(UserAccount==null||UserAccount=="")
         {         
             posturl=app.PostString(IASID,TimeStamp,defaulturl,null);  
             out.print(posturl);  //post to EAC(SSO)
         }
         //结束
        
         //接收从EAC返回的
         if(UserAccount!=null&&UserAccount!="")
         {
            if(!Result.equals("0"))
                 {
                   out.print("Result验证不成功！"); 
                   out.print(IASID+":"+TimeStamp+":"+UserAccount+":"+Result+":"+ErrorDescription+":"+Authenticator);
                   return;
                 }
                if(app.ValidateFromEAC(IASID,TimeStamp,UserAccount,Result,ErrorDescription,Authenticator))
                 {
                  Cookie  newCookie   =  new Cookie("UserAccount",UserAccount);   
                  response.addCookie(newCookie);
                  response.sendRedirect("index.jsp");
                  out.print("成功");
                 }
             else{
                  out.print("验证错误");
                  out.print(IASID+":"+TimeStamp+":"+UserAccount+":"+Result+":"+ErrorDescription+":"+Authenticator);
                 }
             
          }      
         //接收从
    %>    
    </body>
</html>
