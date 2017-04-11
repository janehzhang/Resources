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
	String tabId=request.getParameter("tabId");
	String tabName="";
	String dataDate="";
	String condition="";
	if(tabId.equals("1")){
		tabName="日重点指标";
		dataDate = request.getParameter("dayTime");
		condition="日 期:";
		CfieldsName= new String[]{"指标项","当日值","本月累计","上月同期累计","环比","上年同期累计","同比","本月累计平均值 "};
	}else{
		tabName="月重点指标";
		condition="月 份:";
		dataDate= request.getParameter("monthTime");
		CfieldsName= new String[]{"指标项 ","当月值" ,"上月值","环比","上年同期累计","同比","本年平均值"};
	}
    String  zoneCode  =  request.getParameter("zoneCode");
    String  zone  = request.getParameter("zone");
            zone= new String(zone.trim().getBytes("ISO-8859-1"), "UTF-8");  
     condition +=dataDate+"    区域:"+zone;	
    int fieldNum=CfieldsName.length;
    String realPath = request.getRealPath("/upload/template/"+tabName+".xls");
    File xlsFile = new File(realPath);
	HSSFWorkbook workBook = new HSSFWorkbook();
	ExcelUtil excel=null;
MetaPortalIndexDataDAO dao=new MetaPortalIndexDataDAO();
Map<String,Object> paramMap=new HashMap<String,Object>();
paramMap.put("tabId",tabId);
paramMap.put("dataDate",dataDate);
paramMap.put("zoneCode",zoneCode);
List<Map<String,Object>>  typeList=dao.getViewTabs(paramMap);

for(Map<String,Object> typeKey:typeList){
	String title ="";
    List data = new ArrayList();
    String typeName=(String)typeKey.get("TYPE_NAME");
	title +=tabName+"->"+typeName;
	
	short row_num = 0;
	short col_num = 0;
    HSSFSheet sheet = workBook.createSheet(typeName+"报表");
    sheet.createFreezePane(0, 3);// 冻结    列和行
     excel= new ExcelUtil(workBook,sheet);
	 
	HSSFRow rows = null;
	HSSFCell cells = null;
	rows = sheet.createRow(row_num++);
	cells = rows.createCell(col_num);
	
	//给excel加标题
    excel.createRegion(rows,cells,row_num-1,fieldNum-1).setCellStyle(excel.getBodyTitleStyle());
	cells.setCellValue(title);
	
	rows = sheet.createRow(row_num++);
	cells = rows.createCell(col_num);
	//给excel加条件
	if(null!=condition && ""!=condition){
	excel.createRegion(rows,cells,row_num-1,fieldNum-1).setCellStyle(excel.getBodyTitleStyle());
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
   queryData.put("typeId",typeName);
   List<Map<String, Object>> cellList=dao.getTableData(queryData);
   
  if(cellList != null && cellList.size()>0 ){
    	 for (Map<String, Object> key : cellList) {
    		 data.add(key.get("INDEX_NAME").toString());
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
 }
    excel.save(realPath);
	excel.returnExcel(response,realPath);
	out.clear();
    out = pageContext.pushBody();
%>
