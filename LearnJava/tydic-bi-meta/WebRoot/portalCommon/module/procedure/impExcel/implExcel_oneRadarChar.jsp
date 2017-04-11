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
	
	int fieldNum = fieldsName.length+15;
	String realPath = request.getRealPath("/upload/template/"+ excelName + ".xls");
	String chartPath = request.getRealPath("/savefiles/");
	File xlsFile = new File(realPath);
	
			 
            

	HSSFWorkbook workBook = new HSSFWorkbook();
	List data = new ArrayList();
	int row_num = 0;
	int col_num = 0;
	HSSFSheet sheet = workBook.createSheet(excelName);
	//HSSFSheet sheet1 = workBook.createSheet("图像");
	
	
    sheet.createFreezePane(0, 24);// 冻结    列和行
	ExcelUtil excel = new ExcelUtil(workBook, sheet);
	HSSFRow rows = null;
	HSSFCell cells = null;
	rows = sheet.createRow(row_num++);
	cells = rows.createCell(col_num);
	
	BufferedImage bufferImg = null;   
    // 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray   
           // 读入图片1   
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();   
            bufferImg = ImageIO.read(new File(chartPath+"\\onePic.png"));   
            ImageIO.write(bufferImg, "jpg", byteArrayOut);    
	        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();   
            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,   
                    (short) 0, 2, (short) 5, 5);   
            anchor.setAnchorType(3);           
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
	
	excel.createRow(data, 23, col_num, excel.getHeadStyle());
	data.clear();
	
	 row_num=row_num+22;
	//col_num=col_num+9;
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
	 // 插入图片1   
        patriarch.createPicture(anchor, workBook.addPicture(byteArrayOut   
                    .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG)).resize(1);   
	excel.save(realPath);
	excel.returnExcel(response, realPath);
	out.clear();
	out = pageContext.pushBody();
%>