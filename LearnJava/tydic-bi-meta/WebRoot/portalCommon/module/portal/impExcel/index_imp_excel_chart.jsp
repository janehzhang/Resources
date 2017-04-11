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
<%@ page import="java.awt.image.BufferedImage"%>
<%@ page import="javax.imageio.ImageIO"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFClientAnchor"%>
<%@ page import="org.apache.poi.hssf.usermodel.HSSFPatriarch"%>
<%
	
    String[] CfieldsName =null;
	String tabId=request.getParameter("tabId");
	String curTypeName = new String(request.getParameter("typeId").getBytes("ISO-8859-1"), "UTF-8");
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
    String chartPath = request.getRealPath("/savefiles/");
    File xlsFile = new File(realPath);
	HSSFWorkbook workBook = new HSSFWorkbook();
	ExcelUtil excel=null;
MetaPortalIndexDataDAO dao=new MetaPortalIndexDataDAO();
Map<String,Object> paramMap=new HashMap<String,Object>();
paramMap.put("tabId",tabId);
paramMap.put("dataDate",dataDate);
paramMap.put("zoneCode",zoneCode);
List<Map<String,Object>>  typeList=dao.getViewTabs(paramMap);
HSSFSheet sheet=null;

for(Map<String,Object> typeKey:typeList){
	if(curTypeName.equals((String)typeKey.get("TYPE_NAME"))){
	
	String title ="";
    List data = new ArrayList();
    String typeName=(String)typeKey.get("TYPE_NAME");
	title +=tabName+"->"+typeName;
	
	int row_num = 0;
	int col_num = 0;
    sheet = workBook.createSheet(typeName+"报表");
    sheet.createFreezePane(0, 15);// 冻结    列和行
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
    excel.createRow(data,14,col_num,excel.getHeadStyle());
    data.clear();
   
 
   Map<String, Object> queryData=new HashMap<String,Object>();
   queryData.put("tabId",tabId);
   queryData.put("dataDate",dataDate);
   queryData.put("zoneCode",zoneCode);
   queryData.put("typeId",typeName);
   List<Map<String, Object>> cellList=dao.getTableData(queryData);
   row_num=row_num+13;
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
 }
 BufferedImage bufferImg = null;   
    BufferedImage bufferImg1 = null;
    // 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray   
           // 读入图片1   
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();   
            bufferImg = ImageIO.read(new File(chartPath+"\\linechart.png"));   
            ImageIO.write(bufferImg, "jpg", byteArrayOut);   
              
           // 读入图片2   
            ByteArrayOutputStream byteArrayOut1 = new ByteArrayOutputStream();   
            bufferImg1 = ImageIO.read(new File(chartPath+"\\barchart.png"));   
            ImageIO.write(bufferImg1, "png", byteArrayOut1);   
	HSSFPatriarch patriarch = sheet.createDrawingPatriarch();   
            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,   
                    (short) 0, 2, (short) 5, 5);   
            anchor.setAnchorType(3);   
            HSSFClientAnchor anchor1 = new HSSFClientAnchor(0, 0, 255, 255,   
                    (short) 5, 2, (short) 10, 10);   
            anchor1.setAnchorType(3);       
  // 插入图片1   
        patriarch.createPicture(anchor, workBook.addPicture(byteArrayOut   
                    .toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG)).resize(1);   
     // 插入图片2   
         patriarch.createPicture(anchor1, workBook.addPicture(byteArrayOut1   
                    .toByteArray(), HSSFWorkbook.PICTURE_TYPE_PNG)).resize(1);
    excel.save(realPath);
	excel.returnExcel(response,realPath);
	out.clear();
    out = pageContext.pushBody();
%>
