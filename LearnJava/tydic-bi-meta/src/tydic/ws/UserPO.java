package tydic.ws;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 用户实体PO对象 <br>
 * @date 2012-03-19
 *
 */
public class UserPO {

    private boolean isAdmin;
    private String userEmail;
    private long userId;
    private String userNameCn;
    private String userNameEn;
    private String userPass;
    private int systemId;
    private int userState;
    //用户地域信息
    private ZonePO zonePO;

    public boolean isAdmin(){
        return isAdmin;
    }

    public void setAdmin(boolean admin){
        isAdmin = admin;
    }

    public int getUserState(){
        return userState;
    }

    public void setUserState(int userState){
        this.userState = userState;
    }

    public String getUserPass(){
        return userPass;
    }

    public void setUserPass(String userPass){
        this.userPass = userPass;
    }

    public String getUserNameCn(){
        return userNameCn;
    }

    public void setUserNameCn(String userNameCn){
        this.userNameCn = userNameCn;
    }

    public String getUserNameEn(){
        return userNameEn;
    }

    public void setUserNameEn(String userNameEn){
        this.userNameEn = userNameEn;
    }

    public long getUserId(){
        return userId;
    }

    public void setUserId(long userId){
        this.userId = userId;
    }

    public String getUserEmail(){
        return userEmail;
    }

    public void setUserEmail(String userEmail){
        this.userEmail = userEmail;
    }

    public int getSystemId(){
        return systemId;
    }

    public void setSystemId(int systemId){
        this.systemId = systemId;
    }

    public ZonePO getZonePO() {
        return zonePO;
    }

    public void setZonePO(ZonePO zonePO) {
        this.zonePO = zonePO;
    }
}