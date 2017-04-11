package tydic.meta.module.dim.audit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.module.dim.DimConstant;
import tydic.meta.module.dim.MetaDimTabModHisDAO;
import tydic.meta.module.tbl.MetaTableColsDAO;
import tydic.meta.module.tbl.MetaTablesDAO;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 王晶
 * @description 维度审核中的映射添加
 * @date 2012-02-13
 */
public class DimMappingAddIpml implements IDimAuditType {
    private MappingAuditDAO mappingAuditDAO = new MappingAuditDAO();
    private DimAuditDAO dimAuditDAO = new DimAuditDAO();
    private MetaDimTabModHisDAO metaDimTabModHisDAO = new MetaDimTabModHisDAO();
    private MetaTablesDAO metaTablesDAO = new MetaTablesDAO();
    private MetaTableColsDAO metaTableColsDAO = new MetaTableColsDAO();

    /**
     * 维度映射的添加方法,在末级是否显示处理方法不一致,在末级不显示的时候,需要往当前维度表中写入当前信息,写入维度表的时候,code的生成方式为:srcCode+sysId
     * 在显示的情况下不需要写数据
     * 在映射表中,已经有了该数据的映射,那么不需要在往映射表中写输入,如果已经存在不需要
     *
     * @param map<String,Object> itemId long 当前节点的id hisId 当前节点在历史表中的id
     *                           auditUserId 当前审核人的id   dimTableName 当前维度表的名称
     *                           dimTableId 当前维度表的id dimTablePrefix 当前维度表的前缀
     * @param auditFlag          审核标示 1为通过 2为不通过
     * @param modMask            审核意见
     */
    public int dimAuditByType(Map<String, Object> dataMap, int auditUserId, int auditFlag, String modMask, Map<String, Object> extraMap) throws Exception {
        if (dataMap != null && dataMap.size() != 0) {
            long itemId = MapUtils.getLongValue(dataMap, "itemId", 0);
            long parId = MapUtils.getLongValue(dataMap, "itemParId", -1);
            int hisId = MapUtils.getIntValue(dataMap, "hisId", 0);
            if (auditFlag == DimConstant.DIM_HAS_AUDIT_PASS) {
                String dimTableName = Convert.toString(extraMap.get("dimTableName"));
                String dimTablePrefix = Convert.toString(extraMap.get("dimTablePrefix"));
                String tableOwner = Convert.toString(extraMap.get("tableOwner"));
                String itemCode = Convert.toString(dataMap.get("itemCode"));
                String srcCode = Convert.toString(dataMap.get("srcCode"));
                int sysId = MapUtils.getIntValue(dataMap, "srcSysId", -1);
                int tableId = MapUtils.getIntValue(dataMap, "dimTableId", 0);
                int typeId = MapUtils.getIntValue(dataMap, "dimTypeId", 0);
                int lastLevelFlag = MapUtils.getIntValue(extraMap, "lastLevelFlag", -1);
                String itemName = Convert.toString(dataMap.get("itemName"));
                String srcName = Convert.toString(dataMap.get("srcName"));
                if (sysId != -1 && tableId != -1 && srcCode != null && itemCode != null) { //查找当前节点在映射表中是否存在,不存在先插入映射表中
                    int mappingCount = mappingAuditDAO.queryMappingDataByCondition(sysId, srcCode, tableId, itemCode);
                    if (lastLevelFlag == DimConstant.DIM_LAST_LEVEL_FLAG_UNDISPLAY) { //末级不显示的情况,需要写入当前维度表
                        String itemDesc = Convert.toString(dataMap.get("itemDesc"));
                        int levelId = MapUtils.getIntValue(dataMap, "dimLevel", 0);
                        int orderId = MapUtils.getIntValue(dataMap, "orderId", 0);
                        long itemNewId = dimAuditDAO.queryForNextVal("SEQ_DIM_DATA_ID");
                        String itemMappingCode = srcCode + sysId + typeId;            //末级不显示时，添加新code
                        if (mappingCount == 0) { //映射表不存在的情况,写入映射表
                            mappingAuditDAO.insertMappingDataByCondition(itemMappingCode, srcCode, sysId, itemName, srcName, tableId);
                        }
                        List<Map<String, Object>> nameList = getNameList(tableId);
                        Object[] proParams = new Object[]{itemNewId, itemId, itemMappingCode, itemCode, srcName, itemDesc, typeId, tableId, levelId, orderId};
                        dimAuditDAO.insertDimDataForMappAudit(dimTableName, dimTablePrefix, tableOwner, proParams, nameList);    //新增关联维度数据
                        return metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag, modMask, hisId); //插入维度表之后,更新操作表
                    }
                    if (lastLevelFlag == DimConstant.DIM_LAST_LEVEL_FLAG_DISPLAY) {
                        if (mappingCount == 0) { //映射表不存在的情况,写入映射表
                            mappingAuditDAO.insertMappingDataByCondition(itemCode, srcCode, sysId, itemName, srcName, tableId);
                        }
                        return metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag, modMask, hisId);
                    }
                }
            }
            if (auditFlag == DimConstant.DIM_HAS_AUDIT_NOT_PASS) { //不通过只是更新操作表
                return metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag, modMask, hisId);
            }
        }
        return 0;
    }

    /**
     * 得到动态字段列Name
     *
     * @return
     */
    public List<Map<String, Object>> getNameList(int tableId) {
        int tableVersion = metaTablesDAO.queryValidVersion(tableId);
        List<Map<String, Object>> nameList = metaTableColsDAO.queryMetaTableColsByTableId(tableId, tableVersion, null);
        List<Map<String, Object>> outList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < nameList.size(); i++) {
            if (i > 11) { //过滤掉前面12个字段列
                Map<String, Object> map = nameList.get(i);
                outList.add(map);
            }
        }
        return outList;
    }
}
