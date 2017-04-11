package tydic.meta.module.report;

import tydic.meta.common.MetaBaseDAO;

import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:表META_RPT_MODEL_ISSUE_COLS操作DAO
 * @date 2012-04-06
 */
public class MetaRptModelIssueConfigDAO extends MetaBaseDAO{
    /**
     * 根据模型发布ID查询发布模型信息
     * @param issueId
     * @return
     */
   public Map<String,Object> queryIssueById(long issueId){
        String sql="SELECT ISSUE_ID, TABLE_ID, TABLE_ALIAS, AUDIT_TYPE, TABLE_KEYWORD, " +
                "SUBSCRIBE_TYPE, ISSUE_STATE, ISSUE_NOTE, IS_LISTING," +
                "TO_CHAR(START_TIME,'yyyymmdd') START_TIME FROM META_RPT_MODEL_ISSUE_CONFIG WHERE ISSUE_ID=?";
       return getDataAccess().queryForMap(sql,issueId);
   }

}
