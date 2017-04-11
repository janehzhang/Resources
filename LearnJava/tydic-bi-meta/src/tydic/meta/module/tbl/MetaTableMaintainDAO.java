package tydic.meta.module.tbl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;

/***
 * 
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 熊久亮
 * @date 2011-11-14
 * @description  表类维护DAO
 *
 */
public class MetaTableMaintainDAO extends MetaBaseDAO{
	/**
	 * 根据表ID获取表的基本信息以及数据源名称
	 * @param tableID
	 * @return
	 */
	public List<Map<String, Object>> getTableBaseInfoByTableId(int tableID,int tableVersion){
		String sql = "SELECT T.TABLE_ID,T.TABLE_NAME_CN,T.TABLE_BUS_COMMENT,T.TABLE_TYPE_ID,D.DATA_SOURCE_NAME,D.DATA_SOURCE_ID," +
					"T.TABLE_GROUP_ID,G.TABLE_GROUP_NAME FROM META_TABLES T "
					+ "LEFT JOIN  META_DATA_SOURCE D ON T.DATA_SOURCE_ID = D.DATA_SOURCE_ID " +
					"LEFT JOIN META_TABLE_GROUP G ON T.TABLE_GROUP_ID = G.TABLE_GROUP_ID "
					+ "WHERE T.TABLE_ID = "+tableID
					+ "AND T.TABLE_VERSION ="+tableVersion ;
		return getDataAccess().queryForList(sql);
	}
	
	public Map<String, Object> getTableBaseInfoByTableIdAndVersion(int tableId,int tableVersion){
		String sql = "SELECT T.TABLE_OWNER, T.TABLE_SPACE,T.TABLE_GROUP_ID,T.PARTITION_SQL FROM META_TABLES T "
					+ "WHERE T.TABLE_ID = "+tableId 
					+ "AND T.TABLE_VERSION = "+tableVersion;
		return getDataAccess().queryForMap(sql);
	}
	/**
	 * 获取表的层次分类
	 * @return
	 */
	public List<Map<String, Object>> getTableAllType(){
		String sql = "SELECT T.CODE_ID ,T.CODE_ITEM,T.CODE_NAME FROM META_SYS_CODE T WHERE UPPER(T.CODE_TYPE)='TABLE_TYPE'";
		return getDataAccess().queryForList(sql);
	}
	
	/**
	 * 获取业务类型
	 * @return
	 */
	public List<Map<String, Object>> getTableOperationType(){
		String sql = "SELECT T.TABLE_GROUP_ID,T.TABLE_GROUP_NAME,T.PAR_GROUP_ID FROM META_TABLE_GROUP T";
		return getDataAccess().queryForList(sql);
	}
	
	/**
	 * 根据数据源IDc查询数据源名称
	 * @param dataSouseId
	 * @return
	 */
	public List<Map<String, Object>> getTableDataSoureById(int dataSouseId){
		String sql = "SELECT T.DATA_SOURCE_NAME FROM META_DATA_SOURCE T WHERE T.DATA_SOURCE_ID = "+dataSouseId;
		return getDataAccess().queryForList(sql);
	}
	
	/**
	 * 根据表类ID修改表类基本信息,包括表中文名、业务类型、注释
	 * @param updatas
	 * @throws Exception 
	 */
	public int updateTableBaseInfoByTableId(Map<String, Object> tableDatas) throws Exception{
		String sql = "UPDATE META_TABLES T SET T.TABLE_NAME_CN = "+"'"+tableDatas.get("table_name_cn")+"'"
					  + ",T.TABLE_GROUP_ID = "+tableDatas.get("table_operType")
					  + ",T.TABLE_BUS_COMMENT= "+"'"+tableDatas.get("tableBusComment")+"'"
					  + ",T.TABLE_STATE = 0"
					  +	" WHERE T.TABLE_ID = "+tableDatas.get("tableId");
		return getDataAccess().execUpdate(sql);
	}
	
	/**
	 * 根据修改表类列信息
	 * @return
	 * @throws Exception 
	 */
	public int updataTableColsByTableId(Map<String, Object> closData) throws Exception{
		String sql = "UPDATE META_TABLE_COLS SET "
					 + "COL_NAME= "+"'"+closData.get("colName")+"'"
					 + ",COL_DATATYPE= "+"'"+closData.get("fullDataType")+"'"
					 + ",IS_PRIMARY = "+"'"+closData.get("isPrimary")+"'"
					 + ",DEFAULT_VAL = "+"'"+closData.get("defaultVal")+"'"
					 + ",COL_NULLABLED = "+"'"+closData.get("colNullabled")+"'"
					 + ",COL_BUS_COMMENT = "+"'"+closData.get("colBusComment")+"'"
					 + ",DIM_LEVEL = "+"'"+closData.get("dimLevel")+"'"
					 + "WHERE COL_ID= "+closData.get("colsId");
		return getDataAccess().execUpdate(sql);
	}
	
	/**
	 * 插入日志记录
	 * @param tableId
	 * @param tableName
	 * @param userId
	 * @param relType
	 * @return
	 * @throws Exception
	 */
	public int insertTableUpdateLog(int tableId,String tableName,int userId,int relType,int tableVersion) throws Exception{
		String sql = "insert into META_MAG_USER_TAB_REL"
					+"(REL_ID,TABLE_NAME,REL_TYPE,STATE_DATE,USER_ID,TABLE_ID,TABLE_VERSION)"
			        + "values(?,?,?,TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'),?,?,?)";
			Object params[] = new Object[7];
	        long pk = super.queryForNextVal("SEQ_TAB_REL_ID");
            params[0] = pk;
            params[1] = Convert.toString(tableName).toUpperCase();
            params[2] = relType;
	        Date stateDate = new Date();
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换格式 
            params[3] = dateFormat.format(stateDate);
            params[4] = userId;
            params[5] = tableId;
            params[6] = tableVersion;
		return getDataAccess().execUpdate(sql,params);
	}
	
	/**
	 * 插入新的表类信息
	 * @param data
	 * @throws Exception
	 */
	public void insertMetaTableByPk(Map<String, Object> data) throws Exception{
        //先查询主键ID
        String sql =
                "INSERT INTO META_TABLES( " + "TABLE_ID, TABLE_NAME, TABLE_NAME_CN, TABLE_OWNER, TABLE_BUS_COMMENT, " +
                        "TABLE_STATE, TABLE_SPACE, TABLE_GROUP_ID, DATA_SOURCE_ID, TABLE_TYPE_ID, " +
                        "TABLE_VERSION, PARTITION_SQL) " + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ";
        Object params[] = new Object[12];
        int tableId = data.get("tableId")==null?null:Integer.parseInt(data.get("tableId").toString());
        params[0] = tableId;
        String tableName = Convert.toString(data.get("tableName"));
        params[1] = tableName;
        String tableNameCn = Convert.toString(data.get("table_name_cn"));
        params[2] = tableNameCn;
        String tableOwner = Convert.toString(data.get("tableOwner"));
        params[3] = tableOwner;
        String tableBusComment = Convert.toString(data.get("tableBusComment"));
        params[4] = tableBusComment;
        int tableState = data.get("tableState")==null?null:Integer.parseInt(data.get("tableState").toString());
        params[5] = tableState;
        String tableSpace = Convert.toString(data.get("tableSpace"));
        params[6] = tableSpace;
        int tableGroupId = data.get("tableGroupId")==null?null:Integer.parseInt(data.get("tableGroupId").toString());
        params[7] = tableGroupId;
        int dataSourceId = data.get("data_sourceHidden")==null?null:Integer.parseInt(data.get("data_sourceHidden").toString());
        params[8] = dataSourceId;
        int tableTypeId = data.get("table_attType")==null?null:Integer.parseInt(data.get("table_attType").toString());
        params[9] = tableTypeId;
        int tableVersion = data.get("maxTableVersion")==null?null:Integer.parseInt(data.get("maxTableVersion").toString());
        params[10] = tableVersion;
        String partitionSql = Convert.toString(data.get("partitionSql"));
        params[11] = partitionSql;
        getDataAccess().execUpdate(sql, params);
    }
}
