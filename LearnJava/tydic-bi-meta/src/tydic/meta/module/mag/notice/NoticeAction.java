package tydic.meta.module.mag.notice;

import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.meta.common.OprResult;
import tydic.meta.common.Page;
import tydic.meta.web.session.SessionManager;

/**
 * Copyrights @ 2012,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author: 程钰
 * @description  公告查询业务类
 * @date: 12-3-21
 * @time: 下午2:49
 */

public class NoticeAction {
    //定义事物 DAO
    private NoticeDAO noticeDAO;
    // set 方法
    public void setNoticeDAO(NoticeDAO noticeDAO) {
        this.noticeDAO = noticeDAO;
    }

    /**
     * 按条件查询出对应的公告
     * @param queryData 查询条件
     * @param page  分页条件
     * @return    查询结果
     */
    public List<Map<String, Object>> queryNotice(Map<String,Object> queryData, Page page){
		 if (page == null) {// 如果没有page，为第一页。
			page = new Page(0, 20);
		 }
	     int userId = SessionManager.getCurrentUserID();
	     queryData.put("createId", userId);
         return noticeDAO.queryNotice(queryData,page);
    }

   public List<Map<String, Object>> queryNotice1(Map<String,Object> queryData, Page page){
		 if (page == null) {// 如果没有page，为第一页。
			page = new Page(0, 20);
		 }
        return noticeDAO.queryNotice1(queryData,page);
   }
    
    /**
     * 新增一条公告
     * @param data
     * @return
     */
    public boolean insertNotice(Map<String,Object> data){
    	data.put("noticeState", "0");
        return noticeDAO.insertNotice(data);
    }
    /**
     * 删除系统公告
     * @param noticeIdStr
     * @return
     */
    public OprResult<?,?>[] deleteNotice(String noticeIdStr){
        //前台传入的ID是字符串形式以逗号隔开
		int noticeId[] = new int[noticeIdStr.split(",").length];
        for(int i = 0; i < noticeId.length; i ++){
			noticeId[i] = Integer.parseInt(noticeIdStr.split(",")[i]);
		}
        OprResult<?,?> result[] = new OprResult[noticeId.length];
        try{
			BaseDAO.beginTransaction();
            noticeDAO.deleteNoticeByNoticeIds(noticeId);
            BaseDAO.commit();
			for(int i = 0; i < result.length; i ++){
				result[i] = new OprResult<Integer,Object>(noticeId[i], null, OprResult.OprResultType.delete);
			}
			return result;
		} catch (Exception e) {
			BaseDAO.rollback();
			Log.error("删除系统信息失败", e);
			for(int i = 0; i < result.length; i ++){
				result[i] = new OprResult<Integer,Object>(noticeId[i], null, OprResult.OprResultType.error);
			}
            return result;
		}
    }
    /**
     * 修改公告
     * @param data 修改公告数据
     * @return
     */
    public boolean updateNotice(Map<String,Object> data){
    	data.put("noticeState", "0");
        return noticeDAO.updateNotice(data);
    }

    
    /**
     * 审核公告
     * @param data 公告数据
     * @return
     */
    public boolean spNotice(Map<String,Object> data){
		 int userId = SessionManager.getCurrentUserID();
	     String userName = SessionManager.getCurrentUser().get("userNamecn").toString();
	     data.put("createId", userId);
	     data.put("createName", userName);	
        return noticeDAO.spNotice(data);
    }
    
    
    /**
     * 修改公告状态
     * @param noticeIds  公告状态ID
     * @param noticeState   公告修改后的状态
     * @return  修改结果
     */
    public boolean updateNoticeCtrlr(String noticeId, String noticeState){
        return  noticeDAO.noticeStateCtrlr(noticeId,Integer.parseInt(noticeState));
    }
}
