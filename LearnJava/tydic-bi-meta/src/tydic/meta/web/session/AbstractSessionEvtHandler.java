/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd.
 * All rights reserved.
 * @see 作用(Ctrlr:具体功能作用,DAO:针对表,PO：针对的表)
 *
 * @author 谭红涛
 * @date 2012-1-30
 */
package tydic.meta.web.session;

import javax.servlet.http.HttpSession;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd.
 * All rights reserved.
 *
 * 作用(Ctrlr:具体功能作用,DAO:针对表,PO：针对的表)
 *
 * @author 谭红涛
 * @date 2012-1-30
 */
public abstract class AbstractSessionEvtHandler {
	private String key;
	
	public AbstractSessionEvtHandler(String key){
		this.key = key;
	}
	
	public String getKey(){
		return key;
	}
	public abstract void attributeAdded(HttpSession session);
	public abstract void attributeRemoved(HttpSession session);
	public abstract void attributeReplaced(Object preValue,Object curValue,HttpSession session);
	public abstract void sessionDestroyed(HttpSession session);
}
