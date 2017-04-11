package tydic.ws;

import java.util.List;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 菜单PO对象 <br>
 * @date 2012-03-19
 *
 */
public class MenuPO {

    private String iconUrl;
    private boolean isShow;
    private long menuId;
    private String menuName;
    private String menuTip;
    private String menuUrl;
    private int orderId;
    private List<Button> pageButtons;
    private long parentId;

    public String getIconUrl(){
        return iconUrl;
    }

    public void setIconUrl(String iconUrl){
        this.iconUrl = iconUrl;
    }

    public long getParentId(){
        return parentId;
    }

    public void setParentId(long parentId){
        this.parentId = parentId;
    }

    public List<Button> getPageButtons(){
        return pageButtons;
    }

    public void setPageButtons(List<Button> pageButtons){
        this.pageButtons = pageButtons;
    }

    public String getMenuUrl(){
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl){
        this.menuUrl = menuUrl;
    }

    public String getMenuName(){
        return menuName;
    }

    public void setMenuName(String menuName){
        this.menuName = menuName;
    }

    public long getMenuId(){
        return menuId;
    }

    public void setMenuId(long menuId){
        this.menuId = menuId;
    }

    public boolean isShow(){
        return isShow;
    }

    public void setShow(boolean show){
        isShow = show;
    }

    public int getOrderId(){
        return orderId;
    }

    public void setOrderId(int orderId){
        this.orderId = orderId;
    }

    public String getMenuTip(){
        return menuTip;
    }

    public void setMenuTip(String menuTip){
        this.menuTip = menuTip;
    }
}