<%@page language="java" import="java.util.*,java.text.SimpleDateFormat,java.util.Calendar,tydic.meta.common.DateUtil" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
    <title>回访结果</title>
    <meta http-equiv="pragma" content="no-cache" />
    <%@include file="../../../../public/head.jsp" %>
    <%@include file="../../../../public/include.jsp" %>
    <script type="text/javascript" src="<%=path%>/dwr/interface/ADSLVisitListAction.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/fpage.css" />
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style01.css">
    <link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/style02.css">
    <%
      //  String busiNo=       (String)request.getParameter("busiNo");
      //  String custNo=       (String)request.getParameter("custNo");
      // String areaName=     new String(request.getParameter("areaName").getBytes("ISO-8859-1"), "GBK");
       // String createTime=   (String)request.getParameter("createTime");
       String phone=         new String(request.getParameter("phone").getBytes("ISO-8859-1"), "GBK");
       String date=         new String(request.getParameter("date").getBytes("ISO-8859-1"), "GBK");
        String name=         new String(request.getParameter("name").getBytes("ISO-8859-1"), "GBK");
    %>
</head>
<body>
   <form id="form1" name="form1">
        <input type="hidden"  id="phone"   name="phone"           value="<%=phone %>" />
        <input type="hidden"" id="date"   name="date"           value="<%=date %>" />
	    <table width='100%' height="100%" border='0' cellpadding='0px' cellspacing='0px'>
				<tr>
				   <td  width="65%"></td>
				</tr>
			  
				<tr>
				    <td colspan='2'><textarea id="resultName" name="resultName" rows="13" style="width: 100%"><%=name %></textarea></td>
				</tr> 
			    <tr>
				   <td colspan='2' align="center"">
				       <input type="button"     name="save"    onclick="saveInfo();" value="保存"/>
				       &nbsp;&nbsp;&nbsp;&nbsp;
					   <input type="button"     name="exit"    onclick="exitInfo();" value="退出"/>
				   </td>
				</tr>
	     </table>
    </form>
</body>
</html>
<script type="text/javascript">
/**
 * 主页面初始化
 */
dhtmlx.image_path = getDefaultImagePath();
dhtmlx.skin = getSkin();
var base = getBasePath();
var dwrCaller=new biDwrCaller();
var queryform =$('form1');
var indexInit=function(){
}

var saveInfo=function()
{
   var name=$('resultName').value;
   var phone=$('phone').value;
   var date=$('date').value;
  
   //if(name!="")
   //{
	  var map=new Object();
	  map.phone=phone;
	  map.date=date;
	  map.name=name;
	  dhx.showProgress("正在执行......");
	  ADSLVisitListAction.updateHbVisitResult(map, function (res) {
		   dhx.closeProgress();
				        if (res == null) {
				            dhx.alert("查询失败,请稍后重试!");
				            return;
				        }
						 window.returnValue=name==""?'&nbsp;&nbsp;':name;
	                     window.close();
         });

   //}
  // else
   //{
	 // alert("请您填写回访结果!");
   //}

}
var exitInfo=function(){window.close();}
dhx.ready(indexInit);
</script>