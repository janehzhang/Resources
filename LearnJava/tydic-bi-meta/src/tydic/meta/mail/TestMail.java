
/**   
 * @文件名: TestMail.java
 * @包 tydic.meta.mail
 * @描述: 
 * @author wuxl@tydic.com
 * @创建日期 2012-4-5 上午09:59:35
 *  
 */
  
package tydic.meta.mail;

import tydic.frame.SystemVariable;


/**      
 * 项目名称：tydic-bi-meta   
 * 类名称：TestMail   
 * 类描述：   
 * 创建人：wuxl@tydic.com
 * 创建时间：2012-4-5 上午09:59:35   
 * 修改人：
 * 修改时间：
 * 修改备注：   
 * @version      
 */

public class TestMail {

	/** 
	 * @Title: main 
	 * @Description: 
	 * @param @param args    
	 * @return void   
	 * @throws 
	 */

	public static void main(String[] args) {
		//信息封装
		EmailData data = new EmailData();
		String hostName = "tydic.com".toLowerCase();//202.105.139.115//smtp.tydic.com
		data.setSmtpHost(hostName);
		data.setNeedAuth(true);
		String subject = "报表邮件发送功能测试";
		data.setSubject(subject);
		String content = "您好！这是来自吴喜丽的一封测试邮件，给您带来不便敬请谅解！<font color=blue>谢谢！</font> <a href=\"http://www.tydic.com\">天源迪科</a>";
		data.setContent(content);
		String[] recipients = new String[]{"tanht@tydic.com","wuxl@tydic.com"};//{"tanht@tydic.com","wuxl@tydic.com"};//{"wxlcdut@163.com"};
		data.setRecipients(recipients);
		String fromAddr = "wuxl@tydic.com";//"wxlcdut@163.com";//"wuxl@tydic.com";
		data.setFromAddr(fromAddr);
		String fileAddr = "C:/Users/Administrator/Desktop/报表附件/报表附件测试.xlsx";
		data.setFileAddr(fileAddr);
		String fname = "wuxl@tydic.com";//"wxlcdut";
		data.setFname(fname);
		String fpwd = "fbd*:via$99";//"840756131";//fbd*:via$99
		data.setFpwd(fpwd);
		String mailPort = SystemVariable.getString("mail.port", "25");
		data.setMailPort(mailPort);
		String[] cc = new String[]{"zouyg@tydic.com","fuqiang@tydic.com"};//{"280049885@qq.com"};//{"zouyg@tydic.com"};
		data.setCc(cc);
		//邮件发送
		SendMail s = new SendMail();
		s.setSmtpHost(data.getSmtpHost());
		s.setNeedAuth(data.isNeedAuth());
		s.setPort(data.getMailPort());
		if(s.createMimeMessage(data.getFname(), data.getFpwd()) == false)
			return;
		if(s.setSubject(data.getSubject()) == false)
			return;
		s.setDate();
		if(s.setBody(data.getContent(), null) == false)
			return;
		if(s.setTo(data.getRecipients()) == false)
			return;
		s.setCC(data.getCc());
		if(s.setFrom(data.getFromAddr()) == false)
			return;
		s.addFileAffix(data.getFileAddr());
		if(s.sendout() == false)
			return;
	}
}
