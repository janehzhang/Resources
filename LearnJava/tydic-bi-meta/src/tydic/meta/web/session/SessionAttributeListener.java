/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd.
 * All rights reserved.
 * @see 作用(Ctrlr:具体功能作用,DAO:针对表,PO：针对的表)
 *
 * @author 谭红涛
 * @date 2012-1-30
 */
package tydic.meta.web.session;

import java.util.Hashtable;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

public class SessionAttributeListener implements HttpSessionAttributeListener {
	private static Hashtable<String,AbstractSessionEvtHandler> handlerContext = new Hashtable<String, AbstractSessionEvtHandler>();
	
	public static void addHandler(String key,AbstractSessionEvtHandler handler){
		handlerContext.put(key, handler);
	}
	
	public void attributeAdded(HttpSessionBindingEvent event) {
		String name = event.getName();
		HttpSession session = event.getSession();
		SessionContext.putValue(session, name, event.getValue());
		if(handlerContext.containsKey(name)){
			handlerContext.get(name).attributeAdded(session);
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeRemoved(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void attributeRemoved(HttpSessionBindingEvent event) {
		HttpSession session = event.getSession();
		boolean isValid = isValid(session);
		String name = event.getName();
		if(isValid){
			if(handlerContext.containsKey(name)){
				handlerContext.get(name).attributeRemoved(session);
			}
		}else{
			if(handlerContext.containsKey(name)){
				handlerContext.get(name).sessionDestroyed(session);
			}
			SessionContext.removeSession(session.getId());
		}
		SessionContext.removeValue(session.getId(), name);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionAttributeListener#attributeReplaced(javax.servlet.http.HttpSessionBindingEvent)
	 */
	public void attributeReplaced(HttpSessionBindingEvent event) {
		String name = event.getName();
		HttpSession session = event.getSession();
		Object preValue = event.getValue();
		Object curValue = session.getAttribute(name);
		if(handlerContext.containsKey(name)){
			handlerContext.get(name).attributeReplaced(preValue,curValue,event.getSession());
		}
		SessionContext.putValue(session, name, curValue);
	}
	
	private boolean isValid(HttpSession session){
		boolean isValid = false;
		try{
			session.getLastAccessedTime();
			isValid = true;
		}catch(IllegalStateException e){}
		
		return isValid;
	}
}
