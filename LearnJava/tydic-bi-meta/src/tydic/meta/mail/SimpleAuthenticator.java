/**   
 * @文件名: SimpleAuthenticator.java
 * @包 tydic.meta.mail
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-5 下午02:29:45
 *  
 */

package tydic.meta.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


/**
 * 项目名称：tydic-bi-meta 类名称：SimpleAuthenticator 类描述： 创建人：wuxl@tydic.com
 * 创建时间：2012-4-5 下午02:29:45 修改人： 修改时间： 修改备注：
 * 
 * @version
 */

public class SimpleAuthenticator extends Authenticator {

	private String user;
	private String pwd;

	public SimpleAuthenticator(String user, String pwd) {
		this.user = user;
		this.pwd = pwd;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, pwd);
	}
}
