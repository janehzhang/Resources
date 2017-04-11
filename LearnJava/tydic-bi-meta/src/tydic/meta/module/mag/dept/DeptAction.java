package tydic.meta.module.mag.dept;

import java.util.List;
import java.util.Map;

import tydic.meta.common.Page;

/**
 * Copyrights @ 2011，Tianyuan DIC Information Co., Ltd. All rights reserved.
 * 该类是部门查询相关操作Action类，
 * 
 * @author 刘斌
 * @date 2011-9-29 ----------------------
 * @modify 程钰 增加关联用户的方法
 * @modifyDate
 * 
 * @modify 张伟 新增方法queryDeptByPath
 * @modifyDate 2011-10-11
 *
 * @modify  王春生 新增方法queryAllDeptForBegin
 * @modifyDate  2012-3-12
 */
public class DeptAction {
    /**
     * 数据库操作类
     */
    private DeptDAO deptDAO;

    /**
     * 加载Dept treeGrid树
     * 
     * @param queryData
     * @return
     */
    public List<Map<String, Object>> queryDeptTreeData(
            Map<String, Object> queryData) {
        return deptDAO.queryDept(queryData);
    }

    /**
     * 查询关联用户 // 选中已有的关联用户
     * 
     * @param queryData
     *            查询表单
     * @return 用户列表
     */
    public List<Map<String, Object>> queryRefUser(
            Map<String, Object> queryData, Page page) {
        if (page == null) {
            page = new Page(0, 10);// 每页10行
        }
        return deptDAO.queryUserByCondition(queryData, page);
    }

    /**
     * 动态加载子菜单
     * 
     * @param parentId
     * @return
     */
    public List<Map<String, Object>> querySubDept(int parentId) {

        return deptDAO.querySubDept(parentId);
    }

    /**
     * 根据起始节点到结束节点之间有路径关系节点的所有数据，而不是加载从起始节点到结束节点之间所有的节点数据
     * 
     * @author 张伟
     * @param beginId
     *            起始节点
     * @param endId
     *            结束节点
     * @return
     */
    public List<Map<String, Object>> queryDeptByPath(int beginId, int endId) {
        return deptDAO.queryDeptByBeginEndPath(beginId, endId);
    }

    /**
     * 从一个起始节点开始查询。查询出所有子部门
     * @param beginId
     * @return
     * @author 王春生
     */
    public List<Map<String,Object>> queryAllDeptForBegin(int beginId){
        return deptDAO.queryAllDeptForBegin(beginId);
    }

    /**
     * 查询部门对象
     * @param depId
     * @return
     * @author 王春生
     */
    public Map<String,Object> queryDeptInfo(int depId){
        return deptDAO.queryDeptInfo(depId);
    }
    

    public void setDeptDAO(DeptDAO deptDAO) {
        this.deptDAO = deptDAO;
    }
}
