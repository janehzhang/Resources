package tydic.meta.module.tbl.imptab;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;

import tydic.meta.web.fileUpload.IFileUpload;

/**
 * 上传导入Pdm中的表类
 * @author 李国民
 *
 */
public class PdmUploadImpl implements IFileUpload{

	/**
	 * 上传解析Pdm文件
	 */
	public String upload(HttpServletRequest request, FileItem fileItem) {
		try {
			List<Map<String,Object>> tablesList = PdmParser.parse(fileItem.getInputStream());
			HttpSession session = request.getSession();
			session.setAttribute("importTablesList", tablesList);
			request.setAttribute("isUpload", "OK");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("isUpload", "NO");
		}
		return "meta/module/tbl/import/modelUpload.jsp";
	}
	
}
