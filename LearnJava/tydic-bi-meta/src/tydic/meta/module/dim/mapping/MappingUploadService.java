package tydic.meta.module.dim.mapping;

import java.util.*;

import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.module.dim.DimConstant;
import tydic.meta.module.dim.MetaDimTabIntRelDAO;
import tydic.meta.module.dim.MetaDimTabModHisDAO;

public class MappingUploadService {
    //private DimMappingDAO mappingDao = new DimMappingDAO();
    public List<Map<String, Object>> insertMappData(List<Map<String, Object>> dataList, String tableName, String prefix, String tableOwner, int sysId, int dimTableId, int dimTypeId, int userId) throws Exception {
        DimMappingDAO mappingDao = new DimMappingDAO();
        MetaDimTabIntRelDAO metaDimTabIntRelDAO = new MetaDimTabIntRelDAO();
        List<Map<String, Object>> mappList = null;
        List<Map<String, Object>> rightMapList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> errorList = new ArrayList<Map<String, Object>>();
        //所有已经符合映射条件的末级数据
        Map<String, Map<String, Object>> codeMap = null;
        //已经映射的末级数据
        Map<String, String> existMap = new HashMap<String, String>();
        //所有符合条件的接口表编码数据
        Set<String> intCodes = null;
        try {
            mappList = mappingDao.queryDimCodeByTableName(tableName, prefix, tableOwner, sysId, dimTypeId);
            List<Map<String, Object>> existDataList = mappingDao.queryMappDataByCondition(sysId, dimTableId);
            //接口表信息
            List<Map<String, Object>> intInfo = metaDimTabIntRelDAO.queryBySytemAndDim(sysId, dimTableId, null);
            Map<String, Object> intRel = intInfo.get(0);
            intCodes = mappingDao.queryIntRelCode(MapUtils.getIntValue(intRel, "DATA_SOURCE_ID"),
                    MapUtils.getString(intRel, "INT_TABLE_OWNER"), MapUtils.getString(intRel, "INT_TAB_NAME"));
            if (mappList != null && mappList.size() != 0) {
                codeMap = new HashMap<String, Map<String, Object>>();
                for (int i = 0; i < mappList.size(); i++) {
                    Map<String, Object> map = mappList.get(i);
                    String key = prefix + "_CODE";
                    String itemCode = map.get(key.toUpperCase()).toString();
                    codeMap.put(itemCode, map);
                }
            }
            if (existDataList != null && existDataList.size() != 0) {
                for (int i = 0; i < existDataList.size(); i++) {
                    Map<String, Object> map = existDataList.get(i);
                    if (map != null && map.size() != 0) {
                        String value = map.get("ITEM_CODE").toString();
                        existMap.put(value, value);
                    }
                }
            }
            if (dataList != null && dataList.size() != 0) {
                String batchId = String.valueOf(System.currentTimeMillis());
                for (int i = 1; i < dataList.size(); i++) {
                    Map<String, Object> dataMap = dataList.get(i);
                    String itemCode = dataMap.get("itemCode").toString();
                    Map itemMap = codeMap.get(itemCode);
                    dataMap.put("ITEM_ID", itemMap.get(prefix.toUpperCase() + "_ID"));
                    dataMap.put("ITEM_CODE", itemMap.get(prefix.toUpperCase() + "_CODE"));
                    dataMap.put("SRC_SYS_ID", sysId);
                    dataMap.put("ITEM_NAME", dataMap.get("itemName"));
                    dataMap.put("SRC_CODE", dataMap.get("srcCode"));
                    dataMap.put("SRC_NAME", dataMap.get("srcName"));
                    dataMap.put("DIM_TYPE_ID", dimTypeId);
                    dataMap.put("MOD_FLAG", DimConstant.DIM_MAPP_ADD);
                    dataMap.put("BATCH_ID", batchId);
                    dataMap.put("USER_ID", userId);
                    String errorInfo = "";
                    //错误处理
                    //判断维度编码是否为空
                    if (StringUtils.isEmpty(itemCode)) {
                        errorInfo = "维度编码为空";
                    }
                    //判断映射的源编码是否为空
                    if (StringUtils.isEmpty(MapUtils.getString(dataMap, "SRC_CODE"))) {
                        errorInfo = "原编码为空";
                    }
                    //判断维度编码是否存在
                    if (StringUtils.isEmpty(errorInfo) && !codeMap.containsKey(itemCode)) {
                        errorInfo = "维度编码不存在于此编码或者非末级";
                    }
                    //判断接口编码是否存在
                    if (StringUtils.isEmpty(errorInfo) && !intCodes.contains(MapUtils.getString(dataMap, "SRC_CODE"))) {
                        errorInfo = "接口表不存在此编码或者非末级";
                    }
                    //判断是否已经映射
                    if (StringUtils.isEmpty(errorInfo) && existMap.containsKey(itemCode)) {
                        errorInfo = "此编码已映射";
                    }
                    if (StringUtils.isNotEmpty(errorInfo)) {
                        dataMap.put("errorInfo", errorInfo);
                        errorList.add(dataMap);
                    } else {
                        rightMapList.add(dataMap);
                    }
                }
                MetaDimTabModHisDAO dao = new MetaDimTabModHisDAO();
                dao.insertBatch(rightMapList, 0, dimTableId);
            }
        } catch (Exception e) {
            Log.error(null, e);
            throw e;
        } finally {
            mappingDao.close();
        }
        return errorList;
    }

    /**
     * 生成模板下载需要的数据
     * *
     */
    public List<Map<String, Object>> getExcelTitleList(String tableName, String tablePrefix,
                                                       String tableOwner, int sysId, int dimTableId) {
        List<Map<String, Object>> mappList = new ArrayList<Map<String, Object>>();
        DimMappingDAO mappingDao = new DimMappingDAO();
        List<Map<String, Object>> dataList = mappingDao.queryDimCodeByTableName(tableName, tablePrefix, tableOwner, sysId, dimTableId);
        try {
            Map<String, Object> titleMap = new HashMap<String, Object>();
            titleMap.put("itemCode", "维度编码");
            titleMap.put("itemName", "维度名称");
            titleMap.put("srcCode", "原编码");
            titleMap.put("srcName", "原编码名称");
            Set<String> codes = new HashSet<String>();
            mappList.add(titleMap);
            if (dataList != null && dataList.size() != 0) {
                for (int i = 0; i < dataList.size(); i++) {
                    Map<String, Object> dataMap = new HashMap<String, Object>();
                    Map<String, Object> map = dataList.get(i);
                    Iterator<String> iterator = map.keySet().iterator();
                    String itemCode = MapUtils.getString(map, tablePrefix.toUpperCase() + "_CODE");
                    if (!codes.contains(itemCode)) {
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            if (key.equalsIgnoreCase(tablePrefix.toUpperCase() + "_CODE")) {
                                dataMap.put("itemCode", map.get(key));
                            }
                            if (key.equalsIgnoreCase(tablePrefix.toUpperCase() + "_NAME")) {
                                dataMap.put("itemName", map.get(key));
                            }
                        }
                        mappList.add(dataMap);
                    }
                    codes.add(itemCode);
                }
            }
        } catch (Exception e) {
            Log.error(null, e);
        } finally {
            mappingDao.close();
        }
        return mappList;
    }
}
