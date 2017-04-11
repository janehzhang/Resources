<%@ page import="org.apache.commons.fileupload.DiskFileUpload" %>
<%@ page import="java.util.List" %>
<%@ page import="tydic.frame.SystemVariable" %>
<%@ page import="org.apache.commons.fileupload.FileItem" %>
<%@ page import="java.io.File" %>
<%@ page import="tydic.frame.common.Log" %>
<%@ page import="org.apache.commons.io.FileUtils" %>
<%@ page contentType="text/html;charset=UTF-8"%>
<%
    request.setCharacterEncoding("UTF-8");
    response.setCharacterEncoding("UTF-8");
    DiskFileUpload fu = new DiskFileUpload();
    String rootPath=application.getRealPath("/");
    String imagePath=SystemVariable.getString("attachmentPath","");
    try {
        List fileItems = fu.parseRequest(request);
        FileItem fi = (FileItem)fileItems.get(0);//这里只是取得第一个文件
        File dir=new File(rootPath,imagePath);
        //获取文件后缀
        String fileName=fi.getName();
        if(fileName==null){
            fileName="";
        }
        Log.debug("-------------"+fileName);
        String fileNameSuffix=fileName.substring(fileName.indexOf(".")>0?fileName.lastIndexOf("."):0,fileName.length());
        fileName=System.currentTimeMillis()+fileNameSuffix;
        FileUtils.forceMkdir(dir);
        fi.write(new File(dir,fileName));//保存
        out.write("<script>var fileName='"+imagePath+"/"+fileName+"'</script>");
    } catch (Exception e) {
        Log.error(null,e);
    }

%>