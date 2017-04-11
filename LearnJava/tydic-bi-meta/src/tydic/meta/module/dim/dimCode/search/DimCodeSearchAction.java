package tydic.meta.module.dim.dimCode.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.Constant;
import tydic.meta.module.DimUtils;
import tydic.meta.module.tbl.MetaDimTablesDAO;
import tydic.meta.module.tbl.MetaTableColsDAO;

/**
 * 维度值编码查询Action
 * @author tanwc
 *
 */
public class DimCodeSearchAction {
	DimCodeSearchDAO dimCodeSearchDAO;
	MetaTableColsDAO metaTableaColsDAO;
	MetaDimTablesDAO metaDimTablesDAO;
	
	public DimCodeSearchDAO getDimCodeSearchDAO() {
		return dimCodeSearchDAO;
	}

	public void setDimCodeSearchDAO(DimCodeSearchDAO dimCodeSearchDAO) {
		this.dimCodeSearchDAO = dimCodeSearchDAO;
	}
	
	public MetaTableColsDAO getMetaTableaColsDAO() {
		return metaTableaColsDAO;
	}

	public void setMetaTableaColsDAO(MetaTableColsDAO metaTableaColsDAO) {
		this.metaTableaColsDAO = metaTableaColsDAO;
	}
	
	public List<Map<String,Object>> queryDimCode(Map<?,?> data) {
		ArrayList<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		String 	tableName 	= data.get("tableName").toString();
		String  tableOwner 	= data.get("tableOwner").toString();
		String 	preName 	= data.get("preName").toString();
		long 	dimTypeId	= Long.parseLong(data.get("dimTypeId").toString());
		String keyword		= data.get("keyword").toString();
		int dataSourceId = Integer.parseInt(data.get("dataSourceId").toString());
		if(keyword.isEmpty()) {
			result =  (ArrayList<Map<String, Object>>) dimCodeSearchDAO.queryDimCode(tableName,tableOwner,preName,Constant.DEFAULT_ROOT_PARENT,dimTypeId,keyword,dataSourceId);
		}else {
			result =  (ArrayList<Map<String, Object>>) dimCodeSearchDAO.queryDimCodeByKeyword(tableName,tableOwner,preName,dimTypeId,keyword,dataSourceId);
		}
        for(Map<String,Object> m:result){
            for(String key:m.keySet()){
                if(m.get(key)!=null){
                    m.put(key, Convert.toString(m.get(key)));
                }
            }
        }
		return result;
	}
	public List<Map<String,Object>> querySubDimCode(Map<?,?> data) {
		ArrayList<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		String 	tableName 	= data.get("tableName").toString();
		String 	preName 	= data.get("preName").toString();
		String  tableOwner 	= data.get("tableOwner").toString();
		long 	parId 		= Long.parseLong(data.get("parId").toString());
		int dataSourceId = Integer.parseInt(data.get("dataSourceId").toString());
		result =  (ArrayList<Map<String, Object>>) dimCodeSearchDAO.queryDimCode(tableName,tableOwner,preName,parId,-1,null,dataSourceId);
        for(Map<String,Object> m:result){
            for(String key:m.keySet()){
                if(m.get(key)!=null){
                    m.put(key, Convert.toString(m.get(key)));
                }
            }
        }
        return result;
	}
	
	public List<Map<String, Object>> queryValidCols(long tableId){
		return metaTableaColsDAO.queryValidCols(tableId);
	}
	
    public Map<String, Object> queryDimData(long dimTableId) {
    	Map<String, Object> dimInfo = metaDimTablesDAO.queryDimTableInfo(dimTableId);
    	return dimInfo;
    }

	public MetaDimTablesDAO getMetaDimTablesDAO() {
		return metaDimTablesDAO;
	}

	public void setMetaDimTablesDAO(MetaDimTablesDAO metaDimTablesDAO) {
		this.metaDimTablesDAO = metaDimTablesDAO;
	}
	 
	
}
