package tydic.meta.module.mag.reason;
import java.util.List;
import java.util.Map;
import tydic.frame.BaseDAO;

public class ReasonDAO extends BaseDAO {
	public List<Map<String, Object>> queryReasonByPathCode(String beginId, String endId) {
	   String sql = "SELECT A.TYPE_ID,A.TYPE_PAR_ID,A.TYPE_NAME,A.DIM_LEVEL,A.STATE,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
			+ " FROM META_DM.D_V_JSFH_10000_DICT A"
			+ " LEFT JOIN (SELECT TYPE_PAR_ID, COUNT(1) CNT"
			+ " FROM META_DM.D_V_JSFH_10000_DICT"
			+ " GROUP BY TYPE_PAR_ID) C"
			+ " ON A.TYPE_ID = C.TYPE_PAR_ID" 
			+ " WHERE A.TYPE_ID =?";
			Object[] proParams ={endId};
			return getDataAccess().queryForList(sql, proParams);
	}
	public List<Map<String, Object>> queryIVRZYJReasonByPathCode(String beginId, String endId) {
		   String sql = "SELECT A.TYPE_ID,A.TYPE_PAR_ID,A.TYPE_NAME,A.DIM_LEVEL,A.STATE,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
				+ " FROM META_DM.D_V_JSFH_IVRZYJ_DICT A"
				+ " LEFT JOIN (SELECT TYPE_PAR_ID, COUNT(1) CNT"
				+ " FROM META_DM.D_V_JSFH_IVRZYJ_DICT"
				+ " GROUP BY TYPE_PAR_ID) C"
				+ " ON A.TYPE_ID = C.TYPE_PAR_ID" 
				+ " WHERE A.TYPE_ID =?";
				Object[] proParams ={endId};
				return getDataAccess().queryForList(sql, proParams);
		}
	public List<Map<String, Object>> queryIVRXZReasonByPathCode(String beginId, String endId) {
		   String sql = "SELECT A.TYPE_ID,A.TYPE_PAR_ID,A.TYPE_NAME,A.DIM_LEVEL,A.STATE,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
				+ " FROM META_DM.D_V_JSFH_IVRXZ_DICT A"
				+ " LEFT JOIN (SELECT TYPE_PAR_ID, COUNT(1) CNT"
				+ " FROM META_DM.D_V_JSFH_IVRXZ_DICT"
				+ " GROUP BY TYPE_PAR_ID) C"
				+ " ON A.TYPE_ID = C.TYPE_PAR_ID" 
				+ " WHERE A.TYPE_ID =?";
				Object[] proParams ={endId};
				return getDataAccess().queryForList(sql, proParams);
		}
    public List<Map<String, Object>> querySubReasonCode(String parentCode) {
      String select ="SELECT A.TYPE_ID,A.TYPE_PAR_ID,A.TYPE_NAME,A.DIM_LEVEL,A.STATE,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
				+ " FROM META_DM.D_V_JSFH_10000_DICT A"
				+ " LEFT JOIN (SELECT TYPE_PAR_ID, COUNT(1) CNT"
				+ " FROM META_DM.D_V_JSFH_10000_DICT"
				+ " GROUP BY TYPE_PAR_ID) C"
				+ " ON A.TYPE_ID = C.TYPE_PAR_ID" 
				+ " WHERE A.TYPE_PAR_ID = ? and type_Id <13"
				+ " ORDER BY dim_level, ORDER_ID ASC";
		Object[] proParams = {parentCode};
		return getDataAccess().queryForList(select, proParams);
	}
    public List<Map<String, Object>> querySubIVRZYJReasonCode(String parentCode) {
        String select ="SELECT A.TYPE_ID,A.TYPE_PAR_ID,A.TYPE_NAME,A.DIM_LEVEL,A.STATE,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
  				+ " FROM META_DM.D_V_JSFH_IVRZYJ_DICT A"
  				+ " LEFT JOIN (SELECT TYPE_PAR_ID, COUNT(1) CNT"
  				+ " FROM META_DM.D_V_JSFH_IVRZYJ_DICT"
  				+ " GROUP BY TYPE_PAR_ID) C"
  				+ " ON A.TYPE_ID = C.TYPE_PAR_ID" 
  				+ " WHERE A.TYPE_PAR_ID = ? "
  				+ " ORDER BY dim_level, ORDER_ID ASC";
  		Object[] proParams = {parentCode};
  		return getDataAccess().queryForList(select, proParams);
  	}
    public List<Map<String, Object>> querySubIVRXZReasonCode(String parentCode) {
        String select ="SELECT A.TYPE_ID,A.TYPE_PAR_ID,A.TYPE_NAME,A.DIM_LEVEL,A.STATE,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
  				+ " FROM META_DM.D_V_JSFH_IVRXZ_DICT A"
  				+ " LEFT JOIN (SELECT TYPE_PAR_ID, COUNT(1) CNT"
  				+ " FROM META_DM.D_V_JSFH_IVRXZ_DICT"
  				+ " GROUP BY TYPE_PAR_ID) C"
  				+ " ON A.TYPE_ID = C.TYPE_PAR_ID" 
  				+ " WHERE A.TYPE_PAR_ID = ?"
  				+ " ORDER BY dim_level, ORDER_ID ASC";
  		Object[] proParams = {parentCode};
  		return getDataAccess().queryForList(select, proParams);
  	}
}
