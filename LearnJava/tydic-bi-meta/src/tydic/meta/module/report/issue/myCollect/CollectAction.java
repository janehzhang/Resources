package tydic.meta.module.report.issue.myCollect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tydic.meta.web.session.SessionManager;

/**
 * Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 * All rights reserved.
 * 我收藏的报表
 * @author guoxf 
 * @date 2012-3-9
 * 
 */
public class CollectAction {
	
	private CollectDAO collectDAO;

	public CollectDAO getCollectDAO() {
		return collectDAO;
	}
	public void setCollectDAO(CollectDAO collectDAO) {
		this.collectDAO = collectDAO;
	}
	/**
	 * 获取所有的收藏类型
	 *
	 */
	// 获取当前用户ID
	int userId = SessionManager.getCurrentUserID();
	// 获得岗位应用数
	int stationId = SessionManager.getCurrentStationID();
	public List<Map<String,Object>> getAllCollectType(){
		List<Map<String,Object>> list = collectDAO.findAllCollectType(userId);
        return list;
    }
	/**
     * 根据父ID获取子字段
     * @param parentId	父ID
     * @return
     */
    public List<Map<String,Object>> getSubCollect(int parentId){
        return collectDAO.findSubCollectType(parentId,userId);
    }
    /**
     * 获取当前用户所收藏的报表
     */
	public List<Map<String, Object>> getAllCollectRpt() {
		List<Map<String, Object>> rpts = collectDAO.findAllCollectRpt(userId, stationId);
		Map<String, Map<String, Object>> rustMap = new HashMap<String, Map<String, Object>>();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : rpts) {
			if (rustMap.containsKey(map.get("REPORT_ID").toString())) {
				rustMap.get(map.get("REPORT_ID").toString()).put("COL_ALIAS",rustMap.get(map.get("REPORT_ID").toString()).get("COL_ALIAS").toString()+ "," + map.get("COL_ALIAS").toString());
			} else {
				rustMap.put(map.get("REPORT_ID").toString(), map);
			}
		}
		Iterator it = rustMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry enttry = (Map.Entry) it.next();
			resultList.add((Map<String, Object>) enttry.getValue());
		}
		return resultList;
	}
    /**
     * 取消当前用户所收藏的报表
     */
    public int cancelCollectRpt(int reportId){
    	int count = collectDAO.cancelCollectRpt(userId,reportId);
    	return count;
    }
    
}
