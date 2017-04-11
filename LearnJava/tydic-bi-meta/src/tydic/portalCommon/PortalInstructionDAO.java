package tydic.portalCommon;
/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 杨苏维
 * @description 作用:首页指标批示
 * @date 2012-03-30
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.web.session.SessionManager;

public class PortalInstructionDAO extends MetaBaseDAO {
	
	
	/**
	 *查询指标批示记录 
	 * 查询某条批示记录和所有回复记录
	 */
	public List<Map<String,Object>> queryInstructions(int instructionId,String zoneId){
		String sql = "SELECT ZONE_NAME,INSTRUCTION_ID,INSTRUCTION_INDEX_CD,INSTRUCTION_INDEX_NAME,INSTRUCTION_COMMENT," +
				"INSTRUCTION_LEVEL,REPLY_ID,USER_ID,USER_NAME,to_char(SUBMIT_DATE,'yyyy-mm-dd') SUBMIT_DATE,SUBMIT_DATE ORDER_TIME,DISPLAY_ZONES,DATA_DATE,nvl(to_char(INDEX_VALUE),'空值') INDEX_VALUE FROM META_PORTAL_INSTRUNCTION,META_DIM_ZONE" +
				" WHERE DISPLAY_ZONES = ZONE_CODE " +
				"AND DISPLAY_ZONES IN (SELECT ZONE_CODE  FROM META_DIM_ZONE A START WITH  A.ZONE_ID = ?   CONNECT BY PRIOR A.ZONE_ID = A.ZONE_PAR_ID)";
		      if(instructionId != -1 ){
		    	  sql= sql + " WHERE INSTRUCTION_ID=?" ;
		    	  return getDataAccess().queryForList(sql,zoneId,instructionId);
		      } 
		      sql = sql +" ORDER BY INSTRUCTION_LEVEL,SUBMIT_DATE";
		return getDataAccess().queryForList(sql,zoneId);
	}
	
	//插入回复数据
	public  boolean insertInstrucInfo(Map<String,Object> map){
		String sql = "INSERT INTO META_PORTAL_INSTRUNCTION T (T.INSTRUCTION_ID,T.INSTRUCTION_INDEX_CD, " +
                "T.INSTRUCTION_INDEX_NAME,T.INSTRUCTION_COMMENT,T.INSTRUCTION_LEVEL, " +
                "T.REPLY_ID,T.USER_ID,T.USER_NAME,T.SUBMIT_DATE,T.DISPLAY_ZONES,T.DATA_DATE,T.INDEX_VALUE) " +
				" VALUES(?,?,?,?,?,?,?,?,SYSDATE,?,?,?)";
                 if(map.containsKey("replyId") && map.get("replyId") != "" && !map.get("replyId").equals(null)){
                	 map.put("instructionLevel", queryInstructLevel(map.get("replyId").toString()) + 1) ;
                 }
		Object[] param = {queryForNextVal("seq_app_id"), MapUtils.getString(map,"instructionIndexCd",null),
                MapUtils.getString(map,"instructionIndexName",null),MapUtils.getString(map,"instructionComment",null),
                MapUtils.getInteger(map,"instructionLevel",1),MapUtils.getInteger(map,"replyId",null),
                MapUtils.getInteger(map,"userId",null),MapUtils.getString(map,"userName"),
                MapUtils.getString(map,"displayZones",null),MapUtils.getString(map,"dataDate",null),
                MapUtils.getInteger(map,"indexValue",null)};
		return getDataAccess().execNoQuerySql(sql, param);
	}
	
	//查询当前指标批示的最大等级
	public int queryInstructLevel(String id){
		String sql = "SELECT nvl(INSTRUCTION_LEVEL,1) FROM META_PORTAL_INSTRUNCTION WHERE INSTRUCTION_ID =? ";
		return getDataAccess().queryForInt(sql, id);
	}
	
	/**
	 *查询图形数据（天、周--星期一）
	 * 
	 */
	public List<Map<String,Object>>  queryChartData(String maxData,String minData,String indexCode,String localCode,String tabId){
		   String sql = "SELECT TAB_ID,MONTH_NO,DATE_NO,AREA_NAME,INDEX_NAME,VALUE2,VALUE4,VALUE6,VALUE8,ORDER_ID,VALUE12,VALUE11,VALUE10,VALUE9,VALUE7,VALUE5,VALUE3,VALUE1,INDEX_CD,LOCAL_CODE " +
		   		"  FROM META_PORTAL_INDEX_DATA WHERE DATE_NO <= ? AND DATE_NO >=? AND INDEX_CD=? AND LOCAL_CODE=? AND TAB_ID=?";
			return getDataAccess().queryForList(sql,maxData,minData,indexCode,localCode,tabId);
		}
	/**
	 * @Title: getChartMothData 
	 * @Description: 获取月图形数据
	 * @param maxData 最大月
	 * @param minData 最小月
	 * @param indexCode 指标CD
	 * @param localCode 本地网编码
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getChartMothData(String maxData,String minData,String indexCode,String localCode,String tabId){
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT TAB_ID,MONTH_NO,AREA_NAME,INDEX_NAME,VALUE2,VALUE4,VALUE6,VALUE8,ORDER_ID,VALUE12,VALUE11,VALUE10,");
		buffer.append(" VALUE9,VALUE7,VALUE5,VALUE3,VALUE1,INDEX_CD,LOCAL_CODE");
		buffer.append(" FROM META_PORTAL_INDEX_DATA WHERE MONTH_NO <= ? AND MONTH_NO >=? AND INDEX_CD=? AND LOCAL_CODE=? AND TAB_ID=?");
//		buffer.append("SELECT TAB_ID,MONTH_NO,AREA_NAME,INDEX_NAME,SUM(NVL(VALUE2,0)) VALUE2,SUM(NVL(VALUE4,0)) VALUE4,");
//		buffer.append("SUM(NVL(VALUE6,0)) VALUE6,SUM(NVL(VALUE8,0)) VALUE8,ORDER_ID,SUM(NVL(VALUE12,0)) VALUE12,");
//		buffer.append("SUM(NVL(VALUE11,0)) VALUE11,SUM(NVL(VALUE10,0)) VALUE10,");
//		buffer.append("SUM(NVL(VALUE9,0)) VALUE9,SUM(NVL(VALUE7,0)) VALUE7,SUM(NVL(VALUE5,0)) VALUE5,SUM(NVL(VALUE3,0)) VALUE3,");
//		buffer.append("SUM(NVL(VALUE1,0)) VALUE1,INDEX_CD,LOCAL_CODE");
//		buffer.append(" FROM META_PORTAL_INDEX_DATA WHERE MONTH_NO <= ? AND MONTH_NO >=? AND INDEX_CD=? AND LOCAL_CODE=? AND TAB_ID=?");
//		buffer.append(" GROUP BY TAB_ID,MONTH_NO,AREA_NAME,INDEX_NAME,ORDER_ID,INDEX_CD,LOCAL_CODE");
		return getDataAccess().queryForList(buffer.toString(),maxData,minData,indexCode,localCode,tabId);
	}
	/**
	 *tab_id，index_cd 
	 *查询指标的图形信息,有多少条图形和对应的颜色
	 */
	public List<Map<String,Object>> queryGraphInfo(String tabId,String indexCd){
		String sql = "SELECT B.*,C.*,A.TIME_INTERVAL_ID TIME_INTERVAL_IDS FROM META_PORTAL_INDEX_EXPLAIN A," +
		   " META_PORTAL_TREND_CHART B,META_PORTAL_TIME_INTERVAL C WHERE A.TAB_ID = B.TAB_ID AND A.INDEX_CD = B.INDEX_CD" +
		   " AND B.TIME_INTERVAL_ID = C.RULR_ID AND A.TAB_ID =? AND B.INDEX_CD =?";
		return getDataAccess().queryForList(sql,tabId,indexCd);
	}
	
	/**
	 *查询指标的时间规则
	 *@param 
	 *
	 */
	public int queryTimeInterval(String indexCd){
		StringBuffer sql = new StringBuffer("SELECT B.INTERVAL_VALUE" + 
          " FROM META_PORTAL_TREND_CHART A,META_PORTAL_TIME_INTERVAL B"  +
          " WHERE A.TIME_INTERVAL_ID = B.RULR_ID  AND A.INDEX_CD = ? ");
		return getDataAccess().queryForInt(sql.toString(), indexCd);
	}
	
    
    /**
     * 获取时段ID
     * @return
     */
    public String[] getTimeRodi(String indexCd,String tabId) {
        String sql = "SELECT TIME_INTERVAL_ID FROM meta_portal_index_explain WHERE INDEX_CD = ? AND TAB_ID=?";
        String timeId = getDataAccess().queryForString(sql.toString(), new Object[]{indexCd,tabId});
        return StringUtils.isEmpty(timeId)?new String[0]:timeId.split(",");
    }
    
    /**
     * 获取时段信息
     * @return
     */
    public List<Map<String, Object>> getTimeInfo(String timeId) {
        String sql = "SELECT RULR_ID,RULE_NAME,INTERVAL_VALUE FROM META_PORTAL_TIME_INTERVAL WHERE RULR_ID IN ("+timeId+")" +
        		" ORDER BY INTERVAL_VALUE";
        return StringUtils.isEmpty(timeId)?new ArrayList<Map<String,Object>>():getDataAccess().queryForList(sql);
    }
    
	
	
    //查询某个地市某日的数据
    public  List<Map<String, Object>> getOneAreaData(String dateNo,int tabId,int rptType,String indexCd,String localCode) {
    	String timeField ="A.DATE_NO";
    	if(rptType==2){
    		timeField = "A.MONTH_NO";
    		dateNo = dateNo.substring(0,6);
        }
    	String sql =  "SELECT * FROM META_PORTAL_INDEX_DATA A LEFT JOIN META_DIM_ZONE C ON A.LOCAL_CODE = C.ZONE_CODE WHERE "+timeField+" = ? AND A.TAB_ID=? AND A.INDEX_CD = ?" +
    		" AND A.LOCAL_CODE IN (SELECT B.ZONE_CODE  FROM META_DIM_ZONE B,META_DIM_ZONE C WHERE C.ZONE_ID = B.ZONE_PAR_ID AND C.ZONE_CODE = ?) ORDER BY c.dim_level, c.ORDER_ID, A.ORDER_ID ASC";
    	List<Map<String, Object>> list = getDataAccess().queryForList(sql,dateNo,tabId,indexCd,localCode);
		return list;
    }
	
    /**
     * 查询某个指标某个区域的下属地域值
     * @param dataNo indexcd  localCode
     *
     */
     public  List<Map<String,Object>> queryBg3(String dataNo,String indexCd,String localCode){
        StringBuffer sql = new StringBuffer("SELECT * FROM META_PORTAL_INDEX_DATA A LEFT JOIN META_DIM_ZONE C ON A.LOCAL_CODE = C.ZONE_CODE WHERE A.DATE_NO = ? AND A.INDEX_CD = ?" +
        		" AND A.LOCAL_CODE IN (SELECT B.ZONE_CODE  FROM META_DIM_ZONE B,META_DIM_ZONE C WHERE C.ZONE_ID = B.ZONE_PAR_ID AND C.ZONE_CODE = ?) ORDER BY c.dim_level, c.ORDER_ID, A.ORDER_ID ASC");
        
        return getDataAccess().queryForList(sql.toString(), dataNo,indexCd,localCode);
     }

     
     /**
 	 *后去某个批示下面的所有子节点
 	 *@param instructionId
 	 */
	public List<Map<String, Object>> queryAllChildrenInst(int instructionId) {
		 StringBuffer sql = new StringBuffer("SELECT INSTRUCTION_ID,INSTRUCTION_INDEX_CD,INSTRUCTION_INDEX_NAME,INSTRUCTION_COMMENT," +
				"INSTRUCTION_LEVEL,REPLY_ID,USER_ID,USER_NAME,to_char(SUBMIT_DATE,'yyyy-mm-dd hh24:mi:ss') SUBMIT_DATE,DISPLAY_ZONES,DATA_DATE,nvl(to_char(INDEX_VALUE),'空值') INDEX_VALUE" +
				" FROM META_PORTAL_INSTRUNCTION  START WITH  REPLY_ID =  ? CONNECT BY PRIOR INSTRUCTION_ID = REPLY_ID ORDER SIBLINGS BY  SUBMIT_DATE" );
		 
		return getDataAccess().queryForList(sql.toString(),instructionId);
	}
	/**
	 * @Title: getIntervalById 
	 * @Description: 根据ID获取时间段表信息
	 * @param id
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getIntervalById(int id){
		String sql = "SELECT I.* FROM META_PORTAL_TIME_INTERVAL I WHERE I.RULR_ID=?";
		return getDataAccess().queryForMap(sql, new Object[]{id});
	}
	
	/*********************************************************************************/
	/**
	 * 获取某人未读批示信息
	 */
	public List<Map<String,Object>> getWDInstructions(int zoneId,int userId){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT T.INSTRUCTION_ID,T.INSTRUCTION_INDEX_NAME,T.INSTRUCTION_COMMENT,T.USER_NAME,");
		buffer.append(" TO_CHAR(T.SUBMIT_DATE,'YYYY-MM-DD HH24:MI:SS') SUBMIT_DATE,T.USER_ID");
		buffer.append(" FROM META_PORTAL_INSTRUNCTION T WHERE NOT EXISTS");
		buffer.append(" (SELECT 1 FROM META_PORTAL_INSTRUNCTION_LOG Z WHERE Z.USER_ID = ?");
		buffer.append(" AND T.INSTRUCTION_ID = Z.INSTRUCTION_ID AND T.USER_ID = Z.USER_ID)");
		buffer.append(" AND (T.DISPLAY_ZONES IS NULL OR ','||T.DISPLAY_ZONES||',' LIKE '%,"+zoneId+",%')");
		buffer.append(" AND T.REPLY_ID IS NULL AND T.INSTRUCTION_LEVEL=1");
		return getDataAccess().queryForList(buffer.toString(),new Object[]{userId});
	}
	

	
	/***
	 *  add yanhd 获得未读批示条数
	 */
   public List<Map<String,Object>>	getPsSendList(int userId){
	     List<Object> proParams = new ArrayList<Object>();
	    String sql = "SELECT t.* FROM META_PORTAL_PS t where t.isread ='0' and  ','||t.receiver_ids||','  LIKE ? ESCAPE '/'" +
	    		"  order by send_time desc";
	    proParams.add(SqlUtils.allLikeBindParam(","+String.valueOf(userId)+","));	
		return getDataAccess().queryForList(sql,proParams.toArray());
   }
   /**
    * 
    * @Description: 获取批示的总数 
    * @throws
    */
/*	public int getPsCount(int userId) {
		String sql="SELECT COUNT(ROWID) COUNTS FROM META_PORTAL_PS t where t.isread ='0' and t.receiver_ids='"+userId+"' order by send_time desc";
    	Map<String,Object> map = getDataAccess().queryForMap(sql);
    	return Integer.valueOf(""+map.get("COUNTS"));
	}*/
   
   
    //add by quanxia
	//获取批示列表
    public List<Map<String, Object>> getPsList(
            Map<String, Object> queryData, Page page) {
    	Map<String,Object> userMap = SessionManager.getCurrentUser();
    	String userId =""+userMap.get("userId");
        StringBuffer sql = new StringBuffer(
                "SELECT t.ID,t.INDEX_NAME,t.TITLE,t.content,t.report_name,t.USER_NAME,decode(t.ISREAD,'0','未读','1','已读','2','已处理')ISREAD,TO_CHAR(t.SEND_TIME,'yyyy-mm-dd') SEND_TIME FROM meta_portal_ps t "
                + "WHERE t.ISREAD<>2 and  ','||t.RECEIVER_IDS||','  LIKE ? ESCAPE '/' ");
        // 参数处理
        List<Object> params = new ArrayList<Object>();
        params.add(SqlUtils.allLikeBindParam(","+String.valueOf(userId)+","));	
        if (queryData != null) {
            if (queryData.get("startDate") != null&&!("".equals(queryData.get("startDate")))) {
                try {               	
                    Date startDate = new Date();
                    startDate.setTime(Long.parseLong(queryData.get("startDate").toString()));
                    startDate.setHours(0);
                    startDate.setMinutes(0);
                    startDate.setSeconds(0);
                    sql.append(" AND t.SEND_TIME >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
                    params.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate));
                } catch (NumberFormatException e) {
                }
            }
            if (queryData.get("endDate") != null&&!("".equals(queryData.get("endDate")))) {
                try {               	
                    Date endDate = new Date();
                    endDate.setTime(Long.parseLong(queryData.get("endDate").toString()));
                    endDate.setHours(0);
                    endDate.setMinutes(0);
                    endDate.setSeconds(0);
                    sql.append(" AND t.SEND_TIME <= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
                    params.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate));
                } catch (NumberFormatException e) {
                }
            }
            if (queryData.get("isread") != null&&!("".equals(queryData.get("isread")))&&!("全部".equals(queryData.get("isread")))){
                sql.append(" AND (t.isread = ? ) ");
                params.add(queryData.get("isread"));
            }
        }

        sql.append(" order by t.isread,t.send_time desc ");
        String pageSql = sql.toString();
        //分页包装
        if(page!=null){
            pageSql= SqlUtils.wrapPagingSql(pageSql, page);
        }
        List<Map<String,Object>> rs= getDataAccess().queryForList(pageSql, params.toArray());
        return rs;
    }
    
    public Map<String, Object> getPsInfoById(long id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM META_PORTAL_PS where id =?");
		return getDataAccess().queryForMap(buffer.toString(), new Object[]{id});
		   
	}
   //标为已读
    public boolean psIsRead(long id) {
	    String sql = "UPDATE META_PORTAL_PS  SET isread='1' where id =?";
		return getDataAccess().execNoQuerySql(sql, new Object[]{id});
	}
    //处理
    public boolean psDeal(Map<String, Object> data, Page page) {
	    String sql = "UPDATE META_PORTAL_PS SET isread='2',DEAL_OPINION=?,deal_time=SYSDATE where id =?";
	    Object [] params = {
		        MapUtils.getString(data,"dealOpinion",null),
		        MapUtils.getString(data,"id",null)
		     };
	    return getDataAccess().execNoQuerySql(sql, params);
	} 
	//插入批示数据
	public  boolean insertPsInfo(Map<String,Object> map){
		String sql = "INSERT INTO meta_portal_ps(ID,REPORT_ID,REPORT_NAME,INDEX_CD,INDEX_NAME,TITLE,CONTENT,RECEIVER_IDS,RECEIVER_NAMES,USER_ID,USER_NAME,SEND_TIME,ISREAD,DEAL_TIME,GUID)" 
                    +" VALUES(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,0,SYSDATE,?) ";
		Object[] param = {MapUtils.getString(map,"psId",null),
						  MapUtils.getString(map,"reportId",null),
						  MapUtils.getString(map,"reportName",null),
				          MapUtils.getString(map,"indexId",null),
				          MapUtils.getString(map,"indexName",null),
				          MapUtils.getString(map,"noticeTitle",null),
				          MapUtils.getString(map,"noticeContent",null),
				          MapUtils.getString(map,"userIds",null),
				          MapUtils.getString(map,"userNames",null),
				          MapUtils.getString(map,"createId",null),
				          MapUtils.getString(map,"createName",null),
				          MapUtils.getString(map,"GUID",null)
				    };
		return getDataAccess().execNoQuerySql(sql, param);
	}
/*  public boolean  insertPsSendInfo(Map<String, Object> map) {
	String sql = "INSERT INTO meta_portal_ps_send(ID,PS_ID,INDEX_CD,INDEX_NAME,TITLE,CONTENT,RECEIVER_ID,RECEIVER_NAME,ISREAD,SEND_TIME,GUID)" 
            +" VALUES(?,?,?,?,?,?,?,?,?,SYSDATE,?)  ";
		Object[] param = {queryForNextVal("seq_app_id"),
				          MapUtils.getString(map,"psId",null),
				          MapUtils.getString(map,"indexId",null),
				          MapUtils.getString(map,"indexName",null),
				          MapUtils.getString(map,"noticeTitle",null),
				          MapUtils.getString(map,"noticeContent",null),
				          MapUtils.getString(map,"receiverId",null),
				          MapUtils.getString(map,"receiverName",null),
				          0,
				          MapUtils.getString(map,"GUID",null)
		};
		return getDataAccess().execNoQuerySql(sql, param);
	}*/
    
}
