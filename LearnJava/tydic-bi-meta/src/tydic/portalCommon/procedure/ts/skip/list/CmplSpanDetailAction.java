/**
 * 文件名：CmplSpanDetailAction.java
 * 版本信息：Version 1.0
 * 日期：2013-8-22
 * Copyright tydic.com.cn Corporation 2013 
 * 版权所有
 */
package tydic.portalCommon.procedure.ts.skip.list;

import java.util.Map;

import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.yhd.utils.Pager;

/**
 * 类描述：
 * 
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-8-22 下午04:55:37
 */
public class CmplSpanDetailAction {

	private CmplSpanDetailDAO cmplSpanDetailDAO;
	
	

	/**
	 * 
	 * 得最近月份
	 * 
	 */
	public String getNewMonth() {
		try {
			CmplSpanDetailDAO   cmplSpanDetailDAO = new  CmplSpanDetailDAO();
			return cmplSpanDetailDAO.getNewMonth();
		} catch (Exception e) {
			Log.error(null, e);
		}
		 return "";
	}

	public Map<String, Object> getTableData(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return cmplSpanDetailDAO.getTableData(paramMap, page);
	}

	/**
	 * 导出数据
	 */
	public Map<String, Object> expTxtData(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		CmplSpanDetailDAO cmplSpanDetailDAO = new CmplSpanDetailDAO();
		return cmplSpanDetailDAO.getTableData(paramMap, page);// 记录
	}

	public void setCmplSpanDetailDAO(CmplSpanDetailDAO cmplSpanDetailDAO) {
		this.cmplSpanDetailDAO = cmplSpanDetailDAO;
	}

}
