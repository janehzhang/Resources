package tydic.meta.module.dim.audit;

import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王晶
 * @description 具体维度审核的接口
 * @date 2011-11-16
 */
public interface IDimAuditType {
	/**
	 * 具体审核的执行方法
	 * @param dataMap 包含当前操作表中当前记录所有的记录数据
	 * @param auditUserId 当前审核人的Id
	 * @param auditFlag 审核的标示  1为审核通过,2为审核不通过
	 * @param auditMask 审核的意见
	 * @param extraMap Map 包含了审核中需要的其他信息
	 * */
	public int dimAuditByType(Map<String,Object>dataMap,int auditUserId, int auditFlag,String modMask,Map<String,Object>extraMap)throws Exception;

}
