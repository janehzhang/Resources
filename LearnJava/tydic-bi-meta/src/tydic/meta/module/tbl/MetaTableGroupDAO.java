package tydic.meta.module.tbl;

import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.Log;
import tydic.meta.common.Constant;
import tydic.meta.common.MetaBaseDAO;

/**
 * 
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * 
 * @author 熊小平
 * @date 2011-10-26
 * @description META_TABLE_GROUP操作DAO
 * 
 */
public class MetaTableGroupDAO extends MetaBaseDAO {
	/**
     * @description 增加查询表空间数据
     * @modifyBy 郭行风
     * @modifyDate 2012-01-16
     * @return 查询的表空间
     */
	public List<Map<String, Object>> queryTableSpaceAll(){
		String sql = "select T.NAME from V$TABLESPACE T";
		return getDataAccess().queryForList(sql);
	}
    /**
     * @description 查询元数据表的编码
     * @return 查询结果
     */
    public List<Map<String, Object>> queryTableGroup(int tableType) {
        String sql = "SELECT T.TABLE_GROUP_ID,T.TABLE_GROUP_NAME,T.PAR_GROUP_ID "
                + "FROM META_TABLE_GROUP T WHERE TABLE_TYPE_ID=?";
        return getDataAccess().queryForList(sql, tableType);
    }

    /**
     * @description 查询表类分类，显示为树结构
     * @param queryData
     *            查询参数
     * @modifyBy 王晶
     * @modifyDate 2011-10-27
     * @modifyReson 修改了sql,已经将原来的sql注释掉
     * @return
     */
    public List<Map<String, Object>> queryTableGroup(
            Map<String, Object> queryData) {
//        String sql = "SELECT G.TABLE_GROUP_ID, G.TABLE_GROUP_NAME, G.PAR_GROUP_ID,G.TABLE_TYPE_ID ,C.CODE_NAME," +
//              "C.CODE_ID, "
//                + "DECODE(NVL(C.CNT, 0), 0, 0, 1) AS CHILDREN "
//                + "FROM META_TABLE_GROUP G, META_SYS_CODE C "
//                + "LEFT JOIN (SELECT PAR_GROUP_ID,COUNT(1) COUNT FROM META_TABLE_GROUP GROUP BY PAR_GROUP_ID) A "
//                + "ON G.TABLE_GROUP_ID = A.PAR_GROUP_ID "
//                + "WHERE G.TABLE_TYPE_ID = C.CODE_ID AND G.PAR_GROUP_ID = ? ";
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT G.TABLE_GROUP_ID, G.TABLE_GROUP_NAME, G.PAR_GROUP_ID, G.TABLE_TYPE_ID, C.CODE_NAME,");
        sb.append("C.CODE_ID, P.TABLE_GROUP_NAME PAR_TABLE_GROUP_NAME ");
        sb.append("FROM META_TABLE_GROUP G ,META_TABLE_GROUP P, META_SYS_CODE C ");
        sb.append("WHERE G.PAR_GROUP_ID = P.TABLE_GROUP_ID(+) AND G.TABLE_TYPE_ID || '' = C.CODE_VALUE(+) ");
        // 如果该参数缺省，则解析为0，即查询所有顶级节点
        int codeId = queryData.get("tableCategory")==null?0:Integer.parseInt(queryData.get("tableCategory").toString());
        if(codeId==0){
            sb.append(" AND C.CODE_TYPE_ID=4");
        }else{
        	sb.append(" AND C.CODE_TYPE_ID=4");
        	sb.append(" AND C.CODE_VALUE="+codeId);
        }
        String sql = sb.toString();
        return getDataAccess().queryForList(sql);
    }

    /**
     * @description 查询子表类分类
     * @param parentId
     * @return
     */
    public List<Map<String, Object>> querySubTableGroup(int parentId) {
        String sql = "SELECT G.TABLE_GROUP_ID,G.TABLE_GROUP_NAME,C.CODE_NAME "
                + "FROM META_TABLE_GROUP G "
                + "LEFT JOIN META_SYS_CODE C ON G.TABLE_TYPE_ID = C.CODE_ID "
                + "WHERE G.PAR_GROUP_ID = ? ";
        Object params[] = new Object[1];
        params[0] = parentId;
        return getDataAccess().queryForList(sql, params);
    }

    /**
     * @description 查询父表类分类
     * @param parentId 传入parGroupId值
     * @return 顶级节点返回null，其他返回其相应的父表类
     */
    public Map<String, Object> queryParTableGroup(int parentId) {
        String sql = "SELECT G.TABLE_GROUP_ID,G.TABLE_GROUP_NAME,C.CODE_NAME "
                + "FROM META_TABLE_GROUP G "
                + "LEFT JOIN META_SYS_CODE C ON G.TABLE_TYPE_ID = C.CODE_ID "
                + "WHERE G.GROUP_ID = ? ";
        Object params[] = new Object[1];
        params[0] = parentId;
        List<Map<String,Object>> result = getDataAccess().queryForList(sql, params);
        if(result.size() == 0){
            return null;
        }else{
            return result.get(0);
        }
    }

    /**
     * @description 增加表类分类，同级或下级均可，只是参数parGroupId的值不同而已
     * @param data
     * @return
     */
    public boolean insertTableGroup(Map<String, Object> data) {
        String sql = "INSERT INTO META_TABLE_GROUP( "
                + "TABLE_GROUP_ID,TABLE_GROUP_NAME,PAR_GROUP_ID,TABLE_TYPE_ID) "
                + "VALUES(SEQ_TAB_GROUP_ID.NEXTVAL,?,?,?) ";
        Object params[] = new Object[3];
        String tableGroupName = (String) data.get("tableName");
        params[0] = tableGroupName;
        Integer parGroupId = Integer.valueOf(data.get("parentId").toString());
        params[1] = parGroupId == null ? 0 : parGroupId;// 0为默认值
        Integer tableTypeId = Integer.valueOf(data.get("tableTypeId").toString());
        params[2] = tableTypeId;
        return getDataAccess().execNoQuerySql(sql, params);
    }
    
    /**
     * 修改表类分类，只有单步操作，因此无事务控制
     * @param data 修改参数，其中tableGroupId不能缺省
     * @return
     */
    public boolean updateTableGroup(Map<String,Object> data){
//        String sql = "UPDATE META_TABLE_GROUP " +
//                "SET TABLE_GROUP_NAME=?, " +
//                "TABLE_TYPE_ID=? " +
//                "WHERE TABLE_GROUP_ID=? ";
    	 String sql = "UPDATE META_TABLE_GROUP " +
         "SET TABLE_GROUP_NAME=?" +
         "WHERE TABLE_GROUP_ID=? ";
        Object params[] = new Object[2];

        String tableGroupName = Convert.toString(data.get("tableGroupName"));
        params[0] = tableGroupName;
        if("".equals(tableGroupName)){
            Log.warn("修改表类：TABLE_GROUP_NAME字段为空");
        }
//        int tableTypeId = Convert.toInt(data.get("tableTypeId"));
//        params.addValue(tableTypeId);
        int tableGroupId = data.get("tableGroupId")==null?null:Integer.parseInt(data.get("tableGroupId").toString());
        params[1] = tableGroupId;
        return getDataAccess().execNoQuerySql(sql, params);
    }
    
    /**
     * 根据id删除单条表类分类
     * @param groupId 删除参数，其中应该包含待删除表类分组的id值
     * @return
     * @throws Exception 
     */
    public boolean deleteTableGroup(int groupId) throws Exception{
//        String sql = "SELECT TABLE_GROUP_ID FROM META_TABLES WHERE TABLE_GROUP_ID = ? ";
//        ProParams params = new ProParams();
//        params.addValue(id);
//        List<Map<String,Object>> result = getDataAccess().queryForList(sql, params);
//        if(result.size() != 0){
//            return "FKConstraint";
//        }
//        
//        sql = "SELECT PAR_GROUP_ID FROM META_TABLE_GROUP WHERE PAR_GROUP_ID = ? ";
//        result = getDataAccess().queryForList(sql,params);
//        if(result.size() != 0){
//            return "PARConstraint";
//        }
//        
//        sql = "DELETE FROM META_TABLE_GROUP WHERE TABLE_GROUP_ID = ? ";
//        if(getDataAccess().execNoQuerySql(sql, params)){
//            return "Success";
//        }else{
//            return "Fail";
//        }
    	String sonSql = "SELECT T.TABLE_GROUP_ID FROM META_TABLE_GROUP T START WITH T.TABLE_GROUP_ID="+groupId+" CONNECT BY PRIOR T.TABLE_GROUP_ID=T.PAR_GROUP_ID";
    	List<Map<String,Object>> sonList = this.getDataAccess().queryForList(sonSql);
    	if(sonList.size()==1){
    		String tableSql = "SELECT T.TABLE_ID FROM META_TABLES T WHERE T.TABLE_GROUP_ID="+groupId;
    		List<Map<String,Object>> tableList = this.getDataAccess().queryForList(tableSql);
    		if(tableList==null||tableList.size()==0){
    			String deleteSql =" DELETE FROM META_TABLE_GROUP T WHERE T.TABLE_GROUP_ID="+groupId;
    			int count = getDataAccess().execUpdate(deleteSql);
    			if(count==1){
    			 return true;
    			}
    		}
    	}
    	return false;
    }

    /**
     * 根据起始结束节点查询Group树
     * @param beginId
     * @param endId
     * @return
     */
    public List<Map<String, Object>> queryGroupByBeginEndPath(int beginId,
            int endId,String levelRefID) {
    	
//    	String tmpSql = "SELECT CODE_ITEM  FROM  META_SYS_CODE A WHERE A.CODE_ID ="+levelRefID;
//    	String tableTypeId = getDataAccess().queryForString(tmpSql);
//
    	
        StringBuffer sql = new StringBuffer(
                "SELECT A.TABLE_GROUP_ID, A.PAR_GROUP_ID,A.TABLE_GROUP_NAME,A.TABLE_TYPE_ID "
                        + ",DECODE(NVL(C.CNT,0),0,0,1) AS CHILDREN FROM META_TABLE_GROUP A ");
        // 关联子查询，用于查询是否还有子节点
        sql.append("LEFT JOIN (SELECT PAR_GROUP_ID,COUNT(1) CNT FROM META_TABLE_GROUP GROUP BY PAR_GROUP_ID) C ");
        // 连接条件
        sql.append("ON A.TABLE_GROUP_ID=C.PAR_GROUP_ID ");
        // 下面的SQL用于当endId存在的时候限制查询的层级，如果endId不存在，只查询到begId下一个层级即可。用不到下面这段逻辑。
        if (endId > 0) {
            sql.append("WHERE A.PAR_GROUP_ID IN ");
            sql.append("(SELECT A.TABLE_GROUP_ID FROM META_TABLE_GROUP A  ");
            sql.append("WHERE  LEVEL<= ");
            sql.append("(SELECT NVL(MAX(L),99999999999999) FROM (SELECT TABLE_GROUP_ID,PAR_GROUP_ID, LEVEL L "
                    + "FROM META_TABLE_GROUP CONNECT BY PRIOR PAR_GROUP_ID=TABLE_GROUP_ID START WITH TABLE_GROUP_ID="
                    + endId
                    + ") A "
                    + "WHERE A."
                    + (beginId == Constant.DEFAULT_ROOT_PARENT ? "PAR_GROUP_ID="
                    : "TABLE_GROUP_ID=")
                    + beginId
                    + " )"
                    + " CONNECT BY  PRIOR A.TABLE_GROUP_ID=A.PAR_GROUP_ID START WITH "
                    + (beginId == Constant.DEFAULT_ROOT_PARENT ? "PAR_GROUP_ID="
                    : "TABLE_GROUP_ID=") + beginId + ") ");
            if (beginId == Constant.DEFAULT_ROOT_PARENT) {
                sql.append("OR A.PAR_GROUP_ID =" + beginId);
            }

        } else {// 如果不存在endId，指定查找其子节点数据
            sql.append("WHERE (A.PAR_GROUP_ID=" + beginId + " OR A.TABLE_GROUP_ID="
                    + beginId+") ");
        }
//        if(tableTypeId != null && levelRefID != null) {
//        	sql.append("AND A.TABLE_TYPE_ID = "+tableTypeId);
//        }
        
        return getDataAccess().queryForList(sql.toString());
    }

    /**
     * 动态加载业务类型子菜单数据
     * @param parentId
     * @return
     */
    public List<Map<String, Object>> querySubGroup(int parentId) {
                String sqlQuerySubDept = "SELECT A.TABLE_GROUP_ID, A.PAR_GROUP_ID, A.TABLE_GROUP_NAME, A.TABLE_TYPE_ID, " +
                " DECODE(NVL(C.CNT,0),0,0,1) AS CHILDREN FROM meta_table_group A LEFT JOIN " +
                " (SELECT PAR_GROUP_ID, COUNT(1) CNT FROM meta_table_group GROUP BY PAR_GROUP_ID) " +
                " C ON A.TABLE_GROUP_ID=C.PAR_GROUP_ID WHERE  A.PAR_GROUP_ID=? ORDER BY CHILDREN DESC";
        Object params[] = new Object[1];
        params[0] = parentId;
        return getDataAccess().queryForList(sqlQuerySubDept, params);
    }
}
