<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCellStyle"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFFont"%>
<%@ page import="org.apache.poi.hssf.util.*"%>
<%@ page import="tydic.portalCommon.coreLink.util.*"%>
<%@ page import="tydic.portalCommon.MetaPortalIndexDataDAO"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.DecimalFormat"%>
<%
	
    String[] CfieldsName =null;
    String  tabName="";
	String condition="";
	String  tabId=request.getParameter("tabId");
	String  dataDate=request.getParameter("dataDate");
	String  zoneCode=request.getParameter("zoneCode");
	String  indexId=request.getParameter("indexId");
	String  indexName=request.getParameter("indexName");
    indexName = new String(indexName.trim().getBytes("ISO-8859-1"), "UTF-8");  
   
	if(tabId.equals("1")){
		tabName="日重点指标";
		CfieldsName=  new String[]{"地域","当日值","本月累计","上月同期累计","环比","上年同期累计","同比","本月累计平均值 "};
	}else{
		tabName="月重点指标";
		CfieldsName=  new String[]{"地域","当月值","上月值","环比","上年同期累计","同比","本年平均值 "};
	}
   
    int fieldNum=CfieldsName.length;
    String realPath = request.getRealPath("/upload/template/"+tabName+".xls");
    File xlsFile = new File(realPath);
	HSSFWorkbook workBook = new HSSFWorkbook();
	ExcelUtil excel=null;

	String title =tabName;
    List data = new ArrayList();
	short row_num = 0;
	short col_num = 1;
    HSSFSheet sheet = workBook.createSheet(indexName+"报表");
     excel= new ExcelUtil(workBook,sheet);
	 
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
   
    Map<String, Object> queryData=new HashMap<String,Object>();
    queryData.put("tabId",tabId);
    queryData.put("dataDate",dataDate);
    queryData.put("zoneCode",zoneCode);
    queryData.put("indexCd",indexId);
    MetaPortalIndexDataDAO dao=new MetaPortalIndexDataDAO();
    List<Map<String, Object>> cellList=dao.getDrillTableData(queryData);
    
    if(cellList != null && cellList.size()>0 ){
     	 for (Map<String, Object> key : cellList) {
     		 data.add(key.get("AREA_NAME").toString());
     		 if(tabId.equals("1")){
     			 data.add(key.get("VALUE2").toString());
     			 data.add(key.get("VALUE3").toString());
     			 data.add(key.get("VALUE4").toString());
     			 data.add(key.get("VALUE5").toString());
     			 data.add(key.get("VALUE6").toString());
     			 data.add(key.get("VALUE7").toString());
     			 data.add(key.get("VALUE8").toString());
     		 }else{
     			 data.add(key.get("VALUE2").toString());
     			 data.add(key.get("VALUE3").toString());
     			 data.add(key.get("VALUE4").toString());
     			 data.add(key.get("VALUE6").toString());
     			 data.add(key.get("VALUE7").toString());
     			 data.add(key.get("VALUE8").toString());
     		 }
  	        excel.createRow(data,row_num++,col_num,excel.getBodyStyle());
  	        data.clear();
  	   }
   }
 
    excel.save(realPath);
	excel.returnExcel(response,realPath);
	out.clear();
    out = pageContext.pushBody();
%>
