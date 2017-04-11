<%@ page import="java.net.URLDecoder" %>
<%@ page import="java.io.FileOutputStream" %>
<%@ page import="java.io.OutputStream" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.FileInputStream" %>
<%@ page import="tydic.meta.module.dim.merge.CodeUpLoadService" %>
<%@ page import="tydic.meta.module.dim.ExcelUtil" %>
<%@ page import="java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%@include file="../../../public/head.jsp" %>
<head>
    <script type="text/javascript">
    </script>
</head>
<% response.setHeader("Content-Disposition", "attachment; filename=\"code.xls\"");
    response.setContentType("application/x-download");
%>
<body>
<%
    String tableName = request.getParameter("tableName");
    String tablePrefix = request.getParameter("tablePrefix");
    String tableOwner = request.getParameter("tableOwner");
    String tableId = request.getParameter("tableId");
    String path = request.getContextPath();
    CodeUpLoadService cs = new CodeUpLoadService();
    ExcelUtil util = new ExcelUtil();
    Map<String, Object> dataList = cs.getExcelTitleList(Integer.parseInt(tableId), tablePrefix, tableOwner);
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    list.add(dataList);
    File excelFile = util.getExcelFile(path, list);
    OutputStream ops = null;
    InputStream is = null;
    try {
        ops = response.getOutputStream();
        is = new FileInputStream(excelFile);
        int size = 1024;
        byte[] buffer = new byte[size];
        int len;
        while ((len = is.read(buffer)) != -1) {
            ops.write(buffer, 0, len);
        }
        is.close();
        out.clear();
        out = pageContext.pushBody();

    } catch (IOException e) {

    }

%>
</body>
</html>
