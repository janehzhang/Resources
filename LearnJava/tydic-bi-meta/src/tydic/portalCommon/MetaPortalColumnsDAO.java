package tydic.portalCommon;

import tydic.meta.common.MetaBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:首页配置列信息读取表
 * @date 2012-03-30
 */
public class MetaPortalColumnsDAO extends MetaBaseDAO {
    /**
     * 根据标签页信息获取其对应的列配置
     *
     * @param tabId
     * @return
     */
    public List<Map<String, Object>> queryColumnsConfig(long tabId) {
        String sql = "SELECT COL_ID, COL_EN_NAME, COL_CHN_NAME, SHOW_ORDER_ID, TAB_ID,COLUMN_COMPANY,COLUMN_COMPANY_POS " +
                "FROM META_PORTAL_COLUMNS WHERE TAB_ID=? ORDER BY SHOW_ORDER_ID";
        return getDataAccess().queryForList(sql, tabId);
    }
    /**
     * @Title: getIndexCdWaringMes 
     * @Description: 获取预警信息
     * @param tabId
     * @param indexCd
     * @return List<Map<String,Object>>   
     * @throws
     */
    public List<Map<String,Object>> getIndexCdWaringMes(String tabId){
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("SELECT T.* FROM META_PORTAL_WARNING T WHERE T.TAB_ID = ?");
    	return getDataAccess().queryForList(buffer.toString(), new Object[]{tabId});
    }
    /**
     * @Title: getIndexCdByGroup 
     * @Description: 获取到indexCd
     * @param tabId
     * @return Object[][]   
     * @throws
     */
    public Object[][] getIndexCdByGroup(String tabId){
    	String sql = "SELECT W.INDEX_CD,W.TAB_ID FROM META_PORTAL_WARNING W WHERE W.TAB_ID = ? GROUP BY W.INDEX_CD,W.TAB_ID";
    	return getDataAccess().queryForArray(sql, false, new Object[]{tabId});
    }
}
