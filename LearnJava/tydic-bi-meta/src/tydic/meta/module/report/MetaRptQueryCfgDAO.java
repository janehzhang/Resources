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
 * @description 作用:表META_RPT_TAB_QUERY_CFG操作DAO
 * @date 2012-04-06
 */
public class MetaRptQueryCfgDAO extends MetaBaseDAO {


    /**
     * 批量添加报表查询条件
     *
     * @param datas
     * @param reportId 报表ID
     * @return
     */
    public int[] insertRptTabQuery(final List<Map<String, Object>> datas, final long reportId) {
        String sql = "INSERT INTO META_RPT_TAB_QUERY_CFG(QUERY_ID,QUERY_ORDER,QUERY_CONTROL, QUERY_DEFAULT,DIM_LEVELS,DIM_CODE_FILTER," +
                "REPORT_ID,COL_ID,COLUMN_ID,DIM_TYPE_ID,DIM_TABLE_ID,LABEL_NAME) " +
                " VALUES (SEQ_RPT_TAB_QUERY_CFG_ID.NEXTVAL,?,?,?,?,? ,?,?,?,?,?, ?)";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter() {
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Map<String, Object> data = datas.get(i);
                preparedStatement.setObject(1, MapUtils.getIntValue(data, "queryOrder", 0));             //序号
                preparedStatement.setObject(2, MapUtils.getIntValue(data, "queryControl"));         //控件类型
                preparedStatement.setObject(3, MapUtils.getObject(data, "queryDefault"));        //默认值
                preparedStatement.setObject(4, MapUtils.getObject(data, "dimLevels"));           //维度层级
                preparedStatement.setObject(5, MapUtils.getObject(data, "dimCodeFilter"));         //维度编码过滤
                preparedStatement.setObject(6, reportId);             //报表ID
                preparedStatement.setObject(7, MapUtils.getIntValue(data, "colId"));                 //字段ID
                preparedStatement.setObject(8, MapUtils.getIntValue(data, "columnId"));
                preparedStatement.setObject(9, MapUtils.getObject(data, "dimTypeId")); //归并类型ID
                preparedStatement.setObject(10, MapUtils.getObject(data, "dimTableId"));//维度表类ID
                preparedStatement.setObject(11, MapUtils.getObject(data, "labelName"));//显示名称
            }

            public int batchSize() {
                return datas.size();
            }
        });
    }

    /**
     * 根据报表ID删除对应的查询信息
     *
     * @param reportId
     * @return
     */
    public boolean deleteByReportId(long reportId) {
        String sql = "DELETE FROM META_RPT_TAB_QUERY_CFG WHERE REPORT_ID=?";
        return getDataAccess().execNoQuerySql(sql, reportId);
    }

    /**
     * 查询报表查询条件信息
     * @param reportId
     * @return
     */
    public List<Map<String,Object>> queryRptQueryInfoByReportId(long reportId){
        String sql="SELECT QUERY_ID, QUERY_ORDER, QUERY_CONTROL, QUERY_DEFAULT, DIM_LEVELS," +
                " DIM_CODE_FILTER, REPORT_ID, COL_ID, COLUMN_ID, DIM_TYPE_ID, " +
                "DIM_TABLE_ID,LABEL_NAME FROM META_RPT_TAB_QUERY_CFG WHERE REPORT_ID=?";
        return getDataAccess().queryForList(sql,reportId);
    }
}
