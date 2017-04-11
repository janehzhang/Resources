<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCellStyle"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFFont"%>
<%@ page import="org.apache.poi.hssf.util.*"%>
<%@ page import="tydic.portalCommon.coreLink.util.*"%>
<%@ page import="tydic.reports.complain.monitorDay.CmplIndexDAO"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%
	String title = request.getParameter("title");
	title= new String(title.trim().getBytes("ISO-8859-1"), "UTF-8");  
	String colNames = request.getParameter("colNames");
    colNames= new String(colNames.trim().getBytes("ISO-8859-1"), "UTF-8");  
    String dateTime = request.getParameter("dateTime");
    String  zoneCode  = request.getParameter("zoneCode");
    String  prodType  = request.getParameter("prodTypeCode");
    String  ind  = request.getParameter("ind");
    String[] CfieldsName =colNames.split(",");
    int fieldNum=CfieldsName.length;

	short row_num = 0;
	short col_num = 1;
	
    String realPath = request.getRealPath("/upload/template/"+title+".xls");
    File xlsFile = new File(realPath);
    List data = new ArrayList();
	HSSFWorkbook workBook = new HSSFWorkbook();
    HSSFSheet sheet = workBook.createSheet("投诉类指标月监测报表");
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
   
   CmplIndexDAO dao=new CmplIndexDAO();
   Map<String, Object> queryData=new HashMap<String,Object>();
   queryData.put("dateTime",dateTime);
   queryData.put("zoneCode",zoneCode);
   queryData.put("prodType",prodType);
   queryData.put("ind",ind);
   List<Map<String, Object>> cellList=dao.getCmplIndex_Mon(queryData);
  if(cellList != null && cellList.size()>0 ){          
	   for (Map<String, Object> key : cellList) {
	          data.add(key.get("PAR_REGION_NAME").toString());
	          data.add(key.get("REGION_NAME").toString());
	          data.add(key.get("IND_NAME").toString());
	          data.add(key.get("CURRENT_VALUE").toString());
	          data.add(key.get("LASTMON_VALUE").toString());
	          data.add(key.get("HB").toString());
	          data.add(key.get("TB").toString());
	          data.add(key.get("AVG_VALUE").toString());
	          
	        excel.createRow(data,row_num++,col_num,excel.getBodyStyle());
	    	data.clear();
	   }
	 }
    excel.save(realPath);
	excel.returnExcel(response,realPath);
	out.clear();
    out = pageContext.pushBody();
%>
