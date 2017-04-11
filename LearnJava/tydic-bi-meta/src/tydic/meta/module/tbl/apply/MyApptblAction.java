package tydic.meta.module.tbl.apply;

import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;
import tydic.meta.module.tbl.MetaSysCodeDAO;
import tydic.meta.module.tbl.MetaTableGroupDAO;
import tydic.meta.module.tbl.TblConstant;
import tydic.meta.sys.code.CodeManager;
import tydic.meta.web.session.SessionManager;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 杨苏维
 * @description 我的表类申请Action <br>
 * @date 2012-1-16
 */
public class MyApptblAction {
	
    private MetaSysCodeDAO metaSysCodeDAO;

    private MetaTableGroupDAO metaTableGroupDAO;
    
    private MyApptblDAO myApptblDAO;


	/**
     * 查询所有的表类型
     */
    public List<Map<String,Object>> queryTableType(){
        return metaSysCodeDAO.querySysCodeByCodeType(TblConstant.META_SYS_CODE_TABLE_TYPE);
    }
    
    /**
     * 根据表typeId查找所有的表分类。
     * @param tableType
     * @return
     */
    public List<Map<String,Object>> queryTableGroup(int tableType){
        return myApptblDAO.queryTableGroup(tableType);
    }
    
    
    
    /**
     * 查询用户申请的表类
     * @param tableType
     * @return
     */
    public List<Map<String,Object>> queryTablesByCondition(Map<String,Object> queryData,Page page){
//    	if(!SessionManager.isCurrentUserAdmin()){
    		queryData.put("userId", SessionManager.getCurrentUserID());
//    	}
    	List<Map<String,Object>> hasAut = myApptblDAO.queryTablesByCondition(queryData,page);
    	for(Map<String,Object> appData:hasAut){
    		appData.put("CODE_ITEM",MapUtils.getString(appData,"TABLE_TYPE_ID"));
    		appData.put("CODE_NAME",CodeManager.getName("TABLE_TYPE",MapUtils.getString(appData,"TABLE_TYPE_ID")));
    	}
    	
    	
    	
    	
    	
        return hasAut;
    }
    
    
    /**
     * 根据表typeId查找所有的表分类。
     * @param tableType
     * @return
     */
    public List<Map<String,Object>> queryByTableId(int tableId,int tableVersion){
        return myApptblDAO.queryMetaTableColsByTableId(tableId, tableVersion);
    }
    
    /**
     * 判断指定表名在数据库中是否已经存在
     * @param
     * @return
     */
    public boolean isExistsMatchTables(String tableName, int dataSource,String tableOwner,int tableId){
        return myApptblDAO.isExistsMatchTables(tableName, dataSource,tableOwner,tableId);
    }
    
    
    
    
    public MyApptblDAO getMyApptblDAO() {
		return myApptblDAO;
	}

	public void setMyApptblDAO(MyApptblDAO myApptblDAO) {
		this.myApptblDAO = myApptblDAO;
	}    

	public void setMetaSysCodeDAO(MetaSysCodeDAO metaSysCodeDAO) {
		this.metaSysCodeDAO = metaSysCodeDAO;
	}

	public MetaSysCodeDAO getMetaSysCodeDAO() {
		return metaSysCodeDAO;
	}

	public MetaTableGroupDAO getMetaTableGroupDAO() {
		return metaTableGroupDAO;
	}

	public void setMetaTableGroupDAO(MetaTableGroupDAO metaTableGroupDAO) {
		this.metaTableGroupDAO = metaTableGroupDAO;
	}
    
}
