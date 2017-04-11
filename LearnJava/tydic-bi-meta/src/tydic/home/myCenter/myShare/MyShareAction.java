package tydic.home.myCenter.myShare;

import tydic.frame.common.Log;
import tydic.meta.common.Page;
import tydic.meta.web.session.SessionManager;

import java.util.List;
import java.util.Map;

/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 * @author 刘斌
 * @description 报表管理 —— 个人中心 —— 共享给我的报表 <br>
 * @date 2012-4-17
 */
public class MyShareAction {

    private MyShareDAO myShareDAO;

    /**
     * 查询别人共享给我的报表
     * @param queryData
     * @param page
     * @return
     */
    public List<Map<String, Object>> queryMyShareReport(Map<String, Object> queryData, Page page){
        queryData.put("userId", SessionManager.getCurrentUserID());
        if(page == null){
            page=new Page(0,20);
        }
        return myShareDAO.queryMyShareReport(queryData, page);
    }

    /**
     * 删除一条别人共享给我的报表
     * @param shareListId
     * @return
     */
    public boolean deleteOneShare(int shareListId){
        try{
            myShareDAO.deleteOneShare(shareListId);
            return true;
        }catch (Exception e){
            Log.error("",e);
            return false;
        }
    }

    public void setMyShareDAO(MyShareDAO myShareDAO) {
        this.myShareDAO = myShareDAO;
    }
}
