package tydic.meta.common;

import tydic.frame.BaseDAO;
import tydic.frame.jdbc.DataAccess;
import tydic.frame.jdbc.DataAccessFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 元数据管理项目DAO基类，提供与项目有关的基础方法<br>
 * @date 2011-10-06
 */
public class MetaBaseDAO extends BaseDAO{

    /**
     * 是否记录操作日志
     */
    private boolean isWriteLog = true;

    public MetaBaseDAO(){
    }

    public MetaBaseDAO(boolean writeLog){
        isWriteLog = writeLog;
    }

    /**
     * 覆盖父类获取dataAcess方法，添加操作日志处理逻辑的access类。
     * @param connection
     * @return
     */
    protected DataAccess getDataAccessInstance(Connection connection){
        if(isWriteLog){
//            DataAccess access= DataAccessFactory.getProxyDataAccess(connection,new InvokeAdapter(){
//                        /**
//                         * 实现此方法用于在方法执行执行记录操作日志。该方法不会引起业务的正常运行，若记录
//                         * 日志失败，只会简单的处理错误，打印到日志文件中，业务方法会继续运行。
//                         * @param source  拦截对象
//                         * @param methodName  运行时方法名。
//                         * @param args 参数集合
//                         * @return
//                         */
//                        public boolean beforeInvoke(Object source, String methodName, Object[] args){
//                            return true;
//                        }
//                    }, new String[]{"exec\\w*", "query\\w*", "insert\\w*"},//要进行拦截的方法正则表达式。
//                    new IAopMethodFilter(){
//                        /**
//                         * 过滤器，过滤某个方法，返回true表示可以进行AOP拦截，返回false表示不能进行AOP拦截，与AopFactory联合使用
//                         * @param souceClass
//                         * @param  methodName
//                         * @param paramType
//                         * @return
//                         */
//                        public boolean filter(Class<?> souceClass, String methodName, Class<?>[] paramType){
//                            if((methodName.equals("queryForList") || methodName.equals("queryForMap") ||
//                                methodName.equals("queryForDataTable") || methodName.equals("queryForInt") ||
//                                methodName.equals("queryForObject") || methodName.equals("queryForObjectArray")) &&
//                               paramType.length == 1){
//                                return false;
//                            } else if((methodName.equals("queryForListByRowMapper") ||
//                                       methodName.equals("queryByRowHandler")) && paramType.length == 2){
//                                return false;
//                            }
//                            return true;
//                        }
//                    }
//            );
            DataAccess dataAccess= DataAccessFactory.getInstance(connection);
//            access.setConnection (connection);
            return dataAccess;
        }
        return super.getDataAccessInstance(connection);
    }

    /**
     * 查询序列的下一个值
     * @param scequenceName 序列名称
     * @return
     */
    public long queryForNextVal(String scequenceName){
        String sql = "SELECT " + scequenceName + ".NEXTVAL FROM DUAL";
        return getDataAccess().queryForLong(sql);
    }

    /**
     * 查询序列的下一个值
     * @param scequenceName 序列名称
     * @param configName 数据源名称
     * @return
     */
    public int queryForNextVal(String scequenceName, String configName){
        String sql = "SELECT " + scequenceName + ".NEXTVAL FROM DUAL";
        return getDataAccess(configName).queryForInt(sql);
    }

    public void setWriteLog(boolean writeLog){
        isWriteLog = writeLog;
    }

    /**
     *
     * @param sql
     * @param CodeBeans
     * @param params
     * @return
     */
    public List<Map<String, Object>> queryForListByCodeBean(String sql, CodeBean[] CodeBeans, Object... params){
        List<Map<String, Object>> list = getDataAccess().queryForList(sql, new CodeMapper(CodeBeans), params);
        return list;
    }
    
	/*	
	 *格式化存储过程字符串
	 */
	public String convertStore(String storeName,int paramNum) {
		String preStoreString = "CALL ";
		String sufStoreString = "";
		String storeString = "";
		for(int i=0;i<paramNum;i++){
			sufStoreString = sufStoreString + ",?";
		}
		if (paramNum>0){
			sufStoreString = sufStoreString.substring(1);
		}
		storeString = preStoreString + storeName + "(" + sufStoreString + ")";
		return storeString;
	}
	
  public List<Map<String, Object>> rsToMaps(ResultSet rs) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			while (rs.next()) {
				result.add(rsToMap(rs, getRsCloumns(rs)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	public String[] getRsCloumns(ResultSet rs) throws SQLException {
		ResultSetMetaData rsm = rs.getMetaData();
		String[] columns = new String[rsm.getColumnCount()];
		for (int i = 0; i < columns.length; i++) {
			columns[i] = rsm.getColumnLabel(i + 1);
		}
		return columns;
	}
	
	public  Map<String, Object> rsToMap(ResultSet rs, String[] keys) {
		Map<String, Object> temp = new HashMap<String, Object>();
		for (int i = 0; i < keys.length; i++) {
			try {
				temp.put(keys[i], replaceNull(rs.getObject(keys[i])));
			} catch (SQLException e) {
				continue;
			}
		}
		return temp;
	}
   
    public Object replaceNull(Object value)
    {
    	  return null==value? "&nbsp;&nbsp;" :value;
    }
    
}
