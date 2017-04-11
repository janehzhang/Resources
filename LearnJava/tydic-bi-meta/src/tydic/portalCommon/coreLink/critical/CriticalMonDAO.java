package tydic.portalCommon.coreLink.critical;

import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;

public class CriticalMonDAO extends MetaBaseDAO {
    /**
     * 
      * 方法描述：
      * @param: 
      * @return: 
      * @version: 1.0
      * @author: yanhaidong
      * @version: 2013-4-16 下午02:37:39
     */
	public String getNewMonth() {
			String sql = "SELECT TO_DATE(MAX(t.MONTH_ID), 'yyyy-MM') MONTH_ID FROM CS_BUSI_CRITICAL_MON t";
			return getDataAccess().queryForString(sql);
	}

	
	/**
	  * 方法描述：
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-4-16 下午02:46:35
	  */
	public List<Map<String, Object>> getTableData(Map<String, Object> map) {
		String dateTime = Convert.toString(map.get("dateTime")).replaceAll("-","");
		String zoneCode = Convert.toString(map.get("zoneCode"));
		StringBuffer sql = new StringBuffer();
		sql.append("select a.CRITICAL_NUM ,a.TOTAL_CMPL_NUM,a.NUM1,round(a.CRITICAL_PER*1000,4) CRITICAL_PER ,b.zone_name from CS_BUSI_CRITICAL_MON a "
				+ " left join meta_dim_zone b on a.region_id = b.zone_code "
				+ " where a.month_id = '" + dateTime + "' "
				+ " and (b.zone_code = '" + zoneCode
				+ "' or b.zone_par_code = '" + zoneCode + "') "
				+ " ORDER BY b.dim_level, b.ORDER_ID");
		return getDataAccess().queryForList(sql.toString());

	}
	
	
	
}
