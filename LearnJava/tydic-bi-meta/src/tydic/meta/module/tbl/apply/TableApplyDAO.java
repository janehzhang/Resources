package tydic.meta.module.tbl.apply;


import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.frame.jdbc.DataAccess;
import tydic.frame.jdbc.JdbcException;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.tbl.TblCommon;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 表类申请相关DAO <br>
 * @date 2011-10-31
 */
public class TableApplyDAO extends MetaBaseDAO {
    /**
     * 测试SQL语句
     *
     * @param sqls 建表SQL
     * @param url  数据源连接URL
     * @param user 用户名
     * @param pwd  密码
     * @return
     */
    public void testCreatetSql(List<String> sqls, String dataSourceId, String tableName, String tableOwner)
            throws Exception {
        Statement statement = null;
        Connection connection = null;
        sqls.add("DROP TABLE " + tableOwner + "." + tableName);
        try {
            connection = getConnection(dataSourceId);
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            for (int i = 0; i < sqls.size(); i++) {
                statement.addBatch(TblCommon.sqlMarcoReplace(sqls.get(i)));
            }
            statement.executeBatch();
            //测试SQL不用提交。
            connection.rollback();
        } catch (Exception e) {
            //如果报错，应该尝试删除表，因为建表SQL没有事物回滚。
            try {
                statement = connection.createStatement();
                statement.execute(TblCommon.sqlMarcoReplace(sqls.get(sqls.size() - 1)));//最后一条语句是删除表语句
            } catch (Exception e1) {
            }
            throw e;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * 根据给定的连接查询数据库中的所有状态有效且是普通用户的用户
     *
     * @param url
     * @param user
     * @param pwd
     * @return
     */
    public String[] queryDbUsers(String dataSourceId) {
        String sql = "select USERNAME from all_users order by USERNAME";
        DataAccess access = getDataAccess(dataSourceId);
        try {
            return access.queryForDataTable(sql).getColAsString(0);
        } catch (JdbcException e) {
            e.printStackTrace();
            return new String[]{};
        }

    }

    /**
     * 查询数据库表信息。
     *
     * @param url
     * @param user
     * @param pwd
     * @param owner
     * @param keyWord
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryDbTables(String dataSourceID, String owner, String keyWord,
                                                   Page page) {
        String sql = "select A.OWNER TABLE_OWNER, A.TABLE_NAME, TABLESPACE_NAME TABLE_SPACE, B.COMMENTS TABLE_BUS_COMMENT from (select * from all_tables a where a.owner = ?) a  left join (select * from all_tab_comments t where t.owner = ?) b on a.table_name = b.table_name where 1=1 ";
        if (keyWord != null && !keyWord.equals("")) {
            keyWord = keyWord.replaceAll("_", "/_").replaceAll("%","/%");
            sql += " AND (A.TABLE_NAME LIKE '%" + keyWord + "%' escape '/' OR B.COMMENTS LIKE '%" + keyWord + "%' escape '/' ) ";
        }
        sql += "ORDER BY A.TABLE_NAME";
        DataAccess access = getDataAccess(dataSourceID);
        access.setQueryTimeout(600);
        return access.queryForList(SqlUtils.wrapPagingSql(sql, page), owner, owner);
    }

    /**
     * 查询数据库某个表的字段信息。
     *
     * @param url
     * @param user
     * @param pwd
     * @param owner
     * @param tableName
     * @return
     */
    public List<Map<String, Object>> queryDbTableColumns(String dataSourceId, String owner, String tableName) {
        String sql = "SELECT A.COLUMN_NAME COL_NAME, " +
                "    DATA_TYPE  COL_DATATYPE,DATA_SCALE, DECODE(NULLABLE, 'Y', 1, 0) COL_NULLABLED, " +
                "    DATA_DEFAULT DEFAULT_VAL, DATA_SCALE COL_PREC, B.COMMENTS COL_BUS_COMMENT,DATA_TYPE, " +
                "    CASE  WHEN DATA_TYPE='DATE' THEN NULL WHEN DATA_PRECISION > 0 THEN DATA_PRECISION WHEN DATA_TYPE='NUMBER' THEN NULL  ELSE CASE " +
                "    WHEN CHAR_COL_DECL_LENGTH > 0 THEN CHAR_COL_DECL_LENGTH ELSE DATA_LENGTH " +
                "    END END  COL_SIZE, CASE WHEN C.TABLE_NAME IS NULL THEN 0 ELSE 1 END IS_PRIMARY " +
                "    FROM   ALL_TAB_COLUMNS A " +
                "    LEFT   JOIN ALL_COL_COMMENTS B " +
                "    ON     A.TABLE_NAME = B.TABLE_NAME " +
                "    AND    A.OWNER = B.OWNER " +
                "    AND    A.COLUMN_NAME = B.COLUMN_NAME " +
                "    LEFT   JOIN (SELECT C.TABLE_NAME, COLUMN_NAME, C.OWNER " +
                "    FROM   ALL_CONSTRAINTS C, ALL_CONS_COLUMNS COL " +
                "    WHERE  C.CONSTRAINT_NAME = COL.CONSTRAINT_NAME " +
                "    AND    C.CONSTRAINT_TYPE = 'P') C " +
                "    ON     A.TABLE_NAME = C.TABLE_NAME " +
                "    AND    A.OWNER = C.OWNER " +
                "    AND    A.COLUMN_NAME = C.COLUMN_NAME " +
                "    WHERE  A.TABLE_NAME = ? AND A.OWNER = ?  " +
                "    ORDER  BY A.COLUMN_ID";
        DataAccess access = getDataAccess(dataSourceId);
        List<Map<String, Object>> datas = access.queryForList(sql, tableName.toUpperCase(), owner.toUpperCase());
        //计算列长度
        for (Map<String, Object> queryData : datas) {
            String colDataType = MapUtils.getString(queryData, "COL_DATATYPE");
            String dataSize = MapUtils.getString(queryData, "COL_SIZE");
            String dataScale = MapUtils.getString(queryData, "COL_PREC");
            if (StringUtils.isNotEmpty(dataSize)&& Convert.toInt(dataSize)>0) {
                colDataType += "(" + dataSize;
                if (StringUtils.isNotEmpty(dataScale)&&Convert.toInt(dataScale)>0) {
                    colDataType += "," + dataScale;
                }
                colDataType += ")";
                queryData.put("COL_DATATYPE", colDataType);
            }
        }
        return datas;
    }
    
    public Map<String, Object> queryColNameCnAndColId(String tableName,int dataSourceID,String colName) {
    	String sql = 	"SELECT COL_ID,COL_NAME_CN "+
					 	"FROM META_TABLE_COLS "+
					 	"WHERE TABLE_ID = "+
					       "(SELECT TABLE_ID "+
					         	"FROM META_TABLES "+
					         	"WHERE TABLE_NAME = ? "+
					         		"AND TABLE_VERSION = "+
					         			"(SELECT MAX(TABLE_VERSION) "+
					         				"FROM META_TABLES "+
					         				"WHERE TABLE_NAME = ?)) "+
					     "AND COL_STATE = 1 AND COL_NAME = ? ";
    	DataAccess access = getDataAccess(dataSourceID);
    	Map<String, Object> datas = access.queryForMap(sql, tableName.toUpperCase(), tableName.toUpperCase(),colName);
    	
    	return datas;
    }
    
    
    

}
