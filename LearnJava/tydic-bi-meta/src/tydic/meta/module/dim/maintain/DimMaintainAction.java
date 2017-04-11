package tydic.meta.module.dim.maintain;

import tydic.meta.common.Page;
import tydic.meta.module.tbl.MetaTableInstDAO;
import tydic.meta.module.tbl.MetaTablesDAO;
import tydic.meta.module.tbl.TblConstant;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 维护维度视图 <br>
 * @date 2011-11-07
 */
public class DimMaintainAction{

    private MetaTableInstDAO metaTableInstDAO;

    private DimMaintainDAO dimMaintainDAO;

    private MetaTablesDAO metaTablesDAO;

    /**
     * 查询维度表类视图，
     * @param queryData 包含键值tableGroup 表分类，keyWord.关键字。
     * @return
     */
    public List<Map<String,Object>> queryDimView(Map<String,Object> queryData,Page page){
        if(page == null){
            page = new Page(0,20);
        }
        List<Map<String,Object>> returnValue = dimMaintainDAO.queryDimView(queryData,page);
        return returnValue;
    }

    /**
     * 根据表类ID查询表实例信息
     * @param tableId
     * @return
     */
    public Map<String,Object>  queryDimInstance(int tableId){
        List<Map<String,Object>> insts=metaTableInstDAO.queryTableInstanceByTableId(tableId);
        if(insts==null||insts.size()==0){
            return null;
        }
        return insts.get(0);
    }

    /**
     * 查询表类有效版本号。
     * @param tableId
     * @return
     */
    public int  queryValidVersion(int tableId){
        return metaTablesDAO.queryValidVersion(tableId);
    }

    public void setMetaTableInstDAO(MetaTableInstDAO metaTableInstDAO){
        this.metaTableInstDAO = metaTableInstDAO;
    }

    public void setDimMaintainDAO(DimMaintainDAO dimMaintainDAO){
        this.dimMaintainDAO = dimMaintainDAO;
    }

    public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO){
        this.metaTablesDAO = metaTablesDAO;
    }
}
