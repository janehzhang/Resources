<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCellStyle"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFFont"%>
<%@ page import="org.apache.poi.hssf.util.*"%>
<%@ page import="tydic.portalCommon.coreLink.util.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="tydic.meta.common.yhd.utils.Pager,tydic.frame.common.utils.Convert"%>
<%@ page import="tydic.portalCommon.procedure.visitDetail.update.ADSLVisitUpdateListAction"%>
<iframe id="hiddenStream" name="hiddenStream" width="0" height="0"  frameborder="0" src=""></iframe>
<%
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
try{
	String rootPath=request.getContextPath();
	String excelName = new String(request.getParameter("excelTitle").getBytes("ISO-8859-1"), "UTF-8");
    String condition = new String(request.getParameter("excelCondition").getBytes("ISO-8859-1"), "UTF-8");
	String excelHeader = new String(request.getParameter("excelHeader").getBytes("ISO-8859-1"), "UTF-8");
	String excelData = new String(request.getParameter("excelData").getBytes("ISO-8859-1"), "UTF-8");
	//String filePath =(String)request.getParameter("filePath");
	//String filePath="/upload/excelHeader/serviceSatisfiedHB_MonTmethod.xls";
	String filePath="/upload/excelHeader/sample.xls";
	//int row =Integer.parseInt((String)request.getParameter("row"));
	int row =2;
	String totalCount= request.getParameter("totalCount");
	//String dateTime=    request.getParameter("dateTime");
	String startTime=    request.getParameter("startTime");
	String endTime=    request.getParameter("endTime");
    String zoneCode=   request.getParameter("zoneCode");
    String indexType=   request.getParameter("indexType");
    String satisType=   request.getParameter("satisType");
	//String dateType=  request.getParameter("dateType");
	//String actType=   request.getParameter("vTypeId");
	//String demension=   request.getParameter("demension");
	   
	excelData = excelData.replaceAll("&nbsp;&nbsp;", " ");
	String[] fieldsName = excelHeader.split(",");
	String[] dataValues = excelData.split("]");

	int fieldNum = fieldsName.length;
	String realPath = request.getRealPath("/upload/template/"+ excelName + ".xls");
	File xlsFile = new File(realPath);
	String fileToBeRead = request.getRealPath(filePath);
	HSSFWorkbook workBook = new HSSFWorkbook(new FileInputStream(fileToBeRead));  
    List data = new ArrayList();
	short row_num = 0;
	short col_num = 0;
	HSSFSheet sheet = workBook.getSheet(workBook.getSheetName(0));//第1 个Sheet 对象
    sheet.createFreezePane(0, row);// 冻结    列和行
	ExcelUtil excel = new ExcelUtil(workBook, sheet);	
	
	HSSFRow rows = null;
	HSSFCell cells = null;
	rows = sheet.createRow(row_num++);
	cells = rows.createCell(col_num);

	//给excel加标题
	excel.createRegion(rows, cells, row_num - 1, fieldNum-1)
			.setCellStyle(excel.getBodyTitleStyle());
	cells.setCellValue(excelName);

	rows = sheet.createRow(row_num++);
	cells = rows.createCell(col_num);
	
	//给excel加条件
   if(null!=condition && ""!=condition){
	excel.createRegion(rows,cells,row_num-1, fieldNum-1).setCellStyle(excel.getBodyTitleStyle());
	cells.setCellValue("条件："+condition);
	}	
	
 //给excel的列头加中文字段名
	int temp=row-2;
	for(int i=0;i<temp;i++){
		row_num++;
	}
	
	Map<String, Object> paramMap=new HashMap<String,Object>();
	   paramMap.put("startTime",startTime);
	   paramMap.put("startTime",startTime);
	   paramMap.put("endTime",endTime);
	   paramMap.put("indexType",indexType);
	   paramMap.put("satisType",satisType);
	   //paramMap.put("demension",demension);
	   
	   Pager pager = Pager.getInstance();
	   pager.setCurrNum(1);//当前页,从第1页开始
	   pager.setSize(2000); //每次查询多少条
	   pager.setTotalNum(Convert.toInt(totalCount));
	 
	   ADSLVisitUpdateListAction  action = new ADSLVisitUpdateListAction();
	   List<Map<String, Object>> dataColumn=null;
	   for(int  i=1; i <= pager.getTotalPage(); i++)
       {
   	   pager.setCurrNum(i);
   	   paramMap.put("page",pager);
	   	   Map<String, Object>  resultMap=action.expTsTxtData(paramMap);
	   	   String[] headColumn=(String[])resultMap.get("headColumn");
	   	   dataColumn=(List<Map<String, Object>>)resultMap.get("dataColumn");  
	   	   //row_num+=10;
	   	   if(i==1){
	   	   for(String headStr: headColumn)
			    	{
			    		//String tempColumn=Convert.toString(map.get(headStr)); 
			    		if(!headStr.contains("_")){
			    			data.add(headStr);
			    		}
			    		
			    	} 
			    	
			    	excel.createRow(data, row_num++, col_num, excel.getBodyStyle());
			    	 data.clear(); 
			    	//col_num=0;
			    	}
		       for(Map<String, Object> map: dataColumn)
		       {
			    	for(String headStr: headColumn)
			    	{
			    		String tempColumn=Convert.toString(map.get(headStr)); 
			    		 if(headStr.contains("_")){
			    		    
			    		 }else{
			    			 data.add(tempColumn.replaceAll("&nbsp;&nbsp;", ""));
			    		 }
			    	} 
			         excel.createRow(data, row_num++, col_num, excel.getBodyStyle());
			    	 data.clear(); 
		       }
       }
       
	   excel.save(realPath);

	%>
	<SCRIPT LANGUAGE="JavaScript">
  	parent.dhx.closeProgress();
  	var path="<%=rootPath %>/portalCommon/module/procedure/impExcel/iframe/outStreamIframeExcel.jsp";
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
%>