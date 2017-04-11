package tydic.meta.module.mag.login;


import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.meta.module.mag.user.UserConstant;
import tydic.meta.module.mag.user.UserDAO;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * 
 * @author 熊小平
 * @date 2011-10-22
 * @description 新OA登录实现类
 * 
 */
public class LoginOAImpl extends LoginCommonImpl {
    public LoginResult login(Map<String, Object> loginMessage) {
        String newOaId=loginMessage.get("uid")+"";
        if (newOaId != null) {
            List<Map<String,Object>> users = super.getLoginDAO().queryUserById(Integer.parseInt(newOaId));
            return super.afterLoginVaildate(users);
        } else {
            Log.warn("根据新OA用户名查询用户："+newOaId+"为空");
            return null;
        }
    }
}
