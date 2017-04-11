package tydic.reports.customerKeep.serviceProcessAnalysis;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.yhd.utils.Pager;
import tydic.portalCommon.DateUtil;
import tydic.reports.ReportsMonDAO;
import tydic.reports.graph.GraphDAO;
/**
 * 
 * @author qx 定制化月报表
 * 
 */
public class ServiceReActiveAction {
	private ServiceReActiveDAO serviceReActiveDAO; 
    private GraphDAO graphDAO; 
	
	public GraphDAO getGraphDAO() {
		return graphDAO;
	}
	public void setGraphDAO(GraphDAO graphDAO) {
		this.graphDAO = graphDAO;
	}

	public ServiceReActiveDAO getServiceReActiveDAO() {
		return serviceReActiveDAO;
	}

	public void setServiceReActiveDAO(ServiceReActiveDAO serviceReActiveDAO) {
		this.serviceReActiveDAO = serviceReActiveDAO;
	}

	/**
	 * 获取最新显示的月份
	 */
	public String getNewMonth() {
		try {
			ServiceReActiveDAO dao = new ServiceReActiveDAO();
			return dao.getNewMonth();
		} catch (Exception e) {
			Log.error(null, e);
		}
		return "";
	} 	
	
	//移动和宽带用户停机复开及时率月报表
	public Map getServiceReActive_Mon(Map<String, Object> queryData) {
		int numVdivlines =-1;
		StringBuffer  barChartMap=new StringBuffer();
		StringBuffer  lineChartMap=new StringBuffer();
		Map<String, Object> map=new HashMap<String,Object>();
		String dataDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
    	String field =       MapUtils.getString(queryData, "field",    "YD_SUM_NUM");
		String currentColor= "FBC62A";
    	String lastColor=    "71B359";
    	String VcurrentColor= "FF0000";
    	String VlastColor=    "0066FF";
    	double maxVal=0;
        double minVal=0;
        
        StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
      	
      	StringBuffer  temp4=new StringBuffer();
      	temp4.append("<categories>\n");
      	StringBuffer  temp5=new StringBuffer();
      	StringBuffer  temp6=new StringBuffer();
      	
      	
		String showCurrentYEAR = String.valueOf(dataDate);// 当月
		 String showLastYEAR = getLastMon(showCurrentYEAR);//上月
  	     temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
         temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
         
        List<Map<String,Object>> tabList=serviceReActiveDAO.getServiceReActive_Mon(queryData);//表格数据
        List<Map<String,Object>> barList=graphDAO.getChangeZoneLevel(queryData);//柱状图数据

        map.put("list", tabList);
        //柱状图
       for (Map<String, Object> key : barList){
       	  String curValue="0";
   		  String lastValue="0";
   		  Map<String,Object> param=new HashMap<String, Object>();
   		  param.put("zoneId",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
   	     // maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
   	     // minVal=compareMinValue(minVal,Double.parseDouble(curValue));
   		  Map<String, Object> lastObj =    serviceReActiveDAO.getChartData(showLastYEAR,param);
   		  Map<String, Object> curObj =    serviceReActiveDAO.getChartData(showCurrentYEAR,param);
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
	   map.put("barChartMap", barChartMap.toString());
	  
	   //折线图
	   String showCurrentYEAR1 = dataDate.substring(0, dataDate.length()-2);// 当月
	   String showLastYEAR1 = String.valueOf(Integer.parseInt(showCurrentYEAR1) - 1);// 去年同期
	   String tempMonth=showCurrentYEAR1+"01";//201301
 	   temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VcurrentColor+"' seriesName='"+showCurrentYEAR1+"'      color='"+VcurrentColor+"' anchorBorderColor='FFFFFF'>\n");
       temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VlastColor+"'    seriesName='"+showLastYEAR1+"'         color='"+VlastColor+"'    anchorBorderColor='FFFFFF'>\n");
         
	         for (int i = Integer.parseInt(tempMonth); i <= Integer.parseInt(dataDate); i++) {
	    		  String year      =String.valueOf(i).substring(0, 4);
	    	      String month_no  = String.valueOf(i).substring(4, 6);
	    	      int showDay   =Integer.parseInt(month_no);
                String lastYear =	String.valueOf(Integer.parseInt(year)-1)+month_no;    	      
	    		  String currentValue="0";
	    		  String lastValue="0";
	    		  Map<String, Object> currentObj = serviceReActiveDAO.getChartData(String.valueOf(i),queryData);
	    		  Map<String, Object> lastObj =    serviceReActiveDAO.getChartData(lastYear,queryData);
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
		          temp5.append("<set value='"+currentValue+"' hoverText='"+showCurrentYEAR1+"： "+currentValue+"' />\n");
		    	  temp6.append("<set value='"+lastValue+"'    hoverText='"+showLastYEAR1+"： "+lastValue+"' />\n");  
		    	  numVdivlines=numVdivlines+1;
	    	 }
	         temp4.append("</categories>\n");
	         temp5.append("</dataset>\n");
	         temp6.append("</dataset>\n");
	     	
	         lineChartMap.append("<chart labelDisplay='WRAP'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	     	        " numdivlines='4' adjustDiv='0'  yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' rotateNames='0'"+
	     	        " chartRightMargin='20' chartLeftMargin='10'"+
	     	        " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	         		" exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	         
	         lineChartMap.append(temp4).append(temp5).append(temp6);
	         lineChartMap.append("</chart>");
	         map.put("lineChartMap", lineChartMap.toString());
         return map; 
	}

	public List<Map<String,Object>> getSelectMon(){
		return serviceReActiveDAO.getSelectMon();
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
		  // }else{
			   strXML=getAreaChartXML(map);
		  // }
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
			String dataDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
	    	String field =       MapUtils.getString(queryData, "field", "YD_SUM_NUM");
			String currentColor= "FBC62A";
	    	String lastColor=    "71B359";
	    	double maxVal=0;
	        double minVal=0;
	        
	        StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
	      	
			String showCurrentYEAR = String.valueOf(dataDate);// 当月
		    String showLastYEAR = getLastMon(showCurrentYEAR);//上月
	  	    temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
	        temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
	         
	        List<Map<String,Object>> barList=serviceReActiveDAO.get21BarServiceReActive_Mon(queryData);//柱状图数据
	        
	        //柱状图
	       for (Map<String, Object> key : barList){
	       	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
	   		  String lastValue="0";
	   		  Map<String,Object> param=new HashMap<String, Object>();
	   		  param.put("zoneId",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
	   	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
	   	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
	   		  Map<String, Object> lastObj =    serviceReActiveDAO.getChartData(showLastYEAR,param);
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
              " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'"+
              " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
    		  " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
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
			String dataDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
	    	String field =       MapUtils.getString(queryData, "field", "YD_SUM_NUM");
			String currentColor= "FBC62A";
	    	String lastColor=    "71B359";
	    	double maxVal=0;
	        double minVal=0;
	        
	        StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
	      	
			String showCurrentYEAR = String.valueOf(dataDate);// 当月
		    String showLastYEAR = getLastMon(showCurrentYEAR);//上月
	  	    temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
	        temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
	         
	        List<Map<String,Object>> barList=graphDAO.getChangeZoneLevel(queryData);//柱状图数据
	        
	        //柱状图
	       for (Map<String, Object> key : barList){
	       	  String curValue="0";
	   		  String lastValue="0";
	   		  Map<String,Object> param=new HashMap<String, Object>();
	   		  param.put("zoneId",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
	   	      //maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
	   	     // minVal=compareMinValue(minVal,Double.parseDouble(curValue));
	   		  Map<String, Object> lastObj =    serviceReActiveDAO.getChartData(showLastYEAR,param);
	   		  Map<String, Object> curObj =    serviceReActiveDAO.getChartData(showCurrentYEAR,param);
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
		//移动和宽带用户停机复开及时率日报表
		public Map getServiceReActive_Day(Map<String, Object> queryData) {
			int numVdivlines =-1;
			StringBuffer  barChartMap=new StringBuffer();
			StringBuffer  lineChartMap=new StringBuffer();
			Map<String, Object> map=new HashMap<String,Object>();
			String dataDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
			
			String currYear  = String.valueOf(dataDate).substring(0, 4);
   	        String currMonth = String.valueOf(dataDate).substring(4, 6);
   	        String currDay = String.valueOf(dataDate).substring(6, 8);
   		 
   	        String showCurrentYEAR=currYear+currMonth;//当月
   	        String showLastYEAR=getLastMon(showCurrentYEAR);//上月
			
	    	String field =       MapUtils.getString(queryData, "field",    "YD_SUM_NUM");
			String currentColor= "FBC62A";
	    	String lastColor=    "71B359";
	    	String VcurrentColor= "FF0000";
	    	String VlastColor=    "0066FF";
	    	double maxVal=0;
	        double minVal=0;
	        
	        StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
	      	
	      	StringBuffer  temp4=new StringBuffer();
	      	temp4.append("<categories>\n");
	      	StringBuffer  temp5=new StringBuffer();
	      	StringBuffer  temp6=new StringBuffer();
	      	
	  	     temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
	         temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
	         
	        List<Map<String,Object>> tabList=serviceReActiveDAO.getServiceReActive_Day(queryData);//表格数据
	        List<Map<String,Object>> barList=serviceReActiveDAO.getBarServiceReActive_Day(queryData);//柱状图数据

	        map.put("list", tabList);
	        //柱状图
	       for (Map<String, Object> key : barList){
	       	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
	   		  String lastValue="0";
	   		  Map<String,Object> param=new HashMap<String, Object>();
	   		  param.put("zoneId",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
	   	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
	   	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
	   		  Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_ReActiveDay(showLastYEAR+currDay,param);
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
	       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	              " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	              " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	              " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	              " numdivlines='4' adjustDiv='0' rotateNames='0'"+
	              " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");        
	       barChartMap.append(temp1).append(temp2).append(temp3);
	       barChartMap.append("</chart>");
		   map.put("barChartMap", barChartMap.toString());
		  
  	       String currentMonth1=currYear+currMonth;
  	       String lastMonth1=getLastMon(currentMonth1);
  	       String tempDay=dataDate.substring(0,dataDate.length()-2)+"01";//201201
	 	   temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VcurrentColor+"' seriesName='"+currentMonth1+"'      color='"+VcurrentColor+"' anchorBorderColor='FFFFFF'>\n");
	       temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VlastColor+"'    seriesName='"+lastMonth1+"'         color='"+VlastColor+"'    anchorBorderColor='FFFFFF'>\n");
	         
		         for (int i = Integer.parseInt(tempDay); i <= Integer.parseInt(dataDate); i++) {
		        	 String year      =String.valueOf(i).substring(0, 4);
		    	      String month_no = String.valueOf(i).substring(4, 6);
		    	      String day      = String.valueOf(i).substring(6, 8);
		    	      int showDay=Integer.parseInt(day);
		    
		    	  	  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		    	      String lastDay=sdf.format(DateUtil.getDateOfLastMonth(year+month_no+day).getTime());	      
		    		  String currentValue="0";
		    		  String lastValue="0";
		    		  Map<String, Object> currentObj = serviceReActiveDAO.getChartData_ReActiveDay(String.valueOf(i),queryData);
		    		  Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_ReActiveDay(lastDay,queryData);
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
			          temp5.append("<set value='"+currentValue+"' hoverText='"+String.valueOf(i)+"： "+currentValue+"' />\n");
			    	  temp6.append("<set value='"+lastValue+"'    hoverText='"+lastDay+"： "+lastValue+"' />\n");  
			    	  numVdivlines=numVdivlines+1;
		    	 }
		         temp4.append("</categories>\n");
		         temp5.append("</dataset>\n");
		         temp6.append("</dataset>\n");
		     	
		         lineChartMap.append("<chart labelDisplay='WRAP'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
		     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
		     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
		     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
		     	        " numdivlines='4' adjustDiv='0' numVdivlines='"+numVdivlines+"'   rotateNames='0'"+
		     	        " chartRightMargin='20' chartLeftMargin='10'>\n");    
		         
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
		   public Map<String, Object> loadSet21AreaChart_ReActiveDay(Map<String, Object> map){
			   String changeZone=MapUtils.getString(map, "changeZone",    "0");
			   Map<String, Object> key=new HashMap<String, Object>();
			   //柱状图
			   map.put("currentColor", "FBC62A");
			   map.put("lastColor",    "71B359");
			   String strXML="";
			   if(changeZone.equals("0")){
				   strXML=get21AreaChartXML_ReActiveDay(map);
			   }else{
				   strXML=getAreaChartXML_ReActiveDay(map);
			   }
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
			private String get21AreaChartXML_ReActiveDay(Map<String, Object> queryData) {
				StringBuffer  barChartMap=new StringBuffer();
				String dataDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
		    	String field =       MapUtils.getString(queryData, "field", "YD_SUM_NUM");
				String currentColor= "FBC62A";
		    	String lastColor=    "71B359";
		    	double maxVal=0;
		        double minVal=0;
		        
		        StringBuffer  temp1=new StringBuffer();
		    	temp1.append("<categories>\n");
		      	StringBuffer  temp2=new StringBuffer();
		      	StringBuffer  temp3=new StringBuffer();
		      	
		      	String year      =String.valueOf(dataDate).substring(0, 4);
		   	    String month_no = String.valueOf(dataDate).substring(4, 6);
		   	    String day      = String.valueOf(dataDate).substring(6, 8);
		   	     
	    	  	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    	    String lastDay=sdf.format(DateUtil.getDateOfLastMonth(year+month_no+day).getTime());
	    	    
		  	    temp2.append("<dataset  seriesName='"+dataDate+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
		        temp3.append("<dataset  seriesName='"+lastDay+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
		         
		        List<Map<String,Object>> barList=serviceReActiveDAO.get21BarServiceReActive_Day(queryData);//柱状图数据
		        
		        //柱状图
		       for (Map<String, Object> key : barList){
		       	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  String lastValue="0";
		   		  Map<String,Object> param=new HashMap<String, Object>();
		   		  param.put("zoneId",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
		   	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
		   	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
		   		  Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_ReActiveDay(lastDay,param);
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
			private String getAreaChartXML_ReActiveDay(Map<String, Object> queryData) {
				StringBuffer  barChartMap=new StringBuffer();
				String dataDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
		    	String field =       MapUtils.getString(queryData, "field", "YD_SUM_NUM");
				String currentColor= "FBC62A";
		    	String lastColor=    "71B359";
		    	double maxVal=0;
		        double minVal=0;
		        
		        StringBuffer  temp1=new StringBuffer();
		    	temp1.append("<categories>\n");
		      	StringBuffer  temp2=new StringBuffer();
		      	StringBuffer  temp3=new StringBuffer();
		      	
				String showCurrentYEAR = String.valueOf(dataDate);// 当月
			    String showLastYEAR = getLastMon(showCurrentYEAR);//上月
		  	    temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
		        temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
		         
		        List<Map<String,Object>> barList=serviceReActiveDAO.getBarServiceReActive_Day(queryData);//柱状图数据
		        
		        //柱状图
		       for (Map<String, Object> key : barList){
		       	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  String lastValue="0";
		   		  Map<String,Object> param=new HashMap<String, Object>();
		   		  param.put("zoneId",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
		   	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
		   	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
		   		  Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_ReActiveDay(showLastYEAR,param);
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
		       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
		              " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
		              " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
		              " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
		              " numdivlines='4' adjustDiv='0' rotateNames='0'"+
		              " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");        
		       barChartMap.append(temp1).append(temp2).append(temp3);
		       barChartMap.append("</chart>");
		      return barChartMap.toString();	
		  }
			
         //移动和宽带用户停机复开各环节平均时间月报表
			public Map getServiceAvgSecends_Mon(Map<String, Object> queryData) {
				int numVdivlines =-1;
				StringBuffer  barChartMap=new StringBuffer();
				StringBuffer  lineChartMap=new StringBuffer();
				Map<String, Object> map=new HashMap<String,Object>();
				String dataDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
		    	String field =       MapUtils.getString(queryData, "field",    "YD_CBS_COSTTIME");
				String currentColor= "FBC62A";
		    	String lastColor=    "71B359";
		    	String VcurrentColor= "FF0000";
		    	String VlastColor=    "0066FF";
		    	double maxVal=0;
		        double minVal=0;
		        
		        StringBuffer  temp1=new StringBuffer();
		    	temp1.append("<categories>\n");
		      	StringBuffer  temp2=new StringBuffer();
		      	StringBuffer  temp3=new StringBuffer();
		      	
		      	StringBuffer  temp4=new StringBuffer();
		      	temp4.append("<categories>\n");
		      	StringBuffer  temp5=new StringBuffer();
		      	StringBuffer  temp6=new StringBuffer();
		      	
		      	
				String showCurrentYEAR = String.valueOf(dataDate);// 当月
				 String showLastYEAR = getLastMon(showCurrentYEAR);//上月
		  	     temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
		         temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
		         
		        List<Map<String,Object>> tabList=serviceReActiveDAO.getServiceAvgSecends_Mon(queryData);//表格数据
		        List<Map<String,Object>> barList=graphDAO.getChangeZoneLevel(queryData);

		        map.put("list", tabList);
		        //柱状图
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  Map<String,Object> param=new HashMap<String, Object>();
		   		  param.put("zoneId",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
		   		  param.put("prodTypeId",Convert.toString(queryData.get("prodTypeId")));
		   	     // maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
		   	    //  minVal=compareMinValue(minVal,Double.parseDouble(curValue));
		   		  Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_AvgSecendsMon(showLastYEAR,param);
		   		  Map<String, Object> curObj =    serviceReActiveDAO.getChartData_AvgSecendsMon(showCurrentYEAR,param);
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
			   map.put("barChartMap", barChartMap.toString());
			  
			   //折线图
			   String showCurrentYEAR1 = dataDate.substring(0, dataDate.length()-2);// 当月
			   String showLastYEAR1 = String.valueOf(Integer.parseInt(showCurrentYEAR1) - 1);// 去年同期
			   String tempMonth=showCurrentYEAR1+"01";//201301
		 	   temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VcurrentColor+"' seriesName='"+showCurrentYEAR1+"'      color='"+VcurrentColor+"' anchorBorderColor='FFFFFF'>\n");
		       temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+VlastColor+"'    seriesName='"+showLastYEAR1+"'         color='"+VlastColor+"'    anchorBorderColor='FFFFFF'>\n");
		         
			         for (int i = Integer.parseInt(tempMonth); i <= Integer.parseInt(dataDate); i++) {
			    		  String year      =String.valueOf(i).substring(0, 4);
			    	      String month_no  = String.valueOf(i).substring(4, 6);
			    	      int showDay   =Integer.parseInt(month_no);
		                String lastYear =	String.valueOf(Integer.parseInt(year)-1)+month_no;    	      
			    		  String currentValue="0";
			    		  String lastValue="0";
			    		  Map<String, Object> currentObj = serviceReActiveDAO.getChartData_AvgSecendsMon(String.valueOf(i),queryData);
			    		  Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_AvgSecendsMon(lastYear,queryData);
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
				          temp5.append("<set value='"+currentValue+"' hoverText='"+showCurrentYEAR1+"： "+currentValue+"' />\n");
				    	  temp6.append("<set value='"+lastValue+"'    hoverText='"+showLastYEAR1+"： "+lastValue+"' />\n");  
				    	  numVdivlines=numVdivlines+1;
			    	 }
			         temp4.append("</categories>\n");
			         temp5.append("</dataset>\n");
			         temp6.append("</dataset>\n");
			     	
			         lineChartMap.append("<chart labelDisplay='WRAP'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
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
			   public Map<String, Object> loadSet21AreaChart_AvgSecendsMon(Map<String, Object> map){
				  // String changeZone=MapUtils.getString(map, "changeZone",    "0");
				   Map<String, Object> key=new HashMap<String, Object>();
				   //柱状图
				   map.put("currentColor", "FBC62A");
				   map.put("lastColor",    "71B359");
				   String strXML="";
				  // if(changeZone.equals("0")){
					//   strXML=get21AreaChartXML_AvgSecendsMon(map);
				  // }else{
					   strXML=getAreaChartXML_AvgSecendsMon(map);
				 //  }
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
				private String get21AreaChartXML_AvgSecendsMon(Map<String, Object> queryData) {
					StringBuffer  barChartMap=new StringBuffer();
					String dataDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
			    	String field =       MapUtils.getString(queryData, "field", "YD_SUM_NUM");
					String currentColor= "FBC62A";
			    	String lastColor=    "71B359";
			    	double maxVal=0;
			        double minVal=0;
			        
			        StringBuffer  temp1=new StringBuffer();
			    	temp1.append("<categories>\n");
			      	StringBuffer  temp2=new StringBuffer();
			      	StringBuffer  temp3=new StringBuffer();
			      	
					String showCurrentYEAR = String.valueOf(dataDate);// 当月
				    String showLastYEAR = getLastMon(showCurrentYEAR);//上月
			  	    temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
			        temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
			         
			        List<Map<String,Object>> barList=serviceReActiveDAO.get21BarAvgSecendsMon_Mon(queryData);//柱状图数据
			        
			        //柱状图
			       for (Map<String, Object> key : barList){
			       	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
			   		  String lastValue="0";
			   		  Map<String,Object> param=new HashMap<String, Object>();
			   		  param.put("zoneId",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
			   		  param.put("prodTypeId",Convert.toString(queryData.get("prodTypeId")));
			   		  maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
			   	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
			   		  Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_AvgSecendsMon(showLastYEAR,param);
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
				private String getAreaChartXML_AvgSecendsMon(Map<String, Object> queryData) {
					StringBuffer  barChartMap=new StringBuffer();
					String dataDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
			    	String field =       MapUtils.getString(queryData, "field", "YD_SUM_NUM");
					String currentColor= "FBC62A";
			    	String lastColor=    "71B359";
			    	double maxVal=0;
			        double minVal=0;
			        
			        StringBuffer  temp1=new StringBuffer();
			    	temp1.append("<categories>\n");
			      	StringBuffer  temp2=new StringBuffer();
			      	StringBuffer  temp3=new StringBuffer();
			      	
					String showCurrentYEAR = String.valueOf(dataDate);// 当月
				    String showLastYEAR = getLastMon(showCurrentYEAR);//上月
			  	    temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
			        temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
			         
			        List<Map<String,Object>> barList=graphDAO.getChangeZoneLevel(queryData);//柱状图数据
			        
			        //柱状图
			       for (Map<String, Object> key : barList){
			       	  String curValue="0";
			   		  String lastValue="0";
			   		  Map<String,Object> param=new HashMap<String, Object>();
			   		  param.put("zoneId",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
			   		  param.put("prodTypeId",Convert.toString(queryData.get("prodTypeId")));
			   		  //maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
			   	      //minVal=compareMinValue(minVal,Double.parseDouble(curValue));
			   		  Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_AvgSecendsMon(showLastYEAR,param);
			   		  if (null != lastObj) {
			   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
			   		   }
			   		  Map<String, Object>curObj =    serviceReActiveDAO.getChartData_AvgSecendsMon(showCurrentYEAR,param);
			   		  if (null != curObj) {
			   	    	  curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
			   		   }
				    	    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
				    	    minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
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
	//客户保有_存储过程
	public Map getCustKeep_pg (Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
		map.putAll(serviceReActiveDAO.getCustKeep_pg(queryData));//客户保有报表——存储过程实现
		map.put("barChartMap", getAreaChartXML(queryData,"0"));//柱状图
		map.put("lineChartMap", getCustKeepLine(queryData));//折线图
		 return map; 
	}
	//提醒投诉分析报表——存储过程实现
	public Map getCmplRemind_pg (Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
		map.putAll(serviceReActiveDAO.getCustKeep_pg(queryData));//提醒投诉分析报表——存储过程实现
		map.put("pieChartMap",getProductTypePie(queryData));//产品类型——饼图
		map.put("pieChartMap2",getComplainTypePie(queryData));//投诉表象——饼图
		 return map; 
	}
	//客户保有_切换地市
	public Map<String, Object> loadSet21AreaChart_CustKeep(Map<String, Object> map){
		   String changeZone=MapUtils.getString(map, "changeZone",    null);//0代表五大区，1代表下级地市
		   Map<String, Object> key=new HashMap<String, Object>(); 
		   String strXML=getAreaChartXML(map,changeZone);
		   key.put("XML",strXML);
		   return key;
	   }
	//客户保有_指标切换
	public Map getCustKeepChange(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
		map.put("barChartMap", getAreaChartXML(queryData,"0"));//柱状图
	    map.put("lineChartMap", getCustKeepLine(queryData));//折线图
        return map; 
	}
	/**
	 *   饼图XML
	  * 方法描述：
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: qx
	  * @version: 2013-5-20 下午05:22:26
	 */	
	//产品类型占比——饼图
	private String getProductTypePie(Map<String, Object> excuteInitData) {
		StringBuffer  pieChartMap=new StringBuffer();
		//饼图展示
      	StringBuffer  temp=new StringBuffer();
      	List<Map<String, Object>>barList=null;
		barList = serviceReActiveDAO.getProductTypePieList(excuteInitData);// 产品类型列表
		for (Map<String, Object> key : barList) {
			String reasonName = String.valueOf(key.get("产品类型") == null ? "-": key.get("产品类型"));
            String curValue = String.valueOf(key.get("合计") == null ? "0": key.get("合计"));
			temp.append("<set label='"+reasonName+"' value='"+curValue+"'/> ");
		}
	   pieChartMap.append("<chart caption='产品类型' animation='1' showBorder='1' startingAngle='70' " +
       		"bgAngle='360' bgRatio='0,100' bgColor='99ccff,ffffff' manageLabelOverflow='0' " +
       		"pieRadius='130' enableSmartLabels='1' slicingDistance='30' " +
       		" baseFontSize='10' decimals='2' palette='4' formatNumberScale='0' showPercentValues='1'" +
       		" exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
            " exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	   pieChartMap.append(temp);
	   pieChartMap.append("</chart>");
      return pieChartMap.toString();	
  }	
	//投诉表象占比——饼图
	private String getComplainTypePie(Map<String, Object> excuteInitData) {
		StringBuffer  pieChartMap2=new StringBuffer();
		//饼图展示
      	StringBuffer  temp=new StringBuffer();
      	List<Map<String, Object>>barList=null;
		barList = serviceReActiveDAO.getComplainTypePieList(excuteInitData);// 投诉表象列表
		for (Map<String, Object> key : barList) {
			String reasonName = String.valueOf(key.get("投诉表象") == null ? "-": key.get("投诉表象"));
            String curValue = String.valueOf(key.get("合计") == null ? "0": key.get("合计"));
			temp.append("<set label='"+reasonName+"' value='"+curValue+"'/> ");
		}
	   pieChartMap2.append("<chart caption='投诉表象' animation='1' showBorder='1' startingAngle='70' " +
       		"bgAngle='360' bgRatio='0,100' bgColor='99ccff,ffffff' manageLabelOverflow='0' " +
       		"pieRadius='130' enableSmartLabels='1' slicingDistance='30' " +
       		" baseFontSize='10' decimals='2' palette='4' formatNumberScale='0' showPercentValues='1'" +
       		" exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
            " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	   pieChartMap2.append(temp);
	   pieChartMap2.append("</chart>");
      return pieChartMap2.toString();	
  }	
	/**
	 *   柱状图XML
	  * 方法描述：
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: qx
	  * @version: 2013-7-19下午05:22:26
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
		 //月报表
		if("custKeepIndexMon".equals(rptIndex)){//客户保有类指标月监测报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
			
		    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		
		    barList=graphDAO.getChangeZoneLevel(queryData);
		     //柱状图_月报表
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		  param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
		   		  param.put("prodTypeCode",MapUtils.getString(queryData, "prodTypeCode", null));
		   		  param.put("selectCol",MapUtils.getString(queryData, "selectCol", null)=="" ? "4": Convert.toString(queryData.get("selectCol")));
		   	      Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_CustKeepMon(lastYearMon,param);
		   	   Map<String, Object> curObj =    serviceReActiveDAO.getChartData_CustKeepMon(inYearMon,param);
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
		}if("custPhotoRev".equals(rptIndex)){//中高端拍照用户收入保有率分析月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
			
		    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		
		    barList=graphDAO.getChangeZoneLevel(queryData);
		     //柱状图_月报表
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		  param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
		   		  param.put("prodTypeCode",MapUtils.getString(queryData, "prodTypeCode", null));
		   	      Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_CustPhotoRevMon(lastYearMon,param);
		   	   Map<String, Object> curObj =    serviceReActiveDAO.getChartData_CustPhotoRevMon(inYearMon,param);
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
		}if("activeServMonitor".equals(rptIndex)){//主动服务类指标月监测报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
			
		    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		
/*		    if("0".equals(changeZone)){
		       //月报表
		       barList=serviceReActiveDAO.getBarActiveServMonitor_Zone(queryData);//五大区柱状图数据     
		    }if("1".equals(changeZone)){	
		    	barList=serviceReActiveDAO.getBarActiveServMonitor_DownZone(queryData);//切换地市（下一级）
		    }*/
		    
		    barList=graphDAO.getChangeZoneLevel(queryData);
		    
		     //柱状图_月报表
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		  param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
		   		  param.put("prodTypeCode",MapUtils.getString(queryData, "prodTypeCode", null));
		   		  curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  
		   		  Map<String, Object> curObj =    serviceReActiveDAO.getChartData_ActiveServMonitorMon(inYearMon,param);
		   	      Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_ActiveServMonitorMon(lastYearMon,param);
		   	      
		   	       if (null != curObj) {
		   	    	curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
		   		   }else{
		   			curValue="0";
		   		   }
		   		  if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
		        	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		} if("integralCust".equals(rptIndex)){//积分客户分析月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
			
		    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		
/*		    if("0".equals(changeZone)){
		       //月报表
		       barList=serviceReActiveDAO.getBarIntegralCust_Zone(queryData);//五大区柱状图数据     
		    }if("1".equals(changeZone)){	
		    	barList=serviceReActiveDAO.getBarIntegralCust_DownZone(queryData);//切换地市（下一级）
		    }*/
		    barList=graphDAO.getChangeZoneLevel(queryData);
		     //柱状图_月报表
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		  //param.put("zoneCode",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
		   		  param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
		   		  curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  
		   		  Map<String, Object> curObj =    serviceReActiveDAO.getChartData_IntegralCustMon(inYearMon,param);
		   	      Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_IntegralCustMon(lastYearMon,param);
		   	      
		   	      if (null != curObj) {
		   	    	curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
		   		   }else{
		   			curValue="0";
		   		   }
		   		  if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
		        	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		}if("integralScore".equals(rptIndex)){//积分分值分析月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
			
		    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		
		    /*if("0".equals(changeZone)){
		       //月报表
		       barList=serviceReActiveDAO.getBarIntegralScore_Zone(queryData);//五大区柱状图数据     
		    }if("1".equals(changeZone)){	
		    	barList=serviceReActiveDAO.getBarIntegralScore_DownZone(queryData);//切换地市（下一级）
		    }*/
		    barList=graphDAO.getChangeZoneLevel(queryData);
		     //柱状图_月报表
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		  //param.put("zoneCode",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
		   		  param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
		   		  curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  
		   		  Map<String, Object> curObj =    serviceReActiveDAO.getChartData_IntegralScoreMon(inYearMon,param);
		   	      Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_IntegralScoreMon(lastYearMon,param);

		   	      if (null != curObj) {
		   	    	curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
		   		   }else{
		   			curValue="0";
		   		   }		   	      
		   		  if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
		        	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		}if("integralExchange".equals(rptIndex)){//积分兑换分析月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
			
		    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		
		    /*if("0".equals(changeZone)){
		       //月报表
		       barList=serviceReActiveDAO.getBarIntegralExchange_Zone(queryData);//五大区柱状图数据     
		    }if("1".equals(changeZone)){	
		    	barList=serviceReActiveDAO.getBarIntegralExchange_DownZone(queryData);//切换地市（下一级）
		    }*/
		    barList=graphDAO.getChangeZoneLevel(queryData);
		     //柱状图_月报表
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		   param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
		   		  //param.put("zoneCode",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
		   		  curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  
		   		  Map<String, Object> curObj =    serviceReActiveDAO.getChartData_IntegralExchangeMon(inYearMon,param);
		   	      Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_IntegralExchangeMon(lastYearMon,param);
		   	      
		   	      if (null != curObj) {
		   	    	curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
		   		   }else{
		   			curValue="0";
		   		   }
		   		  if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
		        	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		}if("VIPCust".equals(rptIndex)){//VIP客户分析汇总月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
			
		    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		
		    /*if("0".equals(changeZone)){
		       //月报表
		       barList=serviceReActiveDAO.getBarVIPCust_Zone(queryData);//五大区柱状图数据     
		    }if("1".equals(changeZone)){	
		    	barList=serviceReActiveDAO.getBarVIPCust_DownZone(queryData);//切换地市（下一级）
		    }*/
		    barList=graphDAO.getChangeZoneLevel(queryData);
		     //柱状图_月报表
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		   param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
		   		  //param.put("zoneCode",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
		   		  curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  
		   		  Map<String, Object> curObj =    serviceReActiveDAO.getChartData_VIPCustMon(inYearMon,param);		   		
		   	      Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_VIPCustMon(lastYearMon,param);
		   	      
		   	      if (null != curObj) {
		   	    	curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
		   		   }else{
		   			curValue="0";
		   		   }
		   	      if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
		        	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		}if("clubMembers".equals(rptIndex)){//俱乐部会员数分析月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
			
		    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		       //月报表
		    /*if("0".equals(changeZone)){
			       //月报表
			       barList=serviceReActiveDAO.getBarClubMembers_Zone(queryData);//五大区柱状图数据     
			    }if("1".equals(changeZone)){	
			    	barList=serviceReActiveDAO.getBarClubMembers_DownZone(queryData);//切换地市（下一级）
			    }*/
		      
		    barList=graphDAO.getChangeZoneLevel(queryData);
		     //柱状图_月报表
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		   param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
		   		  //param.put("zoneCode",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
		   		  curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  
		   		  Map<String, Object> curObj =    serviceReActiveDAO.getChartData_ClubMembersMon(inYearMon,param);
		   	      Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_ClubMembersMon(lastYearMon,param);
		   	      
		   	      if (null != curObj) {
		   	    	curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
		   		   }else{
		   			curValue="0";
		   		   }
		   		  if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
		        	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		}if("servProcessMonitor".equals(rptIndex)){//服务过程监控类指标月监测报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
			
		    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		       //月报表
		    /*if("0".equals(changeZone)){
			       //月报表
			       barList=serviceReActiveDAO.getBarServProcessMonitorMon_Zone(queryData);//五大区柱状图数据     
			    }if("1".equals(changeZone)){	
			    	barList=serviceReActiveDAO.getBarServProcessMonitorMon_DownZone(queryData);//切换地市（下一级）
			    }*/
			  barList=graphDAO.getChangeZoneLevel(queryData);
		     //柱状图_月报表
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		  //param.put("zoneCode",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
		   		  param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
		   		  param.put("prodTypeCode",MapUtils.getString(queryData, "prodTypeCode",null));
		   		  param.put("indId",MapUtils.getString(queryData, "indId",null));
		   		  curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));		   		  
		   		  
		   		  Map<String, Object> curObj =    serviceReActiveDAO.getChartData_ServProcessMonitorMon(inYearMon,param);		   		  
		   	      Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_ServProcessMonitorMon(lastYearMon,param);
		   	      
		   	      if (null != curObj) {
		   	    	curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
		   		   }else{
		   			curValue="0";
		   		   }
		   		  if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
		        	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		}if("servProcessMonitorDay".equals(rptIndex)){//服务过程监控类指标日监测报表
			String dataDate=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String year      =String.valueOf(dataDate).substring(0, 4);
	   	    String month_no = String.valueOf(dataDate).substring(4, 6);
	   	    String day      = String.valueOf(dataDate).substring(6, 8);
	   	     
    	  	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    	    String lastDay=sdf.format(DateUtil.getDateOfLastMonth(year+month_no+day).getTime());
			
		    temp2.append("<dataset  seriesName='" + dataDate + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastDay + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		       //月报表
		    /*if("0".equals(changeZone)){
			       //月报表
			       barList=serviceReActiveDAO.getBarServProcessMonitorDay_Zone(queryData);//五大区柱状图数据     
			    }if("1".equals(changeZone)){	
			    	barList=serviceReActiveDAO.getBarServProcessMonitorDay_DownZone(queryData);//切换地市（下一级）
			    }*/
		       barList=graphDAO.getChangeZoneLevel(queryData);
		     //柱状图_月报表
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		  //param.put("zoneCode",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
		   		  param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
		   		  param.put("prodTypeCode",MapUtils.getString(queryData, "prodTypeCode",null));
		   		  param.put("indId",MapUtils.getString(queryData, "indId",null));
		   		  //curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  
		   		  Map<String, Object> curObj =    serviceReActiveDAO.getChartData_ServProcessMonitorDay(dataDate,param);
		   	      Map<String, Object> lastObj =    serviceReActiveDAO.getChartData_ServProcessMonitorDay(lastDay,param);
		   	      
		   	      if (null != curObj) {
		   	    	curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
		   		   }else{
		   			curValue="0";
		   		   }
		   		  if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
		        	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		}
       temp1.append("</categories>\n");
       temp2.append("</dataset>\n");
       temp3.append("</dataset>\n");
    /*   if("1".equals(changeZone)){//下级地市
    	   barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
               " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
               " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
               " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
               " numdivlines='4' adjustDiv='0' rotateNames='0'"+
               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");
       }if("0".equals(changeZone)){//五大区
    	   if("0000".equals(zoneCode)||"0001".equals(zoneCode)||"0002".equals(zoneCode)||"0003".equals(zoneCode)||"0004".equals(zoneCode)||"0005".equals(zoneCode)){
    		   barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
    		              " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
    		              " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
    		              " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
    		              " numdivlines='4' adjustDiv='0' rotateNames='0'"+
    		              " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");  
    	   }else{
    		   barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
    	               " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
    	               " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
    	               " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
    	               " numdivlines='4' adjustDiv='0' rotateNames='0'"+
    	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");
    	   }
          
       } */  
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
	//客户保有_折线图
	public String getCustKeepLine(Map<String, Object> queryData) {
		int numVdivlines =-1;
		double maxVal=0;
        double minVal=0;
		StringBuffer  lineChartMap=new StringBuffer();//折线图
		Map<String, Object> map=new HashMap<String,Object>();//参数map
		
	    //图形展示字段
    	String field =       MapUtils.getString(queryData, "field",null);
    	String currentColor= "FF0000";//当月红色
    	String lastColor=    "0066FF";//上月蓝色

      	StringBuffer  temp1=new StringBuffer();
      	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
		
		String rptIndex=MapUtils.getString(queryData, "rptIndex", null);
		String tempStart="01";//折线图起点
		//月报表
		if("custKeepIndexMon".equals(rptIndex)){//客户保有类指标月监测报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String inYear=inYearMon.substring(0,4);
			String inMon=inYearMon.substring(4,6);
			String lastYearMon=Integer.parseInt(inYear)-1+inMon;//去年同期
	        temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		
			   //月报表折线图
			for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(inMon); i++) {
				int showDay = i;
				String lineMon="";
				if(i<10){//小于10的前面加0;
					lineMon="0"+i;
				}else{
					lineMon=i+"";
				}
				String currentValue = "0";
				String lastValue = "0";
				String currentYM=inYear+lineMon;
				String lastYMM=Integer.parseInt(inYear)-1+lineMon;
				
				Map<String, Object> currentObj = serviceReActiveDAO.getChartData_CustKeepMon(currentYM, queryData);
				Map<String, Object> lastObj = serviceReActiveDAO.getChartData_CustKeepMon(lastYMM, queryData);

				if (null != currentObj) {
					currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
				} else {
					currentValue = "0";
				}
				if (null != lastObj) {
					lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
				} else {
					lastValue = "0";
				}
				temp1.append("<category name='" + showDay + "月' />\n");
				temp2.append("<set value='" + currentValue + "' hoverText='"+ currentYM + "：" + currentValue + "' />\n");
				temp3.append("<set value='" + lastValue + "'    hoverText='"+ lastYMM + "： " + lastValue + "' />\n");
				numVdivlines = numVdivlines + 1;
			}
		}if("custPhotoRev".equals(rptIndex)){ //中高端拍照用户收入保有率分析月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String inYear=inYearMon.substring(0,4);
			String inMon=inYearMon.substring(4,6);
			String lastYearMon=Integer.parseInt(inYear)-1+inMon;//去年同期
	        temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		
			   //月报表折线图
			for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(inMon); i++) {
				int showDay = i;
				String lineMon="";
				if(i<10){//小于10的前面加0;
					lineMon="0"+i;
				}else{
					lineMon=i+"";
				}
				String currentValue = "0";
				String lastValue = "0";
				String currentYM=inYear+lineMon;
				String lastYMM=Integer.parseInt(inYear)-1+lineMon;
				
				Map<String, Object> currentObj = serviceReActiveDAO.getChartData_CustPhotoRevMon(currentYM, queryData);
				Map<String, Object> lastObj = serviceReActiveDAO.getChartData_CustPhotoRevMon(lastYMM, queryData);

				if (null != currentObj) {
					currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
				} else {
					currentValue = "0";
				}
				if (null != lastObj) {
					lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
				} else {
					lastValue = "0";
				}
				temp1.append("<category name='" + showDay + "月' />\n");
				temp2.append("<set value='" + currentValue + "' hoverText='"+ currentYM + "：" + currentValue + "' />\n");
				temp3.append("<set value='" + lastValue + "'    hoverText='"+ lastYMM + "： " + lastValue + "' />\n");
				numVdivlines = numVdivlines + 1;
			}
		}if("activeServMonitor".equals(rptIndex)){ //主动服务类指标月监测报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String inYear=inYearMon.substring(0,4);
			String inMon=inYearMon.substring(4,6);
			String lastYearMon=Integer.parseInt(inYear)-1+inMon;//去年同期
	        temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		
			   //月报表折线图
			for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(inMon); i++) {
				int showDay = i;
				String lineMon="";
				if(i<10){//小于10的前面加0;
					lineMon="0"+i;
				}else{
					lineMon=i+"";
				}
				String currentValue = "0";
				String lastValue = "0";
				String currentYM=inYear+lineMon;
				String lastYMM=Integer.parseInt(inYear)-1+lineMon;
				
				Map<String, Object> currentObj = serviceReActiveDAO.getChartData_ActiveServMonitorMon(currentYM, queryData);
				Map<String, Object> lastObj = serviceReActiveDAO.getChartData_ActiveServMonitorMon(lastYMM, queryData);

				if (null != currentObj) {
					currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
				} else {
					currentValue = "0";
				}
				if (null != lastObj) {
					lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
				} else {
					lastValue = "0";
				}
				temp1.append("<category name='" + showDay + "月' />\n");
				temp2.append("<set value='" + currentValue + "' hoverText='"+ currentYM + "：" + currentValue + "' />\n");
				temp3.append("<set value='" + lastValue + "'    hoverText='"+ lastYMM + "： " + lastValue + "' />\n");
				numVdivlines = numVdivlines + 1;
			}
		}if("integralCust".equals(rptIndex)){ //积分客户分析月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String inYear=inYearMon.substring(0,4);
			String inMon=inYearMon.substring(4,6);
			String lastYearMon=Integer.parseInt(inYear)-1+inMon;//去年同期
	        temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		
			   //月报表折线图
			for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(inMon); i++) {
				int showDay = i;
				String lineMon="";
				if(i<10){//小于10的前面加0;
					lineMon="0"+i;
				}else{
					lineMon=i+"";
				}
				String currentValue = "0";
				String lastValue = "0";
				String currentYM=inYear+lineMon;
				String lastYMM=Integer.parseInt(inYear)-1+lineMon;
				
				Map<String, Object> currentObj = serviceReActiveDAO.getChartData_IntegralCustMon(currentYM, queryData);
				Map<String, Object> lastObj = serviceReActiveDAO.getChartData_IntegralCustMon(lastYMM, queryData);

				if (null != currentObj) {
					currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
				} else {
					currentValue = "0";
				}
				if (null != lastObj) {
					lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
				} else {
					lastValue = "0";
				}
				temp1.append("<category name='" + showDay + "月' />\n");
				temp2.append("<set value='" + currentValue + "' hoverText='"+ currentYM + "：" + currentValue + "' />\n");
				temp3.append("<set value='" + lastValue + "'    hoverText='"+ lastYMM + "： " + lastValue + "' />\n");
				numVdivlines = numVdivlines + 1;
			}
		}if("integralScore".equals(rptIndex)){ //积分分值分析月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String inYear=inYearMon.substring(0,4);
			String inMon=inYearMon.substring(4,6);
			String lastYearMon=Integer.parseInt(inYear)-1+inMon;//去年同期
	        temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		
			   //月报表折线图
			for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(inMon); i++) {
				int showDay = i;
				String lineMon="";
				if(i<10){//小于10的前面加0;
					lineMon="0"+i;
				}else{
					lineMon=i+"";
				}
				String currentValue = "0";
				String lastValue = "0";
				String currentYM=inYear+lineMon;
				String lastYMM=Integer.parseInt(inYear)-1+lineMon;
				
				Map<String, Object> currentObj = serviceReActiveDAO.getChartData_IntegralScoreMon(currentYM, queryData);
				Map<String, Object> lastObj = serviceReActiveDAO.getChartData_IntegralScoreMon(lastYMM, queryData);

				if (null != currentObj) {
					currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
				} else {
					currentValue = "0";
				}
				if (null != lastObj) {
					lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
				} else {
					lastValue = "0";
				}
				temp1.append("<category name='" + showDay + "月' />\n");
				temp2.append("<set value='" + currentValue + "' hoverText='"+ currentYM + "：" + currentValue + "' />\n");
				temp3.append("<set value='" + lastValue + "'    hoverText='"+ lastYMM + "： " + lastValue + "' />\n");
				numVdivlines = numVdivlines + 1;
			}
		}if("integralExchange".equals(rptIndex)){ //积分兑换分析月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String inYear=inYearMon.substring(0,4);
			String inMon=inYearMon.substring(4,6);
			String lastYearMon=Integer.parseInt(inYear)-1+inMon;//去年同期
	        temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		
			   //月报表折线图
			for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(inMon); i++) {
				int showDay = i;
				String lineMon="";
				if(i<10){//小于10的前面加0;
					lineMon="0"+i;
				}else{
					lineMon=i+"";
				}
				String currentValue = "0";
				String lastValue = "0";
				String currentYM=inYear+lineMon;
				String lastYMM=Integer.parseInt(inYear)-1+lineMon;
				
				Map<String, Object> currentObj = serviceReActiveDAO.getChartData_IntegralExchangeMon(currentYM, queryData);
				Map<String, Object> lastObj = serviceReActiveDAO.getChartData_IntegralExchangeMon(lastYMM, queryData);

				if (null != currentObj) {
					currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
				} else {
					currentValue = "0";
				}
				if (null != lastObj) {
					lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
				} else {
					lastValue = "0";
				}
				temp1.append("<category name='" + showDay + "月' />\n");
				temp2.append("<set value='" + currentValue + "' hoverText='"+ currentYM + "：" + currentValue + "' />\n");
				temp3.append("<set value='" + lastValue + "'    hoverText='"+ lastYMM + "： " + lastValue + "' />\n");
				numVdivlines = numVdivlines + 1;
			}
		}if("VIPCust".equals(rptIndex)){ //VIP客户分析汇总月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String inYear=inYearMon.substring(0,4);
			String inMon=inYearMon.substring(4,6);
			String lastYearMon=Integer.parseInt(inYear)-1+inMon;//去年同期
	        temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		
			   //月报表折线图
			for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(inMon); i++) {
				int showDay = i;
				String lineMon="";
				if(i<10){//小于10的前面加0;
					lineMon="0"+i;
				}else{
					lineMon=i+"";
				}
				String currentValue = "0";
				String lastValue = "0";
				String currentYM=inYear+lineMon;
				String lastYMM=Integer.parseInt(inYear)-1+lineMon;
				
				Map<String, Object> currentObj = serviceReActiveDAO.getChartData_VIPCustMon(currentYM, queryData);
				Map<String, Object> lastObj = serviceReActiveDAO.getChartData_VIPCustMon(lastYMM, queryData);

				if (null != currentObj) {
					currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
				} else {
					currentValue = "0";
				}
				if (null != lastObj) {
					lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
				} else {
					lastValue = "0";
				}
				 //maxVal=compareMaxValue(maxVal,Double.parseDouble(currentValue));
	    	    // minVal=compareMinValue(minVal,Double.parseDouble(currentValue));
	    	     //maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
	    	    // minVal=compareMinValue(minVal,Double.parseDouble(lastValue));
				temp1.append("<category name='" + showDay + "月' />\n");
				temp2.append("<set value='" + currentValue + "' hoverText='"+ currentYM + "：" + currentValue + "' />\n");
				temp3.append("<set value='" + lastValue + "'    hoverText='"+ lastYMM + "： " + lastValue + "' />\n");
				numVdivlines = numVdivlines + 1;
			}
		}if("clubMembers".equals(rptIndex)){ //俱乐部会员数分析月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String inYear=inYearMon.substring(0,4);
			String inMon=inYearMon.substring(4,6);
			String lastYearMon=Integer.parseInt(inYear)-1+inMon;//去年同期
	        temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		
			   //月报表折线图
			for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(inMon); i++) {
				int showDay = i;
				String lineMon="";
				if(i<10){//小于10的前面加0;
					lineMon="0"+i;
				}else{
					lineMon=i+"";
				}
				String currentValue = "0";
				String lastValue = "0";
				String currentYM=inYear+lineMon;
				String lastYMM=Integer.parseInt(inYear)-1+lineMon;
				
				Map<String, Object> currentObj = serviceReActiveDAO.getChartData_ClubMembersMon(currentYM, queryData);
				Map<String, Object> lastObj = serviceReActiveDAO.getChartData_ClubMembersMon(lastYMM, queryData);

				if (null != currentObj) {
					currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
				} else {
					currentValue = "0";
				}
				if (null != lastObj) {
					lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
				} else {
					lastValue = "0";
				}
				temp1.append("<category name='" + showDay + "月' />\n");
				temp2.append("<set value='" + currentValue + "' hoverText='"+ currentYM + "：" + currentValue + "' />\n");
				temp3.append("<set value='" + lastValue + "'    hoverText='"+ lastYMM + "： " + lastValue + "' />\n");
				numVdivlines = numVdivlines + 1;
			}
		}if("servProcessMonitor".equals(rptIndex)){ //服务过程监控类指标月监测报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String inYear=inYearMon.substring(0,4);
			String inMon=inYearMon.substring(4,6);
			String lastYearMon=Integer.parseInt(inYear)-1+inMon;//去年同期
	        temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
		
			   //月报表折线图
			for (int i = Integer.parseInt(tempStart); i <= Integer.parseInt(inMon); i++) {
				int showDay = i;
				String lineMon="";
				if(i<10){//小于10的前面加0;
					lineMon="0"+i;
				}else{
					lineMon=i+"";
				}
				String currentValue = "0";
				String lastValue = "0";
				String currentYM=inYear+lineMon;
				String lastYMM=Integer.parseInt(inYear)-1+lineMon;
				
				Map<String, Object> currentObj = serviceReActiveDAO.getChartData_ServProcessMonitorMon(currentYM, queryData);
				Map<String, Object> lastObj = serviceReActiveDAO.getChartData_ServProcessMonitorMon(lastYMM, queryData);

				if (null != currentObj) {
					currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
				} else {
					currentValue = "0";
				}
				if (null != lastObj) {
					lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
				} else {
					lastValue = "0";
				}
				temp1.append("<category name='" + showDay + "月' />\n");
				temp2.append("<set value='" + currentValue + "' hoverText='"+ currentYM + "：" + currentValue + "' />\n");
				temp3.append("<set value='" + lastValue + "'    hoverText='"+ lastYMM + "： " + lastValue + "' />\n");
				numVdivlines = numVdivlines + 1;
			}
		}if("servProcessMonitorDay".equals(rptIndex)){ //服务过程监控类指标日监测报表
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
	  	        String inY=surDay.substring(0,4);//当前日期年
		        String inM=surDay.substring(4,6);//当前月
		        String inD=surDay.substring(6,8);//当前日
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	  	        String lastDay=sdf.format(DateUtil.getDateOfLastMonth(inY+inM+inD).getTime());
	  	        
	  	        
	    		  String currentValue="0";
	    		  String lastValue="0";
	    		  /*
	               * 根据不同的报表Id取数   start by qx
	               */
	    		  Map<String, Object> currentObj=null;
	    		  Map<String, Object> lastObj=null;
	    			  currentObj = serviceReActiveDAO.getChartData_ServProcessMonitorDay(surDay,queryData);
		    		  lastObj =    serviceReActiveDAO.getChartData_ServProcessMonitorDay(lastDay,queryData);
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
	    	      temp1.append("<category name='"+showDay+"' />\n");
		          temp2.append("<set value='"+currentValue+"' hoverText='"+surDay+"："+currentValue+"' />\n");
		    	  temp3.append("<set value='"+lastValue+"'    hoverText='"+lastDay+"： "+lastValue+"' />\n");  
		    	  numVdivlines=numVdivlines+1;
	    	 }
		}
	         temp1.append("</categories>\n");
	         temp2.append("</dataset>\n");
	         temp3.append("</dataset>\n");
	     	
	         lineChartMap.append("<chart labelDisplay='WRAP'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	     	        " numdivlines='4' adjustDiv='0' yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"'   rotateNames='0'"+
	     	        " chartRightMargin='20' chartLeftMargin='10'"+
	     	       " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
 	   				" exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	         lineChartMap.append(temp1).append(temp2).append(temp3);
	         lineChartMap.append("</chart>");
         return lineChartMap.toString(); 
	}
	public Map getFailureOrder_pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(serviceReActiveDAO.getFailureOrder_pg(queryData));//客户密码异常监控管理模块
        return map; 
	}
	//停机复开及时率日(月)报_存储过程实现
	public Map getServiceReActive_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
	     map.putAll(serviceReActiveDAO.getServiceReActive_Pg(queryData));//投诉类指标检测报表——存储过程实现
		 map.put("barChartMap", getAreaChartXMLP(queryData));//柱状图
	     map.put("lineChartMap", getReActiveLineP(queryData));//折线图
         return map; 
	} 
	//停机复开及时率日(月)报_指标切换
	public Map getServiceReActive_Diagram(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
		map.put("barChartMap", getAreaChartXMLP(queryData));//柱状图
	    map.put("lineChartMap", getReActiveLineP(queryData));//折线图
        return map; 
	}
	//停机复开及时率日(月)报_横向对比
	public Map<String, Object> getCompaChart(Map<String, Object> map){
		   Map<String, Object> key=new HashMap<String, Object>(); 
		   String strXML=getAreaChartXMLP(map);
		   key.put("XML",strXML);
		   return key;
	   }
	/**
	 *   柱状图XML
	  * 方法描述：
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: qx
	  * @version: 2014-1-20 下午05:22:26
	 */
	private String getAreaChartXMLP(Map<String, Object> queryData) {
		StringBuffer  barChartMap=new StringBuffer();
		//柱状图展示
    	String field =       MapUtils.getString(queryData, "field",null);
		String currentColor= "FBC62A";//当月
    	String lastColor=    "71B359";//上月
    	
        StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();

		String rptIndex=MapUtils.getString(queryData, "rptIndex", null);
		Map<String,Object> param=new HashMap<String, Object>();
		
	    List<Map<String,Object>>barList=null;
		//日报表
	if ("serviceReActiveDayP".equals(rptIndex)) {// 停机复开及时率日报表
		String inDate = MapUtils.getString(queryData, "dateTime", null).replace("-", "");// 当前 年月日
        String inY=inDate.substring(0,4);//当前日期年
        String inM=inDate.substring(4,6);//当前月
        String inD=inDate.substring(6,8);//当前日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    String lastDate=sdf.format(DateUtil.getDateOfLastMonth(inY+inM+inD).getTime());

		temp2.append("<dataset  seriesName='" + inDate + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		temp3.append("<dataset  seriesName='" + lastDate + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

		barList=graphDAO.getChangeZoneLevel(queryData);
	        //柱状图_日报表
		for (Map<String, Object> key : barList) {
			String curValue = "0";
			String lastValue = "0";
			param.put("zoneCode", String.valueOf(key.get("ZONE_CODE") == null ? "0000" : key.get("ZONE_CODE")));
			Map<String, Object> lastObj = serviceReActiveDAO.getChartData_ReActiveDay(lastDate, param);
			Map<String, Object> curObj = serviceReActiveDAO.getChartData_ReActiveDay(inDate, param);
			if (null != lastObj) {
				lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
			} else {
				lastValue = "0";
			}
			if (null != curObj) {
				curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
			} else {
				curValue = "0";
			}
			temp1.append("<category label='" + key.get("ZONE_NAME")+ "'/>\n");
			temp2.append("<set      value='" + curValue + "' />\n");
			temp3.append("<set      value='" + lastValue + "' />\n");
		}
		}
	//月报表
	if ("serviceReActiveMonP".equals(rptIndex)) {// 停机复开及时率月报表
		String inDate = MapUtils.getString(queryData, "dateTime", null).replace("-", "");// 当前 年月
		String lastDate=DateUtil.getLastMon(inDate);//上月年月

		temp2.append("<dataset  seriesName='" + inDate + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		temp3.append("<dataset  seriesName='" + lastDate + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

		barList=graphDAO.getChangeZoneLevel(queryData);
	        //柱状图_日报表
		for (Map<String, Object> key : barList) {
			String curValue = "0";
			String lastValue = "0";
			param.put("zoneCode", String.valueOf(key.get("ZONE_CODE") == null ? "0000" : key.get("ZONE_CODE")));
			Map<String, Object> lastObj = serviceReActiveDAO.getChartData_ReActiveDay(lastDate, param);
			Map<String, Object> curObj = serviceReActiveDAO.getChartData_ReActiveDay(inDate, param);
			if (null != lastObj) {
				lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
			} else {
				lastValue = "0";
			}
			if (null != curObj) {
				curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
			} else {
				curValue = "0";
			}
			temp1.append("<category label='" + key.get("ZONE_NAME")+ "'/>\n");
			temp2.append("<set      value='" + curValue + "' />\n");
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
	//停机复开及时率日报_折线图
	public String getReActiveLineP(Map<String, Object> queryData) {
		int numVdivlines =0;
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
		if("serviceReActiveDayP".equals(rptIndex)){//停机复开及时率日报表
			String inDate=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月日
			String inDateYM=inDate.substring(0,6);//当前日期年月
			String lastYM=DateUtil.getLastMon(inDateYM);//获取上月年月
			
			String curStartTime=inDateYM+"01";
			String curEndTime=inDate;
	        temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inDateYM+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYM+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
	        //日报表折线图
	        List<Map<String,Object>>curList=serviceReActiveDAO.getLinePoint(curStartTime, curEndTime, queryData);//当月日期列表
	        for (Map<String, Object> key : curList){
	        	  String lastValue="";
		       	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
		       	  String dateTime=String.valueOf(key.get("DAY_ID"));
		          String inY=dateTime.substring(0,4);//当前日期年
		          String inM=dateTime.substring(4,6);//当前月
		          String inD=dateTime.substring(6,8);//当前日
		          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    	      String lastTime=sdf.format(DateUtil.getDateOfLastMonth(inY+inM+inD).getTime());
		       	  Map<String, Object> lastObj = serviceReActiveDAO.getChartData_ReActiveDay(lastTime, queryData);
		       	if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }
		       	  temp1.append("<category name='"+Integer.parseInt(inD)+"' />\n");
		          temp2.append("<set value='"+curValue+"' hoverText='"+dateTime+"： "+curValue+"' />\n");
		    	  temp3.append("<set value='"+lastValue+"'    hoverText='"+lastTime+"： "+lastValue+"' />\n"); 
		    	  numVdivlines++;
		       } 
		}
		//月报表
		if("serviceReActiveMonP".equals(rptIndex)){//停机复开及时率月报表
			String inDateYM=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月		
			String lastYM=DateUtil.getLastMon(inDateYM);//上月
			
			String curStartTime=inDateYM+"01";
			String curEndTime=inDateYM+"31";
	        temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inDateYM+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYM+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
	        //日报表折线图
	        List<Map<String,Object>>curList=serviceReActiveDAO.getLinePoint(curStartTime, curEndTime, queryData);//当月日期列表
	        for (Map<String, Object> key : curList){
	        	  String lastValue="";
		       	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
		       	  String dateTime=String.valueOf(key.get("DAY_ID"));
		          String inY=dateTime.substring(0,4);//当前日期年
		          String inM=dateTime.substring(4,6);//当前月
		          String inD=dateTime.substring(6,8);//当前日
		          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    	      String lastTime=sdf.format(DateUtil.getDateOfLastMonth(inY+inM+inD).getTime());
		       	  Map<String, Object> lastObj = serviceReActiveDAO.getChartData_ReActiveDay(lastTime, queryData);
		       	if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }
		       	  temp1.append("<category name='"+Integer.parseInt(inD)+"' />\n");
		          temp2.append("<set value='"+curValue+"' hoverText='"+dateTime+"： "+curValue+"' />\n");
		    	  temp3.append("<set value='"+lastValue+"'    hoverText='"+lastTime+"： "+lastValue+"' />\n"); 
		    	  numVdivlines++;
		       } 
		}
		     numVdivlines -=2;
	         temp1.append("</categories>\n");
	         temp2.append("</dataset>\n");
	         temp3.append("</dataset>\n"); 
	         lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	     	        " numdivlines='4' adjustDiv='0' numVdivlines='"+numVdivlines+"'   rotateNames='0'"+
	     	        " chartRightMargin='20' chartLeftMargin='10'"+
	     	       " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	               " exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");    
	         lineChartMap.append(temp1).append(temp2).append(temp3);
	         lineChartMap.append("</chart>");
         return lineChartMap.toString(); 
	}
	//停复机清单
	public Map<String, Object> getServiceReActiveDetail_Pg(Map<String, Object> paramMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		int allPageCount = serviceReActiveDAO.getDataCount(paramMap);// 总记录数
		map.put("allPageCount", allPageCount);

		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
		page.setSize(Convert.toInt(paramMap.get("pageCount")));
		page.setTotalNum(allPageCount);

		map.putAll(serviceReActiveDAO.getTableData(paramMap, page));// 记录
		return map;
	}
	/**
	 * 导出数据
	 */
	public Map<String, Object> expExcData(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		ServiceReActiveDAO serviceReActiveDAO = new ServiceReActiveDAO();
		return serviceReActiveDAO.getTableData(paramMap, page);// 记录
	}
}
