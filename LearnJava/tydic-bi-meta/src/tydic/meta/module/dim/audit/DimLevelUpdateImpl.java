package tydic.meta.module.dim.audit;

import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.module.dim.DimConstant;
import tydic.meta.module.dim.MetaDimTabModHisDAO;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王晶
 * @description 维度批量中维度层级的修改
 * @date 2012-02-10
 */
public class DimLevelUpdateImpl implements IDimAuditType{

    private DimAuditDAO dimAuditDAO= new DimAuditDAO();;
    private MetaDimTabModHisDAO metaDimTabModHisDAO = new MetaDimTabModHisDAO();
    /**
     * 维度层级的修改实现方法
     * @param map<String,Object> itemId long 当前节点的id hisId 当前节点在历史表中的id
     *                           auditUserId 当前审核人的id   dimTableName 当前维度表的名称
     *                           dimTableId 当前维度表的id dimTablePrefix 当前维度表的前缀
     *                           更变的字段
     * @param auditFlag 审核标示 1为通过 2为不通过
     * @param modMask  审核意见
     * */
    public int dimAuditByType(Map<String, Object> dataMap, int auditUserId,int auditFlag, String modMask, Map<String, Object> extraMap)throws Exception {
        long itemId = dataMap.get("itemId")==null?0:Long.parseLong(dataMap.get("itemId").toString());
        long parId =  dataMap.get("itemParId")==null?-1:Long.parseLong(dataMap.get("itemParId").toString());
        int  hisId =dataMap.get("hisId")==null?0:Integer.parseInt(dataMap.get("hisId").toString());
        String parCode = dataMap.get("itemParCode")==null?null:dataMap.get("itemParCode").toString();
        String dimTableName = Convert.toString(extraMap.get("dimTableName"));
        String dimTablePrefix = Convert.toString(extraMap.get("dimTablePrefix"));
        String tableOwner = Convert.toString(extraMap.get("tableOwner"));
        List dymaicColNames = (List) extraMap.get("dymaicColNames");
        dimAuditDAO.insertHisDataAfterAudit(MapUtils.getString(extraMap, "batchId"), MapUtils.getLongValue(extraMap,"dimTableId")
                ,itemId, dimTableName,dimTablePrefix,tableOwner,dymaicColNames);
        if(auditFlag==DimConstant.DIM_HAS_AUDIT_PASS){ //1.通过的情况 更改维度表中该条记录 2 更新操作表中的状态
            if(itemId!=0&&dimTableName!=null&&dimTablePrefix!=null){ //确保当前维度记录在本身的维度中是存在的
                dimAuditDAO.updateDimBaseRecord(dataMap, dimTableName,tableOwner,dimTablePrefix);
                dimAuditDAO.updateParCodeByItemId(dimTableName, dimTablePrefix, tableOwner, parCode, itemId);
                metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag, modMask, hisId);
            }
        }
        if(auditFlag==DimConstant.DIM_HAS_AUDIT_NOT_PASS){ //不通过的情况只是更新操作表的状态
            metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag, modMask, hisId);
        }
        return 0;
    }
}
