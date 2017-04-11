package tydic.meta.module.tbl.diff;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.Page;
import tydic.meta.common.SqlUtils;
import tydic.meta.sys.code.CodeManager;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @date 2011-11-14
 * @description 表META_TABLE_DIFF的操作类
 */
public class DiffDAO extends MetaBaseDAO {

    /**
     * 条件查询有差异的表类数据源，根据数据源进行分组统计
     * @param startDate
     * @param endDate
     * @param dataSourceId
     * @param page
     * @return
     */
    public List<Map<String,Object>> queryDiffList(Date startDate,Date endDate,String dataSourceId,Page page){
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT TO_CHAR(A.CREATE_DATE,'YYYY-MM-DD') CREATE_DATE, C.DATA_SOURCE_ID, C.DATA_SOURCE_NAME, COUNT(*) DIFF_COUNT ");
        sql.append(" FROM META_TABLE_DIFF A, META_TABLES B, META_DATA_SOURCE C ");
        sql.append(" WHERE A.CREATE_DATE>=TO_DATE(?,'YYYY-MM-DD') AND A.CREATE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS') ");
        if(dataSourceId!=null&&!dataSourceId.equals("")){
            sql.append(" AND B.DATA_SOURCE_ID = "+dataSourceId);
        }
        sql.append(" AND A.TABLE_ID = B.TABLE_ID AND A.STATE=1 AND A.TABLE_VERSION=B.TABLE_VERSION AND B.DATA_SOURCE_ID = C.DATA_SOURCE_ID ");
        sql.append(" GROUP BY TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD'), C.DATA_SOURCE_ID, C.DATA_SOURCE_NAME ");
        sql.append(" ORDER BY TO_CHAR(A.CREATE_DATE, 'YYYY-MM-DD') DESC ");
        return super.getDataAccess().queryForList(SqlUtils.wrapPagingSql(sql.toString(), page),DateUtil.format(startDate,"yyyy-MM-dd"),DateUtil.format(endDate,"yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 根据数据源查询所有的差异表类
     * @param dataSourceId
     * @return
     */
    public List<Map<String,Object>> queryDetialDiffList(int dataSourceId,String dateStr,Page page){
        String sql="SELECT A.TABLE_NAME, A.TABLE_NAME_CN, A.TABLE_OWNER, A.TABLE_BUS_COMMENT, A.TABLE_SPACE, " +
                   "A.TABLE_STATE, A.TABLE_ID, A.TABLE_GROUP_ID, A.DATA_SOURCE_ID, B.TABLE_GROUP_NAME, A.TABLE_TYPE_ID, " +
//                   "C.CODE_NAME,D.DATA_SOURCE_NAME,A.TABLE_VERSION " + "FROM META_TABLES A  " +
                    "             D.DATA_SOURCE_NAME,A.TABLE_VERSION " + "FROM META_TABLES A  " +
                   "LEFT JOIN META_TABLE_GROUP B ON A.TABLE_GROUP_ID = B.TABLE_GROUP_ID " +
//                   "LEFT JOIN META_SYS_CODE C ON B.TABLE_TYPE_ID=C.CODE_ITEM AND C.CODE_TYPE='TABLE_TYPE' " +
                   "LEFT JOIN META_DATA_SOURCE D ON A.DATA_SOURCE_ID = D.DATA_SOURCE_ID " +
                   "INNER JOIN META_TABLE_DIFF E ON E.TABLE_ID=A.TABLE_ID AND E.TABLE_VERSION=A.TABLE_VERSION " +
                   "WHERE A.DATA_SOURCE_ID=? AND E.STATE=1 AND E.CREATE_DATE>=TO_DATE(?,'YYYY-MM-DD') AND E.CREATE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS') ORDER BY A.TABLE_ID,A.TABLE_VERSION";
        List<Map<String,Object>> rs = getDataAccess().queryForList(SqlUtils.wrapPagingSql(sql, page),dataSourceId, dateStr, dateStr+" 23:59:59");
        //加入码表信息
        if(rs!=null&&rs.size()>0){
            for(Map<String,Object> map:rs){
                map.put("CODE_NAME", CodeManager.getName("TABLE_TYPE", MapUtils.getString(map,"TABLE_TYPE_ID")));
            }
        }
        return   rs;
    }

    /**
     * 执行SQL
     * @param sql
     * @param dataSource
     * @return
     * @throws Exception
     */
    public void executeSQL(String sql,Map<String,Object> dataSource) throws Exception{
        Connection connection=getConnection(MapUtils.getString(dataSource,"DATA_SOURCE_ID"));
        Statement statement =connection.createStatement() ;
        statement.executeUpdate(sql);
        statement.close();
        connection.close();
    }

    /**
     * 同步数据失败的回退操作，其基本思想就是将所有生成的临时表干掉，不管其存在与不存在。
     * @param tableNames 生成的临时表表名集合
     * @param dataSource 数据源信息
     */
    public void rollBack(String[] tableNames,Map<String,Object> dataSource){
        Connection connection=getConnection(MapUtils.getString(dataSource,"DATA_SOURCE_ID"));
        try{
            Statement statement=connection.createStatement() ;
            try{
                for(String tableName:tableNames){
                    statement.execute("DROP TABLE "+tableName);
                }
            }catch(SQLException e){}//忽略删除表错误。
            connection.close();
        }catch(Exception e){ }
    }

    /**
     * 更新差异表中的状态为无效
     * @param diffId
     */
    public void updateDiffStateInVaildate(int diffId) throws Exception{
        String sql="UPDATE META_TABLE_DIFF SET STATE=0 WHERE TABLE_ID=?";
        getDataAccess().execUpdate(sql,diffId);
    }

}
