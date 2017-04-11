package tydic.meta.module.gdl.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;
import tydic.meta.module.gdl.GdlAlterHisDAO;
import tydic.meta.module.gdl.GdlConstant;
import tydic.meta.module.gdl.GdlDimGroupMethodDAO;
import tydic.meta.module.gdl.GdlGroupDAO;
import tydic.meta.module.gdl.GdlGroupRelDAO;
import tydic.meta.module.gdl.GdlTabRelDAO;

/**
*
* @author 李国民
* @description 基础指标管理Action 
* @date 2012-06-05
*
*/
public class GdlBasicMagAction {
	
	private GdlBasicMagDAO gdlBasicMagDAO;			//基础指标管理DAO
	private GdlTabRelDAO gdlTabRelDAO;				//指标与表关系DAO
	private GdlAlterHisDAO gdlAlterHisDAO;			//指标变动历史表DAO
	private GdlGroupRelDAO gdlGroupRelDAO;			//指标与指标分类关系DAO
	private GdlGroupDAO gdlGroupDAO;				//指标分类表
	private GdlDimGroupMethodDAO gdlDimGroupMethodDAO;	//指标与维度汇总关系DAO

	/**
	 * 查询可以创建基础指标的表信息
	 * @param data
	 * @param page
	 */
	public List<Map<String, Object>> queryTableInfoToGdl(Map<String, Object> data, Page page){
		if(page==null){
			page=new Page(0,20);
		}
		return gdlBasicMagDAO.queryTableInfoToGdl(data, page);
	}
	
	/**
	 * 通过表类id查询对应可以添加的指标及维度
	 * @param tableId 表类id
	 * @return
	 */
	public Map<String, Object> queryColsByTableId(int tableId){
		Map<String,Object> rs = new HashMap<String,Object>();
		List<Map<String, Object>> dimData = gdlBasicMagDAO.queryDimColsByTableId(tableId);
		List<Map<String, Object>> gdlData = gdlBasicMagDAO.queryGdlColsByTableId(tableId);
		rs.put("dimData", dimData);
		rs.put("gdlData", gdlData);
		return rs;
	}
	
	/**
	 * 添加基础指标
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean insertGdlBasic(List<Map<String, Object>> dataList){
		try{
            BaseDAO.beginTransaction();
            for(int i=0;i<dataList.size();i++){
            	Map<String, Object> data = dataList.get(i);
    			long gdlId = gdlBasicMagDAO.queryForNextVal("SEQ_GDL_ID");	//得到指标id
    			data.put("GDL_ID", gdlId);			//指标id	
    			data.put("GDL_TYPE", GdlConstant.GDL_TYPE_BASIC);		//指标类型
    			data.put("GDL_SRC_TABLE_NAME", MapUtils.getString(data, "TABLE_NAME"));	//指标来源表名
    			data.put("GDL_SRC_TABLE_ID", MapUtils.getString(data, "TABLE_ID"));		//指标来源表id
    			data.put("GDL_SRC_COL", MapUtils.getString(data, "COL_NAME"));			//指标来源列
    			data.put("GDL_SRC_COL_ID", MapUtils.getString(data, "COL_ID"));			//指标来源列ID
    			data.put("GDL_COL_NAME", MapUtils.getString(data, "COL_NAME_CN"));		//指标字段名称
    			data.put("GDL_STATE", GdlConstant.GDL_STATE_INVALID);		//指标状态
    			data.put("GDL_VERSION", 1);		//指标版本
    			gdlBasicMagDAO.insertGDL(data);		//插入主表数据

    			data.put("REL_TYPE", 0);		//关联类型
    			data.put("REL_LEVEL", 0);		//层级数
    			gdlTabRelDAO.insertGdlTbaRel(data);	//插入指标和表的关联

    			data.put("ALTER_TYPE", GdlConstant.GDL_ALTER_TYPE_CREATE);		//建立
    			data.put("AUDIT_STATE", GdlConstant.GDL_NO_AUDIT);		//未审核
    			data.put("GDL_VERSION", 1);		//版本
    			gdlAlterHisDAO.insertGdlAlertHistory(data);
    			
    			if(data.get("DIM_INFO")!=null){		//如果存在关联维度，添加关联维度
    				List<Map<String, Object>> dimList = (List<Map<String, Object>>) data.get("DIM_INFO");
    				for (int j = 0; j < dimList.size(); j++) {
    					Map<String, Object> dimMap = dimList.get(j);
    					dimMap.put("GDL_VERSION", 1);
    					dimMap.put("GDL_ID", gdlId);
    				}
    				gdlDimGroupMethodDAO.insertGdlDimGroupMethod(dimList);		//批量添加关联维度
    			}
    			
    			String gdlGroupIdS =  MapUtils.getString(data,"GDL_GROUP_IDS");		//所属分类id
    			if(!gdlGroupIdS.equals("")){
    				List<Map<String, Object>> gdlGroupList = new ArrayList<Map<String,Object>>();
    				String gdlGroupId [] = gdlGroupIdS.split(",");
    				for (int j = 0; j < gdlGroupId.length; j++) {
    					Map<String, Object> gdlGroupMap = new HashMap<String, Object>();
    					gdlGroupMap.put("GDL_ID", gdlId);
    					gdlGroupMap.put("GDL_GROUP_ID", gdlGroupId[j]);
    					gdlGroupList.add(gdlGroupMap);
					}
    				gdlGroupRelDAO.insertGdlDimGroupMethod(gdlGroupList);		//批量添加指标与指标分类关系
    			}
            }
            BaseDAO.commit();
            return true;
		}catch(Exception e){
            Log.error(null, e);
            BaseDAO.rollback();
            return false;
		}
	}
	
    /**
     * 得到所有分类数据，用于绑定树上
     * @return
     */
    public Object[][] quertAllData(){
    	return gdlGroupDAO.quertAllData();
    }
    
    /**
     * 通过指标id及版本号得到指标详细信息（包括关联分类信息、支持维度信息、关联表类信息）
     * @param gdlId 指标id
     * @param gdlVersion 指标版本号
     * @return
     */
    public Map<String, Object> queryGdlInfoById(int gdlId, int gdlVersion){
    	//指标指标信息
    	Map<String, Object> gdlData = gdlBasicMagDAO.queryGdlInfoById(gdlId, gdlVersion);
    	//指标支持维度信息
    	List<Map<String, Object>> dimData = gdlBasicMagDAO.queryDimData(gdlData);
    	gdlData.put("DIM_DATA", dimData);
    	//指标关联分类信息
    	List<Map<String, Object>> groupData = gdlBasicMagDAO.queryGroupByGdlId(gdlId);
    	String gdlGroupIds = "";		//指标分类ids
    	String gdlGroupName = "";		//指标分类名称
    	for (int i = 0; i < groupData.size(); i++) {
    		if(i==0){
    			gdlGroupIds += MapUtils.getString(groupData.get(i), "GDL_GROUP_ID");
    			gdlGroupName += MapUtils.getString(groupData.get(i), "GROUP_NAME");
    		}else{
    			gdlGroupIds += ","+MapUtils.getString(groupData.get(i), "GDL_GROUP_ID");
    			gdlGroupName += ","+MapUtils.getString(groupData.get(i), "GROUP_NAME");
    		}
		}
    	gdlData.put("GDL_GROUP_IDS", gdlGroupIds);
    	gdlData.put("GDL_GROUP_NAME", gdlGroupName);
    	return gdlData;
    }
    
    /**
	 * 修改基础指标
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean updateGdlBasic(Map<String, Object> data){
		try{
            BaseDAO.beginTransaction();
            int onlyUpdateGroup = MapUtils.getIntValue(data, "ONLY_UPDATE_GROUP",0);	//是否值修改分类（1为是）
			int gdlId = MapUtils.getIntValue(data, "GDL_ID");		//指标id
            if(onlyUpdateGroup==1){
            	gdlGroupRelDAO.deleteGdlDimGroupMethod(gdlId);	//删除以前的分类关系
            	
    			String gdlGroupIdS =  MapUtils.getString(data,"GDL_GROUP_IDS");		//所属分类id
    			if(!gdlGroupIdS.equals("")){
    				List<Map<String, Object>> gdlGroupList = new ArrayList<Map<String,Object>>();
    				String gdlGroupId [] = gdlGroupIdS.split(",");
    				for (int j = 0; j < gdlGroupId.length; j++) {
    					Map<String, Object> gdlGroupMap = new HashMap<String, Object>();
    					gdlGroupMap.put("GDL_ID", gdlId);
    					gdlGroupMap.put("GDL_GROUP_ID", gdlGroupId[j]);
    					gdlGroupList.add(gdlGroupMap);
    				}
    				gdlGroupRelDAO.insertGdlDimGroupMethod(gdlGroupList);		//批量添加指标与指标分类关系
    			}
            }else{
            	int maxGdlVersion = gdlBasicMagDAO.queryNextVersion(gdlId);
            	
    			data.put("GDL_TYPE", GdlConstant.GDL_TYPE_BASIC);		//指标类型
    			data.put("GDL_STATE", GdlConstant.GDL_STATE_INVALID);		//指标状态
    			data.put("GDL_VERSION", maxGdlVersion);		//指标版本
    			gdlBasicMagDAO.insertGDL(data);		//插入主表数据

    			data.put("ALTER_TYPE", GdlConstant.GDL_ALTER_TYPE_UPDATE);		//建立
    			data.put("AUDIT_STATE", GdlConstant.GDL_NO_AUDIT);		//未审核
    			data.put("GDL_VERSION", maxGdlVersion);		//版本
    			gdlAlterHisDAO.insertGdlAlertHistory(data);	//插入用户操作表
    			
    			if(data.get("DIM_INFO")!=null){		//如果存在关联维度，添加关联维度
    				List<Map<String, Object>> dimList = (List<Map<String, Object>>) data.get("DIM_INFO");
    				for (int j = 0; j < dimList.size(); j++) {
    					Map<String, Object> dimMap = dimList.get(j);
    					dimMap.put("GDL_VERSION", maxGdlVersion);
    					dimMap.put("GDL_ID", gdlId);
    					dimMap.put("DIM_COL_ID", MapUtils.getString(dimMap, "COL_ID"));	//设置宽表关联维度列id
    				}
    				gdlDimGroupMethodDAO.insertGdlDimGroupMethod(dimList);		//批量添加关联维度
    			}
    			
            	gdlGroupRelDAO.deleteGdlDimGroupMethod(gdlId);	//删除以前的分类关系
            	
    			String gdlGroupIdS =  MapUtils.getString(data,"GDL_GROUP_IDS");		//所属分类id
    			if(!gdlGroupIdS.equals("")){
    				List<Map<String, Object>> gdlGroupList = new ArrayList<Map<String,Object>>();
    				String gdlGroupId [] = gdlGroupIdS.split(",");
    				for (int j = 0; j < gdlGroupId.length; j++) {
    					Map<String, Object> gdlGroupMap = new HashMap<String, Object>();
    					gdlGroupMap.put("GDL_ID", gdlId);
    					gdlGroupMap.put("GDL_GROUP_ID", gdlGroupId[j]);
    					gdlGroupList.add(gdlGroupMap);
    				}
    				gdlGroupRelDAO.insertGdlDimGroupMethod(gdlGroupList);		//批量添加指标与指标分类关系
    			}
            }
            BaseDAO.commit();
            return true;
		}catch(Exception e){
            Log.error(null, e);
            BaseDAO.rollback();
            return false;
		}
	}
	
	/**
	 * 批量验证指标编码是否存在(添加时)
	 * @param gdlCode 指标编码
	 * @return
	 */
	public Map<String, Object> checkGdlCodeByAdd(List<Map<String, Object>> data){
		boolean isExits = false;	//是否重复
		String gdlCode = "";		//重复的指标编码
		Map<String, Object> rs = new HashMap<String, Object>();
		for (int i = 0; i < data.size(); i++) {
			Map<String, Object> map = data.get(i);
			gdlCode = MapUtils.getString(map, "GDL_CODE");
			isExits = gdlBasicMagDAO.checkReGdlCode(gdlCode);
			if(isExits){
				break;
			}
		}
		rs.put("IS_EXITS", isExits);
		rs.put("GDL_CODE", gdlCode);
		return rs;
	}
	
	/**
	 * 验证指标编码是否存在(修改时)
	 * @param gdlCode 指标编码
	 * @return
	 */
	public boolean checkGdlCode(String gdlCode, int gdlId){
		return gdlBasicMagDAO.checkReGdlCode(gdlCode, gdlId);
	}
	
	
	public void setGdlGroupDAO(GdlGroupDAO gdlGroupDAO) {
		this.gdlGroupDAO = gdlGroupDAO;
	}

	public void setGdlTabRelDAO(GdlTabRelDAO gdlTabRelDAO) {
		this.gdlTabRelDAO = gdlTabRelDAO;
	}

	public void setGdlAlterHisDAO(GdlAlterHisDAO gdlAlterHisDAO) {
		this.gdlAlterHisDAO = gdlAlterHisDAO;
	}

	public void setGdlGroupRelDAO(GdlGroupRelDAO gdlGroupRelDAO) {
		this.gdlGroupRelDAO = gdlGroupRelDAO;
	}

	public void setGdlDimGroupMethodDAO(GdlDimGroupMethodDAO gdlDimGroupMethodDAO) {
		this.gdlDimGroupMethodDAO = gdlDimGroupMethodDAO;
	}

	public void setGdlBasicMagDAO(GdlBasicMagDAO gdlBasicMagDAO) {
		this.gdlBasicMagDAO = gdlBasicMagDAO;
	}
}
