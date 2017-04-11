
/**   
 * @文件名: ClassContextUtil.java
 * @包 tydic.meta.common
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-6 下午04:13:08
 *  
 */
  
package tydic.meta.common;

/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：ClassContextUtil   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-4-6 下午04:13:08   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class ClassContextUtil {
	private ClassContextUtil() {
	}

	private static ClassContextUtil classContext;
	static {
		classContext = new ClassContextUtil();
	}

	public synchronized static ClassContextUtil getInstance() {
		if (classContext == null) {
			classContext = new ClassContextUtil();
		}
		return classContext;
	}

	public String getWebAppRootPath() {
		String result = ClassContextUtil.class.getResource("ClassContextUtil.class").toString();
		int index = result.indexOf("WEB-INF");
		if (index == -1) {
			index = result.indexOf("bin");
		}
		result = result.substring(0, index);
		if (result.startsWith("zip")) {
			result = result.substring(4);
		} else if (result.startsWith("file")) {
												
			result = result.substring(6);
		} else if (result.startsWith("jar")) { 
												
			result = result.substring(10);
		}
		if (result.endsWith("/"))
			result = result.substring(0, result.length() - 1);
		result = result.replace("%20", " ");
		String osname = System.getProperty("os.name");
		if (osname.toLowerCase().startsWith("lin")) {
			result = "/" + result;
		}
		return result;
	}
}
