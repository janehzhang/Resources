package tydic.meta.module.dim.mapping;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;

import tydic.meta.module.dim.CsvUtil;
import tydic.meta.module.dim.ExcelUtil;
import tydic.meta.web.fileUpload.IFileUpload;
import tydic.meta.web.session.SessionManager;

public class MappUploadImpl implements IFileUpload{
   private MappingUploadService mappService = new MappingUploadService();
   private List<Map<String,Object>> errorList =null;
	public String upload(HttpServletRequest request, FileItem fileItem){
	  HttpSession session = request.getSession();
	  int  sysId = 0;
	  int  dimTableId = 0;
	  int  dimTypeId=0;
	  String msg = null;
	   String sysIdStr = request.getParameter("sysId");
	   String dimTableIdStr = request.getParameter("dimTableId");
	   String dimTypeIdStr = request.getParameter("dimTypeId");
	   String dimTableName = request.getParameter("dimTableName");
	   String dimTablePrefix = request.getParameter("dimTablePrefix");
	   String tableOwner = request.getParameter("tableOwner");
	   List<Map<String,Object>> dataList = null;
	   if(sysIdStr!=null){
		   sysId = Integer.valueOf(sysIdStr);
	   }
	   if(dimTableIdStr!=null){
		   dimTableId = Integer.valueOf(dimTableIdStr);
	   }
	   if(dimTypeIdStr!=null){
		   dimTypeId = Integer.valueOf(dimTypeIdStr);
	   }
	   if(fileItem==null){
		   msg = "请选择文件";
	   }else{
		   String fileName = fileItem.getName();
		   if(fileName==null||!fileName.matches("^.+\\.(?i)((xls)|(xlsx))$")||!(fileName.substring(fileName.lastIndexOf(".")+1)).equalsIgnoreCase("csv")){ //判断当前的文件是不是execl文件
			   msg= "请选择正确的文件";
		   }
	       try {
			InputStream inputStream = fileItem.getInputStream();
			if(fileName.matches("^.+\\.(?i)((xls)|(xlsx))$")){
				ExcelUtil util = new ExcelUtil();
				dataList = util.readFile(inputStream);
			}
			if(fileName.endsWith(".csv")){
				CsvUtil util = new CsvUtil();
				dataList = util.readFile(inputStream);
			}
			String sessionId = session.getId();
			int userId = SessionManager.getCurrentUserID(sessionId);
			errorList = mappService.insertMappData(dataList, dimTableName, dimTablePrefix,tableOwner,sysId,dimTableId,dimTypeId,userId);
			if(errorList==null||errorList.size()==0){
				msg = "上传成功";
			}
			if(errorList!=null&&errorList.size()!=0){
				msg = "上传未成功的条数"+"<span>"+errorList.size()+"</span>"+"条,详情请点击<a href='#' onClick='testFun(1);return false;'>这里</a>";
        	}
		    } catch (Exception e) {
		    	msg = "上传失败,这也许是您上传的文件中有误";
			e.printStackTrace();
		   }
		    session.setAttribute("errorList",errorList);
		}
	   request.setAttribute("errorMsg", msg);
	   return "meta/module/dim/merge/codeUploadMsg.jsp";
	}
}
