package tydic.meta.module.tbl.apply;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.Log;
import tydic.meta.common.Common;
import tydic.meta.common.Page;
import tydic.meta.module.tbl.TblPartitonDAO;
import tydic.meta.module.tbl.*;
import tydic.meta.web.session.SessionManager;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 表类申请Action <br>
 * @date 2011-10-26
 * @modify 谭红涛 修改了queryDbTables方法中从queryMessage方法中获取参数部分,郭行风20120116添加queryTableSpace方法
 * @date 2011-11-11
 */
public class TableApplyAction {

    private MetaSysCodeDAO metaSysCodeDAO;

    private MetaTableGroupDAO metaTableGroupDAO;

    private MetaDataSourceDAO metaDataSourceDAO;

    private MetaTablesDAO metaTablesDAO;

    private MetaDimTypeDAO metaDimTypeDAO;

    private MetaDimLevelDAO metaDimLevelDAO;

    private MetaTableColsDAO metaTableColsDAO;

    private TableApplyDAO tableApplyDAO;

    private MetaMagUserTabRelDAO metaMagUserTabRelDAO;

    private MetaTableRelDAO metaTableRelDAO;

    private TblPartitonDAO tblPartitonDAO;

    /**
     * 查询所有的表空间
     */
    public List<Map<String, Object>> queryTableSpaceByDataSourceId(int dataSourceId) {
        return metaTablesDAO.getTableSpaceByDataSourceId(dataSourceId);
    }

    /**
     * 查询所有的表类型
     */
    public List<Map<String, Object>> queryTableType() {
        return metaSysCodeDAO.querySysCodeByCodeType(TblConstant.META_SYS_CODE_TABLE_TYPE);
    }

    /**
     * 查询所有的列类型
     *
     * @return
     */
    public List<Map<String, Object>> queryColumnDataType() {
        return metaSysCodeDAO.querySysCodeByCodeType(TblConstant.META_SYS_CODE_DATA_TYPE);

    }

    /**
     * 根据表typeId查找所有的表分类。
     *
     * @param tableType
     * @return
     */
    public List<Map<String, Object>> queryTableGroup(int tableType) {
        return metaTableGroupDAO.queryTableGroup(tableType);
    }

    /**
     * 根据条件查询维度表信息。
     *
     * @param queryData 内包含键值tableGroupId,tableTypeId,queryMessage
     * @return
     */
    public List<Map<String, Object>> queryDimInfo(Map<String, Object> queryData, Page page) {
        if (page == null) {
            page = new Page(0, 10);
        }
        return metaTablesDAO.queryDimTableInfo(queryData, page);
    }

    /**
     * 根据表类ID和表类版本号查询有效的维度归并类型。
     *
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryDimType(int tableId) {
        return metaDimTypeDAO.queryDimType(tableId);
    }

    /**
     * 查询维表层级数据
     *
     * @param tableId
     * @param dimTypeId
     * @return
     */
    public List<Map<String, Object>> queryDimLevels(int tableId, int dimTypeId) {
        return metaDimLevelDAO.queryDimLevel(tableId, dimTypeId);
    }

    /**
     * 查询维度表的列信息（只查后缀为_CODE,_ID的列，用于做下拉框选择。）
     *
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryDimCols(int tableId, int tableVersion) {
        return metaTableColsDAO.queryIdNameByTableId(tableId, tableVersion);
    }

    /**
     * 输入四个字母及以上下拉显示已有的相似字段 ,从META_TABLE_COLS中读取列信息自动填写类型字段，
     * 匹配方式：前缀相同
     *
     * @param proto 要匹配的字符串
     * @return
     */
    public List<Map<String, Object>> matchCols(String proto) {
        return metaTableColsDAO.matchCols(proto);
    }

    /**
     * 输入表名查询数据库中存在的相似表名，
     * 匹配方式：前缀相同
     *
     * @param proto 要匹配的字符串
     * @return
     */
    public List<Map<String, Object>> matchTables(String proto) {
        return metaTablesDAO.queryMatchTables(proto);
    }

    /**
     * 查询表类的数据源
     *
     * @return
     */
    public List<Map<String, Object>> queryTableDataSource() {
        return metaDataSourceDAO.queryDataSourceByType(TblConstant.META_DATA_SOURCE_TABLE, TblConstant.COL_STATE_VALID);
    }

    /**
     * 查询数据库分区SQL
     *
     * @param tableName
     * @param tableOwner
     * @param dataSourceId
     * @return
     */
    public String queryPartitionSql(String tableName, String tableOwner, int dataSourceId) {
        return tblPartitonDAO.buildPartionSql(tableName, tableOwner, dataSourceId);
    }

    public List<Map<String, Object>> queryTableDataSourceByCurId() {
        List<Map<String, Object>> rtn = new ArrayList<Map<String, Object>>();
        rtn.add(metaDataSourceDAO.queryDIMDataSource());
        return rtn;
    }

    /**
     * 根据条件查询业务表信息
     *
     * @param queryMessage 内包括键值 tableTypeId 表层次分类。tableGroupId：表业务类型。
     *                     keyWord：关键字
     * @return
     */
    public List<Map<String, Object>> queryBusinessTables(Map<String, Object> queryMessage, Page page) {
        if (page == null) {
            page = new Page(0, 7);
        }
        return metaTablesDAO.queryMetaTables(queryMessage, page);
    }

    /**
     * 根据表信息查询分页
     *
     * @param tableId
     * @return
     */
    public List<Map<String, Object>> queryColumnsByTableID(int tableId, int tableVersion) {
        return metaTableColsDAO.queryMetaTableColsByTableId(tableId, tableVersion, null);
    }

    /**
     * 根据配置生成一个预览SQL。
     *
     * @param data 数据结构定义如下:例：{
     *             tableData:{dataSourceId: "9",partitionSql: "ew",tableBusComment: "ew", tableGroup: "测试"
     *             tableGroupId: "1",tableName: "as",tableNameCn: "当山峰",tableSpace: "",tableState: 2,
     *             tableTypeId: "1",tableVersion: 0}，
     *             columnDatascolumnDatas：[
     *             {
     *             colBusComment: "",colBusType: "1",colDatatype: "NUMBER",	colName: "ass",
     *             colNullabled: 0	,colOrder: 1,	colPrec: "2"	,colSize: "3"	,
     *             defaultVal: ""	,dimColId: ""	,	dimLevel: "",	dimTableId: "",
     *             dimTypeId: "",	fullDataType: "NUMBER(3,2)"	,isPrimary: 0,	tableState: 2	,tableVersion: 0
     *             }
     *             ]
     *             }
     * @return 返回一个建表SQL语句。
     */
    public String generateSql(Map<String, Object> data) {
        //获取TABlL NAME
        StringBuffer createSql = new StringBuffer("CREATE TABLE ");
        Map<String, Object> tableData = (Map<String, Object>) data.get("tableData");
        String tableOwner = Convert.toString(tableData.get("tableOwner"));
        createSql.append(tableOwner + ".");
        createSql.append(tableData.get("tableName").toString().toUpperCase());
        createSql.append(" ( <br/>");
        List<Map<String, Object>> columnDatas = (List<Map<String, Object>>) data.get("columnDatas");
        //读取列配置动态生成SQL
        int count = 0;
        String columnComment = "";//列注释。
        List<String> primaryKeys = new ArrayList<String>();//生成主键字段。
        if (columnDatas != null && columnDatas.size() > 0) {
            for (Map<String, Object> column : columnDatas) {
                boolean isNumber = TblCommon.isNumber(String.valueOf(column.get("colDatatype")));
                createSql.append(column.get("colName").toString().toUpperCase() + " ");
                //添加数据类型
                int colSize = column.get("colSize") == null || column.get("colSize").equals("") ? 0 : Integer.parseInt(column.get("colSize").toString());
                int colPrec = column.get("colPrec") == null || column.get("colPrec").equals("") ? 0 : Integer.parseInt(column.get("colPrec").toString());
                createSql.append(TblCommon.buildDataType(String.valueOf(column.get("colDatatype")).toUpperCase(),
                        colSize, colPrec) + " ");
                //添加默认值。
                if (column.get("defaultVal") != null &&
                        !column.get("defaultVal").toString().equals("")) {
                    createSql.append("DEFAULT " + (isNumber || column.get("defaultVal").toString().equalsIgnoreCase("SYSDATE") ? "" : "'")
                            + column.get("defaultVal") + (isNumber || column.get("defaultVal").toString().equalsIgnoreCase("SYSDATE") ? " " : "' "));
                }
                //添加是否为空处理。
                int colNullabled = column.get("colNullabled") == null ? null : Integer.parseInt(column.get("colNullabled").toString());
                if (colNullabled == 0) {
                    createSql.append("NOT NULL ");
                }
                //是否为主键
                int isPrimary = column.get("isPrimary") == null ? null : Integer.parseInt(column.get("isPrimary").toString());
                if (isPrimary == 1) {
                    primaryKeys.add(column.get("colName").toString());
//                    createSql.append("PRIMARY KEY ");
                }
                //加入分隔逗号
                if (count++ != columnDatas.size() - 1) {
                    createSql.append(",");
                }
                createSql.append("<br/>");
                //列注释
                if (column.get("colBusComment") != null &&
                        !column.get("colBusComment").toString().equals("")) {
                    columnComment += "COMMENT ON COLUMN " + tableOwner + "." + tableData.get("tableName").toString().toUpperCase() + "."
                            + column.get("colName").toString().toUpperCase() + " IS '" + column.get("colBusComment")
                            + "';<br/>";
                }
            }
        }
        createSql.append(")");
        //分区SQL
        if (tableData.get("partitionSql") != null
                && !tableData.get("partitionSql").toString().equals("")) {
            createSql.append("<br/>" + tableData.get("partitionSql").toString().toUpperCase());
        }
        // 表空间
        if (tableData.get("tableSpace") != null) {
            createSql.append("<br/>TABLESPACE " + tableData.get("tableSpace"));
        }
        createSql.append(";<br/>");

        //建表SQL完成。
        //添加表注释
        if (tableData.get("tableBusComment") != null
                && !tableData.get("tableBusComment").toString().equals("")) {
            createSql.append("COMMENT ON TABLE " + tableOwner + "." + tableData.get("tableName").toString().toUpperCase() + " IS '"
                    + tableData.get("tableBusComment") + "';<br/>");
        }
        //行级注释
        createSql.append(columnComment);
        //主键SQL
        if (primaryKeys.size() > 0) {
            createSql.append("ALTER TABLE " + tableOwner + "." + tableData.get("tableName").toString().toUpperCase() + "  ADD PRIMARY KEY ("
                    + Common.join(primaryKeys.toArray(new String[primaryKeys.size()]), ",").toUpperCase() + ");<br/>");
        }

        //返回最终生成的SQL
        return TblCommon.sqlMarcoReplace(createSql.toString());
    }

    /**
     * 表类配置完成提交验证
     *
     * @param data 数据结构说明见 generateSql
     * @return 返回验证的信息，比如验证项，检查值，说明，检查结果
     */
    public List<Map<String, Object>> validate(Map<String, Object> data) {
        //主键数量验证。
        List<Map<String, Object>> columnDatas = (List<Map<String, Object>>) data.get("columnDatas");
        int keyCount = 0;
        if (columnDatas != null && columnDatas.size() > 0) {
            for (Map<String, Object> column : columnDatas) {
                int isPrimary = column.get("isPrimary") == null ? null : Integer.parseInt(column.get("isPrimary").toString());
                if (isPrimary == 1) {
                    keyCount++;
                }
            }
        }
        List<Map<String, Object>> validateResult = new ArrayList<Map<String, Object>>();
        Map<String, Object> validateTemp = new HashMap<String, Object>();
        validateTemp.put("checkItem", "主键验证");
        validateTemp.put("checkValue", keyCount);
        validateTemp.put("desc", "主键个数应大于一个且小于三个。");
        validateTemp.put("res", keyCount > 0 && keyCount <= 3);
        validateResult.add(validateTemp);
        //对生成建表语句，验证建表SQL
        Map<String, Object> tableData = (Map<String, Object>) data.get("tableData");
//        String tableName= tableData.get("tableName").toString();
        //修改tableName，便于测试
        String tableName = "TEMP_" + System.currentTimeMillis();
        String tableOwner = Convert.toString(tableData.get("tableOwner"));
        tableData.put("tableName", tableName);
        //查询数据源
//        Map<String,Object> dataSource=metaDataSourceDAO.queryDataSourceById(
//                Integer.parseInt(tableData.get("dataSourceId").toString()));
        //测试执行
        validateTemp = new HashMap<String, Object>();
        validateTemp.put("checkItem", "建表SQL验证");
        validateTemp.put("checkValue", "验证成功！");
        validateTemp.put("desc", "实时验证生成的建表SQL语句");
        validateTemp.put("res", true);
        try {
            List<String> sqls = new ArrayList<String>();
            StringBuffer createSql = new StringBuffer("CREATE TABLE ");
            createSql.append(tableOwner + ".");
            createSql.append(tableData.get("tableName"));
            createSql.append(" ( ");
            //读取列配置动态生成SQL
            int count = 0;
            List<String> primaryKeys = new ArrayList<String>();//生成主键字段。
            if (columnDatas != null && columnDatas.size() > 0) {
                for (Map<String, Object> column : columnDatas) {
                    boolean isNumber = TblCommon.isNumber(column.get("colDatatype").toString());
                    createSql.append(column.get("colName") + " ");
                    //添加数据类型
                    createSql.append(column.get("fullDataType") + " ");
                    //添加默认值。
                    if (column.get("defaultVal") != null &&
                            !column.get("defaultVal").toString().equals("")) {
                        createSql.append("DEFAULT " + (isNumber || column.get("defaultVal").toString().equalsIgnoreCase("SYSDATE") ? "" : "'")
                                + column.get("defaultVal") + (isNumber || column.get("defaultVal").toString().equalsIgnoreCase("SYSDATE") ? " " : "' "));
                    }

                    //添加是否为空处理。
                    int colNullabled = column.get("colNullabled") == null ? null : Integer.parseInt(column.get("colNullabled").toString());
                    if (colNullabled == 0) {
                        createSql.append("NOT NULL ");
                    }
                    //是否为主键
                    int isPrimary = column.get("isPrimary") == null ? null : Integer.parseInt(column.get("isPrimary").toString());
                    if (isPrimary == 1) {
                        primaryKeys.add(column.get("colName").toString());
//                        createSql.append("PRIMARY KEY ");
                    }

                    //加入分隔逗号
                    if (count++ != columnDatas.size() - 1) {
                        createSql.append(",");
                    }
                    createSql.append("");
                    //列注释
                    if (column.get("colBusComment") != null &&
                            !column.get("colBusComment").toString().equals("")) {
                        sqls.add("COMMENT ON COLUMN " + tableOwner + "." + tableData.get("tableName") + "."
                                + column.get("colName") + " IS '" + column.get("colBusComment")
                                + "' ");
                    }
                }
            }
            createSql.append(") ");
            //分区SQL
            if (tableData.get("partitionSql") != null
                    && !tableData.get("partitionSql").toString().equals("")) {
                String parSql = Convert.toString(tableData.get("partitionSql")).replaceAll("\n", " ");
                createSql.append(parSql);
            }
            //添加表注释
            if (tableData.get("tableBusComment") != null
                    && !tableData.get("tableBusComment").toString().equals("")) {
                sqls.add(0, "COMMENT ON TABLE " + tableOwner + "." + tableData.get("tableName") + " IS '"
                        + tableData.get("tableBusComment") + "' ");
            }
            sqls.add(0, createSql.toString());
            //主键SQL
            if (primaryKeys.size() > 0) {
                sqls.add("ALTER TABLE " + tableOwner + "." + tableData.get("tableName") + "  ADD PRIMARY KEY ("
                        + Common.join(primaryKeys.toArray(new String[primaryKeys.size()]), ",") + ")");
            }
            tableApplyDAO.testCreatetSql(sqls, tableData.get("dataSourceId").toString(), tableName, tableOwner);

        } catch (Exception e) {
            if (e instanceof SQLException) {
                SQLException sqlException = (SQLException) e;
                validateTemp.put("checkValue", "SQLSTATE:" + sqlException.getSQLState() + ";"
                        + "ERRORCODE:" + sqlException.getErrorCode() + ";MESSAGE:" + sqlException.getMessage());
            } else {
                validateTemp.put("checkValue", e.getMessage());
            }
            validateTemp.put("res", false);
        }
        validateResult.add(validateTemp);
        return validateResult;
    }

    /**
     * 保存一个新增的表类。
     *
     * @param data 数据结构说明见 generateSql
     * @return
     */
    public boolean insertTable(Map<String, Object> data) {
        Map<String, Object> tableData = (Map<String, Object>) data.get("tableData");
        List<Map<String, Object>> columnDatas = (List<Map<String, Object>>) data.get("columnDatas");
        try {
            BaseDAO.beginTransaction();
            long tableId = metaTablesDAO.insertMetaTables(tableData);
            if (columnDatas != null && columnDatas.size() > 0) {
                for (Map<String, Object> column : columnDatas) {
                    column.put("tableId", tableId);
                    if (column.get("colId") != null && !column.get("colId").equals("")) {
                    } else {
                        column.put("colId", metaTableColsDAO.queryForNextVal("SEQ_TAB_COL_ID"));
                    }
                }
            }
            metaTableColsDAO.insertBatch(columnDatas);
            //新增描述用户与表的关系META_MAG_USER_TAB_REL
            Map<String, Object> userTabRel = new HashMap<String, Object>();
            userTabRel.put("userId", SessionManager.getCurrentUserID());
            userTabRel.put("tableId", tableId);
            userTabRel.put("relType", TblConstant.USER_TAB_REL_TYPE_APPLY);
            userTabRel.put("tableState", tableData.get("tableState"));
            userTabRel.put("tableVersion", tableData.get("tableVersion"));
            userTabRel.put("tableName", tableData.get("tableName"));
            metaMagUserTabRelDAO.insertMetaMagUserTable(userTabRel);

            //根据新增表的ID查询表类字段信息
            for (int i = 0; i < columnDatas.size(); i++) {
                //根据ID查询其关联的维度列
                int dimTableId = Common.parseInt(columnDatas.get(i).get("dimTableId")) == null ? 0 : Common.parseInt(columnDatas.get(i).get("dimTableId"));
                int dimColId = Common.parseInt(columnDatas.get(i).get("dimColId")) == null ? 0 : Common.parseInt(columnDatas.get(i).get("dimColId"));
                if (dimTableId > 0 && dimColId > 0) {
                    //保存表类关系信息
                    Map<String, Object> insertData = new HashMap<String, Object>();
                    insertData.put("tableId1ColIds", columnDatas.get(i).get("colId"));
                    insertData.put("tableId2ColIds", dimColId);
                    insertData.put("tableRelDesc", "");
                    insertData.put("tableRelType", 1);
                    insertData.put("tableId1", tableId);
                    insertData.put("tableId2", Common.parseInt(columnDatas.get(i).get("dimTableId")));
                    if (!metaTableRelDAO.checkExist(insertData)) {
                        metaTableRelDAO.insertMetaTableRel(insertData);
                    }
                }
            }
            BaseDAO.commit();
        } catch (Exception e) {
            BaseDAO.rollback();
            Log.error(null, e);
            return false;
        }
        return true;
    }

    /**
     * 判断指定表名在数据库中是否已经存在
     *
     * @param
     * @return
     */
    public boolean isExistsMatchTables(String tableName, int dataSource, String tableOwner) {
        return metaTablesDAO.isExistsMatchTables(tableName, dataSource, tableOwner);
    }

    public List<Map<String, Object>> getUserNameByDataSourceId(int dataSourceId) {
        Map<String, Object> userInfo = metaTablesDAO.getLoginUserNameByDataSourceId(dataSourceId);
        List<Map<String, Object>> maps = metaTablesDAO.getUserNameListByDataSourceId(dataSourceId);
        String defaultTbSpace = metaTablesDAO.getDefaultTablespaceByDataSource(dataSourceId);
        for (int i = 0; i < maps.size(); i++) {
            Map<String, Object> temp = maps.get(i);
            temp.put("DEFAULT_NAME", userInfo.get("DATA_SOURCE_USER").toString().toUpperCase());
            temp.put("DEFAULT_TABLESPACE", defaultTbSpace);
            break;
        }
        return maps;
    }

    /**
     * 根据数据源ID查询该数据源下所有的有效用户。
     *
     * @param dataSourceID
     * @return
     */
    public String[] queryDbUsers(int dataSourceID) {
        //查询数据源

        return tableApplyDAO.queryDbUsers(dataSourceID + "");
    }

    /**
     * 查询数据库表信息
     *
     * @return queryMessage 内包括键值 dataSource 数据源。owner：所属用户。keyWord：关键字
     */
    public List<Map<String, Object>> queryDbTables(Map<String, String> queryMessage, Page page) {
        if (page == null) {
            if (queryMessage.containsKey("pageSize")) {
                page = new Page(0, Integer.parseInt(queryMessage.get("pageSize")));
            } else {
                page = new Page(0, 7);
            }

        }
        String dataSourceID = Convert.toString(queryMessage.get("dataSource"));
        //查询数据源
        return tableApplyDAO.queryDbTables(dataSourceID, queryMessage.get("owner"), queryMessage.get("keyWord").toUpperCase(), page);
    }

    /**
     * 查询数据表某表字段信息。
     *
     * @param dataSourceID
     * @param tableName
     * @param owner
     * @return
     */
    public List<Map<String, Object>> queryDbTableColumns(String tableName, String owner, int dataSourceID) {
        //查询数据源
        //Map<String,Object> dataSource=metaDataSourceDAO.queryDataSourceById(dataSourceID);
        return tableApplyDAO.queryDbTableColumns(dataSourceID + "", owner, tableName);
    }

    public Map<String, Object> queryColNameCnAndColId(String tableName, int dataSourceID, String colName) {
        return tableApplyDAO.queryColNameCnAndColId(tableName, dataSourceID, colName);
    }

    /**
     * 根据数据源取用户信息
     *
     * @param dataSourceId
     * @return
     */
    public String queryDefaultUserByDataSource(int dataSourceId) {
        Map<String, Object> userInfo = metaTablesDAO.getLoginUserNameByDataSourceId(dataSourceId);
        if (dataSourceId == -1) {
            return null;
        }
        return userInfo.get("DATA_SOURCE_USER").toString().toUpperCase();
    }

    /**
     * 我的表类申请中，重新申请新增被驳回的表类，对表类名重复性的检测
     *
     * @param tableName
     * @param owner
     * @param dataSourceID
     * @param orgTableId
     * @return
     */
    public boolean ifExistTableFromMyApply(String tableName, String owner, int dataSourceID, int orgTableId) {
        return metaTablesDAO.ifExistTableFromMyApply(tableName, owner, dataSourceID, orgTableId);
    }

    public void setMetaSysCodeDAO(MetaSysCodeDAO metaSysCodeDAO) {
        this.metaSysCodeDAO = metaSysCodeDAO;
    }

    public void setMetaTableGroupDAO(MetaTableGroupDAO metaTableGroupDAO) {
        this.metaTableGroupDAO = metaTableGroupDAO;
    }

    public void setMetaDataSourceDAO(MetaDataSourceDAO metaDataSourceDAO) {
        this.metaDataSourceDAO = metaDataSourceDAO;
    }

    public void setMetaTablesDAO(MetaTablesDAO metaTablesDAO) {
        this.metaTablesDAO = metaTablesDAO;
    }

    public void setMetaDimTypeDAO(MetaDimTypeDAO metaDimTypeDAO) {
        this.metaDimTypeDAO = metaDimTypeDAO;
    }

    public void setMetaDimLevelDAO(MetaDimLevelDAO metaDimLevelDAO) {
        this.metaDimLevelDAO = metaDimLevelDAO;
    }

    public void setMetaTableColsDAO(MetaTableColsDAO metaTableColsDAO) {
        this.metaTableColsDAO = metaTableColsDAO;
    }

    public TableApplyDAO getTableApplyDAO() {
        return tableApplyDAO;
    }

    public void setTableApplyDAO(TableApplyDAO tableApplyDAO) {
        this.tableApplyDAO = tableApplyDAO;
    }

    public void setMetaMagUserTabRelDAO(MetaMagUserTabRelDAO metaMagUserTabRelDAO) {
        this.metaMagUserTabRelDAO = metaMagUserTabRelDAO;
    }

    public void setMetaTableRelDAO(MetaTableRelDAO metaTableRelDAO) {
        this.metaTableRelDAO = metaTableRelDAO;
    }

    public void setTblPartitonDAO(TblPartitonDAO tblPartitonDAO) {
        this.tblPartitonDAO = tblPartitonDAO;
    }
}
