package tydic.reports.CommonUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.StringUtil;

import tydic.frame.common.Log;
import tydic.frame.common.aop.AopHandler;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.yhd.utils.Pager;

import com.sun.org.apache.commons.beanutils.BeanUtils;

public class DownLoadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Constructor of the object.
	 */
	public DownLoadServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String path="";
		Map<String, Object> paramMap=DownLoadUtil.getParameters(request);//将所有请求参数全部封装到一个map中
		String existFile =  paramMap.get("existFile")==null?"0":paramMap.get("existFile").toString();//指定是否是因存在的文件
		try {
			if(paramMap.get("path")!=null)
			path = paramMap.get("path").toString();
			if("0".equals(existFile))
			path=DownLoadUtil.createFile(paramMap);//使用respon对象返回返回文件IO
			
			DownLoadUtil.responseFile(response, path);//输出文件IO
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
 
	}

}
