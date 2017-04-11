<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCellStyle"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFFont"%>
<%@ page import="org.apache.poi.hssf.util.*"%>
<%@ page import="tydic.portalCommon.coreLink.util.*"%>
<%@ page import="tydic.reports.channel.allBusiness.AllBusinessDAO"%>
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
    String  zoneCode  = request.getParameter("zoneCode");
    String  channelTypeCode  = request.getParameter("channelTypeCode");
    String[] CfieldsName =colNames.split(",");
    int fieldNum=CfieldsName.length;

	short row_num = 0;
	short col_num = 1;
	
    String realPath = request.getRealPath("/upload/template/"+title+".xls");
    File xlsFile = new File(realPath);
    List data = new ArrayList();
	HSSFWorkbook workBook = new HSSFWorkbook();
    HSSFSheet sheet = workBook.createSheet("全渠道总体情况分析日报表");
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
   
   AllBusinessDAO dao=new AllBusinessDAO();
   Map<String, Object> queryData=new HashMap<String,Object>();
   queryData.put("startDate",startDate);
   queryData.put("endDate",endDate);
   queryData.put("zoneCode",zoneCode);
   queryData.put("channelTypeCode",channelTypeCode);
   List<Map<String, Object>> cellList=dao.getChannelGlobal_Week(queryData);
  if(cellList != null && cellList.size()>0 ){
	   for (Map<String, Object> key : cellList) {
	          data.add(key.get("PAR_REGION_NAME").toString());
	          data.add(key.get("REGION_NAME").toString());
	          data.add(key.get("CHANNEL_TYPE_NAME").toString());
	          data.add(key.get("PAY_NUM").toString());
	          data.add(key.get("QUERY_NUM").toString());
	          data.add(key.get("CONSULT_NUM").toString());
	          data.add(key.get("DEAL_NUM").toString());
	          data.add(key.get("FEEDBACK_NUM").toString());
	          data.add(key.get("FEEDBACK2_NUM").toString());
	          data.add(key.get("CHANGE_NUM").toString());
	          data.add(key.get("COMPLAIN_NUM").toString());
	          data.add(key.get("FAULT_NUM").toString());
	          data.add(key.get("OTHER_NUM").toString());
	          data.add(key.get("SERVICE_NUM").toString());
	          data.add(key.get("PERC_NUM").toString()+"%");
	          data.add(key.get("PERC_NUM2").toString()+"%");
	          
	        excel.createRow(data,row_num++,col_num,excel.getBodyStyle());
	    	data.clear();
	   }
	 }
    excel.save(realPath);
	excel.returnExcel(response,realPath);
	out.clear();
    out = pageContext.pushBody();
%>
