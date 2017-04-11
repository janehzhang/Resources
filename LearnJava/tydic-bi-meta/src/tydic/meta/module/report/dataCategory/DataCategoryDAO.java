package tydic.meta.module.report.dataCategory;

import tydic.meta.common.MetaBaseDAO;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 数据分类DAO
 * @date 12-4-5
 * -
 * @modify
 * @modifyDate -
 */
public class DataCategoryDAO extends MetaBaseDAO{

    /**
     * 根据父分类查询子分类（一级）
     * @param parent
     * @return
     */
    public List<Map<String,Object>> queryDataCategory(int parent){
        return queryDataCategory(parent,null);
    }

    /**
     * 根据父分类查询子分类(一级)，并根据关键字过滤
     * @param parent
     * @param keyword
     * @return
     */
    public List<Map<String,Object>> queryDataCategory(int parent,String keyword){

        return null;
    }

    /**
     * 根据父ID查询所有子ID
     * @param parent
     * @return
     */
    public List<Map<String,Object>> queryAllSubDataCategory(int parent){
        return null;
    }

    /**
     * 查询某分类下面子分类的数量
     * @param parentId
     * @return
     */
    public int countDataCategorySubNum(int parentId){
        return 0;
    }

    /**
     * 插入一个数据分类对象
     * @param data
     * @return
     */
    public int insertDataCategory(Map<String,Object> data){
        return 1;
    }

    /**
     * 更新一个数据分类对象
     * @param data
     * @return
     */
    public int updateDataCategory(Map<String,Object> data){
        return 1;
    }

    /**
     * 保存分类关系
     * @param categoryId 分类id
     * @param dataType 数据类型（1报表，2清单，3指标）
     * @param relids 数据关系ID集合
     * @return
     */
    public int saveDataCategoryRel(int categoryId,int dataType,String ... relids){
        String sql = "";
        return 1;
    }

    /**
     * 删除分类
     * @param categoryId
     * @return
     */
    public int deleteDataCategory(int categoryId){
        return 1;
    }

    /**
     * 删除数据分类关系
     * @param categoryId
     * @param dataType 数据类型（1报表，2清单，3指标）
     * @param relids 数据关系ID集合
     * @return
     */
    public int deleteDataCategoryRel(int categoryId,int dataType,String ... relids){

        return 1;
    }

    /**
     * 根据数据分类ID删除所有关系
     * @param categoryId
     * @return
     */
    public int deleteDataCategoryRelByCatId(int categoryId){
        return 1;
    }

}
