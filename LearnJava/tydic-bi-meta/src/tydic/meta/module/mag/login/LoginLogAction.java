/**
 *
 */
package tydic.meta.module.mag.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.directwebremoting.WebContextFactory;

import tydic.frame.BaseDAO;
import tydic.frame.SystemVariable;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.OprResult;
import tydic.meta.common.Page;
import tydic.meta.common.yhd.utils.Pager;
import tydic.meta.module.mag.user.UserConstant;
import tydic.meta.web.session.SessionManager;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 熊小平
 * @description 登录日志控制类
 * @date 2011-10-17
 *
 */
public class LoginLogAction {
    private LoginLogDAO loginLogDAO;

    /**
     * setter
     *
     * @param loginLogDAO
     */
    public void setLoginLogDAO(LoginLogDAO loginLogDAO) {
        this.loginLogDAO = loginLogDAO;
    }
    /**
     * 访问排名
     *
     * @param queryData 参数列表
     * @param page 分页参数
     * @return
     */
    public List<Map<String, Object>> queryLoginLog(Map<String, Object> queryData) {
        return loginLogDAO.queryLoginLog(queryData);
    }
    


    /**
     * 某一用户详细访问信息
     *
     * @param queryData 参数列表
     * @param page 分页参数
     * @return
     */
    public List<Map<String, Object>> queryLoginLogByID(Map<String, Object> queryData) {
        return loginLogDAO.queryLoginLogByID(queryData);
    }
    /**
     * 取得需要隐藏的岗位和指定的菜单名称
     */
    String hideStations= SystemVariable.getString("hidden.stations", "");
    private String menuName = (String) SessionManager.getCurrentSession().getAttribute("MenuName");
    /**
     * 此方法用来动态生成登陆日志和菜单访问报表
     * 
     * @param queryData 需要的一些判断条件
     * @return 返回值
     */
    public List<Map<String, Object>> queryLoginReport(Map<String,Object> queryData){
    	//读取config 配置参数，获取隐藏的岗位
        //Map<String,Object> user= SessionManager.getCurrentUser();
    	Map<String,Object> user= (Map<String, Object>) WebContextFactory.get().getSession().getAttribute(LoginConstant.SESSION_KEY_USER);
        if(user.get("adminFlag").toString().trim().equals("1")){
        	queryData.put("adminFlag", "true");
        }else{
        	queryData.put("adminFlag", "false");
        }
        int currentStation= user.get("stationId")==null?null:Integer.parseInt(user.get("stationId").toString());
        //如果是用户管理员或者是隐藏岗位的人员登录，可以看到所有
        if(SessionManager.getCurrentUserID()== UserConstant.ADMIN_USERID
           ||hideStations.contains(String.valueOf(currentStation))){
            hideStations="";
        }
        List<Map<String ,Object>> loginList = loginLogDAO.queryLoginReport(queryData,hideStations);
        String[] menus = menuName.split(",");
        List<Map<String ,Object>> loginCount = new ArrayList<Map<String ,Object>>();
        /**
         * 统计出访问记录的和放在最后一个map中
         */
        Map<String, Object> totalCount = new HashMap<String, Object>();
        totalCount.put("ZONE_ID", -1);
        totalCount.put("ZONE_NAME", "合计");
        int sum=0,menuVisit0=0,menuVisit1=0,menuVisit2=0,menuVisit3=0;
    	for(int j=0; j<loginList.size(); j++){
    		sum += loginList.get(j).get("SUM")==null?0:Integer.parseInt(loginList.get(j).get("SUM").toString());
    		Map<String ,Object> loginList_ = loginList.get(j);
    		for(int i =0 ; i< menus.length; i++){
    			boolean flag = true;
    			List<Map<String, Object>> menuMaps = new ArrayList<Map<String, Object>>();
    			menuMaps = loginLogDAO.queryMenuList(menus[i]);
    			if(menuMaps.size()==0){
    				Map<String, Object> menuMap = new HashMap<String, Object>();
    				menuMap.put("MENU_ID", menus[i]);
    				menuMaps.add(menuMap);
    			}
	            List<Map<String ,Object>> menuList = loginLogDAO.queryMenuReport(queryData, hideStations, menuMaps);
	    		for(int k=0; k<menuList.size(); k++){
	    			Map<String ,Object> menuList_ = menuList.get(k);
	    			
	    			if(menuList_.containsKey("MENUVISITCOUNT") && 
	    					menuList_.get("ZONE_ID").toString().trim().equals(loginList_.get("ZONE_ID").toString().trim())){
	    				loginList_.put("MENU_VISIT"+i, menuList_.get("MENUVISITCOUNT"));
	    				if(i==0)menuVisit0 += menuList_.get("MENUVISITCOUNT") == null?0:Integer.parseInt(menuList_.get("MENUVISITCOUNT").toString());
	    				if(i==1)menuVisit1 += menuList_.get("MENUVISITCOUNT") == null?0:Integer.parseInt(menuList_.get("MENUVISITCOUNT").toString());
	    				if(i==2)menuVisit2 += menuList_.get("MENUVISITCOUNT") == null?0:Integer.parseInt(menuList_.get("MENUVISITCOUNT").toString());
	    				if(i==3)menuVisit3 += menuList_.get("MENUVISITCOUNT") == null?0:Integer.parseInt(menuList_.get("MENUVISITCOUNT").toString());
	    				flag = false;
	    			}
	    		}
	    		if(flag){
					loginList_.put("MENU_VISIT"+i,"0");
				}
    		}
    		loginCount.add(loginList_);
        }
        totalCount.put("SUM", sum);
        totalCount.put("MENU_VISIT0", menuVisit0);
        totalCount.put("MENU_VISIT1", menuVisit1);
        totalCount.put("MENU_VISIT2", menuVisit2);
        totalCount.put("MENU_VISIT3", menuVisit3);
        loginCount.add(totalCount);
        return loginCount;
    }
    /**
     * 获得菜单 名称，用于前台生成列名
     * 
     * @return 指定菜单的名称
     */
    public List<Map<String , Object>> queryMenuName(){
    	//String menuName = SystemVariable.CONF_PROPERTIES.getProperty("reportMenuId");
        Map<String,Object> user= SessionManager.getCurrentUser();
        SessionManager.getCurrentUserID();
        int currentStation= user.get("stationId")==null?null:Integer.parseInt(user.get("stationId").toString());
    	if(SessionManager.getCurrentUserID()== UserConstant.ADMIN_USERID
    	           ||hideStations.contains(String.valueOf(currentStation))){
    	            hideStations="";
    	        }
    	return loginLogDAO.getMenuName(menuName);
    }
    
    
    
    /**
     *  add  @author yanhaidong  start
     */
    
    public  List<Map<String , Object>>  getMenuVisitCount(Map<String , Object>  paramMap)
    {
    	    return loginLogDAO.getMenuVisitCount(paramMap);
    }  
    //OA账号配置列表
    public  Map<String, Object>  getUsersList(Map<String , Object>  paramMap)
	{       Map<String, Object> mapList =new  HashMap<String, Object>();
	        Pager page = Pager.getInstance();
			page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
			page.setSize(Convert.toInt(paramMap.get("pageCount")));
			List<Map<String,Object>> dataColumn = null;
			dataColumn=loginLogDAO.getUsersList(paramMap,page);
			mapList.put("allPageCount", loginLogDAO.getUsersCount(paramMap));
			mapList.put("dataColumn", dataColumn);
    	    return mapList;
    }
    public  Map<String, Object>  getEwamUsersList(Map<String , Object>  paramMap)
	{       Map<String, Object> mapList =new  HashMap<String, Object>();
	        Pager page = Pager.getInstance();
			page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
			page.setSize(Convert.toInt(paramMap.get("pageCount")));
			List<Map<String,Object>> dataColumn = null;
			dataColumn=loginLogDAO.getEwamUsersList(paramMap,page);
			mapList.put("allPageCount", loginLogDAO.getEwamUsersCount(paramMap));
			mapList.put("dataColumn", dataColumn);
    	    return mapList;
    }
    public  List<Map<String , Object>>  getMenuVisitDetail(Map<String , Object>  paramMap)
    {
    	    return loginLogDAO.getMenuVisitDetail(paramMap); 
    }
     
    
    public  List<Map<String , Object>>  getUserVisitCount(Map<String , Object>  paramMap)
    {
    	    return loginLogDAO.getUserVisitCount(paramMap);
    }
     
    public  List<Map<String , Object>>  getUserVisitDetail(Map<String , Object>  paramMap)
    {
    	    return loginLogDAO.getUserVisitDetail(paramMap);
    }
    public boolean deleteOaUser(String seq){
        try {
        	loginLogDAO.deleteOaUser(seq);
            return true;
        } catch (Exception e) {
            Log.error("删除OA推送用户失败，请稍后重试！", e);
            return false;
        }
    }
    public boolean deleteEwamUser(String seq){
        try {
        	loginLogDAO.deleteEwamUser(seq);
            return true;
        } catch (Exception e) {
            Log.error("删除OA推送用户失败，请稍后重试！", e);
            return false;
        }
    }
    public Object insertUser(Map<String,Object> queryData){
    	OprResult<Integer, Object> result = null;
    	//检查申请的营销中心是否存在
    	String zoneDimLevel=loginLogDAO.getDimLevelById(Convert.toString(queryData.get("zoneId")).trim());
    	if("5".equals(zoneDimLevel)||zoneDimLevel=="5"){
        List<Map<String, Object>> mapList = loginLogDAO.getDeptIdByZoneId(Convert.toString(queryData.get("zoneId")).trim());
        if (mapList.size() > 0) {//营销中心已存在
            result = new OprResult<Integer, Object>();
            result.setSid(-1);
            mapList = null;
            return result;
        }
    	}
        try {
        	result = new OprResult<Integer, Object>(null, loginLogDAO.insertUser(queryData), OprResult.OprResultType.insert);
            return result;
        } catch (Exception e) {
        	result.setSid(-2);
            Log.error("新增OA推送用户失败，请稍后重试！", e);
            return result;
        }
    }
    public Object insertEwamUser(Map<String,Object> queryData){
    	OprResult<Integer, Object> result = null;
    	//检查申请的营销中心是否存在
    	String zoneDimLevel=loginLogDAO.getDimLevelById(Convert.toString(queryData.get("zoneId")).trim());
    	if("5".equals(zoneDimLevel)||zoneDimLevel=="5"){
        List<Map<String, Object>> mapList = loginLogDAO.getEwamDeptIdByZoneId(Convert.toString(queryData.get("zoneId")).trim());
        if (mapList.size() > 0) {//营销中心已存在
            result = new OprResult<Integer, Object>();
            result.setSid(-1);
            mapList = null;
            return result;
        }
    	}
        try {
        	result = new OprResult<Integer, Object>(null, loginLogDAO.insertEwamUser(queryData), OprResult.OprResultType.insert);
            return result;
        } catch (Exception e) {
        	result.setSid(-2);
            Log.error("新增OA推送用户失败，请稍后重试！", e);
            return result;
        }
    }
    public Object editUser(Map<String,Object> queryData){
    	OprResult<Integer, Object> result = null;
    	//检查申请的营销中心是否存在
    	String zoneDimLevel=loginLogDAO.getDimLevelById(Convert.toString(queryData.get("zoneId")).trim());
    	if("5".equals(zoneDimLevel)||zoneDimLevel=="5"){
        List<Map<String, Object>> mapList = loginLogDAO.getDeptIdByZoneId(Convert.toString(queryData.get("zoneId")).trim());
        if (mapList.size() >1) {//营销中心已存在
            result = new OprResult<Integer, Object>();
            result.setSid(-1);
            mapList = null;
            return result;
        }
    	}
        try {
        	result = new OprResult<Integer, Object>(null, loginLogDAO.updateUser(queryData), OprResult.OprResultType.update);
            return result;
        } catch (Exception e) {
        	result.setSid(-2);
            Log.error("修改OA推送用户失败，请稍后重试！", e);
            return result;
        }
    }
    public Object editEwamUser(Map<String,Object> queryData){
    	OprResult<Integer, Object> result = null;
    	//检查申请的营销中心是否存在
    	String zoneDimLevel=loginLogDAO.getDimLevelById(Convert.toString(queryData.get("zoneId")).trim());
    	if("5".equals(zoneDimLevel)||zoneDimLevel=="5"){
        List<Map<String, Object>> mapList = loginLogDAO.getEwamDeptIdByZoneId(Convert.toString(queryData.get("zoneId")).trim());
        if (mapList.size() >1) {//营销中心已存在
            result = new OprResult<Integer, Object>();
            result.setSid(-1);
            mapList = null;
            return result;
        }
    	}
        try {
        	result = new OprResult<Integer, Object>(null, loginLogDAO.updateEwamUser(queryData), OprResult.OprResultType.update);
            return result;
        } catch (Exception e) {
        	result.setSid(-2);
            Log.error("修改OA推送用户失败，请稍后重试！", e);
            return result;
        }
    }
    public  Map<String, Object>  getUserById(String seq)
	{      
    	    return loginLogDAO.getUserById(seq);
    }
    public  Map<String, Object>  getEwamUserById(String seq)
	{      
    	    return loginLogDAO.getEwamUserById(seq);
    }
    public  Map<String, Object>  getDimLevelById(String zoneId)
	{      
    	    return loginLogDAO.getDimLevelByZoneId(zoneId);
    }
    /**
     *  add  @author yanhaidong   end
     */
}
