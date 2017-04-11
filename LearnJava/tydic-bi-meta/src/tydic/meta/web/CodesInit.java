/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd.
 * All rights reserved.
 * @see 作用(Ctrlr:具体功能作用,DAO:针对表,PO：针对的表)
 *
 * @author 谭红涛
 * @date 2012-3-27
 */
package tydic.meta.web;

import javax.servlet.ServletContext;

import tydic.frame.web.ISystemStart;
import tydic.meta.sys.code.CodeManager;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd.
 * All rights reserved.
 *
 * 作用(Ctrlr:具体功能作用,DAO:针对表,PO：针对的表)
 *
 * @author 谭红涛
 * @date 2012-3-27
 */
public class CodesInit implements ISystemStart {

	/* (non-Javadoc)
	 * @see tydic.frame.web.ISystemStart#destory()
	 */
	public void destory() {
		CodeManager.clear();
	}

	/* (non-Javadoc)
	 * @see tydic.frame.web.ISystemStart#init()
	 */
	public void init() {
		CodeManager.load();
	}

	/* (non-Javadoc)
	 * @see tydic.frame.web.ISystemStart#setServletContext(javax.servlet.ServletContext)
	 */
	public void setServletContext(ServletContext arg0) {
		
	}

}
