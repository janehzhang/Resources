package tydic.portalCommon;

import tydic.meta.common.MetaBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用：门户首页数据范围审核表DAO
 * @date 2012-03-30
 */
public class MetaPortalScopeDAO extends MetaBaseDAO{
    /**
     * 根据标签ID查询数据审核范围
     * @param tabId
     * @return
     */
    public List<Map<String,Object>> queryScope(int tabId){
/*        String sql="SELECT SCOPE_ID, TAB_ID, TO_CHAR(MIN_DATE,'yyyymmdd') MIN_DATENO," +
                " TO_CHAR(SYSDATE+DATE_INTERVAL,'yyyymmdd') MAX_DATENO, TO_CHAR(MAX_EFFECT_DATE,'yyyymmdd') EFFECT_DATENO, " +
                "AUDIT_TYPE, AUDIT_NOTE,MONTH_INTERVAL " +
                "FROM META_PORTAL_SCOPE WHERE TAB_ID=? ";*/
  
   /**
    yanhd  update 
    * */
   String sql="SELECT SCOPE_ID, TAB_ID, TO_CHAR(MIN_DATE,'yyyymmdd') MIN_DATENO," +
         " TO_CHAR(SYSDATE+DATE_INTERVAL,'yyyymmdd') MAX_DATENO, TO_CHAR(SYSDATE + DATE_INTERVAL, 'yyyymmdd') EFFECT_DATENO, " +
         "AUDIT_TYPE, AUDIT_NOTE,MONTH_INTERVAL " +
         "FROM META_PORTAL_SCOPE WHERE TAB_ID=? "; 	
        return getDataAccess().queryForList(sql,tabId);
    }

}
