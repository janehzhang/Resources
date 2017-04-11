package tydic.meta.module.dim.audit;

import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DataAccess;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.tbl.TblConstant;
import tydic.meta.sys.code.CodeManager;

/**
 * 
 * @author 李国民
 * @description 审核编码映射DAO <br>
 * @date 2012-02-08
 */
public class MappingAuditDAO extends MetaBaseDAO{
	/**
	 * 通过批次号和维度表id查询未审核的编码映射变更数据
	 * @param batchId 批次号
	 * @param dimTableId 维度表id
	 * @return 未审核的编码映射变更数据
	 */
    public List<Map<String, Object>> queryMappingAudit(String batchId, int dimTableId, Page page){
    	String sql = "SELECT T.ITEM_CODE AS SHOW_ITEM_CODE, T.ITEM_NAME AS SHOW_ITEM_NAME, T.*" +
    			" FROM META_DIM_TAB_MOD_HIS T WHERE T.AUDIT_FLAG=0 AND T.BATCH_ID=? AND T.DIM_TABLE_ID=? ORDER BY T.HIS_ID";
    	DataAccess dataAccess = getDataAccess();
        //分页包装
        if(page!=null){
        	sql= SqlUtils.wrapPagingSql(sql, page);
        }
        List<Map<String,Object>> rs= dataAccess.queryForList(sql,batchId,dimTableId);
        //加入码表信息
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("FLAG", CodeManager.getName(TblConstant.DIM_APPLY_TYPE,MapUtils.getString(map,"MOD_FLAG")));
            }
        }
        return rs;
    }
    
    /**
     * 通过维度表id、源系统id、系统编码查询映射表中对应的维度编码
     * @param paramMap 
     * @return 维度编码
     */
    public Map<String, Object> queryMappItemCode(Object[] paramMap){
    	String sql = "SELECT T.ITEM_CODE,T.ITEM_NAME FROM META_DIM_MAPP T " +
    			"WHERE T.DIM_TABLE_ID=? and T.SYS_ID=? and T.SRC_CODE=?";
    	DataAccess dataAccess = getDataAccess();
    	return dataAccess.queryForMap(sql, paramMap);
    }

    /**
     * 根据维度表id查询该维度表末级是否显示
     * @param dimTableId 维度表id
     * @return 维度表信息
     */
    public Map<String, Object> queryLastLevelFlag(int dimTableId){
    	String sql = "SELECT T.LAST_LEVEL_FLAG FROM META_DIM_TABLES T WHERE T.DIM_TABLE_ID=?";
    	DataAccess dataAccess = getDataAccess();
    	return dataAccess.queryForMap(sql, dimTableId);
    }
    
    /**
     * 当末级为不显示情况下时，查询映射关系中对应维度编码的上级编码
     * @param paramMap
     * @return
     */
    public Map<String, Object> queryItemCode(String tableName, String tableDimPrefix, String tableOwner, String itemCode){
    	String sql = "SELECT T."+tableDimPrefix+"_CODE AS ITEM_CODE, T."+tableDimPrefix+"_NAME AS ITEM_NAME " +
    			" FROM "+tableOwner+"."+tableName+" T WHERE T."+tableDimPrefix+"_ID = (SELECT A."+tableDimPrefix+"_PAR_ID " +
    				" FROM "+tableOwner+"."+tableName+" A WHERE A."+tableDimPrefix+"_CODE = ?)";
    	DataAccess dataAccess = getDataAccess(TblConstant.META_DIM_DATA_SOURCE_ID);
    	return dataAccess.queryForMap(sql, itemCode);
    }
    
    /**
     * 根据源系统id和源系统的code与当前维度表的id与itemCode查找映射表中是否存在
     * @author 王晶
     * @param sysId int 源系统的id
     * @param srcCode 源系统的code
     * @param dimTableId 维度表的tableId
     * @param itemCode 维度的itemCode
     * */
    public int queryMappingDataByCondition(int sysId,String srcCode,int dimTableId,String itemCode){
    	String sql ="SELECT COUNT(SYS_ID) FROM META_DIM_MAPP T WHERE T.ITEM_CODE=? AND T.SRC_CODE=? AND T.SYS_ID=? AND T.DIM_TABLE_ID=?";
    	DataAccess dataAccess = getDataAccess();
		return dataAccess.queryForInt(sql, itemCode,srcCode,sysId,dimTableId);
    }
    
    /**
     * 插入映射表中
     * @author 王晶
     * @param sysId int 源系统的id
     * @param sysCode 源系统的code
     * @param dimTableId 维度表的tableId
     * @param itemCode 维度的itemCode
     * @param itemName 维度节点的名称
     * @param srcName 维度节点源名称
     * */
     public int insertMappingDataByCondition(String itemCode,String srcCode,int sysId,String itemName,String srcName,int dimTableId){
	     String  sql ="INSERT INTO META_DIM_MAPP (ITEM_CODE,SRC_CODE,SYS_ID,ITEM_NAME,SRC_NAME,DIM_TABLE_ID)VALUES(?,?,?,?,?,?)";
	     return getDataAccess().execUpdate(sql, itemCode,srcCode,sysId,itemName,srcName,dimTableId);
     }
     
     /**
      * 删除映射表
      * @author 王晶
      * @param sysId int 源系统的id
      * @param sysCode 源系统的code
      * @param dimTableId 维度表的tableId
      * @param itemCode 维度的itemCode
      * */
      public int deleteMappingDataByCondition(String itemCode,String srcCode,int sysId,int dimTableId){
	      String  sql ="DELETE FROM META_DIM_MAPP T WHERE T.ITEM_CODE=? AND T.SRC_CODE=? AND T.SYS_ID=? AND T.DIM_TABLE_ID=?";
	      return getDataAccess().execUpdate(sql, itemCode,srcCode,sysId,dimTableId);
      }
      
      /**
       * 修改映射表映射值（ 末级显示时操作）
       * @param itemCode 维度的code
       * @param srcCode  源系统的code
       * @param sysId 源系统的id
       * @param dimTableId 维度表的id
       * @return
       */
      public int updateMappingDataByCondition(String itemCode,String itemName,String srcCode,int sysId,int dimTableId){
    	  String  sql ="update META_DIM_MAPP T SET T.ITEM_CODE=?, T.ITEM_NAME=? WHERE T.SRC_CODE=? AND T.SYS_ID=? AND T.DIM_TABLE_ID=?";
	      return getDataAccess().execUpdate(sql, itemCode,itemName,srcCode,sysId,dimTableId);
      }
      
      /**
       * 根据itemCode删除映射表数据
       * @param  itemCodeArr itemCode的集合
       * */
      public int deleteMappingDataByCondition(List<String>itemCodeArr,int dimTableId){
    	  if(dimTableId!=0){
    	    String  sql ="DELETE FROM META_DIM_MAPP T WHERE T.ITEM_CODE IN "+SqlUtils.inParamDeal(itemCodeArr)+" AND T.DIM_TABLE_ID=?";
            return getDataAccess().execUpdate(sql,dimTableId);
          }
		return -1;
      }
}  
