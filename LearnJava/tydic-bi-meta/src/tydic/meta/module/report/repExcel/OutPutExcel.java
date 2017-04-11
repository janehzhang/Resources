
/**   
 * @文件名: OutPutExcel.java
 * @包 tydic.meta.module.report.repExcel
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-6 下午03:42:09
 *  
 */
  
package tydic.meta.module.report.repExcel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.ClassContextUtil;
import tydic.meta.module.report.ReportConstant;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：OutPutExcel   
 * 类描述：   
 * 创建人：wuxl@tydic.com 
 * 创建时间：2012-4-6 下午03:42:09   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class OutPutExcel {
	private FormatExcel formatExcel = new FormatExcel();
	/**
	 * @Title: save 
	 * @Description: 创建excel文件（注意：只支持表头为1列：字段1，字段2，字段3......不支持列合并与行合并）
	 * @param headList 表头信息
	 * @param time 时间
	 * @param repName 文件名称
	 * @param data 表数据
	 * @return void   
	 * @throws
	 */
	public void save(List<ReportBean> headList,String time,String repName,Object[][] data){//,HttpServletResponse response
		if(headList == null){
			throw new RuntimeException(this.getClass().getName()+"输入参数不对！");
		}
		String saveName = repName + "_" + time;
		String savePath = ReportConstant.MAILFILEPATH;//ClassContextUtil.getInstance().getWebAppRootPath() + "/meta/module/reportManage/reportFiles/";
		savePath += saveName + ".xls";
		WritableWorkbook wwb = null;
		try {
			File file = new File(savePath); 
			wwb = Workbook.createWorkbook(file);
			WritableSheet sheet = wwb.createSheet(saveName, 0);
			int rsp = 0;//行起始位置
			int csp = 0;//列起始位置
			WritableCellFormat cellFormat = null;
			//表头
			for(ReportBean hb : headList){//注意：灰色背景，白色字体，居中，字体大小为11
				if(cellFormat == null)
					cellFormat = formatExcel.setFormat(11, true, "WHITE", "CENTRE", "GRAY");
				addCell(csp, rsp, hb.getColumnName(), sheet, cellFormat);
				formatExcel.setColumnView(formatExcel.getCellWidth(hb.getColumnName()), sheet, csp);
				csp++;
			}
			rsp++;
			cellFormat = null;
			int reportCols = headList.get(0).getReportCols();
			if(data != null && data.length > 0){
				//数据
				for(int row = 0; row < data.length; row++){
					String d = "";
					int col = 0;
					for(ReportBean hb : headList){
						if(col < reportCols){
							if(hb.getDimTableId() > 0){//如果是维度，取值为：（报表列数-1）+1 因为下标是从0开始
								d = ""+data[row][col+reportCols-1+1];
							}else
								d = ""+data[row][col];
							
							if(formatExcel.isNumeric(d))
								cellFormat = formatExcel.setFormat(10, false, "", "RIGHT", "");
							else if(StringUtils.isNotEmpty(d)){
								if(d.indexOf("-") != -1)
									cellFormat = formatExcel.setFormat(10, false, "", "CENTRE", "");
								else
									cellFormat = formatExcel.setFormat(10, false, "", "LEFT", "");
							}else
								cellFormat = formatExcel.setFormat(10, false, "", "LEFT", "");
							
							addCell(col, rsp, d, sheet, cellFormat);
						}
						col++;
					}
					rsp++;
				}
			}
			wwb.write();
		}catch(IOException e) {
			e.printStackTrace();
		}finally{
			try{
				if(wwb != null)
					try{
						wwb.close();
					}catch(WriteException e) {
						e.printStackTrace();
					}
			}catch(IOException ie){
				ie.printStackTrace();
			}
		}
	}
	/**
	 * 增加列
	 * @param col 列号
	 * @param row 行号
	 * @param content 文本内容
	 * @param sheet 
	 * @param cellFormat
	 */
	public void addCell(int col, int row, String content,WritableSheet sheet,WritableCellFormat cellFormat){
		try{
			if(cellFormat == null){
				sheet.addCell(new Label(col,row,content));
			}else{	
				sheet.addCell(new Label(col,row,content,cellFormat));
			}
		}catch(RowsExceededException e){
			e.printStackTrace();
		}catch(WriteException e){
			e.printStackTrace();
		}
	}
	/**
	 * 删除文件
	 * @param path
	 */
	public void deletFile(String path){
		File file = new File(path);
		if(file.exists())
			file.delete();
	}
	public static void main(String[] args){
		OutPutExcel o = new OutPutExcel();
		//封装数据
		List<ReportBean> headList = new ArrayList<ReportBean>();
		ReportBean bean = new ReportBean();
		bean.setColumnName("字段1");
		bean.setReportCols(6);
		bean.setDimTableId(1);
		headList.add(bean);
		bean = new ReportBean();
		bean.setColumnName("字段2");
		bean.setReportCols(6);
		bean.setDimTableId(2);
		headList.add(bean);
		bean = new ReportBean();
		bean.setColumnName("字段3");
		bean.setReportCols(6);
		bean.setDimTableId(-1);
		headList.add(bean);
		bean = new ReportBean();
		bean.setColumnName("字段三砥砺风节撒地方4");
		bean.setReportCols(6);
		bean.setDimTableId(-1);
		headList.add(bean);
		bean = new ReportBean();
		bean.setColumnName("字段5sdfsdfsdfdsfsdfsdf");
		bean.setReportCols(6);
		bean.setDimTableId(-1);
		headList.add(bean);
		bean = new ReportBean();
		bean.setColumnName("字段6");
		bean.setReportCols(6);
		bean.setDimTableId(-1);
		headList.add(bean);
//		bean = new ReportBean();
//		bean.setColumnName("字段111");
//		bean.setReportCols(6);
//		headList.add(bean);
//		bean = new ReportBean();
//		bean.setColumnName("字段222");
//		bean.setReportCols(6);
//		headList.add(bean);
		
		Object[][] data = new Object[3][8];
		data[0] = new Object[]{12345,"sfsdfsdfsdf",8977987,"2012-04-09","osudfosdf","989778","wuxl","good"};
		data[1] = new Object[]{12345,"好哦哈哈哈哈",8977987,"2012-04-09","osudfosdf","989778","wuxl","good"};
		data[2] = new Object[]{12345,"sfsdfsdfsdf",8977987,"2012-04-09","好哦哈哈","989778","wuxl","good"};
		
		String time = "2012-04-09";
		String repName = "报表测试";
		
		o.save(headList, time, repName, data);
	}
}
