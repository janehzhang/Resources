package tydic.portalCommon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import tydic.frame.BaseDAO;
import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;
import tydic.meta.web.session.SessionManager;
import tydic.portalCommon.util.IUnionWorkItemService;
import tydic.portalCommon.util.UnionWorkItemService;
import tydic.portalCommon.util.WorkItemBean;
import tydic.portalCommon.util.WorkItemsBean;


/**
 * Copyrights @ 2011,Tianyuan DIC Information Co.,Ltd. All rights reserved.<br>
 *
 * @author: 杨苏维
 * @description 指标批示
 * @date: 12-1-31
 * @time: 上午9:56
 */


public class PortalInstructionAction {
	private DateUtil du = new DateUtil();
	
	private PortalInstructionDAO instructionDAO;
	//公告表dao
    private MetaMagNoticeDAO metaMagNoticeDAO;
    
    private MetaPortalIndexDataDAO metaPortalIndexDataDAO;
    
    
    //指标批示
    /****************************************************************************************/
    //批示未读信息
    public List<Map<String,Object>> getWDInstructions(){
    	Map<String,Object> userMap = SessionManager.getCurrentUser();
    	int zoneId = Integer.valueOf(""+userMap.get("zoneId"));
    	int userId = Integer.valueOf(""+userMap.get("userId"));
    	return instructionDAO.getWDInstructions(zoneId,userId);
    }
    /****************************************************************************************/
    
	/**
	 *查询指标批示记录 
	 * 查询某条批示记录和所有回复记录
	 */
	public List<Map<String,Object>> queryInstructions(int instructionId){
//		String zoneId =  SessionManager.getCurrentUser().get("zoneId").toString();
		List<Map<String,Object>> noticeList = metaMagNoticeDAO.getNotice();
//		List<Map<String,Object>> instructionList = instructionDAO.queryInstructions(instructionId,zoneId);
//		instructionList.addAll(noticeList);
//		Collections.sort(instructionList, new Comparator<Map<String,Object>>() {
//
//			@Override
//			public int compare(Map<String,Object> map1, Map<String,Object> map2) {
//				Date date1 = (Date)map1.get("ORDER_TIME");
//				Date date2 = (Date)map2.get("ORDER_TIME");
//				int rtn = 0;
//				if(date1.after(date2)){
//					rtn=-1;
//				}else{
//					rtn=1;
//				}
//				return rtn;
//			}
//		});
		return noticeList;
	}
	
	/**
	 *插入批示回复数据 
	 */
	public boolean insertInstrucInfo(Map<String,Object> map){
	     int userId = SessionManager.getCurrentUserID();
	     String userName = SessionManager.getCurrentUser().get("userNamecn").toString();
	     map.put("userId", userId);
	     map.put("userName", userName);
	     try{
	    	 BaseDAO.beginTransaction();
	    	 instructionDAO.insertInstrucInfo(map);
	    	 BaseDAO.commit();
	     }catch(Exception e){
	    	 BaseDAO.rollback();
	         Log.error(null,e);
	         return false;
	     }
	     return true;
	}
	
	/**
	 *后去某个批示下面的所有子节点
	 *@param instructionId
	 */
	public List<Map<String,Object>> queryAllChildrenInst(int instructionId){
		return instructionDAO.queryAllChildrenInst(instructionId);
	}
	
	
	
	/**
	 *查询图形数据
	 *查询某个时间段内一个指标的所有值 
	 *查询上月同期数据
	 */
	public  List<Map<String,Object>>  queryChartData(String tabId,String maxDate,String indexCode,String localCode,int chartRadio){
		List<Map<String,Object>> charData = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> graphInfoList =  instructionDAO.queryGraphInfo(tabId, indexCode);
        int id= chartRadio;
        if(chartRadio == -1){//初始值(timeIntervalIds值来源于META_PORTAL_INDEX_EXPLAIN行配置)
            String timeIntervalIds = ""+graphInfoList.get(0).get("TIME_INTERVAL_IDS");
            id = Integer.valueOf(timeIntervalIds.split(",")[0]);
            //timeInfo = instructionDAO.getIntervalById(id);
           // intervalValue = Integer.valueOf(""+map.get("INTERVAL_VALUE"));
        }
        Map<String,Object> timeInfo=instructionDAO.getIntervalById(id);
        int intervalValue =MapUtils.getIntValue(timeInfo,"INTERVAL_VALUE");//默认当月
       
        String ruleType= MapUtils.getString(timeInfo,"RULE_TYPE");

		for(int i = 0; i< graphInfoList.size(); i++){
			graphInfoList.get(i).put("RULE_TYPE", ruleType);
			if(ruleType.equals("1")){//1.X轴只能一种情况：天、周、月；2.这里表示按天
				/*X轴时间格式是按天*/
			     if(chartRadio==3){
					intervalValue=Integer.valueOf(maxDate.substring(maxDate.length()-2,maxDate.length()));
				  }
				//System.out.println(intervalValue);
				if(Integer.parseInt(graphInfoList.get(i).get("INTERVAL_VALUE").toString()) == 0){//当月
//					String minDate = getYesLastMouth(maxDate,intervalValue);

					List<String> dlist = getDayArrayInMonth(maxDate,intervalValue-1);
					String minDate = dlist.get(0);
					graphInfoList.get(i).put("listDate", dlist);
					graphInfoList.get(i).put("listData", instructionDAO.queryChartData(maxDate, minDate, indexCode, localCode,tabId));
					charData.add(graphInfoList.get(i));
				}else{
					String lastMaxDate = getYesLastMouth(maxDate,Integer.parseInt(graphInfoList.get(i).get("INTERVAL_VALUE").toString()));
//					String lastMinDate = getBeforeDay(lastMaxDate,intervalValue);
					List<String> dlist = getDayArrayInMonth(lastMaxDate,intervalValue-1);
					String lastMinDate = dlist.get(0);
					graphInfoList.get(i).put("listDate", dlist);
					graphInfoList.get(i).put("listData", instructionDAO.queryChartData(lastMaxDate, lastMinDate, indexCode, localCode,tabId));
					charData.add(graphInfoList.get(i));
				}
			}else if(ruleType.equals("2")){/*X轴时间格式是月*/
			      if(chartRadio==9){
						intervalValue=Integer.valueOf(maxDate.substring(maxDate.length()-4,maxDate.length()-2))-1;
				    }
	     if  (Integer.parseInt(graphInfoList.get(i).get("INTERVAL_VALUE").toString()) == 0) {  //当月

                List<String> mlist = du.listAsc(du.getMothIntervals(maxDate, intervalValue, "yyyyMMdd"));
				graphInfoList.get(i).put("listDate", mlist);
				//计算最小月份：来源于月列表的第一月份
				String minMoth = mlist.get(0).substring(0,6);
				String maxMotn = maxDate.substring(0,6);
				graphInfoList.get(i).put("listData", instructionDAO.getChartMothData(maxMotn, minMoth, indexCode, localCode,tabId));
				charData.add(graphInfoList.get(i));
				
				}else{ //去年同期
				//maxDate =IntmaxDate.substring(0, 4)-1+""
				List<String> mlist = du.listAsc(du.getMothIntervals(maxDate, intervalValue, "yyyyMMdd"));
				graphInfoList.get(i).put("listDate", mlist);
				//计算最小月份：来源于月列表的第一月份
				//String minMoth = mlist.get(0).substring(0,6);
				//String maxMotn = maxDate.substring(0,6);
				
				String minMoth = String.valueOf(Integer.parseInt(mlist.get(0).substring(0,4))-1)+ mlist.get(0).substring(4,6);
				String maxMotn = String.valueOf(Integer.parseInt(maxDate.substring(0,4))-1)+ maxDate.substring(4,6);
				
				graphInfoList.get(i).put("listData", instructionDAO.getChartMothData(maxMotn, minMoth, indexCode, localCode,tabId));
				charData.add(graphInfoList.get(i));
				
			   }
			
			}else if(ruleType.equals("3")){//按周
				List<String> wlist = du.listAsc(du.getWeekIntervals(maxDate, intervalValue, "yyyyMMdd"));
				String minWeekDay = wlist.get(0);
				graphInfoList.get(i).put("listDate", wlist);
				graphInfoList.get(i).put("listData", instructionDAO.queryChartData(maxDate, minWeekDay, indexCode, localCode,tabId));
				charData.add(graphInfoList.get(i));
			}
		}
		
		return charData;
	}
	
	
    /**
     * 获取前1月的日期
     *
     * @param date
     * @param monNum
     * @return
     */
    private static String getMiniTime(String date, Integer monNum) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month_no = Integer.parseInt(date.substring(4, 6)) - monNum;
        int day = Integer.parseInt(date.substring(6, 8));
        Calendar cal = new GregorianCalendar(year, month_no - 1, day);
        String preDate = cal.get(Calendar.YEAR) + String.format("%1$02d", cal.get(Calendar.MONTH) + 1)
                + String.format("%1$02d", cal.get(Calendar.DAY_OF_MONTH));
        return preDate;
    }
    
    /**
     * 获取前多少天的日期
     *
     * @param date
     * @param monNum
     * @return
     */
    private static String getBeforeDay(String date, int monNum) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month_no = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8)) - monNum;
        Calendar cal = new GregorianCalendar(year, month_no - 1, day);

        String preDate = cal.get(Calendar.YEAR) + String.format("%1$02d", cal.get(Calendar.MONTH) + 1)
                + String.format("%1$02d", cal.get(Calendar.DAY_OF_MONTH));
        return preDate;
    }
    

    /**
     * 获取时段段
     * @return
     */
    public List<Map<String, Object>> getTimeRodi(String indexCd,String tabId) {
        String timeId[] =  instructionDAO.getTimeRodi(indexCd,tabId);
        String timesId = "";
        for (int i = 0; i<timeId.length; i++){
        	if(timesId != ""){
        		timesId = timesId +","+timeId[i];
        	}else{
        		timesId = timeId[i];
        	}
        	
        }
        return instructionDAO.getTimeInfo(timesId);
    }
    
    
    //取得昨天的日期
    private static String getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyyMMdd").format(cal.getTime());
        return yesterday;
    }
    
    //取得前一天的上一个月的时间
    private static String getYesLastMouth(String date, int monNum) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month_no = Integer.parseInt(date.substring(4, 6));
        int day = Integer.parseInt(date.substring(6, 8));
        String preDate ="";
        if(monNum >= month_no){
        	int i = 1;
        	if(monNum > 12){
        		i = monNum/12; 
        	}
        	Calendar cal = new GregorianCalendar(year, month_no - 1, day);
            preDate = cal.get(Calendar.YEAR)-1 + String.format("%1$02d", cal.get(Calendar.MONTH)+i*12 + 1 - monNum)
                    + String.format("%1$02d", cal.get(Calendar.DAY_OF_MONTH));
        }else{
        	Calendar cal = new GregorianCalendar(year, month_no - 1, day);
            preDate = cal.get(Calendar.YEAR) + String.format("%1$02d", cal.get(Calendar.MONTH) + 1 - monNum)
                    + String.format("%1$02d", cal.get(Calendar.DAY_OF_MONTH));
        }
        
        return preDate;
    }
    
    
    /**
     * 获取指定月份内的日期序列 曲线使用日期显示周期为30天
     * @param date dayArray in month
     * @return 趋势图显示页面URL
     */
    private static List<String> getDayArrayInMonth(String date, int num) {
        List<String> days = new ArrayList<String>();
        if (date != null && !"".equals(date.trim())) {
            int year = Integer.parseInt(date.substring(0, 4));
            int month_no = Integer.parseInt(date.substring(4, 6)) - 1;
            int day = Integer.parseInt(date.substring(6, 8));
            Calendar calendar = new GregorianCalendar(year, month_no, day);
            Calendar cal = new GregorianCalendar(year, month_no, day - num);
            String[] dateArray = new String[((int) ((calendar.getTimeInMillis() - cal
                    .getTimeInMillis()) / 1000 / 3600 / 24)) + 1];
            cal.add(Calendar.DAY_OF_MONTH, -1);
            for (String aDateArray : dateArray) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
                days.add(cal.get(Calendar.YEAR)
                                + String.format("%1$02d", (cal.get(Calendar.MONTH) + 1))
                                + String.format("%1$02d", cal.get(Calendar.DAY_OF_MONTH)));
            }
        }
        return days;
    }
    
    //查询某个地市某日的数据
    public Object getOneAreaData(String dateNo,int tabId,String indexCd,String localCode) {
       int rptType = metaPortalIndexDataDAO.getTabRptType(tabId);
       return instructionDAO.getOneAreaData(dateNo,tabId,rptType,indexCd,localCode);
    }
    
	 
   /**
    * 查询某个指标的某个区域的下级倒数后三名 
    * @param data indexcd  localCode
    * 
    */
    public List<Map<String,Object>> queryBg3(String dataNo,String indexCd,String localCode,String valueName){
    	List<Map<String,Object>> list = instructionDAO.queryBg3(dataNo, indexCd, localCode);
    	List<Map<String,Object>> listBg3 = new ArrayList<Map<String,Object>>();
    	
    	for(Map<String,Object> map :list){
    		//如果为空先初始化3个值
    		if(listBg3.size() == 0){
    			listBg3.add(map);
    			listBg3.add(map);
    			listBg3.add(map);
    		}
    		if(Double.parseDouble(listBg3.get(0).get(valueName).toString()) > Double.parseDouble(map.get(valueName).toString())){
    			//listBg3.remove(3);
    			listBg3.set(2, listBg3.get(1));
    			listBg3.set(1, listBg3.get(0));
    			listBg3.set(0, map);
    		}
    	}
    	return listBg3;
    }
    
   
    /**add yanhd **/    
   public List<Map<String,Object>> getPsSendList(){
    	Map<String,Object> userMap = SessionManager.getCurrentUser();
    	return instructionDAO.getPsSendList(Integer.valueOf(""+userMap.get("userId")));
    }
    /**
     * 批示查询
     * @param queryData 参数列表
     * @param page 分页参数
     * @return
     */
  //add by quanxia
    public List<Map<String, Object>> getPsListByPara(
            Map<String, Object> queryData, Page page) {
        if (page == null) {
            page = new Page(0, 20);
        }
        return instructionDAO.getPsList(queryData,page);
    }  
    
    /**
     *  根据id查询对象
     * @param id
     * @return
     */
    public Map<String,Object> getPsInfoById(long id){
    	return instructionDAO.getPsInfoById(id);
    }
    
    /**
     *  已读
     * @param id
     * @return
     */
    public boolean psIsRead(Map<String, Object> data, Page page) {
    	Integer id=Integer.parseInt(MapUtils.getString(data,"id",null));
    	Map<String,Object> map= getPsInfoById(id);
    	deleteWorkItems(map);
	    return instructionDAO.psIsRead(id);
    }
    /**
     *  处理
     * @param id
     * @return
     */
    public boolean psDeal(Map<String,Object> data, Page page) {
      	Integer id=Integer.parseInt(MapUtils.getString(data,"id",null));
    	Map<String,Object> map= getPsInfoById(id);
    	deleteWorkItems(map);
    	return instructionDAO.psDeal(data,page);
    } 
   /**
    * 流程删除  
    * @param map
    */
   private void deleteWorkItems(Map<String,Object> map){
		String GUID=MapUtils.getString(map,"GUID",null);
    	String userNames = MapUtils.getString(map,"RECEIVER_NAMES",null);
        String[] receiver_names = userNames.split(",");
        for(int i = 0;i < receiver_names.length;i++){ 
            WorkItemsBean bean=new WorkItemsBean();
    	    bean.setRequestType("1");
    	    bean.setAppProcInstID(GUID);
    	    bean.setAppWorkQueueID(receiver_names[i]+"_"+GUID);
    	    bean.setTitle(MapUtils.getString(map,"TITLE",null));
    	    bean.setExecutor(receiver_names[i]);
    	    bean.setExecutorName(receiver_names[i]);
    	    IUnionWorkItemService uwis=new UnionWorkItemService();
		    uwis.deleteWorkItem(bean);
       }
   }
    
    /**
     *  新增批示信息  add yanhd
     * @param map
     * @return
     */
	public boolean addPsInfo(Map<String,Object> map){
	     String GUID=UUID.randomUUID().toString();
	     int userId = SessionManager.getCurrentUserID();
	     String userName = SessionManager.getCurrentUser().get("userNamecn").toString();
	     map.put("createId", userId);
	     map.put("createName", userName);
	     map.put("GUID", GUID);
	     try{
	    	 BaseDAO.beginTransaction();
	    	 long id=instructionDAO.queryForNextVal("seq_app_id");
	    	 map.put("psId", id);
	    	//批示发送
	       String receiver_id=MapUtils.getString(map,"userIds",null);
	       String receiver_name=MapUtils.getString(map,"userNames",null); 	   
	       String[] receiver_ids = receiver_id.split(",");
	       String[] receiver_names = receiver_name.split(",");
	       /**
 	       * 统一待办
 	       */
	        WorkItemsBean bean=new WorkItemsBean();
	        bean.setRequestType("1");//新建提交
		    bean.setAppProcInstID(GUID);
		    bean.setTitle(MapUtils.getString(map,"noticeTitle",null));
		    bean.setCreateTime(tydic.meta.common.DateUtil.getCurrentDay("yyyy-MM-dd HH:mm:ss"));
		    bean.setInitiator(userName);
		    bean.setInitiatorName(userName);
	        for(int i = 0;i < receiver_ids.length;i++){ 
	    	      /**
	    	       * 统一待办
	    	       */
	    		  WorkItemBean workItem =new  WorkItemBean();
	    		  workItem.setAppWorkQueueID(receiver_names[i]+"_"+GUID);
	    		  workItem.setCurrActivity("未读");
	    		  workItem.setPendingItemURL("http://132.121.165.45:8081/tydic-bi-meta/portal/login_sso.jsp");
	    		  workItem.setExecutor(receiver_names[i]);
	    		  workItem.setExecutorName(receiver_names[i]);
	    		  bean.addWorkItems(workItem);
			}
			instructionDAO.insertPsInfo(map);
	        //统一待办流程提交     
		    IUnionWorkItemService uwis=new UnionWorkItemService();
		    uwis.createWorkItems(bean);
	    	BaseDAO.commit();
	     }catch(Exception e){
	    	 BaseDAO.rollback();
	         Log.error(null,e);
	         return false;
	     }
	     return true;
	}
	public void setInstructionDAO(PortalInstructionDAO instructionDAO) {
		this.instructionDAO = instructionDAO;
	}
    public void setMetaMagNoticeDAO(MetaMagNoticeDAO metaMagNoticeDAO){
        this.metaMagNoticeDAO = metaMagNoticeDAO;
    }
    public void setMetaPortalIndexDataDAO(MetaPortalIndexDataDAO metaPortalIndexDataDAO) {
        this.metaPortalIndexDataDAO = metaPortalIndexDataDAO;
    }
}
