package tydic.portalCommon.coreLink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;

/**
 * 
 * @author yanhd
 * 
 */
public class CustomerCoreScoreDetailAction {
	
	private CustomerCoreScoreDetailDAO customerCoreScoreDetailDAO; 
	
	
	
	/**
	 * 获取最新显示的月份
	 */
	public String getNewMonth() {
		try {
			CustomerCoreScoreDetailDAO dao = new CustomerCoreScoreDetailDAO();
			return dao.getNewMonth();
		} catch (Exception e) {
			Log.error(null, e);
		}
		return "";
	} 	
	
	//环节得分
	public Map<String, Object> getHjCoreData(Map<String, Object> queryData) {
		return customerCoreScoreDetailDAO.getHjCoreData(queryData);
	}
	
	//环节偏离量
	public Map<String, Object> getHjBllData(Map<String, Object> queryData) {
		return customerCoreScoreDetailDAO.getHjBllData(queryData);
	}
	
	
	//月环节得分
	public Map<String, Object> getMonHjCoreData(Map<String, Object> queryData) {
		return customerCoreScoreDetailDAO.getMonHjCoreData(queryData);
	}
	
	//区域环节得分
	public Map<String, Object> getAreaHjCoreData(Map<String, Object> queryData) {
		return customerCoreScoreDetailDAO.getAreaHjCoreData(queryData);
	}
	
	
	
	//21区域环节得分
	public Map<String, Object> get21AreaHjCoreData(Map<String, Object> queryData) {
		return customerCoreScoreDetailDAO.get21AreaHjCoreData(queryData);
	}
	   /**
	    *  21个地市
	     * 方法描述：
	     * @param: 
	     * @return: 
	     * @version: 1.0
	     * @author: yanhaidong
	     * @version: 2013-4-24 下午04:21:10
	    */
	   public Map<String, Object> loadSet21AreaChart(Map<String, Object> map){
		   String changeZone=MapUtils.getString(map, "changeZone",    "0");
		   Map<String, Object> keyXML=new HashMap<String, Object>();
		   //柱状图
		   if(changeZone.equals("0")){
			   keyXML=get21AreaHjCoreData(map);
		   }else{
			   keyXML=getAreaHjCoreData(map);
		   }
		   return keyXML;
	   }
	   
	public List<Map<String,Object>> getSelectMon(){
		return customerCoreScoreDetailDAO.getSelectMon();
	}   
	public void setCustomerCoreScoreDetailDAO(CustomerCoreScoreDetailDAO customerCoreScoreDetailDAO) {
		this.customerCoreScoreDetailDAO = customerCoreScoreDetailDAO;
	}

	

}
