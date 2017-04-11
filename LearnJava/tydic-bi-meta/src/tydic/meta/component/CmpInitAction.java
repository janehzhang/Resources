package tydic.meta.component;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.module.tbl.MetaDataSourceDAO;
import tydic.meta.module.tbl.MetaTableGroupDAO;
import tydic.meta.module.tbl.MetaTablesDAO;
import tydic.meta.module.tbl.TblConstant;
import tydic.meta.sys.code.CodeManager;
import tydic.meta.sys.code.CodePO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 初始业务组件的Action
 * @date 12-5-7
 * -
 * @modify
 * @modifyDate -
 */
public class CmpInitAction {

    private MetaDataSourceDAO metaDataSourceDAO;
    private MetaTablesDAO metaTablesDAO;
    private MetaTableGroupDAO metaTableGroupDAO;

    /**
     * 获取表类弹出选择框查询区初始数据
     * @return
     */
    public Map<String,Object> getTablesGridHeader(){
        Map<String,Object> map = new HashMap<String,Object>();
        List<Map<String,Object>> ds = metaDataSourceDAO.queryDataSourceByType(TblConstant.META_DATA_SOURCE_TABLE, TblConstant.COL_STATE_VALID);
        map.put("dataSource",ds);

        CodePO[] codes = CodeManager.getCodes("TABLE_TYPE");
        List<Map<String,Object>> cs = new ArrayList<Map<String, Object>>();
        for(CodePO po : codes){
            Map<String,Object> codeMap = new HashMap<String,Object>();
            codeMap.put("codeValue",po.getCodeValue());
            codeMap.put("codeName",po.getCodeName());
            cs.add(codeMap);
        }
        map.put("tableTypes",cs);
        return map;
    }

    /**
     * 获取数据源用户
     * @param dataSourceId
     * @return
     */
    public List<Map<String,Object>> getDSOwner(int dataSourceId){
        List<Map<String,Object>> maps = metaTablesDAO.getUserNameListByDataSourceId(dataSourceId);
        return maps;
    }

    /**
     * 根据表typeId查找所有的表分类。
     * @param tableType
     * @return
     */
    public List<Map<String,Object>> queryTableGroup(int tableType){
        return metaTableGroupDAO.queryTableGroup(tableType);
    }

    /**
     * 获取用户弹出选择框头区域初始数据
     * @return
     */
    public Map<String,Object> getUsersGridHeader(){
        return null;
    }

    public void setMetaDataSourceDAO(MetaDataSourceDAO metaDataSourceDAO) {
        this.metaDataSourceDAO = metaDataSourceDAO;
    }

    public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO) {
        this.metaTablesDAO = metaTablesDAO;
    }

    public void setMetaTableGroupDAO(MetaTableGroupDAO metaTableGroupDAO) {
        this.metaTableGroupDAO = metaTableGroupDAO;
    }
}
