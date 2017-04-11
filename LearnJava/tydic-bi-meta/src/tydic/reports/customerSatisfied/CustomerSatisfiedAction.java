package tydic.reports.customerSatisfied;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import tydic.frame.BaseDAO;
import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.meta.common.OprResult;
import tydic.meta.common.yhd.utils.Pager;
import tydic.portalCommon.DateUtil;
import tydic.portalCommon.serviceManage.ServiceProblemDAO;
import tydic.portalCommon.util.IUnionWorkItemService;
import tydic.portalCommon.util.UnionWorkItemService;
import tydic.portalCommon.util.WorkItemBean;
import tydic.portalCommon.util.WorkItemsBean;
import tydic.reports.graph.GraphDAO;
/**
 * 
 * @author qx 客户满意率监测报表
 * 
 */
public class CustomerSatisfiedAction {
	private CustomerSatisfiedDAO customerSatisfiedDAO; 
	private ServiceProblemDAO serviceProblemDAO;
 
	private GraphDAO graphDAO; 
	
	public GraphDAO getGraphDAO() {
		return graphDAO;
	}
	public void setGraphDAO(GraphDAO graphDAO) {
		this.graphDAO = graphDAO;
	}
	public CustomerSatisfiedDAO getCustomerSatisfiedDAO() {
		return customerSatisfiedDAO;
	}
	public void setCustomerSatisfiedDAO(CustomerSatisfiedDAO customerSatisfiedDAO) {
		this.customerSatisfiedDAO = customerSatisfiedDAO;
	}
    public void setServiceProblemDAO(ServiceProblemDAO serviceProblemDAO) {
        this.serviceProblemDAO = serviceProblemDAO;
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
			lastWeek=customerSatisfiedDAO.queryMaxWeek(String.valueOf(lastMonth));
		}else{
			lastweek=Integer.parseInt(currentWeek)-1;
			lastWeek=lastweek+"";
		}
		return lastWeek;
	}
	//客户满意率监测报表_初始化（日，月，周，季度，半年，年）
	public Map getCustomerSatisfied(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(customerSatisfiedDAO.getCustomerSatisfied_Week(queryData));//客户满意率监测报表表格数据
		map.put("barChartMap", getAreaChartXML_CustomerSatisfied(queryData));//客户满意率监测报表柱状图
        return map; 
	}
	//通过触点id动态获取时间列表
	public Map getDateListByIndex(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("dateList", customerSatisfiedDAO.getDateListByIndex(queryData));//客户满意率监测报表柱状图
        return map; 
	}
	//满意度评测总体情况_初始化（日，月，周，季度，半年，年）
	public Map getCustomerSatisfiedSum(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(customerSatisfiedDAO.getCustomerSatisfied_Sum(queryData));//满意度评测总体情况表格数据
		map.put("barChartMap", getAreaChartXML_CustomerSatisfiedSum(queryData));//满意度评测总体情况柱状图
        return map; 
	}
	//客服部关键指标满意率报表
	public Map getCustKeyIndex_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(customerSatisfiedDAO.getCustKeyIndex_Pg(queryData));//满意度评测总体情况表格数据
        return map; 
	}
	

	
	/**
	 *   柱状图XML
	  * 方法描述：客户满意率监测报表_柱状图（日，月，周，季度，半年，年）
	  * @param: 
	  * @return: 
	  * @version: 1.0
	  * @author: qx
	  * @version: 2013-5-31 下午15:22:26
	 */
	private String getAreaChartXML_CustomerSatisfied(Map<String, Object> queryData) {
		StringBuffer  barChartMap=new StringBuffer();
		//柱状图展示
    	String field =       MapUtils.getString(queryData, "field",null);
    	String zoneCode =       MapUtils.getString(queryData, "zoneCode",null);
		String currentColor= "FBC62A";//当周
    	String lastColor=    "71B359";//上周
        
        StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();

		String dateType=MapUtils.getString(queryData, "dateType", null);
		double maxVal=0;
		Map<String,Object> param=new HashMap<String, Object>();		
	    List<Map<String,Object>>barList=null;
		 //周报表
		if("1".equals(dateType)){//客户满意率周报
			String inWeek=MapUtils.getString(queryData, "dateTime", null);//当前周
			
			String curDateStart=inWeek.substring(0,8);//当前周开始日期
			String curDateYear=curDateStart.substring(0,4);
			String curDateMon=curDateStart.substring(4,6);
			String curDateDay=curDateStart.substring(6,8);
			String curDateTime=curDateYear+"-"+curDateMon+"-"+curDateDay;
			
			String curDateEnd=inWeek.substring(9,17);//当前周结束日期
			String dateYear=curDateEnd.substring(0,4);
			String dateMon=curDateEnd.substring(4,6);
			String dateDay=curDateEnd.substring(6,8);
			String dateTime=dateYear+"-"+dateMon+"-"+dateDay;
			
			String lastDateStart=DateUtil.lastDay(curDateTime, -7);
			String lastDateEnd=DateUtil.lastDay(dateTime, -7);
			
			String lastWeek=lastDateStart.replaceAll("-", "")+"~"+lastDateEnd.replaceAll("-", "");
			
		    temp2.append("<dataset  seriesName='" + inWeek + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastWeek + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		
		    barList=customerSatisfiedDAO.getBarCustomerSatisfied(queryData);//各触点数据    
		   
		     //柱状图_周报表
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		  curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  param.put("vTypeId", key.get("V_TYPE_ID"));
		   		  param.put("dateType", "1");
		   		  param.put("zoneCode", zoneCode);
		   	      Map<String, Object> lastObj =    customerSatisfiedDAO.getChartData_CustomerSatisfied(lastWeek,param);
		   		  if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
		   		maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
		   		maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		        	temp1.append("<category label='"+key.get("V_TYPE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		}
		 //月报表
		if("2".equals(dateType)){//客户满意率月报
			String inMonth=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前月
			String lastMonth=DateUtil.getLastMon(inMonth);
			
		    temp2.append("<dataset  seriesName='" + inMonth + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastMonth + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		
		    barList=customerSatisfiedDAO.getBarCustomerSatisfied(queryData);//各触点数据    
		   
		     //柱状图_月报表
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		  curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  param.put("vTypeId", key.get("V_TYPE_ID"));
		   		  param.put("dateType", "2");
		   		  param.put("zoneCode", zoneCode);
		   	      Map<String, Object> lastObj =    customerSatisfiedDAO.getChartData_CustomerSatisfied(lastMonth,param);
		   		  if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
		   		maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
		   		maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		        	temp1.append("<category label='"+key.get("V_TYPE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		}
		 //日报表
		if("0".equals(dateType)){//客户满意率日报
			String inDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前日
			String curDateYear=inDate.substring(0,4);
			String curDateMon=inDate.substring(4,6);
			String curDateDay=inDate.substring(6,8);
			String curDateTime=curDateYear+"-"+curDateMon+"-"+curDateDay;
			String lastDay=DateUtil.lastDay(curDateTime, -1).replaceAll("-", "");//昨日
			
		    temp2.append("<dataset  seriesName='" + inDate + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastDay + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		
		    barList=customerSatisfiedDAO.getBarCustomerSatisfied(queryData);//各触点数据    
		   
		     //柱状图_日报表
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		  curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  param.put("vTypeId", key.get("V_TYPE_ID"));
		   		  param.put("dateType", "0");
		   		  param.put("zoneCode", zoneCode);
		   	      Map<String, Object> lastObj =    customerSatisfiedDAO.getChartData_CustomerSatisfied(lastDay,param);
		   		  if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
		   		maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
		   		maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		        	temp1.append("<category label='"+key.get("V_TYPE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		}
       temp1.append("</categories>\n");
       temp2.append("</dataset>\n");
       temp3.append("</dataset>\n");
       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
       " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
       " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
       " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
       " numdivlines='4' adjustDiv='0' rotateNames='0' yaxismaxvalue='"+maxVal+"'"+
       " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n"); 
       barChartMap.append(temp1).append(temp2).append(temp3);
       barChartMap.append("</chart>");
      return barChartMap.toString();	
  }
	//满意度评测总体情况柱状图
	private String getAreaChartXML_CustomerSatisfiedSum(Map<String, Object> queryData) {
		StringBuffer  barChartMap=new StringBuffer();
		//柱状图展示
    	String field =       MapUtils.getString(queryData, "field",null);
    	String zoneCode =       MapUtils.getString(queryData, "zoneCode",null);
		String currentColor= "FBC62A";//当周
    	String lastColor=    "71B359";//上周
        
        StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();

		String dateType=MapUtils.getString(queryData, "dateType", null);
		double maxVal=0;
		Map<String,Object> param=new HashMap<String, Object>();		
	    List<Map<String,Object>>barList=null;
		 //周报表
		if("1".equals(dateType)){//客户满意率周报
			String inWeek=MapUtils.getString(queryData, "startDate", null);//当前周
			
			String curDateStart=inWeek.substring(0,8);//当前周开始日期
			String curDateYear=curDateStart.substring(0,4);
			String curDateMon=curDateStart.substring(4,6);
			String curDateDay=curDateStart.substring(6,8);
			String curDateTime=curDateYear+"-"+curDateMon+"-"+curDateDay;
			
			String curDateEnd=inWeek.substring(9,17);//当前周结束日期
			String dateYear=curDateEnd.substring(0,4);
			String dateMon=curDateEnd.substring(4,6);
			String dateDay=curDateEnd.substring(6,8);
			String dateTime=dateYear+"-"+dateMon+"-"+dateDay;
			
			String lastDateStart=DateUtil.lastDay(curDateTime, -7);
			String lastDateEnd=DateUtil.lastDay(dateTime, -7);
			
			String lastWeek=lastDateStart.replaceAll("-", "")+"~"+lastDateEnd.replaceAll("-", "");
			
		    temp2.append("<dataset  seriesName='" + inWeek + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastWeek + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		
		    barList=customerSatisfiedDAO.getBarCustomerSatisfiedSum(queryData);//周报
			   
		     //周报
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		  curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  param.put("vTypeId", key.get("V_TYPE_ID"));
		   		  param.put("dateType", "1");
		   		  param.put("zoneCode", zoneCode);
		   	      Map<String, Object> lastObj =    customerSatisfiedDAO.getChartData_CustomerSatisfiedSum(lastWeek,lastWeek,param);
		   		  if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
		   		    maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
		   		    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		        	temp1.append("<category label='"+key.get("V_TYPE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		}
		 //月报表
		if("2".equals(dateType)){//客户满意率月报
			String inMonth=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前月
			String lastMonth=DateUtil.getLastMon(inMonth);
			
		    temp2.append("<dataset  seriesName='" + inMonth + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastMonth + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
	   		
	   		barList=customerSatisfiedDAO.getBarCustomerSatisfiedSum(queryData);//月报   
			   
		     //月报
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		  curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  param.put("vTypeId", key.get("V_TYPE_ID"));
		   		  param.put("dateType", "2");
		   		  param.put("zoneCode", zoneCode);
		   	      Map<String, Object> lastObj =    customerSatisfiedDAO.getChartData_CustomerSatisfiedSum(lastMonth,lastMonth,param);
		   		  if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
			   		maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
			   		maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		        	temp1.append("<category label='"+key.get("V_TYPE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		}
		 //日报表
		if("0".equals(dateType)){//客户满意率日报
			String curDateStart=MapUtils.getString(queryData, "startDate", null);//当前日开始
			String curStart=curDateStart.replaceAll("-", "");
			String curDateEnd=MapUtils.getString(queryData, "endDate", null);//当前日结束
			String curEnd=curDateEnd.replaceAll("-", "");
			String curDate=curDateStart.replaceAll("-", "")+"~"+curDateEnd.replaceAll("-", "");
			
			int interval=0;
			long interval1=DateUtil.getInterval(curEnd,curStart);
			interval=(int)interval1-1;

			String lastDateStart=DateUtil.lastDay(curDateStart, interval);
			String lastStart=lastDateStart.replaceAll("-", "");
			String lastDateEnd=DateUtil.lastDay(curDateEnd, interval);
			String lastEnd=lastDateEnd.replaceAll("-", "");
			String lastDate=lastDateStart.replaceAll("-", "")+"~"+lastDateEnd.replaceAll("-", "");
			
		    temp2.append("<dataset  seriesName='" + curDate + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
		    temp3.append("<dataset  seriesName='" + lastDate + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
		    barList=customerSatisfiedDAO.getBarCustomerSatisfiedSum(queryData);//日报
			   
		     //日报
		       for (Map<String, Object> key : barList){
		       	  String curValue="0";
		   		  String lastValue="0";
		   		  
		   		  curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
		   		  param.put("vTypeId", key.get("V_TYPE_ID"));
		   		  param.put("dateType", "0");
		   		  param.put("zoneCode", zoneCode);
		   	      Map<String, Object> lastObj =    customerSatisfiedDAO.getChartData_CustomerSatisfiedSum(lastStart,lastEnd,param);
		   		  if (null != lastObj) {
		   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
		   		   }else{
		   			  lastValue="0";
		   		   }
		   		maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
		   		maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
		        	temp1.append("<category label='"+key.get("V_TYPE_NAME")+"'/>\n");
		     	    temp2.append("<set      value='"+curValue+"' />\n");
		     	    temp3.append("<set      value='"+lastValue+"' />\n");
		       } 
		}
       temp1.append("</categories>\n");
       temp2.append("</dataset>\n");
       temp3.append("</dataset>\n");
       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
       " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
       " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
       " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
       " numdivlines='4' adjustDiv='0' rotateNames='0' yaxismaxvalue='"+maxVal+"'"+
       " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1' " +
       " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
       " exportFileName='onePic' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");    
       barChartMap.append(temp1).append(temp2).append(temp3);
       barChartMap.append("</chart>");
      return barChartMap.toString();	
  }
	//各触点满意率监测报表_初始化（日，月，周，季度，半年，年）
	public Map getCustomerSatisfied_everyType(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		//周报表
		map.putAll(customerSatisfiedDAO.getEveryType_Week(queryData));//各触点报表表格数据
		map.put("barChartMap", getAreaChartXML(queryData,"1"));//柱状图
		map.put("lineChartMap", getEveryTypeSatisfiedLine(queryData));//折线图
        return map; 
	}
	//各触点满意率监测报表_初始化（日，月，周，季度，半年，年）_修改版
	public Map getCustomerSatisfied_everyTypeMod(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		//周报表
		map.putAll(customerSatisfiedDAO.getEveryType_WeekMod(queryData));//各触点报表表格数据
		map.put("barChartMap", getAreaChartXMLMod(queryData,"1"));//柱状图
		map.put("lineChartMap", getEveryTypeSatisfiedLineMod(queryData));//折线图
        return map; 
	}
	//满意度测评详情总体报表
	public Map getCustomerSatisfiedDetails_Sum(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		String vTypeId=MapUtils.getString(queryData, "actType", null);
		map.putAll(customerSatisfiedDAO.getCustomerSatisfiedDetails_Sum(queryData));//各触点报表表格数据
		map.put("barChartMap", getAreaChartXMLSum(queryData));//柱状图
		map.put("lineChartMap", getEveryTypeSatisfiedLineSum(queryData));//折线图
		map.put("sqlString", "");
        return map; 
	}
	//满意度测评详情总体报表
	public Map getCustomerSatisfiedDetails_Sum_Dg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(customerSatisfiedDAO.getCustomerSatisfiedDetails_Sum_Dg(queryData));//各触点报表表格数据
		map.put("barChartMap", getAreaChartXMLSum_Dg(queryData));//柱状图
		map.put("lineChartMap", getEveryTypeSatisfiedLineSum(queryData));//折线图
        return map; 
	}
	//满意度测评详情总体报表获取图形变更
	public Map getCustomerSatisfiedDetails_GraphSum(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("barChartMap", getAreaChartXMLSum(queryData));//柱状图
		map.put("lineChartMap", getEveryTypeSatisfiedLineSum(queryData));//折线图
        return map; 
	}
	public Map getCustomerSatisfiedDetails_GraphSum_Dg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("barChartMap", getAreaChartXMLSum_Dg(queryData));//柱状图
		map.put("lineChartMap", getEveryTypeSatisfiedLineSum(queryData));//折线图
        return map; 
	}
	//各触点满意率监测报表_初始化（日，月，周，季度，半年，年）_维度修改版
	public Map getCustomerSatisfied_everyTypeDemension(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(queryData.get("currPageNum")));
		page.setSize(Convert.toInt(queryData.get("pageCount")));
		map.putAll(customerSatisfiedDAO.getEveryType_WeekDemension(queryData,page));//维度各触点报表表格数据
        return map; 
	}
	//满意度测评结果周报_初始化（日，月，周，季度，半年，年）_总表
	public Map getCustomerSatisfied_ResultSum(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(queryData.get("currPageNum")));
		page.setSize(Convert.toInt(queryData.get("pageCount")));
		map.putAll(customerSatisfiedDAO.getResultSum(queryData,page));//总表表格数据
        return map; 
	}
	//不满意原因周报
	public Map getNosatisfied_ResultSum(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(customerSatisfiedDAO.getNosatisfied_ResultSum(queryData));//总表表格数据
        return map; 
	}
	//装维IVR不满意原因周报
	public Map getNosatisfiedZWIVR_ResultSum(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(customerSatisfiedDAO.getNosatisfiedZWIVR_ResultSum(queryData));//总表表格数据
        return map; 
	}
	//满意度测评结果周报（各触点）
	public Map getServicesatisfiedResult(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		Pager page = Pager.getInstance();
		page.setCurrNum(Convert.toInt(queryData.get("currPageNum")));
		page.setSize(Convert.toInt(queryData.get("pageCount")));
		map.putAll(customerSatisfiedDAO.getServicesatisfiedResult(queryData,page));
        return map; 
	}
	 //不满意周报列表
    public  Map<String, Object>  getNoSatisfyInstall(Map<String , Object>  paramMap)
	{       Map<String, Object> mapList =new  HashMap<String, Object>();
			List<Map<String,Object>> dataColumn = null;
			dataColumn=customerSatisfiedDAO.getNoSatisfyInstall(paramMap);
			mapList.put("dataColumn", dataColumn);
    	    return mapList;
    }
    //10000号不满意周报列表
    public Map getNoSatisfyResult(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(customerSatisfiedDAO.getNoSatisfyResult(queryData));//总表表格数据
        return map; 
	}
	/**
	 * 导出数据
	 */
	public Map<String, Object> expHfTxtData(Map<String, Object> paramMap) {
		Pager page = (Pager) paramMap.get("page");
		CustomerSatisfiedDAO customerSatisfiedDAO= new CustomerSatisfiedDAO();
		return customerSatisfiedDAO.getEveryType_WeekDemension(paramMap, page);// 记录
	}
	//各触点满意率监测报表_折线图
	public String getEveryTypeSatisfiedLine(Map<String, Object> queryData) {
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
      	
      //String actType=MapUtils.getString(queryData, "actType", null);//触点名称	
		String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
		
		String tempStart="1";//折线图起点
		if("1".equals(dateType)){//周报
		//周报表
			String queryWeek=MapUtils.getString(queryData, "dateTime", null);
			Map<String,Object> monthWeek=customerSatisfiedDAO.queryMonthAndWeek(queryWeek);
			String month=String.valueOf(monthWeek.get("MONTH_ID")) ;//获取当前月份201305
			String week=String.valueOf(monthWeek.get("WEEK_ID"));//获取当前周 3
			String lastMonth=DateUtil.getLastMon(month);//获取上月201304
			
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
	    			  currentObj = customerSatisfiedDAO.getChartData_EveryTypeWeek(String.valueOf(i),month,queryData);
		    		  lastObj =    customerSatisfiedDAO.getChartData_EveryTypeWeek(String.valueOf(i),lastMonth,queryData);
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
	    			  currentObj = customerSatisfiedDAO.getChartData_EveryType(surMon,queryData);
		    		  lastObj =    customerSatisfiedDAO.getChartData_EveryType(lastMon,queryData);
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
	    			  currentObj = customerSatisfiedDAO.getChartData_EveryType(surDay,queryData);
		    		  lastObj =    customerSatisfiedDAO.getChartData_EveryType(lastDay,queryData);
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
	     	        " chartRightMargin='20' chartLeftMargin='10'>\n");    
	         lineChartMap.append(temp1).append(temp2).append(temp3);
	         lineChartMap.append("</chart>");
         return lineChartMap.toString(); 
	}
	//各触点满意率监测报表_折线图_修改版
	public String getEveryTypeSatisfiedLineMod(Map<String, Object> queryData) {
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
      	
      //String actType=MapUtils.getString(queryData, "actType", null);//触点名称	
		String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
		
		String tempStart="1";//折线图起点
		if("1".equals(dateType)){//周报
		//周报表
			Map<String,Object> monthWeek=customerSatisfiedDAO.queryMonthAndWeekMod(queryData);
			String month=String.valueOf(monthWeek.get("MONTH_ID")) ;//获取当前月份201305
			String week=String.valueOf(monthWeek.get("WEEK_ID"));//获取当前周 3
			String lastMonth=DateUtil.getLastMon(month);//获取上月201304
			
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
	    			  currentObj = customerSatisfiedDAO.getChartData_EveryTypeWeekMod(String.valueOf(i),month,queryData);
		    		  lastObj =    customerSatisfiedDAO.getChartData_EveryTypeWeekMod(String.valueOf(i),lastMonth,queryData);
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
	    			  currentObj = customerSatisfiedDAO.getChartData_EveryTypeMod(surMon,queryData);
		    		  lastObj =    customerSatisfiedDAO.getChartData_EveryTypeMod(lastMon,queryData);
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
	    			  currentObj = customerSatisfiedDAO.getChartData_EveryTypeMod(surDay,queryData);
		    		  lastObj =    customerSatisfiedDAO.getChartData_EveryTypeMod(lastDay,queryData);
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
		if(maxVal==minVal){
			minVal=0;
		}
	         temp1.append("</categories>\n");
	         temp2.append("</dataset>\n");
	         temp3.append("</dataset>\n");//yaxisminvalue='10' yaxismaxvalue='1'double maxVal=0; double minVal=0;
	         lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	     	        " numdivlines='4' adjustDiv='0'  yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' rotateNames='0'"+
	     	        " chartRightMargin='20' chartLeftMargin='10'>\n");    
	         lineChartMap.append(temp1).append(temp2).append(temp3);
	         lineChartMap.append("</chart>");
         return lineChartMap.toString(); 
	}
	//满意度测评详情总体报表
	public String getEveryTypeSatisfiedLineSum(Map<String, Object> queryData) {
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
			Map<String,Object> monthWeek=customerSatisfiedDAO.queryMonthAndWeekSum(queryData);
			String month=String.valueOf(monthWeek.get("MONTH_ID")) ;//获取当前月份201305
			String week=String.valueOf(monthWeek.get("WEEK_ID"));//获取当前周 3
			String lastMonth=DateUtil.getLastMon(month);//获取上月201304
			
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
	    			  currentObj = customerSatisfiedDAO.getChartData_EveryTypeWeekSum(String.valueOf(i),month,queryData);
		    		  lastObj =    customerSatisfiedDAO.getChartData_EveryTypeWeekSum(String.valueOf(i),lastMonth,queryData);
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
			String inMonth=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前月
			if (inMonth.length()==0) {
				inMonth="201404";
			}
			
			String month=inMonth.substring(4,6);
			String year=inMonth.substring(0,4);
			String inMonthStart=year+"01";
			String lastMonth=(Integer.parseInt(year)-1)+month;//去年月
			String lastMonthStart=(Integer.parseInt(year)-1)+"01";
			temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inMonth+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastMonth+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
	        
	        List<Map<String, Object>> currentList=null;
	        List<Map<String, Object>> lastList=null;
	        currentList = customerSatisfiedDAO.getChartData_EveryTypeSumLine(inMonthStart,inMonth,queryData);
	        lastList =    customerSatisfiedDAO.getChartData_EveryTypeSumLine(lastMonthStart,lastMonth,queryData);
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
		       	  temp1.append("<category name='"+date1+"月' />\n");
		          temp2.append("<set value='"+curValue+"' hoverText='"+(year+show)+"： "+curValue+"' />\n");
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
		          temp3.append("<set value='"+lastValue+"' hoverText='"+(year+show)+"： "+lastValue+"' />\n");
		    	  numVdivlines++;
		    	  date2++;
		       }
		}if("0".equals(dateType)){//日报
			String inEndDate=MapUtils.getString(queryData, "endDate", null).replaceAll("-", "");//当前结束日期
			String inYearMon=inEndDate.substring(0,6);//月
			String inStartDate=inYearMon+"01";
			String inEndDay=inEndDate.substring(6,8);//结束日
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月
			String lastStartDate=lastYearMon+"01";
			String lastEndDate=lastYearMon+inEndDay;
			
			temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
	        List<Map<String, Object>> currentList=null;
	        List<Map<String, Object>> lastList=null;
	        currentList = customerSatisfiedDAO.getChartData_EveryTypeSumLine(inStartDate,inEndDate,queryData);
	        lastList =    customerSatisfiedDAO.getChartData_EveryTypeSumLine(lastStartDate,lastEndDate,queryData);
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
		}
		if(maxVal==minVal){
			minVal=0;
		}
	         temp1.append("</categories>\n");
	         temp2.append("</dataset>\n");
	         temp3.append("</dataset>\n");//yaxisminvalue='10' yaxismaxvalue='1'double maxVal=0; double minVal=0;
	         lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	     	        " numdivlines='4' adjustDiv='0'  yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' rotateNames='0'"+
	     	        " chartRightMargin='20' chartLeftMargin='10'"+
	     	        " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
                    " exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	         lineChartMap.append(temp1).append(temp2).append(temp3);
	         lineChartMap.append("</chart>");
         return lineChartMap.toString(); 
	}
	   /**
	    *  21个地市
	     * 方法描述：
	     * @param: 
	     * @return: 
	     * @version: 1.0
	     * @author: qx
	     * @version: 2013-4-24 下午04:21:10
	    */
	   public Map<String, Object> loadSet21AreaChart(Map<String, Object> map){
		   String changeZone=MapUtils.getString(map, "changeZone",    null);//0代表五大区，1代表21地市
		   Map<String, Object> key=new HashMap<String, Object>(); 
		   String strXML=getAreaChartXML(map,changeZone);
		   key.put("XML",strXML);
		   return key;
	   }
	   /**
	    *  21个地市
	     * 方法描述：
	     * @param: 
	     * @return: 
	     * @version: 1.0
	     * @author: qx
	     * @version: 2013-8-2下午04:21:10
	    */
	   public Map<String, Object> loadSet21AreaChartMod(Map<String, Object> map){
		   String changeZone=MapUtils.getString(map, "changeZone",    null);//0代表五大区，1代表21地市
		   Map<String, Object> key=new HashMap<String, Object>(); 
		   String strXML=getAreaChartXMLMod(map,changeZone);
		   key.put("XML",strXML);
		   return key;
	   }
	   //测评满意度详情总体
	   public Map<String, Object> loadSet21AreaChartSum(Map<String, Object> map){
		   Map<String, Object> key=new HashMap<String, Object>(); 
		   String strXML=getAreaChartXMLSum(map);
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
		  * @version: 2013-5-20 下午05:22:26
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
	      	
	      	double maxVal=0;
 
			String actType=MapUtils.getString(queryData, "actType", null);//触点名称	
			String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
			Map<String,Object> param=new HashMap<String, Object>();			
		    List<Map<String,Object>>barList=null;
		    	if("1".equals(dateType)){//周报
					//宽带新装
		            String inWeek=MapUtils.getString(queryData, "dateTime", null);//当前周
					
					String curDateStart=inWeek.substring(0,8);//当前周开始日期
					String curDateYear=curDateStart.substring(0,4);
					String curDateMon=curDateStart.substring(4,6);
					String curDateDay=curDateStart.substring(6,8);
					String curDateTime=curDateYear+"-"+curDateMon+"-"+curDateDay;
					
					String curDateEnd=inWeek.substring(9,17);//当前周结束日期
					String dateYear=curDateEnd.substring(0,4);
					String dateMon=curDateEnd.substring(4,6);
					String dateDay=curDateEnd.substring(6,8);
					String dateTime=dateYear+"-"+dateMon+"-"+dateDay;
					
					String lastDateStart=DateUtil.lastDay(curDateTime, -7);
					String lastDateEnd=DateUtil.lastDay(dateTime, -7);
					
					String lastWeek=lastDateStart.replaceAll("-", "")+"~"+lastDateEnd.replaceAll("-", "");

					temp2.append("<dataset  seriesName='" + inWeek + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
					temp3.append("<dataset  seriesName='" + lastWeek + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

					if ("1".equals(changeZone)) {
						barList = customerSatisfiedDAO.getBarEveryType_5Zone(zoneCode);// 五大区柱状图数据
					}
					if ("0".equals(changeZone)) {
						barList = customerSatisfiedDAO.getBarEveryType_21Zone(zoneCode);//21地市
					}
				        //柱状图_周报表
					for (Map<String, Object> key : barList) {
						String curValue = "0";
						String lastValue = "0";
						param.put("zoneCode", String.valueOf(key.get("ZONE_CODE") == null ? "0" : key.get("ZONE_CODE")));
						param.put("actType", actType);
						param.put("dateType", dateType);
						  
						Map<String, Object> lastObj = customerSatisfiedDAO.getChartData_EveryType(lastWeek, param);
						Map<String, Object> curObj = customerSatisfiedDAO.getChartData_EveryType(inWeek, param);
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
						maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
						maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
						temp1.append("<category label='" + key.get("ZONE_NAME")+ "'/>\n");
						temp2.append("<set      value='" + curValue + "' />\n");
						temp3.append("<set      value='" + lastValue + "' />\n");
					}
		    	}
		    	if("2".equals(dateType)){//月报
		    		String inMonth=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前月
					String lastMonth=DateUtil.getLastMon(inMonth);

					temp2.append("<dataset  seriesName='" + inMonth + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
					temp3.append("<dataset  seriesName='" + lastMonth + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

					if ("1".equals(changeZone)) {
						barList = customerSatisfiedDAO.getBarEveryType_5Zone(zoneCode);// 五大区柱状图数据
					}
					if ("0".equals(changeZone)) {
						barList = customerSatisfiedDAO.getBarEveryType_21Zone(zoneCode);//21地市
					}
				        //柱状图_周报表
					for (Map<String, Object> key : barList) {
						String curValue = "0";
						String lastValue = "0";
						param.put("zoneCode", String.valueOf(key.get("ZONE_CODE") == null ? "0" : key.get("ZONE_CODE")));
						param.put("actType", actType);
						param.put("dateType", dateType);
						  
						Map<String, Object> lastObj = customerSatisfiedDAO.getChartData_EveryType(lastMonth, param);
						Map<String, Object> curObj = customerSatisfiedDAO.getChartData_EveryType(inMonth, param);
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
						maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
						maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
						temp1.append("<category label='" + key.get("ZONE_NAME")+ "'/>\n");
						temp2.append("<set      value='" + curValue + "' />\n");
						temp3.append("<set      value='" + lastValue + "' />\n");
					}
		    	}
		    	if("0".equals(dateType)){//日报
					String inDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前日
					String curDateYear=inDate.substring(0,4);
					String curDateMon=inDate.substring(4,6);
					String curDateDay=inDate.substring(6,8);
					String curDateTime=curDateYear+"-"+curDateMon+"-"+curDateDay;
					String lastDate=DateUtil.lastDay(curDateTime, -1).replaceAll("-", "");//昨日

					temp2.append("<dataset  seriesName='" + inDate + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
					temp3.append("<dataset  seriesName='" + lastDate + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

					if ("1".equals(changeZone)) {
						barList = customerSatisfiedDAO.getBarEveryType_5Zone(zoneCode);// 五大区柱状图数据
					}
					if ("0".equals(changeZone)) {
						barList = customerSatisfiedDAO.getBarEveryType_21Zone(zoneCode);//21地市
					}
				        //柱状图_周报表
					for (Map<String, Object> key : barList) {
						String curValue = "0";
						String lastValue = "0";
						param.put("zoneCode", String.valueOf(key.get("ZONE_CODE") == null ? "0" : key.get("ZONE_CODE")));
						param.put("actType", actType);
						param.put("dateType", dateType);
						  
						Map<String, Object> lastObj = customerSatisfiedDAO.getChartData_EveryType(lastDate, param);
						Map<String, Object> curObj = customerSatisfiedDAO.getChartData_EveryType(inDate, param);
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
						maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
						maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
						temp1.append("<category label='" + key.get("ZONE_NAME")+ "'/>\n");
						temp2.append("<set      value='" + curValue + "' />\n");
						temp3.append("<set      value='" + lastValue + "' />\n");
					}
		    	}
	       temp1.append("</categories>\n");
	       temp2.append("</dataset>\n");
	       temp3.append("</dataset>\n");
	       if("0".equals(changeZone)){
	    	   barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	               " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	               " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	               " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	               " numdivlines='4' adjustDiv='0' rotateNames='0' yaxismaxvalue='"+maxVal+"'"+
	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");
	       }if("1".equals(changeZone)){
	       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	              " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	              " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	              " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	              " numdivlines='4' adjustDiv='0' rotateNames='0' yaxismaxvalue='"+maxVal+"'"+
	              " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");   
	       }    
	       barChartMap.append(temp1).append(temp2).append(temp3);
	       barChartMap.append("</chart>");
	      return barChartMap.toString();	
	  }
		private String getAreaChartXMLMod(Map<String, Object> queryData,String changeZone) {
			StringBuffer  barChartMap=new StringBuffer();
			//柱状图展示
	    	String field =       MapUtils.getString(queryData, "field",null);
	    	String zoneCode =       MapUtils.getString(queryData, "zoneCode",null);
			String currentColor= "FBC62A";//当月
	    	String lastColor=    "71B359";//上月
	    	
	    	double maxVal=0;
	    	
	        StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
 
			String actType=MapUtils.getString(queryData, "actType", null);//触点名称	
			String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
			Map<String,Object> param=new HashMap<String, Object>();			
		    List<Map<String,Object>>barList=null;
		    	if("1".equals(dateType)){//周报
					//宽带新装
		            String inWeek=MapUtils.getString(queryData, "dateTime", null);//当前周
					
					String curDateStart=inWeek.substring(0,8);//当前周开始日期
					String curDateYear=curDateStart.substring(0,4);
					String curDateMon=curDateStart.substring(4,6);
					String curDateDay=curDateStart.substring(6,8);
					String curDateTime=curDateYear+"-"+curDateMon+"-"+curDateDay;
					
					String curDateEnd=inWeek.substring(9,17);//当前周结束日期
					String dateYear=curDateEnd.substring(0,4);
					String dateMon=curDateEnd.substring(4,6);
					String dateDay=curDateEnd.substring(6,8);
					String dateTime=dateYear+"-"+dateMon+"-"+dateDay;
					
					String lastDateStart=DateUtil.lastDay(curDateTime, -7);
					String lastDateEnd=DateUtil.lastDay(dateTime, -7);
					
					String lastWeek=lastDateStart.replaceAll("-", "")+"~"+lastDateEnd.replaceAll("-", "");

					temp2.append("<dataset  seriesName='" + inWeek + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
					temp3.append("<dataset  seriesName='" + lastWeek + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

					barList=graphDAO.getChangeZoneLevel(queryData);// 柱状图数据
				        //柱状图_周报表
					for (Map<String, Object> key : barList) {
						String curValue = "0";
						String lastValue = "0";
						param.put("zoneCode", String.valueOf(key.get("ZONE_CODE") == null ? "0" : key.get("ZONE_CODE")));
						param.put("actType", actType);
						param.put("dateType", dateType);
						  
						Map<String, Object> lastObj = customerSatisfiedDAO.getChartData_EveryTypeMod(lastWeek, param);
						Map<String, Object> curObj = customerSatisfiedDAO.getChartData_EveryTypeMod(inWeek, param);
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
						  maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
			    	      maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
						temp1.append("<category label='" + key.get("ZONE_NAME")+ "'/>\n");
						temp2.append("<set      value='" + curValue + "' />\n");
						temp3.append("<set      value='" + lastValue + "' />\n");
					}
		    	}
		    	if("2".equals(dateType)){//月报
		    		String inMonth=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前月
					String lastMonth=DateUtil.getLastMon(inMonth);

					temp2.append("<dataset  seriesName='" + inMonth + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
					temp3.append("<dataset  seriesName='" + lastMonth + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

					barList=graphDAO.getChangeZoneLevel(queryData);// 柱状图数据
				        //柱状图_周报表
					for (Map<String, Object> key : barList) {
						String curValue = "0";
						String lastValue = "0";
						param.put("zoneCode", String.valueOf(key.get("ZONE_CODE") == null ? "0" : key.get("ZONE_CODE")));
						param.put("actType", actType);
						param.put("dateType", dateType);
						  
						Map<String, Object> lastObj = customerSatisfiedDAO.getChartData_EveryTypeMod(lastMonth, param);
						Map<String, Object> curObj = customerSatisfiedDAO.getChartData_EveryTypeMod(inMonth, param);
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
						maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
			    	    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
						temp1.append("<category label='" + key.get("ZONE_NAME")+ "'/>\n");
						temp2.append("<set      value='" + curValue + "' />\n");
						temp3.append("<set      value='" + lastValue + "' />\n");
					}
		    	}
		    	if("0".equals(dateType)){//日报
					String inDate=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前日
					String curDateYear=inDate.substring(0,4);
					String curDateMon=inDate.substring(4,6);
					String curDateDay=inDate.substring(6,8);
					String curDateTime=curDateYear+"-"+curDateMon+"-"+curDateDay;
					String lastDate=DateUtil.lastDay(curDateTime, -1).replaceAll("-", "");//昨日

					temp2.append("<dataset  seriesName='" + inDate + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
					temp3.append("<dataset  seriesName='" + lastDate + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

					barList=graphDAO.getChangeZoneLevel(queryData);// 柱状图数据
				        //柱状图_周报表
					for (Map<String, Object> key : barList) {
						String curValue = "0";
						String lastValue = "0";
						param.put("zoneCode", String.valueOf(key.get("ZONE_CODE") == null ? "0" : key.get("ZONE_CODE")));
						param.put("actType", actType);
						param.put("dateType", dateType);
						  
						Map<String, Object> lastObj = customerSatisfiedDAO.getChartData_EveryTypeMod(lastDate, param);
						Map<String, Object> curObj = customerSatisfiedDAO.getChartData_EveryTypeMod(inDate, param);
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
						maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
			    	    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
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
	               " numdivlines='4' adjustDiv='0' rotateNames='0' yaxismaxvalue='"+maxVal+"' "+
	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");
	       barChartMap.append(temp1).append(temp2).append(temp3);
	       barChartMap.append("</chart>");
	      return barChartMap.toString();	
	  }
		
private String getAreaChartXMLSum(Map<String, Object> queryData) {
			StringBuffer  barChartMap=new StringBuffer();
			//柱状图展示
	    	String field =       MapUtils.getString(queryData, "field",null);
			String currentColor= "FBC62A";//当月
	    	String lastColor=    "71B359";//上月
	    	
	    	double maxVal=0;
            double minVal=0;
            
            int numVdivlines =-1;
	    	
	        StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
 
			String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
		    	if("1".equals(dateType)){//周报
					//宽带新装
		            String inWeek=MapUtils.getString(queryData, "startDate", null);//当前周
					
					String curDateStart=inWeek.substring(0,8);//当前周开始日期
					String curDateYear=curDateStart.substring(0,4);
					String curDateMon=curDateStart.substring(4,6);
					String curDateDay=curDateStart.substring(6,8);
					String curDateTime=curDateYear+"-"+curDateMon+"-"+curDateDay;
					
					String curDateEnd=inWeek.substring(9,17);//当前周结束日期
					String dateYear=curDateEnd.substring(0,4);
					String dateMon=curDateEnd.substring(4,6);
					String dateDay=curDateEnd.substring(6,8);
					String dateTime=dateYear+"-"+dateMon+"-"+dateDay;
					
					String lastDateStart=DateUtil.lastDay(curDateTime, -7);
					String lastDateEnd=DateUtil.lastDay(dateTime, -7);
					
					String lastWeek=lastDateStart.replaceAll("-", "")+"~"+lastDateEnd.replaceAll("-", "");

					temp2.append("<dataset  seriesName='" + inWeek + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
					temp3.append("<dataset  seriesName='" + lastWeek + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

					List<Map<String,Object>>barList1=customerSatisfiedDAO.getChartData_EveryTypeSumByDate(inWeek,inWeek,queryData);//本月
					List<Map<String,Object>>barList2=customerSatisfiedDAO.getChartData_EveryTypeSumByDate(lastWeek,lastWeek,queryData);//上月
					
				        //柱状图_周报表
					for (Map<String, Object> key1 : barList1) {
						String curValue = "0";
						curValue = String.valueOf(key1.get(field) == null ? "0": key1.get(field));
						maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
			    	    minVal=compareMinValue(minVal,Double.parseDouble(curValue));  
						temp1.append("<category label='" + key1.get("REGION_NAME")+ "'/>\n");
						temp2.append("<set      value='" + curValue + "' />\n");
					}for (Map<String, Object> key2 : barList2) {
						String lastValue = "0";
					    lastValue = String.valueOf(key2.get(field) == null ? "0": key2.get(field));
			    	    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
			    	    minVal=compareMinValue(minVal,Double.parseDouble(lastValue));  
						temp3.append("<set      value='" + lastValue + "' />\n");
						numVdivlines=numVdivlines+1;
					}
		    	}
		    	if("2".equals(dateType)){//月报
		    		String inMonth=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前月
		    		if(inMonth.length()==0){
		    			inMonth="201404";
		    		}
					String lastMonth=DateUtil.getLastMon(inMonth);

					temp2.append("<dataset  seriesName='" + inMonth + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
					temp3.append("<dataset  seriesName='" + lastMonth + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
					
					List<Map<String,Object>>barList1=customerSatisfiedDAO.getChartData_EveryTypeSumByDate(inMonth,inMonth,queryData);//本月
					List<Map<String,Object>>barList2=customerSatisfiedDAO.getChartData_EveryTypeSumByDate(lastMonth,lastMonth,queryData);//上月
					
				        //柱状图_周报表
					for (Map<String, Object> key1 : barList1) {
						String curValue = "0";
						curValue = String.valueOf(key1.get(field) == null ? "0": key1.get(field));
						maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
			    	    minVal=compareMinValue(minVal,Double.parseDouble(curValue));  
						temp1.append("<category label='" + key1.get("REGION_NAME")+ "'/>\n");
						temp2.append("<set      value='" + curValue + "' />\n");
					}for (Map<String, Object> key2 : barList2) {
						String lastValue = "0";
					    lastValue = String.valueOf(key2.get(field) == null ? "0": key2.get(field));
					    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
			    	    minVal=compareMinValue(minVal,Double.parseDouble(lastValue));  
						temp3.append("<set      value='" + lastValue + "' />\n");
						numVdivlines=numVdivlines+1;
					}
		    	}
		    	if("0".equals(dateType)){//日报
		    		String curDateStart=MapUtils.getString(queryData, "startDate", null);//当前日开始
					String curStart=curDateStart.replaceAll("-", "");
					String curDateEnd=MapUtils.getString(queryData, "endDate", null);//当前日结束
					String curEnd=curDateEnd.replaceAll("-", "");
					String curDate=curDateStart.replaceAll("-", "")+"~"+curDateEnd.replaceAll("-", "");
					
					int interval=0;
					long interval1=DateUtil.getInterval(curEnd,curStart);
					interval=(int)interval1-1;

					String lastDateStart=DateUtil.lastDay(curDateStart, interval);
					String lastStart=lastDateStart.replaceAll("-", "");
					String lastDateEnd=DateUtil.lastDay(curDateEnd, interval);
					String lastEnd=lastDateEnd.replaceAll("-", "");
					String lastDate=lastDateStart.replaceAll("-", "")+"~"+lastDateEnd.replaceAll("-", "");

					temp2.append("<dataset  seriesName='" + curDate + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
					temp3.append("<dataset  seriesName='" + lastDate + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

					List<Map<String,Object>>barList1=customerSatisfiedDAO.getChartData_EveryTypeSumByDate(curStart,curEnd,queryData);//本月
					List<Map<String,Object>>barList2=customerSatisfiedDAO.getChartData_EveryTypeSumByDate(lastStart,lastEnd,queryData);//上月
					
				        //柱状图_周报表
					for (Map<String, Object> key1 : barList1) {
						String curValue = "0";
						curValue = String.valueOf(key1.get(field) == null ? "0": key1.get(field));
						maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
			    	    minVal=compareMinValue(minVal,Double.parseDouble(curValue));  
						temp1.append("<category label='" + key1.get("REGION_NAME")+ "'/>\n");
						temp2.append("<set      value='" + curValue + "' />\n");
					}for (Map<String, Object> key2 : barList2) {
						String lastValue = "0";
					    lastValue = String.valueOf(key2.get(field) == null ? "0": key2.get(field));
					    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
			    	    minVal=compareMinValue(minVal,Double.parseDouble(lastValue));  
						temp3.append("<set      value='" + lastValue + "' />\n");
						numVdivlines=numVdivlines+1;
					}
		    	}
		   if(maxVal==minVal){
			 minVal=0;
		   } 
	       temp1.append("</categories>\n");
	       temp2.append("</dataset>\n");
	       temp3.append("</dataset>\n");
	       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	               " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	               " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	               " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	               " numdivlines='4' adjustDiv='0' rotateNames='0' yaxismaxvalue='"+maxVal+"' numVdivlines='"+numVdivlines+"' yaxisminvalue='"+minVal+"' "+
	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'" +
	               " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	               " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	       barChartMap.append(temp1).append(temp2).append(temp3);
	       barChartMap.append("</chart>");
	      return barChartMap.toString();	
	  }
		private String getAreaChartXMLSum_Dg(Map<String, Object> queryData) {
			StringBuffer  barChartMap=new StringBuffer();
			//柱状图展示
	    	String field =       MapUtils.getString(queryData, "field",null);
			String currentColor= "FBC62A";//当月
	    	String lastColor=    "71B359";//上月
	    	
	    	double maxVal=0;
	    	
	        StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
 
			String dateType=MapUtils.getString(queryData, "dateType", null);//日期类型
		    	if("1".equals(dateType)){//周报
					//宽带新装
		            String inWeek=MapUtils.getString(queryData, "startDate", null);//当前周
					
					String curDateStart=inWeek.substring(0,8);//当前周开始日期
					String curDateYear=curDateStart.substring(0,4);
					String curDateMon=curDateStart.substring(4,6);
					String curDateDay=curDateStart.substring(6,8);
					String curDateTime=curDateYear+"-"+curDateMon+"-"+curDateDay;
					
					String curDateEnd=inWeek.substring(9,17);//当前周结束日期
					String dateYear=curDateEnd.substring(0,4);
					String dateMon=curDateEnd.substring(4,6);
					String dateDay=curDateEnd.substring(6,8);
					String dateTime=dateYear+"-"+dateMon+"-"+dateDay;
					
					String lastDateStart=DateUtil.lastDay(curDateTime, -7);
					String lastDateEnd=DateUtil.lastDay(dateTime, -7);
					
					String lastWeek=lastDateStart.replaceAll("-", "")+"~"+lastDateEnd.replaceAll("-", "");

					temp2.append("<dataset  seriesName='" + inWeek + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
					temp3.append("<dataset  seriesName='" + lastWeek + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

					List<Map<String,Object>>barList1=customerSatisfiedDAO.getChartData_EveryTypeSumByDate_Dg(inWeek,inWeek,queryData);//本月
					List<Map<String,Object>>barList2=customerSatisfiedDAO.getChartData_EveryTypeSumByDate_Dg(lastWeek,lastWeek,queryData);//上月
					
				        //柱状图_周报表
					for (Map<String, Object> key1 : barList1) {
						String curValue = "0";
						curValue = String.valueOf(key1.get(field) == null ? "0": key1.get(field));
						maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
						temp1.append("<category label='" + key1.get("REGION_NAME")+ "'/>\n");
						temp2.append("<set      value='" + curValue + "' />\n");
					}for (Map<String, Object> key2 : barList2) {
						String lastValue = "0";
					    lastValue = String.valueOf(key2.get(field) == null ? "0": key2.get(field));
			    	    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
						temp3.append("<set      value='" + lastValue + "' />\n");
					}
		    	}
		    	if("2".equals(dateType)){//月报
		    		String inMonth=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前月
					String lastMonth=DateUtil.getLastMon(inMonth);

					temp2.append("<dataset  seriesName='" + inMonth + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
					temp3.append("<dataset  seriesName='" + lastMonth + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
					
					List<Map<String,Object>>barList1=customerSatisfiedDAO.getChartData_EveryTypeSumByDate_Dg(inMonth,inMonth,queryData);//本月
					List<Map<String,Object>>barList2=customerSatisfiedDAO.getChartData_EveryTypeSumByDate_Dg(lastMonth,lastMonth,queryData);//上月
					
				        //柱状图_周报表
					for (Map<String, Object> key1 : barList1) {
						String curValue = "0";
						curValue = String.valueOf(key1.get(field) == null ? "0": key1.get(field));
						maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
						temp1.append("<category label='" + key1.get("REGION_NAME")+ "'/>\n");
						temp2.append("<set      value='" + curValue + "' />\n");
					}for (Map<String, Object> key2 : barList2) {
						String lastValue = "0";
					    lastValue = String.valueOf(key2.get(field) == null ? "0": key2.get(field));
			    	    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
						temp3.append("<set      value='" + lastValue + "' />\n");
					}
		    	}
		    	if("0".equals(dateType)){//日报
		    		String curDateStart=MapUtils.getString(queryData, "startDate", null);//当前日开始
					String curStart=curDateStart.replaceAll("-", "");
					String curDateEnd=MapUtils.getString(queryData, "endDate", null);//当前日结束
					String curEnd=curDateEnd.replaceAll("-", "");
					String curDate=curDateStart.replaceAll("-", "")+"~"+curDateEnd.replaceAll("-", "");
					
					int interval=0;
					long interval1=DateUtil.getInterval(curEnd,curStart);
					interval=(int)interval1-1;

					String lastDateStart=DateUtil.lastDay(curDateStart, interval);
					String lastStart=lastDateStart.replaceAll("-", "");
					String lastDateEnd=DateUtil.lastDay(curDateEnd, interval);
					String lastEnd=lastDateEnd.replaceAll("-", "");
					String lastDate=lastDateStart.replaceAll("-", "")+"~"+lastDateEnd.replaceAll("-", "");

					temp2.append("<dataset  seriesName='" + curDate + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
					temp3.append("<dataset  seriesName='" + lastDate + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");

					List<Map<String,Object>>barList1=customerSatisfiedDAO.getChartData_EveryTypeSumByDate_Dg(curStart,curEnd,queryData);//本月
					List<Map<String,Object>>barList2=customerSatisfiedDAO.getChartData_EveryTypeSumByDate_Dg(lastStart,lastEnd,queryData);//上月
					
				        //柱状图_周报表
					for (Map<String, Object> key1 : barList1) {
						String curValue = "0";
						curValue = String.valueOf(key1.get(field) == null ? "0": key1.get(field));
						maxVal=compareMaxValue(maxVal,Double.parseDouble(curValue));
						temp1.append("<category label='" + key1.get("REGION_NAME")+ "'/>\n");
						temp2.append("<set      value='" + curValue + "' />\n");
					}for (Map<String, Object> key2 : barList2) {
						String lastValue = "0";
					    lastValue = String.valueOf(key2.get(field) == null ? "0": key2.get(field));
			    	    maxVal=compareMaxValue(maxVal,Double.parseDouble(lastValue));
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
	               " numdivlines='4' adjustDiv='0' rotateNames='0' yaxismaxvalue='"+maxVal+"' "+
	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'" +
	               " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	               " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	       barChartMap.append(temp1).append(temp2).append(temp3);
	       barChartMap.append("</chart>");
	      return barChartMap.toString();	
	  }
		//宽带新装不满意原因TOP周报
		public Map getNOSatisfiedReason_Week(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			//周报表
			map.put("pieChartMap",getNoSatisfiedReasonPie(queryData));//饼图
			map.putAll(customerSatisfiedDAO.getNOSatisfiedReason_Week(queryData));//宽带新装不满意原因TOP周报表格数据
	        return map; 
		}
		//满意度不满意原因TOP_最新
		public Map getNOSatisfiedReason(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("pieChartMap",getNoSatisfiedPie(queryData));//饼图
			map.putAll(customerSatisfiedDAO.getNOSatisfiedReason(queryData));//不满意原因TOP表格数据
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
		private String getNoSatisfiedReasonPie(Map<String, Object> queryData) {
			StringBuffer  pieChartMap=new StringBuffer();
			//饼图展示
	      	StringBuffer  temp=new StringBuffer();
		    List<Map<String,Object>>barList=null;
		    String field =MapUtils.getString(queryData, "field",null);
			//宽带新装不满意原因TOP饼图
			barList = customerSatisfiedDAO.getEveryNoSatisfiedReason(queryData);// 不满意原因列表
			for (Map<String, Object> key : barList) {
				String reasonName = String.valueOf(key.get("ERR_REASON_NAME") == null ? "-": key.get("ERR_REASON_NAME"));
	            String curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
				temp.append("<set label='"+reasonName+"' value='"+curValue+"'/> ");
			}
		   pieChartMap.append("<chart caption='不满意原因 ' animation='1' showBorder='1' startingAngle='70' " +
	       		"bgAngle='360' bgRatio='0,100' bgColor='99ccff,ffffff' manageLabelOverflow='1' " +
	       		"pieRadius='152' enableSmartLabels='1' slicingDistance='30' " +
	       		"decimals='0' palette='4' formatNumberScale='0'>\n");   
		   pieChartMap.append(temp);
		   pieChartMap.append("</chart>");
	      return pieChartMap.toString();	
	  }	
		//不满意原因top饼图_最新
		private String getNoSatisfiedPie(Map<String, Object> queryData) {
			StringBuffer  pieChartMap=new StringBuffer();
			String field =MapUtils.getString(queryData, "field",null);
			//饼图展示
	      	StringBuffer  temp=new StringBuffer();
		    String vTypeId =MapUtils.getString(queryData, "actType",null);	
			//宽带新装不满意原因TOP饼图
			
			if("5".equals(vTypeId)){
				String reasonCode=MapUtils.getString(queryData, "reasonCode",null);	
				boolean isRootLeaf=customerSatisfiedDAO.getIsRootLeaf(reasonCode);//是否为根节点
				boolean isLeaf=customerSatisfiedDAO.getIsLeaf(reasonCode);//是否为叶子节点
				queryData.put("isRootLeaf",isRootLeaf);
				queryData.put("isLeaf",isLeaf);
				List<Map<String,Object>>barList1 = customerSatisfiedDAO.getNoSatisfiedReasonPieList(queryData);// 不满意原因列表
				if(isRootLeaf){//为根节点
					for (Map<String, Object> key : barList1) {
						String reasonName = String.valueOf(key.get("ERR_REASON_NAME") == null ? "-": key.get("ERR_REASON_NAME"));
			            String curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
						temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-chartChange('"+key.get("ERR_REASON_ID")+"','"+MapUtils.getString(queryData, "dateType", null)+"','1')\"/> ");
					}
				}else{//非根节点
					for (Map<String, Object> key : barList1) {
						String reasonName = String.valueOf(key.get("ERR_REASON_NAME_2") == null ? "-": key.get("ERR_REASON_NAME_2"));
			            String curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
						temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-chartChange('"+key.get("ERR_REASON_ID_2")+"','"+MapUtils.getString(queryData, "dateType", null)+"','0')\"/> ");
					}
				}
			
			}/*else if("11".equals(vTypeId)||"12".equals(vTypeId)){
				String reasonCode=MapUtils.getString(queryData, "reasonCode",null);	
				boolean isRootLeaf=customerSatisfiedDAO.getIsRootLeaf(reasonCode);//是否为根节点
				boolean isLeaf=customerSatisfiedDAO.getIsLeaf(reasonCode);//是否为叶子节点
				queryData.put("isRootLeaf",isRootLeaf);
				queryData.put("isLeaf",isLeaf);
				List<Map<String,Object>>barList1 = customerSatisfiedDAO.getNoSatisfiedReasonPieList(queryData);// 不满意原因列表
				if(isRootLeaf){//为根节点
					for (Map<String, Object> key : barList1) {
						String reasonName = String.valueOf(key.get("ERR_REASON_NAME") == null ? "-": key.get("ERR_REASON_NAME"));
			            String curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
						temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-chartChange('"+key.get("ERR_REASON_ID")+"','"+MapUtils.getString(queryData, "dateType", null)+"','1')\"/> ");
					}
				}else{//非根节点
					for (Map<String, Object> key : barList1) {
						String reasonName = String.valueOf(key.get("ERR_REASON_NAME2") == null ? "-": key.get("ERR_REASON_NAME2"));
			            String curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
						temp.append("<set label='"+reasonName+"' value='"+curValue+"' link=\"j-chartChange('"+key.get("ERR_REASON_ID2")+"','"+MapUtils.getString(queryData, "dateType", null)+"','0')\"/> ");
					}
				}
			
			}*/else{
				 List<Map<String,Object>>barList = customerSatisfiedDAO.getNoSatisfiedReasonPieList(queryData);// 不满意原因列表
				for (Map<String, Object> key : barList) {
					String reasonName = String.valueOf(key.get("ERR_REASON_NAME") == null ? "-": key.get("ERR_REASON_NAME"));
		            String curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
					temp.append("<set label='"+reasonName+"' value='"+curValue+"' /> ");
				}
			}
		   pieChartMap.append("<chart caption='不满意原因 ' animation='1' showBorder='1' startingAngle='70' " +
	       		"bgAngle='360' bgRatio='0,100' bgColor='99ccff,ffffff' manageLabelOverflow='1' " +
	       		"pieRadius='152' enableSmartLabels='1' slicingDistance='30' " +
	       		"decimals='0' palette='4' formatNumberScale='0' " +
	       		" exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	            " exportFileName='onePic' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
		   pieChartMap.append(temp);
		   pieChartMap.append("</chart>");
	      return pieChartMap.toString();	
	  }
		
		public Map<String, Object> getNosatisDetailData(Map<String, Object> paramMap) {
			Pager page = Pager.getInstance();
			page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
			page.setSize(Convert.toInt(paramMap.get("pageCount")));
			return customerSatisfiedDAO.getNosatisDetailData(paramMap, page);
		}
		
		public Map<String, Object> getEWAMDetailData(Map<String, Object> paramMap) {
			Pager page = Pager.getInstance();
			page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
			page.setSize(Convert.toInt(paramMap.get("pageCount")));
			return customerSatisfiedDAO.getEWAMDetailData(paramMap, page);
		}
		
		public Map<String, Object> getEWAMDetailLog(Map<String, Object> paramMap) {
			Pager page = Pager.getInstance();
			page.setCurrNum(Convert.toInt(paramMap.get("currPageNum")));
			page.setSize(Convert.toInt(paramMap.get("pageCount")));
			return customerSatisfiedDAO.getEWAMDetailLog(paramMap, page);
		}
		
		//所有子节点
		public Map getUserSubCode(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			List<Map<String,Object>> userCodeList=customerSatisfiedDAO.getUserSubCode(queryData);
			map.put("userCodeList", userCodeList);
			return map;
		}
		
		/**
		 * 满意度回访处理结果清单
		 */
		public Map getOaSatisfiedSum(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			map.putAll(customerSatisfiedDAO.getOaSatisfiedSum(queryData));//各触点报表表格数据
			//map.put("barChartMap", getAreaChartXMLSum(queryData));//柱状图
			//map.put("lineChartMap", getEveryTypeSatisfiedLineSum(queryData));//折线图
			map.put("sqlString", "");
	        return map; 
		}
		
		//消息推送
	public  boolean sendNoSatisDetailData(Map<String, Object> queryData) {
		String GUID="";
		String title="";
		String deptId="";
		String deptName="";
		String areaId="";
		String city_id="";
		String[] busiCodes;
		String[] busiNames;
		String sec_type;
		
		OprResult<Integer,Object> result=null;
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> param = new HashMap<String, Object>();
		boolean isSucc=false;
		try{
			
			String startTime=MapUtils.getString(queryData, "startTime",null).replaceAll("-", "");
			String endTime=MapUtils.getString(queryData, "endTime",null).replaceAll("-", "");
			
			String initiator="sys";
			String initiatorName="sys";
//			if(SessionManager==null||SessionManager.get)
//			 initiator=SessionManager.getCurrentUserID()+"";
//			 initiatorName=SessionManager.getCurrentUser().get("userName")==null?"sys":SessionManager.getCurrentUser().get("userName").toString();
			
			//获取当前日期			
			Calendar c = Calendar.getInstance();
	        c.setTime(new Date());
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
			String createTime=sdf.format(c.getTime());
			List<Map<String,Object>> chargeManagers=customerSatisfiedDAO.getChargeManagers();
			
			String busiCode="";
			String busiName="";
			
			//更新每条工单的发送时间
			customerSatisfiedDAO.updatePushDate(startTime,createTime);
			
			//推送所有名单
			for(Map<String,Object> map: chargeManagers){
				String executor="";
				String executorName="";
				deptId=map.get("DEPT_ID")==null?"":map.get("DEPT_ID").toString();
				deptName=map.get("DEPT_NAME")==null?"":map.get("DEPT_NAME").toString();
				areaId=map.get("AREA_ID")==null?"":map.get("AREA_ID").toString();
				city_id=map.get("CITY_ID")==null?"":map.get("CITY_ID").toString();
				busiCode=map.get("BUSI_CODE")==null?"":map.get("BUSI_CODE").toString();
				busiName=map.get("BUSI_NAME")==null?"":map.get("BUSI_NAME").toString();
				sec_type=map.get("IS_CORRECT")==null?"":map.get("IS_CORRECT").toString();
				
				executor=map.get("STAFF_CODE")==null?"":map.get("STAFF_CODE").toString();
				executorName=map.get("STAFF_NAME")==null?"":map.get("STAFF_NAME").toString();
				
				busiCodes=busiCode.split("[,]");
				busiNames=busiName.split("[,]");
				
				//配置不正确的数据直接跳过
				if(busiCodes.length<1||busiCodes.length!=busiNames.length)
					continue;
				
				
				//删除已发生的待办事项
				param.put("processLimited", startTime);
				param.put("acceptActorId", executor);
				param.put("startTime", startTime);
				param.put("executor", executor);
				affirmServiceProblem(param);
				
				if("".equals(deptId)){
					deptId=areaId;
				}
				List<Map<String,Object>> messList=null;
				
				for(int i=0;i<busiCodes.length;i++){
				messList=null;
				messList=customerSatisfiedDAO.getMessList(queryData,deptId,busiCodes[i],sec_type);
				
				WorkItemsBean bean=new WorkItemsBean();
				GUID=UUID.randomUUID().toString();

				if(null==messList||messList.size()==0)
					continue;
				
				title="您有一"+startTime+"日"+deptName+busiNames[i]+"工单待处理";
				bean.setRequestType("1");
				bean.setTitle(title);
				bean.setCreateTime(createTime);
				bean.setInitiator(initiator);
				bean.setInitiatorName(initiatorName);
				bean.setAppProcInstID(GUID);
				WorkItemBean workItem=new WorkItemBean();
				workItem.setAppWorkQueueID(GUID);
				workItem.setCurrActivity("待处理");
				workItem.setExecutor(executor);
				workItem.setExecutorName(executorName);
				workItem.setPendingItemURL(tydic.meta.common.Constant.OA_URL_TO_LOCALPAGE+"?orderId="+startTime+"_"+deptId+"_"+busiCodes[i]+"_"+GUID);
				bean.addWorkItems(workItem);

				//send report message
				IUnionWorkItemService uwis=new UnionWorkItemService();
			    uwis.createWorkItems(bean);
			    BaseDAO.commit();
			 
			  //标记已发送的待办
			    pushFlag(messList);
			    
			    data.put("theme", title);
			    data.put("GUID", GUID);
			    data.put("dealActorName", initiatorName);
			    data.put("processLimited", startTime);
			    data.put("attachmentAddress", tydic.meta.common.Constant.OA_URL_TO_LOCALPAGE+"?orderId="+startTime+"_"+deptId+"_"+busiCodes[i]+"_"+GUID);
			    
			    data.put("acceptActorId", executor);
			    data.put("acceptActorName", executorName);
			    data.put("acceptDeptId", deptId);
			    data.put("acceptDeptName", deptName);
			    
			    result = new OprResult<Integer,Object>(null, Integer.parseInt(serviceProblemDAO.insertServiceProblem(data)+""), OprResult.OprResultType.insert);
			    
			    bean=null;
			    workItem=null;
			    workItem=null;
				}
				deptId=null;
				deptName="";
				 busiCodes=null;
				 busiNames=null;
				 title=null;
			}
	           isSucc=true;
		}catch(Exception e){
			 e.printStackTrace();
	         BaseDAO.rollback();
	         isSucc=false;
		}
			return isSucc;
		}
	
	//预警 消息推送
	public  boolean sendEWAMDetailData(Map<String, Object> queryData) {
		String GUID="";
		String title="";
		String deptId="";
		String deptName="";
		String areaId="";
		String areaName="";
		String city_id="";
		String cityName="";
//		String[] busiCodes;
//		String[] busiNames;
//		String sec_type;
		String vDate="";
		String vTypeId="";
		String vTypeName="";
		String targetId="";
		String targetName="";
		String judgeRusultId="";
		String judgeRusultName="";
		
		OprResult<Integer,Object> result=null;
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> param = new HashMap<String, Object>();
		boolean isSucc=false;
		int weekFlag = -1;
		try{
			String startTime=MapUtils.getString(queryData, "startTime",null).replaceAll("-", "");
			String endTime=MapUtils.getString(queryData, "endTime",null).replaceAll("-", "");
			
			String initiator="sys";
			String initiatorName="sys";
//			if(SessionManager==null||SessionManager.get)
//			 initiator=SessionManager.getCurrentUserID()+"";
//			 initiatorName=SessionManager.getCurrentUser().get("userName")==null?"sys":SessionManager.getCurrentUser().get("userName").toString();
			
			//获取当前日期			
			Calendar c = Calendar.getInstance();
	        c.setTime(new Date());
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
			String createTime=sdf.format(c.getTime());
			List<Map<String,Object>> chargeManagers=customerSatisfiedDAO.getChargeManagersEWAM();
			
			//String busiCode="";
			//String busiName="";
			
			//更新每条工单的发送时间
			customerSatisfiedDAO.updateEwamPushDate(startTime,createTime);
			
			//推送所有名单
			for(Map<String,Object> map: chargeManagers){
				String executor="";
				String executorName="";
				deptId=map.get("DEPT_ID")==null?"":map.get("DEPT_ID").toString();
				deptName=map.get("DEPT_NAME")==null?"":map.get("DEPT_NAME").toString();
				areaId=map.get("AREA_ID")==null?"":map.get("AREA_ID").toString();
				areaName=map.get("AREA_NAME")==null?"":map.get("AREA_NAME").toString();
				city_id=map.get("CITY_ID")==null?"":map.get("CITY_ID").toString();
				cityName=map.get("CITY_NAME")==null?"":map.get("CITY_NAME").toString();
				//busiCode=map.get("BUSI_CODE")==null?"":map.get("BUSI_CODE").toString();
				//busiName=map.get("BUSI_NAME")==null?"":map.get("BUSI_NAME").toString();
				//sec_type=map.get("IS_CORRECT")==null?"":map.get("IS_CORRECT").toString();
				
				executor=map.get("STAFF_CODE")==null?"":map.get("STAFF_CODE").toString();
				executorName=map.get("STAFF_NAME")==null?"":map.get("STAFF_NAME").toString();
				
				//busiCodes=busiCode.split("[,]");
				//busiNames=busiName.split("[,]");
				
				//配置不正确的数据直接跳过
				//if(busiCodes.length<1||busiCodes.length!=busiNames.length)
					//continue;
				
				
				//删除已发生的待办事项
				param.put("processLimited", startTime);
				param.put("acceptActorId", executor);
				param.put("startTime", startTime);
				param.put("executor", executor);
				param.put("ewamFlag", "1");
				affirmServiceProblem(param);
				
				if("".equals(deptId)){
					if("".equals(areaId)){
						deptId = city_id;
						deptName = cityName;
					}
					else{
						deptId=areaId;
						deptName = areaName;
					}					
				}
				List<Map<String,Object>> messList=null;
				int ewamMessCount;
				ewamMessCount = Integer.parseInt(customerSatisfiedDAO.getEwamMessCount(queryData, deptId));
				
				for(int i=0;i<ewamMessCount;i++){

				messList=customerSatisfiedDAO.getEwamMessList(queryData,deptId);

				
				WorkItemsBean bean=new WorkItemsBean();
				GUID=UUID.randomUUID().toString();

				if(null==messList||messList.size()==0)
					continue;
				else {
					for(Map<String,Object> messMap: messList){
						vDate = messMap.get("V_DATE")==null?"":messMap.get("V_DATE").toString();
						vTypeId = messMap.get("V_TYPE_ID")==null?"":messMap.get("V_TYPE_ID").toString();
						vTypeName = messMap.get("V_TYPE_NAME")==null?"":messMap.get("V_TYPE_NAME").toString();
						targetId = messMap.get("TARGET_ID")==null?"":messMap.get("TARGET_ID").toString();
						targetName = messMap.get("TARGET_NAME")==null?"":messMap.get("TARGET_NAME").toString();
						judgeRusultId = messMap.get("JUDGE_RESULT_ID")==null?"":messMap.get("JUDGE_RESULT_ID").toString();
						judgeRusultName = messMap.get("JUDGE_RESULT")==null?"":messMap.get("JUDGE_RESULT").toString();
					}										
				}
				
				title=startTime+"日"+deptName+vTypeName+"满意度测评"+targetName+"指标波动异常："+judgeRusultName;
				bean.setRequestType("1");
				bean.setTitle(title);
				bean.setCreateTime(createTime);
				bean.setInitiator(initiator);
				bean.setInitiatorName(initiatorName);
				bean.setAppProcInstID(GUID);
				WorkItemBean workItem=new WorkItemBean();
				workItem.setAppWorkQueueID(GUID);
				workItem.setCurrActivity("待处理");
				workItem.setExecutor(executor);
				workItem.setExecutorName(executorName);
				workItem.setPendingItemURL(tydic.meta.common.Constant.OA_URL_TO_LOCALPAGE_EWAM+"?orderId="+vDate+"_"+deptId+"_"+vTypeId+"_"+targetId+"_"+judgeRusultId+"_"+GUID);
				bean.addWorkItems(workItem);

				//send report message
				IUnionWorkItemService uwis=new UnionWorkItemService();
			    uwis.createWorkItems(bean);
			    BaseDAO.commit();
			 
			  //标记已发送的待办
			    String uniqueValue = vDate+"_"+deptId+"_"+vTypeId+"_"+targetId+"_"+judgeRusultId;
			    customerSatisfiedDAO.ewamPushFlag(uniqueValue);
			    
			    data.put("theme", title);
			    data.put("GUID", GUID);
			    data.put("dealActorName", initiatorName);
			    data.put("processLimited", startTime);
			    data.put("attachmentAddress", tydic.meta.common.Constant.OA_URL_TO_LOCALPAGE_EWAM+"?orderId="+vDate+"_"+deptId+"_"+vTypeId+"_"+targetId+"_"+judgeRusultId+"_"+GUID);
			    
			    data.put("acceptActorId", executor);
			    data.put("acceptActorName", executorName);
			    data.put("acceptDeptId", deptId);
			    data.put("acceptDeptName", deptName);
			    data.put("ewamFlag", "1");
			    
			    result = new OprResult<Integer,Object>(null, Integer.parseInt(serviceProblemDAO.insertServiceProblemEwam(data)+""), OprResult.OprResultType.insert);
			    
			    bean=null;
			    workItem=null;
			    workItem=null;
				}
				
				//如果当天为星期四，则执行周异常数据推送
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				Date myDate = formatter.parse(startTime);
				Calendar cld = Calendar.getInstance();
				cld.setTime(myDate);
				weekFlag = cld.get(Calendar.DAY_OF_WEEK);
				
				if(weekFlag == 5){
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(myDate);
					gc.add(5, -6);
					myDate = gc.getTime();
					String weekTime = "";
					weekTime = formatter.format(myDate)+"~"+startTime;
					startTime = weekTime;
					
					//更新每条工单的发送时间
					customerSatisfiedDAO.updateEwamPushDate(startTime,createTime);
					//调整时间为周时间
					param.put("startTime", startTime);
					param.put("processLimited", startTime);
					affirmServiceProblem(param);
					
//					if("".equals(deptId)){
//						deptId=areaId;
//					}
					//List<Map<String,Object>> messList=null;
					//调整时间为周时间
					queryData.put("startTime", startTime);
					queryData.put("endTime", startTime);
					ewamMessCount = Integer.parseInt(customerSatisfiedDAO.getEwamMessCount(queryData, deptId));
					
					for(int i=0;i<ewamMessCount;i++){

					messList=customerSatisfiedDAO.getEwamMessList(queryData,deptId);

					
					WorkItemsBean bean=new WorkItemsBean();
					GUID=UUID.randomUUID().toString();

					if(null==messList||messList.size()==0)
						continue;
					else {
						for(Map<String,Object> messMap: messList){
							vDate = messMap.get("V_DATE")==null?"":messMap.get("V_DATE").toString();
							vTypeId = messMap.get("V_TYPE_ID")==null?"":messMap.get("V_TYPE_ID").toString();
							vTypeName = messMap.get("V_TYPE_NAME")==null?"":messMap.get("V_TYPE_NAME").toString();
							targetId = messMap.get("TARGET_ID")==null?"":messMap.get("TARGET_ID").toString();
							targetName = messMap.get("TARGET_NAME")==null?"":messMap.get("TARGET_NAME").toString();
							judgeRusultId = messMap.get("JUDGE_RESULT_ID")==null?"":messMap.get("JUDGE_RESULT_ID").toString();
							judgeRusultName = messMap.get("JUDGE_RESULT")==null?"":messMap.get("JUDGE_RESULT").toString();
						}										
					}
					
					title=startTime+"周"+deptName+vTypeName+"满意度测评"+targetName+"指标波动异常："+judgeRusultName;
					bean.setRequestType("1");
					bean.setTitle(title);
					bean.setCreateTime(createTime);
					bean.setInitiator(initiator);
					bean.setInitiatorName(initiatorName);
					bean.setAppProcInstID(GUID);
					WorkItemBean workItem=new WorkItemBean();
					workItem.setAppWorkQueueID(GUID);
					workItem.setCurrActivity("待处理");
					workItem.setExecutor(executor);
					workItem.setExecutorName(executorName);
					workItem.setPendingItemURL(tydic.meta.common.Constant.OA_URL_TO_LOCALPAGE_EWAM+"?orderId="+vDate+"_"+deptId+"_"+vTypeId+"_"+targetId+"_"+judgeRusultId+"_"+GUID);
					bean.addWorkItems(workItem);

					//send report message
					IUnionWorkItemService uwis=new UnionWorkItemService();
				    uwis.createWorkItems(bean);
				    BaseDAO.commit();
				 
				  //标记已发送的待办
				    String uniqueValue = vDate+"_"+deptId+"_"+vTypeId+"_"+targetId+"_"+judgeRusultId;
				    customerSatisfiedDAO.ewamPushFlag(uniqueValue);
				    
				    data.put("theme", title);
				    data.put("GUID", GUID);
				    data.put("dealActorName", initiatorName);
				    data.put("processLimited", startTime);
				    data.put("attachmentAddress", tydic.meta.common.Constant.OA_URL_TO_LOCALPAGE_EWAM+"?orderId="+vDate+"_"+deptId+"_"+vTypeId+"_"+targetId+"_"+judgeRusultId+"_"+GUID);
				    
				    data.put("acceptActorId", executor);
				    data.put("acceptActorName", executorName);
				    data.put("acceptDeptId", deptId);
				    data.put("acceptDeptName", deptName);
				    
				    result = new OprResult<Integer,Object>(null, Integer.parseInt(serviceProblemDAO.insertServiceProblemEwam(data)+""), OprResult.OprResultType.insert);
				    
				    bean=null;
				    workItem=null;
				    workItem=null;
					}
				}
				
				
				deptId=null;
				deptName="";
				vDate="";
				vTypeId="";
				vTypeName="";
				targetId="";
				targetName="";
				judgeRusultId="";
				judgeRusultName="";
				title=null;
			}
	           isSucc=true;
		}catch(Exception e){
			 e.printStackTrace();
	         BaseDAO.rollback();
	         isSucc=false;
		}
			return isSucc;
		}
	
	 public boolean  updateVisitResultIVR(Map<String, Object> param)
	    {	
		 	affirmServiceProblem(param);
	    	return customerSatisfiedDAO.updateVisitResultIVR(param);
	    }
	 
	 public boolean  updateEwamFeedback(Map<String, Object> param)
	    {	
		 	affirmServiceProblem(param);
	    	return customerSatisfiedDAO.updateEwamFeedback(param);
	    }
	
	 /**
	  * 标记已发送待办的清单项
	  * @param deptId
	  * @param busiCodes
	  * @param sec_type
	  */
	 public void pushFlag(List<Map<String,Object>> messList){
		 for(Map<String,Object> data:messList){
			 customerSatisfiedDAO.pushFlag(data.get("UNIQUE_VALUE")==null?"":data.get("UNIQUE_VALUE").toString());
		 }
	 }

	 
    /**
     * 删除oa待办
     * @param data
     * @return
     */
    public void affirmServiceProblem(Map<String,Object> data){
    	List<Map<String,Object>> orders=serviceProblemDAO.getMainPromblemByAddressUrl(data);
    	for(Map order:orders){
    	
    	if(orders==null)
    	return;
    	String GUID=order.get("GUID").toString();
    	String title=order.get("THEME").toString();
//    	String createDate=orders.get("PROCESS_LIMITED").toString();
//    	String acceptActorId=orders.get("ACCEPT_ACTOR_ID").toString();
    	
//    	String userName=orders.get("CREATE_ACTOR_NAME")==null?"system":orders.get("CREATE_ACTOR_NAME").toString();
    	String executorId=order.get("ACCEPT_ACTOR_ID")==null?"system":order.get("ACCEPT_ACTOR_ID").toString(); 
    	String executorName=order.get("ACCEPT_ACTOR_NAME")==null?"system":order.get("ACCEPT_ACTOR_NAME").toString();  
    	BaseDAO.beginTransaction();
    	//1.结束流程
        WorkItemsBean bean=new WorkItemsBean();
	    bean.setRequestType("1");//
	    bean.setAppProcInstID(GUID);
	    bean.setAppWorkQueueID(GUID);
	    bean.setTitle(title);
	    bean.setExecutor(executorId);
	    bean.setExecutorName(executorName);
	    IUnionWorkItemService uwis=new UnionWorkItemService();
	    uwis.deleteWorkItem(bean);
	    serviceProblemDAO.delMianstate(GUID);//删除待办记录事项
    	BaseDAO.commit();
    	}
    }
    
    
    public int finishEAIC(Map<String,Object> data){
    	Map<String,Object> orders=serviceProblemDAO.getMainPromblemByMainId(data.get("guid").toString());
    	
    	if(orders==null)
    	return 0;
    	String GUID=orders.get("GUID").toString();
    	String title=orders.get("THEME").toString();
    	
    	//String userName=orders.get("CREATE_ACTOR_NAME")==null?"system":orders.get("CREATE_ACTOR_NAME").toString();
    	String executorId=orders.get("ACCEPT_ACTOR_ID")==null?"system":orders.get("ACCEPT_ACTOR_ID").toString(); 
    	String executorName=orders.get("ACCEPT_ACTOR_NAME")==null?"system":orders.get("ACCEPT_ACTOR_NAME").toString();  
    	
    	BaseDAO.beginTransaction();
    	//1.结束流程
        WorkItemsBean bean=new WorkItemsBean();
	    bean.setRequestType("1");//
	    bean.setAppProcInstID(GUID);
	    bean.setAppWorkQueueID(GUID);
	    bean.setTitle(title);
	    bean.setExecutor(executorId);
	    bean.setExecutorName(executorName);
	    IUnionWorkItemService uwis=new UnionWorkItemService();
	    uwis.deleteWorkItem(bean);
	    serviceProblemDAO.updateMianstate(GUID);//删除待办记录事项
    	BaseDAO.commit();
    	return 1;
    }
	
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
		
		  
	//即时测评及网络质量
	public Map getItanTass(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(customerSatisfiedDAO.getItanTass(queryData));//满意度评测总体情况表格数据
        return map; 
	}
	
	//延迟测评
		public Map getsmsDelay(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			map.putAll(customerSatisfiedDAO.getsmsDelay(queryData));//延迟测评总体情况表格数据
	        return map; 
		}
		
		public Map getsmsDelayList(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			Pager page = Pager.getInstance();
			page.setCurrNum(Convert.toInt(queryData.get("currPageNum")));
			page.setSize(Convert.toInt(queryData.get("pageCount")));
			map.putAll(customerSatisfiedDAO.getsmsDelayList(queryData,page));//延迟测评清单表格数据
	        return map; 
		}
		
		//政企满意度
		public Map getzqSatisfyList(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			Pager page = Pager.getInstance();
			page.setCurrNum(Convert.toInt(queryData.get("currPageNum")));
			page.setSize(Convert.toInt(queryData.get("pageCount")));
			map.putAll(customerSatisfiedDAO.getzqSatisfyList(queryData,page));//延迟测评清单表格数据
	        return map; 
		}
		
		public Map getzqMYDDetails(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			map.putAll(customerSatisfiedDAO.getzqMYDDetails(queryData));//延迟测评总体情况表格数据
	        return map; 
		}
		
		/**
		 * 导出数据
		 */
		public Map<String, Object> expTxtData(Map<String, Object> queryData) {
			Map<String, Object> map=new HashMap<String,Object>();
			Pager page = Pager.getInstance();
			page.setCurrNum(Convert.toInt(queryData.get("currPageNum")));
			page.setSize(Convert.toInt(queryData.get("pageCount")));
			map.putAll(customerSatisfiedDAO.getsmsDelayList(queryData,page));//延迟测评清单表格数据
			return map;// 记录
		}
	
	//渠道服务偏好挖掘(服务)
	public Map channelCustService(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(customerSatisfiedDAO.channelCustService(queryData));//渠道服务偏好挖掘
        return map; 
	}
	
	//渠道服务偏好挖掘(渠道)
	public Map channelCustQd(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(customerSatisfiedDAO.channelCustQd(queryData));//渠道服务偏好挖掘
        return map; 
	}
	
	//渠道服务偏好挖掘(时机)
	public Map channelCustSj(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(customerSatisfiedDAO.channelCustSj(queryData));//渠道服务偏好挖掘
        return map; 
	}
	
	public Map<String, Object> expNoSatusDetailData(Map<String, Object> queryData) {
		Pager page=(Pager)queryData.get("page");
		CustomerSatisfiedDAO customerSatisfiedDAO =new CustomerSatisfiedDAO();
		return customerSatisfiedDAO.getNosatisDetailData(queryData, page);// 记录	
	}

}
