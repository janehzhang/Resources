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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.MapUtils;
import org.directwebremoting.WebContextFactory;

import tydic.meta.module.mag.login.LoginLogDAO;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd.
 * All rights reserved.
 *
 * 作用(Ctrlr:具体功能作用,DAO:针对表,PO：针对的表)
 *
 * @author 谭红涛
 * @date 2012-2-1
 */
public class LoginEventHandler extends AbstractSessionEvtHandler {
	
	private LoginLogDAO loginLogDAO = new LoginLogDAO();
	/**
	 * @param key
	 */
	public LoginEventHandler(String key) {
		super(key);
	}
	
	public void attributeAdded(HttpSession session) {
		logIn(session,(Map<String, Object>)SessionContext.getValue(session.getId(), super.getKey()));
	}

	public void attributeRemoved(HttpSession session) {
		String sid = session.getId();
		if(SessionContext.containsSession(sid)){
			Map<String,Object> sessionMap = SessionContext.getMap(sid);
			if(sessionMap.containsKey(getKey())){
				Map<String,Object> userMap = (Map<String, Object>) sessionMap.get(super.getKey());
				int userId = MapUtils.getIntValue(userMap, "userId");
				if(SessionManager.isLogIn(userId)){
					User user = SessionManager.getUser(userId);
					try{
						loginLogDAO.updateLoginOutTime(user.getLogId());
					}catch(Exception e){
						
					}finally{
						loginLogDAO.close();
					}
					SessionManager.logOut(userId);
				}
			}
		}
	}

	public void attributeReplaced(Object preValue, Object curValue,HttpSession session) {
		attributeRemoved(session);
		logIn(session,(Map<String, Object>)curValue);
	}
	
	public void sessionDestroyed(HttpSession session) {
		attributeRemoved(session);
	}
	
	private void logIn(HttpSession session,Map<String, Object> userMap){
		User user = new User();
		user.setLogInTime(System.currentTimeMillis());
		String sid = session.getId();
		user.setSessionID(sid);
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		userMap.put("loginIp", request.getRemoteAddr());
		userMap.put("loginMac",request.getRemoteHost());
		user.setUserMap(userMap);
		if(SessionManager.isLoggedIn(user.getUserID())){
			String tempSessionId = SessionManager.getUser(user.getUserID()).getSessionID();
			HttpSession tempSession = SessionContext.getSession(tempSessionId);
			if(tempSession!=null){
				tempSession.removeAttribute(getKey());
			}
		}
		if(SessionManager.isLogIn(sid)){
			session.removeAttribute(getKey());
		}
		user.setLogInTime(System.currentTimeMillis());
		SessionManager.logIn(user);
		try{
			user.setUserMap(userMap);
			int logId = loginLogDAO.insertLoginLog(user);
			user.setLogId(logId);
		}catch(Exception e){
			
		}finally{
			loginLogDAO.close();
		}
	}
}
