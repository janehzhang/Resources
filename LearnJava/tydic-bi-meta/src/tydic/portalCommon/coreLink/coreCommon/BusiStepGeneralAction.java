package tydic.portalCommon.coreLink.coreCommon;

import java.util.Map;

import tydic.frame.common.Log;

/**
 * 
 * @author yanhd
 * 
 */
public class BusiStepGeneralAction {
	
	private BusiStepGeneralDAO busiStepGeneralDAO; 
	
	/**
	 * 获取最新显示的月份
	 */
	public String getNewMonth(String indId) {
		try {
			BusiStepGeneralDAO dao = new BusiStepGeneralDAO();
			return dao.getNewMonth(indId);
		} catch (Exception e) {
			Log.error(null, e);
		}
		return "";
	} 
	
	//指标偏离值
	public Map<String, Object> getIndPlValue(Map<String, Object> queryData) {
		return busiStepGeneralDAO.getIndPlValue(queryData);
	}
	//月份指标值
	public Map<String, Object> getMonthIndValue(Map<String, Object> queryData) {
		return busiStepGeneralDAO.getMonthIndValue(queryData);
	}
    //区域指标值
	public Map<String, Object> getAreaIndValue(Map<String, Object> queryData) {
		return busiStepGeneralDAO.getAreaIndValue(queryData);
	}
	public void setBusiStepGeneralDAO(BusiStepGeneralDAO busiStepGeneralDAO) {
		this.busiStepGeneralDAO = busiStepGeneralDAO;
	}



}
