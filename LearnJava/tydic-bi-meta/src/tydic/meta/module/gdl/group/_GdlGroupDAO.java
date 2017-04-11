package tydic.meta.module.gdl.group;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 指标分类管理DAO <br>
 * @date 2012-3-28
 */
public class _GdlGroupDAO extends MetaBaseDAO {

    /**
     * 根据指标分类类型查询父分类树
     * @param queryData
     * @return
     */
    public List<Map<String, Object>> queryGroupTree(Map<String, Object> queryData){
        String sql = "SELECT A.GDL_GROUP_ID, A.PAR_GROUP_ID, A.GROUP_NAME, A.GROUP_TYPE,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN " +
                "FROM META_GDL_GROUP A LEFT JOIN (SELECT PAR_GROUP_ID, COUNT(1) CNT FROM   META_GDL_GROUP GROUP  BY PAR_GROUP_ID) C " +
                "ON A.GDL_GROUP_ID = C.PAR_GROUP_ID WHERE a.group_type = ? AND a.par_group_id = 0 ORDER BY A.ORDER_ID";
        return getDataAccess().queryForList(sql, Convert.toInt(queryData.get("groupTypeId"), 0));
    }

    /**
     * 查询子分类
     * @param parentId
     * @return
     */
    public List<Map<String, Object>> querySubGroupTree(int parentId){
        String sql = "SELECT A.GDL_GROUP_ID, A.PAR_GROUP_ID, A.GROUP_NAME, A.GROUP_TYPE,DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN " +
                "FROM META_GDL_GROUP A LEFT JOIN (SELECT PAR_GROUP_ID, COUNT(1) CNT FROM   META_GDL_GROUP GROUP  BY PAR_GROUP_ID) C " +
                "ON A.GDL_GROUP_ID = C.PAR_GROUP_ID WHERE a.par_group_id = ? ORDER BY A.ORDER_ID";
        return getDataAccess().queryForList(sql, parentId);
    }

    /**
     * 检查是否可以删除指标分类类型
     * @param typeId
     * @return
     */
    public boolean canDeleteType(int typeId){
        String sql = "SELECT COUNT(1) FROM meta_gdl_group WHERE group_type = " + typeId;
        return getDataAccess().queryForInt(sql)==0;
    }

    /**
     * 查询最大指标分类类型值
     * @return
     */
    public int queryForMaxTypeValue(){
        String sql = "SELECT MAX(CODE_VALUE) FROM META_SYS_CODE WHERE CODE_TYPE_ID=21";
        return getDataAccess().queryForInt(sql);
    }

    /**
     * 新增指标分类
     * @param data
     */
    public void addGroup(Map<String, Object> data){
        String sql = "INSERT INTO META_GDL_GROUP " +
                "  (GDL_GROUP_ID, PAR_GROUP_ID, GROUP_NAME, ORDER_ID, GROUP_TYPE) " +
                "   VALUES " +
                "  (?, ?, ?, ?, ?) ";
        getDataAccess().execUpdate(sql, Convert.toInt(data.get("groupId")), Convert.toInt(data.get("parId"))
            , Convert.toString(data.get("groupName")), 999,Convert.toInt(data.get("typeId")));

    }

    /**
     * 根据指标分类ID取指标分类信息
     * @param groupId
     * @return
     */
    public Map<String, Object> queryById(int groupId){
        String sql = "SELECT GDL_GROUP_ID, PAR_GROUP_ID, GROUP_NAME, ORDER_ID, GROUP_TYPE FROM META_GDL_GROUP" +
                " WHERE GDL_GROUP_ID = ?";
        return getDataAccess().queryForMap(sql, groupId);
    }

    /**
     * 根据指标分组ID修改指标分组名称
     * @param groupId
     * @param groupName
     */
    public void updateGroup(int groupId, String groupName){
        String sql = "UPDATE META_GDL_GROUP SET GROUP_NAME = ? WHERE GDL_GROUP_ID = "+groupId;
        getDataAccess().execUpdate(sql, groupName);
    }

    /**
     * 检查该指标是否能被删除
     * @param groupId
     * @return
     */
    public boolean canBeRemoved(int groupId){
        String sql = "SELECT COUNT(1) FROM META_GDL_GROUP_REL WHERE GDL_GROUP_ID="+groupId;
        return getDataAccess().queryForInt(sql)==0;
    }

    /**
     * 删除指标分类
     * @param groupId
     * @return
     */
    public void removeGroup(int groupId){
        String sql = "DELETE FROM meta_gdl_group WHERE GDL_GROUP_ID="+groupId;
        getDataAccess().execUpdate(sql);
    }

}

