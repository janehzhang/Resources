package tydic.meta.module.tbl;

import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.MetaBaseDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 作用:查询表分区信息
 * @date 2012-05-23
 */
public class TblPartitonDAO extends MetaBaseDAO {
    /**
     * 查询指定表名的表分区详细信息，包括查询表分区类型，表分区数目，表子分区数目等
     *
     * @param tableName
     * @param tableOwner
     * @param dataSourceId
     * @return
     */
    public Map<String, Object> queryPartTables(String tableName, String tableOwner, int dataSourceId) {
        String sql = "SELECT T.PARTITIONING_TYPE,T.PARTITION_COUNT,T.SUBPARTITIONING_TYPE,T.DEF_SUBPARTITION_COUNT, T.PARTITIONING_KEY_COUNT," +
                " T.SUBPARTITIONING_KEY_COUNT " +
                "  FROM ALL_PART_TABLES T " +
                " WHERE TABLE_NAME = ? AND OWNER=? AND STATUS = 'VALID'";
        return getDataAccess(dataSourceId).queryForMap(sql, tableName, tableOwner);
    }

    /**
     * 查询分区列信息，主要查询分区的名称
     *
     * @param tableName
     * @param tableOwner
     * @param dataSourceId
     * @return
     */
    public List<Map<String, Object>> queryPartColumns(String tableName, String tableOwner, int dataSourceId) {
        String sql = "SELECT T.COLUMN_NAME FROM ALL_PART_KEY_COLUMNS T " +
                "WHERE NAME=? AND OWNER=? AND T.OBJECT_TYPE='TABLE' ORDER BY T.COLUMN_POSITION ";
        return getDataAccess(dataSourceId).queryForList(sql,tableName, tableOwner);
    }

    /**
     * 查询子分区列信息，查询子分区列名称
     *
     * @param tableName
     * @param tableOwner
     * @param dataSourceId
     * @return
     */
    public List<Map<String, Object>> querySubPartColumns(String tableName, String tableOwner, int dataSourceId) {
        String sql = "SELECT T.COLUMN_NAME FROM ALL_SUBPART_KEY_COLUMNS T " +
                "WHERE T.NAME=? AND T.OWNER=? AND T.OBJECT_TYPE='TABLE' ORDER BY T.COLUMN_POSITION";
        return getDataAccess(dataSourceId).queryForList(sql, tableName, tableOwner);
    }

    /**
     * 查询分区详细信息，主要查询分区子数目，分区名称，分区值，分区表空间
     *
     * @param tableName
     * @param tableOwner
     * @param dataSourceId
     * @return
     */
    public List<Map<String, Object>> queryPartDeatil(String tableName, String tableOwner, int dataSourceId) {
        String sql = "SELECT T.PARTITION_NAME,T.SUBPARTITION_COUNT,T.HIGH_VALUE,T.TABLESPACE_NAME " +
                " FROM ALL_TAB_PARTITIONS T WHERE TABLE_NAME=? AND T.TABLE_OWNER=? ORDER BY T.PARTITION_POSITION";
        return getDataAccess(dataSourceId).queryForList(sql, tableName, tableOwner);
    }

    /**
     * 查询子分区详细信息，主要查询分区子分区名称，分区名称，分区值，分区表空间
     *
     * @param tableName
     * @param tableOwner
     * @param dataSourceId
     * @return
     */
    public List<Map<String, Object>> querySubPartDeatil(String tableName, String tableOwner, int dataSourceId) {
        String sql = "SELECT T.PARTITION_NAME,T.HIGH_VALUE,T.TABLESPACE_NAME,T.SUBPARTITION_NAME " +
                "FROM ALL_TAB_SUBPARTITIONS T WHERE TABLE_NAME=? AND T.TABLE_OWNER=? ORDER BY T.SUBPARTITION_POSITION";
        return getDataAccess(dataSourceId).queryForList(sql, tableName, tableOwner);
    }

    /**
     * 生成一个表的完成分区SQL，例
     * PARTITION BY RANGE (CUSTOMER_ID,FIRST_NAME)
     * (
     * PARTITION CUS_PART1 VALUES LESS THAN (100000,2001) TABLESPACE USERS,
     * PARTITION CUS_PART2 VALUES LESS THAN (200000,222222) TABLESPACE USERS
     * )
     *
     * @param tableName
     * @param tableOwner
     * @param dataSourceId
     * @return
     */
    public String buildPartionSql(String tableName, String tableOwner, int dataSourceId) {
        StringBuffer partionSql = new StringBuffer("PARTITION BY ");
        //查询表分区信息
        Map<String, Object> partInfo = queryPartTables(tableName, tableOwner, dataSourceId);
        if (partInfo != null) {
            String partionType = MapUtils.getString(partInfo, "PARTITIONING_TYPE");
            partionSql.append(partionType);
            partionSql.append("(");
            //查询分区列信息。
            List<Map<String, Object>> partColumns = queryPartColumns(tableName, tableOwner, dataSourceId);
            int count = 0;
            for (Map partColumn : partColumns) {
                partionSql.append(MapUtils.getString(partColumn, "COLUMN_NAME") + (count++ == partColumns.size() - 1 ? "" : ","));
            }
            partionSql.append(")\n");
            //如果有子分区，查询子分区信息
            int subpartitioningKeyCount = MapUtils.getIntValue(partInfo, "SUBPARTITIONING_KEY_COUNT", 0);
            List<Map<String, Object>> subPartitions = null;
            String subPartType = null;
            if (subpartitioningKeyCount > 0) {
                subPartitions = querySubPartDeatil(tableName, tableOwner, dataSourceId);
                //子分区类型
                subPartType = MapUtils.getString(partInfo, "SUBPARTITIONING_TYPE");
                partionSql.append("SUBPARTITION BY " + subPartType);
                partionSql.append("(");
                //子分区列信息
                List<Map<String, Object>> subPartColumns = querySubPartColumns(tableName, tableOwner, dataSourceId);
                count = 0;
                for (Map subPartColumn : subPartColumns) {
                    partionSql.append(MapUtils.getString(subPartColumn, "COLUMN_NAME") + (count++ == subPartColumns.size() - 1 ? "" : ","));
                }
                partionSql.append(")\n");
            }
            //生成子分区SQL ,以分区名称作为主键
            Map<String, List<String>> subPartitionSql = new HashMap<String, List<String>>();
            if (subPartitions != null && subPartitions.size() > 0) {
                for (Map<String, Object> subPartition : subPartitions) {
                    List partitionSqls = null;
                    String partName = MapUtils.getString(subPartition, "PARTITION_NAME");
                    if (subPartitionSql.containsKey(partName)) {
                        partitionSqls = subPartitionSql.get(partName);
                    } else {
                        partitionSqls = new ArrayList();
                    }
                    partitionSqls.add(buildSubPartitionFragment(subPartType, subPartition));
                    subPartitionSql.put(partName, partitionSqls);
                }
            }
            partionSql.append("(");
            //查询分区详细信息
            List<Map<String, Object>> allDetialPart = queryPartDeatil(tableName, tableOwner, dataSourceId);
            count = 0;
            for (Map<String, Object> detialPart : allDetialPart) {
                partionSql.append(buildPartitionFragment(partionType, detialPart));
                String partName = MapUtils.getString(detialPart, "PARTITION_NAME");
                if (subPartitionSql.containsKey(partName)) {
                    List<String> subSqls = subPartitionSql.get(partName);
                    partionSql.append("(");
                    for (int i = 0; i < subSqls.size(); i++) {
                        partionSql.append(subSqls.get(i));
                        if (i != subSqls.size() - 1) {
                            partionSql.append(",");
                        }
                    }
                    partionSql.append(")");
                }
                if (count++ != allDetialPart.size() - 1) {
                    partionSql.append(",");
                }
            }
            partionSql.append(")");
            return partionSql.toString();
        } else {
            return null;
        }
    }

    /**
     * 根据分区详细信息构造一个分区SQL片段
     * 例：PARTITION CUS_PART1 VALUES LESS THAN (100000,2001) TABLESPACE USERS
     *
     * @param partType       分区类型 ,RANGE,HASH,LIST
     * @param detailParition 详细分区信息，表ALL_TAB_PARTITIONS中的字段
     * @return
     */
    private String buildPartitionFragment(String partType, Map<String, Object> detailParition) {
        String sql = "PARTITION ";
        String partName = MapUtils.getString(detailParition, "PARTITION_NAME");
        sql += partName + " ";
        String highValue = MapUtils.getString(detailParition, "HIGH_VALUE");
        if (partType.equalsIgnoreCase("RANGE")) {
            //范围分区
            sql += "VALUES LESS THAN (" + highValue + ") ";
        } else if (partType.equalsIgnoreCase("LIST")) {
            //列表分区
            sql += "VALUES (" + highValue + ") ";
        } else {
            //Hash分区
            sql += "";
        }
        String tablespaceName = MapUtils.getString(detailParition, "TABLESPACE_NAME");
        //如果表空间为不为空，加入表空间信息
        if (StringUtils.isNotEmpty(tablespaceName)) {
            sql += " TABLESPACE " + tablespaceName;
        }
        return sql;
    }

    /**
     * 根据子分区详细信息构造一个分区SQL片段
     *
     * @param subPartType
     * @param detailParition
     * @return
     */
    private String buildSubPartitionFragment(String subPartType, Map<String, Object> detailParition) {
        String sql = "SUBPARTITION ";
        String partName = MapUtils.getString(detailParition, "SUBPARTITION_NAME");
        sql += partName + " ";
        String highValue = MapUtils.getString(detailParition, "HIGH_VALUE");
        if (subPartType.equalsIgnoreCase("RANGE")) {
            //范围分区
            sql += "VALUES LESS THAN (" + highValue + ") ";
        } else if (subPartType.equalsIgnoreCase("LIST")) {
            //列表分区
            sql += "VALUES (" + highValue + ") ";
        } else {
            //Hash分区
            sql += "";
        }
        String tablespaceName = MapUtils.getString(detailParition, "TABLESPACE_NAME");
        //如果表空间为不为空，加入表空间信息
        if (StringUtils.isNotEmpty(tablespaceName)) {
            sql += " TABLESPACE " + tablespaceName;
        }
        return sql;
    }

}
