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
    String condition =request.getParameter("excelCondition")==null?"":new String(request.getParameter("excelCondition").getBytes("ISO-8859-1"), "UTF-8");
	String excelHeader = new String(request.getParameter("excelHeader").getBytes("ISO-8859-1"), "UTF-8");
	String excelData = new String(request.getParameter("excelData").getBytes("ISO-8859-1"), "UTF-8");
	excelData = excelData.replaceAll("&nbsp;&nbsp;", " ");
	String explain="";
	if(request.getParameter("explain")!=null&&request.getParameter("explain")!=""){
	   explain = new String(request.getParameter("explain").getBytes("ISO-8859-1"), "UTF-8");
	}
	String[] fieldsName = excelHeader.split(",");
	String[] dataValues = excelData.split("]");
	String[] explainValues = explain.split("]");
	explain = explain.replaceAll("&nbsp;&nbsp;", " ");

	int fieldNum = fieldsName.length;
	String realPath = request.getRealPath("/upload/template/"+ excelName + ".xls");
	File xlsFile = new File(realPath);

	HSSFWorkbook workBook = new HSSFWorkbook();
	List data = new ArrayList();
	short row_num = 0;
	short col_num = 0;
	HSSFSheet sheet = workBook.createSheet(excelName);
    sheet.createFreezePane(0, 3);// 冻结    列和行
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
 
	excel.createRegion(rows,cells,row_num-1, fieldNum-1).setCellStyle(excel.getBodyTitleStyle());
	cells.setCellValue("条件："+condition);
	
	
	//给excel的列头加中文字段名
	for (String fieldName : fieldsName) {
		data.add(fieldName);
	}
	excel.createRow(data, row_num++, col_num, excel.getHeadStyle());
	data.clear();

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
	sheet.addMergedRegion(new Region(3, (short)0, 9, (short)0));//合并单元格
	sheet.addMergedRegion(new Region(10, (short)0, 21, (short)0));
	sheet.addMergedRegion(new Region(22, (short)0, 25, (short)0));
	sheet.addMergedRegion(new Region(26, (short)0, 37, (short)0));
	sheet.addMergedRegion(new Region(38, (short)0, 41, (short)0));
	sheet.addMergedRegion(new Region(13, (short)1, 17, (short)1));
    sheet.addMergedRegion(new Region(18, (short)1, 20, (short)1));
	sheet.addMergedRegion(new Region(13, (short)3, 17, (short)3));
    sheet.addMergedRegion(new Region(18, (short)3, 20, (short)3));
    sheet.setColumnWidth(0,6000); //设置列宽
    sheet.setColumnWidth(1,8000); 
    HSSFCellStyle cellStyle = workBook.createCellStyle();    
    cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直    
    cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
	excel.save(realPath);
	excel.returnExcel(response, realPath);
	out.clear();
	out = pageContext.pushBody();
%>