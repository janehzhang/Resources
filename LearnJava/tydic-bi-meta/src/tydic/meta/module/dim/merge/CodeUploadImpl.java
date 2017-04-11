package tydic.meta.module.dim.merge;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;

import tydic.frame.common.Log;
import tydic.meta.module.dim.CsvUtil;
import tydic.meta.module.dim.DimConstant;
import tydic.meta.module.dim.ExcelUtil;
import tydic.meta.module.dim.MetaDimTabModHisDAO;
import tydic.meta.module.tbl.MetaTableColsDAO;
import tydic.meta.web.fileUpload.IFileUpload;
import tydic.meta.web.session.SessionManager;

public class CodeUploadImpl implements IFileUpload {
    //	private  MetaDimTabModHisDAO metaDimTabModHisDAO = new MetaDimTabModHisDAO();
    public String upload(HttpServletRequest request, FileItem fileItem) {
        int dimTableId = 0;
        int dimTypeId = 0;
        HttpSession session = request.getSession();
        List<CodeBean> list = null;
        String dimTableStr = request.getParameter("dimTableId");
        String dimTypeStr = request.getParameter("dimTypeId");
        String dimTableName = request.getParameter("dimTableName");
        String dimTablePrefix = request.getParameter("dimTablePrefix");
        String tableOwner = request.getParameter("tableOwner");
        String errorMsg = null;
        List<Map<String, Object>> dataList = null;
        if (dimTableStr != null && dimTableStr != "") {
            dimTableId = Integer.valueOf(dimTableStr);
        }
        if (dimTypeStr != null && dimTypeStr != "") {
            dimTypeId = Integer.valueOf(dimTypeStr);
        }
        if (fileItem == null) {
            errorMsg = "请选择文件";
        } else {
            String fileName = fileItem.getName();
            if (fileName == null || !fileName.matches("^.+\\.(?i)((xls)|(xlsx))$") || !(fileName.substring(fileName.lastIndexOf(".") + 1)).equalsIgnoreCase("csv")) { //判断当前的文件是不是execl文件
                errorMsg = "当前选择的文件有误,请重新选择execl文件或者csv文件";
            }
            try {
                InputStream inputStream = fileItem.getInputStream();
                if (fileName.matches("^.+\\.(?i)((xls)|(xlsx))$")) {
                    ExcelUtil util = new ExcelUtil();
                    dataList = util.readFile(inputStream);
                }
                if (fileName.endsWith(".csv")) {
                    CsvUtil util = new CsvUtil();
                    dataList = util.readFile(inputStream);
                }
                Map<String, Object> map = dataList.get(0);
                int colCount = map.size();
                MetaTableColsDAO metaTableColsDAO = new MetaTableColsDAO();
                List<Map<String, Object>> dynCols = metaTableColsDAO.queryDimTableDycCols(dimTableId);
                MetaDimTabModHisDAO metaDimTabModHisDAO=new MetaDimTabModHisDAO();
                int dynColCount = metaDimTabModHisDAO.quertDynColCount();
                if (colCount - DimConstant.FIX_COL_COUNT > dynColCount) {
                    errorMsg = "当前文件中的动态字段个数大于审核表中的个数,请联系管理员处理";
                } else {
                    CodeUpLoadService cus = new CodeUpLoadService();
                    String sessionId = session.getId();
                    int userId = SessionManager.getCurrentUserID(sessionId);
                    String batchId = String.valueOf(System.currentTimeMillis());
                    list = cus.insertCodeData(dataList, dimTableId, dimTableName, dimTablePrefix, tableOwner, dimTypeId, dynCols, batchId, userId);
                    if (list != null && list.size() == 0) {
                        errorMsg = "上传成功";
                    }
                    if (list != null && list.size() != 0) {
                        errorMsg = "上传未成功的条数" + "<span>" + list.size() + "</span>" + "条,详情请点击<a href='#' onClick='testFun(0);return false;'>这里</a>";
                    }

                }
            } catch (Exception e) {
                errorMsg = "上传失败,这也许是由于您选择的文件有误";
                e.printStackTrace();
            }

        }
        request.setAttribute("errorMsg", errorMsg);
        session.setAttribute("errorList", list);
        return "meta/module/dim/merge/codeUploadMsg.jsp";
    }
}
