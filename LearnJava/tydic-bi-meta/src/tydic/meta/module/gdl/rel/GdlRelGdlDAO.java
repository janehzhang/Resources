package tydic.meta.module.gdl.rel;

import tydic.meta.common.MetaBaseDAO;
import tydic.meta.module.gdl.GdlConstant;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 指标和指标关系DAO <br>
 * @date 2012-6-13
 */
public class GdlRelGdlDAO extends MetaBaseDAO {

    /**
     * 查询一个指标的所有子指标
     * @param gdlId
     * @return
     */
    public List<Map<String, Object>> queryChildGdls(int gdlId){
        String sql = "SELECT A.GDL_ID,A.PAR_GDL_ID,B.GDL_NAME,B.GDL_BUS_DESC,B.GDL_TYPE,B.GDL_VERSION,B.GDL_CODE, " +
                "LEVEL LV FROM META_GDL_REL A  " +
                "LEFT JOIN META_GDL B ON A.GDL_ID = B.GDL_ID WHERE B.GDL_STATE=1 " +
                "CONNECT BY PRIOR A.GDL_ID = A.PAR_GDL_ID START WITH A.PAR_GDL_ID="+gdlId;
        return getDataAccess().queryForList(sql);
    }

    /**
     * 查询一个指标的所有父指标
     * @param gdlId
     * @return
     */
    public List<Map<String, Object>> queryFatherGdls(int gdlId){
        String sql = "SELECT A.GDL_ID,A.PAR_GDL_ID,B.GDL_NAME,B.GDL_BUS_DESC,B.GDL_TYPE,B.GDL_VERSION,B.GDL_CODE, " +
                "LEVEL LV FROM META_GDL_REL A " +
                "LEFT JOIN META_GDL B ON A.PAR_GDL_ID = B.GDL_ID WHERE B.GDL_STATE=1 " +
                "CONNECT BY PRIOR A.PAR_GDL_ID = A.GDL_ID START WITH A.GDL_ID="+gdlId;
        return getDataAccess().queryForList(sql);
    }

    /**
     * 查询两个维度的绑定维度差集（两维度为父子关系）
     * @param parGdlId
     * @param chiGdlId
     * @return
     */
    public List<Map<String, Object>> queryDiffBandDim(int parGdlId, int chiGdlId){
        String sql = "SELECT A.GDL_ID, A.DIM_TYPE_ID, A.GDL_TBL_REL_ID, A.DIM_TABLE_ID, A.DIM_LEVEL, " +
                " A.DIM_COL_ID, A.DIM_CODE, A.ORDER_ID, MT.TABLE_NAME, MT.TABLE_NAME_CN, " +
                " MT.DATA_SOURCE_ID, MT.TABLE_OWNER FROM META_GDL_TBL_REL_TERM A" +
                " LEFT JOIN META_TABLES MT ON A.DIM_TABLE_ID=MT.TABLE_ID " +
                " WHERE MT.TABLE_STATE=1 " +
                " AND A.GDL_TBL_REL_ID="+ GdlConstant.BASE_GDL_TBL_REL_ID+" " +
                " AND A.GDL_ID = " + chiGdlId +
                " AND NOT EXISTS (SELECT 1 FROM META_GDL_TBL_REL_TERM B WHERE B.GDL_TBL_REL_ID="+ GdlConstant.BASE_GDL_TBL_REL_ID+" " +
                " AND B.GDL_ID = "+ parGdlId +" AND A.DIM_TYPE_ID = B.DIM_TYPE_ID AND A.DIM_TABLE_ID = B.DIM_TABLE_ID " +
                " AND A.DIM_LEVEL = B.DIM_LEVEL AND A.DIM_COL_ID = B.DIM_COL_ID AND A.DIM_CODE = B.DIM_CODE )";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 查询两指标绑定维度差的个数
     * @param parGdlId
     * @param chiGdlId
     * @return
     */
    public int queryDiffBandDimNum(int parGdlId, int chiGdlId){
        String sql = "SELECT COUNT(1) FROM META_GDL_TBL_REL_TERM A WHERE A.GDL_TBL_REL_ID="+ GdlConstant.BASE_GDL_TBL_REL_ID+" " +
                " AND A.GDL_ID = " + chiGdlId +
                " AND NOT EXISTS (SELECT 1 FROM META_GDL_TBL_REL_TERM B WHERE B.GDL_TBL_REL_ID="+ GdlConstant.BASE_GDL_TBL_REL_ID+" " +
                " AND B.GDL_ID = "+ parGdlId +" AND A.DIM_TYPE_ID = B.DIM_TYPE_ID AND A.DIM_TABLE_ID = B.DIM_TABLE_ID " +
                " AND A.DIM_LEVEL = B.DIM_LEVEL AND A.DIM_COL_ID = B.DIM_COL_ID AND A.DIM_CODE = B.DIM_CODE )";
        return getDataAccess().queryForIntByNvl(sql, 0);
    }

}
