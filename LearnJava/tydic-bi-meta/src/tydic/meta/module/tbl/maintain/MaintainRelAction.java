package tydic.meta.module.tbl.maintain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.Log;
import tydic.meta.common.Common;
import tydic.meta.common.OprResult;
import tydic.meta.module.tbl.MetaMagUserTabRelDAO;
import tydic.meta.module.tbl.MetaTableColsDAO;
import tydic.meta.module.tbl.MetaTableMaintainDAO;
import tydic.meta.module.tbl.MetaTableRelDAO;
import tydic.meta.module.tbl.MetaTablesDAO;
import tydic.meta.module.tbl.TblConstant;
import tydic.meta.module.tbl.apply.MyApptblDAO;
import tydic.meta.web.session.SessionManager;

/**
 *
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 *
 * @author 刘斌
 * @date 2011-11-1
 * @description 表类维护——表类关系维护Action
 *
 */
public class MaintainRelAction {
	/**维护表类*/
	private MetaTableMaintainDAO metaTableMaintainDao;
    private MetaTableColsDAO metaTableColsDAO;
    private MetaTablesDAO metaTablesDAO;
    private MetaTableRelDAO metaTableRelDAO;
    private MetaMagUserTabRelDAO metaMagUserTabRelDAO;
    private MyApptblDAO myApptblDAO;


	/**
     * 根据表类ID构造一颗列树，子节点是该表类的列，父节点是该表，并且只有一层叶子节点
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryColTree(int tableId){
        return metaTableColsDAO.queryColTreeByTableId(tableId);
    }
    
	/**
	 * 根据表类ID查询基本资料及数据源
	 * @param tableId
	 * @return
	 */
	public Map<String, Object> queryTableBaseInfoByTableId(int tableId,int tableVersion){
		return metaTablesDAO.queryTableByTableId(tableId, tableVersion);
	}
	
	/**
	 * 查询表的所有层次分类
	 * @return
	 */
	public List<Map<String, Object>> queryTableAttributeType(){
		return metaTableMaintainDao.getTableAllType();
	}
	
	/**
	 * 查询表的业务类型
	 * @return
	 */
	public List<Map<String, Object>> queryTableOperationType(){
		return metaTableMaintainDao.getTableOperationType();
	}

    /**
     * 根据表ID取表信息
     * @param tableId
     * @return
     */
    public Map<String, Object> queryTableByTableId(int tableId){
        return metaTablesDAO.queryTableByTableId(tableId, metaTablesDAO.queryValidVersion(tableId));
    }

    /**
     * 保存血缘关系
     * @param flowData
     * @param linkData
     * @return
     */
    public boolean saveRelInfo(Map<String, Object> flowData, Map<String, Map<String, Object>> linkData, int tableId){
        List<Map<String, Object>> links = (List<Map<String, Object>>)(flowData.get("links"));
        Map<String,String> alreadyDeleted = new HashMap<String, String>();

        try{
            BaseDAO.beginTransaction();
            for(int i=0; i<links.size();i++){
                Map<String, Object> link=links.get(i);
                //tableId1
                int fromTId=Integer.parseInt(link.get("from").toString().replace("TABLE_", ""));
                //tableId2
                int toTId=Integer.parseInt(link.get("to").toString().replace("TABLE_", ""));
                //忽略节点不为当前表的情况
                if(fromTId!=tableId&&toTId!=tableId){
                    continue;
                }

                //如果流程图的箭头对象在箭头数据中
                List<Map<String, Object>> tmpMap=isInUpdate(fromTId,toTId,linkData);
                if(tmpMap.size()>0){
                    if(!alreadyDeleted.containsKey(fromTId+"_"+toTId)){
                        // 删除对应记录并新增
                        metaTableRelDAO.deleteByTbl1Tbl2(fromTId, toTId);
                        for(int j=0; j<tmpMap.size(); j++){
                            metaTableRelDAO.insertMetaTableRel(tmpMap.get(j));
                        }
                        alreadyDeleted.put(fromTId+"_"+toTId,fromTId+"_"+toTId);
                    }else{
                        // 直接新增
                        for(int j=0; j<tmpMap.size(); j++){
                            metaTableRelDAO.insertMetaTableRel(tmpMap.get(j));
                        }
                    }
                }
            }
            //删除对应 tableId 中不存在的关联
            //该表对应的所有关联关系信息
            List<Map<String, Object>> allRelInfo = metaTableRelDAO.queryDetailByTableId(tableId, null);
            for(int i=0; i<allRelInfo.size(); i++){
                boolean b = false;
                for(int j=0; j<links.size(); j++){
                    int fromId=Integer.parseInt(links.get(j).get("from").toString().replace("TABLE_",""));
                    int toId=Integer.parseInt(links.get(j).get("to").toString().replace("TABLE_",""));
                	int tid1 = allRelInfo.get(i).get("TABLE_ID1")==null?null:Integer.parseInt(allRelInfo.get(i).get("TABLE_ID1").toString());
                	int tid2 = allRelInfo.get(i).get("TABLE_ID2")==null?null:Integer.parseInt(allRelInfo.get(i).get("TABLE_ID2").toString());
                    if((fromId==tid1&&toId==tid2)||(fromId==tid2&&toId==tid1)){
                        //正反向箭头同时存在时无法删除一条
//                    if(fromId==tid1&&toId==tid2){
                        //存在
                        b = true;
                        break;
                    }
                }
                if(!b){
                	int tableId1 = allRelInfo.get(i).get("TABLE_ID1")==null?null:Integer.parseInt(allRelInfo.get(i).get("TABLE_ID1").toString());
                	int tableId2 = allRelInfo.get(i).get("TABLE_ID2")==null?null:Integer.parseInt(allRelInfo.get(i).get("TABLE_ID2").toString());
                    metaTableRelDAO.deleteByTbl1Tbl2(tableId1, tableId2);
                }
            }


            if(links.size()==0){// 如果前台没有节点数据传来 则删除所有的对应tableId的关联关系
                metaTableRelDAO.deleteByTb1(tableId);
                metaTableRelDAO.deleteByTb2(tableId);
            }

            BaseDAO.commit();
        }catch (Exception e){
            Log.error(null, e);
            BaseDAO.rollback();
            return false;
        }
        return true;
    }

    /**
     * 判断箭头数据是否在被修改的数据中
     * @param fromTId
     * @param toTId
     * @param linkData
     * @return
     */
    private List<Map<String, Object>> isInUpdate(int fromTId, int toTId, Map<String, Map<String, Object>> linkData){
        List<Map<String, Object>> rtnL = new ArrayList<Map<String, Object>>();
        for (Iterator iter = linkData.keySet().iterator(); iter.hasNext();) {
            String key = Convert.toString(iter.next());
            if(key.contains(fromTId+"_"+toTId+"_")){
                Map<String, Object> tempM = new HashMap<String, Object>();
                tempM.put("tableId1ColIds", Convert.toString(linkData.get(key).get("colName1Id")));
                tempM.put("tableId2ColIds", Convert.toString(linkData.get(key).get("colName2Id")));
                tempM.put("tableRelDesc", linkData.get(key).get("relComm"));
                tempM.put("tableRelType", linkData.get(key).get("relType"));
                tempM.put("tableId1", fromTId);
                tempM.put("tableId2", toTId);
                rtnL.add(tempM);
            }
        }
        return rtnL;
    }

	/**
	 * 根据数据源ID获取其名字
	 * @param dataSouseId
	 * @return
	 */
	public String queryTableDataSouseNameById(int dataSouseId){
		List<Map<String, Object>> dataList = metaTableMaintainDao.getTableDataSoureById(dataSouseId);
		String dataSouseName = new String();
		for (int i = 0; i < dataList.size(); i++) {
			dataSouseName = (String) dataList.get(i).get("DATA_SOURCE_NAME");
		}
		return dataSouseName;
	}
	
	/**
	 * 修改表类基本信息、列信息
	 * @param formData
	 * @return
	 */
	public int updataTableBaseInfoByTableId(Map<String, Object> formData){
		int reInt = -1;
		try {
            BaseDAO.beginTransaction();
            //获取页面传递的表类基本信息
            Map<String, Object> tableDatas =  (Map<String, Object>) formData.get("tableData");
            //获取页面传递的表类列信息
            List<Map<String, Object>> talbeCols = (List<Map<String, Object>>) formData.get("columnDatas");
            int tableId = Integer.parseInt((String) tableDatas.get("tableId"));
            String tableName = (String) tableDatas.get("tableName");
            //查询表的最大版本号
            int maxVersion = metaTablesDAO.queryMaxVersion(tableId);
            /*
             * 增加保存后信息是否在审核状态的判断（meta_mag_user_tab_rel--根据表ID与版本号获取审核状态）
             * 增加人：吴喜丽
             * 增加时间：2012-01-09
             */
        	List<Map<String, Object>> relTypeList = metaMagUserTabRelDAO.queryRelTypeByTableId(tableId, maxVersion);
        	if(relTypeList != null && relTypeList.size() > 0){
        		if(relTypeList.size() == 1){//只判断申请状态
        			Map<String, Object> m = (Map<String, Object>)relTypeList.get(0);
        			int relType = m.get("REL_TYPE")==null?null:Integer.parseInt(m.get("REL_TYPE").toString());
        			if(relType == 0 || relType == 2){//说明该表id+版本号正在审核中
        				reInt = 2;
        				return reInt;
        			}
        		}
        	}
        	//判断表名是否存在
        	if(tableName != null && !tableName.equals("")){
        		if(myApptblDAO.isExistsMatchTables(tableName, Integer.parseInt(tableDatas.get("data_sourceHidden").toString()), tableDatas.get("tableOwner").toString(), Integer.parseInt(tableDatas.get("tableId").toString()))){
        			reInt = 3;
        			return reInt;
        		}
        	}

        	/*根据表ID+版本号，更新meta_table_cols表的COL_STATE=0（以前列信息）*/
            // 这里不将meta_table_cols表的COL_STATE设为0，在审核的时候才更新该记录的COL_STATE为0
//        	metaTableColsDAO.updateMetaTableColsByTableIdAndVersion(tableId, maxVersion);
            
            int currentVersion = maxVersion+1;
            tableDatas.put("maxTableVersion", currentVersion);

            //根据ID和版本号查询表未修改的基本信息
            Map<String, Object> tableInfoMap = metaTableMaintainDao.getTableBaseInfoByTableIdAndVersion(tableId, Integer.parseInt(tableDatas.get("tableVersion").toString()));
            tableDatas.put("tableOwner", Convert.toString(tableDatas.get("tableOwner"))); //20120131 修改tableOwner从界面传来
            tableDatas.put("tableSpace", tableInfoMap.get("TABLE_SPACE"));
            tableDatas.put("tableGroupId", tableDatas.get("tableGroupId"));
            tableDatas.put("tableBusComment", tableDatas.get("tableBusComment"));
            tableDatas.put("partitionSql", tableDatas.get("partitionSql"));
            tableDatas.put("tableState",2);//修改表类之后状态为2，成为待审批状态

            //设置列的TableVersion
            for(int i=0; i< talbeCols.size(); i++){
                talbeCols.get(i).put("tableVersion",currentVersion);
                if(talbeCols.get(i).get("colId")!=null&&!talbeCols.get(i).get("colId").equals("")){
                }else{
                    talbeCols.get(i).put("colId", metaTableColsDAO.queryForNextVal("SEQ_TAB_COL_ID"));
                }
            }

            //根据名称修改其表状态为无效(根据业务需求不更改之前表状态)
//            metaTablesDAO.inValidTable(tableName);
            //修改的信息作为数据插入数据库
            metaTableMaintainDao.insertMetaTableByPk(tableDatas);
            //增加列信息
            metaTableColsDAO.insertBatch(talbeCols);
            metaTableColsDAO.updateMetaTableColsByTableIdAndVersion(tableId, currentVersion, 0);
            metaTableMaintainDao.insertTableUpdateLog(tableId, tableName, SessionManager.getCurrentUserID(),TblConstant.META_TABLE_STATE_MODIFY,currentVersion);
            /*
             * 新增META_TABLE_REL数据
             * 步骤：1.删除原有数据：根据tableId
             * 		2.新增现有数据
             * 修改人：刘斌
             * 修改时间：2012-01-06
             */
            Map<String, Object> dimTableDatas = new HashMap<String, Object>();
            Map<Integer, String> alreadyDel = new HashMap<Integer, String>();
            for(Map<String, Object> m : talbeCols){
            	//根据COL_ID是否为空来判断属于新增关系：为空--新增
    			int dimColId = Convert.toString(m.get("dimColId")).equals("")?0:Integer.parseInt(m.get("dimColId").toString());
            	if(dimColId > 0){
                    //保存表类关系信息
                    Map<String, Object> insertData = new HashMap<String, Object>();
                    insertData.put("tableId1ColIds", m.get("colId"));
                    insertData.put("tableId2ColIds", dimColId);
                    insertData.put("tableRelDesc", "");
                    insertData.put("tableRelType", 1);
                    insertData.put("tableId1", tableId);
                    insertData.put("tableId2", Common.parseInt(m.get("dimTableId")));
                    if(!metaTableRelDAO.checkExist(insertData)){
                        metaTableRelDAO.insertMetaTableRel(insertData);
                    }
            	}
            }


            BaseDAO.commit();
            reInt = 0;
        } catch (Exception e) {
        	reInt = 1;
            BaseDAO.rollback();
            Log.error("", e);
		}
		return reInt;
	}

    /**
     * 根据前台血缘关系列表信息保存血缘关系
     * @return
     */
    public boolean saveRelFromGrid(int tableId, List<Map<String, Object>> datas){
        try {
            BaseDAO.beginTransaction();
            //先删除所有对应tableId的关系记录 再新增
            metaTableRelDAO.deleteByTb1(tableId);
            metaTableRelDAO.deleteByTb2(tableId);
            for(int i=0; i<datas.size(); i++){
                //如果前台传来的数据relType==3，则需要设置relType=2、并且交换tableId1、tableId2以及colId1，colId2的值
                Map<String, Object> data = datas.get(i);
                Map<String, Object> insertData = new HashMap<String, Object>();
                if(data.get("relType").toString().equals("3")){//交换
                    insertData.put("tableId1ColIds", data.get("colinfo2Id"));
                    insertData.put("tableId2ColIds", data.get("colinfo1Id"));
                    insertData.put("tableRelDesc", data.get("relDesc"));
                    insertData.put("tableRelType", 2);
                    insertData.put("tableId1", data.get("t2nameId"));
                    insertData.put("tableId2", tableId);
                    metaTableRelDAO.insertMetaTableRel(insertData);
                } else {
                    insertData.put("tableId1ColIds", data.get("colinfo1Id"));
                    insertData.put("tableId2ColIds", data.get("colinfo2Id"));
                    insertData.put("tableRelDesc", data.get("relDesc"));
                    insertData.put("tableRelType", data.get("relType"));
                    insertData.put("tableId1", tableId);
                    insertData.put("tableId2", data.get("t2nameId"));
                    metaTableRelDAO.insertMetaTableRel(insertData);
                }
            }
            BaseDAO.commit();
        } catch (Exception e){
            Log.error(null, e);
            BaseDAO.rollback();
            return false;
        }
        return true;
    }

    /**
     * 表类下线
     * @param tableId
     * @return
     */
    public OprResult<?,?> offlineTable(int tableId, int version){
        OprResult<Integer,Object> result = null;
        try {
            BaseDAO.beginTransaction();
            result = new OprResult<Integer,Object>(null, metaTablesDAO.offlineTable(tableId, version), OprResult.OprResultType.update);
            result.setSuccessData(metaTablesDAO.queryTablesByIdAndVersion(tableId,version));
            //记录日志
            Map<String, Object> userTabRel = new HashMap<String, Object>();
            userTabRel.put("userId", SessionManager.getCurrentUserID());
            userTabRel.put("tableId", tableId);
            userTabRel.put("relType", TblConstant.USER_TAB_REL_TYPE_OFFLINE);
            userTabRel.put("tableVersion", version);
            userTabRel.put("stateMark", "表类下线");
            metaMagUserTabRelDAO.insertMetaMagUserTable(userTabRel);
            BaseDAO.commit();
            return result;
        }catch (Exception e){
            Log.error("表类下线出错", e);
            result = new OprResult<Integer,Object>(null, null, OprResult.OprResultType.error);
            BaseDAO.rollback();
            return result;
        }
    }

    /**
     * 表类上线
     * @param tableId
     * @return
     */
    public OprResult<?,?> onlineTable(int tableId, int version){
        OprResult<Integer,Object> result = null;
        try {
            BaseDAO.beginTransaction();
            result = new OprResult<Integer,Object>(null, metaTablesDAO.onlineTable(tableId, version), OprResult.OprResultType.update);
            result.setSuccessData(metaTablesDAO.queryTablesByIdAndVersion(tableId,version));
            //记录日志
            Map<String, Object> userTabRel = new HashMap<String, Object>();
            userTabRel.put("userId", SessionManager.getCurrentUserID());
            userTabRel.put("tableId", tableId);
            userTabRel.put("relType", TblConstant.USER_TAB_REL_TYPE_ONLINE);
            userTabRel.put("tableVersion", version);
            userTabRel.put("stateMark", "表类上线");
            metaMagUserTabRelDAO.insertMetaMagUserTable(userTabRel);
            BaseDAO.commit();
            return result;
        }catch (Exception e){
            Log.error("表类下线出错", e);
            result = new OprResult<Integer,Object>(null, null, OprResult.OprResultType.error);
            BaseDAO.rollback();
            return result;
        }
    }

	/**
	 * 根据表ID和状态查询表字段信息
	 * @param tableId
	 * @return
	 */
	public List<Map<String, Object>> queryTableColsByTableIdAndState(int tableId){
		return metaTableColsDAO.queryTableColsByTableIdAndState(tableId);
	}

    /**
     * 查询页面提交上来的信息是否做了变更
     * @param formData
     * @return  true:表类基本信息完全相同，false:表类基本信息有不同
     */
    public boolean checkIsSame(Map<String, Object> formData){
        //页面传来的表信息
        Map<String, Object> tableDatas =  (Map<String, Object>) formData.get("tableData");
        //获取页面传递的表类列信息
        List<Map<String, Object>> talbeCols = (List<Map<String, Object>>) formData.get("columnDatas");
        int tableId = Integer.parseInt((String) tableDatas.get("tableId"));
        int tableVersion = Integer.parseInt((String) tableDatas.get("tableVersion"));
        //数据库查询的信息
        Map<String, Object> tableDatas_DB = metaTablesDAO.queryTablesByIdAndVersion(tableId,tableVersion);
        List<Map<String, Object>> talbeCols_DB = metaTableColsDAO.queryValidColInfoByTableId(tableId, null);

        //开始执行比较
        if(talbeCols_DB.size()!=talbeCols.size()){//列信息数量不同，直接返回true
            return false;
        }
        return compareTableDatas(tableDatas, tableDatas_DB)&&compareColDatas(talbeCols, talbeCols_DB);
    }

    /**
     * 比较基本信息
     * @param tableDatas
     * @param tableDatas_DB
     * @return true:表类基本信息完全相同，false:表类基本信息有不同
     */
    private boolean compareTableDatas(Map<String, Object> tableDatas, Map<String, Object> tableDatas_DB){
        //比较表名
        if(!Convert.toString(tableDatas.get("tableName")).equalsIgnoreCase(Convert.toString(tableDatas_DB.get("TABLE_NAME")))){
            return false;
        }
        //比较中文名
        if(!Convert.toString(tableDatas.get("table_name_cn")).equalsIgnoreCase(Convert.toString(tableDatas_DB.get("TABLE_NAME_CN")))){
            return false;
        }
        if(!Convert.toString(tableDatas.get("tableGroupId")).equalsIgnoreCase(Convert.toString(tableDatas_DB.get("TABLE_GROUP_ID")))){
            return false;
        }
        if(!Convert.toString(tableDatas.get("data_sourceHidden")).equalsIgnoreCase(Convert.toString(tableDatas_DB.get("DATA_SOURCE_ID")))){
            return false;
        }
        if(!Convert.toString(tableDatas.get("table_attType")).equalsIgnoreCase(Convert.toString(tableDatas_DB.get("TABLE_TYPE_ID")))){
            return false;
        }
        if(!Convert.toString(tableDatas.get("tableOwner")).equalsIgnoreCase(Convert.toString(tableDatas_DB.get("TABLE_OWNER")))){
            return false;
        }if(!Convert.toString(tableDatas.get("tableSpace")).equalsIgnoreCase(Convert.toString(tableDatas_DB.get("TABLE_SPACE")))){
            return false;
        }
        if(!Convert.toString(tableDatas.get("table_operType")).equalsIgnoreCase(Convert.toString(tableDatas_DB.get("TABLE_GROUP_NAME")))){
            return false;
        }
        if(!Convert.toString(tableDatas.get("tableBusComment")).equalsIgnoreCase(Convert.toString(tableDatas_DB.get("TABLE_BUS_COMMENT")))){
            return false;
        }
        if(!Convert.toString(tableDatas.get("partitionSql")).equalsIgnoreCase(Convert.toString(tableDatas_DB.get("PARTITION_SQL")))){//partitionSql
            return false;
        }
        return true;
    }

    /**
     * 比较列信息
     * @param talbeCols
     * @param talbeCols_DB
     * @return  true:表类所有列信息完全相同，false:表类列信息有不同
     */
    private boolean compareColDatas(List<Map<String, Object>> talbeCols, List<Map<String, Object>> talbeCols_DB){
        for(int i=0; i < talbeCols.size(); i++){
            Map<String, Object> col =  talbeCols.get(i);
            Map<String, Object> col_DB =  talbeCols_DB.get(i);
            if(!Convert.toString(col.get("colName")).equalsIgnoreCase(Convert.toString(col_DB.get("COL_NAME")))){
                return false;
            }
            if(!Convert.toString(col.get("colNameCn")).equalsIgnoreCase(Convert.toString(col_DB.get("COL_NAME_CN")))){
                return false;
            }
            if(!Convert.toString(col.get("fullDataType")).equalsIgnoreCase(Convert.toString(col_DB.get("COL_DATATYPE")))){
                return false;
            }
            if(!Convert.toString(col.get("isPrimary")).equalsIgnoreCase(Convert.toString(col_DB.get("IS_PRIMARY")))){
                return false;
            }
            if(!Convert.toString(col.get("defaultVal")).equalsIgnoreCase(Convert.toString(col_DB.get("DEFAULT_VAL")))){
                return false;
            }
            if(!Convert.toString(col.get("colNullabled")).equalsIgnoreCase(Convert.toString(col_DB.get("COL_NULLABLED")))){
                return false;
            }
            if(!Convert.toString(col.get("colBusComment")).equalsIgnoreCase(Convert.toString(col_DB.get("COL_BUS_COMMENT")))){
                return false;
            }
            if(!Convert.toString(col.get("dimTypeId")).equalsIgnoreCase(Convert.toString(col_DB.get("DIM_TYPE_ID")))){
                return false;
            }
            if(!Convert.toString(col.get("dimTableId")).equalsIgnoreCase(Convert.toString(col_DB.get("DIM_TABLE_ID")))){
                return false;
            }
            if(!Convert.toString(col.get("dimColId")).equalsIgnoreCase(Convert.toString(col_DB.get("DIM_COL_ID")))){
                return false;
            }
            if(!Convert.toString(col.get("dimLevel")).equalsIgnoreCase(Convert.toString(col_DB.get("DIM_LEVEL")))){
                return false;
            }
        }
        return true;
    }
    /**
     * 判断当前表是否在审核中
     * @param tableId 表类Id
     * @param tableVersion 表类版本
     * */
    public boolean isAudit(int tableId,int tableVersion){
    	boolean isAudit  = false;
    	int count =  metaMagUserTabRelDAO.isAudit(tableId, tableVersion+1);
    	if(count==1){
    		isAudit = true;
    	}
		return isAudit;
    }
   

	public void setMetaTableMaintainDao(MetaTableMaintainDAO metaTableMaintainDao) {
		this.metaTableMaintainDao = metaTableMaintainDao;
	}

    public void setMetaTableColsDAO(MetaTableColsDAO metaTableColsDAO) {
        this.metaTableColsDAO = metaTableColsDAO;
    }

    public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO) {
        this.metaTablesDAO = metaTablesDAO;
    }

    public void setMetaTableRelDAO(MetaTableRelDAO metaTableRelDAO) {
        this.metaTableRelDAO = metaTableRelDAO;
    }

    public void setMetaMagUserTabRelDAO(MetaMagUserTabRelDAO metaMagUserTabRelDAO) {
        this.metaMagUserTabRelDAO = metaMagUserTabRelDAO;
    }
    public void setMyApptblDAO(MyApptblDAO myApptblDAO) {
		this.myApptblDAO = myApptblDAO;
	}

}
