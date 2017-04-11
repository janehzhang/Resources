/**
 * 文件名：DbUtil.java
 * 版本信息：Version 1.0
 * 日期：2013-5-20
 * Copyright tydic.com.cn Corporation 2013 
 * 版权所有
 */
package tydic.meta.common.yhd.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 类描述：
 * 
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-5-20 下午04:02:02
 */
public class DbUtil {

	private static Connection connection = null;

	private static DbUtil instance = null;

	private DbUtil() {

	} // 防止在外部实例化
	public synchronized static DbUtil getInstance() {
		if (instance == null) {
			instance = new DbUtil();
		}
		return instance;
	}

   public synchronized static Connection getConnection() {
	       try {
	            if (null == connection) {
	            	return DriverManager.getConnection("proxool.config1");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } 
	        return connection;
  }
	
  public synchronized static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
}
