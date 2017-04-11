package tydic.meta.web.fileUpload;

import org.apache.commons.fileupload.FileItem;
import javax.servlet.http.HttpServletRequest;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 张伟
 * @description 文件上传接口，此接口和FileUploadServlet联合使用，用于处理文件上传之后的操作。 <br>
 * @date 2012-03-20
 */
public interface IFileUpload{
    /**
     * 文件上传需要实现的方法
     * @param request
     * @param fileItem
     * @return 返回一个文件上传的重定向URL
     */
    public String upload(HttpServletRequest request,FileItem fileItem);

}
