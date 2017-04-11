package tydic.meta.module.dim.intRel;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.Constant;
import tydic.meta.common.Page;
import tydic.meta.module.dim.MetaDimTabIntRelDAO;
import tydic.meta.module.mag.group.GroupDAO;
import tydic.meta.module.tbl.MetaDimTablesDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 接口表Action <br>
 * @date 2011-11-19
 */
public class DimIntRelAction {

    private MetaDimTablesDAO metaDimTablesDAO;
    private GroupDAO groupDAO;
    private MetaDimTabIntRelDAO metaDimTabIntRelDAO;
    private DimIntRelDAO dimIntRelDAO;

    /**
     * 查询所有有效的维度表
     *
     * @return
     */
    public List<Map<String, Object>> queryValidDimTables() {
        List<Map<String, Object>> list = metaDimTablesDAO.queryAllValidDimTables();
//    	Map<String,Object> map = new HashMap<String, Object>();
//    	map.put("TABLE_VERSION", list.get(0).get("TABLE_VERSION"));
//    	map.put("TABLE_NAME", list.get(0).get("TABLE_NAME"));
//    	map.put("TABLE_NAME_CN", list.get(0).get("TABLE_NAME_CN"));
//    	map.put("TABLE_ID", list.get(0).get("TABLE_ID"));
//    	list.add(map);
//    	list.get(0).put("TABLE_NAME_CN", "");
//    	list.get(0).put("TABLE_ID", "");
//    	list.get(0).put("TABLE_NAME", "");

        return list;
    }

    /**
     * 查询接口表信息
     *
     * @param sysId
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryIntRelInfo(Integer sysId, Integer tableId, Page page) {
        if (page == null) {
            page = new Page(0, 10);
        }
        return metaDimTabIntRelDAO.queryBySytemAndDim(sysId, tableId, page);
    }

    /**
     * 查询维度接口值信息。
     *
     * @param dimInfo      维度表定义信息，包含键值dimTableId,tableName,tableDimPrefix
     * @param queryMessage 查询信息，包含键值 keyWord:关键字，dimTypeId:归并类型ID，system:系统ID。mappingStatus 查询状态
     * @return
     */
    public List<Map<String, Object>> queryIntRelValueInfo(Map<String, Object> dimInfo,
                                                          Map<String, Object> queryMessage, Page page) {
        int mappingStatus = Integer.parseInt(queryMessage.get("mappingStatus").toString());
        List<Map<String, Object>> rs = null;
        switch (mappingStatus) {
            case 2: { //查询未映射的数据
                rs = dimIntRelDAO.queryUnMapping(dimInfo, queryMessage, page);
                break;
            }
            case 3:	//查询未审核的数据
                rs = dimIntRelDAO.queryByHis(dimInfo, queryMessage);
                break;
            case 4: {//查询已审核映射的数据
                rs = dimIntRelDAO.queryByHisIsAudit(dimInfo, queryMessage);
                break;
            }
            default: {
                break;
            }
        }
        return rs;
    }

    /**
     * 查询所有的接口值，异步加载
     *
     * @param dimInfo
     * @param queryMessage
     * @return
     */
    public List<Map<String, Object>> queryAllIntRelValue(Map<String, Object> dimInfo,
                                                         Map<String, Object> queryMessage, Integer parId) {
        String dimTablePrefix = Convert.toString(dimInfo.get("tableDimPrefix"));
        List<Map<String, Object>> rs = dimIntRelDAO.queryAll(dimInfo, queryMessage, parId);
        String keyWord = Convert.toString(queryMessage.get("keyWord"));
        if (parId != null) {
            keyWord = "";
        }
        int minLevel = 0;//最小层级
//        Map<Integer, Map<String, Object>> mapping = new HashMap<Integer, Map<String, Object>>();
        if (rs != null && rs.size() > 0) {
            minLevel = MapUtils.getInteger(rs.get(0), "DIM_LEVEL");
            for (int i = 0; i < rs.size(); i++) {
                Map<String, Object> map = rs.get(i);
                int noDisplayCount = MapUtils.getIntValue(map, "NO_DISPLAY_COUNT");
                int childCount = MapUtils.getIntValue(map, "CHILDREN");
                if (StringUtils.isNotEmpty(keyWord)) {
                    //查询最小层次的层级
                    if (MapUtils.getInteger(map, "DIM_LEVEL") > minLevel) {
                        rs.remove(i--);
                        continue;
                    }
                    map.put(dimTablePrefix + "_PAR_ID", Constant.DEFAULT_ROOT_PARENT);
                }
                if (childCount > 0 && noDisplayCount == 0) {
                    //如果有子节点，且子节点有可以显示的节点
                    map.put("ISMAPPING", "--");
                    map.put("SRC_CODE", "--");
                    map.put("SRC_NAME", "--");
                }
                if (noDisplayCount > 0) {
                    //如果存在未显示的子节点
                    int tempParId = MapUtils.getIntValue(map, dimTablePrefix + "_ID");
                    List<Map<String, Object>> unDisplay = dimIntRelDAO.queryChildMapping(dimInfo, queryMessage, tempParId);
                    rs.remove(i--);
                    i = i == -1 ? 0 : i;
                    for (int j = 0; j < unDisplay.size(); j++) {
                        Map<String, Object> temp = new HashMap<String, Object>();
                        temp.putAll(map);
                        temp.put("ISMAPPING", 1);
                        temp.put("SRC_CODE", unDisplay.get(j).get("SRC_CODE"));
                        temp.put("SRC_NAME", unDisplay.get(j).get("SRC_NAME"));
                        temp.put(dimTablePrefix + "_ID", unDisplay.get(j).get(dimTablePrefix + "_ID"));
                        temp.put("CHILDREN", 0);
                        i = i + j;
                        rs.add(i, temp);
                    }
                }
            }
        }
        return rs;
    }

    /**
     * 查询所有的系统
     *
     * @return
     */
    public List<Map<String, Object>> queryAllSystem() {
        List<Map<String, Object>> list = groupDAO.queryGroup(null, null);
        return list;
    }

    public void setMetaDimTablesDAO(MetaDimTablesDAO metaDimTablesDAO) {
        this.metaDimTablesDAO = metaDimTablesDAO;
    }

    public void setGroupDAO(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public void setMetaDimTabIntRelDAO(MetaDimTabIntRelDAO metaDimTabIntRelDAO) {
        this.metaDimTabIntRelDAO = metaDimTabIntRelDAO;
    }

    public void setDimIntRelDAO(DimIntRelDAO dimIntRelDAO) {
        this.dimIntRelDAO = dimIntRelDAO;
    }
}
