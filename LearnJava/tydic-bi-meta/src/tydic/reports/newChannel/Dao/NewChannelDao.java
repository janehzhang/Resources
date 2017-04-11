package tydic.reports.newChannel.Dao;


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
import tydic.reports.CommonUtils.CommonUtils;

/***
 * 政企报表改动
 * @author 我爱家乡
 *
 */
public class NewChannelDao extends MetaBaseDAO{
	/***
	 * 获取到最新的月份
	 * @param zqType
	 * @return
	 */
	public String getMonthData(String zqType){
		String sql ="select max(month_id) month_no from "+CommonUtils.getTableName(zqType);
		
		return getDataAccess().queryForString(sql);
	}
	/***
	 * 获取所有的月份
	 * @param zqType
	 * @return
	 */
	public List<Map<String, Object>> getAllMonthData(String zqType){
		String sql="select month_id  from (select distinct month_id as month_id from "+CommonUtils.getTableName(zqType)+"  order by month_id desc)";
		return getDataAccess().queryForList(sql);
	}
	/***
	 * 执行存储过程 展现数据信息
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> exeCallReplaceProcedure(Map<String,Object> queryData){
		Map<String, Object> mapList = new HashMap<String, Object>();
		String proName = CommonUtils.getProName(queryData.get("zqType").toString());
		Object[] params = new Object[]{MapUtils.getString(queryData, "dateTime", null),
				MapUtils.getString(queryData, "zoneCode", null),
				MapUtils.getString(queryData, "numberType", null),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(proName, len);
		ResultSet rs = null;
		try {
			CallableStatement statement = getDataAccess().execQueryCall(sql,params);
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
