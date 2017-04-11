package tydic.meta.module.mag.zone;

import java.util.List;
import java.util.Map;

import tydic.meta.common.Page;
/**
 * Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 * All rights reserved.
 * 该类是地域查询相关操作Action类，
 * @author 刘斌
 * @date 2011-9-29
 * ----------------------
 * @modify 程钰
 * 增加关联用户的方法
 * @modifyDate

 * @modify  张伟 新增方法queryZoneByPath
 * @modifyDate  2011-10-11
 *
 * @modify  王春生 新增方法queryAllZoneForBegin
 * @modifyDate  2012-3-12
 */
public class ZoneAction {
    /**
     * 数据库操作类
     */
    private ZoneDAO zoneDAO;
    
    
    
    /**
     * add yanhd 
     */
    /**
     *   装载树    
     * @param queryData
     * @return
     */
    public List<Map<String,Object>> queryZoneTree(Map<String,Object> queryData){
    	return zoneDAO.loadZoneTree(queryData);
    }
    
    
    
    
    /**
     * 加载Zone treeGrid树
     * @param queryData
     * @return
     */
    public List<Map<String,Object>> queryZoneTreeData(Map<String,Object> queryData){
        return zoneDAO.queryZone(queryData);
    }
    /**
     * 查询关联用户
     * 选中已有的关联用户
     * @param queryData 查询表单
     * @return 用户列表
     */
    public List<Map<String,Object>> queryRefUser(Map<String,Object> queryData, Page page){
        if(page == null){
            page=new Page(0,10);//每页10行
        }
        return zoneDAO.queryUserByCondition(queryData, page);
    }
    
    public void setZoneDAO(ZoneDAO zoneDAO) {
        this.zoneDAO = zoneDAO;
    }
    /**
     * 动态加载子菜单
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> querySubZone(int parentId){
        return zoneDAO.querySubZone(parentId);
    }
    public List<Map<String,Object>> querySubZoneToBuss(int parentId){
        return zoneDAO.querySubZoneToBuss(parentId);
    }
    public List<Map<String,Object>> querySubMenu(int parentId){
        return zoneDAO.querySubMenu(parentId);
    }

    /**
     * 根据起始节点到结束节点之间有路径关系节点的所有数据，而不是加载从起始节点到结束节点之间所有的节点数据
     * @author 张伟
     * @param beginId   起始节点
     * @param endId   结束节点
     * @return
     */
    public List<Map<String,Object>> queryZoneByPath(int beginId,int endId){
        return zoneDAO.queryZoneByBeginEndPath(beginId,endId);
    }
    public List<Map<String,Object>> queryZoneByPathToBuss(int beginId,int endId){
        return zoneDAO.queryZoneByBeginEndPathToBuss(beginId,endId);
    }
    public List<Map<String,Object>> queryMenuByPath(int beginId,int endId){
        return zoneDAO.queryMenuByPath(beginId,endId);
    }
    
/**yanhd start **/
    public List<Map<String,Object>> queryZoneByPathCode(String beginId,String endId){
        return zoneDAO.queryZoneByPathCode(beginId, endId);
    }
    
    public List<Map<String,Object>> queryZoneByIVRPathCode(String beginId,String endId){
        return zoneDAO.queryZoneByIVRPathCode(beginId, endId);
    }
    
    public List<Map<String,Object>> querySubZoneCode(String parentCode){
        return zoneDAO.querySubZoneCode(parentCode);
    }

    public List<Map<String,Object>> queryIVRZoneCode(String parentCode){
        return zoneDAO.queryIVRZoneCode(parentCode);
    }
    
 
/**yanhd end **/   
    //渠道三级分类树形结构
    public List<Map<String,Object>> queryChannelServById(String beginId,String endId){
        return zoneDAO.queryChannelServById(beginId, endId);
    }
    public List<Map<String,Object>> querySubChannelServId(String parentCode){
        return zoneDAO.querySubChannelServId(parentCode);
    }
    //到网格的区域树
    public List<Map<String,Object>> queryZoneByPathCodeGrid(String beginId,String endId){
        return zoneDAO.queryZoneByPathCodeGrid(beginId, endId);
    }
    public List<Map<String,Object>> querySubZoneCodeGrid(String parentCode){
        return zoneDAO.querySubZoneCodeGrid(parentCode);
    }
    public List<Map<String,Object>> querySubZoneCodeGrid_Dg(String parentCode){
        return zoneDAO.querySubZoneCodeGrid_Dg(parentCode);
    }
    
    /**
     * 营服中心区域树
     * @param parentCode
     * @return
     */
    public List<Map<String,Object>> queryProCenterCode(String parentCode){
        return zoneDAO.queryProCenterCode(parentCode);
    }
    
    public List<Map<String,Object>> queryIVRCenterCode(String parentCode){
        return zoneDAO.queryIVRCenterCode(parentCode);
    }
    
    public List<Map<String,Object>> queryZoneByPathCenterCode(String beginId,String endId){
        return zoneDAO.queryZoneByPathCenterCode(beginId, endId);
    }
    
    
    public List<Map<String,Object>> queryZoneByIVRPathCenterCode(String beginId,String endId){
        return zoneDAO.queryZoneByIVRPathCenterCode(beginId, endId);
    }
    //只到地市的区域树
 
    public List<Map<String,Object>> queryZoneByPath1(String beginId,String endId){
        return zoneDAO.queryZoneByPath(beginId, endId);
    }
    
    
    public List<Map<String,Object>> querySubZone1(String parentCode){
        return zoneDAO.querySubZone1(parentCode);
    }

    
    /**
     * 从一个起始节点开始查询。查询出所有子地域
     * @param beginId
     * @return
     * @author 王春生
     */
    public List<Map<String,Object>> queryAllZoneForBegin(int beginId){
        return zoneDAO.queryAllZoneForBegin(beginId);
    }

    /**
     * 查询地域信息
     * @param zoneId
     * @return
     * @author 王春生
     */
    public Map<String,Object> queryZoneInfo(int zoneId){
        return zoneDAO.queryZoneInfo(zoneId);
    }


}
