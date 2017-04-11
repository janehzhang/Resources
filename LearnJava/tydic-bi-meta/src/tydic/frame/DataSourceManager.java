package tydic.frame;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import tydic.frame.common.Log;
import tydic.frame.jdbc.JdbcException;
import tydic.frame.jdbc.TestDB;
import tydic.frame.jndi.ILookupHandler;
import tydic.frame.jndi.IServer;

public class DataSourceManager
{
  private static final Map<String, DataSource> dataSourceMap = new HashMap();

  private static final List<String> nameList = new ArrayList();

  private static Hashtable<Long, Hashtable<String, Connection>> usingConnections = new Hashtable();

  private static boolean debug = false;

  private static TestDB testDB = null;

  public static final String JNDI_NAMESPACE = SystemVariable.getString("jdbc_namespace", "jdbc");
  
  private static boolean init = false;

  public static void init()
  {
    try
    {
      IServer server = (IServer)Class.forName(SystemVariable.getString("server.jndi.class")).newInstance();
      server.lookup(JNDI_NAMESPACE, new ILookupHandler()
      {
        public void handle(String name, DataSource dataSource) {
          System.out.println("-------------" + name + ":" + dataSource);
          DataSourceManager.nameList.add(name);
          DataSourceManager.dataSourceMap.put(name, dataSource);
        } } );
    } catch (Exception e) {
      Log.error("初始化DataSourceManager失败", e);
    }
  }

  public static Connection getConnection(String datasource)
  {
    long threadId = Thread.currentThread().getId();
    Connection connection = null;
    try {
      if ((usingConnections.containsKey(Long.valueOf(threadId))) && (((Hashtable)usingConnections.get(Long.valueOf(threadId))).containsKey(datasource))) {
        connection = (Connection)((Hashtable)usingConnections.get(Long.valueOf(threadId))).get(datasource);
      }
      else if ((debug) && (usingConnections.containsKey(Long.valueOf(threadId)))) {
        Class.forName(testDB.getClassName());
        connection = DriverManager.getConnection(testDB.getUrl(), testDB.getUser(), testDB.getPasswd());
        ((Hashtable)usingConnections.get(Long.valueOf(threadId))).put(datasource, connection);
      }
      else if (usingConnections.containsKey(Long.valueOf(threadId))) {
        connection = ((DataSource)dataSourceMap.get(datasource)).getConnection();
        ((Hashtable)usingConnections.get(Long.valueOf(threadId))).put(datasource, connection);
      } else if (debug) {
        usingConnections.put(Long.valueOf(threadId), new Hashtable());
        Class.forName(testDB.getClassName());
        connection = DriverManager.getConnection(testDB.getUrl(), testDB.getUser(), testDB.getPasswd());
        ((Hashtable)usingConnections.get(Long.valueOf(threadId))).put(datasource, connection);
      } else {
        usingConnections.put(Long.valueOf(threadId), new Hashtable());

		
        DataSource ds = (DataSource)dataSourceMap.get(datasource);
        try {
			
		
    if(!init){
        String username = getValue(ds, "username").toString();
        String password = getValue(ds, "password").toString();
        username = DESEncrypt.decrypt(username);
    	password = DESEncrypt.decrypt(password);
    	setValue(ds, "username", username);
    	setValue(ds, "password", password);
    	init = true;
    }
        } catch (Exception e) {
        	Log.error("",e);
		}
    	connection = ds.getConnection();
        ((Hashtable)usingConnections.get(Long.valueOf(threadId))).put(datasource, connection);
      }
    } catch (SQLException e) {
      Log.error("获取数据库连接失败：" + datasource, e);
      throw new JdbcException(e);
    } catch (ClassNotFoundException e) {
      throw new JdbcException(e);
    } 
    return connection;
  }

  public static Connection getConnection(String user, String passwd, String url, String className)
  {
	user = DESEncrypt.decrypt(user);
	passwd = DESEncrypt.decrypt(passwd);
    long threadId = Thread.currentThread().getId();
    String key = user + "/" + passwd + "@" + url + "$" + className;
    Connection connection = null;
    try {
      if ((usingConnections.containsKey(Long.valueOf(threadId))) && (((Hashtable)usingConnections.get(Long.valueOf(threadId))).containsKey(key))) {
        connection = (Connection)((Hashtable)usingConnections.get(Long.valueOf(threadId))).get(key);
      } else if (debug) {
        connection = getConnection(testDB.getUser(), testDB.getPasswd(), testDB.getUrl(), testDB.getClassName());
      } else if (usingConnections.containsKey(Long.valueOf(threadId))) {
        Class.forName(className);
        connection = DriverManager.getConnection(url, user, passwd);
        ((Hashtable)usingConnections.get(Long.valueOf(threadId))).put(key, connection);
      } else {
        usingConnections.put(Long.valueOf(threadId), new Hashtable());
        Class.forName(className);
        connection = DriverManager.getConnection(url, user, passwd);
        ((Hashtable)usingConnections.get(Long.valueOf(threadId))).put(key, connection);
      }
    }
    catch (Exception e) {
      Log.error("获取数据库连接失败：" + className, e);
      throw new JdbcException(e);
    }
    return connection;
  }

  public static Connection[] getUsedConnection()
  {
    long threadId = Thread.currentThread().getId();
    Connection[] connections = new Connection[0];
    if (usingConnections.containsKey(Long.valueOf(threadId))) {
      connections = (Connection[])((Hashtable)usingConnections.get(Long.valueOf(threadId))).values().toArray(connections);
    }
    return connections;
  }

  public static void pushUsedConnection(String key, Connection connection)
  {
    long threadId = Thread.currentThread().getId();

    if (!usingConnections.containsKey(Long.valueOf(threadId))) {
      usingConnections.put(Long.valueOf(threadId), new Hashtable());
    }
    if (!((Hashtable)usingConnections.get(Long.valueOf(threadId))).containsKey(key))
      ((Hashtable)usingConnections.get(Long.valueOf(threadId))).put(key, connection);
  }

  public static void destroy()
  {
    long threadId = Thread.currentThread().getId();
    if (usingConnections.containsKey(Long.valueOf(threadId))) {
      Set entrySet = ((Hashtable)usingConnections.get(Long.valueOf(threadId))).entrySet();
      for (Iterator iterator = entrySet.iterator(); iterator.hasNext(); ) {
        Map.Entry entry = (Map.Entry)iterator.next();
        Connection conn = (Connection)entry.getValue();
        try {
          if (conn != null)
            conn.close();
        }
        catch (SQLException e) {
          Log.error("数据库连接关闭异常：" + conn, e);
          throw new JdbcException(e);
        }
      }
      usingConnections.remove(Long.valueOf(threadId));
      BaseDAO.IS_TRANSACTION_MAP.remove(Long.valueOf(threadId));
    }
  }

  public static void destroy(List<Connection> conns)
  {
    long threadId = Thread.currentThread().getId();
    if (usingConnections.containsKey(Long.valueOf(threadId))) {
      Set entrySet = ((Hashtable)usingConnections.get(Long.valueOf(threadId))).entrySet();
      for (Iterator iterator = entrySet.iterator(); iterator.hasNext(); ) {
        Map.Entry entry = (Map.Entry)iterator.next();
        Connection conn = (Connection)entry.getValue();
        try {
          if ((conns.contains(conn)) && (conn != null)) {
            conn.close();
            iterator.remove();
          }
        } catch (SQLException e) {
          Log.error("数据库连接关闭异常：" + conn, e);
          throw new JdbcException(e);
        }
      }

      if (entrySet.isEmpty()) {
        usingConnections.remove(Long.valueOf(threadId));
        BaseDAO.IS_TRANSACTION_MAP.remove(Long.valueOf(threadId));
      }
    }
  }

  public static Map<String, DataSource> getAllDataSource()
  {
    return dataSourceMap;
  }

  public static void addDataSource(String key, DataSource dataSource)
  {
    dataSourceMap.put(key, dataSource);
  }

  public static void removeDataSource(String key)
  {
    dataSourceMap.remove(key);
  }

  public static boolean containKey(String key)
  {
    return dataSourceMap.containsKey(key);
  }

  public static void setDebug(TestDB testDB)
  {
    testDB = testDB;
    debug = true;
  }
	public static Object getValue(Object instance, String fieldName) throws IllegalAccessException,
	NoSuchFieldException {

	Field field = getField(instance.getClass(), fieldName);
	// 参数值为true，禁用访问控制检查
	field.setAccessible(true);
	return field.get(instance);
	}
	
	public static void setValue(Object instance, String fieldName,String value) throws IllegalAccessException,
	NoSuchFieldException {
	
		Field field = getField(instance.getClass(), fieldName);
		// 参数值为true，禁用访问控制检查
		field.setAccessible(true);
		field.set(instance, value);
	}

	public static Field getField(Class thisClass, String fieldName) throws NoSuchFieldException {
	
		if (fieldName == null) {
			throw new NoSuchFieldException("Error field !");
		}
		
		Field field = thisClass.getDeclaredField(fieldName);
		return field;
	}
}