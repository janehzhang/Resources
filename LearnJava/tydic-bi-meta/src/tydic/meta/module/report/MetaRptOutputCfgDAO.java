package tydic.meta.module.report;

import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.Constant;
import tydic.meta.common.MetaBaseDAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:表 META_RPT_TAB_OUTPUT_CFG 操作DAO
 * @date 2012-04-06
 */
public class MetaRptOutputCfgDAO extends MetaBaseDAO {

    /**
     * 批量添加报表输出字段
     *
     * @param datas
     * @param reportId 报表ID
     * @return
     */
    public long[] insertRptTabOutput(final List<Map<String, Object>> datas, final long reportId) {
        String sql = "INSERT INTO META_RPT_TAB_OUTPUT_CFG(REPORT_ID,OUTPUT_ORDER,OUTPUT_EXPRESS,DIM_LEVELS,DIM_CODE_FILTER," +
                "TOTAL_FLAG,COL_ID,COLUMN_ID,TOTAL_DISPLAY,DUCK_FLAG," +
                "TRANS_CODE,COLUMN_NAME,DIM_TYPE_ID,DIM_TABLE_ID,COLLECT_MOTHED,SELECT_NAME,OUTPUT_ID) " +
                " VALUES (?,?,?,?,? ,?,?,?,?,?, ?,?,?,?,?, ?,?)";
        final long[] returnIds = new long[datas.size()];
        for (int i = 0; i < returnIds.length; i++) {
            returnIds[i] = super.queryForNextVal("SEQ_RPT_TAB_OUTPUT_CFG_ID");
        }
        getDataAccess().execUpdateBatch(sql, new IParamsSetter() {
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                Map<String, Object> data = datas.get(i);
                preparedStatement.setObject(1, reportId);             //报表ID
                preparedStatement.setObject(2, MapUtils.getIntValue(data, "outputOrder"));         //序号
                preparedStatement.setObject(3, MapUtils.getString(data, "outputExpress"));        //表达式
                preparedStatement.setObject(4, MapUtils.getString(data, "dimLevels"));           //维度层级
                preparedStatement.setObject(5, MapUtils.getString(data, "dimCodeFilter"));         //维度编码过滤
                preparedStatement.setObject(6, MapUtils.getIntValue(data, "totalFlag", Constant.META_DISABLE));//是否合计
                preparedStatement.setObject(7, MapUtils.getIntValue(data, "colId"));                 //发布模型ID
                preparedStatement.setObject(8, MapUtils.getIntValue(data, "columnId"));//字段ID
                preparedStatement.setObject(9, MapUtils.getString(data, "totalDisplay")); //非合计显示值
                preparedStatement.setObject(10, MapUtils.getIntValue(data, "duckFlag")); //是否下转标识
                preparedStatement.setObject(11, MapUtils.getString(data, "transCode"));  //转换编码
                preparedStatement.setObject(12, MapUtils.getString(data, "columnName")); //显示列名
                preparedStatement.setObject(13, MapUtils.getObject(data, "dimTypeId"));  //归并类型ID
                preparedStatement.setObject(14, MapUtils.getObject(data, "dimTableId")); //维度表类ID
                preparedStatement.setObject(15, MapUtils.getObject(data, "collectMothed"));//汇总方式
                preparedStatement.setObject(16, MapUtils.getObject(data, "selectName"));//查询列别名
                preparedStatement.setObject(17, returnIds[i]);
            }

            public int batchSize() {
                return datas.size();
            }
        });
        return returnIds;
    }

    /**
     * 根据报表ID删除下面所有的报表输出设置
     *
     * @param reportId
     * @return
     */
    public boolean deleteByReportID(long reportId) {
        String sql = "DELETE FROM META_RPT_TAB_OUTPUT_CFG WHERE REPORT_ID=?";
        return getDataAccess().execNoQuerySql(sql, reportId);
    }

    /**
     * 根据报表ID查询其报表输出信息
     *
     * @param reportId
     * @return
     */
    public List<Map<String, Object>> queryOutputInfoByReportId(long reportId) {
        String sql = "SELECT OUTPUT_ID, REPORT_ID, OUTPUT_ORDER, OUTPUT_EXPRESS, DIM_LEVELS, DIM_CODE_FILTER," +
                " TOTAL_FLAG,TOTAL_DISPLAY, COL_ID, COLUMN_ID, DUCK_FLAG, TRANS_CODE, COLUMN_NAME, DIM_TYPE_ID," +
                " DIM_TABLE_ID, COLLECT_MOTHED,SELECT_NAME,GDL_ID FROM META_RPT_TAB_OUTPUT_CFG WHERE REPORT_ID=?";
        return getDataAccess().queryForList(sql, reportId);
    }
}
