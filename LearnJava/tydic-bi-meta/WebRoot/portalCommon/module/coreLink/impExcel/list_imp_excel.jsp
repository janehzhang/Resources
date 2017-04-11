<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCellStyle"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFFont"%>
<%@ page import="org.apache.poi.hssf.util.*"%>
<%@ page import="tydic.portalCommon.coreLink.util.*"%>
<%@ page import="tydic.portalCommon.coreLink.CustomerCoreScoreDAO"%>
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
  
    String[] CfieldsName =colNames.split(",");
    int fieldNum=CfieldsName.length;

	short row_num = 0;
	short col_num = 1;
	
    String realPath = request.getRealPath("/upload/template/"+title+".xls");
    File xlsFile = new File(realPath);
    List data = new ArrayList();
    
	HSSFWorkbook workBook = new HSSFWorkbook();
    HSSFSheet sheet = workBook.createSheet("环节得分月报表");
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
   
   CustomerCoreScoreDAO dao=new CustomerCoreScoreDAO();
   Map<String, Object> queryData=new HashMap<String,Object>();
   queryData.put("dateTime",dateTime);
   queryData.put("zoneId",zoneId);
   List<Map<String, Object>> cellList=dao.getListMapData(queryData);
      Double total_BUSI_STEP_WEIGHT=0d;
      Double total_STEP_SCORE=0d;
      Double total_REAL_SCORE=0d;
  if(cellList != null && cellList.size()>0 ){
	   for (Map<String, Object> key : cellList) {
	          data.add(key.get("BUSI_STEP_NAME").toString());
	          data.add(key.get("BUSI_STEP_WEIGHT").toString());
	          data.add(key.get("STEP_SCORE").toString());
	          data.add(key.get("REAL_SCORE").toString());
	          data.add(key.get("SORT_NUM").toString());
	          
	          total_BUSI_STEP_WEIGHT +=  Double.parseDouble(key.get("BUSI_STEP_WEIGHT").toString());
	          total_STEP_SCORE  += Double.parseDouble(key.get("STEP_SCORE").toString());
	          total_REAL_SCORE  += Double.parseDouble(key.get("REAL_SCORE").toString());
	        excel.createRow(data,row_num++,col_num,excel.getBodyStyle());
	    	data.clear();
	   }
             Double total_BUSI_STEP_WEIGHT1=Double.parseDouble(new java.text.DecimalFormat("#.00").format(total_BUSI_STEP_WEIGHT));
             Double total_STEP_SCORE1=Double.parseDouble(new java.text.DecimalFormat("#.00").format(total_STEP_SCORE));
             Double total_REAL_SCORE1=Double.parseDouble(new java.text.DecimalFormat("#.00").format(total_REAL_SCORE)); 


          data.add("所有环节");
          data.add(String.valueOf(total_BUSI_STEP_WEIGHT1));
          data.add(String.valueOf(total_STEP_SCORE1));
          data.add(String.valueOf(total_REAL_SCORE1));
          data.add("");
         excel.createRow(data,row_num++,col_num,excel.getBodyStyle());
    	 data.clear();
	 }
    excel.save(realPath);
	excel.returnExcel(response,realPath);
	out.clear();
    out = pageContext.pushBody();

%>
