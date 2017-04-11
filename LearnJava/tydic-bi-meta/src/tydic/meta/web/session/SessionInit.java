/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd.
 * All rights reserved.
 * @see 作用(Ctrlr:具体功能作用,DAO:针对表,PO：针对的表)
 *
 * @author 谭红涛
 * @date 2012-3-13
 */
package tydic.meta.web.session;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletContext;

import tydic.frame.SystemVariable;
import tydic.frame.web.ISystemStart;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd.
 * All rights reserved.
 *
 * 作用(Ctrlr:具体功能作用,DAO:针对表,PO：针对的表)
 *
 * @author 谭红涛
 * @date 2012-3-13
 */
public class SessionInit implements ISystemStart {
	
	/* (non-Javadoc)
	 * @see tydic.frame.web.ISystemStart#destory()
	 */
	public void destory() {
		SessionManager.destroy();
		SessionContext.destory();
	}

	/* (non-Javadoc)
	 * @see tydic.frame.web.ISystemStart#init()
	 */
	public void init() {
		String handlerStr = SystemVariable.getString("session.handler", "");
		String[] handlers = handlerStr.split(",");
		for(String handler:handlers){
			String[] array = handler.split(":");
			String key = array[0];
			String className = array[1];
			try {
				SessionAttributeListener.addHandler(key,(AbstractSessionEvtHandler) Class.forName(className).getConstructor(String.class).newInstance(key));
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see tydic.frame.web.ISystemStart#setServletContext(javax.servlet.ServletContext)
	 */
	public void setServletContext(ServletContext servletContext) {
	}

}
