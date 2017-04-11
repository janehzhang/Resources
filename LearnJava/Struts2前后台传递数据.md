Struts2从后台传递数据到前台的主要方法有两种主要方式：
一 和Servlet API耦合的访问方式
二 和Servlet API解耦的访问方式

********************************************************************
##一 和Servlet API耦合的访问方式
###1、 采用Request （HttpServletRequest）对象来传递数据
（1）在Action类文件中
（A） 导入ServletActionContext类：import org.apache.struts2.ServletActionContext;
（B） 获得request对象，具体的方法如下:
HttpServletRequest request = ServletActionContext.getRequest ();
（C）通过setAttribute()方法把需要传递的数据对象放入request对象中：
request.setAttribute("key",Object);

（2）JSP文件中，有多种方法可以获得被传递的数据对象，比如：
(A) request.getAttribute("key")获得被传递的数据对象。
(B) <s:iterator value="#request.key"> 获得被传递的数据对象。

###2、采用application (ServletContext) 对象来传递数据
（1）在Action类文件中
（A） 导入ServletActionContext类：
import org.apache.struts2.ServletActionContext;
（B） 获得application对象，具体的方法如下:
ServletContext application = ServletActionContext.getServletContext ();
（C）通过setAttribute()方法把需要传递的数据对象放入application对象中：
application.setAttribute("key",Object);

（2）JSP文件中，有多种方法可以获得被传递的数据对象，比如：
(A) application.getAttribute("key")获得被传递的数据对象。
(B)<s:iterator value="#application.key"> 获得被传递的数据对象。

###3、采用session (HttpSession) 对象来传递数据
（1）在Action类文件中
（A） 导入ServletActionContext类：
import org.apache.struts2.ServletActionContext;
（B） 获得session对象，具体的方法如下:
HttpSession session = ServletActionContext.getRequest ().getSession();
（C） 通过setAttribute()方法把需要传递的数据对象放入session对象中：
session.setAttribute("key",Object);

（2）JSP文件中，有多种方法可以获得被传递的数据对象，比如：
(A) session.getAttribute("key")获得被传递的数据对象。
(B) <s:iterator value="#session.key"> 获得被传递的数据对象。

##二和Servlet API解耦的访问方式

###1、 采用Request （HttpServletRequest对应的Map对象）对象来传递数据
（1）在Action类文件中
（A） 导入ActionContext类：
import com.opensymphony.xwork2.ActionContext;
（B） 获得request对象，具体的方法如下:
ActionContext context= ActionContext.getContext();
Map request = (Map)context.get("request");
（C）通过put()方法把需要传递的数据对象放入request对象中：
request.put("key",Object);

（2）JSP文件中，有多种方法可以获得被传递的数据对象，比如：
(A) request.getAttribute("key")获得被传递的数据对象。
(B) request.get("key")获得被传递的数据对象。
(C) <s:iterator value="#request.key"> 获得被传递的数据对象。
(D) requestScope.key 获得被传递的数据对象。

###2、采用application (ServletContext对应的Map对象) 对象来传递数据
（1）在Action类文件中
（A） 导入ActionContext类：
import com.opensymphony.xwork2.ActionContext;
（B） 获得application对象，具体的方法如下:
ActionContext context= ActionContext.getContext();
Map application = (Map)context.getApplication();
（C）通过put()方法把需要传递的数据对象放入application对象中：
application.put("key",Object);

（2）JSP文件中，有多种方法可以获得被传递的数据对象，比如：
(A) application.getAttribute("key")获得被传递的数据对象。
(B) application.get("key")获得被传递的数据对象。
(C) <s:iterator value="#application.key"> 获得被传递的数据对象。
(D) applicationScope.key 获得被传递的数据对象。



###3、采用session (HttpSession对应的Map对象) 对象来传递数据
（1）在Action类文件中
（A） 导入ActionContext类
（B） 获得session对象，具体的方法如下:
ActionContext context= ActionContext.getContext();
Map session = (Map)context.getSession();
（C）通过put()方法把需要传递的数据对象放入session对象中：
session.put("key",Object);

（2）JSP文件中，有多种方法可以获得被传递的数据对象，比如：
(A) session.getAttribute("key")获得被传递的数据对象。
(B) session.get("key")获得被传递的数据对象。
(C) <s:iterator value="#session.key"> 获得被传递的数据对象。
(D) sessionScope.key 获得被传递的数据对象。

