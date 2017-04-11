package tydic.home.myCenter.myCreate;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.Page;
import tydic.meta.module.report.MetaRptShareListDAO;
import tydic.meta.web.session.SessionManager;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 刘斌
 * @description 我创建的报表Action
 * @date 2012-04-11
 */
public class MyCreateAction {

    private MyCreateDAO myCreateDAO;

    private MetaRptShareListDAO metaRptShareListDAO;

    /**
     * 取得我创建的报表列表
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryMyCreateReport(Map<String, Object> queryData, Page page){
        queryData.put("userId", SessionManager.getCurrentUserID());
        if(page == null){
            page=new Page(0,20);
        }
        return myCreateDAO.queryMyCreateReport(queryData, page);
    }

    /**
     * 根据报表ID取报表信息
     * @param reportId
     * @return
     */
    public Map<String, Object> queryReportByReportId(int reportId){
        Map<String, Object> rtn = myCreateDAO.queryReportByReportId(reportId);
        if(Convert.toInt(rtn.get("RIGHT_LEVEL"),0)!=0){//非公共报表
            List<Map<String, Object>> userStrL = myCreateDAO.queryShareUserStr(reportId);
            String refUserStr = "";
            String userIds = "";
            for(int i = 0; i<userStrL.size(); i++){
                String tmpStr = "<div id=\"#ID\">#VALUE</div>";
                String valStr = Convert.toString(userStrL.get(i).get("USTR")) + "<img src=\"cancel.png\" height=\"16\" width=\"16\" onclick=\"removeOneUser("+Convert.toInt(userStrL.get(i).get("USER_ID"))+")\" alt=\"删除该用户\" title=\"删除该用户\">" + "；";
                tmpStr = tmpStr.replaceAll("#ID","U_"+Convert.toString(userStrL.get(i).get("USER_ID"))).replaceAll("#VALUE", valStr);
                refUserStr = refUserStr + tmpStr;
                userIds = userIds + Convert.toInt(userStrL.get(i).get("USER_ID"));
                userIds = userIds + ",";
            }
            rtn.put("refUserStr", refUserStr);
            rtn.put("userIds", userIds);
        }
        return rtn;
    }

    /**
     * 查询共享用户
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryRefUser(Map<String, Object> queryData, Page page){
        if(page == null){
            page=new Page(0,10);
        }
        if(Convert.toString(queryData.get("zoneId")).equals("")){
            queryData.put("zoneId", SessionManager.getCurrentUser().get("zoneId"));
        }
        queryData.put("mguserId", SessionManager.getCurrentUserID());
        return myCreateDAO.queryRefUser(queryData, page);
    }

    /**
     * 分享用户
     * @param data
     * @return
     */
    public boolean shareUser(Map<String, Object> data){
        String userIds = Convert.toString(data.get("refUserIds"));//用户ID字符串
        int rightLevel = Convert.toInt(data.get("rightLevel"),0);
        int reportId = Convert.toInt(data.get("reportId"));
        try{
            BaseDAO.beginTransaction();
            metaRptShareListDAO.deleteShareByReportId(reportId);
            myCreateDAO.updateRightLevelByReportId(rightLevel, reportId);
            if(!userIds.equals("") && rightLevel==1){
                String[] uidA = userIds.split(",");
                for(int i = 0 ; i < uidA.length; i++){
                    metaRptShareListDAO.insertShare(reportId, Convert.toInt(uidA[i]));
                }
            }
            BaseDAO.commit();
            return true;
        }catch (Exception e){
            Log.error("",e);
            BaseDAO.rollback();
            return false;
        }
    }


    public void setMyCreateDAO(MyCreateDAO myCreateDAO) {
        this.myCreateDAO = myCreateDAO;
    }

    public void setMetaRptShareListDAO(MetaRptShareListDAO metaRptShareListDAO) {
        this.metaRptShareListDAO = metaRptShareListDAO;
    }
}
