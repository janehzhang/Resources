package tydic.meta.module.gdl;

import tydic.meta.common.MetaBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 指标关系条件DAO
 * @date 12-6-6
 * -
 * @modify
 * @modifyDate -
 */
public class GdlTabRelTermDAO extends MetaBaseDAO{

    /**
     * 插入指标-维度绑定信息数据
     * @param data 数据结构为
     * (
     *      gdlId:
     *      dimTypeId:
     *      gdlTblRelId:
     *      dimTableId:
     *      dimLevel:
     *      dimColId:
     *      dimCode:
     *      orderId:
     * )
     */
    public void insertGdlTblRelTerm(Map<String, Object> data){
        String sql = "INSERT INTO META_GDL_TBL_REL_TERM " +
                " (GDL_ID, DIM_TYPE_ID, GDL_TBL_REL_ID, DIM_TABLE_ID, DIM_LEVEL, " +
                "  DIM_COL_ID, DIM_CODE, ORDER_ID) " +
                " VALUES " +
                " (?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] param= {
                data.get("gdlId"),
                data.get("dimTypeId"),
                data.get("gdlTblRelId"),
                data.get("dimTableId"),
                data.get("dimLevel"),
                data.get("dimColId"),
                data.get("dimCode"),
                data.get("orderId"),
        };
        getDataAccess().execUpdate(sql, param);
    }

    /**
     * 根据指标与表关联ID删除对应的绑定维度数据
     * @param gdlTblRelId
     */
    public void deleteByGdlTblRelId(int gdlTblRelId){
        String sql = "DELETE FROM META_GDL_TBL_REL_TERM WHERE GDL_TBL_REL_ID="+gdlTblRelId;
        getDataAccess().execUpdate(sql);
    }

    /**
     * 查询从创建复合指标过来的指标绑定维度信息
     * @param gdlId
     * @return
     */
    public List<Map<String, Object>> queryBasicDetail(int gdlId){
        String sql = "SELECT GDL_ID, DIM_TYPE_ID, GDL_TBL_REL_ID, DIM_TABLE_ID, DIM_LEVEL, " +
                "DIM_COL_ID, DIM_CODE, ORDER_ID FROM META_GDL_TBL_REL_TERM WHERE GDL_ID="+gdlId+" AND GDL_TBL_REL_ID="+GdlConstant.BASE_GDL_TBL_REL_ID;
        return getDataAccess().queryForList(sql);
    }

}
