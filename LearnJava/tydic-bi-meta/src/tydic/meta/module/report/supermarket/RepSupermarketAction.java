package tydic.meta.module.report.supermarket;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.DateUtil;
import tydic.meta.common.Page;
import tydic.meta.web.session.SessionManager;


/**
 * 报表超市Action
 * @author 李国民
 * Date：2012-03-13
 */
public class RepSupermarketAction {

	private RepSupermarketDAO repSupermarketDAO;
	
	/**
	 * 得到当前登录人下同岗位人订阅最多的报表（倒序排列）
	 * @return
	 */
	public List<Map<String,Object>> getRepListByPost(){
        Page page=new Page(0,2);	//显示前面2条数据
		Map<String,Object> user = SessionManager.getCurrentUser();
		return repSupermarketDAO.getRepList(user.get("stationId").toString(),page);
	}
	
	/**
	 * 得到订阅最多的报表（倒序排列）
	 * @return
	 */
	public List<Map<String,Object>> getRepListBySub(){
        Page page=new Page(0,2);	//显示前面2条数据
		return repSupermarketDAO.getRepList(null,page);
	}
	
	/**
	 * 得到最新的报表数据
	 * @return
	 */
	public List<Map<String,Object>> getRepListByTime(){
        Page page=new Page(0,2);	//显示前面2条数据
		return repSupermarketDAO.getRepListByTime(page);
	}

	/**
	 * 得到不同的排序数据
	 * @param orderId 排序分类id(1为按推荐报表排序，2为人气最旺报表排序，3为新鲜出炉报表排序)
	 * @param page
	 * @return
	 */
	public List<Map<String,Object>> getRepListByOrderId(int orderId, Page page){
		if(page ==null){
	        page=new Page(0,7);	//按每页7条分页
		}
		if(orderId==1){	//按推荐报表排序
			Map<String,Object> user = SessionManager.getCurrentUser();
			return repSupermarketDAO.getRepList(user.get("stationId").toString(),page);
		}else if(orderId==2){	//按人气最旺排序
			return repSupermarketDAO.getRepList(null,page);
		}else if(orderId==3){	//按新鲜出炉排序
			return repSupermarketDAO.getRepListByTime(page);
		}else{
			return null;
		}
	}

	/**
	 * 通过搜索条件查询报表
	 * @param searchName 搜索条件
	 * @param page
	 * @return
	 */
	public List<Map<String,Object>> getRepListBySearchName(String searchName, Page page){
		if(page ==null){
	        page=new Page(0,7);	//按每页7条分页
		}
		return repSupermarketDAO.getRepListBySearchName(searchName, page);
	}
	
    /**
     * 查询所有报表分类
     * @return
     */
    public List<Map<String,Object>> queryFavoriteGroup(){
        return repSupermarketDAO.queryFavoriteGroup();
    }
    
    /**
     * 添加报表收藏
     * @param data
     * @return 返回 1表示该报表已经存在于收藏中，返回 2表示报表收藏成功，，返回 2表示报表收藏失败
     */
    public int insertFavorite(Map<String,Object> data){
    	int rs =1;
        //当该报表没有被用户收藏时，才收藏报表
    	if(!repSupermarketDAO.isExistFavorite(data)){
        	if(repSupermarketDAO.insertFavorite(data)){
        		rs = 2;
        	}else{
        		rs = 3;
        	}
    	}
    	return rs;
    }
    
    /**
     * 收藏并订阅报表
     * @param data
     * @return
     */
    public int insertFavoriteAndSub(Map<String,Object> data){
    	int rs = 1;
    	try{
    		boolean check  = false;		//判断是否存在收藏开关
    		if(data.get("reportFavoriteId")==null||data.get("reportFavoriteId").equals("")){
    			//如果不存在该值，表示为收藏并订阅操作
    			check = repSupermarketDAO.isExistFavorite(data);	//查询该报表是否已经收藏
    		}
    		if(!check){	
    	        //当该报表没有被用户收藏时，才收藏并订阅报表
	        	if(repSupermarketDAO.insertFavoriteAndSub(data)){
	        		rs = 2;
	        	}else{
	        		rs = 3;
	        	}
    		}
	    }catch(Exception e) {
	        rs = 0;
	    }
    	return rs;
    }

    /**
     * 获取报表订阅信息
     * @param favoriteId 收藏ID
     * @param reportId 报表ID
     * @return 返回订阅方案详细配置
     * @author 王春生
     * @date 2012-03-27 14:40
     */
    public Map<String,Object> getReportPushConfig(int favoriteId,int reportId){
        Map<String,Object> pushConfig =  repSupermarketDAO.getReportPushConfig(favoriteId,reportId);
        if(pushConfig!=null){
            String pushTypes = MapUtils.getString(pushConfig,"PUSH_TYPE");
            String[] arr = pushTypes.split(",");
            for (String anArr : arr) {
                if (anArr.equals("1")) {
                    pushConfig.put("sendMethod1","1");
                } else if (anArr.equals("2")) {
                    pushConfig.put("sendMethod2","2");
                } else if (anArr.equals("3")) {
                    pushConfig.put("sendMethod3","3");
                }
            }
            String sendTime = MapUtils.getString(pushConfig,"SEND_BASE_TIME");
            int startNum = MapUtils.getIntValue(pushConfig, "SEND_TIME_ADD", 0);
            Date sendBaseTime = DateUtil.getDateTimeByString(sendTime, "yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sendBaseTime);
            calendar.add(Calendar.DATE, startNum);	//基准时间为发送时间减去初始时间增量天数
            pushConfig.put("sendTime",DateUtil.format(calendar.getTime(),"yyyy-MM-dd HH:mm:ss"));
        }
        return pushConfig;
    }

    /**
     * 修改报表订阅配置信息
     * @param data 表单数据
     * @return
     */
    public int updateReportPushConfig(Map<String,Object> data){
        try{
            BaseDAO.beginTransaction();
            repSupermarketDAO.updateReportPushConfig(data);
            BaseDAO.commit();
            return 1;
        }catch (Exception e){
            BaseDAO.rollback();
            Log.error("修改订阅信息出错", e);
        }
        return 0;
    }

    /**
     * 通过报表id得到报表信息
     * @param reportId 报表id
     * @return
     */
    public Map<String,Object> getReportDetail(String reportId){
    	return repSupermarketDAO.getReportDetail(reportId);
    }
	
	public void setRepSupermarketDAO(RepSupermarketDAO repSupermarketDAO) {
		this.repSupermarketDAO = repSupermarketDAO;
	}
	
}



