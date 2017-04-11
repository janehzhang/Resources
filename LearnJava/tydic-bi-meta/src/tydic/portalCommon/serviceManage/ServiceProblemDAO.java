package tydic.portalCommon.serviceManage;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;

import com.jspsmart.upload.SmartUploadException;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.sys.code.CodeManager;
import tydic.meta.web.session.SessionManager;

public class ServiceProblemDAO extends MetaBaseDAO {
    /*
     * 按id查询服务问题单
     */
    public List<Map<String, Object>> detailProblem(
    		String mainProblemId) {
    	List<Object> params = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer(
                "SELECT DISTINCT MAIN_PROBLEM_ID,return_reason,ACCEPT_ACTOR_NAME,ACCEPT_ACTOR_ID,THEME, PROBLEM_DESCRIPTION, SOURCE, URGENCY_DEGREE,MAIN_STATE,"
                + "PROCESS_LIMITED,PROBLEM_TYPE,TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME,CREATE_ACTOR_NAME,"
                +"decode(PROBLEM_SOLUTION,'','无','有') IS_EVALUATION_PLAN,decode(ATTACHMENT_NAME,'','无',ATTACHMENT_NAME)ATTACHMENT_NAME,ATTACHMENT_ADDRESS,CREATE_ACTOR_ID,CREATE_DEPT_ID,CREATE_DEPT_NAME,"
                +"PROBLEM_SOLUTION,to_char(PLANNED_FINISH_TIME,'YYYY-MM-DD HH24:MI:SS') as PLANNED_FINISH_TIME,EVALUATION_ATTACHMENT,EVALUATION_ATTACHMENT_NAME,FIRST_ACCEPT_ACTOR_ID "
                + " FROM CS_MAIN_SERVICE_PROBLEM WHERE MAIN_PROBLEM_ID="+mainProblemId);
        // 参数处理
        String pageSql = sql.toString();
      //分页包装
        List<Map<String,Object>> rs= getDataAccess().queryForList(pageSql, params.toArray());
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("SOURCE", CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_SOURCE, MapUtils.getString(map, "SOURCE")));
                map.put("URGENCY_DEGREE",CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_URGENCY,MapUtils.getString(map, "URGENCY_DEGREE")));
                map.put("PROBLEM_TYPE",CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_TYPE,MapUtils.getString(map,"PROBLEM_TYPE")));
            }
        }
        return rs;
    }
    /**
     * 查询服务问题单
     * @param queryData,page
     * @return
     */
    public List<Map<String, Object>> queryProblem(
            Map<String, Object> queryData, Page page) {
    	List<Object> params = new ArrayList<Object>();
        String userId= MapUtils.getString(queryData,"userId",null);
        StringBuffer sql = new StringBuffer(
                "SELECT DISTINCT MAIN_PROBLEM_ID,return_reason,ACCEPT_ACTOR_NAME,ACCEPT_ACTOR_ID,THEME, PROBLEM_DESCRIPTION, SOURCE, URGENCY_DEGREE,MAIN_STATE,"
                + "PROCESS_LIMITED,PROBLEM_TYPE,TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME,CREATE_ACTOR_NAME,"
                +"decode(PROBLEM_SOLUTION,'','无','有') IS_EVALUATION_PLAN,decode(ATTACHMENT_NAME,'','无',ATTACHMENT_NAME)ATTACHMENT_NAME,ATTACHMENT_ADDRESS,CREATE_ACTOR_ID,CREATE_DEPT_ID,CREATE_DEPT_NAME,"
                +"PROBLEM_SOLUTION,to_char(PLANNED_FINISH_TIME,'YYYY-MM-DD HH24:MI:SS') as PLANNED_FINISH_TIME,EVALUATION_ATTACHMENT,EVALUATION_ATTACHMENT_NAME,FIRST_ACCEPT_ACTOR_ID "
                + " FROM CS_MAIN_SERVICE_PROBLEM WHERE (CREATE_ACTOR_ID='"+userId+"' OR ACCEPT_ACTOR_ID='"+userId+"'"
                + " OR MAIN_PROBLEM_ID IN(SELECT DISTINCT MAIN_PROBLEM_ID FROM CS_DEAL_SERVICE_PROBLEM WHERE DEAL_ACTOR_ID='"+ userId +"' OR NEXT_ACTOR_ID='"+ userId +"'))");
    
        // 参数处理
        if (queryData != null) {
            if (queryData.get("startDate") != null&&!("".equals(queryData.get("startDate")))) {
                try {               	
                    Date startDate = new Date();
                    startDate.setTime(Long.parseLong(queryData.get("startDate").toString()));
                    startDate.setHours(0);
                    startDate.setMinutes(0);
                    startDate.setSeconds(0);
                    sql.append("AND CREATE_TIME >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
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
                    sql.append("AND CREATE_TIME <= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
                    params.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate));
                } catch (NumberFormatException e) {
                }
            }
            if (queryData.get("deptId") != null&&!("".equals(queryData.get("deptId")))&&Integer.parseInt(queryData.get("deptId").toString()) != 1){
                sql.append("AND (CREATE_DEPT_ID = ? ) ");
                params.add(queryData.get("deptId"));
            }
            if (queryData.get("theme") != null&&!("".equals(queryData.get("theme")))){
                sql.append("AND theme LIKE "+SqlUtils.allLikeParam(queryData.get("theme").toString()));
            }
            if (queryData.get("source") != null&&!("".equals(queryData.get("source")))){
                sql.append("AND (SOURCE = ? ) ");
                params.add(queryData.get("source"));
            }
            if (queryData.get("problemStep") != null&&!("".equals(queryData.get("problemStep")))){
                sql.append("AND (MAIN_STATE = ? ) ");
                params.add(queryData.get("problemStep"));
            }
        }

        sql.append(" ORDER BY CREATE_TIME DESC ");
        String pageSql = sql.toString();
        //分页包装
        if(page!=null){
            pageSql= SqlUtils.wrapPagingSql(pageSql, page);
        }
        List<Map<String,Object>> rs= getDataAccess().queryForList(pageSql, params.toArray());
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("SOURCE", CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_SOURCE, MapUtils.getString(map, "SOURCE")));
                map.put("URGENCY_DEGREE",CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_URGENCY,MapUtils.getString(map, "URGENCY_DEGREE")));
                map.put("PROBLEM_TYPE",CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_TYPE,MapUtils.getString(map,"PROBLEM_TYPE")));
            }
        }
        return rs;
    }  
    /**
     * 首页待办
     * @param queryData,page
     * @return
     */
    public List<Map<String, Object>> indexToDeal(Map<String, Object> queryData) {
        String userId= MapUtils.getString(queryData,"userId",null);
        StringBuffer sql = new StringBuffer(
                "SELECT DISTINCT MAIN_PROBLEM_ID,return_reason,ACCEPT_ACTOR_NAME,ACCEPT_ACTOR_ID,THEME, PROBLEM_DESCRIPTION, SOURCE, URGENCY_DEGREE,MAIN_STATE,"
                + "PROCESS_LIMITED,PROBLEM_TYPE,TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME,CREATE_ACTOR_NAME,"
                +"decode(PROBLEM_SOLUTION,'','无','有') IS_EVALUATION_PLAN,decode(ATTACHMENT_NAME,'','无',ATTACHMENT_NAME)ATTACHMENT_NAME,ATTACHMENT_ADDRESS,CREATE_ACTOR_ID,CREATE_DEPT_ID,CREATE_DEPT_NAME,"
                +"PROBLEM_SOLUTION,to_char(PLANNED_FINISH_TIME,'YYYY-MM-DD HH24:MI:SS') as PLANNED_FINISH_TIME,EVALUATION_ATTACHMENT,EVALUATION_ATTACHMENT_NAME,FIRST_ACCEPT_ACTOR_ID "
                + " FROM CS_MAIN_SERVICE_PROBLEM WHERE (CREATE_ACTOR_ID='"+userId+"' OR ACCEPT_ACTOR_ID='"+userId+"'"
                + " OR MAIN_PROBLEM_ID IN(SELECT DISTINCT MAIN_PROBLEM_ID FROM CS_DEAL_SERVICE_PROBLEM WHERE DEAL_ACTOR_ID='"+ userId +"' OR NEXT_ACTOR_ID='"+ userId +"'))");
        sql.append(" ORDER BY CREATE_TIME DESC ");
        String pageSql = sql.toString();
        List<Map<String,Object>> rs= getDataAccess().queryForList(pageSql);
        return rs;
    }  
    /**
     * 查询服务问题单
     * @param queryData,page
     * @return
     * @throws ParseException 
     */
    public List<Map<String, Object>> indexQueryProblem(String startTime,String endTime,int startSubscript,int endSubscript) throws ParseException {
    	List<Object> params = new ArrayList<Object>();
    	Map<String,Object> userMap = SessionManager.getCurrentUser();
    	String userId= MapUtils.getString(userMap,"userId",null);
        StringBuffer buffer = new StringBuffer();
    	buffer.append("select * from (select ");
        buffer.append(" a.*, rownum rn from ( ");
        buffer.append(
                "SELECT DISTINCT MAIN_PROBLEM_ID,return_reason,ACCEPT_ACTOR_NAME,ACCEPT_ACTOR_ID,CREATE_ACTOR_NO,THEME, PROBLEM_DESCRIPTION, SOURCE, URGENCY_DEGREE,MAIN_STATE,"
                + "PROCESS_LIMITED,PROBLEM_TYPE,TO_CHAR(CREATE_TIME,'YYYY-MM-DD HH24:MI:SS') CREATE_TIME,CREATE_ACTOR_NAME,"
                +"decode(PROBLEM_SOLUTION,'','无','有') IS_EVALUATION_PLAN,decode(ATTACHMENT_NAME,'','无',ATTACHMENT_NAME)ATTACHMENT_NAME,ATTACHMENT_ADDRESS,CREATE_ACTOR_ID,CREATE_DEPT_ID,CREATE_DEPT_NAME,"
                +"PROBLEM_SOLUTION,to_char(PLANNED_FINISH_TIME,'YYYY-MM-DD HH24:MI:SS') as PLANNED_FINISH_TIME,EVALUATION_ATTACHMENT,EVALUATION_ATTACHMENT_NAME,FIRST_ACCEPT_ACTOR_ID "
                + " FROM CS_MAIN_SERVICE_PROBLEM WHERE (CREATE_ACTOR_ID='"+userId+"' OR ACCEPT_ACTOR_ID='"+userId+"'"
                + " OR MAIN_PROBLEM_ID IN(SELECT DISTINCT MAIN_PROBLEM_ID FROM CS_DEAL_SERVICE_PROBLEM WHERE DEAL_ACTOR_ID='"+ userId +"' OR NEXT_ACTOR_ID='"+ userId +"'))");
        buffer.append("  ) a where rownum <= "+endSubscript+") where rn >= " + startSubscript);
        // 参数处理	 
        	if (startTime != null && !("".equals(startTime))) {//Convert.ToDateTime(str)，  
        		startTime=startTime+" 00:00:00";
                buffer.append(" AND CREATE_TIME >= ? ");//to_date(?,'yyyy-MM-dd HH:MM:ss')                 
                params.add(startTime);
          }        
            if (endTime != null && !("".equals(endTime))) {
            	endTime=endTime+" 23:59:59";
                buffer.append(" AND CREATE_TIME <= ? ");//to_date(?,''yyyy-MM-dd HH:MM:ss')
                params.add(endTime);
          }
        buffer.append(" ORDER BY CREATE_TIME DESC ");
        String pageSql = buffer.toString();
        List<Map<String,Object>> rs= getDataAccess().queryForList(pageSql, params.toArray());
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("SOURCE", CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_SOURCE, MapUtils.getString(map, "SOURCE")));
                map.put("URGENCY_DEGREE",CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_URGENCY,MapUtils.getString(map, "URGENCY_DEGREE")));
                map.put("PROBLEM_TYPE",CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_TYPE,MapUtils.getString(map,"PROBLEM_TYPE")));
            }
        }
        return rs;
    }  
       
    /**
     * @Title: getServiceCount 
     * @Description: 获取服务问题单信息的总数
     * @param zoneId
     * @return int   
     * @throws
     */
    public int getServiceCount(String startDate,String endDate){ 	
    	Map<String,Object> userMap = SessionManager.getCurrentUser();
    	String userId= MapUtils.getString(userMap,"userId",null);
        StringBuffer sql = new StringBuffer(
                "SELECT COUNT(ROWID) COUNTS FROM CS_MAIN_SERVICE_PROBLEM WHERE (CREATE_ACTOR_ID='"+userId+"' OR ACCEPT_ACTOR_ID='"+userId+"'"
                + " OR MAIN_PROBLEM_ID IN(SELECT DISTINCT MAIN_PROBLEM_ID FROM CS_DEAL_SERVICE_PROBLEM WHERE DEAL_ACTOR_ID='"+ userId +"' OR NEXT_ACTOR_ID='"+ userId +"'))");
        Map<String,Object> map = getDataAccess().queryForMap(sql.toString());
    	return Integer.valueOf(""+map.get("COUNTS"));
    }
    
    /**
     * 查询评估报告列表
     * @param queryData,page
     * @return
     */
    public List<Map<String, Object>> queryEvaluationInfoById(Map<String,Object> queryData, Page page) {
        StringBuffer sql = new StringBuffer("SELECT DEAL_PROBLEM_ID,EVALUATION_DETAIL,DEAL_ACTOR_NAME,DEAL_TIME FROM CS_DEAL_SERVICE_PROBLEM " +
        		" WHERE EVALUATION_DETAIL IS NOT NULL ");
        // 参数处理
        List<Object> params = new ArrayList<Object>();
        if (queryData != null) {
            if (queryData.get("mainProblemId") != null&&!("".equals(queryData.get("mainProblemId")))) {
            	sql.append("AND (MAIN_PROBLEM_ID = ? ) ");      	
            	params.add(queryData.get("mainProblemId"));
            }
        }
        sql.append(" ORDER BY DEAL_TIME DESC ");
        String pageSql = sql.toString();
        //分页包装
        if(page!=null){
            pageSql= SqlUtils.wrapPagingSql(pageSql, page);
        }
        List<Map<String,Object>> rs= getDataAccess().queryForList(pageSql, params.toArray());
        return rs;
    }
    
    /**
     * 新增一条服务问题单
     * @param data
     * @return
     * @throws Exception 
     */
    public Long insertServiceProblem(Map<String, Object> data) throws Exception{
        String insertSql = "INSERT INTO CS_MAIN_SERVICE_PROBLEM(MAIN_ID,MAIN_PROBLEM_ID,THEME,PROBLEM_DESCRIPTION,SOURCE,URGENCY_DEGREE,PROCESS_LIMITED,PROBLEM_TYPE, " +
                "CREATE_ACTOR_NAME,CREATE_ACTOR_ID,CREATE_DEPT_NAME,CREATE_DEPT_ID,ACCEPT_ACTOR_NO,ACCEPT_ACTOR_NAME,ACCEPT_ACTOR_ID,ACCEPT_DEPT_ID,ACCEPT_DEPT_NAME,NOTICE_WAY,IS_SMS_NOTICE,CREATE_TIME," +
                "DEAL_OPINION,main_state,attachment_type,attachment_name,attachment_address," +
                "PROBLEM_SOLUTION,PLANNED_FINISH_TIME,EVALUATION_ATTACHMENT,EVALUATION_ATTACHMENT_NAME,FIRST_ACCEPT_ACTOR_ID,FIRST_ACCEPT_ACTOR_NAME,FIRST_ACCEPT_DEPT_ID,FIRST_ACCEPT_DEPT_NAME,GUID ) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,'1',?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?) ";
        if(data != null){		
       }
        getDataAccess().beginTransaction();
        
        Long pk = queryForNextVal("SEQ_MAG_NOTICE_ID");
        String strPk=pk+"";
        String mainId=pk+"";
        Object [] params = {
        		mainId,
        		strPk,
                MapUtils.getString(data,"theme",null),
                MapUtils.getString(data,"problemDescription",null),
                MapUtils.getString(data,"source",null),
                MapUtils.getString(data,"urgencyDegree",null),
                MapUtils.getString(data,"processLimited",null),
                MapUtils.getString(data,"problemType",null),// 
                MapUtils.getString(data,"createActorName", null) ,
                MapUtils.getString(data,"createActorId", null) ,
                MapUtils.getString(data,"createDeptName", null) ,
                MapUtils.getString(data,"createDeptId", null) ,
                MapUtils.getString(data,"acceptActorNo",null),
                MapUtils.getString(data,"acceptActorName",null),
                MapUtils.getString(data,"acceptActorId",null),
                MapUtils.getString(data,"acceptDeptId",null),
                MapUtils.getString(data,"acceptDeptName",null),
                MapUtils.getString(data,"noticeWay",null),
                MapUtils.getString(data,"isSMSNotice",null),
                MapUtils.getString(data,"dealOpinion",null),
                MapUtils.getString(data,"attachmentType",null),
                MapUtils.getString(data,"attachmentName",null),
                MapUtils.getString(data,"attachmentAddress",null),
                MapUtils.getString(data,"problemSolution",null),
                MapUtils.getString(data, "plannedFinishTime", null), 
                MapUtils.getString(data,"evaluationAttachment",null),
                MapUtils.getString(data,"evaluationAttachmentName",null),
                MapUtils.getString(data,"acceptActorId",null),
                MapUtils.getString(data,"acceptActorName",null),
                MapUtils.getString(data,"acceptDeptId",null),
                MapUtils.getString(data,"acceptDeptName",null),
                MapUtils.getString(data,"GUID",null)
        };
        //getDataAccess().execQuerySql(insertSql,params);
        getDataAccess().execNoQuerySql(insertSql, params);
        getDataAccess().commit();
        return pk;
    }
    
    public Long insertServiceProblemEwam(Map<String, Object> data) throws Exception{
        String insertSql = "INSERT INTO CS_MAIN_SERVICE_PROBLEM(MAIN_ID,MAIN_PROBLEM_ID,THEME,PROBLEM_DESCRIPTION,SOURCE,URGENCY_DEGREE,PROCESS_LIMITED,PROBLEM_TYPE, " +
                "CREATE_ACTOR_NAME,CREATE_ACTOR_ID,CREATE_DEPT_NAME,CREATE_DEPT_ID,ACCEPT_ACTOR_NO,ACCEPT_ACTOR_NAME,ACCEPT_ACTOR_ID,ACCEPT_DEPT_ID,ACCEPT_DEPT_NAME,NOTICE_WAY,IS_SMS_NOTICE,CREATE_TIME," +
                "DEAL_OPINION,main_state,attachment_type,attachment_name,attachment_address," +
                "PROBLEM_SOLUTION,PLANNED_FINISH_TIME,EVALUATION_ATTACHMENT,EVALUATION_ATTACHMENT_NAME,FIRST_ACCEPT_ACTOR_ID,FIRST_ACCEPT_ACTOR_NAME,FIRST_ACCEPT_DEPT_ID,FIRST_ACCEPT_DEPT_NAME,GUID,EWAM_FLAG ) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,'1',?,?,?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,?,?,?,?) ";
        if(data != null){		
       }
        getDataAccess().beginTransaction();
        
        Long pk = queryForNextVal("SEQ_MAG_NOTICE_ID");
        String strPk=pk+"";
        String mainId=pk+"";
        Object [] params = {
        		mainId,
        		strPk,
                MapUtils.getString(data,"theme",null),
                MapUtils.getString(data,"problemDescription",null),
                MapUtils.getString(data,"source",null),
                MapUtils.getString(data,"urgencyDegree",null),
                MapUtils.getString(data,"processLimited",null),
                MapUtils.getString(data,"problemType",null),// 
                MapUtils.getString(data,"createActorName", null) ,
                MapUtils.getString(data,"createActorId", null) ,
                MapUtils.getString(data,"createDeptName", null) ,
                MapUtils.getString(data,"createDeptId", null) ,
                MapUtils.getString(data,"acceptActorNo",null),
                MapUtils.getString(data,"acceptActorName",null),
                MapUtils.getString(data,"acceptActorId",null),
                MapUtils.getString(data,"acceptDeptId",null),
                MapUtils.getString(data,"acceptDeptName",null),
                MapUtils.getString(data,"noticeWay",null),
                MapUtils.getString(data,"isSMSNotice",null),
                MapUtils.getString(data,"dealOpinion",null),
                MapUtils.getString(data,"attachmentType",null),
                MapUtils.getString(data,"attachmentName",null),
                MapUtils.getString(data,"attachmentAddress",null),
                MapUtils.getString(data,"problemSolution",null),
                MapUtils.getString(data, "plannedFinishTime", null), 
                MapUtils.getString(data,"evaluationAttachment",null),
                MapUtils.getString(data,"evaluationAttachmentName",null),
                MapUtils.getString(data,"acceptActorId",null),
                MapUtils.getString(data,"acceptActorName",null),
                MapUtils.getString(data,"acceptDeptId",null),
                MapUtils.getString(data,"acceptDeptName",null),
                MapUtils.getString(data,"GUID",null),
                MapUtils.getString(data,"ewamFlag",null)
        };
        //getDataAccess().execQuerySql(insertSql,params);
        getDataAccess().execNoQuerySql(insertSql, params);
        getDataAccess().commit();
        return pk;
    }
    
    /**
     * 查询服务问题单详情
     * @param queryData,page
     * @return
     */
    public List<Map<String, Object>> queryDetailServiceProblem(
            Map<String, Object> queryData, Page page) {
        StringBuffer sql = new StringBuffer(
                "SELECT D.DEAL_PROBLEM_ID,M.THEME, M.PROBLEM_DESCRIPTION, M.SOURCE, M.URGENCY_DEGREE,"
                + "M.PROCESS_LIMITED,M.PROBLEM_TYPE,D.deal_state,D.DEAL_ACTOR_NAME,TO_CHAR(D.deal_time,'YYYY-MM-DD HH24:MI:SS') deal_time,D.DEAL_OPINION,decode(D.ATTACHMENT_NAME,'','无',D.ATTACHMENT_NAME)ATTACHMENT_NAME,D.ATTACHMENT_ADDRESS "
                + "FROM CS_DEAL_SERVICE_PROBLEM D,CS_MAIN_SERVICE_PROBLEM M "
                + "WHERE D.MAIN_PROBLEM_ID=M.MAIN_PROBLEM_ID ");
        // 参数处理
        List<Object> params = new ArrayList<Object>();
        if (queryData != null) {
        	if (queryData.get("mainProblemId") != null&&!("".equals(queryData.get("mainProblemId")))){
                sql.append("AND (D.main_problem_id = ? ) ");
                params.add(queryData.get("mainProblemId"));
            }
        	if (queryData.get("startDate") != null&&!("".equals(queryData.get("startDate")))) {
                try {               	
                    Date startDate = new Date();
                    startDate.setTime(Long.parseLong(queryData.get("startDate").toString()));
                    startDate.setHours(0);
                    startDate.setMinutes(0);
                    startDate.setSeconds(0);
                    sql.append("AND D.deal_time >= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
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
                    sql.append("AND D.deal_time <= TO_DATE(?,'yyyy-MM-dd hh24:mi:ss') ");
                    params.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endDate));
                } catch (NumberFormatException e) {
                }
            }
            if (queryData.get("createDeptName") != null&&!("".equals(queryData.get("createDeptName")))&&!("所有部门".equals(queryData.get("createDeptName")))){
                sql.append("AND (M.CREATE_DEPT_ID = ? ) ");
                params.add(queryData.get("createDeptId"));
            }
            if (queryData.get("dealDeptName") != null&&!("".equals(queryData.get("dealDeptName")))&&!("所有部门".equals(queryData.get("dealDeptName")))){
                sql.append("AND (D.DEAL_DEPT_ID = ? ) ");
                params.add(queryData.get("dealDeptId"));
            }
            if (queryData.get("theme") != null&&!("".equals(queryData.get("theme")))){
                sql.append("AND M.theme LIKE "+SqlUtils.allLikeParam(queryData.get("theme").toString()));
            }
            if (queryData.get("source") != null&&!("".equals(queryData.get("source")))){
                sql.append("AND (M.SOURCE = ? ) ");
                params.add(queryData.get("source"));
            }
            if (queryData.get("dealState") != null&&!("".equals(queryData.get("dealState")))){
                sql.append("AND (D.deal_state = ? ) ");
                params.add(queryData.get("dealState"));
            }
            if (queryData.get("attachmentType") != null&&!("".equals(queryData.get("attachmentType")))){
                sql.append("AND (D.ATTACHMENT_TYPE = ? ) ");
                params.add(queryData.get("attachmentType"));
            }
        }

        sql.append(" ORDER BY D.deal_time DESC ");
        String pageSql = sql.toString();
        //分页包装
        if(page!=null){
            pageSql= SqlUtils.wrapPagingSql(pageSql, page);
        }
        List<Map<String,Object>> rs= getDataAccess().queryForList(pageSql, params.toArray());
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("SOURCE", CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_SOURCE, MapUtils.getString(map, "SOURCE")));
                map.put("URGENCY_DEGREE",CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_URGENCY,MapUtils.getString(map, "URGENCY_DEGREE")));
                map.put("PROBLEM_TYPE",CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_TYPE,MapUtils.getString(map,"PROBLEM_TYPE")));
                map.put("DEAL_STATE",CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_STEP,MapUtils.getString(map,"DEAL_STATE")));
                map.put("ATTACHMENT_TYPE",CodeManager.getName(ServiceProblemConstant.META_SERVICE_PROBLEM_ATTACHMENT,MapUtils.getString(map,"ATTACHMENT_TYPE")));
            }
        }
        return rs;
    }
    /**
     * 主(副)单处理
     * @param queryData,page
     * @return
     */
    public Long nextDealServiceProblem(Map<String, Object> data, Page page) {
	        String insertSql = "INSERT INTO CS_DEAL_SERVICE_PROBLEM(MAIN_PROBLEM_ID,deal_problem_id,deal_opinion,deal_time,DEAL_ACTOR_NAME,DEAL_ACTOR_ID,DEAL_DEPT_ID,DEAL_DEPT_NAME,NEXT_ACTOR_NO, " +
	        "attachment_address,attachment_type,attachment_name,NEXT_ACTOR_NAME,NEXT_ACTOR_ID,NEXT_DEPT_NAME,NEXT_DEPT_ID,deal_state,EVALUATION_DETAIL " +
	        ") VALUES (?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	        String state="1";
	        if (data.get("nextActorName") != null&&!("".equals(data.get("nextActorName")))){
                state="2";//转办后处理单处于处理中
            }else{
            	if(data.get("evaluationDetail") != null&&!("".equals(data.get("evaluationDetail")))){//待评估
            	   state="6";//有评估计划处理后处理单处于待评估
            	}else{
            		state="3";//无评估计划_待确认
            	}
            }
	        Long pk = queryForNextVal("SEQ_MAG_NOTICE_ID");
	        Object [] params = {
	        MapUtils.getString(data,"mainProblemId",null),
	        pk,
	        MapUtils.getString(data,"dealOpinion",null),//
	        MapUtils.getString(data,"dealActorName",null),
	        MapUtils.getString(data,"dealActorId",null),
	        MapUtils.getString(data,"dealDeptId",null),
	        MapUtils.getString(data,"dealDeptName",null),
	        MapUtils.getString(data,"nextActorNo",null),
	        MapUtils.getString(data,"attachmentAddress",null),
	        MapUtils.getString(data,"attachmentType", null) ,
	        MapUtils.getString(data,"attachmentName",null) ,
	        MapUtils.getString(data,"nextActorName",null),
	        MapUtils.getString(data,"nextActorId",null),
	        MapUtils.getString(data,"nextDeptName",null),
	        MapUtils.getString(data,"nextDeptId",null),
	        MapUtils.getString(data,"dealState",state),
	        MapUtils.getString(data,"evaluationDetail",null)
	        
	     };
	        getDataAccess().execQuerySql(insertSql,params);
	        	
	return pk;
    }
    /**
     * 退回
     * @param queryData,page
     * @return
     */
    public boolean nextReturnServiceProblem(Map<String, Object> data, Page page) {
	        String updateSql = "UPDATE CS_MAIN_SERVICE_PROBLEM SET main_state=?,return_reason=?,ACCEPT_ACTOR_NO=?,return_time=SYSDATE,GUID=? WHERE MAIN_PROBLEM_ID=? ";
	        Object [] params = {
	        MapUtils.getString(data,"mainState","4"),
	        MapUtils.getString(data,"returnReason",null),
	        MapUtils.getString(data,"returnNo",null), 
	        MapUtils.getString(data,"GUID",null),
	        MapUtils.getString(data,"mainProblemId",null)   
	     };
	        return getDataAccess().execNoQuerySql(updateSql, params);
    }
    /**
     * 确认/评估
     * @param queryData,page
     * @return
     */
    public boolean affirmServiceProblem(Map<String, Object> data, Page page) {
	        String updateSql = "UPDATE CS_MAIN_SERVICE_PROBLEM SET main_state=? ,IS_SATISFIED_EVALUATION=? ,ACCEPT_ACTOR_ID=?,GUID=? WHERE MAIN_PROBLEM_ID=? ";
	        Object [] params = {
	        MapUtils.getString(data,"dealState",null),
	        MapUtils.getString(data,"isSatisfiedEvaluation",null),
	        MapUtils.getString(data,"acceptActorId",null),
	        MapUtils.getString(data,"GUID",null),
	        MapUtils.getString(data,"mainProblemId",null)
	     };
	        return getDataAccess().execNoQuerySql(updateSql, params);
    }
    /**
     * 派送
     * @param queryData,page
     * @return
     */
    public boolean sendServiceProblem(Map<String, Object> data, Page page) {
	        String updateSql = "UPDATE CS_MAIN_SERVICE_PROBLEM SET main_state=?,IS_SMS_NOTICE=?,ACCEPT_ACTOR_NO=?,ACCEPT_ACTOR_NAME=?," +
	        		"ACCEPT_ACTOR_ID=?,ACCEPT_DEPT_ID=?,ACCEPT_DEPT_NAME=?,GUID=? WHERE MAIN_PROBLEM_ID=? ";
	        Object [] params = {
	        MapUtils.getString(data,"mainState","1"),
	        MapUtils.getString(data,"isSMSNotice",null),
	        MapUtils.getString(data,"acceptActorNo",null),
	        MapUtils.getString(data,"acceptActorName",null),
	        MapUtils.getString(data,"acceptActorId",null),
	        MapUtils.getString(data,"acceptDeptId",null),
	        MapUtils.getString(data,"acceptDeptName",null),
	        MapUtils.getString(data,"GUID",null),
	        MapUtils.getString(data,"mainProblemId",null)
	     };
	        return getDataAccess().execNoQuerySql(updateSql, params);
    }
    /**
     * 删除主单
     * @param noticeIds
     * @return
     */
    public int delServiceProblem(int[] problemIds) throws Exception{
        if(problemIds != null && problemIds.length > 0){
            StringBuffer sql = new StringBuffer("DELETE FROM CS_MAIN_SERVICE_PROBLEM WHERE main_problem_id IN (");
            for(int i = 0; i < problemIds.length; i++){
                sql.append(problemIds[i]);
                if(i != problemIds.length - 1){
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
     * 删除副单
     * @param noticeIds
     * @return
     */
    public int delViceServiceProblem(int[] problemIds) throws Exception{
        if(problemIds != null && problemIds.length > 0){
            StringBuffer sql = new StringBuffer("DELETE FROM cs_deal_service_problem WHERE main_problem_id IN (");
            for(int i = 0; i < problemIds.length; i++){
                sql.append(problemIds[i]);
                if(i != problemIds.length - 1){
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
     * 同步主单状态
     * @param queryData,page
     * @return
     */
    public void mainServiceProblemState(Map<String,Object> data, String state) {
	        String updateSql = "UPDATE CS_MAIN_SERVICE_PROBLEM SET main_state=?,ACCEPT_ACTOR_NAME=?,ACCEPT_ACTOR_ID=?,ACCEPT_DEPT_ID=?,ACCEPT_DEPT_NAME=?,GUID=? WHERE MAIN_PROBLEM_ID=? ";   
	        Object [] params = {
	        state,
	        MapUtils.getString(data,"nextActorName",null),
	        MapUtils.getString(data,"nextActorId",null),
	        MapUtils.getString(data,"nextDeptId",null),
	        MapUtils.getString(data,"nextDeptName",null),
	        MapUtils.getString(data,"GUID",null),
	        MapUtils.getString(data,"mainProblemId",null)   
	     };
	       getDataAccess().execNoQuerySql(updateSql, params);
    }
    /**
     * 同步副单状态
     * @param queryData,page
     * @return
     */
    public void dealServiceProblemState(Map<String, Object> data, Page page) {
    	String insertSql = "INSERT INTO CS_DEAL_SERVICE_PROBLEM(MAIN_PROBLEM_ID,deal_problem_id,deal_opinion,deal_time,DEAL_ACTOR_NAME,DEAL_ACTOR_ID,DEAL_DEPT_NAME,DEAL_DEPT_ID,IS_SMS_NOTICE,NEXT_ACTOR_NO, " +
        "attachment_address,attachment_type,attachment_name,NEXT_ACTOR_NAME,NEXT_ACTOR_ID,NEXT_DEPT_NAME,NEXT_DEPT_ID,deal_state " +
        ") VALUES (?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        Long pk = queryForNextVal("SEQ_MAG_NOTICE_ID");
        Object [] params = {
        MapUtils.getString(data,"mainProblemId",null),
        pk,
        MapUtils.getString(data,"dealOpinion",null),
        MapUtils.getString(data,"dealActorName",null),
        MapUtils.getString(data,"dealActorId",null),
        MapUtils.getString(data,"dealDeptName",null),
        MapUtils.getString(data,"dealDeptId",null),
        MapUtils.getString(data,"isSMSNotice",null),
        MapUtils.getString(data,"nextActorNo",null),
        MapUtils.getString(data,"attachmentAddress",null),
        MapUtils.getString(data,"attachmentType", null) ,
        MapUtils.getString(data,"attachmentName",null) ,
        MapUtils.getString(data,"nextActorName",null),
        MapUtils.getString(data,"nextActorId",null),
        MapUtils.getString(data,"nextDeptName",null),
        MapUtils.getString(data,"nextDeptId",null),
        MapUtils.getString(data,"dealState",null)        
     };
        getDataAccess().execQuerySql(insertSql,params);
    }  
    /**
     * 处理过程增加退单
     * @param queryData,page
     * @return
     */
    public void returnDealServiceProblem(Map<String, Object> data, Page page) {
	        String insertSql = "INSERT INTO CS_DEAL_SERVICE_PROBLEM(MAIN_PROBLEM_ID,deal_problem_id,deal_opinion,deal_time,DEAL_ACTOR_NAME,DEAL_ACTOR_ID,DEAL_DEPT_NAME,DEAL_DEPT_ID,IS_SMS_NOTICE,NEXT_ACTOR_NO, " +
	        "attachment_address,attachment_type,attachment_name,NEXT_ACTOR_NAME,NEXT_ACTOR_ID,NEXT_DEPT_NAME,NEXT_DEPT_ID,deal_state" +
	        ") VALUES (?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	        String state="4";
	        Long pk = queryForNextVal("SEQ_MAG_NOTICE_ID");
	        Object [] params = {
	        MapUtils.getString(data,"mainProblemId",null),
	        pk,
	        MapUtils.getString(data,"returnReason",null),
	        MapUtils.getString(data,"dealActorName",null),
	        MapUtils.getString(data,"dealActorId",null),
	        MapUtils.getString(data,"dealDeptName",null),
	        MapUtils.getString(data,"dealDeptId",null),
	        MapUtils.getString(data,"isSMSNotice",null),
	        MapUtils.getString(data,"returnNo",null),//
	        MapUtils.getString(data,"attachmentAddress",null),
	        MapUtils.getString(data,"attachmentType", null) ,
	        MapUtils.getString(data,"attachmentName",null) ,
	        MapUtils.getString(data,"nextActorName",null),
	        MapUtils.getString(data,"nextActorId",null),
	        MapUtils.getString(data,"nextDeptName",null),
	        MapUtils.getString(data,"nextDeptId",null),
	        MapUtils.getString(data,"dealState",state)
	     };
	        getDataAccess().execQuerySql(insertSql,params);
    }

	public Map<String, Object> getMainPromblemById(String key) {
		String sql = "select * from CS_MAIN_SERVICE_PROBLEM where MAIN_PROBLEM_ID='"+ key + "'";
		return getDataAccess().queryForMap(sql);
	}
	
	public List<Map<String,Object>> getMainPromblemByAddressUrl(Map<String, Object> param) {
		String sql = "select * from CS_MAIN_SERVICE_PROBLEM where PROCESS_LIMITED=? and ACCEPT_ACTOR_ID=? and main_state='1'";
		String ewamFlag = param.get("ewamFlag").toString();
		if("1".equals(ewamFlag)){
			sql += "and ewam_flag = 1";
		}
		return getDataAccess().queryForList(sql, param.get("startTime").toString(),param.get("executor").toString());
	}
	
	public List<Map<String,Object>> getMainPromblemByAddressUrl2(Map<String, Object> param) {
		String sql = "select * from CS_MAIN_SERVICE_PROBLEM a , not_statisfy_charge_manager b where a.first_accept_actor_id=b.staff_code and b.city_id='200'";
		return getDataAccess().queryForList(sql);
	}
	
	public void deleteByAddressUrl(Map<String, Object> param) {
		String sql = "delete from CS_MAIN_SERVICE_PROBLEM where PROCESS_LIMITED='"+param.get("startTime").toString()+"' and ACCEPT_ACTOR_ID='"+param.get("executor").toString()+"'";
		getDataAccess().execUpdate(sql);
	}
	
	public void updateMianstate(String guid){
		String sql = "update CS_MAIN_SERVICE_PROBLEM set main_state=?,finish_time=sysdate where guid=?";
		getDataAccess().execUpdate(sql,0,guid);
	}
	
	public void delMianstate(String guid){
		String sql = "update CS_MAIN_SERVICE_PROBLEM set main_state=? where guid=?";
		getDataAccess().execUpdate(sql,0,guid);
	}
	
	public Map<String, Object> getMainPromblemByMainId(String guid) {
		String sql = "select * from CS_MAIN_SERVICE_PROBLEM where GUID='"+ guid + "'";
		return getDataAccess().queryForMap(sql);
	}
	
	public void deleteByGUID(String guid) {
		String sql = "delete from CS_MAIN_SERVICE_PROBLEM where guid='"+guid+"'";
		getDataAccess().execUpdate(sql);
	}
 }