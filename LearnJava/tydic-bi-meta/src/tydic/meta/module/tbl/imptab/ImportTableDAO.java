package tydic.meta.module.tbl.imptab;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import tydic.frame.jdbc.DataAccess;
import tydic.frame.jdbc.IParamsSetter;
import tydic.meta.common.Constant;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.common.SqlUtils;
import tydic.meta.module.tbl.TblConstant;

/**
 * 
 * @author 李国民
 * @description 表类管理-导入实体表DAO <br>
 * @date 2012-01-10
 */
public class ImportTableDAO extends MetaBaseDAO{
	
	/**
	 * 查询数据库表信息
	 * @param url 数据库连接地址
	 * @param user 登录名
	 * @param pwd 密码
	 * @param owner 拥有者
	 * @param keyWord 关键字
	 * @param tableNamesList 已经存在的映射表的数据集合
	 * @return
	 */
    public List<Map<String, Object>> queryDbTables(int dataSourceID, String owner, 
    		String keyWord, List<String> tableNamesList){ 
        String sql = "SELECT A.OWNER AS TABLE_OWNER,A.TABLE_NAME,TABLESPACE_NAME TABLE_SPACE "+
        		" FROM ALL_TABLES A WHERE A.OWNER=? ";
        if(keyWord != null && !keyWord.equals("")){
            sql += " AND A.TABLE_NAME LIKE "+ SqlUtils.allLikeParam(keyWord);
        }
        if(tableNamesList!=null && tableNamesList.size()!=0){
        	for (int i = 0; i < tableNamesList.size(); i++) {
				String tableNames = (String) tableNamesList.get(i);
		        if(tableNames != null && !tableNames.equals("")){
		            sql += " AND A.TABLE_NAME not in ("+tableNames+") ";
		        }
			}
        }
        sql += " ORDER BY A.TABLE_NAME";
        return getDataAccess(dataSourceID).queryForList(sql, owner);
    }
	
    /**
     * 得到实体表映射表里面的信息
     * @param owner 表用户
     * @return
     */
    public List<Map<String, Object>> queryInstTables(String owner){ 
    	String sql = "SELECT T.TABLE_NAME FROM META_TABLE_INST T WHERE T.TABLE_OWNER=?";
        return getDataAccess().queryForList(sql,owner);
    }
    
    /**
     * 查询出带宏变量的表类
     * @param dataSourceID 数据源id
     * @return
     */
    public List<Map<String, Object>> queryMetaTables(int dataSourceID){ 
    	String sql = "SELECT T.TABLE_ID,T.TABLE_NAME,T.TABLE_VERSION FROM " +
    			" META_TABLES T WHERE T.TABLE_NAME LIKE '%{%}%' " +
    			" AND T.TABLE_STATE=1 AND T.DATA_SOURCE_ID=?";
        return getDataAccess().queryForList(sql,dataSourceID);
    }
    
    /**
     * 批量导入实体表
     * @param datas 实体表映射关系数据
     * @return
     */
    public int[] insertMetaMagUserTable(final List<Map<String,Object>> datas) {
        String sql = "INSERT INTO META_TABLE_INST(TABLE_INST_ID,TABLE_NAME,TABLE_ID," +
        		"TABLE_SPACE,TABLE_OWNER,TABLE_VERSION,STATE,TABLE_DATE) " +
        		"VALUES (?,?,?,?,?, ?,?,sysdate)";
        return getDataAccess().execUpdateBatch(sql, new IParamsSetter(){
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException{
                Map<String,Object> data=datas.get(i);
                preparedStatement.setObject(1,queryForNextVal("SEQ_TAB_INST_ID")); 	//自动生成id
                preparedStatement.setObject(2,data.get("tableName")); 	//实体表表名
                preparedStatement.setObject(3,data.get("tableId")); //表类id
                preparedStatement.setObject(4,data.get("tableSpace"));//表空间
                preparedStatement.setObject(5,data.get("tableOwner"));   	//表用户
                preparedStatement.setObject(6,data.get("tableVersion")); //表类版本号
                preparedStatement.setObject(7, TblConstant.META_TABLE_STATE_VAILD); //状态
            }
            public int batchSize(){
                return datas.size();
            }
        });
    }
    
    /**
     * 判断是否存在该地域宏变量
     * @param localCode
     * @return
     */
    public boolean isExistLocalCode(String localCode){
    	String sql = "SELECT * FROM META_DIM_ZONE T WHERE T.ZONE_CODE=?";
    	List<Map<String, Object>> rs = getDataAccess().queryForList(sql,localCode);
    	return rs!=null&&rs.size()>0;
    }
}
