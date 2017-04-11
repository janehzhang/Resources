package tydic.meta.module.report.myReport;

import java.util.List;
import java.util.Map;

import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.web.session.SessionManager;

/**
 * 我创建的报表DAO
 * @author 李国民
 * Date：2012-03-19
 */
public class MyReportDAO extends MetaBaseDAO{
	
	/**
	 * 查询我创建的报表
	 * @param page 
	 * @return
	 */
	public List<Map<String,Object>> getMyReportList(Page page){
        int userId = SessionManager.getCurrentUserID();
		String sql = "SELECT T.REPORT_ID,T.REPORT_NAME,T.REPORT_NOTE,R.COL_ALIAS," +
				" to_char(T.CREATE_TIME,'yyyy-mm-dd hh24:mi:ss') AS CREATE_TIME," +
				" decode(L.NUM, '', 0, L.NUM) AS FLAG" +
				" FROM META_RPT_TAB_REPORT_CFG T" +
				" LEFT JOIN (SELECT A.REPORT_ID, COUNT(A.USE_LOG_ID) AS NUM " +
					" FROM META_RPT_USE_LOG A WHERE A.USER_ID = ? " +
					" GROUP BY A.REPORT_ID) L ON T.REPORT_ID = L.REPORT_ID" +
				" LEFT JOIN (SELECT REPORT_ID, wm_concat(COL_ALIAS) AS COL_ALIAS" +
					" FROM META_RPT_TAB_OUTPUT_CFG M" +
					" LEFT JOIN META_RPT_MODEL_ISSUE_COLS N ON M.COLUMN_ID = N.COLUMN_ID" +
					" GROUP BY REPORT_ID) R ON R.REPORT_ID = T.REPORT_ID" +
				" WHERE T.USER_ID = ? AND T.REPORT_STATE<>-1" +
				" ORDER BY T.CREATE_TIME DESC, T.REPORT_ID DESC";
		if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
		return getDataAccess().queryForList(sql,userId,userId);
	}
	
	/**
	 * 根据id删除对应报表
	 * @param reportId
	 * @return
	 */
	public boolean deleteReportById(int reportId){
		String sql = "DELETE FROM META_RPT_TAB_REPORT_CFG T WHERE T.REPORT_ID=?";
		return getDataAccess().execNoQuerySql(sql,reportId);
	}
}
