package tydic.reports.codeMonitor;

import java.util.HashMap;
import java.util.Map;
/**
 by qx
 * @modifyDate  2013-12-12
 */
public class CodeMonitorAction {
    /**
     * 数据库操作类
     */
    private CodeMonitorDAO codeMonitorDAO;
    
	public CodeMonitorDAO getCodeMonitorDAO() {
		return codeMonitorDAO;
	}
	public void setCodeMonitorDAO(CodeMonitorDAO codeMonitorDAO) {
		this.codeMonitorDAO = codeMonitorDAO;
	}    
	/***
	 * 查询 客户密码总监控报表
	 * @param queryData
	 * @return
	 */
	public Map getCustCodeMonitor_sum(Map<String, Object> queryData){
		Map<String, Object> map=new HashMap<String,Object>();
		 map.putAll(codeMonitorDAO.getCustCodeMonitor_sum(queryData));//客户密码激活管理模块
		return map;
	}
	
	public Map getCustCodeMonitor_pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(codeMonitorDAO.getCustCodeMonitor_pg(queryData));//客户密码激活管理模块
        return map; 
	}
	public Map getCustCodeExceptionMonitor_pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(codeMonitorDAO.getCustCodeExceptionMonitor_pg(queryData));//客户密码异常监控管理模块
        return map; 
	}
	public Map getCustCodeActiveMonitor_pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(codeMonitorDAO.getCustCodeActiveMonitor_pg(queryData));//客户密码异常监控管理模块
        return map; 
	}
	public Map getCustCodePreferMonitor_pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(codeMonitorDAO.getCustCodePreferMonitor_pg(queryData));//客户密码异常监控管理模块
        return map; 
	}
}
