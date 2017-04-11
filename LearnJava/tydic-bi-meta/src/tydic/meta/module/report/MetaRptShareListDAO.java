package tydic.meta.module.report;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:分享表DAO
 * @date 2012-04-16
 */
public class MetaRptShareListDAO extends MetaBaseDAO {

    /**
     * 根据报表ID删除报表分享记录
     *
     * @param reportId
     */
    public void deleteShareByReportId(long reportId) {
        String sql = "DELETE FROM META_RPT_SHARE_LIST WHERE REPORT_ID = " + reportId;
        getDataAccess().execUpdate(sql);
    }

    /**
     * 新增报表分享信息
     *
     * @param
     */
    public void insertShare(long reportId, long userId) {
        String sql = "INSERT INTO META_RPT_SHARE_LIST(SHARE_LIST_ID,REPORT_ID,SHARE_TIME,USER_ID)" +
                " VALUES(SEQ_RPT_SHARE_LIST.NEXTVAL,?,TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'),?)";
        getDataAccess().execUpdate(sql,
                reportId,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                userId);
    }

    /**
     * 批量新增分享
     *
     * @param reportId
     * @param userIds
     */
    public void insertShareBatch(final long reportId, final String[] userIds) {
        String sql = "INSERT INTO META_RPT_SHARE_LIST(SHARE_LIST_ID,REPORT_ID,SHARE_TIME,USER_ID)" +
                " VALUES(SEQ_RPT_SHARE_LIST.NEXTVAL,?,SYSDATE,?)";
        Object[][] params = new Object[userIds.length][2];
        for (int i = 0; i < userIds.length; i++) {
            params[i][0] = reportId;
            params[i][1] = Convert.toLong(userIds[i]);
        }
        getDataAccess().execUpdateBatch(sql, params);
    }

    /**
     * 查询所有的报表共享
     *
     * @param reportId
     * @return 返回USER_ID 数组
     */
    public String[] queryShareUserIds(long reportId) {
        String sql = "SELECT USER_ID FROM META_RPT_SHARE_LIST WHERE REPORT_ID=?";
        Object[][] data = getDataAccess().queryForArray(sql, false, reportId);
        if (data != null && data.length > 0) {
            String[] result = new String[data.length];
            for (int i = 0; i < data.length; i++) {
                result[i] = data[i][0].toString();
            }
            return result;
        }
        return null;
    }

}
