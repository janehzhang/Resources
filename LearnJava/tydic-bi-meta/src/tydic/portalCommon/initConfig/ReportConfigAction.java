
/**   
 * @文件名: ReportConfig.java
 * @包 tydic.portalCommon.initConfig
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-5-15 上午11:16:39
 *  
 */
  
package tydic.portalCommon.initConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.meta.common.Page;
import tydic.meta.web.session.SessionManager;

/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：ReportConfig   
 * 类描述：   
 * 创建人：wuxl@tydic.com 
 * 创建时间：2012-5-15 上午11:16:39   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class ReportConfigAction {
	ReportConfigDAO reportConfigDAO;
	public ReportConfigDAO getReportConfigDAO() {
		return reportConfigDAO;
	}
	public void setReportConfigDAO(ReportConfigDAO reportConfigDAO) {
		this.reportConfigDAO = reportConfigDAO;
	}
	
	/**
	 * @Title: ReportConfigAction 
	 * @Description: 获取该报表下的用户配置的所有配置指标
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String, Object>>  getUserIndicators(Map<String, Object> data, Page page){
		Object userId = SessionManager.getCurrentUserID();
		String zoneId = reportConfigDAO.getZoneCode(userId.toString());
		return reportConfigDAO.getUserIndicators(data,userId.toString(),zoneId);
	}
	/**
	 * @Title: getIndexMes 
	 * @Description: 获取灌输数据表的指标编码和名称
	 * @param tabId
	 * @param localCode
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getIndexMes(String tabId){
		Object userId = SessionManager.getCurrentUserID();
		String zoneId = reportConfigDAO.getZoneCode(userId.toString());
		return reportConfigDAO.getIndexMes(tabId,zoneId);
	}
	/**
	 * @Title: ReportConfigAction 
	 * @Description: 获取该报表下的所有指标信息
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String, Object>>  getIndexInfo(String tabId){
		return reportConfigDAO.getIndexInfo(tabId);
	}
	
	/**
	 * @Title: getColumnsInfo 
	 * @Description: 获取该报表下的所有的列信息
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String, Object>>  getColumnsInfo(String tabId){
		return reportConfigDAO.getColumnsInfo(tabId);
	}
	
	/**
	 * @Title: ReportConfigAction 
	 * @Description: 保存用户指标配置
	 * @return boolean
	 * @throws
	 */
    public boolean saveUserConfigure(List<Map<String,Object>> datas){
    	try{
    		BaseDAO.beginTransaction();
    		
       	    Map<String,Object> data = new HashMap<String,Object>();
       	    data.put("userId", SessionManager.getCurrentUserID());
       	    if(datas.size() == 1)  
       	    {
       	        //如果配置时用户一行都没选，则设tabId为用户选择的报表类型
       	    	data.put("tabId", datas.get(0)); 
       	    }
       	    else
       	    {	
       	    	data.put("tabId", datas.get(0).get("tabId")); 
       	    }
       	    
       	    //删除当前用户已配置的所有指标
	    	 int deleteflag = reportConfigDAO.deleteIndexData(data);
	    	 if(deleteflag < 0)
	    	 {
	    		 return false;
	    	 }
	    	 
	    	//保存用户指标配置 
       	    for(int i=0;i<datas.size()-1;i++)
       	    {
       	    	//获取对应的TBA_ID INDEX_CD
           	    List<Map<String,Object>> saveData = new ArrayList<Map<String,Object>>();
       	    	data.put("tabId", datas.get(i).get("tabId")); 
       	    	data.put("indexCd", datas.get(i).get("indexCd")); 
       	    	saveData.add(data);
       	    	
       	    	//保存
       	    	if (saveData != null && saveData.size() > 0) 
       	    	{
       	    		reportConfigDAO.saveUserConfigure(saveData);
       	    	}
       	    }
    		BaseDAO.commit();
        } catch(Exception e){
            BaseDAO.rollback();
            Log.error(null,e);
            return false;
        }
    	return true;
    }
    
	/**
	 * @Title: getPortalWarning 
	 * @Description: 保存一条预警信息
	 * @return boolean
	 * @throws
	 */
	public String saveWarningInfo(Map<String,Object> data){
		Map<String,Object> map = new HashMap<String,Object>();
		String  tabId =data.get("tabId").toString();
		String  columnId=data.get("columnId").toString();
		String  indexId=data.get("indexCd").toString();
		if(tabId.equals("1")) //日报
		{
			if(columnId.equals("VALUE2")) data.put("columnName", "当日值");
			if(columnId.equals("VALUE3")) data.put("columnName", "本月累计");
			if(columnId.equals("VALUE4")) data.put("columnName", "上月同期累计");
			if(columnId.equals("VALUE5")) data.put("columnName", "环比");
			if(columnId.equals("VALUE6")) data.put("columnName", "上年同期累计");
			if(columnId.equals("VALUE7")) data.put("columnName", "同比");
			if(columnId.equals("VALUE8")) data.put("columnName", "本月累计平均值");
			data.put("tabName", "日重点指标");
		}else//月报
		{
			if(columnId.equals("VALUE2")) data.put("columnName", "当月值");
			if(columnId.equals("VALUE3")) data.put("columnName", "上月值");
			if(columnId.equals("VALUE4")) data.put("columnName", "环比");
			if(columnId.equals("VALUE6")) data.put("columnName", "上年同期累计");
			if(columnId.equals("VALUE7")) data.put("columnName", "同比");
			if(columnId.equals("VALUE8")) data.put("columnName", "本年平均值");
			data.put("tabName", "月重点指标");
		}
		
	    String indexName= reportConfigDAO.getIndexName(tabId,indexId);
	    data.put("indexName", indexName);
		map = reportConfigDAO.getWarningId(tabId ,indexId,columnId);
		if(null != map)
		{
			 return "该预警配置已存在，不能重复配置！";
		}else{
			
			boolean flag = reportConfigDAO.saveWarningInfo(data);
			if(flag)
			{
				return "新增预警配置成功!";
			}else{
				return "新增预警配置失败,请重试!";
			}
		}
	}
	
	/**
	 * @Title: getPortalWarning 
	 * @Description: 获取报表预警所有信息
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getPortalWarning(Map<String,Object> data){
		return reportConfigDAO.getPortalWarning(data);
	}
	
	/**
	 * @Title: ReportConfigAction 
	 * @Description: 获取报表tab所有信息
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getReportTabMes(Map<String,Object> data){
		return reportConfigDAO.getReportTabMes(data);
	}
	public boolean saveReportTab(Map<String,Object> data){
		return reportConfigDAO.saveReportTab(data);
	}

    /**
     * 查询报表名称
     * @return
     */
    public List<Map<String,Object>> queryReportName(){
        return reportConfigDAO.queryReportName();
    }
    
    /**
     * 方法：执行数据导入
     * @param data
     * @return boolean
     */
    public boolean importData(Map<String,Object> data)
    {
    	//获取要导入的数据DATA_DATE及LOCAL_CODE
    	List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
    	
   	    //获取对应的Scope_id外键及RPT_TYPE字段
   	    List<Map<String,Object>> listscope = new ArrayList<Map<String,Object>>();
   	    
   	    //放入用户ID及时间到data中
   	    data.put("userId", SessionManager.getCurrentUserID());
   	    data.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    	 try
    	 {
    		 BaseDAO.beginTransaction();
    		 //查询对应的Scope_id外键及RPT_TYPE字段
    		 listscope = reportConfigDAO.getScope_id(data); 
    		 if(listscope.size() <= 0)
    		 {
    			 return false;
    		 }
	    	 for(int j = 0;j < listscope.size();j++)
	    	 {

	    	     //封装要导入的所有数据
	    	   	 List<Map<String,Object>> datas = new ArrayList<Map<String,Object>>();
	    		 data.put("TAB_ID", listscope.get(j).get("TAB_ID")); 
	    		 data.put("SCOPE_ID", listscope.get(j).get("SCOPE_ID")); 
	    		 data.put("RPT_TYPE", listscope.get(j).get("RPT_TYPE")); 
	    	 
		    	 //封装导入的数据
		    	 list = reportConfigDAO.getInputData(data);
		    	 for(int i = 0;i < list.size();i++)
		    	 {
		    		 data.put("DATA_DATE" + i, list.get(i).get("DATA_DATE"));
		             data.put("LOCAL_CODE" + i, list.get(i).get("LOCAL_CODE"));
		             datas.add(data);
		         }
		    	
		    	 //删除日期范围内已经存在的数据
		    	 int deleteflag = reportConfigDAO.deleteData(data);
		    	 if(deleteflag < 0)
		    	 {
		    		 return false;
		    	 }
		    	 //执行数据导入
	             if (datas != null && datas.size() > 0) 
	             {
	            	 reportConfigDAO.importData(datas);  
	             }
	    	 }
	         BaseDAO.commit();
         } catch(Exception e){
             BaseDAO.rollback();
             tydic.frame.common.Log.error("", e);
             return false;
         }
    	return true;
    }  
    
	/**
	 * @Title: getReportTabById 
	 * @Description: 根据ID获取报表tab信息
	 * @param id
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getReportTabById(String id){
		return reportConfigDAO.getReportTabById(id);
	}
	/**
	 * @Title: updateReportTabById 
	 * @Description: 根据id更新信息
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean updateReportTabById(Map<String,Object> data){
		return reportConfigDAO.updateReportTabById(data);
	}
	/**
	 * @Title: delReportTabById 
	 * @Description: 根据tabId删除所有信息（1、配置信息；2、灌输的数据信息）
	 * @param id
	 * @return boolean   
	 * @throws
	 */
	public boolean delReportTabById(String id){
		try{
			BaseDAO.beginTransaction();
			if(reportConfigDAO.delReportTabById(id)){
				BaseDAO.commit();
				return true;
			}else
				return false;
		}catch(Exception e){
            BaseDAO.rollback();
            Log.error(null,e);
            return false;
        }
	}
	/**
	 * @Title: getRepColMesByTabId 
	 * @Description: 根据tabId获取列信息
	 * @param tabId
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getRepColMesByTabId(String tabId){
		return reportConfigDAO.getRepColMesByTabId(tabId);
	}
	/**
	 * @Title: getRepChartByTabId 
	 * @Description:根据tabId获取图形信息
	 * @param tabId
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getRepChartByTabId(String tabId){
		return reportConfigDAO.getRepChartByTabId(tabId);
	}
	/**
	 * @Title: getRepExpByTabId 
	 * @Description: 根据tabId获取指标解释信息
	 * @param tabId
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getRepExpByTabId(String tabId){
		return reportConfigDAO.getRepExpByTabId(tabId);
	}
	/**
	 * @Title: getColMesByColId 
	 * @Description: 根据colId获取列信息
	 * @param colId
	 * @param @return    
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getColMesByColId(String colId){
		return reportConfigDAO.getColMesByColId(colId);
	}
	/**
	 * @Title: delColMesByColId 
	 * @Description: 根据colId删除列信息
	 * @param colId
	 * @return boolean   
	 * @throws
	 */
	public boolean delColMesByColId(String colId){
		return reportConfigDAO.delColMesByColId(colId);
	}
	/**
	 * @Title: saveColMes 
	 * @Description:保存列信息 
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean saveColMes(Map<String,Object> data){
		return reportConfigDAO.saveColMes(data);
	}
	/**
	 * @Title: updateColMes 
	 * @Description: 根据colId更新列信息
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean updateColMes(Map<String,Object> data){
		return reportConfigDAO.updateColMes(data);
	}
	/**
	 * @Title: getInterval 
	 * @Description:获取时间区间 
	 * @param state 1:时间段标识；0：不是时间段 
	 * @param type 获取时间的类型 按天  按日 按月
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getInterval(Map<String,Object> data){
		int typeId = 1;
//		if(type!=null){
//		  typeId = Integer.valueOf(type.trim());
//	   }
	   String state =data.get("state").toString();
	   Object type = data.get("type");
	   if(type!=null){
		   typeId = Integer.valueOf(type.toString().trim());
	   }
//	   map.put("state", state);
//	   map.put("typeId", typeId);
	  return reportConfigDAO.getInterval(state,typeId);
	}
	/**
	 * @Title: getInterval 
	 * @Description:获取时间区间 
	 * @param state 1:时间段标识；0：不是时间段 
	 * @param type 获取时间的类型 按天  按日 按月
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getInterval2(String state,String type){
		int typeId = 1;
	   if(type!=null){
		   typeId = Integer.valueOf(type.toString().trim());
	   }
//	   map.put("state", state);
//	   map.put("typeId", typeId);
	  return reportConfigDAO.getInterval(state,typeId);
	}
	/**
	 * @Title: saveChart 
	 * @Description: 新增图形配置信息
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public String saveChart(Map<String,Object> data){
		String tabId = null;
		Object tabIdObj = data.get("tabId");
		if(tabIdObj!=null){
			tabId = tabIdObj.toString();
		}
		Object userId = SessionManager.getCurrentUserID();
		String zoneId = reportConfigDAO.getZoneCode(userId.toString());
		if(tabId!=null){  
		  List<Map<String,Object>> list =  reportConfigDAO.getIndexMes(tabId,zoneId);//先判断当前报表是否有指标名称
		  if(list==null||list.size()==0){
			  return "1";
		  }
		String code = data.get("indexCd").toString();
		String colId = data.get("columnId").toString();
		String color = data.get("brokenLineColor").toString();
		List<Map<String,Object>> list1 = reportConfigDAO.getIndexColsInfo(tabId, code,colId,color); //判断当前的code是否已经有关联列重复(关联列可以重复,但是折线颜色不能重复)
		if(list1!=null&&list1.size()>0){
			  return "2";
		}else{
		reportConfigDAO.saveChart(data);
		return "3";
	   }
	  }
		return "0";
	}
	/**
	 * @Title: updateChart 
	 * @Description: 更新图形配置信息
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean updateChart(Map<String,Object> data){
		return reportConfigDAO.updateChart(data);
	}
	/**
	 * @Title: delChart 
	 * @Description: 删除图形配置信息
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean delChart(Map<String,Object> data){
		return reportConfigDAO.delChart(data);
	}
	/**
	 * @Title: saveExp 
	 * @Description: 新增指标解释
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean saveExp(Map<String,Object> data){
		return reportConfigDAO.saveExp(data);
	}
	/**
	 * @Title: updateExp 
	 * @Description: 修改指标解释
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean updateExp(Map<String,Object> data){
		return reportConfigDAO.updateExp(data);
	}
	/**
	 * @Title: delExp 
	 * @Description: 删除指标解释
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean delExp(Map<String,Object> data){
		return reportConfigDAO.delExp(data);
	}
	/**
	 * @Title: modifyRepTabOrderId 
	 * @Description: 修改tab的排序ID
	 * @param datas
	 * @return boolean   
	 * @throws
	 */
	public boolean modifyRepTabOrderId(List<Map<String,Object>> datas){
		try{
			if(datas != null && datas.size() > 0){
				Object[][] data = new Object[datas.size()][2];
				int r = 0;
				for(Map<String,Object> m : datas){
					Object[] o = {m.get("orderId"),m.get("tabId")};
					data[r] = o;
					r++;
				}
				reportConfigDAO.batchUpdateRepTabOrderId(data);
				return true;
			}else
				return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * @Title: modifyColOrderId 
	 * @Description: 修改列的排序ID
	 * @param datas
	 * @return boolean   
	 * @throws
	 */
	public boolean modifyColOrderId(List<Map<String,Object>> datas){
		try{
			if(datas != null && datas.size() > 0){
				Object[][] data = new Object[datas.size()][2];
				int r = 0;
				for(Map<String,Object> m : datas){
					Object[] o = {m.get("orderId"),m.get("colId")};
					data[r] = o;
					r++;
				}
				reportConfigDAO.batchUpdateColOrderId(data);
				return true;
			}else
				return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 查询列信息中的相关关联列
	 * @param tableId 报表的id
	 * */
	public List<Map<String,Object>> queryColInfoData(int tableId){
		return reportConfigDAO.queryColInfoData(tableId);
	}

	/**
	 * 判断当前指标编码是否已经存在
	 * @param code 新增的code
	 * @param tableId 报表id
	 * */
	public boolean isExistCode(String code,int tableId){
		return reportConfigDAO.isExistCode(code, tableId);
	}

	/**
	 * @Title: getWarningId 
	 * @Description: 根据tabid及indexCd查询出一条预警信息
	 * @param id
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getWarningId(String tabId,String indexCd,String columnId){
		 return reportConfigDAO.getWarningId(tabId,indexCd,columnId);
	}
	/**
	 * @Title: getWarningId 
	 * @Description: 根据tabid及indexCd删除一条预警信息
	 * @param id
	 * @return Map<String,Object>   
	 * @throws
	 */
	public boolean deleteWarningId(String tabId,String indexCd,String columnId){
		return reportConfigDAO.deleteWarningId(tabId,indexCd,columnId);
	}
	/**
	 * @Title: updateWarning
	 * @Description: 根据tabid及indexCd修改一条预警信息
	 * @param data
	 * @return boolean   
	 * @throws
	 */
	public boolean updateWarning(Map<String,Object> data){
		return reportConfigDAO.updateWarning(data);
	}
	
	public List<Map<String, Object>>  getUserIndexsByTabId(String tabId){
		Object userId = SessionManager.getCurrentUserID();
		String zoneId = reportConfigDAO.getZoneCode(userId.toString());
		return reportConfigDAO.getUserIndexsByTabId(tabId,userId.toString(),zoneId);
    }
	
	
	
	
	/**
	 *  普通报表的预警
	 *  add yanhd start
	 */
	public List<Map<String, Object>> getReportWarnList(String menuName) {
		return reportConfigDAO.getReportWarnList(menuName);
	}
	/**
	 * 分页
	 */
	public List<Map<String, Object>> getReportWarnPage(Map<String,Object> data) {
		return reportConfigDAO.getReportWarnPage(data);
	}
	/**
	 * 保存
	 */
	public String  saveReportWarning(Map<String,Object> data)
	{
		   boolean flag = reportConfigDAO.saveReportWarning(data);
			if(flag)
			{
				return "新增预警配置成功!";
			}else{
				return "新增预警配置失败,请重试!";
			}
	}
	/**
	 * 删除
	 */
	public boolean deleteReportWarning(String reportId){
		return reportConfigDAO.deleteReportWarning(reportId);
	}
	
	
	/**
	 * 通过主键查询
	 */
	
	public Map<String,Object>  getReportWarningId(String reportId)
	{
		 return reportConfigDAO.getReportWarningId(reportId);
	}
	/**
	 * 修改
	 */
	public  boolean  updateReportWarning(Map<String,Object> data)
	{
		return reportConfigDAO.updateReportWarning(data);
	}
	/**
	 *  add yanhd end
	 */
}
