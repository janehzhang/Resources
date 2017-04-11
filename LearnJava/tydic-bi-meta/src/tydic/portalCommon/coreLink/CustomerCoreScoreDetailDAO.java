package tydic.portalCommon.coreLink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;
import tydic.meta.sys.code.CodeManager;

public class CustomerCoreScoreDetailDAO extends MetaBaseDAO {

	public List<Map<String, Object>> getSelectMon() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("select month_id from (select distinct month_id as month_id from  customerCore_Mon  order by month_id desc)");
		return getDataAccess().queryForList(buffer.toString());
	}
	public List<Map<String, Object>> getListDetailData(Map<String, Object> queryData){
		 String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	     String zoneId = Convert.toString(queryData.get("zoneId"));
         String sql="select a.BUSI_STEP_ID,a.BUSI_STEP_NAME,round(a.BUSI_STEP_WEIGHT*100,2) BUSI_STEP_WEIGHT,round(a.STEP_OFFSET*100,2) STEP_OFFSET,round(a.CMPL_OFFSET*100,2) CMPL_OFFSET,round(a.TOTAL_OFFSET*100,2) TOTAL_OFFSET,round(a.STEP_SCORE*100,2) STEP_SCORE,round(a.REAL_SCORE,2) REAL_SCORE,a.SORT_NUM"
             +" from CS_BUSI_STEP_DETAIL a, META_DIM_ZONE b"
             +" where a.region_id = b.zone_code"
             +" and b.zone_id in (" + zoneId + ")"
             +" and a.month_id="+dateTime
             +" order by to_number(a.BUSI_STEP_ID) asc";
	     return getDataAccess().queryForList(sql);
	  }
	
//客户服务核心环节详细得分
	public Map<String, Object> getHjCoreData(Map<String, Object> queryData) {
		  Map<String, Object> map=new HashMap<String,Object>();
	      String dateTime = Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
	      String zoneId = Convert.toString(queryData.get("zoneId"));
          String sql="select a.BUSI_STEP_ID,a.BUSI_STEP_NAME,round(a.BUSI_STEP_WEIGHT*100,2) BUSI_STEP_WEIGHT,round(a.STEP_OFFSET*100,2) STEP_OFFSET,round(a.CMPL_OFFSET*100,2) CMPL_OFFSET,round(a.TOTAL_OFFSET*100,2) TOTAL_OFFSET,round(a.STEP_SCORE*100,2) STEP_SCORE,round(a.REAL_SCORE,2) REAL_SCORE,a.SORT_NUM"
              +" from CS_BUSI_STEP_DETAIL a, META_DIM_ZONE b"
              +" where a.region_id = b.zone_code"
              +" and b.zone_id in (" + zoneId + ")"
              +" and a.month_id="+dateTime
              +" order by to_number(a.BUSI_STEP_ID) asc";
           List<Map<String,Object>> list=getDataAccess().queryForList(sql);
           map.put("list", list);
           
     		 //String year      = String.valueOf(dateTime).substring(0, 4);
    	     //String month_no  = String.valueOf(dateTime).substring(4, 6);
    		 String showCurrentYEAR = String.valueOf(dateTime);// 当月
	   	     //String showLastYEAR = String.valueOf(Integer.parseInt(year)-1)+month_no;// 去年同期
    		 String showLastYEAR = getLastMon(showCurrentYEAR);
           
           StringBuffer  chartMap=new StringBuffer();
           chartMap.append("<chart hovercapbg='FFECAA' hovercapborder='F47E00' palette='1' bgcolor='FFFFFF' useRoundEdges='1' caption='环节得分' decimals='2' showLabels='1' showvalues='0' " +
           		"numberSuffix='%' sYAxisValuesDecimals='2' connectNullData='0' PYAxisName='' " +
           		"SYAxisName='' numDivLines='4' formatNumberScale='0' labelDisplay='ROTATE' slantLabels='1' exportEnabled='1' exportFormats='PDF=输出为PDF|PNG=输出为PNG|JPEG=输出为JPEG'  exportFileName='报表图形'  exportAtClient='0'  exportAction='download' exportHandler='http://132.121.165.45:8081/tydic_generateReportsV1/FCExporterDirectPic.jsp'>\n");
		
           StringBuffer  temp1=new StringBuffer();
           temp1.append("<categories>\n");
           StringBuffer  temp2=new StringBuffer();
           temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='FBC62A' showValues='0'>\n");
           StringBuffer  temp3=new StringBuffer();
           temp3.append("<dataset  seriesName='"+showLastYEAR+"'     color='71B359' showValues='0' >\n");
           StringBuffer  temp4=new StringBuffer();
           temp4.append("<dataset seriesName='100分制' color='FF0000' showValues='0' parentYAxis='S' renderAs = \"Line\">\n");
           
           for (Map<String, Object> key : list){
        		  String lastValue="0";
        		  String indId=(String)key.get("BUSI_STEP_ID");
	    	      Map<String, Object> lastObj =    getMonByHjCoreValue(showLastYEAR,  zoneId ,  indId);
	    	      if (null != lastObj) {
	    	    	  lastValue = String.valueOf(lastObj.get("STEP_SCORE") == null ? "0": lastObj.get("STEP_SCORE"));
	    		   }
	    	      
        	   temp1.append("<category label='"+key.get("BUSI_STEP_NAME")+"'/>\n");
        	   temp2.append("<set value='"+key.get("STEP_SCORE")+"' />\n");
        	   temp3.append("<set value='"+lastValue+"' />\n");
        	   temp4.append("<set value='"+key.get("REAL_SCORE")+"' />\n");
           }
           temp1.append("</categories>\n");
           temp2.append("</dataset>\n");
           temp3.append("</dataset>\n");
           temp4.append("</dataset>\n");
           chartMap.append(temp1).append(temp2).append(temp3).append(temp4);
		   chartMap.append("</chart>");
		   map.put("chartMap", chartMap.toString());
	       return map;
	}

	
	
	
	
	//偏离
	public Map<String, Object> getHjBllData(Map<String, Object> queryData) {
		  Map<String, Object> map=new HashMap<String,Object>();
	      String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneId = Convert.toString(queryData.get("zoneId"));

	      /*	      String sql = "select A.AREA_NAME, A.AREA_ID, A.IND_NAME, A.IND_ID, A.NUM1, A.NUM2, A.NUM3,A.NUM4,A.NUM5, A.NUM4+A.NUM5 as NUM6, ORDER_BY"
                       +" from (SELECT b.zone_id, level zone_level"
                       +" FROM META_DIM_ZONE B"
                       +" START WITH B.zone_id in (" + zoneId + ")"
                       +" CONNECT BY PRIOR B.ZONE_ID = B.ZONE_PAR_ID) E,"
                       +" customerCore_Mon A LEFT JOIN META_DIM_ZONE B ON A.AREA_ID = B.ZONE_ID"
                       +" WHERE  E.ZONE_ID = A.AREA_ID AND A.month_id=" + dateTime 
                       +" order by to_number(ind_id) asc";*/
	      
          String sql="select a.BUSI_STEP_ID,a.BUSI_STEP_NAME,a.BUSI_STEP_WEIGHT,a.STEP_OFFSET,a.CMPL_OFFSET,a.TOTAL_OFFSET,a.STEP_SCORE,a.REAL_SCORE,a.SORT_NUM"
              +" from CS_BUSI_STEP_DETAIL a, META_DIM_ZONE b"
              +" where a.region_id = b.zone_code"
              +" and b.zone_id in (" + zoneId + ")"
              +" and a.month_id="+dateTime
              +" order by to_number(a.BUSI_STEP_ID) asc";
           List<Map<String,Object>> list=getDataAccess().queryForList(sql);
           map.put("list", list);
           StringBuffer  chartMap=new StringBuffer();
           chartMap.append("<chart hovercapbg='FFECAA' hovercapborder='F47E00' caption='环节偏离量'  decimals='2' bgColor='FFFFFF' radarFillColor='FFFFFF' plotFillAlpha='5'  plotBorderThickness='2'" 
                           +" anchorAlpha='100'  numberSuffix='%' numDivLines='2' legendPosition='RIGHT' exportEnabled='1' exportFormats='PDF=输出为PDF|PNG=输出为PNG|JPEG=输出为JPEG'  exportFileName='报表图形'  exportAtClient='0'  exportAction='download' exportHandler='http://132.121.165.45:8081/tydic_generateReportsV1/FCExporterDirectPic.jsp'> \n");
		
           StringBuffer  temp1=new StringBuffer();
           temp1.append("<categories font='Arial' fontSize='11'> \n");
           StringBuffer  temp2=new StringBuffer();
           temp2.append("<dataset seriesname='环节权重'  color='FF83FA'  anchorSides='6'   anchorBorderColor='0099FF' anchorBgAlpha='0'  anchorRadius='2'>\n"); 
           
           StringBuffer  temp3=new StringBuffer();
           temp3.append("<dataset seriesname='环节得分'  color='8BBA00'  anchorSides='10'  anchorBorderColor='CD6AC0' anchorBgAlpha='0'  anchorRadius='2'>\n");
           
         /**temp3.append("<dataset seriesname='总偏离量'   color='AFD8F8' anchorSides='10' anchorBorderColor='CD6AC0' anchorBgAlpha='0'  anchorRadius='2'>\n");
           StringBuffer  temp4=new StringBuffer();
           temp4.append("<dataset seriesname='环节偏离量' color='F6BD0F' anchorSides='12' anchorBorderColor='0099FF' anchorBgAlpha='0'  anchorRadius='2'>\n");
           StringBuffer  temp5=new StringBuffer();
           temp5.append("<dataset seriesname='投诉偏离量' color='8BBA00' anchorSides='14' anchorBorderColor='CD6AC0' anchorBgAlpha='0'  anchorRadius='2'>\n");
          **/
           for (Map<String, Object> key : list){
        	   temp1.append("<category label='"+key.get("BUSI_STEP_NAME")+"'/>\n");
        	   temp2.append("<set value='"+key.get("BUSI_STEP_WEIGHT")+"' />\n");
        	   temp3.append("<set value='"+key.get("STEP_SCORE")+"' />\n");
        	   //temp4.append("<set value='"+key.get("STEP_OFFSET")+"' />\n");
        	   //temp5.append("<set value='"+key.get("CMPL_OFFSET")+"' />\n");
        	  
           }
           temp1.append("</categories>\n");
           
           temp2.append("</dataset>\n");
           temp3.append("</dataset>\n");
           //temp4.append("</dataset>\n");
           //temp5.append("</dataset>\n");           
           
           StringBuffer  temp6=new StringBuffer(); 
              temp6.append("<styles>\n")
                  .append("<definition>\n") 
                  .append("<style name='myCaptionFont' type='font' font='Arial' size='14' color='666666' bold='1' underline='1'/> \n")
                  .append("</definition>\n")
                  .append("<application> \n")
                  .append("<apply toObject='Caption' styles='myCaptionFont'/> \n")
                  .append("</application> \n")
       	          .append("</styles>\n");
           chartMap.append(temp1).append(temp2).append(temp3).append(temp6);
		   chartMap.append("</chart>");
		   System.out.println(chartMap.toString());
		   map.put("chartMap", chartMap.toString());
	       return map;
	}
	
	/***
	 *  月份
	 * @param queryData
	 * @return
	 */
	public Map<String, Object> getMonHjCoreData(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
	    String dataDate = Convert.toString(queryData.get("dateTime")).replace("-", "");
	    String zoneId = Convert.toString(queryData.get("zoneId"));
	    String indId = Convert.toString(queryData.get("indId"));
	    String indName="";
	    if(!indId.equals("")){indName=CodeManager.getName("HUGJHJ_TYPE", indId);}else{indName="所有环节";}
		StringBuffer  chartMap=new StringBuffer();
		
		
    	String currentColor= "FF0000";
    	String lastColor=    "0066FF";
    	int numVdivlines =0;
    	StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
      	
	     String showCurrentYEAR = dataDate.substring(0, dataDate.length() - 2);// 当年
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
	    		 
	    		  Map<String, Object> currentObj = getMonByHjCoreValue(String.valueOf(i), zoneId, indId);
	    		  
	    		  Map<String, Object> lastObj =    getMonByHjCoreValue(lastYear, zoneId, indId);
	    		  
	    	      if (null != currentObj) {
	    	    	  currentValue = String.valueOf(currentObj.get("REAL_SCORE") == null ? "0": currentObj.get("REAL_SCORE"));
	    		   }
	    	      if (null != lastObj) {
	    	    	  lastValue =    String.valueOf(lastObj.get("REAL_SCORE") == null ? "0": lastObj.get("REAL_SCORE"));
	    		   }
	    	      
	    	      temp1.append("<category name='"+showDay+"月份' />\n");
		          temp2.append("<set value='"+currentValue+"' hoverText='"+showCurrentYEAR+"： "+currentValue+"' />\n");
		    	  temp3.append("<set value='"+lastValue+"'    hoverText='"+showLastYEAR+"： "+lastValue+"' />\n");  
		    	  numVdivlines++;
	    	 }
	         numVdivlines -=2;
	         temp1.append("</categories>\n");
	         temp2.append("</dataset>\n");
	         temp3.append("</dataset>\n");
		
	         chartMap.append("<chart caption='"+indName+"月份趋势' yAxisName='100分制'  labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
	     	        " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
	     	        " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
	     	        " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
	     	        " numdivlines='4' adjustDiv='0' numVdivlines='"+numVdivlines+"'   rotateNames='0'"+
	     	        " chartRightMargin='20' chartLeftMargin='10'>\n"); 
	         
	       chartMap.append(temp1).append(temp2).append(temp3);
	 	   chartMap.append("</chart>");
		map.put("chartMap", chartMap.toString());
		return map;
	}
	
	
/**
 *  区域
  * @param: 
  * @return: 
  * @version: 1.0
  * @author: yanhaidong
  * @version: 2013-4-15 上午10:48:53
 */
 public Map<String, Object> getAreaHjCoreData(Map<String, Object> queryData) {
		Map<String, Object> map=new HashMap<String,Object>();
	    String dataDate = Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
	    String zoneId = Convert.toString(queryData.get("zoneId"));
	    String indId = Convert.toString(queryData.get("indId"));
	    String indName="";
	    if(!indId.equals("")){indName=CodeManager.getName("HUGJHJ_TYPE", indId);}else{indName="所有环节";}
		
	    StringBuffer  chartMap=new StringBuffer();
    	String currentColor= "FBC62A";
    	String lastColor=    "71B359";
    	StringBuffer  temp1=new StringBuffer();
    	temp1.append("<categories>\n");
      	StringBuffer  temp2=new StringBuffer();
      	StringBuffer  temp3=new StringBuffer();
      	
		 String year      = String.valueOf(dataDate).substring(0, 4);
	     String month_no  = String.valueOf(dataDate).substring(4, 6);
		 String showCurrentYEAR = String.valueOf(dataDate);// 当月
		 String showLastYEAR = getLastMon(showCurrentYEAR);
		 
   	     temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
         temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
        
		List<Map<String, Object>> list  =  getAreaValueByMonth(showCurrentYEAR,zoneId, indId);
		
       for (Map<String, Object> key : list){
        	  String curValue=String.valueOf(key.get("REAL_SCORE") == null ? "0": key.get("REAL_SCORE"));
    		  String lastValue="0";
    	      Map<String, Object> lastObj =getAreaObjectByMonth(showLastYEAR, indId,(String)key.get("ZONE_NAME"));
    	      if (null != lastObj) {
    	    	  lastValue = String.valueOf(lastObj.get("REAL_SCORE") == null ? "0": lastObj.get("REAL_SCORE"));
    		   }
         	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
      	    temp2.append("<set      value='"+curValue+"' />\n");
      	    temp3.append("<set      value='"+lastValue+"' />\n");
        }  
       
        temp1.append("</categories>\n");
        temp2.append("</dataset>\n");
        temp3.append("</dataset>\n");
        chartMap.append("<chart  caption='"+indName+"区域分布' yAxisName='100分制'  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='NONE'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
              " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
              " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
              " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
              " numdivlines='4' adjustDiv='0' rotateNames='0'"+
              " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");
        
		chartMap.append(temp1).append(temp2).append(temp3);
	    chartMap.append("</chart>");
		map.put("chartMap", chartMap.toString());
		return map;	
 }
 
 
 
 /**
  *  21区域
   * @param: 
   * @return: 
   * @version: 1.0
   * @author: yanhaidong
   * @version: 2013-4-15 上午10:48:53
  */
  public Map<String, Object> get21AreaHjCoreData(Map<String, Object> queryData) {
	    Map<String, Object> map=new HashMap<String,Object>();
 	    String dataDate = Convert.toString(queryData.get("dateTime")).replaceAll("-", "");
 	    String zoneId = Convert.toString(queryData.get("zoneId"));
 	    String indId = Convert.toString(queryData.get("indId"));
 	    String indName="";
 	    if(!indId.equals("")){indName=CodeManager.getName("HUGJHJ_TYPE", indId);}else{indName="所有环节";}
 		
 	    StringBuffer  chartMap=new StringBuffer();
     	String currentColor= "FBC62A";
     	String lastColor=    "71B359";
     	StringBuffer  temp1=new StringBuffer();
     	temp1.append("<categories>\n");
       	StringBuffer  temp2=new StringBuffer();
       	StringBuffer  temp3=new StringBuffer();
       	
 		 String year      = String.valueOf(dataDate).substring(0, 4);
 	     String month_no  = String.valueOf(dataDate).substring(4, 6);
 		 String showCurrentYEAR = String.valueOf(dataDate);// 当月
 		 String showLastYEAR = getLastMon(showCurrentYEAR);
 		 
    	     temp2.append("<dataset  seriesName='"+showCurrentYEAR+"' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
          temp3.append("<dataset  seriesName='"+showLastYEAR+"'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
         
 		List<Map<String, Object>> list  =  get21AreaValueByMonth(showCurrentYEAR,indId);
 		
        for (Map<String, Object> key : list){
         	  String curValue=String.valueOf(key.get("REAL_SCORE") == null ? "0": key.get("REAL_SCORE"));
     		  String lastValue="0";
     	      Map<String, Object> lastObj =getAreaObjectByMonth(showLastYEAR, indId,(String)key.get("ZONE_NAME"));
     	      if (null != lastObj) {
     	    	  lastValue = String.valueOf(lastObj.get("REAL_SCORE") == null ? "0": lastObj.get("REAL_SCORE"));
     		   }
          	temp1.append("<category label='"+key.get("ZONE_NAME")+"'/>\n");
       	    temp2.append("<set      value='"+curValue+"' />\n");
       	    temp3.append("<set      value='"+lastValue+"' />\n");
         }  
        
         temp1.append("</categories>\n");
         temp2.append("</dataset>\n");
         temp3.append("</dataset>\n");
         chartMap.append("<chart  caption='"+indName+"区域分布' yAxisName='100分制'  plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' labelDisplay='WRAP'  alternateHGridAlpha='20' formatNumber='1'   lineThickness='1' canvasBorderThickness='0' showBorder='0'"+
               " baseFontSize='12' caption='' subcaption='' bgcolor='FFFFFF' alternateHGridColor='FFFFFF' divLineAlpha='30' " +
               " hovercapbg='FFECAA' hovercapborder='F47E00' formatNumber='0' formatNumberScale='0' showToolTip='1'" +
               " showZeroPlane='0' decimalPrecision='2' showhovercap='0' showValues='0' rotateValues='1' valuePosition='auto'"+
               " numdivlines='4' adjustDiv='0' rotateNames='0'"+
               " chartRightMargin='20' chartLeftMargin='10' useRoundEdges='1'>\n");
         
 		chartMap.append(temp1).append(temp2).append(temp3);
 	    chartMap.append("</chart>");
 		map.put("chartMap", chartMap.toString());
 		return map;	
  } 
 
 
 
 
  private Map<String, Object> getMonByHjCoreValue(String monthId, String zoneId , String indId) {
	  String sql="";
	  if(!indId.equals("")){
	       sql="select  round(a.STEP_SCORE*100,2) STEP_SCORE,REAL_SCORE"
	            +" from CS_BUSI_STEP_DETAIL a, META_DIM_ZONE b"
	           +" where a.region_id = b.zone_code"
	             +" and a.month_id = "+monthId
	             +" and b.zone_id = '"+zoneId+"'"
	             +" and a.BUSI_STEP_ID='"+indId+"'";
	  }else{
	       sql="select  round(SUM(STEP_SCORE)*100,2) REAL_SCORE"
	            +" from CS_BUSI_STEP_DETAIL a, META_DIM_ZONE b"
	           +" where a.region_id = b.zone_code"
	             +" and a.month_id = "+monthId
	             +" and b.zone_id = '"+zoneId+"'";
	  }
	
      return getDataAccess().queryForMap(sql);
}

 private List<Map<String, Object>> getAreaValueByMonth(String monthId,String zoneId, String indId) {
		String sql = "";
		if (!indId.equals("")) {
	         sql="SELECT b.zone_name,  REAL_SCORE"
		         +" FROM CS_BUSI_STEP_DETAIL a"
		         +" LEFT JOIN META_DIM_ZONE b"
		           +" on a.region_id = b.zone_code"
		        +" where a.month_id = "+monthId
		          +" and b.zone_par_id = '"+zoneId+"'"
		          +" and a.BUSI_STEP_ID='"+indId+"'"
		          +" ORDER BY b.dim_level, b.ORDER_ID";
		} else {
	         sql="SELECT b.zone_name,  round(SUM(STEP_SCORE)*100,2) REAL_SCORE"
		         +" FROM CS_BUSI_STEP_DETAIL a"
		         +" LEFT JOIN META_DIM_ZONE b"
		           +" on a.region_id = b.zone_code"
		        +" where a.month_id = "+monthId
		          +" and b.zone_par_id = '"+zoneId+"'"
		          +" group by b.zone_name,b.dim_level, b.ORDER_ID"
		          +" ORDER BY b.dim_level, b.ORDER_ID";
		}
		return getDataAccess().queryForList(sql);
	}
 
 private List<Map<String, Object>> get21AreaValueByMonth(String monthId,String indId) {
		String sql = "";
		if (!indId.equals("")) {
	         sql="SELECT b.zone_name,  REAL_SCORE"
		         +" FROM CS_BUSI_STEP_DETAIL a"
		         +" LEFT JOIN META_DIM_ZONE b"
		           +" on a.region_id = b.zone_code"
		        +" where a.month_id = "+monthId
		          +" and b.dim_level = '3'"
		          +" and a.BUSI_STEP_ID='"+indId+"'"
		          +" ORDER BY b.dim_level, b.ORDER_ID";
		} else {
	         sql="SELECT b.zone_name,  round(SUM(STEP_SCORE)*100,2) REAL_SCORE"
		         +" FROM CS_BUSI_STEP_DETAIL a"
		         +" LEFT JOIN META_DIM_ZONE b"
		           +" on a.region_id = b.zone_code"
		        +" where a.month_id = "+monthId
		          +" and b.dim_level = '3'"
		          +" group by b.zone_name,b.dim_level, b.ORDER_ID"
		          +" ORDER BY b.dim_level, b.ORDER_ID";
		}
		return getDataAccess().queryForList(sql);
}
 
 
 
 private Map<String, Object>  getAreaObjectByMonth(String monthId, String indId, String zoneName) {
		String sql = "";
		if (!indId.equals("")) {
	         sql="SELECT b.zone_name,  REAL_SCORE"
		         +" FROM CS_BUSI_STEP_DETAIL a"
		         +" LEFT JOIN META_DIM_ZONE b"
		           +" on a.region_id = b.zone_code"
		        +" where a.month_id = "+monthId
		          //+" and b.zone_par_id = '"+zoneId+"'"
		          +" and a.BUSI_STEP_ID='"+indId+"'"
		          +" and b.zone_name='"+zoneName+"'";
		} else {
	         sql="SELECT b.zone_name, round(SUM(STEP_SCORE)*100,2) REAL_SCORE"
		         +" FROM CS_BUSI_STEP_DETAIL a"
		         +" LEFT JOIN META_DIM_ZONE b"
		           +" on a.region_id = b.zone_code"
		        +" where a.month_id = "+monthId
		         // +" and b.zone_par_id = '"+zoneId+"'"
		          +" and b.zone_name='"+zoneName+"'"
		          +" group by b.zone_name";
		}
		return getDataAccess().queryForMap(sql);
	}

/**
 * 方法描述：
 * @param: 
 * @return: 
 * @version: 1.0
 * @author: Administrator
 * @version: 2013-4-2 下午04:52:36
 */
	public String getNewMonth() {
		String sql = "SELECT TO_DATE(MAX(t.MONTH_ID),'YYYY-MM') MONTH_ID FROM CS_BUSI_STEP_DETAIL t";
		return getDataAccess().queryForString(sql);
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
