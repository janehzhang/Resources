package tydic.meta.module.mag.zone;

import tydic.frame.BaseDAO;
import tydic.frame.SystemVariable;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.Constant;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.mag.user.UserConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 * All rights reserved.
 * 该类是地域查询DAO类，用于连接数据库操作，供ZoneAction调用
 * @author 刘斌
 * @date 2011-9-26
 * ----------------------
 * @modify 程钰
 * 增加关联用户的DAO，修改初始化查询条件
 * @modifyDate 2011-10-11
 *
 * @modify  张伟 新增方法queryDeptByBeginEndPath
 * @modifyDate  2011-10-11
 *
 * @modify  王春生 新增方法queryAllZoneForBegin
 * @modifyDate  2012-3-12
 */
public class ZoneDAO extends BaseDAO {
    /**
     * 初始化页面查询
     * @param queryData
     * @return
     */
    public List<Map<String,Object>> queryZone(Map<?,?> queryData){
        String select = "SELECT A.ZONE_ID, A.ZONE_NAME, A.ZONE_PAR_ID, A.ZONE_CODE, A.ZONE_DESC, " +
                        "A.DIM_TYPE_ID, A.STATE, A.DIM_LEVEL,D.DIM_TYPE_NAME, DECODE(NVL(C.CNT,0),0,0,1) AS " +
                        "CHILDREN FROM META_DIM_ZONE A LEFT JOIN " +
                        "(SELECT ZONE_PAR_ID, COUNT(1) CNT FROM META_DIM_ZONE GROUP BY ZONE_PAR_ID) C " +
                        "ON A.ZONE_ID=C.ZONE_PAR_ID LEFT JOIN META_DIM_TYPE D ON D.DIM_TYPE_ID = A.DIM_TYPE_ID WHERE 1=1 ";
        List proParams = new ArrayList();
        if(queryData != null){
            Object zoneName = queryData.get("zoneName");
            Object parZoneName = queryData.get("parZoneName");
            if(zoneName!=null && !zoneName.toString().trim().equals("")){
                if(parZoneName != null && !parZoneName.toString().trim().equals("")){
                    select += "AND A.ZONE_PAR_ID NOT IN " +
                              "(SELECT D.ZONE_ID FROM META_DIM_ZONE D WHERE D.ZONE_NAME LIKE ?) ";
                    proParams.add(SqlUtils.allLikeBindParam(parZoneName.toString()));
                    select += "AND A.ZONE_NAME LIKE ? ESCAPE '/' " +
                              "CONNECT BY A.ZONE_PAR_ID = PRIOR A.ZONE_ID START WITH A.ZONE_ID IN( " +
                              "SELECT B.ZONE_ID FROM META_DIM_ZONE B WHERE B.ZONE_NAME LIKE ? ESCAPE '/' " +
                              "CONNECT BY ZONE_PAR_ID = PRIOR ZONE_ID START WITH ZONE_PAR_ID=0) ";
                    proParams.add(SqlUtils.allLikeBindParam(zoneName.toString()));
                    proParams.add(SqlUtils.allLikeBindParam(parZoneName.toString()));
                }else{
                    select += "AND A.ZONE_NAME LIKE ? ESCAPE '/' ";
                    proParams.add(SqlUtils.allLikeBindParam(zoneName.toString()));
                    select += "AND A.ZONE_PAR_ID NOT IN " +
                              "(SELECT D.ZONE_ID FROM META_DIM_ZONE D WHERE D.ZONE_NAME LIKE ?) ";
                    proParams.add(SqlUtils.allLikeBindParam(zoneName.toString()));
                }
            }else{
                if(parZoneName != null && !parZoneName.toString().trim().equals("")){
                    select += "AND A.ZONE_NAME LIKE ? ESCAPE '/' ";
                    proParams.add(SqlUtils.allLikeBindParam(parZoneName.toString()));
                    select += "AND A.ZONE_PAR_ID NOT IN " +
                              "(SELECT D.ZONE_ID FROM META_DIM_ZONE D WHERE D.ZONE_NAME LIKE ?) ";
                    proParams.add(SqlUtils.allLikeBindParam(parZoneName.toString()));
                }else{
                    select += "AND A.ZONE_PAR_ID=0 ";
                }
            }
        }else{
            select += "AND A.ZONE_PAR_ID=0 ";
        }
        select += " ORDER BY dim_level, ORDER_ID ASC";
        return getDataAccess().queryForList(select, proParams.toArray());
    }

    /**
     * 查询子地域
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> querySubZone(int parentId){
       /** String select = "SELECT A.ZONE_ID, A.ZONE_NAME, A.ZONE_PAR_ID, A.ZONE_CODE, A.ZONE_DESC, " +
                        "A.DIM_TYPE_ID, A.STATE, A.DIM_LEVEL, D.DIM_TYPE_NAME, DECODE(NVL(C.CNT,0),0,0,1) AS " +
                        "CHILDREN FROM META_DIM_ZONE A LEFT JOIN " +
                        "(SELECT ZONE_PAR_ID, COUNT(1) CNT FROM META_DIM_ZONE GROUP BY ZONE_PAR_ID) C " +
                        "ON A.ZONE_ID=C.ZONE_PAR_ID LEFT JOIN META_DIM_TYPE D ON D.DIM_TYPE_ID = A.DIM_TYPE_ID WHERE A.ZONE_PAR_ID=? ORDER BY  dim_level, ORDER_ID ASC";
        **/
    	String select="SELECT A.ZONE_ID,A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN" +
		" FROM META_DIM_ZONE A" +
		" LEFT JOIN (SELECT ZONE_PAR_ID, COUNT(1) CNT"+
	               " FROM META_DIM_ZONE"+
    		              " GROUP BY ZONE_PAR_ID) C"+
    		    " ON A.ZONE_ID = C.ZONE_PAR_ID"+
    			" WHERE A.ZONE_PAR_ID = ?" +
    			" ORDER BY dim_level, ORDER_ID ASC";
        Object[] proParams = {parentId};
        return getDataAccess().queryForList(select, proParams);
    }
    //营服中心
    public List<Map<String,Object>> querySubZoneToBuss(int parentId){
     	String select="SELECT A.ZONE_ID,A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN" +
     			" FROM Meta_Dim_Zone_Branch A" +
     			" LEFT JOIN (SELECT ZONE_PAR_ID, COUNT(1) CNT"+
     		               " FROM Meta_Dim_Zone_Branch"+
     		              " GROUP BY ZONE_PAR_ID) C"+
     		    " ON A.ZONE_ID = C.ZONE_PAR_ID"+
     			" WHERE A.ZONE_PAR_ID = ?" +
     			" ORDER BY dim_level, ORDER_ID ASC";
         Object[] proParams = {parentId};
         return getDataAccess().queryForList(select, proParams);
     }
    // 菜单树
    public List<Map<String,Object>> querySubMenu(int parentId){
     	String select="SELECT A.MENU_ID,A.MENU_NAME,A.PARENT_ID,A.IS_SHOW,A.ORDER_ID,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN" +
     			" FROM meta_mag_menu_apply A" +
     			" LEFT JOIN (SELECT PARENT_ID, COUNT(1) CNT"+
     		               " FROM meta_mag_menu_apply"+
     		              " GROUP BY PARENT_ID) C"+
     		    " ON A.MENU_ID = C.PARENT_ID"+
     			" WHERE A.PARENT_ID = ?" +
     			" ORDER BY ORDER_ID ASC";
         Object[] proParams = {parentId};
         return getDataAccess().queryForList(select, proParams);
     }
    
    /**
     * 根据地域关联用户
     *
     * @param condtions 限制条件
     * @param page 分页
     * @return
     */
    public List<Map<String,Object>> queryUserByCondition(Map<String,Object> condtions, Page page){
        StringBuffer sql = new StringBuffer("SELECT A.USER_NAMECN, A.USER_NAMEEN, A.USER_MOBILE, A.USER_EMAIL," +
                                            "A.USER_ID, A.USER_PASS,A.STATE, A.ADMIN_FLAG, A.CREATE_DATE, A.OA_USER_NAME, "+
                                            "B.DEPT_NAME, C.STATION_NAME, D.ZONE_NAME, A.HEAD_SHIP FROM " +
                                            "META_MAG_USER A LEFT JOIN META_DIM_USER_DEPT B " +
                                            "ON A.DEPT_ID=B.DEPT_CODE LEFT JOIN META_DIM_USER_STATION C " +
                                            "ON C.STATION_CODE=A.STATION_ID LEFT JOIN META_DIM_ZONE D " +
                                            "ON D.ZONE_ID=A.ZONE_ID WHERE A.USER_ID<>"+ UserConstant.ADMIN_USERID+" ");
        List<Object> params=new ArrayList<Object>();
        if(condtions != null && condtions.get("userName") != null
           && !condtions.get("userName").toString().equals("")){//姓名
            sql.append("AND USER_NAMECN LIKE ? ESCAPE '/' ");
            params.add(SqlUtils.allLikeBindParam(Convert.toString(condtions.get("userName")).toString()) + "%");
        }
        if(condtions != null && condtions.get("userZone") != null
           && !condtions.get("userZone").toString().equals("-1")){//地域
            sql.append("AND ZONE_ID IN (SELECT ZONE_ID FROM META_DIM_ZONE " +
                       "START WITH ZONE_ID=? CONNECT BY PRIOR ZONE_ID=ZONE_PAR_ID) ");
            params.add(Integer.parseInt(String.valueOf(condtions.get("userZone"))));
        }
        sql.append("ORDER BY A.ZONE_ID,A.USER_SN,A.USER_ID");
        String pageSql=sql.toString();
        //分页包装
        if(page!=null){
            pageSql= SqlUtils.wrapPagingSql(sql.toString(), page);
        }
        return getDataAccess().queryForList(pageSql, params.toArray());
    }

    /**
     * 加载从起始节点到结束节点之间有路径关系节点的所有数据，而不是加载从起始节点到结束节点之间所有的节点数据。
     * 比如可以查询起始地域为0，结束地域为7之间树集关系的所有的数据，而地域7必然为地域0下的地域。
     *@author 张伟
     * @param beginId 起始地域ID。
     * @param endId   结束地域ID，如=0，只查找从指定起始节点下的两层树形数据。
     * @return
     */
	public List<Map<String, Object>> queryZoneByBeginEndPath(int beginId, int endId) {
		/**
		 * String sql = "SELECT A.ZONE_ID,A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_PAR_CODE,A.ZONE_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
				+ " FROM META_DIM_ZONE A"
				+ " LEFT JOIN (SELECT ZONE_PAR_ID, COUNT(1) CNT"
				+ " FROM META_DIM_ZONE"
				+ " GROUP BY ZONE_PAR_ID) C"
				+ " ON A.ZONE_ID = C.ZONE_PAR_ID" + " WHERE 1=1";
		 //根结点
		  if(endId==0){
			   sql+="AND A.ZONE_PAR_ID =?";
			}else{
			   sql+="AND A.ZONE_ID =?";
		    }
		Object[] proParams = {endId};
		return getDataAccess().queryForList(sql, proParams);
		**/
        StringBuffer sql=new StringBuffer("SELECT A.ZONE_ID, A.ZONE_NAME, A.ZONE_PAR_ID, A.ZONE_CODE, A.ZONE_DESC, " +
                "A.DIM_TYPE_ID, A.STATE, A.DIM_LEVEL, DECODE(NVL(C.CNT,0),0,0,1) AS CHILDREN " +
        "FROM META_DIM_ZONE A ");
			//关联子查询，用于查询是否还有子节点
		sql.append("LEFT JOIN (SELECT ZONE_PAR_ID,COUNT(1) CNT FROM META_DIM_ZONE  WHERE DIM_TYPE_ID = "+SystemVariable.getString("userZoneDimTypeId", "4")+" GROUP BY ZONE_PAR_ID) C ");
			//连接条件
			sql.append("ON A.ZONE_ID=C.ZONE_PAR_ID ");
			//下面的SQL用于当endId存在的时候限制查询的层级，如果endId不存在，只查询到begId下一个层级即可。用不到下面这段逻辑。
			if(endId>0){
				sql.append("WHERE A.ZONE_ID IN ");
				sql.append("(SELECT A.ZONE_ID FROM META_DIM_ZONE A  ");
				sql.append("WHERE A.DIM_TYPE_ID = "+SystemVariable.getString("userZoneDimTypeId", "4")+" AND  LEVEL<= ");
				sql.append("(SELECT NVL(MAX(L),99999999999999) FROM (SELECT ZONE_ID,ZONE_PAR_ID, LEVEL L " +
				"FROM META_DIM_ZONE CONNECT BY PRIOR ZONE_PAR_ID=ZONE_ID START WITH ZONE_ID="+endId+") A " +
				"WHERE A."+(beginId==Constant.DEFAULT_ROOT_PARENT?"ZONE_PAR_ID=":"ZONE_ID=")+beginId+ " )"+
			" CONNECT BY  PRIOR A.ZONE_ID=A.ZONE_PAR_ID START WITH "+
			(beginId==Constant.DEFAULT_ROOT_PARENT?"ZONE_PAR_ID=":"ZONE_ID=")+beginId+") ");
				if(beginId==Constant.DEFAULT_ROOT_PARENT){
			//sql.append("OR A.ZONE_PAR_ID ="+beginId);
			}
			
			}else{//如果不存在endId，指定查找其子节点数据
			//sql.append("WHERE A.ZONE_PAR_ID="+beginId+" OR A.ZONE_ID="+beginId);
			sql.append("WHERE A.ZONE_PAR_ID="+beginId+" AND A.DIM_TYPE_ID="+SystemVariable.getString("userZoneDimTypeId", "4")+"");
			}
			return getDataAccess().queryForList(sql.toString());		
		
	}
    /**
                     营业厅
     */
	public List<Map<String, Object>> queryZoneByBeginEndPathToBuss(int beginId, int endId) {
        StringBuffer sql=new StringBuffer("SELECT A.ZONE_ID, A.ZONE_NAME, A.ZONE_PAR_ID, A.ZONE_CODE, A.ZONE_DESC, " +
                "A.DIM_TYPE_ID, A.STATE, A.DIM_LEVEL, DECODE(NVL(C.CNT,0),0,0,1) AS CHILDREN " +
                "FROM Meta_Dim_Zone_Branch A ");
			//关联子查询，用于查询是否还有子节点
			sql.append("LEFT JOIN (SELECT ZONE_PAR_ID,COUNT(1) CNT FROM Meta_Dim_Zone_Branch    GROUP BY ZONE_PAR_ID) C ");
			//sql.append("LEFT JOIN (SELECT ZONE_PAR_ID,COUNT(1) CNT FROM Meta_Dim_Zone_Branch  WHERE DIM_TYPE_ID = "+SystemVariable.getString("userZoneDimTypeId", "5")+" GROUP BY ZONE_PAR_ID) C ");

			//连接条件
			sql.append("ON A.ZONE_ID=C.ZONE_PAR_ID ");
			//下面的SQL用于当endId存在的时候限制查询的层级，如果endId不存在，只查询到begId下一个层级即可。用不到下面这段逻辑。
			if(endId>0){
			sql.append("WHERE A.ZONE_ID IN ");
			sql.append("(SELECT A.ZONE_ID FROM Meta_Dim_Zone_Branch A  ");
			//sql.append("WHERE A.DIM_TYPE_ID = "+SystemVariable.getString("userZoneDimTypeId", "5")+" AND  LEVEL<= ");
			sql.append("WHERE   LEVEL<= ");
			sql.append("(SELECT NVL(MAX(L),99999999999999) FROM (SELECT ZONE_ID,ZONE_PAR_ID, LEVEL L " +
			"FROM Meta_Dim_Zone_Branch CONNECT BY PRIOR ZONE_PAR_ID=ZONE_ID START WITH ZONE_ID="+endId+") A " +
			"WHERE A."+(beginId==Constant.DEFAULT_ROOT_PARENT?"ZONE_PAR_ID=":"ZONE_ID=")+beginId+ " )"+
			" CONNECT BY  PRIOR A.ZONE_ID=A.ZONE_PAR_ID START WITH "+
			(beginId==Constant.DEFAULT_ROOT_PARENT?"ZONE_PAR_ID=":"ZONE_ID=")+beginId+") ");
			if(beginId==Constant.DEFAULT_ROOT_PARENT){
			//sql.append("OR A.ZONE_PAR_ID ="+beginId);
			}
			
			}else{//如果不存在endId，指定查找其子节点数据
			//sql.append("WHERE A.ZONE_PAR_ID="+beginId+" OR A.ZONE_ID="+beginId);
			sql.append("WHERE A.ZONE_PAR_ID="+beginId+" AND A.DIM_TYPE_ID="+SystemVariable.getString("userZoneDimTypeId", "5")+"");
			}
			return getDataAccess().queryForList(sql.toString());		
		
	}
	public List<Map<String, Object>> queryMenuByPath(int beginId, int endId) {
		   String sql = "SELECT A.MENU_ID,A.MENU_NAME,A.PARENT_ID,A.IS_SHOW,A.ORDER_ID,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
				+ " FROM meta_mag_menu_apply A"
				+ " LEFT JOIN (SELECT PARENT_ID, COUNT(1) CNT"
				+ " FROM meta_mag_menu_apply"
				+ " GROUP BY PARENT_ID) C"
				+ " ON A.MENU_ID = C.PARENT_ID" 
				+ " WHERE A.MENU_ID =? or A.PARENT_ID = ?";
				Object[] proParams ={endId,endId};
				return getDataAccess().queryForList(sql, proParams);
		}
    /**
     * 查询出所有begionId 下所有子地域
     * @param beginId
     * @return
     * @author 王春生
     */
    public List<Map<String,Object>> queryAllZoneForBegin(int beginId){
        String sql = "SELECT A.ZONE_ID, A.ZONE_NAME, A.ZONE_PAR_ID, A.ZONE_CODE, A.ZONE_DESC, A.DIM_TYPE_ID, A.STATE," +
                "A.DIM_LEVEL, DECODE(NVL(C.CNT,0),0,0,1) AS CHILDREN " +
                "FROM META_DIM_ZONE A LEFT JOIN (SELECT ZONE_PAR_ID,COUNT(1) CNT FROM META_DIM_ZONE GROUP BY ZONE_PAR_ID) C ON A.ZONE_ID=C.ZONE_PAR_ID " +
                "WHERE A.ZONE_ID IN(SELECT ZONE_ID FROM META_DIM_ZONE CONNECT BY PRIOR ZONE_ID=ZONE_PAR_ID START WITH ZONE_PAR_ID=?)  ORDER BY A.ZONE_PAR_ID ASC";
        return getDataAccess().queryForList(sql,beginId);
    }

    /**
     * 根据zoneId查询相关的地域信息。
     * @param zoneId
     * @return
     */
    public Map<String,Object>  queryZoneInfo(int zoneId){
        String sql= " SELECT A.ZONE_ID,A.ZONE_PAR_ID,A.ZONE_NAME,A.ZONE_DESC,A.DIM_TYPE_ID, A.DIM_TYPE_ID,A.DIM_LEVEL," +
			        " CASE WHEN A.ZONE_CODE IS NULL OR A.ZONE_CODE = '0000' THEN '0000' ELSE A.ZONE_CODE END ZONE_CODE," +
			        " CASE WHEN B.ZONE_CODE IS NULL OR B.ZONE_CODE = '0000' THEN '0' ELSE A.ZONE_CODE END AREA_ID" +
                    " FROM META_DIM_ZONE_BRANCH A, META_DIM_ZONE_BRANCH B WHERE A.ZONE_ID = ? " +
                    " AND A.ZONE_PAR_ID = B.ZONE_ID(+)";
        //AND A.DIM_TYPE_ID=4
        return getDataAccess().queryForMap(sql,zoneId);
    }
    
    /**
     *  add yanhd 
     * @param queryData
     * @return
     */
    public List<Map<String, Object>> loadZoneTree(Map<String, Object> queryData) {
		String select = "SELECT A.ZONE_ID,A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_DESC,A.STATE,A.DIM_LEVEL" +
				       " FROM META_DIM_ZONE A ";
		List proParams = new ArrayList();
		if (queryData != null) {
			Object zoneName = queryData.get("zoneName");
			if (zoneName != null && !zoneName.toString().trim().equals("")) {
                select += ",(select ZONE_ID FROM META_DIM_ZONE start with ZONE_NAME LIKE ? ESCAPE '/' " +
                		 " connect by  prior ZONE_ID=ZONE_PAR_ID) B WHERE   A.ZONE_ID=B.ZONE_ID";
                proParams.add(SqlUtils.allLikeBindParam(zoneName.toString()));			
			}
		} 
		select += " ORDER BY dim_level, ORDER_ID ASC";
		return getDataAccess().queryForList(select, proParams.toArray());
	}

	/** yanhd start **/
	
    /**
	  * 方法描述：
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-4-16 下午03:55:13
	  */
	public List<Map<String, Object>> queryZoneByPathCode(String beginId, String endId) {
	   String sql = "SELECT A.ZONE_ID,A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_PAR_CODE,A.ZONE_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
			+ " FROM META_DIM_ZONE A"
			+ " LEFT JOIN (SELECT ZONE_PAR_ID, COUNT(1) CNT"
			+ " FROM META_DIM_ZONE"
			+ " GROUP BY ZONE_PAR_ID) C"
			+ " ON A.ZONE_ID = C.ZONE_PAR_ID" 
			+ " WHERE A.ZONE_CODE =?";
			Object[] proParams ={endId};
			return getDataAccess().queryForList(sql, proParams);
	}
	
	public List<Map<String, Object>> queryZoneByIVRPathCode(String beginId, String endId) {
		   String sql = "SELECT A.AREA_CODE ZONE_ID,A.AREA_NAME ZONE_NAME,A.PAR_AREA_CODE ZONE_PAR_ID,A.AREA_CODE ZONE_CODE,A.PAR_AREA_CODE ZONE_PAR_CODE,'' ZONE_DESC,4 DIM_TYPE_ID,1 STATE,A.AREA_LEVEL DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
				+ " FROM META_DIM_USER_ORG_BRANCH A"
				+ " LEFT JOIN (SELECT PAR_AREA_CODE , COUNT(1) CNT"
				+ " FROM META_DIM_USER_ORG_BRANCH"
				+ " GROUP BY PAR_AREA_CODE) C"
				+ " ON A.AREA_CODE = C.PAR_AREA_CODE" 
				+ " WHERE A.AREA_CODE =?";
				Object[] proParams ={endId};
				return getDataAccess().queryForList(sql, proParams);
		}
	
	public List<Map<String, Object>> queryChannelServById(String beginId, String endId) {
		   String sql = "SELECT A.SERV_ID,A.SERV_NAME,A.SERV_PAR_ID,A.SERV_DESC,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
				+ " FROM META_DM.DM_CHANNEL_SERV A"
				+ " LEFT JOIN (SELECT SERV_PAR_ID, COUNT(1) CNT"
				+ " FROM META_DM.DM_CHANNEL_SERV"
				+ " GROUP BY SERV_PAR_ID) C"
				+ " ON A.SERV_ID = C.SERV_PAR_ID" 
				+ " WHERE A.SERV_ID =?";
				Object[] proParams ={endId};
				return getDataAccess().queryForList(sql, proParams);
		}
	public List<Map<String, Object>> queryZoneByPathCodeGrid(String beginId, String endId) {
		   String sql = "SELECT A.ZONE_ID,A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_PAR_CODE,A.ZONE_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
				+ " FROM(select * from meta_user.meta_dim_zone_custgrp where DIM_LEVEL<7)A"
				+ " LEFT JOIN (SELECT ZONE_PAR_ID, COUNT(1) CNT"
				+ " FROM meta_user.meta_dim_zone_custgrp where DIM_LEVEL<7"
				+ " GROUP BY ZONE_PAR_ID) C"
				+ " ON A.ZONE_ID = C.ZONE_PAR_ID" 
				+ " WHERE A.ZONE_CODE =?";
				Object[] proParams ={endId};
				return getDataAccess().queryForList(sql, proParams);
		}
	public List<Map<String, Object>> queryZoneByPath(String beginId, String endId) {
		   String sql = "SELECT A.ZONE_ID,A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_PAR_CODE,A.ZONE_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
				+ " FROM(select * from meta_user.meta_dim_zone_custgrp where DIM_LEVEL<4)A"
				+ " LEFT JOIN (SELECT ZONE_PAR_ID, COUNT(1) CNT"
				+ " FROM meta_user.meta_dim_zone_custgrp where DIM_LEVEL<4"
				+ " GROUP BY ZONE_PAR_ID) C"
				+ " ON A.ZONE_ID = C.ZONE_PAR_ID" 
				+ " WHERE A.ZONE_CODE =?";
				Object[] proParams ={endId};
				return getDataAccess().queryForList(sql, proParams);
		}
	
	
	//营服中心区域树
	
	public List<Map<String, Object>> queryProCenterCode(String parentCode) {
	      String select = "SELECT A.ZONE_ID,A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_PAR_CODE,A.ZONE_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
					+ " FROM(select * from meta_user.meta_dim_zone_branch where DIM_LEVEL<6)A"
					+ " LEFT JOIN (SELECT ZONE_PAR_ID, COUNT(1) CNT"
					+ " FROM meta_user.meta_dim_zone_branch where DIM_LEVEL<6"
					+ " GROUP BY ZONE_PAR_ID) C"
					+ " ON A.ZONE_ID = C.ZONE_PAR_ID"
					+ " WHERE A.ZONE_PAR_CODE = ?"
					+ " ORDER BY dim_level, ORDER_ID ASC";
			Object[] proParams = {parentCode};
			return getDataAccess().queryForList(select, proParams);
		}
	
	public List<Map<String, Object>> queryIVRCenterCode(String parentCode) {
	      String select = " SELECT A.AREA_CODE ZONE_ID,"
	      +"        A.AREA_NAME ZONE_NAME,                                                    "
	      +"        A.PAR_AREA_CODE ZONE_PAR_ID,                                              "
	      +"        A.AREA_CODE ZONE_CODE,                                                    "
	      +"        A.PAR_AREA_CODE ZONE_PAR_CODE,                                            "
	      +"        '' ZONE_DESC,                                                             "
	      +"		4 DIM_TYPE_ID,															  "
	      +"        '1' STATE,                                                                "
	      +"       A.AREA_LEVEL DIM_LEVEL,                                                    "
	      +"        DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN                                "
	      +"   FROM (select * from meta_user.META_DIM_USER_ORG_BRANCH where AREA_LEVEL < 6) A "
	      +"   LEFT JOIN (SELECT PAR_AREA_CODE, COUNT(1) CNT                                  "
	      +"                FROM meta_user.META_DIM_USER_ORG_BRANCH                           "
	      +"               where AREA_LEVEL < 6                                               "
	      +"               GROUP BY PAR_AREA_CODE) C                                          "
	      +"     ON A.AREA_CODE = C.PAR_AREA_CODE                                             "
	      +"  WHERE A.PAR_AREA_CODE = ?                                                 "
	      +"  ORDER BY AREA_LEVEL ASC                                                        ";
			Object[] proParams = {parentCode};
			return getDataAccess().queryForList(select, proParams);
		}
	
	
	public List<Map<String, Object>> queryZoneByPathCenterCode(String beginId, String endId) {
		   String sql = "SELECT A.ZONE_ID,A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_PAR_CODE,A.ZONE_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
				+ " FROM(select * from meta_user.meta_dim_zone_branch where DIM_LEVEL<6)A"
				+ " LEFT JOIN (SELECT ZONE_PAR_ID, COUNT(1) CNT"
				+ " FROM meta_user.meta_dim_zone_branch where DIM_LEVEL<6"
				+ " GROUP BY ZONE_PAR_ID) C"
				+ " ON A.ZONE_ID = C.ZONE_PAR_ID" 
				+ " WHERE A.ZONE_CODE =?";
				Object[] proParams ={endId};
				return getDataAccess().queryForList(sql, proParams);
		}
	
	public List<Map<String, Object>> queryZoneByIVRPathCenterCode(String beginId, String endId) {
		   String sql = "select A.ZONE_NAME,A.ZONE_CODE,A.ZONE_PAR_CODE,'' ZONE_DESC," +
		   		" 1 STATE,A.DIM_LEVEL ,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN "
		   		+"from (select area_code ZONE_CODE,par_area_code ZONE_PAR_CODE,area_name ZONE_NAME,area_level DIM_LEVEL from META_DIM_USER_ORG_BRANCH where area_level < 5) A "
		   		+" left join (select par_area_code ZONE_PAR_CODE,count(1) cnt from META_DIM_USER_ORG_BRANCH  where area_level < 5  GROUP BY par_area_code) c "
		   		+"on A.ZONE_CODE = C.ZONE_PAR_CODE "
		   		+"where A.ZONE_CODE=? order by dim_level desc";
				Object[] proParams ={endId};
				return getDataAccess().queryForList(sql, proParams);
		}
	
	
	/**
	  * 方法描述：
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: yanhaidong
	  * @version: 2013-4-16 下午03:55:22
	  */
	public List<Map<String, Object>> querySubZoneCode(String parentCode) {
      String select = "SELECT A.ZONE_ID, A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_PAR_CODE,A.ZONE_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
				+ " FROM META_DIM_ZONE A"
				+ " LEFT JOIN (SELECT ZONE_PAR_ID, COUNT(1) CNT"
				+ " FROM META_DIM_ZONE"
				+ " GROUP BY ZONE_PAR_ID) C"
				+ " ON A.ZONE_ID = C.ZONE_PAR_ID"
				+ " WHERE A.ZONE_PAR_CODE = ?"
				+ " ORDER BY dim_level, ORDER_ID ASC";
		Object[] proParams = {parentCode};
		return getDataAccess().queryForList(select, proParams);
	}
	
	public List<Map<String, Object>> queryIVRZoneCode(String parentCode) {
	      String select = "SELECT A.AREA_CODE ZONE_ID,A.AREA_NAME ZONE_NAME,A.PAR_AREA_CODE ZONE_PAR_ID,A.AREA_CODE ZONE_CODE,A.PAR_AREA_CODE ZONE_PAR_CODE ,'' ZONE_DESC,AREA_LEVEL DIM_LEVEL ,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN "
          +" FROM META_DIM_USER_ORG_BRANCH A "
          +" LEFT JOIN (SELECT PAR_AREA_CODE , COUNT(1) CNT "
          +" FROM META_DIM_USER_ORG_BRANCH "
          +" GROUP BY PAR_AREA_CODE) C "
          +" ON A.AREA_CODE = C.PAR_AREA_CODE "
          +" WHERE A.PAR_AREA_CODE = ? "
          +" ORDER BY AREA_LEVEL ASC";
			Object[] proParams = {parentCode};
			return getDataAccess().queryForList(select, proParams);
		}
	
	
	
	
	public List<Map<String, Object>> querySubChannelServId(String parentCode) {
	      String select = "SELECT A.SERV_ID,A.SERV_NAME,A.SERV_PAR_ID,A.SERV_DESC,A.STATE,A.DIM_LEVEL,A.ORDER_ID,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
					+ " FROM META_DM.DM_CHANNEL_SERV A"
					+ " LEFT JOIN (SELECT SERV_PAR_ID, COUNT(1) CNT"
					+ " FROM META_DM.DM_CHANNEL_SERV"
					+ " GROUP BY SERV_PAR_ID) C"
					+ " ON A.SERV_ID = C.SERV_PAR_ID"
					+ " WHERE A.SERV_PAR_ID = ? "//and (serv_id like '10%' or serv_id like '30%')
					+ " ORDER BY dim_level, ORDER_ID ASC";
			Object[] proParams = {parentCode};
			return getDataAccess().queryForList(select, proParams);
		}
	
	/**
	  * 方法描述：满意度评测东莞个性化
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: qinsh
	  * @version: 2013-11-14
	  */
	public List<Map<String, Object>> querySubZoneCode1(String parentCode) {
			String select=new String();
				select = "SELECT A.ZONE_ID,A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_PAR_CODE,A.ZONE_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL, DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
					+" FROM META_DIM_ZONE_branch A  LEFT JOIN (SELECT ZONE_PAR_ID, COUNT(1) CNT  FROM META_DIM_ZONE  where DIM_LEVEL < 4   GROUP BY ZONE_PAR_ID) C ON A.ZONE_ID = C.ZONE_PAR_ID "
					+" WHERE A.ZONE_PAR_CODE = ? ORDER BY dim_level, ORDER_ID ASC";
			Object[] proParams = {parentCode};
			return getDataAccess().queryForList(select, proParams);
	}
	public List<Map<String, Object>> querySubZoneCodeGrid(String parentCode) {
	      String select = "SELECT A.ZONE_ID,A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_PAR_CODE,A.ZONE_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
					+ " FROM(select * from meta_user.meta_dim_zone_custgrp where DIM_LEVEL<7)A"
					+ " LEFT JOIN (SELECT ZONE_PAR_ID, COUNT(1) CNT"
					+ " FROM meta_user.meta_dim_zone_custgrp where DIM_LEVEL<7"
					+ " GROUP BY ZONE_PAR_ID) C"
					+ " ON A.ZONE_ID = C.ZONE_PAR_ID"
					+ " WHERE A.ZONE_PAR_CODE = ?"
					+ " ORDER BY dim_level, ORDER_ID ASC";
			Object[] proParams = {parentCode};
			return getDataAccess().queryForList(select, proParams);
		}
	public List<Map<String, Object>> querySubZoneCodeGrid_Dg(String parentCode) {
			String select=new String();
				select = "SELECT A.ZONE_ID,A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_PAR_CODE,A.ZONE_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
						+ " FROM(select * from meta_user.meta_dim_zone_custgrp where DIM_LEVEL<7)A"
						+ " LEFT JOIN (SELECT ZONE_PAR_ID, COUNT(1) CNT"
						+ " FROM meta_user.meta_dim_zone_custgrp where DIM_LEVEL<4"
						+ " GROUP BY ZONE_PAR_ID) C"
						+ " ON A.ZONE_ID = C.ZONE_PAR_ID"
						+ " WHERE A.ZONE_PAR_CODE = ?"
						+ " ORDER BY dim_level, ORDER_ID ASC";
	     
			Object[] proParams = {parentCode};
			return getDataAccess().queryForList(select, proParams);
		}
	public List<Map<String, Object>> querySubZone1(String parentCode) {
	      String select = "SELECT A.ZONE_ID,A.ZONE_NAME,A.ZONE_PAR_ID,A.ZONE_CODE,A.ZONE_PAR_CODE,A.ZONE_DESC,A.DIM_TYPE_ID,A.STATE,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
					+ " FROM(select * from meta_user.meta_dim_zone_custgrp where DIM_LEVEL<4)A"
					+ " LEFT JOIN (SELECT ZONE_PAR_ID, COUNT(1) CNT"
					+ " FROM meta_user.meta_dim_zone_custgrp where DIM_LEVEL<4"
					+ " GROUP BY ZONE_PAR_ID) C"
					+ " ON A.ZONE_ID = C.ZONE_PAR_ID"
					+ " WHERE A.ZONE_PAR_CODE = ?"
					+ " ORDER BY dim_level, ORDER_ID ASC";
			Object[] proParams = {parentCode};
			return getDataAccess().queryForList(select, proParams);
		}
}
