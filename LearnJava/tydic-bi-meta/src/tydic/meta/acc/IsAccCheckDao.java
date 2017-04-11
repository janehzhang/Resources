package tydic.meta.acc;

import tydic.meta.common.MetaBaseDAO;

public class IsAccCheckDao extends MetaBaseDAO {
	public String IsAccCheck(String userName){
		String str = "SELECT DISTINCT(A.IS_ACC_CHECK) FROM META_USER.META_MAG_USER A WHERE A.USER_NAMECN = ? OR A.USER_NAMEEN = ?";
		
		StringBuffer sql = new StringBuffer();
		sql.append(str);
		return getDataAccess().queryForString(sql.toString(),userName,userName); 
	}
}
