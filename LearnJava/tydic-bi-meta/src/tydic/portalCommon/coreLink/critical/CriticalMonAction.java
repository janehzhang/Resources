package tydic.portalCommon.coreLink.critical;

import java.util.List;
import java.util.Map;

import tydic.frame.common.Log;

/**
 * 
 * @author yanhd
 * 
 */
public class CriticalMonAction {

	private CriticalMonDAO criticalMonDAO;

	/**
	 * 获取最新显示的月份
	 */
	public String getNewMonth() {
		try {
			CriticalMonDAO criticalMonDAO = new CriticalMonDAO();
			return criticalMonDAO.getNewMonth();
		} catch (Exception e) {
			Log.error(null, e);
		}
		return "";
	}
	
	public List<Map<String, Object>> getTableData(Map<String, Object>  map) {
		return criticalMonDAO.getTableData(map);
	}
	 
	public void setCriticalMonDAO(CriticalMonDAO criticalMonDAO) {
		this.criticalMonDAO = criticalMonDAO;
	}

}
