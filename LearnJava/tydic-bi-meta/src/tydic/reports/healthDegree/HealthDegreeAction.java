package tydic.reports.healthDegree;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;
import tydic.portalCommon.PublicUse;
/**
 * 
 * @author qx 健康度模型报表
 * 
 */
public class HealthDegreeAction {
	private HealthDegreeDAO healthDegreeDAO; 
	
	public HealthDegreeDAO getHealthDegreeDAO() {
		return healthDegreeDAO;
	}
	public void setHealthDegreeDAO(HealthDegreeDAO healthDegreeDAO) {
		this.healthDegreeDAO = healthDegreeDAO;
	}
	//健康度模型_评分架构（存储过程）
	public Map getScoringArchitecture_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(healthDegreeDAO.getScoringArchitecture_Pg(queryData));
        return map; 
	}
	//健康度模型_总体情况（存储过程）
	public Map getOverallPerformance_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(healthDegreeDAO.getOverallPerformance_Pg(queryData));
		map.put("barChartMap", getOverallPerformanceBarChart(queryData));
		map.put("chartMap", getOverallPerformanceMap(queryData));
        return map; 
	}
	//健康度模型_业务项（存储过程）
	public Map getBusinessItem_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(healthDegreeDAO.getBusinessItem_Pg(queryData));
		map.put("radarMap", getBusinessItemRadarMap(queryData));
        return map; 
	}
	//健康度模型_业务指标（存储过程）
	public Map getBusinessIndex_Pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(healthDegreeDAO.getBusinessIndex_Pg(queryData));
        return map; 
	}
	//健康度模型_总体情况（柱状图）
	private String getOverallPerformanceBarChart(Map<String, Object> queryData) {
		StringBuffer  barChartMap=new StringBuffer();
		//柱状图展示
    	String field =       MapUtils.getString(queryData, "field",null);
		String currentColor= "FBC62A";//当月
        StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();

		double maxVal=0;
	    String inMonth=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前月
			
		temp2.append("<dataset  seriesName='" + inMonth + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");	   		
		List<Map<String,Object>>barList=healthDegreeDAO.getChartData_OverallPerformance(inMonth);//本月		
		int i=0;
		String colorStr="";
		for (Map<String, Object> key : barList) {
			colorStr=getColorStr(i);
			i++;
			String curValue = "0";
			curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
			maxVal=PublicUse.compareMaxValue(maxVal,Double.parseDouble(curValue));
			temp1.append("<category label='" + key.get("REGION_NAME")+ "'/>\n");
			temp2.append("<set      value='" + curValue + "' color='"+colorStr+"' />\n");
		}
       temp1.append("</categories>\n");
       temp2.append("</dataset>\n");
       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
       " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
       " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
       " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
       " numdivlines='4' adjustDiv='0' rotateNames='0' yaxismaxvalue='"+maxVal+"'"+
       " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'" +
       " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
       " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");    
       barChartMap.append(temp1).append(temp2);
       barChartMap.append("</chart>");
      return barChartMap.toString();	
  }
	//健康度模型_总体情况（地图）
	private String getOverallPerformanceMap(Map<String, Object> queryData) {
		StringBuffer  chartMap=new StringBuffer();
    	String field =       MapUtils.getString(queryData, "field",null);   
        StringBuffer  temp1=new StringBuffer();
    	temp1.append("<data>\n");
      	StringBuffer  temp2=new StringBuffer();
	    String inMonth=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前月	
		List<Map<String,Object>>mapList=healthDegreeDAO.getChartData_OverallPerformance(inMonth);	
		for (Map<String, Object> key : mapList) {
			String curValue = "0";
			curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
			String tempRegionId=key.get("REGION_ID").equals("200")?"020":"0"+key.get("REGION_ID");
			temp1.append("<entity id='" + tempRegionId+ "'  value='" + curValue + "' />\n");
		}
       temp2.append("</data>\n");
       chartMap.append("<map animation='0' showBevel='0' useHoverColor='1' canvasBorderColor='ffffff' borderColor='FBC62A'" +
       		" showLegend='1' showShadow='1' legendPosition='BOTTOM' legendBorderAlpha='1' legendBorderColor='FBC62A' " +
       		"legendAllowDrag='1' legendShadow='0' connectorColor='FBC62A' fillAlpha='80' hoverColor='FBC62A' " +
       " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
       " exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");    
       chartMap.append(temp1).append(temp2);
       chartMap.append("</map>");
      return chartMap.toString();	
  }
	//健康度模型_业务项（雷达图）
	private String getBusinessItemRadarMap(Map<String, Object> queryData) {
		String zoneCode =       MapUtils.getString(queryData, "zoneCode",null);
		String dateTime=MapUtils.getString(queryData, "dateTime", null).replaceAll("-", "");//当前月	
		String field =       MapUtils.getString(queryData, "field",null);
		StringBuffer  chartMap=new StringBuffer();
        StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories font='Arial' fontSize='11'>\n");
		List<Map<String,Object>>itemList=healthDegreeDAO.getBusinessItemList();	
		
		List<Map<String,Object>>zoneList=healthDegreeDAO.getZoneList(zoneCode);
		for (Map<String, Object> key : itemList) {
			String curValue = "";
			curValue = String.valueOf(key.get("IND_NAME") == null ? "-": key.get("IND_NAME"));
			temp1.append("<category label='" + curValue + "' />\n");			
		}
	    temp1.append("</categories>\n");
		StringBuffer  temp2=new StringBuffer();
		Map<String, Object> avgObj=healthDegreeDAO.getBusinessItemAvgRadarMap(zoneCode, dateTime);	
		StringBuffer  temp3=new StringBuffer();
		temp3.append("<dataset seriesname='平均分' color='000000' anchorSides='14' anchorBorderColor='0099FF' anchorBgAlpha='0' anchorRadius='2'>\n");
		temp3.append("<set value='"+avgObj.get("即时回访满意率")+"' />\n");
		temp3.append("<set value='"+avgObj.get("投诉率")+"' />\n");
		temp3.append("<set value='"+avgObj.get("客户维系")+"' />\n");
		temp3.append("<set value='"+avgObj.get("宽带专项服务提升")+"' />\n");
		temp3.append("<set value='"+avgObj.get("划小责任提升")+"' />\n");
		temp3.append("</dataset>\n");
		int i=0;
		for (Map<String, Object> key1 : zoneList) {
			String tempColor=getColor(i);
			i++;
			temp2.append("<dataset seriesname='"+key1.get("ZONE_NAME")+"' color='"+tempColor+"' anchorSides='14' anchorBorderColor='0099FF' anchorBgAlpha='0' anchorRadius='2'>\n");
			for (Map<String, Object> key2 : itemList) {
				String indId=String.valueOf(key2.get("IND_ID") == null ? "": key2.get("IND_ID"));
				String zoneId=String.valueOf(key1.get("ZONE_CODE") == null ? "": key1.get("ZONE_CODE"));
				Map<String, Object> currentObj=healthDegreeDAO.getBusinessItemRadarMap(zoneId, indId, dateTime);
				 String currentValue="0";
				if (null != currentObj) {
	    	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
	    		   }else{
	    			   currentValue="0";
	    		   }
				temp2.append("<set value='"+currentValue+"' />\n");
			}
			temp2.append("</dataset>\n");
		}
		StringBuffer  temp4=new StringBuffer();
		temp4.append("<styles><definition> <style name='myCaptionFont' type='font' font='Arial' size='14' color='666666' bold='1' />" +
				" </definition> <application> <apply toObject='Caption' styles='myCaptionFont'/> </application> </styles> \n");
       chartMap.append("<chart  hovercapbg='FFECAA' hovercapborder='F47E00'  bgColor='FFFFFF' radarFillColor='FFFFFF' " +
       		"plotFillAlpha='5' plotBorderThickness='2' anchorAlpha='100' numberSuffix='%' numDivLines='2' legendPosition='RIGHT' exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
       " exportFileName='onePic' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");    
       chartMap.append(temp1).append(temp2).append(temp3).append(temp4);
       chartMap.append("</chart>");
      return chartMap.toString();	
  }
	String getColor(int i){
		String colorStr="FF0000";
	    if(i==0){
	    	colorStr="EA0000";
	    }if(i==1){
	    	colorStr="005AB5";
	    }if(i==2){
	    	colorStr="977C00";
	    }if(i==3){
	    	colorStr="02DF82";
	    }if(i==4){
	    	colorStr="9F4D95";
	    }if(i==5){
	    	colorStr="6C3365";
	    }
	    return colorStr;
	}
	String getColorStr(int i){
		String colorStr="FF0000";
	    if(i<4){
	    	colorStr="EA0000";
	    }if(i>=4&&i<8){
	    	colorStr="005AB5";
	    }if(i>=8&&i<12){
	    	colorStr="977C00";
	    }if(i>=12&&i<16){
	    	colorStr="02DF82";
	    }if(i>=16&&i<21){
	    	colorStr="9F4D95";
	    }
	    return colorStr;
	}
}
