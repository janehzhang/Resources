package tydic.portalCommon;

import tydic.meta.common.MetaBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author: 程钰
 * @description    查询当前时间下的所以公告
 * @date: 12-4-17
 * @time: 下午3:11
 */

public class MetaMagNoticeDAO extends MetaBaseDAO{
    public List<Map<String,Object>> getNotice(){
        String sql = "SELECT T.NOTICE_ID,T.NOTICE_TITLE,T.NOTICE_CONTENT,T.INIT_DATE ORDER_TIME, t.pop_state," +
                "T.NOTICE_LEVEL,T.NOTICE_DISPLAY_ZONES FROM META_MAG_NOTICE T WHERE T.NOTICE_STATE=2 " +
                "AND (T.EFFECT_DATE IS NULL OR T.EFFECT_DATE<=SYSDATE) AND " +
                "(T.FAILURE_DATE IS NULL OR T.FAILURE_DATE>SYSDATE) ORDER BY T.NOTICE_LEVEL DESC,T.UPDATE_DATE DESC ";
        return getDataAccess().queryForList(sql);
    }
    public Map<String,Object> getNoticeById(long id){
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("SELECT T.NOTICE_ID,T.NOTICE_TITLE,T.NOTICE_CONTENT,TO_CHAR(T.INIT_DATE,'YYYY-MM-DD') INIT_DATE,");
    	buffer.append("T.NOTICE_LEVEL,T.NOTICE_DISPLAY_ZONES,T.AFFIX_PATH,NVL(T.AFFIX_NAME,'没有附件') AFFIX_NAME FROM META_MAG_NOTICE T WHERE T.NOTICE_STATE=2");
    	buffer.append(" AND T.NOTICE_ID=?");
    	return getDataAccess().queryForMap(buffer.toString(), new Object[]{id});
    }
    public List<Map<String,Object>> getNoticeAllMes(int zoneId){
    	String sql = "SELECT T.NOTICE_ID,T.NOTICE_TITLE,T.NOTICE_CONTENT,TO_CHAR(T.INIT_DATE,'yyyy-mm-dd') INIT_DATE, t.pop_state," +
        "T.NOTICE_LEVEL,T.NOTICE_DISPLAY_ZONES FROM META_MAG_NOTICE T" +
        " WHERE ','||T.NOTICE_DISPLAY_ZONES||',' LIKE '%,"+zoneId+",%' " +
        "AND (T.EFFECT_DATE IS NULL OR T.EFFECT_DATE<=SYSDATE) AND " +
        "(T.FAILURE_DATE IS NULL OR T.FAILURE_DATE>SYSDATE) AND T.NOTICE_STATE=2" +
        " ORDER BY T.NOTICE_LEVEL DESC,T.UPDATE_DATE DESC ";
    	return getDataAccess().queryForList(sql);
    }
    /**
     * @Title: getNoticeSql 
     * @Description:获取公告信息sql 
     * @param zoneId 公告所属地域
     * @return String   
     * @throws
     */
    public String getNoticeSql(int zoneId,int startSubscript,int endSubscript){
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("select * from (select /*+ first_rows */ ");
        buffer.append(" a.*, rownum rn from ( ");
    	buffer.append("SELECT T.NOTICE_ID,T.NOTICE_TITLE,T.NOTICE_CONTENT,TO_CHAR(T.INIT_DATE,'yyyy-mm-dd') INIT_DATE,t.pop_state,");
    	buffer.append(" T.NOTICE_LEVEL,T.NOTICE_DISPLAY_ZONES FROM META_MAG_NOTICE T WHERE");
    	buffer.append(" ','||T.NOTICE_DISPLAY_ZONES||',' LIKE '%,"+zoneId+",%'");
    	buffer.append(" AND (T.EFFECT_DATE IS NULL OR T.EFFECT_DATE<=SYSDATE) AND");
    	buffer.append(" (T.FAILURE_DATE IS NULL OR T.FAILURE_DATE>SYSDATE) AND T.NOTICE_STATE=2");
    	buffer.append(" ORDER BY T.NOTICE_LEVEL DESC,T.UPDATE_DATE DESC");
    	buffer.append("  ) a where rownum <= "+endSubscript+") where rn >= " + startSubscript);
    	return buffer.toString();
    }
    /**
     * @Title: getNoticeCount 
     * @Description: 获取公告信息的总数
     * @param zoneId
     * @return int   
     * @throws
     */
    public int getNoticeCount(int zoneId){
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("SELECT COUNT(ROWID) COUNTS");
    	buffer.append(" FROM META_MAG_NOTICE T WHERE");
    	buffer.append(" ','||T.NOTICE_DISPLAY_ZONES||',' LIKE '%,"+zoneId+",%'");
    	buffer.append(" AND (T.EFFECT_DATE IS NULL OR T.EFFECT_DATE<=SYSDATE) AND");
    	buffer.append(" (T.FAILURE_DATE IS NULL OR T.FAILURE_DATE>SYSDATE) AND T.NOTICE_STATE=2");
    	Map<String,Object> map = getDataAccess().queryForMap(buffer.toString());
    	return Integer.valueOf(""+map.get("COUNTS"));
    }
    /**
     * @Title: getNotices 
     * @Description: 根据sql获取公告信息列表
     * @param sql
     * @return List<Map<String,Object>>   
     * @throws
     */
    public List<Map<String,Object>> getNotices(String sql){
    	return getDataAccess().queryForList(sql);
    }
    
    
}
