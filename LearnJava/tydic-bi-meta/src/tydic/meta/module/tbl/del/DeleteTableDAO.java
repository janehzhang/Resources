package tydic.meta.module.tbl.del;

import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.tbl.TblConstant;
import tydic.meta.sys.code.CodeManager;
import tydic.meta.web.session.SessionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.
 *
 * @author 王春生
 * @description 表类维度删除DAO，此类方法会随系统后实现功能量不断添加接口
 * 此类是被保护的，只有被DeleteTableAction调用（其他地方调用时很危险的）
 * @date 12-5-14 截止此时，删除表类涉及到的表有：
 * --表类
 * META_TABLES
 * META_TABLE_INST
 * META_TABLE_COLS
 * META_TABLE_INST_DATA
 * META_TABLE_REL
 * META_TABLE_PRO_INST_REL
 * META_TABLE_DIFF
 * META_TABLE_DIFF_ITEM (目前暂未往其中写数据)
 * --具体实体表（界面可选）
 * <p/>
 * --维度表类 特有
 * META_DIM_TABLES
 * META_DIM_TAB_MOD_HIS
 * META_DIM_MAPP
 * META_DIM_TYPE
 * META_DIM_LEVEL
 * META_DIM_TAB_INT_REL
 * <p/>
 * --业务表类 特有
 * META_RPT_MODEL_ISSUE_CONFIG
 * META_RPT_MODEL_ISSUE_COLS
 * -
 * @modify
 * @modifyDate -
 */
public class DeleteTableDAO extends MetaBaseDAO {

    /**
     * 删除表类 （此类只暴露一个接口给同级的Action）
     *
     * @param tableId
     * @param tableType 表类型
     * @param logStr    日志
     * @return
     */
    protected int deleteTables(int tableId, int tableType, boolean delInstane, StringBuilder logStr) {
        int ret = 0;
        logStr.append("正在删除列信息\n");
        ret += deleteTableCols(tableId);

        logStr.append("正在删除差异信息\n");
        ret += deleteTableDiff(tableId);

        logStr.append("正在删除关系表信息\n");
        ret += deleteTableRels(tableId);

        logStr.append("正在删除关联程序\n");
        ret += deleteTablePros(tableId);
        //维度表
        if (tableType == TblConstant.META_TABLE_TYPE_ID_DIM) {
            logStr.append("正在删除维度表信息\n");
            ret += deleteDimTables(tableId);

            logStr.append("正在删除维度归并信息\n");
            ret += deleteDimTableTypes(tableId);

            logStr.append("正在删除维度接口关系\n");
            ret += deleteDimTableInterface(tableId);

            logStr.append("正在删除维度映射信息\n");
            ret += deleteDimTableMapping(tableId);

            logStr.append("正在删除维度审核信息\n");
            ret += deleteDimTableCodeHis(tableId);
        } else if (tableType == TblConstant.META_TABLE_TYPE_ID_INT) {
            //维度接口表 
            int dimTableId = queryDimTableIdByIntId(tableId);
            if (dimTableId > 0) {
                //删除维度接口表
                ret += deleteDimTableInterface(dimTableId);
                //删除映射
                ret += deleteDimTableMapping(dimTableId);
            }
        } else {
            logStr.append("正在删除业务模型\n");
            ret += deleteBusTableModels(tableId);
        }
        String sql = "SELECT DISTINCT A.DATA_SOURCE_ID,B.TABLE_OWNER,B.TABLE_NAME FROM " +
                "META_TABLES A,META_TABLE_INST B WHERE A.TABLE_ID=B.TABLE_ID AND A.TABLE_ID=?";//实例信息
        List<Map<String, Object>> instans = getDataAccess().queryForList(sql, tableId);

        logStr.append("正在删除实例信息\n");
        ret += deleteTableInsts(tableId);

        logStr.append("正在删除表类基本信息\n");
        sql = "DELETE FROM META_TABLES WHERE TABLE_ID=?"; //删除表类基本信息
        ret += getDataAccess().execUpdate(sql, tableId);

        recordLog(tableId); //记录日志
        if (delInstane) {
            logStr.append("正在删除实体表(无法恢复)\n");
            dropInstanceTable(instans);
        }
        logStr.append("删除完成!\n");
        return ret;
    }

    /**
     * 记录日志
     *
     * @param tableId
     * @return
     */
    private void recordLog(int tableId) {
//        String sql = "insert into table_del_log(log_id,key_id,STATE_DATE,USER_ID) values(del_log.NEXT_VAL,?,TO_DATE(?,'yyyy-MM-dd hh24:mi:ss'),?)";  //目前此表未设计记录日志
//        getDataAccess().execUpdate(sql,tableId, SessionManager.getCurrentUserID());
    }

    /**
     * 删除列
     *
     * @param tableId
     * @return
     */
    private int deleteTableCols(int tableId) {
        String sql = "DELETE FROM META_TABLE_COLS WHERE TABLE_ID=?";
        return getDataAccess().execUpdate(sql, tableId);
    }

    /**
     * 根据接口表查询对应的维度关联关系
     *
     * @param intRealId
     * @return
     */
    private int queryDimTableIdByIntId(int intRealId) {
        String sql = "SELECT DIM_TABLE_ID FROM META_DIM_TAB_INT_REL WHERE INT_TAB_ID=?";
        return getDataAccess().queryForIntByNvl(sql, 0, intRealId);
    }

    /**
     * 删除表类关系
     *
     * @param tableId
     * @return
     */
    private int deleteTableRels(int tableId) {
        String sql = "DELETE FROM META_TABLE_REL WHERE TABLE_ID1=? OR TABLE_ID2=?";
        return getDataAccess().execUpdate(sql, tableId, tableId);
    }

    /**
     * 删除表类实例关系数据
     *
     * @param tableId
     * @return
     */
    private int deleteTableInsts(int tableId) {
        String sql = "DELETE FROM META_TABLE_INST WHERE TABLE_ID=?";
        String insdataSql = "DELETE FROM META_TABLE_INST_DATA A WHERE EXISTS" +
                "(SELECT TABLE_ID FROM META_TABLE_INST WHERE TABLE_INST_ID=A.TABLE_INST_ID AND TABLE_ID=?)";
        int ret = 0;
        ret += getDataAccess().execUpdate(insdataSql, tableId); //先删除实例关系数据
        ret += getDataAccess().execUpdate(sql, tableId);
        return ret;
    }

    /**
     * 删除差异分析数据
     *
     * @param tableId
     * @return
     */
    private int deleteTableDiff(int tableId) {
        String sql = "DELETE FROM META_TABLE_DIFF WHERE TABLE_ID=?";
        String diffItemSql = "DELETE FROM META_TABLE_DIFF_ITEM A WHERE EXISTS" +
                "(SELECT DIFF_ID FROM META_TABLE_DIFF WHERE DIFF_ID=A.DIFF_ID AND TABLE_ID=?)";
        int ret = 0;
        ret += getDataAccess().execUpdate(diffItemSql, tableId);//先删除差异项
        ret += getDataAccess().execUpdate(sql, tableId);
        return ret;
    }

    /**
     * 删除程序数据
     *
     * @param tableId
     * @return
     */
    private int deleteTablePros(int tableId) {
        String sql = "DELETE FROM META_TABLE_PRO_INST_REL WHERE TABLE_ID=?";
        return getDataAccess().execUpdate(sql, tableId);
    }

    /**
     * 删除维度表类
     *
     * @param tableId
     * @return
     */
    private int deleteDimTables(int tableId) {
        String sql = "DELETE FROM META_DIM_TABLES WHERE DIM_TABLE_ID=?";
        return getDataAccess().execUpdate(sql, tableId);
    }

    /**
     * 删除维度归并类型
     *
     * @param tableId
     * @return
     */
    private int deleteDimTableTypes(int tableId) {
        String sql = "DELETE FROM META_DIM_TYPE WHERE DIM_TABLE_ID=?";
        String levelSql = "DELETE FROM META_DIM_LEVEL A WHERE EXISTS" +
                "(SELECT DIM_TYPE_ID FROM META_DIM_TYPE WHERE DIM_TYPE_ID=A.DIM_TYPE_ID AND DIM_TABLE_ID=?)";
        int ret = 0;
        ret += getDataAccess().execUpdate(levelSql, tableId); //先删归并层级
        ret += getDataAccess().execUpdate(sql, tableId);
        return ret;
    }

    /**
     * 删除维度映射
     *
     * @param tableId
     * @return
     */
    private int deleteDimTableMapping(int tableId) {
        String sql = "DELETE FROM META_DIM_MAPP WHERE DIM_TABLE_ID=?";
        return getDataAccess().execUpdate(sql, tableId);
    }

    /**
     * 删除维度编码审核记录
     *
     * @param tableId
     * @return
     */
    private int deleteDimTableCodeHis(int tableId) {
        String sql = "DELETE FROM META_DIM_TAB_MOD_HIS WHERE DIM_TABLE_ID=?";
        return getDataAccess().execUpdate(sql, tableId);
    }

    /**
     * 删除维度接口数据
     *
     * @param tableId
     * @return
     */
    private int deleteDimTableInterface(int tableId) {
        String sql = "DELETE FROM META_DIM_TAB_INT_REL WHERE DIM_TABLE_ID=?";
        return getDataAccess().execUpdate(sql, tableId);
    }

    /**
     * 删除业务表数据模型
     *
     * @param tableId
     * @return
     */
    private int deleteBusTableModels(int tableId) {
        String sql = "DELETE FROM META_RPT_MODEL_ISSUE_CONFIG WHERE TABLE_ID=?";
        String issueColSql = "DELETE FROM META_RPT_MODEL_ISSUE_COLS A WHERE EXISTS" +
                "(SELECT ISSUE_ID FROM META_RPT_MODEL_ISSUE_CONFIG WHERE ISSUE_ID=A.ISSUE_ID AND TABLE_ID=?)";//删除模型字段
        String issueLogSql = "DELETE FROM META_RPT_MODEL_ISSUE_LOG A WHERE EXISTS" +
                "(SELECT ISSUE_ID FROM META_RPT_MODEL_ISSUE_CONFIG WHERE ISSUE_ID=A.ISSUE_ID AND TABLE_ID=?)";//删除模型日志
        String auditSql = "DELETE FROM META_RPT_DATA_AUDIT_CFG A WHERE EXISTS" +
                "(SELECT ISSUE_ID FROM META_RPT_MODEL_ISSUE_CONFIG WHERE ISSUE_ID=A.ISSUE_ID AND TABLE_ID=?)";//删除数据审核规则
        int ret = 0;
        ret += getDataAccess().execUpdate(issueColSql, tableId);
        ret += getDataAccess().execUpdate(issueLogSql, tableId);
        ret += getDataAccess().execUpdate(auditSql, tableId);
        ret += getDataAccess().execUpdate(sql, tableId);
        return ret;
    }

    /**
     * Drop掉实例表
     *
     * @param instances
     * @return
     */
    private void dropInstanceTable(List<Map<String, Object>> instances) {
        for (Map<String, Object> map : instances) {
            String user = MapUtils.getString(map, "TABLE_OWNER");
            String tableName = MapUtils.getString(map, "TABLE_NAME");
            int dataSourceId = MapUtils.getInteger(map, "DATA_SOURCE_ID");
            try {
                String sql = "DROP TABLE " + user + "." + tableName + " ";
                getDataAccess(dataSourceId).execNoQuerySql(sql);
            } catch (Exception ex) {
                Log.info("DROP TABLE " + user + "." + tableName + " 发生异常!");
            }
        }
    }

    /**
     * 删除表类-查询表类信息列表
     *
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryMetaTablesMatain(Map<String, Object> queryData, Page page) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT, A.TABLE_SPACE, " +
                "A.TABLE_STATE, A.TABLE_ID, A.TABLE_GROUP_ID, A.DATA_SOURCE_ID, B.TABLE_GROUP_NAME, A.TABLE_TYPE_ID, " +
                "D.DATA_SOURCE_NAME,A.TABLE_VERSION,F.TABLE_DIM_LEVEL FROM META_TABLES A " +
                "LEFT JOIN META_TABLE_GROUP B ON A.TABLE_GROUP_ID = B.TABLE_GROUP_ID " +
                "LEFT JOIN META_DATA_SOURCE D ON A.DATA_SOURCE_ID = D.DATA_SOURCE_ID " +
                //关联维度表，获取其维度信息
                "LEFT JOIN META_DIM_TABLES F ON A.TABLE_ID=F.DIM_TABLE_ID " +
                //寻找有效版本
                ",(SELECT TABLE_ID,CASE WHEN MAX(TABLE_STATE)=0 THEN MAX(TABLE_VERSION) ELSE MAX(VALID_VERSION) END " +
                " VALID_VERSION  FROM " +
                "(SELECT TABLE_ID,TABLE_VERSION, TABLE_STATE,DECODE(TABLE_STATE,1,TABLE_VERSION,2,TABLE_VERSION,0)  " +
                "VALID_VERSION FROM META_TABLES  GROUP BY TABLE_ID,TABLE_VERSION,TABLE_STATE)  GROUP BY TABLE_ID) E " +
                "WHERE A.TABLE_ID=E.TABLE_ID AND A.TABLE_VERSION=E.VALID_VERSION ");
        List params = new ArrayList();
        if (queryData != null) {
            if (queryData.get("dataSourceId") != null && !queryData.get("dataSourceId").toString().equals("")) {
                sql.append("AND A.DATA_SOURCE_ID = ? ");
                int dataSourceId = queryData.get("dataSourceId") == null ? null : Integer.parseInt(queryData.get("dataSourceId").toString());
                params.add(dataSourceId);
            }

            if (queryData.get("tableGroupId") != null && !queryData.get("tableGroupId").toString().equals("")) {
                //业务类型
                sql.append("AND A.TABLE_GROUP_ID = ? ");
                int tableGroupId = queryData.get("tableGroupId") == null ? null : Integer.parseInt(queryData.get("tableGroupId").toString());
                params.add(tableGroupId);
            }

            if (queryData.get("tableTypeId") != null && !queryData.get("tableTypeId").toString().equals("")) {
                //层次分类
                sql.append("AND A.TABLE_TYPE_ID = ? ");
                int tableTypeId = queryData.get("tableTypeId") == null ? null : Integer.parseInt(queryData.get("tableTypeId").toString());
                params.add(tableTypeId);
            }

            //关键字处理，仅支持单个关键字
            if (queryData.get("keyWord") != null && !queryData.get("keyWord").toString().equals("")) {
                String key = queryData.get("keyWord").toString();
                if (!key.contains("%") && !key.contains("_")) {
                    sql.append("AND (Upper(A.TABLE_NAME) LIKE UPPER(?) OR ");
                    sql.append("UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) OR UPPER(A.TABLE_BUS_COMMENT) LIKE UPPER(?)) ");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                } else {
                    key = key.replaceAll("_", "/_").replaceAll("%", "/%");
                    sql.append("AND (Upper(A.TABLE_NAME) LIKE UPPER(?) ESCAPE '/' OR ");
                    sql.append("UPPER(A.TABLE_NAME_CN) LIKE UPPER(?) ESCAPE '/' OR UPPER(A.TABLE_BUS_COMMENT) LIKE UPPER(?) ESCAPE '/' ) ");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                    params.add("%" + key + "%");
                }
            }
        }

        sql.append("ORDER BY A.TABLE_ID DESC ");

        String pageSql = sql.toString();
        //分页包装
        if (page != null) {
            pageSql = SqlUtils.wrapPagingSql(pageSql, page);
        }

        List<Map<String, Object>> rs = getDataAccess().queryForList(pageSql, params.toArray());
        if (rs != null && rs.size() > 0) {
            for (Map<String, Object> map : rs) {
                map.put("CODE_NAME", CodeManager.getName(TblConstant.META_SYS_CODE_TABLE_TYPE, MapUtils.getString(map, "TABLE_TYPE_ID")));
            }
        }
        return rs;
    }
}
