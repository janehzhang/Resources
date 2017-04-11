/**
 * 文件名：FaultDetailDAO.java
 * 版本信息：Version 1.0
 * 日期：2013-5-21
 * Copyright tydic.com.cn Corporation 2013 
 * 版权所有
 */
package tydic.portalCommon.procedure.faultDetail;

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
public class FaultDetailDAO extends MetaBaseDAO {

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
		String storeName = ConstantStoreProc.RPT_FAULT__LIST;
		Object[] params = {
				MapUtils.getString(map, "startTime", null).replaceAll("-", ""),
				MapUtils.getString(map, "endTime",   null).replaceAll("-", ""),
				MapUtils.getString(map, "zoneCode",  null),
				MapUtils.getString(map, "prodType",  null),
				MapUtils.getString(map, "indexId",   null),
			    page.getStartRow(),
				page.getEndRow(),
				new DBOutParameter(OracleTypes.CURSOR)};
		int len = params.length;
		String sql = convertStore(storeName, len);
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
	        Object endTime =   paramMap.get("endTime");
	        Object zoneCode =  paramMap.get("zoneCode");
	        Object prodType =  paramMap.get("prodType");
	        Object indexId =   paramMap.get("indexId");
	       
	        String startDateTime=Convert.toString(startTime).replaceAll("-", "");
		    StringBuffer sql=new StringBuffer();
		                  sql.append("SELECT COUNT(DAY_ID) FROM TBAS_DM.FT_MAINT_D_"+startDateTime.substring(0, 6)+" f WHERE 1=1");
	                  
		              List<Object> params=new ArrayList<Object>();
	                  if(startTime != null && !"".equals(startTime)){
	                      sql.append(" AND f.day_id >=?");
	                      params.add(startDateTime);
	                  }   
	                  if(endTime != null && !"".equals(endTime)){
	                      sql.append(" AND f.day_id <=?");
	                      params.add(Convert.toString(endTime).replaceAll("-",   ""));
	                  }   
	                  if(zoneCode != null && !"".equals(zoneCode))
	                  {
	                	  sql.append(" AND f.area_treecode like (select treecode from meta_dim_zone z where zone_code =?)||'%' ");
	                	  params.add(Convert.toString(zoneCode));
	                  }
	                  
	                  if(prodType != null && !"".equals(prodType)){
	                      sql.append(" AND f.PROD_TYPE_ID = ?");
	                      params.add(Convert.toString(prodType));
	                  } 
	                  if(indexId != null && !"".equals(indexId)){
	                      sql.append(" AND('"+indexId+"' is null " +
	                      		         " or ('"+indexId+"'='1' ) " +
			                      		 " or ('"+indexId+"'='2' and ARCHIVE_TYPE_ID='1' and NO_SERV_FLAG='1') " +
			                      		 " or ('"+indexId+"'='3' and ARCHIVE_TYPE_ID='1' and NO_SERV_FLAG='0') )");
	                     //params.add(Convert.toString(indexId));
	                  } 
	                  sql.append(" AND f.bill_type_id='0001'");
	         return getDataAccess().queryForInt(sql.toString(), params.toArray());
	}

}
