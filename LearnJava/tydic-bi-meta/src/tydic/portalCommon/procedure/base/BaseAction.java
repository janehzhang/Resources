package tydic.portalCommon.procedure.base;

import java.util.Map;

/**
 * Action 基类
 */
public abstract class BaseAction
{
  
  private static final long serialVersionUID = 1L;
  
  //表格数据
  public abstract Map<String, Object> getTableData(Map<String, Object> param);
  
  //曲线图
  public abstract Map<String, Object>  bulidLineChart(Map<String, Object> param);
  
  //柱状图
  public abstract Map<String, Object>  bulidBarChart(Map<String, Object> param);
  
  public static String getLastMon(String currentMon) {
		String tempStr = currentMon.substring(4, currentMon.length());
		Integer retValue = 0;
		if ("01".equals(tempStr)) {
			retValue = Integer.parseInt(currentMon) - 89;
		} else {
			retValue = Integer.parseInt(currentMon) - 1;
		}
		return String.valueOf(retValue);
	}
}