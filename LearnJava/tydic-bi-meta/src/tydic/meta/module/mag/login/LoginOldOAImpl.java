package tydic.meta.module.mag.login;

import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.meta.module.mag.user.UserConstant;
import tydic.meta.module.mag.user.UserDAO;

/**
 * 老OA登录实现类（待实现） Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights
 * reserved.<br>
 * 
 * @author 熊小平
 * @date 2011-10-22
 * @description
 * 
 */
public class LoginOldOAImpl extends LoginCommonImpl {
    public LoginResult login(Map<String, Object> loginMessage) {
        String namecn = (String) loginMessage.get("oldPortal");
        String src = (String) loginMessage.get("src");
        if (namecn != null && src.trim().equals("oa")) {
            List<Map<String, Object>> users = super.getLoginDAO().queryUserByNamecn(namecn);
            return super.afterLoginVaildate(users);
        } else {
            Log.warn("根据用户中文名查询用户："+namecn+"为空");
            return null;
        }
    }
}
