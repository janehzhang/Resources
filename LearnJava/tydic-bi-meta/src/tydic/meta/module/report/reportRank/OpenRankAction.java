package tydic.meta.module.report.reportRank;

import tydic.frame.SystemVariable;
import tydic.meta.common.Page;
import tydic.meta.web.session.SessionManager;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 刘斌
 * @description 报表打开排名Action
 * @date 2012-04-11
 */
public class OpenRankAction {
    private ReportRankDAO reportRankDAO;

    /**
     * 报表打开排名
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryOpenRank(Map<String, Object> queryData, Page page){
        if(page==null){//如果没有page，为第一页。
            page=new Page(0,20);
        }
        String hideStations = "";
        if(SessionManager.isCustomer()){//如果是客户
            //读取config 配置参数，获取不显示的岗位
            hideStations= SystemVariable.getString("hidden.stations", "");
        }
        return reportRankDAO.queryOpenRank(queryData, hideStations, page);
    }

    /**
     * 根据报表ID查看报表打开记录
     * @param reportId
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryForOpenDetail(int reportId, Page page){
        if(page==null){//如果没有page，为第一页。
            page=new Page(0,20);
        }
        String hideStations = "";
        if(SessionManager.isCustomer()){//如果是客户
            //读取config 配置参数，获取不显示的岗位
            hideStations= SystemVariable.getString("hidden.stations", "");
        }
        return reportRankDAO.queryForOpenDetail(reportId, hideStations, page);
    }


    public void setReportRankDAO(ReportRankDAO reportRankDAO) {
        this.reportRankDAO = reportRankDAO;
    }
}
