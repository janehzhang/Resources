package tydic.meta.web.fileUpload;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import tydic.frame.common.Log;
import tydic.frame.common.utils.StringUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 文件上传类Servlet，此Servlet做了一些公共的文件处理 <br>
 * @date 2012-03-20
 */
public class FileUploadServlet extends HttpServlet{
    /**
     * 实现Servlet的doPost方法，此方法根据req的中参数"fileUploadCalss"然后实例化IFileUpload具体的类，如果不存在此参数或者
     * 实力化失败，文件上传出现异常。
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        //获取要实例化的类
        String fileUploadCalss=req.getParameter("fileUploadCalss");
        String errorMessage="";
        if(StringUtils.isEmpty(fileUploadCalss)){
            errorMessage="未指明文件上传实现类，即未找到fileUploadCalss参数,上传失败！";
        }
        //实例化文件上传类
        IFileUpload fileUploadObj=null;
        try{
            Class fileUpload=Class.forName(fileUploadCalss);
            fileUploadObj=(IFileUpload)fileUpload.newInstance();
        } catch(ClassNotFoundException e){
            errorMessage="实例化文件上传类失败！";
            Log.error(null,e);
        } catch(InstantiationException e){
            errorMessage="实例化文件上传类失败！";
            Log.error(null, e);
        } catch(IllegalAccessException e){
            errorMessage="实例化文件上传类失败！";
            Log.error(null, e);
        }
        //进行异常判断
        if(StringUtils.isEmpty(errorMessage)){
            DiskFileUpload fu = new DiskFileUpload();
            try{
                List<FileItem> fileItems = fu.parseRequest(req);
                String url="";
                for(FileItem fileItem:fileItems){
                    if(StringUtils.isNotEmpty(fileItem.getName())
                       &&!fileItem.getName().equals("null")){
                        url= fileUploadObj.upload(req,fileItem);
                    }
                }
                if(!StringUtils.isEmpty(url)){
                    req.getRequestDispatcher(url).forward(req, resp);
                }
                return;
            } catch(FileUploadException e){
                errorMessage="解析文件类失败！";
                Log.error(null, e);
            }
        }
        if(!StringUtils.isEmpty(errorMessage)){
            req.setAttribute("errorMessage",errorMessage);
            req.getRequestDispatcher("/meta/error/fileUploadError.jsp").forward(req, resp);
        }
    }
}
