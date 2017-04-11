/*
 * Created on 2005-9-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tydic.meta.common.yhd.sql;

/**
 * @author 李春刚
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;




public class ServicePool {


	private static List cache = new ArrayList();
	private static Map datasource_map = new HashMap();

	/**	
	 *初始化配置文件
	 */
	public static void init(File configfile) 
				throws Exception{
		try{
		    SAXReader reader = new SAXReader();
		    Document doc = reader.read(configfile);
			Element root = doc.getRootElement();
		    for(Iterator it = root.elementIterator(); it.hasNext();){
		    	Element config = (Element) it.next();
			    String jndi = config.element("Jndi").getText();
				String connname = config.attributeValue("id");
				try{
					InitialContext initialContext = new InitialContext();
					DataSource dataSource = (DataSource)initialContext.lookup(jndi);
					datasource_map.put(connname,dataSource);
				}catch(Exception e){
					 e.printStackTrace();
				}
		    }	
		}catch(Exception me){
			throw new Exception(me.getMessage());
		}
	}

	/*
	 * 获取DB2数据库的连接池
	 */
	public static Connection getDB2Connection() 
					throws Exception{
		Connection conn = null;
		try{
		    conn = getConnection("DB2Conn");
			conn = getConnection();
		}catch(Exception ce){
			throw new Exception(ce.getMessage());
		}
		return conn;
	}
	
	
	/**	
	 *通过连接名称初始化连接池
	 */	
	private static Connection getConnection(String connname) 
					throws Exception{
		
		//if(datasource_map.containsKey(connname)){
			//DataSource dataSource = (DataSource)datasource_map.get(connname);
			Connection connection;	
			try{
				//connection = dataSource.getConnection();
				connection = getConnection();
			}catch(Exception e){
				System.out.println(e.getMessage());
				throw new Exception(e.getMessage());	
			}		
			return connection;
		//}else{
		//	throw new CreditException("Get"+connname+" connection error");
		//}
		
	}
	/**	
	 *初始化连接池
	 */	
	private static Connection getConnection() 
					throws  Exception{
		
		String driver = "com.ibm.db2.jcc.DB2Driver";
        String url = "jdbc:db2://168.33.100.200:50000/credit";
        String userName = "administrator";
        String passWord = "123";
        
        Connection conn = null;
        try{
            Class.forName(driver).newInstance();

            conn = DriverManager.getConnection(url, userName, passWord);
            return conn;
        } catch (Exception e) {
            System.out.println("error:" + e.getMessage() );
            System.out.println(e.toString());
            throw new  Exception(e.getMessage());	
        }
    }


}
