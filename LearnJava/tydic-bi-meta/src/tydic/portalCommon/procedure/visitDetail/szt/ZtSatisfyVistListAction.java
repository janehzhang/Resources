package tydic.portalCommon.procedure.visitDetail.szt;
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

public class ZtSatisfyVistListAction {
	private ZtSatisfyVistListDAO visitListDAO;

	public Map<String, Object> getTableData(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getTableData(paramMap, page);
	}

	/**
	 * 导出数据
	 */
	public Map<String, Object> expTxtData(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		ZtSatisfyVistListDAO visitListDAO = new ZtSatisfyVistListDAO();
		return visitListDAO.getTableData(paramMap, page);// 记录
	}


    
	public void setVisitListDAO(ZtSatisfyVistListDAO visitListDAO) {
		this.visitListDAO = visitListDAO;
	}
}
