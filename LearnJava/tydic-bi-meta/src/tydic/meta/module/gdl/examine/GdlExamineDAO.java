package tydic.meta.module.gdl.examine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.gdl.GdlConstant;
import tydic.meta.module.gdl.GdlDAO;
import tydic.meta.sys.code.CodeManager;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * 
 * @author 刘弟伟
 * @description 指标审核DAO
 * @date 12-6-5 -
 * @modify
 * @modifyDate -
 */
public class GdlExamineDAO extends GdlDAO {

	/**
	 * @Title: getExamineInfo
	 * @Description: 获取指标审核信息
	 * @return List<Map<String,Object>> 
	 * @throws
	 */
	public List<Map<String, Object>> getExamineInfo(Map<String, Object> data,Page page) {
		 String gdlGroup = MapUtils.getString(data,"GDLTYPE").trim();
		 String keyWord = MapUtils.getString(data,"KEYWORD").trim();
		 String alertType = MapUtils.getString(data,"ALERTTYPE");
		 String gdlState = MapUtils.getString(data,"GDLSTATE");
		 String sql = "";
		StringBuffer buffer = new StringBuffer(); 
		buffer.append("SELECT A.GDL_ID,A.GDL_CODE,A.GDL_NAME,A.GDL_TYPE,");
		buffer.append("B.AUDIT_STATE,A.GDL_UNIT,A.USER_ID,C.USER_NAMECN, ");
		buffer.append("A.GDL_BUS_DESC,B.ALTER_TYPE,A.GDL_VERSION ");
		buffer.append("FROM META_GDL A, META_GDL_ALTER_HISTORY B, META_MAG_USER C ");
        buffer.append("WHERE A.GDL_VERSION = B.GDL_VERSION AND A.GDL_ID = B.GDL_ID ");
        buffer.append(" AND A.USER_ID = C.USER_ID AND");
        buffer.append(" a.GDL_VERSION = (SELECT MAX(x.GDL_VERSION) FROM META_GDL x WHERE x.GDL_ID = a.GDL_ID)");
        
        if(gdlGroup!=null && !"".equals(gdlGroup)){
        	 buffer.append(" AND A.GDL_ID IN (SELECT GDL_ID FROM META_GDL_GROUP_REL WHERE GDL_GROUP_ID IN "+SqlUtils.inParamDeal(gdlGroup.split(","))+")");
        }
        
        if(!"0".equals(alertType)){
        	buffer.append(" AND B.ALTER_TYPE="+alertType);
        }

        if(gdlState!=null && !"".equals(gdlState)){
        	buffer.append(" AND B.AUDIT_STATE="+gdlState);
        }

		if(keyWord!=null && !"".equals(keyWord)){
			buffer.append(" AND (A.GDL_NAME LIKE '%"
					+ keyWord + "%' or A.GDL_BUS_DESC LIKE '%"
					+ keyWord + "%' or A.GDL_CODE LIKE '%"+keyWord+"%')");
		}
		buffer.append(" ORDER BY A.CREATE_TIME");
		 if(page!=null){
	            sql = SqlUtils.wrapPagingSql(buffer.toString(),page);
	        }
		List<Map<String,Object>> list = getDataAccess().queryForList(sql);
		for(Map<String,Object> map : list){
            map.put("GDL_TYPE_NAME", CodeManager.getName(GdlConstant.GDL_TYPE_CODE_NAME, MapUtils.getString(map, "GDL_TYPE")));
            map.put("AUDIT_STATE_NAME", CodeManager.getName(GdlConstant.GDL_AUDIT_STATE_NAME, MapUtils.getString(map, "AUDIT_STATE")));
            map.put("ALTER_TYPE_NAME", CodeManager.getName(GdlConstant.GDL_ALTER_TYPE_NAME, MapUtils.getString(map, "ALTER_TYPE")));
		}
		return list;
	}

	/**
	 * 根据指标ID及版本获取指标基本信息
	 * 
	 * @param gdlId gdlVersion version
	 * @return map
	 */
	public Map<String, Object> getExamineInfoByGdlId(int gdlId, int gdlVersion , String version) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT A.GDL_ID,A.GDL_CODE,A.GDL_NAME,A.GDL_TYPE,A.GDL_SRC_TABLE_NAME,");
		buffer.append("A.GDL_SRC_TABLE_ID,A.GDL_SRC_COL,A.GDL_SRC_COL_ID,A.GDL_COL_NAME,");
		buffer.append("A.GDL_BUS_DESC,A.SRC_CALC_GROUP,A.GDL_CALC_EXPR,A.GDL_UNIT,");
		buffer.append("A.USER_ID,B.USER_NAMECN,TO_CHAR(A.CREATE_TIME,'YYYY-MM-DD hh24:mi:ss') AS CREATE_TIME,");
		buffer.append("A.VALID_TIME,A.GDL_STATE,A.GDL_VERSION, A.NUM_FORMAT,C.AUDIT_STATE,C.AUDIT_OPINION,C.ALTER_VERSION ");
		buffer.append("FROM META_GDL A,META_MAG_USER B ,META_GDL_ALTER_HISTORY C ");
		buffer.append("WHERE A.GDL_ID=?  AND A.USER_ID=B.USER_ID ");
		buffer.append("AND A.GDL_ID=C.GDL_ID ");
		Map<String,Object> map = new HashMap<String,Object>();
		if("befor".equals(version)){  //查询修改前指标信息,进行审核 
			buffer.append(" AND A.GDL_VERSION=C.GDL_VERSION AND A.GDL_STATE = 1");
			map = getDataAccess().queryForMap(buffer.toString(), gdlId);
		}else if("Current".equals(version)){                       //查询当前指标信息，进行审核 
			buffer.append(" AND A.GDL_VERSION=C.GDL_VERSION AND A.GDL_VERSION=?");
			map = getDataAccess().queryForMap(buffer.toString(), gdlId, gdlVersion);
		}else{                       //查询修改前指标信息查看                                
			buffer.append(" AND A.GDL_VERSION= (SELECT ALTER_VERSION FROM META_GDL_ALTER_HISTORY T WHERE GDL_ID=? AND GDL_VERSION =? )");
			map = getDataAccess().queryForMap(buffer.toString(), gdlId,gdlId, gdlVersion);
		}
		
		map.put("GDL_TYPE_NAME", CodeManager.getName(GdlConstant.GDL_TYPE_CODE_NAME, MapUtils.getString(map, "GDL_TYPE")));
		map.put("AUDIT_STATE_NAME", CodeManager.getName(GdlConstant.GDL_AUDIT_STATE_NAME, MapUtils.getString(map, "AUDIT_STATE")));
        return map;
	}
	/**
	 * 审核指标信息
	 * 
	 * @param data
	 * @return int
	 */
	public int examineGdlInfo(Map<String, Object> data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE META_GDL SET GDL_CODE=?,GDL_NAME=?,GDL_UNIT=? ");
		
		if("2".equals(data.get("effectivetime").toString())){  //为2时表示延时生效
			buffer.append(",VALID_TIME =TO_DATE(?,'YYYY-MM-DD hh24:mi:ss'),GDL_STATE =2 WHERE GDL_ID=? AND GDL_VERSION=?");
			
			return getDataAccess().execUpdate(buffer.toString(), data.get("gdl_code").toString(),
					data.get("gdl_name").toString(),
					data.get("gdl_unit").toString(),
					data.get("valid_time").toString(),
					data.get("gdl_id").toString(),
					data.get("gdl_version").toString());
		}
		buffer.append(",GDL_STATE =1,VALID_TIME =sysdate WHERE GDL_ID=? AND GDL_VERSION=?");
		
		return getDataAccess().execUpdate(buffer.toString(), data.get("gdl_code").toString(),
				data.get("gdl_name").toString(),
				data.get("gdl_unit").toString(), 
				data.get("gdl_id").toString(),
				data.get("gdl_version").toString());
	}
	
	/**
	 * 修改指标审核
	 * 
	 * @param data
	 * @return int
	 */
	public int alertExamine(Map<String, Object> data) {
		String sql = "";
		if("2".equals(data.get("effectivetime").toString())){  //为2时表示延时生效
			sql = "UPDATE META_GDL SET VALID_TIME =TO_DATE(?,'YYYY-MM-DD hh24:mi:ss'),GDL_STATE =2 WHERE GDL_ID=? AND GDL_VERSION=?";
			
			return getDataAccess().execUpdate(sql,
					data.get("valid_time").toString(),
					data.get("gdl_id").toString(),
					data.get("gdl_version").toString());
		}else{
			sql = "UPDATE META_GDL SET GDL_STATE =1,VALID_TIME =sysdate WHERE GDL_ID=? AND GDL_VERSION=?";
			
			return getDataAccess().execUpdate(sql,
				data.get("gdl_id").toString(),
				data.get("gdl_version").toString());
		}
	}
	
	/**
	 * 修改审核时，当审核状态为立即生效时，将上一版本指标状态设为无效
	 * 
	 * @param data
	 * @return int
	 */
	public int setGdlState(Map<String, Object> data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE META_GDL SET GDL_STATE =0 WHERE GDL_ID=? ");
		
		//如果审核为立即生效，将上个版本指标状态改为无效
		if("1".equals(data.get("effectivetime").toString())){
			buffer.append(" AND GDL_VERSION =?");
			
			return getDataAccess().execUpdate(buffer.toString(),
					data.get("gdl_id").toString(),
					data.get("alter_version").toString());
		}else{//当审核状态为延时生效时，将之前指标状态为延时待生效的状态置0
			buffer.append(" AND GDL_STATE =2");
			
			return getDataAccess().execUpdate(buffer.toString(),
					data.get("gdl_id").toString());
		}
	}
	/**
	 * 更新指标审核操作表
	 * 
	 * @param data
	 * @return int
	 */
	public int updateHistory(Map<String, Object> data,
			Map<String, Object> userInfo,String opr) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UPDATE META_GDL_ALTER_HISTORY SET AUDIT_USER_ID=?,USER_ZONE_ID=?,");
		buffer.append("USER_DEPT_ID=?,USER_STATION_ID=?,AUDIT_STATE=?,AUDIT_OPINION=?,AUDIT_TIME=SYSDATE ");
		
		if("alert".equals(opr)){  //修改审核时，记录修改之前版本号
			buffer.append(",ALTER_VERSION=? ");
			buffer.append("WHERE GDL_ID=? AND GDL_VERSION=?");
			
			return getDataAccess().execUpdate(buffer.toString(), userInfo.get("USER_ID").toString(),
					userInfo.get("ZONE_ID").toString(),
					userInfo.get("DEPT_ID").toString(),
					userInfo.get("STATION_ID").toString(),
					data.get("audit").toString(),
					data.get("audit_opinion").toString(),
					data.get("alter_version").toString(),
					data.get("gdl_id").toString(),
					data.get("gdl_version").toString());
		}else{   //新增审核时，不记录版本号
			buffer.append("WHERE GDL_ID=? AND GDL_VERSION=?");
			
			return getDataAccess().execUpdate(buffer.toString(), userInfo.get("USER_ID").toString(),
					userInfo.get("ZONE_ID").toString(),
					userInfo.get("DEPT_ID").toString(),
					userInfo.get("STATION_ID").toString(),
					data.get("audit").toString(),
					data.get("audit_opinion").toString(),
					data.get("gdl_id").toString(),
					data.get("gdl_version").toString());
		}
	}

	/**
	 * @Title: getUserInfo
	 * @Description: 根据userID查询用户信息
	 * @return String
	 * @throws
	 */
	public Map<String, Object> getUserInfo(String userID) {
		String sql = "SELECT USER_ID,USER_EMAIL,USER_NAMECN,STATION_ID,DEPT_ID,ZONE_ID,VIP_FLAG,GROUP_ID FROM META_MAG_USER T WHERE USER_ID=?";
		
		return getDataAccess().queryForMap(sql, userID);
	}
	
    /**
     * 方法：删除当前指标下的所有分组信息
     * @param data
     * @return  int
    */
    public int deleteGdlGroupInfo(Map<String,Object> data)
    {
    	String sql = "DELETE FROM META_GDL_GROUP_REL WHERE GDL_ID=?"; 
    	
    	return getDataAccess().execUpdate(sql,data.get("gdl_id").toString());
    }
    /**
     * 方法：保存当前指标下的所有分组信息
     * @param data
     * @return  boolean
    */
    public boolean saveGdlGroupInfo(String gdl_id,String gdl_group_id){
		String sql = "INSERT INTO META_GDL_GROUP_REL(GDL_ID,GDL_GROUP_ID) VALUES(?,?)";
		
		return getDataAccess().execNoQuerySql(sql, gdl_id,gdl_group_id);
    }
    
    /**
     * 方法：删除当前指标下的维度计算方法
     * @param data
     * @return  int
    */
    public int deleteDimGroupMethod(String gdlId,String gdlVersion)
    {
    	String sql = "DELETE FROM META_GDL_DIM_GROUP_METHOD WHERE GDL_ID=? AND GDL_VERSION=?"; 
    	
    	return getDataAccess().execUpdate(sql,gdlId,gdlVersion);
    }
    
    /**
     * 方法：保存当前指标下的维度计算方法
     * @param data
     * @return  boolean
    */
    public boolean saveDimGroupMethod(Map<String,Object> data,Map<String,Object> gdlGroupInfo){
		String sql = "INSERT INTO META_GDL_DIM_GROUP_METHOD(GDL_VERSION,GDL_ID,DIM_TABLE_ID,GROUP_METHOD) VALUES(?,?,?,?)";
		
		return getDataAccess().execNoQuerySql(sql, 
				data.get("gdl_version").toString(),
				data.get("gdl_id").toString(),
				gdlGroupInfo.get("DIM_TABLE_ID").toString(),
				gdlGroupInfo.get("GROUP_METHOD").toString());
    }
    
    /**
     * 获取新增审核 操作指标的支撑维度
     * @param gdlId
     * @return
     */
    public List<Map<String,Object>> getGdlDims_add(int gdlId,int gdlVersion){
    	List<Map<String,Object>> list = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT B.DIM_TABLE_ID,B.GROUP_METHOD, ");
        buffer.append("(SELECT TABLE_NAME_CN FROM META_TABLES WHERE TABLE_ID = B.DIM_TABLE_ID AND TABLE_STATE=1) DIM_NAME_CN ");
        buffer.append("FROM META_GDL A, META_GDL_DIM_GROUP_METHOD B ");
        buffer.append("WHERE A.GDL_ID=B.GDL_ID AND B.GDL_VERSION = A.GDL_VERSION AND B.GDL_ID = ? ");
        buffer.append("AND B.GDL_VERSION=? ");
        list = getDataAccess().queryForList(buffer.toString(),gdlId,gdlVersion);
        
        for(Map<String,Object> map : list){
            map.put("GROUP_METHOD_NAME", CodeManager.getName(GdlConstant.GDL_GROUP_METHOD_NAME, MapUtils.getString(map, "GROUP_METHOD")));
		}
        return list;
    }

    /**
     * 获取修改审核操作指标的修改前及修改后的技撑维度
     * @param gdlId
     * @return
     */
    public List<Map<String,Object>> getGdlDims_alert(int gdlId,int gdlVersion,String oldVersion){
    	List<Map<String,Object>> list = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT K.GROUP_METHOD AS OLD_GROUP_METHOD,K.DIM_NAME_CN AS OLD_DIM_NAME_CN, ");
        buffer.append("J.GROUP_METHOD AS NEW_GROUP_METHOD,J.DIM_NAME_CN AS NEW_DIM_NAME_CN from ");
        buffer.append("(SELECT B.DIM_TABLE_ID, B.GROUP_METHOD, ");
        buffer.append(" (SELECT TABLE_NAME_CN FROM META_TABLES WHERE TABLE_ID = B.DIM_TABLE_ID AND TABLE_STATE=1) DIM_NAME_CN ");
    	buffer.append(" FROM META_GDL A, META_GDL_DIM_GROUP_METHOD B");
    	buffer.append(" WHERE A.GDL_ID=B.GDL_ID AND B.GDL_VERSION = A.GDL_VERSION AND B.GDL_ID =? ");
    	buffer.append(" AND B.GDL_VERSION=? ) K ");
    	buffer.append(" full join ");
    	buffer.append("(SELECT B.DIM_TABLE_ID, B.GROUP_METHOD, ");
    	buffer.append("  (SELECT TABLE_NAME_CN FROM META_TABLES WHERE TABLE_ID = B.DIM_TABLE_ID AND TABLE_STATE=1) DIM_NAME_CN ");
    	buffer.append("  FROM META_GDL A, META_GDL_DIM_GROUP_METHOD B ");
    	buffer.append("  WHERE A.GDL_ID=B.GDL_ID AND B.GDL_VERSION = A.GDL_VERSION AND B.GDL_ID =? ");
    	buffer.append("  AND B.GDL_VERSION=? ) J");
    	buffer.append("  ON K.DIM_TABLE_ID=J.DIM_TABLE_ID");
    	
    	list = getDataAccess().queryForList(buffer.toString(),gdlId,oldVersion,gdlId,gdlVersion);
        for(Map<String,Object> map : list){
            map.put("OLD_GROUP_METHOD_NAME", CodeManager.getName(GdlConstant.GDL_GROUP_METHOD_NAME, MapUtils.getString(map, "OLD_GROUP_METHOD")));
            map.put("NEW_GROUP_METHOD_NAME", CodeManager.getName(GdlConstant.GDL_GROUP_METHOD_NAME, MapUtils.getString(map, "NEW_GROUP_METHOD")));
    	}
        return list;
    }

    /**
	 * @Title: getExamineInfo
	 * @Description: 获取某条基础指标下的所有复合指标
	 * @return List<Map<String,Object>> 
	 * @throws
	 */
	public List<Map<String, Object>> getGdl_Child(Map<String, Object> data) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT A.GDL_ID,A.GDL_VERSION FROM META_GDL A WHERE ");
		buffer.append("A.GDL_ID IN(SELECT DISTINCT A.GDL_ID FROM META_GDL_REL A  CONNECT BY PRIOR A.GDL_ID = A.PAR_GDL_ID START WITH A.PAR_GDL_ID = ?) ");
		buffer.append("AND A.GDL_STATE=1");
		
		return getDataAccess().queryForList(buffer.toString(),data.get("gdl_id").toString());
	}

}
