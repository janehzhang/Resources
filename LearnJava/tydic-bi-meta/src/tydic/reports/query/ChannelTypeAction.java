package tydic.reports.query;

import java.util.List;
import java.util.Map;
/**
 by qx
 * @modifyDate  2013-5-9
 */
public class ChannelTypeAction {
    /**
     * 数据库操作类
     */
    private ChannelTypeDAO channelTypeDAO;    

	public ChannelTypeDAO getChannelTypeDAO() {
		return channelTypeDAO;
	}

	public void setChannelTypeDAO(ChannelTypeDAO channelTypeDAO) {
		this.channelTypeDAO = channelTypeDAO;
	}

	/**
     * 动态加载子菜单
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> querySubChannelType(String parentId){
        return channelTypeDAO.querySubChannelType(parentId);
    }
    public List<Map<String,Object>> querySubChannelType_new(String parentId){
        return channelTypeDAO.querySubChannelType_new(parentId);
    }

    /**
     * 根据起始节点到结束节点之间有路径关系节点的所有数据，而不是加载从起始节点到结束节点之间所有的节点数据
     * @author qx
     * @param beginId   起始节点
     * @param endId   结束节点
     * @return
     */
    public List<Map<String,Object>> queryChannelTypeByPath(String beginId,String endId){
        return channelTypeDAO.queryChannelTypeByBeginEndPath(beginId,endId);
    }
	/**
     * 动态加载子菜单--去掉某些渠道
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> querySubChannelTypePart(String parentId){
        return channelTypeDAO.querySubChannelTypePart(parentId);
    }
    
    public List<Map<String,Object>> querySubChannelTypePart_new(String parentId){
        return channelTypeDAO.querySubChannelTypePart_new(parentId);
    }
    //全渠道服务区域_渠道类型树
    public List<Map<String,Object>> querySubChannelTypeZone(String parentId){
        return channelTypeDAO.querySubChannelTypeZone(parentId);
    }
    public List<Map<String,Object>> querySubChannel(String parentId){
        return channelTypeDAO.querySubChannel(parentId);
    }
    /**
     * 根据起始节点到结束节点之间有路径关系节点的所有数据，而不是加载从起始节点到结束节点之间所有的节点数据--去掉某些渠道
     * @author qx
     * @param beginId   起始节点
     * @param endId   结束节点
     * @return
     */
    public List<Map<String,Object>> queryChannelTypeByPathPart(String beginId,String endId){
        return channelTypeDAO.queryChannelTypeByBeginEndPathPart(beginId,endId);
    }
    public List<Map<String,Object>> queryChannelType(String beginId,String endId){
        return channelTypeDAO.queryChannelType(beginId,endId);
    }
     /***
      * 查询 渠道类型“其他”
      * @param parentCode
      * @return
      */
    public List<Map<String,Object>> querySubChannelTypePartOther_new(String parentCode){
    	
    	return channelTypeDAO.querySubChannelTypePartOther_new(parentCode);
    }
     /**
      * 查询 产品类型展现
      * @return
      */
    public List<Map<String,Object>> terminalPathPart(){
    	
    	
        return channelTypeDAO.terminalPath();
    }
}
