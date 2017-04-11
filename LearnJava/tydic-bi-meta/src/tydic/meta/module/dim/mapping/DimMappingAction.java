package tydic.meta.module.dim.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;
import tydic.meta.module.dim.MetaDimTabIntRelDAO;
import tydic.meta.module.dim.MetaDimTabModHisDAO;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 维度编码映射Action <br>
 * @date 2011-11-16
 */
public class DimMappingAction {

    private DimMappingDAO dimMappingDAO;

    private MetaDimTabIntRelDAO metaDimTabIntRelDAO;

    protected MetaDimTabModHisDAO metaDimTabModHisDAO;
    
    final int maxDimCount = 1000;		//允许一次查询时的归并类型数据最大数

    /**
     * 查询归并类型下的数据
     * @param tableName
     * @param dimTablePrefix
     * @param tableUser
     * @param dimTypeId
     * @param lastLevelFalg
     * @return
     */
    public List<Map<String, Object>> queryDimTreeData(String tableName, String dimTablePrefix, String tableUser, int dimTypeId, int lastLevelFalg) {
    	int dimCount = dimMappingDAO.queryDimDataCount(tableName, dimTablePrefix, tableUser, dimTypeId, lastLevelFalg);
		if(dimCount>maxDimCount){	//当数据量大于允许最大数据量时，异步加载数据
	    	return dimMappingDAO.getDimTreeData(tableName, dimTablePrefix, tableUser, dimTypeId, lastLevelFalg, 0);
		}else{
	    	return dimMappingDAO.queryDimTreeData(tableName, dimTablePrefix, tableUser, dimTypeId, lastLevelFalg);
		}
    }

    /**
     * 异步加载查询维度数据
     * @param data
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryDimTreeByNode(Map<String, Object> data, Map<String,Object> map) {
    	String tableName = MapUtils.getString(data, "tableName");
    	String dimTablePrefix = MapUtils.getString(data, "tableDimPrefix");
    	String tableUser = MapUtils.getString(data, "tableUser");
    	int dimTypeId = MapUtils.getIntValue(data, "dimType");
    	int lastLevelFalg = MapUtils.getIntValue(data, "lastLevelFalg");
    	long parId = MapUtils.getLongValue(map, "id");
    	return dimMappingDAO.getDimTreeData(tableName, dimTablePrefix, tableUser, dimTypeId, lastLevelFalg, parId);
    }
    
    /**
     * 查询已映射数，未映射数，未审核数
     * @param data
     * @return
     */
    public Map<String,Object> queryMappingCount(Map<String, Object> data){
    	int mappCount = dimMappingDAO.queryMappCount(data, true);		//已映射数
    	int noMappCount = dimMappingDAO.queryMappCount(data, false);	//未映射数
    	int noAuditCount = dimMappingDAO.queryNoAuditCount(data);		//未审核数
    	String tableName = MapUtils.getString(data, "tableName");
    	String dimTablePrefix = MapUtils.getString(data, "tableDimPrefix");
    	String tableUser = MapUtils.getString(data, "tableOwner");
    	int dimTypeId = MapUtils.getIntValue(data, "dimType");
    	int lastLevelFalg = MapUtils.getIntValue(data, "lastLevelFalg");
    	int dimCount = dimMappingDAO.queryDimDataCount(tableName, dimTablePrefix, tableUser, dimTypeId, lastLevelFalg);
    	Map<String, Object> rs = new HashMap<String, Object>();
    	rs.put("mappCount", mappCount);
    	rs.put("noMappCount", noMappCount);
    	rs.put("noAuditCount", noAuditCount);
    	rs.put("dimCount", dimCount);
    	return rs;
    }

    /**
     * 查询映射数据
     *
     * @param dimTableInfo     维度表定义信息，包含键值dimTableId,tableName,tableDimPrefix
     * @param areaIntTableName 维度接口表表名
     * @param queryMessage     查询信息，包含键值 keyWord:关键字，dimTypeId:归并类型ID，system:系统ID。 mappingStatus：映射状态
     * @return
     */
    public List<Map<String, Object>> queryDimMapping(Map<String, Object> queryMessage, Map<String, Object> dimTableInfo, Page page) {
        if (page == null) {
            page = new Page(0, 20);
        }
        int mappingStatus = Integer.parseInt(queryMessage.get("mappingStatus").toString());
        switch (mappingStatus) {
            case 0: //查询所有
                return dimMappingDAO.queryDimMapping(dimTableInfo, queryMessage, true, page, 1);
            case 1://查询未映射
                return dimMappingDAO.queryDimMapping(dimTableInfo, queryMessage, false, page, 0);
            case 2://查询未审核的数据
                return dimMappingDAO.queryDimMappingByHis(dimTableInfo, queryMessage, page);
            case 3://查询映射已审核的数据
                return dimMappingDAO.queryDimMapping(dimTableInfo, queryMessage, true, page, 0);
        }
        return null;
    }

    /**
     * 映射提交审核。
     *
     * @param auditData 审核数据，数据结构如下
     *                  {
     *                  dimInfo:{dimTableId,tableName,tablePrefixName...},维表信息
     *                  cancelNotAuditData：[1,2,3],尚未审核的历史表数据ID
     *                  cancelAuditData:[{SRC_CODE,SRC_SYS_ID,SRC_NAME,ITEM_CODE,ITEM_NAME,ITEM_ID}]，未通过审核要删除的数据,未审核
     *                  dymaicCloCount:1,动态字段数目
     *                  insertData:[{SRC_CODE,SRC_SYS_ID,SRC_NAME,ITEM_CODE,ITEM_NAME,ITEM_ID}] 新增的申请
     *                  updateData：[{SRC_CODE,SRC_SYS_ID,SRC_NAME,ITEM_CODE,ITEM_NAME,ITEM_ID}] 修改的申请
     *                  }
     * @return
     */
    public boolean sumbitAudit(Map<String, Object> auditData) {
        //维表信息
        @SuppressWarnings("unchecked")
        Map<String, Object> dimInfo = (Map<String, Object>) auditData.get("dimInfo");
        String tableName = dimInfo.get("tableName").toString();
        String tablePrefixName = dimInfo.get("tableDimPrefix").toString();
        int dimTableId = Integer.parseInt(dimInfo.get("dimTableId").toString());
        int sysId = Integer.parseInt(auditData.get("sysId").toString());
        int dymaicCloCount = Integer.parseInt(auditData.get("dymaicCloCount").toString());
        int mappCount = dimMappingDAO.queryUnAuditedCount(dimTableId, sysId);
        if (mappCount > 0) {
            return false;
        } else {
            try {
                BaseDAO.beginTransaction();
                @SuppressWarnings("unchecked")
                List<Long> cancelNotAuditData = (List<Long>) auditData.get("cancelNotAuditData");
                if (cancelNotAuditData != null && cancelNotAuditData.size() > 0) {
                    metaDimTabModHisDAO.delelteByBatch(cancelNotAuditData);
                }
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> cancelAuditData = (List<Map<String, Object>>) auditData.get("cancelAuditData");
                if (cancelAuditData != null && cancelAuditData.size() > 0) {
                    metaDimTabModHisDAO.insertBatch(cancelAuditData, dymaicCloCount, dimTableId);
                }
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> insertData = (List<Map<String, Object>>) auditData.get("insertData");
                if (insertData != null && insertData.size() > 0) {
                    metaDimTabModHisDAO.insertBatch(insertData, dymaicCloCount, dimTableId);
                }
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> updateData = (List<Map<String, Object>>) auditData.get("updateData");
                if (updateData != null && updateData.size() > 0) {
                    metaDimTabModHisDAO.insertBatch(updateData, dymaicCloCount, dimTableId);
                }
                BaseDAO.commit();
            } catch (Exception e) {
                BaseDAO.rollback();
                return false;
            }
        }
        return true;
    }

    /**
     * 查询注册tableId的系统信息。
     *
     * @return
     */
    public List<Map<String, Object>> systemOption(int tableId) {
        return metaDimTabIntRelDAO.querySystem(tableId);
    }

    /**
     * 对于选择的维度，是否所有的已通过审核
     *
     * @param dimTableId
     * @return 返回true表示所有已经通过审核，false表示有未通过审核
     */
    public boolean isAllAudit(int dimTableId, int dimTypeId) {
        return dimMappingDAO.queryUnAuditedCount(dimTableId, dimTypeId) == 0;
    }

    /**
     * 选择的当前维度表中,那些映射数据是在正在被审核的
     *
     * @return 正在被审核的数据 MAP<String,Object> key:节点的itemId itemCode srcCode
     * @prama tableId 当前维度表的id
     * @prama sysId 当前源系统的id
     */
    public List<Map<String, Object>> queryMappAuditData(int tableId, int sysId) {
        return dimMappingDAO.queryMappAuditData(tableId, sysId);
    }

    public void setDimMappingDAO(DimMappingDAO dimMappingDAO) {
        this.dimMappingDAO = dimMappingDAO;
    }

    public void setMetaDimTabIntRelDAO(MetaDimTabIntRelDAO metaDimTabIntRelDAO) {
        this.metaDimTabIntRelDAO = metaDimTabIntRelDAO;
    }

    public void setMetaDimTabModHisDAO(MetaDimTabModHisDAO metaDimTabModHisDAO) {
        this.metaDimTabModHisDAO = metaDimTabModHisDAO;
    }
}
