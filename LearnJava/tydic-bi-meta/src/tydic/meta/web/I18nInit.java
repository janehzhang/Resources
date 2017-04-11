package tydic.meta.web;

import tydic.frame.web.ISystemStart;
import tydic.meta.sys.i18n.I18nManager;

import javax.servlet.ServletContext;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author: 程钰
 * @description b本地化信息启动加载类
 * @date: 12-3-31
 * @time: 上午9:37
 */

public class I18nInit implements ISystemStart {
    /* (non-Javadoc)
	 * @see tydic.frame.web.ISystemStart#destory()
	 */
	public void destory() {
    	I18nManager.clear();
	}

	/* (non-Javadoc)
	 * @see tydic.frame.web.ISystemStart#init()
	 */
	public void init() {
		I18nManager.load();
	}

	/* (non-Javadoc)
	 * @see tydic.frame.web.ISystemStart#setServletContext(javax.servlet.ServletContext)
	 */
	public void setServletContext(ServletContext arg0) {

	}
}
