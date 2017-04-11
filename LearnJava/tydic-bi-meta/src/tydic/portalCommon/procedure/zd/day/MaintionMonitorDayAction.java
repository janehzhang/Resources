/**
  * 文件名：MaintionMonitorAction.java
  * 版本信息：Version 1.0
  * 日期：2013-5-28
  * Copyright tydic.com.cn Corporation 2013 
  * 版权所有
  */
package tydic.portalCommon.procedure.zd.day;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.Log;
import tydic.frame.common.utils.MapUtils;
import tydic.portalCommon.DateUtil;
import tydic.reports.graph.GraphDAO;


	/**
 * 类描述：
 * @version: 1.0
 * @author: yanhaidong
 * @version: 2013-5-28 下午03:59:47 
 */
public class MaintionMonitorDayAction {

	private MaintionMonitorDayDAO mainDayDAO;
    private GraphDAO graphDAO; 
	
	public GraphDAO getGraphDAO() {
		return graphDAO;
	}
	public void setGraphDAO(GraphDAO graphDAO) {
		this.graphDAO = graphDAO;
	}
	
	public String getNewDate() {
		try {
			MaintionMonitorDayDAO mainDayDAO = new MaintionMonitorDayDAO();
			return mainDayDAO.getNewDate();
		} catch (Exception e) {
			Log.error(null, e);
		}
		  return "";
	}  

	public List<Map<String, Object>> getProdTypeList(Map<String, Object> param) {
		return mainDayDAO.getProdTypeList(param);
	}
	
	public List<Map<String, Object>>  getIndexList(Map<String, Object> param) {
		return mainDayDAO.getIndexList(param);
	}
	
    /**
     * 表格数据
     */
	public Map<String, Object> getTableData(Map<String, Object> param) {
		return mainDayDAO.getTableData(param);
	}
	
    /**
     * 曲线图
     */
	public Map<String, Object>  bulidLineChart(Map<String, Object> param){
	  	    String dataDate =     MapUtils.getString(param, "dayTime", null).replaceAll("-", "");
		    Map<String, Object> key=new HashMap<String, Object>();
	    	StringBuffer  chartMap=new StringBuffer();
	    	String currentColor="FF0000";
	    	String lastColor="0066FF";
	    	String field="当日值";//当日值
	    	
	    	int numVdivlines =0;
	    	StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
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
		    	  	  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		    	      String lastDay=sdf.format(DateUtil.getDateOfLastMonth(year+month_no+day).getTime());
		    	      String currentValue="0";
		    		  String lastValue="0";
		    		  Map<String, Object> currentObj = mainDayDAO.getChartData(String.valueOf(i),param);
		    		  Map<String, Object> lastObj =    mainDayDAO.getChartData(lastDay,param);
		    	      if (null != currentObj) {
		    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
		    		   }
		    	      if (null != lastObj) {
		    	    	  lastValue =     String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		    		   }
		    		 temp1.append("<category name='"+showDay+"' />\n");
		    		 temp2.append("<set value='"+currentValue+"' hoverText='"+String.valueOf(i)+"： "+currentValue+"' />\n");
		    		 temp3.append("<set value='"+lastValue+"'    hoverText='"+lastDay+"： "+lastValue+"' />\n");
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
        String dataDate =MapUtils.getString(param, "dayTime", null).replaceAll("-", "");
		Map<String, Object> mapKey=new HashMap<String, Object>();
    	String currentColor="FBC62A";
    	String lastColor="71B359";
    	String field="当日累计值";//当日累计值
    	
		StringBuffer  chartMap=new StringBuffer();
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
        temp3.append("<dataset  seriesName='"+lastDay+"'  color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n");    
        
/*        List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
        String labelDisplay="";
        if( param.get("changeZone")!= null && param.get("changeZone").equals("0"))
        {
        	list=mainDayDAO.get21DrillTableData(param);
        	labelDisplay="WRAP";
        }else
        {
        	list=mainDayDAO.getDrillTableData(param);
        	labelDisplay="NONE";
        }
        
        if(param.get("changeZone")!= null && param.get("changeZone").equals("3"))
        {
        	 labelDisplay="WRAP";
        }*/   
        
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
        	  
        	  Map<String, Object> curObj =    mainDayDAO.getChartData(dataDate,params);
     		  Map<String, Object> lastObj =    mainDayDAO.getChartData(lastDay,params);
     		  
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
	public void setMainDayDAO(MaintionMonitorDayDAO mainDayDAO) {
		this.mainDayDAO = mainDayDAO;
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
