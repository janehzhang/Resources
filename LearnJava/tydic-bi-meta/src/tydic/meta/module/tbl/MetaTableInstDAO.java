package tydic.meta.module.tbl;


import java.util.List;
import java.util.Map;

import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

/**
 *
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 刘斌
 * @date 2011-11-04
 * @description 表实例：META_TABLE_INST 操作DAO
 *
 */
public class MetaTableInstDAO extends MetaBaseDAO {
    /**
     * 根据表ID,表版本查询对应表实例信息
     * @param tableId
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryTableInstanceByTableIdAnVersion(int tableId,int tableVersion, Page page){
        String sql = "SELECT A.TABLE_NAME, A.TABLE_ID, A.TABLE_RECORDS, A.TABLE_SPACE, A.TABLE_OWNER, " +
                     " TO_CHAR(A.TABLE_DATE,'YYYY-MM-DD HH:MI:SS') TABLE_DATE, A.STATE, A.TABLE_INST_ID,C.DATA_SOURCE_NAME " +
                     " FROM META_TABLE_INST A LEFT JOIN META_TABLES B ON A.TABLE_ID=B.TABLE_ID AND A.TABLE_VERSION=B.TABLE_VERSION " +
                     " LEFT JOIN META_DATA_SOURCE C ON B.DATA_SOURCE_ID=C.DATA_SOURCE_ID WHERE A.TABLE_ID="+tableId+" AND B.TABLE_VERSION="+tableVersion+" " +
                     " ORDER BY A.TABLE_INST_ID";
        if(page != null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql);
    }

    /**
     * 查询表ID对应的实例表信息，不关心其版本号
     * @param tableId
     * @return
     */
    public List<Map<String,Object>> queryTableInstanceByTableId(int tableId){
        String sql = "SELECT A.TABLE_NAME, A.TABLE_ID, A.TABLE_RECORDS, A.TABLE_SPACE, A.TABLE_OWNER, " +
                     " TO_CHAR(A.TABLE_DATE,'YYYY-MM-DD HH:MI:SS') TABLE_DATE, A.STATE, A.TABLE_INST_ID  " +
                     " FROM META_TABLE_INST A  " +
                     " WHERE A.TABLE_ID="+tableId
                     +" ORDER BY A.TABLE_INST_ID";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 根据实例表ID取出数据周期值为空的实例数据信息
     * @param tableInstId
     * @return
     */
    public List<Map<String,Object>> queryTableInstCycleNullByInstId(int tableInstId){
        String sql = "SELECT T.TABLE_INST_ID, T.TABLE_INST_DATA_ID, T.TABLE_INST_DATA_STATE, T.STATE_DATE, T.PARTITION, " +
                     "T.SUBPARTITION, T.DATA_CYCLE_NO, T.DATA_LOCAL_CODE, T.CREATE_DATE, T.ROW_RECORDS, A.ZONE_NAME " +
                     "FROM META_TABLE_INST_DATA T LEFT JOIN META_DIM_ZONE A ON T.DATA_LOCAL_CODE=A.ZONE_CODE " +
                     "WHERE DATA_CYCLE_NO IS NULL AND " +
                     "TABLE_INST_ID="+tableInstId+" ORDER BY DATA_CYCLE_NO,DATA_LOCAL_CODE";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 根据表实例ID取得表实例数据信息
     * @return
     */
    public List<Map<String,Object>> queryTableInstDataByInstId(int tableInstId, String dataLoaclCode){
        String sql = "SELECT TABLE_INST_ID, TABLE_INST_DATA_ID, TABLE_INST_DATA_STATE, STATE_DATE, PARTITION, " +
                     "SUBPARTITION, DATA_CYCLE_NO, DATA_LOCAL_CODE, CREATE_DATE, ROW_RECORDS " +
                     "FROM META_TABLE_INST_DATA WHERE TABLE_INST_ID=" + tableInstId ;
        if(dataLoaclCode != null){
            sql = sql + " AND DATA_LOCAL_CODE = '"+dataLoaclCode+"' ";
        }else{
            sql = sql + " AND DATA_LOCAL_CODE IS NULL ";
        }
        sql = sql + " ORDER BY DATA_CYCLE_NO,DATA_LOCAL_CODE";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 根据表实例ID取得对应表实例数据地域信息
     * @param tableInstId
     * @return
     */
    public List<Map<String,Object>> queryLocalCodeByInstId(int tableInstId){
        String sql = "SELECT DISTINCT T.DATA_LOCAL_CODE,A.ZONE_NAME FROM META_TABLE_INST_DATA T " +
                     "LEFT JOIN META_DIM_ZONE A ON T.DATA_LOCAL_CODE=A.ZONE_CODE WHERE T.TABLE_INST_ID=" + tableInstId
                     +" ORDER BY T.DATA_LOCAL_CODE ";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 根据实例ID获取实例信息。
     * @param instId
     * @return
     */
    public Map<String,Object> queryInstInfoById(int instId){
        String sql = "SELECT A.TABLE_NAME, A.TABLE_ID, A.TABLE_RECORDS, A.TABLE_SPACE, A.TABLE_OWNER, " +
                     " TO_CHAR(A.TABLE_DATE,'YYYY-MM-DD HH:MI:SS') TABLE_DATE, A.STATE, A.TABLE_INST_ID,B.DATA_SOURCE_ID " +
                     " FROM META_TABLE_INST A LEFT JOIN META_TABLES B ON A.TABLE_ID=B.TABLE_ID AND A.TABLE_VERSION=B.TABLE_VERSION " +
                     " WHERE TABLE_INST_ID=? "+
                     " ORDER BY A.TABLE_INST_ID";
        return getDataAccess().queryForMap(sql,instId);
    }

    /**
     *  根据实例ID修改其表版本号
     * @param instId
     * @param tableVersion
     */
    public void updateInstTableVersion(int instId,int tableVersion) throws Exception{
        String sql="UPDATE META_TABLE_INST SET TABLE_VERSION=? WHERE TABLE_INST_ID=? ";
        getDataAccess().execUpdate(sql,new Integer[]{tableVersion,instId});
    }
    /**
     * 根据表ID和表的名称查询对应表实例信息
     * @param tableId
     * @param tableName
     * @return flag为没有,ture为已经存在
     */
    public boolean queryTableInstanceByTableInfo(int tableId,String tableName){
    	boolean flag = false;
        String sql = "SELECT T.TABLE_NAME,T.TABLE_ID FROM META_TABLE_INST T WHERE T.TABLE_NAME=? AND T.TABLE_ID=? AND T.STATE=1";
        Object params[] = new Object[2];
        params[0] = tableName;
        params[1] = tableId;
        List<Map<String,Object>> list =  this.getDataAccess().queryForList(sql,params);
        if(list!=null&&list.size()!=0){
        	flag = true;
        }
        return flag;
    }
    /**
     * 新增实例表
     * @throws Exception 
     * */
    public int insertInsTableByInfo(Map<String,Object>map) throws Exception{
    	int count =-1;
    	String tableSpace = null;
    	String tableOwener = null;
    	if(map!=null&&map.size()!=0){
    	String sql = "INSERT INTO META_TABLE_INST(TABLE_NAME,TABLE_ID,TABLE_SPACE,TABLE_OWNER,TABLE_INST_ID,STATE,TABLE_VERSION) VALUES(?,?,?,?,SEQ_TAB_INST_ID.NEXTVAL,1,1)";
    	  Object params[] = new Object[4];
    	  params[0] = map.get("tableName").toString();
    	  int tableId = map.get("tableId")==null?null:Integer.parseInt(map.get("tableId").toString());
    	  params[1] = tableId;
    	  if(map.get("tableSpace")!=null){
    		  tableSpace = map.get("tableSpace").toString();
    	  }
    	  params[2] = tableSpace;
    	  if(map.get("tableOwener")!=null){
    		  tableOwener = map.get("tableOwener").toString();
    	  }
    	  params[3] = tableOwener;
    	  count = this.getDataAccess().execUpdate(sql,params);
    	}
    	
		return count;
    }

    /**
     * 根据表类ID，版本，取表类的数据源信息
     * @param tableId
     * @param tableVersion
     * @return
     */
    public Map<String, Object> queryDataSourceInfoByTableId(int tableId, int tableVersion){
        String sql = "SELECT * FROM META_DATA_SOURCE T WHERE" +
                " EXISTS (SELECT 1 FROM META_TABLES A WHERE T.DATA_SOURCE_ID = A.DATA_SOURCE_ID AND A.TABLE_ID="+tableId+" AND A.TABLE_VERSION="+tableVersion+")";
        return getDataAccess().queryForMap(sql);
    }

    /**
     * 新实例表版本为最新版本
     * @param tableId
     * @param tableVersion
     */
    public void updateInstByTableId(int tableId,int tableVersion){
        String sql = "UPDATE META_TABLE_INST SET TABLE_VERSION = " + tableVersion + " WHERE TABLE_ID="+tableId;
        getDataAccess().execUpdate(sql);
    }
    
    /**
     * 查询有效的表实例信息，按照表实例ID倒排序
     */
    public  Map<String, Object>  queryEffectTableInstanceByTableId(int tableId){
        String sql = "select TABLE_NAME,TABLE_ID,TABLE_SPACE,TABLE_OWNER from meta_table_inst where state=1 and table_id=? and rownum=1 order by table_inst_id desc";
       
        return  getDataAccess().queryForMap(sql,tableId);
    }
    
}
