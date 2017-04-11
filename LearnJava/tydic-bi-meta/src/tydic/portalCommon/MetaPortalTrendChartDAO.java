package tydic.portalCommon;

import tydic.meta.common.MetaBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:指标趋势图配置表DAO
 * @date 2012-04-01
 */
public class MetaPortalTrendChartDAO extends MetaBaseDAO{
    /**
     * 根据标签页ID查询定义在此标签下所有的折线图定义
     * @param tabId
     * @return
     */
    public List<Map<String,Object>> queryTrendChartsByTabId(long tabId){
//        String sql="SELECT TAB_ID, INDEX_CD, BROKEN_LINE_NAME, BROKEN_LINE_DESC, BROKEN_LINE_COLOR, TIME_INTERVAL_ID,COLUMN_ID,IS_SHOW_COL " +
//                "FROM META_PORTAL_TREND_CHART WHERE TAB_ID=?";
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT C.*,CO.SHOW_ORDER_ID FROM META_PORTAL_TREND_CHART C,META_PORTAL_COLUMNS CO");
        buffer.append(" WHERE C.COLUMN_ID = CO.COL_ID(+) AND C.TAB_ID = ?");
        return getDataAccess().queryForList(buffer.toString(),tabId);
    }
    /**
     * @Title: getTrendChartByColIdAndTabId 
     * @Description: 根据指标code、tabId、colId判断是否该列有图形
     * @param indexCd 指标code
     * @param tabId 
     * @param showOrderId colId
     * @return Map<String,Object>   
     * @throws
     */
    public Map<String,Object> getTrendChartByColIdAndTabId(String indexCd,String tabId,String showOrderId){
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("SELECT C.*,CO.SHOW_ORDER_ID FROM META_PORTAL_TREND_CHART C,META_PORTAL_COLUMNS CO");
    	buffer.append(" WHERE C.COLUMN_ID = CO.COL_ID AND C.TAB_ID = ? AND C.IS_SHOW_COL = 0");
    	buffer.append(" AND CO.SHOW_ORDER_ID = ? AND C.INDEX_CD = ?");
    	Map<String,Object> map = getDataAccess().queryForMap(buffer.toString(), new Object[]{tabId,showOrderId,indexCd});
    	return map;
    }
}
