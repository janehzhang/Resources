package tydic.reports.channel.allBusiness;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.yhd.utils.Pager;
import tydic.portalCommon.DateUtil;
import tydic.portalCommon.procedure.ts.list.RptSkipCmplDetailDAO;
import tydic.reports.graph.GraphDAO;
/**
 * 
 * @author qx 定制化月报表
 * 
 */
public class AllChannelAction {
	private AllChannelDAO allChannelDAO; 
	private GraphDAO graphDAO; 

	public AllChannelDAO getAllChannelDAO() {
		return allChannelDAO;
	}
	public void setAllChannelDAO(AllChannelDAO allChannelDAO) {
		this.allChannelDAO = allChannelDAO;
	}
	public GraphDAO getGraphDAO() {
		return graphDAO;
	}
	public void setGraphDAO(GraphDAO graphDAO) {
		this.graphDAO = graphDAO;
	}
	public List<Map<String,Object>> getSelectMon(String reportId){
		List<Map<String,Object>> list=null;
		if("2".equals(reportId)){
			list=allChannelDAO.getSelectMon();
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
			lastWeek=allChannelDAO.queryMaxWeek(String.valueOf(lastMonth));
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
			AllBusinessDAO_New dao = new AllBusinessDAO_New();
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
	
	//全渠道服务日、月、周
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
		String month1="";
		String lastMonth1="";
		int interval=0;
		String month3="";
		String week3="";
		String lastMonth3="";
		String lastWeek3="";
		String percentFlag="0";
		if("1".equals(queryData.get("reportId").toString())){
			
			startDate =MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
			endDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
			currDay = String.valueOf(startDate).substring(6, 8);
			startDate=String.valueOf(startDate).substring(0,6)+"01";
			long interval1=DateUtil.getInterval(startDate,endDate);
			interval=(int)interval1;
			month1=startDate.substring(0,6);
			lastMonth1=getLastMon(month1);//获取上月201304
		}if("2".equals(queryData.get("reportId").toString())){
			endDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
			startDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
		}if("3".equals(queryData.get("reportId").toString())){
			startDate="20130521";
			String queryWeek=MapUtils.getString(queryData, "dateTime", null);
			Map<String,Object> monthWeek=allChannelDAO.queryMonthAndWeek(queryWeek);
			month3=String.valueOf(monthWeek.get("MONTH_NO")) ;//获取当前月份201305
			week3=String.valueOf(monthWeek.get("WEEK_NO"));//获取当前周 3
			lastMonth3=getLastMon(month3);//获取上月201304
			lastWeek3=getLastWeek(month3,week3);//获取上周2
			
		}
		 String currYear  = String.valueOf(startDate).substring(0, 4);//2013
	     String showCurrentYEAR1 = startDate.substring(0, startDate.length()-2);// 当月
  	     String showLastYEAR1 = String.valueOf(Integer.parseInt(showCurrentYEAR1) - 1);// 去年同期
		
  	     String field =       MapUtils.getString(queryData, "field");
  	     if(field.equals("PERC_NUM")){
  	    	 percentFlag="1";
  	     }
  	     else{
  	    	percentFlag="0";
  	     }
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
        	 //tabList=allChannelDAO.getChannelGlobal_Week(queryData);//日报表格数据
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
  	         
  	         temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VcurrentColor+"' seriesName='"+month1+"'      color='"+VcurrentColor+"' anchorBorderColor='FFFFFF'>\n");
  	         temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VlastColor+"'    seriesName='"+lastMonth1+"'         color='"+VlastColor+"'    anchorBorderColor='FFFFFF'>\n");
        }if("2".equals(queryData.get("reportId").toString())){
             //tabList=allChannelDAO.getChannelGlobal_Mon(queryData);//月报表格数据
             tempMonth=currYear+"01";;//201201
             
             temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VcurrentColor+"' seriesName='"+showCurrentYEAR1+"'      color='"+VcurrentColor+"' anchorBorderColor='FFFFFF'>\n");
             temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VlastColor+"'    seriesName='"+showLastYEAR1+"'         color='"+VlastColor+"'    anchorBorderColor='FFFFFF'>\n");
         }if("3".equals(queryData.get("reportId").toString())){
        	 //tabList=allChannelDAO.getChannelGlobal_Day(queryData);//周报表格数据
             tempMonth="1";//201201 折线图
             
             temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VcurrentColor+"' seriesName='"+month3+"'      color='"+VcurrentColor+"' anchorBorderColor='FFFFFF'>\n");
             temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VlastColor+"'    seriesName='"+lastMonth3+"'        color='"+VlastColor+"'    anchorBorderColor='FFFFFF'>\n");
         }
         map.putAll(allChannelDAO.getChannelGlobalSer_Pg(queryData));//全渠道服务——存储过程实现
 	   
       if("1".equals(queryData.get("reportId").toString())){
	         for (int i =0; i <= interval; i++) {
	        	  
	    	      String day = String.valueOf(tempMonth).substring(6, 8);//显示日期号
	    	      int showDay   =Integer.parseInt(day);//11

	    		  String currentValue="0";
	    		  String lastValue="0";

	    		  Map<String, Object> currentObj=null;
	    		  Map<String, Object> lastObj=null;
	    		 
	    		  currentObj = allChannelDAO.getChartData_WeekLink(tempMonth,queryData);
		          lastObj =    allChannelDAO.getChartData_WeekLink(lastBeginDate,queryData);
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
	    			  currentObj = allChannelDAO.getChartData(String.valueOf(i),queryData);
		    		  lastObj =    allChannelDAO.getChartData(lastYear,queryData);
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
	    			  currentObj = allChannelDAO.getChartData_Day(String.valueOf(i),month3,queryData);
		    		  lastObj =    allChannelDAO.getChartData_Day(String.valueOf(i),lastMonth3,queryData);
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
	         if("1".equals(queryData.get("reportId").toString())){
	         StringBuffer  pieChartMap=new StringBuffer();
				//饼图展示
		      	StringBuffer  temp=new StringBuffer();
			    List<Map<String,Object>>barList=null;
			    field =MapUtils.getString(queryData, "field",null);
			    barList = allChannelDAO.getAllBussinessPieList_Day(queryData);
			    	for (Map<String, Object> key : barList) {
				    	String reasonName = String.valueOf(key.get("CHANNEL_TYPE_NAME") == null ? "-": key.get("CHANNEL_TYPE_NAME"));
				    	String reasonCode = String.valueOf(key.get("CHANNEL_TYPE_CODE") == null ? "10": key.get("CHANNEL_TYPE_CODE"));
				    	String curValue=new String();
				    	curValue = String.valueOf(key.get(field) == null ? "0":  key.get(field));
				    	Map<String, Object>  dimLevel=allChannelDAO.getChannel(reasonCode,"1");
				    	if(dimLevel.get("DIM_LEVEL").equals("2")){
				    		if(queryData.get("channelTypeCode").equals("1")){
				    			temp.append("<set label='"+reasonName+"' value='"+curValue+"'/> ");
				    		}
				    		else{
				    			temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-myJs('1')\"/> ");
				    		}
				    	}
				    	else{
				    		temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-myJs('"+reasonCode+"')\"/> ");
				    	}
				    }
			    	Map<String, Object> channelData=allChannelDAO.getChannel(Convert.toString(queryData.get("channelTypeCode")),"1");
			    	String caption= Convert.toString(channelData.get("CHANNEL_TYPE_NAME"));
			    	if(queryData.get("showAllChannel").equals("true")&&queryData.get("channelTypeCode").equals("1")){
			    		caption="渠道小类";
			    	}
			    	else if(queryData.get("showAllChannel").equals("false")&&queryData.get("channelTypeCode").equals("1")){
			    		caption="渠道大类";
			    	}
			    	pieChartMap.append("<chart caption='"+caption+"' animation='1' showBorder='1' startingAngle='70' " +
		       		"bgAngle='360' bgRatio='0,100' bgColor='99ccff,ffffff' manageLabelOverflow='1' " +
		       		"pieRadius='152' enableSmartLabels='1' slicingDistance='30' " +
		       		"decimals='2' palette='4' formatNumberScale='0' showPercentValues='"+percentFlag+"' " +
		       		" exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
		            " exportFileName='onePic' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
			   pieChartMap.append(temp);
			   pieChartMap.append("</chart>");
		      map.put("pieChartMap", pieChartMap.toString());
	         }
	         if("3".equals(queryData.get("reportId").toString())){
		         StringBuffer  pieChartMap=new StringBuffer();
					//饼图展示
			      	StringBuffer  temp=new StringBuffer();
				    List<Map<String,Object>>barList=null;
				    field =MapUtils.getString(queryData, "field",null);
					//宽带新装不满意原因TOP饼图
				    barList = allChannelDAO.getAllBussinessPieList_Week(queryData);
				    for (Map<String, Object> key : barList) {
				    	String reasonName = String.valueOf(key.get("CHANNEL_TYPE_NAME") == null ? "-": key.get("CHANNEL_TYPE_NAME"));
				    	String reasonCode = String.valueOf(key.get("CHANNEL_TYPE_CODE") == null ? "10": key.get("CHANNEL_TYPE_CODE"));
				    	String curValue=new String();
				    	curValue = String.valueOf(key.get(field) == null ? "0":  key.get(field));
				    	Map<String, Object>  dimLevel=allChannelDAO.getChannel(reasonCode,"1");
				    	if(dimLevel.get("DIM_LEVEL").equals("2")){
				    		if(queryData.get("channelTypeCode").equals("1")){
				    			temp.append("<set label='"+reasonName+"' value='"+curValue+"'/> ");
				    		}
				    		else{
				    			temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-myJs('1')\"/> ");
				    		}
				    	}
				    	else{
				    		temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-myJs('"+reasonCode+"')\"/> ");
				    	}
				    }
				    Map<String, Object> channelData=allChannelDAO.getChannel(Convert.toString(queryData.get("channelTypeCode")),"1");
			    	String caption= Convert.toString(channelData.get("CHANNEL_TYPE_NAME"));
			    	if(queryData.get("showAllChannel").equals("true")&&queryData.get("channelTypeCode").equals("1")){
			    		caption="渠道小类";
			    	}
			    	else if(queryData.get("showAllChannel").equals("false")&&queryData.get("channelTypeCode").equals("1")){
			    		caption="渠道大类";
			    	}
				   pieChartMap.append("<chart caption='"+caption+"' animation='1' showBorder='1' startingAngle='70' " +
			       		"bgAngle='360' bgRatio='0,100' bgColor='99ccff,ffffff' manageLabelOverflow='1' " +
			       		"pieRadius='152' enableSmartLabels='1' slicingDistance='30' " +
			       		"decimals='2' palette='4' formatNumberScale='0' showPercentValues='"+percentFlag+"' " +
			       		" exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
			            " exportFileName='onePic' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
				   pieChartMap.append(temp);
				   pieChartMap.append("</chart>");
			      map.put("pieChartMap", pieChartMap.toString());
		         }
	         if("2".equals(queryData.get("reportId").toString())){
		         StringBuffer  pieChartMap=new StringBuffer();
					//饼图展示
			      	StringBuffer  temp=new StringBuffer();
				    List<Map<String,Object>>barList=null;
				    field =MapUtils.getString(queryData, "field",null);
					//宽带新装不满意原因TOP饼图
				    barList = allChannelDAO.getAllBussinessPieList_Mon(queryData);
				    for (Map<String, Object> key : barList) {
				    	String reasonName = String.valueOf(key.get("CHANNEL_TYPE_NAME") == null ? "-": key.get("CHANNEL_TYPE_NAME"));
				    	String reasonCode = String.valueOf(key.get("CHANNEL_TYPE_CODE") == null ? "10": key.get("CHANNEL_TYPE_CODE"));
				    	String curValue=new String();
				    	curValue = String.valueOf(key.get(field) == null ? "0":  key.get(field));
				    	Map<String, Object>  dimLevel=allChannelDAO.getChannel(reasonCode,"1");
				    	if(dimLevel.get("DIM_LEVEL").equals("2")){
				    		if(queryData.get("channelTypeCode").equals("1")){
				    			temp.append("<set label='"+reasonName+"' value='"+curValue+"'/> ");
				    		}
				    		else{
				    			temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-myJs('1')\"/> ");
				    		}
				    	}
				    	else{
				    		temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-myJs('"+reasonCode+"')\"/> ");
				    	}
				    }
				    Map<String, Object> channelData=allChannelDAO.getChannel(Convert.toString(queryData.get("channelTypeCode")),"1");
			    	String caption= Convert.toString(channelData.get("CHANNEL_TYPE_NAME"));
			    	if(queryData.get("showAllChannel").equals("true")&&queryData.get("channelTypeCode").equals("1")){
			    		caption="渠道小类";
			    	}
			    	else if(queryData.get("showAllChannel").equals("false")&&queryData.get("channelTypeCode").equals("1")){
			    		caption="渠道大类";
			    	}
				   pieChartMap.append("<chart caption='"+caption+"' animation='1' showBorder='1' startingAngle='70' " +
			       		"bgAngle='360' bgRatio='0,100' bgColor='99ccff,ffffff' manageLabelOverflow='1' " +
			       		"pieRadius='152' enableSmartLabels='1' slicingDistance='30' " +
			       		"decimals='2' palette='4' formatNumberScale='0' showPercentValues='"+percentFlag+"' " +
			       		" exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
			            " exportFileName='onePic' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
				   pieChartMap.append(temp);
				   pieChartMap.append("</chart>");
			      map.put("pieChartMap", pieChartMap.toString());
		         }
         return map; 
	}
	public Map getChannelGlobal_Zone(Map<String, Object> queryData) {
		int numVdivlines =-1;
		StringBuffer  lineChartMap=new StringBuffer();
		Map<String, Object> map=new HashMap<String,Object>();
		/*
         * 根据不同的报表Id取数   start by qx
         */        
		String startDate="";
		String endDate="";
		String currDay="";
		String month1="";
		String lastMonth1="";
		int interval=0;
		String month3="";
		String week3="";
		String lastMonth3="";
		String lastWeek3="";
		String percentFlag="0";
		if("1".equals(queryData.get("reportId").toString())){
			
			startDate =MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
			endDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
			currDay = String.valueOf(startDate).substring(6, 8);
			startDate=String.valueOf(startDate).substring(0,6)+"01";
			long interval1=DateUtil.getInterval(startDate,endDate);
			interval=(int)interval1;
			month1=startDate.substring(0,6);
			lastMonth1=getLastMon(month1);//获取上月201304
		}if("2".equals(queryData.get("reportId").toString())){
			endDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
			startDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
		}if("3".equals(queryData.get("reportId").toString())){
			startDate="20130521";
			String queryWeek=MapUtils.getString(queryData, "dateTime", null);
			Map<String,Object> monthWeek=allChannelDAO.queryMonthAndWeek(queryWeek);
			month3=String.valueOf(monthWeek.get("MONTH_NO")) ;//获取当前月份201305
			week3=String.valueOf(monthWeek.get("WEEK_NO"));//获取当前周 3
			lastMonth3=getLastMon(month3);//获取上月201304
			lastWeek3=getLastWeek(month3,week3);//获取上周2
			
		}
		 String currYear  = String.valueOf(startDate).substring(0, 4);//2013
	     String showCurrentYEAR1 = startDate.substring(0, startDate.length()-2);// 当月
  	     String showLastYEAR1 = String.valueOf(Integer.parseInt(showCurrentYEAR1) - 1);// 去年同期
		
  	     String field =       MapUtils.getString(queryData, "field");
  	     if(field.equals("PERC_NUM")){
  	    	 percentFlag="1";
  	     }
  	     else{
  	    	percentFlag="0";
  	     }
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
        	 tabList=allChannelDAO.getChannelGlobal_WeekZone(queryData);//日报表格数据
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
  	         
  	         temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VcurrentColor+"' seriesName='"+month1+"'      color='"+VcurrentColor+"' anchorBorderColor='FFFFFF'>\n");
  	         temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VlastColor+"'    seriesName='"+lastMonth1+"'         color='"+VlastColor+"'    anchorBorderColor='FFFFFF'>\n");
        }if("2".equals(queryData.get("reportId").toString())){
             tabList=allChannelDAO.getChannelGlobalZone_Mon(queryData);//月报表格数据
             tempMonth=currYear+"01";;//201201
             
             temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VcurrentColor+"' seriesName='"+showCurrentYEAR1+"'      color='"+VcurrentColor+"' anchorBorderColor='FFFFFF'>\n");
             temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VlastColor+"'    seriesName='"+showLastYEAR1+"'         color='"+VlastColor+"'    anchorBorderColor='FFFFFF'>\n");
         }if("3".equals(queryData.get("reportId").toString())){
        	 tabList=allChannelDAO.getChannelGlobal_Week2(queryData);//周报表格数据
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
	    		 
	    		  currentObj = allChannelDAO.getChartData_WeekLink(tempMonth,queryData);
		          lastObj =    allChannelDAO.getChartData_WeekLink(lastBeginDate,queryData);
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
	    			  currentObj = allChannelDAO.getChartData(String.valueOf(i),queryData);
		    		  lastObj =    allChannelDAO.getChartData(lastYear,queryData);
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
	    			  currentObj = allChannelDAO.getChartData_Day(String.valueOf(i),month3,queryData);
		    		  lastObj =    allChannelDAO.getChartData_Day(String.valueOf(i),lastMonth3,queryData);
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
		   Map<String, Object> key=new HashMap<String, Object>();
		   //柱状图
		   map.put("currentColor", "FBC62A");
		   map.put("lastColor",    "71B359");
		   String strXML="";
		   strXML=getAreaChartXML(map);
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
				
				
				Map<String,Object> monthWeek=allChannelDAO.queryMonthAndWeek(queryWeek);
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
  			 barList=allChannelDAO.get21BarChannelGlobal_Week(queryData);//柱状图数据
  		  }if("2".equals(queryData.get("reportId").toString())){
  			 barList=allChannelDAO.get21BarChannelGlobal_Mon(queryData);//柱状图数据
  		  }
  		if("3".equals(queryData.get("reportId").toString())){
 			 barList=allChannelDAO.get21BarChannelGlobal_Day(queryData);//柱状图数据
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
	  			lastObj =    allChannelDAO.getChartData_Week(lastYEARStart,lastYEAREnd,param);
	  		  }if("2".equals(queryData.get("reportId").toString())){
	  			lastObj =    allChannelDAO.getChartData(showLastYEAR,param);
	  		  }
	  		if("3".equals(queryData.get("reportId").toString())){
	  			  String month="";
	  			  if("1".equals(week3)){//最小周的上一周
	  				  month=lastMonth3;
	  			  }else{
	  				  month=month3;
	  			  }
	  			lastObj =    allChannelDAO.getChartData_Day(lastWeek3,month,param);
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
				startDate =MapUtils.getString(queryData, "dateTime", null).replaceAll("-", ""); //20130510
				endDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//20130516
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
			    queryData.put("lastWeek", lastWeek);
			    
				Map<String,Object> monthWeek=allChannelDAO.queryMonthAndWeek(queryWeek);
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
	   	    	temp2.append("<dataset  seriesName='"+startDate+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
		        temp3.append("<dataset  seriesName='"+lastYEAREnd+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
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
	       List<Map<String,Object>> barList2=null;
  		  if("1".equals(queryData.get("reportId").toString())){
  			 queryData.put("startDate", startDate);
  			 queryData.put("endDate", endDate);
  			 barList=allChannelDAO.getBarChannelGlobal_Week(queryData);//柱状图数据
  		  }if("2".equals(queryData.get("reportId").toString())){
  			barList=allChannelDAO.getBarChannelGlobalZone_Mon(queryData);//柱状图数据
  			queryData.put("lastMon", showLastYEAR);
  			barList2=allChannelDAO.getBarChannelGlobalZone_Mon2(queryData);//柱状图数据
  		  }if("3".equals(queryData.get("reportId").toString())){
  			barList=allChannelDAO.getBarChannelGlobal_Week2(queryData);//柱状图数据
  			barList2=allChannelDAO.getBarChannelGlobal_WeekLast(queryData);//柱状图数据
  		  }
  		  /*
  		   * 根据不同的报表Id取数   end by qx
  		   */	        
	        //柱状图
  		int i=0;
	       for (Map<String, Object> key : barList){
	       	  String curValue="0";
	   		  String lastValue="0";
	   		  Map<String,Object> param=new HashMap<String, Object>();
	   		  param.put("zoneCode",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
	   		  param.put("channelTypeCode",String.valueOf(queryData.get("channelTypeCode") == null ? "1": String.valueOf(queryData.get("channelTypeCode"))));
	   	        /*
	             * 根据不同的报表Id取数   start by qx
	             */
	   	       Map<String, Object> lastObj=null;
	   	       Map<String, Object> curObj=null;
	  		  if("1".equals(queryData.get("reportId").toString())){
	  			lastObj =    allChannelDAO.getChartData_Week(lastYEAREnd,lastYEAREnd,param);
	  			curObj =    allChannelDAO.getChartData_Week(startDate,startDate,param);
	  		  }if("2".equals(queryData.get("reportId").toString())){
	  			 if(barList2.size()!=0){
	  				lastObj=barList2.get(i++);
	  			 }
	  			curObj=key;
	  		  }if("3".equals(queryData.get("reportId").toString())){
	  			  String month="";
	  			  if("1".equals(week3)){//最小周的上一周
	  				  month=lastMonth3;
	  			  }else{
	  				  month=month3;
	  			  }
	  			  if(barList2.size()!=0){
	  				  lastObj=barList2.get(i++);
	  			  }
	  			curObj=key;
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
	        	temp1.append("<category label='"+key.get("REGION_NAME")+"'/>\n");
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
			map.putAll(allChannelDAO.getChannelSer_pg(queryData));//客户保有报表——存储过程实现
			map.put("barChartMap", getAreaChartXML(queryData,"0"));//柱状图
			map.put("lineChartMap", getChannelSerLine(queryData));//折线图
			 return map; 
		}
		//渠道服务_切换地市
		public Map<String, Object> loadSet21AreaChart_ChannelSer(Map<String, Object> map){
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
			   
			    List<Map<String,Object>>barList1=allChannelDAO.getChartData_ChannelSerMonitorDayByDate(dataDate,queryData);//本月
				List<Map<String,Object>>barList2=allChannelDAO.getChartData_ChannelSerMonitorDayByDate(lastDay,queryData);//上月
				
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
		        currentList = allChannelDAO.getChartData_ChannelSerMonitorDaySumLine(inStartDate,inDate,queryData);
		        lastList =    allChannelDAO.getChartData_ChannelSerMonitorDaySumLine(lastStartDate,lastEndDate,queryData);
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
		 
	 ////新渠道服务报表方法
		
		//渠道服务分类报表_存储过程
		public Map getChannelSerClass_Pg(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			map.putAll(allChannelDAO.getChannelSerClass_Pg(queryData));
			map.put("lineChartMap", getChannelLineChart(queryData));//折线图
			map.put("pieChartMap",getChannelPieChart(queryData) );//饼图
	        return map; 
		}
		//渠道服务分类(一级)报表_存储过程
		public Map getChannelSerClassFirst_Pg(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			map.putAll(allChannelDAO.getChannelSerClassFirst_Pg(queryData));
			map.put("lineChartMap", getChannelLineChart_FirstSecThi(queryData));//折线图
			map.put("pieChartMap",getChannelPieChart_FirstSecThi(queryData) );//饼图
	        return map; 
		}
		//渠道服务分类(二级)报表_存储过程
		public Map getChannelSerClassSecond_Pg(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			map.putAll(allChannelDAO.getChannelSerClassSecond_Pg(queryData));
			map.put("lineChartMap", getChannelLineChart_FirstSecThi(queryData));//折线图
			map.put("pieChartMap",getChannelPieChart_FirstSecThi(queryData) );//饼图
	        return map; 
		}
		//渠道服务分类(三级)报表_存储过程
		public Map getChannelSerClassThird_Pg(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			map.putAll(allChannelDAO.getChannelSerClassThird_Pg(queryData));
			map.put("lineChartMap", getChannelLineChart_FirstSecThi(queryData));//折线图
			map.put("pieChartMap",getChannelPieChart_FirstSecThi(queryData) );//饼图
	        return map; 
		}
		//渠道服务分类报表_图形变化
		public Map getChannelSerClass_Chart(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("lineChartMap", getChannelLineChart(queryData));//折线图
			map.put("pieChartMap",getChannelPieChart(queryData) );//饼图
	        return map; 
		}
		//渠道服务分类报表_图形变化(一、二、三级)
		public Map getChannelSerClass_Chart_FirstSecThi(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("lineChartMap", getChannelLineChart_FirstSecThi(queryData));//折线图
			map.put("pieChartMap",getChannelPieChart_FirstSecThi(queryData) );//饼图
	        return map; 
		}
		public String getParChannel(String channelCode,String actType){
			Map<String,Object> par=allChannelDAO.getChannel(channelCode,actType);
			return Convert.toString(par.get("CHANNEL_TYPE_PAR_CODE"));
		}
		//渠道服务分类报表_折线图
		private String getChannelLineChart(Map<String, Object> queryData) {
			int numVdivlines =-1;
			StringBuffer  lineChartMap=new StringBuffer();//折线图
			Map<String, Object> map=new HashMap<String,Object>();//参数map
			
		    //图形展示字段
	    	String field =       MapUtils.getString(queryData, "field",null);
	    	String currentColor= "FF0000";//当月红色
	    	String lastColor=    "0066FF";//上月蓝色
	    	
	    	double maxVal=0;
	        double minVal=0;

	      	StringBuffer  temp1=new StringBuffer();
	      	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
	      	
			String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
			
			String tempStart="1";//折线图起点
			if("1".equals(dateType)){//周报
			    //周报表
				String queryWeek=MapUtils.getString(queryData, "dateTime", null);
				Map<String,Object> monthWeek=allChannelDAO.queryMonthAndWeek(queryWeek);
				String month=String.valueOf(monthWeek.get("MONTH_NO")) ;//获取当前月份
				String week=String.valueOf(monthWeek.get("WEEK_NO"));//获取当前周 
				String lastMonth=DateUtil.getLastMon(month);//获取上月
				
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+month+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
		        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastMonth+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
				 for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(week); i++) {
		    	      int showDay   =i;

		    		  String currentValue="0";
		    		  String lastValue="0";
		    		  /*
		               * 根据不同的报表Id取数   start by qx
		               */
		    		  Map<String, Object> currentObj=null;
		    		  Map<String, Object> lastObj=null;
		    			  currentObj = allChannelDAO.getChartDataWeek_ChannelSerClass(String.valueOf(i),month,queryData);
			    		  lastObj =    allChannelDAO.getChartDataWeek_ChannelSerClass(String.valueOf(i),lastMonth,queryData);
		    		  /*
		    		   * 根据不同的报表Id取数   end by qx
		    		   */
		    	      if (null != currentObj) {
		    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
		    		   }else{
		    			   currentValue="0";
		    		   }
		    	      
		    	      if (null != lastObj) {
		    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		    		   }else{
		    			   lastValue="0";
		    		   }    
		    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue));
		    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
		    	      temp1.append("<category name='第"+showDay+"周' />\n");
			          temp2.append("<set value='"+currentValue+"' hoverText='"+month+"第"+i+"周："+currentValue+"' />\n");
			    	  temp3.append("<set value='"+lastValue+"'    hoverText='"+lastMonth+":第"+i+"周： "+lastValue+"' />\n");  
			    	  numVdivlines=numVdivlines+1;
		    	 }
			}if("2".equals(dateType)){//月报
				String inMonth=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前月
				String month=inMonth.substring(4,6);
				String year=inMonth.substring(0,4);
				String lastMonth=(Integer.parseInt(year)-1)+month;//去年月
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inMonth+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
		        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastMonth+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		        for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(month); i++) {
		    	      int showDay   =i;
		    	      String show="";
		    	      if(showDay<10){
		    	    	  show="0"+showDay;
		    	      }else{
		    	    	  show=showDay+"";
		    	      }
		    	      String surMon=year+show;//当月
		    	      String lastMon=(Integer.parseInt(year)-1)+show;//去年月
		    		  String currentValue="0";
		    		  String lastValue="0";
		    		  /*
		               * 根据不同的报表Id取数   start by qx
		               */
		    		  Map<String, Object> currentObj=null;
		    		  Map<String, Object> lastObj=null;
		    			  currentObj = allChannelDAO.getChartData_ChannelSerClass(surMon,queryData);
			    		  lastObj =    allChannelDAO.getChartData_ChannelSerClass(lastMon,queryData);
		    		  /*
		    		   * 根据不同的报表Id取数   end by qx
		    		   */
		    	      if (null != currentObj) {
		    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
		    		   }else{
		    			   currentValue="0";
		    		   }
		    	      
		    	      if (null != lastObj) {
		    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		    		   }else{
		    			   lastValue="0";
		    		   }   
		    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue));
		    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
		    	      temp1.append("<category name='"+showDay+"月' />\n");
			          temp2.append("<set value='"+currentValue+"' hoverText='"+surMon+"："+currentValue+"' />\n");
			    	  temp3.append("<set value='"+lastValue+"'    hoverText='"+lastMon+"： "+lastValue+"' />\n");  
			    	  numVdivlines=numVdivlines+1;
		    	 }
			}if("0".equals(dateType)){//日报
				String inDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前日期
				String inYearMon=inDate.substring(0,6);
				String inDay=inDate.substring(6,8);
				String lastYearMon=DateUtil.getLastMon(inYearMon);
				
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
		        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		        for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(inDay); i++) {
		    	      int showDay   =i;
		    	      String show="";
		    	      if(showDay<10){
		    	    	  show="0"+showDay;
		    	      }else{
		    	    	  show=showDay+"";
		    	      }
		    	      String surDay=inYearMon+show;//当月日	    	      
		  		  	  String year      =String.valueOf(surDay).substring(0, 4);
		  	   	      String month_no = String.valueOf(surDay).substring(4, 6);
		  	   	      String day      = String.valueOf(surDay).substring(6, 8);
		  	   	     
		      	  	  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		      	      String lastDay=sdf.format(DateUtil.getDateOfLastMonth(year+month_no+day).getTime());
		    	      
		    		  String currentValue="0";
		    		  String lastValue="0";
		    		  /*
		               * 根据不同的报表Id取数   start by qx
		               */
		    		  Map<String, Object> currentObj=null;
		    		  Map<String, Object> lastObj=null;
		    			  currentObj = allChannelDAO.getChartData_ChannelSerClass(surDay,queryData);
			    		  lastObj =    allChannelDAO.getChartData_ChannelSerClass(lastDay,queryData);
		    		  /*
		    		   * 根据不同的报表Id取数   end by qx
		    		   */
		    	      if (null != currentObj) {
		    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
		    		   }else{
		    			   currentValue="0";
		    		   }
		    	      
		    	      if (null != lastObj) {
		    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		    		   }else{
		    			   lastValue="0";
		    		   } 
		    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue));
		    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
		    	      temp1.append("<category name='"+showDay+"' />\n");
			          temp2.append("<set value='"+currentValue+"' hoverText='"+surDay+"："+currentValue+"' />\n");
			    	  temp3.append("<set value='"+lastValue+"'    hoverText='"+lastDay+"： "+lastValue+"' />\n");  
			    	  numVdivlines=numVdivlines+1;
		    	 }
			}
		         temp1.append("</categories>\n");
		         temp2.append("</dataset>\n");
		         temp3.append("</dataset>\n");//yaxisminvalue='10' yaxismaxvalue='1'double maxVal=0; double minVal=0;
		         lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
		     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
		     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
		     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
		     	        " numdivlines='4' adjustDiv='0'  yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' rotateNames='0'"+
		     	        " chartRightMargin='20' chartLeftMargin='10'" +
		     	       " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	         		" exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");  
		         lineChartMap.append(temp1).append(temp2).append(temp3);
		         lineChartMap.append("</chart>");
	         return lineChartMap.toString(); 
		}
		//渠道服务分类报表_折线图(一级、二级、三级)
		private String getChannelLineChart_FirstSecThi(Map<String, Object> queryData) {
			int numVdivlines =-1;
			StringBuffer  lineChartMap=new StringBuffer();//折线图
			Map<String, Object> map=new HashMap<String,Object>();//参数map
			
		    //图形展示字段
	    	String field =       MapUtils.getString(queryData, "field",null);
	    	String currentColor= "FF0000";//当月红色
	    	String lastColor=    "0066FF";//上月蓝色
	    	
	    	double maxVal=0;
	        double minVal=0;

	      	StringBuffer  temp1=new StringBuffer();
	      	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
	      	
			String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
			
			String tempStart1="0";//折线图起点
			String tempStart="1";//折线图起点
			if("1".equals(dateType)){//周报
			    //周报表
				String queryWeek=MapUtils.getString(queryData, "dateTime", null);
				String curWeekArray=allChannelDAO.getCurWeekArray(queryWeek);
				String []curWeekArr=curWeekArray.split(",");
				String lastWeekArray=allChannelDAO.getLastWeekArray(queryWeek);
				String []lastWeekArr=lastWeekArray.split(",");
				
				Map<String,Object> monthNo=allChannelDAO.getMonth(queryWeek);
				String month=String.valueOf(monthNo.get("MONTH_NO")) ;//获取当前月份
				String lastMonth=DateUtil.getLastMon(month);//获取上月
				
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+month+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
		        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastMonth+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		        int curShowDay   =1;
		        int lastShowDay   =1;
		        for (int i = curWeekArr.length ; i > Integer.parseInt(tempStart1); i--) {
		    		  String currentValue="0";
		    		  Map<String, Object> currentObj=null;
		    			  currentObj = allChannelDAO.getChartDataWeek_FirstSecThi(curWeekArr[i-1],queryData);
		    	      if (null != currentObj) {
		    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
		    		   }else{
		    			   currentValue="0";
		    		   }  
		    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue));
		    	      temp1.append("<category name='第"+curShowDay+"周' />\n");
			          temp2.append("<set value='"+currentValue+"' hoverText='"+month+"第"+curShowDay+"周："+currentValue+"' />\n");
			    	  numVdivlines=numVdivlines+1;
			    	  curShowDay++;
		    	 }
		        if(curWeekArr.length>lastWeekArr.length){
		        	 for (int i = lastWeekArr.length ; i >=Integer.parseInt(tempStart); i--) {
		        	String lastValue="0";
		    		  Map<String, Object> lastObj=null;
		    			  lastObj = allChannelDAO.getChartDataWeek_FirstSecThi(lastWeekArr[i-1],queryData);
		    	      if (null != lastObj) {
		    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		    		   }else{
		    			   lastValue="0";
		    		   }  
		    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
			          temp3.append("<set value='"+lastValue+"' hoverText='"+lastMonth+"第"+lastShowDay+"周："+lastValue+"' />\n");
			    	  numVdivlines=numVdivlines+1;
			    	  lastShowDay++;
		        	 }
		        	 temp3.append("<set value='0' hoverText='"+lastMonth+"第"+curWeekArr.length+"周：0' />\n");
	    		  }else{
				 for (int i = curWeekArr.length ; i >=Integer.parseInt(tempStart); i--) {
		    		  String lastValue="0";
		    		  Map<String, Object> lastObj=null;
		    			  lastObj = allChannelDAO.getChartDataWeek_FirstSecThi(lastWeekArr[i-1],queryData);
		    	      if (null != lastObj) {
		    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		    		   }else{
		    			   lastValue="0";
		    		   }  
		    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
			          temp3.append("<set value='"+lastValue+"' hoverText='"+lastMonth+"第"+lastShowDay+"周："+lastValue+"' />\n");
			    	  numVdivlines=numVdivlines+1;
			    	  lastShowDay++;
		    	 }
	    		  }
			}if("2".equals(dateType)){//月报
				String inMonth=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前月
				String month=inMonth.substring(4,6);
				String year=inMonth.substring(0,4);
				String lastMonth=(Integer.parseInt(year)-1)+month;//去年月
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inMonth+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
		        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastMonth+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		        for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(month); i++) {
		    	      int showDay   =i;
		    	      String show="";
		    	      if(showDay<10){
		    	    	  show="0"+showDay;
		    	      }else{
		    	    	  show=showDay+"";
		    	      }
		    	      String surMon=year+show;//当月
		    	      String lastMon=(Integer.parseInt(year)-1)+show;//去年月
		    		  String currentValue="0";
		    		  String lastValue="0";
		    		  /*
		               * 根据不同的报表Id取数   start by qx
		               */
		    		  Map<String, Object> currentObj=null;
		    		  Map<String, Object> lastObj=null;
		    			  currentObj = allChannelDAO.getChartData_FirstSecThi(surMon,queryData);
			    		  lastObj =    allChannelDAO.getChartData_FirstSecThi(lastMon,queryData);
		    		  /*
		    		   * 根据不同的报表Id取数   end by qx
		    		   */
		    	      if (null != currentObj) {
		    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
		    		   }else{
		    			   currentValue="0";
		    		   }
		    	      
		    	      if (null != lastObj) {
		    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		    		   }else{
		    			   lastValue="0";
		    		   }   
		    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue));
		    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
		    	      temp1.append("<category name='"+showDay+"月' />\n");
			          temp2.append("<set value='"+currentValue+"' hoverText='"+surMon+"："+currentValue+"' />\n");
			    	  temp3.append("<set value='"+lastValue+"'    hoverText='"+lastMon+"： "+lastValue+"' />\n");  
			    	  numVdivlines=numVdivlines+1;
		    	 }
			}if("0".equals(dateType)){//日报
				String inDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前日期
				String inYearMon=inDate.substring(0,6);
				String inDay=inDate.substring(6,8);
				String lastYearMon=DateUtil.getLastMon(inYearMon);
				
				temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
		        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		        for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(inDay); i++) {
		    	      int showDay   =i;
		    	      String show="";
		    	      if(showDay<10){
		    	    	  show="0"+showDay;
		    	      }else{
		    	    	  show=showDay+"";
		    	      }
		    	      String surDay=inYearMon+show;//当月日	    	      
		  		  	  String year      =String.valueOf(surDay).substring(0, 4);
		  	   	      String month_no = String.valueOf(surDay).substring(4, 6);
		  	   	      String day      = String.valueOf(surDay).substring(6, 8);
		  	   	     
		      	  	  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		      	      String lastDay=sdf.format(DateUtil.getDateOfLastMonth(year+month_no+day).getTime());
		    	      
		    		  String currentValue="0";
		    		  String lastValue="0";
		    		  /*
		               * 根据不同的报表Id取数   start by qx
		               */
		    		  Map<String, Object> currentObj=null;
		    		  Map<String, Object> lastObj=null;
		    			  currentObj = allChannelDAO.getChartData_FirstSecThi(surDay,queryData);
			    		  lastObj =    allChannelDAO.getChartData_FirstSecThi(lastDay,queryData);
		    		  /*
		    		   * 根据不同的报表Id取数   end by qx
		    		   */
		    	      if (null != currentObj) {
		    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
		    		   }else{
		    			   currentValue="0";
		    		   }
		    	      
		    	      if (null != lastObj) {
		    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		    		   }else{
		    			   lastValue="0";
		    		   } 
		    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(currentValue));
		    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		    	      minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
		    	      temp1.append("<category name='"+showDay+"' />\n");
			          temp2.append("<set value='"+currentValue+"' hoverText='"+surDay+"："+currentValue+"' />\n");
			    	  temp3.append("<set value='"+lastValue+"'    hoverText='"+lastDay+"： "+lastValue+"' />\n");  
			    	  numVdivlines=numVdivlines+1;
		    	 }
			}
		         temp1.append("</categories>\n");
		         temp2.append("</dataset>\n");
		         temp3.append("</dataset>\n");//yaxisminvalue='10' yaxismaxvalue='1'double maxVal=0; double minVal=0;
		         lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
		     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
		     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
		     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
		     	        " numdivlines='4' adjustDiv='0'  yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' rotateNames='0'"+
		     	        " chartRightMargin='20' chartLeftMargin='10'" +
		     	        " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	         		    " exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");   
		         lineChartMap.append(temp1).append(temp2).append(temp3);
		         lineChartMap.append("</chart>");
	         return lineChartMap.toString(); 
		}
		public String getChannelPieChart(Map<String,Object>queryData){
			StringBuffer  pieChartMap=new StringBuffer();//折线图
			Map<String, Object> map=new HashMap<String,Object>();//参数map
		    //图形展示字段
	    	String field =       MapUtils.getString(queryData, "field",null);
			 List<Map<String,Object>>barList=null;
					//饼图展示
			      	StringBuffer  temp=new StringBuffer();
				    barList = allChannelDAO.getChannelPieList(queryData);
				    	for (Map<String, Object> key : barList) {
					    	String reasonName = String.valueOf(key.get("CHANNEL_TYPE_NAME") == null ? "-": key.get("CHANNEL_TYPE_NAME"));
					    	String reasonCode = String.valueOf(key.get("CHANNEL_TYPE_CODE") == null ? "10": key.get("CHANNEL_TYPE_CODE"));
					    	String curValue=new String();
					    	curValue = String.valueOf(key.get(field) == null ? "0":  key.get(field));
					    	Map<String, Object>  dimLevel=allChannelDAO.getChannel(reasonCode,Convert.toString(queryData.get("actType")));
					    	if(dimLevel.get("DIM_LEVEL").equals("2")){
					    		if(queryData.get("channelTypeCode").equals("1")){
					    			temp.append("<set label='"+reasonName+"' value='"+curValue+"'/> ");
					    		}
					    		else{
					    			temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-myJs('1')\"/> ");
					    		}
					    	}
					    	else{
					    		temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-myJs('"+reasonCode+"')\"/> ");
					    	}
					    }
				    	Map<String, Object> channelData=allChannelDAO.getChannel(Convert.toString(queryData.get("channelTypeCode")),Convert.toString(queryData.get("actType")));
				    	String caption= Convert.toString(channelData.get("CHANNEL_TYPE_NAME"));
				    	if(queryData.get("showAllChannel").equals("true")&&queryData.get("channelTypeCode").equals("1")){
				    		caption="渠道小类";
				    	}
				    	else if(queryData.get("showAllChannel").equals("false")&&queryData.get("channelTypeCode").equals("1")){
				    		caption="渠道大类";
				    	}
				    	pieChartMap.append("<chart caption='"+caption+"' animation='1' showBorder='1' startingAngle='70' " +
			       		"bgAngle='360' bgRatio='0,100' bgColor='99ccff,ffffff' manageLabelOverflow='1' " +
			       		"pieRadius='152' enableSmartLabels='1' slicingDistance='30' " +
			       		"decimals='2' palette='4' formatNumberScale='0' showPercentValues='0' " +
			       		" exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
			            " exportFileName='onePic' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
				   pieChartMap.append(temp);
				   pieChartMap.append("</chart>"); 
	         return pieChartMap.toString(); 
		}
		public String getChannelPieChart_FirstSecThi(Map<String,Object>queryData){
			StringBuffer  pieChartMap=new StringBuffer();//折线图
			Map<String, Object> map=new HashMap<String,Object>();//参数map
		    //图形展示字段
	    	String field =       MapUtils.getString(queryData, "field",null);
			 List<Map<String,Object>>barList=null;
					//饼图展示
			      	StringBuffer  temp=new StringBuffer();
				    barList = allChannelDAO.getChannelPieList_FirstSecThi(queryData);
				    	for (Map<String, Object> key : barList) {
					    	String reasonName = String.valueOf(key.get("CHANNEL_TYPE_NAME") == null ? "-": key.get("CHANNEL_TYPE_NAME"));
					    	String reasonCode = String.valueOf(key.get("CHANNEL_TYPE_CODE") == null ? "10": key.get("CHANNEL_TYPE_CODE"));
					    	String curValue=new String();
					    	curValue = String.valueOf(key.get(field) == null ? "0":  key.get(field));
					    	Map<String, Object>  dimLevel=allChannelDAO.getChannel(reasonCode,Convert.toString(queryData.get("actType")));
					    	if(dimLevel.get("DIM_LEVEL").equals("2")){
					    		if(queryData.get("channelTypeCode").equals("1")){
					    			temp.append("<set label='"+reasonName+"' value='"+curValue+"'/> ");
					    		}
					    		else{
					    			temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-myJs('1')\"/> ");
					    		}
					    	}
					    	else{
					    		temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-myJs('"+reasonCode+"')\"/> ");
					    	}
					    }
				    	Map<String, Object> channelData=allChannelDAO.getChannel(Convert.toString(queryData.get("channelTypeCode")),Convert.toString(queryData.get("actType")));
				    	String caption= Convert.toString(channelData.get("CHANNEL_TYPE_NAME"));
				    	if(queryData.get("showAllChannel").equals("true")&&queryData.get("channelTypeCode").equals("1")){
				    		caption="渠道小类";
				    	}
				    	else if(queryData.get("showAllChannel").equals("false")&&queryData.get("channelTypeCode").equals("1")){
				    		caption="渠道大类";
				    	}
				   pieChartMap.append("<chart caption='"+caption+"' animation='1' showBorder='1' startingAngle='70' " +
				       		"bgAngle='360' bgRatio='0,100' bgColor='99ccff,ffffff' manageLabelOverflow='1' " +
				       		"pieRadius='152' enableSmartLabels='1' slicingDistance='30' " +
				       		"decimals='0' palette='4' formatNumberScale='0' " +
				       		" exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
				            " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
					   pieChartMap.append(temp);
					   pieChartMap.append("</chart>");
	         return pieChartMap.toString(); 
		}
		public Map getChannelKeyIndex_Pg(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			map.putAll(allChannelDAO.getChannelKeyIndex_Pg(queryData));
	        return map; 
		}
		public Map<String, Object> getTableData(Map<String, Object> paramMap) {
			Pager page = Pager.getInstance();
			page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
			page.setSize(Convert.toInt(paramMap.get("pageCount")));
			return allChannelDAO.getTableData(paramMap, page);
		}
		public Map<String, Object> getIndexTableData(Map<String, Object> paramMap) {
			Pager page = Pager.getInstance();
			page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
			page.setSize(Convert.toInt(paramMap.get("pageCount")));
			return allChannelDAO.getIndexTableData(paramMap, page);
		}
		/**
		 * 导出数据
		 */
		public Map<String, Object> expTxtData(Map<String, Object> paramMap) {
			Pager page = (Pager) paramMap.get("page");
			AllChannelDAO allChannelDAO = new AllChannelDAO();
			return allChannelDAO.getTableData(paramMap, page);// 记录
		}
		public Map<String, Object> expIndexData(Map<String, Object> paramMap) {
			Pager page = (Pager) paramMap.get("page");
			AllChannelDAO allChannelDAO = new AllChannelDAO();
			return allChannelDAO.getIndexTableData(paramMap, page);// 记录
		}
		//渠道服务总体目标_存储过程
		public Map getChannelOverGoals_Pg (Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();//参数map
			map.putAll(allChannelDAO.getChannelOverGoals_Pg(queryData));//渠道服务总体目标
			map.put("barChartMap", getAreaChartXML_ChannnelOverGoals(queryData));//柱状图
			 return map; 
		}
		//渠道服务偏好_存储过程
		public Map queryChannelPrefer_pg (Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();//参数map
			map.putAll(allChannelDAO.queryChannelPrefer_pg(queryData));//渠道服务总体目标
			 return map; 
		}
		//渠道服务总体目标柱状图
		private String getAreaChartXML_ChannnelOverGoals(Map<String, Object> queryData) {
			StringBuffer  barChartMap=new StringBuffer();
			//柱状图展示
	    	String field =       MapUtils.getString(queryData, "field",null);
			String currentColor= "FBC62A";//当周
	    	String lastColor=    "71B359";//上周
	        
	        StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();

			String dateType=MapUtils.getString(queryData, "dateType", null);
		    List<Map<String,Object>>barList=null;
			if("0".equals(dateType)){//渠道服务总体目标日报
			    temp2.append("<dataset  seriesName='目标' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
			    temp3.append("<dataset  seriesName='占比'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
			    barList=allChannelDAO.getBarChannnelOverGoals(queryData);//日报
			     //日报
			       for (Map<String, Object> key : barList){
			   		  String lastValue = "0" ;
			   		  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
			   		  String channelName=String.valueOf(key.get("CHANNEL_NAME")==null?"0":key.get("CHANNEL_NAME"));
			   		  if("新媒体渠道".equals(channelName)||channelName=="新媒体渠道"){
			   			lastValue="42";
			   		  }else{
			   			lastValue="75";
			   		  }
			          temp1.append("<category label='"+key.get("CHANNEL_NAME")+"'/>\n");
			     	  temp2.append("<set      value='"+lastValue+"' />\n");
			     	  temp3.append("<set      value='"+curValue+"' />\n");
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
	       " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1' " +
	       " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	       " exportFileName='onePic' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");    
	       barChartMap.append(temp1).append(temp2).append(temp3);
	       barChartMap.append("</chart>");
	      return barChartMap.toString();	
	  }
}
