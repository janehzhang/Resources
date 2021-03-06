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
<%
	String excelName = new String(request.getParameter("excelTitle").getBytes("ISO-8859-1"), "UTF-8");
    String condition = new String(request.getParameter("excelCondition").getBytes("ISO-8859-1"), "UTF-8");
	String excelHeader = new String(request.getParameter("excelHeader").getBytes("ISO-8859-1"), "UTF-8");
	String excelData = new String(request.getParameter("excelData").getBytes("ISO-8859-1"), "UTF-8");
	String explain="";
	if(request.getParameter("explain")!=null&&request.getParameter("explain")!=""){
	   explain = new String(request.getParameter("explain").getBytes("ISO-8859-1"), "UTF-8");
	}
	String filePath =(String)request.getParameter("filePath");
	int row =Integer.parseInt((String)request.getParameter("row"));
	excelData = excelData.replaceAll("&nbsp;&nbsp;", " ");
	String[] fieldsName = excelHeader.split(",");
	String[] dataValues = excelData.split("]");
	String[] explainValues = explain.split("]");
	explain = explain.replaceAll("&nbsp;&nbsp;", " ");

	int fieldNum = fieldsName.length;
	String realPath = request.getRealPath("/upload/template/"+ excelName + ".xls");
	File xlsFile = new File(realPath);
	String fileToBeRead = request.getRealPath(filePath);
	HSSFWorkbook workBook = new HSSFWorkbook(new FileInputStream(fileToBeRead));  
    List data = new ArrayList();
	short row_num = 0;
	short col_num = 0;
	HSSFSheet sheet = workBook.getSheet(workBook.getSheetName(0));//第1 个Sheet 对象
    sheet.createFreezePane(0, 4);// 冻结    列和行
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
	int temp=row-3;
	for(int i=0;i<temp;i++){
		row_num++;
	}
   for (String dataValue : dataValues) {
		String[] values = dataValue.split("}");
		for (String value : values) {
			data.add(value);
		}
		excel.createRow(data, row_num++, col_num, excel.getBodyStyle());
		data.clear();
	}
	//给excel加解释
 	if(null!=explain && ""!=explain){
 		rows = sheet.createRow(row_num++);
		cells = rows.createCell(col_num);
		excel.createRegion(rows, cells, row_num - 1, fieldNum-1);
        cells.setCellValue("口径解释：");
 		for (String explainValue : explainValues) {
 			rows = sheet.createRow(row_num++);
 			cells = rows.createCell(col_num);
 			excel.createRegion(rows, cells, row_num - 1, fieldNum-1);
	        cells.setCellValue(explainValue);
 		}	
	}
	excel.save(realPath);
	excel.returnExcel(response, realPath);
	out.clear();
	out = pageContext.pushBody();
	
%>