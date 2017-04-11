package tydic.reports.detailMarket;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.MapUtils;

/**
 * 
 * @author qx 客户满意率监测报表
 * 
 */
public class DetailMarketAction {
	private DetailMarketDAO detailMarketDAO;

	public DetailMarketDAO getDetailMarketDAO() {
		return detailMarketDAO;
	}

	public void setDetailMarketDAO(DetailMarketDAO detailMarketDAO) {
		this.detailMarketDAO = detailMarketDAO;
	}
	//细分市场存储过程
	public Map queryDetailMarket_pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(detailMarketDAO.queryDetailMarket_pg(queryData));//各触点报表表格数据
        return map; 
	}
	//细分市场存储过程
	public Map queryDetailMarket1_pg(Map<String, Object> queryData) throws ParseException {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(detailMarketDAO.queryDetailMarket_pg(queryData));//各触点报表表格数据
		map.put("pieChartMap1", getDetailMarket1_PieChart1(queryData));//饼图1
		map.put("pieChartMap2",getDetailMarket1_PieChart2(queryData) );//饼图2
        return map; 
	}
	//细分市场存储过程
	public Map queryDetailMarket2_pg(Map<String, Object> queryData) throws ParseException {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(detailMarketDAO.queryDetailMarket_pg(queryData));//各触点报表表格数据
		map.put("pieChartMap", getDetailMarket2_PieChart(queryData));//饼图
		map.put("lineChartMap",getDetailMarket2_LineChart(queryData) );//折线图
		map.put("barChartMap",getDetailMarket2_BarChart(queryData) );//柱状图
        return map; 
	}
	//细分市场存储过程
	public Map queryDetailMarket3_pg(Map<String, Object> queryData) throws ParseException {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(detailMarketDAO.queryDetailMarket_pg(queryData));//各触点报表表格数据
		map.put("barChartMap1",getDetailMarket3_BarChart1(queryData) );//柱状图
		map.put("barChartMap2",getDetailMarket3_BarChart2(queryData) );//柱状图
        return map; 
	}
	//细分市场存储过程
	public Map queryDetailMarket4_pg(Map<String, Object> queryData) throws ParseException {
		Map<String, Object> map=new HashMap<String,Object>();
		map.putAll(detailMarketDAO.queryDetailMarket_pg(queryData));//各触点报表表格数据
		map.put("barChartMap1",getDetailMarket4_BarChart1(queryData) );//柱状图
		map.put("barChartMap2",getDetailMarket4_BarChart2(queryData) );//柱状图
        return map; 
	}
	public String getDetailMarket1_PieChart1(Map<String,Object>queryData) throws ParseException{
		StringBuffer  pieChartMap=new StringBuffer();
		//饼图展示
      	StringBuffer  temp=new StringBuffer();
	    List<Map<String,Object>>barList=null;
	    String field =MapUtils.getString(queryData, "field",null);
		barList = detailMarketDAO.getDetailMarket1_PieChartList1(queryData);// 不满意原因列表
		for (Map<String, Object> key : barList) {
			String prodTypeName = String.valueOf(key.get("CMPL_PROD_TYPE_NAME") == null ? "-": key.get("CMPL_PROD_TYPE_NAME"));
            String curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
			temp.append("<set label='"+prodTypeName+"' value='"+curValue+"'/> ");
		}
	   pieChartMap.append("<chart caption='产品类别占比图 ' animation='1' showBorder='1' startingAngle='70' " +
       		"bgAngle='360' bgRatio='0,100' bgColor='99ccff,ffffff' manageLabelOverflow='1' " +
       		"pieRadius='152' enableSmartLabels='1' slicingDistance='30' " +
       		"decimals='2' palette='4' formatNumberScale='2'" +
       		" exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
            " exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	   pieChartMap.append(temp);
	   pieChartMap.append("</chart>");
      return pieChartMap.toString();	
	}
	public String getDetailMarket1_PieChart2(Map<String,Object>queryData) throws ParseException{
		StringBuffer  pieChartMap=new StringBuffer();
		//饼图展示
      	StringBuffer  temp=new StringBuffer();
	    List<Map<String,Object>>barList=null;
	    String field =MapUtils.getString(queryData, "field",null);
		barList = detailMarketDAO.getDetailMarket1_PieChartList2(queryData);// 不满意原因列表
		for (Map<String, Object> key : barList) {
			String cmplTypeName = String.valueOf(key.get("CMPL_BUSI_TYPE_NAME") == null ? "-": key.get("CMPL_BUSI_TYPE_NAME"));
            String curValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
			temp.append("<set label='"+cmplTypeName+"' value='"+curValue+"'/> ");
		}
	   pieChartMap.append("<chart caption='服务类别占比图' animation='1' showBorder='1' startingAngle='70' " +
       		"bgAngle='360' bgRatio='0,100' bgColor='99ccff,ffffff' manageLabelOverflow='1' " +
       		"pieRadius='152' enableSmartLabels='1' slicingDistance='30' " +
       		"decimals='2' palette='4' formatNumberScale='0'" +
       		" exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
            " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");   
	   pieChartMap.append(temp);
	   pieChartMap.append("</chart>");
      return pieChartMap.toString();	
	}
	//饼图变换
	public Map getDetailMarket1_PieChart(Map<String, Object> queryData) throws ParseException {
		Map<String, Object> map=new HashMap<String,Object>();
		map.put("pieChartMap1", getDetailMarket1_PieChart1(queryData));
		map.put("pieChartMap2", getDetailMarket1_PieChart2(queryData));
        return map; 
	}
	public String getDetailMarket2_PieChart(Map<String,Object>queryData) throws ParseException{
		StringBuffer  pieChartMap=new StringBuffer();
		String rtpName="";
		String rptType =MapUtils.getString(queryData, "rptType",null);
		if(rptType.equals("2")||rptType=="2"){
			rtpName="本地抱怨量 ";
		}else{
			rtpName="本地投诉量 ";
		}
		//饼图展示
      	StringBuffer  temp=new StringBuffer();
	    String field =MapUtils.getString(queryData, "field",null);
		for (int i =1; i <= 6; i++) {
			String detailsMarketName =detailMarketDAO.getDetailsMarketName(i+"");
			Map<String, Object> currentObj=null;
			currentObj = detailMarketDAO.getDetailMarket2_PieData(i+"",queryData);
			String currentValue="";
			if (null != currentObj) {
  	    	  currentValue = String.valueOf(currentObj.get(field) == null ? "0": currentObj.get(field));
  		   }else{
  			   currentValue="0";
  		   }
			temp.append("<set label='"+detailsMarketName+"' value='"+currentValue+"'/> ");
		}
	   pieChartMap.append("<chart caption='细分市场占比图 ' animation='1' showBorder='1' startingAngle='70' " +
       		"bgAngle='360' bgRatio='0,100' bgColor='99ccff,ffffff' manageLabelOverflow='1' " +
       		"pieRadius='152' enableSmartLabels='1' slicingDistance='30' " +
       		"decimals='2' palette='4' formatNumberScale='2'" +
       		" exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
            " exportFileName='onePic' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n"); 
	   pieChartMap.append(temp);
	   pieChartMap.append("</chart>");
      return pieChartMap.toString();	
	}
	public String getDetailMarket2_LineChart(Map<String,Object>queryData) throws ParseException{
		String field =       MapUtils.getString(queryData, "field1",null);
		StringBuffer  lineChartMap=new StringBuffer();//折线图
		int numVdivlines =-1;
		StringBuffer  temp1=new StringBuffer();
		temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
      	StringBuffer  temp4=new StringBuffer();
      	StringBuffer  temp5=new StringBuffer();
      	StringBuffer  temp6=new StringBuffer();
      	StringBuffer  temp7=new StringBuffer();
      	
      	String color1= "458B00";
    	String color2= "CDAD00";
    	String color3= "8DB6CD";
    	String color4= "CD2626";
    	String color5= "00868B";
    	String color6= "EE7942 ";
    	
    	String rtpName="";
		String rptType =MapUtils.getString(queryData, "rptType",null);
		if(rptType.equals("2")||rptType=="2"){
			rtpName="本地抱怨率(‰)";
		}else{
			rtpName="本地投诉率(‰)";
		}
		String inMonth=MapUtils.getString(queryData, "startDate", null).replaceAll("-", "");//当前月
		String month=inMonth.substring(4,6);
		String year=inMonth.substring(0,4);
		temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color1+"' seriesName='"+rtpName+" 校园市场' color='"+color1+"' anchorBorderColor='FFFFFF'>\n");
        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color2+"' seriesName='"+rtpName+" 农村市场'  color='"+color2+"' anchorBorderColor='FFFFFF'>\n");
        temp4.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color3+"' seriesName='"+rtpName+" 大客户'    color='"+color3+"' anchorBorderColor='FFFFFF'>\n");
        temp5.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color4+"' seriesName='"+rtpName+" 商业客户'  color='"+color4+"' anchorBorderColor='FFFFFF'>\n");
        temp6.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color5+"' seriesName='"+rtpName+" 城市家庭'  color='"+color5+"' anchorBorderColor='FFFFFF'>\n");
        temp7.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+color6+"' seriesName='"+rtpName+" 流动市场'  color='"+color6+"' anchorBorderColor='FFFFFF'>\n");
		for (int i = 1; i <= Integer.parseInt(month); i++) {
  	      int showDay   =i;
  	      String show="";
  	      if(showDay<10){
  	    	  show="0"+showDay;
  	      }else{
  	    	  show=showDay+"";
  	      }
  	      String surMon=year+show;//当月
  		  String currentValue1="0";
  		  String currentValue2="0";
  		  String currentValue3="0";
  		  String currentValue4="0";
  		  String currentValue5="0";
  		  String currentValue6="0";

  		  Map<String, Object> currentObj=null;
  		  currentObj = detailMarketDAO.getDetailMarket2_LineData(surMon,queryData);
  	      if (null != currentObj) {
  	    	   currentValue1 = String.valueOf(currentObj.get("SHOOL_MARKET"+field) == null ? "0": currentObj.get("SHOOL_MARKET"+field));
  	    	   currentValue2 = String.valueOf(currentObj.get("COUNTRY_MARKET"+field) == null ? "0": currentObj.get("COUNTRY_MARKET"+field));
  	    	   currentValue3 = String.valueOf(currentObj.get("BIG_CUSTOMER"+field) == null ? "0": currentObj.get("BIG_CUSTOMER"+field));
  	    	   currentValue4 = String.valueOf(currentObj.get("BUSINESS_CUSTOMER"+field) == null ? "0": currentObj.get("BUSINESS_CUSTOMER"+field));
  	    	   currentValue5 = String.valueOf(currentObj.get("CITY_FAMILY"+field) == null ? "0": currentObj.get("CITY_FAMILY"+field));
  	    	   currentValue6 = String.valueOf(currentObj.get("FLOW_MARKET"+field) == null ? "0": currentObj.get("FLOW_MARKET"+field));
  		   }else{
  			   currentValue1="0";
  			   currentValue2="0";
  			   currentValue3="0";
  			   currentValue4="0";
  			   currentValue5="0";
  			   currentValue6="0";
  		   }  
  	          temp1.append("<category name='"+showDay+"月' />\n");
	          temp2.append("<set value='"+currentValue1+"' hoverText='校园市场："+currentValue1+"' />\n");
	    	  temp3.append("<set value='"+currentValue2+"' hoverText='农村市场： "+currentValue2+"' />\n");  
	    	  temp4.append("<set value='"+currentValue3+"' hoverText='大客户： "+currentValue3+"' />\n");  
	    	  temp5.append("<set value='"+currentValue4+"' hoverText='商业客户： "+currentValue4+"' />\n");  
	    	  temp6.append("<set value='"+currentValue5+"' hoverText='城市家庭： "+currentValue5+"' />\n");  
	    	  temp7.append("<set value='"+currentValue6+"' hoverText='流动市场： "+currentValue6+"' />\n"); 
	    	  numVdivlines=numVdivlines+1;
  	 }
		temp1.append("</categories>\n");
        temp2.append("</dataset>\n");
        temp3.append("</dataset>\n");
        temp4.append("</dataset>\n");
        temp5.append("</dataset>\n");
        temp6.append("</dataset>\n");
        temp7.append("</dataset>\n");
        lineChartMap.append("<chart labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
    	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
    	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
    	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
    	        " numdivlines='4' adjustDiv='0' numVdivlines='"+numVdivlines+"' rotateNames='0'"+
    	        " chartRightMargin='20' chartLeftMargin='10'"+
    	        " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
                " exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
        lineChartMap.append(temp1).append(temp2).append(temp3).append(temp4).append(temp5).append(temp6).append(temp7);
        lineChartMap.append("</chart>");
    return lineChartMap.toString();
	}
	public String getDetailMarket2_BarChart(Map<String,Object>queryData) throws ParseException{
		String field =       MapUtils.getString(queryData, "field1",null);
		StringBuffer  barChartMap=new StringBuffer();//折线图
		StringBuffer  temp1=new StringBuffer();
		temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
      	StringBuffer  temp4=new StringBuffer();
      	StringBuffer  temp5=new StringBuffer();
      	StringBuffer  temp6=new StringBuffer();
      	StringBuffer  temp7=new StringBuffer();
      	
      	String color1= "458B00";
    	String color2= "CDAD00";
    	String color3= "8DB6CD";
    	String color4= "CD2626";
    	String color5= "00868B";
    	String color6= "EE7942 ";
    	String rtpName="";
		String rptType =MapUtils.getString(queryData, "rptType",null);
		if(rptType.equals("2")||rptType=="2"){
			rtpName="本地抱怨率(‰)";
		}else{
			rtpName="本地投诉率(‰)";
		}	
		temp2.append("<dataset  seriesName='"+rtpName+" 校园市场' color='"+color1+"' anchorBorderColor='"+color1+"' anchorBgColor='"+color1+"' >\n");
		temp3.append("<dataset  seriesName='"+rtpName+" 农村市场' color='"+color2+"' anchorBorderColor='"+color2+"' anchorBgColor='"+color2+"' >\n");
		temp4.append("<dataset  seriesName='"+rtpName+" 大客户'   color='"+color3+"' anchorBorderColor='"+color3+"' anchorBgColor='"+color3+"' >\n");
		temp5.append("<dataset  seriesName='"+rtpName+" 商业客户' color='"+color4+"' anchorBorderColor='"+color4+"' anchorBgColor='"+color4+"' >\n");
		temp6.append("<dataset  seriesName='"+rtpName+" 城市家庭' color='"+color5+"' anchorBorderColor='"+color5+"' anchorBgColor='"+color5+"' >\n");
		temp7.append("<dataset  seriesName='"+rtpName+" 流动市场' color='"+color6+"' anchorBorderColor='"+color6+"' anchorBgColor='"+color6+"' >\n");
		List<Map<String,Object>>barList=detailMarketDAO.getDetailMarket2_barChart(queryData);//本月
		for (Map<String, Object> key : barList) {
			String curValue1 = "0";
			String curValue2 = "0";
			String curValue3 = "0";
			String curValue4 = "0";
			String curValue5 = "0";
			String curValue6 = "0";
			curValue1 = String.valueOf(key.get("SHOOL_MARKET"+field) == null ? "0": key.get("SHOOL_MARKET"+field));
			curValue2 = String.valueOf(key.get("COUNTRY_MARKET"+field) == null ? "0": key.get("COUNTRY_MARKET"+field));
			curValue3 = String.valueOf(key.get("BIG_CUSTOMER"+field) == null ? "0": key.get("BIG_CUSTOMER"+field));
			curValue4 = String.valueOf(key.get("BUSINESS_CUSTOMER"+field) == null ? "0": key.get("BUSINESS_CUSTOMER"+field));
			curValue5 = String.valueOf(key.get("CITY_FAMILY"+field) == null ? "0": key.get("CITY_FAMILY"+field));
			curValue6 = String.valueOf(key.get("FLOW_MARKET"+field) == null ? "0": key.get("FLOW_MARKET"+field));
			temp1.append("<category label='" + key.get("REGION_NAME")+ "'/>\n");
			temp2.append("<set      value='" + curValue1 + "' />\n");
			temp3.append("<set      value='" + curValue2 + "' />\n");
			temp4.append("<set      value='" + curValue3 + "' />\n");
			temp5.append("<set      value='" + curValue4 + "' />\n");
			temp6.append("<set      value='" + curValue5 + "' />\n");
			temp7.append("<set      value='" + curValue6 + "' />\n");
		}
		   temp1.append("</categories>\n");
	       temp2.append("</dataset>\n");
	       temp3.append("</dataset>\n");
	       temp4.append("</dataset>\n");
	       temp5.append("</dataset>\n");
	       temp6.append("</dataset>\n");
	       temp7.append("</dataset>\n");
	       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	               " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	               " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	               " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	               " numdivlines='4' adjustDiv='0' rotateNames='0' "+
	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'" +
	               " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	               " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	       barChartMap.append(temp1).append(temp2).append(temp3).append(temp4).append(temp5).append(temp6).append(temp7);
	       barChartMap.append("</chart>");
    return barChartMap.toString();
	}
	public String getDetailMarket3_BarChart1(Map<String,Object>queryData) throws ParseException{
		String field =       MapUtils.getString(queryData, "field1",null);
		StringBuffer  barChartMap=new StringBuffer();//柱状图
		StringBuffer  temp1=new StringBuffer();
		temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
      	StringBuffer  temp4=new StringBuffer();
      	StringBuffer  temp5=new StringBuffer();
      	StringBuffer  temp6=new StringBuffer();
      	StringBuffer  temp7=new StringBuffer();
      	
      	String color1= "458B00";
    	String color2= "CDAD00";
    	String color3= "8DB6CD";
    	String color4= "CD2626";
    	String color5= "00868B";
    	String color6= "EE7942 ";
    	String rtpName="";
		String rptType =MapUtils.getString(queryData, "rptType",null);
		if(rptType.equals("3")||rptType=="3"){
			rtpName="本地抱怨率(‰)";
		}else{
			rtpName="本地投诉率(‰)";
		}	
		temp2.append("<dataset  seriesName='"+rtpName+" 校园市场' color='"+color1+"' anchorBorderColor='"+color1+"' anchorBgColor='"+color1+"' >\n");
		temp3.append("<dataset  seriesName='"+rtpName+" 农村市场' color='"+color2+"' anchorBorderColor='"+color2+"' anchorBgColor='"+color2+"' >\n");
		temp4.append("<dataset  seriesName='"+rtpName+" 大客户'   color='"+color3+"' anchorBorderColor='"+color3+"' anchorBgColor='"+color3+"' >\n");
		temp5.append("<dataset  seriesName='"+rtpName+" 商业客户' color='"+color4+"' anchorBorderColor='"+color4+"' anchorBgColor='"+color4+"' >\n");
		temp6.append("<dataset  seriesName='"+rtpName+" 城市家庭' color='"+color5+"' anchorBorderColor='"+color5+"' anchorBgColor='"+color5+"' >\n");
		temp7.append("<dataset  seriesName='"+rtpName+" 流动市场' color='"+color6+"' anchorBorderColor='"+color6+"' anchorBgColor='"+color6+"' >\n");
		List<Map<String,Object>>barList=detailMarketDAO.getDetailMarket3_barChart1(queryData);//本月
		for (Map<String, Object> key : barList) {
			String curValue1 = "0";
			String curValue2 = "0";
			String curValue3 = "0";
			String curValue4 = "0";
			String curValue5 = "0";
			String curValue6 = "0";
			curValue1 = String.valueOf(key.get("SHOOL_MARKET"+field) == null ? "0": key.get("SHOOL_MARKET"+field));
			curValue2 = String.valueOf(key.get("COUNTRY_MARKET"+field) == null ? "0": key.get("COUNTRY_MARKET"+field));
			curValue3 = String.valueOf(key.get("BIG_CUSTOMER"+field) == null ? "0": key.get("BIG_CUSTOMER"+field));
			curValue4 = String.valueOf(key.get("BUSINESS_CUSTOMER"+field) == null ? "0": key.get("BUSINESS_CUSTOMER"+field));
			curValue5 = String.valueOf(key.get("CITY_FAMILY"+field) == null ? "0": key.get("CITY_FAMILY"+field));
			curValue6 = String.valueOf(key.get("FLOW_MARKET"+field) == null ? "0": key.get("FLOW_MARKET"+field));
			temp1.append("<category label='" + key.get("CMPL_PROD_TYPE_NAME")+ "'/>\n");
			temp2.append("<set      value='" + curValue1 + "' />\n");
			temp3.append("<set      value='" + curValue2 + "' />\n");
			temp4.append("<set      value='" + curValue3 + "' />\n");
			temp5.append("<set      value='" + curValue4 + "' />\n");
			temp6.append("<set      value='" + curValue5 + "' />\n");
			temp7.append("<set      value='" + curValue6 + "' />\n");
		}
		   temp1.append("</categories>\n");
	       temp2.append("</dataset>\n");
	       temp3.append("</dataset>\n");
	       temp4.append("</dataset>\n");
	       temp5.append("</dataset>\n");
	       temp6.append("</dataset>\n");
	       temp7.append("</dataset>\n");
	       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	               " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	               " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	               " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	               " numdivlines='4' adjustDiv='0' rotateNames='0' "+
	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'" +
	               " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	               " exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	       barChartMap.append(temp1).append(temp2).append(temp3).append(temp4).append(temp5).append(temp6).append(temp7);
	       barChartMap.append("</chart>");
    return barChartMap.toString();
	}
	public String getDetailMarket3_BarChart2(Map<String,Object>queryData) throws ParseException{
		String field =       MapUtils.getString(queryData, "field1",null);
		StringBuffer  barChartMap=new StringBuffer();//柱状图
		StringBuffer  temp1=new StringBuffer();
		temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
      	StringBuffer  temp4=new StringBuffer();
      	StringBuffer  temp5=new StringBuffer();
      	StringBuffer  temp6=new StringBuffer();
      	StringBuffer  temp7=new StringBuffer();
      	
      	String color1= "458B00";
    	String color2= "CDAD00";
    	String color3= "8DB6CD";
    	String color4= "CD2626";
    	String color5= "00868B";
    	String color6= "EE7942 ";
    	String rtpName="";
		String rptType =MapUtils.getString(queryData, "rptType",null);
		if(rptType.equals("3")||rptType=="3"){
			rtpName="本地抱怨率(‰)";
		}else{
			rtpName="本地投诉率(‰)";
		}	
		temp2.append("<dataset  seriesName='"+rtpName+" 校园市场' color='"+color1+"' anchorBorderColor='"+color1+"' anchorBgColor='"+color1+"' >\n");
		temp3.append("<dataset  seriesName='"+rtpName+" 农村市场' color='"+color2+"' anchorBorderColor='"+color2+"' anchorBgColor='"+color2+"' >\n");
		temp4.append("<dataset  seriesName='"+rtpName+" 大客户'   color='"+color3+"' anchorBorderColor='"+color3+"' anchorBgColor='"+color3+"' >\n");
		temp5.append("<dataset  seriesName='"+rtpName+" 商业客户' color='"+color4+"' anchorBorderColor='"+color4+"' anchorBgColor='"+color4+"' >\n");
		temp6.append("<dataset  seriesName='"+rtpName+" 城市家庭' color='"+color5+"' anchorBorderColor='"+color5+"' anchorBgColor='"+color5+"' >\n");
		temp7.append("<dataset  seriesName='"+rtpName+" 流动市场' color='"+color6+"' anchorBorderColor='"+color6+"' anchorBgColor='"+color6+"' >\n");
		List<Map<String,Object>>barList=detailMarketDAO.getDetailMarket3_barChart2(queryData);//本月
		for (Map<String, Object> key : barList) {
			String curValue1 = "0";
			String curValue2 = "0";
			String curValue3 = "0";
			String curValue4 = "0";
			String curValue5 = "0";
			String curValue6 = "0";
			curValue1 = String.valueOf(key.get("SHOOL_MARKET"+field) == null ? "0": key.get("SHOOL_MARKET"+field));
			curValue2 = String.valueOf(key.get("COUNTRY_MARKET"+field) == null ? "0": key.get("COUNTRY_MARKET"+field));
			curValue3 = String.valueOf(key.get("BIG_CUSTOMER"+field) == null ? "0": key.get("BIG_CUSTOMER"+field));
			curValue4 = String.valueOf(key.get("BUSINESS_CUSTOMER"+field) == null ? "0": key.get("BUSINESS_CUSTOMER"+field));
			curValue5 = String.valueOf(key.get("CITY_FAMILY"+field) == null ? "0": key.get("CITY_FAMILY"+field));
			curValue6 = String.valueOf(key.get("FLOW_MARKET"+field) == null ? "0": key.get("FLOW_MARKET"+field));
			temp1.append("<category label='" + key.get("REGION_NAME")+ "'/>\n");
			temp2.append("<set      value='" + curValue1 + "' />\n");
			temp3.append("<set      value='" + curValue2 + "' />\n");
			temp4.append("<set      value='" + curValue3 + "' />\n");
			temp5.append("<set      value='" + curValue4 + "' />\n");
			temp6.append("<set      value='" + curValue5 + "' />\n");
			temp7.append("<set      value='" + curValue6 + "' />\n");
		}
		   temp1.append("</categories>\n");
	       temp2.append("</dataset>\n");
	       temp3.append("</dataset>\n");
	       temp4.append("</dataset>\n");
	       temp5.append("</dataset>\n");
	       temp6.append("</dataset>\n");
	       temp7.append("</dataset>\n");
	       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	               " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	               " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	               " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	               " numdivlines='4' adjustDiv='0' rotateNames='0' "+
	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'" +
	               " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	               " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	       barChartMap.append(temp1).append(temp2).append(temp3).append(temp4).append(temp5).append(temp6).append(temp7);
	       barChartMap.append("</chart>");
    return barChartMap.toString();
	}
	public String getDetailMarket4_BarChart1(Map<String,Object>queryData) throws ParseException{
		String field =       MapUtils.getString(queryData, "field1",null);
		StringBuffer  barChartMap=new StringBuffer();//柱状图
		StringBuffer  temp1=new StringBuffer();
		temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
      	StringBuffer  temp4=new StringBuffer();
      	StringBuffer  temp5=new StringBuffer();
      	StringBuffer  temp6=new StringBuffer();
      	StringBuffer  temp7=new StringBuffer();
      	
      	String color1= "458B00";
    	String color2= "CDAD00";
    	String color3= "8DB6CD";
    	String color4= "CD2626";
    	String color5= "00868B";
    	String color6= "EE7942 ";
    	String rtpName="";
		String rptType =MapUtils.getString(queryData, "rptType",null);
		if(rptType.equals("4")||rptType=="4"){
			rtpName="本地抱怨率(‰)";
		}else{
			rtpName="本地投诉率(‰)";
		}	
		temp2.append("<dataset  seriesName='"+rtpName+" 校园市场' color='"+color1+"' anchorBorderColor='"+color1+"' anchorBgColor='"+color1+"' >\n");
		temp3.append("<dataset  seriesName='"+rtpName+" 农村市场' color='"+color2+"' anchorBorderColor='"+color2+"' anchorBgColor='"+color2+"' >\n");
		temp4.append("<dataset  seriesName='"+rtpName+" 大客户'   color='"+color3+"' anchorBorderColor='"+color3+"' anchorBgColor='"+color3+"' >\n");
		temp5.append("<dataset  seriesName='"+rtpName+" 商业客户' color='"+color4+"' anchorBorderColor='"+color4+"' anchorBgColor='"+color4+"' >\n");
		temp6.append("<dataset  seriesName='"+rtpName+" 城市家庭' color='"+color5+"' anchorBorderColor='"+color5+"' anchorBgColor='"+color5+"' >\n");
		temp7.append("<dataset  seriesName='"+rtpName+" 流动市场' color='"+color6+"' anchorBorderColor='"+color6+"' anchorBgColor='"+color6+"' >\n");
		List<Map<String,Object>>barList=detailMarketDAO.getDetailMarket4_barChart1(queryData);//本月
		for (Map<String, Object> key : barList) {
			String curValue1 = "0";
			String curValue2 = "0";
			String curValue3 = "0";
			String curValue4 = "0";
			String curValue5 = "0";
			String curValue6 = "0";
			curValue1 = String.valueOf(key.get("SHOOL_MARKET"+field) == null ? "0": key.get("SHOOL_MARKET"+field));
			curValue2 = String.valueOf(key.get("COUNTRY_MARKET"+field) == null ? "0": key.get("COUNTRY_MARKET"+field));
			curValue3 = String.valueOf(key.get("BIG_CUSTOMER"+field) == null ? "0": key.get("BIG_CUSTOMER"+field));
			curValue4 = String.valueOf(key.get("BUSINESS_CUSTOMER"+field) == null ? "0": key.get("BUSINESS_CUSTOMER"+field));
			curValue5 = String.valueOf(key.get("CITY_FAMILY"+field) == null ? "0": key.get("CITY_FAMILY"+field));
			curValue6 = String.valueOf(key.get("FLOW_MARKET"+field) == null ? "0": key.get("FLOW_MARKET"+field));
			temp1.append("<category label='" + key.get("CMPL_BUSI_TYPE_NAME")+ "'/>\n");
			temp2.append("<set      value='" + curValue1 + "' />\n");
			temp3.append("<set      value='" + curValue2 + "' />\n");
			temp4.append("<set      value='" + curValue3 + "' />\n");
			temp5.append("<set      value='" + curValue4 + "' />\n");
			temp6.append("<set      value='" + curValue5 + "' />\n");
			temp7.append("<set      value='" + curValue6 + "' />\n");
		}
		   temp1.append("</categories>\n");
	       temp2.append("</dataset>\n");
	       temp3.append("</dataset>\n");
	       temp4.append("</dataset>\n");
	       temp5.append("</dataset>\n");
	       temp6.append("</dataset>\n");
	       temp7.append("</dataset>\n");
	       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	               " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	               " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	               " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	               " numdivlines='4' adjustDiv='0' rotateNames='0' "+
	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'" +
	               " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	               " exportFileName='linechart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	       barChartMap.append(temp1).append(temp2).append(temp3).append(temp4).append(temp5).append(temp6).append(temp7);
	       barChartMap.append("</chart>");
    return barChartMap.toString();
	}
	public String getDetailMarket4_BarChart2(Map<String,Object>queryData) throws ParseException{
		String field =       MapUtils.getString(queryData, "field1",null);
		StringBuffer  barChartMap=new StringBuffer();//柱状图
		StringBuffer  temp1=new StringBuffer();
		temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
      	StringBuffer  temp4=new StringBuffer();
      	StringBuffer  temp5=new StringBuffer();
      	StringBuffer  temp6=new StringBuffer();
      	StringBuffer  temp7=new StringBuffer();
      	
      	String color1= "458B00";
    	String color2= "CDAD00";
    	String color3= "8DB6CD";
    	String color4= "CD2626";
    	String color5= "00868B";
    	String color6= "EE7942 ";
    	String rtpName="";
		String rptType =MapUtils.getString(queryData, "rptType",null);
		if(rptType.equals("4")||rptType=="4"){
			rtpName="本地抱怨率(‰)";
		}else{
			rtpName="本地投诉率(‰)";
		}	
		temp2.append("<dataset  seriesName='"+rtpName+" 校园市场' color='"+color1+"' anchorBorderColor='"+color1+"' anchorBgColor='"+color1+"' >\n");
		temp3.append("<dataset  seriesName='"+rtpName+" 农村市场' color='"+color2+"' anchorBorderColor='"+color2+"' anchorBgColor='"+color2+"' >\n");
		temp4.append("<dataset  seriesName='"+rtpName+" 大客户'   color='"+color3+"' anchorBorderColor='"+color3+"' anchorBgColor='"+color3+"' >\n");
		temp5.append("<dataset  seriesName='"+rtpName+" 商业客户' color='"+color4+"' anchorBorderColor='"+color4+"' anchorBgColor='"+color4+"' >\n");
		temp6.append("<dataset  seriesName='"+rtpName+" 城市家庭' color='"+color5+"' anchorBorderColor='"+color5+"' anchorBgColor='"+color5+"' >\n");
		temp7.append("<dataset  seriesName='"+rtpName+" 流动市场' color='"+color6+"' anchorBorderColor='"+color6+"' anchorBgColor='"+color6+"' >\n");
		List<Map<String,Object>>barList=detailMarketDAO.getDetailMarket4_barChart2(queryData);//本月
		for (Map<String, Object> key : barList) {
			String curValue1 = "0";
			String curValue2 = "0";
			String curValue3 = "0";
			String curValue4 = "0";
			String curValue5 = "0";
			String curValue6 = "0";
			curValue1 = String.valueOf(key.get("SHOOL_MARKET"+field) == null ? "0": key.get("SHOOL_MARKET"+field));
			curValue2 = String.valueOf(key.get("COUNTRY_MARKET"+field) == null ? "0": key.get("COUNTRY_MARKET"+field));
			curValue3 = String.valueOf(key.get("BIG_CUSTOMER"+field) == null ? "0": key.get("BIG_CUSTOMER"+field));
			curValue4 = String.valueOf(key.get("BUSINESS_CUSTOMER"+field) == null ? "0": key.get("BUSINESS_CUSTOMER"+field));
			curValue5 = String.valueOf(key.get("CITY_FAMILY"+field) == null ? "0": key.get("CITY_FAMILY"+field));
			curValue6 = String.valueOf(key.get("FLOW_MARKET"+field) == null ? "0": key.get("FLOW_MARKET"+field));
			temp1.append("<category label='" + key.get("REGION_NAME")+ "'/>\n");
			temp2.append("<set      value='" + curValue1 + "' />\n");
			temp3.append("<set      value='" + curValue2 + "' />\n");
			temp4.append("<set      value='" + curValue3 + "' />\n");
			temp5.append("<set      value='" + curValue4 + "' />\n");
			temp6.append("<set      value='" + curValue5 + "' />\n");
			temp7.append("<set      value='" + curValue6 + "' />\n");
		}
		   temp1.append("</categories>\n");
	       temp2.append("</dataset>\n");
	       temp3.append("</dataset>\n");
	       temp4.append("</dataset>\n");
	       temp5.append("</dataset>\n");
	       temp6.append("</dataset>\n");
	       temp7.append("</dataset>\n");
	       barChartMap.append("<chart  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'   alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	               " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	               " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	               " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	               " numdivlines='4' adjustDiv='0' rotateNames='0' "+
	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'" +
	               " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	               " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	       barChartMap.append(temp1).append(temp2).append(temp3).append(temp4).append(temp5).append(temp6).append(temp7);
	       barChartMap.append("</chart>");
    return barChartMap.toString();
	}
	
}
