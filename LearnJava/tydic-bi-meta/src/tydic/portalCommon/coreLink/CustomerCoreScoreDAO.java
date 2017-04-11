package tydic.portalCommon.coreLink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tydic.frame.common.utils.Convert;
import tydic.meta.common.MetaBaseDAO;

public class CustomerCoreScoreDAO extends MetaBaseDAO {
	
	public List<Map<String, Object>> getListMapData(Map<String, Object> queryData){
	      String dateTime = Convert.toString(queryData.get("dateTime")).replace("-", "");
	      String zoneId = Convert.toString(queryData.get("zoneId"));
          String sql="select a.BUSI_STEP_ID,a.BUSI_STEP_NAME,round(sum(a.BUSI_STEP_WEIGHT)*100,2) BUSI_STEP_WEIGHT,round(sum(a.STEP_OFFSET)*100,2) STEP_OFFSET,round(sum(a.CMPL_OFFSET)*100,2) CMPL_OFFSET,round(sum(a.TOTAL_OFFSET)*100,2) TOTAL_OFFSET,round(sum(a.STEP_SCORE)*100,2) STEP_SCORE,round(sum(a.REAL_SCORE),2) REAL_SCORE," +
          		"row_number() over(order by round(sum(real_score),2) desc) SORT_NUM"
                      +" from CS_BUSI_STEP_DETAIL a, META_DIM_ZONE b"
                      +" where a.region_id = b.zone_code"
                      +" and b.zone_id in (" + zoneId + ")"
                      +" and a.month_id="+dateTime
                      +" group by a.busi_step_id,a.busi_step_name"
                      +" order by to_number(a.BUSI_STEP_ID) asc";
		  
	     return getDataAccess().queryForList(sql);
	  }
	 
	
	public Map<String, Object> getChartData(Map<String, Object> queryData) {
		  Map<String, Object> map=new HashMap<String,Object>();
           List<Map<String,Object>> list=getListMapData(queryData);
           map.put("list", list);
           StringBuffer  chartMap=new StringBuffer();
           chartMap.append("<chart palette='1' caption='环节得分' decimals='2' showLabels='1' showvalues='0' " +
           		"numberSuffix='%' sYAxisValuesDecimals='2' connectNullData='0' PYAxisName='' " +
           		"SYAxisName='' numDivLines='4' formatNumberScale='0' labelDisplay='ROTATE' slantLabels='1' exportEnabled='1' exportFormats='PDF=输出为PDF|PNG=输出为PNG|JPEG=输出为JPEG'  exportFileName='报表图形'  exportAtClient='0'  exportAction='download' exportHandler='http://132.121.165.45:8081/tydic_generateReportsV1/FCExporterDirectPic.jsp' >\n");
		
           StringBuffer  temp1=new StringBuffer();
           temp1.append("<categories>\n");
           StringBuffer  temp2=new StringBuffer();
           temp2.append("<dataset seriesName='环节权重' color='AFD8F8' showValues='0'>\n");
           StringBuffer  temp3=new StringBuffer();
           temp3.append("<dataset seriesName='环节得分' color='F6BD0F' showValues='0' >\n");
           StringBuffer  temp4=new StringBuffer();
           temp4.append("<dataset seriesName='转换100分制得分' color='8BBA00' showValues='0' parentYAxis='S' renderAs = \"Line\">\n");
		for (Map<String, Object> key : list) {
				temp1.append("<category label='" + key.get("BUSI_STEP_NAME")+ "'/>\n");
				temp2.append("<set value='" + key.get("BUSI_STEP_WEIGHT") + "' />\n");
				temp3.append("<set value='" + key.get("STEP_SCORE") + "' />\n");
				temp4.append("<set value='" + key.get("REAL_SCORE") + "' />\n");
		}
           temp1.append("</categories>\n");
           temp2.append("</dataset>\n");
           temp3.append("</dataset>\n");
           temp4.append("</dataset>\n");
           
           chartMap.append(temp1).append(temp2).append(temp3).append(temp4);
		   chartMap.append("</chart>");
           //System.out.println("XML:="+chartMap.toString());
		   map.put("chartMap", chartMap.toString());
	       return map;
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
			String sql = "select to_date(max(t.month_id),'yyyy-MM') month_no from CS_BUSI_STEP_DETAIL t ";
			return getDataAccess().queryForString(sql);
	}
	
	
	
}
