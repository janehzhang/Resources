package tydic.meta.module.mag.login;

import tydic.frame.SystemVariable;
import tydic.meta.web.session.SessionManager;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author: 程钰
 * @description
 * @date: 12-2-16
 * @time: 下午3:28
 */

public class LoginReportAction {
    /**
     * 数据库操作类
     */
    private LoginReportDAO loginReportDAO;
    public void setLoginReportDAO(LoginReportDAO loginReportDAO) {
        this.loginReportDAO = loginReportDAO;
    }

    /**
     * 加载Zone treeGrid树
     * @param queryData
     * @return
     */
    public List<Map<String,Object>> queryZoneTreeData(Map<String,Object> queryData){
        return loginReportDAO.queryZone(queryData,menuName);
    }
    /**
     * 动态加载子菜单
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> querySubZone(Map<String,Object> queryData,int parentId){

        return loginReportDAO.querySubZone(parentId,menuName,queryData);
    }
    /**
     * 获得菜单 名称，用于前台生成列名
     *
     * @return 指定菜单的名称
     */
    private String menuName = SystemVariable.getString("menuId","10,11,12,13,294,443").toString();
    public List<Map<String , Object>> queryMenuName(){
    	//String menuName = SystemVariable.CONF_PROPERTIES.getProperty("reportMenuId");
//        Map<String,Object> user= SessionManager.getCurrentUser();
    	return loginReportDAO.getMenuName(menuName);
    }

}
