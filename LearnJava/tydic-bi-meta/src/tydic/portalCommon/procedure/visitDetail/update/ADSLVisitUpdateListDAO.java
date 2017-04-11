package tydic.portalCommon.procedure.visitDetail.update;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.jdbc.OracleTypes;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DBOutParameter;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.yhd.constant.ConstantStoreProc;
import tydic.meta.common.yhd.utils.Pager;

/**
 * 类描述：
 * 
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-5-21 下午04:15:56
 */
public class ADSLVisitUpdateListDAO extends MetaBaseDAO {

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
		String storeName = ConstantStoreProc.RPT_VISIT_SATISFY_KD_UP_DETALL;
		Object[] params = {
				MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
				MapUtils.getString(map, "zoneCode",  null),
				//MapUtils.getString(map, "actType",   null),
				MapUtils.getString(map, "indexType", null),
				//MapUtils.getString(map, "reasonId",  null),
				MapUtils.getString(map, "satisType",  null),
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
	
	
	/**
	 * 方法描述：分页IVR修障显示记录
	 * 
	 * @param:
	 * @return:
	 * @version: 1.0
	 * @author: Chenwei Guang
	 * @version: 2015-1-28 上午11:38
	 */
	public Map<String, Object> getTableDataIVRXZ(Map<String, Object> map, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName="";
		String ripId = MapUtils.getString(map, "ripId",  null);
		if("1".equals(ripId)){
			 storeName = ConstantStoreProc.RPT_VISIT_IVRXZ_KD_UP_DETALL_N;
		}
		else if("14".equals(ripId)){
			 storeName = ConstantStoreProc.RPT_VISIT_IVRXZ_GK_DETALL;
		}
		else{
			 storeName = ConstantStoreProc.RPT_VISIT_IVRXZ_KD_UP_DETALL;
		}
		
		Object[] params = {
				MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
				MapUtils.getString(map, "zoneCode",  null),
				//MapUtils.getString(map, "actType",   null),
				MapUtils.getString(map, "indexType", null),
				//MapUtils.getString(map, "reasonId",  null),
				MapUtils.getString(map, "satisType",  null),
				MapUtils.getString(map, "notSatisType1",  null),
				MapUtils.getString(map, "notSatisType2",  null),
				MapUtils.getString(map, "visitChannel",  null),
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
	
	public boolean updateVisitResultIVRXZ(Map<String, Object> param) {
		String dateName=MapUtils.getString(param, "date", null);
		dateName=dateName.replaceAll("-", "");
	    StringBuffer sql = new StringBuffer("UPDATE TBAS_DM.FT_VISIT_RESULT_IVRXZ_"+dateName.substring(0, 6)+" SET VISIT_RESULT=? WHERE ORDER_ID=?");
	    Object[] params = {MapUtils.getString(param,  "name", null) , 
	    		           MapUtils.getString(param,  "id",   null)};
	    return getDataAccess().execUpdate(sql.toString(), params) != -1;
	}

	
	/**
	 * 
	  * 方法描述：获得总记录数
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-5-23 下午01:56:48
	 */
	public Integer getDataCount(Map<String, Object> paramMap) {
		Object startTime = paramMap.get("startTime");
		Object endTime = paramMap.get("endTime");
		Object zoneCode = paramMap.get("zoneCode");
		//Object actType= paramMap.get("actType");
		Object indexType = paramMap.get("indexType");
		Object reasonId = paramMap.get("reasonId");
		Object satisType = paramMap.get("satisType");// 满意度

		String startDateTime = Convert.toString(startTime).replaceAll("-", "");
		startDateTime = startDateTime.substring(0, 6);
		String endDateTime = Convert.toString(endTime).replaceAll("-", "");
		endDateTime = endDateTime.substring(0, 6);
		String ripId = MapUtils.getString(paramMap, "ripId",  null);
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
	    sql.append("select sum(cnt) from (");
		sql.append("SELECT COUNT(ID) cnt FROM  TBAS_DM.FT_VISIT_RESULT_"+ startDateTime + " f WHERE 1=1");
		if (startTime != null && !"".equals(startTime)) {
			sql.append(" AND f.DAY_ID >=?");
			params.add(Convert.toString(startTime).replaceAll("-", ""));
		}
		if (endTime != null && !"".equals(endTime)) {
			sql.append(" AND f.DAY_ID <=?");
			params.add(Convert.toString(endTime).replaceAll("-", ""));
		}
		if (zoneCode != null && !"".equals(zoneCode)) {
			sql.append(" AND f.REGION_TREECODE like (select treecode from meta_dim_zone z where zone_code =?)||'%' ");
			params.add(Convert.toString(zoneCode));
		}
		/*
		 * if(actType != null && !"".equals(actType)) {
		 * sql.append(" AND f.ACT_TYPE =? ");
		 * params.add(Convert.toString(actType)); }
		 */
		sql.append(" AND f.ACT_TYPE ='1' ");

		if (indexType != null && !"".equals(indexType)
				&& !indexType.equals("0")) {
			sql.append(" AND f.ON_SITE is not null  ");
			if (indexType.equals("2")) {
				sql.append(" and SATIS in('0','1','2')");
			}
			if (indexType.equals("3")) {
				sql.append(" and nvl(SATIS,3) not in('0','1','2') ");
			}
			// params.add(Convert.toString(indexType));
		}
		if (reasonId != null && !"".equals(reasonId)) {
			sql.append(" AND REASON like '"
					+ Convert.toString(reasonId)
					+ "%' and ON_SITE is not null and nvl(SATIS,3) not in('0','1','2')");

		}
		if (satisType != null && !"".equals(satisType)) {
			sql.append(" AND SATIS =?");
			params.add(Convert.toString(satisType));
		}
		
		if (!startDateTime.equals(endDateTime)) {
			sql.append(" union all ");
			sql.append("SELECT COUNT(ID) cnt FROM  TBAS_DM.FT_VISIT_RESULT_"+ endDateTime.substring(0, 6) + " f WHERE 1=1");
			if (startTime != null && !"".equals(startTime)) {
				sql.append(" AND f.DAY_ID >=?");
				params.add(Convert.toString(startTime).replaceAll("-", ""));
			}
			if (endTime != null && !"".equals(endTime)) {
				sql.append(" AND f.DAY_ID <=?");
				params.add(Convert.toString(endTime).replaceAll("-", ""));
			}
			if (zoneCode != null && !"".equals(zoneCode)) {
				sql.append(" AND f.REGION_TREECODE like (select treecode from meta_dim_zone z where zone_code =?)||'%' ");
				params.add(Convert.toString(zoneCode));
			}
			/*
			 * if(actType != null && !"".equals(actType)) {
			 * sql.append(" AND f.ACT_TYPE =? ");
			 * params.add(Convert.toString(actType)); }
			 */
			sql.append(" AND f.ACT_TYPE ='1' ");

			if (indexType != null && !"".equals(indexType)
					&& !indexType.equals("0")) {
				sql.append(" AND f.ON_SITE is not null  ");
				if (indexType.equals("2")) {
					sql.append(" and SATIS in('0','1','2')");
				}
				if (indexType.equals("3")) {
					sql.append(" and nvl(SATIS,3) not in('0','1','2') ");
				}
				// params.add(Convert.toString(indexType));
			}
			if (reasonId != null && !"".equals(reasonId)) {
				sql.append(" AND REASON like '"
						+ Convert.toString(reasonId)
						+ "%' and ON_SITE is not null and nvl(SATIS,3) not in('0','1','2')");

			}
			if (satisType != null && !"".equals(satisType)) {
				sql.append(" AND SATIS =?");
				params.add(Convert.toString(satisType));
			}
		}
		
		sql.append(")");
		return getDataAccess().queryForInt(sql.toString(), params.toArray());
	}


	
    /**
     *  营业厅回访清单过程
     */
	public Map<String, Object> getHfTableData(Map<String, Object>  map,Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName="";
		Object[] params =null;
		if("20".equals(map.get("actType"))){
		     storeName = ConstantStoreProc.RPT_VISIT_SATISFY_BUSI_DET_O;
		     params = new Object[]{MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
					MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
					MapUtils.getString(map, "zoneCode",  null),
					MapUtils.getString(map, "indexType", null),
					MapUtils.getString(map, "satisType",  null),
					MapUtils.getString(map, "actType",   null),
				    page.getStartRow(),
					page.getEndRow(),
					new DBOutParameter(OracleTypes.INTEGER),
					new DBOutParameter(OracleTypes.CURSOR)};
		}else if("9".equals(map.get("actType"))){
			 storeName = ConstantStoreProc.RPT_VISIT_SATISFY_ISHOP_DETALL;
			 params = new Object[]{MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
						MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
						MapUtils.getString(map, "zoneCode",  null),
						MapUtils.getString(map, "indexType", null),
						MapUtils.getString(map, "satisType",  null),
					    page.getStartRow(),
						page.getEndRow(),
						new DBOutParameter(OracleTypes.INTEGER),
						new DBOutParameter(OracleTypes.CURSOR)};
		}else if("300".equals(map.get("actType"))){
			 storeName = ConstantStoreProc.RPT_VISIT_SATISFY_WX_DETALL;
			 params = new Object[]{MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
						MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
						MapUtils.getString(map, "zoneCode",  null),
						MapUtils.getString(map, "indexType", null),
						MapUtils.getString(map, "satisType",  null),
					    page.getStartRow(),
						page.getEndRow(),
						new DBOutParameter(OracleTypes.INTEGER),
						new DBOutParameter(OracleTypes.CURSOR)};
		}
		else if("10".equals(map.get("actType"))){
			 storeName = ConstantStoreProc.RPT_VISIT_SATISFY_DX_DETALL;
			 params = new Object[]{MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
					MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
					MapUtils.getString(map, "zoneCode",  null),
					MapUtils.getString(map, "indexType", null),
					MapUtils.getString(map, "satisType",  null),
					MapUtils.getString(map, "testMethod",  null),
					page.getStartRow(),
					page.getEndRow(),
					new DBOutParameter(OracleTypes.INTEGER),
					new DBOutParameter(OracleTypes.CURSOR)};
		}	
		else if("15".equals(map.get("actType"))||"18".equals(map.get("actType"))||"19".equals(map.get("actType"))){
			map.put("dateType", "0");
			map.put("actType", map.get("actType"));
			map.put("rptId", "ishop1");
			map.put("startDate", map.get("startTime"));
			map.put("endDate", map.get("endTime"));
			return getStaffSumData(map, page);
		}
		else if("16".equals(map.get("actType"))||"17".equals(map.get("actType"))){
			map.put("dateType", "0");
			map.put("actType", map.get("actType"));
			map.put("rptId", "kdxz");
			map.put("startDate", map.get("startTime"));
			map.put("endDate", map.get("endTime"));
			return getInstallerSumData(map, page);
		}
		else{
			 storeName = ConstantStoreProc.RPT_VISIT_SATISFY_BUSI_DETALL;
			 params = new Object[]{MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
					MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
					MapUtils.getString(map, "zoneCode",  null),
					MapUtils.getString(map, "indexType", null),
					MapUtils.getString(map, "satisType",  null),
					MapUtils.getString(map, "testMethod",  null),
					page.getStartRow(),
					page.getEndRow(),
					new DBOutParameter(OracleTypes.INTEGER),
					new DBOutParameter(OracleTypes.CURSOR)};
		}
		 
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
	
	public Map<String, Object> getVipTableData(Map<String, Object> map, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_VISIT_SATISFY_VIP_DETALL;
		Object[] params = {
				MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
				MapUtils.getString(map, "zoneCode",  null),
				//MapUtils.getString(map, "actType",   null),
				MapUtils.getString(map, "indexType", null),
				//MapUtils.getString(map, "reasonId",  null),
				MapUtils.getString(map, "satisType",  null),
				MapUtils.getString(map, "visitChannel",  null),
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

	/**
	 * 实体渠道营业厅-营业员一级数据满意度测评
	 * @param map
	 * @param page
	 * @return
	 */
	public Map<String, Object> getStaffSumData(Map<String, Object>  map,Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName="";
		Object[] params =null;
			 storeName = "RPT_ISHOP_SATISFY_AREA_STAFF";
			 params = new Object[]{ MapUtils.getString(map, "startDate", null).replaceAll("-", ""),
					    MapUtils.getString(map, "endDate", null).replaceAll("-", ""),
						MapUtils.getString(map, "zoneCode", null),
						MapUtils.getString(map, "dateType", null),
						MapUtils.getString(map, "actType", null),
						MapUtils.getString(map, "rptId", null),
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
	
	/**
	 * 宽带装维-装维人员满意度测评
	 * @param map
	 * @param page
	 * @return
	 */
	public Map<String, Object> getInstallerSumData(Map<String, Object>  map,Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName="";
		Object[] params =null;
			 storeName = "RPT_KD_SATISFY_AREA_INSTALLER";
			 params = new Object[]{ MapUtils.getString(map, "startDate", null).replaceAll("-", ""),
					    MapUtils.getString(map, "endDate", null).replaceAll("-", ""),
						MapUtils.getString(map, "zoneCode", null),
						MapUtils.getString(map, "dateType", null),
						MapUtils.getString(map, "actType", null),
						MapUtils.getString(map, "rptId", null),
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
	
	/**
	 * 预警监控异常数据报表
	 * @param map
	 * @param page
	 * @return
	 */
	public Map<String, Object> getEwamSumFaultData(Map<String, Object>  map,Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_VISIT_EWAM_FAULTDATA;
		Object[] params =null;
			 //storeName = "RPT_KD_SATISFY_AREA_INSTALLER";
			 params = new Object[]{ MapUtils.getString(map, "startDate", null).replaceAll("-", ""),
					    MapUtils.getString(map, "endDate", null).replaceAll("-", ""),
						MapUtils.getString(map, "zoneCode", null),
						MapUtils.getString(map, "dateType", null),
						MapUtils.getString(map, "vTypeId", null),
						MapUtils.getString(map, "targetId", null),
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
	
	/**
	 * 预警监控异常数据清单
	 * @param map
	 * @param page
	 * @return
	 */
	public Map<String, Object> getEwamFaultDataList(Map<String, Object>  map,Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_VISIT_EWAM_DETALL;
		Object[] params =null;
			 //storeName = "RPT_KD_SATISFY_AREA_INSTALLER";
			 params = new Object[]{ MapUtils.getString(map, "startDate", null).replaceAll("-", ""),
					    MapUtils.getString(map, "endDate", null).replaceAll("-", ""),
						MapUtils.getString(map, "zoneCode", null),
						MapUtils.getString(map, "dateType", null),
						MapUtils.getString(map, "vTypeId", null),
//						MapUtils.getString(map, "targetId", null),
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
	
    /**
     *  营业厅回访清单过程
     */
	public Map<String, Object> getTsTableData(Map<String, Object>   map,Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_VISIT_SATISFY_CMPL_DETALL;
		Object[] params = {
				MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
				MapUtils.getString(map, "zoneCode",  null),
				//MapUtils.getString(map, "actType",   null),
				MapUtils.getString(map, "indexType", null),
				MapUtils.getString(map, "reasonId",  null),
				MapUtils.getString(map, "satisType",  null),
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


	
	//智能监控清单
	public Map<String, Object> getMonitorSumData(Map<String, Object> map,Pager page) {
		
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName="";
		Object[] params =null;
			 storeName = ConstantStoreProc.RPT_VISIT_EWAM_DETALL;
			 params = new Object[]{ 
					    MapUtils.getString(map, "startDate", null).replaceAll("-", ""),
					    MapUtils.getString(map, "endDate", null).replaceAll("-", ""),
						MapUtils.getString(map, "zoneCode", null),
						MapUtils.getString(map, "vType", null),		//筛选测评体系
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
		
		System.out.println(mapList.size());
		return mapList;	

	}
}
