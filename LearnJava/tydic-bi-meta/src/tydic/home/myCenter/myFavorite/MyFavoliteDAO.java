package tydic.home.myCenter.myFavorite;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 刘斌
 * @description 我收藏的报表DAO
 * @date 2012-04-11
 */
public class MyFavoliteDAO extends MetaBaseDAO {

    /**
     * 查询我收藏的报表
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryMyFavolite(Map<String, Object> queryData, Page page){
        List<Object> param = new ArrayList<Object>();
        String keyWord = Convert.toString(queryData.get("keyWord"));
        param.add(Convert.toInt(queryData.get("userId"),0));
        String sql = "SELECT a.report_favorite_id,c.report_id,d.user_namecn,e.zone_name,f.dept_name," +
                " c.report_name,c.report_note,c.REPORT_STATE, " +
                " to_char(a.favorite_report_time, 'YYYY-MM-DD hh24:mi:ss') AS fav_time, " +
                " to_char(b.create_time, 'YYYY-MM-DD hh24:mi:ss') AS push_time,b.push_config_id, " +
                " CASE WHEN b.push_config_id>0 THEN 1 ELSE 0 END AS is_pushed," +
                " b.push_type,b.send_sequnce, to_char(b.send_base_time, 'YYYY-MM-DD hh24:mi:ss') AS send_base_time  " +
                " FROM META_RPT_USER_FAVORITE a " +
                " LEFT JOIN META_RPT_PUSH_CONFIG b ON a.report_favorite_id = b.report_favorite_id " +
                " LEFT JOIN META_RPT_TAB_REPORT_CFG c ON a.report_id = c.report_id " +
                " LEFT JOIN meta_mag_user d ON d.user_id = c.User_Id " +
                " LEFT JOIN meta_dim_zone e ON d.zone_id = e.zone_id " +
                " LEFT JOIN META_DIM_USER_DEPT f ON d.dept_id = f.dept_CODE " +
                " WHERE c.REPORT_STATE <> -1 and a.user_id = ? ";
        if(!keyWord.equals("")){
            sql = sql + " AND  c.report_name LIKE "+SqlUtils.allLikeParam(keyWord) ;
        }
        sql = sql + " ORDER BY c.REPORT_STATE desc, a.favorite_report_time DESC, b.create_time DESC, a.report_id DESC ";
        //分页包装
        if(page != null){
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql, param.toArray());
    }

    /**
     * 订阅收藏的报表
     * @param data
     */
    public void insertPush(Map<String, Object> data){
        String sql = "INSERT INTO META_RPT_PUSH_CONFIG " +
                " (PUSH_CONFIG_ID,REPORT_ID,REPORT_FAVORITE_ID,USER_ID,PUSH_TYPE," +
                "SEND_SEQUNCE,  SEND_BASE_TIME,SEND_TIME_ADD,CREATE_TIME) " +
                " VALUES(?,?,?,?,?,?,TO_DATE(?,'yyyy-mm-dd hh24:mi:ss'),?,TO_DATE(?,'yyyy-mm-dd hh24:mi:ss'))";
        getDataAccess().execUpdate(sql,
                queryForNextVal("SEQ_RPT_PUSH_CONFIG_ID"),data.get("reportId"),data.get("reportFavoriteId"),data.get("userId"),data.get("pushType"),
                data.get("sendSequnce"),data.get("sendBaseTime"),data.get("sendTimeAdd"),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        );

    }

    /**
     * 查询一个报表收藏信息
     * @param userId
     * @param reportId
     * @return
     */
    public Map<String, Object> queryOneFavolite(int userId, int reportId){
         String sql = "SELECT a.report_favorite_id,c.report_id,d.user_namecn,e.zone_name,f.dept_name," +
                " c.report_name,c.report_note,c.REPORT_STATE, " +
                " to_char(a.favorite_report_time, 'YYYY-MM-DD hh24:mi:ss') AS fav_time, " +
                " to_char(b.create_time, 'YYYY-MM-DD hh24:mi:ss') AS push_time,b.push_config_id, " +
                " CASE WHEN b.push_config_id>0 THEN 1 ELSE 0 END AS is_pushed," +
                " b.push_type,b.send_sequnce, to_char(b.send_base_time, 'YYYY-MM-DD hh24:mi') AS send_base_time  " +
                " FROM META_RPT_USER_FAVORITE a " +
                " LEFT JOIN META_RPT_PUSH_CONFIG b ON a.report_favorite_id = b.report_favorite_id " +
                " LEFT JOIN META_RPT_TAB_REPORT_CFG c ON a.report_id = c.report_id " +
                " LEFT JOIN meta_mag_user d ON d.user_id = c.User_Id " +
                " LEFT JOIN meta_dim_zone e ON d.zone_id = e.zone_id " +
                " LEFT JOIN META_DIM_USER_DEPT f ON d.dept_id = f.dept_CODE " +
                " WHERE a.user_id = ? and A.REPORT_ID = ?";
        return getDataAccess().queryForMap(sql, userId, reportId);
    }

    /**
     * 删除订阅信息
     * @param pushConfigId
     */
    public void deletePushConfig(int pushConfigId){
        String sql = "delete from META_RPT_PUSH_CONFIG where push_config_id = ?";
        getDataAccess().execUpdate(sql, pushConfigId);
    }

    /**
     * 删除用户收藏信息
     * @param reportFavoriteId
     */
    public void deleteUserFav(int reportFavoriteId){
        String sql = "delete from META_RPT_USER_FAVORITE where report_Favorite_Id = "+reportFavoriteId;
        getDataAccess().execUpdate(sql);
    }

    /**
     * 记录用户——报表操作日志
     * @param data {userId:,useZoneId:,useDeptId:,useStationId:,reportId:,operateType}
     */
    public void insertMetaRptUseLog(Map<String, Object> data){
        String sql = "INSERT INTO META_RPT_USE_LOG(USE_LOG_ID, USER_ID, USE_TIME, USE_ZONE_ID, USE_DEPT_ID, " +
                " USE_STATION_ID, REPORT_ID, OPERATE_TYPE) " +
                " VALUES(SEQ_RPT_USE_LOG_ID.NEXTVAL, ?, TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'), ?, ?, " +
                " ?, ?, ?)";
        getDataAccess().execUpdate(sql, data.get("userId"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                , data.get("useZoneId"), data.get("useDeptId")
                , data.get("useStationId"), data.get("reportId"), data.get("operateType"));
    }
}
