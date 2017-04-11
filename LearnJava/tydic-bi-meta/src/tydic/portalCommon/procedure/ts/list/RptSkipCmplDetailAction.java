/**
 * 文件名：RptSkipCmplDetailAction.java
 * 版本信息：Version 1.0
 * 日期：2013-6-25
 * Copyright tydic.com.cn Corporation 2013 
 * 版权所有
 */
package tydic.portalCommon.procedure.ts.list;

import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.yhd.utils.Pager;

/**
 * 类描述：
 * 
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-6-25 上午11:50:31
 */
public class RptSkipCmplDetailAction {

	private RptSkipCmplDetailDAO rptSkipCmplDetailDAO;

	public Map<String, Object> getTableData(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return rptSkipCmplDetailDAO.getTableData(paramMap, page);
	}

	/**
	 * 导出数据
	 */
	public Map<String, Object> expTxtData(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		RptSkipCmplDetailDAO rptSkipCmplDetailDAO = new RptSkipCmplDetailDAO();
		return rptSkipCmplDetailDAO.getTableData(paramMap, page);// 记录
	}

	public void setRptSkipCmplDetailDAO(
			RptSkipCmplDetailDAO rptSkipCmplDetailDAO) {
		this.rptSkipCmplDetailDAO = rptSkipCmplDetailDAO;
	}

}
