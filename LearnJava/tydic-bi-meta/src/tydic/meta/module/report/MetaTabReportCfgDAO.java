package tydic.meta.module.report;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Constant;
import tydic.meta.common.DateUtil;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.web.session.SessionManager;

import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:表META_RPT_TAB_REPORT_CFG 操作DAO
 * @date 2012-04-06
 */
public class MetaTabReportCfgDAO extends MetaBaseDAO {
    /**
     * 新增报表
     *
     * @param data
     * @return 成功后返回一个报表ID
     */
    public long insertRptTabReport(Map<String, Object> data) throws Exception {
        int userId = SessionManager.getCurrentUserID();
        //获取报表ID
        long reportId = queryForNextVal("SEQ_RPT_REPORT_ID");
        //获取当前时间字符串
        String currentDate = DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss");
        String sql = "INSERT INTO META_RPT_TAB_REPORT_CFG(REPORT_ID,USER_ID,REPORT_NAME,CREATE_TIME," +
                " START_DATE,EFFECT_TIME,REPORT_KEYWORD,REPORT_SQL,REPORT_NOTE,REPORT_TYPE_ID," +
                " REPORT_STATE,ISSUE_ID,IS_LISTING,REPORT_TRANS_CODE_SQL,DATA_SOURCE_ID," +
                "RIGHT_LEVEL,DATA_TRANS_DIMS,DATA_TRANS_GDLS,DATA_TRANS_GDL_COLNAME,DIM_DUCK_TERM_SQL) VALUES (?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss')" +
                ",to_date(?,'yyyy-mm-dd hh24:mi:ss')  ,?,?,?,?,? ,?,?,?,?,? ,?,?,?,?,?)";
        Object[] params = new Object[]{
                reportId, userId, MapUtils.getString(data, "reportName"), currentDate, MapUtils.getString(data, "startDate", currentDate),
                MapUtils.getIntValue(data, "effectTime", ReportConstant.REPORT_EFFECT_TIME_ONE_YEAR), MapUtils.getString(data, "reportKeyword"), MapUtils.getString(data, "reportSql"), MapUtils.getString(data, "reportNote"), MapUtils.getObject(data, "reportTypeId"),
                MapUtils.getIntValue(data, "reportState", Constant.META_ENABLE), MapUtils.getIntValue(data, "issueId"), MapUtils.getIntValue(data, "isListing"), MapUtils.getString(data, "reportTransCodeSql"), MapUtils.getIntValue(data, "dataSourceId"),
                MapUtils.getIntValue(data, "rightLevel", ReportConstant.REPORT_RIGHT_LEVEL_PRIVATE), MapUtils.getString(data, "dataTransDims"), MapUtils.getString(data, "dataTransGdls"), MapUtils.getString(data, "dataTransGdlColname"), MapUtils.getString(data, "dimDuckTermSql")
        };
        getDataAccess().execUpdate(sql, params);
        return reportId;
    }

    /**
     * 修改报表
     *
     * @param data
     * @return
     */
    public boolean updateRptTabReport(Map<String, Object> data, long reportId) {
        String currentDate = DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss");
        String sql = "UPDATE META_RPT_TAB_REPORT_CFG T SET T.REPORT_NAME=?," +
                " T.START_DATE=to_date(?,'yyyy-mm-dd hh24:mi:ss'),T.EFFECT_TIME=?," +
                " T.REPORT_KEYWORD=?,T.REPORT_SQL=?,T.REPORT_NOTE=?,T.REPORT_TYPE_ID=?," +
                " T.REPORT_TRANS_CODE_SQL=?, " +
                "T.REPORT_STATE=?,T.RIGHT_LEVEL=?,T.DATA_TRANS_DIMS=?,T.DATA_TRANS_GDLS=?,T.DATA_TRANS_GDL_COLNAME=?, " +
                "T.DIM_DUCK_TERM_SQL=? WHERE T.REPORT_ID=?";
        Object[] params = new Object[]{
                MapUtils.getString(data, "reportName"), MapUtils.getString(data, "startDate", currentDate), MapUtils.getIntValue(data, "effectTime", ReportConstant.REPORT_EFFECT_TIME_ONE_YEAR),
                MapUtils.getString(data, "reportKeyword"), MapUtils.getString(data, "reportSql"), MapUtils.getString(data, "reportNote"),
                MapUtils.getObject(data, "reportTypeId"), MapUtils.getString(data, "reportTransCodeSql"),
                MapUtils.getIntValue(data, "reportState", Constant.META_ENABLE), MapUtils.getIntValue(data, "rightLevel", ReportConstant.REPORT_RIGHT_LEVEL_PRIVATE), MapUtils.getString(data, "dataTransDims"), MapUtils.getString(data, "dataTransGdls"), MapUtils.getString(data, "dataTransGdlColname"),
                MapUtils.getString(data, "dimDuckTermSql"), reportId
        };
        return getDataAccess().execNoQuerySql(sql, params);
    }

    /**
     * 更新其编码转换信息
     *
     * @param reportId
     * @param dataTransDims 编码转换维度字段
     * @param dataTransGdls 编码转换指标字段
     * @return
     */
    public boolean updateTranInfo(long reportId, String dataTransDims, String dataTransGdls) {
        String sql = "UPDATE META_RPT_TAB_REPORT_CFG T SET T.DATA_TRANS_DIMS=?,T.DATA_TRANS_GDLS=? " +
                " WHERE T.REPORT_ID=? ";
        return getDataAccess().execNoQuerySql(sql, dataTransDims, dataTransGdls, reportId);
    }

    /**
     * 查询报表配置信息
     *
     * @param reportId
     * @return
     */
    public Map<String, Object> queryRptById(long reportId) {
        String sql = "SELECT REPORT_ID, USER_ID, REPORT_NAME, CREATE_TIME, TO_CHAR(START_DATE,'yyyymmdd') START_DATE, EFFECT_TIME," +
                " REPORT_KEYWORD, REPORT_SQL, REPORT_NOTE, REPORT_TYPE_ID, REPORT_STATE, ISSUE_ID, IS_LISTING," +
                " REPORT_TRANS_CODE_SQL,RIGHT_LEVEL,DATA_SOURCE_ID,DATA_TRANS_DIMS,DATA_TRANS_GDLS,DATA_TRANS_GDL_COLNAME,REPORT_TYPE " +
                "FROM META_RPT_TAB_REPORT_CFG WHERE REPORT_ID=?";
        return getDataAccess().queryForMap(sql, reportId);
    }

    /**
     * 删除一个报表信息
     *
     * @param reportId
     * @return
     */
    public int deleteRptById(long reportId) {
        String sql = "DELETE FROM META_RPT_TAB_REPORT_CFG WHERE REPORT_ID=?";
        return getDataAccess().execUpdate(sql, reportId);
    }

}
