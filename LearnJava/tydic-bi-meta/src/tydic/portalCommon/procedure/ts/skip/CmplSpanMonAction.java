/**
 * 文件名：ServWarnMonAction.java
 * 版本信息：Version 1.0
 * 日期：2013-7-24
 * Copyright tydic.com.cn Corporation 2013 
 * 版权所有
 */
package tydic.portalCommon.procedure.ts.skip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.portalCommon.procedure.base.BaseAction;
import tydic.reports.graph.GraphDAO;

/**
 * 类描述：
 * 
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-7-24 下午02:44:19
 */
public class CmplSpanMonAction  extends BaseAction{

	private CmplSpanMonDAO cmplSpanMonDAO;
	private GraphDAO graphDAO;

	public GraphDAO getGraphDAO() {
		return graphDAO;
	}
	public void setGraphDAO(GraphDAO graphDAO) {
		this.graphDAO = graphDAO;
	}
	public CmplSpanMonDAO getCmplSpanMonDAO() {
		return cmplSpanMonDAO;
	}
	/**
	 * 
	 * 得最近月份
	 * 
	 */
	public String getNewMonth() {
		try {
			CmplSpanMonDAO cmplSpanMonDAO = new CmplSpanMonDAO();
			return cmplSpanMonDAO.getNewMonth();
		} catch (Exception e) {
			Log.error(null, e);
		}
		return "";
	}
    /**
     * 表格数据
    */
	@Override
	public Map<String, Object> getTableData(Map<String, Object> param) {
		return cmplSpanMonDAO.getTableData(param);
	}
	
    /**
     * 曲线图
     */
	@Override
	public Map<String, Object>  bulidLineChart(Map<String, Object> param){
	  	    String dataDate =     MapUtils.getString(param, "dateTime", null);
		    Map<String, Object> key=new HashMap<String, Object>();
	    	StringBuffer  chartMap=new StringBuffer();
	    	String currentColor="FF0000";
	    	String lastColor="0066FF";
	    	String field="越级投诉率"; //越级投诉率 
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
		    		  Map<String, Object> currentObj = cmplSpanMonDAO.getChartData(String.valueOf(i),param);
		    		  Map<String, Object> lastObj =    cmplSpanMonDAO.getChartData(lastYear,param);
		    	      if (null != currentObj) {
		    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
		    		   }
		    	      if (null != lastObj) {
		    	    	   lastValue =   String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
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
	
	@Override
	public Map<String, Object> bulidBarChart(Map<String, Object> param) {
		String dataDate =     MapUtils.getString(param, "dateTime", null);
		Map<String, Object> mapKey=new HashMap<String, Object>();
    	String currentColor="FBC62A";
    	String lastColor="71B359";
    	String field="越级投诉率";//越级投诉率
		StringBuffer  chartMap=new StringBuffer();
    	StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();

		 String showCurrentYEAR = String.valueOf(dataDate);// 当月
		 String showLastYEAR = getLastMon(showCurrentYEAR);
   	     temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
         temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
         List<Map<String,Object>>barList=graphDAO.getChangeZoneLevel(param);
         
        for (Map<String, Object> key : barList){
        	  String curValue="0";
      	      String lastValue="0";;
    		  Map<String,Object> params=new HashMap<String, Object>();
        	  params.put("zoneCode",  key.get("ZONE_CODE"));
        	  Map<String, Object> curObj =    cmplSpanMonDAO.getChartData(showCurrentYEAR,params);
    	      Map<String, Object> lastObj =    cmplSpanMonDAO.getChartData(showLastYEAR,params);
    	      if (null != lastObj) {
    	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
    		   }
    	      if (null != curObj) {
   				  curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
   		      } 
         	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
      	    temp2.append("<set      value='"+curValue+"' />\n");
      	    temp3.append("<set      value='"+lastValue+"' />\n");
        }         
	        temp1.append("</categories>\n");
	        temp2.append("</dataset>\n");
	        temp3.append("</dataset>\n");
	        chartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
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
	
	public void setCmplSpanMonDAO(CmplSpanMonDAO cmplSpanMonDAO) {
		this.cmplSpanMonDAO = cmplSpanMonDAO;
	}
}
