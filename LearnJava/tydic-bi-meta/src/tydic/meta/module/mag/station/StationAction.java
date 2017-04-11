package tydic.meta.module.mag.station;

import java.util.List;
import java.util.Map;

import tydic.meta.common.Page;
/**
 * Copyrights @ 2011，Tianyuan DIC Information Co., Ltd.
 * All rights reserved.
 * 该类是岗位查询相关操作Action类，
 * @author 刘斌
 * @date 2011-9-29
 * ----------------------
 *
 * @modify  张伟 新增方法queryStationByPath
 * @modifyDate  2011-10-11
 *
 * @modify 程钰
 * 添加关联用户方法
 * 查询方法没有写完，请先不走读
 * @modifyDate
 *
 * @modify  王春生 新增方法queryAllStationForBegin
 * @modifyDate  2012-3-12
 */
public class StationAction {
    /**
     * 数据库操作类
     */
    private StationDAO stationDAO;
    /**
     * 加载Station treeGrid树
     * @param queryData
     * @return
     */
    public List<Map<String,Object>> queryStationTreeData(Map<String, Object> queryData){
        return stationDAO.queryStationTree(queryData);
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
        return stationDAO.queryUserByCondition(queryData, page);
    }

    /**
     * 根据起始节点到结束节点之间有路径关系节点的所有数据，而不是加载从起始节点到结束节点之间所有的节点数据
     * @author 张伟
     * @param beginId   起始节点
     * @param endId   结束节点
     * @return
     */
    public List<Map<String,Object>> queryStationByPath(int beginId,int endId){
        return stationDAO.queryStationByBeginEndPath(beginId,endId);
    }

    /**
     * 从一个起始节点开始查询。查询出所有子岗位
     * @param beginId
     * @return
     * @author 王春生
     */
    public List<Map<String,Object>> queryAllStationForBegin(int beginId){
        return stationDAO.queryAllStationForBegin(beginId);
    }

    /**
     * 查询岗位信息
     * @param stationId
     * @return
     * @author 王春生
     */
    public Map<String,Object> queryStationInfo(int stationId){
        return stationDAO.queryStationInfo(stationId);
    }

    /**
     * 动态加载子菜单
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> querySubStation(int parentId){
        return stationDAO.querySubStation(parentId);
    }
    public void setStationDAO(StationDAO stationDAO) {
        this.stationDAO = stationDAO;
    }
}
