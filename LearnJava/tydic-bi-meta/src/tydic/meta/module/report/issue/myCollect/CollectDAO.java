package tydic.meta.module.report.issue.myCollect;

import tydic.meta.common.MetaBaseDAO;

import java.util.List;
import java.util.Map;

public class CollectDAO extends MetaBaseDAO {
	// 获取所有顶层节点内容
	public List<Map<String, Object>> findAllCollectType(int userId) {
		String sql="SELECT A.FAVORITE_ID, A.FAVORITE_NAME, A.PARENT_ID, A.FAVORITE_ORDER, A.USER_ID, DECODE (NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN FROM META_MAG_FAVORITE_DIR A LEFT JOIN (SELECT PARENT_ID, COUNT(1) CNT FROM META_MAG_FAVORITE_DIR GROUP BY PARENT_ID) C ON A.FAVORITE_ID = C.PARENT_ID WHERE A.PARENT_ID = 0 AND A.USER_ID="+userId+" ORDER BY A.FAVORITE_ORDER ASC ";
		return getDataAccess().queryForList(sql);
	}

	// 查看该节点下是否有子节点
	public List<Map<String, Object>> findSubCollectType(int parentId, int userId) {
		String sql="SELECT A.FAVORITE_ID, A.FAVORITE_NAME, A.PARENT_ID, A.FAVORITE_ORDER, A.USER_ID, DECODE (NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN FROM META_MAG_FAVORITE_DIR A LEFT JOIN (SELECT PARENT_ID, COUNT(1) CNT FROM META_MAG_FAVORITE_DIR GROUP BY PARENT_ID) C ON A.FAVORITE_ID = C.PARENT_ID WHERE A.PARENT_ID = "+parentId+" AND A.USER_ID="+userId+" ORDER BY A.FAVORITE_ORDER ASC ";
		return getDataAccess().queryForList(sql);
	}
	// 获得该用户收藏的所有报表信息
	public List<Map<String, Object>> findAllCollectRpt(int userId, int stationId) {
		String sql = "SELECT A.REPORT_ID, A.REPORT_NAME, C.COL_ALIAS, F.NUM,A.REPORT_NOTE, G.PUSH_TYPE FROM META_RPT_TAB_REPORT_CFG A, META_RPT_TAB_OUTPUT_CFG B,META_RPT_MODEL_ISSUE_COLS C,  META_RPT_USER_FAVORITE E, META_RPT_PUSH_CONFIG G, " +
				" (SELECT D.REPORT_ID,COUNT(1) NUM FROM  META_RPT_USE_LOG D WHERE D.USE_STATION_ID = "+ stationId+ " GROUP BY D.REPORT_ID) F" +
				"  WHERE E.REPORT_ID = A.REPORT_ID AND A.REPORT_ID = B.REPORT_ID AND B.COLUMN_ID = C.COLUMN_ID AND A.REPORT_ID = F.REPORT_ID AND A.REPORT_ID = G.REPORT_ID AND A.USER_ID = " + userId;
		return getDataAccess().queryForList(sql);
	}

	public int cancelCollectRpt(int userId, int reportId) {
		String sql = "DELETE FROM META_RPT_USER_FAVORITE A WHERE A.USER_ID = "+userId+" AND A.REPORT_ID = "+reportId+"";
		return getDataAccess().execUpdate(sql);
	}
	
}
