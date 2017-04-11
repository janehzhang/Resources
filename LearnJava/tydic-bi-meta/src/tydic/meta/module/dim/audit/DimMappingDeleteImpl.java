package tydic.meta.module.dim.audit;

import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.module.dim.DimConstant;
import tydic.meta.module.dim.MetaDimTabModHisDAO;
/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王晶
 * @description 维度审核中的映射删除
 * @date 2012-02-13
 */
public class DimMappingDeleteImpl implements IDimAuditType{
	
	private MappingAuditDAO  mappingAuditDAO =new MappingAuditDAO();;
	private DimAuditDAO dimAuditDAO =new DimAuditDAO();
	private MetaDimTabModHisDAO metaDimTabModHisDAO = new MetaDimTabModHisDAO();;
	/**
     * 维度映射的删除方法 1.不管删除的节点在维度表中有多少个归并类型,只要在一个归并类型下删除,那么在映射表中也会被删除
     *                  2.在末级不显示的情况下,当前维度表的数据要删除
     *                  3.在末级显示的情况下,不做操作
     * @param map<String,Object> itemId long 当前节点的id hisId 当前节点在历史表中的id
     *                           auditUserId 当前审核人的id   dimTableName 当前维度表的名称
     *                           dimTableId 当前维度表的id dimTablePrefix 当前维度表的前缀
     * @param auditFlag 审核标示 1为通过 2为不通过
     * @param modMask  审核意见
     * */
	public int dimAuditByType(Map<String, Object> dataMap, int auditUserId,int auditFlag, String modMask, Map<String, Object> extraMap)throws Exception {
		if(dataMap!=null&&dataMap.size()!=0){
			long itemId = MapUtils.getLongValue(dataMap,"itemId",0);
    		long parId =  MapUtils.getLongValue(dataMap,"itemParId",-1);
    		int  hisId =   MapUtils.getIntValue(dataMap,"hisId",0);
    		if(auditFlag==DimConstant.DIM_HAS_AUDIT_PASS){
    			String dimTableName = Convert.toString(extraMap.get("dimTableName"));
    			String dimTablePrefix = Convert.toString(extraMap.get("dimTablePrefix"));
    			String tableOwner = Convert.toString(extraMap.get("tableOwner"));
    			String itemCode = Convert.toString(dataMap.get("itemCode"));
    			String srcCode = Convert.toString(dataMap.get("srcCode"));
    			int sysId = MapUtils.getIntValue(dataMap,"srcSysId",-1);
    		    int tableId= MapUtils.getIntValue(dataMap,"dimTableId",0);
    		    int lastLevelFlag = MapUtils.getIntValue(extraMap,"lastLevelFlag",-1);
                if(sysId!=-1&&tableId!=-1&&srcCode!=null&&itemCode!=null){
                	if(lastLevelFlag==DimConstant.DIM_LAST_LEVEL_FLAG_UNDISPLAY){ //末级不显示的情况,需要删除当前维度表
        				Object params[] = new Object[3];
        				params[0] = tableId;
        				params[1] = sysId;
        				params[2] = srcCode;
        				Map itemCodeResult = mappingAuditDAO.queryMappItemCode(params);
        				String itemMappingCode = itemCodeResult.get("ITEM_CODE").toString();		//修改前维度编码
                    	//删除映射表的数据
                    	mappingAuditDAO.deleteMappingDataByCondition(itemMappingCode, srcCode, sysId, tableId);
                    	//删除维度表中的关联
                        dimAuditDAO.deleteDimData(dimTableName, dimTablePrefix,tableOwner, itemMappingCode);
                        //更新操作表
                		return metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag, modMask, hisId);
                	}
                	if(lastLevelFlag==DimConstant.DIM_LAST_LEVEL_FLAG_DISPLAY){		//末级显示情况。
                		//删除映射表的数据
                    	mappingAuditDAO.deleteMappingDataByCondition(itemCode, srcCode, sysId, tableId);
                    	//更新操作表
    		    		return metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag,modMask, hisId);
    		    	    
    		    	}
                }
    		}
    		if(auditFlag==DimConstant.DIM_HAS_AUDIT_NOT_PASS){ //不通过只是更新操作表
    			return metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag,modMask, hisId);
    		}
		}
		return 0;
	}	
}
