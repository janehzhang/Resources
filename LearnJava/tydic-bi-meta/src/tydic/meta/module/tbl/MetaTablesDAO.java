package tydic.meta.module.tbl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.jdbc.DataAccess;
import tydic.meta.common.Constant;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.sys.code.CodeManager;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 熊小平
 * @date 2011-10-26
 * @description META_TABLES操作DAO
 */
public class MetaTablesDAO extends MetaBaseDAO {

    /**
     * @param data 例：{tableName:"test",tableNameCn:"",tableOwner:'',tableBusComment:"',tableState:1
     *             ,tableSpace:'',tableSpace:"",tableGroupId:1,dataSourceId;2,tableTypeId:2,tableVersion:2,partitionSql:"" }
     * @return long  新增成功返回其主键ID，也即tableId
     * @description 向META_TABLES插入单条记录
     */
    public long insertMetaTables(Map<String, Object> data) throws Exception {
        //先查询主键ID
        long tableId = super.queryForNextVal("SEQ_TAB_ID");
        String sql =
                "INSERT INTO META_TABLES( " + "TABLE_ID, TABLE_NAME, TABLE_NAME_CN, TABLE_OWNER, TABLE_BUS_COMMENT, " +
                        "TABLE_STATE, TABLE_SPACE, TABLE_GROUP_ID, DATA_SOURCE_ID, TABLE_TYPE_ID, " +
                        "TABLE_VERSION, PARTITION_SQL) " + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ";
        Object params[] = new Object[12];
        params[0] = tableId;
        String tableName = Convert.toString(data.get("tableName")).toUpperCase();
        params[1] = tableName;
        String tableNameCn = Convert.toString(data.get("tableNameCn"));
        params[2] = tableNameCn;
        String tableOwner = Convert.toString(data.get("tableOwner"));
        params[3] = tableOwner;
        String tableBusComment = Convert.toString(data.get("tableBusComment"));
        params[4] = tableBusComment;
        int tableState = data.get("tableState") == null ? null : Integer.parseInt(data.get("tableState").toString());
        params[5] = tableState;
        String tableSpace = Convert.toString(data.get("tableSpace"));
        params[6] = tableSpace;
        int tableGroupId = data.get("tableGroupId") == null ? null : Integer.parseInt(data.get("tableGroupId").toString());
        params[7] = tableGroupId;
        int dataSourceId = data.get("dataSourceId") == null ? null : Integer.parseInt(data.get("dataSourceId").toString());
        params[8] = dataSourceId;
        Integer tableTypeId = data.get("tableTypeId") == null ? null : Integer.parseInt(data.get("tableTypeId").toString());
        params[9] = tableTypeId;
        int tableVersion = data.get("tableVersion") == null ? null : Integer.parseInt(data.get("tableVersion").toString());
        params[10] = tableVersion;
        String partitionSql = Convert.toString(data.get("partitionSql"));
        params[11] = partitionSql;
        getDataAccess().execUpdate(sql, params);
        return tableId;
    }

    /**
     * tableID自定义生成，此方法只插入数据
     *
     * @param data 例：{tableId:1,tableName:"test",tableNameCn:"",tableOwner:'',tableBusComment:"',tableState:1
     *             ,tableSpace:'',tableSpace:"",tableGroupId:1,dataSourceId;2,tableTypeId:2,tableVersion:2,partitionSql:"" }
     * @throws Exception
     */
    public void insertMetaTableByPk(Map<String, Object> data) throws Exception {
        //先查询主键ID
        String sql =
                "INSERT INTO META_TABLES( " + "TABLE_ID, TABLE_NAME, TABLE_NAME_CN, TABLE_OWNER, TABLE_BUS_COMMENT, " +
                        "TABLE_STATE, TABLE_SPACE, TABLE_GROUP_ID, DATA_SOURCE_ID, TABLE_TYPE_ID, " +
                        "TABLE_VERSION, PARTITION_SQL) " + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ";
        Object params[] = new Object[12];
        int tableId = data.get("tableId") == null ? null : Integer.parseInt(data.get("tableId").toString());
        params[0] = tableId;
        String tableName = Convert.toString(data.get("tableName")).toUpperCase();
        params[1] = tableName;
        String tableNameCn = Convert.toString(data.get("tableNameCn"));
        params[2] = tableNameCn;
        String tableOwner = Convert.toString(data.get("tableOwner"));
        params[3] = tableOwner;
        String tableBusComment = Convert.toString(data.get("tableBusComment"));
        params[4] = tableBusComment;
        int tableState = data.get("tableState") == null ? null : Integer.parseInt(data.get("tableState").toString());
        params[5] = tableState;
        String tableSpace = Convert.toString(data.get("tableSpace"));
        params[6] = tableSpace;
        int tableGroupId = data.get("tableGroupId") == null ? null : Integer.parseInt(data.get("tableGroupId").toString());
        params[7] = tableGroupId;
        int dataSourceId = data.get("dataSourceId") == null ? null : Integer.parseInt(data.get("dataSourceId").toString());
        params[8] = dataSourceId;
        Integer tableTypeId = data.get("tableTypeId") == null ? null : Integer.parseInt(data.get("tableTypeId").toString());
        params[9] = tableTypeId;
        int tableVersion = data.get("tableVersion") == null ? null : Integer.parseInt(data.get("tableVersion").toString());
        params[10] = tableVersion;
        String partitionSql = Convert.toString(data.get("partitionSql"));
        params[11] = partitionSql;
        getDataAccess().execUpdate(sql, params);
    }

    /**
     * 根据表类分类ID，TypeId，表中文名模糊匹配查询维度表信息。
     *
     * @param queryData 内包含键值tableGroupId,tableTypeId,queryMessage,如果无查询所有。
     * @return
     * @author 张伟
     */
    public List<Map<String, Object>> queryDimTableInfo(Map<String, Object> queryData, Page page) {
        String sql = "SELECT ";
        if (!Convert.toString(queryData.get("dimTableId")).equals("")) {
            sql = sql + " CASE WHEN A.TABLE_ID = " + Convert.toString(queryData.get("dimTableId")) +
                    " THEN 1 ELSE 0 END ISEXIST, ";
        }
        sql = sql + " A.TABLE_ID, A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT, " +
                "A.TABLE_VERSION,B.TABLE_DIM_PREFIX,A.TABLE_STATE,A.DATA_SOURCE_ID " +
                "FROM META_TABLES A,META_DIM_TABLES B " +
                "WHERE A.TABLE_ID=B.DIM_TABLE_ID AND A.TABLE_STATE=" + TblConstant.META_TABLE_STATE_VAILD + " ";
        if (queryData != null && queryData.size() > 0) {
            if (queryData.get("tableGroupId") != null && !queryData.get("tableGroupId").toString().equals("")) {
                sql += "AND A.TABLE_GROUP_ID=" + queryData.get("tableGroupId") + " ";
            }
            if (queryData.get("tableTypeId") != null && !queryData.get("tableTypeId").toString().equals("")) {
                sql += "AND A.TABLE_TYPE_ID=" + queryData.get("tableTypeId") + " ";
            }
            //用表名、表中文、表注释模糊匹配queryMessage
            if (queryData.get("queryMessage") != null && !queryData.get("queryMessage").toString().equals("")) {
                String queryMessage = queryData.get("queryMessage").toString().toUpperCase();
                sql += "AND (A.TABLE_NAME LIKE " + SqlUtils.allLikeParam(queryMessage) + " OR A.TABLE_NAME_CN " +
                        "LIKE " + SqlUtils.allLikeParam(queryMessage) + " OR A.TABLE_BUS_COMMENT LIKE "
                        + SqlUtils.allLikeParam(queryMessage) + ")";
            }
        }
        sql += " ORDER BY ";
        if (!Convert.toString(queryData.get("dimTableId")).equals("")) {
            sql += " ISEXIST DESC, ";
        }
        sql += " A.TABLE_ID";
        return getDataAccess().queryForList(SqlUtils.wrapPagingSql(sql, page));
    }

    /**
     * 根据一个字符串模糊匹配系统中已存在的表名，用于前台做输入提醒。
     *
     * @param matchs
     * @return
     */
    public List<Map<String, Object>> queryMatchTables(String matchs) {
        String sql =
                "SELECT A.TABLE_ID, A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT " +
                        "FROM META_TABLES A WHERE UPPER(A.TABLE_NAME) LIKE UPPER(?) AND TABLE_STATE=" + TblConstant.META_TABLE_STATE_VAILD;
        return getDataAccess().queryForList(sql, matchs + "%");
    }

    /**
     * 判断某个命名的表是否在数据库中存在。
     *
     * @param matchs
     * @return
     */
    public boolean isExistsMatchTables(String matchs, int dataSourceId, String tableOwner) {
        String sql =
                "SELECT A.TABLE_ID, A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT " +
                        "FROM META_TABLES A WHERE UPPER(A.TABLE_NAME) = UPPER(?) AND A.DATA_SOURCE_ID=" + dataSourceId + " AND UPPER(A.TABLE_OWNER) = UPPER('" + tableOwner + "')  AND TABLE_STATE <> " + TblConstant.META_TABLE_STATE_MODIFY;
        return getDataAccess().queryForList(sql, matchs).size() > 0 ? true : false;
    }

    /**
     * 查询表类全息视图
     *
     * @param queryData
     * @return
     */
    public List<Map<String, Object>> queryMetaTables(Map<String, Object> queryData, Page page) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT, A.TABLE_SPACE, " +
                "A.TABLE_STATE, A.TABLE_ID, A.TABLE_GROUP_ID, A.DATA_SOURCE_ID, B.TABLE_GROUP_NAME, A.TABLE_TYPE_ID, " +
                "D.DATA_SOURCE_NAME,A.TABLE_VERSION,F.TABLE_DIM_LEVEL FROM META_TABLES A " +
                "LEFT JOIN META_TABLE_GROUP B ON A.TABLE_GROUP_ID = B.TABLE_GROUP_ID " +
                "LEFT JOIN META_DATA_SOURCE D ON A.DATA_SOURCE_ID = D.DATA_SOURCE_ID " +
                //关联维度表，获取其维度信息
                "LEFT JOIN META_DIM_TABLES F ON A.TABLE_ID=F.DIM_TABLE_ID " +
//                //寻找有效版本
//                ",(SELECT TABLE_ID,CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END " +
//                " VALID_VERSION  FROM " +
//                "(SELECT TABLE_ID,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0)  " +
//                "VALID_VERSION FROM META_TABLES GROUP BY TABLE_ID,TABLE_VERSION,TABLE_STATE)  GROUP BY TABLE_ID) E "+
                "WHERE " +
//                "A.TABLE_ID=E.TABLE_ID AND A.TABLE_VERSION=E.VALID_VERSION AND " +
                "A.TABLE_STATE = " + TblConstant.META_TABLE_STATE_VAILD + " ");
        List params = new ArrayList();
        if (queryData != null) {
            if (queryData.get("dataSourceId") != null && !queryData.get("dataSourceId").toString().equals("")) {
                sql.append("AND A.DATA_SOURCE_ID = ? ");
                int dataSourceId = queryData.get("dataSourceId") == null ? null : Integer.parseInt(queryData.get("dataSourceId").toString());
                params.add(dataSourceId);
            }

            if (queryData.get("tableGroupId") != null && !queryData.get("tableGroupId").toString().equals("")) {
                //业务类型
                sql.append("AND A.TABLE_GROUP_ID = ? ");
                int tableGroupId = queryData.get("tableGroupId") == null ? null : Integer.parseInt(queryData.get("tableGroupId").toString());
                params.add(tableGroupId);
            }

            if (queryData.get("tableTypeId") != null && !queryData.get("tableTypeId").toString().equals("")) {
                //层次分类
                sql.append("AND A.TABLE_TYPE_ID = ? ");
                int tableTypeId = queryData.get("tableTypeId") == null ? null : Integer.parseInt(queryData.get("tableTypeId").toString());
                params.add(tableTypeId);
            }

            //关键字处理，仅支持单个关键字
            if (queryData.get("keyWord") != null && !queryData.get("keyWord").toString().equals("")) {
                String key = queryData.get("keyWord").toString();
                if (!key.contains("%") && !key.contains("_")) {
                    sql.append("AND (Upper(A.TABLE_NAME) LIKE UPPER(?) OR ");
                    sql.append("UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) OR UPPER(A.TABLE_BUS_COMMENT) LIKE UPPER(?)) ");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                } else {
                    key = key.replaceAll("_", "/_").replaceAll("%", "/%");
                    sql.append("AND (Upper(A.TABLE_NAME) LIKE UPPER(?) ESCAPE '/' OR ");
                    sql.append("UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) ESCAPE '/' OR UPPER(A.TABLE_BUS_COMMENT) LIKE UPPER(?) ESCAPE '/' ) ");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                }
            }

            if (queryData.get("excludTableId") != null && !queryData.get("excludTableId").toString().equals("")) {
                int excludTableId = queryData.get("excludTableId") == null ? null : Integer.parseInt(queryData.get("excludTableId").toString());
                sql.append(" AND A.TABLE_ID <> " + excludTableId + " ");
            }
        }

        sql.append("ORDER BY A.TABLE_ID DESC ");

        String pageSql = sql.toString();
        //分页包装
        if (page != null) {
            pageSql = SqlUtils.wrapPagingSql(pageSql, page);
        }

        List<Map<String, Object>> rs = getDataAccess().queryForList(pageSql, params.toArray());
        if (rs != null && rs.size() > 0) {
            for (Map<String, Object> map : rs) {
                map.put("CODE_NAME", CodeManager.getName(TblConstant.META_SYS_CODE_TABLE_TYPE, MapUtils.getString(map, "TABLE_TYPE_ID")));
            }
        }
        return rs;
    }

    /**
     * 维护表类-查询表类信息列表
     *
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryMetaTablesMatain(Map<String, Object> queryData, Page page) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT, A.TABLE_SPACE, " +
                "A.TABLE_STATE, A.TABLE_ID, A.TABLE_GROUP_ID, A.DATA_SOURCE_ID, B.TABLE_GROUP_NAME, A.TABLE_TYPE_ID, " +
                "D.DATA_SOURCE_NAME,A.TABLE_VERSION,F.TABLE_DIM_LEVEL FROM META_TABLES A " +
                "LEFT JOIN META_TABLE_GROUP B ON A.TABLE_GROUP_ID = B.TABLE_GROUP_ID " +
                "LEFT JOIN META_DATA_SOURCE D ON A.DATA_SOURCE_ID = D.DATA_SOURCE_ID " +
                //关联维度表，获取其维度信息
                "LEFT JOIN META_DIM_TABLES F ON A.TABLE_ID=F.DIM_TABLE_ID " +
                //寻找有效版本
                ",(SELECT TABLE_ID,CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END " +
                " VALID_VERSION  FROM " +
                "(SELECT TABLE_ID,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0)  " +
                "VALID_VERSION FROM META_TABLES WHERE TABLE_STATE<>" + TblConstant.META_TABLE_STATE_MODIFY + "  GROUP BY TABLE_ID,TABLE_VERSION,TABLE_STATE)  GROUP BY TABLE_ID) E " +
                "WHERE A.TABLE_ID=E.TABLE_ID AND A.TABLE_VERSION=E.VALID_VERSION ");
        List params = new ArrayList();
        if (queryData != null) {
            if (queryData.get("dataSourceId") != null && !queryData.get("dataSourceId").toString().equals("")) {
                sql.append("AND A.DATA_SOURCE_ID = ? ");
                int dataSourceId = queryData.get("dataSourceId") == null ? null : Integer.parseInt(queryData.get("dataSourceId").toString());
                params.add(dataSourceId);
            }

            if (queryData.get("tableGroupId") != null && !queryData.get("tableGroupId").toString().equals("")) {
                //业务类型
                sql.append("AND A.TABLE_GROUP_ID = ? ");
                int tableGroupId = queryData.get("tableGroupId") == null ? null : Integer.parseInt(queryData.get("tableGroupId").toString());
                params.add(tableGroupId);
            }

            if (queryData.get("tableTypeId") != null && !queryData.get("tableTypeId").toString().equals("")) {
                //层次分类
                sql.append("AND A.TABLE_TYPE_ID = ? ");
                int tableTypeId = queryData.get("tableTypeId") == null ? null : Integer.parseInt(queryData.get("tableTypeId").toString());
                params.add(tableTypeId);
            }

            //关键字处理，仅支持单个关键字
            if (queryData.get("keyWord") != null && !queryData.get("keyWord").toString().equals("")) {
                String key = queryData.get("keyWord").toString();
                if (!key.contains("%") && !key.contains("_")) {
                    sql.append("AND (Upper(A.TABLE_NAME) LIKE UPPER(?) OR ");
                    sql.append("UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) OR UPPER(A.TABLE_BUS_COMMENT) LIKE UPPER(?)) ");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                } else {
                    key = key.replaceAll("_", "/_").replaceAll("%", "/%");
                    sql.append("AND (Upper(A.TABLE_NAME) LIKE UPPER(?) ESCAPE '/' OR ");
                    sql.append("UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) ESCAPE '/' OR UPPER(A.TABLE_BUS_COMMENT) LIKE UPPER(?) ESCAPE '/' ) ");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                }
            }
        }

        sql.append("ORDER BY A.TABLE_ID DESC ");

        String pageSql = sql.toString();
        //分页包装
        if (page != null) {
            pageSql = SqlUtils.wrapPagingSql(pageSql, page);
        }

        List<Map<String, Object>> rs = getDataAccess().queryForList(pageSql, params.toArray());
        if (rs != null && rs.size() > 0) {
            for (Map<String, Object> map : rs) {
                map.put("CODE_NAME", CodeManager.getName(TblConstant.META_SYS_CODE_TABLE_TYPE, MapUtils.getString(map, "TABLE_TYPE_ID")));
            }
        }
        return rs;
    }

    /**
     * 根据表ID和版本号查询表所有信息，其中包括维度表扩展信息。
     *
     * @param tableId
     * @param tableVersion
     * @return
     */
    public Map<String, Object> queryTablesByIdAndVersion(int tableId, int tableVersion) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT, A.TABLE_SPACE, " +
                "A.TABLE_STATE, A.TABLE_ID, A.TABLE_GROUP_ID, A.DATA_SOURCE_ID,A.PARTITION_SQL, B.TABLE_GROUP_NAME, A.TABLE_TYPE_ID, " +
                "D.DATA_SOURCE_NAME,A.TABLE_VERSION,F.TABLE_DIM_LEVEL,F.TABLE_DIM_PREFIX," +
                "F.DIM_KEY_COL_ID,F.DIM_PAR_KEY_COL_ID,F.LAST_LEVEL_FLAG FROM META_TABLES A " +
                "LEFT JOIN META_TABLE_GROUP B ON A.TABLE_GROUP_ID = B.TABLE_GROUP_ID " +
                "LEFT JOIN META_DATA_SOURCE D ON A.DATA_SOURCE_ID = D.DATA_SOURCE_ID " +
                //关联维度表，获取其维度信息
                "LEFT JOIN META_DIM_TABLES F ON A.TABLE_ID=F.DIM_TABLE_ID " +
                "WHERE A.TABLE_ID=" + tableId + " AND A.TABLE_VERSION=" + tableVersion);

        Map<String, Object> rs = getDataAccess().queryForMap(sql.toString());
        rs.put("CODE_NAME", CodeManager.getName(TblConstant.META_SYS_CODE_TABLE_TYPE, MapUtils.getString(rs, "TABLE_TYPE_ID")));
        return rs;
    }


    /**
     * 查询表类
     *
     * @param
     * @return
     */
    public List<Map<String, Object>> queryTables(Map<String, Object> queryData, Page page) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.TABLE_ID, A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_BUS_COMMENT ");
        sql.append("FROM META_TABLES A WHERE 1=1 ");
        List params = new ArrayList();
        if (queryData != null) {

            //业务类型
            if (queryData.get("tableGroupId") != null && !queryData.get("tableGroupId").toString().equals("")) {
                sql.append("AND A.TABLE_GROUP_ID = ? ");
                int tableGroupId = queryData.get("tableGroupId") == null ? null : Integer.parseInt(queryData.get("tableGroupId").toString());
                params.add(tableGroupId);
            }

            //层次分类
            if (queryData.get("tableTypeId") != null && !queryData.get("tableTypeId").toString().equals("")) {
                sql.append("AND A.TABLE_TYPE_ID = ? ");
                int tableTypeId = queryData.get("tableTypeId") == null ? null : Integer.parseInt(queryData.get("tableTypeId").toString());
                params.add(tableTypeId);
            }

            if (queryData.get("keyWord") != null && !queryData.get("keyWord").toString().equals("")) {
                // 关键字匹配方式：表名，表中文，表注释三个字段模糊匹配
                sql.append("AND (UPPER(A.TABLE_NAME) LIKE UPPER(?) OR UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) " +
                        "OR UPPER(A.TABLE_BUS_COMMENT) LIKE UPPER(?)) ");

                params.add("%" + queryData.get("keyWord").toString() + "%");
                params.add("%" + queryData.get("keyWord").toString() + "%");
                params.add("%" + queryData.get("keyWord").toString() + "%");
            }
        }

        sql.append("ORDER BY A.TABLE_ID ");
        return getDataAccess().queryForList(SqlUtils.wrapPagingSql(sql.toString(), page),
                params.toArray());
    }

    /**
     * 根据表ID取表基本信息
     *
     * @param tableId
     * @return
     */
    public Map<String, Object> queryTableByTableId(int tableId, int tableVersion) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.TABLE_ID, A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT, " +
                "A.TABLE_STATE, A.TABLE_SPACE, A.TABLE_GROUP_ID, A.DATA_SOURCE_ID, A.TABLE_TYPE_ID, " +
                "A.TABLE_VERSION, A.PARTITION_SQL, B.TABLE_GROUP_NAME, " +
                "D.DATA_SOURCE_NAME FROM META_TABLES A " +
                "LEFT JOIN META_TABLE_GROUP B ON A.TABLE_GROUP_ID = B.TABLE_GROUP_ID " +
                "LEFT JOIN META_DATA_SOURCE D ON A.DATA_SOURCE_ID = D.DATA_SOURCE_ID " +
                "WHERE A.TABLE_ID=" + tableId + " AND A.TABLE_VERSION=" + tableVersion);
        DataAccess access = getDataAccess();
        Map<String, Object> rs = access.queryForMap(sql.toString());
        rs.put("CODE_NAME", CodeManager.getName(TblConstant.META_SYS_CODE_TABLE_TYPE, MapUtils.getString(rs, "TABLE_TYPE_ID")));
        return rs;
    }

    /**
     * 根据表ID取表有效信息
     *
     * @param tableId
     * @return
     */
    public Map<String, Object> queryTableByTableId(int tableId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.TABLE_ID, A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT, " +
                "A.TABLE_STATE, A.TABLE_SPACE, A.TABLE_GROUP_ID, A.DATA_SOURCE_ID, A.TABLE_TYPE_ID, " +
                "A.TABLE_VERSION, A.PARTITION_SQL, B.TABLE_GROUP_NAME, " +
                "D.DATA_SOURCE_NAME FROM META_TABLES A " +
                "LEFT JOIN META_TABLE_GROUP B ON A.TABLE_GROUP_ID = B.TABLE_GROUP_ID " +
                "LEFT JOIN META_DATA_SOURCE D ON A.DATA_SOURCE_ID = D.DATA_SOURCE_ID " +
                "WHERE A.TABLE_ID=" + tableId + " AND A.TABLE_STATE=" + TblConstant.META_TABLE_STATE_VAILD);
        DataAccess access = getDataAccess();
        Map<String, Object> rs = access.queryForMap(sql.toString());
//        rs.put("CODE_NAME", CodeManager.getName(TblConstant.META_SYS_CODE_TABLE_TYPE,MapUtils.getString(rs,"TABLE_TYPE_ID")));
        return rs;
    }

    /**
     * 根据表类名查询当前最大的版本号
     *
     * @param tableId
     * @return
     */
    public int queryMaxVersion(long tableId) {
        String sql = "SELECT MAX(TABLE_VERSION) FROM META_TABLES WHERE　TABLE_ID=?";
        return getDataAccess().queryForInt(sql, tableId);
    }

    /**
     * 将指定表类名的所有版本全部置为失效。
     *
     * @param tableId
     * @return
     */
    public int inValidTable(int tableId) throws Exception {
        String sql = "UPDATE  META_TABLES SET TABLE_STATE=" + TblConstant.META_TABLE_STATE_INVAILD + " WHERE TABLE_ID=?";
        return getDataAccess().execUpdate(sql, tableId);
    }

    /**
     * 根据指定的表名返回其所有版本的表ID
     *
     * @param tableName
     * @return
     */
    public Object[] queryTableIdsByTableName(String tableName) {
        String sql = "SELECT TABLE_ID FROM META_TABLES WHERE　TABLE_NAME=?";
        return getDataAccess().queryForArray(sql, tableName);
    }

    /**
     * 根据表类Id和版本号下线该表类
     *
     * @param tableId
     * @param version
     * @throws Exception
     */
    public int offlineTable(int tableId, int version) throws Exception {
        String sql = "UPDATE  META_TABLES SET TABLE_STATE=" + TblConstant.META_TABLE_STATE_INVAILD + " WHERE TABLE_ID=" + tableId + " AND TABLE_VERSION=" + version;
        return getDataAccess().execUpdate(sql);
    }

    /**
     * 根据表类Id和版本号上线该表类
     *
     * @param tableId
     * @param version
     * @throws Exception
     */
    public int onlineTable(int tableId, int version) throws Exception {
        String sql = "UPDATE  META_TABLES SET TABLE_STATE=" + TblConstant.META_TABLE_STATE_VAILD + " WHERE TABLE_ID=" + tableId + " AND TABLE_VERSION=" + version;
        return getDataAccess().execUpdate(sql);
    }

    /**
     * 将指定表类名的所有版本全部置为失效。
     *
     * @param tableId
     * @return
     */
    public int updateValidTable(int tableId) throws Exception {
        String sql = "UPDATE  META_TABLES SET TABLE_STATE=" + TblConstant.META_TABLE_STATE_INVAILD + " WHERE TABLE_ID=?";
        return getDataAccess().execUpdate(sql, tableId);
    }

    /**
     * 设置某表类的某一个版本为有效
     *
     * @param tableId
     * @return
     */
    public int updateTableVersionVaild(int tableId, int tableVersion) throws Exception {
        String sql = "UPDATE  META_TABLES SET TABLE_STATE=" + TblConstant.META_DIM_TYPE_STATE_VALID + " WHERE TABLE_ID=" + tableId + " AND TABLE_VERSION=" + tableVersion;
        return getDataAccess().execUpdate(sql);
    }


    /**
     * 取得表类的有效版本：1，如果表类有一个版本有效，返回这个版本；
     * 2，如果该表类所有版本都无效，则返回最大版本
     *
     * @param tableId
     * @return
     */
    public int queryValidVersion(int tableId) {
        String sql = "SELECT CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END " +
                " VALID_VERSION  FROM " +
                " (SELECT TABLE_ID,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0)  VALID_VERSION FROM META_TABLES " +
                "  GROUP BY TABLE_ID,TABLE_VERSION,TABLE_STATE)WHERE table_id=" + tableId + " GROUP BY TABLE_ID ";

        return getDataAccess().queryForInt(sql);
    }

    /**
     * @param dataSourceId
     * @return List<Map<String,Object>>
     * @throws
     * @Title: getUserNameListByDataSourceId
     * @Description: 获取指定数据库的用户列表（包括系统用户表）
     */
    public List<Map<String, Object>> getUserNameListByDataSourceId(int dataSourceId) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("select username from all_users order by USERNAME");
//    	buffer.append("SELECT USERNAME,DEFAULT_TABLESPACE TABLESPACE FROM DBA_USERS WHERE DEFAULT_TABLESPACE NOT IN('SYSAUX','SYSTEM')");
//    	buffer.append(" AND ACCOUNT_STATUS='OPEN' ORDER BY USERNAME");
        return getDataAccess(dataSourceId + "").queryForList(buffer.toString());
    }

    /**
     * @param map
     * @return List<Map<String,Object>>
     * @throws
     * @Title: getTableSpaceByDataSourceId
     * @Description: 获取指定数据库的表空间
     */
    public List<Map<String, Object>> getTableSpaceByDataSourceId(int dataSourceId) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT T.TABLESPACE_NAME NAME FROM USER_TABLESPACES T ");
//    	buffer.append("SELECT USERNAME,DEFAULT_TABLESPACE TABLESPACE FROM DBA_USERS WHERE DEFAULT_TABLESPACE NOT IN('SYSAUX','SYSTEM')");
//    	buffer.append(" AND ACCOUNT_STATUS='OPEN' ORDER BY USERNAME");
        return getDataAccess(dataSourceId).queryForList(buffer.toString());
    }

    /**
     * 根据数据源取默认表空间
     *
     * @param dataSourceId
     * @return
     */
    public String getDefaultTablespaceByDataSource(int dataSourceId) {
        String sql = "SELECT DEFAULT_TABLESPACE FROM USER_USERS";
        return getDataAccess(dataSourceId + "").queryForString(sql);
    }

    /**
     * @param map   数据源
     * @param owner 数据库用户
     * @return List<Map<String,Object>>
     * @throws
     * @Title: getTablesByOwner
     * @Description: 根据用户获取该数据源中的所有表
     */
    public List<Map<String, Object>> getTablesByOwner(Map<String, Object> map, String owner, String keyWord, int dataSourceID, Page page) {
        StringBuffer buffer = new StringBuffer();
//    	buffer.append("select t.table_name as praTableName,t.tablespace_name as tableSpace from all_tables t where t.owner = '"+owner+"'");

        buffer.append("SELECT C.COMMENTS tablebuscomment,C.COMMENTS tabletypename,T.TABLE_NAME praTableName,");
        buffer.append(" T.TABLESPACE_NAME tableSpace,T.PARTITIONED FROM USER_TAB_COMMENTS C,ALL_TABLES T");
        buffer.append(" WHERE C.TABLE_NAME=T.TABLE_NAME AND T.OWNER='" + owner + "'");

        if (keyWord != null && !keyWord.equals("")) {
            buffer.append(" AND t.TABLE_NAME LIKE " + SqlUtils.allLikeParam(keyWord.toUpperCase()));
        }
        List<Map<String, Object>> mlist = null;
        DataAccess da = null;
        try {
            da = getDataAccess(dataSourceID);
            if (da == null) {
                mlist = new ArrayList<Map<String, Object>>();
                Map<String, Object> m = new HashMap<String, Object>();
                m.put("errorstring", "连接数据源失败！");
                mlist.add(m);
                return mlist;
            } else {
                String pageSql = buffer.toString();
                pageSql += " order by T.table_name";
                // 分页包装
                if (page != null) {
                    pageSql = SqlUtils.wrapPagingSql(pageSql, page);
                }

                mlist = da.queryForList(pageSql);
                if (mlist != null && mlist.size() > 0) {
//                    //已经导入的表
//                    buffer = null;
//                    buffer = new StringBuffer();
//                    buffer.append(" select distinct a.* from (select b.table_name from META_TABLES b where b.table_owner = '" + owner + "'");
//                    buffer.append(" and b.data_source_id = " + dataSourceID + " union all select a.table_name from META_TABLE_INST a");
//                    buffer.append(" where a.TABLE_OWNER = '" + owner + "' and a.table_id in (select t.table_id");
//                    buffer.append(" from meta_tables t where t.table_owner = '" + owner + "' and t.data_source_id = " + dataSourceID + ")) a");
//                    List<Map<String, Object>> olist = null;
//                    da = getDataAccess();
//                    olist = da.queryForList(buffer.toString());
//                    List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
//                    if (olist != null && olist.size() > 0) {
//                        for (Map<String, Object> mm : mlist) {
//                            Map<String, Object> bm = new HashMap<String, Object>();
//                            bm.put("TABLE_NAME", mm.get("PRATABLENAME"));
//                            if (!olist.contains(bm)) {
//                                resList.add(mm);
//                            }
//                        }
//                        return resList;
//                    } else
                	return mlist;
                } else {
                    mlist = new ArrayList<Map<String, Object>>();
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put("errorstring", "没有相关实体表！");
                    mlist.add(m);
                    return mlist;
                }
            }
        } catch (Exception e) {
            mlist = null;
            mlist = new ArrayList<Map<String, Object>>();
            Map<String, Object> m = new HashMap<String, Object>();
            m.put("errorstring", "连接数据源失败！");
            mlist.add(m);
            return mlist;
        }

        //排除已经导入的系统表
//    	buffer.append(" and t.table_name not in (");
//    	buffer.append(" select distinct a.* from (select b.table_name from META_TABLES b where b.table_owner = '"+owner+"'");
//    	buffer.append(" and b.data_source_id = "+dataSourceID+" union all select a.table_name from META_TABLE_INST a");
//    	buffer.append(" where a.TABLE_OWNER = '"+owner+"' and a.table_id in (select t.table_id");
//    	buffer.append(" from meta_tables t where t.table_owner = '"+owner+"' and t.data_source_id = "+dataSourceID+")) a");
//    	buffer.append(")");
    }

    /**
     * @param datasourceId
     * @return String[]
     * @throws
     * @Title: getTableSpaces
     * @Description: 获取该数据源的用户空间
     */
    public String[] getTableSpaces(int datasourceId) {
        String sql = "SELECT T.TABLESPACE_NAME NAME FROM USER_TABLESPACES T";
        return getDataAccess(datasourceId + "").queryForDataTable(sql).getColAsString(0);
    }

    /**
     * 根据数据源ID获取数据源名称
     *
     * @param dataSourceId
     * @return
     */
    public Map<String, Object> getUserNameByDataSourceId(int dataSourceId) {
        String sql = "SELECT T.DATA_SOURCE_USER,T.DATA_SOURCE_PASS,T.DATA_SOURCE_RULE FROM META_DATA_SOURCE T WHERE DATA_SOURCE_ID = " + dataSourceId;
        return getDataAccess().queryForMap(sql);
    }

    /**
     * 根据数据源ID获取数据源登陆用户名称
     *
     * @param dataSourceId
     * @return
     */
    public Map<String, Object> getLoginUserNameByDataSourceId(int dataSourceId) {
        String sql = "SELECT * FROM META_DATA_SOURCE T WHERE DATA_SOURCE_ID = " + dataSourceId;
        return getDataAccess().queryForMap(sql);
    }

    /**
     * @param tableId
     * @return void
     * @throws Exception
     * @throws
     * @Title: deleteMetaTableRelByTableId
     * @Description: 根据表ID删除关系表中数据
     */
    public void deleteMetaTableRelByTableId(int tableId) throws Exception {
        String sql = "DELETE FROM META_TABLE_REL WHERE TABLE_ID1=" + tableId;
        getDataAccess().execUpdate(sql);
    }

    /**
     * 我的表类申请中，重新申请新增被驳回的表类，对表类名重复性的检测
     *
     * @param tableName
     * @param owner
     * @param dataSourceID
     * @param orgTableId
     * @return
     * @throws Exception
     */
    public boolean ifExistTableFromMyApply(String tableName, String owner, int dataSourceID, int orgTableId) {
        String sql = "SELECT COUNT(*) FROM META_TABLES A WHERE A.TABLE_STATE = " + TblConstant.META_TABLE_STATE_MODIFY
                + " AND A.TABLE_NAME = ? AND A.TABLE_OWNER = ? AND A.DATA_SOURCE_ID = " + dataSourceID + " AND A.TABLE_ID <> " + orgTableId + " " +
                "AND NOT EXISTS(SELECT 1 FROM META_MAG_USER_TAB_REL B WHERE A.TABLE_ID = B.TABLE_ID AND (B.REL_TYPE = " + TblConstant.USER_TAB_REL_TYPE_REJECT + " OR B.REL_TYPE = " + TblConstant.USER_TAB_REL_TYPE_PASS + "))";
        return getDataAccess().queryForInt(sql, tableName, owner) > 0;
    }

    /**
     * 组件查询表类DAO
     *
     * @param data
     * @param page
     * @return
     * @author 王春生
     */
    public List<Map<String, Object>> queryCmpTables(Map<String, Object> data, Page page) {
        List param = new ArrayList();
        StringBuffer bufferSql = new StringBuffer("SELECT T.TABLE_ID,T.TABLE_OWNER,T.TABLE_NAME,T.TABLE_TYPE_ID," +
                " B.TABLE_GROUP_NAME,C.DATA_SOURCE_NAME,T.TABLE_BUS_COMMENT" +
                " FROM META_TABLES T " +
                " LEFT JOIN(SELECT TABLE_GROUP_ID,TABLE_TYPE_ID,TABLE_GROUP_NAME FROM META_TABLE_GROUP)" +
                " B ON T.TABLE_GROUP_ID = B.TABLE_GROUP_ID AND T.TABLE_TYPE_ID=B.TABLE_TYPE_ID " +
                " LEFT JOIN(SELECT DATA_SOURCE_NAME,DATA_SOURCE_ID FROM META_DATA_SOURCE) " +
                " C ON T.DATA_SOURCE_ID=C.DATA_SOURCE_ID " +
                " WHERE T.TABLE_STATE=1 ");
        if (data.get("specialFilter") != null) {
            Map specialFilter = MapUtils.getMap(data, "specialFilter");
            if (specialFilter != null && specialFilter.containsKey("TABLE_TYPE_ID") && !"".equals(specialFilter.get("TABLE_TYPE_ID"))) {
                bufferSql.append(" AND T.TABLE_TYPE_ID NOT IN(" + specialFilter.get("TABLE_TYPE_ID") + ")");
            }
            if (specialFilter != null && specialFilter.containsKey("TABLE_ID") && !"".equals(specialFilter.get("TABLE_ID"))) {
                bufferSql.append(" AND T.TABLE_ID NOT IN(" + specialFilter.get("TABLE_ID") + ")");
            }
        }
        if (data.get("keyword") != null && !data.get("keyword").equals("")) {
            bufferSql.append(" AND T.TABLE_NAME LIKE ? ESCAPE '/' ");
            param.add("%" + data.get("keyword").toString().toUpperCase() + "%");
        }
        if (data.get("dataSourceId") != null && !data.get("dataSourceId").equals("")) {
            bufferSql.append(" AND C.DATA_SOURCE_ID=? ");
            param.add(data.get("dataSourceId"));
        }
        if (data.get("tableOwner") != null && !data.get("tableOwner").equals("") && !data.get("tableOwner").equals("-1")) {
            bufferSql.append(" AND LOWER(T.TABLE_OWNER)=? ");
            param.add(data.get("tableOwner").toString().toLowerCase());
        }
        if (data.get("tableTypeId") != null && !data.get("tableTypeId").equals("")) {
            bufferSql.append(" AND T.TABLE_TYPE_ID=? ");
            param.add(data.get("tableTypeId"));
        }
        if (data.get("tableGroupId") != null && !data.get("tableGroupId").equals("")) {
            bufferSql.append(" AND T.TABLE_GROUP_ID=? ");
            param.add(data.get("tableGroupId"));
        }
        bufferSql.append(" ORDER BY T.TABLE_ID DESC");

        String sql = bufferSql.toString();
        if (page != null) {
            sql = SqlUtils.wrapPagingSql(sql, page);
        }

        List<Map<String, Object>> rs = getDataAccess().queryForList(sql, param.toArray());
        //加入码表信息
        if (rs != null && rs.size() > 0) {
            for (Map<String, Object> map : rs) {
                map.put("TABLE_TYPE_NAME", CodeManager.getName(TblConstant.META_SYS_CODE_TABLE_TYPE, MapUtils.getString(map, "TABLE_TYPE_ID")));
            }
        }
        return rs;
    }

    /**
     * @param tableName
     * @return int
     * @throws
     * @Title: getCountsByTableName
     * @Description: 根据表名获取表类中个数
     */
    public int getCountsByTableName(String tableName) {
        int counts = 0;
        String sql = "SELECT COUNT(ROWID) COUNTS FROM META_TABLES WHERE TABLE_NAME=?";
        Object[][] o = getDataAccess().queryForArray(sql, false, new Object[]{tableName});
        if (o != null && o.length > 0) {
            counts = Integer.valueOf("" + o[0][0]);
        }
        return counts;
    }
}
