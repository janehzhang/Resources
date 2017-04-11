package tydic.meta.module.gdl;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.SqlUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 指标分类DAO
 * @date 12-6-4
 * -
 * @modify
 * @modifyDate -
 */
public class GdlGroupDAO extends MetaBaseDAO {

    /**
     * 查询分类
     *
     * @param pid
     * @param kwd
     * @return
     */
    public List<Map<String, Object>> queryGroup(int pid, String kwd) {
        String sql = "SELECT A.GDL_GROUP_ID,A.PAR_GROUP_ID,A.GROUP_NAME," +
                "(SELECT GROUP_NAME FROM META_GDL_GROUP WHERE GDL_GROUP_ID=A.PAR_GROUP_ID) PAR_GROUP_NAME," +
                "(case when  exists (select 1 from META_GDL_GROUP where PAR_GROUP_ID=A.GDL_GROUP_ID) then 1 else 0 end) HAS_CHILD," +
                "A.ORDER_ID " +
                "FROM META_GDL_GROUP A WHERE 1=1 ";
        List<Object> par = new ArrayList<Object>();
        if (kwd != null && !"".equals(kwd)) {
            if (!kwd.contains("%") && !kwd.contains("_")) {
                sql += "AND Upper(A.GROUP_NAME) LIKE UPPER(?) ";
                par.add("%" + kwd + "%");
            } else {
                kwd = kwd.replaceAll("_", "/_").replaceAll("%", "/%");
                sql += "AND Upper(A.GROUP_NAME) LIKE UPPER(?) ESCAPE '/' ";
                par.add("%" + kwd + "%");
            }
        }else{
            sql += " AND A.PAR_GROUP_ID=? ";
            par.add(pid);
        }
        sql += " ORDER BY A.ORDER_ID ";
        return getDataAccess().queryForList(sql, par.toArray());
    }

    /**
     * 根据指标集合查询指标分组信息
     *
     * @param gdls
     * @return
     */
    public List<Map<String, Object>> queryGroupByGdls(List<Integer> gdls) {
        String sql = "SELECT DISTINCT TAB.* FROM (SELECT T.GDL_GROUP_ID,T.PAR_GROUP_ID,T.GROUP_NAME,T.ORDER_ID FROM META_GDL_GROUP T  CONNECT BY PRIOR T.PAR_GROUP_ID=T.GDL_GROUP_ID " +
                " START WITH T.GDL_GROUP_ID IN (SELECT GDL_GROUP_ID  FROM META_GDL_GROUP_REL WHERE GDL_ID IN" + SqlUtils.inParamDeal(gdls) + ")) TAB ORDER BY  ORDER_ID ASC";
        return getDataAccess().queryForList(sql);
    }


    /**
     * 根据指标集合和分组集合查询指标和分组之间的关系
     *
     * @param gdls
     * @param groups
     * @return
     */
    public List<Map<String, Object>> queryRelByGdlsAndGroups(List<Integer> gdls, List<Integer> groups) {
        String sql = "SELECT DISTINCT T.GDL_ID,T.GDL_GROUP_ID FROM META_GDL_GROUP_REL T WHERE T.GDL_ID IN " + SqlUtils.inParamDeal(gdls)
                + " AND T.GDL_GROUP_ID IN " + SqlUtils.inParamDeal(groups);
        return getDataAccess().queryForList(sql);
    }

    /**
     * 检测指标分类名称是否已存在
     *
     * @param groupName
     * @param groupId   检测时需要排除此ID，不传默认为0
     * @return
     */
    public boolean checkGroupNameExists(String groupName, int groupId) {
        String sql = "SELECT COUNT(1) FROM META_GDL_GROUP WHERE GROUP_NAME=? AND GDL_GROUP_ID!=? ";
        return getDataAccess().queryForInt(sql, groupName, groupId) >= 1;
    }

    /**
     * 添加分类
     *
     * @param data
     * @return
     */
    public int insertGroupInfo(Map<String, Object> data) {
        String sql = "INSERT INTO META_GDL_GROUP " +
                "(GDL_GROUP_ID, PAR_GROUP_ID, GROUP_NAME, ORDER_ID) " +
                "SELECT ?,?,?,NVL(MAX(ORDER_ID),1)+1 FROM META_GDL_GROUP ";
        long pk = queryForNextVal("SEQ_GDL_GROUP_ID");
        return getDataAccess().execUpdate(sql, pk, Convert.toInt(data.get("parentId"))
                , Convert.toString(data.get("groupName")));
    }

    /**
     * 修改分类
     *
     * @param data
     * @return
     */
    public int updateGroupInfo(Map<String, Object> data) {
        String sql = "UPDATE META_GDL_GROUP SET GDL_GROUP_ID=? ";
        String parentId = MapUtils.getString(data, "parentId", "");
        String groupName = MapUtils.getString(data, "groupName", "");
        String orderId = MapUtils.getString(data, "orderId", "");
        int id = MapUtils.getInteger(data, "groupId");
        List<Object> par = new ArrayList<Object>();
        par.add(id);
        if (!"".equals(parentId)) {
            sql += ",PAR_GROUP_ID=? ";
            par.add(Convert.toInt(parentId));
        }
        if (!"".equals(groupName)) {
            sql += ",GROUP_NAME=? ";
            par.add(Convert.toString(groupName));
        }
        if (!"".equals(orderId)) {
            sql += ",ORDER_ID=? ";
            par.add(Convert.toInt(orderId));
        }
        sql += " WHERE GDL_GROUP_ID=? ";
        par.add(id);
        return getDataAccess().execUpdate(sql, par.toArray());
    }

    /**
     * 删除分类
     *
     * @param id
     */
    public void deleteGroup(int id) {
        String sql = "DELETE FROM META_GDL_GROUP WHERE GDL_GROUP_ID=?";
        getDataAccess().execNoQuerySql(sql, id);
    }

    /**
     * 检查是否可以删除指标分类
     *
     * @param typeId
     * @return
     */
    public boolean canDeleteGroup(int typeId) {
        String sql = "SELECT (CASE WHEN " +
                "EXISTS(SELECT 1 FROM META_GDL_GROUP WHERE PAR_GROUP_ID=?) " +
                "OR EXISTS(SELECT 1 FROM META_GDL_GROUP_REL WHERE GDL_GROUP_ID=?) " +
                "THEN 1 else 0 END) es FROM DUAL";
        return getDataAccess().queryForInt(sql, typeId, typeId) == 0;
    }


    /**
     * 添加指标与分类的关系
     *
     * @param gdlId
     * @param groupId
     * @return
     */
    public int insertGdlGroupRel(int gdlId, int groupId) {
        deleteGdlGroupRel(gdlId, groupId);
        String sql = "INSERT INTO META_GDL_GROUP_REL(GDL_ID,GDL_GROUP_ID) VALUES(?,?)";
        return getDataAccess().execUpdate(sql, gdlId, groupId);
    }

    /**
     * 删除指标与分类关系
     *
     * @param gdlId
     * @param groupId
     * @return
     */
    public int deleteGdlGroupRel(int gdlId, int groupId) {
        String sql = "DELETE FROM META_GDL_GROUP_REL WEHRE GDL_ID=? ";
        List<Integer> par = new ArrayList<Integer>();
        par.add(gdlId);
        if (groupId != 0) {
            sql += " AND GDL_GROUP_ID=? ";
            par.add(groupId);
        }
        return getDataAccess().execUpdate(sql, par.toArray());
    }

    /**
     * 保存层级信息
     *
     * @param data
     * @return
     */
    public int saveGroupLevel(Map<String, Object> data) {
        if (data.size() > 0) {
            String sql = "UPDATE META_GDL_GROUP SET PAR_GROUP_ID=?,ORDER_ID=? WHERE GDL_GROUP_ID=?";
            Object[][] param = new Object[data.size()][3];
            int i = 0;
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                int id = Convert.toInt(entry.getKey());
                Map<String, Object> _d = (Map<String, Object>) entry.getValue();
                int pid = MapUtils.getInteger(_d, "pid");
                int order = MapUtils.getInteger(_d, "order");
                param[i] = new Object[]{pid, order, id};
                i++;
            }
            getDataAccess().execUpdateBatch(sql, param);
            return 1;
        }
        return 0;
    }

    /**
     * 得到所有分类数据，用于绑定树上
     *
     * @return
     */
    public Object[][] quertAllData() {
        String sql = "SELECT T.GDL_GROUP_ID,T.GROUP_NAME,T.PAR_GROUP_ID " +
                " FROM META_GDL_GROUP T ORDER BY T.ORDER_ID ASC";
        return getDataAccess().queryForArray(sql, false);
    }
}
