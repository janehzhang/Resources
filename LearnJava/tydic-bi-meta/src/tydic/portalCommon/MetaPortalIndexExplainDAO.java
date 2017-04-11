package tydic.portalCommon;

import tydic.meta.common.MetaBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:指标解释DAO
 * @date 2012-03-30
 */
public class MetaPortalIndexExplainDAO extends MetaBaseDAO {
    /**
     * 根据标签页ID获取对应的指标解释
     *
     * @param tabId
     * @return
     */
    public List<Map<String, Object>> queryExplain(long tabId) {
        String sql = "SELECT INDEX_CD, TAB_ID, INDEX_NAME, INDEX_EXPLAIN,TIME_INTERVAL_ID" +
                " FROM META_PORTAL_INDEX_EXPLAIN WHERE TAB_ID=?";
        return getDataAccess().queryForList(sql, tabId);
    }

}
