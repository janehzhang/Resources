package tydic.ws;

import tydic.meta.module.mag.login.ILoginType;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 登录结果PO对象 <br>
 * @date 2012-03-19
 *
 */
public class LoginResult {

    /**
     * 登录结果，分别有如下结果。
     *1:登录成功
     *2:登录失败，该用户已被禁用
     *3:登录失败，该用户已被锁定
     *4:登录失败，该用户正在被审核中
     *5:登录失败，用户名或者密码输入不正确
     *6:登录失败，该用户名中文名重复
     *7:登录无效，用户所属系统无效
     */
	private int result;
    /**
     * 登录后的用户信息
     */
	private UserPO userInfo;

    public int getResult(){
        return result;
    }

    public void setResult(int result){
        this.result = result;
    }

    public UserPO getUserInfo(){
        return userInfo;
    }

    public void setUserInfo(UserPO userInfo){
        this.userInfo = userInfo;
    }
}