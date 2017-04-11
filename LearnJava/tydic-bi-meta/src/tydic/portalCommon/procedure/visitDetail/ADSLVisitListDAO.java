package tydic.portalCommon.procedure.visitDetail;

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
public class ADSLVisitListDAO extends MetaBaseDAO {

	/**
	 * 方法描述：分页显示记录
	 * 
	 * @param:
	 * @return:
	 * @version: 1.0
	 * @author: yanhaidong
	 * @version: 2013-5\-21 下午04:17:15
	 */
	public Map<String, Object> getTableData(Map<String, Object> map, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName = ConstantStoreProc.RPT_VISIT_SATISFY_KD_DETALL;
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
	 * 方法描述：分页显示IVR记录
	 * 
	 * @param:
	 * @return:
	 * @version: 1.0
	 * @author: Chenwei Guang
	 * @version: 2015-1-28 上午 10:33
	 */
	public Map<String, Object> getTableDataIVRZYJ(Map<String, Object> map, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName="";
		String ripId = MapUtils.getString(map, "ripId",  null);
		String rptId = MapUtils.getString(map, "rptId",  null);
		//String typeId = MapUtils.getString(map, "typeId",  null);
		
		if("1".equals(ripId)){
			storeName = ConstantStoreProc.RPT_VISIT_IVRZYJ_KD_DETALL_NEW;
		}
		else if("13".equals(ripId)){
			storeName = ConstantStoreProc.RPT_VISIT_IVRZYJ_GK_DETALL;
		}
		else if("21".equals(ripId)){
			storeName = "RPT_VISIT_IVRZYJ_NEW_KD_DETALL";
		}
		else if("22".equals(ripId)){
			storeName = "RPT_IVR_ZW_SATISFY_AREA_STAFF";
//			map.put("indexType", "12");
		}
		else{
			storeName = ConstantStoreProc.RPT_VISIT_IVRZYJ_KD_DETALL;
		}
		Object[] params = null;
		if("21".equals(ripId)){
		Object[] params2  = {
				MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
				MapUtils.getString(map, "zoneCode",  null),
				MapUtils.getString(map, "typeId",   null),
				MapUtils.getString(map, "indexType", null),
				//MapUtils.getString(map, "reasonId",  null),
				MapUtils.getString(map, "satisType",  null),
				MapUtils.getString(map, "notSatisType1",  null),
				MapUtils.getString(map, "notSatisType2",  null),
			    page.getStartRow(),
				page.getEndRow(),
				new DBOutParameter(OracleTypes.INTEGER),
				new DBOutParameter(OracleTypes.CURSOR)};
				params = params2;
		}
		else if("13".equals(ripId)){
			if("1".equals(rptId)){
				Object[] params1 = {
						MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
						MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
						MapUtils.getString(map, "zoneCode",  null),
						MapUtils.getString(map, "indexType",   null),
						//MapUtils.getString(map, "reasonId",  null),
						"1",
						MapUtils.getString(map, "satisType",  null),
						MapUtils.getString(map, "notSatisType1",  null),
						MapUtils.getString(map, "notSatisType2",  null),
					    page.getStartRow(),
						page.getEndRow(),
						new DBOutParameter(OracleTypes.INTEGER),
						new DBOutParameter(OracleTypes.CURSOR)};
				        params = params1;
			}
			else if("3".equals(rptId)){
				Object[] params1 = {
						MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
						MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
						MapUtils.getString(map, "zoneCode",  null),
						MapUtils.getString(map, "indexType",   null),
						//MapUtils.getString(map, "reasonId",  null),
						"3",
						MapUtils.getString(map, "satisType",  null),
						MapUtils.getString(map, "notSatisType1",  null),
						MapUtils.getString(map, "notSatisType2",  null),
					    page.getStartRow(),
						page.getEndRow(),
						new DBOutParameter(OracleTypes.INTEGER),
						new DBOutParameter(OracleTypes.CURSOR)};
				        params = params1;
			}
			else{
				Object[] params1 = {
						MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
						MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
						MapUtils.getString(map, "zoneCode",  null),
						MapUtils.getString(map, "indexType",   null),
						//MapUtils.getString(map, "reasonId",  null),
						"4",
						MapUtils.getString(map, "satisType",  null),
						MapUtils.getString(map, "notSatisType1",  null),
						MapUtils.getString(map, "notSatisType2",  null),
					    page.getStartRow(),
						page.getEndRow(),
						new DBOutParameter(OracleTypes.INTEGER),
						new DBOutParameter(OracleTypes.CURSOR)};
				        params = params1;
			}
		}
		else {
			Object[] params3 = {
					MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
					MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
					MapUtils.getString(map, "zoneCode",  null),
					MapUtils.getString(map, "indexType",   null),
//					MapUtils.getString(map, "indexType", null),
					//MapUtils.getString(map, "reasonId",  null),
					MapUtils.getString(map, "satisType",  null),
					MapUtils.getString(map, "notSatisType1",  null),
					MapUtils.getString(map, "notSatisType2",  null),
					MapUtils.getString(map, "visitChannel",  null),
				    page.getStartRow(),
					page.getEndRow(),
					new DBOutParameter(OracleTypes.INTEGER),
					new DBOutParameter(OracleTypes.CURSOR)};
					params = params3;
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
	
	
	public Map<String, Object> tsSatisfyVisitDetails(Map<String, Object> map, Pager page) {
		Map<String, Object> mapList =new  HashMap<String, Object>();
		String storeName="";
//		String ripId = MapUtils.getString(map, "ripId",  null);
	
			storeName = "RPT_TS_SATISFY_VISI_DETAILS";
		//need modfy
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
		sql.append(" AND f.ACT_TYPE ='0' ");

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
			sql.append(" AND f.ACT_TYPE ='0' ");

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
	  * 方法描述：更新回返结果
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-6-5 下午03:44:01
	  */
	public boolean updateVisitResult(Map<String, Object> param) {
		String dateName=MapUtils.getString(param, "date", null);
		dateName=dateName.replaceAll("-", "");
	    StringBuffer sql = new StringBuffer("UPDATE TBAS_DM.FT_VISIT_RESULT_T_"+dateName.substring(0, 6)+" SET VISIT_RESULT=?  WHERE  ID=?");
	    Object[] params = {MapUtils.getString(param,  "name", null) , 
	    		           MapUtils.getString(param,  "id",   null)};
	    return getDataAccess().execUpdate(sql.toString(), params) != -1;
	}
	
	
	public boolean updateVisitResultIVR(Map<String, Object> param) {
		String dateName=MapUtils.getString(param, "date", null);
		dateName=dateName.replaceAll("-", "");
	    StringBuffer sql = new StringBuffer("UPDATE TBAS_DM.FT_VISIT_RESULT_IVR_"+dateName.substring(0, 6)+" SET VISIT_RESULT=? WHERE day_id=? and ORDER_ID=? ");
	    Object[] params = {MapUtils.getString(param,  "name", null) ,
	    					dateName, 
	    		           MapUtils.getString(param,  "id",   null)};
	    return getDataAccess().execUpdate(sql.toString(), params) != -1;
	}
	
    /**
     * 营业厅
     */
	public boolean updateHfVisitResult(Map<String, Object> param) {
		String busiNo=MapUtils.getString(param,  "busiNo", "");
		String custNo=MapUtils.getString(param,  "custNo", "");
		String areaName=MapUtils.getString(param,"areaName", "");
		String createTime=MapUtils.getString(param, "createTime", "");
		String name=MapUtils.getString(param, "name", "");
	    StringBuffer sql = new StringBuffer("UPDATE TBAS_DM.FT_BUSI_VISIT_RESULT_T_"+createTime.substring(0, 6)+" SET VISIT_RESULT=?  WHERE  SALES_ID=? AND PHONE_NO=? AND REGION_NAME=? AND DAY_ID=?");
	    Object[] params = {name,busiNo, custNo,areaName,createTime};
	    return getDataAccess().execUpdate(sql.toString(), params) != -1;
	}
	/**
     * 实体渠道评价器
     */
	public boolean updateIshopVisitResult(Map<String, Object> param) {
		String busiNo=MapUtils.getString(param,  "busiNo", "");
		String custNo=MapUtils.getString(param,  "custNo", "");
		String areaName=MapUtils.getString(param,"areaName", "");
		String createTime=MapUtils.getString(param, "createTime", "");
		String name=MapUtils.getString(param, "name", "");
	    StringBuffer sql = new StringBuffer("UPDATE TBAS_DM.FT_ISHOP_VISIT_RESULT_"+createTime.substring(0, 6)+" SET VISIT_RESULT=?  WHERE  STAFF_ID=? AND RECEIPT_SEQ=? AND REGION_NAME=? AND DAY_ID=?");
	    Object[] params = {name,busiNo, custNo,areaName,createTime};
	    return getDataAccess().execUpdate(sql.toString(), params) != -1;
	}
	 /**
     * 10000号
     */
	public boolean updateS10000VisitResult(Map<String, Object> param) {
		String phone=MapUtils.getString(param,"phone", "");
		String date=MapUtils.getString(param, "date", "");
		date=date.replaceAll("-", "");
		String name=MapUtils.getString(param, "name", "");
	    StringBuffer sql = new StringBuffer("UPDATE TBAS_DM.FT_10000_VISIT_RESULT_"+date.substring(0, 6)+" SET VISIT_RESULT=?  WHERE  CALLERNO=? AND DAY_ID=?");
	    Object[] params = {name,phone,date.substring(0, 8)};
	    return getDataAccess().execUpdate(sql.toString(), params) != -1;
	}
	/**
     * 号百
     */
	public boolean updateHbVisitResult(Map<String, Object> param) {
		String phone=MapUtils.getString(param,"phone", "");
		String date=MapUtils.getString(param, "date", "");
		date=date.replaceAll("-", "");
		String name=MapUtils.getString(param, "name", "");
	    StringBuffer sql = new StringBuffer("UPDATE TBAS_DM.FT_HB_VISIT_RESULT_"+date.substring(0, 6)+" SET VISIT_RESULT=?  WHERE  CALLER_NO=? AND DAY_ID=?");
	    Object[] params = {name,phone,date.substring(0, 8)};
	    return getDataAccess().execUpdate(sql.toString(), params) != -1;
	}
	
    /***
     * 投诉
     */
	public boolean updateTsVisitResult(Map<String, Object> param) {
		String busiNo=MapUtils.getString(param,  "busiNo", "");
		String hjNo=MapUtils.getString(param,  "hjNo", "");
		String hfType=MapUtils.getString(param,"hfType", "");
		String countTime=MapUtils.getString(param, "countTime", "");
		String name=MapUtils.getString(param, "name", "");
	    StringBuffer sql = new StringBuffer("UPDATE TBAS_DM.FT_COMP_VISIT_RESULT_"+countTime.substring(0, 6)+" SET VISIT_RESULT=?  WHERE  BILL_ID=? AND TACHE_NO =? AND VISIT_TYPE_NAME =? AND DAY_ID=?");
	    Object[] params = {name,busiNo,hjNo,hfType,countTime};
	    return getDataAccess().execUpdate(sql.toString(), params) != -1;
	}
	/***
     * 掌厅
     */
	public boolean updateZtVisitResult(Map<String, Object> param) {
		String id=MapUtils.getString(param,  "id", "");
		String phone=MapUtils.getString(param,  "phone", "");
		String date=MapUtils.getString(param,  "date", "");
		date=date.substring(0,6);
		String name=MapUtils.getString(param, "name", "");
	    StringBuffer sql = new StringBuffer("UPDATE TBAS_DM.FT_WTZT_VISIT_RESULT_"+date+" SET VISIT_RESULT=?  WHERE  ID=? AND ORDER_NUMBER =?");
	    Object[] params = {name,id,phone};
	    return getDataAccess().execUpdate(sql.toString(), params) != -1;
	}
	/***
     * 网厅
     */
	public boolean updateWtVisitResult(Map<String, Object> param) {
		String id=MapUtils.getString(param,  "id", "");
		String phone=MapUtils.getString(param,  "phone", "");
		String date=MapUtils.getString(param,  "date", "");
		date=date.substring(0,6);
		String name=MapUtils.getString(param, "name", "");
	    StringBuffer sql = new StringBuffer("UPDATE TBAS_DM.FT_WTZT_VISIT_RESULT_"+date+" SET VISIT_RESULT=?  WHERE  ID=? AND ORDER_NUMBER =?");
	    Object[] params = {name,id,phone};
	    return getDataAccess().execUpdate(sql.toString(), params) != -1;
	}
}
