<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFWorkbook"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFSheet"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFRow"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCell"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFCellStyle"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFFont"%>
<%@ page import="org.apache.poi.hssf.util.*"%>
<%@ page import="tydic.portalCommon.coreLink.util.*"%>
<%@ page import="tydic.portalCommon.serviceManage.report.WriteReportDAO"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="tydic.frame.common.Log"%>
<%@ page import="tydic.frame.common.utils.MapUtils"%>
<%@ page import="java.text.DecimalFormat"%>
<%
	String title = "服务上报月报表";
	String colNames = "月份,地区,标题,服务热点,服务亮点,工作建义,整改情况,填报人";
    String  monthId = request.getParameter("monthId");
    String  zoneId  = request.getParameter("zoneId");
    //Log.debug("条件: "+monthId+" : "+zoneId);   
    String[] CfieldsName =colNames.split(",");
    int fieldNum=CfieldsName.length;

	short row_num = 0;
	short col_num = 1;
	
    String realPath = request.getRealPath("/upload/template/"+title+".xls");
    File xlsFile = new File(realPath);
    List data = new ArrayList();
	HSSFWorkbook workBook = new HSSFWorkbook();
    HSSFSheet sheet = workBook.createSheet("服务上报月报表");
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
   WriteReportDAO dao=new WriteReportDAO();
   Map<String, Object> queryData=new HashMap<String,Object>();
   queryData.put("monthId",monthId);
   queryData.put("zoneId",zoneId);
   List<Map<String, Object>> cellList=dao.getListMapData(queryData);
   if(cellList != null && cellList.size()>0 ){
	   for (Map<String, Object> key : cellList) {
	          data.add(MapUtils.getString(key, "MONTHID", null));
	          data.add(MapUtils.getString(key, "ZONE_NAME", null));
	          data.add(MapUtils.getString(key, "TITLE", null));  
	          data.add(MapUtils.getString(key, "GZLD", null));   
	          data.add(MapUtils.getString(key, "GZLID", null));  
	          data.add(MapUtils.getString(key, "GZJY", null));   
	          data.add(MapUtils.getString(key, "ZGQK", null));   
	          data.add(MapUtils.getString(key, "CREATENAME", null));
	          excel.createRow(data,row_num++,col_num,excel.getBodyStyle());
	    	  data.clear();
	   }
    }
    excel.save(realPath);
	excel.returnExcel(response,realPath);
	out.clear();
    out = pageContext.pushBody();

%>
