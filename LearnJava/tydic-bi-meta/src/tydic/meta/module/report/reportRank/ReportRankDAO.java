package tydic.meta.module.report.reportRank;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 刘斌
 * @description 报表排名DAO
 * @date 2012-04-11
 */
public class ReportRankDAO extends MetaBaseDAO {

    /**
     * 报表打开排名
     * @param queryData
     * @param hideStations 不显示的岗位（IT厂商）
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryOpenRank(Map<String, Object> queryData, String hideStations, Page page){
        String keyWord = Convert.toString(queryData.get("keyWord"));
        String sql = "SELECT A.REPORT_ID,A.REPORT_NAME,A.REPORT_NOTE,C.USER_NAMECN, " +
                " TO_CHAR(A.CREATE_TIME, 'YYYY-MM-DD hh24:mi:ss') AS CREATE_TIME, " +
                " COUNT(C1.USER_ID) AS COUNT " +
                " FROM   META_RPT_TAB_REPORT_CFG A " +
                " LEFT   JOIN META_RPT_USE_LOG B " +
                " ON     A.REPORT_ID = B.REPORT_ID " +
                " AND    B.OPERATE_TYPE = 31 " +
                " LEFT   JOIN META_MAG_USER C ON A.USER_ID = C.USER_ID ";
        sql = sql +" LEFT JOIN META_MAG_USER C1 ON B.USER_ID = C1.USER_ID ";
        if(hideStations!=null&&!hideStations.equals("")){
            sql = sql + " AND C1.STATION_ID NOT IN (";
            String[] stations=hideStations.contains(",")?hideStations.split(","):new String[]{hideStations};
            for(int i=0;i<stations.length;i++){
                sql = sql + (i==stations.length-1?stations[i]:(stations[i]+","));
            }
            sql= sql + ") ";
        }
        sql = sql +" WHERE a.report_state <> -1 ";
        if(!keyWord.equals("")){
            sql = sql + " and a.report_name LIKE ? ESCAPE '/' OR a.report_note LIKE ? ESCAPE '/' ";
        }

        sql = sql +
                " GROUP  BY A.REPORT_ID,A.REPORT_NAME,A.REPORT_NOTE,C.USER_NAMECN,A.CREATE_TIME,B.REPORT_ID " +
                " ORDER  BY COUNT DESC, CREATE_TIME DESC,REPORT_NAME";
        //分页包装
        if(page != null){
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        if(!keyWord.equals("")){
            return getDataAccess().queryForList(sql, "%"+keyWord+"%", "%"+keyWord+"%");
        }else{
            return getDataAccess().queryForList(sql);
        }
    }

    /**
     * 根据报表ID取报表打开记录
     * @param reportId
     * @param hideStations 不显示的岗位（IT厂商）
     * @return
     */
    public List<Map<String, Object>> queryForOpenDetail(int reportId, String hideStations, Page page){
        String sql = "SELECT B.USER_NAMECN,C.ZONE_NAME,D.DEPT_NAME,E.STATION_NAME," +
                " TO_CHAR(A.USE_TIME, 'YYYY-MM-DD hh24:mi:ss') AS USE_TIME " +
                " FROM META_RPT_USE_LOG A " +
                " LEFT JOIN META_MAG_USER B ON A.USER_ID = B.USER_ID " +
                " LEFT JOIN META_DIM_ZONE C ON C.ZONE_ID = A.USE_ZONE_ID " +
                " LEFT JOIN META_DIM_USER_DEPT D ON D.DEPT_CODE = A.USE_DEPT_ID " +
                " LEFT JOIN META_DIM_USER_STATION E ON A.USE_STATION_ID = E.STATION_CODE " +
                " WHERE A.OPERATE_TYPE = 31 AND a.report_id = ? ";
        if(hideStations!=null&&!hideStations.equals("")){
            sql = sql + " AND B.STATION_ID NOT IN (";
            String[] stations=hideStations.contains(",")?hideStations.split(","):new String[]{hideStations};
            for(int i=0;i<stations.length;i++){
                sql = sql + (i==stations.length-1?stations[i]:(stations[i]+","));
            }
            sql= sql + ") ";
        }
        sql =  sql + " ORDER BY USE_TIME DESC ";

        //分页包装
        if(page != null){
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql, reportId);
    }

}
