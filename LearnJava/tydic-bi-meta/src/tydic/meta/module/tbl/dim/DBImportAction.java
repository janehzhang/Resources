package tydic.meta.module.tbl.dim;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.Page;
import tydic.meta.module.tbl.MetaDimTablesDAO;
import tydic.meta.module.tbl.MetaTableColsDAO;
import tydic.meta.module.tbl.MetaTablesDAO;

import java.util.List;
import java.util.Map;

/**
 *
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @date 2012-5-8
 * @description 导入数据库维表数据
 *
 */
public class DBImportAction {

    private DBImportDAO dbImportDAO;
    private MetaTablesDAO metaTablesDAO;
    private MetaTableColsDAO metaTableColsDAO;
    private MetaDimTablesDAO metaDimTablesDAO;

    /**
     * 导入维表数据
     * @return
     */
    public String importDimInfo(final List<Map<String, Object>> rowDatas, Map<String, Object> confData){
        //横表表名
        String tableName = Convert.toString(confData.get("tableName"));
        //横表所在数据源用户信息
        String tableOwner = Convert.toString(confData.get("tableOwner"));
        //横表所在数据源ID
        int dataSourceId = Convert.toInt(confData.get("dataSourceId"));
        //保留当前数据 1：保留，2：清空
        int dataType = Convert.toInt(confData.get("dataType"), 0);
        //当前维度表表类ID
        int tableId = Convert.toInt(confData.get("tableId"));
        //表类型 1：横表，2：纵表
        int tableType = Convert.toInt(confData.get("tableType"));
        //是否追加CODE 1：否，2：是
        int codeType = Convert.toInt(confData.get("codeType"), 0);
        //当前维表归并类型
        int dimType = Convert.toInt(confData.get("dimType"));
        //动态字段
        final List<Map<String, Object>> dyColData = (List<Map<String, Object>>)confData.get("dyColData");


        //维表信息
        Map<String, Object> dimInfo = metaDimTablesDAO.queryMetaDimTablesByTableId(tableId);
        String dimPrefix = Convert.toString(dimInfo.get("TABLE_DIM_PREFIX")).toUpperCase();

        //如果为横表
        if(tableType == 1){
            //临时表表名
            final String tmpTableName = "tmp_importDim_" + System.currentTimeMillis();

            Map<String, Object> tableInfo = metaTablesDAO.queryTableByTableId(tableId);

            //临时表所在数据源以及所属用户，跟该维度表表类匹配
            final int tmpDataSourceId = Convert.toInt(tableInfo.get("DATA_SOURCE_ID"), 0);
            final String tmpTableOwner = Convert.toString(tableInfo.get("TABLE_OWNER"));
            try{
                BaseDAO.beginTransaction();
//                System.out.println("================================ 临时表："+tmpTableName);
                Map<String, Object> maxValues = dbImportDAO.queryForMax(rowDatas,tableName,tableOwner,dataSourceId,dyColData);
                boolean isEmpty = true;
                for(String key:maxValues.keySet()){
                    if(maxValues.get(key)==null){
                        maxValues.put(key,1);
                    }else{
                        isEmpty = false;
                    }
                }
                if(isEmpty){
                    BaseDAO.rollback();
                    return "对应横表无数据，无法导入！";
                }

                //根据当前维表表类所在数据源建临时表
                dbImportDAO.createTmpTable(maxValues, tmpTableName, tmpTableOwner, tmpDataSourceId);
                dbImportDAO.insertDataByDB(rowDatas, tableName, tableOwner, dataSourceId, tmpTableName, tmpTableOwner, tmpDataSourceId,dyColData);
                if(dataType == 2){//清空当前数据
                    dbImportDAO.emptyTable(Convert.toString(tableInfo.get("TABLE_NAME")), tmpTableOwner, tmpDataSourceId, dimType);
                }
                List<Map<String, Object>> colData = metaTableColsDAO.queryColIdByTableId(tableId, Convert.toInt(tableInfo.get("TABLE_VERSION")));
                for(int i = 0; i < rowDatas.size(); i++){
                    dbImportDAO.insertLevelDimData(colData, rowDatas.get(i), tableId, Convert.toString(tableInfo.get("TABLE_NAME")),
                            tmpTableName, dimType, tmpTableOwner, tmpDataSourceId, i+1, dimPrefix, rowDatas, codeType,dyColData);
                }
                BaseDAO.commit();
            }catch (Exception e){
                if(e.getMessage().contains("value too large for column")){
                    BaseDAO.rollback();
                    return "您选择的源表中的字段数据值大小超过了该维表字段的最大数据长度，不能导入！";
                }
                e.printStackTrace();
                BaseDAO.rollback();
                return "导入出错！ 出错信息：" + e.getMessage();
            }finally {
                dbImportDAO.dropTmpTable(tmpTableName, tmpTableOwner, tmpDataSourceId);
            }
        }
        // 如果为纵表
        else if(tableType == 2){
            Map<String, Object> verMap = rowDatas.get(0);
            String verCodeCol = Convert.toString(verMap.get("verCodeCol"));//纵表编码列
            String verParCodeCol = Convert.toString(verMap.get("verParCodeCol"));//纵表父编码列
            String verNameCol = Convert.toString(verMap.get("verNameCol"));//纵表名称列
            String verDescCol = Convert.toString(verMap.get("verDescCol"));//纵表描述列
            String verRootVal = Convert.toString(verMap.get("verRootVal"));//纵表根节点值

            //临时表表名
            final String tmpTableName = "tmp_importDim_" + System.currentTimeMillis();
            //维度表表类信息
            Map<String, Object> tableInfo = metaTablesDAO.queryTableByTableId(tableId);

            //临时表所在数据源以及所属用户，跟该维度表表类匹配
            final int tmpDataSourceId = Convert.toInt(tableInfo.get("DATA_SOURCE_ID"), 0);
            final String tmpTableOwner = Convert.toString(tableInfo.get("TABLE_OWNER"));
            try{
                BaseDAO.beginTransaction();
//                System.out.println("================================ 临时表："+tmpTableName);
                Map<String, Object> maxValues = dbImportDAO.queryForMax(rowDatas,tableName,tableOwner,dataSourceId,dyColData);
                boolean isEmpty = true;
                for(String key:maxValues.keySet()){
                    if(maxValues.get(key)==null){
                        maxValues.put(key,1);
                    }else{
                        isEmpty = false;
                    }
                }
                if(isEmpty){
                    BaseDAO.rollback();
                    return "对应纵表无数据，无法导入！";
                }

                //根据当前维表表类所在数据源建临时表
                dbImportDAO.createTmpTable(maxValues, tmpTableName, tmpTableOwner, tmpDataSourceId);
                dbImportDAO.insertDataByDB(rowDatas, tableName, tableOwner, dataSourceId, tmpTableName, tmpTableOwner, tmpDataSourceId,dyColData);
                int level = dbImportDAO.queryVerLevel(tableName, tableOwner, verCodeCol, verParCodeCol, verRootVal, dataSourceId);
                // 导入纵表
                if(level == 0){
                    BaseDAO.rollback();
                    return "导入失败，纵表"+tableName+"没有根节点编码值为“"+verRootVal+"”的数据！";
                }
                if(dataType == 2){//清空当前数据
                    dbImportDAO.emptyTable(Convert.toString(tableInfo.get("TABLE_NAME")), tmpTableOwner, tmpDataSourceId, dimType);
                }
                List<Map<String, Object>> colData = metaTableColsDAO.queryColIdByTableId(tableId, Convert.toInt(tableInfo.get("TABLE_VERSION")));
                for(int i=0; i<level; i++){
                    dbImportDAO.insertVerDimData(colData,rowDatas.get(0),tableId,Convert.toString(tableInfo.get("TABLE_NAME")),tmpTableName,
                            dimType,tmpTableOwner,tmpDataSourceId,i+1,dimPrefix,dyColData);
                }
                BaseDAO.commit();
            }catch (Exception e){
                if(e.getMessage().contains("value too large for column")){
                    BaseDAO.rollback();
                    return "您选择的源表中的字段数据值大小超过了该维表字段的最大数据长度，不能导入！";
                }
                e.printStackTrace();
                BaseDAO.rollback();
                return "导入出错！ 出错信息：" + e.getMessage();
            }finally {
                dbImportDAO.dropTmpTable(tmpTableName, tmpTableOwner, tmpDataSourceId);
            }
        }
        return null;
    }

    /**
     * 查询导入目标表已经存在与源表相同编码CODE的数据
     * @return
     */
    public List<Map<String, Object>> queryDupData(final List<Map<String, Object>> rowDatas, Map<String, Object> confData){
        //横表表名
        String tableName = Convert.toString(confData.get("tableName"));
        //横表所在数据源用户信息
        String tableOwner = Convert.toString(confData.get("tableOwner"));
        //横表所在数据源ID
        int dataSourceId = Convert.toInt(confData.get("dataSourceId"));
        //保留当前数据 1：保留，2：清空
        int dataType = Convert.toInt(confData.get("dataType"), 0);
        //当前维度表表类ID
        int tableId = Convert.toInt(confData.get("tableId"));
        //表类型 1：横表，2：纵表
        int tableType = Convert.toInt(confData.get("tableType"));
        //是否追加CODE 1：否，2：是
        int codeType = Convert.toInt(confData.get("codeType"), 0);
        //当前维表归并类型
        int dimType = Convert.toInt(confData.get("dimType"));
        //动态字段
        final List<Map<String, Object>> dyColData = (List<Map<String, Object>>)confData.get("dyColData");

        //维表信息
        Map<String, Object> dimInfo = metaDimTablesDAO.queryMetaDimTablesByTableId(tableId);
        String dimPrefix = Convert.toString(dimInfo.get("TABLE_DIM_PREFIX")).toUpperCase();
        //临时表表名
        final String tmpTableName = "tmp_importDim_" + System.currentTimeMillis();
        //维度表表类信息
        Map<String, Object> tableInfo = metaTablesDAO.queryTableByTableId(tableId);

        //临时表所在数据源以及所属用户，跟该维度表表类匹配
        final int tmpDataSourceId = Convert.toInt(tableInfo.get("DATA_SOURCE_ID"), 0);
        final String tmpTableOwner = Convert.toString(tableInfo.get("TABLE_OWNER"));

        //维度表实体表表名
        String tableInstName = Convert.toString(tableInfo.get("TABLE_NAME"));
        try{
            //查询临时表tmpTableName和维度实体表Convert.toString(tableInfo.get("TABLE_NAME"))中的数据是否有编码重复的
            Map<String, Object> maxValues = dbImportDAO.queryForMax(rowDatas,tableName,tableOwner,dataSourceId,dyColData);
            for(String key:maxValues.keySet()){
                if(maxValues.get(key)==null){
                    maxValues.put(key,1);
                }
            }
            //根据当前维表表类所在数据源建临时表
            dbImportDAO.createTmpTable(maxValues, tmpTableName, tmpTableOwner, tmpDataSourceId);
            dbImportDAO.insertDataByDB(rowDatas, tableName, tableOwner, dataSourceId, tmpTableName, tmpTableOwner, tmpDataSourceId,dyColData);
            List<Map<String, Object>> rtn = dbImportDAO.queryDupData(rowDatas,tmpTableName,tmpDataSourceId,tmpTableOwner,tableInstName,dimPrefix,dimType);
            return rtn;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            dbImportDAO.dropTmpTable(tmpTableName, tmpTableOwner, tmpDataSourceId);
        }
    }

    /**
     * 判断是否有重复编码
     * @param rowDatas
     * @param confData
     * @return
     */
    public boolean haveDupCode(final List<Map<String, Object>> rowDatas, Map<String, Object> confData){
        //横表表名
        String tableName = Convert.toString(confData.get("tableName"));
        //横表所在数据源用户信息
        String tableOwner = Convert.toString(confData.get("tableOwner"));
        //横表所在数据源ID
        int dataSourceId = Convert.toInt(confData.get("dataSourceId"));
        //保留当前数据 1：保留，2：清空
        int dataType = Convert.toInt(confData.get("dataType"), 0);
        //当前维度表表类ID
        int tableId = Convert.toInt(confData.get("tableId"));
        //表类型 1：横表，2：纵表
        int tableType = Convert.toInt(confData.get("tableType"));
        //是否追加CODE 1：否，2：是
        int codeType = Convert.toInt(confData.get("codeType"), 0);
        //当前维表归并类型
        int dimType = Convert.toInt(confData.get("dimType"));
        //动态字段
        final List<Map<String, Object>> dyColData = (List<Map<String, Object>>)confData.get("dyColData");
        //维表信息
        Map<String, Object> dimInfo = metaDimTablesDAO.queryMetaDimTablesByTableId(tableId);
        String dimPrefix = Convert.toString(dimInfo.get("TABLE_DIM_PREFIX")).toUpperCase();
        //临时表表名
        final String tmpTableName = "tmp_importDim_" + System.currentTimeMillis();
        //维度表表类信息
        Map<String, Object> tableInfo = metaTablesDAO.queryTableByTableId(tableId);

        //临时表所在数据源以及所属用户，跟该维度表表类匹配
        final int tmpDataSourceId = Convert.toInt(tableInfo.get("DATA_SOURCE_ID"), 0);
        final String tmpTableOwner = Convert.toString(tableInfo.get("TABLE_OWNER"));

        //维度表实体表表名
        String tableInstName = Convert.toString(tableInfo.get("TABLE_NAME"));
        try{
            // 实现
            Map<String, Object> maxValues = dbImportDAO.queryForMax(rowDatas,tableName,tableOwner,dataSourceId,dyColData);
            boolean isEmpty = true;
            for(String key:maxValues.keySet()){
                if(maxValues.get(key)==null){
                    maxValues.put(key,1);
                }else{
                    isEmpty = false;
                }
            }
            if(isEmpty){
                return false;
            }

            //根据当前维表表类所在数据源建临时表
            dbImportDAO.createTmpTable(maxValues, tmpTableName, tmpTableOwner, tmpDataSourceId);
            dbImportDAO.insertDataByDB(rowDatas, tableName, tableOwner, dataSourceId, tmpTableName, tmpTableOwner, tmpDataSourceId,dyColData);
            boolean rtn = dbImportDAO.haveDupCode(rowDatas,tmpTableName,tmpDataSourceId,tmpTableOwner,tableInstName,dimPrefix,dimType);
            return rtn;
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            dbImportDAO.dropTmpTable(tmpTableName, tmpTableOwner, tmpDataSourceId);
        }
    }

    /**
     * 纵表导入时，判断当前选中的纵表是否有断链
     * @param rowDatas
     * @param confData
     * @return
     */
    public boolean haveDisLinked(final List<Map<String, Object>> rowDatas, Map<String, Object> confData){
        //横表表名
        String tableName = Convert.toString(confData.get("tableName"));
        //横表所在数据源用户信息
        String tableOwner = Convert.toString(confData.get("tableOwner"));
        //横表所在数据源ID
        int dataSourceId = Convert.toInt(confData.get("dataSourceId"));

        Map<String, Object> verMap = rowDatas.get(0);
        String verCodeCol = Convert.toString(verMap.get("verCodeCol"));//纵表编码列
        String verParCodeCol = Convert.toString(verMap.get("verParCodeCol"));//纵表父编码列
        String verRootVal = Convert.toString(verMap.get("verRootVal"));//纵表根节点值
        return dbImportDAO.haveDisLinked(tableName,tableOwner,dataSourceId,verCodeCol,verParCodeCol,verRootVal);
    }

    /**
     * 纵表导入时，列出断链的父节点
     * @param rowDatas
     * @param confData
     * @return
     */
    public List<Map<String, Object>> queryDisLinked(final List<Map<String, Object>> rowDatas, Map<String, Object> confData){
        //横表表名
        String tableName = Convert.toString(confData.get("tableName"));
        //横表所在数据源用户信息
        String tableOwner = Convert.toString(confData.get("tableOwner"));
        //横表所在数据源ID
        int dataSourceId = Convert.toInt(confData.get("dataSourceId"));

        Map<String, Object> verMap = rowDatas.get(0);
        String verCodeCol = Convert.toString(verMap.get("verCodeCol"));//纵表编码列
        String verParCodeCol = Convert.toString(verMap.get("verParCodeCol"));//纵表父编码列
        String verNameCol = Convert.toString(verMap.get("verNameCol"));//纵表名称列
        String verRootVal = Convert.toString(verMap.get("verRootVal"));//纵表根节点值
        return dbImportDAO.queryDisLinked(tableName,tableOwner,dataSourceId,verNameCol,verCodeCol,verParCodeCol,verRootVal);
    }

    /**
     * 判断源横表或者纵表是否存在重复的编码
     * @param rowDatas
     * @param confData
     * @return
     */
    public boolean haveDupCodeOfSoucre(final List<Map<String, Object>> rowDatas, Map<String, Object> confData){
        //源表表名
        String tableName = Convert.toString(confData.get("tableName"));
        //源表所在数据源用户信息
        String tableOwner = Convert.toString(confData.get("tableOwner"));
        //源表所在数据源ID
        int dataSourceId = Convert.toInt(confData.get("dataSourceId"));
        return dbImportDAO.haveDupCodeOfSoucre(tableName,tableOwner,dataSourceId,rowDatas);
    }

    /**
     * 查询源横表或者纵表存在的重复编码
     * @param rowDatas
     * @param confData
     * @return
     */
    public List<Map<String, Object>> queryDupCodeOfSoucre(final List<Map<String, Object>> rowDatas, Map<String, Object> confData, Page page){
        if(page==null){//如果没有page，为第一页。
           page=new Page(0,20);
        }
        //源表表名
        String tableName = Convert.toString(confData.get("tableName"));
        //源表所在数据源用户信息
        String tableOwner = Convert.toString(confData.get("tableOwner"));
        //源表所在数据源ID
        int dataSourceId = Convert.toInt(confData.get("dataSourceId"));
        return dbImportDAO.queryDupCodeOfSoucre(tableName,tableOwner,dataSourceId,rowDatas,page);
    }

    /**
     * 检查选择的动态字段列里有无空值
     * @param confData
     * @return
     */
    public boolean haveNullValue(Map<String, Object> confData, String cols){
        //横表表名
        String tableName = Convert.toString(confData.get("tableName"));
        //横表所在数据源用户信息
        String tableOwner = Convert.toString(confData.get("tableOwner"));
        //横表所在数据源ID              qwer
        int dataSourceId = Convert.toInt(confData.get("dataSourceId"));
        //动态字段
        return dbImportDAO.haveNullValue(dataSourceId,tableOwner,tableName,cols);
    }


    public void setMetaDimTablesDAO(MetaDimTablesDAO metaDimTablesDAO) {
        this.metaDimTablesDAO = metaDimTablesDAO;
    }

    public void setMetaTableColsDAO(MetaTableColsDAO metaTableColsDAO) {
        this.metaTableColsDAO = metaTableColsDAO;
    }

    public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO) {
        this.metaTablesDAO = metaTablesDAO;
    }

    public void setDbImportDAO(DBImportDAO dbImportDAO) {
        this.dbImportDAO = dbImportDAO;
    }

}
