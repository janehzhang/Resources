<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="tydic.portalCommon.procedure.rptOpenDetail.RptOpenDetailAction"%>
<%@ page import="tydic.meta.common.yhd.utils.Pager,tydic.frame.common.utils.Convert"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<iframe id="hiddenStream" name="hiddenStream" width="0" height="0"  frameborder="0" src=""></iframe>
<%
       String rootPath=request.getContextPath();
	   String excelName =   new String(request.getParameter("excelTitle").getBytes("ISO-8859-1"),  "UTF-8");
	   String excelHeader = new String(request.getParameter("excelHeader").getBytes("ISO-8859-1"), "UTF-8");
	   String totalCount= request.getParameter("totalCount");
	   String startTime=  request.getParameter("startTime");
	   String endTime=    request.getParameter("endTime");
	   String zoneCode=   request.getParameter("zoneCode");
	   
	   String realPath = request.getRealPath("/upload/template/"+ excelName + ".txt");
	   FileWriter  fw=null;
	   BufferedWriter bw=null;
	   
try{	   
	   File file=new File(realPath);
		if(!file.exists()){
			file.createNewFile();
		}
	   fw=new FileWriter(file);
	   bw= new BufferedWriter(fw);
	   
	   bw.write(excelHeader.replaceAll(",","|"));//表格头部
	   bw.newLine();
       bw.flush();
	   
	   Map<String, Object> paramMap=new HashMap<String,Object>();
	   paramMap.put("startTime",startTime);
	   paramMap.put("endTime",endTime);
	   paramMap.put("zoneCode",zoneCode);
	   Pager pager = Pager.getInstance();
	   pager.setCurrNum(1);//当前页,从第1页开始
	   pager.setSize(2000); //每次查询多少条
	   pager.setTotalNum(Convert.toInt(totalCount));
	 
	   RptOpenDetailAction  servlet = new RptOpenDetailAction();
	   List<Map<String, Object>> dataColumn=null;
	   for(int  i=1; i <= pager.getTotalPage(); i++)
        {
    	   pager.setCurrNum(i);
    	   paramMap.put("page",pager);
	   	   Map<String, Object>  resultMap=servlet.expTxtData(paramMap);
	   	   //Thread.sleep(1000);
	   	   String[]                  headColumn=(String[])resultMap.get("headColumn");
	   	   dataColumn=(List<Map<String, Object>>)resultMap.get("dataColumn");
	   	   for(Map<String, Object> map: dataColumn)
	   	   {
	   		  for(String headStr: headColumn)
	   		  {
	   			bw.write(Convert.toString(map.get(headStr)).replaceAll("&nbsp;&nbsp;", "") +"|" );//表格数据
	   		  }
	   		    bw.newLine(); 
	   		    bw.flush();
	   	   }
	   	   dataColumn.clear();
        }
%>	
<SCRIPT LANGUAGE="JavaScript">
	parent.dhx.closeProgress();
	var path="<%=rootPath %>/portalCommon/module/procedure/impExcel/iframe/outStreamIframe.jsp";
    document.getElementById("hiddenStream").src=path+"?excelName=<%=excelName %>";
</SCRIPT>
<%    

   }
   catch(Exception e)
   {
%>	
 <SCRIPT LANGUAGE="JavaScript">
    alert("导出文件出错,请重试！");
 	parent.dhx.closeProgress();
</SCRIPT>
<% 	   
	   
	   e.printStackTrace();
   }
    finally
   {
       bw.close();	
       fw.close();
	   //System.out.println("关闭IO-->"+realPath);
   }   
 	    
%>



