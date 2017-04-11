package tydic.portalCommon;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.module.mag.menu.MenuDAO;
import tydic.meta.web.session.SessionManager;
import tydic.portalCommon.audit.AuditDAO;
import tydic.reports.graph.GraphDAO;


public class PortalCtrlr {

    private PortalDAO portalDAO;

    private MetaPortalTabDAO metaPortalTabDAO;

    private MenuDAO menuDAO;
    private GraphDAO graphDAO;
    public GraphDAO getGraphDAO() {
		return graphDAO;
	}

	public void setGraphDAO(GraphDAO graphDAO) {
		this.graphDAO = graphDAO;
	}
	private MetaPortalColumnsDAO metaPortalColumnsDAO;

    private MetaPortalIndexExplainDAO metaPortalIndexExplainDAO;

    private MetaPortalScopeDAO metaPortalScopeDAO;

    private AuditDAO auditDAO;

    private MetaPortalIndexDataDAO metaPortalIndexDataDAO;

    private MetaPortalTimeIntervalDAO metaPortalTimeIntervalDAO;

    private MetaPortalTrendChartDAO metaPortalTrendChartDAO;
    //公告表dao
    private MetaMagNoticeDAO metaMagNoticeDAO;
    public void setPortalDAO(PortalDAO portalDAO) {
        this.portalDAO = portalDAO;
    }

    public void setMetaPortalTabDAO(MetaPortalTabDAO metaPortalTabDAO) {
        this.metaPortalTabDAO = metaPortalTabDAO;
    }

    public void setMenuDAO(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }

    public void setMetaPortalColumnsDAO(MetaPortalColumnsDAO metaPortalColumnsDAO) {
        this.metaPortalColumnsDAO = metaPortalColumnsDAO;
    }

    public void setMetaPortalIndexExplainDAO(MetaPortalIndexExplainDAO metaPortalIndexExplainDAO) {
        this.metaPortalIndexExplainDAO = metaPortalIndexExplainDAO;
    }

    public void setMetaPortalScopeDAO(MetaPortalScopeDAO metaPortalScopeDAO) {
        this.metaPortalScopeDAO = metaPortalScopeDAO;
    }

    public void setAuditDAO(AuditDAO auditDAO) {
        this.auditDAO = auditDAO;
    }

    public void setMetaPortalIndexDataDAO(MetaPortalIndexDataDAO metaPortalIndexDataDAO) {
        this.metaPortalIndexDataDAO = metaPortalIndexDataDAO;
    }

    public void setMetaPortalTimeIntervalDAO(MetaPortalTimeIntervalDAO metaPortalTimeIntervalDAO) {
        this.metaPortalTimeIntervalDAO = metaPortalTimeIntervalDAO;
    }

    public void setMetaPortalTrendChartDAO(MetaPortalTrendChartDAO metaPortalTrendChartDAO) {
        this.metaPortalTrendChartDAO = metaPortalTrendChartDAO;
    }
    public void setMetaMagNoticeDAO(MetaMagNoticeDAO metaMagNoticeDAO){
        this.metaMagNoticeDAO = metaMagNoticeDAO;
    }

    /**
     * 获取显示表单栏
     *
     * @return
     */

    public Object[] getAreaName() {
        return portalDAO.getAreaName();
    }

    /**
     * 获取审核数据
     *
     * @param tab_id
     * @param dateNo
     * @param areaCode
     * @return
     */
    public Object[] getDataAudit(int tabId, String dateNo, String areaCode) {
    	int rptType = metaPortalIndexDataDAO.getTabRptType(tabId);
        return auditDAO.queryAuditData(tabId,rptType, dateNo, areaCode);
    }

    /**
     * @Title: getNoticeById 
     * @Description: 根据ID获取公告信息
     * @param id
     * @return Map<String,Object>
     * @throws
     */
    public Map<String,Object> getNoticeById(long id){
    	return metaMagNoticeDAO.getNoticeById(id);
    }
    /**
     * 获取公告所有信息
     */
    public List<Map<String,Object>> getNoticeAllMes(){
    	Map<String,Object> userMap = SessionManager.getCurrentUser();
    	int zoneId = Integer.valueOf(""+userMap.get("zoneId"));
    	return metaMagNoticeDAO.getNoticeAllMes(zoneId);
    }
    //公告信息
    /*************************************************************************************/
    /**
     * 公告列表信息
     * @param currentPage 当前页 
     * @param dataCounts 数据总数 
     * @param pageCounts 页面总数 
     * @param pageSize 页面条数     
     */
    public Object[] getNoticeByPara(int currentPage,int dataCounts,int pageCounts,int pageSize){
    	Object[] res = new Object[3];
    	//zoneId
    	Map<String,Object> userMap = SessionManager.getCurrentUser();
    	int zoneId = Integer.valueOf(""+userMap.get("zoneId"));
    	//获取总数
    	int counts = dataCounts;
    	if(dataCounts == 0){
    		counts = metaMagNoticeDAO.getNoticeCount(zoneId);
    		Pagination pagination = new Pagination();
    		pageCounts = pagination.getPagesCount(counts, pageSize);
    	}
    	/*计算开始和结束下标值*/
    	int startSubscript = 0;
    	if(currentPage <= 0)
    		currentPage = 1;
    	if(currentPage == 1){
    		startSubscript = 1;
    	}else
    		startSubscript = (currentPage - 1) * pageSize + 1;
    	int endSubscript = currentPage * pageSize;
    	//数据
    	List<Map<String,Object>> data = metaMagNoticeDAO.getNotices(metaMagNoticeDAO.getNoticeSql(zoneId, startSubscript, endSubscript));
    	//保存到数组中
    	res[0] = counts;//数据总数
    	res[1] = data;//数据
    	res[2] = pageCounts;//总页数
    	return res;
    }
    /*************************************************************************************/
    /**
     * 获取首页菜单子报表项及属性 时间
     *
     * @return
     */

 /*   public Object[] getViewTabs() {
        try {
            Object[] res = new Object[6];
            boolean isShowChart = true;
            List<Map<String, Object>> tbs = metaPortalTabDAO.queryAllTabs(); //tab项
            //进行权限认证，即当前用户拥有的菜单权限。
            if (tbs != null && tbs.size() > 0) {
                if (!SessionManager.isCurrentUserAdmin()) {
                    for (int i = 0; i < tbs.size(); i++) {
                        Map<String, Object> tab = tbs.get(i);
                        if (MapUtils.getIntValue(tab, "MENU_ID", -1) > 0) {
                            if (!menuDAO.isUserExistsMenu(MapUtils.getIntValue(tab, "MENU_ID", -1), SessionManager.getCurrentUserID())) {
                                tbs.remove(i--);
                            }
                        }
                    }
                }
                if (tbs.size() > 0) {
                    Object[] rpt_index = new Object[tbs.size()];
                    Object[] rpt_index_EXP = new Object[tbs.size()];
                    List<Map<String,Object>> trendChartList = null;
                    for (int i = 0; i < tbs.size(); i++) {
                        int tab_id = MapUtils.getIntValue(tbs.get(i), "TAB_ID");
                        //找每个tabid 查询当前指标页的所有信息
                        rpt_index[i] = metaPortalColumnsDAO.queryColumnsConfig(tab_id); //报表指标配置
                        rpt_index_EXP[i] = metaPortalIndexExplainDAO.queryExplain(tab_id);//指标解释
                        List<Map<String, Object>> tmp = metaPortalScopeDAO.queryScope(tab_id);//数据审核标识
                        
                        //指标趋势查询
                        trendChartList = metaPortalTrendChartDAO.queryTrendChartsByTabId(tab_id);
                        判断是否需要展示图形
                        if(trendChartList != null && trendChartList.size() > 0){
                        	//默认列
                        	String defaultGrid = MapUtils.getString(tbs.get(i), "DEFAULT_GRID");
                        	int defaultGridColNum = -1;
                        	if(StringUtils.isNotEmpty(defaultGrid) && !"null".equals(defaultGrid))
                        		defaultGridColNum = Integer.valueOf(defaultGrid.split(",")[1]);
                        	for(Map<String,Object> map : trendChartList){
                        		if((""+(defaultGridColNum+1)).equals(""+map.get("SHOW_ORDER_ID"))){
                        			if("0".equals(""+map.get("IS_SHOW_COL")))
                        				isShowChart = false;
                        		}
                        	}
                        }
                        if (tmp.size() > 0 ) {//&& isShowChart
                            //判断是月报还是日报
                            int rtpType = MapUtils.getIntValue(tbs.get(i), "RPT_TYPE", 1);
                            tbs.get(i).put("MIN_DATENO", tmp.get(0).get("MIN_DATENO"));
                            //月间隔
                            int monthInterval = MapUtils.getIntValue(tmp.get(0), "MONTH_INTERVAL", 0);
                            if (rtpType == PortalConstant.REPORT_TYPE_DATE
                                    || (rtpType == PortalConstant.REPORT_TYPE_MONTH && monthInterval == 0)) {
                                //如果是日报，取当前日期前几天
                                tbs.get(i).put("MAX_DATENO", tmp.get(0).get("MAX_DATENO"));
                            } else {//如果是月报，且定义了月间隔值,有效账期取间隔的月份的最后一天。
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(new Date());
                                calendar.add(Calendar.MONTH, monthInterval);
                                String maxDateNo = DateUtil.format(DateUtil.getLastMonthDay(calendar.getTime()), "yyyyMMdd");
                                tbs.get(i).put("MAX_DATENO", maxDateNo);
                            }
                            tbs.get(i).put("EFFECT_DATENO", tmp.get(0).get("EFFECT_DATENO"));
                        }
                    }
                    //首页所有的时间间隔规则定义
                    List<Map<String,Object>> timeIntervals=metaPortalTimeIntervalDAO.queryAllTimeInterval();
                    res[0] = tbs;
                    res[1] = rpt_index;
                    res[2] = rpt_index_EXP;
                    //第四位为指标趋势定义
                    res[3]=trendChartList;
                    //第五位为时间间隔定义
                    res[4]=timeIntervals;
                    res[5]=isShowChart;//默认是否会展示图形
                    return res;
                } else {
                    return new Object[0];
                }
            } else { //如果没有配置其标签页信息，返空数组
                return new Object[0];
            }
        } catch (Exception e) {
            Log.error(null, e);
        }

        return null;
    }*/
    
    
/**  yanhd start **/
	/**
	 * 获取首页最新日期
	 */
	public String getNewDate() {
		try {
			MetaPortalIndexDataDAO dao = new MetaPortalIndexDataDAO();
			return dao.getNewDate();
		} catch (Exception e) {
			Log.error(null, e);
		}
		return "";
	}  
	
	/**
	 * 获取首页最新月份
	 */
	public String getNewMonth() {
		try {
			MetaPortalIndexDataDAO dao = new MetaPortalIndexDataDAO();
			return dao.getNewMonth();
		} catch (Exception e) {
			Log.error(null, e);
		}
		return "";
	} 	
	
  /**
   * 获取首页菜单子报表项
   */
  public List<Map<String, Object>> getViewTabs(Map<String, Object> map) {
		try {
			return metaPortalIndexDataDAO.getViewTabs(map);
		} catch (Exception e) {
			Log.error(null, e);
		}
		return null;
	}
/**  yanhd end**/    
    
    /**
     * @Title: getIndexCdWaringMes 
     * @Description: 获取预警信息
     * @param tabId
     * @param indexCd
     * @return Map<String,List<Map<String,Object>>> key:indexCd;value:list
     * @throws
     */
    public List<Map<String,Object>> getIndexCdWaringMes(String tabId){
    	return  metaPortalColumnsDAO.getIndexCdWaringMes(tabId);
    }
    /**
     * @Title: isShowChartByColIdAndTabId 
     * @Description: 根据指标code、tabId、colId判断是否该列有图形
     * @param indexCd 指标code
     * @param tabId 
     * @param showOrderId colId
     * @return boolean   
     * @throws
     */
    public boolean isShowChartByColIdAndTabId(String indexCd,String tabId,String showOrderId){
    	Map<String,Object> map = metaPortalTrendChartDAO.getTrendChartByColIdAndTabId(indexCd, tabId, showOrderId);
    	if(map != null && map.size() > 0)
    		return false;
    	else
    		return true;
    }
    /**
     * 获取表Data
     *
     * @return
     */

/*    public Object[][] getTableData(ReportPO po, String cols[]) {
    	Object userId = SessionManager.getCurrentUserID();
        String indexCd = po.getIndexCd();
        String dateNo = po.getDateNo();
        String localCode = po.getLocalCode();
        long tabId = po.getTabId();
        int rptType = metaPortalIndexDataDAO.getTabRptType(tabId);
        if (StringUtils.isEmpty(indexCd)) {
        	 先判断“首页指标用户配置表”中是否有配置记录：有需要管理取，默认取所有指标
             boolean isConfig = metaPortalIndexDataDAO.isHaveDataByTabIdAndUserId(""+tabId, ""+userId);
            //如果指标数据为空，说明加载某一地域的汇总数据
            return metaPortalIndexDataDAO.getData(tabId,rptType, dateNo, localCode, cols,isConfig,""+userId);
        } else {//下钻
            return metaPortalIndexDataDAO.getChildData(tabId,rptType,dateNo,localCode,indexCd,cols);
        }
    }*/
    
   /**yanhd start**/
    public List<Map<String, Object>> getTableData(Map<String, Object> map) {
		return metaPortalIndexDataDAO.getTableData(map);
	}
    
    /**
    public String getChart(Map<String, Object> map){
    	StringBuffer  chartMap=new StringBuffer();
    	String tabId =    MapUtils.getString(map, "tabId",    null);
    	String dataDate = MapUtils.getString(map, "dataDate", null).replaceAll("-", "");
    	String field =    MapUtils.getString(map, "field",    null);
    	int numVdivlines =-1;
    	StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
    	if(tabId.equals("1")){//日
    		 temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='d562a3' seriesName='当月' color='d562a3' anchorBorderColor='FFFFFF'>\n");
             temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='7b04b8' seriesName='上月' color='7b04b8' anchorBorderColor='FFFFFF'>\n");
          
             String tempDay=dataDate.substring(0,dataDate.length()-2)+"01";//201201
	    	 for (int i = Integer.parseInt(tempDay); i <= Integer.parseInt(dataDate); i++) {
	    		  String year      =String.valueOf(i).substring(0, 4);
	    	      String month_no = String.valueOf(i).substring(4, 6);
	    	      String day      = String.valueOf(i).substring(6, 8);
	    	      String showDay=day;
	    		  String lastDay=getLastMon(year+month_no)+day;
	    		  String currentValue="0";
	    		  String lastValue="0";
	    		  
	    		  Map<String, Object> currentObj = metaPortalIndexDataDAO.getChartData(String.valueOf(i),map);
	    		  Map<String, Object> lastObj =    metaPortalIndexDataDAO.getChartData(lastDay,map);
	    	      if (null != currentObj) {
	    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
	    		   }
	    	      if (null != lastObj) {
	    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
	    		   }
	    		 temp1.append("<category name='"+showDay+"' />\n");
	    		 temp2.append("<set value='"+currentValue+"' hoverText='当月： "+currentValue+"' />\n");
	    		 temp3.append("<set value='"+lastValue+"'    hoverText='上月： "+lastValue+"' />\n");
	    		 
	    		 numVdivlines=numVdivlines+1;
	    	 }
    		
    		
    	}else{//月
   		 temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='d562a3' seriesName='当月'      color='d562a3' anchorBorderColor='FFFFFF'>\n");
         temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='7b04b8' seriesName='去年同期'  color='7b04b8' anchorBorderColor='FFFFFF'>\n");
	  	 String showMonth=dataDate.substring(0, 6);
	       Calendar c = Calendar.getInstance();
	       String   currentMonth = new SimpleDateFormat("yyyyMM").format(c.getTime());
	       if(currentMonth.equals(showMonth)){
	    	   showMonth=getLastMon(showMonth);
	       }
	         String tempMonth=showMonth.substring(0,showMonth.length()-2)+"01";//201301
	    	 for (int i = Integer.parseInt(tempMonth); i <= Integer.parseInt(showMonth); i++) {
	    		  String year      =String.valueOf(i).substring(0, 4);
	    	      String month_no  = String.valueOf(i).substring(4, 6);
	    	      int showDay   =Integer.parseInt(month_no);
                  String lastYear =	String.valueOf(Integer.parseInt(year)-1)+month_no;    	      
	    		  String currentValue="0";
	    		  String lastValue="0";
	    		  
	    		  Map<String, Object> currentObj = metaPortalIndexDataDAO.getChartData(String.valueOf(i),map);
	    		  Map<String, Object> lastObj =    metaPortalIndexDataDAO.getChartData(lastYear,map);
	    	      if (null != currentObj) {
	    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
	    		   }
	    	      if (null != lastObj) {
	    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
	    		   }
	    		  
	    	      temp1.append("<category name='"+showDay+"月份' />\n");
		          temp2.append("<set value='"+currentValue+"' hoverText='当月： "+currentValue+"' />\n");
		    	  temp3.append("<set value='"+lastValue+"'    hoverText='去年同期： "+lastValue+"' />\n");  
		    	  
		    	  numVdivlines=numVdivlines+1;
		    		
	    	 }
    		
    	}
        temp1.append("</categories>\n");
        temp2.append("</dataset>\n");
        temp3.append("</dataset>\n");
    	
        chartMap.append("<chart  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
    	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
    	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
    	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
    	        " numdivlines='4' adjustDiv='0' numVdivlines='"+numVdivlines+"'   rotateNames='0'"+
    	        " chartRightMargin='20' chartLeftMargin='10'>\n");        
        chartMap.append(temp1).append(temp2).append(temp3);
	    chartMap.append("</chart>");
    	return chartMap.toString();
    } 
     **/
    /**
     * 指标解释
      * 方法描述：
      * @param: 
      * @return: 
      * @version: 1.0
      * @author: Administrator
      * @version: 2013-3-5 上午10:40:32
     */
	public  List<Map<String,Object>> getIndexExp(Map<String,Object> map){
		 return metaPortalIndexDataDAO.getIndexExp(map);
	 } 
    
   /**
    * 钻取各地市的数据
     * 方法描述：
     * @param: 
     * @return: 
     * @version: 1.0
     * @author: Administrator
     * @version: 2013-3-6 下午04:19:55
    */
	public List<Map<String, Object>> getDrillTableData(Map<String, Object> map) {
		return metaPortalIndexDataDAO.getDrillTableData(map);
	}
    /**
     *  
      * 方法描述：地市图形展示
      * @param: 
      * @return: 
      * @version: 1.0
      * @author: Administrator
      * @version: 2013-3-6 下午05:48:09
     */
	public String getAreaChart(Map<String, Object> map){
    	StringBuffer  chartMap=new StringBuffer();
    	String tabId =       MapUtils.getString(map, "tabId",    null);
    	String dataDate =    MapUtils.getString(map, "dataDate", null).replaceAll("-", "");
    	String field =       MapUtils.getString(map, "field",    null);
        String indexCd =     MapUtils.getString(map, "indexCd", null);
    	String currentColor= "FBC62A";
    	String lastColor=    "71B359";
        double maxVal=0;
        double minVal=0;
        
    	StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
    	if(tabId.equals("1")){//日
	    	  String year      =String.valueOf(dataDate).substring(0, 4);
	   	      String month_no = String.valueOf(dataDate).substring(4, 6);
	   	      String day      = String.valueOf(dataDate).substring(6, 8);
	   	     
	   	      //String lastDay=getLastMon(year+month_no)+day;
    	  	  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	      String lastDay=sdf.format(DateUtil.getDateOfLastMonth(year+month_no+day).getTime());
    	      
    		 temp2.append("<dataset  seriesName='"+dataDate+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
             temp3.append("<dataset  seriesName='"+lastDay+"'  color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n");    
            List<Map<String,Object>> list=metaPortalIndexDataDAO.getDrillTableData(map);
         for (Map<String, Object> key : list){
        	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
        	  String lastValue="0";
        	  
     		  Map<String,Object> param=new HashMap<String, Object>();
     		  param.put("tabId", tabId);
     		  param.put("zoneCode",key.get("LOCAL_CODE"));
     		  param.put("indexCd",indexCd);
    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
    	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
     		  Map<String, Object> lastObj =    metaPortalIndexDataDAO.getChartData(lastDay,param);
     	      if (null != lastObj) {
     	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
     		   }	
	    	    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
	    	    minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
             	temp1.append("<category label='"+key.get("AREA_NAME")+"'/>\n");
          	    temp2.append("<set      value='"+curValue+"' />\n");
          	    temp3.append("<set      value='"+lastValue+"' />\n");
          }           
    	}else{//月
    		 String year      = String.valueOf(dataDate).substring(0, 4);
    	     String month_no  = String.valueOf(dataDate).substring(4, 6);
    		 String showCurrentYEAR = String.valueOf(dataDate);// 当月
	   	     //String showLastYEAR = String.valueOf(Integer.parseInt(year)-1)+month_no;// 去年同期
    		 String showLastYEAR = getLastMon(showCurrentYEAR);
	   	     temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
	         temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
            List<Map<String,Object>> list=metaPortalIndexDataDAO.getDrillTableData(map);
            for (Map<String, Object> key : list){
            	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
	    		  String lastValue="0";
	    		  
	    		  Map<String,Object> param=new HashMap<String, Object>();
	    		  param.put("tabId", tabId);
	    		  param.put("zoneCode",key.get("LOCAL_CODE"));
	    		  param.put("indexCd",indexCd);
	    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
	    	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
	    	      
	    		  Map<String, Object> lastObj =    metaPortalIndexDataDAO.getChartData(showLastYEAR,param);
	    		  if (null != lastObj) {
	    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
	    		   }
	    		  
		    	    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		    	    minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
             	temp1.append("<category label='"+key.get("AREA_NAME")+"'/>\n");
          	    temp2.append("<set      value='"+curValue+"' />\n");
          	    temp3.append("<set      value='"+lastValue+"' />\n");
            }   		
    	}
        temp1.append("</categories>\n");
        temp2.append("</dataset>\n");
        temp3.append("</dataset>\n");
        chartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
              " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
              " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
              " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
              " numdivlines='4' adjustDiv='0'   rotateNames='0'"+
              " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");        
       
        
        chartMap.append(temp1).append(temp2).append(temp3);
	    chartMap.append("</chart>");
    	return chartMap.toString(); 
	}
	
	/**
	 *  管理层
	  * 方法描述：加载图形
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2013-3-7 下午11:07:18
	 */
   public List<Map<String, Object>> loadSetChart(Map<String, Object> map){
	   List<Map<String, Object>> chartSetList=new ArrayList<Map<String,Object>>();

	   //拆线图
	   Map<String, Object> key=new HashMap<String, Object>();
	   key.put("ID", "1");
	   key.put("chartTitle",   MapUtils.getString(map, "tabId","").equals("1")?MapUtils.getString(map, "chartTitle",    null)+"->当日值":MapUtils.getString(map, "chartTitle",    null));
	   key.put("indexCd",      MapUtils.getString(map, "indexCd",       null));
	   key.put("SHOW_ID",   "VALUE2");
	   key.put("SHOW_NAME", "当月值");
	   key.put("CHART_NAME", "MSLine.swf");
	   key.put("SHAPE_ID", "1");
	   key.put("CURRENT_COLOR","FF0000");
	   key.put("LAST_COLOR",   "0066FF");
	   map.put("currentColor", "FF0000");
	   map.put("lastColor",    "0066FF");
	   map.put("shapeId",      "1");
	   key.put("XML", getLineChartXML(map));
	   chartSetList.add(key);
	   
	
	 
	   //柱状图
	   Map<String, Object> keyArea=new HashMap<String, Object>();
	   keyArea.put("ID", "2");
	   keyArea.put("chartTitle",   MapUtils.getString(map, "tabId","").equals("1")?MapUtils.getString(map, "chartTitle",    null)+"->本月累计":MapUtils.getString(map, "chartTitle",    null));
	   keyArea.put("indexCd",      MapUtils.getString(map, "indexCd",       null));
	   keyArea.put("SHOW_ID",   "VALUE2");
	   keyArea.put("SHOW_NAME", "当月值");
	   keyArea.put("CHART_NAME", "MSColumn2D.swf");
	   keyArea.put("SHAPE_ID", "2");
	   keyArea.put("CURRENT_COLOR","FBC62A");
	   keyArea.put("LAST_COLOR",   "71B359");	   
	   map.put("currentColor", "FBC62A");
	   map.put("lastColor",    "71B359");
	   map.put("shapeId",      "2");
	   keyArea.put("XML", getAreaChartXML(map));
	   chartSetList.add(keyArea);
	 return chartSetList;
   }
   
   /**
    *  21个地市
     * 方法描述：
     * @param: 
     * @return: 
     * @version: 1.0
     * @author: yanhaidong
     * @version: 2013-4-24 下午04:21:10
    */
   public Map<String, Object> loadSet21AreaChart(Map<String, Object> map){
	   String changeZone=MapUtils.getString(map, "changeZone",    "0");
	   Map<String, Object> key=new HashMap<String, Object>();
	   //柱状图
	   map.put("currentColor", "FBC62A");
	   map.put("lastColor",    "71B359");
	   String strXML="";
	   if(changeZone.equals("0")){
		   strXML=get21AreaChartXML(map);
	   }else{
		   strXML=getAreaChartXML(map);
	   }
	   key.put("XML",strXML);
	   return key;
   }
   
   
	/**
	 *  领导层
	  * 方法描述：加载图形
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2013-3-7 下午11:07:18
	 */
  public List<Map<String, Object>> loadSetChart_1(Map<String, Object> map){
	  List<Map<String, Object>> chartSetList =metaPortalIndexDataDAO.loadSetChart(map);
	  for(Map<String, Object> key:chartSetList){
		  map.put("indexCd", MapUtils.getString(key, "INDEX_CD",    null));
		  map.put("field",   MapUtils.getString(key, "SHOW_ID",    null));
		  map.put("currentColor", MapUtils.getString(key, "CURRENT_COLOR",    null));
		  map.put("lastColor",    MapUtils.getString(key, "LAST_COLOR",    null));
	      String shapeId=         MapUtils.getString(key, "SHAPE_ID",    null);
		  map.put("shapeId",     shapeId);
		 if(shapeId.equals("1")){ //拆线图
		      key.put("XML", getLineChartXML(map));
		  }else{                 //柱状图
			  key.put("XML", getAreaChartXML(map));
		  }
	   } 
	 return chartSetList;
  }
	
	/**
	  * 方法描述：拆线图形XML
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2013-3-8 上午11:18:03
	  */
	private String getLineChartXML(Map<String, Object> map) {
    	StringBuffer  chartMap=new StringBuffer();
    	String tabId =    MapUtils.getString(map, "tabId",    null);
    	String dataDate = MapUtils.getString(map, "dataDate", null).replaceAll("-", "");
    	String field =    MapUtils.getString(map, "field",    null);
    	String currentColor= MapUtils.getString(map, "currentColor",    null);
    	String lastColor=    MapUtils.getString(map, "lastColor",    null);
    	int numVdivlines =0;
        double maxVal=0;
        double minVal=0;
    	StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
    	if(tabId.equals("1")){//日
    	
             String currYear  = String.valueOf(dataDate).substring(0, 4);
    	     String currMonth = String.valueOf(dataDate).substring(4, 6);
    		 
    	     String currentMonth=currYear+currMonth;
    	     String lastMonth=getLastMon(currentMonth);
    		 temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+currentMonth+"' color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
             temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastMonth+"'    color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
          
             String tempDay=dataDate.substring(0,dataDate.length()-2)+"01";//201201
	    	 for (int i = Integer.parseInt(tempDay); i <= Integer.parseInt(dataDate); i++) {
	    		  String year      =String.valueOf(i).substring(0, 4);
	    	      String month_no = String.valueOf(i).substring(4, 6);
	    	      String day      = String.valueOf(i).substring(6, 8);
	    	      int showDay=Integer.parseInt(day);
	    
	    		  //String lastDay=getLastMon(year+month_no)+day;
	    	  	  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    	      String lastDay=sdf.format(DateUtil.getDateOfLastMonth(year+month_no+day).getTime());
	    		  
	    	      String currentValue="0";
	    		  String lastValue="0";
	    		  
	    		  Map<String, Object> currentObj = metaPortalIndexDataDAO.getChartData(String.valueOf(i),map);
	    		  Map<String, Object> lastObj =    metaPortalIndexDataDAO.getChartData(lastDay,map);
	    	      if (null != currentObj) {
	    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
	    	    	 
	    		   }
	    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue));
	    	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue));
	    	      
	    	      if (null != lastObj) {
	    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
	    	    	  
	    		   }
	    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
	    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
                  
	    		 temp1.append("<category name='"+showDay+"' />\n");
	    		 temp2.append("<set value='"+currentValue+"' hoverText='"+String.valueOf(i)+"： "+currentValue+"' />\n");
	    		 temp3.append("<set value='"+lastValue+"'    hoverText='"+lastDay+"： "+lastValue+"' />\n");
	    		 
	    		 numVdivlines++;
	    	 }
    		
    	}else{//月
	     String showCurrentYEAR = dataDate.substring(0, dataDate.length() - 2);// 当月
	     String showLastYEAR = String.valueOf(Integer.parseInt(showCurrentYEAR) - 1);// 去年同期
         
	     String tempMonth=showCurrentYEAR+"01";//201301
   		 temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+showCurrentYEAR+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
         temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+showLastYEAR+"'         color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
           
	         for (int i = Integer.parseInt(tempMonth); i <= Integer.parseInt(dataDate); i++) {
	    		  String year      =String.valueOf(i).substring(0, 4);
	    	      String month_no  = String.valueOf(i).substring(4, 6);
	    	      int showDay   =Integer.parseInt(month_no);
                  String lastYear =	String.valueOf(Integer.parseInt(year)-1)+month_no;    	      
	    		  String currentValue="0";
	    		  String lastValue="0";
	    		  Map<String, Object> currentObj = metaPortalIndexDataDAO.getChartData(String.valueOf(i),map);
	    		  Map<String, Object> lastObj =    metaPortalIndexDataDAO.getChartData(lastYear,map);
	    	      if (null != currentObj) {
	    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
	    		   }
	    	      
	    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue));
	    	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue));
	    	      
	    	      if (null != lastObj) {
	    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
	    		   }
	    	      
	    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
	    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
	    	      
	    	      temp1.append("<category name='"+showDay+"月' />\n");
		          temp2.append("<set value='"+currentValue+"' hoverText='"+showCurrentYEAR+"： "+currentValue+"' />\n");
		    	  temp3.append("<set value='"+lastValue+"'    hoverText='"+showLastYEAR+"： "+lastValue+"' />\n");  
		    	  numVdivlines++;
	    	 }
    	}
    	numVdivlines-=2;
        temp1.append("</categories>\n");
        temp2.append("</dataset>\n");
        temp3.append("</dataset>\n");
    	
        chartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
    	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
    	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
    	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
    	        " numdivlines='4' adjustDiv='0' numVdivlines='"+numVdivlines+"'   rotateNames='0'"+
    	        " chartRightMargin='20' chartLeftMargin='10'"+
    	        " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
        	   " exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
        
        chartMap.append(temp1).append(temp2).append(temp3);
	    chartMap.append("</chart>");
    	return chartMap.toString();
	}

	/**
	 *   柱状图XML
	  * 方法描述：
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2013-3-20 下午05:22:26
	 */
	private String getAreaChartXML(Map<String, Object> map) {
    	StringBuffer  chartMap=new StringBuffer();
    	String tabId =       MapUtils.getString(map, "tabId",    null);
    	String dataDate =    MapUtils.getString(map, "dataDate", null).replaceAll("-", "");
    	String field =       MapUtils.getString(map, "field",    null);
    	String currentColor= MapUtils.getString(map, "currentColor",    null);
    	String lastColor=    MapUtils.getString(map, "lastColor",    null);
        String indexCd =  MapUtils.getString(map, "indexCd", null);
        
        double maxVal=0;
        double minVal=0;
    	StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
    	if(tabId.equals("1")){//日
    		  field="VALUE3";
	    	  String year      =String.valueOf(dataDate).substring(0, 4);
	   	      String month_no = String.valueOf(dataDate).substring(4, 6);
	   	      String day      = String.valueOf(dataDate).substring(6, 8);
	   	     
	   	      // String lastDay=getLastMon(year+month_no)+day;
    	  	  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	      String lastDay=sdf.format(DateUtil.getDateOfLastMonth(year+month_no+day).getTime());
    	      
    		 temp2.append("<dataset  seriesName='"+dataDate+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
             temp3.append("<dataset  seriesName='"+lastDay+"'  color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n");    
            List<Map<String,Object>> list=graphDAO.getChangeZoneLevel(map);
         for (Map<String, Object> key : list){
        	  String curValue="0";
        	  String lastValue="0";
     		  Map<String,Object> param=new HashMap<String, Object>();
     		  param.put("tabId", tabId);
     		  param.put("zoneCode",key.get("ZONE_CODE"));
     		  param.put("indexCd",indexCd);
     		  
    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
    	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
    	      
     		  Map<String, Object> lastObj =    metaPortalIndexDataDAO.getChartData(lastDay,param);
     		 Map<String, Object> curObj =    metaPortalIndexDataDAO.getChartData(dataDate,param);
     		  if (null != lastObj) {
     	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
     		   }
     		 if (null != curObj) {
     			  curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
    		   }
     		  
    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
    	      
             	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
          	    temp2.append("<set      value='"+curValue+"' />\n");
          	    temp3.append("<set      value='"+lastValue+"' />\n");
          }           
    	}else{//月
    		 String year      = String.valueOf(dataDate).substring(0, 4);
    	     String month_no  = String.valueOf(dataDate).substring(4, 6);
    		 String showCurrentYEAR = String.valueOf(dataDate);// 当月
	   	     //String showLastYEAR = String.valueOf(Integer.parseInt(year)-1)+month_no;// 去年同期
    		 String showLastYEAR = getLastMon(showCurrentYEAR);
	   	     temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
	         temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
            List<Map<String,Object>> list=graphDAO.getChangeZoneLevel(map);
            for (Map<String, Object> key : list){
            	  String curValue="0";
	    		  String lastValue="0";
	    		  Map<String,Object> param=new HashMap<String, Object>();
	    		  param.put("tabId", tabId);
	    		  param.put("zoneCode",key.get("ZONE_CODE"));
	    		  param.put("indexCd",indexCd);
	    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
	    	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
	    		  
	    	      Map<String, Object> lastObj =    metaPortalIndexDataDAO.getChartData(showLastYEAR,param);
	    	      Map<String, Object> curObj =    metaPortalIndexDataDAO.getChartData(dataDate,param);
	    	      if (null != lastObj) {
	    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
	    		   }
	    	      if (null != curObj) {
	    	    	  curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
	    		   }
	    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
	    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
             	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
          	    temp2.append("<set      value='"+curValue+"' />\n");
          	    temp3.append("<set      value='"+lastValue+"' />\n");
            }   		
    	}
        temp1.append("</categories>\n");
        temp2.append("</dataset>\n");
        temp3.append("</dataset>\n");
        chartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	               " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	               " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	               " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	               " numdivlines='4' adjustDiv='0' rotateNames='0'"+
	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'"+
	               " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
   					" exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
        chartMap.append(temp1).append(temp2).append(temp3);
	    chartMap.append("</chart>");
    	return chartMap.toString();    	
  }
	
	
	/**
	 *   柱状图XML
	  * 方法描述：
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: Administrator
	  * @version: 2013-3-20 下午05:22:26
	 */
	private String get21AreaChartXML(Map<String, Object> map) {
    	StringBuffer  chartMap=new StringBuffer();
    	String tabId =       MapUtils.getString(map, "tabId",    null);
    	String dataDate =    MapUtils.getString(map, "dataDate", null).replaceAll("-", "");
    	String field =       MapUtils.getString(map, "field",    null);
    	String currentColor= MapUtils.getString(map, "currentColor",    null);
    	String lastColor=    MapUtils.getString(map, "lastColor",    null);
        String indexCd =  MapUtils.getString(map, "indexCd", null);
        
        double maxVal=0;
        double minVal=0;
    	StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
    	if(tabId.equals("1")){//日
    		  field="VALUE3";
	    	  String year      =String.valueOf(dataDate).substring(0, 4);
	   	      String month_no = String.valueOf(dataDate).substring(4, 6);
	   	      String day      = String.valueOf(dataDate).substring(6, 8);
	   	     
	   	      // String lastDay=getLastMon(year+month_no)+day;
    	  	  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	      String lastDay=sdf.format(DateUtil.getDateOfLastMonth(year+month_no+day).getTime());
    	      
    		 temp2.append("<dataset  seriesName='"+dataDate+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
             temp3.append("<dataset  seriesName='"+lastDay+"'  color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n");    
            List<Map<String,Object>> list=graphDAO.getChangeZoneLevel(map);
         for (Map<String, Object> key : list){
        	  String curValue="0";
        	  String lastValue="0";
     		  Map<String,Object> param=new HashMap<String, Object>();
     		  param.put("tabId", tabId);
     		  param.put("zoneCode",key.get("ZONE_CODE"));
     		  param.put("indexCd",indexCd);
     		  
    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
    	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
    	      
     		  Map<String, Object> lastObj =    metaPortalIndexDataDAO.getChartData(lastDay,param);
     		  Map<String, Object> curObj =    metaPortalIndexDataDAO.getChartData(dataDate,param);
     		  if (null != lastObj) {
     	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
     		   }	
     		 if (null != curObj) {
     			  curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
    		   }
    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
    	      
             	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
          	    temp2.append("<set      value='"+curValue+"' />\n");
          	    temp3.append("<set      value='"+lastValue+"' />\n");
          }           
    	}else{//月
    		 String year      = String.valueOf(dataDate).substring(0, 4);
    	     String month_no  = String.valueOf(dataDate).substring(4, 6);
    		 String showCurrentYEAR = String.valueOf(dataDate);// 当月
	   	     //String showLastYEAR = String.valueOf(Integer.parseInt(year)-1)+month_no;// 去年同期
    		 String showLastYEAR = getLastMon(showCurrentYEAR);
	   	     temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
	         temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
            List<Map<String,Object>> list=graphDAO.getChangeZoneLevel(map);
            for (Map<String, Object> key : list){
            	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
	    		  String lastValue="0";
	    		  Map<String,Object> param=new HashMap<String, Object>();
	    		  param.put("tabId", tabId);
	    		  param.put("zoneCode",key.get("ZONE_CODE"));
	    		  param.put("indexCd",indexCd);
	    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
	    	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
	    		  
	    	      Map<String, Object> lastObj =    metaPortalIndexDataDAO.getChartData(showLastYEAR,param);
	    	      Map<String, Object> curObj =    metaPortalIndexDataDAO.getChartData(dataDate,param);
	    	      if (null != lastObj) {
	    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
	    		   }
	    	      if (null != curObj) {
	     			  curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
	    		   }
	    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
	    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
             	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
          	    temp2.append("<set      value='"+curValue+"' />\n");
          	    temp3.append("<set      value='"+lastValue+"' />\n");
            }   		
    	}
        temp1.append("</categories>\n");
        temp2.append("</dataset>\n");
        temp3.append("</dataset>\n");
        chartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
              " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
              " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
              " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
              " numdivlines='4' adjustDiv='0' rotateNames='0'"+
              " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'"+
              " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
   				" exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
        chartMap.append(temp1).append(temp2).append(temp3);
	    chartMap.append("</chart>");
    	return chartMap.toString();    	
  }
		
	
	//下拉框选取单个图形
	public String getSingleChart(Map<String, Object> map) {
		String shapeId = MapUtils.getString(map, "shapeId", null);
		String xml = "";
		if (shapeId.equals("1")) { // 拆线图
			xml = getLineChartXML(map);
		} else { // 柱状图
			xml = getAreaChartXML(map);
		}
		return xml;
	}
	
	/**yanhd end**/

    /**
     * 获取chart的Data
     *
     * @return
     */
    public Object[] getTableChart(String indexTypeId, String reportLevelId, String indexCd,
                                  String localCode, String areaId, String fieldName, int radio, String dateNo) {
        //定义一个boolean值，用来保存是否是新oa方法调用，这里对显示做一些调整
        boolean isNewOaPara = false;
        if (dateNo.endsWith("$")) {
            isNewOaPara = true;
            dateNo = dateNo.replace("$", "");
        }
        int cunt = 28;
        int jPoint = 2;
        if (radio == 2) {
            cunt = 63;
            jPoint = 3;
        } else if (radio == 3) {
            cunt = 91;
            jPoint = 7;
        } else {
        }

        if (fieldName == null || fieldName.equals("")) {
            fieldName = "Value2";
        }

        if (dateNo == null || dateNo.equals("")) {
            dateNo = getYesterday();
        }
        try {

            String startDayNo = getBeforeDay(dateNo, cunt);

            String lsDayNo = getBeforeDay(startDayNo, cunt);

            Integer year = (Integer.parseInt(dateNo.substring(0, 4)) - 1);
            String yesYear = year.toString() + dateNo.substring(4);
            String lsyesYearNo = year.toString() + startDayNo.substring(4);
            fieldName = fieldName.toUpperCase();
            //用于处理累计数据的时间
            String lastMoth = getMiniTime(dateNo, 1);
            //这里加一个数字的原因是因为，比如3。4月的前一个月 是2.4日，但是前28天只能查到2.7日的情况，所以要把数据查出来，但是日期不会影响，因为日期格式已固定
            String isIastMoth = getBeforeDay(lastMoth, cunt + 3);
            //获取当期
            List<Map<String, Object>> list1 = portalDAO.getChart(indexTypeId, localCode, areaId, indexCd, reportLevelId,
                    startDayNo, dateNo, fieldName);

            //获取上月同期
            //将累计数据做特殊处理
            List<Map<String, Object>> list2 = null;
            if (indexCd.equals("YHFZ_IND_5") || indexCd.equals("YHFZ_IND_6") || indexCd.equals("YHFZ_IND_7") ||
                    indexCd.equals("YHFZ_IND_8") || indexCd.equals("YWLL_IND_3") || indexCd.equals("YWLL_IND_4") ||
                    indexCd.equals("SRYC_IND_2") || indexCd.equals("SRYC_IND_5") || indexCd.equals("GWYH_IND_2") ||
                    indexCd.equals("GWYH_IND_3") || indexCd.equals("GWYH_IND_4")) {
                list2 = portalDAO.getChart(indexTypeId, localCode, areaId, indexCd, reportLevelId,
                        isIastMoth, lastMoth, fieldName);
            } else {
                list2 = portalDAO.getChart(indexTypeId, localCode, areaId, indexCd, reportLevelId,
                        lsDayNo, startDayNo, fieldName);
            }

            //获取上年同期
            List<Map<String, Object>> list3 = portalDAO.getChart(indexTypeId, localCode, areaId, indexCd, reportLevelId,
                    lsyesYearNo, yesYear, fieldName);

            //anchorRadius设置线点半径，lineThickness折线厚度 showhovercap='0'
            //String  data ="<chart yAxisValuesStep='2'  formatNumber='1' labelStep='"+jPoint+"' lineThickness='1' canvasBorderThickness='0' showBorder='0'  baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='0' hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' decimalPrecision='2' showhovercap='0' showValues='1' rotateValues='1' valuePosition='auto' numdivlines='5' numVdivlines='6' yaxisminvalue='10' yaxismaxvalue='1' rotateNames='0' chartRightMargin='20' chartLeftMargin='10'>";
            //			String data = "<graph caption='' subcaption=''  hovercapbg='FFECAA' hovercapborder='F47E00' "
            //					+ "formatNumber='0' labelStep='"
            //					+ jPoint
            //					+ "'  formatNumberScale='0' decimalPrecision='0' baseFontSize='12' showvalues='0' canvasBorderThickness='1' "
            //					+
            //					"numdivlines='3' numVdivlines='0'   yaxisminvalue='10' yaxismaxvalue='1'  rotateNames='1' chartRightMargin='20' "
            //					+
            //					"chartLeftMargin='10'>\n"; yAxisValuesStep='2' yaxismaxvalue='1'
            BigDecimal minValue = new BigDecimal(999999999);
            BigDecimal maxValue = new BigDecimal(-888888888);
            BigDecimal maXvalue = new BigDecimal(-88888888);
            boolean bool = false;
            for (int i = 0; i < list1.size(); i++) {
                BigDecimal val = ((BigDecimal) (list1.get(i).get(fieldName)));
                if (val.compareTo(maXvalue) == 0) {
                    list1.get(i).remove(fieldName);
                    list1.get(i).put(fieldName, "0");
                    val = new BigDecimal(0);
                }
                if (val.compareTo(minValue) == -1) minValue = val;
                if (val.compareTo(maxValue) == 1) maxValue = val;
                if (val.toString().indexOf(".") > 0) bool = true;
            }
            for (int i = 0; i < list2.size(); i++) {
                BigDecimal val = ((BigDecimal) (list2.get(i).get(fieldName)));
                if (val.compareTo(maXvalue) == 0) {
                    list2.get(i).remove(fieldName);
                    list2.get(i).put(fieldName, "0");
                    val = new BigDecimal(0);
                }
                if (val.compareTo(minValue) == -1) minValue = val;
                if (val.compareTo(maxValue) == 1) maxValue = val;
                if (val.toString().indexOf(".") > 0) bool = true;
            }
            for (int i = 0; i < list3.size(); i++) {
                BigDecimal val = ((BigDecimal) (list3.get(i).get(fieldName)));
                if (val.compareTo(maXvalue) == 0) {
                    list3.get(i).remove(fieldName);
                    list3.get(i).put(fieldName, "0");
                    val = new BigDecimal(0);
                }
                if (val.compareTo(minValue) == -1) minValue = val;
                if (val.compareTo(maxValue) == 1) maxValue = val;
                if (val.toString().indexOf(".") > 0) bool = true;
            }
            int minValueInt = minValue.intValue();
            if (!bool || isNewOaPara) {
                int step = maxValue.intValue() - minValue.intValue();
                if (step > 5) {
                    maxValue = BigDecimal.valueOf(maxValue.intValue() + (5 - step % 5));
                }
            }
            String data = "<chart   formatNumber='1' labelStep='"
                    + jPoint
                    + "' lineThickness='1' canvasBorderThickness='0' showBorder='0'  baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF'" +
                    " alternateHGridColor='FFFFFF' divLineAlpha='0' hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' " +
                    "showToolTip='1' showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto' numdivlines='4' adjustDiv='0' numVdivlines='6' " +
                    " yaxisminvalue='" + minValueInt + "' yaxismaxvalue='" + maxValue.intValue() + "' rotateNames='0' chartRightMargin='20' chartLeftMargin='10'>";
            //String data = "<chart caption='' subcaption='' hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' decimalPrecision='0' baseFontSize='12' showvalues='0' canvasBorderThickness='1' numdivlines='3' numVdivlines='0'  yaxisminvalue='10' yaxismaxvalue='1' useRoundEdges='1' rotateNames='1' chartRightMargin='20' chartLeftMargin='10'>\n";
            String timedata = "<categories>\n";
            String data_u = "<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='FF2121' seriesName='" + "当期值"
                    + "' color='FF2121'   anchorBorderColor='FFFFFF' >\n";
            String data_m = "<dataset  anchorRadius='4' anchorBgColor='0000FF' seriesName='" + "上月同期"
                    + "' color='0000FF' anchorBorderColor='FFFFFF'>\n";
            String data_v;
            if (list3.size() > 0) {
                data_v = "<dataset anchorSides='3' anchorRadius='5' anchorBgColor='00FF00' seriesName='" + "上年同期"
                        + "' color='00FF00' anchorBorderColor='FFFFFF'>\n";
            } else {
                data_v = null;
            }


            //			for (int i = 0; i < list1.size(); i++)
            //			{
            //				Map<String, Object> map1 = (Map<String, Object>) list1.get(i);
            //				data_u += "<set  value='" + map1.get(fieldName.toUpperCase()).toString() + "' hoverText='当期值 "
            //						+ map1.get(fieldName.toUpperCase()).toString() + "'/>\n";
            //
            //			}

            List lsArrMon = getDayArrayInMonth(dateNo, cunt);
            List lsArrMon1 = null;
            if (indexCd.equals("YHFZ_IND_5") || indexCd.equals("YHFZ_IND_6") || indexCd.equals("YHFZ_IND_7") ||
                    indexCd.equals("YHFZ_IND_8") || indexCd.equals("YWLL_IND_3") || indexCd.equals("YWLL_IND_4") ||
                    indexCd.equals("SRYC_IND_2") || indexCd.equals("SRYC_IND_5") || indexCd.equals("GWYH_IND_2") ||
                    indexCd.equals("GWYH_IND_3") || indexCd.equals("GWYH_IND_4")) {
                lsArrMon1 = getDayLastMo(lsArrMon);
                //lsArrMon1 = getDayArrayInMonth(lastMoth,cunt);
            } else {
                lsArrMon1 = getDayArrayInMonth(lsArrMon.get(0).toString(), cunt);
            }
            for (int i = 0; i < lsArrMon.size(); i++) {//必须显示30个日期，没有数据显示0

                Boolean v1Flag = true;
                Boolean v2Flag = true;
                Boolean v3Flag = true;
                Boolean v2pFlag = true;
                //判定是是否少一天
                //当期趋势线
                for (int m = 0; m < list1.size(); m++) {
                    Map<String, Object> map1 = (Map<String, Object>) list1.get(m);
                    //String temp1 = lsArrMon.get(i).toString();
                    if (map1.containsKey(fieldName) && map1.get("DATENO").equals(lsArrMon.get(i).toString())) {//如果此日期存在
                        try {
                            if (indexTypeId.toString().equals("12") && isNewOaPara && lsArrMon.get(i).toString().equals(dateNo)) {
                                data_u += "<set  value='-' hoverText='当期值：-'/>\n";
                            } else {
                                data_u += "<set  value='" + map1.get(fieldName).toString() + "' hoverText='当期值："
                                        + map1.get(fieldName).toString() + "'/>\n";
                            }
                        } catch (Exception e) {
                            Log.error(null, e);
                        }
                        v1Flag = false;
                    }
                }
                if (v1Flag) {
                    if (isNewOaPara && lsArrMon.get(i).toString().equals(dateNo)) {
                        //新OA传过来的参数判断
                        data_u += "<set  value='-' hoverText='当期值：-'/>\n";
                    } else {
                        data_u += "<set  value='0' hoverText='当期值：0'/>\n";
                    }

                }
                //上月同期趋势线
                for (int m = 0; m < list2.size(); m++) {
                    Map<String, Object> map2 = (Map<String, Object>) list2.get(m);
                    if (map2.containsKey(fieldName) && map2.get("DATENO").equals(lsArrMon1.get(i).toString())) {//如果此日期存在
                        data_m += "<set  value='" + map2.get(fieldName).toString() + "' hoverText='上月同期： "
                                + map2.get(fieldName).toString() + "'/>\n";
                        v2Flag = false;
                    }
                }
                if (v2Flag) {
                    if (i > 0) {
                        for (int p = 0; p < list2.size(); p++) {
                            Map<String, Object> map21 = (Map<String, Object>) list2.get(p);
                            if (map21.containsKey(fieldName) && map21.get("DATENO").equals(lsArrMon1.get(i - 1).toString())) {//如果此日期存在
                                data_m += "<set  value='" + map21.get(fieldName).toString() + "' hoverText='上月同期： "
                                        + map21.get(fieldName).toString() + "'/>\n";
                                v2pFlag = false;
                            }
                        }
                        if (v2pFlag) {
                            data_m += "<set  value='0' hoverText='上月同期：0'/>\n";
                        }
                    } else {
                        data_m += "<set  value='0' hoverText='上月同期：0'/>\n";
                    }

                }
                //上年同期趋势线
                if (data_v != null) {
                    for (int m = 0; m < list3.size(); m++) {
                        Map<String, Object> map3 = (Map<String, Object>) list3.get(m);
                        if (map3.containsKey(fieldName)
                                && map3.get("DATENO").toString().substring(4).equals(lsArrMon.get(i).toString().substring(4))) {//如果此日期存在
                            data_v += "<set  value='" + map3.get(fieldName).toString() + "' hoverText='上年同期："
                                    + map3.get(fieldName).toString() + "'/>\n";
                            v3Flag = false;
                        }
                    }
                    list3.clear();
                    if (v3Flag) {
                        data_v += "<set  value='" + "0" + "' hoverText='上年同期：0'/>\n";
                    }
                }
                //线条节点
                if (isNewOaPara) {
                    timedata += "<category name='" + lsArrMon.get(i).toString().substring(6, 8) + "' />\n";
                } else {
                    timedata += "<category name='" + lsArrMon.get(i).toString().substring(4, 6) + "."
                            + lsArrMon.get(i).toString().substring(6, 8) + "' />\n";
                }

                //timedata += lsArrMon.get(i).toString().substring(4, 6)+"."+lsArrMon.get(i).toString().substring(6, 8)+"|";
            }
            timedata += "</categories>\n";
            data_u += "</dataset>\n";
            data_m += "</dataset>\n";
            if (data_v != null) {
                data_v += "</dataset>\n";
                data += data_v;
            }
            data += timedata;
            data += data_u;
            data += data_m;
            data += "</chart>";
            Object[] rtnObj = new Object[2];
            rtnObj[0] = data;
            if ((reportLevelId != null && reportLevelId.equals("1"))
                    && (areaId == null || areaId.equals("") || areaId.equals("0"))) {
                rtnObj[1] = portalDAO.getMapData(dateNo, indexTypeId, indexCd, fieldName);
            } else {
                rtnObj[1] = null;
            }
            return rtnObj;
        } catch (Exception e) {
            Log.error(null, e);
            return null;
        }
    }

    /**
     * 获取值班人员
     *
     * @return
     */
    public String getDuty() {
        StringBuffer rtn = new StringBuffer();
        List list = portalDAO.getDuty();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Map record = (Map) iterator.next();
            rtn.append(record.get("PRINCIPAL_MAN"));
            rtn.append("&nbsp;");
            rtn.append(record.get("MOBILE"));
            rtn.append("&nbsp;&nbsp;");
        }
        return rtn.toString();
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
        Calendar cal = new GregorianCalendar(year, month_no - 1, day);

        String preDate = cal.get(Calendar.YEAR) + String.format("%1$02d", cal.get(Calendar.MONTH) + 1 - monNum)
                + String.format("%1$02d", cal.get(Calendar.DAY_OF_MONTH));
        return preDate;
    }

    /**
     * <p/>
     * 获取指定月份内的日期序列 曲线使用日期显示周期为30天
     *
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

    //获取上一个月的当前天，没有值也映射出来。
    private static List<String> getDayLastMo(List list) {
        List<String> days = new ArrayList<String>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String day = "";
                day += list.get(i).toString().substring(0, 4);
                int month_no = Integer.parseInt(list.get(i).toString().substring(4, 6)) - 1;
                if ((month_no + "").length() == 1 && month_no != 0) {
                    day += "0";
                    day += month_no;
                } else if (month_no == 0) {
                    day = "";
                    day += Integer.parseInt(list.get(i).toString().substring(0, 4)) - 1 + "";
                    day += "12";
                } else {
                    day += month_no;
                }
                int moth_day = Integer.parseInt(list.get(i).toString().substring(6, 8));
                if ((moth_day + "").length() == 1) {
                    day += "0";
                }
                day += moth_day + "";
                days.add(day);
            }
        }
        return days;
    }
    
	public static String getLastMon(String currentMon){
		String tempStr=currentMon.substring(4, currentMon.length());
		Integer retValue=0;
		if("01".equals(tempStr)){
			retValue=Integer.parseInt(currentMon)-89;
		}else{
			retValue=Integer.parseInt(currentMon)-1;
		}
		return String.valueOf(retValue);
	}
	
	/**yanhd start **/
	/**
	 * 最大值
	 */
	private double compareMaxValue(double maxVal, double curVal) {
		if (curVal > maxVal) {
			maxVal = curVal;
		}
		return maxVal;
	}
	/**
	 * 最小值
	 */
	private double compareMinValue(double minVal, double curVal) {
		if (minVal == 0 && curVal != 0) {
			minVal = curVal;
		} else if (curVal < minVal) {
			minVal = curVal;
		}
		return minVal;
	}
	/**yanhd end  **/
}
