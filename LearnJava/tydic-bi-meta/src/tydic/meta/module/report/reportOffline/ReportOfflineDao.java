
/**   
 * @文件名: ReportOfflineDao.java
 * @包 tydic.meta.module.report.reportOffline
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-3-20 下午03:45:27
 *  
 */
  
package tydic.meta.module.report.reportOffline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.report.ReportConstant;
import tydic.meta.sys.code.CodeManager;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：ReportOfflineDao   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-3-20 下午03:45:27   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class ReportOfflineDao extends MetaBaseDAO {
	/**
	 * @Title: getReportMes 
	 * @Description: 获取报表信息
	 * @param data
	 * @param page
	 * @return List<Map<String,Object>>   
	 * @throws
	 */
	public List<Map<String,Object>> getReportMes(Map<String,Object> data,Page page){
		List<Object> params = new ArrayList<Object>();
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT A.REPORT_ID,B.DEPT_NAME,B.STATION_NAME,B.USER_NAMECN,A.REPORT_NAME,A.OPERATE_TIME,A.REPORT_STATE FROM"); 
		buffer.append(" (SELECT A.*,B.USER_ID,B.USER_DEPT_ID,B.USER_STATION_ID,B.OPERATE_TIME FROM");
		buffer.append(" (SELECT A.REPORT_ID,A.REPORT_NAME,A.REPORT_STATE FROM META_RPT_TAB_REPORT_CFG A");
		buffer.append(" WHERE");
		if(data.get("reportId") != null){
			buffer.append(" A.REPORT_ID=?");
			params.add(data.get("reportId"));
		}else{
			//报表状态
			if(data.get("reportState") != null){
				if(!"-1".equals(data.get("reportState"))){
					buffer.append(" A.REPORT_STATE=?");
					params.add(data.get("reportState"));
				}else{
					buffer.append(" A.REPORT_STATE IN (0,1)");
				}
			}
			//关键字
			if(data.get("keyWord") != null && !"".equals(""+data.get("keyWord"))){
				buffer.append(" AND A.REPORT_NAME LIKE ? ESCAPE '/'");
				String keyWord = (data.get("keyWord")+"");
				params.add(SqlUtils.allLikeBindParam(keyWord));
			}
		}
		int operateType = ReportConstant.PEPORT_OPERATE_TYPE_OFFLINE;
		buffer.append(" ) A LEFT JOIN META_RPT_CONFIG_LOG B ON A.REPORT_ID=B.REPORT_ID");
		buffer.append(" AND B.OPERATE_TYPE="+operateType+") A,(SELECT A.*,S.STATION_NAME FROM (");
		buffer.append(" SELECT D.DEPT_NAME,U.DEPT_ID,U.STATION_ID,U.USER_ID,U.USER_NAMECN FROM");
		buffer.append(" META_DIM_USER_DEPT D,META_MAG_USER U WHERE D.DEPT_CODE=U.DEPT_ID");
		buffer.append(" ) A,META_DIM_USER_STATION S WHERE A.STATION_ID=S.STATION_CODE");
		buffer.append(" ) B WHERE A.USER_ID=B.USER_ID(+) AND A.USER_DEPT_ID=B.DEPT_ID(+) AND A.USER_STATION_ID=B.STATION_ID(+)");
		
		buffer.append(" ORDER BY A.REPORT_STATE DESC,A.REPORT_ID DESC");
		String sql = buffer.toString();
		if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
		List<Map<String,Object>> list = getDataAccess().queryForList(sql,params.toArray());
		//日期转换
		if(list != null && list.size() > 0){
			for(Map<String,Object> m : list){
				if(m.get("OPERATE_TIME") != null && !"null".equals(""+m.get("OPERATE_TIME")) && !"".equals(""+m.get("OPERATE_TIME")))
					m.put("OPERATE_TIME", DateUtil.getParamDay(m.get("OPERATE_TIME")+"", "yyyy-MM-dd HH:mm:ss"));
			}
		}
		return list;
	}
	/**
	 * @Title: insertReportSearchLog 
	 * @Description: 关键字查询日志记录
	 * @param userMap
	 * @param keyWord
	 * @param type 查询的类别：1报表搜索、2数据搜索  
	 * @return void   
	 * @throws
	 */
	public void insertReportSearchLog(Map<String,Object> userMap,String keyWord,String type){
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO META_MAG_USER_SEARCH_LOG(LOG_ID,USER_ZONE_ID,USER_DEPT_ID,USER_STATION_ID,");
		buffer.append(" USER_ID,KEYWORD,KEYWORD_TYPE)");
		buffer.append(" VALUES(?,?,?,?,?,?,?)");
		Object[] obj = new Object[]{
				queryForNextVal("SEQ_META_MAG_USER_SEARCH_LOG"),
				userMap.get("zoneId"),
				userMap.get("deptId"),
				userMap.get("stationId"),
				userMap.get("userId"),
				keyWord.toUpperCase(),
				type};
		getDataAccess().execNoQuerySql(buffer.toString(), obj);
	}
	/**
	 * @Title: getDataModelMesByRepId 
	 * @Description: 根据报表ID，获取数据模型信息
	 * @param reportId
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getDataModelMesByRepId2(String reportId){
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT A.*,B.SC_NUM,B.DY_NUM FROM (");
		buffer.append(" SELECT A.DATA_AUDIT_TYPE,A.ISSUE_ID,A.AUDIT_TYPE,A.TABLE_ALIAS,A.SUBSCRIBE_TYPE,A.REPORT_ID,B.* FROM (SELECT A.*,B.REPORT_ID FROM (");
		buffer.append(" SELECT A.TABLE_ID,A.ISSUE_ID,A.AUDIT_TYPE,A.TABLE_ALIAS,A.SUBSCRIBE_TYPE,");
		buffer.append(" A.ISSUE_NOTE,B.AUDIT_TYPE DATA_AUDIT_TYPE FROM META_RPT_MODEL_ISSUE_CONFIG A,META_RPT_DATA_AUDIT_CFG B WHERE A.ISSUE_ID=B.ISSUE_ID) A,(");
		buffer.append(" SELECT T.ISSUE_ID,T.REPORT_ID FROM META_RPT_TAB_REPORT_CFG T WHERE T.REPORT_ID=?");
		buffer.append(" ) B WHERE A.ISSUE_ID=B.ISSUE_ID) A,(");
		buffer.append(" SELECT Z.DATA_SOURCE_NAME,C.TABLE_NAME,C.TABLE_NAME_CN,C.TABLE_OWNER,C.TABLE_ID");
		buffer.append("  FROM META_DATA_SOURCE Z,META_TABLES C");
		buffer.append(" WHERE Z.DATA_SOURCE_ID=C.DATA_SOURCE_ID");
		buffer.append(" ) B WHERE A.TABLE_ID=B.TABLE_ID ) A LEFT JOIN ");
		buffer.append(" (SELECT NVL(SUM(DECODE(T.OPERATE_TYPE,11,1,0)),0) SC_NUM,T.REPORT_ID,");
		buffer.append(" NVL(SUM(DECODE(T.OPERATE_TYPE,21,1,0)),0) DY_NUM");
		buffer.append(" FROM META_RPT_USE_LOG T WHERE T.OPERATE_TYPE IN (11,21)");
		buffer.append(" AND T.REPORT_ID=?");
		buffer.append(" GROUP BY T.REPORT_ID) B ON A.REPORT_ID=B.REPORT_ID");
		Map<String,Object> rs = getDataAccess().queryForMap(buffer.toString(),new Object[]{reportId,reportId});
		//加入码表信息
        if(rs != null && rs.size() > 0){
        	rs.put("DATA_AUDIT_TYPE", CodeManager.getName(ReportConstant.REPORT_DATA_CYCLE,MapUtils.getString(rs,"DATA_AUDIT_TYPE")));
        }
        return rs;
	}
	/**
	 * @Title: getDataModelMesByRepId 
	 * @Description: 根据报表ID，获取数据模型信息
	 * @param reportId
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getDataModelMesByRepId(String reportId){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT A.*,B.DATA_SOURCE_NAME,B.TABLE_OWNER,B.TABLE_NAME_CN,B.TABLE_NAME FROM(SELECT A.* FROM (");
		buffer.append(" SELECT I.ISSUE_ID,I.TABLE_ID,I.TABLE_ALIAS,I.AUDIT_TYPE,I.ISSUE_NOTE,I.SUBSCRIBE_TYPE,");
		buffer.append(" A.AUDIT_TYPE DATA_AUDIT_TYPE");
		buffer.append(" FROM META_RPT_MODEL_ISSUE_CONFIG I,META_RPT_DATA_AUDIT_CFG A");
		buffer.append(" WHERE I.ISSUE_ID=A.ISSUE_ID) A,");
		buffer.append(" META_RPT_TAB_REPORT_CFG R WHERE A.ISSUE_ID=R.ISSUE_ID");
		buffer.append(" AND R.REPORT_ID=?");
		buffer.append(" ) A,(SELECT Z.DATA_SOURCE_NAME,C.TABLE_NAME,C.TABLE_NAME_CN,C.TABLE_OWNER,C.TABLE_ID");
		buffer.append(" FROM META_DATA_SOURCE Z, META_TABLES C");
		buffer.append(" WHERE Z.DATA_SOURCE_ID = C.DATA_SOURCE_ID AND C.TABLE_STATE=1) B");
		buffer.append(" WHERE A.TABLE_ID=B.TABLE_ID");
		Map<String,Object> rs = getDataAccess().queryForMap(buffer.toString(),new Object[]{reportId});
		//加入码表信息
        if(rs != null && rs.size() > 0){
        	rs.put("DATA_AUDIT_TYPE", CodeManager.getName(ReportConstant.REPORT_DATA_CYCLE,MapUtils.getString(rs,"DATA_AUDIT_TYPE")));
        }
        return rs;
	}
	/**
	 * @Title: getReportMesByRepId 
	 * @Description: 根据报表ID获取报表信息
	 * @param reportId
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getReportMesByRepId(String reportId){
		StringBuffer buffer = new StringBuffer();
		int operateType = ReportConstant.PEPORT_OPERATE_TYPE_ADD;
		buffer.append(" SELECT A.ISSUE_ID,A.REPORT_ID,A.REPORT_NAME,A.REPORT_STATE,A.REPORT_KEYWORD,A.REPORT_NOTE,B.* FROM(");
		buffer.append(" SELECT A.REPORT_ID,A.REPORT_NAME,A.REPORT_STATE,A.REPORT_KEYWORD,A.REPORT_NOTE,");
		buffer.append(" B.USER_ID,B.USER_DEPT_ID,B.USER_ZONE_ID,A.ISSUE_ID FROM");
		buffer.append(" META_RPT_TAB_REPORT_CFG A,META_RPT_CONFIG_LOG B");
		buffer.append(" WHERE A.REPORT_ID=B.REPORT_ID AND B.OPERATE_TYPE="+operateType);
		buffer.append(" AND A.REPORT_ID=?");
		buffer.append(" ) A,(SELECT A.*,B.ZONE_NAME FROM (");
		buffer.append(" SELECT D.DEPT_NAME,U.DEPT_ID,U.STATION_ID,U.USER_ID,U.USER_NAMECN,");
		buffer.append(" U.USER_EMAIL,U.USER_MOBILE,U.ZONE_ID FROM");
		buffer.append(" META_DIM_USER_DEPT D,META_MAG_USER U WHERE D.DEPT_CODE=U.DEPT_ID");
		buffer.append(" ) A,META_DIM_ZONE B WHERE A.ZONE_ID=B.ZONE_ID) B");
		buffer.append(" WHERE A.USER_ID=B.USER_ID ");
//		buffer.append(" AND A.USER_DEPT_ID=B.DEPT_ID AND A.USER_ZONE_ID=B.ZONE_ID");
		Map<String,Object> rs = getDataAccess().queryForMap(buffer.toString(),new Object[]{reportId});
		if(rs != null && rs.size() > 0){
			//计算订阅数和收藏数
			buffer = null;
			buffer = new StringBuffer();
			buffer.append(" SELECT NVL(SUM(CASE WHEN L.OPERATE_TYPE=21 THEN 1 ELSE 0 END),0) DY_SUMNUM,");
			buffer.append(" NVL(SUM(CASE WHEN L.OPERATE_TYPE=11 THEN 1 ELSE 0 END),0) SC_SUMNUM");
			buffer.append(" FROM META_RPT_USE_LOG L WHERE L.REPORT_ID=?");
			Map<String,Object> m = getDataAccess().queryForMap(buffer.toString(),new Object[]{reportId});
			if(m != null && m.size() > 0){
				rs.put("DY_SUMNUM", m.get("DY_SUMNUM"));
				rs.put("SC_SUMNUM", m.get("SC_SUMNUM"));
			}
		}
		return rs;
	}
	/**
	 * @Title: getReportUseMesByRepId 
	 * @Description: 根据报表ID获取报表使用信息
	 * @param reportId
	 * @return Map<String,Object>   
	 * @throws
	 */
	public Map<String,Object> getReportUseMesByRepId(String reportId){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" SELECT A.*,B.SC_NUM,B.DY_NUM FROM (SELECT A.USE_TIME,A.REPORT_ID,B.* FROM ("); 
		buffer.append(" SELECT MAX(T.USE_TIME) USE_TIME,T.USER_ID,T.REPORT_ID");
		buffer.append(" FROM META_RPT_USE_LOG T");
		buffer.append(" WHERE T.USER_ID IS NOT NULL AND T.REPORT_ID=?");
		buffer.append(" GROUP BY T.USER_ID,T.REPORT_ID) A,");
		buffer.append(" (SELECT A.*,S.ZONE_NAME FROM (");
		buffer.append(" SELECT D.DEPT_NAME,U.DEPT_ID,U.STATION_ID,U.USER_ID,U.USER_NAMECN,U.ZONE_ID,U.USER_MOBILE,U.USER_EMAIL FROM");
		buffer.append(" META_DIM_USER_DEPT D,META_MAG_USER U WHERE D.DEPT_CODE=U.DEPT_ID");
		buffer.append(" ) A,META_DIM_ZONE S WHERE A.ZONE_ID=S.ZONE_ID");
		buffer.append(" ) B WHERE A.USER_ID=B.USER_ID) A,(");
		buffer.append(" SELECT SUM(DECODE(T.OPERATE_TYPE,11,1,0)) SC_NUM,T.REPORT_ID,");
		buffer.append(" SUM(DECODE(T.OPERATE_TYPE,21,1,0)) DY_NUM");
		buffer.append(" FROM META_RPT_USE_LOG T WHERE T.OPERATE_TYPE IN (11,21)");
		buffer.append(" AND T.REPORT_ID=?");
		buffer.append(" GROUP BY T.REPORT_ID) B");
		buffer.append(" WHERE A.REPORT_ID=B.REPORT_ID");
		Map<String,Object> rs = getDataAccess().queryForMap(buffer.toString(),new Object[]{reportId,reportId});
		if(rs != null && rs.get("USE_TIME") != null){
			rs.put("USE_TIME", DateUtil.getParamDay(rs.get("USE_TIME")+"", "yyyy-MM-dd HH:mm:ss"));
		}
		return rs;
	}
	/**
	 * @Title: updateReportStateByRepId 
	 * @Description: 根据报表ID下线操作
	 * @param reportId
	 * @return boolean   
	 * @throws
	 */
	public boolean updateReportStateByRepId(String reportId){
		String sql = "UPDATE META_RPT_TAB_REPORT_CFG SET REPORT_STATE=0 WHERE REPORT_ID=?";
		return getDataAccess().execNoQuerySql(sql,new Object[]{reportId});
	}
	/**
	 * @Title: insertReportLog 
	 * @Description: 新增下线记录
	 * @param reportId
	 * @param userMap
	 * @param @return    
	 * @return boolean   
	 * @throws
	 */
	public boolean insertReportLog(String reportId,Map<String,Object> userMap){
		StringBuffer buffer = new StringBuffer();
		int operateType = ReportConstant.PEPORT_OPERATE_TYPE_OFFLINE;
		buffer.append(" INSERT INTO META_RPT_CONFIG_LOG(LOG_ID,USER_ID,USER_ZONE_ID,USER_DEPT_ID,");
		buffer.append(" USER_STATION_ID,OPERATE_TYPE,OPERATE_TIME,OPERATE_OPINION,REPORT_ID)");
		buffer.append(" VALUES(?,?,?,?,?, ?,sysdate,?,?)");
		Object[] obj = new Object[]{
				queryForNextVal("SEQ_RPT_CONFIG_LOG_ID"),
				userMap.get("userId"),
				userMap.get("zoneId"),
				userMap.get("deptId"),
				userMap.get("stationId"),
				operateType,
				null,
				reportId};
		return getDataAccess().execNoQuerySql(buffer.toString(), obj);
	}
	
	public Map<String,Object> getIssueIdByReportId(String reportId){
		String sql = "SELECT ISSUE_ID FROM META_RPT_TAB_REPORT_CFG WHERE REPORT_ID=?";
		return getDataAccess().queryForMap(sql, new Object[]{reportId});
	}
}
