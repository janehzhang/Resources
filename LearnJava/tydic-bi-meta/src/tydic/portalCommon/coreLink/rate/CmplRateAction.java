package tydic.portalCommon.coreLink.rate;

import java.util.Map;

/**
 * 
 * @author yanhd
 * 
 */
public class CmplRateAction {

	private CmplRateDAO cmplRateDAO;
    
	//投诉率对比
	public Map<String, Object> getCompareRate(Map<String, Object> queryData) {
		return cmplRateDAO.getCompareRate(queryData);
	}
	
	//投诉率偏离值
	public Map<String, Object> getPianLiRate(Map<String, Object> queryData) {
		return cmplRateDAO.getPianLiRate(queryData);
	}
	
	//月份投诉率
	public Map<String, Object> getMonthRate(Map<String, Object> queryData) {
		return cmplRateDAO.getMonthRate(queryData);
	}
	
	//区域投诉率
	public Map<String, Object> getAreaRate(Map<String, Object> queryData) {
		return cmplRateDAO.getAreaRate(queryData);
	}

	public void setCmplRateDAO(CmplRateDAO cmplRateDAO) {
		this.cmplRateDAO = cmplRateDAO;
	}

}
