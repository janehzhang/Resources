package tydic.portalCommon.coreLink.coreCommon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;
import tydic.portalCommon.coreLink.util.Constants;

public class BusiStepGeneralDAO extends MetaBaseDAO {	
	public List<Map<String, Object>> getBusiMapData(Map<String, Object> queryData){
		   String sql="select a.BUSI_STEP1_NAME," 
	           +" a.BUSI_STEP2_NAME, "
	           +" a.BUSI_STEP3_NAME,"
	           +" to_char(a.BUSI_STEP_WEIGHT * 100,'90.0000')||'％' BUSI_STEP_WEIGHT, "
	           +" a.BUSI_STEP_DIRECTION,"
	           +" case"
	           +" when a.valuetype_flag = 1 then"
	           +" to_char(round(a.ALARM_VALUE * 1000, 5), '990.99999')||'‰'"
	           +" when a.valuetype_flag = 2 then"
	           +" to_char(round(a.ALARM_VALUE * 100, 2), '990.99')||'％' "
	           +" when a.valuetype_flag = 3 then"
	           +"  to_char(round(a.ALARM_VALUE, 4), '990.9999')"
	           +" when a.valuetype_flag = 4 then"
	           +"  to_char(round(a.ALARM_VALUE, 2), '990.99')"
	           +" else"
	           +" to_char(a.ALARM_VALUE, '990.9999')"
	           +" end ALARM_VALUE,"
	           +" case"
	           +" when a.valuetype_flag = 1 then"
	           +"   to_char(round(a.CURRENT_VALUE * 1000, 5), '990.99999')||'‰'"
	           +"  when a.valuetype_flag = 2 then"
	           +"  to_char(round(a.CURRENT_VALUE * 100, 2), '990.99')||'％' "
	           +"  when a.valuetype_flag = 3 then"
	           +"  to_char(round(a.CURRENT_VALUE, 4), '990.9999')"
	           +" when a.valuetype_flag = 4 then"
	           +"  to_char(round(a.CURRENT_VALUE, 2), '990.99')"
	           +" else"
	           +"   to_char(CURRENT_VALUE, '990.9999')"
	           +" end CURRENT_VALUE, "
	           +" to_char(round(a.OFFSET * 100, 2),'990.99')||'％'  OFFSET,"
	           +" to_char(round(a.SCORE * 100, 6),'990.99999')||'％'  SCORE, "
	           +" to_char(round(nvl(CMPL_PROPORTION, 0) * 100, 4),'90.0000')||'％'  CMPL_PROPORTION"
	           +" from CS_BUSI_STEP_GENERAL a, META_DIM_ZONE b"
	           +" where a.region_id = b.zone_code and a.month_id = ? and  a.busi_step1_id = ? and b.zone_id in (?)"
	           +" order by a.busi_step2_id,a.busi_step3_id  asc ";
		       Object[] p = new Object[3];
		     	p[0] = Convert.toString(queryData.get("dateTime")).replace("-", "");
				p[1] = queryData.get("indId");
				p[2] = queryData.get("zoneId");	     
        return  getDataAccess().queryForList(sql,p);
	  }	 
  //指标偏离值
  public Map<String, Object> getIndPlValue(Map<String, Object> queryData) {
	   Map<String, Object> map = new HashMap<String, Object>();
	   String sql="select a.valuetype_flag,a.BUSI_STEP1_ID,a.BUSI_STEP1_NAME,a.BUSI_STEP2_ID,a.BUSI_STEP2_NAME,a.BUSI_STEP3_ID,a.BUSI_STEP3_NAME,to_char(BUSI_STEP_WEIGHT*100,'90.0000') BUSI_STEP_WEIGHT,a.BUSI_STEP_DIRECTION,"
           +" case when a.valuetype_flag = 1 then to_char(round(a.ALARM_VALUE * 1000, 5),'990.99999') " 
           +" when a.valuetype_flag = 2 then to_char(round(a.ALARM_VALUE * 100, 2),'990.99') " 
           +" when a.valuetype_flag = 3 then to_char(round(a.ALARM_VALUE, 4),'990.9999') " 
           +"when a.valuetype_flag = 4 then to_char(round(a.ALARM_VALUE, 2),'990.99') else to_char(ALARM_VALUE, '990.9999') end  ALARM_VALUE,"     
           +" case when a.valuetype_flag = 1 then to_char(round(a.CURRENT_VALUE * 1000, 5),'990.99999') " 
           +" when a.valuetype_flag = 2 then to_char(round(a.CURRENT_VALUE * 100, 2),'990.99') " 
           +" when a.valuetype_flag = 3 then to_char(round(a.CURRENT_VALUE, 4),'990.9999') " 
           +"when a.valuetype_flag = 4 then to_char(round(a.CURRENT_VALUE, 2),'990.99') else to_char(CURRENT_VALUE,'990.9999') end  CURRENT_VALUE,"
           +"a.CV_TOP,a.CV_BOTTOM,round(a.OFFSET*100,2)OFFSET,round(a.SCORE*100,6)SCORE,a.SORT_NUM,(case when a.busi_step3_name like '%长%' then '0'"
           +" when a.busi_step3_name like '%率%' then '1' end) flag,to_char(round(nvl(CMPL_PROPORTION, 0) * 100, 4),'90.0000') CMPL_PROPORTION"
           +" from CS_BUSI_STEP_GENERAL a, META_DIM_ZONE b"
           +" where a.region_id = b.zone_code and a.month_id = ? and  a.busi_step1_id = ? and b.zone_id in (?)"
           +" order by a.busi_step2_id,a.busi_step3_id  asc ";

	       Object[] p = new Object[3];
	     	p[0] = Convert.toString(queryData.get("dateTime")).replace("-", "");
			p[1] = queryData.get("indId");
			p[2] = queryData.get("zoneId");
	  
	  List<Map<String,Object>> list=getDataAccess().queryForList(sql,p);
	  map.put("list", list);	
	
	  
	  StringBuffer  chartMap=new StringBuffer();
	  chartMap.append("<chart  hovercapbg='FFECAA' hovercapborder='F47E00' caption=\"指标偏离值\" bgColor=\"FFFFFF\" radarFillColor=\"FFFFFF\" plotFillAlpha=\"5\" plotBorderThickness=\"2\" anchorAlpha=\"100\" numberSuffix='%' numDivLines=\"2\" legendPosition=\"RIGHT\" exportEnabled='1' exportFormats='PDF=输出为PDF|PNG=输出为PNG|JPEG=输出为JPEG'  exportFileName='报表图形'  exportAtClient='0'  exportAction='download' exportHandler='http://132.121.165.45:8081/tydic_generateReportsV1/FCExporterDirectPic.jsp'>  \n");
      StringBuffer  temp1=new StringBuffer();
      temp1.append("<categories font=\"Arial\" fontSize=\"11\"> \n");
      StringBuffer  temp2=new StringBuffer();
      temp2.append("<dataset seriesname=\"基准线\" color=\"F6BD0F\" anchorSides=\"14\" anchorBorderColor=\"0099FF\" anchorBgAlpha=\"0\" anchorRadius=\"2\"> \n");	
      StringBuffer  temp3=new StringBuffer();
      temp3.append("<dataset seriesname=\"偏离度\" color=\"0099FF\"  anchorSides=\"10\" anchorBorderColor=\"0099FF\" anchorBgAlpha=\"0\" anchorRadius=\"2\"> \n");
     for (Map<String, Object> key : list) {
		temp1.append("<category label=\""+ key.get("BUSI_STEP3_NAME")+"\"/> \n");
		//temp2.append("<set value=\""+key.get("BUSI_STEP_WEIGHT")+"\"/> \n");
		temp2.append("<set value=\"0\" /> \n");
		temp3.append("<set value=\""+key.get("OFFSET")+"\"/> \n");
	 }
      temp1.append("</categories>\n");
      temp2.append("</dataset>\n");
      temp3.append("</dataset>\n");
      StringBuffer  temp4=new StringBuffer();
      temp4.append("<styles> \n");
	      temp4.append("<definition> \n");
	        temp4.append("<style name=\"myCaptionFont\" type=\"font\" font=\"Arial\" size=\"14\" color=\"666666\" bold=\"1\" /> \n");
	      temp4.append("</definition> \n");
	      temp4.append("<application> \n");
	        temp4.append("<apply toObject=\"Caption\" styles=\"myCaptionFont\"/> \n");
	      temp4.append("</application> \n");
      temp4.append("</styles> \n");
      chartMap.append(temp1).append(temp2).append(temp3).append(temp4);
	  chartMap.append("</chart>");
     // System.out.println("XML:="+chartMap.toString());
	  map.put("chartMap", chartMap.toString());
      return map;
   }
 
  
  //月份指标值
  public Map<String, Object> getMonthIndValue(Map<String, Object> queryData) {
          Map<String, Object> map=new HashMap<String,Object>();
	      String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneId = Convert.toString(queryData.get("zoneId"));
	      String indId = Convert.toString(queryData.get("indId"));
	      String step3Id = Convert.toString(queryData.get("step3Id"));
	      String step3Name = Convert.toString(queryData.get("step3Name"));
	      step3Id="".equals(step3Id)?Constants.BUSI_STEP3_ID:step3Id;
	      step3Name="".equals(step3Name)?Constants.BUSI_STEP3_NAME:step3Name;
	      String tempDateTime=dateTime.substring(0,dateTime.length()-2)+"01";//201201
	      String currentColor= "FF0000";
	      String lastColor=    "0066FF";
	      int numVdivlines =0;
	       StringBuffer  chartMap=new StringBuffer();
	       StringBuffer  temp1=new StringBuffer();
	       temp1.append("<categories>\n");
	       StringBuffer  temp2=new StringBuffer();
	       StringBuffer  temp3=new StringBuffer();
	   	   temp2.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+currentColor+"' seriesName='预警值'      color='"+currentColor+"' anchorBorderColor='FFFFFF'>\n");
	       temp3.append("<dataset  anchorSides='4' anchorRadius='5' anchorBgColor='"+lastColor+"'    seriesName='当月值'      color='"+lastColor+"'    anchorBorderColor='FFFFFF'>\n");
	       
	     for (int i=Integer.parseInt(tempDateTime);i<=Integer.parseInt(dateTime);i++){
	          String alarmValue="0";
	          String currentValue="0";
	    	 Map<String, Object> objValue=getMonByHjCoreValue(i,zoneId,indId ,step3Id) ;
	    	  if(null != objValue){
	    		  alarmValue=String.valueOf(objValue.get("ALARM_VALUE")==null?"0":objValue.get("ALARM_VALUE"));
	    		  currentValue=String.valueOf(objValue.get("CURRENT_VALUE")==null?"0":objValue.get("CURRENT_VALUE"));
			  }
	      	   //System.out.println("月份开始: "+i+" 数据:"+ value);
	    	 temp1.append("<category label='"+i+"' /> \n"); 
	    	 temp2.append("<set value='"+alarmValue+"'  hoverText='预警值： "+alarmValue+"'  /> \n"); 
	    	 temp3.append("<set value='"+currentValue+"'hoverText='当月值： "+currentValue+"'  /> \n"); 
	    	  numVdivlines++;  
	      }
	     numVdivlines -=2;
	     temp1.append("</categories>\n");
         temp2.append("</dataset>\n");
         temp3.append("</dataset>\n");
         
	    chartMap.append("<chart hovercapbg='FFECAA' hovercapborder='F47E00' canvasPadding='10' caption='"+step3Name+"月份指标值'" +
      		"   bgcolor='FFFFFF' alternateHGridColor='FFFFFF'  numVdivlines='"+numVdivlines+"' divLineAlpha='30' " +
      				" labelPadding ='10' yAxisValuesPadding ='10' showValues='0' rotateValues='1' valuePosition='auto' exportEnabled='1' exportFormats='PDF=输出为PDF|PNG=输出为PNG|JPEG=输出为JPEG'  exportFileName='报表图形'  exportAtClient='0'  exportAction='download' exportHandler='http://132.121.165.45:8081/tydic_generateReportsV1/FCExporterDirectPic.jsp'> \n"); 
     
         chartMap.append(temp1).append(temp2).append(temp3);
	     chartMap.append("</chart>"); 
	      //System.out.println(chartMap.toString());
		 map.put("chartMap", chartMap.toString());
		return map;
	}






    //区域指标值
	public Map<String, Object> getAreaIndValue(Map<String, Object> queryData) {
	      Map<String, Object> map=new HashMap<String,Object>();
	      String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneId = Convert.toString(queryData.get("zoneId"));
	      String indId = Convert.toString(queryData.get("indId"));

	      String step3Id = Convert.toString(queryData.get("step3Id"));
	      String step3Name = Convert.toString(queryData.get("step3Name"));
	      step3Id="".equals(step3Id)?Constants.BUSI_STEP3_ID:step3Id;
	      step3Name="".equals(step3Name)?Constants.BUSI_STEP3_NAME:step3Name;
	    	String currentColor= "FBC62A";
	    	String lastColor=    "71B359";
		StringBuffer chartMap = new StringBuffer();
		String[] areaList = new String[] { "深圳", "广州", "东莞", "佛山", "中山", "惠州",
				"江门", "珠海", "汕头", "揭阳", "潮州", "汕尾", "湛江", "茂名", "阳江", "云浮",
				"肇庆", "清远", "梅州", "河源", "韶关" };
		StringBuffer temp1 = new StringBuffer();
		temp1.append("<categories>\n");
		StringBuffer temp2 = new StringBuffer();
		//temp2.append("<dataset seriesName='预警值'  color='6A5ACD' >\n");
		StringBuffer temp3 = new StringBuffer();
		//temp3.append("<dataset  seriesName='当月值' color='CD5C5C'>\n");

		
	     temp2.append("<dataset  seriesName='预警值' color='"+currentColor+"' anchorBorderColor='"+currentColor+"' anchorBgColor='"+currentColor+"' >\n");
         temp3.append("<dataset  seriesName='当月值'    color='"+lastColor+"'    anchorBorderColor='"+lastColor+"'    anchorBgColor='"+lastColor+"' >\n"); 
        
		for (int i = 0; i < areaList.length; i++) {
	        String alarmValue="0";
	        String currentValue="0";
			Map<String, Object> objValue = getAreaValueByMonth(dateTime,zoneId, areaList[i], indId ,step3Id);
			if (null != objValue) {
				 alarmValue=String.valueOf(objValue.get("ALARM_VALUE")==null?"0":objValue.get("ALARM_VALUE"));
	    		 currentValue=String.valueOf(objValue.get("CURRENT_VALUE")==null?"0":objValue.get("CURRENT_VALUE"));
			}
			temp1.append("<category label='"+ areaList[i] +"' />\n");
			temp2.append("<set value='"+ alarmValue +"' />\n");
			temp3.append("<set value='"+ currentValue +"' />\n");
		}
		temp1.append("</categories>\n");
		temp2.append("</dataset>\n");
		temp3.append("</dataset>\n");
		
		
		chartMap.append("<chart hovercapbg='FFECAA' hovercapborder='F47E00' plotGradientColor='333333' plotFillRatio='80,20' plotFillAlpha='95,100' plotFillAngle='45' caption='"+step3Name+"环节得分区域分布' showLabels='1' showvalues='0' " +
				" placeValuesInside='1' bgcolor='FFFFFF' labelDisplay='WRAP'  useRoundEdges='1' alternateHGridColor='FFFFFF' rotateValues='1' exportEnabled='1' exportFormats='PDF=输出为PDF|PNG=输出为PNG|JPEG=输出为JPEG'  exportFileName='报表图形'  exportAtClient='0'  exportAction='download' exportHandler='http://132.121.165.45:8081/tydic_generateReportsV1/FCExporterDirectPic.jsp'>\n");
		

        
		chartMap.append(temp1).append(temp2).append(temp3);
		chartMap.append("</chart>\n");
		//System.out.println(chartMap.toString());
		map.put("chartMap", chartMap.toString());
		return map;
	}
	
	

	
private Map<String, Object> getMonByHjCoreValue( int monthId,  String zoneId, String indId , String  step3Id ) {
	String sql="select a.valuetype_flag,a.BUSI_STEP1_ID,a.BUSI_STEP1_NAME,a.BUSI_STEP2_ID,a.BUSI_STEP2_NAME,a.BUSI_STEP3_ID,a.BUSI_STEP3_NAME,to_char(BUSI_STEP_WEIGHT*100,'90.0000') BUSI_STEP_WEIGHT,a.BUSI_STEP_DIRECTION,"
        +" case when a.valuetype_flag = 1 then to_char(round(a.ALARM_VALUE * 1000, 5),'990.99999') " 
        +" when a.valuetype_flag = 2 then to_char(round(a.ALARM_VALUE * 100, 2),'990.99') " 
        +" when a.valuetype_flag = 3 then to_char(round(a.ALARM_VALUE, 4),'990.9999') " 
        +"when a.valuetype_flag = 4 then to_char(round(a.ALARM_VALUE, 2),'990.99') else to_char(ALARM_VALUE, '990.9999') end  ALARM_VALUE,"     
        +" case when a.valuetype_flag = 1 then to_char(round(a.CURRENT_VALUE * 1000, 5),'990.99999') " 
        +" when a.valuetype_flag = 2 then to_char(round(a.CURRENT_VALUE * 100, 2),'990.99') " 
        +" when a.valuetype_flag = 3 then to_char(round(a.CURRENT_VALUE, 4),'990.9999') " 
        +"when a.valuetype_flag = 4 then to_char(round(a.CURRENT_VALUE, 2),'990.99') else to_char(CURRENT_VALUE,'990.9999') end  CURRENT_VALUE,"
		   +"a.CV_TOP,a.CV_BOTTOM,round(a.OFFSET*100,2)OFFSET,round(a.SCORE*100,6)SCORE,a.SORT_NUM"
           +" from CS_BUSI_STEP_GENERAL a, META_DIM_ZONE b"
           +" where a.region_id = b.zone_code and a.month_id = ? and  a.busi_step1_id = ? and b.zone_id in (?) and a.BUSI_STEP3_ID=?"
           +" order by a.busi_step2_id,a.busi_step3_id  asc ";
     
       Object[] p = new Object[4];
  	    p[0] =monthId;
		p[1] =indId;
		p[2] =zoneId;
		p[3] =step3Id;
	   return getDataAccess().queryForMap(sql,p);
}
private Map<String, Object> getAreaValueByMonth(String dateTime, String zoneId,String zoneName,String indId , String  step3Id) {
	String sql="select a.valuetype_flag,a.BUSI_STEP1_ID,a.BUSI_STEP1_NAME,a.BUSI_STEP2_ID,a.BUSI_STEP2_NAME,a.BUSI_STEP3_ID,a.BUSI_STEP3_NAME,to_char(BUSI_STEP_WEIGHT*100,'90.0000') BUSI_STEP_WEIGHT,a.BUSI_STEP_DIRECTION,"
        +" case when a.valuetype_flag = 1 then to_char(round(a.ALARM_VALUE * 1000, 5),'990.99999') " 
        +" when a.valuetype_flag = 2 then to_char(round(a.ALARM_VALUE * 100, 2),'990.99') " 
        +" when a.valuetype_flag = 3 then to_char(round(a.ALARM_VALUE, 4),'990.9999') " 
        +"when a.valuetype_flag = 4 then to_char(round(a.ALARM_VALUE, 2),'990.99') else to_char(ALARM_VALUE, '990.9999') end  ALARM_VALUE,"     
        +" case when a.valuetype_flag = 1 then to_char(round(a.CURRENT_VALUE * 1000, 5),'990.99999') " 
        +" when a.valuetype_flag = 2 then to_char(round(a.CURRENT_VALUE * 100, 2),'990.99') " 
        +" when a.valuetype_flag = 3 then to_char(round(a.CURRENT_VALUE, 4),'990.9999') " 
        +"when a.valuetype_flag = 4 then to_char(round(a.CURRENT_VALUE, 2),'990.99') else to_char(CURRENT_VALUE,'990.9999') end  CURRENT_VALUE "
	       +" from("
		   +" SELECT b.zone_id, b.ZONE_CODE ,level zone_level"
		   +" FROM META_DIM_ZONE B"
		   +" START WITH B.zone_id in (?)"
		   +" CONNECT BY PRIOR B.ZONE_ID = B.ZONE_PAR_ID) E,"
		   +" CS_BUSI_STEP_GENERAL a ,META_DIM_ZONE c"
		   +" where  a.region_id = c.zone_code and E.zone_code= a.region_id  AND c.Zone_Name=? and" 
		   +" a.month_id = ? and  a.busi_step1_id = ? and a.BUSI_STEP3_ID=?";
	Object[] p = new Object[5];
	p[0] =zoneId;
	p[1] =zoneName;
	p[2] =dateTime;
	p[3] =indId;
	p[4] =step3Id;
   return getDataAccess().queryForMap(sql,p);
	}

/**
  * 方法描述：
  * @param: 
  * @return: 
  * @version: 1.0
  * @author: Administrator
  * @version: 2013-4-2 下午05:42:07
  */
	public String getNewMonth(String indId) {
		String sql = "SELECT TO_DATE(MAX(t.MONTH_ID),'yyyy-MM') MONTH_ID FROM CS_BUSI_STEP_GENERAL t WHERE t.BUSI_STEP1_ID='"+ indId + "'";
		return getDataAccess().queryForString(sql);
	}
}
