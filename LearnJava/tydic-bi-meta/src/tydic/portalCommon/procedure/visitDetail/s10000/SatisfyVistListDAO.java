package tydic.portalCommon.procedure.visitDetail.s10000;

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
 * @version: 2013-6-25 上午11:51:00 
 */
public class SatisfyVistListDAO extends MetaBaseDAO{
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
		String storeName = ConstantStoreProc.RPT_VISIT_SATISFY_10000_DETALL;
		Object[] params = {
				MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
				MapUtils.getString(map, "zoneCode",  null),
				//MapUtils.getString(map, "actType",   null),
				MapUtils.getString(map, "indexType", null),
				//MapUtils.getString(map, "reasonId",  null),
				MapUtils.getString(map, "satisType",  null),
				MapUtils.getString(map, "notSatisType2",  null),
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
