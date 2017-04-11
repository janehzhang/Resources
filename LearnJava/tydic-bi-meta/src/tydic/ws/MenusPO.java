package tydic.ws;


import java.util.List;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 菜单PO集合对象 <br>
 * @date 2012-03-19
 *
 */
public class MenusPO {

    private List<MenuPO> menus;
    private long userId;

    public List<MenuPO> getMenus(){
        return menus;
    }

    public void setMenus(List<MenuPO> menus){
        this.menus = menus;
    }

    public long getUserId(){
        return userId;
    }

    public void setUserId(long userId){
        this.userId = userId;
    }
}