package tydic.portalCommon.coreLink.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * excel导出工具类
 * @author 颜海东
 * 2008-3-15
 */
public class ExcelUtil {
	private HSSFWorkbook workBook;
	private HSSFSheet sheet;
	private HSSFFont font;
	
	public ExcelUtil(HSSFWorkbook workBook,HSSFSheet sheet){
		this.workBook = workBook;
		this.sheet = sheet;
		this.font = workBook.createFont();
	}
	
	/**
	 * 将list中的数据创建为指定行和列开始的行
	 * @param listToRow
	 * @param row_num
	 * @param col_num
	 */
	public HSSFSheet createRow(List listToRow,int rownum,int colnum,HSSFCellStyle style){
		HSSFRow row = sheet.createRow(rownum);
		for(int i = 0;i < listToRow.size();i++){
			HSSFCell cell = row.createCell((short)colnum++);
			cell.setCellStyle(style);
			//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			sheet.setColumnWidth((short)(colnum-1), (short)5000);
			cell.setCellValue((String)listToRow.get(i));
		}
		return sheet;
	}
	
	public HSSFSheet createRow(List listToRow,int rownum,int colnum){
		return createRow(listToRow,rownum,colnum,null);
	}
	
	//合并单元格
	public HSSFCell createRegion(HSSFRow row,HSSFCell cell,int to_row,int to_col){
		int from_row = row.getRowNum();
		short from_col = cell.getCellNum();
		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		Region region = new Region(from_row,(short)from_col,to_row,(short)to_col);
		sheet.addMergedRegion(region);
		return cell;
	}
	
	public HSSFCell createRegion(int from_row,int from_col,int to_row,int to_col){
		HSSFRow row = sheet.createRow((short)from_row);
		HSSFCell cell = row.createCell((short)from_col);
		Region region = new Region(from_row,(short)from_col,to_row,(short)to_col);
		sheet.addMergedRegion(region);
		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		return cell;
	}
	
	/**
	 * 根据行号\列号\样式 创建单元格
	 * @param rownum
	 * @param colnum
	 * @param style
	 * @return
	 */
	public HSSFCell createCell(int rownum,int colnum,HSSFCellStyle style){
		HSSFRow row = sheet.getRow(rownum);
		if(row == null)
			row = sheet.createRow((short)rownum);
		HSSFCell cell = row.createCell((short)colnum);
		cell.setCellStyle(style);
		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		return cell;
	}
	
	/**
	 * 将excel保存为指定路径的文件
	 * @param filePath
	 */
	public void save(String filePath){
		try {
			FileOutputStream out = new FileOutputStream(filePath);
			workBook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将excel文件发送到客户端
	 * @param response
	 * @param filePath
	 */
	public void returnExcel(HttpServletResponse response,String filePath){
		File t_file = new java.io.File(filePath);
		OutputStream os = null;
		InputStream in =null;
		try {
			in = new FileInputStream(t_file);
			if (in != null) {
				String fs = t_file.getName();
				int l = (int) t_file.length();
				response.reset();
				response.setContentType("application/x-octetstream;charset=ISO8859-1");	
				response.setHeader("Content-Disposition","attachment;filename=\""+ new String(fs.getBytes(), "ISO8859-1")+ "\"");
				byte[] b = new byte[l];
				int len = 0;
				
				while ((len = in.read(b)) > 0) {
					os = response.getOutputStream();
					os.write(b, 0, len);
					os.flush();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(os != null){
				try{
				    os.close();
				}catch(Exception e1){
				    os = null;
				}
			}
			if(in != null){
				try{
				    in.close();
				}catch(Exception e2 ){
					in = null;
				}
				
			}
		}
	}
	
	//表体样式
	public HSSFCellStyle getBodyStyle(){
		HSSFCellStyle style = workBook.createCellStyle();
		//HSSFFont font = workBook.createFont();
		//font.setFontHeightInPoints((short)12);
		//style.setFont(font);
		style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		
		style.setBorderLeft((short)1);
		style.setBorderTop((short)1);
		style.setBorderBottom((short)1);
		style.setBorderRight((short)1);
		return style;
	}
	
	//表体标题样式
	public HSSFCellStyle getBodyTitleStyle(){
		HSSFCellStyle style = workBook.createCellStyle();
		//HSSFFont font = workBook.createFont();
		font.setFontHeightInPoints((short)12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		style.setVerticalAlignment(HSSFCellStyle.ALIGN_LEFT);
		style.setFillBackgroundColor(HSSFColor.ORANGE.index);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		//style.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		//style.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		//style.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		//style.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		//style.setBottomBorderColor(HSSFColor.BLACK.index);
		//style.setRightBorderColor(HSSFColor.BLACK.index);
		//style.setLeftBorderColor(HSSFColor.BLACK.index);
		//style.setTopBorderColor(HSSFColor.BLACK.index);
		return style;
	}
	
	//标题样式
	public HSSFCellStyle getTitleStyle(){
		HSSFCellStyle style = workBook.createCellStyle();
		//HSSFFont font = workBook.createFont();
		font.setFontHeightInPoints((short)18);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.RED.index);
		style.setFont(font);
		style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return style;
	}
	
	//子标题样式
	public HSSFCellStyle getHeadStyle() {
	  /**
	   HSSFCellStyle style = workBook.createCellStyle();
		//HSSFFont font = workBook.createFont();
		font.setFontHeightInPoints((short)16);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.BLUE.index);
		style.setFont(font);
		style.setVerticalAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return style;
		**/
		HSSFCellStyle style = workBook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());// 设置背景色
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中   
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 上下居中   
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setTopBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft((short)1);
		style.setBorderTop((short)1);
		style.setBorderBottom((short)1);
		style.setBorderRight((short)1);
		
		HSSFFont font = workBook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);// 设置字体大小
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 粗体显示
		style.setFont(font);
		
		return style;
	}
}