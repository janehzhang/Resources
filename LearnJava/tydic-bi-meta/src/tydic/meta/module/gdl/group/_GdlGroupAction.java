package tydic.meta.module.gdl.group;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.meta.sys.code.CodeDAO;
import tydic.meta.sys.code.CodeManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 指标分类管理Action <br>
 * @date 2012-3-28
 */
public class _GdlGroupAction {

    private _GdlGroupDAO gdlGroupDAO;
    private CodeDAO codeDAO;


    /**
     * 根据指标分类类型查询父分类树
     * @param queryData
     * @return
     */
    public List<Map<String, Object>> queryGroupTree(Map<String, Object> queryData){
        return gdlGroupDAO.queryGroupTree(queryData);
    }

    /**
     * 取子分类
     * @param parentId
     * @return
     */
    public List<Map<String, Object>> querySubGroupTree(int parentId){
        return gdlGroupDAO.querySubGroupTree(parentId);
    }

    /**
     * 判断是否能删除分类类型
     * @param typeId
     * @return
     */
    public boolean canDeleteType(int typeId){
        return gdlGroupDAO.canDeleteType(typeId);
    }

    /**
     * 修改指标分类类型
     * @param updateData 修改或新增的数据
     * @param rmTypeId 被删除的TypeId
     * @return
     */
    public boolean updateGroupType(List<Map<String, Object>> updateData, List<Integer> rmTypeId){
        try{
            BaseDAO.beginTransaction();
            for(int i = 0; i < updateData.size(); i++){
                Map<String, Object> m = updateData.get(i);
                if(m.get("typeId").toString().equals("-1")){//新增
                    Map<String, Object> codeM = new HashMap<String, Object>();
                    codeM.put("codeId",Convert.toInt(gdlGroupDAO.queryForNextVal("SEQ_SYS_CODE_ID")));
                    codeM.put("codeTypeId",21);
                    codeM.put("codeName",m.get("typeText"));
                    codeM.put("codeValue",gdlGroupDAO.queryForMaxTypeValue()+1);
                    codeM.put("orderId",999999);
                    codeDAO.insertSysCode(codeM);
                }else{//修改
                    Map<String, Object> codeM = new HashMap<String, Object>();
                    codeM.put("codeName",m.get("typeText"));
                    codeM.put("codeId",m.get("typeId"));
                    codeDAO.updateSysName(codeM);
                }
            }
            for(int i = 0; i < rmTypeId.size(); i++){
                codeDAO.deleteCode(Convert.toInt(rmTypeId.get(i),0));
            }
            BaseDAO.commit();
            CodeManager.load();
            return true;
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("",e);
            return false;
        }
    }

    /**
     * 新增指标分类
     * //TODO 当该新增的分类的父分类关联了指标的话，需要维护关联关系表中把其父分类关联的指标改为关联到该分类上
     * @param data
     * @return
     */
    public Map<String, Object> addGroup(Map<String, Object> data){
        try{
            int newGroupId = (int)gdlGroupDAO.queryForNextVal("SEQ_GDL_GROUP_ID");
            data.put("groupId", newGroupId);
            gdlGroupDAO.addGroup(data);
            return gdlGroupDAO.queryById(newGroupId);
        }catch (Exception e){
            Log.error("",e);
            return null;
        }
    }

    /**
     * 修改指标分组
     * @param data
     * @return
     */
    public Map<String, Object> updateGroup(Map<String, Object> data){
        try{
            int groupId = Convert.toInt(data.get("groupId"), 0);
            gdlGroupDAO.updateGroup(groupId, Convert.toString(data.get("groupName")));
            return gdlGroupDAO.queryById(groupId);
        }catch (Exception e){
            Log.error("",e);
            return null;
        }
    }

    /**
     * 查询该指标分类ID是否能被删除
     * @param groupId
     * @return
     */
    public boolean canBeRemoved(int groupId){
        return gdlGroupDAO.canBeRemoved(groupId);
    }

    /**
     * 删除指标分类
     * @param groupId
     * @return
     */
    public boolean removeGroup(int groupId){
        try{
            gdlGroupDAO.removeGroup(groupId);
            return true;
        }catch (Exception e){
            Log.error("",e);
            return false;
        }
    }

    public void setCodeDAO(CodeDAO codeDAO) {
        this.codeDAO = codeDAO;
    }

    public void setGdlGroupDAO(_GdlGroupDAO gdlGroupDAO) {
        this.gdlGroupDAO = gdlGroupDAO;
    }
}
