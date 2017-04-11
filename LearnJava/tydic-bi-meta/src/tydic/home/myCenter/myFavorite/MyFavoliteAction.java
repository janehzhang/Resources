package tydic.home.myCenter.myFavorite;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.meta.common.Page;
import tydic.meta.web.session.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author 刘斌
 * @description 我收藏的报表Action
 * @date 2012-04-11
 */
public class MyFavoliteAction {

    private MyFavoliteDAO myFavoliteDAO;
    /**
     * 查询我收藏的报表
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryMyFavolite(Map<String, Object> queryData, Page page){
        queryData.put("userId", SessionManager.getCurrentUserID());
        if(page == null){
            page=new Page(0,20);
        }
        return myFavoliteDAO.queryMyFavolite(queryData, page);
    }

    /**
     * 执行订阅
     * @param data
     * @return
     */
    public Map<String, Object> doPush(Map<String, Object> data){
        try{
            BaseDAO.beginTransaction();
            int sendSequnce = Convert.toInt(data.get("sendSequnce"), 0);
            String sendTimeAdd = "";
            if(sendSequnce == 5){//每天发送一次
                sendTimeAdd = "1";
            }
            if(sendSequnce == 4){//每周
                sendTimeAdd = "7";
            }
            if(sendSequnce == 3){//每月
                sendTimeAdd = "30";
            }
            if(sendSequnce == 2){//每半年
                sendTimeAdd = "180";
            }
            if(sendSequnce == 1){//每年
                sendTimeAdd = "365";
            }
            data.put("sendTimeAdd", sendTimeAdd);
            data.put("userId", SessionManager.getCurrentUserID());
            myFavoliteDAO.insertPush(data);
            //记录用户对报表的操作日志
            //数据结构：{userId:,useZoneId:,useDeptId:,useStationId:,reportId:,operateType}
            Map<String, Object> logData = new HashMap<String, Object>();
            logData.put("userId", SessionManager.getCurrentUserID());
            logData.put("useZoneId", SessionManager.getCurrentUser().get("zoneId"));
            logData.put("useDeptId", SessionManager.getCurrentUser().get("deptId"));
            logData.put("useStationId", SessionManager.getCurrentUser().get("stationId"));
            logData.put("reportId",data.get("reportId"));
            logData.put("operateType",21);//21为执行订阅
            myFavoliteDAO.insertMetaRptUseLog(logData);
            BaseDAO.commit();
            return myFavoliteDAO.queryOneFavolite(SessionManager.getCurrentUserID(), Convert.toInt(data.get("reportId"),0));
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("", e);
            return null;
        }
    }

    /**
     * 取消订阅（删除对应订阅信息）
     * @param pushConfigId
     * @return
     */
    public Map<String, Object> cancelPush(int pushConfigId, int reportId){
        try{
            BaseDAO.beginTransaction();
            Map<String, Object> logData = new HashMap<String, Object>();
            logData.put("userId", SessionManager.getCurrentUserID());
            logData.put("useZoneId", SessionManager.getCurrentUser().get("zoneId"));
            logData.put("useDeptId", SessionManager.getCurrentUser().get("deptId"));
            logData.put("useStationId", SessionManager.getCurrentUser().get("stationId"));
            logData.put("reportId",reportId);
            logData.put("operateType",20);//20为取消订阅
            myFavoliteDAO.insertMetaRptUseLog(logData);
            myFavoliteDAO.deletePushConfig(pushConfigId);
            BaseDAO.commit();
            return myFavoliteDAO.queryOneFavolite(SessionManager.getCurrentUserID(), reportId);
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("",e);
            return null;
        }
    }

    /**
     * 删除收藏(同时删除订阅信息)
     * @param reportFavoriteId 收藏信息ID
     * @param pushConfigId 订阅信息ID
     * @return
     */
    public boolean deleteFav(int reportFavoriteId, int pushConfigId, int reportId){
        try{
            BaseDAO.beginTransaction();
            if(pushConfigId != -1){
                myFavoliteDAO.deletePushConfig(pushConfigId);
                Map<String, Object> logData = new HashMap<String, Object>();
                logData.put("userId", SessionManager.getCurrentUserID());
                logData.put("useZoneId", SessionManager.getCurrentUser().get("zoneId"));
                logData.put("useDeptId", SessionManager.getCurrentUser().get("deptId"));
                logData.put("useStationId", SessionManager.getCurrentUser().get("stationId"));
                logData.put("reportId",reportId);
                logData.put("operateType",20);//20为取消订阅
                myFavoliteDAO.insertMetaRptUseLog(logData);
            }
            Map<String, Object> logData = new HashMap<String, Object>();
            logData.put("userId", SessionManager.getCurrentUserID());
            logData.put("useZoneId", SessionManager.getCurrentUser().get("zoneId"));
            logData.put("useDeptId", SessionManager.getCurrentUser().get("deptId"));
            logData.put("useStationId", SessionManager.getCurrentUser().get("stationId"));
            logData.put("reportId",reportId);
            logData.put("operateType",10);//10为取消收藏
            myFavoliteDAO.insertMetaRptUseLog(logData);
            myFavoliteDAO.deleteUserFav(reportFavoriteId);
            BaseDAO.commit();
            return true;
        }catch (Exception e){
            Log.error("",e);
            BaseDAO.rollback();
            return false;
        }
    }


    public void setMyFavoliteDAO(MyFavoliteDAO myFavoliteDAO) {
        this.myFavoliteDAO = myFavoliteDAO;
    }
}
