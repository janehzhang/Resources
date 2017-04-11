package tydic.meta.module.report.issue.Table;

import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.meta.common.Page;
import tydic.meta.module.DimUtils;
import tydic.meta.module.report.ReportConstant;
import tydic.meta.module.tbl.MetaDataSourceDAO;
import tydic.meta.module.tbl.MetaTablesDAO;
import tydic.meta.module.tbl.TblConstant;

/**
 * Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 * All rights reserved.
 * 发布数据模型类
 * @author 谭万昌
 * @date 2012-2-21
 * 
 * @modifier 李国民
 * @modifierDate：2012-04-05
 * 
 */
public class IssueDataModelAction {
	private IssueDataModelDAO issueDataDAO;
	
	private MetaTablesDAO metaTablesDAO;
	
	private MetaDataSourceDAO metaDataSourceDAO;

	public MetaDataSourceDAO getMetaDataSourceDAO() {
		return metaDataSourceDAO;
	}

	public void setMetaDataSourceDAO(MetaDataSourceDAO metaDataSourceDAO) {
		this.metaDataSourceDAO = metaDataSourceDAO;
	}

	public MetaTablesDAO getMetaTablesDAO() {
		return metaTablesDAO;
	}

	public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO) {
		this.metaTablesDAO = metaTablesDAO;
	}

	public IssueDataModelDAO getIssueDataDAO() {
		return issueDataDAO;
	}
	
	public void setIssueDataDAO(IssueDataModelDAO issueDataDAO) {
		this.issueDataDAO = issueDataDAO;
	}
	
	/**
	 * 得到数据源
	 * @return
	 */
    public List<Map<String,Object>> queryDataSource(){
    	return metaDataSourceDAO.queryDataSourceByType(TblConstant.META_DATA_SOURCE_TABLE, TblConstant.COL_STATE_VALID);
    }
    
    /**
     * 得到数据源对应用户
     * @param dataSourceId
     * @return
     */
    public List<Map<String,Object>> getUserNameByDataSourceId(int dataSourceId){
//    	List<Map<String,Object>> maps = metaTablesDAO.getUserNameListByDataSourceId(dataSourceId);
    	Map<String,Object> userInfo = metaTablesDAO.getLoginUserNameByDataSourceId(dataSourceId);
    	List<Map<String,Object>> maps = metaTablesDAO.getUserNameListByDataSourceId(dataSourceId);
//        String defaultTbSpace = metaTablesDAO.getDefaultTablespaceByDataSource(dataSourceId);
    	for(int i = 0; i < maps.size(); i++){
    		Map<String,Object> temp = maps.get(i);
    		temp.put("DEFAULT_NAME", userInfo.get("DATA_SOURCE_USER").toString().toUpperCase());
//            temp.put("DEFAULT_TABLESPACE", defaultTbSpace);
//    		break;
    	}
        return maps;
    }
	
	/**
	 * 查询已发布的模型信息
	 * @param data 查询条件
	 * @param page 分页
	 * @return
	 */
    public List<Map<String,Object>> queryDataModel(Map<?,?> data,Page page){
        if(page==null){//如果没有page，为第一页。
            page=new Page(0,20);
        }

        return issueDataDAO.queryDataModel(data,page);
    }
    
    public List<Map<String,Object>> queryDataModelByIssueId(String issueId){
    	return issueDataDAO.queryDataModelByIssueId(issueId);
    }
    
    /**
	 * 发布模型时，查询选择表类
	 * @modifier 李国民
	 * 
     * @param data 查询条件
     * @param page 分页
     * @return
     */
    public List<Map<String,Object>> queryTableType(Map<String,Object> data,Page page){
    	if(page==null){
	        page=new Page(0,20);	//按每页20条分页;
    	}
        return issueDataDAO.queryTableType(data,page);
    }
    
	/**
	 * 查询表类字段信息（发布数据模型时字段）
	 * 
	 * @modifier 李国民
	 * @param tableId	表类id
	 * @return	list
	 */
    public List<Map<String,Object>> queryTableTypeColInfo(String tableId){
        return issueDataDAO.queryTableTypeColInfo(tableId);
    }
	
	/**
	 * 查询已发布数据模型的字段信息
	 * @param tableId
	 * @return
	 */
    public List<Map<String,Object>> queryPublishColInfo(String issueId){
    	return issueDataDAO.queryPublishColInfo(issueId);
    }
    
    /**
     * 修改模型时，查询对应的模型字段
	 * @modifier 李国民
	 * 
     * @param tableId 表类id
     * @param issueId 模型id
     * @return
     */
    public List<Map<String,Object>> queryModifyPublishColInfo(String tableId,String issueId){
    	List<Map<String,Object>> colsList = issueDataDAO.queryModifyPublishColInfo(tableId,issueId);
    	return colsList;
    }
    
    /**
     * 得到对应维度值
	 * @modifier 李国民
	 * 
     * @param dimTableId 维度表id
     * @param dimTypeId 维度归并类型id
     * @param dimLevel 维度层级
     * @return
     */
    public List<Map<String,Object>> queryDimCodes(String dimTableId,String dimTypeId,String dimLevel){
        return issueDataDAO.queryDimCodes(dimTableId,dimTypeId,dimLevel);
    }
    
    /**
     * 异步动态加载维度数据
     * @author 李国民
     * 
     * @param data {dimTableId：维度表id，dimTypeId：归并类型id，dimLevel：维度值层级id}
     * @param map {id：tree当前选中id}
     * @return
     */
    public List<Map<String,Object>> queryCodesByNode(Map<String,Object> data, Map<String,Object> map){
    	long dimTableId = Long.parseLong(data.get("dimTableId").toString());
    	long dimTypeId = Long.parseLong(data.get("dimTypeId").toString());
    	long parId = Long.parseLong(map.get("id").toString());
    	String dimLevels [] = data.get("dimLevel").toString().split(",");
    	int maxLevel = Integer.parseInt(dimLevels[dimLevels.length-1]);
    	List<Map<String,Object>> rs = DimUtils.queryChildDimData(dimTableId, dimTypeId, parId, null, maxLevel);
    	return rs;
    }
    
    /**
     * 发布数据模型
	 * @modifier 李国民
	 * 
	 * @param data
	 * @return true和false
     */
    public boolean insertPublishDataModel(Map<?,?> data){
        try{
            BaseDAO.beginTransaction();
            //发布数据模型主表
            int issueId = issueDataDAO.insetIssueConfig(data);
            //发布数据模型从表--数据审核范围表
            issueDataDAO.insertDateAuditCfg(data,issueId);
            //发布数据模型字段列信息
            List<Map<String,Object>> colData = (List<Map<String, Object>>) data.get("colData");
            issueDataDAO.insertPublishDataModelColInfo(colData,issueId);
            //添加发布数据模型日志
            issueDataDAO.insertPublishDataModelLog(data,issueId,ReportConstant.REPORT_ISSUE_LOG_INSERT_TYPE);

            BaseDAO.commit();
            return true;
        } catch(Exception e){
            Log.error(null, e);
            BaseDAO.rollback();
            return false;
        }
    }
    
	/**
	 * 修改数据模型
	 * @modifier 李国民
	 * 
	 * @param data
	 * @return
	 */
    public boolean updatePublishDataModel(Map<String,Object> data){
    	try{
    		BaseDAO.beginTransaction();
    		int issueId = Integer.parseInt(data.get("issueId").toString());
    		//修改数据模型主表
    		issueDataDAO.updateIssueConfig(data);
    		//修改数据模型从表--数据审核范围表
    		issueDataDAO.updateDateAuditCfg(data);
            //新增数据模型字段列信息
            List<Map<String,Object>> colData = (List<Map<String, Object>>) data.get("colData");
            issueDataDAO.insertPublishDataModelColInfo(colData,issueId);
            //修改数据模型字段列信息
            List<Map<String,Object>> updateColData = (List<Map<String, Object>>)data.get("updataColData");
    		issueDataDAO.updatePublishDataModelColInfo(updateColData);
            //添加发布数据模型日志
            issueDataDAO.insertPublishDataModelLog(data,issueId,ReportConstant.REPORT_ISSUE_LOG_UPDATE_TYPE);
            //删除为在报表创建使用且已取消的字段
            List<Map<String,Object>> delCols = (List<Map<String, Object>>)data.get("delCols");
            issueDataDAO.delPublishDataModelColInfo(delCols);
			BaseDAO.commit();
			return true;
	    } catch(Exception e){
	        Log.error(null, e);
	        BaseDAO.rollback();
	        return false;
	    }
    }
	
    /**
     * 判断数据别名是否存在
     * @param tableAlias 数据别名
     * @return
     */
    public boolean validateName(String tableAlias){
    	return issueDataDAO.validateName(tableAlias);
    }
}
