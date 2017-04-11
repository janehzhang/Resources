package tydic.portal.audit;

import tydic.frame.SystemVariable;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.OprResult;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AuditDao extends MetaBaseDAO {

    private static final String sourceName	= SystemVariable.getString("db.poatal","config2");//数据源使用config2
    /**
     * 取得审核范围列表
     * @param page
     * @return
     */
    public List<Map<String, Object>> listScopes(Page page) {
        String sql = "SELECT scope_id as scopeId, bus_flag as busFlag, module_addres as moduleAddres, " +
                     "page_addres as pageAddres, min_date as minDate, " +
                     "max_date maxDate, max_effect maxEffect, aduit_flag as aduitFlag, " +
                     "audit_type as auditType, audit_note as auditNote, audit_prop as auditProp, " +
                     "effect_state as effectState FROM tb_b_data_scope";
        //分页包装
        if(page!=null){
            sql= SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess(sourceName).queryForList(sql);
    }

    /**
     * 取得审核列表
     * @param auditPO
     * @param page
     * @return
     */
    public List<Map<String, Object>> listAudits(AuditPO auditPO, Page page) {
        String sql = "SELECT a.audit_id AS auditId, a.audit_time AS auditTime," +
                     " b.bus_flag as busFlag, b.module_addres as moduleaddres, b.page_addres as pageaddres, " +
                     " b.audit_type as auditType, b.audit_note as auditNote,b.audit_prop as auditProp," +
                     "a.audit_user AS auditUser, a.audit_opinion AS auditOpinion,a.AUDIT_MARK as auditMark, " +
                     "a.audit_conclude AS auditConclude, a.audit_dept AS auditDept, " +
                     "a.data_date AS dataDate, b.scope_id AS scopeId, a.data_area " +
                     "AS dataArea, a.show_opinion AS showOpinion FROM tb_b_data_scope b left join tb_b_data_audit a on a.scope_id = b.scope_id ";
        if(auditPO.getDataDate() != null && !"".equals(auditPO.getDataDate())){
            sql = sql + " and a.data_date = '"+auditPO.getDataDate()+"' ";
        }

        sql = sql + " where b.effect_state = '11' ";
        if(auditPO.getBusFlag() != null && !"".equals(auditPO.getBusFlag())){
            sql = sql + " and b.bus_flag = '" + auditPO.getBusFlag() + "' ";
        }
        if(auditPO.getModuleAddres() != null && !"".equals(auditPO.getModuleAddres())){
            sql = sql + " and b.module_addres = '" + auditPO.getModuleAddres() + "' ";
        }
        if(auditPO.getEffectState() != null && !"".equals(auditPO.getEffectState())){
            sql = sql + " and b.effect_state = '" + auditPO.getEffectState() + "'  ";
        }
        if(auditPO.getAudittype() != null && !"".equals(auditPO.getAudittype())){
            sql = sql + " and b.audit_type = '" + auditPO.getAudittype() + "' ";
        }
        if(auditPO.getAuditConclude() != null && !"".equals(auditPO.getAuditConclude())){
            if("-1".equals(auditPO.getAuditConclude())){
                sql = sql + " and a.audit_conclude is null  ";
            }else{
                sql = sql + " and a.audit_conclude = '"+auditPO.getAuditConclude()+"' ";
            }
        }
        List<Map<String, Object>> rtn = getDataAccess(sourceName).queryForList(sql.toString());
        return rtn;
    }

    /**
     * 新的审核列表显示方式：传入的DataDate根据SCOPE_ID和MAX_DATE做修正，生成显示的新的DataDate
     * @param auditPO
     * @return
     */
    public List<Map<String, Object>> listAuditsNew(AuditPO auditPO){
        List<Map<String, Object>> scopeIdAndMaxDate = this.getMaxDateAndScopeId();
        String sql = "SELECT a.audit_id AS auditId, a.audit_time AS auditTime, b.bus_flag as busFlag, b.module_addres as moduleaddres, " +
                     "b.page_addres as pageaddres,  b.audit_type as auditType, b.audit_note as auditNote,b.audit_prop as auditProp," +
                     "a.audit_user AS auditUser, a.audit_opinion AS auditOpinion,a.AUDIT_MARK as auditMark, a.audit_conclude AS auditConclude," +
                     " a.audit_dept AS auditDept, a.data_date AS dataDate, b.scope_id AS scopeId, a.data_area AS dataArea, a.show_opinion AS showOpinion " +
                     "FROM tb_b_data_scope b left join tb_b_data_audit a ON a.scope_id = b.scope_id WHERE b.effect_state = '11'";

        sql = sql + " AND (";
        for(int i = 0; i < scopeIdAndMaxDate.size(); i ++){
            Map<String, Object> m = scopeIdAndMaxDate.get(i);
            sql = sql + " (a.data_date='"+this.changeDate(auditPO.getDataDate(), m.get("MAXDATE")==null||m.get("MAXDATE").equals("")
                    ?0:Integer.parseInt(m.get("MAXDATE").toString()))
                  + "' AND b.scope_id = '"+m.get("SCOPEID")+"') ";
            if(i != scopeIdAndMaxDate.size() - 1){
                sql = sql + " OR ";
            }
        }
        sql = sql + ")";

        if(auditPO.getBusFlag() != null && !"".equals(auditPO.getBusFlag())){
            sql = sql + " and b.bus_flag = '" + auditPO.getBusFlag() + "' ";
        }
        if(auditPO.getModuleAddres() != null && !"".equals(auditPO.getModuleAddres())){
            sql = sql + " and b.module_addres = '" + auditPO.getModuleAddres() + "' ";
        }
        if(auditPO.getEffectState() != null && !"".equals(auditPO.getEffectState())){
            sql = sql + " and b.effect_state = '" + auditPO.getEffectState() + "'  ";
        }
        if(auditPO.getAudittype() != null && !"".equals(auditPO.getAudittype())){
            sql = sql + " and b.audit_type = '" + auditPO.getAudittype() + "' ";
        }
        if(auditPO.getAuditConclude() != null && !"".equals(auditPO.getAuditConclude())){
            if("-1".equals(auditPO.getAuditConclude())){
                sql = sql + " and a.audit_conclude is null  ";
            }else{
                sql = sql + " and a.audit_conclude = '"+auditPO.getAuditConclude()+"' ";
            }
        }
        List<Map<String, Object>> rtn = getDataAccess(sourceName).queryForList(sql.toString());
        return rtn;
    }

    /**
     * 取得t.scope_id和t.max_date的对应关系
     * @return
     */
    public List<Map<String, Object>> getMaxDateAndScopeId(){
        String sql = "SELECT t.scope_id AS scopeid, t.max_date AS maxdate FROM TB_B_DATA_scope t WHERE effect_state='11'";
        return getDataAccess(sourceName).queryForList(sql);
    }

    /**
     * 取得各分公司列表
     * @return
     */
    public List<Map<String, Object>> getChildCompany(){
        String sql = "SELECT t.zone_name as name, t.zone_id as id FROM meta_dim_zone t WHERE t.zone_par_id = '1'";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 修改或新增审核信息
     * isUpdate:是否更新最大有效时间
     * @param auid
     * @param showopinion
     * @param auditconclude
     * @param userId
     * @param deptId
     * @return
     */
    public String updateAuditInfo(String auid, String auditopinion, String showopinion, String auditconclude, int userId, int deptId, String nameCn, boolean isUpdate) throws Exception{
        String s[] = auid.split(",");
        String scopeId = s[0];
        String dataDate = s[1];
        String zoneId = s[2];
        OprResult<Integer,Object> result=null;
        java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("yyyyMMdd");
        int maxDate = this.getMaxDate(scopeId);
        java.util.Calendar cal = java.util.Calendar.getInstance();

        String checkDate = sdf1.format(new Date());
        try {
            // 取得当前日期根据maxDate修正之后的日期
            cal.setTime(sdf1.parse(checkDate));
            cal.add(cal.DATE, maxDate);
            checkDate = sdf1.format(cal.getTime());
        }catch (Exception e){

        }
        if(dataDate.compareTo(checkDate) > 0){//大于修正日期的不能做操作
            return "0";
        }

        int rtn = 0;
        dataDate = dataDate.replace("-", "");
        Date date = new Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newOption = "";

        if(this.checkIsExit(scopeId, dataDate, zoneId)){
            String update = "update TB_B_DATA_AUDIT set AUDIT_OPINION=?, AUDIT_TIME=?, AUDIT_CONCLUDE=?, AUDIT_DEPT=?, SHOW_OPINION=?, AUDIT_USER=? ,AUDIT_MARK=?" +
                            "where DATA_DATE=? and SCOPE_ID=? and DATA_AREA=?";
            List params = new ArrayList();
            //审核历史记录
            String oldOption = this.getHisAuditOpinion(scopeId, dataDate, zoneId);
            if(!"".equals(oldOption)){
                if("1".equals(auditconclude)){
                    newOption = oldOption + "" + "审核人:" + nameCn+" ,审核意见: 可展现 " + auditopinion + " ,审核时间:"+sdf.format(date)+"。";
                }else{
                    newOption = oldOption + "" + "审核人:" + nameCn+" ,审核意见: 不展现 " + showopinion + " ,审核时间:"+sdf.format(date)+"。";
                }
            } else {
                if("1".equals(auditconclude)){
                    newOption = "审核人:" + nameCn+"  ,审核意见: 可展现" + auditopinion + " ,审核时间:"+sdf.format(date)+"。";
                }else{
                    newOption = "审核人:" + nameCn+"  ,审核意见: 不展现" + showopinion + " ,审核时间:"+sdf.format(date)+"。";
                }
            }
//			if(shwtdj.equals("") && newOption.length()>0){
//				newOption = newOption.substring(0 ,newOption.length() - 1);
//			}

            params.add(auditopinion);
            params.add(new Date());
            params.add(auditconclude);
            params.add(deptId);
            params.add(showopinion);
            params.add(userId);
            params.add(newOption);
            params.add(dataDate);
            params.add(scopeId);
            params.add(zoneId);
            rtn = getDataAccess(sourceName).execUpdate(update, params.toArray());
        }else{//新增
            String insert = "insert into TB_B_DATA_AUDIT values(SQE_TB_B_DATA_AUDIT.NEXTVAL,?,?,?,?,?,?,?,?,?,?)";
            List params = new ArrayList();
            params.add(new Date());
            params.add(userId);
            params.add(auditopinion);
            params.add(auditconclude);
            params.add(deptId);
            params.add(dataDate);
            params.add(scopeId);
            params.add(zoneId);
            params.add(showopinion);
            String oldOption = this.getHisAuditOpinion(scopeId, dataDate, zoneId);
//            String newOption = "";
            if(!"".equals(oldOption)){
                if("1".equals(auditconclude)){
                    newOption = oldOption + "" + "审核人:" + nameCn+" ,审核意见: 可展现 " + auditopinion + " ,审核时间:"+sdf.format(date)+"。";
                }else{
                    newOption = oldOption + "" + "审核人:" + nameCn+" ,审核意见: 不展现 " + showopinion + " ,审核时间:"+sdf.format(date)+"。";
                }
            } else {
                if("1".equals(auditconclude)){
                    newOption = "审核人:" + nameCn+" ,审核意见: 可展现 " + auditopinion + " ,审核时间:"+sdf.format(date)+"。";
                }else{
                    newOption = "审核人:" + nameCn+" ,审核意见: 不展现 " + showopinion + " ,审核时间:"+sdf.format(date)+"。";
                }
            }
            params.add(newOption);
            rtn = getDataAccess(sourceName).execUpdate(insert, params.toArray());
        }
        if(isUpdate){
            if("1".equals(auditconclude)){
                this.updateDataDateFlow(scopeId, dataDate, true);
            }else{
                this.updateDataDateFlow(scopeId, dataDate, false);
            }
        }
//        return rtn;
        if(rtn>0){
            return  newOption;
        }else {return "0";}
    }

    /**
     * 取得代码表
     * 模块应用 以及模块类型
     * @return
     */
    public List<Map<String, Object>> getCodeText(String textName){
        String sql = "SELECT i.code_code as id,i.code_name as text FROM tb_b_data_scope b,sys_code_type t ,sys_code_item i WHERE b.effect_state = '11' AND t.type_name='"+textName+"' AND t.type_id=i.type_id AND b.module_addres=i.code_code ";
        return getDataAccess(sourceName).queryForList(sql);
    }

    /**
     * 取得类型列表
     */
    public List<Map<String, Object>> getTableSet(){
        String sql = "SELECT b.module_addres AS ID,t.tab_name AS text FROM tb_b_data_scope b,DM_1_DATA_VIEW_TAB_SET t WHERE b.module_addres=t.tab_id AND t.show_flag = 1";
        return getDataAccess(sourceName).queryForList(sql);
    }

    /**
     * 取得应用列表
     */
    public List<Map<String, Object>> getTableType(){
        String sql = "SELECT DISTINCT t.table_type_id as ID, t.table_type_name as text FROM DM_1_DATA_VIEW_TAB_SET t WHERE t.show_flag = 1";
        return getDataAccess(sourceName).queryForList(sql);
    }
    /**
     * 根据类型取应用
     * @param typeId
     * @return
     */
    public List<Map<String, Object>> getTableSetByType(int typeId){
        String sql = "SELECT DISTINCT t.table_type_id as ID, t.table_type_name as text FROM DM_1_DATA_VIEW_TAB_SET t WHERE t.show_flag = 1 AND t.table_type_id = " + typeId;
        return getDataAccess(sourceName).queryForList(sql);
    }

    /**
     * 取得最大有效时间
     */
    public String getMaxEffectDate(String scopeId){
        String sql = "select max_effect from tb_b_data_scope where scope_id="+scopeId;
        return getDataAccess(sourceName).queryForString(sql);
    }

    /**
     * 取得最大可展现时间
     * @param scopeId
     * @return
     */
    public String getMaxShowDate(String scopeId){
        String sql1 = "select MAX(t1.data_date) from tb_b_data_audit t1, tb_b_data_scope t2 " +
                      "WHERE t1.scope_id=t2.scope_id AND t2.effect_state='11' AND t1.audit_conclude='1' " +
                      "AND t1.scope_id='"+scopeId+"'";
        String maxDataDate = getDataAccess(sourceName).queryForString(sql1);
        String rtn = maxDataDate;
//		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
//		String sql = "select MAX_DATE from tb_b_data_scope where scope_id = "+scopeId;
//		int mDate = Convert.ToInt(DataAccess.queryForString(sql, sourceName));
//		java.util.Calendar cal = java.util.Calendar.getInstance();
//		try {
//			// 得到日期止的第前n天
//			cal.setTime(sdf.parse(maxDataDate));
//			cal.add(cal.DATE, mDate);
//			rtn = sdf.format(cal.getTime());
//		}catch (Exception e){
//		}
        return rtn;
    }



    /**
     * 取得最大有效时间的修正值
     * @param scopeId
     * @return
     */
    public int getMaxDate(String scopeId){
        String sql = "select MAX_DATE from tb_b_data_scope where scope_id = "+scopeId;
        return getDataAccess(sourceName).queryForInt(sql);
    }



    /**
     * 更新tb_b_data_scope中的max_effect(最大有效时间)
     * @param scopeId
     * @param dataDate
     * @param b true:设置为可展现;false:设置为不展现
     */
    public void updateDataDateFlow(String scopeId, String dataDate, boolean b){
        if(b){//更新不展现到展现
            String maxShowDate = this.getMaxShowDate(scopeId);//
            String maxEffectDate = this.getMaxEffectDate(scopeId);//
            /**
             * 大于最大有效时间，小于等于最大可展现时间
             */
            if(dataDate.compareTo(maxEffectDate) > 0 && dataDate.compareTo(maxShowDate) <= 0){
                this.updateDataDate(scopeId, dataDate);
            }
        }else{
            String maxShowDate = this.getMaxShowDate(scopeId);//
            String maxEffectDate = this.getMaxEffectDate(scopeId);//
            if(maxShowDate.compareTo(maxEffectDate) < 0){
                this.updateDataDate(scopeId, maxShowDate);
            }
        }
    }

    /**
     * 更新tb_b_data_scope中的max_effect字段
     * @param scopeId
     * @param dataDate
     */
    public void updateDataDate(String scopeId, String dataDate){
        String sql = "update tb_b_data_scope set max_effect='"+dataDate+"' where scope_id="+scopeId;
//		DataAccess access = new DataAccess();
        try {
//			access.OpenConnWithContent("ORACLE", sourceName);
            getDataAccess(sourceName).execUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        finally {
//			access.Dispose();
//		}
    }


    /**
     * 检查数据是否存在
     * @param dataDate
     * @param zoneId
     * @return
     */
    private boolean checkIsExit(String scopeId, String dataDate, String zoneId){
        String sql = "select count(*) from TB_B_DATA_AUDIT where DATA_DATE=? and SCOPE_ID=? and DATA_AREA=?";
        int n = getDataAccess(sourceName).queryForInt(sql, dataDate,scopeId,zoneId);
        return n > 0;
    }

    /**
     * 取得历史审核记录
     * @param scopeId
     * @param dataDate
     * @param zoneId
     * @return
     */
    private String getHisAuditOpinion(String scopeId, String dataDate, String zoneId){
        String sql = "select AUDIT_MARK from TB_B_DATA_AUDIT where DATA_DATE=? and SCOPE_ID=? and DATA_AREA=?";
        String rtn = getDataAccess(sourceName).queryForString(sql, dataDate,scopeId,zoneId);
        return Convert.toString(rtn);
    }

    /**
     * 取得根据oldDate修正了maxDate天之后的日期
     * @param oldDate
     * @param maxDate
     * @return
     */
    private String changeDate(String oldDate, int maxDate){
        String newDate = oldDate;
        newDate = newDate.replace("-", "").replace("\\", "");
        java.text.SimpleDateFormat sdf1 = new java.text.SimpleDateFormat("yyyyMMdd");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        try {
            cal.setTime(sdf1.parse(newDate));
            cal.add(cal.DATE, maxDate);
            newDate = sdf1.format(cal.getTime());
        }catch (Exception e){
        }
        return newDate;
    }

    public static void main(String args[]){
        String s1 = "20110826";
        String s2 = "20110927";
        System.out.println(s1.compareTo(s2));
    }

}
