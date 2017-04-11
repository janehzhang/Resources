package tydic.meta.module.dim.audit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.module.dim.DimConstant;
import tydic.meta.module.dim.MetaDimTabModHisDAO;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王晶
 * @description 维度审核中的code的停用操作
 * @date 2012-02-13
 */
public class DimCodeChangeStateImpl implements IDimAuditType{

	private DimAuditDAO dimAuditDAO= new DimAuditDAO();;
	private MetaDimTabModHisDAO metaDimTabModHisDAO = new MetaDimTabModHisDAO();
	private MappingAuditDAO mappingAuditDAO = new MappingAuditDAO();

	/**
	 * 维度审核中停用code的操作  1 审核通过,停用code,将停用自身(有子节点,会作为多条数据传入),并删除映射表中的数据
	 *                        2.审核不通过,那么他以前通过的子节点将整个变为不通过
	 *                        3.本身维度表中的数据(子节点数据)会变成有效
	 * */
	public int dimAuditByType(Map<String, Object> dataMap, int auditUserId,int auditFlag, String modMask, Map<String, Object> extraMap)throws Exception {
		 if(dataMap!=null&&dataMap.size()!=0){
			    List<String> itemCodeList = new ArrayList<String>();
	    		long itemId = MapUtils.getLongValue(dataMap,"itemId",0);
	    		long parId =  MapUtils.getLongValue(dataMap,"itemParId",-1);
	    		int  hisId =   MapUtils.getIntValue(dataMap,"hisId",0);
	    		String dimTableName = Convert.toString(extraMap.get("dimTableName"));
				String dimTablePrefix = Convert.toString(extraMap.get("dimTablePrefix"));
				String tableOwner = Convert.toString(extraMap.get("tableOwner"));
				int tableId = MapUtils.getIntValue(extraMap,"dimTableId",-1);
				String itemCode = Convert.toString(dataMap.get("itemCode"));
			    if(auditFlag==DimConstant.DIM_HAS_AUDIT_PASS){
			    	dimAuditDAO.updateDimStateByItemId(dimTableName,dimTablePrefix,tableOwner,itemId,0); //更新维度表的状态
			    	itemCodeList.add(itemCode);
			    	mappingAuditDAO.deleteMappingDataByCondition(itemCodeList,tableId); //删除映射表的数据
			    	return  metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag, modMask, hisId); //更新操作表的状态
			    }
			    if(auditFlag==DimConstant.DIM_HAS_AUDIT_NOT_PASS){
                    Map<String,Object> changeMap = new HashMap<String,Object>();
    		    	changeMap.put("AUDIT_USER_ID",auditUserId); //把当前的节点变成审核用户变成当前用户
    		    	changeMap.put("AUDIT_FLAG", DimConstant.DIM_HAS_AUDIT_NOT_PASS);//审核状态变成不通过
			    	return  metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag, modMask, hisId); //更新操作表的状态
			    }
	     }
		return 0;
	}
}
