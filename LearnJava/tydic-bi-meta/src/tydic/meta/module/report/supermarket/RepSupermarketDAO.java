package tydic.meta.module.report.supermarket;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.mag.timer.MetaTimerPO;
import tydic.meta.module.mag.timer.TimerConstant;
import tydic.meta.module.report.ReportConstant;
import tydic.meta.module.report.RptUserLogDAO;
import tydic.meta.module.report.emailSubscription.PushEmailUtil;
import tydic.meta.sys.code.CodeManager;
import tydic.meta.web.session.SessionManager;

/**
 * 报表超市DAO
 * @author 李国民
 * Date：2012-03-13
 */
public class RepSupermarketDAO extends MetaBaseDAO{

    public static final Integer SUB_PPT_SEND_ADDDATE= 11;//订阅报表时间增量(天)


	/**
	 * 查询报表订阅数（如果存在岗位号，则查询同岗位下的信息）
	 * @param useStation 岗位号
	 * @param page 
	 * @return
	 */
	public List<Map<String,Object>> getRepList(String useStation,Page page){
		List<Object> params = new ArrayList<Object>();
		params.add(RptUserLogDAO.PUSH);
		StringBuffer sqlBuffer = new StringBuffer("SELECT T.REPORT_ID,T.REPORT_NAME," +
				" T.REPORT_NOTE,decode(L.NUM,'',0,L.NUM) AS SUB_COUNT,R.COL_ALIAS,G.AUDIT_TYPE," +
				" decode(F.REPORT_FAVORITE_ID,'',0,F.REPORT_FAVORITE_ID,1) FAVORITE_FLAG" +
				" FROM META_RPT_TAB_REPORT_CFG T " +
				" LEFT JOIN (SELECT A.REPORT_ID, COUNT(A.USE_LOG_ID) AS NUM FROM META_RPT_USE_LOG A" +
						" WHERE A.OPERATE_TYPE = ?");
		if(useStation!=null&&!useStation.equals("")){
			//如果存在岗位号，添加查询条件
			sqlBuffer.append(" AND A.USE_STATION_ID=?");
			params.add(useStation);
		}
		sqlBuffer.append(" GROUP BY A.REPORT_ID) L ON T.REPORT_ID = L.REPORT_ID " +
				" LEFT JOIN (SELECT REPORT_ID,wm_concat(COL_ALIAS) AS COL_ALIAS " +
					" FROM META_RPT_TAB_OUTPUT_CFG M " +
					" LEFT JOIN META_RPT_MODEL_ISSUE_COLS N ON M.COLUMN_ID=N.COLUMN_ID" +
					" GROUP BY REPORT_ID) R ON R.REPORT_ID=T.REPORT_ID" +
				" LEFT JOIN (SELECT * FROM META_RPT_USER_FAVORITE E WHERE E.USER_ID=1) F " +
					" ON T.REPORT_ID=F.REPORT_ID" +
				" LEFT JOIN META_RPT_DATA_AUDIT_CFG G ON G.ISSUE_ID=T.ISSUE_ID " +
				" WHERE T.REPORT_STATE = 1 ORDER BY SUB_COUNT DESC");
		String sql = sqlBuffer.toString();
		if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
		return getDataAccess().queryForList(sql,params.toArray());
	}	

	/**
	 * 得到最新的报表数据
	 * @param page
	 * @return
	 */
	public List<Map<String,Object>> getRepListByTime(Page page){
		String sql = "SELECT T.REPORT_ID,T.REPORT_NAME,T.REPORT_NOTE,R.COL_ALIAS," +
				" to_char(T.START_DATE,'yyyy-mm-dd hh24:mi:ss') AS START_DATE,G.AUDIT_TYPE," +
				" decode(F.REPORT_FAVORITE_ID,'',0,F.REPORT_FAVORITE_ID,1) FAVORITE_FLAG" +
				" FROM META_RPT_TAB_REPORT_CFG T" +
				" LEFT JOIN (SELECT REPORT_ID, wm_concat(COL_ALIAS) AS COL_ALIAS" +
					" FROM META_RPT_TAB_OUTPUT_CFG M " +
					" LEFT JOIN META_RPT_MODEL_ISSUE_COLS N ON M.COLUMN_ID = N.COLUMN_ID" +
					" GROUP BY REPORT_ID) R ON R.REPORT_ID = T.REPORT_ID" +
				" LEFT JOIN (SELECT * FROM META_RPT_USER_FAVORITE E WHERE E.USER_ID=1) F " +
					" ON T.REPORT_ID=F.REPORT_ID" +
				" LEFT JOIN META_RPT_DATA_AUDIT_CFG G ON G.ISSUE_ID=T.ISSUE_ID " +
				" WHERE T.REPORT_STATE = 1 ORDER BY T.START_DATE DESC";
		if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
		return getDataAccess().queryForList(sql);
	}
	
	/**
	 * 通过搜索条件查询报表
	 * @param searchName 搜索条件
	 * @param page
	 * @return
	 */
	public List<Map<String,Object>> getRepListBySearchName(String searchName, Page page){
		StringBuffer sqlBuffer = new StringBuffer("SELECT T.REPORT_ID,T.REPORT_NAME,T.REPORT_NOTE," +
				" R.COL_ALIAS,to_char(T.START_DATE,'yyyy-mm-dd hh24:mi:ss') AS START_DATE," +
				" decode(F.REPORT_FAVORITE_ID,'',0,F.REPORT_FAVORITE_ID,1) FAVORITE_FLAG,G.AUDIT_TYPE" +
				" FROM META_RPT_TAB_REPORT_CFG T" +
				" LEFT JOIN (SELECT REPORT_ID, wm_concat(COL_ALIAS) AS COL_ALIAS" +
					" FROM META_RPT_TAB_OUTPUT_CFG M " +
					" LEFT JOIN META_RPT_MODEL_ISSUE_COLS N ON M.COLUMN_ID = N.COLUMN_ID" +
					" GROUP BY REPORT_ID) R ON R.REPORT_ID = T.REPORT_ID" +
				" LEFT JOIN (SELECT * FROM META_RPT_USER_FAVORITE E WHERE E.USER_ID=1) F " +
					" ON T.REPORT_ID=F.REPORT_ID" +
				" LEFT JOIN META_RPT_DATA_AUDIT_CFG G ON G.ISSUE_ID=T.ISSUE_ID " +
				" WHERE T.REPORT_STATE = 1 ");
		if(searchName!=null&&!searchName.equals("")){
			sqlBuffer.append(" AND (T.REPORT_NAME LIKE '%"+searchName+"%' " +
					" OR T.REPORT_NOTE LIKE '%"+searchName+"%' " +
					" OR T.REPORT_KEYWORD LIKE '%"+searchName+"%')");
		}
		sqlBuffer.append(" ORDER BY T.START_DATE DESC");
		String sql = sqlBuffer.toString();
		if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
		return getDataAccess().queryForList(sql);
	}
    
    /**
     * 通过报表id得到报表信息
     * @param reportId 报表id
     * @return
     */
    public Map<String,Object> getReportDetail(String reportId){
    	String sql ="SELECT T.REPORT_NAME,A.REPORT_TYPE_NAME,C.USER_NAMECN,D.DEPT_NAME," +
    			" to_char(T.START_DATE,'yyyy-mm-dd') AS START_DATE," +
    			" T.EFFECT_TIME,T.REPORT_KEYWORD,T.REPORT_NOTE,G.AUDIT_TYPE" +
    			" FROM META_RPT_TAB_REPORT_CFG T" +
    			" LEFT JOIN META_RPT_TYPE A ON T.REPORT_TYPE_ID = A.REPORT_TYPE_ID" +
    			" LEFT JOIN (SELECT * FROM META_RPT_CONFIG_LOG L WHERE L.OPERATE_TYPE = '11') B " +
    				" ON T.REPORT_ID = B.REPORT_ID" +
    			" LEFT JOIN META_MAG_USER C ON T.USER_ID = C.USER_ID" +
    			" LEFT JOIN META_DIM_USER_DEPT D ON B.USER_DEPT_ID = D.DEPT_CODE" +
    			" LEFT JOIN META_RPT_DATA_AUDIT_CFG G ON G.ISSUE_ID=T.ISSUE_ID" +
    			" WHERE T.REPORT_ID=?";

        Map<String,Object> rs = getDataAccess().queryForMap(sql,reportId);
        //加入码表信息
        rs.put("SHOW_EFFECT_TIME", CodeManager.getName(ReportConstant.REPORT_TIME,MapUtils.getString(rs,"EFFECT_TIME")));
    	return rs;
    }
	
	/**
     * 查询所有报表分类
	 * @return
	 */
    public List<Map<String, Object>> queryFavoriteGroup() {
    	String sql = "SELECT T.FAVORITE_ID,T.FAVORITE_NAME,T.PARENT_ID " +
    			" FROM META_MAG_FAVORITE_DIR T WHERE T.FAVORITE_TYPE=2 AND T.USER_ID=?";
        return getDataAccess().queryForList(sql,SessionManager.getCurrentUserID());
    }
    
    /**
     * 查询用户某用于对于该报表是否存在收藏
     * @return
     */
    public boolean isExistFavorite(Map<String,Object> data){
    	//得到当前登录用户id
    	String sql = "SELECT T.FAVORITE_ID FROM META_RPT_USER_FAVORITE T WHERE T.USER_ID=? AND T.REPORT_ID=?";
    	Map<String,Object> rs = getDataAccess().queryForMap(sql,
    			SessionManager.getCurrentUserID(),data.get("reportId"));
        return rs!=null&&rs.size()>0;
    }
    
    /**
     * 添加报表收藏
     * @param data
     * @return
     */
    public boolean insertFavorite(Map<String,Object> data){
    	boolean rs = true;
    	try{
        BaseDAO.beginTransaction();
		Map<String,Object> user = SessionManager.getCurrentUser();
        if(rs){ //添加报表收藏
	        String sql = "INSERT INTO META_RPT_USER_FAVORITE (REPORT_FAVORITE_ID,REPORT_ID," +
	        		" FAVORITE_ID,USER_ID,FAVORITE_REPORT_TIME,FAVORITE_REPORT_ORDER) "
	            + " SELECT ?,?,?,?,sysdate,NVL(MAX(FAVORITE_REPORT_ORDER),1)+1 FROM META_RPT_USER_FAVORITE";
	        rs = getDataAccess().execNoQuerySql(sql,queryForNextVal("SEQ_RPT_USER_FAVORITE_ID"),
        		data.get("reportId"),data.get("favoriteId"),user.get("userId"));
        }
        if(rs){  //添加记录日志
            RptUserLogDAO logDAO = new RptUserLogDAO();
            logDAO.recordRptUserLog(MapUtils.getLongValue(data, "reportId"),RptUserLogDAO.FAV);
        }
        if(rs){  //当所有都成功是，提交事务
            BaseDAO.commit();
        }else{
            BaseDAO.rollback();
        }
    	}catch(Exception e){
            BaseDAO.rollback();
            rs = false;
    	}
    	return rs;
    }
    
    /**
     * 收藏并订阅报表
     * @param data
     * @return
     */
    public boolean insertFavoriteAndSub(Map<String,Object> data){
    	boolean rs = true;
    	try{
        BaseDAO.beginTransaction();
		Map<String,Object> user = SessionManager.getCurrentUser();
		long reportFavoriteId = 0; //报表收藏id
        RptUserLogDAO logDAO = new RptUserLogDAO();
		if(data.get("reportFavoriteId")==null||data.get("reportFavoriteId").equals("")){
			
			reportFavoriteId = queryForNextVal("SEQ_RPT_USER_FAVORITE_ID");	
	        if(rs){ //添加报表收藏
		        String sql = "INSERT INTO META_RPT_USER_FAVORITE (REPORT_FAVORITE_ID,REPORT_ID," +
		        		" FAVORITE_ID,USER_ID,FAVORITE_REPORT_TIME,FAVORITE_REPORT_ORDER) "
		            + "SELECT ?,?,?,?,sysdate,NVL(MAX(FAVORITE_REPORT_ORDER),1)+1 FROM META_RPT_USER_FAVORITE";
		        rs = getDataAccess().execNoQuerySql(sql,reportFavoriteId,
	        		data.get("reportId"),data.get("favoriteId"),user.get("userId"));
	        }
	        if(rs){  //添加记录日志
                logDAO.recordRptUserLog(MapUtils.getLongValue(data, "reportId"),RptUserLogDAO.FAV);
	        }
		}else{
			reportFavoriteId = MapUtils.getLongValue(data, "reportFavoriteId");	
		}
		
		String sendTime = "";
        if(rs){  //添加报表订阅
        	int startNum = SUB_PPT_SEND_ADDDATE;	//初始时间增量
            Date sendBaseTime = DateUtil.getDateTimeByString(data.get("sendBaseTime").toString(), "yyyy-MM-dd HH:mm:ss");
    		Calendar calendar = Calendar.getInstance();
    		calendar.setTime(sendBaseTime);
    		calendar.add(Calendar.DATE, (0-startNum));	//基准时间为发送时间减去初始时间增量天数
        	sendTime = DateUtil.format(calendar.getTime(),"yyyy-MM-dd HH:mm:ss");
        	String sql = "INSERT INTO META_RPT_PUSH_CONFIG(PUSH_CONFIG_ID,REPORT_ID," +
        			" REPORT_FAVORITE_ID,USER_ID,PUSH_TYPE,SEND_SEQUNCE,SEND_BASE_TIME," +
        			" SEND_TIME_ADD,CREATE_TIME) VALUES(?,?,?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,sysdate)";
            rs = getDataAccess().execNoQuerySql(sql,queryForNextVal("SEQ_RPT_PUSH_CONFIG_ID"),
            		MapUtils.getLongValue(data, "reportId"),reportFavoriteId,user.get("userId"),
            		data.get("pushType"),data.get("sendSequnce"),sendTime,startNum);
        }
        if(rs){  //添加订阅日志记录
            logDAO.recordRptUserLog(MapUtils.getLongValue(data, "reportId"),RptUserLogDAO.PUSH);
        }
//        /*增加調度信息：注--現在只開通郵件*/
//        if((""+data.get("pushType")).indexOf(""+ReportConstant.PUSHTYPEMAIL) != -1){
//        	//新增調度表信息
//        	PushEmailUtil pushEmailUtil = new PushEmailUtil();
//        	String sendSequnce = ""+data.get("sendSequnce");
//        	String timerClass = pushEmailUtil.getTimerClass(sendSequnce);
//        	sendSequnce = ""+TimerConstant.codeManageMappingTheTimer(Integer.valueOf(sendSequnce));
//        	/*根据映射后的sendSequnce获取timerRule*/
//        	String timerRule = TimerConstant.getTimerRuleBySendSquenceAndTime(Integer.valueOf(sendSequnce), sendTime);
//        	String timerDesc = MapUtils.getString(data, "reportId")+","+user.get("userId");
//        	String startTime = DateUtil.getParamDay(sendTime, "yyyyMMddHHmmss");
//        	MetaTimerPO metaTimerPO = pushEmailUtil.addMagTimerTabMes(timerClass, sendSequnce, timerRule, timerDesc,startTime,null);
//        	//添加任務調度
//        	rs = pushEmailUtil.addTimer(metaTimerPO);
//        }
        if(rs){  //当所有都成功是，提交事务
            BaseDAO.commit();
        }else{
            BaseDAO.rollback();
        }
    	}catch(Exception e){
            BaseDAO.rollback();
            rs = false;
    	}
    	return rs;
    }

    /**
     * 获取报表订阅信息
     * @param favoriteId 收藏ID
     * @param reportId 报表ID
     * @return 返回订阅方案详细配置
     * @author 王春生
     * @date 2012-03-27 14:40
     */
    public Map<String,Object> getReportPushConfig(int favoriteId,int reportId){
        String sql = "SELECT PUSH_CONFIG_ID,REPORT_ID,REPORT_FAVORITE_ID,USER_ID,PUSH_TYPE,SEND_SEQUNCE," +
                " to_char(SEND_BASE_TIME,'yyyy-mm-dd hh24:mi:ss') AS SEND_BASE_TIME," +
                " SEND_TIME_ADD,CREATE_TIME FROM META_RPT_PUSH_CONFIG WHERE REPORT_FAVORITE_ID=? AND REPORT_ID=?";
        return getDataAccess().queryForMap(sql,favoriteId,reportId);
    }

    /**
     * 修改报表订阅配置信息
     * @param data 表单数据
     * @return
     */
    public int updateReportPushConfig(Map<String,Object> data){
        Map<String,Object> user = SessionManager.getCurrentUser();
        int startNum = SUB_PPT_SEND_ADDDATE;	//初始时间增量
        Date sendBaseTime = DateUtil.getDateTimeByString(data.get("sendBaseTime").toString(), "yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sendBaseTime);
        calendar.add(Calendar.DATE, (0-startNum));	//基准时间为发送时间减去初始时间增量天数
        String sendTime = DateUtil.format(calendar.getTime(),"yyyy-MM-dd HH:mm:ss");

        String sql = "UPDATE META_RPT_PUSH_CONFIG SET PUSH_TYPE=?,SEND_SEQUNCE=?," +
                " SEND_BASE_TIME=to_date(?,'yyyy-mm-dd hh24:mi:ss') " +
                " WHERE REPORT_ID=? AND REPORT_FAVORITE_ID=?";
        getDataAccess().execNoQuerySql(sql,data.get("pushType"),data.get("sendSequnce"),sendTime,
                MapUtils.getLongValue(data, "reportId"),MapUtils.getLongValue(data, "reportFavoriteId"));
        
//        /*更新調度信息：注--現在只開通郵件*/
//        if((""+data.get("pushType")).indexOf(""+ReportConstant.PUSHTYPEMAIL) != -1){
//        	PushEmailUtil pushEmailUtil = new PushEmailUtil();
//        	String reportIdAndUserId = MapUtils.getString(data, "reportId")+","+user.get("userId");
//        	MetaTimerPO metaTimerPO = pushEmailUtil.getTimerPOMesByTimerDesc(reportIdAndUserId);
//        	String timerRule = TimerConstant.getTimerRuleBySendSquenceAndTime(metaTimerPO.getTimerType(), sendTime);
//        	String startTime = DateUtil.getParamDay(sendTime, "yyyyMMddHHmmss");
//        	pushEmailUtil.updateTimer(reportIdAndUserId, timerRule, startTime, null);
//        }

        RptUserLogDAO logDAO = new RptUserLogDAO();
        logDAO.recordRptUserLog(MapUtils.getLongValue(data, "reportId"),RptUserLogDAO.MODIFY_PUSH);
        return 1;
    }
}
