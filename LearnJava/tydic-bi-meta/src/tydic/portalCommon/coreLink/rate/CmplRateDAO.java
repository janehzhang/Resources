package tydic.portalCommon.coreLink.rate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;
import tydic.portalCommon.coreLink.util.Constants;

public class CmplRateDAO extends MetaBaseDAO {
	
   public List<Map<String, Object>> getListMapData(Map<String, Object> queryData){
	     
	      String dateTime = Convert.toString(queryData.get("dateTime"));
	      String zoneId = Convert.toString(queryData.get("zoneId"));
	      String sql="select a.MONTH_ID,a.BUSI_STEP_ID,a.BUSI_STEP_NAME,round(a.BUSI_STEP_WEIGHT*100,2) BUSI_STEP_WEIGHT,a.SUBS_NUM,a.CMPL_NUM,round(a.CMPL_RATE*100,4) CMPL_RATE,round(a.CMPL_RATE_AVG*100,4) CMPL_RATE_AVG,round(a.OFFSET*100,2) OFFSET,round(a.SCORE*100,2) SCORE"
					  +" from CS_BUSI_STEP_CMPL_RATE a, META_DIM_ZONE b"
					  +" where a.region_id = b.zone_code"
					  +" and a.month_id="+dateTime
					  +" and b.zone_id in("+zoneId+")"
					  +" order by to_number(a.BUSI_STEP_ID) asc";
	      
	     return getDataAccess().queryForList(sql);
	  }
	//投诉率对比
	public Map<String, Object> getCompareRate(Map<String, Object> queryData) {
		  Map<String, Object> map=new HashMap<String,Object>();
           List<Map<String,Object>> list=getListMapData(queryData);
           map.put("list", list);
           StringBuffer  chartMap=new StringBuffer();
           chartMap.append("<chart caption='投诉率对比' showLabels='1' showvalues='1'  placeValuesInside='1' rotateValues='1' labelDisplay='ROTATE' slantLabels='1' exportEnabled='1' exportFormats='PDF=输出为PDF|PNG=输出为PNG|JPEG=输出为JPEG'  exportFileName='报表图形'  exportAtClient='0'  exportAction='download' exportHandler='http://132.121.165.45:8081/tydic_generateReportsV1/FCExporterDirectPic.jsp'>\n");
           StringBuffer  temp1=new StringBuffer();
           temp1.append("<categories>\n");
           StringBuffer  temp2=new StringBuffer();
           temp2.append("<dataset seriesName='投诉率'       color='6A5ACD'>\n");
           StringBuffer  temp3=new StringBuffer();
           temp3.append("<dataset seriesName='投诉率平均值'  color='CD5C5C'>\n");
           for (Map<String, Object> key : list){
   	          String alarmValue="0";
	          String currentValue="0";
	          if (null != key) {
					 alarmValue=String.valueOf(key.get("CMPL_RATE")==null?"0":key.get("CMPL_RATE"));
		    		 currentValue=String.valueOf(key.get("CMPL_RATE_AVG")==null?"0":key.get("CMPL_RATE_AVG"));
			   }
        	   temp1.append("<category label='"+key.get("BUSI_STEP_NAME")+"'/>\n");
			   temp2.append("<set value='"+ alarmValue +"' />\n");
			   temp3.append("<set value='"+ currentValue +"' />\n");
           }
           temp1.append("</categories>\n");
           temp2.append("</dataset>\n");
           temp3.append("</dataset>\n");
           chartMap.append(temp1).append(temp2).append(temp3);
		   chartMap.append("</chart>");
		  //System.out.println("XML := "+chartMap.toString());
		   map.put("chartMap", chartMap.toString());
	       return map;
	}
    //投诉率偏离值
	public Map<String, Object> getPianLiRate(Map<String, Object> queryData) {
		   Map<String, Object> map=new HashMap<String,Object>();
		   List<Map<String,Object>> list=getListMapData(queryData);
           map.put("list", list);
           
           StringBuffer  chartMap=new StringBuffer();
           chartMap.append("<chart caption='投诉率偏离值'  decimals='2' bgColor='FFFFFF' radarFillColor='FFFFFF' plotFillAlpha='5'  plotBorderThickness='2'" 
                           +" anchorAlpha='100'  numberSuffix='%' numDivLines='2' legendPosition='RIGHT' exportEnabled='1' exportFormats='PDF=输出为PDF|PNG=输出为PNG|JPEG=输出为JPEG'  exportFileName='报表图形'  exportAtClient='0'  exportAction='download' exportHandler='http://132.121.165.45:8081/tydic_generateReportsV1/FCExporterDirectPic.jsp'> \n");
		
           StringBuffer  temp1=new StringBuffer();
           temp1.append("<categories font='Arial' fontSize='11'> \n");
           
           StringBuffer  temp2=new StringBuffer();
           temp2.append("<dataset seriesname='环节权重'   color='F6BD0F'   anchorSides='6'   anchorBorderColor='0099FF'   anchorBgAlpha='0'   anchorRadius='2'>\n");	
           StringBuffer  temp3=new StringBuffer();
           temp3.append("<dataset seriesname='偏离度'     color='FF83FA'   anchorSides='10'  anchorBorderColor='0099FF'   anchorBgAlpha='0'   anchorRadius='2'>\n"); 
        
           for (Map<String, Object> key : list){
        	  String weightValue="0";
        	  String offsetValue="0";
 	          if (null != key) {
 	        	   weightValue=String.valueOf(key.get("BUSI_STEP_WEIGHT")==null?"0":key.get("BUSI_STEP_WEIGHT"));
 	        	   offsetValue=String.valueOf(key.get("OFFSET")==null?"0":key.get("OFFSET"));
 	          }
        	   temp1.append("<category label='"+key.get("BUSI_STEP_NAME")+"'/>\n");
        	   temp2.append("<set value='"+weightValue+"' />\n");
        	   temp3.append("<set value='"+offsetValue+"' />\n");
           }
           temp1.append("</categories>\n");
           temp2.append("</dataset>\n");
           temp3.append("</dataset>\n");
           
           StringBuffer  temp4=new StringBuffer(); 
             temp4.append("<styles>\n")
                  .append("<definition>\n") 
                  .append("<style name='myCaptionFont' type='font' font='Arial' size='14' color='666666' bold='1' underline='1'/> \n")
                  .append("</definition>\n")
                  .append("<application> \n")
                  .append("<apply toObject='Caption' styles='myCaptionFont'/> \n")
                  .append("</application> \n")
       	          .append("</styles>\n");
           chartMap.append(temp1).append(temp2).append(temp3).append(temp4);
		   chartMap.append("</chart>");
		   //System.out.println(chartMap.toString());
		   map.put("chartMap", chartMap.toString());
	       return map;
	}
	
    //月份投诉率
	public Map<String, Object> getMonthRate(Map<String, Object> queryData) {
		  Map<String, Object> map=new HashMap<String,Object>();
	      String dateTime = Convert.toString(queryData.get("dateTime"));
	      String zoneId = Convert.toString(queryData.get("zoneId"));
	      
	      String indId = Convert.toString(queryData.get("indId"));
	      String indName = Convert.toString(queryData.get("indName"));
	      
	      indId="".equals(indId)?Constants.BUSI_STEP1_ID:indId;
	      indName="".equals(indName)?Constants.BUSI_STEP1_NAME:indName;
	      
	      
	      String tempDateTime=dateTime.substring(0,dateTime.length()-2)+"01";//201201
		    
	       StringBuffer  chartMap=new StringBuffer();
	       chartMap.append("<chart canvasPadding='10' caption='"+indName+"月份指标值'  bgColor='F7F7F7,E9E9E9'  numberSuffix='%' numVDivLines='10' divLineAlpha='30'  labelPadding ='10' yAxisValuesPadding ='10' showValues='0' rotateValues='1' valuePosition='auto' exportEnabled='1' exportFormats='PDF=输出为PDF|PNG=输出为PNG|JPEG=输出为JPEG'  exportFileName='报表图形'  exportAtClient='0'  exportAction='download' exportHandler='http://132.121.165.45:8081/tydic_generateReportsV1/FCExporterDirectPic.jsp'> \n"); 
	      
	       StringBuffer  temp1=new StringBuffer();
	       temp1.append("<categories>\n");
	       StringBuffer  temp2=new StringBuffer();
	       temp2.append("<dataset seriesName='投诉率' color='A66EDD'>\n");
	       StringBuffer  temp3=new StringBuffer();
	       temp3.append("<dataset seriesName='投诉率均值' color='F6BD0F'>\n");
	       
	     for (int i=Integer.parseInt(tempDateTime);i<=Integer.parseInt(dateTime);i++){
	          String alarmValue="0";
	          String currentValue="0";
	    	 Map<String, Object> objValue=getMonByHjCoreValue(i,zoneId,indId) ;
	    	  if(null != objValue){
	    		  alarmValue=String.valueOf(objValue.get("CMPL_RATE")==null?"0":objValue.get("CMPL_RATE"));
	    		  currentValue=String.valueOf(objValue.get("CMPL_RATE_AVG")==null?"0":objValue.get("CMPL_RATE_AVG"));
			  }
	      	   //System.out.println("月份开始: "+i+" 数据:"+ value);
	    	 temp1.append("<category label='"+i+"' /> \n"); 
	    	 temp2.append("<set value='"+alarmValue+"' /> \n"); 
	    	 temp3.append("<set value='"+currentValue+"' /> \n"); 
	    	  
	      }
	     temp1.append("</categories>\n");
        temp2.append("</dataset>\n");
        temp3.append("</dataset>\n");
        
         chartMap.append(temp1).append(temp2).append(temp3);
	     chartMap.append("</chart>"); 
	     System.out.println(chartMap.toString());
		 map.put("chartMap", chartMap.toString());
		return map;
	}
    //区域投诉率
	public Map<String, Object> getAreaRate(Map<String, Object> queryData) {
		  Map<String, Object> map = new HashMap<String, Object>();
	      String dateTime = Convert.toString(queryData.get("dateTime"));
	      String zoneId = Convert.toString(queryData.get("zoneId"));
	      
	      String indId = Convert.toString(queryData.get("indId"));
	      String indName = Convert.toString(queryData.get("indName"));
	      
	      indId="".equals(indId)?Constants.BUSI_STEP1_ID:indId;
	      indName="".equals(indName)?Constants.BUSI_STEP1_NAME:indName;
		
	  	StringBuffer chartMap = new StringBuffer();
		String[] areaList = new String[] { "深圳", "广州", "东莞", "佛山", "中山", "惠州",
				"江门", "珠海", "汕头", "揭阳", "潮州", "汕尾", "湛江", "茂名", "阳江", "云浮",
				"肇庆", "清远", "梅州", "河源", "韶关" };
		
		chartMap.append("<chart caption='"+indName+"区域分布' numberSuffix='%'  showLabels='1' showvalues='0'  placeValuesInside='1' rotateValues='1' exportEnabled='1' exportFormats='PDF=输出为PDF|PNG=输出为PNG|JPEG=输出为JPEG'  exportFileName='报表图形'  exportAtClient='0'  exportAction='download' exportHandler='http://132.121.165.45:8081/tydic_generateReportsV1/FCExporterDirectPic.jsp'>\n");
		StringBuffer temp1 = new StringBuffer();
		temp1.append("<categories>\n");
		StringBuffer temp2 = new StringBuffer();
		temp2.append("<dataset  seriesName='投诉率'  color='6A5ACD' >\n");
		StringBuffer temp3 = new StringBuffer();
		temp3.append("<dataset  seriesName='投诉率均值' color='CD5C5C'>\n");

		for (int i = 0; i < areaList.length; i++) {
	        String alarmValue="0";
	        String currentValue="0";
			Map<String, Object> objValue = getAreaValueByMonth(dateTime,zoneId, areaList[i], indId);
			if (null != objValue) {
				 alarmValue=String.valueOf(objValue.get("CMPL_RATE")==null?"0":objValue.get("CMPL_RATE"));
	    		 currentValue=String.valueOf(objValue.get("CMPL_RATE_AVG")==null?"0":objValue.get("CMPL_RATE_AVG"));
			}
			temp1.append("<category label='"+ areaList[i] +"' />\n");
			temp2.append("<set value='"+ alarmValue +"' />\n");
			temp3.append("<set value='"+ currentValue +"' />\n");
		}
		temp1.append("</categories>\n");
		temp2.append("</dataset>\n");
		temp3.append("</dataset>\n");
		
		chartMap.append(temp1).append(temp2).append(temp3);
		chartMap.append("</chart>\n");
		//System.out.println(chartMap.toString());
		map.put("chartMap", chartMap.toString());
		return map;
		
	}
	
  private Map<String, Object> getMonByHjCoreValue( int monthId,  String zoneId, String indId) {
		   String sql ="select a.MONTH_ID,a.BUSI_STEP_ID,a.BUSI_STEP_NAME,a.BUSI_STEP_WEIGHT,a.SUBS_NUM,a.CMPL_NUM, round(a.CMPL_RATE*100,4) CMPL_RATE, round(a.CMPL_RATE_AVG*100,4) CMPL_RATE_AVG, a.OFFSET,a.SCORE"
	           +" from CS_BUSI_STEP_CMPL_RATE  a, META_DIM_ZONE b"
	           +" where a.region_id = b.zone_code and a.month_id = ? and  a.BUSI_STEP_ID = ? and b.zone_id in (?)"
	           +" order by to_number(a.BUSI_STEP_ID) asc ";
	     
	       Object[] p = new Object[3];
	  	    p[0] =monthId;
			p[1] =indId;
			p[2] =zoneId;
		   return getDataAccess().queryForMap(sql,p);
	}
   
  private Map<String, Object> getAreaValueByMonth(String dateTime, String zoneId,String zoneName,String indId) {
	 String sql ="select a.MONTH_ID,a.BUSI_STEP_ID,a.BUSI_STEP_NAME,a.BUSI_STEP_WEIGHT,a.SUBS_NUM,a.CMPL_NUM,round(a.CMPL_RATE*100,4) CMPL_RATE, round(a.CMPL_RATE_AVG*100,4) CMPL_RATE_AVG,a.OFFSET,a.SCORE from("
			   +" SELECT b.zone_id, b.ZONE_CODE ,level zone_level"
			   +" FROM META_DIM_ZONE B"
			   +" START WITH B.zone_id in (?)"
			   +" CONNECT BY PRIOR B.ZONE_ID = B.ZONE_PAR_ID) E,"
			   +" CS_BUSI_STEP_CMPL_RATE a ,META_DIM_ZONE c"
			   +" where  a.region_id = c.zone_code and E.zone_code= a.region_id  AND c.Zone_Name=? and" 
			   +" a.month_id = ? and  a.BUSI_STEP_ID = ?";
		Object[] p = new Object[4];
		p[0] =zoneId;
		p[1] =zoneName;
		p[2] =dateTime;
		p[3] =indId;
		
	   return getDataAccess().queryForMap(sql,p);
	}
	
  
}
