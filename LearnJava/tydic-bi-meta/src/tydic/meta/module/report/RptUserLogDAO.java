package tydic.meta.module.report;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.web.session.SessionManager;

import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description  报表收藏订阅打开 日志记录DAO
 * @date 12-4-19
 * -
 * @modify
 * @modifyDate -
 */
public class RptUserLogDAO extends MetaBaseDAO{
    
    public final static int FAV = 11;             //收藏
    public final static int CANCEL_FAV = 10;  //取消收藏

    public final static int PUSH = 21;        //订阅
    public final static int CANCEL_PUSH = 20;      //取消订阅
    public final static int MODIFY_PUSH = 22;     //修改订阅方式

    public final static int OPEN = 31;      //打开

    /**
     * 记录报表收藏订阅打开日志
     * @param rptId 报表ID
     * @param opt 操作类型
     * @return
     */
    public int recordRptUserLog(long rptId,int opt){
        Map<String,Object> user = SessionManager.getCurrentUser();
        String sql = "INSERT INTO META_RPT_USE_LOG(USE_LOG_ID,USER_ID,USE_TIME,USE_ZONE_ID," +
                " USE_DEPT_ID,USE_STATION_ID,REPORT_ID,OPERATE_TYPE) VALUES(?,?,sysdate,?,?,?,?,?)";
        long pk = queryForNextVal("SEQ_RPT_USE_LOG_ID");
        return getDataAccess().execUpdate(sql, pk, user.get("userId"), user.get("zoneId"), user.get("deptId"), user.get("stationId"), rptId, opt);
    }

    /**
     * 记录报表收藏订阅打开日志
     * @param rptId 报表ID
     * @param opt 操作类型
     * @return
     */
    public int recordRptUserLog(int rptId,int opt){
        return recordRptUserLog(Long.parseLong(String.valueOf(rptId)),opt);
    }

    
}
