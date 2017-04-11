<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%@include file="../../../public/head.jsp" %>
<head>
  <script type="text/javascript">
  var tableOwner = '<%=request.getParameter("tableOwner")%>';
  </script>
</head>
<body style="background: #ffffff;">
   <div>
     <form enctype="multipart/form-data" action='<%=rootPath %>/upload?fileUploadCalss=tydic.meta.module.dim.mapping.MappUploadImpl&sysId=<%=request.getParameter("sysId")%>&dimTableId=<%=request.getParameter("dimTableId") %>&dimTypeId=<%=request.getParameter("dimTypeId") %>&dimTableName=<%=request.getParameter("dimTableName")%>&dimTablePrefix=<%=request.getParameter("dimTablePrefix")%>&tableOwner=<%=request.getParameter("tableOwner")%>' id="_uploadForm" method="post">
       <input style="width:150px; border: 1px solid #88afe8;margin-top:10px;margin-left:5%;" class="dhxlist_txt_textarea"  name="upload" id="_fileupload" type="file"/>
       <input type="submit" name="submit" value="上传"/>
     </form>
   </div>
   <div id="showMsg" style="border-top:1px solid #88afe8;width: 90%;margin:10px auto;padding-top: 10px;">
              请选择一个excel文件,您也可以<a href="mappDownload.jsp?tableName=<%= request.getParameter("dimTableName")%>&tablePrefix=<%=request.getParameter("dimTablePrefix")%>&sysId=<%=request.getParameter("sysId")%>&dimTableId=<%=request.getParameter("dimTableId") %>&tableOwner=<%=request.getParameter("tableOwner")%>">点击这里</a>下载该维度表的映射模板,请使用Excel2007或者Excel2003格式或者将其转换为CSV格式
   </div>
</body>
</html>
