package tydic.home.myCenter.myRecent;

import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 报表管理 —— 个人中心 —— 我最近的操作 DAO<br>
 * @date 2012-4-10
 */
public class MyRecentDAO extends MetaBaseDAO {

    /**
     * 根据用户ID查询其最近打开的报表
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryRecentOpen(int userId, Page page){
        String sql = "SELECT B.REPORT_ID, " +
                " B.REPORT_NAME,B.REPORT_STATE, " +
                " TO_CHAR(MAX(A.USE_TIME),'YYYY-MM-DD hh24:mi:ss') AS USE_TIME, " +
                " COUNT(A.USER_ID) AS COUNT " +
                " FROM   META_RPT_USE_LOG A " +
                " LEFT   JOIN META_RPT_TAB_REPORT_CFG B " +
                " ON     A.REPORT_ID = B.REPORT_ID AND B.REPORT_STATE <> -1" +
                " WHERE  A.USER_ID = ? AND B.REPORT_STATE <> -1 " +
                " AND    A.OPERATE_TYPE = 31 " +
                " GROUP  BY B.REPORT_ID, B.REPORT_NAME,B.REPORT_STATE " +
                " ORDER  BY B.REPORT_STATE DESC,USE_TIME DESC";
        //分页包装
        if(page != null){
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql, userId);
    }

    /**
     * 查询当前用户所创建的被评论的报表
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryMyBeCommented(int userId, Page page){
        String sql = "SELECT B.REPORT_ID,B.REPORT_NAME,B.REPORT_STATE,COUNT(B.REPORT_ID) AS COMMENT_COUNT, " +
                " TO_CHAR(MAX(A.COMMENT_TIME), 'YYYY-MM-DD HH24:MI:SS') COMMENT_TIME FROM META_RPT_COMMENT A " +
                " INNER JOIN META_RPT_TAB_REPORT_CFG B " +
                " ON A.REPORT_ID = B.REPORT_ID AND B.USER_ID = ? AND A.COMMENT_TYPE = 0 AND B.REPORT_STATE <> -1 " +
                " GROUP BY B.REPORT_ID,B.REPORT_NAME,B.REPORT_STATE " +
                " ORDER BY B.REPORT_STATE DESC,MAX(A.COMMENT_TIME) DESC ";
        //分页包装
        if(page != null){
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql, userId);
    }

    /**
     * 根据报表ID查询该报表最新的评论信息
     * @param reportId
     * @return
     */
    public Map<String, Object> queryRecentCommentByReportId(int reportId){
        String sql = "SELECT T.COMMENT_CONTEXT,A.USER_NAMECN FROM META_RPT_COMMENT T  " +
                " LEFT JOIN META_MAG_USER A ON T.USER_ID = A.USER_ID " +
                " WHERE " +
                " T.COMMENT_ID IN (SELECT MAX(COMMENT_ID) FROM META_RPT_COMMENT WHERE REPORT_ID = ? AND COMMENT_TYPE=0) ";
        return getDataAccess().queryForMap(sql, reportId);
    }

    /**
     * 查询我评论过的信息
     * @param userId
     * @return
     */
    public List<Map<String, Object>> queryMyCommented(int userId, Page page){
        String sql = "SELECT A.COMMENT_ID,B.REPORT_ID,B.REPORT_NAME,B.REPORT_STATE,C.USER_NAMECN,A.COMMENT_CONTEXT, " +
                " TO_CHAR(A.COMMENT_TIME, 'YYYY-MM-DD HH24:MI:SS') COMMENT_TIME FROM META_RPT_COMMENT A " +
                " LEFT JOIN META_RPT_TAB_REPORT_CFG B ON A.REPORT_ID = B.REPORT_ID " +
                " LEFT JOIN META_MAG_USER C ON C.USER_ID = B.USER_ID " +
                " WHERE A.USER_ID = ? AND A.COMMENT_TYPE = 0 AND B.REPORT_STATE <> -1 ORDER BY B.REPORT_STATE DESC,A.COMMENT_TIME DESC, A.COMMENT_ID DESC";
        //分页包装
        if(page != null){
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql, userId);
    }

    /**
     * 根据评论ID查询其回复数量
     * @param commentId
     * @param isNewRe 是否是查询新回复数量
     * @return
     */
    public int queryReCountByCommentId(int commentId, boolean isNewRe){
        String sql = "SELECT COUNT(1) FROM META_RPT_COMMENT t WHERE t.par_comment_id = ? ";
        if(isNewRe){
            sql = sql + " AND T.IS_READ = 0 ";
        }
        return getDataAccess().queryForInt(sql, commentId);
    }

    /**
     * 根据报表ID查询其评论信息
     * @param reportId
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryCommentByReportId(int reportId, Page page){
        String sql = "SELECT A.COMMENT_ID,B.REPORT_NAME,B.REPORT_STATE,C.USER_NAMECN,A.COMMENT_CONTEXT, " +
                " TO_CHAR(A.COMMENT_TIME, 'YYYY-MM-DD HH24:MI:SS') COMMENT_TIME " +
                " FROM META_RPT_COMMENT A " +
                " LEFT JOIN META_RPT_TAB_REPORT_CFG B ON A.REPORT_ID = B.REPORT_ID " +
                " LEFT JOIN META_MAG_USER C ON A.USER_ID = C.USER_ID " +
                " WHERE A.REPORT_ID = ? AND A.COMMENT_TYPE = 0 " +
                " ORDER BY A.COMMENT_TIME DESC, A.COMMENT_ID DESC";
        //分页包装
        if(page != null){
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql, reportId);
    }

}
