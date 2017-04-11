
/**   
 * @文件名: FormatExcel.java
 * @包 tydic.meta.module.report.repExcel
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-6 下午03:00:23
 *  
 */
  
package tydic.meta.module.report.repExcel;

import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import tydic.frame.common.utils.StringUtils;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：FormatExcel   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-4-6 下午03:00:23   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class FormatExcel {
	/**
	 * @Title: getFormat 
	 * @Description: 设置cell格式
	 * @param fontPx 字体大小，比如18px--18
	 * @param isBlod 字体是否加粗
	 * @param fontColor 字体颜色 （注意：必须是JXL支持的颜色，在传值时不清楚请查看JXL API）
	 * 								         现在只开通白色、黑色(默认)
	 * @param fontAlignment 字体对齐(只支持：左、中、右)
	 * @param bgColor 背景颜色（注意：必须是JXL支持的颜色，在传值时不清楚请查看JXL API）
	 * 								   现在只开通灰色、白色(默认)、蓝色
	 * @return WritableCellFormat   
	 * @throws
	 */
	public WritableCellFormat setFormat(int fontPx,boolean isBlod,String fontColor,String fontAlignment,String bgColor){
		if(fontPx < 1)
			fontPx = 11;
		WritableFont font = null;
		if(isBlod){
			if(StringUtils.isNotEmpty(fontColor)){
				if("WHITE".equals(fontColor.toUpperCase()))
					font = new WritableFont(WritableFont.TIMES,fontPx,WritableFont.BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.WHITE); 
				else
					font = new WritableFont(WritableFont.TIMES,fontPx,WritableFont.BOLD,false); 
			}else
				font = new WritableFont(WritableFont.TIMES,fontPx,WritableFont.BOLD,false); 
		}else{
			if(StringUtils.isNotEmpty(fontColor)){
				if("WHITE".equals(fontColor.toUpperCase()))
					font = new WritableFont(WritableFont.TIMES,fontPx,WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.WHITE); 
				else
					font = new WritableFont(WritableFont.TIMES,fontPx,WritableFont.NO_BOLD,false); 
			}else
				font = new WritableFont(WritableFont.TIMES,fontPx,WritableFont.NO_BOLD,false); 
		}
		
		WritableCellFormat format = new WritableCellFormat(font);
		if(StringUtils.isNotEmpty(fontAlignment)){
			try {
				if("CENTRE".equals(fontAlignment.toUpperCase()))
					format.setAlignment(Alignment.CENTRE);
				else if("LEFT".equals(fontAlignment.toUpperCase()))
					format.setAlignment(Alignment.LEFT);
				else
					format.setAlignment(Alignment.RIGHT);
			}catch(WriteException e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isNotEmpty(bgColor)){
			try{
				if(bgColor.toUpperCase().indexOf("BLUE") != -1)
					format.setBackground(Colour.BLUE2);
				else if(bgColor.toUpperCase().indexOf("GRAY") != -1)
					format.setBackground(Colour.GRAY_50);
			}catch(WriteException e) {
				e.printStackTrace();
			}
		}
		return format;
	}
	/**
	 * @Title: setColumnView 
	 * @Description: 设置cell列宽度
	 * @param cellWidth
	 * @param sheet
	 * @param cellCol    
	 * @return void   
	 * @throws
	 */
	public void setColumnView(int cellWidth,WritableSheet sheet,int cellCol){
		sheet.setColumnView(cellCol,cellWidth);
	}
	/**
	 * @Title: countChinieseNum 
	 * @Description: 计算字符串的中文个数
	 * @param s
	 * @return int   
	 * @throws
	 */
	public int countChinieseNum(String s){
		int count = 0;
		for(int i = 0; i < s.length(); i++){
			String ss = s.substring(i,i+1);
			if(ss.matches("[\\u4E00-\\u9FA5]+")){
				count++;
			}
		}
		return count;
	}
	/**
	 * @Title: getCellWidth 
	 * @Description: 根据字符串获取在cell中的宽度
	 * @param s
	 * @return int   
	 * @throws
	 */
	public int getCellWidth(String s){
		if(StringUtils.isEmpty(s))
			return 0;
		int chinesesNum = countChinieseNum(s);
		int widthNum = chinesesNum*4 + (s.length()-chinesesNum)*2;
		return widthNum;
	}
	/**
	 * 判断纯数字
	 * @Title: isNumeric 
	 * @Description: 
	 * @param str
	 * @return boolean   
	 * @throws
	 */
	public boolean isNumeric(String str){   
		boolean flag = true;   
		if(str.length() == 0){   
			flag = false;   
		}else{   
			for (int i = str.length();--i>=0;){   
				if (!Character.isDigit(str.charAt(i))){    
					flag = false;    
				}    
			}   
		}   
		return flag;    
	}  
}
