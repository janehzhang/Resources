package tydic.meta.module.dim;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hslf.model.Sheet;
import org.apache.poi.hssf.model.Workbook;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sun.rowset.internal.Row;

/**
 * 工具类,解析excel文件(包括03以及07)
 * */ 
public class ExcelUtil {
	/**
	 * 读取execl文件,生成对应的文件
	 * @param fileName 上传之后的文件名字
	 * @return map key为itemCode value为该行的数据
	 * */
	int totalCount = 0; 
    public List<Map<String,Object>> readFile(InputStream inputStream) throws Exception{
    	 Workbook book = null;
     /*    try {
             book = new XSSFWorkbook(inputStream);
         } catch (Exception ex) {
             book = new HSSFWorkbook(inputStream);
         }*/
         return read(book);
    }

    public List<Map<String, Object>> read(Workbook wb) {
        List<Map<String, Object>> dataList = null;
        Map<String, Object> dataMap = null;
        Map<Integer, String> keyMap = null;
        /*Sheet sheet = wb.getSheetAt(0);
        totalCount = sheet.getPhysicalNumberOfRows();  //得到当前的文件的总条数,如果有数据则初始化map装数据
        if (totalCount != 0 && sheet != null) {
            dataList = new ArrayList<Map<String, Object>>();
            int cellCount = 0;
            String cellValue = null;
            Row titleRow = sheet.getRow(0);
            if (titleRow != null) {
                keyMap = new HashMap<Integer, String>();
                cellCount = titleRow.getPhysicalNumberOfCells();  //处理第一行的title
                for (int tc = 0; tc < cellCount; tc++) {
                    Cell titleCell = titleRow.getCell(tc);
                    if (Cell.CELL_TYPE_STRING == titleCell.getCellType()) {
                        cellValue = titleCell.getStringCellValue();
                        keyMap.put(tc, cellValue);
                    }
                }
            }
            for (int r = 1; r < totalCount; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    System.out.println(r + "行为空");
                    continue;
                } else {
                    dataMap = new HashMap<String, Object>();
                    for (int c = 0; c < row.getPhysicalNumberOfCells(); c++) {
                        Cell cell = row.getCell(c);
                        if (cell != null) {
                            if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
                                Long cellLong = Double.valueOf(row.getCell(c).getNumericCellValue()).longValue();
                                dataMap.put(keyMap.get(c), cellLong.toString());
                            }
                            if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
                                dataMap.put(keyMap.get(c), row.getCell(c).getStringCellValue());
                            }
                        }
                    }
                    dataList.add(dataMap);
                }
            }
        }*/
        return dataList;
    }
    /**
     * 得到数据生成一个excel文件
     */
    public File getExcelFile(String path, List<Map<String, Object>> cellList) {
        Map<String,Integer> mappingIndex=new HashMap<String, Integer>();
        //各键值应该放置Excel表的位置
        mappingIndex.put("itemCode",0);
        mappingIndex.put("itemName",1);
        mappingIndex.put("parCode",2);
        mappingIndex.put("itemDesc",3);
        mappingIndex.put("srcCode",2);
        mappingIndex.put("srcName",3);

        File file = new File(path);
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        //设置默认宽度，占25个字符
        sheet.setDefaultColumnWidth(25);
        sheet.setDefaultRowHeight((short) 30);
        //设置表头样式
        HSSFCellStyle setBorder = wb.createCellStyle();
        setBorder.setFillForegroundColor(IndexedColors.AQUA.getIndex());// 设置背景色
        setBorder.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        setBorder.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        HSSFFont font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 13);//设置字体大小
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示
        setBorder.setFont(font);
        //绘制表头
        if (cellList != null && cellList.size() != 0) {
            HSSFRow row0 = sheet.createRow((short) 0);//隐藏表头
            HSSFRow row1 = sheet.createRow((short) 1);//表头
            HSSFCell cell = null;
            Map<String, Object> title = cellList.get(0);
            int count = title.size() - 1;
            for (Map.Entry<String, Object> entry : title.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                int cellIndex=mappingIndex.containsKey(key)?mappingIndex.get(key):count--;
                cell = row0.createCell(cellIndex);
                cell.setCellValue(key);
                cell = row1.createCell(cellIndex);
                cell.setCellValue(value);
                cell.setCellStyle(setBorder);
            }
            //第0列隐藏
            row0.setHeight((short) 1);
            //绘制表数据
            if (cellList.size() > 1) {
                for (int i = 1; i < cellList.size(); i++) {
                    Map<String, Object> content = cellList.get(i);
                    HSSFRow row = sheet.createRow((short) i+1);
                    count = content.size() - 1;
                    for (Map.Entry<String, Object> entry : content.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue().toString();
                        int cellIndex=mappingIndex.containsKey(key)?mappingIndex.get(key):count--;
                        cell = row.createCell(cellIndex);
                        cell.setCellValue(value);
                    }
                }
            }
        }
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
