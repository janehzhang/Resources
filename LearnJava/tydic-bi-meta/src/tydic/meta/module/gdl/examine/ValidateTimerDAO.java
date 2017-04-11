package tydic.meta.module.gdl.examine;

import tydic.frame.BaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 指标到达生效时间自动生效 <br>
 * @date 2012-6-15
 */
public class ValidateTimerDAO extends BaseDAO {

    /**
     * 找出已到规定时间内还未生效的待生效指标ID
     * @param time
     * @return
     */
    public List<Map<String, Object>> querySuitGdlIds(String time){
        String sql = "SELECT gdl_id FROM meta_gdl WHERE " +
                " valid_time < to_date('"+time+"', 'yyyy-MM-dd hh24:mi:ss') AND gdl_state=2";
        return getDataAccess().queryForList(sql);
    }

    /**
     * 对已生效的指标设置为无效
     * @param gdlId
     */
    public void invalidGdl(int gdlId){
        String sql = "UPDATE META_GDL SET GDL_STATE=0 WHERE GDL_ID="+gdlId+" AND GDL_STATE=1";
        getDataAccess().execUpdate(sql);
    }

    /**
     * 生效指标
     * @param time
     */
    public void validSuitGdls(String time){
        String sql = "update meta_gdl set gdl_state=1 WHERE " +
                " valid_time < to_date('"+time+"', 'yyyy-MM-dd hh24:mi:ss') AND gdl_state=2";
        getDataAccess().execUpdate(sql);
    }

}
