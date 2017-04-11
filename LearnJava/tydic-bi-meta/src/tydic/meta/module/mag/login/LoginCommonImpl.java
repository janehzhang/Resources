package tydic.meta.module.mag.login;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.SystemVariable;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.meta.module.mag.user.UserConstant;
import tydic.meta.module.mag.user.UserDAO;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 常规登录实现类，该类实现了用户根据用户名和中文名称进行登录的方式 <br>
 *     进入此方法后只会做一些基本的判断，不会再做查询数据操作。
 * @date 2011-09-22
 *
 * @modify 熊久亮
 * @description 增加登陆验证码功能，修改验证码比较不区分大小写。
 * @date 2011-10-12
 */
public class LoginCommonImpl implements ILoginType{
    /**
     * UserDao 操作类
     */
    private UserDAO loginDAO;
    public void setLoginDAO(BaseDAO loginDAO){
        this.loginDAO = (UserDAO) loginDAO;
    }
    public UserDAO getLoginDAO(){
        return this.loginDAO;
    }
    /**
     * 用户数据
     */
    private Map<String, Object> userData;

    /**
     * 用户登录待实现类
     * @param loginMessage
     * @return
     */
    public LoginResult login(Map<String, Object> loginMessage){
        return null;
    }

//        }

    /**
     * 查询数据后，做基本效验
     * @param loginMessage
     * @return
     */
    public LoginResult afterLoginVaildate(List<Map<String, Object>> loginMessage){
        if(loginMessage == null){
                return LoginResult.ERROR_USER_PASSWD;
        }
        if(loginMessage.size() == 0){
            return LoginResult.ERROR_USER_PASSWD;
        }
        if(loginMessage.size() >1){
            Log.warn("根据用户传入信息 匹配出多个用户！");
            return LoginResult.ERROR_NAME_REPEAT;
        }
        Map<String,Object>  userData  =  loginMessage.get(0);
        Object userState = userData.get("STATE");
        //检查系统状态
        Object groupState = userData.get("GROUP_STATE");
        if(groupState != null){
            switch (Integer.parseInt(groupState.toString())){
                case UserConstant.META_MENU_GROUP_STATE_DISENABLE:{
                    return LoginResult.ERROR_GROUP_DISENBLE;
                }
                default:
                    break;
            }
        }

        if(userState != null){
            switch(Integer.parseInt(userState.toString())){
                case UserConstant.META_MAG_USER_STATE_DISABLE:{
                    return LoginResult.ERROR_DISABLED;
                }
                case UserConstant.META_MAG_USER_STATE_AUDITING:{
                    return LoginResult.ERROR_AUDITING;
                }
                case UserConstant.META_MAG_USER_STATE_LOCK:{
                    return LoginResult.ERROR_LOCKING;
                }
                default:
                    break;
            }
        }
        this.userData = userData;
        //检查用后是否第一次登录  排除隐藏ID  和  超级管理员
        Object loginDate = userData.get("CHANGE_TIME");
//        if(userData.get("")){
//
//        }
        //获取 超级用户，取消禁用
        String adminUsers = SystemVariable.getString("adminUser","457,120,353331,353332,353333,201");
        String adminUser[] = adminUsers.contains(",")?adminUsers.split(","):new String []{adminUsers};
        Object userId = userData.get("USER_ID");
        for(int i=0 ; i<adminUser.length; i++){
            if(Convert.toInt(userId) == Convert.toInt(adminUser[i])){
                return LoginResult.SUCCESS;
            }
        }

        if(loginDate == null || "".equals(loginDate.toString().trim())){
            return LoginResult.USER_FIRST_LOGIN;
        }
        //判断用登陆时长并返回相应的结果
        String userForceModifyPassS = SystemVariable.getString("userForceModifyPassS", "90*24*60*60*1000");
        String userTipModifyPassS = SystemVariable.getString("userTipModifyPass", "80*24*60*60*1000");

        String userTipModifyPass[] = userTipModifyPassS.contains("*")?userTipModifyPassS.split("\\*"):new String[]{userTipModifyPassS};
        String userForceModifyPass[] = userForceModifyPassS.contains("*")?userForceModifyPassS.split("\\*"):new String[]{userForceModifyPassS};
        long timeTipPass = 1;
        long timeForcePass = 1;
        for (int i=0; i<userTipModifyPass.length; i++){
            timeTipPass = timeTipPass*Long.parseLong(userTipModifyPass[i]);
        }
        for (int i=0; i<userForceModifyPass.length; i++){
            timeForcePass = timeForcePass*Long.parseLong(userForceModifyPass[i]);
        }
        //得到提示修改密码的时间
        DateFormat simple = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date dateTip = new java.util.Date(System.currentTimeMillis()-timeTipPass);
        java.util.Date dateForce = new Date(System.currentTimeMillis()-timeForcePass);
        if(loginDate.toString().compareTo(simple.format(dateForce)) < 0){
            return LoginResult.USER_FORCE_MODIFY_PASS;
        }
        if (loginDate.toString().compareTo(simple.format(dateTip)) < 0){
            return LoginResult.USER_TIP_MODIFY_PASS;
        }
        return LoginResult.SUCCESS;
    }
    /**
     * 登录成功之后返回用户的数据
     * @return
     */
    public Map<String, Object> getUserData(){
        return this.userData;
    }
}
