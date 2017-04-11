package tydic.meta.module.mag.favorite.report;

import tydic.meta.common.DateUtil;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.report.RptUserLogDAO;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 王春生
 * @description 收藏报表DAO
 * @date 12-3-29
 * -
 * @modify
 * @modifyDate -
 */
public class FavReportDAO extends MetaBaseDAO{

    /**
     * 根据收藏夹ID查询所有收藏报表
     * @param favId
     * @return
     */
    public List<Map<String,Object>> queryFavReportByFavId(int favId){
        String sql = "SELECT A.REPORT_FAVORITE_ID,A.REPORT_ID,A.FAVORITE_ID,A.USER_ID," +
                "to_char(A.FAVORITE_REPORT_TIME,'yyyy-mm-dd hh24:mi:ss') AS FAVORITE_REPORT_TIME,A.FAVORITE_REPORT_ORDER," +
                "B.REPORT_NAME,(SELECT COUNT(1) CNT FROM META_RPT_PUSH_CONFIG C WHERE C.REPORT_FAVORITE_ID=A.REPORT_FAVORITE_ID) CNT," +
                "D.AUDIT_TYPE " +
                "FROM META_RPT_USER_FAVORITE A "+
                "LEFT JOIN META_RPT_TAB_REPORT_CFG B ON A.REPORT_ID=B.REPORT_ID "+
                "LEFT JOIN META_RPT_DATA_AUDIT_CFG D ON D.ISSUE_ID=B.ISSUE_ID " +
                "WHERE A.FAVORITE_ID=? ORDER BY A.FAVORITE_REPORT_ORDER ASC";
        return getDataAccess().queryForList(sql,favId);
    }

    /**
     * 根据收藏夹ID查询所有收藏报表
     * @param favIds
     * @return
     */
    public List<Map<String,Object>> queryFavReportByFavIds(int[] favIds){
        String sql = "SELECT A.REPORT_FAVORITE_ID,A.REPORT_ID,A.FAVORITE_ID,A.USER_ID," +
                "to_char(A.FAVORITE_REPORT_TIME,'yyyy-mm-dd hh24:mi:ss') AS FAVORITE_REPORT_TIME,A.FAVORITE_REPORT_ORDER," +
                "B.REPORT_NAME,(SELECT COUNT(1) CNT FROM META_RPT_PUSH_CONFIG C WHERE C.REPORT_FAVORITE_ID=A.REPORT_FAVORITE_ID) CNT," +
                "D.AUDIT_TYPE " +
                "FROM META_RPT_USER_FAVORITE A "+
                "LEFT JOIN META_RPT_TAB_REPORT_CFG B ON A.REPORT_ID=B.REPORT_ID "+
                "LEFT JOIN META_RPT_DATA_AUDIT_CFG D ON D.ISSUE_ID=B.ISSUE_ID " +
                "WHERE A.FAVORITE_ID IN" + SqlUtils.inParamDeal(favIds) +
                " ORDER BY A.FAVORITE_REPORT_ORDER ASC";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 收藏报表
     * @param data
     * @return
     */
    public int insertFavoriteReport(Map<String,Object> data){
        String sql = "INSERT INTO META_RPT_USER_FAVORITE "
                + "(REPORT_FAVORITE_ID,REPORT_ID,FAVORITE_ID,USER_ID,FAVORITE_REPORT_TIME,FAVORITE_REPORT_ORDER) "
                + "SELECT ?,?,?,?,TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'),NVL(MAX(FAVORITE_REPORT_ORDER),1)+1 FROM META_RPT_USER_FAVORITE";
        List<Object> proParams = new ArrayList<Object>();
        long pk = queryForNextVal("SEQ_RPT_USER_FAVORITE_ID");
        proParams.add(pk);
        if(data.containsKey("objId")) proParams.add(Integer.parseInt(String.valueOf(data.get("objId"))));
        else proParams.add(0);
        if(data.containsKey("favoriteType")) proParams.add(Integer.parseInt(String.valueOf(data.get("favoriteType"))));
        else proParams.add(0);
        if(data.containsKey("userId")) proParams.add(Integer.parseInt(String.valueOf(data.get("userId"))));
        else proParams.add(0);
        proParams.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//时间
        int ret = getDataAccess().execUpdate(sql, proParams.toArray());
        if(ret>0){
            RptUserLogDAO logDAO = new RptUserLogDAO();
            logDAO.recordRptUserLog(pk,RptUserLogDAO.FAV);
        }
        return (int)pk;
    }

    /**
     * 收藏报表
     * @param rptId
     * @param typeId
     * @param userId
     * @return
     */
    public int insertFavoriteReport(int rptId,int typeId,int userId){
        String sql = "INSERT INTO META_RPT_USER_FAVORITE "
                + "(REPORT_FAVORITE_ID,REPORT_ID,FAVORITE_ID,USER_ID,FAVORITE_REPORT_TIME,FAVORITE_REPORT_ORDER) "
                + "SELECT ?,?,?,?,sysdate,NVL(MAX(FAVORITE_REPORT_ORDER),1)+1 FROM META_RPT_USER_FAVORITE";
        long pk = queryForNextVal("SEQ_RPT_USER_FAVORITE_ID");
        int ret = getDataAccess().execUpdate(sql,pk,rptId,typeId,userId);
        if(ret>0){
            RptUserLogDAO logDAO = new RptUserLogDAO();
            logDAO.recordRptUserLog(pk,RptUserLogDAO.FAV);
        }
        return (int)pk;
    }

    /**
     * 订阅报表
     * @param rptId
     * @param favId
     * @param userId
     * @param sendTime
     * @param sendSequnce
     * @param pushType
     * @return
     */
    public int insertPushRpt(int rptId,int favId,int userId,String sendTime,int sendSequnce,String pushType){
        int startNum = 11;	//初始时间增量
        Date sendBaseTime = DateUtil.getDateTimeByString(sendTime, "yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sendBaseTime);
        calendar.add(Calendar.DATE, (0-startNum));	//基准时间为发送时间减去初始时间增量天数
        sendTime = DateUtil.format(calendar.getTime(),"yyyy-MM-dd HH:mm:ss");
        String sql = "INSERT INTO META_RPT_PUSH_CONFIG(PUSH_CONFIG_ID,REPORT_ID," +
                " REPORT_FAVORITE_ID,USER_ID,PUSH_TYPE,SEND_SEQUNCE,SEND_BASE_TIME," +
                " SEND_TIME_ADD,CREATE_TIME) VALUES(?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,sysdate)";
        long pk = queryForNextVal("SEQ_RPT_PUSH_CONFIG_ID");
        boolean rs = getDataAccess().execNoQuerySql(sql,pk,
                rptId,favId,userId,pushType,sendSequnce,sendTime,startNum);
        if(rs){  //添加记录日志
            RptUserLogDAO logDAO = new RptUserLogDAO();
            logDAO.recordRptUserLog(rptId,RptUserLogDAO.PUSH);
        }
        return (int)pk;
    }

    /**
     * 修改订阅
     * @param rptId
     * @param favId
     * @param userId
     * @param sendTime
     * @param sendSequnce
     * @param pushType
     * @return
     */
    public int updatePushRpt(int rptId,int favId,int userId,String sendTime,int sendSequnce,String pushType){
        int startNum = 11;	//初始时间增量
        Date sendBaseTime = DateUtil.getDateTimeByString(sendTime, "yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sendBaseTime);
        calendar.add(Calendar.DATE, (0-startNum));	//基准时间为发送时间减去初始时间增量天数
        sendTime = DateUtil.format(calendar.getTime(),"yyyy-MM-dd HH:mm:ss");
        String sql = "UPDATE META_RPT_PUSH_CONFIG SET PUSH_TYPE=?,SEND_SEQUNCE=?," +
                " SEND_BASE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss') " +
                " WHERE REPORT_ID=? AND REPORT_FAVORITE_ID=?";
        boolean rs = getDataAccess().execNoQuerySql(sql,pushType,sendSequnce,sendTime,rptId,favId);
        if(rs){
            RptUserLogDAO logDAO = new RptUserLogDAO();
            logDAO.recordRptUserLog(rptId,RptUserLogDAO.MODIFY_PUSH);
        }
        return 0;
    }

    /**
     * 修改收藏报表的分类（主要是收藏分类树 拖动）
     * @param id
     * @param type
     * @return
     */
    public int updateFavoriteReportType(int id,int type){
        String sql = "UPDATE META_RPT_USER_FAVORITE SET FAVORITE_ID=? WHERE REPORT_FAVORITE_ID=?";
        return getDataAccess().execUpdate(sql,type,id);
    }

    /**
     * 删除订阅报表
     * @param id
     * @return
     */
    public int deletePushReport(int id){
        String dysql = "DELETE FROM META_RPT_PUSH_CONFIG WHERE PUSH_CONFIG_ID=?"; //删除订阅
        return getDataAccess().execUpdate(dysql,id);
    }

    /**
     * 删除收藏报表
     * @param id
     * @return
     */
    public int deleteFavoriteReport(int id){
        String sql = "DELETE FROM META_RPT_USER_FAVORITE WHERE REPORT_FAVORITE_ID=?";//删除收藏
        return getDataAccess().execUpdate(sql,id);
    }

    /**
     * 删除收藏时级联删除订阅
     * @param favId
     * @return
     */
    public int deleteFavPush(int favId){
        String dysql = "DELETE FROM META_RPT_PUSH_CONFIG WHERE REPORT_FAVORITE_ID=?"; //删除订阅
        return getDataAccess().execUpdate(dysql,favId);
    }

    /**
     * 返回某收藏夹某报表数量。用于验证
     * @param reportId
     * @param userId
     * @return
     */
    public int countFavoriteReportNum(int reportId,int userId){
        String sql = "SELECT COUNT(1) FROM META_MAG_REPORT_USER_FAVORITE WHERE USER_ID=? AND REPORT_ID=?";
        return getDataAccess().queryForInt(sql,userId,reportId);
    }

    /**
     * 修改收藏报表的排序值
     * @param id
     * @param ot true升，false降
     * @return
     */
    public int updateFavoriteReportOrder(int id,int favId,boolean ot){
        String sql = "SELECT A.REPORT_FAVORITE_ID,A.FAVORITE_REPORT_ORDER FROM META_RPT_USER_FAVORITE A WHERE A.REPORT_FAVORITE_ID=? " +
                "OR (A.FAVORITE_ID=? AND A.FAVORITE_REPORT_ORDER=(SELECT "+(ot?"MAX":"MIN")+"(FAVORITE_REPORT_ORDER) " +
                "FROM META_RPT_USER_FAVORITE WHERE FAVORITE_ID=? " +
                "AND FAVORITE_REPORT_ORDER"+(ot?"<":">")+"(SELECT FAVORITE_REPORT_ORDER FROM META_RPT_USER_FAVORITE WHERE REPORT_FAVORITE_ID=?)))";
        List<Map<String,Object>> datas = getDataAccess().queryForList(sql,id,favId,favId,id);
        String updateSql = "UPDATE META_RPT_USER_FAVORITE SET FAVORITE_REPORT_ORDER=? WHERE REPORT_FAVORITE_ID=?";
        if(datas.size()>1){
            int oid = 0;  //原ID
            int order = 0;//原排序ID
            int _order = 0;//交换排序ID
            List<Integer> _ids = new ArrayList<Integer>();//交换ID集
            for(Map<String,Object> map : datas){
                int mid = Integer.parseInt(String.valueOf(map.get("REPORT_FAVORITE_ID")));
                int morder = Integer.parseInt(String.valueOf(map.get("FAVORITE_REPORT_ORDER")));
                if(mid==id){
                    oid = mid;
                    _order = morder;
                }else{
                    _ids.add(mid);
                    order = morder;
                }
            }
            Object[][] proParamses=new Object[_ids.size()+1][2];
            proParamses[0][0] = order;
            proParamses[0][1] = oid;
            for(int x=0;x<_ids.size();x++){
                proParamses[x+1][0] = _order;
                proParamses[x+1][1] = _ids.get(x);
            }
            getDataAccess().execUpdateBatch(updateSql, proParamses);
            return 1;
        }
        return 0;
    }


}
