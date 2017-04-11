package tydic.meta.module.tbl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.ArrayUtils;
import tydic.frame.common.utils.Convert;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.Common;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

/**
 * 
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * 
 * @author 熊小平
 * @date 2011-10-27
 * @description META_TABLE_REL操作DAO
 * 
 */
public class MetaTableRelDAO extends MetaBaseDAO {

    /**
     * 向META_TABLE_REL表中插入单条记录
     * 
     * @param data 参数， Map中Key为数据库字段名，并且为驼峰式命名，Object为该字段对应值。
     * @return 插入成功与否
     */
    public boolean insertMetaTableRel(Map<String, Object> data) {
        String sql = "INSERT INTO META_TABLE_REL("
                + "TABLE_REL_ID,TABLE_ID1_COL_IDS,TABLE_ID2_COL_IDS,TABLE_REL_DESC,TABLE_REL_TYPE, "
                + "TABLE_ID1,TABLE_ID2) "
                + "VALUES(SEQ_TAB_REL_ID.NEXTVAL,?,?,?,?,?,?)";
        Object params[] = new Object[6];
        //当传入的关联关系为3(多对一)时，将表一表二数据对调、并将关联关系设为2(一对多)之后保存
        int tableRelType = Integer.parseInt(data.get("tableRelType").toString());
        if(tableRelType!=3){
            String tableId1ColIds = Convert.toString(data.get("tableId1ColIds"));
            params[0] = tableId1ColIds;
            String tableId2ColIds = Convert.toString(data.get("tableId2ColIds"));
            params[1] = tableId2ColIds;
            String tableRelDesc = Convert.toString(data.get("tableRelDesc"));
            params[2] = tableRelDesc;
            params[3] = tableRelType;
            int tableId1 = Common.parseInt(data.get("tableId1"))==null?null:Integer.parseInt(data.get("tableId1").toString());
            params[4] = tableId1;
            int tableId2 = Common.parseInt(data.get("tableId2"))==null?null:Integer.parseInt(data.get("tableId2").toString());
            params[5] = tableId2;
        }else{
            String tableId1ColIds = Convert.toString(data.get("tableId2ColIds"));
            params[0] = tableId1ColIds;
            String tableId2ColIds = Convert.toString(data.get("tableId1ColIds"));
            params[1] = tableId2ColIds;
            String tableRelDesc = Convert.toString(data.get("tableRelDesc"));
            params[2] = tableRelDesc;
            tableRelType = 2;
            params[3] = tableRelType;
            int tableId1 = Common.parseInt(data.get("tableId1"))==null?null:Integer.parseInt(data.get("tableId1").toString());
            params[4] = tableId1;
            int tableId2 = Common.parseInt(data.get("tableId2"))==null?null:Integer.parseInt(data.get("tableId2").toString());
            params[5] = tableId2;
        }
        return getDataAccess().execNoQuerySql(sql, params);
    }

    /**
     * 判断两列关系是否已经存在
     * @param data
     * @return
     */
    public boolean checkExist(Map<String, Object> data){
        Integer tableId1 = Common.parseInt(data.get("tableId1"));
        Integer tableId2 = Common.parseInt(data.get("tableId2"));
        String tableId1ColIds = Convert.toString(data.get("tableId1ColIds"));
        String tableId2ColIds = Convert.toString(data.get("tableId2ColIds"));
        String sql = "SELECT COUNT(*) FROM meta_table_rel t WHERE " +
                "(t.table_id1 = "+tableId1+" AND t.table_id2 = "+tableId2+" AND t.table_id1_col_ids LIKE '%"+tableId1ColIds+"%' AND t.table_id2_col_ids LIKE '%"+tableId2ColIds+"%') " +
                "OR (t.table_id2 = "+tableId1+" AND t.table_id1 = "+tableId2+" AND t.table_id2_col_ids LIKE '%"+tableId1ColIds+"%' AND t.table_id1_col_ids LIKE '%"+tableId2ColIds+"%') ";
        return getDataAccess().queryForInt(sql) > 0;
    }

    /**
     * 向META_TABLE_REL表中批量插入记录
     * @param data
     * @return
     */
    public int[] insertBatch(final List<Map<String,Object>> data){
        String sql = "INSERT INTO META_TABLE_REL("
            + "TABLE_REL_ID,TABLE_ID1_COL_IDS,TABLE_ID2_COL_IDS,TABLE_REL_DESC,TABLE_REL_TYPE, "
            + "TABLE_ID1,TABLE_ID2) "
            + "VALUES(SEQ_TAB_REL_ID.NEXTVAL,?,?,?,?,?,?)";
        
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                preparedStatement.setObject(1,data.get(i).get("tableId1ColIds"));
                preparedStatement.setObject(2,data.get(i).get("tableId2ColIds"));
                preparedStatement.setObject(3,data.get(i).get("tableRelDesc"));
                preparedStatement.setObject(4,data.get(i).get("tableRelType"));
                preparedStatement.setObject(5,data.get(i).get("tableId1"));
                preparedStatement.setObject(6,data.get(i).get("tableId2"));

            }
            public int batchSize(){
                return data.size();
            }
        });
    }

    /**
     * 标间关系流程图初始化数据
     * 查询正向关联的数据
     * @return
     */
    public List<Map<String, Object>> queryInitFlowDataP(int tableId){
        String sql = "SELECT T.TABLE_ID1, T.TABLE_ID2,A.TABLE_NAME AS T1NAME,A.TABLE_NAME_CN AS T1NAMECN, " +
                "  B.TABLE_NAME AS T2NAME, B.TABLE_NAME_CN AS T2NAMECN " +
                "    FROM  META_TABLE_REL T " +
                "    LEFT JOIN META_TABLES A ON A.TABLE_ID = T.TABLE_ID1 " +
                "    AND A.Table_Version IN (" +
                "     SELECT CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END VALID_VERSION " +
                " FROM  (SELECT TABLE_ID,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0) VALID_VERSION " +
                "  FROM META_TABLES   GROUP BY TABLE_ID,TABLE_VERSION,TABLE_STATE)WHERE table_id=A.TABLE_ID GROUP BY TABLE_ID " +
                "    ) " +
                "    LEFT JOIN META_TABLES B ON B.TABLE_ID = T.TABLE_ID2 " +
                "    AND B.Table_Version IN(" +
                "     SELECT CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END VALID_VERSION " +
                " FROM  (SELECT TABLE_ID,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0) VALID_VERSION " +
                "  FROM META_TABLES   GROUP BY TABLE_ID,TABLE_VERSION,TABLE_STATE)WHERE table_id=B.TABLE_ID GROUP BY TABLE_ID " +
                "    ) " +
                "    WHERE T.TABLE_ID1="+tableId;
        List<Map<String,Object>> result = getDataAccess().queryForList(sql);
        return result;
    }

    /**
     * 标间关系流程图初始化数据
     * 查询负向关联的数据
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryInitFlowDataN(int tableId){
        String sql = "SELECT T.TABLE_ID1, T.TABLE_ID2,A.TABLE_NAME AS T1NAME,A.TABLE_NAME_CN AS T1NAMECN, " +
                "  B.TABLE_NAME AS T2NAME, B.TABLE_NAME_CN AS T2NAMECN " +
                "    FROM  META_TABLE_REL T " +
                "    LEFT JOIN META_TABLES A ON A.TABLE_ID = T.TABLE_ID1 " +
                "    AND A.Table_Version IN (" +
                "     SELECT CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END VALID_VERSION " +
                " FROM  (SELECT TABLE_ID,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0) VALID_VERSION " +
                "  FROM META_TABLES   GROUP BY TABLE_ID,TABLE_VERSION,TABLE_STATE)WHERE table_id=A.TABLE_ID GROUP BY TABLE_ID " +
                "    ) " +
                "    LEFT JOIN META_TABLES B ON B.TABLE_ID = T.TABLE_ID2 " +
                "    AND B.Table_Version IN(" +
                "     SELECT CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END VALID_VERSION " +
                " FROM  (SELECT TABLE_ID,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0) VALID_VERSION " +
                "  FROM META_TABLES   GROUP BY TABLE_ID,TABLE_VERSION,TABLE_STATE)WHERE table_id=B.TABLE_ID GROUP BY TABLE_ID " +
                "    ) " +
                "    WHERE T.TABLE_ID2="+tableId;
        List<Map<String,Object>> result = getDataAccess().queryForList(sql);
        return result;
    }


    /**
     * 根据table1ID和table2ID查询表1和表2关联关系具体信息
     * @param tbl2Id
     * @return
     */
    public List<Map<String, Object>> queryDetailsByTbl1Tb2(int tbl1Id, int tbl2Id, int tbl1Version, int tbl2Version){
        String sql = "select T.table_rel_id, T.table_id1_col_ids, T.table_id2_col_ids, T.table_rel_desc, T.table_rel_type, " +
                " T.table_id1, T.table_id2,A.TABLE_NAME AS T1NAME,A.TABLE_NAME_CN AS T1NAMECN,A.TABLE_VERSION AS T1VERSION,B.TABLE_NAME AS T2NAME, " +
                "  B.TABLE_NAME_CN AS T2NAMECN,B.TABLE_VERSION AS T2VERSION " +
                "    FROM meta_table_rel t " +
                "     LEFT JOIN META_TABLES A ON A.TABLE_ID = T.TABLE_ID1 AND A.TABLE_VERSION=" + tbl1Version +
                "      LEFT JOIN META_TABLES B ON B.TABLE_ID = T.TABLE_ID2 AND B.TABLE_VERSION=" + tbl2Version +
                "       WHERE t.table_id1="+tbl1Id+" AND t.table_id2="+tbl2Id+" ";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 根据表一表二ID删除关联关系信息
     * @param tbl1Id
     * @param tbl2Id
     * @return
     * @throws Exception
     */
    public int deleteByTbl1Tbl2(int tbl1Id, int tbl2Id) throws Exception{
        String sql="delete from meta_table_rel where table_id1="+tbl1Id+" and table_id2="+tbl2Id;
        return getDataAccess().execUpdate(sql);
    }

    /**
     * 根据表1ID删除关联信息
     * @param tbl1Id
     * @return
     * @throws Exception
     */
    public int deleteByTb1(int tbl1Id) throws Exception{
        String sql="delete from meta_table_rel where table_id1="+tbl1Id;
        return getDataAccess().execUpdate(sql);
    }

    /**
     * 根据表2ID删除关联信息
     * @param tbl2Id
     * @return
     * @throws Exception
     */
    public int deleteByTb2(int tbl2Id) throws Exception{
        String sql="delete from meta_table_rel where table_id2="+tbl2Id;
        return getDataAccess().execUpdate(sql);
    }

    /**
     * 查询表对应的所有关联信息
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryDetailByTableId(int tableId, Page page){
        String sql="SELECT T.table_rel_id, T.table_id1_col_ids, T.table_id2_col_ids, T.table_rel_desc, T.table_rel_type,  T.table_id1, " +
                " T.table_id2,A.TABLE_NAME AS T1NAME, " +
                " A.TABLE_NAME_CN AS T1NAMECN,B.TABLE_NAME AS T2NAME, " +
                " B.TABLE_NAME_CN AS T2NAMECN " +
                " FROM meta_table_rel t  LEFT JOIN META_TABLES A ON A.TABLE_ID = T.TABLE_ID1 " +
                " AND A.TABLE_VERSION IN (" +
                " SELECT CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END VALID_VERSION " +
                " FROM  (SELECT TABLE_ID,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0) VALID_VERSION " +
                "  FROM META_TABLES   GROUP BY TABLE_ID,TABLE_VERSION,TABLE_STATE)WHERE table_id=A.TABLE_ID GROUP BY TABLE_ID )  " +
                " LEFT JOIN META_TABLES B ON B.TABLE_ID = T.TABLE_ID2 " +
                " AND B.TABLE_VERSION IN (SELECT CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END VALID_VERSION " +
                " FROM  (SELECT TABLE_ID,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0) VALID_VERSION " +
                "  FROM META_TABLES   GROUP BY TABLE_ID,TABLE_VERSION,TABLE_STATE)WHERE table_id=B.TABLE_ID GROUP BY TABLE_ID ) " +
                " WHERE t.table_id1="+tableId+"   OR t.table_id2="+tableId+" ORDER BY TABLE_REL_ID ";
        if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql);
    }

    /**
     * 查询量表维度列相关信息
     * @param table1Id
     * @param table2Id
     * @return
     */
    public List<Map<String, Object>> queryForDimRel(int table1Id, int table2Id){
        String sql = "SELECT DISTINCT COL_ID, DIM_COL_ID FROM META_TABLE_COLS T WHERE COL_STATE=1 AND ((" +
                "TABLE_ID = ? AND DIM_TABLE_ID = ?) OR (TABLE_ID = ? AND DIM_TABLE_ID = ?))";
        Object params[] = new Object[4];
        params[0] = table1Id;
        params[1] = table2Id;
        params[2] = table2Id;
        params[3] = table1Id;
        return getDataAccess().queryForList(sql, params);
    }

    /**
     * 查询相关联的表类ID
     * @param tableId
     * @return
     */
    public Integer[] queryExistRelTableId(int tableId){
        String sql1 = "SELECT TABLE_ID1 FROM META_TABLE_REL WHERE TABLE_ID2="+tableId;
        String sql2 = "SELECT TABLE_ID2 FROM META_TABLE_REL WHERE TABLE_ID1="+tableId;
        List<Map<String, Object>> rtn1 = getDataAccess().queryForList(sql1);
        List<Map<String, Object>> rtn2 = getDataAccess().queryForList(sql2);
        Integer[] rtn = new Integer[rtn1.size()+rtn2.size()+1];
        rtn[0] = tableId;
        int index=1;
        for(Map<String, Object> m : rtn1){
            rtn[index] = Convert.toInt(m.get("TABLE_ID1"),0);
            index ++;
        }
        for(Map<String, Object> m : rtn2){
            rtn[index] = Convert.toInt(m.get("TABLE_ID2"),0);
            index ++;
        }
        return rtn;
    }
}
