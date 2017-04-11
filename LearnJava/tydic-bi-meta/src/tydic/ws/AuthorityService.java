package tydic.ws;

import org.apache.commons.collections.map.HashedMap;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.Constant;
import tydic.meta.module.mag.dept.DeptDAO;
import tydic.meta.module.mag.login.ILoginType;
import tydic.meta.module.mag.login.LoginCommonImpl;
import tydic.meta.module.mag.login.LoginWsImpl;
import tydic.meta.module.mag.menu.MenuConstant;
import tydic.meta.module.mag.menu.MenuDAO;
import tydic.meta.module.mag.user.UserConstant;
import tydic.meta.module.mag.user.UserDAO;
import tydic.meta.module.mag.zone.ZoneDAO;
import tydic.ws.Button;
import tydic.ws.MenusPO;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description WebService服务端权限管理接口 <br>
 * @date 2012-03-19
 *
 */
@WebService
@SOAPBinding(style =  SOAPBinding.Style.RPC)
public class AuthorityService{

    private final static Map<ILoginType.LoginResult,Integer> RS_MAPPING=new HashedMap();
    //初始化映射数据
    static {
        RS_MAPPING.put(ILoginType.LoginResult.SUCCESS,1 );
        RS_MAPPING.put(ILoginType.LoginResult.ERROR_DISABLED,2);
        RS_MAPPING.put(ILoginType.LoginResult.ERROR_LOCKING,3);
        RS_MAPPING.put(ILoginType.LoginResult.ERROR_AUDITING,4);
        RS_MAPPING.put(ILoginType.LoginResult.ERROR_USER_PASSWD,5);
        RS_MAPPING.put(ILoginType.LoginResult.ERROR_NAME_REPEAT,6);
        RS_MAPPING.put(ILoginType.LoginResult.ERROR_GROUP_DISENBLE,7);
    }

    public AuthorityService(){

    }

    /**
     * 根据用户ID和菜单ID获取此用户对此菜单无权限的按钮集合
     * @param userID 用户Id
     * @param menuId  菜单ID
     * @return
     */
    public List<Button> getExcludeButtons(long userID,long menuId){
        MenuDAO menuDAO=new MenuDAO();
        List<String> buttons=menuDAO.excludeButton(menuId,userID);
        List<Button> rsButtons=new ArrayList<Button>();
        for(String button:buttons){
            Button newButton=new Button();
            newButton.setButtonId(button);
            rsButtons.add(newButton);
        }
        menuDAO.close();
        return rsButtons;
    }

    /**
     * 获取此用户拥有的所有菜单
     * @param userID
     * @param sysId 系统ID
     */
    public MenusPO getMenusByUserId(long userID,int sysId){
        MenuDAO menuDAO=new MenuDAO();
        List<Map<String,Object>> menus=menuDAO.queryAllMenu(Convert.toInt(userID),sysId);
        List<MenuPO> menuPOs=new ArrayList<MenuPO>();
        Map<Integer,Integer> removeMenuIds=new HashMap<Integer, Integer>();
        if(menus != null && menus.size() > 0){
            for(int i = 0; i < menus.size(); i++){
                Map<String, Object> menu = menus.get(i);
                if(menu.get("ROLE_FLAG")!=null && !menu.get("ROLE_FLAG").equals("")&&
                        Integer.parseInt(menu.get("ROLE_FLAG").toString()) == MenuConstant.ROLE_FLAG_DELETE_MENU){
                    Integer menuId=Integer.parseInt(menu.get("MENU_ID").toString());
                    removeMenuIds.put(menuId,menuId);
                }
            }
        }
        //再次遍历列表，删除某些需要删除的MenuId
//        if(removeMenuIds.size()>0){
        for(int i=0;i<menus.size();i++){
            Map<String, Object> menu = menus.get(i);
            Integer menuId=Integer.parseInt(menu.get("MENU_ID").toString());
            if(removeMenuIds.containsKey(menuId)){
                menus.remove(i--);
            }else{
                //转换为对应的Bean
                MenuPO menuPO=new MenuPO();
                menuPO.setMenuId(MapUtils.getLong(menu,"MENU_ID"));
                menuPO.setIconUrl(MapUtils.getString(menu,"MENU_URL"));
                menuPO.setMenuName(MapUtils.getString(menu,"MENU_NAME"));
                menuPO.setMenuTip(MapUtils.getString(menu,"MENU_TIP"));
                menuPO.setMenuUrl(MapUtils.getString(menu,"MENU_URL"));
                menuPO.setOrderId(MapUtils.getIntValue(menu,"ORDER_ID",0));
                menuPO.setParentId(MapUtils.getLong(menu,"PARENT_ID"));
                menuPO.setShow(Constant.META_ENABLE==MapUtils.getIntValue(menu,"IS_SHOW"));
                //页面按钮处理
                String pageButton=MapUtils.getString(menu,"PAGE_BUTTON");
                if(!StringUtils.isEmpty(pageButton)){
                    String split1[]=StringUtils.split(pageButton,",");
                    List<Button> pageButon=new ArrayList<Button>();
                    for(String tempSplit:split1){
                        String split2[]=tempSplit.split(":");
                        Button button=new Button();
                        if(split2!=null&&split2.length>0){
                            button.setButtonId(split2[0]);
                        }
                        if(split2!=null&&split2.length>1){
                            button.setButtonDesc(split2[1]);
                        }
                        pageButon.add(button);
                    }
                    menuPO.setPageButtons(pageButon);
                    menuPOs.add(menuPO);
                }
            }
        }
        //整理menusPO
        MenusPO menusPO=new MenusPO();
        menusPO.setUserId(userID);
        menusPO.setMenus(menuPOs);
        menuDAO.close();
        return menusPO;
    }

    /**
     * 根据用户名查询用户所拥有的菜单
     * @param userName 用户名，可以为用户中文名或者用户emai
     * @return
     */
    public MenusPO getMenusByUserName(String userName){
        //首先查询用户信息
        UserPO userPO=getUserInfo(userName);
        return getMenusByUserId(userPO.getUserId(),userPO.getSystemId());
    }
    //Email pattern
    private Pattern pattern = Pattern.compile("\\w+@\\w+(\\.\\w+)+");
    //电话号码 parttern
    private  Pattern ptel = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    /**
     * 根据用户中文名/用户Email/电话号码查询用户信息。
     * @param userName
     */
    public UserPO getUserInfo(String userName){
        UserDAO userDAO=new UserDAO();
        List<Map<String,Object>> userInfos=null;
        Matcher matcher = pattern.matcher(userName);
        Matcher matchertel = ptel.matcher(userName);
        if(matcher.matches()){//匹配用Email登录
            userInfos = userDAO.queryUserByEmail(userName);
        }else if(matchertel.matches()){//匹配手机
            userInfos = userDAO.queryUserByTel(userName);
        } else{//尝试用中文名
            userInfos = userDAO.queryUserByNamecn(userName);
        }
        //返回第一个
        UserPO userPO=new UserPO();
        if(userInfos!=null&&userInfos.size()>0){
            Map<String,Object> userdata=userInfos.get(0);
            userPO.setAdmin(UserConstant.ADMIN_FLAG_IS_ADMIN == MapUtils.getIntValue(userdata, "ADMIN_FLAG"));
            userPO.setUserEmail(MapUtils.getString(userdata, "USER_EMAIL"));
            userPO.setUserId(MapUtils.getIntValue(userdata, "USER_ID"));
            userPO.setUserNameCn(MapUtils.getString(userdata,"USER_NAMECN"));
            userPO.setUserNameEn(MapUtils.getString(userdata,"USER_NAMEEN"));
            userPO.setUserPass(MapUtils.getString(userdata,"USER_PASS"));
            userPO.setUserState(MapUtils.getIntValue(userdata,"STATE"));
            userPO.setSystemId(MapUtils.getIntValue(userdata,"GROUP_ID"));
            userPO.setZonePO(getZone(MapUtils.getInteger(userdata,"ZONE_ID")));
        }else{
            userPO=null;
        }
        userDAO.close();
        return userPO;
    }

    /**
     * 判断用户是否存在指定菜单的权限
     * @param userID 用户ID
     * @param menuID  菜单ID
     */
    public boolean isUserExistsMenu(long userID, long menuID){
        MenuDAO menuDAO=new MenuDAO();
        UserDAO userDAO=new UserDAO();
        //首先判断用户是否是管理员，管理员拥有所有的菜单权限
        Map<String,Object> userInfo=userDAO.queryUserByUserId(Convert.toInt(userID));
        boolean rs=false;
        if(UserConstant.ADMIN_FLAG_IS_ADMIN == MapUtils.getIntValue(userInfo, "ADMIN_FLAG")){
            rs=true;
        }else{
            rs=menuDAO.isUserExistsMenu(menuID,userID);
        }
        menuDAO.close();
        userDAO.close();
        return rs;
    }

    /**
     * 登录操作
     * @param password 此密码为未加密的原始密码，为用户输入的密码
     * @param userName 用户名，可以为用户中文名或者用户emai
     */
    public LoginResult login(String userName, String password){
        //进行登录信息封装
        Map<String,Object>  loginMessage=new HashedMap();
        loginMessage.put("loginId",userName);
        loginMessage.put("password",password);
        UserDAO userDAO=new UserDAO();
        //实例化进行验证的类
        ILoginType loginType=new LoginWsImpl();
        loginType.setLoginDAO(userDAO);
        ILoginType.LoginResult rs=loginType.login(loginMessage);
        //初始化webService的LoginResult
        LoginResult result=new LoginResult();
        result.setResult(RS_MAPPING.get(rs));
        if(rs==ILoginType.LoginResult.SUCCESS){//登录成功设置用户信息
            UserPO userPO=new UserPO();
            LoginCommonImpl common=(LoginCommonImpl)loginType;
            Map<String,Object> userdata=common.getUserData();
            userPO.setAdmin(UserConstant.ADMIN_FLAG_IS_ADMIN == MapUtils.getIntValue(userdata, "ADMIN_FLAG"));
            userPO.setUserEmail(MapUtils.getString(userdata, "USER_EMAIL"));
            userPO.setUserId(MapUtils.getIntValue(userdata, "USER_ID"));
            userPO.setUserNameCn(MapUtils.getString(userdata,"USER_NAMECN"));
            userPO.setUserNameEn(MapUtils.getString(userdata,"USER_NAMEEN"));
            userPO.setUserPass(MapUtils.getString(userdata,"USER_PASS"));
            userPO.setUserState(MapUtils.getIntValue(userdata,"STATE"));
            result.setUserInfo(userPO);
        }
        userDAO.close();
        return result;
    }

   /* *//**
     * 查询用户的部门信息
     * @param deptId
     * @return
     *//*
    public DeptPO queryDept(long deptId){
        DeptDAO deptDAO=new DeptDAO();
        Map<String,Object> dept=deptDAO.queryDeptInfo(Convert.toInt(deptId));
        DeptPO deptPO=null;
        if(dept!=null){
            deptPO=new DeptPO();
            deptPO.setDeptId(MapUtils.getIntValue(dept,"DEPT_ID"));
            deptPO.setDeptParId(MapUtils.getIntValue(dept,"PARENT_ID"));
            deptPO.setDeptName(MapUtils.getString(dept,"DEPT_NAME"));
        }
        deptDAO.close();
        return deptPO;
    }
*/
    /**
     * 查询用户的部门信息
     * @param zoneId
     * @return
     */
    public ZonePO getZone(long zoneId){
        ZoneDAO zoneDAO=new ZoneDAO();
        Map<String,Object> zone=zoneDAO.queryZoneInfo(Convert.toInt(zoneId));
        ZonePO zonePO=null;
        if(zone!=null){
            zonePO=new ZonePO();
            zonePO.setZoneID(MapUtils.getIntValue(zone,"ZONE_ID"));
            zonePO.setZoneParId(MapUtils.getIntValue(zone,"ZONE_PAR_ID"));
            zonePO.setZoneName(MapUtils.getString(zone,"ZONE_NAME"));
            zonePO.setZoneDesc(MapUtils.getString(zone,"ZONE_DESC"));
            zonePO.setZoneCode(MapUtils.getString(zone,"ZONE_CODE"));
        }
        zoneDAO.close();
        return zonePO;
    }


}