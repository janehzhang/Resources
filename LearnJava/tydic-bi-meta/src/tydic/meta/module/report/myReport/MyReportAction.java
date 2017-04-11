package tydic.meta.module.report.myReport;

import java.util.List;
import java.util.Map;

import tydic.meta.common.Page;


/**
 * 我创建的报表action
 * @author 李国民
 * Date：2012-03-19
 */
public class MyReportAction {

	private MyReportDAO myReportDAO;
	
	/**
	 * 得到我创建的报表
	 * @return
	 */
	public List<Map<String,Object>> getMyReportList(Page page){
		if(page ==null){
	        page=new Page(0,12);	//按每页12条分页
		}
		return myReportDAO.getMyReportList(page);
	}
	
	/**
	 * 通过报表id删除报表
	 * @param reportId
	 * @return
	 */
	public boolean deleteReportById(int reportId){
		return myReportDAO.deleteReportById(reportId);
	}

	public void setMyReportDAO(MyReportDAO myReportDAO) {
		this.myReportDAO = myReportDAO;
	}
	
}



