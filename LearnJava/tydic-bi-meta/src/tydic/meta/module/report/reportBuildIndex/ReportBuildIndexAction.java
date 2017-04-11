package tydic.meta.module.report.reportBuildIndex;

import tydic.meta.common.Page;
import tydic.meta.web.session.SessionManager;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 张伟
 * @description 报表创建首页Action
 * @date 2012-04-26
 */
public class ReportBuildIndexAction {
    private ReportBuildIndexDAO reportBuildIndexDAO;

    /**
     * 查询推荐数据
     *
     * @param stationId  部门ID
     * @param showNumber 显示条数
     * @return
     */
    public List<Map<String, Object>> queryRecommendData(Page page) {
        Map<String, Object> user = SessionManager.getCurrentUser();
        if (page == null) {
            page = new Page(0, 2);
        }
        return reportBuildIndexDAO.queryRecommendData(user.get("stationId").toString(), page);
    }


    /**
     * 查询人气最旺数据
     *
     * @param showNumber 显示条数
     * @return
     */
    public List<Map<String, Object>> queryPopularData(Page page) {
        if (page == null) {
            page = new Page(0, 2);
        }
        return reportBuildIndexDAO.queryPopularData(page);
    }

    /**
     * 查询最新数据
     *
     * @param showNumber 显示条数
     * @return
     */
    public List<Map<String, Object>> queryNewestData(Page page) {
        if (page == null) {
            page = new Page(0, 2);
        }
        return reportBuildIndexDAO.queryNewestData(page);
    }

    /**
     * 搜索数据
     *
     * @param showNumber 显示条数
     * @param condition  搜索条件
     * @return
     */
    public List<Map<String, Object>> queryRptModelDataByCondition(Page page, String condition) {
        if (page == null) {
            page = new Page(0, 2);
        }
        return reportBuildIndexDAO.queryRptModelDataByCondition(page, condition);
    }


    /**
     * 根据发布模型ID，查询出字段分类树，包含字段信息
     *
     * @param issueId 发布模型ID
     * @return
     */
    public List<Map<String, Object>> queryFields(String issueId) {
        return reportBuildIndexDAO.queryFields(issueId);
    }


    public List<Map<String, Object>> queryDimName(String colId) {
        return reportBuildIndexDAO.queryDimName(colId);
    }

    public List<Map<String, Object>> queryLevelName(String columnId, String colId) {
        return reportBuildIndexDAO.queryLevelName(columnId, colId);
    }

    public List<Map<String, Object>> queryCodes(String dimLevel, String colId) {
        return reportBuildIndexDAO.queryCodes(dimLevel, colId);
    }

    public List<Map<String, Object>> queryLowLevelNameByLevel(String level, String colId) {
        return reportBuildIndexDAO.queryLowLevelNameByLevel(level, colId);
    }

    public List<Map<String, Object>> queryLowCodesByCode(String codeId, String colId) {
        return reportBuildIndexDAO.queryLowCodesByCode(codeId, colId);
    }

    public List<Map<String, Object>> queryInstTable(Map<?, ?> data) {
        return reportBuildIndexDAO.queryInstTable(data);
    }

    public String querySumValue(String curTd, String issueId, String colInfo) {
        return reportBuildIndexDAO.querySumValue(curTd, issueId, colInfo);
    }

    public List<Map<String, Object>> getCodeByColumnId(String transColumnId, String dimLevels, String dimCodeFilters) {
        return reportBuildIndexDAO.getCodeByColumnId(transColumnId, dimLevels, dimCodeFilters);
    }

    public void setReportBuildIndexDAO(ReportBuildIndexDAO reportBuildIndexDAO) {
        this.reportBuildIndexDAO = reportBuildIndexDAO;
    }
}
