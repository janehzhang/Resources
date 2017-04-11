
/**   
 * @文件名: EmailData.java
 * @包 tydic.meta.mail
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-1 下午05:25:54
 *  
 */
  
package tydic.meta.mail;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：EmailData   
 * 类描述：   
 * 创建人：wuxl@tydic.com 
 * 创建时间：2012-4-1 下午05:25:54   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class EmailData {
	String fromName;  //发件人 名称
	String fromAddr;//邮箱地址
	String fname;//发件人邮箱用户名
	String fpwd;//发件人邮箱密码
    String[] recipients;  //收件人,可以多个 
    String[] cc;//抄送人，可以多个
    String[] bcc;//暗送人，可以多个
    String subject;  //邮件主题 
    String content;  //邮件内容 
    String contentType;  //邮件内容格式(文本或html) 
    String fileName;  //附件文件名(目前只提供一个附件) 
    String smtpHost;//stmp主机
    boolean needAuth;//身份验证
    String fileAddr;
    String mailPort;
    
	public String getMailPort() {
		return mailPort;
	}
	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}
	public String getFileAddr() {
		return fileAddr;
	}
	public void setFileAddr(String fileAddr) {
		this.fileAddr = fileAddr;
	}
	public boolean isNeedAuth() {
		return needAuth;
	}
	public void setNeedAuth(boolean needAuth) {
		this.needAuth = needAuth;
	}
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getFromAddr() {
		return fromAddr;
	}
	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getFpwd() {
		return fpwd;
	}
	public void setFpwd(String fpwd) {
		this.fpwd = fpwd;
	}
	public String[] getRecipients() {
		return recipients;
	}
	public void setRecipients(String[] recipients) {
		this.recipients = recipients;
	}
	public String[] getCc() {
		return cc;
	}
	public void setCc(String[] cc) {
		this.cc = cc;
	}
	public String[] getBcc() {
		return bcc;
	}
	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
