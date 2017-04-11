package tydic.reports.query;

import tydic.frame.BaseDAO;
import java.util.List;
import java.util.Map;

/**
 * @modify  qx
 * @modifyDate  2013-4-15
 */
public class ChannelTypeDAO extends BaseDAO {
    /**
     * 查询子业务类型
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> querySubChannelType(String parentCode){
        String select = "SELECT A.CHANNEL_TYPE_CODE,A.CHANNEL_TYPE_PAR_CODE,"
        +" A.CHANNEL_TYPE_NAME,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
        +" FROM meta_dm.d_v_channel_type A"
        +" LEFT JOIN (SELECT CHANNEL_TYPE_PAR_CODE, COUNT(1) CNT"
        +" FROM meta_dm.d_v_channel_type"
        +" GROUP BY CHANNEL_TYPE_PAR_CODE) C"
        +" ON A.CHANNEL_TYPE_CODE = C.CHANNEL_TYPE_PAR_CODE"
        +" WHERE A.CHANNEL_TYPE_PAR_CODE = ?"
        +" ORDER BY dim_level, ORDER_ID ASC";
        	
	    Object[] proParams = {parentCode};
        return getDataAccess().queryForList(select, proParams);
    }
    /* 全渠道月报（新）    类型树*/
    public List<Map<String,Object>> querySubChannelType_new(String parentCode){
        String select = "SELECT A.CHANNEL_TYPE_CODE,A.CHANNEL_TYPE_PAR_CODE,"
        +" A.CHANNEL_TYPE_NAME,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
        +" FROM meta_dm.d_v_channel_type_new A"
        +" LEFT JOIN (SELECT CHANNEL_TYPE_PAR_CODE, COUNT(1) CNT"
        +" FROM meta_dm.d_v_channel_type_new"
        +" GROUP BY CHANNEL_TYPE_PAR_CODE) C"
        +" ON A.CHANNEL_TYPE_CODE = C.CHANNEL_TYPE_PAR_CODE"
        +" WHERE A.View_State=1 and A.CHANNEL_TYPE_PAR_CODE = ?"
        +" ORDER BY dim_level, ORDER_ID ASC";
        	
	    Object[] proParams = {parentCode};
        return getDataAccess().queryForList(select, proParams);
    }
   
    /**
     * 加载从起始节点到结束节点之间有路径关系节点的所有数据，而不是加载从起始节点到结束节点之间所有的节点数据。
     *@author qx
     * @param beginId 起始ID
     * @param endId   结束ID
     * @return
     */
    public List<Map<String,Object>> queryChannelTypeByBeginEndPath(String beginId, String endId){	
 	   String sql = "SELECT A.CHANNEL_TYPE_CODE,A.CHANNEL_TYPE_PAR_CODE,"
 	        +" A.CHANNEL_TYPE_NAME,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
 	        +" FROM meta_dm.d_v_channel_type_new A"
 	        +" LEFT JOIN (SELECT CHANNEL_TYPE_PAR_CODE, COUNT(1) CNT"
 	        +" FROM meta_dm.d_v_channel_type_new"
 	        +" GROUP BY CHANNEL_TYPE_PAR_CODE) C"
 	        +" ON A.CHANNEL_TYPE_CODE = C.CHANNEL_TYPE_PAR_CODE"
 		   + " WHERE  A.CHANNEL_TYPE_CODE = ? ";
	    Object[] proParams ={endId};
	   return getDataAccess().queryForList(sql, proParams);
    }
    /**
     * 查询子业务类型--去掉部分叶子节点
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> querySubChannelTypePart(String parentCode){
        String select = "SELECT A.CHANNEL_TYPE_CODE,A.CHANNEL_TYPE_PAR_CODE,"
        +" A.CHANNEL_TYPE_NAME,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
        +" FROM (select * from meta_dm.d_v_channel_type where CHANNEL_TYPE_CODE NOT IN('10005','10006','10007')) A"
        +" LEFT JOIN (SELECT CHANNEL_TYPE_PAR_CODE, COUNT(1) CNT from meta_dm.d_v_channel_type where " +
        		"CHANNEL_TYPE_CODE NOT IN ('10005', '10006', '10007')"
        +" GROUP BY CHANNEL_TYPE_PAR_CODE) C"
        +" ON A.CHANNEL_TYPE_CODE = C.CHANNEL_TYPE_PAR_CODE"
        +" WHERE A.CHANNEL_TYPE_PAR_CODE = ? ";
        	
	    Object[] proParams = {parentCode};
        return getDataAccess().queryForList(select, proParams);
    }
    
   /* 全渠道周报（新）    类型树*/
    public List<Map<String,Object>> querySubChannelTypePart_new(String parentCode){
        String select = "SELECT A.CHANNEL_TYPE_CODE,A.CHANNEL_TYPE_PAR_CODE,"
        +" A.CHANNEL_TYPE_NAME,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
        +" FROM (select * from meta_dm.d_v_channel_type_new where CHANNEL_TYPE_CODE NOT IN('10005','10006','10007')) A"
        +" LEFT JOIN (SELECT CHANNEL_TYPE_PAR_CODE, COUNT(1) CNT from meta_dm.d_v_channel_type_new where " +
        		"CHANNEL_TYPE_CODE NOT IN ('10005', '10006', '10007')"
        +" GROUP BY CHANNEL_TYPE_PAR_CODE) C"
        +" ON A.CHANNEL_TYPE_CODE = C.CHANNEL_TYPE_PAR_CODE"
        +" WHERE A.View_State=1 and A.CHANNEL_TYPE_PAR_CODE = ? ORDER BY ORDER_ID";
        	
	    Object[] proParams = {parentCode};
        return getDataAccess().queryForList(select, proParams);
    }
    public List<Map<String,Object>> querySubChannelTypeZone(String parentCode){
        String select = "SELECT A.CHANNEL_TYPE_CODE,A.CHANNEL_TYPE_PAR_CODE,"
        +" A.CHANNEL_TYPE_NAME,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
        +" FROM (select * from meta_dm.d_v_channel_type_new where CHANNEL_TYPE_CODE NOT IN('10005','10006','10007')) A"
        +" LEFT JOIN (SELECT CHANNEL_TYPE_PAR_CODE, COUNT(1) CNT from meta_dm.d_v_channel_type_new where " +
        		"CHANNEL_TYPE_CODE NOT IN ('10005', '10006', '10007')"
        +" GROUP BY CHANNEL_TYPE_PAR_CODE) C"
        +" ON A.CHANNEL_TYPE_CODE = C.CHANNEL_TYPE_PAR_CODE"
        +" WHERE A.CHANNEL_TYPE_PAR_CODE = ? ORDER BY ORDER_ID";
        	
	    Object[] proParams = {parentCode};
        return getDataAccess().queryForList(select, proParams);
    } 
    public List<Map<String,Object>> querySubChannel(String parentCode){
        String select = "SELECT A.CHANNEL_TYPE_CODE,A.CHANNEL_TYPE_PAR_CODE,"
        +" A.CHANNEL_TYPE_NAME,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
        +" FROM (select * from meta_dm.d_v_channel_type_c where CHANNEL_TYPE_CODE NOT IN('10005','10006','10007')) A"
        +" LEFT JOIN (SELECT CHANNEL_TYPE_PAR_CODE, COUNT(1) CNT from meta_dm.d_v_channel_type_c where " +
        		"CHANNEL_TYPE_CODE NOT IN ('10005', '10006', '10007')"
        +" GROUP BY CHANNEL_TYPE_PAR_CODE) C"
        +" ON A.CHANNEL_TYPE_CODE = C.CHANNEL_TYPE_PAR_CODE"
        +" WHERE A.CHANNEL_TYPE_PAR_CODE = ? ORDER BY ORDER_ID";
        	
	    Object[] proParams = {parentCode};
        return getDataAccess().queryForList(select, proParams);
    }
    /**
     * 加载从起始节点到结束节点之间有路径关系节点的所有数据，而不是加载从起始节点到结束节点之间所有的节点数据。--去掉部分叶子节点
     *@author qx
     * @param beginId 起始ID
     * @param endId   结束ID
     * @return
     */
    public List<Map<String,Object>> queryChannelTypeByBeginEndPathPart(String beginId, String endId){	
 	   String sql = "SELECT A.CHANNEL_TYPE_CODE,A.CHANNEL_TYPE_PAR_CODE,"
 	        +" A.CHANNEL_TYPE_NAME,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
 	        +" FROM (select * from meta_dm.d_v_channel_type_new where CHANNEL_TYPE_CODE NOT IN('10005','10006','10007')) A"
 	        +" LEFT JOIN (SELECT CHANNEL_TYPE_PAR_CODE, COUNT(1) CNT from meta_dm.d_v_channel_type_new where " +
        		"CHANNEL_TYPE_CODE NOT IN ('10005', '10006', '10007')"
 	        +" GROUP BY CHANNEL_TYPE_PAR_CODE) C"
 	        +" ON A.CHANNEL_TYPE_CODE = C.CHANNEL_TYPE_PAR_CODE"
 		   + " WHERE A.CHANNEL_TYPE_CODE = ? ";
	    Object[] proParams ={endId};
	   return getDataAccess().queryForList(sql, proParams);
    }
    public List<Map<String,Object>> queryChannelType(String beginId, String endId){	
  	   String sql = "SELECT A.CHANNEL_TYPE_CODE,A.CHANNEL_TYPE_PAR_CODE,"
  	        +" A.CHANNEL_TYPE_NAME,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
  	        +" FROM (select * from meta_dm.d_v_channel_type_c where CHANNEL_TYPE_CODE NOT IN('10005','10006','10007')) A"
  	        +" LEFT JOIN (SELECT CHANNEL_TYPE_PAR_CODE, COUNT(1) CNT from meta_dm.d_v_channel_type_c where " +
         		"CHANNEL_TYPE_CODE NOT IN ('10005', '10006', '10007')"
  	        +" GROUP BY CHANNEL_TYPE_PAR_CODE) C"
  	        +" ON A.CHANNEL_TYPE_CODE = C.CHANNEL_TYPE_PAR_CODE"
  		   + " WHERE A.CHANNEL_TYPE_CODE = ? ";
 	    Object[] proParams ={endId};
 	   return getDataAccess().queryForList(sql, proParams);
     }
    
     /***
      *  渠道类型 单独 “其他”的引用查询 子查询的时候调用 
      * @param parentCode
      * @return
      */
    public List<Map<String,Object>> querySubChannelTypePartOther_new(String parentCode){
        String select = "SELECT A.CHANNEL_TYPE_CODE,A.CHANNEL_TYPE_PAR_CODE,"
        +" A.CHANNEL_TYPE_NAME,A.DIM_LEVEL,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN"
        +" FROM (select * from meta_dm.d_v_channel_type_new where CHANNEL_TYPE_CODE NOT IN('10005','10006','10007')) A"
        +" LEFT JOIN (SELECT CHANNEL_TYPE_PAR_CODE, COUNT(1) CNT from meta_dm.d_v_channel_type_new where " +
        		"CHANNEL_TYPE_CODE NOT IN ('10005', '10006', '10007')"
        +" GROUP BY CHANNEL_TYPE_PAR_CODE) C"
        +" ON A.CHANNEL_TYPE_CODE = C.CHANNEL_TYPE_PAR_CODE"
        +" WHERE A.View_State=2 and A.CHANNEL_TYPE_PAR_CODE = ? ORDER BY ORDER_ID";
        	
	    Object[] proParams = {parentCode};
        return getDataAccess().queryForList(select, proParams);
    }
    /***
     * 查询 产品类型展现
     * @param 
     * @return
     */
    public List<Map<String,Object>> terminalPath(){	
  	   String sql = "select  t.KEY_ID,t.PROD_TYPE1_NAME from tbas_dm.D_V_PROD_TYPE t where t.PROD_TYPE1_ID in ('10', '20', '30', '40', '-999') order by t.PROD_TYPE1_ORDER ";
 	     
 	   return getDataAccess().queryForList(sql);
     }
    
    /***
     * 查询 服务类型展现
     * @param 
     * @return
     */
    public List<Map<String,Object>> servicePath(){	
   	   String sql = "select  t.serv_id,t.serv_name from META_DM.DM_CHANNEL_SERV t where t.dim_level=1 group by t.serv_id,t.serv_name";
  	     
  	   return getDataAccess().queryForList(sql);
      }
    
    
    
    
    
    
}
