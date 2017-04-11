package tydic.meta.module.tbl.view;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.Common;
import tydic.meta.common.Page;
import tydic.meta.module.tbl.*;
import tydic.meta.module.tbl.apply.TableApplyAction;
import tydic.meta.module.tbl.diff.DiffAnysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;


/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 表类全息视图Action <br>
 * @date 2011-10-41
 */
public class TableViewAction {

    /**
     * 数据处理类
     */
    private MetaTablesDAO metaTablesDAO;
    private MetaDataSourceDAO metaDataSourceDAO;
    private MetaTableGroupDAO metaTableGroupDAO;
    private MetaSysCodeDAO metaSysCodeDAO;
    private MetaMagUserTabRelDAO metaMagUserTabRelDAO;
    private MetaTableColsDAO metaTableColsDAO;
    private MetaTableInstDAO metaTableInstDAO;
    private MetaTablePartStatusDAO metaTablePartStatusDAO;
    public final static String SQLERROR="sqlError";
    /**
     * 查询符合条件的表类
     * @param queryData 查询条件
     * @param page 分页参数
     * @return
     */
    public List<Map<String,Object>> queryTables(Map<String,Object> queryData,Page page){
        if(page == null){
            page = new Page(0,20);
        }
        List<Map<String,Object>> returnValue = metaTablesDAO.queryMetaTables(queryData, page);
        return returnValue;
    }

    /**
     * 维护表类-查询表类信息列表
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryTablesMatain(Map<String,Object> queryData,Page page){
        if(page == null){
            page = new Page(0,20);
        }
        List<Map<String,Object>> returnValue = metaTablesDAO.queryMetaTablesMatain(queryData, page);
        return returnValue;
    }


    /**
     * 查询表类的数据源
     * 下拉框
     * @return
     */
    public List<Map<String,Object>> queryTableDataSource(){
        return metaDataSourceDAO.queryDataSourceByType(TblConstant.META_DATA_SOURCE_TABLE, TblConstant.COL_STATE_VALID);
    }

    /**
     * 加载业务类型树
     * @param beginId
     * @param endId
     * @return
     */
    public List<Map<String, Object>> queryGroupTree(int beginId, int endId,String levelRefID){
        return metaTableGroupDAO.queryGroupByBeginEndPath(beginId,endId,levelRefID);
    }

    /**
     * 动态加载业务类型子菜单数据
     * @param parentId
     * @return
     */
    public List<Map<String, Object>> querySubGroup(int parentId) {
        return metaTableGroupDAO.querySubGroup(parentId);
    }

    /**
     * 查询层次分类
     * 下拉框
     * @return
     */
    public List<Map<String,Object>> querySysCode(){
    	Map<String ,Object> tmp = new HashMap<String ,Object>();
    	List<Map<String,Object>> result = metaSysCodeDAO.querySysCodeByCodeType(TblConstant.META_SYS_CODE_TABLE_TYPE);
        return result;
    }
    
    public List<Map<String,Object>> querySysCode4Category(){
    	Map<String ,Object> tmp = new HashMap<String ,Object>();
//    	tmp.put("CODE_NAME", "全部");
//    	tmp.put("CODE_ID", "");
//    	tmp.put("CODE_ITEM", "");
    	List<Map<String,Object>> result = metaSysCodeDAO.querySysCodeByCodeType(TblConstant.META_SYS_CODE_TABLE_TYPE);
    	//result.add(0, tmp);
        return result;
    }
    
    public List<Map<String,Object>> queryLevelType(int pId){
    	List<Map<String,Object>> result = metaSysCodeDAO.querySysCodeByPid(TblConstant.META_SYS_CODE_TABLE_TYPE,pId);
        return result;
    }

    /**
     * 根据表ID取得表类变动历史
     * @param tableId
     * @return
     */
    public List<Map<String,Object>> queryHistoryByTableId(int tableId, Page page){
        if(page == null){
            page = new Page(0,5);
        }
        List<Map<String,Object>> rtnList = metaMagUserTabRelDAO.queryByTableId(tableId, page);
        return rtnList;
    }

    /**
     * 根据表ID查询表基本信息
     * @param tableId
     * @return
     */
    public Map<String, Object> queryBasicInfoByTableId(int tableId, int tableVersion){
        Map<String, Object> returnValue = metaTablesDAO.queryTableByTableId(tableId, tableVersion);
        Map<String,Object> data = new HashMap<String, Object>();
        // 查询rel表中审核相关信息
        Map<String, Object> auditData = metaMagUserTabRelDAO.queryDetailByTableIdTableVersion(tableId, tableVersion);
        returnValue.put("auditData", auditData);
        Map<String, Object> tableData = new HashMap<String, Object>();
        tableData.put("dataSourceId", returnValue.get("DATA_SOURCE_ID"));
        tableData.put("partitionSql", returnValue.get("PARTITION_SQL"));
        tableData.put("tableBusComment", returnValue.get("TABLE_BUS_COMMENT"));
        tableData.put("tableGroup",returnValue.get("TABLE_GROUP_NAME"));
        tableData.put("tableGroupId", returnValue.get("TABLE_GROUP_ID"));
        tableData.put("tableName", returnValue.get("TABLE_NAME"));
        tableData.put("tableNameCn", returnValue.get("TABLE_NAME_CN"));
        tableData.put("tableSpace", returnValue.get("TABLE_SPACE"));
        tableData.put("tableState", returnValue.get("TABLE_STATE"));
        tableData.put("tableTypeId", returnValue.get("TABLE_TYPE_ID"));
        tableData.put("tableVersion", returnValue.get("TABLE_VERSION"));
        tableData.put("tableOwner", returnValue.get("TABLE_OWNER"));
        data.put("tableData", tableData);
        List<Map<String, Object>> cols = metaTableColsDAO.queryMetaTableColsByTableId(tableId,Integer.parseInt(returnValue.get("TABLE_VERSION").toString()), null);
        List<Map<String,Object>> columnDatas = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < cols.size(); i++){
            Map<String, Object> columnData = new HashMap<String, Object>();
            columnData.put("colBusComment", cols.get(i).get("COL_BUS_COMMENT"));
            columnData.put("colBusType", cols.get(i).get("COL_BUS_TYPE"));
            columnData.put("colDatatype", cols.get(i).get("DATA_TYPE"));
            columnData.put("colName", cols.get(i).get("COL_NAME"));
            columnData.put("colNullabled", cols.get(i).get("COL_NULLABLED"));
            columnData.put("colOrder", cols.get(i).get("COL_ORDER"));
            columnData.put("colPrec", cols.get(i).get("COL_PREC"));
            columnData.put("colSize", cols.get(i).get("COL_SIZE"));
            columnData.put("defaultVal", cols.get(i).get("DEFAULT_VAL"));
            columnData.put("dimColId", cols.get(i).get("DIM_COL_ID"));
            columnData.put("dimLevel", cols.get(i).get("DIM_LEVEL"));
            columnData.put("dimTableId", cols.get(i).get("DIM_TABLE_ID"));
            columnData.put("dimTypeId", cols.get(i).get("DIM_TYPE_ID"));
            columnData.put("isPrimary", cols.get(i).get("IS_PRIMARY"));
            columnData.put("tableVersion", cols.get(i).get("TABLE_VERSION"));
            columnDatas.add(columnData);
        }
        data.put("columnDatas",columnDatas);
        TableApplyAction obj = new TableApplyAction();
        String sql = obj.generateSql(data);
        returnValue.put("generalSql", sql);
        return returnValue;
    }

    /**
     * 根据表ID查询对应列信息
     * @param tableId
     * @param tableVersion 版本
     * @param type  表操作类型
     * @return
     */
    public List<Map<String,Object>> queryColsByTableId(int tableId,int tableVersion, int type ,Page page){
//        if(page == null){
//            page = new Page(0,20);
//        }
        
        
        List<Map<String,Object>> returnValue = metaTableColsDAO.queryMetaTableColsByTableId(tableId, tableVersion, page);
        for(int i = 0; i < returnValue.size(); i++){
            if(Convert.toString(returnValue.get(i).get("COL_BUS_TYPE")).equals("0")){//是维度列
                returnValue.get(i).put("thisDim",returnValue.get(i).get("COL_ID"));
            }else{
                returnValue.get(i).put("thisDim","");
            }
        }
        //如果表类操作类型为不是修改，不做差异分析
        if(type != 2) {
        	return returnValue;
        }
        List<Map<String,Object>> tmp = metaTableColsDAO.queryValidColInfoByTableId(tableId, page);
        for(int i = 0; i < tmp.size(); i++){
            if(Convert.toString(tmp.get(i).get("COL_BUS_TYPE")).equals("0")){//是维度列
            	tmp.get(i).put("thisDim",tmp.get(i).get("COL_ID"));
            }else{
            	tmp.get(i).put("thisDim","");
            }
        }
        return MetaTableColsDAO.diffCompare(returnValue,tmp);
    }

    /**
     * 查询列信息
     * @param tableId
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryColsByTableIdNode(int tableId, Page page){
        int version =  metaTablesDAO.queryValidVersion(tableId);
        List<Map<String,Object>> returnValue = metaTableColsDAO.queryMetaTableColsByTableId(tableId, version, null);
        for(int i = 0; i < returnValue.size(); i++){
            if(Convert.toString(returnValue.get(i).get("COL_BUS_TYPE")).equals("0")){//是维度列
                returnValue.get(i).put("thisDim",returnValue.get(i).get("COL_ID"));
            }else{
                returnValue.get(i).put("thisDim","");
            }
        }
        return returnValue;
    }

    /**
     * 根据维度列ID查询维度信息
     * @param colId
     * @return
     */
    public Map<String,Object> queryDimInfoByColId(int colId,int tableVersion){
        return metaTableColsDAO.queryDimInfoByColId(colId,tableVersion);
    }

    /**
     * 根据表ID查询对应表实例信息
     * @param tableId
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryTableInstanceByTableId(int tableId,int tableVersion, Page page){
        if(page == null){
            page = new Page(0,10);
        }
        return metaTableInstDAO.queryTableInstanceByTableIdAnVersion(tableId, tableVersion, page);
    }

    /**
     * 查询所有表分区状态
     * @param page
     * @param
     * @return
     */
    public List<Map<String, Object>> queryTablePartStatueById(int tableInstId,Page page){
    	if(null == page)
    	{
    		page = new Page(0,10);
    	}
		return metaTablePartStatusDAO.queryTablePartStatueById(tableInstId,page);
    }
    
    /**
     * 查询表ID查询下拉菜单中的表名
     * @return
     */
    public List<Map<String, Object>> queryTableName(int tableId){
    	return metaTablePartStatusDAO.queryAllTableName(tableId);
    }
    
    /**
     * 查询实例表的字段
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryDataAnalysisById(int tableId){
		return metaTablePartStatusDAO.queryDataAnalysisByTableId(tableId);
    }
    
    /**
     * 根据前台传来的SQL获取当前实例表的列名
     * 结合表类数据源，用户信息，执行查询脚本
     * @param sql
     * @return
     */
    public Map<String, Object> queryDataClosBySql(String sql, int tableId, int tableVersion){
    	//解析SQL语句作为表头
    	String tableHead = sql.split("FROM")[0].split("SELECT")[1];
    	//存放数据库查询的所有数据
    	List<Map<String, Object>> list = null;
    	//返回存放表格初始化参数需要的条件
    	Map<String, Object> returnMap = new HashMap<String, Object>();
    	//保存所有列名
    	String colNames = "";
    	//保存列ID
    	List<String> colIds = new ArrayList<String>();
    	//设置对其方式
    	String alignType = "";
    	//设置只读
    	String onlyRead = "";
    	for (int i = 0; i < tableHead.split(",").length; i++) {
    		colIds.add(Common.tranColumnToJavaName(tableHead.split(",")[i]));
    		colNames += Common.tranColumnToJavaName(tableHead.split(",")[i]);
    		colNames += ",";
    		alignType += "center,";
    		onlyRead += "ro,";
		}

        //取数据源信息
        Map<String, Object> dataSourceInfo = metaTableInstDAO.queryDataSourceInfoByTableId(tableId, tableVersion);
        if(dataSourceInfo == null){
            returnMap.put("ERROR", "SQLERROR");
            returnMap.put("ERRORMSG", "无效的数据源！");
        }else{
            try {
                list =  metaTablePartStatusDAO.queryDataClosBySql(sql, dataSourceInfo);
            } catch (Exception e) {
                returnMap.put("ERROR", "SQLERROR");
                returnMap.put("ERRORMSG", e.getMessage());
            }
        }

		returnMap.put("rowDatas", list);
		returnMap.put("colNames", colNames);
		returnMap.put("tableHead",tableHead);
		returnMap.put("colIds", colIds);
		returnMap.put("alignType", alignType);
		returnMap.put("onlyRead", onlyRead);
		return returnMap;
    }
    
    /**
     * 查询字段分析数据
     * @param sql
     * @return
     */
    public List<Map<String, Object>> queryDataClos(String sql, int tableId, int tableVersion){
    	List<Map<String, Object>>  list = null;
        //取数据源信息
        Map<String, Object> dataSourceInfo = metaTableInstDAO.queryDataSourceInfoByTableId(tableId, tableVersion);
    	try {
			list = metaTablePartStatusDAO.queryDataClosBySql(sql, dataSourceInfo);
		} catch (Exception e) {
			//  Auto-generated catch block
			e.printStackTrace();
		}
    	return list;
    }


    /**
     * 历史版本比较
     * @param data
     * @return
     */
    public List<Map<String,Object>> historyCompare(Map<String, Object> data){
        int tableId = Integer.parseInt(data.get("tableId").toString());
        int tableVersion1 = Integer.parseInt(data.get("tableVersion1").toString());
        int tableVersion2 = Integer.parseInt(data.get("tableVersion2").toString());

        //第一个版本的列信息
        List<Map<String,Object>> tableColumns1=metaTableColsDAO.queryMetaTableColsByTableId(tableId,tableVersion1,null);
        //第二个版本的列信息
        List<Map<String,Object>> tableColumns2=metaTableColsDAO.queryMetaTableColsByTableId(tableId,tableVersion2,null);

        return HistoryCompare.diffCompare(tableColumns1, tableColumns2);
    }


    
    public void setMetaTableInstDAO(MetaTableInstDAO metaTableInstDAO) {
        this.metaTableInstDAO = metaTableInstDAO;
    }

    public void setMetaTableColsDAO(MetaTableColsDAO metaTableColsDAO) {
        this.metaTableColsDAO = metaTableColsDAO;
    }

    public void setMetaMagUserTabRelDAO(MetaMagUserTabRelDAO metaMagUserTabRelDAO) {
        this.metaMagUserTabRelDAO = metaMagUserTabRelDAO;
    }

    public void setMetaSysCodeDAO(MetaSysCodeDAO metaSysCodeDAO) {
        this.metaSysCodeDAO = metaSysCodeDAO;
    }

    public void setMetaTableGroupDAO(MetaTableGroupDAO metaTableGroupDAO) {
        this.metaTableGroupDAO = metaTableGroupDAO;
    }

    public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO) {
        this.metaTablesDAO = metaTablesDAO;
    }

    public void setMetaDataSourceDAO(MetaDataSourceDAO metaDataSourceDAO) {
        this.metaDataSourceDAO = metaDataSourceDAO;
    }
    
    public void setMetaTablePartStatusDao(MetaTablePartStatusDAO metaTablePartStatusDAO){
    	this.metaTablePartStatusDAO = metaTablePartStatusDAO;
    }
}
