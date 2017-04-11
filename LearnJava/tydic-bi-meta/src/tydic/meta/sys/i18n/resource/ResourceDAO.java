package tydic.meta.sys.i18n.resource;

import tydic.meta.common.MetaBaseDAO;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author: 程钰
 * @description   本地化资源管理数据库操作类
 * @date: 12-3-31
 * @time: 上午11:31
 */

public class ResourceDAO extends MetaBaseDAO{
    /**
     * 获取所以的本地化资源
     * @return
     */
    public ResourcePO[] queryAllResource(){
        String sql = "SELECT B.RESOURCE_ID,B.MENU_ID,B.RESOURCE_NAME, " +
                "B.RESOURCE_PATH,B.RESOUCE_CODE,C.MENU_NAME FROM META_SYS_I18N_RESOURCE B " +
                "LEFT JOIN META_MAG_MENU C ON B.MENU_ID=C.MENU_ID " +
                " ORDER BY B.RESOURCE_ID ASC ,C.ORDER_ID ASC ";
        return getDataAccess().queryForBeanArray(sql,ResourcePO.class);
    }
}
