package tydic.home.commentRpt;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.Page;
import tydic.meta.module.mag.favorite.FavoriteDAO;
import tydic.meta.module.mag.favorite.report.FavReportDAO;
import tydic.meta.module.report.RptUserLogDAO;
import tydic.meta.web.session.SessionManager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 评论报表  Action
 * @date 12-4-17
 * -
 * @modify
 * @modifyDate -
 */
public class CommentRptAction  {

    private CommentRptDAO commentRptDAO;
    private RptUserLogDAO logDAO;
    private FavReportDAO favRptDAO;
    private FavoriteDAO favDAO;

    /**
     * 获取报表基本信息
     * @param rptId
     * @return
     */
    public Map<String,Object> getRptInfo(int rptId){
        int userId = SessionManager.getCurrentUserID();
        Map<String,Object> data = commentRptDAO.getRptInfo(rptId);
        if(data!=null){
            boolean authFlag = commentRptDAO.checkCurrentUserOpenRptAuth(userId,rptId);
            if(authFlag){
                int gradeNum = commentRptDAO.getGradeRptNum(rptId,userId,true);
                BigDecimal bd = new BigDecimal(MapUtils.getString(data,"GRADE"));
                bd = bd.setScale(1,BigDecimal.ROUND_HALF_UP);
                data.put("avgGrade",String.valueOf(bd));//在评论的第一条记录设置 报表平均分
                data.put("gradeNum",gradeNum);  //设置当前人评分次数
            }
            data.put("currentUserAuth",authFlag?1:0); //设置权限
        }
        return data;
    }

    /**
     * 获取报表订阅信息
     * @param rptId
     * @return
     */
    public Map<String,Object> getRptFavPushInfo(int rptId){
        int userId = SessionManager.getCurrentUserID();
        Map<String,Object> data = commentRptDAO.getRptFavPushInfo(userId, rptId);
        if(data!=null){
            String pushId = MapUtils.getString(data, "PUSH_CONFIG_ID");
            if(pushId!=null && !"".equals(pushId) && !"null".equals(pushId)){
                String pushTypes = MapUtils.getString(data,"PUSH_TYPE");
                String[] arr = pushTypes.split(",");
                for (String anArr : arr) {
                    if (anArr.equals("1")) {
                        data.put("sendMethod1","1");
                    } else if (anArr.equals("2")) {
                        data.put("sendMethod2","2");
                    } else if (anArr.equals("3")) {
                        data.put("sendMethod3","3");
                    }
                }
                String sendTime = MapUtils.getString(data,"SEND_BASE_TIME");
                int startNum = MapUtils.getIntValue(data, "SEND_TIME_ADD", 0);
                Date sendBaseTime = DateUtil.getDateTimeByString(sendTime, "yyyy-MM-dd HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sendBaseTime);
                calendar.add(Calendar.DATE, startNum);	//基准时间为发送时间减去初始时间增量天数
                data.put("sendTime",DateUtil.format(calendar.getTime(),"yyyy-MM-dd HH:mm:ss"));
            }
        }
        return data;
    }

    /**
     * 提交收藏订阅操作
     * @param data
     * @return
     */
    public int favPushOpt(Map<String,Object> data){
        int userId = SessionManager.getCurrentUserID();
        String optIds = MapUtils.getString(data, "optId");
        int rptId = MapUtils.getIntValue(data, "rptId");
        int favId = MapUtils.getIntValue(data, "favId", 0);
        int pushId = MapUtils.getIntValue(data, "pushId", 0);
        int favTypeId = MapUtils.getInteger(data,"favTypeId");
        String favTypeName = MapUtils.getString(data,"favTypeName");
        int ret = 1;
        try{
            BaseDAO.beginTransaction();
            RptUserLogDAO logDAO = new RptUserLogDAO();
            if(optIds.contains("0")){//收藏
                favTypeId = favDAO.insertRptFavorite(userId,favTypeName,2);
                favId = favRptDAO.insertFavoriteReport(rptId,favTypeId,userId);
            }
            if(optIds.contains("2")){//订阅
                String sendTime = MapUtils.getString(data,"sendBaseTime");
                int sendSequnce = MapUtils.getInteger(data,"sendSequnce");
                String pushType = MapUtils.getString(data,"pushType");
                pushId = favRptDAO.insertPushRpt(rptId,favId,userId,sendTime,sendSequnce,pushType);
            }
            if(optIds.contains("3")){//取消订阅
                int a = favRptDAO.deletePushReport(pushId);
                if(a>0){
                    logDAO.recordRptUserLog(rptId,RptUserLogDAO.CANCEL_PUSH);
                }
            }
            if(optIds.contains("1")){//取消收藏
                int a = favRptDAO.deleteFavoriteReport(favId);
                if(a>0){
                    logDAO.recordRptUserLog(rptId,RptUserLogDAO.CANCEL_FAV);
                }
            }
            if(optIds.contains("4")){
                favRptDAO.updateFavoriteReportType(favId,favTypeId);
                if(pushId!=0){
                    String sendTime = MapUtils.getString(data,"sendBaseTime");
                    int sendSequnce = MapUtils.getInteger(data,"sendSequnce");
                    String pushType = MapUtils.getString(data,"pushType");
                    favRptDAO.updatePushRpt(rptId,favId,userId,sendTime,sendSequnce,pushType);
                }
            }
            BaseDAO.commit();
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("收藏订阅出错", e);
            ret = -1;
        }
        return ret;
    }

    /**
     * 记录打开日志
     * @param rptId
     * @return
     */
    public int recordOpenRptLog(int rptId){
        return logDAO.recordRptUserLog(rptId,RptUserLogDAO.OPEN);
    }
    
    /**
     * 根据报表ID获取评论信息(包括评分)
     * @param rptId 报表ID
     * @param ps 评论起始数
     * @param num 显示数量（num=0时，取系统默认值）
     * @return
     */
    public List<Map<String,Object>> queryCommentsInfo(int rptId,int ps,int num){
        int userId = SessionManager.getCurrentUserID();
        Page page = new Page(ps>=0?ps:0,num!=0?num:CommentRptConstant.SHOW_COMMENTS_NUM);
        List<Map<String,Object>> comments = commentRptDAO.queryCommentsById(rptId,userId, num<0?null:page);
        return comments;
    }

    /**
     * 刷新报表的平均分
     * @param rptId
     * @return
     */
    public String refreshAvgGrade(int rptId){
        String grade = commentRptDAO.getAvgGrade(rptId);
        if(grade!=null && !"".equals(grade)){
            BigDecimal bd = new BigDecimal(grade);
            bd = bd.setScale(1,BigDecimal.ROUND_HALF_UP);
            return String.valueOf(bd);
        }
        return "";
    }

    /**
     * 获取某一评论回复信息
     * @param rptId
     * @param commentId
     * @param ps 起始数
     * @param num 一页大小
     * @return
     */
    public List<Map<String,Object>> queryReplyComments(int rptId,int commentId,int ps,int num){
        Page page = new Page(ps>=0?ps:0,num!=0?num:CommentRptConstant.SHOW_COMMENTS_NUM);
        List<Map<String,Object>> replys = commentRptDAO.queryReplyCommentsById(rptId,commentId,null,page);
        return replys;
    }

    /**
     * 评论报表
     * @param rptId 报表ID
     * @param info 评论信息
     * @return
     */
    public int commentRpt(int rptId,String info){
        int ret = 0;
        int userId = SessionManager.getCurrentUserID();
        try{
            BaseDAO.beginTransaction();
            ret = commentRptDAO.commentRpt(rptId,info,userId);
            BaseDAO.commit();
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("评论报表出错", e);
            ret = -1;
        }
        return ret;
    }

    /**
     * 打分
     * @param rptId 报表ID
     * @param grade 分数
     * @return
     */
    public String gradeRpt(int rptId,int grade){
        int ret = 0;
        int userId = SessionManager.getCurrentUserID();
        try{
            BaseDAO.beginTransaction();
            ret = commentRptDAO.scoringRpt(rptId,userId,grade);
            BaseDAO.commit();
            String avg = commentRptDAO.getAvgGrade(rptId);
            BigDecimal bd = new BigDecimal(avg);
            bd = bd.setScale(1,BigDecimal.ROUND_HALF_UP);
            return String.valueOf(bd);
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("报表评分出错", e);
            ret = -1;
        }
        return String.valueOf(ret);
    }

    /**
     * 回复评论
     * @param rptId 报表ID
     * @param commentId  被回复的评论ID
     * @param info 回复信息
     * @return
     */
    public int replyCommentRpt(int rptId,int commentId,String info){
        int ret = 0;
        int userId = SessionManager.getCurrentUserID();
        try{
            BaseDAO.beginTransaction();
            ret = commentRptDAO.replyCommentRpt(rptId,commentId,info,userId);
            BaseDAO.commit();
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("回复报表评论出错", e);
            ret = -1;
        }
        return ret;
    }

    /**
     * 设置回复已读
     * @return
     */
    public int setReplyRead(int commentId){
        int ret = 0;
        int userId = SessionManager.getCurrentUserID();
        try{
            BaseDAO.beginTransaction();
            ret = commentRptDAO.setReplyRead(commentId,userId);
            BaseDAO.commit();
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("设置回复已读出错", e);
            ret = -1;
        }
        return ret;
    }


    public void setCommentRptDAO(CommentRptDAO commentRptDAO) {
        this.commentRptDAO = commentRptDAO;
    }

    public void setLogDAO(RptUserLogDAO logDAO) {
        this.logDAO = logDAO;
    }

    public void setFavRptDAO(FavReportDAO favRptDAO) {
        this.favRptDAO = favRptDAO;
    }

    public void setFavDAO(FavoriteDAO favDAO) {
        this.favDAO = favDAO;
    }
}
