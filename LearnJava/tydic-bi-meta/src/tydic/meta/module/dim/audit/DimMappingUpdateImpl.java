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
 * @author 王晶
 * @description 维度批量中映射中的修改
 *               1.删除原来的映射表中的数据,在末级不显示的情况下删除维度表中的数据
 *               2.将修改后的数据写入映射表,在末级不显示的情况下写入维度表(但保证itemCode不变)
 * @date 2012-02-14
 */
public class DimMappingUpdateImpl implements IDimAuditType {

	private MappingAuditDAO  mappingAuditDAO = new MappingAuditDAO();
	private DimAuditDAO dimAuditDAO = new DimAuditDAO();
	private MetaDimTabModHisDAO metaDimTabModHisDAO = new MetaDimTabModHisDAO();
    private MetaTablesDAO metaTablesDAO = new MetaTablesDAO();
    private MetaTableColsDAO metaTableColsDAO = new MetaTableColsDAO();
    
	/**
     * 维度映射的修改方法
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
			String showItemCode = Convert.toString(dataMap.get("showItemCode"));
			String showItemName = Convert.toString(dataMap.get("showItemName"));
    		if(auditFlag==DimConstant.DIM_HAS_AUDIT_PASS){
    			String dimTableName = Convert.toString(extraMap.get("dimTableName"));
    			String dimTablePrefix = Convert.toString(extraMap.get("dimTablePrefix"));
    			String itemCode = Convert.toString(dataMap.get("itemCode"));
    			String itemName = Convert.toString(dataMap.get("itemName"));
    			String srcName = Convert.toString(dataMap.get("srcName"));
    			String srcCode = Convert.toString(dataMap.get("srcCode"));
    			int sysId = MapUtils.getIntValue(dataMap,"srcSysId",-1);
    		    int tableId= MapUtils.getIntValue(dataMap,"dimTableId",0);
    		    int typeId =MapUtils.getIntValue(dataMap,"dimTypeId",0);
    		    int lastLevelFlag = MapUtils.getIntValue(extraMap,"lastLevelFlag",-1);
    		    String tableOwner = Convert.toString(extraMap.get("tableOwner"));
    		    if(sysId!=-1&&tableId!=0&&srcCode!=null&&itemCode!=null){ //先删除映射表中的数据
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
                        dimAuditDAO.deleteDimData(dimTableName, dimTablePrefix, tableOwner,itemMappingCode);
                        
    		    		itemMappingCode = srcCode+sysId+typeId; //获取生成的新code
                        //映射表中写入新的映射数据
        		    	mappingAuditDAO.insertMappingDataByCondition(itemMappingCode, srcCode, sysId, itemName, srcName, tableId);
        		    	String itemDesc = Convert.toString(dataMap.get("itemDesc"));
    		       		int levelId = MapUtils.getIntValue(dataMap,"levelId",0);
    		       		int orderId = MapUtils.getIntValue(dataMap,"orderId",0);
    		       		long itemNewId =dimAuditDAO.queryForNextVal("SEQ_DIM_DATA_ID");

        		    	List<Map<String,Object>> nameList = getNameList(tableId);
        		    	Object[] proParams = new Object[]{itemNewId,itemId,itemMappingCode,itemCode,srcName,itemDesc,typeId,tableId,levelId,orderId};
    		       		dimAuditDAO.insertDimDataForMappAudit(dimTableName, dimTablePrefix,tableOwner, proParams, nameList);
    		       		return metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag,modMask, hisId,showItemCode,showItemName); //插入维度表之后,更新操作表
    		    	}
    		    	if(lastLevelFlag==DimConstant.DIM_LAST_LEVEL_FLAG_DISPLAY){
                		//把映射表中的维度值修改成新维度值
                    	mappingAuditDAO.updateMappingDataByCondition(itemCode, itemName, srcCode, sysId, tableId);
    		    		return metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag,modMask, hisId,showItemCode,showItemName); //插入维度表之后,更新操作表
    		    	}
    		    }
    		}
    		if(auditFlag==DimConstant.DIM_HAS_AUDIT_NOT_PASS){
    			return metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag,modMask, hisId,showItemCode,showItemName); //插入维度表之后,更新操作表
    		}
		}
		return 0;
	}
	
	/**
	 * 得到动态字段列Name
	 * @return
	 */
    public List<Map<String,Object>> getNameList(int tableId){
        int tableVersion= metaTablesDAO.queryValidVersion(tableId);
        List<Map<String, Object>> nameList = metaTableColsDAO.queryMetaTableColsByTableId(tableId,tableVersion,null);
        List<Map<String,Object>> outList = new ArrayList<Map<String,Object>>();
        for(int i=0;i<nameList.size();i++){
            if(i>11){ //过滤掉前面12个字段列
                Map<String, Object> map = nameList.get(i);
                outList.add(map);
            }
        }
        return outList;
    }
}
