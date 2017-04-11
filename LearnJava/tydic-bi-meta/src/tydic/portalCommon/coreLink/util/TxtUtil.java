/**
 * 文件名：TxtUtil.java
 * 版本信息：Version 1.0
 * 日期：2013-6-20
 * Copyright tydic.com.cn Corporation 2013 
 * 版权所有
 */
package tydic.portalCommon.coreLink.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

/**
 * 类描述：
 * 
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-6-20 上午11:07:31
 */
public class TxtUtil {

	/**
	 * 将txt文件发送到客户端
	 * 
	 * @param response
	 * @param filePath
	 */
	public static void  returnTxt(HttpServletResponse response, String filePath) {
		File t_file = new java.io.File(filePath);
		OutputStream os = null;
		InputStream in = null;
		try {
			in = new FileInputStream(t_file);
			if (in != null) {
				String fs = t_file.getName();
				int l = (int) t_file.length();
				response.reset();
				response.setContentType("application/x-octetstream;charset=ISO8859-1");
				response.setHeader("Content-Disposition",
						"attachment;filename=\""
								+ new String(fs.getBytes(), "ISO8859-1") + "\"");
				byte[] b = new byte[l];
				int len = 0;

				while ((len = in.read(b)) > 0) {
					os = response.getOutputStream();
					os.write(b, 0, len);
					os.flush();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e1) {
					os = null;
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e2) {
					in = null;
				}

			}
		}
	}
	
	

 public static void export(String path, HttpServletResponse response,
			String filename) {
		response.setContentType("application/x-download");// 设置为下载application/x-download
		response.setCharacterEncoding("gbk");

		try {
			response.addHeader("Content-Disposition", "attachment;filename="
					+ filename);
			OutputStream out = response.getOutputStream();
			InputStream in = new FileInputStream(path);
			byte[] b = new byte[1024];
			int i = 0;

			while ((i = in.read(b)) > 0) {
				out.write(b, 0, i);
			}
			out.flush();
			in.close();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}	
}
