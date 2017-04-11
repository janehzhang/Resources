package tydic.meta.module.gdl.group;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.module.gdl.GdlGroupDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description
 * @date 12-6-4
 * -
 * @modify
 * @modifyDate -
 */
public class GdlGroupAction {

    private GdlGroupDAO groupDAO;

    public void setGroupDAO(GdlGroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    /**
     * 查询分类树
     * @param data
     * @return
     */
    public List<Map<String,Object>> queryGroup(Map<String,Object> data){
        String kwd = MapUtils.getString(data,"KEY_WORD","");
        return groupDAO.queryGroup(0,kwd);
    }

    /**
     * 查询分类树子节点
     * @param parentID
     * @return
     */
    public List<Map<String,Object>> queryChild(int parentID){
        return groupDAO.queryGroup(parentID,"");
    }

    /**
     * 保存分类信息
     * @param data
     * @return
     */
    public Map<String,Object> saveGroupInfo(Map<String,Object> data){
        Map<String,Object> result = new HashMap<String,Object>();
        try{
            String groupId = MapUtils.getString(data,"groupId");
            String groupName = MapUtils.getString(data,"groupName");
            BaseDAO.beginTransaction();
            if(groupId!=null && !"".equals(groupId) && !"0".equals(groupId)){
                //修改
                if(groupDAO.checkGroupNameExists(groupName, Convert.toInt(groupId))){
                    result.put("flag","exists");
                }else{
                    groupDAO.updateGroupInfo(data);
                    result.put("flag","update");
                }
            }else{
                //添加
                if(groupDAO.checkGroupNameExists(groupName,0)){
                    result.put("flag","exists");
                }else{
                    groupDAO.insertGroupInfo(data);
                    result.put("flag","insert");
                }
            }
            BaseDAO.commit();
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("保存分类信息出错", e);
            result.put("flag","error");
            result.put("msg","");
        }
        return result;
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    public Map<String,Object> deleteGroupInfo(int id){
        Map<String,Object> result = new HashMap<String,Object>();
        try{
            if(groupDAO.canDeleteGroup(id)){
                BaseDAO.beginTransaction();
                groupDAO.deleteGroup(id);
                BaseDAO.commit();
                result.put("flag","true");
            }else{
                result.put("flag","false");
            }
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("删除分类信息出错", e);
            result.put("flag","error");
            result.put("msg","");
        }
        return result;
    }

    /**
     * 保存分类层次调整
     * @param data
     * @return
     */
    public Map<String,Object> saveGroupLevel(Map<String,Object> data){
        Map<String,Object> result = new HashMap<String,Object>();
        try{
            BaseDAO.beginTransaction();
            groupDAO.saveGroupLevel(data);
            BaseDAO.commit();
            result.put("flag","true");
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("保存层级信息出错", e);
            result.put("flag","error");
            result.put("msg","");
        }
        return result;
    }



}
