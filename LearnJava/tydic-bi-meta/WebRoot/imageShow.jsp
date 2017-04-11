<%@ page language="java" contentType="image/jpeg;charset=utf-8" %>
<%@page import="tydic.fusioncharts.OracleBlob"%>
<%OracleBlob ob=new OracleBlob("jdbc:oracle:thin:@132.121.82.154:1521:khfwfx2","meta_user","Aa123456");
    //图片在数据库中的记录ID
    String strID = request.getParameter("imageID");
    String exportFormat = request.getParameter("exportFormat");
    response.setContentType("image/"+exportFormat);
    response.addHeader("Content-Disposition", "attachment; filename=\""+strID+"."+exportFormat+"\"");
    byte[] data = null;
    if(strID != null){
       //获取图片的byte数据
       data = ob.GetImgByteById(null,strID);
       ServletOutputStream op = response.getOutputStream();        
       op.write(data, 0, data.length);
       op.close(); 
       op = null;
       response.flushBuffer();
       //清除输出流，防止释放时被捕获异常
       out.clear();
       out = pageContext.pushBody();
    }%>