package tydic.meta.module.gdl.rel;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 指标关系DAO <br>
 * @date 2012-6-5
 */
public class GdlRelDAO extends MetaBaseDAO {

    /**
     * 查找与指标有关系的表类
     * @param gdlId
     * @return
     */
    public List<Map<String, Object>> queryRelTable(int gdlId){
        String sql = "SELECT a.gdl_tbl_rel_id, a.table_id, a.gdl_id, a.rel_type, a.rel_level, " +
                "a.col_id,b.table_name,b.table_name_cn,c.col_name FROM meta_gdl_tbl_rel a " +
                "LEFT JOIN meta_tables b ON a.table_id=b.table_id AND b.table_state=1 " +
                "LEFT JOIN meta_table_cols c on a.col_id = c.col_id and c.col_state=1 " +
                "WHERE a.gdl_id="+gdlId;
        return getDataAccess().queryForList(sql);
    }

    /**
     * 根据维度列条件筛选
     * 查询备选能被关联的表类
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryLeftTable(Map<String,Object> queryData,Page page){
        List<Object> param = new ArrayList<Object>();
        String sql = "SELECT DISTINCT A.TABLE_ID,A.TABLE_NAME,a.table_name_cn, " +
                "CASE WHEN B.TABLE_ID  IN (SELECT table_id FROM META_GDL_TBL_REL WHERE gdl_id="+Convert.toString(queryData.get("gdlId"))+")  THEN 1 ELSE 0 END RELATED " +
                "FROM   META_TABLES A " +
                "LEFT   JOIN META_GDL_TBL_REL B " +
                "ON     A.TABLE_ID = B.TABLE_ID " +
                "WHERE A.TABLE_STATE = 1 AND EXISTS " +
                " (SELECT 1 " +
                " FROM   META_TABLE_COLS B1 " +
                " WHERE  A.TABLE_ID = B1.TABLE_ID " +
                " AND    B1.COL_STATE = 1 " +
                " AND    EXISTS (SELECT 1 " +
                " FROM   META_GDL_DIM_GROUP_METHOD C " +
                " WHERE  B1.DIM_TABLE_ID = C.DIM_TABLE_ID " +
                " AND c.gdl_id = "+Convert.toString(queryData.get("gdlId"))+")) ";
        if(!Convert.toString(queryData.get("keyWord")).equals("")){
            sql += " AND (UPPER(A.TABLE_NAME) LIKE UPPER(?) ESCAPE '/' OR UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) ESCAPE '/') ";
            param.add(SqlUtils.allLikeBindParam(Convert.toString(queryData.get("keyWord"))));
            param.add(SqlUtils.allLikeBindParam(Convert.toString(queryData.get("keyWord"))));
        }
        if(!Convert.toString(queryData.get("tableTypeId")).equals("")){
            sql += " AND A.TABLE_TYPE_ID="+Convert.toString(queryData.get("tableTypeId"));
        }
        sql += " ORDER BY RELATED DESC, A.TABLE_NAME ";
        //分页包装
        if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql, param.toArray());
    }

    /**
     * 根据表类ID查询该表指标列信息
     * @param tableId
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryGdlColByTableId(int tableId, Page page){
        String sql = "SELECT a.col_id,a.col_name,a.col_name_cn," ;
        sql+="(CASE ";
        sql+="WHEN a.COL_DATATYPE = 'DATE' THEN ";
        sql+="'DATE' ";
        sql+="WHEN a.COL_SIZE > 0 AND a.COL_PREC > 0 THEN ";
        sql+="a.COL_DATATYPE || '(' || a.COL_SIZE || ',' || a.COL_PREC || ')' ";
        sql+="WHEN a.COL_SIZE > 0 THEN ";
        sql+="a.COL_DATATYPE || '(' || a.COL_SIZE || ')' ";
        sql+="ELSE ";
        sql+="a.COL_DATATYPE ";
        sql+="END) COL_DATATYPE ";
        sql += " FROM meta_table_cols a " +
                "WHERE a.col_state = 1 AND a.table_id=" + tableId + " AND a.col_bus_type=1 order by a.col_id";
        //分页包装
        if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql);
    }

    /**
     * 查询该指标所有子指标ID
     * @param parGdlId
     * @return
     */
    public List<Map<String, Object>> queryAllChildGdlIds(int parGdlId){
        String sql = "select distinct a.gdl_id,level lv from meta_gdl_rel a CONNECT BY PRIOR a.GDL_ID = a.PAR_GDL_ID " +
                "START WITH a.par_gdl_id="+parGdlId;
        return getDataAccess().queryForList(sql);
    }

    /**
     * 判断一个复合指标的绑定维度是否是一个宽表支撑维度的子集（并且该复合指标不是计算指标）
     * @param gdlId
     * @param tableId
     * @return
     */
    public boolean checkBindDim(int gdlId, int tableId){
        String sql = "SELECT COUNT(1) FROM META_GDL_TBL_REL_TERM B " +
                " INNER JOIN META_GDL GD ON B.GDL_ID=GD.GDL_ID AND GD.GDL_TYPE <> 2 " +
                " WHERE B.GDL_ID="+gdlId+" AND EXISTS " +
                " (SELECT 1 FROM META_TABLE_COLS C WHERE C.COL_STATE = 1 AND C.TABLE_ID = "+tableId+" " +
                "AND B.DIM_TYPE_ID = C.DIM_TYPE_ID AND B.DIM_TABLE_ID=C.DIM_TABLE_ID AND B.DIM_COL_ID=C.DIM_COL_ID)";
        return getDataAccess().queryForIntByNvl(sql, 0)>0;
    }

    /**
     * 根据宽表的维度列和指标的支撑维度查询该宽表对该指标的支撑维度
     * @param gdlId
     * @param tableId
     * @param page
     * @return
     */
    public List<Map<String, Object>> querySupportDimsByTableId(int gdlId,int tableId, Page page){
        String supportSql = "SELECT DISTINCT " +
                "B.DIM_TABLE_ID, " +
                "B.GROUP_METHOD, " +
                "(SELECT TABLE_NAME_CN FROM META_TABLES WHERE TABLE_ID = B.DIM_TABLE_ID " +
                "     AND TABLE_STATE=1) DIM_NAME_CN " +
                "  FROM META_GDL A, META_GDL_DIM_GROUP_METHOD B, META_TABLE_COLS C " +
                " WHERE B.GDL_ID = A.GDL_ID AND C.TABLE_ID="+ tableId +
                " AND C.COL_STATE = 1 AND C.DIM_TABLE_ID=B.DIM_TABLE_ID" +
                "  AND B.GDL_VERSION = A.GDL_VERSION " +
                "   AND A.GDL_STATE =1 " +
                "   AND B.GDL_ID = "+gdlId;
        if(page!=null){
            supportSql = supportSql + " ORDER BY B.DIM_TABLE_ID ";
            supportSql = SqlUtils.wrapPagingSql(supportSql,page);
        }
        return getDataAccess().queryForList(supportSql);
    }

    /**
     * 根据宽表的维度列和指标的绑定维度查询该宽表对该指标的绑定维度
     * @param gdlId
     * @param tableId
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryGdlBindDimsByTableId(int gdlId,int tableId,Page page){
        String sql = "SELECT DISTINCT A.GDL_ID,A.DIM_TYPE_ID,A.GDL_TBL_REL_ID,A.DIM_TABLE_ID,A.DIM_LEVEL, " +
                " A.DIM_COL_ID,A.DIM_CODE,A.ORDER_ID,C.DIM_TYPE_NAME,D.TABLE_NAME_CN DIM_NAME_CN " +
                "FROM   META_GDL_TBL_REL_TERM A, META_TABLE_COLS B, META_DIM_TYPE C, META_TABLES D " +
                "WHERE  GDL_TBL_REL_ID = 0 " +
                "AND    B.TABLE_ID = "+ tableId +
                "AND    B.COL_STATE = 1 " +
                "AND    A.DIM_TABLE_ID = B.DIM_TABLE_ID " +
                "AND    A.DIM_TYPE_ID = B.DIM_TYPE_ID " +
                "AND    A.DIM_TYPE_ID = C.DIM_TYPE_ID " +
                "AND    D.TABLE_ID=A.DIM_TABLE_ID " +
                "AND    D.TABLE_STATE=1 " +
                "AND    A.GDL_ID = "+gdlId;
        if(page!=null){
            sql = sql + " ORDER BY A.GDL_ID,A.DIM_TABLE_ID,A.DIM_TYPE_ID ";
            sql = SqlUtils.wrapPagingSql(sql,page);
        }
        return getDataAccess().queryForList(sql);
    }

}
