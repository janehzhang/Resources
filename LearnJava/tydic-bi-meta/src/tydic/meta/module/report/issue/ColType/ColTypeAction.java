package tydic.meta.module.report.issue.ColType;

import java.util.List;
import java.util.Map;
import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.meta.common.OprResult;
import tydic.meta.common.Page;

/**
 * Copyrights @ 2012，Tianyuan DIC Information Co., Ltd.
 * All rights reserved.
 * 字段分类管理类
 * @author tanwc 
 * @date 2012-2-20
 * 
 */
public class ColTypeAction {
	private ColTypeDAO colTypeDAO;
	
	public ColTypeDAO getColTypeDAO() {
		return colTypeDAO;
	}

	public void setColTypeDAO(ColTypeDAO colTypeDAO) {
		this.colTypeDAO = colTypeDAO;
	}
	
	/**
	 * 获取所有的字段分类
	 * @param page	分页参数
	 * @return	list集合
	 */
    public List<Map<String,Object>> getAllColType(Page page){
        if(page==null){//如果没有page，为第一页。
           page=new Page(0,20);
        }
        List<Map<String,Object>> list = colTypeDAO.getAllColType(page);
        return list;
    }
    
    /**
     * 根据父ID获取子字段
     * @param parentId	父ID
     * @return
     */
    public List<Map<String,Object>> getSubColType(Integer parentId){
        return colTypeDAO.getSubColType(parentId);
    }
    
    /**
     * 添加字段分类
     * @param colTypeName	字段名称
     * @param parentId		父ID
     * @return
     */
	public OprResult<?,?> addColType(String colTypeName,String parentId,int isShowIndex){
		
		OprResult<Integer,Map<String,Object>> result = null;
	 	try{
	 		BaseDAO.beginTransaction();
	 		//同名检测
	 		if(colTypeDAO.hasExist(colTypeName,parentId)) {
	 			result = new OprResult<Integer,Map<String,Object>>(null, null, OprResult.OprResultType.nameExist);
	 			return result;
	 		}
            result = new OprResult<Integer,Map<String,Object>>(null, colTypeDAO.addColType(colTypeName,parentId,isShowIndex), OprResult.OprResultType.insert);
            result.setSuccessData(colTypeDAO.queryColTypeById(Integer.parseInt(result.getTid().toString())));
            
            BaseDAO.commit();
            return result;
        } catch(Exception e){
            Log.error(null, e);
            BaseDAO.rollback();
            result = new OprResult<Integer,Map<String,Object>>(null, null, OprResult.OprResultType.error);
        }
        return result;
	}
	
	/**
	 * 修改字段分类
	 * @param colTypeId	字段分类ID
	 * @param colTypeName	修改的字段分类名
	 * @return
	 */
	public OprResult<?,?> updateColType(String oldColTypeName,String colTypeId,String colTypeName,String parentId,int isShowIndex){
        OprResult<Integer,Map<String,Object>> result = null;
        try{
            BaseDAO.beginTransaction();
            //名称未修改
            if(oldColTypeName.equals(colTypeName)){
            	result = new OprResult<Integer,Map<String,Object>>(Integer.parseInt(colTypeId),
                        Integer.parseInt(colTypeId), OprResult.OprResultType.update);
                colTypeDAO.updateColType(colTypeId,colTypeName,isShowIndex);
                result.setSuccessData(colTypeDAO.queryColTypeById(Integer.parseInt(colTypeId)));
                BaseDAO.commit();
                return result;
            }
	 		//同名检测
	 		if(colTypeDAO.hasExist(colTypeName,parentId)) {
	 			result = new OprResult<Integer,Map<String,Object>>(null, null, OprResult.OprResultType.nameExist);
	 			return result;
	 		}
            
            colTypeDAO.updateColType(colTypeId,colTypeName,isShowIndex);
            result = new OprResult<Integer,Map<String,Object>>(Integer.parseInt(colTypeId),
                    Integer.parseInt(colTypeId), OprResult.OprResultType.update);
            
            result.setSuccessData(colTypeDAO.queryColTypeById(Integer.parseInt(result.getSid().toString())));
            BaseDAO.commit();
            return result;
        } catch(Exception e){
            Log.error(null, e);
            BaseDAO.rollback();
            result = new OprResult<Integer,Map<String,Object>>(null, null, OprResult.OprResultType.error);
        }
        return result;
    }
	
	/**
	 * 删除字段分类
	 * @param colTypeId	字段分类ID
	 * @param parentId	字段分类父ID
	 * @return
	 */
    public OprResult<?,?> deleteColType(int colTypeId, int parentId){
        OprResult<Integer,Map<String,Object>> result = null;
        try{
            BaseDAO.beginTransaction();
            colTypeDAO.deleteColType(colTypeId);
            
            if(colTypeDAO.getSubColType(colTypeId).size() > 0) {
            	result = new OprResult<Integer,Map<String,Object>>(-1, null, OprResult.OprResultType.error);
            	return result;
            }
            // 查看是否有已发布的数据模型在引用该字段分类
            if(colTypeDAO.isCite(colTypeId).size() > 0) {
            	result = new OprResult<Integer,Map<String,Object>>(-2, null, OprResult.OprResultType.error);
            	return result;
            }
            
            result = new OprResult<Integer,Map<String,Object>>(colTypeId, null, OprResult.OprResultType.delete);
            BaseDAO.commit();
            return result;
        } catch(Exception e){
            Log.error(null, e);
            result = new OprResult<Integer,Map<String,Object>>(colTypeId, null, OprResult.OprResultType.error);
            return result;
        }
    }
    
    /**
     * 改变字段分类层级关系
     * @param levelData	层次数据
     * @param orderData	排序数据
     * @return
     */
    public boolean changeLevel(List<Map<String,Object>> levelData,Map<String,Long> orderData){
        try{
            BaseDAO.beginTransaction();
            colTypeDAO.updateBatchLevel(levelData);
            colTypeDAO.updateBatchOrder(orderData);
            BaseDAO.commit();
            return true;
        }catch(Exception e){
            Log.error(null,e);
            BaseDAO.rollback();
            return false;
        }
    }
    
    
    public List<Map<String,Object>> queryColType() {
    	
    	return colTypeDAO.queryColType();
    }
    
    
	

}
