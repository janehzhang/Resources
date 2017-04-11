package tydic.home.myCenter.myCreate;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.mag.user.UserConstant;
import tydic.meta.sys.code.CodeManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 刘斌
 * @description 我创建的报表DAO
 * @date 2012-04-11
 */
public class MyCreateDAO extends MetaBaseDAO {

    /**
     * 取得我创建的报表
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryMyCreateReport(Map<String, Object> queryData, Page page){
        List<Object> param = new ArrayList<Object>();
        String keyWord = Convert.toString(queryData.get("keyWord"));
        param.add(Convert.toInt(queryData.get("userId"),0));
        String sql = "SELECT a.report_id,a.report_name,a.REPORT_STATE," +
                " TO_CHAR(a.create_time, 'YYYY-MM-DD hh24:mi:ss') as create_time," +
                " COUNT(DISTINCT B.USE_LOG_ID) AS count_open," +
                " COUNT(DISTINCT C.COMMENT_ID) AS count_comment, round(avg(d.grade), 1) AS grade " +
                " FROM meta_rpt_tab_report_cfg a " +
                " LEFT JOIN META_RPT_USE_LOG b ON a.report_id = b.report_id AND b.operate_type = 31 " +
                " LEFT JOIN META_RPT_COMMENT c ON a.report_id = c.report_id AND c.comment_type = 0 " +
                " LEFT JOIN META_RPT_USER_GRADE d ON a.report_id = d.report_id " +
                " WHERE a.REPORT_STATE <> -1 and a.user_id = ? ";
        if(!keyWord.equals("")){
            sql = sql + " AND  a.report_name LIKE ?   ";
            param.add(SqlUtils.allLikeBindParam(keyWord));
        }
        sql = sql + " GROUP BY a.report_id,a.report_name,a.REPORT_STATE,a.create_time ORDER BY a.REPORT_STATE desc,a.create_time DESC, a.report_id DESC, a.report_name";
        //分页包装
        if(page != null){
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql, param.toArray());

    }

    /**
     * 根据报表ID取报表信息
     * @param reportId
     * @return
     */
    public Map<String, Object> queryReportByReportId(int reportId){
        String sql = "SELECT REPORT_ID, USER_ID, REPORT_NAME, CREATE_TIME, START_DATE, " +
                " EFFECT_TIME, REPORT_KEYWORD, REPORT_SQL, REPORT_NOTE, REPORT_TYPE_ID, " +
                " REPORT_STATE, ISSUE_ID, IS_LISTING, REPORT_TRANS_CODE_SQL, DATA_SOURCE_ID, " +
                " RIGHT_LEVEL, DATA_TRANS_DIMS, DATA_TRANS_GDLS, DATA_TRANS_GDL_COLNAME FROM META_RPT_TAB_REPORT_CFG " +
                " WHERE REPORT_ID = ?";
        return getDataAccess().queryForMap(sql, reportId);
    }

    /**
     * 查询推荐用户
     * @param condtions
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryRefUser(Map<String, Object> condtions, Page page){
        String deptId =  "";
     	String stationId = "";
     	String zoneId= "";
     	String deptIdSql = "",stationSql = "",zoneSql="";
     	if(condtions != null &&  condtions.get("deptId") != null){
     		deptId = condtions.get("deptId").toString();
     		deptIdSql  = " AND A.DEPT_ID IN (" + deptId + ")";
     	}
     	if(condtions != null &&  condtions.get("stationId") != null){
     		stationId = condtions.get("stationId").toString();
     		stationSql  = " AND A.STATION_ID IN (" + stationId + ")";
     	}
     	if(condtions != null &&  condtions.get("zoneId") != null){
     		zoneId =  condtions.get("zoneId").toString();
     		zoneSql = "((SELECT b.zone_id,level zone_level  FROM META_DIM_ZONE B START WITH B.zone_id in ("+ zoneId + ") CONNECT BY PRIOR B.ZONE_ID = B.ZONE_PAR_ID " +
                        ")) E,";
     	}
        StringBuffer sql = new StringBuffer(
                "SELECT DISTINCT CASE WHEN RPTSL.USER_ID > 0 THEN 1 ELSE 0 END ISCHECKED,A.USER_ID, A.USER_EMAIL, A.USER_PASS, A.USER_NAMECN, A.STATE, "
                + "A.USER_MOBILE, A.STATION_ID, A.ADMIN_FLAG, A.HEAD_SHIP,TO_CHAR(CREATE_DATE,'YYYY-MM-DD HH24:MI:SS') CREATE_DATE, "
                + "A.USER_NAMEEN, A.OA_USER_NAME, A.DEPT_ID, A.ZONE_ID, A.USER_SN, "
                + "A.VIP_FLAG, A.GROUP_ID, B.ZONE_NAME, C.DEPT_NAME,D.STATION_NAME,E.zone_level "
                + "FROM " +  zoneSql+ "META_MAG_USER A " +
                        " LEFT   JOIN META_RPT_SHARE_LIST RPTSL " +
                        " ON     A.USER_ID = RPTSL.USER_ID AND RPTSL.Report_Id=" + Convert.toInt(condtions.get("reportId"),0) + " " +
                        " LEFT JOIN META_DIM_ZONE B ON A.ZONE_ID=B.ZONE_ID "
                + "LEFT JOIN META_DIM_USER_DEPT C ON A.DEPT_ID=C.DEPT_CODE LEFT JOIN META_DIM_USER_STATION D ON D.STATION_CODE=A.STATION_ID "
                + "WHERE A.USER_ID<>" + UserConstant.ADMIN_USERID +"AND A.STATE <> 2 AND E.ZONE_ID = A.ZONE_ID" + deptIdSql + stationSql);

        List<Object> params = new ArrayList<Object>();
        if (condtions != null && condtions.get("userName") != null
            && !condtions.get("userName").toString().equals("")) {// 姓名
            sql.append(" AND A.USER_NAMECN LIKE  "+SqlUtils.allLikeParam(Convert.toString(condtions.get("userName")).trim()) );
            //params.add("%" + Convert.toString(condtions.get("userName")).trim() + "%");
        }
        if (condtions != null && condtions.get("userState") != null
            && !String.valueOf(condtions.get("userState")).equals("-1")) {// 状态
            sql.append(" AND A.STATE=? ");
            params.add(Integer.parseInt(String.valueOf(condtions
                    .get("userState"))));
        }
        if (condtions != null && condtions.get("dimUserState")!=null){
            sql.append(" AND A.STATE ="+Integer.parseInt(condtions.get("dimUserState").toString()) + " ");
        }
        if (condtions != null && condtions.get("userId") != null) {// 用户ID
            sql.append(" AND A.USER_ID=? ");
            params.add(Integer
                    .parseInt(String.valueOf(condtions.get("userId"))));
        }
        if (condtions != null && condtions.get("mguserId") != null) {// 用户ID
            sql.append(" AND A.USER_ID !=? ");
            params.add(Integer
                    .parseInt(String.valueOf(condtions.get("mguserId"))));
        }
        String pageSql = sql.toString();
        pageSql += " ORDER BY ISCHECKED DESC,ZONE_LEVEL,ZONE_ID,STATE DESC";
        // 分页包装
        if (page != null) {
            pageSql = SqlUtils.wrapPagingSql(pageSql, page);
        }
        List<Map<String, Object>> rtn = getDataAccess().queryForList(pageSql, params.toArray());
        if(rtn!=null&&rtn.size()>0){
            for(Map<String,Object> map:rtn){
                map.put("STATE_NAME", CodeManager.getName(UserConstant.META_MAG_USER_STATE, MapUtils.getString(map, "STATE")));
            }
        }
        return rtn;
    }

    /**
     * 根据报表ID修改报表的分享状态
     * @param rightLevel
     * @param reportId
     */
    public void updateRightLevelByReportId(int rightLevel, int reportId){
        String sql = "update META_RPT_TAB_REPORT_CFG set RIGHT_LEVEL = ? where REPORT_ID = ?";
        getDataAccess().execUpdate(sql, rightLevel, reportId);
    }


    /**
     * 根据报表ID查询对应的分享人员，并组合成字符串
     * @param reportId
     * @return
     */
    public List<Map<String, Object>> queryShareUserStr(int reportId){
        String sql = "SELECT b.user_namecn || '--' || c.zone_name || '--' || d.dept_name || '--' || e.station_name AS ustr,a.user_id " +
                " FROM META_RPT_SHARE_LIST a " +
                " LEFT JOIN meta_mag_user b ON a.user_id = b.user_id " +
                " LEFT JOIN meta_dim_zone c ON c.zone_id = b.zone_id " +
                " LEFT JOIN META_DIM_USER_DEPT d ON b.dept_id = d.dept_CODE " +
                " LEFT JOIN META_DIM_USER_STATION e ON e.station_CODE = b.station_id " +
                " WHERE a.report_id = "+reportId;
        return getDataAccess().queryForList(sql);
    }

}
