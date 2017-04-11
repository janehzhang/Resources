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
	String excelName = new String(request.getParameter("excelTitle2").getBytes("ISO-8859-1"), "UTF-8");
	String excelHeader = new String(request.getParameter("excelHeader2").getBytes("ISO-8859-1"), "UTF-8");
	String excelData = new String(request.getParameter("excelData2").getBytes("ISO-8859-1"), "UTF-8");
	excelData = excelData.replaceAll("&nbsp;&nbsp;", " ");
	String[] fieldsName = excelHeader.split(",");
	String[] dataValues = excelData.split("]");

	int fieldNum = fieldsName.length;
	String realPath = request.getRealPath("/upload/template/"+ excelName + ".xls");
	File xlsFile = new File(realPath);

	HSSFWorkbook workBook = new HSSFWorkbook();
	List data = new ArrayList();
	short row_num = 0;
	short col_num = 1;
	HSSFSheet sheet = workBook.createSheet(excelName);
	ExcelUtil excel = new ExcelUtil(workBook, sheet);
	HSSFRow rows = null;
	HSSFCell cells = null;
	rows = sheet.createRow(row_num++);
	cells = rows.createCell(col_num);

	//给excel加标题
	excel.createRegion(rows, cells, row_num - 1, fieldNum)
			.setCellStyle(excel.getBodyTitleStyle());
	cells.setCellValue(excelName);

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
	excel.save(realPath);
	excel.returnExcel(response, realPath);
	out.clear();
	out = pageContext.pushBody();
%>