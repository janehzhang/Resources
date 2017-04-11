package tydic.portalCommon.procedure.visitDetail;

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
public class ADSLVisitListAction {

	private ADSLVisitListDAO visitListDAO;

	public Map<String, Object> getTableData(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getTableData(paramMap, page);
	}
	
	
    /**
     * add Chenwei Guang
     * date 2015-01-28
     * ivr data load 
     * **/
	
	public Map<String, Object> getTableDataIVRZYJ(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getTableDataIVRZYJ(paramMap, page);
	}
	
	public Map<String, Object> getTableDataIVRZYJSum(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		return visitListDAO.getTableDataIVRZYJ(paramMap, page);
	}
	
	/**
	 * 导出数据
	 */
	public Map<String, Object> expTxtData(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		ADSLVisitListDAO visitListDAO = new ADSLVisitListDAO();
		return visitListDAO.getTableData(paramMap, page);// 记录
	}
	
	public Map<String, Object> expTxtDataIVRZYJ(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		ADSLVisitListDAO visitListDAO = new ADSLVisitListDAO();
		return visitListDAO.getTableDataIVRZYJ(paramMap, page);// 记录
	}
	
    public boolean  updateVisitResult(Map<String, Object> param)
    {
    	return visitListDAO.updateVisitResult(param);
    }
          
    public boolean  updateVisitResultIVR(Map<String, Object> param)
    {
    	return visitListDAO.updateVisitResultIVR(param);
    }
    
    /**
     * add yanhd
     * **/
    //营业厅
    public boolean  updateHfVisitResult(Map<String, Object> param)
    {
    	return visitListDAO.updateHfVisitResult(param);
    }
    //实体渠道评价器
    public boolean  updateIshopVisitResult(Map<String, Object> param)
    {
    	return visitListDAO.updateIshopVisitResult(param);
    }
  //10000号
    public boolean  updateS10000VisitResult(Map<String, Object> param)
    {
    	return visitListDAO.updateS10000VisitResult(param);
    }
  //号百
    public boolean  updateHbVisitResult(Map<String, Object> param)
    {
    	return visitListDAO.updateHbVisitResult(param);
    }
    //投诉
    public boolean  updateTsVisitResult(Map<String, Object> param)
    {
    	return visitListDAO.updateTsVisitResult(param);
    }
  //掌厅
    public boolean  updateZtVisitResult(Map<String, Object> param)
    {
    	return visitListDAO.updateZtVisitResult(param);
    }
  //网厅
    public boolean  updateWtVisitResult(Map<String, Object> param)
    {
    	return visitListDAO.updateWtVisitResult(param);
    }
    
    /**
     * 整体投诉满意度
     * @param param
     * @return
     */
	public Map<String, Object> tsSatisfyVisitDetails(Map<String, Object> paramMap) {
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
//		return visitListDAO.getTableDataIVRZYJ(paramMap, page);
		return visitListDAO.tsSatisfyVisitDetails(paramMap, page);
		
	}
    
	public void setVisitListDAO(ADSLVisitListDAO visitListDAO) {
		this.visitListDAO = visitListDAO;
	}
}
