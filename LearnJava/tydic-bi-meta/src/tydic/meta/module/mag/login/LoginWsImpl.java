package tydic.meta.module.mag.login;

import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 外部web service登录 <br>
 * @date 2012-03-20
 */
public class LoginWsImpl extends LoginBiMetaImpl{
    /**
     * 返回null代表无先绝性验证。
     * @param loginMessage
     * @return
     */
    public LoginResult beginLogin(Map<String, Object> loginMessage){
        return null;
    }
}
