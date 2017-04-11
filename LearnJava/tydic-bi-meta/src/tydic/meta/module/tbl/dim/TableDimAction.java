package tydic.meta.module.tbl.dim;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Common;
import tydic.meta.module.dim.merge.DimMergeDAO;
import tydic.meta.module.tbl.*;
import tydic.meta.web.session.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011，Tianyuan DIC Information Co., Ltd. All rights reserved. 维度表相关操作类，包括：维度表添加、维度表修改等
 * @author 张伟
 * @date 2011-10-27
 */
public class TableDimAction{

    private MetaTablesDAO metaTablesDAO;
    private MetaDimTablesDAO metaDimTablesDAO;
    private MetaDimTypeDAO metaDimTypeDAO;
    private MetaDimLevelDAO metaDimLevelDAO;
    private MetaMagUserTabRelDAO metaMagUserTabRelDAO;
    private MetaTableColsDAO metaTableColsDAO;
    private MetaUpdateDimTableDao metaUpdateDimTableDao;
    private MetaDataSourceDAO metaDataSourceDAO;
    private DimMergeDAO dimMergeDAO;
    private MetaTableRelDAO metaTableRelDAO;

	/**
     * 新增维度表申请
     * @param dimData 维度数据，数据结构比较复杂，内部结构如下:
     * {
     *     columnDatas:[{colBusComment: "主键，ID",colBusType: 1,colDatatype: "NUMBER",colName: "ZONE_ID",
     *                  colNullabled: 0,colOrder: 1,colPrec: 2,colSize: 3,defaultVal: "",fullDataType: "NUMBER(3,2)",
     *                  isPrimary: 1,tableState: 2,tableVersion: 1},{...}]
     *     dimTableData: {dataSourceId: "165",dimKeyColId: "ZONE_PAR_ID",tableDimLevel: 3,tableDimPrefix: "ZONE",
     *                    tableName: "ZONE_TEST_{YYYY}" ,tableState: 2 ,tableVersion: 1}
     *     dimTypeDates: [{dimTypeCode: "1",dimTypeDesc: "",dimTypeName: "时间维度",lastLevelFlag: 0,
     *                    tableState: 2,tableVersion: 0,dimLevelDatas:[{dimLevel: "1",dimLevelName: "年",tableState: 2
     *                    tableVersion: 1},{...}]},{...}]
     *     tableData: {dataSourceId: "165",partitionSql: "",tableBusComment: "",tableGroup: "地域类",tableGroupId: "3"
     *                ,tableName: "ZONE_TEST_{YYYY}",tableNameCn: "地域维度测试",tableOwner: "META",tableSpace: "USERS"
     *                 tableState: 2,tableTypeId: 2,tableVersion: 0}
     * }
     * @return
     */
    public boolean insertDim(Map<String, Object> dimData){
        @SuppressWarnings("unchecked") Map<String, Object> tableData = (Map<String, Object>) dimData.get("tableData");
        try{
            BaseDAO.beginTransaction();
            //新增表类定义
            long tableId = metaTablesDAO.insertMetaTables(tableData);
            //新增维度表扩展定义
            @SuppressWarnings("unchecked") Map<String, Object> dimTablesData = (Map<String, Object>) dimData
                    .get("dimTableData");
            if(dimTablesData == null){
                throw new IllegalStateException("维度表扩展信息缺失，新增维度表失败!");
            }
            dimTablesData.put("dimTableId", tableId);
            dimTablesData.put("lastLevelFlag", tableData.get("lastLevelFlag"));
            //父子节点
            String dimKeyColId =
                    dimTablesData.get("dimKeyColId") != null ? dimTablesData.get("dimKeyColId").toString() : "";
            String dimParKeyColId =
                    dimTablesData.get("dimParKeyColId") != null ? dimTablesData.get("dimParKeyColId").toString() : "";
            //新增列信息定义
            @SuppressWarnings("unchecked") List<Map<String, Object>> columnDatas = (List<Map<String, Object>>) dimData
                    .get("columnDatas");
            if(columnDatas == null || columnDatas.size() == 0){
                throw new IllegalStateException("新增维表，列不能为空,新增失败!");
            }
            Map<String, Object> key = null;
            Map<String, Object> parkey = null;
            for(Map<String, Object> column : columnDatas){
                column.put("tableId", tableId);
                if(column.get("colName").equals(dimKeyColId)){
                    dimTablesData.put("dimKeyColId", metaTableColsDAO.insertMetaTableCols(column));
                    key = column;
                }
                if(column.get("colName").equals(dimParKeyColId)){
                    dimTablesData.put("dimParKeyColId", metaTableColsDAO.insertMetaTableCols(column));
                    parkey = column;
                }
                if(column.get("colId")!=null&&!column.get("colId").equals("")){
                }else{
                    column.put("colId", metaTableColsDAO.queryForNextVal("SEQ_TAB_COL_ID"));
                }

            }
            if(key != null){
                columnDatas.remove(key);
            }
            if(parkey != null){
                columnDatas.remove(parkey);
            }
            metaTableColsDAO.insertBatch(columnDatas);
            metaDimTablesDAO.insertMetaDimTables(dimTablesData);
            //新增META_DIM_TYPE表信息
            @SuppressWarnings("unchecked") List<Map<String, Object>> dimTypeDates = (List<Map<String, Object>>) dimData
                    .get("dimTypeDates");
            if(dimTypeDates == null || dimTypeDates.size() == 0){
                throw new IllegalStateException("维表编码信息不能为空，新增失败");
            }
            for(Map<String, Object> dimTypeData : dimTypeDates){
                dimTypeData.put("dimTableId", tableId);
                long dimTypeId = metaDimTypeDAO.insertDimType(dimTypeData);
                //新增维度层级
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> levels = (List<Map<String, Object>>) dimTypeData.get("dimLevelDatas");
                if(levels != null && levels.size() > 0){
                    for(Map<String, Object> levle : levels){
                        levle.put("dimTableId", tableId);
                        levle.put("dimTypeId", dimTypeId);
                    }
                    metaDimLevelDAO.insertBatch(levels);
                }
            }
            //新增描述用户与表的关系META_MAG_USER_TAB_REL
            Map<String, Object> userTabRel = new HashMap<String, Object>();
            userTabRel.put("userId", SessionManager.getCurrentUserID());
            userTabRel.put("tableId", tableId);
            userTabRel.put("relType", TblConstant.USER_TAB_REL_TYPE_APPLY);
            userTabRel.put("tableState", tableData.get("tableState"));
            userTabRel.put("tableVersion", tableData.get("tableVersion"));
            userTabRel.put("tableName", tableData.get("tableName"));
            metaMagUserTabRelDAO.insertMetaMagUserTable(userTabRel);
            
            for(Map<String, Object> column : columnDatas){
                //如果有维度列信息
            	if(Common.parseInt(column.get("dimColId"))!=null && Common.parseInt(column.get("dimColId")) > 0){
            		// 保存表类关系信息
                    Map<String, Object> insertData = new HashMap<String, Object>();
                    insertData.put("tableId1ColIds", column.get("colId"));
                    insertData.put("tableId2ColIds", Convert.toString(column.get("dimColId")));
                    insertData.put("tableRelDesc", "");
                    insertData.put("tableRelType", 1);
                    insertData.put("tableId1", tableId);
                    insertData.put("tableId2", Common.parseInt(column.get("dimTableId")));
                    if(!metaTableRelDAO.checkExist(insertData)){
                        metaTableRelDAO.insertMetaTableRel(insertData);
                    }
            	}
			}
            BaseDAO.commit();
        } catch(Exception e){
            BaseDAO.rollback();
            tydic.frame.common.Log.error("", e);
            return false;
        }
        return true;
    }

    /**
     * 修改维度申请
     * @param dimData 维度数据，数据结构比较复杂，内部结构如下:
     * {
     *     columnDatas:[{colBusComment: "主键，ID",colBusType: 1,colDatatype: "NUMBER",colName: "ZONE_ID",
     *                  colNullabled: 0,colOrder: 1,colPrec: 2,colSize: 3,defaultVal: "",fullDataType: "NUMBER(3,2)",
     *                  isPrimary: 1,tableState: 2},{...}]
     *     dimTableData: {dataSourceId: "165",dimKeyColId: "ZONE_PAR_ID",tableDimLevel: 3,tableDimPrefix: "ZONE",
     *                    tableName: "ZONE_TEST_{YYYY}" ,tableState: 2 ,tableVersion: 1}
     *     dimTypeDates: [{dimTypeCode: "1",dimTypeDesc: "",dimTypeName: "时间维度",lastLevelFlag: 0,
     *                    tableState: 2,tableVersion: 0,dimLevelDatas:[{dimLevel: "1",dimLevelName: "年",tableState: 2
     *                    tableVersion: 1},{...}]},{...}]
     *     tableData: {dataSourceId: "165",partitionSql: "",tableBusComment: "",tableGroup: "地域类",tableGroupId: "3"
     *                ,tableName: "ZONE_TEST_{YYYY}",tableNameCn: "地域维度测试",tableOwner: "META",tableSpace: "USERS"
     *                 tableState: 2,tableTypeId: 2,tableVersion: 0}
     * }
     * @return
     */
    public Map<String ,String> updateDim(Map<String, Object> dimData){
        @SuppressWarnings("unchecked") Map<String, Object> tableData = (Map<String, Object>) dimData.get("tableData");
        Map<String ,String> result = new HashMap<String,String>();
        try{
            BaseDAO.beginTransaction();
            
            //获取表ID
            int tableId = Integer.parseInt(tableData.get("tableId").toString());
            //获取表版本
        	//int tableVersion = Integer.parseInt(tableData.get("tableVersion"));
            //查询最大版本号
            int maxVersion=metaTablesDAO.queryMaxVersion(tableId);
            //表名
            String tableName = tableData.get("tableName").toString();
            String tableOwner = MapUtils.getString(tableData,"tableOwner");
            //（未修改前的）根据表ID和版本获取表的列信息。
        	List<Map<String, Object>> dimColsData = metaTableColsDAO.queryMetaTableColsByTableId(tableId, maxVersion,null);
        	
        	//获取数据源
        	Map<String,Object> dataSource = metaDataSourceDAO.queryDataSourceById(Integer.parseInt(tableData.get("dataSourceId").toString()));
        	
        	/*根据表ID+版本号，更新meta_table_cols表的COL_STATE=0（以前列信息）*/
        	metaTableColsDAO.updateMetaTableColsByTableIdAndVersion(tableId, maxVersion, 0);
        	
            int currentVersion=maxVersion+1;
            tableData.put("tableVersion",currentVersion);
            //以前版本全部置为失效
            metaTablesDAO.inValidTable(tableId);
            //查询所有的表ID
//            Object[] tableIds=metaTablesDAO.queryTableIdsByTableName(tableData.get("tableName").toString());
            //新增表类定义
            metaTablesDAO.insertMetaTableByPk(tableData);
            //新增维度表扩展定义
            @SuppressWarnings("unchecked") Map<String, Object> dimTablesData = (Map<String, Object>) dimData
                    .get("dimTableData");
            if(dimTablesData == null){
                throw new IllegalStateException("维度表扩展信息缺失，新增维度表失败!");
            }
            dimTablesData.put("dimTableId", tableId);
            dimTablesData.put("lastLevelFlag", tableData.get("lastLevelFlag"));
            //父子节点
            String dimKeyColId =
                    dimTablesData.get("dimKeyColId") != null ? dimTablesData.get("dimKeyColId").toString() : "";
            String dimParKeyColId =
                    dimTablesData.get("dimParKeyColId") != null ? dimTablesData.get("dimParKeyColId").toString() : "";
            //新增列信息定义
            @SuppressWarnings("unchecked") List<Map<String, Object>> columnDatas = (List<Map<String, Object>>) dimData
                    .get("columnDatas");
            if(columnDatas == null || columnDatas.size() == 0){
                throw new IllegalStateException("新增维表，列不能为空,新增失败!");
            }
            Map<String, Object> key = null;
            Map<String, Object> parkey = null;
//            metaTableColsDAO.inValidTable(tableIds);
            for(Map<String, Object> column : columnDatas){
                column.put("tableId", tableId);
                column.put("tableVersion",currentVersion);
                if(column.get("colName").equals(dimKeyColId)){
                    dimTablesData.put("dimKeyColId", metaTableColsDAO.insertMetaTableCols(column));
                    key = column;
                }
                if(column.get("colName").equals(dimParKeyColId)){
                    dimTablesData.put("dimParKeyColId", metaTableColsDAO.insertMetaTableCols(column));
                    parkey = column;
                }
                if(column.get("colId")!=null&&!column.get("colId").equals("")){
                }else{
                    column.put("colId", metaTableColsDAO.queryForNextVal("SEQ_TAB_COL_ID"));
                }
            }
            if(key != null){
                columnDatas.remove(key);
            }
            if(parkey != null){
                columnDatas.remove(parkey);
            }
            metaTableColsDAO.insertBatch(columnDatas);
            metaDimTablesDAO.deleteMetaDimTablesByTableId(tableId);
            metaDimTablesDAO.insertMetaDimTables(dimTablesData);
            //新增META_DIM_TYPE表信息
            @SuppressWarnings("unchecked") List<Map<String, Object>> dimTypeDates = (List<Map<String, Object>>) dimData
                    .get("dimTypeDates");
            
            if(dimTypeDates == null || dimTypeDates.size() == 0){
                throw new IllegalStateException("维表编码信息不能为空，新增失败");
            }
//            metaDimTypeDAO.deleteDimTypeByTableId(tableId);
            metaDimLevelDAO.deleteLevelByTableId(tableId);
            List<Long> dimTypeIds=new ArrayList<Long>();
            for(Map<String, Object> dimTypeData : dimTypeDates){
                long dimTypeId=0l;
                dimTypeData.put("dimTableId", tableId);
                if(dimTypeData.get("dimTypeId")!=null&&!dimTypeData.get("dimTypeId").equals("")){
                    //有类型ID，为修改。
                    dimTypeId= Long.parseLong(dimTypeData.get("dimTypeId").toString());
                    metaDimTypeDAO.updateDimType(dimTypeData);
                }else{
                    dimTypeData.put("dimTypeState",TblConstant.META_DIM_TYPE_STATE_VALID);
                    dimTypeId = metaDimTypeDAO.insertDimType(dimTypeData);
                }
                dimTypeIds.add(dimTypeId);
                //新增维度层级
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> levels = (List<Map<String, Object>>) dimTypeData.get("dimLevelDatas");
                if(levels != null && levels.size() > 0){
                    for(Map<String, Object> levle : levels){
                        levle.put("dimTableId", tableId);
                        levle.put("dimTypeId", dimTypeId);
                    }
                    metaDimLevelDAO.insertBatch(levels);
                }
            }
            metaDimTypeDAO.inValidDimTypeByNotInclude(tableId,dimTypeIds);
            //新增描述用户与表的关系META_MAG_USER_TAB_REL
            Map<String, Object> userTabRel = new HashMap<String, Object>();
            userTabRel.put("userId", SessionManager.getCurrentUserID());
            userTabRel.put("tableId", tableId);
            userTabRel.put("relType", TblConstant.USER_TAB_REL_TYPE_MODIFY);
            userTabRel.put("tableName", tableData.get("tableName"));
            userTabRel.put("tableState", tableData.get("tableState"));
            userTabRel.put("tableVersion", tableData.get("tableVersion"));
            metaMagUserTabRelDAO.insertMetaMagUserTable(userTabRel);
            userTabRel.put("relType", TblConstant.USER_TAB_REL_TYPE_PASS);
            metaMagUserTabRelDAO.insertMetaMagUserTable(userTabRel);	//插入一条维度表审核通过记录
            /*
             * 新增META_TABLE_REL数据
             * 步骤：1.删除原有数据：根据tableId
             * 		2.新增现有数据
             * 修改人：吴喜丽
             * 修改时间：2012-01-06
             */
            Map<String, Object> dimTableDatas = new HashMap<String, Object>();
            for(Map<String, Object> m : columnDatas){
            	//如果有维度列信息
            	if(Common.parseInt(m.get("dimColId")) != null && Common.parseInt(m.get("dimColId")) > 0){
            		// 保存表类关系信息
                    Map<String, Object> insertData = new HashMap<String, Object>();
                    insertData.put("tableId1ColIds", m.get("colId"));
                    insertData.put("tableId2ColIds", Convert.toString(m.get("dimColId")));
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
            boolean isHaveTable =  dimMergeDAO.isExitsTable(tableName,tableOwner);
            if(true == isHaveTable){
        	   List<Map<String, Object>> newDimColsData = metaTableColsDAO.queryMetaTableColsByTableId(tableId, currentVersion,null);
               DimTableContrast tableContrast = new DimTableContrast();
               //比较修改前和修改后的列长度.列的长度有变化说明新增了列信息，则修改表结构
               try{
            	   	List<String> sql = new ArrayList<String>();
	           		for (Map<String, Object> map1 : newDimColsData) {
	           			int colId1 = Convert.toInt(map1.get("COL_ID"));
	           			boolean existColObj = false;//判断是否在修改前的数据中存在该对象
	           			boolean colNameSame = false;//判断前后列名是否相同。
	           			boolean nullAbledSame = false;
	           			boolean defaultValueSame = false;
	           			String oldColName = "";
	           			for (Map<String, Object> map2 : dimColsData) {
	           				int colId2 = Convert.toInt(map2.get("COL_ID"));
	           				if(colId1 == colId2) {
	           					colNameSame = Convert.toString(map2.get("COL_NAME")).toUpperCase().equals(Convert.toString(map1.get("COL_NAME")));
	           					if(!colNameSame) {
	           						oldColName = Convert.toString(map2.get("COL_NAME"));
	           					}
	           					if(Convert.toInt(map1.get("COL_NULLABLED")) == Convert.toInt(map2.get("COL_NULLABLED"))){
	           						nullAbledSame = true;
	           					}
	           					if(Convert.toString(map1.get("DEFAULT_VAL")).equals(Convert.toString(map2.get("DEFAULT_VAL")))){
	           						defaultValueSame = true;
	           					}
	           					existColObj = true;
	           					dimColsData.remove(map2);
	           					break;
	           				}
	           			}
	           			//如果不存在如果在修改前的数据中不存在，则生成一个修改表SQL,新增一个列（ALTER TABLE ADD COLUMN）
	           			if(!existColObj) {
	           				if(Convert.toInt(map1.get("COL_NULLABLED")) == 0) {
	           					sql.add("alter table "+tableName+" add("+Convert.toString(map1.get("COL_NAME"))+" "+Convert.toString(map1.get("COL_DATATYPE"))+" NOT NULL) ");
	           				}else {
	           					sql.add("alter table "+tableName+" add("+Convert.toString(map1.get("COL_NAME"))+" "+Convert.toString(map1.get("COL_DATATYPE"))+")");
	           				}
	           			}else {
	           				String tmp = "";
	           				if(Convert.toInt(map1.get("COL_NULLABLED")) == 0 && !nullAbledSame) {
	           					tmp = "NOT NULL";
	           				}else if(Convert.toInt(map1.get("COL_NULLABLED")) == 1 && !nullAbledSame){
	           					tmp = "NULL";
	           				}
	           				                                                                  
	           				if(colNameSame) {//如果相同，则生成一个简单的ALTER语句，其中，动态字段不需要。
	           					sql.add("ALTER TABLE "+tableName+" MODIFY("+Convert.toString(map1.get("COL_NAME"))+" "+Convert.toString(map1.get("COL_DATATYPE"))+" "+tmp+")");
	           				}else {//如果不同，生成一个ALTER列名的语句(ALTER  RENAME TO )
	           					sql.add("ALTER TABLE "+tableName+" RENAME COLUMN "+oldColName+" TO "+Convert.toString(map1.get("COL_NAME"))+"");
	           					sql.add("ALTER TABLE "+tableName+" MODIFY("+Convert.toString(map1.get("COL_NAME"))+" "+Convert.toString(map1.get("COL_DATATYPE"))+" "+tmp+")");
	           				}
	           				
	           				if(!defaultValueSame) {
	           					sql.add("ALTER TABLE "+tableName+" MODIFY   "+Convert.toString(map1.get("COL_NAME"))+"  "+Convert.toString(map1.get("COL_DATATYPE"))+" DEFAULT '"+Convert.toString(map1.get("DEFAULT_VAL"))+"' ");
	           				}
	           			}
	           			//sql.add("ALTER TABLE "+tableName+" ADD CONSTRAINT df_"+Convert.toString(map1.get("COL_NAME"))+" DEFAULT "+Convert.toString(map1.get("DEFAULT_VAL"))+" FOR "+Convert.toString(map1.get("COL_NAME"))+"");
	           			//ALTER TABLE T_DIM_TEST3 MODIFY   mod_flag  NUMBER(18) DEFAULT 10
	               		if(map1.get("COL_BUS_COMMENT") != null){
	               			sql.add("COMMENT ON COLUMN "+tableName+"."+map1.get("COL_NAME")+" IS '"+map1.get("COL_BUS_COMMENT").toString()+"'");
	               		} else {
	               			sql.add("COMMENT ON COLUMN "+tableName+"."+map1.get("COL_NAME")+" IS '' ");
	               		}
	           		}
	           		metaUpdateDimTableDao.addOrUpdateDimTableCol(sql);
               }catch(Exception e) {
            	   result.put("exception", ""+e.getMessage());
               }
               /*
               if(newDimColsData.size() > dimColsData.size()){
               	//获取增加字段信息：contrastList
	               	List<Map<String, Object>> contrastList = tableContrast.modifyTableStructure(dimColsData,newDimColsData);
	               	List<String> addSql = new ArrayList<String>();
	               	for (int i = 0; i < contrastList.size(); i++) {
	               		addSql.add("alter table "+tableName+" add("+contrastList.get(i).get("COL_NAME")+" "+contrastList.get(i).get("COL_DATATYPE")+")");
	               		if(contrastList.get(i).get("COL_BUS_COMMENT") != null){
	               			addSql.add("COMMENT ON COLUMN "+tableName+"."+contrastList.get(i).get("COL_NAME")+" IS '"+contrastList.get(i).get("COL_BUS_COMMENT").toString()+"'");
	               		}
	   				}
	               	//执行添加字段
	           		metaUpdateDimTableDao.addDimTableCol(addSql);
               }else if(newDimColsData.size() == dimColsData.size()){	//长度相等时值修改表字段信息结构
            	   List<String> updateSql = new ArrayList<String>();
            	   for (int i = 0; i < dimColsData.size(); i++) {
            		   updateSql.add("COMMENT ON COLUMN "+tableName+"."+dimColsData.get(i).get("COL_NAME")+" IS '"+dimColsData.get(i).get("COL_BUS_COMMENT")+"'");
            	   }
            	   metaUpdateDimTableDao.updateDimTableCol(updateSql);
               }
               **/
           }
        } catch(Exception e){
            tydic.frame.common.Log.error(null,e);
            BaseDAO.rollback();
            result.put("result", "false");
            return result;
        }
        result.put("result", "true");
        return result;
    }

    public void setMetaDataSourceDAO(MetaDataSourceDAO metaDataSourceDAO) {
		this.metaDataSourceDAO = metaDataSourceDAO;
	}

	/**
     * 根据表ID查询维度表信息。
     * @param tableId
     * @return
     */
    public Map<String, Object> queryDimTablesInfoById(int tableId){
        return metaDimTablesDAO.queryMetaDimTablesByTableId(tableId);
    }

    /**
     * 根据表信息查询所有列信息
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryAllColumnsByTableID(int tableId,int tableVersion){
        return metaTableColsDAO.queryMetaTableColsByTableId(tableId,tableVersion, null);
    }

    public Map<String,Object> queryTableByIdAndVersion(int tableId,int tableVersion){
        Map<String,Object> rtnMap = metaTablesDAO.queryTablesByIdAndVersion(tableId,tableVersion);
        Map<String, Object> auditMap = metaMagUserTabRelDAO.queryDetailByTableIdTableVersion(tableId, tableVersion);
        rtnMap.put("auditData", auditMap);
        return rtnMap;
    }

    /**
     * 根据表ID和最大版本重置其状态（维度表修改后不审核）
     * @param dimData
     */
    public void updataDimTableSateByTableIdAndVersion(Map<String, Object> dimData){
    	try {
    		Map<String, Object> tableData = (Map<String, Object>) dimData.get("tableData");
    		 //获取表ID
            int tableId = Integer.parseInt(tableData.get("tableId").toString());
            //查询最大版本号
            int maxVersion=metaTablesDAO.queryMaxVersion(tableId);
			metaTablesDAO.updateTableVersionVaild(tableId, maxVersion);
			BaseDAO.commit();
		} catch (Exception e) {
			 tydic.frame.common.Log.error(null,e);
	         BaseDAO.rollback();
		}
    }
    public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO){
        this.metaTablesDAO = metaTablesDAO;
    }

    public void setMetaDimTablesDAO(MetaDimTablesDAO metaDimTablesDAO){
        this.metaDimTablesDAO = metaDimTablesDAO;
    }

    public void setMetaDimTypeDAO(MetaDimTypeDAO metaDimTypeDAO){
        this.metaDimTypeDAO = metaDimTypeDAO;
    }

    public void setMetaDimLevelDAO(MetaDimLevelDAO metaDimLevelDAO){
        this.metaDimLevelDAO = metaDimLevelDAO;
    }

    public void setMetaMagUserTabRelDAO(MetaMagUserTabRelDAO metaMagUserTabRelDAO){
        this.metaMagUserTabRelDAO = metaMagUserTabRelDAO;
    }

    public void setMetaTableColsDAO(MetaTableColsDAO metaTableColsDAO){
        this.metaTableColsDAO = metaTableColsDAO;
    }
    public void setMetaUpdateDimTableDao(MetaUpdateDimTableDao metaUpdateDimTableDao) {
		this.metaUpdateDimTableDao = metaUpdateDimTableDao;
	}
    public void setDimMergeDAO(DimMergeDAO dimMergeDAO) {
		this.dimMergeDAO = dimMergeDAO;
	}
    public void setMetaTableRelDAO(MetaTableRelDAO metaTableRelDAO) {
        this.metaTableRelDAO = metaTableRelDAO;
    }
}
