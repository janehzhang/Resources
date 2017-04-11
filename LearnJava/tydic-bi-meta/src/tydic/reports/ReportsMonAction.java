package tydic.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.Log;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.Page;
import tydic.meta.common.yhd.utils.Pager;
/**
 * 
 * @author qx 定制化月报表
 * 
 */
public class ReportsMonAction {
	private ReportsMonDAO reportsMonDAO; 
	
	public ReportsMonDAO getReportsMonDAO() {
		return reportsMonDAO;
	}

	public void setReportsMonDAO(ReportsMonDAO reportsMonDAO) {
		this.reportsMonDAO = reportsMonDAO;
	}

	/**
	 * 获取最新显示的月份
	 */
	public String getNewMonth() {
		try {
			ReportsMonDAO dao = new ReportsMonDAO();
			return dao.getNewMonth();
		} catch (Exception e) {
			Log.error(null, e);
		}
		return "";
	} 	
	
	//本地全业务抱怨率月报表
	public Map getCmplReport_Mon(Map<String, Object> queryData) {
		int numVdivlines =-1;
		StringBuffer  barChartMap=new StringBuffer();
		StringBuffer  lineChartMap=new StringBuffer();
		Map<String, Object> map=new HashMap<String,Object>();
		String dataDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
    	String field =       MapUtils.getString(queryData, "field",    "COMPLAIN_NUM");
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
         
        List<Map<String,Object>> tabList=reportsMonDAO.getCmplReport_Mon(queryData);//表格数据
        List<Map<String,Object>> barList=reportsMonDAO.getBarCmplReport_Mon(queryData);//柱状图数据
        
        
        map.put("list", tabList);
        //柱状图
       for (Map<String, Object> key : barList){
       	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
   		  String lastValue="0";
   		  Map<String,Object> param=new HashMap<String, Object>();
   		  param.put("zoneId",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
   		  param.put("prodTypeId",MapUtils.getString(queryData, "prodTypeId", null)=="" ? "-1": Convert.toString(queryData.get("prodTypeId")));
   		  param.put("cmplBusiTypeId",MapUtils.getString(queryData, "cmplBusiTypeId",null)=="" ? "1": Convert.toString(queryData.get("cmplBusiTypeId")));
   	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
   	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
   		  Map<String, Object> lastObj =    reportsMonDAO.getChartData(showLastYEAR,param);
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
       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
              " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
              " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
              " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
              " numdivlines='4' adjustDiv='0' rotateNames='0'"+
              " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");        
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
	    		  Map<String, Object> currentObj = reportsMonDAO.getChartData(String.valueOf(i),queryData);
	    		  Map<String, Object> lastObj =    reportsMonDAO.getChartData(lastYear,queryData);
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
	    	      
	    	      temp4.append("<category name='"+showDay+"月份' />\n");
		          temp5.append("<set value='"+currentValue+"' hoverText='"+showCurrentYEAR1+"： "+currentValue+"' />\n");
		    	  temp6.append("<set value='"+lastValue+"'    hoverText='"+showLastYEAR1+"： "+lastValue+"' />\n");  
		    	  numVdivlines=numVdivlines+1;
	    	 }
	         temp4.append("</categories>\n");
	         temp5.append("</dataset>\n");
	         temp6.append("</dataset>\n");
	     	
	         lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
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
	public Map getListCmplReport_Mon(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		//分页
        Integer rows = reportsMonDAO.getCountListCmplReport_Mon(queryData);
        map.put("totalSize", rows);
        map.put("pageIndex", queryData.get("pageIndex"));
        map.put("pageSize", queryData.get("pageSize"));
        Page page=new Page();
        Map<String,Object> pageInfo = page.getPageInfo(map);
        map.put("pageCount", pageInfo.get("pageCount"));
        map.put("pageNo", pageInfo.get("pageNo"));
        List<Map<String,Object>> tabList=reportsMonDAO.getListCmplReport_Mon(queryData,pageInfo);
        map.put("list", tabList);
        return map;
	}
	
	
	public List<Map<String,Object>> getSelectMon(){
		return reportsMonDAO.getSelectMon();
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
	    	String field =       MapUtils.getString(queryData, "field", "COMPLAIN_NUM");
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
	         
	        List<Map<String,Object>> barList=reportsMonDAO.get21BarCmplReport_Mon(queryData);//柱状图数据
	        
	        //柱状图
	       for (Map<String, Object> key : barList){
	       	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
	   		  String lastValue="0";
	   		  Map<String,Object> param=new HashMap<String, Object>();
	   		  param.put("zoneId",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
	   		  param.put("prodTypeId",MapUtils.getString(queryData, "prodTypeId", null)=="" ? "-1": Convert.toString(queryData.get("prodTypeId")));
	   		  param.put("cmplBusiTypeId",MapUtils.getString(queryData, "cmplBusiTypeId",null)=="" ? "1": Convert.toString(queryData.get("cmplBusiTypeId")));
	   	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
	   	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
	   		  Map<String, Object> lastObj =    reportsMonDAO.getChartData(showLastYEAR,param);
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
			String dataDate =    MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");
	    	String field =       MapUtils.getString(queryData, "field", "COMPLAIN_NUM");
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
	         
	        List<Map<String,Object>> barList=reportsMonDAO.getBarCmplReport_Mon(queryData);//柱状图数据
	        
	        //柱状图
	       for (Map<String, Object> key : barList){
	       	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
	   		  String lastValue="0";
	   		  Map<String,Object> param=new HashMap<String, Object>();
	   		  param.put("zoneId",String.valueOf(key.get("REGION_ID") == null ? "0": key.get("REGION_ID")));
	   		  param.put("prodTypeId",MapUtils.getString(queryData, "prodTypeId", null)=="" ? "-1": Convert.toString(queryData.get("prodTypeId")));
	   		  param.put("cmplBusiTypeId",MapUtils.getString(queryData, "cmplBusiTypeId",null)=="" ? "1": Convert.toString(queryData.get("cmplBusiTypeId")));
	   	      maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
	   	      minVal=compareMinValue(minVal,Double.parseDouble(curValue));
	   		  Map<String, Object> lastObj =    reportsMonDAO.getChartData(showLastYEAR,param);
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
	       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	              " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	              " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	              " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	              " numdivlines='4' adjustDiv='0' rotateNames='0'"+
	              " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");        
	       barChartMap.append(temp1).append(temp2).append(temp3);
	       barChartMap.append("</chart>");
	      return barChartMap.toString();	
	  }
		public Map<String, Object> getTableData(Map<String, Object> paramMap) {
			Map<String, Object> map = new HashMap<String, Object>();

			String ripId = MapUtils.getString(paramMap, "ripId",  null);
			if("2".equals(ripId)){
				Pager page = Pager.getInstance();
				page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
				page.setSize(Convert.toInt(paramMap.get("pageCount")));
				map.putAll(reportsMonDAO.getGroupChannelList_Pg(paramMap, page));// 记录
				
			}else
			{
				int allPageCount = reportsMonDAO.getDataCount(paramMap);// 总记录数
				map.put("allPageCount", allPageCount);

				Pager page = Pager.getInstance();
				page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
				page.setSize(Convert.toInt(paramMap.get("pageCount")));
				page.setTotalNum(allPageCount);

				map.putAll(reportsMonDAO.getTableData(paramMap, page));// 记录
				
			}
			
			
			return map;
		}
		
		
		/**
		 * 导出数据
		 */
		public Map<String, Object> expTxtData(Map<String, Object> paramMap) {
			Map<String, Object> map = new HashMap<String, Object>();
			int allPageCount = reportsMonDAO.getDataCount(paramMap);// 总记录数
			map.put("allPageCount", allPageCount);
			Pager page = Pager.getInstance();
			map.putAll(reportsMonDAO.getTableData(paramMap, page));// 记录
			return map;// 记录
		}

}
