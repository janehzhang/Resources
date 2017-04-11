package tydic.home.myCenter.myRecent;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.Page;
import tydic.meta.web.session.SessionManager;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 报表管理 —— 个人中心 —— 我最近的操作 <br>
 * @date 2012-4-10
 */
public class MyRecentAction {

    private MyRecentDAO myRecentDAO;

    /**
     * 查询最近打开的报表
     * @return
     */
    public List<Map<String, Object>> queryRecentOpen(Page page){
        if(page==null){//如果没有page，为第一页。
           page=new Page(0,20);
        }
        int currentUserId = SessionManager.getCurrentUserID();
        return myRecentDAO.queryRecentOpen(currentUserId, page);
    }

    /**
     * 查询当前用户所创建的被评论的报表
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryMyBeCommented(Page page){
        if(page==null){//如果没有page，为第一页。
           page=new Page(0,20);
        }
        List<Map<String, Object>> rtn = myRecentDAO.queryMyBeCommented(SessionManager.getCurrentUserID(), page);
        //用AOP方式显示最近的一条评论内容和评论人员
        for(Map<String, Object> one:rtn){
            int reportId = Convert.toInt(one.get("REPORT_ID"),0);
            one.put("COMMENT_CONTEXT", myRecentDAO.queryRecentCommentByReportId(reportId).get("COMMENT_CONTEXT"));
            one.put("USER_NAMECN", myRecentDAO.queryRecentCommentByReportId(reportId).get("USER_NAMECN"));
        }
        return rtn;
    }

    /**
     * 查询我评论过的报表信息
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryMyCommented(Page page){
        if(page==null){//如果没有page，为第一页。
           page=new Page(0,20);
        }
        List<Map<String, Object>> rtn = myRecentDAO.queryMyCommented(SessionManager.getCurrentUserID(), page);
        //用AOP方式显示该评论的回复条数以及未读回复条数
        for(Map<String, Object> one:rtn){
            int commentId = Convert.toInt(one.get("COMMENT_ID"), 0);
            one.put("RE_COUNT", myRecentDAO.queryReCountByCommentId(commentId, false));
            one.put("NEW_RE_COUNT", myRecentDAO.queryReCountByCommentId(commentId, true));
        }
        return rtn;
    }

    /**
     * 根据报表ID查询其评论信息
     * @param reportId
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryCommentByReportId(int reportId, Page page){
        if(page==null){//如果没有page，为第一页。
           page=new Page(0,20);
        }
        List<Map<String, Object>> rtn = myRecentDAO.queryCommentByReportId(reportId, page);
        //TODO 用AOP方式显示该评论的回复条数
        for(Map<String, Object> one:rtn){
            int commentId = Convert.toInt(one.get("COMMENT_ID"), 0);
            one.put("RE_COUNT", myRecentDAO.queryReCountByCommentId(commentId, false));
            one.put("NEW_RE_COUNT", myRecentDAO.queryReCountByCommentId(commentId, true));
        }
        return rtn;
    }

    public void setMyRecentDAO(MyRecentDAO myRecentDAO) {
        this.myRecentDAO = myRecentDAO;
    }
}
