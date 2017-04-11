package tydic.meta.module.dim.merge;

import java.util.*;

import org.apache.poi.util.StringUtil;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.Common;
import tydic.meta.common.Constant;
import tydic.meta.module.dim.MetaDimTabModHisDAO;
import tydic.meta.module.tbl.MetaTableColsDAO;
import tydic.meta.module.tbl.TblConstant;

/**
 * 批量导入编码的业务层类
 */
public class CodeUpLoadService {

    /**
     * 将读取到的数据进行封装,形成树插入历史表中
     *
     * @param dataList    数据集合
     * @param dimTableId  维度表id
     * @param tableName   维度表名称
     * @param tablePrefix 维度表前缀
     * @param tableOwner  表用户
     * @param dimTypeId   当前归并类型的id
     * @param dynCols     动态字段集合
     * @param batchId     批次id
     * @param userId      用户id
     */
    public List<CodeBean> insertCodeData(List<Map<String, Object>> dataList, int dimTableId, String tableName,
                                         String tablePrefix, String tableOwner, int dimTypeId, List<Map<String, Object>> dynCols, String batchId, int userId) {
        DimMergeDAO dimMergeDAO = new DimMergeDAO();
        List<CodeBean> errorCodeList = null;
        MetaDimTabModHisDAO metaDimTabModHisDAO = new MetaDimTabModHisDAO();
        Set<String> notNullColumn = new HashSet<String>();
        //非空字段处理 
        for (Map<String, Object> data : dynCols) {
            if (MapUtils.getIntValue(data, "COL_NULLABLED") == TblConstant.COL_STATE_NULLABELD_NO) {
                notNullColumn.add(MapUtils.getString(data, "COL_NAME"));
            }
        }
        try {
            List<CodeBean> codeList = null;
            Map<String, String> codeMap = null;
            Map<String, DimBean> dimCodeMap = null;
            Map<String, Long> parCodeMap = null;
            Map<String, Integer> levelMap = null;
            Map<String, CodeBean> codeBeanMap = new HashMap<String, CodeBean>();//处理子节点已经形成,但是父节点还没有形成的节点,待全部节点完成后,更新节点的par_id
            if (dataList != null && dimTableId != 0 && dimTypeId != 0) {
                codeList = new ArrayList<CodeBean>();
                errorCodeList = new ArrayList<CodeBean>();
                parCodeMap = new HashMap<String, Long>();
                levelMap = new HashMap<String, Integer>();
                codeMap = new HashMap<String, String>();
                dimCodeMap = dimMergeDAO.queryCodeByDimTypeId(tableName, tablePrefix.toUpperCase(), tableOwner, dimTypeId);
                String itemCode = null;
                String parCode = null;
                for (int i = 1; i < dataList.size(); i++) {
                    Map<String, Object> dynColMap = new HashMap<String, Object>();
                    CodeBean codeBean = new CodeBean();
                    codeBean.setDimTableId(dimTableId);
                    codeBean.setDimTypeId(dimTypeId);
                    codeBean.setBatchId(batchId);
                    codeBean.setDynColMap(dynColMap);
                    codeBean.setUserId(userId);
                    Map<String, Object> map = dataList.get(i);
                   if(map!=null&&map.size()>1){
	                    Iterator<String> iterator = map.keySet().iterator();
	                    while (iterator.hasNext()) {
	                        String key = iterator.next();
	                        if (key.startsWith("item") || key.startsWith("par")) {
	                            if (key.equalsIgnoreCase("itemCode")) {
	                                String value = map.get(key).toString();
	                                if (!codeMap.containsKey(value) && !dimCodeMap.containsKey(value)) {
	                                    long itemId = 0 - System.currentTimeMillis() + i;
	                                    codeBean.setItemId(itemId);
	                                    itemCode = map.get(key).toString();
	                                    codeBean.setItemCode(itemCode);
	                                    parCodeMap.put(itemCode, itemId);
	                                    codeMap.put(itemCode, itemCode);
	                                    codeList.add(codeBean);
	                                } else if (codeMap.containsKey(value) && !dimCodeMap.containsKey(value)) {
	                                    codeBean.setItemCode(value);
	                                    codeBean.setErrorInfo("上传的文件中包含了此编码");
	                                   // errorCodeList.add(codeBean);
	                                } else {
	                                    codeBean.setItemCode(value);
	                                    codeBean.setErrorInfo("维度表中包含了此编码");
	                                    //errorCodeList.add(codeBean);
	                                }
	                            }
	                            if (key.equalsIgnoreCase("itemName")) {
	                                codeBean.setItemName(map.get(key).toString());
	                            }
	                            if (key.equalsIgnoreCase("itemDesc")) {
	                                codeBean.setItemDesc(map.get(key).toString());
	                            }
	                            if (key.equalsIgnoreCase("parCode")) {
	                                parCode = map.get(key).toString();
	                                if (parCode != null && parCode != "") {
	                                    codeBean.setItemParCode(parCode);
	                                    if (parCode.equals("0")) {
	                                        codeBean.setDimLevel(1);
	                                        codeBean.setParId(Long.valueOf("0"));
	                                        levelMap.put(map.get("itemCode").toString(), 1);
	                                    }
	                                    if (!parCode.equals("0") && !parCodeMap.containsKey(parCode) && dimCodeMap.containsKey(parCode)) {
	                                        //处理当前父不为0,为本身的维度表中的记录时
	                                        DimBean dim = dimCodeMap.get(parCode);
	                                        codeBean.setParId(dim.getItemId());
	                                        levelMap.put(map.get("itemCode").toString(), dim.getLevel() + 1);
	                                        codeBean.setDimLevel(dim.getLevel() + 1);
	                                    }
	                                    if (!parCode.equals("0") && parCodeMap.containsKey(parCode) && !dimCodeMap.containsKey(parCode)) {
	                                        //处理新上传的,父id能够在已经形成的
	                                    	System.out.println(parCode);
	                                        Long parId = parCodeMap.get(parCode);
	                                        codeBean.setParId(parId);
	                                        levelMap.put(map.get("itemCode").toString(), levelMap.get(parCode) + 1);
	                                        codeBean.setDimLevel(levelMap.get(parCode) + 1);
	                                    }
	                                }
	                            }
	                        } else {
	                            //判读必填字段的非空
	                            Object value=map.get(key);
	                            if (notNullColumn.contains(key)&&(value==null||StringUtils.isEmpty(value.toString()))) {
	                                codeBean.setErrorInfo("非空字段【" + key + "】输入空值");
	//                                errorCodeList.add(codeBean);
	                            }
	                            String inputValue = "colName:" + key + ",value:" + value;
	                            dynColMap.put(key, inputValue);
	                        }
	                     }
                
                    //判断编码，父编码，编码名称不能为NULL
                    if (StringUtils.isEmpty(codeBean.getItemCode())) {
                        codeBean.setErrorInfo("编码为空");
                        //errorCodeList.add(codeBean);
                    }
                    if (StringUtils.isEmpty(codeBean.getItemName())) {
                        codeBean.setErrorInfo("编码名称为空");
                        //errorCodeList.add(codeBean);
                    }
                    if (StringUtils.isEmpty(codeBean.getItemParCode())) {
                        codeBean.setErrorInfo("父编码为空");
                        //errorCodeList.add(codeBean);
                    }
                    //进行错误判断
                    if(StringUtils.isNotEmpty(codeBean.getErrorInfo())){
                        errorCodeList.add(codeBean);
                        continue;
                    }
                    codeBeanMap.put(map.get("itemCode").toString(), codeBean);
                 }
               }
            }
            try {
                //循环数据，设置其父ID
                for(CodeBean code:codeList){
                    if(parCodeMap.containsKey(code.getItemParCode())){
                        code.setParId(parCodeMap.get(code.getItemParCode())); 
                    }else{
                        if(dimCodeMap.containsKey(code.getItemParCode())){
                            code.setParId(dimCodeMap.get(code.getItemParCode()).getItemId());
                        }else{
                            code.setParId((long)Constant.DEFAULT_ROOT_PARENT);
                        }
                    }
                }
                metaDimTabModHisDAO.insertBatchByBean(codeList, dynCols.size(), dimTableId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            Log.error(null, e);
        } finally {
            dimMergeDAO.close();
            metaDimTabModHisDAO.close();
        }
        return errorCodeList;
    }

    /**
     * 查询当前维度表中的动态字段,用于生成下载模板的表头
     *
     * @param tableId
     * @param tablePrefix
     * @param tableOwner
     * @return
     */
    public Map<String, Object> getExcelTitleList(int tableId, String tablePrefix, String tableOwner) {
        MetaTableColsDAO metaTableColsDAO = new MetaTableColsDAO();
        try {
            Map<String, Object> titleMap = new HashMap<String, Object>();
            titleMap.put("itemCode", "维度编码");
            titleMap.put("itemName", "编码名称");
            titleMap.put("itemDesc", "编码描述");
            titleMap.put("parCode", "父编码");
            List<Map<String, Object>> maps = metaTableColsDAO.queryDimTableDycCols(tableId);
            if (maps != null && maps.size() > 0) {
                for (int i = 0; i < maps.size(); i++) {
                    Map<String, Object> cols = maps.get(i);
                    String colName = MapUtils.getString(cols, "COL_NAME");
                    int isCanNull = MapUtils.getInteger(cols, "COL_NULLABLED");
                    String title = MapUtils.getString(cols, "COL_NAME_CN");
                    if (StringUtils.isEmpty(title)) {
                        title = colName;
                    }
                    //不允许为空
                    if (isCanNull == TblConstant.COL_STATE_NULLABELD_NO) {
                        title += "(此列不允许为空)";
                    }
                    titleMap.put(colName, title);
                }
            }
            return titleMap;
        } catch (Exception e) {
            Log.error(null, e);
        } finally {
            metaTableColsDAO.close();
        }
        return null;
    }
}
