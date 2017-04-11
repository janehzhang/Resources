package tydic.meta.sys.i18n.item;

import tydic.frame.jdbc.Column;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author: 程钰
 * @description 本地化文字信息PO
 * @date: 12-3-31
 * @time: 上午10:29
 */

public class ItemPO {
    @Column("I18N_ITEM_ID")
    private int i18nItemId;
    @Column("I18N_ITEM_CODE")
    private String i18nItemCode;
    @Column("MAX_LENGTH")
    private int maxLength;
    @Column("VAL_TEXT")
    private String valText;
    @Column("MENU_ID")
    private int menuId;
    @Column("MENU_NAME")
    private String menuName;

    public int getI18nItemId() {
        return i18nItemId;
    }

    public void setI18nItemId(int i18nItemId) {
        this.i18nItemId = i18nItemId;
    }

    public String getI18nItemCode() {
        return i18nItemCode;
    }

    public void setI18nItemCode(String i18nItemCode) {
        this.i18nItemCode = i18nItemCode;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getValText() {
        return valText;
    }

    public void setValText(String valText) {
        this.valText = valText;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
