<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="java.io.*"%>
<%!
public static String toUtf8String(String s) {
	StringBuffer sb = new StringBuffer();
	for (int i=0;i<s.length();i++) {
		char c = s.charAt(i);
		if (c >= 0 && c <= 255) {
			sb.append(c);
		} else {
			byte[] b;
			try {
				b = Character.toString(c).getBytes("utf-8");
			} catch (Exception ex) {		
				b = new byte[0];
			}
			for (int j = 0; j < b.length; j++) {
				int k = b[j];
				if (k < 0) k += 256;
				sb.append("%" + Integer.toHexString(k).toUpperCase());
			}
		}
	}
	return sb.toString();
}
%>
<%
try{
	   request.setCharacterEncoding("UTF-8");
	   response.setCharacterEncoding("GBK");
	   String s = new String(request.getParameter("file").getBytes("ISO-8859-1"),  "UTF-8");
	   String realpath=request.getRealPath("/");
	   System.out.println(realpath+s);

//s=new String(s.getBytes("ISO8859-1"),"GBK");
File t_file = new java.io.File(realpath+s);
if (!t_file.exists()){%>
	<script language="JavaScript">
		alert("文件已经不存在!");
		//history.go(-1);
	</script>
<%
		return;
}//end if
InputStream in = new FileInputStream (t_file); 
if(in != null){
	String fs = t_file.getName();
	int l=(int)t_file.length();
	//out.clear();
	response.reset();
	response.setHeader("Content-Disposition", "attachment;filename=\"" +new String(fs.getBytes(),"ISO8859-1") + "\"");	
	byte[] b = new byte[l]; 
	int len = 0; 

	OutputStream os = response.getOutputStream();
	while((len=in.read(b))>0){		
		os.write(b,0,len);
		//os.flush();
	} 	
	os.flush();
	os.close();
	in.close(); 
	os=null;
	//response.flushBuffer();
    out.clear();
    out = pageContext.pushBody();
	
}//end if
}catch(Exception e){
	//e.printStackTrace();
	out.clear();
    out = pageContext.pushBody();
}
%>