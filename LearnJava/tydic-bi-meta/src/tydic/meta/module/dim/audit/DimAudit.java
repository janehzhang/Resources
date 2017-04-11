package tydic.meta.module.dim.audit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tydic.frame.BaseDAO;
import tydic.frame.DataSourceManager;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.Log;
import tydic.meta.module.dim.DimConstant;
/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 王晶
 * @description 维度批量审核前台统一调用接口
 * @date 2012-02-09
 */
public class DimAudit {

    //维度审核的审核映射map,根据前台传入的字段获取维度审核的实例类
    private final static Map<Integer,IDimAuditType> DIMAUDIT_TYPE_MAP = new HashMap<Integer, IDimAuditType>();
    static{
        DIMAUDIT_TYPE_MAP.put(DimConstant.DIM_RECORD_CHANGE, new DimRecordUpdateImpl()); //维度记录修改
        DIMAUDIT_TYPE_MAP.put(DimConstant.DIM_MAPP_ADD, new DimMappingAddIpml());//新增维度映射
        DIMAUDIT_TYPE_MAP.put(DimConstant.DIM_LEVEL_CHANGE, new DimLevelUpdateImpl());//维度层级修改
        DIMAUDIT_TYPE_MAP.put(DimConstant.DIM_CODE_ADD, new DimCodeAddImpl());//新增维度
        DIMAUDIT_TYPE_MAP.put(DimConstant.DIM_CODE_STOP, new DimCodeChangeStateImpl()); //停用维度
        DIMAUDIT_TYPE_MAP.put(DimConstant.DIM_TYPE_ADD, new DimAuditTypeAddImpl());//维度编码归并申请
        DIMAUDIT_TYPE_MAP.put(DimConstant.DIM_CODE_ENABLE, new DimCodeEnableImpl());//启用维度
        DIMAUDIT_TYPE_MAP.put(DimConstant.DIM_MAPP_UPDATE,new DimMappingUpdateImpl());//修改维度映射
        DIMAUDIT_TYPE_MAP.put(DimConstant.DIM_MAPP_DELETE,new DimMappingDeleteImpl());//删除维度映射
    }
    /**
     * 维度审核的前台调用静态方法
     * @param List<Map<String,Object>> 审核数据的list map中包含了需要的审核的数据,与数据库中的一致即可,如:字段ITEM_CODE,map中的key为itemCode
     * @param auditUserId 审核人Id
     * @param auditFlag  审核的标示 1为通过 2为不通过
     * @param modMask  审核意见
     * @param extraMap 包含其他附属信息的map 如维度表名称  维度表前缀等
     *
     * */
    public static boolean dimAudit(List<Map<String,Object>> auditList,int auditUserId,int auditFlag,String modMask,Map<String,Object>extraMap){
        BaseDAO.beginTransaction();
        try{
            if(auditList!=null&&auditList.size()!=0){
                for(int i = 0 ;i<auditList.size();i++){
                    Map<String,Object> auditMap = auditList.get(i);
                    if(auditMap!=null){
                        String auditTypeStr = Convert.toString(auditMap.get("modFlag")); //审核类型,如对节点进行多种操作,中间以","隔开
                        if(auditTypeStr!=null){
                            String[]typeArr = auditTypeStr.split(",");
                            for(int j = 0;j<typeArr.length;j++){
                                String auditType = typeArr[j];
                                if(auditType!=null){
                                    int auditTypeId = Integer.valueOf(auditType);
                                    DIMAUDIT_TYPE_MAP.get(auditTypeId).dimAuditByType(auditMap,auditUserId,auditFlag,modMask,extraMap);
                                }
                            }
                        }
                    }
                }
            }
            BaseDAO.commit();
            return true;
        }catch(Exception e){
            Log.error(null,e);
            BaseDAO.rollback();
        }finally{
            DataSourceManager.destroy();
        }
        return false;
    }
}
