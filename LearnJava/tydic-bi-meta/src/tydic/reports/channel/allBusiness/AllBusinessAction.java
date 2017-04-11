package tydic.reports.channel.allBusiness;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.portalCommon.DateUtil;
import tydic.reports.graph.GraphDAO;
/**
 * 
 * @author qx 定制化月报表
 * 
 */
public class AllBusinessAction {
	private AllBusinessDAO allBusinessDAO; 
	private GraphDAO graphDAO; 
	
	public GraphDAO getGraphDAO() {
		return graphDAO;
	}
	public void setGraphDAO(GraphDAO graphDAO) {
		this.graphDAO = graphDAO;
	}
	public AllBusinessDAO getAllBusinessDAO() {
		return allBusinessDAO;
	}

	public void setAllBusinessDAO(AllBusinessDAO allBusinessDAO) {
		this.allBusinessDAO = allBusinessDAO;
	}
	public List<Map<String,Object>> getSelectMon(String reportId){
		List<Map<String,Object>> list=null;
		/*if("1".equals(reportId)){
			list=allBusinessDAO.getSelectMon_Week();
		}*/if("2".equals(reportId)){
			list=allBusinessDAO.getSelectMon();
		}
		return list;
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
	//获取上周
	public  String getLastWeek(String currentMon,String currentWeek){
		String tempStr=currentMon.substring(4, currentMon.length());
		Integer lastMonth=0;
		String lastWeek="";
		Integer lastweek=0;
		if("01".equals(tempStr)){
			lastMonth=Integer.parseInt(currentMon)-89;
		}else{
			lastMonth=Integer.parseInt(currentMon)-1;
		}
		if("1".equals(currentWeek)){//最小周
			lastWeek=allBusinessDAO.queryMaxWeek(String.valueOf(lastMonth));
		}else{
			lastweek=Integer.parseInt(currentWeek)-1;
			lastWeek=lastweek+"";
		}
		return lastWeek;
	}
	/**qx start **/
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
	/**
	 * 获取最新显示的月份
	 */
	public String getNewMonth(String reportId) {
		try {
			AllBusinessDAO dao = new AllBusinessDAO();
			/*
             * 根据不同的报表Id取数   start by qx
             */
		/*if("1".equals(reportId)){
			return dao.getNewMonth_Week();
  		  }*/if("2".equals(reportId)){
			return dao.getNewMonth();
  		  }
		} catch (Exception e) {
			Log.error(null, e);
		}
		return "";
	} 	
	/*
	 * 获取周报周列表,最大周
	 */
/*	public Map getSelectWeek_Day(String monthNo) {
		Map<String, Object> map=new HashMap<String,Object>();
		 List<Map<String,Object>>weekList=allBusinessDAO.getSelectWeek_Day(monthNo);
		 map.put("weekList", weekList);
		return map;
	}*/
	
	//全渠道总体分析月报表
	public Map getChannelGlobal_Mon(Map<String, Object> queryData) {
		int numVdivlines =-1;
		StringBuffer  lineChartMap=new StringBuffer();
		Map<String, Object> map=new HashMap<String,Object>();
		/*
         * 根据不同的报表Id取数   start by qx
         */        
		String startDate="";
		String endDate="";
		String currDay="";
		int interval=0;
		String month3="";
		String week3="";
		String lastMonth3="";
		String lastWeek3="";
		if("1".equals(queryData.get("reportId").toString())){
			startDate =MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");
			endDate=MapUtils.getString(queryData, "endDate", null).replaceAll("-", "");
			currDay = String.valueOf(startDate).substring(6, 8);
			long interval1=DateUtil.getInterval(startDate,endDate);
			interval=(int)interval1;
		}if("2".equals(queryData.get("reportId").toString())){
			endDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
			startDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
		}if("3".equals(queryData.get("reportId").toString())){
			startDate="20130521";
			String queryWeek=MapUtils.getString(queryData, "dateTime", null);
			Map<String,Object> monthWeek=allBusinessDAO.queryMonthAndWeek(queryWeek);
			month3=String.valueOf(monthWeek.get("MONTH_NO")) ;//获取当前月份201305
			week3=String.valueOf(monthWeek.get("WEEK_NO"));//获取当前周 3
			lastMonth3=getLastMon(month3);//获取上月201304
			lastWeek3=getLastWeek(month3,week3);//获取上周2
			
		}
		 String currYear  = String.valueOf(startDate).substring(0, 4);//2013
	    // String currMonth = String.valueOf(startDate).substring(4, 6);//05
		 
	     //String showCurrentYEAR=currYear+currMonth;//当月
	     //String showLastYEAR=getLastMon(showCurrentYEAR);//上月
	     String showCurrentYEAR1 = startDate.substring(0, startDate.length()-2);// 当月
  	     String showLastYEAR1 = String.valueOf(Integer.parseInt(showCurrentYEAR1) - 1);// 去年同期
		
    	String field =       MapUtils.getString(queryData, "field",    "SERVICE_NUM");
    	String VcurrentColor= "FF0000";
    	String VlastColor=    "0066FF";
    	double maxVal=0;
        double minVal=0;
      	
      	StringBuffer  temp4=new StringBuffer();
      	temp4.append("<categories>\n");
      	StringBuffer  temp5=new StringBuffer();
      	StringBuffer  temp6=new StringBuffer();
        /*
         * 根据不同的报表Id取数   start by qx
         */
         List<Map<String,Object>> tabList=null;
         String tempMonth="";
         String lastBeginDate="";
         String lastEndDate="";
        if("1".equals(queryData.get("reportId").toString())){
        	 tabList=allBusinessDAO.getChannelGlobal_Week(queryData);//周报表格数据
             tempMonth=startDate;//20130511
  	         SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
  	         lastBeginDate=sdf.format(DateUtil.getDateOfLastMonth(tempMonth).getTime());//20130411
  	         String lastYear  = String.valueOf(lastBeginDate).substring(0, 4);
	         String lastMonth = String.valueOf(lastBeginDate).substring(4, 6);
	         String lastDay = String.valueOf(lastBeginDate).substring(6, 8);
	         String time=lastYear+"-"+lastMonth+"-"+lastDay;
  	         lastEndDate=DateUtil.lastDay(time, interval);
  	         lastBeginDate=Convert.toString(lastBeginDate).replace("-", "");
  	         lastEndDate=Convert.toString(lastEndDate).replace("-", "");
  	         
  	         temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VcurrentColor+"' seriesName='"+startDate+"—"+endDate+"'      color='"+VcurrentColor+"' anchorBorderColor='FFFFFF'>\n");
  	         temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VlastColor+"'    seriesName='"+lastBeginDate+"—"+lastEndDate+"'         color='"+VlastColor+"'    anchorBorderColor='FFFFFF'>\n");
        }if("2".equals(queryData.get("reportId").toString())){
             tabList=allBusinessDAO.getChannelGlobal_Mon(queryData);//月报表格数据
             tempMonth=currYear+"01";;//201201
             
             temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VcurrentColor+"' seriesName='"+showCurrentYEAR1+"'      color='"+VcurrentColor+"' anchorBorderColor='FFFFFF'>\n");
             temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VlastColor+"'    seriesName='"+showLastYEAR1+"'         color='"+VlastColor+"'    anchorBorderColor='FFFFFF'>\n");
         }if("3".equals(queryData.get("reportId").toString())){
        	 tabList=allBusinessDAO.getChannelGlobal_Day(queryData);//周报表格数据
             tempMonth="1";//201201 折线图
             
             temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VcurrentColor+"' seriesName='"+month3+"'      color='"+VcurrentColor+"' anchorBorderColor='FFFFFF'>\n");
             temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VlastColor+"'    seriesName='"+lastMonth3+"'        color='"+VlastColor+"'    anchorBorderColor='FFFFFF'>\n");
         }
       map.put("list", tabList);
       map.put("barChartMap", getAreaChartXML(queryData));//柱状图
	   //折线图
	   
 	   
       if("1".equals(queryData.get("reportId").toString())){
	         for (int i =0; i <= interval; i++) {
	        	  
	    	      String day = String.valueOf(tempMonth).substring(6, 8);//显示日期号
	    	      int showDay   =Integer.parseInt(day);//11

	    		  String currentValue="0";
	    		  String lastValue="0";

	    		  Map<String, Object> currentObj=null;
	    		  Map<String, Object> lastObj=null;
	    		 
	    		  currentObj = allBusinessDAO.getChartData_WeekLink(tempMonth,queryData);
		          lastObj =    allBusinessDAO.getChartData_WeekLink(lastBeginDate,queryData);
	    		  /*
	    		   * 根据不同的报表Id取数   end by qx
	    		   */
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
	    	     
	    	    	  temp4.append("<category name='"+showDay+"' />\n");
			          temp5.append("<set value='"+currentValue+"' hoverText='"+tempMonth+"： "+currentValue+"' />\n");
			    	  temp6.append("<set value='"+lastValue+"'    hoverText='"+lastBeginDate+"： "+lastValue+"' />\n");  
			    	  numVdivlines=numVdivlines+1;
			    	  //日期往后推一天
			    	  String lastYear=String.valueOf(lastBeginDate).substring(0, 4);
		    	      String lastMonth=String.valueOf(lastBeginDate).substring(4, 6);
		    	      String lastDay=String.valueOf(lastBeginDate).substring(6, 8);
		    	      String lastDate=lastYear+"-"+lastMonth+"-"+lastDay;
		    	      lastBeginDate=DateUtil.lastDay(lastDate,1);//上月开始日期20130411
		    	      lastBeginDate = Convert.toString(lastBeginDate).replace("-", "");
		    	      
		    	      String currentYear=String.valueOf(tempMonth).substring(0, 4);
		    	      String currentMonth=String.valueOf(tempMonth).substring(4, 6);
		    	      String currentDay=String.valueOf(tempMonth).substring(6, 8);
		    	      String currentDate=currentYear+"-"+currentMonth+"-"+currentDay;
		    	      tempMonth=DateUtil.lastDay(currentDate,1);
		    	      tempMonth = Convert.toString(tempMonth).replace("-", "");
		    	      
	    	 }
       }
       if("2".equals(queryData.get("reportId").toString())){//月报
	         for (int i = Integer.parseInt(tempMonth); i <= Integer.parseInt(endDate); i++) {
	    		  String year      =String.valueOf(i).substring(0, 4);
	    	      String month_no  = String.valueOf(i).substring(4, 6);
	    	      
	    	      int showDay   =Integer.parseInt(month_no);
	    	      String lastYear =String.valueOf(Integer.parseInt(year)-1)+month_no;   
	      
	    		  String currentValue="0";
	    		  String lastValue="0";
	    		  /*
	               * 根据不同的报表Id取数   start by qx
	               */
	    		  Map<String, Object> currentObj=null;
	    		  Map<String, Object> lastObj=null;
	    			  currentObj = allBusinessDAO.getChartData(String.valueOf(i),queryData);
		    		  lastObj =    allBusinessDAO.getChartData(lastYear,queryData);
	    		  /*
	    		   * 根据不同的报表Id取数   end by qx
	    		   */
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
	    	      
	    	      temp4.append("<category name='"+showDay+"月' />\n");
		          temp5.append("<set value='"+currentValue+"' hoverText='"+i +"： "+currentValue+"' />\n");
		    	  temp6.append("<set value='"+lastValue+"'    hoverText='"+lastYear+"： "+lastValue+"' />\n");  
		    	  numVdivlines=numVdivlines+1;
	    	 }
       }
       if("3".equals(queryData.get("reportId").toString())){//周报
	         for (int i = Integer.parseInt(tempMonth); i <= Integer.parseInt(week3); i++) {
	    	      int showDay   =i;

	    		  String currentValue="0";
	    		  String lastValue="0";
	    		  /*
	               * 根据不同的报表Id取数   start by qx
	               */
	    		  Map<String, Object> currentObj=null;
	    		  Map<String, Object> lastObj=null;
	    			  currentObj = allBusinessDAO.getChartData_Day(String.valueOf(i),month3,queryData);
		    		  lastObj =    allBusinessDAO.getChartData_Day(String.valueOf(i),lastMonth3,queryData);
	    		  /*
	    		   * 根据不同的报表Id取数   end by qx
	    		   */
	    	      if (null != currentObj) {
	    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
	    		   }else{
	    			   currentValue="0";
	    		   }
	    	      
	    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue));
	    	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue));
	    	      
	    	      if (null != lastObj) {
	    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
	    		   }else{
	    			   lastValue="0";
	    		   }
	    	      
	    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
	    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
	    	      
	    	      temp4.append("<category name='第"+showDay+"周' />\n");
		          temp5.append("<set value='"+currentValue+"' hoverText='"+month3+"第"+i+"周："+currentValue+"' />\n");
		    	  temp6.append("<set value='"+lastValue+"'    hoverText='"+lastMonth3+":第"+i+"周： "+lastValue+"' />\n");  
		    	  numVdivlines=numVdivlines+1;
	    	 }
     }
	         temp4.append("</categories>\n");
	         temp5.append("</dataset>\n");
	         temp6.append("</dataset>\n");
	     	
	         lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	     	        " numdivlines='4' adjustDiv='0' numVdivlines='"+numVdivlines+"'   rotateNames='0'"+
	     	        " chartRightMargin='20' chartLeftMargin='10'"+
	     	       " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	       			" exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	         
	         lineChartMap.append(temp4).append(temp5).append(temp6);
	         lineChartMap.append("</chart>");
	         map.put("lineChartMap", lineChartMap.toString());
         return map; 
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
		   //String changeZone=MapUtils.getString(map, "changeZone",    "0");
		   Map<String, Object> key=new HashMap<String, Object>();
		   //柱状图
		   map.put("currentColor", "FBC62A");
		   map.put("lastColor",    "71B359");
		   String strXML="";
		 //  if(changeZone.equals("0")){
		//	   strXML=get21AreaChartXML(map);
		 //  }else{
			   strXML=getAreaChartXML(map);
		//   }
		   key.put("XML",strXML);
		   return key;
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
		private String get21AreaChartXML(Map<String, Object> queryData) {
			StringBuffer  barChartMap=new StringBuffer();
			/*
	         * 根据不同的报表Id取数   start by qx
	         */
			String endDate="";
			String startDate="";
			String month3="";
			String week3="";
			String lastMonth3="";
			String lastWeek3="";
			String lastWeek="";
			String queryWeek=MapUtils.getString(queryData, "dateTime", null);
			int interval=0;//开始结束日期之间的时间间隔
			if("1".equals(queryData.get("reportId").toString())){
				startDate =MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""); //20130510
				endDate=MapUtils.getString(queryData, "endDate", null).replaceAll("-", "");//20130516
			}if("2".equals(queryData.get("reportId").toString())){
				endDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
				startDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
			}
			if("3".equals(queryData.get("reportId").toString())){
				startDate="20130521";
				
				
				String startTime=queryWeek.substring(0,8);
			    String endTime=queryWeek.substring(9,queryWeek.length());
				//开始日期往前推7天
			    String startYear=startTime.substring(0,4);
			    String startMon=startTime.substring(4,6);
			    String startDay=startTime.substring(6,8);
			    String startT=startYear+"-"+startMon+"-"+startDay;
			    String start=DateUtil.lastDay(startT, -7);
			    start=start.replaceAll("-", "");
			    
			    //结束日期往后前7天
			    String endYear=endTime.substring(0,4);
			    String endMon=endTime.substring(4,6);
			    String endDay=endTime.substring(6,8);
			    String endT=endYear+"-"+endMon+"-"+endDay;
			    String end=DateUtil.lastDay(endT, -7);
			    end=end.replaceAll("-", "");
			    lastWeek=start+"~"+end;
				
				
				Map<String,Object> monthWeek=allBusinessDAO.queryMonthAndWeek(queryWeek);
				month3=String.valueOf(monthWeek.get("MONTH_NO")) ;//获取当前月份201305
				week3=String.valueOf(monthWeek.get("WEEK_NO"));//获取当前周 3
				lastMonth3=getLastMon(month3);//获取上月201304
				lastWeek3=getLastWeek(month3,week3);//获取上周2
				
			}
			String currYear  = String.valueOf(startDate).substring(0, 4);
   	        String currMonth = String.valueOf(startDate).substring(4, 6);
   	        String currDay ="";
   	       if("1".equals(queryData.get("reportId").toString())){
   	         currDay = String.valueOf(startDate).substring(6, 8);
   	       }
	   	     if("1".equals(queryData.get("reportId").toString())){
				long interval1=DateUtil.getInterval(startDate,endDate);
				interval=(int)interval1;
	   	     }
			
	    	String field =       MapUtils.getString(queryData, "field", "SERVICE_NUM");
			String currentColor= "FBC62A";
	    	String lastColor=    "71B359";
	    	double maxVal=0;
	        double minVal=0;
	        
	        StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
	      	
	      	String showCurrentYEAR=currYear+currMonth;//当月
   	        String showLastYEAR=getLastMon(showCurrentYEAR);//上月
   	        
   	        String lastYEAREnd ="";
   	        String lastYEARStart="";
   	       if("1".equals(queryData.get("reportId").toString())){
   	    	lastYEARStart=showLastYEAR+currDay;//上月开始日期
   	        String lastYear  = String.valueOf(lastYEARStart).substring(0, 4);
	        String lastMonth = String.valueOf(lastYEARStart).substring(4, 6);
	        String lastDay = String.valueOf(lastYEARStart).substring(6, 8);
	        String time=lastYear+"-"+lastMonth+"-"+lastDay;
		    lastYEAREnd = DateUtil.lastDay(time,interval);//上月结束日期20130416
		    lastYEAREnd=Convert.toString(lastYEAREnd).replace("-", "");
   	       }
   	       if("1".equals(queryData.get("reportId").toString())){
	   	    	temp2.append("<dataset  seriesName='"+startDate+"—"+endDate+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
		        temp3.append("<dataset  seriesName='"+lastYEARStart+"—"+lastYEAREnd+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
		    }if("2".equals(queryData.get("reportId").toString())){
		    	 temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
			     temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
		    }if("3".equals(queryData.get("reportId").toString())){
		    	 temp2.append("<dataset  seriesName='"+queryWeek+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
			     temp3.append("<dataset  seriesName='"+lastWeek+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
		    }
	        /*
             * 根据不同的报表Id取数   start by qx
             */
	       List<Map<String,Object>> barList=null;
  		  if("1".equals(queryData.get("reportId").toString())){
  			 barList=allBusinessDAO.get21BarChannelGlobal_Week(queryData);//柱状图数据
  		  }if("2".equals(queryData.get("reportId").toString())){
  			 barList=allBusinessDAO.get21BarChannelGlobal_Mon(queryData);//柱状图数据
  		  }
  		if("3".equals(queryData.get("reportId").toString())){
 			 barList=allBusinessDAO.get21BarChannelGlobal_Day(queryData);//柱状图数据
 		  }
  		  /*
  		   * 根据不同的报表Id取数   end by qx
  		   */	        
	        //柱状图
	       for (Map<String, Object> key : barList){
	       	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
	   		  String lastValue="0";
	   		  Map<String,Object> param=new HashMap<String, Object>();
	   		  param.put("zoneCode",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
	   	      param.put("channelTypeCode",String.valueOf(queryData.get("channelTypeCode") == null ? "1": String.valueOf(queryData.get("channelTypeCode"))));
	   	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
	   	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
	   	        /*
	             * 根据不同的报表Id取数   start by qx
	             */
	   	       Map<String, Object> lastObj=null;
	  		  if("1".equals(queryData.get("reportId").toString())){
	  			lastObj =    allBusinessDAO.getChartData_Week(lastYEARStart,lastYEAREnd,param);
	  		  }if("2".equals(queryData.get("reportId").toString())){
	  			lastObj =    allBusinessDAO.getChartData(showLastYEAR,param);
	  		  }
	  		if("3".equals(queryData.get("reportId").toString())){
	  			  String month="";
	  			  if("1".equals(week3)){//最小周的上一周
	  				  month=lastMonth3;
	  			  }else{
	  				  month=month3;
	  			  }
	  			lastObj =    allBusinessDAO.getChartData_Day(lastWeek3,month,param);
	  		  }
	  		  /*
	  		   * 根据不同的报表Id取数   end by qx
	  		   */
	   		  if (null != lastObj) {
	   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
	   		   }
		    	    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		    	    minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
	        	temp1.append("<category label='"+key.get("REGION_NAME")+"'/>\n");
	     	    temp2.append("<set      value='"+curValue+"' />\n");
	     	    temp3.append("<set      value='"+lastValue+"' />\n");
	       } 
	       temp1.append("</categories>\n");
	       temp2.append("</dataset>\n");
	       temp3.append("</dataset>\n");
	       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	               " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	               " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	               " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	               " numdivlines='4' adjustDiv='0' rotateNames='0'"+
	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");        
	       barChartMap.append(temp1).append(temp2).append(temp3);
	       barChartMap.append("</chart>");
	      return barChartMap.toString();	
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
		private String getAreaChartXML(Map<String, Object> queryData) {
			StringBuffer  barChartMap=new StringBuffer();
			/*
	         * 根据不同的报表Id取数   start by qx
	         */
			String endDate="";
			String startDate="";
			String month3="";
			String week3="";
			String lastMonth3="";
			String lastWeek3="";
			
			String lastWeek="";
			String queryWeek=MapUtils.getString(queryData, "dateTime", null);
			
			int interval=0;//开始结束日期之间的时间间隔
			if("1".equals(queryData.get("reportId").toString())){
				startDate =MapUtils.getString(queryData, "startDate", null).replaceAll("-", ""); //20130510
				endDate=MapUtils.getString(queryData, "endDate", null).replaceAll("-", "");//20130516
			}if("2".equals(queryData.get("reportId").toString())){
				endDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
				startDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
			}if("3".equals(queryData.get("reportId").toString())){
				startDate="20130521";
				
			    String startTime=queryWeek.substring(0,8);
			    String endTime=queryWeek.substring(9,queryWeek.length());
			    
			    //开始日期往前推7天
			    String startYear=startTime.substring(0,4);
			    String startMon=startTime.substring(4,6);
			    String startDay=startTime.substring(6,8);
			    String startT=startYear+"-"+startMon+"-"+startDay;
			    String start=DateUtil.lastDay(startT, -7);
			    start=start.replaceAll("-", "");
			    
			    //结束日期往后前7天
			    String endYear=endTime.substring(0,4);
			    String endMon=endTime.substring(4,6);
			    String endDay=endTime.substring(6,8);
			    String endT=endYear+"-"+endMon+"-"+endDay;
			    String end=DateUtil.lastDay(endT, -7);
			    end=end.replaceAll("-", "");
			    
			    lastWeek=start+"~"+end;
			    
				Map<String,Object> monthWeek=allBusinessDAO.queryMonthAndWeek(queryWeek);
				month3=String.valueOf(monthWeek.get("MONTH_NO")) ;//获取当前月份201305
				week3=String.valueOf(monthWeek.get("WEEK_NO"));//获取当前周 3
				lastMonth3=getLastMon(month3);//获取上月201304
				lastWeek3=getLastWeek(month3,week3);//获取上周2	
			}
			String currYear  = String.valueOf(startDate).substring(0, 4);
   	        String currMonth = String.valueOf(startDate).substring(4, 6);
   	        String currDay ="";
   	       if("1".equals(queryData.get("reportId").toString())){
   	         currDay = String.valueOf(startDate).substring(6, 8);
   	       }
	   	     if("2".equals(queryData.get("reportId").toString())){
				long interval1=DateUtil.getInterval(startDate,endDate);
				interval=(int)interval1;
	   	     }
			
	    	String field =       MapUtils.getString(queryData, "field", "SERVICE_NUM");
			String currentColor= "FBC62A";
	    	String lastColor=    "71B359";
	        
	        StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
	      	
	      	String showCurrentYEAR=currYear+currMonth;//当月
   	        String showLastYEAR=getLastMon(showCurrentYEAR);//上月
   	        
   	        String lastYEAREnd ="";
   	        String lastYEARStart="";
   	       if("1".equals(queryData.get("reportId").toString())){
   	    	lastYEARStart=showLastYEAR+currDay;//上月开始日期
   	        String lastYear  = String.valueOf(lastYEARStart).substring(0, 4);
	        String lastMonth = String.valueOf(lastYEARStart).substring(4, 6);
	        String lastDay = String.valueOf(lastYEARStart).substring(6, 8);
	        String time=lastYear+"-"+lastMonth+"-"+lastDay;
		    lastYEAREnd = DateUtil.lastDay(time,interval);//上月结束日期20130416
		    lastYEAREnd=Convert.toString(lastYEAREnd).replace("-", "");
   	       }
   	       if("1".equals(queryData.get("reportId").toString())){
	   	    	temp2.append("<dataset  seriesName='"+startDate+"—"+endDate+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
		        temp3.append("<dataset  seriesName='"+lastYEARStart+"—"+lastYEAREnd+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
		    }if("2".equals(queryData.get("reportId").toString())){
		    	 temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
			     temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
		    }if("3".equals(queryData.get("reportId").toString())){
		    	 temp2.append("<dataset  seriesName='"+queryWeek+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
			     temp3.append("<dataset  seriesName='"+lastWeek+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
		    }
	        /*
             * 根据不同的报表Id取数   start by qx
             */
	       List<Map<String,Object>> barList=null;
  		  if("1".equals(queryData.get("reportId").toString())){
  			 barList=allBusinessDAO.getBarChannelGlobal_Week(queryData);//柱状图数据
  		  }if("2".equals(queryData.get("reportId").toString())){
  			 barList=graphDAO.getChangeZoneLevel(queryData);//柱状图数据
  		  }if("3".equals(queryData.get("reportId").toString())){
  			barList=graphDAO.getChangeZoneLevel(queryData);//柱状图数据
  		  }
  		  /*
  		   * 根据不同的报表Id取数   end by qx
  		   */	        
	        //柱状图
	       for (Map<String, Object> key : barList){
	       	  String curValue="0";
	   		  String lastValue="0";
	   		  Map<String,Object> param=new HashMap<String, Object>();
	   		  param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
	   		  param.put("channelTypeCode",String.valueOf(queryData.get("channelTypeCode") == null ? "1": String.valueOf(queryData.get("channelTypeCode"))));
	   	        /*
	             * 根据不同的报表Id取数   start by qx
	             */
	   	       Map<String, Object> lastObj=null;
	   	       Map<String, Object> curObj=null;
	  		  if("1".equals(queryData.get("reportId").toString())){
	  			lastObj =    allBusinessDAO.getChartData_Week(lastYEARStart,lastYEAREnd,param);
	  			curObj =    allBusinessDAO.getChartData_Week(lastYEARStart,lastYEAREnd,param);
	  		  }if("2".equals(queryData.get("reportId").toString())){
	  			lastObj =    allBusinessDAO.getChartData(showLastYEAR,param);
	  			curObj =    allBusinessDAO.getChartData(showCurrentYEAR,param);
	  		  }if("3".equals(queryData.get("reportId").toString())){
	  			  String month="";
	  			  if("1".equals(week3)){//最小周的上一周
	  				  month=lastMonth3;
	  			  }else{
	  				  month=month3;
	  			  }
	  			lastObj =    allBusinessDAO.getChartData_Day(lastWeek3,month,param);
	  			curObj =    allBusinessDAO.getChartData_Day(week3,month,param);
	  		  }
	  		  /*
	  		   * 根据不同的报表Id取数   end by qx
	  		   */
	   		  if (null != lastObj) {
	   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
	   		   }else{
	   			  lastValue="0";
	   		   }
	   		 if (null != curObj) {
	   	    	  curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
	   		   }else{
	   			  curValue="0";
	   		   }
	        	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
	     	    temp2.append("<set      value='"+curValue+"' />\n");
	     	    temp3.append("<set      value='"+lastValue+"' />\n");
	       } 
	       temp1.append("</categories>\n");
	       temp2.append("</dataset>\n");
	       temp3.append("</dataset>\n");
	       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	              " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	              " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	              " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	              " numdivlines='4' adjustDiv='0' rotateNames='0'"+
	              " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'"+
	              " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	       		  " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	       barChartMap.append(temp1).append(temp2).append(temp3);
	       barChartMap.append("</chart>");
	      return barChartMap.toString();	
	  }
		//渠道服务_存储过程
		public Map getChannelSer_pg (Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();//参数map
			map.putAll(allBusinessDAO.getChannelSer_pg(queryData));//客户保有报表——存储过程实现
			map.put("barChartMap", getAreaChartXML(queryData,"0"));//柱状图
			map.put("lineChartMap", getChannelSerLine(queryData));//折线图
			 return map; 
		}
		//渠道服务_切换地市
		public Map<String, Object> loadSet21AreaChart_ChannelSer(Map<String, Object> map){
			  // String changeZone=MapUtils.getString(map, "changeZone",    null);//0代表五大区，1代表下级地市
			   Map<String, Object> key=new HashMap<String, Object>(); 
			   String strXML=getAreaChartXML(map,"0");
			   key.put("XML",strXML);
			   return key;
		   }
		//渠道服务_指标切换
		public Map getChannelSerChange(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();//参数map
			map.put("barChartMap", getAreaChartXML(queryData,"0"));//柱状图
		    map.put("lineChartMap", getChannelSerLine(queryData));//折线图
	        return map; 
		}
		/**
		 *   柱状图XML
		  * 方法描述：
		  * @param: 
		  * @return: 
		  * @version: 1.0
		  * @author: qx
		  * @version: 2013-8-6下午05:22:26
		 */
		private String getAreaChartXML(Map<String, Object> queryData,String changeZone) {
			StringBuffer  barChartMap=new StringBuffer();
			//柱状图展示
	    	String field =       MapUtils.getString(queryData, "field",null);
	    	String zoneCode =       MapUtils.getString(queryData, "zoneCode",null);
			String currentColor= "FBC62A";//当月
	    	String lastColor=    "71B359";//上月
	        
	        StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();

			String rptIndex=MapUtils.getString(queryData, "rptIndex", null);
			Map<String,Object> param=new HashMap<String, Object>();
			
		    List<Map<String,Object>>barList=null;
		    if("channelSerMonitorDay".equals(rptIndex)){//渠道服务类指标日监测报表
				String dataDate=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月日
				String year      =String.valueOf(dataDate).substring(0, 4);
		   	    String month_no = String.valueOf(dataDate).substring(4, 6);
		   	    String day      = String.valueOf(dataDate).substring(6, 8);
		   	     
	    	  	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    	    String lastDay=sdf.format(DateUtil.getDateOfLastMonth(year+month_no+day).getTime());
				
			    temp2.append("<dataset  seriesName='" + dataDate + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
			    temp3.append("<dataset  seriesName='" + lastDay + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
			   
			    List<Map<String,Object>>barList1=allBusinessDAO.getChartData_ChannelSerMonitorDayByDate(dataDate,queryData);//本月
				List<Map<String,Object>>barList2=allBusinessDAO.getChartData_ChannelSerMonitorDayByDate(lastDay,queryData);//上月
				
			        //柱状图_周报表
				for (Map<String, Object> key1 : barList1) {
					String curValue = "0";
					curValue = String.valueOf(key1.get(field) == null ? "0": key1.get(field));
					temp1.append("<category label='" + key1.get("ZONE_NAME")+ "'/>\n");
					temp2.append("<set      value='" + curValue + "' />\n");
				}for (Map<String, Object> key2 : barList2) {
					String lastValue = "0";
				    lastValue = String.valueOf(key2.get(field) == null ? "0": key2.get(field));
					temp3.append("<set      value='" + lastValue + "' />\n");
				}
			}
	       temp1.append("</categories>\n");
	       temp2.append("</dataset>\n");
	       temp3.append("</dataset>\n");
	       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	       " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	       " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	       " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	       " numdivlines='4' adjustDiv='0' rotateNames='0'"+
	       " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'"+ 
	       " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
    		" exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	       barChartMap.append(temp1).append(temp2).append(temp3);
	       barChartMap.append("</chart>");
	      return barChartMap.toString();	
	  }
		//渠道服务_折线图
		public String getChannelSerLine(Map<String, Object> queryData) {
			int numVdivlines =-1;
			double maxVal=0;
	        double minVal=0;
			StringBuffer  lineChartMap=new StringBuffer();//折线图			
		    //图形展示字段
	    	String field =       MapUtils.getString(queryData, "field",null);
	    	String currentColor= "FF0000";//当月红色
	    	String lastColor=    "0066FF";//上月蓝色

	      	StringBuffer  temp1=new StringBuffer();
	      	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
			
			String rptIndex=MapUtils.getString(queryData, "rptIndex", null);
			//日报表
     		if("channelSerMonitorDay".equals(rptIndex)){ //渠道服务类指标日监测报表
				String inDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前日期
				String inYearMon=inDate.substring(0,6);
				String inStartDate=inYearMon+"01";
				String inDay=inDate.substring(6,8);
				String lastYearMon=DateUtil.getLastMon(inYearMon);	
				String lastStartDate=lastYearMon+"01";
				String lastEndDate=lastYearMon+inDay;
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
		        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		       
		        List<Map<String, Object>> currentList=null;
		        List<Map<String, Object>> lastList=null;
		        currentList = allBusinessDAO.getChartData_ChannelSerMonitorDaySumLine(inStartDate,inDate,queryData);
		        lastList =    allBusinessDAO.getChartData_ChannelSerMonitorDaySumLine(lastStartDate,lastEndDate,queryData);
		        int date1=1;
		        int date2=1;
		        for (Map<String, Object> key1 : currentList){
		        	  String show="";
		    	      if(date1<10){
		    	    	  show="0"+date1;
		    	      }else{
		    	    	  show=date1+"";
		    	      }
			       	  String curValue=String.valueOf(key1.get(field) == null ? "0": key1.get(field));
			       	  maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
			       	  temp1.append("<category name='"+date1+"' />\n");
			          temp2.append("<set value='"+curValue+"' hoverText='"+(inYearMon+show)+"： "+curValue+"' />\n");
			    	  numVdivlines++;
			    	  date1++;
			       }
		        for (Map<String, Object> key2 : lastList){
		        	  String show="";
		    	      if(date2<10){
		    	    	  show="0"+date2;
		    	      }else{
		    	    	  show=date2+"";
		    	      }
			       	  String lastValue=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
			          maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
			  	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
			          temp3.append("<set value='"+lastValue+"' hoverText='"+(lastYearMon+show)+"： "+lastValue+"' />\n");
			    	  numVdivlines++;
			    	  date2++;
			       }
		         temp1.append("</categories>\n");
		         temp2.append("</dataset>\n");
		         temp3.append("</dataset>\n");
		     	
		         lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
		     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
		     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
		     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
		     	        " numdivlines='4' adjustDiv='0' yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"'   rotateNames='0'"+
		     	        " chartRightMargin='20' chartLeftMargin='10'"+
		     	       " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
		         		" exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
		         lineChartMap.append(temp1).append(temp2).append(temp3);
		         lineChartMap.append("</chart>");
		}
     		 return lineChartMap.toString(); 
}
}
