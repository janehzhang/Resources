package tydic.meta.module.tbl.group;

import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.module.tbl.MetaSysCodeDAO;
import tydic.meta.module.tbl.MetaTableGroupDAO;
import tydic.meta.module.tbl.TblConstant;

/**
 * 
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * 
 * @author 熊小平
 * @date 2011-10-26
 * @description 表类分类管理控制类
 * 
 */
public class TableCategoryAction {

    private MetaSysCodeDAO metaSysCodeDAO;
    private MetaTableGroupDAO metaTableGroupDAO;

    /**
     * 查询所有的表类型:TABLE_TYPE
     * 
     * @return
     */
    public List<Map<String, Object>> queryTableType() {
        return metaSysCodeDAO.querySysCodeByCodeType(TblConstant.META_SYS_CODE_TABLE_TYPE);
    }

    /**
     * 查询表类分类
     * @param queryData查询参数
     * @return
     */
    public List<Map<String, Object>> queryTableGroup(Map<String,Object> queryData) {
        return metaTableGroupDAO.queryTableGroup(queryData);
    }
    
    /**
     * 查询子表类
     * @param parentId
     * @return
     */
    public List<Map<String, Object>> querySubTableGroup(int parentId) {
        return metaTableGroupDAO.querySubTableGroup(parentId);
    }

    /**
     * 查询父表类
     * @param parGroupId 传入parGroupId值
     * @return 顶级节点返回null，其他返回其相应的父表类
     */
    public Map<String, Object> queryParTableGroup(int parentId) {
        return metaTableGroupDAO.queryParTableGroup(parentId);
    }

    /**
     * 增加表类，同级或下级均可，只是参数parGroupId的值不同而已
     * @param data
     * @return
     */
    public boolean insertTableGroup(Map<String, Object> data) {
        return metaTableGroupDAO.insertTableGroup(data);
    }
    
    /**
     * 修改表类
     * @param data 修改参数，其中tableGroupId不能缺省
     * @return
     */
    public boolean updateTableGroup(Map<String,Object> data){
        return metaTableGroupDAO.updateTableGroup(data);
    }
    
    /**
     * 删除表类
     * @param data 参数中应含有待删除的表类的ID值
     * @return 返回固定的几个值，其含义如下：
     * Success删除成功，ParamsError参数错误，FKConstraint外键约束，PARConstraint自约束（含子分类），Fail删除失败
     * @throws Exception 
     */
    public boolean deleteTableGroup(int groupId) throws Exception{
        return metaTableGroupDAO.deleteTableGroup(groupId);
    }

    // setters
    public void setMetaSysCodeDAO(MetaSysCodeDAO metaSysCodeDAO) {
        this.metaSysCodeDAO = metaSysCodeDAO;
    }

    public void setTableCategoryDAO(MetaTableGroupDAO tableCategoryDAO) {
        this.metaTableGroupDAO = tableCategoryDAO;
    }

}
