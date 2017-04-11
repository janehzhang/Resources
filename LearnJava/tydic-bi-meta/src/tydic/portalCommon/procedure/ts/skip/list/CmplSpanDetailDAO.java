/**
  * 文件名：CmplSpanDetailDAO.java
  * 版本信息：Version 1.0
  * 日期：2013-8-22
  * Copyright tydic.com.cn Corporation 2013 
  * 版权所有
  */
package tydic.portalCommon.procedure.ts.skip.list;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DBOutParameter;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.yhd.constant.ConstantStoreProc;
import tydic.meta.common.yhd.utils.Pager;


/**
 * 类描述：
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-8-22 下午04:57:58 
 */
public class CmplSpanDetailDAO extends MetaBaseDAO{
	
	public String getNewMonth() {
		String sql = "select to_date(max(t.MONTH_ID),'yyyy-MM')   month_no  from TBAS_DM.FT_SPAN_CMPL t";
		return getDataAccess().queryForString(sql);
	}
	
	
	/**
	 * 方法描述：分页显示记录
	 * 
	 * @param:
	 * @return:
	 * @version: 1.0
	 * @author: yanhaidong
	 * @version: 2013-5-21 下午04:17:15
	 */
	public Map<String, Object> getTableData(Map<String, Object> map, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_CMPL_SPAN_DETALL;
		Object[] params = {
				MapUtils.getString(map, "dateTime", null),
				MapUtils.getString(map, "zoneCode",  null),
				MapUtils.getString(map, "dataType",  null),
			    page.getStartRow(),
				page.getEndRow(),
				new DBOutParameter(OracleTypes.INTEGER),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
			mapList.put("allPageCount", statement.getInt(len-1));
			rs = (ResultSet) statement.getObject(len);
			List<Map<String, Object>> dataColumn = new ArrayList<Map<String, Object>>();
			dataColumn = rsToMaps(rs);
			ResultSetMetaData rsm = rs.getMetaData();
			String[]  headColumn = new String[rsm.getColumnCount()];
			for (int i = 0; i < headColumn.length; i++) {
				headColumn[i]=rsm.getColumnLabel(i+ 1);
			}
			mapList.put("headColumn", headColumn);
			mapList.put("dataColumn", dataColumn);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.getDataAccess().close(rs);
		}
		return mapList;
	}
}
