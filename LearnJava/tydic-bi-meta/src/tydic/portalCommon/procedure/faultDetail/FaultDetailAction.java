/**
  * 文件名：FaultDetailAction.java
  * 版本信息：Version 1.0
  * 日期：2013-5-21
  * Copyright tydic.com.cn Corporation 2013 
  * 版权所有
  */
package tydic.portalCommon.procedure.faultDetail;

import java.util.HashMap;
import java.util.Map;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.yhd.utils.Pager;



	/**
 * 类描述：
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-5-21 下午04:13:02 
 */
public class FaultDetailAction {
	
	private FaultDetailDAO faultDetailDAO;
	
	public Map<String, Object> getTableData(Map<String, Object>  paramMap) {
		 
		  Map<String, Object> map=new HashMap<String,Object>();
		  int allPageCount=faultDetailDAO.getDataCount(paramMap);//总记录数
		  map.put("allPageCount", allPageCount);
		
		  Pager page=Pager.getInstance();
		  page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		  page.setSize(Convert.toInt(paramMap.get("pageCount")));
		  page.setTotalNum(allPageCount);
		  //map.put("page", page);
		  
		  map.putAll(faultDetailDAO.getTableData(paramMap,page));// 记录
          return map;
	}
	
	
	/**
	 * 导出数据
	 */
	public Map<String, Object> expTxtData(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		FaultDetailDAO faultDetailDAO = new FaultDetailDAO();
		return faultDetailDAO.getTableData(paramMap, page);// 记录
	}
	public void setFaultDetailDAO(FaultDetailDAO faultDetailDAO) {
		this.faultDetailDAO = faultDetailDAO;
	}
}
