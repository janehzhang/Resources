package tydic.meta.sys.i18n.item;

import tydic.meta.common.MetaBaseDAO;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author: 程钰
 * @description  本地化文本文字操作类
 * @date: 12-3-31
 * @time: 上午10:30
 */

public class ItemDAO extends MetaBaseDAO{
    /**
     * 加载所以的本地化文字信息
     */
    public ItemPO[] queryAllItem(){
		String sql = "SELECT A.I18N_ITEM_ID,A.I18N_ITEM_CODE, " +
                "A.MAX_LENGTH,A.VAL_TEXT,A.MENU_ID,B.MENU_NAME FROM META_SYS_I18N_ITEM A LEFT  " +
                "JOIN META_MAG_MENU B ON A.MENU_ID=B.MENU_ID ORDER BY A.I18N_ITEM_ID ASC,B.ORDER_ID ASC ";
		return getDataAccess().queryForBeanArray(sql,ItemPO.class);
	}
}
