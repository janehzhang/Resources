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
<%@ page import="java.awt.image.BufferedImage"%>
<%@ page import="javax.imageio.ImageIO"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFClientAnchor"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFPatriarch"%>
<%
	String excelName = new String(request.getParameter("excelTitle").getBytes("ISO-8859-1"), "UTF-8");
    String condition = new String(request.getParameter("excelCondition").getBytes("ISO-8859-1"), "UTF-8");
	String excelData = new String(request.getParameter("excelData").getBytes("ISO-8859-1"), "UTF-8");
	String filePath =(String)request.getParameter("filePath");
	int row =Integer.parseInt((String)request.getParameter("row"));
	excelData = excelData.replaceAll("&nbsp;&nbsp;", " ");
	String[] dataValues = excelData.split("]");

	int fieldNum = Integer.parseInt((String)request.getParameter("col"));//表头列数
	String realPath = request.getRealPath("/upload/template/"+ excelName + ".xls");
	String chartPath = request.getRealPath("/savefiles/");
	File xlsFile = new File(realPath);
	String fileToBeRead = request.getRealPath(filePath);
	HSSFWorkbook workBook = new HSSFWorkbook(new FileInputStream(fileToBeRead));  
    List data = new ArrayList();
	int row_num = 0;
	int col_num = 0;
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
	int temp=row-2;
	for(int i=0;i<temp;i++){
		row_num++;
	}
	row_num=row_num+1;
   for (String dataValue : dataValues) {
		String[] values = dataValue.split("}");
		for (String value : values) {
			data.add(value.replaceAll(" ","").replaceAll(",",""));
		}
		excel.createRow(data, row_num++, col_num, excel.getBodyStyle());
		data.clear();
	}
	excel.save(realPath);
	excel.returnExcel(response, realPath);
	out.clear();
	out = pageContext.pushBody();
	
%>