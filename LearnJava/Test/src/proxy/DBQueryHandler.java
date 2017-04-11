package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DBQueryHandler implements InvocationHandler{

	IDBQuery real = null;
	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if(real == null){
			System.out.println("let me create you");
			real = new DBQuery();
		}
		
		return real.request();
	}

	
}
