package tydic.meta.module.tbl.grab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.DataSourceManager;
import tydic.frame.common.Log;
import tydic.meta.module.mag.timer.IMetaTimer;
import tydic.meta.module.tbl.MetaDataSourceDAO;
import tydic.meta.module.tbl.MetaTableColsDAO;
import tydic.meta.module.tbl.TblConstant;
import tydic.meta.module.tbl.apply.TableApplyDAO;
import tydic.meta.module.tbl.diff.DiffAnysis;


/**
 * @author 李国民
 * @date 2012-02-27
 * @description 后台定时抓取数据（实现了定时任务接口）
 */
public class GrabData implements IMetaTimer {

    private GrabDataDAO grabDataDAO = new GrabDataDAO();
    private MetaDataSourceDAO metaDataSourceDAO = new MetaDataSourceDAO();
    private MetaTableColsDAO metaTableColsDAO = new MetaTableColsDAO();
    private TableApplyDAO tableApplyDAO = new TableApplyDAO();


    /**
     * 第一次初始化调用的方法
     */
    public void init() {

    }

    /**
     * 定时调用方法。
     */
    public void run(String timerName) {

        try {
            grabWarning();    //抓取预警信息
            grabRecord();    //抓取信息量统计
        } catch (Exception e) {
            Log.error(null, e);
        } finally {
            DataSourceManager.destroy();
        }
    }


    /**
     * 抓取统计信息量统计
     */
    private void grabRecord() {
        String owners = getOwnerString();        //得到需要查询所属的所有用户
        int instUpdateCount = 0;    //映射表修改统计数
        int updateDataOneCount = 0;    //记录表修改统计数（条件无子分区）
        int updateDataTwoCount = 0;    //记录表修改统计数（条件包含子分区）
        int insertDataCount = 0;    //记录表新增统计数
        int maxNum = 200;    //到达目标数时执行操作
        //得到所有有效的数据源信息
        List<Map<String, Object>> dataSourceList = metaDataSourceDAO.
                queryDataSourceByType(TblConstant.META_DATA_SOURCE_TABLE, TblConstant.DATA_SOURCE_VALID);
        //映射表下的修改数据信息
        List<Map<String, Object>> instUpdateList = new ArrayList<Map<String, Object>>();
        //记录信息表下的修改数据信息（条件无子分区）
        List<Map<String, Object>> updateDataOneList = new ArrayList<Map<String, Object>>();
        //记录信息表下的修改数据信息（条件包含子分区）
        List<Map<String, Object>> updateDataTwoList = new ArrayList<Map<String, Object>>();
        //记录信息表下的新增数据信息
        List<Map<String, Object>> insertDataList = new ArrayList<Map<String, Object>>();

        //循环数据源，根据不同数据源抓取数据
        for (int i = 0; i < dataSourceList.size(); i++) {
            Map<String, Object> dataSource = dataSourceList.get(i);
            try {
                //数据源id
                int dataSourceId = Integer.parseInt(dataSource.get("DATA_SOURCE_ID").toString());
                //通过数据源id得到映射关联表中对应的数据
                List<Map<String, Object>> tableInstList = grabDataDAO.getTableInstList(dataSourceId);
                //如果存在数据，进行记录数抓取
                if (tableInstList != null && tableInstList.size() > 0) {
                    //得到当前数据源下的所有分区信息
                    List<Map<String, Object>> tabPartitionList = grabDataDAO.getAllTabPartition(dataSourceId, owners);
                    //得到当前数据源下的所有表信息
                    List<Map<String, Object>> tableInfoList = grabDataDAO.getAllTablesListByDataSource(dataSourceId, owners);
                    //得到当前数据源下的所有子分区信息
                    List<Map<String, Object>> tabSubpartitionList = grabDataDAO.getAllTabSubpartition(dataSourceId, owners);

                    for (int j = 0; j < tableInstList.size(); j++) {
                        //取到其中一条数据进行数据抓取
                        Map<String, Object> tableInst = tableInstList.get(j);
                        int tableInstId = Integer.parseInt(tableInst.get("TABLE_INST_ID").toString());    //映射表中的tableInstId
                        String tableName = tableInst.get("TABLE_INST_NAME").toString().toUpperCase();    //映射表中的tableName
                        String tableOwner = tableInst.get("TABLE_OWNER").toString().toUpperCase();        //映射表中的tableOwner
                        for (int m = 0; m < tableInfoList.size(); m++) {
                            //取到数据源下的其中一条表数据，进行匹配
                            Map<String, Object> tableInfo = tableInfoList.get(m);
                            String name = tableInfo.get("TABLE_NAME").toString().toUpperCase();    //表数据中的tableName
                            String owner = tableInfo.get("OWNER").toString().toUpperCase();        //表数据中的tableOwner
                            if (tableName.equals(name) && tableOwner.equals(owner)) {
                                //找到匹配数据后，记录操作，移除该数据
                                Map<String, Object> updateInstMap = new HashMap<String, Object>();
                                updateInstMap.put("tableInstId", tableInstId);                  //映射表id
                                updateInstMap.put("tableRecords", tableInfo.get("NUM_ROWS")); //记录数
                                instUpdateList.add(updateInstMap);
                                instUpdateCount++;        //映射表修改统计数增加1
                                tableInfoList.remove(m);
                                break;
                            }
                        }
                        for (int m = 0; m < tabPartitionList.size(); m++) {
                            Map<String, Object> partitionMap = tabPartitionList.get(m);
                            String name = partitionMap.get("TABLE_NAME").toString().toUpperCase();     //分区中数据的tableName
                            String owner = partitionMap.get("TABLE_OWNER").toString().toUpperCase(); //分区中数据的tableOwner
                            if (tableName.equals(name) && tableOwner.equals(owner)) {
                                //找到匹配数据后，判断META_TABLE_INST_DATA中是否存在数据，存在则修改，不存在则新增
                                Map<String, Object> paramMap = new HashMap<String, Object>();
                                paramMap.put("tableInstId", tableInstId);                      //映射表id
                                paramMap.put("partition", partitionMap.get("PARTITION_NAME")); //分区
                                paramMap.put("subpartition", "");                             //子分区
                                paramMap.put("rowRecords", partitionMap.get("NUM_ROWS"));    //记录数
                                if (grabDataDAO.isInTableData(paramMap, 1)) {    //如果存在该数据，修改操作
                                    updateDataOneList.add(paramMap);
                                    updateDataOneCount++;        //记录表修改统计数增加1（条件无子分区）
                                    tabPartitionList.remove(m);
                                    break;
                                } else {     //不存在数据，新增操作
                                    insertDataList.add(paramMap);
                                    insertDataCount++;        //记录表新增统计数增加1
                                    tabPartitionList.remove(m);
                                    break;
                                }
                            }

                        }
                        for (int m = 0; m < tabSubpartitionList.size(); m++) {
                            Map<String, Object> subpartitionMap = tabSubpartitionList.get(m);
                            String name = subpartitionMap.get("TABLE_NAME").toString().toUpperCase();    //子分区数据中的tableName
                            String owner = subpartitionMap.get("TABLE_OWNER").toString().toUpperCase();    //子分区数据中的tableOwner
                            if (tableName.equals(name) && tableOwner.equals(owner)) {
                                //找到匹配数据后，判断META_TABLE_INST_DATA中是否存在数据，存在则修改，不存在则新增
                                Map<String, Object> paramMap = new HashMap<String, Object>();
                                paramMap.put("tableInstId", tableInstId);                      //映射表id
                                paramMap.put("partition", subpartitionMap.get("PARTITION_NAME")); //分区
                                paramMap.put("subpartition", subpartitionMap.get("SUBPARTITION_NAME")); //子分区
                                paramMap.put("rowRecords", subpartitionMap.get("NUM_ROWS"));    //记录数
                                if (grabDataDAO.isInTableData(paramMap, 2)) {    //如果存在该数据，修改操作
                                    updateDataTwoList.add(paramMap);
                                    updateDataTwoCount++;        //记录表修改统计数增加1（条件包含子分区）
                                    tabSubpartitionList.remove(m);
                                    break;
                                } else {     //不存在数据，新增操作
                                    insertDataList.add(paramMap);
                                    insertDataCount++;        //记录表新增统计数增加1
                                    tabSubpartitionList.remove(m);
                                    break;
                                }
                            }

                        }
                        if (instUpdateCount == maxNum) {
                            //当映射表修改统计数和目标数一致时，执行修改操作
                            grabDataDAO.updateTableInst(instUpdateList); //批量修改映射表中记录数
                            instUpdateCount = 0;     //记录数初始化
                            instUpdateList.clear();     //修改数据清空
                        }
                        if (updateDataOneCount == maxNum) {
                            //当记录表修改统计数（条件无子分区）和目标数一致时，执行修改操作
                            grabDataDAO.updateTableInstData(updateDataOneList, 1);
                            updateDataOneCount = 0;          //记录数初始化
                            updateDataOneList.clear();     //修改数据清空（条件无子分区）
                        }
                        if (updateDataTwoCount == maxNum) {
                            //当记录表修改统计数（条件包含子分区）和目标数一致时，执行修改操作
                            grabDataDAO.updateTableInstData(updateDataTwoList, 2);
                            updateDataTwoCount = 0;         //记录数初始化
                            updateDataTwoList.clear();    //修改数据清空（条件包含子分区）
                        }
                        if (insertDataCount == maxNum) {
                            //当记录表新增统计数和目标数一致时，执行新增操作
                            grabDataDAO.insterTableInstData(insertDataList);
                            insertDataCount = 0;     //记录数初始化
                            insertDataList.clear();     //新增数据清空
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
                Log.error("定时程序报错", e);
            }
        }
        if (instUpdateList.size() > 0) {
            //如果修改映射表信息中存在数据，则修改
            grabDataDAO.updateTableInst(instUpdateList); //批量修改映射表中记录数
            instUpdateList.clear();     //修改数据清空
        }
        if (updateDataOneList.size() > 0) {
            //如果修改记录表信息中存在数据，执行修改操作
            grabDataDAO.updateTableInstData(updateDataOneList, 1);
            updateDataOneList.clear();     //修改数据清空（条件无子分区）
        }
        if (updateDataTwoList.size() > 0) {
            //如果修改记录表信息中存在数据，执行修改操作
            grabDataDAO.updateTableInstData(updateDataTwoList, 2);
            updateDataTwoList.clear();     //修改数据清空（条件包含子分区）
        }
        if (insertDataList.size() > 0) {
            //如果新增记录表信息中存在数据，执行新增操作
            grabDataDAO.insterTableInstData(insertDataList);
            insertDataList.clear();     //新增数据清空
        }

    }

    /**
     * 得到需要查询数据的所有所属用户
     *
     * @return
     */
    private String getOwnerString() {
        String[] ownerList = grabDataDAO.getOwnerList();
        String owner = "";
        for (int i = 0; i < ownerList.length; i++) {
            owner += "'" + ownerList[i] + "',";
        }
        if (!owner.equals("")) {
            owner = owner.substring(0, owner.length() - 1);
        }
        return owner;
    }

    /**
     * 抓取预警信息
     */
    private void grabWarning() {
        int insertCount = 0;    //新增统计数
        int maxCount = 200;    //到达目标数时执行操作
        //得到所有有效的数据源信息
        List<Map<String, Object>> dataSourceList = metaDataSourceDAO.
                queryDataSourceByType(TblConstant.META_DATA_SOURCE_TABLE, TblConstant.DATA_SOURCE_VALID);
        //记录信息表下的新增数据信息
        List<Map<String, Object>> insertTableDiffList = new ArrayList<Map<String, Object>>();
        //循环数据源，根据不同数据源抓取数据
        for (int i = 0; i < dataSourceList.size(); i++) {
            Map<String, Object> dataSource = dataSourceList.get(i);
            try {
                //数据源id
                int dataSourceId = Integer.parseInt(dataSource.get("DATA_SOURCE_ID").toString());
                //通过数据源id得到对应的实体表关联信息
                List<Map<String, Object>> tablesList = grabDataDAO.getTablesList(dataSourceId);
                for (int j = 0; j < tablesList.size(); j++) {
                    Map<String, Object> map = tablesList.get(j);
                    boolean check = isDiffCheck(map);
                    if (check) {    //如果存在差异，插入预警信息
                        insertTableDiffList.add(map);
                        insertCount++;
                    }
                    if (insertCount == maxCount) {
                        //新增统计数和目标数一致时，执行新增操作
                        grabDataDAO.insterTableDiff(insertTableDiffList);
                        insertCount = 0;                 //记录数初始化
                        insertTableDiffList.clear();    //数据清空
                    }

                }
            } catch (Exception e) {
                System.out.println(e);
                Log.error("定时程序报错", e);
            }

        }
        if (insertTableDiffList.size() > 0) {
            //如果新增信息中存在数据，执行新增操作
            grabDataDAO.insterTableDiff(insertTableDiffList);
            insertTableDiffList.clear();     //数据清空
        }
    }


    /**
     * 判断是否存在差异
     *
     * @param data data包含键值：dataSourceId  数据源id ，tableId 表类id ，
     *             tableVersion 表类版本 ，tableOwner 实体表拥有者 ，tableName 实体表名
     * @return 返回true为存在差异，返回false为不存在差异
     */
    private boolean isDiffCheck(Map<String, Object> data) {
        String dataSourceId = data.get("DATA_SOURCE_ID").toString();
        int tableId = Integer.parseInt(data.get("TABLE_ID").toString());
        int tableVersion = Integer.parseInt(data.get("TABLE_VERSION").toString());
        String tableOwner = data.get("TABLE_OWNER").toString();
        String tableName = data.get("TABLE_NAME").toString();
        //获取表类当前版本所有列信息。
        List<Map<String, Object>> tableColumnsConfig = metaTableColsDAO.queryMetaTableColsByTableId(tableId, tableVersion, null);
        List<Map<String, Object>> instTableColumns = null;
        try {
            //根据实例表信息到对应数据源查询实例表列信息。
            instTableColumns = tableApplyDAO.queryDbTableColumns(dataSourceId + "", tableOwner, tableName);
        } catch (Exception e) {
            System.out.println(e);
            Log.error("定时程序报错", e);
            return false;
        }
        if (instTableColumns != null && instTableColumns.size() > 0) {
            return DiffAnysis.isDiffCompare(tableColumnsConfig, instTableColumns);
        } else {
            return false;        //如果不存在实体表，则不比对写入预警
        }
    }
}
