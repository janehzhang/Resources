package tydic.meta.sys.i18n.resource;

import tydic.frame.jdbc.Column;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author: 程钰
 * @description
 * @date: 12-3-31
 * @time: 上午11:31
 */

public class ResourcePO {
    @Column("MENU_ID")
    private int menuId;
    @Column("RESOURCE_ID")
    private int resourceId;
    @Column("RESOURCE_NAME")
    private String resourceName;
    @Column("RESOURCE_PATH")
    private String resourcePath;
    @Column("RESOUCE_CODE")
    private String resouceCode;
    @Column("MENU_NAME")
    private String menuName;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getResouceCode() {
        return resouceCode;
    }

    public void setResouceCode(String resouceCode) {
        this.resouceCode = resouceCode;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
