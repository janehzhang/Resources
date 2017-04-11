package tydic.meta.component;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;
import tydic.meta.module.report.issue.Table.IssueDataModelDAO;
import tydic.meta.module.tbl.MetaTablesDAO;
import tydic.meta.module.tbl.apply.TableApplyDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 业务组件Action
 * @date 12-5-7
 * -
 * @modify
 * @modifyDate -
 */
public class ComponentAction {

    private MetaTablesDAO metaTablesDAO;

    /**
     * 查询表类数据
     * @param data
     * @param page
     * @return
     */
    public List<Map<String,Object>> getTablesGridData(Map<String,Object> data,Page page){
        return metaTablesDAO.queryCmpTables(data, page);
    }

    /**
     * 获取用户查询数据
     * @param data
     * @param page
     * @return
     */
    public List<Map<String,Object>> getUsersGridData(Map<String,Object> data,Page page){
        return null;
    }

    public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO) {
        this.metaTablesDAO = metaTablesDAO;
    }
}
