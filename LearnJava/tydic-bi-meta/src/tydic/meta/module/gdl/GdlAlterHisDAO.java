package tydic.meta.module.gdl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.web.session.SessionManager;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 指标变动历史表DAO
 * @date 12-6-6
 * -
 * @modify
 * @modifyDate -
 */
public class GdlAlterHisDAO extends MetaBaseDAO{

	/**
     * 向用户指标关系表中添加一条记录
     * @param data
     * @return
	 * @author 李国民
     */
    public int insertGdlAlertHistory(Map<String,Object> data){
        String sql = "INSERT INTO META_GDL_ALTER_HISTORY(GDL_ALTER_HISTOTY_ID, " +
        		" GDL_ID, ALTER_TYPE, AUDIT_STATE, GDL_VERSION)" +
                " VALUES(SEQ_GDL_ALTER_HISTOTY_ID.NEXTVAL,?,?,?,?)";
        return getDataAccess().execUpdate(sql,MapUtils.getString(data,"GDL_ID"),
        		MapUtils.getString(data,"ALTER_TYPE"),MapUtils.getString(data,"AUDIT_STATE"),
        		MapUtils.getString(data,"GDL_VERSION"));
    }

    /**
     * 插入指标历史操作记录
     * @param his
     * @return
     */
    public int insertGdlAlterHis(Map<String,Object> his){
        String sql = "INSERT INTO META_GDL_ALTER_HISTORY(GDL_ALTER_HISTOTY_ID, " +
                " GDL_ID, ALTER_TYPE,USER_ZONE_ID,USER_DEPT_ID,USER_STATION_ID," +
                "AUDIT_USER_ID,AUDIT_STATE,AUDIT_TIME,AUDIT_OPINION,GDL_VERSION," +
                "ALTER_VERSION) " +
                "VALUES(SEQ_GDL_ALTER_HISTOTY_ID.NEXTVAL," +
                "?,?,?,?,?," +
                "?,?,to_date(?,'yyyy-mm-dd hh24:mi:ss'),?,?,?)";
        List<Object> param = new ArrayList<Object>();
        param.add(his.get("GDL_ID"));
        param.add(his.get("ALTER_TYPE"));
        param.add(SessionManager.getCurrentZoneID());
        param.add(SessionManager.getCurrentDeptID());
        param.add(SessionManager.getCurrentStationID());
        param.add(SessionManager.getCurrentUserID());
        param.add(his.get("AUDIT_STATE"));
        param.add(DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss"));
        param.add(his.get("AUDIT_OPINION"));
        param.add(his.get("GDL_VERSION"));
        param.add(his.get("ALTER_VERSION"));
        return getDataAccess().execUpdate(sql,param.toArray());
    }
}
