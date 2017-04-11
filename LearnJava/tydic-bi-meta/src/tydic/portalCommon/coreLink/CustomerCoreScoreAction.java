package tydic.portalCommon.coreLink;

import java.util.Map;

import tydic.frame.common.Log;
import tydic.portalCommon.MetaPortalIndexDataDAO;

/**
 * 
 * @author yanhd
 * 
 */
public class CustomerCoreScoreAction {
	
	private CustomerCoreScoreDAO customerCoreScoreDAO;
    
	
	/**
	 * 获取最新显示的月份
	 */
	public String getNewMonth() {
		try {
			CustomerCoreScoreDAO dao = new CustomerCoreScoreDAO();
			return dao.getNewMonth();
		} catch (Exception e) {
			Log.error(null, e);
		}
		return "";
	} 	
	public Map<String, Object> getChartData(Map<String, Object> queryData) {
		return customerCoreScoreDAO.getChartData(queryData);
	}

	public void setCustomerCoreScoreDAO(CustomerCoreScoreDAO customerCoreScoreDAO) {
		this.customerCoreScoreDAO = customerCoreScoreDAO;
	}	

}
