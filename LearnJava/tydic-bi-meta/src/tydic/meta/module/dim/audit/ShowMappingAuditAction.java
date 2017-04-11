package tydic.meta.module.dim.audit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;
import tydic.meta.module.dim.DimConstant;
import tydic.meta.module.mag.user.UserDAO;

/**
*
* @author 李国民
* @date 2012-02-15
* @description 编码映射审核结果查看
*
*/
public class ShowMappingAuditAction{
	
	ShowMappingAuditDAO showMappingAuditDAO;
	UserDAO userDao;

	/**
	 * 通过条件查询编码映射审核结果
	 * @param queryMessage 批次号、维度表id等条件
	 * @return 未审核的编码映射变更数据
	 */
	public List<Map<String,Object>> queryMapping(Map<String,String> queryMessage, Page page){
		if(page==null){
			page= new Page(0,20);
		}
		int dimTableId = Integer.parseInt(queryMessage.get("dimTableId").toString());	//维度表id
		String batchId = queryMessage.get("batchId");									//批次号
		//查询出未审核的编码映射变更数据
		List<Map<String,Object>> list = showMappingAuditDAO.queryMapping(batchId,dimTableId,page);
		List<Map<String,Object>> outList = new ArrayList<Map<String,Object>>();
		for(int i=0;i<list.size();i++){		//对查询出的数据进行遍历过滤
			Map<String, Object> map = list.get(i);
			//通过userId得到用户信息
			Map<String, Object> userMap = userDao.queryUserByUserId(MapUtils.getIntValue(map,"AUDIT_USER_ID",0));
			if(userMap != null){
				map.put("AUDIT_USER", userMap.get("USER_NAMECN"));	//审核人名称
			}
			int auditFlag = MapUtils.getIntValue(map,"AUDIT_FLAG",0);	//审核结果标记
			String auditDesc = "";	//审核结果
			if(auditFlag == DimConstant.DIM_HAS_AUDIT_PASS){
				auditDesc = "审核通过";
			}else if(auditFlag == DimConstant.DIM_HAS_AUDIT_NOT_PASS){
				auditDesc = "审核驳回";
			}
			map.put("AUDIT_DESC", auditDesc);
			if(Integer.parseInt(map.get("MOD_FLAG").toString()) == DimConstant.DIM_MAPP_DELETE){//删除操作。
				map.put("DELETE_FLAG", "1");		//标记删除
			}else if(Integer.parseInt(map.get("MOD_FLAG").toString()) == DimConstant.DIM_MAPP_UPDATE){//修改操作。
				map.put("DELETE_FLAG", "0");		//标记不删除
			}else{ 		//以上两种情况都不成立，则为新增操作
				map.put("DELETE_FLAG", "0");		//标记不删除
			}
			outList.add(map);
		}
		return outList;
	}

	public void setShowMappingAuditDAO(ShowMappingAuditDAO showMappingAuditDAO) {
		this.showMappingAuditDAO = showMappingAuditDAO;
	}

		public void setUserDao(UserDAO userDao) {
		this.userDao = userDao;
	}
	
}
