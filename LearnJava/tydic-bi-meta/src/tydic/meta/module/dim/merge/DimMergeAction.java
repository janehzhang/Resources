package tydic.meta.module.dim.merge;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.module.dim.MetaDimTabModHisDAO;
import tydic.meta.module.tbl.MetaDataSourceDAO;
import tydic.meta.module.tbl.MetaDimTablesDAO;
import tydic.meta.module.tbl.MetaDimTypeDAO;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 编码归并Action <br>
 * @date 2011-11-09
 */
public class DimMergeAction{

    private DimMergeDAO dimMergeDAO;

    private MetaDimTablesDAO metaDimTablesDAO;

    private MetaDimTabModHisDAO metaDimTabModHisDAO;

    private MetaDataSourceDAO metaDataSourceDAO;

    private MetaDimTypeDAO metaDimTypeDAO;

    /**
     *查询具体维表归并数据
     * @param queryMessage  包含键值dimType：归并类型，dimTypeStatus：归并状态,keyWord:关键词。
     * @param dimTableName 维度表名称
     * @param dynamicCols   动态字段集
     * @param dimTablePrefix 维度表前缀
     * @return
     */
    public List<Map<String,Object>>  queryDimData(Map<String,Object> queryMessage,String dimTableName
            ,List<String> dynamicCols,String dimTablePrefix,String tableUser){
        //归并状态
        int dimTypeStatus = MapUtils.getIntValue(queryMessage,"dimTypeStatus",0);
        int lastLevelFlag = MapUtils.getIntValue(queryMessage,"lastLevelFlag",0);
        int dataSourceId = MapUtils.getIntValue(queryMessage,"dataSourceId");
        Map<String,Object> dataSource = metaDataSourceDAO.queryDataSourceById(dataSourceId);
        switch(dimTypeStatus){
            case 0:{//所有
                List<Map<String,Object>> rtn = dimMergeDAO.queryAllDimData(Integer.parseInt(queryMessage.get("dimType").toString()),dimTableName,dynamicCols,dimTablePrefix,tableUser
                        , Convert.toString(queryMessage.get("keyWord")),lastLevelFlag,dataSource);
                for(Map<String,Object> m:rtn){
                    for(String key:m.keySet()){
                        if(m.get(key)!=null){
                            m.put(key, Convert.toString(m.get(key)));
                        }
                    }

                }
                return rtn;
            }
            case 1:{//尚未归并。
                List<Map<String,Object>> rtn =  dimMergeDAO.queryNotMergeDimData(Integer.parseInt(queryMessage.get("dimType").toString()),dimTableName,dynamicCols,dimTablePrefix,tableUser
                        , Convert.toString(queryMessage.get("keyWord")),lastLevelFlag,dataSource);
                for(Map<String,Object> m:rtn){
                    for(String key:m.keySet()){
                        if(m.get(key)!=null){
                            m.put(key, Convert.toString(m.get(key)));
                        }
                    }

                }
                return rtn;
            }
            case 2:{//已归并
                List<Map<String,Object>> rtn =  dimMergeDAO.queryAlreadyMergeDimData(Integer.parseInt(queryMessage.get("dimType").toString()),dimTableName,dynamicCols,dimTablePrefix,tableUser
                        , Convert.toString(queryMessage.get("keyWord")),lastLevelFlag,dataSource);
                for(Map<String,Object> m:rtn){
                    for(String key:m.keySet()){
                        if(m.get(key)!=null){
                            m.put(key, Convert.toString(m.get(key)));
                        }
                    }

                }
                return rtn;
            }
        }
        return null;
    }

    /**
     * 提交审核
     * @param auditData   审核数据，数据结构如下
     * {
     *     oderData:{1:98,2:-2}，//排序数据,key表示维表ID值，value表示ORDER值。
     *     dimInfo:{dimTableId,tableName,tablePrefixName...},维表信息
     *     modifyData://修改数据，数据结构与META_DIM_TAB_MOD_HIS一致。
     *     dymaicCloCount:1 动态字段数目
     * }
     * @return
     */
    public  boolean sumbitAudit(Map<String,Object> auditData){
    	//提交之前先进行验证该表的该归并类型下是否有数据没有审核,如果有审核的数据则提示
    	Map<String,Object> dimInfo=(Map<String,Object>)auditData.get("dimInfo");
    	int dimTableId = Integer.parseInt(dimInfo.get("dimTableId").toString());//当前维度表的id
    	int dimTypeId =Convert.toInt(auditData.get("dimTypeId")); //维度表当前归并类型的id
        //维表信息
    	int auditCount = dimMergeDAO.queryUnAuditedCount(dimTableId, dimTypeId);
    	if(auditCount>0){
    		return false;
    	}else{
	        String tableName=dimInfo.get("tableName").toString();
	        String tablePrefixName=dimInfo.get("tableDimPrefix").toString();
	        String tableOwner = dimInfo.get("tableOwner").toString();
	        
	        //排序数据
	        @SuppressWarnings("unchecked")
	        Map<String ,Long> orderData=(Map<String ,Long>)auditData.get("orderData");
	        @SuppressWarnings("unchecked")
	        List<Map<String,Object>> modifyData=(List<Map<String,Object>>)auditData.get("modifyData");
	        int dymaicCloCount=Integer.parseInt(auditData.get("dymaicCloCount").toString());
	        //排序数据无需审核，直接提交。
	        try{
	            BaseDAO.beginTransaction();
	            if(orderData!=null&&orderData.size()>0){
	                dimMergeDAO.order(orderData,tableName,tablePrefixName,tableOwner);
	            }
	            if(modifyData!=null&&modifyData.size()>0){
	                metaDimTabModHisDAO.insertBatch(modifyData,dymaicCloCount,dimTableId);
	            }
	            BaseDAO.commit();
	        } catch(Exception e){
	            BaseDAO.rollback();
	            tydic.frame.common.Log.error(null,e);
	            return false;
	        }
    	}
        return true;
    }

    /**
     * 查询子集节点
     * @param tableName
     * @param dimTablePrefix
     * @param dynamicCols
     * @param parId
     * @return
     */
    public List<Map<String,Object>>  querySub(String tableName,String dimTablePrefix,List<String> dynamicCols,long parId,String tableOwner){
        List<Map<String,Object>> rtn = dimMergeDAO.querySub(tableName,dimTablePrefix,tableOwner,dynamicCols,parId);
        for(Map<String,Object> m:rtn){
            for(String key:m.keySet()){
                if(m.get(key)!=null){
                    m.put(key, Convert.toString(m.get(key)));
                }
            }

        }
        return rtn;
    }

    /**
     * 维表层级字段数据纠错或者叫做整理
     * @param tableName
     * @param tableDimPrefix
     * @return
     */
    public boolean dealDimLevel(String tableName,String tableDimPrefix,String tableOwner) {
        try{
            BaseDAO.beginTransaction();
            dimMergeDAO.dealLevel(tableName,tableDimPrefix,tableOwner);
            BaseDAO.commit();
        }catch(Exception e){
            BaseDAO.rollback();
            Log.error(null,e);
            return false;
        }
        return true;
    }

    /**
     * 判断是否存在某个维度表
     * @param tableName
     * @return
     */
    public boolean isExitsTable(String tableName,String tableUser){
        return  dimMergeDAO.isExitsTable(tableName,tableUser);
    }

    /**
     * 对于某个节点编码，查询其所属的所有归并类型。
     * @param tableId
     * @param dimCode
     * @param tableName
     * @return
     */
    public List<Map<String,Object>> queryDimTypesByCode(int tableId,String tableName,String tableDimPrefix,String dimCode,String tableOwner){
        return dimMergeDAO.queryDimTypesByCode(tableId,tableName,tableDimPrefix,tableOwner,dimCode);
    }
    /**
     * 获取归并类型的下某节点的完整路径
     * @author 王晶
     * @param tableName 表名
     * @param tableDimPrefix 维度前缀
     * @param dimCode 维度编码
     * @param typeId 归类类型Id
     * */
    public String queryPathNameByCode (String tableName,String tableDimPrefix,String tableOwner,String dimCode,int typeId){
    	 return dimMergeDAO.queryPathNameByCode(tableName,tableDimPrefix,tableOwner,dimCode,typeId);
    }
    /**
     * 根据编码查询所有归并类型的数据
     * @param code
     * @param tableName
     * @param tableDimPrefix
     * @return
     */
    public List<Map<String,Object>> queryDimDataByCode(String code,String tableName,String tableOwner,String tableDimPrefix,List<String> dynamicCols){
        return dimMergeDAO.queryDimDataByCode(code, tableName, tableDimPrefix,tableOwner, dynamicCols);
    }

    /**
     * 查找完整的维表完整的树形结构，其归并类型ID不能为NULL，状态为有效，归并类型同样为有效。
     * @param tableName
     * @param dimTablePrefix
     * @param dynamicCols
     * @return
     */
    public List<Map<String,Object>> queryCompleteTreeData(String tableName,String dataSourceId,String dimTablePrefix,String tableOwner,int lasetLevelFlag, List<String> dynamicCols){
        Map<String, Object> dataSource = metaDataSourceDAO.queryDataSourceById(Integer.parseInt(dataSourceId));
        return dimMergeDAO.queryCompleteTreeData(tableName,dimTablePrefix,tableOwner,dynamicCols,lasetLevelFlag,dataSource);
    }

    /**
     * 查询关联维表字段指定层级的数据。
     * @param dimTableId
     * @param level
     * @param dimType
     * @return 
     */
    public List<Map<String,Object>> queryRefDimData(int dimTableId, String tableOwner,int level,int dimType){
        //根据tableId查询表名
        Map<String,Object> dimTableInfo= metaDimTablesDAO.queryMetaDimTablesByTableId(dimTableId);
        //然后查询关联的字段信息
        return dimMergeDAO.queryRefDimData(Convert.toString(dimTableInfo.get("TABLE_DIM_PREFIX")),tableOwner,
                level,dimType,Convert.toString(dimTableInfo.get("TABLE_NAME")));
    }

    /**
     * 根据编码查询该编码以及归并的所有归并类型，便于前台展示。
     * @param tableName
     * @param dimTablePrefix
     * @param code
     * @return
     */
    public List<Map<String,Object>>  queryCodeDimTypeInfo(String tableName,String dimTablePrefix,String tableOwner,String code){
        return dimMergeDAO.queryCodeDimTypeInfo(tableName,dimTablePrefix,tableOwner,code);
    }

    /**
     * 修改编码时，是否重复修改。
     * @param code 修改后的code
     * @param id 编码归并ID
     * @param tableName 表名
     * @param tableDimPrefix 前缀
     * @param dynamicCols 字段
     * @return
     */
    public boolean valiHasCode(String code,String id ,String tableName,String tableDimPrefix,String tableOwner,List<String> dynamicCols){
        boolean flag = false;
        List<Map<String, Object>> mapList = dimMergeDAO.valiHasCode(code, id, tableName, tableDimPrefix,tableOwner, dynamicCols);
        if(mapList.size() == 0){
            flag = true;
        }
        return flag;
    }

    /**
     * 根据父ID查询其父ID下的所有子节点
     * @param tableName  表名称
     * @param dimTablePrefix  表前缀
     * @param dynamicCols  动态字段
     * @param parId  父节点
     * @return
     */
    public List<Map<String,Object>> queryAllSub(String tableName,String dimTablePrefix,String tableOwner,List<String> dynamicCols,long parId){
        return  dimMergeDAO.queryAllSub(tableName,dimTablePrefix,tableOwner,dynamicCols,parId);
    }

    /**
     * 对于选择的维度，是否所有的已通过审核
     * @param dimTableId
     * @return 返回true表示所有已经通过审核，false表示有未通过审核
     */
    public boolean isAllAudit(int dimTableId,int dimTypeId){
       return dimMergeDAO.queryUnAuditedCount(dimTableId,dimTypeId)==0;
    }

    /**
     * 判断当前历史操作表(META_DIM_TAB_MOD_HIS)的动态字段数有多少个,用于在当前批量导入判断当前文档的动态字段数是否超出
     * 超出则提示,如果超出则不提示
     * */
	public int quertDynColCount(){
		return metaDimTabModHisDAO.quertDynColCount();
	} 
	
    public void setMetaDimTablesDAO(MetaDimTablesDAO metaDimTablesDAO){
        this.metaDimTablesDAO = metaDimTablesDAO;
    }

    public void setDimMergeDAO(DimMergeDAO dimMergeDAO){
        this.dimMergeDAO = dimMergeDAO;
    }

    public void setMetaDimTabModHisDAO(MetaDimTabModHisDAO metaDimTabModHisDAO){
        this.metaDimTabModHisDAO = metaDimTabModHisDAO;
    }

    public void setMetaDataSourceDAO(MetaDataSourceDAO metaDataSourceDAO){
        this.metaDataSourceDAO = metaDataSourceDAO;
    }

    public void setMetaDimTypeDAO(MetaDimTypeDAO metaDimTypeDAO) {
        this.metaDimTypeDAO = metaDimTypeDAO;
    }
}
