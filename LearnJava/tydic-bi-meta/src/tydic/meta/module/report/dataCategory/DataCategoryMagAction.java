package tydic.meta.module.report.dataCategory;

import tydic.frame.common.Log;
import tydic.meta.common.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 王春生
 * @description 数据分类维护
 * @date 12-4-5
 * -
 * @modify
 * @modifyDate -
 */
public class DataCategoryMagAction {

    private DataCategoryDAO categoryDAO;

    /**
     * 查询数据分类 根据关键字
     * @param keyWord
     * @return
     */
    public List<Map<String,Object>> queryDataCategory(String keyWord){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("categoryId",1);
        map.put("parentId",0);
        map.put("categoryName","adfdf");
        map.put("children",0);
        list.add(map);

        Map<String,Object> map1 = new HashMap<String,Object>();
        map1.put("categoryId",2);
        map1.put("parentId",0);
        map1.put("categoryName","adfdfsss");
        map1.put("children",1);
        list.add(map1);
        return list;
    }


    /**
     * 查询数据对象
     * @param data (包含dataType, keyword)
     * @param page 分页对象
     * @return
     */
    public List<Map<String,Object>> queryData(Map<String,Object> data,Page page){

        return null;
    }

    /**
     * 根据父查询子
     * @param parentId
     * @return
     */
    public List<Map<String,Object>> querySubDataCategory(int parentId){
        return null;
    }

    /**
     * 保存数据分类
     * @param data（无ID为新增，有ID为修改）
     * @return
     */
    public int saveDataCategory(Map<String,Object> data){
        int ret = 0;
        try{


        }catch (Exception e){
            Log.error("保存数据分类出错", e);
            ret = -1;
        }
        return ret;
    }

    /**
     * 删除数据分类
     * @param categoryId
     * @return
     */
    public int deleteDataCategory(int categoryId){
        int ret = 0;
        try{


        }catch (Exception e){
            Log.error("删除数据分类出错", e);
            ret = -1;
        }
        return ret;
    }
    
    public int saveDataCategoryRel(int categoryId,int dataType,String relIds){
        int ret = 0;
        try{


        }catch (Exception e){
            Log.error("保存数据分类关系出错", e);
            ret = -1;
        }
        return ret;
    }

    /**
     * 删除数据分类关系
     * @param categoryId 分类ID
     * @param relids 关系字符列表，以“，”分割
     * @return
     */
    public int deleteDataCategoryRel(int categoryId,int dataType,String relids){
        int ret = 0;
        try{


        }catch (Exception e){
            Log.error("删除数据分类关系出错", e);
            ret = -1;
        }
        return ret;
    }

    public void setCategoryDAO(DataCategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }
}
