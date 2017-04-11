package tydic.meta.module.gdl;

import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.MetaBaseDAO;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 指标表类关系DAO
 * @date 12-6-6
 * -
 * @modify
 * @modifyDate -
 */
public class GdlTabRelDAO extends MetaBaseDAO{

    /**
     * 插入指标与表的关联数据
     * @param gdlId
     * @param tableId
     * @param colId
     * @param relLevel
     * @param relType
     */
    public void insertRel(long gdlTblRelId,int gdlId, int tableId, int colId, int relLevel, int relType){
        String sql = "INSERT INTO META_GDL_TBL_REL " +
                "  (GDL_TBL_REL_ID, TABLE_ID, GDL_ID, REL_TYPE, REL_LEVEL, COL_ID) " +
                "VALUES " +
                "  (?, ?, ?, ?, ?, ?)";
        getDataAccess().execUpdate(sql, gdlTblRelId, tableId, gdlId, relType, relLevel, colId);
    }

    /**
     * 向指标与表的关联中插入数据
     * @param data
     * @author 李国民
     * @return
     */
    public int insertGdlTbaRel(Map<String,Object> data){
        String sql = "INSERT INTO META_GDL_TBL_REL(GDL_TBL_REL_ID, TABLE_ID," +
                " GDL_ID, REL_TYPE, REL_LEVEL, COL_ID)" +
                " VALUES(SEQ_GDL_TBL_REL_ID.NEXTVAL,?,?,?,?,?)";
        return getDataAccess().execUpdate(sql,MapUtils.getString(data,"TABLE_ID"),
                MapUtils.getString(data,"GDL_ID"),MapUtils.getString(data,"REL_TYPE"),
                MapUtils.getString(data,"REL_LEVEL"),MapUtils.getString(data,"COL_ID"));
    }

    /**
     * 根据TABLEID和GDLID根据主键修改关联的COLID
     * @param colId
     * @param tableId
     * @param gdlId
     */
    public void updateByTableIdAndGdlId(int colId, int tableId, int gdlId){
        String sql = "update META_GDL_TBL_REL set COL_ID=? where TABLE_ID = ? and gdl_id = ?";
        getDataAccess().execUpdate(sql,  colId, tableId, gdlId);
    }

    /**
     * 根据表类ID和指标ID取对应的唯一一条的指标-表类关系数据
     * @param tableId
     * @param gdlId
     * @return
     */
    public Map<String, Object> queryInfoByTableIdAndGdlId(int tableId, int gdlId){
        String sql = "SELECT B.COL_NAME,B.COL_NAME_CN, A.GDL_TBL_REL_ID, A.TABLE_ID, A.GDL_ID, A.REL_TYPE, A.REL_LEVEL, " +
                " A.COL_ID FROM META_GDL_TBL_REL A " +
                " LEFT JOIN META_TABLE_COLS B ON A.COL_ID=B.COL_ID WHERE " +
                " B.COL_STATE=1 AND A.TABLE_ID="+tableId+" AND A.GDL_ID="+gdlId;
        return getDataAccess().queryForMap(sql);
    }

    /**
     * 根据指标ID和表类ID删除指标-表类关系数据
     * @param tableId
     * @param gdlId
     */
    public void deleteByTableIdAndGldId(int tableId, int gdlId){
        String sql = "DELETE FROM META_GDL_TBL_REL A WHERE A.TABLE_ID="+tableId+" AND A.GDL_ID="+gdlId;
        getDataAccess().execUpdate(sql);
    }

    /**
     * 根据指标ID和表类ID取对应的唯一主键
     * @param tableId
     * @param gdlId
     * @return
     */
    public long queryGdlTblRelIdByTableIdAndGldId(int tableId, int gdlId){
        String sql = "SELECT GDL_TBL_REL_ID FROM META_GDL_TBL_REL WHERE TABLE_ID="+tableId+" AND GDL_ID="+gdlId;
        return getDataAccess().queryForLongByNvl(sql, -999999);
    }

}
