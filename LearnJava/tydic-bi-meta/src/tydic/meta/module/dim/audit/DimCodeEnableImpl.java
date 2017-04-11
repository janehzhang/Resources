package tydic.meta.module.dim.audit;

import java.util.HashMap;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.module.dim.DimConstant;
import tydic.meta.module.dim.MetaDimTabModHisDAO;
/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王晶
 * @description 维度审核中启动的操作
 * @date 2012-02-13
 */
public class DimCodeEnableImpl implements IDimAuditType{

	private DimAuditDAO dimAuditDAO= new DimAuditDAO();
	private MetaDimTabModHisDAO metaDimTabModHisDAO = new MetaDimTabModHisDAO();
	/**
	 * 维度审核中启用code的操作  1 审核通过,启动code,将停用自身(有父节点,会作为多条数据传入)
	 *                        2.审核不通过,那么他以前通过的子节点将整个变为不通过
	 *                        3.本身维度表中的数据(子节点数据)会变成无效
	 * */
	public int dimAuditByType(Map<String, Object> dataMap, int auditUserId,int auditFlag, String modMask, Map<String, Object> extraMap)throws Exception {
		 if(dataMap!=null&&dataMap.size()!=0){
	    		long itemId = MapUtils.getLongValue(dataMap,"itemId",0);
	    		long parId =  MapUtils.getLongValue(dataMap,"itemParId",-1);
	    		int  hisId =   MapUtils.getIntValue(dataMap,"hisId",0);
	    		String dimTableName = Convert.toString(extraMap.get("dimTableName"));
				String dimTablePrefix = Convert.toString(extraMap.get("dimTablePrefix"));
				String tableOwner = Convert.toString(extraMap.get("tableOwner"));
			    if(auditFlag==DimConstant.DIM_HAS_AUDIT_PASS){
			    	dimAuditDAO.updateDimStateByItemId(dimTableName,dimTablePrefix,tableOwner,itemId,DimConstant.DIM_HAS_AUDIT_PASS); //更新维度表的状态
			    	return  metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag, modMask, hisId); //更新操作表的状态
			    }
			    if(auditFlag==DimConstant.DIM_HAS_AUDIT_NOT_PASS){
			    	dimAuditDAO.updateDimStateByParId(dimTableName, dimTablePrefix,tableOwner,itemId,0);//自身节点与子节点变成无效并删除映射表中的数据
			    	/*List<Map<String,Object>> list  = dimAuditDAO.queryDimCodeByParId(dimTableName, dimTablePrefix, itemId);
			    	if(list!=null&&list.size()!=0){
			    		for(int i = 0 ;i<list.size();i++){
			    		   Map<String,Object> codeMap = list.get(i);
			    		   itemCodeList.add(Convert.toString(codeMap.get(dimTablePrefix+"_CODE")));
			    		}
			    		mappingAuditDAO.deleteMappingDataByCondition(itemCodeList, tableId);
			    	}*/
                    Map<String,Object> changeMap = new HashMap<String,Object>();
    		    	changeMap.put("AUDIT_USER_ID",auditUserId); //把当前的节点变成审核用户变成当前用户
    		    	changeMap.put("AUDIT_FLAG", DimConstant.DIM_HAS_AUDIT_NOT_PASS);//审核状态变成不通过
//    		    	metaDimTabModHisDAO.updateBatchValueByParId(changeMap,itemId);//先更改子节点
			    	return  metaDimTabModHisDAO.updateDimModHis(itemId, parId, auditUserId, auditFlag, modMask, hisId); //更新操作表的状态
			    }
	     }
		return 0;
	}
}
