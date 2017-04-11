package tydic.portalCommon;

import tydic.meta.common.MetaBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 时间间隔规则定义表
 * @date 2012-04-01
 */
public class MetaPortalTimeIntervalDAO extends MetaBaseDAO{
    /**
     * 查询所有的时间间隔规则
     * @return
     */
    public List<Map<String,Object>> queryAllTimeInterval(){
        String sql="SELECT RULR_ID, RULE_NAME, RULE_DESC, RULE_TYPE, INTERVAL_VALUE FROM META_PORTAL_TIME_INTERVAL ";
        return getDataAccess().queryForList(sql);
    }

}
