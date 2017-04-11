package tydic.meta.module.tbl.diff;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.Log;
import tydic.meta.common.Page;
import tydic.meta.module.tbl.MetaDataSourceDAO;
import tydic.meta.module.tbl.MetaTableColsDAO;
import tydic.meta.module.tbl.MetaTableInstDAO;
import tydic.meta.module.tbl.MetaTablesDAO;
import tydic.meta.module.tbl.apply.TableApplyAction;
import tydic.meta.module.tbl.apply.TableApplyDAO;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 谭红涛
 * @date 2011-11-14
 * @description 表类差异预警Action类
 */
public class DiffAction {

    private DiffDAO diffDAO;

    private MetaTableColsDAO metaTableColsDAO;

    private MetaTableInstDAO metaTableInstDAO;

    private TableApplyDAO tableApplyDAO;

    private MetaDataSourceDAO metaDataSourceDAO;

    private MetaTablesDAO metaTablesDAO;
    /**
     * 获取
     * @param startDate
     * @param endDate
     * @param dataSourceId
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryDiffList(Date startDate,Date endDate,String dataSourceId,Page page){
        if(page==null){
            page=new Page(0,10);
        }
        endDate.setHours(23);
        endDate.setMinutes(59);
        endDate.setSeconds(59);
        return diffDAO.queryDiffList(startDate, endDate, dataSourceId, page);
    }


    /**
     * 根据数据源查询所有的差异表类
     * @param dataSourceId
     * @return
     */
    public List<Map<String,Object>> queryDetialDiffList(int dataSourceId,String dateStr, Page page){
        if(page==null){
            page=new Page(0,10);
        }
        return diffDAO.queryDetialDiffList(dataSourceId, dateStr, page);
    }

    /**
     * 表类和具体实例差异分析。
     * @param tableId  表ID
     * @param tableVersion 表版本号
     * @param insId  实例ID
     * @return    返回结果，此处规定，如果List长度为1，且Map长度为1且键值为errorMessage表示异常。由前台展现。
     */
    public List<Map<String,Object>> diffAnalysis(int tableId,int tableVersion
            ,int insId){
        //返回结果，此处规定，如果List长度为1，且Map长度为1且键值为errorMessage表示异常。由前台展现。
        List<Map<String,Object>> result=new ArrayList<Map<String, Object>>();
        Map<String,Object> error=new HashMap<String, Object>();
        //获取表类当前版本所有列信息。
        List<Map<String,Object>> tableColumnsConfig=metaTableColsDAO.queryMetaTableColsByTableId(tableId,tableVersion,null);
        //根据实例ID获取表实例信息。
        Map<String,Object> intsInfo= metaTableInstDAO.queryInstInfoById(insId);
        //查询数据源信息。
        int dataSourceId= Integer.parseInt(intsInfo.get("DATA_SOURCE_ID").toString());
        Map<String,Object> dataSource= metaDataSourceDAO.queryDataSourceById(dataSourceId);
        //表拥有者。
        String owner=(intsInfo.get("TABLE_OWNER")!=null&&!intsInfo.get("TABLE_OWNER").toString().equals(""))
                ?intsInfo.get("TABLE_OWNER").toString():dataSource.get("DATA_SOURCE_USER").toString();
        //实例表明。
        String insTableName=intsInfo.get("TABLE_NAME").toString();
        List<Map<String,Object>> instTableColumns=null;
        try{
            //根据实例表信息到对应数据源查询实例表列信息。
            instTableColumns= tableApplyDAO.queryDbTableColumns(intsInfo.get("DATA_SOURCE_ID").toString(),owner,insTableName);
        }catch(Exception e){
            Log.error(null,e);
            error.put("_errorMessage","根据数据源查询实例表列信息失败,可能数据库中不存在此数据源");
            result.add(error);
            return  result;
        }
        if(instTableColumns==null||instTableColumns.size()==0){
            Log.error("无法查询到实例表列信息,或许此实例表["+insTableName+"]不存在");
            error.put("_errorMessage","无法查询到实例表列信息,或许此实例表["+insTableName+"]不存在");
            result.add(error);
            return  result;
        }
        
        //对比分析表类配置的列信息和实例表列信息之间存在的差异。
        return DiffAnysis.diffCompare(tableColumnsConfig,instTableColumns);
    }

    /**
     * 获取表类对应版本的所有实例表。
     * @param tableId
     * @return
     */
    public List<Map<String,Object>> queryInsts(int tableId){
        return metaTableInstDAO.queryTableInstanceByTableId(tableId);
    }

    /**
     * 获取仅同步结构SQL
     * @param tableId
     * @param tableVersion
     * @return
     */
    public String synchronousStructSql(int tableId,int tableVersion,String  instTableName){
        //建表SQL。
        Map<String, Object> returnValue = metaTablesDAO.queryTablesByIdAndVersion(tableId,tableVersion);
        //先删除表SQL
        String dropTableSql="DROP TABLE "+ Convert.toString(returnValue.get("TABLE_OWNER")) + "." +instTableName+";<br/>";
        Map<String,Object> data = new HashMap<String, Object>();
        Map<String, Object> tableData = new HashMap<String, Object>();
        tableData.put("dataSourceId", returnValue.get("DATA_SOURCE_ID"));
        tableData.put("partitionSql", returnValue.get("PARTITION_SQL"));
        tableData.put("tableBusComment", returnValue.get("TABLE_BUS_COMMENT"));
        tableData.put("tableName", instTableName);
        tableData.put("tableVersion", returnValue.get("TABLE_VERSION"));
        tableData.put("tableOwner", Convert.toString(returnValue.get("TABLE_OWNER")));
        data.put("tableData", tableData);
        List<Map<String, Object>> cols = metaTableColsDAO.queryMetaTableColsByTableId(tableId,Integer.parseInt(returnValue.get("TABLE_VERSION").toString()), null);
        List<Map<String,Object>> columnDatas = new ArrayList<Map<String, Object>>();
        for(int i = 0; i < cols.size(); i++){
            Map<String, Object> columnData = new HashMap<String, Object>();
            columnData.put("colBusComment", cols.get(i).get("COL_BUS_COMMENT"));
            columnData.put("colDatatype", cols.get(i).get("DATA_TYPE"));
            columnData.put("colName", cols.get(i).get("COL_NAME"));
            columnData.put("colNullabled", cols.get(i).get("COL_NULLABLED"));
            columnData.put("colPrec", cols.get(i).get("COL_PREC"));
            columnData.put("colSize", cols.get(i).get("COL_SIZE"));
            columnData.put("defaultVal", cols.get(i).get("DEFAULT_VAL"));
            columnData.put("dimColId", cols.get(i).get("DIM_COL_ID"));
            columnData.put("isPrimary", cols.get(i).get("IS_PRIMARY"));
            columnData.put("tableVersion", cols.get(i).get("TABLE_VERSION"));
            columnDatas.add(columnData);
        }
        data.put("columnDatas",columnDatas);
        TableApplyAction obj = new TableApplyAction();
        return dropTableSql+obj.generateSql(data);
    }

    /**
     * 获取同步结构和数据SQL语句。
     * @param tableId
     * @param tableVersion
     * @param instTableName
     * @param columnDiffData  包含三个键值：
     * modifyColumn:[{configColumnId:1,instColumnName:""}...],
     * addColumn:[...];//记录新增的列Id
     * deleteColumn:[...];//记录删除的实例表列名
     * columnMapping:[{configColumnId:1,instColumnName:""}...]://记录其列映射（从配置到实例之间的列映射）
     * @return SQL集合
     */
    public String[]  synchronousStructDataSql(int tableId,int tableVersion,String  instTableName,
            Map<String,Object> columnDiffData){
        //表类配置信息
        Map<String, Object> tableConfig = metaTablesDAO.queryTablesByIdAndVersion(tableId,tableVersion);
        //获取表类当前版本所有列信息。
        List<Map<String,Object>> tableColumnsConfig=metaTableColsDAO.queryMetaTableColsByTableId(tableId,tableVersion,null);
        return DiffAnysis.synchronousStructDataSql(tableConfig,tableColumnsConfig,columnDiffData,instTableName,Convert.toString(tableConfig.get("TABLE_OWNER")));
    }

    /**
     * 同步数据失败的回退操作，其基本思想就是将所有生成的临时表干掉，不管其存在与不存在。
     * @param tableNames 生成的临时表表名集合
     * @param dataSource 数据源信息
     */
    public void rollBack(String[] tableNames,Map<String,Object> dataSource){
        diffDAO.rollBack(tableNames,dataSource);
    }

    /**
     * 根据表ID和表版本查询表类数据源信息
     * @param tableId
     * @param tableVersion
     * @return
     */
    public Map<String,Object> queryDataSourceByTable(int tableId,int tableVersion){
        return metaDataSourceDAO.queryDataSourceByTable(tableId,tableVersion);
    }

    /**
     * 执行指定事情SQL语句，并返回执行结果至前台。
     * @param sql
     * @param dataSource
     * @return 包含以下键值：result：true/false; SQL执行成功或者失败
     *                    errorMessage:如果失败需要显示的Message；
     */
    public Map<String,Object> executeSQL(String sql,Map<String,Object> dataSource){
        Map<String,Object> result=new HashMap<String, Object>();
        try{
            diffDAO.executeSQL(sql,dataSource);
            result.put("result",true);
        }catch (Exception e){
            result.put("result",false);
            Log.error(null,e);
            result.put("errorMessage",e.getMessage());
        }
        return result;
    }

    /**
     * 同步完成之后的操作
     * @param tableVersion
     * @param insId
     * @param tableId
     */
    public void afterSync(int tableId ,int tableVersion,int insId){
        try{
            metaTableInstDAO.updateInstTableVersion(insId,tableVersion);
            diffDAO.updateDiffStateInVaildate(tableId);
        }catch(Exception e){
            Log.error(null,e);
        }
    }

    public void setDiffDAO(DiffDAO diffDAO){
        this.diffDAO=diffDAO;
    }

    public void setMetaTableColsDAO(MetaTableColsDAO metaTableColsDAO){
        this.metaTableColsDAO = metaTableColsDAO;
    }

    public void setMetaTableInstDAO(MetaTableInstDAO metaTableInstDAO){
        this.metaTableInstDAO = metaTableInstDAO;
    }

    public void setTableApplyDAO(TableApplyDAO tableApplyDAO){
        this.tableApplyDAO = tableApplyDAO;
    }

    public void setMetaDataSourceDAO(MetaDataSourceDAO metaDataSourceDAO){
        this.metaDataSourceDAO = metaDataSourceDAO;
    }

    public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO){
        this.metaTablesDAO = metaTablesDAO;
    }
}
