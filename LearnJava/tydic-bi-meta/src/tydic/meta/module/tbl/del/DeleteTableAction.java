package tydic.meta.module.tbl.del;

import tydic.frame.BaseDAO;
import tydic.meta.common.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 表类维度删除Action
 * @date 12-5-14
 * -
 * @modify
 * @modifyDate -
 */
public class DeleteTableAction {

    private DeleteTableDAO delTableDAO;

    /**
     * 删除表类
     * @param tableId 表类ID
     * @param tableType 层级分类
     * @return
     */
    public Map<String,Object> deleteTable(int tableId,String tableName,int tableType,boolean delInstance){
        Map<String,Object> ret = new HashMap<String,Object>();
        StringBuilder logStr = new StringBuilder("删除表类《"+tableName+"》数据:\n");
        try{
            BaseDAO.beginTransaction();
            delTableDAO.deleteTables(tableId,tableType,delInstance,logStr);
            BaseDAO.commit();
            ret.put("flag", "1");//flag 为1表示删除成功
            logStr.append("已提交操作！\n");
        }catch(Exception e){
            logStr.append("发生错误:"+e.getMessage()+"\n");
            BaseDAO.rollback();
            ret.put("flag", "2");//flag 为2表示失败
            logStr.append("已回滚操作！\n");
            ret.put("msg", logStr.toString());
        }
        return ret;
    }

    /**
     * 维护表类-查询表类信息列表
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryTablesMatain(Map<String,Object> queryData,Page page){
        if(page == null){
            page = new Page(0,20);
        }
        List<Map<String,Object>> returnValue = delTableDAO.queryMetaTablesMatain(queryData, page);
        return returnValue;
    }

    public void setDelTableDAO(DeleteTableDAO delTableDAO) {
        this.delTableDAO = delTableDAO;
    }
}
