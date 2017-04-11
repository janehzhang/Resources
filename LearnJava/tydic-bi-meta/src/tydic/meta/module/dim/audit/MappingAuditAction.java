package tydic.meta.module.dim.audit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.meta.common.Page;
import tydic.meta.module.dim.DimConstant;
import tydic.meta.web.session.SessionManager;

/**
*
* @author 李国民
* @date 2012-02-08
* @description 审核编码映射Action
*
*/
public class MappingAuditAction{
	
	MappingAuditDAO mappingAuditDAO;

	/**
	 * 通过条件查询未审核的编码映射变更数据
	 * @param queryMessage 批次号、维度表id等条件
	 * @return 未审核的编码映射变更数据
	 */
	public List<Map<String,Object>> queryMappingAudit(Map<String,String> queryMessage,Page page){
		if(page==null){
			page= new Page(0,20);
		}
		int dimTableId = Integer.parseInt(queryMessage.get("dimTableId").toString());	//维度表id
		String batchId = queryMessage.get("batchId");							//批次号
		String tableName = queryMessage.get("tableName");						//维度表名称
		String tableOwner = queryMessage.get("tableOwner");
		String tableDimPrefix = queryMessage.get("tableDimPrefix");				//前缀
		//查询出未审核的编码映射变更数据
		List<Map<String,Object>> list = mappingAuditDAO.queryMappingAudit(batchId,dimTableId,page);
		List<Map<String,Object>> outList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<list.size();i++){		//对查询出的数据进行遍历过滤
			Map<String, Object> map = list.get(i);
			if(Integer.parseInt(map.get("MOD_FLAG").toString()) == DimConstant.DIM_MAPP_DELETE){//删除操作。
				map.put("DELETE_FLAG", "1");		//标记删除
			}else if(Integer.parseInt(map.get("MOD_FLAG").toString()) == DimConstant.DIM_MAPP_UPDATE){//修改操作。
				map.put("DELETE_FLAG", "0");		//标记不删除
	    		int lastLevelFlag = getLastLevelFlag(dimTableId);		//是否末级显示标记
				Object params[] = new Object[3];
				params[0] = dimTableId;
				params[1] = map.get("SRC_SYS_ID");
				params[2] = map.get("SRC_CODE");
				Map itemCodeResult = mappingAuditDAO.queryMappItemCode(params);
				//映射表中对应的原映射维度code
				String mappItemCode = itemCodeResult.get("ITEM_CODE").toString();		//修改前维度编码
				String mappItemName = itemCodeResult.get("ITEM_NAME").toString();		//修改前维度编码名称
				if(lastLevelFlag==0){
					Map outResult = mappingAuditDAO.queryItemCode(tableName,tableDimPrefix,tableOwner,mappItemCode);
					if(outResult!=null){
						mappItemCode = outResult.get("ITEM_CODE").toString();
						mappItemName = outResult.get("ITEM_NAME").toString();
					}
				}
				map.put("SHOW_ITEM_CODE", mappItemCode+" -&gt; "+map.get("ITEM_CODE").toString());
				map.put("SHOW_ITEM_NAME", mappItemName+" -&gt; "+map.get("ITEM_NAME").toString());
			}else{ 		//以上两种情况都不成立，则为新增操作
				map.put("DELETE_FLAG", "0");		//标记不删除
			}
			outList.add(map);
		}
		return outList;
	}
	
	/**
	 * 进行映射审核、驳回
	 * @param data
	 * @return
	 */
	public boolean commitMappingAudit(Map<String,Object> data){
		boolean check = false;
        Integer userId = SessionManager.getCurrentUserID();
        if(userId != null){
            Map<String,Object> tableData =(Map<String,Object>)data.get("tableData");
    		int dimTableId = Integer.parseInt(tableData.get("dimTableId").toString());	//维度表id
    		int lastLevelFlag = getLastLevelFlag(dimTableId);		//是否末级显示标记
    		tableData.put("lastLevelFlag", lastLevelFlag);
            List<Map<String,Object>> columnDatas = (List<Map<String,Object>>)data.get("columnDatas");
            check = DimAudit.dimAudit(columnDatas, userId.intValue(), Integer.parseInt(tableData.get("auditFlag").toString()), 
            		tableData.get("modMask").toString(), tableData);
        }
        return check;
	}
	
	/**
	 * 通过维度表id查询是否末级显示
	 * @param dimTableId 维度表id
	 * @return 是否末级显示标记
	 */
	private int getLastLevelFlag(int dimTableId){
		Map flaResult = mappingAuditDAO.queryLastLevelFlag(dimTableId);
		int lastLevelFlag = Integer.parseInt(flaResult.get("LAST_LEVEL_FLAG").toString());
		return lastLevelFlag;
	}
	
	public void setMappingAuditDAO(MappingAuditDAO mappingAuditDAO) {
		this.mappingAuditDAO = mappingAuditDAO;
	}
}
