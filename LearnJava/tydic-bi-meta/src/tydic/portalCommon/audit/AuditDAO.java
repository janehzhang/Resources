package tydic.portalCommon.audit;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.OprResult;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AuditDAO extends MetaBaseDAO {

    /**
     * 根据标签ID和数据账期获取审核数据
     *
     * @param tabId  标签页ID
     * @param dataNo  数据日期
     * @param areaCode 地域编码
     * @return
     */
    public Object[] queryAuditData(long tabId,int rptType, String dataNo, String areaCode) {
        String sql = "SELECT T.AUDIT_CONCLUDE AUDIT_STATE ,T.SHOW_OPINION MSG,T.AUDIT_OPINION EXP FROM  META_PORTAL_DATA_AUDIT T ," +
                "META_PORTAL_SCOPE M WHERE T.SCOPE_ID=M.SCOPE_ID AND M.TAB_ID=? AND T.DATA_AREA=? AND T.DATA_DATE=?";
        if(rptType==2){
        	dataNo = dataNo.substring(0,6);
        }
        Map<String, Object> re = getDataAccess().queryForMap(sql,tabId,areaCode,dataNo);
        if (re != null && re.size() > 0) {
            return new Object[]{re.get("AUDIT_STATE"), re.get("MSG"), re.get("EXP")};
        }
        // return null;
        /**
         * add yanhd
         * **/
        return new Object[]{"1", "", ""};
    }

    /**
     * 取得审核范围列表
     *
     * @param page
     * @return
     */
    public List<Map<String, Object>> listScopes(Page page) {
        String sql = "SELECT SCOPE_ID as scopeId, bus_flag as busFlag, module_addres as moduleAddres, " +
                "page_addres as pageAddres, min_date as minDate, " +
                "max_date maxDate, max_effect maxEffect, aduit_flag as aduitFlag, " +
                "audit_type as auditType, audit_note as auditNote, audit_prop as auditProp, " +
                "effect_state as effectState FROM tb_b_data_scope";
        //分页包装
        if (page != null) {
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql);
    }
    /**
     * 新的审核列表显示方式：传入的DataDate根据SCOPE_ID和MAX_DATE做修正，生成显示的新的DataDate
     *
     * @param auditPO
     * @return
     */
    public List<Map<String, Object>> listAuditsNew(AuditPO auditPO) {
        List<Map<String, Object>> scopeIdAndMaxDate = this.getMaxDateAndScopeId();
        String sql = "SELECT A.AUDIT_ID, A.AUDIT_TIME,A.SCOPE_ID,A.AUDIT_TIME, " +
                "A.AUDIT_USER,A.AUDIT_OPINION,A.AUDIT_CONCLUDE,A.DATA_DATE,A.DATA_AREA, " +
                "A.SHOW_OPINION,A.AUDIT_MARK,B.TAB_ID, B.MIN_DATE, B.DATE_INTERVAL,B.MAX_EFFECT_DATE, " +
                "B.AUDIT_TYPE, B.AUDIT_NOTE FROM META_PORTAL_SCOPE B " +
                "LEFT JOIN META_PORTAL_DATA_AUDIT A ON A.SCOPE_ID = B.SCOPE_ID WHERE B.AUDIT_TYPE = '1'";

        sql = sql + " AND (";
        for (int i = 0; i < scopeIdAndMaxDate.size(); i++) {
            Map<String, Object> m = scopeIdAndMaxDate.get(i);
            sql = sql + " (A.DATA_DATE='"+this.changeDate(auditPO.getDataDate(), m.get("MAXDATE")==null||m.get("MAXDATE").equals("")
                    ?0:Integer.parseInt(m.get("MAXDATE").toString()))
                    + "' AND B.SCOPE_ID = '"+m.get("SCOPEID")+"') ";
            if(i != scopeIdAndMaxDate.size() - 1){
                sql = sql + " OR ";
            }
        }
        sql = sql + ")";
        if(auditPO.getAuditConclude() != null && !"".equals(auditPO.getAuditConclude())){
            if("-1".equals(auditPO.getAuditConclude())){
                sql = sql + " AND A.AUDIT_CONCLUDE IS NULL  ";
            }else{
                sql = sql + " AND A.AUDIT_CONCLUDE = '"+auditPO.getAuditConclude()+"' ";
            }
        }
        return getDataAccess().queryForList(sql.toString());
    }

    /**
     * 取得t.scope_id和t.max_date的对应关系
     *
     * @return
     */
    public List<Map<String, Object>> getMaxDateAndScopeId(){
        String sql = "SELECT T.SCOPE_ID AS SCOPEID, T.DATE_INTERVAL AS MAXDATE " +
                "  FROM META_PORTAL_SCOPE  T " +
                " WHERE T.AUDIT_TYPE = '1' ";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 取得各分公司列表
     *
     * @return
     */
    public List<Map<String, Object>> getChildCompany() {
        String sql = "SELECT t.zone_name as name, t.zone_id as id FROM meta_dim_zone t WHERE t.zone_par_id = '1'";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 修改或新增审核信息
     * isUpdate:是否更新最大有效时间
     *
     * @param auid
     * @param showopinion
     * @param auditconclude
     * @param userId
     * @param
     * @return
     */
    public String updateAuditInfo(String auid, String auditopinion, String showopinion, String auditconclude, int userId, String nameCn, boolean isUpdate) throws Exception{
        String s[] = auid.split(",");
        String scopeId = s[0];
        String dataDate = s[1];
        String zoneId = s[2];
        OprResult<Integer, Object> result = null;
        java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("yyyyMMdd");
        int maxDate = this.getMaxDate(scopeId);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        String checkDate = sdf1.format(new Date());
        cal.setTime(sdf1.parse(checkDate));
        cal.add(cal.DATE, maxDate);
        checkDate = sdf1.format(cal.getTime());
        if(dataDate.compareTo(checkDate) > 0){//大于修正日期的不能做操作
            return "0";
        }
        int rtn = 0;
        dataDate = dataDate.replace("-", "");
        Date date = new Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newOption = "";

        if(this.checkIsExit(scopeId, dataDate, zoneId)){
            String update = "UPDATE META_PORTAL_DATA_AUDIT SET AUDIT_OPINION=?, AUDIT_TIME=?, AUDIT_CONCLUDE=?, SHOW_OPINION=?, AUDIT_USER=? ,AUDIT_MARK=?" +
                    "where DATA_DATE=? and SCOPE_ID=? and DATA_AREA=?";
            List params = new ArrayList();
            //审核历史记录
            String oldOption = this.getHisAuditOpinion(scopeId, dataDate, zoneId);
            if (!"".equals(oldOption)) {
                if ("1".equals(auditconclude)) {
                    newOption = oldOption + "" + "审核人:" + nameCn + " ,审核意见: 可展现 " + auditopinion + " ,审核时间:" + sdf.format(date) + "。";
                } else {
                    newOption = oldOption + "" + "审核人:" + nameCn + " ,审核意见: 不展现 " + showopinion + " ,审核时间:" + sdf.format(date) + "。";
                }
            } else {
                if ("1".equals(auditconclude)) {
                    newOption = "审核人:" + nameCn + "  ,审核意见: 可展现" + auditopinion + " ,审核时间:" + sdf.format(date) + "。";
                } else {
                    newOption = "审核人:" + nameCn + "  ,审核意见: 不展现" + showopinion + " ,审核时间:" + sdf.format(date) + "。";
                }
            }
//			if(shwtdj.equals("") && newOption.length()>0){
//				newOption = newOption.substring(0 ,newOption.length() - 1);
//			}

            params.add(auditopinion);
            params.add(new Date());
            params.add(auditconclude);
            params.add(showopinion);
            params.add(userId);
            params.add(newOption);
            params.add(dataDate);
            params.add(scopeId);
            params.add(zoneId);
            rtn = getDataAccess().execUpdate(update, params.toArray());
        }
        if (isUpdate) {
            if ("1".equals(auditconclude)) {
                this.updateDataDateFlow(scopeId, dataDate, true);
            } else {
                this.updateDataDateFlow(scopeId, dataDate, false);
            }
        }
//        return rtn;
        if (rtn > 0) {
            return newOption;
        } else {
            return "0";
        }
    }

    /**
     * 取得即指需要审核的菜单 列表
     */
    public List<Map<String, Object>> getTableSet(){
        String sql = "SELECT TAB_ID AS ID, TAB_NAME AS TEXT FROM META_PORTAL_TAB ";
        return getDataAccess().queryForList(sql);
    }
    /**
     * 取得最大有效时间
     */
    public String getMaxEffectDate(String scopeId){
        String sql = "SELECT TO_CHAR(T.MAX_EFFECT_DATE,'YYYYMMDD') FROM META_PORTAL_SCOPE T WHERE T.SCOPE_ID=? ";
        return getDataAccess().queryForString(sql,scopeId);
    }

    /**
     * 取得最大可展现时间
     *
     * @param scopeId
     * @return
     */
    public String getMaxShowDate(String scopeId){
        String sql1 = "SELECT MAX(T1.DATA_DATE) FROM META_PORTAL_DATA_AUDIT T1 WHERE T1.AUDIT_CONCLUDE='1' " +
                "AND T1.SCOPE_ID= ? ";
        return getDataAccess().queryForString(sql1,scopeId);
    }


    /**
     * 取得最大有效时间的修正值
     *
     * @param scopeId
     * @return
     */
    public int getMaxDate(String scopeId){
        String sql = "SELECT DATE_INTERVAL FROM META_PORTAL_SCOPE WHERE SCOPE_ID = "+scopeId;
        return getDataAccess().queryForInt(sql);
    }


    /**
     * 更新tb_b_data_scope中的max_effect(最大有效时间)
     *
     * @param scopeId
     * @param dataDate
     * @param b        true:设置为可展现;false:设置为不展现
     */
    public void updateDataDateFlow(String scopeId, String dataDate, boolean b){
        if(b){//更新展现到展现
            String maxShowDate = this.getMaxShowDate(scopeId);//
            String maxEffectDate = this.getMaxEffectDate(scopeId);//
            /**
             * 大于最大有效时间，小于等于最大可展现时间
             */
            if (dataDate.compareTo(maxEffectDate) > 0 && dataDate.compareTo(maxShowDate) <= 0) {
                this.updateDataDate(scopeId, dataDate);
            }
        } else {
            String maxShowDate = this.getMaxShowDate(scopeId);//
            String maxEffectDate = this.getMaxEffectDate(scopeId);//
            if (maxShowDate.compareTo(maxEffectDate) < 0) {
                this.updateDataDate(scopeId, maxShowDate);
            }
        }
    }

    /**
     * 更新tb_b_data_scope中的max_effect字段
     *
     * @param scopeId
     * @param dataDate
     */
    public void updateDataDate(String scopeId, String dataDate){
        String sql = "UPDATE META_PORTAL_SCOPE SET MAX_EFFECT_DATE=TO_DATE(?,'YYYY-MM-DD') WHERE SCOPE_ID=? ";
        getDataAccess().execUpdate(sql,dataDate,scopeId);
    }


    /**
     * 检查数据是否存在
     *
     * @param dataDate
     * @param zoneId
     * @return
     */
    private boolean checkIsExit(String scopeId, String dataDate, String zoneId) {
        String sql = "SELECT COUNT(*) FROM META_PORTAL_DATA_AUDIT WHERE DATA_DATE=? AND SCOPE_ID=? AND DATA_AREA=?";
        int n = getDataAccess().queryForInt(sql, dataDate,scopeId,zoneId);
        return n > 0;
    }

    /**
     * 取得历史审核记录
     *
     * @param scopeId
     * @param dataDate
     * @param zoneId
     * @return
     */
    private String getHisAuditOpinion(String scopeId, String dataDate, String zoneId) {
        String sql = "SELECT AUDIT_MARK FROM META_PORTAL_DATA_AUDIT WHERE DATA_DATE=? AND SCOPE_ID=? AND DATA_AREA=?";
        String rtn = getDataAccess().queryForString(sql, dataDate,scopeId,zoneId);
        return Convert.toString(rtn);
    }

    /**
     * 取得根据oldDate修正了maxDate天之后的日期
     *
     * @param oldDate
     * @param maxDate
     * @return
     */
    private String changeDate(String oldDate, int maxDate) {
        String newDate = oldDate;
        newDate = newDate.replace("-", "").replace("\\", "");
        java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("yyyyMMdd");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        try {
            cal.setTime(sdf1.parse(newDate));
            cal.add(cal.DATE, maxDate);
            newDate = sdf1.format(cal.getTime());
        } catch (Exception e) {
        }
        return newDate;
    }
}
