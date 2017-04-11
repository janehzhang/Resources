package tydic.meta.module.mag.notice;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.*;
import tydic.meta.sys.code.CodeManager;
import tydic.meta.web.session.SessionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author: 程钰
 * @description 公告查询相关dao
 * @date: 12-3-21
 * @time: 下午2:49
 */

public class NoticeDAO extends MetaBaseDAO{
    /**
     * 按条件查询出相应的公告
     * @param queryData 查询条件
     * @param page  分页条件
     * @return  查询结果
     */
    public List<Map<String,Object>> queryNotice(Map<String,Object> queryData, Page page){
        String createId=MapUtils.getString(queryData, "createId", null);
        StringBuffer sql = new StringBuffer("SELECT T.NOTICE_ID,T.NOTICE_TITLE, " +
                "T.NOTICE_TYPE,T.NOTICE_CONTENT,T.NOTICE_LEVEL,T.NOTICE_STATE, " +
                "TO_CHAR(T.UPDATE_DATE,'YYYY-MM-DD HH24:MI:SS') UPDATE_DATE,T.NOTICE_USER,A.USER_NAMECN,NOTICE_DISPLAY_ZONES, " +
                "TO_CHAR(T.INIT_DATE,'YYYY-MM-DD HH24:MI:SS') INIT_DATE,TO_CHAR(T.FAILURE_DATE,'YYYY-MM-DD') FAILURE_DATE, " +
                "TO_CHAR(T.EFFECT_DATE,'YYYY-MM-DD') EFFECT_DATE,T.NOTICE_DISPLAY_ZONE,T.AFFIX_PATH,NVL(T.AFFIX_NAME, '没有附件') AFFIX_NAME FROM META_MAG_NOTICE T LEFT JOIN META_MAG_USER A ON T.NOTICE_USER=A.USER_ID WHERE T.NOTICE_USER='"+createId+"' ");
        List params = new ArrayList();
        if(queryData.containsKey("noticeTitle") && queryData.get("noticeTitle")!= null
                && !"".endsWith(queryData.get("noticeTitle").toString())){
            sql.append("AND T.NOTICE_TITLE LIKE "+SqlUtils.allLikeParam(queryData.get("noticeTitle").toString()));
//            params.add("%"+queryData.get("noticeTitle")+"%");
        }
        if(queryData.containsKey("noticeLevel") && queryData.get("noticeLevel")!= null
                && !"".endsWith(queryData.get("noticeLevel").toString())){
            sql.append("AND T.NOTICE_LEVEL = ? ");
            params.add(queryData.get("noticeLevel"));
        }
        if(queryData.containsKey("noticeState") && queryData.get("noticeState")!=null
                &&!"".equals(queryData.get("noticeState").toString())){
            sql.append("AND T.NOTICE_STATE = ? ");
            params.add(queryData.get("noticeState"));
        }
        sql.append("ORDER BY T.UPDATE_DATE DESC ");
        String pageSql = sql.toString();
        //分页包装
        if(page!=null){
            pageSql= SqlUtils.wrapPagingSql(pageSql, page);
        }
        List<Map<String,Object>> rs= getDataAccess().queryForList(pageSql, params.toArray());
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("LEVEL_NAME", CodeManager.getName(NoticeConstant.META_MAG_CODE_NOTICE_LEVEL, MapUtils.getString(map, "NOTICE_LEVEL")));
                map.put("IS_SHOW",CodeManager.getName(NoticeConstant.META_MAG_CODE_NOTICE_IS_SHOW,MapUtils.getString(map, "NOTICE_STATE")));
                map.put("NOTICE_FUNCTION",CodeManager.getName(NoticeConstant.META_MAG_CODE_NOTICE_FUNCITON,MapUtils.getString(map,"NOTICE_TYPE")));
            }
        }
        return rs;
    }

    
    /**
     * 按条件查询出相应的公告
     * @param queryData 查询条件
     * @param page  分页条件
     * @return  查询结果
     */
    public List<Map<String,Object>> queryNotice1(Map<String,Object> queryData, Page page){

        StringBuffer sql = new StringBuffer("SELECT T.NOTICE_ID,T.NOTICE_TITLE, " +
                "T.NOTICE_TYPE,T.NOTICE_CONTENT,T.NOTICE_LEVEL,T.NOTICE_STATE, " +
                "TO_CHAR(T.UPDATE_DATE,'YYYY-MM-DD HH24:MI:SS') UPDATE_DATE,T.NOTICE_USER,A.USER_NAMECN,NOTICE_DISPLAY_ZONES, " +
                "TO_CHAR(T.INIT_DATE,'YYYY-MM-DD HH24:MI:SS') INIT_DATE,TO_CHAR(T.FAILURE_DATE,'YYYY-MM-DD') FAILURE_DATE, " +
                "TO_CHAR(T.EFFECT_DATE,'YYYY-MM-DD') EFFECT_DATE,T.NOTICE_DISPLAY_ZONE,T.AFFIX_PATH,NVL(T.AFFIX_NAME, '没有附件') AFFIX_NAME FROM META_MAG_NOTICE T LEFT JOIN META_MAG_USER A ON T.NOTICE_USER=A.USER_ID WHERE  T.NOTICE_STATE<>0 ");
        List params = new ArrayList();
        if(queryData.containsKey("noticeTitle") && queryData.get("noticeTitle")!= null
                && !"".endsWith(queryData.get("noticeTitle").toString())){
            sql.append("AND T.NOTICE_TITLE LIKE "+SqlUtils.allLikeParam(queryData.get("noticeTitle").toString()));
//            params.add("%"+queryData.get("noticeTitle")+"%");
        }
        if(queryData.containsKey("noticeLevel") && queryData.get("noticeLevel")!= null
                && !"".endsWith(queryData.get("noticeLevel").toString())){
            sql.append("AND T.NOTICE_LEVEL = ? ");
            params.add(queryData.get("noticeLevel"));
        }
        if(queryData.containsKey("noticeState") && queryData.get("noticeState")!=null
                &&!"".equals(queryData.get("noticeState").toString())){
            sql.append("AND T.NOTICE_STATE = ? ");
            params.add(queryData.get("noticeState"));
        }
        sql.append("ORDER BY T.UPDATE_DATE DESC ");
        String pageSql = sql.toString();
        //分页包装
        if(page!=null){
            pageSql= SqlUtils.wrapPagingSql(pageSql, page);
        }
        List<Map<String,Object>> rs= getDataAccess().queryForList(pageSql, params.toArray());
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("LEVEL_NAME", CodeManager.getName(NoticeConstant.META_MAG_CODE_NOTICE_LEVEL, MapUtils.getString(map, "NOTICE_LEVEL")));
                map.put("IS_SHOW",CodeManager.getName(NoticeConstant.META_MAG_CODE_NOTICE_IS_SHOW,MapUtils.getString(map, "NOTICE_STATE")));
                map.put("NOTICE_FUNCTION",CodeManager.getName(NoticeConstant.META_MAG_CODE_NOTICE_FUNCITON,MapUtils.getString(map,"NOTICE_TYPE")));
            }
        }
        return rs;
    }

    /**
     * 新增一条公告
     * @param data
     * @return
     */
    public boolean insertNotice(Map<String, Object> data){
        String insertSql = "INSERT INTO META_MAG_NOTICE(NOTICE_ID,NOTICE_TITLE, " +
                "NOTICE_TYPE,NOTICE_CONTENT,NOTICE_LEVEL,NOTICE_STATE, " +
                "UPDATE_DATE,INIT_DATE,NOTICE_DISPLAY_ZONES,NOTICE_USER,EFFECT_DATE,FAILURE_DATE,NOTICE_DISPLAY_ZONE,AFFIX_PATH,AFFIX_NAME) VALUES (?,?,?,?,?,?,SYSDATE,SYSDATE,?,?,TO_DATE(?,'YYYY-MM-DD'),TO_DATE(?,'YYYY-MM-DD'),?,?,?) ";
        Long pk = queryForNextVal("SEQ_MAG_NOTICE_ID");
        Object [] params = {
                pk,MapUtils.getString(data,"noticeTitle",null),
                MapUtils.getString(data,"noticeType","1"),
                MapUtils.getString(data,"noticeContent",null),
                MapUtils.getString(data,"noticeLevel","1"),
                MapUtils.getString(data,"noticeState","0"),
                MapUtils.getString(data,"noticeDisplayZones",null),
                MapUtils.getString(data,"noticeUser", SessionManager.getCurrentUserID()+"") ,
                MapUtils.getString(data,"effectDate",null) ,
                MapUtils.getString(data,"failureDate",null),
                null,
                MapUtils.getString(data,"iconUrl",null),
                MapUtils.getString(data,"affixName",null)	
        };
      
        return getDataAccess().execNoQuerySql(insertSql,params);
    }

    /**
     * 根据公告ID 查询公告信息
     * @param noticeId
     * @return
     */
    public List<Map<String,Object>> queryNoticeById(Integer noticeId[]){
        String sql = "SELECT T.NOTICE_ID,T.NOTICE_TITLE, " +
                "T.NOTICE_TYPE,T.NOTICE_CONTENT,T.NOTICE_LEVEL,T.NOTICE_STATE, " +
                "TO_CHAR(T.UPDATE_DATE,'YYYY-MM-DD HH24:MI:SS') UPDATE_DATE,T.NOTICE_USER,A.USER_NAMECN,NOTICE_DISPLAY_ZONES, " +
                "TO_CHAR(T.INIT_DATE,'YYYY-MM-DD HH24:MI:SS') INIT_DATE,TO_CHAR(T.FAILURE_DATE,'YYYY-MM-DD') FAILURE_DATE, " +
                "TO_CHAR(T.EFFECT_DATE,'YYYY-MM-DD') EFFECT_DATE,T.NOTICE_DISPLAY_ZONE FROM META_MAG_NOTICE T LEFT JOIN META_MAG_USER A ON T.NOTICE_USER=A.USER_ID WHERE NOTICE_ID IN ( " +
                Common.join(noticeId,",")+")";
        List<Map<String,Object>> rs= getDataAccess().queryForList(sql);
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("LEVEL_NAME", CodeManager.getName(NoticeConstant.META_MAG_CODE_NOTICE_LEVEL, MapUtils.getString(map, "NOTICE_LEVEL")));
                map.put("IS_SHOW",CodeManager.getName(NoticeConstant.META_MAG_CODE_NOTICE_IS_SHOW,MapUtils.getString(map, "NOTICE_STATE")));
                map.put("NOTICE_FUNCTION",CodeManager.getName(NoticeConstant.META_MAG_CODE_NOTICE_FUNCITON,MapUtils.getString(map,"NOTICE_TYPE")));
            }
        }
        return rs;
    }
    /**
     * 删除一天公告
     * @param noticeIds 被删除的系统公告ID
     * @return
     * @throws Exception
     */
    public int deleteNoticeByNoticeIds(int[] noticeIds) throws Exception{
        if(noticeIds != null && noticeIds.length > 0){
            StringBuffer sql = new StringBuffer("DELETE FROM META_MAG_NOTICE WHERE NOTICE_ID IN (");
            for(int i = 0; i < noticeIds.length; i++){
                sql.append(noticeIds[i]);
                if(i != noticeIds.length - 1){
                    sql.append(",");
                }
            }
            sql.append(")");
            return getDataAccess().execUpdate(sql.toString());
        } else{
            return -1;
        }
    }
    /**
     * 修改公告
     * @param data 修改后的信息
     * @return
     * @throws Exception
     */
    public boolean updateNotice(Map<?, ?> data){
        String sql = "UPDATE META_MAG_NOTICE SET NOTICE_TITLE=?, " +
                "NOTICE_TYPE=?,NOTICE_CONTENT=?,NOTICE_LEVEL=?,NOTICE_STATE=?, " +
                "UPDATE_DATE=SYSDATE,NOTICE_DISPLAY_ZONES=?,EFFECT_DATE=TO_DATE(?,'YYYY-MM-DD'), " +
                "FAILURE_DATE=TO_DATE(?,'YYYY-MM-DD'),AFFIX_PATH=?,AFFIX_NAME=? WHERE NOTICE_ID=?";
        Object[] porParams = {
            MapUtils.getString(data,"noticeTitle",null),MapUtils.getInteger(data,"noticeType",1),
            MapUtils.getString(data,"noticeContent",null),MapUtils.getInteger(data,"noticeLevel",1),
            MapUtils.getInteger(data,"noticeState",0),MapUtils.getString(data,"noticeDisplayZones",null),
            MapUtils.getString(data,"effectDate",null) ,
                MapUtils.getString(data,"failureDate",null),
                MapUtils.getString(data,"iconUrl",null),
                MapUtils.getString(data,"affixName",null),
                MapUtils.getInteger(data,"noticeId",null)
        };
        return getDataAccess().execUpdate(sql,porParams)!= -1;
    }
    
    
    /**
     * 审核公告
     * @param data
     * @return
     * @throws Exception
     */
    public boolean spNotice(Map<String,Object> data){
        String sql = "update  META_MAG_NOTICE t  set t.notice_state=? , t.sp_idea=?,t.sp_userid=?,t.sp_username=?,t.sp_date=sysdate where t.notice_id=?";
        Object[] porParams = {
        		 MapUtils.getInteger(data,"spStatus",null),
        		 MapUtils.getString(data,"spIdea",null),
        		 MapUtils.getString(data,"createId",null),
        		 MapUtils.getString(data,"createName",null),
        		 MapUtils.getInteger(data,"noticeId",null)
        };
        return getDataAccess().execUpdate(sql,porParams)!= -1;
    }

    /**
     * 改变公告状态
     * @param noticeId  公告ID
     * @param noticeState  公告状态
     * @return  修改记录
     */
    public boolean noticeStateCtrlr(String noticeId, Integer noticeState){
        String sql = "UPDATE META_MAG_NOTICE SET NOTICE_STATE = ? WHERE NOTICE_ID ='"+noticeId+"'";
        return getDataAccess().execUpdate(sql,noticeState)!= -1;
    }
}
