package tydic.meta.module.mag.cmplDept;

import java.util.List;
import java.util.Map;
/**
 * Copyrights @ 2013，Tianyuan DIC Information Co., Ltd.
 * All rights reserved.
 * 该类是部门查询相关操作Action类，
 * @modifyDate  2013-7-04
 */
public class CmplDeptAction {
    /**
     * 数据库操作类
     */
    private CmplDeptDAO cmplDeptDAO;
    
	public CmplDeptDAO getCmplDeptDAO() {
		return cmplDeptDAO;
	}

	public void setCmplDeptDAO(CmplDeptDAO cmplDeptDAO) {
		this.cmplDeptDAO = cmplDeptDAO;
	}

	/**
     * 动态加载子菜单
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> querySubDeptCode(String parentId){
        return cmplDeptDAO.querySubDeptCode(parentId);
    }

    /**
     * 根据起始节点到结束节点之间有路径关系节点的所有数据，而不是加载从起始节点到结束节点之间所有的节点数据
     * @author qx
     * @param beginId   起始节点
     * @param endId   结束节点
     * @return
     */
    public List<Map<String,Object>> queryDeptByPathCode(String beginId,String endId){
        return cmplDeptDAO.queryDeptByPathCode(beginId,endId);
    }
}
