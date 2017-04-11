package tydic.meta.module.dim.audit;

import tydic.meta.common.Page;
import tydic.meta.module.tbl.MetaTableColsDAO;
import tydic.meta.module.tbl.MetaTablesDAO;
import tydic.meta.web.session.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author: 程钰
 * @description 审核维度控制类
 * @date: 12-1-31
 * @time: 上午9:56
 */
public class DimAuditAction {
    private DimAuditDAO dimAuditDAO;
    private MetaTablesDAO metaTablesDAO;
    private MetaTableColsDAO metaTableColsDAO;

    /**
     * 按照条件查询维度表各个
     * */
    public List<Map<String,Object>> queryDimTableDataByCondition(Map<String,Object> map,Page page){
        if(page==null){
            page = new Page(0,20);
        }

        return dimAuditDAO.queryDimTableByCondition(map, page);

    }
    /**
     * 查询维度数
     */
//    public List<Map<String, Object>> queryDimTree(int dimTableId){
//        //根据dimTableId 获取维度表信息
//        Map dimTableInfo = dimAuditDAO.queryDimTableInfo(dimTableId);
//        return dimAuditDAO.queryDimTree(dimTableInfo.get("TABLE_NAME").toString());
//    }


    
    /**
     * 查询
     * @param tableId
     * @return
     */

    public List<Map<String,Object>> queryDimTreeData(String tableName,String dimTablePrefix ,String tableOwner,String dimTypeId,String batchId,List<String> dycols){
        int dimypeId = Integer.parseInt(dimTypeId);
        String hisdyColums = "";
        List<Map<String,Object>> dimColData = null;
        if(dycols != null && dycols.size() > 0){
            dimColData = dimAuditDAO.queryDimCols(tableName, dimTablePrefix,tableOwner,batchId, dimypeId, hisdyColums, dycols);
        }
        List<Map<String,Object>> dimPathData = dimAuditDAO.queryDimPathData(tableName, dimTablePrefix,tableOwner,dimypeId, batchId);
    	List<Map<String,Object>> dimDataList = dimAuditDAO.queryDimTreeData(tableName,dimTablePrefix,tableOwner,dimypeId,batchId);

    	//处理动态字段和动态字段的值
    	if(dimColData != null && dimColData.size() > 0){
    		for(Map<String,Object> dimData:dimDataList){
    			if(dimData.get("HIS").toString().equals("1")){
    				for(int i = 0;i < dimColData.size();i++){
        				if(dimData.get(dimTablePrefix.toUpperCase() + "_ID").toString().equals(dimColData.get(i).get("ITEM_ID").toString()) &&  dimColData.get(i).get("DATA_TYPE").toString().equals("HIS") ){
        					for(String cols:dycols){
        						if(dimColData.get(i).get(cols.toUpperCase()) != null &&! dimColData.get(i).get(cols.toUpperCase()).toString().equals("")){
            						String[] colContent = dimColData.get(i).get(cols.toUpperCase()).toString().split(",");
            						String[] cilValue = colContent[1].split(":");
            						if(cilValue.length > 1 && !cilValue[1].equals("null") ){
            							dimData.put(colContent[0].split(":")[1], cilValue[1]);
            						}else{
            							dimData.put(cols.toUpperCase(), "");
            						}
        						}
        					}
        				}
        			}
    			}else if(dimData.get("HIS").toString().equals("3")){
    				for(int i = 0;i < dimColData.size();i++){
        				if(dimData.get(dimTablePrefix.toUpperCase() + "_ID").toString().equals(dimColData.get(i).get("ITEM_ID").toString()) &&  dimColData.get(i).get("DATA_TYPE").toString().equals("DIM") ){
        					for(String cols:dycols){
        						if(dimColData.get(i).get(cols.toUpperCase()) != null && !dimColData.get(i).get(cols.toUpperCase()).toString().equals("")){
        							dimData.put(cols, dimColData.get(i).get(cols.toUpperCase()));
        						}else{
            						dimData.put(cols.toUpperCase(), "");
            					}
        					}
        				}
        			}
    			}
    		}
    	}
    	
    	//把父节点添加到查询数据中
    	for(int i = 0; i < dimPathData.size(); i++){
    		String pathName = dimPathData.get(i).get("PATH_NAME").toString();
    		String pathId = dimPathData.get(i).get(dimTablePrefix.toUpperCase() + "_ID").toString();
    		for(Map<String,Object> dimData:dimDataList){
    			if(dimData.get(dimTablePrefix.toUpperCase() + "_ID").toString().equals(pathId) && !dimData.get("MOD_FLAG").toString().equals("4")
    					&& !dimData.get("MOD_FLAG").toString().equals("-1")){
    				dimData.put("PATH_NAME", pathName);
    				
    			}
    		}
    	}
        return dimDataList;
    }
    
    /**
     * 查询维度审核数据
     */
    public List<Map<String, Object>> queryDimInfoData(String tableName,String dimTablePrefix ,String tableOwner,String dimTypeId,String batchId,List<String> dycols){
    	int dimypeId = Integer.parseInt(dimTypeId);
    	List<Map<String,Object>> dimDataList = dimAuditDAO.queryDimInfoData(tableName, dimTablePrefix, tableOwner,dimypeId, batchId);
    	List<Map<String,Object>> dimPathData = dimAuditDAO.queryDimPathInfo(tableName, dimTablePrefix, tableOwner,dimypeId, batchId);
    	Map<String,Map<String,Object>> dimFlag = new HashMap<String, Map<String,Object>>();
    	for(int i = 0; i < dimPathData.size(); i++){
    		String pathName = dimPathData.get(i).get("PATH_NAME").toString();
    		String pathId = dimPathData.get(i).get(dimTablePrefix.toUpperCase() + "_ID").toString();
    		for(Map<String,Object> dimData:dimDataList){
    			if(dimData.get(dimTablePrefix.toUpperCase() + "_ID").toString().equals(pathId) && !dimData.get("HIS").toString().equals("4")
    					&& !dimData.get("MOD_FLAG").toString().equals("-1")){
    				dimData.put("PATH_NAME", pathName);
    			}
    			if(!dimData.get("MOD_FLAG").toString().equals("0")){
    				dimFlag.put(dimData.get(dimTablePrefix.toUpperCase() + "_ID").toString(), dimData);
    			}
    		}
    	}
		for(Map<String,Object> dimData:dimDataList){
			if(dimFlag.containsKey(dimData.get(dimTablePrefix.toUpperCase() + "_ID").toString()) && dimData.get("MOD_FLAG").toString().equals("0") ){
				dimData.put("MOD_FLAG", dimFlag.get(dimData.get(dimTablePrefix.toUpperCase() + "_ID").toString()).get("MOD_FLAG"));
				dimData.put("USER_NAMECN", dimFlag.get(dimData.get(dimTablePrefix.toUpperCase() + "_ID").toString()).get("USER_NAMECN"));
				
			}
			
		}
        return dimDataList;
    }
    
    

    /**
     * 根据维度表表类ID查询其有效表类的列信息
     * @param tableId
     * @return
     */
    public List<Map<String,Object>> queryColDatas(int tableId){
        int tableVersion= metaTablesDAO.queryValidVersion(tableId);
        return  metaTableColsDAO.queryMetaTableColsByTableId(tableId,tableVersion,null);
    }


    /**
     * 查询某批次改动涉及到的归并类型
     * @param batchId  批次号
     * @param isAudit 是否已经审核，如果为true表示查询通过审核的所有归并类型，如果是false表示查询某批次改动涉及
     * 到的所有归并类型
     * @author 张伟
     * @return
     */
    public List<Map<String,Object>> queryDimTypeOnModify(String batchId,boolean isAudit){
        return dimAuditDAO.queryDimTypeOnModify(batchId,isAudit);
    }

    /**
     * 审核维度编码
     * @param auditData 审核数据，见
     * @param auditFlag
     * @param auditMark
     * @param dimTableInfo
     * @return
     */
    public boolean audit(List<Map<String,Object>> auditData,int auditFlag,String auditMark,Map<String,Object> dimTableInfo){
        int auditUserId= SessionManager.getCurrentUserID();
        return DimAudit.dimAudit(auditData,auditUserId,auditFlag,auditMark,dimTableInfo);
    }

    /**
     * 实例化
     * @param dimAuditDAO
     */
    public void setDimAuditDAO(DimAuditDAO dimAuditDAO) {
        this.dimAuditDAO = dimAuditDAO;
    }

    public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO){
        this.metaTablesDAO = metaTablesDAO;
    }

    public void setMetaTableColsDAO(MetaTableColsDAO metaTableColsDAO){
        this.metaTableColsDAO = metaTableColsDAO;
    }
}
