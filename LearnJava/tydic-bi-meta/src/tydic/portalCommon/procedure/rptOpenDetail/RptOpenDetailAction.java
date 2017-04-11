package tydic.portalCommon.procedure.rptOpenDetail;

import java.util.HashMap;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.yhd.utils.Pager;

/**
 * 类描述：
 * 
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-5-21 下午04:13:02
 */
public class RptOpenDetailAction {

	private RptOpenDetailDAO rptOpenDetailDAO;

	public Map<String, Object> getTableData(Map<String, Object> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		int allPageCount = rptOpenDetailDAO.getDataCount(paramMap);// 总记录数
		map.put("allPageCount", allPageCount);

		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		page.setTotalNum(allPageCount);

		map.putAll(rptOpenDetailDAO.getTableData(paramMap, page));// 记录
		return map;
	}
	
    /**
     * 导出数据
     */
	public Map<String, Object> expTxtData(Map<String, Object> paramMap) {
		Pager page=(Pager)paramMap.get("page");
		RptOpenDetailDAO rptOpenDetailDAO =new RptOpenDetailDAO();
		return rptOpenDetailDAO.getTableData(paramMap, page);// 记录
	}

	public void setRptOpenDetailDAO(RptOpenDetailDAO rptOpenDetailDAO) {
		this.rptOpenDetailDAO = rptOpenDetailDAO;
	}
}
