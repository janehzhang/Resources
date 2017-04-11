package tydic.home.commentRpt;

import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.web.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description  评论报表DAO
 * @date 12-4-17
 * -
 * @modify
 * @modifyDate -
 */
public class CommentRptDAO extends MetaBaseDAO{

    /**
     * 获取报表基本信息
     * @param rptId
     * @return
     */
    public Map<String,Object> getRptInfo(int rptId){
        String sql = "SELECT A.REPORT_ID,A.USER_ID,A.REPORT_NAME,to_char(A.CREATE_TIME,'yyyy-mm-dd hh24:mi:ss') CREATE_TIME," +
                "NVL(B.USER_NAMECN,B.USER_NAMEEN) USER_NAME,A.IS_LISTING,D.AUDIT_TYPE,C.SUBSCRIBE_TYPE," +
                "(SELECT NVL(AVG(GRADE),0) FROM META_RPT_USER_GRADE WHERE REPORT_ID=A.REPORT_ID) GRADE, "+
                "(SELECT REPORT_ID FROM META_REPORT WHERE REPORT_ID=A.REPORT_ID) DESIGNER_RPT_ID "+
                "FROM META_RPT_TAB_REPORT_CFG A  " +
                "LEFT JOIN META_MAG_USER B ON A.USER_ID=B.USER_ID " +
                "LEFT JOIN META_RPT_DATA_AUDIT_CFG D ON D.ISSUE_ID=A.ISSUE_ID " +
                "LEFT JOIN META_RPT_MODEL_ISSUE_CONFIG C ON C.ISSUE_ID=A.ISSUE_ID " +
                "WHERE A.REPORT_STATE=1 AND A.REPORT_ID=? ";
        return getDataAccess().queryForMap(sql,rptId);
    }

    /**
     * 获取报表收藏订阅信息
     * @param userId
     * @param rptId
     * @return
     */
    public Map<String,Object> getRptFavPushInfo(int userId,int rptId){
        String sql = "SELECT A.REPORT_FAVORITE_ID,A.FAVORITE_ID,C.FAVORITE_NAME,B.PUSH_CONFIG_ID,B.PUSH_TYPE,B.SEND_SEQUNCE," +
                "TO_CHAR(B.SEND_BASE_TIME,'yyyy-mm-dd hh24:mi:ss') SEND_BASE_TIME,B.SEND_TIME_ADD " +
                "FROM META_RPT_USER_FAVORITE A " +
                "LEFT JOIN meta_mag_favorite_dir C ON A.FAVORITE_ID=C.FAVORITE_ID AND FAVORITE_TYPE=2 " +
                "LEFT OUTER JOIN META_RPT_PUSH_CONFIG B ON A.REPORT_ID=B.REPORT_ID AND A.USER_ID=B.USER_ID " +
                "WHERE A.USER_ID=? AND A.REPORT_ID=?";
        return getDataAccess().queryForMap(sql,userId,rptId);
    }
    
    /**
     * 获取报表的评论信息
     * @param rptId 报表ID
     * @param page 分页
     * @param userId 用户ID
     * @return
     */
    public List<Map<String,Object>> queryCommentsById(int rptId,int userId,Page page){
        String sql = "SELECT A.COMMENT_ID,A.REPORT_ID,A.USER_ID,A.COMMENT_CONTEXT," +
                "to_char(A.COMMENT_TIME,'yyyy-mm-dd hh24:mi:ss') COMMENT_TIME,A.PAR_COMMENT_ID," +
                "(SELECT COUNT(1) FROM META_RPT_COMMENT WHERE PAR_COMMENT_ID=A.COMMENT_ID " +
                "AND PAR_USER_ID=? AND IS_READ=0) NEW_REPLY,B.USER_NAMECN " +
                "FROM META_RPT_COMMENT A " +
                "LEFT JOIN META_MAG_USER B ON A.USER_ID=B.USER_ID  "+
                "WHERE A.COMMENT_TYPE=0 AND A.REPORT_ID=? " +
                "ORDER BY A.COMMENT_TIME DESC ";
        if(page!=null){
            sql = SqlUtils.wrapPagingSql(sql,page);
        }
        return getDataAccess().queryForList(sql,userId,rptId);
    }

    /**
     * 获取报表回复信息
     * @param rptId
     * @param parCommentId 被评论的ID
     * @param cond 附加条件
     * @return
     */
    public List<Map<String,Object>> queryReplyCommentsById(int rptId,int parCommentId,String cond,Page page){
        String sql = "SELECT A.COMMENT_ID,A.REPORT_ID,A.USER_ID,A.COMMENT_CONTEXT," +
                "to_char(A.COMMENT_TIME,'yyyy-mm-dd hh24:mi:ss') COMMENT_TIME," +
                "A.PAR_COMMENT_ID,A.IS_READ,B.USER_NAMECN " +
                "FROM META_RPT_COMMENT A " +
                "LEFT JOIN META_MAG_USER B ON A.USER_ID=B.USER_ID  "+
                "WHERE A.COMMENT_TYPE=1 AND A.REPORT_ID=? ";
        List<Integer> params = new ArrayList<Integer>();
        params.add(rptId);
        if(parCommentId!=-1){
            sql += " AND A.PAR_COMMENT_ID=? ";
            params.add(parCommentId);
        }
        if(cond!=null && !"".equals(cond)){
            cond = StringUtils.upperCase(cond);
            sql +=((cond.startsWith("AND") || cond.startsWith(" AND")) ? " " :"AND ") + cond;
        }
        sql += "ORDER BY A.COMMENT_TIME DESC ";
        if(page!=null){
            sql = SqlUtils.wrapPagingSql(sql,page);
        }
        return getDataAccess().queryForList(sql,params.toArray());
    }

    /**
     * 插入评论信息
     * @param rptId 报表ID
     * @param info 评论信息
     * @param userId 评论人ID
     * @return
     */
    public int commentRpt(int rptId,String info,int userId){
        String sql = "INSERT INTO META_RPT_COMMENT(COMMENT_ID,REPORT_ID,USER_ID,COMMENT_CONTEXT," +
                "COMMENT_TYPE,COMMENT_TIME,PAR_COMMENT_ID,PAR_USER_ID,IS_READ) VALUES(?,?,?,?,0,sysdate,0,null,0)";
        long pk = queryForNextVal("SEQ_RPT_COMMENT_ID");
        return getDataAccess().execUpdate(sql,pk,rptId,userId,info);
    }

    /**
     * 插入回复信息
     * @param rptId 报表ID
     * @param commentId 被回复的评论ID
     * @param info 回复信息
     * @param userId 回复人ID
     * @return
     */
    public int replyCommentRpt(int rptId,int commentId,String info,int userId){
        String sql = "INSERT INTO META_RPT_COMMENT(COMMENT_ID,REPORT_ID,USER_ID,COMMENT_CONTEXT," +
                "COMMENT_TYPE,COMMENT_TIME,PAR_COMMENT_ID,PAR_USER_ID,IS_READ) " +
                "SELECT ?,?,?,?,1,sysdate,COMMENT_ID,USER_ID,CASE WHEN USER_ID=? THEN 1 ELSE 0 END FROM META_RPT_COMMENT WHERE COMMENT_ID=? ";
        long pk = queryForNextVal("SEQ_RPT_COMMENT_ID");
        return getDataAccess().execUpdate(sql,pk,rptId,userId,info,userId,commentId);
    }

    /**
     * 将回复评论标记为已读
     * @param commentId  被评论的ID
     * @param userId 被评论用户
     * @return
     */
    public int setReplyRead(int commentId,int userId){
        String sql = "UPDATE META_RPT_COMMENT SET IS_READ=1 WHERE PAR_COMMENT_ID=? AND PAR_USER_ID=? ";
        return getDataAccess().execUpdate(sql,commentId,userId);
    }

    /**
     * 检测当前用户是否有打开某报表的权限
     * 有权限分：公共，我创建，我收藏的，共享给我的
     * @param userId 用户ID
     * @param rptId 报表ID
     * @return 返回true 表示有权限
     */
    public boolean checkCurrentUserOpenRptAuth(int userId,int rptId){
        if(SessionManager.isCurrentUserAdmin()){
            return true;
        }
        String sql = " SELECT 1 FLAG FROM DUAL WHERE " +
                "EXISTS(SELECT REPORT_ID FROM META_RPT_SHARE_LIST WHERE REPORT_ID=? AND USER_ID=?) OR "+
                "EXISTS(SELECT REPORT_ID FROM META_RPT_USER_FAVORITE WHERE REPORT_ID=? AND USER_ID=?) OR " +
                "EXISTS(SELECT REPORT_ID FROM META_RPT_TAB_REPORT_CFG WHERE REPORT_ID=? AND (USER_ID=? OR RIGHT_LEVEL=0)) ";
        int a = getDataAccess().queryForIntByNvl(sql,0,rptId,userId,rptId,userId,rptId,userId);
        return a==1;
    }

    /**
     * 获取用户对某一个报表当日的评分次数
     * @param rptId 报表ID
     * @param userId 用户ID
     * @param currentDate 是否只查当日
     * @return
     */
    public int getGradeRptNum(int rptId,int userId,boolean currentDate){
        String sql = "SELECT COUNT(1) FROM META_RPT_USER_GRADE WHERE REPORT_ID=? AND GRADE_USER_ID=? ";
        if(currentDate){
            sql += " AND TRUNC(GRADE_TIME,'dd')=TRUNC(SYSDATE,'dd') ";
        }
        return getDataAccess().queryForInt(sql,rptId,userId);
    }

    /**
     * 报表打分
     * @param rptId 报表ID
     * @param userId 用户ID
     * @param grade 分数
     * @return
     */
    public int scoringRpt(int rptId,int userId,int grade){
        String sql = "INSERT INTO META_RPT_USER_GRADE(GRADE_ID,REPORT_ID,GRADE,GRADE_USER_ID,GRADE_TIME) " +
                "VALUES(?,?,?,?,sysdate)";
        long pk = queryForNextVal("SEQ_RPT_USER_GRADE_ID");
        return getDataAccess().execUpdate(sql,pk,rptId,grade,userId);
    }

    /**
     * 获取报表平均分
     * @param rptId
     * @return
     */
    public String getAvgGrade(int rptId){
        String sql = "SELECT NVL(AVG(GRADE),0) GRADE FROM META_RPT_USER_GRADE WHERE REPORT_ID=?";
        return getDataAccess().queryForString(sql,rptId);
    }

}
