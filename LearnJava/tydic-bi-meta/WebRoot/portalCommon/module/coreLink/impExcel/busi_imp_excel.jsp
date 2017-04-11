<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCellStyle"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFFont"%>
<%@ page import="org.apache.poi.hssf.util.*"%>
<%@ page import="tydic.portalCommon.coreLink.util.*"%>
<%@ page import="tydic.portalCommon.coreLink.coreCommon.BusiStepGeneralDAO"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%
	String title = request.getParameter("title");
	       title= new String(title.trim().getBytes("ISO-8859-1"), "UTF-8");  
	String colNames = request.getParameter("colNames");
            colNames= new String(colNames.trim().getBytes("ISO-8859-1"), "UTF-8");  
    String dateTime = request.getParameter("dateTime");
    String  zoneId  = request.getParameter("zoneId");
    String  indId  = request.getParameter("indId");
    
    String  zone  = request.getParameter("zone");
    zone= new String(zone.trim().getBytes("ISO-8859-1"), "UTF-8");  
  String condition="月份:"+dateTime+"    区域:"+zone;

    String[] CfieldsName =colNames.split(",");
    int fieldNum=CfieldsName.length;

	short row_num = 0;
	short col_num = 1;
	
    String realPath = request.getRealPath("/upload/template/"+title+".xls");
    File xlsFile = new File(realPath);
    List data = new ArrayList();
	HSSFWorkbook workBook = new HSSFWorkbook();
    HSSFSheet sheet = workBook.createSheet(title+"月报表");
    ExcelUtil excel = new ExcelUtil(workBook,sheet);
	 
	HSSFRow rows = null;
	HSSFCell cells = null;
	rows = sheet.createRow(row_num++);
	cells = rows.createCell(col_num);
	
	//给excel加标题
    excel.createRegion(rows,cells,row_num-1,fieldNum).setCellStyle(excel.getBodyTitleStyle());
	cells.setCellValue(title);
	
	rows = sheet.createRow(row_num++);
	cells = rows.createCell(col_num);
	//给excel加条件
	if(null!=condition && ""!=condition){
	excel.createRegion(rows,cells,row_num-1,fieldNum).setCellStyle(excel.getBodyTitleStyle());
	cells.setCellValue("条件："+condition);
	}	
	
	//给excel的列头加中文字段名
	for(String fieldName:CfieldsName){
		data.add(fieldName);
	}
    excel.createRow(data,row_num++,col_num,excel.getHeadStyle());
    data.clear();
   
   BusiStepGeneralDAO dao=new BusiStepGeneralDAO();
   Map<String, Object> queryData=new HashMap<String,Object>();
   queryData.put("dateTime",dateTime);
   queryData.put("zoneId",zoneId);
   queryData.put("indId",indId);
   List<Map<String, Object>> cellList=dao.getBusiMapData(queryData);
      Double total_WEIGHT=0d;
      Double total_SCORE =0d;
  if(cellList != null && cellList.size()>0 ){
	   for (Map<String, Object> key : cellList) {
	          data.add(key.get("BUSI_STEP2_NAME").toString());
	          data.add(key.get("BUSI_STEP3_NAME").toString());
	          data.add(key.get("BUSI_STEP_WEIGHT").toString());
	          data.add(key.get("BUSI_STEP_DIRECTION").toString());
	          data.add(key.get("ALARM_VALUE").toString());
	          data.add(key.get("CURRENT_VALUE").toString());
	          data.add(key.get("CMPL_PROPORTION").toString());
	          data.add(key.get("OFFSET").toString());
	          data.add(key.get("SCORE").toString());
	          
	        total_WEIGHT += Double.parseDouble(key.get("BUSI_STEP_WEIGHT").toString().replaceAll("％","").replaceAll("‰",""));
	        total_SCORE  += Double.parseDouble(key.get("SCORE").toString().replaceAll("％","").replaceAll("‰",""));
	        excel.createRow(data,row_num++,col_num,excel.getBodyStyle());
	    	data.clear();
	   }
	      total_WEIGHT=Double.parseDouble(new java.text.DecimalFormat("#.00").format(total_WEIGHT));
	      total_SCORE= Double.parseDouble(new java.text.DecimalFormat("#.00").format(total_SCORE));
          data.add("");
          data.add("合计");
          data.add(String.valueOf(total_WEIGHT)+"%");
          data.add("");
          data.add("");
          data.add("");
          data.add("");
          data.add("");
          data.add(String.valueOf(total_SCORE)+"%");
         excel.createRow(data,row_num++,col_num,excel.getBodyStyle());
    	 data.clear();
	 }
  
    excel.save(realPath);
	excel.returnExcel(response,realPath);
	out.clear();
    out = pageContext.pushBody();

%>
