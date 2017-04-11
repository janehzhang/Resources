package tydic.meta.module.mag.login;

import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.meta.module.mag.user.UserConstant;
import tydic.meta.module.mag.user.UserDAO;
/**
 * 
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 熊小平
 * @date 2011-10-24
 * @description 
 *
 */
public class LoginPortalImpl extends LoginCommonImpl{
    public LoginResult login(Map<String, Object> loginMessage) {
    	String userName = (String) loginMessage.get("oldPortal");
    	if (userName != null) {
            List<Map<String, Object>> users = super.getLoginDAO().queryUserByNamecn(userName);
            return super.afterLoginVaildate(users);
        } else {
            Log.warn("用户名"+userName+"为空");
            return null;
        }
    }
}
