package tydic.reports.complain.monitorDay;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.frame.common.utils.MapUtils;
import tydic.portalCommon.DateUtil;
import tydic.reports.graph.GraphDAO;
/**
 * 
 * @author qx 投诉监测定制化报表
 * 
 */
public class CmplIndexAction {
	private CmplIndexDAO cmplIndexDAO; 
	private GraphDAO graphDAO; 
	
	public GraphDAO getGraphDAO() {
		return graphDAO;
	}
	public void setGraphDAO(GraphDAO graphDAO) {
		this.graphDAO = graphDAO;
	}
	public CmplIndexDAO getCmplIndexDAO() {
		return cmplIndexDAO;
	}
	public void setCmplIndexDAO(CmplIndexDAO cmplIndexDAO) {
		this.cmplIndexDAO = cmplIndexDAO;
	}	
	//指标解释
	public Map getIndexExplain(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
		List<Map<String,Object>> expList=cmplIndexDAO.getIndexExplain(queryData);
		map.put("expList", expList);
		return map;
	}
	//投诉指标监测_存储过程实现
	public Map getCmplIndex_pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
	     map.putAll(cmplIndexDAO.getCmplIndex_Pg(queryData));//投诉类指标检测报表——存储过程实现
		 map.put("barChartMap", getAreaChartXML(queryData,"0"));//柱状图
	     map.put("lineChartMap", getCmplIndexLine(queryData));//折线图
         return map; 
	} 
	//投诉现象分析报表_存储过程
	public Map getCmplSkip_pg(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
	     map.putAll(cmplIndexDAO.getCmplSum_Pg(queryData));//投诉情况报表——存储过程实现
		 map.put("barChartMap", getSkipChartXML(queryData,"0"));//柱状图
	     map.put("lineChartMap", getCmplSkipLine(queryData));//折线图
         return map; 
	}
	//投诉情况报表_存储过程
	public Map getCmplSum_pg (Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
	     map.putAll(cmplIndexDAO.getCmplSum_Pg(queryData));//投诉情况报表——存储过程实现
		 map.put("barChartMap", getAreaChartXML(queryData,"0"));//柱状图
	     map.put("lineChartMap", getCmplIndexLine(queryData));//折线图
         return map; 
	}
	//投诉情况日报表_存储过程
	public Map getCmplDayData (Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
	     map.putAll(cmplIndexDAO.getCmplDayData(queryData));//投诉情况报表——存储过程实现
		 //map.put("barChartMap", getAreaChartXML(queryData,"0"));//柱状图
	     //map.put("lineChartMap", getCmplIndexLine(queryData));//折线图
         return map; 
	}
	//投诉现象分析日报表_存储过程
	public Map getCmplPheDayData (Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
	     map.putAll(cmplIndexDAO.getCmplPheDayData(queryData));//投诉情况报表——存储过程实现
		 //map.put("barChartMap", getAreaChartXML(queryData,"0"));//柱状图
	     //map.put("lineChartMap", getCmplIndexLine(queryData));//折线图
         return map; 
	}
	//投诉现象分析日报表_存储过程
	public Map getCmplReasonDayData (Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
	     map.putAll(cmplIndexDAO.getCmplReasonDayData(queryData));//投诉情况报表——存储过程实现
		 //map.put("barChartMap", getAreaChartXML(queryData,"0"));//柱状图
	     //map.put("lineChartMap", getCmplIndexLine(queryData));//折线图
         return map; 
	}
	public Map getCmplSum_pg_Dg (Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
	     map.putAll(cmplIndexDAO.getCmplSum_Pg_Dg(queryData));//投诉情况报表——存储过程实现
		 map.put("barChartMap", getAreaChartXML_Dg(queryData,"0"));//柱状图
	     map.put("lineChartMap", getCmplIndexLine(queryData));//折线图
         return map; 
	}
	//投诉指标监测_初始化
	public Map getCmplIndex(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
		String rptIndex=MapUtils.getString(queryData, "rptIndex", null);
		List<Map<String,Object>>tabList=null;
		//日报表
		if("cmplIndexDay".equals(rptIndex)){//投诉类指标日监测报表
			tabList=cmplIndexDAO.getCmplIndex_Day(queryData);//投诉指标日监测表格数据
		}
		//月报表
		if("cmplIndexMon".equals(rptIndex)){//投诉类指标月监测报表
	        tabList=cmplIndexDAO.getCmplIndex_Mon(queryData);//投诉指标月监测表格数据
		}
		     map.put("barChartMap", getAreaChartXML(queryData,"0"));//柱状图
		     map.put("lineChartMap", getCmplIndexLine(queryData));//折线图
		     map.put("list", tabList);
         return map; 
	}
	//投诉指标监测_指标切换
	public Map getCmplIndexChange(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
		map.put("barChartMap", getAreaChartXML(queryData,"0"));//柱状图
	    map.put("lineChartMap", getCmplIndexLine(queryData));//折线图
        return map; 
	}
	//投诉现象_指标切换换
	public Map getCmplSkipChange(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
		map.put("barChartMap", getSkipChartXML(queryData,"0"));//柱状图
	    map.put("lineChartMap", getCmplSkipLine(queryData));//折线图
        return map; 
	}
	//投诉总体情况汇总月报表_指标切换
	public Map getCmplSumChange(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();//参数map
		map.put("barChartMap", getAreaChartXML(queryData,"0"));//柱状图
	    map.put("lineChartMap", getCmplIndexLine(queryData));//折线图
        return map; 
	}
	//投诉指标监测_折线图
	public String getCmplIndexLine(Map<String, Object> queryData) {
		int numVdivlines =0;
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

		//日报表
		if("cmplIndexDay".equals(rptIndex)){//投诉类指标日监测报表
			String inDate=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月日
			String inDateYM=inDate.substring(0,6);//当前日期年月
			String lastYM=DateUtil.getLastMon(inDateYM);//获取上月年月
			
			String curStartTime=inDateYM+"01";
			String curEndTime=inDate;
	        temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inDateYM+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYM+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
	        //日报表折线图
	        List<Map<String,Object>>curList=cmplIndexDAO.getLinePoint(curStartTime, curEndTime, queryData);//当月日期列表
	        for (Map<String, Object> key : curList){
	        	  String lastValue="";
		       	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
		       	  String dateTime=String.valueOf(key.get("DAY_ID"));
		          String inY=dateTime.substring(0,4);//当前日期年
		          String inM=dateTime.substring(4,6);//当前月
		          String inD=dateTime.substring(6,8);//当前日
		          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    	      String lastTime=sdf.format(DateUtil.getDateOfLastMonth(inY+inM+inD).getTime());
		       	  Map<String, Object> lastObj = cmplIndexDAO.getChartData_CmplIndexDay(lastTime, queryData);
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
		if("cmplIndexMon".equals(rptIndex)){//投诉类指标月监测报表
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
				
				Map<String, Object> currentObj = cmplIndexDAO.getChartData_CmplIndexMon(currentYM, queryData);
				Map<String, Object> lastObj = cmplIndexDAO.getChartData_CmplIndexMon(lastYMM, queryData);

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
		}
		//月报表
		if("cmplSumMon".equals(rptIndex)){//投诉总体情况月报表
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
				
				Map<String, Object> currentObj = cmplIndexDAO.getChartData_CmplSumMon(currentYM, queryData);
				Map<String, Object> lastObj = cmplIndexDAO.getChartData_CmplSumMon(lastYMM, queryData);

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
		}
		//月报表
		if("cmplSumMonZone".equals(rptIndex)){//某区域投诉总体情况月报表
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
				
				Map<String, Object> currentObj = cmplIndexDAO.getChartData_CmplSumZoneMon(currentYM, queryData);
				Map<String, Object> lastObj = cmplIndexDAO.getChartData_CmplSumZoneMon(lastYMM, queryData);

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
		}
		if("allBusiCmplMon".equals(rptIndex)){//本地全业务抱怨率月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String inYear=inYearMon.substring(0,4);
			String lastYear=Integer.parseInt(inYear)-1+"";
			String inMon=inYearMon.substring(4,6);
			String lastYearMon=Integer.parseInt(inYear)-1+inMon;//去年同期
	        temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='"+inYearMon+"'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	        temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='"+lastYearMon+"'        color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
			
			String curStartTime=inYear+"01";
			String curEndTime=inYearMon;
			String lastStartTime=lastYear+"01";
			String lastEndTime=lastYearMon;
			   //月报表折线图
	        List<Map<String,Object>>curList=cmplIndexDAO.getLinePointAllBusiMon(curStartTime, curEndTime, queryData);//当月日期列表
	        List<Map<String,Object>>lastList=cmplIndexDAO.getLinePointAllBusiMon(lastStartTime, lastEndTime, queryData);//当月日期列表
	        for (Map<String, Object> key : curList){
		       	  String curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
		       	  String dateTime=String.valueOf(key.get("MONTH_ID"));
		          String inM=dateTime.substring(4,6);//当前月
		       	  temp1.append("<category name='"+Integer.parseInt(inM)+ "月' />\n");
		          temp2.append("<set value='"+curValue+"' hoverText='"+dateTime+"： "+curValue+"' />\n");
		       }
	        for (Map<String, Object> key : lastList){
	        	  String lastValue = String.valueOf(key.get(field) == null ? "0": key.get(field));
		       	  String dateTime=String.valueOf(key.get("MONTH_ID"));
		    	  temp3.append("<set value='"+lastValue+"'    hoverText='"+dateTime+"： "+lastValue+"' />\n"); 
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
	//本地、越级 投诉现象、原因_折线图
	public String getCmplSkipLine(Map<String, Object> queryData) {
		int numVdivlines =-1;
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
		String cmplBusinessType =       MapUtils.getString(queryData, "cmplBusinessType",null);
		String reasonId =       MapUtils.getString(queryData, "reasonId",null);
		String tempStart="01";//折线图起点

		//月报表
		if("cmplSkipProblemMon".equals(rptIndex)){//越级投诉现象月报
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
				Map<String, Object> currentObj;
				Map<String, Object> lastObj;
				String currentYM=inYear+lineMon;
				String lastYMM=Integer.parseInt(inYear)-1+lineMon;
				if(cmplBusinessType==null||"".equals(cmplBusinessType)){
					currentObj = cmplIndexDAO.getChartData_CmplSkipMon(currentYM, queryData);
					lastObj = cmplIndexDAO.getChartData_CmplSkipMon(lastYMM, queryData);
		        }else{
		            currentObj = cmplIndexDAO.getChartData_CmplSkipMon2(currentYM, queryData);
					lastObj = cmplIndexDAO.getChartData_CmplSkipMon2(lastYMM, queryData);
		        }
				

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
		}
		if("nativeCmplPheMon".equals(rptIndex)){//本地投诉现象月报
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
				Map<String, Object> currentObj;
				Map<String, Object> lastObj;
				String currentYM=inYear+lineMon;
				String lastYMM=Integer.parseInt(inYear)-1+lineMon;
				if(cmplBusinessType==null||"".equals(cmplBusinessType)){
					currentObj = cmplIndexDAO.getChartData_CmplMon(currentYM, queryData);
					lastObj = cmplIndexDAO.getChartData_CmplMon(lastYMM, queryData);
		        }else{
		            currentObj = cmplIndexDAO.getChartData_CmplMon2(currentYM, queryData);
					lastObj = cmplIndexDAO.getChartData_CmplMon2(lastYMM, queryData);
		        }
				

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
		}
		if("cmplReasonProblemMon".equals(rptIndex)){//越级投诉原因月报
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
				Map<String, Object> currentObj;
				Map<String, Object> lastObj;
				String currentYM=inYear+lineMon;
				String lastYMM=Integer.parseInt(inYear)-1+lineMon;
				if(reasonId==null||"".equals(reasonId)){
					queryData.put("tabStr","CS_CMPL_SKIP_REASON_MON");
					currentObj = cmplIndexDAO.getChartData_CmplReasonMon(currentYM, queryData);
					lastObj = cmplIndexDAO.getChartData_CmplReasonMon(lastYMM, queryData);
		        }else{
		        	queryData.put("tabStr","CS_CMPL_SKIP_REASON_MON");
		            currentObj = cmplIndexDAO.getChartData_CmplReasonMon2(currentYM, queryData);
					lastObj = cmplIndexDAO.getChartData_CmplReasonMon2(lastYMM, queryData);
		        }
				

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
		}
		if("nativeCmplReasonMon".equals(rptIndex)){//本地投诉原因月报
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
				Map<String, Object> currentObj;
				Map<String, Object> lastObj;
				String currentYM=inYear+lineMon;
				String lastYMM=Integer.parseInt(inYear)-1+lineMon;
				if(reasonId==null||"".equals(reasonId)){
					queryData.put("tabStr","CS_CMPL_REASON_MON");
					currentObj = cmplIndexDAO.getChartData_CmplReasonMon(currentYM, queryData);
					lastObj = cmplIndexDAO.getChartData_CmplReasonMon(lastYMM, queryData);
		        }else{
		        	queryData.put("tabStr","CS_CMPL_REASON_MON");
		            currentObj = cmplIndexDAO.getChartData_CmplReasonMon2(currentYM, queryData);
					lastObj = cmplIndexDAO.getChartData_CmplReasonMon2(lastYMM, queryData);
		        }
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
		}
		if("cmplOffLineMon".equals(rptIndex)){//年度离网用户投诉情况分析月报表
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
				
				Map<String, Object> currentObj = cmplIndexDAO.getChartData_CmplOffMonLine(i,inYearMon, queryData);
				Map<String, Object> lastObj = cmplIndexDAO.getChartData_CmplOffMonLine(i,lastYearMon, queryData);

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
		}
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
		   String changeZone=MapUtils.getString(map, "changeZone",    null);//0代表五大区，1代表21地市
		   Map<String, Object> key=new HashMap<String, Object>(); 
		   String strXML=getAreaChartXML(map,changeZone);
		   key.put("XML",strXML);
		   return key;
	   }
	   public Map<String, Object> loadSet21AreaSkip(Map<String, Object> map){
		   String changeZone=MapUtils.getString(map, "changeZone",    null);//0代表五大区，1代表21地市
		   Map<String, Object> key=new HashMap<String, Object>(); 
		   String strXML=getSkipChartXML(map,changeZone);
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
	        int i=0;
	    	
	        StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
 
			String rptIndex=MapUtils.getString(queryData, "rptIndex", null);
			Map<String,Object> param=new HashMap<String, Object>();
			
		    List<Map<String,Object>>barList=null;
		    List<Map<String,Object>>barList2=null;
			//日报表
		if ("cmplIndexDay".equals(rptIndex)) {// 投诉类指标日监测报表
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
				param.put("ind",MapUtils.getString(queryData, "ind", null) == "" ? "3": Convert.toString(queryData.get("ind")));
				Map<String, Object> lastObj = cmplIndexDAO.getChartData_CmplIndexDay(lastDate, param);
				Map<String, Object> curObj = cmplIndexDAO.getChartData_CmplIndexDay(inDate, param);
				if (null != lastObj) {
					lastValue = String.valueOf(lastObj.get("TOTAL_NUM") == null ? "0": lastObj.get("TOTAL_NUM"));
				} else {
					lastValue = "0";
				}
				if (null != curObj) {
					curValue = String.valueOf(curObj.get("TOTAL_NUM") == null ? "0": curObj.get("TOTAL_NUM"));
				} else {
					curValue = "0";
				}
				temp1.append("<category label='" + key.get("ZONE_NAME")+ "'/>\n");
				temp2.append("<set      value='" + curValue + "' />\n");
				temp3.append("<set      value='" + lastValue + "' />\n");
			}
			}
			 //月报表
			if("cmplIndexMon".equals(rptIndex)){//投诉类指标月监测报表
				String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
				String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
				
			    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
			    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
			
			    barList=graphDAO.getChangeZoneLevel(queryData);
			     //柱状图_月报表
			       for (Map<String, Object> key : barList){
			       	  String curValue="0";
			   		  String lastValue="0";
			   		  
			   		  param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0000": key.get("ZONE_CODE")));
			   		  param.put("ind",MapUtils.getString(queryData, "ind", null)=="" ? "3": Convert.toString(queryData.get("ind")));//本地全业务抱怨率
			   	      Map<String, Object> lastObj =    cmplIndexDAO.getChartData_CmplIndexMon(lastYearMon,param);
			   	      Map<String, Object> curObj = cmplIndexDAO.getChartData_CmplIndexMon(inYearMon, param);
			   		  if (null != lastObj) {
			   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
			   		   }else{
			   			  lastValue="0";
			   		   }
			   		  if (null != curObj) {
							curValue = String.valueOf(curObj.get(field) == null ? "0": curObj.get(field));
						} else {
							curValue = "0";
						}
			        	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
			     	    temp2.append("<set      value='"+curValue+"' />\n");
			     	    temp3.append("<set      value='"+lastValue+"' />\n");
			       } 
			} //月报表
			if("cmplSumMon".equals(rptIndex)){//投诉总体情况月汇总报表
				String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
				String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
				
			    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
			    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
			
			    barList=graphDAO.getChangeZoneLevel(queryData);
			     //柱状图_月报表
			       for (Map<String, Object> key : barList){
			       	  String curValue="0";
			   		  String lastValue="0";
			   		  
			   		  param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0000": key.get("ZONE_CODE")));
			   		  
			   	      Map<String, Object> lastObj =    cmplIndexDAO.getChartData_CmplSumMon(lastYearMon,param);
			   	      Map<String, Object> curObj =  cmplIndexDAO.getChartData_CmplSumMon(inYearMon,param);
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
			}
			if("cmplSumMonZone".equals(rptIndex)){//某区域投诉总体情况月汇总报表
				String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
				String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
				
			    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
			    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
			
			    barList=graphDAO.getChangeZoneLevel(queryData);
			    param.put("ind",MapUtils.getString(queryData, "ind", null)==null  ? "": MapUtils.getString(queryData, "ind", null));
			     //柱状图_月报表
			       for (Map<String, Object> key : barList){
			       	  String curValue="0";
			   		  String lastValue="0";
			   		  
			   		  param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
			   		  
			   	      Map<String, Object> lastObj =    cmplIndexDAO.getChartData_CmplSumZoneMon(lastYearMon,param);
			   	      Map<String, Object> curObj =  cmplIndexDAO.getChartData_CmplSumZoneMon(inYearMon,param);
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
			}
			if("allBusiCmplMon".equals(rptIndex)){//本地全业务抱怨率月报表
				String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
				String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
				
			    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
			    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
				
			    //月报表
			    barList=cmplIndexDAO.getBarAllBusiMon_Zone(queryData);//区域权限控制处理区域图形层级 
			    queryData.put("dateTime", lastYearMon);
			    barList2=cmplIndexDAO.getBarAllBusiMon_Zone(queryData);//区域权限控制处理区域图形层级 
			    queryData.put("dateTime", inYearMon);
			     //柱状图_月报表
			       for (Map<String, Object> key : barList){
			       	  String curValue="0";
			   		  String lastValue="0";
			   		Map<String, Object> key2=barList2.get(i++);
			   		  param.put("zoneCode", String.valueOf(key.get("REGION_ID") == null ? "0000" : key.get("REGION_ID")));
			   		  param.put("prodTypeCode",MapUtils.getString(queryData, "prodTypeCode", null)=="" ? "-1": Convert.toString(queryData.get("prodTypeCode")));
			   		  param.put("cmplBusiTypeCode",MapUtils.getString(queryData, "cmplBusiTypeCode", null)=="" ? "1": Convert.toString(queryData.get("cmplBusiTypeCode")));
			   		  curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
			   		  lastValue=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
			   	      /*Map<String, Object> lastObj =    cmplIndexDAO.getChartData_AllBusiMon(lastYearMon,param);
			   		Map<String, Object> lastObj =null;
			   		  if (null != lastObj) {
			   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
			   		   }else{
			   			  lastValue="0";
			   		   }*/
			        	temp1.append("<category label='"+key.get("REGION_NAME")+"'/>\n");
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
	               " numdivlines='4' adjustDiv='0' rotateNames='0'"+
	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'"+
	               " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	               " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	       barChartMap.append(temp1).append(temp2).append(temp3);
	       barChartMap.append("</chart>");
	      return barChartMap.toString();	
	  }
		private String getAreaChartXML_Dg(Map<String, Object> queryData,String changeZone) {
			StringBuffer  barChartMap=new StringBuffer();
			//柱状图展示
	    	String field =       MapUtils.getString(queryData, "field",null);
	    	String zoneCode =       MapUtils.getString(queryData, "zoneCode",null);
			String currentColor= "FBC62A";//当月
	    	String lastColor=    "71B359";//上月
	    	int i=0;
	        
	        StringBuffer  temp1=new StringBuffer();
	    	temp1.append("<categories>\n");
	      	StringBuffer  temp2=new StringBuffer();
	      	StringBuffer  temp3=new StringBuffer();
 
			String rptIndex=MapUtils.getString(queryData, "rptIndex", null);
			Map<String,Object> param=new HashMap<String, Object>();
			
		    List<Map<String,Object>>barList=null;
		    List<Map<String,Object>>barList2=null;
			//日报表
		
			if("allBusiCmplMon".equals(rptIndex)){//本地全业务抱怨率月报表
				String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
				String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
				
			    temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
			    temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
				
			    //月报表
			    barList=cmplIndexDAO.getBarAllBusiMon_Zone_Dg(queryData);//区域权限控制处理区域图形层级 
			    queryData.put("dateTime", lastYearMon);
			    barList2=cmplIndexDAO.getBarAllBusiMon_Zone_Dg(queryData);//区域权限控制处理区域图形层级 
			    queryData.put("dateTime", inYearMon);
			     //柱状图_月报表
			       for (Map<String, Object> key : barList){
			       	  String curValue="0";
			   		  String lastValue="0";
			   		Map<String, Object> key2=barList2.get(i++);
			   		 // param.put("zoneCode", String.valueOf(key.get("REGION_ID") == null ? "0000" : key.get("REGION_ID")));
			   		//  param.put("prodTypeCode",MapUtils.getString(queryData, "prodTypeCode", null)=="" ? "-1": Convert.toString(queryData.get("prodTypeCode")));
			   		 // param.put("cmplBusiTypeCode",MapUtils.getString(queryData, "cmplBusiTypeCode", null)=="" ? "1": Convert.toString(queryData.get("cmplBusiTypeCode")));
			   		  curValue=String.valueOf(key.get(field) == null ? "0": key.get(field));
			   		  lastValue=String.valueOf(key2.get(field) == null ? "0": key2.get(field));
			   	     /* Map<String, Object> lastObj =    cmplIndexDAO.getChartData_AllBusiMon(lastYearMon,param);
			   		  if (null != lastObj) {
			   	    	  lastValue = String.valueOf(lastObj.get(field) == null ? "0": lastObj.get(field));
			   		   }else{
			   			  lastValue="0";
			   		   }*/
			        	temp1.append("<category label='"+key.get("REGION_NAME")+"'/>\n");
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
	               " numdivlines='4' adjustDiv='0' rotateNames='0'"+
	               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'"+
	               " exportEnabled='1' exportDialogMessage='正在生成,请稍候...' exportFormats='PNG=输出为PNG|JPEG=输出为JPEG' " +
	               " exportFileName='barchart' exportAtClient='0' exportAction='save' exportHandler='http://132.121.165.45:8081/tydic-bi-meta/fcf/jsp/FCExporter.jsp'>\n");
	       barChartMap.append(temp1).append(temp2).append(temp3);
	       barChartMap.append("</chart>");
	      return barChartMap.toString();	
	  }
		/**
		 *   投诉现象/原因柱状图XML
		  * 方法描述：
		  * @param: 
		  * @return: 
		  * @version: 1.0
		  * @author: qx
		  * @version: 2013-5-20 下午05:22:26
		 */
		private String getSkipChartXML(Map<String, Object> queryData,String changeZone) {
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
			String cmplBusinessType =       MapUtils.getString(queryData, "cmplBusinessType",null);
			String reasonId =       MapUtils.getString(queryData, "reasonId",null);
			String zoneCode =       MapUtils.getString(queryData, "zoneCode",null);
			
			Map<String,Object> param=new HashMap<String, Object>();			
		    List<Map<String,Object>>barList=null;
			//月报表
		if ("cmplSkipProblemMon".equals(rptIndex)) {// 越级投诉现象月报
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月

			temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
			temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
            if(cmplBusinessType==null||"".equals(cmplBusinessType)){
			   barList = cmplIndexDAO.getBarCmplSkip_1Problem(queryData);// 第一级
            }else{
               barList = cmplIndexDAO.getBarCmplSkip_2Problem(queryData);// 第二级
            }
		        //柱状图_月报表
			for (Map<String, Object> key : barList) {
				String curValue = "0";
				String lastValue = "0";
				Map<String, Object> lastObj;
				Map<String, Object> curObj;
				param.put("zoneCode", MapUtils.getString(queryData, "zoneCode", null) == "" ? "0000": Convert.toString(queryData.get("zoneCode")));
				param.put("channelType",MapUtils.getString(queryData, "channelType", null) == "" ? "": Convert.toString(queryData.get("channelType")));
				param.put("prodType",MapUtils.getString(queryData, "prodType", null) == "" ? "": Convert.toString(queryData.get("prodType")));
				if(cmplBusinessType==null||"".equals(cmplBusinessType)){
					param.put("cmplBusinessType",String.valueOf(key.get("CMPL_BUSINESS_TYPE1_ID")));
					lastObj = cmplIndexDAO.getChartData_CmplSkipMon(lastYearMon, param);
					curObj = cmplIndexDAO.getChartData_CmplSkipMon(inYearMon, param);
		        }else{
		            param.put("cmplBusinessType",String.valueOf(key.get("CMPL_BUSINESS_TYPE2_ID")));
		            lastObj = cmplIndexDAO.getChartData_CmplSkipMon1(lastYearMon, param);
					curObj = cmplIndexDAO.getChartData_CmplSkipMon1(inYearMon, param);
		        }
				
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
				if(cmplBusinessType==null||"".equals(cmplBusinessType)){
					temp1.append("<category label='" + key.get("CMPL_BUSINESS_TYPE1_NAME")+ "'/>\n");
		        }else{
		        	temp1.append("<category label='" + key.get("CMPL_BUSINESS_TYPE2_NAME")+ "'/>\n");
		        }
				temp2.append("<set      value='" + curValue + "' />\n");
				temp3.append("<set      value='" + lastValue + "' />\n");
			}
			}
		if ("nativeCmplPheMon".equals(rptIndex)) {// 本地投诉现象月报
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月

			temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
			temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
            if(cmplBusinessType==null||"".equals(cmplBusinessType)){
			   barList = cmplIndexDAO.getBarNativeCmplSkip_1Problem(queryData);// 第一级
            }else{
               barList = cmplIndexDAO.getBarNativeCmplSkip_2Problem(queryData);// 第二级
            }
		        //柱状图_月报表
			for (Map<String, Object> key : barList) {
				String curValue = "0";
				String lastValue = "0";
				Map<String, Object> lastObj;
				Map<String, Object> curObj;
				param.put("zoneCode", MapUtils.getString(queryData, "zoneCode", null) == "" ? "0000": Convert.toString(queryData.get("zoneCode")));
				param.put("prodType",MapUtils.getString(queryData, "prodType", null) == "" ? "": Convert.toString(queryData.get("prodType")));
				if(cmplBusinessType==null||"".equals(cmplBusinessType)){
					param.put("cmplBusinessType",String.valueOf(key.get("CMPL_BUSINESS_TYPE1_ID")));
					lastObj = cmplIndexDAO.getChartData_CmplMon(lastYearMon, param);
					curObj = cmplIndexDAO.getChartData_CmplMon(inYearMon, param);
		        }else{
		            param.put("cmplBusinessType",String.valueOf(key.get("CMPL_BUSINESS_TYPE2_ID")));
		            lastObj = cmplIndexDAO.getChartData_CmplMon1(lastYearMon, param);
					curObj = cmplIndexDAO.getChartData_CmplMon1(inYearMon, param);
		        }
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
				if(cmplBusinessType==null||"".equals(cmplBusinessType)){
					temp1.append("<category label='" + key.get("CMPL_BUSINESS_TYPE1_NAME")+ "'/>\n");
		        }else{
		        	temp1.append("<category label='" + key.get("CMPL_BUSINESS_TYPE2_NAME")+ "'/>\n");
		        }
				temp2.append("<set      value='" + curValue + "' />\n");
				temp3.append("<set      value='" + lastValue + "' />\n");
			}
			}
		//月报表
		if ("cmplReasonProblemMon".equals(rptIndex)) {// 越级投诉原因月报
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月

			temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
			temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
            if(reasonId==null||"".equals(reasonId)){
			   barList = cmplIndexDAO.getBarCmplReason_1Problem(queryData,"CS_CMPL_SKIP_REASON_MON");// 第一级
            }else{
               barList = cmplIndexDAO.getBarCmplReason_2Problem(queryData,"CS_CMPL_SKIP_REASON_MON");// 第二级
            }
		        //柱状图_月报表
			for (Map<String, Object> key : barList) {
				String curValue = "0";
				String lastValue = "0";
				Map<String, Object> lastObj;
				Map<String, Object> curObj;
				param.put("zoneCode", MapUtils.getString(queryData, "zoneCode", null) == "" ? "0000": Convert.toString(queryData.get("zoneCode")));
				param.put("channelType",MapUtils.getString(queryData, "channelType", null) == "" ? "": Convert.toString(queryData.get("channelType")));
				if(reasonId==null||"".equals(reasonId)){
					param.put("reasonId",String.valueOf(key.get("CMPL_REASON1_ID")));
					param.put("tabStr","CS_CMPL_SKIP_REASON_MON");
					lastObj = cmplIndexDAO.getChartData_CmplReasonMon(lastYearMon, param);
					curObj = cmplIndexDAO.getChartData_CmplReasonMon(inYearMon, param);
		        }else{
		        	param.put("tabStr","CS_CMPL_SKIP_REASON_MON");
		            param.put("reasonId",String.valueOf(key.get("CMPL_REASON2_ID")));
		            lastObj = cmplIndexDAO.getChartData_CmplReasonMon1(lastYearMon, param);
					curObj = cmplIndexDAO.getChartData_CmplReasonMon1(inYearMon, param);
		        }
				
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
				if(reasonId==null||"".equals(reasonId)){
					temp1.append("<category label='" + key.get("CMPL_REASON1_NAME")+ "'/>\n");
		        }else{
		        	temp1.append("<category label='" + key.get("CMPL_REASON2_NAME")+ "'/>\n");
		        }
				temp2.append("<set      value='" + curValue + "' />\n");
				temp3.append("<set      value='" + lastValue + "' />\n");
			}
			}
		if ("nativeCmplReasonMon".equals(rptIndex)) {// 本地投诉原因月报
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月

			temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
			temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
            if(reasonId==null||"".equals(reasonId)){
			   barList = cmplIndexDAO.getBarCmplReason_1Problem(queryData,"CS_CMPL_REASON_MON");// 第一级
            }else{
               barList = cmplIndexDAO.getBarCmplReason_2Problem(queryData,"CS_CMPL_REASON_MON");// 第二级
            }
		        //柱状图_月报表
			for (Map<String, Object> key : barList) {
				String curValue = "0";
				String lastValue = "0";
				Map<String, Object> lastObj;
				Map<String, Object> curObj;
				param.put("zoneCode", MapUtils.getString(queryData, "zoneCode", null) == "" ? "0000": Convert.toString(queryData.get("zoneCode")));
				param.put("channelType",MapUtils.getString(queryData, "channelType", null) == "" ? "": Convert.toString(queryData.get("channelType")));
				if(reasonId==null||"".equals(reasonId)){
					param.put("reasonId",String.valueOf(key.get("CMPL_REASON1_ID")));
					param.put("tabStr","CS_CMPL_REASON_MON");
					lastObj = cmplIndexDAO.getChartData_CmplReasonMon(lastYearMon, param);
					curObj = cmplIndexDAO.getChartData_CmplReasonMon(inYearMon, param);
		        }else{
		            param.put("reasonId",String.valueOf(key.get("CMPL_REASON2_ID")));
		            param.put("tabStr","CS_CMPL_REASON_MON");
		            lastObj = cmplIndexDAO.getChartData_CmplReasonMon1(lastYearMon, param);
					curObj = cmplIndexDAO.getChartData_CmplReasonMon1(inYearMon, param);
		        }
				
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
				if(reasonId==null||"".equals(reasonId)){
					temp1.append("<category label='" + key.get("CMPL_REASON1_NAME")+ "'/>\n");
		        }else{
		        	temp1.append("<category label='" + key.get("CMPL_REASON2_NAME")+ "'/>\n");
		        }
				temp2.append("<set      value='" + curValue + "' />\n");
				temp3.append("<set      value='" + lastValue + "' />\n");
			}
			}
		if ("cmplOffLineMon".equals(rptIndex)) {// 年度离网用户投诉分布情况月报表
			String inYearMon=MapUtils.getString(queryData, "dateTime", null).replace("-", "");//当前 年月
			String lastYearMon=DateUtil.getLastMon(inYearMon);//上月年月
			
			int i=Integer.parseInt(inYearMon.substring(4,6));
			int j=Integer.parseInt(lastYearMon.substring(4,6));

			temp2.append("<dataset  seriesName='" + inYearMon + "' color='"+ currentColor + "' anchorBorderColor='" + currentColor+ "' anchorBgColor='" + currentColor + "' >\n");
			temp3.append("<dataset  seriesName='" + lastYearMon + "'    color='"+ lastColor + "'    anchorBorderColor='" + lastColor+ "'    anchorBgColor='" + lastColor + "' >\n");
			if("0".equals(changeZone)){
			       //月报表
			       barList=cmplIndexDAO.getBarCmplIndex_5Zone(zoneCode);//五大区柱状图数据     
			    }if("1".equals(changeZone)){	
			    	barList=cmplIndexDAO.getBarCmplIndex_21Zone();
			    }
		        //柱状图_月报表
			for (Map<String, Object> key : barList) {
				  String curValue="0";
		   		  String lastValue="0"; 
		   		  param.put("zoneCode",String.valueOf(key.get("ZONE_CODE") == null ? "0": key.get("ZONE_CODE")));
		   		  
		   	      Map<String, Object> lastObj =    cmplIndexDAO.getChartData_CmplOffMonLine(j,lastYearMon,param);//上月的上月值
		   	      Map<String, Object> curObj =  cmplIndexDAO.getChartData_CmplOffMonLine(i,inYearMon,param);//当月的当月值
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
}
