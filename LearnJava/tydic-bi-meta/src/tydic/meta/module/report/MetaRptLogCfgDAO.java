package tydic.meta.module.report;

import tydic.meta.common.MetaBaseDAO;
import tydic.meta.web.session.SessionManager;

import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:表META_RPT_CONFIG_LOG操作DAO
 * @date 2012-04-06
 */
public class MetaRptLogCfgDAO extends MetaBaseDAO {

    /**
     * 添加报表操作日志
     * @param operateType 操作类型
     * @param reportId  报表ID
     * @param option 操作内容
     * @return
     */
    public boolean insertRptConfigLog(int operateType,long reportId,String option) {
        Map<String, Object> user = SessionManager.getCurrentUser();
        String sql = "INSERT INTO META_RPT_CONFIG_LOG(LOG_ID,USER_ID,USER_ZONE_ID,USER_DEPT_ID,USER_STATION_ID,OPERATE_TYPE," +
                "OPERATE_TIME,OPERATE_OPINION,REPORT_ID) " +
                " VALUES (SEQ_RPT_CONFIG_LOG_ID.NEXTVAL,?,?,?,?,?, sysdate,?,?)";
        Object[] params=new Object[]{
                user.get("userId"), user.get("zoneId"), user.get("deptId"), user.get("stationId"),operateType,
                option,reportId
        };
       return getDataAccess().execNoQuerySql(sql,params);
    }

}
