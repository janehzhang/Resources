/**
  * 文件名：MaintionMonitorAction.java
  * 版本信息：Version 1.0
  * 日期：2013-5-28
  * Copyright tydic.com.cn Corporation 2013 
  * 版权所有
  */
package tydic.portalCommon.procedure.zd.mon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.reports.graph.GraphDAO;

/**
 * 类描述：
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-5-28 下午03:59:47 
 */
public class MaintionMonitorMonAction {

	private MaintionMonitorMonDAO mainMonDAO;
    private GraphDAO graphDAO; 
	
	public GraphDAO getGraphDAO() {
		return graphDAO;
	}
	public void setGraphDAO(GraphDAO graphDAO) {
		this.graphDAO = graphDAO;
	}

	public String getNewMonth() {
		try {
			MaintionMonitorMonDAO mainMonDAO = new MaintionMonitorMonDAO();
			return mainMonDAO.getNewMonth();
		} catch (Exception e) {
			Log.error(null, e);
		}
		return "";
	} 

	public List<Map<String,Object>> getMonthList(Map<String,Object> param){
		return mainMonDAO.queryMonList(param);
	}
	
	public List<Map<String, Object>> getProdTypeList(Map<String, Object> param) {
		return mainMonDAO.getProdTypeList(param);
	}
	
	public List<Map<String, Object>>  getIndexList(Map<String, Object> param) {
		return mainMonDAO.getIndexList(param);
	}
    /**
     * 表格数据
     */
	public Map<String, Object> getTableData(Map<String, Object> param) {
		return mainMonDAO.getTableData(param);
	}

    /**
     * 曲线图
     */
	public Map<String, Object>  bulidLineChart(Map<String, Object> param){
	  	    String dataDate =     MapUtils.getString(param, "dateTime", null).replaceAll("-", "");
		    Map<String, Object> key=new HashMap<String, Object>();
	    	StringBuffer  chartMap=new StringBuffer();
	    	String currentColor="FF0000";
	    	String lastColor="0066FF";
	    	String field="当月值";//当月值
	    	int numVdivlines =0;
	    	StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
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
		    		  Map<String, Object> currentObj = mainMonDAO.getChartData(String.valueOf(i),param);
		    		  Map<String, Object> lastObj =    mainMonDAO.getChartData(lastYear,param);
		    	      if (null != currentObj) {
		    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
		    		   }
		    	      if (null != lastObj) {
		    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		    		   }
		    	      temp1.append("<category name='"+showDay+"月' />\n");
			          temp2.append("<set value='"+currentValue+"' hoverText='"+showCurrentYEAR+"： "+currentValue+"' />\n");
			    	  temp3.append("<set value='"+lastValue+"'    hoverText='"+showLastYEAR+"： "+lastValue+"' />\n");  
			    	  numVdivlines++;
		    	 }
	    	numVdivlines -=2;
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
		 key.put("XML", chartMap.toString());
		 return key;
	}	

	
	/**
	 * 柱状图
	 */
	public Map<String, Object>  bulidBarChart(Map<String, Object> param){
		String dataDate =     MapUtils.getString(param, "dateTime", null).replaceAll("-", "");
		Map<String, Object> mapKey=new HashMap<String, Object>();
    	String currentColor="FBC62A";
    	String lastColor="71B359";
    	String field="当月值";//当月值
		StringBuffer  chartMap=new StringBuffer();
    	StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();

		 String showCurrentYEAR = String.valueOf(dataDate);// 当月
		 String showLastYEAR = getLastMon(showCurrentYEAR);
   	     temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
         temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
/*         List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
         String labelDisplay="";
         if( param.get("changeZone")!= null && param.get("changeZone").equals("0"))
         {
         	list=mainMonDAO.get21DrillTableData(param);
         	labelDisplay="WRAP";
         }else
         {
         	list=mainMonDAO.getDrillTableData(param);
         	labelDisplay="NONE";
         }
         
         if(param.get("changeZone")!= null && param.get("changeZone").equals("3"))
         {
         	 labelDisplay="WRAP";
         } */
         
         List<Map<String,Object>> list = null;
         list = graphDAO.getChangeZoneLevel(param);
         
        for (Map<String, Object> key : list){
        	  //String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
        	  String curValue="0";
    		  String lastValue="0";
    		  
    		  Map<String,Object> params=new HashMap<String, Object>();
        	  params.put("zoneCode",  key.get("ZONE_CODE"));
        	  params.put("prodType",  param.get("prodType"));
        	  params.put("indexId",   param.get("indexId"));
        	  
        	  Map<String, Object> curObj =    mainMonDAO.getChartData(showCurrentYEAR,params);
    	      Map<String, Object> lastObj =    mainMonDAO.getChartData(showLastYEAR,params);
    	      
    	      if (null != curObj) {
    	    	  curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
    		   }
    	      if (null != lastObj) {
    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
    		   }
    	      
         	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
      	    temp2.append("<set      value='"+curValue+"' />\n");
      	    temp3.append("<set      value='"+lastValue+"' />\n");
        }         
	        temp1.append("</categories>\n");
	        temp2.append("</dataset>\n");
	        temp3.append("</dataset>\n");
	        chartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='wrap'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	              " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	              " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	              " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	              " numdivlines='4' adjustDiv='0' rotateNames='0'"+
	              " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'"+
	              " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	        	  " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	        chartMap.append(temp1).append(temp2).append(temp3);
		    chartMap.append("</chart>");
		
		    mapKey.put("XML", chartMap.toString());
		 return mapKey;
	}
	
	public void setMainMonDAO(MaintionMonitorMonDAO mainMonDAO) {
		this.mainMonDAO = mainMonDAO;
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
}
