package tydic.meta.module.tbl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.frame.common.utils.StringUtils;
import tydic.meta.common.Constant;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 熊小平
 * @date 2011-10-26
 * @description META_DATA_SOURCE表操作DAO
 */
public class MetaDataSourceDAO extends MetaBaseDAO {

    private static Map<String, Object> MetaDataSouce = null;
    private static Map<String, Object> DimDataSouce = null;

    /**
     * 查找当前的数据源
     *
     * @return
     */
    public final static Map<String, Object> queryDIMDataSource() {
        if (DimDataSouce != null) {
            return DimDataSouce;
        } else {
            MetaDataSourceDAO metaDataSourceDAO = new MetaDataSourceDAO();
            DimDataSouce = metaDataSourceDAO.queryDataSourceById(TblConstant.META_DIM_DATA_SOURCE_ID);
            metaDataSourceDAO.close();
        }
        return DimDataSouce;
    }
    
    /**
     * 查找当前的数据源
     *
     * @return
     */
    public final static Map<String, Object> queryMetaDataSource() {
        if (MetaDataSouce != null) {
            return MetaDataSouce;
        } else {
            MetaDataSourceDAO metaDataSourceDAO = new MetaDataSourceDAO();
            MetaDataSouce = metaDataSourceDAO.queryDataSourceById(TblConstant.META_DATA_SOURCE_ID);
            metaDataSourceDAO.close();
        }
        return MetaDataSouce;
    }

    /**
     * 获取元数据管理用户
     *
     * @return
     */
    public final static String getDimOwner() {
        Map<String, Object> ds = queryDIMDataSource();
        return MapUtils.getString(ds, "DATA_SOURCE_USER");
    }
    
    /**
     * 获取元数据管理用户
     *
     * @return
     */
    public final static String getMetaOwner() {
        Map<String, Object> ds = queryMetaDataSource();
        return MapUtils.getString(ds, "DATA_SOURCE_USER");
    }

    /**
     * 根据ID查询数据源信息
     *
     * @param id
     * @return
     */
    public Map<String, Object> queryDataSourceById(int id) {
        String sql = "SELECT A.DATA_SOURCE_ID,A.DATA_SOURCE_NAME,A.DATA_SOURCE_ORANAME,A.DATA_SOURCE_USER, " +
                "A.DATA_SOURCE_PASS, A.DATA_SOURCE_RULE ,A.DATA_SOURCE_MIN_COUNT "
                + "FROM META_DATA_SOURCE A "
                + "WHERE A.DATA_SOURCE_ID=?";
        return getDataAccess().queryForMap(sql, id);
    }

    /**
     * 根据表ID和表版本查询表类数据源信息
     *
     * @param tableId
     * @param tableVersion
     * @return
     */
    public Map<String, Object> queryDataSourceByTable(int tableId, int tableVersion) {
        String sql = "SELECT A.DATA_SOURCE_ID,A.DATA_SOURCE_NAME,A.DATA_SOURCE_ORANAME,A.DATA_SOURCE_USER, " +
                "A.DATA_SOURCE_PASS, A.DATA_SOURCE_RULE "
                + "FROM META_DATA_SOURCE A,META_TABLES B  "
                + "WHERE B.TABLE_ID=" + tableId + " AND B.TABLE_VERSION=" + tableVersion + " AND A.DATA_SOURCE_ID=B.DATA_SOURCE_ID ";
        return getDataAccess().queryForMap(sql);
    }
    
    public List<Map<String, Object>> queryValidDataSource(){
    	String sql = "SELECT A.DATA_SOURCE_ID,A.DATA_SOURCE_NAME,A.DATA_SOURCE_ORANAME,A.DATA_SOURCE_USER, " +
        "A.DATA_SOURCE_PASS, A.DATA_SOURCE_RULE ,A.DATA_SOURCE_MIN_COUNT "
        + "FROM META_DATA_SOURCE A "
        + "WHERE A.data_source_state=?";
    	return getDataAccess().queryForList(sql, TblConstant.DATA_SOURCE_VALID);
    }

    /**
     * 根据关键字查询数据源信息
     *
     * @param key
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryDataSource(String key, Page page) {
        List<String> param = new ArrayList<String>();
        String sql = "SELECT T.DATA_SOURCE_ID, T.DATA_SOURCE_NAME, T.DATA_SOURCE_ORANAME, T.DATA_SOURCE_USER, T.DATA_SOURCE_PASS,\n" +
                " T.DATA_SOURCE_TYPE, T.DATA_SOURCE_RULE, T.DATA_SOURCE_STATE, T.DATA_SOURCE_INTRO, T.SYS_ID, " +
                " T.DATA_SOURCE_MIN_COUNT, A.SYS_NAME " +
                " FROM META_DATA_SOURCE T LEFT JOIN META_SYS A ON T.SYS_ID = A.SYS_ID " +
                " WHERE T.DATA_SOURCE_ID <> " + TblConstant.META_DATA_SOURCE_ID;
        if(!StringUtils.isEmpty(key)) {
            sql = sql + " AND (T.DATA_SOURCE_NAME LIKE ? ESCAPE '/' OR T.DATA_SOURCE_INTRO LIKE ?)";
            param.add("%" + key.replaceAll("_", "/_").replaceAll("%", "/%") + "%");
            param.add("%" + key.replaceAll("_", "/_").replaceAll("%", "/%") + "%");
        }
        sql = sql + " ORDER BY T.DATA_SOURCE_ID DESC ";
        if (page != null) {
            sql = SqlUtils.wrapPagingSql(sql, page);
        }
        return getDataAccess().queryForList(sql, param.toArray());
    }

    /**
     * 根据数据源类型查询相应的状态为有效的数据源
     *
     * @param type 数据源类型名称
     * @return
     */
    public List<Map<String, Object>> queryDataSourceByType(String type) {
        String sql = "SELECT DATA_SOURCE_ID, DATA_SOURCE_NAME, DATA_SOURCE_ORANAME, DATA_SOURCE_USER, DATA_SOURCE_PASS," +
                " DATA_SOURCE_TYPE, DATA_SOURCE_RULE, DATA_SOURCE_STATE, DATA_SOURCE_INTRO, SYS_ID," +
                " DATA_SOURCE_MIN_COUNT" +
                " FROM META_DATA_SOURCE WHERE DATA_SOURCE_STATE=1 AND DATA_SOURCE_TYPE=?";
        return getDataAccess().queryForList(sql, type);
    }

    /**
     * 重载方法：根据数据源类型和状态查询相应的数据源
     *
     * @param type  数据源类型名称
     * @param valid 数据源状态
     * @return
     */
    public List<Map<String, Object>> queryDataSourceByType(String type, int valid) {
        String sql = "SELECT DATA_SOURCE_ID, DATA_SOURCE_NAME, DATA_SOURCE_ORANAME, DATA_SOURCE_USER, DATA_SOURCE_PASS," +
                " DATA_SOURCE_TYPE, DATA_SOURCE_RULE, DATA_SOURCE_STATE, DATA_SOURCE_INTRO, SYS_ID," +
                " DATA_SOURCE_MIN_COUNT" +
                " FROM META_DATA_SOURCE WHERE DATA_SOURCE_TYPE=? AND DATA_SOURCE_STATE=? ORDER BY DATA_SOURCE_ID ";
        List<Object> param = new ArrayList<Object>();
        param.add(type);
        param.add(valid);
        return getDataAccess().queryForList(sql, param.toArray());
    }

    /**
     * 保存
     *
     * @param data
     */
    public void insertDataSource(Map<String, Object> data) {
        String sql = "INSERT INTO META_DATA_SOURCE " +
                "(DATA_SOURCE_ID, DATA_SOURCE_NAME, DATA_SOURCE_ORANAME, DATA_SOURCE_USER, DATA_SOURCE_PASS, " +
                "DATA_SOURCE_TYPE, DATA_SOURCE_RULE, DATA_SOURCE_STATE, DATA_SOURCE_INTRO, SYS_ID, " +
                "DATA_SOURCE_MIN_COUNT) " +
                " VALUES " +
                " (?, ?, ?, ?, ?, " +
                "?, ?, ?, ?, ?, ?)";
        Object[] param = {
                Convert.toInt(data.get("dataSourceId")), Convert.toString(data.get("dataSourceName")), Convert.toString(data.get("dataSourceOraname")), Convert.toString(data.get("dataSourceUser")), Convert.toString(data.get("dataSourcePass")),
                Convert.toString(data.get("dataSourceType")), Convert.toString(data.get("dataSourceRule")), 1, Convert.toString(data.get("dataSourceIntro")), Convert.toInt(data.get("sysId")),
                Convert.toInt(data.get("dataSourceMinCount"))
        };
        getDataAccess().execUpdate(sql, param);
    }

    /**
     * 修改
     *
     * @param data
     * @return
     */
    public int updateDataSource(Map<String, Object> data) {
        String sql = "UPDATE META_DATA_SOURCE " +
                " SET DATA_SOURCE_NAME = ?, " +
                " DATA_SOURCE_ORANAME = ?, " +
                " DATA_SOURCE_USER = ?, " +
                " DATA_SOURCE_PASS = ?, " +
                " DATA_SOURCE_TYPE = ?, " +
                " DATA_SOURCE_RULE = ?, " +
                " DATA_SOURCE_STATE = ?, " +
                " DATA_SOURCE_INTRO = ?, " +
                " SYS_ID = ?, " +
                " DATA_SOURCE_MIN_COUNT = ? " +
                " WHERE DATA_SOURCE_ID = ? ";
        Object[] param = {
                Convert.toString(data.get("dataSourceName")), Convert.toString(data.get("dataSourceOraname")), Convert.toString(data.get("dataSourceUser")), Convert.toString(data.get("dataSourcePass")),
                Convert.toString(data.get("dataSourceType")), Convert.toString(data.get("dataSourceRule")), 1, Convert.toString(data.get("dataSourceIntro")), Convert.toInt(data.get("sysId")),
                Convert.toInt(data.get("dataSourceMinCount")), Convert.toInt(data.get("dataSourceId"))
        };
        return getDataAccess().execUpdate(sql, param);
    }

    /**
     * 根据ID取得datasource信息，包括起所属系统信息
     *
     * @param dataSourceId
     * @return
     */
    public Map<String, Object> queryDetailById(int dataSourceId) {
        String sql = "SELECT t.DATA_SOURCE_ID, t.DATA_SOURCE_NAME, t.DATA_SOURCE_ORANAME, t.DATA_SOURCE_USER," +
                " t.DATA_SOURCE_PASS, t.DATA_SOURCE_TYPE, t.DATA_SOURCE_RULE, t.DATA_SOURCE_STATE, " +
                " t.DATA_SOURCE_INTRO, t.SYS_ID, t.DATA_SOURCE_MIN_COUNT,T.DIM_USER,a.sys_name " +
                "FROM   META_DATA_SOURCE t LEFT JOIN META_SYS a ON a.sys_id = t.sys_id " +
                "WHERE t.data_source_id=" + dataSourceId;
        return getDataAccess().queryForMap(sql);
    }

    /**
     * 数据源上线
     *
     * @param sourceId
     * @return
     */
    public int onlineDataSource(int sourceId) {
        String sql = "UPDATE META_DATA_SOURCE SET DATA_SOURCE_STATE = " + TblConstant.DATA_SOURCE_VALID + " WHERE DATA_SOURCE_ID=" + sourceId;
        return getDataAccess().execUpdate(sql);
    }

    /**
     * 数据源下线
     *
     * @param sourceId
     * @return
     */
    public int offlineDataSource(int sourceId) {
        String sql = "UPDATE META_DATA_SOURCE SET DATA_SOURCE_STATE = " + TblConstant.DATA_SOURCE_INVALID + " WHERE DATA_SOURCE_ID=" + sourceId;
        return getDataAccess().execUpdate(sql);
    }

    /**
     * 测试数据源
     *
     * @param url
     * @param user
     * @param pwd
     * @throws Exception
     */
    public void testDataSource(String url, String user, String pwd) throws Exception {
        Connection connection = null;
        try {
            connection = getConnection(user, pwd, url, Constant.ORACLE_DRIVER_STRING);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
            }
        }

    }

    /**
     * 检查数据源是否被表类引用
     *
     * @param dataSourceId
     * @return
     */
    public boolean checkUsing(int dataSourceId) {
        String sql = "select count(*) from meta_tables t where t.data_source_id=" + dataSourceId;
        return getDataAccess().queryForInt(sql) > 0;
    }

    public List<Map<String, Object>> querySysInfo() {
        String sql = "SELECT SYS_ID, SYS_NAME FROM META_SYS ORDER BY SYS_ID ";
        return getDataAccess().queryForList(sql);
    }
}
