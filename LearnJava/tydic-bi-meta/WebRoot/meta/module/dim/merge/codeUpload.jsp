<%@ page import="java.net.URLDecoder" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%@include file="../../../public/head.jsp" %>
<head>
   <script type="text/javascript">
   </script>
</head>
<body style="background: #ffffff;">
   <div> 
     <form enctype="multipart/form-data" action='<%=rootPath %>/upload?fileUploadCalss=tydic.meta.module.dim.merge.CodeUploadImpl&dimTableId=<%=request.getParameter("dimTableId")%>&dimTypeId=<%=request.getParameter("dimTypeId")%>&tableOwner=<%=request.getParameter("tableOwner")%>&dimTablePrefix=<%=request.getParameter("dimTablePrefix")%>&dimTableName=<%=request.getParameter("dimTableName")%>' id="_uploadForm" method="post">
       <input style="width:150px; border: 1px solid #88afe8;margin-top:10px;margin-left:5%;" class="dhxlist_txt_textarea"  name="upload" id="_fileupload" type="file"/>
       <input type="submit" name="submit" value="上传"/>
     </form>
   </div>
    <div id="showMsg" style="border-top:1px solid #88afe8;width: 90%;margin:10px auto;padding-top: 10px;">
               请选择一个excel文件,您也可以<a href="codeDownload.jsp?tableId=<%=request.getParameter("dimTableId")%>&tableName=<%= request.getParameter("dimTableName")%>&tablePrefix=<%=request.getParameter("dimTablePrefix")%>&tableOwner=<%=request.getParameter("tableOwner")%>">点击这里</a>下载Excel模板,请使用Excel2007或者Excel2003或者将其转换为CSV格式
    </div>
</body>
</html>
