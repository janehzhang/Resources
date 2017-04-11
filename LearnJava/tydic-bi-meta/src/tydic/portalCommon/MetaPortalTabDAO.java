package tydic.portalCommon;

import tydic.meta.common.MetaBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用：表MetaPortalTabDAO
 * @date 2012-03-30
 */
public class MetaPortalTabDAO extends MetaBaseDAO {
    /**
     * 查询所有的标签页
     *
     * @return
     */
    public List<Map<String, Object>> queryAllTabs() {
        String sql = "SELECT TAB_ID, TAB_NAME, TAB_DESC, ORDER_ID, ROLLDOWN_LAYER, DEFAULT_GRID,RPT_TYPE," +
                " MENU_ID FROM META_PORTAL_TAB ORDER BY ORDER_ID";
        return getDataAccess().queryForList(sql);
    }

}
