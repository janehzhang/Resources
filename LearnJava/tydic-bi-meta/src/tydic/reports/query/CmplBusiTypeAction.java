package tydic.reports.query;

import java.util.List;
import java.util.Map;
/**
 by qx
 * @modifyDate  2013-4-15
 */
public class CmplBusiTypeAction {
    /**
     * 数据库操作类
     */
    private CmplBusiTypeDAO cmplBusiTypeDAO;    


	public void setCmplBusiTypeDAO(CmplBusiTypeDAO cmplBusiTypeDAO) {
		this.cmplBusiTypeDAO = cmplBusiTypeDAO;
	}

	/**
     * 动态加载子菜单
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> querySubCmplBusiType(String parentId){
        return cmplBusiTypeDAO.querySubCmplBusiType(parentId);
    }

    /**
     * 根据起始节点到结束节点之间有路径关系节点的所有数据，而不是加载从起始节点到结束节点之间所有的节点数据
     * @author qx
     * @param beginId   起始节点
     * @param endId   结束节点
     * @return
     */
    public List<Map<String,Object>> queryCmplBusiTypeByPath(String  beginId,String endId){
        return cmplBusiTypeDAO.queryCmplBusiTypeByBeginEndPath(beginId,endId);
    }
}
