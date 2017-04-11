package tydic.meta.module.mag.login;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContextFactory;

import tydic.frame.SystemVariable;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.Common;
import tydic.meta.common.Constant;
import tydic.meta.module.mag.dept.DeptDAO;
import tydic.meta.module.mag.group.GroupDAO;
import tydic.meta.module.mag.menu.MenuCommon;
import tydic.meta.module.mag.menu.MenuDAO;
import tydic.meta.module.mag.station.StationDAO;
import tydic.meta.module.mag.user.UserConstant;
import tydic.meta.module.mag.user.UserDAO;
import tydic.meta.module.mag.zone.ZoneDAO;
import tydic.meta.web.session.SessionContext;
import tydic.meta.web.session.SessionManager;
import tydic.meta.web.session.User;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 登录控制Action <br>
 * @date 2011-09-22
 */
public class LoginAction {

    /**
     * 用户DAO
     */
    private UserDAO userDAO;

    private MenuDAO menuDAO;

    private GroupDAO groupDAO;

    private ZoneDAO zoneDAO;

    private DeptDAO deptDAO;

    private StationDAO stationDAO;

    /**
     * 登录类型与登录控制类之间的映射
     */
    private final static Map<String, ILoginType> LOGIN_TYPE_MAP = new HashMap<String, ILoginType>();

    /**
     * 在此初始化登录控制类与登录类型之间的关系
     */
    public LoginAction() {
        // 读取登陆类型与实现类的关系，并实例化实现类。
        Properties properties = SystemVariable.getProperties();
        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            if (key.startsWith("loginType")) { // 定义的登录类型，读取其具体类型与实现类。
                String[] splits = key.split("\\.");
                if (splits.length == 2) {
                    String implClass = properties.getProperty(key);
                    try {
                        LOGIN_TYPE_MAP.put(splits[1], (ILoginType) Class.forName(implClass).newInstance());
                    } catch (Exception e) {
                        Log.error("初始化指定登录类型[" + key + "]失败，请确认是否配置正确！", e);
                    }
                }
            }
        }
    }

    /**
     * 登录成功之后初始化session信息
     *
     * @param userData
     */
    private void initSession(Map<String, Object> userData, String sysId) {
        // 记录用户信息
        Map<String, Object> formatUser = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : userData.entrySet()) {
            formatUser.put(Common.tranColumnToJavaName(entry.getKey()), entry.getValue());
        }
        SessionManager.setAttribute(LoginConstant.SESSION_KEY_USER, formatUser);
        // //加载部门、岗位、地域信息
        if (userData.get("ZONE_ID") != null) {
            Map<String, Object> zoneInfo = zoneDAO.queryZoneInfo(Integer.parseInt(userData.get("ZONE_ID").toString()));
            Map<String, Object> formatZone = new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : zoneInfo.entrySet()) {
                formatZone.put(Common.tranColumnToJavaName(entry.getKey()), entry.getValue());
            }
            SessionManager.setAttribute(LoginConstant.SESSION_META_ZONE_INFO, formatZone);
        }
        if (userData.get("DEPT_ID") != null) {
            Map<String, Object> deptInfo = deptDAO.queryDeptInfo(Integer.parseInt(userData.get("DEPT_ID").toString()));
            Map<String, Object> formatDept = new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : deptInfo.entrySet()) {
                formatDept.put(Common.tranColumnToJavaName(entry.getKey()), entry.getValue());
            }
            SessionManager.setAttribute(LoginConstant.SESSION_META_DEPT_INFO, formatDept);
        }
        if (userData.get("STATION_ID") != null) {
            Map<String, Object> stationInfo = stationDAO.queryStationInfo(Integer.parseInt(userData.get("STATION_ID").toString()));
            Map<String, Object> formatStation = new HashMap<String, Object>();
            for (Map.Entry<String, Object> entry : stationInfo.entrySet()) {
                formatStation.put(Common.tranColumnToJavaName(entry.getKey()), entry.getValue());
            }
            SessionManager.setAttribute(LoginConstant.SESSION_META_STATION_INFO, formatStation);
        }
        if (StringUtils.isNotEmpty(sysId)) {
            changeSystem(Integer.parseInt(sysId));
        } else {
            changeSystem(null);
        }
    }

    /**
     * 切换系统，做一些切换系统的一些操作，比如变更session中的键值。
     */
    public void changeSystem(Integer sysId) {
        User user = SessionManager.getUser();
        // 根据用户groupId获取其系统信息。
        if (sysId == null) {
            sysId = user.getDefaultGroupID();
        }
        sysId = sysId == null ? Constant.DEFAULT_META_SYSTEM_ID : sysId;
        // 查询并获取系统信息。
        Map<String, Object> sysInfo = groupDAO.queryGroupById(sysId);
        Map<String, Object> formatSysInfo = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : sysInfo.entrySet()) {
            formatSysInfo.put(Common.tranColumnToJavaName(entry.getKey()), entry.getValue());
        }
        SessionManager.setAttribute(LoginConstant.SESSION_META_SYSTEM_INFO, formatSysInfo);
    }

    /**
     * 登录控制
     *
     * @param loginData
     * @param type      登录类型。根据loginType判断是那种登录类型，此类型为null时，默认是正常登录，即登录原系统 另可能会有OA登录等
     * @return
     */
    public ILoginType.LoginResult login(Map<String, Object> loginData, String type) {

        type = (type == null || type.equals("")) ? "meta" : type;
        // 获取实际控制登录类
        ILoginType loginType = LOGIN_TYPE_MAP.get(type);
        //loginType.getClass().getName();
        if (loginType == null) {
            Log.error("未注册登录类型");
            return null;
        }
        // 设置DAO
        loginType.setLoginDAO(userDAO);
        ILoginType.LoginResult loginResult = loginType.login(loginData);
        // 设置session
        if (loginResult == ILoginType.LoginResult.SUCCESS || loginResult == ILoginType.LoginResult.USER_FIRST_LOGIN
                || loginResult == ILoginType.LoginResult.USER_FORCE_MODIFY_PASS || loginResult == ILoginType.LoginResult.USER_TIP_MODIFY_PASS) {
            initSession(loginType.getUserData(), MapUtils.getString(loginData, "systemId"));
        }
        return loginResult;
    }

    /**
     * 访问用户root级菜单。
     *
     * @param systemId
     * @return
     */
    public List<Map<String, Object>> queryRootMenu(Integer systemId) {
        systemId = systemId == null ? Constant.DEFAULT_META_SYSTEM_ID : systemId;
        HttpSession session = WebContextFactory.get().getSession();
        Map<String, Object> userData = (Map<String, Object>) session.getAttribute(LoginConstant.SESSION_KEY_USER);
        int adminFlag = userData.get("adminFlag") == null ? Constant.META_DISABLE : Integer.parseInt(userData.get("adminFlag").toString());
        int userId = ((BigDecimal) (userData.get("userId"))).intValue();
        if (adminFlag == 1) {
            userId = UserConstant.ADMIN_USERID;
        }
        return MenuCommon.filterMenu(menuDAO.queryRootMenu(systemId, userId));
    }

    /**
     * 用户退出系统
     *
     * @return
     */
    public boolean logout() {
        HttpSession session = WebContextFactory.get().getSession();
        session.removeAttribute(LoginConstant.SESSION_KEY_USER);
        session.removeAttribute(LoginConstant.SESSION_META_ZONE_INFO);
        session.removeAttribute(LoginConstant.SESSION_META_DEPT_INFO);
        session.removeAttribute(LoginConstant.SESSION_META_STATION_INFO);
        SessionContext.removeSession(session.getId());
        return true;
    }

    /**
     * 加载所有的子菜单数据，有权限过滤。
     *
     * @param menuId
     * @return
     */
    public List<Map<String, Object>> queryAllSubMenu(int menuId) {
        HttpSession session = WebContextFactory.get().getSession();
        Map<String, Object> userData = (Map<String, Object>) session.getAttribute(LoginConstant.SESSION_KEY_USER);
        int adminFlag = userData.get("adminFlag") == null ? Constant.META_DISABLE : Integer.parseInt(userData.get("adminFlag").toString());
        int userId = ((BigDecimal) (userData.get("userId"))).intValue();
        if (adminFlag == 1) {
            userId = UserConstant.ADMIN_USERID;
        }
        return MenuCommon.filterMenu(menuDAO.queryAllSubMenu(menuId, userId));
    }
    public List<Map<String, Object>> findChild(int menuId) {
    	return userDAO.findChild(menuId);
    }
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setMenuDAO(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }

    public void setGroupDAO(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public void setZoneDAO(ZoneDAO zoneDAO) {
        this.zoneDAO = zoneDAO;
    }

    public void setDeptDAO(DeptDAO deptDAO) {
        this.deptDAO = deptDAO;
    }

    public void setStationDAO(StationDAO stationDAO) {
        this.stationDAO = stationDAO;
    }
}
