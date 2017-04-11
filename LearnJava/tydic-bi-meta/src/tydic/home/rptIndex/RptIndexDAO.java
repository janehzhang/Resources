package tydic.home.rptIndex;

import tydic.meta.common.*;
import tydic.meta.web.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 报表首页DAO（业务DAO）
 * @date 12-4-9
 * -
 * @modify
 * @modifyDate -
 */
public class RptIndexDAO extends MetaBaseDAO{

    /**
     * 根据业务关键字直接搜索模型
     * @param kwd
     * @param issues
     * @param order
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryModels(String kwd,String[] issues,Order order,Page page){
        kwd = kwd.replaceAll("_","/_").replaceAll("%","/%");
        List<String> params = new ArrayList<String>();
        String sql = "SELECT A.ISSUE_ID,A.TABLE_ID,A.TABLE_ALIAS,A.AUDIT_TYPE," +
                "A.ISSUE_NOTE,A.IS_LISTING,B.DIM_COL_ALIAS,B.DIM_CNT,C.GDL_COL_ALIAS,C.GDL_CNT, " +
                "(SELECT to_char(ISSUE_TIME,'yyyy-mm-dd hh24:mi:ss') FROM META_RPT_MODEL_ISSUE_LOG WHERE ISSUE_ID=A.ISSUE_ID AND ISSUE_OPERATE=11) CREATE_TIME, " +
                "(SELECT COUNT(1) FROM META_RPT_TAB_REPORT_CFG WHERE ISSUE_ID=A.ISSUE_ID) INS_CNT " +
                "FROM META_RPT_MODEL_ISSUE_CONFIG A " +
                "LEFT JOIN (SELECT ISSUE_ID,wm_concat(COL_ALIAS) DIM_COL_ALIAS,COUNT(COL_ID) DIM_CNT " +
                "FROM META_RPT_MODEL_ISSUE_COLS WHERE COL_BUS_TYPE=0 GROUP BY ISSUE_ID  " +
                ") B ON A.ISSUE_ID=B.ISSUE_ID " +
                "LEFT JOIN (SELECT ISSUE_ID,wm_concat(COL_ALIAS) GDL_COL_ALIAS,COUNT(COL_ID) GDL_CNT " +
                "FROM META_RPT_MODEL_ISSUE_COLS WHERE COL_BUS_TYPE=1 GROUP BY ISSUE_ID  " +
                ") C ON A.ISSUE_ID=C.ISSUE_ID " +
                "WHERE 1=1 ";
        if(!"".equals(kwd)){
            sql += "AND (UPPER(A.TABLE_KEYWORD) LIKE UPPER(?) ESCAPE '/' OR UPPER(A.ISSUE_NOTE) LIKE UPPER(?) ESCAPE '/') " ;
            params.add("%"+kwd+"%");
            params.add("%"+kwd+"%");
        }
        if(issues!=null && issues.length>0){
            sql += "AND A.ISSUE_ID IN"+ SqlUtils.inParamDeal(issues);
        }
        sql +=
                " AND A.START_TIME<=sysdate " +
                " AND A.ISSUE_STATE=1 ";
        if(order!=null){
            sql += "ORDER BY "+order.getOrderField() + " " + order.getOrderMode();
            SessionManager.setAttribute(Order.SESSION_ORDER_KEY,order);
        }else{
            sql += "ORDER BY INS_CNT DESC,CREATE_TIME DESC";
        }
        if(page!=null){
            sql = SqlUtils.wrapPagingSql(sql,page);
        }
        return getDataAccess().queryForList(sql,params.toArray());
    }

    /**
     * 根据关键字搜索匹配到的模型字段
     * @param kwd 关键字
     * @return
     */
    public List<Map<String,Object>> queryModelFieldByKwd(String kwd){
        kwd = kwd.replaceAll("_","/_").replaceAll("%","/%");
        String sql = "SELECT DISTINCT A.COL_ALIAS,A.COL_ID,A.ISSUE_ID," +
                "(SELECT COUNT(1) FROM META_RPT_MODEL_ISSUE_COLS WHERE COL_ID=A.COL_ID) CNT " +
                "FROM META_RPT_MODEL_ISSUE_COLS A " +
                "LEFT JOIN META_RPT_MODEL_ISSUE_CONFIG B ON A.ISSUE_ID=B.ISSUE_ID " +
                "WHERE UPPER(A.COL_ALIAS) like UPPER(?) ESCAPE '/' AND A.COL_BUS_TYPE=1 AND B.ISSUE_STATE=1 " +
                "AND B.START_TIME<=sysdate " +
                "ORDER BY CNT DESC";
        return getDataAccess().queryForList(sql,"%"+kwd+"%");
    }

    /**
     * 查询首页可以展示的分类
     * 所有可用分类（由于首页无层级关系，因此当父和子同时被选时，只显示父）
     * @param childBubbling  当父被遮挡时子是否冒泡
     * @return
     */
    public List<Map<String,Object>> queryBusTypeByShowIndex(boolean childBubbling){
        String sql = "SELECT COL_TYPE_ID,COL_TYPE_NAME,COL_TYPE_ORDER FROM( " +
                "SELECT COL_TYPE_ID,COL_TYPE_NAME,PARENT_ID,COL_TYPE_ORDER,SHOW_INDEXPAGE_FLAG FROM " +
                "META_RPT_MODEL_COL_BUS_TYPE CONNECT BY PRIOR COL_TYPE_ID=PARENT_ID START WITH PARENT_ID=0 " +
                (childBubbling ? "" : "AND SHOW_INDEXPAGE_FLAG=1")+
                ") A WHERE A.PARENT_ID NOT IN(SELECT COL_TYPE_ID FROM META_RPT_MODEL_COL_BUS_TYPE CONNECT BY " +
                "PRIOR COL_TYPE_ID=PARENT_ID START WITH PARENT_ID=0 AND SHOW_INDEXPAGE_FLAG=1) " +
                "AND A.SHOW_INDEXPAGE_FLAG=1 ORDER BY A.COL_TYPE_ORDER ";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 查询子分类（一级）
     * @param typeId
     * @return
     */
    public List<Map<String,Object>> querySubType(int typeId){
        String sql = "SELECT COL_TYPE_ID,COL_TYPE_NAME,PARENT_ID,COL_TYPE_ORDER,SHOW_INDEXPAGE_FLAG " +
                "FROM META_RPT_MODEL_COL_BUS_TYPE WHERE PARENT_ID=? AND SHOW_INDEXPAGE_FLAG=1 " +
                "ORDER BY COL_TYPE_ORDER";
        return getDataAccess().queryForList(sql,typeId);
    }

    /**
     * 获取分类
     * @param typeId
     * @return
     */
    public Map<String,Object> getBusType(int typeId){
        String sql = "SELECT COL_TYPE_ID,COL_TYPE_NAME,PARENT_ID,COL_TYPE_ORDER,SHOW_INDEXPAGE_FLAG " +
                "FROM META_RPT_MODEL_COL_BUS_TYPE WHERE COL_TYPE_ID=? ";
        return getDataAccess().queryForMap(sql, typeId);
    }

    /**
     * 根据业务分类以查询其下所有字段
     * 需要把子类字段递归到父类中
     * @param typeId 业务分类ID
     * @param isAll 是否递归展示所有子
     * @return
     */
    public List<Map<String,Object>> queryFieldsByBusType(int typeId,boolean isAll){
        String fieldSql = "SELECT DISTINCT B.PARENT_ID,C.COL_ALIAS,C.COL_ID,C.ISSUE_ID," +
                "(SELECT COUNT(1) FROM META_RPT_MODEL_ISSUE_COLS WHERE COL_ID=C.COL_ID) CNT FROM " ;
        if(isAll){
            fieldSql += "(SELECT COL_TYPE_ID,PARENT_ID FROM META_RPT_MODEL_COL_BUS_TYPE CONNECT BY PRIOR COL_TYPE_ID=PARENT_ID " +
                    "START WITH COL_TYPE_ID=?) B ";
        }else{
            fieldSql += "(SELECT COL_TYPE_ID,PARENT_ID FROM META_RPT_MODEL_COL_BUS_TYPE WHERE COL_TYPE_ID=?) B ";
        }
        fieldSql+= "LEFT JOIN META_RPT_MODEL_ISSUE_COLS C ON B.COL_TYPE_ID=C.COL_TYPE_ID " +
                "LEFT JOIN META_RPT_MODEL_ISSUE_CONFIG D ON C.ISSUE_ID=D.ISSUE_ID " +
                "WHERE C.COL_BUS_TYPE=1 " +
                "AND D.START_TIME<=sysdate " +
                "AND D.ISSUE_STATE=1 " +
                "ORDER BY CNT DESC";
        return getDataAccess().queryForList(fieldSql,typeId);
    }

    /**
     * 根据列ID集，查询所有与之匹配的报表（根据相似度排序）
     * @param colIds
     * @param fieldNum 字段数
     * @param page
     * @param authFilter 权限过滤
     * @return
     */
    public List<Map<String,Object>> queryApproximateRptByCols(String[] colIds,int fieldNum,Page page,boolean authFilter){
        int userId = SessionManager.getCurrentUserID();
        if(fieldNum==0){
            return new ArrayList<Map<String,Object>>();
        }
        int p = RptIndexConstant.APPROXIMATE_P;
        String sql = "SELECT A.REPORT_ID,A.USER_ID,A.REPORT_NAME,to_char(A.CREATE_TIME,'yyyy-mm-dd') CREATE_TIME," +
                "A.REPORT_KEYWORD,A.REPORT_NOTE,A.REPORT_STATE,A.ISSUE_ID,A.IS_LISTING,A.RIGHT_LEVEL," +
                "(SELECT COUNT(1) FROM META_RPT_USE_LOG WHERE REPORT_ID=A.REPORT_ID AND OPERATE_TYPE=31) OPEN_CNT," +
                "B.COL_CNT,C.S_COL_CNT," +
                "(B.COL_CNT*100/"+fieldNum+"-(C.S_COL_CNT-B.COL_CNT)/C.S_COL_CNT) " +
//                "CASE " +
//                "  WHEN C.S_COL_CNT>=("+cn+"*"+p+") THEN B.COL_CNT/"+cn+"-(C.S_COL_CNT-"+cn+")/C.S_COL_CNT " +
//                "  WHEN B.COL_CNT="+cn+" AND B.COL_CNT<=C.S_COL_CNT THEN 1 - (C.S_COL_CNT-B.COL_CNT)/(C.S_COL_CNT*"+p+") - ("+cn+"-B.COL_CNT)/("+cn+"*"+p+") " +
//                "  WHEN C.S_COL_CNT<"+cn+" THEN B.COL_CNT/"+cn+" - ("+cn+"-C.S_COL_CNT)/("+cn+"*"+p+") " +
//                "  ELSE B.COL_CNT/C.S_COL_CNT - (C.S_COL_CNT-"+cn+")/(C.S_COL_CNT*"+p+") " +
//                "END " +
                "APPROXIMATE " +
                "FROM META_RPT_TAB_REPORT_CFG A " +
                "INNER JOIN( " +
                "SELECT REPORT_ID,COUNT(COL_ID) COL_CNT  " +
                "FROM META_RPT_TAB_OUTPUT_CFG WHERE COL_ID IN"+ SqlUtils.inParamDeal(colIds) +
                "GROUP BY REPORT_ID " +
                ") B ON A.REPORT_ID=B.REPORT_ID " +
                "LEFT JOIN( " +
                "SELECT X.REPORT_ID,COUNT(X.COL_ID) S_COL_CNT  " +
                "FROM META_RPT_TAB_OUTPUT_CFG X,META_RPT_MODEL_ISSUE_COLS Y " +
                "WHERE X.COLUMN_ID=Y.COLUMN_ID AND Y.COL_BUS_TYPE=1 GROUP BY X.REPORT_ID " +
                ") C ON A.REPORT_ID=C.REPORT_ID WHERE 1=1 " ;
        List<Integer> pa = new ArrayList<Integer>();
        if(authFilter){
            if(!SessionManager.isCurrentUserAdmin()){
                sql += " AND (EXISTS(SELECT REPORT_ID FROM META_RPT_SHARE_LIST WHERE REPORT_ID=A.REPORT_ID AND USER_ID=?) OR " +
                        "EXISTS(SELECT REPORT_ID FROM META_RPT_USER_FAVORITE WHERE REPORT_ID=A.REPORT_ID AND USER_ID=?) OR " +
                        "A.USER_ID=? OR A.RIGHT_LEVEL=0)";
                pa.add(userId);
                pa.add(userId);
                pa.add(userId);
            }
        }
        sql += "AND A.REPORT_STATE=1 ORDER BY APPROXIMATE DESC,OPEN_CNT DESC";
        if(page!=null){
            sql = SqlUtils.wrapPagingSql(sql,page);
        }
        return getDataAccess().queryForList(sql,pa.toArray());
    }

    /**
     * 我创建的报表
     * @param userId
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryMyCreateRpt(int userId,Page page){
        String sql = "SELECT T.REPORT_ID,T.REPORT_NAME,T.REPORT_NOTE,T.IS_LISTING," +
                " to_char(T.CREATE_TIME,'yyyy-mm-dd') AS CREATE_TIME," +
                " decode(L.NUM, '', 0, L.NUM) OPEN_NUM" +
                " FROM META_RPT_TAB_REPORT_CFG T" +
                " LEFT JOIN (SELECT A.REPORT_ID, COUNT(A.USE_LOG_ID) AS NUM " +
                " FROM META_RPT_USE_LOG A WHERE A.OPERATE_TYPE =31" +
                " GROUP BY A.REPORT_ID) L ON T.REPORT_ID = L.REPORT_ID" +
                " WHERE T.USER_ID = ? AND T.REPORT_STATE=1 " +
                " ORDER BY T.CREATE_TIME DESC,L.NUM DESC";
        if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql,userId);
    }
    
    /**
     * 获取别人共享给自己的报表
     * @param userId 用户ID
     * @param page
     * @return
     */
    public List<Map<String,Object>> querySharingRpt(int userId,Page page){
        String sql = "SELECT A.REPORT_ID,A.USER_ID,A.REPORT_NAME,to_char(A.CREATE_TIME,'yyyy-mm-dd') CREATE_TIME," +
                "A.REPORT_KEYWORD,A.REPORT_NOTE,A.REPORT_STATE,A.ISSUE_ID," +
                "A.IS_LISTING,A.RIGHT_LEVEL,NVL(C.USER_NAMECN,'&nbsp;') USER_NAMECN " +
                "FROM META_RPT_TAB_REPORT_CFG A " +
                "LEFT JOIN META_RPT_SHARE_LIST B ON A.REPORT_ID=B.REPORT_ID " +
                "LEFT JOIN META_MAG_USER C ON A.USER_ID=C.USER_ID " +
                "WHERE B.USER_ID=? AND A.USER_ID!=? AND A.RIGHT_LEVEL=1 AND A.REPORT_STATE=1 " +
//                " AND B.SHARE_START_TIME>=sysdate AND B.SHARE_END_TIME<=sysdate "+
                "ORDER BY A.CREATE_TIME DESC ";
        if(page!=null){
            sql = SqlUtils.wrapPagingSql(sql,page);
        }
        return getDataAccess().queryForList(sql,userId,userId);
    }

    /**
     * 获取我收藏的报表
     * @param userId
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryMyFavRpt(int userId,Page page){
        String sql = "SELECT A.REPORT_ID,A.USER_ID,A.REPORT_NAME,to_char(A.CREATE_TIME,'yyyy-mm-dd') CREATE_TIME," +
                "A.REPORT_KEYWORD,A.REPORT_NOTE,A.REPORT_STATE,A.ISSUE_ID," +
                "A.IS_LISTING,A.RIGHT_LEVEL," +
                "to_char(B.FAVORITE_REPORT_TIME,'yyyy-mm-dd hh24:mi:ss') FAVORITE_REPORT_TIME " +
                "FROM META_RPT_TAB_REPORT_CFG A " +
                "LEFT JOIN META_RPT_USER_FAVORITE B ON A.REPORT_ID=B.REPORT_ID " +
                "WHERE B.USER_ID=? AND A.REPORT_STATE=1 " +
                "ORDER BY B.FAVORITE_REPORT_TIME DESC, A.CREATE_TIME DESC";
        if(page!=null){
            sql = SqlUtils.wrapPagingSql(sql,page);
        }
        return getDataAccess().queryForList(sql,userId);
    }

    /**
     * 获取我订阅的报表
     * @param userId
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryMyPushRpt(int userId,Page page){
        String sql = "SELECT A.REPORT_ID,A.USER_ID,A.REPORT_NAME,to_char(A.CREATE_TIME,'yyyy-mm-dd') CREATE_TIME," +
                "A.REPORT_KEYWORD,A.REPORT_NOTE,A.REPORT_STATE,A.ISSUE_ID," +
                "A.IS_LISTING,A.RIGHT_LEVEL," +
                "to_char(B.CREATE_TIME,'yyyy-mm-dd hh24:mi:ss') PUSH_TIME " +
                "FROM META_RPT_TAB_REPORT_CFG A " +
                "LEFT JOIN META_RPT_PUSH_CONFIG B ON A.REPORT_ID=B.REPORT_ID " +
                "WHERE B.USER_ID=? AND A.REPORT_STATE=1 " +
                "ORDER BY B.CREATE_TIME DESC, A.CREATE_TIME DESC";
        if(page!=null){
            sql = SqlUtils.wrapPagingSql(sql,page);
        }
        return getDataAccess().queryForList(sql,userId);
    }

    /**
     * 获取公共报表
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryPublicRpt(Page page){
        int userId = SessionManager.getCurrentUserID();
        String sql = "SELECT A.REPORT_ID,A.USER_ID,A.REPORT_NAME,to_char(A.CREATE_TIME,'yyyy-mm-dd') CREATE_TIME," +
                "A.REPORT_KEYWORD,A.REPORT_NOTE,A.REPORT_STATE,A.ISSUE_ID," +
                "A.IS_LISTING,A.RIGHT_LEVEL,NVL(B.USER_NAMECN,'&nbsp;') USER_NAMECN " +
                "FROM META_RPT_TAB_REPORT_CFG A " +
                "LEFT JOIN META_MAG_USER B ON A.USER_ID=B.USER_ID " +
                "WHERE A.USER_ID!=? AND A.REPORT_STATE=1 AND A.RIGHT_LEVEL=0 " +
                "ORDER BY A.CREATE_TIME DESC";
        if(page!=null){
            sql = SqlUtils.wrapPagingSql(sql,page);
        }
        return getDataAccess().queryForList(sql,userId);
    }


}
