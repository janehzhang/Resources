package tydic.meta.module.report;

import tydic.meta.common.MetaBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:META_RPT_MODEL_ISSUE_COLS操作DAO
 * @date 2012-04-06
 */
public class MetaRptModelIssueColsDAO extends MetaBaseDAO{
    /**
     * 查询模型ID下所有发布的列信息
     * @param issueId  发布模型ID
     * @return
     */
    public List<Map<String,Object>> queryIssueCols(long issueId){
        String sql="SELECT A.COLUMN_ID, A.COL_ID, A.COL_ALIAS, A.COL_BUS_TYPE, A.DIM_LEVELS, A.DIM_CODES, A.SELECTED_FLAG," +
                " A.COL_TYPE_ID, A.AMOUNT_FLAG, A.ISSUE_ID, A.DIM_TYPE, A.DIM_TABLE_ID, A.COL_NAME,B.COL_BUS_COMMENT," +
                "A.DIM_TYPE_ID FROM META_RPT_MODEL_ISSUE_COLS A" +
                " LEFT JOIN META_TABLE_COLS B ON A.COL_ID=B.COL_ID"+
                " WHERE ISSUE_ID=?";
        return getDataAccess().queryForList(sql,issueId);
    }

    /**
     * 查询对应列支撑的数据层次 
     * @param columnId
     * @return
     */
    public String queryDimLevels(long columnId){
        String sql="SELECT DIM_LEVELS FROM META_RPT_MODEL_ISSUE_COLS WHERE COLUMN_ID=?";
        return getDataAccess().queryForString(sql,columnId);
    }
}
