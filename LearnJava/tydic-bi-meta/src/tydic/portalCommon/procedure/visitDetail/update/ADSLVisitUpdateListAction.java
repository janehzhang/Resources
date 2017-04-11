package tydic.portalCommon.procedure.visitDetail.update;

import java.util.HashMap;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.yhd.utils.Pager;

/**
 * 类描述：
 * 
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-5-21 下午04:13:02
 */
public class ADSLVisitUpdateListAction {

	private ADSLVisitUpdateListDAO visitListDAO;

	//预警监控清单
	public Map<String, Object> getMonitorData(Map<String, Object> paramMap){
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getMonitorSumData(paramMap,page);
	}
	
	
	public Map<String, Object> getTableData(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getTableData(paramMap, page);
	}
	
	/**
	 * add Chenwei Guang
	 * date 2015-01-28 上午 11:34
	 */	
	public Map<String, Object> getTableDataIVRXZ(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getTableDataIVRXZ(paramMap, page);
	}
	
    public boolean  updateVisitResultIVRXZ(Map<String, Object> param)
    {
    	return visitListDAO.updateVisitResultIVRXZ(param);
    }
	
	
	
	/**
	 * 导出数据
	 */
	public Map<String, Object> expTxtData(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		ADSLVisitUpdateListDAO visitListDAO= new ADSLVisitUpdateListDAO();
		return visitListDAO.getTableData(paramMap, page);// 记录
	}
	
	public Map<String, Object> expTxtDataIVRXZ(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		ADSLVisitUpdateListDAO visitListDAO= new ADSLVisitUpdateListDAO();
		return visitListDAO.getTableDataIVRXZ(paramMap, page);// 记录
	}
	
	public void setVisitListDAO(ADSLVisitUpdateListDAO visitListDAO) {
		this.visitListDAO = visitListDAO;
	}
	
	
	
	
	/**
	 *     add yanhd  
	 */
	
	/**
	 * 营业厅回访清单过程
	 */
	public Map<String, Object> getHfTableData(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getHfTableData(paramMap,page);
	}
	
	/**
	 * 营业厅回访清单过程
	 */
	public Map<String, Object> getVipTableData(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getVipTableData(paramMap,page);
	}
	
	/**
	 * 营业员一级统计汇总
	 */
	public Map<String, Object> getStaffSumData(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getStaffSumData(paramMap,page);
	}
	
	/**
	 * 宽带装维-装维人员测评清单
	 */
	public Map<String, Object> getInstallerSumData(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getInstallerSumData(paramMap,page);
	}
	
	/**
	 * 满意度预警监控-异常数据报表
	 */
	public Map<String, Object> getEwamSumFaultData(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getEwamSumFaultData(paramMap,page);
	}
	
	/**
	 * 满意度预警监控-异常数据清单
	 */
	public Map<String, Object> getEwamFaultDataList(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getEwamFaultDataList(paramMap,page);
	}
	
	
	/**
	 * 导出数据
	 */
	public Map<String, Object> expHfTxtData(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		ADSLVisitUpdateListDAO visitListDAO= new ADSLVisitUpdateListDAO();
		return visitListDAO.getHfTableData(paramMap, page);// 记录
	}
	
	/**
	 * 投诉回访清单过程
	 */
	public Map<String, Object> getTsTableData(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getTsTableData(paramMap,page);
	}
	
	/**
	 * 导出数据
	 */
	public Map<String, Object> expTsTxtData(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		ADSLVisitUpdateListDAO visitListDAO= new ADSLVisitUpdateListDAO();
		return visitListDAO.getTsTableData(paramMap, page);// 记录
	}
	
}
