package proxy;

import java.lang.reflect.Proxy;

import javax.management.Query;

public class Client {

	public static void main(String[] args) {
		
		IDBQuery jdkProxy = (IDBQuery) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), 
				new Class[]{IDBQuery.class}, new DBQueryHandler());
		
		jdkProxy.request();
	}
}
