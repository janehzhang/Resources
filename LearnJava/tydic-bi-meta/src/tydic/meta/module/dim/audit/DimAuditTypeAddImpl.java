package tydic.meta.module.dim.audit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.Constant;
import tydic.meta.module.dim.DimConstant;
import tydic.meta.module.dim.MetaDimTabModHisDAO;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 王晶
 * @description 维度审核中的归并类型的添加
 * @date 2012-02-13
 */
public class DimAuditTypeAddImpl implements IDimAuditType {
    /**
     * 维度归并类型的添加方法
     * 1.审核通过,则在维度表中添加数据,
     * 2.如果不通过,如果当前节点有子节点的状态全变为未通过并删除维度表的记录
     * 3.如果节点的父节点也是新增的,插入维度表之后,还需要更改操作表
     *
     * @param map<String,Object> itemId long 当前节点的id hisId 当前节点在历史表中的id
     * auditUserId 当前审核人的id   dimTableName 当前维度表的名称
     * dimTableId 当前维度表的id dimTablePrefix 当前维度表的前缀
     * @param auditFlag 审核标示 1为通过 2为不通过
     * @param modMask  审核意见
     */
    private DimAuditDAO dimAuditDAO = new DimAuditDAO();
    private MetaDimTabModHisDAO metaDimTabModHisDAO = new MetaDimTabModHisDAO();

    public int dimAuditByType(Map<String, Object> dataMap, int auditUserId, int auditFlag, String modMask, Map<String, Object> extraMap) throws Exception {
        if (dataMap != null && dataMap.size() != 0) {
            long itemId = MapUtils.getLongValue(dataMap, "itemId", 0);
            long parId = MapUtils.getLongValue(dataMap, "itemParId", -1);
            int hisId = MapUtils.getIntValue(dataMap, "hisId", 0);
            String dimTableName = Convert.toString(extraMap.get("dimTableName"));
            String dimTablePrefix = Convert.toString(extraMap.get("dimTablePrefix"));
            String tableOwner = Convert.toString(extraMap.get("tableOwner"));
            int typeId = MapUtils.getIntValue(dataMap, "dimTypeId", 0);
            //parCode处理
            if (StringUtils.isEmpty(MapUtils.getString(dataMap, "itemParCode"))) {
                dataMap.put("itemParCode", Constant.DEFAULT_ROOT_PARENT);
            }
            if (auditFlag == DimConstant.DIM_HAS_AUDIT_PASS) { //审核通过,写入数据
                long newItemId = dimAuditDAO.queryForNextVal("SEQ_DIM_DATA_ID"); //查询当前序列
                List<String> dymaicColNames = (List<String>) extraMap.get("dymaicColNames"); //获取动态字段
                dimAuditDAO.insertDimData(dimTableName, dimTablePrefix, tableOwner, newItemId, dataMap, dymaicColNames);
                dimAuditDAO.updateDimDataByParId(dimTableName, dimTablePrefix, tableOwner, newItemId, itemId, typeId);//插入维度表之后,更新维度表中原来parId为负数的节点
                metaDimTabModHisDAO.updateValueByParId("ITEM_PAR_ID", itemId, String.valueOf(newItemId));//更新历史表中节点为当前审核节点的子节点
                return metaDimTabModHisDAO.updateDimModHis(newItemId, parId, auditUserId, auditFlag, modMask, hisId);//更新当前节点再更新操作表
            }
            if (auditFlag == DimConstant.DIM_HAS_AUDIT_NOT_PASS) { //审核不通过的情况,更改当前节点的子节点的状态
                Map<String, Object> changeMap = new HashMap<String, Object>();
                changeMap.put("AUDIT_USER_ID", auditUserId); //把当前的节点变成审核用户变成当前用户
                changeMap.put("AUDIT_FLAG", 2);//审核状态变成不通过
                metaDimTabModHisDAO.updateBatchValueByParId(changeMap, itemId);//先更改子节点
                dimAuditDAO.deleteDimDataByParId(dimTableName, dimTablePrefix, tableOwner, itemId);//删除当前维度表中的记录
                return metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag, modMask, hisId);//更改本身节点的信息
            }
        }
        return 0;
    }
}
