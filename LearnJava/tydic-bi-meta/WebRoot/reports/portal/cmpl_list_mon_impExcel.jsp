<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCellStyle"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFFont"%>
<%@ page import="org.apache.poi.hssf.util.*"%>
<%@ page import="tydic.portalCommon.coreLink.util.*"%>
<%@ page import="tydic.reports.ReportsMonDAO"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%
	String title = request.getParameter("title");
	title= new String(title.trim().getBytes("ISO-8859-1"), "UTF-8");  
	String colNames = request.getParameter("colNames");
    colNames= new String(colNames.trim().getBytes("ISO-8859-1"), "UTF-8");  
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");
    String  zoneId  = request.getParameter("zoneId");
    String  prodTypeId  = request.getParameter("prodTypeId");
    String  cmplBusiTypeId  = request.getParameter("cmplBusiTypeId");
    String[] CfieldsName =colNames.split(",");
    int fieldNum=CfieldsName.length;

	short row_num = 0;
	short col_num = 1;
	
    String realPath = request.getRealPath("/upload/template/"+title+".xls");
    File xlsFile = new File(realPath);
    List data = new ArrayList();
	HSSFWorkbook workBook = new HSSFWorkbook();
    HSSFSheet sheet = workBook.createSheet("清单报表");
    ExcelUtil excel = new ExcelUtil(workBook,sheet);
	 
	HSSFRow rows = null;
	HSSFCell cells = null;
	rows = sheet.createRow(row_num++);
	cells = rows.createCell(col_num);
	
	//给excel加标题
    excel.createRegion(rows,cells,row_num-1,fieldNum).setCellStyle(excel.getBodyStyle());
	cells.setCellValue(title);
	//给excel的列头加中文字段名
	for(String fieldName:CfieldsName){
		data.add(fieldName);
	}
    excel.createRow(data,row_num++,col_num,excel.getBodyStyle());
    data.clear();
   
   ReportsMonDAO dao=new ReportsMonDAO();
   Map<String, Object> queryData=new HashMap<String,Object>();
   queryData.put("startDate",startDate);
   queryData.put("endDate",endDate);
   queryData.put("zoneId",zoneId);
   queryData.put("prodTypeId",prodTypeId);
   queryData.put("cmplBusiTypeId",cmplBusiTypeId);
   List<Map<String, Object>> cellList=dao.getListCmplReport_MonExp(queryData);
  if(cellList != null && cellList.size()>0 ){
	   for (Map<String, Object> key : cellList) {
	          data.add(key.get("BILL_ID").toString());
	          data.add(key.get("PROD_TYPE_NAME").toString());
	          data.add(key.get("CMPL_BUSINESS_TYPE_NAME").toString());
	          data.add(key.get("CMPL_CHANNEL_NAME").toString());
	          data.add(key.get("CMPL_BILL_TYPE_NAME").toString());
	          data.add(key.get("CUST_NBR").toString());
	          data.add(key.get("ZONE_NAME").toString());
	          data.add(key.get("CUST_NAME").toString());
	          data.add(key.get("CUST_LINK_NBR").toString());
	          data.add(key.get("CUST_LINK_NBR2").toString());
	          data.add(key.get("HANDLE_DATE").toString());
	          data.add(key.get("HANDLE_DEPT_ID").toString());
	          data.add(key.get("HANDLE_DEPT_NAME").toString());
	          data.add(key.get("HANDLE_DEPT_NAME_DESC").toString());
	          data.add(key.get("ARCHIVE_DATE").toString()); 
	          data.add(key.get("FILE_TYPE_NAME").toString()); 
	          data.add(key.get("BILL_TIME_COST").toString()); 
	          data.add(key.get("BILL_TIMEOUT_COST").toString()); 
	          data.add(key.get("TIMEOUT_DURATION").toString()); 
	          data.add(key.get("REPEAT_NUM").toString()); 
	          data.add(key.get("DUTY_DEPT_ID").toString()); 
	          data.add(key.get("DUTY_DEPT_NAME").toString()); 
	          data.add(key.get("DUTY_DEPT_NAME_DESC").toString()); 
	          data.add(key.get("ARCHIVE_DESC").toString()); 
	          data.add(key.get("SATISFY_NAME").toString());        
	          data.add(key.get("CALL_SEQ").toString()); 
	          data.add(key.get("CUST_LEVEL_NAME").toString());      
	        excel.createRow(data,row_num++,col_num,excel.getBodyStyle());
	    	data.clear();
	   }
	 }
    excel.save(realPath);
	excel.returnExcel(response,realPath);
	out.clear();
    out = pageContext.pushBody();
%>
