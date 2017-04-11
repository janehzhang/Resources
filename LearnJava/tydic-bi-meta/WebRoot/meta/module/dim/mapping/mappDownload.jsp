<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="tydic.meta.module.dim.mapping.MappingUploadService" %>
<%@ page import="tydic.meta.module.dim.ExcelUtil" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%@include file="../../../public/head.jsp" %>
<head>
   <script type="text/javascript">
   </script>
</head>
<% response.setHeader("Content-Disposition", "attachment; filename=\"mapping.xls\"");
   response.setContentType("application/x-download");
%>
<body>
   <%
      String tableOwner = request.getParameter("tableOwner");
      List<Map<String,Object>> dataList = null;
      String tableName = request.getParameter("tableName");
      String tablePrefix = request.getParameter("tablePrefix");
      String sysIdStr = request.getParameter("sysId");
      String dimTableIdStr = request.getParameter("dimTableId");
      int sysId =-1;
      int dimTableId = -1;
      if(sysIdStr!=null&&sysIdStr!=""){
    	  sysId = Integer.valueOf(sysIdStr);
      }
      if(dimTableIdStr!=null&&dimTableIdStr!=""){
    	  dimTableId = Integer.valueOf(dimTableIdStr);
      }
      String  path = request.getContextPath();
      MappingUploadService ms = new MappingUploadService();
      ExcelUtil util = new ExcelUtil();
      if(sysId!=-1&&dimTableId!=-1&&tableName!=null&&tablePrefix!=null){
        dataList = ms.getExcelTitleList(tableName,tablePrefix,tableOwner,sysId,dimTableId);
      }
      File excelFile = util.getExcelFile(path,dataList);
      OutputStream ops = null;
		InputStream is =null;
		try {
			 ops = response.getOutputStream();
			 is = new FileInputStream(excelFile);
			int size=1024;
			byte[] buffer=new byte[size];
			int len;
			while((len=is.read(buffer)) != -1){
				ops.write(buffer,0,len);
			}
			is.close();
			out.clear();
			out = pageContext.pushBody();
			
		} catch (IOException e) {
		    
		}
		
   %>
</body>
</html>
