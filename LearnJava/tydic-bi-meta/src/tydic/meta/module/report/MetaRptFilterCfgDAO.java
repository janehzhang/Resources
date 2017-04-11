package tydic.meta.module.report;

import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.MetaBaseDAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:META_RPT_TAB_FILTER_CFG操作DAO
 * @date 2012-04-06
 */
public class MetaRptFilterCfgDAO extends MetaBaseDAO {

    /**
     * 批量添加报表过滤条件
     *
     * @param datas
     * @param reportId 报表ID
     * @return
     */
    public int[] insertRptTabFilter(final List<Map<String, Object>> datas, final long reportId) {
        String sql = "INSERT INTO META_RPT_TAB_FILTER_CFG(FILTER_ID,REPORT_ID,FILTER_OPERATOR,FILTER_TARGET,DIM_LEVELS," +
                "FILTER_ORDER,COL_ID,COLUMN_ID,DIM_TYPE_ID,DIM_TABLE_ID,FILTER_ATTR_CONFIG) " +
                " VALUES (SEQ_RPT_TAB_FILTER_CFG_ID.NEXTVAL,?,?,?,?,?, ?,?,?,?,?)";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter() {
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Map<String, Object> data = datas.get(i);
                preparedStatement.setObject(1, reportId);             //报表ID
                preparedStatement.setObject(2, MapUtils.getString(data, "filterOperator"));         //字段算符
                preparedStatement.setObject(3, MapUtils.getString(data, "filterTarget"));        //字段目标值
                preparedStatement.setObject(4, MapUtils.getString(data, "dimLevels"));           //维度层级
                preparedStatement.setObject(5, MapUtils.getIntValue(data, "filterOrder", 0));
                preparedStatement.setObject(6, MapUtils.getIntValue(data, "colId"));                 //字段ID
                preparedStatement.setObject(7, MapUtils.getIntValue(data, "columnId"));
                preparedStatement.setObject(8, MapUtils.getObject(data, "dimTypeId"));
                preparedStatement.setObject(9, MapUtils.getObject(data, "dimTableId"));
                preparedStatement.setObject(10, MapUtils.getObject(data, "filterAttrConfig"));
            }

            public int batchSize() {
                return datas.size();
            }
        });
    }

    /**
     * 根据报表ID删除报表的过滤设置
     *
     * @param reportId
     * @return
     */
    public boolean deleteByReportId(long reportId) {
        String sql = "DELETE FROM META_RPT_TAB_FILTER_CFG WHERE REPORT_ID=?";
        return getDataAccess().execNoQuerySql(sql, reportId);
    }

    /**
     * 查询报表的过滤信息
     *
     * @param reportId
     * @return
     */
    public List<Map<String, Object>> queryFilterInfoByReportId(long reportId) {
        String sql = "SELECT FILTER_ID, REPORT_ID, FILTER_OPERATOR, FILTER_TARGET, DIM_LEVELS, " +
                "FILTER_ORDER, COL_ID, COLUMN_ID, DIM_TYPE_ID, DIM_TABLE_ID,FILTER_ATTR_CONFIG FROM META_RPT_TAB_FILTER_CFG WHERE REPORT_ID=?";
        return getDataAccess().queryForList(sql, reportId);
    }

}
