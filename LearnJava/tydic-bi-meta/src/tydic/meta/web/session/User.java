/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd.
 * All rights reserved.
 * @see 作用(Ctrlr:具体功能作用,DAO:针对表,PO：针对的表)
 *
 * @author 谭红涛
 * @date 2012-2-1
 */
package tydic.meta.web.session;

import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.module.mag.login.LoginConstant;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd.
 * All rights reserved.
 *
 * 作用(Ctrlr:具体功能作用,DAO:针对表,PO：针对的表)
 *
 * @author 谭红涛
 * @date 2012-2-1
 */
public class User {
	
	private String sessionID;
	private long logInTime;
	private long logOutTime;
	private int logId;
	private Map<String, Object> lastVisitedMenu;
	private Map<String, Object> userMap;
	
	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public int getUserID() {
		return MapUtils.getIntValue(userMap, "userId");
	}
	
	public int getGroupID() {
		Map<String, Object> sessionMap = SessionContext.getMap(getSessionID());
		return MapUtils.getIntValue(MapUtils.getMap(sessionMap, LoginConstant.SESSION_META_SYSTEM_INFO), "groupId");
	}
	
	public int getDefaultGroupID(){
		return MapUtils.getIntValue(userMap, "groupId");
	}
	
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	public long getLogInTime() {
		return logInTime;
	}
	public void setLogInTime(long logInTime) {
		this.logInTime = logInTime;
	}
	
	public long getLogOutTime() {
		return logOutTime;
	}
	public void setLogOutTime(long logOutTime) {
		this.logOutTime = logOutTime;
	}
	public Map<String, Object> getUserMap() {
		return userMap;
	}
	public void setUserMap(Map<String, Object> userMap) {
		this.userMap = userMap;
	}

	public Map<String, Object> getLastVisitedMenu() {
		return lastVisitedMenu;
	}

	public void setLastVisitedMenu(Map<String, Object> lastVisitedMenu) {
		this.lastVisitedMenu = lastVisitedMenu;
	}
}